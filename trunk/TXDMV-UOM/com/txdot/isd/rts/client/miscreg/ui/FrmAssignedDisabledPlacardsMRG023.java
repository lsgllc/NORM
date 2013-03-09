package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.DisabledPlacardCustomerIdTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * FrmAssignedDisabledPlacardsMRG023.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	10/30/2008	Added navigation by cursor movement keys to 
 * 							buttons.  
 * 							add keyPressed() 
 * 							modify initialize() 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	07/26/2009	Implement HB 3095 - Delete "Type", Retitle
 * 							"Term" as "Type".  Consolidation TMMRG023A, 
 * 							TMMRG023B to TMMRG023A.
 * 							modify caActResultsTableModel,
 *							  caInActResultsTableModel   
 * 							modify getActivePlacardResults(), 
 * 							 getInActivePlacardResults() 
 * 							defect 10133 Ver Defect_POS_F  
 * K Harrell	10/16/2011	Allow Disabled Veterans to have more than 
 * 							2 placards 
 * 							modify setData()
 * 							defect 11050 Ver 6.9.0   
 * K Harrell	02/05/2012	Allow Renew for Permanent Placards in 
 * 							Window. Reinstate (at HQ w/ Security Auth)
 * 							for revoked - Permanent or Temporary. 
 * 							Buttons moved, resized to accommodate new. 	 
 * 							add ciLastActive, ciInLastActive,
 * 							ciLastActiveTbl. cbActiveSelected
 *							add ACTIVETBL, INACTIVETBL, REVOKED_REASONCD 
 *							add cbReInstateAccs, cbAvailForUpdate
 *							add ivjbtnReinstate, ivjbtnRenew, get methods
 *							add valueChanged()  
 * 							delete carrRTSBtn, HELP, keyPressed(), 
 * 							  ivjbtnHelp, get method
 * 							modify caInActResultsTableModel, actionPerformed(),
 * 							  getActivePlacardResults(), getbtnReplace(), 
 * 							  caInActResultsTableModel, 
 * 							  getInActivePlacardResults(), 
 * 							  getJDialogContentPane(), initialize(), 
 * 							  setData()   
 * 							defect 11214 Ver 6.10.0 
 * K Harrell	02/23/2012 	Renew was enabled during Inquiry
 * 							modify valueChanged() 
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for Assigned Disabled Placards MRG023. 
 *
 * @version	6.10.0			02/23/2012 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008
 */

