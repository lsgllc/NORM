package com.txdot.isd.rts.server.dataaccess;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * DatabaseAccess.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    09/07/2001	Added convertToString for String
 * R Hicks      09/18/2001  Fixed Timestamp handling in
 *                          	setValueInPreparedStatement
 * R Hicks      09/25/2001  Corrected userid / password
 * R Hicks      09/25/2001  Close DB connections
 * R Hicks      10/8/2001   Add JavaDoc / Resource Management
 * R Hicks      10/12/2001  Throw RTSException from getConnection()
 * R Hicks      11/27/2001  Set autocommit(false) in beginTransaction
 * R Hicks      12/01/2001  Call commit on queries in denTransaction()
 * N Ting       12/13/2001  Fix numeric value out of range problem by
 * 							changing from using BigDecimal to double in
 * 							setValueInPreparedStatement.
 * R Hicks      01/14/2002  Close all statements in endtransaction()
 * R Hicks      01/14/2002  Throw DB_DOWN and handle 
 * 							StaleConnectionExceptions
 * R Hicks      06/07/2002  Don't throw excetpion from endTransaction()
 * R Hicks      06/12/2002  Catch NullPointerException to workaround 
 * 							WebSphere 4.0.3 problem
 * R Hicks      07/12/2002  Adding statement to staments Vector in
 * 							wrong order
 * R Hicks      07/12/2002  Add method closeLastDBStatement()
 * R Hicks      09/09/2002  Close prepared statement in insert/update
 * 							method
 * R Hicks      09/25/2002  Call endTransaction() from beginTransaction
 * 							on RTSException
 * R Hicks		09/30/2002	Clean up logging, 
 * 							add closeStatements(), closeConnection()
 * Ray Rowehl	02/10/2003	Add boolean to convertToString to determine
 * 							if trimming is needed or not.
 *							defect 4735
 * Ray Rowehl	03/05/2003 	Add timestamp in front of stack trace
 * 							defect 5263
 * Ray Rowehl	02/04/2005	Change import for StaleConnectionException.
 * 							Change the Context Factory class.
 * 							Also format source and rename fields.
 * 							change import
 * 							modify CONNECTION_CLASS_NAME
 * 							defect 7140
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	08/17/2005	Java 1.4 Work plus 	
 * 							add Log.write for retries on getConnection()
 *							when executing SQL and receive Null Pointer
 *							Exception or Stale Connection Exception 
 *							modify executeDBInsertUpdateDelete(),
 *							executeDBQuery(String),
 *							executeDBQuery(String,Vector) 
 * 							defect 8120  Ver 5.2.2 Fix 3
 * K Harrell	10/10/2005	Java 1.4 Work 
 * 							defect 7889 Ver 5.2.3  
 * Ray Rowehl	12/06/2005	If SystemProperty.getContextProviderURL
 * 							is set, add it to the IntialContext 
 * 							hashtable to do datasource lookup.
 * 							Also found that init() is not used!
 * 							Also did some constants and formatting.
 * 							deprecate init()
 * 							modify getDataSource()
 * 							defect 8461 Ver 5.2.3
 * Ray Rowehl	01/13/2006	Remove StaleConnectionException since we
 * 							are having issues with it in SendTrans.
 * 							modify executeDBInsertUpdateDelete(),
 * 								executeDBQuery(String),
 * 								executeDBQuery(String, Vector)
 * 							defect 7140 Ver 5.2.3
 * Ray Rowehl	03/23/2006	Add more logging to catch more information
 *							on SQLExceptions.
 *							Found that the log level was high.  Lower
 *							to SQL_EXCP so it they will usually be 
 *							written.
 *							Also rename Exceptions in catches to
 *							conform to standards.
 *							modify closeConnection(), 
 *								closeLastDBStatement(),  
 *								executeDBInsertUpdateDelete(),
 *								executeDBQuery(),
 *								executeDBQuery(String, Vector),
 *								getConnection()
 *							defect 8633 Ver 5.2.2 Fix 8?
 * Ray Rowehl	03/24/2006	Blocking retries!  
 *							Change liRetry from 0 to 5.
 *							Add a litle more granularity to logging.
 *							Document NullPointers that are from 
 *							setValue...
 *							modify executeDBInsertUpdateDelete(),
 *								executeDBQuery(String),
 *								executeDBQuery(String, Vector),
 *								setValueInPreparedStatement()
 *							defect 8633 Ver 5.2.2 Fix8?
 * Ray Rowehl	03/29/2006	Write the values to the log on Insert...
 *							Also found the the log write was not
 *							in the best spot.
 *							modify executeDBInsertUpdateDelete(),
 *								executeDBQuery(String),
 *								executeDBQuery(String, Vector)
 *							defect 8633 Ver 5.2.3
 * Ray Rowehl	03/31/2006	Do not write the stacktrace on 803's.
 *							modify executeDBInsertUpdateDelete()
 *							defect 8633 Ver 5.2.3
 * Ray Rowehl	04/06/2006	Migrate into 5.2.3 code and modify to final
 * 							form.  Note Retries have been commented out.
 * 							modify executeDBInsertUpdateDelete(),
 *								executeDBQuery(String),
 *								executeDBQuery(String, Vector)
 *							defect 8633 Ver 5.2.3
 * Ray Rowehl	04/10/2006	Clean up the formatting for constants.
 * 							defect 8633 Ver 5.2.3
 * Ray Rowehl	04/11/2006	Add a constant for -803.
 * 							Found that we should not cast when getting
 * 							the strings out of the values vector when 
 * 							logging on NullPointers.
 * 							add CODE_SQL_DUPLICATE_KEY
 * 							modify executeDBInsertUpdateDelete(),
 * 								executeDBQuery(String, Vector)
 * 							defect 8633 Ver 5.2.3
 * Ray Rowehl	04/25/2006	Remove the use of the ContextProviderURL in 
 * 							getDataSource() done on 12/06/2005.
 * 							modify getDataSource()
 * 							defect 8461 Ver 5.2.3
 * Ray Rowehl	08/03/2006	Allow a retry on getConnection() if we get
 * 							one StaleConnectionException.
 * 							Also deleted some unused constants.
 * 							add MSG_STALECONN_RETRY,
 * 								TXT_STALECONNECTION
 * 							delete TXT_NULL_POINTER_EXCEPTION,
 * 								TXT_STALE_CONNECTION_EXCEPTION
 * 							modify getConnection()
 * 							defect 8869 Ver 5.2.4
 * Ray Rowehl	09/11/2006	Setup to allow overriding the normal 
 * 							database user and password.
 * 							Clean up old comments.
 * 							add csDbPassword, csDbUser
 * 							add DatabaseAccess(String, String)
 * 							modify DatabaseAccess(),
 * 								getConnection()
 * 							defect 8923 Ver 5.2.5
 * K Harrell	09/12/2006	Add logic to retry Stale Connection on 
 * 							1st sql statement w/in a unit of work. 
 * 							add cbFirstSQLStmt
 * 							modify MSG_STALECONN_RETRY, 
 * 							beginTransaction(), 
 * 							executeDBInsertUpdateDelete(),
 * 							executeDBQuery(String),
 * 							executeDBQuery(String,Vector)
 * 							defect 8919 Ver 5.2.5  
 * Ray Rowehl	02/18/2007	Add handling for booleans.
 * 							Assume translation to 0 and 1.
 * 							add convertToString(boolean abValue)
 * 								getBooleanFromDB(),
 * 								setValueInPreparedStatement()
 * 							defect 9116 Ver Special Plates 
 * Ray Rowehl	03/13/2007	Print a stack trans and info if the timezone
 * Kathy Harrell			is not Central.
 * 							modify convertToString(RTSDate aaValue),
 * 								getRTSDateFromDB()
 * 							defect 9138 Ver 5.2.4
 * Ray Rowehl	04/19/2007	Do not print the stack trace since it is 
 * 							filling the server logs.
 * 							modify getRTSDateFromDB()
 * 							defect 9138 Ver Special Plates
 * Ray Rowehl	05/22/2007	Do not print the stack trace since it is 
 * 							filling the server logs.
 * 							modify convertToString(RTSDate)
 * 							defect 9138 Ver Special Plates
 * Bob Brown	01/23/2008	Add access to an image column
 * 							add getByteArrayFromDB()
 * 							defect 9473 Ver Tres Amigos Prep
 * Ray Rowehl	01/26/2009	Add a method to determine if a connection
 * 							is still active. 
 * 							add isConnected()
 * 							defect 9804 Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */

