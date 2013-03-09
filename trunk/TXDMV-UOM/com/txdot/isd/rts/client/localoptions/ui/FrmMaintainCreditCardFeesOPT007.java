package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.CreditCardFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmMaintainCreditCardFeesOPT007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * S Govindappa 11/18/2002 	Fixing defect 4925.Changed the visual 
 *							compostion to make the cursor around 
 *							percentage field appear completly.
 * B Arredondo	03/12/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		03/08/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang		08/24/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/13/2005	Line up the add and cancel buttons.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	12/14/2005	Comment out focusLost
 * 							modify focusLost()  
 * 							defect 7891 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove focusGained(), focusLost()
 * 							modify keyPressed(), getradioFlat(), 
 * 								getradioPercentage(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	09/03/2008	Rename class variable according to standards.
 * 							  (ciSelectedRow, csType, cvFees)
 * 							Standardize population of AdminLogData
 * 							Add'l class cleanup.  
 * 							add getAdminLogData()
 * 							delete getBuilderData()
 * 							modify actionPerformed()
 * 							defect 8595 Ver Defect_POS_B 
 * K Harrell	08/22/2009	Implement new AdminLogData constructor()
 * 							Implement RTSButtonGroup
 * 							add caRTSButtonGroup 
 *							delete keyPressed(), KeyListener 		 
 * 							modify getAdminLogData(), setData() 
 * 							defect 8628 Ver Defect_POS_F   
 * ---------------------------------------------------------------------
 */

/**
 * This screen is where you would enter the amount or percentage of the
 * credit card fee.
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	Michael Abernethy
 * <br>Creation Date:		04/25/2002 10:19:17  
 */

public class FrmMaintainCreditCardFeesOPT007
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblCreditFee = null;
	private JLabel ivjstcLblEffDate = null;
	private JLabel ivjstcLblEndDate = null;
	private RTSDateField ivjtxtEffDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private RTSInputField ivjtxtFee = null;
	private JRadioButton ivjradioFlat = null;
	private JRadioButton ivjradioPercentage = null;

	//	defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628 

	// int
	private int ciSelectedRow;

	// String 
	private String csType;

	// Vector 
	private Vector cvFees;

	// Constants
	private final static String CRDTCARDFEE = "CrdtCardFee";
	private final static String CREDIT_CARD_FEE = "Credit Card Fee:";
	private final static String EFFECTIVE_DATE = "Effective Date:";
	private final static String END_DATE = "End Date:";
	private final static String FLAT_FEE = "Flat Fee";
	private final static String OPT007_FRAME_TITLE =
		"Maintain Credit Card Fees   OPT007";
	private final static String PERCENTAGE = "Percentage";

	/**
	 * FrmMaintainCreditCardFeesOPT007 constructor comment.
	 */
	public FrmMaintainCreditCardFeesOPT007()
	{
		super();
		initialize();
	}

	/**
	 * FrmMaintainCreditCardFeesOPT007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmMaintainCreditCardFeesOPT007(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmMaintainCreditCardFeesOPT007 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmMaintainCreditCardFeesOPT007(Frame aaOwner)
	{
		super(aaOwner);
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

			// getbtnAdd() also serves for REVISE & DELETE! 
			if (aaAE.getSource() == getbtnAdd())
			{

				// VALIDATION
				RTSException leRTSEx = new RTSException();

				Dollar laFee =
					new Dollar(CommonConstant.STR_ZERO_DOLLAR);
				RTSDate laEffDate = new RTSDate();
				RTSDate laEndDate = new RTSDate();

				if (gettxtFee()
					.getText()
					.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtFee());
				}
				else
				{
					double ldFee2 =
						Double.parseDouble(gettxtFee().getText());
					laFee = new Dollar(ldFee2);
					if (ldFee2 < 0.0)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtFee());
					}
					if (ldFee2 > CreditCardFeesConstants.MAX_FEE
						&& getradioFlat().isSelected())
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtFee());
					}
					if (ldFee2 > CreditCardFeesConstants.MAX_PERCENTAGE
						&& getradioPercentage().isSelected())
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtFee());
					}
				}

				if (!gettxtEffDate().isValidDate())
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtEffDate());
				}
				else
				{
					laEffDate = gettxtEffDate().getDate();
				}

				if (!gettxtEndDate().isValidDate())
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtEndDate());
				}
				else
				{
					laEndDate = gettxtEndDate().getDate();
				}

				if (gettxtEffDate().isValidDate())
				{
					if (laEffDate
						.compareTo(
							RTSDate.getCurrentDate().add(
								RTSDate.DATE,
								-1))
						< 0
						&& gettxtEffDate().isEnabled())
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtEffDate());
					}
				}

				if (gettxtEffDate().isValidDate()
					&& gettxtEndDate().isValidDate())
				{
					if (laEffDate.compareTo(laEndDate) > 0)
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtEndDate());
				}

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				// END VALIDATION
				
				// get data from the screen
				CreditCardFeeData laCreditData =
					new CreditCardFeeData();
				if (!csType.equals(CreditCardFeesConstants.ADD))
					laCreditData =
						(CreditCardFeeData) cvFees.get(ciSelectedRow);
				laCreditData.setItmPrice(laFee);
				laCreditData.setRTSEffDate(laEffDate);
				laCreditData.setRTSEffEndDate(laEndDate);
				laCreditData.setPercentage(
					getradioPercentage().isSelected());
				laCreditData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laCreditData.setSubstaId(
					SystemProperty.getSubStationId());
				if (csType.equals(CreditCardFeesConstants.ADD))
				{
					cvFees.add(laCreditData);
				}
				else if (csType.equals(CreditCardFeesConstants.REVISE))
				{
					cvFees.set(ciSelectedRow, laCreditData);
				}
				else if (csType.equals(CreditCardFeesConstants.DELETE))
				{
					cvFees.remove(ciSelectedRow);
				}

				// prepare the data to go to the server side
				Map lhmMap = new HashMap();
				lhmMap.put(CreditCardFeesConstants.DATA, cvFees);
				lhmMap.put(
					CreditCardFeesConstants.SELECTED_RECORD,
					laCreditData);
				lhmMap.put(CreditCardFeesConstants.TYPE, csType);
				
				// defect 8595 
				// Add AdminLog Info 
				lhmMap.put(
					CreditCardFeesConstants.ADMIN,
					getAdminLogData());
				// end defect 8595 

				getController().processData(
					AbstractViewController.ENTER,
					lhmMap);
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					cvFees);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnAdd property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic('a');
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				// user code begin {1}
				ivjbtnAdd.addActionListener(this);
				ivjbtnAdd.addKeyListener(this);
				getRootPane().setDefaultButton(ivjbtnAdd);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAdd;
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

		laAdminlogData.setAction(csType);
		laAdminlogData.setEntity(CRDTCARDFEE);
		String lsAppend =
			getradioPercentage().isSelected() ? " %" : " $";
		laAdminlogData.setEntityValue(
			gettxtEffDate().getDate().getMMDDYY()
				+ CommonConstant.STR_DASH
				+ gettxtEndDate().getDate().getMMDDYY()
				+ CommonConstant.STR_SPACE_ONE
				+ gettxtFee().getText()
				+ lsAppend);
		return laAdminlogData;
	}

	/**
	 * Return the radioFlat property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioFlat()
	{
		if (ivjradioFlat == null)
		{
			try
			{
				ivjradioFlat = new JRadioButton();
				ivjradioFlat.setName("radioFlat");
				ivjradioFlat.setSelected(false);
				ivjradioFlat.setMnemonic(KeyEvent.VK_F);
				ivjradioFlat.setText(FLAT_FEE);
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
		return ivjradioFlat;
	}

	/**
	 * Return the radioPercentage property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioPercentage()
	{
		if (ivjradioPercentage == null)
		{
			try
			{
				ivjradioPercentage = new JRadioButton();
				ivjradioPercentage.setName("radioPercentage");
				ivjradioPercentage.setSelected(true);
				ivjradioPercentage.setMnemonic('p');
				ivjradioPercentage.setText(PERCENTAGE);
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
		return ivjradioPercentage;
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
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints4 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints5 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints6 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints8 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints7 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints9 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints10 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints5.insets =
					new java.awt.Insets(12, 2, 12, 88);
				consGridBagConstraints5.ipadx = -12;
				consGridBagConstraints5.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints5.weightx = 1.0;
				consGridBagConstraints5.gridwidth = 2;
				consGridBagConstraints5.gridy = 2;
				consGridBagConstraints5.gridx = 1;
				consGridBagConstraints1.insets =
					new java.awt.Insets(8, 30, 14, 24);
				consGridBagConstraints1.ipadx = 6;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				consGridBagConstraints4.insets =
					new java.awt.Insets(6, 2, 12, 88);
				consGridBagConstraints4.ipadx = 98;
				consGridBagConstraints4.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints4.weightx = 1.0;
				consGridBagConstraints4.gridwidth = 2;
				consGridBagConstraints4.gridy = 0;
				consGridBagConstraints4.gridx = 1;
				consGridBagConstraints3.insets =
					new java.awt.Insets(12, 30, 17, 24);
				consGridBagConstraints3.ipadx = 43;
				consGridBagConstraints3.gridy = 3;
				consGridBagConstraints3.gridx = 0;
				consGridBagConstraints7.insets =
					new java.awt.Insets(12, 49, 10, 1);
				consGridBagConstraints7.ipadx = 44;
				consGridBagConstraints7.gridy = 4;
				consGridBagConstraints7.gridx = 0;
				consGridBagConstraints2.insets =
					new java.awt.Insets(14, 30, 14, 24);
				consGridBagConstraints2.ipadx = 15;
				consGridBagConstraints2.gridy = 2;
				consGridBagConstraints2.gridx = 0;
				consGridBagConstraints6.insets =
					new java.awt.Insets(13, 2, 12, 88);
				consGridBagConstraints6.ipadx = -12;
				consGridBagConstraints6.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints6.weightx = 1.0;
				consGridBagConstraints6.gridwidth = 2;
				consGridBagConstraints6.gridy = 3;
				consGridBagConstraints6.gridx = 1;
				consGridBagConstraints9.insets =
					new java.awt.Insets(12, 69, 12, 11);
				consGridBagConstraints9.ipadx = 24;
				consGridBagConstraints9.gridwidth = 2;
				consGridBagConstraints9.gridy = 1;
				consGridBagConstraints9.gridx = 0;
				consGridBagConstraints8.insets =
					new java.awt.Insets(12, 27, 10, 50);
				consGridBagConstraints8.ipadx = 20;
				consGridBagConstraints8.gridy = 4;
				consGridBagConstraints8.gridx = 2;
				consGridBagConstraints10.insets =
					new java.awt.Insets(12, 11, 12, 45);
				consGridBagConstraints10.ipadx = 23;
				consGridBagConstraints10.gridy = 1;
				consGridBagConstraints10.gridx = 2;
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new java.awt.GridBagLayout());

				ivjRTSDialogBoxContentPane.add(
					getstcLblCreditFee(),
					consGridBagConstraints1);
				ivjRTSDialogBoxContentPane.add(
					getstcLblEffDate(),
					consGridBagConstraints2);
				ivjRTSDialogBoxContentPane.add(
					getstcLblEndDate(),
					consGridBagConstraints3);
				ivjRTSDialogBoxContentPane.add(
					gettxtFee(),
					consGridBagConstraints4);
				ivjRTSDialogBoxContentPane.add(
					gettxtEffDate(),
					consGridBagConstraints5);
				ivjRTSDialogBoxContentPane.add(
					gettxtEndDate(),
					consGridBagConstraints6);
				ivjRTSDialogBoxContentPane.add(
					getbtnAdd(),
					consGridBagConstraints7);
				ivjRTSDialogBoxContentPane.add(
					getbtnCancel(),
					consGridBagConstraints8);
				ivjRTSDialogBoxContentPane.add(
					getradioFlat(),
					consGridBagConstraints9);
				ivjRTSDialogBoxContentPane.add(
					getradioPercentage(),
					consGridBagConstraints10);
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
	 * Return the stcLblCreditFee property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCreditFee()
	{
		if (ivjstcLblCreditFee == null)
		{
			try
			{
				ivjstcLblCreditFee = new JLabel();
				ivjstcLblCreditFee.setName("stcLblCreditFee");
				ivjstcLblCreditFee.setText(CREDIT_CARD_FEE);
				ivjstcLblCreditFee.setHorizontalAlignment(
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
		return ivjstcLblCreditFee;
	}

	/**
	 * Return the stcLblEffDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEffDate()
	{
		if (ivjstcLblEffDate == null)
		{
			try
			{
				ivjstcLblEffDate = new JLabel();
				ivjstcLblEffDate.setName("stcLblEffDate");
				ivjstcLblEffDate.setText(EFFECTIVE_DATE);
				ivjstcLblEffDate.setHorizontalAlignment(
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
		return ivjstcLblEffDate;
	}

	/**
	 * Return the stcLblEndDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			try
			{
				ivjstcLblEndDate = new JLabel();
				ivjstcLblEndDate.setName("stcLblEndDate");
				ivjstcLblEndDate.setText(END_DATE);
				ivjstcLblEndDate.setHorizontalAlignment(
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
		return ivjstcLblEndDate;
	}

	/**
	 * Return the txtEffDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtEffDate()
	{
		if (ivjtxtEffDate == null)
		{
			try
			{
				ivjtxtEffDate = new RTSDateField();
				ivjtxtEffDate.setName("txtEffDate");
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
		return ivjtxtEffDate;
	}

	/**
	 * Return the txtEndDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setName("txtEndDate");
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
		return ivjtxtEndDate;
	}
	/**
	 * Return the txtFee property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtFee()
	{
		if (ivjtxtFee == null)
		{
			try
			{
				ivjtxtFee = new RTSInputField();
				ivjtxtFee.setName("txtFee");
				ivjtxtFee.setInput(5);
				ivjtxtFee.setMaxLength(4);
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
		return ivjtxtFee;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
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
			setName("FrmMaintainCreditCardFeesOPT007");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(350, 250);
			setTitle(OPT007_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// defect 7891
		// Changed from ButtonGroup to RTSButtonGroup
		RTSButtonGroup laBG = new RTSButtonGroup();
		laBG.add(getradioFlat());
		laBG.add(getradioPercentage());
		// end defect 7891
		// defect 8628 
		caRTSButtonGroup.add(getbtnAdd());
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
			FrmMaintainCreditCardFeesOPT007 laFrmMaintainCreditCardFeesOPT007;
			laFrmMaintainCreditCardFeesOPT007 =
				new FrmMaintainCreditCardFeesOPT007();
			laFrmMaintainCreditCardFeesOPT007.setModal(true);
			laFrmMaintainCreditCardFeesOPT007
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmMaintainCreditCardFeesOPT007.show();
			Insets insets =
				laFrmMaintainCreditCardFeesOPT007.getInsets();
			laFrmMaintainCreditCardFeesOPT007.setSize(
				laFrmMaintainCreditCardFeesOPT007.getWidth()
					+ insets.left
					+ insets.right,
				laFrmMaintainCreditCardFeesOPT007.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmMaintainCreditCardFeesOPT007.setVisibleRTS(true);
		}
		catch (Throwable leThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		Map laMap = (Map) aaDataObject;
		cvFees = (Vector) laMap.get(CreditCardFeesConstants.DATA);
		csType = (String) laMap.get(CreditCardFeesConstants.TYPE);
		ciSelectedRow =
			((Integer) laMap.get(CreditCardFeesConstants.SELECTED_ROW))
				.intValue();

		// Set Button Text 
		getbtnAdd().setText(csType);

		// Set Button Mnemonic 
		char lchMnemonic = csType.charAt(0);
		getbtnAdd().setMnemonic(lchMnemonic);

		// ADD 
		if (csType.equals(CreditCardFeesConstants.ADD))
		{
			if (cvFees.size() > 0)
			{
				CreditCardFeeData laLastRecord =
					(CreditCardFeeData) cvFees.get(cvFees.size() - 1);
					
				gettxtEffDate().setDate(
					laLastRecord.getRTSEffEndDate().add(
						RTSDate.DATE,
						1));
			}
			else if (cvFees.size() == 0)
			{
				gettxtEffDate().setDate(RTSDate.getCurrentDate());
			}
			gettxtEffDate().setEnabled(false);
			getradioPercentage().setSelected(true);
		}
		// defect 8628  
		// REVISE / DELETE  
		else
		{
			// Common Frame Initialization 
			CreditCardFeeData laSelectedRecord =
				(CreditCardFeeData) cvFees.get(ciSelectedRow);
			gettxtFee().setText(
				laSelectedRecord.getItmPrice().toString());
			gettxtEffDate().setDate(laSelectedRecord.getRTSEffDate());
			gettxtEndDate().setDate(
				laSelectedRecord.getRTSEffEndDate());
			gettxtEffDate().setEnabled(false);
			getradioFlat().setSelected(
				!laSelectedRecord.isPercentage());
			getradioPercentage().setSelected(
				laSelectedRecord.isPercentage());

			// REVISE  
			if (csType.equals(CreditCardFeesConstants.REVISE))
			{
				gettxtEndDate().setEnabled(
					(ciSelectedRow == cvFees.size() - 1));
			}
			// DELETE 
			else if (csType.equals(CreditCardFeesConstants.DELETE))
			{
				gettxtFee().setEnabled(false);
				gettxtEndDate().setEnabled(false);
				getradioFlat().setEnabled(false);
				getradioPercentage().setEnabled(false);
			}
		}
		// end defect 8628
	}
}
