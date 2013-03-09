package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmCustomerNameINQ007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Abernethy 	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 *							make changes in actionPerformed().
 * B Arredondo	12/19/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes in
 *							actionPerformed().
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Ray Rowehl	02/07/2005	Change Fees to use import definition instead
 * 							of including package with in code.
 * 							Format code and clean some fields.
 * 							add import
 * 							modify actionPerformed()
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * S Johnston	03/28/2005	Remove duplicate FrmCustomerName screen
 * 							modify 
 * 							defect 7953 Ver 5.2.3
 * K Harrell	07/22/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel() 
 * 							defect 8240 Ver 5.2.3 
 * K Harrell	05/19/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	06/28/2009	Implement new OwnerData
 * 							modify actionPerformed() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	09/13/2010	modify actionPerformed()
 * 							defect 10590 Ver 6.6.0 
 * K Harrell	11/14/2011	Accommodate VTR 275 
 * 							modify actionPerformed()
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/07/2011	Set CTData NoMFRecords
 * 							modify actionPerformed()
 * 							defect 11169 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * INQ007 handles the Search by Name functionality found in the 
 * accounting module.  This screen appears in the refund sequence, after
 * the REG003 screen if the results came back empty on the plate search.
 *
 * @version	6.9.0 			12/07/2011
 * @author	Michael Abernethy
 * <br>Creation Date:		06/11/2001 15:35:26 
 */
