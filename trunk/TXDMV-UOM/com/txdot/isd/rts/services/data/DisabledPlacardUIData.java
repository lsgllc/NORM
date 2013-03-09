package com.txdot.isd.rts.services.data;

/*
 * DisabledPlacardUIData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect POS B
 * K Harrell	01/11/2012	add cbRenTrans, cbReiTrans, 
 * 							 get/is methods 
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Data class to communicate report requirements to server.
 *
 * @version	6.10.0 			01/11/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardUIData extends ExemptAuditUIData
{
	
	private boolean cbAddTrans;
	private boolean cbDelTrans;
	private boolean cbRplTrans;
	
	// defect 11214 
	private boolean cbRenTrans;
	private boolean cbReiTrans;
	// end defect 11214 
	
	static final long serialVersionUID = 521549931655708045L;
	
	/**
	 * Get value of cbAddTrans
	 * 
	 * @return boolean
	 */
	public boolean isAddTrans()
	{
		return cbAddTrans;
	}

	/**
	 * Get value of cbDelTrans
	 * 
	 * @return boolean
	 */
	public boolean isDelTrans()
	{
		return cbDelTrans;
	}

	/**
	 * Get value of cbReiTrans 
	 * 
	 * @return cbReiTrans
	 */
	public boolean isReiTrans()
	{
		return cbReiTrans;
	}

	/**
	 * Get value of cbRenTrans 
	 * 
	 * @return the cbRenTrans
	 */
	public boolean isRenTrans()
	{
		return cbRenTrans;
	}

	/**
	 * Get value of cbRplTrans
	 * 
	 * @return boolean
	 */
	public boolean isRplTrans()
	{
		return cbRplTrans;
	}

	/**
	 * Set value of cbAddTrans
	 * 
	 * @param abAddTrans
	 */
	public void setAddTrans(boolean abAddTrans)
	{
		cbAddTrans = abAddTrans;
	}

	/**
	 * Set value of cbDelTrans
	 * 
	 * @param abDelTrans
	 */
	public void setDelTrans(boolean abDelTrans)
	{
		cbDelTrans = abDelTrans;
	}

	/**
	 * Sets value of cbReiTrans
	 * 
	 * @param cbReiTrans 
	 */
	public void setReiTrans(boolean abReiTrans)
	{
		cbReiTrans = abReiTrans;
	}

	/**
	 * Sets value of cbRenTrans 
	 * 
	 * @param abRenTrans 
	 */
	public void setRenTrans(boolean abRenTrans)
	{
		cbRenTrans = abRenTrans;
	}

	/**
	 * Set value of cbRplTrans
	 * 
	 * @param abRplTrans
	 */
	public void setRplTrans(boolean abRplTrans)
	{
		cbRplTrans = abRplTrans;
	}
}
