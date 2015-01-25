package it.txt.tpms.reports.request;

import it.txt.general.utils.GeneralStringUtils;


import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-mar-2006
 * Time: 13.16.14
 * To change this template use File | Settings | File Templates.
 */
public class ReportRequestObjectsList {
//OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private Vector reportRequestList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the ReportRequests list
    private Enumeration reportRequestEnum = null;

    /**
     * default contructor
     */
    public ReportRequestObjectsList() {
        reportRequestList = new Vector();
    }

    /**
     * add reportRequest to the list
     *
     * @param reportRequest the reportRequest to be added
     * @return KO_RESULT if the given reportRequest is null or do not have an id, OK_RESULT otherwirse
     */
    public int addReportRequest(ReportRequestObject reportRequest) {
        if (reportRequest == null || GeneralStringUtils.isEmptyString(reportRequest.getId()))
            return KO_RESULT;
        else {
            reportRequestList.add(reportRequest);
            reportRequestEnum = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a ReportRequests from the ReportRequests list
     *
     * @param reportRequestKey the key that identifies a ReportRequests in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given ReportRequests key is null or empty)
     */
    public int removeReportRequest(String reportRequestKey) {
        if (!GeneralStringUtils.isEmptyString(reportRequestKey)) {
            reportRequestList.remove(reportRequestKey);
            reportRequestEnum = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a reportRequest from the reportRequest list
     *
     * @param reportRequest the reportRequest in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given reportRequest key is null or empty)
     */
    public int removeReportRequest(ReportRequestObject reportRequest) {
        if (reportRequest != null && !GeneralStringUtils.isEmptyString(reportRequest.getId())) {
            return this.removeReportRequest(reportRequest.getId());
        } else {
            return KO_RESULT;
        }
    }


    /**
     * true if the given reportRequest is present in the list, false otherwise
     *
     * @param reportRequest the reportRequest to look for.
     * @return true if the given reportRequest is present in the list, false otherwise
     */
    public boolean containsReportRequest(ReportRequestObject reportRequest) {
        if (reportRequest != null && reportRequestList != null) {
            return reportRequestList.contains(reportRequest);
        }
        return false;
    }

    /**
     * return the ReportRequests identified by the given ReportRequests id
     *
     * @param index
     * @return the ReportRequests identified by the given ReportRequests id
     */
    public ReportRequestObject getReportRequest(int index) {
        if (reportRequestList != null && index >= 0) {
            return (ReportRequestObject) reportRequestList.get(index);
        }
        return null;
    }

    /**
     * Tests if this reportRequestList contains more elements
     *
     * @return true if and only if this reportRequestList contains at least one more ReportRequests to provide; false otherwise.
     */
    public boolean hasMoreReportRequests() {
        if (reportRequestEnum == null) {
            reportRequestEnum = reportRequestList.elements();
        }
        return reportRequestEnum.hasMoreElements();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next ReportRequests of this ReportRequests list.
     */
    public ReportRequestObject nextReportRequest() {
        if (reportRequestEnum == null) {
            reportRequestEnum = reportRequestList.elements();
        }
        return (ReportRequestObject) reportRequestEnum.nextElement();
    }

    /**
     * @return Returns the number of ReportRequests in this reportRequestList.
     */
    public int size() {
        return reportRequestList.size();
    }

    /**
     * @return Tests if this reportRequestList contains no ReportRequests.
     */
    public boolean isEmpty() {
        return reportRequestList.isEmpty();
    }
}
