package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.CreditCardFeesConstants;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmMaintainCreditCardFeesOPT006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo	03/12/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		02/25/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	09/06/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	09/03/2008	class cleanup
 * 							delete getBuilderData()
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	08/22/2009	Implement RTSButtonGroup, constants for 
 * 							 mnemonics.  
 * 							add caRTSButtonGroup, caCreditCardFeeTableModel
 * 							delete ctblModel
 * 							modify actionPerformed(), initialize()
 * 							defect 8628 Ver Defect_POS_F 
 * Min Wang 03/09/2010		Fix inappropriate Msg is displayed when 
 * 							no Credit Card Fee changes were made.
 * 							add ciSetDataCount
 * 							modify actionPerformed(), setData() 
 * 							defect 8569 Ver POS_640
 * ---------------------------------------------------------------------
 */

/**
 * Maintains credit card fee charges.
 *
 * @version	Ver POS_640		03/09/2010
 * @author	Michael Abernethy
 * <br>Creation Date:		04/25/2002 9:44:53 
 */

public class FrmMaintainCreditCardFeesOPT006
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSTable ivjtblFees = null;

	// defect 8628	
	private TMOPT006 caCreditCardFeeTableModel;

	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628  

	private Vector cvFees;
	
	// defect 8569
	private int ciSetDataCount = 0;
	// end defect 8569

	private final static String OPT006_FRAME_TITLE =
		"Maintain Credit Card Fees   OPT006";
	private final static String OPT006_FRAME_NAME =
		"FrmMaintainCreditCardFeesOPT006";
	private final static String MSG_CREDIT_CARD_FEE_CHANGE_RESTART =
		"Credit Card Fee changes will be available upon restart.<br>Exit to Main Menu?";

	/**
	 * FrmMaintainCreditCardFeesOPT006 constructor comment.
	 */
	public FrmMaintainCreditCardFeesOPT006()
	{
		super();
		initialize();
	}

	/**
	 * FrmMaintainCreditCardFeesOPT006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmMaintainCreditCardFeesOPT006(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmMaintainCreditCardFeesOPT006 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmMaintainCreditCardFeesOPT006(Frame aaOwner)
	{
		super(aaOwner);
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

			// defect 8628 
			// Reorder; No need to populate Hashmap if Cancel 
			// CANCEL 
			if (aaAE.getSource() == getbtnCancel())
			{
				// defect 8569 
				// Only present exception if data modified
				int liRetCode = RTSException.YES; 
				if (ciSetDataCount >1)
				{
					RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						MSG_CREDIT_CARD_FEE_CHANGE_RESTART,
						ScreenConstant.CTL001_FRM_TITLE);
					liRetCode = leRTSEx.displayError(this);
				}
				// end defect 8569
				if (liRetCode == RTSException.YES)
				{
					getController().processData(
						AbstractViewController.CANCEL,
						cvFees);
				}
			}
			else
			{
				Map laMap = new HashMap();
				laMap.put(CreditCardFeesConstants.DATA, cvFees);
				laMap.put(
					CreditCardFeesConstants.SELECTED_ROW,
					new Integer(gettblFees().getSelectedRow()));

				//TODO Each of these take the same Action 
				// Why not consolidate into just process? 

				// ADD 
				if (aaAE.getSource() == getbtnAdd())
				{
					laMap.put(
						CreditCardFeesConstants.TYPE,
						CreditCardFeesConstants.ADD);
					getController().processData(
						VCMaintainCreditCardFeesOPT006.ADD,
						laMap);
				}
				// REVISE 
				else if (aaAE.getSource() == getbtnRevise())
				{
					laMap.put(
						CreditCardFeesConstants.TYPE,
						CreditCardFeesConstants.REVISE);
					getController().processData(
						VCMaintainCreditCardFeesOPT006.REVISE,
						laMap);
				}
				// DELETE 
				else if (aaAE.getSource() == getbtnDelete())
				{
					laMap.put(
						CreditCardFeesConstants.TYPE,
						CreditCardFeesConstants.DELETE);
					getController().processData(
						AbstractViewController.DELETE,
						laMap);
				}
				// end defect 8628 
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnAdd property value.
	 * 
	 * @return RTSButton
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
				ivjbtnAdd.setMnemonic(KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				// user code begin {1}
				ivjbtnAdd.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
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
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjbtnDelete.setMnemonic(KeyEvent.VK_D);
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				// user code begin {1}
				ivjbtnDelete.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjbtnRevise.setMnemonic(KeyEvent.VK_R);
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				// user code begin {1}
				ivjbtnRevise.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				getJScrollPane1().setViewportView(gettblFees());
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
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 381;
				constraintsJScrollPane1.ipady = 172;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(34, 31, 17, 31);
				getRTSDialogBoxContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				GridBagConstraints constraintsbtnAdd =
					new java.awt.GridBagConstraints();
				constraintsbtnAdd.gridx = 1;
				constraintsbtnAdd.gridy = 2;
				constraintsbtnAdd.ipadx = 44;
				constraintsbtnAdd.insets = new Insets(17, 12, 34, 6);
				getRTSDialogBoxContentPane().add(
					getbtnAdd(),
					constraintsbtnAdd);

				GridBagConstraints constraintsbtnRevise =
					new GridBagConstraints();
				constraintsbtnRevise.gridx = 2;
				constraintsbtnRevise.gridy = 2;
				constraintsbtnRevise.ipadx = 28;
				constraintsbtnRevise.insets = new Insets(17, 6, 34, 6);
				getRTSDialogBoxContentPane().add(
					getbtnRevise(),
					constraintsbtnRevise);

				GridBagConstraints constraintsbtnDelete =
					new GridBagConstraints();
				constraintsbtnDelete.gridx = 3;
				constraintsbtnDelete.gridy = 2;
				constraintsbtnDelete.ipadx = 30;
				constraintsbtnDelete.insets = new Insets(17, 6, 34, 6);
				getRTSDialogBoxContentPane().add(
					getbtnDelete(),
					constraintsbtnDelete);

				GridBagConstraints constraintsbtnCancel =
					new GridBagConstraints();
				constraintsbtnCancel.gridx = 4;
				constraintsbtnCancel.gridy = 2;
				constraintsbtnCancel.ipadx = 28;
				constraintsbtnCancel.insets =
					new java.awt.Insets(17, 6, 34, 13);
				getRTSDialogBoxContentPane().add(
					getbtnCancel(),
					constraintsbtnCancel);
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
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblFees()
	{
		if (ivjtblFees == null)
		{
			try
			{
				ivjtblFees = new RTSTable();
				ivjtblFees.setName("tblFees");
				getJScrollPane1().setColumnHeaderView(
					ivjtblFees.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblFees.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblFees.setModel(new TMOPT006());
				ivjtblFees.setShowVerticalLines(false);
				ivjtblFees.setShowHorizontalLines(false);
				ivjtblFees.setIntercellSpacing(new Dimension(0, 0));
				ivjtblFees.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caCreditCardFeeTableModel =
					(TMOPT006) ivjtblFees.getModel();
				ivjtblFees.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblFees.init();
				ivjtblFees.setColumnSize(0, 115);
				ivjtblFees.setColumnSize(1, 115);
				ivjtblFees.setColumnSize(2, 115);
				ivjtblFees.setColumnAlignment(0, RTSTable.RIGHT);
				ivjtblFees.setColumnAlignment(1, RTSTable.CENTER);
				ivjtblFees.setColumnAlignment(2, RTSTable.CENTER);

				ivjtblFees
					.getSelectionModel()
					.addListSelectionListener(
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
		return ivjtblFees;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
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
			setName(OPT006_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(465, 325);
			setTitle(OPT006_FRAME_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(getRTSDialogBoxContentPane());
			// defect 8628 
			caRTSButtonGroup.add(getbtnAdd());
			caRTSButtonGroup.add(getbtnRevise());
			caRTSButtonGroup.add(getbtnDelete());
			caRTSButtonGroup.add(getbtnCancel());
			// end defect 8628 
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run 
	 * as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmMaintainCreditCardFeesOPT006 laFrmMaintainCreditCardFeesOPT006;
			laFrmMaintainCreditCardFeesOPT006 =
				new FrmMaintainCreditCardFeesOPT006();
			laFrmMaintainCreditCardFeesOPT006.setModal(true);
			laFrmMaintainCreditCardFeesOPT006
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmMaintainCreditCardFeesOPT006.show();
			Insets insets =
				laFrmMaintainCreditCardFeesOPT006.getInsets();
			laFrmMaintainCreditCardFeesOPT006.setSize(
				laFrmMaintainCreditCardFeesOPT006.getWidth()
					+ insets.left
					+ insets.right,
				laFrmMaintainCreditCardFeesOPT006.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmMaintainCreditCardFeesOPT006.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information to 
	 * the view
	 *
	 * All future date ranges must be deleted before can delete current
	 * 
	 * If future range exists:
	 *   Can modify only amount and type for current row.  
	 * Else 
	 *   Can modify end date, amount and type. 
	 * 
	 * No prior ranges are visible 
	 *  
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 8569 
		// Data modified if setData() called more than once.
		ciSetDataCount = ciSetDataCount + 1;  
		// end defect 8569

		cvFees = (Vector) aaDataObject;
		caCreditCardFeeTableModel.add(cvFees);

		if (cvFees.size() > 1)
		{
			getbtnDelete().setEnabled(false);
			getbtnRevise().setEnabled(true);
			gettblFees().setRowSelectionInterval(0, 0);
		}
		else if (cvFees.size() == 1)
		{
			getbtnDelete().setEnabled(true);
			getbtnRevise().setEnabled(true);
			gettblFees().setRowSelectionInterval(0, 0);
		}
		else
		{
			getbtnDelete().setEnabled(false);
			getbtnRevise().setEnabled(false);
		}
		gettblFees().requestFocus();
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		if (gettblFees().getSelectedRow() == cvFees.size() - 1)
		{
			getbtnDelete().setEnabled(true);
		}
		else
		{
			getbtnDelete().setEnabled(false);
		}
	}
}
