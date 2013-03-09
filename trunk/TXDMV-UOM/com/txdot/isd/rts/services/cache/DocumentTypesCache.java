package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DocumentTypesData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * DocumentTypesCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * A Yang		08/24/2001	Add comments
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * B Hargrove	01/17/2008	Add method to return a vector of all
 * 							Doc Types.
 * 							add getDocTypesVec()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	03/04/2009	add isETtlAllowd()  
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/06/2009	add getDefaultETtlCd()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	02/01/2012	add SALV_DOC_TYPE_GRP_CD
 * 							add isExportEligible() 
 * 							defect 11228 Ver 6.10.0 
 *----------------------------------------------------------------------
 */

/**
 * The DocumentTypesCache class provides static methods to 
 * retrieve a DocumentTypesData based on different input parameters.
 *
 * <p>DocumentTypesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	6.10.0			02/01/2012 
 * @author	Alexandra Yang 
 * <br>Creation Date: 		08/27/2001	14:37:37  
 */

public class DocumentTypesCache
	extends GeneralCache
	implements java.io.Serializable
{

	// defect 11228 
	private static String SALV_DOC_TYPE_GRP_CD = "SALV"; 
	// end defect 11228 
	
	private static Hashtable shtDocumentTypes = new Hashtable();
	private final static long serialVersionUID = 1003466983201444381L;

	/**
	 * DocumentTypesCache constructor comment.
	 */
	public DocumentTypesCache()
	{
		super();
	}

	/**
	 * Return the CacheConstant for this cache type.
	 *
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.DOCUMENT_TYPES_CACHE;
	}

	/**
	 * Get Default ETtlCd
	 * 
	 * @param aiDocTypeCd 
	 * @return smallint 
	 */
	public static int getDefaultETtlCd(int aiDocTypeCd)
	{
		DocumentTypesData laData = getDocType(aiDocTypeCd);
		return laData.getDefaultETtlCd();
	}

	/**
	 * Returns the DocumentTypesData object based on the input
	 * aiDocTypeCd.
	 *
	 * @param  aiDocTypeCd int
	 * @return DocumentTypesData
	 */
	public static DocumentTypesData getDocType(int aiDocTypeCd)
	{
		Object laObject =
			shtDocumentTypes.get(new Integer(aiDocTypeCd));

		if (laObject != null)
		{
			return (DocumentTypesData) laObject;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get a vector of Document Type objects
	 * 
	 * @return Vector
	 */
	public static Vector getDocTypesVec()
	{
		if (shtDocumentTypes.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturnVector = new Vector();
			for (Enumeration e = shtDocumentTypes.elements();
				e.hasMoreElements();
				)
			{
				lvReturnVector.addElement(e.nextElement());

			}
			return lvReturnVector;

		}
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
		return shtDocumentTypes;
	}

	/**
	 * Is ETtlAllowd 
	 * 
	 * @param aiDocTypeCd 
	 * @return boolean 
	 */
	public static boolean isETtlAllowd(int aiDocTypeCd)
	{
		DocumentTypesData laData = getDocType(aiDocTypeCd);
		return laData.isETtlAllowd();
	}
	
	/**
	 * Is Export Eligible 
	 * 
	 * @param aiDocTypeCd
	 * @return boolean 
	 */
	public static boolean isExportEligible(int aiDocTypeCd)
	{
		DocumentTypesData laData = getDocType(aiDocTypeCd);
		String lsDocTypeGrpCd = laData.getDocTypeGrpCd() == null ? 
				new String() : laData.getDocTypeGrpCd(); 
		return lsDocTypeGrpCd.equals(SALV_DOC_TYPE_GRP_CD);
	}

	/**
	 * Test main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvInput = new Vector();

			DocumentTypesData laDocTypesData1 = new DocumentTypesData();
			laDocTypesData1.setDocTypeCd(1);
			laDocTypesData1.setDocTypeCdDesc("REGULAR TITLE");
			laDocTypesData1.setRegRecIndi(1);
			lvInput.addElement(laDocTypesData1);

			DocumentTypesData laDocTypesData2 = new DocumentTypesData();
			laDocTypesData2.setDocTypeCd(2);
			laDocTypesData2.setDocTypeCdDesc("OFF-HIGHWAY USE ONLY");
			laDocTypesData2.setRegRecIndi(2);
			lvInput.addElement(laDocTypesData2);

			DocumentTypesData laDocTypesData3 = new DocumentTypesData();
			laDocTypesData3.setDocTypeCd(4);
			laDocTypesData3.setDocTypeCdDesc("SALVAGE CERTIFICATE");
			lvInput.addElement(laDocTypesData3);

			DocumentTypesData laDocTypesData4 = new DocumentTypesData();
			laDocTypesData4.setDocTypeCd(3);
			laDocTypesData4.setDocTypeCdDesc(
				"SALVAGE CERTIFICATE - NO REGIS");
			laDocTypesData4.setRegRecIndi(0);
			lvInput.addElement(laDocTypesData4);

			DocumentTypesCache laDocumentTypesCache =
				new DocumentTypesCache();
			laDocumentTypesCache.setData(lvInput);

			DocumentTypesData laDocTypesData =
				DocumentTypesCache.getDocType(2);
			System.out.println(laDocTypesData.getDocTypeCd());
			System.out.println(laDocTypesData.getDocTypeCdDesc());
			System.out.println("done");

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * avDocTypesData 
	 * 
	 * @param avDocTypesData Vector
	 */
	public void setData(Vector avDocTypesData)
	{
		//reset data
		shtDocumentTypes.clear();
		for (int i = 0; i < avDocTypesData.size(); i++)
		{
			DocumentTypesData laData =
				(DocumentTypesData) avDocTypesData.get(i);
			shtDocumentTypes.put(
				new Integer(laData.getDocTypeCd()),
				laData);
		}
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
		shtDocumentTypes = ahtHashtable;
	}
}
