package com.txdot.isd.rts.server.webapps.order;

import com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse;
import com.txdot.isd.rts.server.webapps.order.countyinfo.business.CountyInfoBusiness;
import com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoRequest;
import com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.business.SpecialPlateInfoBusiness;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoRequest;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse;
import com.txdot.isd.rts.server.webapps.order.transaction.business.TransactionAccessBusiness;
import com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest;
import com.txdot.isd.rts.server.webapps.order.vehiclevalidation.business.VehicleValidationBusiness;
import com.txdot.isd.rts.server.webapps.order.vehiclevalidation.data.VehicleValidationRequest;
import com.txdot.isd.rts.server.webapps.order.virtualinv.business.VirtualInventoryBusiness;
import com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest;
import com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvResponse;

/*
 * SpecialPlatesService.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9120 Ver Special Plates
 * Bob B.		03/24/2008	Add logging
 * 							modify countyInfo(),specialPlateInfo(),
 * 							transactionAccess(),VehicleValidation(),
 * 							virtualInventoryAccess()
 * 							defect 9601 Ver Tres Amigos Prep
 * ---------------------------------------------------------------------
 */

/**
 * Web Service used for Special Plates Order.
 *
 * @version	Special Plates	03/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/01/2007 13:00:00
 */
public class SpecialPlatesAccess
{
	CountyInfoBusiness aaCouInfoBusiness = new CountyInfoBusiness();
	SpecialPlateInfoBusiness aaSPInfoBusiness =
		new SpecialPlateInfoBusiness();
	TransactionAccessBusiness aaTranAccBusiness =
		new TransactionAccessBusiness();
	VehicleValidationBusiness aaVehValBusiness =
		new VehicleValidationBusiness();
	VirtualInventoryBusiness aaViInvBusiness =
		new VirtualInventoryBusiness();

	/**
	 * Web Service Function for handling County Information Requests.
	 * 
	 * @param aaCntyInfoRequestObj CountyInfoRequest
	 * @return CountyInfoResponse[]
	 */
	public CountyInfoResponse[] countyInfo(CountyInfoRequest aaCntyInfoRequestObj)
	{
		// defect 9601
		System.out.println("Processing a web service CountyInfoRequest");
		System.out.println("CntyInfoRequestObj.getAction() = "
				+ aaCntyInfoRequestObj.getAction());
		System.out.println("CntyInfoRequestObj.getCountyNo() = "
				+ aaCntyInfoRequestObj.getCountyNo());
		// end defect 9601
		return (CountyInfoResponse[]) aaCouInfoBusiness.processData(
			aaCntyInfoRequestObj);
	}

	/**
	 * Web Service Function for handling Special Plate Information
	 * Requests.
	 * 
	 * @param aaInfoRequestObj SpecialPlatesInfoRequest
	 * @return SpecialPlatesInfoResponse[]
	 */
	public SpecialPlatesInfoResponse[] specialPlateInfo(SpecialPlatesInfoRequest aaInfoRequestObj)
	{
		// defect 9601
		System.out.println("Processing a web service SpecialPlatesInfoRequest");
		System.out.println(
			"SpecialPlatesInfoRequest.getAction() = "
				+ aaInfoRequestObj.getAction());
		System.out.println(
			"SpecialPlatesInfoRequest.getGrpId() = "
				+ aaInfoRequestObj.getGrpId());
		System.out.println(
			"SpecialPlatesInfoRequest.getPltId() = "
				+ aaInfoRequestObj.getPltId());
		System.out.println(
			"SpecialPlatesInfoRequest.getPltDesign() = "
				+ aaInfoRequestObj.getPltDesign());
		System.out.println(
			"SpecialPlatesInfoRequest.getPltImage() = "
				+ aaInfoRequestObj.getPltImage());
		// end defect 9601
		return (
			SpecialPlatesInfoResponse[]) aaSPInfoBusiness.processData(
			aaInfoRequestObj);
	}

