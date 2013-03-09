package com.txdot.isd.rts.services.cache;import java.util.Enumeration;import java.util.Hashtable;import java.util.Vector;import com.txdot.isd.rts.services.data.OfficeIdsData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.constants.CacheConstant;/* * * OfficeIdsCache.java * * (c) Texas Department of Transportation 2001 *  * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * J Kwik		08/28/2001	Add comments * J Kwik		09/06/2001	Convert to Hungarian notation. * K Harrell    01/07/2003 	Remove EFTIndi * 							defect 5228   * K Harrell	06/20/2005	Java 1.4 Cleanup * 							defect 7899 Ver 5.2.3 * J Ralph		10/20/2006	Added method to get Regional Office Ids * 							add getRegnlOfcIds() * 							defect 8900 Ver Exempts * K Harrell	03/09/2007	add isHQ(),isRegion() * 							add HQ_CD, REGION_CD * 							Used in non-client scenarios * 							defect 9085 Ver Special Plates 	 * Jeff S.		04/05/2007	Added a Number of counties object that can * 							be used to only get the office id's for just * 							the county offices. * 							add TOTAL_COUNTIES * 							defect 9121 Ver Special Plates  * K Harrell	10/12/2009	add getOfcName() * 							defect 10207 Ver Defect_POS_G 	 * K Harrell	03/26/2010	add isEmpty()  * 							defect 8087 Ver POS_640  * K Harrell	06/27/2010	add DEFAULT_REGNL_OFCID, getRegnlOfcId() * 							defect 10491 Ver 6.5.0  * --------------------------------------------------------------------- *//** * OfficeIdsCache class extends GeneralCache. * The OfficeIdsCache class provides static methods to  * retrieve a particular or all OfficeIdsData based  * on the input parameter. * * @version 6.5.0 			06/27/2010 * @author 	Joseph Kwik * <br>Creation Date:  *//* &OfficeIdsCache& */public class OfficeIdsCache extends GeneralCache{	/**	 * A hashtable of vectors with ofcIssuanceNo as key	 *//* &OfficeIdsCache'HQ_CD& */	private static final int HQ_CD = 1;/* &OfficeIdsCache'REGION_CD& */	private static final int REGION_CD = 2;/* &OfficeIdsCache'shtOfficeIds& */	private static Hashtable shtOfficeIds = new Hashtable();	// defect 10207	// Not Used 	//private static final int TOTAL_COUNTIES = 254;	// end defect 10207 	// defect 10491 /* &OfficeIdsCache'DEFAULT_REGNL_OFCID& */	private static final int DEFAULT_REGNL_OFCID = 260;	// end defect 10491 /* &OfficeIdsCache'serialVersionUID& */	private final static long serialVersionUID = -7729116949033094307L;	/**	 * OfficeIdsCache default constructor.  Calls super();	 *//* &OfficeIdsCache.OfficeIdsCache& */	public OfficeIdsCache()	{		super();	}	/**	 * Implements the GeneralCache.getCacheFunctionId() abstract method.	 * Return the CacheConstant for this cache type	 * 	 * @return int	 *//* &OfficeIdsCache.getCacheFunctionId& */	public int getCacheFunctionId()	{		return CacheConstant.OFFICE_IDS_CACHE;	}	/**	 * Get the internally stored Hashtable.	 *	 * <P> Class that inherits from Admin cache is required	 * to implement this method.	 * 	 * @return Hashtable	 *//* &OfficeIdsCache.getHashtable& */	public Hashtable getHashtable()	{		return shtOfficeIds;	}	/**	 * Gets the office id data object.  Returns null if object does not exist.	 * 	 * @param aiOfcIssuanceNo int	 * @return OfficeIdsData 	 *//* &OfficeIdsCache.getOfcId& */	public static OfficeIdsData getOfcId(int aiOfcIssuanceNo)	{		Object laObject =			shtOfficeIds.get(new Integer(aiOfcIssuanceNo));		if (laObject != null)		{			return (OfficeIdsData) laObject;		}		else		{			return null;		}	}	/**	 * 	 * Determine if the passed OfcIssuanceNo belongs to a Region	 * 	 * @param aiOfcIssuanceNo	 * @return boolean 	 *//* &OfficeIdsCache.isRegion& */	public static boolean isRegion(int aiOfcIssuanceNo)	{		boolean lbReturn = false;		OfficeIdsData laOfcIdsData = getOfcId(aiOfcIssuanceNo);		if (laOfcIdsData != null			&& laOfcIdsData.getOfcIssuanceCd() == REGION_CD)		{			lbReturn = true;		}		return lbReturn;	}	/**	 * 	 * Determine if the passed OfcIssuanceNo belongs to HQ	 * 	 * @param aiOfcIssuanceNo	 * @return boolean 	 *//* &OfficeIdsCache.isHQ& */	public static boolean isHQ(int aiOfcIssuanceNo)	{		boolean lbReturn = false;		OfficeIdsData laOfcIdsData = getOfcId(aiOfcIssuanceNo);		if (laOfcIdsData != null			&& laOfcIdsData.getOfcIssuanceCd() == HQ_CD)		{			lbReturn = true;		}		return lbReturn;	}	/** 	 * Is Empty	 *//* &OfficeIdsCache.isEmpty& */	public static boolean isEmpty()	{		return shtOfficeIds.isEmpty();	}	/**	 * Gets the office ids data object vector.	 * Returns null if no objects exist.	 * 	 * @return Vector 	 *//* &OfficeIdsCache.getOfcIds& */	public static Vector getOfcIds()	{		Vector lvOfficeIdsData = new Vector();		Enumeration e = shtOfficeIds.elements();		while (e.hasMoreElements())		{			OfficeIdsData laData = (OfficeIdsData) e.nextElement();			lvOfficeIdsData.addElement(laData);		}		if (lvOfficeIdsData.size() == 0)		{			return null;		}		else		{			return lvOfficeIdsData;		}	}	/**	 * Return Name of Office if passed OfcIssuanceNo found  	 * Method description	 * 	 * @param aiOfcNo	 * @return String	 *//* &OfficeIdsCache.getOfcName& */	public static String getOfcName(int aiOfcIssuanceNo)	{		String lsOfcName = "";		OfficeIdsData laData = getOfcId(aiOfcIssuanceNo);		if (laData != null)		{			lsOfcName = laData.getOfcName();		}		return lsOfcName;	}		/**	 * Return Regional OfficeId associated Created for Development Testing.  	 * 	 * @param aiOfcId int	 * @return int 	 *//* &OfficeIdsCache.getRegnlOfcId& */	public static int getRegnlOfcId(int aiOfcId)	{		int liRegnlOfcId = DEFAULT_REGNL_OFCID;		OfficeIdsData laOfcIdsData = getOfcId(aiOfcId);		if (laOfcIdsData != null && laOfcIdsData.getRegnlOfcId() != 0)		{			liRegnlOfcId = laOfcIdsData.getRegnlOfcId();		}		return liRegnlOfcId; 	}	/**	 * Gets the office ids data object vector for all the offices in 	 * the given region.	 * 	 * Returns null if no objects exist.	 * 	 * @param aiRegnlOfcId int	 * @return Vector of OfficeIdsData objects or null.	 *//* &OfficeIdsCache.getRegnlOfcIds& */	public static Vector getRegnlOfcIds(int aiRegnlOfcId)	{		Vector lvRegnlOfcIds = new Vector();		Enumeration e = shtOfficeIds.elements();		while (e.hasMoreElements())		{			OfficeIdsData laData = (OfficeIdsData) e.nextElement();			if (laData.getRegnlOfcId() == aiRegnlOfcId)			{				lvRegnlOfcIds.addElement(laData);			}		}		if (lvRegnlOfcIds.size() == 0)		{			return null;		}		else		{			return lvRegnlOfcIds;		}	}	/**	 * Main method for testing office ids cache class methods.	 * 	 * @param aarrArgs String[]	 * @throws RTSException	 *//* &OfficeIdsCache.main& */	public static void main(String[] aarrArgs) throws RTSException	{		try		{			Vector lvOfficeIdsData = new Vector();			OfficeIdsData laData1 = new OfficeIdsData();			laData1.setOfcIssuanceCd(100);			laData1.setDefrdRemitCd("d1.aDefrdRemitCd");			laData1.setEFTAccntCd(100);			laData1.setOfcCity("d1.aOfcCity");			laData1.setOfcIssuanceCd(120);			laData1.setOfcIssuanceNo(130);			laData1.setOfcName("d1.aOfcName");			laData1.setOfcSt("d1.aOfcSt");			laData1.setOfcZpCd(140);			laData1.setOfcZpCdP4("d1.aOfcZpCdP4");			laData1.setPhysOfcLoc("d1.aPhysOfcLoc");			laData1.setRegnlOfcId(150);			laData1.setTacAcctNo("d1.aTacAcctNo");			laData1.setTacName("d1.aTacName");			laData1.setTacPhoneNo("d1.aTacPhoneNo");			laData1.setTXDOTCntyNo(160);			lvOfficeIdsData.addElement(laData1);			OfficeIdsData laData2 = new OfficeIdsData();			laData2.setOfcIssuanceCd(200);			laData2.setDefrdRemitCd("d2.aDefrdRemitCd");			laData2.setEFTAccntCd(200);			laData2.setOfcCity("d2.aOfcCity");			laData2.setOfcIssuanceCd(220);			laData2.setOfcIssuanceNo(230);			laData2.setOfcName("d2.aOfcName");			laData2.setOfcSt("d2.aOfcSt");			laData2.setOfcZpCd(240);			laData2.setOfcZpCdP4("d2.aOfcZpCdP4");			laData2.setPhysOfcLoc("d2.aPhysOfcLoc");			laData2.setRegnlOfcId(250);			laData2.setTacAcctNo("d2.aTacAcctNo");			laData2.setTacName("d2.aTacName");			laData2.setTacPhoneNo("d2.aTacPhoneNo");			laData2.setTXDOTCntyNo(260);			lvOfficeIdsData.addElement(laData2);			OfficeIdsCache laOfcIdsCache = new OfficeIdsCache();			laOfcIdsCache.setData(lvOfficeIdsData);			Vector lvOfcIdsData = OfficeIdsCache.getOfcIds();			for (int i = 0; i < lvOfcIdsData.size(); i++)			{				OfficeIdsData laData =					(OfficeIdsData) lvOfcIdsData.get(i);				System.out.println(					"OfficeIssuance code: "						+ laData.getOfcIssuanceCd());				System.out.println(					"setDefrdRemitCd: " + laData.getDefrdRemitCd());				System.out.println(					"EFTAccntCd: " + laData.getEFTAccntCd());				System.out.println("OfcCity: " + laData.getOfcCity());				System.out.println(					"OfcIssuanceCd: " + laData.getOfcIssuanceCd());				System.out.println(					"OfcIssuanceNo: " + laData.getOfcIssuanceNo());				System.out.println("OfcName: " + laData.getOfcName());				System.out.println("OfcSt: " + laData.getOfcSt());				System.out.println("OfcZpCd: " + laData.getOfcZpCd());				System.out.println(					"OfcZpCdP4: " + laData.getOfcZpCdP4());				System.out.println(					"PhysOfcLoc: " + laData.getPhysOfcLoc());				System.out.println(					"RegnlOfcId: " + laData.getRegnlOfcId());				System.out.println(					"TacAcctNo: " + laData.getTacAcctNo());				System.out.println("TacName: " + laData.getTacName());				System.out.println(					"TacPhoneNo: " + laData.getTacPhoneNo());				System.out.println(					"TXDOTCntyNo: " + laData.getTXDOTCntyNo());			}			System.out.println("done");		}		catch (Exception aeEx)		{			aeEx.printStackTrace();		}	}	/**	 * Implements the GeneralCache.setData() abstract method.	 * Populates officeIdsData vector into a hashtable.	 * 	 * @param avOfficeIdsDataVector	 *//* &OfficeIdsCache.setData& */	public void setData(Vector avOfficeIdsData)	{		//reset the hashtable		shtOfficeIds.clear();		for (int i = 0; i < avOfficeIdsData.size(); i++)		{			OfficeIdsData laData =				(OfficeIdsData) avOfficeIdsData.get(i);			shtOfficeIds.put(				new Integer(laData.getOfcIssuanceNo()),				laData);		}	}	/**	 * Set the internally stored Hashtable.	 *	 * <P> Class that inherits from Admin cache is required	 * to implement this method.	 * 	 * @param ahtHashtable Hashtable	 *//* &OfficeIdsCache.setHashtable& */	public void setHashtable(Hashtable ahtHashtable)	{		shtOfficeIds = ahtHashtable;	}}/* #OfficeIdsCache# */