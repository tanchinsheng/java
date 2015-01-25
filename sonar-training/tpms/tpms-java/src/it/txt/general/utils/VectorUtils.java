package it.txt.general.utils;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


/**
 * Classe di utilità generica sui vettori
 * User: fabio.furgiuele
 * Date: 16-mar-2005
 * Time: 15.44.10
 */
public class VectorUtils {


    private static AlphabeticalSort myAlphabeticalSort = null;

    static {
        myAlphabeticalSort = new AlphabeticalSort();
    }

    public static void sortAlphabeticallyVectorOfStrings ( Vector v ) {
        Collections.sort( v, myAlphabeticalSort );
    }


    /**
     * dato un vettore i cui elementi sono array di stringhe cerca nella colonna arrayColumnNumber
     * il valore value passato in ingresso
     *
     * @param v                 vettore i cui elementi sono array di stringhe
     * @param arrayColumnNumber colonna in cui cercare il valore
     * @param value             valore da ricercare
     *
     * @return la posizione all'interno del vettore in cui è contenuto l'array che a suia volta contiene il valore,
     *         -1 se non viene trovato il valore o se i dati in ingresso non sono validi.
     */
    public static int findInVectorOfStringArray ( Vector v, int arrayColumnNumber, String value ) {
        if ( v != null && !v.isEmpty() &&
                arrayColumnNumber >= 0 && !GeneralStringUtils.isEmptyString( value ) ) {
            Iterator it = v.iterator();
            String[] tmpArr;
            int arrayLenght;
            int pos = 0;
            while ( it.hasNext() ) {
                tmpArr = ( String[] )it.next();
                arrayLenght = tmpArr.length;
                if ( arrayColumnNumber < arrayLenght && tmpArr[arrayColumnNumber].equals( value ) ) {
                    return pos;
                }
                ++pos;
            }
        }
        return -1;
    }

    /**
     * dato un vettore i cui elementi sono vettori  cerca nella colonna columnNumber
     * il valore value passato in ingresso
     *
     * @param v            vettore i cui elementi sono vettori
     * @param columnNumber colonna in cui cercare il valore, se è negativo cerca in tutte le posizioni del sottovettore
     * @param value        valore da ricercare
     *
     * @return la posizione all'interno del vettore in cui è contenuto il vettore che a sua volta contiene il valore,
     *         -1 se non viene trovato il valore o se i dati in ingresso non sono validi.
     */
    public static int findInVectorOfVectors ( Vector v, int columnNumber, String value ) {
        if ( v != null && !v.isEmpty() && !GeneralStringUtils.isEmptyString( value ) ) {
            Iterator it = v.iterator();
            Iterator subIt;
            Vector subVect;
            String tmpStr;
            int pos = 0;

            while ( it.hasNext() ) {

                if ( columnNumber < 0 ) {
                    subIt = ( ( Vector )it.next() ).iterator();
                    while ( subIt.hasNext() ) {
                        tmpStr = ( String )subIt.next();
                        if ( tmpStr.equals( value ) ) {
                            return pos;
                        }
                    }
                } else {
                    subVect = ( ( Vector )it.next() );
                    if ( subVect.size() > columnNumber ) {
                        tmpStr = ( String )subVect.get( columnNumber );
                        if ( tmpStr.equals( value ) ) {
                            return pos;
                        }
                    }
                }
                ++pos;
            }
        }
        return -1;
    }

    /**
     * dato un vettore i cui elementi sono stringhe  cerca la stringa passata in ingresso
     *
     * @param v     vettore i cui elementi sono stringhe
     * @param value valore da ricercare
     *
     * @return la posizione all'interno del vettore in cui è contenuto la stringa
     *         -1 se non viene trovato il valore o se i dati in ingresso non sono validi.
     */
    public static int findInVectorOfString ( Vector v, String value ) {
        if ( v != null && !v.isEmpty() ) {
            Iterator it = v.iterator();
            String tmpStr;
            int pos = 0;

            while ( it.hasNext() ) {
                tmpStr = ( String )it.next();
                if ( tmpStr.equals( value ) ) {
                    return pos;
                }
                ++pos;
            }
        }
        return -1;
    }

    /**
     * dato un vettore i cui elementi sono vettori  cerca negli elementi del secondo vettore
     * il valore value passato in ingresso
     *
     * @param v     vettore i cui elementi sono vettori
     * @param value valore da ricercare
     *
     * @return la posizione all'interno del vettore in cui è contenuto il vettore che a sua volta contiene il valore,
     *         -1 se non viene trovato il valore o se i dati in ingresso non sono validi.
     */
    public static int findInVectorOfVectors ( Vector v, String value ) {
        return findInVectorOfVectors( v, -1, value );
    }

    /**
     * Dato un Resultset lo trasforma in un vettore di String Array
     *
     * @param rs
     *
     * @return vettore di String Array
     */
    public static Vector dumpResultSetToVectorOfStrArray ( ResultSet rs ) {

        return dumpResultSetToVectorOfStrArray( rs, false );
    }

    /**
     * Dato un Resultset lo trasforma in un vettore di String Array
     *
     * @param rs
     * @param addEmptyEntry
     *
     * @return vettore di String Array
     */
    public static Vector dumpResultSetToVectorOfStrArray ( ResultSet rs, boolean addEmptyEntry ) {
        Vector vect = new Vector();
        if ( rs != null ) {
            try {
                int colCount = rs.getMetaData().getColumnCount();
                String[] row;

                while ( rs.next() ) {
                    if ( rs.isFirst() && addEmptyEntry ) {
                        row = new String[colCount];
                        for ( int i = 0; i < colCount; i++ ) {
                            row[i] = "";
                        }
                        vect.add( row );
                    }

                    row = new String[colCount];
                    for ( int i = 1; i <= colCount; i++ ) {
                        row[i - 1] = rs.getString( i );
                    }
                    vect.add( row );
                }
            } catch ( SQLException e ) {
                
            }
        }
        return vect;
    }

    /**
     * Dato un Resultset lo trasforma in un vettore di Hashtable
     *
     * @param rs
     *
     * @return vettore di Hashtable
     */
    public static Vector dumpResultSetToVectorOfHashtable ( ResultSet rs ) {
        Vector vect = new Vector();
        if ( rs != null ) {
            try {
                ResultSetMetaData rsMd = rs.getMetaData();
                int colCount = rsMd.getColumnCount();
                Hashtable row;
                while ( rs.next() ) {
                    row = new Hashtable( colCount );
                    for ( int i = 1; i <= colCount; i++ ) {
                        String tmpValue = rs.getString( i );
                        if ( tmpValue == null ) {
                            row.put( rsMd.getColumnName( i ), "" );
                        } else {
                            row.put( rsMd.getColumnName( i ), tmpValue );
                        }
                    }
                    vect.add( row );
                }
            } catch ( SQLException e ) {
                //System.err.println("VectorUtils :: dumpResultSetToVectorOfHashtable(ResultSet rs) : SQLException message " + e.getMessage());
            }
        }

        return vect;
    }

    /**
     * Trasforma i valori di una Hashtable in un vettore
     *
     * @param hashtable da trasformare
     *
     * @return un vettore contenente i valori della hashtable
     */
    public static Vector dumpHashtableToVector ( Hashtable hashtable ) {
        Vector result = new Vector();
        if ( hashtable != null && !hashtable.isEmpty() ) {
            result.addAll( hashtable.values() );
        }
        return result;
    }
}
