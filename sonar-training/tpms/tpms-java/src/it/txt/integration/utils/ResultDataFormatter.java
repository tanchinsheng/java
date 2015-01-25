package it.txt.integration.utils;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 29-giu-2006
 * Time: 12.55.52
 * This is a really simple but usefull class used to centralize the common operation needed in order to format a call result
 */
public class ResultDataFormatter extends AfsCommonStaticClass {
    //*********************LIST OF POSSIBLE VALUES OF ACTION RESULT*********************//
    public static final String OK_RESULT = "OK";
    public static final String KO_RESULT = "KO";
    public static final String WARNING_RESULT = "WARNING";

    //*********************RESULT GENERAL TAGS****************************//
    public static final String ACTION_RESULT_OPEN_TAG = "<action_result>";
    public static final String ACTION_RESULT_CLOSING_TAG = "</action_result>";
    public static final String RESULT_DATA_OPEN_TAG = "<result_data>";
    public static final String RESULT_DATA_CLOSING_TAG = "</result_data>";
    public static final String RESULT_OPEN_TAG = "<result>";
    public static final String RESULT_CLOSING_TAG = "</result>";
    public static final String RETURN_OPEN_TAG = "<return>";
    public static final String RETURN_CLOSING_TAG = "</return>";
    public static final String MESSAGE_OPEN_TAG = "<message>";
    public static final String MESSAGE_CLOSING_TAG = "</message>";


    /**
     * This method append to the input StringBuffer the xml header,
     * if the given StringBuffer is null istatiates a new one
     *
     * @param sb
     * @return the Stringbuffer containing xml header
     */
    public static StringBuffer addXmlHeader(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
        return sb;
    }

    /**
     * This method append to the input StringBuffer the RESULT_DATA_OPEN_TAG,
     * if the given StringBuffer is null istatiates a new one
     *
     * @param sb
     * @return the Stringbuffer containing RESULT_DATA_OPEN_TAG
     */
    public static StringBuffer addResultDataOpenTag(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        return sb.append(RESULT_DATA_OPEN_TAG);
    }

    /**
     * This method append to the input StringBuffer the RESULT_DATA_CLOSING_TAG,
     * if the given StringBuffer is null istatiates a new one
     *
     * @param sb
     * @return the Stringbuffer containing RESULT_DATA_CLOSING_TAG
     */
    public static StringBuffer addResultDataClosingTag(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        return sb.append(RESULT_DATA_CLOSING_TAG);
    }

    /**
     * This method append to the input StringBuffer the ACTION_RESULT_OPEN_TAG,
     * if the given StringBuffer is null istatiates a new one
     *
     * @param sb
     * @return the Stringbuffer containing ACTION_RESULT_OPEN_TAG
     */
    public static StringBuffer addActionResultOpenTag(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        return sb.append(ACTION_RESULT_OPEN_TAG);
    }

    /**
     * This method append to the input StringBuffer the ACTION_RESULT_CLOSING_TAG,
     * if the given StringBuffer is null istatiates a new one
     *
     * @param sb
     * @return the Stringbuffer containing ACTION_RESULT_CLOSING_TAG
     */
    public static StringBuffer addActionResultClosingTag(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        return sb.append(ACTION_RESULT_CLOSING_TAG);
    }


    public static StringBuffer generateKoResult(String errorMessage){
        errorLog(errorMessage);
        return generateResult(KO_RESULT, errorMessage, null);
    }

    public static StringBuffer generateKoResult(String errorMessage, Throwable t){
        errorLog(errorMessage, t);
        return generateResult(KO_RESULT, errorMessage, null);
    }

    public static StringBuffer generateKoResult(String errorMessage, String resultData){
        errorLog(errorMessage);
        return generateResult(KO_RESULT, errorMessage, resultData);
    }

    public static StringBuffer generateKoResult(String errorMessage, String resultData, Throwable t){
        errorLog(errorMessage + " - " + t.getMessage(), t);
        return generateResult(KO_RESULT, errorMessage, resultData);
    }

    public static StringBuffer generateOkResult(String resultData){
        return generateResult(OK_RESULT, null, resultData);

    }

    public static StringBuffer generateWarningResult(String warningMessage){
        return generateResult(WARNING_RESULT, warningMessage, null);
    }

    public static StringBuffer generateWarningResult(String warningMessage, String resultData){
        return generateResult(WARNING_RESULT, warningMessage, resultData);
    }

    
    /**
     *
     * @param result: one of the following values OK_RESULT, KO_RESULT, WARNING_RESULT
     * @param message
     * @param resultData
     * @return a string buffer that represents the xml that should be sent to the caller.
     */
    public static StringBuffer generateResult(String result, String message, String resultData) {
        StringBuffer sb = new StringBuffer();
        if (GeneralStringUtils.isEmptyString(message)) {
            message = "General error";
        }
        ResultDataFormatter.addXmlHeader(sb);
        ResultDataFormatter.addActionResultOpenTag(sb);
        if (GeneralStringUtils.isEmptyString(message)){
            ResultDataFormatter.addHeaderData(sb);
        } else {
            ResultDataFormatter.addHeaderData(sb, result, message);
        }
        ResultDataFormatter.addResultDataOpenTag(sb);
        if (!GeneralStringUtils.isEmptyString(resultData)) {
            sb.append(resultData);
        }
        ResultDataFormatter.addResultDataClosingTag(sb);
        ResultDataFormatter.addActionResultClosingTag(sb);
        return sb;
    }


    /**
     * prepare the header data related to the action result: it return an OK RESULT
     *
     * @param sb
     * @return prepare the header data related to the action result: it return an OK RESULT,
     */
    public static StringBuffer addHeaderData(StringBuffer sb) {
        return addHeaderData(sb, OK_RESULT, null);
    }

    /**
     * prepare the header data related to the action result
     *
     * @param sb
     * @param result
     * @param errorMessage
     * @return prepare the header data related to the action result: it return an OK RESULT,
     */
    public static StringBuffer addHeaderData(StringBuffer sb, String result, String errorMessage) {
        if (!GeneralStringUtils.isEmptyString(result)) {
            if (sb == null) {
                sb = new StringBuffer();
            }
            if (result.equals(KO_RESULT) || result.equals(WARNING_RESULT)) {
                //ko or warning
                sb.append(RESULT_OPEN_TAG).append(RETURN_OPEN_TAG).append(result).append(RETURN_CLOSING_TAG).append(MESSAGE_OPEN_TAG).append(errorMessage).append(MESSAGE_CLOSING_TAG).append(RESULT_CLOSING_TAG);
            } else {
                //OK
                sb.append(RESULT_OPEN_TAG + RETURN_OPEN_TAG).append(OK_RESULT).append(RETURN_CLOSING_TAG).append(MESSAGE_OPEN_TAG).append(MESSAGE_CLOSING_TAG).append(RESULT_CLOSING_TAG);
            }

        }
        return sb;
    }
}
