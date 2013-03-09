package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.data.SecurityLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * VCEmployeeAccessRightsSEC005.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl 	10/24/2003 	Update to allow Active Directory Add to 
 *							occur before the database add occurs.
 *							add methods  adAddUser()
 *							change processData()
 *							defect 6445  ver  5.1.6
 * Ray Rowehl	12/01/2003	rename AdUserId to SysUserId
 *							defect 6445  ver 5.1.6
 * Ray Rowehl	12/30/2003	rename SysUserId to UserName
 *							defect 6445  ver 5.1.6
 * Ray Rowehl	02/03/2004	Update incremented username message
 *							Move AD methods over to the JNI Interface.
 *							modify processData()
 *							defect 6445  ver 5.1.6
 * Ray Rowehl	02/18/2004	when adding, do not try the AD Delete
 *							if the error came from jni.
 *							On errors, try to send the data back
 *							to FrmSec005.
 *							reflow java comments to match updated 
 *							java standards.
 *							modify processData()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/24/2004	If the user does not want to keep an
 *							incremented user on active directory add, 
 *							delete it back out. 
 *							modify processData()
 *							defect 6445 Ver 5.1.6
 * Min Wang		02/23/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang		04/07/2005	correct setVisible
 * 							defect 7891
 * Min Wang		04/16/2004  removed unused method.
 * 							delete handleError()
 * 							defect 7891  Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3 
 * Min Wang		09/07/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3   
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFM_MSG
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3 
 * M Reyes		02/26/2007	Changes for Special Plates
 * 							modify processData()
 * 							defect 9124 Ver Special Plates 							          
 * ---------------------------------------------------------------------
 */

/**
 * Controller for Employee rights SEC005 screen.
 *
 * @version Special Plates			02/26/2007
 * @author	Administrator
 * <br>Creation Date: 		08/13/2001 15:15:51
 */