public class FrmCustomerNameINQ007
	extends RTSDialogBox
	implements ActionListener
{

	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblEnterCustName = null;
	private RTSInputField ivjtxtCustomerName = null;

	//	Object 
	private MFVehicleData caMFData;
	private VehicleInquiryData caVehicleInquiryData;

	private final static String ENTER_NAME = "Enter Customer Name:";
	private final static String TITLE_INQ007 = "Customer Name   INQ007";

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
			FrmCustomerNameINQ007 laFrmCustomerNameINQ007;
			laFrmCustomerNameINQ007 = new FrmCustomerNameINQ007();
			laFrmCustomerNameINQ007.setModal(true);
			laFrmCustomerNameINQ007
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmCustomerNameINQ007.show();
			Insets laInsets = laFrmCustomerNameINQ007.getInsets();
			laFrmCustomerNameINQ007.setSize(
				laFrmCustomerNameINQ007.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCustomerNameINQ007.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCustomerNameINQ007.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmCustomerNameINQ007 constructor comment.
	 */
	public FrmCustomerNameINQ007()
	{
		super();
		initialize();
	}

	/**
	 * Creates a FrmCustomerNameINQ007 with the parent
	 * 
	 * @param aaParent Dialog
	 */
	public FrmCustomerNameINQ007(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Creates a FrmCustomerNameINQ007 with the parent
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCustomerNameINQ007(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE	ActionEvent
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
			
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leRTSEx = new RTSException();
				if (gettxtCustomerName().getText().equals(""))
				{
					// defect 10112 
					leRTSEx.addException(
					//new RTSException(150),
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtCustomerName());
					// end defect 10112 
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				String lsCustomerName =
					gettxtCustomerName().getText().toUpperCase();
				if (getController()
					.getTransCode()
					.equals(TransCdConstant.VEHINQ))
				{
					CompleteTransactionData laCTData =
						new CompleteTransactionData();

					// defect 11052 
					laCTData.setTransCode(UtilityMethods.getVehInqTransCd(
							caVehicleInquiryData.getPrintOptions()));
					laCTData.getRegTtlAddlInfoData().setChrgFeeIndi(
							caVehicleInquiryData.isInqChrgFee() ? 1:0);
					// end defect 11052

					// defect 11169 
					laCTData.setNoMFRecs(caVehicleInquiryData.getNoMFRecs());
					// end defect 11169 

					MFVehicleData laMFData =
						caVehicleInquiryData.getMfVehicleData();
					laCTData.setOrgVehicleInfo(laMFData);
					MFVehicleData laNewMFData =
						(MFVehicleData) UtilityMethods.copy(laMFData);

					laCTData.setCustName(lsCustomerName);
					laCTData.setVehicleInfo(laNewMFData);
					laCTData.setOfcIssuanceNo(
							SystemProperty.getOfficeIssuanceNo());
					laCTData.setPrintOptions(
							caVehicleInquiryData.getPrintOptions());
					// defect 11052 
					//		if (caData.getPrintOptions()
					//			== VehicleInquiryData.CHARGE_FEE_VIEW_AND_PRINT)
					//		{
					//			aaCTData.getRegTtlAddlInfoData().setChrgFeeIndi(1);
					//		}
					//		else
					//		{
					//			aaCTData.getRegTtlAddlInfoData().setChrgFeeIndi(0);
					//		}
					// end defect 11052
					Fees laFees = new Fees();
					laCTData =
						laFees.calcFees(
								laCTData.getTransCode(),
								laCTData);
					getController().processData(
							AbstractViewController.ENTER,
							laCTData);
				}
				else
				{
					OwnerData laOwnerData = caMFData.getOwnerData();
					if (laOwnerData == null)
					{
						laOwnerData = new OwnerData();
						caMFData.setOwnerData(laOwnerData);
					}

					// defect 10112 
					laOwnerData.setName1(lsCustomerName);
					// end defect 10112 

					getController().processData(
						AbstractViewController.ENTER,
						caMFData);
				}
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caMFData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.REFUND))
				{
					RTSHelp.displayHelp(RTSHelp.INQ007);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbuttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("ivjbuttonPanel");
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}
	
	/**
	 * Return the ivjRTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"ivjRTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new GridBagLayout());
				GridBagConstraints constraintstxtCustomerName =
					new GridBagConstraints();
				constraintstxtCustomerName.gridx = 1;
				constraintstxtCustomerName.gridy = 2;
				constraintstxtCustomerName.fill =
					GridBagConstraints.HORIZONTAL;
				constraintstxtCustomerName.weightx = 1.0;
				constraintstxtCustomerName.ipadx = 219;
				constraintstxtCustomerName.insets =
					new Insets(3, 13, 3, 14);
				getRTSDialogBoxContentPane().add(
					gettxtCustomerName(),
					constraintstxtCustomerName);
				GridBagConstraints constraintsbuttonPanel =
					new GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 3;
				constraintsbuttonPanel.fill = GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 6;
				constraintsbuttonPanel.ipady = 25;
				constraintsbuttonPanel.insets =
					new Insets(3, 13, 4, 14);
				getRTSDialogBoxContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				GridBagConstraints constraintsstcLblEnterCustName =
					new GridBagConstraints();
				constraintsstcLblEnterCustName.gridx = 1;
				constraintsstcLblEnterCustName.gridy = 1;
				constraintsstcLblEnterCustName.ipadx = 95;
				constraintsstcLblEnterCustName.insets =
					new Insets(15, 13, 3, 14);
				getRTSDialogBoxContentPane().add(
					getstcLblEnterCustName(),
					constraintsstcLblEnterCustName);
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
	 * Return the ivjstcLblEnterCustName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterCustName()
	{
		if (ivjstcLblEnterCustName == null)
		{
			try
			{
				ivjstcLblEnterCustName = new JLabel();
				ivjstcLblEnterCustName.setName(
					"ivjstcLblEnterCustName");
				ivjstcLblEnterCustName.setText(ENTER_NAME);
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
		return ivjstcLblEnterCustName;
	}

	/**
	 * Return the ivjtxtCustomerName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustomerName()
	{
		if (ivjtxtCustomerName == null)
		{
			try
			{
				ivjtxtCustomerName = new RTSInputField();
				ivjtxtCustomerName.setName("ivjtxtCustomerName");
				ivjtxtCustomerName.setEditable(true);
				ivjtxtCustomerName.setEnabled(true);
				ivjtxtCustomerName.setMaxLength(30);
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
		return ivjtxtCustomerName;
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
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmCustomerNameINQ007");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(250, 125);
			setModal(true);
			setTitle(TITLE_INQ007);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaObject Object 
	 */
	public void setData(Object aaObject)
	{
		if (aaObject instanceof VehicleInquiryData)
		{
			caVehicleInquiryData =
				(VehicleInquiryData) UtilityMethods.copy(aaObject);
			caMFData =
				((VehicleInquiryData) aaObject).getMfVehicleData();
		}
		else
		{
			caMFData = (MFVehicleData) aaObject;
		}
	}
}
