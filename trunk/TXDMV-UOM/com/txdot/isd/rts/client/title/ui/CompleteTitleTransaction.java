package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.title.business.TitleInventory;

import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * CompleteTitleTransaction.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							methods doCompleteTransaction()
 *							defect 7496 Ver 5.2.1
 * Ray Rowehl	02/08/2005	Change import for Fees
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							Format and Hungarian notation to standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	11/05/2007	Add logic for DTA PTO.  Assign Customer
 * 							Supplied Plate No to CompleteTransactionData
 * 							object. 
 * 							modify doCompleteTransaction() 
 * 							defect 9425 Ver Special Plates 2
 * K Harrell	11/08/2007	Add Plate Age Logic for DTA PTO, Validate
 * 							Customer Supplied Plate No 
 * 							add INVALID_CUST_SUPPLIED_ERROR 
 * 							modify doCompleteTransaction() 
 * 							defect 9425 Ver Special Plates 2
 * K Harrell	12/16/2009	DTA Cleanup 
 * 							refactor caCompleteTransObj to caCTData
 * 							Implement use of UtilityMethods.copy() for 
 * 							 set methods. 
 * 							delete setOffcIssnNum() 
 * 							modify doCompleteTransaction(), calcFee(),
 * 							 initialize(),setModifiedMFVehData(), 
 * 							 setOrigMfVehData(), setRegTtlAddtnlData(), 
 * 							 setVehMiscData()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/13/2009	Remove use of UtilityMethods.copy() for 
 * 							objects. 
 * 							modify setModifiedMFVehData(), 
 * 							 setOrigMfVehData(), setRegTtlAddtnlData(), 
 * 							 setVehMiscData()
 * 							defect 10290 Ver Defect_POS_H 
 * ------------------------------------------------------------------
 */

/**
 * Complete Title Transaction. Calculate FEES.
 *
 * @version	Defect_POS_H	01/13/2009
 * @author	Ashish Mahajan 
 * <br>Creation Date:		10/25/2001 16:30:54
 */
public class CompleteTitleTransaction
{
	// defect 10290 
	private CompleteTransactionData caCTData = null;
	// end defect 10290 

	private TitleInventory caTtlInventory = null;
	private VehicleInquiryData caVehInqData = null;
	private String csTransCode;

	// defect 9425 
	private static final String INVALID_CUST_SUPPLIED_ERROR =
		"FOR THE CUSTOMER SUPPLIED PLATE NUMBER, THE LETTER OR NUMBER COMBINATION IS INVALID.";
	// end defect 9425 

	/**
	 * CompleteTitleTransaction constructor comment.
	 */
	public CompleteTitleTransaction()
	{
		super();
		initialize(null);

	}

	/**
	 * CompleteTitleTransaction
	 *  
	 * @param aaVehInqData VehicleInquiryData
	 * @param asTransCode String
	 */
	public CompleteTitleTransaction(
		VehicleInquiryData aaVehInqData,
		String asTransCode)
	{
		super();

		this.csTransCode = asTransCode;
		initialize(aaVehInqData);

	}

