package com.st.mcc.rtm.main;

import java.io.File;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import com.st.mcc.rtm.dao.process.ProcessRegistration;
import com.st.mcc.rtm.manager.Cfg;
import com.st.mcc.rtm.manager.FTPManager;
import com.st.mcc.rtm.parser.TTReportParser;
import com.st.mcc.rtm.util.FileUtil;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.util.exception.RTMException;

import static com.st.mcc.rtm.util.AppConstants.*;

/**
 * Streetwise files loader running in multi-threading
 * 
 */
public class FilesLoader extends Thread 
{

	private static Logger _logger = Logger.getLogger(FilesLoader.class.getName());
	
	/* Streetwise parameter names in trigger file */
//	public static final String TRIGGER_OUTLIER_REPORT = "Outlier_report";
//	public static final String TRIGGER_ALGORITHM_REPORT = "Algorithm_report";
//	public static final String TRIGGER_GEOGRAPHIC_REPORT = "Geographic_report";
//	public static final String TRIGGER_STACK_REPORT = "sPat_report";

//	public static boolean blnStack = false;

	/* Working directory for this files loading */
	private String workdir = "";

	/* Trigger file name (absolute filename) */
	private String ttFileName = "";

	/**
	 * Constructor with working directory and trigger filename.
	 * @param dir: working directory
	 * @param filename: trigger filename
	 */
	public FilesLoader(String dir, String filename) 
	{
		super();
		workdir = dir;
		ttFileName = filename;
	}


	/**
	 * Thread execution method
	 */
	public void run() 
	{
		_logger.debug("START FILES LOADING IN WORKING DIRECTORY: " + workdir + "...");

 	    // Initialization
		int result = RESULT_NO_ERROR;
		BeanRTMHeader rtmHeader = new BeanRTMHeader();
		RTMException rtmEx = new RTMException();

	    // Parse report files
		do
		{
			//1.01.01B: Change the flow and include TT file reloading and discard.
			result = parseReportFiles(rtmHeader, rtmEx);
			if (result == RESULT_ERROR) break;
		    
			if (rtmHeader.getLoadMethod().compareTo(LOAD_NO) == 0)
		    {
				// Delete original directory
			    FileUtil.deleteDir(workdir);
			    _logger.info("No loading: Discard TT file.");
			    result = RESULT_NO_ERROR;
		        break;
		    }

			if (result == RESULT_FILE_RELOAD)
			{
				FileUtil.revertFile2WorkDir(workdir);
		        break;
			}
			
			// Post-process report files
			result = insertReport2DB(rtmHeader, rtmEx);
			if (result == RESULT_ERROR) break;
			if (result == RESULT_FILE_RELOAD)
			{
				FileUtil.revertFile2WorkDir(workdir);
		        break;
			}
			
			result = archiveReportFiles(rtmHeader, rtmEx);
			if (result == RESULT_NO_ERROR) _logger.info("TT file loading succeeds.");
			
			break;
		} while(false);
			
		if (result == RESULT_ERROR) 
		{
			_logger.error("TT file loading fails.");
	
			// Move erroneous files to error dir		
			_logger.info("Move erroneous files to ERROR dir...");
	
			// Zip workdir
			String zipFileName = workdir + ".zip";
			FileUtil.zipDir(workdir, zipFileName);
	
			// Move to error dir
			File zipFile = new File(zipFileName);
	
			if (FileUtil.renameFileOverwrite(zipFile, Cfg.getProperty("rtmloader_conf.dir.errordir") + "/" + zipFile.getName())) {
				// Delete TT file work dir
				FileUtil.deleteDir(workdir);
		    }
	
			// Send email to notify this error
			//StMailManager.emailRTMLoadError(triggerFileName, rtmEx);			
		}
	}

	
	/**
	 * Parse tt result files: TT Header, TT TouchDown, TT Bin String
	 * @param rtmHeader: registration bean to fill
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 */
	public int parseReportFiles(BeanRTMHeader rtmHeader, RTMException rtmEx) 
	{
		// Initialization
		int result = RESULT_NO_ERROR;
		String fileName	= ttFileName;
		TTReportParser rtmParser = null;
		
		try
		{
			rtmParser = new TTReportParser(fileName, rtmHeader, rtmEx);
			result = rtmParser.parse();
		}
        catch (Exception ex) 
        {
        	_logger.error(ex.getMessage());
        }
		finally
		{
			rtmParser = null;
		}
        
		return result;
	}

	
	/**
	 * Insert result files info to database
	 * @param srtmHeader: registration bean to be inserted
	 * @param rtmEx: exception returned with error detail if any
	 * @return 0 if insertion succeeds; !=0 if insertion fails 
	 */
	public int insertReport2DB(BeanRTMHeader rtmHeader, RTMException rtmEx) 
	{
		_logger.info("Insert result files info. to database...");

		// Initialization
		int result = RESULT_NO_ERROR;

		try	
		{
			ProcessRegistration processReg = new ProcessRegistration();
			processReg.insert(rtmHeader);
		}
		catch (Exception ex) 
		{
			result = FileUtil.reloadFile(ex.getMessage());
			if (result == RESULT_ERROR) rtmEx.reportError(result, "DB loading: " + ex.getMessage(), _logger);	
		}

		return result;
	}

	
	/**
	 * Zip and archive report files
	 * @param rtmHeader
	 * @param rtmEx
	 * @return 0 if success; <> 0 if failure
	 */
	public int archiveReportFiles(BeanRTMHeader rtmHeader, RTMException rtmEx)
	{
		_logger.debug("Zip and archive report files...");

		// Initialization
		int result = RESULT_NO_ERROR;
		String zipFileName = workdir + ".zip";

		try 
		{
			// Zip directory
			if (result == RESULT_NO_ERROR) FileUtil.zipDir(workdir, zipFileName);
	
			// Delete original directory
			if (result == RESULT_NO_ERROR) result = FileUtil.deleteDir(workdir);
	
			// FTP zip file to archive repository
			if (result == RESULT_NO_ERROR) result = ftpUploadReportFiles(rtmHeader, zipFileName, rtmEx);
	
			// Delete zip file
			if (result == RESULT_NO_ERROR) {
				File zipFile = new File(zipFileName);
				if (! zipFile.delete()) {
					result = RESULT_ERROR;
					rtmEx.reportError(result, "Delete zip TT file fails: " + zipFileName, _logger);
				}
			}
	
			if (result != RESULT_NO_ERROR) {
				if (rtmEx.getErrorCode() == 0) {
					rtmEx.setErrorCode(result);
					rtmEx.setErrorText("Archive TT files in working dir fails: " + workdir);
				}
			}
		}
		catch (Exception ex) 
		{
			result = RESULT_ERROR;
			_logger.error(ex.getMessage());
		}
			
		return result;
//		if (result == RESULT_NO_ERROR)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
	}


