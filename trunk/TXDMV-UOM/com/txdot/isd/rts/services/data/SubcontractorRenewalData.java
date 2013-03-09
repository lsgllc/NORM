package com.txdot.isd.rts.services.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * SubcontractorRenewalData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa	05/09/2002	Fixed CQU100003828 by adding compareTo(Obj)
 * 	and Jeff				to sort transactions by TransID 
 * K Harrell	01/22/2004	5.2.0 Merge. Complete class replacement.
 *							Ver 5.2.0
 * K Harrell	07/11/2004	Modify for read of export diskette w/ 15
 *							items.
 *							modify setField()
 *							defect 7287  Ver 5.2.1
 * K Harrell	07/31/2004	Modify to show seconds on Export Dates
 *							modify setField()
 *							defect 7412  Ver 5.2.1
 * K Harrell	08/16/2004	Read Process Time
 *							add cdSubconProcessDateTime and associated
 *							get/set methods
 *							modify setField()
 *							defect 7413  Ver 5.2.1
 * K Harrell	10/10/2004	add cbPosted,isPosted(),setPosted()
 *							static variables PROCESS,VOID,REPRINT
 *							add'l variable name cleanup, e.g. Vin=>VIN
 *							defect 7586  Ver 5.2.1
 * K Harrell	10/29/2004	Allow no VIN processing from RSPS Diskette
 *							modify setField()
 *							defect 7674  Ver 5.2.1 Fix 1
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							add  constants for field positions 
 * 							deleted TODAY
 * 							deprecated calculateTotalFees()
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/17/2005	Mark record invalid if length of DocNo is 
 * 							not equal to 17 or length VIN > 22.
 * 							Trim asValue upon entry into setField()
 * 							rename csProcsId to csRSPSId  
 * 							modify setField(),populateItmPrice()  
 * 							defect 7983 Ver 5.2.3
 * K Harrell	02/09/2006	Restored use of Float.parseFloat(asValue) vs.
 * 							isNumeric() for itmPrice
 *							modify populateItmPrice()
 *							defect 7893 Ver 5.2.3  
 * Jeff S.		08/08/2006	Add the ability to accept XML data for the
 * 							subcon data.  Add implements 
 * 							XMLDataInterface.
 * 							add cvXMLTags, setFields()
 * 							defect 8451 Ver 5.2.4
 * K Harrell	02/08/2007	add ciSpclPltIndi, csSpclPltRegPltCd,csOrgNo,
 * 							 ciAddlSetIndi, csNewPltsReqdCd
 * 							 plus get/set methods
 * 							delete deprecated calculateTotalFees()
 *							defect 9085 Ver Special Plates
 * K Harrell	10/16/2007	ORGNO,ADDLSETINDI,NEWPLTSREQDCD,
 * 								BARCODEVERSION	
 * 							add setupAsSpclPlt()
 * 							modify chtXMLTags  
 * 							modify setFields(), setField() 
 * 							defect 9362 Ver Special Plates   
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9631 Ver Defect POS A
 * J Rue		05/14/2008	Add setRecType() and setField()
 * 							to corresponding to the field number in 
 * 							parse interface
 * 							add setField(), setRecType()
 * 							defect 9656 Ver Defect_POS_A
 * B Hargrove	05/22/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							References to diskValidations() became 
 * 							mediaValidations(). 
 * 							defect 10075 Ver Defect_POS_F  
 * T Pederson	01/04/2010 	Remove check for barcode version 05.									
 *							modify setupAsSpclPlt()
 *							defect 10303  Ver Defect_POS_H
 * T Pederson	04/08/2010 	Handle new vendor plates data added to 
 * 							subcontractor export file.
 * 							add REGNXTEXPMO, REGNXTEXPYR, PLTVLDTYTERM,
 * 							PLTEXPMO, PLTEXPYR, PLTNXTEXPMO,
 * 							PLTNXTEXPYR
 * 							add get/set RegNextExpMo, RegNextExpYr,
 * 							PltVldtyTerm, PltExpMo, PltExpYr, 
 * 							PltNextExpMo, PltNextExpYr							
 * 							modify chtXMLTags, setField() 
 *							defect 10392  Ver POS_640
 * K Harrell	01/24/2011	add SubcontractorRenewalData(
 *							 WebAgencyTransactionData, Vector)
 *							add csVehMk, csVehModl, ciVehModlYr, 
 *							 ciPltBirthDate, get/set methods.
 *							add getPltSoldMos()  
 *							defect 10734 Ver 6.7.0  
 * K Harrell	02/02/2011 	add ciVehGrossWt, get/set methods
 * 						    defect 10734 Ver 6.7.0 
 * K Harrell	03/25/2011	handle empty BarCdVersionNo from WATransData
 * 							modify SubcontractorRenewalData(
 *							 WebAgencyTransactionData, Vector)
 *							defect 10768 Ver 6.7.1 
 * K Harrell	04/22/2011	add cbMfgSpclPlt, is/set methods
 *							defect 10768 Ver 6.7.1 
 * K Harrell	04/26/2011	modify SubcontractorRenewalData(
 *							aaWATData, avWATFeeData)
 *							defect 10768 Ver 6.7.1
 * K McKee      01/27/2012  CustActulRegFee column is not large enough to handle
 *			                optional fees
 * 							modify populateItmPrice(asValue, abFirstRecord)
 * 							add isEligibleForRegFee(asAcctItmCd)
 *							defect 11240 Ver 6.10.0
 * ---------------------------------------------------------------------
 */
/**
 * This data object stores the information related to the Subcontractor 
 * Renewal item.
 * 
 * @version	6.10.0 				01/27/2012
 * @author	Nancy Ting
 * <br>Creation Date:			10/03/2001
 */

