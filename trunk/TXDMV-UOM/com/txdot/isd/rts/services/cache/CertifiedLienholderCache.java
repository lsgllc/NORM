package com.txdot.isd.rts.services.cache;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.CertifiedLienholderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.CertifiedLienholder;

/*
 *
 * CertifiedLienholderCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2009	Created
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/23/2009	Added sort for returned vectors 
 * 							modify getLastestETtlCertfdLienhldrs(), 
 * 							 getCurrentCertfdLienhldrs(), 
 * 							 getLatestCertfdLienhldrs()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/07/2009	Certified w/ ETtlRdyIndi can ONLY be used
 * 							for ETitle. 
 * 							modify getCurrentCertfdLienhldrs() 
 * 							defect 10124 Ver Defect_POS_F
 * K Harrell	07/27/2009	Server should retrieve from DB if available
 * 							add MSG_SERVER_CALL_FAILED
 * 							add getFromDB() 
 * 							modify getLatestCertfdLienhldr()
 * 							defect 10117 Ver Defect_POS_E'
 * K Harrell	08/13/2009	ETitle Report was showing Non-ELienRdy
 * 							modify getLastestETtlCertfdLienhldrs()
 * 							defect 10117 Ver Defect_POS_E' 
 * K Harrell	01/12/2011	add getMinRTSEffDate()
 * 							defect 10631 Ver 6.7.0    
 * ---------------------------------------------------------------------
 */

/**
 * The CertifiedLienholderCache class provides static methods to 
 * retrieve a particular CertifiedLienholderData based upon  
 * input parameters.
 *
 * <p>CertifiedLienholderCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	6.7.0  			01/12/2011
 * @author Kathy Harrell 
 * <br>Creation Date:		02/26/2009  
 */
