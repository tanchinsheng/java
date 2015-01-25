package it.txt.common.elements;

import it.txt.afs.AfsCommonStaticClass;

import java.util.Date;
import java.text.SimpleDateFormat;

import tpms.VobManager;
import tpms.utils.Vob;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 1-mar-2006
 * Time: 10.43.35
 * This class represents a tp or a package in the received tp and packets list.
 */
public class TpPacketElement extends AfsCommonStaticClass {


    protected final SimpleDateFormat dateFormat = new SimpleDateFormat(tpmsConfiguration.getAfsDatesFormat());

    private String id = "";
    private String jobName = "";
    private String jobRelease = "";
    private String jobRevision = "";
    private String fromPlant = "";
    private String ownerLogin = "";
    private Date sendDate = null;
    private String status = "";
    private boolean isTp = true;
    private Vob rVob = null;



    public TpPacketElement(String id, String jobName, String jobRelease, String jobRevision, String fromPlant, String ownerLogin,
                           Date sendDate, String status, boolean isTp){
        this.id = id;
        this.jobName = jobName;
        this.jobRelease = jobRelease;
        this.jobRevision = jobRevision;
        this.fromPlant = fromPlant;
        this.ownerLogin = ownerLogin;
        this.sendDate = sendDate;
        this.status = status;
        this.isTp = isTp;
        this.rVob = VobManager.searchRVobByDestinationPlant(fromPlant);
    }

    public TpPacketElement(String id){
        this.id = id;
    }

    private String formatDate(Date d) {
        if (d == null)
            return "";
        else
            return dateFormat.format(d);
    }

    public String getFormattedSendDate(){
        return formatDate(sendDate);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobRelease() {
        return jobRelease;
    }

    public void setJobRelease(String jobRelease) {
        this.jobRelease = jobRelease;
    }

    public String getJobRevision() {
        return jobRevision;
    }

    public void setJobRevision(String jobRevision) {
        this.jobRevision = jobRevision;
    }

    public String getFromPlant() {
        return fromPlant;
    }

    public void setFromPlant(String fromPlant) {
        this.fromPlant = fromPlant;
    }

    public Vob getRVob(){
        return rVob;

    }

    public String getFromPlantVobName(){
        if (rVob == null){
            return "";
        } else {
            return rVob.getName();
        }
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTp() {
        return isTp;
    }

    public void setTp(boolean tp) {
        isTp = tp;
    }



}
