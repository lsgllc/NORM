package com.txdot.isd.rts.client.inquiry.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InquiryConstants;

/*
 * FrmInquiryOwnerAddressessINQ006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * MAbs			05/14/2002	CQU100003898
 * B Hargrove	01/08/2004  Added field for Vehicle Unit Number.
 *                          modified FrmMultipleRecordsINQ006 and 
 *							setData().
 *                          defect 6495. Rel 5.1.5 fix 2.
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * B Arredondo	03/25/2004	Note from Robin: defect 6981 completed by
 *							Becky for 5.1.6 but did not get merged into
 *							5.2.0. See comments in defect 7244 below.
 *							Adding this info for tracking purposes.
 * R Taylor		06/29/2004	Increased Renewal Recipient data field to
 *							display entire text in VC.
 *							defect 7244 Ver 5.2.0
 * S Johnston	03/24/2005	Rename FrmMultipleRecordsINQ006 to 
 * 							FrmInquiryOwnerAddressessINQ006
 * 							to accurately reflect the name of the screen
 * 							that is displayed.
 * 							Modify constructors, initialize(), main()
 * 							defect 7952 Ver 5.2.3
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7887 Ver 5.2.3
 * S Johnston	07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify actionPerformed, handleException
 * 							defect 7887 Ver 5.2.3
 * K Harrell	01/24/2006	Add logic for "ESC" processing.
 * 							add implements KeyListener
 * 							add keyReleased() 
 * 							defect 8486 Ver 5.2.3    
 * J Rue		04/05/2007	Add button panel.
 * 							Add Enter and Cancel functions
 * 							add getButtonPanel()
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	06/27/2007	Enable Help; Present msg "... not available
 * 							at this time. 
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	06/29/2009	Implement new OwnerData, new NameAddressData,
 * 							AddressData methods.  
 * 							keyPressed() no	longer required 
 * 								 w/ ButtonPanel.(9086 4/2007)
 * 							Static labels not named appropriately. 
 * 							(Sort Members does not work.) 
 * 							add ivjstclblOwnrId, ivjstclblRnwRcpInfo,
 * 							 ivjstclblUnitNo, ivjstclblVehInfo, 
 * 							 ivjtxtAOwnerInfo, ivjtxtARnwlRcptInfo(),
 * 							 ivjtxtAVehLocInfo, get methods
 * 							delete ivjlblOwnrID, ivjlblRnwRcpInfo,
 * 							 ivjstclblUnitNo, ivjstclblVehInfo,
 *							 ivjtxtOwnerInfo, ivjtxtRnwlRcptInfo(),
 * 							 ivjtxtVehLocInfo, get methods
 * 							delete KeyListener 
 * 							delete keyPressed() 
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	08/10/2009	Move presentation of Renewal Name outside 
 * 							conditional for Renewal Address 
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	02/16/2010	Add Recipient E-Mail Information
 * 							add ivjstcLblEMail(), ivjlblRecpntEMail, 
 * 							  get methods
 * 							modify setData()
 * 							defect 10372 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/** 
 * Class for screen INQ006. Displays Owner Address Information
 *
 * @version POS_640 		02/16/2010
 * @author  Ashish Mahajan 
 * <br>Creation Date:		09/05/2001 13:30:59
 */
