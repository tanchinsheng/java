package it.txt.tpms.tp.list;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tp.ProductionArea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-gen-2007
 * Time: 9.55.00
 */
public class ProductionAreasList {

    //OK_RESULT and KO_RESULT are used in add and remove methods to return respectively succesfull or error operation
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = -1;
    //this hash table contains the list of packages represented by this list.
    private Vector productionAreasList = null;
    //this enumeration is usefull to implement nextElement and has moreElements in the tp/packet list
    private Iterator productionAreasIterator = null;


    /**
     * default contructor
     */
    public ProductionAreasList () {
        productionAreasList = new Vector();
    }


    public ProductionAreasList ( ProductionAreasList productionAreas ) {
        productionAreasList = new Vector(  );
        this.addElements( productionAreas );
    }


    /**
     * add a tp or a packet to the list
     *
     * @param productionArea the element to be added
     *
     * @return KO_RESULT if the given tp/packet is null or do not have an id, OK_RESULT otherwirse
     */
    public int addElement ( ProductionArea productionArea ) {
        if ( productionArea == null || GeneralStringUtils.isEmptyString( productionArea.getId() ) )
            return KO_RESULT;
        else {
            productionAreasList.add( productionArea );
            productionAreasIterator = null;
        }
        return OK_RESULT;
    }


    public int addElements ( ProductionAreasList productionAreas ) {
        if ( productionAreas == null )
            return KO_RESULT;
        else {
            ProductionArea pa;
            productionAreas.rewind();
            while (productionAreas.hasNext()){
                pa = productionAreas.next();
                productionAreasList.add( pa );
            }
            productionAreasIterator = null;
        }
        return OK_RESULT;
    }


    public ProductionArea findProductAreaById ( String productionAreaId ) {
        if ( productionAreasList != null && !productionAreasList.isEmpty() && !GeneralStringUtils.isEmptyString( productionAreaId ) ) {
            Iterator it = productionAreasList.iterator();
            ProductionArea tmpProdArea;
            while ( it.hasNext() ) {
                tmpProdArea = ( ProductionArea )it.next();
                if ( productionAreaId.equals( tmpProdArea.getId() ) ) {
                    return tmpProdArea;
                }
            }
        }
        return null;
    }


    public ProductionAreasList findProductAreasByInstallationId ( String installationId ) {
        ProductionAreasList result = new ProductionAreasList();
        if ( productionAreasList != null && !productionAreasList.isEmpty() && !GeneralStringUtils.isEmptyString( installationId ) ) {
            Iterator it = productionAreasList.iterator();
            ProductionArea tmpProdArea;
            while ( it.hasNext() ) {
                tmpProdArea = ( ProductionArea )it.next();
                if ( installationId.equals( tmpProdArea.getInstallationId() ) ) {
                    result.addElement( tmpProdArea );
                }
            }
        }
        return result;
    }

    public ProductionAreasList findProductAreasByPlant ( String plant ) {
        ProductionAreasList result = new ProductionAreasList();
        if ( productionAreasList != null && !productionAreasList.isEmpty() && !GeneralStringUtils.isEmptyString( plant ) ) {
            Iterator it = productionAreasList.iterator();
            ProductionArea tmpProdArea;
            while ( it.hasNext() ) {
                tmpProdArea = ( ProductionArea )it.next();
                if ( plant.equals( tmpProdArea.getPlant() ) ) {
                    result.addElement( tmpProdArea );
                }
            }
        }
        return result;
    }

    public ProductionAreasList findProductAreasByDescription ( String description ) {
        ProductionAreasList result = new ProductionAreasList();
        if ( productionAreasList != null && !productionAreasList.isEmpty() && !GeneralStringUtils.isEmptyString( description ) ) {
            Iterator it = productionAreasList.iterator();
            ProductionArea tmpProdArea;
            while ( it.hasNext() ) {
                tmpProdArea = ( ProductionArea )it.next();
                if ( description.equals( tmpProdArea.getDescription() ) ) {
                    result.addElement( tmpProdArea );
                }
            }
        }
        return result;
    }


    /**
     * Tests if this productionAreasList contains more elements
     *
     * @return true if and only if this productionAreasList contains at least one more tp/packet to provide; false otherwise.
     */
    public boolean hasNext () {
        if ( productionAreasIterator == null ) {
            productionAreasIterator = productionAreasList.iterator();
        }
        return productionAreasIterator.hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next tp/packet of this packet list.
     */
    public ProductionArea next () {
        if ( productionAreasIterator == null ) {
            productionAreasIterator = productionAreasList.iterator();
        }
        return ( ProductionArea )productionAreasIterator.next();
    }

    public void rewind () {
        productionAreasIterator = null;
    }


    /**
     * @return Returns the number of tp/packets in this productionAreasList.
     */
    public int size () {
        return productionAreasList.size();
    }

    /**
     * @return Tests if this productionAreasList contains no tp/packets. false otherwise
     */
    public boolean isEmpty () {
        return productionAreasList.isEmpty();
    }


    public ArrayList getProductAreaArrayList(){
       return new ArrayList( productionAreasList );
    }
}
