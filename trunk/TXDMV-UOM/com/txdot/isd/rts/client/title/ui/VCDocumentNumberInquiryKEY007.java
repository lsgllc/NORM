package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.TitleInProcessData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * VCDocumentNumberInquiryKEY007.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/24/2005	Change key007 to KEY007 in class name
 * 							Code Cleanup
 * 							modify class name, super, setView()
 * 							defect 6967 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * K Harrell	10/04/2010	add INPROC_TRANS, TTL042 
 * 							modify processData(), setView() 
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * View controller for the InquiryKey007 screen 
 *
 * @version	6.6.0 			10/04/2010
 * @author	Administrator
 * <br>Creation Date:		8/22/01 11:12:42
 */

public class VCDocumentNumberInquiryKEY007
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	//	defect 10598 
	public static final int INPROCS_TRANS = 27;
	public final static int TTL042 = 42;
	// end defect 10598 
	

	/**
	 * VCDocumentNumberInquiryKEY007 constructor comment.
	 */
	public VCDocumentNumberInquiryKEY007()
	{
		super();
	}
	/**
	 * Displays the error message
	 * 
	 * @param aiCode int
	 */
	private void displayError(int aiCode)
	{
		RTSException laMsg = new RTSException(aiCode);
		laMsg.displayError(getFrame());

	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand String the command so the Frame can communicate 
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		setTransCode(TransCdConstant.DELTIP);

		switch (aiCommand)
		{
			// defect 10598
			case INPROCS_TRANS :
				{
					setNextController(ScreenConstant.INQ002);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case TTL042 :
				{
					setNextController(ScreenConstant.TTL042);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN))
						{
							displayError(1);
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					// end defect 10598
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.DELETE_TITLE_IN_PROCESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN))
						{
							displayError(1);
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}
	/**
	 * Creates the form and sets the data objects and controllers
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData      Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		// defect 10598 
		if (aaData != null && aaData instanceof TitleInProcessData)
		{
			TitleInProcessData laTIPData = (TitleInProcessData) aaData;

			if (laTIPData.hasInProcsTrans())
			{
				processData(INPROCS_TRANS, laTIPData);
			}
			else if (laTIPData.getDocNo() == null)
			{
				displayError(182);
			}
			else
			{
				processData(TTL042, aaData);
			}
		}
		else
		// end defect 10598 
		 if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
				
			if (laRTSDBox != null)
			{
				setFrame(new FrmDocumentNumberInquiryKEY007(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmDocumentNumberInquiryKEY007(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
