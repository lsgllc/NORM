package com.txdot.isd.rts.services.webapps.util;

import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.transaction
	.business
	.TransactionAccessBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.transaction
	.data
	.TransactionRequest;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.EmailUtil;

/*
 * EmailSpecialPlateReceipt.java
 *
* (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown		05/18/2007	Created Class.
 * 							defect 9118 Ver Special Plates
 * B. Brown		09/12/2007	Corrected fees listed per plate and total
 * 							fees for the shopping cart in the email 
 * 							sent.
 * 							modify emailReceipt()
 * 							defect 9301 Ver Special Plates
 * B. Brown		09/13/2007	Corrected Plate Type and Org listed
 * 							per plate in the email sent.
 * 							modify emailReceipt()
 * 							defect 9290 Ver Special Plates
 * B. Brown		09/14/2007	Added a no-refund statement to the email
 * 							modify emailReceipt()
 * 							defect 9314 Ver Special Plates
 * B. Brown		09/27/2007	Changed all the \n's to <br>'s so we can
 * 							make the email body html for underlining,
 * 							bolding, etc	 
 * 							modify emailReceipt()
 * 							defect 9119 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Business class used to handle the Special Plate Transactions.
 *
 * @version	Special Plates	09/27/2007
 * @author	Bob brown
 * <br>Creation Date:		05/18/2007 11:130:00
 */
public class EmailSpecialPlateReceipt
{
	/**
	 * CommunicationProperty constructor comment.
	 */
	public EmailSpecialPlateReceipt()
	{
		super();
	}

	public static void main(String[] args)
	{

	}

	public boolean emailReceipt(TransactionRequest[] aaTransReq)
	{

		boolean lbNewEmailAddr = true;
		SpecialPlateItrntTransData laSPItrntTransData =
			new SpecialPlateItrntTransData();
		EmailUtil laEmailService = new EmailUtil();
		//String lsEmailAddr = "";
		String lsOrgName = "";
		StringBuffer lsbEmailMsg = new StringBuffer();
		//Vector lvToAddr = new Vector();
		String[] larrToAddr = new String[aaTransReq.length];
		int liUniqueEmails = -1;
		// defect 9301
		Dollar ldShopCartTot = new Dollar(0.00);
		// end defect 9301

		try
		{
			for (int liX = 0; liX < aaTransReq.length; liX++)
			{
				TransactionAccessBusiness laTransAccBus =
					new TransactionAccessBusiness();
				TransactionRequest laTransReq =
					(TransactionRequest) aaTransReq[liX];
				laSPItrntTransData =
					laTransAccBus.convertToSPTransData(laTransReq);
				if (liX == 0)
				{
					// defect 9301
					lsbEmailMsg.append(
						"Thank you for your specialty plate order below:");
					lsbEmailMsg.append("<br>Your Trace Number is: ");
					lsbEmailMsg.append(
						laSPItrntTransData.getItrntTraceNo());
//					lsbEmailMsg.append("<br>Amount: $");
//						lsbEmailMsg.append(
//							UtilityMethods.format(
//								laSPItrntTransData.getPymntAmt()));
					// end defect 9301
					
					lsbEmailMsg.append(
						"<br>------------------------------------");
				}

				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd(
						laSPItrntTransData.getRegPltCd());
				
				// defect 9290		
				lsbEmailMsg.append(
					"<br>Plate Type: " + laItemCodesData.getItmCdDesc());

				if (!laSPItrntTransData.getOrgNo().equals(""))
				{
//					lsOrgName =
//						OrganizationNumberCache.getOrgName(
//							laSPItrntTransData.getRegPltCd(),
//							laSPItrntTransData.getOrgNo());
					lsbEmailMsg.append("<br>Organization: " + 
						OrganizationNumberCache.getOrgName(
						laSPItrntTransData.getRegPltCd(),
						laSPItrntTransData.getOrgNo()));
				}
				// end defect 9290

				if (laSPItrntTransData.getMfgPltNo() == null
					|| laSPItrntTransData.getMfgPltNo() == "")
				{
					lsbEmailMsg.append(
						"<br>Plate Number: "
							+ laSPItrntTransData.getRegPltNo());
				}
				else
				{
					lsbEmailMsg.append(
						"<br>Plate Number: "
							+ laSPItrntTransData.getMfgPltNo());
				}
				
				// defect 9301
				lsbEmailMsg.append("<br>Amount: $");
				lsbEmailMsg.append(
					UtilityMethods.format(
						laSPItrntTransData.getPymntAmt()));
				// end defect 9301		

				OfficeIdsData laOfficeIdsData =
					OfficeIdsCache.getOfcId(
						laSPItrntTransData.getResComptCntyNo());

				lsbEmailMsg.append(
					"<br><br>Pick up your specialty plates here:");

				lsbEmailMsg.append(
					"<br>"
						+ laOfficeIdsData.getOfcName()
						+ " County Main Office ");

				lsbEmailMsg.append(
					"<br>"
						+ laOfficeIdsData.getPhysOfcLoc()
						+ "<br>"
						+ laOfficeIdsData.getOfcCity()
						+ ", TX "
						+ laOfficeIdsData.getOfcZpCd());

				lsbEmailMsg.append(
					"<br>Phone: "
						+ UtilityMethods.toPhoneFormat(
							laOfficeIdsData.getTacPhoneNo()));

				lsbEmailMsg.append(
					"<br>------------------------------------");

				// defect 9301
				ldShopCartTot =
					ldShopCartTot.add(
						laSPItrntTransData.getTotalFees());

				if (liX == aaTransReq.length - 1)
				{

					lsbEmailMsg.append(
						"<br>Total Shopping Cart Amount: $");
					lsbEmailMsg.append(ldShopCartTot);
					lsbEmailMsg.append(
						"<br>------------------------------------");
				// end defect 9301		
					lsbEmailMsg.append(
						"<br>Your specialty plates should be ready "
							+ "within 14 business days.  Call to confirm delivery "
							+ "before attempting to obtain your specialty "
							+ "plates. "
							+ "<br><br>Additional fees may be collected to "
							+ "establish a single expiration date for both "
							+ "the specialty plates and the vehicle "
							+ "registration. "
							+ "<br><br>Personalized number "
							+ "selections are subject to additional review "
							+ "and approval. If denied, you will be "
							+ "given the opportunity to "
							+ "make another selection."
							+ "<br><br>You will be contacted by email or phone "
							+ "if a problem occurs with your order."
							// defect 9314
							+ "<br><br><B><U>Specialty plate fees will not be refunded " 
							+ "once application is submitted.</U></B>");
							// end defect 9314

					lsbEmailMsg.append(
						"<br><br>Do not reply to this email.");
				}

				lbNewEmailAddr = true;

				for (int liY = 0; liY <= liUniqueEmails; liY++)
				{
					if (laSPItrntTransData
						.getPltOwnrEmail()
						.equals(larrToAddr[liY].toString()))
					{
						lbNewEmailAddr = false;
						break;
					}
				}

				if (lbNewEmailAddr)
				{
					liUniqueEmails = liUniqueEmails + 1;
					larrToAddr[liUniqueEmails] =
						laSPItrntTransData.getPltOwnrEmail();
				}
			}
		}

		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}

		// System.out.println(lsbEmailMsg.toString());

		return laEmailService.sendEmail(
			larrToAddr,
			"RE: internet Special Plate Order",
			lsbEmailMsg.toString());
	}
}
