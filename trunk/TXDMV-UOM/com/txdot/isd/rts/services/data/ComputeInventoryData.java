package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

/*
 *
 * ComputeInventoryData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Min Wang		02/16/2007  change InventoryAllocationUIData
 * 							into InventoryAllocationData.
 * 							add InventoryAllocationData,
 * 							    getInvAlloctnData(), setInvAlloctnData()
 * 							defect 9117 Ver Special Plates  
 * ---------------------------------------------------------------------
 */

/** 
 * Data object used in calculating inventory.  It is created by 
 * ValidateInvNoInput and is required by CalcInvNo and CalcInvEndNo.
 * 
 * @version	Special Plates		02/16/2007 
 * @author	Charlie Walker
 * <br>Creation Date:	10/26/2001 10:42:12
 */

public class ComputeInventoryData implements Serializable
{
	// boolean 	
	private boolean cbValidItmNo = false;

	//	Objects
	// defect 9117
	//private InventoryAllocationUIData caInvAlloctnUIData =
	//	new InventoryAllocationUIData();
	private InventoryAllocationData caInvAlloctnData =
		new InventoryAllocationData();
	// end defect 9117
	private InventoryPatternsData caInvPatrnsData =
		new InventoryPatternsData();
	// Vector 
	/**
	 * These vectors holds data about the inventory pattern
	 *   Element  0 - (String) the alphanumeric inventory pattern for that item
	 *   Element  1 - (String) the first group of characters
	 *   Element  2 - (String) the second group of characters
	 *   Element  3 - (String) the third group of characters
	 *   Element  4 - (String) the first group of numeric characters
	 *   Element  5 - (String) the second group of numeric characters
	 *   Element  6 - (String) the third group of numeric characters
	 *   Element  7 - (String) the inventory item number
	 *   Element  8 - (Integer) the decimalized value for the first group of characters
	 *   Element  9 - (Integer) the decimalized value for the second group of characters
	 *   Element 10 - (Integer) the decimalized value for the third group of characters
	 */
	private Vector cvEndInvNoPatrn = new Vector(11);
	private Vector cvInvNoPatrn = new Vector(11);
	private Vector cvMinInvNoPatrn = new Vector(11);
	private Vector cvMaxInvNoPatrn = new Vector(11);
	private Vector cvOrigInvNoPatrn = new Vector(11);
	private Vector cvOrigMaxInvNoPatrn = new Vector(11);
	private Vector cvOrigMinInvNoPatrn = new Vector(11);

	private final static long serialVersionUID = 8231708637960846472L;

	/**
	 * Returns value of EndInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getEndInvNoPatrn()
	{
		return cvEndInvNoPatrn;
	}
	/**
	 * Returns value of InvAlloctnData
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData getInvAlloctnData()
	{
		return caInvAlloctnData;
	}
	/**
	 * Returns value of InvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getInvNoPatrn()
	{
		return cvInvNoPatrn;
	}
	/**
	 * Returns value of InvPatrnsData
	 * 
	 * @return InventoryPatternsData
	 */
	public InventoryPatternsData getInvPatrnsData()
	{
		return caInvPatrnsData;
	}
	/**
	 * Returns value of MaxInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getMaxInvNoPatrn()
	{
		return cvMaxInvNoPatrn;
	}
	/**
	 * Returns value of MinInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getMinInvNoPatrn()
	{
		return cvMinInvNoPatrn;
	}
	/**
	 * Returns value of OrigInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getOrigInvNoPatrn()
	{
		return cvOrigInvNoPatrn;
	}
	/**
	 * Returns value of OrigMaxInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getOrigMaxInvNoPatrn()
	{
		return cvOrigMaxInvNoPatrn;
	}
	/**
	 * Returns value of OrigMinInvNoPatrn
	 * 
	 * @return Vector
	 */
	public Vector getOrigMinInvNoPatrn()
	{
		return cvOrigMinInvNoPatrn;
	}
	/**
	 * Returns value of ValidItmNo
	 * 
	 * @return boolean
	 */
	public boolean getValidItmNo()
	{
		return cbValidItmNo;
	}
	/**
	 * Returns value of EndInvNoPatrn
	 * 
	 * @param avEndInvNoPatrn Vector
	 */
	public void setEndInvNoPatrn(Vector avEndInvNoPatrn)
	{
		cvEndInvNoPatrn = avEndInvNoPatrn;
	}
	/**
	 * Returns value of InvAlloctnData
	 * 
	 * @param aaInvAlloctnData InventoryAllocationData
	 */
	public void setInvAlloctnData(InventoryAllocationData aaInvAlloctnData)
	{
		caInvAlloctnData = aaInvAlloctnData;
	}
	/**
	 * Returns value of InvNoPatrn
	 * 
	 * @param avInvNoPatrn Vector
	 */
	public void setInvNoPatrn(Vector avInvNoPatrn)
	{
		cvInvNoPatrn = avInvNoPatrn;
	}
	/**
	 * Returns value of InvPatrnsData
	 * 
	 * @param aaInvPatrnsData InventoryPatternsData
	 */
	public void setInvPatrnsData(InventoryPatternsData aaInvPatrnsData)
	{
		caInvPatrnsData = aaInvPatrnsData;
	}
	/**
	 * Returns value of MaxInvNoPatrn
	 * 
	 * @param avMaxInvNoPatrn Vector
	 */
	public void setMaxInvNoPatrn(Vector avMaxInvNoPatrn)
	{
		cvMaxInvNoPatrn = avMaxInvNoPatrn;
	}
	/**
	 * Returns value of MinInvNoPatrn
	 * 
	 * @param avMinInvNoPatrn Vector
	 */
	public void setMinInvNoPatrn(Vector avMinInvNoPatrn)
	{
		cvMinInvNoPatrn = avMinInvNoPatrn;
	}
	/**
	 * Returns value of OrigInvNoPatrn
	 * 
	 * @param avOrigInvNoPatrn Vector
	 */
	public void setOrigInvNoPatrn(Vector avOrigInvNoPatrn)
	{
		cvOrigInvNoPatrn = avOrigInvNoPatrn;
	}
	/**
	 * Returns value of OrigMaxInvNoPatrn
	 * 
	 * @param avOrigMaxInvNoPatrn Vector
	 */
	public void setOrigMaxInvNoPatrn(Vector avOrigMaxInvNoPatrn)
	{
		cvOrigMaxInvNoPatrn = avOrigMaxInvNoPatrn;
	}
	/**
	 * Returns value of OrigMinInvNoPatrn
	 * 
	 * @param avOrigMinInvNoPatrn Vector
	 */
	public void setOrigMinInvNoPatrn(Vector avOrigMinInvNoPatrn)
	{
		cvOrigMinInvNoPatrn = avOrigMinInvNoPatrn;
	}
	/**
	 * Returns value of ValidItmNo
	 * 
	 * @param abValidItmNo boolean
	 */
	public void setValidItmNo(boolean abValidItmNo)
	{
		cbValidItmNo = abValidItmNo;
	}
}
