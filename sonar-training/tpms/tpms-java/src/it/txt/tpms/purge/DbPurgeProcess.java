package it.txt.tpms.purge;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tol.oneConnDbWrtr;
import tol.xmlRdr;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Puglisi
 * Date: Apr 20, 2005
 * Time: 2:59:11 PM
 * To change this template use Options | File Templates.
 */
public class DbPurgeProcess {

    static oneConnDbWrtr dbwrt;
    Exception dbConnExc = null;
    static String _status = System.getProperty("STATUS");
    static String _logFile = System.getProperty("LOG_FILE");
    static String _detLogFile = System.getProperty("DET_LOG_FILE");
    static String _fName = System.getProperty("LIST_FILE");
    static String _webAppDir = System.getProperty("TOMCAT_HOME");

    public static void main(String args[]) throws Exception {
        setLog("------START JAVA DB_UPDATE_PROCESS FOR TP  " + _status + "-------", 0, 1);
        setLog("------START JAVA DB_UPDATE_PROCESS FOR TP  " + _status + "-------", 0, 0);
        //String fName= _webAppDir + "/webapps/tpms/logs/Tp_OffLine.list";
        //String fName= "/tpms/logs/purge/Tp_OffLine.list";
        setLog("File to read:" + _fName, 0, 1);

        if (new File(_fName).exists()) {
            BufferedReader in = new BufferedReader(new FileReader(_fName));
            String s;
            int rows = 0;
            int errors = 0;
            while ((s = in.readLine()) != null) {
                setLog("row readed ->" + s, 0, 1);
                setLog("TP to Process->" + s, 0, 0);
                String tp;
                if (s.startsWith("TP=")) {
                    rows ++;
                    tp = s.substring(3);
                    int index1 = 0;
                    int index2 = tp.indexOf(",", index1);
                    String jobName = tp.substring(index1, index2);
                    setLog("jobName = " + jobName, 0, 1);
                    index1 = index2 + 1;
                    index2 = tp.indexOf(",", index1);
                    String jobRel = tp.substring(index1, index2);
                    setLog("jobRel = " + jobRel, 0, 1);
                    index1 = index2 + 1;
                    index2 = tp.indexOf(",", index1);
                    String jobRev = tp.substring(index1, index2);
                    setLog("jobRev = " + jobRev, 0, 1);
                    index1 = index2 + 1;
                    String tpmsVer = tp.substring(index1);
                    setLog("tpmsVer = " + tpmsVer, 0, 1);
                    /*index1 = index2+1;
                    index2 = tp.indexOf(",",index1);
                    String tpmsVer = tp.substring(index1,index2);
                    setLog("tpmsVer = " + tpmsVer,0,1);
                    index1 = index2+1;
                    String toPlant = tp.substring(index1);
                    setLog("toPLant = " + toPlant,0,1);
                    String queryTpPlant = createQueryUpdateTpPlant(jobName,jobRel,jobRev,tpmsVer,toPlant,_status);
                    */
                    String queryTpPlant = createQueryUpdateTpPlant(jobName, jobRel, jobRev, tpmsVer, _status);
                    setLog("query TP_PLANT-->" + queryTpPlant, 0, 1);

                    String queryTpHitory = createQueryUpdateTpHistory(jobName, jobRel, jobRev, tpmsVer, _status);
                    setLog("query TP_HISTORY-->" + queryTpHitory, 0, 1);
                    if (openConnection()) {
                        setLog("DB Connection OPENED", 0, 1);
                        if (updateDb(queryTpPlant)) {
                            setLog("TP_PLANT UPDATED  SUCCESSFULLY", 0, 1);
                            setLog("TP_PLANT UPDATED  SUCCESSFULLY", 0, 0);
                        } else {
                            errors++;
                            setLog("DB Update TP_PLANT NO COMPLETED", 1, 1);
                            setLog("DB Update TP_PLANT NO COMPLETED", 0, 0);
                        }

                        if (updateDb(queryTpHitory)) {
                            setLog("TP_HISTORY UPDATED  SUCCESSFULLY", 0, 1);
                            setLog("TP_HISTORY UPDATED  SUCCESSFULLY", 0, 0);
                        } else {
                            errors++;
                            setLog("DB Update TP_HISTORY NO COMPLETED", 1, 1);
                            setLog("DB Update TP_HISTORY NO COMPLETED", 0, 0);
                        }
                        closeConnection(dbwrt);

                        setLog("-----------DB_UPDATE_PROCESS ENDED-----------", 0, 1);
                    } else {
                        setLog("DB Connection NO OPENED-", 1, 1);
                        setLog("DB Connection NO OPENED-", 1, 0);
                    }
                } else if (s.startsWith("BS=")) {

                    setLog("Skipped LineSet", 0, 1);

                } else {
                    setLog("NO TP to Update", 0, 1);
                }
            }
            setLog("------JAVA DB_UPDATE_PROCESS FOR TP  " + _status + " ENDED-------", 0, 1);
            setLog("------JAVA DB_UPDATE_PROCESS FOR TP  " + _status + " ENDED-------", 0, 0);
            setLog("-----------------------------", 0, 0);
            setLog("-----------------------------", 0, 1);
            setLog("RESULTS OF JAVA PURGING PROCEDURE ", 0, 0);
            setLog("NUMBERS OF TPs TO PUT " + _status + " =  " + rows, 0, 0);
            setLog("NUMBERS OF Errors =  " + errors, 0, 0);

            setLog("RESULTS OF JAVA PURGING PROCEDURE ", 0, 1);
            setLog("NUMBERS OF TPs TO PUT " + _status + " =  " + rows, 0, 1);
            setLog("NUMBERS OF Errors =  " + errors, 0, 1);
            setLog("-----------------------------", 0, 0);
            setLog("-----------------------------", 0, 1);

        } else {
            setLog("File" + _fName + "NOT EXIST", 0, 1);
            setLog("File" + _fName + "NOT EXIST", 0, 0);
        }

    }

