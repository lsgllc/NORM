package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.VehicleSearchData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegistrationRenewalConstants;

/*
 *
 * TMREG102.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		8/01/2002	Added ability to arrow up and down through 
 * 							the registration records as soon as the data
 * 							is displayed in REG102. Changed 
 * 							getScrollPaneTable method to use table model
 * 							TMREG102, and changed populateReportsTable 
 * 							method to add to a cvTableMdlVector, with 
 * 							each cvTableMdlVector row (a 
 * 							VehicleSearchData object) loaded in the 
 * 							table that will be viewed in 
 * 							frmSearchResults (REG102).
 * 							add TMREG102, VehicleSearchData.
 * 							defect 4416 Ver
 * Jeff S.		02/22/2005	Get code to standard.
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		07/12/2005	Removed not used constants. 
 * 							Added new Constants for strings and Integers
 * 							delete YES, NO
 * 							defect 7889 Ver 5.2.3
 * Min Wang		06/14/2007	modify Constants
 * 							COLMN0, COLMN1, COLMN2, COLMN3
 * 							defect 8768 Ver Special Plates
 * K Harrell	05/29/2009	Further Class Cleanup/Standardization.
 * 							add carrColumn_Name
 * 							delete ZERO,ONE,TWO,THREE,FOUR,COLMN0,
 * 							 COLMN1,COLMN2,COLMN3,COLMN4,EMPTY_STRING,
 * 							 COLUMN_COUNT
 * 							modify getColumnCount(),getColumnName(),
 * 							 getRowCount(), getValueAt() 
 * 							defect 8749 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This is the table model for FrmSearchResultsREG102.
 *
 * @version	Defect_POS_F	06/14/2006
 * @author	B Brown			
 * <br>Creation Date:		08/02/2002 14:57:38
 */
public class TMREG102 extends AbstractTableModel
{
	// defect 8749 
	private final static String[] carrColumn_Name =
		{
			"Plate No",
			"Recipient Name",
			"Internet Date",
			"County Date",
			"Status" };
	// end defect 8749 
			
	private static final String SPACE = " ";
	
	private Vector cvRevisedObjects = null;
	private Vector cvTableMdlVector;

	/**
	 * SearchResultsTableModel constructor comment.
	 */
	public TMREG102()
	{
		super();
		cvTableMdlVector = new Vector();
		cvRevisedObjects = new Vector();
	}
	
	/**
	 * Add a cvTableMdlVector to the table to post rows.
	 * 
	 * @param avAddVector Vector
	 */
	public void add(Vector avAddVector)
	{
		cvTableMdlVector = new Vector(avAddVector);
		fireTableDataChanged();
	}

	/**
	 * Get the number of columns in the table.
	 * 
	 * @return	int
	 */
	public int getColumnCount()
	{
		// defect 8749
		return carrColumn_Name.length;
		// end defect 8749 
	}

	/**
	 * Get the column name at the specified location.
	 * 
	 * <p>Returns empty string if location is not defined.
	 * 
	 * @param aiCol int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		// defect 8749 
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
		// end defect 8749 
	}

	/**
	 * Return the number of rows in the Table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTableMdlVector.size();
	}

	/**
	 * Return values from the table
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		// defect 8749
		VehicleSearchData laDataObj =
			(VehicleSearchData) cvTableMdlVector.get(aiRow);

		switch (aiColumn)
		{
			case RegistrationRenewalConstants.REG102_COL_PLATE_NO :
				{
					return SPACE + laDataObj.getPlateNo();
				}
			case RegistrationRenewalConstants.REG102_COL_REC_NAME :
				{
					return SPACE + laDataObj.getName();
				}
			case RegistrationRenewalConstants.REG102_COL_ITRNT_DATE :
				{
					return SPACE + laDataObj.getRenewalDateTime();
				}
			case RegistrationRenewalConstants.REG102_COL_CNTY_DATE :
				{
					return SPACE + laDataObj.getCountyProcessedDate();
				}
			case RegistrationRenewalConstants.REG102_COL_STATUS :
				{
					return SPACE + laDataObj.getStatus();
				}
			default :
				{
					return null;
				}
		}
		// end defect 8749 
	}
}