/**
 * 
 */
package com.txdot.isd.rts.services.util;

import java.awt.Graphics;
import java.text.DateFormatSymbols;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * VehicleDataGraphicsHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * B Woodson    11/01/2011  new class
 *                          defect 11052 - VTR-275 Form Project @ POS
 * K Harrell	11/20/2011	add nullSafe(), getMFVehicleData(), 
 * 							 drawSpecialPlateOnly()
 * 							modify drawVehicleInquiry(), 
 * 							 transformIndicators()
 * 							defect 11052 Ver 6.9.0
 * K Harrell	12/07/2011	Handle Record Not Found CTData.ciNoMFRecs
 * 							indicator 
 * 							modify drawVehicleInquiry(), 
 * 							 drawSpecialPlateOnly()
 * 							defect 11169 Ver 6.9.0
 * B Woodson	02/07/2012	add ITEM_CODE_DESC_LENGTH
 * 							modify drawVehicleInquiry()
 * 							defect 11250 Ver 6.10.0
 * B Woodson	03/09/2012	add getMonth(), getShortMonth()
 * 							modify drawVehicleInquiry(),
 *  						 drawSpecialPlateOnly()
 * 							remove getmonth()
 * 							defect 11302 Ver 6.10.0
 * K Harrell	03/16/2012	Update positions for Special Plates Inquiry 
 * 							Correct 3rd Lien Date alignment
 * 							Do not print $0.00 for Sales Price on 
 * 							 No Record Found for Vehicle Inquiry  
 * 							modify drawSpecialPlateOnly(), 
 * 							  drawVehicleInquiry()
 * 							defect 11310 Ver 6.10.0 
 * ---------------------------------------------------------------------                          
 */

/** 
 * encapsulates drawing code 
 * 
 * @version	6.10.0 		03/16/2012
 * @author	Buck Woodson 
 * @Creation Date:		11/01/2011 
 */
public class VehicleDataGraphicsHelper
{
	/**
	 * 
	 */
	private static final int ITEM_CODE_DESC_LENGTH = 27;

	private static final int MAX_FORM_LINE_SIZE = 100;
	
	private static final String ACTIVE = "AC";
	private static final String INACTIVE = "IN";
	private static final String ARCHIVE = "AR";
	private static final String SPECIAL_PLATE = "SP";
	
	private MFVehicleData caMFVehicleData = null;
	private OwnerData caOwnerData;
	private RegistrationData caRegistrationData = null;
	private TitleData caTitleData;
	private VehicleData caVehicleData = null;
	private CompleteTransactionData caCompTransData = null;
	
