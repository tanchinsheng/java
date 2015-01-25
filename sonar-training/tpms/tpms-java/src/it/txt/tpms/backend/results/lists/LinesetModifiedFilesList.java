package it.txt.tpms.backend.results.lists;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.backend.results.LinesetModifiedFileData;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 11-ott-2006
 * Time: 11.53.58
 * This class is used to contain a list of files modified, deleted or added inside of lineset
 */
public class LinesetModifiedFilesList {

    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap privateLinesetModifiedFilesList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the ls list
    private Iterator linesetIterator = null;

    /**
     * default contructor
     */
    public LinesetModifiedFilesList() {
        privateLinesetModifiedFilesList = new HashMap();
    }

    /**
     * add a ls to the list
     *
     * @param lsFile the element to be added
     * @return KO_RESULT if the given ls is null or do not have an id, OK_RESULT otherwirse
     */
    public int addFile(LinesetModifiedFileData lsFile ) {
        if (lsFile == null || GeneralStringUtils.isEmptyString(lsFile.getId()))
            return KO_RESULT;
        else {
            privateLinesetModifiedFilesList.put(lsFile.getId(), lsFile);
            linesetIterator = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a ls from the  list
     *
     * @param fileId the key that identifies the ls in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given ls key is null or empty)
     */
    public int removeFile(String fileId) {
        if (!GeneralStringUtils.isEmptyString(fileId)) {
            privateLinesetModifiedFilesList.remove(fileId);
            linesetIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a linesetElement from the linesetElement list
     *
     * @param lsFile the linesetElement in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given LinesetElement key is null or empty)
     */
    public int removeFile(LinesetModifiedFileData lsFile) {
        if (lsFile != null && !GeneralStringUtils.isEmptyString(lsFile.getId())) {
            return this.removeFile(lsFile.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the ls identified by lsId is present in the list, false otherwise
     *
     * @param fileId the id of the ls to look for.
     * @return true if the  identified by LinesetId is present in the list, false otherwise
     */
    public boolean containsFile(String fileId) {
        if (!GeneralStringUtils.isEmptyString(fileId) && privateLinesetModifiedFilesList != null) {
            return privateLinesetModifiedFilesList.containsKey(fileId);
        }
        return false;
    }

    /**
     * true if the given ls is present in the list, false otherwise
     *
     * @param lsFile the ls to look for.
     * @return true if the given ls is present in the list, false otherwise
     */
    public boolean containsFile(LinesetModifiedFileData lsFile) {
        if (lsFile != null && privateLinesetModifiedFilesList != null) {
            return privateLinesetModifiedFilesList.containsValue(lsFile);
        }
        return false;
    }

    /**
     * return the ls identified by the given element id
     *
     * @param fileId
     * @return the ls identified by the given lsId
     */
    public LinesetModifiedFileData getFile(String fileId) {
        if (privateLinesetModifiedFilesList != null && !GeneralStringUtils.isEmptyString(fileId)) {
            return (LinesetModifiedFileData) privateLinesetModifiedFilesList.get(fileId);
        }
        return null;
    }

    /**
     * Tests if this privateLinesetModifiedFilesList contains more elements
     *
     * @return true if and only if this privateLinesetModifiedFilesList contains at least one more ls/ to provide; false otherwise.
     */
    public boolean hasNext() {
        if (linesetIterator == null) {
            linesetIterator = privateLinesetModifiedFilesList.values().iterator();
        }
        return linesetIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next ls of this  list.
     */
    public LinesetModifiedFileData next() {
        if (linesetIterator == null) {
            linesetIterator = privateLinesetModifiedFilesList.values().iterator();
        }
        return (LinesetModifiedFileData) linesetIterator.next();
    }

    public void rewind(){
        linesetIterator = null;
    }


    /**
     * @return Returns the number of ls in this privateLinesetModifiedFilesList.
     */
    public int size() {
        return privateLinesetModifiedFilesList.size();
    }

    /**
     * @return Tests if this privateLinesetModifiedFilesList contains no ls. false otherwise
     */
    public boolean isEmpty() {
        return privateLinesetModifiedFilesList.isEmpty();
    }
}
