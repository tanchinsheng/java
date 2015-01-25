package it.txt.tpms.reports.request.utils;

import it.txt.tpms.reports.TpReportTypes;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.HtmlUtils;
import it.txt.general.utils.CoolString;
import it.txt.afs.AfsCommonStaticClass;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.ParseException;

import tpms.utils.QueryUtils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-mar-2006
 * Time: 11.11.11
 * To change this template use File | Settings | File Templates.
 */
public class ReportRequestUtils extends AfsCommonStaticClass {


    protected static final String FIELD_NAME = "FIELD_NAME";
    protected static final String FIELD_VALUE = "FIELD_VALUE";
    protected static final String DATE_FIELD_IDENTIFIER = "DATE";


    /**
     * this method is in charge to produce from the request object the criteria that will be inserted in TPMS_REPORTS_REQUESTS.CRITERIA
     *
     * @param request
     * @return the criteria that will be used for the report production
     */
    public static String getReportRequestCriteria(HttpServletRequest request) {
        StringBuffer result = new StringBuffer();
        String reportType = request.getParameter(TpReportTypes.REPORT_TYPE_FIELD_NAME);
        ArrayList searchFieldsValues = null;
        if (reportType.equals(TpReportTypes.TP_LAST_STATUS)) {
            searchFieldsValues = retrieveTpLastStatusSearchFields(request);
        }

        if ((searchFieldsValues != null) && (searchFieldsValues.size() > 0)) {
            int searchFieldsFilledCount = searchFieldsValues.size();
            String currentFieldName;
            String currentFieldValue;
            String clientSideFieldName;
            HashMap tmpFiledNameValue;

            for (int i = 0; i < searchFieldsFilledCount; i++) {
                tmpFiledNameValue = (HashMap) searchFieldsValues.get(i);
                currentFieldName = (String) tmpFiledNameValue.get(FIELD_NAME);

                int dateFieldLabelPos = currentFieldName.indexOf(DATE_FIELD_IDENTIFIER);
                if (dateFieldLabelPos >= 0) {
                    //is a date field
                    clientSideFieldName = currentFieldName;
                    currentFieldName = currentFieldName.substring(0, dateFieldLabelPos + DATE_FIELD_IDENTIFIER.length());
                    Date date = (Date) tmpFiledNameValue.get(FIELD_VALUE);
                    debugLog("ReportRequestUtils :: getReportRequestCriteria : currentFieldName = " + currentFieldName + " currentFieldValue = " + date);
                    if (clientSideFieldName.indexOf("FROM") >= 0) {
                        if (!GeneralStringUtils.isEmptyString(result.toString())) {
                            result.append(" AND ").append(QueryUtils.getDateForQueryGreaterEqualThan(currentFieldName, date));
                        } else {
                            result.append(QueryUtils.getDateForQueryGreaterEqualThan(currentFieldName, date));
                        }
                    } else if (clientSideFieldName.indexOf("TO") >= 0) {
                        if (!GeneralStringUtils.isEmptyString(result.toString())) {
                            result.append(" AND ").append(QueryUtils.getDateForQueryLessEqualThan(currentFieldName, date));
                        } else {
                            result.append(QueryUtils.getDateForQueryLessEqualThan(currentFieldName, date));
                        }
                    }
                } else {

                    //it's a normal field
                    currentFieldValue = (String) tmpFiledNameValue.get(FIELD_VALUE);
                    debugLog("ReportRequestUtils :: getReportRequestCriteria : currentFieldName = " + currentFieldName + " currentFieldValue = " + currentFieldValue);
                    if (!GeneralStringUtils.isEmptyString(result.toString())) {
                        result.append(" AND ").append(currentFieldName).append(" = ").append(QueryUtils.getStringValueForQuery(currentFieldValue));
                    } else {
                        result.append(currentFieldName).append(" = ").append(QueryUtils.getStringValueForQuery(currentFieldValue));
                    }
                }
            }

        }

        return QueryUtils.duplicateQuotes(result.toString());
    }