	/**
	 * draw data to the graphics
	 * @param laGraphics
	 */
	public boolean drawVehicleInquiry(Graphics aaGraphics)
	{		
		if (caMFVehicleData == null ||
				caOwnerData == null ||
				caRegistrationData == null ||
				caTitleData == null ||
				caVehicleData == null)
		{
			return false;
		}

		//1st Row
		aaGraphics.drawString(nullSafe(caVehicleData.getVin()), 177, 335);
		
		if (caVehicleData.getVehModlYr() != 0)
		{
			aaGraphics.drawString(Integer.toString(caVehicleData.getVehModlYr()), 498, 335);
		}
		
		aaGraphics.drawString(nullSafe(caVehicleData.getVehMk()), 594, 335);
		aaGraphics.drawString(nullSafe(caVehicleData.getVehModl()), 699, 335);
		aaGraphics.drawString(nullSafe(caVehicleData.getVehBdyType()), 789, 335);
		aaGraphics.drawString(getVehicleColor(), 925, 335);

		int liIssueDate = caTitleData.getTtlIssueDate();
		if (liIssueDate > 0)
		{
			aaGraphics.drawString(
					new RTSDate(
							RTSDate.YYYYMMDD,
							caTitleData.getTtlIssueDate())
					.toString(), 1047, 335);
		}		

		aaGraphics.drawString(nullSafe(caTitleData.getDocNo()), 1278, 335);

		//2nd row
		aaGraphics.drawString(nullSafe(caRegistrationData.getRegPltNo()), 243, 414);
		
		if (caRegistrationData.getRegIssueDt() !=0)
		{
			aaGraphics.drawString(
					new RTSDate(
							RTSDate.YYYYMMDD,
							caRegistrationData.getRegIssueDt())
					.toString(), 420, 414);
		}
		
		int liExpYr = caRegistrationData.getRegExpYr(); 
		
		if (liExpYr != 0)
		{
			aaGraphics.drawString(getShortMonth(caRegistrationData.getRegExpMo()) + "/" + caRegistrationData.getRegExpYr(), 573, 414);
		}
		else
		{
			aaGraphics.drawString(getShortMonth(caRegistrationData.getRegExpMo()), 573, 414);
		}
		aaGraphics.drawString(nullSafe(caRegistrationData.getPrevPltNo()), 753, 414);
		
		if (caRegistrationData.getPrevExpYr() > 0)
		{
			aaGraphics.drawString(getShortMonth(caRegistrationData.getPrevExpMo()) + "/" + caRegistrationData.getPrevExpYr(), 870, 414);
		}

		aaGraphics.drawString(nullSafe(caVehicleData.getVehClassCd()), 1062, 414);
		if (caRegistrationData.getPltBirthDate() != 0)
		{
			aaGraphics.drawString(Integer.toString(caRegistrationData.getRegPltAge(false)), 1272, 414);
		}
		
		if (caRegistrationData.getCustActlRegFee() != null)
		{
			aaGraphics.drawString(caRegistrationData.getCustActlRegFee().printDollar(true), 1434, 414);
		}

		//3rd row
		aaGraphics.drawString(getRegPltCd(getItmCdDesc(nullSafe(caRegistrationData.getRegPltCd()))), 171, 489);
		if (caRegistrationData.getRegClassCd() !=0)
		{
			aaGraphics.drawString(Integer.toString(caRegistrationData.getRegClassCd()), 576, 489);
		}

		aaGraphics.drawString(getOfcName(caRegistrationData.getResComptCntyNo()), 699, 489);
		if (caVehicleData.getVehEmptyWt() != 0)
		{
			aaGraphics.drawString(Integer.toString(caVehicleData.getVehEmptyWt()), 855, 489);
			aaGraphics.drawString(nullSafe(Integer.toString(caRegistrationData.getVehGrossWt())), 1002, 489);
			aaGraphics.drawString(getVehTon(caVehicleData.getVehTon()), 1143, 489);
		}
		aaGraphics.drawString(nullSafe(caVehicleData.getVehOdmtrReadng()), 1248, 489);
		if (caTitleData.getVehSalesPrice() != null && caTitleData.getVehSalesPrice().compareTo(new Dollar(0))>0)
		{
			aaGraphics.drawString(nullSafe(caTitleData.getVehSalesPrice().printDollar(true)), 1383, 489);
		} 
		// defect 11310
		// Do not print Sales Price on No Record Found
		// Note: # Records is carried in VehicleInquiryData, not (yet) available here
		// Using populated VehClassCd to designate Record Found
		else if (caVehicleData.getVehClassCd()!= null && caVehicleData.getVehClassCd().trim().length()>0)
		{
			// end defect 11310 
			aaGraphics.drawString("$0.00", 1383, 489);
		}

		//Vehicle Owner
		caOwnerData.initWhereNull();
		AddressData laOwnrAddr = caOwnerData.getAddressData();
		aaGraphics.drawString(caOwnerData.getName1(), 381, 600);
		aaGraphics.drawString(caOwnerData.getName2(), 381, 625);
		aaGraphics.drawString(laOwnrAddr.getSt1(),381, 650);
		aaGraphics.drawString(laOwnrAddr.getSt2(), 381, 675);
		aaGraphics.drawString(laOwnrAddr.getCity(), 381, 700);
		aaGraphics.drawString(laOwnrAddr.getState(), 645, 700);
		aaGraphics.drawString(laOwnrAddr.getCntryUSAZpcd(), 690, 700);
		
		// Recipient / Renewal Address
		AddressData laRnwlAddr =caRegistrationData.getRenwlMailAddr();
		laRnwlAddr.initWhereNull();
		aaGraphics.drawString(nullSafe(caRegistrationData.getRecpntName()), 1023, 600);
		aaGraphics.drawString(laRnwlAddr.getSt1(), 1023, 650);
		aaGraphics.drawString(laRnwlAddr.getSt2(), 1023, 675);
		aaGraphics.drawString(laRnwlAddr.getCity(), 1023, 700);
		aaGraphics.drawString(laRnwlAddr.getState(), 1287, 700);
		aaGraphics.drawString(laRnwlAddr.getCntryUSAZpcd(), 1332, 700);

		//Vehicle Owner, 4th row
		aaGraphics.drawString(caOwnerData.getAddressData().getCntry(), 381, 725);
		
		//Vehicle Location Info
		aaGraphics.drawString(nullSafe(caTitleData.getTtlVehAddr().getSt1()), 339, 822);
		aaGraphics.drawString(nullSafe(caTitleData.getTtlVehAddr().getSt2()), 768, 822);
		aaGraphics.drawString(nullSafe(caTitleData.getTtlVehAddr().getCity()), 339, 852);
		aaGraphics.drawString(nullSafe(caTitleData.getTtlVehAddr().getState()), 603, 852);
		aaGraphics.drawString(nullSafe(caTitleData.getTtlVehAddr().getCntryUSAZpcd()), 648, 852);

		//1st Lienholder Info
		LienholderData laLienData =
			caTitleData.getLienholderData(TitleConstant.LIENHLDR1);
		String lsDate = "UNKNOWN";
		AddressData laLienAddr = null; 
		if (laLienData.isPopulated())
		{			
			laLienData.initWhereNull(); 
			aaGraphics.drawString(laLienData.getName1(), 360, 978);
			aaGraphics.drawString(laLienData.getName2(), 360, 1003);
			laLienAddr = laLienData.getAddressData(); 
			aaGraphics.drawString(laLienAddr.getSt1(), 360, 1028);
			aaGraphics.drawString(laLienAddr.getSt2(), 360, 1053);
			
			lsDate =
				new RTSDate(
						RTSDate.YYYYMMDD,
						laLienData.getLienDate())
			.toString();

			aaGraphics.drawString(lsDate, 1332, 978);
			aaGraphics.drawString(laLienAddr.getCity(), 360, 1078);
			aaGraphics.drawString(laLienAddr.getState(), 624, 1078);
			aaGraphics.drawString(laLienAddr.getCntryUSAZpcd(), 669, 1078);
			aaGraphics.drawString(laLienAddr.getCntry(), 360, 1103);
		}		

		//2nd Lienholder Info
		laLienData =
			caTitleData.getLienholderData(TitleConstant.LIENHLDR2);
		lsDate = "UNKNOWN";

		if (laLienData.isPopulated())
		{			
			laLienData.initWhereNull(); 
			aaGraphics.drawString(laLienData.getName1(), 360, 1158);
			aaGraphics.drawString(laLienData.getName2(), 360, 1184);
			laLienAddr = laLienData.getAddressData(); 
			aaGraphics.drawString(laLienAddr.getSt1(), 360, 1210);
			aaGraphics.drawString(laLienAddr.getSt2(), 360, 1236);
			lsDate =
				new RTSDate(
						RTSDate.YYYYMMDD,
						laLienData.getLienDate())
			.toString();

			aaGraphics.drawString(lsDate, 1332, 1158);
			aaGraphics.drawString(laLienAddr.getCity(), 360, 1262);
			aaGraphics.drawString(laLienAddr.getState(), 624, 1262);
			aaGraphics.drawString(laLienAddr.getCntryUSAZpcd(), 669, 1262);
			aaGraphics.drawString(laLienAddr.getCntry(), 360, 1288);
		}	

		//3rd Lienholder Info
		laLienData =
			caTitleData.getLienholderData(TitleConstant.LIENHLDR3);
		lsDate = "UNKNOWN";

		if (laLienData.isPopulated())
		{			
			laLienData.initWhereNull(); 
			aaGraphics.drawString(laLienData.getName1(),360, 1339);
			aaGraphics.drawString(laLienData.getName2(),360, 1365);
			
			laLienAddr = laLienData.getAddressData(); 
			aaGraphics.drawString(laLienAddr.getSt1(), 360, 1391);
			aaGraphics.drawString(laLienAddr.getSt2(), 360, 1417);
			lsDate =
				new RTSDate(
						RTSDate.YYYYMMDD,
						laLienData.getLienDate())
			.toString();

			// defect 11310
			aaGraphics.drawString(lsDate, 1332, 1339);
			// end defect 11310 
			
			aaGraphics.drawString(laLienAddr.getCity(), 360, 1443);
			aaGraphics.drawString(laLienAddr.getState(), 585, 1443);
			aaGraphics.drawString(laLienAddr.getCntryUSAZpcd(), 693, 1443);
			aaGraphics.drawString(laLienAddr.getCntry(), 360, 1469);
		}	

		//previous owner info
		aaGraphics.drawString(nullSafe(caTitleData.getPrevOwnrName()), 360, 1594);
		aaGraphics.drawString(nullSafe(caTitleData.getPrevOwnrCity()), 828, 1594);
		aaGraphics.drawString(nullSafe(caTitleData.getPrevOwnrState()), 1098, 1594);

		//remarks
		Vector lvIndis =
			IndicatorLookup.getIndicators(
					caMFVehicleData,
					TransCdConstant.VEHINQ,
					IndicatorLookup.VTR275); 
		Vector lvTransformedIndis = transformIndicators(lvIndis);
		if (lvTransformedIndis != null) 
		{
			if (lvTransformedIndis.size() >= 1)
			{
				aaGraphics.drawString(nullSafe((String)lvTransformedIndis.get(0)), 168, 1695);
			}
			if (lvTransformedIndis.size() >= 2)
			{
				aaGraphics.drawString(nullSafe((String)lvTransformedIndis.get(1)), 168, 1721);
			}
			if (lvTransformedIndis.size() >= 3)
			{
				aaGraphics.drawString(nullSafe((String)lvTransformedIndis.get(2)), 168, 1747);
			}
			if (lvTransformedIndis.size() >= 4)
			{
				aaGraphics.drawString(nullSafe((String)lvTransformedIndis.get(3)), 168, 1773);
			}
			if (lvTransformedIndis.size() >= 5)
			{
				aaGraphics.drawString(nullSafe((String)lvTransformedIndis.get(4)), 168, 1799);
			}
		}

		//final integration transaction data
		if (caCompTransData != null)
		{
			aaGraphics.drawString(
					new RTSDate().getMFLongDate(), 363, 2010);
			aaGraphics.drawString(nullSafe(SystemProperty.getCurrentEmpId()), 912, 2010);
			aaGraphics.drawString(nullSafe(Integer.toString(SystemProperty.getOfficeIssuanceNo())), 1026, 2010);
			aaGraphics.drawString(nullSafe(caCompTransData.getTransId()), 1281, 2010);
			
			// defect 11169 
			// if (caVehicleData.getVin() == null || caVehicleData.getVin().length() == 0)
			if (caCompTransData.getNoMFRecs() ==0)
			{
				// end defect 11169 
				//aaGraphics.drawString("x", 165, 1902);
				aaGraphics.drawString("x", 165, 1980);
			}
			else
			{
				aaGraphics.drawString(nullSafe(getSearchRecordMnemonic()), 1089, 2010);
				aaGraphics.drawString("x", 165, 1905);
			}
		}

		return true;
	}
	/**
	 * draw data to the graphics
	 * @param laGraphics
	 */
	public boolean drawSpecialPlateOnly(Graphics aaGraphics)
	{		

		if (caMFVehicleData == null ||
				caMFVehicleData.getSpclPltRegisData() == null)
		{
			return false;
		}
		SpecialPlatesRegisData laSpclPltRegisData = caMFVehicleData.getSpclPltRegisData();
		caOwnerData = laSpclPltRegisData.getOwnrData(); 
		caOwnerData.initWhereNull(); 
		
		//1st Row
		if (!UtilityMethods.isAllZeros(laSpclPltRegisData.getSpclDocNo()))
		{
			aaGraphics.drawString(laSpclPltRegisData.getSpclDocNo(), 1278, 335);
		}
		
		//2nd row
		// defect 11310 
		// Adjust for new Format 
		
		//aaGraphics.drawString(laSpclPltRegisData.getRegPltNo(), 186, 414);
		aaGraphics.drawString(laSpclPltRegisData.getRegPltNo(), 243, 414);
		if (caCompTransData.getNoMFRecs() != 0)
		{
			aaGraphics.drawString(getShortMonth(laSpclPltRegisData.getPltExpMo()) + "/"
				//	+ laSpclPltRegisData.getPltExpYr(), 432, 414);
					+ laSpclPltRegisData.getPltExpYr(), 573, 414);
		}
		if (laSpclPltRegisData.getPltBirthDate() != 0)
		{
			//aaGraphics.drawString(Integer.toString(laSpclPltRegisData.getRegPltAge(false)), 1428, 414);
			aaGraphics.drawString(Integer.toString(laSpclPltRegisData.getRegPltAge(false)), 1272, 414);
		}
		
		//3rd row
		//aaGraphics.drawString(getRegPltCd(getItmCdDesc(laSpclPltRegisData.getRegPltCd())), 171, 495);
		aaGraphics.drawString(getRegPltCd(getItmCdDesc(laSpclPltRegisData.getRegPltCd())), 171, 489);
		
		//aaGraphics.drawString(getOfcName(laSpclPltRegisData.getResComptCntyNo()), 645, 495);
		aaGraphics.drawString(getOfcName(laSpclPltRegisData.getResComptCntyNo()), 699, 489);
		
		//Special Plate Owner
		caOwnerData.initWhereNull();
		AddressData laOwnrAddr = caOwnerData.getAddressData();
//		aaGraphics.drawString(caOwnerData.getName1(), 381, 638);
//		aaGraphics.drawString(caOwnerData.getName2(), 381, 663);
//		aaGraphics.drawString(laOwnrAddr.getSt1(),381, 723);
//		aaGraphics.drawString(laOwnrAddr.getSt2(), 381, 748);
//		aaGraphics.drawString(laOwnrAddr.getCity(), 246, 801);
//		aaGraphics.drawString(laOwnrAddr.getState(), 582, 801);
//		aaGraphics.drawString(laOwnrAddr.getCntryUSAZpcd(), 690, 801);
//		aaGraphics.drawString(caOwnerData.getAddressData().getCntry(), 291, 858);
		
		aaGraphics.drawString(caOwnerData.getName1(), 381, 600);
		aaGraphics.drawString(caOwnerData.getName2(), 381, 625);
		aaGraphics.drawString(laOwnrAddr.getSt1(),381, 650);
		aaGraphics.drawString(laOwnrAddr.getSt2(), 381, 675);
		aaGraphics.drawString(laOwnrAddr.getCity(), 381, 700);
		aaGraphics.drawString(laOwnrAddr.getState(), 645, 700);
		aaGraphics.drawString(laOwnrAddr.getCntryUSAZpcd(), 690, 700);
		//Vehicle Owner, 4th row
		aaGraphics.drawString(caOwnerData.getAddressData().getCntry(), 381, 725);

		if (caCompTransData != null)
		{
			aaGraphics.drawString(
				//	new RTSDate().getMFLongDate(), 363, 1935);
					new RTSDate().getMFLongDate(), 363, 2010);
			
			//aaGraphics.drawString(nullSafe(SystemProperty.getCurrentEmpId()), 912, 1935);
			aaGraphics.drawString(nullSafe(SystemProperty.getCurrentEmpId()), 912, 2010);
			
			//aaGraphics.drawString(nullSafe(Integer.toString(SystemProperty.getOfficeIssuanceNo())), 1026, 1935);
			aaGraphics.drawString(nullSafe(Integer.toString(SystemProperty.getOfficeIssuanceNo())), 1026, 2010);
			
			if (caCompTransData.getNoMFRecs() ==0)
			{
				// aaGraphics.drawString("x", 165, 1902);
				aaGraphics.drawString("x", 165, 1980);
			}
			else
			{
				//aaGraphics.drawString(nullSafe(getSearchRecordMnemonic()), 1089, 1935);
				//aaGraphics.drawString("x", 165, 1827);
				aaGraphics.drawString(nullSafe(getSearchRecordMnemonic()), 1089, 2010);	
				aaGraphics.drawString("x", 165, 1905);
			}

			//aaGraphics.drawString(nullSafe(caCompTransData.getTransId()), 1281, 1935);
			aaGraphics.drawString(nullSafe(caCompTransData.getTransId()), 1281, 2010);
			// end defect 11310 
		}
		return true;
	}


