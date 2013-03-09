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
 * FrmPlaceItemsOnHoldINV018.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/17/2002	Defect CQU100004634 rearranged INV018 screen.
 * Min Wang  	01/30/2003  modify enableDisableYear(). 
 * 							defect 5294
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify setcomboItmCdDesc()
 * 							Ver 5.2.0
 * Min Wang		06/25/2004	Fix Year field is red when it is greyed out.
 *							modified enableDisableYear()
 *							defect 6486 Ver 5.2.1 
 * Ray Rowehl	02/22/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	03/23/2004	Get VAJ BuilderData from old rtsII vob.
 * 							May not match current gui objects.
 * 							defect 6976 Ver 5.2.3
 * Ray Rowehl	03/28/2005	Remove setNextFocusable's
 * 							Also rename the setVisible method.
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							defect 7890 Ver 5.2.3
 * Min Wang		03/15/2005	Change to use new isCalcInv() instead of 
 * 							old getCalcInv().
 * 							modified setData()	
 * 							defect 6370 Ver 5.2.3	
 * Min Wang		06/07/2004  Remove second else-if and relaced with just
 * 							else.  The if statement is for a boolean.
 * 							modify setData()
 * 							defect 8221	Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							add TXT_ITEM_DESC
 * 							modify captureUserInput(), enableDisableYear(),
 * 							getstcLblItmCdDesc(), setcomboItmCdDesc()	
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/12/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/15/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboItmCdDesc()
 * 							defect 8479 Ver 5.2.3 
 * K Harrell	03/09/2007	Implement SystemProperty.isCounty()
 * 							delete getBuilderData()
 * 							modify setcomboItmCdDesc()
 * 							defect 9085 Ver Special Plates     
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV018 prompts for inventory items to be placed on user hold.
 *
 * @version Special Plates	03/09/2007 
 * @author	Charlie Walker
 * <br>Creation Date:		06/27/2001
 */

