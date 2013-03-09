package com.txdot.isd.rts.client.common.ui;import java.util.Vector;import com.txdot.isd.rts.client.general.ui.AbstractViewController;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.GeneralConstant;/* * * VCDocumentNoConfirmCTL006.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup * 							organize imports, format source, * 							rename fields * 							defect 7885 Ver 5.2.3 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog. * 							defect 7885 Ver 5.2.3 * --------------------------------------------------------------------- *//** * VC for VCDocumentNoConfirmCTL006. *  * @version	5.2.3		04/26/2005 * @author	Joseph Peters * <br>Creation Date:	12/16/2001 12:15:28 *//* &VCDocumentNoConfirmCTL006& */public class VCDocumentNoConfirmCTL006	extends AbstractViewController{	/**	 * VCDocumentNoConfirmCTL006 constructor comment.	 *//* &VCDocumentNoConfirmCTL006.VCDocumentNoConfirmCTL006& */	public VCDocumentNoConfirmCTL006()	{		super();	}	/**	 * All subclasses must override this method to return their own 	 * module name.	 * 	 * @return int	 *//* &VCDocumentNoConfirmCTL006.getModuleName& */	public int getModuleName()	{		return GeneralConstant.COMMON;	}	/**	 * All subclasses must override this method to handle data coming 	 * from their JDialogBox - inside the subclasses implementation	 * should be calls to fireRTSEvent() to pass the data to the 	 * RTSMediator.	 * 	 * @param aiCommand int	 * @param aaData Object 	 *//* &VCDocumentNoConfirmCTL006.processData& */	public void processData(int aiCommand, Object aaData)	{		switch (aiCommand)		{			case AbstractViewController.ENTER :			{				setDirectionFlow(AbstractViewController.PREVIOUS);				try				{					getMediator().processData(						getModuleName(),						CommonConstant.NO_DATA_TO_BUSINESS,						aaData);				}				catch (RTSException leRTSEx)				{					leRTSEx.displayError(getFrame());				}				getFrame().setVisibleRTS(false);				break;			}			case AbstractViewController.CANCEL :			{				setDirectionFlow(AbstractViewController.CANCEL);				try				{					getMediator().processData(						getModuleName(),						CommonConstant.NO_DATA_TO_BUSINESS,						null);				}				catch (RTSException leRTSEx)				{					leRTSEx.displayError(getFrame());				}				getFrame().setVisibleRTS(false);				break;			}		}	}	/**	 * Send the data to the frame.	 * 	 * @param avPreviousControllers Vector	 * @param asTransCode String	 * @param aaData Object	 *//* &VCDocumentNoConfirmCTL006.setView& */	public void setView(		Vector avPreviousControllers,		String asTransCode,		Object aaData)	{		if (getFrame() == null)		{			setFrame(new FrmDocumentNoConfirmCTL006(				getMediator().getDesktop()));		}		super.setView(avPreviousControllers, asTransCode, aaData);	}}/* #VCDocumentNoConfirmCTL006# */