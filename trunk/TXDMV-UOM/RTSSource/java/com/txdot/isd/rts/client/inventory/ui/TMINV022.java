package com.txdot.isd.rts.client.inventory.ui;import java.util.Vector;import javax.swing.table.AbstractTableModel;import com.txdot.isd.rts.services.data.InventoryPatternsDescriptionData;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.InventoryConstant;/* * * TMINV022.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup * 							rename fields * 							defect 7890 Ver 5.2.3 * Min Wang		08/01/2005	Remove Item code from table. * 							modify getColumnCount(), getColumnName()  * 							getValueAt() * 							defect 8269 Ver 5.2.2 Fix 6   * Ray Rowehl	08/13/2005	Cleanup pass. * 							Add white space between methods. * 							Work on constants. * 							defect 7890 Ver 5.2.3 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings. * 							defect 7890 Ver 5.2.3 * B Hargrove	09/29/2005	Re-do column name handling using array. * 							defect 7890 Ver 5.2.3	   * --------------------------------------------------------------------- *//** * Table Model for INV022. *  * @version	5.2.3		09/29/2005 * @author	Charlie Walker * <br>Creation Date:	12/01/2001 15:47:03 *//* &TMINV022& */public class TMINV022 extends AbstractTableModel{	//private static final int COLUMN_COUNT = 2;/* &TMINV022'carrColumn_Name& */	private final static String[] carrColumn_Name = 		{InventoryConstant.TXT_ITEM_DESCRIPTION, 		 InventoryConstant.TXT_ITEM_YEAR};/* &TMINV022'cvTblData& */	private Vector cvTblData = new Vector();	/**	 * TMINV022 constructor comment.	 *//* &TMINV022.TMINV022& */	public TMINV022()	{		super();	}	/**	 * Add data to the table model.	 * 	 * @param avDataIn Vector	 *//* &TMINV022.add& */	public void add(Vector avDataIn)	{		cvTblData = new Vector(avDataIn);		fireTableDataChanged();	}	/**	 * Get the Column Count.	 * 	 * @return int	 *//* &TMINV022.getColumnCount& */	public int getColumnCount()	{		//return COLUMN_COUNT;		return carrColumn_Name.length;	}	/**	 * Get the column name at the specified location.	 * 	 * <p>Returns empty string if the location is not defined.	 * 	 * @param aiCol int	 * @return String	 *//* &TMINV022.getColumnName& */	public String getColumnName(int aiCol)	{		if (aiCol >= 0 && aiCol < carrColumn_Name.length)		{			return carrColumn_Name[aiCol];		}		else		{			return CommonConstant.STR_SPACE_EMPTY;		}	}	/**	 * Get the Row Count.	 * 	 * @return int	 *//* &TMINV022.getRowCount& */	public int getRowCount()	{		return cvTblData.size();	}	/**	 * Get the value at the specified location.	 * 	 * <p>Return empty string is the location is not specified.	 * 	 * @param aiRow int	 * @param aiColumn int	 * @return Object	 *//* &TMINV022.getValueAt& */	public Object getValueAt(int aiRow, int aiColumn)	{		if (cvTblData != null)		{			switch (aiColumn)			{				case 0 :					{						return (							(								InventoryPatternsDescriptionData) cvTblData									.get(								aiRow))							.getItmCdDesc();					}				case 1 :					{						int liYr =							(								(InventoryPatternsDescriptionData) cvTblData								.get(aiRow))								.getInvItmYr();						if (liYr == 0)						{							return new String(								CommonConstant.STR_SPACE_ONE);						}						else						{							return String.valueOf(liYr);						}					}				default :					{						break;					}			}		}		return CommonConstant.STR_SPACE_EMPTY;	}}/* #TMINV022# */