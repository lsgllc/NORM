package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmInventoryInquiryItemTypeINV022.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * Min Wang     04/09/2003  Modified actionPerformed(). 
 * 							defect 5940
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 ver 5.2.3
 * Min Wang		08/02/2005	Remove item code from screen.
 * 				 			modify captureUserInput(), 
 * 							gettblSelectOneOrMore(),setData()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/10/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7890 Ver 5.2.3
 * Min Wang		03/12/2007	Set the data in the Selection Criteria box.
 * 							modify setData()
 * 							defect 9117 Ver Special Plates
 * Min Wang		04/03/2007	get all item codes.
 * 							modify captureUserInput(), setData()
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	04/24/2007	Send an empty vector if all item codes are
 * 							selected.
 * 							modify captureUserInput()
 * 							defect 9117 Ver Special Plates
 * K Harrell	08/29/2008	Implemented indicator for Select All Item(s)
 * 							Additional class cleanup 
 * 							modify captureUserInput()
 * 							defect 9706 Ver Defect_POS_B
 * K Harrell	11/08/2008	Load vector of Inventory Items w/ Select All
 * 							for Exception Report. Code was bypassed w/ 
 * 							defect 9706.
 * 							modify captureUserInput()   
 * 							defect 9862 Ver Defect_POS_B
 * K Harrell	08/25/2009	Implement ReportSearchData.initForClient()
 * 							modify actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * In the Inquiry event, frame INV022 prompts for which item code(s) to
 * run the Inquiry report.
 *
 * @version	Defect_POS_F		08/25/2009
 * @author	Charlie Walker
 * <br>Creation Date:			06/28/2001 10:31:13
 */