public class FrmInquiryOwnerAddressessINQ006
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjFrmInquiryOwnerAddressessINQ006ContentPane = null;
	private JLabel ivjlblOwnrId = null;
	private JLabel ivjlblVehUnitNo = null;
	private JLabel ivjstclblOwnrId = null;
	private JLabel ivjstclblRnwRcpInfo = null;
	private JLabel ivjstclblUnitNo = null;
	private JLabel ivjstclblVehInfo = null;
	private JPanel ivjJPanel1 = null;
	private JTextArea ivjtxtAVehLocInfo = null;
	private JTextArea ivjtxtARnwlRcptInfo = null;
	private JTextArea ivjtxtAOwnerInfo = null;
	private ButtonPanel ivjButtonPanel = null;

	// defect 10372 
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjlblRecpntEMail = null;
	// end defect 10372

	/**
	 * FrmInquiryOwnerAddressessINQ006 constructor.
	 */
	public FrmInquiryOwnerAddressessINQ006()
	{
		super();
		initialize();
	}

	/**
	 * FrmInquiryOwnerAddressessINQ006 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInquiryOwnerAddressessINQ006(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInquiryOwnerAddressessINQ006 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInquiryOwnerAddressessINQ006(JFrame aaParent)
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
			// ENTER 
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			// HELP 
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
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
	 * Return the ivjButtonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (ivjButtonPanel == null)
		{
			try
			{
				ivjButtonPanel = new ButtonPanel();
				ivjButtonPanel.setName("ivjButtonPanel");
				ivjButtonPanel.setBounds(97, 499, 216, 36);
				// user code begin {1}
				ivjButtonPanel.addActionListener(this);
				ivjButtonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the FrmInquiryOwnerAddressessINQ006ContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmInquiryOwnerAddressessINQ006ContentPane()
	{
		if (ivjFrmInquiryOwnerAddressessINQ006ContentPane == null)
		{
			try
			{
				ivjFrmInquiryOwnerAddressessINQ006ContentPane =
					new JPanel();
				ivjFrmInquiryOwnerAddressessINQ006ContentPane.setName(
					"FrmInquiryOwnerAddressessINQ006");
				ivjFrmInquiryOwnerAddressessINQ006ContentPane
					.setLayout(
					null);
				getFrmInquiryOwnerAddressessINQ006ContentPane().add(
					getButtonPanel(),
					getButtonPanel().getName());
				getFrmInquiryOwnerAddressessINQ006ContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjFrmInquiryOwnerAddressessINQ006ContentPane;
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
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(16, 19, 404, 466);
				getJPanel1().add(
					getstclblOwnrId(),
					getstclblOwnrId().getName());
				getJPanel1().add(
					getlblOwnrId(),
					getlblOwnrId().getName());
				getJPanel1().add(
					getstclblRnwRcpInfo(),
					getstclblRnwRcpInfo().getName());
				getJPanel1().add(
					getstclblVehInfo(),
					getstclblVehInfo().getName());
				getJPanel1().add(
					gettxtAVehLocInfo(),
					gettxtAVehLocInfo().getName());
				getJPanel1().add(
					gettxtARnwlRcptInfo(),
					gettxtARnwlRcptInfo().getName());
				getJPanel1().add(
					gettxtAOwnerInfo(),
					gettxtAOwnerInfo().getName());
				getJPanel1().add(
					getstclblUnitNo(),
					getstclblUnitNo().getName());
				getJPanel1().add(
					getlblVehUnitNo(),
					getlblVehUnitNo().getName());
				ivjJPanel1.add(getstcLblEMail(), null);
				ivjJPanel1.add(getlblRecpntEMail(), null);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return ivjstclblOwnrId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblOwnrId()
	{
		if (ivjstclblOwnrId == null)
		{
			try
			{
				ivjstclblOwnrId = new JLabel();
				ivjstclblOwnrId.setSize(60, 20);
				ivjstclblOwnrId.setName("ivjstclblOwnrId");
				ivjstclblOwnrId.setText(InquiryConstants.TXT_OWNER_ID);
				ivjstclblOwnrId.setLocation(37, 9);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstclblOwnrId;
	}

	/**
	 * Return the ivjlblOwnrId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOwnrId()
	{
		if (ivjlblOwnrId == null)
		{
			try
			{
				ivjlblOwnrId = new JLabel();
				ivjlblOwnrId.setName("ivjlblOwnrId");
				ivjlblOwnrId.setText("");
				ivjlblOwnrId.setBounds(110, 9, 119, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblOwnrId;
	}

	/**
	 * Return ivjstclblRnwRcpInfo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblRnwRcpInfo()
	{
		if (ivjstclblRnwRcpInfo == null)
		{
			try
			{
				ivjstclblRnwRcpInfo = new JLabel();
				ivjstclblRnwRcpInfo.setName("ivjlblRnwRcpInfo");
				ivjstclblRnwRcpInfo.setText(
					InquiryConstants.TXT_RENEWAL_RECIPIENT_INFO);
				ivjstclblRnwRcpInfo.setBounds(37, 150, 228, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstclblRnwRcpInfo;
	}

	/**
	 * This method initializes ivjstcLblEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			ivjstcLblEMail = new JLabel();
			ivjstcLblEMail.setSize(244, 20);
			ivjstcLblEMail.setText(CommonConstant.TXT_EMAIL);
			ivjstcLblEMail.setLocation(37, 259);
		}
		return ivjstcLblEMail;
	}

	/**
	 * Return ivjstclblUnitNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblUnitNo()
	{
		if (ivjstclblUnitNo == null)
		{
			try
			{
				ivjstclblUnitNo = new JLabel();
				ivjstclblUnitNo.setName("ivjstclblUnitNo");
				ivjstclblUnitNo.setText(InquiryConstants.TXT_UNIT_NO);
				ivjstclblUnitNo.setBounds(37, 434, 48, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstclblUnitNo;
	}

	/**
	 * Return ivjstclblVehInfo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblVehInfo()
	{
		if (ivjstclblVehInfo == null)
		{
			try
			{
				ivjstclblVehInfo = new JLabel();
				ivjstclblVehInfo.setSize(234, 20);
				ivjstclblVehInfo.setName("ivjstclblVehInfo");
				ivjstclblVehInfo.setText(
					InquiryConstants.TXT_VEHICLE_LOC_INFO);
				ivjstclblVehInfo.setLocation(37, 315);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstclblVehInfo;
	}

	/**
	 * Return ivjlblVehUnitNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehUnitNo()
	{
		if (ivjlblVehUnitNo == null)
		{
			try
			{
				ivjlblVehUnitNo = new JLabel();
				ivjlblVehUnitNo.setName("ivjlblVehUnitNo");
				ivjlblVehUnitNo.setText("");
				ivjlblVehUnitNo.setBounds(101, 434, 191, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblVehUnitNo;
	}

	/**
	 * Return ivjtxtAOwnerInfo property value.
	 * Arial is the default font on all frames.  The font consturctor is
	 * used here in order to make the text bold.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAOwnerInfo()
	{
		if (ivjtxtAOwnerInfo == null)
		{
			try
			{
				ivjtxtAOwnerInfo = new JTextArea();
				ivjtxtAOwnerInfo.setName("ivjtxtAOwnerInfo");
				ivjtxtAOwnerInfo.setBackground(
					new Color(204, 204, 204));
				ivjtxtAOwnerInfo.setDisabledTextColor(
					new Color(0, 0, 0));
				ivjtxtAOwnerInfo.setFont(
					new Font("Arial", Font.BOLD, 12));
				ivjtxtAOwnerInfo.setBounds(37, 34, 256, 106);
				ivjtxtAOwnerInfo.setEditable(false);
				ivjtxtAOwnerInfo.setEnabled(false);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAOwnerInfo;
	}

	/**
	 * This method initializes ivjstcLblRecpntEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRecpntEMail()
	{
		if (ivjlblRecpntEMail == null)
		{
			ivjlblRecpntEMail = new JLabel();
			ivjlblRecpntEMail.setSize(357, 20);
			ivjlblRecpntEMail.setText("");
			ivjlblRecpntEMail.setLocation(37, 282);
		}
		return ivjlblRecpntEMail;
	}

	/**
	 * Return the ivjtxtARnwlRcptInfo property value.
	 * Arial is the default font on all frames.  The font consturctor is
	 * used here in order to make the text bold.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtARnwlRcptInfo()
	{
		if (ivjtxtARnwlRcptInfo == null)
		{
			try
			{
				ivjtxtARnwlRcptInfo = new JTextArea();
				ivjtxtARnwlRcptInfo.setName("ivjtxtARnwlRcptInfo");
				ivjtxtARnwlRcptInfo.setBackground(
					new Color(204, 204, 204));
				ivjtxtARnwlRcptInfo.setDisabledTextColor(
					new Color(0, 0, 0));
				ivjtxtARnwlRcptInfo.setFont(
					new Font("Arial", Font.BOLD, 12));
				ivjtxtARnwlRcptInfo.setBounds(37, 174, 256, 76);
				ivjtxtARnwlRcptInfo.setEditable(false);
				ivjtxtARnwlRcptInfo.setEnabled(false);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtARnwlRcptInfo;
	}

	/**
	 * Return the ivjtxtAVehLocInfo property value.
	 * Arial is the default font on all frames.  The font consturctor is
	 * used here in order to make the text bold.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAVehLocInfo()
	{
		if (ivjtxtAVehLocInfo == null)
		{
			try
			{
				ivjtxtAVehLocInfo = new JTextArea();
				ivjtxtAVehLocInfo.setName("ivjtxtAVehLocInfo");
				ivjtxtAVehLocInfo.setBackground(
					new Color(204, 204, 204));
				ivjtxtAVehLocInfo.setDisabledTextColor(
					new Color(0, 0, 0));
				ivjtxtAVehLocInfo.setFont(
					new Font("Arial", Font.BOLD, 12));
				ivjtxtAVehLocInfo.setBounds(37, 338, 256, 76);
				ivjtxtAVehLocInfo.setEnabled(false);
				ivjtxtAVehLocInfo.setEditable(false);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAVehLocInfo;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aaEx Throwable
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
	private void initialize()
	{
		try
		{
			setName("FrmInquiryOwnerAddressessINQ006");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(436, 572);
			setTitle(InquiryConstants.TITLE_FRM_INQ006);
			setContentPane(
				getFrmInquiryOwnerAddressessINQ006ContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
	}

	/**
	 * main entrypoint starts this part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInquiryOwnerAddressessINQ006 laFrmInquiryOwnerAddressessINQ006 =
				new FrmInquiryOwnerAddressessINQ006();
			laFrmInquiryOwnerAddressessINQ006.setModal(true);
			laFrmInquiryOwnerAddressessINQ006
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInquiryOwnerAddressessINQ006.show();
			Insets laInsets =
				laFrmInquiryOwnerAddressessINQ006.getInsets();
			laFrmInquiryOwnerAddressessINQ006.setSize(
				laFrmInquiryOwnerAddressessINQ006.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmInquiryOwnerAddressessINQ006.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmInquiryOwnerAddressessINQ006.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * setData
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
			VehicleInquiryData laVehInqData =
				(VehicleInquiryData) aaDataObject;
			OwnerData laOwnerData =
				laVehInqData.getMfVehicleData().getOwnerData();
			TitleData laTTLData =
				laVehInqData.getMfVehicleData().getTitleData();
			RegistrationData laRegData =
				laVehInqData.getMfVehicleData().getRegData();

			if (laOwnerData != null)
			{
				getlblOwnrId().setText(laOwnerData.getOwnrId());

				gettxtAOwnerInfo().setText(
					laOwnerData
						.getNameAddressStringBuffer()
						.toString());
			}
			if (laTTLData != null)
			{
				AddressData laAddrData = laTTLData.getTtlVehAddr();

				if (laAddrData != null)
				{
					gettxtAVehLocInfo().setText(
						laAddrData.getAddressStringBuffer().toString());
				}

				// defect 6495	
				if (laTTLData.getVehUnitNo() != null
					&& laTTLData.getVehUnitNo().length() > 0)
				{
					getlblVehUnitNo().setText(laTTLData.getVehUnitNo());
				}
				else
				{
					getlblVehUnitNo().setText("");
				}
				// end defect 6495	
			}
			if (laRegData != null)
			{
				// defect 10112
				// This was previously within the laRnwlAddr!= null
				StringBuffer laStrRnwlBuf = new StringBuffer("");

				if (laRegData.getRecpntName() != null)
				{
					laStrRnwlBuf.append(
						laRegData.getRecpntName()
							+ CommonConstant.SYSTEM_LINE_SEPARATOR);
				}

				AddressData laRnwlAddr = laRegData.getRenwlMailAddr();
				if (laRnwlAddr != null)
				{
					laStrRnwlBuf.append(
						laRnwlAddr.getAddressStringBuffer());

					gettxtARnwlRcptInfo().setText(
						laStrRnwlBuf.toString());
				}

				// defect 10372 
				if (laRegData.getRecpntEMail() != null)
				{
					String lsEMail = laRegData.getRecpntEMail();
					getlblRecpntEMail().setText(lsEMail);
				}
				// end defect 10372 
			}
		}
		catch (Exception aeEx)
		{
			handleException(aeEx);
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"