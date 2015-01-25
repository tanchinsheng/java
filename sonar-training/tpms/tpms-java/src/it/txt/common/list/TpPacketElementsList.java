package it.txt.common.list;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.common.elements.TpPacketElement;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 1-mar-2006
 * Time: 10.43.53
 * this class represent a list of tp and packages
 */
public class TpPacketElementsList {
    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private Hashtable elementsList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the tp/packet list
    private Enumeration elementEnumeration = null;

    /**
     * default contructor
     */
    public TpPacketElementsList() {
        elementsList = new Hashtable();
    }

    /**
     * add a tp or a packet to the list
     *
     * @param tpPackageElement the element to be added
     * @return KO_RESULT if the given tp/packet is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement(TpPacketElement tpPackageElement ) {
        if (tpPackageElement == null || GeneralStringUtils.isEmptyString(tpPackageElement.getId()))
            return KO_RESULT;
        else {
            elementsList.put(tpPackageElement.getId(), tpPackageElement);
            elementEnumeration = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a tp or packet from the packet list
     *
     * @param elementId the key that identifies the tp or packet in the list
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given tp/packet key is null or empty)
     */
    public int removeElement(String elementId) {
        if (!GeneralStringUtils.isEmptyString(elementId)) {
            elementsList.remove(elementId);
            elementEnumeration = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a tpPackageElement from the tpPackageElement list
     *
     * @param tpPackageElement the tpPackageElement in the list that should be removed
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given tpPackageElement key is null or empty)
     */
    public int removeElement(TpPacketElement tpPackageElement) {
        if (tpPackageElement != null && !GeneralStringUtils.isEmptyString(tpPackageElement.getId())) {
            return this.removeElement(tpPackageElement.getId());
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the tp/packet identified by elementId is present in the list, false otherwise
     *
     * @param elementId the id of the tp/packet to look for.
     * @return true if the packet identified by elementId is present in the list, false otherwise
     */
    public boolean containsElement(String elementId) {
        if (!GeneralStringUtils.isEmptyString(elementId) && elementsList != null) {
            return elementsList.containsKey(elementId);
        }
        return false;
    }

    /**
     * true if the given tp or packet is present in the list, false otherwise
     *
     * @param tpPackageElement the tp or packet to look for.
     * @return true if the given tp or packet is present in the list, false otherwise
     */
    public boolean containsElemenet(TpPacketElement tpPackageElement) {
        if (tpPackageElement != null && elementsList != null) {
            return elementsList.containsValue(tpPackageElement);
        }
        return false;
    }

    /**
     * return the tp or packet identified by the given element id
     *
     * @param elementId
     * @return the tp/packet identified by the given elementId
     */
    public TpPacketElement getElement(String elementId) {
        if (elementsList != null && !GeneralStringUtils.isEmptyString(elementId)) {
            return (TpPacketElement) elementsList.get(elementId);
        }
        return null;
    }

    /**
     * Tests if this elementsList contains more elements
     *
     * @return true if and only if this elementsList contains at least one more tp/packet to provide; false otherwise.
     */
    public boolean hasMoreElements() {
        if (elementEnumeration == null) {
            elementEnumeration = elementsList.elements();
        }
        return elementEnumeration.hasMoreElements();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next tp/packet of this packet list.
     */
    public TpPacketElement nextElement() {
        if (elementEnumeration == null) {
            elementEnumeration = elementsList.elements();
        }
        return (TpPacketElement) elementEnumeration.nextElement();
    }

    /**
     * @return Returns the number of tp/packets in this elementsList.
     */
    public int size() {
        return elementsList.size();
    }

    /**
     * @return Tests if this elementsList contains no tp/packets. false otherwise
     */
    public boolean isEmpty() {
        return elementsList.isEmpty();
    }
}
