package com.txdot.isd.rts.services.exception;

import com.txdot.isd.rts.services.data.ErrorMessagesData;

/*
 *
 * PCErrorLog.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/11/2005	RTS 5.2.3 clean up
 *							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                 
 * ---------------------------------------------------------------------
 */

/**
 * Log error on the database
 * 
 * @version 5.2.3		06/19/2005
 * @author	Nancy Ting
 * <br>Creation Date: 	03/26/2002 20:14:10
 */
public class PCErrorLog implements Runnable
{
	private ErrorMessagesData caErrorMsgData;
	/**
	 * PCErrorLog constructor comment.
	 */
	public PCErrorLog()
	{
		super();
	}
	/**
	 * Get ErrorMsgData
	 * 
	 * @return ErrorMessagesData
	 */
	public ErrorMessagesData getErrorMsgData()
	{
		return caErrorMsgData;
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
		if (caErrorMsgData != null)
		{
			com.txdot.isd.rts.services.util.Log.write(
				com.txdot.isd.rts.services.util.Log.SQL_EXCP,
				this,
				caErrorMsgData.getErrMsgType()
					+ ":"
					+ caErrorMsgData.getErrMsgDesc());
		}

	}
	/**
	 * Set ErrorMsgData
	 * 
	 * @param aaNewErrorMsgData ErrorMessagesData
	 */
	public void setErrorMsgData(ErrorMessagesData aaNewErrorMsgData)
	{
		caErrorMsgData = aaNewErrorMsgData;
	}
}