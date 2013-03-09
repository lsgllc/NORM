package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetDepositRecon;
import com.txdot.isd.rts.server.db.InternetDepositReconHstry;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.InternetDepositReconHstryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.EmailUtil;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * IVTRSDepositReconInsert.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		02/18/2009	Created Class.
 * 							defect 9936 Ver Defect_POS_D
 * B Brown		10/27/2009	Process multiple deposit dates
 * 							add svIDRHD
 * 							modify main(), doDepositReconInsert()
 * 							defect 10111 Ver Defect_POS_G
 * B Brown		12/10/2009	remove static identifier for variables and
 * 							methods where appropriate.
 * 							add final variables in place of hard coded
 * 							text
 * 							add DEP_RECON_INSERT_PROCESS =                     
 *							DEP_RECON_HSTRY_NOT_UPDATED         
 *							WITH_ERROR_MESSAGE 
 *							ERROR_CODE     
 *							String EMPTY_STRING         
 *							MESSAGE           
 *							HTML_BREAK               
 *							ERROR_EMAIL_PROBLEM             
 *							DRHREQID
 *							INSERT_ERROR                 
 *							DEPRECONINS_PROCESSING                
 *							SENDING_ERROR_EMAIL
 *							PRODUCTION       
 *							TEST                  
 *							UPDATE_HSTRY_ERROR                     
 *							ROLLBACK_ERROR                  
 *							NUM_ROWS_INSERTED                        
 *							PROC_DEP_RECON_HSTRY                           
 *							BANK_DEP_DATE
 *							UPDATE_ERROR                   
 *							ErrorMessagesData
 *							ERROR_NUM_NOT_FOUND      
 *							COULD_NOT_LOAD_STATIC_CACHE
 *							add loadStaticCache()
 *								createMsg()
 * 							modify logError()
 *			   					   main()
 *								   sendEmailErrorMsg()
 * 								   updateDepositReconHstry()
 * 								   doDepositReconInsert()
 * 							defect 10262 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * Batch process that will handle the insert of 
 * rts_itrnt_deposit_recon_tmp rows into rts_itrnt_deposit_recon
 * 
 * @version	Defect_POS_H	12/10/2009
 * @author	Bob Brown
 * <br>Creation Date:		02/18/2009 13:40:00
 */

public class IVTRSDepositReconInsert
{
	// defect 10262
	// remove static identifier where appropriate
	// end defect 10262
	private static final String DASHES = "====================";
	// Default FTP properties.
	private final String EMAIL_BODY =
		"The insert into rts_itrnt_deposit_recon process failed and "
			+ "requires attention. <br> Batchlog.log also contains "
			+ "error details.";
	private final String EMAIL_FROM =
		"TSD-RTS-POS@dot.state.tx.us";
	//		"RTS-IVTRS@dot.state.tx.us";
	// defect 10262
	private String csEmailSubject =
		"Deposit File Insert Processing error";
//	private String EMAIL_SUBJECT =
//		"Deposit File Insert Processing error";
	// end defect 10262	
	private final String EMAIL_TO =
		"TSD-RTS-POS@dot.state.tx.us";
//	private static final String ENCODETYPE = "UTF-8";
	private static final String END_OF = "End of ";
	private final String ERROR_DURRING = "Error durring ";
	private final String ERROR_HANDLER = "Error handler ";
	private final String PROD_DATASOURCE = "P0RTSDB";
	private final String START_OF = "Start of ";

	private static DatabaseAccess saDBAccess = null;
	
	private static InternetDepositReconHstryData saIDRHD = null;

