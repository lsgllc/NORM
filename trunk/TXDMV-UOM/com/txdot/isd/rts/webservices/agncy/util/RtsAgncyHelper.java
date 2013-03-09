package com.txdot.isd.rts.webservices.agncy.util;

import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.services.data.WebAgencyAuthCfgData;
import com.txdot.isd.rts.services.data.WebAgencyAuthData;
import com.txdot.isd.rts.services.data.WebAgencyBatchData;
import com.txdot.isd.rts.services.data.WebAgencyData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncy;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncyAuthCfg;

/*
 * RtsAgncyHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/26/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/01/2011	Add method for getting agency list.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	03/18/2011	Modify so at least Agency data can be updated.
 * 							modify addUpdateAgency()
 * 							defect 10718 Ver 6.7.1
 * K McKee		07/06/2011  Added call to updateAgencyAuthCfg
 * 							defect 10718 Ver 6.8.0
 * K McKee		07/07/2011  Set offissuanceno for insertAgencyAuth
 * 							defect 10718 Ver 6.8.0
 * D Hamilton	07/07/2011  Added method & call to updateAgencyAuth
 * 							defect 10718 Ver 6.8.0
 * K McKee      08/19/2011  Added method to select the pending batches 
 * 							for an agency
 * 							add chkForPendingWebAgencyBatch()
 * 							defect 10729 Ver 6.8.1
 * K McKee      08/19/2011  Added method to set the deleteindi for agency
 * 							add deleteAgency()
 * 							defect 10729 Ver 6.8.1
 * K McKee      08/19/2011  Added method to set the deleteindi for agency auth
 * 							add deleteAgencyAuth()
 * 							defect 10729 Ver 6.8.1
 * K McKee      08/19/2011  Added method to set the deleteindi for agency auth cfg
 * 							add deleteAgencyAuthCfg()
 * 							defect 10729 Ver 6.8.1
 * K McKee      08/22/2011  Renamed qryPendingWebAgencyBatch() method to 
 * 							updPendingWebAgencyBatch
 * 							defect 10729 Ver 6.8.1
 * K McKee      09/01/2011  setUpdtngAgntIdntyNo
 *                          method updateAgency()
 *                          defect 10729 Ver 6.8.1
 * K McKee      09/15/2011  Delete the Agents for an Agency
 *                          added deleteAgencyAgentSecurity()
 *                          defect 10729 Ver 6.8.1
 * K McKee      09/15/2011  Insert agency cfg and auth cfg for
 *                          adding an agency to another county
 *                          modify addUpdateAgency()
 *                          defect 10729 Ver 6.8.1
 * K McKee      10/03/2011  Logged the runtime exception
 *                          modify getAgencyName()
 *                          defect 10729 Ver 6.9.0
 * K McKee      10/20/2011  Changed code to allow for Auth/Auth Cfg array
 *                          modify getAgencyList(),addUpdateAgency(),
 *							       insertAgencyAuth(),updateAgencyAuth(),
 *                           	   insertAgencyAuthCfg(),updateAgencyAuthCfg(),
 *                          	   deleteAgencyAuth(),deleteAgencyAuthCfg(),
 *                                 updPendingWebAgencyBatch()
 *                          add qryAgencyAuthCfg();
 *                          defect 11151 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Contains helper methods used by the web services.
 * 
 * @version 6.9.0. 10/20/2011
 * @author Ray Rowehl <br>
 *         Creation Date: 01/26/2011 12:23:00
 */

