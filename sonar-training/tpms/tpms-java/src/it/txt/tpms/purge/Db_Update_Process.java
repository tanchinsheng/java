package it.txt.tpms.purge;

import java.io.*;
import tol.oneConnDbWrtr;
import tol.dbRdr;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import it.txt.general.utils.XmlUtils;
import tpms.utils.StringMasking;
import tpms.utils.TpmsConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: Puglisi
 * Date: Apr 20, 2005
 * Time: 2:59:11 PM
 * To change this template use Options | File Templates.
 */
public class Db_Update_Process {

    static oneConnDbWrtr dbwrt;
    Exception dbConnExc=null;
    //static String _webAppDir="D:/jakarta-tomcat-3.2.3";
    //static String _webAppDir="/users/vobadm/jakarta-tomcat-3.2.3";
    static String _logFile = System.getProperty("LOG_FILE");
    static String _webAppDir = System.getProperty("TOMCAT_HOME");

    public static void main(String args[]) throws Exception
    {
        setLog("------START DB_UPDATE_PROCESS-------",0);
        //String fName= _webAppDir + "/webapps/tpms/logs/Tp_OffLine.list";
        String fName= "/tpms/logs/purge/Tp_OffLine.list";
        setLog("File to read:" + fName,0);

        if (new File(fName).exists())
        {
            BufferedReader in= new BufferedReader(new FileReader(fName));
            String s;

            while((s=in.readLine())!=null)
           {
             setLog("riga letta->" + s,0);
             String tp;
             if (s.startsWith("TP="))
             {
                 tp=s.substring(3);
                 int index1 = 0;
                 int index2 = tp.indexOf(",",index1);
                 String jobName = tp.substring(index1,index2);
                 setLog("jobName = " + jobName,0);
                 index1 = index2+1;
                 index2 = tp.indexOf(",",index1);
                 String jobRel = tp.substring(index1,index2);
                 setLog("jobRel = " + jobRel,0);
                 index1 = index2+1;
                 index2 = tp.indexOf(",",index1);
                 String jobRev = tp.substring(index1,index2);
                 setLog("jobRev = " + jobRev,0);
                 index1 = index2+1;
                 index2 = tp.indexOf(",",index1);
                 String tpmsVer = tp.substring(index1,index2);
                 setLog("tpmsVer = " + tpmsVer,0);
                 index1 = index2+1;
                 String toPlant = tp.substring(index1);
                 setLog("toPLant = " + toPlant,0);
                 String queryTpPlant = createQueryUpdateTpPlant(jobName,jobRel,jobRev,tpmsVer,toPlant);
                 setLog("query TP_PLANT-->" + queryTpPlant,0);

                 String queryTpHitory = createQueryUpdateTpHistory(jobName,jobRel,jobRev,tpmsVer);
                 setLog("query TP_HISTORY-->" + queryTpHitory,0);
                 if (openConnection()){
                    setLog("DB Connection OPENED", 0);
                    if (updateDb(queryTpPlant))
                    {
                        setLog("TP_PLANT UPDATED  SUCCESSFULLY",0);
                    }else{
                        setLog("DB Update TP_PLANT NO COMPLETED",1);
                    }

                    if (updateDb(queryTpHitory))
                    {
                        setLog("TP_HISTORY UPDATED  SUCCESSFULLY",0);
                    }else{
                        setLog("DB Update TP_HISTORY NO COMPLETED",1);
                    }
                    closeConnection(dbwrt);

                    setLog("-----------DB_UPDATE_PROCESS ENDED-----------",0);
                 }else{
                    setLog("DB Connection NO OPENED-", 1);
                 }
             }else if (s.startsWith("BS=")){

                 setLog("Skipped LineSet",0);

             }else{
                 setLog("no TP to Update",1);
             }
           }
        }else{
            setLog("File Tp_OffLine NOT EXIST",1);
        }

    }

    public static void setLog(String msg, int esit) throws Exception{
        //PrintWriter out=new PrintWriter(new FileWriter(_webAppDir + "/webapps/tpms/logs/Db_Update_Process.log",true));
        //PrintWriter out=new PrintWriter(new FileWriter("/tpms/logs/purge/Db_Update_Process.log",true));
        PrintWriter out=new PrintWriter(new FileWriter(_logFile,true));
        if (esit == 1)
        {
            out.println("esit=" + esit);
            out.println("error=" + msg);
        }else{
            out.println(">" + msg);
        }
        out.flush();
        out.close();

    }

    public static String createQueryUpdateTpPlant(String jobName, String jobRel, String jobVer, String tpmsVer, String toPlant){
        String query = "UPDATE TP_PLANT SET" +
                     " VOB_STATUS ='OffLine'" +
                     " WHERE " +
                     " JOBNAME='" + jobName + "' AND " +
                     " JOB_RELEASE=" + jobRel  + " AND " +
                     " JOB_REVISION='" + jobVer + "' AND " +
                     " TPMS_VER=" + tpmsVer + " AND " +
                     " TO_PLANT='"  + toPlant + "'";
        return query;
    }

    public static String createQueryUpdateTpHistory(String jobName, String jobRel, String jobVer, String tpmsVer){
        String query = "UPDATE TP_HISTORY SET" +
                     " VOB_STATUS ='OffLine'" +
                     " WHERE " +
                     " JOBNAME='" + jobName + "' AND " +
                     " JOB_RELEASE=" + jobRel  + " AND " +
                     " JOB_REVISION='" + jobVer + "' AND " +
                     " TPMS_VER="  + tpmsVer ;
        return query;
    }

    public static boolean updateDb(String query) throws Exception {

        boolean commitBool=false;
        for (int i=0; ((!commitBool)&&(i<3)); i++)
        {
            commitBool=true;
            try
            {
                dbwrt.submit(query);
                dbwrt.commit();
                setLog("Query committed", 0);
            }
            catch(Exception e)
            {
                commitBool=false;
                dbwrt.checkConn();
                dbwrt.rollback();
                Thread.currentThread().sleep(2000);
                setLog("query execution Exception-"+ e, 1);
                return false;
            }
        }
        if (commitBool) dbwrt.commit();
        else
        {
            setLog("DB Commit Exception ", 1);
            return false;

        }

        return true;

    }

    public static boolean openConnection() throws Exception {
        try
        {
            setLog("DB Open Connection START", 0);
            //dbwrt=new oneConnDbWrtr(_webAppDir+"/webapps/tpms/cfg/local_cfg/"+"plants.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File (_webAppDir + File.separator + "webapps" + File.separator
                    + "tpms" + File.separator + "cfg" + File.separator + "local_cfg/" + File.separator + "plants.xml"));
            Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
            String username = XmlUtils.getTextValue(dbInfoElement, "USERNAME");
            String pwd = XmlUtils.getTextValue(dbInfoElement, "PASSWORD");
            pwd = StringMasking.decriptString(pwd);
            String connectionString =  XmlUtils.getTextValue(dbInfoElement, "CONN_STR");
            dbwrt = new oneConnDbWrtr(connectionString, username, pwd);
            return true;
        }catch (Exception e){
            setLog("DB Open Connection Exception-" + e.getMessage() , 1);
            return false;
        }
    }

    public static void closeConnection(oneConnDbWrtr dbwrt) throws Exception {
        try
        {
            dbwrt.close();
            setLog("Close Connection ended successfully", 0);
        }catch (Exception e){
            setLog("DB Open Connection Exception-" + e.getMessage() , 1);

        }
    }
}
