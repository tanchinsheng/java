package it.txt.general.utils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-gen-2006
 * Time: 13.29.45
 * To change this template use File | Settings | File Templates.
 */

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * A mutable string class, suitable for slamming strings into and then munging
 * based on other strings. Insert, delete and replace operations are all supported,
 * as well as tokenization. Comparisons can be case sensitive or not.
 *
 * @author Fabio Furgiuele
 * @version 1.0.2
 */
public final class CoolString implements java.io.Serializable {
    protected char buffer[];
    protected int count;
    protected boolean caseSensitive = false;


    /**
     * Construct an empty string.
     */
    public CoolString() {
        buffer = new char[0];    // trash
        count = 0;
    }


    /**
     * Construct a cool string from a Java String.
     */
    public CoolString(String s) {
        set(s);
    }


    /**
     * Construct a cool string from a CoolString.
     */
    public CoolString(CoolString s) {
        set(s);
    }


    /**
     * Construct a cool string from a char array.
     */
    public CoolString(char[] s) {
        set(s);
    }


    /**
     * Construct a cool string from a file.
     */
    public CoolString(File f) throws java.io.FileNotFoundException, java.io.IOException {
        set(f);
    }


    /**
     * Set this cool string from a file.
     */
    public void set(File f) throws java.io.FileNotFoundException, java.io.IOException {
        FileInputStream fis = new FileInputStream(f);
        try {
            DataInputStream dis = new DataInputStream(fis);
            String temp = dis.readUTF();    // trash
            set(temp);
        }
        finally {
            fis.close();
        }
    }


    /**
     * Set this to a specified String, discarding any current string.
     */
    public void set(String s) {
        buffer = s.toCharArray();
        count = s.length();
    }


    /**
     * Set this to a specified CoolString, discarding any current string.
     */
    public void set(CoolString s) {
        buffer = s.toCharArray();
        count = s.length();
    }


    /**
     * Set this to a specified char array, discarding any current string.
     */
    public void set(char[] s) {
        buffer = s;
        count = s.length;
    }


    /**
     * Convert to String.
     */
    public String toString() {
        return String.valueOf(buffer);
    }


    /**
     * Return the raw char[]
     */
    public char[] toCharArray() {
        char[] newBuffer = new char[count];
        System.arraycopy(buffer, 0, newBuffer, 0, count);
        return newBuffer;
    }


    /**
     * Return the current string length.
     */
    public int length() {
        return count;
    }


    /**
     * Return the offset to the first instance of the specified String.
     */
    public int indexOf(String s) {
        return indexOf(s, 0);
    }


    /**
     * Return the offset to the specified String starting from the specified index.
     * Should replace this with Wirth's improved algorithm!
     */
    public int indexOf(String s, int startingIndex) {
        int sCount = s.length();
        // bail if substring is longer than ours
        if (sCount > count)
            return -1;
        // create temporary buffer
        char sub[] = s.toCharArray();    // trash
        // search for offset
        int i = 0;
        int j = startingIndex;
        while (i < sCount && j < count - sCount + 1) {
            i = 0;
            while (i < sCount && equalChars(j + i, sub[i])) i++;
            if (i < sCount)
                j++;
        }
        // if we found all the chars from s, then j is the zero based offset
        if (i == sCount)
            return j;
        else
            return -1;
    }


    /**
     * Return the offset to the last instance of the specified String.
     */
    public int lastIndexOf(String s) {
        return lastIndexOf(s, count);
    }


    /**
     * Return the offset to the specified String starting from the specified index.
     * Should replace this with Wirth's improved algorithm!
     */
    public int lastIndexOf(String s, int startingIndex) {
        int sCount = s.length();
        // bail if substring is longer than ours
        if (sCount > count)
            return -1;
        // create temporary buffer
        char sub[] = s.toCharArray();    // trash
        // search for offset
        int i = 0;
        int j = Math.min(startingIndex, count - sCount);
        while (i < sCount && j >= 0) {
            i = 0;
            while (i < sCount && equalChars(j + i, sub[i])) i++;
            if (i < sCount)
                --j;
        }
        // if we found all the chars from s, then j is the offset
        if (i == sCount)
            return j;
        else
            return -1;
    }