	/**
	 * 
	 * @return String
	 */
	private String getSearchRecordMnemonic()
	{
		if (caMFVehicleData.isSPRecordOnlyVehInq())
		{
			return SPECIAL_PLATE;
		}
		else if (caRegistrationData.getFileTierCd() == 0)
		{
			return ACTIVE;
		}
		else if (caRegistrationData.getFileTierCd() == 1)
		{
			return INACTIVE;
		}	
		else if (caRegistrationData.getFileTierCd() == 2)
		{
			return ARCHIVE;
		}
		return null;
	}

	/**
	 * @param asPltCdDesc
	 * @return
	 */
	private String getRegPltCd(String asPltCdDesc)
	{
		if (asPltCdDesc.length() > ITEM_CODE_DESC_LENGTH)
		{
			return asPltCdDesc.substring(0, ITEM_CODE_DESC_LENGTH);
		}
		if (asPltCdDesc == null || asPltCdDesc.length() == 0)
		{
			return "";
		}
		else
		{
			return asPltCdDesc;
		}
	}

	/**
	 * @param avIndis
	 * @return
	 */
	private Vector transformIndicators(Vector avIndis)
	{
//		if ((avIndis == null || avIndis.size() == 0) 
//				&& caRegistrationData.getCancPltIndi() ==0)
//		{
//			return null;
//		}
		
		if (caTitleData.getDocTypeCd() > 1)
		{
			addDocTypeCdDesc(avIndis, caTitleData.getDocTypeCd());
		}
		
		if (caTitleData.getDocTypeCd() > 0 && caRegistrationData.getRegClassCd() == 0)
		{
			addRemark(avIndis, "REGISTRATION DATA UNAVAILABLE");
		}
		
		if (caMFVehicleData.isSpclPlt())
		{
			addRemark(avIndis, "SPECIAL PLATE");
		}
		
		Vector lvTransformedIndis = new Vector();
		StringBuffer laIndiLine = new StringBuffer();
		if (caRegistrationData.getCancPltIndi() == 1)
		{
			laIndiLine.append("PLATE CANCELED.");
		}
		boolean lbSeparator = true;  
		
		for (Iterator iter = avIndis.iterator(); iter
				.hasNext();)
		{
			IndicatorData laIndicator = (IndicatorData) iter.next();
			boolean lbNewSeparator = laIndicator.getDesc().equals("."); 
			if (lbNewSeparator)
			{
				if (lbSeparator)
				{
					continue; 
				}
			}
			lbSeparator = lbNewSeparator;
			
			if ((laIndiLine.length() + laIndicator.getDesc().length()) > MAX_FORM_LINE_SIZE)
			{
				lvTransformedIndis.add(laIndiLine.toString());
				laIndiLine = new StringBuffer();
				laIndiLine.append(laIndicator.getDesc());
				continue;
			}
			laIndiLine.append(laIndicator.getDesc());		
		}
		
		lvTransformedIndis.add(laIndiLine.toString());
		
		return lvTransformedIndis;
	}

