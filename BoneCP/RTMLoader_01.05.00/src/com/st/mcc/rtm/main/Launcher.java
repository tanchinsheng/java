package com.st.mcc.rtm.main;

import com.jolbox.bonecp.BoneCP;
import com.st.mcc.rtm.dao.manager.ConnectionManager;
import com.st.mcc.rtm.dao.manager.IEDBConnectionManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import com.st.mcc.rtm.manager.Cfg;
// import com.st.mcc.rtm.manager.StMailManager;
import com.st.mcc.rtm.util.FileUtil;
import com.st.mcc.rtm.util.exception.RTMException;

import static com.st.mcc.rtm.util.AppConstants.*;


/**
 * Main program for RTM Loader
 */
public class Launcher 
{
	static Logger _logger = Logger.getLogger(Launcher.class.getName());
	
	// Stop application message file
	private static final String FILE_STOP_APPLICATION = "stop_rtmloader.cmd";
	
	// Main working directory
	static String workdir = "";
	static String errordir = "";
	static String errorFTPdir = "";
	static String appVersion = "";
	
	// Trigger file extension
	static String triggerFileExt = "";
		
	// Active threads' list
	private static Vector<FilesLoader> activeThreads = new Vector<FilesLoader>();
	
	// Number of concurrent threads allowed at loading
	private static int threadCntLimit = 0;
	

	/**
	 * Main program for RTM Loader.
	 * @param args
	 */
	public static void main(String[] args) {
	    
		int result = RESULT_NO_ERROR;
		int i = 0;
		boolean continueNextLoad = false;	// Flag to continue another loading iteration if cannot complete in one show
        ConnectionManager.configureConnPool();
        IEDBConnectionManager.configureConnPool();
		try {
			// Read configurable parameters
			Cfg.init();
			
			appVersion = Cfg.getProperty("rtmloader_conf.appVersion");
			_logger.info("*****************************************");
			_logger.info("*** RTM LOADER VERSION " + appVersion + " STARTS ***");
			_logger.info("*****************************************");
			
			workdir = Cfg.getProperty("rtmloader_conf.dir.workdir");
			errordir = Cfg.getProperty("rtmloader_conf.dir.errordir");
			errorFTPdir = Cfg.getProperty("rtmloader_conf.dir.errordirFTP");
			triggerFileExt = "." + Cfg.getProperty("rtmloader_conf.file.triggerfileext");
											
			// Main loop timeout counter in seconds
			long timeoutCounter = 0;

			// RTM loading frequency in milliseconds
			long loadfreq = Long.parseLong(Cfg.getProperty("rtmloader_conf.time.loadfrequency"));
			if (loadfreq <= 0) 
			{
				_logger.error("RTM loading frequency is NOT positive integer: " + loadfreq);
				_logger.error("RTM loading frequency needs to be fixed before restarting application");
				return;
			}
			
			// Thread count's limit
			threadCntLimit = Integer.parseInt(Cfg.getProperty("rtmloader_conf.load.threadCountLimit"));
			if (threadCntLimit < 1) 
			{
				_logger.error("Thread count limit is less than 1: " + threadCntLimit);
				_logger.error("Thread count limit needs to be fixed before restarting application");
				return;
			}
			
			File[] triggerFiles = new File[0];
			
			File stopFile = new File(workdir + "/" + FILE_STOP_APPLICATION);
			if (stopFile.exists()) stopFile.delete();
			
			try 
			{
				while (true) 
				{
					// Check stop message
					if (stopFile.exists()) 
					{
						stopFile.delete();
						break;
					}
					 
                    // Check if need to check trigger file now
					if ((timeoutCounter != 0) && (!continueNextLoad)) {
						Thread.sleep(1000); // Sleep for one second
						
						// Initialize for next iteration
						timeoutCounter = (timeoutCounter + 1) % loadfreq;
						continue;
					
					} 
					else {
						if (!continueNextLoad) {
							_logger.info("~~~~~~~~~~ Wake up to work after config time " + loadfreq + " seconds...");
						} 
						else {
							_logger.info("~~~~~~~~~~ Continue next loading shot...");
						}
						
						// Initialize for loading
						result = 0;
					}
					
					// Check existence of working & error directory
					if (result == RESULT_NO_ERROR) {
						checkWorkDir();
						checkErrDir();
					}
					
                    // Search WMR trigger files in working directory
					if (result == RESULT_NO_ERROR) {
						_logger.info("Search TT files in working directory: " + workdir + "...");
						triggerFiles = FileUtil.searchFilesBySuffix(workdir, triggerFileExt);
						_logger.info("No. of TT files are found: "+ triggerFiles.length);
					}
					
					// Process current trigger files
					if (result == RESULT_NO_ERROR) 
					{					
						if (triggerFiles.length > threadCntLimit) {
							continueNextLoad = true;
						} 
						else {
							continueNextLoad = false;
						}
						
						for (i=0; (i < triggerFiles.length) && (i < threadCntLimit); i++) {
							processTriggerFile(triggerFiles[i]);
						}
					}
					
					/*
					 * Wait for all loader threads to complete
					 */
					for (i = 0; i < activeThreads.size(); i++) {
						((FilesLoader) activeThreads.get(i)).join();
					}
					activeThreads.clear();
					
					/*
					 * Post-processing
					 */
					
					/* 
					 * Sleep for one second even for the case of continuing loading
					 * to prevent resource exhaustion
					 */
					Thread.sleep(1000);
					if (! continueNextLoad) {
						timeoutCounter = (timeoutCounter + 1) % loadfreq;
					}
									
				}
			} /* Main while loop */
			catch ( OutOfMemoryError e )
			{
				_logger.error(e.toString());
			    // code to execute after the user closes or clicks OK in the dialog box.
			    // System.exit(0);
			}
			
		} 
		catch (Exception e) 
		{
			_logger.error("Error occurs in main process: " + e.toString());
			_logger.error("The application has been shut down. The application can be restarted after fixing the error.");
		}
        finally
        {
            ConnectionManager.shutdownConnPool();
            IEDBConnectionManager.shutdownConnPool();
        }
//		System.out.println("*************** RTM LOADER VERSION " + appVersion + " STOPS ***************");
		_logger.info("*** RTM LOADER VERSION " + appVersion + " STOPS ***");
	}
	
	
	/**
	 * Check working directory & create it if not existing.
	 * @return 0 if check passes; 1 if check fails
	 */
	private static int checkWorkDir() 
	{
		int result = RESULT_NO_ERROR;
		
		File workDirHandler = new File(workdir);
		
		if (! workDirHandler.exists()) {
			if (workDirHandler.mkdirs()) {
				result = RESULT_NO_ERROR;
			} 
			else {
				result = RESULT_ERROR;
			}
		}
		
		return result;
	}
	
