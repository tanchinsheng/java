package tpms;

import tol.LogWriter;
import tol.dateRd;
import tpms.utils.TpmsConfiguration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import it.txt.afs.servlets.master.AfsGeneralServlet;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * To change this template use Options | File Templates.
 * FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
 */

public class VobQryServlet extends AfsGeneralServlet {
    boolean _DEBUG;
    String _webAppDir;
    String _ccScriptsDir;
    String _ccInOutDir;
    public static String _cc_debug;

    // dvl execution
    static String _dvlCcInOutDir;
    static boolean _dvlBool;
    static String _dvlUnixHost;
    static String _dvlUnixAppDir;

    long _timeOut; //[sec]
    String _timeOutStr;
    String _execMode;

    protected TpmsConfiguration tpmsConfiguration = null;

    LogWriter log = null;

    public void debug(Object msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public void debug(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();

        this.log = (LogWriter) this.getServletContext().getAttribute("log");
        _DEBUG = Boolean.valueOf(getServletContext().getInitParameter("debug")).booleanValue();
        _webAppDir = getServletContext().getInitParameter("webAppDir");
        _ccScriptsDir = getServletContext().getInitParameter("ccScriptsDir");
        _ccInOutDir = getServletContext().getInitParameter("ccInOutDir");
        _cc_debug = (getServletContext().getInitParameter("cc_debug").equalsIgnoreCase("true") ? "1" : "0");
        _execMode = getServletContext().getInitParameter("unixScriptExecMode");
        _timeOutStr = getServletConfig().getInitParameter("timeOut");
        _timeOut = Long.parseLong(_timeOutStr);

        /* DVL EXECUTION  (Java Windows  - CC Unix)
        *  _dvlCcInOutDir --> Unix dir with CCInOut FILE
        *  _dvlBool       --> (true) dvl execution
        *  _dvlUnixHost   --> Unix host of machine for rsh
        *  _dvlUnixAppDir --> Unix Web App Dir
        */
        _dvlCcInOutDir = getServletContext().getInitParameter("dvlCcInOutDir");
        _dvlBool = Boolean.valueOf(getServletContext().getInitParameter("dvlBool")).booleanValue();
        _dvlUnixHost = getServletContext().getInitParameter("dvlUnixHost");
        _dvlUnixAppDir = getServletContext().getInitParameter("dvlUnixWebAppDir");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String qryType = request.getParameter("qryType");
        HttpSession session = request.getSession();
        String REQID = (!_DEBUG ? session.getId() + "_" + Long.toString(System.currentTimeMillis()) : qryType);
        // RepFileName to create from CC Command
        String repFileNameCc;
        // RepFileName for Out Page
        String repFileNameOut;
        boolean fetchBool;
        String nextPage = getServletConfig().getInitParameter("nextPage");
        String outPage = getServletConfig().getInitParameter("outPage");
        String unixUser = (String) session.getAttribute("unixUser");
        String actionTxt = getServletConfig().getInitParameter("actionTxt");
        String filter;
        try {
            if (session.getAttribute("vob") == null) {
                throw new NoVobSelectedException(actionTxt + " ABORTED");
            }
            String vob = (String) session.getAttribute("vob");
            String workMode = (String) session.getAttribute("workMode");

            if (request.getParameterValues("repFileName") == null) {
                fetchBool = true;
                if (session.getAttribute("exception" + "_" + REQID) != null) session.removeAttribute("exception" + "_" + REQID);
                session.setAttribute("startBool" + "_" + REQID, Boolean.TRUE);
                /* DVL Execution
                *  if dvlBool=true --> RepFileNameCc producec in Unix System
                *  if dvlBool=false --> RepFileNameCc producec in Windows System
                */
                if (_DEBUG) {
                    repFileNameCc = _webAppDir + "/" + "images" + "/" + getDebugXmlFileName(qryType);
                } else if (_dvlBool) {
                    repFileNameCc = _dvlUnixAppDir + "/" + "images" + "/" + REQID + "_rep.xml";
                } else {
                    repFileNameCc = _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml";
                }
                debugLog("repfilename cc>" + repFileNameCc);
                //repFileName=(!_DEBUG ? _webAppDir+"/"+"images"+"/"+REQID+"_rep.xml" : _webAppDir+"/"+"images"+"/"+getDebugXmlFileName(qryType));
                Properties filterProps = new Properties();
                filterProps.setProperty("filter1", getStatus1Filter(session, qryType, workMode));
                filterProps.setProperty("filter2", getStatus2Filter(session, qryType, workMode));
                filterProps.setProperty("ownerFilter", getOwnerFilter(session, qryType));
                filter = getFilter(request, fetchBool, filterProps);
                debugLog("VOB-ACTION-START-" + REQID + ">");
                debugLog("QRY-TYPE-" + REQID + ">" + qryType);
                debugLog("USER-" + REQID + ">" + unixUser);
                doQry(session, REQID, repFileNameCc, (!_DEBUG ? _ccScriptsDir : _webAppDir + "/" + "images"), qryType, unixUser, vob, _timeOut, actionTxt, filterProps);
            } else {
                fetchBool = false;
                repFileNameOut = request.getParameter("repFileName");
                filter = getFilter(request, fetchBool, null);
            }
            request.setAttribute("status1Filter", getStatus1Filter(session, qryType, workMode));
            request.setAttribute("status2Filter", getStatus2Filter(session, qryType, workMode));
            debugLog("user-filt>" + getUserFilter(session, qryType));
            request.setAttribute("userFilter", getUserFilter(session, qryType));
            request.setAttribute("ownerFilter", getOwnerFilter(session, qryType));
            request.setAttribute("outPage", outPage);
            request.setAttribute("actionTxt", actionTxt);
            request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
            request.setAttribute("reqId", REQID);
            // if dvlBool=true the repFilename for out page is in Windows
            repFileNameOut = (!_DEBUG ? _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml" : _webAppDir + "/" + "images" + "/" + getDebugXmlFileName(qryType));
            debugLog("repfilename outfile>" + repFileNameOut);
            request.setAttribute("repFileName", repFileNameOut);
            request.setAttribute("xslFileName", getXslFileName(qryType));
            request.setAttribute("repTitle", getReportTitle(qryType));
            request.setAttribute("qryType", qryType);
            if (!filter.equals("")) {
                request.setAttribute("filterXslFile", repFileNameOut + ".xsl");
                FileWriter fout = new FileWriter(repFileNameOut + ".xsl");
                buildXslFile(getXmlRepRootTag(qryType), getXmlRowTag(qryType), filter, fout);
                fout.flush();
                fout.close();
            }
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        }
        catch (NoVobSelectedException e) {
            session.setAttribute("startBool" + "_" + REQID, Boolean.FALSE);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + e.getFwdPage());
            rDsp.forward(request, response);
        }
        catch (Exception e) {
            session.setAttribute("startBool" + "_" + REQID, Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }

    }


    public void doQry(HttpSession session, String REQID, String repFileName, String scriptDir, String qryType, String unixUser, String vob, long timeOut, String actionTxt, Properties filterProps) throws IOException, TpmsException {
        String SID = session.getId();
        String inFileName = this._ccInOutDir + "/" + REQID + "_in";
        String outFileName = this._ccInOutDir + "/" + REQID + "_out";
        String inFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_in";
        String outFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_out";
        String shellScriptName = scriptDir + "/" + CtrlServlet._tpmsShellScriptName;
        debugLog("OUT-FILE-NAME-" + REQID + ">" + outFileName);

        try {
            writeBackEndInFile(SID, inFileName, repFileName, qryType, unixUser, vob, filterProps);
        }
        catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC INPUT FILE CREATION FAILURE";
            throw new TpmsException(msg, action, e);
        }

        try {
            String cmd;
            if (_dvlBool) {
                debugLog("VobQryServlet :: doQry : _dvlBool");
                cmd = VobActionDaemon.getUnixClearCaseCmdDvl(_execMode, unixUser, shellScriptName, inFileNameDvl, outFileNameDvl, _dvlUnixHost);
                debugLog("VobQryServlet :: doQry : UNIX-COMMAND-DVl" + REQID + ">" + cmd);
            } else {
                debugLog("VobQryServlet :: doQry : no _dvlBool");
                cmd = VobActionDaemon.getUnixClearCaseCmd(_execMode, unixUser, shellScriptName, inFileName, outFileName);
                debugLog("VobQryServlet :: doQry : UNIX-COMMAND-" + REQID + ">" + cmd);
            }
            //String cmd=VobActionDaemon.getUnixClearCaseCmd(_execMode, unixUser, shellScriptName, inFileName, outFileName);
            VobActionDaemon daemon = new VobActionDaemon(session, this.log, null, REQID, actionTxt, this._DEBUG, inFileName, outFileName, this._timeOut, cmd, _dvlBool, _dvlUnixHost, _dvlCcInOutDir, unixUser, _webAppDir, _dvlUnixAppDir);
            Thread daemonThread = new Thread(daemon);
            daemonThread.start();
        }
        catch (Exception e) {
            errorLog("VobQryServlet :: doQry : exception " +e.getMessage(), e);
            String action = actionTxt + " ABORTED";
            String msg = "CC COMMAND EXECUTION FAILURE<!--vob query servlet-->";
            throw new TpmsException(msg, action, e);
        }
    }


    public void writeBackEndInFile(String SID, String fName, String repFileName, String qryType, String unixUser, String vob, Properties filters) throws IOException {
        debugLog("filtProps>" + filters);
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("action=" + this.getCCAction(qryType));
        out.println("rectype=" + "query");
        out.println("debug=" + _cc_debug);
        out.println("outfile=" + repFileName);
        out.println("unixUser=" + unixUser);
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        for (Enumeration e = filters.keys(); e.hasMoreElements();) {
            String prop = (String) e.nextElement();
            String propVal = filters.getProperty(prop);
            out.println(prop + "=" + propVal);
        }
        out.flush();
        out.close();
        debugLog("CC IN FILE CREATED>" + fName);
    }

    public String getCCAction(String qryType) {
        if (qryType.equals("vob_tp_history")) return "tp_history";
        if (qryType.equals("vob_tp_search")) return "tp_query";
        if (qryType.equals("vob_my_linesets")) return "ls_query";
        if (qryType.equals("vob_lineset_submits")) return "ls_query";
        if (qryType.equals("vob_lineset_history")) return "ls_history";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "tp_delete_query";
        else
            return "tp_query";
    }


    static public String getReportTitle(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return "TP DISTRIBUTED REPORT (VOB)";
        if (qryType.equals("vob_tp_in_progress")) return "TP IN PROGRESS REPORT (VOB)";
        if (qryType.equals("vob_tp_in_production")) return "TP IN PRODUCTION REPORT (VOB)";
        if (qryType.equals("vob_tp_history")) return "TP HISTORY REPORT (VOB)";
        if (qryType.equals("vob_prod_lib_hist")) return "PRODUCTION LIBRARY HISTORY REPORT (VOB)";
        if (qryType.equals("vob_my_linesets")) return "MY LINESET REPORT (VOB)";
        if (qryType.equals("vob_lineset_submits")) return "MY LINESET IN PROGRESS REPORT (VOB)";
        if (qryType.equals("vob_lineset_history")) return "LINESET HISTORY REPORT (VOB)";
        if (qryType.equals("vob_tp_search")) return "TP SEARCH REPORT (VOB)";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "DELETE TP SEARCH REPORT (VOB)";
        return "";
    }

    static public boolean isSearchFormEnabled(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return false;
        if (qryType.equals("vob_tp_in_progress")) return false;
        if (qryType.equals("vob_tp_in_production")) return false;
        if (qryType.equals("vob_tp_history")) return true;
        if (qryType.equals("vob_tp_search")) return true;
        if (qryType.equals("vob_prod_lib_hist")) return true;
        if (qryType.equals("vob_my_linesets")) return false;
        if (qryType.equals("vob_lineset_submits")) return false;
        return false;
    }

    static public String getXmlRepRootTag(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return "TPS";
        if (qryType.equals("vob_tp_in_progress")) return "TPS";
        if (qryType.equals("vob_tp_in_production")) return "TPS";
        if (qryType.equals("vob_tp_history")) return "TPS";
        if (qryType.equals("vob_tp_search")) return "TPS";
        if (qryType.equals("vob_prod_lib_hist")) return "TPS";
        if (qryType.equals("vob_my_linesets")) return "LineSET_LIST";
        if (qryType.equals("vob_lineset_submits")) return "LineSET_LIST";
        if (qryType.equals("vob_lineset_history")) return "REP";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "LS_DETAILS";
        return "";
    }

    static public String getXmlRowTag(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return "TP";
        if (qryType.equals("vob_tp_in_progress")) return "TP";
        if (qryType.equals("vob_tp_in_production")) return "TP";
        if (qryType.equals("vob_tp_history")) return "TP";
        if (qryType.equals("vob_tp_search")) return "TP";
        if (qryType.equals("vob_prod_lib_hist")) return "TP";
        if (qryType.equals("vob_my_linesets")) return "LS";
        if (qryType.equals("vob_lineset_submits")) return "LS";
        if (qryType.equals("vob_lineset_history")) return "BS";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "TPS_SUM";
        return "";
    }

    String getStatus1Filter(HttpSession session, String qryType, String workMode) {
        if (qryType.equals("vob_tp_to_process")) return "Distributed";
        if (qryType.equals("vob_tp_in_progress")) return "In_Validation";
        if (qryType.equals("vob_tp_in_production")) return "In_Production";
        if (qryType.equals("vob_prod_lib_hist")) return "In_Production";
        return "";
    }

    String getStatus2Filter(HttpSession session, String qryType, String workMode) {
        if (qryType.equals("vob_tp_in_progress")) return "Ready_to_production";
        if (qryType.equals("vob_prod_lib_hist")) return "Obsolete";
        return "";
    }

    String getUserFilter(HttpSession session, String qryType) {

        String unixUser = (String) session.getAttribute("unixUser");
        String workMode = (String) session.getAttribute("workMode");
        if ((workMode == null) || (workMode.equals("RECWORK"))) {
            if (qryType.equals("vob_tp_to_process")) return unixUser;
            if (qryType.equals("vob_tp_in_progress")) return unixUser;
            if (qryType.equals("vob_tp_in_production")) return unixUser;

            if (qryType.equals("vob_tp_search")) return unixUser;
            if (qryType.equals("vob_tp_history")) return unixUser;

            if (qryType.equals("vob_prod_lib_hist")) return unixUser;
            return "";
        } else
            return "";
    }


    /**
     * private String getUserFilter(HttpSession session, String qryType) {
     * <p/>
     * String unixUser = (String) session.getAttribute("unixUser");
     * String workMode = (String) session.getAttribute("workMode");
     * String currentUser = (String) session.getAttribute("user");
     * String result = "";
     * if (currentUser.equals(TpmsConfiguration.getInstance().getCommonEngineerLogin()) && workMode.equals("RECWORK")){
     * result = "";
     * } else if ( ((workMode == null) || workMode.equals("RECWORK"))) {
     * if (qryType.equals("vob_tp_to_process")) {
     * result = unixUser;
     * } else if (qryType.equals("vob_tp_in_progress")) {
     * result = unixUser;
     * } else if (qryType.equals("vob_tp_in_production")) {
     * result = unixUser;
     * //if (qryType.equals("vob_tp_history")) return unixUser;
     * } else if (qryType.equals("vob_prod_lib_hist")) {
     * result = unixUser;
     * }
     * }
     * debugLog("VobQryServlet :: getUserFilter : unixUser = " + unixUser + " workMode = " + workMode + " result = " + result);
     * return result;
     * }
     */

    String getOwnerFilter(HttpSession session, String qryType) {
        String unixUser = (String) session.getAttribute("unixUser");
        String workMode = (String) session.getAttribute("workMode");
        if ((workMode == null) || (workMode.equals("SENDWORK"))) {
            return unixUser;
        } else
            return "";
    }

    String getDebugXmlFileName(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return "vob_tp_rep.xml";
        if (qryType.equals("vob_tp_in_progress")) return "vob_tp_rep.xml";
        if (qryType.equals("vob_tp_in_production")) return "vob_tp_rep.xml";
        if (qryType.equals("vob_tp_history")) return "vob_tp_hist.xml";
        if (qryType.equals("vob_prod_lib_hist")) return "vob_tp_rep.xml";
        if (qryType.equals("vob_tp_search")) return "vob_tp_search.xml";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "vob_delete_tp_search.xml";
        return "";
    }

    String getXslFileName(String qryType) {
        if (qryType.equals("vob_tp_to_process")) return "tp_query_vob.xsl";
        if (qryType.equals("vob_tp_in_progress")) return "tp_query_vob.xsl";
        if (qryType.equals("vob_tp_in_production")) return "tp_query_vob.xsl";
        if (qryType.equals("vob_tp_history")) return "tp_history_vob.xsl";
        if (qryType.equals("vob_prod_lib_hist")) return "tp_query_vob.xsl";
        if (qryType.equals("vob_tp_search")) return "tp_search_vob.xsl";
        // FP- rev5 gestita una nuova qryType='vob_delete_tp_search' per Delete Tp from Vob 'R' per Admin
        if (qryType.equals("vob_delete_tp_search")) return "vob_delete_tp_search.xsl";
        return "";
    }


    public String getFilter(HttpServletRequest request, boolean fetchBool, Properties filterProps) throws Exception {
        String filter = "";
        debugLog("fetchBool>" + fetchBool);
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String attrnam = (String) e.nextElement();
            if (attrnam.indexOf("field.") < 0) continue;
            String attrval = request.getParameter(attrnam);
            if (attrval.equals("")) continue;
            request.setAttribute(attrnam, attrval);
            String fieldName = (attrnam.startsWith("fixed.") ? attrnam.substring(12) : attrnam.substring(6));
            if (fetchBool) {
                if (fieldName.equals("JOBNAME")) {
                    filterProps.setProperty(fieldName.toLowerCase(), attrval);
                }
                if (fieldName.equals("JOB_REL")) {
                    filterProps.setProperty("release_nb", attrval);
                }
                request.setAttribute("fixed." + attrnam, attrval);
            }
            if (fieldName.endsWith(".min")) {
                fieldName = fieldName.substring(0, fieldName.length() - 4);
                filter = filter + "[" + "translate(substring(" + fieldName + ",1,10),'.','')" + "&gt;" + getVobDateForm(attrval) +
                        " or " + "translate(substring(" + fieldName + ",1,10),'.','')" + "=" + getVobDateForm(attrval) +
                        "]";
            } else if (fieldName.endsWith(".max")) {
                fieldName = fieldName.substring(0, fieldName.length() - 4);
                filter = filter + "[" + "translate(substring(" + fieldName + ",1,10),'.','')" + "&lt;" + getVobDateForm(attrval) +
                        " or " + "translate(substring(" + fieldName + ",1,10),'.','')" + "=" + getVobDateForm(attrval) +
                        "]";
            } else {
                boolean isLikeOperator = (attrval.indexOf('%') > 0);
                attrval = attrval.replace('%', ' ').trim();
                debugLog("attrval>" + attrval);
                int len = attrval.length();
                filter = filter + "[" + (isLikeOperator ? "substring(" + fieldName + ",1," + len + ")" : fieldName) + "='" + attrval + "'" + "]";
            }
            debugLog("fieldName>" + fieldName);
            debugLog("filter>" + filter);
        }
        return filter;
    }

    static public void buildXslFile(String rootEl, String rootTag, String filter, FileWriter out) throws IOException {
        out.write("<?xml version=\"1.0\"?>\n");
        out.write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">\n");

        out.write("<xsl:template match=\"" + rootEl + "\">\n");
        out.write("<xsl:copy><xsl:apply-templates select=\"" + rootTag + filter + "\"/></xsl:copy>\n");
        out.write("</xsl:template>\n");

        out.write("<xsl:template match=\"@*|*|text()\">\n");
        out.write("<xsl:copy><xsl:apply-templates select=\"@*|*|text()\"/></xsl:copy>\n");
        out.write("</xsl:template>\n");

        out.write("</xsl:stylesheet>\n");
    }

    String getVobDateForm(String date) {
        if (date.length() == 8) return date;
        String day = date.substring(0, 2);
        int m = dateRd.getMonthNum(date.substring(3, 6));
        String month = (m < 10 ? "0" + new Integer(m).toString() : new Integer(m).toString());
        String year = date.substring(7, 11);
        String vobDate = year + month + day;
        debugLog("date>" + vobDate);
        return vobDate;
    }

}