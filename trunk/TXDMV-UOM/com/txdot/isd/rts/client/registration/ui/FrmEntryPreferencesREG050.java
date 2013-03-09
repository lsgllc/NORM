package com.txdot.isd.rts.client.registration.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.FileUtil;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmEntryPreferencesREG050.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class.  Ver 5.2.0	
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	07/18/2005	Modify code for move to Java 1.4. Cleanup to
 * 							standards (if-else braces, var names, etc.)
 * 							and remove unused variables.
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	12/14/2005	Arrow buttons were selecting 
 * 							renamed ciSelctdRadioButton to 
 * 							 ciRadioButtonWithFocus
 * 							modify keyPressed()
 * 							defect 7894 Ver 5.2.3  
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciRadioButtonWithFocus
 * 							modify getJPanel1(), initialize(), 
 * 								getradioKeyboard(), actionPerformed(),
 * 								getradioSubconSuppliedDiskette()
 * 							defect 7894 Ver 5.2.3
 * B Hargrove   06/03/2009 	Add Flashdrive option to RSPS Subcon.
 * 							add STRMSGFLASH, SUBCON_FLASHDRIVE,
 *                   		modify actionPerformed(), getJPanel1(), 
 * 					 		initialize(), 
 * 							rename ivjradioSubconSuppliedDiskette to
 * 							ivjradioSubconSuppliedMedia,
 * 							getradioSubconSuppliedDiskette() to
 * 							getradioSubconSuppliedMedia()
 * 					 		remove builder data, keyPressed()
 * 							defect 10064 Ver Defect_POS_F  
 * ---------------------------------------------------------------
 *
 */
/**
 * Frame Entry Preferences REG050
 * 
 * @version	Defect_POS_F	06/03/2009 
 * @author 	Administrator
 * <br>Creation date:		06/26/2001
 */
