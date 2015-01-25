package it.txt.tpms.lineset.packages.list;

import it.txt.general.utils.CoolString;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.packages.ReceivedLinesetPackage;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 19-ott-2006
 * Time: 10.19.00
 *
 */
public class ReceivedLinesetPackagesList {


    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap linesetPackagesList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the installationData list
    private Iterator packagesListIterator = null;


    private class AlphabeticalSortByLinsetName implements Comparator {

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

                String s1 = ((ReceivedLinesetPackage) obj1).getLinesetName().toLowerCase();
                String s2 = ((ReceivedLinesetPackage) obj2).getLinesetName().toLowerCase();


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
                if (s2Length == s1Length) {
                    //todo agisci qui se devi ordinare oltre che per nome anche per qualche altro attributo
                    //questo caso si verifica se le due stringhe sono perfettamente uguali
                    result = 0;
                }
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
    public ReceivedLinesetPackagesList() {
        linesetPackagesList = new HashMap();
    }


    private ArrayList getSortedLinesetPackagesArrayList() {
        ArrayList arrLst = new ArrayList(linesetPackagesList.values());
        Collections.sort(arrLst, new ReceivedLinesetPackagesList.AlphabeticalSortByLinsetName());
        return arrLst;
    }


    private Iterator getSortedLinesetPackagesListIterator() {
        return getSortedLinesetPackagesArrayList().iterator();
    }

    /**
     * add a installationData to the list
     *
     * @param linesetPackage the element to be added
     * @return KO_RESULT if the given installationData is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(ReceivedLinesetPackage linesetPackage) {
        if (linesetPackage == null || GeneralStringUtils.isEmptyString(linesetPackage.getLinesetPackageId()))
            return KO_RESULT;
        else {
            linesetPackagesList.put(linesetPackage.getLinesetPackageId(), linesetPackage);
            packagesListIterator = null;
        }
        return OK_RESULT;
    }


    /**
     * remove a installationData from the  list
     *
     * @param linesetPackageId the key that identifies the installationData in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(String linesetPackageId) {
        if (!GeneralStringUtils.isEmptyString(linesetPackageId)) {
            linesetPackagesList.remove(linesetPackageId);
            packagesListIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a TpmsInstallationID from the TpmsInstallationList list
     *
     * @param linesetPackage the TpmsInstallationData in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(ReceivedLinesetPackage linesetPackage) {
        if (linesetPackage != null && !GeneralStringUtils.isEmptyString(linesetPackage.getLinesetPackageId())) {
            return this.removeElement(linesetPackage.getLinesetPackageId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the installationData identified by installationId is present in the list, false otherwise
     *
     * @param linesetPackageId the id of the ls to look for.
     * @return true if the  identified by installationId is present in the list, false otherwise
     */
    public boolean containsLinesetPackage(String linesetPackageId) {
        return !GeneralStringUtils.isEmptyString(linesetPackageId) && linesetPackagesList != null && linesetPackagesList.containsKey(linesetPackageId);
    }

    /**
     * true if the given installationData is present in the list, false otherwise
     *
     * @param linesetPackage the installationData to look for.
     * @return true if the given installationData is present in the list, false otherwise
     */
    public boolean containsLinesetPackage(ReceivedLinesetPackage linesetPackage) {
        return linesetPackage != null && linesetPackagesList != null && linesetPackagesList.containsValue(linesetPackage);
    }




    /**
     * return the installationData identified by the given element installationId
     *
     * @param linesetPackageId the id of the received package that should be retrivied
     * @return the installationData identified by the given element installationId
     */
    public ReceivedLinesetPackage getElement(String linesetPackageId) {
        if (linesetPackagesList != null && !GeneralStringUtils.isEmptyString(linesetPackageId)) {
            return (ReceivedLinesetPackage) linesetPackagesList.get(linesetPackageId);
        }
        return null;
    }

    /**
     * Tests if this linesetPackagesList contains more elements
     *
     * @return true if and only if this linesetPackagesList contains at least one more installationData to provide; false otherwise.
     */
    public boolean hasNext() {
        if (packagesListIterator == null) {
            packagesListIterator = getSortedLinesetPackagesListIterator();
        }
        return packagesListIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next installationData of this  list.
     */
    public ReceivedLinesetPackage next() {
        if (packagesListIterator == null) {
            packagesListIterator = getSortedLinesetPackagesListIterator();
        }
        return (ReceivedLinesetPackage) packagesListIterator.next();
    }

    public void rewind() {
        packagesListIterator = null;
    }


    /**
     * @return Returns the number of TpmsInstallationData in this linesetPackagesList.
     */
    public int size() {
        return linesetPackagesList.size();
    }

    /**
     * @return Tests if this linesetPackagesList contains no installationData. false otherwise
     */
    public boolean isEmpty() {
        return linesetPackagesList.isEmpty();
    }



}
