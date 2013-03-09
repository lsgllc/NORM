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
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * FrmAddItemToInvoiceINV012.java
 *
 * (c) Texas Department of Transportation 2002
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/15/2002	Lengthen item desc field.
 *							defect 3497
 * M Wang		08/19/2002  Fixed by leaving begin number 
 * 							as empty string by default.
 *							defect 4608 
 *                          This will fail the edit & return on rts001.
 * M Wang		12/03/2003 	Add check to prevent multiple button clicks 
 * 							if frame is not visible.
 *							Modified actionPerformed() and reorganized 
 *							the header of class.
 *                          defect 6687. Ver 5.1.5 fix 2.
 * M Wang 		12/17/2003	Prevent receiving ' ' invoice of PLP and 
 * 							show msg 11 
 *							modify captureUserInput() 
 *                          defect 6756. Ver 5.1.5 fix 2
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify captureUserInput(), 
 *							 setcomboItmCdDesc()
 * 							Ver 5.2.0
 * Min Wang		06/25/2004	Fix Year field is red when it is greyed out.
 *							modified enableDisableYear()
 *							defect 6486 Ver 5.2.1
 * Min Wang     08/12/2004	Fix 5.2.0 merge problem. 
 *							modify captureUserInput()
 *							defect 4608 Ver 5.2.1
 * Ray Rowehl	02/19/2005	Code Cleanup
 * 							organize imports, format source,
 * 							rename variables
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	06/17/2005	Cleanup Button Panel key stuff.
 * 							based on defect 8240.
 * 							Also remove the default button set.
 * 							delete keyPressed()
 * 							modify getButtonPanel1(), gettxtBegNo(),
 * 								gettxtEndNo(), gettxtQty()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3 
 * Ray Rowehl	06/27/2005 	Add more white space for readablility.
 * 							Made more effective use of constants
 * 							defect 7890 Ver 5.2.3  
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							add TXT_ITEM_DESC
 * 							delete TXT_ITEM_CODE_DESC
 * 							modify captureUserInput(), 
 * 							enableDisableYear(),
 * 							getstcLblItmCdDesc(), setcomboItmCdDesc()	
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/08/2005	Code cleanup pass.
 * 							Add white space between methods.
 * 							Improve constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to constant classes as 
 * 							appropriate.
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
 * Frame INV012 allows for items to be added to an invoice in the 
 * Receive Invoice event.
 *
 * @version	Special Plates	03/09/2007
 * @author	Charlie Walker
 * <br>Creation Date:		01/31/2002 15:14:17 
 */

