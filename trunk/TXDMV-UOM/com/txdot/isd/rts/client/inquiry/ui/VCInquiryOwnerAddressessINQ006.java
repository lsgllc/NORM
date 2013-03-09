package com.txdot.isd.rts.client.inquiry.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * VCInquiryOwnerAddressessINQ006.java
 * 
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name        	Date        Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	03/24/2005	Rename VCMultipleRecordsINQ006 to 
 * 							VCInquiryOwnerAddressessINQ006
 * 							to accurately reflect the name of the screen
 * 							that is displayed.
 * 							defect 7952 Ver 5.2.3
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7887 Ver 5.2.3 
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 * 							defect 7887 Ver 5.2.3
 * K Harrell	01/24/2006	Modification to support "ESC" processing
 * 							modify processData()
 * 							defect 7887 Ver 5.2.3 
 * J Rue		04/06/2007	Add code to enter.
 * 							Note: No data is returned, set to Cancel
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * ---------------------------------------------------------------------
 */
/** 
 * Controller for screen INQ006. Displays Dealer information
 *
 * @version Special Plates	04/06/2007
 * @author  Ashish Mahajan
 * <br>Creation Date:		09/05/2001 13:30:59
 */
public class VCInquiryOwnerAddressessINQ006
	extends AbstractViewController
{
	/**
	 * VCInquiryOwnerAddressessINQ006 constructor
	 */
	public VCInquiryOwnerAddressessINQ006()
	{
		super();
	}
	/**
	* Set the view for current controller. Instantiate the frame
	* object for this controller and set it to the frame variable.
	* 
	* @param aaPreviousControllers Vector
	* @param asTransCode String
	* @param aaData Object
	*/
	public void setView(Vector aaPreviousControllers,
		String asTransCode, Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox rd = getMediator().getParent();
			if (rd != null)
			{
				setFrame(new FrmInquiryOwnerAddressessINQ006(rd));
			}
			else
			{
				setFrame(new FrmInquiryOwnerAddressessINQ006(
					getMediator().getDesktop()));
			}
		}
		super.setView(aaPreviousControllers, asTransCode, aaData);
	}
	/**
	 * Return "INQUIRY" as the module name
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.INQUIRY;
	}
	/**
	 * Process data for the screen. Called by the screen.
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 * 		with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			// defect 7887
			// change setVisible to setVisibleRTS
			getFrame().setVisibleRTS(false);
			// end defect 7887
			switch (aiCommand)
			{
				case ENTER:
				{	// No data is return on Enter.
					// Set directional flow to Cancel
					setDirectionFlow(AbstractViewController.CANCEL);
					getMediator().processData(getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
					break;
				}
				case CANCEL: 
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					getMediator().processData(getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
					break;
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}