public class RtsAgncyHelper
{
	/**
	 * Get the list of agencies
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return Vector RtsWebAgncyAuthCfg
	 * @throws RTSException
	 */
	public Vector getAgencyList(RtsWebAgncy aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgency laSQL = new WebAgency(aaDBA);

		Vector lvAgencies = laSQL.qryWebAgencies(aaRequest);

		Vector lvAgencyReturn = new Vector();

		RtsWebAgncy laRtsWebAgncy = new RtsWebAgncy();

		if (lvAgencies.size() > 0)
		{
			//	 For each agency, retreive the RTS.RTS_WEB_AGNCY_AUTH and
			//     RTS.RTS_WEB_AGNCY_AUTH_CFG   
			
			// defect 11151
			for (int i = 0; i < lvAgencies.size(); i++)
			{
				laRtsWebAgncy = (RtsWebAgncy) lvAgencies.elementAt(i);
				RtsWebAgncyAuthCfg[] larrRtsWebAgncyAuthCfg = 
					qryAgencyAuthCfg(laRtsWebAgncy, aaDBA);
				laRtsWebAgncy.setAgncyAuthCfgs(larrRtsWebAgncyAuthCfg);
				lvAgencyReturn.add(laRtsWebAgncy);
			}
			// end defect 11151
		}
		return lvAgencyReturn;
	}

	/**
	 * Get the Agency Name.
	 * 
	 * @param aiAgncyIdntyNo
	 *            int
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return String lsAgncyName
	 * @throws RTSException
	 */
	public String getAgencyName(int aiAgncyIdntyNo, DatabaseAccess aaDBA)
			throws RTSException
	{
		String lsAgncyName = "";
		WebAgencyData laSQLData = new WebAgencyData();
		laSQLData.setAgncyIdntyNo(aiAgncyIdntyNo);
		WebAgency laSQL = new WebAgency(aaDBA);

		Vector lvReturnData = laSQL.qryWebAgency(laSQLData);

		if (lvReturnData.size() == 1)
		{
			WebAgencyData laReturnElement = (WebAgencyData) lvReturnData
					.elementAt(0);
			lsAgncyName = laReturnElement.getName1();

		}
		else
		{
			Log.write(Log.APPLICATION,
				this,"getAgencyName - aiAgncyIdntyNo = "
				+ aiAgncyIdntyNo
				+ " ******  ERROR - 2300 ****  - more than one row returned");
			throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}
		return lsAgncyName;
	}

	/**
	 * Process an add Agency Update Request
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return RtsWebAgncy
	 * @throws RTSException
	 */
	public RtsWebAgncy addUpdateAgency(RtsWebAgncy aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		// defect 11151
		RtsWebAgncyAuthCfg laRtsWebAgncyAuthCfg = aaRequest
				.getAgncyAuthCfgs()[0];
		// end defect 11151
		laRtsWebAgncyAuthCfg.setUpdtngAgntIdntyNo(Integer
				.parseInt(aaRequest.getUpdtngUserName()));

		if (aaRequest.getAgncyIdntyNo() < 1)
		{
			// This is an add.
			WebAgencyData laAgencyReturnData = insertAgency(aaRequest,
					aaDBA);

			// copy over the agency idnty no.
			aaRequest.setAgncyIdntyNo(laAgencyReturnData
					.getAgncyIdntyNo());
			laRtsWebAgncyAuthCfg.setOfcIssuanceNo(aaRequest
					.getInitOfcNo());
			laRtsWebAgncyAuthCfg.setAgncyIdntyNo(aaRequest
					.getAgncyIdntyNo());
			// defect 11151
			laRtsWebAgncyAuthCfg.setSubconId(aaRequest
					.getAgncyAuthCfgs()[0].getSubconId());
			// end defect 11151

			insertAgencyAuth(laRtsWebAgncyAuthCfg, aaDBA);
			insertAgencyAuthCfg(laRtsWebAgncyAuthCfg, aaDBA);

			return aaRequest;
		}
		// This is an update.

		// defect 10729 Begin
		// if the AgncyAuthIdntyNo is zero, the agency is being added to a
		// differenct county so insert the auth and auth config.
		if (laRtsWebAgncyAuthCfg.getAgncyAuthIdntyNo() == 0)
		{
			WebAgencyAuthData laAgencyAuthReturnData = insertAgencyAuth(
					laRtsWebAgncyAuthCfg, aaDBA);

			// copy over the agency auth idnty no
			laRtsWebAgncyAuthCfg
					.setAgncyAuthIdntyNo(laAgencyAuthReturnData
							.getAgncyAuthIdntyNo());
		
			insertAgencyAuthCfg(laRtsWebAgncyAuthCfg, aaDBA);
		}
		// defect 10729 END
		else
		{
			if (aaRequest.isChngAgncy())
			{
				updateAgency(aaRequest, aaDBA);
			}
			// KMcKee -- added update to agency auth cfg
			// DHamilton -- added update to agency auth (for SubCon ID)
			if (aaRequest.isChngAgncyCfg())
			{
				updateAgencyAuthCfg(laRtsWebAgncyAuthCfg, aaDBA);
				updateAgencyAuth(laRtsWebAgncyAuthCfg, aaDBA);
			}
		}
		return aaRequest;
	}

