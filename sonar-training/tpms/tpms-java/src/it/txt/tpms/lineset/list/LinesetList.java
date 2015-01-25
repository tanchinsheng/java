package it.txt.tpms.lineset.list;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.Lineset;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 14-set-2006
 * Time: 13.13.32
 */
public class LinesetList {
    //todo implementare metodi di sorting????
    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap privateLinesetList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the ls list
    private Iterator linesetIterator = null;

    /**
     * default contructor
     */
    public LinesetList() {
        privateLinesetList = new HashMap();
    }

    /**
     * add a ls to the list
     *
     * @param lsElement the element to be added
     * @return KO_RESULT if the given ls is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(Lineset lsElement ) {
        if (lsElement == null || GeneralStringUtils.isEmptyString(lsElement.getId()))
            return KO_RESULT;
        else {
            privateLinesetList.put(lsElement.getId(), lsElement);
            linesetIterator = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a ls from the  list
     *
     * @param lsId the key that identifies the ls in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given ls key is null or empty)
     */
    public int removeElement(String lsId) {
        if (!GeneralStringUtils.isEmptyString(lsId)) {
            privateLinesetList.remove(lsId);
            linesetIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a linesetElement from the linesetElement list
     *
     * @param lsElement the linesetElement in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given LinesetElement key is null or empty)
     */
    public int removeElement(Lineset lsElement) {
        if (lsElement != null && !GeneralStringUtils.isEmptyString(lsElement.getId())) {
            return this.removeElement(lsElement.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the ls identified by lsId is present in the list, false otherwise
     *
     * @param lsId the id of the ls to look for.
     * @return true if the  identified by LinesetId is present in the list, false otherwise
     */
    public boolean containsLineset(String lsId) {
        if (!GeneralStringUtils.isEmptyString(lsId) && privateLinesetList != null) {
            return privateLinesetList.containsKey(lsId);
        }
        return false;
    }

    /**
     * true if the given ls is present in the list, false otherwise
     *
     * @param lsElement the ls to look for.
     * @return true if the given ls is present in the list, false otherwise
     */
    public boolean containsLineset(Lineset lsElement) {
        if (lsElement != null && privateLinesetList != null) {
            return privateLinesetList.containsValue(lsElement);
        }
        return false;
    }

    /**
     * return the ls identified by the given element id
     *
     * @param lsId
     * @return the ls identified by the given lsId
     */
    public Lineset getElement(String lsId) {
        if (privateLinesetList != null && !GeneralStringUtils.isEmptyString(lsId)) {
            return (Lineset) privateLinesetList.get(lsId);
        }
        return null;
    }

    /**
     * Tests if this privateLinesetList contains more elements
     *
     * @return true if and only if this privateLinesetList contains at least one more ls/ to provide; false otherwise.
     */
    public boolean hasNext() {
        if (linesetIterator == null) {
            linesetIterator = privateLinesetList.values().iterator();
        }
        return linesetIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next ls of this  list.
     */
    public Lineset next() {
        if (linesetIterator == null) {
            linesetIterator = privateLinesetList.values().iterator();
        }
        return (Lineset) linesetIterator.next();
    }

    public void rewind(){
        linesetIterator = null;
    }


    /**
     * @return Returns the number of ls in this privateLinesetList.
     */
    public int size() {
        return privateLinesetList.size();
    }

    /**
     * @return Tests if this privateLinesetList contains no ls. false otherwise
     */
    public boolean isEmpty() {
        return privateLinesetList.isEmpty();
    }
    
    public Lineset[] toSortedArray(){
    	Collection coll = privateLinesetList.values();
    	Lineset[] lsArray = (Lineset[])coll.toArray(new Lineset[0]);    	
    	Arrays.sort(lsArray);
    	
    	return lsArray;
    }
}
