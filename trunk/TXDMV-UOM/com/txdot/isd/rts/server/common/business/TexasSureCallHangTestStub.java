/**
 * 
 */
package com.txdot.isd.rts.server.common.business;

import java.util.Date;

import com.txdot.isd.rts.services.util.Log;

/*
 * TexasSureCallHangTest.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * Buck Woodson	08/26/2011	subclass created to emulate a TexasSure hang for junit testing
 *                          overrides run()
 *                          defect 10119 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/** 
 * subclass created to emulate a TexasSure hang for junit testing 
 * 
 * @version	6.8.1 08/26/2011 
 * @author	Buck Woodson 
 * @since   08/26/2011 
 */
public class TexasSureCallHangTestStub extends TexasSureCall
{
	/**
     * Run method to call TDI Interface.
     * Never returns to emulate hung TexasSure web service
     */
	public void run()
	{
		String lsStartMessage = this.toString() + " Starting Texas sure run: " + new Date();
		Log.write(Log.DEBUG, this, lsStartMessage);
		System.out.println(lsStartMessage);
		int i = 120000;
		
		try
		{
			while(i > 0)
			{
				//do nothing and just hang
				try
				{
					//if we don't relinquish we consume cpu and make the machine slow
					Thread.sleep(180);
					i--;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		finally
		{
			String lsEndMessage = this.toString() + " Exiting Texas sure run: " + new Date();
			Log.write(Log.DEBUG, this, lsEndMessage);
			System.out.println(lsEndMessage);
		}
	}
}
