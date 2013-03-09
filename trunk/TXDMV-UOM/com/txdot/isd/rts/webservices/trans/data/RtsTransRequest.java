package com.txdot.isd.rts.webservices.trans.data;

import java.util.Calendar;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;
import com.txdot.isd.rts.webservices.common.data.RtsAddress;

/*
 * RtsTransRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/01/2008	Created class.
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	06/04/2008	Add PltTerm, PltExpMo, and PltExpYr so MPI
 * 							can let us know how long this plate is 
 * 							valid.
 * 							add ciPltExpMo, ciPltExpMo, ciPltExpYr
 * 							add getPltExpMo(), getPltExpYr(), getPltTerm(),
 * 								setPltExpMo(), setPltExpYr(), setPltTerm()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	06/25/2008	Use SystemProperty to set the office and 
 * 							wsid for inv.
 * 							modify buildIAD()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/01/2008	If orgNo is empty or zero, set it to blank.
 * 							modify setOrgNo()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/12/2008	Add a routine to to a basic check of the 
 * 							data sent from VendorPlates.
 * 							add validateTransRequest()
 * 							defect 9680 Ver MyPlates_POS
 * Ray Rowehl	07/17/2008	update basic edit routine.
 * 							modify validateTransRequest()
 * 							defect 9680 Ver MyPlates_POS
 * K Harrell	07/12/2009	implement new OwnerData()
 * 							modify buildSPITD()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	02/09/2010	Implement ciPltValidityTerm vs. ciPltTerm
 * 							add ciPltValidityTerm, get/set methods
 * 							delete ciPltTerm, get/set methods
 * 							defect 10366 Ver POS_640  	
 * ---------------------------------------------------------------------
 */

/**
 * Transaction data submitted to POS for creating SPAPP_TRANS data.
 *
 * @version	POS_640  		02/09/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		06/01/2008 12:04:01
 */

public class RtsTransRequest extends RtsAbstractRequest
{
	private Calendar caEpayRcveTimeStmp;
	private Calendar caEpaySendTimeStmp;
	private Calendar caInitReqTimeStmp;
	private RtsAddress caPltOwnrAddr;
	private boolean cbFromReserveIndi;
	private boolean cbIsaIndi;
	private boolean cbPlpIndi;
	private double cdPymntAmt;
	private int ciAddlSetIndi;
	private int ciItrntPymntStatusCd;
	private int ciPltExpMo;
	private int ciPltExpYr;
	// defect 10366 
	//private int ciPltTerm;
	private int ciPltValidityTerm; 
	// end defect 10366
	private int ciResComptCntyNo;
	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csOrgNo;
	private String csPltCd;
	private String csPltNo;
	private String csPltOwnrEmailAddr;
	private String csPltOwnrName1;
	private String csPltOwnrName2;
	private String csPltOwnrPhone;
	private String csPymntOrderId;

	/**
	 * Build the FeesData component for VP Processing.
	 * 
	 * @return FeesData
	 */
	public Vector buildFees()
	{
		FeesData laFeesData = null;
		Vector lvReturnVec = null;

		OrganizationNumberData laOrgNoData =
			OrganizationNumberCache.getOrgNo(
				getPltCd(),
				getOrgNo(),
				new RTSDate().getYYYYMMDDDate());

		if (laOrgNoData != null)
		{
			laFeesData = new FeesData();
			laFeesData.setAcctItmCd(laOrgNoData.getAcctItmCd());
			laFeesData.setItemPrice(new Dollar(cdPymntAmt));
			lvReturnVec = new Vector(1);
			lvReturnVec.add(laFeesData);
		}

		return lvReturnVec;
	}

