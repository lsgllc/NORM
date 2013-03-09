package com.txdot.isd.rts.client.inquiry.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCCustomerNameINQ008.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name        	Date        Description
 * S Johnston	03/28/2005	Remove duplicate FrmCustomerName screen
 * 							This class was deprecated because there are
 * 							no references to it in the project
 * 							defect 7953 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 * 							defect 7887 Ver 5.2.3
 * K Harrell	08/04/2009	deprecated. Inquiry uses INQ007 (Accounting) 
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/** 
 * The View Controller for the INQ008 screen.  It handles screen
 * navigation and controls the visibility of its frame.
 *
 * @version Defect_POS_F	08/04/2009		
 * @author  Michael Abernethy
 * <br>Creation date:		12/17/2001 16:29:39
 */
public class VCCustomerNameINQ008 extends AbstractViewController
{
	/**
	 * VCCustomerNameINQ008 constructor
	 */
	public VCCustomerNameINQ008()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.INQUIRY;
	}
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 * 		with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER:
			{
				setData(aaData);
				setNextController(ScreenConstant.PMT004);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					getMediator().processData(
						getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				break;
			}
			case CANCEL:
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getMediator().processData(
						getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				// defect 7887
				// change setVisible to setVisibleRTS
				getFrame().setVisibleRTS(false);
				// end defect 7887
				break;
			}
			case HELP:
			{
				// empty code block
			}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed
	 * by the VC, and sends the data to the frame.
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
			RTSDialogBox laRDBox = getMediator().getParent();
			if (laRDBox != null)
			{
				setFrame(new FrmCustomerNameINQ008(laRDBox));
			}
			else
			{
				setFrame(new FrmCustomerNameINQ008(
					getMediator().getDesktop()));
			}
		}
		super.setView(aaPreviousControllers, asTransCode, aaData);
	}
}