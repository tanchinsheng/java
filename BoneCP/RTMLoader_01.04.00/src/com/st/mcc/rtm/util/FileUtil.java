package com.st.mcc.rtm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.st.mcc.rtm.manager.Cfg;

import static com.st.mcc.rtm.util.AppConstants.*;


public class FileUtil 
{
	static Logger _logger = Logger.getLogger(FileUtil.class.getName());
	
    /**
     * Method declaration searches one or more files in directory that       
     * have given suffix in their file names.      
     * @param dir searched directory 
     * @param suffix file name's suffix
     * @return the file names obtained by research      
     */
    public static File[] searchFilesBySuffix(String dir, String suffix) {
        
    	File  descFiles = null;
    	File[] listFile = null;
    	
    	_logger.info("Search directory " + dir + " for files with suffix " + suffix + "...");

        try {
        	descFiles = new File(dir);
        	FilenameFilter filter = new SuffixFilter(suffix);
            listFile = descFiles.listFiles(filter);
            // V2.02 changed by Tony on Apr-08-2009
            Arrays.sort(listFile);
        
        } 
        catch (Exception e) {
            _logger.error("Search files in directory " + dir + " by suffix " + suffix + " has error: " + e.toString());
        }

        /* Post-process */
        if (listFile == null) listFile = new File[0];
        return listFile;
        
    }

    
    /**
     * This method returns suffix sub string of original string orginStr which is before ending string endStr.       
     * Eg. suffixSubString("abc.msg", ".msg") returns "abc".
     * @param originStr - original string
     * @param endStr - ending string
     * @return suffix sub string if endStr is found at the end; "" if endStr is not found at the end.      
     */
    public static String suffixSubString(String originStr, String endStr) {
    	
    	if (! originStr.endsWith(endStr)) return "";
    
    	String retStr = "";
    	
    	int lastIndex = originStr.lastIndexOf(endStr);
    	retStr = originStr.substring(0, lastIndex);
    	
    	return retStr;
    	
    }
    
    /** 
     * Zip the contents of the directory, and save it in the zipfile
     * @param dir: directory to be zipped
     * @param zipfile: output zip file
     * @return 0 if success; <>0 if failure
     */
    public static int zipDir(String dir, String zipfile) {

    	int result = RESULT_NO_ERROR;
    	
    	try {
    		// Check that the directory is a directory, and get its contents
    		File d = new File(dir);
    		if (!d.isDirectory()) {
    			throw new IllegalArgumentException("Not a directory: " + dir);
    		}
    		
    		String[] entries = d.list();
    		byte[] buffer = new byte[4096]; // Create a buffer for copying
    		int bytesRead;
    		
    		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
    		
    		for (int i = 0; i < entries.length; i++) {
    			File f = new File(d, entries[i]);
    			if (f.isDirectory()) {
    				continue;	//Ignore directory
    			}
    			
    			FileInputStream in = new FileInputStream(f);	// Stream to read file
    			ZipEntry entry = new ZipEntry(d.getName() + "/" + f.getName());	// Make a ZipEntry
    			out.putNextEntry(entry); // Store entry
    			while ((bytesRead = in.read(buffer)) != -1)
    				out.write(buffer, 0, bytesRead);
    			in.close(); 
    			out.closeEntry();
    		}
    		
    		out.close();
    	} 
    	catch (Exception e) 
    	{
    		_logger.error("Directory zipping fails: " + e.toString());
    		result = RESULT_ERROR;
    	}
    	
    	return result;
    }
    
    /**
     * Delete directory and its content.
     * - Assumption: input directory only contains files BUT NOT any sub-directory.
     * @param dirName
     * @return 0 if success; <>0 if failure
     */
    public static int deleteDir(String dirName) {
    	
    	int result = RESULT_NO_ERROR;
    	int i = 0;
    	
    	// Checking
    	File dir = new File(dirName);
    	if (! dir.isDirectory()) {
    		result = RESULT_ERROR;
    		_logger.error(dirName + " is NOT a directory");
    		return result;
    	}
    	
    	// Delete directory content
    	if (result == RESULT_NO_ERROR) {
    		File[] allFiles = dir.listFiles();
    		
    		for (i=0; i<allFiles.length; i++) {
    			if (! allFiles[i].delete()) {
    				result = RESULT_ERROR;
    				_logger.error("Delete file " + allFiles[i].getName() + " fails");
    				break;
    			}
    		}
    	}
    	
    	// Delete directory itself
    	if (result == RESULT_NO_ERROR) {
			if (! dir.delete()) {
				result = RESULT_ERROR;
				_logger.error("Delete directory " + dir.getName() + " fails");
			}    		
    	}
    	
    	return result;

    }
    
    
    /**
     * Rename current file to new name and overwrite existing file if any 
     * @param f: file to be moved
     * @param newFileName: new file name to move to
     * @return true if success; false if failure
     */
    public static boolean renameFileOverwrite(File f, String newFileName) {
		
		File dstFile = new File(newFileName);
		
		if (dstFile.exists()) {
			_logger.warn("Overwriting existing file " + dstFile.getName());
			if (! dstFile.delete()) {
				_logger.error("Deleting existing file " + dstFile.getName() + " fails");
				return false;
			}
		}
		
		if (! f.renameTo(dstFile)) {
			_logger.error("Moving file " + f.getName() + " fails");
			return false;
		} else {
			if (! dstFile.exists()) {
				_logger.error("After moving, destination file " + dstFile.getName() + " does not exist");
				return false;
			}
		}				
    	return true;  	
    }


