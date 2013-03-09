package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;
import com.txdot.isd.rts.client.localoptions.business.FlashDriveRefreshHelper;

import com.txdot.isd.rts.services.cache.RSPSWsStatusCache;
import com.txdot.isd.rts.services.data.RSPSIdsData;
import com.txdot.isd.rts.services.data.RSPSUpdData;
import com.txdot.isd.rts.services.data.RSPSWsStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.refresh.RefreshProgressBarPanel;

/*
 *
 * FrmRSPSStatusUpdateRSP001.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/24/2004	New class 
 * 							defect 7135 Ver 5.2.1
 * Min Wang		08/20/2004	Handle exceptions better
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/13/2004	Pop up a message box about how many updates
 * Min Wang					will be applied.  User will be seeing this
 *							while a thread is running to do the flash
 *							drive refresh.
 *							add handleRefresh(Vector)
 *							modify actionPerformed(), setData()
 *							defect 7135 Ver 5.2.1
 * Min Wang		09/16/2004	Pass dialog to helper to display exceptions
 *							modify handleRefresh()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/20/2004	Add help not available message.
 *							modify actionPerformed()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/28/2004	Improve wait on flash drive writes.
 *							modify handleRefresh()
 *							defect 7135 Ver 5.2.1
 * Min Wang		10/28/2004	Display right message.
 *							modify handleRefresh()
 *							defect 7660 Ver 5.2.1.1
 * Min Wang		10/28/2004	change RSPS Id and RSPS Name Border.
 *							modify itemStateChanged()
 *							defect 7664 Ver 5.2.1.1
 * Min Wang		10/29/2004  change message when flash driv missing.
 *							modify actionPerformed()
 *							defect 7672 Ver 5.2.1.1
 * Min Wang		10/29/2004  display correct message when finishing 
 *							update.
 *							modify setData()
 *							defect 7665 Ver 5.2.1.1
 * Min Wang		11/02/2004	RSPS name is pulling the correct record
 *							 from comboRSPSIdName().
 *							modify actionPerformed(),
 *							getcomboRSPSIdName(),  
 *							itemStateChanged(), setcomboRSPSIdInfo(), 
 *							VC 
 *							defect 7662 Ver 5.2.1.1
 * K Harrell	12/27/2004	Present RSPSIds in Alpha order
 *							modify itemStateChanged()
 *							defect 7801 Ver 5.2.2
 * K Harrell	12/29/2004	Entries in table should not be selectable
 *							Formatting/JavaDoc/Variable Name cleanup
 *							add variables DEALER_CD,SUBCONTRACTOR_CD,
 *							MAX_HOST_NAME_LENGTH 
 *							modify via VC
 *							modify getScrollPaneTable()
 *							defect 7809 Ver 5.2.2
 * Jeff S.		05/04/2005	When checking the refresh thread to see if 
 * 							it was alive it would return false when it
 * 							was actually still working.  This caused
 * 							the finished msg to be displayed prematurely
 * 							Changes where made to allow the thread to
 * 							tell this frame that it is done.
 * 							modify handelRefresh(), setData()
 * 							add enableFame(), getlblProgress()
 * 								handleReturnFromRefresh()
 * 							defect 8190 Ver. 5.2.2 Fix 4
 * Min Wang		03/11/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang 	03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 * 							add import com.txdot.isd.rts.services.util.
 * 								RTSHelp
 *  						modify actionPerfomed() 
 * 							defect 7570 Ver 5.2.2 Fix 5
 * K Harrell	06/17/2005	Removed reference to substaid in RSPS data
 * 							objects & calls.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3  
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3 
 * Min Wang 	08/25/2005	Resize Rodio Button and fix  
 * 							java.lang.StackOverflowError.
 * 							modify itemStateChange()
 * 							resize radio buttons
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		09/19/2005	Added the Progress Bar Panel to frame. Just
 * 							replaced the progress.gif that was added in
 * 							5.2.2 fix 4.
 * 							add refreshProgressBarPanel, 
 * 								getRefreshProgressBarPanel()
 * 							modify enableFame()
 * 							remove getlblProgress(), IMAGE_PROGRESS
 * 							defect 8014 Ver 5.2.3
 * Jeff S.		09/20/2005	Fixed problem cause during cleanup.  Was 
 * 							using a string constant for two different
 * 							instances but needed a separate constant.
 * 							Causing the control.dat to not be created.
 * 							defect 8014 Ver 5.2.3
 * Jeff S.		09/26/2005	Update the laptop list after we return from
 * 							the refresh thread.  This is done so that 
 * 							the user can see the results.  Removed mouse
 * 							listener and it's inherited methods b/c it
 * 							was never used.
 * 							add updateLaptopList()
 * 							modify itemStateChanged(), 
 * 								handleReturnFromRefresh()
 * 							delete mouseDragged(),mouseMoved()
 * 							defect 8380 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboRSPSIdInfo()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed()
 * 							modify getJPanelRadio(), getradioDealer(), 
 * 								getradioSubcon()
 * 							defect 7891 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Frame RSP001 update status for RSPS
 *
 * @version	5.2.3			01/03/2006 
 * @author	Min Wang
 * <br>Creation Date:		06/24/2004 08:50:08
 */
