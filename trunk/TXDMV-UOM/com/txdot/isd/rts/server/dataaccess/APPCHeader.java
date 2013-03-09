package com.txdot.isd.rts.server.dataaccess;

/*
 *
 * APPCHeader.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Rich Hicks	08/23/2001	Created the Class
 * M Rajangam 	08/30/2001	Added to dataaccess package
 * Ray Rowehl	06/07/2004	Add JavaDoc to help Document the
 *							various constants defined.
 *							defect 6701 Ver 5.2.1
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	10/14/2006	Make this class public so the MfAccess 
 * 							formatter classes can use it.
 * 							modify class definition
 * 							defect 6701 Ver Exempts
 * J Rue		04/27/2007	New method to return record length.
 * 							add rtnMfOutputLength()
 * 							defect 8983 Ver Special Plates
 * J Rue		05/14/2007	Comment out used variables
 * 							defect 8983, Special Plates
 * J rue		05/27/2008	Add comments to set asHdddtsq to "Y"
 * 							to view data string on MF
 * 						 	modify setMainframeHeader()
 * 							defect 9656 Ver Defect_POS_A
 * ---------------------------------------------------------------------
 */

/**
 * <p> Serves as the Header record that is sent to the CICS program
 * (HEADER_U) as well as the Header record received from the CICS
 * program (HEADER_D).
 * 
 * <p> This record is 256 bytes long.
 * 
 * <p> However, the field structure in this record varies depending on
 * whether it is HEADER_U or HEADER_D. The first 132 bytes constitute
 * same fields in both cases. The fields after 132 bytes differ in
 * structure between HEADER_U and HEADER_D.
 *
 * <p><b>Usage:</b> HEADER_U record definition should be used when
 * information is sent to the mainframe. When reading the mainframe
 * response to a query or an update HEADER_D should be used. 
 * 
 * <p> When a response is returned from the mainframe, the last 
 * 128 bytes are used by the COBOL programs to specify any error 
 * messages. The CICS intermediery program also writes any errors 
 * in this area. Hence, it is possible for the CICS program to
 * overwrite the errors returned from the COBOL program.
 *
 * <p> The HDUNUSED_OFFSET field is set to "Y" if there is an error
 * message from the CICS program.
 *
 * @version	Defect_POS_A	05/27/2008
 * @author	Richard Hicks
 * <p>Creation Date:		08/30/2001 11:13:22
 */

public class APPCHeader
{
	/**
	 * The length of the APPCHeader is 256
	 */
	final static private int HEADER_LENGTH = 256;

	/**
	 * The following fields are common for HEADER_U and HEADER_D.
	 * HEADER_U - Header sent to the mainframe from POS
	 * HEADER_D - Header obtained by POS from mainframe
	 */

