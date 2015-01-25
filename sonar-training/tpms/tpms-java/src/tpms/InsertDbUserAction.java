package tpms;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tol.dateRd;
import tpms.utils.DBTrack;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: Oct 13, 2003
 * Time: 1:18:22 PM
 * To change this template use Options | File Templates.
 * FP Rev 5 - aggiunta la scrittura del campo DIVISION
 */
public class InsertDbUserAction extends AfsCommonStaticClass {
    protected Element accntData = null;

    protected String localPlant = null;
    protected String webAppDir = null;

    protected String sessionId = null;
    protected String userId = null;


    public InsertDbUserAction(Element accountData, String sessionId, String userId) {
        debugLog("InsertDbUserAction :: contructor : (accountData == null) ? " + (accountData == null));
        this.accntData = accountData;
        this.localPlant = tpmsConfiguration.getLocalPlant();
        this.webAppDir = tpmsConfiguration.getWebAppDir();
        this.userId = userId;
        this.sessionId = sessionId;

    }

    private String generateQuery() {
        String query = null;
        try {
            query = "INSERT INTO USERS " +
                    "( " +
                    " INSTALLATION_ID, " +
                    " FIRST_NAME, " +
                    " SURNAME, " +
                    " DIVISION, " +
                    " TPMS_ROLE, " +
                    " TPMS_LOGIN, " +
                    " EMAIL, " +
                    " UNIX_LOGIN, " +
                    " UNIX_HOME, " +
                    " WORK_DIR" +
                    ") " +
                    "VALUES " +
                    "( '" +
                    localPlant + "','" +
                    XmlUtils.getVal(accntData, "FIRST_NAME") + "','" +
                    XmlUtils.getVal(accntData, "SURNAME") + "','" +
                    XmlUtils.getVal(accntData, "DIVISION") + "','" +
                    XmlUtils.getVal(accntData, "ROLE") + "','" +
                    XmlUtils.getVal(accntData, "NAME") + "','" +
                    XmlUtils.getVal(accntData, "EMAIL") + "','" +
                    XmlUtils.getVal(accntData, "UNIX_USER") + "','" +
                    XmlUtils.getVal(accntData, "HOME_DIR") + "','" +
                    XmlUtils.getVal(accntData, "WORK_DIR") +
                    "') ";
        } catch (Exception e) {
            errorLog("InsertDbUserAction :: generateQuery : unable to generate query!!!", e);
        }
        return query;
    }

    public void doDbTransaction(String query) throws Exception {
        debugLog("InsertDbUserAction :: doDbTransaction QUERY = " + query);

        try {

            dbWriter.submit(query);
            dbWriter.commit();
        } catch (Exception e) {
            if (dbWriter != null)
                try {
                    dbWriter.rollback();
                } catch (SQLException e1) {
                    errorLog("InsertDbUserAction :: doDbTransaction : unable to rollback!", e1);
                }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION: INSERT DB USER ACTION";
            s = s + " NEW USER:" + XmlUtils.getVal(accntData, "NAME") + " ";
            s = s + "DATE:" + dateRd.getCurDateTime() + " ";
            if (e != null) {
                s = s + "ERRTYP:" + e.getClass().getName() + " ";
                s = s + "ERRMSG:" + (e.getMessage() != null ? e.getMessage() : "");
            }
        }
        catch (Exception exc) {
            return null;
        }
        return s;
    }

    public void updateDb() throws Exception {
        Exception DbException = null;
        boolean commitBool = false;
        String query = generateQuery();
        if (!GeneralStringUtils.isEmptyString(query)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(query);
                }
                catch (Exception e) {
                    DbException = e;
                    commitBool = false;
                    try {
                        dbWriter.checkConn();
                    } catch (SQLException e1) {
                        errorLog("InsertDbUserAction :: updateDb : SQLException dbWriter == null ? " + (dbWriter == null), e1);
                    } catch (NullPointerException e1){
                        errorLog("InsertDbUserAction :: updateDb : NullPointerException dbWriter == null ? " + (dbWriter == null), e1);
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
                DBTrack.trackQuery(this.sessionId, this.userId, query);
                debugLog("DB-TRANS-FAILED> " + getDbTransLogMsg(DbException));
            }
        } else {
            errorLog("InsertDbUserAction :: updateDb unable to get query");
        }
    }

}