	/**
	 * Build the InventoryAllocationData for working with Inventory.
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData buildIAD()
	{
		InventoryAllocationData laIAD = new InventoryAllocationData();

		laIAD.setItmCd(csPltCd);
		laIAD.setInvItmNo(csPltNo);
		laIAD.setInvItmEndNo(csPltNo);
		laIAD.setInvQty(1);
		laIAD.setInvItmYr(0);
		laIAD.setISA(cbIsaIndi);
		laIAD.setUserPltNo(cbPlpIndi);
		laIAD.setMfgPltNo(csMfgPltNo);
		laIAD.setItrntReq(true);
		laIAD.setOfcIssuanceNo(SystemProperty.getVpOfcIssuanceNo());
		laIAD.setTransWsId(SystemProperty.getVpWsId());

		if (getCaller() != null && getCaller().length() > 7)
		{
			laIAD.setTransEmpId(getCaller().substring(0, 7));
		}
		else
		{
			laIAD.setTransEmpId(getCaller());
		}

		String lsRIP = getSessionId();
		if (lsRIP.length() > 15)
		{
			lsRIP = lsRIP.substring(lsRIP.length() - 15);
		}
		laIAD.setRequestorIpAddress(lsRIP);

		laIAD.setRequestorRegPltNo(CommonConstant.STR_SPACE_EMPTY);
		laIAD.setTransAmDate(new RTSDate().getAMDate());

		try
		{
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction laTransactionEngine =
				new com.txdot.isd.rts.services.common.Transaction(
					TransCdConstant.IAPPL);
			laIAD.setTransTime(laTransactionEngine.getTransTime());
		}
		catch (RTSException aeRTSEx)
		{
			// just use the time right now!
			laIAD.setTransTime(new RTSDate().get24HrTime());
		}

		return laIAD;
	}

	/**
	 * Build SpecialPlateItrntTransData out of the request data.
	 * 
	 * @return SpecialPlateItrntTransData
	 */
	public SpecialPlateItrntTransData buildSPITD()
	{
		SpecialPlateItrntTransData laSPITD =
			new SpecialPlateItrntTransData();

		laSPITD.setEpaySendTimeStmp(
			new RTSDate(caEpaySendTimeStmp.getTimeInMillis()));
		laSPITD.setEpayRcveTimeStmp(
			new RTSDate(caEpayRcveTimeStmp.getTimeInMillis()));
		laSPITD.setInitReqTimeStmp(
			new RTSDate(caInitReqTimeStmp.getTimeInMillis()));
		laSPITD.setUpdtTimeStmp(new RTSDate());

		if (laSPITD.getOwnerData() == null)
		{
			laSPITD.setOwnerData(new OwnerData());
		}
		if (laSPITD.getOwnerData().getAddressData() == null)
		{
			laSPITD.getOwnerData().setAddressData(new AddressData());
		}
		// defect 10112 
		laSPITD.getOwnerData().setName1(csPltOwnrName1);
		laSPITD.getOwnerData().setName2(csPltOwnrName2);
		laSPITD.getOwnerData().getAddressData().setSt1(
			caPltOwnrAddr.getStreetLine1());
		laSPITD.getOwnerData().getAddressData().setSt2(
			caPltOwnrAddr.getStreetLine2());
		laSPITD.getOwnerData().getAddressData().setCity(
			caPltOwnrAddr.getCity());
		laSPITD.getOwnerData().getAddressData().setState(
			caPltOwnrAddr.getState());
		laSPITD.getOwnerData().getAddressData().setZpcd(
			caPltOwnrAddr.getZip());
		laSPITD.getOwnerData().getAddressData().setZpcdp4(
			caPltOwnrAddr.getZipP4());
		// end defect 10112 
		
		laSPITD.setPltOwnrEmail(csPltOwnrEmailAddr);
		laSPITD.setPltOwnrPhone(csPltOwnrPhone);

		if (isFromReserve())
		{
			laSPITD.setFromReserveIndi(1);
		}
		else
		{
			laSPITD.setFromReserveIndi(0);
		}
		if (isIsa())
		{
			laSPITD.setISAIndi(1);
		}
		else
		{
			laSPITD.setISAIndi(0);
		}
		if (isPlp())
		{
			laSPITD.setPLPIndi(1);
		}
		else
		{
			laSPITD.setPLPIndi(0);
		}

		laSPITD.setPymntAmt(cdPymntAmt);
		laSPITD.setAddlSetIndi(ciAddlSetIndi);
		laSPITD.setItrntPymntStatusCd(ciItrntPymntStatusCd);
		// defect 10366
		//laSPITD.setPltTerm(ciPltTerm);
		laSPITD.setPltValidityTerm(ciPltValidityTerm);
		// end defect 10366 
		laSPITD.setResComptCntyNo(ciResComptCntyNo);
		laSPITD.setItrntTraceNo(csItrntTraceNo);
		laSPITD.setMfgPltNo(csMfgPltNo);
		laSPITD.setOrgNo(csOrgNo);
		laSPITD.setRegPltCd(csPltCd);
		laSPITD.setRegPltNo(csPltNo);

		laSPITD.setPymntOrderId(csPymntOrderId);
		laSPITD.setPltExpMo(ciPltExpMo);
		laSPITD.setPltExpYr(ciPltExpYr);
		if (getCaller() != null && getCaller().length() > 7)
		{
			laSPITD.setTransEmpID(getCaller().substring(0, 7));
		}
		else
		{
			laSPITD.setTransEmpID(getCaller());
		}

		laSPITD.setTransStatusCd("P");

		String lsRIP = getSessionId();
		if (lsRIP.length() > 15)
		{
			lsRIP = lsRIP.substring(lsRIP.length() - 15);
		}
		laSPITD.setReqIPAddr(lsRIP);

		return laSPITD;
	}

