package com.txdot.isd.rts.webservices.trans.data;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.cache.PlateSurchargeCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;
import com.txdot.isd.rts.webservices.common.data.RtsAddress;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;

/*
 * RtsTransRequestV1.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/01/2008	Created V0 class.
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
 * Ray Rowehl	03/15/2010	Created from version 0 of this class.
 * 							Updated to use BigDecimal for payment amount.
 * 							Added cbAuctnPltIndi with getter and setter.
 * 							Added cbMktngAllowdIndi with getter and 
 * 							setter.
 * 							Added clSpclRegId with getter and setter.
 * 							Added csReservReasnCd with getter and 
 * 							setter.
 * 							Use CommonValidations.isValidState to 
 * 							validate code. 
 * 							organize imports to remove unused imports.
 * 							add caPymntAmt, cbMktngAllowdIndi, clSpclRegId, 
 * 								csReservReasnCd
 * 							add getSRId(), setSRId()
 * 							delete caEpayRcveTimeStmp, caEpaySendTimeStmp, 
 * 								caInitReqTimeStmp, cdPymntAmt
 * 							delete getEpayRcveTimeStmp(),
 * 								getEpaySendTimeStmp(),
 * 								getInitReqTimeStmp(),
 * 								setEpayRcveTimeStmp(), 
 * 								setEpaySendTimeStmp(),
 * 								setInitReqTimeStmp() 
 * 							modify buildFees(), buildSPITD(), 
 * 								getPymntAmt(), setPymntAmt(),
 * 								validateTransRequest() 
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	03/22/2010	Bring the validate routine up to date.
 * 							modify validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	03/23/2010	Sync up SpclRegId definition.
 * 							add clSpclRegId
 * 							delete clSRId
 * 							add getSpclRegId(), setSpclRegId()
 * 							delete getSRId(), setSRId()
 * 							modify RtsTransRequestV1(), buildSPITD(), 
 * 								validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	03/26/2010	Work on validation.
 * 							Add routine to lookup trans codes based on 
 * 							action.
 * 							add csTransCd
 * 							add getTransCode()
 * 							delete cbFromReserveIndi
 * 							delete isFromReserve(), setFromReserve() 
 * 							modify RtsTransRequestV1(), buildSPITD(),
 * 								validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/06/2010	If there is a SpclRegId passed in,
 * 							we will not have Inventory.
 * 							Work on Fees component.
 * 							modify buildFees(), hasInventory()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/09/2010	Work on transcode assignment.			
 * 							modify getTransCode()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/13/2010	Add Port and ReDo.			
 * 							modify getTransCode()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/19/2010	Use constants for validation routine.	
 * 							modify validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/23/2010	Added getPltValidityTerm() to call for
 * 							PlateSurchargeCache.getPltSurcharge()
 * 							modify buildFees()	
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/30/2010	Allow the VP Office for processing Reserve
 * 							requests.
 * 							modify validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	05/04/2010	Setup account code for Auctions.
 * 							modify buildFees()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	05/05/2010	Allow a validity term of 25 years.
 * 							modify validateTransRequest()
 * 							defect 10401 Ver 6.4.0
 * J Zwiener	02/01/2011	Use RestyleAcctItmCd from OrgNo instead of
 * 							"RESTYLE" constant
 * 							modify buildFees()
 * 							defect 10627 Ver POS_670
 * ---------------------------------------------------------------------
 */

/**
 * Transaction Request data (V1) for Vendor Plates web services
 *
 * @version	6.4.0			05/05/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		03/12/2010 07:27:10
 */

public class RtsTransRequestV1 extends RtsAbstractRequest
{
	// This is to indicate that this is a new application for a plate
	// A renewal is 0.
	private final int NEWPLTAPP = 1;

