package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;
import com.txdot.isd.rts.client.webapps.registrationrenewal.RegRenClientUtility;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.InternetRegRecData;
import com.txdot.isd.rts.services.data.VehicleSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegistrationRenewalConstants;

/*
 *
 * FrmSearchResultsREG102.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		08/01/2002	Added ability to arrow up 
 * 							and down through the registration records as
 * 							soon as the data is displayed in REG102. 
 * 							Changed getScrollPaneTable method to use 
 * 							table model TMREG102, and changed 
 * 							populateReportsTable method to add to a 
 * 							vector, with each vector row (a 
 * 							VehicleSearchData object) loaded in the 
 * 							table that will be viewed in 
 * 							frmSearchResults (REG102). Added classes 
 * 							TMREG102 and VehicleSearchData. In setData 
 * 							method, removed references to 
 * 							defaulttablemodel.
 * 							defect 4416 Ver
 * Clifford 	09/03/2002	DB down handling.
 * 							defect 3700 Ver
 * B Brown 		10/23/2002	Putting and accessing Phase 2 error msgs 
 * 							in rts_err_msgs.
 * 							modify actionPerformed()
 * 							defect 4205 Ver
 * Clifford 	01/28/2003	Add hot keys
 * 							add keyTyped
 * 							modify getLabel211().
 * 							defect 4542 Ver
 * B Brown		02/20/2003	Commented out UtilityMethods.beep(); in 
 * 							method actioned performed so there is not 
 * 							two beeps.
 * 							modify actionPerformed()
 * 							defect 5379 Ver
 * Jeff S.		06/20/2005	No need to remove record from the table when
 * 							leaving to go process it since we will
 * 							either re-search or just leave the same list
 * 							intact depending on the view only status.
 * 							modify actionPerformed()
 * 							defect 8247 Ver 5.2.3
 * Jeff S.		07/12/2005	Get code to standard. Changed a non-static
 * 							call to a static call. Fixed setVisible for 
 * 							java 1.4.  Added Constants.
 * 							modify actionPerformed(), 
 * 								getScrollPaneTable(), handleException(), 
 * 								main()
 * 							deprecate getBuilderData()
 * 							removed setVisible() - meaningless
 *							defect 7889 Ver 5.2.3
 * B Brown		09/23/2005	Give the county a better error message when
 * 							the rts_itrnt_data record is missing 
 * 							add RECORD_MISSING, RECORD_MISSING_TITLE,
 * 								CNTYSTATUSCD, PLTNO
 * 							add dataRecordMissing()					
 * 							modify checkoutVeh()		
 * 							defect 8245 Ver 5.2.3
 * K Harrell	08/30/2005	Remove mnemonic for Cancel
 * 							modify getBtnCancel()
 * 							defect 8361 Ver 5.2.3
 * K Harrell	02/11/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates   
 * Min Wang		06/11/2007	modify fields and screen.
 * 							defect 8768 Ver Special Plates  
 * Min Wang		06/13/2007	enlarge screen.   
 * 							defect 8768 Ver Special Plates  
 * K Harrell	05/29/2009	On missing CompleteTransactionData, all 
 * 							words in Exception title capitalized. 
 *							Use	KeyEvent constants for mnemonics.
 *							Refactored objects/get methods to 
 *							 application standards.  Help button to 
 *							 RTSButton vs. JButton. 
 * 							"Sort members" did not work.
 *							add ivjlblRecordFound, ivjScrollPaneTable,
 *							ivjlblNumRecords, ivjbtnHelp, get methods  
 * 							delete ivjJlblRecordFound, ivjJsclpnTable,
 *							  ivjJlblNumRecords, ivjbtnHelp, get methods
 * 							delete PLTNO, RECORD_MISSING, 
 * 							 RECORD_MISSING_TITLE
 * 							delete dataRecordMissing() 
 * 							modify actionPerformed(),checkOutVeh(),
 * 						 	 getRTSTableResults() 
 * 							defect 8749 Ver Defect_POS_F 
 * K Harrell	06/23/2009	Cursor Movement Keys did not work. Incomplete 
 * 							display of last record. Additional Cleanup. 
 * 							Refactored objects/methods to standard naming
 * 							 conventions.
 * 							add caButtonGroup   
 * 							modify getJDialogContentPane()
 * 							defect 8763 Ver Defect_POS_F  
 * K Harrell	09/24/2010	Show transactions in the last 48 hours
 * 							add cvInProcsTrans 
 * 							modify actionPerformed(), checkOutVeh()
 * 							defect 10598 Ver 6.6.0    
 * B Brown		11/16/2011	Get CompleteTransactionData no matter what
 * 							the cntystatuscd of the itrnt_trans record
 * 							is so the REG103 screen populates the new
 * 							expiration month and year properly.
 * 							modify actionPerformed()
 * 							defect 10895 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * FrmSearchResultsREG102 allows the county office to approve, decline 
 * for various reasons, or put on hold, internet registration renewals 
 * that are in the list of search results.
 *
 * @version	6.6.0 		  	09/24/2010
 * @author	George Donoso
 * <br>Creation Date:		01/09/2002 12:21:22
 */
