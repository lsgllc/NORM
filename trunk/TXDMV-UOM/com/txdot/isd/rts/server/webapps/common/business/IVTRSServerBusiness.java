package com.txdot.isd.rts.server.webapps.common.business;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.EmailUtilEReminders;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.EmailSpecialPlateReceipt;
import com.txdot.isd.rts.services.webapps.util.constants.AddressChangeConstants;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.MessageConstants;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;

/*
 * IVTRSServerBusiness.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		12/08/2010	Add this class for eReminder project.
 * 							defect 10610 Ver 6.7.0  
 * B Brown		05/03/2011	Change the csConfirmationMessage to look
 * 							for img src on IVTRS (use lsWebURL), not 
 * 							POS (lsImageURL).
 * 							defect 10807 Ver 6.7.0 fix 1
 * ---------------------------------------------------------------------
 */
/**
 * This frame displays the address info.
 *
 * @version	6.7.0 fix 1 	05/03/2011
 * @author	Bob Brown
 * <br>Creation Date:		See Clearcase History
 */
public class IVTRSServerBusiness
{
	private final static int INT_MAX_RETRY = 5;
	private static final String MSG_RETRY =
		"Retrying IADDR/IRNR transaction request ";
	private static final String MSG_RETRY_EXCEEDED =
		"Maximum Retry for IADDR/IRNR transaction exceeded";
	private static final String MSG_RTSEX_ON_TRANS_CREATE =
		"RTSException processing IADDR/IRNR trans request";
	private static final String MSG_SLEEP_INTERRUPTED =
		"Sleep interrupted ";
	// defect 10610
	private String csConfirmationSubject =
			"TxDMV eReminder Confirmation";
//	private String csConfirmationMessage =
//			"<b>Thanks for signing up for the TxDMV eReminder!</b> -- We'll send you an e-mail reminder 3 weeks before<br>"
//			+ "your vehicle registration expires. <br><br>" 			
//			+ "We also understand you\'re a busy person and might need another reminder. If you don\'t renew after the first e-mail,<br>"			
//			+ "we\'ll send another a week before your expiration date. If you do renew right away (remember in most counties you <br> " 			
//			+ " can renew online), we won\'t send the second reminder<br><br> " 
//			+ "Thanks you for being a Registered Texan and signing up for eReminder!"
//			+ "<br><br>";
	// defect 10807
	// everywhere you see "#imagehost# below, /NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/
	// replaced the RTSPOSProject URL forthe images
	private String csConfirmationMessage =
		"<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> "
			+ "<title></title></head><body>"
			+ "<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr><td><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/1_01.jpg\" alt=\"\" width=\"600\"\" height=\"153\" align=\"top\" /></td></tr>"
			+ "<tr><td><table width=\"100%\" border=\"0\" cellspacing=\"10\" cellpadding=\"0\" background=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/email-bg.jpg\">"
			+ "<tr><td align=\"left\" valign=\"top\"><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 12px;\"><font color=\"#0055a4\"><em style=\"font-size: 14px; font-weight: bold;\">Thanks for signing up for the TxDMV eReminder!</em></font>&mdash; We'll send you an e-mail reminder 3 weeks before your vehicle registration expires. <br /><br />"
			+ "We also understand you're a busy person and might need another reminder. If you don't renew after the first e-mail, we'll send another a week before your expiration date. If you do renew right away (remember in most counties you can renew online), we won't send the second reminder. <br /><br />"
			+ "Thank you for being a Registered Texan and signing up for eReminder! </span><br /><br /></p></p>"
			+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr><td align=\"center\" valign=\"top\"><p><span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\">Have you moved? Click here to <a href=\"#webhost#/NASApp/txdotrts/AddressChangeServlet\"><font color=\"#0054a5\">update your address with the TxDMV.</font></a></span></p>"
			+ "<span style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px;\">"
			+ "Join the registration conversation on Facebook and follow us on Twitter @TxDMV.<br />"
			+ "<a href=\"http://www.facebook.com/TxDMV\" target=\"_blank\"><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/facebook.png\" alt=\"Find us on Facebook\" width=\"101\" height=\"31\" border=\"0\" /></a><br />"
			+ "Make sure you receive your eReminder: Add <strong>eReminder@TxDMV.gov</strong> to your address book or safe-sender list.<br />"
			+ "And please don't reply to this automated message, because the sending mailbox cannot receive e-mails. <br /><br />"
			+ "To remove yourself from eReminder, click the button below.<br />"
			+ "<a href=\"#webhost#/NASApp/txdotrts/EReminderServlet?XXtask=5\" target=\"_blank\"><img src=\"#imagehost#/NASApp/txdotrts/images/apps/txdotrts/vtr/redesign_images/unsubscribe.png\" alt=\"Unsubscribe\" width=\"101\" height=\"28\" border=\"0\" /></a></span></td></tr>"
			+ "</table></td></tr></table></td></tr></table></BODY></HTML>";
	// end defect 10807	

