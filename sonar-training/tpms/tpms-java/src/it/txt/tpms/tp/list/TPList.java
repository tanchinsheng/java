package it.txt.tpms.tp.list;



import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tp.TP;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 17-mag-2006
 * Time: 16.28.46
 * To change this template use File | Settings | File Templates.
 */
public class TPList {
    //todo implementare metodi di sorting????
    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap privateTpList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the tp/packet list
    private Iterator tpIterator = null;

    /**
     * default contructor
     */
    public TPList() {
        privateTpList = new HashMap();
    }

    /**
     * add a tp or a packet to the list
     *
     * @param tpElement the element to be added
     * @return KO_RESULT if the given tp/packet is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(TP tpElement ) {
        if (tpElement == null || GeneralStringUtils.isEmptyString(tpElement.getId()))
            return KO_RESULT;
        else {
            privateTpList.put(tpElement.getId(), tpElement);
            tpIterator = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a tp or packet from the packet list
     *
     * @param tpId the key that identifies the tp or packet in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given tp/packet key is null or empty)
     */
    public int removeElement(String tpId) {
        if (!GeneralStringUtils.isEmptyString(tpId)) {
            privateTpList.remove(tpId);
            tpIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a tpElement from the tpElement list
     *
     * @param tpElement the tpElement in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given tpElement key is null or empty)
     */
    public int removeElement(TP tpElement) {
        if (tpElement != null && !GeneralStringUtils.isEmptyString(tpElement.getId())) {
            return this.removeElement(tpElement.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the tp/packet identified by tpId is present in the list, false otherwise
     *
     * @param tpId the id of the tp/packet to look for.
     * @return true if the packet identified by tpId is present in the list, false otherwise
     */
    public boolean containsTP(String tpId) {
        if (!GeneralStringUtils.isEmptyString(tpId) && privateTpList != null) {
            return privateTpList.containsKey(tpId);
        }
        return false;
    }

    /**
     * true if the given tp or packet is present in the list, false otherwise
     *
     * @param tpElement the tp or packet to look for.
     * @return true if the given tp or packet is present in the list, false otherwise
     */
    public boolean containsTP(TP tpElement) {
        if (tpElement != null && privateTpList != null) {
            return privateTpList.containsValue(tpElement);
        }
        return false;
    }

    /**
     * return the tp or packet identified by the given element id
     *
     * @param tpId
     * @return the tp/packet identified by the given tpId
     */
    public TP getElement(String tpId) {
        if (privateTpList != null && !GeneralStringUtils.isEmptyString(tpId)) {
            return (TP) privateTpList.get(tpId);
        }
        return null;
    }

    /**
     * Tests if this privateTpList contains more elements
     *
     * @return true if and only if this privateTpList contains at least one more tp/packet to provide; false otherwise.
     */
    public boolean hasNext() {
        if (tpIterator == null) {
            tpIterator = privateTpList.values().iterator();
        }
        return tpIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next tp/packet of this packet list.
     */
    public TP next() {
        if (tpIterator == null) {
            tpIterator = privateTpList.values().iterator();
        }
        return (TP) tpIterator.next();
    }

    public void rewind(){
        tpIterator = null;
    }


    /**
     * @return Returns the number of tp/packets in this privateTpList.
     */
    public int size() {
        return privateTpList.size();
    }

    /**
     * @return Tests if this privateTpList contains no tp/packets. false otherwise
     */
    public boolean isEmpty() {
        return privateTpList.isEmpty();
    }

}
