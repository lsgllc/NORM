package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * FrmUpdateVerificationFUN016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		09/23/2003	Frame was throwing exception because there
 *							where refrences to tablemodel columns
 *							not there.
 *							This fix made the tabing and Shift+tab work.
 *							changes: keyPressed(),gettblDrawersToReset(),
 *							getbtnYes(),getbtnNo()
 *							defect 6591 Ver 5.1.6					
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3
 * K Harrell	11/01/2005	Correct the text for "Closeout" 
 * 							defect 8379 Ver 5.2.3
 * K Harrell	12/16/2006	Change Vertical Scrollbar to as needed.
 * 							Set table enabled false.
 * 							modify getJScrollPane1(),
 * 							 gettblDrawersToReset() 
 * 							defect 7886 Ver 5.2.3   
 * K Harrell	05/13/2008	Add ESC key processing.  Resize buttons to 
 * 							better reflect 'regular' buttons.
 * 							add keyReleased()
 * 							delete getBuilderData()
 * 							defect 8562 Ver Defect POS A
 * K Harrell	06/08/2009	Screen Cleanup 
 * 							add ivjstcLblLine1,ivjstcLblLine2,
 * 								ivjstcLblLine3, ivjstcLblLine4, get 
 * 								methods
 *   						add caSelectedDrawerTableModel
 * 							add DRWR_DYWT =
 * 							delete ivjstcLblSentence1,ivjstcLblSentence2,
 * 								ivjstcLblSentence3, get methods
 * 							delete selectedDrawerTableModel 
 *							modify DRWRS_DYWT, RESET_FOR_FOLLOWING
 *							modify setData()
 *							defect 9943 Ver Defect_POS_F 					
 * ---------------------------------------------------------------------
 */
