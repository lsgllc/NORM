package com.txdot.isd.rts.server.webapps.general.servlets;

// java
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.server.webapps.addresschange.AddrChgServerBusiness;
import com.txdot.isd.rts.server.webapps.common.business.IVTRSServerBusiness;
import com.txdot.isd.rts.server.webapps.registrationrenewal.RegRenProcessingServerBusiness;
import com.txdot.isd.rts.server.webapps.registrationrenewal.RegistrationRenewalServerBusiness;

import com.txdot.isd.rts.services.data.RefundData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.webapps.communication.Comm;
import com.txdot.isd.rts.services.webapps.exception.RTSWebAppsException;
import com.txdot.isd.rts.services.webapps.util.CommunicationProperty;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegistrationRenewalConstants;

/*
 * RTSWebAppsServlet.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Cliff Chen	09/23/2002	added conversation format support
 * Ray Rowehl	03/19/2003	add date time before stack traces
 *							defect 5653
 * B. Brown		04/05/2005  change init()
 * 							defect 7864
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * B. Brown		09/25/2008	Remove the call to laCMSB.loadAdminCache()
 * 							as it is no longer needed.
 * 							modify init()
 * 							defect 9669 Ver Defect_POS_B 
 * B. Brown		12/04/2009	Process refund results sent via HTTP from 
 * 							TxO.
 * 							modify performTask()
 * 							defect 10019 Ver defect_POS_H
 * B. Brown		12/08/2010	Modify for eReminder project
 * 							modify performTask()
 * 							defect 10610 Ver POS_670 
 *----------------------------------------------------------------------
 */

/**
 * RTS WebApps Servlet
 *  
 * @version	POS_670			12/08/2010
 * @author	Administrator
 * <br>Creation Date:		10/09/2001 17:55:52
 */
