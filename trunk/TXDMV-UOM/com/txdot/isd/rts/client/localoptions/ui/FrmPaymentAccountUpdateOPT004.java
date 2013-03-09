package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.PaymentAccountData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPaymentAccountUpdateOPT004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * Sai			06/07/2002	Fixed OPT005 Displaying twice CQU100004235
 * B Arredondo	03/12/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		03/08/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang 	03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang		08/24/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	08/22/2009	Cleanup. 
 * 							Implement RTSButtonGroup vs. keyPressed()
 * 							add caPymntAcctUpdtModel, caRTSButtonGroup
 * 							delete carrBtn[], ciSelect, ctblModel 
 * 							delete keyPressed(), getBuilderData()
 * 							modify getJScrollPane1() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for payment account update.  User can Add, Delete or Revise 
 * Comptroller's Location Code and Account description from the list of 
 * payment accounts that is displayed to him.
 *
 * @version	Defect_POS_F	08/22/2009	
 * @author	Administrator
 * <br>Creation date:		11/13/2001 18:01:01 
 */

public class FrmPaymentAccountUpdateOPT004
	extends RTSDialogBox
	implements ActionListener, WindowListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblPymtAccts = null;

	// defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	private TMOPT004 caPymntAcctUpdtModel = null;
	// end defect 8628 

	private boolean cbInit = true;

	private Vector cvPAData = new Vector();

	private final static String OPT004_FRAME_TITLE =
		"Payment Account Update     OPT004 ";

	/**
	 * FrmPaymentAccountUpdateOPT004 constructor comment.
	 */
	public FrmPaymentAccountUpdateOPT004()
	{
		super();
		initialize();
	}

	/**
	 * FrmPaymentAccountUpdateOPT004 constructor comment.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmPaymentAccountUpdateOPT004(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPaymentAccountUpdateOPT004 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPaymentAccountUpdateOPT004(JFrame aaParent)
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
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			// ADD 
			if (aaAE.getSource() == getbtnAdd())
			{
				Vector lvData = new Vector();
				lvData.add(
					new Integer(
						LocalOptionConstant.ADD_PAYMENT_ACCOUNT));
				lvData.add(new Integer(-1));
				lvData.add(cvPAData);
				getController().processData(
					LocalOptionConstant.ADD_PAYMENT_ACCOUNT,
					lvData);
			}
			// REVISE  
			else if (aaAE.getSource() == getbtnRevise())
			{
				Vector lvData = new Vector();
				int liCtr = gettblPymtAccts().getSelectedRow();
				if (liCtr != -1)
				{
					lvData.add(
						new Integer(
							LocalOptionConstant
								.REVISE_PAYMENT_ACCOUNT));
					lvData.add(new Integer(liCtr));
					lvData.add(cvPAData);
					getController().processData(
						LocalOptionConstant.REVISE_PAYMENT_ACCOUNT,
						lvData);
				}
			}
			// DELETE 
			else if (aaAE.getSource() == getbtnDelete())
			{
				Vector lvData = new Vector();
				int liCtr = gettblPymtAccts().getSelectedRow();
				if (liCtr != -1)
				{
					lvData.add(
						new Integer(
							LocalOptionConstant
								.DELETE_PAYMENT_ACCOUNT));
					lvData.add(new Integer(liCtr));
					lvData.add(cvPAData);
					getController().processData(
						LocalOptionConstant.DELETE_PAYMENT_ACCOUNT,
						lvData);
				}
			}
			// CANCEL 
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
		} //end of try block
		finally
		{
			doneWorking();
		}

	}

	/**
	 * Return the btnAdd property value.
	 * 
	 * @return  RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic('A');
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return  RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				// user code begin {1}
				//getbtnCancel().setNextFocusableComponent(
				//	gettblPymtAccts());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnDelete property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			try
			{
				ivjbtnDelete = new RTSButton();
				ivjbtnDelete.setName("btnDelete");
				ivjbtnDelete.setMnemonic('D');
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnDelete;
	}

	/**
	 * Return the btnRevise property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setName("btnRevise");
				ivjbtnRevise.setMnemonic('R');
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRevise;
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
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setRequestFocusEnabled(true);
				getJScrollPane1().setViewportView(gettblPymtAccts());
				// defect 8628 
				caRTSButtonGroup.add(getbtnAdd());
				caRTSButtonGroup.add(getbtnRevise());
				caRTSButtonGroup.add(getbtnDelete());
				caRTSButtonGroup.add(getbtnCancel());
				// end defect 8628 
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints constraintsJScrollPane1 =
					new GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.gridwidth = 4;
				constraintsJScrollPane1.fill = GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 477;
				constraintsJScrollPane1.ipady = 197;
				constraintsJScrollPane1.insets =
					new Insets(15, 21, 7, 22);
				getRTSDialogBoxContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsbtnAdd =
					new GridBagConstraints();
				constraintsbtnAdd.gridx = 1;
				constraintsbtnAdd.gridy = 2;
				constraintsbtnAdd.ipadx = 28;
				constraintsbtnAdd.insets = new Insets(8, 40, 46, 20);
				getRTSDialogBoxContentPane().add(
					getbtnAdd(),
					constraintsbtnAdd);

				GridBagConstraints constraintsbtnRevise =
					new GridBagConstraints();
				constraintsbtnRevise.gridx = 2;
				constraintsbtnRevise.gridy = 2;
				constraintsbtnRevise.ipadx = 12;
				constraintsbtnRevise.insets = new Insets(8, 20, 46, 20);
				getRTSDialogBoxContentPane().add(
					getbtnRevise(),
					constraintsbtnRevise);

				GridBagConstraints constraintsbtnDelete =
					new GridBagConstraints();
				constraintsbtnDelete.gridx = 3;
				constraintsbtnDelete.gridy = 2;
				constraintsbtnDelete.ipadx = 14;
				constraintsbtnDelete.insets = new Insets(8, 20, 46, 20);
				getRTSDialogBoxContentPane().add(
					getbtnDelete(),
					constraintsbtnDelete);

				GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 4;
				constraintsbtnCancel.gridy = 2;
				constraintsbtnCancel.ipadx = 12;
				constraintsbtnCancel.insets = new Insets(8, 20, 46, 42);
				getRTSDialogBoxContentPane().add(
					getbtnCancel(),
					constraintsbtnCancel);
				// user code begin {1}
				getbtnAdd().addActionListener(this);
				getbtnRevise().addActionListener(this);
				getbtnDelete().addActionListener(this);
				getbtnCancel().addActionListener(this);
				gettblPymtAccts().addActionListener(this);

				getbtnAdd().addKeyListener(this);
				getbtnRevise().addKeyListener(this);
				getbtnDelete().addKeyListener(this);
				getbtnCancel().addKeyListener(this);

				addWindowListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the tblPymtAccts property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblPymtAccts()
	{
		if (ivjtblPymtAccts == null)
		{
			try
			{
				ivjtblPymtAccts = new RTSTable();
				ivjtblPymtAccts.setName("tblPymtAccts");
				getJScrollPane1().setColumnHeaderView(
					ivjtblPymtAccts.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblPymtAccts.setModel(new TMOPT004());
				ivjtblPymtAccts.setBounds(0, 0, 200, 200);
				ivjtblPymtAccts.setShowVerticalLines(false);
				ivjtblPymtAccts.setShowHorizontalLines(false);
				// user code begin {1}
				caPymntAcctUpdtModel =
					(TMOPT004) ivjtblPymtAccts.getModel();

				TableColumn a =
					ivjtblPymtAccts.getColumn(
						ivjtblPymtAccts.getColumnName(0));
				a.setPreferredWidth(90);

				TableColumn b =
					ivjtblPymtAccts.getColumn(
						ivjtblPymtAccts.getColumnName(1));
				b.setPreferredWidth(220);

				ivjtblPymtAccts.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblPymtAccts.init();

				a.setCellRenderer(
					ivjtblPymtAccts.setColumnAlignment(RTSTable.LEFT));
				b.setCellRenderer(
					ivjtblPymtAccts.setColumnAlignment(RTSTable.LEFT));
				//ivjtblPymtAccts.setNextFocusableComponent(getbtnAdd());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblPymtAccts;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		///* Uncomment the following lines to print uncaught exceptions
		// * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			setName("FrmPaymentAccountUpdateOPT004");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(542, 324);
			setTitle(OPT004_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getbtnAdd());
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPaymentAccountUpdateOPT004 laFrmPaymentAccountUpdateOPT004;
			laFrmPaymentAccountUpdateOPT004 =
				new FrmPaymentAccountUpdateOPT004();
			laFrmPaymentAccountUpdateOPT004.setModal(true);
			laFrmPaymentAccountUpdateOPT004
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPaymentAccountUpdateOPT004.show();
			Insets insets = laFrmPaymentAccountUpdateOPT004.getInsets();
			laFrmPaymentAccountUpdateOPT004.setSize(
				laFrmPaymentAccountUpdateOPT004.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPaymentAccountUpdateOPT004.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPaymentAccountUpdateOPT004.setVisibleRTS(true);
		}
		catch (Throwable leIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 8628 
		// Implement one return; Consolidate enable statements  
		if (aaDataObject == null)
		{
			if (gettblPymtAccts().getRowCount() > 0)
			{
				gettblPymtAccts().setRowSelectionInterval(0, 0);
			}
		}
		else
		{
			cvPAData = (Vector) aaDataObject;
			UtilityMethods.sort(cvPAData);
			caPymntAcctUpdtModel.add(cvPAData);
			if (gettblPymtAccts().getRowCount() > 0)
			{
				gettblPymtAccts().setRowSelectionInterval(0, 0);
			}
			getbtnRevise().setEnabled(!cvPAData.isEmpty());
			getbtnDelete().setEnabled(!cvPAData.isEmpty());
		}
		// end defect 8628 
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		if (cbInit)
		{
			cbInit = false;
			PaymentAccountData lPAUData = new PaymentAccountData();
			lPAUData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			lPAUData.setSubstaId(SystemProperty.getSubStationId());

			getController().processData(
				LocalOptionConstant.GET_PAYMENT_ACCOUNTS,
				lPAUData);
		}
		else
		{
			return;
		}
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(WindowEvent e)
	{
		//empty code block
	}

	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosing(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeactivated(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeiconified(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowIconified(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		//empty code block
	}
}
