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

import com.txdot.isd.rts.services.cache.DeleteReasonsCache;
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
 * FrmInventoryDeleteINV006.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		05/02/2002	Defect 3760 Lengthen Item Desc field
 * Min Wang		05/14/2002 	(CQU3765) Fixed when the tab for up and 
 *							down arrows is selected on second JComboBox, 
 *							the inv006 screen looses focus.
 * Min Wang					Modified gettxtReasonDesc() and 
 * 							captureUserInput(). Defect CQU100005091.
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Version 5.2.0
 * Min Wang		06/25/2004	Fix Year field is red when it is greyed out.
 *							modified enableDisableYear()
 *							defect 6486 Ver 5.2.1 
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/29/2005	Remove setNextFocusables
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3   
 * Min Wang		08/01/2005	Remove itme code from screen.
 * 							add TXT_ITEM_DESC
 * 							modify captureUserInput(), 
 * 							getstcLblItmCdDesc(),
 * 							setcomboItmCdDesc()
 * 							defect 8269 Ver 5.2.2 Fix 6  
 * Ray Rowehl	08/09/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Remove key processing for button panel.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3	
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboItmCdDesc(), setData()
 * 							defect 8479 Ver 5.2.3 
 * K Harrell	03/09/2007	Implement SystemProperty.isCounty()
 * 							modify procsExcptn()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV006 prompts for which inventory items should be deleted from
 * the database and why in the Delete event.
 *
 * @version Special Plates	03/09/2007
 * @author	Charlie Walker
 * <br>Creation Date:		06/27/2001 15:34:02
 */

