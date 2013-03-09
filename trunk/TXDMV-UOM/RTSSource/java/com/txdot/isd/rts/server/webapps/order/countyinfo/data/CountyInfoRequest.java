package com.txdot.isd.rts.server.webapps.order.countyinfo.data;import com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest;/* * CountyInfoRequest.java * * (c) Texas Department of Transportation 2007 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Jeff S.		03/02/2007	Created class. * 							defect 9120 Ver Special Plates  * --------------------------------------------------------------------- *//** * Object used to handle the County Info Request Function. * * @version	Special Plates	03/02/2007 * @author	Jeff Seifert * <br>Creation Date:		03/02/2007 14:30:00 *//* &CountyInfoRequest& */public class CountyInfoRequest extends AbstractRequest{	/* &CountyInfoRequest'countyNo& */	private int countyNo = 0;		/**	 * Gets the county number that is needed to lookup the 	 * county info.	 * 	 * @return	 *//* &CountyInfoRequest.getCountyNo& */	public int getCountyNo()	{		return countyNo;	}	/**	 * Sets the county number that is needed to lookup the 	 * county info.	 * 	 * @param aiCountyNo int	 *//* &CountyInfoRequest.setCountyNo& */	public void setCountyNo(int aiCountyNo)	{		countyNo = aiCountyNo;	}}/* #CountyInfoRequest# */