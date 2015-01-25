package it.txt.afs.utils;

import it.txt.afs.AfsCommonClass;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 31-gen-2006
 * Time: 14.56.52
 * This class is used in order to maintain the needed information to show on not
 * the wait message in order to wait that the clearcase action ends...
 */
public class AfsWaitInformations extends AfsCommonClass {

    private String servletName;
    private String actionValue;
    private String requestId;


    public AfsWaitInformations(String servletName, String actionValue, String requestId) {
        debugLog("AfsWaitInformations :: constructor : servletName = " + servletName + " actionValue = " + actionValue + " requestId = " + requestId);
        this.servletName = servletName;
        this.actionValue = actionValue;
        this.requestId = requestId;
    }

    public String getServletName() {
        return servletName;
    }

    public String getActionValue() {
        return actionValue;
    }

    public String getRequestId() {
        return requestId;
    }

    public long getReloadTimeout() {
        return tpmsConfiguration.getAfsTimeOut();
    }
}
