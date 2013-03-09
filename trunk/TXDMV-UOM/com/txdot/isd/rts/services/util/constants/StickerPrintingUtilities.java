package com.txdot.isd.rts.services.util.constants;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.cache.*;

/*
 * 
 * StickerPrintingUtilities.java
 * 
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2005	Import; New Class
 * 							Ver 5.2.0
 * --------------------------------------------------------------------- 
 */
/**
 * Utility Method to determine if sticker is printable.
 * 
 * @version	5.2.0		03/21/2004
 * @author	Michael Abernethy
 * <p>Creation date:	08/12/02 15:40:58  
 */ 

public class StickerPrintingUtilities
{
	/**
	 * Method to evaluate isStickerPrintable
	 * @return boolean
	 * @param invData com.txdot.isd.rts.services.data.ProcessInventoryData
	 */
	public static boolean isStickerPrintable(ProcessInventoryData invData)
	{
		ItemCodesData itemCodesData =
			ItemCodesCache.getItmCd(invData.getItmCd());
		if (itemCodesData.getPrintableIndi() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Method to evaluate isStickerPrintable
	 * @return boolean
	 * @param itemCode java.lang.String
	 */
	public static boolean isStickerPrintable(String aItmCd)
	{
		ItemCodesData itemCodesData = ItemCodesCache.getItmCd(aItmCd);
		if (itemCodesData.getPrintableIndi() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
