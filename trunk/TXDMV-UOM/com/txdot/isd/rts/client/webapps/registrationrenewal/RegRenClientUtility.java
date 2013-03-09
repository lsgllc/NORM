package com.txdot.isd.rts.client.webapps.registrationrenewal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InternetRegRecData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
/*
 *
 * RegRenClientUtility.java
 *
 * (c) Texas Department of Transportation 2002.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford 	09/03/2002	CQU100003700. DB down handling.
 * 							checkOutVeh added.
 * B Brown		10/23/2002	CQU100004205.(putting and accessing Phase 2 
 * 							error msgs in rts_err_msgs). 
 * 							Changed method:checkOutVeh. 
 * Jeff S.		02/17/2005	Get code to standard. Changed a non-static
 * 							call to a static call.
 * 							modify dateTooOld()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	After an exception was displayed the code 
 * 							was requesting focus to the first component
 * 							but the first component was null.
 * 							modify checkOutVeh()
 * 							defect 7889 Ver 5.2.3
 * K Harrell	02/09/2008	Class cleanup; Implement ErrorsConstant.xxx
 * 							delete dateInFuture() 
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	09/23/2010	modify checkOutVeh() 
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Main Frame Reports Processing Class.
 *
 * @version	6.6.0			09/23/2010
 * @author	CCHEN2
 * <br>Creation Date:		06/05/2002 12:08:28
 */
public class RegRenClientUtility
{
	/**
	 * RegRenClientUtility constructor comment.
	 */
	public RegRenClientUtility()
	{
		super();
	}