public class FrmEntryPreferencesREG050
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmEntryPreferencesDTA001ContentPane = null;
	private JRadioButton ivjradioKeyboard = null;
	// defect 10064
	//private JRadioButton ivjradioSubconSuppliedDiskette = null;
	private JRadioButton ivjradioSubconSuppliedMedia = null;
	// end defect 10064
	
	// defect 7894
	//	// Array used allow for correct keyboard navigation
	//	private JRadioButton[] carrRadioButton = new JRadioButton[2];
	//	// Int used to specify which radio button is selected
	//	private int ciRadioButtonWithFocus = 0;
	// end defect 7894
	
	private static final String KEY_SCAN = "Keyboard/Scanner";
	//private static final String OTHER = "Other (MCO, O/S, Title,Etc.)";
	private static final String SELECT_CHOICE = "Select Choice:";
	private static final String STRMSG =
	// defect 10064
	//	"Insert diskette containing subcontractor renewal records " +
		"Insert external media containing subcontractor renewal records " +
		"and press \"Enter\"";
	//private static final String SUBCON_DISK =
	//	"Subcontractor-supplied diskette";
	private static final String SUBCON_FILE = "subcon.dat";
	// end defect 10064
	private static final String TITLE_REG050 =	
		"Entry Preferences     REG050";
	private static final String TITLE_REG051 =	
		"Copy Instructions    REG051";
		
	
	/**
	 * FrmEntryPreferencesREG050 constructor comment.
	 */
	public FrmEntryPreferencesREG050()
	{
		super();
		initialize();
	}
	/**
	 * FrmEntryPreferencesREG050 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmEntryPreferencesREG050(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmEntryPreferencesREG050 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmEntryPreferencesREG050(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when Enter/Cancel/Help is pressed
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10064
				// Set for Subcon file on diskette or flashdrive
				if (getradioSubconSuppliedMedia().isSelected())
				{
					try
					{
							
						if (getradioSubconSuppliedMedia().isSelected())
						{
							RTSException leEx =
								new RTSException(
									RTSException.INFORMATION_VALIDATION,
									STRMSG,
									TITLE_REG051);
							leEx.setHelpURL(RTSHelp.DTA003);
							int liRet = leEx.displayError(this);
							if (liRet == RTSException.ENTER)
							{
								FileUtil.determineIfExternalDriveReady(
									SUBCON_FILE);
									
								getController().processData(
									VCEntryPreferencesREG050.SUBCON_SUPP_DISK,
									null);
							}
							else if (liRet == RTSException.CANCEL)
							{
								getController().processData(
									AbstractViewController.CANCEL,
									null);
							}
							// defect 8177
							else
							{
								RTSHelp.displayHelp(RTSHelp.REG051);
							}
							// end defect 8177
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(this);
					}
					catch (Throwable aeIVJEx)
					{
						handleException(aeIVJEx);
					}
					
				}
				else if (getradioKeyboard().isSelected())
				{
					getController().processData(
						VCEntryPreferencesREG050.KEYBOARD,
						null);
				}
				// end defect 10064
			}
			else if (aaAE.getSource() ==
				 getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				// defect 8177
				// defect 7894
				//if (carrRadioButton[0].isSelected())
				if (getradioSubconSuppliedMedia().isSelected())
				// end defect 7894
				{
					// diskette entry
					RTSHelp.displayHelp(RTSHelp.REG050A);
				}
				else
				{
					// keyboard entry
					RTSHelp.displayHelp(RTSHelp.REG050B);
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
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
				ivjButtonPanel1 =
					new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(42, 165, 260, 46);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// defect 7894
				// remove key listener
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the FrmEntryPreferencesREG050ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmEntryPreferencesDTA001ContentPane()
	{
		if (ivjFrmEntryPreferencesDTA001ContentPane == null)
		{
			try
			{
				ivjFrmEntryPreferencesDTA001ContentPane =
					new javax.swing.JPanel();
				ivjFrmEntryPreferencesDTA001ContentPane.setName(
					"FrmEntryPreferencesDTA001ContentPane");
				ivjFrmEntryPreferencesDTA001ContentPane.setLayout(null);
				ivjFrmEntryPreferencesDTA001ContentPane.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmEntryPreferencesDTA001ContentPane.setMinimumSize(
					new java.awt.Dimension(900, 199));
				getFrmEntryPreferencesDTA001ContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmEntryPreferencesDTA001ContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmEntryPreferencesDTA001ContentPane;
	}
	/**
	 * Return the JPaREG050nel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_CHOICE));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(223, 97));
				ivjJPanel1.setBounds(27, 18, 297, 124);
				getJPanel1().add(
					getradioSubconSuppliedMedia(),
					getradioSubconSuppliedMedia().getName());
				getJPanel1().add(
					getradioKeyboard(),
					getradioKeyboard().getName());
				// user code begin {1}
				// defect 10064				
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						// defect 10064
						//"Select Choice");
						SELECT_CHOICE);
						// end defect 10064
				getJPanel1().setBorder(laBorder);
				// defect 7894
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laBorderGroup = new RTSButtonGroup();
				laBorderGroup.add(getradioSubconSuppliedMedia());
				laBorderGroup.add(getradioKeyboard());
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}
	
	/**
	 * Return the radioKeyboard property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioKeyboard()
	{
		if (ivjradioKeyboard == null)
		{
			try
			{
				ivjradioKeyboard = new javax.swing.JRadioButton();
				ivjradioKeyboard.setName("radioKeyboard");
				ivjradioKeyboard.setMnemonic(KeyEvent.VK_K);
				ivjradioKeyboard.setText(KEY_SCAN);
				ivjradioKeyboard.setMaximumSize(
					new java.awt.Dimension(175, 22));
				//ivjradioKeyboard.setActionCommand(OTHER);
				ivjradioKeyboard.setBounds(19, 68, 171, 22);
				ivjradioKeyboard.setMinimumSize(
					new java.awt.Dimension(175, 22));
				// user code begin {1}
				// defect 7894
				// remove key listener
				//ivjradioKeyboard.addKeyListener(this);
				// remove action listener
				//ivjradioKeyboard.addActionListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioKeyboard;
	}
	/**
	 * Return the radioDealerSuppliedMedia property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioSubconSuppliedMedia()
	{
		if (ivjradioSubconSuppliedMedia == null)
		{
			try
			{
				ivjradioSubconSuppliedMedia =
					new javax.swing.JRadioButton();
				ivjradioSubconSuppliedMedia.setName(
					"radioSubconSuppliedMedia");
				ivjradioSubconSuppliedMedia.setMnemonic(KeyEvent.VK_S);
				ivjradioSubconSuppliedMedia.setText("Subcontractor-supplied external media");
				ivjradioSubconSuppliedMedia.setMaximumSize(
					new java.awt.Dimension(112, 22));
				//ivjradioSubconSuppliedDiskette.setActionCommand(
				//	"Texas Transfer");
				ivjradioSubconSuppliedMedia.setBounds(19, 35, 254, 22);
				ivjradioSubconSuppliedMedia.setMinimumSize(
					new java.awt.Dimension(112, 22));
				// user code begin {1}
				//ivjradioDealerSuppliedDiskette.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// defect 7894
				// remove key listener
				//ivjradioSubconSuppliedDiskette.addKeyListener(this);
				// remove action listener
				//ivjradioSubconSuppliedDiskette.addActionListener(this);
				// end defect 7894
				
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioSubconSuppliedMedia;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
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
			setLocation(10, 10);
			// defect 7894
			//carrRadioButton[0] = getradioSubconSuppliedDiskette();
			//carrRadioButton[1] = getradioKeyboard();
			// end defect 7894
			// user code end
			setName("FrmEntryPreferencesREG050");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(365, 252);
			setModal(true);
			setTitle(TITLE_REG050);
			setContentPane(getFrmEntryPreferencesDTA001ContentPane());
		}
		catch (java.lang.Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());
		getradioSubconSuppliedMedia().setSelected(true);
		getradioSubconSuppliedMedia().requestFocus();
		// user code end
	}
	
	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmEntryPreferencesREG050 laFrmEntryPreferencesREG050;
			laFrmEntryPreferencesREG050 =
				new FrmEntryPreferencesREG050();
			laFrmEntryPreferencesREG050.setModal(true);
			laFrmEntryPreferencesREG050
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmEntryPreferencesREG050.show();
			java.awt.Insets laInsets =
				laFrmEntryPreferencesREG050.getInsets();
			laFrmEntryPreferencesREG050.setSize(
				laFrmEntryPreferencesREG050.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmEntryPreferencesREG050.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmEntryPreferencesREG050.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
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
		// empty code block
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
