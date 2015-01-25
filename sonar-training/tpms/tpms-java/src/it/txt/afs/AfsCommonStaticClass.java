package it.txt.afs;

import org.xml.sax.SAXException;
import tol.LogWriter;
import tol.oneConnDbWrtr;
import tpms.CtrlServlet;
import tpms.TpmsException;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import tpms.utils.TpmsConfiguration;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 26-gen-2006
 * Time: 15.13.13
 * The main purpose of this class is to centralize some common task to afs static class such as Manager...
 */
public class AfsCommonStaticClass {
    //todo mettere nella configurazione l'attivazione dei messaggi dei vari livelli...
    protected static TpmsConfiguration tpmsConfiguration = null;
    protected static LogWriter log = null;

    public static final String DEBUG_LEVEL = "DEBUG : ";
    public static final String ERROR_LEVEL = "ERROR : ";

    protected static oneConnDbWrtr dbWriter = null;


    public static LogWriter getLog() {
        return log;
    }

    public static void debugLog(Object msg) {
        writeLog(msg, DEBUG_LEVEL);
    }

    public static void errorLog(Object msg) {
        writeLog(msg, ERROR_LEVEL);
    }

    public static void errorLog(Object msg, Throwable t) {
        writeLog(msg, ERROR_LEVEL, t);
    }

    public static void writeLog(Object msg, String msgLevel, Throwable t) {
        writeLog((msgLevel + msg.toString()), t);
    }

    public static void writeLog(Object msg, String msgLevel) {
        writeLog((msgLevel + msg.toString()));
    }

