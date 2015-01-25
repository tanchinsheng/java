package it.txt.afs;

import tpms.utils.TpmsConfiguration;
import tpms.TpmsException;

import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 30-gen-2006
 * Time: 9.57.23
 * To change this template use File | Settings | File Templates.
 */
public class AfsCommonClass {
    public TpmsConfiguration tpmsConfiguration = AfsCommonStaticClass.tpmsConfiguration;

    public void debugLog(Object msg) {
        AfsCommonStaticClass.debugLog(msg);
    }

    public void errorLog (Object msg, Throwable t){
        AfsCommonStaticClass.errorLog(msg, t);
    }

    public void errorLog(Object msg) {
        AfsCommonStaticClass.errorLog(msg);
    }

    public void writeLog(Object msg, String msgLevel) {
        AfsCommonStaticClass.writeLog(msgLevel + msg.toString());
    }

    /**
     * Execute a select sql statement and return the corrisponding resultset
     *
     * @param sqlSelectStatement sql select statement that should be executed
     * @return the ResultSet related to the input query
     * @throws tpms.TpmsException the database connection is not available.
     */
    protected ResultSet executeSelectQuery(String sqlSelectStatement) throws TpmsException {
        return AfsCommonStaticClass.executeSelectQuery(sqlSelectStatement);
    }

    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlDeleteStatement the sql delete statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    protected static boolean executeDeleteQuery(String sqlDeleteStatement, String sessionId, String tpmsLogin)  {
        return AfsCommonStaticClass.executeUpdateQuery(sqlDeleteStatement, sessionId, tpmsLogin);
    }

    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlInsertStatement the sql insert statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    protected static boolean executeInsertQuery(String sqlInsertStatement, String sessionId, String tpmsLogin) {
        return AfsCommonStaticClass.executeUpdateQuery(sqlInsertStatement, sessionId, tpmsLogin);
    }


    /**
     * Execute an update/insert/delete sql statement
     *
     * @param sqlUpdateStatement the sql update statement that should be executed
     * @param sessionId the current user session id
     * @param tpmsLogin the current user tpmsLogin
     * @return true if the execution of the query succeded, false otherwise
     */
    protected static boolean executeUpdateQuery(String sqlUpdateStatement, String sessionId, String tpmsLogin) {
        return AfsCommonStaticClass.executeUpdateQuery(sqlUpdateStatement, sessionId, tpmsLogin);
    }
}
