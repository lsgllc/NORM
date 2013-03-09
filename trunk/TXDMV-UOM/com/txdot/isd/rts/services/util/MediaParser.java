package com.txdot.isd.rts.services.util;

import java.io.*;
import java.util.*;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.exception.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
/*
 *
 * MediaParser.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  Imported new class.
 * 							Ver 5.2.0	
 * J Rue		06/27/2004	Add try/catch box to catch file not found
 *							 exceptions
 *							method updateRecord()
 *							defect 7298 VER 5.2.1
 * J Rue		07/27/2004	Create new method to write data to diskette
 *							Change convertParseableToString() from
 *								private to public
 *							new updateRecord(File, Vector)
 *							depercate updateRecord(File, parseable, 
 *													string, int)
 *							modify convertParseableToString()
 *							defect 7240 VER 5.2.1
 * J Rue		07/18/2005	Update message for file not found
 * 							Clean up variable names
 * 							modify updateRecord()
 * 							defect 8227, 7898 Ver 5.2.3
 * J Rue		08/25/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3 
 * J Rue		08/29/2005	Log all exceptions
 * 							modify updateRecord()
 * 							defect 8304 Ver 5.2.2  Fix 6
 * J Rue		12/14/2005	Shift code 1 tab space for 5.2.3 standard
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/21/2008	Add process to handle CustSupplyIndi and
 * 							CustSupplyPltAge
 * 							add getRecordType()
 * 							modify parse()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		05/22/2008	Add new record types 3 and 4
 * 							modify getRecordType()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		06/02/2008	Replace ghost number. Change Ver to 
 * 								Defect_POS_A
 * 							modify setRecordType()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		12/30/2008	Remove comma from error message
 * 							Move variable to class level liRefer_To_Recs
 * 								to csReferToRecs
 * 							modify getRecordType()
 * 							defect 9869 Ver Defect_POS_D
 * J Rue		01/07/2009	Rename csReferToRecs to REFER_TO_REC
 * 							modify getRecordType()
 * 							defect 9869 Ver Defect_POS_D
 * J Rue		01/12/2009	Remove "s" from Record int the varaible 
 * 							REFER_TO_REC
 * 							defect 9869 Ver Defect_POS_D
 * J Rue		01/15/2009	add "<br>" HTML command for next line
 * 							modify getRecordType()
 * 							defect 9869 Ver Defect_POS_D
 * J Rue		02/27/2009	Change variable name and values for ELT
 * 							modify getRecordType()
 * 							defect 9973 Ver Defect_POS_E
 * B Hargrove	05/26/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							DiskParser / MediaParser 
 * 							defect 10075 Ver Defect_POS_F  
 * T Pederson	06/23/2010	Changed variable name and values for Email
 * 							disk format.
 * 							add BASE_123_DEL, BASE_124_DEL, 
 * 							EMAIL_125_DEL, EMAIL_126_DEL, RSPS_131_DEL,
 * 							RSPS_132_DEL, RSPS_EMAIL_133_DEL and
 * 							RSPS_EMAIL_134_DEL
 * 							modify getRecordType()
 * 							defect 10509 Ver POS_650
 * T Pederson	01/19/2011	Changed variable name and values for 
 * 							VehColor disk format.
 * 							add BASE_125_DEL, BASE_126_DEL, 
 * 							VEHCOLOR_127_DEL, VEHCOLOR_128_DEL, 
 * 							RSPS_133_DEL, RSPS_134_DEL, 
 * 							RSPS_VEHCOLOR_135_DEL and 
 * 							RSPS_VEHCOLOR_136_DEL
 * 							modify getRecordType()
 * 							defect 10709 Ver POS_670
 * K Harrell	02/15/2011	delete VEHCOLOR_127_DEL, 
 * 								RSPS_VEHCOLOR_135_DEL
 * 							add VEHCOLOR_129_DEL, 
 * 								RSPS_VEHCOLOR_137_DEL
 * 							modify getRecordType() 
 * 							defect 10752 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class provides methods for reading(parsing), and writing 
 * external media data.
 * 
 * @version	6.7.0  			02/15/2011
 * @author	Michael Abernethy
 * <p>Creation Date:		08/06/2002
 */
