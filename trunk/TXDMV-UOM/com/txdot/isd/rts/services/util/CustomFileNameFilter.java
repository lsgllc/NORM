package com.txdot.isd.rts.services.util;

import java.io.File;
import java.io.FilenameFilter;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * CustomFileNameFilter.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/02/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * Used to filter out just the files that match the criteria.
 * This can also be used to return files that don't match the criteria.
 *
 * @version	Broadcast Message	04/02/2007
 * @author	Jeff S.
 * <br>Creation Date:			03/24/2006 10:34:00
 */

public class CustomFileNameFilter implements FilenameFilter
{
	private boolean cbCompare = true;
	private String csFilterParam = CommonConstant.STR_SPACE_EMPTY;

	/**
	 * MessagesClientBusiness.java Constructor
	 * 
	 * @param asFilterParam String
	 */
	public CustomFileNameFilter(String asFilterParam)
	{
		super();
		this.csFilterParam = asFilterParam;
	}

	/**
	 * MessagesClientBusiness.java Constructor
	 * 
	 * @param asFilterParam String
	 * @param abCompare boolean
	 */
	public CustomFileNameFilter(
		String asFilterParam,
		boolean abCompare)
	{
		super();
		this.csFilterParam = asFilterParam;
		this.cbCompare = abCompare;
	}

	/**
	 * Override method that returns if we are going to accept the file
	 * or not.
	 * 
	 * @param aadir File
	 * @param asname String
	 */
	public boolean accept(File aadir, String asname)
	{
		if (asname.indexOf(csFilterParam) > -1)
		{
			return cbCompare;
		}
		else
		{
			return !cbCompare;
		}
	}
}
