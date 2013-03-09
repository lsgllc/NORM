package com.txdot.isd.rts.services.util;

import java.io.*;
import java.net.URL;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.webapps.util.WebAgentProperty;
/*
 * JetPclProcess.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		02/24/2011	Created Class.
 * 							defect 10718 Ver 6.7.0
 * ---------------------------------------------------------------------
 */
/**
 * This class handles the conversion from a PCL file to a PDF file.
 * 
 * The original java code for this class was received from:
 * Joseph A. Puglielli
 * Copyright © 2004-2011 Tech Know Systems, Inc.
 * 
 * The code was altered to use String[] aarrArgs into main, and to use
 * StreamGobbler to consume the command output. If StreamGobbler is not
 * used, the process hangs and waits for a place to put the output.
 *
 * @version	6.7.0			02/24/2011
 * @author	Bob Brown
 * <br>Creation Date:		02/24/2011  12:00:01
 */
public class JetPclProcess
{	
	private final static String PROCESS_NEEDS_2_PARMS = "JetPcl process " +		"needs 2 parameters. Stopping the conversion process: ";
	private final static String INPUT_STREAM_PASSED = "JetPCL Process " +		"input stream passed in to save as RCP";
	private final static String SAVREQID = " savReqID = "; 	
	private final static String NO_SAVREQID = "No savReqID parm passed " +		"to JetPCL process."; 	
	private final static String NO_PARMS = "No parms passed to JetPCL " +		"process. Terminating the coversion process";
	private final static String EMPTY_STRING = "";
	
	private final String RCP = ".RCP"; 
	private final String PDF = ".PDF";
	private final String RCP_DIR = "rcp/"; 
	private final String PDF_DIR = "pdf/";  	
	private final String FILE_SEPARATOR = "file.separator";
	private final String RECEIPT_DIR = "WebAgentReceipt";	
	private final String BLANK = " ";
	
	private final String JETPCL_COMMAND_TXT = "JetPCL Conversion command = " ;
	private final String ERROR_TXT = "ERROR";
	private final String OUTPUT_TXT = "OUTPUT";	
	private final String EXIT_VAL_TXT = "JetPCL ExitValue: ";
	private final String PDF_PROB_TXT = "JetPCL problem creating PDF receipt for : ";
	private final String CONV_COMPL_TXT = "JetPCL PDF Conversion completed successfully for ";
	private final String DEL_ERROR_TXT = "Delete file error. File: " ;
	private final String PROTECTED_TXT = " write proctected: "; 
	/**
	 * JetPclProcess.java Constructor
	 * 
	 * 
	 */
	public JetPclProcess()
	{
		super();
	}  

	public static void main(String[] aarrArgs)
	{
		String lsSavReqID = EMPTY_STRING;
		if (aarrArgs.length != 2)
		{
			BatchLog.write(PROCESS_NEEDS_2_PARMS); 
			System.exit(1);
		}
		if (aarrArgs[0] != null
			&& aarrArgs[0].length() > 0)
		{
			BatchLog.write(INPUT_STREAM_PASSED);
			if (aarrArgs[1] != null
				&& aarrArgs[1].length() > 0)
			{
				lsSavReqID = aarrArgs[1];
				BatchLog.write(SAVREQID + lsSavReqID);
			}
			else
			{
				BatchLog.write(NO_SAVREQID);
				System.exit(1);	
			}
		}
		else
		{
			BatchLog.write(NO_PARMS);
			System.exit(1);	
		}
		
		JetPclProcess laJetPclProcess = new JetPclProcess();
		laJetPclProcess.convertFile(aarrArgs[0], lsSavReqID);
	}
	