	/**
	 * This method puts the vehicle in process so that others can't
	 * process the same vehicle.
	 * 
	 * @return Object
	 * @param aaData InternetRegRecData
	 * @param aaFrame RTSDialogBox
	 */
	public static Object checkOutVeh(
		InternetRegRecData aaData,
		RTSDialogBox aaFrame)
	{
		boolean lbCheckedOut = true;
		Vector lvResult = null;

		//standard validation
		RTSException leValidEx = new RTSException();

		//obtain workstation Id, do not allow anonymous users to take 
		//ownership of records!
		int liWorkStationId = SystemProperty.getWorkStationId();
		int liOrigWorkStationId = 0;
		if (aaData.getEmployeeId() != null
			&& !aaData.getEmployeeId().equals(""))
			liOrigWorkStationId =
				Integer.valueOf(aaData.getEmployeeId()).intValue();

		RegRenProcessingClientBusiness laRegRenClientBus =
			new RegRenProcessingClientBusiness();

		//check if record already checked out by other workstation
		if (aaData.getStatus().equals(CommonConstants.IN_PROCESS + ""))
		{
			if (liWorkStationId != liOrigWorkStationId)
			{
				// 962 
				leValidEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_RECORD_CHECKED_OUT),
					leValidEx.getFirstComponent());
				leValidEx.displayError(aaFrame);
				// defect 7889
				// This was causing a null pointer because 
				// getFirstComponent was return null
				if (leValidEx.getFirstComponent() != null)
				{
					leValidEx.getFirstComponent().requestFocus();
				}
				// end defect 7889
				lbCheckedOut = false;
			}
			else
			{
				// It's a checked out record, no need to check out 
				// again.  Need to get the transaction object.
				try
				{
					Hashtable lhtHashTable = new Hashtable();
					lhtHashTable.put("TransCd", "IRENEW");
					lhtHashTable.put(
						"RegPltNo",
						aaData
							.getCompleteRegRenData()
							.getVehBaseData()
							.getPlateNo());
					lhtHashTable.put(
						"DocNo",
						aaData
							.getCompleteRegRenData()
							.getVehBaseData()
							.getDocNo());
					Object obj =
						laRegRenClientBus.processData(
							GeneralConstant.INTERNET_REG_REN_PROCESSING,
							RegRenProcessingConstants.GET_TX,
							lhtHashTable);
							
					// defect 10598 
					Vector lvReturn = (Vector) obj; 
					lvResult = new Vector();
					lvResult.add("1");
					lvResult.add(lvReturn.elementAt(0));
					lvResult.add(lvReturn.elementAt(1));
					// end defect 10598 
				}
				catch (Exception leEx)
				{
					lbCheckedOut = false;
				}
			}
		}
		else
		{
			//update record status to 'IN_PROCESS'
			try
			{
				Object laObject =
					laRegRenClientBus.processData(
						GeneralConstant.INTERNET_REG_REN_PROCESSING,
						RegRenProcessingConstants.VEH_CHECKOUT,
						aaData);
						
				//evaluate return object
				if (laObject == null)
				{
					// 983 
					leValidEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_NULL_OBJECT),
						leValidEx.getFirstComponent());
					leValidEx.displayError(aaFrame);
					lbCheckedOut = false;
				}
				if (laObject != null
					&& laObject instanceof RTSException)
				{
					// 973 
					leValidEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_ERROR_HAS_OCCURRED),
						leValidEx.getFirstComponent());
					leValidEx.displayError(aaFrame);

					lbCheckedOut = false;
				}
				if (laObject != null && laObject instanceof Vector)
				{
					String lsString =
						(String) ((Vector) laObject).elementAt(0);
					if (lsString.equals("0"))
					{
						// 962 
						leValidEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_RECORD_CHECKED_OUT),
							leValidEx.getFirstComponent());
						leValidEx.displayError(aaFrame);
						lbCheckedOut = false;
					}
					lvResult = (Vector) laObject;
				}
			}
			catch (RTSException aeEx)
			{
				System.out.println(
					"SERVER ERROR: " + aeEx.getMessage());
				// 973 
				leValidEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					leValidEx.getFirstComponent());
				leValidEx.displayError(aaFrame);
				lbCheckedOut = false;
			}
		}
		if (!lbCheckedOut)
		{
			lvResult = new Vector();
			lvResult.add("0");
		}
		return lvResult;
	}

	/**
	 * This method returns true or false depending on if the date passed
	 * is to old or not.
	 * 
	 * @return boolean
	 * @param aaDate java.lang.String
	 * @param aiDays int
	 */
	public static boolean dateTooOld(RTSDate aaDate, int aiDays)
	{
		RTSDate laRTSDateCurrent = new RTSDate();
		RTSDate laAllowedPreDays =
			laRTSDateCurrent.add(RTSDate.DATE, - (aiDays));
		laAllowedPreDays.setHour(0);
		laAllowedPreDays.setMinute(0);
		laAllowedPreDays.setSecond(0);
		laAllowedPreDays.setMillisecond(0);
		laAllowedPreDays.setMicroseconds(0);
		return laAllowedPreDays.compareTo(aaDate) > 0;
	}

	/**
	 * This method returns a valid date.
	 * 
	 * @return String
	 * @param aaDateField String
	 */
	public static String validateDate(RTSDateField aaDateField)
	{
		String lsDate = aaDateField.getText();
		String lsYear = "", lsMonth = "", lsDay = "";
		//ignore invalid strings
		if (lsDate.length() != 10)
		{
			return null;
		}
		if (lsDate != null)
		{
			lsYear = lsDate.substring(6, 10).trim();
			lsMonth = lsDate.substring(0, 2).trim();
			if (lsMonth.length() == 1)
			{
				lsMonth = "0" + lsMonth;
			}
			lsDay = lsDate.substring(3, 5).trim();
			if (lsDay.length() == 1)
			{
				lsDay = "0" + lsDay;
			}
		}
		lsDate = lsMonth + "/" + lsDay + "/" + lsYear;
		SimpleDateFormat laDateFormat =
			new SimpleDateFormat("MM/dd/yyyy");
		try
		{
			laDateFormat.parse(lsDate, new ParsePosition(0));
		}
		catch (Exception leEx)
		{
			return null;
		}
		// the getDate() method check if it is a valid range date in Phase 1.
		if (aaDateField.getDate() == null)
		{
			return null;
		}
		String lsReturnValue = lsYear + lsMonth + lsDay;
		return lsReturnValue;
	}
}