public class SubcontractorRenewalData
	implements
		java.io.Serializable,
		Displayable,
		Comparable,
		Parseable,
		XMLDataInterface
{

	//	Object
	private Dollar caCustBaseRegFees = new Dollar("0.00");
	private RTSDate caExportOn;
	private RTSDate caLastExport;
	private ProcessInventoryData caProcInvPlt;
	private RenewalBarCodeData caRenewalBarCodeData;
	private Dollar caRenwlTotalFees = new Dollar("0.00");
	private RTSDate caSubconProcessDateTime;
	private Dollar caTmpPrice;

	//Transaction key
	//for deleting individual records
	private TransactionKey caTransactionKey;
	//stores this object's key value in the SortedMap in SubcontractorRenewalCacheData
	//used to indicate whether validation of plate is required
	private Integer caTransKeyNumber;
	// boolean 
	private boolean cbDiskEntry;
	private boolean cbError; //for diskette renewal
	private boolean cbInvalidRecord;
	private boolean cbMarkForDeletion;
	private boolean cbPosted;
	private boolean cbPrint; //print sticker 
	private boolean cbProcessed;
	private boolean cbMfgSpclPlt;

	private int ciAddlSetIndi;
	private int ciBarCdIndi;
	private int ciBarCdVersion;
	private int ciCntyNo;
	private int ciDiskSeqNo;
	private int ciEntryOrder;
	private int ciNewExpYr;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPltNextExpMo;
	private int ciPltNextExpYr;
	private int ciPltVldtyTerm;
	private int ciRecordType;
	private int ciRegExpMo;
	private int ciRegNextExpMo;
	private int ciRegNextExpYr;
	private int ciSpclPltIndi;
	private int ciSubconId;
	private int ciSubconIssueDate;
	private int ciTransTime;
	private int ciTransType;
	private int ciValidatePltIndi;
	// defect 10734 
	private int ciVehGrossWt;
	// end defect 10734 

	// long 
	private long clAuditTrailTransid;

	// String 
	private String csDocNo;
	private String csEmpId;
	private String csNewPltNo;
	private String csNewPltsReqdCd;
	private String csNewStkrNo;
	private String csOldPltItmCd;
	private String csOrgNo;
	private String csPltDesc;
	private String csPltItmCd;
	private String csRegClassCd;
	private String csRegPltNo;
	private String csRSPSId;
	private String csSpclPltRegPltCd;
	private String csStkrDesc;
	private String csStkrItmCd;
	private String csTmpItmCd;
	private String csTransID;
	private String csVIN;

	// defect 10734
	private int ciPltBirthDate;
	private int ciVehModlYr;
	private String csVehMk;
	private String csVehModl;
	// end defect 10734

	// Vector 
	// Populated after recalfees; Used for populating RTS_TR_FDS_DETAIL
	private Vector cvFeesDataTrFunds;

	public static final int ACCTITMCD1 = 13;
	public static final int ACCTITMCD10 = 31;
	public static final int ACCTITMCD11 = 33;
	public static final int ACCTITMCD12 = 35;
	public static final int ACCTITMCD13 = 37;
	public static final int ACCTITMCD14 = 39;
	public static final int ACCTITMCD15 = 41;
	public static final int ACCTITMCD2 = 15;
	public static final int ACCTITMCD3 = 17;
	public static final int ACCTITMCD4 = 19;
	public static final int ACCTITMCD5 = 21;
	public static final int ACCTITMCD6 = 23;
	public static final int ACCTITMCD7 = 25;
	public static final int ACCTITMCD8 = 27;
	public static final int ACCTITMCD9 = 29;
	public static final int ADDLSETINDI = 53;
	public static final int AUDITTRAILTRANSID = 3;
	public static final int BARCODEVERSION = 55;
	// end defect 10392 

	// defect 8451
	public static final int CNTYNO = 11;
	public static final int DISKSEQNO = 51;

	public static final int DO_NOT_VALIDATE_PLT = 0;

	// Constants for variable positions on diskette
	public static final int DOCNO = 1;
	public static final int EMPID = 47;
	public static final int EXPORTNO = 45;
	public static final int EXPORTON = 49;
	public static final int LASTEXPORT = 50;
	public static final int NEWEXPYRMO = 10;
	public static final int NEWPLTNO = 9;
	public static final int NEWPLTSREQDCD = 54;
	public static final int OLDPLTITMCD = 6;
	public static final int OLDSTKRITMCD = 7;
	public static final int ORGNO = 52;
	public static final int PLT = SubcontractorRenewalCacheData.PLT;
	public static final int PLT_STKR =
		SubcontractorRenewalCacheData.PLT_STKR;
	public static final int PLT_VALIDATED = 2;
	public static final int PLTEXPMO = 59;
	public static final int PLTEXPYR = 60;
	public static final int PLTITMCD = 4;
	public static final int PLTNXTEXPMO = 61;
	public static final int PLTNXTEXPYR = 62;
	public static final int PLTVLDTYTERM = 58;
	public static final int PROCESS = 0;
	public static final int REGCLASSCD = 43;
	public static final int REGNXTEXPMO = 56;
	public static final int REGNXTEXPYR = 57;
	public static final int REGPLTNO = 8;
	public static final int RENEWLITMPRICE1 = 14;
	public static final int RENEWLITMPRICE10 = 32;
	public static final int RENEWLITMPRICE11 = 34;
	public static final int RENEWLITMPRICE12 = 36;
	public static final int RENEWLITMPRICE13 = 38;
	public static final int RENEWLITMPRICE14 = 40;
	public static final int RENEWLITMPRICE15 = 42;
	public static final int RENEWLITMPRICE2 = 16;
	public static final int RENEWLITMPRICE3 = 18;
	public static final int RENEWLITMPRICE4 = 20;
	public static final int RENEWLITMPRICE5 = 22;
	public static final int RENEWLITMPRICE6 = 24;
	public static final int RENEWLITMPRICE7 = 26;
	public static final int RENEWLITMPRICE8 = 28;
	public static final int RENEWLITMPRICE9 = 30;
	public static final int REPRINT = 1;
	private final static int REQ_DOCNO_LENGTH = 17;
	public static final int RSPSID = 46;
	public static final int STKR = SubcontractorRenewalCacheData.STKR;
	public static final int STKRITMCD = 5;
	public static final int SUBCONID = 44;
	public static final int SUBCONISSUEDATE = 48;
	public static final int TRANSTYPE = 12;
	public static final int VALIDATE_PLT = 1;
	public static final int VIN = 2;

	//	defect 7586 
	//-1 (void), 0(processed),1 (reprint)
	// for TransType interpretation
	public static final int VOID = -1;

	private static Hashtable chtXMLTags = new Hashtable();
	static {
		chtXMLTags.put("DOC_NO", "1");
		chtXMLTags.put("VIN", "2");
		chtXMLTags.put("AUDIT_TRAIL_TRANS_ID", "3");
		chtXMLTags.put("NEW_PLT_TYPE", "4");
		chtXMLTags.put("NEW_STKR_TYPE", "5");
		chtXMLTags.put("OLD_PLT_TYPE", "6");
		chtXMLTags.put("OLD_STKR_TYPE", "7");
		chtXMLTags.put("CURR_PLT_NO", "8");
		chtXMLTags.put("NEW_PLT_NO", "9");
		chtXMLTags.put("EXP_YR_MO", "10");
		chtXMLTags.put("OFCISSUANCENO", "11");
		chtXMLTags.put("TRANS_TYPE", "12");
		chtXMLTags.put("ACCT_ITMCD1", "13");
		chtXMLTags.put("ACCT_ITMPRICE1", "14");
		chtXMLTags.put("ACCT_ITMCD2", "15");
		chtXMLTags.put("ACCT_ITMPRICE2", "16");
		chtXMLTags.put("ACCT_ITMCD3", "17");
		chtXMLTags.put("ACCT_ITMPRICE3", "18");
		chtXMLTags.put("ACCT_ITMCD4", "19");
		chtXMLTags.put("ACCT_ITMPRICE4", "20");
		chtXMLTags.put("ACCT_ITMCD5", "21");
		chtXMLTags.put("ACCT_ITMPRICE5", "22");
		chtXMLTags.put("ACCT_ITMCD6", "23");
		chtXMLTags.put("ACCT_ITMPRICE6", "24");
		chtXMLTags.put("ACCT_ITMCD7", "25");
		chtXMLTags.put("ACCT_ITMPRICE7", "26");
		chtXMLTags.put("ACCT_ITMCD8", "27");
		chtXMLTags.put("ACCT_ITMPRICE8", "28");
		chtXMLTags.put("ACCT_ITMCD9", "29");
		chtXMLTags.put("ACCT_ITMPRICE9", "30");
		chtXMLTags.put("ACCT_ITMCD10", "31");
		chtXMLTags.put("ACCT_ITMPRICE10", "32");
		chtXMLTags.put("ACCT_ITMCD11", "33");
		chtXMLTags.put("ACCT_ITMPRICE11", "34");
		chtXMLTags.put("ACCT_ITMCD12", "35");
		chtXMLTags.put("ACCT_ITMPRICE12", "36");
		chtXMLTags.put("ACCT_ITMCD13", "37");
		chtXMLTags.put("ACCT_ITMPRICE13", "38");
		chtXMLTags.put("ACCT_ITMCD14", "39");
		chtXMLTags.put("ACCT_ITMPRICE14", "40");
		chtXMLTags.put("ACCT_ITMCD15", "41");
		chtXMLTags.put("ACCT_ITMPRICE15", "42");
		chtXMLTags.put("REG_CLASS_CD", "43");
		chtXMLTags.put("LOC_ID", "44");
		chtXMLTags.put("EXP_NO", "45");
		chtXMLTags.put("RSPS_ID", "46");
		chtXMLTags.put("USER_ID", "47");
		chtXMLTags.put("ISSUE_DATE", "48");
		chtXMLTags.put("EXPORT_DATE", "49");
		chtXMLTags.put("LAST_EXPORT", "50");
		chtXMLTags.put("DSK_SEQ_NO", "51");
		chtXMLTags.put("ORG_NO", "52");
		chtXMLTags.put("ADDL_SET_INDI", "53");
		chtXMLTags.put("NEW_PLATES_REQD_CD", "54");
		chtXMLTags.put("BAR_CD_VERSION", "55");
		chtXMLTags.put("REG_NXT_EXP_MO", "56");
		chtXMLTags.put("REG_NXT_EXP_YR", "57");
		chtXMLTags.put("PLT_VLDTY_TERM", "58");
		chtXMLTags.put("PLT_EXP_MO", "59");
		chtXMLTags.put("PLT_EXP_YR", "60");
		chtXMLTags.put("PLT_NXT_EXP_MO", "61");
		chtXMLTags.put("PLT_NXT_EXP_YR", "62");
	}

	private final static long serialVersionUID = 5657346928437118601L;

	/**
	 * SubcontractorRenewalData constructor comment.
	 */
	public SubcontractorRenewalData()
	{
		super();
	}
	/**
	 * SubcontractorRenewalData constructor comment.
	 */
	public SubcontractorRenewalData(
		WebAgencyTransactionData aaWATData,
		Vector avWATFeeData)
	{
		super();

		if (!UtilityMethods.isEmpty(aaWATData.getInvItmNo()))
		{
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(aaWATData.getRegPltCd());

			if (laPltTypeData.isAnnualPlt())
			{
				setRecordType(SubcontractorRenewalData.PLT);
			}
			else
			{
				setRecordType(SubcontractorRenewalData.PLT_STKR);
			}
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setInvLocIdCd("S");
			laProcInvData.setInvId("" + aaWATData.getSubconId());
			setProcInvPlt(laProcInvData);
		}
		else
		{
			setRecordType(SubcontractorRenewalData.STKR);
		}

		if (PlateTypeCache.isSpclPlate(aaWATData.getRegPltCd()))
		{
			ciSpclPltIndi = 1;
			setSpclPltRegPltCd(aaWATData.getRegPltCd());
			setMfgSpclPlt(aaWATData.getMustReplPltIndi() == 1);
		}
		setCntyNo(aaWATData.getResComptCntyNo());
		if (!UtilityMethods.isEmpty(aaWATData.getBarCdVersionNo()))
		{
			try
			{
				setBarCdVersion(
					Integer.parseInt(aaWATData.getBarCdVersionNo()));
			}
			catch (NumberFormatException aeNFEx)
			{

			}
		}
		setVIN(aaWATData.getVIN());
		setDocNo(aaWATData.getDocNo());
		setVehGrossWt(aaWATData.getVehGrossWt());
		setVehMk(aaWATData.getVehMk());
		setVehModl(aaWATData.getVehModl());
		setVehModlYr(aaWATData.getVehModlYr());
		setRegPltNo(aaWATData.getRegPltNo());
		setRegClassCd("" + aaWATData.getRegClassCd());
		setOldPltItmCd(aaWATData.getRegPltCd());
		setPltItmCd(aaWATData.getRegPltCd());
		setStkrItmCd(aaWATData.getStkrItmCd());
		setAuditTrailTransid(
			Long.parseLong(aaWATData.getAuditTrailTransId()));
		setRegExpMo(aaWATData.getRegExpMo());
		setNewExpYr(aaWATData.getNewRegExpYr());
		setRegNextExpMo(aaWATData.getNewRegExpMo());
		setRegNextExpYr(aaWATData.getNewRegExpYr());
		setNewPltNo(aaWATData.getInvItmNo());
		setOrgNo(aaWATData.getOrgNo());
		setAddlSetIndi(aaWATData.getAddlSetIndi());
		setPltBirthDate(aaWATData.getPltBirthDate());
		setPltVldtyTerm(aaWATData.getPltValidityTerm());
		setPltExpMo(aaWATData.getPltExpMo());
		setPltExpYr(aaWATData.getPltExpYr());
		setPltNextExpMo(aaWATData.getNewPltExpMo());
		setPltNextExpYr(aaWATData.getNewPltExpYr());
		setSubconId(aaWATData.getSubconId());
		setSubconIssueDate(
			aaWATData.getInitReqTimestmp().getYYYYMMDDDate());
		setSubconProcessDateTime(new RTSDate());

		if (avWATFeeData != null && avWATFeeData.size() != 0)
		{
			for (int i = 0; i < avWATFeeData.size(); i++)
			{
				WebAgencyTransactionFeeData laWATFeesData =
					(
						WebAgencyTransactionFeeData) avWATFeeData
							.elementAt(
						i);

				csTmpItmCd = laWATFeesData.getAcctItmCd();
				populateItmPrice(
							"" + laWATFeesData.getItmPrice(),
							i == 0);
				}
		}
	}

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject Object
	 * @return  int
	 * 
	 * @throws ClassCastException 
	 */
	public int compareTo(Object aaObject)
	{
		SubcontractorRenewalData laSBRNWData =
			(SubcontractorRenewalData) aaObject;
		if (getTransID() == null || laSBRNWData.getTransID() == null)
		{
			return 0;
		}
		return getTransID().compareTo(laSBRNWData.getTransID());
	}

	/**
	 * Get value of ciAddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}
	/**
	 * Get the attributes  
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHashMap = new HashMap();
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					fields[i].getName(),
					fields[i].get(this));
			}
			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
	}
	/**
	 * Return the value of AuditTrailTransid
	 * 
	 * @return int
	 */
	public long getAuditTrailTransid()
	{
		return clAuditTrailTransid;
	}
	/**
	 * Return the value of BarCdIndi
	 * 
	 * @return int
	 */
	public int getBarCdIndi()
	{
		return ciBarCdIndi;
	}
	/**
	 * Return the value of CntyNo
	 * 
	 * @return int
	 */
	public int getCntyNo()
	{
		return ciCntyNo;
	}
	/**
	 * Return the value of CustBaseRegFees
	 * 
	 * @return Dollar
	 */
	public Dollar getCustBaseRegFees()
	{
		return caCustBaseRegFees;
	}
	/**
	 * Return the value of DiskSeqNo
	 * 
	 * @return int
	 */
	public int getDiskSeqNo()
	{
		return ciDiskSeqNo;
	}
	/**
	 * Return the value of DocNo
	 * 
		* @return int
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Return the value of EmpId
	 * 
	 * @return String
	 */
	public String getEmpId()
	{
		return csEmpId;
	}
	/**
	 * Return the value of EntryOrder
	 * 
	 * @return int
	 */
	public int getEntryOrder()
	{
		return ciEntryOrder;
	}
	/**
	 * Return the value of ExportOn
	 * 
	 * @return RTSDate
	 */
	public RTSDate getExportOn()
	{
		return caExportOn;
	}
	/**
	 * Return the value of FeesDataTrFunds
	 * 
	 * @return Vector
	 */
	public Vector getFeesDataTrFunds()
	{
		return cvFeesDataTrFunds;
	}
	/**
	 * Used by black box
	 * 
	 * @return String
	 * @param aiFieldNum int
	 */
	public String getField(int aiFieldNum)
	{
		switch (aiFieldNum)
		{
			case 1 :
				return "";
				// etc...
			default :
				return null;
		}
	}
	/**
	 * Return the value of LastExport
	 * 
	 * @return RTSDate
	 */
	public RTSDate getLastExport()
	{
		return caLastExport;
	}
	/**
	 * Return the value of NewExpYr
	 * 
	 * @return int
	 */
	public int getNewExpYr()
	{
		return ciNewExpYr;
	}
	/**
	 * Return the value of NewPltNo
	 * 
	 * @return String
	 */
	public String getNewPltNo()
	{
		return csNewPltNo;
	}

	/**
	 * Get value of csNewPltsReqdCd
	 * 
	 * @return String 
	 */
	public String getNewPltsReqdCd()
	{
		return csNewPltsReqdCd;
	}
	/**
	 * Return the value of NewStkrNo
	 * 
	 * @return String
	 */
	public String getNewStkrNo()
	{
		return csNewStkrNo;
	}

	/**
	 * Get value of OldPltItmCd
	 * 
	 * @return String
	 */
	public String getOldPltItmCd()
	{
		return csOldPltItmCd;
	}

	/**
	 * Return value of csOrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}
	/**
	 * Return the value of PltDesc
	 * 
	 * @return String
	 */
	public String getPltDesc()
	{
		return csPltDesc;
	}

	/**
	 * Get value of ciPltExpMo
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Get value of ciPltExpYr
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}
	/**
	 * Return the value of PltItmCd
	 * 
	 * @return String
	 */
	public String getPltItmCd()
	{
		return csPltItmCd;
	}

	/**
	 * Get value of ciPltNextExpMo
	 * 
	 * @return int
	 */
	public int getPltNextExpMo()
	{
		return ciPltNextExpMo;
	}

	/**
	 * Get value of ciPltNextExpYr
	 * 
	 * @return int
	 */
	public int getPltNextExpYr()
	{
		return ciPltNextExpYr;
	}
	/** 
	 * 
	 * Return calculated Months Sold 
	 * 
	 * @return int 
	 */
	public int getPltSoldMos()
	{
		int liPltSoldMos = 0;

		if (getPltNextExpMo() != 0 && getPltNextExpYr() != 0)
		{
			liPltSoldMos =
				(getPltNextExpMo() + getPltNextExpYr() * 12)
					- (getPltExpMo() + getPltExpYr() * 12);
		}
		return liPltSoldMos;
	}

	/**
	 * Get value of ciPltVldtyTerm
	 * 
	 * @return int
	 */
	public int getPltVldtyTerm()
	{
		return ciPltVldtyTerm;
	}

	/**
	 * Return the value of ProcInvPlt
	 * 
	 * @return ProcessInventoryData
	 */
	public ProcessInventoryData getProcInvPlt()
	{
		return caProcInvPlt;
	}
	/**
	 * Return the value of RecordType
	 * 
	 * @return int
	 */
	public int getRecordType()
	{
		return ciRecordType;
	}
	/**
	 * Return the value of RegClassCd
	 * 
	 * @return String
	 */
	public String getRegClassCd()
	{
		return csRegClassCd;
	}
	/**
	 * Return the value of RegExpMo
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Get value of ciRegNextExpMo
	 * 
	 * @return int
	 */
	public int getRegNextExpMo()
	{
		return ciRegNextExpMo;
	}

	/**
	 * Get value of ciRegNextExpYr
	 * 
	 * @return int
	 */
	public int getRegNextExpYr()
	{
		return ciRegNextExpYr;
	}
	/**
	 * Return the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	/**
	 * Return the value of RenewalBarCodeData
	 * 
	 * @return RenewalBarCodeData
	 */
	public RenewalBarCodeData getRenewalBarCodeData()
	{
		return caRenewalBarCodeData;
	}
	/**
	 * Return the value of RenwlTotalFees
	 * 
	 * @return Dollar
	 */
	public Dollar getRenwlTotalFees()
	{
		return caRenwlTotalFees;
	}

	/**
	 * Return the value of RSPSID
	 * 
	 * @return String
	 */
	public String getRSPSId()
	{
		return csRSPSId;
	}
	/**
	 * Return value of ciSpclPltIndi
	 * 
	 * @return int
	 */
	public int getSpclPltIndi()
	{
		return ciSpclPltIndi;
	}

	/**
	 * Return value of csSpclPltRegPltCd
	 * 
	 * @return String
	 */
	public String getSpclPltRegPltCd()
	{
		return csSpclPltRegPltCd;
	}
	/**
	 * Return the value of StkrDesc
	 * 
	 * @return String
	 */
	public String getStkrDesc()
	{
		return csStkrDesc;
	}
	/**
	 * Return the value of StkrItmCd
	 * 
	 * @return String
	 */
	public String getStkrItmCd()
	{
		return csStkrItmCd;
	}
	/**
	 * Return the value of SubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}
	/**
	 * Return the value of SubconIssueDate
	 * 
	 * @return int
	 */
	public int getSubconIssueDate()
	{
		return ciSubconIssueDate;
	}
	/**
	 * Return the value of SubconProcessDateTime
	 * 
	 * @return RTSDate
	 */
	public RTSDate getSubconProcessDateTime()
	{
		return caSubconProcessDateTime;
	}
	/**
	 * Return the value of TransactionKey
	 * 
	 * @return TransactionKey
	 */
	public TransactionKey getTransactionKey()
	{
		return caTransactionKey;
	}
	/**
	 * Return the value of TransID
	 * 
	 * @return String
	 */
	public String getTransID()
	{
		return csTransID;
	}
	/**
	 * Return the value of TransKeyNumber
	 * 
	 * @return Integer
	 */
	public Integer getTransKeyNumber()
	{
		return caTransKeyNumber;
	}
	/**
	 * Return the value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Return the value of TransType
	 * 
	 * @return int
	 */
	public int getTransType()
	{
		return ciTransType;
	}
	/**
	 * Return the value of ValidatePltIndi
	 * 
	 * @return int
	 */
	public int getValidatePltIndi()
	{
		return ciValidatePltIndi;
	}

	/**
	 * Get value of ciVehGrossWt
	 * 
	 * @return int
	 */
	public int getVehGrossWt()
	{
		return ciVehGrossWt;
	}

	/**
	 * Get value of csVehMk 
	 * 
	 * @return String 
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get value of csVehModl
	 * 
	 * @return String 
	 */
	public String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Get value of ciVehModlYr
	 * 
	 * @return int 
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}
	/**
	 * Return the value of VIN
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}
	/**
	 * Return the value of DiskEntry
	 * 
	 * @return boolean
	 */
	public boolean isDiskEntry()
	{
		return cbDiskEntry;
	}
	/**
	 * Return the value of Error
	 * 
	 * @return boolean
	 */
	public boolean isError()
	{
		return cbError;
	}
	/**
	 * Return the value of InvalidRecord
	 * 
	 * @return boolean
	 */
	public boolean isInvalidRecord()
	{
		return cbInvalidRecord;
	}
	/**
	 * Return the value of MarkForDeletion
	 * 
	 * @return boolean
	 */
	public boolean isMarkForDeletion()
	{
		return cbMarkForDeletion;
	}
	/**
	 * Return boolean to denote if Special Plate is to be Manufactured
	 * 
	 * @return boolean 
	 */
	public boolean isMfgSpclPlt()
	{
		return cbMfgSpclPlt;
	}
	/**
	 * Return the value of Posted
	 * 
	 * @return boolean
	 */
	public boolean isPosted()
	{
		return cbPosted;
	}
	/**
	 * Return the value of Print
	 * 
	 * @return boolean
	 */
	public boolean isPrint()
	{
		return cbPrint;
	}
	/**
	 * Return the value of Processed
	 * 
	 * @return boolean
	 */
	public boolean isProcessed()
	{
		return cbProcessed;
	}
	/**
	 * Populates item code
	 * 
	 * @param value String
	 */
	private void populateItmCd(String value)
	{
		if (value == null || value.trim().equals(""))
		{
			csTmpItmCd = null;
		}
		else
		{
			csTmpItmCd = value;
		}
	}
	/**
	 * Populates item price
	 * 
	 * @param asValue String
	 */
	private void populateItmPrice(
		String asValue,
		boolean abFirstRecord)
	{
		if (asValue != null && !asValue.trim().equals(""))
		{
			try
			{
				Float.parseFloat(asValue);
			}
			catch (NumberFormatException aeNFE)
			{
				setInvalidRecord(true);
				csTmpItmCd = null;
				caTmpPrice = null;
				return;
			}
			caTmpPrice = new Dollar(asValue);
		}
		else
		{
			csTmpItmCd = null;
			caTmpPrice = null;
			return;
		}
		if (csTmpItmCd != null && caTmpPrice != null)
		{
			if (cvFeesDataTrFunds == null)
			{
				cvFeesDataTrFunds = new Vector();
			}
			FeesData laFeesData = new FeesData();
			laFeesData.setAcctItmCd(csTmpItmCd);
			laFeesData.setItmQty(1);
			laFeesData.setItemPrice(caTmpPrice);
			int liToday = new RTSDate().getYYYYMMDDDate();
			AccountCodesData laAcctCodesData =
				AccountCodesCache.getAcctCd(csTmpItmCd, liToday);
			if (laAcctCodesData == null)
			{
				setInvalidRecord(true);
			}
			else
			{
				laFeesData.setDesc(laAcctCodesData.getAcctItmCdDesc());
				laFeesData.setCrdtAllowedIndi(
					laAcctCodesData.getCrdtAllowdIndi());
				// defect 11240
				// RTS_MV_FUNC_TRANS CustActulRegFee column is not large enough to handle
				// optional fees... bypass adding the optional fees to totals
				if (isEligibleForRegFee(laAcctCodesData.getAcctItmCd().trim()))
				{
					setRenwlTotalFees(
							getRenwlTotalFees().add(laFeesData.getItemPrice()));

					setCustBaseRegFees(
							getCustBaseRegFees().add(
									laFeesData.getItemPrice()));
				}
				//	end defect 11240
			}
			cvFeesDataTrFunds.addElement(laFeesData);
		}
		else
		{
			//reprint has no amount
			if (abFirstRecord && ciTransType != 1)
			{
				setInvalidRecord(true);
			}
		}
		csTmpItmCd = null;
		caTmpPrice = null;
	}

	/**
	 * Set value of ciAddlSetIndi
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}
	/**
	 * Set  the value of clAuditTrailTransid
	 * 
	 * @param alAuditTrailTransid int
	 */
	public void setAuditTrailTransid(long alAuditTrailTransid)
	{
		clAuditTrailTransid = alAuditTrailTransid;
	}
	/**
	 * Set  the value of BarCdIndi
	 * 
	 * @param aiBarCdIndi int
	 */
	public void setBarCdIndi(int aiBarCdIndi)
	{
		ciBarCdIndi = aiBarCdIndi;
	}
	/**
	 * Set  the value of BarCdVersion
	 * 
	 * @param aiBarCdVersion int
	 */
	public void setBarCdVersion(int aiBarCdVersion)
	{
		ciBarCdVersion = aiBarCdVersion;
	}
	/**
	 * Set  the value of CntyNo
	 * 
	 * @param aiCntyNo int
	 */
	public void setCntyNo(int aiCntyNo)
	{
		ciCntyNo = aiCntyNo;
	}
	/**
	 * Set  the value of CustBaseRegFees
	 * 
	 * @param aaCustBaseRegFees Dollar
	 */
	public void setCustBaseRegFees(Dollar aaCustBaseRegFees)
	{
		caCustBaseRegFees = aaCustBaseRegFees;
	}
	/**
	 * Set  the value of DiskEntry
	 * 
	 * @param abDiskEntry boolean
	 */
	public void setDiskEntry(boolean abDiskEntry)
	{
		cbDiskEntry = abDiskEntry;
	}
	/**
	 * Set  the value of DiskSeqNo
	 * 
	 * @param aiDiskSeqNo int
	 */
	public void setDiskSeqNo(int aiDiskSeqNo)
	{
		ciDiskSeqNo = aiDiskSeqNo;
	}
	/**
	 * Set  the value of DocNo
	 * 
	 * @param asDocNo int
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Set  the value of EmpId
	 * 
	 * @param asEmpId String
	 */
	public void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}
	/**
	 * Set  the value of EntryOrder
	 * 
	 * @param aiEntryOrder int
	 */
	public void setEntryOrder(int aiEntryOrder)
	{
		ciEntryOrder = aiEntryOrder;
	}
	/**
	 * Set  the value of Error
	 * 
	 * @param abError boolean
	 */
	public void setError(boolean abError)
	{
		cbError = abError;
	}
	/**
	 * Set  the value of ExportOn
	 * 
	 * @param aaExportOn RTSDate
	 */
	public void setExportOn(RTSDate aaExportOn)
	{
		caExportOn = aaExportOn;
	}
	/**
	 * Set  the value of cvFeesDataTrFunds
	 * 
	 * @param avFeesDataTrFunds Vector
	 */
	public void setFeesDataTrFunds(Vector avFeesDataTrFunds)
	{
		cvFeesDataTrFunds = avFeesDataTrFunds;
	}
	/**
	 * Called be parser to set the data corresponding to the field number.
	 * 
	 * @param aiFieldNum int
	 * @param asValue String
	 */
	public void setField(int aiFieldNum, String asValue)
	{
		if (asValue != null)
		{
			// defect 7983 
			// trim here to avoid later trimming
			asValue = asValue.trim();
			// end defect 7983  

			switch (aiFieldNum)
			{
				// 1
				case DOCNO :
					{
						//set disk entry to true
						setDiskEntry(true);
						// defect 7983 
						// Verify Doc No Length 
						if (asValue.length() != REQ_DOCNO_LENGTH)
						{
							setInvalidRecord(true);
						}
						else
						{
							//Check Mandatory Field
							try
							{
								Long.parseLong(asValue);
								setDocNo(asValue.trim().toUpperCase());
							}
							catch (NumberFormatException aeNFEx)
							{
								setInvalidRecord(true);
							}
							setInvalidRecord(false);
						}
						// end defect 7893 
						break;

					}
					// 2
				case VIN :
					{
						// defect 7983 
						// Verify length VIN 
						if (asValue.length()
							> CommonConstant.LENGTH_VIN_MAX)
						{
							setInvalidRecord(true);
						}
						else
						{
							// defect 7674
							// Do not set record Invalid if no VIN 
							setVIN(asValue.toUpperCase());
							//
							//if (getVIN().equals(""))
							//{
							//	setInvalidRecord(true);
							//}
							// end defect 7674
						}
						// end defect 7983 
						break;
					}
					// 3		
				case AUDITTRAILTRANSID :
					{
						// defect 10075
						// diskValidations() refactored/renamed to
						// mediaValidations()
						setAuditTrailTransid(
							MediaValidations.validateAuditTrailTransid(
								asValue.toUpperCase()));
						// end defect 10075
						if (getAuditTrailTransid() == 0)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 4
				case PLTITMCD :
					{
						ItemCodesData laItemCodesData =
							ItemCodesCache.getItmCd(asValue);
						if (laItemCodesData == null)
						{
							if (asValue.equals(""))
							{
								setPltItmCd(null);
								setPltDesc(null);
							}
							else
							{
								setInvalidRecord(true);
							}
						}
						else
						{
							setPltItmCd(
								laItemCodesData
									.getItmCd()
									.trim()
									.toUpperCase());
							setPltDesc(
								laItemCodesData
									.getItmCdDesc()
									.trim()
									.toUpperCase());
						}
						break;
					}
					// 5
				case STKRITMCD :
					{
						ItemCodesData laItemCodesData =
							ItemCodesCache.getItmCd(asValue);
						if (laItemCodesData == null)
						{
							if (asValue.equals(""))
							{
								setStkrItmCd(null);
								setStkrDesc(null);
							}
							else
							{
								setInvalidRecord(true);
							}
						}
						else
						{
							setStkrItmCd(
								laItemCodesData
									.getItmCd()
									.trim()
									.toUpperCase());
							setStkrDesc(
								laItemCodesData
									.getItmCdDesc()
									.trim()
									.toUpperCase());
						}
						if (getPltItmCd() == null
							&& getStkrItmCd() == null)
						{
							setInvalidRecord(true);
						}
						if (!isInvalidRecord())
						{
							if (getPltItmCd() != null
								&& getStkrItmCd() != null)
							{
								setRecordType(
									SubcontractorRenewalData.PLT_STKR);
							}
							else if (getPltItmCd() != null)
							{
								setRecordType(
									SubcontractorRenewalData.PLT);
							}
							else
							{
								setRecordType(
									SubcontractorRenewalData.STKR);
							}
						}
						break;
					}
					// 6
					// defect 
				case OLDPLTITMCD :
					{
						if (asValue != null)
						{
							setOldPltItmCd(asValue.trim());
						}
						break;
					}
					// 7
				case OLDSTKRITMCD :
					{
						break;
					}
					// 8 
				case REGPLTNO :
					{
						setRegPltNo(asValue.toUpperCase());
						if (getRegPltNo().equals(""))
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 9 
				case NEWPLTNO :
					{
						setNewPltNo(asValue.toUpperCase());
						if (getPltItmCd() != null
							&& getNewPltNo().equals(""))
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 10
				case NEWEXPYRMO :
					{
						try
						{
							new Integer(asValue);
							if (asValue.length() == 6)
							{
								setNewExpYr(
									Integer.parseInt(
										asValue.substring(0, 4))
										+ 1);
								setRegExpMo(
									Integer.parseInt(
										asValue.substring(4)));
							}
							else
							{
								throw new NumberFormatException();
							}
						}
						catch (NumberFormatException aeNFEx)
						{
							setRegExpMo(0);
							setNewExpYr(0);
						}
						if (getRegExpMo() == 0 || getNewExpYr() == 0)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 11
				case CNTYNO :
					{
						try
						{
							setCntyNo(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFEx)
						{
							setCntyNo(0);
						}
						if (getCntyNo() == 0)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 12 
				case TRANSTYPE :
					{
						try
						{
							setTransType(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFEx)
						{
							setInvalidRecord(true);
						}
						if (ciTransType != VOID
							&& ciTransType != PROCESS
							&& ciTransType != REPRINT)
						{
							setInvalidRecord(true);
						}
						break;
					}

					// 13
				case ACCTITMCD1 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 14
				case RENEWLITMPRICE1 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 15 
				case ACCTITMCD2 :
					{
						populateItmCd(asValue);
						break;
					}
					// 16
				case RENEWLITMPRICE2 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// 17 
				case ACCTITMCD3 :
					{
						populateItmCd(asValue);
						break;
					}
					// 18
				case RENEWLITMPRICE3 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// 19
				case ACCTITMCD4 :
					{
						populateItmCd(asValue);
						break;
					}
					// 20
				case RENEWLITMPRICE4 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// 21 
				case ACCTITMCD5 :
					{
						populateItmCd(asValue);
						break;
					}
					// 22
				case RENEWLITMPRICE5 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// 23 
				case ACCTITMCD6 :
					{
						populateItmCd(asValue);
						break;
					}
					// 24
				case RENEWLITMPRICE6 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// 25 
				case ACCTITMCD7 :
					{
						populateItmCd(asValue);
						break;
					}
					// 26
				case RENEWLITMPRICE7 :
					{
						populateItmPrice(asValue, false);
						break;
					}
					// defect 7287 
					// 27
				case ACCTITMCD8 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 28 
				case RENEWLITMPRICE8 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 29 
				case ACCTITMCD9 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 30 
				case RENEWLITMPRICE9 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 31
				case ACCTITMCD10 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 32
				case RENEWLITMPRICE10 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 33
				case ACCTITMCD11 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 34 
				case RENEWLITMPRICE11 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 35 
				case ACCTITMCD12 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 36
				case RENEWLITMPRICE12 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 37
				case ACCTITMCD13 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 38 
				case RENEWLITMPRICE13 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 39
				case ACCTITMCD14 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 40 
				case RENEWLITMPRICE14 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// 41
				case ACCTITMCD15 :
					{
						populateItmCd(asValue.toUpperCase());
						break;
					}
					// 42
				case RENEWLITMPRICE15 :
					{
						populateItmPrice(asValue, true);
						break;
					}
					// end defect 7287 
					// 43
				case REGCLASSCD :
					{
						setRegClassCd(asValue.toUpperCase());
						if (getRegClassCd().equals(""))
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 44 
				case SUBCONID :
					{
						try
						{
							setSubconId(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}

					// 45 
				case EXPORTNO :
					{
						break;
					}
					// 46
				case RSPSID :
					{
						if (asValue == null || asValue.length() == 0)
						{
							setInvalidRecord(true);
						}
						else
						{
							setRSPSId(asValue);
						}
						break;
					}
					// 47
				case EMPID :
					{
						setEmpId(asValue);
						break;
					}
					// 48 
				case SUBCONISSUEDATE :
					{
						if (asValue == null || asValue.length() != 14)
						{
							setInvalidRecord(true);
						}
						else
						{
							try
							{
								// defect 7413 
								setSubconIssueDate(
									Integer.parseInt(
										asValue.substring(0, 8)));
								setSubconProcessDateTime(
									new RTSDate(
										Integer.parseInt(
											asValue.substring(0, 4)),
										Integer.parseInt(
											asValue.substring(4, 6)),
										Integer.parseInt(
											asValue.substring(6, 8)),
										Integer.parseInt(
											asValue.substring(8, 10)),
										Integer.parseInt(
											asValue.substring(10, 12)),
										Integer.parseInt(
											asValue.substring(12, 14)),
										0));
								// end defect 7413 
							}
							catch (NumberFormatException aeNFex)
							{
								setInvalidRecord(true);
							}
						}
						break;
					}
					// 49
				case EXPORTON :
					{
						if (asValue == null || asValue.length() != 14)
						{
							setInvalidRecord(true);
						}
						else
						{
							try
							{
								setExportOn(
									new RTSDate(
										Integer.parseInt(
											asValue.substring(0, 4)),
										Integer.parseInt(
											asValue.substring(4, 6)),
										Integer.parseInt(
											asValue.substring(6, 8)),
										Integer.parseInt(
											asValue.substring(8, 10)),
										Integer.parseInt(
											asValue.substring(10, 12)),
										Integer.parseInt(
											asValue.substring(12, 14)),
										0));
							}
							catch (NumberFormatException aeNFex)
							{
								setInvalidRecord(true);
							}
						}
						break;
					}

					// 50
				case LASTEXPORT :
					{
						if (asValue == null || asValue.length() != 14)
						{
							setInvalidRecord(true);
						}
						else
						{
							try
							{
								setLastExport(
									new RTSDate(
										Integer.parseInt(
											asValue.substring(0, 4)),
										Integer.parseInt(
											asValue.substring(4, 6)),
										Integer.parseInt(
											asValue.substring(6, 8)),
										Integer.parseInt(
											asValue.substring(8, 10)),
										Integer.parseInt(
											asValue.substring(10, 12)),
										Integer.parseInt(
											asValue.substring(12, 14)),
										0));
							}
							catch (NumberFormatException aeNFex)
							{
								setInvalidRecord(true);
							}
						}
						break;
					}

					// 51 	
				case DISKSEQNO :
					{
						if (asValue.equals(""))
						{
							setInvalidRecord(true);
						}
						else
						{
							try
							{
								setDiskSeqNo(Integer.parseInt(asValue));
							}
							catch (NumberFormatException aeNFex)
							{
								setInvalidRecord(true);
							}
						}
						break;
					}
					// defect 9362 
					// 52
				case ORGNO :
					{
						setOrgNo(asValue);
						break;
					}
					// 53 
				case ADDLSETINDI :
					{
						try
						{
							setAddlSetIndi(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 54
				case NEWPLTSREQDCD :
					{
						setNewPltsReqdCd(asValue);
						break;
					}
					// 55 
				case BARCODEVERSION :
					{
						try
						{
							setBarCdVersion(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// end defect 9362 
					// defect 10392 
					// 56 
				case REGNXTEXPMO :
					{
						try
						{
							setRegNextExpMo(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 57 
				case REGNXTEXPYR :
					{
						try
						{
							setRegNextExpYr(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 58 
				case PLTVLDTYTERM :
					{
						try
						{
							setPltVldtyTerm(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 59 
				case PLTEXPMO :
					{
						try
						{
							setPltExpMo(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 60 
				case PLTEXPYR :
					{
						try
						{
							setPltExpYr(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 61 
				case PLTNXTEXPMO :
					{
						try
						{
							setPltNextExpMo(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// 62 
				case PLTNXTEXPYR :
					{
						try
						{
							setPltNextExpYr(Integer.parseInt(asValue));
						}
						catch (NumberFormatException aeNFex)
						{
							setInvalidRecord(true);
						}
						break;
					}
					// end defect 10392 

				default :
					setInvalidRecord(true);
			}
		}
	}
	/**
	 * Called be parser to set the data corresponding to the field number.
	 * 
	 * @param aiFieldNum int
	 * @param asValue String
	 * @param aiRecType	int
	 */
	public void setField(int aiFieldNum, String asValue, int aiRecType)
	{
	}

	/**
	 * Used to load all of the fields in this data class with values
	 * from an XML file.
	 * 
	 * @param aaNode Node
	 */
	public void setFields(Node aaNode)
	{
		NodeList laNodes = aaNode.getChildNodes();
		if (laNodes != null)
		{
			for (int i = 1; i <= laNodes.getLength(); i++)
			{
				String lsValue = CommonConstant.STR_SPACE_EMPTY;
				Node lsChild = laNodes.item(i - 1).getFirstChild();
				if (lsChild != null && lsChild.getNodeValue() != null)
				{
					lsValue = lsChild.getNodeValue();
				}
				int liFieldNum =
					Integer.parseInt(
						String.valueOf(
							chtXMLTags.get(
								laNodes.item(i - 1).getNodeName())));
				setField(liFieldNum, lsValue);
			}
			// defect 9362 
			// Set Indicators for Special Plate Processing
			setupAsSpclPlt();
			// end defect 9362 
		}
	}

	/**
	 * Set  the value of InvalidRecord
	 * 
	 * @param abInvalidRecord boolean
	 */
	public void setInvalidRecord(boolean abInvalidRecord)
	{
		cbInvalidRecord = abInvalidRecord;
	}

	/**
	 * Set  the value of LastExport
	 * 
	 * @param aaLastExport RTSDate
	 */
	public void setLastExport(RTSDate aaLastExport)
	{
		caLastExport = aaLastExport;
	}
	/**
	 * Set  the value of MarkForDeletion
	 * 
	 * @param abMarkForDeletion boolean
	 */
	public void setMarkForDeletion(boolean abMarkForDeletion)
	{
		cbMarkForDeletion = abMarkForDeletion;
	}
	/**
	 * Set boolean to denote if Special Plate is to be Manufactured
	 * 
	 * @param abMfgSpclPlt
	 */
	public void setMfgSpclPlt(boolean abMfgSpclPlt)
	{
		cbMfgSpclPlt = abMfgSpclPlt;
	}
	/**
	 * Set  the value of NewExpYr
	 * 
	 * @param aaExpYr int
	 */
	public void setNewExpYr(int aaExpYr)
	{
		ciNewExpYr = aaExpYr;
	}
	/**
	 * Set  the value of NewPltNo
	 * 
	 * @param aaPltNo String
	 */
	public void setNewPltNo(String aaPltNo)
	{
		csNewPltNo = aaPltNo;
	}

	/**
	 * Set value of csNewPltsReqdCd
	 * 
	 * @param asNewPltsReqdCd
	 */
	public void setNewPltsReqdCd(String asNewPltsReqdCd)
	{
		csNewPltsReqdCd = asNewPltsReqdCd;
	}
	/**
	 * Set  the value of NewStkrNo
	 * 
	 * @param aaStkrNo String
	 */
	public void setNewStkrNo(String aaStkrNo)
	{
		csNewStkrNo = aaStkrNo;
	}

	/**
	 * Set value of OldPltItmCd
	 * 
	 * @param asOldPltItmCd
	 */
	public void setOldPltItmCd(String asOldPltItmCd)
	{
		csOldPltItmCd = asOldPltItmCd;
	}

	/**
	 * Set value of csOrgNo
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}
	/**
	 * Set  the value of PltDesc
	 * 
	 * @param asPltDesc String
	 */
	public void setPltDesc(String asPltDesc)
	{
		csPltDesc = asPltDesc;
	}

	/**
	 * Set value of ciPltExpMo
	 * 
	 * @param aiPltExpMo
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set value of ciPltExpYr
	 * 
	 * @param aiPltExpYr
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}
	/**
	 * Set  the value of PltItmCd
	 * 
	 * @param asPltItmCd String
	 */
	public void setPltItmCd(String asPltItmCd)
	{
		csPltItmCd = asPltItmCd;
	}

	/**
	 * Set value of ciPltNextExpMo
	 * 
	 * @param aiPltNextExpMo
	 */
	public void setPltNextExpMo(int aiPltNextExpMo)
	{
		ciPltNextExpMo = aiPltNextExpMo;
	}

	/**
	 * Set value of ciPltNextExpYr
	 * 
	 * @param aiPltNextExpYr
	 */
	public void setPltNextExpYr(int aiPltNextExpYr)
	{
		ciPltNextExpYr = aiPltNextExpYr;
	}

	/**
	 * Set value of ciPltVldtyTerm
	 * 
	 * @param aiPltVldtyTerm
	 */
	public void setPltVldtyTerm(int aiPltVldtyTerm)
	{
		ciPltVldtyTerm = aiPltVldtyTerm;
	}
	/**
	 * Set  the value of Posted 
	 * 
	 * @param abPosted boolean
	 */
	public void setPosted(boolean abPosted)
	{
		cbPosted = abPosted;
	}
	/**
	 * Set  the value of Print
	 * 
	 * @param abPrint boolean
	 */
	public void setPrint(boolean abPrint)
	{
		cbPrint = abPrint;
	}
	/**
	 * Set  the value of Processed
	 * 
	 * @param abProcessed boolean
	 */
	public void setProcessed(boolean abProcessed)
	{
		cbProcessed = abProcessed;
	}
	//	/**
	//	 * Set  the value of ProcInvINV014
	//	 *
	//	 * @param aaProcInvINV014 ProcessInventoryData
	//	 */
	//	public void setProcInvINV014(ProcessInventoryData aaProcInvINV014)
	//	{
	//		caProcInvINV014 = aaProcInvINV014;
	//	}
	/**
	 * Set  the value of ProcInvPlt
	 * 
	 * @param aaProcInvPlt ProcessInventoryData
	 */
	public void setProcInvPlt(ProcessInventoryData aaProcInvPlt)
	{
		caProcInvPlt = aaProcInvPlt;
	}
	/**
	 * Set  the value of RecordType
	 * 
	 * @param aRecordType int
	 */
	public void setRecordType(int aRecordType)
	{
		ciRecordType = aRecordType;
	}
	/**
	 * Called be parser to set the data corresponding to the field number.
	 * 
	 * @param aiFieldNum int
	 * @param asValue String
	 * @param aiRecType	int
	 */
	public void setRecType(int aiFieldNum)
	{
	}
	/**
	 * Set  the value of RegClassCd
	 * 
	 * @param aRegClassCd String
	 */
	public void setRegClassCd(String aRegClassCd)
	{
		csRegClassCd = aRegClassCd;
	}
	/**
	 * Set  the value of RegExpMo 
	 * 
	 * @param aiRegExpMo int
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set value of ciRegNextExpMo
	 * 
	 * @param aiRegNextExpMo
	 */
	public void setRegNextExpMo(int aiRegNextExpMo)
	{
		ciRegNextExpMo = aiRegNextExpMo;
	}

	/**
	 * Set value of ciRegNextExpYr
	 * 
	 * @param aiRegNextExpYr
	 */
	public void setRegNextExpYr(int aiRegNextExpYr)
	{
		ciRegNextExpYr = aiRegNextExpYr;
	}
	/**
	 * Set  the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}
	/**
	 * Set  the value of RenewalBarCodeData
	 * 
	 * @param aaRenewalBarCodeData RenewalBarCodeData
	 */
	public void setRenewalBarCodeData(RenewalBarCodeData aaRenewalBarCodeData)
	{
		caRenewalBarCodeData = aaRenewalBarCodeData;
	}
	/**
	 * Set  the value of RenwlTotalFees
	 * 
	 * @param aaRenwlTotalFees Dollar
	
	 */
	public void setRenwlTotalFees(Dollar aaRenwlTotalFees)
	{
		caRenwlTotalFees = aaRenwlTotalFees;
	}

	/**
	 * Set  the value of RSPSId
	 * 
	 * @param asRSPSId int
	 */
	public void setRSPSId(String asRSPSId)
	{
		csRSPSId = asRSPSId;
	}

	/**
	 * Set value of ciSpclPltIndi 
	 * 
	 * @param aiSpclPltIndi
	 */
	public void setSpclPltIndi(int aiSpclPltIndi)
	{
		ciSpclPltIndi = aiSpclPltIndi;
	}

	/**
	 * Set value of csSpclPltRegPltCd
	 * 
	 * @param asSpclPltRegPltCd
	 */
	public void setSpclPltRegPltCd(String asSpclPltRegPltCd)
	{
		csSpclPltRegPltCd = asSpclPltRegPltCd;
	}
	/**
	 * Set  the value of StkrDesc
	 * 
	 * @param asStkrDesc String
	 */
	public void setStkrDesc(String asStkrDesc)
	{
		csStkrDesc = asStkrDesc;
	}
	/**
	 * Set  the value of StkrItmCd
	 * 
	 * @param asStkrItmCd String
	 */
	public void setStkrItmCd(String asStkrItmCd)
	{
		csStkrItmCd = asStkrItmCd;
	}
	/**
	 * Set  the value of SubconId
	 * 
	 * @param aiSubconId int
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}
	/**
	 * Set  the value of SubconIssueDate
	 * 
	 * @param aiSubconIssueDate int
	 */
	public void setSubconIssueDate(int aiSubconIssueDate)
	{
		ciSubconIssueDate = aiSubconIssueDate;
	}
	/**
	 * Set  the value of SubconProcessDateTime
	 * 
	 * @param aaSubconProcessDateTime RTSDate
	 */
	public void setSubconProcessDateTime(RTSDate aaSubconProcessDateTime)
	{
		caSubconProcessDateTime = aaSubconProcessDateTime;
	}
	/**
	 * Set  the value of TransactionKey
	 * 
	 * @param aaTransactionKey TransactionKey
	 */
	public void setTransactionKey(TransactionKey aaTransactionKey)
	{
		caTransactionKey = aaTransactionKey;
	}
	/**
	 * Set  the value of TransID
	 * 	
	 * @param asTransID String
	 */
	public void setTransID(String asTransID)
	{
		csTransID = asTransID;
	}
	/**
	 * Set  the value of TransKeyNumber
	 * 
	 * @param aaTransKeyNumber Integer
	 */
	public void setTransKeyNumber(Integer aaTransKeyNumber)
	{
		caTransKeyNumber = aaTransKeyNumber;
	}
	/**
	 * Set  the value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * Set  the value of TransType
	 * 
	 * @param aiTransType int
	 */
	public void setTransType(int aiTransType)
	{
		ciTransType = aiTransType;
	}

	/**
	 * 
	 * Setup as Special Plate
	 * 
	 */
	private void setupAsSpclPlt()
	{
		String lsRegPltCd =
			csPltItmCd != null ? csPltItmCd : csOldPltItmCd;

		// defect 10303
		//if (lsRegPltCd != null
		//	&& ciBarCdVersion
		//		== Integer.parseInt(BarCodeScanner.BARCODE_VERSION05)
		//	&& PlateTypeCache.isSpclPlate(lsRegPltCd)
		//	&& csNewPltsReqdCd != null
		//	&& !csNewPltsReqdCd.equals(
		//		SpecialPlatesConstant.SPCLPLT_FEES_NOT_INCLUDED))
		if (lsRegPltCd != null
			&& PlateTypeCache.isSpclPlate(lsRegPltCd))
			// end defect 10303
		{
			ciSpclPltIndi = 1;
			csSpclPltRegPltCd = lsRegPltCd;
		}
	}
	/**
	 * Determine if the fee is eligible for registration total
	 * 
	 * @param asAcctItmCd String
	 * @return boolean
	 */
	public boolean isEligibleForRegFee(String asAcctItmCd){
		if (asAcctItmCd.equalsIgnoreCase(AcctCdConstant.PARKS_FUND_CODE) ||
			asAcctItmCd.equalsIgnoreCase(AcctCdConstant.VET_FUND_CODE) ||
			asAcctItmCd.equalsIgnoreCase(AcctCdConstant.MAIL_CODE)) 
		{
			return false;
		}
		return true;
	}
	/**
	 * Set  the value of ValidatePltIndi
	 * 
	 * @param aiValidatePltIndi int
	 */
	public void setValidatePltIndi(int aiValidatePltIndi)
	{
		ciValidatePltIndi = aiValidatePltIndi;
	}

	/**
	 * Set value of ciVehGrossWt
	 * 
	 * @param aiVehGrossWt
	 */
	public void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}

	/**
	 * Set value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set value of csVehModl
	 * 
	 * @param asVehModl
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}
	/**
	 * Set  the value of VIN
	 *
	 * @param asVIN String
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * Get ciPltBirthDate
	 * 
	 * @return int
	 */
	public int getPltBirthDate()
	{
		return ciPltBirthDate;
	}

	/**
	 * Set value of ciPltBirthDate
	 * 
	 * @param aiPltBirthDate
	 */
	public void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}

}
