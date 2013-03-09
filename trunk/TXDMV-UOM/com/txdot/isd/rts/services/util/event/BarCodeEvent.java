package com.txdot.isd.rts.services.util.event;

/*
 *
 * Template.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M. Abernethy	09/10/2001	Added Comments
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */

/**
 *
 * An event that has defined that a barcode has been scanned.  
 * It contains a data object where each field contains the 
 * corresponding value that was scanned.
 * 
 * <p>An object implementing the interface <code>BarCodeListener</code>
 * will be able to receive BarCodeEvents and will be spared all the 
 * details of reading in the barcode data and parsing it into the 
 * data object.
 * 
 * @version	5.2.3			06/21/2005
 * @author Michael Abernethy
 * <br>Creation Date:		09/05/2001 12:06:58
 */
public class BarCodeEvent
{
	/** 
	 * An graph of the data object containing the barcode information.
	 */
	private Object caBarcodeData;
	/**
	 * Creates a BarCodeEvent with the data object containing the 
	 * barcode information.
	 * 
	 * @param data the data object
	 */
	public BarCodeEvent(Object aaData)
	{
		caBarcodeData = aaData;
	}
	/**
	 * Returns the Data object containing the barcode information.
	 * 
	 * @return Object
	 */
	public Object getBarCodeData()
	{
		return caBarcodeData;
	}
}
