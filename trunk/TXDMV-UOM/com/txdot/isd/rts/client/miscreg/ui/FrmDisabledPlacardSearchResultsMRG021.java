package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * FrmDisabledPlacardSearchResultsMRG021.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							defect 9831 Ver POS_Defect_B 
 * K Harrell	10/30/2008	Added navigation by cursor movement keys to 
 * 							buttons.  
 * 							add keyPressed() 
 * 							modify initialize() 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/01/2008	Moved assessment of Inquiry event from VC 
 * 							to Frame. 
 * 							add isINQ() 
 * 							modify actionPerformed()
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/03/2008	Sorted Customer Vector prior to presentation
 * 							modify setData()
 * 							defect 9831 Ver POS_Defect_B    
 * K Harrell	11/04/2008	Resized Frame for Production via Visual 
 * 							Composition 
 * 							defect 9831 Ver POS_Defect_B 
 * K Harrell	11/09/2008	Display Exception if Exceeded Max Rows 
 * 							modify setData()
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	07/26/2009	Implement HB 3095 - Delete "Type", Retitle
 * 							"Term" as "Type". Additional Cleanup. 
 * 							modify getRTSTableResults() 
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	08/22/2009	Implement RTSButtonGroup 
 * 							add caRTSButtonGroup, caSearchResultsTableModel
 * 							delete carrRTSBtn[], keyPressed(), 
 * 							  SearchResultsTableModel
 * 							modify getJScrollPane() 
 * 							defect 8628 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Search Results Screen (when Multiples) for Disabled Placard. 
 *
 * @version	 Defect_POS_F	08/22/2009
 * @author	Kathy Harrell	
 * <br>Creation Date:		10/21/2008
 */