public class FrmAddItemToInvoiceINV012
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblEndNo = null;
	private RTSInputField ivjtxtEndNo = null;
	private JPanel ivjFrmAddItemToInvoiceINV012ContentPane1 = null;
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
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private MFInventoryAllocationData caMFInvAlloctnDataFinal =
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
	 * Flag so that the caMFInvAlloctnDataFinal is only initialized once
	 */
	private boolean cbInit = false;

	/**
	 * A hashtable that contains all of the frame's components that take
	 * input.  (Used for exception handling.)
	 */
	private Hashtable chtFrmComponents = new Hashtable();

	/**
	 * FrmAddItemToInvoiceINV012 constructor comment.
	 */
	public FrmAddItemToInvoiceINV012()
	{
		super();
		initialize();
	}

	/**
	 * FrmAddItemToInvoiceINV012 constructor comment.
	 * 
	 * @param aaParent  JDialog
	 */
	public FrmAddItemToInvoiceINV012(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmAddItemToInvoiceINV012 constructor comment.
	 * 
	 * @param aaParent  JFrame
	 */
	public FrmAddItemToInvoiceINV012(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * <p>Do not allow action when the frame is not visible.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking() || !isVisible())
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
				int liIndx = caMFInvAlloctnData.getSelctdRowIndx();
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						liIndx);
				CommonInvScrnProcsng laCISP =
					new CommonInvScrnProcsng();
				if (laCISP
					.invScrnValidation(this, chtFrmComponents, laIAUID))
				{
					return;
				}

				// Reset the exception thrown flag to false.
				cbExThrown = false;
				caMFInvAlloctnData.setCalcInv(false);
				Vector lvSendData = new Vector();
				lvSendData.addElement(cvSubstaData);
				lvSendData.addElement(caMFInvAlloctnData);
				getController().processData(
					AbstractViewController.ENTER,
					lvSendData);
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
				RTSHelp.displayHelp(RTSHelp.INV012);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture User Input.
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
		laInvAlloctnUIData.setItmCdDesc(lsStr);

		// defect 8269
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

		caMFInvAlloctnData.getInvAlloctnData().add(laInvAlloctnUIData);
		int liIndx =
			caMFInvAlloctnData.getInvAlloctnData().lastIndexOf(
				laInvAlloctnUIData);
		caMFInvAlloctnData.setSelctdRowIndx(liIndx);
		caMFInvAlloctnData.setTransCd(InventoryConstant.ADD);
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
		lsMsg = InventoryConstant.TXT_CORRECTVALUES_QUESTION;
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
			// caMFInvAlloctnData.setProcsdInvItm(true);
			int liIndx =
				caMFInvAlloctnData.getInvAlloctnData().size() - 1;
			InventoryAllocationUIData laInvAlloctnUIData =
				(InventoryAllocationUIData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liIndx);
			caMFInvAlloctnDataFinal.getInvAlloctnData().add(
				laInvAlloctnUIData);
			caMFInvAlloctnDataFinal.setSelctdRowIndx(
				caMFInvAlloctnDataFinal.getInvAlloctnData().size() - 1);
			caMFInvAlloctnDataFinal.setCalcInv(true);
			caMFInvAlloctnDataFinal.setTransCd(InventoryConstant.ADD);
			Vector lvSendData = new Vector();
			lvSendData.addElement(cvSubstaData);
			lvSendData.addElement(caMFInvAlloctnDataFinal);
			getController().processData(
				VCAddItemToInvoiceINV012.YES,
				lvSendData);
		}
		else if (liYesNo == RTSException.NO)
		{
			caMFInvAlloctnData.setCalcInv(false);
			return;
		}
	}

	/**
	 * Enable or Disable the Year shown.
	 */
	private void enableDisableYear()
	{
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		lsStr = (String) getcomboItmCdDesc().getSelectedItem();

		// defect 8269
		//lsStr = lsStr.substring(0, lsStr.indexOf(" "));
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

		InventoryPatternsData laInvPatt = null;
		RTSDate laDate = new RTSDate();
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
				ivjButtonPanel1.setBounds(193, 114, 244, 62);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// defect 7890 
				// refer to defect 8240
				// ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				// ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				// ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjcomboItmCdDesc = new javax.swing.JComboBox();
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
				ivjcomboItmCdDesc.setMaximumRowCount(4);
				ivjcomboItmCdDesc.setBounds(11, 53, 297, 23);
				//ivjcomboItmCdDesc.setNextFocusableComponent(gettxtYr());
				// user code begin {1}
				getcomboItmCdDesc().addItemListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboItmCdDesc;
	}

	/**
	 * Return the FrmAddItemToInvoiceINV012ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmAddItemToInvoiceINV012ContentPane1()
	{
		if (ivjFrmAddItemToInvoiceINV012ContentPane1 == null)
		{
			try
			{
				ivjFrmAddItemToInvoiceINV012ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmAddItemToInvoiceINV012ContentPane1.setName(
					"FrmAddItemToInvoiceINV012ContentPane1");
				ivjFrmAddItemToInvoiceINV012ContentPane1.setLayout(
					null);
				ivjFrmAddItemToInvoiceINV012ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(550, 206));
				//		new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmAddItemToInvoiceINV012ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(550, 206));
				ivjFrmAddItemToInvoiceINV012ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getcomboItmCdDesc(),
					getcomboItmCdDesc().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					gettxtYr(),
					gettxtYr().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					gettxtQty(),
					gettxtQty().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					gettxtBegNo(),
					gettxtBegNo().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					gettxtEndNo(),
					gettxtEndNo().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getFrmAddItemToInvoiceINV012ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmAddItemToInvoiceINV012ContentPane1;
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
				ivjstcLblBegNo = new javax.swing.JLabel();
				ivjstcLblBegNo.setName("stcLblBegNo");
				ivjstcLblBegNo.setText(InventoryConstant.TXT_BEGIN_NO);
				ivjstcLblBegNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setBounds(429, 35, 90, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjstcLblEndNo.setBounds(528, 35, 90, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjstcLblItmCdDesc.setBounds(11, 35, 240, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
	private javax.swing.JLabel getstcLblQty()
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
				ivjstcLblQty.setBounds(364, 35, 56, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
			catch (java.lang.Throwable aeIVJEx)
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
				ivjtxtBegNo.setBounds(429, 54, 90, 20);
				//ivjtxtBegNo.setNextFocusableComponent(gettxtEndNo());
				// defect 7890 
				// use a constant to set this length
				//ivjtxtBegNo.setMaxLength(10);
				ivjtxtBegNo.setMaxLength(
					InventoryConstant.MAX_ITEM_LENGTH);
				// end defect 7890 
				ivjtxtBegNo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjtxtEndNo.setBounds(528, 54, 90, 20);
				//ivjtxtEndNo.setNextFocusableComponent(
				//	getButtonPanel1());
				// defect 7890 
				// use a constant to set this
				//ivjtxtEndNo.setMaxLength(10);
				ivjtxtEndNo.setMaxLength(
					InventoryConstant.MAX_ITEM_LENGTH);
				// end defect 7890 
				ivjtxtEndNo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjtxtQty.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtQty.setBounds(364, 54, 57, 20);
				//ivjtxtQty.setNextFocusableComponent(gettxtBegNo());
				//ivjtxtQty.setMaxLength(7);
				// Use a constant for this set.
				ivjtxtQty.setMaxLength(
					InventoryConstant.MAX_QTY_LENGTH);
				ivjtxtQty.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjtxtYr.setBounds(318, 54, 35, 20);
				//ivjtxtYr.setNextFocusableComponent(gettxtQty());
				ivjtxtYr.setMaxLength(CommonConstant.LENGTH_YEAR);
				ivjtxtYr.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.INV012_FRAME_NAME);
			setSize(630, 180);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV012_FRAME_TITLE);
			setContentPane(getFrmAddItemToInvoiceINV012ContentPane1());
		}
		catch (java.lang.Throwable aeIVJEx)
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
	 * Check to see if this Inventory is PLP, ROP, or OldPlt 
	 * and set the appropriate fields up.
	 * Quantity can only be one if the result is true.
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
	 * Checks to see if Year needs to have its state changed.
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
	// this method is not longer used due to 8240
	//	/**
	//	 * Determines which radio button is currently selected.  
	//	 * Then depending on which arrow key is pressed, 
	//	 * it sets that radio button selected and requests focus.
	//	 *
	//	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
	//	 */
	//	public void keyPressed(java.awt.event.KeyEvent aaKE)
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
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmAddItemToInvoiceINV012 laFrmAddItemToInvoiceINV012;
			laFrmAddItemToInvoiceINV012 =
				new FrmAddItemToInvoiceINV012();
			laFrmAddItemToInvoiceINV012.setModal(true);
			laFrmAddItemToInvoiceINV012
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmAddItemToInvoiceINV012.show();
			java.awt.Insets insets =
				laFrmAddItemToInvoiceINV012.getInsets();
			laFrmAddItemToInvoiceINV012.setSize(
				laFrmAddItemToInvoiceINV012.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAddItemToInvoiceINV012.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAddItemToInvoiceINV012.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.out.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Takes the exception thrown from the server, and determines which
	 * field should turn red and receive focus.
	 * 
	 * @param aaRTSEx  RTSException
	 */
	public void procsExcptn(RTSException aaRTSEx)
	{
		CommonInvScrnProcsng laCISP = new CommonInvScrnProcsng();
		laCISP.hndlServerExcptns(this, chtFrmComponents, aaRTSEx);
	}

	/**
	 * Setup the Combo Box for Item Description.
	 */
	private void setcomboItmCdDesc()
	{
		Vector lvSort = new Vector();
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		ItemCodesData laItemCodesData = new ItemCodesData();
		try
		{
			// PCR 34
			//	cvItmCdsData.addAll(ItemCodesCache.getItmCds(ItemCodesCache.PROCSNGCD,1,""));
			//	cvItmCdsData.addAll(ItemCodesCache.getItmCds(ItemCodesCache.PROCSNGCD,2,""));
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
			// End PCR 34

			// Add VTR only item codes if not a county
			// defect 9085 
			// if (!UtilityMethods.isCounty())
			if (!SystemProperty.isCounty())
			{
				// PCR 34
				// cvItmCdsData.addAll(ItemCodesCache.getItmCds(ItemCodesCache.PROCSNGCD,3,""));
				cvItmCdsData.addAll(
					ItemCodesCache.getItmCds(
						ItemCodesCache.PROCSNGCD,
						InventoryConstant.INV_PROCSNGCD_RESTRICTED,
						CommonConstant.STR_SPACE_EMPTY,
						true));
				// End PCR 34
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
			//lsStr = lsStr + " - ";
			//lsStr = lsStr + laItemCodesData.getItmCdDesc();
			lsStr = laItemCodesData.getItmCdDesc();
			// end defect 8269
			lvSort.addElement(lsStr);
		}
		UtilityMethods.sort(lvSort);
		ComboBoxModel laComboModel = new DefaultComboBoxModel(lvSort);
		getcomboItmCdDesc().setModel(laComboModel);
		// PCR 34
		// getcomboItmCdDesc().setSelectedItem("WS - WINDSHIELD STICKER");
		getcomboItmCdDesc().setSelectedItem(
			InventoryConstant.DEFAULT_SELECTION);
		// defect 8479
		comboBoxHotKeyFix(getcomboItmCdDesc());
		// end defect 8479
		// END PCR 34
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
		if (!cbInit)
		{
			caMFInvAlloctnDataFinal =
				(MFInventoryAllocationData) UtilityMethods.copy(
					caMFInvAlloctnData);
			cbInit = true;
		}
		if (!caMFInvAlloctnData.getCalcInv())
		{
			setcomboItmCdDesc();
			gettxtQty().setText(CommonConstant.STR_ZERO);
			getcomboItmCdDesc().requestFocus();
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
			super.setVisibleRTS(true);
		}
		else
		{
			super.setVisibleRTS(false);
		}
	}
}
