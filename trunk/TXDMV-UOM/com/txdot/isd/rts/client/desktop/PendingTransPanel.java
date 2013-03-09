package com.txdot.isd.rts.client.desktop;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.general.ui.RTSButton;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.Report;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*  
 * PendingTransPanel.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	10/03/2001	Created
 * N Ting		04/12/2002	Fix for defect CQU100003465, add 
 *							ProcessInventory Vector
 * 							to SaveInv in Restore set aside 
 *							situation
 * N Ting		04/30/2002	Fix for defect CQU100003689, move 
 *							restoreSetAside to the line after 
 *							restoring TransactionHeaderData
 * N Ting		05/01/2002	Fix for defect CQU100003734, set 
 *							btnsetaside button to enable in setData
 * MAbs			05/08/2002	Fix for defect Set tab order for button 
 *							so focus isn't lost in menu CQU100003817
 * SGovindappa	07/11/2002  Fixed CQU100004438, when completing a 
 *							setaside DTA transactions "Enter" and 
 *							"SetAside" Buttons are disabled.
 * K Harrell 	08/02/2002  Fixed CQU100004551, menu items enabled 
 *							when canel off "Set Aside"
 * SGovindappa	08/08/2002  Fixed 4483. Assigned data variable with 
 *							CompleteTransactionData object in 
 *							updatePendingTransForDTA method
 * Nancy					to make PCR25 changes for DTA event.
 * SGovindappa	10/17/2002 	Fixed CQU100004902, when completing a 
 *							VOID transactions "Enter" button is 
 *							disabled.
 * Ray Rowehl	12/18/2002	Fixed CQU100004892.  method setData.
 *							enable cancel button if the restored 
 *							printimmediate is off and cancel is 
 *							allowed. do a desktop.reset in 
 *							handlecancel().
 * Ray Rowehl	07/15/2003	Do not save inventory when restoring set
 *							aside unless it does not exist already
 *							update setData()
 *							defect 6157
 * K Harrell	10/16/2003	Use isDBReady() vs. isDBUp()
 *							modified handleCancel(),reset()
 *							Defect 6614 Ver 5.1.5 Fix 1
 * K Harrell	12/10/2003	Disable Enter on restored 
 *							SetAsideTransaction from prior dates
 *							modified setData()
 *							Defect 6729 Ver 5.1.5 Fix 2
 * K Harrell	12/29/2003	Enable "Enter" only on first transaction.
 *							modified setData()
 *							Defect 6357  Ver 5.1.5 Fix 2
 * J Rue		12/10/2004	Check for diskette in A:\\ drive for DTA
 *							add import
 *							com.txdot.isd.rts.client.title.business
 *							add DISKERROR
 *							add verifyOrigMatchPrcsDTA()
 *							modify actionPerformed()
 *							defect 7736 Ver 5.2.2
 * J Rue		12/15/2004	Change the param from DealerTitleData to 
 *							null.Passing null will ensure only the 
 *							original data is returned without the 
 *							POS/RSPS updated info
 *							modify verifyOrigMatchPrcsDTA()
 *							defect 7736 Ver 5.2.2
 * K Harrell	12/16/2004	Formatting/JavaDoc/Variable Name Cleanup
 *							defect 7736 Ver 5.2.2
 * K Harrell	01/21/2005	Null pointer exception when reject 1st
 *							RSPS diskette transaction.
 *							Reorganized declarations 
 *							modify verifyOrigMatchPrcsDTA(),
 *							actionPerformed()
 *							defect 7910 Ver 5.2.2 
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							Set the default focus on the enter button 
 * 							after the DeskTop has been initialized.
 * 							modify getbtnEnter(), setDesktop()
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	04/01/2005	Handle the setting of the defaultButton 
 * 							better.
 * 							modify setData()
 * 							defect 7885 Ver 5.2.3
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify actionPerformed()
 * 							modify verifyOrigMatchPrcsDTA()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * J Rue		07/07/2005	Removed commented out code.
 * 							defect 7898 Ver 5.2.3
 * J Rue		07/18/2005	Remove changes added by defect 8227
 * 							defect 8227 Ver 5.2.3
 * B Hargrove	08/22/2005	Change text strings to constants. Java 1.4
 * 							cleanup.
 * 							defect 7885 Ver 5.2.3
 * J Rue		08/30/2005	Move verifyOrigMatchPrcsDTA() to 
 * 							TitleClientBusiness
 * 							delete verifyOrigMatchPrcsDTA()
 * 							defect 8227 Ver 5.2.3
 * K Harrell	09/27/2005	Remove isDBReady as parameter to 
 * 							Desktop.repaintMenuItems()
 * 							modify handleCancel(),reset() 
 * 							defect 8337 Ver 5.2.3
 * K Harrell	01/02/2006	Added methods to work with RTSAppController
 * 							for management focus.
 * 							add getCancelButton(),getSetAsideButton()
 * 							modify keyPressed()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	03/09/2007	Implement SystemProperty.isHQ()
 * 							delete getBuilderData()
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/01/2007	Use CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	01/26/2009	Use Transaction.isCancelTrans() to 
 * 							  enable/disable Cancel button. 
 * 							modify setData() 
 * 							defect 7717 Ver Defect POS_D
 * K Harrell	02/03/2009	Do not setCancelTrans for Transaction from
 * 							 TransHeader. Handled in Transaction class.
 * 							modify setData() 
 * 		  					defect 7717 Ver Defect POS_D
 * K Harrell	08/21/2009	Verify DTA transcd, verify vector content 
 * 							 prior to calling updatePendingTransForDTA()
 * 							Implement UtilityMethods.isDTA()
 * 							delete cbIsTransPending 
 * 							modify setData(), updatePendingTrans(), 
 * 							 updatePendingTransForDTA(),getbtnSetAside()
 * 							defect 9387 Ver Defect_POS_F
 * K Harrell	12/16/2009	Minor Cleanup of code comments 
 * 							defect 10290 Ver Defect_POS_H
 * Min Wang		09/21/2010	Accommodate new max lenghs of fee, 
 * 							modify FEE_LENGTH 
 * 							defect 10596 Ver 6.6.0
 * Min Wang		09/27/2010 	modify DEFLT_AMT, FEE_LENGTH
 * 							defect 10596 Ver 6.6.0
 * ---------------------------------------------------------------------
 */

