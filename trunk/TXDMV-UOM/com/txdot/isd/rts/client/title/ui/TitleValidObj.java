package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.DealerTitleData;

/*
 *
 * TitleValidObj.java 
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * J Rue		03/22/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	02/21/2007	Renaming for analysis of invprocsngcd use 
 * 							rename ciInvProcssCd to ciInvProcsngCd 
 * 							rename get/set methods to match
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	delete ciInvProcsngCd, get/set methods
 * 							defect 9085 Ver Special Plates  
 * K Harrell	06/09/2009	add cbRecordNotApplicable, get/set method
 * 							delete caVehMiscData, get/set methods
 * 							delete cbIsRecordNotApplicable, is/set method
 * 							delete ciStolenWaivedIndi, get/set methods 
 * 							delete getPlateType(), getRegClassDesc(),
 * 							 getRegData()
 * 							defect 10035 Ver Defect_POS_F
 * K Harrell	12/16/2009	modify caDlrTtlData, get/set methods 
 * 							delete cvDlrs, getDlrs(), setDlrs()
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * Valid Utility class for the Title Event
 *
 * @version	Defect_POS_H	12/16/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:		09/20/2001 19:42:28
 */

public class TitleValidObj implements java.io.Serializable
{
	// defect 10290 
	// DealerTitleData vs. Object  
	private DealerTitleData caDlrTtlData = null;
	// end defect 10290 

	//LienHoldersData
	private Object caLienData;

	//MfVehicle original data
	private Object caMfVehOrig = null;
	private Object caOwnrEvidCds;

	// Objects
	private Object caRegData;

	//Reg title additional data for Fees
	private Object caRegTtlAddData = null;

	//	Change Registration
	private boolean cbChangeRegis = false;

	//Used for LienDisplay screen TTL005
	private boolean cbDoneTTL005 = false;

	/**
	 * Set if from TTL003. Used in REG008 for determining the
	 * previous screen to set up direction flow. 
	 */
	private boolean cbFromTTL003 = false;

	//Record Not Applicable
	private boolean cbRecordNotApplicable = false;
	// boolean
	private boolean cbRegExpired;

	//Indicator for Apprehended county
	private int ciRegPnltyChrgIndi = 0;

	// String 
	private String csDocTypeCdDesc;
	private String csPlateType;
	private String csRegClassDesc;
	//private Vector cvDlrs = null;
	private Vector cvOdoBrn;

	// Vector 
	private Vector cvTrlTyp;

	// defect 10035 	
	//	//Veh MiscData
	//	private Object caVehMiscData = null;
	// end defect 10035 

	private final static long serialVersionUID = -8281530272509265397L;

	/**
	 * TitleValidObj constructor comment.
	 */
	public TitleValidObj()
	{
		super();
	}


	/**
	 * Get value of caDlrTtlData
	 * 
	 * @return DealerTitleData
	 */
	// defect 10290 
	// Return DealerTitleData vs. Object
	public DealerTitleData getDlrTtlData()
	{
		// end defect 10290 
		return caDlrTtlData;
	}

	/**
	 * Get value of csDocTypeCdDesc
	 * 
	 * @return String
	 */
	public String getDocTypeCdDesc()
	{
		return csDocTypeCdDesc;
	}

	/**
	 * Get value of caLienData
	 * 
	 * @return Object
	 */
	public Object getLienData()
	{
		return caLienData;
	}

	/**
	 * Get value of caMfVehOrig
	 * 
	 * @return Object
	 */
	public Object getMfVehOrig()
	{
		return caMfVehOrig;
	}

	/**
	 * Get value of cvOdoBrn
	 * 
	 * @return Vector
	 */
	public Vector getOdBrn()
	{
		return cvOdoBrn;
	}

	/**
	 * Get value of caOwnrEvidCds
	 * 
	 * @return Object
	 */
	public Object getOwnrEvidCds()
	{
		return caOwnrEvidCds;
	}


	/**
	 * Get value of ciRegPnltyChrgIndi
	 * 
	 * @return int
	 */
	public int getRegPnltyChrgIndi()
	{
		return ciRegPnltyChrgIndi;
	}

	/**
	 * Get value of caRegTtlAddData
	 * 
	 * @return Object
	 */
	public Object getRegTtlAddData()
	{
		return caRegTtlAddData;
	}

	/**
	 * Get value of cvTrlTyp
	 * 
	 * @return Vector
	 */
	public Vector getTrlTyp()
	{
		return cvTrlTyp;
	}

	
	/**
	 * Get value of cbChangeRegis
	 * 
	 * @return boolean
	 */
	public boolean isChangeRegis()
	{
		return cbChangeRegis;
	}

	/**
	 * Get value of cbDoneTTL005
	 * 
	 * @return boolean
	 */
	public boolean isDoneTTL005()
	{
		return cbDoneTTL005;
	}

