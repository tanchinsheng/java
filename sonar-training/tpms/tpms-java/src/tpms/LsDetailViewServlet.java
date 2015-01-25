package tpms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tol.xmlRdr;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 3, 2003
 * Time: 1:59:37 PM
 * To change this template use Options | File Templates.
 * FP rev5 - gestione nuova qrytype=ls_details_for_tp_delete in ls_viwe_out.jsp
 */


public class LsDetailViewServlet extends LsVobQryServlet {


    public String getCCAction(String qryType) {
        if (qryType.equals("ls_query_details")) return "ls_query_details";
        //FP 3/06/2005 rev5
        if (qryType.equals("ls_details_for_tp_delete")) return "ls_details_for_tp_delete";
        return null;
    }


    static public String getReportTitle(String qryType) {
        if (qryType.equals("ls_query_details")) return "LINESET DATA (VOB)";
        return "";
    }

    String getDebugXmlFileName(String qryType) {
        if (qryType.equals("ls_query_details")) return "vob_ls_det_rep.xml";
        if (qryType.equals("ls_details_for_tp_delete")) return "vob_ls_det_for_tp_delete_rep.xml";
        if (qryType.equals("vob_lineset_history")) return "vob_ls_det_rep.xml";
        return "";
    }

    String getXslFileName(String qryType) {
        if (qryType.equals("vob_my_linesets")) return "ls_view_vob.xsl";
        if (qryType.equals("vob_lineset_history")) return "ls_view_vob.xsl";
        return "";
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        debug("start lsDetailViewservlet");
        String qryType = request.getParameterValues("qryType")[0];
        HttpSession session = request.getSession();
        String REQID = (!_DEBUG ? session.getId() + "_" + Long.toString(System.currentTimeMillis()) : qryType);
        String repFileName = null;
        // RepFileName to create from CC Command
        //String repFileNameCc=null ;
        String xmlFileName = null;
        boolean fetchBool;
        String nextPage = getServletConfig().getInitParameter("nextPage");
        String outPage = getServletConfig().getInitParameter("outPage");
        String unixUser = (String) session.getAttribute("unixUser");
        String actionTxt = getServletConfig().getInitParameter("actionTxt");
        String filter = "";
        try {
            if (session.getAttribute("vob") == null) {
                throw new NoVobSelectedException(actionTxt + " ABORTED");
            }
            String vob = (String) session.getAttribute("vob");
            String workMode = (String) session.getAttribute("workMode");

            if (request.getParameterValues("repFileName") == null) {
                if (request.getParameterValues("xmlFileName") != null) {
                    /**
                     * LINESET DATA KEPT FROM THE XML LINESETS RECORDSET
                     */

                    xmlFileName = request.getParameterValues("xmlFileName")[0];
                    String linesetName = request.getParameterValues("FIELD.LS_NAME")[0];
                    Element lsRec = getLsData(xmlFileName, "vob", linesetName);
                    session.setAttribute("lsRec", lsRec);
                    request.setAttribute("showTpList", "Y");
                    request.setAttribute("qryType", qryType);
                } else {
                    fetchBool = true;
                    if (session.getAttribute("exception" + "_" + REQID) != null) session.removeAttribute("exception" + "_" + REQID);
                    session.setAttribute("startBool" + "_" + REQID, new Boolean(true));
                    repFileName = (!_DEBUG ? _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml" : _webAppDir + "/" + "images" + "/" + getDebugXmlFileName(qryType));
                    /* DVL Execution
                    *  if dvlBool=true --> RepFileNameCc producec in Unix System
                    *  if dvlBool=false --> RepFileNameCc producec in Windows System
                    */
                    /*
                    if(_DEBUG)
                    {
                      repFileNameCc= _webAppDir+"/"+"images"+"/"+getDebugXmlFileName(qryType);
                    }else if (_dvlBool)
                    {
                      repFileNameCc=_dvlUnixAppDir+"/"+"images"+"/"+REQID+"_rep.xml";
                    }else{
                      repFileNameCc=_webAppDir+"/"+"images"+"/"+REQID+"_rep.xml";
                    }
                    */
                    Properties filterProps = new Properties();
                    filterProps.setProperty("filter1", getStatus1Filter(session, qryType, workMode));
                    filterProps.setProperty("filter2", getStatus2Filter(session, qryType, workMode));
                    filter = getFilter(request, fetchBool, filterProps);
                    debug("VOB-ACTION-START-" + REQID + ">");
                    debug("QRY-TYPE-" + REQID + ">" + qryType);
                    debug("USER-" + REQID + ">" + unixUser);
                    doQry(session, REQID, repFileName, (!_DEBUG ? _ccScriptsDir : _webAppDir + "/" + "images"), qryType, unixUser, vob, _timeOut, actionTxt, filterProps);
                    request.setAttribute("showTpList", "N");
                    request.setAttribute("qryType", qryType);
                }
            } else {
                fetchBool = false;
                repFileName = request.getParameterValues("repFileName")[0];
                insertNewTp((Element) session.getAttribute("newTpRec"), repFileName);
                request.setAttribute("showTpList", "N");
                request.setAttribute("qryType", qryType);
            }
            request.setAttribute("status1Filter", getStatus1Filter(session, qryType, workMode));
            request.setAttribute("status2Filter", getStatus2Filter(session, qryType, workMode));
            debug("user-filt>" + getUserFilter(session, qryType));
            request.setAttribute("userFilter", getUserFilter(session, qryType));
            request.setAttribute("ownerFilter", getOwnerFilter(session, qryType));
            request.setAttribute("outPage", outPage);
            request.setAttribute("actionTxt", actionTxt);
            request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
            request.setAttribute("reqId", REQID);
            if (xmlFileName != null) request.setAttribute("xmlFileName", xmlFileName);
            if (repFileName != null) request.setAttribute("repFileName", repFileName);
            request.setAttribute("xslFileName", getXslFileName(qryType));
            request.setAttribute("repTitle", getReportTitle(qryType));
            request.setAttribute("qryType", qryType);
            if (!filter.equals("")) {
                request.setAttribute("filterXslFile", repFileName + ".xsl");
                FileWriter fout = new FileWriter(repFileName + ".xsl");
                buildXslFile(getXmlRepRootTag(qryType), getXmlRowTag(qryType), filter, fout);
                fout.flush();
                fout.close();
            }
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        }
        catch (NoVobSelectedException e) {
            session.setAttribute("startBool" + "_" + REQID, new Boolean(false));
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + e.getFwdPage());
            rDsp.forward(request, response);
        }
        catch (Exception e) {
            session.setAttribute("startBool" + "_" + REQID, new Boolean(false));
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }
    }