public class VCEmployeeAccessRightsSEC005
	extends AbstractViewController
{
	public static final int ADD = 5;
	public static final int REVISE = 6;
	public static final int DELETE = 7;
	public static final int SEC05_CANCEL = 8;
	public static final int REGONLY = 9;
	public static final int TITLE_REG = 10;
	public static final int STATCHNG = 11;
	public static final int INQRY = 12;
	public static final int MISCREG = 13;
	public static final int MISC = 14;
	public static final int REPRT = 15;
	public static final int LOCAL = 16;
	public static final int ACCT = 17;
	public static final int INV = 18;
	public static final int FUNDS = 19;
	// defect 9124
	public static final int SPLPLTS = 20;
	// end defect 9124
	private static final String TXT_EXIT_MSG =
		"Do you really want to exit to the Main Menu?";
	// defect 8756
	// Used common constant for CTL001 title
	//private static final String TXT_CONFM_MSG =
	//	"CONFIRM ACTION    CTL001";
	// end defect 8756
	private static final String TXT_NAME_CHANGE =
		"The User Name was changed to be"
			+ " unique, do you want to continue?"
			+ "  The User Name is now ";

	/**
	 * VCEmployeeAccessRightsSEC005 constructor comment.
	 */
	public VCEmployeeAccessRightsSEC005()
	{
		super();
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}

	// defect 7891
	///**
	// * Handles any errors that may occur
	// * 
	// * @deprecated
	// * @param aeRTSEx RTSException
	// */
	//public void handleError(RTSException aeRTSEx)
	//{
	//	 
	//}
	// end defect 7891

	/**
	 * Handles data coming from their JDialogBox - inside the 
	 * subclasses implementation should be calls to fireRTSEvent() to 
	 * pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
				{
					if (getFrame() != null)
					{
						boolean lbCancel = false;
						String lsId =
							(
								(FrmEmployeeAccessRightsSEC005) getFrame())
								.getEmpId();
						if (lsId == null)
						{
							lbCancel = true;
						}
						else
						{
							// defect 8756
							// Used common constant for CTL001 title
							RTSException leRTSEx =
								new RTSException(
									RTSException.CTL001,
									TXT_EXIT_MSG,
									ScreenConstant.CTL001_FRM_TITLE);
							// end defect 8756
							int liRetCode =
								leRTSEx.displayError(getFrame());
							if (liRetCode == RTSException.YES)
							{
								lbCancel = true;
							}
							else
							{
								//((FrmEmployeeAccessRightsSEC005)frame)
								// .reinitializeWindow();
								// defect 6445
								// reinitialize window by returning null
								setDirectionFlow(
									AbstractViewController.CURRENT);
								try
								{
									getMediator().processData(
										getModuleName(),
										LocalOptionConstant
											.NO_DATA_TO_BUSINESS,
										null);
								}
								catch (RTSException aeRTSEx2)
								{
									aeRTSEx2.displayError(getFrame());
								}
								// end defect 6445
							}
						}
						if (lbCancel)
						{
							setDirectionFlow(
								AbstractViewController.FINAL);
							try
							{
								getMediator().processData(
									getModuleName(),
									LocalOptionConstant
										.NO_DATA_TO_BUSINESS,
									aaData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}

							if (getFrame() != null)
							{
								getFrame().setVisibleRTS(false);
							}
						}
					}
					break;
				}
			case SEC05_CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case VCEmployeeAccessRightsSEC005.DELETE :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						SecurityLogData laSecLogData =
							new SecurityLogData((SecurityData) aaData);
						laSecLogData.setTransAMDate(
							RTSDate.getCurrentDate().getAMDate());
						// use the 24hrtime 
						laSecLogData.setTransTime(
							RTSDate.getCurrentDate().get24HrTime());
						laSecLogData.setTransWsId(
							SystemProperty.getWorkStationId());
						laSecLogData.setUpdtActn("D");
						laSecLogData.setUpdtngEmpId(
							SystemProperty.getCurrentEmpId());
						Vector laVector = new Vector();
						laVector.add(aaData);
						laVector.add(laSecLogData);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.DEL_EMP_ACCS_RIGHTS,
							laVector);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						// We now return the object so the user can see it. 
						// SecurityClientDataObject
						setDirectionFlow(
							AbstractViewController.CURRENT);
						try
						{
							SecurityClientDataObject laSecObj =
								new SecurityClientDataObject();
							laSecObj.setSecData((SecurityData) aaData);
							getMediator().processData(
								getModuleName(),
								LocalOptionConstant.NO_DATA_TO_BUSINESS,
								laSecObj);
						}
						catch (RTSException aeRTSEx2)
						{
							aeRTSEx2.displayError(getFrame());
						}
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.ADD :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						// add the user to the directory
						int liRC = JniAdInterface.adAddUser(aaData);
						// if the user id was incremented, show the 
						// screen again
						if (liRC == JniAdInterface.USERNAMEINCREMENTED)
						{
							//Vector lvSec = (Vector) aData;
							SecurityData laSecData =
								(SecurityData) aaData;
							// defect 8756
							// Used common constant for CTL001 title
							RTSException aeRTSEx =
								new RTSException(
									RTSException.CTL001,
									TXT_NAME_CHANGE
										+ laSecData.getUserName(),
									ScreenConstant.CTL001_FRM_TITLE);
							// end defect 8756
							int liRetCode =
								aeRTSEx.displayError(getFrame());
							if (liRetCode == RTSException.NO)
							{
								// remove the user from Active Directory
								// because we do not want to keep it
								// defect 7891
								// remove unused variable
								//int liRC2 =
								JniAdInterface.adDelUser(aaData);
								// end defect 7891
								// If they do not want to continue, 
								// clear the SEC005 screen
								// cause the flow to come back with an 
								// empty object to clear the screen
								setDirectionFlow(
									AbstractViewController.CURRENT);
								try
								{
									getMediator().processData(
										getModuleName(),
										LocalOptionConstant
											.NO_DATA_TO_BUSINESS,
										null);
								}
								catch (RTSException aeRTSEx2)
								{
									aeRTSEx2.displayError(getFrame());
								}
								break;
							}
						}
						else if (
							liRC != JniAdInterface.NORMALRETURNCODE
								&& liRC
									!= JniAdInterface.USERNAMEINCREMENTED)
						{
							throw new RTSException(752);
						}
						SecurityLogData laSecLogData =
							new SecurityLogData((SecurityData) aaData);
						laSecLogData.setTransAMDate(
							RTSDate.getCurrentDate().getAMDate());
						laSecLogData.setTransTime(
							RTSDate.getCurrentDate().get24HrTime());
						laSecLogData.setTransWsId(
							SystemProperty.getWorkStationId());
						laSecLogData.setUpdtActn("I");
						laSecLogData.setUpdtngEmpId(
							SystemProperty.getCurrentEmpId());
						Vector laVector = new Vector();
						laVector.add(aaData);
						laVector.add(laSecLogData);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.ADD_EMP_ACCS_RIGHTS,
							laVector);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						// delete the user from the directory on an add
						//  error
						try
						{
							// only try the delete if the error is from
							//  server
							if (aeRTSEx.getCode() != 751
								&& aeRTSEx.getCode() != 752)
							{
								// defect 7891
								//int liRC =
								JniAdInterface.adDelUser(aaData);
								// end defect 7891 
							}
						}
						catch (RTSException aeRTSEx2)
						{
							aeRTSEx2.displayError(getFrame());
						}
						// reinitialize window by returning a new
						//  SecurityClientDataObject
						setDirectionFlow(
							AbstractViewController.CURRENT);
						try
						{
							SecurityClientDataObject laSecObj =
								new SecurityClientDataObject();
							laSecObj.setSecData((SecurityData) aaData);
							getMediator().processData(
								getModuleName(),
								LocalOptionConstant.NO_DATA_TO_BUSINESS,
								laSecObj);
						}
						catch (RTSException aeRTSEx2)
						{
							aeRTSEx2.displayError(getFrame());
						}
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.SEARCH :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_EMP_DATA_ONID,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.FUNDS :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC016);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.INQRY :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC009);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.INV :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC015);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.LOCAL :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC013);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant
								.ADD_CURRENT_EMP_DATA_ONID,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.MISC :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC011);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.MISCREG :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC010);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.REGONLY :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC006);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.REPRT :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC012);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.REVISE :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						SecurityLogData laSecLogData =
							new SecurityLogData((SecurityData) aaData);
						laSecLogData.setTransAMDate(
							RTSDate.getCurrentDate().getAMDate());
						// use 24hrtime
						laSecLogData.setTransTime(
							RTSDate.getCurrentDate().get24HrTime());
						laSecLogData.setTransWsId(
							SystemProperty.getWorkStationId());
						laSecLogData.setUpdtActn("U");
						laSecLogData.setUpdtngEmpId(
							SystemProperty.getCurrentEmpId());
						Vector laVector = new Vector();
						laVector.add(aaData);
						laVector.add(laSecLogData);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.REVISE_EMP_ACCS_RIGHTS,
							laVector);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						// reinitialize window by returning a new 
						//  SecurityClientDataObject
						setDirectionFlow(
							AbstractViewController.CURRENT);
						try
						{
							SecurityClientDataObject laSecObj =
								new SecurityClientDataObject();
							laSecObj.setSecData((SecurityData) aaData);
							getMediator().processData(
								getModuleName(),
								LocalOptionConstant.NO_DATA_TO_BUSINESS,
								laSecObj);
						}
						catch (RTSException aeRTSEx2)
						{
							aeRTSEx2.displayError(getFrame());
						}
					}
					break;
				}
				// defect 9124
			case VCEmployeeAccessRightsSEC005.SPLPLTS :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC018);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 9124
			case VCEmployeeAccessRightsSEC005.STATCHNG :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC008);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.TITLE_REG :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC007);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCEmployeeAccessRightsSEC005.ACCT :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.SEC014);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
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
	 * Creates the actual frame, stores the protected variables needed
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers Vector  
	 * @param asTransCode String  
	 * @param aadata Object 
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aadata)
	{
		if (getFrame() == null)
		{
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmEmployeeAccessRightsSEC005(laDialog));
			}
			else
			{
				setFrame(
					new FrmEmployeeAccessRightsSEC005(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aadata);
	}
}