	public boolean convertFile(String asPDFStream, String asSaveRequestID)
	{
		String lsInFile  = asSaveRequestID + RCP;
		String lsOutFile = asSaveRequestID + PDF;
		String lsCommand = EMPTY_STRING;
		int liExitVal = 0; 
		String lsFileSeparator = System.getProperty(FILE_SEPARATOR);
		Process laProcHndl = null;
		boolean lbConverted = false;
		 try 
		 {
			if (WebAgentProperty.getRegion().equalsIgnoreCase(WebAgentProperty.DESKTOP)) 
			{
				URL laReceiptDir =
					WebAgentProperty.class.getClassLoader().getResource(
						lsFileSeparator + RECEIPT_DIR);
				lsInFile = laReceiptDir.getPath().toString().substring(1) + RCP_DIR + lsInFile;
				saveFile(asPDFStream, lsInFile);		
				lsOutFile =
					laReceiptDir.getPath().toString().substring(1) + PDF_DIR + lsOutFile;
			}	 
			else 
			{
				lsInFile = WebAgentProperty.getInputDir() + lsInFile;
				saveFile(asPDFStream, lsInFile);
				lsOutFile = WebAgentProperty.getOutputDir() + lsOutFile;
			}
//			lsURL = WebAgentProperty.getUrl() + lsOutFileURL + TARGET_BLANK;
//			System.out.println(" before JetPCL conversion   date = " + new java.util.Date() + " asSaveRequestID = " + asSaveRequestID);
			lsCommand =	WebAgentProperty.getJetPclCmd() + BLANK
						+ lsInFile
						+ BLANK
						+ lsOutFile;
			BatchLog.write(JETPCL_COMMAND_TXT + lsCommand + " before JetPCL conversion, asSaveRequestID = " + asSaveRequestID);
			/* Run the JetPCL lsCommand line process. */
			laProcHndl = Runtime.getRuntime().exec(lsCommand);
			BatchLog.write(JETPCL_COMMAND_TXT + lsCommand + " after JetPCL conversion, asSaveRequestID = " + asSaveRequestID);
			StreamGobbler laErrorGobbler =
				new StreamGobbler(laProcHndl.getErrorStream(), ERROR_TXT);

			// any output?
			StreamGobbler laOutputGobbler =
				new StreamGobbler(laProcHndl.getInputStream(), OUTPUT_TXT);

			// kick them off
			laErrorGobbler.start();
			laOutputGobbler.start();

			liExitVal = laProcHndl.waitFor();
			BatchLog.write(EXIT_VAL_TXT + liExitVal + " after stream Gobbler and waitfor, asSaveRequestID = " + asSaveRequestID);
			if (liExitVal == 0) 
			{
				lbConverted = true;
				deleteFile(lsInFile);
			} 
			else 
			{
				BatchLog.write(
					PDF_PROB_TXT + lsOutFile);
			}
		}
	
		 catch (Exception laIOE)
			 {
				 laIOE.printStackTrace();
				 BatchLog.error(laIOE.getMessage());
			 }
		
	
		 finally 
			 {
				 BatchLog.write(CONV_COMPL_TXT + lsOutFile);
				laProcHndl.destroy();					
			 }
			 
		return lbConverted;	 
	 }
		
	private void saveFile(String asPDFStream, String asRcptFileName) throws RTSException
	 {	
		File laFile = new File(asRcptFileName);
		try 
		{
			laFile.createNewFile();
		}
		catch (IOException aeIOExp)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				aeIOExp);
		}
		try
		{
			FileOutputStream laFileOutStream =
				new FileOutputStream(asRcptFileName, true);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(asPDFStream);
	//			laBuffWtr.newLine();
			laBuffWtr.flush();
			laFileOutStream.close();
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				aeFNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				aeIOEx);
		}
	
	 }
	 
	 private void deleteFile (String asFileName)
	 {
		File laFileName = new File(asFileName);

		// Make sure the file or directory exists and isn't write protected
		if (!laFileName.exists())
		{
			BatchLog.write(
				DEL_ERROR_TXT + asFileName);
		}

		if (!laFileName.canWrite())
		{
			BatchLog.write(
				DEL_ERROR_TXT
					+ asFileName
					+ PROTECTED_TXT);
		}

		if (!laFileName.delete())
		{
			BatchLog.write(DEL_ERROR_TXT + asFileName);
		}
	  }
}