public class FrmAssignedDisabledPlacardsMRG023
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private RTSTable ivjActivePlacardTableResults = null;
	private RTSButton ivjbtnAddNew = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnReplace = null;
	private JPanel ivjDialogContentPane = null;
	private RTSTable ivjInActivePlacardTableResults = null;
	private JScrollPane ivjJScrollPane = null;
	private JScrollPane ivjJScrollPane2 = null;
	private JLabel ivjlblAddress1 = null;
	private JLabel ivjlblAddress2 = null;
	private JLabel ivjlblAddress3 = null;
	private JLabel ivjlblCustId = null;
	private JLabel ivjlblCustName = null;
	private JLabel ivjlblIdDesc = null;
	private JLabel ivjlblInActRecordCount = null;
	private JLabel ivjlblNumRecords = null;
	private JLabel ivjlblRecordFound = null;
	private JLabel ivjstclblDelExpRecords = null;
	// defect 11214  
	private RTSButton ivjbtnReinstate = null;
	private RTSButton ivjbtnRenew = null;
	boolean cbActiveSelected = true; 
	private int ciLastActive = -1; 
	private int ciInLastActive = -1;
	private int ciLastActiveTbl = ACTIVETBL; 
	//private RTSButton[] carrRTSBtn = new RTSButton[5];
	private TMMRG023B caInActResultsTableModel = null;
	private boolean cbReInstateAccs = false;
	private boolean cbAvailForUpdate = false; 
	// end defect 11214 
		
	// defect 10133
	private TMMRG023 caActResultsTableModel = null;
	// end defect 10133
	
	// Vector 
	private Vector cvActDsabldPlcrd = new Vector();
	private Vector cvDPData = null;
	private Vector cvInActDsabldPlcrd = new Vector();

	// Object 
	private DisabledPlacardCustomerData caDPCustData = null;

	// Constant 
	private final static String ACTIVE_LBL = "Active Records:";
	private final static String ADD_NEW = "Add New";
	private final static String CANCEL = "Cancel";
	private final static String DELETE = "Delete";
	private final static String FRM_TITLE =
		"Assigned Disabled Placards     MRG023";
	private final static String INACTIVE_LBL =
		"Deleted / Expired Records:";
	private final static String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of JDialog";
	private final static String REPLACE = "Replace";
	
	// defect 11214 
	// private final static String HELP = "Help";
	private final static int ACTIVETBL = 0;
	private final static int INACTIVETBL = 1;
	private final static int REVOKED_REASONCD = 7; 
	// end defect 11214 
	

	/**
	 * This method initializes ivjbtnReinstate	
	 * 	
	 * @return RTSButton
	 */
	private RTSButton getbtnReinstate()
	{
		if (ivjbtnReinstate == null)
		{
			ivjbtnReinstate = new RTSButton();
			ivjbtnReinstate.setBounds(new Rectangle(450, 451, 90, 25));
			ivjbtnReinstate.setText("Reinstate");
			ivjbtnReplace.setMnemonic(KeyEvent.VK_I);
			ivjbtnReinstate.setEnabled(false);
			ivjbtnReinstate.addActionListener(this);
		}
		return ivjbtnReinstate;
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
			FrmAssignedDisabledPlacardsMRG023 laFrmSearchResults;
			laFrmSearchResults =
				new FrmAssignedDisabledPlacardsMRG023();
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
	 * FrmAssignedDisabledPlacardsMRG023 constructor comment.
	 */
	public FrmAssignedDisabledPlacardsMRG023()
	{
		super();
		initialize();
	}

	/**
	 * FrmAssignedDisabledPlacardsMRG023 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmAssignedDisabledPlacardsMRG023(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmAssignedDisabledPlacardsMRG023 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmAssignedDisabledPlacardsMRG023(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
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
			// ADD NEW 
			if (aaAE.getSource() == getbtnAddNew())
			{
				getController().processData(
					VCAssignedDisabledPlacardsMRG023.MRG025,
					caDPCustData);
			}
			// CANCEL 
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caDPCustData);
			}
			// defect 11214 
//			// HELP 
//			else if (aaAE.getSource() == getbtnHelp())
//			{
//				RTSException leRTSEx =
//					new RTSException(
//						RTSException.WARNING_MESSAGE,
//						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
//						"Information");
//				leRTSEx.displayError(this);
//			}
			// REINSTATE  
			else if (aaAE.getSource() == getbtnReinstate())
			{
				// extract obj from vector 
				DisabledPlacardCustomerData laDPCustDataCopy =
					(DisabledPlacardCustomerData) UtilityMethods.copy(
						cvInActDsabldPlcrd.elementAt(ciInLastActive));

				getController().processData(
					VCAssignedDisabledPlacardsMRG023.MRG029,
					laDPCustDataCopy);
				
			}
			// REPLACE, DELETE, RENEW   
			// end defect 11214 
			else
			{
				// extract obj from vector 
				DisabledPlacardCustomerData laDPCustDataCopy =
					(DisabledPlacardCustomerData) UtilityMethods.copy(
						cvActDsabldPlcrd.elementAt(ciLastActive));

				Vector lvVCVector = new Vector();
				lvVCVector.add(laDPCustDataCopy);

				// DELETE 
				if (aaAE.getSource() == getbtnDelete())
				{
					lvVCVector.add(
						new Integer(
							VCAssignedDisabledPlacardsMRG023.MRG024));
				}
				// REPLACE 
				else if (aaAE.getSource() == getbtnReplace())
				{
					lvVCVector.add(
						new Integer(
							VCAssignedDisabledPlacardsMRG023.MRG026));
				}
				// defect 11214 
				// RENEW 
				else if (aaAE.getSource() == getbtnRenew())
				{
					lvVCVector.add(
						new Integer(
							VCAssignedDisabledPlacardsMRG023.MRG028));
				}
				// end defect 11214 
				
				getController().processData(
					VCAssignedDisabledPlacardsMRG023.CHKIFVOIDABLE,
					lvVCVector);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjActivePlacardTableResults property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getActivePlacardResults()
	{
		if (ivjActivePlacardTableResults == null)
		{
			try
			{
				ivjActivePlacardTableResults = new RTSTable();
				ivjActivePlacardTableResults.setName("ivjActivePlacardTableResults");
				getJScrollPane().setColumnHeaderView(
					ivjActivePlacardTableResults.getTableHeader());
				getJScrollPane().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjActivePlacardTableResults.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
					
				// defect 10133 
				ivjActivePlacardTableResults.setModel(new TMMRG023());
				// end defect 10133 
				
				ivjActivePlacardTableResults.setShowVerticalLines(true);
				ivjActivePlacardTableResults.setShowHorizontalLines(
					true);
				ivjActivePlacardTableResults
					.setAutoCreateColumnsFromModel(
					false);
				ivjActivePlacardTableResults.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjActivePlacardTableResults.setBounds(0, 0, 146, 200);
				
				// defect 11214
				ivjActivePlacardTableResults.setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					ivjActivePlacardTableResults.setEnabled(true);
					ivjActivePlacardTableResults.setRowSelectionAllowed(
							true);
					ivjActivePlacardTableResults.addMultipleSelectionListener(
							this);
				// end defect 11214
					
				// user code begin {1}

				// defect 10133 
				caActResultsTableModel =
					(TMMRG023) ivjActivePlacardTableResults.getModel();
				TableColumn a =
					ivjActivePlacardTableResults.getColumn(
						ivjActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_PLACARD));
				a.setPreferredWidth(120);
				TableColumn b =
					ivjActivePlacardTableResults.getColumn(
						ivjActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_DESCRIPTION));
				b.setPreferredWidth(400);
				TableColumn c =
					ivjActivePlacardTableResults.getColumn(
						ivjActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_ISSUE_DATE));
				c.setPreferredWidth(120);
				TableColumn d =
					ivjActivePlacardTableResults.getColumn(
						ivjActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_EXP_DATE));
				d.setPreferredWidth(120);
				TableColumn e =
					ivjActivePlacardTableResults.getColumn(
						ivjActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant.MRG023_COL_TYPE));
				e.setPreferredWidth(50);
				
				// defect 11214 
				// ivjActivePlacardTableResults.setSelectionMode(
				//		ListSelectionModel.SINGLE_SELECTION);
				// end defect 11214 
				
				ivjActivePlacardTableResults.init();
				a.setCellRenderer(
					ivjActivePlacardTableResults.setColumnAlignment(
						RTSTable.LEFT));
				b.setCellRenderer(
					ivjActivePlacardTableResults.setColumnAlignment(
						RTSTable.LEFT));
				c.setCellRenderer(
					ivjActivePlacardTableResults.setColumnAlignment(
						RTSTable.CENTER));
				d.setCellRenderer(
					ivjActivePlacardTableResults.setColumnAlignment(
						RTSTable.CENTER));
				e.setCellRenderer(
					ivjActivePlacardTableResults.setColumnAlignment(
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
		return ivjActivePlacardTableResults;
	}

	/**
	 * Return the ivjbtnAddNew property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAddNew()
	{
		if (ivjbtnAddNew == null)
		{
			try
			{
				ivjbtnAddNew = new RTSButton();
				ivjbtnAddNew.setName("ivjbtnAddNew");
				ivjbtnAddNew.setMnemonic(KeyEvent.VK_A);
				ivjbtnAddNew.setText(ADD_NEW);
				ivjbtnAddNew.setBounds(25, 451, 95, 25);
				// user code begin {1}
				ivjbtnAddNew.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnAddNew);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnAddNew;
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
				ivjbtnCancel.setName("ivjbtnCancel");
				ivjbtnCancel.setText(CANCEL);
				ivjbtnCancel.setBounds(551, 451, 90, 25);
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
	 * This method initializes ivjbtnDelete
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			ivjbtnDelete = new RTSButton();
			ivjbtnDelete.setSize(84, 25);
			ivjbtnDelete.setText(DELETE);
			ivjbtnDelete.setMnemonic(KeyEvent.VK_D);
			ivjbtnDelete.setLocation(248, 451);
			ivjbtnDelete.setEnabled(false);
			ivjbtnDelete.addActionListener(this);
		}
		return ivjbtnDelete;
	}

	/**
	 * Return the ivjbtnRenew property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRenew()
	{
		if (ivjbtnRenew == null)
		{
			try
			{
				ivjbtnRenew = new RTSButton();
				ivjbtnRenew.setName("ivjbtnRenew");
				ivjbtnRenew.setMnemonic(KeyEvent.VK_W);
				ivjbtnRenew.setText("Renew");
				ivjbtnRenew.setBounds(348, 451, 90, 25);
				// user code begin {1}
				ivjbtnRenew.addActionListener(this);
				ivjbtnRenew.setEnabled(false);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnRenew;
	}

	/**
	 * This method initializes ivjbtnReplace
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnReplace()
	{
		if (ivjbtnReplace == null)
		{
			ivjbtnReplace = new RTSButton();
			ivjbtnReplace.setText(REPLACE);
			ivjbtnReplace.setBounds(135, 451, 95, 25);
			ivjbtnReplace.setMnemonic(KeyEvent.VK_R);
			// defect 11214 
			ivjbtnReplace.setEnabled(false);
			// end defect 11214 
			ivjbtnReplace.addActionListener(this);
		}
		return ivjbtnReplace;
	}
	
	/**
	 * Return the ivjInActivePlacardTableResults property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getInActivePlacardResults()
	{
		if (ivjInActivePlacardTableResults == null)
		{
			try
			{
				ivjInActivePlacardTableResults = new RTSTable();
				ivjInActivePlacardTableResults.setName(
					"ivjInActivePlacardTableResults");
				getJScrollPane().setColumnHeaderView(
					ivjInActivePlacardTableResults.getTableHeader());
				getJScrollPane().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// defect 11214
				// ivjInActivePlacardTableResults.setAutoResizeMode(
				//	JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjInActivePlacardTableResults.setAutoResizeMode(
						JTable.AUTO_RESIZE_OFF);
				ivjInActivePlacardTableResults.setModel(
					new TMMRG023B());
				// end defect 11214 
				
				ivjInActivePlacardTableResults.setShowVerticalLines(
					true);
				ivjInActivePlacardTableResults.setShowHorizontalLines(
					true);
				ivjInActivePlacardTableResults
					.setAutoCreateColumnsFromModel(
					false);
				ivjInActivePlacardTableResults.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				
				// defect 11214 
				ivjInActivePlacardTableResults.setBounds(
					0,
					0,
					146,
					300);
				// user code begin {1}
				
				caInActResultsTableModel =
					(TMMRG023B) ivjInActivePlacardTableResults
						.getModel();
				TableColumn a =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_PLACARD));
				a.setPreferredWidth(90);
				TableColumn b =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_DESCRIPTION));
				b.setPreferredWidth(200);
				TableColumn c =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_ISSUE_DATE));
				c.setPreferredWidth(88);
				TableColumn d =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant
								.MRG023_COL_EXP_DATE));
				d.setPreferredWidth(87);
				TableColumn e =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant.MRG023_COL_TYPE));
				e.setPreferredWidth(50);
				
//				ivjInActivePlacardTableResults.setSelectionMode(
//						ListSelectionModel.SINGLE_SELECTION);
//				ivjInActivePlacardTableResults.setEnabled(false);
//				ivjInActivePlacardTableResults.setRowSelectionAllowed(
//						false);

				TableColumn f =
					ivjInActivePlacardTableResults.getColumn(
						ivjInActivePlacardTableResults.getColumnName(
							MiscellaneousRegConstant.MRG023_COL_DELETE_REASON));
				f.setPreferredWidth(200);

				ivjInActivePlacardTableResults.setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjInActivePlacardTableResults.setEnabled(true);
				ivjInActivePlacardTableResults.setRowSelectionAllowed(
						true);
				ivjInActivePlacardTableResults.addMultipleSelectionListener(
						this);
				// end defect 11214 
				
				ivjInActivePlacardTableResults.init();
				a.setCellRenderer(
					ivjInActivePlacardTableResults.setColumnAlignment(
						RTSTable.LEFT));
				b.setCellRenderer(
					ivjInActivePlacardTableResults.setColumnAlignment(
						RTSTable.LEFT));
				c.setCellRenderer(
					ivjInActivePlacardTableResults.setColumnAlignment(
						RTSTable.CENTER));
				d.setCellRenderer(
					ivjInActivePlacardTableResults.setColumnAlignment(
						RTSTable.CENTER));
				e.setCellRenderer(
					ivjInActivePlacardTableResults.setColumnAlignment(
						RTSTable.CENTER));
				// end defect 10133 
				// defect 11214 
				f.setCellRenderer(
						ivjInActivePlacardTableResults.setColumnAlignment(
							RTSTable.LEFT));
				getJScrollPane2().setHorizontalScrollBarPolicy(
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				// end defect 11214 
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjInActivePlacardTableResults;
	}

	/**
	 * Return the ivjDialogContentPane property value.
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
					getlblRecordFound(),
					getlblRecordFound().getName());
				getJDialogContentPane().add(
					getJScrollPane(),
					getJScrollPane().getName());
				getJDialogContentPane().add(
					getbtnAddNew(),
					getbtnAddNew().getName());
				getJDialogContentPane().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getJDialogContentPane().add(
					getlblNumRecords(),
					getlblNumRecords().getName());
				
				// defect 11214 
				// getJDialogContentPane().add(
				//	 	getbtnHelp(),
				//	 	getbtnHelp().getName());
				
				getJDialogContentPane().add(
					getbtnRenew(),
					getbtnRenew().getName());
				// user code begin {1}
				ivjDialogContentPane.add(getbtnReplace(), null);
				ivjDialogContentPane.add(getbtnReinstate(), null);
				// end defect 11214 
				
				// user code end
				ivjDialogContentPane.add(getbtnDelete(), null);
				ivjDialogContentPane.add(getlblCustName(), null);
				ivjDialogContentPane.add(getlblAddress1(), null);
				ivjDialogContentPane.add(getlblAddress2(), null);
				ivjDialogContentPane.add(getlblAddress3(), null);
				ivjDialogContentPane.add(getlblCustId(), null);
				ivjDialogContentPane.add(getlblIdDesc(), null);
				ivjDialogContentPane.add(getJScrollPane2(), null);
				ivjDialogContentPane.add(
					getstclblDelExpRecords(),
					null);
				ivjDialogContentPane.add(
					getlblInActRecordCount(),
					null);
				ivjDialogContentPane.setSize(638, 367);
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
				getJScrollPane().setViewportView(
					getActivePlacardResults());
				// user code begin {1}
				ivjJScrollPane.setSize(594, 132);
				ivjJScrollPane.setLocation(36, 118);
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
	 * This method initializes ivjJScrollPane2
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane2()
	{

		if (ivjJScrollPane2 == null)
		{
			try
			{
				ivjJScrollPane2 = new JScrollPane();
				ivjJScrollPane2.setName("ivjJScrollPane2");
				ivjJScrollPane2.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane2.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane2.setBounds(33, 306, 594, 132);
				getJScrollPane2().setViewportView(
					getInActivePlacardResults());
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
		return ivjJScrollPane2;
	}

	/**
	 * This method initializes ivjlblAddress1
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress1()
	{
		if (ivjlblAddress1 == null)
		{
			ivjlblAddress1 = new JLabel();
			ivjlblAddress1.setSize(250, 20);
			ivjlblAddress1.setLocation(158, 30);
		}
		return ivjlblAddress1;
	}

	/**
	 * This method initializes ivjlblAddress2
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress2()
	{
		if (ivjlblAddress2 == null)
		{
			ivjlblAddress2 = new JLabel();
			ivjlblAddress2.setSize(250, 20);
			ivjlblAddress2.setLocation(158, 50);
		}
		return ivjlblAddress2;
	}

	/**
	 * This method initializes ivjlblAddress3
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress3()
	{
		if (ivjlblAddress3 == null)
		{
			ivjlblAddress3 = new JLabel();
			ivjlblAddress3.setSize(250, 20);
			ivjlblAddress3.setLocation(158, 70);
		}
		return ivjlblAddress3;
	}

	/**
	 * This method initializes ivjlblCustId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCustId()
	{
		if (ivjlblCustId == null)
		{
			ivjlblCustId = new JLabel();
			ivjlblCustId.setBounds(418, 30, 247, 20);
		}
		return ivjlblCustId;
	}

	/**
	 * This method initializes ivjlblCustName
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCustName()
	{
		if (ivjlblCustName == null)
		{
			ivjlblCustName = new JLabel();
			ivjlblCustName.setSize(452, 20);
			ivjlblCustName.setLocation(158, 10);
		}
		return ivjlblCustName;
	}

	/**
	 * This method initializes ivjlblIdDesc
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIdDesc()
	{
		if (ivjlblIdDesc == null)
		{
			ivjlblIdDesc = new JLabel();
			ivjlblIdDesc.setBounds(418, 50, 247, 20);
		}
		return ivjlblIdDesc;
	}
	/**
	 * This method initializes ivjlblInActRecordCount
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblInActRecordCount()
	{
		if (ivjlblInActRecordCount == null)
		{
			ivjlblInActRecordCount = new javax.swing.JLabel();
			ivjlblInActRecordCount.setSize(24, 20);
			ivjlblInActRecordCount.setLocation(199, 280);
		}
		return ivjlblInActRecordCount;
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
				ivjlblNumRecords.setSize(24, 20);
				ivjlblNumRecords.setName("lblNumRecords");
				ivjlblNumRecords.setLocation(140, 93);
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
	 * Return the ivjlblRecordFound property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRecordFound()
	{
		if (ivjlblRecordFound == null)
		{
			try
			{
				ivjlblRecordFound = new JLabel();
				ivjlblRecordFound.setName("JLabelRecordFound");
				ivjlblRecordFound.setText(ACTIVE_LBL);
				ivjlblRecordFound.setDisplayedMnemonic(KeyEvent.VK_V);
				// user code begin {1}
				ivjlblRecordFound.setLabelFor(
					getActivePlacardResults());
				// user code end
				ivjlblRecordFound.setSize(89, 20);
				ivjlblRecordFound.setLocation(39, 93);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblRecordFound;
	}

	/**
	 * This method initializes ivjstclblDelExpRecords
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblDelExpRecords()
	{
		if (ivjstclblDelExpRecords == null)
		{
			ivjstclblDelExpRecords = new JLabel();
			ivjstclblDelExpRecords.setSize(149, 20);
			ivjstclblDelExpRecords.setText(INACTIVE_LBL);
			ivjstclblDelExpRecords.setLocation(38, 280);
		}
		return ivjstclblDelExpRecords;
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
			// user code begin {1}
			// user code end
			setName("FrmAssignedDisabledPlacardsMRG023");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(678, 518);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogContentPane());
			// defect 11214 
			RTSButtonGroup laRTSBtnGroup = new RTSButtonGroup(); 
			laRTSBtnGroup.add(getbtnAddNew());
			laRTSBtnGroup.add(getbtnReplace());
			laRTSBtnGroup.add(getbtnDelete());
			laRTSBtnGroup.add(getbtnRenew());
			laRTSBtnGroup.add(getbtnReinstate());
			laRTSBtnGroup.add(getbtnCancel());
			// end defect 11214 
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
	}

//	/**
//	 * Handles the key navigation of the button panel
//	 * 
//	 * @param aaKE 
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
//
//		int liNumBtn = carrRTSBtn.length;
//
//		if (aaKE.getSource() instanceof JButton)
//		{
//			int liSelect = 0;
//			for (int i = 0; i < liNumBtn; i++)
//			{
//				if (carrRTSBtn[i].hasFocus())
//				{
//					liSelect = i;
//					break;
//				}
//			}
//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//			{
//				boolean lbStop = true;
//				do
//				{
//					liSelect++;
//					if (liSelect > liNumBtn - 1)
//					{
//						liSelect = 0;
//					}
//					if (liSelect < liNumBtn
//						&& carrRTSBtn[liSelect].isEnabled())
//					{
//						carrRTSBtn[liSelect].requestFocus();
//						lbStop = false;
//					}
//				}
//				while (lbStop);
//			}
//			else if (
//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
//			{
//				boolean lbStop = true;
//				do
//				{
//					liSelect--;
//					if (liSelect < 0)
//					{
//						liSelect = liNumBtn - 1;
//					}
//					if (liSelect >= 0
//						&& carrRTSBtn[liSelect].isEnabled())
//					{
//						carrRTSBtn[liSelect].requestFocus();
//						lbStop = false;
//					}
//				}
//				while (lbStop);
//			}
//		}
//
//		super.keyPressed(aaKE);
//	}

	/**
	 * Populates the report table on the screen
	 */
	private void populateResultsTable()
	{
		int liIndex = 0;
		Vector lvRowData = new Vector();
		try
		{
			for (Enumeration e = cvActDsabldPlcrd.elements();
				e.hasMoreElements();
				)
			{
				DisabledPlacardCustomerData laDPCustData =
					(DisabledPlacardCustomerData) e.nextElement();

				lvRowData.add(laDPCustData);
				liIndex++;
			}
			getlblNumRecords().setText("" + cvActDsabldPlcrd.size());

			if (lvRowData.size() > 0)
			{
				getActivePlacardResults().setRowSelectionAllowed(true);
				getActivePlacardResults().unselectAllRows();
			}
			caActResultsTableModel.add(lvRowData);
			if (getActivePlacardResults().getRowCount() > 0)
			{
				getActivePlacardResults().setRowSelectionInterval(0, 0);
			}
			getActivePlacardResults().requestFocus();
			getJScrollPane().getViewport().setViewPosition(new Point());
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}

		// Inactive Records
		// Deleted or Expired  
		liIndex = 0;
		lvRowData = new Vector();
		try
		{
			for (Enumeration e = cvInActDsabldPlcrd.elements();
				e.hasMoreElements();
				)
			{
				DisabledPlacardCustomerData laDPCustData =
					(DisabledPlacardCustomerData) e.nextElement();

				lvRowData.add(laDPCustData);
				liIndex++;
			}
			getlblInActRecordCount().setText(
				"" + cvInActDsabldPlcrd.size());

			if (lvRowData.size() > 0)
			{
				getInActivePlacardResults().setRowSelectionAllowed(
					true);
				getInActivePlacardResults().unselectAllRows();
			}
			caInActResultsTableModel.add(lvRowData);
			getInActivePlacardResults().requestFocus();
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
		if (aaData != null)
		{
			caDPCustData = (DisabledPlacardCustomerData) aaData;
			cvDPData = (Vector) caDPCustData.getDsabldPlcrd();

			String lsName = "";

			getlblCustId().setText(caDPCustData.getCustId());

			String lsIdTypeDesc = "";
			int liIdTypeCd = caDPCustData.getCustIdTypeCd();
			DisabledPlacardCustomerIdTypeData laDPAIdTypeData =
				DisabledPlacardCustomerIdTypeCache
					.getDsabldPlcrdCustIdType(
					liIdTypeCd);

			if (laDPAIdTypeData != null)
			{
				lsIdTypeDesc =
					laDPAIdTypeData.getCustIdTypeDesc().toUpperCase();
			}
			getlblIdDesc().setText(lsIdTypeDesc);
			if (caDPCustData.isInstitution())
			{
				lsName = caDPCustData.getInstName();
			}
			else
			{
				lsName = caDPCustData.getOwnerName();
			}

			AddressData laAddrData =
				(AddressData) caDPCustData.getAddressData();

			getlblCustName().setText(lsName);
			// Get Vector of String of formatted Address Data  
			Vector lvData = laAddrData.getAddressVector();
			getlblAddress1().setText((String) lvData.elementAt(0));
			getlblAddress2().setText((String) lvData.elementAt(1));
			getlblAddress3().setText((String) lvData.elementAt(2));

			// Copy Cust Info w/ Vector of only one element 			
			for (int i = 0; i < cvDPData.size(); i++)
			{
				DisabledPlacardCustomerData laNewDPCustData =
					(DisabledPlacardCustomerData) UtilityMethods.copy(
						caDPCustData);
				DisabledPlacardData laNewDPData =
					(DisabledPlacardData) UtilityMethods.copy(
						cvDPData.get(i));
				Vector lvNewDsabldPlcrd = new Vector();
				lvNewDsabldPlcrd.add(laNewDPData);
				laNewDPCustData.setDsabldPlcrd(lvNewDsabldPlcrd);

				if (laNewDPData.isActive())
				{
					cvActDsabldPlcrd.add(laNewDPCustData);
				}
				else
				{
					cvInActDsabldPlcrd.add(laNewDPCustData);
				}
			}
			int liNumPlcrds = cvActDsabldPlcrd.size();

			populateResultsTable();

			if (!caDPCustData.isAvailableForUpdate())
			{
				getbtnDelete().setEnabled(false);
				getbtnReplace().setEnabled(false);
				getbtnAddNew().setEnabled(false);
			}
			else if (liNumPlcrds == 0)
			{
				getbtnDelete().setEnabled(false);
				getbtnReplace().setEnabled(false);
			}
			// defect 11050
			//	else if (!caDPCustData.isInstitution())
			//		{
			//			if (liNumPlcrds == 2
			//				|| (liNumPlcrds == 1
			//				&& caDPCustData.isDsabldPltIndi()))
			else if (!caDPCustData.isInstitution() && 
					!caDPCustData.isDsabldVetPltIndi())
			{
				if (liNumPlcrds >= 2
					|| caDPCustData.isDsabldPltIndi())
				{
					getbtnAddNew().setEnabled(false);
				}
			}
			// end defect 11050 
			// defect 11214 
			if (caDPCustData.isAvailableForUpdate()) 
			{	
				cbAvailForUpdate= true; 
				SecurityData laScrtyData =
					getController()
					.getMediator()
					.getDesktop()
					.getSecurityData();
				cbReInstateAccs = laScrtyData.getDsabldPlcrdReInstateAccs() == 1;
			}
			// end defect 11214 
		}
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
		
		int larrActiveRows[] = getActivePlacardResults().getSelectedRows(); 
		int liActiveRows = larrActiveRows.length; 
		if (liActiveRows !=0)
		{
			if (ciLastActiveTbl == INACTIVETBL)
			{
				getInActivePlacardResults().unselectAllRows();
				ciLastActiveTbl = ACTIVETBL; 
			}
			if (liActiveRows == 1)
			{
				ciLastActive = larrActiveRows[0]; 
			}
			else 
			{
				int liNewActive = -1; 
				for (int i = 0; i< liActiveRows; i++ )
				{
					int liActive = larrActiveRows[i]; 
					if (liActive != ciLastActive)
					{
						getActivePlacardResults().unselectAllRows(); 
						getActivePlacardResults().setSelectedRow(liActive);
						liNewActive = liActive; 
					}
				}
				ciLastActive = liNewActive;
			}
		}
		else
		{
			ciLastActive = -1; 
		}
		if (ciLastActive == -1)
		{
			getbtnRenew().setEnabled(false);
			getbtnReplace().setEnabled(false);
			getbtnDelete().setEnabled(false);
		}
		else 
		{
			DisabledPlacardCustomerData laDPCustData =
				(DisabledPlacardCustomerData) 
				cvActDsabldPlcrd.elementAt(ciLastActive);
			if (laDPCustData.getDsabldPlcrd() != null)
			{
				DisabledPlacardData laDPData = (DisabledPlacardData) laDPCustData.getDsabldPlcrd().elementAt(0);
				getbtnRenew().setEnabled(cbAvailForUpdate && laDPData.isPermanent() && laDPData.isInRenewalWindow());
				getbtnReplace().setEnabled(cbAvailForUpdate);
				getbtnDelete().setEnabled(cbAvailForUpdate); 
			}
		}
		int larrInActiveRows[] = getInActivePlacardResults().getSelectedRows();
		int liInActiveRows = larrInActiveRows.length;

		if (liInActiveRows !=0)
		{
			if (ciLastActiveTbl == ACTIVETBL)
			{
				getActivePlacardResults().unselectAllRows();
				ciLastActiveTbl = INACTIVETBL; 
			}
			if (liInActiveRows == 1)
			{
				ciInLastActive = larrInActiveRows[0]; 
			}
			else 
			{
				int liNewInActive = -1; 
				for (int i = 0; i< liInActiveRows; i++ )
				{
					int liInActive = larrInActiveRows[i]; 
					if (liInActive != ciInLastActive)
					{
						getInActivePlacardResults().unselectAllRows(); 
						getInActivePlacardResults().setSelectedRow(liInActive);
						liNewInActive = liInActive; 
					}
				}
				ciInLastActive = liNewInActive;
			}
		}
		else
		{
			ciInLastActive = -1; 
		}
		if (ciInLastActive == -1)
		{
			getbtnReinstate().setEnabled(false);
		}
		else 
		{
			getbtnRenew().setEnabled(false);
			getbtnReplace().setEnabled(false);
			getbtnDelete().setEnabled(false); 

			DisabledPlacardCustomerData laDPCustData =
				(DisabledPlacardCustomerData) 
				cvInActDsabldPlcrd.elementAt(ciInLastActive);
			if (laDPCustData.getDsabldPlcrd() != null)
			{
				DisabledPlacardData laDPData = (DisabledPlacardData) laDPCustData.getDsabldPlcrd().elementAt(0);
				getbtnReinstate().setEnabled(getbtnAddNew().isEnabled() && !laDPData.isExpired() && laDPData.getDelReasnCd() == REVOKED_REASONCD && cbReInstateAccs); 
			}
		}
	}
}