package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * DisabledPlacardCustomerData
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/01/2008	Add attributes, methods for Transaction Cache
 * 							 processing 	
 * 							add ciTransAMDate, ciTransTime, 
 * 							  get/set methods
 * 							add getAttributes() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/03/2008	add Comparable 
 * 							add compareTo()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/09/2008	Modified compareTo() to show disabled persons
 * 							first, then institutions. For Disabled, sorts
 * 							by LastName, First, CustId; For Institutions, 
 * 							sorts by Institution Name, Custid.  
 * 							modify compareTo()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	07/25/2009	add hasActivePlacard(), isTempDsabld()
 * 							delete ciMobltyDsabldIndi, get/set methods 
 * 							modify getAcctItmCd() 
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add DisabledPlacardCustomerData()
 * 							modify getAttributes(), getAddressData()
 * 							defect 10191 Ver Defect_POS_G 
 * K Harrell	05/28/2011	delete hasActivePlacard() 
 * 							add hasActivePlacard(boolean)
 * 							defect 10831 Ver 6.8.0 
 * K Harrell	10/10/2011	add ciNumPlacardsIssued, get/set methods
 * 							delete ciIssueTwoPlacards, get/set methods
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	02/06/2012	add isDeletePlacardTransaction()
 * 							defect 11279 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardCustomerData.
 *
 * @version	6.10.0			02/06/2012
 * @author	Kathy Harrell
 * @since 					10/27/2008
 */