	/**
	 * calcFee
	 *  
	 * @throws RTSException
	 */
	private void calcFee() throws RTSException
	{
		if (caCTData != null && csTransCode != null)
		{
			Fees laFees = new Fees();

			caCTData =
				laFees.calcFees(caCTData.getTransCode(), caCTData);

			// for DTA
			// if the new exp yr and date is not between the min and 
			// max, then user need to escape back to DTA008
			if (caCTData.getDTAErrorMsg() != null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					caCTData.getDTAErrorMsg(),
					"ERROR");
			}

			if (caVehInqData != null)
			{
				TitleValidObj laTtlValidObj =
					(TitleValidObj) caVehInqData.getValidationObject();

				if (laTtlValidObj.getDlrTtlData() != null)
				{
					RegFeesData laRegFeesData =
						caCTData.getRegFeesData();
					Vector lvVecFeesData = laRegFeesData.getVectFees();
					Dollar laDlrTotalFee = new Dollar("0.00");
					(
						(DealerTitleData) laTtlValidObj
							.getDlrTtlData())
							.setRegFee(
						lvVecFeesData);

					for (int i = 0; i < lvVecFeesData.size(); i++)
					{
						FeesData laCurrFee =
							(FeesData) lvVecFeesData.get(i);
						Dollar laFee = laCurrFee.getItemPrice();
						if (laFee != null)
						{
							laDlrTotalFee = laDlrTotalFee.add(laFee);
						}

					}

					// defect 10290
					DealerTitleData laCurrDlrTtlData =
						(DealerTitleData) laTtlValidObj.getDlrTtlData();

					laCurrDlrTtlData.setTransFee(laDlrTotalFee);

					laCurrDlrTtlData.setRegFee(lvVecFeesData);

					//find out which element to update
					//if (laTtlValidObj.getDlrs() != null)
					//{
					//	int index = 0;
					//
					//	for (int i = 0;
					//		i < laTtlValidObj.getDlrs().size();
					//		i++)
					//	{
					//		if (((DealerTitleData) laTtlValidObj
					//			.getDlrs()
					//			.get(i))
					//			.isProcessed())
					//		{
					//			index = i;
					//		}
					//	}
					//
					//	laTtlValidObj.getDlrs().setElementAt(
					//		laCurrDlrTitleData,
					//		index);
					//}
					// end defect 10290 
				}
			}
		}
	}

	/**
	 * doCompleteTransaction
	 * 
	 * @return CompleteTransactionData 
	 * @throws RTSException 
	 */
	public CompleteTransactionData doCompleteTransaction()
		throws RTSException
	{
		// Verify Title Type Event
		if (csTransCode.equals(TransCdConstant.TITLE)
			|| csTransCode.equals(TransCdConstant.CORTTL)
			|| csTransCode.equals(TransCdConstant.NONTTL)
			|| csTransCode.equals(
				TransCdConstant.REJCOR) // defect 10290
			|| csTransCode.equals(TransCdConstant.CCO)
			|| UtilityMethods.isDTA(csTransCode))
			// end defect 10290 
		{
			if (caTtlInventory != null)
			{
				try
				{
					// defect 10290 
					if (UtilityMethods.isDTA(csTransCode))
					{
						// end defect 10290 

						TitleValidObj laValidObj =
							(TitleValidObj) caVehInqData
								.getValidationObject();

						if (laValidObj != null)
						{
							DealerTitleData laDlrTtlData =
								(DealerTitleData) laValidObj
									.getDlrTtlData();

							caTtlInventory.doDlrTtlInventory(
								caVehInqData,
								laDlrTtlData,
								csTransCode);

							// defect 10290
							// caCTData.setDlrTtlDataObjs(
							// laValidObj.getDlrs());
							caCTData.setDlrTtlData(laDlrTtlData);
							// end defect 10290 

							MFVehicleData laVehData =
								caCTData.getVehicleInfo();

							if (laVehData == null)
							{
								laVehData = new MFVehicleData();
								caCTData.setVehicleInfo(laVehData);
							}
							RegistrationData laRegData =
								laVehData.getRegData();

							if (laRegData == null)
							{
								laRegData = new RegistrationData();
								laVehData.setRegData(laRegData);
							}

							// defect 7496
							// NewRegExpMo and NewRegExpYr now int 
							laRegData.setRegExpMo(
								laDlrTtlData.getNewRegExpMo());
							laRegData.setRegExpYr(
								laDlrTtlData.getNewRegExpYr());

							// defect 9425 
							// Set OwnrSuppliedxxx if DTA && Cust 
							// Supplied. Note:  Done on REG029 for 
							// other trans. 
							if (laDlrTtlData.isCustSuppliedPlt())
							{
								// Validate Plate No 
								for (int i = 0;
									i
										<= caTtlInventory
											.getInvItems()
											.size();
									i++)
								{
									ProcessInventoryData laProcInvData =
										(ProcessInventoryData) caTtlInventory
											.getInvItems()
											.elementAt(
											i);
									if (laProcInvData
										.getItmCd()
										.equals(
											laRegData.getRegPltCd()))
									{
										ValidateInventoryPattern laValInvPattern =
											new ValidateInventoryPattern();
										try
										{
											laValInvPattern
												.validateItmNoInput(
												laProcInvData);
										}
										catch (RTSException leRTSEx)
										{
											throw new RTSException(
												RTSException
													.FAILURE_MESSAGE,
												INVALID_CUST_SUPPLIED_ERROR,
												"ERROR");
										}
										break;
									}
								}

								caCTData.setOwnrSuppliedPltNo(
									laDlrTtlData.getNewPltNo());
								caCTData
									.getVehicleInfo()
									.getRegData()
									.setOwnrSuppliedExpYr(
									laDlrTtlData.getNewRegExpYr());
								laRegData.setRegPltAge(
									laDlrTtlData
										.getCustSuppliedPltAge());
							}
							// end defect 9425 
						}
					}
					else if (csTransCode.equals(TransCdConstant.CCO))
					{
						caTtlInventory.issueCCOTitle();
					}
					else
					{
						caTtlInventory.doInventory(
							caVehInqData,
							csTransCode);
					}
					Vector lvInvItms = caTtlInventory.getInvItems();
					caCTData.setInvItms(lvInvItms);
					caCTData.setInvItemCount(lvInvItms.size());
				}
				catch (RTSException leRTSEx)
				{
					throw leRTSEx;
				}
			}
			caCTData.setNoMFRecs(caVehInqData.getNoMFRecs());

			calcFee();
		}
		return caCTData;
	}

	/**
	 * initialize
	 *  
	 * @param VehicleInquiryData aaVehInqData
	 */
	private void initialize(VehicleInquiryData aaVehInqData)
	{
		caCTData = new CompleteTransactionData();
		caTtlInventory = new TitleInventory();
		this.caVehInqData = aaVehInqData;

		if (aaVehInqData != null)
		{
			TitleValidObj laValidObj =
				(TitleValidObj) aaVehInqData.getValidationObject();

			if (laValidObj != null)
			{
				// Use UtilityMethods.copy() to assign to CTData 
				setOrigMfVehData(laValidObj.getMfVehOrig());
				setRegTtlAddtnlData(laValidObj.getRegTtlAddData());
				setVehMiscData(this.caVehInqData.getVehMiscData());
				caCTData.setRegPnltyChrgIndi(
					laValidObj.getRegPnltyChrgIndi());
			}

			setModifiedMfVehData(aaVehInqData.getMfVehicleData());
			caCTData.setTransactionDate(aaVehInqData.getRTSEffDt());
		}

		// defect 10290 
		caCTData.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
		// end defect 10290 
		caCTData.setTransCode(csTransCode);

		// defect 10290 
		if (UtilityMethods.isDTA(caCTData.getTransCode()))
		{
			// end defect 10290 
			TitleValidObj laTtlValObj =
				(TitleValidObj) aaVehInqData.getValidationObject();

			DealerTitleData laDlrTtlData =
				(DealerTitleData) laTtlValObj.getDlrTtlData();

			MFVehicleData laMfVeh =
				laDlrTtlData.getMFVehicleDataFromMF();

			// Use UtilityMethods.copy() to assign 
			setOrigMfVehData(laMfVeh);
		}

	}

	/**
	 * setModifiedMfVehData
	 *  
	 * @param aaData Object MFVehicleData
	 */
	private void setModifiedMfVehData(Object aaData)
	{
		if (aaData != null)
		{
			MFVehicleData laMfVehData = (MFVehicleData) aaData;

			caCTData.setVehicleInfo(laMfVehData);
		}
	}

	/**
	 * setOrigMfVehData
	 *  
	 * @param aaData Object MFVehicleData
	 */
	private void setOrigMfVehData(Object aaData)
	{
		if (aaData != null)
		{
			MFVehicleData laMfVehData = (MFVehicleData) aaData;

			caCTData.setOrgVehicleInfo(laMfVehData);
		}
	}

	/**
	 * setRegTtlAddtnlData
	 *  
	 * @param aaData Object RegTtlAddlInfoData
	 */
	private void setRegTtlAddtnlData(Object aaData)
	{
		if (aaData != null)
		{
			RegTtlAddlInfoData laRegTtlAddlInfoData =
				(RegTtlAddlInfoData) aaData;

			caCTData.setRegTtlAddlInfoData(laRegTtlAddlInfoData);
		}
	}

	/**
	 * setVehMiscData
	 *  
	 * @param aaData VehMiscData
	 */
	private void setVehMiscData(VehMiscData aaData)
	{
		if (aaData != null)
		{
			VehMiscData laVehMiscData = (VehMiscData) aaData;

			caCTData.setVehMisc(laVehMiscData);
		}
	}
}
