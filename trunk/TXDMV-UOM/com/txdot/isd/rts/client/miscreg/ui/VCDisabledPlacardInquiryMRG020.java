package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCDisabledPlacardInquiryMRG020.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/01/2008	Modify to handle Server/DB Exceptions
 * 							add handle_DB_Server_Down 
 * 							modify processData()  
 * 							defect 9831 Ver POS_Defect_B  
 * K Harrell	11/09/2008	Modify to handle Max Returned Rows 
 * 							modify setView()
 * 							defect 9831 Ver POS_Defect_B 
 * ---------------------------------------------------------------------
 */

/**
 * View controller for FrmDisabledPlacardInquiryMRG020 
 *
 * @version	Defect_POS_B	11/09/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class VCDisabledPlacardInquiryMRG020
	extends AbstractViewController
{
	public final static int SEARCH = 20;
	public final static int MRG021 = 21;
	public final static int MRG022 = 22;

	/**
	* VCDisabledPlacardInquiryMRG020 default constructor.
	*/
	public VCDisabledPlacardInquiryMRG020()
	{
		super();
	}

	/**
	 * Get Module Name MISCELLANEOUSREG
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISCELLANEOUSREG;
	}

	/**
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 */
	public void handle_DB_Server_Down(RTSException aeRTSEx)
	{
		if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
			|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
		{
			aeRTSEx.setCode(618);
			aeRTSEx.displayError(getFrame());
		}
		else
		{
			aeRTSEx.displayError(getFrame());
		}
		processData(AbstractViewController.CANCEL, null);
	}

	/**
	 * Process Data
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{

					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								MiscellaneousRegConstant.SEARCH,
								aaData);

					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;
				}
			case MRG021 :
				{
					setNextController(ScreenConstant.MRG021);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;

				}
			case MRG022 :
				{
					setNextController(ScreenConstant.MRG022);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;
				}

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
		}
	}

	/**
	 * This method is called by RTSMediator to display the frame.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(
					new FrmDisabledPlacardInquiryMRG020(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmDisabledPlacardInquiryMRG020(
						getMediator().getDesktop()));

			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else if (aaData instanceof Vector)
		{
			Vector lvVector = (Vector) aaData; 
			
			// Vector lvData contains result set  
			Vector lvData = (Vector) lvVector.elementAt(1); 
			
			if (lvData.size() == 0)
			{
				new RTSException(57).displayError(getFrame());
			}
			else if (lvData.size() == 1)
			{
				DisabledPlacardCustomerData laData =
					(DisabledPlacardCustomerData) lvData.firstElement();

				if (laData.isNoRecordFound()
					&& asTransCode.equals(MiscellaneousRegConstant.INQ))
				{
					new RTSException(57).displayError(getFrame());
				}
				else
				{
					processData(MRG022, laData);
				}
			}
			else
			{
				processData(MRG021, aaData);
			}
		}
	}
}