    public static Element getLsData(HttpServletRequest request) throws Exception {
        return getLsData(request, null);
    }

    public static Element getLsData(HttpServletRequest request, Element lsRec) throws Exception {
        Properties props = new Properties();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String parnam = (String) e.nextElement();
            if (parnam.startsWith("FIELD.")) {
                props.setProperty(parnam.substring(6), request.getParameterValues(parnam)[0]);
            }
        }
        return getLsData(props, lsRec);
    }

    public static Element getLsData(Properties props) throws Exception {
        return getLsData(props, null);
    }

    public static Element getLsData(Properties props, Element lsRec) throws Exception {
        lsRec = (lsRec != null ? lsRec : xmlRdr.getNewDoc("LS").getDocumentElement());
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String parnam = (String) e.nextElement();
            String tpFieldVal = props.getProperty(parnam);
            if (xmlRdr.getChild(lsRec, parnam.toUpperCase()) != null) xmlRdr.setVal(lsRec, parnam.toUpperCase(), tpFieldVal);
            else
                xmlRdr.addEl(lsRec, parnam.toUpperCase(), tpFieldVal);
        }
        return lsRec;
    }


    public static Element getLsData(String repFileName, String source, String linesetName) throws Exception {
        String xslFileName = CtrlServlet._webAppDir + "/" + "ls_sel_vob.xsl";
        String tempOutFile = repFileName + ".out" + ".xml";
        Properties xslProps = new Properties();
        xslProps.setProperty("lineset_name", linesetName);

        Document doc;

        File fOut = new File(tempOutFile);
        FileWriter fWrtr = new FileWriter(fOut);
        xmlRdr.transform(repFileName, xslFileName, xslProps, fWrtr);
        fWrtr.flush();
        fWrtr.close();
        doc = xmlRdr.getRoot(tempOutFile, false).getOwnerDocument();
        fOut.delete();

        return doc.getDocumentElement();
    }


    public static void insertNewTp(Element tpRec, String repFileName) throws Exception {
        Element root = xmlRdr.getRoot(repFileName, false);
        xmlRdr.copy(tpRec, root);
        FileWriter fOut = new FileWriter(repFileName);
        xmlRdr.print(root.getOwnerDocument(), fOut);
        fOut.flush();
        fOut.close();
    }

    public static void main(String args[]) throws Exception {
        Element lsRec = LsDetailViewServlet.getLsData("C:/jakarta-tomcat-3.2.3/webapps/tpms/images/vob_ls_rep.xml", "vob", "netline");
        xmlRdr.print(lsRec.getOwnerDocument(), System.out);
    }
}