public class FrmPlaceItemsOnHoldINV018
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblEndNo = null;
	private RTSInputField ivjtxtEndNo = null;
	private JComboBox ivjcomboItmCdDesc = null;
	private JLabel ivjstcLblBegNo = null;
	private JLabel ivjstcLblItmCdDesc = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblYr = null;
	private RTSInputField ivjtxtBegNo = null;
	private RTSInputField ivjtxtQty = null;
	private RTSInputField ivjtxtYr = null;
	private JPanel ivjFrmPlaceItemsOnHoldINV018ContentPane1 = null;

	/**
	 * Vector used to store the inventory item codes returned from cache
	 */
	private Vector cvItmCdsData = new Vector();

	/**
	 * Vector used to store the inventory patterns returned from cache
	 */
	private Vector cvInvPatrns = new Vector();

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * Flag to check if exception thrown in the server business layer
	 */
	public boolean cbExThrown = false;

	/**
	 * A hashtable that contains all of the frame's components that 
	 * take input.
	 * (Used for exception handling.)
	 */
	private Hashtable chtFrmComponents = new Hashtable();

	/**
	 * FrmPlaceItemsOnHoldINV018 constructor comment.
	 */
	public FrmPlaceItemsOnHoldINV018()
	{
		super();
		initialize();
	}

	/**
	 * FrmPlaceItemsOnHoldINV018 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmPlaceItemsOnHoldINV018(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPlaceItemsOnHoldINV018 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPlaceItemsOnHoldINV018(JFrame aaParent)
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

			// Determines what actions to take when Enter, Cancel, 
			// or Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				captureUserInput();

				// Validate the screen.
				CommonInvScrnProcsng laCISP =
					new CommonInvScrnProcsng();
				if (laCISP
					.invScrnValidation(
						this,
						chtFrmComponents,
						caInvAlloctnUIData))
					return;

				// Reset the exception throw flag to false.
				cbExThrown = false;
				getController().processData(
					AbstractViewController.ENTER,
					caInvAlloctnUIData);
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
					null);

			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV018);
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
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		lsStr = (String) getcomboItmCdDesc().getSelectedItem();
		caInvAlloctnUIData.setItmCdDesc(lsStr);
		// defect 8269
		//lsStr = lsStr.substring(0, lsStr.indexOf(" "));
		//caInvAlloctnUIData.setItmCd(lsStr);
		for (Iterator laItemSearch = cvItmCdsData.iterator();
			laItemSearch.hasNext();
			)
		{
			ItemCodesData laTempItemCode =
				(ItemCodesData) laItemSearch.next();
			if (laTempItemCode.getItmCdDesc().equals(lsStr))
			{
				caInvAlloctnUIData.setItmCd(laTempItemCode.getItmCd());
				break;
			}
		}
		// end defect 8269

		lsStr = gettxtYr().getText();
		if (lsStr.length() != 0)
		{
			caInvAlloctnUIData.setInvItmYr(Integer.parseInt(lsStr));
		}
		else
		{
			caInvAlloctnUIData.setInvItmYr(0);
		}

		lsStr = gettxtQty().getText();
		if (lsStr.length() != 0)
		{
			caInvAlloctnUIData.setInvQty(Integer.parseInt(lsStr));
		}
		else
		{
			caInvAlloctnUIData.setInvQty(0);
		}

		lsStr = gettxtBegNo().getText();
		if (lsStr.length() != 0)
		{
			caInvAlloctnUIData.setInvItmNo(lsStr);
		}
		else
		{
			caInvAlloctnUIData.setInvItmNo(
				CommonConstant.STR_SPACE_EMPTY);
		}

		lsStr = gettxtEndNo().getText();
		if (lsStr.length() != 0)
		{
			caInvAlloctnUIData.setInvItmEndNo(lsStr);
		}
		else
		{
			caInvAlloctnUIData.setInvItmEndNo(
				CommonConstant.STR_SPACE_EMPTY);
		}

		caInvAlloctnUIData.setInvStatusCd(1);
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
		RTSException leRTSEx = new RTSException(lsMsgType, lsMsg, null);

		// Set the position of the confirmation box so it doesn't 
		// cover the field entries
		int liFrmVertPos = (int) this.getLocation().getY();
		leRTSEx.setMsgLoc(
			RTSException.CENTER_HORIZONTAL,
			liFrmVertPos - 200);

		liYesNo = leRTSEx.displayError(this);

		if (liYesNo == RTSException.YES)
		{
			caInvAlloctnUIData.setAllocatedData(true);
			ProcessInventoryData laPIData = new ProcessInventoryData();
			laPIData =
				laPIData.convertFromInvAlloctnData(caInvAlloctnUIData);

			getController().processData(
				VCPlaceItemsOnHoldINV018.YES,
				laPIData);
		}
		else if (liYesNo == RTSException.NO)
		{
			caInvAlloctnUIData.setCalcInv(false);
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
						clearAllColor(gettxtYr());
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(185, 99, 260, 57);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// defect 7890
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7890
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjcomboItmCdDesc.setMaximumRowCount(
					CommonConstant.ELEMENT_4);
				ivjcomboItmCdDesc.setBackground(java.awt.Color.white);
				ivjcomboItmCdDesc.setBounds(10, 48, 297, 23);
				//ivjcomboItmCdDesc.setNextFocusableComponent(gettxtYr());
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
	 * Return the FrmAllocateItemINV013ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmPlaceItemsOnHoldINV018ContentPane1()
	{
		if (ivjFrmPlaceItemsOnHoldINV018ContentPane1 == null)
		{
			try
			{
				ivjFrmPlaceItemsOnHoldINV018ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmPlaceItemsOnHoldINV018ContentPane1.setName(
					"FrmPlaceItemsOnHoldINV018ContentPane1");
				ivjFrmPlaceItemsOnHoldINV018ContentPane1
					.setPreferredSize(
					new java.awt.Dimension(653, 176));
				ivjFrmPlaceItemsOnHoldINV018ContentPane1.setLayout(
					null);
				ivjFrmPlaceItemsOnHoldINV018ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(653, 176));
				ivjFrmPlaceItemsOnHoldINV018ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(653, 176));
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getcomboItmCdDesc(),
					getcomboItmCdDesc().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					gettxtYr(),
					gettxtYr().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					gettxtQty(),
					gettxtQty().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					gettxtBegNo(),
					gettxtBegNo().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getFrmPlaceItemsOnHoldINV018ContentPane1().add(
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
		return ivjFrmPlaceItemsOnHoldINV018ContentPane1;
	}

	/**
	 * Return the stcLblBeginNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjstcLblBegNo.setBounds(435, 35, 90, 14);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjstcLblEndNo.setBounds(537, 35, 90, 14);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblItmCdDesc()
	{
		if (ivjstcLblItmCdDesc == null)
		{
			try
			{
				ivjstcLblItmCdDesc = new JLabel();
				ivjstcLblItmCdDesc.setName("stcLblItmCdDesc");
				// defect 8269
				//ivjstcLblItmCdDesc.setText("Item Code - Description");
				ivjstcLblItmCdDesc.setText(
					InventoryConstant.TXT_ITEM_DESCRIPTION);
				// end defect 8269
				ivjstcLblItmCdDesc.setMaximumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setMinimumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setBounds(14, 35, 240, 14);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjstcLblQty.setBounds(366, 35, 56, 14);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjstcLblYr.setBounds(318, 35, 35, 14);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjtxtBegNo.setBounds(434, 50, 94, 20);
				//ivjtxtBegNo.setNextFocusableComponent(gettxtEndNo());
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjtxtEndNo.setBounds(536, 50, 94, 20);
				//ivjtxtEndNo.setNextFocusableComponent(
				//	getButtonPanel1());
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjtxtQty.setText(CommonConstant.STR_ZERO);
				ivjtxtQty.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtQty.setBounds(365, 50, 61, 20);
				//ivjtxtQty.setNextFocusableComponent(gettxtBegNo());
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
				ivjtxtYr.setBounds(317, 50, 39, 20);
				//ivjtxtYr.setNextFocusableComponent(gettxtQty());
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
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		///* Uncomment the following lines to print uncaught exceptions
		// *  to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
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
			setName(ScreenConstant.INV018_FRAME_NAME);
			setSize(648, 171);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV018_FRAME_TITLE);
			setContentPane(getFrmPlaceItemsOnHoldINV018ContentPane1());
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
	 * If Item Code is plp, rop, or oldplt, set the Quantity field as 
	 * appropriate.
	 * 
	 * @param asItmCd String
	 */
	private void isPLP(String asItmCd)
	{

		if (!ValidateInventoryPattern
			.chkIfItmPLPOrOLDPLTOrROP(asItmCd))
		{
			getstcLblQty().setEnabled(true);
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

	// defect 7890
	//	/**
	//	 * Determines which radio button is currently selected.  
	//	 * Then depending on which arrow key is pressed, it sets that 
	//	 * radio button selected and requests focus.
	//	 *
	//	 * @param aaKE KeyEvent 
	//	 */
	//	public void keyPressed(KeyEvent aaKE)
	//	{
	//		super.keyPressed(aaKE);
	//
	//		if (aaKE.getSource() instanceof RTSButton)
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
	//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
	//			{
	//				if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
	//			{
	//				if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//		}
	//	}
	// end defect 7890

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
			FrmAllocateItemINV013 laFrmAllocateItemINV013;
			laFrmAllocateItemINV013 = new FrmAllocateItemINV013();
			laFrmAllocateItemINV013.setModal(true);
			laFrmAllocateItemINV013
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmAllocateItemINV013.show();
			java.awt.Insets insets =
				laFrmAllocateItemINV013.getInsets();
			laFrmAllocateItemINV013.setSize(
				laFrmAllocateItemINV013.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAllocateItemINV013.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAllocateItemINV013.setVisibleRTS(true);
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
	 * Setup the ComboBox for Item Code Desc.
	 */
	private void setcomboItmCdDesc()
	{
		Vector lvSort = new Vector();
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
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

			// Check to see if a regional office
			//OfficeIdsData laOID =
			//	OfficeIdsCache.getOfcId(
			//		SystemProperty.getOfficeIssuanceNo());
			//if (laOID.getOfcIssuanceCd() == 2)
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
			// defect 8269
			//lsStr = laItemCodesData.getItmCd();
			//lsStr += " - ";
			//lsStr += laItemCodesData.getItmCdDesc();
			lsStr = laItemCodesData.getItmCdDesc();
			// end defect 8269
			lvSort.addElement(lsStr);
		}

		UtilityMethods.sort(lvSort);

		ComboBoxModel laComboModel = new DefaultComboBoxModel(lvSort);
		getcomboItmCdDesc().setModel(laComboModel);

		getcomboItmCdDesc().setSelectedItem(
			InventoryConstant.DEFAULT_SELECTION);
		// defect 8479
		comboBoxHotKeyFix(getcomboItmCdDesc());
		// end defect 8479
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		caInvAlloctnUIData =
			(InventoryAllocationUIData) UtilityMethods.copy(aaData);
		//defect 6370
		if (!caInvAlloctnUIData.isCalcInv())
		{
			// end defect 6370
			setcomboItmCdDesc();
			gettxtQty().setText(CommonConstant.STR_ZERO);
			return;
			// defect 6370
		}
		//defect 8221
		//else if (caInvAlloctnUIData.isCalcInv())
		else
		{
			// end defect 8221
			// end defect 6370
			gettxtBegNo().setText(caInvAlloctnUIData.getInvItmNo());
			gettxtEndNo().setText(caInvAlloctnUIData.getInvItmEndNo());
			gettxtQty().setText(
				String.valueOf(caInvAlloctnUIData.getInvQty()));
			gettxtYr().setText(
				String.valueOf(caInvAlloctnUIData.getInvItmYr()));
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
