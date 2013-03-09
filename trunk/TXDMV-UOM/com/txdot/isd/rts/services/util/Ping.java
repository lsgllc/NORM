package com.txdot.isd.rts.services.util;

/**
 * A small simple object that Send Cache uses to send across the server to determine server status and DB status
 * 
 * @date (11/28/01 3:02:30 PM)
 * @author: Michael Abernethy
 * 
 *  ---------------------------------------------------------------
 *  Change History:
 *  Name        Date        Description
 * 
 *  ---------------------------------------------------------------
 */
public class Ping implements java.io.Serializable {
	private final static long serialVersionUID = -1707077500294227056L;
	private boolean shouldCheckDB;
	private boolean dbOk;
/**
 * Creates a Ping 
 */
public Ping() {}
/**
 * Returns whether the DB should be pinged
 * @return boolean
 */
public boolean isCheckDB() {
	return shouldCheckDB;
}
/**
 * Returns whether the DB is up
 * @return boolean
 */
public boolean isOk() {
	return dbOk;
}
/**
 * Sets whether the DB should be pinged
 * @param newCheckDB boolean
 */
public void setCheckDB(boolean newCheckDB) {
	shouldCheckDB = newCheckDB;
}
/**
 * Sets whether the DB is up
 * @param newOk boolean
 */
public void setOk(boolean newOk) {
	dbOk = newOk;
}
}
