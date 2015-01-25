package it.txt.tpms.tplib;

import java.util.Vector;
import tpms.utils.MailUtils;
import tpms.utils.TpmsConfiguration;
import it.txt.tpms.tplib.actions.TPLibAction;
import it.txt.tpms.tplib.actions.TPLibDeleteAction;
import it.txt.tpms.tplib.actions.TPLibProductionAction;
import it.txt.tpms.tplib.logger.Logger;
import it.txt.tpms.tplib.xml.XmlReader;
import it.txt.tpms.tplib.xml.XmlValidator;

public class TPLibProcess{
	/* This class is activated through /tpms/scripts/ExecFiles/tpmsw_tp_lib 
	 * java_command="$JAVACMD -Dpid=$$ -cp 
	 * xercesImpl.jar:tpms.jar:xml-apis.jar:epwy.jar:oracle.jar:rowset.jar:activation.jar:mail.jar:jaxp.jar:parser.jar 
	 * it.txt.tpms.tplib.TPLibProcess 
	 * $XML_FILE tar $myname"
	 */   
	private static final String TPMS_DIR = "/tpms/webtpms/";

	private TPLibAction tpAction;
	private XmlReader parser;
	private String packageExtension;
	private String daemonName;
	private String xmlFile;

	public void setXmlFile(String XmlFile) {
		xmlFile = XmlFile;
	}

	public String getXmlFile() {
		return xmlFile;
	}
	
	public static void main(String[] args) throws Exception{
		TPLibProcess process = new TPLibProcess();
		process.run(args);
	}
	
	/* Setting of System properties not activated through shell script calling */
	static {
		System.setProperty("tomcat.home","/tpms/webtpms/../..");
		System.setProperty("webappname","tpms");
	}
	
	public static final String LOCAL_PLANT = TpmsConfiguration.getInstance().getLocalPlant();
	public static final String MAIL_SERVER = TpmsConfiguration.getInstance().getMailServerName();
	public static final String FROM_ADDRESS = TpmsConfiguration.getInstance().getMailFromAddress();
	public static final String SUPPORT_EMAIL = TpmsConfiguration.getInstance().getSupportMail();	
	
	public void run(String[] args){
		
		/* Note the argument for tar/zip is not needed for Java code and can be removed 
		 * in future. It is handled by C-code to check whether it is present and also 
		 * if it is zip or tar
		 */
		if (args.length < 3) {
			Logger.reportError("Too few parameters for the call: it is necessary to specify the xml file name, package file extension and daemon name!");
		} else {
			Logger.debug("Received parameters --> xml file name:" + args[0] + " -package file extension:" + args[1] + " -daemon name:" + args[2]);
		}
		setXmlFile(args[0]);
		packageExtension = args[1];
		daemonName = args[2];
		parser = new XmlReader(getXmlFile());
		boolean parserResult = parser.parse();
		
		/* Check if xml file can be parsed */
		if (!parserResult) {
			Logger.error("TPLibProcess :: run : Error parsing the xml file " + getXmlFile() + "!");
			String subject = mailSubject(": TPLib Error!");
			String content = mailContent("TPLibProcess :: run : Error parsing the xml file!\n");
			sendMail(subject,content);
			Logger.reportError("TPLibProcess :: run : Parser message : " + parser.getMessage());
		} 
		Logger.debug("TPLibProcess :: run : ...parserResult OK");

        /* Check if xml file can be validated */
		if (!validateTPLibXmlFile()) {
			Logger.error("TPLibProcess :: run : Error validating the xml file " + getXmlFile() + "!");	
			String subject = mailSubject(": TPLib Error!");
			String content = mailContent("TPLibProcess :: run : Error validating the xml file!\n");
			sendMail(subject,content);
			Logger.reportError("TPLibProcess :: run : failed to validate xml file!");
		}
		Logger.debug("TPLibProcess :: run : validateTPLibXmlFile OK");
		
		/* Check if TP_IN_TPLIB=[RELEASED|DELETED] */
		String tpInTpLib = parser.getValue("TP_IN_TPLIB");

		/*
		if(tpInTpLib == null){
			Logger.reportError("The field TP_IN_TPLIB is not found in the xml file.");
		}
        */
			
		if (tpInTpLib.equalsIgnoreCase("Released")) {
			tpAction = new TPLibProductionAction();
			Logger.debug("TPLibProcess :: run : TP_IN_TPLIB='RELEASED'");
		} else if (tpInTpLib.equalsIgnoreCase("Deleted")) {
			tpAction = new TPLibDeleteAction();
			Logger.debug("TPLibProcess :: run : TP_IN_TPLIB='DELETED'");
		}
		
		if (tpAction != null) {
			doAction();
		} else { 
			String subject = mailSubject(": TPLib Error!");	
			String content = mailContent("The value of the field TP_IN_TPLIB in the xml file is incorrect: it must " +
					"be equals to <RELEASED|DELETED>, and it is not case sensitive.\n");		
    		sendMail(subject,content);
			Logger.reportError("The value of the field TP_IN_TPLIB in the xml file is incorrect: it must " +
			"be equals to <RELEASED|DELETED>, and it is not case sensitive.");
		}
		Logger.debug("TPLibProcess :: run : ...tpAction OK");
	}

