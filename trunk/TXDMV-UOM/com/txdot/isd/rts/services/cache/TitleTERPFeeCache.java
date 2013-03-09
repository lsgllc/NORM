package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTERPFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TitleTERPFeeCache.java  
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  			 
 * B Hargrove 	06/05/2008 	There are now two TERP fee Acct Cds for Cnty   
 * 							Status Cd = 'A' counties. Return a vector of   
 * 							all current TERP fees for the Cnty Status Cd. 
 * 							modify getTERPFee(), main()
 * 							defect 8552 Ver Defect POS A
 * ---------------------------------------------------------------------
 */
/**
 * The TitleTERPFeeCache  class provides static methods to 
 * retrieve a particular or a list of SubstationData based 
 * on different input parameters.
 *
 * <p>TitleTERPFeeCache  is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	Defect POS A	06/05/2008
 * @author	Kathy Harrell
 * <br>Creation Date: 		07/30/2003 22:10:33  
 */

public class TitleTERPFeeCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* TtlTERPFee hashtable
	*/
	private static Hashtable shtTtlTERPFee = new Hashtable();

	private final static long serialVersionUID = -3606596557420971470L;
	
	/**
	 * TitleTERPFeeCache default constructor.
	 */
	public TitleTERPFeeCache()
	{
		super();
	}
	
	/**
	* Return the CacheConstant for this cache type
	* 
	* @return int
	*/
	public int getCacheFunctionId()
	{
		return CacheConstant.TITLE_TERP_FEE_CACHE;
	}
	
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtTtlTERPFee;
	}
	
	/**
	 * Get a vector of Title TERP Percent objects
	 * 
	 * @return Vector 
	 */
	public static Vector getTtlTERPFeeVec()
	{

		if (shtTtlTERPFee.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturn = new Vector();
			for (Enumeration e = shtTtlTERPFee.elements();
				e.hasMoreElements();
				)
			{
				lvReturn.addElement(e.nextElement());

			}
			return lvReturn;

		}
	}
	/**
	* Test main
	* 
	* @param aarrArgs String[]
	*/
	public static void main(String[] args)
	{
		try
		{
			Vector lvData = new Vector();

			TitleTERPFeeData laData1 = new TitleTERPFeeData();
			laData1.setRTSEffDate(20030701);
			laData1.setRTSEffEndDate(20080831);
			laData1.setTERPAcctItmCd("TERPTTL");
			laData1.setCntyTERPStatusCd("N");
			laData1.setTtlTERPFlatFee(new Dollar("15.00"));
			lvData.addElement(laData1);

			TitleTERPFeeData laData2 = new TitleTERPFeeData();
			laData2.setRTSEffDate(20030701);
			laData2.setRTSEffEndDate(20080831);
			laData2.setTERPAcctItmCd("TERPTTL");
			laData2.setCntyTERPStatusCd("A");
			laData2.setTtlTERPFlatFee(new Dollar("20.00"));
			lvData.addElement(laData2);

			TitleTERPFeeData laData3 = new TitleTERPFeeData();
			laData3.setRTSEffDate(20080901);
			laData3.setRTSEffEndDate(99991231);
			laData3.setTERPAcctItmCd("TERPTTL");
			laData3.setCntyTERPStatusCd("N");
			laData3.setTtlTERPFlatFee(new Dollar("15.00"));
			lvData.addElement(laData3);

			TitleTERPFeeData laData4 = new TitleTERPFeeData();
			laData4.setRTSEffDate(20080901);
			laData4.setRTSEffEndDate(99991231);
			laData4.setCntyTERPStatusCd("A");
			laData4.setTERPAcctItmCd("TERPTTL");
			laData4.setTtlTERPFlatFee(new Dollar("15.00"));
			lvData.addElement(laData4);

			TitleTERPFeeCache laTTFCache = new TitleTERPFeeCache();
			laTTFCache.setData(lvData);

			// defect 8552
			// There are now two rows for Cnty Status Cd =  'A'.
			// getTERPFee() now returns a Vector.
			//TitleTERPFeeData laData =
			//	TitleTERPFeeCache.getTERPFee("N", 20030715);
			//System.out.println(
			//	"A"
			//		+ 1
			//		+ ")"
			//		+ " "
			//		+ laData.getTtlTERPFlatFee()
			//		+ " "
			//		+ laData.getTERPAcctItmCd());
			Vector lvTtlTERP = TitleTERPFeeCache.getTERPFee(
				"A",20080601);

			Object[] larrTemp = lvTtlTERP.toArray();
			
			for (int i = 0; i < larrTemp.length; i++)
			{
				TitleTERPFeeData laTitleTERP = 
					(TitleTERPFeeData) larrTemp[i];
				  
				System.out.println(
					"A"
						+ i
						+ ")"
						+ " "
						+ laTitleTERP.getTtlTERPFlatFee()
						+ " "
						+ laTitleTERP.getTERPAcctItmCd());
			}
			// end defect 8552

			lvData = TitleTERPFeeCache.getTtlTERPFeeVec();

			int i = 0;

			for (Enumeration e = lvData.elements();
				e.hasMoreElements();
				)
			{
				Vector lvData1 = (Vector) e.nextElement();

				for (Enumeration e1 = lvData1.elements();
					e1.hasMoreElements();
					)
				{
					i++;
					TitleTERPFeeData laTTFData =
						(TitleTERPFeeData) e1.nextElement();

					System.out.println(
						"B"
							+ i
							+ ")"
							+ " "
							+ laTTFData.getRTSEffDate()
							+ " "
							+ laTTFData.getRTSEffEndDate()
							+ " "
							+ laTTFData.getTtlTERPFlatFee()
							+ " "
							+ laTTFData.getTERPAcctItmCd());
				}
			}
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	* Set the data of the cache to contain the elements of the vector
	* being passed in.
	* 
	* @param avTransCdsDataVector Vector
	*/
	public void setData(Vector avTtlTERPFeeDataVector)
		throws RTSException
	{
		shtTtlTERPFee.clear();

		if (avTtlTERPFeeDataVector == null)
		{
			return;
		}

		for (int i = 0; i < avTtlTERPFeeDataVector.size(); i++)
		{
			TitleTERPFeeData laTtlTERPFeeData =
				(TitleTERPFeeData) avTtlTERPFeeDataVector.get(i);
			String lsPrimaryKey =
				laTtlTERPFeeData.getCntyTERPStatusCd();

			if (shtTtlTERPFee.containsKey(lsPrimaryKey))
			{
				Vector lvData =
					(Vector) shtTtlTERPFee.get(lsPrimaryKey);
				lvData.add(laTtlTERPFeeData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laTtlTERPFeeData);
				shtTtlTERPFee.put(lsPrimaryKey, lvData);
			}
		}
		return;
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable
	*/
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtTtlTERPFee = ahtHashtable;
	}

	/**
	* Returns the TitleTERPFeeData object based on the input String
	* defect 8552
	* Return a vector of all current TERP fees for this Cnty Status Cd
	*   
	* String asCntyTERPStatusCd
	* int aiEffDate
	* 
	* @return Vector 
	*/
	//public static TitleTERPFeeData getTERPFee(
	//	String asCntyTERPStatusCd,
	//	int aiEffDate)
	public static Vector getTERPFee(String asCntyTERPStatusCd,
		int aiEffDate)
	{
	//	TitleTERPFeeData laReturnData = null;
		Vector lvReturn = new Vector();

		if (shtTtlTERPFee.containsKey(asCntyTERPStatusCd))
		{
			Vector lvTtlTERPFeeData =
				(Vector) shtTtlTERPFee.get(asCntyTERPStatusCd);

			for (int i = 0; i < lvTtlTERPFeeData.size(); i++)
			{
				TitleTERPFeeData laData =
					(TitleTERPFeeData) lvTtlTERPFeeData.get(i);

				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
	//				laReturnData = laData;
	//				break;
					lvReturn.addElement(laData);
				}
			}
		}
	//	return laReturnData;
		return lvReturn;
	}
	// end defect 8552
}