	/**
	 * Transaction Id Offset - 0
	 */
	final static private int HDTRAN_OFFSET = 0;
	/**
	 * Transaction Id Length - 4
	 */
	final static private int HDTRAN_LENGTH = 4;
	/**
	 * SYSIDA Offset - 4
	 */
	final static private int HDSYSIDA_OFFSET = 4;
	/**
	 * SYSIDA Length - 4
	 */
	final static private int HDSYSIDA_LENGTH = 4;
	/**
	 * SYSIDB Offset - 8
	 */
	final static private int HDSYSIDB_OFFSET = 8;
	/**
	 * SYSIDB Length - 4
	 */
	final static private int HDSYSIDB_LENGTH = 4;
	/**
	 * TS Queue A Offset - 12
	 */
	final static private int HDTSQA_OFFSET = 12;
	/**
	 * TS Queue A Length - 8
	 */
	final static private int HDTSQA_LENGTH = 8;
	/**
	 * TS Queue B Offset - 20
	 */
	final static private int HDTSQB_OFFSET = 20;
	/**
	 * TS Queue B Length - 8
	 */
	final static private int HDTSQB_LENGTH = 8;
	/**
	 * Request Id Offset - 28
	 * <br>Actually is two pieces of 4 bytes each.
	 */
	final static private int HDREQID_OFFSET = 28;
	/**
	 * Request Id Length - 8
	 */
	final static private int HDREQID_LENGTH = 8;
	/**
	 * Signon Key Offset - 36
	 */
	final static private int HDSKEY_OFFSET = 36;
	/**
	 * Signon Key Length - 8
	 */
	final static private int HDSKEY_LENGTH = 8;
	/**
	 * Password Offset - 44
	 */
	final static private int HDPWD_OFFSET = 44;
	/**
	 * Password Length - 8
	 */
	final static private int HDPWD_LENGTH = 8;
	/**
	 * Post Interval Offset - 52
	 */
	final static private int HDPIVAL_OFFSET = 52;
	/**
	 * Post Interval Length - 6
	 */
	final static private int HDPIVAL_LENGTH = 6;
	/**
	 * Override Interval Flag Offset - 58
	 */
	final static private int HDOPIVAL_OFFSET = 58;
	/**
	 * Override Interval Flag Length - 1
	 */
	final static private int HDOPIVAL_LENGTH = 1;
	/**
	 * Test Record Count Offset - 59
	 */
	final static private int HDTEST_OFFSET = 59;
	/**
	 * Test Record Count Length - 1
	 */
	final static private int HDTEST_LENGTH = 1;
	/**
	 * Return Record Count Offset - 60
	 */
	final static private int HDRECCT_OFFSET = 60;
	/**
	 * Return Record Count Length - 2
	 */
	final static private int HDRECCT_LENGTH = 2;
	/**
	 * Error Flag Offset - 62
	 */
	final static private int HDERR_OFFSET = 62;
	/**
	 * Error Flag Length - 1
	 */
	final static private int HDERR_LENGTH = 1;
	/**
	 * Returned Data Flag Offset - 63
	 */
	final static private int HDDATA_OFFSET = 63;
	/**
	 * Returned Data Flag Length - 1
	 */
	final static private int HDDATA_LENGTH = 1;
	/**
	 *  Do Not Delete the TSQ Offset - 64
	 */
	final static private int HDDDTSQ_OFFSET = 64;
	/**
	 * Do Not Delete the TSQ Length - 1
	 */
	final static private int HDDDTSQ_LENGTH = 1;
	/**
	 * TSQ B Created Offset - 65
	 */
	final static private int HDTSQHVB_OFFSET = 65;
	/**
	 * TSQ B Created Length - 1
	 */
	final static private int HDTSQHVB_LENGTH = 1;
	/**
	 * TSQ A Created Offset - 66
	 */
	final static private int HDTSQHVA_OFFSET = 66;
	/**
	 * TSQ A Created Length - 1
	 */
	final static private int HDTSQHVA_LENGTH = 1;
	/**
	 * Program Completed Offset - 67
	 */
	final static private int HDCOMPL_OFFSET = 67;
	/**
	 * Program Completed Length - 1
	 */
	final static private int HDCOMPL_LENGTH = 1;
	/**
	 * Java Gateway Error Offset - 68
	 */
	// java gate error set here (cics pgm err msg)
	final static private int HDJGERR_OFFSET = 68;
	/**
	 * Java Gateway Error Length - 1
	 */
	final static private int HDJGERR_LENGTH = 1;
	/**
	 * Field 8 Offset - 69
	 */
	final static private int HDF8_OFFSET = 69;
	/**
	 * Field 8 Length - 1
	 */
	final static private int HDF8_LENGTH = 1;
	/**
	 * NetName Offset - 70
	 */
	final static private int HDNETNAM_OFFSET = 70;
	/**
	 * NetName Length - 8
	 */
	final static private int HDNETNAM_LENGTH = 8;
	/**
	 * Date Offset - 78
	 */
	final static private int HDDATE_OFFSET = 78;
	/**
	 * Date Length - 8
	 */
	final static private int HDDATE_LENGTH = 8;
	/**
	 * Time 1 Offset - 86
	 */
	final static private int HDTIME1_OFFSET = 86;
	/**
	 * Time1 Length - 8
	 */
	final static private int HDTIME1_LENGTH = 8;
	/**
	 * Time 2 Offset - 94
	 */
	final static private int HDTIME2_OFFSET = 94;
	/**
	 * Time 2 Length - 8
	 */
	final static private int HDTIME2_LENGTH = 8;
	/**
	 * Offset - 102
	 */
	final static private int HDMSEC_OFFSET = 102;
	/**
	 * Length - 8
	 */
	final static private int HDMSEC_LENGTH = 8;
	/**
	 * Input Length Offset - 110
	 */
	final static private int HDINTLEN_OFFSET = 110;
	/**
	 * Input Length Length - 5
	 */
	// the length of the input written to commarea(to cics)
	final static private int HDINTLEN_LENGTH = 5;
	/**
	 * Output Length Offset - 115
	 */
	final static private int HDOUTLEN_OFFSET = 115;
	/**
	 * Output Length Length - 5
	 */
	// the length of the output to read from commarea (from cics)    
	final static private int HDOUTLEN_LENGTH = 5;
	/**
	 * Unused Area Offset - 120
	 */
	final static private int HDUNUSED_OFFSET = 120;
	/**
	 * Unused Area Length - 8
	 */
	final static private int HDUNUSED_LENGTH = 8;
	/**
	 * Mainframe Database Offset - 128
	 */
	final static private int HDDB_OFFSET = 128;
	/**
	 * Mainframe Database Length - 4
	 */
	final static private int HDDB_LENGTH = 4;

