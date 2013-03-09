package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
/*
 *
 * VCProcessVehicleREG103.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/23/2005	Get code up to standards.
 * 							Changed setvisible to setVisibleRTS.
 * 							modify processData()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/20/2005	Made change to re-search when going back
 * 							to the search results screen and we are not
 * 							in a view only mode.
 * 							modify processData()
 * 							defect 8247 Ver 5.2.3
 * Jeff S.		07/12/2005	Renamed class to have the frame number.
 * 							Added String Constants.
 * 							defect 7889 Ver 5.2.3
 * Jeff S.		08/16/2005	Used new screen constants that match the 
 * 							frame number.
 * 							modify processData()
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	09/23/2010	add INPROCS_TRANS
 * 							modify processData() 
 * 							defect 10598 Ver 6.6.0
 * K Harrell	10/27/2011	add PREVIEWRECEIPT
 * 							modify processData() 
 * 							defect 11127 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * VC for FrmProcessVehicleREG103.
 *
 * @version	6.9.0 		10/27/2011
 * @author	GDONOSO
 * @since 				11/27/2001 19:30:09
 */
public class VCProcessVehicleREG103 extends AbstractViewController
{
	// defect 10598 
	public static final int INPROCS_TRANS = 27;
	// end defect 10598 
	
	// defect 11127 
	public static final int PREVIEWRECEIPT = 24;
	// end defect 11127 
	
	/**
	 * String Constants
	 */
	private static final String VIEWONLY = "ViewOnly";
	
	/**
	 * VCProcessVehicleREG103 constructor comment.
	 */
	public VCProcessVehicleREG103()
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
		return GeneralConstant.INTERNET_REG_REN_PROCESSING;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int - Command so the Frame can communicate 
	 * 							with the VC
	 * @param data Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case CANCEL :
					{
						getFrame().setVisibleRTS(false);

						try
						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
							setPreviousController(
								ScreenConstant.REG102);
							// defect 8247
							// This is the check to verify that if we
							// are in a search or we are just processing
							// the queue. If previos controllers is empty
							// then we are processing a queue.
							if (getPreviousControllers() != null
								&& getPreviousControllers().isEmpty())
							{
								getMediator().processData(
									getModuleName(),
									RegRenProcessingConstants.NO_DATA,
									aaData);
							}
							// We are in a search and want to re-search
							// only if we are not in approved status.
							else
							{
								// If one of our search parameters make
								// all of the records we are searching
								// for "view only" then then there
								// is no reason to re-search b/c the 
								// results can't change.
								if (((Boolean) ((Hashtable) aaData)
									.get(VIEWONLY))
									.booleanValue())
								{
									getMediator().processData(
										getModuleName(),
										RegRenProcessingConstants
											.NO_DATA,
										aaData);
								}
								else
								{
									// setting direction flow to previous
									// because we want to go back through
									// setdata b/c we have re-searched.
									setDirectionFlow(
										AbstractViewController
											.PREVIOUS);
									getMediator().processData(
										getModuleName(),
										RegRenProcessingConstants
											.GET_PROC_VEHICLE,
										aaData);
								}
							}
							// end defect 8247
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}
					// defect 11127
				case PREVIEWRECEIPT :
					{
						setNextController(ScreenConstant.RPR000);
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
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
				// end defect 11127 
				case ENTER :
					{
						setData(aaData);
						setNextController(ScreenConstant.INV007);
						setDirectionFlow(AbstractViewController.NEXT);

						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								aaData);
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}

				case RegRenProcessingConstants.VIEW_INS :
					{
						setData(aaData);
						setNextController(ScreenConstant.REG104);
						setDirectionFlow(AbstractViewController.NEXT);

						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								getData());
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}

				case RegRenProcessingConstants.VIEW_ADDR :
					{
						setData(aaData);
						setNextController(ScreenConstant.REG105);
						setDirectionFlow(AbstractViewController.NEXT);

						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								getData());
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}

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
					// end defect 10598 
			}
		}
		catch (Exception leRTSEx)
		{
		}
	}
	
	/**
	 * Retrieves apporpriate data if necessary, stores variables, 
	 * displays frame, and passes control.
	 *
	 * @param avPreviousControllers vector containing the names 
	 * 								of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		//TODO fix problems in setView
		if (getFrame() == null)
		{
			if (getMediator().getParent() != null)
			{
				setFrame(
					new FrmProcessVehicleREG103(
						getMediator().getParent()));
			}
			else
			{
				setFrame(
					new FrmProcessVehicleREG103(
						getMediator().getDesktop()));
			}
		}

		setData(aaData);
		this.setPreviousControllers(avPreviousControllers);
		setTransCode(asTransCode);

		getFrame().setController(this);

		if (aaData instanceof RTSException)
		{
			// comes back from phase 1 with error,
			// i.e., transaction approval does not go through,
			// show the error.
			 ((RTSException) aaData).displayError(getFrame());
			return;
		}
		getFrame().setData(aaData);
	}
}
