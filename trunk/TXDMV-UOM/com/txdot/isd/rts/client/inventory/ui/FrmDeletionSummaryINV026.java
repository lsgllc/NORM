package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.SubstationCache;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmDeletionSummaryINV026.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * BTulsiani
 * JKwik		05/03/2002	Cut off long descriptions for item 
 *							defect 3766
 * MAbs						?
 * Min Wang	   	02/10/2003	Modify setData() for ClassCastException.
 *							Defect 5387.
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
 * Min Wang		08/01/2005	Remove item code from screen
 * 							modify ITM_CD_REASON
 * 							defect 8269 Ver 5.2.2. Fix 6 
 * Ray Rowehl	08/09/2005	Cleanup pass
 * 							Remove key processing from button panel.
 * 							Work on constants.
 * 							Add white space between methods.
 * 							Make use of UtilityMethods to determine if
 * 							site is a county.
 * 							Organize imports.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Setup to use default font and size when 
 * 							doing bold.
 * 							Move constants to appropriate constants
 * 							classes.
 * 							modify getstcLblItmCdDesc()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	12/08/2005	Remove KeyListener
 * 							delete cbKeyPressed
 * 							delete keyPressed(), keyReleased()
 * 							defect 7890 Ver 5.2.3
 * Jeff S		02/08/2006	Set the default focus field to the Delete
 * 							button.
 * 							modify initialize()
 * 							defect 7890 Ver 5.2.3 
 * K Harrell	03/09/2007	Implement SystemProperty.isCounty()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * Min Wang		05/20/2008	Show region substa name
 * 							modify setData()
 * 							defect 9181 Ver Defect_POS_A
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV026 displays the inventory items that have been deleted 
 * during the Delete event.
 *
 * @version Ver Defect_POS_A	05/20/2008
 * @author	Charlie Walker
 * <br>Creation Date:		06/28/2001 13:11:56
 */

