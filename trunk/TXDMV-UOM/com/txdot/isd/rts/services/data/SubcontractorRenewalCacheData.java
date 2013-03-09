package com.txdot.isd.rts.services.data;

import java.lang.reflect.Field;
import java.util.*;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * SubcontractorRenewalCacheData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		05/06/2002	Added clientTranstime so that transtime can 
 * 							be generated on the client side always
 * 							defect 3773 
 * MAbs			05/08/2002	so class be viewed in ShowCache
 * 							defect 3776 
 * K Harrell	03/21/2004	5.2.0 Merge.  Complete replacement 
 * 							Ver 5.2.0
 * K Harrell	10/10/2004	Subcon Cleanup Work
 *							deprecated addIssuedInventory(),
 *							getClientTranstime(),
 *							getHeldInvStkr(),
 *							getINV014CurrentInvType(),
 *							getLeaveListBtnDisplay(),
 *							getSubconIssuedInv(),
 *							isListBox(),isMoreTrans(),
 *							removeIssuedInventory()
 *							setClientTranstime(),
 *							setHeldInvStkr(),
 *							setINV014CurrentInvType(),
 *							setLeaveListBtnDisplay(),
 *							setListBox(),
 *							setMoreTrans()
 *							setSubconIssuedInv(),
 *							defect 7609 Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 * 							deleted above deprecated methods
 * 							defect 7899 Ver 5.2.3
 * K Harrell	05/02/2005	INV014 renamed to INV003 
 * 							rename associated variables, methods. 
 * 							defect 6966 Ver 5.2.3  
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * This data object stores pending subcontractor renewal 
 * transactions for later retrieval.  This object will be 
 * used for writing and reading to/from a local disk file.  
 * It is also used for displaying the list of transactions 
 * being processed.
 * 
 * 
 * @version	5.2.3			06/19/2005 
 * @author	Nancy Ting 
 * <br>Creation Date:		10/18/2001
 */