	/**
	 * Returns the Additional Set Indicator.
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Returns the Epay Receive TimeStamp.
	 * 
	 * @return Calendar
	 */
	public java.util.Calendar getEpayRcveTimeStmp()
	{
		return caEpayRcveTimeStmp;
	}

	/**
	 * Returns the Epay Send TimeStamp.
	 * 
	 * @return Calendar
	 */
	public java.util.Calendar getEpaySendTimeStmp()
	{
		return caEpaySendTimeStmp;
	}

	/**
	 * Get the Initial Request Start TimeStamp.
	 * 
	 * @return Calendar
	 */
	public java.util.Calendar getInitReqTimeStmp()
	{
		return caInitReqTimeStmp;
	}

	/**
	 * Returns the Payment Status Code
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciItrntPymntStatusCd;
	}

	/**
	 * Return the Trace Number for this transaction request.
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Return the Manufacturing Plate Number for this transaction request.
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Returns the Organization Number for the Plate.
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Returns the Plate Code of this transaction request.
	 * 
	 * @return String
	 */
	public String getPltCd()
	{
		return csPltCd;
	}

	/**
	 * Get the Plate's Expiration Month.
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Get the Plate's Expiration Year.
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * Returns the Plate Number of the transaction request.
	 * 
	 * @return String
	 */
	public String getPltNo()
	{
		return csPltNo;
	}

	/**
	 * Return Plate Owner Address.
	 * 
	 * @return RtsAddress
	 */
	public RtsAddress getPltOwnrAddr()
	{
		return caPltOwnrAddr;
	}

	/**
	 * Returns the Plate Owner Email Address.
	 * 
	 * @return String
	 */
	public String getPltOwnrEmailAddr()
	{
		return csPltOwnrEmailAddr;
	}

	/**
	 * Returns Line 1 of the Plate Owner Name.
	 * 
	 * @return String
	 */
	public String getPltOwnrName1()
	{
		return csPltOwnrName1;
	}

	/**
	 * Returns Line 2 of the Plate Owner Name.
	 * 
	 * @return String
	 */
	public String getPltOwnrName2()
	{
		return csPltOwnrName2;
	}

	/**
	 * Returns Phone Number of the Plate Owner.
	 * 
	 * @return String
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
	}

	/**
	 * Get the Plate Validity Term value.
	 * 
	 * <p>This is number of years the plate is good for.
	 * 
	 * @return int
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}

	/**
	 * Returns the Payment Amount.
	 * 
	 * @return double
	 */
	public double getPymntAmt()
	{
		return cdPymntAmt;
	}

	/**
	 * Returns the Payment Order Id for this transaction request.
	 * 
	 * @return String
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}

	/**
	 * Return the Plate Owner's Comptroller County Number of Residence.
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Returns indication if we are issuing from Reserved.
	 * 
	 * @return boolean
	 */
	public boolean isFromReserve()
	{
		return cbFromReserveIndi;
	}