	/**
	 * Query RTS.RTS_WEB_AGNCY_AUTH and RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return RtsWebAgncyAuthCfg[]
	 * @throws RTSException
	 */
	private RtsWebAgncyAuthCfg[] qryAgencyAuthCfg(
			RtsWebAgncy aaRequest, DatabaseAccess aaDBA)
			throws RTSException
	{
		WebAgencyAuth laSQL = new WebAgencyAuth(aaDBA);

		Vector lvAgencyAuthAndConfig = laSQL
				.qryWebAgencyAuthAndCfg(aaRequest.getAgncyIdntyNo());

		RtsWebAgncyAuthCfg[] laarRtsWebAgncyAuthCfg = new RtsWebAgncyAuthCfg[lvAgencyAuthAndConfig
				.size()];

		int index = 0;

		if (lvAgencyAuthAndConfig.size() > 0)
		{
			for (Iterator laIter = lvAgencyAuthAndConfig.iterator(); laIter
					.hasNext();)
			{
				WebAgencyAuthCfgData laAgencyAuthCfgData = (WebAgencyAuthCfgData) laIter
						.next();

				RtsWebAgncyAuthCfg laRtsWebAgncyAuthCfg = new RtsWebAgncyAuthCfg(
						laAgencyAuthCfgData);

				laarRtsWebAgncyAuthCfg[index] = laRtsWebAgncyAuthCfg;
				index++;
			}
		}
		return laarRtsWebAgncyAuthCfg;
	}

