package it.txt.tpms.backend.results;

import it.txt.tpms.backend.results.lists.LinesetModifiedFilesList;
import it.txt.tpms.lineset.Lineset;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 11-ott-2006
 * Time: 10.47.43
 * This class will is thinked as a container for Get lineset ownership result (i.e. contains the data of the resulting xml from the call)
 */
public class GetLinesetOwnershipResult {
    /**
     * the following list of values are the ones that may be used for result attribute
     */
    public static final int OK_RESULT = 0;
    public static final int KO_RESULT = 1;
    public static final int WARNING_RESULT = 2;
    public static final int DEFAULT_RESULT = KO_RESULT;

    /**
     * the folowing label are the ones that are associated to any possible value of result attribute
     */
    private static final String OK_RESULT_LABEL = "OK";
    private static final String KO_RESULT_LABEL = "KO";
    private static final String WARNING_RESULT_LABEL = "WARNING";



    private int result = DEFAULT_RESULT;
    private String message = "";

    private Lineset lineset1 = null;
    private Lineset lineset2 = null;

    private HashMap modificationsSummary = new HashMap();

    private final String NEW_NOT_REFERENCED_FILES_KEY = "new_files_not_referenced";
    private final String NEW_REFERENCED_FILES_KEY = "new_files_referenced";
    private final String TOTAL_NOT_REFERENCED_FILES_KEY = "total_files_not_referenced";
    private final String NOT_MODIFIED_FILES_KEY = "files_not_modified";
    private final String MODIFIED_FILES_KEY = "files_modified";
    private final String REMOVED_FILES_KEY = "files_removed";

   /**
    * Sample of summary content:
    * <SUMM>
    *  <NB_NEW>0</NB_NEW> <!-sono i file NUOVI presenti sul filesystem MA NON referenziati in nessun xfer-->
    *  <NB_NEW_REFBYXFER>1</NB_NEW_REFBYXFER> <!--sono i file NUOVI presenti sul filesystem e referenziati in nessun xfer-->
    *  <!--la totalità dei file nuovi è NB_NEW + NB_NEW_REFBYXFER-->
    *  <NB_OUTOFXFER>0</NB_OUTOFXFER><!-- è il numero di file presenti sul filesystem e NON referenziati negli xfer -->
    *  <NB_UNCHANGED>12</NB_UNCHANGED><!-- è il numero di file NON modificati -->
    *  <NB_MODIFIED>1</NB_MODIFIED><!--è il numero di file modficati -->
    *  <NB_REMOVED>2</NB_REMOVED><!-- è il numero di file rimossi -->
    * </SUMM>
    */


    /**
     * this list will contain only those file that are present in lineset1 and lineset2 but there are some changes
     */
    private LinesetModifiedFilesList modifiedFilesList = new LinesetModifiedFilesList();
    /**
     * this list will contain only those file that are NOT present in lineset1
     */
    private LinesetModifiedFilesList deletedFilesList = new LinesetModifiedFilesList();
    /**
     * this list will contain only those file that are present ONLY in lineset1 (i.e. NOT lineset2)
     */
    private LinesetModifiedFilesList addedFilesList = new LinesetModifiedFilesList();

    public GetLinesetOwnershipResult(String message, int result, Lineset lineset1, Lineset lineset2,
                                     long newFilesNotReferencedCount, long newFilesRefedencedCount, long totalFilesNotReferencedCount,
                                     long filesNotModifiedCount, long filesModifiedCount, long filesRemovedCount){
        this.message = message;
        this.result = result;
        this.lineset1 = lineset1;
        this.lineset2 = lineset2;
        modificationsSummary.put(NEW_NOT_REFERENCED_FILES_KEY, new Long (newFilesNotReferencedCount));
        modificationsSummary.put(NEW_REFERENCED_FILES_KEY, new Long (newFilesRefedencedCount));
        modificationsSummary.put(TOTAL_NOT_REFERENCED_FILES_KEY, new Long (totalFilesNotReferencedCount));
        modificationsSummary.put(NOT_MODIFIED_FILES_KEY, new Long (filesNotModifiedCount));
        modificationsSummary.put(MODIFIED_FILES_KEY, new Long (filesModifiedCount));
        modificationsSummary.put(REMOVED_FILES_KEY, new Long (filesRemovedCount));
    }

    public GetLinesetOwnershipResult(String message, int result, Lineset lineset1, Lineset lineset2){
        this.message = message;
        this.result = result;
        this.lineset1 = lineset1;
        this.lineset2 = lineset2;
    }