	/**
	 * HDDESCAREA_OFFSET is defined only for HEADER_U
	 */
	final static private int HDDESCAREA_OFFSET = 132;
	/**
	 * HDDESCAREA_LENGTH is defined only for HEADER_U
	 */
	final static private int HDDESCAREA_LENGTH = 124;

//	/**
//	 * HDERRTYPE_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDERRTYPE_OFFSET = 132;
//	/**
//	 * HDERRTYPE_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDERRTYPE_LENGTH = 2;
//
//	/**
//	 * HDRETCD_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDRETCD_OFFSET = 134;
//	/**
//	 * HDRETCD_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDRETCD_LENGTH = 2;
//
//	/**
//	 * HDAMMSGNO_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDAMMSGNO_OFFSET = 136;
//	/**
//	 * HDAMMSGNO_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDAMMSGNO_LENGTH = 9;
//
//	/**
//	 * HDNOOFRECS_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDNOOFRECS_OFFSET = 145;
//	/**
//	 * HDNOOFRECS_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDNOOFRECS_LENGTH = 3;
//
//	/**
//	 * HDERRMSGAREA_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDERRMSGAREA_OFFSET = 148;
//	/**
//	 * HDERRMSGAREA_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDERRMSGAREA_LENGTH = 89;
//
//	/**
//	 * HDFILLER2_OFFSET is defined only for HEADER_D
//	 */
//	final static private int HDFILLER2_OFFSET = 237;
//	/**
//	 * HDFILLER2_LENGTH is defined only for HEADER_D
//	 */
//	final static private int HDFILLER2_LENGTH = 19;

	/**
	 * This area actually holds the Header Area
	 */
	private byte[] cbyHeaderRecord = new byte[HEADER_LENGTH];