public class FrmDeletionSummaryINV026
	extends RTSDialogBox
	implements ActionListener //, KeyListener
{
	private static final String TEXT_FOUR_ARROWS = ">>>> ";

	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjpnlDeleteLocation = null;
	private JLabel ivjstcLblNote = null;
	private RTSButton ivjbtnDel = null;
	private JCheckBox ivjchkViewInvDelRpt = null;
	private JLabel ivjlblDelLoc = null;
	private JLabel ivjstcLblBegNo = null;
	private JLabel ivjstcLblEndNo = null;
	private RTSTextArea ivjstcLblItmCdDesc = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblYr = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JList ivjlstDelInv = null;

	/**
	 * Vector that contains the table data
	 */
	private Vector cvInvAlloctnUIData = new Vector();

	/**
	 * Vector that contains the formatted table data
	 */
	private Vector cvLstData = new Vector();

	/**
	 * String that contains the delete location
	 */
	private String csDelLoc = null;

	/**
	 * Flag to determine whether an item has been deleted
	 */
	private boolean cbDisableCancel = false;

	/**
	 * String that holds 36 spaces used in formatting itm cd and desc
	 */
	private static String ITMCDDESCSPACES =
		new String("                                   ");

	/**
	 * String that holds 5 spaces used in formatting the yr
	 */
	private static String YRSPACES = new String("     ");

	/**
	 * String that holds 11 spaces used in formatting the qty
	 */
	private static String QTYSPACES = new String("           ");

	/**
	 * String that holds 13 spaces used in formatting the beg no
	 */
	private static String BEGNOSPACES = new String("             ");

	/**
	 * String that holds 13 spaces used in formatting the end no
	 */
	private static String ENDNOSPACES = new String("             ");

	/**
	 * FrmDeletionSummaryINV026 constructor comment.
	 */
	public FrmDeletionSummaryINV026()
	{
		super();
		initialize();
	}

	/**
	 * FrmDeletionSummaryINV026 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmDeletionSummaryINV026(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmDeletionSummaryINV026 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDeletionSummaryINV026(JFrame aaParent)
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
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// Determines what actions to take when Allocate, Enter, 
			// Cancel, or Help are pressed.
			if (aaAE.getSource() == getbtnDel())
			{
				getController().processData(
					VCDeletionSummaryINV026.DELETE,
					null);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& !getchkViewInvDelRpt().isSelected())
			{
				getController().processData(
					AbstractViewController.ENTER,
					cvInvAlloctnUIData);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& getchkViewInvDelRpt().isSelected())
			{
				getController().processData(
					VCDeletionSummaryINV026.GENERATE_DELETE_REPORT,
					cvInvAlloctnUIData);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV026);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnDel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDel()
	{
		if (ivjbtnDel == null)
		{
			try
			{
				ivjbtnDel = new RTSButton();
				ivjbtnDel.setName("btnDel");
				ivjbtnDel.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjbtnDel.setText(InventoryConstant.TXT_DELETE);
				ivjbtnDel.setBounds(13, 289, 71, 26);
				// user code begin {1}
				getbtnDel().addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDel;
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
				ivjButtonPanel1.setBounds(91, 284, 233, 62);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the chkViewInventoryDeleteReport property value.
	 * 
	 * @return JCheckBox
	 */

	private JCheckBox getchkViewInvDelRpt()
	{
		if (ivjchkViewInvDelRpt == null)
		{
			try
			{
				ivjchkViewInvDelRpt = new JCheckBox();
				ivjchkViewInvDelRpt.setName("chkViewInvDelRpt");
				ivjchkViewInvDelRpt.setMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjchkViewInvDelRpt.setText(
					InventoryConstant.TXT_VIEW_INVENTORY_DELETE_REPORT);
				ivjchkViewInvDelRpt.setBounds(337, 278, 206, 42);
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
		return ivjchkViewInvDelRpt;
	}

	/**
	 * Return the JDialogContentPane property value.
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
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(
					getpnlDeleteLocation(),
					getpnlDeleteLocation().getName());
				getJDialogContentPane().add(
					getstcLblNote(),
					getstcLblNote().getName());
				getJDialogContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getJDialogContentPane().add(
					getchkViewInvDelRpt(),
					getchkViewInvDelRpt().getName());
				getJDialogContentPane().add(
					getbtnDel(),
					getbtnDel().getName());
				getJDialogContentPane().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getJDialogContentPane().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getJDialogContentPane().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getJDialogContentPane().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getJDialogContentPane().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getJDialogContentPane().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
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
		return ivjJDialogContentPane;
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
				ivjJScrollPane1.setBounds(13, 133, 524, 143);
				getJScrollPane1().setViewportView(getlstDelInv());
				// user code begin {1}
				// defect 7890 
				ivjJScrollPane1.setFocusable(false);
				// end defect 7890 
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
	 * Return the lblDeleteLocation property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblDelLoc()
	{
		if (ivjlblDelLoc == null)
		{
			try
			{
				ivjlblDelLoc = new JLabel();
				ivjlblDelLoc.setName("lblDelLoc");
				ivjlblDelLoc.setText(
					InventoryConstant.TXT_DELETE_LOCATION);
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
		return ivjlblDelLoc;
	}

	/**
	 * Return the JList1 property value.
	 * 
	 * @return jJList
	 */

	private JList getlstDelInv()
	{
		if (ivjlstDelInv == null)
		{
			try
			{
				ivjlstDelInv = new JList();
				ivjlstDelInv.setName("lstDelInv");
				ivjlstDelInv.setFont(
					new java.awt.Font(
						CommonConstant.FONT_JLIST,
						java.awt.Font.PLAIN,
						12));
				ivjlstDelInv.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// defect 7890 
				ivjlstDelInv.setFocusable(false);
				// end defect 7890 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlstDelInv;
	}

	/**
	 * Return the pnlDeleteLocation property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getpnlDeleteLocation()
	{
		if (ivjpnlDeleteLocation == null)
		{
			try
			{
				ivjpnlDeleteLocation = new JPanel();
				ivjpnlDeleteLocation.setName("pnlDeleteLocation");
				ivjpnlDeleteLocation.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_DELETE_LOCATION_COLON));
				ivjpnlDeleteLocation.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlDeleteLocation.setBounds(13, 15, 520, 52);

				java.awt.GridBagConstraints constraintslblDelLoc =
					new java.awt.GridBagConstraints();
				constraintslblDelLoc.gridx = 1;
				constraintslblDelLoc.gridy = 1;
				constraintslblDelLoc.weightx = 1.0;
				constraintslblDelLoc.ipadx = 386;
				constraintslblDelLoc.insets =
					new java.awt.Insets(17, 22, 18, 22);
				getpnlDeleteLocation().add(
					getlblDelLoc(),
					constraintslblDelLoc);
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
		return ivjpnlDeleteLocation;
	}

	/**
	 * Return the stcLblBegNo property value.
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
				ivjstcLblBegNo.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblBegNo.setBounds(383, 102, 69, 28);
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
				ivjstcLblEndNo.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblEndNo.setBounds(476, 101, 56, 28);
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
	 * Return the stcLblItmCdDesc property value.
	 * 
	 * @return RTSTextArea
	 */

	private RTSTextArea getstcLblItmCdDesc()
	{
		if (ivjstcLblItmCdDesc == null)
		{
			try
			{
				ivjstcLblItmCdDesc = new RTSTextArea();
				ivjstcLblItmCdDesc.setName("stcLblItmCdDesc");
				ivjstcLblItmCdDesc.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
				//new java.awt.Font("Arial", 1, 12);
				ivjstcLblItmCdDesc.setText(
					InventoryConstant.TXT_ITEM_DESCRIPTION_REASON_DESC);
				ivjstcLblItmCdDesc.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjstcLblItmCdDesc.setBounds(17, 95, 180, 34);
				//ivjstcLblItmCdDesc.setForeground(
				ivjstcLblItmCdDesc.setEditable(false);
				//	new java.awt.Color(102, 102, 153));
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
		return ivjstcLblItmCdDesc;
	}

	/**
	 * Return the stcLblNote property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblNote()
	{
		if (ivjstcLblNote == null)
		{
			try
			{
				ivjstcLblNote = new JLabel();
				ivjstcLblNote.setName("stcLblNote");
				ivjstcLblNote.setText(
					ErrorsConstant.MSG_DELETE_AREA_WARNING);
				ivjstcLblNote.setBounds(75, 78, 399, 14);
				ivjstcLblNote.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
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
		return ivjstcLblNote;
	}

	/**
	 * Return the stcLblQty property value.
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
				ivjstcLblQty.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblQty.setBounds(309, 101, 61, 28);
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
	 * Return the stcLblYr property value.
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
				ivjstcLblYr.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblYr.setBounds(255, 101, 45, 28);
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
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		///* Uncomment the following lines to print uncaught exceptions 
		// * to stdout */
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//exception.printStackTrace(System.out);
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
			setName(ScreenConstant.INV026_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(550, 350);
			setModal(true);
			setTitle(ScreenConstant.INV026_FRAME_TITLE);
			setContentPane(getJDialogContentPane());
			// defect 7890
			// Set the default focus field to the Allocate Button
			setDefaultFocusField(getbtnDel());
			// end defect 7890
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Method to see if the cancel button has been disabled.  
	 * Used on the vc to determine whether or not to perform the cancel
	 * code when the esc key is pressed.
	 * 
	 * @return boolean
	 */
	public boolean isCancelEnabled()
	{
		return !cbDisableCancel;
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
			FrmDeletionSummaryINV026 laFrmDeletionSummaryINV026;
			laFrmDeletionSummaryINV026 = new FrmDeletionSummaryINV026();
			laFrmDeletionSummaryINV026.setModal(true);
			laFrmDeletionSummaryINV026
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDeletionSummaryINV026.show();
			java.awt.Insets insets =
				laFrmDeletionSummaryINV026.getInsets();
			laFrmDeletionSummaryINV026.setSize(
				laFrmDeletionSummaryINV026.getWidth()
					+ insets.left
					+ insets.right,
				laFrmDeletionSummaryINV026.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmDeletionSummaryINV026.setVisibleRTS(true);
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
		// Get substation name and display it
		if (csDelLoc == null)
		{
			// defect 9181
			// found that defect 9085 cause defect 9181.
			// remove county only requirement for displaying name.
			// defect 9085 
			//if (UtilityMethods.isCounty())
			//if (SystemProperty.isCounty())
			//{
				SubstationData laSubstaData =
					SubstationCache.getSubsta(
						SystemProperty.getOfficeIssuanceNo(),
						SystemProperty.getSubStationId());
				if (laSubstaData
					.getSubstaName()
					.equals(CommonConstant.STR_SPACE_EMPTY)
					|| laSubstaData.getSubstaName() == null)
				{
					csDelLoc = InventoryConstant.TXT_MAIN_OFFICE_UC;
				}
				else
				{
					csDelLoc = laSubstaData.getSubstaName();
				}
			//}
			//else
			//{
			//	csDelLoc = CommonConstant.STR_SPACE_EMPTY;
			//}
			// end defect 9085 
			// end defect 9181
			getlblDelLoc().setText(csDelLoc);
		}
		else if (aaData != null)
		{
			// Display the deleted inventory list data
			if (aaData instanceof Vector)
			{
				Vector lvDeletedInv = (Vector) aaData;
				for (int i = 0; i < lvDeletedInv.size(); i++)
				{
					cvInvAlloctnUIData.add(
						(InventoryAllocationUIData) lvDeletedInv.get(
							i));
				}
				setlstDelInv();
			}
			else
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					ErrorsConstant.MSG_UNEXPECTED_PROBLEM
						+ aaData.toString());
			}
		}
		// If an item has been deleted, disable the cancel button so 
		// the user can't cancel
		if (cvInvAlloctnUIData.size() > CommonConstant.ELEMENT_0)
		{
			getchkViewInvDelRpt().setSelected(true);
			getButtonPanel1().getBtnCancel().setEnabled(false);
			cbDisableCancel = true;
		}
	}

	/**
	 * Construct the list display text for the deleted inventory items.
	 */
	private void setlstDelInv()
	{
		String lsInvRnge = new String();
		String lsTmp = new String();
		int liTmp = 0;

		for (int i = cvLstData.size() / 2;
			i < cvInvAlloctnUIData.size();
			i++)
		{
			InventoryAllocationUIData laIAUID =
				(InventoryAllocationUIData) cvInvAlloctnUIData.get(i);

			liTmp = laIAUID.getItmCdDesc().length();
			if (ITMCDDESCSPACES.length() > liTmp + 1)
			{
				lsTmp = ITMCDDESCSPACES.substring(liTmp + 1);
				lsInvRnge = laIAUID.getItmCdDesc() + lsTmp;
			}
			else
			{
				String lsTemp =
					laIAUID.getItmCdDesc().substring(
						0,
						ITMCDDESCSPACES.length() - 1);
				lsInvRnge = lsTemp;
			}

			liTmp = String.valueOf(laIAUID.getInvItmYr()).length();
			lsTmp = YRSPACES.substring(liTmp + 1);
			if (laIAUID.getInvItmYr() == 0)
			{
				lsInvRnge =
					lsInvRnge + CommonConstant.STR_SPACE_ONE + lsTmp;
			}
			else
			{
				lsInvRnge = lsInvRnge + laIAUID.getInvItmYr() + lsTmp;
			}

			liTmp = String.valueOf(laIAUID.getInvQty()).length();
			lsTmp = QTYSPACES.substring(liTmp + 1);
			lsInvRnge = lsInvRnge + lsTmp + laIAUID.getInvQty();

			liTmp = laIAUID.getInvItmNo().length();
			lsTmp = BEGNOSPACES.substring(liTmp + 1);
			lsInvRnge = lsInvRnge + lsTmp + laIAUID.getInvItmNo();

			liTmp = laIAUID.getInvItmEndNo().length();
			lsTmp = ENDNOSPACES.substring(liTmp + 1);
			lsInvRnge = lsInvRnge + lsTmp + laIAUID.getInvItmEndNo();

			cvLstData.add(lsInvRnge);

			lsTmp = TEXT_FOUR_ARROWS + laIAUID.getDelInvReasnTxt();

			cvLstData.add(lsTmp);
		}
		getlstDelInv().setListData(cvLstData);
	}
}