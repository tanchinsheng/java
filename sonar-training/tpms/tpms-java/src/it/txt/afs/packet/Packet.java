package it.txt.afs.packet;

import it.txt.afs.packet.utils.PacketUtils;
import it.txt.afs.AfsCommonClass;
import it.txt.general.utils.GeneralStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-gen-2006
 * Time: 13.35.37
 * This class represent one package.
 */

public class Packet extends AfsCommonClass {



    protected final SimpleDateFormat dateFormat = new SimpleDateFormat(tpmsConfiguration.getAfsDatesFormat());


    protected String id = "";
    protected String name = "";
    protected String tpRelease = "";
    protected String tpRevision = "";
    protected String destinationPlant = "";
    protected String fromPlant = "";
    protected String senderLogin = "";
    protected String senderEmail = "";
    protected String firstRecieveLogin = "";
    protected String firstRecieveEmail = "";
    protected String secondRecieveLogin = "";
    protected String secondRecieveEmail = "";
    protected String ccEmail = "";
    protected String status = "";
    protected Date sentDate = null;
    protected Date extractionDate = null;
    protected String extractionLogin = "";
    protected Date lastActionDate = null;
    protected String xferPath = "";
    //not still used attributes....
    protected String vobStatus = "";
    protected String recieverComments = "";
    protected String senderComments = "";
    //not saved to db
    protected String extractionPath = "";

    public Packet() {

    }

    /**
     * @param name
     * @param destinationPlant
     * @param fromPlant
     * @param senderLogin
     * @param senderEmail
     */
    public Packet(String name, String destinationPlant, String fromPlant, String senderLogin, String senderEmail) {
        this.name = name;
        this.destinationPlant = destinationPlant;
        this.fromPlant = fromPlant;
        this.senderLogin = senderLogin;
        this.senderEmail = senderEmail;
        this.id = PacketUtils.generatePacketId(name, fromPlant, destinationPlant, senderLogin);
    }

//**********Packet methods for visualizationr*****************//

    private String formatDate(Date d) {
        if (d == null)
            return "";
        else
            return dateFormat.format(d);
    }


    public String getFormattedSentDate() {
        return formatDate(sentDate);
    }

    public String getFormattedExtractionDate() {
        return formatDate(extractionDate);
    }

    public String getFormattedLastActionDate() {
        return formatDate(lastActionDate);
    }

    public String getFormattedStatus() {
        if (GeneralStringUtils.isEmptyString(status)) {
            return "";
        } else {
            return GeneralStringUtils.capitalize(status);
        }
    }

//**********Packet attributes getter and setter*****************//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTpRelease() {
        return tpRelease;
    }

    public void setTpRelease(String tpRelease) {
        this.tpRelease = tpRelease;
    }

    public String getTpRevision() {
        return tpRevision;
    }

    public void setTpRevision(String tpRevision) {
        this.tpRevision = tpRevision;
    }

    public String getDestinationPlant() {
        return destinationPlant;
    }

    public void setDestinationPlant(String destinationPlant) {
        this.destinationPlant = destinationPlant;
    }

    public String getFromPlant() {
        return fromPlant;
    }

    public void setFromPlant(String fromPlant) {
        this.fromPlant = fromPlant;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getFirstRecieveLogin() {
        return firstRecieveLogin;
    }

    public void setFirstRecieveLogin(String firstRecieveLogin) {
        this.firstRecieveLogin = firstRecieveLogin;
    }

    public String getFirstRecieveEmail() {
        return firstRecieveEmail;
    }

    public void setFirstRecieveEmail(String firstRecieveEmail) {
        this.firstRecieveEmail = firstRecieveEmail;
    }

    public String getSecondRecieveLogin() {
        return secondRecieveLogin;
    }

    public void setSecondRecieveLogin(String secondRecieveLogin) {
        this.secondRecieveLogin = secondRecieveLogin;
    }

    public String getSecondRecieveEmail() {
        return secondRecieveEmail;
    }

    public void setSecondRecieveEmail(String secondRecieveEmail) {
        this.secondRecieveEmail = secondRecieveEmail;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVobStatus() {
        return vobStatus;
    }

    public void setVobStatus(String vobStatus) {
        this.vobStatus = vobStatus;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getExtractionDate() {
        return extractionDate;
    }

    public void setExtractionDate(Date extractionDate) {
        this.extractionDate = extractionDate;
    }

    public String getExtractionLogin() {
        return extractionLogin;
    }

    public void setExtractionLogin(String extractionLogin) {
        this.extractionLogin = extractionLogin;
    }

    public Date getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public String getRecieverComments() {
        return recieverComments;
    }

    public void setRecieverComments(String recieverComments) {
        this.recieverComments = recieverComments;
    }

    public String getSenderComments() {
        return senderComments;
    }

    public void setSenderComments(String senderComments) {
        this.senderComments = senderComments;
    }

    public String getXferPath() {
        return xferPath;
    }

    public void setXferPath(String xferPath) {
        this.xferPath = xferPath;
    }

    public String getExtractionPath() {
        return extractionPath;
    }

    public void setExtractionPath(String extractionPath) {
        this.extractionPath = extractionPath;
    }
}