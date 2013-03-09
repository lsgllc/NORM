package com.txdot.isd.rts.services.data;import java.io.Serializable;/* * OwnershipEvidenceCodesData.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	06/19/2005	Java 1.4 Work * 							moved to services.data	 * 							defect 7899 Ver 5.2.3 * K Harrell	04/03/2008	add csTtlTrnsfrPnltyExmptCd, get/set methods * 							defect 9583 Ver 3 Defect POS A * K Harrell	05/21/2008	removed csTtlTrnsfrPnltyExmptCd, get/set  * 							methods; No longer required.  * 							defect 9583 Ver Defect POS A * K Harrell	06/20/2008	restored csTtlTrnsfrPnltyExmptCd,  * 							 get/set methods * 							defect 9724 Ver Defect POS A      			  * --------------------------------------------------------------------- *//** * This Data class contains attributes and get/set methods for  * OwnershipEvidenceCodesData *  * @version	Defect POS A		06/20/2008  * @author	Administrator * <br>Creation Date: 			09/05/2001  *//* &OwnershipEvidenceCodesData& */public class OwnershipEvidenceCodesData	implements Serializable, Comparable{	// int/* &OwnershipEvidenceCodesData'ciBndedTtlCdReqd& */	protected int ciBndedTtlCdReqd;/* &OwnershipEvidenceCodesData'ciEvidFreqSortNo& */	protected int ciEvidFreqSortNo;/* &OwnershipEvidenceCodesData'ciEvidImprtnceSortNo& */	protected int ciEvidImprtnceSortNo;/* &OwnershipEvidenceCodesData'ciOwnrshpEvidCd& */	protected int ciOwnrshpEvidCd;/* &OwnershipEvidenceCodesData'ciPriorCCOReqd& */	protected int ciPriorCCOReqd;	// String/* &OwnershipEvidenceCodesData'csEvidSurrCd& */	protected String csEvidSurrCd;/* &OwnershipEvidenceCodesData'csOwnrshpEvidCdDesc& */	protected String csOwnrshpEvidCdDesc;	// defect 9724 /* &OwnershipEvidenceCodesData'csTtlTrnsfrPnltyExmptCd& */	protected String csTtlTrnsfrPnltyExmptCd;	// end defect 9724 /* &OwnershipEvidenceCodesData'serialVersionUID& */	private final static long serialVersionUID = 2150934460897697206L;	/**	 * Compares this object with the specified object for order.  Returns a	 * negative integer, zero, or a positive integer as this object is less	 * than, equal to, or greater than the specified object.<p>	 *	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>	 *	 * The implementor must also ensure that the relation is transitive:	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies	 * <tt>x.compareTo(z)&gt;0</tt>.<p>	 *	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for	 * all <tt>z</tt>.<p>	 *	 * It is strongly recommended, but <i>not</i> strictly required that	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any	 * class that implements the <tt>Comparable</tt> interface and violates	 * this condition should clearly indicate this fact.  The recommended	 * language is "Note: this class has a natural ordering that is	 * inconsistent with equals."	 * 	 * @param   aaObject Object	 * @return  boolean a negative integer, zero, or a positive integer as this object	 *		is less than, equal to, or greater than the specified object.	 * 	 *//* &OwnershipEvidenceCodesData.compareTo& */	public int compareTo(Object aaObject)	{		OwnershipEvidenceCodesData laData =			(OwnershipEvidenceCodesData) aaObject;		if (getEvidImprtnceSortNo() < laData.getEvidImprtnceSortNo())		{			return -1;		}		else if (			getEvidImprtnceSortNo() > laData.getEvidImprtnceSortNo())		{			return 1;		}		else		{			return 0;		}	}	/**	 * Returns the value of BndedTtlCdReqd	 * 	 * @return  int 	 *//* &OwnershipEvidenceCodesData.getBndedTtlCdReqd& */	public final int getBndedTtlCdReqd()	{		return ciBndedTtlCdReqd;	}	/**	 * Returns the value of EvidFreqSortNo	 * 	 * @return  int 	 *//* &OwnershipEvidenceCodesData.getEvidFreqSortNo& */	public final int getEvidFreqSortNo()	{		return ciEvidFreqSortNo;	}	/**	 * Returns the value of EvidImprtnceSortNo	 * 	 * @return  int 	 *//* &OwnershipEvidenceCodesData.getEvidImprtnceSortNo& */	public final int getEvidImprtnceSortNo()	{		return ciEvidImprtnceSortNo;	}	/**	 * Returns the value of EvidSurrCd	 * 	 * @return  String 	 *//* &OwnershipEvidenceCodesData.getEvidSurrCd& */	public final String getEvidSurrCd()	{		return csEvidSurrCd;	}	/**	 * Returns the value of OwnrshpEvidCd	 * 	 * @return  int 	 *//* &OwnershipEvidenceCodesData.getOwnrshpEvidCd& */	public final int getOwnrshpEvidCd()	{		return ciOwnrshpEvidCd;	}	/**	 * Returns the value of OwnrshpEvidCdDesc	 * 	 * @return  String 	 *//* &OwnershipEvidenceCodesData.getOwnrshpEvidCdDesc& */	public final String getOwnrshpEvidCdDesc()	{		return csOwnrshpEvidCdDesc;	}	/**	 * Returns the value of PriorCCOReqd	 * 	 * @return  int 	 *//* &OwnershipEvidenceCodesData.getPriorCCOReqd& */	public final int getPriorCCOReqd()	{		return ciPriorCCOReqd;	}	/**	 * This method sets the value of BndedTtlCdReqd.	 * 	 * @param aiBndedTtlCdReqd   int 	 *//* &OwnershipEvidenceCodesData.setBndedTtlCdReqd& */	public final void setBndedTtlCdReqd(int aiBndedTtlCdReqd)	{		ciBndedTtlCdReqd = aiBndedTtlCdReqd;	}	/**	 * This method sets the value of EvidFreqSortNo.	 * 	 * @param aiEvidFreqSortNo   int 	 *//* &OwnershipEvidenceCodesData.setEvidFreqSortNo& */	public final void setEvidFreqSortNo(int aiEvidFreqSortNo)	{		ciEvidFreqSortNo = aiEvidFreqSortNo;	}	/**	 * This method sets the value of EvidImprtnceSortNo.	 * 	 * @param aiEvidImprtnceSortNo   int 	 *//* &OwnershipEvidenceCodesData.setEvidImprtnceSortNo& */	public final void setEvidImprtnceSortNo(int aiEvidImprtnceSortNo)	{		ciEvidImprtnceSortNo = aiEvidImprtnceSortNo;	}	/**	 * This method sets the value of EvidSurrCd.	 * 	 * @param asEvidSurrCd   String 	 *//* &OwnershipEvidenceCodesData.setEvidSurrCd& */	public final void setEvidSurrCd(String asEvidSurrCd)	{		csEvidSurrCd = asEvidSurrCd;	}	/**	 * This method sets the value of OwnrshpEvidCd.	 * 	 * @param aiOwnrshpEvidCd   int 	 *//* &OwnershipEvidenceCodesData.setOwnrshpEvidCd& */	public final void setOwnrshpEvidCd(int aiOwnrshpEvidCd)	{		ciOwnrshpEvidCd = aiOwnrshpEvidCd;	}	/**	 * This method sets the value of OwnrshpEvidCdDesc.	 * 	 * @param asOwnrshpEvidCdDesc   String 	 *//* &OwnershipEvidenceCodesData.setOwnrshpEvidCdDesc& */	public final void setOwnrshpEvidCdDesc(String asOwnrshpEvidCdDesc)	{		csOwnrshpEvidCdDesc = asOwnrshpEvidCdDesc;	}	/**	 * This method sets the value of PriorCCOReqd.	 * 	 * @param aiPriorCCOReqd   int 	 *//* &OwnershipEvidenceCodesData.setPriorCCOReqd& */	public final void setPriorCCOReqd(int aiPriorCCOReqd)	{		ciPriorCCOReqd = aiPriorCCOReqd;	}	/**	 * Return value of TtlTrnsfrPnltyExmptCd	 * 	 * @return String	 *//* &OwnershipEvidenceCodesData.getTtlTrnsfrPnltyExmptCd& */	public String getTtlTrnsfrPnltyExmptCd()	{		return csTtlTrnsfrPnltyExmptCd;	}	/**	 * Set value of TtlTrnsfrPnltyExmptCd	 * 	 * @param asTtlTrnsfrPnltyExmptCd	 *//* &OwnershipEvidenceCodesData.setTtlTrnsfrPnltyExmptCd& */	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)	{		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;	}}/* #OwnershipEvidenceCodesData# */