/**  
 * The Pending Trans Panel displays all the information needed in a 
 * Pending Transaction.
 * 
 * @version 6.6.0			09/29/2010 
 * @author Michael Abernethy
 * <br>Creation Date:		10/04/2001 09:39:50
 */

public class PendingTransPanel
	extends javax.swing.JPanel
	implements ActionListener, KeyListener
{
	// Pending Transaction Panel Variables 
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSButton ivjbtnSetAside = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblAmount = null;
	private JLabel ivjlblCustomer = null;
	private JLabel ivjstcLblCustomer = null;
	private JLabel ivjstcLblPending = null;
	private JLabel ivjstcLblTotal = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private javax.swing.JList ivjtxtPending = null;
	private RTSDeskTop caDesktop;

	private Object caNextData = null;
	private CompleteTransactionData caCTData;
	private Vector cvPendingList = new Vector();
	private Vector cvListeners;

	//	Class Variables 
	private boolean cbHasFocus = false;
	private boolean cbWorking = false;

	// Final Static Variables
	public static final String PENDING_MARKER = "PENDING$";

	private static final String CENTER = "Center";
	private static final String CUSTOMER = "Customer:";
	// defect 10596
	//private static final String DEFLT_AMT = "9,999,999.99";
	private static final String DEFLT_AMT = "999,999,999.99";
	// defect 10596
	private static final String DEFLT_SPACES = "                   ";
	private static final String MSG_CANCEL =
		"Are you sure you want to cancel this transaction?";
	private static final String MSG_CHNG_DUE = "Change Due: ";
	private static final String MSG_ROOT_PANE_NULL =
		"Root Pane is null";
	private static final String PENDING_TRANS =
		"PENDING TRANSACTION(S)";
	private static final String SET_ASIDE = "Set Aside";
	private static final String SOUTH = "South";
	private static final String TOT_AMT_DUE = "Total Amount Due:";

	private static final int FEE_DESC_LENGTH = 55;
	// defect 10596
	//private static final int FEE_LENGTH = 8;
	private static final int FEE_LENGTH =12;
	// end defect 10596
	private static final int FEE_STRT_PT = 60;

	/**
	 * PendingTransPanel constructor comment.
	 */
	public PendingTransPanel()
	{
		super();
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!isVisible())
		{
			return;
		}
		if (cbWorking)
		{
			return;
		}
		else
		{
			cbWorking = true;
		}
		try
		{
			if (aaAE.getSource() == getbtnEnter())
			{
				boolean lbSkipPaymentScreen = false;
				boolean lbChngDue = false;
				
				if (SystemProperty.isHQ())
				{
					lbSkipPaymentScreen = true;
				}
				if (Transaction
					.getRunningSubtotal()
					.compareTo(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR))
					== 0)
				{
					lbSkipPaymentScreen = true;
					//lbChngDue = true;
				}
				if (lbSkipPaymentScreen)
				{
					StringBuffer lStringBuffer = new StringBuffer();
					if (lbChngDue)
					{
						lStringBuffer.append(MSG_CHNG_DUE);
						lStringBuffer.append(
							Transaction
								.getRunningSubtotal()
								.printDollar());
					}
					lStringBuffer.append(
						" "
							+ CommonConstant.TXT_COMPLETE_TRANS_QUESTION);
					RTSException laRTSEx =
						new RTSException(
							RTSException.CTL001,
							lStringBuffer.toString(),
							null);
					int liReturnStatus =
						laRTSEx.displayError(
							(javax.swing.JFrame) caDesktop);
					if (liReturnStatus == RTSException.YES)
					{
						Vector lvIn = new Vector();
						lvIn.addElement(caCTData);
						CommonClientBusiness laCommonClientBusiness =
							new CommonClientBusiness();
						laCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.END_TRANS,
							lvIn);
						getbtnEnter().requestFocus();
						reset();
					}
					else
					{
						return;
					}
				}
				else
				{
					caNextData = caCTData;
					//desktop.repaintMenuItems(desktop.getSecurityData(), 
					//  RTSApplicationController.isDBUp());
					getbtnEnter().requestFocus();
					ActionEvent laAE =
						new ActionEvent(
							this,
							ActionEvent.ACTION_PERFORMED,
							PENDING_MARKER + ScreenConstant.PMT001);
					fireActionEvent(laAE);
				}
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getbtnEnter().requestFocus();
				handleCancel();
			}
			else if (aaAE.getSource() == getbtnSetAside())
			{
				getbtnEnter().requestFocus();
				// defect 4551 
				//		desktop.repaintMenuItems(
				//  	desktop.getSecurityData(),
				//  RTSApplicationController.isDBUp());
				// end defect 4551 
				ActionEvent laAE =
					new ActionEvent(
						this,
						ActionEvent.ACTION_PERFORMED,
						PENDING_MARKER + ScreenConstant.CUS002);
				fireActionEvent(laAE);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError((javax.swing.JFrame) this.getParent());
		}
		finally
		{
			cbWorking = false;
		}
	}
	
	/**
	 * Add Action Listener 
	 * 
	 * @param aaAL ActionListener
	 */
	public void addActionListener(ActionListener aaAL)
	{
		if (!cvListeners.contains(aaAL))
		{
			cvListeners.add(aaAL);
		}
	}
	
	/**
	 * This sets the variable to indicate whether any of the components of 
	 * this panel has focus.
	 * 
	 * @param aaComp Component 
	 */
	public void containsFocus(Component aaComp)
	{
		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			Component[] larrChildren = laCont.getComponents();
			for (int i = 0; i < larrChildren.length; i++)
			{
				containsFocus(larrChildren[i]);
			}
		}
		if (aaComp == null)
		{
			return;
		}
		if (aaComp.hasFocus())
		{
			cbHasFocus = true;
			return;
		}
	}
	
	/**
	 * Fire Action Event
	 *  
	 * @param aaAE ActionEvent
	 */
	private synchronized void fireActionEvent(ActionEvent aaAE)
	{
		for (int i = 0; i < cvListeners.size(); i++)
		{
			ActionListener laAL = (ActionListener) cvListeners.get(i);
			laAL.actionPerformed(aaAE);
		}
	}
	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addKeyListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnCancel;
	}
	
	/**
	 * Return the btnEnter property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setName("btnEnter");
				ivjbtnEnter.setText(CommonConstant.BTN_TXT_ENTER);
				// user code begin {1}

				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.addKeyListener(this);
				if (getRootPane() != null)
				{
					getRootPane().setDefaultButton(ivjbtnEnter);
				}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnEnter;
	}
	/**
	 * Return the btnSetAside property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnSetAside()
	{
		if (ivjbtnSetAside == null)
		{
			try
			{
				ivjbtnSetAside = new RTSButton();
				ivjbtnSetAside.setName("btnSetAside");
				// defect 9387 
				//ivjbtnSetAside.setMnemonic('s');
				ivjbtnSetAside.setMnemonic(KeyEvent.VK_S);
				// end defect 9387 
				ivjbtnSetAside.setText(SET_ASIDE);
				// user code begin {1}
				ivjbtnSetAside.addActionListener(this);
				ivjbtnSetAside.addKeyListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnSetAside;
	}
	
	/**
	 * Return value of caNextData
	 *
	 * @return Object
	 */
	public Object getData()
	{
		return caNextData;
	}
	
	/**
	 * Return the EnterButton property value
	 *  
	 * @return RTSButton
	 */
	public RTSButton getEnterButton()
	{
		return getbtnEnter();
	}
	
	/**
	 * Return the SetAsideButton property value
	 *  
	 * @return RTSButton
	 */
	public RTSButton getSetAsideButton()
	{
		return getbtnSetAside();
	}
	
	/**
	 * Return the CancelButton property value
	 *  
	 * @return RTSButton
	 */
	public RTSButton getCancelButton()
	{
		return getbtnCancel();
	}
	
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsstcLblTotal =
					new java.awt.GridBagConstraints();
				constraintsstcLblTotal.gridx = 0;
				constraintsstcLblTotal.gridy = 0;
				constraintsstcLblTotal.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintsstcLblTotal.anchor =
					java.awt.GridBagConstraints.EAST;
				constraintsstcLblTotal.weightx = 0.6;
				constraintsstcLblTotal.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getJPanel1().add(
					getstcLblTotal(),
					constraintsstcLblTotal);

				java.awt.GridBagConstraints constraintslblAmount =
					new java.awt.GridBagConstraints();
				constraintslblAmount.gridx = 1;
				constraintslblAmount.gridy = 0;
				constraintslblAmount.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintslblAmount.anchor =
					java.awt.GridBagConstraints.EAST;
				constraintslblAmount.weightx = 1.0;
				constraintslblAmount.insets =
					new java.awt.Insets(4, 4, 4, 55);
				getJPanel1().add(getlblAmount(), constraintslblAmount);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}
	
	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(new java.awt.BorderLayout());
				getJPanel2().add(getJPanel1(), SOUTH);
				getJPanel2().add(getJScrollPane1(), CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel2;
	}
	
	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 0;
				constraintsbtnEnter.gridy = 0;
				constraintsbtnEnter.weightx = 1.0;
				constraintsbtnEnter.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getJPanel3().add(getbtnEnter(), constraintsbtnEnter);

				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 1;
				constraintsbtnCancel.gridy = 0;
				constraintsbtnCancel.weightx = 1.0;
				constraintsbtnCancel.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getJPanel3().add(getbtnCancel(), constraintsbtnCancel);

				java.awt.GridBagConstraints constraintsbtnSetAside =
					new java.awt.GridBagConstraints();
				constraintsbtnSetAside.gridx = 2;
				constraintsbtnSetAside.gridy = 0;
				constraintsbtnSetAside.weightx = 1.0;
				constraintsbtnSetAside.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getJPanel3().add(
					getbtnSetAside(),
					constraintsbtnSetAside);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel3;
	}
	
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setRequestFocusEnabled(false);
				ivjJScrollPane1.setViewportView(gettxtPending());
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	
	/**
	 * Return the lblAmount property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblAmount()
	{
		if (ivjlblAmount == null)
		{
			try
			{
				ivjlblAmount = new javax.swing.JLabel();
				ivjlblAmount.setName("lblAmount");
				ivjlblAmount.setAlignmentX(
					java.awt.Component.RIGHT_ALIGNMENT);
				ivjlblAmount.setText(DEFLT_AMT);
				ivjlblAmount.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjlblAmount.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblAmount;
	}
	
	/**
	 * Return the lblCustomer property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblCustomer()
	{
		if (ivjlblCustomer == null)
		{
			try
			{
				ivjlblCustomer = new javax.swing.JLabel();
				ivjlblCustomer.setName("lblCustomer");
				ivjlblCustomer.setText(DEFLT_SPACES);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblCustomer;
	}
	
	/**
	 * Return the stcLblCustomer property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCustomer()
	{
		if (ivjstcLblCustomer == null)
		{
			try
			{
				ivjstcLblCustomer = new javax.swing.JLabel();
				ivjstcLblCustomer.setName("stcLblCustomer");
				ivjstcLblCustomer.setText(CUSTOMER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblCustomer;
	}
	
	/**
	 * Return the stcLblPending property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPending()
	{
		if (ivjstcLblPending == null)
		{
			try
			{
				ivjstcLblPending = new javax.swing.JLabel();
				ivjstcLblPending.setName("stcLblPending");
				ivjstcLblPending.setText(PENDING_TRANS);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPending;
	}
	
	/**
	 * Return the LblTotal property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotal()
	{
		if (ivjstcLblTotal == null)
		{
			try
			{
				ivjstcLblTotal = new javax.swing.JLabel();
				ivjstcLblTotal.setName("stcLblTotal");
				ivjstcLblTotal.setText(TOT_AMT_DUE);
				ivjstcLblTotal.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblTotal;
	}
	
	/**
	 * Return the txtPending property value.
	 * 
	 * @return JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JList gettxtPending()
	{
		if (ivjtxtPending == null)
		{
			try
			{
				ivjtxtPending = new javax.swing.JList();
				ivjtxtPending.setName("txtPending");
				ivjtxtPending.setFont(
					new java.awt.Font("monospaced", 0, 12));
				ivjtxtPending.setBounds(0, 0, 368, 198);
				// user code begin {1}
				//ivjtxtPending.setNextFocusableComponent(getbtnEnter());
				ivjtxtPending.addKeyListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPending;
	}
	
	/**
	 * Handle Cancel
	 * 
	 */
	private void handleCancel()
	{
		try
		{
			RTSException leRTSEx =
				new RTSException(RTSException.CTL001, MSG_CANCEL, null);
			int liReturnStatus =
				leRTSEx.displayError((javax.swing.JFrame) caDesktop);
			if (liReturnStatus == RTSException.YES)
			{
				caDesktop.repaintMenuItems(
					caDesktop.getSecurityData(),
					RTSApplicationController.isDBReady());
				CommonClientBusiness laCommonClientBusiness =
					new CommonClientBusiness();
				laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.CANCEL_TRANS,
					null);
				reset();
				// defect 4892
				caDesktop.reset();
				// end defect 4892
			}
			else
			{
				return;
			}
			reset();
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(caDesktop);
		}
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		///* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(caDesktop);
	}
	
	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			cvListeners = new java.util.Vector();
			// user code end
			setName("PendingTransPanel");
			setLayout(new java.awt.GridBagLayout());
			setSize(620, 315);

			java.awt.GridBagConstraints constraintslblCustomer =
				new java.awt.GridBagConstraints();
			constraintslblCustomer.gridx = 1;
			constraintslblCustomer.gridy = 1;
			constraintslblCustomer.gridwidth = 2;
			constraintslblCustomer.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			constraintslblCustomer.anchor =
				java.awt.GridBagConstraints.WEST;
			constraintslblCustomer.ipadx = 125;
			constraintslblCustomer.insets =
				new java.awt.Insets(4, 4, 4, 0);
			add(getlblCustomer(), constraintslblCustomer);

			java.awt.GridBagConstraints constraintsstcLblCustomer =
				new java.awt.GridBagConstraints();
			constraintsstcLblCustomer.gridx = 0;
			constraintsstcLblCustomer.gridy = 1;
			constraintsstcLblCustomer.ipadx = 2;
			constraintsstcLblCustomer.insets =
				new java.awt.Insets(4, 41, 4, 4);
			add(getstcLblCustomer(), constraintsstcLblCustomer);

			java.awt.GridBagConstraints constraintsstcLblPending =
				new java.awt.GridBagConstraints();
			constraintsstcLblPending.gridx = 0;
			constraintsstcLblPending.gridy = 0;
			constraintsstcLblPending.gridwidth = 2;
			constraintsstcLblPending.ipadx = 5;
			constraintsstcLblPending.insets =
				new java.awt.Insets(22, 41, 3, 43);
			add(getstcLblPending(), constraintsstcLblPending);

			java.awt.GridBagConstraints constraintsJPanel2 =
				new java.awt.GridBagConstraints();
			constraintsJPanel2.gridx = 0;
			constraintsJPanel2.gridy = 2;
			constraintsJPanel2.gridwidth = 4;
			constraintsJPanel2.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel2.weightx = 1.0;
			constraintsJPanel2.weighty = 1.0;
			constraintsJPanel2.insets =
				new java.awt.Insets(4, 40, 4, 40);
			add(getJPanel2(), constraintsJPanel2);

			java.awt.GridBagConstraints constraintsJPanel3 =
				new java.awt.GridBagConstraints();
			constraintsJPanel3.gridx = 0;
			constraintsJPanel3.gridy = 3;
			constraintsJPanel3.gridwidth = 4;
			constraintsJPanel3.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel3.weightx = 1.0;
			constraintsJPanel3.insets =
				new java.awt.Insets(4, 40, 4, 40);
			add(getJPanel3(), constraintsJPanel3);
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * Invoked when a key has been pressed.
	 *
	 * @param aaKE KeyEvent 
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		cbHasFocus = false;
		//call this method to set the value of cbHasFocus.
		containsFocus(this);
		if (!(cbHasFocus && isVisible()))
		{
			// If doesn't have focus or is not visible, return.This will 
			// happen when panel is hidden or menu item is clicked.
			return;
		}
		if (aaKE.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (aaKE.getSource().equals(gettxtPending()))
			{
				getbtnEnter().doClick();
				aaKE.consume();
				return;
			}
		}
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (getbtnCancel().isVisible()
				&& getbtnCancel().isEnabled())
			{
				handleCancel();
				aaKE.consume();
				return;
			}
		}
		//		if (KeyEvent.VK_TAB == aaKE.getKeyCode()
		//			&& aaKE.getSource() instanceof javax.swing.JTextArea)
		//		{
		//			if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
		//			{
		//				getbtnSetAside().requestFocus();
		//			}
		//			else
		//			{
		//				getbtnEnter().requestFocus();
		//			}
		//			return;
		//		}
		if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			//	Enter <-> Cancel <-> Set Aside 
			if (getbtnEnter().hasFocus())
			{
				if (getbtnCancel().isEnabled())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnSetAside().isEnabled())
				{
					getbtnSetAside().requestFocus();
				}
				else
				{
					getbtnEnter().requestFocus();
				}
			}
			//	Enter <-> Cancel <-> Set Aside
			else if (getbtnCancel().hasFocus())
			{
				if (getbtnSetAside().isEnabled())
				{
					getbtnSetAside().requestFocus();
				}
				else if (getbtnEnter().isEnabled())
				{
					getbtnEnter().requestFocus();
				}
				else
				{
					getbtnCancel().requestFocus();
				}
			}
			//	Enter <-> Cancel <-> Set Aside
			else if (getbtnSetAside().hasFocus())
			{
				if (getbtnEnter().isEnabled())
				{
					getbtnEnter().requestFocus();
				}
				else if (getbtnCancel().isEnabled())
				{
					getbtnCancel().requestFocus();
				}
				else
				{
					getbtnSetAside().requestFocus();
				}
			}
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		{
			// Enter <-> Cancel <-> Set Aside 
			if (getbtnCancel().hasFocus())
			{
				if (getbtnEnter().isEnabled())
				{
					getbtnEnter().requestFocus();
				}
				else if (getbtnSetAside().isEnabled())
				{
					getbtnSetAside().requestFocus();
				}
				else
				{
					getbtnCancel().requestFocus();
				}
			}
			//	Enter <-> Cancel <-> Set Aside
			else if (getbtnSetAside().hasFocus())
			{
				if (getbtnCancel().isEnabled())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnEnter().isEnabled())
				{
					getbtnEnter().requestFocus();
				}
				else
				{
					getbtnSetAside().requestFocus();
				}
			}
			//	Enter <-> Cancel <-> Set Aside
			else if (getbtnEnter().hasFocus())
			{
				if (getbtnSetAside().isEnabled())
				{
					getbtnSetAside().requestFocus();
				}
				else if (getbtnCancel().isEnabled())
				{
					getbtnCancel().requestFocus();
				}
				else
				{
					getbtnEnter().requestFocus();
				}
			}
		}
	}
	
	/**
	 * Invoked when a key has been released.
	 *
	 * @param aaKE KeyEvent 
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// empty code block
	}
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 *
	 * @param aaKE KeyEvent 
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param aaArgs String[]
	 */
	public static void main(String[] aaArgs)
	{
		try
		{
			javax.swing.JFrame laFrame = new javax.swing.JFrame();
			PendingTransPanel laPendingTransPanel;
			laPendingTransPanel = new PendingTransPanel();
			laFrame.setContentPane(laPendingTransPanel);
			laFrame.setSize(laPendingTransPanel.getSize());
			laFrame
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrame.show();
			java.awt.Insets insets = laFrame.getInsets();
			laFrame.setSize(
				laFrame.getWidth() + insets.left + insets.right,
				laFrame.getHeight() + insets.top + insets.bottom);
			laFrame.setVisible(true);
		}
		catch (Throwable leThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leThrowable.printStackTrace(System.out);
		}
	}
	
	/**
	 * Remove Action Listener 
	 * 
	 * @param aaAL ActionListener
	 */
	public void removeActionListener(ActionListener aaAL)
	{
		cvListeners.remove(aaAL);
	}
	
	/** 
	 * Reset the Pending Trans Panel
	 */
	public void reset()
	{
		cvPendingList.clear();
		gettxtPending().setListData(cvPendingList);
		caDesktop.setImagePanelVisible(true);
		caDesktop.repaintMenuItems(
			caDesktop.getSecurityData(),
			RTSApplicationController.isDBReady());
	}
	
	/**
	 * Based on the dataObject, call appropriate methods to populate the
	 * Pending Trans list box
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			// defect 7885
			// TODO Take another look at improving this!
			// set the default button after the RootPane has been 
			// initialized.
			if (getRootPane() != null)
			{
				getRootPane().setDefaultButton(getEnterButton());
			}
			else
			{
				System.out.println(MSG_ROOT_PANE_NULL);
			}
			// end defect 7885

			// make sure enter and set aside is enabled
			// defect 6357
			// Enable SetAside only on first transaction.
			// getbtnSetAside().setEnabled(true);
			if (cvPendingList.size() == 0)
			{
				getbtnSetAside().setEnabled(true);
			}
			// end defect 6357 
			getbtnEnter().setEnabled(true);
			// defect 7717 
			//			if (!Transaction.isCancelTrans())
			//			{
			//				getbtnCancel().setEnabled(false);
			//			}
			//			else
			//			{
			//				getbtnCancel().setEnabled(true);
			//			}
			if (SystemProperty.getPrintImmediateIndi() == 1
				|| SystemProperty.getPrintImmediateIndi() == 2)
			{
				getbtnCancel().setEnabled(false);
			}
			else
			{
				getbtnCancel().setEnabled(Transaction.isCancelTrans());
			}
			// end defect 7717 

			caDesktop.setPrintImmediate(
				SystemProperty.getPrintImmediateIndi());
			if (aaDataObject != null)
			{
				if (aaDataObject instanceof CompleteTransactionData)
				{
					caCTData =
						(CompleteTransactionData) UtilityMethods.copy(
							aaDataObject);
					if (Transaction.isCustomer())
					{
						updatePendingTrans();
						getlblAmount().setText(
							Transaction
								.getRunningSubtotal()
								.printDollar());
						getlblCustomer().setText(
							Transaction.getCustName());
						// defect 9387 
						if (UtilityMethods
							.isDTA(caCTData.getTransCode()))
						{
							// end defect 9387 
							getbtnSetAside().setEnabled(false);
						}
					}
					else
					{
						reset();
					}
				} // CompleteTransactionData 
				else
					//Not CompleteTransactionData
					{
					// defect 9387 
					if (aaDataObject instanceof Vector)
					{
						Vector lvData = (Vector) aaDataObject;
						
						// CTData is null at this point 
						if (!lvData.isEmpty()
							&& lvData.elementAt(0)
								instanceof DealerTitleData)
						{
							updatePendingTransForDTA(lvData);
							// end defect 9387

							getlblAmount().setText(
								Transaction
									.getRunningSubtotal()
									.printDollar());
							getlblCustomer().setText(
								Transaction.getCustName());
						}
					} // Vector 
					else
					{
						if (aaDataObject
							instanceof CompleteVehicleTransactionData)
						{
							//reset();
							CompleteVehicleTransactionData laCVTData =
								(CompleteVehicleTransactionData) aaDataObject;
							TransactionHeaderData laTransHeaderData =
								laCVTData.getTransactionHeader();
							//fill in the header data
							Transaction.setTransactionHeaderData(
								laTransHeaderData);
							//fill in the Saved Vehicle Data
							Transaction.restoreSetAside();
							// defect 7717 
							// Removed - set later 
							//Transaction.setCancelTrans(
							//	laTransHeaderData.isCancelTrans());
							// end defect 7717 
							//populate name field
							getlblCustomer().setText(
								laTransHeaderData.getTransName());
							//Populate pending trans screen and calculate subtotal
							Vector lvRegFeesData =
								laCVTData.getRegFeesData();
							for (int i = 0;
								i < lvRegFeesData.size();
								i++)
							{
								RegFeesData laRegFeesData =
									(RegFeesData) lvRegFeesData.get(i);
								caCTData =
									new CompleteTransactionData();
								caCTData.setTransCode(
									laRegFeesData.getTransCd());
								caCTData.setRegFeesData(laRegFeesData);
								updatePendingTrans();
								Vector lvFeesData =
									laRegFeesData.getVectFees();
								for (int j = 0;
									j < lvFeesData.size();
									j++)
								{
									FeesData laFeesData =
										(FeesData) lvFeesData.get(j);
									Transaction.setRunningSubtotal(
										Transaction
											.getRunningSubtotal()
											.add(
											laFeesData.getItemPrice()));
								}
							}
							//Disable the Enter and SetAside button for DTA events
							RegFeesData laRegFeesData = null;
							if (lvRegFeesData.size() > 0)
							{
								laRegFeesData =
									(
										RegFeesData) lvRegFeesData
											.elementAt(
										0);
							}

							String lsTransCd =
								laRegFeesData.getTransCd();

							// defect 9387 
							if (UtilityMethods.isDTA(lsTransCd))
							{
								// end defect 9387
								getbtnEnter().setEnabled(false);
								getbtnSetAside().setEnabled(false);
							}

							// defect 6157
							// only save Inventory from CompleteVehicleTransactionData if the
							// current memory is empty
							//Save issued inventory in memory
							CommonClientBusiness laCommonClientBusiness =
								new CommonClientBusiness();
							Object laObject =
								laCommonClientBusiness.processData(
									GeneralConstant.COMMON,
									CommonConstant.GET_SAVED_INVENTORY,
									laCVTData
										.getProcessInventoryData());
							if (laObject == null)
							{
								laCommonClientBusiness.processData(
									GeneralConstant.COMMON,
									CommonConstant.SAVE_INVENTORY,
									laCVTData
										.getProcessInventoryData());
							}
							// end defect 6157
							if (lvRegFeesData.size() == 1)
							{
								Transaction.setMultipleTrans(false);
							}
							else
							{
								Transaction.setMultipleTrans(true);
							}
							getlblAmount().setText(
								Transaction
									.getRunningSubtotal()
									.printDollar());
							//getlblCustomer().setText(CompleteTransactionData.getCustName());
							//Restore printimmediate
							Transaction.setRestoredSetAside(true);
							Transaction.setStoredPrintIndi(
								SystemProperty.getPrintImmediateIndi());
							SystemProperty.setPrintImmediateIndi(
								laCVTData
									.getTransactionHeader()
									.getPrintImmediate());
							if (SystemProperty.getPrintImmediateIndi()
								== 1
								|| SystemProperty.getPrintImmediateIndi()
									== 2)
							{
								getbtnCancel().setEnabled(false);
							}
							// defect 4892
							// enable cancel in this case
							else
							{
								// defect 7717 
								// 	if (Transaction.isCancelTrans()
								//		&& SystemProperty
								//			.getPrintImmediateIndi()
								//			== 0)
								//	{
								//		getbtnCancel().setEnabled(true);
								//	}

								getbtnCancel().setEnabled(
									Transaction.isCancelTrans());
								// end defect 7717
							}
							// end defect 4892
							caDesktop.setPrintImmediate(
								SystemProperty.getPrintImmediateIndi());
							//restore set PrintCashReceipt status
							Transaction.setPrintCashReceipt(
								laCVTData.isMultipleTrans());
							if (SystemProperty.getPrintImmediateIndi()
								== 1)
							{
								Transaction.setPrintCashReceipt(true);
							}
							Transaction.setTransIdList(
								laCVTData.getTransIdList());
							Transaction.setRcptDirForCust(
								Transaction.getCompleteTransRcptName(
									laCVTData
										.getTransactionHeader()
										.getCustSeqNo()));
							//defect 4902
							//Force set aside void transactions to cancel out
							if (lsTransCd.equals(TransCdConstant.VOID))
							{
								getbtnEnter().setEnabled(false);
								getbtnSetAside().setEnabled(true);
								getbtnCancel().setEnabled(true);
							}
							// defect 6729 
							// Disable Enter for transactions of prior date
							RTSDate laCurrentRTSDate =
								RTSDate.getCurrentDate();
							int liCurrentTransAMDate =
								laCurrentRTSDate.getAMDate();
							if (laTransHeaderData.getTransAMDate()
								!= liCurrentTransAMDate)
							{
								getbtnEnter().setEnabled(false);
							}
							// end defect 6729 
						} // CompleteVehicleTransactionData 
					} // Not Vector
				} // Not CompleteTransactionData
				if (getbtnEnter().isEnabled())
				{
					getbtnEnter().requestFocus();
				} // Enter enabled
				else
				{
					if (getbtnCancel().isEnabled())
					{
						getbtnCancel().requestFocus();
					} // Cancel enabled 
				} // Enter disabled 
			} // aaDataObject !=null
		} // try
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(caDesktop);
		}
	}
	
	/**
	 * Set the caDesktop
	 *
	 * @param aaDesktop RTSDeskTop
	 */
	public void setDesktop(RTSDeskTop aaDesktop)
	{
		this.caDesktop = aaDesktop;
		// set the default button after the Desktop has been 
		// initialized.
		if (getRootPane() != null)
		{
			getRootPane().setDefaultButton(getEnterButton());
		}
		else
		{
			System.out.println(MSG_ROOT_PANE_NULL);
		}
	}
	
	/**
	 * Update Pending Trans List box using the RegFees Vector
	 *
	 * @throws RTSException 
	 */
	private void updatePendingTrans() throws RTSException
	{
		// defect 9387 
		// cbIsTransPending = true;
		// end defect 9387 
		Report laReport = null;
		RegFeesData laRegFees = caCTData.getRegFeesData();
		TransactionCodesData laTransactionCodesData =
			TransactionCodesCache.getTransCd(caCTData.getTransCode());
		if (laTransactionCodesData != null)
		{
			cvPendingList.addElement(
				laTransactionCodesData.getTransCdDesc());
		}
		else
		{
			cvPendingList.addElement(caCTData.getTransCode());
		}
		if (laRegFees != null)
		{
			Vector lvFees = laRegFees.getVectFees();
			
			if (lvFees != null)
			{
				for (int i = 0; i < lvFees.size(); i++)
				{
					FeesData laFeeData = (FeesData) lvFees.get(i);
					laReport = new Report();
					AccountCodesData laAcctCdData =
						AccountCodesCache.getAcctCd(
							laFeeData.getAcctItmCd(),
							new RTSDate().getYYYYMMDDDate());
					if (laAcctCdData == null)
					{
						laReport.print(
							CommonConstant.STR_SPACE_TWO
								+ CommonConstant.STR_DASH
								+ CommonConstant.STR_SPACE_ONE,
							0,
							FEE_DESC_LENGTH);
					}
					else
					{
						laReport.print(
							CommonConstant.STR_SPACE_TWO
								+ CommonConstant.STR_DASH
								+ CommonConstant.STR_SPACE_ONE
								+ laAcctCdData.getAcctItmCdDesc(),
							0,
							FEE_DESC_LENGTH);
					}
					laReport.rightAlign(
						laFeeData.getItemPrice().toString(),
						FEE_STRT_PT,
						FEE_LENGTH);
					cvPendingList.addElement(
						laReport.getReport().toString());
				}
			}
		}
		gettxtPending().setListData(cvPendingList);
		gettxtPending().setSelectedIndex(0);
		getbtnEnter().requestFocus();
	}
	
	/**
	 * Update Pending Trans List box for DTA events
	 * 
	 * @param avDlrTtlData Vector 
	 */
	private void updatePendingTransForDTA(Vector avDlrTtlData)
		throws RTSException
	{
		caCTData = null;
		// defect 9387 
		// cbIsTransPending = true;
		// end dfect 9387 
		Report laReport = null;
		String lsDTATransCd = null;
		String lsTransCd = null;
		for (int i = 0; i < avDlrTtlData.size(); i++)
		{
			DealerTitleData laDlrTtlData = (DealerTitleData) avDlrTtlData.get(i);
			lsDTATransCd = (String) laDlrTtlData.getTransCd();
			if (lsDTATransCd != null)
			{
				lsTransCd = lsDTATransCd;
				TransactionCodesData laTransactionCodesData =
					TransactionCodesCache.getTransCd(lsDTATransCd);
				if (laTransactionCodesData != null)
				{
					cvPendingList.addElement(
						laTransactionCodesData.getTransCdDesc());
				}
				// defect 9387
				//	else
				//	{
				//		//lReport.println(data.getTransCode());
				//		cvPendingList.addElement(caCTData.getTransCode());
				//	}
				// end defect 9387
			}
			if (!laDlrTtlData.isRecordRejected())
			{
				Vector lvDTAInv = laDlrTtlData.getInventoryData();
				
				if (lvDTAInv != null)
				{
					CommonClientBusiness laCommonClientBusiness =
						new CommonClientBusiness();
					laCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_INVENTORY,
						lvDTAInv);
				}
				Vector lvRegFee = laDlrTtlData.getRegFee();
				
				if (lvRegFee != null)
				{
					for (int j = 0; j < lvRegFee.size(); j++)
					{
						FeesData laFeeData = (FeesData) lvRegFee.get(j);
						laReport = new Report();
						AccountCodesData laAcctCdData =
							AccountCodesCache.getAcctCd(
								laFeeData.getAcctItmCd(),
								new RTSDate().getYYYYMMDDDate());
						if (laAcctCdData == null)
						{
							laReport.print(
								CommonConstant.STR_SPACE_TWO
									+ CommonConstant.STR_DASH
									+ CommonConstant.STR_SPACE_ONE,
								0,
								FEE_DESC_LENGTH);
						}
						else
						{
							laReport.print(
								CommonConstant.STR_SPACE_TWO
									+ CommonConstant.STR_DASH
									+ CommonConstant.STR_SPACE_ONE
									+ laAcctCdData.getAcctItmCdDesc(),
								0,
								FEE_DESC_LENGTH);
						}
						laReport.rightAlign(
							laFeeData.getItemPrice().toString(),
							FEE_STRT_PT,
							FEE_LENGTH);
						cvPendingList.addElement(
							laReport.getReport().toString());
					}
				}
			}
		}
		gettxtPending().setListData(cvPendingList);
		gettxtPending().setSelectedIndex(0);
		getbtnSetAside().setEnabled(false);
		caCTData = new CompleteTransactionData();
		caCTData.setTransCode(lsTransCd);
	}
}
