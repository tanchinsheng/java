package tpms;

import it.txt.general.utils.GeneralStringUtils;
import org.w3c.dom.Element;
import tol.dateRd;
import tol.xmlRdr;
import tpms.utils.DBTrack;

import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: Oct 14, 2003
 * Time: 11:14:10 AM
 * To change this template use Options | File Templates.
 * FP Rev5 - aggiunto il controllo sulla presenza dello user: se presente faccio UPDATE altrimenti INSERT
 * FP Rev5 - aggiunta la scitture dal campo DIVISION
 */
public class UpdateDbUserAction extends InsertDbUserAction {

    public UpdateDbUserAction(Element accntData, String sessionId, String userId) {
        super(accntData, sessionId, userId);
    }

    private String generateQuery() {
        String query = null;
        try {
            if (getUser() == 0) {
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
                        xmlRdr.getVal(accntData, "FIRST_NAME") + "','" +
                        xmlRdr.getVal(accntData, "SURNAME") + "','" +
                        xmlRdr.getVal(accntData, "DIVISION") + "','" +
                        xmlRdr.getVal(accntData, "ROLE") + "','" +
                        xmlRdr.getVal(accntData, "NAME") + "','" +
                        xmlRdr.getVal(accntData, "EMAIL") + "','" +
                        xmlRdr.getVal(accntData, "UNIX_USER") + "','" +
                        xmlRdr.getVal(accntData, "HOME_DIR") + "','" +
                        xmlRdr.getVal(accntData, "WORK_DIR") +
                        "') ";
            } else {
                query = "UPDATE USERS SET " +
                        " FIRST_NAME='" + xmlRdr.getVal(accntData, "FIRST_NAME") + "'," +
                        " SURNAME='" + xmlRdr.getVal(accntData, "SURNAME") + "'," +
                        " DIVISION='" + xmlRdr.getVal(accntData, "DIVISION") + "'," +
                        " TPMS_ROLE='" + xmlRdr.getVal(accntData, "ROLE") + "'," +
                        " EMAIL='" + xmlRdr.getVal(accntData, "EMAIL") + "'," +
                        " UNIX_LOGIN='" + xmlRdr.getVal(accntData, "UNIX_USER") + "'," +
                        " UNIX_HOME='" + xmlRdr.getVal(accntData, "HOME_DIR") + "'," +
                        " WORK_DIR='" + xmlRdr.getVal(accntData, "WORK_DIR") + "'" +
                        " WHERE INSTALLATION_ID = '" +
                        localPlant + "' AND " +
                        "TPMS_LOGIN = '" +
                        xmlRdr.getVal(accntData, "NAME") + "'";

            }
        } catch (Exception e) {
            errorLog("UpdateDbUserAction :: generateQuery : unable to generate query!!", e);
        }

        return query;
    }

    public void doDbTransaction(String query) throws Exception {
        try {
            dbWriter.submit(query);
            dbWriter.commit();
        } catch (Exception e) {
            if (dbWriter != null) {
                try {
                    dbWriter.rollback();
                } catch (SQLException e1) {
                    errorLog("UpdateDbUserAction :: doDbTransaction : unable to rollback!", e1);
                }
            }

            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    int getUser() {
        try {
            Vector v = new Vector();
            dbWriter.getRows(v,
                    "SELECT TPMS_LOGIN " +
                            "FROM  USERS " +
                            " WHERE INSTALLATION_ID = '" +
                            localPlant + "' AND " +
                            "TPMS_LOGIN = '" +
                            xmlRdr.getVal(accntData, "NAME") + "'"
            );
            return v.size();
        } catch (Exception e) {
            errorLog("", e);
            return -1;
        }
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION: UPDATE DB USER ACTION";
            s = s + " NEW USER:" + xmlRdr.getVal(accntData, "NAME") + " ";
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
                        errorLog("UpdateDbUserAction :: updateDb : SQLException ", e1);
                    } catch (NullPointerException e1) {
                        errorLog("UpdateDbUserAction :: updateDb : NullPointerException ", e1);
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
                DBTrack.trackQuery(this.sessionId, this.userId, query);
                debugLog("DB-TRANS-FAILED> " + getDbTransLogMsg(DbException));
            }
        } else {
            errorLog("UpdateDbUserAction :: updateDb unable to get query");
        }
    }

}