/**
 * 
 * The DatabaseAccess class provides the interface between the 
 * server-side business objects and the DB2 database.  
 * It is responsible for retrieving a jdbc connection, executing the SQL
 * statements provided by the server-side business objects, 
 * returning the results, releasing the JDBC resources, and closing the
 * connection.  This layer also provides the mechanism for converting
 * Java and SQL types.
 * 
 * <p>
 * This execution of queries is performed using PreparedStatements.  
 * The values to be inserted into the PreparedStatement are contained 
 * in a Vector of DBValue objects.
 * 
 * <p>
 * It is the responsibility of the server-side objects to control the
 * allocation and the deallocation of the JDBC resources via the 
 * beginTransaction() and endTransaction() methods.  By default, the
 * autocommit feature of the JDBC connection is turned off.  Any 
 * changes that are required to be made to the DB require a DB commit.
 * This is accomplished by specifiying DatabaseAccess.COMMIT as 
 * a parameter to endTransaction().  Likewise, changes can be rolled 
 * back by passing the parameter DatabaseAccess.ROLLBACK.
 * 
 * <p>
 * A series of convertToString() methods are provided for use by the 
 * com.txdot.isd.rts.server.db objects.  These methods allow the db 
 * objects to be generated by a generic script.
 * 
 * <p>
 * A series of getXXXFromDB() methods are provided to retrieve a 
 * specific type of value from a specified column in the DB2 ResultSet.
 * 
 * @version	Defect_POS_D		01/26/2009
 * @author	Richard Hicks
 * <br>Creation Date:			08/28/2001 17:41:17
 */

public class DatabaseAccess
{
	//	defect 8919 
	/**
	 * boolean to denote if 1st SQL statment of a unit of work 
	 */
	boolean cbFirstSQLStmt = true;
	// end defect 8919  

	/**
	 * This is the SQL Code for Duplicate Key
	 */
	public static final int CODE_SQL_DUPLICATE_KEY = -803;

	/** 
	 * Static variable used by endTransaction() 
	 */
	public final static int NONE = 0;

	/** 
	 * Static variable used by endTransaction() to commit changes. 
	 */
	public final static int COMMIT = 1;

	/** 
	 * Static variable used by endTransaction() to rollback changes. 
	 */
	public final static int ROLLBACK = 2;

	/** 
	 * Static variable that contains the class name to use for creation
	 * of the InitialContext. 
	 */
	private final static String CONNECTION_CLASS_NAME =
		"com.ibm.websphere.naming.WsnInitialContextFactory";

	/** 
	 * Static variable that contains the JDBC URL. 
	 */
	private final static String JDBC_URL = "jdbc/";

	private static final String MSG_BEGINTRANSACTION_ENTRY =
		" - beginTransaction() - Entry.";
	private static final String MSG_BEGINTRANSACTION_EXIT =
		" - beginTransaction() - Exit.";
	private static final String MSG_CLOSECONNECTION_ENTRY =
		"closeConnection() - entry";
	private static final String MSG_CLOSECONNECTION_EXIT =
		"closeConnection() - exit";
	private static final String MSG_CLOSESTATEMENTS_ENTRY =
		"closeStatements() - entry";
	private static final String MSG_CLOSESTATEMENTS_EXIT =
		"closestatements() - exit";
	private static final String MSG_ENDTRANSACTION_ENTRY =
		" - endTransaction() - Entry.";
	private static final String MSG_ENDTRANSACTION_EXIT =
		" - endTransaction() - Exit.";

	private static final String MSG_EXECUTEDBINSERTUPDATEDELETE_ENTRY =
		" - executeDBInsertUpdateDelete() - Entry.";
	private static final String MSG_EXECUTEDBINSERTUPDATEDELETE_EXIT =
		" - executeDBInsertUpdateDelete() - Exit.";
	private static final String MSG_EXECUTEDBQUERY_ENTRY =
		" - executeDBQuery(String) - Entry.";
	private static final String MSG_EXECUTEDBQUERY_EXIT =
		" - executeDBQuery(String) - Exit.";
	private static final String MSG_EXECUTEDBQUERY_SV_ENTRY =
		" - executeDBQuery(String, Vector) - Entry.";
	private static final String MSG_EXECUTEDBQUERY_SV_EXIT =
		" - executeDB(String, Vector) - Exit.";