	/**
	 * @param avIndis
	 * @param asRemark
	 */
	private void addRemark(Vector avIndis, String asRemark)
	{
		IndicatorData laIndicatorData = new IndicatorData();
		laIndicatorData.setDesc(asRemark);
		avIndis.add(laIndicatorData);
		laIndicatorData = new IndicatorData();
		laIndicatorData.setDesc(".");
		avIndis.add(laIndicatorData);
		
	}
	/**
	 * @param avIndis 
	 * @param docTypeCd
	 * @return
	 */
	private void addDocTypeCdDesc(Vector avIndis, int docTypeCd)
	{
		DocumentTypesData laDocumentTypesData = DocumentTypesCache.getDocType(docTypeCd);
		if (laDocumentTypesData != null)
		{
			IndicatorData laIndicatorData = new IndicatorData();
			laIndicatorData.setDesc(laDocumentTypesData.getDocTypeCdDesc());
			avIndis.add(laIndicatorData);
			laIndicatorData = new IndicatorData();
			laIndicatorData.setDesc(".");
			avIndis.add(laIndicatorData);
		}

	}
	/**
	 * @param aaTon
	 * @return
	 */
	private String getVehTon(Dollar aaTon)
	{
		if (aaTon == null)
		{
			return "0.00";
		}
		else
		{
			return aaTon.printDollar(false);
		}
	}

