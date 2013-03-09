package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.server.webapps.order.common.data.Address;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse;
import com.txdot.isd.rts.services.data.AddressData;

/*
 * SpecialPltOrdrCartItem.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/14/2007	Created Class.
 * 							defect 9120 Ver Special Plates
 * Bob B.		03/18/2008	Add SerialversionUID.
 * 							defect 9599 Ver Tres Amigos Prep
 * Bob B.		03/25/2008	Let WSAD generate serialVersionUID  
 * 							defect 9599 Ver Tres Amigos Prep  
 * ---------------------------------------------------------------------
 */

/**
 * Data class used to hold the Special Plate Shopping Cart Item.  
 * This is the data will not be added to a shopping cart vector until
 * they have entered all of the needed information.
 *
 * @version	Tres Amigos Prep	03/25/2008
 * @author	Jeff Seifert
 * <br>Creation Date:			03/14/2007 14:00:00
 */
public class SpecialPltOrdrCartItem implements Serializable
{
	// defect 9599
	// the 318 stands for march 18th
	// private final static long serialVersionUID = 318;
	static final long serialVersionUID = 3718181817407692221L;
	// end defect 9599	
	private Address caAddress = null;
	private SpecialPlatesInfoResponse caSpecialPlateSelection = null;
	private boolean cbAdditionalSet = false;
	private boolean cbInsertTrans = false;
	private boolean cbISA = false;
	private boolean cbPLP = false;
	private boolean cbUpdateTrans = false;
	private int ciResComptCnty;
	private String csEmailAddr = "";
	private String csItrntTraceNo = "";
	private String csMfgPltNo = "";
	private String csName1 = "";
	private String csName2 = "";
	private String csPhoneNo = "";
	private String csRegPltNo = "";

	/**
	 * Gets the AddressData object.
	 * 
	 * @return AddressData
	 */
	public Address getAddress()
	{
		return caAddress;
	}

	/**
	 * Gets the email Address.
	 * 
	 * @return
	 */
	public String getEmailAddr()
	{
		return csEmailAddr;
	}

	/**
	 * Gets the Internet Trace Number.
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Gets the Manufacturing Plate Number Requested.
	 * Used for PLP requests.
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Gets the name.
	 * 
	 * @return String
	 */
	public String getName1()
	{
		return csName1;
	}

	/**
	 * Gets the name 2.
	 * 
	 * @return String
	 */
	public String getName2()
	{
		return csName2;
	}

//	/**
//	 * Gets the Organization No
//	 * 
//	 * @return String
//	 */
//	public String getOrgNo()
//	{
//		return csOrgNo;
//	}

	/**
	 * Gets the Phone Number
	 * 
	 * @return String
	 */
	public String getPhoneNo()
	{
		return csPhoneNo;
	}

//	/**
//	 * Gets Reg Plate Code for Shopping Cart Item
//	 * 
//	 * @return String
//	 */
//	public String getRegPltCd()
//	{
//		return csRegPltCd;
//	}

	/**
	 * Gets the Registration Plate Number.
	 * Used for both PLP and Non PLP.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Gets the Res. Compt. County.
	 * 
	 * @return int
	 */
	public int getResComptCnty()
	{
		return ciResComptCnty;
	}

	/**
	 * Returns the special plate information about the plate
	 * that was selected.
	 * 
	 * @return SpecialPlatesInfoResponse
	 */
	public SpecialPlatesInfoResponse getSpecialPlateSelection()
	{
		return caSpecialPlateSelection;
	}

	/**
	 * Returns if this item in the shopping cart is a purchase
	 * of an additional set or a single set.
	 * 
	 * @return boolean
	 */
	public boolean isAdditionalSet()
	{
		return cbAdditionalSet;
	}

	/**
	 * Gets if the Shopping cart item has been inserted into the 
	 * transaction table at TxDOT.
	 * 
	 * @return boolean
	 */
	public boolean isInsertTrans()
	{
		return cbInsertTrans;
	}

	/**
	 * Returns if this item in the shopping cart is a purchase
	 * of an ISA symboled plate or not.
	 * 
	 * @return boolean
	 */
	public boolean isISA()
	{
		return cbISA;
	}

	/**
	 * Returns if this item in the shopping cart is a purchase
	 * of a PLP or not.
	 * 
	 * @return boolean
	 */
	public boolean isPLP()
	{
		return cbPLP;
	}

