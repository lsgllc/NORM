package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmStolenSRSTTL037.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			Made changes for validations
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/04/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3  
 * K Harrell	11/17/2011	Ensure at least one checkbox is selected
 * 							delete getBuilderData() 
 *   						modify actionPerformed(), getchkSRS(), 
 *   						 getchkStolen() 
 *   						defect 11004 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to capture the stolen and/or safety responsibility 
 * suspension indicators in status change.
 * 
 * @version	6.9.0			11/17/2011
 * @author	Ashish Mahajan
 * <br>Creation Date:		06/26/2001 15:42:38
 */
public class FrmStolenSRSTTL037
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmStolenSRSTTL037ContentPane1 = null;
	private JCheckBox ivjchkSRS = null;
	private JCheckBox ivjchkStolen = null;
	private VehicleInquiryData caVehInqData = null;
	
	// Constant String
	private final static String SRS = "SRS";
	private final static String STOLEN = "Stolen";
	private final static String SELECT_CHANGE = "Select Change:";

	/**
	 * FrmStolenSRSTTL037 constructor
	 */
	public FrmStolenSRSTTL037()
	{
		super();
		initialize();
	}
	/**
	 * FrmStolenSRSTTL037 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmStolenSRSTTL037(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmStolenSRSTTL037 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmStolenSRSTTL037(JFrame aaParent)
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
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);
			
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 11004 
				// Ensure something is selected 
				if (getchkStolen().isSelected() || getchkSRS().isSelected()) 
				{
					//Set data to the data object
					setDataToVehObj();
					getController().processData(
						AbstractViewController.ENTER,
						getController().getData());
				}
				else
				{
					RTSException leRTSEx = new RTSException(); 
					leRTSEx.addException(new RTSException(150), getchkStolen()); 
					leRTSEx.addException(new RTSException(150), getchkSRS());
					leRTSEx.displayError(this); 
				}
				// end defect 11004 
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL037);
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
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjButtonPanel1.setMinimumSize(new Dimension(197, 25));
				ivjButtonPanel1.setBounds(103, 144, 220, 63);
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
	 * Return the chkSRS property value.
	 * 	 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSRS()
	{
		if (ivjchkSRS == null)
		{
			try
			{
				ivjchkSRS = new JCheckBox();
				ivjchkSRS.setName("chkSRS");
				ivjchkSRS.setMnemonic('R');
				ivjchkSRS.setText(SRS);
				ivjchkSRS.setBounds(34, 64, 97, 22);
				// defect 11004 
				ivjchkSRS.addActionListener(this);
				// end defect 11004 
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
		return ivjchkSRS;
	}
	/**
	 * Return the chkStolen property value.	 
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkStolen()
	{
		if (ivjchkStolen == null)
		{
			try
			{
				ivjchkStolen = new JCheckBox();
				ivjchkStolen.setName("chkStolen");
				ivjchkStolen.setMnemonic('S');
				ivjchkStolen.setText(STOLEN);
				ivjchkStolen.setBounds(34, 28, 97, 22);
				// defect 11004
				ivjchkStolen.addActionListener(this);
				// end defect 11004
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
		return ivjchkStolen;
	}
	/**
	 * Return the FrmStolenSRSTTL037ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmStolenSRSTTL037ContentPane1()
	{
		if (ivjFrmStolenSRSTTL037ContentPane1 == null)
		{
			try
			{
				ivjFrmStolenSRSTTL037ContentPane1 = new JPanel();
				ivjFrmStolenSRSTTL037ContentPane1.setName(
					"FrmStolenSRSTTL037ContentPane1");
				ivjFrmStolenSRSTTL037ContentPane1.setLayout(null);
				ivjFrmStolenSRSTTL037ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmStolenSRSTTL037ContentPane1.setMinimumSize(
					new Dimension(872, 266));
				getFrmStolenSRSTTL037ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmStolenSRSTTL037ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmStolenSRSTTL037ContentPane1;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(new EtchedBorder());
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(new Dimension(140, 113));
				ivjJPanel1.setBounds(134, 17, 157, 111);
				getJPanel1().add(
					getchkStolen(),
					getchkStolen().getName());
				getJPanel1().add(getchkSRS(), getchkSRS().getName());
				// user code begin {1}
				Border laBrdr =
					new TitledBorder(
						new EtchedBorder(), SELECT_CHANGE);
				ivjJPanel1.setBorder(laBrdr);
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
			setName(ScreenConstant.TTL037_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(426, 240);
			setTitle(ScreenConstant.TTL037_FRAME_TITLE);
			setContentPane(getFrmStolenSRSTTL037ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Allows for keyboard navigation within the button panel and 
	 * between the check boxes.
	 *
	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		if (aaKE.getSource() instanceof JCheckBox)
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_UP
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				if (getchkStolen().hasFocus())
				{
					getchkSRS().requestFocus();
				}
				else
				{
					getchkStolen().requestFocus();
				}
			}
		}
		// defect 8240
		// arrow keys now handled in ButtonPanel
		//		else if (aaKE.getSource() instanceof RTSButton)
		//		{
		//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
		//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		//			{
		//				if (getButtonPanel1().getBtnEnter().hasFocus())
		//				{
		//					getButtonPanel1().getBtnCancel().requestFocus();
		//				}
		//				else if (getButtonPanel1().getBtnCancel().hasFocus())
		//				{
		//					getButtonPanel1().getBtnHelp().requestFocus();
		//				}
		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
		//				{
		//					getButtonPanel1().getBtnEnter().requestFocus();
		//				}
		//				aaKE.consume();
		//			}
		//			else if (
		//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
		//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		//			{
		//				if (getButtonPanel1().getBtnCancel().hasFocus())
		//				{
		//					getButtonPanel1().getBtnEnter().requestFocus();
		//				}
		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
		//				{
		//					getButtonPanel1().getBtnCancel().requestFocus();
		//				}
		//				else if (getButtonPanel1().getBtnEnter().hasFocus())
		//				{
		//					getButtonPanel1().getBtnHelp().requestFocus();
		//				}
		//				aaKE.consume();
		//			}
		//		}
		// end defect 8240
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
			FrmStolenSRSTTL037 laFrmStolenSRSTTL037;
			laFrmStolenSRSTTL037 = new FrmStolenSRSTTL037();
			laFrmStolenSRSTTL037.setModal(true);
			laFrmStolenSRSTTL037.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laRTSDBox)
				{
					System.exit(0);
				};
			});
			laFrmStolenSRSTTL037.show();
			Insets laInsets = laFrmStolenSRSTTL037.getInsets();
			laFrmStolenSRSTTL037.setSize(
				laFrmStolenSRSTTL037.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmStolenSRSTTL037.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmStolenSRSTTL037.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
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
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();

			if (laVehData.getDpsStlnIndi() == 1)
			{
				getchkStolen().setSelected(true);
			}

			if (laRegData.getDpsSaftySuspnIndi() == 1)
			{
				getchkSRS().setSelected(true);
			}
		}
	}
	/**
	 * setDataToVehObj
	 */
	private void setDataToVehObj()
	{
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();
		VehicleData lavehData =
			caVehInqData.getMfVehicleData().getVehicleData();
		if (lavehData != null)
		{
			if (getchkStolen().isSelected())
			{
				lavehData.setDpsStlnIndi(1);
			}
			else
			{
				lavehData.setDpsStlnIndi(0);
			}
		}
		if (laRegData != null)
		{
			if (getchkSRS().isSelected())
			{
				laRegData.setDpsSaftySuspnIndi(1);
			}
			else
			{
				laRegData.setDpsSaftySuspnIndi(0);
			}
		}
	}
}
