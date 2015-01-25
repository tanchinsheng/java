package it.txt.tpms.users.list;


import it.txt.general.utils.CoolString;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.users.TpmsUser;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-ott-2006
 * Time: 13.07.33
 * To change this template use File | Settings | File Templates.
 */
public class TpmsUsersList {

    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap tpmsUsersList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the installationData list
    private Iterator usersListIterator = null;

    private ArrayList thisArrayList = null;


    private class AlphabeticalSortByTpmsLogin implements Comparator {

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

                String s1 = ((TpmsUser) obj1).getName().toLowerCase();
                String s2 = ((TpmsUser) obj2).getName().toLowerCase();


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
    public TpmsUsersList() {
        tpmsUsersList = new HashMap();
    }


    private ArrayList getSortedUsersArrayList() {
        ArrayList arrLst = new ArrayList(tpmsUsersList.values());
        Collections.sort(arrLst, new AlphabeticalSortByTpmsLogin());
        return arrLst;
    }


    private Iterator getSortedUsersListIterator() {
        return getSortedUsersArrayList().iterator();
    }

    private void populateArrayList() {

        thisArrayList = new ArrayList();
        Iterator myIterator = getSortedUsersListIterator();
        TpmsUser tmpOneUser;
        while (myIterator.hasNext()) {
            tmpOneUser = (TpmsUser) myIterator.next();
            thisArrayList.add(tmpOneUser.getUserDataHashMap());
        }

    }

    public ArrayList getUsersArrayListOfHashMap() {
        if (thisArrayList == null || (thisArrayList.size() == 0 && tpmsUsersList.size() > 0)) {
            populateArrayList();
        }
        return thisArrayList;
    }


    /**
     * add a installationData to the list
     *
     * @param tpmsUser the element to be added
     * @return KO_RESULT if the given installationData is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(TpmsUser tpmsUser) {
        if (tpmsUser == null || GeneralStringUtils.isEmptyString(tpmsUser.getId()))
            return KO_RESULT;
        else {
            tpmsUsersList.put(tpmsUser.getId(), tpmsUser);
            usersListIterator = null;
        }
        return OK_RESULT;
    }


    /**
     * remove a installationData from the  list
     *
     * @param userId the key that identifies the installationData in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(String userId) {
        if (!GeneralStringUtils.isEmptyString(userId)) {
            tpmsUsersList.remove(userId);
            usersListIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a TpmsInstallationID from the TpmsInstallationList list
     *
     * @param tpmsUser the TpmsInstallationData in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given installationData key is null or empty)
     */
    public int removeElement(TpmsUser tpmsUser) {
        if (tpmsUser != null && !GeneralStringUtils.isEmptyString(tpmsUser.getId())) {
            return this.removeElement(tpmsUser.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the installationData identified by installationId is present in the list, false otherwise
     *
     * @param userId the id of the ls to look for.
     * @return true if the  identified by installationId is present in the list, false otherwise
     */
    public boolean containsTpmsUser(String userId) {
        if (!GeneralStringUtils.isEmptyString(userId) && tpmsUsersList != null) {
            return tpmsUsersList.containsKey(userId);
        }
        return false;
    }

    /**
     * true if the given installationData is present in the list, false otherwise
     *
     * @param tpmsUser the installationData to look for.
     * @return true if the given installationData is present in the list, false otherwise
     */
    public boolean containsTpmsUser(TpmsUser tpmsUser) {
        if (tpmsUser != null && tpmsUsersList != null) {
            return tpmsUsersList.containsValue(tpmsUser);
        }
        return false;
    }

    /**
     * return the installationData identified by the given element installationId
     *
     * @param tpmsLogin
     * @return the installationData identified by the given element installationId
     */
    public TpmsUser getElementByTpmsLogin(String tpmsLogin) {
        if (tpmsUsersList != null && !GeneralStringUtils.isEmptyString(tpmsLogin)) {
            Iterator myUsersIterator = getSortedUsersListIterator();
            TpmsUser tpmsUser;
            while (myUsersIterator.hasNext()) {
                tpmsUser = (TpmsUser) myUsersIterator.next();
                if (tpmsLogin.equals(tpmsUser.getName())) {
                    return tpmsUser;
                }
            }
        }
        return null;
    }


    /**
     * return the installationData identified by the given element installationId
     *
     * @param userId
     * @return the installationData identified by the given element installationId
     */
    public TpmsUser getElement(String userId) {
        if (tpmsUsersList != null && !GeneralStringUtils.isEmptyString(userId)) {
            return (TpmsUser) tpmsUsersList.get(userId);
        }
        return null;
    }

    /**
     * Tests if this tpmsUsersList contains more elements
     *
     * @return true if and only if this tpmsUsersList contains at least one more installationData to provide; false otherwise.
     */
    public boolean hasNext() {
        if (usersListIterator == null) {
            usersListIterator = getSortedUsersListIterator();
        }
        return usersListIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next installationData of this  list.
     */
    public TpmsUser next() {
        if (usersListIterator == null) {
            usersListIterator = getSortedUsersListIterator();
        }
        return (TpmsUser) usersListIterator.next();
    }

    public void rewind() {
        usersListIterator = null;
    }


    /**
     * @return Returns the number of TpmsInstallationData in this tpmsUsersList.
     */
    public int size() {
        return tpmsUsersList.size();
    }

    /**
     * @return Tests if this tpmsUsersList contains no installationData. false otherwise
     */
    public boolean isEmpty() {
        return tpmsUsersList.isEmpty();
    }


}