	/**
	 * AddrChgServerBusiness constructor comment.
	 */
	public IVTRSServerBusiness()
	{
		super();
	}

	/**
	 * EReminder Change
	 * 
	 * @param avCompAddrChgData CompleteAddrChgData
	 * @return Object
	 * @throws RTSException
	 */
	private Object doEReminder(Vector avCompleteAddrChgData)
		throws RTSException
	{
		Object laObj = null;
		// defect 10610
		Vector lvEmailAddrs = new Vector();
		Vector lvEmailAddrsNoDups = new Vector();
		// end defect 10610
		try
		{
			for (int x = 0; x < avCompleteAddrChgData.size(); x++)
			{
				CompleteAddrChgData laCompleteAddrChgData =
					(CompleteAddrChgData) avCompleteAddrChgData.elementAt(x);
				VehicleBaseData laVehBaseData =	laCompleteAddrChgData.getVehBaseData();
				String lsVIN = laVehBaseData.getVin();
				if (lsVIN.length() > 4)
				{
					lsVIN =
						lsVIN.substring(lsVIN.length() - 4, lsVIN.length());
				}
				laVehBaseData.setVin(lsVIN);

				// Query the mainframe to get the complete data
				SearchVehicle laSearchVeh =
					new SearchVehicle(
						laVehBaseData,
						CommonConstants.ADD_EREMINDER);
				laObj = laSearchVeh.getVehicle();

				if (laObj == null)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						new NullPointerException(
							"Retrieved object from "
								+ "SearchVehicle is null !!!"));
				}
		
				if (!(laObj instanceof VehicleInquiryData))
				{
					return MessageConstants.MSG_SYSTEM_ERROR;
				}
		
				// Vehicle was successfully found.
				VehicleInquiryData laVehInqData = (VehicleInquiryData) laObj;
				MFVehicleData laMFVehDataOriginal =
					laVehInqData.getMfVehicleData();
				MFVehicleData laMFVehDataModified =
					(MFVehicleData) UtilityMethods.copy(laMFVehDataOriginal);
				RegistrationData laRegData = laMFVehDataModified.getRegData();
				
				VehicleUserData laVehUserData =
					laCompleteAddrChgData.getVehUserData();

				laRegData.setRecpntEMail(laVehUserData.getEmail());
				// defect 10610 
				if (laVehUserData.getEMailRenwlReqCd() == 1)
				{
					lvEmailAddrs.add(laVehUserData.getEmail());
				}
				// end defect 10610
				laRegData.setEMailRenwlReqCd(
					laVehUserData.getEMailRenwlReqCd());
		
				laRegData.setRenwlMailAddr(laVehUserData.getAddress());
				laRegData.setRecpntName(laVehUserData.getRecipientName());
				laRegData.setResComptCntyNo(laVehBaseData.getOwnerCounty());
				// ...then put RegistrationData into MFVehicleData
				laMFVehDataModified.setRegData(laRegData);
				// Build CompleteTransactionData object
				CompleteTransactionData laCompTransData =
					new CompleteTransactionData();
				// set Modified data
				laCompTransData.setVehicleInfo(laMFVehDataModified);
				// set Original data
				laCompTransData.setOrgVehicleInfo(laMFVehDataOriginal);

				String lsTransCode = TransCdConstant.IADDRE;
					
				// Add Retry for IADDR/IRNR 
				for (int i = 0; i < INT_MAX_RETRY; i++)
				{
					try
					{
						CacheManagerServerBusiness.setCacheServer(true);
						laCompTransData.setTransCode(lsTransCode);
						CommonServerBusiness laCSB = new CommonServerBusiness();
						laCSB.processData(
							GeneralConstant.COMMON,
							CommonConstant.PROC_EREMINDER,
							laCompTransData);
						break;
					}
					catch (RTSException aeRTSEx)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_RTSEX_ON_TRANS_CREATE);
		
						// Check to see if this is a duplicate key.
						if (aeRTSEx.getDetailMsg() != null
							&& aeRTSEx.getDetailMsg().indexOf(
								CommonConstant.DUPLICATE_KEY_EXCEPTION)
								> -1)
						{
							String lsVin =
								laMFVehDataModified.getVehicleData().getVin();
		
							String lsRegPltNo = laRegData.getRegPltNo();
		
							if (i < INT_MAX_RETRY - 1)
							{
		
								Log.write(
									Log.SQL_EXCP,
									this,
									MSG_RETRY
										+ i
										+ " "
										+ lsTransCode
										+ " VIN: "
										+ lsVin
										+ " PlateNo: "
										+ lsRegPltNo);
								try
								{
									Thread.sleep(1000);
								}
								catch (InterruptedException aeIEx)
								{
									Log.write(
										Log.SQL_EXCP,
										this,
										MSG_SLEEP_INTERRUPTED + i);
								}
							}
							else
							{
								Log.write(
									Log.SQL_EXCP,
									this,
									MSG_RETRY_EXCEEDED
										+ " "
										+ " VIN: "
										+ lsVin
										+ " PlateNo: "
										+ lsRegPltNo);
		
								throw aeRTSEx;
							}
						}
						else
						{
							// Some other exception.  Just throw it.
							throw aeRTSEx;
						}
					} 
				}
			}
			// defect 10610
			//Collections.sort(lvEmailAddrs);
			if (!lvEmailAddrs.isEmpty())
			{
				// the following statement removes duplicate String from a vector
				lvEmailAddrsNoDups = new Vector(new LinkedHashSet(lvEmailAddrs)); 
				EmailUtilEReminders laEmail = new EmailUtilEReminders();
				String[] larrEmailAddrsNoDups = new String[lvEmailAddrsNoDups.size()];
				for (int liX = 0; liX < lvEmailAddrsNoDups.size(); liX++)
				{
					larrEmailAddrsNoDups[liX] = (String)lvEmailAddrsNoDups.elementAt(liX);
				}
				String lsImageURL = "";				
				String lsWebURL = "";
				// defect 10807 use lsWebURL for images, not lsImageURL
				if (CommunicationProperty
					.getRegion()
					.equalsIgnoreCase("desktop")) 
				{
//					lsImageURL = "localhost:8080";
					lsWebURL = "http://localhost:9081";
				} 
				else 
				{ 
					if (CommunicationProperty.getRegion().equals("prod")) 
					{
//						lsImageURL = "wt-rts-appl:86";
						lsWebURL = "https://rts.texasonline.state.tx.us";
					} 
					else 
					{
//						lsImageURL = "wt-rts-ts1:8588";
						lsWebURL = "https://stage.rts.texasonline.state.tx.us";
					}
				}
//				csConfirmationMessage = csConfirmationMessage.replaceAll(
//					"#imagehost#", lsImageURL);	
				csConfirmationMessage = csConfirmationMessage.replaceAll(
					"#imagehost#", lsWebURL);
				// end defect 10807 - change to using lsWebURL		
				csConfirmationMessage = csConfirmationMessage.replaceAll(
					"#webhost#", lsWebURL);	
				//laEmail.
				if (!laEmail.sendEmail(larrEmailAddrsNoDups, csConfirmationSubject, csConfirmationMessage)) 
				{
					System.out.println(" email problem - msg not sent");
					//TODO Code error message here	
			// end defect 10610			
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		return (Object) MessageConstants.MSG_SUCCESSFUL_ADDRESS_CHANGE;
	}
	/**
	 * Used to Test Class.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
	}
	/**
	 * Used to process the data.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			switch (aiFunctionId)
			{
				case CommonConstants.ADD_EREMINDER :
					{
						Vector lvCompleteAddrChgData = (Vector)aaData;
						return doEReminder(lvCompleteAddrChgData);
					}
				default :
					{
						throw new RTSException(
							RTSException.JAVA_ERROR,
							new Exception(
								"EReminderServerBusiness - No such "
									+ "functionId:"
									+ aiFunctionId));
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeRTSEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeRTSEx);
		}
	}
}