	private InternetDepositRecon caIVTRSDepositReconAccess =
		null;
	private static InternetDepositReconHstry saIVTRSDepositReconHstryAccess =
		null;	
	private int ciReqId = 0;
	// defect 10111
	private static Vector svIDRHD = new Vector();
	// end defect 10111
	private RTSDate caProcsComplTimeStmp;
	private RTSDate caTmpInsTimeStmp;
	private RTSDate caBankDepositDate;
	private int ciErrMsgNo;
	private int ciSuccessIndi;
	private int ciTransCnt;
	// defect 10262
	private final String DEP_RECON_INSERT_PROCESS =
		"DepositRecon Insert processing";
	private final String DEP_RECON_HSTRY_NOT_UPDATED =
		"Could not update Deposit Recon Hstry ";
	private final String WITH_ERROR_MESSAGE = "with error message ";
	private final String ERROR_CODE = "Error code = ";
	private final static String EMPTY_STRING = "";
	private final String MESSAGE = "Message = ";
	private final String HTML_BREAK = "<br>";
	private final String ERROR_EMAIL_PROBLEM =
		"Could not send deposit file recon process error email";
	private final static String DRHREQID = "DepositReconHstryReqId ";
	private final static String INSERT_ERROR =
		"Error inserting into deposit recon file";
	private final static String DEPRECONINS_PROCESSING =
		"DepositReconInsert processing";
	private final String SENDING_ERROR_EMAIL = "SENDING ERROR EMAIL";
	private final String PRODUCTION = "Production: ";
	private final String TEST = "Test: ";
	private final String UPDATE_HSTRY_ERROR =
		"Could not update Deposit Recon Hstry";
	private final String ROLLBACK_ERROR =
		"Cound not do an end transaction rollback on the Recon Hstry table update";
	private final String NUM_ROWS_INSERTED =
		"Deposit file liRetRows inserted = ";
	private final String PROC_DEP_RECON_HSTRY =
		"Processing DepositReconHstry ";
	private final String BANK_DEP_DATE = "bank deposit date ";
	private final String UPDATE_ERROR =
		"Could not update temp Deposit Recon Hstry current completion date and time";
	private static ErrorMessagesData caErrorMsgData = null;
	private static final String ERROR_NUM_NOT_FOUND =
		"Could not find the following error in ErrorMessagesCache: ";
	private final static String COULD_NOT_LOAD_STATIC_CACHE =
		"Could not load static cache";	
	// end defect 10262

