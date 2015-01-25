package it.txt.tpms.lineset;

import it.txt.afs.AfsCommonClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.filters.list.LinesetFilterList;
import it.txt.tpms.lineset.utils.LinesetConstants;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.users.manager.TpmsUsersManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import tpms.VobManager;
import tpms.utils.Vob;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 14-set-2006
 * Time: 13.15.50
 */
public class Lineset extends AfsCommonClass implements Comparable{


    private String id = null;
    private String name = null;//*
    private String owner = null;//*
    private String testerFamily = null;//*
    private String unixBaseDirectory = null;//*
    private String syncroDir = null;
    private Date lastActionDate = null;
    private String baseline = null;
    private int filesCount = -1;
    private float linesetSizeKB = -1;
    private String submitStatus = null;
    private String vobName = null;//*
    private Vob vob = null;
    private TpmsUser ownerTpmsUser = null;
    protected final SimpleDateFormat dateFormat = new SimpleDateFormat( tpmsConfiguration.getAfsDatesFormat() );
    private String originalOwnerUnixLogin = null;
    private TpmsUser originalOwnerTpmsUser = null;
    private String plant = null;
    private LinesetFilterList filtersList = null;




    public Lineset ( String linesetName, String actualOwner, String originalOwnerUnixLogin, String testerFamily, String unixBaseDirectory, String syncroDir, Date lastActionDate, String baseline, int filesCount, float linesetSizeKB, String submitStatus, String vobName, String plant ) {
        this.baseline = GeneralStringUtils.isEmptyString( baseline ) ? LinesetConstants.NEW_LINEST_TEMPORAY_BASELINE : baseline;
        this.id = generateId( this.baseline, linesetName, vobName );
        this.name = linesetName;
        this.owner = actualOwner;
        this.testerFamily = testerFamily;
        this.unixBaseDirectory = unixBaseDirectory;
        this.syncroDir = syncroDir;
        this.lastActionDate = lastActionDate;
        this.filesCount = filesCount;
        this.linesetSizeKB = linesetSizeKB;
        this.submitStatus = submitStatus;
        this.vobName = vobName;
        this.vob = VobManager.getVobDataByVobName( vobName );
        this.ownerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( actualOwner );
        this.originalOwnerUnixLogin = originalOwnerUnixLogin;
        this.plant = plant;
    }


    public Lineset ( String linesetName, String owner, String testerFamily, String unixBaseDirectory, String syncroDir, Date lastActionDate, String baseline, int filesCount, float linesetSizeKB, String submitStatus, Vob vob, String originalOwnerUnixLogin, String plant ) {


        this.baseline = GeneralStringUtils.isEmptyString( baseline ) ? LinesetConstants.NEW_LINEST_TEMPORAY_BASELINE : baseline;
        this.id = generateId( this.baseline, linesetName, vobName );
        this.name = linesetName;
        this.owner = owner;
        this.testerFamily = testerFamily;
        this.unixBaseDirectory = unixBaseDirectory;
        this.syncroDir = syncroDir;
        this.lastActionDate = lastActionDate;

        this.filesCount = filesCount;
        this.linesetSizeKB = linesetSizeKB;
        this.submitStatus = submitStatus;
        this.vobName = vob.getName();
        this.vob = vob;
        this.ownerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( owner );
        if ( GeneralStringUtils.isEmptyString( originalOwnerUnixLogin ) ) {
            this.originalOwnerUnixLogin = owner;
            this.originalOwnerTpmsUser = this.ownerTpmsUser;
        } else {
            this.originalOwnerUnixLogin = originalOwnerUnixLogin;
            this.originalOwnerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( originalOwnerUnixLogin );
        }
        this.plant = plant;

    }


    public Lineset ( String linesetName, String vobName, String owner, String unixBaseDirectory, String testerFamily, String baseline, String plant ) {
        this.baseline = GeneralStringUtils.isEmptyString( baseline ) ? LinesetConstants.NEW_LINEST_TEMPORAY_BASELINE : baseline;
        this.id = generateId( this.baseline, linesetName, vobName );
        this.owner = owner;
        this.ownerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( owner );
        this.originalOwnerUnixLogin = owner;
        this.originalOwnerTpmsUser = ownerTpmsUser;
        this.unixBaseDirectory = unixBaseDirectory;
        this.testerFamily = testerFamily;
        this.name = linesetName;
        this.vobName = vobName;
        this.vob = VobManager.getVobDataByVobName( vobName );
        this.plant = plant;
    }

    public Lineset ( String linesetName, Vob vob, String owner, String unixBaseDirectory, String testerFamily, String baseline, String plant ) {
        this.baseline = GeneralStringUtils.isEmptyString( baseline ) ? LinesetConstants.NEW_LINEST_TEMPORAY_BASELINE : baseline;
        this.id = generateId( this.baseline, linesetName, vob.getName() );
        this.owner = owner;
        this.ownerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( owner );
        this.originalOwnerUnixLogin = owner;
        this.originalOwnerTpmsUser = ownerTpmsUser;
        this.unixBaseDirectory = unixBaseDirectory;
        this.testerFamily = testerFamily;
        this.name = linesetName;
        this.vobName = vob.getName();
        this.vob = vob;
        this.plant = plant;
    }


