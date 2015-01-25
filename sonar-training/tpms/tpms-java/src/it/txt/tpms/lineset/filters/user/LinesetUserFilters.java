package it.txt.tpms.lineset.filters.user;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.filters.LinesetFilter;
import it.txt.tpms.lineset.filters.list.LinesetFilterList;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
//import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 20-dic-2006
 * Time: 15.45.29
 */
public class LinesetUserFilters {

    private Hashtable userFiltersList = new Hashtable();
    private static final String KEY_VALUES_SEPARATOR = "@#@#";
    
    private Hashtable load(TpmsUser user) throws TpmsException {
    	
    	String commonErrorMessage = "LinesetUserFilters :: load";
    	Hashtable result = new Hashtable();
        String query = "select FILTER_ID, TPMS_LOGIN, FILTER_DISPLAY_VALUE, INSTALLATION_ID, LINESET_NAME, VOB_NAME, OWNER_INSTALLATION_ID " +
                "from TPMS_LINESET_FILTERS";
        if (user != null) {
            query = query + " where TPMS_LOGIN = " + 
            QueryUtils.getStringValueForQuery(user.getTpmsLogin()) + 
            " AND OWNER_INSTALLATION_ID = " + 
            QueryUtils.getStringValueForQuery(user.getInstallationId());
        }
        query = query + " order by LINESET_NAME, VOB_NAME, INSTALLATION_ID";

        AfsCommonStaticClass.debugLog( commonErrorMessage + " : query = " + query );
        ResultSet rs = AfsCommonStaticClass.executeSelectQuery( query );
        
        if (rs != null) {
            LinesetFilter lsFilter;
            LinesetFilterList lsFilterList = new LinesetFilterList();
            String vobName;
            String installationId;
            String linesetName;
            String previousVobName = "";
            String previousInstallationId = "";
            String previousLinesetName = "";
            try {
                int z = 0;
                AfsCommonStaticClass.debugLog(commonErrorMessage + " : STARTED...");
                while (rs.next()) {
                    //AfsCommonStaticClass.debugLog(commonErrorMessage + " inizio giro " + z);	
                    installationId = rs.getString("INSTALLATION_ID");
                    linesetName = rs.getString("LINESET_NAME");
                    vobName = rs.getString("VOB_NAME");
                    //AfsCommonStaticClass.debugLog(commonErrorMessage + " giro " + z + " installationId = " + installationId + " linesetName = " + linesetName + " vobName = " + vobName);

                    if (!rs.isFirst() && !(previousLinesetName.equals(linesetName) && previousVobName.equals(vobName) && previousInstallationId.equals(installationId))) {
                        //se il corrente NON è lo stesso lineset aggiungo alla hashtable la lista corrente e reinizializzo la lista
                        result.put(buildHashtableKey(previousLinesetName, previousVobName, previousInstallationId), lsFilterList);
                        lsFilterList = new LinesetFilterList();
                        //AfsCommonStaticClass.debugLog( commonErrorMessage + " giro " + z + " Inizio filtri relativi a un nuovo ls");
                    }

                    lsFilter = new LinesetFilter(rs.getString("TPMS_LOGIN"), rs.getString("FILTER_ID"), rs.getString("FILTER_DISPLAY_VALUE"), installationId, linesetName, vobName, rs.getString("OWNER_INSTALLATION_ID"));
                    lsFilterList.addElement(lsFilter);
                    previousVobName = vobName;
                    previousLinesetName = linesetName;
                    previousInstallationId = installationId;
                    //ocio se è
                    z++;
                }
                result.put(buildHashtableKey(previousLinesetName, previousVobName, previousInstallationId), lsFilterList);
                AfsCommonStaticClass.debugLog(commonErrorMessage + " : ENDED.");
            } catch (SQLException e) {
                throw new TpmsException("Error while retrieving user lineset filters", commonErrorMessage, e);
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    AfsCommonStaticClass.errorLog(commonErrorMessage + ": error while tring to close resultset: " + e.getMessage(), e);
                }
            }
        }
        return result;
    }


    private String buildHashtableKey(String linesetName, String vobName, String installationId) {
        return (linesetName + KEY_VALUES_SEPARATOR + vobName + KEY_VALUES_SEPARATOR + installationId).trim();
    }

    public LinesetUserFilters() throws TpmsException {
        userFiltersList = load(null);
    }

    public LinesetUserFilters (TpmsUser user) throws TpmsException {
       userFiltersList = load(user);
    }

    public LinesetFilterList getLinesetFiltersList(String linesetName, String vobName, String installationId){
        String commonErrorMessage = "LinesetUserFilters :: getLinesetFiltersList";
        String searchedKey = buildHashtableKey(linesetName, vobName, installationId);
        AfsCommonStaticClass.debugLog(commonErrorMessage + " : STARTED...");
        AfsCommonStaticClass.debugLog(commonErrorMessage + " : searchedKey = " + searchedKey);
        LinesetFilterList result = (LinesetFilterList) userFiltersList.get(searchedKey);
        /*
        Iterator i = userFiltersList.keySet().iterator();
        
        while (i.hasNext()) {
            AfsCommonStaticClass.debugLog(commonErrorMessage + " : ****" + i.next());
        }
        */
        if (result == null) {
            AfsCommonStaticClass.debugLog(commonErrorMessage + " NOT founded and ENDED!");
            return new LinesetFilterList();
        } else {
            AfsCommonStaticClass.debugLog(commonErrorMessage + " founded and ENDED!");
            return result;
        }
    }

    public LinesetFilterList getLinesetFiltersList(Lineset ls) {
        if (ls != null) {
            return this.getLinesetFiltersList(ls.getName(), ls.getVobName(), ls.getPlant());
        }
        return new LinesetFilterList();
    }
}
