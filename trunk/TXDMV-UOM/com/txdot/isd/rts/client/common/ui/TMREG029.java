package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMREG029.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown &	04/26/2002	CQU100003680 - Line up total amounts in 
 * Robin Taylor             getValueAt method with detail amounts. 
 * B Hargrove	03/16/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * T Pederson	11/08/2005	Add constant, use common constants 
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for REG029
 * 
 * @version	5.2.3		03/16/2005
 * @author	Nancy Ting
 * <br>Creation Date:	09/13/2001 15:24:38
 */

public class TMREG029 extends TMPMT004
{
	// defect 7885
	private final static String ERROR = "error";
	// end defect 7885

	/**
	 * TMAPMT004 constructor comment.
	 */
	public TMREG029()
	{
		super();
	}
	
	/**
	 * Get the value at a specific row/column
	 *  
	 * @param aiRow
	 * @param aiCol 
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{

		if (aiCol == 0)
			return CommonConstant.STR_SPACE_ONE + 
				((FeesData) cvVector.get(aiRow)).getDesc();
		else if (aiCol == 1)
			return ((FeesData) cvVector.get(aiRow))
				.getItemPrice()
				.printDollar()
				+ CommonConstant.STR_SPACE_ONE;
		return ERROR;

	}
}
