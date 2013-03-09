package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.VehicleInsuranceData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;

/*
 *
 * FrmInsuranceInfoREG104.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown	    07/09/2002	Fixed defect #4338 by resizing the Policy
 * 							number field in VCE.
 * E LyBrand	08/08/2002	CQ4496: Aligned REG104 text labels
 * Jeff S.		02/17/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify actionPerformed(), main()
 *							defect 7889 ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		07/12/2005	Code cleanup for Java 1.4. Added the ECH
 * 							panel instead of separate buttons.
 * 							deprecate getBuilderData(), 
 * 								initConnections()
 * 							defect 7889 ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/30/2005	Remove setting of color 
 * 							defect 7889 Ver 5.2.3  
 * K Harrell	02/11/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates 
 * Min Wang		06/18/2007	modify constants
 * 							defect 8768 Ver Special Plates 
 * ---------------------------------------------------------------------
 */
/**
 * FrmInsuranceInfoREG104 allows the county office to view,
 * registration insurance.
 *
 * @version	Special Plates	06/18/2007
 * @author	George Donoso
 * <br>Creation Date:		10/10/2001 13:01:13
 */
public class FrmInsuranceInfoREG104
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjJlblAgent = null;
	private JLabel ivjJlblEndDate = null;
	private JLabel ivjJlblEndDate2 = null;
	private JLabel ivjJlblInsuranceCo = null;
	private JLabel ivjJlblInsuranceCo2 = null;
	private JLabel ivjJlblPhone = null;
	private JLabel ivjJlblPhone2 = null;
	private JLabel ivjJlblPolicyNo = null;
	private JLabel ivjJlblPolicyNo2 = null;
	private JLabel ivjJlblStartDate = null;
	private JLabel ivjJlblStartDate2 = null;
	private JPanel ivjJPanel1 = null;
	private JLabel ivjJlblAgent2 = null;
	private Object caData = null;
	private ButtonPanel ivjButtonPanel1 = null;

	/**
	 * String Constants
	 */
	private static final String EMPTY_STRING = "";
	private static final String FRM_TITLE = 
		"Vehicle Insurance Information    REG104";
	private static final String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of JDialog";
	private static final String AGENT_LBL = "Agent Name:";
	private static final String END_DATE_LBL = "End Date:";
	private static final String INS_CO_LBL = "Ins Co Name:";
	private static final String INS_INFO_LBL = "Insurance Information:";
	private static final String PHONE_LBL = "Phone No:";
	private static final String POLICY_NO_LBL = "Policy No:";
	private static final String START_DATE_LBL = "Begin Date:";
	private static final String DASH = "-";

	/**
	 * InsuranceInfo default constructor
	 */
	public FrmInsuranceInfoREG104()
	{
		super();
		initialize();
	}
	/**
	 * InsuranceInfo one arg constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmInsuranceInfoREG104(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * InsuranceInfo constructor 
	 * 
	 * @param aaOwner Frame
	 */
	public FrmInsuranceInfoREG104(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInsuranceInfoREG104 aaInsuranceInfo;
			aaInsuranceInfo = new FrmInsuranceInfoREG104();
			aaInsuranceInfo.setModal(true);
			aaInsuranceInfo.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
			aaInsuranceInfo.show();
			Insets laInsets = aaInsuranceInfo.getInsets();
			aaInsuranceInfo.setSize(
				aaInsuranceInfo.getWidth()
					+ laInsets.left
					+ laInsets.right,
				aaInsuranceInfo.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7889
			// Changed to RTS setvisible for Java 1.4
			// aaInsuranceInfo.setVisible(true);
			aaInsuranceInfo.setVisibleRTS(true);
			// end defect 7889
		}
		catch (Throwable aeIVJExc)
		{
			System.err.println(MAIN_EXCEPTION_MSG);
			aeIVJExc.printStackTrace(System.out);
		}
	}
	/**
	 * Method to handle events for the ActionListener interface.
	 * 
	 * @param leAE ActionEvent
	 */
	public void actionPerformed(ActionEvent leAE)
	{
		if (leAE.getSource() == getButtonPanel1().getBtnCancel())
		{
			// defect 7889
			// change controller.CANCEL to be accessed in a static way
			getController().processData(
				AbstractViewController.CANCEL,
				caData);
			// end defect 7889
		}
		else if (leAE.getSource() == getButtonPanel1().getBtnHelp())
		{
			RTSHelp.displayHelp(RTSHelp.REG104);
		}
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * This is the ECH or ENTER, CANCEL, HELP.
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
				ivjButtonPanel1.setBounds(87, 202, 250, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);

				// Enter is not needed
				ivjButtonPanel1.getBtnEnter().setEnabled(false);
				ivjButtonPanel1.getBtnEnter().setVisible(false);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjButtonPanel1;
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
					getJPanel1(),
					getJPanel1().getName());
				getJDialogContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