	// defect 10401
	// remove these since they are not useful
	//private Calendar caEpayRcveTimeStmp;
	//private Calendar caEpaySendTimeStmp;
	//private Calendar caInitReqTimeStmp;
	// end defect 10401
	private RtsAddress caPltOwnrAddr;
	// defect 10401
	private boolean cbAuctnPltIndi;
	//private boolean cbFromReserveIndi;
	// end defect 10401
	private boolean cbIsaIndi;
	// defect 10401
	private boolean cbMktngAllowdIndi;
	// end defect 10401
	private boolean cbPlpIndi;
	// defect 10401
	// convert to BigDecimal for more accurate conversion.
	private BigDecimal caPymntAmt;
	// end defect 10401
	private int ciAddlSetIndi;
	private int ciItrntPymntStatusCd;
	private int ciPltExpMo;
	private int ciPltExpYr;
	// defect 10366 
	//private int ciPltTerm;
	private int ciPltValidityTerm;
	// end defect 10366
	private int ciResComptCntyNo;
	// defect 10401
	private long clSpclRegId;
	// end defect 10401
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
	// defect 10401
	private String csReservReasnCd;
	private String csTransCd;
	// end defect 10401

	/**
	 * RtsTransRequestV1.java Constructor
	 */
	public RtsTransRequestV1()
	{
		super();
		// the version is always 1!
		setVersionNo(1);
	}

	/**
	 * RtsTransRequestV1.java Constructor
	 * 
	 * <p>Construct from a V0 type object.
	 * 
	 * @param aaRtsTransRequest
	 */
	public RtsTransRequestV1(RtsTransRequest aaRtsTransRequest)
	{
		super();

		// copy over parent data
		setAction(aaRtsTransRequest.getAction());
		setVersionNo(aaRtsTransRequest.getVersionNo());
		setCaller(aaRtsTransRequest.getCaller());
		setSessionId(aaRtsTransRequest.getSessionId());

		// copy over trans request data
		caPltOwnrAddr = aaRtsTransRequest.getPltOwnrAddr();
		cbIsaIndi = aaRtsTransRequest.isIsa();
		cbPlpIndi = aaRtsTransRequest.isPlp();
		caPymntAmt = new BigDecimal(aaRtsTransRequest.getPymntAmt());
		ciAddlSetIndi = aaRtsTransRequest.getAddlSetIndi();
		ciItrntPymntStatusCd =
			aaRtsTransRequest.getItrntPymntStatusCd();
		ciPltExpMo = aaRtsTransRequest.getPltExpMo();
		ciPltExpYr = aaRtsTransRequest.getPltExpYr();
		ciPltValidityTerm = aaRtsTransRequest.getPltValidityTerm();
		ciResComptCntyNo = aaRtsTransRequest.getResComptCntyNo();
		// SRId is not set in V0.
		clSpclRegId = 0;
		csItrntTraceNo = aaRtsTransRequest.getItrntTraceNo();
		csMfgPltNo = aaRtsTransRequest.getMfgPltNo();
		csOrgNo = aaRtsTransRequest.getOrgNo();
		csPltCd = aaRtsTransRequest.getPltCd();
		csPltNo = aaRtsTransRequest.getPltNo();
		csPltOwnrEmailAddr = aaRtsTransRequest.getPltOwnrEmailAddr();
		csPltOwnrName1 = aaRtsTransRequest.getPltOwnrName1();
		csPltOwnrName2 = aaRtsTransRequest.getPltOwnrName2();
		csPltOwnrPhone = aaRtsTransRequest.getPltOwnrPhone();
		csPymntOrderId = aaRtsTransRequest.getPymntOrderId();
		// Reserve Reason Code is not set in V0
		csReservReasnCd = CommonConstant.STR_SPACE_EMPTY;
	}

