package com.txdot.isd.rts.services.reports;

import java.util.Hashtable;

import com.txdot.isd.rts.services.data.PrintDocumentData;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * PrintDocManager.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/27/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Min Wang		03/20/2003	Modified PrintDocManager.CQU100005781
 * Jeff S.		02/23/2004	Added TTLP and VOID
 *							TTLP - Just a barcode file
 *							VOID - An original and Dup
 *							add new hash values
 *							defect 6848, 6898 Ver 5.1.6
 * J Rue		08/20/2004	Add "DLRSTKR" for DTA- Print Sticker and
 * 							County receipts.
 *							defect 7430 Ver 5.2.1
 * S Johnston	05/09/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException()
 *							defect 7896 Ver 5.2.3
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							modify class variables
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	06/12/2007	Add Special Plate Trans Cds 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/07/2008	Remove for SLVG, add for CORNRT,CORSLV, 
 * 							CCONRT, CCOSLV
 * 							defect 9636 Ver 3 Amigos PH B
 * K Harrell	05/07/2008	Modify for COA as uses NRT document
 * 							defect 9642 Ver 3 Amigos PH B
 * K Harrell	10/21/2008	Add entries to hash table for new Disabld 
 * 							 Placard transactions, delete old
 * 							modify laDocProp 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	07/29/2009	Replace hash table entries with new Disabled
 * 							 Placard transcds.
 * 							add shDocProp  (refactored from laDocProp) 
 * 							delete laDocProp
 * 							defect 10133 Ver Defect_POS_F  
 * K Harrell	06/27/2010	add entry for PRMDUP; delete entry for PT24
 * 							modify laDocProp
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/02/2010	Implement "Barcode" logic to handle 
 * 							Printed Permit
 * 							modify shDocProp
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	12/20/2010  Implement "Permit" logic for Special Plates 
 * 							Permit.  Modify Timed Permit entries to use
 * 							new permit logic/  
 * 							add DPSPPT (entry) 
 * 							modify shDocProp entries  
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	05/29/2011	Add MODPT entry 
 * 							modify shDocProp 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	11/14/2011	temp change for VTR275 
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/12/2011	remove temp change for VTR275
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	03/20/2012	add RENPDC,REIPDC,REITDC 
 * 							modify shDocProp
 * 							defect 11321 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * PrintDocManager
 *
 * @version	6.10.0 		 	03/20/2012
 * @author	Rakesh Duggirala
 * <br>Creation Date:		02/27/2002 15:27:19
 */
public class PrintDocManager
{
	private static Hashtable shDocProp = null;

