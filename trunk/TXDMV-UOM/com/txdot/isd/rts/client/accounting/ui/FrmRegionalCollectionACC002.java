package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.ProductServiceCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;
// defect 11218
//import java.awt.Dimension;
// end defect 11218
import java.awt.Rectangle;
import javax.swing.JLabel;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import javax.swing.JTextField;

/*
 *
 * FrmRegionalCollectionACC002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/15/2002	Adding Max Length to Reason textfield to 
 * 							avoid DB2Exception 
 * 							defect 3479
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking() 
 * MAbs			04/24/2002	Inventory was not prompting 
 * 							defect 3451
 * B Arredondo	03/15/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	05/20/2004	Remove items with PrintableIndi from
 *							listbox
 *							modify getTableData()
 *							defect 7106  Ver 5.2.0
 * Min Wang		11/12/2004	Fix two selections on the list.
 * 							modify getTableData()
 *							defect 7316 Ver 5.2.2
 * K Harrell	01/13/2005	ClearAllColor when select new item
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							modify valueChanged()
 *							defect 7099 Ver 5.2.3 
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	06/20/2005	Java 1.4 Work
 * 							movement of services.cache.*Data to 
 * 							services.data 
 * 							defect 7899  Ver 5.2.3
 * K Harrell	06/28/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3  
 * B Hargrove 	07/02/2007 	Rounding error. Parsing text item price of 
 * 							'2.3' into a double, then multiplying by 
 * 							integer quantity 6 = 13.7999999999999999995
 * 							(should be 13.80).
 * 							modify actionPerformed()
 * 							defect 9137 Ver Special Plates
 * K Harrell	01/25/2008	Allow quantity <= 40 when inventory issued
 * 							delete getBuilderData() 
 * 							modify ERRMSG_QTY, actionPerformed()  
 * 							defect 9519 Ver 3 Amigos Prep 
 * B Hargrove 	05/05/2008 	Add Try\Catch blocks to handle error when no
 * 							Quantity is entered.
 * 							modify actionPerformed()
 * 							defect 8697 Ver Defect POS A
 * K Harrell	08/29/2008	Move validations from actionPerformed to 
 * 							new method validateFields().  Add validation
 * 							of InvItmYr. Made grammar in error msgs 
 * 							consistent. Print out item code if in 
 * 							development. 
 * 							add isIssueInventory(), setupComplTransData(),
 * 							 validateFields() 
 * 							add MAX_PRICE 
 * 							modify ERRMSG_PRICE, ERRMSG_QTY
 * 							modify actionPerformed(), valueChanged() 
 * 							defect 9522 Ver Defect_POS_B
 * K Harrell	09/08/2008	Use ErrMsgNo 153 vs. 150 on invalid year. 
 * 							modify validateFields()
 * 							defect 9522 Ver Defect_POS_B  
 * Min Wang		11/08/2010	Accommodate new max field length of the price.
 * 							modify ERRMSG_PRICE, MAX_PRICE, visual editor
 * 							defect 10656 Ver 6.6.0
 * K Harrell	12/09/2010	Modified in refactor of 
 * 							ProductServiceData InvItmCd to ItmCd, 
 * 							 YrRqrdIndi to YrReqdIndi
 * 							defect 10695 Ver 6.7.0 
 * B Hargrove	02/08/2012	If an IRP item is selected, display entry 
 * 							fields to allow all three IRP fields to be 
 * 							entered (CABCRD-R, IRPFEE-R, IRPTX-R).
 * 							Use constants for all 'setInput()'.
 * 							add ivjlblItem2/3, ivjtxtQty2/3, ivjtxtPrice2/3,
 * 							MAX_PRICE_LENGTH,
 * 							getlblItem2(), getlblItem3(), 
 * 							getIvjtxtQty2(), getIvjtxtQty3(),  
 * 							getIvjtxtPrice2(), getIvjtxtPrice3(), 
 * 							addIRPFees, validateIRPFields()
 * 							modify actionPerformed(), getJPanel2(), 
 * 							getJPanel3(), setData(), setupComplTransData(),
 * 							valueChanged()
 * 							delete keyPressed()
 * 							defect 11218 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Regional Collections ACC002 screen
 * 
 * @version	6.10.0		02/08/2012 
 * @author 	Michael Abernethy
 * <br>Creation Date: 	12/17/2001 15:12:25 
 */

