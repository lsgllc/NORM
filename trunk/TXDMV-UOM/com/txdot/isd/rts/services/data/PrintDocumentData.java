package com.txdot.isd.rts.services.data;

/*
 *
 * PrintDocumentData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	12/26/2010	add cbPermit, get/set methods
 * 							modify PrintDocumentData(boolean,boolean,  
 * 							 boolean, boolean, boolean, boolean, boolean) 
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */
/**
 * This class is used for calculating Misc Registration Fees.
 * 
 * @version	6.7.0		12/26/2010 
 * @author	Rakesh Duggirala
 * <br>Creation Date:		
 */

public class PrintDocumentData
{
	// boolean
	private boolean cbBarCode;
	private boolean cbFinal;
	private boolean cbFirstDuplicate;
	private boolean cbOriginal;
	private boolean cbPreliminary;
	private boolean cbSecondDuplicate;
	
	// defect 10700 
	private boolean cbPermit; 
	// defect 10700 

	/**
	 * PrintDocumentData constructor comment.
	 */
	public PrintDocumentData()
	{
		super();
	}
	/**
	 * PrintDocumentData Constructor
	 * 
	 * @param  abOriginal boolean
	 * @param  abFirstDuplicate boolean
	 * @param  abSecondDuplicate
	 * @param  abBarCode
	 * @param  abPreliminary
	 * @param  abFinal
	 * @param  abPermit
	 */
	public PrintDocumentData(
		boolean abOriginal,
		boolean abFirstDuplicate,
		boolean abSecondDuplicate,
		boolean abBarCode,
		boolean abPreliminary,
		boolean abFinal,
		// defect 10700 
		boolean abPermit)
		// end defect 10700 
	{
		this.cbOriginal = abOriginal;
		this.cbFirstDuplicate = abFirstDuplicate;
		this.cbSecondDuplicate = abSecondDuplicate;
		this.cbBarCode = abBarCode;
		this.cbPreliminary = abPreliminary;
		this.cbFinal = abFinal;
		// defect 10700 
		this.cbPermit = abPermit;
		// end defect 10700  
	}
	
	/**
	 * Return value of BarCode
	 * 
	 * @return boolean
	 */
	public boolean isBarCode()
	{
		return cbBarCode;
	}
	/**
	 * Return value of Final
	 * 
	 * @return boolean
	 */
	public boolean isFinal()
	{
		return cbFinal;
	}
	/**
	 * Return value of FirstDuplicate
	 * 
	 * @return boolean
	 */
	public boolean isFirstDuplicate()
	{
		return cbFirstDuplicate;
	}
	/**
	 * Return value of Original
	 * 
	 * @return boolean
	 */
	public boolean isOriginal()
	{
		return cbOriginal;
	}
	/**
	 * Return value of Preliminary
	 * 
	 * @return boolean
	 */
	public boolean isPreliminary()
	{
		return cbPreliminary;
	}
	/**
	 * Return value of SecondDuplicate
	 * 
	 * @return boolean
	 */
	public boolean isSecondDuplicate()
	{
		return cbSecondDuplicate;
	}
	/**
	 * Set value of BarCode
	 *
	 * @param abBarCode boolean
	 */
	public void setBarCode(boolean abBarCode)
	{
		cbBarCode = abBarCode;
	}
	/**
	 * Set value of Final
	 *
	 * @param abFinal boolean
	 */
	public void setFinal(boolean abFinal)
	{
		cbFinal = abFinal;
	}
	/**
	 * Set value of FirstDuplicate
	 *
	 * @param abFirstDuplicate boolean
	 */
	public void setFirstDuplicate(boolean abFirstDuplicate)
	{
		cbFirstDuplicate = abFirstDuplicate;
	}
	/**
	 * Set value of Original
	 * 
	 * @param aOriginal boolean
	 */
	public void setOriginal(boolean abOriginal)
	{
		cbOriginal = abOriginal;
	}
	/**
	 * Set value of Preliminary
	 *
	 * @param abPreliminary boolean
	 */
	public void setPreliminary(boolean abPreliminary)
	{
		cbPreliminary = abPreliminary;
	}
	/**
	 * Set value of SecondDuplicate
	 * 
	 * @param abSecondDuplicate boolean
	 */
	public void setSecondDuplicate(boolean abSecondDuplicate)
	{
		cbSecondDuplicate = abSecondDuplicate;
	}
	/**
	 * Return value of cbPermit
	 * 
	 * @return boolean 
	 */
	public boolean isPermit()
	{
		return cbPermit;
	}

	/**
	 * Set value of cbPermit
	 * 
	 * @param abPermit
	 */
	public void setPermit(boolean abPermit)
	{
		cbPermit = abPermit;
	}
}
