package com.txdot.isd.rts.client.registration.ui;

import java.util.SortedMap;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ImageData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMREG007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/26/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify add(), getColumnCt(), 
 *							 getColumnName(),getValueAt()
 * 							Ver 5.2.0
 * K Harrell	08/17/2004	Modify column headers for list
 *							modify getColumnName()
 *							defect 7455  Ver 5.2.1
 * K Harrell	08/21/2004	Do not show PltCd unless issue new plate
 *							modify getValueAt()
 *							defect    Ver 5.2.1
 * K Harrell	04/28/2005	Java 1.4 Work
 * 							defect 8020  Ver 5.2.3 
 * K Harrell	05/19/2005	Java 1.4 Work
 * 							Use ImageData constants DISK_ERROR, MANUAL,
 * 							PRINT vs. get methods
 * 							modify getValueAt()
 * 							defect 7899  Ver 5.2.3 
 * B Hargrove	09/28/2005	Modify for move to Java 1.4. Bring code to
 * 							standards. Format, Hungarian notation, etc. 
 * 							defect 7894 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for REG007
 * 
 * @version	5.2.3		09/28/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/22/2001 
 */
public class TMREG007 extends javax.swing.table.AbstractTableModel
{
	
 	private Vector cvSubconRewnwlData;
 	
	private final static String MSG_ERR0R = "error";
	private final static String SPACE10 = "          ";
	
	private final static String[] carrColumn_Name = 
		{"Input", "Print", "Issue Date", "Year", "Plt Cd", "New Plt No", 
		 "Stkr Cd", "Curr Plt No", "  Exp ", "Fee"};
	
	/**
	 * TMREG007 constructor.
	 */
	public TMREG007()
	{
		super();
		cvSubconRewnwlData = new Vector();
	}
	/**
	 * Add the data to the table model
	 * 
	 * @param aaSortedMap  SortedMap
	 */
	public void add(SortedMap aaSortedMap)
	{
		if (aaSortedMap != null)
		{
			//		PCR 34
			//public void add(Vector aVector) {
			//    if (aVector != null) {
			//    
			cvSubconRewnwlData.clear();
			cvSubconRewnwlData = new Vector(aaSortedMap.values());
			// End PCR 34
			//        for (int i = 0; i < aVector.size(); i++) {
			//            SubcontractorRenewalData lSubcontractorRenewalData =
			//                (SubcontractorRenewalData) aVector.get(i);
			//            cvSubconRewnwlData.addElement(lSubcontractorRenewalData);
		}
		else
		{
			cvSubconRewnwlData.clear();
		}
		fireTableDataChanged();
	}
	/**
	 * Get the number of columns of the table
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		// PCR 34
		// return 7;
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
	 * Get number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvSubconRewnwlData.size();
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
		SubcontractorRenewalData laSubcontractorRenewalData =
			(SubcontractorRenewalData) cvSubconRewnwlData.get(aiRow);
		//switch (aCol) {
		//    case 0 :
		//    	String lStr = new RTSDate(
		//            RTSDate.YYYYMMDD,
		//            lSubcontractorRenewalData.getSubconIssueDate())
		//            .toString();
		//        return new ImageData(ImageData.cCheck, lStr);
		//    case 1 :
		//    	return String.valueOf(lSubcontractorRenewalData.getNewExpYr());
		//    case 2:
		//        return lSubcontractorRenewalData.getNewPltNo();
		//    case 3 :
		//        return lSubcontractorRenewalData.getNewStkrNo();
		//    case 4 :
		//       return lSubcontractorRenewalData.getRegPltNo();
		//    case 5 :
		//        return UtilityMethods.addPadding(
		//            new String[] {
		//                String.valueOf(lSubcontractorRenewalData.getRegExpMo()),
		//                "/",
		//                String.valueOf(lSubcontractorRenewalData.getNewExpYr()-1)},
		//            new int[] { 2, 1, 4 },
		//            "0");
		//    case 6 :
		//        return lSubcontractorRenewalData.getRenwlTotalFees().toString();
		//    default :
		//        return "error";
		switch (aiCol)
		{
			// defect 7899 
			// use ImageData constants DISK_ERROR, MANUAL, PRINT
			// vs. get methods 
			case 0 :
			{
				if (laSubcontractorRenewalData.isError())
				{
					return new ImageData(ImageData.DISK_ERROR);
				}
				else
				{
					if (laSubcontractorRenewalData.isDiskEntry())
					{
						return new ImageData(ImageData.DISK, 
							CommonConstant.STR_SPACE_EMPTY);
					}
					else
					{
						return new ImageData(ImageData.MANUAL,
							CommonConstant.STR_SPACE_EMPTY);
					}
				}
			}
			case 1 :
			{
				if (laSubcontractorRenewalData.isPrint())
				{
					return new ImageData(ImageData.PRINT, 
						CommonConstant.STR_SPACE_EMPTY);
				}
				else
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
				// end defect 7899
			} 
			case 2 :
			{
				return new RTSDate(
					RTSDate.YYYYMMDD,
					laSubcontractorRenewalData.getSubconIssueDate())
					.toString();
			}
			case 3 :
			{
				return String.valueOf(
					laSubcontractorRenewalData.getNewExpYr());
			}
			case 4 :
			{
				if (laSubcontractorRenewalData.getNewPltNo() != null
					&& !laSubcontractorRenewalData
						.getNewPltNo()
						.trim()
						.equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					return laSubcontractorRenewalData.getPltItmCd();
				}
				else
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
			}
			case 5 :
			{
				return laSubcontractorRenewalData.getNewPltNo();
			}
			case 6 :
			{
				return laSubcontractorRenewalData.getStkrItmCd();
			}
			case 7 :
			{
				return laSubcontractorRenewalData.getRegPltNo();
			}
			case 8 :
			{
				return UtilityMethods.addPadding(
					new String[] {
						String.valueOf(
							laSubcontractorRenewalData.getRegExpMo()),
						CommonConstant.STR_SLASH,
						String.valueOf(
							laSubcontractorRenewalData.getNewExpYr()
								- 1)},
					new int[] { 2, 1, 4 },
					CommonConstant.STR_ZERO);
			}
			case 9 :
			{
				return SPACE10
					+ laSubcontractorRenewalData
						.getRenwlTotalFees()
						.toString();
			}
			default :
			{
				return MSG_ERR0R;
			}
		}
	}
}