	private static final String MSG_ERR_CLOSE_FAILED =
		"DatabaseAccess - Close failed!";
	private static final String MSG_ERR_INSERT_UPDATE_08S01 =
		"DatabaseAccess.executeDBInsertUpdateDelete() (08S01) SQL is ";
	private static final String MSG_ERR_INSERT_UPDATE_803 =
		"DatabaseAccess.executeDBInsertUpdateDelete() (-803) SQL is ";
	private static final String MSG_ERR_INSERT_UPDATE_NPREX =
		"DatabaseAccess.executeDBInsertUpdateDelete() (NPREx) SQL is ";
	private static final String MSG_ERR_INSERT_UPDATE_NULLPTR_VALUES =
		"DatabaseAccess.executeDBInsertUpdateDelete() NullPtr Values ";
	private static final String MSG_ERR_INSERT_UPDATE_SQLEX =
		"DatabaseAccess.executeDBInsertUpdateDelete() (SQLEx) SQL is ";
	private static final String MSG_ERR_QUERY_08S01 =
		"DatabaseAccess.executeQuery() (08S01) SQL is ";
	private static final String MSG_ERR_QUERY_NULLPTR =
		"DatabaseAccess.executeQuery() (NPREx) SQL is ";
	private static final String MSG_ERR_QUERY_SQLEX =
		"DatabaseAccess.executeQuery() (SQLEx) SQL is ";
	private static final String MSG_ERR_QUERY2_08S01 =
		"DatabaseAccess.executeQuery(2) (08S01) SQL is ";
	private static final String MSG_ERR_QUERY2_NPREX =
		"DatabaseAccess.executeQuery(2) (NPREx) SQL is ";
	private static final String MSG_ERR_QUERY2_NPREX_VALUES =
		"DatabaseAccess.executeQuery(2) NPREx Values ";
	private static final String MSG_ERR_QUERY2_SQLEX =
		"DatabaseAccess.executeQuery(2) (SQLEx) SQL is ";

	// defect 8919	
	private static final String MSG_STALECONN_RETRY =
		" StaleConnectionException;  Retrying: ";
	// end defect 8919 

	private static final String TXT_08S01 = "08S01";
	private static final String TXT_DATABASEACCESS_ERROR =
		"DatabaseAccess error ";
	private static final String TXT_STALECONNECTION = "StaleConnection";

	/** 
	 * Instance variable that holds a reference to a DataSource 
	 */
	private static DataSource saDataSource = null;

	/** 
	 * Instance variable that contains the connection to the database. 
	 */
	private java.sql.Connection caConnection;

	private Vector cvStatements = new Vector();

	// defect 8923
	private String csDbPassword = null;
	private String csDbUser = null;
	// end defect 8923

	/**
	 * DatabaseAccess constructor.
	 * 
	 * <p>Use default Database UserId and Password.
	 */
	public DatabaseAccess()
	{
		// defect 8923
		// Pass the normal database user and password to the constructor.
		//super();
		this(
			SystemProperty.getDBUser(),
			SystemProperty.getDBPassword());
		// end defect 8923
	}

	/**
	 * DatabaseAccess Constructor.
	 * 
	 * @param asUser
	 * @param asPassword
	 */
	public DatabaseAccess(String asUser, String asPassword)
	{
		super();
		csDbUser = asUser;
		csDbPassword = asPassword;
	}

