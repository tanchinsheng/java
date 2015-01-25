package it.txt.tpms.tp.utils;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.HtmlUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.ProductionArea;
import it.txt.tpms.tp.list.ProductionAreasList;
import it.txt.tpms.tp.managers.ProductionAreaData;
import org.w3c.dom.Element;
import tpms.TesterInfoMgr;
import tpms.TpActionServlet;
import tpms.utils.UserUtils;
import tpms.utils.TpmsConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 3-apr-2006
 * Time: 17.27.42
 */
public class TpUtils  {
    private static TpmsConfiguration tpmsConfiguration = AfsCommonStaticClass.getTpmsConfiguration();

    /**
     * @param tpRecord tp data
     *
     * @return true if the give tp (represented by tpRecord) con be extracted false otherwise
     */
    public static boolean allowExtractAction ( Element tpRecord ) {
        if ( tpmsConfiguration.isTpmsdEnabled() ) {
            //if Tpmsd is installed chechk the cosistence of TESTER information between local plant and TP
            if ( tpRecord == null ) {
                return false;
            }
            String tpTesterInfoShow = XmlUtils.getVal( tpRecord, "TESTER_INFO" );
            String testerInfoCode = TesterInfoMgr.getTesterInfoValFromDesk( tpmsConfiguration.getLocalPlant(), tpTesterInfoShow );
            return !GeneralStringUtils.isEmptyString( testerInfoCode );
        } else {
            //if Tpmsd is NOT installed always allow extract action
            return true;
        }
    }

    /**
     * this method should be used in order to understand which is the right user that have to call the BE action.
     *
     * @param currentUserTpmsLogin the login used by the current user in order to login to tpms
     * @param vobType              the currently selected vobType
     * @param action               the strings that identifies the requested action
     *
     * @return the unix login of the user that should be used to execute the action
     */
    public static String getUserForActionExecution ( String currentUserTpmsLogin, String vobType, String action ) {
        String userForAction;
        if ( ( ( vobType.equals( "R" ) && ( action.equals( "extr_to_valid" ) || action.equals( "tp_update" ) || action.equals( "tp_toprod" ) || action.equals( TpActionServlet.TP_REJECT_ACTION_VALUE ) || action.equals( TpActionServlet.TP_UPDATE_STEPS_ACTION_VALUE ) ) ) ) ||
                ( vobType.equals( "D" ) && ( action.equals( "extr_to_valid" ) || action.equals( "tp_update" ) || action.equals( "tp_toprod" ) || action.equals( TpActionServlet.TP_REJECT_ACTION_VALUE ) || action.equals( TpActionServlet.TP_UPDATE_STEPS_ACTION_VALUE ) ) ) ) {
            userForAction = UserUtils.getAdminUnixLogin();//administrator
        } else {
            userForAction = UserUtils.getUserUnixLogin( currentUserTpmsLogin );//currentUser
        }
        AfsCommonStaticClass.debugLog( "TpUtils :: getUserForActionExecution : action = " + action + ", vobType = " + vobType + ", currentUserTpmsLogin = " + currentUserTpmsLogin + " the action will be performed as " + userForAction );
        return userForAction;
    }


    public static String tpDisplayProductionArea ( Element tpRecord ) {
        String result = "";
        if ( tpRecord != null ) {

            String prodAreaId = XmlUtils.getVal( tpRecord, "AREA_PROD" );
            if (GeneralStringUtils.isEmptyString( prodAreaId )){
                prodAreaId = XmlUtils.getVal( tpRecord, "PRODUCTION_AREA_ID" ); 
            }
            String installationId = XmlUtils.getVal( tpRecord, "TO_PLANT" );
            result = tpDisplayProductionArea( prodAreaId, installationId);
        } else {
            AfsCommonStaticClass.debugLog( "TpUtils :: tpDisplayProductionArea : tpRecord is null");
        }
        return result;
    }

    public static String tpDisplayProductionArea ( String prodAreaId, String installationId ) {
        String result = prodAreaId;
        AfsCommonStaticClass.debugLog( "TpUtils :: tpDisplayProductionArea : prodAreaId = " + prodAreaId + " installationId = " + installationId);
        if (!GeneralStringUtils.isEmptyString( prodAreaId) && !GeneralStringUtils.isEmptyString( installationId )){
            ProductionArea prodArea = ProductionAreaData.getInstance().getProductionArea( installationId, prodAreaId );
            if ( prodArea != null ) {
                result = prodArea.getDescription();
            }
        }
        return result;
    }