	/**
	 * Gets if the Shopping cart item has been updated in the 
	 * transaction table at TxDOT.  The update contains the two epay
	 * timestamps (snd/rcv), payment status code, transaction 
	 * status code (POS Trans).  The only fields that need updating if
	 * this is true is done by batch when the POS transaction is 
	 * created.
	 * 
	 * @return boolean
	 */
	public boolean isUpdateTrans()
	{
		return cbUpdateTrans;
	}

	/**
	 * Sets if the item in the shopping cart is a purchase
	 * of an additional set or a single set or not.
	 * 
	 * @param abAdditionalSet boolean
	 */
	public void setAdditionalSet(boolean abAdditionalSet)
	{
		cbAdditionalSet = abAdditionalSet;
	}

	/**
	 * Sets the Address object
	 * 
	 * @param AddressData
	 */
	public void setAddress(Address aaAddress)
	{
		caAddress = aaAddress;
	}

	/**
	 * Sets the email Address.
	 * 
	 * @param asEmailAddr String
	 */
	public void setEmailAddr(String asEmailAddr)
	{
		csEmailAddr = asEmailAddr;
	}

	/**
	 * Sets if the Shopping cart item has been inserted into the 
	 * transaction table at TxDOT.
	 * 
	 * @param abInsertTrans boolean
	 */
	public void setInsertTrans(boolean abInsertTrans)
	{
		cbInsertTrans = abInsertTrans;
	}

	/**
	 * Sets if the item in the shopping cart is a purchase
	 * of an ISA symboled plate or not.
	 * 
	 * @param abISA boolean
	 */
	public void setISA(boolean abISA)
	{
		cbISA = abISA;
	}

	/**
	 * Sets the Internet Trace Number.
	 * 
	 * @param asItrntTraceNo String
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Sets the Manufacturing Plate Number Requested.
	 * Used for PLP requests.
	 * 
	 * @param asMfgPltNo String
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Sets the name information
	 * 
	 * @param asName String
	 */
	public void setName1(String asName)
	{
		csName1 = asName;
	}

	/**
	 * Sets the Name 2.
	 * 
	 * @param asName2 String
	 */
	public void setName2(String asName2)
	{
		csName2 = asName2;
	}

//	/**
//	 * Sets the Organization No
//	 * 
//	 * @param asOrgNo String
//	 */
//	public void setOrgNo(String asOrgNo)
//	{
//		csOrgNo = asOrgNo;
//	}

	/**
	 * Sets the Phone Number.
	 * 
	 * @param asPhoneNo String
	 */
	public void setPhoneNo(String asPhoneNo)
	{
		csPhoneNo = asPhoneNo;
	}

	/**
	 * Sets if the item in the shopping cart is a purchase
	 * of a PLP or not.
	 * 
	 * @param abPLP boolean
	 */
	public void setPLP(boolean abPLP)
	{
		cbPLP = abPLP;
	}

//	/**
//	 * Sets Reg Plate Code for Shopping Cart Item
//	 * 
//	 * @param asRegPltCd String
//	 */
//	public void setRegPltCd(String asRegPltCd)
//	{
//		csRegPltCd = asRegPltCd;
//	}

	/**
	 * Sets the Registration Plate Number.
	 * Used for both PLP and Non PLP.
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Sets the Res. Compt. County.
	 * 
	 * @param aiResComptCnty int
	 */
	public void setResComptCnty(int aiResComptCnty)
	{
		ciResComptCnty = aiResComptCnty;
	}

	/**
	 * Sets the special plate information about the plate
	 * that was selected.
	 * 
	 * @param aaSpecialPlateSelection SpecialPlatesInfoResponse
	 */
	public void setSpecialPlateSelection(SpecialPlatesInfoResponse aaSpecialPlateSelection)
	{
		caSpecialPlateSelection = aaSpecialPlateSelection;
	}

	/**
	 * Sets if the Shopping cart item has been updated in the 
	 * transaction table at TxDOT.  The update contains the two epay
	 * timestamps (snd/rcv), payment status code, transaction 
	 * status code (POS Trans).  The only fields that need updating if
	 * this is true is done by batch when the POS transaction is 
	 * created.
	 * 
	 * @param abUpdateTrans boolean
	 */
	public void setUpdateTrans(boolean abUpdateTrans)
	{
		cbUpdateTrans = abUpdateTrans;
	}

}
