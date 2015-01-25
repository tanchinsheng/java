package tpms.utils;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.CoolString;
import it.txt.afs.AfsCommonStaticClass;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tol.oneConnDbWrtr;

import tpms.CtrlServlet;
import tpms.TpmsException;
import tpms.utils.MailUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;



/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 2-feb-2006
 * Time: 17.18.08
 * To change this template use File | Settings | File Templates.
 */
public class QueryUtils extends AfsCommonStaticClass {
	
    public static final String ORACLE_DATE_FORMAT = "DDMMYYYYHH24MISS";
    public static final String JAVA_DATE_FORMAT = "ddMMyyyyHHmmss";
    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JAVA_DATE_FORMAT);

    public static Date parseDbDate (String date) {
        Date result = null;
        if (!GeneralStringUtils.isEmptyString(date)){
            try {
                result = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                errorLog("QueryUtils :: parseDbDate : error while parsing " + date, e);
            }
        }
        return result;
    }
  
    /**
     * Usefull to prepare Strings for query insertion
     * @param s
     * @return the string given in input with all occurrencies of quetes (') duplicated.
     */
    public static String duplicateQuotes (String s) {
        if (!GeneralStringUtils.isEmptyString(s)) {
            CoolString cs = new CoolString(s);
            cs.replaceAll("'", "''");
            s = cs.toString();
        }
        return s;
    }

  
    /**
     * return the given string ready for the contatenation in a string that will be a query
     *
     * @param s a string that should be prepared for db insertion
     * @return a string ready for the contatenation in a string that will be a query
     */
    public static String getStringValueForQuery(String s) {
        if (GeneralStringUtils.isEmptyString(s))
            return "null";
        else
            return "'" + duplicateQuotes(s) + "'";
    }

    /**
     * return the given date in a format that could be inserted in Oracle
     *
     * @param d the date to be parsed
     * @return the date for oracle operations
     */
    public static String getDateForQuery(Date d) {
        return (d == null) ? "null" : "TO_DATE('" + simpleDateFormat.format(d) + "', '" + ORACLE_DATE_FORMAT + "')";
    }

    /**
     * @param dbAttributeName
     * @param d
     * @return a string representing a date that can be added to where condition that can be added to the where condition
     *         in order to retrieve records that have dbAttributeName greater equal than d.
     */
    public static String getDateForQueryGreaterEqualThan( String dbAttributeName, Date d) {
        return getDateForQueryGreaterThan(dbAttributeName, d, true);
    }

    /**
     * Return
     * @param dbAttributeName
     * @param d
     * @param equal
     * @return a string representing a date taht can be added to where condition that can be added to the where condition
     *         in order to retrieve records that have dbAttributeName greater than d, if equal is true the clasue will be greater equal (strictly greater otherwise).
     */
    public static String getDateForQueryGreaterThan( String dbAttributeName, Date d, boolean equal ) {
         return dbAttributeName + ">" + (equal ? "=": "") + getDateForQuery(d) + " ";
    }

    /**
     * @param dbAttributeName
     * @param d
     * @return a string representing a date that can be added to where condition that can be added to the where condition
     *         in order to retrieve records that have dbAttributeName less equal than d.
     */
    public static String getDateForQueryLessEqualThan( String dbAttributeName, Date d ) {
        return getDateForQueryLessThan(dbAttributeName, d, true);
    }

    /**
     *
     * @param dbAttributeName
     * @param d
     * @param equal
     * @return a string representing a date taht can be added to where condition that can be added to the where condition
     *         in order to retrieve records that have dbAttributeName less than d, if equal is true the calsue will be less equal (strictly less otherwise).
     */
    public static String getDateForQueryLessThan( String dbAttributeName, Date d, boolean equal ) {
         return dbAttributeName + "<" + (equal ? "=": "") + getDateForQuery(d) + " ";
    }


    /**
     *
     * @param dbAttributeName
     * @param from
     * @param to
     * @param equalFrom
     * @param equalTo
     * @return return the where condition that can be added to the where condition in order to retrieve records taht have dbAttributeName between from and to dates
     */
    public static String getDateInInterval(String dbAttributeName, Date from, Date to, boolean equalFrom, boolean equalTo) {
        String result = "";
        if (from != null) {
            result = result + dbAttributeName + ">" + (equalFrom ? "=": "") + getDateForQuery(from) + " ";
        }
        if (to != null){
            if (GeneralStringUtils.isEmptyString(result)){
                result = result + dbAttributeName + "<" + (equalTo ? "=": "") + getDateForQuery(to) + " ";
            } else {
                result = result + " and "+ dbAttributeName + "<" + (equalTo? "=": "") + getDateForQuery(to) + " ";
            }
        }
        return result;
    }

    public static String getDateInInterval(String dbAttributeName, Date from, Date to) {
        return getDateInInterval(dbAttributeName, from, to, true, true);
    }


    public static SimpleDateFormat getDBSimpleDateFormat(){
        return simpleDateFormat;
    }

    public static oneConnDbWrtr getDbConnection() throws ParserConfigurationException, SAXException, IOException, SQLException {
        if (CtrlServlet.dbWriter == null) {
            retrieveDbConnectionInfo();
            CtrlServlet.dbWriter = new oneConnDbWrtr(dbConnectionString, dbUsername, dbPassword);
        }
        return CtrlServlet.dbWriter;
    }

    private static String dbConnectionString = null;
    private static String dbUsername = null;
    private static String dbPassword = null;


    private static void retrieveDbConnectionInfo() throws ParserConfigurationException, SAXException, IOException {
        if (GeneralStringUtils.isEmptyString(dbConnectionString) || GeneralStringUtils.isEmptyString(dbUsername)
                || GeneralStringUtils.isEmptyString(dbPassword)) {
            TpmsConfiguration tpmsConfiguration = TpmsConfiguration.getInstance();
            String plantInitDir = tpmsConfiguration.getWebAppDir() + File.separator + "cfg" + File.separator + "local_cfg";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(plantInitDir + File.separator + "plants.xml"));
            Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
            dbUsername = XmlUtils.getTextValue(dbInfoElement, "USERNAME");
            dbPassword = XmlUtils.getTextValue(dbInfoElement, "PASSWORD");
            dbPassword = StringMasking.decriptString(dbPassword);
            dbConnectionString = XmlUtils.getTextValue(dbInfoElement, "CONN_STR");
        }
    }

    /**
     * making a simple select query checks if the given db connection is valid or not
     * @param dbConnection db connection to be tested
     * @return true if the connection is good, false otherwise
     */
    public static boolean checkDbConnection(oneConnDbWrtr dbConnection) {
        boolean result = false;
        int dbConnectionCheckInt = 1;
        String query = "select " + dbConnectionCheckInt + " as TEST_VALUE from dual";
        CachedRowSet rs = null;
        
        if (dbConnection != null){
        	debugLog("Queryutils ::  checkDbConnection : dbConnection not null.");
        	boolean notConnected = true;
        	// try to connect db three times
        	for(int i=0;(notConnected && i<3);i++) {
        		try {
            		dbConnection.checkConn();
            		notConnected = false;
            	}catch (SQLException se) {            		
            		errorLog("QueryUtils :: checkDbConnection : can not reconnect to database. " ,se);
            		notConnected = true;            		
            	}            	
        	}
        	if(notConnected == true) {        		
        		MailUtils.sendMailImpl("checkDbConnection can't reconnect to DB.");
        		return result;
        	}
        	        	
            try {
                rs = dbConnection.getCacheset(query);
                if (rs != null) {
                    rs.next();
                    if (dbConnectionCheckInt == rs.getInt("TEST_VALUE")) {
                        result = true;
                    } else {
                    	MailUtils.sendMailImpl("checkDbConnection NOK using getCacheset!");
                    }
                }
            } catch (SQLException e) {
                errorLog("QueryUtils :: checkDbConnection : error while checking the given connection: " + e.getMessage(), e);
            } finally {
            	try {
            		if (rs != null) {
            			rs.close();
            		}
            	}
            	catch (Exception e){
            		errorLog("QueryUtils :: checkDbConnection : error closing CacheRow Set: " + e.getMessage(), e);
            	}
            }
        }
        if (result) {
        	debugLog("Queryutils ::  checkDbConnection : Found DB alive");
        }
        else {
        	errorLog("Queryutils ::  checkDbConnection : DB not alive!");
        }
        return result;
    }
   
	/**
     * @return true if the db connection is good, false otherwise
     */
    public static boolean checkCtrlServletDBConnection(){
        return checkDbConnection(CtrlServlet.dbWriter);
    }

    public static int retrieveStringDBColumnSize( String tableName, String attributeName) {
        int result = -1;
        String stringColumnType = "java.lang.String";
        String query = "select " + attributeName + " from " + tableName + " where rownum = 0";
        CachedRowSet rs = null;
        try {
            SQLInterface iface = new SQLInterface();
            rs = iface.execQuery(query);
        } catch (Exception e) {
            errorLog("QueryUtils :: retrieveStringDBColumnSize : TpmsException query = " + query + " - message " + e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }
        if (rs != null) {
            try {
                if (stringColumnType.equals(rs.getMetaData().getColumnClassName(1))){
                    result = rs.getMetaData().getPrecision(1);
                }
            } catch (SQLException e) {
                errorLog("QueryUtils :: retrieveStringDBColumnSize : SQLException query = " + query + " - message " + e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
            } finally { 
            	try {
            		if (rs != null) {
            			rs.close();
            		}
            	} catch (SQLException se) {
            		errorLog("QueryUtils :: retrieveStringDBColumnSize : error while closing resultset! ",se);
            	}
            }
        }
        return result;
    }

}

