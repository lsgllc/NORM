package com.txdot.isd.rts.client.inquiry.ui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InquiryConstants;

/*
 * FrmVehicleInqAddlInfoINQ003.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * J Kwik		04/18/2002	Global change for startWorking() and
 * 							doneWorking()
 * N Ting		04/30/2002	change setData to change for null for 
 * 							regData.getCustActlRegFee()
 * 							defect 3713 
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7887 Ver 5.2.3 
 * S Johnston	07/06/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify actionPerformed, handleException
 * 							defect 7887 Ver 5.2.3
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	01/24/2006	Adjusted size of Enter Key; Modifications
 * 							for ESC processing.
 * 							add implements KeyListener
 * 							add keyReleased()
 * 							defect 7887
 * T Pederson	10/13/2006	Increase size of lblSlsPrice, lblTrdIn
 * 							and lblSlsTaxPaid with visual editor to 
 * 							accomodate larger values. 
 * 							delete getBuilderData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/08/2006	Do not present "0/0" for Previous Expiration
 * 							Date
 * 							modify setData()
 * 							defect 8900 Ver Exempts 
 * J Rue		04/06/2007	Add Enter/Cancel/Help button panel
 * 							add getButtonPanel()
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	06/27/2007	Enable Help; Present msg "... not available
 * 							at this time. 
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	05/20/2009	Add AuditTrailTransId.  Additional cleanup.  
 * 							add ivjstcLblAuditTrailTransId, 
 * 							  ivjlblAuditTrailTransId, get methods 
 * 							modify setData(), getJPanel3()  
 * 							defect 10058 Ver Defect POS F
 * ---------------------------------------------------------------------
 */
/** 
 * Class for screen INQ003. Displays Additional Info 
 *
 * @version Defect POS F	05/20/2009 
 * @author  Ashish Mahajan 
 * <br>Creation Date:		09/05/2001 13:30:59
 */
