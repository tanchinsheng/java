package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import javax.servlet.ServletException;

import java.util.Properties;

import tpms.utils.TpmsConfiguration;
import tpms.CtrlServlet;
import tpms.FacilityMgr;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-lug-2006
 * Time: 16.24.28
 * This servlet is been called during the web pplication start up by tomcat
 */
public class InitServlet extends AfsGeneralServlet {
    public void init() throws ServletException {
        System.out.println("TPMS/W web application initialization started");
        //initialize log4j logs
        initLogging();
        //initialize FacilityManager
        FacilityMgr.setFacilityRoot(CtrlServlet._facilityFileName);
        System.out.println("TPMS/W web application initialization ended");
    }


    /**
     * log4j initialization: this method due to a call to a singlethone that contains configuration parameters initialize
     * the whole set of configuration paraters too.
     */
    private void initLogging() {
        if (tpmsConfiguration == null) tpmsConfiguration = TpmsConfiguration.getInstance();
        Properties loggerProps = tpmsConfiguration.getProperties("log4j.");
        if (loggerProps.size() == 0) {
            BasicConfigurator.configure();
            Logger.getRootLogger().info("TPMS/W Log4j: default configuration");
            System.out.println("TPMS/W Logs configuration not found: default configuration is been loaded");
        } else {
            PropertyConfigurator.configure(loggerProps);
        }
    }
}
