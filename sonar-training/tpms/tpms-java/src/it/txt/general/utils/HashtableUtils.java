package it.txt.general.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 20-dic-2006
 * Time: 16.35.22
 */
public class HashtableUtils {
    public static Hashtable dumpResultSetToHashtableOfHashtable ( ResultSet rs, String[] columnsKeys, String keyValuesSeparator ) {
        Hashtable table = new Hashtable();
        if ( rs != null ) {
            try {
                int colCount = rs.getMetaData().getColumnCount();
                Hashtable row;
                String currentColumnName = "";
                String currentColumnValue = "";
                String key;
                while ( rs.next() ) {
                    key = "";
                    row = new Hashtable( colCount );
                    for ( int i = 1; i <= colCount; i++ ) {
                        currentColumnName = rs.getMetaData().getColumnName( i );
                        currentColumnValue = rs.getString( i );
                        row.put( currentColumnName, currentColumnValue );
                        if ( ArrayUtils.indexOf( currentColumnName, columnsKeys ) >= 0 ) {
                            if ( GeneralStringUtils.isEmptyString( key ) ) {
                                key = currentColumnValue;
                            } else {
                                key = key + keyValuesSeparator + currentColumnValue;
                            }
                        }
                    }
                    table.put( key, row );
                }
            } catch ( SQLException e ) {

            }
        }
        return table;
    }

     public static Hashtable dumpResultSetRowToHashtable (ResultSet rs, String[] columnsKeys, String keyValuesSeparator) {
         Hashtable row = new Hashtable();
         try {
             if ( rs != null && !rs.isBeforeFirst() && !rs.isAfterLast()) {
                 int colCount = rs.getMetaData().getColumnCount();
                 row = new Hashtable( colCount );
                                 String currentColumnName = "";
                String currentColumnValue = "";
                String key = "";

                    for ( int i = 1; i <= colCount; i++ ) {
                        currentColumnName = rs.getMetaData().getColumnName( i );
                        currentColumnValue = rs.getString( i );
                        row.put( currentColumnName, currentColumnValue );
                        if ( ArrayUtils.indexOf( currentColumnName, columnsKeys ) >= 0 ) {
                            if ( GeneralStringUtils.isEmptyString( key ) ) {
                                key = currentColumnValue;
                            } else {
                                key = key + keyValuesSeparator + currentColumnValue;
                            }
                        }
                    }
             }
         } catch ( SQLException e ) {

         }
         return row;
     }

}
