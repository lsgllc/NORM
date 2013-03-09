package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.OwnershipEvidenceCodesCache;
import com.txdot.isd.rts.services.data.OwnershipEvidenceCodesData;
import com.txdot.isd.rts.services.data.TitleInProcessData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmDeleteTitleinProcessTTL042.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Rajangam	06/26/2001	validations update
 * R Taylor		06/23/2004	Aligned surrendered evidence, vin and make
 *							text fields in visual composition
 *							defect 7229 Ver 5.2.1
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Comment out unused variables
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3
 * S Johnston	06/22/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
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
 * J Ralph		12/06/2005	Corrected DOCUMENT_NUMBER Constant spelling
 * 							Aligned Applicant Name using Visual Editor 
 * 							defect 7898 Ver 5.2.3    
 * B Hargrove	04/01/2008	Change all occurrences of 'progress' to   
 * 							'process' (ie: TitleinProgress).
 * 							Removed getBuilderData().
 * 							modify field names, constants, etc. 
 * 							defect 8786 Ver Defect POS A
 * ---------------------------------------------------------------------
 */
/**
 * Delete Title in Process screen. Displays the Title information.
 *
 * @version	POS A 			04/01/2008
 * @author	Marx Rajangam
 * <br>Creation Date:		06/26/2001 14:54:22
 */