    /** 1.01.01B
     * Put the TT file back to work dir for reloading
     * @param workDir: Working dir
     */
    public static void revertFile2WorkDir(String srcWorkDir) 
    {
    	try
    	{
    		String srcFilename;
    		String reloadFilename; 
    		String mainWorkDir = Cfg.getProperty("rtmloader_conf.dir.workdir");
    		
    		File oldFilDest = new File(srcWorkDir + "." + Cfg.getProperty("rtmloader_conf.file.triggerfileext"));
    		File filDest;
    		File filSrc;
    		
    		srcFilename = oldFilDest.getName();
    		
    		//1.02.00: If the TT file is reloaded "NUM_OF_TT_RELOAD" number of times, 
    		//         move the TT file to ERROR folder
    		
    		//1.02.00: Check no. of "#" occurrence in the filename 1st few chars.
    		String chkFilename = StringUtils.left(srcFilename, NUM_OF_TT_RELOAD + 1);
    		int reloadCnt = StringUtils.countMatches(chkFilename, RELOAD_TT_PREFIX);
    		
    		if (reloadCnt >= NUM_OF_TT_RELOAD)
    		{
    			String errorDir = Cfg.getProperty("rtmloader_conf.dir.errordir");
    			filDest = new File(errorDir + "/" + srcFilename);
    			_logger.info("Reload file hits its limit: " + srcFilename);
    			_logger.debug("Reload count: " + reloadCnt);
    		}
    		else 
    		{	
    			reloadFilename = RELOAD_TT_PREFIX + srcFilename;
    		    filDest = new File(mainWorkDir + "/" + reloadFilename);
    		}
		    
    		filSrc = new File(srcWorkDir + "/" + srcFilename);
    		
    		//1.02.00: Delete destination file if it exists
    		if (filDest.exists()) filDest.delete();
    		
    		FileUtils.moveFile(filSrc, filDest);
    		_logger.debug("File is moved to: " + filDest.getPath());
    		FileUtil.deleteDir(srcWorkDir);
    	}
    	catch (Exception ex) 
    	{
			_logger.error(ex.getMessage());
		}
	}
    
    
    /** 1.01.01B
     * Check against the error msg if the file need to be reloaded
     * @param errMsg: Error message
     * @return The result code whether the file need to reload
     */
    public static int reloadFile(String errMsg)
    {
    	int result = RESULT_ERROR;
    	
    	if (errMsg.length() > 0)
		{
			_logger.debug("errMsg: " + errMsg);
			if(errMsg.contains(ERR_CONSTRAINT) 
			|| errMsg.contains(ERR_DEADLOCK)
			|| errMsg.contains(ERR_CLOSE_CONN))
			{
				//iv-10200- add condition for reload
				if(!errMsg.contains(ERR_PARENT_KEY)){
					_logger.info("File send for reload: " + errMsg);
					result = RESULT_FILE_RELOAD;
				}
			}
		}
        return result;
    }
}



/**
 * Filter on the end of a file 
 *
 *
 * @author DN
 * @version %I%, %G%
 */
class SuffixFilter implements FilenameFilter
{
	private String  suffix;
	
	/**
	 * Constructor declaration
	 *
	 *
	 * @param ext
	 */
	public SuffixFilter(String aSuffix)
	{
		suffix = aSuffix.toUpperCase();
	}
	
	/**
	 * Method declaration
	 *
	 *
	 * @param dir
	 * @param name
	 *
	 * @return
	 */
	public boolean accept(File dir, String name)
	{
		return name.toUpperCase().endsWith(suffix);
	}
	
}
