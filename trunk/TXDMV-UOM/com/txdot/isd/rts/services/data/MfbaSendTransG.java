package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.systemcontrolbatch.MFTrans;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.ApplicationControlConstants;
import com.txdot.isd.rts.services.util.constants.MessageConstants;

/*
 * MfbaSendTransG.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/09/2006	New Class
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	10/16/1006	Formatting pass
 * 							defect 6701 Ver Exempts
 * Ray Rowehl 	11/03/2006	Null out formatters when done.
 * 							defect 6701 Ver Exempts
 * J Rue		02/26/2007	Active U version
 * M Reyes		10/13/2010	Modified TRANS_BASIC_BUFF_DESC_AREA
 * 							defect 10595 Ver POS_660
 * ---------------------------------------------------------------------
 */

/**
 * This class directs the building of the SendTrans Data Buffer.
 *
 * @version	6.6.0			10/13/2010 
 * @author	Ray Rowehl
 * <br>Creation Date:		10/09/2006 07:30:00
 */

public class MfbaSendTransG
{
	// defect 10595
	//private final static String TRANS_BASIC_BUFF_DESC_AREA =
		//"TRANS   00MVFUNC  00SRFUNC  00INVFUNC 00FUNDFUNC00TRINVDTL00TRFDSDTL00TRANPYMT00LOGFUNC 00";
	private final static String TRANS_BASIC_BUFF_DESC_AREA =
	    "TRANS   00MVFUNC  00SRFUNC  00INVFUNC 00FUNDFUNC00TRINVDTL00TRFDSDTL00PRMTTRAN00TRANPYMT00LOGFUNC 00"; 
		
	//private final static String TRANS_BASIC_BUFF_DESC_AREA_V =
    	//"TRANS   00MVFUNC  00SRFUNC  00INVFUNC 00FUNDFUNC00TRINVDTL00TRFDSDTL00PRMTTRAN00TRANPYMT00LOGFUNC 00"; 
	// end defect 10595
	/**
	 * Determines which CICS Cobol version to work with and calls 
	 * the appropiate formtter.
	 * 
	 * @param aaMfAccess
	 * @param aaMFTrans
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector formatSendTransData(
		MfAccess aaMfAccess,
		MFTrans aaMFTrans)
		throws RTSException
	{
		// send the string for counting up the data buffers
		StringBuffer lsTransString =
			new StringBuffer(TRANS_BASIC_BUFF_DESC_AREA);

		// response vector
		Vector lvResponse = null;

		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			// Use the T Formatter
			MfbaSendTransT laSendTransT =
				new MfbaSendTransT(aaMfAccess);
			lvResponse =
				laSendTransT.formatSendTransData(
					lsTransString.toString(),
					aaMFTrans);
			laSendTransT = null;
			return lvResponse;
		}
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			// Use the T Formatter
			MfbaSendTransU laSendTransU =
				new MfbaSendTransU(aaMfAccess);
			lvResponse =
				laSendTransU.formatSendTransData(
					lsTransString.toString(),
					aaMFTrans);
			laSendTransU = null;
			return lvResponse;
		}
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			// Use the V Formatter
			lsTransString =
						new StringBuffer(TRANS_BASIC_BUFF_DESC_AREA);
			MfbaSendTransV laSendTransV =
				new MfbaSendTransV(aaMfAccess);
			lvResponse =
				laSendTransV.formatSendTransData(
					lsTransString.toString(),
					aaMFTrans);
			laSendTransV = null;
			return lvResponse;
		}
		else
		{
			// Don't know what they tried.  We do not support it.
			System.out.println(
				SystemProperty.getMFInterfaceVersionCode()
					+ MessageConstants.SE_MF_VERSION_NOT_IMPLEMENTED);
			throw new RTSException(
				RTSException.MF_DOWN,
				SystemProperty.getMFInterfaceVersionCode()
					+ MessageConstants.SE_MF_VERSION_NOT_IMPLEMENTED,
				MessageConstants.SF_MFACCESS_ERROR);
		}
	}
}
