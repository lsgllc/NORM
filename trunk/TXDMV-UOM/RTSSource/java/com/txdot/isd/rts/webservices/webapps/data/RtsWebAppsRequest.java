package com.txdot.isd.rts.webservices.webapps.data;import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;/* * RtsWebAppsRequest.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Min Wang		06/01/2008	Created Class * 							defect 9676 Ver MyPlates_POS * --------------------------------------------------------------------- *//** * Request data for WebApps Service. * * @version	MyPlates_POS		06/26/2008 * @author	mwang * <br>Creation Date:			06/26/2008 12:06:00 *//* &RtsWebAppsRequest& */public class RtsWebAppsRequest extends RtsAbstractRequest{/* &RtsWebAppsRequest'csItrntTraceNo& */	private String csItrntTraceNo;/* &RtsWebAppsRequest'csPymntOrderId& */	private String csPymntOrderId;/* &RtsWebAppsRequest'csRegPltNo& */	private String csRegPltNo;/* &RtsWebAppsRequest'csReqIpAddr& */	private String csReqIpAddr;		/**	 * Return the value of ItrntTraceNo	 * 	 * @return String	 *//* &RtsWebAppsRequest.getItrntTraceNo& */	public String getItrntTraceNo()	{		return csItrntTraceNo;	}	/**	 * Return the value of PymntOrderId	 * 	 * @return String	 *//* &RtsWebAppsRequest.getPymntOrderId& */	public String getPymntOrderId()	{		return csPymntOrderId;	}	/**	 * Return the value of RegPltNo	 * 	 * @return String	 *//* &RtsWebAppsRequest.getRegPltNo& */	public String getRegPltNo()	{		return csRegPltNo;	}	/**	 * Return the value of ReqIpAddr	 * 	 * @return String	 *//* &RtsWebAppsRequest.getReqIpAddr& */	public String getReqIpAddr()	{		return csReqIpAddr;	}	/**	 * Set the value of ItrntTraceNo	 * 	 * @param asItrntTraceNo String	 *//* &RtsWebAppsRequest.setItrntTraceNo& */	public void setItrntTraceNo(String asItrntTraceNo)	{		csItrntTraceNo = asItrntTraceNo;	}	/**	 * Set the value of PymntOrderId	 * 	 * @param asPymntOrderId String	 *//* &RtsWebAppsRequest.setPymntOrderId& */	public void setPymntOrderId(String asPymntOrderId)	{		csPymntOrderId = asPymntOrderId;	}	/**	 * Set the value of RegPltNo	 * 	 * @param asRegPltNo String	 *//* &RtsWebAppsRequest.setRegPltNo& */	public void setRegPltNo(String asRegPltNo)	{		csRegPltNo = asRegPltNo;	}	/**	 * Set the value of ReqIpAddr	 * 	 * @param asReqIpAddr String	 *//* &RtsWebAppsRequest.setReqIpAddr& */	public void setReqIpAddr(String asReqIpAddr)	{		csReqIpAddr = asReqIpAddr;	}}/* #RtsWebAppsRequest# */