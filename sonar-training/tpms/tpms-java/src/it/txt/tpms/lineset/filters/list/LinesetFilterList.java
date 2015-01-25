package it.txt.tpms.lineset.filters.list;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.filters.LinesetFilter;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-dic-2006
 * Time: 15.30.19
 */
public class LinesetFilterList {


    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private HashMap privateLinesetFilterList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the ls list
    private Iterator linesetFilterIterator = null;

    /**
     * default contructor
     */
    public LinesetFilterList () {
        privateLinesetFilterList = new HashMap();
    }


    /**
     * add a ls to the list
     *
     * @param linesetFilterElement the element to be added
     *
     * @return KO_RESULT if the given ls is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement ( LinesetFilter linesetFilterElement ) {
        if ( linesetFilterElement == null || GeneralStringUtils.isEmptyString( linesetFilterElement.getId() ) )
            return KO_RESULT;
        else {
            privateLinesetFilterList.put( linesetFilterElement.getId(), linesetFilterElement );
            linesetFilterIterator = null;
        }
        return OK_RESULT;
    }

    /**
     * remove a ls from the  list
     *
     * @param linesetFilterId the key that identifies the ls in the list
     *
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given ls key is null or empty)
     */
    public int removeElement ( String linesetFilterId ) {
        if ( !GeneralStringUtils.isEmptyString( linesetFilterId ) ) {
            privateLinesetFilterList.remove( linesetFilterId );
            linesetFilterIterator = null;
            return OK_RESULT;
        } else {
            return KO_RESULT;
        }
    }

    /**
     * remove a linesetElement from the linesetElement list
     *
     * @param linesetFilterElement the linesetElement in the list that should be removed
     *
     * @return OK_RESULT, if the operation succeed, KO_RESULT otherwise (i.e. the given LinesetElement key is null or empty)
     */
    public int removeElement ( LinesetFilter linesetFilterElement ) {
        if ( linesetFilterElement != null && !GeneralStringUtils.isEmptyString( linesetFilterElement.getId() ) ) {
            return this.removeElement( linesetFilterElement.getId() );
        } else {
            return KO_RESULT;
        }
    }

    /**
     * true if the ls identified by linesetFilterId is present in the list, false otherwise
     *
     * @param linesetFilterId the id of the ls to look for.
     *
     * @return true if the  identified by LinesetId is present in the list, false otherwise
     */
    public boolean contains ( String linesetFilterId ) {
        if ( !GeneralStringUtils.isEmptyString( linesetFilterId ) && privateLinesetFilterList != null ) {
            return privateLinesetFilterList.containsKey( linesetFilterId );
        }
        return false;
    }

    /**
     * true if the given ls is present in the list, false otherwise
     *
     * @param linesetFilterElement the ls to look for.
     *
     * @return true if the given ls is present in the list, false otherwise
     */
    public boolean contains ( LinesetFilter linesetFilterElement ) {
        if ( linesetFilterElement != null && privateLinesetFilterList != null ) {
            return privateLinesetFilterList.containsValue( linesetFilterElement );
        }
        return false;
    }

    /**
     * return the ls identified by the given element id
     *
     * @param linesetFilterId
     *
     * @return the ls identified by the given linesetFilterId
     */
    public LinesetFilter getElement ( String linesetFilterId ) {
        if ( privateLinesetFilterList != null && !GeneralStringUtils.isEmptyString( linesetFilterId ) ) {
            return ( LinesetFilter ) privateLinesetFilterList.get( linesetFilterId );
        }
        return null;
    }






    /**
     * Tests if this privateLinesetFilterList contains more elements
     *
     * @return true if and only if this privateLinesetFilterList contains at least one more ls/ to provide; false otherwise.
     */
    public boolean hasNext () {
        if ( linesetFilterIterator == null ) {
            linesetFilterIterator = privateLinesetFilterList.values().iterator();
        }
        return linesetFilterIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next ls of this  list.
     */
    public LinesetFilter next () {
        if ( linesetFilterIterator == null ) {
            linesetFilterIterator = privateLinesetFilterList.values().iterator();
        }
        return ( LinesetFilter )linesetFilterIterator.next();
    }

    public void rewind () {
        linesetFilterIterator = null;
    }


    /**
     * @return Returns the number of ls in this privateLinesetFilterList.
     */
    public int size () {
        return privateLinesetFilterList.size();
    }

    /**
     * @return Tests if this privateLinesetFilterList contains no ls. false otherwise
     */
    public boolean isEmpty () {
        return privateLinesetFilterList.isEmpty();
    }
}
