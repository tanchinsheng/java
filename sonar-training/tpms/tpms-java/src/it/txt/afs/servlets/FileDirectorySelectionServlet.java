package it.txt.afs.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.utils.FileSystemNavigation;
import it.txt.general.utils.GeneralStringUtils;
import tpms.TpmsException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.util.Enumeration;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-feb-2006
 * Time: 12.00.32
 * To change this template use File | Settings | File Templates.
 */
public class FileDirectorySelectionServlet extends AfsGeneralServlet {

    //the request parameter with this name MUST contain the relative url where the user will be redirected afeter the file or directory selection.
    public static final String RESULT_PAGE_PARAMETER_NAME = "resultPage";
    //the request parameter with this name should contain the initial direcory where the fs navigation start
    public static final String INITIAL_DIRECTORY_PARAMETER_NAME = "initDir";
    //the request parameter with this name will be the considered as a root: the user can't navigate trought this directory.
    // if null INITIAL_DIRECTORY_PARAMETER_NAME will be considered as the relative root.
    //the INITIAL_DIRECTORY_PARAMETER_NAME MUST be equeal or a subdirectory of this.
    public static final String RELATIVE_ROOT_DIR_PARAMETER_NAME = "realtiveRootDir";
    //the request parameter with this name is setted to FILE_SELECTION_PARAMETER_VALUE the user can select only files
    //if equal to DIRECTORY_SELECTION_PARAMETER_VALUE the user can select only directoy, otherwise the user can select both files and directory
    public static final String FILE_SELECTION_PARAMETER_NAME = "fileSelection";
    //the request parameter with this name is setted to LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE the user can select only files or dir under RELATIVE_ROOT_DIR_PARAMETER_NAME
    //otherwise can navigate and select the entire file system
    public static final String LIMIT_NAVIGATION_UNDER_RELATIVE_ROOT_PARAMETER_NAME = "relativeToRoot";


    public static final String LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE = "limitToRoot";
    public static final String NO_LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE = "noLimitToRoot";

    public static final String FILE_SELECTION_PARAMETER_VALUE = "fileOnly";
    public static final String DIRECTORY_SELECTION_PARAMETER_VALUE = "directoryOnly";


    public static final String PARENT_LINK_VALUE = "..";


    public static final String NAVIGATION_OBJECT_REQUEST_ATTRIBUTE_NAME = "fileSystemNavigationObject";
    public static final String USER_SELECTION_CONTROL_NAME = "selectedElement";


    //action field value...
    public static final String SELECT_ENTRY_ACTION_VALUE = "selectEntry";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = GeneralStringUtils.isEmptyString((String) AfsServletUtils.getRequestAttributeParameter(request, AfsServletUtils.ACTION_FIELD_NAME)) ? "" : (String) AfsServletUtils.getRequestAttributeParameter(request, AfsServletUtils.ACTION_FIELD_NAME);

        String resultPage = (String) AfsServletUtils.getRequestAttributeParameter(request, RESULT_PAGE_PARAMETER_NAME);
        if (GeneralStringUtils.isEmptyString(resultPage)) {
            TpmsException e = new TpmsException("General error", "The result page is null or empty");
            manageError(e, request, response);
        }

        String initialDirectory = (String) AfsServletUtils.getRequestAttributeParameter(request, INITIAL_DIRECTORY_PARAMETER_NAME);
        debugLog("FileDirectorySelectionServlet :: doPost : initialDirectory = " + initialDirectory);
        if (GeneralStringUtils.isEmptyString(resultPage)) {
            TpmsException e = new TpmsException("General error", "The INITIAL_DIRECTORY_PARAMETER_NAME page is null or empty");
            manageError(e, request, response);
        }

        String relativeRootDirectory = (String) AfsServletUtils.getRequestAttributeParameter(request, RELATIVE_ROOT_DIR_PARAMETER_NAME);

        if (GeneralStringUtils.isEmptyString(relativeRootDirectory)) relativeRootDirectory = initialDirectory;
        debugLog("FileDirectorySelectionServlet :: doPost : relativeRootDirectory = " + relativeRootDirectory);
        String tmpLimitNavigationToRoot = (String) AfsServletUtils.getRequestAttributeParameter(request, LIMIT_NAVIGATION_UNDER_RELATIVE_ROOT_PARAMETER_NAME);
        boolean limitNavigationUnderRootDir = true;

