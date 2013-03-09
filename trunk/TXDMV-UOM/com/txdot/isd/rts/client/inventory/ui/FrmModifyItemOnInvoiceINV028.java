package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.inventory.business.CommonInvScrnProcsng;

import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * FrmModifyItemOnInvoiceINV028.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang     12/03/2003  Set InvItmNo to "" to force msg 11 
 *							reorganized the header of class
 *							modify captureUserInput()
 *                          defect 6505. Ver 5.1.5 Fix 2
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify setcomboItmCdDesc()
 * 							Ver 5.2.0
 * Min Wang		06/25/2004	Fix Year field is red when it is greyed out.
 *							modified enableDisableYear()
 *							defect 6486 Ver 5.2.1 
 * Ray Rowehl	02/22/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename to reflect frame number INV028
 * 							modify actionPerformed()
 * 							defect 6965 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							add TXT_ITEM_DESC
 * 							modify captureUserInput(), 
 * 							enableDisableYear(),
 * 							setcomboItmCdDesc(), setData()	
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/12/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing on button panel
 * 							defect 7890 Ver 5.2.3  
 * Ray Rowehl	08/15/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3 
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setData()
 * 							defect 8479 Ver 5.2.3   
 * K Harrell	03/09/2007	Implement SystemProperty.isCounty()
 * 							modify setcomboItmCdDesc()
 * 							defect 9085 Ver Special Plates    
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV028 allows for items on an invoice to be modified in the 
 * Receive Invoice event.
 *
 * @version Special Plates	03/09/2007
 * @author	Charlie Walker
 * <br>Creation Date:		08/03/2001 16:10:04 
 */