public class DisabledPlacardCustomerData
	implements Serializable, Displayable, Comparable
{
	private AddressData caAddressData;
	private RTSDate caChngTimestmp;
	private RTSDate caCreateTimestmp;
	private RTSDate caInProcsTimestmp;
	private boolean cbNoRecordFound;
	private int ciChrgFeeIndi;
	private int ciCustIdntyNo;
	private int ciCustIdTypeCd;
	private int ciDeleteIndi;
	private int ciDsabldPltIndi;
	private int ciDsabldVetPltIndi;
	private int ciInProcsIdntyNo;
	private int ciInProcsIndi;
	private int ciInstIndi;
	// defect 11050
	private int ciNumPlacardsIssued; 
	//private int ciIssueTwoPlacards;
	// end defect 11050 
	
	// defect 10133 
	//private int ciMobltyDsabldIndi;
	// end defect 10133 
	private int ciOfcIssuanceNo;
	private int ciPermDsabldIndi;
	private int ciResComptCntyNo;
	private int ciRTSEffDate;
	private int ciRTSExpMo;
	private int ciRTSExpYr;
	private int ciSearchType;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciWsId;
	private String csCustId;
	private String csDsabldFrstName;
	private String csDsabldLstName;
	private String csDsabldMI;
	private String csEMail;
	private String csEmpId;
	private String csInstName;
	private String csItmCd;
	private String csPhone;
	private String csPlcrdNo;
	private String csTransCd;
	private Vector cvDsabldPlcrd;

	static final long serialVersionUID = -7788240647674926103L;

	/**
	 * Constructor
	 */
	public DisabledPlacardCustomerData()
	{
		super();
		caAddressData = new AddressData();
		cvDsabldPlcrd = new Vector();
	}

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		DisabledPlacardCustomerData laDPCustData =
			(DisabledPlacardCustomerData) aaObject;

		int liReturn = getInstIndi() - laDPCustData.getInstIndi();
		if (liReturn == 0)
		{
			if (isInstitution())
			{
				return getInstName().compareTo(
					laDPCustData.getInstName());
			}
			else
			{
				liReturn =
					getDsabldLstName().compareTo(
						laDPCustData.getDsabldLstName());
				if (liReturn == 0)
				{
					liReturn =
						getDsabldFrstName().compareTo(
							laDPCustData.getDsabldFrstName());
				}
			}

		}
		if (liReturn == 0)
		{
			liReturn = getCustId().compareTo(laDPCustData.getCustId());
		}
		return liReturn;
	}

	/**
	 * Return appropriate AcctItmCd given attributes 
	 * 
	 * @return String 
	 */
	public String getAcctItmCd()
	{
		// defect 10133
		// Consolidate to just two Account Item Codes  
		return isPermDsabld() ? AcctCdConstant.PDC : AcctCdConstant.TDC;

		//		String lsAcctItmCd = "";
		//		if (ciMobltyDsabldIndi == 1)
		//		{
		//			lsAcctItmCd =
		//				ciPermDsabldIndi == 1
		//					? AcctCdConstant.BPM
		//					: AcctCdConstant.BTM;
		//		}
		//		else
		//		{
		//			lsAcctItmCd =
		//				ciPermDsabldIndi == 1
		//					? AcctCdConstant.RPNM
		//					: AcctCdConstant.RTNM;
		//		}
		//		return lsAcctItmCd;
		// end defect 10133 
	}

	/**
	 * Get value of caAddressData
	 * 
	 * @return AddressData
	 */
	public AddressData getAddressData()
	{
		// defect 10191 
		if (caAddressData == null)
		{
			caAddressData = new AddressData();
		}
		// end defect 10191 
		return caAddressData;
	}

	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return HashSet
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();

		Field[] larrFields = this.getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				// defect 10191
				String lsFieldName = larrFields[i].getName();

				if (lsFieldName.equals("cvDsabldPlcrd"))
				{
					for (int j = 0; j < cvDsabldPlcrd.size(); j++)
					{
						DisabledPlacardData laData =
							(DisabledPlacardData) getDsabldPlcrd()
								.elementAt(
								j);

						UtilityMethods.addAttributesForObject(
							"xxDsabldPlcrdData" + (j + 1),
							laData.getAttributes(),
							lhmHash);
					}
				}
				else if (lsFieldName.equals("caAddressData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getAddressData().getAttributes(),
						lhmHash);
				}
				else
				{
					lhmHash.put(lsFieldName, larrFields[i].get(this));
				}
				// end defect 10191
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Get value of ciChrgFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgFeeIndi()
	{
		return ciChrgFeeIndi;
	}

	/**
	 * Get value of caCreateTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getCreateTimestmp()
	{
		return caCreateTimestmp;
	}

	/**
	 * Get value of csCustId
	 * 
	 * @return String
	 */
	public String getCustId()
	{
		return csCustId;
	}

	/**
	 * Get value of ciCustIdntyNo
	 * 
	 * @return int
	 */
	public int getCustIdntyNo()
	{
		return ciCustIdntyNo;
	}

	/**
	 * Get value of ciCustIdTypeCd
	 * 
	 * @return int
	 */
	public int getCustIdTypeCd()
	{
		return ciCustIdTypeCd;
	}

	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of csDsabldFrstName
	 * 
	 * @return String
	 */
	public String getDsabldFrstName()
	{
		return csDsabldFrstName;
	}

	/**
	 * Get value of csDsabldLstName
	 * 
	 * @return String
	 */
	public String getDsabldLstName()
	{
		return csDsabldLstName;
	}

	/**
	 * Set value of csDsabldMI
	 * 
	 * @return String
	 */
	public String getDsabldMI()
	{
		return csDsabldMI;
	}

	/**
	 * Get value of cvDsabldPlcrd
	 * 
	 * @return Vector
	 */
	public Vector getDsabldPlcrd()
	{
		// defect 10191 
		if (cvDsabldPlcrd == null)
		{
			setDsabldPlcrd(new Vector());
		}
		// end defect 10191 
		return cvDsabldPlcrd;
	}

	/**
	 * Get value of ciDsabldPltIndi
	 * 
	 * @return int
	 */
	public int getDsabldPltIndi()
	{
		return ciDsabldPltIndi;
	}

	/**
	 * Get value of ciDsabldVetPltIndi
	 * 
	 * @return int
	 */
	public int getDsabldVetPltIndi()
	{
		return ciDsabldVetPltIndi;
	}

	/**
	 * Get value of csEMail
	 * 
	 * @return String
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Get value of csEmpId
	 * 
	 * @return String
	 */
	public String getEmpId()
	{
		return csEmpId;
	}
	/**
	 * Get value of InProcsIndntyNo
	 * 
	 * @return int 
	 */
	public int getInProcsIdntyNo()
	{
		return ciInProcsIdntyNo;
	}

	/**
	 * Get value of ciInProcsIndi
	 * 
	 * @return int
	 */
	public int getInProcsIndi()
	{
		return ciInProcsIndi;
	}

	/**
	 * Get value of caInProcsTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getInProcsTimestmp()
	{
		return caInProcsTimestmp;
	}

	/**
	 * Get value of ciInstIndi
	 * 
	 * @return int
	 */
	public int getInstIndi()
	{
		return ciInstIndi;
	}

	/**
	 * Get value of csInstName
	 * 
	 * @return String
	 */
	public String getInstName()
	{
		return csInstName;
	}
	//	defect 11050 
	//	/**
	//	* Get value of ciIssueTwoPlacards
	//	* 
	//	* @return int
	//	*/
	//	public int getIssueTwoPlacards()
	//	{
	//	return ciIssueTwoPlacards;
	//	}
		// end defect 11050 

	/**
	 * Get value of csItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}

	//	/**
	//	 * Get value of ciMobltyDsabldIndi
	//	 * 
	//	 * @return int
	//	 */
	//	public int getMobltyDsabldIndi()
	//	{
	//		return ciMobltyDsabldIndi;
	//	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return 'calculated' Owner Name w/ MI 
	 * 
	 * @return String
	 */
	public String getOwnerName()
	{
		return getOwnerName(true);
	}

	/**
	 * Return 'calculated' Owner Name
	 * 
	 * @return String
	 */
	public String getOwnerName(boolean abShowMI)
	{
		String lsOwnrName = "";
		{
			if (ciInstIndi == 0)
			{
				lsOwnrName = csDsabldFrstName.trim() + " ";

				if (abShowMI
					&& csDsabldMI != null
					&& csDsabldMI.length() != 0)
				{
					lsOwnrName = lsOwnrName + csDsabldMI + " ";
				}
				lsOwnrName = lsOwnrName + csDsabldLstName.trim();
			}
			else
			{
				lsOwnrName = csInstName.trim();
			}

		}
		return lsOwnrName;
	}

	/**
	 * Return Short Version of 'calculated' Owner Name
	 * 
	 * @return String
	 */
	public String getOwnerNameShort()
	{
		String lsOwnrName = "";
		{
			if (ciInstIndi == 0)
			{
				lsOwnrName =
					csDsabldFrstName.trim()
						+ " "
						+ csDsabldLstName.trim();
			}
			else
			{
				lsOwnrName = csInstName.trim();
			}

			if (lsOwnrName.length() > 30)
			{
				lsOwnrName = lsOwnrName.substring(0, 30).trim();
			}
		}
		return lsOwnrName;
	}

	/**
	 * Get value of ciPermDsabldIndi
	 * 
	 * @return int
	 */
	public int getPermDsabldIndi()
	{
		return ciPermDsabldIndi;
	}

	/**
	 * Get value of csPhone
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get value of csPlcrdNo
	 * 
	 * @return String
	 */
	public String getPlcrdNo()
	{
		return csPlcrdNo;
	}

	/**
	 * Get value of ciResComptCntyNo
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Get value of ciRTSExpMo
	 * 
	 * @return int
	 */
	public int getRTSExpMo()
	{
		return ciRTSExpMo;
	}

	/**
	 * Get value of ciRTSExpYr
	 * 
	 * @return int
	 */
	public int getRTSExpYr()
	{
		return ciRTSExpYr;
	}

	/**
	 * Get value of ciSearchType
	 * 
	 * @return int
	 */
	public int getSearchType()
	{
		return ciSearchType;
	}

	/**
	 * Get value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @return int 
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Get value of csTransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Get value of ciWsId
	 * 
	 * @return int
	 */
	public int getWsId()
	{
		return ciWsId;
	}

	/**
	 * Return Hashtable to include the following: 
	 * 	- Boolean to denote if Customer has Active Placard
	 *  - Integer - Representation of Max Date in YYYYMMDD format  
	 * 
	 * @param boolean 
	 * @return Hashtable 
	 */
	public Hashtable hasActivePlacard(boolean lbChkTDC)
	{
		boolean lbActive = false;

		// defect 10831
		int liMaxExpDate = 0;

		if (!cvDsabldPlcrd.isEmpty())
		{
			for (int i = 0; i < cvDsabldPlcrd.size(); i++)
			{
				DisabledPlacardData laData =
					(DisabledPlacardData) cvDsabldPlcrd.elementAt(i);

				if (laData.isActive())
				{
					lbActive = true;
					if (!lbChkTDC)
					{
						break;
					}
					else
					{
						int liCompExpDate =
							laData.getRTSExpYr() * 10000
								+ laData.getRTSExpMo() * 100
								+ 1;
								
						liMaxExpDate =
							liCompExpDate > liMaxExpDate
								? liCompExpDate
								: liMaxExpDate;
					}
				}
			}
		}
		Hashtable lhtReturn = new Hashtable();
		lhtReturn.put(
			MiscellaneousRegConstant.DSABLD_PLCRD_HASACTIVE,
			new Boolean(lbActive));
		lhtReturn.put(
			MiscellaneousRegConstant.DSABLD_PLCRD_MAXACTIVE_EXPDATE,
			new Integer(liMaxExpDate));
		return lhtReturn;
		// end defect 10831
	}

	/**
	 * Return boolean to denote if Customer is available for update  
	 * 
	 * @return boolean
	 */
	public boolean isAvailableForUpdate()
	{
		return ciInProcsIndi == 0;
	}
	
	/**
	 * Return boolean to denote if Delete Placard Transaction
	 * 
	 * @return boolean   
	 */
	public boolean isDeletePlacardTransaction() 
	{
		// This list includes all Delete Placard 
		//  TransCds since 2008.  2009 started using 
		//  DELPDC, DELTDC.
		return csTransCd.equals("DLBPM") ||
		csTransCd.equals("DLBTM") ||
		csTransCd.equals("DLRPNM") ||
		csTransCd.equals("DLRTNM") ||
		csTransCd.equals(TransCdConstant.DELPDC) ||
		csTransCd.equals(TransCdConstant.DELTDC); 
	}

	/**
	 * Return boolean to denote if Customer has Disabled Plate  
	 * 
	 * @return boolean
	 */
	public boolean isDsabldPltIndi()
	{
		return ciDsabldPltIndi == 1;
	}

	/**
	 * Return boolean to denote if Customer has either type of plate 
	 * 
	 * @return boolean
	 */
	public boolean isDsabldPltOwnr()
	{
		return isDsabldPltIndi() || isDsabldVetPltIndi();
	}

	/**
	 * Return boolean to denote if Customer has Disabled Vet Plate 
	 * 
	 * @return boolean
	 */
	public boolean isDsabldVetPltIndi()
	{
		return ciDsabldVetPltIndi == 1;
	}

	/**
	 * Return boolean to denote if Customer is an Institution  
	 * 
	 * @return boolean
	 */
	public boolean isInstitution()
	{
		return ciInstIndi == 1;
	}

	//	/**
	//	 * Return boolean to denote if Customer is Mobility Disabled  
	//	 * 
	//	 * @return boolean
	//	 */
	//	public boolean isMobltyDsabld()
	//	{
	//		return ciMobltyDsabldIndi == 1;
	//	}

	/**
	 * Return boolean to denote if No Record was found   
	 * 
	 * @return boolean
	 */
	public boolean isNoRecordFound()
	{
		return cbNoRecordFound;
	}
	
	/**
	 * Return boolean to denote if Customer is Permanently Disabled  
	 * 
	 * @return boolean
	 */
	public boolean isPermDsabld()
	{
		return ciPermDsabldIndi == 1;
	}

	/**	
	 * Return boolean to denote if Customer is Temporarily Disabled  
	 * 
	 * @return boolean
	 */
	public boolean isTempDsabld()
	{
		return ciPermDsabldIndi == 0;
	}

	/**
	 * Set value of caAddressData
	 * 
	 * @param aaAddressData
	 */
	public void setAddressData(AddressData aaAddressData)
	{
		caAddressData = aaAddressData;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Get value of ciChrgFeeIndi
	 * 
	 * @param aiChrgFeeIndi
	 */
	public void setChrgFeeIndi(int aiChrgFeeIndi)
	{
		ciChrgFeeIndi = aiChrgFeeIndi;
	}

	/**
	 * Set value of caCreateTimestmp
	 * 
	 * @param aaCreateTimestmp
	 */
	public void setCreateTimestmp(RTSDate aaCreateTimestmp)
	{
		caCreateTimestmp = aaCreateTimestmp;
	}

	/**
	 * Set value of csCustId
	 * 
	 * @param asCustId
	 */
	public void setCustId(String asCustId)
	{
		csCustId = asCustId;
	}

	/**
	 * Set value of ciCustIdntyNo
	 * 
	 * @param aiCustIdntyNo
	 */
	public void setCustIdntyNo(int aiCustIdntyNo)
	{
		ciCustIdntyNo = aiCustIdntyNo;
	}

	/**
	 * Get value of ciCustIdTypeCd
	 * 
	 * @param aiCustIdTypeCd
	 */
	public void setCustIdTypeCd(int aiCustIdTypeCd)
	{
		ciCustIdTypeCd = aiCustIdTypeCd;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of csDsabldFrstName
	 * 
	 * @param asDsabldFrstName
	 */
	public void setDsabldFrstName(String asDsabldFrstName)
	{
		csDsabldFrstName = asDsabldFrstName;
	}

	/**
	 * Set value of csDsabldLstName
	 * 
	 * @param asDsabldLstName
	 */
	public void setDsabldLstName(String asDsabldLstName)
	{
		csDsabldLstName = asDsabldLstName;
	}

	/**
	 * Set value of csDsabldMI
	 * 
	 * @param asDsabldMI
	 */
	public void setDsabldMI(String asDsabldMI)
	{
		csDsabldMI = asDsabldMI;
	}

	/**
	 * Get value of cvDsabldPlcrd
	 * 
	 * @param avActDsabldPlcrd
	 */
	public void setDsabldPlcrd(Vector avActDsabldPlcrd)
	{
		cvDsabldPlcrd = avActDsabldPlcrd;
	}

	/**
	 * Set value of ciDsabldPltIndi
	 * 
	 * @param aiDsabldPltIndi
	 */
	public void setDsabldPltIndi(int aiDsabldPltIndi)
	{
		ciDsabldPltIndi = aiDsabldPltIndi;
	}

	/**
	 * Set value of ciDsabldVetPltIndi
	 * 
	 * @param aiDsabldVetPltIndi
	 */
	public void setDsabldVetPltIndi(int aiDsabldVetPltIndi)
	{
		ciDsabldVetPltIndi = aiDsabldVetPltIndi;
	}

	/**
	 * Set value of csEMail
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Get value of csEmpId
	 * 
	 * @param asEmpId
	 */
	public void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}

	/**
	 * Set value of InProcsIdntyNo
	 * 
	 * @param aiInProcsIdntyNo
	 */
	public void setInProcsIdntyNo(int aiInProcsIdntyNo)
	{
		ciInProcsIdntyNo = aiInProcsIdntyNo;
	}

	/**
	 * Get value of ciInProcsIndi
	 * 
	 * @param aiInProcsIndi
	 */
	public void setInProcsIndi(int aiInProcsIndi)
	{
		ciInProcsIndi = aiInProcsIndi;
	}

	/**
	 * Get value of caInProcsTimestmp
	 * 
	 * @param aaInProcsTimestmp
	 */
	public void setInProcsTimestmp(RTSDate aaInProcsTimestmp)
	{
		caInProcsTimestmp = aaInProcsTimestmp;
	}

	/**
	 * Get value of ciInstIndi
	 * 
	 * @param aiInstIndi
	 */
	public void setInstIndi(int aiInstIndi)
	{
		ciInstIndi = aiInstIndi;
	}

	/**
	 * Set value of csInstName
	 * 
	 * @param asInstName
	 */
	public void setInstName(String asInstName)
	{
		csInstName = asInstName;
	}
	// defect 11050 
	//	/**
	//	 * Set value of ciIssueTwoPlacards
	//	 * 
	//	 * @param aiIssueTwoPlacards
	//	 */
	//	public void setIssueTwoPlacards(int aiIssueTwoPlacards)
	//	{
	//		ciIssueTwoPlacards = aiIssueTwoPlacards;
	//	}
	// end defect 11050 

	/**
	 * Set value of csItmCd
	 * 
	 * @param asItmCd
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}

	//	/**
	//	 * Set value of ciMobltyDsabldIndi
	//	 * 
	//	 * @param aiMobltyDsabldIndi
	//	 */
	//	public void setMobltyDsabldIndi(int aiMobltyDsabldIndi)
	//	{
	//		ciMobltyDsabldIndi = aiMobltyDsabldIndi;
	//	}

	/**
	 * Set value of cbNoRecordFound 
	 * 
	 * @param abNoRecordFound
	 */
	public void setNoRecordFound(boolean abNoRecordFound)
	{
		cbNoRecordFound = abNoRecordFound;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of ciPermDsabldIndi
	 * 
	 * @param aiPermDsabldIndi
	 */
	public void setPermDsabldIndi(int aiPermDsabldIndi)
	{
		ciPermDsabldIndi = aiPermDsabldIndi;
	}

	/**
	 * Set value of csPhone
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}

	/**
	 * Set value of csPlcrdNo
	 * 
	 * @param asPlcrdNo
	 */
	public void setPlcrdNo(String asPlcrdNo)
	{
		csPlcrdNo = asPlcrdNo;
	}

	/**
	 * Set value of ciResComptCntyNo
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciRTSExpMo
	 * 
	 * @param aiRTSExpMo
	 */
	public void setRTSExpMo(int aiRTSExpMo)
	{
		ciRTSExpMo = aiRTSExpMo;
	}

	/**
	 * Set value of ciRTSExpYr
	 * 
	 * @param aiRTSExpYr
	 */
	public void setRTSExpYr(int aiRTSExpYr)
	{
		ciRTSExpYr = aiRTSExpYr;
	}

	/**
	 * Set value of ciSearchType
	 * 
	 * @param aiSearchType
	 */
	public void setSearchType(int aiSearchType)
	{
		ciSearchType = aiSearchType;
	}

	/**
	 * Set value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Set value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set value of ciWsId
	 * 
	 * @param aiWsId
	 */
	public void setWsId(int aiWsId)
	{
		ciWsId = aiWsId;
	}

	/**
	 * @return the ciNumPlacardsIssued
	 */
	public int getNumPlacardsIssued()
	{
		return ciNumPlacardsIssued;
	}

	/**
	 * @param ciNumPlacardsIssued the ciNumPlacardsIssued to set
	 */
	public void setNumPlacardsIssued(int ciNumPlacardsIssued)
	{
		this.ciNumPlacardsIssued = ciNumPlacardsIssued;
	}
}
