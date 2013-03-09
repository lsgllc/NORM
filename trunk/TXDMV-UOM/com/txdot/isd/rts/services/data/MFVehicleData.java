package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ClassToPlateCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 *
 * MFVehicleData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	05/03/2005	delete caMiscData	
 * 							deprecate getMiscData(),setMiscData()
 * 							defect 8188 Ver 5.2.3 
 * B. Brown		08/18/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table 
 * 							rts_itrnt_data.
 * 							add cOwnerData,cUserSuppliedOwnerData,
 * 								cRegData,cTitleData,cVehicleData
 * 							add getCOwnerData(),
 * 								getCUserSuppliedOwnerData(),
 * 								getCRegData(),getCTitleData(),
 * 								getCVehicleData() 
 * 							modify getOwnerData(),
 * 								   getUserSuppliedOwnerData(),
 * 								   getRegData(),getTitleData(),
 * 								   getVehicleData() 			
 *							defect 7899 Ver 5.2.3
 * K Harrell	02/13/2006	Initialize cbFromMF to true 
 * 							defect 6861 Ver 5.2.3 
 * J Rue		01/31/2007	Add getter/setter spclPltRegis
 * 							add getSpclPltRegis(), 
 * 							add setSpclPltRegis()
 * 							defect 9086 Ver Special Plate
 * K Harrell	02/07/2007  add caSpclPltRegisData, getSpclPltRegis(),
 *  J Rue						setSpclPltRegis()
 * 							delete objects,methods from 7899 
 * 								==> see	08/18/2005 details 
 * 							defect 9085  Ver Special Plates 
 * K Harrell	02/13/2007	added isAvailableForProcessing()
 * 							defect 9085 Ver Special Plates 
 * J Rue		02/22/2007	Rename getter/setter
 * 							getSpclPltRegis() to getSpclPltRegisData()
 * 							defect 9086 Ver Special Plate
 * K Harrell	03/04/2007	Added isSpclPlt()
 * 							defect 9085 Ver Special Plate
 * J Rue		04/06/2007	Add getter/setter if MF record returned
 * 							from is a Special Plate record only
 * 							add isSPRecordOnlyVehInq(), 
 * 							add setSPRecordOnlyVehInq()
 * 							defect 9086 Ver Special Plates
 * K Harrell	10/16/2007	add isPTOEligible()
 * 							defect 9368 Ver Special Plates 2 
 * K Harrell	01/21/2008	Use ClassToPltCache to determine if 
 * 							PTOEligible
 * 							modify isPTOEligible()
 * 							defect 9524 Ver 3 Amigos Prep 
 * B Brown		08/04/2008	Make a Vendor Plate check when an internet 
 * 							customer renews a vehicle with a special
 * 							plate. If the vehicle being renewed has a 
 * 							vendor plate, don't check to see if the
 * 							vehicle and plate renewal exp mo/yr are
 * 							the same (in sync).
 * 							Also, don't let a vehicle with a Vendor 
 * 							plate renew if the plate exp date is less 
 * 							than or equal to the veh exp date.
 * 							modify isAvailableForProcessing()
 * 							defect 9781 Ver MyPlates_POS 
 * K Harrell	01/07/2009 	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify isAvailableForProcessing()  
 *        					defect 9864 Ver Defect_POS_D
 * J Zwiener	01/29/2009	Copy over Renewal Recipient addr, if avail.,
 * 							to SR Func Trans for non-special plate
 * 							events.  Otherwise, use Owner address. 
 * 							add assgnSpclPltRegisAddr()
 * 							defect 9893 Ver Defect_POS_D
 * K Harrell	02/18/2009	add getSpclPltAddrUpdtType() 
 * 							defect 9893 Ver Defect_POS_D 
 * K Harrell	06/28/2009	Implement AddressData.isPopulated()
 * 							add MFVehicleData(boolean) 
 * 							modify assgnSpclPltRegisAddr(), 
 * 							  getSpclPltAddrUpdtType() 
 * 							defect 10112 Ver Defect_POS_E 
 * K Harrell	04/09/2010	Correct assignment of address for VehInq
 * 							only Special Plate.  
 * 							modify assgnSpclPltRegisAddr() 
 * 							defect 10447 Ver POS_640 
 * B Brown		04/30/2010	Remove date check for Vendor plates
 * 							modify isAvailableForProcessing()
 * 							defect 10391 Ver POS_640
 * B Brown		02/02/2011	Remove synch'ed expiration dates edit for 
 * 							Charity plates.
 * 							modify isAvailableForProcessing()
 * 							defect 10714 Ver POS_670
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get and set methods for 
 * MFVehicleData 
 * 
 * @version	POS_670			02/02/2011
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 12:54:34  
 */

