/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 28-set-2005
 * Time: 12.52.15
 *
 */
package tpms.utils;


import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.GeneralStringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 28-set-2005
 * Time: 12.48.38
 * the main purpose of this class is to provide an access to tpms configuration to all class that need it
 * the problem is that all the configuration are contained in web.xml file ( yes it's not right but it is...)
 */

public class TpmsConfiguration {
    private static TpmsConfiguration ourInstance = null;
    private static Properties props = new Properties();

    private static final String DEFAULT_WEBAPP_NAME = "tpms";

    private static final String APPLICATION_CONFIGURATION_ROOT_PATH = System.getProperty("tomcat.home") + File.separator + "webapps" + File.separator + (GeneralStringUtils.isEmptyString(System.getProperty("webappname")) ? DEFAULT_WEBAPP_NAME : System.getProperty("webappname"));

    //path to web.xml in j2ee envirnoment
    private static String XML_PROPERTIES_FILE = APPLICATION_CONFIGURATION_ROOT_PATH + File.separator + "WEB-INF" + File.separator + "web.xml";

    private static final String DB_PASSWORD_CONFIGURATOR = "DBPasswordConfigurator";

    private static int DB_COMMENT_FIELD_MAX_LENGTH = -1;

    private static final String COMMON_CONFIGURATION_FILES_PATH = "cfg/local_cfg";

    /**
     *
     * @return singlethon
     */
    public static TpmsConfiguration getInstance() {
        if (ourInstance == null) {
            ourInstance = new TpmsConfiguration(null);
        }
        return ourInstance;
    }
    private String sessionId;
    private String userName;
 
    public void setSessionId(String sessionId){it.txt.afs.AfsCommonStaticClass.debugLog("SessionSingleTon sessionId : "+sessionId);
        this.sessionId = sessionId;
    }

    public String getSessionId(){
        return this.sessionId;
    }

  	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}   
    /**
     *
     * @param absPathToXmlPropsFile
     * @return singlethon
     */
    /**
     * this method is commented out in order to make the access to the singlethon only using the getIstance() method
    public static TpmsConfiguration getInstance(String absPathToXmlPropsFile) {
        if (ourInstance == null) {
            ourInstance = new TpmsConfiguration(absPathToXmlPropsFile);
        }
        return ourInstance;
    }
     */

    /**
     * Private constructor of the singlethon
     * @param xmlPropertiesFilePath the xml file (web.xml) with the configuration content.
     */
    private TpmsConfiguration(String xmlPropertiesFilePath) {
        //add all the properties given in input in tomcat start command
        props = new Properties(System.getProperties());
        //add all the properties from the configuration file
        File xmlPorpertiesFile = null;
        if (xmlPropertiesFilePath != null) {
            xmlPorpertiesFile = new File(xmlPropertiesFilePath);
        }
        loadXmlPropertiesFile(xmlPorpertiesFile);
        //add all the properties that comes from the TOMCAT environment
        loadEnvironmentProperties();

    }

/*
    private static void loadPropertiesFile(String filePropertiesPath) {
        try {

            if (GeneralStringUtils.isEmptyString(filePropertiesPath))
                filePropertiesPath = PROPERTIES_FILE;

            File propertiesFile = new File(filePropertiesPath);
            if (!propertiesFile.exists()) {
                throw new FileNotFoundException("Properties file not found: " + propertiesFile.getAbsolutePath());
            }
            InputStream inStream = new FileInputStream(propertiesFile);
            props.load(inStream);
            inStream.close();

        } catch (Exception e) {
            System.err.println("ERRORE LETTURA CONFIGURAZIONE: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/

    /**
     * this method look in web.xml file for context parameters and load them into props wich will be accessibel to all
     * class (also the ones that doesn't extend Servlet)
     *
     * @param f the web.xml file to be parsed if is null it tries to load the one defined in XML_PROPERTIES_FILE costant
     */
    private static void loadXmlPropertiesFile(File f) {

        if (props == null) props = new Properties();
        //retrieve the properties given in input to the tomcat virtual machine (i.e. -Dtomcat.home=...)
        Properties systemProperties = System.getProperties();
        if (systemProperties != null) {
            Enumeration sysPropsKeys = systemProperties.keys();
            String currentKey;
            while (sysPropsKeys.hasMoreElements()) {
                currentKey = (String) sysPropsKeys.nextElement();
                props.setProperty(currentKey, systemProperties.getProperty(currentKey));
            }
        }
        //retrive the properties from the web.xml file.
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        if (f == null || !f.exists()) {
            //if the given file is null or doesn't exists try with the default one
            f = new File(XML_PROPERTIES_FILE);
        }

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("TpmsConfiguration :: loadXmlPropertiesFile : ParserConfigurationException unable to parse xmlConfigFile = " + f.getAbsolutePath());
            e.printStackTrace(System.err);
        }

        Document doc = null;
        try {
            doc = documentBuilder.parse(f);
        } catch (SAXException e) {
            System.err.println("TpmsConfiguration :: loadXmlPropertiesFile : SAXException unable to parse xmlConfigFile = " + f.getAbsolutePath());
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println("TpmsConfiguration :: loadXmlPropertiesFile : IOException unable to parse xmlConfigFile = " + f.getAbsolutePath());
            e.printStackTrace(System.err);
        } catch (NullPointerException e) {
            System.err.println("TpmsConfiguration :: loadXmlPropertiesFile : NullPointerException unable to parse xmlConfigFile = " + f.getAbsolutePath());
            e.printStackTrace(System.err);
        }

        if (doc != null) {
            Element xmlProps = doc.getDocumentElement();
            NodeList nl = xmlProps.getElementsByTagName("context-param");
            if (nl != null && nl.getLength() > 0) {
                Element oneConfigParameter;
                for (int i = 0; i < nl.getLength(); i++) {
                    //get the context-param element
                    oneConfigParameter = (Element) nl.item(i);
                    String paramName = XmlUtils.getTextValue(oneConfigParameter, "param-name");
                    String paramValue = XmlUtils.getTextValue(oneConfigParameter, "param-value");
                    props.setProperty(paramName, paramValue);
                }
            }
            props.setProperty(DB_PASSWORD_CONFIGURATOR, "TPMSPasswordConfig");
        } else {
            System.err.println("TpmsConfiguration :: doc is null. please check the following file " + f.getAbsolutePath());
        }
    }

    private static boolean loadEnvironmentProperties() {
        if (props == null) props = new Properties();
        String OSName = System.getProperty("os.name").toLowerCase();
        Process process;
        Runtime runtime = Runtime.getRuntime();
        try {
            if (OSName.indexOf("windows 9") > -1) {
                process = runtime.exec("command.com /c set");
            } else if ((OSName.indexOf("nt") > -1) || (OSName.indexOf("windows") > -1)) {
                process = runtime.exec("cmd.exe /c set");
            } else {
                process = runtime.exec("env");
            }
            BufferedReader br = new BufferedReader (new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                int idx = line.indexOf('=');
                String key = line.substring(0, idx);
                String value = line.substring(idx + 1);
                props.setProperty(key, value);
            }
        } catch (Exception e) {
            System.err.println("TpmsConfiguration :: unable to load environment properties. OS Name = " + OSName);
            e.printStackTrace(System.err);
            return false;
        }

        return true;
    }




    /**
     * ************TPMS GENERAL CONFIGURATION PARAMETERS****************************************
     */


    public int getMaxDBCommentsSize(){
        if (DB_COMMENT_FIELD_MAX_LENGTH < 0) {
            DB_COMMENT_FIELD_MAX_LENGTH = QueryUtils.retrieveStringDBColumnSize("TP_COMMENTS", "COMMENT_BODY");
        }
        return DB_COMMENT_FIELD_MAX_LENGTH;
    }

    public String getCommonConfigurationFilesAbsolutePath(){
        return this.getWebAppDir() + File.separator + COMMON_CONFIGURATION_FILES_PATH;
    }


    public boolean isTpmsdEnabled(){
        return !props.getProperty("tpmsd_mode", "none").equalsIgnoreCase("none");
    }


    public String getAfsDatesFormat(){
        return props.getProperty("afsDatesFormat");
    }

    public String getTomcatHome() {
        return props.getProperty("tomcat.home");
    }

    public String getSoftwareVersion() {
        return props.getProperty("SW_VER");
    }

    public String getUnixScriptExecMode() {
        return props.getProperty("unixScriptExecMode");
    }

    public boolean getDebug() {
        return (props.getProperty("debug").equalsIgnoreCase("true"));
    }

    public String getCCDebug() {
        return props.getProperty("cc_debug");
    }

    public String getTempFileLifeTime() {
        return props.getProperty("tempFileLifeTime");
    }

    public int getNDbConns() {
        return Integer.parseInt(props.getProperty("nDbConns"));
    }

    public int getSeed() {
        return Integer.parseInt(props.getProperty("seed", "3"));
    }

    public String getDvlUnixWebAppDir() {
        return props.getProperty("dvlUnixWebAppDir");
    }

    public boolean getDvlBool() {
        return (props.getProperty("dvlBool").equalsIgnoreCase("true"));
    }

    public String getDvlBoolAsString() {
        return props.getProperty("dvlBool");
    }
    public String getDvlUnixHost() {
        return props.getProperty("dvlUnixHost");
    }

    public String getDvlCcInOutDir() {
        return props.getProperty("dvlCcInOutDir");
    }

    public String getCcScriptsDir() {
        return props.getProperty("ccScriptsDir");
    }

    public String getCcInOutDir() {
        return props.getProperty("ccInOutDir");
    }

    public String getWebAppDir() {
        return props.getProperty("webAppDir");
    }

    public String getTpmsInstName() {
        return props.getProperty("TpmsInstName");
    }

    public String getSupportMail() {
        return props.getProperty("supportMail");
    }
    
    public String getSupportMail2() {
        return props.getProperty("supportMail2");
    }
    public String getSupportMail3() {
        return props.getProperty("supportMail3");
    }
    
    public String getLocalPlant() {
        return props.getProperty("localPlant");
    }
    public String getMailServerName() {
        return props.getProperty("mailServerName");
    }
    public String getMailFromAddress() {
        return props.getProperty("mailFromAddress");
    }
    public String getTxtFilesExtList() {
        return props.getProperty("txtFilesExtList");
    }

    public String getDbTrackLogDir() {
        return props.getProperty("dbTrackLogDir");
    }
    
    public String getEmailTrackLogDir() {
        return props.getProperty("emailTrackLogDir");
    }

    public String getLogDateFormat() {
        return props.getProperty("logDateFormat", "dd/MM/yyyy hh:mm:ss : ");
    }

    public boolean isRemoteInterfaceEnabled() {
        return props.getProperty("steps.interface.active", "0").equals("1");
    }

    public String getCommonEngineerLogin() {
        return props.getProperty("commonEngineerLogin");
    }

    public String getDBPasswordConfigurator() {
        return props.getProperty(DB_PASSWORD_CONFIGURATOR);
    }


    public String getClearcaseCommandExecutionHost() {
        return props.getProperty("clearcaseCommandExecutionHost");
    }

    public long getAfsTimeOut() {
        return Long.parseLong(props.getProperty("afsTimeOut"));
    }

    public String getLdapFullServerName() {
        return props.getProperty("ldap.full.server.name");
    }

    public String getLdapServerPort() {
        return props.getProperty("ldap.server.port");
    }

    public String getLdapSearchBase() {
        return props.getProperty("ldap.search.base");
    }

    public String getLdapDriver() {
        return props.getProperty("ldap.driver");
    }

    public String getLinesetPackageIncomingDirectory() {
        return props.getProperty("lineset.packages.incoming.bay");
    }

    /**
     * ************GENERAL TRANSLATION METHODS ON PROPS VALUES****************************************
     */

    public String getCCDebugClearcaseInterfaceValue() {
        return (ourInstance.getCCDebug().equalsIgnoreCase("true") ? "1" : "0");
    }

    /**
     * ************GENERAL METHODS ON PROPS OBJECT****************************************
     */

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getProperty(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    public long getProperty(String key, long defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        } else {
            return Long.parseLong(value);
        }
    }


    /**
     * this method return all porperties that starts with prefix
     *
     * @param prefix
     * @return return all porperties that starts with prefix
     */

    public Properties getProperties(String prefix) {
        Properties subProps = new Properties();
        Enumeration Enum = props.keys();
        while (Enum.hasMoreElements()) {
            String key = (String) Enum.nextElement();
            if (key.startsWith(prefix)) {
                subProps.setProperty(key, props.getProperty(key));
            }
        }
        return subProps;
    }

    public boolean isTPLibAvailable(){
    	String prop = props.getProperty("TPLib_AVAILABLE");
    	AfsCommonStaticClass.debugLog("TpmsConfiguration::isTPLibAvailable: " + prop);

    	if(prop != null){
    		return prop.equalsIgnoreCase("Y");
    	}

    	return false;
    }
}