    protected static void writeLog(String msg, Throwable t) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
            if (t != null) {
                t.printStackTrace((PrintWriter) log.getOut());
            }
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    protected static void writeLog(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    static {
        try {
            tpmsConfiguration = TpmsConfiguration.getInstance();
            String logFilePath = tpmsConfiguration.getWebAppDir() + File.separator + "logs" + File.separator + "tpms.log";
            log = new LogWriter(logFilePath, false);
            dbWriter = CtrlServlet.dbWriter;
            if (dbWriter == null) {
                try {
                    dbWriter = QueryUtils.getDbConnection();
                } catch (ParserConfigurationException e) {
                    errorLog("AfsCommonStaticClass :: static : ParserConfigurationException unable to parse db connection configuration file", e);
                } catch (SAXException e) {
                    errorLog("AfsCommonStaticClass :: static : SAXException unable to parse db connection configuration file", e);
                } catch (SQLException e) {
                    errorLog("AfsCommonStaticClass :: static : SQLException unable initialize database connection", e);
                }
            }
        } catch (IOException e) {
            //System.out.println("" + e);
        }
    }

    /**
     * Execute a select sql statement and return the corrisponding resultset
     *
     * @param sqlSelectStatement sql select statement that should be executed
     * @return the ResultSet related to the input query
     * @throws tpms.TpmsException the database connection is not available.
     */
    public static ResultSet executeSelectQuery(String sqlSelectStatement) throws TpmsException {
    	ResultSet result = null;
        if (dbWriter == null) {
            try {
                dbWriter = QueryUtils.getDbConnection();
            } catch (Exception e) {
                String msg = "AfsCommonStaticClass :: executeSelectQuery : unable to retrieve the connection execution will continue";
                throw new TpmsException(msg);
            }
        }
        boolean queryNotDone = true;
        for (int i = 0; (queryNotDone && i < 3); i++) {
            //attempt to insert packet data
        	try {       
                result = dbWriter.getRecordset(sqlSelectStatement);
                queryNotDone = false;
            } catch (Exception e) {
                errorLog("AfsCommonStaticClass :: executeSelectQuery : error during query execution: attempt number = " + i + " query = " + sqlSelectStatement, e);
                try {
                    //attempt to restore the connection....
                    if (dbWriter != null) {
                        //if the connection was not null roll back the last query...
                        dbWriter.rollback();
                        //just wait a second
                        Thread.sleep(1000);
                        //and than check it
                        dbWriter.checkConn();
                    } else {
                        //the conenction object is null...
                        //just wait a second
                        Thread.sleep(1000);
                        //try to retrieve it again...
                        dbWriter = QueryUtils.getDbConnection();
                    }
                } catch (SQLException e1) {
                    errorLog("AfsCommonStaticClass :: executeSelectQuery : SQLException attempt of retrieve new connection ", e1);
                } catch (Exception e1) {
                    errorLog("AfsCommonStaticClass :: executeSelectQuery : Exception attempt of retrieve new connection ", e1);
                } 
            }
        }
        return result;
    }

    public static CachedRowSet executeSelectCacheQuery(String sqlSelectStatement) throws TpmsException {
    	CachedRowSet result = null;
        if (dbWriter == null) {
            try {
                dbWriter = QueryUtils.getDbConnection();
            } catch (Exception e) {
                String msg = "AfsCommonStaticClass :: executeSelectCacheQuery : unable to retrieve the connection execution will continue";
                throw new TpmsException(msg);
            }
        }
        boolean queryNotDone = true;
        for (int i = 0; (queryNotDone && i < 3); i++) {
            //attempt to insert packet data
        	try {       
                result = dbWriter.getCacheset(sqlSelectStatement);
                queryNotDone = false;
            } catch (Exception e) {
                errorLog("AfsCommonStaticClass :: executeSelectCacheQuery : error during query execution: attempt number = " + i + " query = " + sqlSelectStatement, e);
                try {
                    //attempt to restore the connection....
                    if (dbWriter != null) {
                        //if the connection was not null roll back the last query...
                        dbWriter.rollback();
                        //just wait a second
                        Thread.sleep(1000);
                        //and than check it
                        dbWriter.checkConn();
                    } else {
                        //the conenction object is null...
                        //just wait a second
                        Thread.sleep(1000);
                        //try to retrieve it again...
                        dbWriter = QueryUtils.getDbConnection();
                    }
                } catch (SQLException e1) {
                    errorLog("AfsCommonStaticClass :: executeSelectCacheQuery : SQLException attempt of retrieve new connection ", e1);
                } catch (Exception e1) {
                    errorLog("AfsCommonStaticClass :: executeSelectCacheQuery : Exception attempt of retrieve new connection ", e1);
                } 
            }
        }
        return result;
    }    
    
    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlDeleteStatement the sql delete statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    public static boolean executeDeleteQuery(String sqlDeleteStatement, String sessionId, String tpmsLogin)  {
        return executeUpdateQuery(sqlDeleteStatement, sessionId, tpmsLogin);
    }

     public static boolean executeDeleteQuery(String sqlUpdateStatement, String tpmsLogin) {
        return executeUpdateQuery(sqlUpdateStatement, null, tpmsLogin);
    }

    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlInsertStatement the sql insert statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    public static boolean executeInsertQuery(String sqlInsertStatement, String sessionId, String tpmsLogin) {
        return executeUpdateQuery(sqlInsertStatement, sessionId, tpmsLogin);
    }

    public static boolean executeInsertQuery(String sqlInsertStatement, String tpmsLogin) {
        return executeUpdateQuery(sqlInsertStatement, null, tpmsLogin);
    }

    public static boolean executeInsertQuery(String sqlInsertStatement, String tpmsLogin, boolean executeCommit) {
        return executeUpdateQuery(sqlInsertStatement, null, tpmsLogin, executeCommit);
    }

    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlUpdateStatement the sql update statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    public static boolean executeUpdateQuery(String sqlUpdateStatement, String sessionId, String tpmsLogin) {
        return executeUpdateQuery(sqlUpdateStatement, sessionId, tpmsLogin, true);
    }

    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlUpdateStatement the sql update statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @param executeCommit if false the commit will not be executed and the process of db track (lost queries) will be skipped
     * @return true if the execution of the query succeded, false otherwise
     */
    public static boolean executeUpdateQuery(String sqlUpdateStatement, String sessionId, String tpmsLogin, boolean executeCommit) {
        boolean queryNotDone = true;
        if (dbWriter == null) {
            try {
                dbWriter = QueryUtils.getDbConnection();
            } catch (Exception e) {
                errorLog("AfsCommonStaticClass :: executeUpdateQuery : unable to retrieve the connection execution will continue", e);
            }
        }

        for (int i = 0; (queryNotDone && i < 3); i++) {
            //attempt to insert packet data
            try {
                dbWriter.submit(sqlUpdateStatement);
                if (executeCommit) dbWriter.commit();
                queryNotDone = false;
            } catch (Exception e) {
                errorLog("AfsCommonStaticClass :: executeUpdateQuery : error during query execution: attempt number = " + i + " query =" + sqlUpdateStatement, e);
                try {
                    //attempt to restore the connection....


                    if (dbWriter != null) {
                        //if the connection was not null roll back the last query...
                        dbWriter.rollback();
                        //just wait a second
                        Thread.sleep(1000);
                        //and than check it
                        dbWriter.checkConn();

                    } else {
                        //the conenction object is null...
                        //just wait a second
                        Thread.sleep(1000);
                        //try to retrieve it again...
                        dbWriter = QueryUtils.getDbConnection();
                    }
                } catch (Exception ex) {
                    errorLog("AfsCommonStaticClass :: executeUpdateQuery : error during retriving db connection attempt nubumber = " + i + " query =" + sqlUpdateStatement, ex);
                }
            }
        }

        if (executeCommit  && queryNotDone) {
            //if the query was not done track it!!!
            try {
                DBTrack.trackQuery(sessionId, tpmsLogin, sqlUpdateStatement);
            } catch (IOException e) {
                errorLog("AfsCommonStaticClass :: executeUpdateQuery : IOException during query tracking: unable to track query!!! " + sqlUpdateStatement, e);
            }
        }

        return !queryNotDone;
     }


    public static TpmsConfiguration getTpmsConfiguration() {
        if (tpmsConfiguration == null) {
            tpmsConfiguration = TpmsConfiguration.getInstance();
        }
        return tpmsConfiguration;
    }
}
