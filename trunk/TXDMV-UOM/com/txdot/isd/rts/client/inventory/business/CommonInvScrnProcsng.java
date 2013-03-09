package com.txdot.isd.rts.client.inventory.business;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.InventoryPatternsData;
import com.txdot.isd.rts.services.data.ValidateInventoryPattern;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * CommonInvScrnProcsng.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/18/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3   
 * Ray Rowehl	07/16/2005	Constants work
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/06/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Use constants to define error numbers.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/16/2005	Move error message numbers.
 * 							defect 7890 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
 
/**
 * Contains methods that handle the red field validation on screens 
 * INV012, INV013, INV006, INV015, INV011, and INV018 using the 
 * exceptions thrown from the database.
 * 
 * @version	5.2.3		08/16/2005
 * @author	Charlie Walker
 * <br>Creation Date:	02/11/2002 17:23:24
 */

public class CommonInvScrnProcsng
{
	/**
	 * CommonInvScrnProcsng constructor comment.
	 */
	public CommonInvScrnProcsng()
	{
		super();
	}

	/**
	 * Takes the exception from the server business layer and determines 
	 * which field on the frame should change color and get focus.
	 *
	 * @param aaFrame RTSDialogBox  
	 * @param ahtFrmComponents Hashtable 
	 * @param aeRTSExcptn RTSException 
	 */
	public void hndlServerExcptns(
		RTSDialogBox aaFrame,
		Hashtable ahtFrmComponents,
		RTSException aeRTSExcptn)
	{
		RTSException leRTSExHldr = new RTSException();

		if (aeRTSExcptn.getCode()
			== ErrorsConstant.ERR_NUM_COMPUTED_NUMBER_INVALID
			|| aeRTSExcptn.getCode()
				== ErrorsConstant.ERR_NUM_INVALID_QTY)
		{
			Object laObj = ahtFrmComponents.get(InventoryConstant.QTY);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(aeRTSExcptn, laComponent);
		}
		else if (
			aeRTSExcptn.getCode()
				== ErrorsConstant.ERR_NUM_ITM_CD_YR_NOT_IN_PATTERNS
				|| aeRTSExcptn.getCode()
					== ErrorsConstant.ERR_NUM_ITM_YEAR_NOT_VALID)
		{
			Object laObj = ahtFrmComponents.get(InventoryConstant.YR);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(aeRTSExcptn, laComponent);
		}
		else if (
			aeRTSExcptn.getCode()
				== ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE)
		{
			Object laObj =
				ahtFrmComponents.get(InventoryConstant.BEG_NO);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(aeRTSExcptn, laComponent);
		}
		else
		{
			aeRTSExcptn.displayError(aaFrame);
		}

		if (leRTSExHldr.isValidationError())
		{
			leRTSExHldr.displayError(aaFrame);
			leRTSExHldr.getFirstComponent().requestFocus();
		}
	}