    /**
     * setLog(message, esit, debugLevel)
     * esit: 0 -->  OK
     * 1 -->  ERRORE
     * Debuglevel: 0 --> message to write into Global purgin log file (_logFile)
     * 1 --> message to write into Developer log file (TOMCAT_HOME/tpms/logs/purging.log)
     */

    public static void setLog(String msg, int esit, int dbLevel) throws Exception {
        //PrintWriter out=new PrintWriter(new FileWriter(_webAppDir + "/webapps/tpms/logs/Db_Update_Process.log",true));
        //PrintWriter out=new PrintWriter(new FileWriter("/tpms/logs/purge/Db_Update_Process.log",true));
        PrintWriter out;
        if (dbLevel == 0) {
            out = new PrintWriter(new FileWriter(_logFile, true));
        } else {
            //out = new PrintWriter(new FileWriter(_webAppDir + "/tpms/logs/purging.log",true));
            out = new PrintWriter(new FileWriter(_detLogFile, true));
        }

        if (esit == 1) {
            out.println("esit=" + esit);
            out.println("error=" + msg);
        } else {
            out.println(">" + msg);
        }
        out.flush();
        out.close();

    }

    public static String createQueryUpdateTpPlantOld(String jobName, String jobRel, String jobVer, String tpmsVer, String toPlant, String status) {
        return "UPDATE TP_PLANT SET" +
                " VOB_STATUS ='" + status + "'" +
                " WHERE " +
                " JOBNAME='" + jobName + "' AND " +
                " JOB_RELEASE=" + jobRel + " AND " +
                " JOB_REVISION='" + jobVer + "' AND " +
                " TPMS_VER=" + tpmsVer + " AND " +
                " TO_PLANT='" + toPlant + "'";
    }

    public static String createQueryUpdateTpPlant(String jobName, String jobRel, String jobVer, String tpmsVer, String status) {
        return  "UPDATE TP_PLANT SET" +
                " VOB_STATUS =" + status + "" +
                " WHERE " +
                " JOBNAME='" + jobName + "' AND " +
                " JOB_RELEASE=" + jobRel + " AND " +
                " JOB_REVISION='" + jobVer + "' AND " +
                " TPMS_VER=" + tpmsVer;
    }

    public static String createQueryUpdateTpHistory(String jobName, String jobRel, String jobVer, String tpmsVer, String status) {
        return "UPDATE TP_HISTORY SET" +
                " VOB_STATUS =" + status + "" +
                " WHERE " +
                " JOBNAME='" + jobName + "' AND " +
                " JOB_RELEASE=" + jobRel + " AND " +
                " JOB_REVISION='" + jobVer + "' AND " +
                " TPMS_VER=" + tpmsVer;
    }

    public static boolean updateDb(String query) throws Exception {

        boolean commitBool = false;
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
            commitBool = true;
            try {
                dbwrt.submit(query);
                dbwrt.commit();
                setLog("Query committed", 0, 1);
            }
            catch (Exception e) {
                dbwrt.checkConn();
                dbwrt.rollback();
                Thread.sleep(2000);
                setLog("query execution Exception-" + e, 1, 1);
                return false;
            }
        }
        if (commitBool) dbwrt.commit();
        else {
            setLog("DB Commit Exception ", 1, 1);
            return false;

        }

        return true;

    }

    public static boolean openConnection() throws Exception {
        try {
            setLog("DB Open Connection START", 0, 1);
            //dbwrt=new oneConnDbWrtr(_webAppDir+"/webapps/tpms/cfg/local_cfg/"+"plants.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(_webAppDir + File.separator + "webapps" + File.separator
                    + "tpms" + File.separator + "cfg" + File.separator + "local_cfg" + File.separator + "plants.xml"));
            Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
            //String username = XmlUtils.getTextValue(dbInfoElement, "USERNAME");
            //String pwd = XmlUtils.getTextValue(dbInfoElement, "PASSWORD");
            String username = xmlRdr.getVal(dbInfoElement, "USERNAME");
            String pwd = xmlRdr.getVal(dbInfoElement, "PASSWORD");
            pwd = tpms.utils.StringMasking.decriptString(pwd);
            //String connectionString =  XmlUtils.getTextValue(dbInfoElement, "CONN_STR");
            String connectionString = xmlRdr.getVal(dbInfoElement, "CONN_STR");
            dbwrt = new oneConnDbWrtr(connectionString, username, pwd);
            return true;
        } catch (Exception e) {
            setLog("DB Open Connection Exception-" + e.getMessage(), 1, 1);
            return false;
        }
    }

    public static void closeConnection(oneConnDbWrtr dbwrt) throws Exception {
        try {
            dbwrt.close();
            setLog("Close Connection ended successfully", 0, 1);
        } catch (Exception e) {
            setLog("DB Open Connection Exception-" + e.getMessage(), 1, 1);

        }
    }
}