	/**
	 * @param aiOfcNo
	 * @return
	 */
	private String getOfcName(int aiOfcNo)
	{
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(aiOfcNo);
		if (laOfficeIdsData != null)
		{
			return laOfficeIdsData.getOfcName();
		}
		return "";
	}

	/**
	 * @param asRegPltCd
	 * @return
	 */
	private String getItmCdDesc(String asRegPltCd)
	{
		ItemCodesData laItemCodesData =
			ItemCodesCache.getItmCd(asRegPltCd);
		if (laItemCodesData != null)
		{
			return laItemCodesData.getItmCdDesc();
		}
		else
		{
			return "";
		}
	}

	/**
	 * @param aiMonth
	 * @return
	 */
	private String getMonth(int aiMonth)
	{
		String lsMonth = new String(); 
		if (aiMonth != 0)
		{
			lsMonth = new DateFormatSymbols().getMonths()[aiMonth -1];
		}
		return lsMonth; 
	}
	
	/**
	 * @param aiMonth
	 * @return
	 */
	private String getShortMonth(int aiMonth)
	{
		String lsMonth = new String(); 
		if (aiMonth != 0)
		{
			lsMonth = new DateFormatSymbols().getShortMonths()[aiMonth -1].toUpperCase();
		}
		return lsMonth; 
	}

