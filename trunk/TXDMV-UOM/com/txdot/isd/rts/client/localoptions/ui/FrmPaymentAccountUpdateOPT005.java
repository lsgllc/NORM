package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.PaymentAccountData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPaymentAccountUpdateOPT005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * B Arredondo	03/12/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		03/09/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		08/25/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	09/03/2008	Add Admin Log Processing
 * 							add PYMNT_ACCT
 * 							add caPymntAcctData
 * 							add getAdminLogData()
 * 							delete cvPAData  (was NOT vector!) 
 * 							delete getBuilderData() 
 * 							modify actionPerformed()
 * 							defect 8595 Ver Defect_POS_B  
 * K Harrell	08/22/2009	Implement new AdminLogData constructor()
 * 							Implement RTSButtonGroup
 * 							delete keyPressed()   
 * 							modify getAdminLogData(), initialize() 
 * 							defect 8628 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Frame for payment account update. In this frame user inputs 
 * Comptroller's Location Code and Account description.
 *
 * @version	Defect_POS_F 	08/22/2009
 * @author	Administrator
 * <br>Creation Date:		11/13/2001 18:16:20
 */

public class FrmPaymentAccountUpdateOPT005
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnAction = null;
	private RTSButton ivjbtnCancel = null;
	private JLabel ivjlblAcctDesc = null;
	private JLabel ivjlblCLocCode = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSInputField ivjtxtPymtLocId = null;
	private RTSInputField ivjtxtPymtLocDesc = null;

	// defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628 

	private boolean cbInit = true;
	private int ciSelRow = -1;
	private int ciActionType = 0;
	private Vector cvData = null;

	// defect 8595 
	private PaymentAccountData caPymntAcctData = null;
	private final static String PYMNT_ACCT = "Payment Account";
	// end defect 8595 

	private final static String ACCOUNT_DESC = "Account Description:";
	private final static String COMPTROLL_LOCATION_CD =
		"Comptroller\'s Location Code:";
	private final static String OPT005_FRAME_TITLE =
		"Payment Account Update     OPT005 ";

	/**
	 * FrmPaymentAccountUpdateOPT005 constructor comment.
	 */
	public FrmPaymentAccountUpdateOPT005()
	{
		super();
		initialize();
	}

	/**
	 * FrmPaymentAccountUpdateOPT005 constructor comment.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmPaymentAccountUpdateOPT005(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPaymentAccountUpdateOPT005 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPaymentAccountUpdateOPT005(JFrame parent)
	{
		super(parent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getbtnAction())
			{
				if (!validateInput())
				{
					return;
				}

				if (ciActionType
					== LocalOptionConstant.ADD_PAYMENT_ACCOUNT)
				{
					caPymntAcctData = new PaymentAccountData();
					caPymntAcctData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					caPymntAcctData.setSubstaId(
						SystemProperty.getSubStationId());
					caPymntAcctData.setDeleteIndi(0);
				}
				caPymntAcctData.setPymntLocDesc(
					gettxtPymtLocDesc().getText());
				caPymntAcctData.setPymntLocId(
					gettxtPymtLocId().getText());

				// defect 8595 
				Vector lvVector = new Vector();
				lvVector.add(caPymntAcctData);
				lvVector.add(getAdminLogData());
				getController().processData(ciActionType, lvVector);
				// end defect 8595 

				if (ciActionType
					== LocalOptionConstant.ADD_PAYMENT_ACCOUNT)
				{
					cvData.add(caPymntAcctData);
				}
				else if (
					ciActionType
						== LocalOptionConstant.DELETE_PAYMENT_ACCOUNT)
				{
					cvData.remove(ciSelRow);
				}
				getController().processData(
					AbstractViewController.CANCEL,
					cvData);

			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					cvData);
			}
		} //end of try
		finally
		{
			doneWorking();
		}

	}

	/**
	 * Return populated AdminLogData
	 * 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData()
	{
		// defect 8628 
		AdministrationLogData laAdminlogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		String lsAction = CommonConstant.TXT_ADMIN_LOG_REVISE;
		if (ciActionType == LocalOptionConstant.ADD_PAYMENT_ACCOUNT)
		{
			lsAction = CommonConstant.TXT_ADMIN_LOG_ADD;
		}
		else if (
			ciActionType == LocalOptionConstant.DELETE_PAYMENT_ACCOUNT)
		{
			lsAction = CommonConstant.TXT_ADMIN_LOG_DELETE;
		}
		laAdminlogData.setAction(lsAction);
		laAdminlogData.setEntity(PYMNT_ACCT);
		laAdminlogData.setEntityValue(
			gettxtPymtLocId().getText()
				+ " "
				+ caPymntAcctData.getPymntLocDesc());
		return laAdminlogData;
	}

	/**
	 * Return the btnAction property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAction()
	{
		if (ivjbtnAction == null)
		{
			try
			{
				ivjbtnAction = new RTSButton();
				ivjbtnAction.setName("btnAction");
				ivjbtnAction.setMnemonic('A');
				ivjbtnAction.setText(CommonConstant.BTN_TXT_ADD);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAction;
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
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	* Return the lblAcctDesc property value.
	* 
	* @return JLabel
	*/
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblAcctDesc()
	{
		if (ivjlblAcctDesc == null)
		{
			try
			{
				ivjlblAcctDesc = new JLabel();
				ivjlblAcctDesc.setName("lblAcctDesc");
				ivjlblAcctDesc.setText(ACCOUNT_DESC);
				ivjlblAcctDesc.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjlblAcctDesc;
	}

	/**
	 * Return the lblCLocCode property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblCLocCode()
	{
		if (ivjlblCLocCode == null)
		{
			try
			{
				ivjlblCLocCode = new JLabel();
				ivjlblCLocCode.setName("lblCLocCode");
				ivjlblCLocCode.setText(COMPTROLL_LOCATION_CD);
				ivjlblCLocCode.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjlblCLocCode;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints constraintstxtPymtLocId =
					new GridBagConstraints();
				constraintstxtPymtLocId.gridx = 2;
				constraintstxtPymtLocId.gridy = 1;
				constraintstxtPymtLocId.fill =
					GridBagConstraints.HORIZONTAL;
				constraintstxtPymtLocId.weightx = 1.0;
				constraintstxtPymtLocId.ipadx = 64;
				constraintstxtPymtLocId.insets =
					new Insets(26, 5, 8, 156);
				getRTSDialogBoxContentPane().add(
					gettxtPymtLocId(),
					constraintstxtPymtLocId);

				GridBagConstraints constraintstxtPymtLocDesc =
					new GridBagConstraints();
				constraintstxtPymtLocDesc.gridx = 2;
				constraintstxtPymtLocDesc.gridy = 2;
				constraintstxtPymtLocDesc.fill =
					GridBagConstraints.HORIZONTAL;
				constraintstxtPymtLocDesc.weightx = 1.0;
				constraintstxtPymtLocDesc.ipadx = 184;
				constraintstxtPymtLocDesc.insets =
					new Insets(8, 5, 18, 36);
				getRTSDialogBoxContentPane().add(
					gettxtPymtLocDesc(),
					constraintstxtPymtLocDesc);

				GridBagConstraints constraintsbtnAction =
					new GridBagConstraints();
				constraintsbtnAction.gridx = 1;
				constraintsbtnAction.gridy = 3;
				constraintsbtnAction.ipadx = 40;
				constraintsbtnAction.insets =
					new Insets(19, 77, 45, 23);
				getRTSDialogBoxContentPane().add(
					getbtnAction(),
					constraintsbtnAction);

				GridBagConstraints constraintsbtnCancel =
					new GridBagConstraints();
				constraintsbtnCancel.gridx = 2;
				constraintsbtnCancel.gridy = 3;
				constraintsbtnCancel.ipadx = 24;
				constraintsbtnCancel.insets =
					new Insets(19, 54, 45, 78);
				getRTSDialogBoxContentPane().add(
					getbtnCancel(),
					constraintsbtnCancel);
				GridBagConstraints constraintslblCLocCode =
					new GridBagConstraints();
				constraintslblCLocCode.gridx = 1;
				constraintslblCLocCode.gridy = 1;
				constraintslblCLocCode.ipadx = 10;
				constraintslblCLocCode.insets =
					new Insets(29, 18, 11, 5);
				getRTSDialogBoxContentPane().add(
					getlblCLocCode(),
					constraintslblCLocCode);

				GridBagConstraints constraintslblAcctDesc =
					new GridBagConstraints();
				constraintslblAcctDesc.gridx = 1;
				constraintslblAcctDesc.gridy = 2;
				constraintslblAcctDesc.ipadx = 30;
				constraintslblAcctDesc.insets =
					new Insets(11, 44, 21, 5);
				getRTSDialogBoxContentPane().add(
					getlblAcctDesc(),
					constraintslblAcctDesc);
				// user code begin {1}
				getbtnAction().addActionListener(this);
				getbtnCancel().addActionListener(this);
				getbtnAction().addKeyListener(this);
				getbtnCancel().addKeyListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the RTSInputField2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPymtLocDesc()
	{
		if (ivjtxtPymtLocDesc == null)
		{
			try
			{
				ivjtxtPymtLocDesc = new RTSInputField();
				ivjtxtPymtLocDesc.setName("txtPymtLocDesc");
				ivjtxtPymtLocDesc.setMaxLength(24);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtPymtLocDesc;
	}

	/**
	 * Return the RTSInputField1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPymtLocId()
	{
		if (ivjtxtPymtLocId == null)
		{
			try
			{
				ivjtxtPymtLocId = new RTSInputField();
				ivjtxtPymtLocId.setName("txtPymtLocId");
				ivjtxtPymtLocId.setInput(1);
				ivjtxtPymtLocId.setMaxLength(5);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtPymtLocId;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */

	private void handleException(Throwable aeEx)
	{
		//defect 7891
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			// user code end
			setName("FrmPaymentAccountUpdateOPT005");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(426, 189);
			setTitle(OPT005_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getbtnAction());
		// defect 8628 
		caRTSButtonGroup.add(getbtnAction());
		caRTSButtonGroup.add(getbtnCancel());
		// end defect 8628
		// user code end 
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPaymentAccountUpdateOPT005 laFrmPaymentAccountUpdateOPT005;
			laFrmPaymentAccountUpdateOPT005 =
				new FrmPaymentAccountUpdateOPT005();
			laFrmPaymentAccountUpdateOPT005.setModal(true);
			laFrmPaymentAccountUpdateOPT005
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPaymentAccountUpdateOPT005.show();
			Insets insets = laFrmPaymentAccountUpdateOPT005.getInsets();
			laFrmPaymentAccountUpdateOPT005.setSize(
				laFrmPaymentAccountUpdateOPT005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPaymentAccountUpdateOPT005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPaymentAccountUpdateOPT005.setVisibleRTS(true);
		}
		catch (Throwable leIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leIVJEx.printStackTrace(System.out);
		}
	}
	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (cbInit)
		{
			cbInit = false;
			Vector lvData = (Vector) aaDataObject;
			ciActionType = ((Integer) lvData.get(0)).intValue();
			ciSelRow = ((Integer) lvData.get(1)).intValue();
			cvData = (Vector) lvData.get(2);

			switch (ciActionType)
			{
				case LocalOptionConstant.ADD_PAYMENT_ACCOUNT :
					{
						gettxtPymtLocId().requestFocus();
						break;
					}

				case LocalOptionConstant.REVISE_PAYMENT_ACCOUNT :
					{
						getbtnAction().setText(
							CommonConstant.BTN_TXT_REVISE);
						getbtnAction().setMnemonic('R');
						caPymntAcctData =
							(PaymentAccountData) cvData.get(ciSelRow);
						gettxtPymtLocDesc().setText(
							caPymntAcctData.getPymntLocDesc());
						gettxtPymtLocId().setText(
							caPymntAcctData.getPymntLocId());
						gettxtPymtLocId().setEnabled(false);
						gettxtPymtLocDesc().requestFocus();
						break;
					}

				case LocalOptionConstant.DELETE_PAYMENT_ACCOUNT :
					{
						getbtnAction().setText(
							CommonConstant.BTN_TXT_DELETE);
						getbtnAction().setMnemonic('D');
						caPymntAcctData =
							(PaymentAccountData) cvData.get(ciSelRow);
						gettxtPymtLocDesc().setText(
							caPymntAcctData.getPymntLocDesc());
						gettxtPymtLocId().setText(
							caPymntAcctData.getPymntLocId());
						gettxtPymtLocDesc().setEnabled(false);
						gettxtPymtLocId().setEnabled(false);
						break;
					}
			}
		}
	}

	/**
	 * Validates the user inputs and throws exceptions
	 * 
	 * @return boolean
	 */
	public boolean validateInput()
	{
		clearAllColor(this);
		RTSException leRTSEx = new RTSException();

		String lsPymtLocId = null;

		if (ciActionType == LocalOptionConstant.ADD_PAYMENT_ACCOUNT)
		{
			lsPymtLocId = gettxtPymtLocId().getText().trim();
			if (lsPymtLocId.length() != 5)
				leRTSEx.addException(
					new RTSException(2000),
					gettxtPymtLocId());
			else
			{
				String lsSubStr1 = lsPymtLocId.substring(0, 3);
				int liOfcIssNo = Integer.parseInt(lsSubStr1);
				String lsSubStr2 = lsPymtLocId.substring(3, 4);
				int liZero = Integer.parseInt(lsSubStr2);

				if (liOfcIssNo != SystemProperty.getOfficeIssuanceNo()
					|| liZero != 0)
					leRTSEx.addException(
						new RTSException(2000),
						gettxtPymtLocId());
				else
				{
					for (int i = 0; i < cvData.size(); i++)
					{
						PaymentAccountData laPAData =
							(PaymentAccountData) cvData.get(i);
						if (lsPymtLocId
							.equals(laPAData.getPymntLocId()))
						{
							leRTSEx.addException(
								new RTSException(2001),
								gettxtPymtLocId());
							break;
						}
					}
				}
			}
		}

		if (ciActionType == LocalOptionConstant.ADD_PAYMENT_ACCOUNT
			|| ciActionType == LocalOptionConstant.REVISE_PAYMENT_ACCOUNT)
		{
			lsPymtLocId = gettxtPymtLocDesc().getText().trim();
			if (lsPymtLocId.length() == 0)
			{
				leRTSEx.addException(
					new RTSException(2002),
					gettxtPymtLocDesc());
			}
			else
			{
				for (int i = 0; i < cvData.size(); i++)
				{
					PaymentAccountData lPAUData =
						(PaymentAccountData) cvData.get(i);
					if (lsPymtLocId.equals(lPAUData.getPymntLocDesc()))
					{
						leRTSEx.addException(
							new RTSException(2002),
							gettxtPymtLocDesc());
						break;
					}
				}
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		return true;
	}
}
