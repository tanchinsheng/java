package com.st.mcc.rtm.manager;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.st.ccam.framework.mail.CCAMMail;
import com.st.ccam.framework.mail.CCAMMailServices;
import com.st.mcc.rtm.bean.BeanMail;
import com.st.mcc.rtm.util.exception.RTMException;
import com.st.mcc.rtm.util.exception.TechnicalException;

public class StMailManager {

	static Logger _logger = Logger.getLogger(Cfg.class.getName());
	
    // Constants
    private static final String CS_COMPLETECLASSNAME = StMailManager.class.getName();
    private static final String CS_CLASSNAME = CS_COMPLETECLASSNAME.substring(CS_COMPLETECLASSNAME.lastIndexOf(".") + 1);
    public static final String MANAGER_NAME = "MailManager";

    // Static instance of the StConnectionManager
    private static StMailManager mo_mailManager = null;

    static
    {
        init();
    }

    private static synchronized void init()
    {
        if (mo_mailManager == null)
        {
            mo_mailManager = new StMailManager();
        }
    }

    private CCAMMailServices mo_realMailManager = null;

    // support email addresses
    private Vector mo_qdmsSupportEmail = null;

   // Application email address
    private String mo_qdmsApplicationEmail = "";

    // Application email address
    private String mo_qdmsSubject = "";
   
    // Constructor
    private StMailManager() {
    	try
    	{
    		mo_realMailManager = new CCAMMailServices();
    		mo_realMailManager.setSendServerHost(Cfg.getProperty("rtmloader_conf.email.serverHost"));
    		mo_realMailManager.setSendServerProtocol(Cfg.getProperty("rtmloader_conf.email.serverProtocol"));
    		
    		mo_qdmsApplicationEmail = Cfg.getProperty("rtmloader_conf.email.applicationEmailAddress");
    		mo_qdmsSubject = Cfg.getProperty("rtmloader_conf.email.emailSubject");
    		
    		/* Read list of support email addresses */
    		mo_qdmsSupportEmail = new Vector();
    		String supportEmailStr = Cfg.getProperty("rtmloader_conf.email.supportEmailAddress");
    		StringTokenizer st = new StringTokenizer(supportEmailStr, ", ");
    		while (st.hasMoreTokens()) {
    			mo_qdmsSupportEmail.add(st.nextToken());
    		}
    	}
    	catch (Exception e)
    	{
    		_logger.debug("Error while instanciating MailManager.");
    	}      
    }

    public synchronized static void refresh()
    {
        mo_mailManager = null;
        init();
    }

    // Accessor
    public static StMailManager getInstance()
    {
        return mo_mailManager;
    }

    // Instance functions
    public void sendMail(BeanMail po_beanMail) throws TechnicalException
    {
        CCAMMail mail = new CCAMMail();
        for (int i=0; i < mo_qdmsSupportEmail.size(); i++) {
        	mail.addTo((String)mo_qdmsSupportEmail.get(i));
        }
        
        mail.setFrom(mo_qdmsApplicationEmail);
        
        mail.setSubject(mo_qdmsSubject);
        mail.setMessage(po_beanMail.getEmailText());

        _logger.debug("sender: " + mail.getFrom());
        _logger.debug("to: " + mail.getToAsString());
        _logger.debug("subject: " + mail.getSubject());
        _logger.debug("message: " + mail.getMessage());

        try
        {
        	_logger.debug("before used of CCAMMailServices");
            mo_realMailManager.sendMail(mail);
            _logger.debug("after used of CCAMMailServices");
        }
        catch (MessagingException me)
        {
            throw new TechnicalException(CS_CLASSNAME, "An error occured while trying to send an email", me);
        }
        
        mail = null;
    }
    
    /**
     * Email notification for RTM loading error
     * @param triggerFileName: trigger filename
     * @param patEx: error detail
     */
    public static synchronized void emailRTMLoadError(String triggerFileName, RTMException rtmEx) {
		
    	_logger.info("Email notification for RTM loading error for trigger file " + triggerFileName + "...");
    	
    	try {
			StMailManager mailManager = StMailManager.getInstance();
			BeanMail mail = new BeanMail();
			mail.setEmailText("RTM report files' loading fails with the following detail: \n\n" + 
					"- Trigger file: " + triggerFileName + "\n\n" +
					"- Error code  : " + rtmEx.getErrorCode() + "\n\n" +
					"- Error text  : " + rtmEx.getErrorText());
			mailManager.sendMail(mail);
			
		} catch (Exception e) {
			_logger.error(e.toString(), e);
		}

    }    
}