public class FrmVehicleInqAddlInfoINQ003
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private ButtonPanel ivjButtonPanel = null;
	private JPanel ivjFrmVehicleInqAddlInfoINQ003ContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JLabel ivjlblPrevExpMoYr = null;
	private JLabel ivjlblPrevOwnrName = null;
	private JLabel ivjlblPrevOwnrNameCity = null;
	private JLabel ivjlblPrevOwnrNameSt = null;
	private JLabel ivjlblPrevPltNo = null;
	private JLabel ivjlblRegEffDt = null;
	private JLabel ivjlblRegFee = null;
	private JLabel ivjlblRegIssDt = null;
	private JLabel ivjlblSlsPrice = null;
	private JLabel ivjlblSlsTaxPaid = null;
	private JLabel ivjlblTireType = null;
	private JLabel ivjlblTrdIn = null;
	private JLabel ivjstcLblPrevExpMoYr = null;
	private JLabel ivjstcLblPrevOwnrNameCity = null;
	private JLabel ivjstcLblPrevPltNo = null;
	private JLabel ivjstcLblRegEffDt = null;
	private JLabel ivjstcLblRegFee = null;
	private JLabel ivjstcLblSlsPrice = null;
	private JLabel ivjstcLblSlsTaxPaid = null;
	private JLabel ivjstcLblTireType = null;
	private JLabel ivjstcLblTrdIn = null;
	private JLabel ivjstcLlblRegIssDt = null;

	// defect 10058
	private JLabel ivjlblAuditTrailTransId = null;
	private JLabel ivjstcLblAuditTrailTransId = null;
	// end defect 10058 

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
			FrmVehicleInqAddlInfoINQ003 laFrmVehicleInqAddlInfo;
			laFrmVehicleInqAddlInfo = new FrmVehicleInqAddlInfoINQ003();
			laFrmVehicleInqAddlInfo.setModal(true);
			laFrmVehicleInqAddlInfo
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmVehicleInqAddlInfo.show();
			Insets laInsets = laFrmVehicleInqAddlInfo.getInsets();
			laFrmVehicleInqAddlInfo.setSize(
				laFrmVehicleInqAddlInfo.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVehicleInqAddlInfo.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmVehicleInqAddlInfo.setVisibleRTS(true);
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
	 * FrmVehicleInqAddlInfoINQ003 constructor.
	 */
	public FrmVehicleInqAddlInfoINQ003()
	{
		super();
		initialize();
	}
	/**
	 * FrmVehicleInqAddlInfoINQ003 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmVehicleInqAddlInfoINQ003(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmVehicleInqAddlInfoINQ003 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmVehicleInqAddlInfoINQ003(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs. Pass the call to controller
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
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
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
	 * Return the ButtonPanel1 property value.
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
				ivjButtonPanel.setName("ButtonPanel1");
				ivjButtonPanel.setBounds(224, 312, 216, 36);

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
	 * Return the FrmVehicleInqAddlInfoINQ003ContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmVehicleInqAddlInfoINQ003ContentPane()
	{
		if (ivjFrmVehicleInqAddlInfoINQ003ContentPane == null)
		{
			try
			{
				ivjFrmVehicleInqAddlInfoINQ003ContentPane =
					new JPanel();
				ivjFrmVehicleInqAddlInfoINQ003ContentPane.setName(
					"FrmVehicleInqAddlInfoINQ003ContentPane1");
				ivjFrmVehicleInqAddlInfoINQ003ContentPane.setLayout(
					null);
				ivjFrmVehicleInqAddlInfoINQ003ContentPane
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmVehicleInqAddlInfoINQ003ContentPane
					.setMinimumSize(
					new Dimension(588, 386));
				getFrmVehicleInqAddlInfoINQ003ContentPane().add(
					getButtonPanel(),
					getButtonPanel().getName());
				getFrmVehicleInqAddlInfoINQ003ContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmVehicleInqAddlInfoINQ003ContentPane().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmVehicleInqAddlInfoINQ003ContentPane().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmVehicleInqAddlInfoINQ003ContentPane().add(
					getJPanel4(),
					getJPanel4().getName());
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjFrmVehicleInqAddlInfoINQ003ContentPane;
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_PREVIOUS_OWNER_INFO));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(20, 32, 300, 108);
				getJPanel1().add(
					getstcLblPrevOwnrNameCity(),
					getstcLblPrevOwnrNameCity().getName());
				getJPanel1().add(
					getlblPrevOwnrName(),
					getlblPrevOwnrName().getName());
				getJPanel1().add(
					getlblPrevOwnrNameCity(),
					getlblPrevOwnrNameCity().getName());
				getJPanel1().add(
					getlblPrevOwnrNameSt(),
					getlblPrevOwnrNameSt().getName());
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_REGISTRATION_INFO));
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setBounds(336, 32, 262, 108);
				getJPanel2().add(
					getstcLblRegFee(),
					getstcLblRegFee().getName());
				getJPanel2().add(
					getstcLblRegEffDt(),
					getstcLblRegEffDt().getName());
				getJPanel2().add(
					getstcLblRegIssDt(),
					getstcLblRegIssDt().getName());
				getJPanel2().add(
					getlblRegFee(),
					getlblRegFee().getName());
				getJPanel2().add(
					getlblRegEffDt(),
					getlblRegEffDt().getName());
				getJPanel2().add(
					getlblRegIssDt(),
					getlblRegIssDt().getName());
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_OTHER_INFO));
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(20, 161, 300, 131);
				getJPanel3().add(
					getstcLblPrevPltNo(),
					getstcLblPrevPltNo().getName());
				getJPanel3().add(
					getstcLblPrevExpMoYr(),
					getstcLblPrevExpMoYr().getName());
				getJPanel3().add(
					getstcLblTireType(),
					getstcLblTireType().getName());
				getJPanel3().add(
					getlblPrevPltNo(),
					getlblPrevPltNo().getName());
				getJPanel3().add(
					getlblPrevExpMoYr(),
					getlblPrevExpMoYr().getName());
				getJPanel3().add(
					getlblTireType(),
					getlblTireType().getName());
				// defect 10058
				getJPanel3().add(
					getstcLblAuditTrailTransId(),
					getstcLblAuditTrailTransId().getName());
				getJPanel3().add(
					getlblAuditTrailTransId(),
					getlblAuditTrailTransId().getName());
				// end defect 10058 
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel4 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new JPanel();
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_SALES_TAX_INFO));
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setBounds(336, 161, 262, 131);
				getJPanel4().add(
					getstcLblSlsPrice(),
					getstcLblSlsPrice().getName());
				getJPanel4().add(
					getstcLblTrdIn(),
					getstcLblTrdIn().getName());
				getJPanel4().add(
					getstcLblSlsTaxPaid(),
					getstcLblSlsTaxPaid().getName());
				getJPanel4().add(
					getlblSlsPrice(),
					getlblSlsPrice().getName());
				getJPanel4().add(
					getlblTrdIn(),
					getlblTrdIn().getName());
				getJPanel4().add(
					getlblSlsTaxPaid(),
					getlblSlsTaxPaid().getName());
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel4;
	}

	/**
	 * This method initializes ivjlblAuditTrailTransId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAuditTrailTransId()
	{
		if (ivjlblAuditTrailTransId == null)
		{
			ivjlblAuditTrailTransId = new JLabel();
			ivjlblAuditTrailTransId.setBounds(165, 98, 130, 14);
			ivjlblAuditTrailTransId.setName("lblAuditTrailTransId");
		}
		return ivjlblAuditTrailTransId;
	}

	/**
	 * Return the lblPrevExpMoYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevExpMoYr()
	{
		if (ivjlblPrevExpMoYr == null)
		{
			try
			{
				ivjlblPrevExpMoYr = new JLabel();
				ivjlblPrevExpMoYr.setName("lblPrevExpMoYr");
				ivjlblPrevExpMoYr.setText("");
				ivjlblPrevExpMoYr.setBounds(165, 50, 85, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPrevExpMoYr;
	}

	/**
	 * Return the lblPrevOwnrName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevOwnrName()
	{
		if (ivjlblPrevOwnrName == null)
		{
			try
			{
				ivjlblPrevOwnrName = new JLabel();
				ivjlblPrevOwnrName.setName("lblPrevOwnrName");
				ivjlblPrevOwnrName.setText("");
				ivjlblPrevOwnrName.setBounds(26, 50, 266, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPrevOwnrName;
	}

	/**
	 * Return the lblPrevOwnrNameCity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevOwnrNameCity()
	{
		if (ivjlblPrevOwnrNameCity == null)
		{
			try
			{
				ivjlblPrevOwnrNameCity = new JLabel();
				ivjlblPrevOwnrNameCity.setName("lblPrevOwnrNameCity");
				ivjlblPrevOwnrNameCity.setText("");
				ivjlblPrevOwnrNameCity.setBounds(26, 74, 135, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPrevOwnrNameCity;
	}

	/**
	 * Return the lblPrevOwnrNameSt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevOwnrNameSt()
	{
		if (ivjlblPrevOwnrNameSt == null)
		{
			try
			{
				ivjlblPrevOwnrNameSt = new JLabel();
				ivjlblPrevOwnrNameSt.setName("lblPrevOwnrNameSt");
				ivjlblPrevOwnrNameSt.setText("");
				ivjlblPrevOwnrNameSt.setBounds(196, 74, 84, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPrevOwnrNameSt;
	}

	/**
	 * Return the lblPrevPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevPltNo()
	{
		if (ivjlblPrevPltNo == null)
		{
			try
			{
				ivjlblPrevPltNo = new JLabel();
				ivjlblPrevPltNo.setSize(85, 14);
				ivjlblPrevPltNo.setName("lblPrevPltNo");
				ivjlblPrevPltNo.setText("");
				ivjlblPrevPltNo.setLocation(165, 26);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPrevPltNo;
	}

	/**
	 * Return the lblRegEffDt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegEffDt()
	{
		if (ivjlblRegEffDt == null)
		{
			try
			{
				ivjlblRegEffDt = new JLabel();
				ivjlblRegEffDt.setName("lblRegEffDt");
				ivjlblRegEffDt.setText("");
				ivjlblRegEffDt.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblRegEffDt.setBounds(148, 50, 87, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblRegEffDt;
	}

	/**
	 * Return the lblRegFee property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegFee()
	{
		if (ivjlblRegFee == null)
		{
			try
			{
				ivjlblRegFee = new JLabel();
				ivjlblRegFee.setSize(87, 14);
				ivjlblRegFee.setName("lblRegFee");
				ivjlblRegFee.setText("");
				ivjlblRegFee.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblRegFee.setLocation(148, 26);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblRegFee;
	}

	/**
	 * Return the lblRegIssDt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegIssDt()
	{
		if (ivjlblRegIssDt == null)
		{
			try
			{
				ivjlblRegIssDt = new JLabel();
				ivjlblRegIssDt.setName("lblRegIssDt");
				ivjlblRegIssDt.setText("");
				ivjlblRegIssDt.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblRegIssDt.setBounds(148, 74, 87, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblRegIssDt;
	}

	/**
	 * Return the lblSlsTaxPrice property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblSlsPrice()
	{
		if (ivjlblSlsPrice == null)
		{
			try
			{
				ivjlblSlsPrice = new JLabel();
				ivjlblSlsPrice.setName("lblSlsPrice");
				ivjlblSlsPrice.setText("");
				ivjlblSlsPrice.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblSlsPrice.setBounds(142, 26, 93, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblSlsPrice;
	}

	/**
	 * Return the lblSlsTaxPaid property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblSlsTaxPaid()
	{
		if (ivjlblSlsTaxPaid == null)
		{
			try
			{
				ivjlblSlsTaxPaid = new JLabel();
				ivjlblSlsTaxPaid.setSize(93, 14);
				ivjlblSlsTaxPaid.setName("lblSlsTaxPaid");
				ivjlblSlsTaxPaid.setText("");
				ivjlblSlsTaxPaid.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblSlsTaxPaid.setLocation(142, 74);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblSlsTaxPaid;
	}

	/**
	 * Return the lblTireType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTireType()
	{
		if (ivjlblTireType == null)
		{
			try
			{
				ivjlblTireType = new JLabel();
				ivjlblTireType.setName("lblTireType");
				ivjlblTireType.setText("");
				ivjlblTireType.setBounds(165, 74, 85, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblTireType;
	}

	/**
	 * Return the lblTrdIn property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTrdIn()
	{
		if (ivjlblTrdIn == null)
		{
			try
			{
				ivjlblTrdIn = new JLabel();
				ivjlblTrdIn.setName("lblTrdIn");
				ivjlblTrdIn.setText("");
				ivjlblTrdIn.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjlblTrdIn.setBounds(142, 50, 93, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblTrdIn;
	}

	/**
	 * This method initializes ivjstcLblAuditTrailTransId
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAuditTrailTransId()
	{
		if (ivjstcLblAuditTrailTransId == null)
		{
			ivjstcLblAuditTrailTransId = new JLabel();
			ivjstcLblAuditTrailTransId.setName(
				"stcLblAuditTrailTransId");
			ivjstcLblAuditTrailTransId.setSize(100, 14);
			ivjstcLblAuditTrailTransId.setText(
				InquiryConstants.TXT_AUDIT_TRAIL_TRANS_ID);
			ivjstcLblAuditTrailTransId.setLocation(49, 98);
		}
		return ivjstcLblAuditTrailTransId;
	}

	/**
	 * Return the stcLblPrevExpMoYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrevExpMoYr()
	{
		if (ivjstcLblPrevExpMoYr == null)
		{
			try
			{
				ivjstcLblPrevExpMoYr = new JLabel();
				ivjstcLblPrevExpMoYr.setName("stcLblPrevExpMoYr");
				ivjstcLblPrevExpMoYr.setText(
					InquiryConstants.TXT_PREV_EXP_MO);
				ivjstcLblPrevExpMoYr.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPrevExpMoYr.setBounds(11, 50, 138, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPrevExpMoYr;
	}

	/**
	 * Return the stcLblPrevOwnrNameCity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrevOwnrNameCity()
	{
		if (ivjstcLblPrevOwnrNameCity == null)
		{
			try
			{
				ivjstcLblPrevOwnrNameCity = new JLabel();
				ivjstcLblPrevOwnrNameCity.setName(
					"stcLblPrevOwnrNameCity");
				ivjstcLblPrevOwnrNameCity.setText(
					InquiryConstants.TXT_NAME_CITY);
				ivjstcLblPrevOwnrNameCity.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPrevOwnrNameCity.setBounds(14, 26, 159, 14);
				ivjstcLblPrevOwnrNameCity.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblPrevOwnrNameCity.setHorizontalTextPosition(
					SwingConstants.LEFT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPrevOwnrNameCity;
	}

	/**
	 * Return the stcLblPrevPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrevPltNo()
	{
		if (ivjstcLblPrevPltNo == null)
		{
			try
			{
				ivjstcLblPrevPltNo = new JLabel();
				ivjstcLblPrevPltNo.setName("stcLblPrevPltNo");
				ivjstcLblPrevPltNo.setText(
					InquiryConstants.TXT_PREV_PLT_NO);
				ivjstcLblPrevPltNo.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPrevPltNo.setBounds(11, 26, 138, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPrevPltNo;
	}

	/**
	 * Return the lblEffDt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegEffDt()
	{
		if (ivjstcLblRegEffDt == null)
		{
			try
			{
				ivjstcLblRegEffDt = new JLabel();
				ivjstcLblRegEffDt.setSize(109, 14);
				ivjstcLblRegEffDt.setName("lblEffDt");
				ivjstcLblRegEffDt.setText(
					InquiryConstants.TXT_EFFECTIVE_DATE);
				ivjstcLblRegEffDt.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblRegEffDt.setLocation(19, 50);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblRegEffDt;
	}

	/**
	 * Return the lblFee property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegFee()
	{
		if (ivjstcLblRegFee == null)
		{
			try
			{
				ivjstcLblRegFee = new JLabel();
				ivjstcLblRegFee.setSize(105, 14);
				ivjstcLblRegFee.setName("lblFee");
				ivjstcLblRegFee.setText(InquiryConstants.TXT_FEE);
				ivjstcLblRegFee.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblRegFee.setHorizontalTextPosition(
					SwingConstants.CENTER);
				ivjstcLblRegFee.setLocation(23, 26);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblRegFee;
	}

	/**
	 * Return the lbIssdt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegIssDt()
	{
		if (ivjstcLlblRegIssDt == null)
		{
			try
			{
				ivjstcLlblRegIssDt = new JLabel();
				ivjstcLlblRegIssDt.setSize(102, 14);
				ivjstcLlblRegIssDt.setName("lbIssdt");
				ivjstcLlblRegIssDt.setText(
					InquiryConstants.TXT_ISSUE_DATE);
				ivjstcLlblRegIssDt.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLlblRegIssDt.setLocation(26, 74);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLlblRegIssDt;
	}

	/**
	 * Return the stcLblSlsPrice property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSlsPrice()
	{
		if (ivjstcLblSlsPrice == null)
		{
			try
			{
				ivjstcLblSlsPrice = new JLabel();
				ivjstcLblSlsPrice.setName("stcLblSlsPrice");
				ivjstcLblSlsPrice.setText(
					InquiryConstants.TXT_SALES_PRICE);
				ivjstcLblSlsPrice.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblSlsPrice.setBounds(17, 26, 111, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblSlsPrice;
	}

	/**
	 * Return the stcLblSlsTaxPaid property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSlsTaxPaid()
	{
		if (ivjstcLblSlsTaxPaid == null)
		{
			try
			{
				ivjstcLblSlsTaxPaid = new JLabel();
				ivjstcLblSlsTaxPaid.setName("stcLblSlsTaxPaid");
				ivjstcLblSlsTaxPaid.setText(
					InquiryConstants.TXT_SALES_TAX_PD);
				ivjstcLblSlsTaxPaid.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblSlsTaxPaid.setBounds(8, 74, 120, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblSlsTaxPaid;
	}

	/**
	 * Return the stcLblTireType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTireType()
	{
		if (ivjstcLblTireType == null)
		{
			try
			{
				ivjstcLblTireType = new JLabel();
				ivjstcLblTireType.setName("stcLblTireType");
				ivjstcLblTireType.setText(
					InquiryConstants.TXT_TIRE_TYPE);
				ivjstcLblTireType.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblTireType.setBounds(25, 74, 124, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblTireType;
	}

	/**
	 * Return the stcLblTrdIn property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrdIn()
	{
		if (ivjstcLblTrdIn == null)
		{
			try
			{
				ivjstcLblTrdIn = new JLabel();
				ivjstcLblTrdIn.setName("stcLblTrdIn");
				ivjstcLblTrdIn.setText(InquiryConstants.TXT_TRADE_IN);
				ivjstcLblTrdIn.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblTrdIn.setBounds(42, 50, 86, 14);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblTrdIn;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		/* Uncomment the following lines to print uncaught exceptions to
		 * stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7170
		// create exception and display it for GUI problems
		RTSException leRE =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRE.displayError(this);
		// end defect 7170
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmVehicleInqAddlInfoINQ003");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(627, 382);
			setTitle(InquiryConstants.TITLE_FRM_INQ003);
			setContentPane(getFrmVehicleInqAddlInfoINQ003ContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
	}

	/**
	 *  Process KeyReleasedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getController().processData(
				AbstractViewController.CANCEL,
				getController().getData());
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
			VehicleInquiryData laVehData =
				(VehicleInquiryData) aaDataObject;
			RegistrationData laRegData =
				(RegistrationData) laVehData
					.getMfVehicleData()
					.getRegData();
			TitleData laTtlData =
				(TitleData) laVehData.getMfVehicleData().getTitleData();
			if (laRegData != null)
			{
				int liEffDt = laRegData.getRegEffDt();
				if (liEffDt > 0)
				{
					RTSDate laRtEff =
						new RTSDate(RTSDate.YYYYMMDD, liEffDt);
					getlblRegEffDt().setText("" + laRtEff.toString());
				}
				// defect 8900
				// Do not present "0/O"
				String lsStrPrevExpMo = "";
				if (laRegData.getPrevExpMo() != 0)
				{
					lsStrPrevExpMo = "" + laRegData.getPrevExpMo();
				}
				String lsPrevExpYr = "";
				if (laRegData.getPrevExpYr() != 0)
				{
					lsPrevExpYr =
						Integer.toString(laRegData.getPrevExpYr());
				}
				// end defect 8900
				getlblPrevExpMoYr().setText(
					lsStrPrevExpMo + "/" + lsPrevExpYr);

				int liIssDt = laRegData.getRegIssueDt();
				if (liIssDt > 0)
				{
					RTSDate laRtIss =
						new RTSDate(RTSDate.YYYYMMDD, liIssDt);
					getlblRegIssDt().setText(laRtIss.toString());
				}
				getlblPrevPltNo().setText(
					laRegData.getPrevPltNo() == null
						? ""
						: laRegData.getPrevPltNo());
				getlblTireType().setText(
					"" + laRegData.getTireTypeCd());
				getlblPrevOwnrNameCity().setText(
					laTtlData.getPrevOwnrCity());
				getlblPrevOwnrName().setText(
					laTtlData.getPrevOwnrName());
				getlblPrevOwnrNameSt().setText(
					laTtlData.getPrevOwnrState());

				Dollar laDollar =
					laTtlData.getVehSalesPrice() == null
						? new Dollar("0.00")
						: laTtlData.getVehSalesPrice();
				String laStrSlsPrc = laDollar.printDollar();
				getlblSlsPrice().setText(laStrSlsPrc);

				laDollar =
					laTtlData.getSalesTaxPdAmt() == null
						? new Dollar("0.00")
						: laTtlData.getSalesTaxPdAmt();
				String laStrSlsTax = laDollar.printDollar();
				getlblSlsTaxPaid().setText(laStrSlsTax);

				laDollar =
					laTtlData.getVehTradeinAllownce() == null
						? new Dollar("0.00")
						: laTtlData.getVehTradeinAllownce();
				String lsStrTradeIn = laDollar.printDollar();
				getlblTrdIn().setText(lsStrTradeIn);

				laDollar =
					laRegData.getCustActlRegFee() == null
						? new Dollar("0.00")
						: laRegData.getCustActlRegFee();
				String lsStrDlr = laDollar.printDollar();
				getlblRegFee().setText(lsStrDlr);

				// defect 10058 
				// Display AuditTrailTransId
				if (laVehData.getMfVehicleData().getVehicleData()
					!= null)
				{
					String lsAuditTrailTransId =
						laVehData
							.getMfVehicleData()
							.getVehicleData()
							.getAuditTrailTransId();
					if (!UtilityMethods.isEmpty(lsAuditTrailTransId))
					{
						getlblAuditTrailTransId().setText(
							lsAuditTrailTransId);
					}
				}
				// end defect 10058 
			}
		}
		catch (Exception aeEx)
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Data error",
				"ERROR").displayError(
				this);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="7,1"
