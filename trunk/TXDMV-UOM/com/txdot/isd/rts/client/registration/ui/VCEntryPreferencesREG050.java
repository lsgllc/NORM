package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
*
* VCEntryPreferencesREG050.java
*
* (c) Texas Department of Transportation 2001
* ---------------------------------------------------------------------
* Change History:
* Name			Date		Description
* ------------	-----------	--------------------------------------------
* K Harrell		01/26/2004	5.2.0 Merge. See PCR 34 comments. New Class.
* 							Ver 5.2.0	  
* B Hargrove	06/23/2005	Modify code for move to Java 1.4.
*							Bring code to standards. Use new getters\
*							setters for frame, controller, etc.
*  	 						Remove unused variables, arguments. .
*  							defect 7894 Ver 5.2.3 
* B Hargrove	08/11/2006	Focus lost issue. 
* 							Use close() so that it does setVisibleRTS().
*							modify processData()
* 							defect 8884 Ver 5.2.4
* B Hargrove	06/01/2009	Add Flashdrive option to RSPS Subcon.
* 							add SUBCON_FLASHDRIVE
* 			                modify directCall(),  processData() 
* 							defect 10064 Ver Defect_POS_F  
* ---------------------------------------------------------------------
*/
/**
 * View Controller for Screen Entry Preferences REG050
 * 
 * @version	Defect_POS_F		06/01/2009
 * @author	Nancy Ting 
 * <br>Creation Date:			08/22/2001
 */

public class VCEntryPreferencesREG050
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int SUBCON_SUPP_DISK = 5;
	public static final int KEYBOARD = 6;
	public static final int TO_REG007 = 7;
	// defect 10064
   public static final int SUBCON_FLASHDRIVE = 8;
	// end defect 10064	
	// defect 7894
	// delete unused variable
	//private static final String STRMSG =
	//	"The Subcontractor Renewal records file has been" +		
	//	" successfully copied to this workstation." +		
	//	" <br><br>Press \"Enter\" to continue.";
	// end defect 7894
		
	/**
	 * VCEntryPreferencesREG050 constructor.
	 */
	public VCEntryPreferencesREG050()
	{
		super();
	}
	
	/**
	 * Direct call to business methods
	 * 
	 * @return java.lang.Object
	 * @param aiCommand int
	 * @exception RTSException The exception description.
	 */
	public Object directCall(int aiCommand)
		throws com.txdot.isd.rts.services.exception.RTSException
	{
		switch (aiCommand)
		{
			case SUBCON_SUPP_DISK :
				{
					setDirectionFlow(DIRECT_CALL);
					return getMediator().processData(
						getModuleName(),
						RegistrationConstant
						// defect 10064
						//	.COPY_AND_VALIDATE_SUBCON_DISKETTE,
							.COPY_AND_VALIDATE_SUBCON_MEDIA,
						// end defect 10064
						null);
				}
		}
		return null;
	}
	
	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Handles any errors that may occur
	 */
	// defect 7894
	// remove unused argument
	//public void handleError(
	//	com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	public void handleError()
	// end defect 7894
	{
		//empty code block
	}
	
	/**
	 * Controls the screen flow from DTA001.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
				case SUBCON_SUPP_DISK :
					{
						try
						{
							Object laObject = directCall(
								SUBCON_SUPP_DISK);
							setNextController(ScreenConstant.REG006);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								laObject);
							if (getFrame() != null)
							{
								// defect 8884
								// use close() so that it does setVisibleRTS()
								close();
								//getFrame().setVisible(false);
								// end 8884
							}
						}
						catch (RTSException aeRTSEx)
						{
							if (aeRTSEx.getCode() != 0)
							{
								aeRTSEx.displayError(getFrame());
								setDirectionFlow(
									AbstractViewController.FINAL);
							}
							else
							{
								if (aeRTSEx
									.getMessage()
									.indexOf("file not found")
									== -1)
								{
									aeRTSEx.displayError(getFrame());
								}
								setNextController(
									ScreenConstant.REG053);
								setDirectionFlow(
									AbstractViewController.NEXT);
							}
							if (getFrame() != null)
							{
								// defect 8884
								// use close() so that it does setVisibleRTS()
								close();
								//getFrame().setVisible(false);
								// end 8884
							}
							try
							{
								getMediator().processData(
									getModuleName(),
									RegistrationConstant
										.NO_DATA_TO_BUSINESS,
									null);
							}
							catch (RTSException aeEx)
							{
								aeEx.displayError(getFrame());
							}
						}
						break;
					}
				case KEYBOARD :
					{
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.REG006);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							null);
						break;
					}
			 	 // defect 10064
			 	 case SUBCON_FLASHDRIVE :
				    {
				       try
		   			   {
			   		   		Object laObject = directCall(
				   				SUBCON_FLASHDRIVE);
			   				setNextController(ScreenConstant.REG006);
			   				setDirectionFlow(
				   				AbstractViewController.NEXT);
			   					getMediator().processData(
				   					getModuleName(),
				   					RegistrationConstant
					   				.NO_DATA_TO_BUSINESS,
				   					laObject);
			   				if (getFrame() != null)
			   				{
				   				close();
			   				}
		   				}
		   				catch (RTSException aeRTSEx)
		   				{
			   				if (aeRTSEx.getCode() != 0)
			   				{
				   				aeRTSEx.displayError(getFrame());
				   				setDirectionFlow(
					   				AbstractViewController.FINAL);
			   				}
			   				else
			   				{
				   				if (aeRTSEx
					   				.getMessage()
					   				.indexOf("file not found")
					   				== -1)
				   				{
					   				aeRTSEx.displayError(getFrame());
				   				}
				   				setNextController(
					   				ScreenConstant.REG053);
				   				setDirectionFlow(
					   				AbstractViewController.NEXT);
			   				}
			   				if (getFrame() != null)
			   				{
				   				close();
			   				}
			   				try
			   				{
				   				getMediator().processData(
					   				getModuleName(),
					   				RegistrationConstant
						   			.NO_DATA_TO_BUSINESS,
					   				null);
			   				}
			   				catch (RTSException aeEx)
			   				{
				   				aeEx.displayError(getFrame());
			   				}
		   				}
		   				break;
	   				}
				// end defect 10064					
				case TO_REG007 :
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.REG007);
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.RESTORE_SUBCON_BUNDLE,
							null);
						break;
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leEx.displayError(getFrame());
		}
	}
	
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing
	 *  the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (SubconBundleManager.bundleExists())
		{
			processData(TO_REG007, null);
		}
		else
		{
			if (getFrame() == null)
			{
				com.txdot.isd.rts.client.general.ui.RTSDialogBox 
					laRTSDialogBox = getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					setFrame(new FrmEntryPreferencesREG050(
						laRTSDialogBox));
				}
				else
				{
					setFrame(new FrmEntryPreferencesREG050(
						getMediator().getDesktop()));
				}
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}
