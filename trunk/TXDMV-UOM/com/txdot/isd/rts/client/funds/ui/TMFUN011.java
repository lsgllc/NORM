package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.EmployeeData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * TMFUN011.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/30/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	06/08/2009	Implement FundsConstant
 * 							modify getValueAt()
 * 							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen FUN011.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMFUN011 extends javax.swing.table.AbstractTableModel
{
	private final static String[] carrColumn_Name =
		{ "Employee Id", "Last Name", "First Name" };

	private Vector cvVector;

	/**
	 * TMFUN011 constructor comment.
	 */
	public TMFUN011()
	{
		super();
		cvVector = new Vector();
	}
	
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}
	
	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}
	
	/**
	 * Specify the names of each column in the table.
	 * 
	 * @param aiColumn int
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
	 * Return the number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	
	/**
	 * Return values from the table
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		// defect 9943
		//if (aiCol == 0)		
		if (aiCol == FundsConstant.FUN011_EMPLOYEE_ID)
		{
			return new String(
				((EmployeeData) cvVector.get(aiRow)).getEmployeeId());

		}
		//else if (aiCol == 1)		
		else if (aiCol == FundsConstant.FUN011_LAST_NAME)
		{
			return ((EmployeeData) cvVector.get(aiRow)).getLastName();
		}
		//else if (aiCol == 2)		
		else if (aiCol == FundsConstant.FUN011_FIRST_NAME)
		{
			return ((EmployeeData) cvVector.get(aiRow)).getFirstName();
		}
		// end defect 9943 
		
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
