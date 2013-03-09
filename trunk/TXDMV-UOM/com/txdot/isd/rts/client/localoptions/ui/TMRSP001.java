package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMRSP001.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang 	07/22/2004	New table model for screen RSP001
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/07/2004	Modify so that HostName shows with the
 *							office number.
 *							modify getValueAt()
 *							defect 7135 Ver 5.2.1
 * Min Wang		03/17/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang		09/06/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7891 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen RSP001. Provides methods to get the columns 
 * host name and last update for inserting rows into the table.
 * 
 * @version	5.2.3			09/30/2005
 * @author	Min Wang
 * <br>Creation Date:		07/22/2004 11:25:00
 */

public class TMRSP001 extends AbstractTableModel
{

	private Vector cvTblData = new Vector();
	public static String csTblType = new String();
	private final static String[] carrColumn_Name = 
		{"Host Name", "Last Update"};

	/**
	 * TMAPMT004 constructor comment.
	 */
	public TMRSP001()
	{
		super();
	}

	/**
	 * Adds the data to the table.
	 * 
	 * @param avData Vector
	 */
	public void add(Vector avData)
	{
		cvTblData = new Vector(avData);
		fireTableDataChanged();
	}

	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int 
	 */
	public int getColumnCount()
	{
		//return 2;
		return carrColumn_Name.length;
	}

	/**
	 * Specify the names of each column in the table.
	 *
	 * @param aiCol int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
	}

	/**
	 * Return the number of rows in the table.
	 * 
	 * @return int 
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}

	/**
	 * Returns the value at the selected spot.
	 *
	 * @param aiRow int  
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (cvTblData != null)
		{
			if (aiCol == 0)
			{
				// add ofc number back on to reflect true host name
				String lsHostName =
					UtilityMethods.addPadding(
						String.valueOf(
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.SystemProperty
								.getOfficeIssuanceNo()),
						3,
						CommonConstant.STR_ZERO)
						+ ((RSPSWsStatusData) cvTblData.get(aiRow))
							.getRSPSId();
				return (lsHostName);
			}
			else if (aiCol == 1)
			{
				return (
					((RSPSWsStatusData) cvTblData.get(aiRow))
						.getLastProcsdTimeStmp());
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