	private void doAction(){
		String sid = System.getProperty("pid");
		String reqId = sid + "_" + Long.toString(System.currentTimeMillis());
		String inFileName = TPMS_DIR + "cc_in_out/" + reqId + tpAction.getActionName() + "_in";
		String outFileName = TPMS_DIR + "cc_in_out/" + reqId + tpAction.getActionName() + "_out";

		tpAction.setReader(parser);
		tpAction.setTpmsDir(TPMS_DIR);
		tpAction.setSid(sid);
		tpAction.setInFileName(inFileName);
		tpAction.setOutFileName(outFileName);
		tpAction.setPackageExtension(packageExtension);
		tpAction.setDaemonName(daemonName);
		
		if(!tpAction.writeBackEndInFile()){
			Logger.reportError("Could not create the input file for the back-end action!");
		} else {
			Logger.debug("TPLibProcess :: doAction : ...writeBackEndInFile OK.");
		}

		int exitCode = tpAction.execute();
		boolean backEndOutOk = tpAction.readBackEndOut();

		/* For UTC, set 1 => Backend action passed */
		if (exitCode == 0) { 
			if (backEndOutOk) {
				Logger.debug("TPLibProcess :: doAction : *** BACK-END ACTION OK ***");
				if (tpAction.updateDb()) {
					Logger.debug("TPLibProcess :: doAction : DB update successfully");
				} else {
					Logger.error("TPLibProcess :: doAction : DB update failed");
				}
			} else {
				Logger.reportError("TPLibProcess :: doAction : DB Update skipped.");
			}
			/* delete if exitCode is 0 */
			tpAction.deleteInOut(); 
		} else {
			String subject = mailSubject(": TPLIB backend action failure!");
			StringBuffer contentBuffer = new StringBuffer("*** BACK-END ACTION EXITED WITH CODE " + exitCode);
			contentBuffer.append("\ninFileName: " + inFileName);
			contentBuffer.append("\noutFileName: " + outFileName);
			contentBuffer.append("\nRelated File : " + getXmlFile());
			contentBuffer.append("\nLook in user log directory for more details.");
			String content= contentBuffer.toString();;
			contentBuffer = null;
    		sendMail(subject,content);
    		Logger.reportError("TPLibProcess :: doAction : BACK-END ACTION EXITED WITH CODE " + exitCode);
		}	
	}

	private boolean validateTPLibXmlFile(){
    	
		XmlValidator validator = new XmlValidator();
		validator.setFieldsToValidate(parser.getResult());
		validator.addFieldNames(new String[]{
				"JOB_NAM",
				"JOB_REV",      //release
				"JOB_VERSION",  //revision
				"TPMS_JOB_VER", //version
				"HOSTNAME",
				"VOB",
				"TIMESTAMP",
				"TP_IN_TPLIB"
		});

		if (!validator.validate()) {
			Logger.error("*** The given XML file is not compliant with the TPLibrary interaction: ***");
			Logger.error(validator.getReport());
			return false;
		} else 
			return true;
	}
	
	public static String mailSubject(String mailSubject){
		StringBuffer subjectBuffer = new StringBuffer();	
		if (LOCAL_PLANT != null) 
			subjectBuffer.append(LOCAL_PLANT + ", ");
		else 
			subjectBuffer.append("Unknown Plant, ");
		try {
			String hostAddress = java.net.InetAddress.getLocalHost().getHostAddress();
			subjectBuffer.append("IP=" + hostAddress);
		} catch (java.net.UnknownHostException ex) {
			subjectBuffer.append("Unknown Host, ");
			Logger.error("TPLibActon :: mailSubject : Unknown Host!" + ex.getMessage());
		}
		subjectBuffer.append(mailSubject);
		String subject = subjectBuffer.toString();;
		subjectBuffer = null;
		return subject;
	}
	
	private String mailContent(String mailContent){
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("*** The given XML file is not compliant with the TPLibrary interaction! ***\n");
		contentBuffer.append(mailContent);
		contentBuffer.append("Action : Request TPLIB to send again with the correct syntax!\n");
		contentBuffer.append("Related File : " + getXmlFile());
		String content= contentBuffer.toString();;
		contentBuffer = null;
		return content;
	}	
	
	public static void sendMail(String subject, String content) {
		Vector toAddress = new Vector();
    	toAddress.add(SUPPORT_EMAIL);
		try {
			if (MAIL_SERVER != null && FROM_ADDRESS != null && toAddress != null) {
				MailUtils.sendMail(MAIL_SERVER, FROM_ADDRESS, toAddress, subject, content);
				Logger.debug("TPLibProcess :: sendMail : Email sent with subject = " + subject);
			}
			else {
				Logger.debug("TPLibProcess :: sendMail : mailServer, fromAddress or toAddress not defined correctly in WEB.XML!");
			}
		}
		catch (Exception ex) {
			Logger.error("TPLibProcess :: sendMail : TP Insert in TPLib Error! Failed to send mail to administrator!");
		}
	}
}
