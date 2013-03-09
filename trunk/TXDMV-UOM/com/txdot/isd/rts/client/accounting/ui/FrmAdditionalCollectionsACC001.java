package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmAdditionalCollectionsACC001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			06/11/2001	Created
 * MAbs			09/07/2001	Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			04/29/2002 	Enlarged Qty field to show 4 digits 
 * 							defect 3693
 * MAbs			05/29/2002	Allowed input for dollars to be 99999.99 
 * 							defect 4137
 * MAbs			06/11/2002	No Negative numbers
 * 							defect 4267
 * M Listberger	11/01/2002	Added code to update table when ALT+R is 
 * 							pressed.
 *        					defect 4971  
 * 							Added code to not update the table when 
 * 							doing a shift+tab from Total Price field to 
 * 							Qty field.
 *                          defect 4837  
 * 							Modified GUI form and focusGained method.
 * S Govindappa 02/27/2003  Fixing the shift tab order by removing the
 *                          nextFocusable calls in the java class.
 * 							defect 5378 
 * S Govindappa 03/27/2003 	Changed shouldContinueAfterValidation(..) to 
 * 							change the validation message when the price
 * 							is above 99,999.99.
 * 							defect 5375
 * K Harrell	12/29/2003	Reset quantity field after error when select 
 * 							new fee item with mouse. 
 *							modify focusGained(). 
 *							add csSavedFeeItem 
 *							defect 6532 Ver 5.1.5 Fix 2  
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/21/2005	Java 1.4 Work 
 * 							defect 7884 Ver 5.2.3
 * B Hargrove	08/09/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel() 
 * 							defect 8240 Ver 5.2.3 
 * Jeff S.		12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateDropDown()
 * 							defect 8479 Ver 5.2.3  
 * B Hargrove 	05/05/2008 	Modify error message for Price entry.
 * 							modify ERRMSG_PRICE_RANGE
 * 							defect 9108 Ver Defect POS A
 * J Zwiener	07/26/2010	Force tabbing to "invisible" button before
 * 							vertical scroll bar on 10th entry.  Changes
 * 							made via Visual Editor.
 * 							Gridbaglayout modified to null on 
 * 							JInternalFrameContentPane and JPanel1 
 * 							modify getbtnValidation()
 * 							defect 10199 Ver POS_650
 * Min Wang		09/24/2010 	Accommodate new max lengh of input field for 
 * 							total price.
 * 							add MAX_PRICE
 * 							modify gettxtTotalPrice(), 
 * 							shouldContinueAfterValidation()
 * 							defect 10596 Ver 6.6.0
 * Min Wang		09/29/2010	09/28/2010	
 * 							add MAX_LENGTH, 
 * 							modify ERRMSG_PRICE_RANGE, 
 * 							shouldContinueAfterValidation()
 * 							defect 10596 Ver 6.6.0
 * K Harrell	02/03/2011	Validate Multiple of 'Base Fee' 
 * 							modify getbtnValidation(),
 * 							  shouldContinueAfterValidation() 
 * 							defect 10741 Ver 6.7.0 
 * K Harrell	02/10/2011	Fixed alignment problem with data entry 
 * 							fields in Visual Editor 
 * 							defect 10741 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * ACC001 handles the Additional Collections/Time Lag functionality 
 * found in the accounting module.
 * 
 * @version	6.7.0			02/10/2011
 * @author	Michael Abernethy
 * <br>Creation Date:		06/11/2001 
 *
 **/
