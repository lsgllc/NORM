package com.txdot.isd.rts.server.db;import java.sql.ResultSet;import java.sql.SQLException;import java.util.Vector;import com.txdot.isd.rts.services.data.TitleTransferEntityData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.Log;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;/* * TitleTransferEntity.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	04/02/2008	Created * 							defect 9583 Ver Defect POS A  * --------------------------------------------------------------------- *//** * This class contains methods to access RTS_TTL_TRNSFR_ENT * * @version	Defect POS A		04/02/2008 * @author	K Harrell * <br>Creation Date:			04/02/2008	17:23:00 *//* &TitleTransferEntity& */public class TitleTransferEntity{/* &TitleTransferEntity'caDA& */	DatabaseAccess caDA;	/**	 * TitleTransferEntity constructor comment.	 *	 * @param  aaDA DatabaseAccess	 * @throws RTSException	 *//* &TitleTransferEntity.TitleTransferEntity& */	public TitleTransferEntity(DatabaseAccess aaDA) throws RTSException	{		caDA = aaDA;	}		/**	 * Method to Query RTS.RTS_TTL_TRNSFR_ENT	 *	 * @return Vector	 * @throws RTSException 	 *//* &TitleTransferEntity.qryTitleTransferEntity& */	public Vector qryTitleTransferEntity() throws RTSException	{		Log.write(			Log.METHOD,			this,			" - qryTitleTransferEntity - Begin");		StringBuffer lsQry = new StringBuffer();		Vector lvRslt = new Vector();		ResultSet lrsQry;		lsQry.append(			"SELECT TtlTrnsfrEntCd,"				+ "TtlTrnsfrEntDesc "				+ "FROM RTS.RTS_TTL_TRNSFR_ENT");		try		{			Log.write(				Log.SQL,				this,				" - qryTitleTransferEntity - SQL - Begin");			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);			Log.write(				Log.SQL,				this,				" - qryTitleTransferEntity - SQL - End");			while (lrsQry.next())			{				TitleTransferEntityData laTtlTrnsfrEntData =					new TitleTransferEntityData();				laTtlTrnsfrEntData.setTtlTrnsfrEntCd(					caDA.getStringFromDB(lrsQry, "TtlTrnsfrEntCd"));				laTtlTrnsfrEntData.setTtlTrnsfrEntDesc(					caDA.getStringFromDB(lrsQry, "TtlTrnsfrEntDesc"));				// Add element to the Vector				lvRslt.addElement(laTtlTrnsfrEntData);			} //End of While			lrsQry.close();			caDA.closeLastDBStatement();			lrsQry = null;			Log.write(				Log.METHOD,				this,				" - qryTitleTransferEntity - End ");			return (lvRslt);		}		catch (SQLException leSQLEx)		{			Log.write(				Log.SQL_EXCP,				this,				" - qryTitleTransferEntity - SQL Exception "					+ leSQLEx.getMessage());			throw new RTSException(RTSException.DB_ERROR, leSQLEx);		}	} //END OF QUERY METHOD}/* #TitleTransferEntity# */