    /**
     * Set all comparisons to either consider or ignore case.  When caseSensitive is true, then
     * case matters. Default is case insensitive (i.e. false).
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }


    /**
     * Return the case sensitive setting for this string. Default is case insensitive (i.e. false).
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }


    /**
     * Compare a character to a character by offset, using the caseSensitive setting.
     *
     * @param index The offset to the character in this CoolString.
     * @param c     The character to compare against.
     */
    public boolean equalChars(int index, char c) {
        if (index >= count)
            return false;
        else if (caseSensitive)
            return buffer[index] == c;
        else
            return Character.toLowerCase(buffer[index]) == Character.toLowerCase(c);
    }


    /**
     * Compare to a char array.
     */
    public boolean equals(char[] c) {
        boolean same = c.length == count;
        int i = 0;
        while (same && i < count) {
            same = equalChars(i, c[i]);
            i++;
        }
        return same;
    }


    /**
     * Compare to anything by converting it to a string and then a char array.
     * This generates too much garbage though.
     */
    public boolean equals(Object o) {
        char c[] = o.toString().toCharArray();    // trash
        return equals(c);
    }


    /**
     * Is the substring present in this string?
     */
    public boolean contains(String s) {
        return indexOf(s) != -1;
    }


    /**
     * Does the string start with the substring?
     */
    public boolean startsWith(String s) {
        return indexOf(s) == 0;
    }


    /**
     * Does the string end with the substring
     */
    public boolean endsWith(String s) {
        return lastIndexOf(s) == count - s.length();
    }


    /**
     * Insert a string before the specified index.
     */
    public void insertAt(int index, String insertThis) {
        // create temporaries
        int iCount = insertThis.length();
        int newCount = count + iCount;
        char newBuffer[] = new char[ newCount ];
        // copy first part of original string
        System.arraycopy(buffer, 0, newBuffer, 0, index);
        // copy inserted string
        for (int i = 0; i < iCount; i++)
            newBuffer[index + i] = insertThis.charAt(i);
        // copy last part of original string
        for (int i = index; i < count; i++)
            newBuffer[iCount + i] = buffer[i];
        // save new string
        buffer = newBuffer;
        count = newCount;
    }


    /**
     * Append a string to the end of this string.
     */
    public void append(String appendThis) {
        insertAt(count, appendThis);
    }


    /**
     * Prepend a string in front of this string.
     */
    public void prepend(String prependThis) {
        insertAt(0, prependThis);
    }