public class FrmDisabledPlacardSearchResultsMRG021
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnRecordNotApplicable = null;
	private RTSButton ivjbtnSelect = null;
	private JPanel ivjDialogContentPane = null;
	private JLabel ivjlblNumRecords = null;
	private JLabel ivjstclblRecordsFound = null;
	private JScrollPane ivjJScrollPane = null;
	private RTSTable ivjRTSTableResults = null;

	// defect 8628
	private TMMRG021 caSearchResultsTableModel = null; 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628 
	

	// Vector 
	private Vector cvDPCustData = null;

	// Object
	private DisabledPlacardCustomerData caFirstDPCustData = null;

	// Constant 
	private final static String CANCEL = "Cancel";
	private final static String EMPTY_STRING = "";
	private final static String FRM_TITLE =
		"Disabled Placard Search Results    MRG021";
	private final static String HELP = "Help";
	private final static String RECORD_FOUND_LBL = "Records Found:";
	private final static String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of javax.swing.JDialog";
	private final static String NOT_APPLICABLE =
		"Records Not Applicable";
	private final static String SELECT = "Select";

	/**
	 * FrmDisabledPlacardSearchResultsMRG021 constructor comment.
	 */
	public FrmDisabledPlacardSearchResultsMRG021()
	{
		super();
		initialize();
	}

	/**
	 * FrmDisabledPlacardSearchResultsMRG021 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmDisabledPlacardSearchResultsMRG021(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmDisabledPlacardSearchResultsMRG021 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmDisabledPlacardSearchResultsMRG021(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDisabledPlacardSearchResultsMRG021 laFrmSearchResults;
			laFrmSearchResults =
				new FrmDisabledPlacardSearchResultsMRG021();
			laFrmSearchResults.setModal(true);
			laFrmSearchResults
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmSearchResults.show();
			Insets insets = laFrmSearchResults.getInsets();
			laFrmSearchResults.setSize(
				laFrmSearchResults.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSearchResults.getHeight()
					+ insets.top
					+ insets.bottom);

			laFrmSearchResults.setVisibleRTS(true);

		}
		catch (Throwable aeThrowable)
		{
			System.err.println(MAIN_EXCEPTION_MSG);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * This method handles action events.
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
			// SELECT 
			if (aaAE.getSource() == getbtnSelect())
			{
				int liRow = getRTSTableResults().getSelectedRow();

				//extract obj from vector (same position as row)
				DisabledPlacardCustomerData laDPCustData =
					(DisabledPlacardCustomerData) cvDPCustData.get(
						liRow);
				laDPCustData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laDPCustData.setWsId(SystemProperty.getWorkStationId());
				laDPCustData.setEmpId(SystemProperty.getCurrentEmpId());

				int liProcess =
					isINQ()
						? VCDisabledPlacardSearchResultsMRG021.MRG022
						: VCDisabledPlacardSearchResultsMRG021.INPROCS;

				getController().processData(liProcess, laDPCustData);
			}
			// RECORD NOT APPLICABLE 
			else if (aaAE.getSource() == getbtnRecordNotApplicable())
			{

				DisabledPlacardCustomerData laDPCustData =
					new DisabledPlacardCustomerData();

				laDPCustData.setCustId(caFirstDPCustData.getCustId());
				laDPCustData.setSearchType(
					caFirstDPCustData.getSearchType());
				laDPCustData.setNoRecordFound(true);

				getController().processData(
					VCDisabledPlacardSearchResultsMRG021.MRG022,
					laDPCustData);

			}
			// CANCEL
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					cvDPCustData);
			}
			// HELP 
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbtnCancel property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setText(CANCEL);
				ivjbtnCancel.setBounds(441, 278, 90, 25);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the ivjbtnHelp property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("ivjbtnHelp");
				ivjbtnHelp.setMnemonic(KeyEvent.VK_H);
				ivjbtnHelp.setText(HELP);
				ivjbtnHelp.setBounds(560, 278, 90, 25);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * This method initializes ivjbtnRecordNotApplicable
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRecordNotApplicable()
	{
		if (ivjbtnRecordNotApplicable == null)
		{
			ivjbtnRecordNotApplicable = new RTSButton();
			ivjbtnRecordNotApplicable.setName("ivjbtnRecordNotApplicable");
			ivjbtnRecordNotApplicable.setText(NOT_APPLICABLE);
			ivjbtnRecordNotApplicable.setBounds(239, 278, 171, 25);
			ivjbtnRecordNotApplicable.addActionListener(this);
			ivjbtnRecordNotApplicable.setMnemonic(KeyEvent.VK_N);
		}
		return ivjbtnRecordNotApplicable;
	}

	/**
	 * Return the ivjbtnSelect property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnSelect()
	{
		if (ivjbtnSelect == null)
		{
			try
			{
				ivjbtnSelect = new RTSButton();
				ivjbtnSelect.setName("ivjbtnSelect");
				ivjbtnSelect.setMnemonic(KeyEvent.VK_S);
				ivjbtnSelect.setText(SELECT);
				ivjbtnSelect.setBounds(111, 278, 95, 25);
				// user code begin {1}
				ivjbtnSelect.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnSelect);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnSelect;
	}

	/**
	 * Return the JDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJDialogContentPane()
	{
		if (ivjDialogContentPane == null)
		{
			try
			{
				ivjDialogContentPane = new JPanel();
				ivjDialogContentPane.setName("ivjDialogContentPane");
				ivjDialogContentPane.setLayout(null);
				getJDialogContentPane().add(
					getstclblRecordsFound(),
					getstclblRecordsFound().getName());
				getJDialogContentPane().add(
					getJScrollPane(),
					getJScrollPane().getName());
				getJDialogContentPane().add(
					getbtnSelect(),
					getbtnSelect().getName());
				getJDialogContentPane().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getJDialogContentPane().add(
					getlblNumRecords(),
					getlblNumRecords().getName());
				getJDialogContentPane().add(
					getbtnHelp(),
					getbtnHelp().getName());
				// user code begin {1}
				ivjDialogContentPane.add(
					getbtnRecordNotApplicable(),
					null);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjDialogContentPane;
	}

	/**
	 * Return the ivjJScrollPane property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane()
	{
		if (ivjJScrollPane == null)
		{
			try
			{
				ivjJScrollPane = new JScrollPane();
				ivjJScrollPane.setName("ivjJScrollPane");
				ivjJScrollPane.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane.setBounds(4, 50, 746, 213);
				getJScrollPane().setViewportView(getRTSTableResults());
				
				// defect 8628 
				caRTSButtonGroup.add(getbtnSelect()); 
				caRTSButtonGroup.add(getbtnRecordNotApplicable());
				caRTSButtonGroup.add(getbtnCancel());
				caRTSButtonGroup.add(getbtnHelp());
				// end defect 8628 
				
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJScrollPane;
	}

	/**
	 * Return the lblNumRecords property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblNumRecords()
	{
		if (ivjlblNumRecords == null)
		{
			try
			{
				ivjlblNumRecords = new JLabel();
				ivjlblNumRecords.setName("ivjlblNumRecords");
				ivjlblNumRecords.setText(EMPTY_STRING);
				ivjlblNumRecords.setBounds(115, 23, 45, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblNumRecords;
	}

	/**
	 * Return the ivjRTSTableResults property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getRTSTableResults()
	{
		if (ivjRTSTableResults == null)
		{
			try
			{
				ivjRTSTableResults = new RTSTable();
				ivjRTSTableResults.setName("ivjRTSTableResults");
				getJScrollPane().setColumnHeaderView(
					ivjRTSTableResults.getTableHeader());
				getJScrollPane().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjRTSTableResults.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjRTSTableResults.setModel(new TMMRG021());
				ivjRTSTableResults.setShowVerticalLines(true);
				ivjRTSTableResults.setShowHorizontalLines(true);
				ivjRTSTableResults.setAutoCreateColumnsFromModel(false);
				ivjRTSTableResults.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjRTSTableResults.setBounds(0, 0, 146, 200);

				// user code begin {1}
				// defect 10133 
				// Remove column re: Mobility; Implement Constants  
				caSearchResultsTableModel =
					(TMMRG021) ivjRTSTableResults.getModel();

				// ID 
				TableColumn a =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG021_COL_CUSTID));
				a.setPreferredWidth(150);

				// NAME 
				TableColumn b =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							MiscellaneousRegConstant.MRG021_COL_NAME));
				b.setPreferredWidth(370);

				// ADDRESS 
				TableColumn c =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG021_COL_ADDRESS));
				c.setPreferredWidth(440);

				// TYPE  ("P" (Permanent) or "T" (Temporary)) 
				TableColumn d =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							MiscellaneousRegConstant.MRG021_COL_TYPE));
				d.setPreferredWidth(50);

				// PLACARDS 
				TableColumn e =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG021_COL_PLACARDS));
				e.setPreferredWidth(70);

				ivjRTSTableResults.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjRTSTableResults.init();
				a.setCellRenderer(
					ivjRTSTableResults.setColumnAlignment(
						RTSTable.LEFT));
				b.setCellRenderer(
					ivjRTSTableResults.setColumnAlignment(
						RTSTable.LEFT));
				c.setCellRenderer(
					ivjRTSTableResults.setColumnAlignment(
						RTSTable.LEFT));
				d.setCellRenderer(
					ivjRTSTableResults.setColumnAlignment(
						RTSTable.CENTER));
				e.setCellRenderer(
					ivjRTSTableResults.setColumnAlignment(
						RTSTable.CENTER));
				// end defect 10133 
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjRTSTableResults;
	}

	/**
	 * Return the ivjlblRecordsFound property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblRecordsFound()
	{
		if (ivjstclblRecordsFound == null)
		{
			try
			{
				ivjstclblRecordsFound = new JLabel();
				ivjstclblRecordsFound.setName("ivstclblRecordsFound");
				ivjstclblRecordsFound.setText(RECORD_FOUND_LBL);
				ivjstclblRecordsFound.setSize(88, 20);
				ivjstclblRecordsFound.setLocation(17, 23);
				// user code begin {1}
				ivjstclblRecordsFound.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjstclblRecordsFound.setLabelFor(getRTSTableResults());

				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjstclblRecordsFound;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmDisabledPlacardSearchResultsMRG021");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(765, 348);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogContentPane());
			// user code begin {1}
			// user code end

		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
	}

	/**
	 * Return boolean denoting if in Disabled Placard Inquiry Event
	 * 
	 * @return boolean 
	 */
	private boolean isINQ()
	{
		return getController().getTransCode().equals(
			MiscellaneousRegConstant.INQ);
	}


	/**
	 * Populates the report table on the screen
	 */
	private void populateResultsTable()
	{
		int liIndex = 0;
		Vector lvRowData = new Vector();
		try
		{
			for (Enumeration e = cvDPCustData.elements();
				e.hasMoreElements();
				)
			{
				DisabledPlacardCustomerData laDPApplData =
					(DisabledPlacardCustomerData) e.nextElement();

				lvRowData.add(laDPApplData);
				liIndex++;
			}
			getlblNumRecords().setText(String.valueOf(liIndex));

			if (lvRowData.size() > 0)
			{
				getRTSTableResults().setRowSelectionAllowed(true);
				getRTSTableResults().unselectAllRows();
			}
			caSearchResultsTableModel.add(lvRowData);
			if (getRTSTableResults().getRowCount() > 0)
			{
				getRTSTableResults().setRowSelectionInterval(0, 0);
			}
			getRTSTableResults().requestFocus();
			getJScrollPane().getViewport().setViewPosition(new Point());
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}
	}

	/**
	 * This sets data up on the screen before the screen is set visible.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		try
		{
			if (aaData != null && aaData instanceof Vector)
			{
				Vector lvVector = (Vector) aaData;

				cvDPCustData = (Vector) lvVector.elementAt(1);

				//Sort by Owner Name 
				UtilityMethods.sort(cvDPCustData);

				caFirstDPCustData =
					(
						DisabledPlacardCustomerData) cvDPCustData
							.elementAt(
						0);

				getbtnRecordNotApplicable().setEnabled(
					caFirstDPCustData.getSearchType()
						== MiscellaneousRegConstant.CUST_ID
						&& !getController().getTransCode().equals(
							MiscellaneousRegConstant.INQ));

				populateResultsTable();

				// If exceed maximum row count, display message 
				if (((Boolean) (lvVector.elementAt(0))).booleanValue())
				{
					RTSException leEx = new RTSException(745);
					leEx.displayError(this);

				}
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leEx.displayError(this);
			leEx = null;
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="-76,10"