	/**
	 * This method retrieves a JDBC connection from the WebSphere 
	 * connection pool.
	 * 
	 * @throws RTSException
	 */
	public void beginTransaction() throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_BEGINTRANSACTION_ENTRY);

		try
		{
			closeStatements();
			getConnection().setAutoCommit(false);
			// defect 8919 
			cbFirstSQLStmt = true;
			// end defect 8919 
		}

		catch (SQLException aeSQLEx)
		{
			closeConnection();

			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_DOWN, aeSQLEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_BEGINTRANSACTION_EXIT);
		}
	}
	
	/**
	 * Close the Connection
	 */
	private void closeConnection()
	{
		Log.write(Log.METHOD, this, MSG_CLOSECONNECTION_ENTRY);
		closeStatements();
		if (caConnection != null)
		{
			try
			{
				caConnection.close();
			}
			catch (Exception aeEx)
			{
				// defect 8633
				// At least log the problem
				RTSException leRTSEx =
					new RTSException(RTSException.DB_ERROR, aeEx);
				leRTSEx.writeExceptionToLog();
				// end defect 8633
			}
			setConnection(null);
		}
		Log.write(Log.METHOD, this, MSG_CLOSECONNECTION_EXIT);
	}

	/**
	 * Close the Last DB Statement.
	 */
	public void closeLastDBStatement()
	{
		if ((cvStatements != null) && (cvStatements.size() > 0))
		{
			int liIndex = cvStatements.size() - 1;
			Statement laStmt =
				(Statement) (cvStatements.elementAt(liIndex));
			try
			{
				laStmt.close();
			}
			catch (Exception aeEx)
			{
				// defect 8633
				// At least log the problem
				RTSException leRTSEx =
					new RTSException(RTSException.DB_ERROR, aeEx);
				leRTSEx.writeExceptionToLog();
				// end defect 8633
			}
			cvStatements.removeElementAt(liIndex);
		}
	}

	/**
	 * Close the Statements
	 */
	private void closeStatements()
	{
		Log.write(Log.METHOD, this, MSG_CLOSESTATEMENTS_ENTRY);
		if (cvStatements != null)
		{
			// System.out.println("--  closing " + statements.size() + " statements");
			for (int i = 0; i < cvStatements.size(); i++)
			{
				try
				{
					((Statement) (cvStatements.elementAt(i))).close();
				}
				catch (Exception aeEx)
				{
					// defect 8633
					// At least log the problem
					RTSException leRTSEx =
						new RTSException(RTSException.DB_ERROR, aeEx);
					leRTSEx.writeExceptionToLog();
					// end defect 8633
				}
			}
			cvStatements.removeAllElements();
		}
		Log.write(Log.METHOD, this, MSG_CLOSESTATEMENTS_EXIT);
	}

	/**
	 * Convert Object to Bytes
	 * 
	 * @param aaObj Object
	 * @return byte[] 
	 */
	public static byte[] convertObjectToBytes(Object aaObj)
	{
		byte[] larrBytes = null;
		try
		{
			ByteArrayOutputStream laBAOS = new ByteArrayOutputStream();
			ObjectOutputStream laOOS = new ObjectOutputStream(laBAOS);
			laOOS.writeObject(aaObj);
			larrBytes = laBAOS.toByteArray();
		}
		catch (Exception aeEx)
		{
			System.err.println(
				TXT_DATABASEACCESS_ERROR
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).getYYYYMMDDDate()
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).get24HrTime());
			aeEx.printStackTrace();
			// defect 8633
			// At least log the problem
			RTSException leRTSEx =
				new RTSException(RTSException.DB_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
			// end defect 8633
		}
		return (larrBytes);
	}

	/**
	 * This method returns the String repesentation of a boolean used in 
	 * indi fields (ie returns 1 or 0).  
	 * This method is used by the server-side db objects.
	 * 
	 * @param abValue boolean
	 * @return String 
	 */
	public static String convertToString(boolean abValue)
	{
		String lsNewString = "0";
		if (abValue)
		{
			lsNewString = "1";
		}
		return lsNewString;
	}

	/**
	 * This method returns the String repesentation of a double.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param adValue double
	 * @return String 
	 */
	public static String convertToString(double adValue)
	{
		return Double.toString(adValue);
	}

	/**
	 * This method returns the String repesentation of a float.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param afValue float
	 * @return String 
	 */
	public static String convertToString(float afValue)
	{
		return Float.toString(afValue);
	}

	/**
	 * This method returns the String repesentation of a int.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param aiValue int
	 * @return String 
	 */
	public static String convertToString(int aiValue)
	{
		return Integer.toString(aiValue);
	}

	/**
	 * This method returns the String repesentation of a long.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param  alValue long
	 * @return String 
	 */
	public static String convertToString(long alValue)
	{
		return Long.toString(alValue);
	}

	/**
	 * This method returns the String repesentation of a Dollar object.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param aaValue Dollar
	 * @return String 
	 */
	public static String convertToString(Dollar aaValue)
	{
		if (aaValue != null)
		{
			return aaValue.toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method returns the String repesentation of a RTSDate object.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param aaValue RTSDate
	 * @return String 
	 */
	public static String convertToString(RTSDate aaValue)
	{
		if (aaValue != null && aaValue.getTimestamp() != null)
		{
			// defect 9138
			// If the Time zone is off, lets print some of the info
//			if ((aaValue.getTimestamp().getTimezoneOffset() / 60) != 6)
//			{
//				System.out.println("=================================");
//				System.out.println("DBAccess convertToString RTSDate");
//				System.out.println(
//					"Time "
//						+ aaValue.getHour()
//						+ " "
//						+ aaValue.getMinute()
//						+ " X "
//						+ aaValue.getTimestamp().getTime()
//						+ " TZ "
//						+ aaValue.getTimestamp().getTimezoneOffset()
//							/ 60);
// 
// these messages are filling was logs.  do not do for now.
//				try
//				{
//					throw new RTSException();
//				}
//				catch (RTSException aeRTSEx)
//				{
//					aeRTSEx.printStackTrace();
//				}
// 
			//	System.out.println(" ");
			//}
			// end defect 9138
			return Long.toString(aaValue.getTimestamp().getTime());
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method returns the String repesentation of a String object.  
	 * This method is used by the server-side db objects.
	 * 
	 * @param asValue String
	 * @return String 
	 */
	public static String convertToString(String asValue)
	{
		if (asValue != null)
		{
			// call the boolean method with boolean true to trim
			return convertToString(asValue, true);
		}
		else
		{
			return asValue;
		}
	}

	/**
	 * This method returns the String repesentation of a String object.  
	 * This method is used by the server-side db objects.
	 * This method only trims when the boolean is true.
	 * Otherwise, it does not.
	 * 
	 * @param asValue String
	 * @param abTrim  boolean
	 * @return String 
	 */
	public static String convertToString(
		String asValue,
		boolean abTrim)
	{
		if (abTrim)
		{
			return asValue.trim();
		}
		else
		{
			return asValue;
		}
	}

	/**
	 * This method is responsible for commiting or rolling back any DB 
	 * changes (if applicable) and releasing the JDBC connection.
	 * 
	 * @param  aiMode int (ie. NONE, COMMIT, ROLLBACK)
	 * @throws RTSException
	 */
	public void endTransaction(int aiMode) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_ENDTRANSACTION_ENTRY);

		try
		{
			if (caConnection != null)
			{
				switch (aiMode)
				{
					case NONE :
						{
							caConnection.commit();
							break;
						}
					case COMMIT :
						{
							caConnection.commit();
							break;
						}
					case ROLLBACK :
						{
							caConnection.rollback();
							break;
						}
					default :
						{
							caConnection.rollback();
							break;
						}
				}
			}
		}
		catch (Exception aeEx)
		{
			Log.write(Log.SQL_EXCP, this, aeEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeEx);
		}
		finally
		{
			closeConnection();
			Log.write(Log.METHOD, this, MSG_ENDTRANSACTION_EXIT);
		}
	}

	/**
	 * This method is invoke to execute the SQL statements for 
	 * performing inserts, updates, and deletes to the DB2 database.
	 * 
	 * @param asSQL String 
	 * @param avValues Vector 
	 * @return int  
	 * @throws RTSException 
	 */
	public int executeDBInsertUpdateDelete(
		String asSQL,
		Vector avValues)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_EXECUTEDBINSERTUPDATEDELETE_ENTRY);

		int liResult = 0;

		// defect 8919
		int liRetries = 0;
		boolean lbRetry = true;

		while (lbRetry)
		{
			PreparedStatement laPS = null;
			try
			{
				laPS = caConnection.prepareStatement(asSQL);
				cvStatements.addElement(laPS);

				for (int i = 0; i < avValues.size(); i++)
				{
					setValueInPreparedStatement(
						laPS,
						i + 1,
						(DBValue) avValues.elementAt(i));
				}

				liResult = laPS.executeUpdate();

				cbFirstSQLStmt = false;
				lbRetry = false;

				try
				{
					laPS.close();
				}
				catch (Exception aeEx)
				{
					// should at least log that the close failed
					Log.write(Log.SQL_EXCP, this, MSG_ERR_CLOSE_FAILED);
				}
			}
			catch (SQLException aeSQLEx)
			{
				Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());

				// Retry Stale Connection if
				//  1st SQL Statement and no prior retries 
				if (aeSQLEx.toString().indexOf(TXT_STALECONNECTION)
					> -1)
				{
					if (liRetries < 1 && cbFirstSQLStmt)
					{
						//	Log that retrying StaleConnection
						Log.write(
							Log.SQL_EXCP,
							aeSQLEx,
							MSG_STALECONN_RETRY + asSQL);
						liRetries = liRetries + 1;
						closeConnection();
						beginTransaction();
						continue;
					}
				}

				if (aeSQLEx.getSQLState().equals(TXT_08S01))
				{
					// write statement to log
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_INSERT_UPDATE_08S01 + asSQL);
					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(RTSException.DB_DOWN, aeSQLEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;
				}
				if (aeSQLEx.getErrorCode() == CODE_SQL_DUPLICATE_KEY)
				{
					// Do not write stack trace to log for 803's
					// write statement to log
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_INSERT_UPDATE_803 + asSQL);

					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(
							RTSException.DB_ERROR,
							aeSQLEx);
					Log.write(
						Log.SQL_EXCP,
						aeSQLEx,
						aeSQLEx.getLocalizedMessage());
					throw leRTSEx;
				}
				else
				{
					// write statement to log
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_INSERT_UPDATE_SQLEX + asSQL);

					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(
							RTSException.DB_ERROR,
							aeSQLEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;
				}
			}
			catch (NullPointerException aeNPEx)
			{
				// The following is a comment from Richard Hicks.
				// *** This is a workaround to a WebSphere 4.0.3 problem.
				// *** This code should be removed when the problem is fixed.

				// write statement to log
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_ERR_INSERT_UPDATE_NPREX + asSQL);

				// write out the values
				StringBuffer lsValuesString = new StringBuffer();
				for (int i = 0; i < avValues.size(); i = i + 1)
				{
					lsValuesString.append(avValues.get(i));
					lsValuesString.append(
						CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE);
				}
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_ERR_INSERT_UPDATE_NULLPTR_VALUES
						+ lsValuesString.toString());

				// do a log write on this before throwing it
				RTSException leRTSEx =
					new RTSException(RTSException.DB_DOWN, aeNPEx);
				leRTSEx.writeExceptionToLog();
				throw leRTSEx;
			}
			catch (RTSException aeRTSEx)
			{
				closeConnection();
				throw aeRTSEx;
			}
		}
		// end defect 8919 
		Log.write(
			Log.METHOD,
			this,
			MSG_EXECUTEDBINSERTUPDATEDELETE_EXIT);

		return liResult;
	}

	/**
	 * This method executes the SQL query passed in as a parameter.
	 * 
	 * @param  asSQL String 
	 * @return ResultSet
	 * @throws RTSException
	 */
	public ResultSet executeDBQuery(String asSQL) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_EXECUTEDBQUERY_ENTRY);

		ResultSet lrsResults = null;
		PreparedStatement laStmt = null;

		// defect 8919
		int liRetries = 0;
		boolean lbRetry = true;

		while (lbRetry)
		{
			try
			{
				laStmt = caConnection.prepareStatement(asSQL);
				cvStatements.addElement(laStmt);

				if (laStmt.execute())
				{
					lrsResults = laStmt.getResultSet();
				}
				cbFirstSQLStmt = false;
				lbRetry = false;
			}
			catch (SQLException aeSQLEx)
			{
				Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());

				// Retry Stale Connection if
				//  1st SQL Statement and no prior retries 
				if (aeSQLEx.toString().indexOf(TXT_STALECONNECTION)
					> -1)
				{
					if (liRetries < 1 && cbFirstSQLStmt)
					{
						//	Log that retrying StaleConnection 
						Log.write(
							Log.SQL_EXCP,
							aeSQLEx,
							MSG_STALECONN_RETRY + asSQL);
						liRetries = liRetries + 1;
						closeConnection();
						beginTransaction();
						continue;
					}
				}

				if (aeSQLEx.getSQLState().equals(TXT_08S01))
				{
					// defect 8633
					// log the statement
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_QUERY_08S01 + asSQL);
					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(RTSException.DB_DOWN, aeSQLEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;
				}
				else
				{
					// log the statement
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_QUERY_SQLEX + asSQL);
					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(
							RTSException.DB_ERROR,
							aeSQLEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;
				}
			}
			catch (NullPointerException aeNPEx)
			{
				// Comment from Richard Hicks
				// *** This is a workaround to a WebSphere 4.0.3 problem.
				//  ***  This code should be removed when the problem is fixed.

				// log the statement
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_ERR_QUERY_NULLPTR + asSQL);
				// do a log write on this before throwing it
				RTSException leRTSEx =
					new RTSException(RTSException.DB_DOWN, aeNPEx);
				leRTSEx.writeExceptionToLog();
				throw leRTSEx;
			}
		}
		// end defect 8919 
		Log.write(Log.METHOD, this, MSG_EXECUTEDBQUERY_EXIT);

		return lrsResults;
	}

	/**
	 * This method executes an SQL query that is contructed from a 
	 * PreparedStatement and a Vector of values.
	 * 
	 * @param asSQL String Contains the PreparedStatement 
	 * @param avValues Vector Contains values to be inserted
	 * @return ResultSet  
	 * @throws RTSException
	 */
	public ResultSet executeDBQuery(String asSQL, Vector avValues)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_EXECUTEDBQUERY_SV_ENTRY);

		ResultSet lrsResults = null;
		PreparedStatement laPS = null;

		//	defect 8919
		// Retry logic w/in executeDBQuery()
		if (avValues == null)
		{
			lrsResults = executeDBQuery(asSQL);
		}
		else
		{
			int liRetries = 0;
			boolean lbRetry = true;

			while (lbRetry)
			{
				try
				{

					laPS = caConnection.prepareStatement(asSQL);
					cvStatements.addElement(laPS);

					for (int i = 0; i < avValues.size(); i++)
					{
						setValueInPreparedStatement(
							laPS,
							i + 1,
							(DBValue) avValues.elementAt(i));
					}

					if (laPS.execute())
					{
						lrsResults = laPS.getResultSet();
					}
					cbFirstSQLStmt = false;
					lbRetry = false;
				}
				catch (SQLException aeSQLEx)
				{
					Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());

					// Retry Stale Connection if
					//  1st SQL Statement and no prior retries
					if (aeSQLEx.toString().indexOf(TXT_STALECONNECTION)
						> -1)
					{
						if (liRetries < 1 && cbFirstSQLStmt)
						{
							//	Log that retrying StaleConnection 
							Log.write(
								Log.SQL_EXCP,
								aeSQLEx,
								MSG_STALECONN_RETRY + asSQL);
							liRetries = liRetries + 1;
							closeConnection();
							beginTransaction();
							continue;
						}
					}

					if (aeSQLEx.getSQLState().equals(TXT_08S01))
					{
						// log the statement
						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_ERR_QUERY2_08S01 + asSQL);
					}
					else
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_ERR_QUERY2_SQLEX + asSQL);
					}
					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(RTSException.DB_DOWN, aeSQLEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;

				}
				catch (NullPointerException aeNPEx)
				{
					// comment from Richard Hicks
					// *** This is a workaround to a WebSphere 4.0.3 problem.
					// ***  This code should be removed when the problem is fixed.

					// log the statement
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_QUERY2_NPREX + asSQL);
					// write out the values
					StringBuffer lsValuesString = new StringBuffer();
					for (int i = 0; i < avValues.size(); i = i + 1)
					{
						lsValuesString.append(avValues.get(i));
						lsValuesString.append(
							CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE);
					}
					Log.write(
						Log.SQL_EXCP,
						this,
						MSG_ERR_QUERY2_NPREX_VALUES
							+ lsValuesString.toString());
					// do a log write on this before throwing it
					RTSException leRTSEx =
						new RTSException(RTSException.DB_DOWN, aeNPEx);
					leRTSEx.writeExceptionToLog();
					throw leRTSEx;
				}
			}
		}
		// end defect 8919 
		Log.write(Log.METHOD, this, MSG_EXECUTEDBQUERY_SV_EXIT);

		return lrsResults;
	}

	/**
	 * Method to retrieve a BigDecimal from a specific column within a
	 * ResultSet.
	 * 
	 * @param  arsRS ResultSet
	 * @param  asColumn String 
	 * @return BigDecimal
	 * @throws RTSException
	 */
	public BigDecimal getBigDecimalFromDB(
		ResultSet arsRS,
		String asColumn)
		throws RTSException
	{
		BigDecimal laResult = null;
		try
		{
			laResult = arsRS.getBigDecimal(asColumn);
		}
		catch (SQLException aeSQLEx)
		{
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return laResult;
	}

	/**
	 * Get BLOB from DB Results
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String
	 * @return Object 
	 * @throws RTSException
	 */
	public Object getBlobFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		Object laResult;
		Blob laBlob = null;

		try
		{
			laBlob = arsRS.getBlob(asColumn);

			if (laBlob == null)
			{
				laResult = null;
			}
			else
			{
				int liLength = (int) laBlob.length();
				ByteArrayInputStream laBAIS =
					new ByteArrayInputStream(
						laBlob.getBytes(1, liLength));
				ObjectInputStream laOIS = new ObjectInputStream(laBAIS);
				laResult = laOIS.readObject();
				laBAIS.close();
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (Exception aeEx)
		{
			Log.write(Log.SQL_EXCP, this, aeEx.getMessage());

			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		return laResult;
	}
	
	/**
	 * Get byte array from DB Results
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String
	 * @return  byte[] 
	 * @throws RTSException
	 */
	
	public byte[] getByteArrayFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		//Object laResult;
		Blob laBlob = null;
		byte[] laImageByteArray = null;

		try
		{
			laBlob = arsRS.getBlob(asColumn);

			if (laBlob != null)
			{
				int liLength = (int) laBlob.length();
				laImageByteArray = (byte[])(laBlob.getBytes(1, liLength));
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (Exception aeEx)
		{
			Log.write(Log.SQL_EXCP, this, aeEx.getMessage());

			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		return laImageByteArray;
	}	

	/**
	 * This method returns a JDBC connection retrieved from the 
	 * WebSphere DataSource.  If the DataSource is null, a JNDI lookup
	 * is performed to retrieve it.
	 * 
	 * @return java.sql.Connection 
	 * @throws RTSException
	 */
	private java.sql.Connection getConnection() throws RTSException
	{
		boolean lbRetry = true;
		int liRetries = 0;

		if (caConnection == null)
		{
			DataSource laDatSource = getDataSource();

			while (lbRetry)
			{
				try
				{
					// defect 8923
					caConnection =
						laDatSource.getConnection(
							csDbUser,
							csDbPassword);
					// end defect 8923
					lbRetry = false;
				}
				catch (SQLException aeSQLEx)
				{
					// defect 8869
					// Allow for one retry on the StaleConnection!
					if (aeSQLEx.getMessage() != null
						&& aeSQLEx.getMessage().indexOf(
							TXT_STALECONNECTION)
							> -1
						&& liRetries < 1)
					{
						liRetries = liRetries + 1;
						// Log that a StaleConnection occurred.
						Log.write(
							Log.SQL_EXCP,
							aeSQLEx,
							MSG_STALECONN_RETRY);
					}
					else
					{
						// This section was here before 8869
						closeConnection();
						// do a log write on this before throwing it
						RTSException leRTSEx =
							new RTSException(
								RTSException.DB_DOWN,
								aeSQLEx);
						leRTSEx.writeExceptionToLog();
						throw leRTSEx;
					}
					// defect 8869
				}
			}
		}
		return caConnection;
	}

	/**
	 * Returns the WebSphere DataSource object.
	 * 
	 * @return DataSource
	 */
	private static DataSource getDataSource() throws RTSException
	{
		if (saDataSource == null)
		{
			//  JNDI context
			InitialContext laContext = null;
			Hashtable lhtParms = null;

			try
			{
				lhtParms = new Hashtable();
				lhtParms.put(
					Context.INITIAL_CONTEXT_FACTORY,
					CONNECTION_CLASS_NAME);

				// defect 8461
				// remove the use of ContextProviderURL

				// setup the datasource definition string based on 
				// whether we are running under WAS or as app client.
				String lsDSSetup;

				// If the context provider URL is not null, add it
				// to the InitialContext setup
				//if (SystemProperty.getContextProviderURL() != null)
				//{
				//	lhtParms.put(
				//		Context.PROVIDER_URL,
				//		SystemProperty.getContextProviderURL());
				//
				//	Properties laProp = System.getProperties();
				//	System.out.println(laProp);
				//
				//	// Setup datasource for app client
				//	lsDSSetup =
				//		JAVA_JDBC_URL + SystemProperty.getDatasource();
				//}
				//else
				//{

				// Setup datasource for WAS env
				lsDSSetup = JDBC_URL + SystemProperty.getDatasource();

				//}

				laContext = new InitialContext(lhtParms);
				saDataSource = (DataSource) laContext.lookup(lsDSSetup);
				// end defect 8461

				setDataSource(saDataSource);
			}
			catch (NamingException aeNEx)
			{
				System.err.println(
					TXT_DATABASEACCESS_ERROR
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).getYYYYMMDDDate()
						+ CommonConstant.STR_SPACE_ONE
						+ (new RTSDate()).get24HrTime());
				aeNEx.printStackTrace();
				throw new RTSException(RTSException.DB_DOWN, aeNEx);
			}
			finally
			{
				laContext = null;
				lhtParms.clear();
				lhtParms = null;
			}
		}

		return saDataSource;
	}

	/**
	 * Method to retrieve a Dollar object from a specific column within
	 * a ResultSet.
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String 
	 * @return BigDecimal
	 * @throws RTSException
	 */
	public Dollar getDollarFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		Dollar laResult = null;

		try
		{
			String str = arsRS.getString(asColumn);

			//If there is null value stored in database. Return 0.0
			if (arsRS.wasNull())
			{
				laResult = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			}
			else
			{
				laResult = new Dollar(str);
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

		return laResult;
	}

	/**
	 * Method to retrieve a Double from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String  
	 * @return Double 
	 * @throws RTSException
	 */
	public double getDoubleFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		double ldResult = 0;

		try
		{
			ldResult = arsRS.getDouble(asColumn);
			if (arsRS.wasNull())
			{
				ldResult = Double.MIN_VALUE;
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return ldResult;
	}

	/**
	 * Method to retrieve a Boolean from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String  
	 * @return Double 
	 * @throws RTSException
	 */
	public boolean getBooleanFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		boolean lbResult = false;

		try
		{
			lbResult = arsRS.getBoolean(asColumn);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

		return lbResult;
	}

	/**
	 * Method to retrieve a Float from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String
	 * @return Float
	 * @throws RTSException
	 */
	public float getFloatFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		float lfResult = 0;
		try
		{
			lfResult = arsRS.getFloat(asColumn);
			if (arsRS.wasNull())
			{
				lfResult = Float.MIN_VALUE;
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return lfResult;
	}

	/**
	 * Method to retrieve a int from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet
	 * @param asColumn String 
	 * @return int 
	 * @throws RTSException
	 */
	public int getIntFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		int liResult = 0;

		try
		{
			liResult = arsRS.getInt(asColumn);
			if (arsRS.wasNull())
			{
				liResult = Integer.MIN_VALUE;
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return liResult;
	}

	/**   
	 * Method to retrieve a long from a specific column within a 
	 * ResultSet.    
	 * 
	 * @param arsRS ResultSet 
	 * @param asColumn String
	 * @return long 
	 * @throws RTSException
	 */
	public long getLongFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		long llResult = 0;

		try
		{
			llResult = arsRS.getLong(asColumn);
			if (arsRS.wasNull())
			{
				llResult = Long.MIN_VALUE;
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return llResult;
	}

	/**
	 * Method to retrieve a RTSDate from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet 
	 * @param asColumn String
	 * @return RTSDate  
	 * @throws RTSException 
	 */
	public RTSDate getRTSDateFromDB(ResultSet arsRS, String asColumn)
		throws RTSException
	{
		RTSDate laResult = null;

		try
		{
			ResultSetMetaData laRSMD = arsRS.getMetaData();
			int liIndex = 1;

			for (int i = 1; i <= laRSMD.getColumnCount(); i++)
			{
				if (laRSMD
					.getColumnName(i)
					.equals(asColumn.toUpperCase()))
				{
					liIndex = i;
					break;
				}
			}

			switch (laRSMD.getColumnType(liIndex))
			{
				case Types.DATE :
					java.sql.Date laSQLDate = arsRS.getDate(asColumn);

					if (arsRS.wasNull())
					{
						laResult = null;
					}
					else
					{
						laResult = new RTSDate(laSQLDate);
					}
					break;

				case Types.TIMESTAMP :
					java.sql.Timestamp laTimeStamp =
						arsRS.getTimestamp(asColumn);
					if (arsRS.wasNull())
					{
						laResult = null;
					}
					else
					{

						laResult = new RTSDate(laTimeStamp);

						// if timezone is not 6, print a message
						if (((laTimeStamp.getTimezoneOffset() / 60)
							!= 6)
							|| ((laResult
								.getTimestamp()
								.getTimezoneOffset()
								/ 60)
								!= 6))
						{
//							System.out.println(
//								"=================================");
//							System.out.println(
//								"DBAccess getRTSDateFromDB TimeStamp");
//							System.out.println(
//								"TimeStamp "
//									+ laTimeStamp.toString()
//									+ " LONG "
//									+ laTimeStamp.getTime()
//									+ " GMT "
//									+ laTimeStamp.toGMTString()
//									+ " TZ "
//									+ laTimeStamp.getTimezoneOffset()
//										/ 60);
//							System.out.println(" ");
							
// defect 9138
// Do not throw this exception.  It fills the server syserr.
//							try
//							{
//								throw new Exception();
//							}
//							catch (Exception aeEx)
//							{
//								aeEx.printStackTrace();
//							}
// end defect 9138
							
//							System.out.println(
//								"RTSDate   "
//									+ laResult.get24HrTime()
//									+ " LONG "
//									+ laResult.getTimestamp().getTime()
//									+ " TZ "
//									+ laResult
//										.getTimestamp()
//										.getTimezoneOffset()
//										/ 60);
//							System.out.println("");
						}

					}
					break;
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return laResult;
	}

	/**
	 * Method to retrieve a String from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsResultSetResultSet
	 * @param asColumn String  
	 * @return String 
	 * @throws RTSException
	 */
	public String getStringFromDB(
		ResultSet arsResultSet,
		String asColumn)
		throws RTSException
	{
		// return result after triming.
		return getStringFromDB(arsResultSet, asColumn, true);
	}

	/**
	 * Method to retrieve a String from a specific column within a 
	 * ResultSet.
	 * 
	 * @return String
	 * @param arsRS ResultSet 
	 * @param asColumn String
	 * @param abTrim boolean  
	 * @throws RTSException
	 */
	public String getStringFromDB(
		ResultSet arsRS,
		String asColumn,
		boolean abTrim)
		throws RTSException
	{
		String lsResult = null;

		try
		{
			lsResult = arsRS.getString(asColumn);
			if (lsResult == null)
			{
				lsResult = CommonConstant.STR_SPACE_EMPTY;
			}

			// determine how to trim extra characters.
			if (abTrim)
			{
				// trim result normally.
				lsResult = lsResult.trim();
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return lsResult;
	}

	/**
	 * Method to retrieve a TimeStamp from a specific column within a 
	 * ResultSet.
	 * 
	 * @param arsRS ResultSet 
	 * @param asColumn String 
	 * @return TimeStamp 
	 * @throws RTSException
	 */
	public Timestamp getTimestampFromDB(
		ResultSet arsRS,
		String asColumn)
		throws RTSException
	{
		Timestamp laResult = null;

		try
		{
			laResult = arsRS.getTimestamp(asColumn);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		return laResult;
	}

	/**
	 * This should be executed by the RTS Servlet init routine.
	 * 
	 * @deprecated
	 */
	public static void init()
	{
		// WebSphere DataSource
		DataSource laDataSource = null;

		//  JNDI context
		InitialContext laContext = null;

		Hashtable lhtParms = null;

		try
		{
			lhtParms = new Hashtable();
			lhtParms.put(
				Context.INITIAL_CONTEXT_FACTORY,
				CONNECTION_CLASS_NAME);
			laContext = new InitialContext(lhtParms);
			laDataSource =
				(DataSource) laContext.lookup(
					JDBC_URL + SystemProperty.getDatasource());
			setDataSource(laDataSource);
		}
		catch (NamingException aeNEx)
		{
			System.err.println(
				TXT_DATABASEACCESS_ERROR
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).getYYYYMMDDDate()
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).get24HrTime());
			aeNEx.printStackTrace();
		}
		finally
		{
			laContext = null;
			lhtParms.clear();
			lhtParms = null;
		}
	}
	
	/**
	 * Returns the boolean reflecting if the connection is still active.
	 * 
	 * @return boolean
	 */
	public boolean isConnected()
	{
		return (caConnection != null);
	}

	/**
	 * To allow standalone testing of class
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		DatabaseAccess laDBA = null;
		ResultSet lrsRS = null;

		try
		{
			String query = "SELECT * FROM RTS.RTS_DEALERS";

			SystemProperty.isCommServer();
			laDBA = new DatabaseAccess();

			// Read the objects in the DB
			int liCount = 0;
			laDBA.beginTransaction();

			lrsRS = laDBA.executeDBQuery(query);
			while (lrsRS.next())
			{
				liCount = liCount + 1;
				System.out.println(
					"DEALERID = "
						+ laDBA.getIntFromDB(lrsRS, "DEALERID"));
				System.out.println(
					"DEALERNAME = "
						+ laDBA.getStringFromDB(lrsRS, "DEALERNAME"));
			}
			lrsRS.close();
			laDBA.endTransaction(DatabaseAccess.NONE);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (Exception aeEx)
		{
			System.err.println(
				TXT_DATABASEACCESS_ERROR
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).getYYYYMMDDDate()
					+ CommonConstant.STR_SPACE_ONE
					+ (new RTSDate()).get24HrTime());
			aeEx.printStackTrace();
		}
	}

	/**
	 * This method sets the connection property to hold the JDBC 
	 * Connection object. 
	 * 
	 * @param aaConnection java.sql.Connection
	 */
	private void setConnection(java.sql.Connection aaConnection)
	{
		caConnection = aaConnection;
	}

	/**
	 * This method sets the saDataSource property to hold the WebSphere
	 * DataSource object.
	 *
	 * @param aaDataSource DataSource
	 */

	private static synchronized void setDataSource(DataSource aaDataSource)
	{
		saDataSource = aaDataSource;
	}

	/**
	 * This method sets a value into the SQL PreparedStatement based on
	 * the type specified.
	 *
	 * @param aaStatement java.sql.PreparedStatement
	 * @param aiIndex int T
	 * @param aaValue DBValue
	 * @throws RTSException
	 */
	private void setValueInPreparedStatement(
		PreparedStatement aaStatement,
		int aiIndex,
		DBValue aaValue)
		throws RTSException
	{
		try
		{
			String lsValue = aaValue.getDbValue();

			switch (aaValue.getDbType())
			{
				//case Types.ARRAY :
				//  Not Implemented
				//    break;

				case Types.BIGINT :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY)
							|| lsValue.equals(
								String.valueOf(Long.MIN_VALUE)))
						{
							aaStatement.setNull(aiIndex, Types.BIGINT);
						}
						else
						{
							aaStatement.setLong(
								aiIndex,
								Long.parseLong(lsValue));
						}
						break;
					}
					// case Types.BINARY :
					//  Not Implemented
					//    break;
					// case Types.BIT :
					//  Not Implemented
					//    break;

				case Types.BLOB :
					{
						if (aaValue.getObjectBytes() == null)
						{
							aaStatement.setNull(aiIndex, Types.BLOB);
						}
						else
						{
							ByteArrayInputStream laBAIS =
								new ByteArrayInputStream(
									aaValue.getObjectBytes());
							aaStatement.setBinaryStream(
								aiIndex,
								laBAIS,
								aaValue.getObjectBytes().length);
						}
						break;
					}
				case Types.BOOLEAN :
					{
						// if there is no value, treat as false
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY)
							|| lsValue.equals(
								String.valueOf(Integer.MIN_VALUE)))
						{
							aaStatement.setInt(aiIndex, 0);
						}
						else
						{
							aaStatement.setInt(
								aiIndex,
								Integer.parseInt(lsValue));
						}
						break;
					}
				case Types.CHAR :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							aaStatement.setNull(aiIndex, Types.VARCHAR);
						}
						else
						{
							aaStatement.setString(aiIndex, lsValue);
						}
						break;
					}
					// case Types.CLOB :
					//  Not Implemented     
					//    break;

					// case Types.DATE :
					//  Not Implemented     
					//    break;

				case Types.DECIMAL :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY)
							|| lsValue.equals(
								String.valueOf(Double.MIN_VALUE)))
						{
							aaStatement.setNull(aiIndex, Types.DOUBLE);
						}
						else
						{
							//Used to be BigDecimal.  
							// Need to be verified by Richard
							aaStatement.setDouble(
								aiIndex,
								new Double(lsValue).doubleValue());
						}
						break;
					}
					// case Types.DISTINCT :
					//  Not Implemented     
					//    break;

					// case Types.DOUBLE :
					//    break;

					// case Types.FLOAT :
					//    break;

				case Types.INTEGER :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY)
							|| lsValue.equals(
								String.valueOf(Integer.MIN_VALUE)))
						{
							aaStatement.setNull(aiIndex, Types.INTEGER);
						}
						else
						{
							aaStatement.setInt(
								aiIndex,
								Integer.parseInt(lsValue));
						}
						break;
					}
					// case Types.JAVA_OBJECT :
					//  Not Implemented     
					//    break;

					// case Types.LONGVARBINARY :
					//  Not Implemented     
					//    break;

				case Types.LONGVARCHAR :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							aaStatement.setNull(
								aiIndex,
								Types.LONGVARCHAR);
						}
						else
						{
							aaStatement.setString(aiIndex, lsValue);
						}
						break;
					}
				case Types.NULL :
					{
						aaStatement.setNull(
							aiIndex,
							aaValue.getDbType());
						break;
					}
					// case Types.NUMERIC :
					//  Not Implemented     
					//    break;

					// case Types.OTHER :
					//  Not Implemented     
					//     break;

					// case Types.REAL :
					//  Not Implemented     
					//     break;

					// case Types.REF :
					//  Not Implemented     
					//    break;

				case Types.SMALLINT :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY)
							|| lsValue.equals(
								String.valueOf(Integer.MIN_VALUE)))
						{
							aaStatement.setNull(
								aiIndex,
								Types.SMALLINT);
						}
						else
						{
							aaStatement.setInt(
								aiIndex,
								Integer.parseInt(lsValue));
						}
						break;
					}
					// case Types.STRUCT :
					//  Not Implemented     
					//    break;

					// case Types.TIME :
					//  Not Implemented     
					//     break;

				case Types.TIMESTAMP :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							aaStatement.setNull(
								aiIndex,
								Types.TIMESTAMP);
						}
						else
						{
							aaStatement.setTimestamp(
								aiIndex,
								new Timestamp(Long.parseLong(lsValue)));
						}
						break;
					}
					// case Types.VARBINARY :
					//  Not Implemented     
					//    break;

				case Types.VARCHAR :
					{
						if ((lsValue == null)
							|| lsValue.equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							aaStatement.setNull(aiIndex, Types.VARCHAR);
						}
						else
						{
							aaStatement.setString(aiIndex, lsValue);
						}
						break;
					}
			}
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}
}