    /**
     * Insert a string before first instance of a string.
     */
    public void insertBefore(String beforeThis, String insertThis) {
        int o = indexOf(beforeThis);
        if (o == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        insertAt(o, insertThis);
    }


    /**
     * Insert a string before last instance of a string.
     */
    public void insertBeforeLast(String beforeThis, String insertThis) {
        int o = lastIndexOf(beforeThis);
        if (o == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        insertAt(o, insertThis);
    }


    /**
     * Insert a string after first instance of a string.
     */
    public void insertAfter(String afterThis, String insertThis) {
        int o = indexOf(afterThis);
        if (o == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        insertAt(o + afterThis.length(), insertThis);
    }


    /**
     * Insert a string after last instance of a string.
     */
    public void insertAfterLast(String afterThis, String insertThis) {
        int o = lastIndexOf(afterThis);
        if (o == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        insertAt(o + afterThis.length(), insertThis);
    }


    /**
     * Return the substring starting at beginIndex and extending to the end of the string.
     */
    public String substring(int beginIndex) {
        return substring(beginIndex, count);
    }

    /**
     * Return the substring between the specified indicies.
     */
    public String substring(int beginIndex, int endIndex) {
        int newSize = endIndex - beginIndex;
        if (newSize < 0)
            newSize = 0;
        char newBuffer[] = new char[ newSize ];
        for (int i = 0; i < newSize; i++)
            newBuffer[i] = buffer[beginIndex + i];
        return String.valueOf(newBuffer);
    }

    /**
     * Return the text which appears before the specified String.
     * If the String does not appear, then this returns "".
     */
    public String before(String beforeThis) {
        int o = indexOf(beforeThis);
        if (o == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        return substring(0, o);
    }

    /**
     * Return the text which follows the specified String.
     * If the String does not appear, then this returns "".
     */
    public String after(String afterThis) {
        int o = indexOf(afterThis);
        if (o == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        return substring(o + afterThis.length(), count);
    }

    /**
     * Return the string between the two specified tags.
     * If either tag is not present then "" is returned.
     */
    public String between(String afterThis, String beforeThis) {
        int startingIndex = indexOf(afterThis);
        if (startingIndex == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        startingIndex += afterThis.length();
        int endingIndex = indexOf(beforeThis, startingIndex);
        if (endingIndex == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        return substring(startingIndex, endingIndex);
    }

    /**
     * Replace the first occurance of a string with another string.
     * Special case replacements of same length and other "in buffer" operations?
     */
    public void replaceFirst(String replaceThis, String withThis) {
        int o = indexOf(replaceThis);
        if (o == -1)
            throw new RuntimeException("String \"" + replaceThis + "\" not found");
        // create temporaries
        int rCount = replaceThis.length();
        int wCount = withThis.length();
        int nCount = count - rCount + wCount;
        char newBuffer[] = new char[nCount];
        char temp[] = withThis.toCharArray();    // trash
        // copy first part
        System.arraycopy(buffer, 0, newBuffer, 0, o);
        // copy withThis
        System.arraycopy(temp, 0, newBuffer, o, wCount);
        // copy last part
        for (int k = o + rCount; k < count; k++)
            newBuffer[k + wCount - rCount] = buffer[k];
        // save new string
        buffer = newBuffer;
        count = nCount;
    }

    /**
     * Replace the last occurance of a string with another string.
     */
    public void replaceLast(String replaceThis, String withThis) {
        int o = lastIndexOf(replaceThis);
        if (o == -1)
            throw new RuntimeException("String \"" + replaceThis + "\" not found");
        // create temporaries
        int rCount = replaceThis.length();
        int wCount = withThis.length();
        int nCount = count - rCount + wCount;
        char newBuffer[] = new char[nCount];
        char temp[] = withThis.toCharArray();    // trash
        // copy first part
        System.arraycopy(buffer, 0, newBuffer, 0, o);
        // copy withThis
        System.arraycopy(temp, 0, newBuffer, o, wCount);
        // copy last part
        for (int k = o + rCount; k < count; k++)
            newBuffer[k + wCount - rCount] = buffer[k];
        // save new string
        buffer = newBuffer;
        count = nCount;
    }

    /**
     * Replace all instances of a string with another string.
     */
    public void replaceAll(String replaceThis, String withThis) {
        int o;
        int startAt = 0;
        while ((o = indexOf(replaceThis, startAt)) != -1) {
            // create temporaries
            int rCount = replaceThis.length();
            int wCount = withThis.length();
            int nCount = count - rCount + wCount;
            char newBuffer[] = new char[nCount];
            char temp[] = withThis.toCharArray();    // trash
            // copy first part
            System.arraycopy(buffer, 0, newBuffer, 0, o);
            // copy withThis
            System.arraycopy(temp, 0, newBuffer, o, wCount);
            // copy last part
            for (int k = o + rCount; k < count; k++)
                newBuffer[k + wCount - rCount] = buffer[k];
            // set new starting point for next offset search
            startAt = o + wCount;
            // save new string
            buffer = newBuffer;
            count = nCount;
        }
    }

    /**
     * Replace the text between two strings with another string.
     */
    public void replaceBetween(String afterThis, String beforeThis, String withThis) {
        int startingIndex = indexOf(afterThis);
        if (startingIndex == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        startingIndex += afterThis.length();
        int endingIndex = indexOf(beforeThis, startingIndex);
        if (endingIndex == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        // create temporaries
        int rCount = endingIndex - startingIndex;
        int wCount = withThis.length();
        int nCount = count - rCount + wCount;
        char newBuffer[] = new char[nCount];
        char temp[] = withThis.toCharArray();    // trash
        // copy first part
        System.arraycopy(buffer, 0, newBuffer, 0, startingIndex);
        // copy withThis
        System.arraycopy(temp, 0, newBuffer, startingIndex, wCount);
        // copy last part
        for (int k = startingIndex + rCount; k < count; k++)
            newBuffer[k + wCount - rCount] = buffer[k];
        // save new string
        buffer = newBuffer;
        count = nCount;
    }


    /**
     * Delete a specified range of characters.
     */
    public void delete(int startingIndex, int endingIndex) {
        // examine error conditions
        if (startingIndex > count)
            throw new RuntimeException("Bad startingIndex specified.");
        if (endingIndex > count)
            throw new RuntimeException("Bad endingIndex specified.");
        // ignore negative ranges - don't throw
        if (startingIndex > endingIndex)
            return;
        // create temporaries
        int dCount = endingIndex - startingIndex + 1;
        char newBuffer[] = new char[count - dCount];
        // copy first part
        System.arraycopy(buffer, 0, newBuffer, 0, startingIndex);
        // copy last part
        for (int i = endingIndex + 1; i < count; i++)
            newBuffer[i - dCount] = buffer[i];
        // save new string
        buffer = newBuffer;
        count -= dCount;
    }


    /**
     * Delete the first occurance of a substring.
     */
    public void deleteFirst(String deleteThis) {
        replaceFirst(deleteThis, "");
    }


    /**
     * Delete the last occurance of the specified string.
     */
    public void deleteLast(String deleteThis) {
        replaceLast(deleteThis, "");
    }


    /**
     * Delete every occurance of the specified string.
     */
    public void deleteAll(String deleteThis) {
        replaceAll(deleteThis, "");
    }


    /**
     * Delete everything before the specified string.
     */
    public void deleteBefore(String beforeThis) {
        int o = indexOf(beforeThis);
        if (o == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        delete(0, o - 1);
    }


    /**
     * Delete everything after the specified string.
     */
    public void deleteAfter(String afterThis) {
        int o = indexOf(afterThis);
        if (o == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        delete(o + afterThis.length(), count - 1);
    }


    /**
     * Delete the string between two strings.
     */
    public void deleteBetween(String afterThis, String beforeThis) {
        int startingIndex = indexOf(afterThis);
        if (startingIndex == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        startingIndex += afterThis.length();
        int endingIndex = indexOf(beforeThis, startingIndex);
        if (endingIndex == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        delete(startingIndex, endingIndex - 1);
    }


    /**
     * Keep on the text before a string (or delete the string and everything after it).
     */
    public void keepBefore(String beforeThis) {
        int o = indexOf(beforeThis);
        if (o == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        delete(o, count - 1);
    }


    /**
     * Keep the text after a string (or delete the string and everything before it).
     */
    public void keepAfter(String afterThis) {
        int o = indexOf(afterThis);
        if (o == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        delete(0, o + afterThis.length() - 1);
    }


    /**
     * Keep only the text between two strings.
     */
    public void keepBetween(String afterThis, String beforeThis) {
        int startingIndex = indexOf(afterThis);
        if (startingIndex == -1)
            throw new RuntimeException("String \"" + afterThis + "\" not found");
        startingIndex += afterThis.length();
        int endingIndex = indexOf(beforeThis, startingIndex);
        if (endingIndex == -1)
            throw new RuntimeException("String \"" + beforeThis + "\" not found");
        // create temporaries
        int nCount = endingIndex - startingIndex;
        char newBuffer[] = new char[ nCount ];
        // copy characters which remain
        for (int i = startingIndex; i < endingIndex; i++)
            newBuffer[i - startingIndex] = buffer[i];
        // save new buffer
        count = nCount;
        buffer = newBuffer;
    }


    /**
     * Return the number of strings (tokens) seperated by the specified token.
     */
    public int countTokens(String token) {
        // count the number of tokens
        int tLength = token.length();
        int tCount = 0;
        int startAt = 0;
        while (true) {
            int o = indexOf(token, startAt);
            if (o == -1)
                break;
            tCount++;
            startAt = o + tLength;
        }
        return tCount + 1;
    }


    /**
     * Return an array of strings seperated by the specified token.
     * The token will not appear in the strings.
     */
    public String[] tokens(String token) {
        int tCount = countTokens(token);
        // create array for tokens
        String tokens[] = new String[tCount];
        // make a temporary CoolString to peel apart
        CoolString temp = new CoolString(this);
        // get all the substrings
        for (int j = 0; j < tCount; j++) {
            // the last token won't have a token terminator
            if (j == tCount - 1)
                tokens[j] = temp.toString();
            else {
                tokens[j] = temp.before(token);
                temp.keepAfter(token);
            }
        }
        return tokens;
    }


    /**
     * Return the string before the first token.
     */
    public String firstToken(String token) {
        return before(token);
    }


    /**
     * Return the string after the last token.
     */
    public String lastToken(String token) {
        int o = lastIndexOf(token);
        return substring(o + 1, count);
    }


    /**
     * Return the Nth string from a tokenized string. N is zero based.
     */
    public String NthToken(String token, int n) {
        String[] temp = tokens(token);
        if (n >= temp.length)
            throw new RuntimeException("Only " + temp.length + " tokens present (try a zero based index).");
        return temp[n];
    }


}