	/**
	 * Returns indication of plate contains ISA.
	 * 
	 * @return boolean
	 */
	public boolean isIsa()
	{
		return cbIsaIndi;
	}

	/**
	 * Checks to see if boolean for PLP is set.
	 * 
	 * @return boolean
	 */
	public boolean isPlp()
	{
		return cbPlpIndi;
	}

	/**
	 * Sets the Additional Set Indicator.
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

//	/**
//	 * Set the Plate's Expiration Year.
//	 * 
//	 * @param aiPltExpYr
//	 */
//	public void setCiPltExpYr(int aiPltExpYr)
//	{
//		ciPltExpYr = aiPltExpYr;
//	}

	/**
	 * Sets the Epay Receive TimeStamp.
	 * 
	 * @param aaEpayRcveTimeStmp
	 */
	public void setEpayRcveTimeStmp(
		java.util.Calendar aaEpayRcveTimeStmp)
	{
		caEpayRcveTimeStmp = aaEpayRcveTimeStmp;
	}

	/**
	 * Sets the Epay Send TimeStamp.
	 * 
	 * @param aaEpaySendTimeStmp
	 */
	public void setEpaySendTimeStmp(
		java.util.Calendar aaEpaySendTimeStmp)
	{
		caEpaySendTimeStmp = aaEpaySendTimeStmp;
	}

	/**
	 * Set the Initial Request Start TimeStamp.
	 * 
	 * @param aaInitReqTimeStmp
	 */
	public void setInitReqTimeStmp(
		java.util.Calendar aaInitReqTimeStmp)
	{
		caInitReqTimeStmp = aaInitReqTimeStmp;
	}

	/**
	 * Sets the indicator for if the plate number has an ISA.
	 * 
	 * @param abIsaIndi
	 */
	public void setIsa(boolean abIsaIndi)
	{
		cbIsaIndi = abIsaIndi;
	}

	/**
	 * Set the Internet Payment Status Code.
	 * 
	 * @param aiItrntPymntStatusCd
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		ciItrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * Set the Internet Trace Number.
	 * 
	 * @param asItrntTraceNo
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Set the plate's Manufacturing Plate Number.
	 * 
	 * @param asMfgPltNo
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Set the Organization Number.
	 * 
	 * <p>An empty or 0 orgNo results in a blank.
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		if (asOrgNo == null
			|| asOrgNo.length() < 1
			|| asOrgNo.equals("0")
			|| asOrgNo.equals(" "))
		{
			csOrgNo = " ";
		}
		else
		{
			csOrgNo = asOrgNo;
		}
	}

	/**
	 * Sets the PLP Indicator
	 * 
	 * @param abPlpIndi
	 */
	public void setPlp(boolean abPlpIndi)
	{
		cbPlpIndi = abPlpIndi;
	}

	/**
	 * Set the Plate Code for this plate.
	 * 
	 * @param asPltCd
	 */
	public void setPltCd(String asPltCd)
	{
		csPltCd = asPltCd;
	}

	/**
	 * Set the Plate's Expiration Month.
	 * 
	 * @param aiPltExpMo
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set the Plate's Expiration Year.
	 * 
	 * @param aiPltExpYr
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Set the Plate Number for this transaction request.
	 * 
	 * @param asPltNo
	 */
	public void setPltNo(String asPltNo)
	{
		csPltNo = asPltNo;
	}

	/**
	 * Sets the Plate Owner Address.
	 * 
	 * @param aaPltOwnrAddr
	 */
	public void setPltOwnrAddr(RtsAddress aaPltOwnrAddr)
	{
		caPltOwnrAddr = aaPltOwnrAddr;
	}

	/**
	 * Set the Plate Owner's Email Address.
	 * 
	 * @param asPltOwnrEmailAddr
	 */
	public void setPltOwnrEmailAddr(String asPltOwnrEmailAddr)
	{
		csPltOwnrEmailAddr = asPltOwnrEmailAddr;
	}