//				ivjJDialogContentPane.setBorder(
//								new TitledBorder(
//									new EtchedBorder(),
//									INS_INFO_LBL));
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJDialogContentPane;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblAgent()
	{
		if (ivjJlblAgent == null)
		{
			try
			{
				ivjJlblAgent = new JLabel();
				ivjJlblAgent.setName("JLabelAgent");
				ivjJlblAgent.setText(AGENT_LBL);
				ivjJlblAgent.setBounds(7, 75, 82, 14);
				//ivjJlblAgent.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblAgent.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblAgent.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblAgent;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblAgent2()
	{
		if (ivjJlblAgent2 == null)
		{
			try
			{
				ivjJlblAgent2 = new JLabel();
				ivjJlblAgent2.setName("JLabelAgent2");
				ivjJlblAgent2.setText(EMPTY_STRING);
				ivjJlblAgent2.setBounds(98, 75, 282, 14);
				//ivjJlblAgent2.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblAgent2;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblEndDate()
	{
		if (ivjJlblEndDate == null)
		{
			try
			{
				ivjJlblEndDate = new JLabel();
				ivjJlblEndDate.setName("JLabelEndDate");
				ivjJlblEndDate.setText(END_DATE_LBL);
				ivjJlblEndDate.setBounds(223, 123, 65, 14);
				//ivjJlblEndDate.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblEndDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblEndDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblEndDate;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblEndDate2()
	{
		if (ivjJlblEndDate2 == null)
		{
			try
			{
				ivjJlblEndDate2 = new JLabel();
				ivjJlblEndDate2.setName("JLabelEndDate2");
				ivjJlblEndDate2.setText(EMPTY_STRING);
				ivjJlblEndDate2.setBounds(297, 123, 84, 14);
				//ivjJlblEndDate2.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblEndDate2;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblInsuranceCo()
	{
		if (ivjJlblInsuranceCo == null)
		{
			try
			{
				ivjJlblInsuranceCo = new JLabel();
				ivjJlblInsuranceCo.setName("JLabelInsuranceCo");
				ivjJlblInsuranceCo.setText(INS_CO_LBL);
				ivjJlblInsuranceCo.setBounds(7, 27, 82, 14);
				//ivjJlblInsuranceCo.setForeground(
				//	new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblInsuranceCo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblInsuranceCo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblInsuranceCo;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblInsuranceCo2()
	{
		if (ivjJlblInsuranceCo2 == null)
		{
			try
			{
				ivjJlblInsuranceCo2 = new JLabel();
				ivjJlblInsuranceCo2.setName("JLabelInsuranceCo2");
				ivjJlblInsuranceCo2.setText(EMPTY_STRING);
				ivjJlblInsuranceCo2.setBounds(98, 27, 282, 14);
				//ivjJlblInsuranceCo2.setForeground(
				//	new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblInsuranceCo2;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPhone()
	{
		if (ivjJlblPhone == null)
		{
			try
			{
				ivjJlblPhone = new JLabel();
				ivjJlblPhone.setName("JLabelPhone");
				ivjJlblPhone.setText(PHONE_LBL);
				ivjJlblPhone.setBounds(7, 99, 82, 14);
				//ivjJlblPhone.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblPhone.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblPhone.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblPhone;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPhone2()
	{
		if (ivjJlblPhone2 == null)
		{
			try
			{
				ivjJlblPhone2 = new JLabel();
				ivjJlblPhone2.setName("JLabelPhone2");
				ivjJlblPhone2.setText(EMPTY_STRING);
				ivjJlblPhone2.setBounds(98, 99, 115, 14);
				//ivjJlblPhone2.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblPhone2;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPolicyNo()
	{
		if (ivjJlblPolicyNo == null)
		{
			try
			{
				ivjJlblPolicyNo = new JLabel();
				ivjJlblPolicyNo.setName("JLabelPolicyNo");
				ivjJlblPolicyNo.setText(POLICY_NO_LBL);
				ivjJlblPolicyNo.setBounds(7, 51, 82, 14);
				//ivjJlblPolicyNo.setForeground(new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblPolicyNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblPolicyNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblPolicyNo;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPolicyNo2()
	{
		if (ivjJlblPolicyNo2 == null)
		{
			try
			{
				ivjJlblPolicyNo2 = new JLabel();
				ivjJlblPolicyNo2.setName("JLabelPolicyNo2");
				ivjJlblPolicyNo2.setText(EMPTY_STRING);
				ivjJlblPolicyNo2.setBounds(98, 51, 282, 14);
				//ivjJlblPolicyNo2.setForeground(
				//	new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblPolicyNo2;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblStartDate()
	{
		if (ivjJlblStartDate == null)
		{
			try
			{
				ivjJlblStartDate = new JLabel();
				ivjJlblStartDate.setName("JLabelStartDate");
				ivjJlblStartDate.setText(START_DATE_LBL);
				ivjJlblStartDate.setBounds(223, 99, 65, 14);
				//ivjJlblStartDate.setForeground(
				//	new Color(102, 102, 153));
				// user code begin {1}
				ivjJlblStartDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblStartDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblStartDate;
	}
	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblStartDate2()
	{
		if (ivjJlblStartDate2 == null)
		{
			try
			{
				ivjJlblStartDate2 = new JLabel();
				ivjJlblStartDate2.setName("JLabelStartDate2");
				ivjJlblStartDate2.setText(EMPTY_STRING);
				ivjJlblStartDate2.setBounds(297, 99, 84, 14);
				//ivjJlblStartDate2.setForeground(
				//	new Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJlblStartDate2;
	}
	/**
	 * Return the JPanel property value.
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
				ivjJPanel1.setName("JPanel6");
				ivjJPanel1.setLayout(null);
				//ivjJPanel1.setBackground(new Color(204, 204, 204));
				ivjJPanel1.setBounds(16, 27, 392, 160);
				getJPanel1().add(
					getJlblInsuranceCo(),
					getJlblInsuranceCo().getName());
				getJPanel1().add(
					getJlblPolicyNo(),
					getJlblPolicyNo().getName());
				getJPanel1().add(
					getJlblAgent(),
					getJlblAgent().getName());
				getJPanel1().add(
					getJlblStartDate(),
					getJlblStartDate().getName());
				getJPanel1().add(
					getJlblPhone(),
					getJlblPhone().getName());
				getJPanel1().add(
					getJlblEndDate(),
					getJlblEndDate().getName());
				getJPanel1().add(
					getJlblInsuranceCo2(),
					getJlblInsuranceCo2().getName());
				getJPanel1().add(
					getJlblPolicyNo2(),
					getJlblPolicyNo2().getName());
				getJPanel1().add(
					getJlblAgent2(),
					getJlblAgent2().getName());
				getJPanel1().add(
					getJlblPhone2(),
					getJlblPhone2().getName());
				getJPanel1().add(
					getJlblStartDate2(),
					getJlblStartDate2().getName());
				getJPanel1().add(
					getJlblEndDate2(),
					getJlblEndDate2().getName());
				// user code begin {1}
				ivjJPanel1.setBorder(
				new TitledBorder(
					new EtchedBorder(),
					INS_INFO_LBL));
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
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
		// defect 7889
		// add RTSException to handleException
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7889
		/* Uncomment the following lines to print uncaught exceptions 
		 * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
	}
	
	// not used
	///**
	// * Initializes connections
	// * 
	// * @throws Exception
	// * @deprecated
	// */
	//private void initConnections() throws Exception
	//{
	//	// user code begin {1}
	//	// user code end
	//	getButtonPanel1().getBtnCancel().addActionListener(this);
	//}
	
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			setModal(true);
			// user code end
			setName("InsuranceInfo");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			//setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(433, 279);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		//trap window closing (else corrupts RTS Controller Stack)
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// user code end
	}
	/**
	 * This an abstract method that is inhereted by RTSDialogBox.
	 * This is used to set all the data on the frame before it is 
	 * displayed.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null
			&& aaData.getClass().isInstance(new VehicleInsuranceData()))
		{
			VehicleInsuranceData laData = (VehicleInsuranceData) aaData;

			//update display values
			getJlblInsuranceCo2().setText(laData.getCompanyName());
			getJlblPolicyNo2().setText(laData.getPolicyNo());
			getJlblAgent2().setText(laData.getAgentName());
			String lsPhoneNo = laData.getPhoneNo();
			String lsPhone =
				lsPhoneNo.substring(0, 3)
					+ DASH
					+ lsPhoneNo.substring(3, 6)
					+ DASH
					+ lsPhoneNo.substring(6, 10);
			getJlblPhone2().setText(lsPhone);

			//set date display
			RTSDate laStartDate =
				new RTSDate(
					1,
					Integer.parseInt(laData.getPolicyStartDt()));
			RTSDate laEndDate =
				new RTSDate(
					1,
					Integer.parseInt(laData.getPolicyEndDt()));
			String lsStart = laStartDate.toString();
			String lsEnd = laEndDate.toString(); 
//			String lsStart =
//				laStartDate.getMonth()
//					+ FOR_SLASH
//					+ laStartDate.getDate()
//					+ FOR_SLASH
//					+ laStartDate.getYear();
//			String lsEnd =
//				laEndDate.getMonth()
//					+ FOR_SLASH
//					+ laEndDate.getDate()
//					+ FOR_SLASH
//					+ laEndDate.getYear();
			getJlblStartDate2().setText(lsStart);
			getJlblEndDate2().setText(lsEnd);
		}
	}
}