package com.txdot.isd.rts.client.accounting.ui;import java.util.Vector;import com.txdot.isd.rts.client.general.ui.AbstractViewController;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.constants.AccountingConstant;import com.txdot.isd.rts.services.util.constants.GeneralConstant;import com.txdot.isd.rts.services.util.constants.ScreenConstant;/* *  * * VCFundsRemittanceACC018.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * M Abernethy	09/07/2001  Added comments  * K Harrell	03/25/2004	JavaDoc Cleanup * 							Ver 5.2.0 * K Harrell	03/02/2005	Java 1.4 Work * 							defect 7884 Ver 5.2.3  * Ray Rowehl	03/21/2005	Use getters and setters to access parent  * 							fields * 							defect 7884 Ver 5.2.3 * K Harrell	07/26/2005	Java 1.4 Work * 							defect 7884 Ver 5.2.3  * ---------------------------------------------------------------------  *//** * The View Controller for the ACC018 screen.  It handles screen  * navigation and controls the visibility of its frame. * * @version	5.2.3		07/26/2005  * @author	Michael Abernethy * <br>Creation Date:	07/03/2001 08:27:41 *//* &VCFundsRemittanceACC018& */public class VCFundsRemittanceACC018 extends AbstractViewController{	// Constant/* &VCFundsRemittanceACC018'PAY_IN_FULL& */	public final static int PAY_IN_FULL = 20;/* &VCFundsRemittanceACC018'ITEM_SELECTED& */	public final static int ITEM_SELECTED = 21;	/**	 * Creates a VCFundsRemittanceACC018.	 *//* &VCFundsRemittanceACC018.VCFundsRemittanceACC018& */	public VCFundsRemittanceACC018()	{		super();	}	/**	 * Returns the Module name constant used by the RTSMediator to pass	 * the data to the appropriate Business Layer class.	 * 	 * @return int	 *//* &VCFundsRemittanceACC018.getModuleName& */	public int getModuleName()	{		return GeneralConstant.ACCOUNTING;	}	/**	 * Controls the screen flow from ACC018.  It passes the data to the	 * RTSMediator.	 * 	 * @param aiCommand	int 	 * @param aaData 	Object	 *//* &VCFundsRemittanceACC018.processData& */	public void processData(int aiCommand, Object aaData)	{		switch (aiCommand)		{			case ENTER :				{					setDirectionFlow(AbstractViewController.PREVIOUS);					try					{						getMediator().processData(							getModuleName(),							AccountingConstant.NO_DATA_TO_BUSINESS,							aaData);					}					catch (RTSException leRTSEx)					{						leRTSEx.displayError(getFrame());					}					getFrame().setVisibleRTS(false);					break;				}			case CANCEL :				{					setDirectionFlow(AbstractViewController.CANCEL);					try					{						getMediator().processData(							getModuleName(),							AccountingConstant.NO_DATA_TO_BUSINESS,							aaData);					}					catch (RTSException leRTSEx)					{						leRTSEx.displayError(getFrame());					}					getFrame().setVisibleRTS(false);					break;				}			case PAY_IN_FULL :				{					setDirectionFlow(AbstractViewController.PREVIOUS);					try					{						getMediator().processData(							getModuleName(),							AccountingConstant.NO_DATA_TO_BUSINESS,							aaData);					}					catch (RTSException leRTSEx)					{						leRTSEx.displayError(getFrame());					}					getFrame().setVisibleRTS(false);					break;				}			case ITEM_SELECTED :				{					setData(aaData);					setNextController(ScreenConstant.ACC019);					setDirectionFlow(AbstractViewController.NEXT);					try					{						getMediator().processData(							getModuleName(),							AccountingConstant.NO_DATA_TO_BUSINESS,							aaData);					}					catch (RTSException leRTSEx)					{						leRTSEx.displayError(getFrame());					}					break;				}		}	}	/**	 * Creates the actual frame, stores the protected variables needed 	 * by the VC, and sends the data to the frame.	 * 	 * @param avPreviousControllers	Vector 	 * @param asTransCode 			String	 * @param aaData 				Object	 *//* &VCFundsRemittanceACC018.setView& */	public void setView(		Vector avPreviousControllers,		String asTransCode,		Object aaData)	{		if (getFrame() == null)		{			java.awt.Dialog laDialog = getMediator().getParent();			if (laDialog == null)			{				setFrame(					new FrmFundsRemittanceACC018(						getMediator().getDesktop()));			}			else			{				setFrame(new FrmFundsRemittanceACC018(laDialog));			}		}		super.setView(avPreviousControllers, asTransCode, aaData);	}}/* #VCFundsRemittanceACC018# */