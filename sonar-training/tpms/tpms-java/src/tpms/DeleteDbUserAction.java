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
 * FP Rev5 - aggiunto il controllo sulla presenza dello user: se presente faccio DELETE altrimenti NIENTE
 * FP Rev5 - aggiunta la scitture dal campo DIVISION
 */
public class DeleteDbUserAction extends InsertDbUserAction {

    public DeleteDbUserAction(Element accntData, String sessionId, String userId) {
        super(accntData, sessionId, userId);
    }


    private String generateQuery() {
        String query = null;
        try {
            query = "DELETE FROM USERS " +
                    "WHERE INSTALLATION_ID = '" +
                    localPlant + "' AND " +
                    "TPMS_LOGIN = '" +
                    xmlRdr.getVal(accntData, "NAME") + "'";
        } catch (Exception e) {
            errorLog("DeleteDbUserAction :: generateQuery : unable to generate query!!!", e);
        }
        return query;
    }

    public void doDbTransaction(String query) throws Exception {
        if (getUser() != 0) {
            debugLog("DELETE DB TRANSACTION");

            debugLog("QUERY = " + query);
            try {
                dbWriter.submit(query);
                dbWriter.commit();
            } catch (Exception e) {
                if (dbWriter != null) {
                    try {
                        dbWriter.rollback();
                    } catch (SQLException e1) {
                        errorLog("DeleteDbUserAction :: doDbTransaction : unable to rollback!", e);
                    }
                }
                String action = "DB UPDATE";
                String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
                throw new TpmsException(msg, action, e);
            }
        }
    }

    private int getUser() throws Exception {
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
                        errorLog("", e1);
                    } catch (NullPointerException e1) {
                        errorLog("", e1);
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
                DBTrack.trackQuery(this.sessionId, this.userId, query);
                debugLog("DB-TRANS-FAILED> " + getDbTransLogMsg(DbException));
            }
        } else {
            errorLog("DeleteDbUserAction :: updateDb unable to get query");
        }
    }


    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION: DELETE DB USER ACTION";
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

}
