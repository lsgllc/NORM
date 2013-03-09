package com.txdot.isd.rts.services.data;

import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;

/*
 *
 * ReprintStickerData.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/02/2004	Created
 *							defect 7284 Ver 5.2.0
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Data object containing sending RPRSTK transaction request 
 *
 * @version	5.2.3		05/19/2005
 * @author	K Harrell 
 * <br>Creation Date:	07/02/2004	16:36:46
 */
public class ReprintStickerTransData
	implements java.io.Serializable, Displayable
{
	private Map caReprintStickerHashMap = new HashMap();

	private final static long serialVersionUID = 628067676835887399L;
	/**
	 * ReprintStickerData constructor comment.
	 */
	public ReprintStickerTransData()
	{
		super();
	}
	/**
	 * Build a HashMap of the values to be displayed 
	 *
	 * @return Map
	 */
	public Map getAttributes()
	{
		java.util.HashMap lhmHashMap = new java.util.HashMap();
		HashMap lhmRprStkrHashMap = (HashMap) caReprintStickerHashMap;
		lhmHashMap.put("TransCd", "RPRSTK");
		lhmHashMap.put("TransEmpId", lhmRprStkrHashMap.get("EMPID"));
		ReceiptLogData laRcptLogData =
			(ReceiptLogData) lhmRprStkrHashMap.get("DATA");
		String lsTransId = laRcptLogData.getTransId();
		lhmHashMap.put("Reprinted TransId", lsTransId);
		return lhmHashMap;
	}
	/**
	 * Return caReprintStickerHashMap
	 *
	 * @return Map
	 */
	public Map getReprintStickerHashMap()
	{
		return caReprintStickerHashMap;
	}
	/**
	 * Set caReprintStickerHashMap
	 *
	 * @param aaReprintStickerHashMap Map
	 */
	public void setReprintStickerHashMap(
		java.util.Map aaReprintStickerHashMap)
	{
		caReprintStickerHashMap = aaReprintStickerHashMap;
	}
}