public class FrmInventoryInquiryItemTypeINV022
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjpnlSelectionCriteria = null;
	private JPanel ivjpnlSelectOneOrMore = null;
	private JPanel ivjFrmInventoryInquiryItemTypeINV022ContentPane1 =
		null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblInvInqHistoryDts = null;
	private JLabel ivjlblInvInqSelctnBy = null;
	private JLabel ivjlblInvInqType = null;
	private JLabel ivjstcLblInvInqHistoryDts = null;
	private JLabel ivjstcLblInvInqSelctnBy = null;
	private JLabel ivjstcLblInvInqType = null;
	private JCheckBox ivjchkExceptionRpt = null;
	private JCheckBox ivjchkSelectAllItms = null;
	private JLabel ivjlblOfcLoc = null;
	private JLabel ivjlblSubstaLoc = null;
	private JLabel ivjstcLblOfcLoc = null;
	private JLabel ivjstcLblSubstaLoc = null;
	private RTSTable ivjtblSelectOneOrMore = null;
	private TMINV022 caTableModel = null;

	/**
	 * InventoryInquiryUIData object used to collect the UI data
	 */
	private InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * Vector used to store the entity data
	 */
	private Vector cvEntityData = new Vector();

	/**
	 * FrmInventoryInquiryItemTypeINV022 constructor comment.
	 */
	public FrmInventoryInquiryItemTypeINV022()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryInquiryItemTypeINV022 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryInquiryItemTypeINV022(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryInquiryItemTypeINV022 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryInquiryItemTypeINV022(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
	 *
	 * <p>Action not active when frame is not visible.
	 * 
	 * @param e ActionEvent  
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			// SELECT ALL ITEMS
			if (aaAE.getSource() == getchkSelectAllItms())
			{
				if (getchkSelectAllItms().isSelected())
				{
					gettblSelectOneOrMore().selectAllRows(
						gettblSelectOneOrMore().getRowCount());
				}
				else
				{
					gettblSelectOneOrMore().unselectAllRows();
				}
			}
			// ENTER
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
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

				Vector lvDataOut = new Vector(2);
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
					null);

			}
			// HELP 
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV022);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the user input so it can be sent to the next screen.
	 */
	private boolean captureUserInput()
	{
		Vector lvSelctdRows =
			new Vector(gettblSelectOneOrMore().getSelectedRowNumbers());
		Vector lvItmCds = new Vector();
		Vector lvItmYrs = new Vector();

		// Verify that at least one row has been selected
		if (lvSelctdRows.size() < 1)
		{
			RTSException leRTSExMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_ITEMS_SELECTED);
			leRTSExMsg.displayError(this);
			gettblSelectOneOrMore().requestFocus();
			return false;
		}

		// defect 9706 
		// Assign boolean to denote selection of "Select All Item(s)" 
		caInvInqUIData.setAllItmsSelected(
			getchkSelectAllItms().isSelected());

		// defect 9862
		// Cannot omit for AllItems as need to gather for Exception Report
		//if (!getchkSelectAllItms().isSelected())
		//{
		// Store the itmcds and years of the selected rows
		UtilityMethods.sort(lvSelctdRows);

		for (int i = 0; i < lvSelctdRows.size(); i++)
		{
			String lsRow = lvSelctdRows.get(i).toString();
			int liRow = Integer.parseInt(lsRow);
			// defect 8269
			String lsItmCdDesc =
				(String) gettblSelectOneOrMore().getValueAt(liRow, 0);
			String lsItmCd = CommonConstant.STR_SPACE_EMPTY;
			Vector lvData = new Vector();
			try
			{
				// defect 9117
				//				Vector lvData3 =
				//					ItemCodesCache.getItmCds(
				//						ItemCodesCache.PROCSNGCD,
				//						InventoryConstant.INV_PROCSNGCD_RESTRICTED,
				//						CommonConstant.STR_SPACE_ONE);
				//				Vector lvData2 =
				//					ItemCodesCache.getItmCds(
				//					ItemCodesCache.PROCSNGCD,
				//					InventoryConstant.INV_PROCSNGCD_SPECIAL,
				//					CommonConstant.STR_SPACE_ONE);
				//				Vector lvData1 =
				//					ItemCodesCache.getItmCds(
				//					ItemCodesCache.PROCSNGCD,
				//					InventoryConstant.INV_PROCSNGCD_NORMAL,
				//					CommonConstant.STR_SPACE_ONE);
				//				 
				//				lvData.addAll(lvData3);
				//				lvData.addAll(lvData2);
				//				lvData.addAll(lvData1);
				lvData = ItemCodesCache.getAllInvItemCds();
				// end defect 9117
			}
			catch (RTSException aeRTSEx)
			{
				// nothing to do if there was an error
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

			lvItmCds.add(lsItmCd);

			String lsItmYr =
				(String) gettblSelectOneOrMore().getValueAt(liRow, 1);
			//String lsItmCd = (String) gettblSelectOneOrMore().getValueAt(liRow, 0);
			//lvItmCds.add(lsItmCd);
			//String lsItmYr = (String) gettblSelectOneOrMore().getValueAt(liRow, 2);
			// end defect 8269

			if (lsItmYr.equals(CommonConstant.STR_SPACE_ONE))
			{
				lvItmYrs.add(new Integer(0));
			}
			else
			{
				lvItmYrs.add(new Integer(lsItmYr));
			}
		}
		caInvInqUIData.setItmCds(lvItmCds);
		caInvInqUIData.setInvItmYrs(lvItmYrs);
		//} // !All Items Selected 
		// end defect 9862 

		// Not needed
		// If all items are selected, just send an empty vector.
		// The SQL will handle it from there.
		//		if (getchkSelectAllItms().isSelected()
		//			&& caInvInqUIData.getRptType().equalsIgnoreCase(
		//				InventoryConstant.CUR_VIRTUAL))
		//		{
		//			lvItmCds = new Vector(0);
		//			lvItmYrs = new Vector(0);
		//		}

		// end defect 9706

		if (getchkExceptionRpt().isSelected())
		{
			caInvInqUIData.setExceptionReport(1);
		}
		else
		{
			caInvInqUIData.setExceptionReport(0);
		}

		return true;
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
				ivjButtonPanel1.setBounds(119, 329, 272, 71);
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
	 * Return the chkExceptionReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkExceptionRpt()
	{
		if (ivjchkExceptionRpt == null)
		{
			try
			{
				ivjchkExceptionRpt = new JCheckBox();
				ivjchkExceptionRpt.setName("chkExceptionRpt");
				ivjchkExceptionRpt.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjchkExceptionRpt.setText(
					InventoryConstant.TXT_EXCEPTION_REPORT);
				ivjchkExceptionRpt.setMaximumSize(
					new java.awt.Dimension(122, 22));
				ivjchkExceptionRpt.setActionCommand(
					InventoryConstant.TXT_EXCEPTION_REPORT);
				ivjchkExceptionRpt.setBounds(358, 120, 132, 22);
				ivjchkExceptionRpt.setMinimumSize(
					new java.awt.Dimension(122, 22));
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
		return ivjchkExceptionRpt;
	}

	/**
	 * Return the chkSelectAllItems property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSelectAllItms()
	{
		if (ivjchkSelectAllItms == null)
		{
			try
			{
				ivjchkSelectAllItms = new JCheckBox();
				ivjchkSelectAllItms.setName("chkSelectAllItms");
				ivjchkSelectAllItms.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjchkSelectAllItms.setText(
					InventoryConstant.TXT_SELECT_ALL_ITEMS);
				ivjchkSelectAllItms.setMaximumSize(
					new java.awt.Dimension(121, 22));
				ivjchkSelectAllItms.setActionCommand(
					InventoryConstant.TXT_SELECT_ALL_ITEMS);
				ivjchkSelectAllItms.setBounds(18, 120, 122, 22);
				ivjchkSelectAllItms.setMinimumSize(
					new java.awt.Dimension(121, 22));
				// user code begin {1}
				ivjchkSelectAllItms.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkSelectAllItms;
	}

	/**
	 * Return the FrmInventoryInquiryItemTypeINV022ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmInventoryInquiryItemTypeINV022ContentPane1()
	{
		if (ivjFrmInventoryInquiryItemTypeINV022ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1
					.setName(
					"FrmInventoryInquiryItemTypeINV022ContentPane1");
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1
					.setLayout(
					null);
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(522, 407));
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(522, 407));
				ivjFrmInventoryInquiryItemTypeINV022ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmInventoryInquiryItemTypeINV022ContentPane1().add(
					getpnlSelectionCriteria(),
					getpnlSelectionCriteria().getName());
				getFrmInventoryInquiryItemTypeINV022ContentPane1().add(
					getchkSelectAllItms(),
					getchkSelectAllItms().getName());
				getFrmInventoryInquiryItemTypeINV022ContentPane1().add(
					getpnlSelectOneOrMore(),
					getpnlSelectOneOrMore().getName());
				getFrmInventoryInquiryItemTypeINV022ContentPane1().add(
					getchkExceptionRpt(),
					getchkExceptionRpt().getName());
				getFrmInventoryInquiryItemTypeINV022ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmInventoryInquiryItemTypeINV022ContentPane1;
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
				getJScrollPane1().setViewportView(
					gettblSelectOneOrMore());
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
	 * Return the lblInventoryInquiryHistoryDates property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvInqHistoryDts()
	{
		if (ivjlblInvInqHistoryDts == null)
		{
			try
			{
				ivjlblInvInqHistoryDts = new JLabel();
				ivjlblInvInqHistoryDts.setName("lblInvInqHistoryDts");
				ivjlblInvInqHistoryDts.setText(
					InventoryConstant.TXT_INV_INQ_HIST_DATES);
				ivjlblInvInqHistoryDts.setMaximumSize(
					new java.awt.Dimension(172, 14));
				ivjlblInvInqHistoryDts.setMinimumSize(
					new java.awt.Dimension(172, 14));
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
		return ivjlblInvInqHistoryDts;
	}

	/**
	 * Return the lblInventoryInquirySelectionBy property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvInqSelctnBy()
	{
		if (ivjlblInvInqSelctnBy == null)
		{
			try
			{
				ivjlblInvInqSelctnBy = new JLabel();
				ivjlblInvInqSelctnBy.setName("lblInvInqSelctnBy");
				ivjlblInvInqSelctnBy.setText(
					InventoryConstant.TXT_INV_INQ_SELECTION);
				ivjlblInvInqSelctnBy.setMaximumSize(
					new java.awt.Dimension(149, 14));
				ivjlblInvInqSelctnBy.setMinimumSize(
					new java.awt.Dimension(149, 14));
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
		return ivjlblInvInqSelctnBy;
	}

	/**
	 * Return the lblInventoryInquiryType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvInqType()
	{
		if (ivjlblInvInqType == null)
		{
			try
			{
				ivjlblInvInqType = new JLabel();
				ivjlblInvInqType.setName("lblInvInqType");
				ivjlblInvInqType.setText(
					InventoryConstant.TXT_INV_INQ_TYPE);
				ivjlblInvInqType.setMaximumSize(
					new java.awt.Dimension(123, 14));
				ivjlblInvInqType.setMinimumSize(
					new java.awt.Dimension(123, 14));
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
		return ivjlblInvInqType;
	}

	/**
	 * Return the lblOfficeLocation property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOfcLoc()
	{
		if (ivjlblOfcLoc == null)
		{
			try
			{
				ivjlblOfcLoc = new JLabel();
				ivjlblOfcLoc.setName("lblOfcLoc");
				ivjlblOfcLoc.setText(
					InventoryConstant.TXT_OFFICE_LOCATION);
				ivjlblOfcLoc.setMaximumSize(
					new java.awt.Dimension(86, 14));
				ivjlblOfcLoc.setMinimumSize(
					new java.awt.Dimension(86, 14));
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
		return ivjlblOfcLoc;
	}

	/**
	 * Return the lblSubstationLocation property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblSubstaLoc()
	{
		if (ivjlblSubstaLoc == null)
		{
			try
			{
				ivjlblSubstaLoc = new JLabel();
				ivjlblSubstaLoc.setName("lblSubstaLoc");
				ivjlblSubstaLoc.setText(
					InventoryConstant.TXT_SUBSTA_LOCATION);
				ivjlblSubstaLoc.setMaximumSize(
					new java.awt.Dimension(113, 14));
				ivjlblSubstaLoc.setMinimumSize(
					new java.awt.Dimension(113, 14));
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
		return ivjlblSubstaLoc;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelectionCriteria()
	{
		if (ivjpnlSelectionCriteria == null)
		{
			try
			{
				ivjpnlSelectionCriteria = new JPanel();
				ivjpnlSelectionCriteria.setName("pnlSelectionCriteria");
				ivjpnlSelectionCriteria.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECTION_CRITERIA_COLON));
				ivjpnlSelectionCriteria.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectionCriteria.setMaximumSize(
					new java.awt.Dimension(485, 152));
				ivjpnlSelectionCriteria.setMinimumSize(
					new java.awt.Dimension(485, 152));
				ivjpnlSelectionCriteria.setBounds(18, 7, 473, 112);

				java
					.awt
					.GridBagConstraints constraintsstcLblInvInqSelctnBy =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvInqSelctnBy.gridx = 1;
				constraintsstcLblInvInqSelctnBy.gridy = 1;
				constraintsstcLblInvInqSelctnBy.ipadx = 27;
				constraintsstcLblInvInqSelctnBy.insets =
					new java.awt.Insets(5, 12, 2, 8);
				getpnlSelectionCriteria().add(
					getstcLblInvInqSelctnBy(),
					constraintsstcLblInvInqSelctnBy);

				java.awt.GridBagConstraints constraintsstcLblInvInqType =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvInqType.gridx = 1;
				constraintsstcLblInvInqType.gridy = 2;
				constraintsstcLblInvInqType.ipadx = 70;
				constraintsstcLblInvInqType.insets =
					new java.awt.Insets(3, 12, 2, 8);
				getpnlSelectionCriteria().add(
					getstcLblInvInqType(),
					constraintsstcLblInvInqType);

				java
					.awt
					.GridBagConstraints constraintsstcLblInvInqHistoryDts =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvInqHistoryDts.gridx = 1;
				constraintsstcLblInvInqHistoryDts.gridy = 3;
				constraintsstcLblInvInqHistoryDts.ipadx = 21;
				constraintsstcLblInvInqHistoryDts.insets =
					new java.awt.Insets(3, 12, 2, 8);
				getpnlSelectionCriteria().add(
					getstcLblInvInqHistoryDts(),
					constraintsstcLblInvInqHistoryDts);

				java.awt.GridBagConstraints constraintsstcLblOfcLoc =
					new java.awt.GridBagConstraints();
				constraintsstcLblOfcLoc.gridx = 1;
				constraintsstcLblOfcLoc.gridy = 4;
				constraintsstcLblOfcLoc.ipadx = 110;
				constraintsstcLblOfcLoc.insets =
					new java.awt.Insets(3, 12, 2, 8);
				getpnlSelectionCriteria().add(
					getstcLblOfcLoc(),
					constraintsstcLblOfcLoc);

				java.awt.GridBagConstraints constraintsstcLblSubstaLoc =
					new java.awt.GridBagConstraints();
				constraintsstcLblSubstaLoc.gridx = 1;
				constraintsstcLblSubstaLoc.gridy = 5;
				constraintsstcLblSubstaLoc.ipadx = 80;
				constraintsstcLblSubstaLoc.insets =
					new java.awt.Insets(3, 12, 6, 8);
				getpnlSelectionCriteria().add(
					getstcLblSubstaLoc(),
					constraintsstcLblSubstaLoc);

				java
					.awt
					.GridBagConstraints constraintslblInvInqSelctnBy =
					new java.awt.GridBagConstraints();
				constraintslblInvInqSelctnBy.gridx = 2;
				constraintslblInvInqSelctnBy.gridy = 1;
				constraintslblInvInqSelctnBy.ipadx = 57;
				constraintslblInvInqSelctnBy.insets =
					new java.awt.Insets(5, 8, 2, 43);
				getpnlSelectionCriteria().add(
					getlblInvInqSelctnBy(),
					constraintslblInvInqSelctnBy);

				java.awt.GridBagConstraints constraintslblInvInqType =
					new java.awt.GridBagConstraints();
				constraintslblInvInqType.gridx = 2;
				constraintslblInvInqType.gridy = 2;
				constraintslblInvInqType.ipadx = 83;
				constraintslblInvInqType.insets =
					new java.awt.Insets(3, 8, 2, 43);
				getpnlSelectionCriteria().add(
					getlblInvInqType(),
					constraintslblInvInqType);

				java
					.awt
					.GridBagConstraints constraintslblInvInqHistoryDts =
					new java.awt.GridBagConstraints();
				constraintslblInvInqHistoryDts.gridx = 2;
				constraintslblInvInqHistoryDts.gridy = 3;
				constraintslblInvInqHistoryDts.ipadx = 34;
				constraintslblInvInqHistoryDts.insets =
					new java.awt.Insets(3, 8, 2, 43);
				getpnlSelectionCriteria().add(
					getlblInvInqHistoryDts(),
					constraintslblInvInqHistoryDts);

				java.awt.GridBagConstraints constraintslblOfcLoc =
					new java.awt.GridBagConstraints();
				constraintslblOfcLoc.gridx = 2;
				constraintslblOfcLoc.gridy = 4;
				constraintslblOfcLoc.ipadx = 120;
				constraintslblOfcLoc.insets =
					new java.awt.Insets(3, 8, 2, 43);
				getpnlSelectionCriteria().add(
					getlblOfcLoc(),
					constraintslblOfcLoc);

				java.awt.GridBagConstraints constraintslblSubstaLoc =
					new java.awt.GridBagConstraints();
				constraintslblSubstaLoc.gridx = 2;
				constraintslblSubstaLoc.gridy = 5;
				constraintslblSubstaLoc.ipadx = 93;
				constraintslblSubstaLoc.insets =
					new java.awt.Insets(3, 8, 6, 43);
				getpnlSelectionCriteria().add(
					getlblSubstaLoc(),
					constraintslblSubstaLoc);
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
		return ivjpnlSelectionCriteria;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelectOneOrMore()
	{
		if (ivjpnlSelectOneOrMore == null)
		{
			try
			{
				ivjpnlSelectOneOrMore = new JPanel();
				ivjpnlSelectOneOrMore.setName("pnlSelectOneOrMore");
				ivjpnlSelectOneOrMore.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_ONE_OR_MORE_COLON));
				ivjpnlSelectOneOrMore.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectOneOrMore.setMaximumSize(
					new java.awt.Dimension(485, 132));
				ivjpnlSelectOneOrMore.setMinimumSize(
					new java.awt.Dimension(485, 132));
				ivjpnlSelectOneOrMore.setBounds(18, 146, 473, 181);

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
				getpnlSelectOneOrMore().add(
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
		return ivjpnlSelectOneOrMore;
	}

	/**
	 * Return the stcLblInventoryInquiryHistoryDates property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvInqHistoryDts()
	{
		if (ivjstcLblInvInqHistoryDts == null)
		{
			try
			{
				ivjstcLblInvInqHistoryDts = new JLabel();
				ivjstcLblInvInqHistoryDts.setName(
					"stcLblInvInqHistoryDts");
				ivjstcLblInvInqHistoryDts.setText(
					InventoryConstant.TXT_INV_INQ_HIST_DATES_COLON);
				ivjstcLblInvInqHistoryDts.setMaximumSize(
					new java.awt.Dimension(175, 14));
				ivjstcLblInvInqHistoryDts.setMinimumSize(
					new java.awt.Dimension(175, 14));
				ivjstcLblInvInqHistoryDts.setHorizontalAlignment(
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
		return ivjstcLblInvInqHistoryDts;
	}

	/**
	 * Return the stcLblInventoryInquirySelectionBy property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvInqSelctnBy()
	{
		if (ivjstcLblInvInqSelctnBy == null)
		{
			try
			{
				ivjstcLblInvInqSelctnBy = new JLabel();
				ivjstcLblInvInqSelctnBy.setName("stcLblInvInqSelctnBy");
				ivjstcLblInvInqSelctnBy.setText(
					InventoryConstant.TXT_INV_INQ_SELECTION_BY_COLON);
				ivjstcLblInvInqSelctnBy.setMaximumSize(
					new java.awt.Dimension(169, 14));
				ivjstcLblInvInqSelctnBy.setMinimumSize(
					new java.awt.Dimension(169, 14));
				ivjstcLblInvInqSelctnBy.setHorizontalAlignment(4);
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
		return ivjstcLblInvInqSelctnBy;
	}

	/**
	 * Return the stcLblInventoryInquiryType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvInqType()
	{
		if (ivjstcLblInvInqType == null)
		{
			try
			{
				ivjstcLblInvInqType = new JLabel();
				ivjstcLblInvInqType.setName("stcLblInvInqType");
				ivjstcLblInvInqType.setText(
					InventoryConstant.TXT_INV_INQ_TYPE_COLON);
				ivjstcLblInvInqType.setMaximumSize(
					new java.awt.Dimension(126, 14));
				ivjstcLblInvInqType.setMinimumSize(
					new java.awt.Dimension(126, 14));
				ivjstcLblInvInqType.setHorizontalAlignment(
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
		return ivjstcLblInvInqType;
	}

	/**
	 * Return the stcLblOfficeLocation property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOfcLoc()
	{
		if (ivjstcLblOfcLoc == null)
		{
			try
			{
				ivjstcLblOfcLoc = new JLabel();
				ivjstcLblOfcLoc.setName("stcLblOfcLoc");
				ivjstcLblOfcLoc.setText(
					InventoryConstant.TXT_OFFICE_LOCATION);
				ivjstcLblOfcLoc.setMaximumSize(
					new java.awt.Dimension(86, 14));
				ivjstcLblOfcLoc.setMinimumSize(
					new java.awt.Dimension(86, 14));
				ivjstcLblOfcLoc.setHorizontalAlignment(
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
		return ivjstcLblOfcLoc;
	}

	/**
	 * Return the stcLblSubstationLocation property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSubstaLoc()
	{
		if (ivjstcLblSubstaLoc == null)
		{
			try
			{
				ivjstcLblSubstaLoc = new JLabel();
				ivjstcLblSubstaLoc.setName("stcLblSubstaLoc");
				ivjstcLblSubstaLoc.setText(
					InventoryConstant.TXT_SUBSTA_LOCATION_COLON);
				ivjstcLblSubstaLoc.setMaximumSize(
					new java.awt.Dimension(116, 14));
				ivjstcLblSubstaLoc.setMinimumSize(
					new java.awt.Dimension(116, 14));
				ivjstcLblSubstaLoc.setHorizontalAlignment(
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
		return ivjstcLblSubstaLoc;
	}

	/**
	 * Return the tblSelectOneOrMore property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSelectOneOrMore()
	{
		if (ivjtblSelectOneOrMore == null)
		{
			try
			{
				ivjtblSelectOneOrMore = new RTSTable();
				ivjtblSelectOneOrMore.setName("tblSelectOneOrMore");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectOneOrMore.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectOneOrMore.setModel(new TMINV022());
				ivjtblSelectOneOrMore.setBounds(0, 0, 200, 200);
				//ivjtblSelectOneOrMore.setNextFocusableComponent(
				//	getchkExceptionRpt());
				ivjtblSelectOneOrMore.setGridColor(
					java.awt.Color.white);
				// user code begin {1}
				caTableModel =
					(TMINV022) ivjtblSelectOneOrMore.getModel();
				// defect 8296
				//TableColumn laTCa =
				//	ivjtblSelectOneOrMore.getColumn(
				//		ivjtblSelectOneOrMore.getColumnName(0));
				//laTCa.setPreferredWidth(15);
				TableColumn laTCb =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(0));
				laTCb.setPreferredWidth(165);
				TableColumn laTCc =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(1));
				laTCc.setPreferredWidth(15);
				ivjtblSelectOneOrMore.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectOneOrMore.init();
				//laTCa.setCellRenderer(
				//	ivjtblSelectOneOrMore.setColumnAlignment(
				//		RTSTable.LEFT));
				laTCb.setCellRenderer(
					ivjtblSelectOneOrMore.setColumnAlignment(
						RTSTable.LEFT));
				laTCc.setCellRenderer(
					ivjtblSelectOneOrMore.setColumnAlignment(
						RTSTable.LEFT));
				ivjtblSelectOneOrMore.addActionListener(this);
				ivjtblSelectOneOrMore.addMultipleSelectionListener(
					this);
				//ivjtblSelectOneOrMore.setNextFocusableComponent(
				//	getchkExceptionRpt());
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblSelectOneOrMore;
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
			setName(ScreenConstant.INV022_FRAME_NAME);
			setSize(510, 390);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV022_FRAME_TITLE);
			setContentPane(
				getFrmInventoryInquiryItemTypeINV022ContentPane1());
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
			FrmInventoryInquiryItemTypeINV022 laFrmInventoryInquiryItemTypeINV022;
			laFrmInventoryInquiryItemTypeINV022 =
				new FrmInventoryInquiryItemTypeINV022();
			laFrmInventoryInquiryItemTypeINV022.setModal(true);
			laFrmInventoryInquiryItemTypeINV022
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryInquiryItemTypeINV022.show();
			java.awt.Insets insets =
				laFrmInventoryInquiryItemTypeINV022.getInsets();
			laFrmInventoryInquiryItemTypeINV022.setSize(
				laFrmInventoryInquiryItemTypeINV022.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryInquiryItemTypeINV022.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryInquiryItemTypeINV022.setVisibleRTS(true);
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
		cvEntityData = (Vector) lvDataIn.get(CommonConstant.ELEMENT_1);

		// Set the data in the Selection Criteria box
		getlblInvInqSelctnBy().setText(caInvInqUIData.getInvInqBy());
		getlblInvInqType().setText(caInvInqUIData.getRptType());
		// defect 9117
		if (caInvInqUIData
			.getRptType()
			.equals(InventoryConstant.CUR_BAL))
		{
			getstcLblInvInqHistoryDts().setEnabled(false);
			getlblInvInqHistoryDts().setText(
				CommonConstant.STR_SPACE_EMPTY);
			getlblInvInqHistoryDts().setEnabled(false);
			getchkExceptionRpt().setEnabled(true);
			getchkExceptionRpt().setVisible(true);

		}
		else if (
			caInvInqUIData.getRptType().equals(
				InventoryConstant.CUR_VIRTUAL))
		{
			getstcLblInvInqHistoryDts().setEnabled(false);
			getlblInvInqHistoryDts().setText(
				CommonConstant.STR_SPACE_EMPTY);
			getlblInvInqHistoryDts().setEnabled(false);
			getchkExceptionRpt().setEnabled(false);
			getchkExceptionRpt().setVisible(false);
		}
		else
		{
			getstcLblInvInqHistoryDts().setEnabled(true);
			getlblInvInqHistoryDts().setEnabled(true);
			String lsRptDts =
				caInvInqUIData.getBeginDt()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_DASH
					+ CommonConstant.STR_SPACE_ONE
					+ caInvInqUIData.getEndDt();
			getlblInvInqHistoryDts().setText(lsRptDts);
		}
		// end defect 9117
		if (caInvInqUIData.getSubstaId() == 0)
		{
			getlblOfcLoc().setText(InventoryConstant.MAIN_OFC);
			getlblSubstaLoc().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblSubstaLoc().setEnabled(false);
			getstcLblSubstaLoc().setEnabled(false);
		}
		else
		{
			getlblOfcLoc().setText(InventoryConstant.SUBSTA_OFC);
			getlblSubstaLoc().setEnabled(true);
			getlblSubstaLoc().setText(caInvInqUIData.getOfcType());
			getstcLblSubstaLoc().setEnabled(true);
		}
		// defect 9117
		if ((caInvInqUIData
			.getInvInqBy()
			.equals(InventoryConstant.ITM_TYPES)
			|| caInvInqUIData.getInvInqBy().equals(
				InventoryConstant.CNTRL))
			&& !caInvInqUIData.getRptType().equals(
				InventoryConstant.CUR_VIRTUAL))
		{
			getchkExceptionRpt().setVisible(true);
			getchkExceptionRpt().setEnabled(true);
		}
		else
		{
			getchkExceptionRpt().setVisible(false);
			getchkExceptionRpt().setEnabled(false);
			//gettblSelectOneOrMore().setNextFocusableComponent(
			//	getButtonPanel1().getBtnEnter());
		}
		// end defect 9117
		// Set the table data
		// defect 8269
		Vector lvData = (Vector) lvDataIn.get(CommonConstant.ELEMENT_2);
		UtilityMethods.sort(lvData);
		caTableModel.add(lvData);
		//caTableModel.add((Vector) lvDataIn.get(2));
		// end defect 8269
		getchkSelectAllItms().requestFocus();
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}

		Vector lvSelctdRows =
			new Vector(gettblSelectOneOrMore().getSelectedRowNumbers());

		if (lvSelctdRows.size()
			== gettblSelectOneOrMore().getRowCount())
		{
			getchkSelectAllItms().setSelected(true);
		}
		else
		{
			getchkSelectAllItms().setSelected(false);
		}
	}
}
