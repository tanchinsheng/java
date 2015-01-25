package it.txt.general.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe di utilità generica sulle stringhe
 * User: fabio.furgiuele
 * Date: 17-feb-2005
 * Time: 10.53.37
 */
public class GeneralStringUtils {


    public static String getNotNullString ( String s ) {
        return isEmptyString( s ) ? "" : s;
    }


    public static String escapedEncode ( String message ) {
        if ( message == null ) return null;
        if ( message.length() > 0 ) {

            StringBuffer escapedMessage = new StringBuffer();
            int messageLength = message.length();
            char c1, c2;

            for ( int i = 0; i < messageLength; i++ ) {
                c1 = message.charAt( i );

                if ( i < ( messageLength - 1 ) ) {
                    c2 = message.charAt( i + 1 );
                } else {
                    c2 = 0;
                }

                if ( c1 == '\n' ) {
                    escapedMessage.append( '\\' );
                    escapedMessage.append( 'n' );
                } else if ( ( c1 == '\r' ) && ( c2 == '\n' ) ) {
                    // drop char (DOS end line char is CR + LF)
                } else if ( c1 == '\t' ) {
                    escapedMessage.append( '\\' );
                    escapedMessage.append( '\t' );
                } else if ( c1 == '\'' ) {
                    escapedMessage.append( '\\' );
                    escapedMessage.append( '\'' );
                } else if ( c1 == '\"' ) {
                    escapedMessage.append( '\\' );
                    escapedMessage.append( '\"' );
                } else if ( c1 == '\\' ) {
                    escapedMessage.append( '\\' );
                    escapedMessage.append( '\\' );
                } else {
                    escapedMessage.append( c1 );
                }
            } // for

            return escapedMessage.toString();
        }
        return "";
    }


    /**
     * Verifica  se la stringa in ingresso è nulla o vuota
     *
     * @param s
     *
     * @return true se la stringa in ingresso è nulla o vuota,
     *         false altriemnti
     */
    public static boolean isEmptyString ( String s ) {
        return !( s != null && !s.equals( "" ) );
    }

    /**
     * Verifica  se la stringa in ingresso è nulla o vuota trimmando gli spazi
     *
     * @param s
     *
     * @return true se la stringa in ingresso è nulla o vuota o composta di soli spazi,
     *         false altriemnti
     */
    public static boolean isEmptyTrimmedString ( String s ) {
        boolean result = isEmptyString( s );
        if ( !result )
            result = isEmptyString( s.trim() );
        return result;
    }


    public static String trimChar ( String s, char c ) {
        return leftTrimChar( rigthTrimChar( s, c ), c );
    }

    public static String leftTrimChar ( String s, char c ) {
        if ( !isEmptyString( s ) ) {
            int lastCharPos;
            do {

                lastCharPos = s.indexOf( c );
                if ( lastCharPos > -1 && lastCharPos == 0 ) {
                    s = s.substring( 1, s.length() );
                }
            } while ( lastCharPos == 0 );
        }
        return s;
    }

    public static String rigthTrimChar ( String s, char c ) {
        if ( !isEmptyString( s ) ) {
            int lastCharPos;
            do {

                lastCharPos = s.lastIndexOf( c );
                if ( lastCharPos > -1 && lastCharPos == ( s.length() - 1 ) ) {
                    s = s.substring( 0, lastCharPos );
                }
            } while ( lastCharPos == s.length() );
        }
        return s;
    }


    /**
     * rimuove gli zeri in testa a una stringa
     *
     * @param s la stringa da cui rimuvere gli zeri
     *
     * @return la stringa a cui sono stati rimossi gli zeri in testa, la stringa senza che
     *         sia apportata alcuna modifica nel caso di errore
     */
    public static String leftTrimZeros ( String s ) {
        if ( !isEmptyString( s ) ) {
            int lastZeroPos;
            do {

                lastZeroPos = s.indexOf( '0' );
                if ( lastZeroPos > -1 && lastZeroPos == 0 ) {
                    s = s.substring( 1, s.length() );
                }
            } while ( lastZeroPos == 0 );
        }
        return s;
    }


    public static String rigthTrimZeros ( String s ) {
        if ( !isEmptyString( s ) ) {
            int lastZeroPos;
            do {

                lastZeroPos = s.lastIndexOf( '0' );
                if ( lastZeroPos > -1 && lastZeroPos == ( s.length() - 1 ) ) {
                    s = s.substring( 0, lastZeroPos );
                }
            } while ( lastZeroPos == s.length() );
        }
        return s;
    }

