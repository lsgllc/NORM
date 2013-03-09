package com.txdot.isd.rts.client.accounting.ui;

import javax.swing.JOptionPane;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * AccountingHelpDeskAssistant.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/03/2004	created with public method promptHelpDesk()
 * 							defect 6913 Ver 5.2.2
 * K Harrell	12/10/2004	altered to use isNumeric()
 *							defect 6913 Ver 5.2.2
 * K Harrell	07/21/2005	Java 1.4 Work 
 * 							defect 7884 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * A container class for method(s) used by the Help Desk in Accounting 
 * support.  
 *
 * @version	5.2.3		07/21/2005   
 * @author	Kathy Harrell
 * <br>Creation Date:	12/03/2004 16:52:30
 */
public class AccountingHelpDeskAssistant
{
	// Constants 

	private static final String COUNTY_NO = "County No: ";
	private static final String COUNTY_NO_NOT_CHANGED =
		"County No will not be changed.";
	private static final String HELPDESK_ASSISTANT =
		"Help Desk Assistant";
	private static final String HELPDESK_OFCID_SYSPROP = "HelpDeskOfcId"; 
	private static final String INVALID_COUNTY_NO =
		" - Invalid County No";

	/**
	 * AccountingHelpDeskAssistant constructor comment.
	 */
	public AccountingHelpDeskAssistant()
	{
		super();
	}
	/**
	 * Use JOptionPane to prompt Help Desk for alternate County No
	 * 
	 * @return boolean  
	 */
	public static boolean promptHelpDesk()
	{
		boolean lbHelpDeskDisplayed = false;
		boolean lbOfcNoChanged = false;
		String lsError = "";
		while (!lbHelpDeskDisplayed)
		{
			String lsOfcIssuanceNo =
				JOptionPane.showInputDialog(
					null,
					COUNTY_NO,
					HELPDESK_ASSISTANT + lsError,
					JOptionPane.QUESTION_MESSAGE);
			if (lsOfcIssuanceNo != null && !lsOfcIssuanceNo.equals(""))
			{
				if (lsOfcIssuanceNo.length() <= 3
					&& UtilityMethods.isNumeric(lsOfcIssuanceNo))
				{
					int liOfcIssuanceNo =
						Integer.parseInt(lsOfcIssuanceNo);
					OfficeIdsData laOfficeIdsData =
						(OfficeIdsData) OfficeIdsCache.getOfcId(
							liOfcIssuanceNo);
					if (laOfficeIdsData != null)
					{
						System.setProperty(
							HELPDESK_OFCID_SYSPROP,
							lsOfcIssuanceNo);
						lbHelpDeskDisplayed = true;
						lbOfcNoChanged = true;
					}
					else
					{
						lsError = INVALID_COUNTY_NO;
					}
				}
				else
				{
					lsError = INVALID_COUNTY_NO;
				}
			}
			else
			{
				lsError = "";
				int liResponse =
					JOptionPane.showConfirmDialog(
						null,
						COUNTY_NO_NOT_CHANGED);
				if (liResponse == JOptionPane.YES_OPTION)
				{
					lbHelpDeskDisplayed = true;
				}
			}
		}
		return (lbOfcNoChanged);
	}
}
