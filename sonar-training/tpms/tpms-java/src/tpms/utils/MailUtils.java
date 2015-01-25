package tpms.utils;

import it.txt.afs.AfsCommonStaticClass;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

public class MailUtils extends AfsCommonStaticClass{
	
	private static String sendAttachment = "NO";
	private static TpmsConfiguration tpmsCfg = TpmsConfiguration.getInstance();
	
    public static void sendMailImpl(String strQuery){  
    	StringBuffer contentBuffer = new StringBuffer();
    	try {
	        String toAddress = tpmsCfg.getSupportMail();
	        String fromAddress = tpmsCfg.getMailFromAddress();
			String mailHost = tpmsCfg.getMailServerName(); 
	    	String subject = tpmsCfg.getLocalPlant() + ": Database connection error!" + strQuery;
	    	Vector toAddresses = new Vector();
			toAddresses.add(toAddress);
            toAddress = tpmsCfg.getSupportMail2();
            toAddresses.add(toAddress);
            toAddress = tpmsCfg.getSupportMail3();
            toAddresses.add(toAddress);
	     	contentBuffer.append("\n Local Plant :");
	     	contentBuffer.append(tpmsCfg.getLocalPlant());
	 		contentBuffer.append("\n\n There was an error in database connection\n\n");
	 		contentBuffer.append(strQuery);
	 		contentBuffer.append("\n\n Time Date : ");
			contentBuffer.append(new Date());
			String content = contentBuffer.toString();
			if (!fromAddress.equals("") && (!toAddresses.equals(""))){
				sendAttachment = "YES";
	        	MailUtils.sendMail(mailHost, fromAddress, toAddresses, subject, content);
	        	debugLog("MailUtils :: sendMailImpl : Email sent with subject: " + subject);
			}
	    } catch (Exception e) {
	    	errorLog("MailUtils :: sendMailImpl : strQuery = " + strQuery, e);
	    } finally {
	    	sendAttachment = "NO";
			try	{
				if (contentBuffer != null) {
					contentBuffer = null;
				}
			} catch (Exception e){
				errorLog("MailUtils :: sendMailImpl : Fail to delocate contentBuffer!", e);
			}
	    }
    } 
	
    public static void sendMail(String mailHost, String fromAddress, Vector toAddresses,
        String subject, String content) throws MessagingException {
    	Properties props = System.getProperties();
    	props.put("mail.smtp.host", mailHost);
    	Session session = Session.getDefaultInstance(props, null);
    	MimeMessage mimeMessage = new MimeMessage(session);
    	mimeMessage.setFrom(new InternetAddress(fromAddress));
    	
    	String toAddress = "";
    	Iterator iterator = toAddresses.iterator();
    	
    	while (iterator.hasNext()) {
    		toAddress = (String) iterator.next();
    		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
    	} // while (iterator ..
    	
    	mimeMessage.setSubject(subject);
    	Multipart mp = new MimeMultipart();
    	MimeBodyPart mbp1 = new MimeBodyPart();
    	mbp1.setText(content);
    	mp.addBodyPart(mbp1);
    	
    	if (sendAttachment.equals("YES")) {
    		MimeBodyPart mbp2 = new MimeBodyPart();
    		String inFilename = tpmsCfg.getWebAppDir() + "/" + "logs" + "/" + "tpms.log";
    		String outFilename = tpmsCfg.getWebAppDir() + "/" + "logs" + "/" + "tpms.zip";
    		String FileName = tpmsCfg.getWebAppDir() + "/" + "logs" + "/" + "tpms.zip";
    		byte[] buf = new byte[1024];
    		try {
    			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
    			FileInputStream in = new FileInputStream(inFilename);
//    			out.putNextEntry(new ZipEntry(inFilename));
    			out.putNextEntry(new ZipEntry("tpms.log"));
    	        // Transfer bytes from the file to the ZIP file
    	        int len;
    	        while ((len = in.read(buf)) > 0) {
    	            out.write(buf, 0, len);
    	        }
    	        // Complete the entry
    	        out.closeEntry();
    	        in.close();
    	        // Complete the ZIP file
    	        out.close();
    			
    			FileDataSource fds = new FileDataSource(FileName);
    			mbp2.setDataHandler(new DataHandler(fds));
    			mbp2.setFileName(fds.getName());
    			mp.addBodyPart(mbp2);
    		} catch (IOException e) {
    			errorLog("MailUtils :: sendMail : Fail to zip!", e);
    		}
    	}
    	
    	mimeMessage.setContent(mp);
		mimeMessage.setSentDate(new Date());
    	Transport.send(mimeMessage);
    }  // sendMail()
    
}  // class MailUtils

