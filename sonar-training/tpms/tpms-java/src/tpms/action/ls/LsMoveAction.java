/**
 * User: Fabio Furgiuele
 * Date: Sep 15, 2005
 * Time: 18:38:07 AM
 */


package tpms.action.ls;


import it.txt.general.utils.GeneralStringUtils;
import tol.LogWriter;
import tol.dateRd;
import tol.xmlRdr;
import tpms.LsAction;
import tpms.TpActionServlet;
import tpms.TpmsException;
import tpms.utils.DBTrack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

public class LsMoveAction extends LsAction {


    public LsMoveAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }


    /**
     * questo metodo produce l'in file per la chiamata clear case
     *
     * @param SID
     * @param fName
     * @param repFileName
     * @throws IOException
     * @throws Exception
     */
    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("action=" + this.getCCAction());
        out.println("rectype=lineset");
        out.println("debug=" + TpActionServlet._cc_debug);
        out.println("outfile=" + repFileName);
        out.println("unixUser=" + this.userName);//FF secondo me vale la pena di differenziare a questo livello tra lo unix user e la login web
        out.println("linesetname=" + xmlRdr.getVal(lsRec, "LS_NAME"));
        String vob = (xmlRdr.getChild(lsRec, "VOB") != null ? xmlRdr.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("work_dir=" + xmlRdr.getVal(lsRec, "LS_NEW_PATH"));
        //out.println("ls_name=" + xmlRdr.getVal(lsRec, "LS_NAME"));

        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }

        out.flush();
        out.close();
        debug("CC IN FILE CREATED>" + fName);
    }


    private String generateQuery(String vob, String newLineSetPath) {
        String query = null;
        try {
            query = "UPDATE LINESET " +
                    "SET FILE_SYSTEM_DIR = '" + newLineSetPath + "' " +
                    "WHERE INSTALLATION_ID = '" + localPlant + "' AND " +
                    " VOB_NAME = '" + vob + "' AND " +
                    " LINESET_NAME = '" + xmlRdr.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsMoveAction :: generateQuery : unable to generate query!!");
        }
        return query;
    }

    /**
     * @throws Exception
     */
    public void doDbTransaction(String query, String vob) throws Exception {
        debug("-START DB TRANSACTION-->MOVE LINESET ");

        // se esiste almeno un line set nella tabella dei ls inizio.... altrimenti non c'è nulla da aggiornare
        if (getNumLineSet(vob) > 0) {

            debug("LINESET MOVE- start moving Lineset");
            //move Lineset from LINESET


            debug("QUERY = " + query);

            try {
                dbwrt.submit(query);
                dbwrt.commit();
                debug("LINESET MOVE - move Lineset completed");
            } catch (Exception e) {
                if (dbwrt != null) {
                    dbwrt.rollback();
                }
                String action = "DB UPDATE";
                String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
                throw new TpmsException(msg, action, e);
            }
        } else {
            debug("-NOT DB TRANSACTION-->LINESET NOT FOUND");
        }
    }

    public String getCcActionUnixLogin(String userLogin, String adminLogin) {
        return adminLogin;
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION:" + this.getAction().toUpperCase() + " ";
            s = s + "USER:" + this.userName + " ";
            s = s + "LS_NAME:" + xmlRdr.getVal(lsRec, "LS_NAME") + " ";
            s = s + "DATE:" + dateRd.getCurDateTime() + " ";
            if (e != null) {
                s = s + "ERRTYP:" + e.getClass().getName() + " ";
                s = s + "ERRMSG:" + (e.getMessage() != null ? e.getMessage() : "");
            }
        } catch (Exception exc) {
            return null;
        }
        return s;
    }


    public void updateDb() throws Exception {
        Exception DbException = null;
        boolean commitBool = false;

        String vob = null;
        String newLineSetPath = null;
        vob = (xmlRdr.getChild(lsRec, "VOB") != null ? xmlRdr.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        newLineSetPath = xmlRdr.getVal(lsRec, "LS_NEW_PATH");
        String query = generateQuery(vob, newLineSetPath);

        if (!GeneralStringUtils.isEmptyString(query)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(query, vob);
                } catch (Exception e) {
                    DbException = e;
                    commitBool = false;

                    try {
                        dbwrt.checkConn();
                    } catch (Exception e1) {
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
                DBTrack.trackQuery(this.sessionId, this.userName, query);
                debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            debug("LsMoveAction :: updateDb : unable to get query!! ");
        }
    }

    public int getNumLineSet(String vob) throws Exception {
        debug("LINESET MOVE- start GetNumLineSet()");
        Vector v = new Vector();
        String query = "SELECT LINESET_NAME " +
                "FROM   LINESET" +
                " WHERE INSTALLATION_ID = '" +
                localPlant + "' AND " +
                " VOB_NAME = '" + vob + "' AND " +
                " LINESET_NAME = '" +
                xmlRdr.getVal(lsRec, "LS_NAME") + "'";
        dbwrt.getRows(v, query);
        debug("LINESET MOVE- GetNumLineSet()-->" + query);
        debug("LINESET MOVE- end GetNumLineSet()");
        return v.size();
    }

    public boolean hasDbAction() {
        return hasDbActionBool;
    }

    public void setReqID(String reqID) {
        this.REQID = reqID;
    }

    public void setRepFileName(String s) {
        this.repFileName = s;
    }

    public String getReqID() {
        return this.REQID;
    }

}


