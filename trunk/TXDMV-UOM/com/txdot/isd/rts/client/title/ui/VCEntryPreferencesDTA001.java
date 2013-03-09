package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * VCEntryPreferencesDTA001.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * 
 * ---------------------------------------------------------------------
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008b to DTA009
 * 							modify processData()
 * 							defect 6963 Ver5.2.3 
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		06/28/2005	Check for "file empty" message in the RTSExc
 *  						Display message DTA005
 * 							modify processData()
 * 							defect 8220 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/14/2005	Add SYSTEM_ERROR as possible error in 
 * 							reading diskette.
 * 							modify processData()
 * 							defect 8220 Ver 5.2.3
 * Ray Rowehl	08/11/2006	Add comment about Enter processing
 * J Rue		01/15/2009	Add comments for Exception display
 * 							modify processData()
 * 							defect 8912 Ver Defect_POS_D
 * B Hargrove	06/03/2009	Add Flashdrive option to DTA.
 * 							refactor rename DEALER_SUPP_DISK to
 * 							DEALER_SUPP_MEDIA
 * 			                modify processData() 
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	06/26/2009	delete handleError()
 * 							modify processData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	11/30/2009	Cleanup 
 * 							modify processData() 
 * 							defect 10290 Ver Defect_POS_H	
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Dealer Diskette Contents DTA007
 * 
 * @version Defect_POS_H	11/30/2009
 * @author	Ashish Mahajan
 * <br>Creation Date: 		08/22/2001 11:13:40
 */

public class VCEntryPreferencesDTA001
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int DEALER_SUPP_MEDIA = 5;
	public static final int KEYBOARD = 6;
	private final static String FILE_NOT_FOUND = "file not found";
	private final static String FILE_EMPTY = "file empty";
	private final static String SYSTEM_ERROR =
		"A System Error has occurred.";

	/**
	 * VCEntryPreferencesDTA001 constructor.
	 */
	public VCEntryPreferencesDTA001()
	{
		super();
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}

	/**
	 * Controls the screen flow from DTA001.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * 	to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				// defect 10290 
				// removed ENTER case 
				// Implement close
				
				case DEALER_SUPP_MEDIA :
					{
						try
						{
							close();
							setNextController(ScreenConstant.DTA007);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								TitleConstant.DATA_FROM_DISK,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							// defect 8220, 8912 
							// Display RTSException if message    
							//   Does not end with: 
							//     - "file not found"
							//     - "file empty" 
							//	  AND does not start with:
							//     - "A System Error has occurred" 
							//
							if (!aeRTSEx
								.getMessage()
								.endsWith(FILE_NOT_FOUND)
								&& !aeRTSEx.getMessage().endsWith(
									FILE_EMPTY)
								&& !aeRTSEx.getMessage().startsWith(
									SYSTEM_ERROR))
							{
								aeRTSEx.displayError(getFrame());
							}
							// end defect 8220, 8912 

							setNextController(ScreenConstant.DTA005);
							setDirectionFlow(
								AbstractViewController.NEXT);
							try
							{
								getMediator().processData(
									getModuleName(),
									TitleConstant.NO_DATA_TO_BUSINESS,
									aaData);
							}
							catch (RTSException aeRTSExec)
							{
								aeRTSExec.displayError(getFrame());
							}
						}
						break;
					}
				case KEYBOARD :
					{
						close();
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.DTA009);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
				case CANCEL :
					{
						close();
						setDirectionFlow(AbstractViewController.FINAL);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
				// end defect 10290 
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.displayError(getFrame());

		}
	}

	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers Vector A vector containing 
	 * 			the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();

			if (laRTSDBox != null)
			{
				setFrame(new FrmEntryPreferencesDTA001(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmEntryPreferencesDTA001(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
