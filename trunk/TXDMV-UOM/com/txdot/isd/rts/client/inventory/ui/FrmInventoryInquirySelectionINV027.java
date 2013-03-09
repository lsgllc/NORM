package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryInquirySelectionINV027.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * Min Wang     04/09/2003  Modified actionPerformed(). 
 * 							defect 5940
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
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
 * K Harrell	04/08/2007	Use SystemProperty.getOfcissuanceCd()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates    
 * K Harrell	06/25/2009	Implemented new DealerData 
 * 							modify setTblInfo()
 *							defect 10112 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify setTblInfo() 
 * 							defect 10161 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * In the Inquiry event, frame INV027 prompts for which specific 
 * entities the Inquiry report should be run.
 *
 * @version	POS_640			02/18/2010
 * @author	Charlie Walker
 * <br>Creation Date:		06/28/2001 13:27:09
 */

public class FrmInventoryInquirySelectionINV027
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjpnlSelectOneOrMore = null;
	private JPanel ivjFrmInventoryInquirySelectionINV027ContentPane1 =
		null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblSelectOneOrMore = null;
	private TMINV027 caTableModel = null;
	private JCheckBox ivjchkSelectAllItms = null;
	private JLabel ivjlblEntity = null;
	private JLabel ivjstcLblInvInqSelctn = null;

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
	 * FrmInventoryInquirySelectionINV027 constructor comment.
	 */
	public FrmInventoryInquirySelectionINV027()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryInquirySelectionINV027 constructor comment.
	 * 
	 * @param aaParent JDialog
	 * @param asTblType String
	 */
	public FrmInventoryInquirySelectionINV027(
		JDialog aaParent,
		String asTblType)
	{
		super(aaParent);
		TMINV027.csTblType = asTblType;
		initialize();
	}

	/**
	 * FrmInventoryInquirySelectionINV027 constructor comment.
	 * 
	 * @param aaParent JFrame
	 * @param asTblType String
	 */
	public FrmInventoryInquirySelectionINV027(
		JFrame aaParent,
		String asTblType)
	{
		super(aaParent);
		TMINV027.csTblType = asTblType;
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
	 *
	 * <p>Action not taken while frame is not visible.
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
			// If user selects SelectAllItem(s) checkbox
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
			// Determines what actions to take when Enter, Cancel, or 
			// Help are pressed.
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				if (!captureUserInput())
				{
					return;
				}

				Vector lvDataOut = new Vector();
				lvDataOut.add(caInvInqUIData);
				lvDataOut.add(cvEntityData);

				// Store the OfcIssuanceCd. Used in determining which 
				// item codes to display on INV022.
				
				// defect 9085 
				// Use SystemProperty.getOfcissuanceCd 
				//	OfficeIdsData laOfcIds =
				//	OfficeIdsCache.getOfcId(
				//		SystemProperty.getOfficeIssuanceNo());
				GeneralSearchData laGSD = new GeneralSearchData();
				//laGSD.setIntKey1(laOfcIds.getOfcIssuanceCd());
				laGSD.setIntKey1(SystemProperty.getOfficeIssuanceCd());
				// end defect 9085 
				lvDataOut.add(laGSD);

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvInqUIData);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV027);
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
		Vector lvInvIds = new Vector();

		// Verify that at least one row has been selected
		if (lvSelctdRows.size() < CommonConstant.ELEMENT_1)
		{
			RTSException leRTSExMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_ITEMS_SELECTED);
			leRTSExMsg.displayError(this);
			gettblSelectOneOrMore().requestFocus();
			return false;
		}

		// Store the entity id of the selected rows
		for (int i = lvSelctdRows.size(); i > 0; i--)
		{
			String lsRow = lvSelctdRows.get(i - 1).toString();
			int liRow = Integer.parseInt(lsRow);
			String lsInvId =
				(String) gettblSelectOneOrMore().getValueAt(liRow, 0);
			lvInvIds.add(lsInvId.trim());
		}
		caInvInqUIData.setInvIds(lvInvIds);

		return true;
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
	 * Return the chkSelectAllItems property value.
	 * 
	 * @return JCheckBox
	 */
	
	private JCheckBox getchkSelectAllItms()
	{
		if (ivjchkSelectAllItms == null)
		{
			try
			{
				ivjchkSelectAllItms = new javax.swing.JCheckBox();
				ivjchkSelectAllItms.setName("chkSelectAllItms");
				ivjchkSelectAllItms.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjchkSelectAllItms.setText(
					InventoryConstant.TXT_SELECT_ALL_ITEMS);
				ivjchkSelectAllItms.setMaximumSize(
					new java.awt.Dimension(121, 22));
				ivjchkSelectAllItms.setVisible(true);
				ivjchkSelectAllItms.setActionCommand(
					InventoryConstant.TXT_SELECT_ALL_ITEMS);
				ivjchkSelectAllItms.setMinimumSize(
					new java.awt.Dimension(121, 22));
				// user code begin {1}
				ivjchkSelectAllItms.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllItms;
	}

	/**
	 * Return the FrmInventoryInquirySelectionINV027ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	
	private JPanel getFrmInventoryInquirySelectionINV027ContentPane1()
	{
		if (ivjFrmInventoryInquirySelectionINV027ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryInquirySelectionINV027ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setName(
					"FrmInventoryInquirySelectionINV027ContentPane1");
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmInventoryInquirySelectionINV027ContentPane1
					.setVisible(
					true);

				java
					.awt
					.GridBagConstraints constraintsstcLblInvInqSelctn =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvInqSelctn.gridx = 1;
				constraintsstcLblInvInqSelctn.gridy = 1;
				constraintsstcLblInvInqSelctn.ipadx = 10;
				constraintsstcLblInvInqSelctn.insets =
					new java.awt.Insets(20, 97, 4, 5);
				getFrmInventoryInquirySelectionINV027ContentPane1()
					.add(
					getstcLblInvInqSelctn(),
					constraintsstcLblInvInqSelctn);

				java.awt.GridBagConstraints constraintslblEntity =
					new java.awt.GridBagConstraints();
				constraintslblEntity.gridx = 2;
				constraintslblEntity.gridy = 1;
				constraintslblEntity.ipadx = 69;
				constraintslblEntity.insets =
					new java.awt.Insets(20, 6, 4, 68);
				getFrmInventoryInquirySelectionINV027ContentPane1()
					.add(
					getlblEntity(),
					constraintslblEntity);

				java.awt.GridBagConstraints constraintschkSelectAllItms =
					new java.awt.GridBagConstraints();
				constraintschkSelectAllItms.gridx = 1;
				constraintschkSelectAllItms.gridy = 2;
				constraintschkSelectAllItms.ipadx = 17;
				constraintschkSelectAllItms.insets =
					new java.awt.Insets(4, 25, 5, 101);
				getFrmInventoryInquirySelectionINV027ContentPane1()
					.add(
					getchkSelectAllItms(),
					constraintschkSelectAllItms);

				java
					.awt
					.GridBagConstraints constraintspnlSelectOneOrMore =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOneOrMore.gridx = 1;
				constraintspnlSelectOneOrMore.gridy = 3;
				constraintspnlSelectOneOrMore.gridwidth = 2;
				constraintspnlSelectOneOrMore.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOneOrMore.weightx = 1.0;
				constraintspnlSelectOneOrMore.weighty = 1.0;
				constraintspnlSelectOneOrMore.ipady = 40;
				constraintspnlSelectOneOrMore.insets =
					new java.awt.Insets(6, 25, 7, 26);
				getFrmInventoryInquirySelectionINV027ContentPane1()
					.add(
					getpnlSelectOneOrMore(),
					constraintspnlSelectOneOrMore);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 4;
				constraintsButtonPanel1.gridwidth = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 50;
				constraintsButtonPanel1.ipady = 26;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(8, 116, 0, 117);
				getFrmInventoryInquirySelectionINV027ContentPane1()
					.add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjFrmInventoryInquirySelectionINV027ContentPane1;
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
				getJScrollPane1().setViewportView(
					gettblSelectOneOrMore());
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
		return ivjJScrollPane1;
	}

	/**
	 * Return the lblInventoryInquirySelection property value.
	 * 
	 * @return JLabel
	 */
	
	private JLabel getlblEntity()
	{
		if (ivjlblEntity == null)
		{
			try
			{
				ivjlblEntity = new JLabel();
				ivjlblEntity.setName("lblEntity");
				ivjlblEntity.setText(InventoryConstant.TXT_INV_INQ);
				ivjlblEntity.setMaximumSize(
					new java.awt.Dimension(93, 14));
				ivjlblEntity.setVisible(true);
				ivjlblEntity.setForeground(java.awt.Color.black);
				ivjlblEntity.setMinimumSize(
					new java.awt.Dimension(93, 14));
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
		return ivjlblEntity;
	}

	/**
	 * Return the pnlSelectOneOrMore property value.
	 * 
	 * @return JPanel
	 */
	
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
					new java.awt.Dimension(449, 184));
				ivjpnlSelectOneOrMore.setVisible(true);
				ivjpnlSelectOneOrMore.setMinimumSize(
					new java.awt.Dimension(449, 184));

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 419;
				constraintsJScrollPane1.ipady = 97;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getpnlSelectOneOrMore().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
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
		return ivjpnlSelectOneOrMore;
	}

	/**
	 * Return the stcLblInventoryInquirySelection property value.
	 * 
	 * @return JLabel
	 */
	
	private JLabel getstcLblInvInqSelctn()
	{
		if (ivjstcLblInvInqSelctn == null)
		{
			try
			{
				ivjstcLblInvInqSelctn = new JLabel();
				ivjstcLblInvInqSelctn.setName("stcLblInvInqSelctn");
				ivjstcLblInvInqSelctn.setText(
					InventoryConstant.TXT_INV_INQ_SELECTION_COLON);
				ivjstcLblInvInqSelctn.setMaximumSize(
					new java.awt.Dimension(152, 14));
				ivjstcLblInvInqSelctn.setVisible(true);
				ivjstcLblInvInqSelctn.setMinimumSize(
					new java.awt.Dimension(152, 14));
				ivjstcLblInvInqSelctn.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblInvInqSelctn;
	}

	/**
	 * Return the tblSelectOneOrMore ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	
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
				ivjtblSelectOneOrMore.setModel(new TMINV027());
				ivjtblSelectOneOrMore.setShowVerticalLines(false);
				ivjtblSelectOneOrMore.setShowHorizontalLines(false);
				ivjtblSelectOneOrMore.setAutoCreateColumnsFromModel(
					false);
				ivjtblSelectOneOrMore.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel =
					(TMINV027) ivjtblSelectOneOrMore.getModel();
				TableColumn laTCa =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(0));
				laTCa.setPreferredWidth(15);
				TableColumn laTCb =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(1));
				laTCb.setPreferredWidth(180);
				ivjtblSelectOneOrMore.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectOneOrMore.init();
				laTCa.setCellRenderer(
					ivjtblSelectOneOrMore.setColumnAlignment(
						RTSTable.LEFT));
				laTCb.setCellRenderer(
					ivjtblSelectOneOrMore.setColumnAlignment(
						RTSTable.LEFT));
				ivjtblSelectOneOrMore.addActionListener(this);
				ivjtblSelectOneOrMore.addMultipleSelectionListener(
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
	
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.INV027_FRAME_NAME);
			setSize(500, 375);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setVisible(false);
			setTitle(ScreenConstant.INV027_FRAME_TITLE);
			setContentPane(
				getFrmInventoryInquirySelectionINV027ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
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
			FrmInventoryInquirySelectionINV027 laFrmInventoryInquirySelectionINV027;
			laFrmInventoryInquirySelectionINV027 =
				new FrmInventoryInquirySelectionINV027();
			laFrmInventoryInquirySelectionINV027.setModal(true);
			laFrmInventoryInquirySelectionINV027
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmInventoryInquirySelectionINV027.show();
			java.awt.Insets insets =
				laFrmInventoryInquirySelectionINV027.getInsets();
			laFrmInventoryInquirySelectionINV027.setSize(
				laFrmInventoryInquirySelectionINV027.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryInquirySelectionINV027.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryInquirySelectionINV027.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
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
		caInvInqUIData =
			(InventoryInquiryUIData) lvDataIn.get(
				CommonConstant.ELEMENT_0);
		cvEntityData = (Vector) lvDataIn.get(CommonConstant.ELEMENT_1);

		getlblEntity().setText(caInvInqUIData.getInvInqBy());
		setTblInfo();
	}

	/**
	 * Setup the Table Info.
	 */
	private void setTblInfo()
	{
		Vector lvTblData = new Vector();

		if (caInvInqUIData.getInvInqBy().equals(InventoryConstant.EMP))
		{
			for (int i = 0; i < cvEntityData.size(); i++)
			{
				String[] larrRowData =
					new String[CommonConstant.ELEMENT_2];
				SecurityData laSecrtyData =
					(SecurityData) cvEntityData.get(i);
				larrRowData[CommonConstant.ELEMENT_0] =
					laSecrtyData.getEmpId();
				String lsEmpName =
					laSecrtyData.getEmpLastName()
						+ CommonConstant.STR_COMMA
						+ CommonConstant.STR_SPACE_ONE
						+ laSecrtyData.getEmpFirstName();
				larrRowData[CommonConstant.ELEMENT_1] = lsEmpName;
				lvTblData.add(larrRowData);
			}
		}
		else if (
			caInvInqUIData.getInvInqBy().equals(InventoryConstant.WS))
		{
			for (int i = 0; i < cvEntityData.size(); i++)
			{
				String[] larrRowData =
					new String[CommonConstant.ELEMENT_2];
				AssignedWorkstationIdsData laWsData =
					(AssignedWorkstationIdsData) cvEntityData.get(i);
				larrRowData[CommonConstant.ELEMENT_0] =
					String.valueOf(laWsData.getWsId());
				larrRowData[CommonConstant.ELEMENT_1] =
					CommonConstant.STR_SPACE_EMPTY;
				lvTblData.add(larrRowData);
			}
		}
		else if (
			caInvInqUIData.getInvInqBy().equals(InventoryConstant.DLR))
		{
			for (int i = 0; i < cvEntityData.size(); i++)
			{
				String[] larrRowData =
					new String[CommonConstant.ELEMENT_2];
				// defect 10112 
				// New DealerData 
				DealerData laDlrData =
					(DealerData) cvEntityData.get(i);
				larrRowData[CommonConstant.ELEMENT_0] =
					String.valueOf(laDlrData.getId());
				larrRowData[CommonConstant.ELEMENT_1] =
					laDlrData.getName1();
				// end defect 10112 
				
				lvTblData.add(larrRowData);
			}
		}
		else if (
			caInvInqUIData.getInvInqBy().equals(
				InventoryConstant.SUBCON))
		{
			for (int i = 0; i < cvEntityData.size(); i++)
			{
				String[] larrRowData =
					new String[CommonConstant.ELEMENT_2];
				SubcontractorData laSubconData =
					(SubcontractorData) cvEntityData.get(i);
				// defect 10161
				larrRowData[CommonConstant.ELEMENT_0] =
					String.valueOf(laSubconData.getId());
				larrRowData[CommonConstant.ELEMENT_1] =
					laSubconData.getName1();
				// end defect 10161 
				lvTblData.add(larrRowData);
			}
		}

		caTableModel.add(lvTblData);
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