	static {
		shDocProp = new Hashtable();

		// defect 10700 

		//Cash Register Receipt   
		shDocProp.put(
			"CSHREG",
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// defect 6848, 6898
		// Added for Void report
		shDocProp.put(
			TransCdConstant.VOID,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Added for Title Package report
		shDocProp.put(
			TransCdConstant.TTLP,
			new PrintDocumentData(
				false,
				false,
				false,
				true,
				false,
				false,
				false));
		// end defect 6848, 6898
		//Title Transaction Codes
		shDocProp.put(
			TransCdConstant.TITLE,
			new PrintDocumentData(
				true,
				true,
				true,
				true,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.CORTTL,
			new PrintDocumentData(
				true,
				true,
				true,
				true,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.NONTTL,
			new PrintDocumentData(
				true,
				true,
				true,
				false,
				false,
				false,
				true));
		shDocProp.put(
			TransCdConstant.REJCOR,
			new PrintDocumentData(
				true,
				true,
				true,
				true,
				false,
				false,
				true));
		//Dealer Completed Report   
		shDocProp.put(
			"DLRCOMPL",
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		//Dealer Preliminary Report   
		shDocProp.put(
			"DLRPRELM",
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// defect 7430
		//  Temp TransCd to print Sticker and County receipts -DTA
		shDocProp.put(
			"DLRSTKR",
			new PrintDocumentData(
				true,
				true,
				false,
				true,
				false,
				false,
				false));
		// end defect 7430
		shDocProp.put(
			TransCdConstant.DTAORD,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DTAORK,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DTANTD,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DTANTK,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.CCO,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.COA,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		// defect 9636
		// No longer create Salvage Certificates; Update for Corrected 
		// and CCO 
		//laDocProp.put(TransCdConstant.SLVG, new PrintDocumentData(
		//		true, false, false, false, false, false));
		shDocProp.put(
			TransCdConstant.CORNRT,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.CORSLV,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.CCOSCT,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.CCONRT,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));
		// end defect 9636

		shDocProp.put(
			TransCdConstant.SCOT,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.NRCOT,
			new PrintDocumentData(
				true,
				false,
				false,
				true,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.ADLSTX,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		shDocProp.put(
			"DPSPPT",
			new PrintDocumentData(
				false,
				false,
				false,
				false,
				false,
				false,
				true));
		// defect 9085
		// Special Plates Application - Manufacture 		
		shDocProp.put(
			TransCdConstant.SPAPPL,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Ownership Change
		shDocProp.put(
			TransCdConstant.SPAPPO,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Reserved
		shDocProp.put(
			TransCdConstant.SPAPPR,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Issue Inventory
		shDocProp.put(
			TransCdConstant.SPAPPI,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Customer Supplied
		shDocProp.put(
			TransCdConstant.SPAPPC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Request Renewal Notice
		shDocProp.put(
			TransCdConstant.SPRNR,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Revise
		shDocProp.put(
			TransCdConstant.SPREV,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// Special Plates Application - Renew 						
		shDocProp.put(
			TransCdConstant.SPRNW,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		// end defect 9085 

		// Registration Transaction Codes
		shDocProp.put(
			TransCdConstant.RENEW,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));
		shDocProp.put(
			TransCdConstant.DUPL,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.EXCH,
			new PrintDocumentData(
				true,
				true,
				false,
				true,
				false,
				false,
				true));
		shDocProp.put(
			TransCdConstant.REPL,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));
		shDocProp.put(
			TransCdConstant.PAWT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.IRENEW,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.CORREG,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DRVED,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		//defect 5781
		//Misc
		shDocProp.put(
			TransCdConstant.VOID,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		//end defect 5781
		//Accounting
		shDocProp.put(
			TransCdConstant.REFUND,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.HOTCK,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.HOTDED,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.CKREDM,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.HCKITM,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.RFCASH,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.ADLCOL,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.RGNCOL,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		//Vehicle Inquiry
		shDocProp.put(
			TransCdConstant.VEHINQ,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		//Miscellaneous Reg Codes
		// Use "Permit" logic to handle Printed Permit 
		// abOriginal,
		// abFirstDuplicate,
		// abSecondDuplicate,
		// abBarCode,
		// abPreliminary,
		// abFinal
		// abPermit 

		// defect 10844 
		shDocProp.put(
			TransCdConstant.MODPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));
		// end defect 10844 
		
		shDocProp.put(
			TransCdConstant.PT72,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.PT144,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.PRMDUP,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.OTPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.FDPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));

		shDocProp.put(
			TransCdConstant.PT30,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				true));
		// end defect 10491 

		shDocProp.put(
			TransCdConstant.TOWP,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.TAWPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		// defect 10133
		// New TransCd  (TDC, PDC, DELTDC, DELPDC, RPLTDC, RPLPDC) 
		shDocProp.put(
			TransCdConstant.PDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.TDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DELPDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.DELTDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.RPLPDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		shDocProp.put(
			TransCdConstant.RPLTDC,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));
		
		// defect 11321 
		shDocProp.put(
				TransCdConstant.RENPDC,
				new PrintDocumentData(
					true,
					true,
					false,
					false,
					false,
					false,
					false));
		
		shDocProp.put(
				TransCdConstant.REIPDC,
				new PrintDocumentData(
					true,
					true,
					false,
					false,
					false,
					false,
					false));
		
		shDocProp.put(
				TransCdConstant.REITDC,
				new PrintDocumentData(
					true,
					true,
					false,
					false,
					false,
					false,
					false));
		// end defect 11321 
		
		shDocProp.put(
			TransCdConstant.NRIPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		shDocProp.put(
			TransCdConstant.NROPT,
			new PrintDocumentData(
				true,
				true,
				false,
				false,
				false,
				false,
				false));

		// end defect 10700 
	}

	/**
	 * PrintDocManager constructor
	 */
	public PrintDocManager()
	{
		super();
	}

	/**
	 * getPrintProps
	 *
	 * @param asTranscd String
	 * @return Object
	 */
	public static Object getPrintProps(String asTranscd)
	{
		if (shDocProp.containsKey(asTranscd))
		{
			return shDocProp.get(asTranscd);
		}
		else
		{
			return null;
		}
	}

	/**
	 * main - used to start class as an application
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		System.out.println("table length is " + shDocProp.size());
		getPrintProps(TransCdConstant.VOID);
	}
}
