package com.txdot.isd.rts.services.util;
/*
 * FtpFile.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		03/24/2009	Created Class.
 * 							defect 9986 Ver Defect_POS_E
 * ---------------------------------------------------------------------
 */

/**
 * This class is used to hold FTP properties used to connect
 * to a remote computer and transfer files. 
 *
 * @version	Defect_POS_E	03/24/2009
 * @author	Bob Brown
 * <br>Creation Date:		03/19/2009 13:48:30
 */
public class FtpFile
	{
		private final String caFTPFile;
		private final String caFTPHost;
		private final String caFTPPassword;
		private final String caFTPUser;
		private final String caLocalFile;

		/**
	 * FtpFile represents a file transfer object.
	 *
	 * @param asFTPHost String
	 * @param asFTPUser String
	 * @param asFTPPassword String
	 * @param asFTPFile String
	 * @param asLocalFile String
	 */
	public FtpFile(
		String asFTPHost,
		String asFTPUser,
		String asFTPPassword,
		String asFTPFile,
		String asLocalFile)
	{
		super();
		this.caFTPHost = asFTPHost;
		this.caFTPUser = asFTPUser;
		this.caFTPPassword = asFTPPassword;
		this.caFTPFile = asFTPFile;
		this.caLocalFile = asLocalFile;
	}

	/**
	 * @return Returns the caFTPFile.
	 */
	public String getFtpFile()
	{
		return this.caFTPFile;
	}

	/**
	 * @return Returns the caFTPHost.
	 */
	public String getFtpHost()
	{
		return this.caFTPHost;
	}

	/**
	 * @return Returns the caFTPPassword.
	 */
	public String getFtpPassword()
	{
		return this.caFTPPassword;
	}

	/**
	 * @return Returns the caFTPUser.
	 */
	public String getFtpUser()
	{
		return this.caFTPUser;
	}

	/**
	 * @return Returns the caLocalFile.
	 */
	public String getLocalFile()
	{
		return this.caLocalFile;
	}
}