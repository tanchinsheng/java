package tpms;

import org.w3c.dom.Element;
import it.txt.general.utils.XmlUtils;

public class TpFlowMgr {
    public static void checkAvailableActions(String user, Element tpRec) throws TpmsException {
        String status = XmlUtils.getVal(tpRec, "STATUS");
        String validLogin = XmlUtils.getVal(tpRec, "VALID_LOGIN");
        String prodLogin = XmlUtils.getVal(tpRec, "PROD_LOGIN");
        String ownerLogin = XmlUtils.getVal(tpRec, "OWNER_LOGIN");
        String fromPlant = XmlUtils.getVal(tpRec, "FROM_PLANT");
        String toPlant = XmlUtils.getVal(tpRec, "TO_PLANT");

        tpRec.removeAttribute("isExtrToValidateEnabled");
        tpRec.removeAttribute("isMarkAsValidatedEnabled");
        tpRec.removeAttribute("isPutInProductionEnabled");
        tpRec.removeAttribute("isRejectEnabled");

        if (status.equals("Distributed")) {
            if ((user.equals(validLogin)) || (isNull(validLogin))) {
                tpRec.setAttribute("isExtrToValidateEnabled", "Y");
            }
            if ((user.equals(ownerLogin)) && (fromPlant.equals(toPlant))) {
                tpRec.setAttribute("isPutInProductionEnabled", "Y");
            }
            tpRec.setAttribute("isRejectEnabled", "Y");
        }
        if (status.equals("In_Validation")) {
            if (user.equals(validLogin)) {
                tpRec.setAttribute("isMarkAsValidatedEnabled", "Y");
            }
            tpRec.setAttribute("isRejectEnabled", "Y");
        }
        if (status.equals("Ready_to_production")) {
            if ((user.equals(prodLogin)) || (isNull(prodLogin))) {
                tpRec.setAttribute("isPutInProductionEnabled", "Y");
            }
        }

        if (status.equals("Ready_to_production")) {
            if ((user.equals(validLogin)) || (isNull(validLogin))) {
                tpRec.setAttribute("isExtrToValidateEnabled", "Y");
            }
        }

        if (status.equals("In_Production")) {
            if ((user.equals(validLogin)) || (isNull(validLogin))) {
                tpRec.setAttribute("isExtrToValidateEnabled", "Y");
            }
        }
        //add controll for download TP in Status 'Ghost' and status 'Obsolete'
        if (status.equals("Ghost")) {
            tpRec.setAttribute("isDownloadTpEnabled", "Y");
        }
        if (status.equals("Rejected")) {
            tpRec.setAttribute("isDownloadTpEnabled", "Y");
        }
        if (status.equals("Obsolete")) {
            tpRec.setAttribute("isDownloadTpEnabled", "Y");
        }
        /**
         * T-VOB TYPE CHECK
         * if vob type ='D' disable all the action to TP
         *
         */
        //System.out.println("vobAttr?="+tpRec.getAttribute("VOB"));
        //System.out.println("isvob?="+XmlUtils.getChild(tpRec, "VOB"));
        //System.out.println("vobVal?="+XmlUtils.getVal(tpRec, "VOB"));
        String vobName = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));

        if (!vobName.equals("")) {


            Element currentVob = VobManager.getVobData(vobName);
            //String vobType = XmlUtils.getVal(currentVob, "TYPE");

            //System.out.println("vobname="+vobName);
            //System.out.println("vobType="+vobType);
            if ((XmlUtils.getVal(currentVob, "TYPE")).equals("T")) {
                tpRec.setAttribute("isExtrToValidateEnabled", "N");
                tpRec.setAttribute("isMarkAsValidatedEnabled", "N");
                tpRec.setAttribute("isPutInProductionEnabled", "N");
            }
        }

        if (status.equals("Submitted")) {
            if (user.equals(ownerLogin)) {
                //tpRec.setAttribute("isPutInProductionEnabled","Y");
            }
        }
    }

    public static void checkPermForAction(String user, Element tpRec, String action) throws Exception {
        checkAvailableActions(user, tpRec);
        boolean enabled = true;
        if ((action.equals("extr_to_valid")) && (!tpRec.getAttribute("isExtrToValidateEnabled").equals("Y"))) enabled = false;
        if ((action.equals("tp_update")) && (!tpRec.getAttribute("isMarkAsValidatedEnabled").equals("Y"))) enabled = false;
        if ((action.equals("tp_toprod")) && (!tpRec.getAttribute("isPutInProductionEnabled").equals("Y"))) enabled = false;
        if (!enabled) throw new TpmsException("YOU ARE NOT SUFFICIENT PRIVILEGES ON THIS TP", "UNPERMITTED ACTION");
    }

    static boolean isNull(String val) {
        return (val == null || val.trim().toLowerCase().equals("null") || val.trim().equals("."));
    }
}