public class MFVehicleData implements Serializable, Displayable
{
	// Object 	
	private OwnerData caOwnerData = null;
	private RegistrationData caRegData = null;
	private SpecialPlatesRegisData caSpclPltRegisData = null;
	private TitleData caTitleData = null;
	private OwnerData caUserSuppliedOwnerData = null;
	private VehicleData caVehicleData = null;

	// boolean 
	private boolean cbDoNotBuildMvFunc;
	private boolean cbFromMF = true;
	private boolean cbSPRecordOnly;

	// int 
	private int ciJnkIndi;
	private int ciTransAMDate; // SendCache - Internet
	private int ciTransTime; // SendCache - Internet

	// Vector 
	private Vector cvSalvage;

	private final static String RENEWAL_CD = "R";

	private final static long serialVersionUID = -6831301951553190734L;

	/**
	 * MFVehicleData constructor comment.
	 */
	public MFVehicleData()
	{
		super();
	}

	/**
	 * MFVehicleData constructor comment.
	 */
	public MFVehicleData(boolean abBuildObjects)
	{
		super();

		if (abBuildObjects)
		{
			caOwnerData = new OwnerData();
			caRegData = new RegistrationData();
			caSpclPltRegisData = new SpecialPlatesRegisData();
			caTitleData = new TitleData();
			caVehicleData = new VehicleData();
			cvSalvage = new Vector();
			SalvageData laSalvageData = new SalvageData();
			cvSalvage.add(laSalvageData);
		}
	}

	/**
	 * Set value of SpclPltRegisAddr
	 * 
	 */
	public void assgnSpclPltRegisAddr()
	{
		// defect 10447 
		// Reset Address on Vehicle Inquiry w/ only Special Plate 
		Object laAddressData = null;

		// Implement new OwnerData, AddressData.isPopulated() 
		if (caRegData != null
			&& caRegData.getRenwlMailAddr() != null
			&& caRegData.getRenwlMailAddr().isPopulated())
		{
			laAddressData =
				UtilityMethods.copy(getRegData().getRenwlMailAddr());
		}
		else
		{
			if (getOwnerData() != null
				&& getOwnerData().getAddressData() != null
				&& getOwnerData().getAddressData().isPopulated())
			{
				laAddressData =
					UtilityMethods.copy(
						getOwnerData().getAddressData());
			}
		}
		if (laAddressData != null)
		{
			caSpclPltRegisData.getOwnrData().setAddressData(
				(AddressData) laAddressData);
		}
		// end defect 10447 
	}

	/**
	 * Return attributes for Object fields 
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Return value of JnkIndi
	 * 
	 * @return int
	 */
	public int getJnkIndi()
	{
		return ciJnkIndi;
	}

	/**
	 * Return value of OwnerData
	 * 
	 * @return OwnerData
	 */
	public OwnerData getOwnerData()
	{
		// defect 10112 
		caOwnerData =
			caOwnerData == null ? new OwnerData() : caOwnerData;
		return caOwnerData;
		// end defect 10112 
	}

	/**
	 * Return value of RegData
	 * 
	 * @return RegistrationData
	 */
	public RegistrationData getRegData()
	{
		return caRegData;
	}

	/**
	 * Return int to determine what address update 'type' will be used  
	 * Special Plate 
	 * 
	 * @return int 
	 */
	public int getSpclPltAddrUpdtType()
	{
		int liAddrUpdtType =
			SpecialPlatesConstant.SPCL_PLT_KEEP_ADDRESS;

		if (isSpclPlt())
		{
			// defect 10112
			// Implement AddressData.isPopulated() 
			if (caRegData != null
				&& caRegData.getRenwlMailAddr() != null
				&& caRegData.getRenwlMailAddr().isPopulated())
			{
				if (caRegData
					.getRenwlMailAddr()
					.compareTo(
						caSpclPltRegisData
							.getOwnrData()
							.getAddressData())
					!= 0)
				{
					liAddrUpdtType =
						SpecialPlatesConstant
							.SPCL_PLT_USE_RECIPIENT_ADDRESS;
				}
			}
			else if (
				getOwnerData() != null
					&& getOwnerData().getAddressData() != null)
			{
				if (getOwnerData()
					.getAddressData()
					.compareTo(
						caSpclPltRegisData
							.getOwnrData()
							.getAddressData())
					!= 0)
				{
					// end defect 10112 
					liAddrUpdtType =
						SpecialPlatesConstant
							.SPCL_PLT_USE_OWNER_ADDRESS;
				}
			}
		}
		return liAddrUpdtType;
	}

