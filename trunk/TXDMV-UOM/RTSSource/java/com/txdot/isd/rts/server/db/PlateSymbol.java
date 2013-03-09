package com.txdot.isd.rts.server.db;import java.sql.ResultSet;import java.sql.SQLException;import java.util.Vector;import com.txdot.isd.rts.services.data.PlateSymbolData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.Log;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;/* * PlateSymbol.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	03/24/2010	Created * 							defect 10366 Ver POS_640 	 * --------------------------------------------------------------------- *//** * This class allows the user to access RTS_PLT_SYM * * @version	POS_640			03/24/2010 * @author	Kathy Harrell * <br>Creation Date:		03/24/2010 12:31:00 *//* &PlateSymbol& */public class PlateSymbol {/* &PlateSymbol'caDA& */	private DatabaseAccess caDA;/* &PlateSymbol'csMethod& */	private String csMethod = new String();	/**	 * PlateSymbol constructor comment.	 *	 * @param  aaDA  DatabaseAccess	 * @throws RTSException	 *//* &PlateSymbol.PlateSymbol& */	public PlateSymbol(DatabaseAccess aaDA) throws RTSException	{		caDA = aaDA;	}	/**	 * Method to Query RTS.RTS_PLT_SYM	 * 	 * @return Vector	 * @throws RTSException	 *//* &PlateSymbol.qryPlateSymbol& */	public Vector qryPlateSymbol() throws RTSException	{		csMethod = "qryPlateSymbol";		Log.write(Log.METHOD, this, csMethod = " - Begin");		StringBuffer lsQry = new StringBuffer();		Vector lvRslt = new Vector();		ResultSet lrsQry;		lsQry.append(			"SELECT "				+ "PltSymCd,"				+ "SymCharLngth,"				+ "PltSymDesc "				+ "FROM "				+ "RTS.RTS_PLT_SYM");		try		{			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);			Log.write(Log.SQL, this, csMethod + " - SQL - End");			while (lrsQry.next())			{				PlateSymbolData laPltSymData = new PlateSymbolData();				laPltSymData.setPltSymCd(					caDA.getStringFromDB(lrsQry, "PltSymCd"));				laPltSymData.setSymCharLngth(					caDA.getIntFromDB(lrsQry, "SymCharLngth"));				laPltSymData.setPltSymDesc(					caDA.getStringFromDB(lrsQry, "PltSymDesc"));				lvRslt.addElement(laPltSymData);			} //End of While			lrsQry.close();			caDA.closeLastDBStatement();			lrsQry = null;			Log.write(Log.METHOD, this, csMethod + " - End ");			return (lvRslt);		}		catch (SQLException aeSQLEx)		{			Log.write(				Log.SQL_EXCP,				this,				csMethod + " - SQL Exception " + aeSQLEx.getMessage());			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);		}		catch (RTSException aeRTSEx)		{			Log.write(				Log.SQL_EXCP,				this,				csMethod + " - Exception - " + aeRTSEx.getMessage());			throw aeRTSEx;		}	} //END OF QUERY METHOD}/* #PlateSymbol# */