package it.txt.general.installations.list;


import it.txt.general.installations.TpmsInstallationData;
import it.txt.general.utils.CoolString;
import it.txt.general.utils.GeneralStringUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-ott-2006
 * Time: 13.02.06
 */
public class TpmsInstallationsList {


    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap tpmsInstallationsList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the installationData list
    private Iterator installationListIterator = null;

    private class AlphabeticalSortByInstallationId implements Comparator {

        /**
         * This inner class is used due to alphabetically order the directories and files to be shown
         * There is a little strange situation that happens when this code runs under unix:
         * Let's suppose that we have the following directories (or files): AUTO, AUTo, AUto, Auto, auto, BASTONE, Bastone, bastone
         * Probably the user expects that those directories will be presente in the order above... but under a unix environment the order
         * will be the following one:
         * AUTO, AUTo, AUto, Auto, BASTONE, Bastone, auto, bastone
         * (the same result that a user get if he execute the ls command from the machine...)
         * This problem does not happen because you it is not case sensitive so can't have more that one file/dir with the same name.
         */
        public int compare(Object obj1, Object obj2) {
            int result = -2;
            char [] s1Lower;
            char [] s2Lower;

            if (obj1 != null && obj2 != null) {

                String s1 = ((TpmsInstallationData) obj1).getTpmsInstallationId().toLowerCase();
                String s2 = ((TpmsInstallationData) obj2).getTpmsInstallationId().toLowerCase();


                s1Lower = (new CoolString(s1)).toCharArray();
                s2Lower = (new CoolString(s2)).toCharArray();

                int s1Length = s1Lower.length;
                int s2Length = s2Lower.length;
                int cicleLimit = s1Length;

                if (s1Length > s2Length) {
                    cicleLimit = s2Length;
                    result = -1;
                } else if (s2Length > s1Length) {
                    cicleLimit = s1Length;
                    result = -1;
                }

                int i = 0;
                char s1CurrentChar;
                char s2CurrentChar;

                while (i < cicleLimit) {
                    s1CurrentChar = s1Lower[i];
                    s2CurrentChar = s2Lower[i];
                    if (s1CurrentChar < s2CurrentChar) {
                        return -1;
                    } else if (s1CurrentChar > s2CurrentChar) {
                        return 1;
                    }
                    i++;
                }
                if (s2Length == s1Length)
                    result = 0;

            } else {
                if (obj1 == null)
                    result = -1;
                else
                    result = 1;
            }
            return result;
        }

    }


    /**
     * default contructor
     */
    public TpmsInstallationsList() {
        tpmsInstallationsList = new HashMap();
    }



    public ArrayList getArrayListOfHashMapPlantsData() {
        ArrayList result = new ArrayList();
        Iterator myInstallationListIterator = getSortedInstallationsDataIterator();
        TpmsInstallationData onePlantData;
        while (myInstallationListIterator.hasNext()) {
            onePlantData = (TpmsInstallationData) myInstallationListIterator.next();
            result.add(onePlantData.getInstallationDataHashMap());
        }
        return result;
    }

     private ArrayList getSortedInstallationsDataList() {
        ArrayList arrLst = new ArrayList(tpmsInstallationsList.values());
        Collections.sort(arrLst, new AlphabeticalSortByInstallationId());
        return arrLst;
    }


    private Iterator getSortedInstallationsDataIterator() {
        return getSortedInstallationsDataList().iterator();
    }
    /**
     * add a installationData to the list
     *
     * @param installationData the element to be added
     * @return KO_RESULT if the given installationData is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(TpmsInstallationData installationData) {
        if (installationData == null || GeneralStringUtils.isEmptyString(installationData.getId()))
            return KO_RESULT;
        else {
            tpmsInstallationsList.put(installationData.getId(), installationData);
            installationListIterator = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a installationData from the  list
     *
     * @param installationId the key that identifies the installationData in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(String installationId) {
        if (!GeneralStringUtils.isEmptyString(installationId)) {
            tpmsInstallationsList.remove(installationId);
            installationListIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a TpmsInstallationID from the TpmsInstallationList list
     *
     * @param installationData the TpmsInstallationData in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(TpmsInstallationData installationData) {
        if (installationData != null && !GeneralStringUtils.isEmptyString(installationData.getId())) {
            return this.removeElement(installationData.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the installationData identified by installationId is present in the list, false otherwise
     *
     * @param installationId the id of the ls to look for.
     * @return true if the  identified by installationId is present in the list, false otherwise
     */
    public boolean containsTpmsInstallation(String installationId) {
        if (!GeneralStringUtils.isEmptyString(installationId) && tpmsInstallationsList != null) {
            return tpmsInstallationsList.containsKey(installationId);
        }
        return false;
    }

    /**
     * true if the given installationData is present in the list, false otherwise
     *
     * @param installationData the installationData to look for.
     * @return true if the given installationData is present in the list, false otherwise
     */
    public boolean containsTpmsInstallation(TpmsInstallationData installationData) {
        if (installationData != null && tpmsInstallationsList != null) {
            return tpmsInstallationsList.containsValue(installationData);
        }
        return false;
    }

    /**
     * return the installationData identified by the given element installationId
     *
     * @param installationId
     * @return the installationData identified by the given element installationId
     */
    public TpmsInstallationData getElement(String installationId) {
        if (tpmsInstallationsList != null && !GeneralStringUtils.isEmptyString(installationId)) {
            return (TpmsInstallationData) tpmsInstallationsList.get(installationId);
        }
        return null;
    }


    public TpmsInstallationData getElementByTpmsInstallationId(String tpmsInstallationId) {
        if (tpmsInstallationsList != null && !GeneralStringUtils.isEmptyString(tpmsInstallationId)) {
            Iterator myInstallationDataIterator = getSortedInstallationsDataIterator();
            TpmsInstallationData tpmsInstallationData;
            while (myInstallationDataIterator.hasNext()) {
                tpmsInstallationData = (TpmsInstallationData) myInstallationDataIterator.next();
                if (tpmsInstallationId.equals(tpmsInstallationData.getTpmsInstallationId())) {
                    return tpmsInstallationData;
                }
            }

            return (TpmsInstallationData) tpmsInstallationsList.get(tpmsInstallationId);
        }
        return null;
    }

    /**
     * Tests if this tpmsInstallationsList contains more elements
     *
     * @return true if and only if this tpmsInstallationsList contains at least one more installationData to provide; false otherwise.
     */
    public boolean hasNext() {
        if (installationListIterator == null) {
            installationListIterator = getSortedInstallationsDataIterator();
        }
        return installationListIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next installationData of this  list.
     */
    public TpmsInstallationData next() {
        if (installationListIterator == null) {
            installationListIterator = getSortedInstallationsDataIterator();
        }
        return (TpmsInstallationData) installationListIterator.next();
    }

    public void rewind() {
        installationListIterator = null;
    }


    /**
     * @return Returns the number of TpmsInstallationData in this tpmsInstallationsList.
     */
    public int size() {
        return tpmsInstallationsList.size();
    }

    /**
     * @return Tests if this tpmsInstallationsList contains no installationData. false otherwise
     */
    public boolean isEmpty() {
        return tpmsInstallationsList.isEmpty();
    }


}
