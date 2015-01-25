package it.txt.tpms.backend.results;

import org.w3c.dom.Element;
import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.GeneralStringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 11-ott-2006
 * Time: 11.11.51
 * This class is used to contain the information on file related to a lineset after a change ownership of a lineset
 */
public class LinesetModifiedFileData {
    /*
    Sample of data that can be given in input to constructor
    <FILE TP="BOTH" TYPE="text_file" EXTENSION="txt">/users/puglisi/tpms_test_last/basa/quattr_test/linea/new_file.txt</FILE>
    */
    /**
     * the following set of constants are used in order to parse an xml element like:
     * &lt;FILE TP="BOTH" TYPE="text_file" EXTENSION="txt"&gt;/users/puglisi/tpms_test_last/basa/quattr_test/linea/new_file.txt&lt;/FILE&gt;
     */
    public static final String FILE_ELEMENT_TAG = "FILE";
    public static final String TP_ATTRIBUTE_TAG = "TP";
    public static final String EXTENSION_ATTRIBUTE_TAG = "EXTENSION";
    public static final String TYPE_ATTRIBUTE_TAG = "TYPE";
    /**
     * this constant is used to identify those files that is been MODIFIED:
     * the file is present in both lineset lineset1 and lineset2
     */
    public static final String TP_MODIFIED_ATTRIBUTE_VALUE = "BOTH";
    /**
     * this constant is used to identify those files that is been ADDED
     * the file is present only in lineset1
     */
    public static final String TP_ADDED_ATTRIBUTE_VALUE = "BS2";
    /**
     * this constant is used to identify those files that is been DELETED (i.e. REMOVED)
     * the file is present in both lineset2 (i.e. is been deleted/removed from lineset1)
     */
    public static final String TP_DELETED_ATTRIBUTE_VALUE = "BS1";

    private String id = "";
    private String type = "";
    private String extension = "";
    private String filename = "";

    private boolean modified = false;
    private boolean deleted = false;
    private boolean added = false;

    public LinesetModifiedFileData(String filename, String type, String extension) {
        this.id = filename;
        this.filename = filename;
        this.type = type;
        this.extension = extension;
    }

    public LinesetModifiedFileData(String type, String extension, String filename, boolean modified, boolean deleted, boolean added) {
        this.id = filename;
        this.type = type;
        this.extension = extension;
        this.filename = filename;
        this.modified = modified;
        this.deleted = deleted;
        this.added = added;
    }

    /**
     * this constructor suppose to get in input an xml element like:
     *  &lt;FILE TP="BOTH" TYPE="text_file" EXTENSION="txt"&gt;/users/puglisi/tpms_test_last/basa/quattr_test/linea/new_file.txt&lt;/FILE&gt;
     * @param lsFileData
     */

    public LinesetModifiedFileData(Element lsFileData) {
        if (lsFileData != null) {
            this.type = lsFileData.getAttribute(TYPE_ATTRIBUTE_TAG);
            this.extension = lsFileData.getAttribute(EXTENSION_ATTRIBUTE_TAG);
            this.filename = XmlUtils.getVal(lsFileData);
            this.id = filename;
            String addedModfiedDeleted = lsFileData.getAttribute(TP_ATTRIBUTE_TAG);
            if ( !GeneralStringUtils.isEmptyString(addedModfiedDeleted) ) {
               if (addedModfiedDeleted.equals(TP_MODIFIED_ATTRIBUTE_VALUE)) {
                   this.added = false;
                   this.modified = true;
                   this.deleted = false;
               } else if (addedModfiedDeleted.equals(TP_DELETED_ATTRIBUTE_VALUE)){
                   this.added = false;
                   this.modified = false;
                   this.deleted = true;
               } else if (addedModfiedDeleted.equals(TP_ADDED_ATTRIBUTE_VALUE)){
                   this.added = true;
                   this.modified = false;
                   this.deleted = false;
               }
            }

        }
    }

    /**
     *
     * @return true if and only if the file is modified (the file is present in both lineset lineset1 and lineset2)
     */
    public boolean isModified() {
        return modified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isAdded() {
        return added;
    }

    /**
     * binary or text
     * @return the file type (binary or text)
     */

    public String getType() {
        return type;
    }

    /**
     * extension
     * @return extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Filename Path
     * @return filenamePath
     */
    public String getFilename() {
        return filename;
    }

    public String getId() {
        return id;
    }
}