public class FrmInventoryDeleteINV006
	extends RTSDialogBox
	implements ActionListener, ItemListener //, KeyListener
{
	private static final int DELETE_RESN_5 = 5;
	private static final int DELETE_RESN_8 = 8;
	private static final int MAX_RESN_CD_COUNT = 1;
	private static final int MAX_RESN_CD_DESC_LENGTH = 110;

	private JLabel ivjstcLblEndNo = null;
	private RTSInputField ivjtxtEndNo = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JComboBox ivjcomboReasnCd = null;
	private JLabel ivjstcLblBegNo = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblReasnCd = null;
	private JLabel ivjstcLblReasnDesc = null;
	private JLabel ivjstcLblYr = null;
	private RTSInputField ivjtxtBegNo = null;
	private RTSInputField ivjtxtQty = null;
	private RTSInputField ivjtxtReasnDesc = null;
	private RTSInputField ivjtxtYr = null;
	private JPanel ivjFrmInventoryDeleteINV006ContentPane1 = null;
	private JComboBox ivjcomboItmCdDesc = null;
	private JLabel ivjstcLbltmCdDesc = null;

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * Vector used to store the inventory item codes returned from cache
	 */
	private Vector cvItmCdsData = new Vector();

	/**
	 * Vector used to store the inventory patterns returned from cache
	 */
	private Vector cvInvPatrns = new Vector();

	/**
	 * Vector used to store the delete reasons returned from cache
	 */
	private Vector cvDelReasns = new Vector();

	/**
	 * Flag to check if exception thrown in the server business layer
	 */
	public boolean cbExThrown = false;

	/**
	 * A hashtable that contains all of the frame's components that take input.
	 * (Used for exception handling.)
	 */
	private Hashtable chtFrmComponents = new Hashtable();

	/**
	 * FRMInventoryDeleteINV006 constructor comment.
	 */
	public FrmInventoryDeleteINV006()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryDeleteINV006 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryDeleteINV006(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryDeleteINV006 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryDeleteINV006(JFrame aaParent)
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
			//  or Help are pressed.
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
				caInvAlloctnUIData.setCalcInv(false);
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
					caInvAlloctnUIData);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV006);
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
		caInvAlloctnUIData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caInvAlloctnUIData.setSubstaId(
			SystemProperty.getSubStationId());
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		lsStr = (String) getcomboItmCdDesc().getSelectedItem();

		// defect 8269
		caInvAlloctnUIData.setItmCdDesc(lsStr);
		//lsStr = lsStr.substring(0, lsStr.indexOf(" "));
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
			caInvAlloctnUIData.setInvItmNo(CommonConstant.STR_ZERO);
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
		lsStr = gettxtReasnDesc().getText().trim();
		if (lsStr.length() != 0)
		{
			caInvAlloctnUIData.setDelInvReasnTxt(lsStr);
		}
		else
		{
			caInvAlloctnUIData.setDelInvReasnTxt(
				CommonConstant.STR_SPACE_EMPTY);
		}
		lsStr = CommonConstant.STR_SPACE_EMPTY;
		lsStr = (String) getcomboReasnCd().getSelectedItem();
		for (int i = 0; i < cvDelReasns.size(); i++)
		{
			DeleteReasonsData laDRD =
				(DeleteReasonsData) cvDelReasns.get(i);
			if (laDRD.getDelInvReasn().equals(lsStr))
			{
				caInvAlloctnUIData.setDelInvReasnCd(
					laDRD.getDelInvReasnCd());
			}
		}
	}

	/**
	 * Display Confirmation Message.
	 */
	private void dispConfirmationBox()
	{
		String lsMsgType = new String();
		String lsMsg = new String();
		int liYesNo = 0;
		// Prompts the user if these are the correct values.
		lsMsgType = RTSException.CTL001;
		lsMsg = ErrorsConstant.MSG_INV_DELETE_CONFIRM;
		RTSException leRTSExMsg =
			new RTSException(lsMsgType, lsMsg, null);
		// Set the position of the confirmation box so it doesn't 
		// cover the field entries
		int liFrmVertPos = (int) this.getLocation().getY();
		leRTSExMsg.setMsgLoc(
			RTSException.CENTER_HORIZONTAL,
			liFrmVertPos - 125);
		liYesNo = leRTSExMsg.displayError(this);
		if (liYesNo == RTSException.YES)
		{
			// Have to add the element to a vector b/c potentially can 
			// have multiple item ranges when leaving INV025.
			Vector lvDataOut = new Vector(CommonConstant.ELEMENT_1);
			lvDataOut.addElement(caInvAlloctnUIData);
			getController().processData(
				VCInventoryDeleteINV006.YES,
				lvDataOut);
		}
		else if (liYesNo == RTSException.NO)
		{
			caInvAlloctnUIData.setCalcInv(false);
			return;
		}
	}

	/**
	 * Enable / Disable the year field as appropriate.
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(138, 215, 282, 60);
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
	 * Return the comboItemCode property value.
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
				ivjcomboItmCdDesc.setBackground(java.awt.Color.white);
				ivjcomboItmCdDesc.setMaximumRowCount(DELETE_RESN_5);
				ivjcomboItmCdDesc.setBounds(12, 121, 297, 23);
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
	 * Return the comboReasonCode property value.
	 * 
	 * @return JComboBox
	 */
	
	private JComboBox getcomboReasnCd()
	{
		if (ivjcomboReasnCd == null)
		{
			try
			{
				ivjcomboReasnCd = new JComboBox();
				ivjcomboReasnCd.setName("comboReasnCd");
				ivjcomboReasnCd.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboReasnCd.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboReasnCd.setBackground(java.awt.Color.white);
				ivjcomboReasnCd.setMaximumRowCount(MAX_RESN_CD_COUNT);
				ivjcomboReasnCd.setBounds(105, 21, 302, 23);
				//ivjcomboReasnCd.setNextFocusableComponent(
				//	gettxtReasnDesc());
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
		return ivjcomboReasnCd;
	}

	/**
	 * Return the FRMInventoryDeleteINV006ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	
	private JPanel getFrmInventoryDeleteINV006ContentPane1()
	{
		if (ivjFrmInventoryDeleteINV006ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryDeleteINV006ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryDeleteINV006ContentPane1.setName(
					"FrmInventoryDeleteINV006ContentPane1");
				ivjFrmInventoryDeleteINV006ContentPane1.setLayout(null);
				ivjFrmInventoryDeleteINV006ContentPane1.setMaximumSize(
					new java.awt.Dimension(628, 250));
				ivjFrmInventoryDeleteINV006ContentPane1.setMinimumSize(
					new java.awt.Dimension(628, 250));
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblReasnCd(),
					getstcLblReasnCd().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getcomboReasnCd(),
					getcomboReasnCd().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblReasnDesc(),
					getstcLblReasnDesc().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					gettxtReasnDesc(),
					gettxtReasnDesc().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getcomboItmCdDesc(),
					getcomboItmCdDesc().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					gettxtYr(),
					gettxtYr().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					gettxtQty(),
					gettxtQty().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					gettxtBegNo(),
					gettxtBegNo().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					gettxtEndNo(),
					gettxtEndNo().getName());
				getFrmInventoryDeleteINV006ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmInventoryDeleteINV006ContentPane1;
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
				ivjstcLblBegNo.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblBegNo.setBounds(430, 102, 90, 14);
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
				ivjstcLblEndNo.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblEndNo.setBounds(529, 102, 90, 14);
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
				ivjstcLblQty.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblQty.setBounds(365, 102, 56, 14);
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
	 * Return the stcLblReasonCode property value.
	 * 
	 * @return JLabel
	 */
	
	private JLabel getstcLblReasnCd()
	{
		if (ivjstcLblReasnCd == null)
		{
			try
			{
				ivjstcLblReasnCd = new JLabel();
				ivjstcLblReasnCd.setName("stcLblReasnCd");
				ivjstcLblReasnCd.setText(
					InventoryConstant.TXT_REASON_CODE);
				ivjstcLblReasnCd.setMaximumSize(
					new java.awt.Dimension(75, 14));
				ivjstcLblReasnCd.setMinimumSize(
					new java.awt.Dimension(75, 14));
				ivjstcLblReasnCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblReasnCd.setBounds(12, 25, 82, 14);
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
		return ivjstcLblReasnCd;
	}

	/**
	 * Return the stcLblReasonDescription property value.
	 * 
	 * @return JLabel
	 */
	
	private JLabel getstcLblReasnDesc()
	{
		if (ivjstcLblReasnDesc == null)
		{
			try
			{
				ivjstcLblReasnDesc = new JLabel();
				ivjstcLblReasnDesc.setName("stcLblReasnDesc");
				ivjstcLblReasnDesc.setText(
					InventoryConstant.TXT_REASON_DESCRIPTION);
				ivjstcLblReasnDesc.setMaximumSize(
					new java.awt.Dimension(111, 14));
				ivjstcLblReasnDesc.setMinimumSize(
					new java.awt.Dimension(111, 14));
				ivjstcLblReasnDesc.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjstcLblReasnDesc.setBounds(12, 69, 116, 14);
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
		return ivjstcLblReasnDesc;
	}

	/**
	 * Return the stcLbltemCode property value.
	 * 
	 * @return JLabel
	 */
	
	private JLabel getstcLblItmCdDesc()
	{
		if (ivjstcLbltmCdDesc == null)
		{
			try
			{
				ivjstcLbltmCdDesc = new JLabel();
				ivjstcLbltmCdDesc.setName("stcLbltmCdDesc");
				// defect 8269
				//ivjstcLbltmCdDesc.setText("Item Code - Description");
				ivjstcLbltmCdDesc.setText(
					InventoryConstant.TXT_ITEM_DESCRIPTION);
				// end defect 8269
				ivjstcLbltmCdDesc.setMaximumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLbltmCdDesc.setMinimumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLbltmCdDesc.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLbltmCdDesc.setBounds(18, 102, 240, 14);
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
		return ivjstcLbltmCdDesc;
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
				ivjstcLblYr.setHorizontalAlignment(SwingConstants.LEFT);
				ivjstcLblYr.setBounds(319, 102, 35, 14);
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
				ivjtxtBegNo.setBounds(430, 122, 90, 20);
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
				ivjtxtEndNo.setBounds(529, 122, 90, 20);
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
				ivjtxtQty.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtQty.setBounds(365, 122, 57, 20);
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
	 * Return the txtReasonDescription property value.
	 * 
	 * @return RTSInputField
	 */
	
	private RTSInputField gettxtReasnDesc()
	{
		if (ivjtxtReasnDesc == null)
		{
			try
			{
				ivjtxtReasnDesc = new RTSInputField();
				ivjtxtReasnDesc.setName("txtReasnDesc");
				ivjtxtReasnDesc.setInput(RTSInputField.DEFAULT);
				ivjtxtReasnDesc.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtReasnDesc.setBounds(135, 66, 374, 20);
				//ivjtxtReasnDesc.setNextFocusableComponent(
				//	getcomboItmCdDesc());
				//defect 5091
				ivjtxtReasnDesc.setMaxLength(MAX_RESN_CD_DESC_LENGTH);
				//end defect 5091
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
		return ivjtxtReasnDesc;
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
				ivjtxtYr.setBounds(319, 122, 35, 20);
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
		// * to stdout */
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
	
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.INV006_FRAME_NAME);
			setSize(630, 300);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV006_FRAME_TITLE);
			setContentPane(getFrmInventoryDeleteINV006ContentPane1());
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
	 * Check to see if this is PLP, ROP, or OldPlt.
	 * <br>Set the Quantity to PLP Quantity if it is.
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
	//	 * Then depending on which arrow key is pressed, 
	//	 * it sets that radio button selected and requests focus.
	//	 *
	//	 * @param aaKE KeyEvent 
	//	 */
	//	public void keyPressed(KeyEvent aaKE)
	//	{
	//		super.keyPressed(aaKE);
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
			FrmInventoryDeleteINV006 laFrmInventoryDeleteINV006;
			laFrmInventoryDeleteINV006 = new FrmInventoryDeleteINV006();
			laFrmInventoryDeleteINV006.setModal(true);
			laFrmInventoryDeleteINV006
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryDeleteINV006.show();
			java.awt.Insets insets =
				laFrmInventoryDeleteINV006.getInsets();
			laFrmInventoryDeleteINV006.setSize(
				laFrmInventoryDeleteINV006.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryDeleteINV006.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryDeleteINV006.setVisibleRTS(true);
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
	// Setup the Item Code Combo Box.
	private void setcomboItmCdDesc()
	{
		Vector lvSort = new Vector();
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		ItemCodesData laItemCodesData = new ItemCodesData();

		// defect 7890
		// make better use of constants and common methods
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
		// defect 8296
		getcomboItmCdDesc().setSelectedItem(
			InventoryConstant.DEFAULT_SELECTION);
		// defect 8479
		comboBoxHotKeyFix(getcomboItmCdDesc());
		// end defect 8479
		// end defect 8296
		enableDisableYear();
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * <p><i><b>This section of code depends on a certain order 
	 * of data in the Delete Reasons Table.
	 * </i></b>
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData == null)
		{
			setcomboItmCdDesc();
			// Set up the reason code combo box
			cvDelReasns = DeleteReasonsCache.getDelReasons();
			// Change the order of the elements in the vector to 
			// simulate their order 
			// in the table.
			// NOTE: If the table changes, this code will have to be 
			// changed.
			UtilityMethods.sort(cvDelReasns);
			DeleteReasonsData laTmp =
				(DeleteReasonsData) cvDelReasns.get(DELETE_RESN_8);
			cvDelReasns.removeElementAt(DELETE_RESN_8);
			cvDelReasns.insertElementAt(laTmp, 0);
			for (int i = 0; i < cvDelReasns.size(); i++)
			{
				DeleteReasonsData laDRD =
					(DeleteReasonsData) cvDelReasns.get(i);
				if (laDRD.getDelInvReasnCd() != DELETE_RESN_5)
				{
					getcomboReasnCd().addItem(laDRD.getDelInvReasn());
				}
			}
			// defect 8479
			comboBoxHotKeyFix(getcomboReasnCd());
			// end defect 8479
			gettxtQty().setText(CommonConstant.STR_ZERO);
			return;
		}
		else
		{
			caInvAlloctnUIData = (InventoryAllocationUIData) aaData;
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
			// Set the position of the frame so the field entries 
			// are visible when the confirmation box is displayed
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
