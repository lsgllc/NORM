package com.txdot.isd.rts.services.data;

/*
 *
 * TransactionControl.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/11/2004	Add boolean cbAutoEndTrans
 *							modify constructor to add initialization of
 *							 AutoEndTrans
 *							add getAutoEndTrans(), isAutoEndTrans()
 *							Defect 6721; Ver 5.1.5 Fix 2
 * K Harrell	03/20/2004	JavaDoc Cleanup
 * 							Ver 5.2.0 
 * Ray Rowehl	02/18/2005	Move to services.data
 * 							code cleanup.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------
 */

/**
* TransactionControl defines the behavior of the following based on Transcd
* <ul>
* 		<li>Building a payment</li>
* 		<li>Printing a receipt</li>
* 		<li>Inserting a MvFuncTrans Record</li>
* 		<li>Is Customer or Backoffice transaction</li>
* 		<li>Event Type</li>
* 		<li>Automatically End Transaction   (defect 6721) </li>    
* </ul>
* <P> Used in Transaction
* 
* @version	5.2.3		04/21/2005
* @author	Nancy Ting
* <br>Creation Date:	09/26/2001 16:17:12
*/
public class TransactionControl
{
	
	// boolean
	// defect 6721 
	protected boolean cbAutoEndTrans;
	// end defect 6721
	protected boolean cbBuildMVFuncTrans; 
	protected boolean cbBuildPayment;
	protected boolean cbPrintReceipt;
	
	// int 
	protected int ciCustomer;
	
	// String 
	protected String csEventType;
	protected String csTransCd;
 
	/**
	 * TransactionControl constructor
	 */
	public TransactionControl()
	{
		super();
	}
	/**
	 * TransactionControl constructor
	 * Creation date: (9/26/2001 4:23:16 PM)
	 * @param asTransCd String transaction code
	 * @param abBuildPayment boolean Flag to indicate whether payment is built
	 * @param abPrintReceipt boolean Flag to indicate whether receipt should be printed
	 * @param abBuildMVFuncTrans boolean Flag to indicate whether MVFuncTrans should record should be inserted
	 * @param aiCustomer int defines whether it is a customer-based or backoffice transaction
	 * @param asEventType String Event type
	 * @param abAutoEndTrans boolean Flag to indicate whether transaction should be automatically ended
	 *          without user intervention
	 */
	public TransactionControl(
		String asTransCd,
		boolean abBuildPayment,
		boolean abPrintReceipt,
		boolean abBuildMVFuncTrans,
		int aiCustomer,
		String asEventType,
		boolean abAutoEndTrans)
	{
		csTransCd = asTransCd;
		cbBuildPayment = abBuildPayment;
		cbPrintReceipt = abPrintReceipt;
		cbBuildMVFuncTrans = abBuildMVFuncTrans;
		ciCustomer = aiCustomer;
		csEventType = asEventType;
		// Defect 6721 
		cbAutoEndTrans = abAutoEndTrans;
		// End Defect 6721 
	}
	/**
	 * Get Customer
	 *  
	 * @return int
	 */
	public int getCustomer()
	{
		return ciCustomer;
	}
	/**
	 * Get Event Type
	 *  
	 * @return String
	 */
	public String getEventType()
	{
		return csEventType;
	}
	/**
	 * Get Transcode
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Check if the transaction should be automatically completed, i.e. the
	 *		<ul>
	 *      <li> RTS_TRANS_HDR.transtimestmp should be updated
	 *		<li> Associated Inventory should be deleted
	 *		<li> RTS_TRANS_PAYMENT record(s) should be inserted, if required
	 *		</ul>
	 *  
	 * @return boolean
	 */
	public boolean isAutoEndTrans()
	{
		return cbAutoEndTrans;
	}
	/**
	 * Check if MVFuncTrans record should be inserted
	 * 
	 * @return boolean
	 */
	public boolean isBuildMVFuncTrans()
	{
		return cbBuildMVFuncTrans;
	}
	/**
	 * Checks if payment needs to be built
	 * 
	 * @return boolean
	 */
	public boolean isBuildPayment()
	{
		return cbBuildPayment;
	}
	/**
	 * Checks if receipt should be generated
	 * 
	 * @return boolean
	 */
	public boolean isPrintReceipt()
	{
		return cbPrintReceipt;
	}
	/**
	 * Set AutoEndTrans variable
	 *  
	 * @param abNewAutoEndTrans boolean
	 */
	public void setAutoEndTrans(boolean abNewAutoEndTrans)
	{
		cbAutoEndTrans = abNewAutoEndTrans;
	}
	/**
	 * Set buildMvFuncTrans variable
	 * 
	 * @param abNewBuildMVFuncTrans boolean
	 */
	public void setBuildMVFuncTrans(boolean abNewBuildMVFuncTrans)
	{
		cbBuildMVFuncTrans = abNewBuildMVFuncTrans;
	}
	/**
	 * Set Build payment variable
	 * 
	 * @param abNewBuildPayment boolean
	 */
	public void setBuildPayment(boolean abNewBuildPayment)
	{
		cbBuildPayment = abNewBuildPayment;
	}
	/**
	 * Set Customer variable
	 * 
	 * @param newCustomer int
	 */
	public void setCustomer(int aiNewCustomer)
	{
		ciCustomer = aiNewCustomer;
	}
	/**
	 * Set event type
	 * 
	 * @param lsNewEventType String
	 */
	public void setEventType(String lsNewEventType)
	{
		csEventType = lsNewEventType;
	}
	/**
	 * Set print receipt variable
	 * 
	 * @param abNewPrintReceipt boolean
	 */
	public void setPrintReceipt(boolean abNewPrintReceipt)
	{
		cbPrintReceipt = abNewPrintReceipt;
	}
	/**
	 * Set transcode
	 * 
	 * @param asNewTransCd String 
	 */
	public void setTransCd(String asNewTransCd)
	{
		csTransCd = asNewTransCd;
	}
}