	/**
	 * Web Service Function for handling Special Plate Transaction
	 * Requests.
	 * 
	 * @param aaTransRequestObj TransactionRequest[]
	 * @return DefaultResponse[]
	 */
	public DefaultResponse[] transactionAccess(TransactionRequest[] aaTransRequestObj)
	{
		// defect 9601
		System.out.println(
			"Processing a web service TransactionRequest array");
		for (int i = 0; i < aaTransRequestObj.length; i++)
		{
			TransactionRequest laTransReq =
				(TransactionRequest) aaTransRequestObj[i];
			System.out.println(
				"TransactionRequest.getAction() #"
					+ i
					+ " = "
					+ laTransReq.getAction());
			System.out.println(
				"TransactionRequest.getItrntPymntStatusCd() #"
					+ i
					+ " = "
					+ laTransReq.getItrntPymntStatusCd());
			System.out.println(
				"TransactionRequest.getItrntTraceNo() #"
					+ i
					+ " = "
					+ laTransReq.getItrntTraceNo());
			System.out.println(
				"TransactionRequest.getRegPltNo() #"
					+ i
					+ " = "
					+ laTransReq.getRegPltNo());
			System.out.println(
				"TransactionRequest.getOrgNo() #"
					+ i
					+ " = "
					+ laTransReq.getOrgNo());
			System.out.println(
				"TransactionRequest.getPlpIndi() #"
					+ i
					+ " = "
					+ laTransReq.getPlpIndi());
			System.out.println(
				"TransactionRequest.getPymntAmt() #"
					+ i
					+ " = "
					+ laTransReq.getPymntAmt());
			System.out.println(
				"TransactionRequest.getPymntOrderID() #"
					+ i
					+ " = "
					+ laTransReq.getPymntOrderID());
			System.out.println(
				"TransactionRequest.getRegPltCd() #"
					+ i
					+ " = "
					+ laTransReq.getRegPltCd());
			System.out.println(
				"TransactionRequest.getReqSessionID() #"
					+ i
					+ " = "
					+ laTransReq.getReqSessionID());
		}
		// end defect 9601
		return (DefaultResponse[]) aaTranAccBusiness.processData(
			aaTransRequestObj);
	}

	/**
	 * Web Service Function for handling Special Plate Vehicle
	 * Validation Requests.
	 * 
	 * @param aaVVRequestObj VehicleValidationRequest
	 * @return DefaultResponse
	 */
	public DefaultResponse vehicleValidation(VehicleValidationRequest aaVVRequestObj)
	{
		return (DefaultResponse) aaVehValBusiness.processData(
			aaVVRequestObj);
	}

	/**
	 * Web Service Function for handling Special Plate Virtual 
	 * Inventory Requests.
	 * 
	 * @param aaVIRequestObj VirtualInvRequest[]
	 * @return VirtualInvResponse[]
	 */
	public VirtualInvResponse[] virtualInventoryAccess(VirtualInvRequest[] aaVIRequestObj)
	{
		// defect 9601
		System.out.println("Processing a web service VirtualInvRequest array");
		for (int i = 0; i < aaVIRequestObj.length; i++)
		{
			VirtualInvRequest laVIReq =
				(VirtualInvRequest) aaVIRequestObj[i];
			System.out.println(
				"VirtualInvRequest.getAction() #"
					+ i
					+ " = "
					+ laVIReq.getAction());
			System.out.println(
				"VirtualInvRequest.getGrpId() #"
					+ i
					+ " = "
					+ laVIReq.getGrpId());
			System.out.println(
				"VirtualInvRequest.getGrpPltId() #"
					+ i
					+ " = "
					+ laVIReq.getGrpPltId());
			System.out.println(
				"VirtualInvRequest.getItemNo() #"
					+ i
					+ " = "
					+ laVIReq.getItemNo());
			System.out.println(
				"VirtualInvRequest.getItemCode() #"
					+ i
					+ " = "
					+ laVIReq.getItemCode());
			System.out.println(
				"VirtualInvRequest.getManufacturingPltNoRequest() #"
					+ i
					+ " = "
					+ laVIReq.getManufacturingPltNoRequest());
			System.out.println(
				"VirtualInvRequest.getRegPltNo() #"
					+ i
					+ " = "
					+ laVIReq.getRegPltNo());
			System.out.println(
				"VirtualInvRequest.getSessionID() #"
					+ i
					+ " = "
					+ laVIReq.getSessionID());
		}
		// end defect 9601
		return (VirtualInvResponse[]) aaViInvBusiness.processData(
			aaVIRequestObj);
	}
}
