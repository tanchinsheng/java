package tpms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 1, 2003
 * Time: 1:59:37 PM
 * To change this template use Options | File Templates.
 */


public class LsVobQryServlet extends VobQryServlet {

    String getStatus1Filter(HttpSession session, String qryType, String workMode) {
        if (qryType.equals("vob_my_linesets")) return "READY";
        if (qryType.equals("vob_lineset_submits")) return "INPROGRESS";

        return "";
    }

    String getStatus2Filter(HttpSession session, String qryType, String workMode) {
        if (qryType.equals("vob_lineset_submits")) return "ABORTED";
        return "";
    }

    String getUserFilter(HttpSession session, String qryType) {
        return "";
    }

    String getOwnerFilter(HttpSession session, String qryType) {
        String unixUser = (String) session.getAttribute("unixUser");
        String workMode = (String) session.getAttribute("workMode");
        if ((workMode == null) || (workMode.equals("SENDWORK"))) {
            return unixUser;
        } else
            return "";
    }

    String getDebugXmlFileName(String qryType) {
        if (qryType.equals("vob_my_linesets")) return "vob_ls_rep.xml";
        if (qryType.equals("vob_lineset_submits")) return "vob_ls_rep.xml";
        if (qryType.equals("vob_lineset_history")) return "vob_ls_history_rep.xml";
        return "";
    }

    String getXslFileName(String qryType) {
        if (qryType.equals("vob_my_linesets")) return "ls_query_vob.xsl";
        if (qryType.equals("vob_lineset_submits")) return "ls_query_vob.xsl";
        if (qryType.equals("vob_lineset_history")) return "ls_history_vob.xsl";
        return "";
    }

    public String getFilter(HttpServletRequest request, boolean fetchBool, Properties filterProps) throws Exception {
        String filter = "";
        debug("fetchBool>" + fetchBool);
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String attrnam = (String) e.nextElement();
            if ((attrnam.indexOf("field.") < 0) && (attrnam.indexOf("FIELD.") < 0)) continue;
            String attrval = request.getParameterValues(attrnam)[0];
            if (attrval.equals("")) continue;
            request.setAttribute(attrnam, attrval);
            String fieldName = (attrnam.startsWith("fixed.") ? attrnam.substring(12) : attrnam.substring(6));
            if (fetchBool) {
                if (fieldName.equals("LS_NAME")) {
                    filterProps.setProperty(fieldName.toLowerCase(), attrval);
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
                debug("attrval>" + attrval);
                int len = attrval.length();
                filter = filter + "[" + (isLikeOperator ? "substring(" + fieldName + ",1," + len + ")" : fieldName) + "='" + attrval + "'" + "]";
            }
            debug("fieldName>" + fieldName);
            debug("filter>" + filter);
        }
        return filter;
    }


}