	/**
	 * Takes the data from the screen and validates the data.  These 
	 * checks are duplicates of what happens in the inventory server 
	 * business layer, but they are necessary to make the screen behave
	 * exactly as the RTSI system.
	 *
	 * <p>Returns False if there aren't any exceptions. 
	 * <br>Returns True if there were validation exceptions.
	 * 
	 * <p>Paramter aaInvAllctnUIData of type InventoryAllocationUIData 
	 * is the inventory number that needs to be validated.  
	 * <br>The required fields are:
	 * <ul>
	 * <ul>ItmCd - The item code for the inventory
	 * <ul>InvItmYr - The inventory year
	 * <ul>InvItmNo - The inventory begin number of the item range
	 * <eul>
	 * <p>In addition to these required fields, one of the 
	 * following fields need to be set
	 * <ul>
	 * <ul>InvQty - The quantity
	 * <ul>InvItmEndNo - The inventory end number of the item range
	 * <eul>
	 *  
	 * @param aaFrame RTSDialogBox 
	 * @param ahtFrmComponents Hashtable 
	 * @param aaInvAllctnUIData InventoryAllocationUIData 
	 * @return boolean 
	 */
	public boolean invScrnValidation(
		RTSDialogBox aaFrame,
		Hashtable ahtFrmComponents,
		InventoryAllocationUIData aaInvAllctnUIData)
	{
		String lsItmCd = aaInvAllctnUIData.getItmCd();
		int liYr = aaInvAllctnUIData.getInvItmYr();
		int liQty = aaInvAllctnUIData.getInvQty();
		String lsBegNo = aaInvAllctnUIData.getInvItmNo();
		String lsEndNo = aaInvAllctnUIData.getInvItmEndNo();
		RTSException leRTSExHldr = new RTSException();

		// Test if the year is valid
		Vector lvInvPatrnsData =
			InventoryPatternsCache.getInvPatrns(
				lsItmCd,
				InventoryPatternsCache.NO_YEAR);
		if (lvInvPatrnsData != null)
		{
			for (int i = 0; i < lvInvPatrnsData.size(); i++)
			{
				InventoryPatternsData laIPD =
					(InventoryPatternsData) lvInvPatrnsData.get(i);
				if (laIPD.getInvItmYr() == liYr)
				{
					break;
				}
				else if (i == lvInvPatrnsData.size() - 1)
				{
					Object laObj =
						ahtFrmComponents.get(InventoryConstant.YR);
					RTSInputField laComponent = (RTSInputField) laObj;
					leRTSExHldr.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_ITM_YEAR_NOT_VALID),
						laComponent);
				}
			}
		}

		// Test if the quantity is valid
		if ((liQty == 0
			&& lsEndNo.equals(CommonConstant.STR_SPACE_EMPTY))
			|| (liQty <= InventoryConstant.MIN_QTY
				&& liQty > InventoryConstant.MAX_QTY))
		{
			Object laObj = ahtFrmComponents.get(InventoryConstant.QTY);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(
				new RTSException(ErrorsConstant.ERR_NUM_INVALID_QTY),
				laComponent);
		}

		// Test if the begin number is valid
		if (lsBegNo.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			Object laObj =
				ahtFrmComponents.get(InventoryConstant.BEG_NO);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE),
				laComponent);
		}
		else
		{
			try
			{
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				laVIP.validateItmNoInput(aaInvAllctnUIData);
			}
			catch (RTSException aeRTSEx)
			{
				Object laObj =
					ahtFrmComponents.get(InventoryConstant.BEG_NO);
				RTSInputField laComponent = (RTSInputField) laObj;
				leRTSExHldr.addException(aeRTSEx, laComponent);
			}
		}

		// Test if the end number is valid
		if (lsEndNo.equals(CommonConstant.STR_SPACE_EMPTY)
			&& lsBegNo.equals(CommonConstant.STR_SPACE_EMPTY)
			&& liQty == 0)
		{
			Object laObj =
				ahtFrmComponents.get(InventoryConstant.END_NO);
			RTSInputField laComponent = (RTSInputField) laObj;
			leRTSExHldr.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE),
				laComponent);
		}
		else if (!lsEndNo.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			try
			{
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) UtilityMethods.copy(
						aaInvAllctnUIData);
				laIAUID.setInvItmNo(lsEndNo);
				laVIP.validateItmNoInput(laIAUID);
			}
			catch (RTSException aeRTSEx)
			{
				Object laObj =
					ahtFrmComponents.get(InventoryConstant.END_NO);
				RTSInputField laComponent = (RTSInputField) laObj;
				leRTSExHldr.addException(aeRTSEx, laComponent);
			}
		}

		if (leRTSExHldr.isValidationError())
		{
			leRTSExHldr.displayError(aaFrame);
			leRTSExHldr.getFirstComponent().requestFocus();
			return true;
		}
		return false;
	}
}