public class MediaParser
{
	private final static String FILE_NOT_FOUND =
		"RSPS_DTA.DAT - file not found";

	// Defect 9869
	// 	Make variable a class level constant
	public static String REFER_TO_REC = "<br> Refer to Record: ";
	// end defect 9869

	//	// defect 10509
	//	// New base format
	//	private final static int BASE_123_DEL = 123;
	//	private final static int BASE_124_DEL = 124;
	//
	//	// Added fields EMailRenwlReqCd and RecpntEMail
	//	private final static int EMAIL_125_DEL = 125;
	//	private final static int EMAIL_126_DEL = 126;
	//
	//	// Base format with RSPS added fields
	//	private final static int RSPS_131_DEL = 131;
	//	private final static int RSPS_132_DEL = 132;
	//
	//	// New format with RSPS added fields
	//	private final static int RSPS_EMAIL_133_DEL = 133;
	//	private final static int RSPS_EMAIL_134_DEL = 134;
	//	// end defect 10509

	// defect 10709
	// New base format
	private final static int BASE_125_DEL = 125;
	private final static int BASE_126_DEL = 126;

	// Added fields MajorColorCd and MinorColorCd
	private final static int VEHCOLOR_128_DEL = 128;
	private final static int VEHCOLOR_129_DEL = 129;

	// Base format with RSPS added fields
	private final static int RSPS_133_DEL = 133;
	private final static int RSPS_134_DEL = 134;

	// New format with RSPS added fields
	private final static int RSPS_VEHCOLOR_136_DEL = 136;
	private final static int RSPS_VEHCOLOR_137_DEL = 137;
	// end defect 10709