public class CertifiedLienholderCache
	extends GeneralCache
	implements java.io.Serializable
{
	// defect 10117 
	private static final String MSG_SERVER_CALL_FAILED =
		"Server down, using local Certified Lienholder cache";
	// end defect 10117

	private static Hashtable shtCertfdLienhldrs = new Hashtable();

	private static final long serialVersionUID = -8155435216128748679L;

	/**
	 * CertifiedLienholderCache default constructor
	 */
	public CertifiedLienholderCache()
	{
		super();
	}

	/**
	 * Get the CertifiedLienholderData
	 * 
	 * @param asCertfdLienhldrId 
	 * @return CertifiedLienholderData 
	 */
	public static CertifiedLienholderData getCurrentCertfdLienhldr(String asCertfdLienhldrId)
	{
		CertifiedLienholderData laData = null;
		int liCurrentDate = new RTSDate().getYYYYMMDDDate();

		if (shtCertfdLienhldrs.containsKey(asCertfdLienhldrId))
		{
			Vector lvData =
				(Vector) shtCertfdLienhldrs.get(asCertfdLienhldrId);

			if (lvData != null && lvData.size() > 0)
			{
				for (int i = 0; i < lvData.size(); i++)
				{
					CertifiedLienholderData laCompData =
						(CertifiedLienholderData) lvData.get(i);

					if (liCurrentDate >= laCompData.getRTSEffDate()
						&& liCurrentDate <= laCompData.getRTSEffEndDate())
					{
						laData = laCompData;
						break;
					}
				}
			}
		}
		return laData;
	}

	/**
	 * Get all  CertifiedLienholderData
	 * 
	 * @return Vector 
	 */
	public static Vector getCurrentCertfdLienhldrs(boolean abElienRdyOnly)
	{
		Vector lvReturn = new Vector();
		Vector lvCertfd = new Vector(shtCertfdLienhldrs.values());
		int liCurrentDate = new RTSDate().getYYYYMMDDDate();

		for (int i = 0; i < lvCertfd.size(); i++)
		{
			Vector lvData = (Vector) lvCertfd.elementAt(i);

			for (int j = 0; j < lvData.size(); j++)
			{
				CertifiedLienholderData laData =
					(CertifiedLienholderData) lvData.get(j);

				if (laData.getRTSEffDate() <= liCurrentDate
					&& laData.getRTSEffEndDate() >= liCurrentDate)
				{
					// defect 10124
					// if (!abElienRdyOnly
					//	|| (abElienRdyOnly && laData.isElienRdy()))
					//
					if (abElienRdyOnly == laData.isElienRdy())
					{
						// end defect 10124 
						lvReturn.add(laData);
						break;
					}
				}
			}
		}
		UtilityMethods.sort(lvReturn);
		return lvReturn;
	}

	/** 
	 * Get From DB
	 * 
	 * @param asCertfdLienhldrId
	 * @return Vector
	 */
	private static Vector getFromDB(String asCertfdLienhldrId)
		throws RTSException
	{
		Vector lvReturn = new Vector();
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			CertifiedLienholderData laSearchCertfdLienhldrData =
				new CertifiedLienholderData();

			laSearchCertfdLienhldrData.setPermLienHldrId(
				asCertfdLienhldrId);

			laDBA.beginTransaction();
			CertifiedLienholder laSQL = new CertifiedLienholder(laDBA);

			lvReturn =
				laSQL.qryCertifiedLienholder(
					laSearchCertfdLienhldrData);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Get Latest ETitle CertifiedLienholderData
	 * 
	 * @return Vector 
	 */
	public static Vector getLastestETtlCertfdLienhldrs()
	{
		Vector lvReturn = new Vector();
		Vector lvCertfd = new Vector(shtCertfdLienhldrs.values());

		for (int i = 0; i < lvCertfd.size(); i++)
		{
			Vector lvData = (Vector) lvCertfd.elementAt(i);

			// defect 10117 
			CertifiedLienholderData laMaxData = null;

			for (int j = 0; j < lvData.size(); j++)
			{
				CertifiedLienholderData laData =
					(CertifiedLienholderData) lvData.get(j);

				if (laData.isElienRdy()
					&& (laMaxData == null
						|| (laData.getRTSEffEndDate()
							> laMaxData.getRTSEffEndDate())))
				{
					laMaxData = laData;
				}
			}
			if (laMaxData != null)
			{
				lvReturn.add(laMaxData);
			}
			// end defect 10117 
		}
		UtilityMethods.sort(lvReturn);
		return lvReturn;
	}

	/**
	 * Get Vector of Latest CertifiedLienholderData where 
	 * RTSEffDate <= Current Date
	 * 
	 * @return Vector 
	 */
	public static Vector getLatestCertfdLienhldrs()
	{
		Vector lvReturn = new Vector();
		Vector lvCertfd = new Vector(shtCertfdLienhldrs.values());

		for (int i = 0; i < lvCertfd.size(); i++)
		{
			Vector lvData = (Vector) lvCertfd.elementAt(i);

			CertifiedLienholderData laMaxData =
				(CertifiedLienholderData) lvData.get(0);

			for (int j = 1; j < lvData.size(); j++)
			{
				CertifiedLienholderData laData =
					(CertifiedLienholderData) lvData.get(j);

				if (laData.getRTSEffEndDate()
					> laMaxData.getRTSEffEndDate())
				{
					laMaxData = laData;
				}
			}
			lvReturn.add(laMaxData);
		}
		UtilityMethods.sort(lvReturn);
		return lvReturn;
	}

	/**
	 * Get the CertifiedLienholderData
	 * 
	 * @param asCertfdLienhldrId 
	 * @return CertifiedLienholderData 
	 */
	public static CertifiedLienholderData getLatestCertfdLienhldr(String asCertfdLienhldrId)
	{
		CertifiedLienholderData laMaxData = null;

		// defect 10117 
		try
		{
			if (Comm.isServer())
			{
				Vector lvReturn = getFromDB(asCertfdLienhldrId);

				if (lvReturn.size() != 0)
				{
					laMaxData =
						(CertifiedLienholderData) lvReturn.get(0);
				}
				return laMaxData;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, aeRTSEx, MSG_SERVER_CALL_FAILED);
		}
		// end defect 10117 

		if (shtCertfdLienhldrs.containsKey(asCertfdLienhldrId))
		{
			Vector lvData =
				(Vector) shtCertfdLienhldrs.get(asCertfdLienhldrId);

			laMaxData = (CertifiedLienholderData) lvData.get(0);

			for (int j = 1; j < lvData.size(); j++)
			{
				CertifiedLienholderData laData =
					(CertifiedLienholderData) lvData.get(j);

				if (laData.getRTSEffEndDate()
					> laMaxData.getRTSEffEndDate())
				{
					laMaxData = laData;
				}
			}
		}
		return laMaxData;
	}

	/**
	 * Get the CertifiedLienholderData
	 * 
	 * @param asCertfdLienhldrId 
	 * @return int 
	 */
	public static int getMinRTSEffDate(String asCertfdLienhldrId)
	{
		int liRTSEffDate = 0;

		if (shtCertfdLienhldrs.containsKey(asCertfdLienhldrId))
		{
			Vector lvData =
				(Vector) shtCertfdLienhldrs.get(asCertfdLienhldrId);

			for (int j = 0; j < lvData.size(); j++)
			{
				CertifiedLienholderData laData =
					(CertifiedLienholderData) lvData.get(j);

				if (j == 0 || laData.getRTSEffDate() < liRTSEffDate)
				{
					liRTSEffDate = laData.getRTSEffDate();
				}
			}
		}
		return liRTSEffDate;
	}

	/**
	 * Get the cache function id
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.CERTFD_LIENHLDR_CACHE;
	}

	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtCertfdLienhldrs;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param avCertLienHldrData Vector
	 * @throws RTSException 
	 */
	public void setData(Vector lvCertfdLienData) throws RTSException
	{
		//reset data
		shtCertfdLienhldrs.clear();

		for (int i = 0; i < lvCertfdLienData.size(); i++)
		{
			CertifiedLienholderData laCertfdLienData =
				(CertifiedLienholderData) lvCertfdLienData.get(i);

			String lsPermLienhldrId =
				laCertfdLienData.getPermLienHldrId();

			if (shtCertfdLienhldrs.containsKey(lsPermLienhldrId))
			{
				Vector lvData =
					(Vector) shtCertfdLienhldrs.get(lsPermLienhldrId);
				lvData.add(laCertfdLienData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.add(laCertfdLienData);
				shtCertfdLienhldrs.put(lsPermLienhldrId, lvData);
			}
		}
	}

	/**
	 * Set the hashtable
	 * 
	 * @param ahtCertLienhldr Hashtable 
	 */
	public void setHashtable(Hashtable ahtCertLienhldr)
	{
		shtCertfdLienhldrs = ahtCertLienhldr;
	}
}
