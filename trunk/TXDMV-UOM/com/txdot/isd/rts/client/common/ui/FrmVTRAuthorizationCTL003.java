package com.txdot.isd.rts.client.common.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmVTRAuthorizationCTL003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()        
 * B Hargrove	03/15/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * T Pederson	08/09/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/04/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	08/06/2009	modify input to AlphaNumericOnly
 * 							add AUTHCD_INVALID_ERR
 * 							delete ciExceptionCode 
 * 							modify gettxtAuthCode(), verifyInput()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	06/14/2010 	modify to include Scroll table, 
 * 							 prefill in development
 * 							add DEVAUTHCD_SUFFIX
 * 							add ivjlstHardStops, ivjHardStopScrollPane, 
 * 								get methods
 * 							add setIndicators(),
 * 								setVTRAuthorizationCdInDev()
 * 							modify setData(), actionPerformed()    
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/27/2010	Modify to show the Junk Attributes in the 
 * 							returned indicators 
 * 							modify setIndicators()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * Screen for VTR Authorization CTL003
 *
 * @version	6.5.0 			07/27/2010
 * @author	Nancy Ting
 * <br>Creation Date:		06/25/2001 15:58:06
 */

public class FrmVTRAuthorizationCTL003
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private RTSInputField ivjtxtAuthCode = null;

	// defect 10491 
	private JList ivjlstHardStops = null;
	private JLabel ivjstcLblAuthCode = null;
	private JScrollPane ivjJScrollPane = null;
	private boolean cbPrmtTrans = false;
	private final static String DEVAUTHCD_SUFFIX = "ZZZ001000000";
	// end defect 10491

	//	Objects
	private VehicleInquiryData caVehicleInquiryData = null;
	private VehMiscData caVehMiscData = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private JPanel ivjFrmVTRAuthorizationCTL003ContentPane1 = null;

	// Constants
	private final static int AUTH_CD_LENGTH = 15;
	private final static int AUTHCD_INVALID_ERR =
		ErrorsConstant.ERR_NUM_AUTH_CODE_INVALID;
	private final static String FRM_NAME_CTL003 =
		"FrmVTRAuthorizationCTL003";
	private final static String FRM_TITLE_CTL003 =
		"VTR Authorization         CTL003";
	private final static int REGION = 2;
	private final static String TXT_CANCEL = "Cancel";
	private final static String TXT_ENT_AUTH_CD =
		"Enter authorization code:";
	private final static String TXT_ENTER = "Enter";
	private final static int VTR_HQ = 1;

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmVTRAuthorizationCTL003 laFrmVTRAuthorizationCTL003;
			laFrmVTRAuthorizationCTL003 =
				new FrmVTRAuthorizationCTL003();
			laFrmVTRAuthorizationCTL003.setModal(true);
			laFrmVTRAuthorizationCTL003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmVTRAuthorizationCTL003.show();
			java.awt.Insets laInsets =
				laFrmVTRAuthorizationCTL003.getInsets();
			laFrmVTRAuthorizationCTL003.setSize(
				laFrmVTRAuthorizationCTL003.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVTRAuthorizationCTL003.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmVTRAuthorizationCTL003.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Verify input from user
	 * 
	 * @param String asAuthCode
	 * @throws RTSException
	 */
	public static void verifyInput(String asAuthCode)
		throws RTSException
	{
		// defect 10127 
		if (asAuthCode.length() != AUTH_CD_LENGTH)
		{
			throw new RTSException(AUTHCD_INVALID_ERR);
		}

		// Verify First Three Digits [0-2] As VTR/Regional Office
		try
		{
			int liDMVRegnlOfc =
				Integer.parseInt(asAuthCode.substring(0, 3));

			if (liDMVRegnlOfc == 0)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}

			OfficeIdsData laOfficeIdsData =
				OfficeIdsCache.getOfcId(liDMVRegnlOfc);

			if (laOfficeIdsData == null)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}
			int liOfcIssuanceCd = laOfficeIdsData.getOfcIssuanceCd();

			if (!(liOfcIssuanceCd == VTR_HQ
				|| liOfcIssuanceCd == REGION))
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}

		}
		catch (NumberFormatException aeNFEx)
		{
			throw new RTSException(AUTHCD_INVALID_ERR);
		}

		// Verify next three digits [3-5] as authorizing person's 
		// initials
		String lsInitials = asAuthCode.substring(3, 6);
		char[] lcarrInitials = lsInitials.toCharArray();
		for (int i = 0; i < lcarrInitials.length; i++)
		{
			if (!Character.isLetter(lcarrInitials[i]))
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}
		}

		//Verify Next Three Digits [6-8] as Julian Date
		try
		{
			int liJulianDate =
				Integer.parseInt(asAuthCode.substring(6, 9));
			if (liJulianDate < 1 || liJulianDate > 366)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			throw new RTSException(AUTHCD_INVALID_ERR);
		}

		//Verify Next Four Digits [9-12] As Time of Authorization
		try
		{
			int liTime = Integer.parseInt(asAuthCode.substring(9, 11));
			if (liTime > 23)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}
			liTime = Integer.parseInt(asAuthCode.substring(11, 13));
			if (liTime > 59)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}

		}
		catch (NumberFormatException aeNFEx)
		{
			throw new RTSException(AUTHCD_INVALID_ERR);
		}

		//Verify Last Two Digits [13-14] As Check Digits
		try
		{
			int liCheckSum =
				Integer.parseInt(asAuthCode.substring(13, 15));
			if (liCheckSum > 10)
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}
			int liNum1 = Integer.parseInt(asAuthCode.substring(9, 10));
			int liNum2 = Integer.parseInt(asAuthCode.substring(10, 11));

			if (liCheckSum != (liNum1 + liNum2))
			{
				throw new RTSException(AUTHCD_INVALID_ERR);
			}

		}
		catch (NumberFormatException aeNFEx)
		{
			throw new RTSException(AUTHCD_INVALID_ERR);
		}
		// end defect 10127 
	}

	/**
	 * VtrAuthorization constructor.
	 */
	public FrmVTRAuthorizationCTL003()
	{
		super();
		initialize();
	}

	/**
	 * VtrAuthorization constructor.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmVTRAuthorizationCTL003(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * VtrAuthorization constructor.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmVTRAuthorizationCTL003(JFrame aaOwner)
	{
		super(aaOwner);
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
			Object laObjectSource = aaAE.getSource();

			if (laObjectSource == getbtnEnter())
			{
				try
				{
					verifyInput(gettxtAuthCode().getText());
				}
				catch (RTSException aeRTSEx)
				{
					RTSException leEx = new RTSException();
					leEx.addException(aeRTSEx, gettxtAuthCode());
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}

				caVehMiscData.setAuthCd(
					gettxtAuthCode().getText().toUpperCase());

				// defect 10491 
				int liFcnId =
					cbPrmtTrans
						? VCVTRAuthorizationCTL003.PRMT
						: AbstractViewController.ENTER;

				getController().processData(
					liFcnId,
					caVehicleInquiryData);
				// end defect 10491 
			}
			else if (laObjectSource == getbtnCancel())
			{
				caVehMiscData.setAuthCd(CommonConstant.STR_SPACE_EMPTY);

				getController().processData(
					AbstractViewController.CANCEL,
					caVehicleInquiryData);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setBounds(222, 176, 77, 26);
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(TXT_CANCEL);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnEnter property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setBounds(117, 176, 77, 26);
				ivjbtnEnter.setName("btnEnter");
				ivjbtnEnter.setText(TXT_ENTER);
				// user code begin {1}
				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.addKeyListener(this);
				getRootPane().setDefaultButton(ivjbtnEnter);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnEnter;
	}

	/**
	 * Return the FrmVTRAuthorizationCTL003ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmVTRAuthorizationCTL003ContentPane1()
	{
		if (ivjFrmVTRAuthorizationCTL003ContentPane1 == null)
		{
			try
			{
				ivjFrmVTRAuthorizationCTL003ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmVTRAuthorizationCTL003ContentPane1.setName(
					"FrmVTRAuthorizationCTL003ContentPane1");
				ivjFrmVTRAuthorizationCTL003ContentPane1.setLayout(
					null);
				ivjFrmVTRAuthorizationCTL003ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmVTRAuthorizationCTL003ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(400, 125));

				ivjFrmVTRAuthorizationCTL003ContentPane1.add(
					getstcLblAuthCode(),
					null);
				ivjFrmVTRAuthorizationCTL003ContentPane1.add(
					gettxtAuthCode(),
					null);
				ivjFrmVTRAuthorizationCTL003ContentPane1.add(
					getbtnEnter(),
					null);
				ivjFrmVTRAuthorizationCTL003ContentPane1.add(
					getbtnCancel(),
					null);
				ivjFrmVTRAuthorizationCTL003ContentPane1.add(
					getJScrollPane(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmVTRAuthorizationCTL003ContentPane1;
	}
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane()
	{
		if (ivjJScrollPane == null)
		{
			ivjJScrollPane = new javax.swing.JScrollPane();
			ivjJScrollPane.setViewportView(getlstHardStops());
			ivjJScrollPane.setBounds(57, 20, 312, 78);
		}
		return ivjJScrollPane;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return JList
	 */
	private JList getlstHardStops()
	{
		if (ivjlstHardStops == null)
		{
			ivjlstHardStops = new javax.swing.JList();
			ivjlstHardStops.setFont(
				new java.awt.Font(CommonConstant.FONT_JLIST, 0, 12));
		}
		return ivjlstHardStops;
	}

	/**
	 * Return the stcLblAuthCode property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAuthCode()
	{
		if (ivjstcLblAuthCode == null)
		{
			try
			{
				ivjstcLblAuthCode = new javax.swing.JLabel();
				ivjstcLblAuthCode.setBounds(56, 129, 151, 20);
				ivjstcLblAuthCode.setName("stcLblAuthCode");
				ivjstcLblAuthCode.setText(TXT_ENT_AUTH_CD);
				ivjstcLblAuthCode.setMaximumSize(
					new java.awt.Dimension(141, 14));
				ivjstcLblAuthCode.setMinimumSize(
					new java.awt.Dimension(141, 14));
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
		return ivjstcLblAuthCode;
	}

	/**
	 * Return the txtAuthCode property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtAuthCode()
	{
		if (ivjtxtAuthCode == null)
		{
			try
			{
				ivjtxtAuthCode =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtAuthCode.setBounds(222, 129, 139, 20);
				ivjtxtAuthCode.setName("txtAuthCode");

				// defect 10127 
				ivjtxtAuthCode.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				// end defect 10127 
				ivjtxtAuthCode.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtAuthCode.setMaxLength(15);
				ivjtxtAuthCode.setRequestFocusEnabled(true);
				// user code begin {1}
				ivjtxtAuthCode.setText("");
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtAuthCode;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7885
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7885
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
			setName(FRM_NAME_CTL003);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

			// defect 10491 
			setRequestFocus(false);
			// end defect 10491 

			setSize(425, 247);
			setTitle(FRM_TITLE_CTL003);
			setContentPane(getFrmVTRAuthorizationCTL003ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Handles the navigation between the buttons of the ButtonPanel
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (getbtnEnter().hasFocus())
			{
				getbtnCancel().requestFocus();
			}
			else if (getbtnCancel().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		cbPrmtTrans =
			getController().getTransCode().equals(TransCdConstant.PT72);

		// Case of REG
		if (aaDataObject instanceof VehicleInquiryData)
		{
			caVehicleInquiryData = (VehicleInquiryData) aaDataObject;
			caVehMiscData =
				(VehMiscData) caVehicleInquiryData.getVehMiscData();

			// defect 10491 
			setIndicators();
			setVTRAuthorizationCdInDev();
			setDefaultFocusField(gettxtAuthCode());
			// end defect 10491 
		}
	}

	/**
	 * Add indicators to JList
	 * 
	 */
	private void setIndicators()
	{
		if (caVehicleInquiryData != null)
		{
			Vector lvIndis =
				IndicatorLookup.getIndicators(
					caVehicleInquiryData.getMfVehicleData(),
					getController().getTransCode(),
					IndicatorLookup.SCREEN);

			StringBuffer lsIndis =
				new StringBuffer(CommonConstant.STR_SPACE_EMPTY);

			int liNumIndis = lvIndis.size();
			if (liNumIndis > 0)
			{
				Vector lvRows = new Vector();

				for (int liIndex = 0; liIndex < liNumIndis; liIndex++)
				{
					IndicatorData laData =
						(IndicatorData) lvIndis.get(liIndex);
					String lsHStop = laData.getStopCode();

					if ((lsHStop != null && lsHStop.equals("H"))
						|| laData.isJnkAttrib())
					{
						lsIndis.append(
							laData.getStopCode() == null
								? CommonConstant.STR_SPACE_ONE
								: laData.getStopCode());
						lsIndis.append(CommonConstant.STR_SPACE_TWO);
						lsIndis.append(laData.getDesc());
						lvRows.add(lsIndis.toString());
						lsIndis =
							new StringBuffer(
								CommonConstant.STR_SPACE_EMPTY);
					}
				}
				getlstHardStops().setListData(lvRows);
				getlstHardStops().setSelectedIndex(0);
			}
		}
	}

	/**
	 * Set VTR Authorization Code in Development
	 */
	private void setVTRAuthorizationCdInDev()
	{
		if (SystemProperty.isDevStatus())
		{
			int liRegOfcId =
				OfficeIdsCache.getRegnlOfcId(
					SystemProperty.getOfficeIssuanceNo());

			String lsVTRAuthorizationCd = liRegOfcId + DEVAUTHCD_SUFFIX;
			gettxtAuthCode().setText(lsVTRAuthorizationCd);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