	/**
	 * Method logError
	 * 
	 * @param aeRTSEx RTSException
	 * @param asMsg String
	 * 
	 */
	private void logError(RTSException aeRTSEx, String asMsg)
	{
		aeRTSEx.printStackTrace();
		// defect 10262
//		BatchLog.write(
//			ERROR_DURRING + "DepositRecon Insert processing");
//		BatchLog.write("Error code = " + aeRTSEx.getCode());
//		if (asMsg != null && !asMsg.trim().equals(""))
//		{
//			BatchLog.write("Message = " + asMsg);
//		}
//		if (aeRTSEx.getMessage() != null
//			&& !aeRTSEx.getMessage().equals(""))
//		{
//			BatchLog.write(aeRTSEx.getMessage());
//		}
//		if (aeRTSEx.getDetailMsg() != null
//			&& !aeRTSEx.getDetailMsg().equals(""))
//		{
//			BatchLog.write(aeRTSEx.getDetailMsg());
//		}		
		BatchLog.write(
			this.ERROR_DURRING + DEP_RECON_INSERT_PROCESS);
		BatchLog.write(ERROR_CODE + aeRTSEx.getCode());
		// defect 10262
		// if (asMsg != null && !asMsg.trim().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(asMsg))
		// end defect 10262
		{
			BatchLog.write(MESSAGE + asMsg);
		}
		// defect 10262
//		if (aeRTSEx.getMessage() != null
//			&& !aeRTSEx.getMessage().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(aeRTSEx.getMessage()))
		{
			BatchLog.write(aeRTSEx.getMessage());
		}
//		if (aeRTSEx.getDetailMsg() != null
//			&& !aeRTSEx.getDetailMsg().equals(EMPTY_STRING))
		if (!UtilityMethods.isEmpty(aeRTSEx.getDetailMsg()))
		{
			BatchLog.write(aeRTSEx.getDetailMsg());
		}	
		// end defect 10262	
		if (saIDRHD != null)
		{		
				caBankDepositDate = saIDRHD.getBankDepositDate();
				ciErrMsgNo = aeRTSEx.getCode();
				ciTransCnt = saIDRHD.getTransCount();
				ciSuccessIndi = 0;
				caTmpInsTimeStmp = saIDRHD.getTmpInsrtTimestmp();
				caProcsComplTimeStmp = null;
				try
				{
					updateDepositReconHstry();
				}
				catch (RTSException aeRTSEx1)
				{
					// defect 10262
		//			System.err.println(
		//				"Could not update Deposit Recon Hstry with error "
		//					+ "message "
		//					+ aeRTSEx.getCode());
					aeRTSEx1.printStackTrace();
					BatchLog.error(
							DEP_RECON_HSTRY_NOT_UPDATED
							+ WITH_ERROR_MESSAGE
							+ aeRTSEx.getCode());
					// BatchLog.write("Could not update Deposit Recon Hstry");
					BatchLog.error(DEP_RECON_HSTRY_NOT_UPDATED);
					// end defect 10262
					BatchLog.error(aeRTSEx1.getMessage());
					BatchLog.error(aeRTSEx1.getDetailMsg());
				}
		}
//		finally
//		{
		// end defect 10262	
		try
		{
			sendEmailErrorMsg(
				aeRTSEx.getMessage()
				// defect 10262
//						+ "<br>"
//						+ aeRTSEx.getDetailMsg()
//						+ "<br>"
//						+ asMsg);
					+ HTML_BREAK
					+ aeRTSEx.getDetailMsg()
					+ HTML_BREAK
					+ asMsg);
				// end defect 10262	
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx1.printStackTrace();
			// defect 10262
//				BatchLog.write(
//					"Could not send deposit file recon process error email");
			BatchLog.error(ERROR_EMAIL_PROBLEM);
			// END defect 10262
			BatchLog.error(aeRTSEx1.getMessage());
			BatchLog.error(aeRTSEx1.getDetailMsg());
		}
//		}
	}
	/**
	 * Main method used for testing.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		
		// defect 10262
		IVTRSDepositReconInsert laIDRI = new IVTRSDepositReconInsert();
		laIDRI.loadStaticCache();		
		// end defect 10262

		try
		{
			BatchLog.write(DASHES);
			saDBAccess = new DatabaseAccess();
			saDBAccess.beginTransaction();
			saIVTRSDepositReconHstryAccess =
				new InternetDepositReconHstry(saDBAccess);
			// defect 10262 - uncomment the loading of saIDHRD as this 
			// is used in case of an error	
			// defect 10111	
			saIDRHD =
				saIVTRSDepositReconHstryAccess
					.qryMaxInternetDepositReconHstry();
			// end defect 10262
			svIDRHD =
				saIVTRSDepositReconHstryAccess
					.qryMaxDepositDates();	
			saDBAccess.endTransaction(DatabaseAccess.NONE);			
			// end defect 10111		
			//if (saIDRHD == null)
			if (svIDRHD == null || svIDRHD.size()==0)
			{
				// defect 10262
//				RTSException leRTSEx = new RTSException();
//				leRTSEx.setMessage(
//					"Error getting max row number in "
//					 defect 10111
//						+ "qryMaxInternetDepositReconHstry");
//						+ "qryMaxDepositDates");
//					// end defect 10111	
//				throw leRTSEx;
				throw createMsg(
					ErrorsConstant
						.ERR_NUM_COULD_NOT_GET_MAX_DEPOSITRECONHSTRY,
					EMPTY_STRING);
			}
//			else
//			{
//				// defect 10111
//				// siReqId = saIDRHD.getDepositReconHstryReqId();
//				for (int x=0;x<svIDRHD.size();x++)
//				{
////					InternetDepositReconHstry laDRHA = (InternetDepositReconHstry)saIDRHD.get(x);
//					svReqId.add(x,(InternetDepositReconHstryData)svIDRHD.get(x));
//					InternetDepositReconHstryData laIDRHD = (InternetDepositReconHstryData)svIDRHD.get(x);
//					BatchLog.write(
//						"Processing DepositReconHstry, "
//							+ "siReqId = "
//							+ x
//							+ laIDRHD.getDepositReconHstryReqId());
//					BatchLog.write(
//						"Processing DepositReconHstry, "
//							+ "bank deposit date = "
//							+ x
//							+ laIDRHD.getBankDepositDate());						
//				}
////				saDBAccess.endTransaction(DatabaseAccess.NONE);
//				// end defect 10111
//			}
		}
		catch (RTSException aeRTSEx1)
		{
			// defect 10262
//				leRTSEx1,
//				"Could not get Max DepositReconHstry siReqId "
//					+ "from DepositReconHstry ");
//			BatchLog.error(
//				COULD_NOT_GET_MAX_DEPOSITRECONHSTRY + DRHREQID);
			//leRTSEx1.setCode(2201);
			// logError(
			laIDRI.logError(
				aeRTSEx1,
				EMPTY_STRING);
			// end defect 10262		
			try
			{
				saDBAccess.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
			// stop the process if we can't get Max DepositReconHstry 
			// siReqId 				
			System.exit(1);
		}
		// defect 10262
		// IVTRSDepositReconInsert lsIDRI = new IVTRSDepositReconInsert();
		// end defect 10262

		try
		{
			laIDRI.doDepositReconInsert();
		}
		catch (RTSException aeRTSEx1)
		{
			// defect 10262
//			System.err.println(
//				"Error inserting into deposit recon file");
// 			logError(aeRTSEx1, "");
			BatchLog.error(INSERT_ERROR);
			laIDRI.logError(aeRTSEx1, EMPTY_STRING);
			// end defect 10262
		}
		finally
		{
			BatchLog.write(END_OF + DEPRECONINS_PROCESSING );
			BatchLog.write(DASHES);
		}
	}
	
	/**
	 * Handles loading cache for error messages
	 * 
	 */
	private void loadStaticCache()
	{
		try
		{
			// Load the Server Side Cache
			CacheManagerServerBusiness laCMSB =
				new CacheManagerServerBusiness();
			laCMSB.loadStaticCache();
		}
		catch (RTSException aeRTSEx)
		{
			logError(
			aeRTSEx,
			COULD_NOT_LOAD_STATIC_CACHE);
			System.exit(1);
		}
	}

