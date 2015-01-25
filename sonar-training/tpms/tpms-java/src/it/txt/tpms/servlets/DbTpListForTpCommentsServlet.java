package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.tpms.tp.managers.TPDbManager;
import it.txt.tpms.tp.list.TPList;
import it.txt.tpms.tp.TP;
import it.txt.tpms.users.TpmsUser;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-mag-2006
 * Time: 17.03.44
 * To change this template use File | Settings | File Templates.
 */
public class DbTpListForTpCommentsServlet extends AfsGeneralServlet {
    public static final String SHOW_MY_TP_LIST_ACTION = "showMyTpList";
    public static final String SHOW_TP_COMMENT_ACTION = "showTpComment";
    public static final String MODIFY_TPS_COMMENT_ACTION = "modifyTPsComment";
    public static final String SAVE_TPS_COMMENT_ACTION = "saveTPsComment";
    public static final String EXPORT_TO_CSV_ACTION = "exportToCsv";
    public static final String SEARCH_MY_TP_ACTION = "searchMyTp";

    public static final String TP_LIST_SESSION_ATTRIBUTE_NAME = "tpList";
    public static final String TP_LIST_SELECTED_FOR_COMMENTES_REQUEST_ATTRIBUTE_NAME = "selectedTpList";
    public static final String TP_DATA_SELECTED_REQUEST_ATTRIBUTE_NAME = "selectedTPForDetails";

    public static final String TP_ID_FIELD_NAME = "tpsId";
    public static final String TP_COMMENTS_FIELD_NAME = "field_comment";
    public static final String LINE_SEARCH_FIELD = "line";

    public static final String ERROR_MESSAGE = "error_message";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String nextPage = "";
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        if (currentUser == null) {
            TpmsException e = new TpmsException("You are not authorized to access this page", "tp comments", "You are not authorized to access this page");
            manageError(e, request, response);
        }
        if ((GeneralStringUtils.isEmptyString(action)) || action.equals(SHOW_MY_TP_LIST_ACTION)) {
            //first call...just present the empty page where the user cha search within its TP
            /*
            TPList tpList = null;
            try {
                tpList = TPDbManager.getMyTPs(currentUser.getTpmsLogin());
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            session.setAttribute(TP_LIST_SESSION_ATTRIBUTE_NAME, tpList);*/
            nextPage = getServletConfig().getInitParameter("myTpList");
        } else if (action.equals(SEARCH_MY_TP_ACTION)){
            //the user has selected the line to be searched... retrieve the list of tp according that value
            String line = request.getParameter(LINE_SEARCH_FIELD);
            TPList tpList = null;
            try {
                tpList = TPDbManager.getMyTPs(currentUser.getTpmsLogin(), line);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            session.setAttribute(TP_LIST_SESSION_ATTRIBUTE_NAME, tpList);
            nextPage = getServletConfig().getInitParameter("myTpList");

        } else if (action.equals(SHOW_TP_COMMENT_ACTION)){
            //the user want to see a comment associated with a TP...
            String tpId = request.getParameter(TP_ID_FIELD_NAME);
            TPList userTpList = (TPList) session.getAttribute(TP_LIST_SESSION_ATTRIBUTE_NAME);
            TP selectedTP = userTpList.getElement(tpId);
            request.setAttribute(TP_DATA_SELECTED_REQUEST_ATTRIBUTE_NAME, selectedTP);
            nextPage = getServletConfig().getInitParameter("showTpComment");
        } else if (action.equals(MODIFY_TPS_COMMENT_ACTION)){
            //the user hase selected a set of TP and want to modify/add comment associated..
            String line = request.getParameter(LINE_SEARCH_FIELD);
            request.setAttribute(LINE_SEARCH_FIELD, line);
            String [] selectedTPsIds = request.getParameterValues(TP_ID_FIELD_NAME);
            if (selectedTPsIds == null || selectedTPsIds.length == 0) {
                TpmsException e = new TpmsException("At least one tp should be selected in order to add comment.");
                manageError(e, request, response);
                return;
            }
            TPList selectedTpList = buildTPlistFromTPsId(session, selectedTPsIds);
            request.setAttribute(TP_LIST_SELECTED_FOR_COMMENTES_REQUEST_ATTRIBUTE_NAME, selectedTpList);

            nextPage = getServletConfig().getInitParameter("modifyTpComment");
        } else if (action.equals(SAVE_TPS_COMMENT_ACTION)) {
            //the user want to save the inserted comment associated with the selected tps
            String [] selectedTPsIds = request.getParameterValues(TP_ID_FIELD_NAME);
            String tpsComment = request.getParameter(TP_COMMENTS_FIELD_NAME);
            //controllo lunghezza massima
            tpsComment = TP.getDbFormatCommentsFromFieldFormat(tpsComment);
            debugLog("DbTpListForTpComments :: doPost : SAVE_TPS_COMMENT_ACTION " + tpsComment);
            if (!GeneralStringUtils.isEmptyString(tpsComment) && tpsComment.length() > TP.COMMENT_FILED_MAX_LENGTH ) {
                //if the user has inserted a comment and this comment is too long advise it.
                TPList selectedTpList = buildTPlistFromTPsId(session, selectedTPsIds);
                request.setAttribute(TP_LIST_SELECTED_FOR_COMMENTES_REQUEST_ATTRIBUTE_NAME, selectedTpList);
                nextPage = getServletConfig().getInitParameter("modifyTpComment");
                request.setAttribute(ERROR_MESSAGE, "The comment field contains too much characters, try to reduce them.\nAcutally you have inserted " + tpsComment.length() + " chars.\n\nThe max allowed length depends from the number of new lines you enter:\nwithout any new line the maximum length is " + TP.COMMENT_FILED_MAX_LENGTH +" chars.\n\nEach new line uses " + TP.DB_NEWLINE.length() + " chars.");
                manageRedirect(nextPage, request, response);
                return;
            }

            TPList tpsListForComment = buildTPlistFromTPsId(session, selectedTPsIds);

            try {
                TPDbManager.modifyTPsComment(tpsListForComment, tpsComment, sessionId, currentUser.getTpmsLogin());
            } catch (TpmsException e) {
                manageError(e, request, response);
                return;
            }
            //add the comments to the TPList in memory...retrieve the full set of found TPS
            TPList userTpList = (TPList) session.getAttribute(TP_LIST_SESSION_ATTRIBUTE_NAME);
            TP oneTmpTP;
            tpsListForComment.rewind();
            userTpList.rewind();
            while (tpsListForComment.hasNext()){
                oneTmpTP = userTpList.getElement(tpsListForComment.next().getId());
                oneTmpTP.setDbComments(tpsComment);
            }




            nextPage = getServletConfig().getInitParameter("myTpListServlet");
        } else if (action.equals(EXPORT_TO_CSV_ACTION)){
            nextPage = getServletConfig().getInitParameter("exportToCsv");
        }
        manageRedirect(nextPage, request, response);
    }

    public static boolean showExportButtons(HttpSession session){
        TPList myTPList = (TPList) session.getAttribute(DbTpListForTpCommentsServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
        return myTPList != null && !myTPList.isEmpty();
    }

    private TPList buildTPlistFromTPsId(HttpSession session, String [] selectedTPsIds ){
        TPList userTpList = (TPList) session.getAttribute(TP_LIST_SESSION_ATTRIBUTE_NAME);
        TPList selectedTpList = new TPList();
        for ( int i = 0; i < selectedTPsIds.length; i++ ) {
            selectedTpList.addElement(userTpList.getElement(selectedTPsIds[i]));
        }
        return selectedTpList;
    }
}
