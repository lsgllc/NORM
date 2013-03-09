package com.txdot.isd.rts.services.util;

/**
 * Insert the type's description here.
 * 
 * @date (6/12/02 4:25:44 PM)
 * @author: Marx Rajangam
 * 
 * ------------------------------------------------------------------
 * Change History:
 * Name				Date		Description
 * 
 * ------------------------------------------------------------------
 */
public class TSQTest implements Runnable {
/**
 * TSQTest constructor comment.
 */
public TSQTest() {
	super();
}
/**
 * Insert the method's description here.
 * 
 * @date (6/12/02 4:28:34 PM)
 * @author: Marx Rajangam
 * 
 * ---------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * Marx Rajangam	(6/12/02 4:28:34 PM)	Created the Method
 * ---------------------------------------------------------------
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) {

for (int j = 0; j < 99; j ++) {
	for (int i = 0; i < 99; i++)
	{
		TSQTest ts = new TSQTest(); 
		Thread tr = new Thread(ts);

		tr.start(); 
	}
} 
}
	/**
	 * When an object implementing interface <code>Runnable</code> is used 
	 * to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * <p>
	 * The general contract of the method <code>run</code> is that it may 
	 * take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
public void run() 
{
	com.txdot.isd.rts.server.dataaccess.MfAccess mf = new com.txdot.isd.rts.server.dataaccess.MfAccess(); 
	System.out.println(mf.getTsqName()); 

}
}
