package it.txt.afs.servlets.master;


import it.txt.afs.AfsCommonStaticClass;
import tpms.TpmsException;
import tpms.utils.TpmsConfiguration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.text.SimpleDateFormat;

import tol.LogWriter;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 24-gen-2006
 * Time: 13.04.28
 * To change this template use File | Settings | File Templates.
 */
public class AfsGeneralServlet extends HttpServlet {

    protected TpmsConfiguration tpmsConfiguration = null;
    protected Hashtable parametersValuesList = new Hashtable();

    protected static SimpleDateFormat dateFormat = new SimpleDateFormat(AfsServletUtils.USER_DATE_FORMAT);




    public static String formatDate(Date d){
        return dateFormat.format(d);
    }

    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
    }

    public void retrieveRequestParametersValues(HttpServletRequest request) {
        Enumeration parameterNames = request.getParameterNames();
        String parameterName;
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            parameterName = (String) parameterNames.nextElement();
            parametersValuesList.put(parameterName, request.getParameter(parameterName));
        }
    }

    public void retrieveRequestAttributesValues(HttpServletRequest request) {
        Enumeration attributeNames = request.getAttributeNames();
        String attributeName;
        while (attributeNames != null && attributeNames.hasMoreElements()) {
            attributeName = (String) attributeNames.nextElement();
            parametersValuesList.put(attributeName, request.getParameter(attributeName));
        }
    }


    public void retrieveRequestParametersAndAttributesValues(HttpServletRequest request) {
        retrieveRequestParametersValues(request);
        retrieveRequestAttributesValues(request);
    }

    public void manageRedirect(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
        rDsp.forward(request, response);
    }

    public void manageError(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AfsCommonStaticClass.errorLog(this.getServletName(), e);
        request.setAttribute("exception", e);
        manageRedirect(AfsServletUtils.ERROR_PAGE, request, response);
    }

    public void manageError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AfsCommonStaticClass.errorLog(this.getServletName() + errorMessage);
        TpmsException e = new TpmsException(errorMessage);
        request.setAttribute("exception", e);
        manageRedirect(AfsServletUtils.ERROR_PAGE, request, response);
    }

    public static void debugLog(Object msg) {
        AfsCommonStaticClass.debugLog(msg);
    }

    public static void errorLog(Object msg) {
        AfsCommonStaticClass.errorLog(msg);
    }

    public static void errorLog(Object msg, Throwable t) {
        AfsCommonStaticClass.errorLog(msg, t);
    }

    public static LogWriter getLog(){
        return AfsCommonStaticClass.getLog();
    }
}