    public Lineset ( String linesetName, Vob vob, TpmsUser owner, String unixBaseDirectory, String testerFamily, String baseline, String plant ) {
        this.baseline = GeneralStringUtils.isEmptyString( baseline ) ? LinesetConstants.NEW_LINEST_TEMPORAY_BASELINE : baseline;
        this.id = generateId( this.baseline, linesetName, vob.getName() );
        this.owner = owner.getName();
        this.ownerTpmsUser = owner;
        this.originalOwnerUnixLogin = owner.getName();
        this.originalOwnerTpmsUser = ownerTpmsUser;
        this.unixBaseDirectory = unixBaseDirectory;
        this.testerFamily = testerFamily;
        this.name = linesetName;
        this.vobName = vob.getName();
        this.vob = vob;
        this.plant = plant;
    }







    //**********Visualization Methods***************/
    private String formatDate ( Date d ) {
        if ( d == null )
            return "";
        else
            return dateFormat.format( d );
    }

    public String getLastActionDateDisplayFormat () {
        return formatDate( this.lastActionDate );
    }

    public String getLinestFullPath () {
        return unixBaseDirectory + syncroDir;
    }


    //*********Common getter methods***************/

    public Vob getVob () {
        if ( vob == null && !GeneralStringUtils.isEmptyString( vobName ) ) {
            vob = VobManager.getVobDataByVobName( vobName );
        }
        return vob;
    }

    public TpmsUser getOriginalOwnerTpmsUser () {
        if ( originalOwnerTpmsUser == null && !GeneralStringUtils.isEmptyString( originalOwnerUnixLogin ) ) {
            this.originalOwnerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( originalOwnerUnixLogin );
        }
        return originalOwnerTpmsUser;
    }

    public String getOriginalOwnerUnixLogin () {
        return originalOwnerUnixLogin;
    }

    public TpmsUser getOwnerTpmsUser () {
        if ( ownerTpmsUser == null && !GeneralStringUtils.isEmptyString( owner ) ) {
            ownerTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( owner );
        }
        return ownerTpmsUser;
    }

    private String generateId ( String baseline, String linesetName, String vobName ) {
        return baseline + "_" + linesetName + "_" + vobName + "_" + System.currentTimeMillis();
    }

    public String getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public String getVobName () {
        return vobName;
    }


    public String getOwner () {
        return owner;
    }

    public String getTesterFamily () {
        return testerFamily;
    }

    public String getUnixBaseDirectory () {
        return unixBaseDirectory;
    }

    public String getSyncroDir () {
        return syncroDir;
    }

    public Date getLastActionDate () {
        return lastActionDate;
    }

    public String getBaseline () {
        return baseline;
    }

    public int getFilesCount () {
        return filesCount;
    }

    public float getLinesetSizeKB () {
        return linesetSizeKB;
    }

    public String getSubmitStatus () {
        return submitStatus;
    }

    public void setSyncroDir ( String syncroDir ) {
        this.syncroDir = syncroDir;
    }

    public void setLastActionDate ( Date lastActionDate ) {
        this.lastActionDate = lastActionDate;
    }

    public void setBaseline ( String baseline ) {
        this.baseline = baseline;
    }

    public void setFilesCount ( int filesCount ) {
        this.filesCount = filesCount;
    }

    public void setLinesetSizeKB ( long linesetSizeKB ) {
        this.linesetSizeKB = linesetSizeKB;
    }

    public void setLinesetSizeKB ( String linesetSizeKB ) {
        try {
            if (!GeneralStringUtils.isEmptyString( linesetSizeKB )) {
                this.linesetSizeKB = Float.parseFloat (linesetSizeKB);
            } else {
                this.linesetSizeKB = -1;
            }
        } catch ( NumberFormatException e ) {
            this.linesetSizeKB = 0;
        }
    }

    public void setSubmitStatus ( String submitStatus ) {
        this.submitStatus = submitStatus;
    }


    public LinesetFilterList getFiltersList () {
        return filtersList;
    }

    public void setFiltersList ( LinesetFilterList filtersList ) {
        this.filtersList = filtersList;
    }

    public String getPlant () {
        return plant;
    }

    public void setPlant ( String plant ) {
        this.plant = plant;
    }

	public boolean equals(Object obj) {
		if(obj instanceof Lineset){
			Lineset ls = (Lineset)obj;
			
            return  this.plant.equals(ls.getPlant()) &&
		            this.name.equals(ls.getName()) &&
		            this.owner.equals(ls.getOwner()) &&
		            this.testerFamily.equals(ls.getTesterFamily()) &&
		            this.vobName.equals(ls.getVobName());
		}
		
		return false;
	}

	public int compareTo(Object obj) {
		if(obj instanceof Lineset){
			Lineset ls = (Lineset)obj;
			
			return this.name.compareTo(ls.getName());
		}
		
		return -1;
	}
}
