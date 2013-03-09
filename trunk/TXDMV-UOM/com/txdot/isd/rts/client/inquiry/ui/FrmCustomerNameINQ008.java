package com.txdot.isd.rts.client.inquiry.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

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
import com.txdot.isd.rts.services.util.constants.InquiryConstants;

/*
 * FrmCustomerNameINQ008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * B Arredondo	05/17/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.2.0
 * Ray Rowehl	02/08/2005	Change import for Fees.
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * S Johnston	03/28/2005	Remove duplicate FrmCustomerName screen
 * 							This class was deprecated because there are
 * 							no references to it in the project
 * 							defect 7953 Ver 5.2.3
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel
 * 							defect 8240 Ver 5.2.3
 * S Johnston	07/06/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException
 * 							defect 7887 Ver 5.2.3
 * K Harrell	06/29/2009	Implement new OwnerData. Additional Cleanup.
 * 							add setDataToDataObject(), validateData()
 * 							modify processData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/04/2009	deprecated.  Inquiry uses INQ007 (Accounting)
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/** 
 * INQ008 accepts the customer name when View and Print or Charge Fee 
 * and View and Print options are selected.
 *
 * @version Defect_POS_F	08/04/2009
 * @author  Michael Abernethy
 * @deprecated 
 * <br>Creation Date:		12/17/2001 16:25:34
 */
public class FrmCustomerNameINQ008
	extends RTSDialogBox
	implements ActionListener
{

	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjFrmCustomerNameINQ008ContentPane = null;
	private JLabel ivjstcLblEnterCustName = null;
	private RTSInputField ivjtxtCustomerName = null;

	private VehicleInquiryData caData;

	/**
	 * main entrypoint starts this part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCustomerNameINQ008 laFrmCustomerNameINQ008;
			laFrmCustomerNameINQ008 = new FrmCustomerNameINQ008();
			laFrmCustomerNameINQ008.setModal(true);
			laFrmCustomerNameINQ008
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCustomerNameINQ008.show();
			Insets laInsets = laFrmCustomerNameINQ008.getInsets();
			laFrmCustomerNameINQ008.setSize(
				laFrmCustomerNameINQ008.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCustomerNameINQ008.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCustomerNameINQ008.setVisibleRTS(true);
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
	 * FrmCustomerNameINQ008 constructor.
	 */
	public FrmCustomerNameINQ008()
	{
		super();
		initialize();
	}

	/**
	 * FrmCustomerNameINQ008 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmCustomerNameINQ008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmCustomerNameINQ008 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCustomerNameINQ008(JFrame aaParent)
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
			clearAllColor(this);

			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				// defect 10112 
				if (validateData())
				{
					CompleteTransactionData laTransData =
						new CompleteTransactionData();

					setDataToDataObject(laTransData);
					// end defect 10112 

					getController().processData(
						AbstractViewController.ENTER,
						laTransData);
				}
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INQ008);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the buttonPanel property value.
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
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setAsDefault(this);
				ivjbuttonPanel.addActionListener(this);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the FrmCustomerNameINQ008ContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmCustomerNameINQ008ContentPane()
	{
		if (ivjFrmCustomerNameINQ008ContentPane == null)
		{
			try
			{
				ivjFrmCustomerNameINQ008ContentPane = new JPanel();
				ivjFrmCustomerNameINQ008ContentPane.setName(
					"FrmCustomerNameINQ008ContentPane");
				ivjFrmCustomerNameINQ008ContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints laConstraintsstcLblEnterCustName =
					new GridBagConstraints();
				laConstraintsstcLblEnterCustName.gridx = 1;
				laConstraintsstcLblEnterCustName.gridy = 1;
				laConstraintsstcLblEnterCustName.ipadx = 105;
				laConstraintsstcLblEnterCustName.insets =
					new Insets(17, 8, 8, 9);
				getFrmCustomerNameINQ008ContentPane().add(
					getstcLblEnterCustName(),
					laConstraintsstcLblEnterCustName);

				GridBagConstraints laConstraintstxtCustomerName =
					new GridBagConstraints();
				laConstraintstxtCustomerName.gridx = 1;
				laConstraintstxtCustomerName.gridy = 2;
				laConstraintstxtCustomerName.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtCustomerName.weightx = 1.0;
				laConstraintstxtCustomerName.ipadx = 229;
				laConstraintstxtCustomerName.insets =
					new Insets(9, 8, 8, 9);
				getFrmCustomerNameINQ008ContentPane().add(
					gettxtCustomerName(),
					laConstraintstxtCustomerName);

				GridBagConstraints laConstraintsbuttonPanel =
					new GridBagConstraints();
				laConstraintsbuttonPanel.gridx = 1;
				laConstraintsbuttonPanel.gridy = 3;
				laConstraintsbuttonPanel.fill = GridBagConstraints.BOTH;
				laConstraintsbuttonPanel.weightx = 1.0;
				laConstraintsbuttonPanel.weighty = 1.0;
				laConstraintsbuttonPanel.ipadx = 16;
				laConstraintsbuttonPanel.ipady = 35;
				laConstraintsbuttonPanel.insets =
					new Insets(9, 8, 20, 9);
				getFrmCustomerNameINQ008ContentPane().add(
					getbuttonPanel(),
					laConstraintsbuttonPanel);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjFrmCustomerNameINQ008ContentPane;
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
				ivjstcLblEnterCustName.setText(
					InquiryConstants.TXT_ENTER_CUST_NAME);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
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
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtCustomerName;
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
	private void initialize()
	{
		try
		{
			setName("FrmCustomerNameINQ008");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(250, 175);
			setTitle(InquiryConstants.TITLE_FRM_INQ008);
			setContentPane(getFrmCustomerNameINQ008ContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the
	 * data on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		caData = (VehicleInquiryData) UtilityMethods.copy(aaDataObject);
	}

	/** 
	 * Set Data To Data Object
	 *
	 */
	private void setDataToDataObject(CompleteTransactionData aaCTData)
	{
		String lsCustomerName =
			gettxtCustomerName().getText().toUpperCase();

		aaCTData.setTransCode(getController().getTransCode());
		MFVehicleData laMFData = caData.getMfVehicleData();
		aaCTData.setOrgVehicleInfo(laMFData);
		MFVehicleData laNewData =
			(MFVehicleData) UtilityMethods.copy(laMFData);
		OwnerData laOwnerData = laNewData.getOwnerData();
		if (laOwnerData == null)
		{
			laOwnerData = new OwnerData();
			laNewData.setOwnerData(laOwnerData);
		}

		// defect 10112 
		laOwnerData.setName1(lsCustomerName);
		// end defect 10112 

		aaCTData.setCustName(lsCustomerName);
		aaCTData.setVehicleInfo(laNewData);
		aaCTData.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
		aaCTData.setPrintOptions(caData.getPrintOptions());
//		if (caData.getPrintOptions()
//			== VehicleInquiryData.CHARGE_FEE_VIEW_AND_PRINT)
//		{
//			aaCTData.getRegTtlAddlInfoData().setChrgFeeIndi(1);
//		}
//		else
//		{
//			aaCTData.getRegTtlAddlInfoData().setChrgFeeIndi(0);
//		}
		Fees laFees = new Fees();
		aaCTData =
			laFees.calcFees(getController().getTransCode(), aaCTData);
	}

	/**
	 * Validate Data
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbValid = true;
		
		RTSException leRTSEx = new RTSException();
		
		if (gettxtCustomerName().getText().trim().equals(""))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtCustomerName());
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;

	}
}