	/**
	 * @return
	 */
	private String getVehicleColor()
	{
		String lsMajor = caVehicleData.getVehMjrColorCd();
		String lsMinor = caVehicleData.getVehMnrColorCd();
		
		if (lsMajor == null || lsMajor.length() == 0)
		{
			return "";
		}
		else if (lsMinor == null || lsMinor.length() == 0)
		{
			return lsMajor;
		}
		else
		{
			return lsMajor + "/" + lsMinor;
		}
	}

	public VehicleDataGraphicsHelper (CompleteTransactionData aaCompTransData)
	{
		this(aaCompTransData.getVehicleInfo());
		caCompTransData = aaCompTransData;

	}
	
	/**
	 * @param aaMFVehicleData
	 */
	public VehicleDataGraphicsHelper(MFVehicleData aaMFVehicleData)
	{
		super();
		caMFVehicleData = aaMFVehicleData;
		caTitleData = caMFVehicleData.getTitleData();
		caOwnerData = caMFVehicleData.getOwnerData();
		caRegistrationData = caMFVehicleData.getRegData();
		caVehicleData = caMFVehicleData.getVehicleData();
	}

	/**
	 * @return the caMFVehicleData
	 */
	public MFVehicleData getMFVehicleData()
	{
		return caMFVehicleData;
	}

	private String nullSafe(String asInput)
	{
		return UtilityMethods.nullSafe(asInput);
	}

}