	/**
	 * APPCHeader constructor
	 */
	public APPCHeader()
	{
		super();
	}
	/**
	 * Returns a empty APPC header record to the calling method. 
	 * 
	 * @return byte
	 */
	public byte[] getAPPCHeaderRecord()
	{
		return cbyHeaderRecord;
	}
	/**
	 * Returns the Input Offset.
	 * 
	 * @return int
	 */
	public int getHDINOFFSET()
	{
		return HDINTLEN_OFFSET;
	}
	/**
	 * Returns the Input Length.
	 * 
	 * @return int
	 */
	public int getHDINTLEN()
	{
		return HDINTLEN_LENGTH;
	}
	/**
	 * Returns the Output Length.
	 * 
	 * @return int
	 */
	public int getHDOUTLEN()
	{
		return HDOUTLEN_LENGTH;
	}
	/**
	 * Return the Output Offset.
	 * 
	 * @return int
	 */
	public int getHDOUTOFFSET()
	{
		return HDOUTLEN_OFFSET;
	}
	/**
	 * Sets a value in the APPC header record. This method receives the
	 * value, its offset and its length as parameters and uses these to
	 * set this value in the APPC header.
	 * 
	 * @param String asValue
	 * @param int aiOffset
	 * @param int aiLength 
	 * 
	 */
	private void setAPPCHeaderValue(
		String asValue,
		int aiOffset,
		int aiLength)
	{

		StringBuffer laBuffer = new StringBuffer();
		//add the value to the end of buffer
		laBuffer.append(asValue);
		//fill spaces at the end for the same length as the value 
		for (int i = asValue.length(); i < aiLength; i++)
			laBuffer.append(" ");

		//put this value in a string, make all letters upper case and convert
		//to bytes and store in temp. 	
		byte[] lbyTemp = laBuffer.toString().toUpperCase().getBytes();

		//add these bytes to the header record starting from the offset
		int liIndex = aiOffset;
		for (int i = 0; i < aiLength; i++)
		{
			cbyHeaderRecord[liIndex] = lbyTemp[i];
			++liIndex;
		}
	}
	/**
	 * This is the signiture of the old setMainFrameHeader.
	 * It substitutes a zero in the place of the delete
	 * TSQ field.
	 * 
	 * @param String asHdtran
	 * @param String asHdtsqa
	 * @param String asHdskey
	 * @param String asHdpwd
	 * @param String asHdpival
	 * @param String asHdopival
	 * @param String asHdtest
	 * @param String asHdrecct
	 * @param String asHdnetnam
	 * @param String asHdintlen
	 * @param String asHddatabase
	 * @param String asHdbuffdescarea
	 */
	public void setMainframeHeader(
		String asHdtran,
		String asHdtsqa,
		String asHdskey,
		String asHdpwd,
		String asHdpival,
		String asHdopival,
		String asHdtest,
		String asHdrecct,
		String asHdnetnam,
		String asHdintlen,
		String asHddatabase,
		String asHdbuffdescarea)
	{
		// Set " " to "Y" for TST testing. 
		//	"Y" tells MF to save data string. jr
		setMainframeHeader(
			asHdtran,
			asHdtsqa,
			asHdskey,
			asHdpwd,
			asHdpival,
			asHdopival,
			asHdtest,
			asHdrecct,
			" ",
			asHdnetnam,
			asHdintlen,
			asHddatabase,
			asHdbuffdescarea);
	}
	/**
	 * Sets the header record with all the required fields. Converts the call
	 * to set header record with minimal, required fields to a request with
	 * all required fields and calls <code>setValuesInAPPCHeader</code>.
	 * 
	 * @param String asHdtran
	 * @param String asHdtsqa
	 * @param String asHdskey
	 * @param String asHdpwd
	 * @param String asHdpival
	 * @param String asHdopival
	 * @param String asHdtest
	 * @param String asHdrecct
	 * @param String asHdddtsq
	 * @param String asHdnetnam
	 * @param String asHdintlen
	 * @param String asHddatabase
	 * @param String asHdbuffdescarea
	 */
	public void setMainframeHeader(
		String asHdtran,
		String asHdtsqa,
		String asHdskey,
		String asHdpwd,
		String asHdpival,
		String asHdopival,
		String asHdtest,
		String asHdrecct,
		String asHdddtsq,
		String asHdnetnam,
		String asHdintlen,
		String asHddatabase,
		String asHdbuffdescarea)
	{
		// make a byte array to fill the HDUNUSED area in the APPC header
		byte[] lbyUnused = new byte[HDUNUSED_LENGTH];

		//initialize unused area to be empty
		for (int i = 0; i < lbyUnused.length; i++)
		{
			lbyUnused[i] = 0;
		}

		String lsUnusedArea = new String(lbyUnused);

		//Make a call to <code> setValuesInAPPCHeader</code> with all values
		setValuesInAPPCHeader(
			asHdtran,
			"    ",
			"    ",
			asHdtsqa,
			"        ",
			"        ",
			asHdskey,
			asHdpwd,
			asHdpival,
			asHdopival,
			asHdtest,
			asHdrecct,
			" ",
			" ",
			asHdddtsq,
			" ",
			" ",
			" ",
			" ",
			" ",
			asHdnetnam,
			"        ",
			"        ",
			"        ",
			"        ",
			asHdintlen,
			lsUnusedArea,
			asHddatabase,
			asHdbuffdescarea);
	}
	/**
	 * This method sets all the values in the APPC Header record. 
	 * 
	 * @param String asHdtran
	 * @param String asHdsysida
	 * @param String asHdsysidb
	 * @param String asHdtsqa
	 * @param String asHdtsqb
	 * @param String asHdreqid
	 * @param String asHdskey
	 * @param String asHdpwd
	 * @param String asHdpival
	 * @param String asHdopival
	 * @param String asHdtest
	 * @param String asHdrecct
	 * @param String asHderr
	 * @param String asHddata
	 * @param String asHdddtsq
	 * @param String asHdtsqhvb
	 * @param String asHdtsqhva
	 * @param String asHdcompl
	 * @param String asHdjgerr
	 * @param String asHdf8
	 * @param String asHdnetnam
	 * @param String asHddate
	 * @param String asHdtime1
	 * @param String asHdtime2
	 * @param String asHdmsec
	 * @param String asHdintlen
	 * @param String asHdunused
	 * @param String asHddatabase
	 * @param String asHdbuffdescarea
	 * 
	 */
	private void setValuesInAPPCHeader(
		String asHdtran,
		String asHdsysida,
		String asHdsysidb,
		String asHdtsqa,
		String asHdtsqb,
		String asHdreqid,
		String asHdskey,
		String asHdpwd,
		String asHdpival,
		String asHdopival,
		String asHdtest,
		String asHdrecct,
		String asHderr,
		String asHddata,
		String asHdddtsq,
		String asHdtsqhvb,
		String asHdtsqhva,
		String asHdcompl,
		String asHdjgerr,
		String asHdf8,
		String asHdnetnam,
		String asHddate,
		String asHdtime1,
		String asHdtime2,
		String asHdmsec,
		String asHdintlen,
		String asHdunused,
		String asHddatabase,
		String asHdbuffdescarea)
	{
		setAPPCHeaderValue(asHdtran, HDTRAN_OFFSET, HDTRAN_LENGTH);

		setAPPCHeaderValue(
			asHdsysida,
			HDSYSIDA_OFFSET,
			HDSYSIDA_LENGTH);

		setAPPCHeaderValue(
			asHdsysidb,
			HDSYSIDB_OFFSET,
			HDSYSIDB_LENGTH);

		setAPPCHeaderValue(asHdtsqa, HDTSQA_OFFSET, HDTSQA_LENGTH);

		setAPPCHeaderValue(asHdtsqb, HDTSQB_OFFSET, HDTSQB_LENGTH);

		setAPPCHeaderValue(asHdreqid, HDREQID_OFFSET, HDREQID_LENGTH);

		setAPPCHeaderValue(asHdskey, HDSKEY_OFFSET, HDSKEY_LENGTH);

		setAPPCHeaderValue(asHdpwd, HDPWD_OFFSET, HDPWD_LENGTH);

		setAPPCHeaderValue(asHdpival, HDPIVAL_OFFSET, HDPIVAL_LENGTH);

		setAPPCHeaderValue(
			asHdopival,
			HDOPIVAL_OFFSET,
			HDOPIVAL_LENGTH);

		setAPPCHeaderValue(asHdtest, HDTEST_OFFSET, HDTEST_LENGTH);

		setAPPCHeaderValue(asHdrecct, HDRECCT_OFFSET, HDRECCT_LENGTH);

		setAPPCHeaderValue(asHderr, HDERR_OFFSET, HDERR_LENGTH);

		setAPPCHeaderValue(asHddata, HDDATA_OFFSET, HDDATA_LENGTH);

		setAPPCHeaderValue(asHdddtsq, HDDDTSQ_OFFSET, HDDDTSQ_LENGTH);

		setAPPCHeaderValue(
			asHdtsqhvb,
			HDTSQHVB_OFFSET,
			HDTSQHVB_LENGTH);

		setAPPCHeaderValue(
			asHdtsqhva,
			HDTSQHVA_OFFSET,
			HDTSQHVA_LENGTH);

		setAPPCHeaderValue(asHdcompl, HDCOMPL_OFFSET, HDCOMPL_LENGTH);

		setAPPCHeaderValue(asHdjgerr, HDJGERR_OFFSET, HDJGERR_LENGTH);

		setAPPCHeaderValue(asHdf8, HDF8_OFFSET, HDF8_LENGTH);

		setAPPCHeaderValue(
			asHdnetnam,
			HDNETNAM_OFFSET,
			HDNETNAM_LENGTH);

		setAPPCHeaderValue(asHddate, HDDATE_OFFSET, HDDATE_LENGTH);

		setAPPCHeaderValue(asHdtime1, HDTIME1_OFFSET, HDTIME1_LENGTH);

		setAPPCHeaderValue(asHdtime2, HDTIME2_OFFSET, HDTIME2_LENGTH);

		setAPPCHeaderValue(asHdmsec, HDMSEC_OFFSET, HDMSEC_LENGTH);

		setAPPCHeaderValue(
			asHdintlen,
			HDINTLEN_OFFSET,
			HDINTLEN_LENGTH);

		setAPPCHeaderValue(
			asHdunused,
			HDUNUSED_OFFSET,
			HDUNUSED_LENGTH);

		setAPPCHeaderValue(asHddatabase, HDDB_OFFSET, HDDB_LENGTH);

		setAPPCHeaderValue(
			asHdbuffdescarea,
			HDDESCAREA_OFFSET,
			HDDESCAREA_LENGTH);
	}
	/**
	 * Return output length from MfResponse
	 * 
	 * @param asMfResponse	String
	 * @return int
	 */
	public int rtnMfOutputLength(String asMfResponse)
	{
		//get the length of output from MF
		final int liOutputLengthOffset = getHDOUTOFFSET();
		final int liOutputLengthLength = getHDOUTLEN();
		int liOutputLength =
			Integer.parseInt(
				asMfResponse.substring(
					liOutputLengthOffset,
					liOutputLengthOffset + liOutputLengthLength));
		
		return liOutputLength;
	}
}
