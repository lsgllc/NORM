package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * SubcontractorHdrData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add import statements
 * 							add cExportOnDate, cLastExportOnDate,
 *							ciDiskSeqNo, csProcsId and associated
 * 							get/set methods 
 * 							Modify variable names to meet standards
 * 							Ver 5.2.0
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3			  
 * ---------------------------------------------------------------------
 */
/**
 * Subcontractor header data contains key information associated
 * with the list of Subcontractor Renewal data.  It is used 
 * as part of SubcontractorRenewalCacheData.
 * 
 * @version 5.2.3		04/21/2005 
 * @author 	Nancy Ting
 * <br>Creation Date:	10/18/2001
 */
public class SubcontractorHdrData implements java.io.Serializable
{
	// int 
	private int ciCustSeqNo;
	private int ciDiskSeqNo;
	private int ciOfcIssuanceNo;
	private int ciSubconId;
	private int ciSubconIssueDate;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransWsId;

	// String 
	private String csTransEmpId;
	private String csProcsId;

	// Object 
	private Dollar caPymntTotalFee;
	private RTSDate caExportOnDate;
	private RTSDate caLastExportOnDate;

	private final static long serialVersionUID = -3494568440409545773L;
	/*
	 * SubcontractorHdrData constructor comment.
	 */
	public SubcontractorHdrData()
	{
		super();
	}
	/**
	 * Return value of CustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	/**
	 * Return value of DiskSeqNo
	 * 
	 * @return int
	 */
	public int getDiskSeqNo()
	{
		return ciDiskSeqNo;
	}
	/**
	 * Return value of ExportOnDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getExportOnDate()
	{
		return caExportOnDate;
	}
	/**
	 * Return value of LastExportOnDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getLastExportOnDate()
	{
		return caLastExportOnDate;
	}
	/**
	 * Return value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return value of ProcsId
	 * 
	 * @return String
	 */
	public String getProcsId()
	{
		return csProcsId;
	}
	/**
	 * Return value of PymntTotalFee
	 * 
	 * @return Dollar
	 */
	public Dollar getPymntTotalFee()
	{
		return caPymntTotalFee;
	}
	/**
	 * Return value of SubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}
	/**
	 * Return value of SubconIssueDate
	 * 
	 * @return int
	 */
	public int getSubconIssueDate()
	{
		return ciSubconIssueDate;
	}
	/**
	 * Return value of SubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
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
	 * Return value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Return value of TransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}
	/**
	 * Set value of CustSeqNo
	 * 
	 * @param aiCustSeqNo int
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	/**
	 * Set value of DiskSeqNo
	 * 
	 * @param aiDiskSeqNo int
	 */
	public void setDiskSeqNo(int aiDiskSeqNo)
	{
		ciDiskSeqNo = aiDiskSeqNo;
	}
	/**
	 * Set value of ExportOnDate
	 * 
	 * @param aaExportOn RTSDate
	 */
	public void setExportOnDate(RTSDate aaExportOnDate)
	{
		caExportOnDate = aaExportOnDate;
	}
	/**
	 * Set value of LastExportOnDate
	 * 
	 * @param aaLastExportOn RTSDate
	 */
	public void setLastExportOnDate(RTSDate aaLastExportOnDate)
	{
		caLastExportOnDate = aaLastExportOnDate;
	}
	/**
	 * Set value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set value of ProcsId
	 * 
	 * @param asProcsId int
	 */
	public void setProcsId(String asProcsId)
	{
		csProcsId = asProcsId;
	}
	/**
	 * Set value of PymntTotalFee
	 * 
	 * @param aaPymntTotalFee Dollar
	 */
	public void setPymntTotalFee(Dollar aaPymntTotalFee)
	{
		caPymntTotalFee = aaPymntTotalFee;
	}
	/**
	 * Set value of SubconId
	 * 
	 * @param aiSubconId int
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}
	/**
	 * Set value of SubconIssueDate
	 * 
	 * @param aiSubconIssueDate int
	 */
	public void setSubconIssueDate(int aiSubconIssueDate)
	{
		ciSubconIssueDate = aiSubconIssueDate;
	}
	/**
	 * Set value of SubstaId
	 * 
	 * @param aiSubstaId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
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
	 * Set value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * Set value of TransWsId
	 * 
	 * @param aiTransWsId int
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
}