	/**
	 * FTP upload zip file to central storage 
	 * @param rtmHeader: pat registration
	 * @param zipFileName: zip file name to upload
	 * @param rtmEx: RTM exception
	 * @return 0 if success; <>0 if failure
	 */
	private static int ftpUploadReportFiles(BeanRTMHeader rtmHeader, String zipFileName, RTMException rtmEx) 
	{
		_logger.debug("FTP upload zipped TT file: " + zipFileName);

		int result = RESULT_NO_ERROR;
		FTPClient ftp = null;

		String server = Cfg.getProperty("rtmloader_conf.ftp.centralStorageServer");
		String username = Cfg.getProperty("rtmloader_conf.ftp.centralStorageUsername");
		String password = Cfg.getProperty("rtmloader_conf.ftp.centralStoragePassword");
		String storageDirName = Cfg.getProperty("rtmloader_conf.ftp.centralStorageDirectory");

		String processDateStr = rtmHeader.getStartTime();
		String lvl1DirName = "";
		String lvl2DirName = "";
		String lvl3DirName = "";
		String subDirName = "";  //1.02.00

		synchronized (FilesLoader.class) 
		{
			try {
				// FTP connection
				ftp = FTPManager.createFTPClientConn(server, username, password);
				if (ftp == null) 
				{
					result = RESULT_ERROR;
					rtmEx.reportError(result, "FTP fail to connect by user: " + username, _logger);
				}
				
				// Create storage directory if needed. Eg. "yyyymm/dd/recipe_id"
				if (result == RESULT_NO_ERROR) 
				{ 		
					subDirName = processDateStr.substring(0, 6);
					lvl1DirName = storageDirName + "/" + subDirName;
					if (! FTPManager.createDirIfNotExist(ftp, storageDirName, subDirName))
					{
						result = RESULT_ERROR;
						rtmEx.reportError(result, "Fail create remote directory: " + lvl1DirName, _logger);
					} 
					else 
					{
						subDirName = processDateStr.substring(6, 8);
						lvl2DirName = lvl1DirName + "/" + subDirName;
                       
						if (! FTPManager.createDirIfNotExist(ftp, lvl1DirName, subDirName)) 
						{
							result = RESULT_ERROR;
							rtmEx.reportError(result, "Fail create remote directory: " + lvl2DirName, _logger);
						} 
						else 
						{
							subDirName = rtmHeader.getLotId();
							lvl3DirName = lvl2DirName + "/" +  subDirName;
							if (! FTPManager.createDirIfNotExist(ftp, lvl2DirName, subDirName)) 
							{
								result = RESULT_ERROR;
								rtmEx.reportError(result, "Fail create remote directory: " + lvl3DirName, _logger);
							}
						}
					}
				}

				// Upload zip file to central storage
				if (result == RESULT_NO_ERROR) 
				{
					File zipFile = new File(zipFileName);
					String remoteFileName = lvl3DirName + "/" + zipFile.getName();
									
					if (! FTPManager.storeFileOverwrite(ftp, zipFileName, remoteFileName)) 
					{
						result = RESULT_ERROR;
						rtmEx.reportError(result, "Fail upload zip file to remote dir: " + remoteFileName, _logger);
					}
					else
					{
						_logger.info("[DONE]: Zipped TT file uploaded: " + zipFileName);
					}
				}
			} 
			catch (Exception e) 
			{
				result = RESULT_ERROR;
				rtmEx.reportError(result, "FTP uploading zipped file fails: " + e.toString(), _logger);
			} 
			finally 
			{
				FTPManager.closeFTPClientConn(ftp);
			}
		}

		return result;
	}
	
}