public class FrmAdditionalCollectionsACC001
	extends RTSDialogBox
	implements ActionListener, FocusListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private Component caPreviousComponent;
	private JComboBox ivjcomboSelectFee = null;
	private JLabel ivjlblCountyName = null;
	private JLabel ivjlblCountyNo = null;
	private JLabel ivjlblTotal = null;
	private JLabel ivjstcLblCountyName = null;
	private JLabel ivjstcLblCountyNo = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblReason = null;
	private JLabel ivjstcLblSelectFee = null;
	private JLabel ivjstcLblTotal = null;
	private JLabel ivjstcLblTotalPrice = null;
	private JLabel ivjstcLabelDollar = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnValidation = null;
	private RTSInputField ivjtxtQuantity = null;
	private RTSInputField ivjtxtReason = null;
	private RTSInputField ivjtxtTotalPrice = null;
	private RTSTable ivjtblFees = null;
	private TMACC001 caTableModel;

	// boolean 
	private boolean cbDoClick = true;

	// String 
	private String csSavedFeeItem = "";

	// Vector 
	private Vector cvData;
	private Vector cvTableData;

	// Constants
	private static final String COUNTY_NAME = "County Name:";
	private static final String COUNTY_NO = "County No:";
	private static final String DEFLT_COUNTY_NAME =
		"MONTGOMERYCOUNTYNAME";
	private static final String DEFLT_COUNTY_NO = "161";
	private static final String DOLLAR_SIGN = "$";
	private static final String EMPTY_STRING = "";
	private static final String ERRMSG_ERROR = "ERROR!";
	// defect 10596
	// defect 9108
	private static final String ERRMSG_PRICE_RANGE =
		//	"The price range should be within -99,999.99 and 99,999.99";
		//	"The price range should be within 0.00 and 99,999.99";
	"The price range should be within 0.00 and 99,999,999.99";
	// end defect 9108	
	// end defect 10596
	private static final String FEE_DESCR = "Select Fee Description";
	private static final String INIT_TITLE =
		"Additional Collections/Time Lag   ACC001";
	private static final String ONE = "1";
	private static final String QTY = "Qty:";
	private static final String REASON = "Reason:";
	private static final String TOTAL_$ = "Total:    $";
	private static final String TOTAL_PRICE = "Total Price:";
	private static final String ZERO_DOLLAR = "0.00";
	// defect 10596
	private static final double MAX_PRICE = 999999999.99;
	private static final int MAX_LENGTH = 11;
	// end defect 10596

	/**
	 * Creates a FrmAdditionalCollections.
	 */
	public FrmAdditionalCollectionsACC001()
	{
		super();
		initialize();
	}
	/**
	 * Creates an ACC001 with a parent.
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmAdditionalCollectionsACC001(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates an ACC001 with a parent.
	 * 
	 * @param aaParent	JFrame
	 */
	public FrmAdditionalCollectionsACC001(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * If Enter is pressed, this function validates the reason textfield
	 * and also checks to ensure at least one entry was created in the
	 * table.
	 * 
	 * @param  aaAE	ActionEvent  
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
			if (aaAE.getSource() == getbtnValidation())
			{
				shouldContinueAfterValidation();
			}
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leRTSEx = new RTSException();
				if (!alreadyContainsEntry())
				{
					if (!shouldContinueAfterValidation())
					{
						return;
					}
				}
				if (getReason().equals(""))
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtReason());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					cbDoClick = false;
					return;
				}
				if (cvTableData.size() == 0)
				{
					leRTSEx.addException(
						new RTSException(140),
						getcomboSelectFee());
				}
				CompleteTransactionData laCTData =
					new CompleteTransactionData();
				RegFeesData laRegFeesData = new RegFeesData();
				laRegFeesData.setReason(getReason());
				Vector lvVector = new Vector();
				for (int i = 0; i < cvTableData.size(); i++)
				{
					AdditionalCollectionsTableData laAddlCollectionsTblData =
						(
							AdditionalCollectionsTableData) cvTableData
								.get(
							i);
					FeesData laFeesData = new FeesData();
					laFeesData.setAcctItmCd(
						laAddlCollectionsTblData.getFeeCd());
					laFeesData.setCrdtAllowedIndi(
						laAddlCollectionsTblData.getCreditIndi());
					laFeesData.setDesc(
						laAddlCollectionsTblData.getFee());
					laFeesData.setItemPrice(
						laAddlCollectionsTblData.getTotal());
					laFeesData.setItmQty(
						laAddlCollectionsTblData.getQty());
					lvVector.add(laFeesData);
				}
				laRegFeesData.setVectFees(lvVector);
				laCTData.setRegFeesData(laRegFeesData);
				laCTData.setTransCode(getController().getTransCode());
				getController().processData(
					AbstractViewController.ENTER,
					laCTData);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					cvData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC001);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Checks to see if the entry is already in the table.  This is used
	 * by actionPerformed() so that the same entry isn't put into the 
	 * table twice when Enter is pressed.
	 * 
	 * @return boolean
	 */
	private boolean alreadyContainsEntry()
	{
		String lsSelectedItem =
			(String) getcomboSelectFee().getSelectedItem();
		for (int i = 0; i < cvTableData.size(); i++)
		{
			AdditionalCollectionsTableData laAddlCollectionsTblData =
				(AdditionalCollectionsTableData) cvTableData.get(i);
			if (laAddlCollectionsTblData
				.getFee()
				.equals(lsSelectedItem))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * When a component gets focus
	 * 
	 * @param aaFE	FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		if (aaFE.isTemporary())
		{
			return;
		}
		if (aaFE.getSource() == getbtnValidation())
		{
			if (caPreviousComponent == gettxtTotalPrice())
			{
				getbtnValidation().doClick();
			}
			else
			{
				getcomboSelectFee().requestFocus();
			}
		}
		if (aaFE.getSource() == gettxtTotalPrice())
		{
			caPreviousComponent = gettxtTotalPrice();
		}
		if (aaFE.getSource() == getcomboSelectFee())
		{
			caPreviousComponent = getcomboSelectFee();
		}
		// defect 4971
		// If the reason box is getting focus from Total field
		// using Alt+R we need to process the update.
		// if (e.getSource() == gettxtReason())
		// 		previousComponent = gettxtReason();
		if (aaFE.getSource() == gettxtReason())
		{
			if (caPreviousComponent == gettxtTotalPrice() && cbDoClick)
			{
				getbtnValidation().doClick();
			}
			else
			{
				cbDoClick = true;
			}
			caPreviousComponent = gettxtReason();
		}
		// defect 6532  
		// Reset fields if have altered selection.
		if (!csSavedFeeItem
			.equals(getcomboSelectFee().getSelectedItem()))
		{
			gettxtQuantity().setText(ONE);
			gettxtTotalPrice().setText(EMPTY_STRING);
		}
		// end defect 6523
		if (aaFE.getSource() == gettxtQuantity())
		{
			// defect 4837
			// original code did not have this if statement
			// This code allows cursor movement to the qty box
			// from the total price box with out updating the
			// numbers in those fields.  
			if (caPreviousComponent != gettxtTotalPrice())
			{
				int liRowSelected = cvTableData.size() - 1;
				gettxtQuantity().setText(ONE);
				gettxtTotalPrice().setText(EMPTY_STRING);
				for (int i = 0; i < cvTableData.size(); i++)
				{
					AdditionalCollectionsTableData laAddlCollectionsTblData =
						(
							AdditionalCollectionsTableData) cvTableData
								.get(
							i);
					if (laAddlCollectionsTblData
						.getFee()
						.equals(getcomboSelectFee().getSelectedItem()))
					{
						liRowSelected = i;
						gettxtQuantity().setText(
							Integer.toString(
								laAddlCollectionsTblData.getQty()));
						gettxtTotalPrice().setText(
							laAddlCollectionsTblData
								.getTotal()
								.toString());
						break;
					}
				}
				if (liRowSelected >= 0)
				{
					gettblFees().setRowSelectionInterval(
						liRowSelected,
						liRowSelected);
				}
				if (gettblFees().getSelectedRow() == 0)
				{
					gettblFees().scrollRectToVisible(
						new Rectangle(0, 0, 1, 1));
				}
				else
				{
					gettblFees().scrollRectToVisible(
						new Rectangle(
							0,
							gettblFees().getRowHeight()
								* (gettblFees().getSelectedRow() + 1),
							1,
							1));
				}
			}
			caPreviousComponent = gettxtQuantity();
		}
		// defect 6532
		csSavedFeeItem = (String) getcomboSelectFee().getSelectedItem();
		// end defect 6532
	}
	/**
	 * Used for validation on Qty field, and the Total Price field.
	 * 
	 * @param aaFE	FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
	}
	/**
	 * Return the btnValidation property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnValidation()
	{
		if (ivjbtnValidation == null)
		{
			try
			{
				ivjbtnValidation =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnValidation.setBounds(554, 129, 12, 16);
				ivjbtnValidation.setName("btnValidation");
				ivjbtnValidation.setBorderPainted(false);
				ivjbtnValidation.setText(EMPTY_STRING);
				// defect 10741
				// ivjbtnValidation.setVisible(true);
				ivjbtnValidation.setVisible(false);
				// end defect 10741
				ivjbtnValidation.setFocusPainted(false);
				// user code begin {1}
				ivjbtnValidation.addActionListener(this);
				ivjbtnValidation.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnValidation;
	}
	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setBounds(40, 373, 513, 32);
				ivjbuttonPanel.setName("buttonPanel");
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// defect 8240 
				// ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				// ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				// ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				// end defect 8240 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the JComboBox1 property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getcomboSelectFee()
	{
		if (ivjcomboSelectFee == null)
		{
			try
			{
				ivjcomboSelectFee = new javax.swing.JComboBox();
				ivjcomboSelectFee.setBounds(31, 106, 267, 23);
				ivjcomboSelectFee.setName("comboSelectFee");
				ivjcomboSelectFee.setPreferredSize(
					new java.awt.Dimension(200, 23));
				ivjcomboSelectFee.setMaximumSize(
					new java.awt.Dimension(200, 23));
				ivjcomboSelectFee.setMinimumSize(
					new java.awt.Dimension(200, 23));
				// user code begin {1}
				ivjcomboSelectFee.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboSelectFee;
	}
	/**
	 * Return the JInternalFrameContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJInternalFrameContentPane()
	{
		if (ivjJInternalFrameContentPane == null)
		{
			try
			{
				ivjJInternalFrameContentPane = new javax.swing.JPanel();
				ivjJInternalFrameContentPane.setName(
					"JInternalFrameContentPane");
				ivjJInternalFrameContentPane.setLayout(null);
				ivjJInternalFrameContentPane.add(getJPanel1(), null);
				ivjJInternalFrameContentPane.add(
					getJScrollPane1(),
					null);
				ivjJInternalFrameContentPane.add(gettxtReason(), null);
				ivjJInternalFrameContentPane.add(
					getbuttonPanel(),
					null);
				ivjJInternalFrameContentPane.add(getlblTotal(), null);
				ivjJInternalFrameContentPane.add(
					getstcLblTotal(),
					null);
				ivjJInternalFrameContentPane.add(
					getstcLblReason(),
					null);
				ivjJInternalFrameContentPane.add(
					getbtnValidation(),
					null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJInternalFrameContentPane;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getstcLblQty(), null);
				ivjJPanel1.add(getstcLblTotalPrice(), null);
				ivjJPanel1.add(getstcLabelDollar(), null);
				ivjJPanel1.add(getstcLblSelectFee(), null);
				ivjJPanel1.add(getlblCountyNo(), null);
				ivjJPanel1.add(getstcLblCountyNo(), null);
				ivjJPanel1.add(getlblCountyName(), null);
				ivjJPanel1.add(getstcLblCountyName(), null);
				ivjJPanel1.add(getcomboSelectFee(), null);
				ivjJPanel1.add(gettxtQuantity(), null);
				ivjJPanel1.add(gettxtTotalPrice(), null);
				ivjJPanel1.setBounds(39, 9, 513, 134);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setEnabled(false);
				getJScrollPane1().setViewportView(gettblFees());
				// user code begin {1}
				ivjJScrollPane1.setBounds(39, 146, 513, 159);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblCountyName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCountyName()
	{
		if (ivjlblCountyName == null)
		{
			try
			{
				ivjlblCountyName = new javax.swing.JLabel();
				ivjlblCountyName.setBounds(140, 18, 179, 16);
				ivjlblCountyName.setName("lblCountyName");
				ivjlblCountyName.setPreferredSize(
					new java.awt.Dimension(156, 14));
				ivjlblCountyName.setText(DEFLT_COUNTY_NAME);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCountyName;
	}
	/**
	 * Return the lblCountyNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCountyNo()
	{
		if (ivjlblCountyNo == null)
		{
			try
			{
				ivjlblCountyNo = new javax.swing.JLabel();
				ivjlblCountyNo.setBounds(140, 52, 147, 16);
				ivjlblCountyNo.setName("lblCountyNo");
				ivjlblCountyNo.setText(DEFLT_COUNTY_NO);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCountyNo;
	}
	/**
	 * Return the lblTotal property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblTotal()
	{
		if (ivjlblTotal == null)
		{
			try
			{
				ivjlblTotal = new javax.swing.JLabel();
				ivjlblTotal.setBounds(450, 315, 99, 16);
				ivjlblTotal.setName("lblTotal");
				ivjlblTotal.setText(ZERO_DOLLAR);
				ivjlblTotal.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblTotal;
	}
	/**
	 * Returns the text in the quantity text field.
	 * 
	 * @return String
	 */
	private String getQuantity()
	{
		return gettxtQuantity().getText();
	}
	/**
	 * Returns the text in the reason text field.
	 * 
	 * @return String 
	 */
	private String getReason()
	{
		return gettxtReason().getText();
	}
	/**
	 * Return the stcLabel$ property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLabelDollar()
	{
		if (ivjstcLabelDollar == null)
		{
			try
			{
				ivjstcLabelDollar = new javax.swing.JLabel();
				ivjstcLabelDollar.setSize(13, 20);
				ivjstcLabelDollar.setName("stcLabelDollar");
				ivjstcLabelDollar.setText(DOLLAR_SIGN);
				// user code begin {1}
				ivjstcLabelDollar.setLocation(404, 108);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLabelDollar;
	}
	/**
	 * Return the stcLblCountyName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCountyName()
	{
		if (ivjstcLblCountyName == null)
		{
			try
			{
				ivjstcLblCountyName = new javax.swing.JLabel();
				ivjstcLblCountyName.setBounds(46, 18, 78, 16);
				ivjstcLblCountyName.setName("stcLblCountyName");
				ivjstcLblCountyName.setText(COUNTY_NAME);
				ivjstcLblCountyName.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyName;
	}
	/**
	 * Return the stcLblCountyNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCountyNo()
	{
		if (ivjstcLblCountyNo == null)
		{
			try
			{
				ivjstcLblCountyNo = new javax.swing.JLabel();
				ivjstcLblCountyNo.setBounds(45, 52, 79, 16);
				ivjstcLblCountyNo.setName("stcLblCountyNo");
				ivjstcLblCountyNo.setText(COUNTY_NO);
				ivjstcLblCountyNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyNo;
	}
	/**
	 * Return the stcLblQty property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblQty()
	{
		if (ivjstcLblQty == null)
		{
			try
			{
				ivjstcLblQty = new javax.swing.JLabel();
				ivjstcLblQty.setBounds(362, 79, 32, 16);
				ivjstcLblQty.setName("stcLblQty");
				ivjstcLblQty.setText(QTY);
				ivjstcLblQty.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblQty;
	}
	/**
	 * Return the stcLblReason property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblReason()
	{
		if (ivjstcLblReason == null)
		{
			try
			{
				ivjstcLblReason = new javax.swing.JLabel();
				ivjstcLblReason.setName("stcLblReason");
				ivjstcLblReason.setLabelFor(gettxtReason());
				ivjstcLblReason.setBounds(68, 350, 53, 16);
				ivjstcLblReason.setDisplayedMnemonic('R');
				ivjstcLblReason.setText(REASON);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblReason;
	}
	/**
	 * Return the stcLblSelectFee property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblSelectFee()
	{
		if (ivjstcLblSelectFee == null)
		{
			try
			{
				ivjstcLblSelectFee = new javax.swing.JLabel();
				ivjstcLblSelectFee.setBounds(31, 79, 134, 16);
				ivjstcLblSelectFee.setName("stcLblSelectFee");
				ivjstcLblSelectFee.setText(FEE_DESCR);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSelectFee;
	}
	/**
	 * Return the stcLblTotal property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotal()
	{
		if (ivjstcLblTotal == null)
		{
			try
			{
				ivjstcLblTotal = new javax.swing.JLabel();
				ivjstcLblTotal.setBounds(375, 315, 67, 16);
				ivjstcLblTotal.setName("stcLblTotal");
				ivjstcLblTotal.setText(TOTAL_$);
				ivjstcLblTotal.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTotal;
	}
	/**
	 * Return the stcLblTotalPrice property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotalPrice()
	{
		if (ivjstcLblTotalPrice == null)
		{
			try
			{
				ivjstcLblTotalPrice = new javax.swing.JLabel();
				ivjstcLblTotalPrice.setBounds(435, 79, 67, 16);
				ivjstcLblTotalPrice.setName("stcLblTotalPrice");
				ivjstcLblTotalPrice.setText(TOTAL_PRICE);
				ivjstcLblTotalPrice.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTotalPrice;
	}
	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblFees()
	{
		if (ivjtblFees == null)
		{
			try
			{
				ivjtblFees = new RTSTable();
				ivjtblFees.setName("tblFees");
				getJScrollPane1().setColumnHeaderView(
					ivjtblFees.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblFees.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblFees.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC001());
				ivjtblFees.setShowVerticalLines(false);
				ivjtblFees.setShowHorizontalLines(false);
				ivjtblFees.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblFees.setBounds(0, 0, 200, 200);
				ivjtblFees.setEnabled(false);
				// user code begin {1}
				caTableModel = (TMACC001) ivjtblFees.getModel();
				TableColumn laTableColumnA =
					ivjtblFees.getColumn(ivjtblFees.getColumnName(0));
				laTableColumnA.setPreferredWidth(300);
				TableColumn laTableColumnB =
					ivjtblFees.getColumn(ivjtblFees.getColumnName(1));
				laTableColumnB.setPreferredWidth(108);
				TableColumn laTableColumnC =
					ivjtblFees.getColumn(ivjtblFees.getColumnName(2));
				laTableColumnC.setPreferredWidth(108);
				ivjtblFees.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblFees.init();
				laTableColumnA.setCellRenderer(
					ivjtblFees.setColumnAlignment(RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjtblFees.setColumnAlignment(RTSTable.CENTER));
				laTableColumnC.setCellRenderer(
					ivjtblFees.setColumnAlignment(RTSTable.RIGHT));
				ivjtblFees.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblFees;
	}
	/**
	 * Returns the String in the Total Price textfield.
	 * 
	 * @return String
	 */
	private String getTotalPrice()
	{
		return gettxtTotalPrice().getText();
	}
	/**
	 * Return the txtQuantity property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtQuantity()
	{
		if (ivjtxtQuantity == null)
		{
			try
			{
				ivjtxtQuantity = new RTSInputField();
				ivjtxtQuantity.setSize(32, 20);
				ivjtxtQuantity.setName("txtQuantity");
				ivjtxtQuantity.setInput(1);
				ivjtxtQuantity.setText(ONE);
				ivjtxtQuantity.setMaxLength(4);
				ivjtxtQuantity.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtQuantity.setLocation(362, 108);
				ivjtxtQuantity.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtQuantity;
	}
	/**
	 * Return the txtReason property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtReason()
	{
		if (ivjtxtReason == null)
		{
			try
			{
				ivjtxtReason = new RTSInputField();
				ivjtxtReason.setBounds(157, 348, 338, 20);
				ivjtxtReason.setName("txtReason");
				ivjtxtReason.setEnabled(true);
				ivjtxtReason.setMaxLength(40);
				// user code begin {1}
				ivjtxtReason.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtReason;
	}
	/**
	 * Return the txtTotalPrice property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtTotalPrice()
	{
		if (ivjtxtTotalPrice == null)
		{
			try
			{
				ivjtxtTotalPrice = new RTSInputField();
				ivjtxtTotalPrice.setBounds(421, 108, 85, 20);
				ivjtxtTotalPrice.setName("txtTotalPrice");
				ivjtxtTotalPrice.setInput(5);
				// defect 10596
				//ivjtxtTotalPrice.setMaxLength(8);
				ivjtxtTotalPrice.setMaxLength(MAX_LENGTH);
				// end defect 10596
				ivjtxtTotalPrice.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtTotalPrice.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTotalPrice;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
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
			setName("FrmAdditionalCollections");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(600, 440);
			setModal(true);
			setTitle(INIT_TITLE);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
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
			FrmAdditionalCollectionsACC001 aaFrmACC001 =
				new FrmAdditionalCollectionsACC001();
			aaFrmACC001.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * Fills the Select Fee combo box with the AccountCodesData.
	 */
	private void populateDropDown()
	{
		int liSize = cvData.size();
		Vector lvVector = new Vector();
		for (int i = 0; i < liSize; i++)
		{
			AccountCodesData laAcctCodesData =
				(AccountCodesData) cvData.get(i);
			lvVector.add(laAcctCodesData.getAcctItmCdDesc().trim());
		}
		DefaultComboBoxModel laDCBModel =
			new DefaultComboBoxModel(lvVector);
		getcomboSelectFee().setModel(laDCBModel);
		cbDoClick = true;
		// defect 8479
		comboBoxHotKeyFix(getcomboSelectFee());
		// end defect 8479
	}
	/**
	 * Sets the name of the county.
	 * 
	 * @param lsCountyName	String
	 */
	private void setCountyName(String lsCountyName)
	{
		getlblCountyName().setText(lsCountyName);
	}
	/**
	 * Sets the county number.
	 * 
	 * @param lsCountyNo	String
	 */
	private void setCountyNo(String lsCountyNo)
	{
		getlblCountyNo().setText(lsCountyNo);
	}
	/**
	 * Sets the Data on ACC001.
	 * <p>aaObject will be of type Vector, which contain 
	 * AccountCodesData.  This function will then populate the 
	 * dropdown list from the vector and set the labels correctly.
	 * 
	 * @param aaObject	Object
	 */
	public void setData(Object aaObject)
	{
		cvData = (Vector) aaObject;
		setQuantity("1");
		cvTableData = new Vector();
		populateDropDown();
		int liCountyNo = SystemProperty.getOfficeIssuanceNo();
		setCountyNo(Integer.toString(liCountyNo));
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(liCountyNo);
		setCountyName(laOfficeIdsData.getOfcName());
	}
	/**
	 * Sets the quantity in the Quantity text field.
	 * 
	 * @param lsQuantity	String
	 */
	private void setQuantity(String lsQuantity)
	{
		gettxtQuantity().setText(lsQuantity);
	}

	/**
	 * Sets the total in the Total label.
	 * 
	 * @param lsTotal	String
	 */
	private void setTotal(String lsTotal)
	{
		getlblTotal().setText(lsTotal);
	}

	/**
	 * Validates the entry to go in the table and then puts focus at the
	 * appropriate place.
	 * 
	 * @return boolean
	 */
	private boolean shouldContinueAfterValidation()
	{
		RTSException leRTSEx = new RTSException();
		int liQuantity = 0;
		try
		{
			liQuantity = Integer.parseInt(getQuantity());
		}
		catch (NumberFormatException aeNFEx)
		{
			gettxtQuantity().setText(ONE);
			leRTSEx.addException(
				new RTSException(150),
				gettxtQuantity());
		}
		if (liQuantity < 1)
		{
			gettxtQuantity().setText(ONE);
			leRTSEx.addException(
				new RTSException(150),
				gettxtQuantity());
		}
		double ldTotalPrice = 0.0;
		try
		{
			ldTotalPrice = Double.parseDouble(getTotalPrice());
		}
		catch (NumberFormatException aeNFEx)
		{
			gettxtTotalPrice().setText(EMPTY_STRING);
			leRTSEx.addException(
				new RTSException(150),
				gettxtTotalPrice());
		}
		if (ldTotalPrice < 0)
		{
			leRTSEx.addException(
				new RTSException(150),
				gettxtTotalPrice());
		}
		// defect 10596
		//		else if (ldTotalPrice > 99999.99)
		//		{
		//			RTSException leRTSPriceExp =
		//				new RTSException(
		//					RTSException.WARNING_MESSAGE,
		//					ERRMSG_PRICE_RANGE,
		//					ERRMSG_ERROR);
		//			leRTSEx.addException(leRTSPriceExp, gettxtTotalPrice());
		//		}
		else if (ldTotalPrice > MAX_PRICE)
		{
			RTSException leRTSPriceExp =
				new RTSException(
					RTSException.WARNING_MESSAGE,
					ERRMSG_PRICE_RANGE,
					ERRMSG_ERROR);
			leRTSEx.addException(leRTSPriceExp, gettxtTotalPrice());
		}
		double ldAddedUpPrice = 0.0;
		try
		{
			ldAddedUpPrice =
				Double.parseDouble(getlblTotal().getText());
		}
		catch (NumberFormatException aeNFEx2)
		{
			// nothing to do.  Ignore.
		}

		if (ldAddedUpPrice + ldTotalPrice > MAX_PRICE
			&& !leRTSEx.isValidationError())
		{
			RTSException leRTSPriceExp =
				new RTSException(
					RTSException.WARNING_MESSAGE,
					ERRMSG_PRICE_RANGE,
					ERRMSG_ERROR);
			leRTSEx.addException(leRTSPriceExp, gettxtTotalPrice());
		}
		// end defect 10596

		// defect 10741		
		// Create a new entry and then populate it before putting it in the table.
		AccountCodesData laAcctCodesData =
			(AccountCodesData) cvData.get(
				getcomboSelectFee().getSelectedIndex());

		if (laAcctCodesData.getBaseFee().compareTo(new Dollar(0)) != 0
			&& ldTotalPrice > 0)
		{
			double ldMultiple =
				AccountCodesCache.getMultiple(
					laAcctCodesData.getAcctItmCd(),
					ldTotalPrice);

			double ldQty =
				Double.parseDouble(gettxtQuantity().getText());

			if (ldMultiple < 0 || ldMultiple != ldQty)
			{
				String lsAppend =
					" $"
						+ laAcctCodesData.getBaseFee()
						+ " AND THE QUANTITY MUST REFLECT THAT MULTIPLE.";

				RTSException leRTSMultEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_MULTIPLE_OF_BASE_FEE,
						new String[] { lsAppend });

				leRTSEx.addException(leRTSMultEx, gettxtTotalPrice());
			}
		}
		// end defect 10741

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			cbDoClick = false;
			return false;
		}

		AdditionalCollectionsTableData laAddlCollectionsTblData =
			new AdditionalCollectionsTableData();
		laAddlCollectionsTblData.setFee(
			laAcctCodesData.getAcctItmCdDesc());
		laAddlCollectionsTblData.setQty(
			Integer.parseInt(getQuantity()));
		Dollar laDollar = new Dollar(ldTotalPrice);
		laAddlCollectionsTblData.setTotal(laDollar);
		laAddlCollectionsTblData.setFeeCd(
			laAcctCodesData.getAcctItmCd());
		laAddlCollectionsTblData.setCreditIndi(
			laAcctCodesData.getCrdtAllowdIndi());
		boolean lbAlreadyPresent = false;
		int liSelectedRow = 0;
		for (int i = 0; i < cvTableData.size(); i++)
		{
			AdditionalCollectionsTableData laAddlCollectionsTableData =
				(AdditionalCollectionsTableData) cvTableData.get(i);
			if (laAddlCollectionsTblData
				.getFee()
				.equals(laAddlCollectionsTableData.getFee()))
			{
				cvTableData.set(i, laAddlCollectionsTblData);
				lbAlreadyPresent = true;
				liSelectedRow = i;
			}
		}
		if (!lbAlreadyPresent)
		{
			cvTableData.add(laAddlCollectionsTblData);
		}
		caTableModel.add(cvTableData);
		gettxtQuantity().setText(ONE);
		gettxtTotalPrice().setText(EMPTY_STRING);
		updateTotal();
		if (!gettxtReason().isEnabled())
		{
			gettxtReason().setEnabled(true);
		}
		if (!lbAlreadyPresent)
		{
			gettblFees().setRowSelectionInterval(
				cvTableData.size() - 1,
				cvTableData.size() - 1);
		}
		else
		{
			gettblFees().setRowSelectionInterval(
				liSelectedRow,
				liSelectedRow);
		}
		// move scroll pane to show last row entered
		if (gettblFees().getSelectedRow() == 0)
		{
			gettblFees().scrollRectToVisible(new Rectangle(0, 0, 1, 1));
		}
		else
		{
			gettblFees().scrollRectToVisible(
				new Rectangle(
					0,
					gettblFees().getRowHeight()
						* (gettblFees().getSelectedRow() + 1),
					1,
					1));
		}
		getcomboSelectFee().requestFocus();
		return true;
	}
	/**
	 * Updates the total label with the sum of the table's totals.
	 */
	private void updateTotal()
	{
		Dollar laDollar = new Dollar(ZERO_DOLLAR);
		for (int i = 0; i < cvTableData.size(); i++)
		{
			AdditionalCollectionsTableData laAddlCollectionsTblData =
				(AdditionalCollectionsTableData) cvTableData.get(i);
			laDollar =
				laDollar.add(laAddlCollectionsTblData.getTotal());
		}
		setTotal(laDollar.toString());
	}
}
