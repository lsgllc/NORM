package com.txdot.isd.rts.services.exception;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;

import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.MFErrorData;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * MFErrLog.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove 	12/07/2004	Added setting of ComptCntyNo to captured 
 *							field so that it prints on VSAM error report
 *							Reformatted comments to standards.
 *							modify run()
 *							defect 6208 Ver 5.2.2
 * B Hargrove 	12/30/2004	More comment and Hungarian notation cleanup. 
 *							modify run()
 *							defect 6208 Ver 5.2.2
 * Ray Rowehl	02/11/2005	RTS 5.2.3 Code clean up.
 * 							format source, organize imports, 
 * 							rename fields.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3   
 * Ray Rowehl	08/23/2005	Move MFErrorData to services.data.
 * 							defect 7705 Ver 5.2.3             
 * ---------------------------------------------------------------------
 */

/**
 * Main frame error log
 *
 * @version	5.2.3		08/23/2005
 * @author	Nancy Ting
 * <br>Creation Date:	03/26/2002 20:17:59 
 */

public class MFErrLog implements Runnable
{
	private ErrorMessagesData caErrorMsgData;

	/**
	 * MFErrLog constructor comment.
	 */
	public MFErrLog()
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
	 * When an object creates a thread, starting the thread causes the 
	 * object's run method to be called in that separately executing 
	 * thread. 
	 */
	public void run()
	{
		if (caErrorMsgData != null)
		{
			CommonClientBusiness laCCB = new CommonClientBusiness();
			MFErrorData laErrorData = new MFErrorData();

			String lsHostName = CommonConstant.TXT_UNKNOWN;
			try
			{
				lsHostName =
					java.net.InetAddress.getLocalHost().getHostName();
			}
			catch (java.net.UnknownHostException leUHEx)
			{
				Log.write(Log.SQL_EXCP, this, leUHEx.getMessage());
			}

			if (caErrorMsgData != null)
			{
				RTSDate laDate = new RTSDate();

				//laErrorData.setMfDate(liDate);
				//laErrorData.setMfTime(liDate.getClockTime());
				// defect 6208
				// TODO this assumes we are on client side!
				laErrorData.setComptCntyNo(
					SystemProperty.getOfficeIssuanceNo());
				//end 6208

				laErrorData.setWsName(
					String.valueOf(SystemProperty.getWorkStationId()));
				laErrorData.setWsLUName(lsHostName);

				laErrorData.setPCDate(laDate.toString());
				laErrorData.setPCTime(laDate.getClockTime());
				laErrorData.setModuleName(
					CommonConstant.STR_SPACE_EMPTY);

				laErrorData.setErrNo(
					String.valueOf(caErrorMsgData.getErrMsgNo()));
				laErrorData.setErrLvl(caErrorMsgData.getErrMsgCat());
				laErrorData.setErrMsg(caErrorMsgData.getErrMsgDesc());

			}
			try
			{
				laCCB.processData(
					GeneralConstant.COMMON,
					CommonConstant.APP_MF_ERR_LOG,
					laErrorData);
			}
			catch (RTSException leRTSEx)
			{
				Log.write(Log.SQL_EXCP, this, leRTSEx.getMessage());
			}
		}
	}

	/**
	 * Set ErrorMsgData
	 * 
	 * @param newErrorMsgData ErrorMessagesData
	 */
	public void setErrorMsgData(ErrorMessagesData aaNewErrorMsgData)
	{
		caErrorMsgData = aaNewErrorMsgData;
	}
}