public class FrmDeleteTitleinProcessTTL042
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjlblApplicantName = null;
	private JLabel ivjlblApplicationDate = null;
	private JLabel ivjlblCountyname = null;
	private JLabel ivjlblDocumentNumber = null;
	private JLabel ivjlblSurrenderedEvidence = null;
	private JLabel ivjlblVIN = null;
	private JLabel ivjstcLblApplicantName = null;
	private JLabel ivjstcLblApplicationDate = null;
	private JLabel ivjstcLblCountyName = null;
	private JLabel ivjstcLblDocumentnumber = null;
	private JLabel ivjstcLblSurrenderedEvidence = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjlblVehicleMake = null;
	private JLabel ivjstcLblVehicleMake = null;
	private JPanel ivjFrmDeleteTitleinProcessTTL042ContentPane1 = null;
	
	// Constants String
	private final static String APPLICANT_NAME = "Applicant Name:";
	private final static String APPLICANT_DATE = "Applicant Date:";
	private final static String COUNTY_NAME = "County Name:";
	private final static String DOCUMENT_NUMBER = "Document Number:";
	private final static String SURRND_EVDNCE = "Surrendered Evidence:";
	private final static String VEH_MAKE = "Vehicle Make:";
	private final static String VIN = "VIN:";
	
	/**
	 * FrmDeleteTitleinProcessTTL042 constructor
	 */
	public FrmDeleteTitleinProcessTTL042()
	{
		super();
		initialize();
	}
	/**
	 * FrmDeleteTitleinProcessTTL042 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDeleteTitleinProcessTTL042(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmDeleteTitleinProcessTTL042 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDeleteTitleinProcessTTL042(JFrame aaParent)
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
			//Clear All Fields
			//clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());
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
				RTSHelp.displayHelp(RTSHelp.TTL042);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Displays the error message
	 * 
	 * @param aiCode int
	 */
	private void displayError(int aiCode)
	{
		RTSException leRTSEx = new RTSException(aiCode);
		leRTSEx.displayError(this);
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
				ivjButtonPanel1.setBounds(55, 286, 296, 58);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
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
	 * Return the FrmDeleteTitleinProcessTTL042ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmDeleteTitleinProcessTTL042ContentPane1()
	{
		if (ivjFrmDeleteTitleinProcessTTL042ContentPane1 == null)
		{
			try
			{
				ivjFrmDeleteTitleinProcessTTL042ContentPane1 =
					new JPanel();
				ivjFrmDeleteTitleinProcessTTL042ContentPane1.setName(
					"FrmDeleteTitleinProcessTTL042ContentPane1");
				ivjFrmDeleteTitleinProcessTTL042ContentPane1
					.setLayout(
					null);
				ivjFrmDeleteTitleinProcessTTL042ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmDeleteTitleinProcessTTL042ContentPane1
					.setMinimumSize(
					new Dimension(871, 450));
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblDocumentnumber(),
					getstcLblDocumentnumber().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblApplicantName(),
					getstcLblApplicantName().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblCountyName(),
					getstcLblCountyName().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblApplicationDate(),
					getstcLblApplicationDate().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblSurrenderedEvidence(),
					getstcLblSurrenderedEvidence().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblVIN(),
					getstcLblVIN().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getstcLblVehicleMake(),
					getstcLblVehicleMake().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblDocumentNumber(),
					getlblDocumentNumber().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblApplicantName(),
					getlblApplicantName().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblCountyname(),
					getlblCountyname().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblApplicationDate(),
					getlblApplicationDate().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblSurrenderedEvidence(),
					getlblSurrenderedEvidence().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblVIN(),
					getlblVIN().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
					getlblVehicleMake(),
					getlblVehicleMake().getName());
				getFrmDeleteTitleinProcessTTL042ContentPane1().add(
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
		return ivjFrmDeleteTitleinProcessTTL042ContentPane1;
	}
	/**
	 * Return the lblApplicantName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblApplicantName()
	{
		if (ivjlblApplicantName == null)
		{
			try
			{
				ivjlblApplicantName = new JLabel();
				ivjlblApplicantName.setName("lblApplicantName");
				ivjlblApplicantName.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblApplicantName.setMaximumSize(
					new Dimension(45, 14));
				ivjlblApplicantName.setMinimumSize(
					new Dimension(45, 14));
				ivjlblApplicantName.setBounds(171, 56, 169, 14);
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
		return ivjlblApplicantName;
	}
	/**
	 * Return the lblApplicationDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblApplicationDate()
	{
		if (ivjlblApplicationDate == null)
		{
			try
			{
				ivjlblApplicationDate = new JLabel();
				ivjlblApplicationDate.setName("lblApplicationDate");
				ivjlblApplicationDate.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblApplicationDate.setMaximumSize(
					new Dimension(52, 14));
				ivjlblApplicationDate.setMinimumSize(
					new Dimension(52, 14));
				ivjlblApplicationDate.setBounds(171, 133, 86, 14);
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
		return ivjlblApplicationDate;
	}
	/**
	 * Return the lblCountyname property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblCountyname()
	{
		if (ivjlblCountyname == null)
		{
			try
			{
				ivjlblCountyname = new JLabel();
				ivjlblCountyname.setName("lblCountyname");
				ivjlblCountyname.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblCountyname.setMaximumSize(new Dimension(52, 14));
				ivjlblCountyname.setMinimumSize(new Dimension(52, 14));
				ivjlblCountyname.setBounds(171, 95, 106, 14);
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
		return ivjlblCountyname;
	}
	/**
	 * Return the lblDocumentNumber property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocumentNumber()
	{
		if (ivjlblDocumentNumber == null)
		{
			try
			{
				ivjlblDocumentNumber = new JLabel();
				ivjlblDocumentNumber.setName("lblDocumentNumber");
				ivjlblDocumentNumber.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblDocumentNumber.setMaximumSize(
					new Dimension(45, 14));
				ivjlblDocumentNumber.setMinimumSize(
					new Dimension(45, 14));
				ivjlblDocumentNumber.setBounds(171, 18, 140, 14);
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
		return ivjlblDocumentNumber;
	}
	/**
	 * Return the lblSurrenderedEvidence property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblSurrenderedEvidence()
	{
		if (ivjlblSurrenderedEvidence == null)
		{
			try
			{
				ivjlblSurrenderedEvidence = new JLabel();
				ivjlblSurrenderedEvidence.setName(
					"lblSurrenderedEvidence");
				ivjlblSurrenderedEvidence.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblSurrenderedEvidence.setMaximumSize(
					new Dimension(52, 14));
				ivjlblSurrenderedEvidence.setMinimumSize(
					new Dimension(52, 14));
				ivjlblSurrenderedEvidence.setBounds(171, 170, 212, 14);
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
		return ivjlblSurrenderedEvidence;
	}
	/**
	 * Return the lblVehicleMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleMake()
	{
		if (ivjlblVehicleMake == null)
		{
			try
			{
				ivjlblVehicleMake = new JLabel();
				ivjlblVehicleMake.setName("lblVehicleMake");
				ivjlblVehicleMake.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblVehicleMake.setMaximumSize(new Dimension(52, 14));
				ivjlblVehicleMake.setMinimumSize(new Dimension(52, 14));
				ivjlblVehicleMake.setBounds(171, 242, 137, 21);
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
		return ivjlblVehicleMake;
	}
	/**
	 * Return the lblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setName("lblVIN");
				ivjlblVIN.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblVIN.setMaximumSize(new Dimension(52, 14));
				ivjlblVIN.setMinimumSize(new Dimension(52, 14));
				ivjlblVIN.setBounds(171, 207, 168, 14);
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
		return ivjlblVIN;
	}
	/**
	 * Return the stcLblApplicantName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblApplicantName()
	{
		if (ivjstcLblApplicantName == null)
		{
			try
			{
				ivjstcLblApplicantName = new JLabel();
				ivjstcLblApplicantName.setName("stcLblApplicantName");
				ivjstcLblApplicantName.setText(APPLICANT_NAME);
				ivjstcLblApplicantName.setMaximumSize(
					new Dimension(92, 14));
				ivjstcLblApplicantName.setMinimumSize(
					new Dimension(92, 14));
				ivjstcLblApplicantName.setBounds(27, 56, 130, 14);
				// user code begin {1}
				ivjstcLblApplicantName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblApplicantName;
	}
	/**
	 * Return the stcLblApplicationDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblApplicationDate()
	{
		if (ivjstcLblApplicationDate == null)
		{
			try
			{
				ivjstcLblApplicationDate = new JLabel();
				ivjstcLblApplicationDate.setName(
					"stcLblApplicationDate");
				ivjstcLblApplicationDate.setText(APPLICANT_DATE);
				ivjstcLblApplicationDate.setMaximumSize(
					new Dimension(95, 14));
				ivjstcLblApplicationDate.setMinimumSize(
					new Dimension(95, 14));
				ivjstcLblApplicationDate.setBounds(27, 131, 130, 14);
				// user code begin {1}
				ivjstcLblApplicationDate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblApplicationDate;
	}
	/**
	 * Return the stcLblCountyName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCountyName()
	{
		if (ivjstcLblCountyName == null)
		{
			try
			{
				ivjstcLblCountyName = new JLabel();
				ivjstcLblCountyName.setName("stcLblCountyName");
				ivjstcLblCountyName.setText(COUNTY_NAME);
				ivjstcLblCountyName.setMaximumSize(
					new Dimension(78, 14));
				ivjstcLblCountyName.setMinimumSize(
					new Dimension(78, 14));
				ivjstcLblCountyName.setBounds(27, 95, 130, 14);
				// user code begin {1}
				ivjstcLblCountyName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyName;
	}
	/**
	 * Return the stcLblDocumentnumber property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDocumentnumber()
	{
		if (ivjstcLblDocumentnumber == null)
		{
			try
			{
				ivjstcLblDocumentnumber = new JLabel();
				ivjstcLblDocumentnumber.setName("stcLblDocumentnumber");
				ivjstcLblDocumentnumber.setText(DOCUMENT_NUMBER);
				ivjstcLblDocumentnumber.setMaximumSize(
					new Dimension(109, 14));
				ivjstcLblDocumentnumber.setMinimumSize(
					new Dimension(109, 14));
				ivjstcLblDocumentnumber.setBounds(27, 19, 130, 14);
				// user code begin {1}
				ivjstcLblDocumentnumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDocumentnumber;
	}
	/**
	 * Return the stcLblSurrenderedEvidence property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSurrenderedEvidence()
	{
		if (ivjstcLblSurrenderedEvidence == null)
		{
			try
			{
				ivjstcLblSurrenderedEvidence = new JLabel();
				ivjstcLblSurrenderedEvidence.setName(
					"stcLblSurrenderedEvidence");
				ivjstcLblSurrenderedEvidence.setText(
					SURRND_EVDNCE);
				ivjstcLblSurrenderedEvidence.setMaximumSize(
					new Dimension(129, 14));
				ivjstcLblSurrenderedEvidence.setMinimumSize(
					new Dimension(129, 14));
				ivjstcLblSurrenderedEvidence.setBounds(
					27,
					170,
					130,
					14);
				// user code begin {1}
				ivjstcLblSurrenderedEvidence.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSurrenderedEvidence;
	}
	/**
	 * Return the stcLblVehicleMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVehicleMake()
	{
		if (ivjstcLblVehicleMake == null)
		{
			try
			{
				ivjstcLblVehicleMake = new JLabel();
				ivjstcLblVehicleMake.setName("stcLblVehicleMake");
				ivjstcLblVehicleMake.setText(VEH_MAKE);
				ivjstcLblVehicleMake.setMaximumSize(
					new Dimension(79, 14));
				ivjstcLblVehicleMake.setMinimumSize(
					new Dimension(79, 14));
				ivjstcLblVehicleMake.setBounds(27, 244, 130, 14);
				// user code begin {1}
				ivjstcLblVehicleMake.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVehicleMake;
	}
	/**
	 * Return the stcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(new Dimension(22, 14));
				ivjstcLblVIN.setBounds(27, 207, 130, 14);
				// user code begin {1}
				ivjstcLblVIN.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVIN;
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
			setName(ScreenConstant.TTL042_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(415, 369);
			setTitle(ScreenConstant.TTL042_FRAME_TITLE);
			setContentPane(
				getFrmDeleteTitleinProcessTTL042ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
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
			FrmDeleteTitleinProcessTTL042 
				laFrmDeleteTitleinProcessTTL042;
			laFrmDeleteTitleinProcessTTL042 =
				new FrmDeleteTitleinProcessTTL042();
			laFrmDeleteTitleinProcessTTL042.setModal(true);
			laFrmDeleteTitleinProcessTTL042
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDeleteTitleinProcessTTL042.show();
			Insets laInsets =
				laFrmDeleteTitleinProcessTTL042.getInsets();
			laFrmDeleteTitleinProcessTTL042.setSize(
				laFrmDeleteTitleinProcessTTL042.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmDeleteTitleinProcessTTL042.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmDeleteTitleinProcessTTL042.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			try
			{
				//the data object received should be the title in 
				//	Process data
				TitleInProcessData laTitleInProcessData =
					(TitleInProcessData) aaDataObject;

				//set all the fields in the screen
				getlblVIN().setText(laTitleInProcessData.getVIN());
				getlblApplicantName().setText(
					laTitleInProcessData.getOwnrTtlName1());
				getlblVehicleMake().setText(
					laTitleInProcessData.getVehMk());
				getlblApplicationDate().setText(
					laTitleInProcessData.getTransAMDate().toString());
				getlblDocumentNumber().setText(
					laTitleInProcessData.getDocNo());

				//set the county name from ofc iss. #
				int liOfcIssID =
					laTitleInProcessData.getOfcIssuanceNo();

				//get county desc from cache using Ofc id.
				String lsCountyName =
					OfficeIdsCache.getOfcId(liOfcIssID).getOfcName();

				//set county name on screen 
				getlblCountyname().setText(lsCountyName);

				//set the ownrshp evd desc from cd
				int liOwnrshpEvdCd =
					laTitleInProcessData.getOwnrshpEvidCd();

				Vector lvOwnrEvid =
					OwnershipEvidenceCodesCache.getOwnrEvidCds(
						OwnershipEvidenceCodesCache
							.SORT_BY_EVIDFREQSORTNO);
				for (int liIndex = 0;
					liIndex < lvOwnrEvid.size();
					liIndex++)
				{
					OwnershipEvidenceCodesData laOwnrShpEvidData =
						(
							OwnershipEvidenceCodesData) lvOwnrEvid
								.elementAt(
							liIndex);
					if (laOwnrShpEvidData.getOwnrshpEvidCd()
						== liOwnrshpEvdCd)
					{
						//set the ownr ship evid description
						getlblSurrenderedEvidence().setText(
							laOwnrShpEvidData.getOwnrshpEvidCdDesc());
					}
				}
			}
			catch (ClassCastException aeCCEx)
			{
				//did not gett he right class 
				displayError(182);
			}
			catch (RTSException aeRTSEx)
			{
				//Problem in gettign owrnshp evid cd
				displayError(182);
			}
			catch (Exception aeEx)
			{
				aeEx.printStackTrace();
			}
		}
		else
		{
			//did not get any data object 
			displayError(182);
		}
	}
}