    /**
     * This method starting from a list of ParameterNames (contained in a vector) return an ArrayList where each entry is an HashMap of two fields:
     * <li> FIELD_NAME which is the name of the parameter
     * <li> FIELD_VALUE which is the value of the parameter in the request object.
     * if a parameter has a null or empty value it will bnot be present in the returned vector.
     *
     * @param request
     * @return a ArrayList of hashmaps
     */
    protected static ArrayList retrieveTpLastStatusSearchFields(HttpServletRequest request) {

        String reportType = request.getParameter(TpReportTypes.REPORT_TYPE_FIELD_NAME);
        ArrayList result = new ArrayList();
        HashMap tmpFieldNameValues;
        String currentFieldValue;
        if (reportType.equals(TpReportTypes.TP_LAST_STATUS)) {

            currentFieldValue = request.getParameter(TpReportTypes.TP_LAST_STATUS_FROM_PLANT_FIELD_NAME);
            if (!GeneralStringUtils.isEmptyString(currentFieldValue)) {
                tmpFieldNameValues = new HashMap();
                tmpFieldNameValues.put(FIELD_NAME, TpReportTypes.TP_LAST_STATUS_FROM_PLANT_FIELD_NAME);
                tmpFieldNameValues.put(FIELD_VALUE, currentFieldValue);
                result.add(tmpFieldNameValues);
            }

            currentFieldValue = request.getParameter(TpReportTypes.TP_LAST_STATUS_TO_PLANT_FIELD_NAME);
            if (!GeneralStringUtils.isEmptyString(currentFieldValue)) {
                tmpFieldNameValues = new HashMap();
                tmpFieldNameValues.put(FIELD_NAME, TpReportTypes.TP_LAST_STATUS_TO_PLANT_FIELD_NAME);
                tmpFieldNameValues.put(FIELD_VALUE, currentFieldValue);
                result.add(tmpFieldNameValues);
            }

            currentFieldValue = request.getParameter(TpReportTypes.TP_LAST_STATUS_DIVISION_FIELD_NAME);
            if (!GeneralStringUtils.isEmptyString(currentFieldValue)) {
                tmpFieldNameValues = new HashMap();
                tmpFieldNameValues.put(FIELD_NAME, TpReportTypes.TP_LAST_STATUS_DIVISION_FIELD_NAME);
                tmpFieldNameValues.put(FIELD_VALUE, currentFieldValue);
                result.add(tmpFieldNameValues);
            }

            currentFieldValue = request.getParameter(TpReportTypes.TP_LAST_STATUS_LAST_ACTION_DATE_FROM_FIELD_NAME);
            if (!GeneralStringUtils.isEmptyString(currentFieldValue)) {
                tmpFieldNameValues = new HashMap();
                tmpFieldNameValues.put(FIELD_NAME, TpReportTypes.TP_LAST_STATUS_LAST_ACTION_DATE_FROM_FIELD_NAME);
                try {
                    tmpFieldNameValues.put(FIELD_VALUE, HtmlUtils.clientSideDateFormatSearchFields.parse(currentFieldValue));
                    result.add(tmpFieldNameValues);
                } catch (ParseException e) {
                    errorLog("ReportRequestUtils :: retrieveTpLastStatusSearchFields : TP_LAST_STATUS_LAST_ACTION_DATE_FROM_FIELD_NAME parse exception", e);
                }
            }

            currentFieldValue = request.getParameter(TpReportTypes.TP_LAST_STATUS_LAST_ACTION_DATE_TO_FIELD_NAME);
            if (!GeneralStringUtils.isEmptyString(currentFieldValue)) {
                tmpFieldNameValues = new HashMap();
                tmpFieldNameValues.put(FIELD_NAME, TpReportTypes.TP_LAST_STATUS_LAST_ACTION_DATE_TO_FIELD_NAME);
                try {
                    tmpFieldNameValues.put(FIELD_VALUE, HtmlUtils.clientSideDateFormatSearchFields.parse(currentFieldValue));
                    result.add(tmpFieldNameValues);
                } catch (ParseException e) {
                    errorLog("ReportRequestUtils :: retrieveTpLastStatusSearchFields : TP_LAST_STATUS_LAST_ACTION_DATE_TO_FIELD_NAME parse exception", e);
                }
            }
        }
        return result;
    }



    public static String getFormattedCriteria(String criteria, String reportType) {
        StringBuffer result = new StringBuffer();
        /*
FROM_PLANT = ''Plant1'' AND TO_PLANT = ''TXT-TR'' AND DIVISION = ''TPA'' AND LAST_ACTION_DATE>=TO_DATE(''08032006000000'', ''DDMMYYYYHH24MISS'')  AND LAST_ACTION_DATE<=TO_DATE(''22032006000000'', ''DDMMYYYYHH24MISS'')
        */
        if (!GeneralStringUtils.isEmptyString(criteria) && !GeneralStringUtils.isEmptyString(reportType)) {
            if (reportType.equals(TpReportTypes.TP_LAST_STATUS)) {
                criteria = criteria.replaceAll("''", "'");
                criteria = criteria.replaceAll("AND", "@@");
                criteria = criteria.replaceAll("OR", "@@");

                StringTokenizer st = new StringTokenizer(criteria, "@@");
                CoolString currentCondition;
                boolean isDateCondition;
                while (st.hasMoreElements()) {

                    currentCondition = new CoolString(st.nextToken());
                    isDateCondition = (currentCondition.indexOf("TO_DATE") >= 0);
                    currentCondition.setCaseSensitive(false);
                    currentCondition.replaceAll("''", "");
                    currentCondition.replaceAll("TO_DATE(", "");
                    currentCondition.replaceAll("'" + QueryUtils.ORACLE_DATE_FORMAT + "')", "");
                    currentCondition.replaceAll("_", " ");
                    currentCondition.replaceAll(",", "");
                    if ( isDateCondition ){
                        int startIndex = currentCondition.toString().indexOf("'");
                        int endIndex = currentCondition.toString().indexOf("'", startIndex + 1);
                        String tmpStringDate = currentCondition.substring(startIndex + 1, endIndex - 1);
                        Date d = QueryUtils.parseDbDate(tmpStringDate);
                        currentCondition.replaceAll(tmpStringDate, HtmlUtils.clientSideDateFormatSearchFields.format(d));
                    }

                    if (GeneralStringUtils.isEmptyString(result.toString())){
                        result.append(currentCondition.toString().trim());
                    } else {
                        result.append("<BR>").append(currentCondition.toString().trim());
                    }


                }



            }
        }

       return result.toString();
    }

    public static String getDbFormattedCriteria(String criteria) {

        if (!GeneralStringUtils.isEmptyString(criteria)) {
            criteria = criteria.replaceAll("''", "'");
        }
        return criteria;

    }



}
