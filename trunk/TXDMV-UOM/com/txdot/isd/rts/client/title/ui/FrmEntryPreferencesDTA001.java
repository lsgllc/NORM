package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;

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
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * FrmEntryPreferencesDTA001.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Rajangam				validations update
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()    
 * T Pederson	05/17/2002	Added setHelpURL to actionPerformed() so 
 * 							that help button would go to correct section
 * 							in users guide help
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 * 							user help guide so had to make changes
 *							in actionPerformed().
 * J Rue		07/21/2004	Incorporate prior 5.1.6 DTA defects to 5.2.1
 *	`						Defect 7350 Ver 5.2.1
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Comment out unused variables
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/21/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							defect 8240 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/09/2005	Fixed arrow key handling to follow 5.2.3
 * 							standard.
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3   
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciSelctdRadioButton
 * 							modify getJPanel1(), initialize()
 * 							defect 7898 Ver 5.2.3    
 * K Harrell	02/11/2007	delete getBuilderData(), OTHER_MSO_TTL,
 * 							TEXAS_TRANSFER
 * 							modify getradioDealerSuppliedDiskette(),
 * 							  getradioKeyboard()
 * 							defect 9085 Ver Special Plates   
 * B Hargrove   06/03/2009 	Add Flashdrive option to DTA.
 * 							add DLRTITLE_FILE
 *                   		modify STRMSG, rename
 * 							ivjradioDealerSuppliedDiskette to
 * 							ivjradioDealerSuppliedMedia,
 * 							actionPerformed(), getJPanel1(),
 * 							getradioDealerDiskette() rename to 
 * 							getradioDealerExternalMedia()
 * 							delete DEALER_SUPPL_DISK, FILE_FROM
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	11/30/2009	DTA Cleanup
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * Frame Entry Preferences DTA001
 * 
 * @version	Defect_POS_H	11/30/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:		06/26/2001 16:03:21
 */
public class FrmEntryPreferencesDTA001
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmEntryPreferencesDTA001ContentPane = null;
	private JRadioButton ivjradioDealerSuppliedMedia = null;
	private JRadioButton ivjradioKeyboard = null;

	// Constants 
	// For DTA001 Frame  
	private final static String SELECT_CHOICE = "Select Choice:";
	private final static String KEYBOARD = "Keyboard";
	private final static String EXT_MEDIA =
		"Dealer-supplied external media";
	private static final String DLRTITLE_FILE = "DLRTITLE.DAT";

	// For DTA003 Exception 
	private final static String DTA003_TITLE =
		"Copy Instructions    DTA003";
	private static final String DTA003_MSG =
		"Insert external media containing title application file "
			+ "and press \"Enter\".";

	/**
	 * FrmEntryPreferencesDTA001 constructor
	 */
	public FrmEntryPreferencesDTA001()
	{
		super();
		initialize();
	}

	/**
	 * FrmEntryPreferencesDTA001 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmEntryPreferencesDTA001(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmEntryPreferencesDTA001 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmEntryPreferencesDTA001(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is pressed
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
			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10075
				// Set for DTA file on diskette or flashdrive
				
				// External Media  
				if (getradioDealerExternalMedia().isSelected())
				{
					try
					{
						RTSException leRTSEx =
							new RTSException(
								RTSException.INFORMATION_VALIDATION,
								DTA003_MSG,
								DTA003_TITLE);

						leRTSEx.setHelpURL(RTSHelp.DTA003);

						int liRet = leRTSEx.displayError(this);

						if (liRet == RTSException.ENTER)
						{
							FileUtil.determineIfExternalDriveReady(
								DLRTITLE_FILE);

							getController().processData(
								VCEntryPreferencesDTA001
									.DEALER_SUPP_MEDIA,
								null);
						}
						else if (liRet == RTSException.CANCEL)
						{
							getController().processData(
								AbstractViewController.CANCEL,
								null);
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
				// end defect 10075
				// Keyboard  
				else
				{
					getController().processData(
						VCEntryPreferencesDTA001.KEYBOARD,
						null);
				}
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				if (getradioDealerExternalMedia().isSelected())
				{
					RTSHelp.displayHelp(RTSHelp.DTA001A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.DTA001B);
				}
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
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setBounds(24, 132, 252, 49);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.setMaximumSize(new Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
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
	 * Return the FrmEntryPreferencesDTA001ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmEntryPreferencesDTA001ContentPane()
	{
		if (ivjFrmEntryPreferencesDTA001ContentPane == null)
		{
			try
			{
				ivjFrmEntryPreferencesDTA001ContentPane = new JPanel();
				ivjFrmEntryPreferencesDTA001ContentPane.setName(
					"ivjFrmEntryPreferencesDTA001ContentPane");
				ivjFrmEntryPreferencesDTA001ContentPane.setLayout(null);
				ivjFrmEntryPreferencesDTA001ContentPane.setMinimumSize(
					new Dimension(900, 199));
				ivjFrmEntryPreferencesDTA001ContentPane.setMaximumSize(
					new Dimension(900, 199));
				ivjFrmEntryPreferencesDTA001ContentPane.add(
					getJPanel1(),
					null);
				ivjFrmEntryPreferencesDTA001ContentPane.add(
					getButtonPanel1(),
					null);
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
		return ivjFrmEntryPreferencesDTA001ContentPane;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECT_CHOICE));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(new Dimension(223, 97));
				ivjJPanel1.setMaximumSize(new Dimension(223, 97));
				ivjJPanel1.add(getradioDealerExternalMedia(), null);
				ivjJPanel1.add(getradioKeyboard(), null);
				ivjJPanel1.setBounds(27, 18, 243, 98);
				Border laBorder =
					new TitledBorder(new EtchedBorder(), SELECT_CHOICE);
				getJPanel1().setBorder(laBorder);
				// Button Group to manage tab, cursor movement keys 
				RTSButtonGroup laBG = new RTSButtonGroup();
				laBG.add(getradioDealerExternalMedia());
				laBG.add(getradioKeyboard());
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
		return ivjJPanel1;
	}

	/**
	 * Return the ivjradioDealerSuppliedMedia property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDealerExternalMedia()
	{
		if (ivjradioDealerSuppliedMedia == null)
		{
			try
			{
				ivjradioDealerSuppliedMedia = new JRadioButton();
				ivjradioDealerSuppliedMedia.setBounds(15, 28, 208, 22);
				ivjradioDealerSuppliedMedia.setName(
					"ivjradioDealerSuppliedMedia");
				ivjradioDealerSuppliedMedia.setMnemonic(KeyEvent.VK_D);
				ivjradioDealerSuppliedMedia.setText(EXT_MEDIA);
				ivjradioDealerSuppliedMedia.setMaximumSize(
					new Dimension(112, 22));
				ivjradioDealerSuppliedMedia.setMinimumSize(
					new Dimension(112, 22));
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
		return ivjradioDealerSuppliedMedia;
	}

	/**
	 * Return the ivjradioKeyboard property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioKeyboard()
	{
		if (ivjradioKeyboard == null)
		{
			try
			{
				ivjradioKeyboard = new JRadioButton();
				ivjradioKeyboard.setBounds(15, 56, 171, 22);
				ivjradioKeyboard.setName("ivjradioKeyboard");
				ivjradioKeyboard.setMnemonic(KeyEvent.VK_K);
				ivjradioKeyboard.setText(KEYBOARD);
				ivjradioKeyboard.setMaximumSize(new Dimension(175, 22));
				ivjradioKeyboard.setMinimumSize(new Dimension(175, 22));
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
		return ivjradioKeyboard;
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
	 * 
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName(ScreenConstant.DTA001_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(310, 206);
			setModal(true);
			setTitle(ScreenConstant.DTA001_FRAME_TITLE);
			setContentPane(getFrmEntryPreferencesDTA001ContentPane());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());
		getradioDealerExternalMedia().setSelected(true);
		getradioDealerExternalMedia().requestFocus();
		// user code end
	}

	/**
	 * main entrypoint- starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmEntryPreferencesDTA001 laFrmEntryPreferencesDTA001;
			laFrmEntryPreferencesDTA001 =
				new FrmEntryPreferencesDTA001();
			laFrmEntryPreferencesDTA001.setModal(true);
			laFrmEntryPreferencesDTA001
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWA)
				{
					System.exit(0);
				};
			});
			laFrmEntryPreferencesDTA001.show();
			Insets laInsets = laFrmEntryPreferencesDTA001.getInsets();
			laFrmEntryPreferencesDTA001.setSize(
				laFrmEntryPreferencesDTA001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmEntryPreferencesDTA001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmEntryPreferencesDTA001.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeEx.printStackTrace(System.out);
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
		// empty block of code
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"