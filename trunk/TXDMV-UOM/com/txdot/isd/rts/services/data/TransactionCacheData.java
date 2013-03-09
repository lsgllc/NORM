package com.txdot.isd.rts.services.data;

import java.util.HashMap;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * TransactionCacheData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/22/2002	defect 3597
 * N Ting		05/28/2002	defect 4025
 * Ray Rowehl	08/21/2002 	add handling for InternetTransData
 *							defect 3700
 * Min Wang		11/11/2002	Add VOID Constant to direct to Void.
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add import statement
 *							add creationDate
 * 							Ver 5.2.0
 * K Harrell	07/02/2004	Modify object type for ReprintStickerTransData
 *							modify getDateTime()
 *							defect 7284  Ver 5.2.0
 * K Harrell	10/04/2004  Assessment of Date/Time for Trans objects
 *							modify getDateTime()
 *							defect 7586  Ver 5.2.1
 * K Harrell	10/12/2004	Assessment of Date/Time for 
 *							ReprintStickerTransData
 *							modify getDateTime()
 *							defect 7460  Ver 5.2.1
 * K Harrell	10/26/2004	Set millisecond on creationDate to 0
 *							so that consistent with all other objects.
 *							modify TransactionCacheData() (constructor)
 *							defect 7651  Ver 5.2.2
 * K Harrell	06/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/29/2005	InternetTransData renamed to 
 * 							InternetTransactionData and moved to 
 * 							isd.rts.services.data
 * 							Ver 5.2.3 
 * K Harrell	10/07/2006	add ExemptAuditData processing 
 * 							modify getDateTime()
 * 							defect 8900 Ver 5.3.0 
 * K Harrell	02/25/2007	add Special Plates Processing
 * 							modify getDateTime()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/05/2007  add UPDVITRANSTIME	
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Add CacheTransAMDate,CacheTransTime 
 * 							processing for InventoryAllocationData
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/27/2008	Add CacheTransAMDate,CacheTransTime 
 * 							processing for DisabledPlacardTransactionData
 * 							modify getDateTime() 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/01/2008	Add CacheTransAMDate, CacheTransTime 
 * 							processing for DisabledPlacardCustomerData 
 * 							modify getDateTime() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	03/12/2009	add processing for ETitle 
 * 							modify getDateTime() 
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	03/29/2010	add processing for WorkstationData
 * 							modify getDateTime() 
 * 							defect 8087 Ver POS_640  
 * K Harrell	05/25/2010	add processing for PermitTransaction
 * 							modify getDateTime()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	12/26/2010	modify getDateTime()
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	05/29/2011	modify getDateTime()
 * 							defect 10865 Ver 6.8.0 
 * K Harrell	05/31/2011	modify getDateTime() 
 * 							defect 10844 Ver 6.8.0
 * K Harrell	06/29/2012	modify getDateTime() 
 * 							defect 11073 Ver 7.0.0 
 * ---------------------------------------------------------------------
 */
/**
 * This is the data class for storing the transactions
 * 
 * @version	  7.0.0			06/29/2012
 * @author	Nancy Ting
 * <br>Creation Date:		09/26/2001 
 */
