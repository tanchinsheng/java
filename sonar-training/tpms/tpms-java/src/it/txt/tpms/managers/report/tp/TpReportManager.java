package it.txt.tpms.managers.report.tp;


import tpms.TpmsException;
import it.txt.afs.AfsCommonClass;
import it.txt.tpms.reports.request.ReportRequestObject;
import it.txt.tpms.reports.request.utils.ReportRequestUtils;
import it.txt.tpms.reports.request.managers.ReportsRequestManager;
import it.txt.general.utils.GeneralStringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 7-mar-2006
 * Time: 12.49.04
 * To change this template use File | Settings | File Templates.
 */
public class TpReportManager extends AfsCommonClass implements Runnable {
    //public static
    private ReportRequestObject reportRequest = null;

    public TpReportManager (ReportRequestObject reportRequest)
    {
        this.reportRequest = reportRequest;
    }

    public TpReportManager (String reportRequestObjectId, String currentUserLogin) throws TpmsException
    {
        this.reportRequest = ReportsRequestManager.getReportRequest(reportRequestObjectId, currentUserLogin);
    }

    /**
     * Starts the command execution
     */
    public void run() {
        if (this.reportRequest != null) {
            debugLog("TpReportManager :: run : started processing " + this.reportRequest.getId());
            //all tp in_production founded with the given criteria.
            String query = "select jobname, job_release, last_action_date " +
                           "from TPMS_TP_PLANT_CLEAN_DATA " +
                           "where status = 'In_Production'";
            String reportCriteria = reportRequest.getCriteria();
            if (!GeneralStringUtils.isEmptyString(reportCriteria)) {
                query = query + " AND " + ReportRequestUtils.getDbFormattedCriteria(reportCriteria);
            }
            ResultSet tpInProductionRs = null;
            try {
                tpInProductionRs = executeSelectQuery(query);
            } catch (TpmsException e) {
                //todo gestisci l'errore.....
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (tpInProductionRs != null) {
                try {
                    while (tpInProductionRs.next()){
                        try {
                           insertTpInReportsData(tpInProductionRs);
                        } catch (SQLException e) {
                            //todo gestire l'errore
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                } catch (SQLException e) {
                    //todo gestire l'errore
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }	finally { //add 17th Dec 2007 to close statement
                	try {
                		tpInProductionRs.close();
                	} catch (SQLException se) {
                		errorLog("TPReportManager :: run : error while closing resultset! ",se);
                	}
                }
            }

        } else {
            errorLog("TpReportManager :: run : nohing to process the given report request is null!");

        }

    }

    private void insertTpInReportsData(ResultSet tpRs) throws SQLException{
        if ( tpRs != null ) {}

    }
}