public class SubcontractorRenewalCacheData
	implements java.io.Serializable, Displayable
{
	// boolean
	private boolean cbAllTransPosted;
	private boolean cbINV003Voided;
	private boolean cbCompleteOneTrans;
	private boolean cbControlVisible;
	private boolean cbModified;
	private boolean cbPrintPrelimReport;
	private boolean cbUseBarCode;

	// int
	private int ciBarCodeIndi;
	private int ciExceptionField;
	private int ciNextVC; // VC Switch
	private int ciTransAMDate; // SendCache
	private int ciTransTime; // SendCache

	// Object
	private Dollar caRunningTotal = new Dollar("0.00");
	private HashSet chsInventoryCheckList = new HashSet();
	private HashSet chsInvValIndex = new HashSet();
	private HashSet chsUnProcsList = new HashSet();
	private Hashtable chtDiskHeldPltList = new Hashtable();
	private Hashtable chtIssuedInventories = new Hashtable();
	private Hashtable chtPrintedInventories = new Hashtable();
	private Hashtable chtReprintStickerReportDataList;
	private Hashtable chtTransCurrPltNo = new Hashtable();
	private Hashtable chtTransDocNo = new Hashtable();
	private Hashtable chtTransNewPltNo = new Hashtable();
	private Hashtable chtTransVIN = new Hashtable();
	private Integer caCurrTransIndex = new Integer(0);
	private Integer caRecordModifyIndex;
	private ProcessInventoryData caHeldInvPlt;
	private ProcessInventoryData caINV003ProcessInventoryData;
	private RenewalBarCodeData caTempRenewalBarCodeData;
	private RTSException ceException;
	private Set caDeleteIndex;
	private Set caErrorIndices;
	private SortedMap csmSubconTransData =
		new TreeMap(new SubcontractorSorting());
	private SubcontractorData caSubconInfo;
	private SubcontractorHdrData caSubcontractorHdrData;
	private SubcontractorRenewalData caRecordModified;
	private SubcontractorRenewalData caRecordTobeModified;
	private SubcontractorRenewalData caTempSubconRenewalData;
	private TransactionHeaderData caTransactionHeaderData;

	// String 
	private String csDisplaySubconInfo; // Used on REG007 
	private String csINV003AllocatedName;
	private String csInvalidRecordsMsg;
	private String csRcptDir;

	// Vector
	private Vector cvDeleteTransKeyList; //stores the transkey
	private Vector cvReleaseInventoryList;
	private Vector cvSubconAllocatedInventory; // for REG006
	private Vector cvSubconDiskData; // vector of subcon data from disk

	// Constants 
	public static final int STKR = 0;
	public static final int PLT = 1;
	public static final int PLT_STKR = 2;

	public static final int FD_EXP_YR = 1;
	public static final int FD_ISSUE_DT = 2;
	public static final int FD_NEW_PLT_NO = 3;
	public static final int FD_DOC_NO = 4;
	public static final int FD_VIN = 5;
	public static final int FD_CURR_PLT_NO = 6;
	public static final int FD_EXP_MO = 7;
	public static final int FD_REG_CLASS = 8;
	public static final int FD_FEE = 9;

	private final static long serialVersionUID = 3042166784314684875L;

	/**
	 * SubcontractorRenewalCacheData constructor comment.
	 */
	public SubcontractorRenewalCacheData()
	{
		super();
	}

	/**
	 * Returns a Map of the internal attributes.  
	 * Implementers of this method should use introspection 
	 * to display their internal variables and values
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHashMap = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
	}
	/**
	 * Return value of BarCodeIndi
	 * 
	 * @return int
	 */
	public int getBarCodeIndi()
	{
		return ciBarCodeIndi;
	}

	/**
	 * Return value of CurrTransIndex
	 * 
	 * @return Integer
	 */
	public Integer getCurrTransIndex()
	{
		return caCurrTransIndex;
	}
	/**
	 * Return value of DeleteIndex
	 * 
	 * @return Set 
	 */
	public Set getDeleteIndex()
	{
		return caDeleteIndex;
	}
	/**
	 * Return value of DeleteTransKeyList
	 * 
	 * @return Vector
	 */
	public Vector getDeleteTransKeyList()
	{
		return cvDeleteTransKeyList;
	}
	/**
	 * Return value of DiskHeldPltList
	 * 
	 * @return Hashtable
	 */
	public Hashtable getDiskHeldPltList()
	{
		return chtDiskHeldPltList;
	}
	/**
	 * Return value of DisplaySubconInfo
	 * 
	 * @return String
	 */
	public String getDisplaySubconInfo()
	{
		return csDisplaySubconInfo;
	}
	/**
	 * Return value of ErrorIndices
	 * 
	 * @return Set
	 */
	public Set getErrorIndices()
	{
		return caErrorIndices;
	}
	/**
	 * Return value of Exception
	 * 
	 * @return RTSException
	 */
	public RTSException getException()
	{
		return ceException;
	}
	/**
	 * Return value of ExceptionField
	 * 
	 * @return int
	 */
	public int getExceptionField()
	{
		return ciExceptionField;
	}
	/**
	 * Return value of HeldInvPlt
	 * 
	 * @return ProcessInventoryData
	 */
	public ProcessInventoryData getHeldInvPlt()
	{
		return caHeldInvPlt;
	}

	/**
	 * Return value of INV003AllocatedName
	 * 
	 * @return String
	 */
	public String getINV003AllocatedName()
	{
		return csINV003AllocatedName;
	}

	/**
	 * Return value of ProcessInventoryData
	 * 
	 * @return ProcessInventoryData
	 */
	public ProcessInventoryData getINV003ProcessInventoryData()
	{
		return caINV003ProcessInventoryData;
	}
	/**
	 * Return value of InvalidRecordsMsg
	 * 
	 * @return String
	 */
	public String getInvalidRecordsMsg()
	{
		return csInvalidRecordsMsg;
	}
	/**
	 * Return value of InventoryCheckList
	 * 
	 * @return HashSet
	 */
	public HashSet getInventoryCheckList()
	{
		return chsInventoryCheckList;
	}
	/**
	 * Return value of InvValIndex
	 * 
	 * @return HashSet
	 */
	public HashSet getInvValIndex()
	{
		return chsInvValIndex;
	}
	/**
	 * Return value of IssuedInventories
	 * 
	 * @return HashSet
	 */
	public Hashtable getIssuedInventories()
	{
		return chtIssuedInventories;
	}

	/**
	 * Return value of NextVC
	 * 
	 * @return int
	 */
	public int getNextVC()
	{
		return ciNextVC;
	}
	/**
	 * Return value of PrintedInventories
	 * 
	 * @return HashSet
	 */
	public Hashtable getPrintedInventories()
	{
		return chtPrintedInventories;
	}
	/**
	 * Return value of RcptDir
	 * 
	 * @return String
	 */
	public String getRcptDir()
	{
		return csRcptDir;
	}
	/**
	 * Return value of RecordModified
	 * 
	 * @return SubcontractorRenewalData
	 */
	public SubcontractorRenewalData getRecordModified()
	{
		return caRecordModified;
	}
	/**
	 * Return value of RecordModifyIndex
	 * 
	 * @return Integer
	 */
	public Integer getRecordModifyIndex()
	{
		return caRecordModifyIndex;
	}
	/**
	 * Return value of RecordTobeModified
	 * 
	 * @return SubcontractorRenewalData
	 */
	public SubcontractorRenewalData getRecordTobeModified()
	{
		return caRecordTobeModified;
	}
	/**
	 * Return value of ReleaseInventoryList
	 * 
	 * @return Vector
	 */
	public Vector getReleaseInventoryList()
	{
		return cvReleaseInventoryList;
	}
	/**
	 * Return value of ReprintStickerReportDataList
	 * 
	 * @return Hashtable
	 */
	public Hashtable getReprintStickerReportDataList()
	{
		return chtReprintStickerReportDataList;
	}
	/**
	 * Return value of RunningTotal
	 * 
	 * @return Dollar
	 */
	public Dollar getRunningTotal()
	{
		return caRunningTotal;
	}
	/**
	 * Return value of SubconAllocatedInventory
	 * 
	 * @return Vector
	 */
	public Vector getSubconAllocatedInventory()
	{
		return cvSubconAllocatedInventory;
	}
	/**
	 * Return value of SubconDiskData
	 * 
	 * @return Vector
	 */
	public Vector getSubconDiskData()
	{
		return cvSubconDiskData;
	}
	/**
	 * Return value of SubconInfo
	 * 
	 * @return SubcontractorData
	 */
	public SubcontractorData getSubconInfo()
	{
		return caSubconInfo;
	}

	/**
	 * Return value of SubcontractorHdrData
	 * 
	 * @return SubcontractorHdrData
	 */
	public SubcontractorHdrData getSubcontractorHdrData()
	{
		return caSubcontractorHdrData;
	}
	/**
	 * Return value of mSubconTransData
	 * 
	 * @return SortedMap
	 */
	public SortedMap getSubconTransData()
	{
		return csmSubconTransData;
	}
	/**
	 * Return value of TempRenewalBarCodeData
	 * 
	 * @return RenewalBarCodeData
	 */
	public RenewalBarCodeData getTempRenewalBarCodeData()
	{
		return caTempRenewalBarCodeData;
	}
	/**
	 * Return value of TempSubconRenewalData
	 * 
	 * @return SubcontractorRenewalData
	 */
	public SubcontractorRenewalData getTempSubconRenewalData()
	{
		return caTempSubconRenewalData;
	}
	/**
	 * Return value of TransactionHeaderData
	 * 
	 * @return TransactionHeaderData
	 */
	public TransactionHeaderData getTransactionHeaderData()
	{
		return caTransactionHeaderData;
	}
	/**
	 * Return value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}
	/**
	 * Return value of TransCurrPltNo
	 * 
	 * @return HashSet
	 */
	public Hashtable getTransCurrPltNo()
	{
		return chtTransCurrPltNo;
	}
	/**
	 * Return value of TransDocNo
	 * 
	 * @return HashSet
	 */
	public Hashtable getTransDocNo()
	{
		return chtTransDocNo;
	}
	/**
	 * Return value of TransNewPltNo
	 * 
	 * @return HashSet
	 */
	public Hashtable getTransNewPltNo()
	{
		return chtTransNewPltNo;
	}
	/**
	 * Return value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Return value of TransVIN
	 * 
	 * @return HashSet
	 */
	public Hashtable getTransVIN()
	{
		return chtTransVIN;
	}
	/**
	 * Return value of UnProcsList
	 * 
	 * @return HashSet
	 */
	public HashSet getUnProcsList()
	{
		return chsUnProcsList;
	}
	/**
	 * Return value of 
	 * 
	 */
	public void incrementTransIndex()
	{
		caCurrTransIndex = new Integer(caCurrTransIndex.intValue() + 1);
	}
	/**
	 * Return value of AllTransPosted
	 * 
	 * @return boolean
	 */
	public boolean isAllTransPosted()
	{
		return cbAllTransPosted;
	}
	/**
	 * Return value of CompleteOneTrans
	 * 
	 * @return boolean
	 */
	public boolean isCompleteOneTrans()
	{
		return cbCompleteOneTrans;
	}
	/**
	 * Return value of ControlVisible
	 * 
	 * @return boolean
	 */
	public boolean isControlVisible()
	{
		return cbControlVisible;
	}
	/**
	 * Return value of INV003Voided
	 * 
	 * @return boolean
	 */
	public boolean isINV003Voided()
	{
		return cbINV003Voided;
	}

	/**
	 * Return value of Modified
	 * 
	 * @return boolean
	 */
	public boolean isModified()
	{
		return cbModified;
	}

	/**
	 * Return value of PrintPrelimReport
	 * 
	 * @return boolean
	 */
	public boolean isPrintPrelimReport()
	{
		return cbPrintPrelimReport;
	}
	/**
	 * Return value of UseBarCode
	 * 
	 * @return boolean
	 */
	public boolean isUseBarCode()
	{
		return cbUseBarCode;
	}

	/**
	 * Reset VC 
	 * 
	 */
	public void resetVC()
	{
		ciNextVC = 0;
	}
	/**
	 * Set value of AllTransPosted
	 * 
	 * @param abAllTransPosted boolean
	 */
	public void setAllTransPosted(boolean abAllTransPosted)
	{
		cbAllTransPosted = abAllTransPosted;
	}
	/**
	 * Set value of BarCodeIndi
	 * 
	 * @param aiBarCodeIndi int
	 */
	public void setBarCodeIndi(int aiBarCodeIndi)
	{
		ciBarCodeIndi = aiBarCodeIndi;
	}

	/**
	 * Set value of CompleteOneTrans
	 * 
	 * @param abCompleteOneTrans boolean
	 */
	public void setCompleteOneTrans(boolean abCompleteOneTrans)
	{
		cbCompleteOneTrans = abCompleteOneTrans;
	}
	/**
	 * Set value of ControlVisible
	 * 
	 * @param aControlVisible boolean
	 */
	public void setControlVisible(boolean abControlVisible)
	{
		cbControlVisible = abControlVisible;
	}
	/**
	 * Set value of CurrTransIndex
	 * 
	 * @param aaCurrTransIndex Integer
	 */
	public void setCurrTransIndex(Integer aaCurrTransIndex)
	{
		caCurrTransIndex = aaCurrTransIndex;
	}
	/**
	 * Set value of DeleteIndex
	 * 
	 * @param aaDeleteIndex int[]
	 */
	public void setDeleteIndex(Set aaDeleteIndex)
	{
		caDeleteIndex = aaDeleteIndex;
	}
	/**
	 * Set value of DeleteTransKeyList
	 * 
	 * @param avDeleteTransKeyList Vector
	 */
	public void setDeleteTransKeyList(Vector avDeleteTransKeyList)
	{
		cvDeleteTransKeyList = avDeleteTransKeyList;
	}
	/**
	 * Set value of DiskHeldPltList
	 * 
	 * @param ahtDiskHeldPltList Hashtable
	 */
	public void setDiskHeldPltList(Hashtable ahtDiskHeldPltList)
	{
		chtDiskHeldPltList = ahtDiskHeldPltList;
	}
	/**
	 * Set value of DisplaySubconInfo
	 * 
	 * @param asDisplaySubconInfo String
	 */
	public void setDisplaySubconInfo(String asDisplaySubconInfo)
	{
		csDisplaySubconInfo = asDisplaySubconInfo;
	}
	/**
	 * Set value of ErrorIndices
	 * 
	 * @param aaErrorIndices Set
	 */
	public void setErrorIndices(Set aaErrorIndices)
	{
		caErrorIndices = aaErrorIndices;
	}
	/**
	 * Set value of Exception
	 * 
	 * @param aeException RTSException
	 */
	public void setException(RTSException aeException)
	{
		ceException = aeException;
	}
	/**
	 * Set value of ExceptionField
	 * 
	 * @param aiExceptionField int
	 */
	public void setExceptionField(int aiExceptionField)
	{
		ciExceptionField = aiExceptionField;
	}
	/**
	 * Set value of HeldInvPlt
	 * 
	 * @param aaHeldInvPlt ProcessInventoryData
	 */
	public void setHeldInvPlt(ProcessInventoryData aaHeldInvPlt)
	{
		caHeldInvPlt = aaHeldInvPlt;
	}

	/**
	 * Set value of INV003AllocatedName
	 * 
	 * @param asINV003AllocatedName String
	 */
	public void setINV003AllocatedName(String asINV003AllocatedName)
	{
		csINV003AllocatedName = asINV003AllocatedName;
	}

	/**
	 * Set value of INV003ProcessInventoryData
	 * 
	 * @param aaINV003ProcessInventoryData ProcessInventoryData
	 */
	public void setINV003ProcessInventoryData(ProcessInventoryData aaINV003ProcessInventoryData)
	{
		caINV003ProcessInventoryData = aaINV003ProcessInventoryData;
	}
	/**
	 * Set value of INV003Voided
	 * 
	 * @param abINV003Voided boolean
	 */
	public void setINV003Voided(boolean abINV003Voided)
	{
		cbINV003Voided = abINV003Voided;
	}
	/**
	 * Set value of InvalidRecordsMsg
	 * 
	 * @param asInvalidRecordsMsg String
	 */
	public void setInvalidRecordsMsg(String asInvalidRecordsMsg)
	{
		csInvalidRecordsMsg = asInvalidRecordsMsg;
	}
	/**
	 * Set value of InventoryCheckList
	 * 
	 * @param ahsInventoryCheckList HashSet
	 */
	public void setInventoryCheckList(HashSet ahsInventoryCheckList)
	{
		chsInventoryCheckList = ahsInventoryCheckList;
	}
	/**
	 * Set value of InvValIndex
	 * 
	 * @param ahsInvValIndex HashSet
	 */
	public void setInvValIndex(HashSet ahsInvValIndex)
	{
		chsInvValIndex = ahsInvValIndex;
	}
	/**
	 * Set value of IssuedInventories
	 * 
	 * @param ahtIssuedInventories HashSet
	 */
	public void setIssuedInventories(Hashtable ahtIssuedInventories)
	{
		chtIssuedInventories = ahtIssuedInventories;
	}

	/**
	 * Set value of Modified
	 * 
	 * @param abModified boolean
	 */
	public void setModified(boolean abModified)
	{
		cbModified = abModified;
	}

	/**
	 * Set value of NextVC
	 * 
	 * @param aiNextVC int
	 */
	public void setNextVC(int aiNextVC)
	{
		ciNextVC = aiNextVC;
	}
	/**
	 * Set value of PrintedInventories
	 *  
	 * @param ahtPrintedInventories HashSet
	 */
	public void setPrintedInventories(Hashtable ahtPrintedInventories)
	{
		chtPrintedInventories = ahtPrintedInventories;
	}
	/**
	 * Set value of PrintPrelimReport
	 * 
	 * @param abPrintPrelimReport boolean
	 */
	public void setPrintPrelimReport(boolean abPrintPrelimReport)
	{
		cbPrintPrelimReport = abPrintPrelimReport;
	}
	/**
	 * Set value of RcptDir
	 * 
	 * @param asRcptDir String
	 */
	public void setRcptDir(String asRcptDir)
	{
		csRcptDir = asRcptDir;
	}
	/**
	 * Set value of RecordModified
	 * 
	 * @param aaRecordModified SubcontractorRenewalData
	
	 */
	public void setRecordModified(SubcontractorRenewalData aaRecordModified)
	{
		caRecordModified = aaRecordModified;
	}
	/**
	 * Set value of RecordModifyIndex
	 * 
	 * @param aaRecordModifyIndex Integer
	 */
	public void setRecordModifyIndex(Integer aaRecordModifyIndex)
	{
		caRecordModifyIndex = aaRecordModifyIndex;
	}
	/**
	 * Set value of RecordTobeModified
	 * 
	 * @param aaRecordTobeModified SubcontractorRenewalData
	 */
	public void setRecordTobeModified(SubcontractorRenewalData aaRecordTobeModified)
	{
		caRecordTobeModified = aaRecordTobeModified;
	}
	/**
	 * Set value of ReleaseInventoryList
	 *  
	 * @param avReleaseInventoryList Vector
	 */
	public void setReleaseInventoryList(Vector avReleaseInventoryList)
	{
		cvReleaseInventoryList = avReleaseInventoryList;
	}
	/**
	 * Set value of ReprintStickerReportDataList
	 * 
	 * @param ahtReprintStickerReportDataList Hashtable
	 */
	public void setReprintStickerReportDataList(Hashtable ahtReprintStickerReportDataList)
	{
		chtReprintStickerReportDataList =
			ahtReprintStickerReportDataList;
	}
	/**
	 * Set value of RunningTotal
	 * 
	 * @param aaRunningTotal Dollar
	 */
	public void setRunningTotal(Dollar aaRunningTotal)
	{
		caRunningTotal = aaRunningTotal;
	}
	/**
	 * Set value of SubconAllocatedInventory
	 * 
	 * @param avSubconAllocatedInventory Vector
	 */
	public void setSubconAllocatedInventory(Vector avSubconAllocatedInventory)
	{
		cvSubconAllocatedInventory = avSubconAllocatedInventory;
	}
	/**
	 * Set value of SubconDiskData
	 * 
	 * @param avSubconDiskData Vector
	 */
	public void setSubconDiskData(Vector avSubconDiskData)
	{
		cvSubconDiskData = avSubconDiskData;
	}
	/**
	 * Set value of SubconInfo
	 * 
	 * @param aaSubconInfo SubcontractorData
	 */
	public void setSubconInfo(SubcontractorData aaSubconInfo)
	{
		caSubconInfo = aaSubconInfo;
	}

	/**
	 * Set value of SubcontractorHdrData
	 * 
	 * @param aaSubcontractorHdrData SubcontractorHdrData
	 */
	public void setSubcontractorHdrData(SubcontractorHdrData aaSubcontractorHdrData)
	{
		caSubcontractorHdrData = aaSubcontractorHdrData;
	}
	/**
	 * Set value of SubconTransData
	 * 
	 * @param asmSubconTransData SortedMap
	 */
	public void setSubconTransData(SortedMap asmSubconTransData)
	{
		csmSubconTransData = asmSubconTransData;
	}
	/**
	 * Set value of TempRenewalBarCodeData
	 * 
	 * @param aaTempRenewalBarCodeData RenewalBarCodeData
	 */
	public void setTempRenewalBarCodeData(RenewalBarCodeData aaTempRenewalBarCodeData)
	{
		caTempRenewalBarCodeData = aaTempRenewalBarCodeData;
	}
	/**
	 * Set value of TempSubconRenewalData
	 * 
	 * @param aaTempSubconRenewalData SubcontractorRenewalData
	 */
	public void setTempSubconRenewalData(SubcontractorRenewalData aaTempSubconRenewalData)
	{
		caTempSubconRenewalData = aaTempSubconRenewalData;
	}
	/**
	 * Set value of TransactionHeaderData
	 * 
	 * @param aaTransactionHeaderData TransactionHeaderData
	 */
	public void setTransactionHeaderData(TransactionHeaderData aaTransactionHeaderData)
	{
		caTransactionHeaderData = aaTransactionHeaderData;
	}
	/**
	 * Set value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * Set value of TransCurrPltNo
	 * 
	 * @param ahtTransCurrPltNo HashSet
	 */
	public void setTransCurrPltNo(Hashtable ahtTransCurrPltNo)
	{
		chtTransCurrPltNo = ahtTransCurrPltNo;
	}
	/**
	 * Set value of TransDocNo
	 * 
	 * @param ahtTransDocNo HashSet
	 */
	public void setTransDocNo(Hashtable ahtTransDocNo)
	{
		chtTransDocNo = ahtTransDocNo;
	}
	/**
	 * Set value of TransNewPltNo
	 * 
	 * @param ahtTransNewPltNo HashSet
	 */
	public void setTransNewPltNo(Hashtable ahtTransNewPltNo)
	{
		chtTransNewPltNo = ahtTransNewPltNo;
	}
	/**
	 * Set value of TransTime
	 * 
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * Set value of TransVIN
	 * 
	 * @param ahsTransVIN HashSet
	 */
	public void setTransVIN(Hashtable ahsTransVIN)
	{
		chtTransVIN = ahsTransVIN;
	}
	/**
	 * Set value of UnProcsList
	 * 
	 * @param ahsUnProcsList HashSet
	 */
	public void setUnProcsList(HashSet ahsUnProcsList)
	{
		chsUnProcsList = ahsUnProcsList;
	}
	/**
	 * Set value of UseBarCode
	 * 
	 * @param abUseBarCode boolean
	 */
	public void setUseBarCode(boolean abUseBarCode)
	{
		cbUseBarCode = abUseBarCode;
	}
}
