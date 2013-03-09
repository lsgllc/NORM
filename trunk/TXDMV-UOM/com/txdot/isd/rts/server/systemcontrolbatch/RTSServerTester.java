package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * RTSServerTester.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/17/2004	Update to use constant to call testing
 *							interface.  Use a vector to pass
 *							testing parameters to server side.
 *							Set RTSDeskTop.setDbStatus to true for test.
 *							modify main()
 *							defect 6785 Ver 5.2.0
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		10/10/2005	Constant cleanup.
 *							defect 7897 Ver 5.2.3
 * K Harrell	11/03/2005	Rename of RTSApplicationController 
 * 							setDBStatus() to setDBUp(). 
 * 							defect 8337 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This class starts up development testing for sendtrans
 *
 * @version	5.2.3 			11/03/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		12/02/2001 13:57:31
 */

public class RTSServerTester
{
	/**
	 * RTSServerTester constructor comment.
	 */
	public RTSServerTester()
	{
		super();
	}
	/**
	 * This makes the server side call to start sendtrans
	 * from client.
	 * <p>This is for development testing.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			// defect 6785
			// use constants
			// defect 8337
			// use renamed RTSApplicationController.setDBUp vs. 
			// setDBStatus
			// signal that database is up so we can get through comm
			com
				.txdot
				.isd
				.rts
				.client
				.desktop
				.RTSApplicationController
				.setDBUp(
				true);
			// end defect 8337 

			Vector lvTestParameters = new Vector(3);

			// use this section of code to test sending just a select 
			// group of counties
			lvTestParameters.add(new Integer(11));
			lvTestParameters.add(new Integer(11));

			// use this parameter to use the R99 transid.
			// Counties must also be specified
			//lvTestParameters.add(new Boolean(true));

			com.txdot.isd.rts.services.communication.Comm.sendToServer(
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.GeneralConstant
					.SYSTEMCONTROLBATCH,
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.SystemControlBatchConstant
					.TEST_SERVER_BATCH,
				lvTestParameters);
			// end defect 6785
		}
		catch (RTSException aeRTSex)
		{
			System.out.println(aeRTSex.toString());
		}
	}
}
