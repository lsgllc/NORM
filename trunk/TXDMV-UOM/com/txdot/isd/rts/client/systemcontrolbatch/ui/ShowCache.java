package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * ShowCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/24/2002	Use SystemProperty to determine how far 
 *							back to go.
 *							defect 4659
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3
 * Jeff S.		07/12/2005	Add string contants.
 * 							defect 7897 ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * An appliction that allows the user to see the contents of the 
 * transaction cache
 * 
 * @version	5.2.3			07/12/2005
 * @author	Michael Abernethy
 * <br>Creation Date: 		09/20/2001 10:45:40
 */
public class ShowCache
{
	/**
	 * 
	 * String Constants
	 */
	private static final String CACHE_READ_ERROR_MSG = 
		"There was an error reading the cache.  "
			+ "The data may be incomplete or incorrect.";
	/**
	 * ShowCache constructor.
	 */
	public ShowCache()
	{
		super();
		try
		{
			VCShowCache laController = new VCShowCache();
			laController.setMediator(this);
			RTSDate laToday = RTSDate.getCurrentDate();
			laToday =
				laToday.add(
					RTSDate.DATE,
					-SystemProperty.getPurgeTrans());
			Vector lvTrans =
				Transaction.readFromCache(laToday);
			laController.setView(lvTrans);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println(CACHE_READ_ERROR_MSG);
		}
	}
	/**
	 * The entry point to the SendCache application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		ShowCache laShwCache = new ShowCache();
	}
	/**
	 * Process the data
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(int aiModuleName,	int aiFunctionId, 
		Object aaData) throws RTSException
	{
		return null;
	}
}
