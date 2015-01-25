package it.txt.general.utils;

import it.txt.afs.AfsCommonClass;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.Collections;
import java.io.File;
import java.io.IOException;


import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-feb-2006
 * Time: 13.07.33
 */
public class FileSystemNavigation extends AfsCommonClass {
    /**
     * This inner class is used due to alphabetically order the directories and files to be shown
     * There is a little strange situation that happens when this code runs under unix:
     * Let's suppose that we have the following directories (or files): AUTO, AUTo, AUto, Auto, auto, BASTONE, Bastone, bastone
     * Probably the user expects that those directories will be presente in the order above... but under a unix environment the order
     * will be the following one:
     * AUTO, AUTo, AUto, Auto, BASTONE, Bastone, auto, bastone
     * (the same result that a user get if he execute the ls command from the machine...)
     * This problem does not happen because you it is not case sensitive so can't have more that one file/dir with the same name.
     */
    private class MyAlphabelticalSort implements Comparator {
        public int compare(Object file1, Object file2) {
            int result = -2;
            char [] s1Lower;
            char [] s2Lower;

            if (file1 != null && file2 != null) {
                String s1 = (((File) file1).getName()).toLowerCase();
                String s2 = (((File) file2).getName()).toLowerCase();
                result = new AlphabeticalSort().compare(s1, s2);
            }
            return result;
        }
    }


    public static final String DIRECTORY_ICON = "/img/folder_icon.gif";


    private String resultPage = null;
    private String initialDirectory = null;
    private String currentDirectory = null;
    private String relativeRootDirectory = null;
    private boolean limitNavigationUnderRootDir = true;
    private Boolean onlyFileSelection = null;
    private Vector selectableFileExtensions = null;

    private Vector subDirectoriesList = new Vector();
    private Vector subFilesList = new Vector();

    private MyAlphabelticalSort alphabelticalSort = new MyAlphabelticalSort();


    public FileSystemNavigation(String resultPage, String initialDirectory, String relativeRootDirectory, boolean limitNavigationUnderRootDir,
                                Boolean onlyFileSelection, String selectableFileExtensions) throws TpmsException {

        File initialDirectoryFile;
        try {
            initialDirectoryFile = new File(initialDirectory);
            this.initialDirectory = initialDirectoryFile .getCanonicalPath();
        } catch (IOException e) {
            throw new TpmsException(initialDirectory + " isn't a valid directory,or the system can not access it, please check rights on the whole path!", "initialDirectory <!--FileSystemNavigation :: constructor -->");
        }

        File relativeRootDirectoryFile;
        try {
            relativeRootDirectoryFile = new File(relativeRootDirectory);
            this.relativeRootDirectory = relativeRootDirectoryFile.getCanonicalPath();
            if (!initialDirectoryFile.exists()) {
                this.initialDirectory = relativeRootDirectoryFile.getCanonicalPath();
            }
        } catch (IOException e) {
            throw new TpmsException(relativeRootDirectory + "  isn't a valid directory,or the system can not access it, please check rights on the whole path!", "relativeRootDirectory <!--FileSystemNavigation :: constructor -->");
        }

        this.resultPage = resultPage;
        setCurrentDirectory(this.initialDirectory);
        this.limitNavigationUnderRootDir = limitNavigationUnderRootDir;
        this.onlyFileSelection = onlyFileSelection;
        this.selectableFileExtensions = getFileExtensionVector(selectableFileExtensions);
    }

    private static Vector getFileExtensionVector(String lst) {
        Vector v = new Vector();
        StringTokenizer stringTokenizer = new StringTokenizer(lst, ",", false);
        String oneExtension;
        while (stringTokenizer.hasMoreElements()) {
            oneExtension = ((String) stringTokenizer.nextElement()).trim().toLowerCase();
            v.addElement(oneExtension);
        }
        return v;
    }


    public boolean showDirectorySelection() {
        if (onlyFileSelection == null) {
            return true;
        } else if (!onlyFileSelection.booleanValue()) {
            return true;
        }
        return false;
    }

    public boolean showFileSelection() {
        if (onlyFileSelection == null) {
            return true;
        } else if (onlyFileSelection.booleanValue()) {
            return true;
        }
        return false;
    }


    public boolean showParentLink() throws TpmsException {
        if (!limitNavigationUnderRootDir) {
            return true;
        } else {
            //todo manca la verifica che la dir corrente sia la root di sistema... in tal caso devo ritornare false
            String canonicalCurrentDirectory;
            try {
                canonicalCurrentDirectory = new File(currentDirectory).getCanonicalPath();
            } catch (IOException e) {
                throw new TpmsException("FileSystemNavigation :: showParentLink " + currentDirectory + " isn't a valid directory", "currentDirectory<!--FileSystemNavigation :: showParentLink-->");
            }
            String canonicalRelativeRootDirectory;
            try {

                canonicalRelativeRootDirectory = new File(relativeRootDirectory).getCanonicalPath();
            } catch (IOException e) {
                throw new TpmsException("FileSystemNavigation :: showParentLink " + relativeRootDirectory + " isn't a valid directory", "relativeRootDirectory<!--FileSystemNavigation :: showParentLink-->");
            }

            if (canonicalCurrentDirectory.equals(canonicalRelativeRootDirectory)) {
                return false;
            } else {
                if (!currentDirectory.equals(relativeRootDirectory) && currentDirectory.startsWith(relativeRootDirectory)) {
                    return true;
                } else {
                    throw new TpmsException("You are not allowed to navigate this part of file system");
                }
            }
        }

    }


    public void setCurrentDirectory(String currentDirectory) throws TpmsException {
        if (GeneralStringUtils.isEmptyString(currentDirectory)) {
            throw new TpmsException("The given directory does not exists <!--message to user uno-->", "FileSystemNavigation :: setCurrentDirectory");
        }

        File currDirFile = new File(currentDirectory);

        if (!currDirFile.exists()) {
            throw new TpmsException("The given directory (" + currentDirectory + ") does not exists <!--message to user due-->", "FileSystemNavigation :: setCurrentDirectory");
        }
        if (!currDirFile.canRead()){
            throw new TpmsException("Can not access the given directory (" + currentDirectory + ")<!--message to user tre--><br>Check read permissions", "FileSystemNavigation :: setCurrentDirectory");
        }
        if (currDirFile.isFile()) {
            currDirFile = new File(currDirFile.getParent());
        }

        File[] subFiles = currDirFile.listFiles();
        File f;
        for (int i = 0; (subFiles != null && i < subFiles.length); i++) {
            f = subFiles[i];
            if (f.isDirectory()) {
                subDirectoriesList.add(f);
            } else {
                subFilesList.add(f);
            }
        }

        Collections.sort(subDirectoriesList, alphabelticalSort);
        Collections.sort(subFilesList, alphabelticalSort);

        this.currentDirectory = currentDirectory;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public Vector getSubDirectoriesList() {
        return subDirectoriesList;
    }

    public Vector getSubFilesList() {
        return subFilesList;
    }

    public String getResultPage() {
        return resultPage;
    }

    public String getInitialDirectory() {
        return initialDirectory;
    }

    public String getRelativeRootDirectory() {
        return relativeRootDirectory;
    }

    public boolean isLimitNavigationUnderRootDir() {
        return limitNavigationUnderRootDir;
    }

    public Boolean getOnlyFileSelection() {
        return onlyFileSelection;
    }

    public Vector getSelectableFileExtensions() {
        return selectableFileExtensions;
    }

}
