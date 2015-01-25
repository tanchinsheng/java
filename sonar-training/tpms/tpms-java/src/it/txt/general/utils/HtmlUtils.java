package it.txt.general.utils;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: fabio.furgiuele
 * Date: 17-feb-2005
 * Time: 19.09.09
 */
public class HtmlUtils {

    public static final String CHECK_BOX_SELECTED = "checked";
    public static final String COMBO_OPTION_SELECTED = "selected";

    public static final String DATE_FORMAT_USER_INPUT = "dd/MMM/yyy";
    public static final SimpleDateFormat clientSideDateFormatSearchFields = new SimpleDateFormat(HtmlUtils.DATE_FORMAT_USER_INPUT, Locale.UK);


    /**
     * Costruisce una lista di option partendo da un vettore:
     * Il valore nel vettore viene presentato sia all'utente che messo nel value della option
     *
     * @param v              vettore di array
     * @param selectedOption valore selezionato nella lista di opzioni
     * @return una stringa di option html
     */
    public static String buildComboOptionsListFromVector(Vector v, String selectedOption) {
        String result = "";
        if (v != null) {
            Iterator i = v.iterator();
            String tmpArr;
            while (i.hasNext()) {
                tmpArr = (String) i.next();
                result = result + "\n" + "<option value=\"" + tmpArr.trim() + "\"";
                if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(tmpArr.trim()))
                    result = result + " " + COMBO_OPTION_SELECTED + " ";

                result = result + ">" + tmpArr.trim() + "</option>";
            }
        }

