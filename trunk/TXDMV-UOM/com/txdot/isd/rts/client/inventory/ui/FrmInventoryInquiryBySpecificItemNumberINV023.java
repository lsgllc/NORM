package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmInventoryInquiryBySpecificItemNumberINV023.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		02/10/2003	Modified 
 * 							FrmInventoryInquiryBySpecificItemNumberINV023
 *                          add focusLost(). 
 * 							defect 5390.
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							delete cvItmCds
 * 				 			modify captureUserInput(), 
 * 							gettblSelectOne(), setData()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/09/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/25/2009	Implement ReportSearchData.initClientInfo()
 * 							modify actionPerformed()
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * In the Inquiry event, frame INV023 prompts for the specific item 
 * number and code with which to create the Inventory Inquiry report.
 *
 * @version	Defect_POS_F	08/25/2009	
 * @author	Charlie Walker
 * <br>Creation Date:		06/28/2001 11:21:21
 */

public class FrmInventoryInquiryBySpecificItemNumberINV023
	extends RTSDialogBox
	implements ActionListener, FocusListener //, KeyListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjpnlSelectOne = null;
	private JPanel ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1 =
		null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjstcLblItmNo = null;
	private RTSInputField ivjtxtItmNo = null;
	private RTSTable ivjtblSelectOne = null;
	private TMINV023 caTableModel = null;

	/**
	 * InventoryInquiryUIData object used to collect the UI data
	 */
	private InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * FrmInventoryInquiryBySpecificItemNumberINV023 constructor comment.
	 */
	public FrmInventoryInquiryBySpecificItemNumberINV023()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryInquiryBySpecificItemNumberINV023 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryInquiryBySpecificItemNumberINV023(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryInquiryBySpecificItemNumberINV023 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryInquiryBySpecificItemNumberINV023(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
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

			// ENTER 
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				if (!captureUserInput())
				{
					return;
				}
				
				// defect 8628
				ReportSearchData laRptSearchData = new ReportSearchData();
				laRptSearchData.initForClient(ReportConstant.ONLINE);
				// end defect 8628  

				Vector lvDataOut = new Vector(CommonConstant.ELEMENT_2);
				lvDataOut.add(caInvInqUIData);
				lvDataOut.add(laRptSearchData);

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
			}
			// CANCEL 
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvInqUIData);
			}
			// HELP 
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV023);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the user input so it can be sent to the next screen.
	 * 
	 * @return boolean
	 */
	private boolean captureUserInput()
	{
		// TODO  Separate into validateData(), setDataToDataObject()
		 
		// Verify that an item number has been inputed
		if (gettxtItmNo()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			RTSException leRTSEx = new RTSException();
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtItmNo());
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}

		// Store the item number
		caInvInqUIData.setInvItmNo(gettxtItmNo().getText());

		// Verify that a row has been selected
		if (gettblSelectOne().getSelectedRowCount() == 0)
		{
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_ITEMS_SELECTED);
			leRTSEx.displayError(this);
			gettblSelectOne().requestFocus();
			return false;
		}

		// Store the itmcd and year of the selected row
		int liSelctdRow = gettblSelectOne().getSelectedRow();
		String lsItmCdDesc =
			(String) gettblSelectOne().getValueAt(liSelctdRow, 0);
		String lsItmCd = CommonConstant.STR_SPACE_EMPTY;
		Vector lvData = new Vector();
		try
		{
			int liInvProcsngCd =
				InventoryConstant.INV_PROCSNGCD_RESTRICTED;
			Vector lvData3 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					liInvProcsngCd,
					CommonConstant.STR_SPACE_ONE);
			liInvProcsngCd = InventoryConstant.INV_PROCSNGCD_SPECIAL;
			Vector lvData2 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					liInvProcsngCd,
					CommonConstant.STR_SPACE_ONE);
			liInvProcsngCd = InventoryConstant.INV_PROCSNGCD_NORMAL;
			Vector lvData1 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					liInvProcsngCd,
					CommonConstant.STR_SPACE_ONE);
			lvData.addAll(lvData3);
			lvData.addAll(lvData2);
			lvData.addAll(lvData1);
		}
		catch (RTSException aeRTSEx)
		{
			// ignore any errors.  Should not happen.
		}
		for (Iterator laItemSearch = lvData.iterator();
			laItemSearch.hasNext();
			)
		{
			ItemCodesData laTempItemCode =
				(ItemCodesData) laItemSearch.next();
			if (laTempItemCode.getItmCdDesc().equals(lsItmCdDesc))
			{
				lsItmCd = laTempItemCode.getItmCd();
				break;
			}
		}
		caInvInqUIData.setItmCd(lsItmCd);

		String lsItmYr =
			(String) gettblSelectOne().getValueAt(liSelctdRow, 1);

		if (lsItmYr.equals(CommonConstant.STR_SPACE_ONE))
		{
			caInvInqUIData.setInvItmYr(0);
		}
		else
		{
			caInvInqUIData.setInvItmYr(new Integer(lsItmYr).intValue());
		}
		// Validate the item number
		if (!ValidateInventoryPattern
			.chkIfItmPLPOrOLDPLTOrROP(caInvInqUIData.getItmCd()))
		{
			ValidateInventoryPattern laVIP =
				new ValidateInventoryPattern();
			InventoryAllocationUIData laIAUID =
				new InventoryAllocationUIData();

			try
			{
				laIAUID.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laIAUID.setSubstaId(SystemProperty.getSubStationId());
				laIAUID.setItmCd(caInvInqUIData.getItmCd());
				laIAUID.setInvItmYr(caInvInqUIData.getInvItmYr());
				laIAUID.setInvItmNo(caInvInqUIData.getInvItmNo());
				laVIP.validateItmNoInput(laIAUID);

				// Store the patrnseqno and patrnseqcd
				caInvInqUIData.setPatrnSeqNo(laIAUID.getPatrnSeqNo());
				caInvInqUIData.setPatrnSeqCd(laIAUID.getPatrnSeqCd());
			}
			catch (RTSException leRTSEx)
			{
				leRTSEx.displayError(this);
				gettxtItmNo().requestFocus();
				return false;
			}
		}

		// Set the quantity equal to 1, report type to current balance
		caInvInqUIData.setInvQty(1);
		caInvInqUIData.setRptType(InventoryConstant.SPECIFIC_ITM);
		return true;
	}

	/** 
	 * Handle Focus Gained Events.
	 * <p>No action on gaining focus.
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// empty code block
	}
	/**
	 * Handle Focus Lost Events.
	 * If nothing was selected from the table, force a selection 
	 * to the first item.
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (aaFE.getSource() == gettxtItmNo())
		{
			if (gettblSelectOne().getSelectedRow()
				== CommonConstant.NOT_FOUND)
			{
				gettblSelectOne().setRowSelectionInterval(0, 0);
			}
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
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1()
	{
		if (ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
			== null)
		{
			try
			{
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
					.setName(
					"FrmInventoryInquiryBySpecificItemNumberINV023ContentPane1");
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(487, 296));
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(487, 296));
				ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintsstcLblItmNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblItmNo.gridx = 1;
				constraintsstcLblItmNo.gridy = 1;
				constraintsstcLblItmNo.ipadx = 9;
				constraintsstcLblItmNo.insets =
					new java.awt.Insets(18, 18, 9, 5);
				getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1()
					.add(
					getstcLblItmNo(),
					constraintsstcLblItmNo);

				java.awt.GridBagConstraints constraintstxtItmNo =
					new java.awt.GridBagConstraints();
				constraintstxtItmNo.gridx = 2;
				constraintstxtItmNo.gridy = 1;
				constraintstxtItmNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtItmNo.weightx = 1.0;
				constraintstxtItmNo.ipadx = 118;
				constraintstxtItmNo.insets =
					new java.awt.Insets(15, 5, 6, 275);
				getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1()
					.add(
					gettxtItmNo(),
					constraintstxtItmNo);

				java.awt.GridBagConstraints constraintspnlSelectOne =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOne.gridx = 1;
				constraintspnlSelectOne.gridy = 2;
				constraintspnlSelectOne.gridwidth = 2;
				constraintspnlSelectOne.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOne.weightx = 1.0;
				constraintspnlSelectOne.weighty = 1.0;
				constraintspnlSelectOne.ipadx = 27;
				constraintspnlSelectOne.ipady = 8;
				constraintspnlSelectOne.insets =
					new java.awt.Insets(6, 18, 4, 19);
				getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1()
					.add(
					getpnlSelectOne(),
					constraintspnlSelectOne);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 2;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 49;
				constraintsButtonPanel1.ipady = 9;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(5, 14, 13, 122);
				getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1()
					.add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
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
				getJScrollPane1().setViewportView(gettblSelectOne());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the pnlSelectOne property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectOne()
	{
		if (ivjpnlSelectOne == null)
		{
			try
			{
				ivjpnlSelectOne = new JPanel();
				ivjpnlSelectOne.setName("pnlSelectOne");
				ivjpnlSelectOne.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectOne.setLayout(new java.awt.GridBagLayout());
				ivjpnlSelectOne.setMaximumSize(
					new java.awt.Dimension(446, 179));
				ivjpnlSelectOne.setMinimumSize(
					new java.awt.Dimension(446, 179));

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getpnlSelectOne().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjpnlSelectOne;
	}

	/**
	 * Return the stcLblItemNumber property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblItmNo()
	{
		if (ivjstcLblItmNo == null)
		{
			try
			{
				ivjstcLblItmNo = new JLabel();
				ivjstcLblItmNo.setName("stcLblItmNo");
				ivjstcLblItmNo.setText(
					InventoryConstant.TXT_ITEM_NUMBER_COLON);
				ivjstcLblItmNo.setMaximumSize(
					new java.awt.Dimension(76, 14));
				ivjstcLblItmNo.setMinimumSize(
					new java.awt.Dimension(76, 14));
				ivjstcLblItmNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblItmNo;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSelectOne()
	{
		if (ivjtblSelectOne == null)
		{
			try
			{
				ivjtblSelectOne =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblSelectOne.setName("tblSelectOne");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectOne.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectOne.setModel(new TMINV023());
				ivjtblSelectOne.setBounds(0, 0, 200, 200);
				ivjtblSelectOne.setGridColor(java.awt.Color.white);
				// user code begin {1}
				caTableModel = (TMINV023) ivjtblSelectOne.getModel();
				TableColumn laTCa =
					ivjtblSelectOne.getColumn(
						ivjtblSelectOne.getColumnName(0));
				laTCa.setPreferredWidth(165);
				TableColumn laTCb =
					ivjtblSelectOne.getColumn(
						ivjtblSelectOne.getColumnName(1));
				laTCb.setPreferredWidth(15);
				ivjtblSelectOne.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblSelectOne.init();
				laTCa.setCellRenderer(
					ivjtblSelectOne.setColumnAlignment(RTSTable.LEFT));
				laTCb.setCellRenderer(
					ivjtblSelectOne.setColumnAlignment(RTSTable.LEFT));
				ivjtblSelectOne.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblSelectOne;
	}

	/**
	 * Return the txtItemNumber property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtItmNo()
	{
		if (ivjtxtItmNo == null)
		{
			try
			{
				ivjtxtItmNo = new RTSInputField();
				ivjtxtItmNo.setName("txtItmNo");
				ivjtxtItmNo.setInput(RTSInputField.DEFAULT);
				ivjtxtItmNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtItmNo.setMaxLength(
					InventoryConstant.MAX_ITEM_LENGTH);
				// user code begin {1}
				ivjtxtItmNo.addFocusListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtItmNo;
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
			setName(ScreenConstant.INV023_FRAME_NAME);
			setSize(510, 300);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV023_FRAME_TITLE);
			setContentPane(
				getFrmInventoryInquiryBySpecificItemNumberINV023ContentPane1());
		}
		catch (java.lang.Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmInventoryInquiryBySpecificItemNumberINV023 laFrmInventoryInquiryBySpecificItemNumberINV023;
			laFrmInventoryInquiryBySpecificItemNumberINV023 =
				new FrmInventoryInquiryBySpecificItemNumberINV023();
			laFrmInventoryInquiryBySpecificItemNumberINV023.setModal(
				true);
			laFrmInventoryInquiryBySpecificItemNumberINV023
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryInquiryBySpecificItemNumberINV023.show();
			java.awt.Insets insets =
				laFrmInventoryInquiryBySpecificItemNumberINV023
					.getInsets();
			laFrmInventoryInquiryBySpecificItemNumberINV023.setSize(
				laFrmInventoryInquiryBySpecificItemNumberINV023
					.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryInquiryBySpecificItemNumberINV023
					.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryInquiryBySpecificItemNumberINV023
				.setVisibleRTS(
				true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
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
		Vector lvDataIn = (Vector) aaData;
		caInvInqUIData =
			(InventoryInquiryUIData) lvDataIn.get(
				CommonConstant.ELEMENT_0);
		// Add inventory items to the table
		Vector lvData = (Vector) lvDataIn.get(CommonConstant.ELEMENT_2);
		UtilityMethods.sort(lvData);
		caTableModel.add(lvData);
		gettxtItmNo().requestFocus();

	}
}
