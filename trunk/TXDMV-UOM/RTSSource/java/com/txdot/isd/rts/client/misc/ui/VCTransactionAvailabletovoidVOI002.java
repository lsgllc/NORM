package com.txdot.isd.rts.client.misc.ui;import com.txdot.isd.rts.client.general.ui.AbstractViewController;import com.txdot.isd.rts.client.general.ui.RTSDialogBox;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.constants.GeneralConstant;import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;/* * * VCTransactionAvailabletovoidVOI002.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Tulsiani   	04/25/2002	Added wait cursor to processData * 							defect 3656 * MAbs			05/22/2002	Made wait cursor go away when an exception  *							occurred  *							defect 4088 * Min Wang		05/12/2004	Comment out frame.setCursor(). *							The cursor will be set on the active  *							gui component (RTSMediator.processData()). *							modify processData() *							defect 7053 Ver 5.1.6 Fix 1 * J Zwiener	03/01/2005	Java 1.4 *							defect 7892 Ver 5.2.3 * J Zwiener	03/21/2005	Use new getters\setters for frame,  *							controller, etc. *							defect 7892 Ver 5.2.3 * B Hargrove	08/11/2006	Focus lost issue.  * 							Use close() so that it does setVisibleRTS(). *							modify processData() * 							defect 8884 Ver 5.2.4 * --------------------------------------------------------------------- *//** * VCCash Drawer Selection Screen VOI002 * View conrtoller handles the control operation of screens. *  * @version	5.2.4 			08/11/2006 * @author	Bobby Tulsiani * <br>Creation Date:		09/05/2001 13:30:59 *//* &VCTransactionAvailabletovoidVOI002& */public class VCTransactionAvailabletovoidVOI002	extends com.txdot.isd.rts.client.general.ui.AbstractViewController{	/**	 * VCTransactionAvailabletovoidVOI002 constructor comment.	 *//* &VCTransactionAvailabletovoidVOI002.VCTransactionAvailabletovoidVOI002& */	public VCTransactionAvailabletovoidVOI002()	{		super();	}	/**	 * All subclasses must override this method to return their own	 * module name.	 * 	 * @return Int	 *//* &VCTransactionAvailabletovoidVOI002.getModuleName& */	public int getModuleName()	{		return GeneralConstant.MISC;	}	/**	 * Handles any errors that may occur	 * 	 * @param aeRTSEx RTSException	 *//* &VCTransactionAvailabletovoidVOI002.handleError& */	public void handleError(RTSException aeRTSEx)	{	}	/**	 * All subclasses must override this method to handle data coming	 * from their JDialogBox - inside the subclasses implementation	 * should be calls to fireRTSEvent() to pass the data to the	 * RTSMediator.	 * 	 * @param aiCommand int	 * @param aaData Object	 *//* &VCTransactionAvailabletovoidVOI002.processData& */	public void processData(int aiCommand, Object aaData)	{		switch (aiCommand)		{ //Close all screens, and proceed to void trancation				case (ENTER) :				setDirectionFlow(AbstractViewController.FINAL);				try				{					//defect 7053					//if (frame != null)					//	frame.setCursor(new java.awt.Cursor					//   (java.awt.Cursor.WAIT_CURSOR));					//end defect 7053					getMediator().processData(						getModuleName(),						MiscellaneousConstant.COMPLETE_TRANSACTION,						aaData);				}				catch (RTSException aeRTSEx)				{					//defect 7053					//if (frame != null)					//	frame.setCursor(new java.awt.Cursor					//    (java.awt.Cursor.DEFAULT_CURSOR));					//end defect 7053					aeRTSEx.displayError(getFrame());				}				break;				//Close all screens				case (CANCEL) :				setDirectionFlow(AbstractViewController.CANCEL);				try				{					getMediator().processData(						getModuleName(),						MiscellaneousConstant.NO_DATA_TO_BUSINESS,						aaData);				}				catch (RTSException aeRTSEx)				{					aeRTSEx.displayError(getFrame());				}				// defect 8884				// use close() so that it does setVisibleRTS()				close();				//getFrame().setVisible(false);				// end 8884				break;				//Return to VOI001 			case (AbstractViewController.PREVIOUS) :				setDirectionFlow(AbstractViewController.PREVIOUS);				try				{					getMediator().processData(						getModuleName(),						MiscellaneousConstant.NO_DATA_TO_BUSINESS,						aaData);				}				catch (RTSException aeRTSEx)				{					aeRTSEx.displayError(getFrame());				}				break;			case (HELP) :				{				}		}	}	/**	 * Set View checks if frame = null, set the screen and pass control	 * to the controller.	 *	 * @param aaPreviousControllers Vector	 * @param asTransCd String	 * @param aaData Object	 *//* &VCTransactionAvailabletovoidVOI002.setView& */	public void setView(		java.util.Vector aaPreviousControllers,		String asTransCd,		Object aaData)	{		if (aaData != null)		{			java.util.Vector lvTransactions = (java.util.Vector) aaData;			//If no transactions, return exception message			if (lvTransactions.size() == 0)			{				RTSException leRTSExNoDataFound = new RTSException(175);				RTSDialogBox laRTSDBox = getMediator().getParent();				if (laRTSDBox != null)				{					leRTSExNoDataFound.displayError(laRTSDBox);				}				else					leRTSExNoDataFound.displayError(						getMediator().getDesktop());				processData(AbstractViewController.PREVIOUS, aaData);				return;			}		}		//if frame has not been displayed		if (getFrame() == null)		{			RTSDialogBox laRTSDBox = getMediator().getParent();			if (laRTSDBox != null)			{				setFrame(					new FrmTransactionAvailabletovoidVOI002(laRTSDBox));			}			else			{				setFrame(					new FrmTransactionAvailabletovoidVOI002(						getMediator().getDesktop()));			}		}		super.setView(aaPreviousControllers, asTransCd, aaData);	}}/* #VCTransactionAvailabletovoidVOI002# */