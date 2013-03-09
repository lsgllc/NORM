package com.txdot.isd.rts.services.data;/*  * FeesItemsTableData.java *  * (c) Texas Department of Transportation  2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog. * 							Format, Hungarian notation for variables.  * 							defect 7894 Ver 5.2.3  * B Hargrove	06/28/2005	Refactor\Move  * 							FeesItemsTableData class from *							com.txdot.isd.rts.client.reg.ui to *							com.txdot.isd.rts.services.data. *							defect 7894 Ver 5.2.3 * --------------------------------------------------------------------- *//** * FeesItemsTableData is a simple data object that contains the data  * used by the tables in REG015. *  * @version	5.2.3		06/28/2005 * @author	Joseph Kwik * <br>Creation Date:	12/20/01 15:22:02 *//* &FeesItemsTableData& */public class FeesItemsTableData{/* &FeesItemsTableData'csItemDesc& */	private java.lang.String csItemDesc;/* &FeesItemsTableData'caAmount& */	private com.txdot.isd.rts.services.util.Dollar caAmount;	/**	 * Creates a FeesItemsTableData.	 *//* &FeesItemsTableData.FeesItemsTableData& */	public FeesItemsTableData()	{		super();	}	/**	 * Return the caAmount.	 * 	 * @return com.txdot.isd.rts.services.util.Dollar	 *//* &FeesItemsTableData.getAmount& */	public com.txdot.isd.rts.services.util.Dollar getAmount()	{		return caAmount;	}	/**	 * Return the item description.	 * 	 * @return java.lang.String	 *//* &FeesItemsTableData.getDesc& */	public java.lang.String getDesc()	{		return csItemDesc;	}	/**	 * Set the caAmount.	 * 	 * @param aaNewAmount com.txdot.isd.rts.services.util.Dollar	 *//* &FeesItemsTableData.setAmount& */	public void setAmount(		com.txdot.isd.rts.services.util.Dollar aaNewAmount)	{		caAmount = aaNewAmount;	}	/**	 * Set the item description.	 * 	 * @param asNewDesc java.lang.String	 *//* &FeesItemsTableData.setDesc& */	public void setDesc(java.lang.String asNewDesc)	{		csItemDesc = asNewDesc;	}}/* #FeesItemsTableData# */