	/**
	 * Sets Line 1 of the Plate Owner Name.
	 * 
	 * @param asPltOwnrName1
	 */
	public void setPltOwnrName1(String asPltOwnrName1)
	{
		csPltOwnrName1 = asPltOwnrName1;
	}

	/**
	 * Sets Line 2 of the Plate Owner Name.
	 * 
	 * @param asPltOwnrName2
	 */
	public void setPltOwnrName2(String asPltOwnrName2)
	{
		csPltOwnrName2 = asPltOwnrName2;
	}

	/**
	 * Method description
	 * 
	 * @param asPltOwnrPhone
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
	}

	/**
	 * Set the Plate Validity Term value.
	 * 
	 * <p>This is number of years the plate is good for.
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	/**
	 * Sets the Payment Amount for this transaction request.
	 * 
	 * @param adPymntAmt
	 */
	public void setPymntAmt(double adPymntAmt)
	{
		cdPymntAmt = adPymntAmt;
	}

	/**
	 * Set the Payment Order Id for this transaction request.
	 * 
	 * @param asPymntOrderId
	 */
	public void setPymntOrderId(String asPymntOrderId)
	{
		csPymntOrderId = asPymntOrderId;
	}

	/**
	 * Set the Plate Owner's Comptroller County Number of Residence.
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Sets the indicator for if the plate number is from Reserve.
	 * 
	 * @param abFromReserveIndi
	 */
	public void setFromReserve(boolean abFromReserveIndi)
	{
		cbFromReserveIndi = abFromReserveIndi;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateTransRequest() throws RTSException
	{
		// make sure there is a CallerId
		if (getCaller() == null || getCaller().length() < 1)
		{
			throw new RTSException(150);
		}

		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			throw new RTSException(150);
		}

		// PLP Indi is always set for VP
		if (!cbPlpIndi)
		{
			throw new RTSException(150);
		}

		if (csPltCd == null || csPltCd.length() < 1)
		{
			throw new RTSException(150);
		}

		if (csPltNo == null
			|| csPltNo.length() < 1
			|| csPltNo.length() > 7)
		{
			throw new RTSException(150);
		}

		if (csMfgPltNo == null
			|| csMfgPltNo.length() < 1
			|| csMfgPltNo.length() > 8)
		{
			throw new RTSException(150);
		}

		if (csPltOwnrName1 == null
			|| csPltOwnrName1.length() < 1
			|| csPltOwnrName1.length() > 30)
		{
			throw new RTSException(150);
		}

		if (csPltOwnrName2 == null || csPltOwnrName2.length() > 30)
		{
			throw new RTSException(150);
		}

		if (caPltOwnrAddr == null
			|| caPltOwnrAddr.getStreetLine1() == null
			|| caPltOwnrAddr.getStreetLine1().length() < 1
			|| caPltOwnrAddr.getStreetLine1().length() > 30)
		{
			throw new RTSException(150);
		}

		if (caPltOwnrAddr.getStreetLine2() == null
			|| caPltOwnrAddr.getStreetLine2().length() > 30)
		{
			throw new RTSException(150);
		}

		if (caPltOwnrAddr.getCity() == null
			|| caPltOwnrAddr.getCity().length() < 1
			|| caPltOwnrAddr.getCity().length() > 19)
		{
			throw new RTSException(150);
		}

		if (csPltOwnrEmailAddr != null
			&& csPltOwnrEmailAddr.length() > 50)
		{
			throw new RTSException(150);
		}

		if (caPltOwnrAddr.getState() == null
			|| caPltOwnrAddr.getState().length() < 2
			|| caPltOwnrAddr.getState().length() > 2)
		{
			throw new RTSException(150);
		}

		if (ciResComptCntyNo < 1 || ciResComptCntyNo > 254)
		{
			throw new RTSException(150);
		}

		if (csPymntOrderId != null && csPymntOrderId.length() > 8)
		{
			throw new RTSException(150);
		}

		if (csPltOwnrPhone != null && csPltOwnrPhone.length() > 10)
		{
			throw new RTSException(150);
		}
	}
}