public class FrmModifyItemOnInvoiceINV028
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblEndNo = null;
	private RTSInputField ivjtxtEndNo = null;
	private JPanel ivjFrmModifyItemOnInvoiceINV028ContentPane1 = null;
	private JComboBox ivjcomboItmCdDesc = null;
	private JLabel ivjstcLblBegNo = null;
	private JLabel ivjstcLblItmCdDesc = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblYr = null;
	private RTSInputField ivjtxtBegNo = null;
	private RTSInputField ivjtxtQty = null;
	private RTSInputField ivjtxtYr = null;

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private MFInventoryAllocationData caMFInvAlloctnData =
		new MFInventoryAllocationData();

	/**
	 * Vector used to store the Substations for the combo box
	 */
	private Vector cvSubstaData = new Vector();

	/**
	 * Vector used to store the inventory item codes returned from cache
	 */
	private Vector cvItmCdsData = new Vector();

	/**
	 * Vector used to store the inventory patterns returned from cache
	 */
	private Vector cvInvPatrns = new Vector();

	/**
	 * Flag to check if exception thrown in the server business layer
	 */
	public boolean cbExThrown = false;

	/**
	 * Variable to store the original invoice line item quantity
	 */
	private int ciOrigInvItmQty = 0;

	/**
	 * Variable to store the original invoice line begin number
	 */
	private String csOrigInvItmNo = new String();

	/**
	 * Variable to store the original invoice line end number
	 */
	private String csOrigInvItmEndNo = new String();

	/**
	 * A hashtable that contains all of the frame's components that 
	 * take input.
	 * (Used for exception handling.)
	 */
	private Hashtable chtFrmComponents = new Hashtable();

	/**
	 * FrmModifyItemOnInvoiceINV028 constructor comment.
	 */
	public FrmModifyItemOnInvoiceINV028()
	{
		super();
		initialize();
	}

	/**
	 * FrmModifyItemOnInvoiceINV028 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmModifyItemOnInvoiceINV028(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmModifyItemOnInvoiceINV028 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmModifyItemOnInvoiceINV028(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// Returns all fields to their original color state
			clearAllColor(this);
			// Determines what actions to take when Enter, Cancel, or 
			// Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				captureUserInput();
				// Validate the screen.
				int liSelctdInvcItm =
					caMFInvAlloctnData.getSelctdRowIndx();
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						liSelctdInvcItm);
				CommonInvScrnProcsng laCISP =
					new CommonInvScrnProcsng();
				if (laCISP
					.invScrnValidation(this, chtFrmComponents, laIAUID))
				{
					return;
				}
				// Reset the exception throw flag to false.
				cbExThrown = false;
				caMFInvAlloctnData.setCalcInv(false);
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
				if (!cbExThrown)
				{
					dispConfirmationBox();
				}
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caMFInvAlloctnData);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV028);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the User's Input.
	 */
	private void captureUserInput()
	{
		InventoryAllocationUIData laInvAlloctnUIData =
			new InventoryAllocationUIData();
		laInvAlloctnUIData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laInvAlloctnUIData.setSubstaId(
			SystemProperty.getSubStationId());
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		lsStr = (String) getcomboItmCdDesc().getSelectedItem();
		//defect 8269
		//laInvAlloctnUIData.setItmCdDesc(lsStr);
		//lsStr = lsStr.substring(0, lsStr.indexOf(" "));
		//laInvAlloctnUIData.setItmCd(lsStr);
		for (Iterator laItemSearch = cvItmCdsData.iterator();
			laItemSearch.hasNext();
			)
		{
			ItemCodesData laTempItemCode =
				(ItemCodesData) laItemSearch.next();
			if (laTempItemCode.getItmCdDesc().equals(lsStr))
			{
				laInvAlloctnUIData.setItmCd(laTempItemCode.getItmCd());
				break;
			}
		}
		// end defect 8269
		lsStr = gettxtYr().getText();
		if (lsStr.length() != 0)
		{
			laInvAlloctnUIData.setInvItmYr(Integer.parseInt(lsStr));
		}
		else
		{
			laInvAlloctnUIData.setInvItmYr(0);
		}
		lsStr = gettxtQty().getText();
		if (lsStr.length() != 0)
		{
			laInvAlloctnUIData.setInvQty(Integer.parseInt(lsStr));
		}
		else
		{
			laInvAlloctnUIData.setInvQty(0);
		}
		lsStr = gettxtBegNo().getText().trim();
		if (lsStr.length() != 0)
		{
			laInvAlloctnUIData.setInvItmNo(lsStr);
		}
		else
		{
			// Set InvItmNo to "" to force msg 11 
			//lInvAlloctnUIData.setInvItmNo(0); 
			laInvAlloctnUIData.setInvItmNo(
				CommonConstant.STR_SPACE_EMPTY);
		}
		lsStr = gettxtEndNo().getText();
		if (lsStr.length() != 0)
		{
			laInvAlloctnUIData.setInvItmEndNo(lsStr);
		}
		else
		{
			laInvAlloctnUIData.setInvItmEndNo(
				CommonConstant.STR_SPACE_EMPTY);
		}
		int liSelctdInvcItm = caMFInvAlloctnData.getSelctdRowIndx();
		caMFInvAlloctnData.getInvAlloctnData().set(
			liSelctdInvcItm,
			laInvAlloctnUIData);
		caMFInvAlloctnData.setTransCd(InventoryConstant.MODFY);
	}

	/**
	 * Display the Confirmation Box.
	 */
	private void dispConfirmationBox()
	{
		String lsMsgType = new String();
		String lsMsg = new String();
		int liYesNo = 0;
		// Prompts the user if these are the correct values.
		lsMsgType = RTSException.CTL001;
		lsMsg = ErrorsConstant.MSG_ARE_VALUES_CORRECT;
		RTSException leRTSExMsg =
			new RTSException(lsMsgType, lsMsg, null);
		// Set the position of the confirmation box so it doesn't 
		// cover the field entries
		int liFrmVertPos = (int) this.getLocation().getY();
		leRTSExMsg.setMsgLoc(
			RTSException.CENTER_HORIZONTAL,
			liFrmVertPos - 200);
		liYesNo = leRTSExMsg.displayError(this);
		if (liYesNo == RTSException.YES)
		{
			caMFInvAlloctnData.setCalcInv(true);
			caMFInvAlloctnData.setTransCd(InventoryConstant.MODFY);
			Vector lvct = new Vector();
			lvct.addElement(cvSubstaData);
			lvct.addElement(caMFInvAlloctnData);
			// Save the original line item quantity, begin number, 
			// and end number
			int liSelctdInvcItm = caMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationUIData laIAUID =
				(InventoryAllocationUIData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdInvcItm);
			laIAUID.setOrigInvQty(ciOrigInvItmQty);
			laIAUID.setOrigInvItmNo(csOrigInvItmNo);
			laIAUID.setOrigInvItmEndNo(csOrigInvItmEndNo);
			getController().processData(
				VCModifyItemOnInvoiceINV028.YES,
				lvct);
		}
		else if (liYesNo == RTSException.NO)
		{
			caMFInvAlloctnData.setCalcInv(false);
			return;
		}
	}

	/**
	 * Enable / Disable the Year as appropriate.
	 */
	private void enableDisableYear()
	{
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		lsStr = (String) getcomboItmCdDesc().getSelectedItem();
		// defect 8269
		//lsStr = lsStr.substring(0, lsStr.indexOf(" "));
		InventoryPatternsData laInvPatt = null;
		RTSDate laDate = new RTSDate();
		for (Iterator laItemSearch = cvItmCdsData.iterator();
			laItemSearch.hasNext();
			)
		{
			ItemCodesData laTempItemCode =
				(ItemCodesData) laItemSearch.next();
			if (laTempItemCode.getItmCdDesc().equals(lsStr))
			{
				lsStr = laTempItemCode.getItmCd();
				break;
			}
		}
		// end defect 8269
		cvInvPatrns =
			InventoryPatternsCache.getInvPatrns(
				lsStr,
				InventoryPatternsCache.NO_YEAR);
		clearAllColor(this);
		boolean lbItmCdFound = false;
		if (cvInvPatrns != null)
		{
			for (int i = 0; i < cvInvPatrns.size(); i++)
			{
				laInvPatt =
					(InventoryPatternsData) cvInvPatrns.elementAt(i);
				if (lsStr.equals(laInvPatt.getItmCd()))
				{
					lbItmCdFound = true;
					if (laInvPatt.getInvItmYr() == 0)
					{
						gettxtYr().setText(CommonConstant.STR_ZERO);
						gettxtYr().setEnabled(false);
						getstcLblYr().setEnabled(false);
						isPLP(lsStr);
					}
					else
					{
						gettxtYr().setText(
							String.valueOf(laDate.getYear() + 1));
						gettxtYr().setEnabled(true);
						getstcLblYr().setEnabled(true);
						isPLP(lsStr);
					}
				}
				if (lbItmCdFound)
				{
					break;
				}
			}
		}
		if (!lbItmCdFound)
		{
			gettxtYr().setText(CommonConstant.STR_ZERO);
			gettxtYr().setEnabled(false);
			getstcLblYr().setEnabled(false);
			isPLP(lsStr);
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */

	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(153, 114, 244, 62);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the comboItemCodeDescription property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboItmCdDesc()
	{
		if (ivjcomboItmCdDesc == null)
		{
			try
			{
				ivjcomboItmCdDesc = new JComboBox();
				ivjcomboItmCdDesc.setName("comboItmCdDesc");
				ivjcomboItmCdDesc.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboItmCdDesc.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboItmCdDesc.setBackground(java.awt.Color.white);
				ivjcomboItmCdDesc.setMaximumRowCount(
					SwingConstants.RIGHT);
				ivjcomboItmCdDesc.setBounds(17, 53, 240, 23);
				// user code begin {1}
				getcomboItmCdDesc().addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboItmCdDesc;
	}

	/**
	 * Return the getFrmModifyItemOnInvoiceINV028ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */

	private JPanel getFrmModifyItemOnInvoiceINV028ContentPane1()
	{
		if (ivjFrmModifyItemOnInvoiceINV028ContentPane1 == null)
		{
			try
			{
				ivjFrmModifyItemOnInvoiceINV028ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmModifyItemOnInvoiceINV028ContentPane1.setName(
					"FrmModifyItemOnInvoiceINV015ContentPane1");
				ivjFrmModifyItemOnInvoiceINV028ContentPane1.setLayout(
					null);
				ivjFrmModifyItemOnInvoiceINV028ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(550, 180));
				ivjFrmModifyItemOnInvoiceINV028ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(550, 180));
				ivjFrmModifyItemOnInvoiceINV028ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getcomboItmCdDesc(),
					getcomboItmCdDesc().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					gettxtYr(),
					gettxtYr().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					gettxtQty(),
					gettxtQty().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					gettxtBegNo(),
					gettxtBegNo().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getFrmModifyItemOnInvoiceINV028ContentPane1().add(
					gettxtEndNo(),
					gettxtEndNo().getName());
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
		return ivjFrmModifyItemOnInvoiceINV028ContentPane1;
	}

	/**
	 * Return the stcLblBeginNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblBegNo()
	{
		if (ivjstcLblBegNo == null)
		{
			try
			{
				ivjstcLblBegNo = new JLabel();
				ivjstcLblBegNo.setName("stcLblBegNo");
				ivjstcLblBegNo.setText(InventoryConstant.TXT_BEGIN_NO);
				ivjstcLblBegNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setBounds(396, 35, 90, 14);
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
		return ivjstcLblBegNo;
	}

	/**
	 * Return the stcLblEndNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEndNo()
	{
		if (ivjstcLblEndNo == null)
		{
			try
			{
				ivjstcLblEndNo = new JLabel();
				ivjstcLblEndNo.setName("stcLblEndNo");
				ivjstcLblEndNo.setText(InventoryConstant.TXT_END_NO);
				ivjstcLblEndNo.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjstcLblEndNo.setMinimumSize(
					new java.awt.Dimension(39, 14));
				ivjstcLblEndNo.setBounds(500, 35, 90, 14);
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
		return ivjstcLblEndNo;
	}

	/**
	 * Return the stcLblItemCodeDescription property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblItmCdDesc()
	{
		if (ivjstcLblItmCdDesc == null)
		{
			try
			{
				ivjstcLblItmCdDesc = new JLabel();
				ivjstcLblItmCdDesc.setName("stcLblItmCdDesc");
				ivjstcLblItmCdDesc.setText(
					InventoryConstant.TXT_ITEM_DESCRIPTION);
				ivjstcLblItmCdDesc.setMaximumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setMinimumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setBounds(17, 35, 240, 14);
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
		return ivjstcLblItmCdDesc;
	}

	/**
	 * Return the stcLblQuantity property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblQty()
	{
		if (ivjstcLblQty == null)
		{
			try
			{
				ivjstcLblQty = new JLabel();
				ivjstcLblQty.setName("stcLblQty");
				ivjstcLblQty.setText(InventoryConstant.TXT_QUANTITY);
				ivjstcLblQty.setMaximumSize(
					new java.awt.Dimension(47, 14));
				ivjstcLblQty.setMinimumSize(
					new java.awt.Dimension(47, 14));
				ivjstcLblQty.setBounds(325, 35, 56, 14);
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
	 * @return JLabel
	 */

	private JLabel getstcLblYr()
	{
		if (ivjstcLblYr == null)
		{
			try
			{
				ivjstcLblYr = new JLabel();
				ivjstcLblYr.setName("stcLblYr");
				ivjstcLblYr.setText(InventoryConstant.TXT_YEAR);
				ivjstcLblYr.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYr.setMinimumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYr.setBounds(275, 35, 35, 14);
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
		return ivjstcLblYr;
	}

	/**
	 * Return the txtBeginNo property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtBegNo()
	{
		if (ivjtxtBegNo == null)
		{
			try
			{
				ivjtxtBegNo = new RTSInputField();
				ivjtxtBegNo.setName("txtBegNo");
				ivjtxtBegNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtBegNo.setInput(RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtBegNo.setBounds(396, 54, 90, 20);
				ivjtxtBegNo.setMaxLength(
					InventoryConstant.MAX_ITEM_LENGTH);
				ivjtxtBegNo.setHorizontalAlignment(
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
		return ivjtxtBegNo;
	}

	/**
	 * Return the txtEndNo property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtEndNo()
	{
		if (ivjtxtEndNo == null)
		{
			try
			{
				ivjtxtEndNo = new RTSInputField();
				ivjtxtEndNo.setName("txtEndNo");
				ivjtxtEndNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEndNo.setInput(RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtEndNo.setBounds(500, 54, 90, 20);
				ivjtxtEndNo.setMaxLength(
					InventoryConstant.MAX_ITEM_LENGTH);
				ivjtxtEndNo.setHorizontalAlignment(
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
		return ivjtxtEndNo;
	}

	/**
	 * Return the txtQuantity property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtQty()
	{
		if (ivjtxtQty == null)
		{
			try
			{
				ivjtxtQty = new RTSInputField();
				ivjtxtQty.setName("txtQty");
				ivjtxtQty.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtQty.setInput(1);
				ivjtxtQty.setBounds(325, 54, 57, 20);
				ivjtxtQty.setMaxLength(
					InventoryConstant.MAX_QTY_LENGTH);
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
	 * Return the txtYear property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtYr()
	{
		if (ivjtxtYr == null)
		{
			try
			{
				ivjtxtYr = new RTSInputField();
				ivjtxtYr.setName("txtYr");
				ivjtxtYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYr.setBounds(275, 54, 35, 20);
				ivjtxtYr.setMaxLength(CommonConstant.LENGTH_YEAR);
				ivjtxtYr.setHorizontalAlignment(
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
		return ivjtxtYr;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException  Throwable
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

	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.INV028_FRAME_NAME);
			setSize(610, 180);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV028_FRAME_TITLE);
			setContentPane(
				getFrmModifyItemOnInvoiceINV028ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// Create the hashtable of input components for use in 
		// exception handling
		chtFrmComponents.put(
			InventoryConstant.ITM_CD_DESC,
			getcomboItmCdDesc());
		chtFrmComponents.put(InventoryConstant.YR, gettxtYr());
		chtFrmComponents.put(InventoryConstant.QTY, gettxtQty());
		chtFrmComponents.put(InventoryConstant.BEG_NO, gettxtBegNo());
		chtFrmComponents.put(InventoryConstant.END_NO, gettxtEndNo());
		// user code end
	}

	/**
	 * Check to see if plate is plp, rop, oldplt and set up 
	 * the quantity field as appropriate.
	 * 
	 * @param asItmCd String
	 */
	private void isPLP(String asItmCd)
	{
		if (!ValidateInventoryPattern
			.chkIfItmPLPOrOLDPLTOrROP(asItmCd))
		{
			getstcLblQty().setEnabled(true);
			gettxtQty().setText(String.valueOf(ciOrigInvItmQty));
			gettxtQty().setEnabled(true);
		}
		else
		{
			getstcLblQty().setEnabled(false);
			gettxtQty().setText(InventoryConstant.STR_PLP_QTY);
			gettxtQty().setEnabled(false);
		}
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		if (aaIE.getSource() == getcomboItmCdDesc())
		{
			enableDisableYear();
		}
	}
	
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmModifyItemOnInvoiceINV028 laFrmModifyItemOnInvoiceINV028;
			laFrmModifyItemOnInvoiceINV028 =
				new FrmModifyItemOnInvoiceINV028();
			laFrmModifyItemOnInvoiceINV028.setModal(true);
			laFrmModifyItemOnInvoiceINV028
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmModifyItemOnInvoiceINV028.show();
			java.awt.Insets insets =
				laFrmModifyItemOnInvoiceINV028.getInsets();
			laFrmModifyItemOnInvoiceINV028.setSize(
				laFrmModifyItemOnInvoiceINV028.getWidth()
					+ insets.left
					+ insets.right,
				laFrmModifyItemOnInvoiceINV028.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmModifyItemOnInvoiceINV028.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Takes the exception thrown from the server, and determines which
	 * field should turn red and receive focus.
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void procsExcptn(RTSException aeRTSEx)
	{
		CommonInvScrnProcsng laCISP = new CommonInvScrnProcsng();
		laCISP.hndlServerExcptns(this, chtFrmComponents, aeRTSEx);
	}

	/**
	 * Setup the Item Codes ComboBox.
	 */
	private void setcomboItmCdDesc()
	{
		Vector lvSort = new Vector();
		String lsStr = new String("");
		ItemCodesData laItemCodesData = new ItemCodesData();
		try
		{
			cvItmCdsData.addAll(
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_NORMAL,
					CommonConstant.STR_SPACE_EMPTY,
					true));
			cvItmCdsData.addAll(
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_SPECIAL,
					CommonConstant.STR_SPACE_EMPTY,
					true));
			// defect 9085
			//if (!UtilityMethods.isCounty())
			if (!SystemProperty.isCounty())
			{
				cvItmCdsData.addAll(
					ItemCodesCache.getItmCds(
						ItemCodesCache.PROCSNGCD,
						InventoryConstant.INV_PROCSNGCD_RESTRICTED,
						CommonConstant.STR_SPACE_EMPTY,
						true));
			}
			// end defect 9085 
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}

		for (int i = 0; i < cvItmCdsData.size(); i++)
		{
			lsStr = CommonConstant.STR_SPACE_EMPTY;
			laItemCodesData = (ItemCodesData) cvItmCdsData.elementAt(i);
			lsStr = laItemCodesData.getItmCd();
			// defect 8269
			//lsStr = lsStr + " - ";
			//lsStr = lsStr + laItemCodesData.getItmCdDesc();
			lsStr = laItemCodesData.getItmCdDesc();
			// end defect 8269
			lvSort.addElement(lsStr);
		}
		UtilityMethods.sort(lvSort);
		ComboBoxModel laComboModel = new DefaultComboBoxModel(lvSort);
		getcomboItmCdDesc().setModel(laComboModel);
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		Vector lvDataIn = (Vector) aaData;
		cvSubstaData =
			(Vector) lvDataIn.elementAt(CommonConstant.ELEMENT_0);
		caMFInvAlloctnData =
			(MFInventoryAllocationData) UtilityMethods.copy(
				lvDataIn.get(CommonConstant.ELEMENT_1));
		if (!caMFInvAlloctnData.getCalcInv())
		{
			setcomboItmCdDesc();
			// Display the selected item from INV002
			int liSelctdInvcItm = caMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationData laInvAlloctnData =
				(InventoryAllocationData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdInvcItm);
			ItemCodesData laICD =
				ItemCodesCache.getItmCd(laInvAlloctnData.getItmCd());
			if (laICD == null)
			{
				getcomboItmCdDesc().setSelectedIndex(
					CommonConstant.ELEMENT_0);
			}
			else
			{
				// defect 8269
				//getcomboItmCdDesc().setSelectedItem(
				//	laICD.getItmCd() + " - " + laICD.getItmCdDesc());
				getcomboItmCdDesc().setSelectedItem(
					laICD.getItmCdDesc());
				// end defect 8269
			}
			// defect 8479
			comboBoxHotKeyFix(getcomboItmCdDesc());
			// end defect 8479
			gettxtYr().setText(
				String.valueOf(laInvAlloctnData.getInvItmYr()));
			gettxtQty().setText(
				String.valueOf(laInvAlloctnData.getInvQty()));
			gettxtBegNo().setText(laInvAlloctnData.getInvItmNo());
			gettxtEndNo().setText(laInvAlloctnData.getInvItmEndNo());
			// Store the original line item quantity, begin number, 
			// and end number
			ciOrigInvItmQty = laInvAlloctnData.getInvQty();
			csOrigInvItmNo = laInvAlloctnData.getInvItmNo();
			csOrigInvItmEndNo = laInvAlloctnData.getInvItmEndNo();
			return;
		}
		else if (caMFInvAlloctnData.getCalcInv())
		{
			int liSelctdIndx = caMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationData laInvAlloctnData =
				(InventoryAllocationData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdIndx);
			gettxtBegNo().setText(laInvAlloctnData.getInvItmNo());
			gettxtEndNo().setText(laInvAlloctnData.getInvItmEndNo());
			gettxtQty().setText(
				String.valueOf(laInvAlloctnData.getInvQty()));
			gettxtYr().setText(
				String.valueOf(laInvAlloctnData.getInvItmYr()));
		}
	}

	/**
	 * Sets the frame location as centered horizontally and one third 
	 * from the bottom of the screen.
	 * 
	 * @param abVisible boolean
	 */
	public void setVisibleRTS(boolean abVisible)
	{
		if (abVisible)
		{
			setManagingLocation(true);
			// Set the position of the frame so the field entries are 
			// visible when the confirmation box is displayed
			int liFrmHorzPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.width
					/ 2
					- getSize().width / 2);
			int liFrmVertPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.height
					* 2
					/ 3
					- getSize().height / 2);
			this.setLocation(liFrmHorzPos, liFrmVertPos);
		}
		super.setVisibleRTS(abVisible);
	}
}