public class FrmRegionalCollectionACC002
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private JLabel ivjlblItem = null;
	private JLabel ivjstcLblItem = null;
	private JLabel ivjstcLblNote = null;
	private JLabel ivjstcLblPrice = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblYear = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSInputField ivjtxtNote = null;
	private RTSInputField ivjtxtPrice = null;
	private RTSInputField ivjtxtQty = null;
	private RTSInputField ivjtxtYear = null;
	private RTSTable ivjtblItems = null;
	private TMACC002 caTableModel;

	// Object 
	private ProductServiceData caSelectedRecord;

	// Vector 
	private Vector cvTableData;

	//String
	private static final String CANCEL = "Cancel";
	private static final String ENTER = "Enter";
	private static final String ERRMSG_INVALID_PRICE = "Invalid Price";
	private static final String ERRMSG_INVALID_QTY = "Invalid Qty";
	//	defect 9522
	// defect 10656
	private static final String ERRMSG_PRICE =
	"Total Quantity times Price must be between 0.00 and 99,999,999.99";
		//"Quantity times price must be between 0.00 and 99,999.99";
	// defect 10656
	//	"Quantity times prices must be in the range of 0.00 to 99,999.99";

	private static final String ERRMSG_QTY =
		"Quantity must be between 1 and "
			+ AccountingConstant.MAX_RGN_COLL_INV_QTY
			+ " when inventory is issued.";
	//"Quantity must be 1 to 3 when inventory is issued.";
	// defect 10656
	// private static final double MAX_PRICE = 99999.99;
	private static final double MAX_PRICE = 99999999.99;
	// end defect 10656
	// end defect 9522 

	private static final String ITEM = "Item";
	private static final String NOTE = "Note:";
	private static final String PRICE = "Price";
	private static final String QTY = "Qty";
	private static final String REFUND_R = "REFUND-R";
	private static final String SHRAGE_R = "SHRAGE-R";
	private static final String TITLE_ACC002 =
		"Regional Collection   ACC002";
	private static final String YEAR = "Year";
	private static final String ZERO_DOLLAR = "0.00";
	
	// defect 11218
	private JLabel ivjlblItem2 = null;
	private JLabel ivjlblItem3 = null;
	private RTSInputField ivjtxtQty2 = null;
	private RTSInputField ivjtxtQty3 = null;
	private RTSInputField ivjtxtPrice2 = null;
	private RTSInputField ivjtxtPrice3 = null;
	private static final int MAX_PRICE_LENGTH = 10;
	private static final String ERRMSG_INVALID_BLANK_ENTRY = "Invalid Blank Entry";
	private static final String ERRMSG_BLANK_ENTRY =
		"No IRP items have been entered. All Quantities are zero.";
	// end defect 11218

	/**
	 * FrmRegionalCollectionACC002 constructor comment.
	 */
	public FrmRegionalCollectionACC002()
	{
		super();
		initialize();
	}
	/**
	 * FrmRegionalCollectionACC002 constructor comment.
	 * 
	 * @param aaOwner	Dialog 
	 */
	public FrmRegionalCollectionACC002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmRegionalCollectionACC002 constructor comment.
	 * 
	 * @param aaOwner	JFrame 
	 */
	public FrmRegionalCollectionACC002(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 *
	 * @param aaAE	ActionEvent 
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

			if (aaAE.getSource() == getbtnEnter())
			{
				// defect 9522
				// defect 11218
				//if (validateFields())
				if ((caSelectedRecord.getIRPIndi() == 0 &&
					validateFields()) ||
					(caSelectedRecord.getIRPIndi() == 1 &&
						validateIRPFields()))
				{
				// end defect 11218
					int liProcess =
						isIssueInventory()
							? VCRegionalCollectionACC002.INV
							: AbstractViewController.ENTER;
					getController().processData(
						liProcess,
						setupComplTransData());
				}
				// end defect 9522 
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Add the IRP fields to Fees.
	 *
	 * @param asAcctItmCd	String  
	 * @param aiQty			int  
	 * @param adPrice		double 
	 * @param aiToday		int  
	 * @param aaFeesData	FeesData  
	 * 
	 */
	private FeesData addIRPFees(
		String asAcctItmCd, int aiQty, double adPrice, int aiToday, FeesData aaFeesData)
	{
		AccountCodesData laAcctCdData =
			AccountCodesCache.getAcctCd(asAcctItmCd, aiToday);
		
		aaFeesData.setAcctItmCd(asAcctItmCd);
		aaFeesData.setDesc(laAcctCdData.getAcctItmCdDesc());
		aaFeesData.setItmQty(aiQty);
		aaFeesData.setItemPrice(new Dollar(adPrice * aiQty).round());
		aaFeesData.setCrdtAllowedIndi(
			laAcctCdData.getCrdtAllowdIndi());
		
		return aaFeesData;
		
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
				ivjbtnCancel.setText(CANCEL);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjbtnEnter.setText(ENTER);
				// user code begin {1}
				getRootPane().setDefaultButton(ivjbtnEnter);
				ivjbtnEnter.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnEnter;
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
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintstxtNote =
					new java.awt.GridBagConstraints();
				constraintstxtNote.gridx = 2;
				constraintstxtNote.gridy = 1;
				constraintstxtNote.gridwidth = 2;
				constraintstxtNote.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtNote.weightx = 1.0;
				constraintstxtNote.ipadx = 423;
				constraintstxtNote.insets =
					new java.awt.Insets(10, 3, 5, 26);
				getJPanel1().add(gettxtNote(), constraintstxtNote);

				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 2;
				constraintsbtnEnter.gridy = 2;
				constraintsbtnEnter.ipadx = 36;
				constraintsbtnEnter.insets =
					new java.awt.Insets(5, 38, 11, 53);
				getJPanel1().add(getbtnEnter(), constraintsbtnEnter);

				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 3;
				constraintsbtnCancel.gridy = 2;
				constraintsbtnCancel.ipadx = 28;
				constraintsbtnCancel.insets =
					new java.awt.Insets(5, 54, 11, 109);
				getJPanel1().add(getbtnCancel(), constraintsbtnCancel);

				java.awt.GridBagConstraints constraintsstcLblNote =
					new java.awt.GridBagConstraints();
				constraintsstcLblNote.gridx = 1;
				constraintsstcLblNote.gridy = 1;
				constraintsstcLblNote.ipadx = 16;
				constraintsstcLblNote.insets =
					new java.awt.Insets(13, 21, 8, 3);
				getJPanel1().add(
					getstcLblNote(),
					constraintsstcLblNote);
				// user code begin {1}
				ivjJPanel1.setBounds(44, 363, 517, 68);
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
	 * Return the JPanel2 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);

				ivjJPanel2.add(getstcLblQty(), null);
				ivjJPanel2.add(getstcLblPrice(), null);
				ivjJPanel2.add(gettxtQty(), null);
				ivjJPanel2.add(gettxtPrice(), null);
				ivjJPanel2.setBounds(381, 227, 170, 125);
				// user code end
				ivjJPanel2.add(gettxtQty2(), null);
				ivjJPanel2.add(gettxtQty3(), null);
				ivjJPanel2.add(gettxtPrice2(), null);
				ivjJPanel2.add(gettxtPrice3(), null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				// defect 11218
				//ivjlblItem3 = new JLabel();
				//ivjlblItem3.setBounds(new Rectangle(15, 99, 244, 16));
				//ivjlblItem3.setText("");
				//ivjlblItem3.setName("lblItem2");
				//ivjlblItem2 = new JLabel();
				//ivjlblItem2.setBounds(new Rectangle(15, 69, 244, 16));
				//ivjlblItem2.setName("lblItem2");
				//ivjlblItem2.setText("");
				ivjJPanel3 = new javax.swing.JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(48, 227, 288, 125);

				ivjJPanel3.add(getstcLblItem(), null);
				ivjJPanel3.add(getlblItem(), null);
				ivjJPanel3.add(getlblItem2(), null);
				ivjJPanel3.add(getlblItem3(), null);
				//ivjJPanel3.add(ivjlblItem2, null);
				//ivjJPanel3.add(ivjlblItem3, null);
				// end defect 11218
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel4 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new javax.swing.JPanel();
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setLayout(null);

				ivjJPanel4.add(getstcLblYear(), null);
				ivjJPanel4.add(gettxtYear(), null);
				ivjJPanel4.setBounds(337, 229, 42, 64);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel4;
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
				getJScrollPane1().setViewportView(gettblItems());
				// user code begin {1}
				ivjJScrollPane1.setBounds(56, 29, 479, 183);
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
	 * Return the lblItem property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblItem()
	{
		if (ivjlblItem == null)
		{
			try
			{
				ivjlblItem = new javax.swing.JLabel();
				ivjlblItem.setBounds(15, 39, 244, 16);
				ivjlblItem.setName("lblItem");
				ivjlblItem.setText(
					"                                                                                 ");
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
		return ivjlblItem;
	}

	/**
	 * Return the lblItem2 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblItem2()
	{
		if (ivjlblItem2 == null)
		{
			try
			{
				ivjlblItem2 = new javax.swing.JLabel();
				ivjlblItem2.setBounds(15, 69, 244, 16);
				ivjlblItem2.setName("lblItem2");
				ivjlblItem2.setText(
					"                                                                                 ");
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
		return ivjlblItem2;
	}

	/**
	 * Return the lblItem3 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblItem3()
	{
		if (ivjlblItem3 == null)
		{
			try
			{
				ivjlblItem3 = new javax.swing.JLabel();
				ivjlblItem3.setBounds(15, 99, 244, 16);
				ivjlblItem3.setName("lblItem3");
				ivjlblItem3.setText(
					"                                                                                 ");
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
		return ivjlblItem3;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new javax.swing.JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);

				ivjRTSDialogBoxContentPane.add(getJScrollPane1(), null);
				ivjRTSDialogBoxContentPane.add(getJPanel4(), null);
				ivjRTSDialogBoxContentPane.add(getJPanel2(), null);
				ivjRTSDialogBoxContentPane.add(getJPanel1(), null);
				ivjRTSDialogBoxContentPane.add(getJPanel3(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the stcLblItem property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblItem()
	{
		if (ivjstcLblItem == null)
		{
			try
			{
				ivjstcLblItem = new javax.swing.JLabel();
				ivjstcLblItem.setBounds(15, 9, 268, 16);
				ivjstcLblItem.setName("stcLblItem");
				ivjstcLblItem.setText(ITEM);
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
		return ivjstcLblItem;
	}

	/**
	 * Return the stcLblNote property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblNote()
	{
		if (ivjstcLblNote == null)
		{
			try
			{
				ivjstcLblNote = new javax.swing.JLabel();
				ivjstcLblNote.setName("stcLblNote");
				ivjstcLblNote.setText(NOTE);
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
		return ivjstcLblNote;
	}

	/**
	 * Return the stcLblPrice property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblPrice()
	{
		if (ivjstcLblPrice == null)
		{
			try
			{
				ivjstcLblPrice = new javax.swing.JLabel();
				ivjstcLblPrice.setBounds(56, 8, 96, 16);
				ivjstcLblPrice.setName("stcLblPrice");
				ivjstcLblPrice.setText(PRICE);
				ivjstcLblPrice.setHorizontalAlignment(
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
		return ivjstcLblPrice;
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
				ivjstcLblQty.setBounds(3, 8, 46, 16);
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
	 * Return the stcLblYear property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblYear()
	{
		if (ivjstcLblYear == null)
		{
			try
			{
				ivjstcLblYear = new javax.swing.JLabel();
				ivjstcLblYear.setBounds(2, 6, 37, 16);
				ivjstcLblYear.setName("stcLblYear");
				ivjstcLblYear.setText(YEAR);
				ivjstcLblYear.setHorizontalAlignment(
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
		return ivjstcLblYear;
	}

	/**
	 * Populate table with data from RTS_PRDCT_SRVC
	 *
	 * @return Vector
	 */
	private Vector getTableData()
	{
		Vector lvProdSrvData =
			ProductServiceCache.getAllProductServiceData();

		for (int i = 0; i < lvProdSrvData.size(); i++)
		{
			ProductServiceData laProdData =
				(ProductServiceData) lvProdSrvData.get(i);

			// defect 7016
			// remove items with PrintableIndi = 1
			if (laProdData.getItmCd() != null)
			{
				ItemCodesData laItmCdData =
					ItemCodesCache.getItmCd(laProdData.getItmCd());

				if (laItmCdData != null
					&& laItmCdData.getPrintableIndi() == 1)
				{
					lvProdSrvData.remove(i);
					//defect 7316
					i = i - 1;
					//end defect 7316
					continue;
				}
			}
			// end defect 7106 
			AccountCodesData laAcctData =
				AccountCodesCache.getAcctCd(
					laProdData.getAcctItmCd(),
					RTSDate.getCurrentDate().getYYYYMMDDDate());

			if (laAcctData != null)
			{
				laProdData.setPrice(laAcctData.getMiscFee());
			}
			else
			{
				laProdData.setPrice(new Dollar(ZERO_DOLLAR));
			}
		}
		return lvProdSrvData;
	}

	/**
	 * Return the tblItems property value
	 * .
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblItems()
	{
		if (ivjtblItems == null)
		{
			try
			{
				ivjtblItems = new RTSTable();
				ivjtblItems.setName("tblItems");
				getJScrollPane1().setColumnHeaderView(
					ivjtblItems.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblItems.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblItems.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC002());
				ivjtblItems.setShowVerticalLines(false);
				ivjtblItems.setShowHorizontalLines(false);
				ivjtblItems.setAutoCreateColumnsFromModel(false);
				ivjtblItems.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblItems.setBounds(0, 0, 162, 200);
				// user code begin {1}
				caTableModel = (TMACC002) ivjtblItems.getModel();
				TableColumn a =
					ivjtblItems.getColumn(ivjtblItems.getColumnName(0));
				a.setPreferredWidth(115);
				ivjtblItems.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblItems.init();
				a.setCellRenderer(
					ivjtblItems.setColumnAlignment(RTSTable.LEFT));
				ivjtblItems
					.getSelectionModel()
					.addListSelectionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblItems;
	}

	/**
	 * Return the RTSInputField3 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtNote()
	{
		if (ivjtxtNote == null)
		{
			try
			{
				ivjtxtNote = new RTSInputField();
				ivjtxtNote.setName("txtNote");
				ivjtxtNote.setMaxLength(40);
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
		return ivjtxtNote;
	}

	/**
	 * Return the RTSInputField2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPrice()
	{
		if (ivjtxtPrice == null)
		{
			try
			{
				ivjtxtPrice = new RTSInputField();
				ivjtxtPrice.setBounds(56, 36, 96, 20);
				ivjtxtPrice.setName("txtPrice");
				// defect 11218
				//ivjtxtPrice.setInput(5);
				ivjtxtPrice.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtPrice.setMaxLength(MAX_PRICE_LENGTH);
				// end defect 11218
				ivjtxtPrice.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
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
		return ivjtxtPrice;
	}

	/**
	 * Return the txtQty property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtQty()
	{
		if (ivjtxtQty == null)
		{
			try
			{
				ivjtxtQty = new RTSInputField();
				ivjtxtQty.setBounds(3, 36, 46, 20);
				ivjtxtQty.setName("txtQty");
				// defect 11218
				//ivjtxtQty.setInput(1);
				ivjtxtQty.setInput(RTSInputField.NUMERIC_ONLY);
				// defect 11218
				ivjtxtQty.setMaxLength(3);
				ivjtxtQty.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
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
		return ivjtxtQty;
	}

	/**
	 * Return the RTSInputField1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setBounds(2, 34, 37, 20);
				ivjtxtYear.setName("txtYear");
				// defect 11218
				//ivjtxtYear.setInput(1);
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				// defect 11218
				ivjtxtYear.setMaxLength(4);
				ivjtxtYear.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
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
		return ivjtxtYear;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException	Throwable 
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
			setName("FrmRegionalCollectionACC002");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(600, 471);
			setTitle(TITLE_ACC002);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 *  Return boolean to indicate if issuing inventory 
	 * 
	 * @return boolean 
	 */
	private boolean isIssueInventory()
	{
		return caSelectedRecord.getItmCd() != null
			&& !caSelectedRecord.getItmCd().equals(
				CommonConstant.STR_SPACE_EMPTY);
	}

	/**
	 * This method initializes ivjtxtQty2	
	 * 	
	 * @return com.txdot.isd.rts.client.general.ui.RTSInputField	
	 */
	private RTSInputField gettxtQty2()
	{
		if (ivjtxtQty2 == null)
		{
			ivjtxtQty2 = new RTSInputField();
			ivjtxtQty2.setBounds(new Rectangle(3, 67, 46, 20));
			ivjtxtQty2.setHorizontalAlignment(JTextField.RIGHT);
			ivjtxtQty2.setMaxLength(3);
			// defect 11218
			//ivjtxtQty2.setInput(1);
			ivjtxtQty2.setInput(RTSInputField.NUMERIC_ONLY);
			// defect 11218
			ivjtxtQty2.setName("txtQty");
		}
		return ivjtxtQty2;
	}
	/**
	 * This method initializes ivjtxtQty3	
	 * 	
	 * @return com.txdot.isd.rts.client.general.ui.RTSInputField	
	 */
	private RTSInputField gettxtQty3()
	{
		if (ivjtxtQty3 == null)
		{
			ivjtxtQty3 = new RTSInputField();
			ivjtxtQty3.setBounds(new Rectangle(3, 98, 46, 20));
			ivjtxtQty3.setHorizontalAlignment(JTextField.RIGHT);
			ivjtxtQty3.setMaxLength(3);
			// defect 11218
			//ivjtxtQty3.setInput(1);
			ivjtxtQty3.setInput(RTSInputField.NUMERIC_ONLY);
			// defect 11218
			ivjtxtQty3.setName("txtQty");
		}
		return ivjtxtQty3;
	}
	/**
	 * This method initializes ivjtxtPrice2	
	 * 	
	 * @return com.txdot.isd.rts.client.general.ui.RTSInputField	
	 */
	private RTSInputField gettxtPrice2()
	{
		if (ivjtxtPrice2 == null)
		{
			ivjtxtPrice2 = new RTSInputField();
			ivjtxtPrice2.setBounds(new Rectangle(56, 67, 96, 20));
			ivjtxtPrice2.setHorizontalAlignment(JTextField.RIGHT);
			// defect 11218
			//ivjtxtPrice2.setInput.setInput(5);
			ivjtxtPrice2.setInput(RTSInputField.DOLLAR_ONLY);
			ivjtxtPrice2.setMaxLength(MAX_PRICE_LENGTH);
			// end defect 11218
			ivjtxtPrice2.setName("txtPrice");
		}
		return ivjtxtPrice2;
	}
	/**
	 * This method initializes ivjtxtPrice3	
	 * 	
	 * @return com.txdot.isd.rts.client.general.ui.RTSInputField	
	 */
	private RTSInputField gettxtPrice3()
	{
		if (ivjtxtPrice3 == null)
		{
			ivjtxtPrice3 = new RTSInputField();
			ivjtxtPrice3.setBounds(new Rectangle(56, 98, 96, 20));
			ivjtxtPrice3.setHorizontalAlignment(JTextField.RIGHT);
			// defect 11218
			//ivjtxtPrice3.setInput.setInput(5);
			ivjtxtPrice3.setInput(RTSInputField.DOLLAR_ONLY);
			ivjtxtPrice3.setMaxLength(MAX_PRICE_LENGTH);
			// end defect 11218
			ivjtxtPrice3.setName("txtPrice");
		}
		return ivjtxtPrice3;
	}
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs	String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRegionalCollectionACC002 aaFrmACC002;
			aaFrmACC002 = new FrmRegionalCollectionACC002();
			aaFrmACC002.setModal(true);
			aaFrmACC002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			aaFrmACC002.show();
			java.awt.Insets insets = aaFrmACC002.getInsets();
			aaFrmACC002.setSize(
				aaFrmACC002.getWidth() + insets.left + insets.right,
				aaFrmACC002.getHeight() + insets.top + insets.bottom);
			aaFrmACC002.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Validate Fields 
	 * 
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		boolean lbValid = true;
		double ldPrice = 0.0;
		RTSException leRTSEx = new RTSException();

		if (gettxtQty().isEmpty())
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtQty());
		}

		if (caSelectedRecord.getYrReqdIndi() == 1
			&& gettxtYear().isEmpty())
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtYear());
		}

		try
		{
			ldPrice = Double.parseDouble(gettxtPrice().getText());

		}
		catch (NumberFormatException leNFEx)
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtPrice());
		}

		if (!leRTSEx.isValidationError())
		{
			int liQty = Integer.parseInt(gettxtQty().getText());
			if (liQty == 0)
			{
				leRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtQty());
			}
			else if (
				isIssueInventory()
					&& liQty > AccountingConstant.MAX_RGN_COLL_INV_QTY)
			{
				leRTSEx.addException(
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						ERRMSG_QTY,
						ERRMSG_INVALID_QTY),
					gettxtQty());
			}
			int liYear = 0;
			if (caSelectedRecord.getYrReqdIndi() != 0)
			{
				liYear = Integer.parseInt(gettxtYear().getText());
				Vector lvInvPatrnsData =
					InventoryPatternsCache.getInvPatrns(
						caSelectedRecord.getItmCd(),
						liYear);
				if (lvInvPatrnsData == null
					|| lvInvPatrnsData.size() == 0)
				{

					leRTSEx.addException(
						new RTSException(153),
						gettxtYear());
				}
			}

			double ldTmpPrice = ldPrice * liQty;
			if (ldTmpPrice > MAX_PRICE)
			{
				leRTSEx.addException(
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						ERRMSG_PRICE,
						ERRMSG_INVALID_PRICE),
					gettxtPrice());
			}
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}


	/**
	 * Validate IRP Fields 
	 * 
	 * @return boolean 
	 */
	private boolean validateIRPFields()
	{
		boolean lbValid = true;
		RTSException leRTSEx = new RTSException();

		int liQty = 0;
		int liQty2 = 0;
		int liQty3 = 0;
		double ldPrice = 0.0;
		double ldPrice2 = 0.0;
		double ldPrice3 = 0.0;
		
		try
		{
			if (gettxtQty().isEmpty())
			{
				liQty = 0;	
			}
			else
			{
				liQty = Integer.parseInt(gettxtQty().getText());
			}
			if (liQty > 0)
			{
				ldPrice = Double.parseDouble(gettxtPrice().getText());
				if ((ldPrice * liQty) > MAX_PRICE)
				{
					leRTSEx.addException(
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							ERRMSG_PRICE,
							ERRMSG_INVALID_PRICE),
							gettxtPrice());
				}
			}
			else if (!gettxtPrice().isEmpty())
			{
				ldPrice = Double.parseDouble(gettxtPrice().getText());
				if (ldPrice > 0.0 && liQty == 0)
				{
					leRTSEx.addException(new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtQty());
				}
			}
		}
		catch (NumberFormatException leNFEx)
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtPrice());
		}

		try
		{
			if (gettxtQty2().isEmpty())
			{
				liQty2 = 0;	
			}
			else
			{
				liQty2 = Integer.parseInt(gettxtQty2().getText());
			}
			if (liQty2 > 0)
			{
				ldPrice2 = Double.parseDouble(gettxtPrice2().getText());
				if ((ldPrice2 * liQty2) > MAX_PRICE)
				{
					leRTSEx.addException(
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							ERRMSG_PRICE,
							ERRMSG_INVALID_PRICE),
							gettxtPrice2());
				}
			}
			else if (!gettxtPrice2().isEmpty())
			{
				ldPrice2 = Double.parseDouble(gettxtPrice2().getText());
				if (ldPrice2 > 0.0 && liQty2 == 0)
				{
					leRTSEx.addException(new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtQty2());
				}
			}
		}
		catch (NumberFormatException leNFEx)
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtPrice2());
		}

		try
		{
			if (gettxtQty3().isEmpty())
			{
				liQty3 = 0;	
			}
			else
			{
				liQty3 = Integer.parseInt(gettxtQty3().getText());
			}
			if (liQty3 > 0)
			{
				ldPrice3 = Double.parseDouble(gettxtPrice3().getText());
				if ((ldPrice3 * liQty3) > MAX_PRICE)
				{
					leRTSEx.addException(
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							ERRMSG_PRICE,
							ERRMSG_INVALID_PRICE),
							gettxtPrice3());
				}
			}
			else if (!gettxtPrice3().isEmpty())
			{
				ldPrice3 = Double.parseDouble(gettxtPrice3().getText());
				if (ldPrice3 > 0.0 && liQty3 == 0)
				{
					leRTSEx.addException(new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtQty3());
				}
			}
		}
		catch (NumberFormatException leNFEx)
		{
			leRTSEx.addException(new RTSException(
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
				gettxtPrice3());
		}
		
		if (!leRTSEx.isValidationError())
		{
			if (liQty == 0 && liQty2 == 0 && liQty3 == 0)
			{
				leRTSEx.addException(
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						ERRMSG_BLANK_ENTRY,
						ERRMSG_INVALID_BLANK_ENTRY),
						gettxtQty());
				leRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
					gettxtQty2());
				leRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), 
					gettxtQty3());
			}
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}
	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 *
	 * @param aaDataObject	Object  
	 */
	public void setData(Object aaDataObject)
	{
		cvTableData = getTableData();
		Collections.sort(cvTableData);
		caTableModel.add(cvTableData);
		if (cvTableData.size() > 0)
		{
			gettblItems().setRowSelectionInterval(0, 0);
		}
		
		// defect 11218
		getlblItem2().setVisible(false);
		getlblItem3().setVisible(false);
		gettxtQty2().setVisible(false);
		gettxtQty2().setEnabled(false);
		gettxtQty3().setVisible(false);
		gettxtQty3().setEnabled(false);
		gettxtPrice2().setVisible(false);
		gettxtPrice2().setEnabled(false);
		gettxtPrice3().setVisible(false);
		gettxtPrice3().setEnabled(false);
		// end defect 11218
	}

	/**
	 * Setup CompleteTransactionData 
	 * 
	 * @return  CompleteTransactionData
	 */
	private CompleteTransactionData setupComplTransData()
	{
		CompleteTransactionData laCTData =
			new CompleteTransactionData();
		// defect 11218
		// If IRP, handle all three IRP entries
		if (caSelectedRecord.getIRPIndi() == 1)
		{
			RegFeesData laRegFeesData = new RegFeesData();
			
			Vector lvFees = new Vector();

			int liToday = new RTSDate().getYYYYMMDDDate();
			int liQty = 0;
			// Item = 'IRP Cab Card'
			if (!gettxtQty().isEmpty())
			{
				liQty = Integer.parseInt(gettxtQty().getText());
				if (liQty > 0)
				{
					FeesData laFeesData = new FeesData();
					String lsAcctItmCd = AcctCdConstant.REGCOL_IRPCAB;
					double ldPrice = Double.parseDouble(gettxtPrice().getText());
					addIRPFees(lsAcctItmCd, liQty, ldPrice, liToday, laFeesData);
					lvFees.add(laFeesData);
				}
			}
			
			// Item2 = 'IRP Out-of-state fee'
			if (!gettxtQty2().isEmpty())
			{
				liQty = Integer.parseInt(gettxtQty2().getText());
				if (liQty > 0)
				{
					FeesData laFeesData = new FeesData();
					String lsAcctItmCd = AcctCdConstant.REGCOL_IRPOS;
					double ldPrice = Double.parseDouble(gettxtPrice2().getText());
					addIRPFees(lsAcctItmCd, liQty, ldPrice, liToday, laFeesData);
					lvFees.add(laFeesData);
				}
			}
			
			// Item3 = 'IRP Texas fee'
			if (!gettxtQty3().isEmpty())
			{
				liQty = Integer.parseInt(gettxtQty3().getText());
				if (liQty > 0)
				{
					FeesData laFeesData = new FeesData();
					String lsAcctItmCd = AcctCdConstant.REGCOL_IRPTX;
					double ldPrice = Double.parseDouble(gettxtPrice3().getText());
					addIRPFees(lsAcctItmCd, liQty, ldPrice, liToday, laFeesData);
					lvFees.add(laFeesData);
				}
			}
			
			laRegFeesData.setReason(gettxtNote().getText());
			laRegFeesData.setVectFees(lvFees);
			
			laCTData.setTransCode(getController().getTransCode());
			laCTData.setRegFeesData(laRegFeesData);

		}
		else
		{
			int liQty = Integer.parseInt(gettxtQty().getText());
			double ldPrice = Double.parseDouble(gettxtPrice().getText());

			int liYear =
				caSelectedRecord.getYrReqdIndi() == 0
					? 0
					: Integer.parseInt(gettxtYear().getText());

			RegFeesData laRegFeesData = new RegFeesData();

			laRegFeesData.setReason(gettxtNote().getText());
			
			Vector lvFees = new Vector();

			FeesData laFeesData = new FeesData();

			laFeesData.setAcctItmCd(caSelectedRecord.getAcctItmCd());
			laFeesData.setCrdtAllowedIndi(
				caSelectedRecord.getCrdtItmIndi());

			// Get account codes from cache instead of drop down
			// if available
			AccountCodesData laAcctCodesData =
				AccountCodesCache.getAcctCd(
					laFeesData.getAcctItmCd(),
					new RTSDate().getYYYYMMDDDate());
			
			if (laAcctCodesData != null)
			{
				laFeesData.setDesc(laAcctCodesData.getAcctItmCdDesc());
			}
			else
			{
				laFeesData.setDesc(caSelectedRecord.getPrdctSrvcDesc());
			}
			// defect 9137
			// Fix rounding error
			//laFeesData.setItemPrice(new Dollar(ldPrice * liQty));
			laFeesData.setItemPrice(new Dollar(ldPrice * liQty).round());
			// end defect 9137

			// Negate value for negative payment
			if (laFeesData.getAcctItmCd().equals(REFUND_R)
				|| laFeesData.getAcctItmCd().equals(SHRAGE_R))
			{
				laFeesData.setItemPrice(
					new Dollar(ZERO_DOLLAR).subtract(
						laFeesData.getItemPrice()));
			}
			laFeesData.setItmQty(liQty);

			if (caSelectedRecord.getFxdChrgIndi() == 0)
			{
				lvFees.add(laFeesData);
				laRegFeesData.setVectFees(lvFees);
			}

			laCTData.setRegFeesData(laRegFeesData);
			laCTData.setTransCode(getController().getTransCode());

			if (isIssueInventory())
			{
				Vector lvInv = new Vector();

				for (int i = 0; i < liQty; i++)
				{
					ProcessInventoryData laInvData =
						new ProcessInventoryData();
					laInvData.setItmCd(caSelectedRecord.getItmCd());
					laInvData.setInvItmYr(liYear);
					laInvData.setInvQty(1);
					laInvData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laInvData.setSubstaId(SystemProperty.getSubStationId());
					laInvData.setTransEmpId(
						SystemProperty.getCurrentEmpId());
					laInvData.setTransWsId(
						Integer.toString(
							SystemProperty.getWorkStationId()));
					lvInv.add(laInvData);
					laCTData.setInvItms(lvInv);
					Vector lvAlloc = new Vector();
					laCTData.setAllocInvItms(lvAlloc);
				}
			}
		}
		// end defect 11218
		
		return laCTData;
	}

	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE	javax.swing.event.ListSelectionEvent
	 */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}
		// defect 7099
		// Clear color with new selection
		clearAllColor(this);
		// end defect 7099
		int liSelectedRow = gettblItems().getSelectedRow();
		caSelectedRecord =
			(ProductServiceData) cvTableData.get(liSelectedRow);
		
		// defect 11218
		// Display the 3 IRP items if an IRP item is selected 
		getstcLblPrice().setEnabled(true);
		gettxtPrice().setEnabled(true);
		boolean lbSet = caSelectedRecord.getIRPIndi() == 1;
		getstcLblYear().setVisible(!lbSet);
		gettxtYear().setVisible(!lbSet);
		gettxtYear().setEnabled(!lbSet);
		getlblItem2().setVisible(lbSet);
		getlblItem3().setVisible(lbSet);
		gettxtQty2().setVisible(lbSet);
		gettxtQty2().setEnabled(lbSet);
		gettxtQty3().setVisible(lbSet);
		gettxtQty3().setEnabled(lbSet);
		gettxtPrice2().setVisible(lbSet);
		gettxtPrice2().setEnabled(lbSet);
		gettxtPrice3().setVisible(lbSet);
		gettxtPrice3().setEnabled(lbSet);
		
		ivjlblItem.setDisplayedMnemonic(KeyEvent.CHAR_UNDEFINED);
		ivjlblItem.setLabelFor(ivjtxtQty);
		if (lbSet)
		{
			ivjlblItem.setDisplayedMnemonic(KeyEvent.VK_C);
			ivjlblItem.setLabelFor(ivjtxtQty);
			ivjlblItem2.setDisplayedMnemonic(KeyEvent.VK_O);
			ivjlblItem2.setLabelFor(ivjtxtQty2);
			ivjlblItem3.setDisplayedMnemonic(KeyEvent.VK_T);
			ivjlblItem3.setLabelFor(ivjtxtQty3);

			gettxtQty().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtQty2().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtQty3().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtPrice().setText(
					CommonConstant.STR_ZERO_DOLLAR);
			gettxtPrice2().setText(
					CommonConstant.STR_ZERO_DOLLAR);
			gettxtPrice3().setText(
					CommonConstant.STR_ZERO_DOLLAR);

			if (caSelectedRecord.getAcctItmCd().equals(
				AcctCdConstant.REGCOL_IRPCAB))
			{
				gettxtQty().setText("1");
				gettxtPrice().setText(
						caSelectedRecord.getPrice().toString());
			}
			else if (caSelectedRecord.getAcctItmCd().equals(
				AcctCdConstant.REGCOL_IRPOS))
			{
				gettxtQty2().setText("1");
				gettxtPrice2().setText(
						caSelectedRecord.getPrice().toString());
			}
			else 
			{
				gettxtQty3().setText("1");
				gettxtPrice3().setText(
						caSelectedRecord.getPrice().toString());
			}

			
			for (int i = 0; i < cvTableData.size(); i++)
			{
				// Item = 'IRP Cab Card'
				ProductServiceData laData =
					(ProductServiceData) cvTableData.get(i);
				if (laData.getAcctItmCd().equals(
					AcctCdConstant.REGCOL_IRPCAB))
				{
					getlblItem().setText(laData.getPrdctSrvcDesc());
				}
				// Item2 = 'IRP Out-of-state fee'
				else if (laData.getAcctItmCd().equals(
					AcctCdConstant.REGCOL_IRPOS))
				{
					getlblItem2().setText(laData.getPrdctSrvcDesc());
				}
				// Item3 = 'IRP Texas fee'
				else if (laData.getAcctItmCd().equals(
					AcctCdConstant.REGCOL_IRPTX))
				{
					getlblItem3().setText(laData.getPrdctSrvcDesc());
				}
			}
		}
		else
		{
		// end defect 11218
			
			getlblItem().setText(caSelectedRecord.getPrdctSrvcDesc());
			gettxtQty().setText("1");
			if (caSelectedRecord.getAcctItmCd() == null
				|| caSelectedRecord.getAcctItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				gettxtPrice().setEnabled(false);
				getstcLblPrice().setEnabled(false);
				gettxtPrice().setText(
					caSelectedRecord.getPrice().toString());
			}
			else
			{
				gettxtPrice().setEnabled(true);
				getstcLblPrice().setEnabled(true);
				gettxtPrice().setText(
					caSelectedRecord.getPrice().toString());
			}

			if (caSelectedRecord.getFxdChrgIndi() != 0)
			{
				gettxtPrice().setEnabled(false);
				getstcLblPrice().setEnabled(false);
				gettxtPrice().setText(
					caSelectedRecord.getPrice().toString());
			}
			else
			{
				gettxtPrice().setEnabled(true);
				getstcLblPrice().setEnabled(true);
				gettxtPrice().setText(
					caSelectedRecord.getPrice().toString());
			}
			// defect 9522 
			// Print out ItemCode if in Development Mode 
			if (SystemProperty.getProdStatus()
					== SystemProperty.APP_DEV_STATUS)
			{
				System.out.println(caSelectedRecord.getItmCd());
			}

			if (caSelectedRecord.getYrReqdIndi() != 0)
			{
				gettxtYear().setVisible(true);
				getstcLblYear().setVisible(true);

				// Use new method to determine next valid year
				// Returns maximum year where all years <= Current Year, 
				// e.g. 5APPRTR  (Z-5 Year Apportioned Trailer) 
				int liYear =
					InventoryPatternsCache.getNextValidYear(
						caSelectedRecord.getItmCd());
				if (liYear != Integer.MIN_VALUE)
				{
					gettxtYear().setText(Integer.toString(liYear));
				}
			}
			// end defect 9522 
			else
			{
				gettxtYear().setVisible(false);
				getstcLblYear().setVisible(false);
			}
		// defect 11218
		}
		// end defect 11218
		
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
