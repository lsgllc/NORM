package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * FrmTitleSurrenderedTTL029.java
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
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	10/15/2011	State/Cntry field required field, to Alpha Only.
 * 							Additional Cleanup.
 * 							add validateData()
 * 							delete STATE_CNTRY_MAX_LEN
 * 							delete 'implements FocusListener'  
 * 							delete getBuilderData(), focusLost(), 
 * 							 focusGained()
 * 							modify actionPerformed(), gettxtStateCountry(),
 * 							 setDataToVehObj() 
 * 							defect 11004 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture the surrendered title state/country in status
 * change.
 * 
 * @version 6.9.0	10/15/2011
 * @author Ashish Mahajan 
 * @since 			06/26/2001 15:21:47
 */
public class FrmTitleSurrenderedTTL029 extends RTSDialogBox implements
		ActionListener
		// defect 11004
		//,FocusListener
		// end defect 11004
{
	/******************************************************** 
	 * WARNING: Do not cleanup w/o consideration of G360 
	 *           automated usage. 
	 ***************************************************/
	
	private ButtonPanel ivjButtonPanel1 = null;

	private JLabel ivjstcLblEnterStateCountry = null;

	private RTSInputField ivjtxtStateCountry = null;

	private JPanel ivjFrmTitleSurrenderedTTL029ContentPane1 = null;

	private VehicleInquiryData caVehInqData = null;

	// defect 11004
	//private final static int STATE_CNTRY_MAX_LEN = 2;
	// end defect 11004

	/**
	 * FrmTitleSurrenderedTTL029 constructor comment.
	 */
	public FrmTitleSurrenderedTTL029()
	{
		super();
		initialize();
	}

	/**
	 * FrmTitleSurrenderedTTL029 constructor.
	 * 
	 * @param aaParent
	 *            javax.swing.JFrame
	 */
	public FrmTitleSurrenderedTTL029(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTitleSurrenderedTTL029 constructor.
	 * 
	 * @param aaParent
	 *            javax.swing.JFrame
	 */
	public FrmTitleSurrenderedTTL029(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		try
		{
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 11004
				// State/Cntry was not previously validated, required 
				if (validateData())
				{		
					setDataToVehObj();
				
					getController().processData(
							AbstractViewController.ENTER,
							getController().getData());
				}
				// end defect 11004 
			}
			else if (aaAE.getSource() == getButtonPanel1()
					.getBtnCancel())
			{
				getController().processData(
						AbstractViewController.CANCEL,
						getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL029);
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(new java.awt.Dimension(
						217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the FrmTitleSurrenderedTTL029ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTitleSurrenderedTTL029ContentPane1()
	{
		if (ivjFrmTitleSurrenderedTTL029ContentPane1 == null)
		{
			try
			{
				ivjFrmTitleSurrenderedTTL029ContentPane1 = new JPanel();
				ivjFrmTitleSurrenderedTTL029ContentPane1
						.setName("FrmTitleSurrenderedTTL029ContentPane1");
				ivjFrmTitleSurrenderedTTL029ContentPane1
						.setLayout(new java.awt.GridBagLayout());
				ivjFrmTitleSurrenderedTTL029ContentPane1
						.setMaximumSize(new java.awt.Dimension(
								2147483647, 2147483647));
				ivjFrmTitleSurrenderedTTL029ContentPane1
						.setMinimumSize(new java.awt.Dimension(425, 240));

				java.awt.GridBagConstraints constraintsstcLblEnterStateCountry = new java.awt.GridBagConstraints();
				constraintsstcLblEnterStateCountry.gridx = 1;
				constraintsstcLblEnterStateCountry.gridy = 1;
				constraintsstcLblEnterStateCountry.ipadx = 4;
				constraintsstcLblEnterStateCountry.insets = new java.awt.Insets(
						61, 79, 12, 2);
				getFrmTitleSurrenderedTTL029ContentPane1().add(
						getstcLblEnterStateCountry(),
						constraintsstcLblEnterStateCountry);

				java.awt.GridBagConstraints constraintstxtStateCountry = new java.awt.GridBagConstraints();
				constraintstxtStateCountry.gridx = 2;
				constraintstxtStateCountry.gridy = 1;
				constraintstxtStateCountry.fill = java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtStateCountry.weightx = 1.0;
				constraintstxtStateCountry.ipadx = 49;
				constraintstxtStateCountry.ipady = -1;
				constraintstxtStateCountry.insets = new java.awt.Insets(
						57, 2, 11, 108);
				getFrmTitleSurrenderedTTL029ContentPane1().add(
						gettxtStateCountry(),
						constraintstxtStateCountry);

				java.awt.GridBagConstraints constraintsButtonPanel1 = new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 2;
				constraintsButtonPanel1.gridwidth = 2;
				constraintsButtonPanel1.fill = java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 30;
				constraintsButtonPanel1.ipady = 13;
				constraintsButtonPanel1.insets = new java.awt.Insets(
						12, 56, 33, 58);
				getFrmTitleSurrenderedTTL029ContentPane1().add(
						getButtonPanel1(), constraintsButtonPanel1);
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
		return ivjFrmTitleSurrenderedTTL029ContentPane1;
	}

	/**
	 * Return the stcLblEnterStateCountry property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterStateCountry()
	{
		if (ivjstcLblEnterStateCountry == null)
		{
			try
			{
				ivjstcLblEnterStateCountry = new JLabel();
				ivjstcLblEnterStateCountry
						.setName("stcLblEnterStateCountry");
				ivjstcLblEnterStateCountry
						.setText("Enter State/Country:");
				ivjstcLblEnterStateCountry
						.setMaximumSize(new java.awt.Dimension(113, 14));
				ivjstcLblEnterStateCountry
						.setMinimumSize(new java.awt.Dimension(113, 14));
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
		return ivjstcLblEnterStateCountry;
	}

	/**
	 * Return the txtStateCountry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtStateCountry()
	{
		if (ivjtxtStateCountry == null)
		{
			try
			{
				ivjtxtStateCountry = new RTSInputField();
				ivjtxtStateCountry.setName("txtStateCountry");
				// defect 11004
				ivjtxtStateCountry.setMaxLength(TitleConstant.STATE_CNTRY_MAX_LEN);
				// end defect 11004 
				ivjtxtStateCountry
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
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
		return ivjtxtStateCountry;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeException);
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
			setName(ScreenConstant.TTL029_FRAME_NAME);
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(361, 192);
			setTitle(ScreenConstant.TTL029_FRAME_TITLE);
			setContentPane(getFrmTitleSurrenderedTTL029ContentPane1());
		}
		catch (Throwable aeException)
		{
			handleException(aeException);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTitleSurrenderedTTL029 laFrmTitleSurrenderedTTL029;
			laFrmTitleSurrenderedTTL029 = new FrmTitleSurrenderedTTL029();
			laFrmTitleSurrenderedTTL029.setModal(true);
			laFrmTitleSurrenderedTTL029
					.addWindowListener(new java.awt.event.WindowAdapter()
					{
						public void windowClosing(
								java.awt.event.WindowEvent laWE)
						{
							System.exit(0);
						};
					});
			laFrmTitleSurrenderedTTL029.show();
			java.awt.Insets insets = laFrmTitleSurrenderedTTL029
					.getInsets();
			laFrmTitleSurrenderedTTL029.setSize(
					laFrmTitleSurrenderedTTL029.getWidth()
							+ insets.left + insets.right,
					laFrmTitleSurrenderedTTL029.getHeight()
							+ insets.top + insets.bottom);
			laFrmTitleSurrenderedTTL029.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leException.printStackTrace(System.out);
		}
	}

	/**
	 * Set controller.
	 * 
	 * @param aaController
	 */
	public void setController(AbstractViewController aaController)
	{
		super.setController(aaController);
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;
			TitleData laTtlData = caVehInqData.getMfVehicleData()
					.getTitleData();

			gettxtStateCountry().setText(laTtlData.getOthrStateCntry());
		}
	}

	/**
	 * Set data to Vehicle Inquiry Data Object.
	 */
	private void setDataToVehObj()
	{

		TitleData laTtlData = caVehInqData.getMfVehicleData()
		.getTitleData();
		
		// defect 11004 
		laTtlData.setOthrStateCntry(gettxtStateCountry().getText());
		laTtlData.setSurrTtlDate(RTSDate.getCurrentDate().getYYYYMMDDDate());
		// end defect 11004 
	}
	
	/**
	 * Validate Data on Screen
	 * 
	 * @returns boolean
	 */
	private boolean validateData() 
	{
		boolean lbValid = true;
		
		RTSException leRTSEx = new RTSException();
		CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtStateCountry(), 
				leRTSEx,TitleConstant.REQUIRED);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid; 
	}
}