	/**
	 * Return caSpclPltRegisData
	 * 
	 * @return caSpclPltRegisData
	 */
	public SpecialPlatesRegisData getSpclPltRegisData()
	{
		return caSpclPltRegisData;
	}

	/**
	 * Return value of TitleData
	 * 
	 * @return TitleData
	 */
	public TitleData getTitleData()
	{
		return caTitleData;
	}

	/**
	 * Return value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Return value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Return value of UserSuppliedOwnerData
	 * 
	 * @return OwnerData
	 */

	public OwnerData getUserSuppliedOwnerData()
	{
		return caUserSuppliedOwnerData;
	}

	/**
	 * Return value of Salvage
	 * 
	 * @return Vector
	 */
	public Vector getVctSalvage()
	{
		return cvSalvage;
	}

	/**
	 * Return value of VehicleData
	 * 
	 * @return VehicleData
	 */
	public VehicleData getVehicleData()
	{
		return caVehicleData;
	}

	/**
	 * Is Error on Special Plate Regis
	 * 
	 * @param  aiOfcIssuanceCd int
	 * @param  asTransCd String
	 * 
	 * @return boolean
	 */
	public boolean isAvailableForProcessing(
		int aiOfcIssuanceCd,
		String asTransCd)
	{
		boolean lbAvailable = true;

		if (caRegData.getSpclRegId() != 0)
		{
			if (caSpclPltRegisData == null)
			{
				lbAvailable = false;
			}
			else
			{
				// PlateCd  
				String lsVehRegPltCd = caRegData.getRegPltCd();
				String lsSpclPltRegPltCd =
					caSpclPltRegisData.getRegPltCd();

				//	Plate No 
				String lsVehRegPltNo = caRegData.getRegPltNo();
				String lsSpclPltRegPltNo =
					caSpclPltRegisData.getRegPltNo();

				// RegExpMo
				int liVehRegExpMo = caRegData.getRegExpMo();

				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
				// Refactored liSpclPltRegExpMo to liSpclPltExpMo,  
				//   liSpclPltRegExpYr to liSpclPltExpYr 
				int liSpclPltExpMo = caSpclPltRegisData.getPltExpMo();

				// RegExpYr
				int liVehRegExpYr = caRegData.getRegExpYr();
				int liSpclPltExpYr = caSpclPltRegisData.getPltExpYr();

				if (PlateTypeCache
					.isOutOfScopePlate(
						lsVehRegPltCd,
						aiOfcIssuanceCd,
						RENEWAL_CD)
					|| !lsVehRegPltCd.equals(lsSpclPltRegPltCd)
					|| !lsVehRegPltNo.equals(lsSpclPltRegPltNo))
				{
					lbAvailable = false;
				}
				// defect 9781
				// defect 10714
//				else if (asTransCd.equals(TransCdConstant.IRENEW))
//				{
					// defect 10391					
//					if (PlateTypeCache.isVendorPlate(lsVehRegPltCd))
//					if (!PlateTypeCache.isVendorPlate(lsVehRegPltCd))
//					{
//						lbAvailable =
//							liSpclPltExpYr * 12 + liSpclPltExpMo
//								> liVehRegExpYr * 12 + liVehRegExpMo;
//					}
//					
//					else
//					{
//						end defect 10391
						// && aiOfcIssuanceCd == CommonConstant.INET_OFC_CD
//						if (liVehRegExpMo != liSpclPltExpMo
//							|| liVehRegExpYr != liSpclPltExpYr)
//						{
//							lbAvailable = false;
//						}
//					}
//					// end defect 9781	
//				}
				// end defect 10714
				// end defect 9864 
			}
		}
		return lbAvailable;
	}

	/**
	 * Return value of DoNotBuildMvFunc
	 * 
	 * @return boolean
	 */
	public boolean isDoNotBuildMvFunc()
	{
		return cbDoNotBuildMvFunc;
	}

	/**
	 * Return value of FromMF
	 * 
	 * @return boolean
	 */
	public boolean isFromMF()
	{
		return cbFromMF;
	}

