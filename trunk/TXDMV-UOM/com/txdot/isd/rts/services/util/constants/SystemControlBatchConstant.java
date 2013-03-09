package com.txdot.isd.rts.services.util.constants;

/*
 *
 * SystemControlBatchConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/25/2004	Added GET_WS_IDS to lookup WS IDS when batch
 *							is trying to redirect printout to another
 *							substation.
 *							add GET_WS_IDS
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		03/05/2004	Added function ID UPDATE_WORKSTATION_STATUS
 *							to let through to the server when DB is down.
 *							This function is for updating the 
 *							RTS_WS_Status with the current status of the
 *							workstion if it is different than what the 
 *							DB shows.
 *							add UPDATE_WORKSTATION_STATUS
 *							defect 6918 Ver 5.1.6
 * Ray Rowehl	05/17/2004	Added constant to document testing interface
 *							add TEST_SERVER_BATCH
 *							defect 6785
 * S Johnston	07/08/2005  Created constants for user visible text
 * 							defect 7897 Ver 5.2.3
 * K Harrell	10/14/2005	deleted MAX_LOG as it is no longer used
 * 							Used in RTS I; Removed after implementation
 * 							in RTS II as LogFuncTrans data is not cached 
 * 							defect 7897 Ver 5.2.3 
 * Ray Rowehl	09/11/2006	Add constant to handle reporting of server 
 * 							side batch results.
 * 							add UPDATE_BATCH_RESULTS_SERVER
 * 							defect 8923 Ver 5.2.5
 * K Harrell	05/09/2007	Add constant to handle request for Special
 * 							Plates Report 
 * 							add GEN_SPCL_PLTS 
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/01/2008	Remove constants for ShowCache.  Now locally
 * 							derived. 
 * 							delete COLUMN_0_LOG_TIME, COLUMN_1_LOG_DATE,
 * 							  COLUMN_2_SQL, COLUMN_3_AM_DATE
 * 							defect 9831 Ver Defect_POS_B
 * K HARRELL	01/21/2009	Add constants for DP Purge 
 * 							add DP_PURGE_EMPID, DP_PURGE_OFCISSUANCENO, 
 * 								DP_PURGE_WSID
 * 							defect 9889 Ver Defect_POS_D
 * K Harrell	02/06/2009	Remove constant for Special Plate Report. 
 * 							Never implemented. 
 * 							delete GEN_SPCL_PLTS 
 * 							defect 9941 Ver Defect_POS_D
 * K Harrell	02/09/2009	Add constant for Internet Deposit 
 * 							 Reconciliation Report
 * 							add GEN_INET_DEPOSIT_RECON
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	08/17/2009	add GET_SUBSTA_INFO
 * 							delete GET_SUBSA_INFO 
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Constants used for System Control Batch.
 *
 * @version	Defect_POS_F	08/17/2009  
 * @author	Michael Abernethy
 * <br>Creation Date:		11/05/2001 16:22:53
 */

public class SystemControlBatchConstant
{
	public static final int MAX_TRANS = 1;

	// defect 9935 
	public static final int GEN_INET_DEPOSIT_RECON = 2;
	// end defect 9935 

	public static final int GET_BATCH_PROCESS = 3;
	public static final int UPDATE_BATCH_RESULTS = 4;
	public static final int GEN_BATCH_REPORT = 5;
	public static final int GEN_COMPLETE_SETASIDE = 6;
	public static final int GEN_COUNTY_WIDE = 7;
	public static final int GEN_SUBSTATION = 8;
	public static final int GEN_TITLE_PACKAGE = 9;
	public static final int GET_START_TIME = 10;
	public static final int GET_TITLE_NUM = 11;
	
	// defect 8628 
	public static final int GET_SUBSTA_INFO = 12;
	// end defect 8628  

	// Added for redirecting batch
	// get Workstation Id List
	public static final int GET_WS_IDS = 13;

	// Added for updating ws status at startup
	public static final int UPDATE_WORKSTATION_STATUS = 14;

	// defect 8923
	public static final int UPDATE_BATCH_RESULTS_SERVER = 16;
	// end defect 8923

	// Added to document testing call from sendtrans
	/**
	 * TEST_SERVER_BATCH for allowing tester to start
	 * SendTrans in development environment.
	 * 
	 * <br>AbstractValue is 22
	 */
	public static final int TEST_SERVER_BATCH = 22;
	// end defect 6785

	// FrmDetailCache constants
	public static final String TITLE_FRM_DETAIL_CACHE = "Detail Cache";
	public static final String TXT_CLASS_TYPE = "Class Type";
	public static final String TXT_ATTRIBUTES = "Attributes";

	// FrmShowCache constants
	public static final String TITLE_FRM_SHOW_CACHE = "Show Cache";

	// Zip Cache constants
	public static final String OUTPUT_FILE = "cache.zip";
	public static final String ERROR_MESSAGE =
		"Error creating .zip file.  No cache.zip file was created";

	// Cross frame constants
	public static final String TXT_CANCEL = "Cancel";

	// defect 9889 
	public static final String DP_PURGE_EMPID = "PURGE";
	public static final int DP_PURGE_OFCISSUANCENO = 999;
	public static final int DP_PURGE_WSID = 999;
	// end defect 9889  

}