public class FrmSearchResultsREG102
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnCancel = null;

	// defect 8749 
	private RTSButton ivjbtnHelp = null;
	private JLabel ivjstclblRecordFound = null;
	private JLabel ivjlblNumRecords = null;
	private JScrollPane ivjScrollPaneTable = null;
	// end defect 8749 

	private RTSButton ivjbtnProcess = null;
	private JPanel ivjJDialogContentPane = null;
	private RTSTable ivjRTSTableResults = null;
	private TMREG102 caSearchResultsTableModel = null;

	private InternetRegRecData caCurInetData;
	private CompleteTransactionData caCurTransData;
	private Vector cvData = null;

	// defect 10598 
	private Vector cvInProcsTrans = new Vector();
	// end defect 10598 

	// Added for passing the search params from REG101
	private Hashtable chtSearchParams;

	// defect 8763 
	private ButtonGroup caButtonGroup = new ButtonGroup();
	// end defect 8763  

	// Constants
	private static final String CANCEL = "Cancel";
	private static final String CNTYSTATUSCD = "CntyStatusCd";
	private static final String EMPTY_STRING = "";
	private static final String FRM_TITLE =
		"Vehicle Search Results    REG102";
	private static final String HELP = "Help";
	private static final String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of javax.swing.JDialog";
	private static final String PROCESS = "Process";
	private static final String RECORD_FOUND_LBL = "Records Found:";
	private static final String RETURNVALUES = "ReturnValues";
	private static final String ZERO = "0";

	/**
	 * FrmSearchResultsREG102 constructor comment.
	 */
	public FrmSearchResultsREG102()
	{
		super();
		initialize();
	}

	/**
	 * FrmSearchResultsREG102 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSearchResultsREG102(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSearchResultsREG102 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSearchResultsREG102(Frame aaOwner)
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
			FrmSearchResultsREG102 laFrmSearchResults;
			laFrmSearchResults = new FrmSearchResultsREG102();
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
			RTSException leRTSValidEx = new RTSException();
			if (aaAE.getSource() == getbtnProcess())
			{
				// Lost in conversion to use new table model mechanism.
				// This is for the no record selected message,
				int liRowCount = getRTSTableResults().getRowCount();
				int liRow = getRTSTableResults().getSelectedRow();
				if (liRowCount > 0 && liRow >= 0)
				{
					//extract obj from vector (same position as row)
					caCurInetData =
						(InternetRegRecData) cvData.get(liRow);
					// defect 8247
					// No need to remove the record from the list b/c
					// we are going to re-search when return to this
					// screen.
					//boolean lbRefresh = false;
					// If we are not in a view only state
					if (caCurInetData.getStatus() != null
						&& !caCurInetData.getStatus().equals(
							CommonConstants.APPROVED + EMPTY_STRING)
						&& !caCurInetData.getStatus().equals(
							CommonConstants.DECLINED_REFUND_PENDING
								+ EMPTY_STRING)
						&& !caCurInetData.getStatus().equals(
							CommonConstants.DECLINED_REFUND_FAILED
								+ EMPTY_STRING)
						&& !caCurInetData.getStatus().equals(
							CommonConstants.DECLINED_REFUND_APPROVED
								+ EMPTY_STRING)
						&& !caCurInetData.getStatus().equals(
							CommonConstants.UNPAID + EMPTY_STRING))
					{
						// update record status to 'IN_PROCESS' if not 
						// already so
						//if (checkOutVeh(caCurInetData))
						//{
						//	lbRefresh = true;
						//	//remove this row
						//	cvData.remove(liRow);
						//	setData(cvData);			 					
						//}
						//else
						//{
						//	// Cannot set the status to 'IN_PROCESS',
						//	// error report in checkoutVeh, 
						//	// no need to repeat here, so just return.
						//	return;
						//}
						if (!checkOutVeh(caCurInetData))
						{
							// Cannot set the status to 'IN_PROCESS',
							// error report in checkoutVeh, 
							// no need to repeat here, so just return. 
							return;
						}
						// end defect 8247
					}
					else
					{
						// defect 10895
						try
						{
							Hashtable lhtSearchParams = new Hashtable();
							lhtSearchParams.put("RegPltNo",
									caCurInetData
											.getCompleteRegRenData()
											.getVehBaseData()
											.getPlateNo());
							Vector lvRecs = (Vector)Comm.sendToServer(
									GeneralConstant.INTERNET_REG_REN_PROCESSING,
									RegRenProcessingConstants.GET_TX ,
									lhtSearchParams);
							caCurTransData = (CompleteTransactionData)lvRecs.get(0);
						}
						catch (Exception leEx)
						{
							// do nothing, we want to continue
						}
						// end defect 10895

						if (caCurInetData.getStatus() != null)
						{
							getRTSTableResults().requestFocus();
						}
					}
					// Pass transaction to FrmProcessVehicleREG103
					Vector lvDataForProcess = new Vector();
					lvDataForProcess.add(caCurInetData);
					lvDataForProcess.add(caCurTransData);

					// defect 10598 
					lvDataForProcess.add(cvInProcsTrans);
					// end defect 10598 

					// defect 8247
					// Changed the data object that is passed to a hash
					// table that it contains all the search params and 
					// results so that we can re-search when needed.
					//getController().processData(
					//	AbstractViewController.ENTER,
					//lvDataForProcess);
					chtSearchParams.put(RETURNVALUES, lvDataForProcess);
					getController().processData(
						AbstractViewController.ENTER,
						chtSearchParams);
					// No need to refresh anymore since we will
					// re-search when returning
					//if (lbRefresh)
					//{
					//	// refresh when table content has been changed.
					//	ivjRTSTableResults.revalidate();
					//	validate();
					//}
					// end defect 8247
				}
				else
				{
					// handle no selection (row = -1)

					leRTSValidEx.addException(
					//new RTSException(960),
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORD_SELECTED),
						getRTSTableResults());

					leRTSValidEx.displayError(this);
					getRTSTableResults().requestFocus();
				}
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					cvData);
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG102);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check out a transaction for processing.
	 * 
	 * @param aaData InternetRegRecData
	 * @return boolean
	 */
	private boolean checkOutVeh(InternetRegRecData aaData)
	{
		Vector lvResult =
			(Vector) RegRenClientUtility.checkOutVeh(aaData, this);
		String lsRet = (String) lvResult.elementAt(0);
		boolean lbSuccessful = (lsRet.equals(ZERO)) ? false : true;
		if (lbSuccessful)
		{
			Object laTrans = lvResult.elementAt(1);
			if (laTrans instanceof CompleteTransactionData)
			{
				caCurTransData = (CompleteTransactionData) laTrans;
				lbSuccessful = true;

				// defect 10598 
				if (lvResult.size() > 2)
				{
					cvInProcsTrans = (Vector) lvResult.elementAt(2);
				}
				// end defect 10598 
			}
			else
			{
				// defect 8749 
				// Use common method in RegistrationVerify 
				// Used also in REG103 
				// defect 8245
				//dataRecordMissing(aaData);
				RegistrationVerify.verifyItrntComplTransData(
					null,
					aaData
						.getCompleteRegRenData()
						.getVehBaseData()
						.getPlateNo());
				aaData.setStatus(
					Integer.toString(CommonConstants.IN_PROCESS));
				// end defect 8749 

				if (chtSearchParams.get(CNTYSTATUSCD) != null
					&& Integer.parseInt(
						(String) chtSearchParams.get(CNTYSTATUSCD))
						!= CommonConstants.IN_PROCESS)
				{
					cvData.remove(aaData);
				}
				populateResultsTable();
				// end defect 8245	
				lbSuccessful = false;
			}
		}
		return lbSuccessful;
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
				ivjbtnCancel.setBounds(298, 262, 90, 25);
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
				ivjbtnHelp.setBounds(452, 262, 90, 25);
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
	 * Return the ivjbtnProcess property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnProcess()
	{
		if (ivjbtnProcess == null)
		{
			try
			{
				ivjbtnProcess = new RTSButton();
				ivjbtnProcess.setName("ivjbtnProcess");
				ivjbtnProcess.setMnemonic(KeyEvent.VK_P);
				ivjbtnProcess.setText(PROCESS);
				ivjbtnProcess.setBounds(144, 262, 90, 25);
				// user code begin {1}
				ivjbtnProcess.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnProcess);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnProcess;
	}

	/**
	 * Return the ivjJDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJDialogContentPane()
	{
		if (ivjJDialogContentPane == null)
		{
			try
			{
				ivjJDialogContentPane = new JPanel();
				ivjJDialogContentPane.setName("ivjJDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(
					getstclblRecordFound(),
					getstclblRecordFound().getName());
				getJDialogContentPane().add(
					getScrollPaneTable(),
					getScrollPaneTable().getName());
				getJDialogContentPane().add(
					getbtnProcess(),
					getbtnProcess().getName());
				getJDialogContentPane().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getJDialogContentPane().add(
					getlblNumRecords(),
					getlblNumRecords().getName());
				getJDialogContentPane().add(
					getbtnHelp(),
					getbtnHelp().getName());

				// defect 8763
				// Add to caButtonGroup for cursor movement
				caButtonGroup = new RTSButtonGroup();
				caButtonGroup.add(getbtnProcess());
				caButtonGroup.add(getbtnCancel());
				caButtonGroup.add(getbtnHelp());
				// end defect 8763 

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
		return ivjJDialogContentPane;
	}

	/**
	 * Return the ivjstclblRecordFound property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblRecordFound()
	{
		if (ivjstclblRecordFound == null)
		{
			try
			{
				ivjstclblRecordFound = new JLabel();
				ivjstclblRecordFound.setName("ivjstclblRecordFound");
				ivjstclblRecordFound.setText(RECORD_FOUND_LBL);
				ivjstclblRecordFound.setBounds(17, 23, 117, 22);
				ivjstclblRecordFound.setForeground(
					java.awt.Color.black);
				// user code begin {1}
				ivjstclblRecordFound.setDisplayedMnemonic(
					KeyEvent.VK_R);
				ivjstclblRecordFound.setLabelFor(getRTSTableResults());
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjstclblRecordFound;
	}

	/**
	 * Return the ivjScrollPaneTable property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new JScrollPane();
				ivjScrollPaneTable.setName("ivjScrollPaneTable");
				ivjScrollPaneTable.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjScrollPaneTable.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjScrollPaneTable.setBounds(4, 50, 686, 195);
				getScrollPaneTable().setViewportView(
					getRTSTableResults());
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
		return ivjScrollPaneTable;
	}

	/**
	 * Return the ivjlblNumRecords property value.
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
				ivjlblNumRecords.setBounds(145, 23, 45, 22);
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
				getScrollPaneTable().setColumnHeaderView(
					ivjRTSTableResults.getTableHeader());
				getScrollPaneTable().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjRTSTableResults.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjRTSTableResults.setModel(new TMREG102());
				ivjRTSTableResults.setShowVerticalLines(true);
				ivjRTSTableResults.setShowHorizontalLines(true);
				ivjRTSTableResults.setAutoCreateColumnsFromModel(false);
				ivjRTSTableResults.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjRTSTableResults.setBounds(0, 0, 146, 200);
				// user code begin {1}
				caSearchResultsTableModel =
					(TMREG102) ivjRTSTableResults.getModel();

				// PLATE NO 
				TableColumn a =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							RegistrationRenewalConstants
								.REG102_COL_PLATE_NO));
				a.setPreferredWidth(80);

				// RECIPIENT NAME
				TableColumn b =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							RegistrationRenewalConstants
								.REG102_COL_REC_NAME));
				b.setPreferredWidth(315);

				// INTERNET DATE 
				TableColumn c =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							RegistrationRenewalConstants
								.REG102_COL_ITRNT_DATE));
				c.setPreferredWidth(90);

				// COUNTY DATE 
				TableColumn d =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							RegistrationRenewalConstants
								.REG102_COL_CNTY_DATE));
				d.setPreferredWidth(90);

				// STATUS 
				TableColumn e =
					ivjRTSTableResults.getColumn(
						ivjRTSTableResults.getColumnName(
							RegistrationRenewalConstants
								.REG102_COL_STATUS));
				e.setPreferredWidth(250);

				ivjRTSTableResults.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);

				ivjRTSTableResults.init();
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
			setName("FrmProcessRenewals");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(700, 329);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
		// user code begin {2}
		// user code end
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
			for (Enumeration e = cvData.elements();
				e.hasMoreElements();
				)
			{
				InternetRegRecData iData =
					(InternetRegRecData) e.nextElement();

				String lsName =
					iData
						.getCompleteRegRenData()
						.getVehUserData()
						.getRecipientName();
				//format status to display
				String lsStatus = iData.getStatus();
				if (!iData.getStatus().equals(EMPTY_STRING))
				{
					if (lsStatus
						.equalsIgnoreCase(
							CommonConstants.UNPAID + EMPTY_STRING))
					{
						lsStatus = RegRenProcessingConstants.UNPAID_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.NEW + EMPTY_STRING))
					{
						lsStatus = RegRenProcessingConstants.NEW_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.HOLD + EMPTY_STRING))
						lsStatus = RegRenProcessingConstants.HOLD_LBL;
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.DECLINED_REFUND_PENDING
								+ EMPTY_STRING))
					{
						lsStatus =
							RegRenProcessingConstants.DECL_PENDING_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.APPROVED + EMPTY_STRING))
					{
						lsStatus =
							RegRenProcessingConstants.APPROVED_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.DECLINED_REFUND_FAILED
								+ EMPTY_STRING))
					{
						lsStatus =
							RegRenProcessingConstants.DECL_FAILED_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.DECLINED_REFUND_APPROVED
								+ EMPTY_STRING))
					{
						lsStatus =
							RegRenProcessingConstants.DECL_SUCCESS_LBL;
					}
					else if (
						lsStatus.equalsIgnoreCase(
							CommonConstants.IN_PROCESS + EMPTY_STRING))
					{
						lsStatus =
							RegRenProcessingConstants.IN_PROC_LBL;
					}
				}
				VehicleSearchData laVehicleSearchData =
					new VehicleSearchData();
				laVehicleSearchData.setPlateNo(
					iData
						.getCompleteRegRenData()
						.getVehBaseData()
						.getPlateNo());
				laVehicleSearchData.setName(lsName.toUpperCase());
				laVehicleSearchData.setRenewalDateTime(
					iData
						.getCompleteRegRenData()
						.getVehUserData()
						.getRenewalDateTime()
						.toString());
				laVehicleSearchData.setCountyProcessedDate(
					iData.getCountyProcessedDt().toString());
				laVehicleSearchData.setStatus(lsStatus);
				lvRowData.add(laVehicleSearchData);
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
			getScrollPaneTable().getViewport().setViewPosition(
				new Point());
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
		if (aaData == null)
		{
			return;
		}
		else
		{
			cvData = (Vector) ((Hashtable) aaData).get(RETURNVALUES);
			chtSearchParams = (Hashtable) aaData;
			populateResultsTable();
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"