    /**
     * aggiunge zeri davanti a s fino a che questa raggiunge lunghezza resultStringLength
     *
     * @param s                  la strings originale
     * @param resultStringLength la lunghezza della stringa finale
     *
     * @return s con un numero di zeri in davanti in modo che abbia lunghezza pari a resultStringLength, nel caso in cui uno
     *         dei parametri passati in ingresso NON sia valido ritorna s senza apportar modifiche.
     */
    public static String addLeftZeros ( String s, int resultStringLength ) {
        if ( !isEmptyString( s ) && resultStringLength > 0 ) {
            while ( s.length() < resultStringLength ) {
                s = "0" + s;
            }
        }
        return s;
    }

    /**
     * This method is intended to be used only with the jdk previous than 1.4.*
     * Replace all occurencies of a substring (oldString) with a new one
     * (newString) from a given string (source)
     *
     * @param source    it's the original string
     * @param oldString it's the substring to look for
     * @param newString it's the new substring
     *
     * @return a string where each occourrency of oldString is been repalced with newString
     */

    public static String replace ( String source, String oldString, String newString ) {
        if ( source != null && oldString != null && newString != null ) {
            int index = 0;

            while ( ( index = source.indexOf( oldString, index ) ) >= 0 ) {
                source = source.substring( 0, index ) + newString + source.substring( index + oldString.length() );
                index += newString.length();
            }
        }
        return source;
    }


    /**
     * Replace all occurencies of a substring (oldString) with a new one
     * (newString) from a given string (source)
     *
     * @param source    it's the original string
     * @param oldString it's the substring to look for
     * @param newString it's the new substring
     *
     * @return a string where each occourrency of oldString is been repalced with newString
     */

    public static String replaceAllIgnoreCase ( String source, String oldString, String newString ) {
        CoolString coolString = new CoolString( source );
        coolString.replaceAll( oldString, newString );
        return coolString.toString();
    }

    /**
     * Splits an input string into a an array of strings, seperating
     * at commas.
     *
     * @param input the string to split, possibly null or empty
     *
     * @return an array of the strings split at commas
     */
    public static String[] split ( String input )

    {
        if ( input == null )

            return new String[0];


        List strings = new ArrayList();


        int startx = 0;

        int cursor = 0;

        int length = input.length();


        while ( cursor < length )

        {
            if ( input.charAt( cursor ) == ',' )

            {
                String item = input.substring( startx, cursor );

                strings.add( item );

                startx = cursor + 1;

            }

            cursor++;

        }

        if ( startx < length )

            strings.add( input.substring( startx ) );


        return ( String[] )strings.toArray( new String[strings.size()] );

    }

    /**
     * Converts a string such that the first character is upper case.
     *
     * @param input the input string (possibly empty)
     *
     * @return the string with the first character converted from lowercase to upper case (may
     *         return the string unchanged if already capitalized)
     */

    public static String capitalize ( String input ) {
        if ( input.length() == 0 ) {
            return input;
        }
        char ch = input.charAt( 0 );
        return String.valueOf( Character.toUpperCase( ch ) ) + input.substring( 1 ).toLowerCase();

    }

    public static String join ( String[] input, char separator )

    {
        if ( input == null || input.length == 0 )

            return null;


        StringBuffer buffer = new StringBuffer();


        for ( int i = 0; i < input.length; i++ )

        {
            if ( i > 0 )

                buffer.append( separator );


            buffer.append( input[i] );

        }

        return buffer.toString();

    }

    /**
     * Replaces all occurrences of <code>pattern</code> in
     * <code>string</code> with <code>replacement</code>
     */
    public static String apacheReplace ( String string, String pattern, String replacement )

    {
        StringBuffer sbuf = new StringBuffer();

        int index = string.indexOf( pattern );

        int pos = 0;

        int patternLength = pattern.length();

        for ( ; index >= 0; index = string.indexOf( pattern, pos ) )

        {
            sbuf.append( string.substring( pos, index ) );

            sbuf.append( replacement );

            pos = index + patternLength;

        }
        sbuf.append( string.substring( pos ) );


        return sbuf.toString();

    }


}
