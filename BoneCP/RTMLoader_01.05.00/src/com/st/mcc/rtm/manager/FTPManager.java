package com.st.mcc.rtm.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * @author hdle
 *
 */
public class FTPManager 
{
	private static Logger _logger = Logger.getLogger(FTPManager.class.getName());
	
	/**
	 * Create FTPClient connection
	 * 
	 * @param server
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws SocketException
	 */
	public static FTPClient createFTPClientConn(String server, String username,
												String password) throws IOException, SocketException {
		
		_logger.info("Establish FTP connection to: " + server);
		
		FTPClient ftp = new FTPClient();

		int reply;
		ftp.connect(server);

		// After connection attempt, you should check the reply code to
		// verify success.
		reply = ftp.getReplyCode();

		if (!FTPReply.isPositiveCompletion(reply)) {
			closeFTPClientConn(ftp);
			ftp = null;
			_logger.error("Could not connect to server: " + server);
		}

		if (!ftp.login(username, password)) {
			ftp.logout();
			closeFTPClientConn(ftp);
			ftp = null;
			_logger.error("FTP authentication failed for user: " + username);
		}

		if (ftp != null) {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
		}
		
		if (_logger.isDebugEnabled())
			_logger.debug("Connected to " + server + "." + username);
		
		return ftp;
	
	}

	
	/**
	 * Close ftp collection
	 * 
	 * @param ftp
	 */
	public static void closeFTPClientConn(FTPClient ftp) {
		
		if (ftp != null && ftp.isConnected()) {
			try {
				ftp.disconnect();
				if (_logger.isDebugEnabled())
					_logger.debug("FTPClient " + ftp + " disconnected!");
			} catch (IOException f) {
				// do nothing
			}
		}
		
	}
	
	
	/**
	 * Test whether remote file/directory exist on ftp server
	 * @param ftp: FTPClient object already opened
	 * @param remoteFileName: remote file/dir name to check
	 * @return true if existing; false if not existing
	 */
	public static boolean isRemoteFileExist(FTPClient ftp, String remoteFileName) {

		boolean result = true;
		
		try {
			String[] fileList = ftp.listNames(remoteFileName);
			
			if (fileList != null) {
				result = true;
			} 
			else {
				result = false;
			}
		
		} 
		catch (Exception e) {
			_logger.error("Checking file/dir existence fails: " + e.toString());
			result = false;
		}

		return result;
		
	}


	/** 1.02.00: Bugs fixed for checking 2nd level subdir
	 * Create remote directory if it is not existing 
	 * @param ftp: FTPClient object already opened
	 * @param parentPath: Parent directory to check for the existence of the sub-directory
	 * @param subDirName: Sub-directory name to check
	 * @return true if success; false if failure
	 */
	public static boolean createDirIfNotExist(FTPClient ftp, String parentPath, String subDirName) 
	{		
		synchronized (FTPManager.class) 
		{
			String ftpDirName;
			String fullpath = parentPath + "/" + subDirName;

			try 
			{
				_logger.debug("Check fullpath: " + fullpath);
                _logger.debug("FTP current working dir: "+ ftp.printWorkingDirectory());
				
    			// Directory already exists, do nothing
    			if (isRemoteFileExist(ftp, fullpath)) return true;
                
				if (ftp.changeWorkingDirectory(fullpath)) return true;
                
                FTPFile[] fileList = ftp.listDirectories(parentPath);

                // Check if dir exists in the FTP server
				for (int i=0; i < fileList.length; i++)
				{
					ftpDirName = fileList[i].getName();
					if (ftpDirName.compareToIgnoreCase(subDirName) == 0) 
					{	
						return true; 
					} 
				}
						
				// Create new directory
				if (ftp.makeDirectory(fullpath)) 
				{
					return true;
				} 
				else 
				{
					return false;
				}
			} 
			catch (Exception e) 
			{
				_logger.error("Checking file/dir existence fails: " + ftp.getReplyString() + ": " + e.toString());
				return false;
			}
		}
	}
	
	
	/**
	 * Upload file to server and overwrite if remote file is already existing
	 * @param ftp: FTPClient object already opened
	 * @param localFile: local file name
	 * @param remoteFile: remote file name
	 * @return true if success; false if failure
	 */
	public static boolean storeFileOverwrite(FTPClient ftp, String localName, String remoteName) {
	
		_logger.info("Uploading file: " + localName + " to " + remoteName + "...");
		
		FileInputStream input = null;
		String reply;
		
		synchronized (FTPManager.class) 
		{
			try 
			{
				// Remote file already exists, delete it
				if (isRemoteFileExist(ftp, remoteName)) {
					if (! ftp.deleteFile(remoteName)) {
						reply = ftp.getReplyString();
						_logger.error("Deleting remote file fails: " + reply + ": " + remoteName);
						return false;
					}
				}
				
				// Upload local file
				input = new FileInputStream(localName);

				ftp.enterLocalActiveMode();  //1.02.00C
				ftp.enterLocalPassiveMode();  //1.02.00C
				
				if (! ftp.storeFile(remoteName, input)) {
					reply = ftp.getReplyString();
					_logger.error("Storing remote file fails: " + reply + ": " + remoteName);
					return false;
				}
				
			} 
			catch (Exception e) 
			{
				_logger.error("Uploading file fails: "  + e.toString() + ": " + localName);
				return false;	
			} 
			finally 
			{
				if (input != null) 
				{
					try {
						input.close();
					} catch (IOException e2) {}
				}
			}
		}
		
		return true;
	}

	
	/** 1.02.00: REMOVE
	 * Create remote directory if it is not existing 
	 * @param ftp: FTPClient object already opened
	 * @param remoteFileName: remote dir name to create
	 * @return true if success; false if failure
	 */
//	public static boolean createDirIfNotExist(FTPClient ftp, String dirName) 
//	{
//		
//		synchronized (FTPManager.class) 
//		{
//			try 
//			{
//				// Directory already exists, do nothing
//				if (isRemoteFileExist(ftp, dirName)) {
//					return true;
//				}
//
//				if (ftp.changeWorkingDirectory(dirName)) 
//				{ 
//					return true; 
//				}
//							
//				// Create new directory
//				if (ftp.makeDirectory(dirName)) 
//				{
//					return true;
//				} 
//				else 
//				{
//					return false;
//				}
//			} 
//			catch (Exception e) 
//			{
//				_logger.error("Checking file/dir existence fails: " + ftp.getReplyString() + ": " + e.toString());
//				return false;
//			}
//		}
//	}
	
	
	/**
	 * Main method for testing purpose
	 */
//	public static void main(String args[]) 
//	{
//	}

}