        return result;
    }

    /**
     * Costruisce una lista di option partendo da un vettore di array di due posizioni:
     * in posizione 0 il valore in posizione 1 la descrizione: nella descrizione presenta la concatenazione di valore e
     * descrizione
     *
     * @param v              vettore di array
     * @param selectedOption valore selezionato nella lista di opzioni
     * @return una stringa di option html
     */
    public static String buildComboOptionsList(Vector v, String selectedOption) {
        String result = "";
        if (v != null) {
            Iterator i = v.iterator();
            String[] tmpArr;
            while (i.hasNext()) {
                tmpArr = (String[]) i.next();
                result = result + "\n" + "<option value=\"" + tmpArr[0].trim() + "\"";
                if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(tmpArr[0].trim()))
                    result = result + " " + COMBO_OPTION_SELECTED + " ";

                result = result + ">" + tmpArr[1].trim() + "</option>";
            }
        }

        return result;
    }


    /**
     * Questo metodo costruisce una stringa che rappresenta la lista di options di una select (combo) html
     *
     * @param v               è un vettore di array contenente l'insieme di informazioni per costruire la stringa
     * @param selectedOption  valore che deve essere selezionato all'interno della combo
     * @param columnNameValue chiave che nella hashmap rappresenta la l'informazione da mettere nel value della option
     * @param columNameShown  chiave che nella hashmap rappresenta la l'informazione da visualizzare
     * @param addEmptyEntry   se true aggiunge in testa alle options una entry vuota.
     * @param emptyEntryValue è il value da assegare alla entry vuota
     * @param emptyEntryText  è il testo presentato dalla entry vuota.
     * @return una stringa che rappresenta la lista di options di una select (combo) html
     */
    public static String buildComboOptionsListFromVectorOfHashtable(Vector v, String selectedOption, String columnNameValue, String columNameShown, boolean addEmptyEntry, String emptyEntryValue, String emptyEntryText) {
        String result = "";
        if (v != null) {
            Iterator i = v.iterator();
            Hashtable oneOptionEntry;
            String currentValue;
            String currentTextShown;
            if (addEmptyEntry) {
                result = "<option value=\"" + emptyEntryValue + "\">" + emptyEntryText + "</option>";
            }

            if (!v.isEmpty()) {
                while (i.hasNext()) {
                    oneOptionEntry = (Hashtable) i.next();
                    currentValue = ((String) oneOptionEntry.get(columnNameValue)).trim();
                    currentTextShown = ((String) oneOptionEntry.get(columNameShown)).trim();
                    result = result + "\n" + "<option value=\"" + currentValue + "\"";
                    if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(currentValue))
                        result = result + " " + COMBO_OPTION_SELECTED + " ";
                    result = result + ">" + currentTextShown + "</option>";
                }
            }
        }
        return result;
    }


    public static String buildComboOptionsListFromArrayListOfStrings(ArrayList a, String selectedOption, boolean addEmptyEntry, String emptyEntryValue, String emptyEntryText) {
        String result = "";
        if (a != null) {
            Iterator i = a.iterator();
            String oneOptionEntry;
            String currentValue;
            if (addEmptyEntry) {
                result = "<option value=\"" + emptyEntryValue + "\">" + emptyEntryText + "</option>";
            }

            if (!a.isEmpty()) {
                while (i.hasNext()) {
                    oneOptionEntry = (String) i.next();
                    currentValue = (oneOptionEntry).trim();
                    result = result + "\n" + "<option value=\"" + currentValue + "\"";
                    if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(currentValue))
                        result = result + " " + COMBO_OPTION_SELECTED + " ";
                    result = result + ">" + currentValue + "</option>";
                }
            }
        }
        return result;
    }



    /**
     * Questo metodo costruisce una stringa che rappresenta la lista di options di una select (combo) html
     *
     * @param a               è un vettore di array contenente l'insieme di informazioni per costruire la stringa
     * @param selectedOption  valore che deve essere selezionato all'interno della combo
     * @param columnNameValue chiave che nella hashmap rappresenta la l'informazione da mettere nel value della option
     * @param columNameShown  chiave che nella hashmap rappresenta la l'informazione da visualizzare
     * @param addEmptyEntry   se true aggiunge in testa alle options una entry vuota.
     * @param emptyEntryValue è il value da assegare alla entry vuota
     * @param emptyEntryText  è il testo presentato dalla entry vuota.
     * @return una stringa che rappresenta la lista di options di una select (combo) html
     */
    public static String buildComboOptionsListFromArrayListOfHashMaps(ArrayList a, String selectedOption, String columnNameValue, String columNameShown, boolean addEmptyEntry, String emptyEntryValue, String emptyEntryText) {
        String result = "";
        if (a != null) {
            Iterator i = a.iterator();
            HashMap oneOptionEntry;
            String currentValue;
            String currentTextShown;
            if (addEmptyEntry) {
                result = "<option value=\"" + emptyEntryValue + "\">" + emptyEntryText + "</option>";
            }

            if (!a.isEmpty()) {
                while (i.hasNext()) {
                    oneOptionEntry = (HashMap) i.next();
                    currentValue = ((String) oneOptionEntry.get(columnNameValue)).trim();
                    currentTextShown = ((String) oneOptionEntry.get(columNameShown)).trim();
                    result = result + "\n" + "<option value=\"" + currentValue + "\"";
                    if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(currentValue))
                        result = result + " " + COMBO_OPTION_SELECTED + " ";
                    result = result + ">" + currentTextShown + "</option>";
                }
            }
        }
        return result;
    }


    /**
     * Questo metodo costruisce una stringa che rappresenta la lista di options di una select (combo) html
     *
     * @param v                         è un vettore di array contenente l'insieme di informazioni per costruire la stringa
     * @param selectedOption            valore che deve essere selezionato all'interno della combo
     * @param columnValue               è l'intero che rappresenta la posizione all'interno dell'array che verrà messo nella proprietà value della option
     * @param columnToConsider          rappresenta la lista di posizioni dell'array che verranno presentate nel testo della option
     * @param columnToConsiderMaxLength per ogni elemento di columnToConsider riporta la lunghezza massima da concatenare:
     *                                  se una posizione è <0 verrà presa l'intera stringa
     * @param columnToConsiderSuspensionString
     *                                  per ogni elemento di columnToConsider se viene troncato questo array contiene la stringa di sospensione
     * @param maxTextLength             lunghezza massima del testo che verrà presentato nella option
     * @param separator                 separatore utilizzato per concatenare le diverse colonne di columnToConsider
     * @param suspensionString          rappresenta la stringa di sospensione che verrà presentate se l'intero testo della option supera maxTextLength
     * @return unastringa che rappresenta la lista di options di una select (combo) html
     */
    public static String buildComboOptionsList(Vector v, String selectedOption, int columnValue, int[] columnToConsider, int[] columnToConsiderMaxLength,
                                               String[] columnToConsiderSuspensionString, int maxTextLength, String separator, String suspensionString) {
        String result = "";
        if (v != null) {
            Iterator i = v.iterator();
            String[] tmpArr;
            String optionText;
            String currentValue;
            while (i.hasNext()) {
                optionText = "";
                tmpArr = (String[]) i.next();
                result = result + "\n" + "<option value=\"" + tmpArr[columnValue].trim() + "\"";

                if (!GeneralStringUtils.isEmptyString(selectedOption) && selectedOption.equals(tmpArr[columnValue].trim()))
                    result = result + " " + COMBO_OPTION_SELECTED + " ";


                for (int z = 0; z < columnToConsider.length; z++) {
                    // recupero il valore corrente ...
                    currentValue = tmpArr[columnToConsider[z]].trim();
                    // se devo troncarlo e supera la lunghezza massima tronco e appendo la stringa di sospensione
                    currentValue = (columnToConsiderMaxLength[z] >= 0 && currentValue.length() > columnToConsiderMaxLength[z]) ? (currentValue.substring(0, columnToConsiderMaxLength[z]) + columnToConsiderSuspensionString[z]) : currentValue;
                    //appendo il valore corrennte alla lista dei testi da presentare nella option
                    optionText = optionText + (GeneralStringUtils.isEmptyString(optionText) ? "" : separator) + currentValue;
                }

                if (maxTextLength >= 0 && optionText.length() > maxTextLength) {
                    optionText = optionText.substring(0, maxTextLength) + suspensionString;
                }

                result = result + ">" + optionText + "</option>";
            }
        }

        return result;
    }

    /**
     * Cotruisce una stringa che rappresenta un array javascript client-side
     *
     * @param csArrayName il nome che dovrà avere l'array client-side
     * @param vect        lista di valori che devono essere utilizzati per costruire l'arrai client-side
     * @return una stringa che rappresenta un array javascript client-side.
     */
    public static String buildCsArray(String csArrayName, Vector vect) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(csArrayName) && vect != null && !vect.isEmpty()) {
            //var materialArray = new Array();
            result = "\nvar " + csArrayName + " = new Array();\n";
            //materialArray[0]= new Array("6003032","17","pz","ASTRALUX GOLD 250g 70x100cm");
            Iterator it = vect.iterator();
            int i = 0;
            String[] tmpArr;
            int tmpArrLength;
            while (it.hasNext()) {
                tmpArr = (String[]) it.next();
                result = result + csArrayName + "[" + i + "] = new Array(";
                tmpArrLength = tmpArr.length;
                for (int z = 0; z < tmpArrLength; z++) {
                    if (z == (tmpArrLength - 1))
                        result += "'" + tmpArr[z] + "'";
                    else
                        result += "'" + tmpArr[z] + "', ";
                }
                result += ");\n";
                i++;
            }

        }
        return result;
    }

    /**
     * Cotruisce una stringa che rappresenta un array javascript client-side
     *
     * @param csArrayName il nome che dovrà avere l'array client-side
     * @param arrayList   lista di valori che devono essere utilizzati per costruire l'arrai client-side
     * @return una stringa che rappresenta un array javascript client-side.
     */
    public static String buildCsArray(String csArrayName, ArrayList arrayList) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(csArrayName) && arrayList != null && !arrayList.isEmpty()) {
            //var materialArray = new Array();
            result = "\nvar " + csArrayName + " = new Array();\n";
            //materialArray[0]= new Array("6003032","17","pz","ASTRALUX GOLD 250g 70x100cm");
            Iterator it = arrayList.iterator();
            int i = 0;
            String[] tmpArr;
            int tmpArrLength;
            while (it.hasNext()) {
                tmpArr = (String[]) it.next();
                result = result + csArrayName + "[" + i + "] = new Array(";
                tmpArrLength = tmpArr.length;
                for (int z = 0; z < tmpArrLength; z++) {
                    if (z == (tmpArrLength - 1))
                        result += "'" + tmpArr[z] + "'";
                    else
                        result += "'" + tmpArr[z] + "', ";
                }
                result += ");\n";
                i++;
            }

        }
        return result;
    }

    /**
     *
     * Cotruisce una stringa che rappresenta un array javascript client-side
     *
     * @param csArrayName il nome che dovrà avere l'array client-side
     * @param arrayList   lista di valori che devono essere utilizzati per costruire l'arrai client-side
     * @return una stringa che rappresenta un array javascript client-side.
     */
    public static String buildCsArrayFromArrayListOfStringArray(String csArrayName, ArrayList arrayList) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(csArrayName) && arrayList != null && !arrayList.isEmpty()) {
            result = "\nvar " + csArrayName + " = new Array();\n";
            //materialArray[0]= new Array("6003032","17","pz","ASTRALUX GOLD 250g 70x100cm");
            Iterator it = arrayList.iterator();
            int i = 0;
            String[] tmpArr;
            int tmpArrLength;
            while (it.hasNext()) {
                tmpArr = (String[]) it.next();
                result = result + csArrayName + "[" + i + "] = new Array(";
                tmpArrLength = tmpArr.length;
                for (int z = 0; z < tmpArrLength; z++) {
                    if (z == (tmpArrLength - 1))
                        result += "'" + tmpArr[z] + "'";
                    else
                        result += "'" + tmpArr[z] + "', ";
                }
                result += ");\n";
                i++;
            }
        }
        return result;
    }

    /**
     * Cotruisce una stringa che rappresenta un array javascript client-side
     *
     * @param csArrayName il nome che dovrà avere l'array client-side
     * @param arrayList   lista di valori che devono essere utilizzati per costruire l'arrai client-side
     * @return una stringa che rappresenta un array javascript client-side.
     */
    public static String buildOneDimensionCsArray(String csArrayName, ArrayList arrayList, String columnName, boolean addEmptyEntry, String emptyValueAndText) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(csArrayName) && arrayList != null && !arrayList.isEmpty()) {
            //var materialArray = new Array();
            result = "var " + csArrayName + " = new Array(";
            boolean firstTime = true;
            if (addEmptyEntry){
                firstTime = false;
                result += "'" + GeneralStringUtils.escapedEncode(emptyValueAndText) + "'";
            }

            Iterator it = arrayList.iterator();
            HashMap tmpArr;

            while (it.hasNext()) {
                tmpArr = (HashMap) it.next();
                if (firstTime) {
                    result += "'" + GeneralStringUtils.escapedEncode((String) tmpArr.get(columnName)) + "'";
                    firstTime = false;
                } else {
                    result += ", '" + GeneralStringUtils.escapedEncode((String) tmpArr.get(columnName)) + "'";
                }
            }
            result += ");\n";
        }
        return result;
    }

    /**
     * Dato un valore e una lista di valori se il valore singolo è contenuto nell'array ritorna la stringa per
     * selezionare una check-box, stringa vuota altrimenti
     *
     * @param checkBoxValue  valore da ricercare
     * @param selectedValues lista di valori
     * @return se il valore singolo è contenuto nell'array ritorna la stringa per selezionare una check-box,
     *         stringa vuota altrimenti
     */
    public static String getCheckBoxChecked(int checkBoxValue, int[] selectedValues) {
        return ArrayUtils.indexOf(checkBoxValue, selectedValues) > 0 ? CHECK_BOX_SELECTED : "";

    }

    /**
     * Dato un valore e una lista di valori se il valore singolo è contenuto nell'array ritorna la stringa per
     * selezionare una check-box, stringa vuota altrimenti
     *
     * @param checkBoxValue  valore da ricercare
     * @param selectedValues lista di valori
     * @return se il valore singolo è contenuto nell'array ritorna la stringa per selezionare una check-box,
     *         stringa vuota altrimenti
     */
    public static String getCheckBoxChecked(String checkBoxValue, String[] selectedValues) {
        return ArrayUtils.indexOf(checkBoxValue, selectedValues) > 0 ? CHECK_BOX_SELECTED : "";
    }

    /**
     * Dato un valore e una lista di valori se il valore singolo è contenuto nell'array ritorna la stringa per
     * selezionare una check-box, stringa vuota altrimenti
     *
     * @param checkBoxValue  valore da ricercare
     * @param selectedValues lista di valori
     * @return se il valore singolo è contenuto nell'array ritorna la stringa per selezionare una check-box,
     *         stringa vuota altrimenti
     */
    public static String getCheckBoxChecked(String checkBoxValue, Hashtable selectedValues) {
        return selectedValues.get(checkBoxValue) != null ? CHECK_BOX_SELECTED : "";
    }
}