	/**
	 * Build the FeesData component for VP Processing.
	 * 
	 * @return FeesData
	 */
	public Vector buildFees()
	{
		FeesData laFeesData = null;
		Vector lvReturnVec = null;

		// defect 10401
		// This method was largely re-designed with 10401.
		// match payment up to need

		// if there is no payment amount, there are no fees (revise)
		if (caPymntAmt.doubleValue() != 0)
		{
			// defect 10401
			if (isAuctnPltIndi())
			{
				// if this is Auction, just use the account code SPLAUCTN.
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					SpecialPlatesConstant.ACCT_CD_AUCTION);
				laFeesData.setItemPrice(
					new Dollar(caPymntAmt.doubleValue()));
				laFeesData.setItmQty(1);
				lvReturnVec = new Vector(1);
				lvReturnVec.add(laFeesData);
			}
			else if (getTransCode().equalsIgnoreCase(TransCdConstant.VPRSTL))
			{
				// end defect 10401
				// if this is a revise, just use the account code RESTYLE.
				laFeesData = new FeesData();
				
				// defect 10627			
				laFeesData.setAcctItmCd(OrganizationNumberCache.
					getRestyleAcctItmCd(csPltCd,csOrgNo));
				if (laFeesData.getAcctItmCd() == null) 
				{
					laFeesData.setAcctItmCd(
						SpecialPlatesConstant.ACCT_CD_RESTYLE);
				}
				// end defect 10627						
						
				laFeesData.setItemPrice(
					new Dollar(caPymntAmt.doubleValue()));
				laFeesData.setItmQty(1);
				lvReturnVec = new Vector(1);
				lvReturnVec.add(laFeesData);
				// defect 10401
			}
			else
			{
				// end defect 10401
				// For Vendor Plates, we will probably get a row for each 
				// Validity Term.  Match up on term.
				Vector lvPltSurChg =
					PlateSurchargeCache.getPltSurcharge(
						getPltCd().trim(),
						getOrgNo().trim(),
						getAddlSetIndi(),
						NEWPLTAPP,
						new RTSDate().getYYYYMMDDDate(),
						getPltValidityTerm());

				if (lvPltSurChg.size() > 0)
				{
					for (Iterator laIter = lvPltSurChg.iterator();
						laIter.hasNext();
						)
					{
						PlateSurchargeData laPltSurCrg =
							(PlateSurchargeData) laIter.next();

						if (laPltSurCrg.getPltValidityTerm()
							== getPltValidityTerm())
						{
							// use the account code that matches the term on
							// the request.
							laFeesData = new FeesData();
							laFeesData.setAcctItmCd(
								laPltSurCrg.getAcctItmCd());
							laFeesData.setItemPrice(
								new Dollar(caPymntAmt.doubleValue()));
							laFeesData.setItmQty(1);
							lvReturnVec = new Vector(1);
							lvReturnVec.add(laFeesData);
							break;
						}
					}
				}
			}
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
		if (lsRIP.length()
			> CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX)
		{
			lsRIP =
				lsRIP.substring(
					lsRIP.length()
						- CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX);
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

		// defect 10401
		//laSPITD.setEpaySendTimeStmp(
		//	new RTSDate(caEpaySendTimeStmp.getTimeInMillis()));
		//laSPITD.setEpayRcveTimeStmp(
		//	new RTSDate(caEpayRcveTimeStmp.getTimeInMillis()));
		//laSPITD.setInitReqTimeStmp(
		//	new RTSDate(caInitReqTimeStmp.getTimeInMillis()));
		//laSPITD.setUpdtTimeStmp(new RTSDate());
		// end defect 10401

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

		// defect 10401
		//		if (isFromReserve())
		//		{
		//			laSPITD.setFromReserveIndi(1);
		//		}
		//		else
		//		{
		//			laSPITD.setFromReserveIndi(0);
		//		}
		// end defect 10401

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

		// defect 10401
		if (isAuctnPltIndi())
		{
			laSPITD.setAuctnPltIndi(1);
		}
		else
		{
			laSPITD.setAuctnPltIndi(0);
		}

		if (isMktngAllowdIndi())
		{
			laSPITD.setMktngAllowdIndi(1);
		}
		else
		{
			laSPITD.setMktngAllowdIndi(0);
		}

		laSPITD.setPymntAmt(caPymntAmt.doubleValue());

		laSPITD.setResrvReasnCd(csReservReasnCd);

		// SRId
		laSPITD.setSpclRegId(clSpclRegId);
		// end defect 10401
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
		if (lsRIP.length()
			> CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX)
		{
			lsRIP =
				lsRIP.substring(
					lsRIP.length()
						- CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX);
		}
		laSPITD.setReqIPAddr(lsRIP);

		// defect 10401
		// set the update time stamp to now.
		laSPITD.setUpdtTimeStmp(new RTSDate());
		// end defect 10401

		// defect 10401
		// lookup the transcd
		laSPITD.setTransCd(getTransCode());
		// end defect 10401

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

	// defect 10401
	//	/**
	//	 * Returns the Epay Receive TimeStamp.
	//	 * 
	//	 * @return Calendar
	//	 */
	//	public java.util.Calendar getEpayRcveTimeStmp()
	//	{
	//		return caEpayRcveTimeStmp;
	//	}
	//
	//	/**
	//	 * Returns the Epay Send TimeStamp.
	//	 * 
	//	 * @return Calendar
	//	 */
	//	public java.util.Calendar getEpaySendTimeStmp()
	//	{
	//		return caEpaySendTimeStmp;
	//	}
	//
	//	/**
	//	 * Get the Initial Request Start TimeStamp.
	//	 * 
	//	 * @return Calendar
	//	 */
	//	public java.util.Calendar getInitReqTimeStmp()
	//	{
	//		return caInitReqTimeStmp;
	//	}
	// end defect 10401

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

	// defect 10401
	/**
	 * Returns the Payment Amount.
	 * 
	 * @return BigDecimal
	 */
	public java.math.BigDecimal getPymntAmt()
	{
		return caPymntAmt;
		// end defect 10401
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
	 * Get the Reserve Reason Code.
	 * 
	 * @return String
	 */
	public String getReservReasnCd()
	{
		return csReservReasnCd;
	}

	/**
	 * Get the Special Regis Id.
	 * 
	 * @return long
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}

	/**
	 * Get the assigned trans code.
	 * 
	 * Lookup it up trans code based on service number if not set.
	 * 
	 * @return String
	 */
	public String getTransCode()
	{
		if (csTransCd == null)
		{
			csTransCd = CommonConstant.STR_SPACE_EMPTY;

			switch (getAction())
			{

				case WebServicesActionsConstants
					.RTS_TRANS_VP_ORDER_NEW :
					{
						csTransCd = TransCdConstant.VPAPPL;
						break;
					}
				case WebServicesActionsConstants
					.RTS_TRANS_VP_ORDER_RESERVE :
					{
						csTransCd = TransCdConstant.VPAPPR;
						break;
					}
				case WebServicesActionsConstants
					.RTS_TRANS_VP_DELETE_SP :
					{
						csTransCd = TransCdConstant.VPDEL;
						break;
					}
				case WebServicesActionsConstants.RTS_TRANS_VP_PORT :
					{
						csTransCd = TransCdConstant.VPPORT;
						break;
					}
				case WebServicesActionsConstants.RTS_TRANS_VP_REDO :
					{
						csTransCd = TransCdConstant.VPREDO;
						break;
					}
				case WebServicesActionsConstants.RTS_TRANS_VP_REVISE :
					{
						csTransCd = TransCdConstant.VPREV;
						break;
					}
				case WebServicesActionsConstants
					.RTS_TRANS_VP_RESERVE_PLT_NO :
					{
						csTransCd = TransCdConstant.VPRSRV;
						break;
					}
				case WebServicesActionsConstants
					.RTS_TRANS_VP_RESTYLE_PLT_NO :
					{
						csTransCd = TransCdConstant.VPRSTL;
						break;
					}
				case WebServicesActionsConstants
					.RTS_TRANS_VP_UNACCEPTABLE :
					{
						csTransCd = TransCdConstant.VPUNAC;
						break;
					}
				default :
					{
						break;
					}
			}
		}

		return csTransCd;
	}

	/**
	 * Indicvates if this transaction type should have inventory.
	 * 
	 * @return boolean
	 */
	public boolean hasInventory()
	{
		boolean lbInv = true;
		if (clSpclRegId != 0)
		{
			lbInv = false;
		}
		return lbInv;
	}
	/**
	 * Get the Auction Plate Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isAuctnPltIndi()
	{
		return cbAuctnPltIndi;
	}

	// defect 10401
	//	/**
	//	 * Returns indication if we are issuing from Reserved.
	//	 * 
	//	 * @return boolean
	//	 */
	//	public boolean isFromReserve()
	//	{
	//		return cbFromReserveIndi;
	//	}
	// end defect 10401

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
	 * Set the Marketing Allowed Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isMktngAllowdIndi()
	{
		return cbMktngAllowdIndi;
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

	/**
	 * Set the Auction Plate Indicator.
	 * 
	 * @param abAuctnPltIndi
	 */
	public void setAuctnPltIndi(boolean abAuctnPltIndi)
	{
		cbAuctnPltIndi = abAuctnPltIndi;
	}

	// defect 10401
	//	/**
	//	 * Sets the Epay Receive TimeStamp.
	//	 * 
	//	 * @param aaEpayRcveTimeStmp
	//	 */
	//	public void setEpayRcveTimeStmp(
	//		java.util.Calendar aaEpayRcveTimeStmp)
	//	{
	//		caEpayRcveTimeStmp = aaEpayRcveTimeStmp;
	//	}
	//
	//	/**
	//	 * Sets the Epay Send TimeStamp.
	//	 * 
	//	 * @param aaEpaySendTimeStmp
	//	 */
	//	public void setEpaySendTimeStmp(
	//		java.util.Calendar aaEpaySendTimeStmp)
	//	{
	//		caEpaySendTimeStmp = aaEpaySendTimeStmp;
	//	}
	//
	//	/**
	//	 * Set the Initial Request Start TimeStamp.
	//	 * 
	//	 * @param aaInitReqTimeStmp
	//	 */
	//	public void setInitReqTimeStmp(
	//		java.util.Calendar aaInitReqTimeStmp)
	//	{
	//		caInitReqTimeStmp = aaInitReqTimeStmp;
	//	}
	// end defect 10401

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
	 * Set the Marketing Allowed Indicator.
	 * 
	 * @param abMktngAllowdIndi
	 */
	public void setMktngAllowdIndi(boolean abMktngAllowdIndi)
	{
		cbMktngAllowdIndi = abMktngAllowdIndi;
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
	 * @param aaPymntAmt
	 */
	// defect 10401
	public void setPymntAmt(java.math.BigDecimal aaPymntAmt)
	{
		caPymntAmt = aaPymntAmt;
		// end defect 10401
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
	 * Set the Reserve Reason Code.
	 * 
	 * @param asReservReasnCd
	 */
	public void setReservReasnCd(String asReservReasnCd)
	{
		csReservReasnCd = asReservReasnCd;
	}

	// defect 10401
	//	/**
	//	 * Sets the indicator for if the plate number is from Reserve.
	//	 * 
	//	 * @param abFromReserveIndi
	//	 */
	//	public void setFromReserve(boolean abFromReserveIndi)
	//	{
	//		cbFromReserveIndi = abFromReserveIndi;
	//	}
	// end defect 10401

	/**
	 * Set the Special Regis Id.
	 * 
	 * @param alSpclRegId
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateTransRequest() throws RTSException
	{
		RTSException leRTSEx = null;

		// make sure there is a CallerId
		if (getCaller() == null || getCaller().length() < 1)
		{

			// put some data in so db2 can handle.
			setCaller(
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					getCaller()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_CALLER_MC);
			}
		}

		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			// put some data in so db2 can handle.
			setSessionId(
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					getSessionId()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_SESSIONID_MC);
			}
		}

		if (csItrntTraceNo == null)
		{
			csItrntTraceNo = CommonConstant.STR_SPACE_ONE;

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csItrntTraceNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ITRNTTRACENO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}
		else if (
			csItrntTraceNo.length()
				> CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX)
		{
			// TODO is there a constant for this
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csItrntTraceNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ITRNTTRACENO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}
			csItrntTraceNo =
				csItrntTraceNo.substring(
					0,
					CommonConstant.INT_ITRNTTRACENO_LENGTH_MAX);
		}

		if (csMfgPltNo == null)
		{
			csMfgPltNo =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csMfgPltNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_MFGPLTNO_MC);
			}
		}
		else if (
			csMfgPltNo.length() > CommonConstant.LENGTH_MFG_PLTNO_MAX)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csMfgPltNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_MFGPLTNO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}
			csMfgPltNo =
				csMfgPltNo.substring(
					0,
					CommonConstant.LENGTH_MFG_PLTNO_MAX);
		}

		if (csOrgNo == null)
		{
			csOrgNo =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csOrgNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ORGNO_MC);
			}
		}
		else if (
			csOrgNo.length() > CommonConstant.INT_ORGNO_LENGTH_MAX)
		{

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csOrgNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ORGNO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
				csOrgNo =
					csOrgNo.substring(
						0,
						CommonConstant.INT_ORGNO_LENGTH_MAX);
			}
		}

		if (csPltCd == null || csPltCd.length() < 1)
		{
			// put some data in so db2 can handle.
			csPltCd =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltCd
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTCD_MC);
			}
		}

		PlateTypeData laPltType = PlateTypeCache.getPlateType(csPltCd);
		if (laPltType == null && leRTSEx == null)
		{
			leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx.setDetailMsg(
				csPltCd
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_PLTCD_MC);
		}

		laPltType = null;

		if (ciPltExpMo < 0 || ciPltExpMo > 12)
		{
			// TODO There has to be a better way!
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					ciPltExpMo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTEXPMO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}

		RTSDate laToday = new RTSDate();
		if (ciPltExpYr < (laToday.getYear() - 1)
			|| ciPltExpYr > (laToday.getYear() + 30))
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					ciPltExpYr
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTEXPYR_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}

		if (csPltNo == null || csPltNo.length() < 1)
		{
			// put some data in so db2 can handle.
			csPltNo =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTNO_MC);
			}
		}
		else if (csPltNo.length() > CommonConstant.LENGTH_PLTNO)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTNO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}
			csPltNo = csPltNo.substring(0, CommonConstant.LENGTH_PLTNO);
		}

		if (csPltOwnrName1 == null || csPltOwnrName1.length() < 1)
		{
			csPltOwnrName1 =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrName1
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTOWNRNAME1_MC);
			}
		}
		else if (csPltOwnrName1.length() > CommonConstant.LENGTH_NAME)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrName1
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTOWNRNAME1_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}
			csPltOwnrName1 =
				csPltOwnrName1.substring(0, CommonConstant.LENGTH_NAME);
		}

		if (csPltOwnrName2 == null)
		{
			csPltOwnrName2 =
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrName2
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTOWNRNAME2_MC);
			}
		}
		else if (csPltOwnrName2.length() > CommonConstant.LENGTH_NAME)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrName2
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTOWNRNAME2_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}

			csPltOwnrName2 =
				csPltOwnrName2.substring(0, CommonConstant.LENGTH_NAME);
		}

		if (caPltOwnrAddr == null
			|| caPltOwnrAddr.getStreetLine1() == null
			|| caPltOwnrAddr.getStreetLine1().length() < 1)
		{
			caPltOwnrAddr.setStreetLine1(
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getStreetLine1()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_STREETLINE1_MC);
			}
		}
		else if (
			caPltOwnrAddr.getStreetLine1().length()
				> CommonConstant.LENGTH_STREET)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getStreetLine1()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_STREETLINE1_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}

			caPltOwnrAddr.setStreetLine1(
				caPltOwnrAddr.getStreetLine1().substring(
					0,
					CommonConstant.LENGTH_STREET));
		}

		if (caPltOwnrAddr.getStreetLine2() == null)
		{
			caPltOwnrAddr.setStreetLine2(
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getStreetLine2()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_STREETLINE2_MC);
			}
		}
		else if (
			caPltOwnrAddr.getStreetLine2().length()
				> CommonConstant.LENGTH_STREET)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getStreetLine2()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_STREETLINE2_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}

			caPltOwnrAddr.setStreetLine2(
				caPltOwnrAddr.getStreetLine2().substring(
					0,
					CommonConstant.LENGTH_STREET));
		}

		if (caPltOwnrAddr.getCity() == null
			|| caPltOwnrAddr.getCity().length() < 1)
		{
			caPltOwnrAddr.setCity(
				CommonConstant.STR_TXT_EMPTY
					+ CommonConstant.STR_SPACE_ONE
					+ ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getCity()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_CITY_MC);
			}
		}
		else if (
			caPltOwnrAddr.getCity().length()
				> CommonConstant.LENGTH_CITY)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getCity()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_CITY_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}

			caPltOwnrAddr.setCity(
				caPltOwnrAddr.getCity().substring(
					0,
					CommonConstant.LENGTH_CITY));
		}

		if (caPltOwnrAddr.getState() == null
			|| !CommonValidations.isValidState(caPltOwnrAddr.getState()))
		{
			if (caPltOwnrAddr.getState() == null)
			{
				caPltOwnrAddr.setState(CommonConstant.STR_SPACE_TWO);
			}
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getState()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_STATE_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}

			if (caPltOwnrAddr.getState().length() > 2)
			{
				caPltOwnrAddr.setState(
					caPltOwnrAddr.getState().substring(0, 2));
			}
		}

		if (caPltOwnrAddr.getZip() == null
			|| (caPltOwnrAddr.getZip().length() > 0
				&& caPltOwnrAddr.getZip().length()
					< CommonConstant.LENGTH_ZIPCODE)
			|| caPltOwnrAddr.getZip().length()
				> CommonConstant.LENGTH_ZIPCODE)
		{
			if (caPltOwnrAddr.getZip() == null)
			{
				caPltOwnrAddr.setZip(CommonConstant.STR_SPACE_ONE);
			}

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getZip()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ZIP_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
			if (caPltOwnrAddr.getZip().length()
				> CommonConstant.LENGTH_ZIPCODE)
			{
				caPltOwnrAddr.setZip(
					caPltOwnrAddr.getZip().substring(
						0,
						CommonConstant.LENGTH_ZIPCODE));
			}
		}

		// make sure Zip is numeric
		try
		{
			Integer.parseInt(caPltOwnrAddr.getZip());
		}
		catch (NumberFormatException aeNFEx)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getZip()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ZIP_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}

		if (caPltOwnrAddr.getZipP4() == null
			|| caPltOwnrAddr.getZipP4().length()
				> CommonConstant.LENGTH_ZIP_PLUS_4
			|| (caPltOwnrAddr.getZipP4().length()
				< CommonConstant.LENGTH_ZIP_PLUS_4
				&& caPltOwnrAddr.getZipP4().length() != 0))
		{
			if (caPltOwnrAddr.getZipP4() == null)
			{
				caPltOwnrAddr.setZipP4(CommonConstant.STR_SPACE_ONE);
			}

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPltOwnrAddr.getZipP4()
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_ZIPP4_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}

			if (caPltOwnrAddr.getZipP4().length()
				> CommonConstant.LENGTH_ZIP_PLUS_4)
			{
				caPltOwnrAddr.setZipP4(
					caPltOwnrAddr.getZipP4().substring(
						0,
						CommonConstant.LENGTH_ZIP_PLUS_4));
			}

		}

		// make sure ZipP4 is numeric if it is there
		if (caPltOwnrAddr.getZipP4().length() > 1)
		{
			try
			{
				Integer.parseInt(caPltOwnrAddr.getZipP4());
			}
			catch (NumberFormatException aeNFEx)
			{
				if (leRTSEx == null)
				{
					leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
					leRTSEx.setDetailMsg(
						caPltOwnrAddr.getZipP4()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_ZIPP4_MC
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.INVALID_REQ);
				}
			}
		}

		if (csPltOwnrEmailAddr == null
			|| csPltOwnrEmailAddr.length() > CommonConstant.LENGTH_EMAIL
			|| !CommonValidations.isValidEMail(csPltOwnrEmailAddr))
		{
			if (csPltOwnrEmailAddr == null)
			{
				csPltOwnrEmailAddr = CommonConstant.STR_SPACE_ONE;
			}
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrEmailAddr
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.TXT_EMAIL
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
			if (csPltOwnrEmailAddr.length()
				> CommonConstant.LENGTH_EMAIL)
			{
				csPltOwnrEmailAddr =
					csPltOwnrEmailAddr.substring(
						0,
						CommonConstant.LENGTH_EMAIL);
			}
		}

		if (csPltOwnrPhone == null
			|| csPltOwnrPhone.length()
				> CommonConstant.LENGTH_TELEPHONE_NUMBER
			|| csPltOwnrPhone.length()
				< CommonConstant.LENGTH_TELEPHONE_NUMBER)
		{
			if (csPltOwnrPhone == null)
			{
				csPltOwnrPhone = CommonConstant.STR_SPACE_ONE;
			}

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPltOwnrPhone
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTOWNRPHONE_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}

			if (csPltOwnrPhone.length()
				> CommonConstant.LENGTH_TELEPHONE_NUMBER)
				csPltOwnrPhone =
					csPltOwnrPhone.substring(
						0,
						CommonConstant.LENGTH_TELEPHONE_NUMBER);
		}

		if (ciPltValidityTerm != 0
			&& ciPltValidityTerm != 1
			&& ciPltValidityTerm != 5
			&& ciPltValidityTerm != 10
			&& ciPltValidityTerm != 25)
		{
			// TODO There has to be a better way!
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					ciPltValidityTerm
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PLTVALIDITYTERM_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}

		if (caPymntAmt == null)
		{
			caPymntAmt = new BigDecimal(0.00);
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPymntAmt
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PYMNTAMT_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}
		else if (
			caPymntAmt.doubleValue()
				> CommonConstant.DBL_PYMNTAMT_VALUE_MAX)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					caPymntAmt
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PYMNTAMT_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_TOO_LONG_MC);
			}
			caPymntAmt = new BigDecimal(0.00);
		}

		if (csPymntOrderId == null
			|| csPymntOrderId.length()
				> CommonConstant.INT_PYMNTORDERID_LNGTH_MAX)
		{
			// TODO is there a constant for this.
			if (csPymntOrderId == null)
			{
				csPymntOrderId = CommonConstant.STR_SPACE_ONE;
			}

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csPymntOrderId
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_PYMNTORDERID_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
			if (csPymntOrderId.length()
				> CommonConstant.INT_PYMNTORDERID_LNGTH_MAX)
			{
				csPymntOrderId =
					csPymntOrderId.substring(
						0,
						CommonConstant.INT_PYMNTORDERID_LNGTH_MAX);
			}
		}

		// defect 10401
		// The VP Office also has to be allowed.
		if (ciResComptCntyNo < CommonConstant.MIN_COUNTY_NO
			|| (ciResComptCntyNo > CommonConstant.MAX_COUNTY_NO 
			&& ciResComptCntyNo != SystemProperty.getVpOfcIssuanceNo()))
		{
			// end defect 10401
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					ciResComptCntyNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_RESCOMPTCNTYNO_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
		}

		if (csReservReasnCd == null || csReservReasnCd.length() > 1)
		{
			if (csReservReasnCd == null)
			{
				csReservReasnCd = CommonConstant.STR_SPACE_ONE;
			}

			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					csReservReasnCd
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_RESERVREASNCD_MC
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}
			if (csReservReasnCd.length() > 1)
			{
				csReservReasnCd = csReservReasnCd.substring(0, 1);
			}
		}

		if (clSpclRegId < 0
			|| clSpclRegId > CommonConstant.L_SPCLREGID_VALUE_MAX)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					clSpclRegId
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.SPCL_REG_ID
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.INVALID_REQ);
			}

			clSpclRegId = 0;
		}

		if ((getTransCode().equals(TransCdConstant.VPDEL)
			|| getTransCode().equals(TransCdConstant.VPREV)
			|| getTransCode().equals(TransCdConstant.VPRSTL))
			&& clSpclRegId < 1)
		{
			if (leRTSEx == null)
			{
				leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.setDetailMsg(
					clSpclRegId
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.SPCL_REG_ID
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_MISSING_MC);
			}
		}

		if (leRTSEx != null)
		{
			throw leRTSEx;
		}
	}
}