    public static String tpDisplayLocalProductionArea ( String prodAreaId ) {
        return tpDisplayProductionArea( prodAreaId, tpmsConfiguration.getLocalPlant() );
    }
      public static String buildProductionAreasCsArrays(String csArrayName){
        String commonErrorMessage = "TpUtils :: buildProductionAreasCsArrays";

        StringBuffer sbProdAreaResult = new StringBuffer("var " + csArrayName + " = new Array();");
        ProductionAreaData pad = ProductionAreaData.getInstance();
        ProductionAreasList productionAreas = pad.getAllProductionsAreas();
        if ( productionAreas == null ) {
            AfsCommonStaticClass.debugLog(commonErrorMessage + ": productionAreas is null");
        } else if (productionAreas.isEmpty()){
            AfsCommonStaticClass.debugLog(commonErrorMessage + ": productionAreas is empty");
        } else {
            AfsCommonStaticClass.debugLog(commonErrorMessage + ": productionAreas size = " + productionAreas.size());
        }

        if (productionAreas != null && !productionAreas.isEmpty()){
            productionAreas.rewind();
            ProductionArea productionArea;
            String currentProdAreaInstallationId;
            String previousProdAreaInstallationId = null;
            String tmpSubArrayStr;
            int i = 0;
            while( productionAreas.hasNext() ) {
                productionArea = productionAreas.next();
                tmpSubArrayStr = "new Array (\"" + productionArea.getId() + "\", \""+ GeneralStringUtils.escapedEncode( productionArea.getDescription() ) + "\");";
                currentProdAreaInstallationId = productionArea.getInstallationId();
                if ( !currentProdAreaInstallationId.equals( previousProdAreaInstallationId ) ){
                    sbProdAreaResult.append( "\n" ).append( csArrayName ).append( "[\"" ).append( productionArea.getInstallationId() ).append( "\"] = new Array ();" );
                    i = 0;
                }


                sbProdAreaResult.append( "\n" ).append( csArrayName ).append( "[\"" ).append( productionArea.getInstallationId() ).append( "\"]" ).append( "[" ).append( i ).append( "] = " ).append( tmpSubArrayStr );
                previousProdAreaInstallationId = currentProdAreaInstallationId;
                i++;

            }

        }

        return sbProdAreaResult.toString();
    }



    public static String buildPlantProductionAreaOptions (String selectedProdArea, String destinationPlantId){
        AfsCommonStaticClass.debugLog( "TpUtils :: buildPlantProductionAreaOptions : selectedProdArea = " + selectedProdArea + " - destinationPlantId = " + destinationPlantId );
        StringBuffer sbProdAreaResult = new StringBuffer("<option value=\"\">--</option>");
        ProductionAreasList productionAreas = ProductionAreaData.getInstance().getPlantInstallationProductionArea( destinationPlantId );

        if (productionAreas != null && !productionAreas.isEmpty()){
            AfsCommonStaticClass.debugLog( "TpUtils :: buildPlantProductionAreaOptions : productionAreas not empty" );
            productionAreas.rewind();
            ProductionArea currentProductionArea;

            while( productionAreas.hasNext() ) {
                currentProductionArea = productionAreas.next();
                AfsCommonStaticClass.debugLog( "TpUtils :: buildPlantProductionAreaOptions :currentProductionArea.getId() = " + currentProductionArea.getId() );
                sbProdAreaResult.append( "\n<option value=\"" ).append( currentProductionArea.getId() ).append( "\"" );
                 if (!GeneralStringUtils.isEmptyString(selectedProdArea) && selectedProdArea.equals( currentProductionArea.getId()))
                    sbProdAreaResult.append( " " + HtmlUtils.COMBO_OPTION_SELECTED + " " );

                sbProdAreaResult.append( ">" ).append( currentProductionArea.getDescription() ).append( "</option>" );

            }

        }
        return sbProdAreaResult.toString();
    }
}