    /******************************************INTERNAL USEFULL METHODS************************************************************/


    /******************************************SUMMARY DATA GETTERS************************************************************/
    /**
     *
     * @return total number o newly created files
     */
    public long getTotalNewFilesCount(){
        return  ((Long) modificationsSummary.get(NEW_NOT_REFERENCED_FILES_KEY)).longValue() + ((Long)modificationsSummary.get(NEW_REFERENCED_FILES_KEY)).longValue();
    }

    public long getNewNotReferencedFilesCount(){
        return  ((Long) modificationsSummary.get(NEW_NOT_REFERENCED_FILES_KEY)).longValue();
    }

    public long getNewReferencedFilesCount(){
        return  ((Long) modificationsSummary.get(NEW_REFERENCED_FILES_KEY)).longValue();
    }

    public long getTotalNotReferencedFilesCount() {
        return  ((Long) modificationsSummary.get(TOTAL_NOT_REFERENCED_FILES_KEY)).longValue();
    }

    public long getNotModifiedFilesCount() {
        return ((Long) modificationsSummary.get(NOT_MODIFIED_FILES_KEY)).longValue();
    }

    public long getModifiedFilesCount() {
        return ((Long) modificationsSummary.get(MODIFIED_FILES_KEY)).longValue();
    }

    public long getRemovedFilesCount(){
        return ((Long) modificationsSummary.get(REMOVED_FILES_KEY)).longValue();
    }


    /*********************************VISUALIZATION METHODS************************************/
    /**
     *
     * @return the label of result to show to the user
     */
    public String getResultLabel(){
        if (result == OK_RESULT) {
            return OK_RESULT_LABEL;
        } else if (result == KO_RESULT) {
            return KO_RESULT_LABEL;
        } else if (result == WARNING_RESULT) {
            return WARNING_RESULT_LABEL;
        } else {
            return "";
        }
    }


    /**
     * @return true if and only if details on files changed are present.
     */
    public boolean isFileChangesDataAvailable(){
        return (modifiedFilesList.size() > 0 || deletedFilesList.size() > 0 || addedFilesList.size() > 0);
    }

    /**
     * @return true if and only if summary data on changed are present.
     */
    public boolean isSummaryDataAvailable() {
        return (modificationsSummary.size() > 0);
    }

    //*********************************SETTERS*************************************************/


    //*********************************GETTERS*************************************************/

    public Lineset getLineset2() {
        return lineset2;
    }

    public Lineset getLineset1() {
        return lineset1;
    }

    public String getMessage() {
        return message;
    }

    public int getResult() {
        return result;
    }

    public LinesetModifiedFilesList getModifiedFilesList() {
        return modifiedFilesList;
    }

    public LinesetModifiedFilesList getDeletedFilesList() {
        return deletedFilesList;
    }

    public LinesetModifiedFilesList getAddedFilesList() {
        return addedFilesList;
    }


    public boolean addFile(LinesetModifiedFileData modifiedFile) {
        if (modifiedFile.isAdded()) {
            return this.addAddedFile(modifiedFile);
        } else if (modifiedFile.isDeleted()){
            return this.addDeletedFile(modifiedFile);
        } else if (modifiedFile.isModified()) {
            return this.addModifiedFile(modifiedFile);
        } else {
            return false;
        }
    }


    public boolean addModifiedFile(LinesetModifiedFileData modifiedFile) {
        if (modifiedFile.isModified()) {
            int addResult = modifiedFilesList.addFile(modifiedFile);
            return addResult == LinesetModifiedFilesList.OK_RESULT;
        } else {
            return false;
        }
    }

    public boolean addAddedFile(LinesetModifiedFileData addedFile) {
        if (addedFile.isAdded()) {
            int addResult = addedFilesList.addFile(addedFile);
            return addResult == LinesetModifiedFilesList.OK_RESULT;
        } else {
            return false;
        }
    }

    public boolean addDeletedFile(LinesetModifiedFileData deletedFile) {
        if (deletedFile.isDeleted()) {
            int addResult = deletedFilesList.addFile(deletedFile);
            return addResult == LinesetModifiedFilesList.OK_RESULT;
        } else {
            return false;
        }
    }

    public boolean packageFileCanBeDeleted() {
        return (result == OK_RESULT);
    }



    public boolean isOkResult() {
        return (result == OK_RESULT);
    }

    public boolean isWarningResult() {
        return (result == WARNING_RESULT);
    }

    public boolean isKoResult() {
        return (result == KO_RESULT);
    }
}