	/**
	 * Handles the Error Processing for the transactions that need some
	 * attention.
	 * 
	 * @param avErrorTraceNos Vector
	 */
	private void sendEmailErrorMsg(String asEmailMsg)
		throws RTSException
	{
		BatchLog.write(START_OF + ERROR_HANDLER);
		// defect 10262
		// BatchLog.write("SENDING ERROR EMAIL");
		BatchLog.write(SENDING_ERROR_EMAIL);
		// end defect 10262

		if (SystemProperty.getDatasource().equals(PROD_DATASOURCE))
		{
			// defect 10262
			// EMAIL_SUBJECT = "Production: " + EMAIL_SUBJECT;
			csEmailSubject = PRODUCTION + csEmailSubject;
			// end defect 10262
		}
		else
		{
			// defect 10262
			// EMAIL_SUBJECT = "Test: " + EMAIL_SUBJECT;
			csEmailSubject = TEST + csEmailSubject;
			// end defect 10262
		}
		// Send email
		try
		{
			EmailUtil laEmailUtil = new EmailUtil();
			laEmailUtil.sendEmail(EMAIL_FROM, EMAIL_TO,
			// defect 10262	
			// EMAIL_SUBJECT,
			csEmailSubject,
			// EMAIL_BODY + "<br>" + asEmailMsg
			EMAIL_BODY + HTML_BREAK + asEmailMsg);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,
			aeEx.getMessage());
		}
//		if (!laEmailUtil
//			.sendEmail(
//				EMAIL_FROM,
//				EMAIL_TO,
//				// defect 10262	
//				// EMAIL_SUBJECT,
//				csEmailSubject,
//				// EMAIL_BODY + "<br>" + asEmailMsg
//				EMAIL_BODY + HTML_BREAK + asEmailMsg
//				// end defect 10262
//				))
//		{
			// defect 10262
//			System.err.println(
//				"Error sending deposit file process " + "error email");
			// leRTSEx.setCode(2207);
//			leRTSEx.setMessage(
//				"Error sending deposit file process " + "error email");		
//			throw leRTSEx;
			// end defect 10262			
//		}
		// BatchLog.write(END_OF + "SENDING ERROR EMAIL");
		BatchLog.write(END_OF + SENDING_ERROR_EMAIL);
		// end defect 10262
	}

	/**
	 * Method updateDepositReconHstry
	 * throws RTSException
	 * 
	 */
	private void updateDepositReconHstry()
		throws RTSException
	{
		try
		{	
			saDBAccess.beginTransaction();
			InternetDepositReconHstryData laIVTRSDRHD =
				new InternetDepositReconHstryData();
			laIVTRSDRHD.setProcsComplTimestmp(caProcsComplTimeStmp);
			laIVTRSDRHD.setDepositReconHstryReqId(ciReqId);
			laIVTRSDRHD.setErrMsgNo(ciErrMsgNo);
			laIVTRSDRHD.setTransCount(ciTransCnt);
			laIVTRSDRHD.setSuccessfulIndi(ciSuccessIndi);
			laIVTRSDRHD.setBankDepositDate(caBankDepositDate);
			// defect 10262
			// laIVTRSDRHD.setTmpInsrtTimestmp(caTmpInsTimeStmp);
			laIVTRSDRHD.setTmpInsrtTimestmp(null);
			// end defect 10262
			saIVTRSDepositReconHstryAccess
				.updInternetDepositReconHstry(
				laIVTRSDRHD);
			saDBAccess.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx1)
		{
			try
			{
				// defect 10262
//				System.err.println(
//					"Could not update Deposit Recon Hstry");
				BatchLog.error(UPDATE_HSTRY_ERROR);
				// end defect 10262	
				saDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				// defect 10262
//				System.err.println(
//					"Cound not do an end transaction rollback "
//						+ "on the Recon Hstry table update");
				BatchLog.error(ROLLBACK_ERROR);	
				// end defect 1026	
			}
			finally
			{
				throw aeRTSEx1;
			}
		}
		// defect 10262
		catch (Exception aeEx)
		{
			throw createMsg(
				ErrorsConstant
					.ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD,
				aeEx.getMessage());		
		}
		// end defect 10262
	}

	/**
	 * IVTRSDepositReconInsert.java Constructor
	 */
	public IVTRSDepositReconInsert()
	{
		super();
	}

	/**
	 * Inserts into the rts_itrnt_deposit_recon table
	 * 
	 * 1. Load the FTP properties that are stored in a separate file.
	 * 2. Do an FTP get to retrieve the Turn Around file from the cpa.
	 * 3. Load the Deposit file.
	 * 4. insert into a temp table andedit the contents 
	 * 
	 * @throws RTSException
	 */
	private void doDepositReconInsert() throws RTSException
	{
		// defect 10262
		// BatchLog.write(START_OF + "DepositRecon Insert processing");
		BatchLog.write(START_OF + DEP_RECON_INSERT_PROCESS);
		// end defect 10262
//		Vector lvDepositRecon = null;
		caIVTRSDepositReconAccess =
			new InternetDepositRecon(saDBAccess);
		try
		{
			saDBAccess.beginTransaction();
			int liNumRecsInserted =
				caIVTRSDepositReconAccess.insInternetDepositRecon();
			// defect 10262	
			saDBAccess.endTransaction(DatabaseAccess.COMMIT);
//			BatchLog.write(
//				"Deposit file liRetRows inserted = "
//					+ liNumRecsInserted);
			BatchLog.write(NUM_ROWS_INSERTED + liNumRecsInserted);
			// saDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// end defect 10262		
			
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				saDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeEx)
			{
				aeEx.printStackTrace();
			}
			finally
			{
				// defect 10262
//				BatchLog.write(
//					"Could not insert into "
//						+ "rts_itrnt_deposit_recon "
//						+ "table");
				BatchLog.write(INSERT_ERROR);
				// end defect 10262		
				throw aeRTSEx;
			}
		}
		try
		{
			  // defect 10111
			  ciErrMsgNo = 0;
			  ciSuccessIndi = 1;
			  caProcsComplTimeStmp = new RTSDate();
			  for (int x=0;x<svIDRHD.size();x++)
			  {
				  InternetDepositReconHstryData laIDRHD1 = (InternetDepositReconHstryData)svIDRHD.get(x);
				  ciReqId = laIDRHD1.getDepositReconHstryReqId();
				  // defect 10262
				  // saDBAccess = new DatabaseAccess();
				  // end defect 10262
				  saDBAccess.beginTransaction();
				  saIVTRSDepositReconHstryAccess =
						new InternetDepositReconHstry(saDBAccess);
						
			 	 InternetDepositReconHstryData laIDRHD2 =
					saIVTRSDepositReconHstryAccess
						.qryInternetDepositReconHstryReqID(
						ciReqId);
				  saDBAccess.endTransaction(DatabaseAccess.NONE);
				  caBankDepositDate = laIDRHD2.getBankDepositDate();
				  ciTransCnt = laIDRHD2.getTransCount();
				  caTmpInsTimeStmp = laIDRHD2.getTmpInsrtTimestmp();
				  // defect 10262
//				  BatchLog.write(
//					  "Processing DepositReconHstry, "
//						  + "siReqId = "
//						  + x
//						  + laIDRHD2.getDepositReconHstryReqId());
//						  
//				  BatchLog.write(
//					  "Processing DepositReconHstry, "
//						  + "bank deposit date = "
//						  + x
//						  + laIDRHD2.getBankDepositDate());
				BatchLog.write(
					PROC_DEP_RECON_HSTRY
						+ DRHREQID
						+ laIDRHD2.getDepositReconHstryReqId());
						  
				BatchLog.write(
						PROC_DEP_RECON_HSTRY + BANK_DEP_DATE
						+ laIDRHD2.getBankDepositDate());
				// saDBAccess.endTransaction(DatabaseAccess.NONE);		
				// end defect 10262		  
						  	
			  	updateDepositReconHstry();		  					
			  }			  	
//				saBankDepositDate = saIDRHD.getBankDepositDate();
//				siErrMsgNo = 0;
//				siTransCnt = saIDRHD.getTransCount();
//				siSuccessIndi = 1;
//				saTmpInsTimeStmp = saIDRHD.getTmpInsrtTimestmp();
//				saProcsComplTimeStmp = new RTSDate();
//				updateDepositReconHstry();
			// end defect 10111	
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10262
//			BatchLog.write(
//				"Could not update temp Deposit Recon "
//					+ "Hstry current completion date and time");
			BatchLog.error(UPDATE_ERROR);
			// end defect 10262		
			throw aeRTSEx;
		}
	}
	/**
	 * Method createMsg
	 * @param aiMsgNo int
	 */
	private static RTSException createMsg(int aiMsgNo, String lsDtlMsg)
	{
		RTSException leRTSEx =
			new RTSException(aiMsgNo);
		caErrorMsgData =
			ErrorMessagesCache.getErrMsg(aiMsgNo);
		if (caErrorMsgData == null)
		{
			leRTSEx.setMessage(ERROR_NUM_NOT_FOUND + aiMsgNo);
		}
		else
		{
			leRTSEx.setMessage(caErrorMsgData.getErrMsgDesc());
		}
		if (!UtilityMethods.isEmpty(lsDtlMsg))
		{
			leRTSEx.setDetailMsg(lsDtlMsg);
		}
		caErrorMsgData = null;
		return leRTSEx;
	}
}