	/**
	 * Return boolean denoting if Eligible for PTO processing
	 * 
	 * @return boolean
	 */
	public boolean isPTOEligible()
	{
		// defect 9524 
		// Use ClassToPltCache to determine if PTOEligible 
		// If >= PTO Start Date &&  
		// (Passenger LE 6000 & PSP  
		//  - OR
		//  Truck LE 1 Ton & TKP)
		//		return RTSDate.getCurrentDate().compareTo(
		//			SystemProperty.getPTOStartDate())
		//			>= 0
		//			&& caRegData != null
		//			&& caRegData.getRegPltCd() != null
		//			&& ((caRegData.getRegClassCd()
		//				== CommonConstant.PASS_LE_6000_REGCLASSCD
		//				&& caRegData.getRegPltCd().equals(
		//					CommonConstant.PASSENGER_PLT))
		//				|| (caRegData.getRegClassCd()
		//					== CommonConstant.TRK_LE_1_TON_REGCLASSCD
		//					&& caRegData.getRegPltCd().equals(
		//						CommonConstant.TRUCK_PLT)));

		return RTSDate.getCurrentDate().compareTo(
			SystemProperty.getPTOStartDate())
			>= 0
			&& ClassToPlateCache.isPTOEligible(
				caRegData.getRegClassCd(),
				caRegData.getRegPltCd());
		// end defect 9524 
	}

	/**
	 * Return boolean denoting if SpclPltRegisData is not null
	 * 
	 * @return boolean
	 */
	public boolean isSpclPlt()
	{
		return caSpclPltRegisData != null;
	}

	/**
	 * Return yes if Special Plate record only was returned form MF
	 * 
	 * @return boolean
	 */
	public boolean isSPRecordOnlyVehInq()
	{
		return cbSPRecordOnly;
	}

	/**
	 * Set value of DoNotBuildMvFunc
	 * 
	 * @param abDoNotBuildMvFunc boolean
	 */
	public void setDoNotBuildMvFunc(boolean abDoNotBuildMvFunc)
	{
		cbDoNotBuildMvFunc = abDoNotBuildMvFunc;
	}

	/**
	 * Set value of FromMF
	 * 
	 * @param abFromMF boolean
	 */
	public void setFromMF(boolean abFromMF)
	{
		cbFromMF = abFromMF;
	}

	/**
	 * Set value of JnkIndi
	 * 
	 * @param aiJnkIndi int
	 */
	public void setJnkIndi(int aiJnkIndi)
	{
		ciJnkIndi = aiJnkIndi;
	}

	/**
	 * Set value of OwnerData
	 * 
	 * @param aaOwnerData OwnerData
	 */
	public void setOwnerData(OwnerData aaOwnerData)
	{
		caOwnerData = aaOwnerData;
	}

	/**
	 * Set value of RegData
	 * 
	 * @param aaRegData RegistrationData
	 */
	public void setRegData(RegistrationData aaRegData)
	{
		caRegData = aaRegData;
	}

	/**
	 * Sets value of caSpclPltRegisData
	 * 
	 * @param aaSpclPltRegis
	 */
	public void setSpclPltRegisData(SpecialPlatesRegisData aaSpclPltRegis)
	{
		caSpclPltRegisData = aaSpclPltRegis;

	}

	/**
	 * Set yes if Special Plate record only was returned form MF
	 * @param abValue
	 */
	public void setSPRecordOnlyVehInq(boolean abValue)
	{
		cbSPRecordOnly = abValue;
	}

	/**
	 * Set value of TitleData
	 * 
	 * @param aaTitleData TitleData
	 */
	public void setTitleData(TitleData aaTitleData)
	{
		caTitleData = aaTitleData;
	}

	/**
	 * Set value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set value of UserSuppliedOwnerData
	 * 
	 * @param aaUserSuppliedOwnerData OwnerData
	 */
	public void setUserSuppliedOwnerData(OwnerData aaUserSuppliedOwnerData)
	{
		caUserSuppliedOwnerData = aaUserSuppliedOwnerData;
	}

	/**
	 * Set value of Salvage
	 * 
	 * @param avSalvage Vector
	 */
	public void setVctSalvage(Vector avSalvage)
	{
		cvSalvage = avSalvage;
	}

	/**
	 * Set value of VehicleData
	 * 
	 * @param aaVehicleData VehicleData
	 */
	public void setVehicleData(VehicleData aaVehicleData)
	{
		caVehicleData = aaVehicleData;
	}
}
