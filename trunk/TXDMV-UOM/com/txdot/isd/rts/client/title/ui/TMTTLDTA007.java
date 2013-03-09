package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * TMTTLDTA007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Imported. Ver 5.2.0		
 * J Rue		07/20/2004	The path for print.gif and processed.gif
 *								was incorrect.
 *							method TMTTLDTA007()
 *							Defect 7308
 * J Rue		07/20/2004	Add print.gif and processed.gif were added 
 *							to eesources.
 *							method TMTTLDTA007()
 *							Defect 7238
 * J Rue		07/27/2004	Change verbage from Pro'd to Processed
 *							modify getColumnName()
 *							defect 7239 VER 5.2.1
 * J Rue		07/27/2004	Set method static variable to class level
 * 							method getColumnName()
 *							defect 7268 VER 5.2.1
 * J Rue		07/28/2004	Set icon to X and write message to log if
 *							 print.gif or processed.gif can not be found
 *							method TMTTLDTA007
 *							defect 7270 VER 5.2.1
 * J Rue		03/22/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning.
 * 							Comment out unused variables
 * 							modify getValueAt()
 * 							defect 8217 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * B Hargrove	03/12/2009	Add column for 'Electronic Title' indicator.
 * 							(ETtlRqst).
 * 							Use title constants for table columns.
 * 							modify getColumnName()
 * 							defect 9977 Ver Defect_POS_E
 * K Harrell	12/16/2009	DTA Cleanup
 * 							delete COLUMN_COUNT
 * 							modify getValueAt(), getColumnCount()   
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	10/04/2010	Use "E" for ETitle Column 
 * 							modify getValueAt() 
 * 							defect 10013 Ver 6.6.0 
 *----------------------------------------------------------------------
*/

/**
* Table Model for DTA007
*  
* @version	6.6.0 			10/04/2010
* @author	Ashish Mahajan
* <br>Creation Date:		08/29/2001 
*/
public class TMTTLDTA007 extends javax.swing.table.AbstractTableModel
{
	// defect 9977
	// add Electronic Title column
	private final static String ETITLE = "ETitle";
	// end defect 9977	
	private final static String FEE_CALC = "Fee Calculated By Dealer";
	private final static String FORM31_NUM = "Form 31 Number";
	private final static String PRINT_GIF = "/images/print.gif";
	private final static String PRINTED = "Printed";
	private final static String PROCESSED = "Processed";
	private final static String PROCESSED_GIF = "/images/processed.gif";
	private final static String X = "X";

	// defect 9977
	private final static String[] carrColumn_Name =
		{ PRINTED, PROCESSED, ETITLE, FORM31_NUM, FEE_CALC };
	// end defect 9977 

	private Vector cvVector;

	private javax.swing.ImageIcon caPrintedIcon;
	private javax.swing.ImageIcon caProcessedIcon;

	private final static String ERROR_CLASS =
		"com.txdot.isd.rts.client.title.ui.TMTTLDTA007";
	private final static String ERROR_MSG1 =
		"Error: PRINT.GIF or PROCESSED.GIF could not be found";

	/**
	 * TMTTLDTA007 Constructor 
	 */
	public TMTTLDTA007()
	{
		super();

		// defect 7270
		// Set icon to X and write message to log if
		//	 print.gif or processed.gif can not be found.
		try
		{
			// defect 7238, 7308
			//  Images not showing up because of missing print.gif and 
			//	processed.gif
			//  ("images/print.gif") should be ("/images/print.gif") and
			//  ("/mages/processed.gif") should be 
			//		("/images/processed.gif")
			caPrintedIcon =
				new javax.swing.ImageIcon(
					getClass().getResource(PRINT_GIF));
			caProcessedIcon =
				new javax.swing.ImageIcon(
					getClass().getResource(PROCESSED_GIF));
		}
		catch (NullPointerException aeNPEx)
		{
			com.txdot.isd.rts.services.util.Log.write(
				Log.START_END,
				this,
				ERROR_CLASS
					+ CommonConstant.STR_SPACE_THREE
					+ aeNPEx
					+ CommonConstant.STR_SPACE_THREE
					+ ERROR_MSG1);
		}
		cvVector = new Vector();
	}

	/**
	 * Updates the table data with the new data in the vector
	 * 
	 * @param avVector Vector the new data
	 */
	public void add(Vector avVector)
	{
		cvVector = avVector;
		fireTableDataChanged();
	}

	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		// defect 10290 
		return carrColumn_Name.length;
		// end defect 10290 
	}

	/**
	 * Returns the column name.
	 * 
	 * @param aiCol int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		return carrColumn_Name[aiCol];
	}

	/**
	 * Returns the number of rows.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Returns the value in the table.
	 * 
	 * @param aiRow int
	 * @param aiCloumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		// defect 10290 
		// Use laDlrTtlData 
		DealerTitleData laDlrTtlData =
			(DealerTitleData) cvVector.get(aiRow);

		Object lbReturnObj = CommonConstant.STR_SPACE_EMPTY;

		switch (aiColumn)
		{
			case TitleConstant.DTA007_COL_PRINTED :
				{
					if (laDlrTtlData.getRSPSPrntInvQty()
						+ laDlrTtlData.getPosPrntInvQty()
						!= 0)
					{
						if (caPrintedIcon != null)
						{
							lbReturnObj = caPrintedIcon;
						}
						else
						{
							lbReturnObj = X;
						}
					}
					break;
				}
			case TitleConstant.DTA007_COL_PROCESSED :
				{
					if (laDlrTtlData.isPOSProcsIndi())
					{
						if (caProcessedIcon != null)
						{
							lbReturnObj = caProcessedIcon;
						}
						else
						{
							lbReturnObj = X;
						}
					}
					break;
				}
				// defect 9977
				// Add Electronic Title column
			case TitleConstant.DTA007_COL_ETITLE :
				{
					if (laDlrTtlData.isETtlRqst())
					{
						// defect 10013 
						// lbReturnObj = X;
						lbReturnObj = TitleConstant.ETITLE_SYMBOL;
						// end defect 10013 
					}
					break;
				}
				// end defect 9977
			case TitleConstant.DTA007_COL_FORM31 :
				{
					if (laDlrTtlData.getForm31No() != null)
					{
						lbReturnObj = laDlrTtlData.getForm31No();
					}
					break;
				}
			case TitleConstant.DTA007_COL_FEES :
				{
					if (!laDlrTtlData.isSkipCurrObj())
					{
						if (laDlrTtlData.getFee() != null)
						{
							lbReturnObj =
								laDlrTtlData.getFee()
									+ CommonConstant.STR_SPACE_TWO;
							// end defect 10290 
						}
						else
						{
							lbReturnObj =
								CommonConstant.STR_ZERO_DOLLAR
									+ CommonConstant.STR_SPACE_TWO;
						}
					}
					break;
				}
		}
		return lbReturnObj;
	}
}