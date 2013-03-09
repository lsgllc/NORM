package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * VehMkListModel.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
  * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3                  
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * ------------------------------------------------------------------
 */

/**
 * Insert the type's description here.
 *
 * @version	5.2.3			08/23/2005
 * @author	Ashish Mahajan
 * <br>Creation Date: 		10/19/2001 16:27:33 
 */

public class VehMkListModel implements javax.swing.ComboBoxModel
{
	private Vector cvVehMk = null;
	private Object caObjSel = null;
	/**
	 * VehMkListModel constructor comment.
	 */
	public VehMkListModel()
	{
		super();
	}
	/**
	  * Add a listener to the list that's notified each time a change
	  * to the data model occurs.
	  * 
	  * @param aaLDL ListDataListener
	  */
	public void addListDataListener(
		javax.swing.event.ListDataListener aaLDL)
	{
		// Empty code block
	}
	/**
	 * getElementAt method comment.
	 * 
	 * @param aiaArg1 int
	 */
	public Object getElementAt(int aiArg1)
	{
		if (cvVehMk != null)
		{
			VehicleMakesData laData =
				(VehicleMakesData) cvVehMk.get(aiArg1);
			String lsDesc =
				laData.getVehMk() 
				+ CommonConstant.STR_SPACE_ONE
				+ CommonConstant.STR_DASH
				+ laData.getVehMkDesc();

			return lsDesc;
		}

		return null;
	}
	/** Return the selected item **/
	public Object getSelectedItem()
	{
		return caObjSel;
	}
	/**
	 * getSize method comment.
	 * 
	 * @return int
	 */
	public int getSize()
	{
		if (cvVehMk != null)
		{
			return cvVehMk.size();
		}

		return 0;
	}
	/**
	  * Remove a listener from the list that's notified each time a 
	  * change to the data model occurs.
	  * 
	  * @param aaLDL ListDataListener
	  */
	public void removeListDataListener(
		javax.swing.event.ListDataListener aaLDL)
	{
		// Empty code block
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param avData Vector
	 */
	public void setData(Vector avData)
	{
		cvVehMk = avData;

	}
	/** 
	 * Set the selected item 
	 * 
	 * @param aaItem Object
	 */
	public void setSelectedItem(Object aaItem)
	{
		caObjSel = aaItem;

	}
}
