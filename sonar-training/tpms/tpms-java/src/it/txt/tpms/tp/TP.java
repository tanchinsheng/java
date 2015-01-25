package it.txt.tpms.tp;

import it.txt.afs.AfsCommonClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tp.managers.ProductionAreaData;
import it.txt.tpms.tp.utils.TPConstants;
import tpms.utils.TpmsConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TP extends AfsCommonClass {

    protected final SimpleDateFormat dateFormat = new SimpleDateFormat(tpmsConfiguration.getAfsDatesFormat());


    public static final String DB_NEWLINE = "#@@#@@";
    public static final String HTML_NEWLINE = "<BR>";
    public static final String NO_NEWLINE = " ";

    public static final String WINDOWS_FIELD_NEWLINE = "\r\n";
    public static final String UNIX_FIELD_NEWLINE = "\r";
    public static final String MAC_FIELD_NEWLINE = "\n";

    public static final int COMMENT_FILED_MAX_LENGTH = TpmsConfiguration.getInstance().getMaxDBCommentsSize();
    private static final int MAX_COMMENT_SHORT_LENGTH = 32;



    private String id = null;

    private String jobName = null;
    private int jobRelease = -1;
    private String jobRevision = null;
    private int tpmsVersion = TPConstants.TPMS_VERSION_DEFAULT_VALUE;
    private String line = null;
    private String facility = null;
    private String fromPlant = null;
    private String ownerLogin = null;
    private String ownerEmail = null;
    private String origin = TPConstants.ORIGIN_DEFAULT_VALUE;
    private String originLabel = null;
    private String linesetName = null;
    private String toPlant = null;
    private String testerInfo = null;
    private String validLogin = null;
    private String prodLogin = null;
    private String lastActionActor = null;
    private Date lastActionDate = null;
    private String status = null;
    private Date distributionDate = null;
    private Date productionDate = null;
    private String division = null;
    private String vobStatus = null;
    private String installationId = null;
    private String prisStatus = null;
    private Date prisFoundDate = null;
    private String dbComments = null;
    private ProductionArea productionArea;
    private String productionAreaId;
    private String deliveryComment = null;
    private String hwModification = null;
    private float expAvgYV;
    private float zeroYW = -1.0f;
    private int newTestTime = -1;
    private String isTemp = null;
    private Date validTill = null;
    private String disEmail = null;


    /*****************CONSTRUCTORS***************************/

    public TP( String jobName, int jobRelease, String jobRevision, int tpmsVersion, String line, String facility, String fromPlant, String ownerLogin, String ownerEmail, String origin, String originLabel, String linesetName, String toPlant, String testerInfo, String validLogin, String prodLogin, String lastActionActor, Date lastActionDate, String status, Date distributionDate, Date productionDate, String division, String vobStatus, String installationId, String prisStatus, Date prisFoundDate, String productionAreaId, String dbComments ) {
        this.id = generateId(jobName, jobRelease, jobRevision, tpmsVersion);
        this.jobName = jobName;
        this.jobRelease = jobRelease;
        this.jobRevision = jobRevision;
        this.tpmsVersion = tpmsVersion;
        this.line = line;
        this.facility = facility;
        this.fromPlant = fromPlant;
        this.ownerLogin = ownerLogin;
        this.ownerEmail = ownerEmail;
        this.origin = origin;
        this.originLabel = originLabel;
        this.linesetName = linesetName;
        this.toPlant = toPlant;
        this.testerInfo = testerInfo;
        this.validLogin = validLogin;
        this.prodLogin = prodLogin;
        this.lastActionActor = lastActionActor;
        this.lastActionDate = lastActionDate;
        this.status = status;
        this.distributionDate = distributionDate;
        this.productionDate = productionDate;
        this.division = division;
        this.vobStatus = vobStatus;
        this.installationId = installationId;
        this.prisStatus = prisStatus;
        this.prisFoundDate = prisFoundDate;
        this.dbComments = dbComments;
        setProductionAreaId( productionAreaId );
        
        }

    public TP (String jobName, int jobRelease, String jobRevision, int tpmsVersion) {
        this.id = generateId(jobName, jobRelease, jobRevision, tpmsVersion);
        this.jobName = jobName;
        this.jobRelease = jobRelease;
        this.jobRevision = jobRevision;
        this.tpmsVersion = tpmsVersion;
    }
    public TP ( String deliveryComment, String hwModification, float expAvgYV, float zeroYW, int newTestTime, String isTemp, Date validTill, String disEmail){
    	this.deliveryComment = deliveryComment;
        this.hwModification = hwModification;
        this.expAvgYV = expAvgYV;
        this.zeroYW = zeroYW;
        this.newTestTime = newTestTime;
        this.isTemp = isTemp;
        this.validTill = validTill;
        this.disEmail = disEmail;
    }
    
    
   public TP (String disEmail){
	   this.disEmail = disEmail;
   }
    private String generateId(String jobName, int jobRelease, String jobRevision, int tpmsVersion){
        return jobName + "_" + jobRelease + "_" + jobRevision + "_" + tpmsVersion + "_" + System.currentTimeMillis();
    }

    /*****************VISUALIZATION METHODS***************************/
    private String formatDate(Date d) {
        if (d == null)
            return "";
        else
            return dateFormat.format(d);
    }

    public String getFormattedLastActionDate() {
        return formatDate(lastActionDate);
    }

    public String getFormattedDistributionDate() {
        return formatDate(distributionDate);
    }

    public String getFormattedProductionDate() {
        return formatDate(productionDate);
    }

    public String getFormattedPrisFoundDate() {
        return formatDate(prisFoundDate);
    }
    public String getFormattedvalidTill() {
    	return formatD(validTill);
    }
    private String formatD(Date d) {
        if (d == null)
            return "";
        else{
        	SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
        	    return format.format(d);}
    }
    private static String formatComments (String comments, String fromFormat, String destinationFormat) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(comments)){
            result = comments.replaceAll(fromFormat, destinationFormat);
        }
        return result;
    }

    public static String getDbFormatCommentsFromFieldFormat(String fieldComments){
        String formattedComments = formatComments(fieldComments, WINDOWS_FIELD_NEWLINE, DB_NEWLINE);
        formattedComments = formatComments(formattedComments, UNIX_FIELD_NEWLINE, DB_NEWLINE);
        formattedComments = formatComments(formattedComments, MAC_FIELD_NEWLINE, DB_NEWLINE);
        return formattedComments;
    }

    public String getNoNewlineDisplayComments(){
        return formatComments(this.dbComments, DB_NEWLINE, NO_NEWLINE);
    }

    public static String getHtmlDisplayComments(String dbComments ){
        return formatComments(dbComments, DB_NEWLINE, HTML_NEWLINE);
    }
    
    public static String getHtmlTextareaComments(String dbComments ){
        return formatComments(dbComments, DB_NEWLINE, WINDOWS_FIELD_NEWLINE);
    }
    
    public String getHtmlDisplayComments(){
        return getHtmlDisplayComments(this.dbComments);
    }

    public String getFieldDisplayComments(){
        return formatComments(this.dbComments, DB_NEWLINE, WINDOWS_FIELD_NEWLINE);
    }

    public void setDbCommentsFromFileds(String fieldComments) {
        if (GeneralStringUtils.isEmptyString(fieldComments)){
            dbComments = "";
        } else {
            dbComments = getDbFormatCommentsFromFieldFormat(fieldComments);
        }
    }

     public String getJobReleaseDisplayFormat() {
        String strJobRelease = (new Integer(jobRelease)).toString();
        if (strJobRelease.length() < 2){
            return "0" + strJobRelease;
        } else {
            return strJobRelease;
        }
    }

    public String getShortHtmlDisplayComments(){
        String comment = getHtmlDisplayComments();
        if (comment.length() < MAX_COMMENT_SHORT_LENGTH + 8){
            return comment;
        } else {
            return comment.substring(0, MAX_COMMENT_SHORT_LENGTH) + "...";
        }
    }

    public String getProductionAreaDisplayValue (){
        String result = "";
        if (this.productionArea != null && !GeneralStringUtils.isEmptyString( this.productionArea.getDescription())){
            result = this.productionArea.getDescription();
        } else if (!GeneralStringUtils.isEmptyString( productionAreaId )) {
            result = productionAreaId;
        }
        return result;
    }

    /*****************GETTERS***************************/
    public String getId() {
        if (GeneralStringUtils.isEmptyString(id)){
            id = generateId(this.jobName, this.jobRelease, this.jobRevision, this.tpmsVersion);
        }
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public int getJobRelease() {
        return jobRelease;
    }

    public String getJobRevision() {
        return jobRevision;
    }

    public int getTpmsVersion() {
        return tpmsVersion;
    }

    public String getLine() {
        return line;
    }

    public String getFacility() {
        return facility;
    }

    public String getFromPlant() {
        return fromPlant;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOriginLabel() {
        return originLabel;
    }

    public String getLinesetName() {
        return linesetName;
    }

    public String getToPlant() {
        return toPlant;
    }

    public String getTesterInfo() {
        return testerInfo;
    }

    public String getValidLogin() {
        return validLogin;
    }

    public String getProdLogin() {
        return prodLogin;
    }

    public String getLastActionActor() {
        return lastActionActor;
    }

    public Date getLastActionDate() {
        return lastActionDate;
    }

    public String getStatus() {
        return status;
    }

    public Date getDistributionDate() {
        return distributionDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public String getDivision() {
        return division;
    }

    public String getVobStatus() {
        return vobStatus;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getPrisStatus() {
        return prisStatus;
    }

    public Date getPrisFoundDate() {
        return prisFoundDate;
    }

    public String getDbComments() {
        return dbComments;
    }

    public ProductionArea getProductionArea(){
        return productionArea;
    }

    public String getProductionAreaId () {
        return productionAreaId;
    }
    public String getdeliveryComment() {
        return deliveryComment;
    }
    public String gethwModification() {
        return hwModification;
    }
    public float getexpAvgYV() {
        return expAvgYV;
    }
    public float getzeroYW() {
        return zeroYW;
    }
    public int getnewTestTime() {
        return newTestTime;
    }
    public String getisTemp() {
        return isTemp;
    }
    public Date getvalidTill() {
        return validTill;
    }
    public String getdisEmail() {
        return disEmail;
    }
    
    /*****************SETTERS***************************/
    public void setDbComments(String dbComments) {
        this.dbComments = dbComments;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public void setFromPlant(String fromPlant) {
        this.fromPlant = fromPlant;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setOriginLabel(String originLabel) {
        this.originLabel = originLabel;
    }

    public void setLinesetName(String linesetName) {
        this.linesetName = linesetName;
    }

    public void setToPlant(String toPlant) {
        this.toPlant = toPlant;
    }

    public void setTesterInfo(String testerInfo) {
        this.testerInfo = testerInfo;
    }

    public void setValidLogin(String validLogin) {
        this.validLogin = validLogin;
    }

    public void setProdLogin(String prodLogin) {
        this.prodLogin = prodLogin;
    }

    public void setLastActionActor(String lastActionActor) {
        this.lastActionActor = lastActionActor;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDistributionDate(Date distributionDate) {
        this.distributionDate = distributionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setVobStatus(String vobStatus) {
        this.vobStatus = vobStatus;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setPrisStatus(String prisStatus) {
        this.prisStatus = prisStatus;
    }

    public void setPrisFoundDate(Date prisFoundDate) {
        this.prisFoundDate = prisFoundDate;
    }

    public void setTpmsVersion ( int tpmsVersion ) {
        this.tpmsVersion = tpmsVersion;
    }

    public void setProductionAreaId (String productionAreaId){
        this.productionAreaId = productionAreaId;
        this.productionArea = ProductionAreaData.getInstance().getProductionArea(toPlant, productionAreaId);
    }

    public void setProductionArea(ProductionArea productionArea){
        this.productionAreaId = productionArea.getId();
        this.productionArea = productionArea;
    }
    public void setdeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }  
    public void sethwModification(String hwModification) {
        this.hwModification = hwModification;
    } 
    public void setexpAvgYV(int expAvgYV) {
        this.expAvgYV = expAvgYV;
    } 
    public void setzeroYW(int zeroYW) {
        this.zeroYW = zeroYW;
    }  
    public void setnewTestTime(int newTestTime) {
        this.newTestTime = newTestTime;
    } 
    public void setisTemp(String isTemp) {
        this.isTemp = isTemp;
    }
    public void setvalidTill(Date validTill) {
        this.validTill = validTill;
    } 
    public void setdisEmail(String disEmail) {
        this.disEmail = disEmail;
    }  
 
}