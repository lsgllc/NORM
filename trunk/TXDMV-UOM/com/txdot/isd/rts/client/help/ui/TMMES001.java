package com.txdot.isd.rts.client.help.ui;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JLabel;

import com.txdot.isd.rts.services.data.CustomTableData;
import com.txdot.isd.rts.services.data.ImageData;
import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMMES001.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/03/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * Jeff S.		06/28/2007	Changes getValueAt() to always return Custom
 * 							Table Data and not return just string so 
 * 							that the allignments will work the same as
 * 							the rest of the table.
 * 							add caNormalFont
 * 							modify getValueAt()
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */
 
/**
 * Table Model for MES001
 * 
 * @version	Broadcast Message	06/28/2007
 * @author	Jeff S.
 * <br>Creation Date:			03/20/2006 12:38.00 
 */
public class TMMES001 extends javax.swing.table.AbstractTableModel
{
	private final static Font caBoldFont =
		new java.awt.Font("Dialog", java.awt.Font.BOLD, 12);
	private final static Font caNormalFont =
		new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);

	private final static String[] carrColumn_Name =
		{ "", " ", "From", "Subject", "Date" };
	private final static String MAIL_NEW_IMG = "mail-new";
	private final static String MAIL_READ_IMG = "mail-read";
	private final static String MAIL_REPLIED_IMG = "mail-replied";

	private final static String MSG_ERR0R = "error";
	// ImageData adds the default image type to the end (.gif)
	private final static String PRIORITY_HIGH_IMG = "priority-high";
	private final static String PRIORITY_LOW_IMG = "priority-low";

	private Vector cvVector;

	/**
	 * TMMES001 constructor.
	 */
	public TMMES001()
	{
		super();
		cvVector = new Vector();
	}
	
	/**
	 * Add data.
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}
	
	/**
	 * Get the number of columns of the table
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}
	
	/**
	 * Get the name of each column based on the column index
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
	 * Returns the object that represents the row.
	 * 
	 * @param aiRow
	 * @return
	 */
	public Object getRow(int aiRow)
	{
		return cvVector.get(aiRow);
	}
	
	/**
	 * Get number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	
	/**
	 * Get the column value of a particular row and column
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		MessageData laMessageData = ((MessageData) cvVector.get(aiRow));

		switch (aiCol)
		{
			case 0 :
				{
					if (laMessageData.isHighPriority())
					{
						return new ImageData(
							PRIORITY_HIGH_IMG,
							JLabel.CENTER,
							JLabel.CENTER);
					}
					else
					{
						return new ImageData(
							PRIORITY_LOW_IMG,
							JLabel.CENTER,
							JLabel.CENTER);
					}
				}
			case 1 :
				{
					if (laMessageData.isOpened())
					{
						if (laMessageData.isReplied())
						{
							return new ImageData(
								MAIL_REPLIED_IMG,
								JLabel.CENTER,
								JLabel.CENTER);
						}
						else
						{
							return new ImageData(
								MAIL_READ_IMG,
								JLabel.CENTER,
								JLabel.CENTER);
						}
					}
					else
					{
						return new ImageData(
							MAIL_NEW_IMG,
							JLabel.CENTER,
							JLabel.CENTER);
					}
				}
			case 2 :
				{
					String lsFrom =
						laMessageData.getFrom().substring(
							0,
							laMessageData.getFrom().indexOf(
								CommonConstant.STR_AT));

					if (!laMessageData.isOpened())
					{
						return new CustomTableData(lsFrom, caBoldFont);
					}
					else
					{
						return new CustomTableData(
							lsFrom,
							caNormalFont);
					}

				}
			case 3 :
				{
					if (!laMessageData.isOpened())
					{
						return new CustomTableData(
							laMessageData.getSubject(),
							caBoldFont);
					}
					else
					{
						return new CustomTableData(
							laMessageData.getSubject(),
							caNormalFont);
					}
				}
			case 4 :
				{
					String lsDate =
						laMessageData.getDate()
							+ CommonConstant.STR_SPACE_ONE
							+ laMessageData.getDate().getTime();
					if (!laMessageData.isOpened())
					{
						return new CustomTableData(lsDate, caBoldFont);
					}
					else
					{
						return new CustomTableData(
							lsDate,
							caNormalFont);
					}
				}
			default :
				{
					return MSG_ERR0R;
				}
		}
	}
}