	/**
	 * Insert Web Agency.
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return WebAgencyData
	 * @throws RTSException
	 */
	private WebAgencyData insertAgency(RtsWebAgncy aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyData laSQLData = new WebAgencyData();

		laSQLData.setAgncyTypeCd(aaRequest.getAgncyTypeCd());
		laSQLData.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest
				.getUpdtngUserName()));
		laSQLData.setName1(aaRequest.getName1());
		laSQLData.setName2(aaRequest.getName2());
		laSQLData.getAddressData().setSt1(aaRequest.getSt1());
		laSQLData.getAddressData().setSt2(aaRequest.getSt2());
		laSQLData.getAddressData().setCity(aaRequest.getCity());
		laSQLData.getAddressData().setState(aaRequest.getState());
		laSQLData.getAddressData().setZpcd(aaRequest.getZpCd());
		laSQLData.getAddressData().setZpcdp4(aaRequest.getZpCdP4());
		laSQLData.setPhone(aaRequest.getPhone());
		laSQLData.setEMail(aaRequest.getEMail());
		laSQLData.setCntctName(aaRequest.getCntctName());
		laSQLData.setInitOfcNo(aaRequest.getInitOfcNo());
		laSQLData.setDeleteIndi(0);

		WebAgency laSQL = new WebAgency(aaDBA);

		WebAgencyData laReturnData = laSQL.insWebAgency(laSQLData);

		return laReturnData;
	}

	/**
	 * Update Agency data.
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	private void updateAgency(RtsWebAgncy aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyData laSQLData = new WebAgencyData();

		laSQLData.setAgncyIdntyNo(aaRequest.getAgncyIdntyNo());
		laSQLData.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest
				.getUpdtngUserName()));
		laSQLData.setAgncyTypeCd(aaRequest.getAgncyTypeCd());
		laSQLData.setName1(aaRequest.getName1());
		laSQLData.setName2(aaRequest.getName2());
		laSQLData.getAddressData().setSt1(aaRequest.getSt1());
		laSQLData.getAddressData().setSt2(aaRequest.getSt2());
		laSQLData.getAddressData().setCity(aaRequest.getCity());
		laSQLData.getAddressData().setState(aaRequest.getState());
		laSQLData.getAddressData().setZpcd(aaRequest.getZpCd());
		laSQLData.getAddressData().setZpcdp4(aaRequest.getZpCdP4());
		laSQLData.setPhone(aaRequest.getPhone());
		laSQLData.setEMail(aaRequest.getEMail());
		laSQLData.setCntctName(aaRequest.getCntctName());
		laSQLData.setInitOfcNo(aaRequest.getInitOfcNo());
		laSQLData.setDeleteIndi(0);

		WebAgency laSQL = new WebAgency(aaDBA);

		laSQL.updWebAgency(laSQLData);
	}

	/**
	 * Insert RTS.RTS_WEB_AGNCY_AUTH
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return WebAgencyAuthData
	 * @throws RTSException
	 */
	private WebAgencyAuthData insertAgencyAuth(
			RtsWebAgncyAuthCfg aaRequest, DatabaseAccess aaDBA)
			throws RTSException
	{
		WebAgencyAuthData laSQLData = new WebAgencyAuthData();

		laSQLData.setAgncyIdntyNo(aaRequest.getAgncyIdntyNo());
		laSQLData.setOfcIssuanceNo(aaRequest.getOfcIssuanceNo());
		laSQLData.setSubconId(aaRequest.getSubconId());
		laSQLData.setDeleteIndi(0);

		WebAgencyAuth laSQL = new WebAgencyAuth(aaDBA);

		WebAgencyAuthData laReturnData = laSQL
				.insWebAgencyAuth(laSQLData);
		// copy over the agency auth idnty no
		aaRequest.setAgncyAuthIdntyNo(laReturnData
				.getAgncyAuthIdntyNo());

		return laReturnData;
	}

	/**
	 * Update RTS.RTS_WEB_AGNCY_AUTH
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	private void updateAgencyAuth(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyAuthData laSQLData = new WebAgencyAuthData();

		laSQLData.setAgncyAuthIdntyNo(aaRequest.getAgncyAuthIdntyNo());
		laSQLData.setAgncyIdntyNo(aaRequest.getAgncyIdntyNo());
		laSQLData.setOfcIssuanceNo(aaRequest.getOfcIssuanceNo());
		laSQLData.setSubconId(aaRequest.getSubconId());
		laSQLData.setDeleteIndi(0);

		WebAgencyAuth laSQL = new WebAgencyAuth(aaDBA);

		laSQL.updWebAgencyAuth(laSQLData);
	}

	/**
	 * Insert RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	private void insertAgencyAuthCfg(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyAuthCfgData laSQLData = new WebAgencyAuthCfgData();

		laSQLData.setAgncyAuthIdntyNo(aaRequest.getAgncyAuthIdntyNo());
		laSQLData.setKeyEntryCd(aaRequest.getKeyEntryCode());
		laSQLData.setIssueInvIndi(aaRequest.isIssueInvIndi() ? 1 : 0);
		laSQLData.setExpProcsngCd(aaRequest.getExpPrcsngCd());
		laSQLData.setExpProcsngMos(aaRequest.getExpPrcsngMos());
		laSQLData.setMaxSubmitDays(aaRequest.getMaxSubmitDays());
		laSQLData.setMaxSubmitCount(aaRequest.getMaxSubmitCount());
		laSQLData.setPilotIndi(aaRequest.isPilotIndi() ? 1 : 0);
		laSQLData.setUpdtngAgntIdntyNo(aaRequest.getUpdtngAgntIdntyNo());
		laSQLData.setDeleteIndi(0);

		WebAgencyAuthCfg laSQL = new WebAgencyAuthCfg(aaDBA);

		laSQL.insWebAgencyAuthCfg(laSQLData);
	}

	/**
	 * Update RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	private void updateAgencyAuthCfg(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyAuthCfgData laSQLData = new WebAgencyAuthCfgData();

		laSQLData.setAgncyAuthIdntyNo(aaRequest.getAgncyAuthIdntyNo());
		laSQLData.setKeyEntryCd(aaRequest.getKeyEntryCode());
		laSQLData.setIssueInvIndi(aaRequest.isIssueInvIndi() ? 1 : 0);
		laSQLData.setExpProcsngCd(aaRequest.getExpPrcsngCd());
		laSQLData.setExpProcsngMos(aaRequest.getExpPrcsngMos());
		laSQLData.setMaxSubmitDays(aaRequest.getMaxSubmitDays());
		laSQLData.setMaxSubmitCount(aaRequest.getMaxSubmitCount());
		laSQLData.setPilotIndi(aaRequest.isPilotIndi() ? 1 : 0);
		laSQLData
				.setUpdtngAgntIdntyNo(aaRequest.getUpdtngAgntIdntyNo());
		laSQLData.setDeleteIndi(0);

		WebAgencyAuthCfg laSQL = new WebAgencyAuthCfg(aaDBA);

		laSQL.updWebAgencyAuthCfg(laSQLData);
	}

	/**
	 * Process a delete Request for RTS.RTS_WEB_AGNCY
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public void deleteAgency(RtsWebAgncy aaRequest, DatabaseAccess aaDBA)
			throws RTSException
	{
		WebAgencyData laSQLData = new WebAgencyData();

		laSQLData.setAgncyIdntyNo(aaRequest.getAgncyIdntyNo());
		laSQLData.setUpdtngAgntIdntyNo(Integer.parseInt(aaRequest
				.getUpdtngUserName()));
		laSQLData.setDeleteIndi(1);

		WebAgency laSQL = new WebAgency(aaDBA);

		laSQL.delWebAgency(laSQLData);
	}

	/**
	 * Process a delete Request for RTS.RTS_WEB_AGNCY_AUTH
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public void deleteAgencyAuth(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyAuthData laSQLData = new WebAgencyAuthData();

		laSQLData.setAgncyAuthIdntyNo(aaRequest.getAgncyAuthIdntyNo());
		laSQLData.setOfcIssuanceNo(aaRequest.getOfcIssuanceNo());
		laSQLData.setDeleteIndi(1);

		WebAgencyAuth laSQL = new WebAgencyAuth(aaDBA);

		laSQL.delWebAgencyAuth(laSQLData);
	}

	/**
	 * Process a delete Request for RTS.RTS_WEB_AGNCY_AUTH_CFG
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public void deleteAgencyAuthCfg(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyAuthData laSQLData = new WebAgencyAuthData();

		laSQLData.setAgncyAuthIdntyNo(aaRequest.getAgncyAuthIdntyNo());
		laSQLData.setOfcIssuanceNo(aaRequest.getOfcIssuanceNo());
		laSQLData.setDeleteIndi(1);

		WebAgencyAuthCfg laSQL = new WebAgencyAuthCfg(aaDBA);

		laSQL.delWebAgencyAuthCfg(laSQLData);
	}

	/**
	 * Process a delete Request for RTS.RTS_WEB_AGNT_SECURITY
	 * 
	 * @param aaRequest
	 *            RtsWebAgncy
	 * @param aaDBA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public void deleteAgencyAgentSecurity(RtsWebAgncy aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgentSecurity laSQL = new WebAgentSecurity(aaDBA);

		laSQL.delWebAgentSecurity(aaRequest.getAgncyIdntyNo());
	}

	/**
	 * Update the pending batches for an agency and set them to submitted
	 * 
	 * @param aaRequest
	 *            RtsWebAgncyAuthCfg
	 * @param aaDBA
	 *            DatabaseAccess
	 * @return int
	 * @throws RTSException
	 */
	public int updPendingWebAgencyBatch(RtsWebAgncyAuthCfg aaRequest,
			DatabaseAccess aaDBA) throws RTSException
	{
		WebAgencyBatch laBatchSql = new WebAgencyBatch(aaDBA);

		WebAgencyBatchData laSQLBatchData = new WebAgencyBatchData();
		laSQLBatchData.setAgncyIdntyNo(aaRequest.getAgncyIdntyNo());
		laSQLBatchData.setOfcIssuanceNo(aaRequest.getOfcIssuanceNo());

		return laBatchSql.updPendingWebAgencyBatch(laSQLBatchData);
	}
}
