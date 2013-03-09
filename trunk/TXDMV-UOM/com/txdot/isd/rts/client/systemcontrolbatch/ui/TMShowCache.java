package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * ShowCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		11/11/2002	Fixed CQU100004746. Modified getValueAt() 
 * 							to add Void as a valid transaction type.
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3
 * Jeff S.		07/12/2005	Added String and Integer Constants.
 * 							defect 7897 ver 5.2.3
 * K Harrell	06/18/2007	add UPDVITIME
 * 							modify getValueAt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/01/2008	Modify to meet TM Standards	
 * 							add RESETINPROCS, LOG_TIME, LOG_DATE, SQL, 
 * 							  AMDATE 
 * 							delete ZERO, ONE, TWO, THREE
 * 							add carrColumn_Name 					
 * 							modify getColumnCount(), getColumnName(),
 * 							  getValueAt() 
 * 							defect 9831 Ver Defect_POS_B  
 *  --------------------------------------------------------------------
 */
/**
 * The table model for the Show Cache screen
 * 
 * @version	Defect_POS_B 	11/01/2008 
 * @author	Michael Abernethy
 * <br>Creation Date: 		09/20/2001 13:32:43
 */
public class TMShowCache extends AbstractTableModel
{
	private Vector cvVector;
	
	// defect 9831 
	private final static String[] carrColumn_Name =
			{
				"Log Time",
				"Log Date",
				"SQL",
				"AM Date"};

	private static final int LOG_TIME = 0;
	private static final int LOG_DATE = 1;
	private static final int SQL = 2;
	private static final int AMDATE = 3;
	private static final String RESETINPROCS = " ResetInProcs ";
	// end defect 9831 
	
	private static final String EMPTY_STRING = "";
	private static final String PERIOD = ".";
	private static final String DELETE = " Delete ";
	private static final String INSERT = " Insert ";
	private static final String UPDATE = " Update ";
	private static final String VOID = " Void   ";
	private static final String UNKNOWN = " Unknown ";
	// defect 9085 
	private static final String UPDVITIME = " UpdVITime ";
	// end defect 9085 

	/**
	 * TMShowCache constructor.
	 */
	public TMShowCache()
	{
		super();
		cvVector = new Vector();
	}

	/**
	 * add
	 * 
	 * @param avV Vector
	 */
	public void add(Vector avV)
	{
		cvVector = avV;
		fireTableDataChanged();
	}

	/**
	 * getColumnCount - returns the number of columns
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		// defect 9831 
		return carrColumn_Name.length;
		// end defect 9831  
	}

	/**
	 * Returns the column name.
	 * 
	 * @param  aiCol	int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		// defect 9831 
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
		// end defect 9831 
	}

	/**
	 * getRowCount - returns the number of rows
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * getValueAt
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		RTSDate laTime =
			((TransactionCacheData) cvVector.get(aiRow)).getDateTime();
		TransactionCacheData laCacheData =
			(TransactionCacheData) cvVector.get(aiRow);
		String lsName =
			laCacheData.getObj().getClass().getName().substring(
				laCacheData.getObj().getClass().getName().lastIndexOf(
					PERIOD)
					+ 1);
					
		// defect 9831 
		switch (aiCol)
		{
			case LOG_TIME :
				{
					return laTime.getClockTime();
				}
			case LOG_DATE :
				{
					return laTime.toString();
				}
			case SQL :
				{
					int liSQL = laCacheData.getProcName();
					switch (liSQL)
					{
						case TransactionCacheData.DELETE :
							{
								return DELETE + lsName;
							}
						case TransactionCacheData.INSERT :
							{
								return INSERT + lsName;
							}
						case TransactionCacheData.UPDATE :
							{
								return UPDATE + lsName;
							}
							//defect 4746
						case TransactionCacheData.VOID :
							{
								return VOID + lsName;
							}
							// end defect 4746

							// defect 9085 
						case TransactionCacheData.UPDVITRANSTIME :
							{
								return UPDVITIME + lsName;
							}
							// end defect 9085
							// defect 9831 
						case TransactionCacheData.RESETINPROCESS :
							{
								return RESETINPROCS + lsName;
							}
							// end defect 9831 
						default :
							{
								return UNKNOWN + lsName;
							}
					}
				}
			case AMDATE :
				{
					return Integer.toString(laTime.getAMDate());
				}
			default :
				{
					return EMPTY_STRING;
				}
		}
		// end defect 9831 
	}
}
