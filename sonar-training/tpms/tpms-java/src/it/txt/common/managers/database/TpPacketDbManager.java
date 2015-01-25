package it.txt.common.managers.database;

import it.txt.afs.packet.utils.PacketConstants;
import it.txt.common.elements.TpPacketElement;
import it.txt.common.list.TpPacketElementsList;
import it.txt.general.utils.GeneralStringUtils;
import tpms.TpmsException;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;

import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;

import javax.sql.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 1-mar-2006
 * Time: 9.54.18
 * This class contains methods that acts on database that return list of packetes and tps
 */
public class TpPacketDbManager extends QueryUtils {

    private static final String DISTRIBUTED_TP_STATUS = "Distributed";//this constyants represent the value of the ditributed status of tps
    private static final int TP_FLAG_VALUE = 1; //this constants will be assigned to thos elements in the tp/packets list if the current element is a tp
    private static final int PACKET_FLAG_VALUE = 0;//this constants will be assigned to thos elements in the tp/packets list if the current element is a packet

    /**
     * this methods retrieve the list of received tp and packets in a unique list: the condition used to identify tp are:
     * Status = 'Distributed', valid_login or prod_login = recieverLogin
     * For packets are:
     * Status = 'Sent', first_recieve_login or second_recieve_login = recieverLogin
     *
     * @param recieverLogin
     * @return TpPacketElementsList containing the tp or packages that are received by the the current user (recieverLogin)
     * @throws TpmsException if an error occours
     */
    public static TpPacketElementsList retrieveRecievedTpAndPacketsList(String recieverLogin) throws TpmsException {

        if (GeneralStringUtils.isEmptyString(recieverLogin)) {
            throw new TpmsException("Unable to retrieve received tp and packets: the given user is null or empty", "", "TpPacketDbManager :: retrieveRecievedTpAndPacketsList");
        }
        TpPacketElementsList tpPackageElementsList = new TpPacketElementsList();
        String query = "select id, jobname, job_release, job_revision, from_plant, owner_login, TO_CHAR(distrib_date, '" + ORACLE_DATE_FORMAT + "') as distrib_date, status, is_tp " +
                "from (" +
                "        select id, name as jobname, tp_release as job_release, tp_revision as job_revision, from_plant, sender_login as owner_login, sent_date as distrib_date, status, " + PACKET_FLAG_VALUE + " as is_tp " +
                "        from afs_packet " +
                "        where status = '" + PacketConstants.SENT_PACKET_STATUS + "' and (first_recieve_login = '" + recieverLogin + "' or second_recieve_login = '" + recieverLogin + "') and (destination_plant = '" + tpmsConfiguration.getLocalPlant() + "')" +
                "     union " +
                "        select (JOBNAME || '_' || JOB_RELEASE || '_' || JOB_REVISION || '_' || TPMS_VER) as ID, jobname, '' || job_release, '' || job_revision, from_plant, owner_login, distrib_date, status, " + TP_FLAG_VALUE + " as is_tp " +
                "        from tp_plant " +
                "        where lower(status) = lower('" + DISTRIBUTED_TP_STATUS + "') and (valid_login = '" + recieverLogin + "' or prod_login = '" + recieverLogin + "') and (to_plant = '" + tpmsConfiguration.getLocalPlant() + "')" +
                ") " +
                "order by distrib_date asc, from_plant desc, owner_login desc, jobname desc";

        debugLog("TpPacketDbManager :: retrieveRecievedTpAndPacketsList : query = " + query);
//        ResultSet rs = executeSelectQuery(query);
        SQLInterface iface = new SQLInterface();
        CachedRowSet rs = iface.execQuery(query);
        
        if (rs != null) {
            try {
                TpPacketElement tmpTpPacketElement;
                Date formattedDistributionDate = null;
                String pkgId;
                while (rs.next()) {
                    pkgId = rs.getString("id");
                    try {
                        formattedDistributionDate = simpleDateFormat.parse(rs.getString("distrib_date"));
                    } catch (ParseException e) {
                        errorLog("TpPacketDbManager :: retrieveRecievedTpAndPacketsList : unable to parse distribution date (element id = " + pkgId, e);
                    }
                    tmpTpPacketElement = new TpPacketElement(pkgId, rs.getString("jobname"), rs.getString("job_release"), rs.getString("job_revision"), rs.getString("from_plant"),
                            rs.getString("owner_login"), formattedDistributionDate, rs.getString("status"), (rs.getInt("is_tp") == TP_FLAG_VALUE));
                    tpPackageElementsList.addElement(tmpTpPacketElement);
                }
            } catch (SQLException e) {
                errorLog("TpPacketDbManager :: retrieveRecievedTpAndPacketsList : SQLException " + e.getMessage(), e);
            } finally {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tpPackageElementsList;
    }
}