	/**
	 * Convert data objects to string.
	 * 
	 * @param aaParse com.txdot.isd.rts.services.util.Parseable
	 * @param asDelim String
	 * @return java.lang.String
	 */
	public static String convertParseableToString(
		Parseable aaParse,
		String asDelim)
	{
		String lsLline = CommonConstant.STR_SPACE_EMPTY;
		String lsField = CommonConstant.STR_SPACE_EMPTY;
		int liJ = 1;
		while ((lsField = aaParse.getField(liJ)) != null)
		{
			if (lsField.equals(CommonConstant.STR_NULL))
			{
				lsField = CommonConstant.STR_SPACE_EMPTY;
			}
			lsLline = lsLline + lsField + asDelim;
			liJ++;
		}
		lsLline = lsLline.substring(0, lsLline.length() - 1);
		return lsLline;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @param apfsFile java.io.File
	 * @param aaParse com.txdot.isd.rts.services.util.Parseable
	 * @param asDelimiter String
	 * @return java.util.Vector
	 * @throws IOException
	 */
	public synchronized static Vector parse(
		File apfsFile,
		Parseable aaParse,
		String asDelimiter)
		throws IOException, RTSException
	{
		Vector lvVct = new Vector();
		FileInputStream lpfsFIS = new FileInputStream(apfsFile);
		BufferedReader lpfsIN =
			new BufferedReader(new InputStreamReader(lpfsFIS));
		String lsLine = CommonConstant.STR_SPACE_EMPTY;
		// defect 9656
		//	Count number of fields for first record
		int liRecType = getRecordType(apfsFile, asDelimiter);
		// end defect 9656
		while ((lsLine = lpfsIN.readLine()) != null)
		{
			if (lsLine == null
				|| lsLine.trim().equals(CommonConstant.STR_SPACE_EMPTY))
			{
				continue;
			}
			Parseable laRecord =
				(Parseable) UtilityMethods.copy(aaParse);

			// defect 9656
			//	Set Record Type for each record
			laRecord.setRecType(liRecType);
			int i = 1;
			lsLine = lsLine.trim();
			while (lsLine.indexOf(asDelimiter) > -1)
			{
				// Get next field
				String token =
					lsLine.substring(0, lsLine.indexOf(asDelimiter));
				//  Read record minus the current token.
				lsLine =
					lsLine.substring(lsLine.indexOf(asDelimiter) + 1);
				// defect 9656
				//	Add Record Type to parm
				laRecord.setField(i, token.trim(), liRecType);
				// end defect 9656
				
				// defect 10752 
				if (SystemProperty.isDevStatus())
				{
					System.out.println("i=" + i);
				}
				// end defect 10752 
				
				i++;
			}
			// Add last fields (RSPSOrigPrntDate) to record
			laRecord.setField(i, lsLine.trim(), liRecType);
			lvVct.add(laRecord);
		}
		lpfsIN.close();
		lpfsFIS.close();
		return lvVct;
	}
	/**
	 * Return File Type
	 * 
	 * @param apfsFile
	 * @param asDelim
	 * @return liFieldCnt
	 * @throws IOException, RTSException
	 */
	private static int getRecordType(File apfsFile, String asDelimiter)
		throws RTSException, IOException
	{
		boolean lbError = false;
		int liRecordType = 0;
		int liPrevFieldCnt = 0;
		int liRecNo = 0;
		String lsLine = CommonConstant.STR_SPACE_EMPTY;

		// Read File into FileInputStream
		FileInputStream lpfsFIS = new FileInputStream(apfsFile);
		// Read FileInputStream into a BufferedReader
		BufferedReader lpfsIN =
			new BufferedReader(new InputStreamReader(lpfsFIS));

		// 	Read record. 
		while ((lsLine = lpfsIN.readLine()) != null)
		{
			int liFieldCnt = 0;
			liRecNo++;

			if (lsLine == null
				|| lsLine.trim().equals(CommonConstant.STR_SPACE_EMPTY))
			{
				continue;
			}
			if (lsLine != null
				&& !lsLine.trim().equals(CommonConstant.STR_SPACE_EMPTY))
			{
				lsLine = lsLine.trim();
				// Read record until all the delimiters have deen found
				while (lsLine.indexOf(asDelimiter) > -1)
				{
					// Remove each fields in the line until all fields
					//	have been read. 
					lsLine =
						lsLine.substring(
							lsLine.indexOf(asDelimiter) + 1);
					liFieldCnt++;
				}
			}
			// defect 10709, 10752 
			// New format added for email capture
			//	125/126 => New base format includes EMail data
			//		=> Disk format (RecordType 1) EMail
			//
			//	128/129 => Addition of MajorColorCd and MinorColorCd 
			//		=> Disk format (RecordType 2) VehColor
			//
			//	133/134 => New base format w/RSPS data
			//		=> Disk format (RecordType 3) EMail + RSPS
			//
			//	136/137 => Addition of VehColor w/RSPS data
			//		=> Disk format (RecordType 4) VehColor + RSPS
			//
			if (liPrevFieldCnt == 0 || (liFieldCnt == liPrevFieldCnt))
			{
				if (liFieldCnt == BASE_125_DEL
					|| liFieldCnt == BASE_126_DEL)
				{
					liRecordType = DealerTitleData.BASE_FRMT_CD;
					liPrevFieldCnt = liFieldCnt;
				}
				else if (
					liFieldCnt == VEHCOLOR_128_DEL
						|| liFieldCnt == VEHCOLOR_129_DEL)
				{
					liRecordType = DealerTitleData.VEHCOLOR_FRMT_CD;
					liPrevFieldCnt = liFieldCnt;
				}
				else if (
					liFieldCnt == RSPS_133_DEL
						|| liFieldCnt == RSPS_134_DEL)
				{
					liRecordType = DealerTitleData.RSPS_FRMT_CD;
					liPrevFieldCnt = liFieldCnt;
				}
				else if (
					liFieldCnt == RSPS_VEHCOLOR_136_DEL
						|| liFieldCnt == RSPS_VEHCOLOR_137_DEL)
				{
					liRecordType =
						DealerTitleData.RSPS_VEHCOLOR_FRMT_CD;
					liPrevFieldCnt = liFieldCnt;
				}
				// end defect 10709, 10752 
				else
				{
					lbError = true;
					break;
				}
			}
			else
			{
				lbError = true;
				break;
			}
		}

		// Throw exception if any record on disk is not of known 
		//	define format or disk contains multiple formats 
		if (lbError)
		{
			// defetc 9869
			//	No comma is needed. Only one record will be identified.
			//  Error message will be displayed in 
			//	VCEntryPreferencesDTA001.processData()
			//  "<br>" is the HTML command for next line
			//			String liRefer_To_Recs = "\n Refer to Records: ";
			//			liRefer_To_Recs += CommonConstant.STR_COMMA
			//				+ (liRecNo);
			String[] larrStrArray = new String[1];
			larrStrArray[0] = REFER_TO_REC + liRecNo;

			throw new RTSException(237, larrStrArray);
		}

		return liRecordType;
	}
	/**
	 * Write record to input media
	 * 
	 * @param apfsFile java.io.File
	 * @param aaParse com.txdot.isd.rts.services.util.Parseable
	 * @param asDelim String
	 * @param aiRecNum int
	 * @depercate
	 */
	public synchronized static void updateRecord(
		File apfsFile,
		Parseable aaParse,
		String asDelim,
		int aiRecNum)
		throws IOException
	{
		Vector lvData = new Vector();
		FileInputStream lpfsFIS = new FileInputStream(apfsFile);
		BufferedReader laBRin =
			new BufferedReader(new InputStreamReader(lpfsFIS));
		String lsLine = CommonConstant.STR_SPACE_EMPTY;
		while ((lsLine = laBRin.readLine()) != null)
		{
			if (lsLine == null
				|| lsLine.trim().equals(CommonConstant.STR_SPACE_EMPTY))
				continue;
			lvData.add(lsLine);
		}
		// Update record in vector based on record number
		lvData.set(
			aiRecNum,
			convertParseableToString(aaParse, asDelim));
		laBRin.close();
		lpfsFIS.close();
		FileOutputStream lpfaFOS = new FileOutputStream(apfsFile);
		PrintWriter laPrntWrtr = new PrintWriter(lpfaFOS);
		// Write vector of transactions to file
		for (int i = 0; i < lvData.size(); i++)
		{
			String record = (String) lvData.get(i);
			laPrntWrtr.println(record);
			laPrntWrtr.flush();
		}
		laPrntWrtr.close();
		lpfaFOS.close();
	}
	/**
	 * Write records with RSPSIds to RSPS_DTA.DAT processing file.
	 * 
	 * @param apfsFile java.io.File
	 * @param avData Vector
	 * @throws RTSException
	 */
	public synchronized static void updateRecord(
		File apfsFile,
		Vector avData)
		throws RTSException
	{
		// Defect 7298
		//  Add try/catch box to catch file not found exception
		if (avData.size() > 0)
		{
			try
			{
				FileOutputStream lpfsFOS =
					new FileOutputStream(apfsFile);
				PrintWriter lpfsOUT = new PrintWriter(lpfsFOS);
				// Write dealer transactions to file
				for (int liIndex = 0;
					liIndex < avData.size();
					liIndex++)
				{
					String lsRecord = (String) avData.get(liIndex);
					lpfsOUT.println(lsRecord);
					lpfsOUT.flush();
				}
				lpfsOUT.close();
				lpfsFOS.close();
			}
			// defect 8227
			//	Update error message
			catch (FileNotFoundException aeFNFEx)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					FILE_NOT_FOUND,
					CommonConstant.STR_ERROR);
			}
			catch (IOException aeIOEx)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					FILE_NOT_FOUND,
					CommonConstant.STR_ERROR);
			}
			// defect 8304
			//	Log all exceptions
			catch (Exception aeExc)
			{
				throw new RTSException(RTSException.JAVA_ERROR);
			}
			// end defect 8304
			// end defect 8227
		}
		// End Defect 7298
	}
	/**
		* Insert the method's description here.
		* @param apfsFile java.io.File
		* @param avVct java.util.Vector
		* @param asDelim java.lang.String
		*/
	public synchronized static void write(
		File apfsFile,
		Vector avVct,
		String asDelim)
		throws IOException
	{
		FileOutputStream lpfsFOS = new FileOutputStream(apfsFile);
		PrintWriter lpfsOUT = new PrintWriter(lpfsFOS);
		for (int i = 0; i < avVct.size(); i++)
		{
			Parseable laParsed = (Parseable) avVct.get(i);
			String line = convertParseableToString(laParsed, asDelim);
			lpfsOUT.println(line);
		}
		lpfsOUT.flush();
		lpfsOUT.close();
		lpfsFOS.close();
	}
}