	/**
	 * Check error directory & create it if not existing.
	 * @return 0 if check passes; 1 if check fails
	 */
	private static int checkErrDir() 
	{
		int result = RESULT_NO_ERROR;
		
		File errDirLoadingHandler = new File(errordir);
		
		if (! errDirLoadingHandler.exists()) 
		{
			if (errDirLoadingHandler.mkdirs()) 
			{
				result = RESULT_NO_ERROR;
			} 
			else 
			{
				result = RESULT_ERROR;
			}
		}
		
		File errDirFTPHandler = new File(errorFTPdir);
		
		if (! errDirFTPHandler.exists()) {
			if (errDirFTPHandler.mkdirs()) {
				result = RESULT_NO_ERROR;
			} 
			else {
				result = RESULT_ERROR;
			}
		}
		return result;
	}
	
	/**
	 * Process trigger file with the following steps:
	 * - Create working directory.
	 * - Move all loaded files to this directory.
	 * - Create process thread to handle.
	 * 
	 * @param triggerFile: triggering file 
	 */
	private static void processTriggerFile(File triggerFile) 
	{	
		_logger.info("Process trigger file " + triggerFile.getName() + "...");
		
		int result = RESULT_NO_ERROR;
		String loadWorkDir = "";
		RTMException patEx = new RTMException();

		try
		{
			// Create working directory
			if (result == RESULT_NO_ERROR) 
			{
				loadWorkDir = createLoadWorkDir(triggerFile);
				if (loadWorkDir.equals("")) 
				{
					result = RESULT_ERROR;
					patEx.setErrorCode(result);
					patEx.setErrorText("Creating working directory for loading trigger file " + triggerFile.getName() + " fails");
				}
			}
			
			// Move all loaded files to this directory
			if (result == RESULT_NO_ERROR) 
			{
				result = moveLoadFiles(triggerFile, loadWorkDir, patEx);
			}
			
			// *** Create process thread to start loading the tt file
			if (result == RESULT_NO_ERROR)
			{
				FilesLoader loader = new FilesLoader(loadWorkDir, loadWorkDir + "/" + triggerFile.getName());
				loader.start();
				activeThreads.add(loader);
			
			} 
			else
			{	// Move trigger file to error dir
				moveFiles2Error(triggerFile, loadWorkDir);
				
				// Send email to notify this error
				// StMailManager.emailRTMLoadError(triggerFile.getName(), patEx);
			}
		}
		catch (Exception e) 
		{
			_logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * Create working directory for storing loading files.
	 * This directory has its directory name to be same as
	 * the trigger file name excluding the file extension (eg. ".msg").
	 * 
	 * @param triggerFile: triggering file
	 * @return loading work directory's name if success; "" if failure. 
	 */
	private static String createLoadWorkDir(File triggerFile) 
	{	
		String dirName = workdir + "/" + FileUtil.suffixSubString(triggerFile.getName(), triggerFileExt);
		_logger.info("Create loading working directory " + dirName + "...");
		
		File dir = new File(dirName);
		
		if (! dir.exists()) { 
			if (! dir.mkdir()) {
				dirName = "";
				_logger.error("Creating loading working directory fails");
			}
		} else {
			_logger.info("Loading working directory already exists, just continue");
		}
		
		return dirName;
	
	}
	
	
	/**
	 * Move all loaded files (outlier, algorithm and geographic report) & trigger file to 
	 * loading working directory.
	 * - Even if any error occurs along the moving, the moving still continues to next files.
	 * Then error code will be returned at the end.
	 * @param triggerFile: triggering file
	 * @param loadWorkDir: loading working directory
	 * @param patEx: load error detail
	 * @return 0 if success; <>0 if failure
	 */
	private static int moveLoadFiles(File triggerFile, String loadWorkDir, RTMException patEx) 
	{	
		int result = RESULT_NO_ERROR;
		
		_logger.info("Move loading files to working directory " + loadWorkDir + "...");
		
		// Parse trigger file
		Properties props = new Properties();
		
		try 
		{
			FileInputStream fisTriggerFile = new FileInputStream(triggerFile);
			BufferedInputStream bisTriggerFile = new BufferedInputStream(fisTriggerFile);  //1.02.00: FingBugs
			props.load(bisTriggerFile);
			fisTriggerFile.close();
			bisTriggerFile.close();
		} 
		catch (Exception e) {
			result = RESULT_ERROR;
			patEx.reportError(result, "Trigger file loading error: " + e.toString(), _logger);
			return result;
		}
		
		// Move loading files
//		String reportFileName = "";
		
		//1.02.00: Unused
		// Outlier report file
//		if ( (reportFileName = props.getProperty(FilesLoader.TRIGGER_OUTLIER_REPORT)) != null ) {
//			_logger.info("Move file " + reportFileName + "...");
//			
//			File srcFile = new File(workdir + "/" + reportFileName);
//			File dstFile = new File(loadWorkDir + "/" + reportFileName);
//			
//			if (! srcFile.renameTo(dstFile)) {
//				result = RESULT_ERROR;
//				patEx.reportError(result, "Moving file " + reportFileName + " fails", _logger);
//			} 
//			else {
//				if (! dstFile.exists()) {
//					result = RESULT_ERROR;
//					patEx.reportError(result, "After moving, destination file " + loadWorkDir + "/" + reportFileName + " does not exist", _logger);					
//				}
//			}
//		}
		
		//1.02.00: Unused
		// Algorithm report file
//		if ( (reportFileName = props.getProperty(FilesLoader.TRIGGER_ALGORITHM_REPORT)) != null ) {
//			_logger.info("Move file " + reportFileName + "...");
//			
//			File srcFile = new File(workdir + "/" + reportFileName);
//			File dstFile = new File(loadWorkDir + "/" + reportFileName);
//			
//			if (! srcFile.renameTo(dstFile)) {
//				result = RESULT_ERROR;
//				patEx.reportError(result, "Moving file " + reportFileName + " fails", _logger);
//			} else {
//				if (! dstFile.exists()) {
//					result = RESULT_ERROR;
//					patEx.reportError(result, "After moving, destination file " + loadWorkDir + "/" + reportFileName + " does not exist", _logger);					
//				}
//			}
//		}
		
		//1.02.00: Unused
		// Geographic report file
//		if ( (reportFileName = props.getProperty(FilesLoader.TRIGGER_GEOGRAPHIC_REPORT)) != null ) {
//			_logger.info("Move file " + reportFileName + "...");
//			
//			File srcFile = new File(workdir + "/" + reportFileName);
//			File dstFile = new File(loadWorkDir + "/" + reportFileName);
//			
//			if (! srcFile.renameTo(dstFile)) {
//				result = RESULT_ERROR;
//				patEx.reportError(result, "Moving file " + reportFileName + " fails", _logger);
//			} 
//			else {
//				if (! dstFile.exists()) {
//					result = RESULT_ERROR;
//					patEx.reportError(result, "After moving, destination file " + loadWorkDir + "/" + reportFileName + " does not exist", _logger);
//				}
//			}
//		}
		
		//1.02.00: Unused
		// Stack pat report file
//		if ( (reportFileName = props.getProperty(FilesLoader.TRIGGER_STACK_REPORT)) != null ) {
//			_logger.info("Move file " + reportFileName + "...");
//			
//			File srcFile = new File(workdir + "/" + reportFileName);
//			File dstFile = new File(loadWorkDir + "/" + reportFileName);
//			
//			if (! srcFile.renameTo(dstFile)) {
//				result = RESULT_ERROR;
//				patEx.reportError(result, "Moving file " + reportFileName + " fails", _logger);
//			} 
//			else {
//				if (! dstFile.exists()) {
//					result = RESULT_ERROR;
//					patEx.reportError(result, "After moving, destination file " + loadWorkDir + "/" + reportFileName + " does not exist", _logger);
//				}
//			}
//		}
		
		// Move trigger file
		props = null;
		_logger.info("Move file " + triggerFile.getName() + "...");
		File dstFile = new File(loadWorkDir + "/" + triggerFile.getName());
		
		if (! triggerFile.renameTo(dstFile)) {
			result = RESULT_ERROR;
			patEx.reportError(result, "Moving file " + triggerFile.getName() + " fails", _logger);
		} 
		else {
			if (! dstFile.exists()) {
				result = RESULT_ERROR;
				patEx.reportError(result, "After moving, destination file " + loadWorkDir + "/" + triggerFile.getName() + " does not exist", _logger);
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * Move trigger file and/or report files to error dir.
	 * - If trigger file exists in main work dir, move it to error dir and stop.
	 * - Otherwise, move load work dir to error dir.
	 * @param triggerFile: full-path triggering file in work dir if existing 
	 * @param loadWorkDir: full-path loading working directory if existing
	 */
	private static void moveFiles2Error(File triggerFile, String loadWorkDirName) 
	{
		_logger.info("Move erroneous files to error dir...");
		
		if (triggerFile.exists()) 
		{
			FileUtil.renameFileOverwrite(triggerFile, errordir + "/" + triggerFile.getName());
		} 
		else 
		{	// trigger file does not exist
			File loadWorkDir = new File(loadWorkDirName);
			
			if (loadWorkDir.exists()) {
				// Zip directory
				String zipFileName = loadWorkDirName + ".zip";
				FileUtil.zipDir(loadWorkDirName, zipFileName);
				
				// Move to error dir
				File zipFile = new File(zipFileName);
				
				if (FileUtil.renameFileOverwrite(zipFile, errordir + "/" + zipFile.getName())) {
					FileUtil.deleteDir(loadWorkDirName);
				}				
			}
		}	
	}

	
}
