package it.txt.tpms.reports.request;

import it.txt.afs.AfsCommonClass;
import it.txt.tpms.reports.TpReportTypes;
import it.txt.tpms.reports.request.utils.ReportRequestUtils;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-mar-2006
 * Time: 13.15.49
 * To change this template use File | Settings | File Templates.
 */
public class ReportRequestObject extends AfsCommonClass {

    private String id = "";
    private String installationId = "";
    private String ownerTpmsLogin = "";
    private Date startDate = null;
    private Date endDate = null;
    private String criteria = "";
    private String reportType = "";

    protected final SimpleDateFormat dateFormat = new SimpleDateFormat(tpmsConfiguration.getAfsDatesFormat());

/********************methods for visualization***************************/
    private String formatDate(Date d) {
        if (d == null)
            return "";
        else
            return dateFormat.format(d);
    }

    public String getFormattedStartDate() {
        return formatDate(this.startDate);
    }

    public String getFormattedEndDate() {
        return formatDate(this.endDate);
    }

    public String getDisplayReportType() {
        if (reportType.equals(TpReportTypes.TP_LAST_STATUS)){
            return TpReportTypes.TP_LAST_STATUS_DISPLAY;
        }
        return "";
    }

    public String getFormattedCriteria(){
        return ReportRequestUtils.getFormattedCriteria(this.criteria, this.reportType);
    }

    public boolean canBeDeleted(){
        return this.endDate != null;
    }


/********************methods for visualization***************************/
    public ReportRequestObject(String id, String installationId, String ownerTpmsLogin, Date startDate, Date endDate, String criteria, String reportType) {
        this.id = id;
        this.installationId = installationId;
        this.ownerTpmsLogin = ownerTpmsLogin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.criteria = criteria;
        this.reportType = reportType;
    }

    public ReportRequestObject(String id, String installationId, String ownerTpmsLogin, String criteria, String reportType, Date startDate) {
        this.id = id;
        this.installationId = installationId;
        this.ownerTpmsLogin = ownerTpmsLogin;
        this.criteria = criteria;
        this.reportType = reportType;
        this.startDate = startDate;
    }


    public String getId() {
        return id;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getOwnerTpmsLogin() {
        return ownerTpmsLogin;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getReportType() {
        return reportType;
    }

}
