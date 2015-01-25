package tpms;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tol.LogWriter;
import tol.xmlRdr;
import tpms.utils.TpmsConfiguration;

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


public class TpDetailViewServlet extends AfsGeneralServlet {
    LogWriter log = null;
    protected TpmsConfiguration tpmsConfiguration = null;



    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.log = (LogWriter) this.getServletContext().getAttribute("log");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = getServletConfig().getInitParameter("nextPage");
        HttpSession session = request.getSession();
        String unixUser = (String) session.getAttribute("unixUser");

            debugLog("TpDetailViewServlet :: doPost : ACTION-START- TpDetailViewServlet");
            String repFileName = request.getParameter("repFileName");
            String source = request.getParameter("source");
            String qryType = ((request.getParameter("qryType")) != null ? request.getParameter("qryType") : "");
            String jobName = request.getParameter("jobname");
            String jobRel = request.getParameter("job_rel");
            String jobRev = request.getParameter("job_rev");
            String jobVer = request.getParameter("job_ver");
            debugLog("TpDetailViewServlet :: doPost : USER-" + unixUser);
            debugLog("TpDetailViewServlet :: doPost : Get Data jobName-" + jobName + ", jobRel-" + jobRel + ", jobRev-" + jobRev + ", jobVer-" + jobVer);
            debugLog("TpDetailViewServlet :: doPost : Get Data from repFileName-" + repFileName);
            debugLog("TpDetailViewServlet :: doPost : Get Data from source-" + source);
        Element tpRec;
        try {
            tpRec = getTpData(repFileName, source, jobName, jobRel, jobRev, jobVer);
            debugLog("TpDetailViewServlet :: doPost : tpRec OK");
            TpFlowMgr.checkAvailableActions(unixUser, tpRec);
        } catch (Exception e) {
            manageError(e, request, response);
            return;
        }
            session.setAttribute("tpData", tpRec.getOwnerDocument());
            session.setAttribute("tpRec", tpRec);
            request.setAttribute("source", source);
            request.setAttribute("qryType", qryType);
            request.setAttribute("repFileName", repFileName);
            CtrlServlet.setAttributes(request);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);

    }

    public static String getTpLabel(Element tpRec) throws Exception {
        String jobnam = XmlUtils.getVal(tpRec, "JOBNAME");
        String jobrel = XmlUtils.getVal(tpRec, "JOB_REL");
        jobrel = (jobrel.length() < 2 ? "0" + jobrel : jobrel);
        String jobrev = XmlUtils.getVal(tpRec, "JOB_REV");
        jobrev = (jobrev.length() < 2 ? "0" + jobrev : jobrev);
        String jobver = XmlUtils.getVal(tpRec, "JOB_VER");
        jobver = (jobver.length() < 2 ? "0" + jobver : jobver);
        return "TP_" + jobnam + "." + jobrel + "." + jobrev + "." + jobver;
    }

    public static Element getTpData(HttpServletRequest request) throws Exception {
        return getTpData(request, null);
    }

    public static Element getTpData(HttpServletRequest request, Element tpRec) throws Exception {
        Properties props = new Properties();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String parnam = (String) e.nextElement();
            if (parnam.startsWith("FIELD.")) {
                props.setProperty(parnam.substring(6), request.getParameter(parnam));
            }
        }
        return getTpData(props, tpRec);
    }

    public static Element getTpData(Properties props) throws Exception {
        return getTpData(props, null);
    }

    public static Element getTpData(Properties props, Element tpRec) throws Exception {
        tpRec = (tpRec != null ? tpRec : XmlUtils.getNewDoc("TP").getDocumentElement());
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {

            String parnam = (String) e.nextElement();
            String tpFieldVal = props.getProperty(parnam);
            //debugLog("TpDetailServlet :: getTpData : param = " + parnam + " tpFieldVal = " + tpFieldVal );
            if (XmlUtils.getChild(tpRec, parnam.toUpperCase()) != null) XmlUtils.setVal(tpRec, parnam.toUpperCase(), tpFieldVal);
            else
                XmlUtils.addEl(tpRec, parnam.toUpperCase(), tpFieldVal);
        }
        if (XmlUtils.getChild(tpRec, "TP_LABEL") == null) {
            XmlUtils.addEl(tpRec, "TP_LABEL", getTpLabel(tpRec));
        }
        return tpRec;
    }

    public static Element getTpData(String repFileName, String source, Properties props) throws Exception {
        String jobName = props.getProperty("jobname");
        String jobRel = props.getProperty("job_rel");
        String jobRev = props.getProperty("job_rev");
        String jobVer = props.getProperty("job_ver");
        String toPlant = props.getProperty("to_plant");
        return getTpData(repFileName, source, jobName, jobRel, jobRev, jobVer, (toPlant != null ? toPlant : ""));
    }

    /**
     * This method starting from an XML file name return an XML Element that represents TP data identified by given
     * jobName, jobRel, jobRev, jobVer, the source could be "db" or "vob" in order to undertand if it has to
     * manage xml data coming from db or from vob.
     *
     * @param repFileName
     * @param source
     * @param jobName
     * @param jobRel
     * @param jobRev
     * @param jobVer
     * @return
     * @throws Exception This method starting from an XML file name return an XML Element that represents TP data
     */
    public static Element getTpData(String repFileName, String source, String jobName, String jobRel, String jobRev, String jobVer) throws Exception {
        return getTpData(repFileName, source, jobName, jobRel, jobRev, jobVer, "");
    }

    /**
     * This method starting from an XML file name return an XML Element that represents TP data identified by given
     * jobName, jobRel, jobRev, jobVer, toPlant, the source could be "db" or "vob" in order to undertand if it has to
     * manage xml data coming from db or from vob.
     *
     * @param repFileName
     * @param source
     * @param jobName
     * @param jobRel
     * @param jobRev
     * @param jobVer
     * @param toPlant
     * @return
     * @throws Exception This method starting from an XML file name return an XML Element that represents TP data
     */
    public static Element getTpData(String repFileName, String source, String jobName, String jobRel, String jobRev, String jobVer, String toPlant) throws Exception{
        String xslFileName = CtrlServlet._webAppDir + "/" + (source.equals("db") ? "tp_sel_db.xsl" : "tp_sel_vob.xsl");
        String tempOutFile = repFileName + ".out" + ".xml";
        Properties xslProps = new Properties();
        xslProps.setProperty("jobname", jobName);
        xslProps.setProperty("job_rel", jobRel);
        xslProps.setProperty("job_rev", jobRev);
        xslProps.setProperty("job_ver", jobVer);
        xslProps.setProperty("to_plant", toPlant);

        Document doc;

        File fOut = new File(tempOutFile);
        FileWriter fWrtr = new FileWriter(fOut);
        xmlRdr.transform(repFileName, xslFileName, xslProps, fWrtr);
        fWrtr.flush();
        fWrtr.close();
        doc = xmlRdr.getRoot(tempOutFile, false).getOwnerDocument();
        fOut.delete();

        return (source.equals("vob") ? doc.getDocumentElement() : (Element) doc.getDocumentElement().getElementsByTagName("STDATA_RECORD").item(0));
    }

    public static Element getTpData(String repFileName, String source, String jobLabel) throws Exception {
        String xslFileName = CtrlServlet._webAppDir + "/" + (source.equals("db") ? "tp_sel_db.xsl" : "tp_sel_vob.xsl");
        String tempOutFile = repFileName + ".out" + ".xml";
        Properties xslProps = new Properties();
        xslProps.setProperty("jobLabel", jobLabel);

        Document doc;

        File fOut = new File(tempOutFile);
        FileWriter fWrtr = new FileWriter(fOut);
        xmlRdr.transform(repFileName, xslFileName, xslProps, fWrtr);
        fWrtr.flush();
        fWrtr.close();
        doc = XmlUtils.getRoot(tempOutFile).getOwnerDocument();
        fOut.delete();

        return (source.equals("vob") ? doc.getDocumentElement() : (Element) doc.getDocumentElement().getElementsByTagName("STDATA_RECORD").item(0));
    }

    public static void main(String args[]) throws Exception {
        Properties props = new Properties();
        props.setProperty("JOBNAME", "V259");
        props.setProperty("JOB_REL", "01");
        props.setProperty("JOB_REV", "05");
        props.setProperty("JOB_VER", "99");
        props.setProperty("FACILITY", "EWS");
        Element tpRec = TpDetailViewServlet.getTpData(props);
        XmlUtils.print(tpRec.getOwnerDocument(), System.out);
    }
}