public class FrmRSPSStatusUpdateRSP001
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	// defect 7809 
	private static final String CONTROL_DAT = "control.dat";
	private static final String NO_DEVICE = "The device is not ready";
	private static final String NO_PATH_SPECIFIED =
		"The system cannot find the path specified";
	private static final String CANNOT_FIND_FILE =
		"The system cannot find the file specified";
	private static final String RSP002_FRAME_TITLE =
		"Copy Instructions    RSP002";
	private static final String NO_FLASH_DRIVE =
		"Flash Drive missing.  Please insert the "
			+ "Flash Drive before continuing.";
	private static final String UNKNOWN = "UNKNOWN";
	private static final String ENTITY_SELECTION = "Entity Selection:";
	// defect 8014
	// replaced with a progress bar
	//private static final String IMAGE_PROGRESS = "/images/progress.gif";
	private RefreshProgressBarPanel refreshProgressBarPanel = null;
	// end defect 8014
	private static final String DEALER = " Dealer";
	private static final String SUBCONTRACTOR = " Subcontractor";
	private static final String RSPS_UPDATE_STATUS =
		"RSPS UPDATE STATUS";
	private static final String SEND_FLASH_DRIVE =
		"There are updates to send to the Flash Drive.";
	private static final String FLASH_REFRESH = "FlashRefresh";
	private static final String COMPLETE_PROCESS =
		"Flash Drive processing is complete. You may remove "
			+ "the Flash Drive.";
	private static final String RSP001_FRAME_TITLE =
		"RSPS Status Update     RSP001";
	private static final String SUBCON_ID =
		" Subcontractor Id and Name:";
	private static final String DEALER_ID_NAME = " Dealer Id and Name:";
	private static final int MAX_HOST_NAME_LENGTH = 25;
	// end defect 7809 
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JRadioButton ivjradioDealer = null;
	private JRadioButton ivjradioSubcon = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanelRadio = null;
	private TMRSP001 caTblModel = null;
	private RTSTable ivjScrollPaneTable = null;
	private JScrollPane ivjtblRspsUpdate = null;
	Vector cvRSPSIdInfo = null;
	private int ciRSPSIdInfoIndx = 0;
	// never used
	//private int ciUpdateProgress = 0;
	//private int ciOfcId;
	private JComboBox ivjcomboRSPSIdName = null;
	private JPanel ivjJPanelRSPSIdName = null;
	/**
	 * FrmRSPSStatusUpdateRSP001 constructor
	 */
	public FrmRSPSStatusUpdateRSP001()
	{
		super();
		initialize();
	}

	/**
	 * FrmRSPSStatusUpdateRSP001 constructor
	 *
	 * @param aaOwner Dialog
	 */
	public FrmRSPSStatusUpdateRSP001(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmRSPSStatusUpdateRSP001 constructor
	 *
	 * @param aaOwner Frame
	 */
	public FrmRSPSStatusUpdateRSP001(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is performed 
	 * on the screen.
	 *
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		/**
		 * Code to prevent multiple button clicks
		 */
		if (!startWorking())
		{
			return;
		}
		try
		{
			/**
			 * Returns all fields to their original color state
			 */
			clearAllColor(this);
			if (aaAE.getSource() instanceof JRadioButton)
			{
				setcomboRSPSIdInfo();
				return;
			}

			RSPSUpdData laRSPSUpdData = new RSPSUpdData();

			/**
			 * Determines what actions to take when Enter, Cancel, 
			 * or Help are pressed.
			 */
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				/**
				 * Handles the case when the combo box doesn't contain 
				 * any values
				 */
				//defect 7662
				// if ((String) getcomboRSPSId().getSelectedItem() == null
				//     || ((String) getcomboRSPSId().getSelectedItem()).equals("")
				//     || (String) getcomboRSPSName().getSelectedItem() == null
				//     || ((String) getcomboRSPSName().getSelectedItem()).equals(""))
				if ((String) getcomboRSPSIdName().getSelectedItem()
					== null
					|| (
						(String) getcomboRSPSIdName()
							.getSelectedItem())
							.equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					RTSException leRTSEx = new RTSException();
					//  laRTSException.addException(new RTSException(601), 
					//getcomboRSPSId());
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_ENTITY_INVALID),
						getcomboRSPSIdName());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				//end defect 7662
				// This section verifies that the control file on the 
				// jump drive matches to this machines ofcissuanceno.
				try
				{

					// setup for rsps directory
					File laOfcFile =
						new File(
							SystemProperty
								.getFlashDriveSourceDirectory()
								+ CONTROL_DAT);
					FileInputStream lpfsFileInputStream =
						new FileInputStream(laOfcFile);
					BufferedReader laBufferedReader =
						new BufferedReader(
							new InputStreamReader(lpfsFileInputStream));
					String lsBufRd;

					while ((lsBufRd = laBufferedReader.readLine())
						!= null)
					{
						if (!lsBufRd
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							int liOfc =
								Integer.parseInt(lsBufRd.trim());
							if (SystemProperty.getOfficeIssuanceNo()
								!= liOfc)
							{
								// This comes up if control does not 
								//match local ofcissuanceno.
								RTSException laRTSException =
									new RTSException(780);
								laRTSException.setMsgType(
									RTSException.WARNING_VALIDATION);
								laRTSException.displayError(this);
							}
						}
					}
					laBufferedReader.close();
					lpfsFileInputStream.close();
				}
				catch (FileNotFoundException aeFNFE)
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							aeFNFE);
					if (aeFNFE.getMessage().indexOf(NO_DEVICE) > -1
						|| aeFNFE.getMessage().indexOf(NO_PATH_SPECIFIED)
							> -1)
					{
						leRTSEx.setTitle(RSP002_FRAME_TITLE);
						//defect 7672
						leRTSEx.setMessage(NO_FLASH_DRIVE);
						//end defect 7672
						leRTSEx.displayError(this);
						return;
					}
					if (aeFNFE.getMessage().indexOf(CANNOT_FIND_FILE)
						> -1)
					{
						try
						{
							String lsOffice =
								UtilityMethods.addPadding(
									String.valueOf(
										SystemProperty
											.getOfficeIssuanceNo()),
									3,
									CommonConstant.STR_ZERO);
							// Write to file
							File laOfcFile =
								new File(
									SystemProperty
										.getFlashDriveSourceDirectory()
										+ CONTROL_DAT);
							FileOutputStream lpfsFileOutStream =
								new FileOutputStream(laOfcFile);
							lpfsFileOutStream.write(
								lsOffice.getBytes());
							lpfsFileOutStream.flush();
							lpfsFileOutStream.close();
						}
						catch (IOException aeIOEx)
						{
							RTSException leRTSEx2 =
								new RTSException(
									RTSException.JAVA_ERROR,
									aeIOEx);
							leRTSEx2.displayError(this);
							return;
						}

					}

				}
				catch (Exception aeEx)
				{
					RTSException leRTSEx =
						new RTSException(RTSException.JAVA_ERROR, aeEx);
					leRTSEx.displayError(this);
					// Just return to screen.  maybe clerk forgot drive.
					return;
				}

				laRSPSUpdData.setRSPSWsStatusData(
					new RSPSWsStatusData());
				laRSPSUpdData.getRSPSWsStatusData().setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());

				String lsLocIdCd = CommonConstant.POS_DTA;
				if (getradioSubcon().isSelected())
				{
					lsLocIdCd = CommonConstant.POS_SUB;
				}
				//laRSPSUpdData.setIdType(lsIdType);
				laRSPSUpdData.getRSPSWsStatusData().setLocIdCd(
					lsLocIdCd);

				//defect 7662
				//laRSPSUpdData.setId(
				//    Integer.parseInt(
				//		(String) getcomboRSPSId().getSelectedItem()));
				laRSPSUpdData.getRSPSWsStatusData().setLocId(
					Integer.parseInt(
						(
							(String) getcomboRSPSIdName()
								.getSelectedItem())
								.substring(
							0,
							3)));
				//laRSPSUpdData.getRSPSWsStatusData().setLocId(
				//Integer.parseInt(
				//		(String) getcomboRSPSId().getSelectedItem()));
				//end defect 7662
				laRSPSUpdData
					.getRSPSWsStatusData()
					.setLastProcsdUserName(
					SystemProperty.getCurrentEmpId());

				try
				{
					String lsHostName =
						InetAddress.getLocalHost().getHostName();
					if (lsHostName.length() > MAX_HOST_NAME_LENGTH)
					{
						lsHostName =
							lsHostName.substring(
								0,
								MAX_HOST_NAME_LENGTH);
					}
					laRSPSUpdData
						.getRSPSWsStatusData()
						.setLastProcsdHostName(
						lsHostName);
				}
				catch (Exception aeEx2)
				{
					// just set the value on any exception
					laRSPSUpdData
						.getRSPSWsStatusData()
						.setLastProcsdHostName(
						UNKNOWN);
				}
				getController().processData(
					AbstractViewController.ENTER,
					laRSPSUpdData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				//RTSHelp.displayHelp(RTSHelp.RSPS_STATUS_UPDATE);
				// defect 7570
				//RTSException leRTSEx =
				//	new RTSException(
				//		RTSException.WARNING_MESSAGE,
				//		"Help is not available for this event at " 
				//       + "this time.",
				//		"Help Not Available    HLP001");
				// the value is never used
				//int liRetCode = 
				//leRTSEx.displayError(this);
				//return;
				RTSHelp.displayHelp(RTSHelp.RSP001);
				// end defect 7570
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Handle the enabling and disabling of the frame while the
	 * refresh thread is in the process of moving the data to the flash
	 * drive.
	 * 
	 * While everything is disabled display the progress gif that will
	 * re-assure the user that something is working.
	 * 
	 * @param abStatus boolean
	 */
	public void enableFame(boolean abStatus)
	{
		getButtonPanel1().getBtnHelp().setEnabled(abStatus);
		getButtonPanel1().getBtnCancel().setEnabled(abStatus);

		gettblRspsUpdate().setEnabled(abStatus);
		getcomboRSPSIdName().setEnabled(abStatus);
		getradioDealer().setEnabled(abStatus);
		getradioSubcon().setEnabled(abStatus);

		// do enter btn last so the cursor does not get stuck on any
		// other field
		getButtonPanel1().getBtnEnter().setEnabled(abStatus);

		// defect 8014
		// replaced progress gif with progress bar panel
		// Show the progress gif if everything is being disabled
		// Hide the progress gif if everything is enabled
		getRefreshProgressBarPanel().setVisible(!abStatus);
		//getlblProgress().setVisible(!abStatus);
		// end defect 8014
	}

	/**
	 * VAJ Builder Data 
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GBBF117B1GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBB8DF4D46515C182840CA21A6E66E4E317E32DE6F7E3B7D6BCF2DCEA294DE9E9CD551E52ADFA521633CD3B34329E3ACD3754323BAF1984028608981536E863CFB5949A03C54588AE7953A0C3B2E446A41010491BF9EFE65E644D3C493CB719C9C05DFB3F6FFDEF5E3C4CA48625B96772663B5F5F7D6E3D5F7DFB8F2E718FABEB2B2A9BB92E2A16537F5E28623855EFF15C45950B6FE23840D0010DB37D
			BD8770B8375DD1834F847475DCD7E0EB606EBEDC876DDCE8EF3B364076B47CFE02FB74F5218DFEE4586798748DBDF2E26F6C7B6CB9457659C15AF90375705C8DF08D60D6611C3338FF7160BE9D3F81F03B755F88D5559CD7CC46FCEBFA1F895FF5160E4B571FC74F61182F8F6E577BAB810FE36A815FF4AE3966CEC25D223623FAFBE4458DDC65B74FD46F423DCC6BB34852675E28BF1B388788A3ECDC591BBD70BC6698773F6DD5C2ADDAC2D03496C995DA221A5A6215A5B12C35480A17171508A6A9E135A5A635
			353D938D75BB9DCE2746EBB1757D0840EBA234DBDB8FB78EA37D3F182EA3EBF3DCB96CC7E97D762EE3BAADF963B7F0AEFBC9EDA6DA0706A90D9707133416AC58E3BBB402EBEFA13C47F9EB203F92006EF9F4086D9901F1DB48586D53B83688FE2F8558B042E8B97EB223E568422D5CD60FD63DA783ADC74ED39AC64EA7E9915CFBEC3FA5FAF0139DE5198FFDEBG6E872884E89CAD309D8178BD513F01D3FF02E73BC76BD4C2A1A55C9E09F448C27434A208B27CEEEB039E9D57AFFA152840F1D44736CE9FCAC38F
			837BE82AFEA5F5E210E3BAD15CDF44599F393536B64D5932B65CDABB262F210DA64FC64F55510B674A83FC8940BA000DG1C9BF4504D4E357254DF66BA574930A6C6433C6C0CF9A2FCD46B08C932A0C675737D5760EC67CB87F4DE4D50FC67D13E947D454C17725BE255BB5270253C68DC75B1B765074B1D648B5D7BF8463DB577CCF910FB6DC6BD1CA6FA18833F2BF49D2578EC02AFB761290E5793BC36B7C31FE30C696D50EEF686FBCDA157F0F8B26D99726F9E2FDEB5C669A99B334A76D9875A162D00EF648B
			EC07G3AG8400AC00BC1E497721D32FFCCC3DF518E4CB677E337DA9F80A89AD4A1F8C2B9A9F760A83FCD462BD3228F27A1855F6245D13B26FCB64BEF9E10FF7453098655160406AFCC884C5D269182F599F47B1D111743A2410889B04A29CDB77DE7B937014F9D523F6A9B902767F2B9DF9B9498F400A30G7C2E601167FBAE207DD8853F6F87203CBAF586F14D1E825B898F634B195368ACCE01E20BF2DBDB1764C5B27868945B7FD4AF5A92F1926CE617989143827CD0753E7E5E1DC93E78940366A10247BB52
			89042B86FE3477F10A07B54CDCE67DBE524FEBDAD47244B451A9EA1A9476A7670789ADEA4A75D3B97D0E77230E3960DCB240C563FC3F6AFD42F4BE3B220C47A2AE141C4EA7973133574078A28F7307D953689F4BBCCCCF9F9EE2FA5A356BE66E50F73534FA9AG9BD469217AD96F355E35E5E44D649EFB2707B25C3DB2CF7A3BB7FF5BF2B0639E36CF38BE82657B6DCE647B91B82F83A062E5FCFF352F095E378EDE95598D6B0B0244C4A129E5F4EC33A3330E3D62C82FE32FB9526B58DB0E59F5285311CE07E85F
			FB8E74ADCABCEC5249BE47DEF20743E11190EDCC61E83C71308BF9A105A5EDD0927C22661426C50EAB7122EE3DC46EE08E7CAE74B2DD0A3F40F46945694FF1EB5DFD55697CD97E674355153A8E5589C9DD22B4B94EA04FC5D98C8123BA15D8D853FDB3395FBADE37AFBD7D6947767623BE0E0BD37A387E7E6D9C5A91B9A69AE7AB619FC39EC72590088AAF2C9E730D8F19E33EE6208F63C3991ED9GF4EED5BF6AC746404A501F07DDC8DFBE4CAB86A895187E5CCB6633FB2BABCE27A24BD48C06FC7E4559007294
			A7E2A218E53C58270CB35D6D5CEB699B504FC77B3F69447349A61BD0AE60790EDFE031977A8C0AF3915C2F601F65354C871FEEB0472B026ACFABCBA2BB114A7008E875DFAF8C603AAE408BA2BBFB374BEC335AD5D571CA44BD68F259DE73BAB7D70C6BA79EC7A661ED3F4B59A5C1446B6FD09210BA1078723B446E211DC808AC3EBCB10C32A80176BA000D86BD152E19FA75EB57237074C9D19562A2C1CCB01E7E2F8BF9037ECAC733B86ED5B32F566C7AE25AB88EE12B4E23FA1F15D752A071DF00BF66E334CD3F
			7F3809D790F302FB7603A499BD4D83B62A67FA7B50C0AD53F30E594987C99C647025373B02851F00B60DF7D691FE657A2CF150DD84DF683346C16F8C33B828987A9A7C4CAF3B09ACB4E8E77B59393EB2304BF4AE07A205B563CC61C13C2BCAD472CBE106D3898EA2EEE23C28EF7BB9D9B78F56DC8DC0F1A74FA22E8C5B86EE5DB94455858AEC7B834CBF5DB90C762159E81712365DE85B07B37916CB815CE7267F92BF55BD2CBE0F72CD98C299A50CF60034277471CFF0F5E71FC49D90FDFCCC86D72328E2DFC48F
			65D00E81645F8B0427B8AF92E0766F56B85A969F84FB60FE1C91514BE5CBB8F66CAC331DDBE0EC8D60DE84688290G681EBFBC07EBE812C67438CC429CFE484D99B46D2C42DA0079D6F7AA101BE0D09596F59B7D4BE1330DEE8852786688BCFB02EC1F3633E85F027CA41FE8D163C0E74B20949614B82C06D1AE98132EBEF7EF1F2B67E46F3B6E3EDEF7273D4F592D5F6715F173FDBE7AA81F51760D85753CCCFF56F817D4D19B345860DBB174D584913777DC23C5E64370CF8BBCE9FFD7CABF5E7D831C39FFB225
			9FE99D2FB177972574571A6653F35D73C716CBEF3B679FB979F6EF469A4421F1FA9E077E4CBF3C24AAD0B6DCC75739EF584C1F0602D355D59956495167670DDBEB993FB800B2A4F65EA424363677D04EB265611FC76A74F32C9BE7F67C4ED134B31CDCE05B8750AAB3BB33E18C4776C0DB10191D395EFD901E83FCD04FFE3A85C9D322A4E08324A32A0036EBD394CB3C27FEB7FFFA765272562F6CE07CDDF5670D5C6A45AFD4BF1601AF5932DE63511F2D2DCB2A7029477B9E644DFA685B825098025C8F608440D0
			081D2D420DB4C25C096437EB9A6F8D908F46425181C90CA751F8C9695AF0D9B9E68F7EB47647CC7C01955E833174F90260F5DC3C1FE3F66B5591ABEF64D0523EE505E67A996AB7DA5CA8373C90759BBB8D7CDB84DFE242D3FF321648995BE550579AE6677E7E982EE94440D4EC7AF9AA46D07FB1DE855BA1CD93C3660401E1BFBDFBD4407EBCD82B84E0BD40A600ED465A817EF94A53B47C6C06G18598EA3FF744C12BFFA664E2B07BD98E7F894CDD3C238BE43739E643526C488D24F9FBD488BF00B810D8E5571
			328775DE96FD842D7BC1F2EF76A920B78A73035D234CB7BA9466670E389B48BE8536DC009B81569AFD6D242F9C5ADC846CB8C0CF0475FDED546C3BF00ED5661A3EDEEE1A3E39006E714314BDF62699E75347564EF37D24FF1C96B24BEF01B0377C1689A4F6843E3B26A2A245AD97C87C6273292221A7F9C20301BB29BA94D5C24D24FEEAEC84474B7C1492037177C7D0D67191665FE4E8E7C7A88FD74393E314C393B8E6E4140D59B7B11347F46D078E227D2196351F97A4C5470FB6635D6A08015E05C967E10A4F
			3321BF50BCF23F9A51B39AE32D6F35B7A45702346724504B07C46A0FE3EE2BBFC6BAB520AB87000ED9A119472C1C20E3AA6039559873141C32CE140EB986CFC7140D39CDCB37D7G7AAF82D05B717EF9D6F74E83DCD91445AD7E67515E99390564893544A6C3AD3CC7EB91417435357546E4592E0B02237E66DA32D60D8A71A3409115653A0917182DAF3E2910DBDD1E3EC63E51B5D2EDD729FCA2EA5287BA5CFB74DA7895234C47959BEB8747595A7B7F69FAB2BE131F2E50572C5312EB63132EFD0B1B65C1BD9A
			731FCBBDF81EACE8678314E80CB7EF9FFC0D1BCF5EE5529EE33FEFC8063FB676730E93FF9D83FF8D509AB3E4517D8C17567FBB3DD14804C93447566D150DF344583ACFCF10F3C0BB9F20A4465EADBC2499B498E333620406C9208120F5125931617317D6571666BD0F7A3A6D442775CCD2DFD7E56077903C4B0427FA1CEF6871987415CE32D8671C8F6FC0F39CE2EB0091G8DA08F20A44EFC598131151B23A66CD2A2F6B0FE3229DCC94F37E454FC3ED2D8F3C3FCF6BBCE697A329F695A8CE36B92F097813AG06
			GA289C617AA623B08CCB1D93F2210C09FE3FEDE82EDA3AEB74AE306C8E97B225B4A7B6CC41236FC78DD1CC8251572783F095E2FCDD09E579A78BAAF62571B7014774B49F8ECEF02BE7B945319FB2559E4DF8263CA27526D7FBDF28FB6CCD9E53F1160AB262C321746196C37C01FEB1A6D6F14597E2B81D7B61D3A574669CC715715643ED4CEDB73794F90FC4D85EBBE7F4C844B679B21AFF1016D3FC5331E3F7CC2F24FAD97B26DDFC96EE04DC56B7E7FC3700D972D7BAF0E337DDF043E1C0BEC7F7AD14E3807CF
			1AFEFF767C2719CF273F77F7DC56BD687D707AB247A4C1DF7AFAF144173EDEBC691B3DDE7C21AFF33DF8114FDCAF26B164E7092F5A8C32403C1362BEC7F0B59F9458165B583A6B7C194FF01FFF6E38EA03BFFDDC7C837F4C3878C77E74F171CF7DB36362EF38D9DC5C74810B0B9F65D9DC5CE5608E1218BAG6D5C8F185E593C08AB0476AA83371B604A203D5940FD46E356919CBB175C8BBF4618D4BD7CC24A9E9FA77E1D4FBB914A039FCB0CAF759FB23EEC9FC7DCAB34DD9F3273FEFF1CB3687A096937B2FE79
			6FE34EE55D031EB9C8E28F814E1BE370612B84D7886DD283D721B0FEEDB0F833E2826B0A5B0C765F11763E0FD87B0589B37DB86E5367513114BDBE0DF7E3B3F934A306E7BF8667F2FC4478F1FA1271C3B34E7BC89C4F1FEF34EB9258DEE534F3631FC41E386E5C7C787CA75AE3AE7E515816FF89FD17DE07981484ADC0717B933B12789E91ED9647F23E7FB4F228759FA5F33E4D709BF3F0DADFFD0EE7E3EA32A01685B88AB0841048E2F54F8ED2F7880301FDC77146D4342B9081C6143008F50B22ACDC673C0748
			41183371B06609D14C901DB10FD7896BB95FCD278DFC17484B60898EADA06FD23C66BA6366ACCA63BE68237DCD7EF47D5A82F60EEDC6AE188338C200B5G65G5B96B01A1E991F7B9CC7518DF17B2FE0F39648D8BB1E94231A6465E51AC8F470D107A2CB5EA93D1EDDC86EAE56C51B0DF98F4A983F861428B40DF1670C192C963E065C7163B8768AF6165FA86CAC4500DB8B309100DB88BEE6A1DB7F3EE974074944063E0F2379EC5904B91FBD311056DA326019E74CFFB6B633C670DA4C58F7E1D2DFCA9752BA8D
			25CD18E4E34A813F8520A19B729FGC7B6DBFB4791248DD2BE0F12CC6BF5B93F16B04BB112CD574E4FE6F9766F46D80CDA8C380AEC16EF8D927C7EC510AF835897315C6CDD1E554405C52CA69E632DB571EFF1661A764D3C35A67EC6CA7F833C35A67E07AAF37FE39EEBCD5C2F4F2767D7BD78740B1ADE43BDAE1AEB5B2D978ED65B387439F761A24A7362C5495ABF3D67CFF5BC4B254DEDC99106E64EAC370D13988277AEDF44727F8F5EB1FFFF37E64FC846BAF263E27D5D6BE25377F7F1D6DB987433353545CC
			864F870D77920B198C9CC12B8C4E2670780A20D58657D71B7BD7852DB260D37AFF92344AE0C715D9863DA4EE203FEF130FF0296FEDCC950116016EFE57494EF63B5B59595FE737F734773B5B1D6E5E6E6E2E6EAEBA7FBB4A6B334C6F696BBFF92E2F5715FE85A61367A20CC795860F7AA2D69EBD1722E78BA2D69EF526745F963172689FD25E7D7CB8E265912F4B006A544D31D739F93E9727E6C6BFD3CD1455D076AFE1777417AA29BBC0DB8648DE426E7579983B570500DBEF0CAFA0787A9C30D1G47F218CEAD
			1BE47CF265B0FE2D1E1C7DDE7FECF276FB3DBFEE65171A22D33DF1AB3F7E364A4CAF3F7C7B74F73093CD9DFEACC0E917932CDE141B4362102631CB2B8B354CFB9E4D016FA17632B447DA9BD8CF707767D8EB83678D3BDB8EFDA7169A6F2D79796ECF6BB693927A519B17329ACDA2006FC1B255E8BA95C124EFD558397FCD306E3B9A56AADBCAED52C6F8D6AECD0D4D683EC544AE54AD83FD82B88660825016B1BA5686667CEE35BBB14BF72BA3FEABDDF930F651B2CA4B9AF85E3FAC5D3B79170317A67F17A217B6
			EFE79467D5ACCB9F3FEE85FC7D1569EACA1206FA50F425D5EF2284FF68CA2B5E7CC6E5FAF3947A8A2FE4FA630DDD9A5D93731EC7695BBDC96A3139D63A1FA6787ADCAB5DAB8DFB348F7AAE66B23A17CFDEA25D09CC73B6425A1539B365C067FDFB0CB3FEBFE87A6D9D3B7C396E454B3A873DEFB18963CCD53F5BB40F7BB5F947D5F7D501ED7FD5AC376B90C99DG5AA78C5C4DA24B776C86EE1D0F55D1A2D731BBDD924CDC9B3AA3183E3E7525E07A7A56D70333FFF3FBCFB0D37D0B52277A5959328C1A6F703333
			659938FD81F63615866ECD091D2D5440B5919F3B9E5A1B8C5CF8144D5DEA607EC6C55C7E2B4166DD4DF4D951127A14B07D2E52AE3FEE7D6E2F30C763E472CC4F18FE5F943B7CFBFF79326EC1E51AC362888748D33E1A4979BB89A67B2C2BD93E55C06EC9A8AC02C3133CB42FFC1960EE86385198F7D6C41F2C41E5205FC89D4757EC7AF7D6F709AC2FDB85635781EC8228DBDEE0EB862039714FFDB83F875AFA5ECB74FAA900GB83A66D8A83966AB813626867DB9388E40BA00CD461A9E92A754DD43E8FCA30AF1
			D3887DB147F2725EA80EF9719A1603DE91E56B1EG1C8BC0834883D8E52CF30B0A5F04C61538D354BF01ED17E5A5AE8AFA5E7DF51545176B0DB9FF1FC0BB926293D2A8966A476F3AE2E1CD9F3F1A709DF326CD869DBFCBB0BA36822EFE85443AGF6GF985DB73ED911FAFBBDF56E3438A116D1B3B4278064BE70DEB7F4C25FC43651BBD2E1D74D9635A87D27A0BFC563876A0E74E15F67871BDBC642B26143D79001EBB79897DDD0C7E77830C7ED2037E050159693FBDE0257FC2CA7F3F87E6277F37526C74A724
			4C748BD11C939663CD2E5374D36A6683ACFFA90A32B8FD1BF1160DD16BD9D62464DCFBD56BD92AD2E525DA4F12BAFFD2351E25AA65ACBFD0916763E5B5791D44FD899C036F52A95D8DD3CC86C72EE5F43FBDE5257B15943AA2D3D63A5FCC693FE14ACA7769145CE87D14156EA774797A77ED622545CD678217B66F1628F91E8CFC483A766354EA1F1C1A6FFE546E16C659FBAA14277E6EA74A5ED3A10E7EFF535BFB7F8F1E457F4155EA5CB3280ACD74E37EA67A6DCB937ED1BD54385DCD34B48A31099712A10D5B
			512457C4E94D6640C7663A5156C59D993F994D3D0E1EFD65F556EF406B487FF1A8877C566BD83CB464407B19699BBDE70817E50D17475B4382FE2F57B125094978E85799DCDFE5E374CE5A57F886698E1A667CBFD0CB8788C4739ECFFA94GG60BBGGD0CB818294G94G88G88GBBF117B1C4739ECFFA94GG60BBGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG3494GGGG
		**end of data**/
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
				ivjButtonPanel1.setBounds(126, 351, 304, 44);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				// defect 8240
				// remove KeyListeners from ButtonPanel
				// ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				// ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				// ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 8240
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
	 * Return the JComboBox1 property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboRSPSIdName()
	{
		if (ivjcomboRSPSIdName == null)
		{
			try
			{
				ivjcomboRSPSIdName = new JComboBox();
				ivjcomboRSPSIdName.setName("comboRSPSIdName");
				ivjcomboRSPSIdName.setMaximumRowCount(5);
				ivjcomboRSPSIdName.setBounds(9, 28, 294, 23);
				// user code begin {1}
				//defect 7662
				//ivjcomboRSPSIdName.setNextFocusableComponent(
				//	getButtonPanel1().getBtnEnter());
				ivjcomboRSPSIdName.addItemListener(this);
				//end defect 7662
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboRSPSIdName;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanelRadio()
	{
		if (ivjJPanelRadio == null)
		{
			try
			{
				ivjJPanelRadio = new JPanel();
				ivjJPanelRadio.setName("JPanelRadio");
				ivjJPanelRadio.setLayout(null);
				ivjJPanelRadio.setBounds(46, 18, 143, 102);
				getJPanelRadio().add(
					getradioSubcon(),
					getradioSubcon().getName());
				getJPanelRadio().add(
					getradioDealer(),
					getradioDealer().getName());
				// user code begin {1}
				//ivjJPanelRadio.setNextFocusableComponent(
				//	getcomboRSPSIdName());
				Border laBrdr =
					new TitledBorder(
						new EtchedBorder(),
						ENTITY_SELECTION);
				ivjJPanelRadio.setBorder(laBrdr);
				// defect 7891
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laRadioGrpEntity = new RTSButtonGroup();
				laRadioGrpEntity.add(getradioSubcon());
				laRadioGrpEntity.add(getradioDealer());
				// end defect 7891
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanelRadio;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanelRSPSIdName()
	{
		if (ivjJPanelRSPSIdName == null)
		{
			try
			{
				ivjJPanelRSPSIdName = new JPanel();
				ivjJPanelRSPSIdName.setName("JPanelRSPSIdName");
				ivjJPanelRSPSIdName.setLayout(null);
				ivjJPanelRSPSIdName.setBounds(198, 18, 313, 102);
				getJPanelRSPSIdName().add(
					getcomboRSPSIdName(),
					getcomboRSPSIdName().getName());
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
		return ivjJPanelRSPSIdName;
	}
	/**
	 * Return the radioDealer property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioDealer()
	{
		if (ivjradioDealer == null)
		{
			try
			{
				ivjradioDealer = new JRadioButton();
				ivjradioDealer.setName("radioDealer");
				ivjradioDealer.setMnemonic('D');
				ivjradioDealer.setText(DEALER);
				ivjradioDealer.setBounds(10, 60, 124, 22);
				// user code begin {1}
				// defect 7891
				//ivjradioDealer.addKeyListener(this);
				// end defect 7891
				ivjradioDealer.addActionListener(this);
				ivjradioDealer.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDealer;
	}

	/**
	 * Return the radioSubcon property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSubcon()
	{
		if (ivjradioSubcon == null)
		{
			try
			{
				ivjradioSubcon = new JRadioButton();
				ivjradioSubcon.setName("radioSubcon");
				ivjradioSubcon.setMnemonic('S');
				ivjradioSubcon.setText(SUBCONTRACTOR);
				ivjradioSubcon.setBounds(10, 26, 124, 22);
				// user code begin {1}
				// defect 7891
				//ivjradioSubcon.addKeyListener(this);
				// end defect 7891
				ivjradioSubcon.addActionListener(this);
				ivjradioSubcon.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSubcon;
	}
	/**
	 * This method initializes refreshProgressBarPanel
	 * 
	 * @return RefreshProgressBarPanel
	 */
	private RefreshProgressBarPanel getRefreshProgressBarPanel()
	{
		if (refreshProgressBarPanel == null)
		{
			refreshProgressBarPanel = new RefreshProgressBarPanel();
			refreshProgressBarPanel.setBounds(98, 297, 360, 40);
			refreshProgressBarPanel.setVisible(false);
		}
		return refreshProgressBarPanel;
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
				ivjRTSDialogBoxContentPane.setLayout(null);
				ivjRTSDialogBoxContentPane.add(getJPanelRadio(), null);
				ivjRTSDialogBoxContentPane.add(getButtonPanel1(), null);
				ivjRTSDialogBoxContentPane.add(
					gettblRspsUpdate(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getJPanelRSPSIdName(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getRefreshProgressBarPanel(),
					null);
			}
			catch (Throwable aeIVJjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJjEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				gettblRspsUpdate().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				gettblRspsUpdate().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjScrollPaneTable.setModel(new TMRSP001());
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				ivjScrollPaneTable.setRowSelectionAllowed(false);
				// user code begin {1}
				caTblModel = (TMRSP001) ivjScrollPaneTable.getModel();
				TableColumn ltblColumnA =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				ltblColumnA.setPreferredWidth(50);
				TableColumn ltblColumnB =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				ltblColumnB.setPreferredWidth(50);
				//defect 7809
				//ivjScrollPaneTable.setSelectionMode(
				//		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				// defect 7809 
				ivjScrollPaneTable.init();
				ltblColumnA.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				ltblColumnB.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				ivjScrollPaneTable.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the tblRspsUpdate property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane gettblRspsUpdate()
	{
		if (ivjtblRspsUpdate == null)
		{
			try
			{
				ivjtblRspsUpdate = new JScrollPane();
				ivjtblRspsUpdate.setName("tblRspsUpdate");
				ivjtblRspsUpdate.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjtblRspsUpdate.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjtblRspsUpdate.setBounds(44, 138, 468, 144);
				gettblRspsUpdate().setViewportView(
					getScrollPaneTable());
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
		return ivjtblRspsUpdate;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		///* Uncomment the following lines to print uncaught exceptions
		// * to stdout */
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//aeEx.printStackTrace(System.out);
		RTSException leRTSE =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSE.writeExceptionToLog();
		leRTSE.displayError(this);
		//end defect 7891
	}

	/**
	 * This method handles show the number of updates
	 * available to apply and kicks off the
	 * refresh process for the flash drive.
	 *
	 * @param avExcludeList Vector
	 */
	private void handleRefresh(Vector avExcludeList)
	{
		try
		{
			// defect 8190
			// Removed the logic that counted and diaplayed the number 
			// of system updates that will be sent to the flash drive. 
			// Replaced it with a message that is the same all the time.
			//  See the message below.

			//int liUpdatesCount = 0;

			//liUpdatesCount =
			//	RSPSSysUpdateCache.getUpdatesList().size()
			//		- avExcludeList.size();

			//RTSException leRTSException =
			//	new RTSException(RTSException.INFORMATION_MESSAGE);
			//leRTSException.setTitle("RSPS UPDATE STATUS");

			//defect 7660
			//		lRTSException.setMessage(
			//	"There are "
			//		+ String.valueOf(liUpdatesCount)
			//		+ " System Updates available to apply.");

			//if(liUpdatesCount == 0)
			//{
			//	laRTSException.setMessage("There are no updates to " 
			//				+ "send to the Flash Drive.");		
			//}
			//else if(liUpdatesCount == 1)
			//{
			//	laRTSException.setMessage("There is " 
			//				+ String.valueOf(liUpdatesCount)
			//				+ " update to send to the Flash Drive.");
			//}
			//else
			//{
			//	laRTSException.setMessage("There are " 
			//				+ String.valueOf(liUpdatesCount)
			//				+ " updates to send to the Flash Drive.");	
			//}
			//end defect 7660

			RTSException leRTSException =
				new RTSException(
					RTSException.INFORMATION_MESSAGE,
					SEND_FLASH_DRIVE,
					RSPS_UPDATE_STATUS);
			leRTSException.displayError(this);

			// start a thread to do the refresh 
			FlashDriveRefreshHelper laFDRH =
				new FlashDriveRefreshHelper(avExcludeList, this);
			Thread laThread = new Thread(laFDRH, FLASH_REFRESH);
			laThread.start();

			// Removed checking for the thread to be done.  Let the 
			// thread tell us when it is done.
			//while (laThread.isAlive())
			//{
			//	System.out.println("waiting on refresh");
			//	wait(1000);
			//}
			// end defect 8190
		}
		catch (IllegalMonitorStateException aeIMSEx)
		{
			aeIMSEx = null;
		}
		// defect 8190
		// not needed since the above code is commented out
		//catch (InterruptedException leIEx)
		//{
		//	leIEx = null;
		//}
		// end defect 8190
		catch (Exception aeEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			laRTSE.displayError(this);
		}
	}

	/**
	 * This is the method that is called when the refresh thread is
	 * done.  This is a public method so that the thread can call it.
	 */
	public void handleReturnFromRefresh()
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.INFORMATION_MESSAGE,
				COMPLETE_PROCESS,
				RSPS_UPDATE_STATUS);
		leRTSEx.displayError(this);
		// defect 8380
		// Update the LaptopList when you come back from refreshing
		if (getradioSubcon().isSelected())
		{
			updateLaptopList(CommonConstant.POS_SUB);
		}
		else
		{
			updateLaptopList(CommonConstant.POS_DTA);
		}
		// end defect 8380
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
			setName("FrmRSPSStatusUpdateRSP001");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(565, 429);
			setTitle(RSP001_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 *
	 * @param aaIE ItemEvent 
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		//defect 7662
		//if(aaItemEvent.getSource() == getcomboRSPSId())
		if (aaIE.getSource() == getcomboRSPSIdName())
		{
			// defect 7891
			if (ciRSPSIdInfoIndx
				!= getcomboRSPSIdName().getSelectedIndex())
			{
				//ciRSPSIdInfoIndx = getcomboRSPSId().getSelectedIndex();
				ciRSPSIdInfoIndx =
					getcomboRSPSIdName().getSelectedIndex();
				//if ( ciRSPSIdInfoIndx > -1 && 
				//ciRSPSIdInfoIndx < getcomboRSPSName().getItemCount())
				if (ciRSPSIdInfoIndx > -1
					&& ciRSPSIdInfoIndx
						< getcomboRSPSIdName().getItemCount())
				{
					//getcomboRSPSName().setSelectedIndex(ciRSPSIdInfoIndx);
					getcomboRSPSIdName().setSelectedIndex(
						ciRSPSIdInfoIndx);
				}
			}
		}
		//else
		//{
		//	setcomboRSPSIdInfo();
		//}
		// end defect 7891

		//else if (aaItemEventgetSource() == getcomboRSPSName())
		//	{
		//		ciRSPSIdInfoIndx = getcomboRSPSName().getSelectedIndex();
		//		if ( ciRSPSIdInfoIndx > -1 && 
		//		ciRSPSIdInfoIndx < getcomboRSPSId().getItemCount())
		//		{
		//			getcomboRSPSId().setSelectedIndex(ciRSPSIdInfoIndx);
		//		}	
		//	}
		//end defect 7662
		//	else if (aaItemEventgetSource() == getradioDealer()
		//		  || aaItemEventgetSource() == getradioSubcon())
		//	{
		//		setcomboRSPSIdInfo();
		//		if (getcomboRSPSId().getItemCount() > 0)
		//		{
		//			getcomboRSPSId().setSelectedIndex(0); 
		//		}
		//	}

		String lsLocIdCd = CommonConstant.POS_DTA;
		if (getradioSubcon().isSelected())
		{
			lsLocIdCd = CommonConstant.POS_SUB;
			//defect 7664
			Border laBorder =
				new TitledBorder(new EtchedBorder(), SUBCON_ID);
			getJPanelRSPSIdName().setBorder(laBorder);
			//	 getJPanelRSPSId().setBorder(b);
			//Border b2 = new TitledBorder(
			//			new EtchedBorder(), " Subcontractor Name:");
			//	 getJPanelRSPSName().setBorder(b2);
		}
		else
		{
			Border laBorder =
				new TitledBorder(new EtchedBorder(), DEALER_ID_NAME);
			getJPanelRSPSIdName().setBorder(laBorder);
			//	 getJPanelRSPSId().setBorder(b);
			//	 Border b2 = new TitledBorder(new EtchedBorder(), 
			//		" Dealer Name:");
			//	 getJPanelRSPSName().setBorder(b2);	
		}
		//end defect 7664

		// defect 8380
		// Moved code to separate method so it could be called after
		// we come back from refresh.
		updateLaptopList(lsLocIdCd);
		// end defect 8380
	}

//	/**
//	 * Key Pressed
//	 * 
//	 * @param aaKE KeyEvent
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			if (aaKE.getSource() == getradioDealer()
//				|| aaKE.getSource() == getradioSubcon())
//			{
//				if (aaKE.getKeyCode() == KeyEvent.VK_UP
//					|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
//					|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//				{
//					if (getradioDealer().isSelected()
//						&& getradioSubcon().isEnabled())
//					{
//						getradioSubcon().setSelected(true);
//						getradioDealer().setSelected(false);
//						getradioSubcon().requestFocus();
//					}
//					else if (
//						getradioSubcon().isSelected()
//							&& getradioDealer().isEnabled())
//					{
//						getradioDealer().setSelected(true);
//						getradioSubcon().setSelected(false);
//						getradioDealer().requestFocus();
//					}
//				}
//			}
//		}
//		// defect 8240
//		// arrows now handled in ButtonPanel
//		//		else if (aaKE.getSource() instanceof RTSButton)
//		//		{
//		//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//		//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//		//			{
//		//				if (getButtonPanel1().getBtnEnter().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnCancel().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnCancel().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnHelp().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnEnter().requestFocus();
//		//				}
//		//				aaKE.consume();
//		//			}
//		//			else if (
//		//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
//		//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
//		//			{
//		//				if (getButtonPanel1().getBtnCancel().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnEnter().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnCancel().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnEnter().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnHelp().requestFocus();
//		//				}
//		//				aaKE.consume();
//		//			}
//		//		}
//		// end defect 8240
//	}

	/**
	 * Starts the application.
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRSPSStatusUpdateRSP001 laFrmRSPSStatusUpdateRSP001;
			laFrmRSPSStatusUpdateRSP001 =
				new FrmRSPSStatusUpdateRSP001();
			laFrmRSPSStatusUpdateRSP001.setModal(true);
			laFrmRSPSStatusUpdateRSP001
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmRSPSStatusUpdateRSP001.show();
			Insets insets = laFrmRSPSStatusUpdateRSP001.getInsets();
			laFrmRSPSStatusUpdateRSP001.setSize(
				laFrmRSPSStatusUpdateRSP001.getWidth()
					+ insets.left
					+ insets.right,
				laFrmRSPSStatusUpdateRSP001.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmRSPSStatusUpdateRSP001.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeIVJEx.printStackTrace(System.out);
		}
	}
	/**
	 * Initialize the destination combo box.
	 */
	private void setcomboRSPSIdInfo()
	{
		//defect 7662
		getcomboRSPSIdName().removeItemListener(this);
		//end defect 7662
		String lsLocIdCd = CommonConstant.POS_DTA;
		if (getradioSubcon().isSelected())
		{
			lsLocIdCd = CommonConstant.POS_SUB;
		}
		if (cvRSPSIdInfo != null)
		{
			//defect 7662
			//getcomboRSPSId().removeAllItems();
			//getcomboRSPSName().removeAllItems();
			getcomboRSPSIdName().removeAllItems();
			for (int i = 0; i < cvRSPSIdInfo.size(); i++)
			{
				RSPSIdsData laRSPSIdsData =
					(RSPSIdsData) cvRSPSIdInfo.elementAt(i);
				if (lsLocIdCd
					.equalsIgnoreCase(laRSPSIdsData.getIdType()))
				{
					//getcomboRSPSId().addItem(
					//			String.valueOf(lRSPSIdsData.getId()));
					//getcomboRSPSName().addItem(
					//			lRSPSIdsData.getIdName());
					getcomboRSPSIdName().addItem(
						UtilityMethods.addPadding(
							String.valueOf(laRSPSIdsData.getId()),
							3,
							CommonConstant.STR_ZERO)
							+ CommonConstant.STR_DASH
							+ laRSPSIdsData.getIdName());
				}
			}
			//end defect 7662
			// defect 8479
			comboBoxHotKeyFix(getcomboRSPSIdName());
			// end defect 8479
		}
		//defect 7662
		getcomboRSPSIdName().addItemListener(this);
		//end defect 7662
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject == null)
		{
			handleRefresh(new Vector());
			// defect 8190
			// Let setdata fall out while the refresh thread is 
			// processing.  The thread will display the msg once it is
			// done.
			//			RTSException leRTSEx =
			//				new RTSException(
			//					RTSException.INFORMATION_MESSAGE);
			//			leRTSEx.setTitle("RSPS UPDATE STATUS");
			//			leRTSEx.setMessage(
			//				"RSPS update status has been submitted.");
			//			leRTSEx.displayError(this);
			// end defect 8190
		}
		else if (aaDataObject instanceof Vector)
		{
			cvRSPSIdInfo = new Vector();
			getradioSubcon().setEnabled(false);
			getradioDealer().setEnabled(false);
			Vector lvMultiVector = (Vector) aaDataObject;
			Vector lvSubcon = (Vector) lvMultiVector.elementAt(0);
			Vector lvDealer = (Vector) lvMultiVector.elementAt(1);
			if ((lvSubcon != null && lvSubcon.size() > 0)
				&& (lvDealer != null && lvDealer.size() > 0))
			{
				getradioSubcon().setEnabled(true);
				getradioDealer().setEnabled(true);
				cvRSPSIdInfo.addAll(lvSubcon);
				cvRSPSIdInfo.addAll(lvDealer);
				getradioSubcon().setSelected(true);
			}
			else if (lvDealer != null && lvDealer.size() > 0)
			{
				getradioDealer().setEnabled(true);
				getradioSubcon().setEnabled(false);
				cvRSPSIdInfo.addAll(lvDealer);
				getradioDealer().setSelected(true);
			}
			else if (lvSubcon != null && lvSubcon.size() > 0)
			{
				getradioSubcon().setEnabled(true);
				getradioDealer().setEnabled(false);
				cvRSPSIdInfo.addAll(lvSubcon);
				getradioSubcon().setSelected(true);
			}
			setcomboRSPSIdInfo();
		}
		else if (aaDataObject instanceof RSPSUpdData)
		{
			if (((RSPSUpdData) aaDataObject).getSysUpdates() != null)
			{
				handleRefresh(
					((RSPSUpdData) aaDataObject).getSysUpdates());
			}
			// defect 8190
			// Let setdata fall out while the refresh thread is 
			// processing.  The thread will display the msg once it is
			// done.
			//			RTSException leRTSEx =
			//				new RTSException(
			//						RTSException.INFORMATION_MESSAGE);
			//			leRTSEx.setTitle("RSPS UPDATE STATUS");
			//			//defect 7665
			//			//lRTSException.setMessage(
			//			//	"RSPS update status has been submitted.");
			//			leRTSEx.setMessage(
			//				"Flash Drive processing is complete. " 
			// 					+ "You may remove "
			//					+ "the Flash Drive.");
			//			//end defect 7665
			//			leRTSEx.displayError(this);
			// end defect 8190
		}
	}
	/**
	 * Used to update the list of Laptop's and the last time they where
	 * updated.
	 * 
	 * @param asLocCd String
	 */
	private void updateLaptopList(String asLocCd)
	{
		int liLocId = 1;
		try
		{
			//defect 7662
			//liLocId = Integer.parseInt(
			//(String)getcomboRSPSId().getSelectedItem());
			if (getcomboRSPSIdName().getSelectedIndex() > -1)
			{
				liLocId =
					Integer.parseInt(
						(
							(String) getcomboRSPSIdName()
								.getSelectedItem())
								.substring(
							0,
							3));
			}
			//end defect 7662
		}
		catch (NumberFormatException aeNFEx)
		{
			// do nothing, it is already initialized
		}
		Vector lvRSPSWsStatus = new Vector();
		try
		{
			lvRSPSWsStatus =
				RSPSWsStatusCache.getRSPSWsList(
					SystemProperty.getOfficeIssuanceNo(),
					asLocCd,
					liLocId);
			// defect 7801
			// Present RSPSIds in Alpha Order 
			UtilityMethods.sort(lvRSPSWsStatus);
			// end defect 7801 
		}
		catch (RTSException aeRTSEx)
		{
			// empty code block
		}
		caTblModel.add(lvRSPSWsStatus);
	}
}