        if (tmpLimitNavigationToRoot.equals(NO_LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE)) {
            limitNavigationUnderRootDir = false;
        }


        String canonicalInitialDirectory = null;
        try {
            canonicalInitialDirectory = new File(initialDirectory).getCanonicalPath();
        } catch (IOException e) {
            TpmsException e1 = new TpmsException("Can not access " + initialDirectory + " directory<br>Please check rights on the whole path", "File System navigation");
            manageError(e1, request, response);
            return;
        }
        String canonicalRelativeRootDirectory = null;
        try {
            canonicalRelativeRootDirectory = new File(relativeRootDirectory).getCanonicalPath();
        } catch (IOException e) {
            TpmsException e1 = new TpmsException("Can not access " + relativeRootDirectory + " directory<br>Please check rights on the whole path", "File System navigation");
            manageError(e1, request, response);
            return;
        }

        if (limitNavigationUnderRootDir && !canonicalInitialDirectory.startsWith(canonicalRelativeRootDirectory)) {
            TpmsException e = new TpmsException("You can't navigate trought " + relativeRootDirectory, "");
            manageError(e, request, response);
        }



        String tmpFileSelection = (String) AfsServletUtils.getRequestAttributeParameter(request, FILE_SELECTION_PARAMETER_NAME);
        Boolean onlyFileSelection = null;
        if (!GeneralStringUtils.isEmptyString(tmpFileSelection)) {
            if (tmpFileSelection.equals(FILE_SELECTION_PARAMETER_VALUE)) {
                onlyFileSelection = Boolean.TRUE;
            } else {
                onlyFileSelection = Boolean.FALSE;
            }
        }
        String nextPage;

        if (action.equals(SELECT_ENTRY_ACTION_VALUE)) {
            //the user has selected an entry ... it's time to return the selection to the caller...
            //Clean the \\
            String selectedPath = request.getParameter(USER_SELECTION_CONTROL_NAME);
            //selectedPath = GeneralStringUtils.replace(selectedPath, "\\\\", "\\");
            request.setAttribute(USER_SELECTION_CONTROL_NAME, selectedPath);
            nextPage = resultPage;
        } else {
            //the user has clicked on one directory and want to navigate inside of that directory
            FileSystemNavigation fileSystemNavigation;
            try {

                fileSystemNavigation = new FileSystemNavigation(resultPage, initialDirectory, relativeRootDirectory, limitNavigationUnderRootDir,
                        onlyFileSelection, tpmsConfiguration.getTxtFilesExtList());
            } catch (TpmsException e) {
                manageError(e, request, response);
                return;
            }

            request.setAttribute(NAVIGATION_OBJECT_REQUEST_ATTRIBUTE_NAME, fileSystemNavigation);
            nextPage = this.getServletConfig().getInitParameter("fileSystemNavigation");
        }
        manageRedirect(nextPage, request, response);
    }


    public static String printAllRequestsAttributesAndParameters(HttpServletRequest request) {
        Enumeration attributeNamesList = request.getAttributeNames();
        String name;
        Object obj;
        String result = "";
        if (attributeNamesList != null) {
            while (attributeNamesList.hasMoreElements()) {
                name = (String) attributeNamesList.nextElement();
                if (!name.equals(AfsServletUtils.ACTION_FIELD_NAME) && !name.equals(NAVIGATION_OBJECT_REQUEST_ATTRIBUTE_NAME) &&
                        !name.equals(USER_SELECTION_CONTROL_NAME)) {
                    obj = request.getAttribute(name);
                    if (obj != null && obj instanceof String)
                        result = (result + AfsServletUtils.printHiddenField(name, (String) obj));
                }
            }
        }

        Enumeration parameterNamesList = request.getParameterNames();
        String value;
        if (parameterNamesList != null) {
            while (parameterNamesList.hasMoreElements()) {
                name = (String) parameterNamesList.nextElement();
                if (!name.equals(AfsServletUtils.ACTION_FIELD_NAME) && !name.equals(NAVIGATION_OBJECT_REQUEST_ATTRIBUTE_NAME) &&
                        !name.equals(USER_SELECTION_CONTROL_NAME)) {
                    value = request.getParameter(name);
                    result = (result + AfsServletUtils.printHiddenField(name, value));
                }
            }
        }
        return result;
    }


}
