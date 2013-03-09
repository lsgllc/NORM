package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.MiscellaneousData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSupervisorOverrideUpdateSEC017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * B Arredondo	03/12/2004	Re-generated code.
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		03/17/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3 
 * Ray Rowehl	08/24/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3   
 * K Harrell	09/03/2008	Add processing for Admin Logging 
 * 							add  TXT_SUPERVISOR_OVERRIDE
 * 							add getAdminLogData()
 * 							modify actionPerformed()
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	08/22/2009	Implement new AdminLogData constructor()
 * 							modify getAdminLogData() 
 * 							defect 8628 Ver Defect_POS_F             
 * ---------------------------------------------------------------------
 */

/**
 * Class for Supervisor Override Update Screen SEC017
 *
 * @version Defect_POS_B	09/03/2008
 * @author	Ashish Mahajan
 * <br>Creation Date:		10/05/2001 13:40:58
 */

public class FrmSupervisorOverrideUpdateSEC017
	extends RTSDialogBox
	implements ActionListener
{
	private static final int LENGTH_PASSWORD = 6;

	private static final String SEC017_FRAME_TITLE =
		"Supervisor Override Update   SEC017";
	private static final String SEC017_FRAME_NAME =
		"FrmSupervisorOverrideUpdateSEC017";

	// defect 8595 
	// For Admin Log  
	private final static String TXT_SUPERVISOR_OVERRIDE =
		"Suprvsr Override";
	// end defect 8595 

	private static final String MSG_SUPERVISOR_OVERRIDE_UPDATE_CONTINUE =
		"Supervisor Override Code will be updated. "
			+ "Do you want to continue?";

	private static final String TXT_LBL_SUPOVR =
		"Enter the new Supervisor Override Code (six characters):";

	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcSupOvr = null;
	private RTSInputField ivjtxtSupvOvrride = null;
	private ButtonPanel ivjbuttonPanel = null;
	private String csOldSupervisorOverride;

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 */
	public FrmSupervisorOverrideUpdateSEC017()
	{
		super();
		initialize();
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSupervisorOverrideUpdateSEC017(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSupervisorOverrideUpdateSEC017(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSupervisorOverrideUpdateSEC017 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSupervisorOverrideUpdateSEC017(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
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
			// CANCEL
			if (aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			// HELP 
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC017);
			}
			// ENTER 
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leRTSEx = new RTSException();
				//First Validate
				String lsSupvOvr = gettxtSupvOvrride().getText().trim();
				if (csOldSupervisorOverride.equals(lsSupvOvr))
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_SUPER_OVERRIDE_CODE_SAME),
						gettxtSupvOvrride());
				}
				if (lsSupvOvr.length() != LENGTH_PASSWORD)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtSupvOvrride());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				RTSException leRTSEx2 =
					new RTSException(
						RTSException.CTL001,
						MSG_SUPERVISOR_OVERRIDE_UPDATE_CONTINUE,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx2.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					lsSupvOvr =
						UtilityMethods.encryptPassword(lsSupvOvr);
					MiscellaneousData laMiscData =
						new MiscellaneousData();
					laMiscData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laMiscData.setSubstaId(
						SystemProperty.getSubStationId());
					laMiscData.setSupvOvrideCd(lsSupvOvr);

					// defect 8595 
					Vector lvVector = new Vector();
					lvVector.add(CommonConstant.ELEMENT_0, laMiscData);
					lvVector.add(
						CommonConstant.ELEMENT_1,
						getAdminLogData(lsSupvOvr));
					getController().processData(
						AbstractViewController.ENTER,
						lvVector);
					// end defect 8595 

					gettxtSupvOvrride().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtSupvOvrride().requestFocus();
				}
				else
				{
					gettxtSupvOvrride().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtSupvOvrride().requestFocus();
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/** 
	 * Return populated AdminLogData Object
	 * 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(String asEncryptPass)
	{
		// defect 8628 
		AdministrationLogData laLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		laLogData.setEntity(TXT_SUPERVISOR_OVERRIDE);
		laLogData.setAction(CommonConstant.TXT_ADMIN_LOG_REVISE);
		laLogData.setEntityValue(asEncryptPass);
		return laLogData;
	}
	
	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
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
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new GridBagLayout());
				java.awt.GridBagConstraints constraintsstcSupOvr =
					new GridBagConstraints();
				constraintsstcSupOvr.gridx = 1;
				constraintsstcSupOvr.gridy = 1;
				constraintsstcSupOvr.ipadx = 33;
				constraintsstcSupOvr.insets = new Insets(53, 35, 27, 0);
				getRTSDialogBoxContentPane().add(
					getstcSupOvr(),
					constraintsstcSupOvr);
				java.awt.GridBagConstraints constraintstxtSupvOvrride =
					new GridBagConstraints();
				constraintstxtSupvOvrride.gridx = 2;
				constraintstxtSupvOvrride.gridy = 1;
				constraintstxtSupvOvrride.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtSupvOvrride.weightx = 1.0;
				constraintstxtSupvOvrride.ipadx = 95;
				constraintstxtSupvOvrride.insets =
					new Insets(47, 0, 27, 109);
				getRTSDialogBoxContentPane().add(
					gettxtSupvOvrride(),
					constraintstxtSupvOvrride);
				java.awt.GridBagConstraints constraintsbuttonPanel =
					new GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 2;
				constraintsbuttonPanel.gridwidth = 2;
				constraintsbuttonPanel.fill = GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 41;
				constraintsbuttonPanel.ipady = 30;
				constraintsbuttonPanel.insets =
					new Insets(28, 157, 13, 185);
				getRTSDialogBoxContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
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
	 * Return the stcSupOvr property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcSupOvr()
	{
		if (ivjstcSupOvr == null)
		{
			try
			{
				ivjstcSupOvr = new JLabel();
				ivjstcSupOvr.setName("stcSupOvr");
				ivjstcSupOvr.setText(TXT_LBL_SUPOVR);
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
		return ivjstcSupOvr;
	}

	/**
	 * Return the txtSupvOvrride property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtSupvOvrride()
	{
		if (ivjtxtSupvOvrride == null)
		{
			try
			{
				ivjtxtSupvOvrride = new RTSInputField();
				ivjtxtSupvOvrride.setName("txtSupvOvrride");
				ivjtxtSupvOvrride.setMaxLength(LENGTH_PASSWORD);
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
		return ivjtxtSupvOvrride;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//aeEx.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			setName(SEC017_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(600, 200);
			setTitle(SEC017_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmSupervisorOverrideUpdateSEC017 laFrmSupervisorOverrideUpdateSEC017;
			laFrmSupervisorOverrideUpdateSEC017 =
				new FrmSupervisorOverrideUpdateSEC017();
			laFrmSupervisorOverrideUpdateSEC017.setModal(true);
			laFrmSupervisorOverrideUpdateSEC017
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSupervisorOverrideUpdateSEC017.show();
			Insets insets =
				laFrmSupervisorOverrideUpdateSEC017.getInsets();
			laFrmSupervisorOverrideUpdateSEC017.setSize(
				laFrmSupervisorOverrideUpdateSEC017.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSupervisorOverrideUpdateSEC017.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSupervisorOverrideUpdateSEC017.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * This method sets the data on the screen 
	 * and is how the controller relays information
	 * to the view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		String lsEncryptedPass = (String) aaDataObject;
		csOldSupervisorOverride =
			UtilityMethods.decryptPassword(lsEncryptedPass);
	}
}