public class TransactionCacheData
	implements java.io.Serializable, Comparable
{
	// boolean 
	private boolean cbFromSendCache;

	// int	
	protected int ciProcName;

	// Object
	protected Object caObj;
	private RTSDate caCreationDate;

	// Constants 
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	public static final int VOID = 4;
	// defect 9085
	public static final int UPDVITRANSTIME = 5;
	// end defect 9085 
	// defect 9831 
	public static final int RESETINPROCESS = 6;
	// end defect 9831

	private final static long serialVersionUID = -461335585287519128L;

	/**
	 * TransactionCacheData constructor comment.
	 */
	public TransactionCacheData()
	{
		super();
		caCreationDate = RTSDate.getCurrentDate();
		caCreationDate.setMillisecond(0);
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
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		TransactionCacheData laTransCacheData =
			(TransactionCacheData) aaObject;
		return (
			-1
				* getDateTime().compareTo(
					laTransCacheData.getDateTime()));
	}

	/**
	 * Get DateTime of the transaction
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDateTime()
	{
		if (caObj == null)
		{
			return null;
		}
		RTSDate laDate = null;

		if (caObj instanceof MotorVehicleFunctionTransactionData)
		{
			MotorVehicleFunctionTransactionData laData =
				(MotorVehicleFunctionTransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof TransactionHeaderData)
		{
			TransactionHeaderData laData =
				(TransactionHeaderData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// defect 10491 
		else if (caObj instanceof PermitTransactionData)
		{
			PermitTransactionData laData =
				(PermitTransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 10491 

		// defect 10865 
		else if (caObj instanceof FraudLogData)
		{
			FraudLogData laData = (FraudLogData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 10865 
		
		//defect 10844 
		else if (caObj instanceof ModifyPermitTransactionHistoryData)
		{
			ModifyPermitTransactionHistoryData laData =
				(ModifyPermitTransactionHistoryData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 10844 

		// defect 10700  
		else if (caObj instanceof SpecialPlatePermitData)
		{
			SpecialPlatePermitData laData =
				(SpecialPlatePermitData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 10700  
		else if (caObj instanceof TransactionData)
		{
			TransactionData laData = (TransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof TransactionPaymentData)
		{
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((TransactionPaymentData) caObj).getTransAMDate());
			laDate.setTime(
				((TransactionPaymentData) caObj).getTransTime());
			return laDate;
		}
		else if (caObj instanceof TransactionFundsDetailData)
		{
			TransactionFundsDetailData laData =
				(TransactionFundsDetailData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof TransactionInventoryDetailData)
		{
			TransactionInventoryDetailData laData =
				(TransactionInventoryDetailData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof FundFunctionTransactionData)
		{
			FundFunctionTransactionData laData =
				(FundFunctionTransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof InventoryFunctionTransactionData)
		{
			InventoryFunctionTransactionData laData =
				(InventoryFunctionTransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// defect 8900
		else if (caObj instanceof ExemptAuditData)
		{
			ExemptAuditData laData = (ExemptAuditData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 8900

		// defect 9085
		else if (
			caObj
				instanceof SpecialRegistrationFunctionTransactionData)
		{
			SpecialRegistrationFunctionTransactionData laData =
				(SpecialRegistrationFunctionTransactionData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof SpecialPlateTransactionHistoryData)
		{
			SpecialPlateTransactionHistoryData laData =
				(SpecialPlateTransactionHistoryData) caObj;
			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 9085

		// defect 9972
		else if (caObj instanceof ElectronicTitleHistoryData)
		{
			ElectronicTitleHistoryData laData =
				(ElectronicTitleHistoryData) caObj;

			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());

				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		// end defect 9972

		// defect 9831 
		else if (caObj instanceof DisabledPlacardTransactionData)
		{
			DisabledPlacardTransactionData laData =
				(DisabledPlacardTransactionData) caObj;

			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());

				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAMDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;
		}
		else if (caObj instanceof DisabledPlacardCustomerData)
		{
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((DisabledPlacardCustomerData) caObj)
						.getTransAMDate());
			laDate.setTime(
				((DisabledPlacardCustomerData) caObj).getTransTime());
			return laDate;
		}
		// end defect 9831 

		else if (caObj instanceof MFVehicleData)
		{
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((MFVehicleData) caObj).getTransAMDate());
			laDate.setTime(((MFVehicleData) caObj).getTransTime());
			return laDate;
		}
		else if (caObj instanceof AssignedWorkstationIdsData)
		{
			//for put assigned workstation
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((AssignedWorkstationIdsData) caObj).getAMDate());
			laDate.setTime(
				((AssignedWorkstationIdsData) caObj).getTranstime());
			return laDate;
		}
		else if (caObj instanceof CompleteTransactionData)
		{
			//for internet group
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((CompleteTransactionData) caObj).getTransAMDate());
			laDate.setTime(
				((CompleteTransactionData) caObj).getTransTime());
			return laDate;
		}
		else if (caObj instanceof InventoryAllocationData)
		{
			InventoryAllocationData laData =
				(InventoryAllocationData) caObj;

			if (laData.getCacheTransTime() != 0)
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getCacheTransAMDate());
				laDate.setTime(laData.getCacheTransTime());
			}
			else
			{
				laDate =
					new RTSDate(
						RTSDate.AMDATE,
						laData.getTransAmDate());
				laDate.setTime(laData.getTransTime());
			}
			return laDate;

		}
		// end defect 9085 
		else if (caObj instanceof SubcontractorRenewalCacheData)
		{ //for delete inventory
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((SubcontractorRenewalCacheData) caObj)
						.getTransAMDate());
			laDate.setTime(
				((SubcontractorRenewalCacheData) caObj).getTransTime());
			return laDate;
		}
		else if (caObj instanceof TransactionKey)
		{ //for delete inventory
			laDate =
				new RTSDate(
					RTSDate.AMDATE,
					((TransactionKey) caObj).getCacheAMDate());
			laDate.setTime(((TransactionKey) caObj).getCacheTime());
			return laDate;
		}
		else if (caObj instanceof LogonFunctionTransactionData)
		{ 
			// for Logon Function Transaction Data
			laDate =
				// defect 11073 
				new RTSDate(
						RTSDate.YYYYMMDD,
					//RTSDate.AMDATE,
					((LogonFunctionTransactionData) caObj)
						.getSysDate());
			    // end defect 11073 
			
			laDate.setTime(
				((LogonFunctionTransactionData) caObj).getSysTime());
			return laDate;
		}
		else if (caObj instanceof InternetTransactionData)
		{
			// for Internet Trans Data Object
			laDate =
				(((InternetTransactionData) caObj).getTransDateTime());
			//date.setTime(((InternetTransData) cObj).getTransTime().getClockTime());
			return laDate;
		}
		else if (caObj instanceof ReprintData)
		{
			return caCreationDate;
		}
		else if (caObj instanceof ReprintStickerTransData)
		{
			ReprintStickerTransData laRprntStkrTransData =
				(ReprintStickerTransData) caObj;
			HashMap lhmHashMap =
				(HashMap) laRprntStkrTransData
					.getReprintStickerHashMap();
			CompleteTransactionData laCTD =
				((CompleteTransactionData) lhmHashMap.get("CTDATA"));
			laDate =
				new RTSDate(RTSDate.AMDATE, laCTD.getTransAMDate());
			laDate.setTime(laCTD.getTransTime());
			return laDate;
		}
		// defect 8087 
		else if (caObj instanceof WorkstationStatusData)
		{
			return ((WorkstationStatusData) caObj)
				.getLastRestartTstmp();
		}
		// end defect 8087 
		else
		{
			// invalid object
			return null;
		}
	}

	/**
	 * Get object of any of transaction table type. 
	 * i.e. TransHdrData, TransData, etc...
	 * 
	 * @return Object
	 */
	public Object getObj()
	{
		return caObj;
	}

	/**
	 * Get Operation for the object. 
	 *
	 * <P>Like INSERT, DELETE, UPDATE etc....
	 * 
	 * @return int
	 */
	public int getProcName()
	{
		return ciProcName;
	}

	/**
	 * Assess if cbFromSendCache is true (i.e. posted via SendCache)
	 *
	 * @return boolean
	 */
	public boolean isFromSendCache()
	{
		return cbFromSendCache;
	}

	/**
	 * Assign value to FromSendCache
	 * 
	 * @param abFromSendCache boolean
	 */
	public void setFromSendCache(boolean abFromSendCache)
	{
		cbFromSendCache = abFromSendCache;
	}

	/**
	 * Set object of any of transaction table type. 
	 * i.e. TransHdrData, TransData, etc...
	 * 
	 * @param aaObj Object
	 */
	public void setObj(Object aaObj)
	{
		caObj = aaObj;
	}

	/**
	 * Set Operation for the object. 
	 *
	 * <P>Like INSERT, DELETE, UPDATE etc....
	 * 
	 * @param aiProcName int
	 */
	public void setProcName(int aiProcName)
	{
		ciProcName = aiProcName;
	}
}