/**
 * Screen presents list of selected cash drawers to reset from FUN008.
 *
 * @version	Defect_POS_F	06/08/2009  
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmUpdateVerificationFUN016
	extends RTSDialogBox
	implements ActionListener
{
	// defect 9943 
	private JLabel ivjstcLblLine1 = null;
	private JLabel ivjstcLblLine2 = null;
	private JLabel ivjstcLblLine3 = null;
	private JLabel ivjstcLblLine4 = null;
	// end defect 9943 
	
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjFrmUpdateVerificationFUN016ContentPane1 = null;
	private RTSButton ivjbtnNO = null;
	private RTSButton ivjbtnYes = null;
	private RTSTable ivjtblDrawersToReset = null;
	
	// defect 9943 
	private TMFUN016 caSelectedDrawerTableModel = null;
	// end defect 9943 

	// Object
	private FundsData caFundsData = null;

	// Constants 
	// defect 8379 
	private final static String CLSOUT_INDI_WILL_RESET =
		"The Closeout Indicator will be";
	// end defect 8379 	
	private final static String CONTINUE = "continue?";
	
	// defect 9943 
	private final static String DRWRS_DYWT =
		"drawers.   Do you want to";
	private final static String DRWR_DYWT =
		"drawer.   Do you want to";
	private final static String RESET_FOR_FOLLOWING =
		"reset for the following cash ";
	// end defect 9943		
	private final static String NO = "No";
	private final static String TITLE_FUN016 =
		"Update Verification     FUN016";
	private final static String YES = "Yes";

	private final static String EXCEPT_IN_MAIN =
		"Exception occurred in main() of util.JDialogTxDot";

	/**
	 * FrmUpdateVerificationFUN016 constructor comment.
	 */
	public FrmUpdateVerificationFUN016()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmUpdateVerificationFUN016 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmUpdateVerificationFUN016(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * FrmUpdateVerificationFUN016 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmUpdateVerificationFUN016(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getbtnYes(this))
			{
				getController().processData(
					VCUpdateVerificationFUN016.YES,
					caFundsData);
			}
			else if (aaAE.getSource() == getbtnNo())
			{
				getController().processData(
					VCUpdateVerificationFUN016.NO,
					caFundsData);
			}
		}

		finally
		{
			doneWorking();
		}

	}
	
	/**
	 * Return the ivjbtnNO property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnNo()
	{
		if (ivjbtnNO == null)
		{
			try
			{
				ivjbtnNO = new RTSButton();
				ivjbtnNO.setSize(69, 25);
				ivjbtnNO.setName("ivjbtnNO");
				ivjbtnNO.setMnemonic(KeyEvent.VK_N);
				ivjbtnNO.setText(NO);
				ivjbtnNO.setLocation(147, 266);
				ivjbtnNO.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnNO;
	}
	
	/**
	 * Return the ivjbtnYes property value.
	 * 
	 * @param aaParent JDialog
	 * @return RTSButton
	 */
	private RTSButton getbtnYes(JDialog aaParent)
	{
		if (ivjbtnYes == null)
		{
			try
			{
				ivjbtnYes = new RTSButton();
				ivjbtnYes.setSize(69, 25);
				ivjbtnYes.setName("ivjbtnYes");
				ivjbtnYes.setMnemonic(KeyEvent.VK_Y);
				ivjbtnYes.setText(YES);
				ivjbtnYes.setLocation(48, 266);
				ivjbtnYes.addActionListener(this);
				aaParent.getRootPane().setDefaultButton(ivjbtnYes);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnYes;
	}

	
	/**
	 * Return the ivjFrmUpdateVerificationFUN016ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private javax
		.swing
		.JPanel getFrmUpdateVerificationFUN016ContentPane1()
	{
		if (ivjFrmUpdateVerificationFUN016ContentPane1 == null)
		{
			try
			{
				ivjFrmUpdateVerificationFUN016ContentPane1 =
					new JPanel();
				ivjFrmUpdateVerificationFUN016ContentPane1.setName(
					"ivjFrmUpdateVerificationFUN016ContentPane1");
				ivjFrmUpdateVerificationFUN016ContentPane1.setLayout(
					null);
				ivjFrmUpdateVerificationFUN016ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmUpdateVerificationFUN016ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(275, 300));
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getstcLblLine1(),
					getstcLblLine1().getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getstcLblLine2(),
					getstcLblLine2().getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getstcLblLine3(),
					getstcLblLine3().getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getstcLblLine4(),
					getstcLblLine4().getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getbtnYes(this),
					getbtnYes(this).getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
					getbtnNo(),
					getbtnNo().getName());
				getFrmUpdateVerificationFUN016ContentPane1().add(
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
		return ivjFrmUpdateVerificationFUN016ContentPane1;
	}
	
	/**
	 * Return the ivjJScrollPane1 property value.
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
				ivjJScrollPane1.setName("ivjJScrollPane1");
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
				ivjJScrollPane1.setBounds(33, 96, 198, 148);
				getJScrollPane1().setViewportView(
					gettblDrawersToReset());
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
	 * Return the ivjstcLblLine1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine1()
	{
		if (ivjstcLblLine1 == null)
		{
			try
			{
				ivjstcLblLine1 = new JLabel();
				ivjstcLblLine1.setSize(166, 16);
				ivjstcLblLine1.setName("ivjstcLblLine1");
				ivjstcLblLine1.setText(CLSOUT_INDI_WILL_RESET);
				ivjstcLblLine1.setMaximumSize(
					new java.awt.Dimension(171, 14));
				ivjstcLblLine1.setMinimumSize(
					new java.awt.Dimension(171, 14));
				ivjstcLblLine1.setLocation(36, 19);
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
		return ivjstcLblLine1;
	}
	
	/**
	 * Return the ivjstcLblLine2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine2()
	{
		if (ivjstcLblLine2 == null)
		{
			try
			{
				ivjstcLblLine2 = new JLabel();
				ivjstcLblLine2.setSize(161, 16);
				ivjstcLblLine2.setName("ivjstcLblLine2");
				ivjstcLblLine2.setText(RESET_FOR_FOLLOWING);
				ivjstcLblLine2.setMaximumSize(
					new java.awt.Dimension(161, 14));
				ivjstcLblLine2.setMinimumSize(
					new java.awt.Dimension(161, 14));
				ivjstcLblLine2.setLocation(36, 35);
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
		return ivjstcLblLine2;
	}
	
	/**
	 * Return the ivjstcLblLine3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine3()
	{
		if (ivjstcLblLine3 == null)
		{
			try
			{
				ivjstcLblLine3 = new JLabel();
				ivjstcLblLine3.setSize(151, 15);
				ivjstcLblLine3.setName("ivjstcLblLine3");
				ivjstcLblLine3.setText(DRWRS_DYWT);
				ivjstcLblLine3.setMaximumSize(
					new java.awt.Dimension(151, 14));
				ivjstcLblLine3.setMinimumSize(
					new java.awt.Dimension(151, 14));
				ivjstcLblLine3.setLocation(36, 51);
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
		return ivjstcLblLine3;
	}
	
	/**
	 * Return the ivjstcLblLine4 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine4()
	{
		if (ivjstcLblLine4 == null)
		{
			try
			{
				ivjstcLblLine4 = new JLabel();
				ivjstcLblLine4.setSize(59, 16);
				ivjstcLblLine4.setName("ivjstcLblLine4");
				ivjstcLblLine4.setText(CONTINUE);
				ivjstcLblLine4.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblLine4.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblLine4.setLocation(36, 66);
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
		return ivjstcLblLine4;
	}
	
	/**
	 * Return the tblDrawersToReset property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblDrawersToReset()
	{
		if (ivjtblDrawersToReset == null)
		{
			try
			{
				ivjtblDrawersToReset = new RTSTable();
				ivjtblDrawersToReset.setName("ivjtblDrawersToReset");
				getJScrollPane1().setColumnHeaderView(
					ivjtblDrawersToReset.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblDrawersToReset.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN016());
				ivjtblDrawersToReset.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblDrawersToReset.setBounds(0, 0, 200, 200);
				ivjtblDrawersToReset.setShowVerticalLines(false);
				ivjtblDrawersToReset.setShowHorizontalLines(false);
				// user code begin {1}
				caSelectedDrawerTableModel =
					(TMFUN016) ivjtblDrawersToReset.getModel();
				TableColumn laTableColumnA =
					ivjtblDrawersToReset.getColumn(
						ivjtblDrawersToReset.getColumnName(0));
				laTableColumnA.setPreferredWidth(115);
				ivjtblDrawersToReset.setRowSelectionAllowed(false);
				ivjtblDrawersToReset.init();
				ivjtblDrawersToReset.addActionListener(this);
				ivjtblDrawersToReset.setBackground(Color.white);
				ivjtblDrawersToReset.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblDrawersToReset;
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
			setName("FrmUpdateVerificationFUN016");
			setSize(271, 333);
			setTitle(TITLE_FUN016);
			setContentPane(
				getFrmUpdateVerificationFUN016ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * Key Pressed
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{

			if (getbtnYes(this).hasFocus())
			{
				getbtnNo().requestFocus();
			}
			else if (getbtnNo().hasFocus())
			{
				getbtnYes(this).requestFocus();
			}
		}
		super.keyPressed(aaKE);
	}

	/**
	 * Process KeyReleasedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getController().processData(
				VCUpdateVerificationFUN016.NO,
				caFundsData);
		}
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmUpdateVerificationFUN016 laFrmUpdateVerificationFUN016;
			laFrmUpdateVerificationFUN016 =
				new FrmUpdateVerificationFUN016();
			laFrmUpdateVerificationFUN016.setModal(true);
			laFrmUpdateVerificationFUN016
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmUpdateVerificationFUN016.show();
			java.awt.Insets laInsets =
				laFrmUpdateVerificationFUN016.getInsets();
			laFrmUpdateVerificationFUN016.setSize(
				laFrmUpdateVerificationFUN016.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmUpdateVerificationFUN016.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmUpdateVerificationFUN016.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(EXCEPT_IN_MAIN);
			aeException.printStackTrace(System.out);
		}
	}
	
	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null)
			{ 
				//Add data to table	
				caFundsData = (FundsData) aaDataObject;
				caSelectedDrawerTableModel.add(
					caFundsData.getSelectedCashDrawers());
					
				// defect 9943 
				if (caFundsData.getSelectedCashDrawers().size() == 1) 
				{
					getstcLblLine3().setText(DRWR_DYWT);
				}
				// end defect 9943 
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
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