public class RTSWebAppsServlet extends HttpServlet
{
	/**
	 * Process incoming HTTP GET requests 
	 * 
	 * @param aaRequest Object that encapsulates the request to the servlet 
	 * @param aaResponse Object that encapsulates the response from the servlet
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	public void doGet(
		HttpServletRequest aaRequest,
		HttpServletResponse aaResponse)
		throws javax.servlet.ServletException, java.io.IOException
	{

		performTask(aaRequest, aaResponse);

	}
	/**
	 * Process incoming HTTP POST requests 
	 * 
	 * @param aaRequest Object that encapsulates the request to the servlet 
	 * @param aaResponse Object that encapsulates the response from the servlet
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	public void doPost(
		HttpServletRequest aaRequest,
		HttpServletResponse aaResponse)
		throws javax.servlet.ServletException, java.io.IOException
	{

		performTask(aaRequest, aaResponse);

	}
	/**
	 * Returns the servlet info string.
	 * 
	 * @return String
	 */
	public String getServletInfo()
	{

		return "RTSWebappsServlet";

	}
	/**
	 * Initializes the servlet.
	 */
	public void init()
	{

		// Populate the cache.
		try
		{

			com.txdot.isd.rts.services.communication.Comm.setIsServer(
				true);

			//Setting properties on server
			//to be externalized in to server properties in the future!
			SystemProperty.setOfficeIssuanceNo(290);
			SystemProperty.setSubStationId(0);
			SystemProperty.setWorkStationId(999);
			SystemProperty.setCurrentEmpId("IUSER");

			//Set up input to admin cache
			// defect 9669
//			com.txdot.isd.rts.services.data.GeneralSearchData 
//				laGenSearchData =
//				new com.txdot.isd.rts.services.data.GeneralSearchData();
//			laGenSearchData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
//			laGenSearchData.setIntKey2(SystemProperty.getSubStationId());
//			laGenSearchData.setDate1(new RTSDate(1900, 1, 1));
			// end defct 9669

			CacheManagerServerBusiness laCMSB =
				new CacheManagerServerBusiness();
			// defect 7864
			//lCMSB.loadStaticCache(null);
			laCMSB.loadStaticCache();
			// end defect 7864
			// defect 9669
			// the call to loadAdminCache is no longer necessary
			// laCMSB.loadAdminCache(laGenSearchData);
			// end defect 9669
		}
		catch (Exception leRTSEx)
		{
			System.out.println(
				"----- Problem with loading cache in servlet for server side ------");
			System.err.println(
				"RTSWebAppsServlet Problem "
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			leRTSEx.printStackTrace();
		}
	}
	/**
	 * Process incoming requests for information
	 * 
	 * @param aaRequest Object that encapsulates the request to the servlet 
	 * @param aaResponse Object that encapsulates the response from the servlet
	 */
	public void performTask(
		HttpServletRequest aaRequest,
		HttpServletResponse aaResponse)
	{

		try
		{
			// System.out.println("RTSWebappsServlet: ModuleName="+lsModuleName+", FunctionID="+lsFunctionId+ "Object="+lsData +" --->1");        		
			String lsModuleName =
				(String) aaRequest.getParameter("ModuleName");
			String lsFunctionId =
				(String) aaRequest.getParameter("FunctionId");
			String lsData = (String) aaRequest.getParameter("Data");

			String lsFormat = (String) aaRequest.getHeader("Format");

			int liLen = lsData.length();
			int li1 = lsData.charAt(liLen - 2);
			int li2 = lsData.charAt(liLen - 1);
			int liCPos = 0;

			// IMPORTANT .........
			//This portion is added for NT client.
			if (li1 == 13 && li2 == 10)
			{
				liCPos = 2;
			}
			// This portion is added for UNIX,AIX client.
			else
			{
				if (li2 == 10)
					liCPos = 1;
			}
			// .......

			Object laObject =
				Comm.StringToObj(
					lsData.substring(0, lsData.length() - liCPos));

			int liModuleName = Integer.parseInt(lsModuleName);
			int liFunctionId = Integer.parseInt(lsFunctionId);
			
			//changes for debugging test TxO for UAT
			if (CommunicationProperty.getRegion().equalsIgnoreCase("test"))
			{
			   Log.write(Log.SQL_EXCP, this, " ");
			   Log.write(Log.SQL_EXCP, null, "Has received a request from:");
			   Log.write(Log.SQL_EXCP, null, "RemoteAddr = " + aaRequest.getRemoteAddr());
			   Log.write(Log.SQL_EXCP, null, "RemoteHost = " + aaRequest.getRemoteHost());
			   Log.write(Log.SQL_EXCP, null, "RemoteUser = " + aaRequest.getRemoteUser());
			   Log.write(Log.SQL_EXCP, null, "lsModuleName = " + lsModuleName);
			   Log.write(Log.SQL_EXCP, null, "lsFunctionId = " + lsFunctionId);
			}
		   // end changes for debugging test TxO for UAT

			switch (liModuleName)
			{
				case CommonConstants.ADDRESS_CHANGE :
					AddrChgServerBusiness lAddrChgServerBusiness =
						new AddrChgServerBusiness();
					laObject =
						lAddrChgServerBusiness.processData(
							liModuleName,
							liFunctionId,
							laObject);
					break;
				case CommonConstants.REGISTRATION_RENEWAL :
					RegistrationRenewalServerBusiness 
						lRegistrationRenewalServerBusiness =
						new RegistrationRenewalServerBusiness();
					// defect 10019
					if (laObject instanceof RefundData)
					{
						RegRenProcessingServerBusiness laRegRenBO =
							new RegRenProcessingServerBusiness();
						laRegRenBO.processData(
							liModuleName,
							liFunctionId,
							laObject);		
					}
					else
					{
					// end defect 10019
						laObject =
							lRegistrationRenewalServerBusiness.processData(
								liModuleName,
								liFunctionId,
								laObject);
					}
					break;
				
				// defct 10610
				case CommonConstants.EREMINDER :
					IVTRSServerBusiness laIVTRSServerBusiness  =
						new IVTRSServerBusiness();
					laObject =
						laIVTRSServerBusiness.processData(
							liModuleName,
							liFunctionId,
							laObject);
					break;		
				// end defect 10610
					
				default :
					throw new Exception(
						"RTSWebappsServlet - No such module name:"
							+ lsModuleName);

			}

			PrintWriter laOut = aaResponse.getWriter();
			Object laRetObj = laObject;

			// Conversation format support
			if (lsFormat != null
				&& lsFormat.equalsIgnoreCase("object"))
				// object format conversation, convert object to string.
				laRetObj = Comm.objToString(laObject);

			// else, string format conversion, just return the string result,
			// this is for returning a long string, which can not be in object format.

			laOut.println(laRetObj);
			// System.out.println("RTSWebappsServlet: ModuleName="+lsModuleName+", FunctionID="+lsFunctionId+" ---> 2");        		        

		}
		catch (RTSException leRTSEx)
		{
			// RTSException
			System.err.println(
				"RTSWebAppsServlet Problem "
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			leRTSEx.printStackTrace();

			try
			{
				PrintWriter laOut = aaResponse.getWriter();

				// send back RTSWebAppsException, not RTSException
				RTSWebAppsException laRTSWebAppsException =
					new RTSWebAppsException(leRTSEx);
				laOut.println(Comm.objToString(laRTSWebAppsException));

			}
			catch (IOException leIOEx)
			{
				Log.write(
					Log.APPLICATION,
					this,
					leIOEx.getMessage());
			}
			catch (RTSWebAppsException leRTSWAEx)
			{

				// Cannot send back the exception            
				Log.write(
					Log.APPLICATION,
					this,
					leRTSWAEx.getMessage());
				System.err.println(
					"RTSWebAppsServlet Problem "
						+ " "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
				leRTSWAEx.printStackTrace();
			}
		}
		catch (Throwable leEx)
		{
			// all other Exceptions	
			System.err.println(
				"RTSWebAppsServlet Problem "
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			leEx.printStackTrace();
			try
			{
				PrintWriter laOut = aaResponse.getWriter();

				// send back RTSWebAppsException
				RTSWebAppsException leRTSWAEx =
					new RTSWebAppsException(
						RTSWebAppsException.JAVA_ERROR,
						leEx);
				laOut.println(Comm.objToString(leRTSWAEx));
			}
			catch (Throwable leEx2)
			{
				// Cannot send back the exception
				System.err.println(
					"RTSWebAppsServlet Problem "
						+ " "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
				leEx2.printStackTrace();
			}
		}
	}
}