	/**
	 * Get value of cbFromTTL003
	 * 
	 * @return boolean
	 */
	public boolean isFromTTL003()
	{
		return cbFromTTL003;
	}

	/**
	 * Get value of cbRecordNotApplicable
	 * 
	 * @return boolean
	 */
	public boolean isRecordNotApplicable()
	{
		return cbRecordNotApplicable;
	}

	/**
	 * Get value of cbRegExpired
	 * 
	 * @return boolean
	 */
	public boolean isRegExpired()
	{
		return cbRegExpired;
	}

	/**
	 * Set value of cbChangeRegis
	 * 
	 * @param abChangeRegis boolean
	 */
	public void setChangeRegis(boolean abChangeRegis)
	{
		cbChangeRegis = abChangeRegis;
	}

	/**
	 * Set value of caDlrTtlData
	 * 
	 * @param aaDlrTtlData DealerTitleData
	 */
	// defect 10290 
	// set DealerTitleData vs. Object 
	public void setDlrTtlData(DealerTitleData aaDlrTtlData)
	{
		// end defect 10290 
		caDlrTtlData = aaDlrTtlData;
	}

	/**
	 * Set value of csDocTypeCdDesc
	 * 
	 * @param asDocTypeCdDesc String
	 */
	public void setDocTypeCdDesc(String asDocTypeCdDesc)
	{
		csDocTypeCdDesc = asDocTypeCdDesc;
	}

	/**
	 * Set value of cbDoneTTL005
	 * 
	 * @param abDoneTTL005 boolean
	 */
	public void setDoneTTL005(boolean abDoneTTL005)
	{
		cbDoneTTL005 = abDoneTTL005;
	}

	/**
	 * Set value of cbFromTTL003
	 * 
	 * @param abFromTTL003 boolean
	 */
	public void setFromTTL003(boolean abFromTTL003)
	{
		cbFromTTL003 = abFromTTL003;
	}

	/**
	 * Set value of caLienData
	 * 
	 * @param aaLienData Object
	 */
	public void setLienData(Object aaLienData)
	{
		caLienData = aaLienData;
	}

	/**
	 * Set value of caMfVehOrig
	 * 
	 * @param aaMfVehOrig Object
	 */
	public void setMfVehOrig(Object aaMfVehOrig)
	{
		caMfVehOrig = aaMfVehOrig;
	}

	/**
	 * Set value of cvOdoBrn
	 * 
	 * @param avOdoBrn Vector
	 */
	public void setOdBrn(Vector avOdoBrn)
	{
		cvOdoBrn = avOdoBrn;
	}

	/**
	 * Set value of caOwnrEvidCds
	 * 
	 * @param aaOwnrEvidCds Object
	 */
	public void setOwnrEvidCds(Object aaOwnrEvidCds)
	{
		caOwnrEvidCds = aaOwnrEvidCds;
	}

	/**
	 * Set value of csPlateType
	 * 
	 * @param asPlateType String
	 */
	public void setPlateType(String asPlateType)
	{
		csPlateType = asPlateType;
	}

	/**
	 * Set value of cbRecordNotApplicable
	 * 
	 * @param abRecordNotApplicable boolean
	 */
	public void setRecordNotApplicable(boolean abRecordNotApplicable)
	{
		cbRecordNotApplicable = abRecordNotApplicable;
	}

	/**
	 * Set value of caRegData
	 * 
	 * @param asRegClassDesc String
	 */
	public void setRegClassDesc(String asRegClassDesc)
	{
		csRegClassDesc = asRegClassDesc;
	}

	/**
	 * Set value of caRegData
	 * 
	 * @param aaRegData Object
	 */
	public void setRegData(Object aaRegData)
	{
		caRegData = aaRegData;
	}

	/**
	 * Set value of cbRegExpired
	 * 
	 * @param abRegExpired boolean
	 */
	public void setRegExpired(boolean abRegExpired)
	{
		cbRegExpired = abRegExpired;
	}

	/**
	 * Set value of ciRegPnltyChrgIndi
	 * 
	 * @param aiRegPnltyChrgIndi int
	 */
	public void setRegPnltyChrgIndi(int aiRegPnltyChrgIndi)
	{
		ciRegPnltyChrgIndi = aiRegPnltyChrgIndi;
	}

	/**
	 * Set value of caRegTtlAddData
	 * 
	 * @param aaRegTtlAddData Object
	 */
	public void setRegTtlAddData(Object aaRegTtlAddData)
	{
		caRegTtlAddData = aaRegTtlAddData;
	}

	/**
	 * Set value of cvTrlTyp
	 * 
	 * @param avTrlTyp Vector
	 */
	public void setTrlTyp(Vector avTrlTyp)
	{
		cvTrlTyp = avTrlTyp;
	}
}
