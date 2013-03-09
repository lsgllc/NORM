package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCCCOTTL018.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/08/2005	VAJ to WASD Clean Up
 *							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	06/13/2005	Modify Cancel action to return a null.
 * 							this will be used to signal KEY001 that 
 * 							we canceled off the screen and that we
 * 							should restart.
 * 							Remove cache reference in getOfcIssuanceCd()
 * 							The reference was not used.
 * 							Remove handleError().  It is not used.
 * 							delete handleError()
 * 							modify getOfcIssuanceCd(), processData()
 * 							defect 6691 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                  
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbiage.
 * 							defect 7898 Ver 5.2.3   
 * K Harrell	04/22/2007	use SystemProperty.isRegion()
 * 							delete getOfcIssuanceCd()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	03/11/2009	Comment Cleanup. 
 * 							defect 9969 Ver Defect_POS_E  
 * K Harrell	05/29/2011	add FRAUDCD_MGMT
 * 							modify processData()
 * 							defect 10865 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * View controller for CCO018 screen
 *
 * @version	6.8.0		 	05/29/2011
 * @author	Marx Rajangam
 * <br>Creation Date:		08/22/2001 10:32:39
 */

public class VCCCOTTL018 extends AbstractViewController
{
	/**
	 * Required for VTR Authorisation on hard stops
	 */
	public final static int VTR_AUTHORIZATION = 23;

	/**
	 * VCCCOTTL018 constructor comment.
	 */
	public VCCCOTTL018()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return String
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
	 * @param aiCommand int the command so the Frame can communicate 
	 * 					with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10865 
			case TitleConstant.FRAUDCD_MGMT :
				{
					setData(aaData);
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.FRAUDCD_MGMT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			// end defect 10865 
			case ENTER :
				{
					setNextController(ScreenConstant.TTL019);
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
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					if (SystemProperty.isRegion())
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					else
					{
						// Return to Inquiry Screen for HQ 
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
			case VTR_AUTHORIZATION :
				{
					try
					{
						setNextController(ScreenConstant.CTL003);
						setDirectionFlow(AbstractViewController.NEXT);
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
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmCCOTTL018(laRTSDBox));
			}
			else
			{
				setFrame(new FrmCCOTTL018(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
