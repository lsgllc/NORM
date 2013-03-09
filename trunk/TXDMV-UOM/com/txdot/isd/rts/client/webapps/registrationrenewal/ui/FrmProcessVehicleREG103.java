package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;
import com.txdot.isd.rts.client.webapps.registrationrenewal.RegRenProcessingClientBusiness;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
import java.awt.Rectangle;

/*
 *
 * FrmProcessVehicleREG103.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		06/17/2002	Rebuilt visual composition editor view by 
 *                          going into VCE, bean, construct visuals from
 *                          source, resized the visual correctly, then
 *                          bean, regenerate the code (getBuilderData
 *                          method). Because this process can add
 *                          undesirable code, and the getBuilderData 
 *                          method is what is needed for VCE, I saved 
 *                          the getBuilderData method to an external 
 *                          file, checked out the most recent version
 *                          of the class from ClearCase, and re-copied
 *                          in the getBuilderData method and saved it 
 *                          in the class.
 * Clifford 	07/11/2002	Address CQU100004301. rts572 message pop-up
 * Clifford 	09/03/2002	CQU100003700. DB down handling.
 * Clifford 	09/25/2002	CQU100004681. Escape, modified keyPressed(),
 *                          refreshData(), added undoCheckout(), 
 *                          viewOnly().
 * Clifford		10/09/2002	PCR44, no insurance required, modified 
 *                          refreshData().
 * B Brown 		10/23/2002  CQU100004205.(putting and accessing Phase 2
 *                          error msgs in rts_err_msgs). 
 *                          Changed methods: doProcessData, processData,
 *                          actionPerformed, getData, getValidRecord, 
 *                          gotoNext, setData, getNextRecordDlg,
 *                          usrAgreeSwitch, undoCheckout, and display
 *                          Message to switch over to database error
 *                          messages from hard coded messages.
 * B Brown 		11/25/2002  CQU100004542.(adding hot keys). 
 *                          Changed methods: getchkboxSendEmail - added 
 *                          a setMnemonic statement	getJlabel10 - added
 *                          a setDisplayedMnemonic.	SetButtonGroup - 
 *                          added setLabelFor statements to activate hot    
 *                          keys if Hold or Declines button selected.
 * Clifford 	01/21/2003	CQU100004542 related, when Hold/Decline is 
 *                          selected, place focus on the corresponding
 *                          combo box.
 * B Brown  	01/31/2003  CQU00005333 Changed method setData to look 
 *                          at the parent UI class, RTSDialogBox rd = 
 *                          controller.getMediator().getParent();if any
 *                          parent,and the desktop RTSDeskTop rdDesk
 *                          Top = controller.getMediator().getDesktop();
 *                          if no parent, when there are no internet 
 *                          registration records to process.
 * B Brown  	05/01/2003  CQU00006051 Changed method refreshData, 
 *                          added getbtnInsurance().setEnabled(true);
 *                          to make sure the insurance button is enabled 
 *                          when insurance data is present.
 * B Brown  	12/29/2003  CQU00006099 Changed methods acceptData, 
 *                          getData, getNextRec, goToNext, and setData 
 *                          for improved exception and object handling.
 *                          Defect 6099.Version 5.1.5 fix 2
 * B Brown	   	05/17/2004	During county processing, the last "hold" 
 *                          record being processed, if left in hold, 
 *                          was coming out "in process"
 *							modified method getData.
 *                          modified method getValidRecord. 
 *							defect 6380 Ver 5.2.0
 * K Harrell	06/21/2004	Incorrect RegExpYr printed on sticker.
 *							Also modified to not go to server if only
 *							printed inventory. 
 *							modify selectInventory()
 *							defect 7221 Ver 5.2.0
 * B Brown	   	12/01/2004	During county processing, when the last new
 *							record has been processed, execute the beep
 *                          so the user is alerted.
 *                          Modify method cbUserAgreeSwitch().
 *							defect 6531 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		02/28/2005	Get code to standard. Changed a non-static
 * 							call to a static call. Fixed setVisible and 
 * 							setFocusable for Java 1.4.
 * 							modify closeDlg(), handleException(), 
 * 								keyPressed(), selectInventory(), main(),
 * 								refreshData(), gettxtAreaComments()
 *							defect 7889 Ver 5.2.3
 * Jeff S.		06/17/2005	Removed setNextFocusableCom and made sure 
 * 							that all labels fit within the panel. Had to
 * 							move the actions and comments into separate
 * 							panels so that tab order was correct.  Also
 * 							removed inner class in gettxtAreaComments()
 * 							and changed it to use a document that we
 * 							currently have to manage max characters.
 * 							Added handling for up,down,left,right on the
 * 							radio groups and buttons. Renamed class to
 * 							have the frame number in it.
 * 							modify ActionPerformed(), closeDlg(), 
 * 							getBtnContinue(), getJDialogContentPane(), 
 * 							gettxtAreaComments(), keyPressed(), 
 * 							processData()
 * 							deprecate displayMessage( , , )
 *							defect 7889 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3  
 * Jeff S.		06/20/2005	Added the ability to accept a hashtable that
 * 							contains both the current record and the 
 * 							search keys that where used.  This change is
 * 							only for when in search mode.
 * 							modify setData(), closeDlg()
 * 							add chtSearchParams
 * 							defect 8247 Ver 5.2.3
 * Jeff S.		06/20/2005	When you leave this frame and come back the 
 * 							focus needs to be on the insurance button.
 * 							modify setData(), actionPerformed()
 * 							defect 7080 Ver 5.2.3  
 * Jeff S.		08/16/2005	Used new screen constants that match the 
 * 							frame number.
 * 							modify selectInventory()
 * 							defect 7889 Ver 5.2.3   
 * B Brown		09/23/2005	Give the county a better error message when
 * 							the rts_itrnt_data record is missing, and 
 * 							allow processing to continue. 
 * 							add PLTNO	
 * 							delete initializeNext(HashTable),
 * 								   checkOutVehicle(InternetRegRecData),
 * 								   displayMessage(String,String,String)							
 * 							modify acceptData(),actionPerformed(),
 * 								   getNextRecord(),getNextRecordDlg(),
 * 								   getValidRecord(),goToNext(),setData()		
 * 							defect 8245 Ver 5.2.3   
 * Jeff S.		01/05/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Added Insurance and
 * 							address btns to an RTSButtonGroup so that
 * 							it could handle the arrowing through these
 * 							buttons.
 * 							remove keyPressed()
 * 							modify setButtonGroup(), getrbtnApprove(),
 * 								getrbtnDecline(), getJpnlDetail(), 
 * 								getJDialogContentPane() 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	04/25/2006	No data returning on request to Process Hold.
 * 							New constants to verify mode processing.
 * 							renamed all arguments for Exceptions.
 * 							add PROCESS_HOLD,PROCESS_NEW,PROCESS_SEARCH
 * 							modify setMode()
 * 							defect 8736 Ver 5.2.3
 * K Harrell	04/25/2006	Do not reset data upon return from 
 * 							View/Change Address
 * 							modify refreshData() 
 * 							defect 8359 Ver 5.2.3
 * K Harrell	04/26/2006	Red Field for Decline not entirely red on
 * 							second error when no Action Command specified. 
 * 							modify processData()
 * 							defect 7125 Ver 5.2.3  
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify selectInventory()
 *							defect 8901 Ver Exempts
 * K Harrell	02/05/2007 Use PlateTypeCache vs. 
 * 									RegistrationRenewalsCache
 *							modify selectInventory(),getAnnualPltIndi() 
 * 							defect 9085 Ver Special Plates
 * Min Wang		06/18/2004	modify fields and screen.
 * 							defect 8768 Ver Special Plates
 * K Harrell	08/07/2007  Modified for special plate plate age  
 * B Brown					modify selectInventory()
 * 							defect 9119 Ver Special Plates
 * B Brown		01/31/2008	Get the exp mo/yr from the 
 * 							caTransData RegFeesData object
 * 							Also, add a NeedsProgramCd check to not 
 * 							prompt for inventory at appropriate times.
 * 							modify refreshData(), selectInventory()
 * 							defect 9541 Ver Tres Amigos Prep 
 * B Brown		02/19/2008	Check for caTransData not equal to null 
 * 							before getting data from it. This affected 
 * 							the processing of unpaid records.			
 * 							modify refreshData()
 * 							defect 9565 Ver Special Plates
 * B Brown		02/20/2008	Use caTransData to get the exp mo/yr only 
 * 							if caTransData is not null. If it is null
 * 							get the exp mo/yr the old way - from
 * 							caInetData.
 * 							defect 9565 Ver Special Plates
 * B Brown		09/09/2008	Check laPlateTypeData.getAnnualPltIndi to 
 * 							determine if the currecnt plate becomes
 * 							the previous plate on the receipt
 * 							modify selectInventory()  
 * 							defect 9638 Ver Defect_POS_B
 * B Brown		09/25/2008	Look at the ins policy info in next record 
 * 							coming in to be processed to determine
 * 							where to put cursor focus: on the insurance
 * 							button if there is ins data present, and on
 * 							the address button if no insurance data.
 * 							modify refreshData()
 * 							defect 9034 Ver Defect_POS_B
 * B Brown		10/28/2008	Come back to checking isInsuranceRequired 
 * 							to determine whether to set focus on the
 * 							insurance or address button.
 * 							modify refreshData(), setData(), 
 * 							actionPerformed() 
 * 							defect 9034 Ver Defect_POS_B
 * B Brown		01/08/2009	Undid these changes:
 * 							Check to see if the trace number being sent
 * 							to Epay for a decline has 3 characters after
 * 							the plate number. If it does, we strip off 
 * 							the last byte (credit card type), then send
 * 							to TxO and Epay.
 * 							modify processData().
 * 							defect 9878 Ver Defect_POS_C
 * K Harrell	05/29/2009	Use ErrorsConstant as Exception Header when
 * 							data from ITRNT_DATA not found. Sort members.
 * 							Replaced error numbers with ErrorsConstant 
 * 							constants.  
 * 							delete RECORD_MISSING_TITLE, RECORD_MISSING,
 * 							 PLTNO
 * 							modify actionPerformed(),
 * 							 doProcessData(), getData(), getValidRecord(),
 * 							 goToNext(), processData(), setData(),
 *							 undoCheckout(),userAgreeSwitch()
 * 							defect 8749 Ver Defect_POS_F
 * K Harrell	03/25/2010	Add Recipient Address via Visual Comp.
 * 							Sorted members. 
 * 							add  ivjstcLblAddress, ivjlblAddress, 
 * 							 get methods()
 * 							add RECIPIENT_ADDRESS 
 * 							modify getJpnlDetail(), refreshData(), 
 * 							 resetGUI() 
 * 							defect 10420 Ver POS_640 
 * K Harrell	07/10/2010	add logic to retain EMailRenwlCd 
 * 							modify selectInventory()
 * 							defect 10508 Ver 6.5.0 
 * K Harrell	09/23/2010	modify to present Pending Transactions where
 * 							applicable. 
 * 							add cvInProcsTrans
 * 							modify actionPerformed(), getValidRecord(),
 * 							 acceptData() 
 * 							defect 10598 Ver 6.6.0 
 * B Brown		09/29/2011	Get the actual new expiration year, instead
 * 							of just ading 1 to previous expiration year.
 * 							modify refreshData()
 * 							defect 10895 Ver 6.9.0
 * K Harrell	10/25/2011	add ivjbtnPreviewReceipt, get method
 * 							add getDataForPreviewReceipt(), 
 * 								setDataFromAddressChange() 
 * 							modify getJDialogContentPanel(),
 * 							   actionPerformed() 
 * 							defect 11127 Ver 6.9.0 
 * K Harrell	11/13/2011	do not refresh data from Address Change if 
 * 							view only. csPrevPlateNo is not used 
 * 							delete csPrevPlateNo 
 * 							modify actionPerformed(), goToNext()
 * 								setDataFromAddressChange(), refreshData() 
 * 							defect 11127 Ver 6.9.0 
 * K Harrell	11/18/2011	On Preview Receipt, assign Exp Yr to 
 * 							copy of CompleteTransactionData.  Make 
 * 							availability of Preview Receipt function 
 * 							of presence of CompleteTransactionData  
 * 							modify getDataForPreviewReceipt()
 * 							defect 11127 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * FrmProcessVehicleREG103 allows the county office to approve, decline 
 * for various reasons, or put on hold, internet registration renewals.
 *
 * @version	6.9.0			11/18/2011
 * @author	J. Giroux
 * @since 					10/09/2001 12:21:22
 */

public class FrmProcessVehicleREG103
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnAddress = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnContinue = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnInsurance = null;
	
	// defect 11127 
	private RTSButton ivjbtnPreviewReceipt = null;
	// end defect 11127 
	
	private JCheckBox ivjchkboxSendEmail = null;
	private JComboBox ivjcmbDecline = null;
	private JComboBox ivjcmbHold = null;
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjJLabel10 = null;
	private JLabel ivjJlblAppName = null;
	private JLabel ivjJlblArrows1 = null;
	private JLabel ivjJlblArrows2 = null;
	private JLabel ivjJlblCarryingCap = null;
	private JLabel ivjJlblDetailRec = null;
	private JLabel ivjJlblDocNo = null;
	private JLabel ivjJlblEmptyWeight = null;
	private JLabel ivjJlblGrossWeight = null;
	private JLabel ivjJlblIssuanceDate = null;
	private JLabel ivjJlblName = null;
	private JLabel ivjJlblNewExpDate = null;
	private JLabel ivjJlblPlateAge = null;
	private JLabel ivjJlblPlateNo = null;
	private JLabel ivjJlblStatus = null;
	private JLabel ivjJlblTonnage = null;
	private JLabel ivjJlblTraceNo = null;
	private JLabel ivjJlblTransId = null;
	private JLabel ivjJlblVin = null;
	private JPanel ivjJpnlActionCom = null;
	private JPanel ivjJpnlDetail = null;
	private JScrollPane ivjJScrlPnComments = null;
	private JLabel ivjlblAddress = null;
	private JLabel ivjlblApplicantName = null;
	private JLabel ivjlblCarryingCapacity = null;
	private JLabel ivjlblDocumentNo = null;
	private JLabel ivjlblEmptyWt = null;
	private JLabel ivjlblGrossWt = null;
	private JLabel ivjlblNameOnRecord = null;
	private JLabel ivjlblNewExpDate = null;
	private JLabel ivjlblPlateAge = null;
	private JLabel ivjlblPlateNo = null;
	private JLabel ivjlblStatus = null;
	private JLabel ivjlblTitleIssuanceDate = null;
	private JLabel ivjlblTonnage = null;
	private JLabel ivjlblTraceNo = null;
	private JLabel ivjlblTransID = null;
	private JLabel ivjlblVIN = null;
	private JRadioButton ivjrbtnApprove = null;
	private JRadioButton ivjrbtnDecline = null;
	private JRadioButton ivjrbtnHold = null;
	private JLabel ivjstcLblAddress = null;
	private JTextArea ivjtxtAreaComments = null;
	private JPanel jPnlAction = null;
	private JPanel jPnlComments = null;
	private RTSButtonGroup caBtnGroup = null;

	// boolean 
	// defect 7080
	// Added to tell frame when it has gone away to 
	// get the address data.  This is so when it returns
	// we know to set focus back to that button.
	private boolean cbGoingToAddress = false;
	// end defect 7080

	private boolean cbIsLastTransCached;
	private boolean cbUserAgreeSwitch = false;
	private boolean cbViewOnly = false;

	//defect 8247
	// Added so that we can re-search when going back to the
	// search results screen.
	private Hashtable chtSearchParams;

	// int 
	// ciProcessMode values, SEARCH, NEXT_NEW, NEXT_HOLD
	public int ciProcessMode = 0;

	// String
	// defect 11127 
	//private String csPrevPlateNo = "";
	// end defect 11127 
	private String csPrevSelection = "";

	// Object 
	private InternetRegRecData caInetData = null;
	private CompleteTransactionData caTransData = null;

	// defect 10598 
	private Vector cvInProcsTrans = null;
	private boolean cbShownPndng = false;
	// end defect 10598 

	// Constants
	private static final String ACTION = "Action:";
	private static final String ADDRESS = "Address";
	private static final String ADDUPDHASH = "AddressUpdateHash";
	private static final String APPROVE = "Approve";
	private static final String CNTYSTCD = "CntyStatusCd";
	private static final String CONTINUE = "Continue";
	private static final String COUNTY = "County";
	private static final String DECLINE = "Decline";
	private static final String HOLD = "Hold";
	private static final String INSURANCE = "Insurance";
	private static final String NEW_EXP_MO_YR = "New Exp Mo/Yr:";
	private static final int NEXT_HOLD = 2;
	private static final int NEXT_NEW = 1;
	private static final String OTHER = "Other";
	private static final String OWNER_NAME = "Owner Name:";
	private static final String PROCESS_HOLD = "HOLD";
	private static final String PROCESS_NEW = "NEW";
	private static final String PROCESS_SEARCH = "SEARCH";
	private static final String RECIPIENT_ADDRESS =
		"Recipient Address:";
	private static final String RECIPIENT_NAME = "Recipient Name:";
	private static final int SEARCH = 0;
	private static final String SND_EMAIL = "Send email";
	private static final String SVR_ERR = "SERVER ERROR: ";
	private static final String VC_ADDRESS = "View/Change Address";
	private static final String VIEW_INS = "View Insurance";
	
	

	
	/**
	 * This method initializes ivjbtnPreviewReceipt	
	 * 	
	 * @return RTSButton	
	 */
	private RTSButton getbtnPreviewReceipt()
	{
		if (ivjbtnPreviewReceipt == null)
		{
			ivjbtnPreviewReceipt = new RTSButton();
			ivjbtnPreviewReceipt.setBounds(new Rectangle(503, 442, 135, 25));
			ivjbtnPreviewReceipt.setText("Preview Receipt");
			ivjbtnPreviewReceipt.setMnemonic(KeyEvent.VK_R);
			ivjbtnPreviewReceipt.addActionListener(this);
		}
		return ivjbtnPreviewReceipt;
	}

	/**
	 * main entrypoint - starts the part when it is run as 
	 * an application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmProcessVehicleREG103 laFrmProcessVehicle;
			laFrmProcessVehicle = new FrmProcessVehicleREG103();
			laFrmProcessVehicle.setModal(true);
			laFrmProcessVehicle
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					//System.exit(0);
				};
			});
			laFrmProcessVehicle.show();
			java.awt.Insets insets = laFrmProcessVehicle.getInsets();
			laFrmProcessVehicle.setSize(
				laFrmProcessVehicle.getWidth()
					+ insets.left
					+ insets.right,
				laFrmProcessVehicle.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmProcessVehicle.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of JDialog");
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**   
	 * FrmProcessVehicleREG103 constructor comment.
	 */
	public FrmProcessVehicleREG103()
	{
		super();
		initialize();
	}

	/**
	 * 
	 * FrmProcessVehicleREG103 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmProcessVehicleREG103(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmProcessVehicleREG103 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmProcessVehicleREG103(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
		setMode(aaOwner.getTitle());
	}

	/**
	 * Accept data coming from FrmSearchResultsREG102 for processing.
	 * Called from method setData.
	 * 
	 * @param avData Vector
	 * @return Object
	 */
	private Object acceptData(Vector avData)
	{
		caInetData = (InternetRegRecData) avData.elementAt(0);
		caTransData = (CompleteTransactionData) avData.elementAt(1);
		
		// defect 10598 
		if (avData.size() > 2)
		{
			cvInProcsTrans = (Vector) avData.elementAt(2);
		}
		// end defect 10598  

		return caInetData;
	}

	/**
	 * This method handles action events from components that have
	 * an actionlistener.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		RTSException leRTSEx = new RTSException();

		//Code to avoid clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{
			//  defect 10590  
			if (aaAE.getSource() == getbtnPreviewReceipt())
			{
				Vector lvData = getDataForPreviewReceipt();

				getController().processData(
					VCProcessVehicleREG103.PREVIEWRECEIPT,
					lvData);
			}
			else
				if (aaAE.getSource() == getBtnInsurance())
			{
				if (caInetData == null)
				{
					leRTSEx.addException(
					//new RTSException(986),
					new RTSException(
						ErrorsConstant.ERR_NUM_NULL_INTERNET_DATA),
						leRTSEx.getFirstComponent());

					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				getController().processData(
					RegRenProcessingConstants.VIEW_INS,
					caInetData
						.getCompleteRegRenData()
						.getVehInsuranceData());
			}
			else if (aaAE.getSource() == getBtnAddress())
			{
				if (caInetData == null)
				{
					leRTSEx.addException(
					//new RTSException(986),
					new RTSException(
						ErrorsConstant.ERR_NUM_NULL_INTERNET_DATA),
						leRTSEx.getFirstComponent());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				cbGoingToAddress = true;
				getController().processData(
					RegRenProcessingConstants.VIEW_ADDR,
					caInetData);
				// defect 11127
				if (!cbViewOnly)
				{
					setDataFromAddressChange();
				}
				// end defect 11127 
				
			}
			else if (aaAE.getSource() == getBtnContinue())
			{
				if (cbViewOnly)
				{
					closeDlg();
					return;
				}
				else
				{
					//update current record
					if (processData())
					{
						// defect 10598 
						if ((getrbtnApprove().isSelected()
							|| getrbtnHold().isSelected())
							&& cvInProcsTrans != null
							&& cvInProcsTrans.size() > 0
							&& !cbShownPndng)
						{
							VehicleInquiryData laVehInqData =
								new VehicleInquiryData();

							laVehInqData.setInProcsTransDataList(
								cvInProcsTrans);

							getController().processData(
								VCProcessVehicleREG103.INPROCS_TRANS,
								laVehInqData);

							if (laVehInqData.getInProcsTransDataList()
								!= null)
							{
								cbShownPndng = true;
								return;
							}
						}
						// end defect 10598 

						if (getrbtnApprove().isSelected())
						{
							//proceed to inventory screen
							selectInventory();
						}
						else
						{
							// Hold, Decline, acknowledge update

							leRTSEx.addException(
							//new RTSException(978),
							new RTSException(
								ErrorsConstant.ERR_NUM_RECORD_UPDATED),
								leRTSEx.getFirstComponent());
							leRTSEx.displayError(this);

							// defect 3700, DB down 
							if (!cbIsLastTransCached)
							{
								// defect 8245
								while (true)
								{
									// try to go to next, including asking.									
									if (goToNext())
									{
										// defect 7080
										// This is so that when you put a
										// record on hold or decline the 
										// next record will have focus on
										// insurance.  If you process a
										// record the code goes through
										// setdata.
										// defect 9034
										if (caInetData
											.getCompleteRegRenData()
											.isInsuranceRequired())
										{
											getBtnInsurance()
												.requestFocus();

										}
										else
										{
											getBtnAddress()
												.requestFocus();

										}
										// end defect 9034		
										return;
										// end defect 7080
										// in the next record, just return
									}
									else
									{
										// For defect 8245:
										// if just the data record is missing
										// read the next record
										if (caTransData == null
											&& caInetData != null)
										{
											continue;
										}
										else
										{
											break;
										}
									}
								}
								// end defect 8245
							}
							else
							{
								// the last processed transaction was 
								// cached, DB/server is down, can not 
								// process further, adknowledge 
								// connection failure.
								leRTSEx.addException(
								//new RTSException(618),
								new RTSException(
									ErrorsConstant
										.ERR_NUM_DBSERVER_UNAVAILABLE),
									leRTSEx.getFirstComponent());
								leRTSEx.displayError(this);
							}
							closeDlg();
						}
					}
				}
			}
			else if (aaAE.getSource() == getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG103);
			}
			// defect 7889
			// Added cancel button to frame so that we could handle the
			// esc just like the rest of RTS  The code was taken from
			// KeyPressed()
			else if (aaAE.getSource() == getBtnCancel())
			{
				// Records that are View only and IN_PROCESS do not 
				// need to undo checkout becasue they where not checked
				// out
				if (!(viewOnly()
					|| caInetData.getStatus().equals(
						CommonConstants.IN_PROCESS
							+ CommonConstant.STR_SPACE_EMPTY)))
				{
					undoCheckout();
				}
				else
				{
					// Fast is not always good, sometimes 
					// we need to slow down.
					try
					{
						Thread.sleep(300);
					}
					catch (InterruptedException aeIntEx)
					{
					}
				}
				closeDlg();
				return;
			}
			// end defect 7889
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Close this frame.
	 */
	private void closeDlg()
	{
		// defect 8247
		// Changed the data that was passed to the search params
		// If we are processing a queue then null would be passed
		getController().processData(
			AbstractViewController.CANCEL,
			chtSearchParams);
		// end defect 8247
	}

	/**
	 * Disale all the fields so that the vehicle cannot be edited.
	 */
	private void disableEdit()
	{
		getrbtnApprove().setEnabled(false);
		getrbtnDecline().setEnabled(false);
		getrbtnHold().setEnabled(false);
		getcmbDecline().setEnabled(false);
		getcmbHold().setEnabled(false);
		getchkboxSendEmail().setEnabled(false);
		gettxtAreaComments().setEnabled(false);
		// defect 11127 
		getbtnPreviewReceipt().setEnabled(caTransData != null);
		// end defect 11127
	}

	/**
	 * Display Error Msg for a specified error code.
	 * If this frame is visible then tie the exception frame to it.
	 * If it is not visible then try to find a paranet to tie it to.
	 * 
	 * @param aiMsgCode
	 */
	private void displayMessage(int aiMsgCode)
	{
		RTSException leRTSEx = new RTSException();
		leRTSEx.addException(
			new RTSException(aiMsgCode),
			leRTSEx.getFirstComponent());
		if (this.isVisible())
		{
			leRTSEx.displayError(this);
		}
		else
		{
			RTSDialogBox laRTSDialogBox =
				getController().getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				leRTSEx.displayError(laRTSDialogBox);
			}
			else
			{
				leRTSEx.displayError(
					getController().getMediator().getDesktop());
			}
		}
	}

	/**
	 * Do the actual update of the info, for Hold and Decline.
	 * and cache the transaction update to cache if db down.
	 * 
	 * @param chtHashTable HashTable
	 * @return boolean
	 */
	private boolean doProcessData(Hashtable chtHashTable)
	{
		// defect 3700, DB down
		// Address update information 
		Hashtable lhtAddressUpdate = getAddressUpdate();
		if (lhtAddressUpdate != null)
		{
			chtHashTable.put(ADDUPDHASH, lhtAddressUpdate);
		}
		InternetTransactionData laInetTransData =
			new InternetTransactionData(chtHashTable);
		laInetTransData.setTransDateTime(RTSDate.getCurrentDate());
		if (((String) chtHashTable.get(CNTYSTCD))
			.equalsIgnoreCase(
				CommonConstants.APPROVED
					+ CommonConstant.STR_SPACE_EMPTY))
		{
			caTransData.setInternetTransData(laInetTransData);
			// for approve action, needs to pass to Phase1 and 
			// not update here.
			return true;
		}
		// defect 3700, DB down
		RegRenProcessingClientBusiness laRegRenClientBus =
			new RegRenProcessingClientBusiness();
		try
		{
			laRegRenClientBus.processData(
				GeneralConstant.INTERNET_REG_REN_PROCESSING,
				RegRenProcessingConstants.PROC_REG_RENEWAL,
				chtHashTable);
			// process through, not cached.
			cbIsLastTransCached = false;
		}
		catch (RTSException aeRTSEx)
		{
			// defect 3700, DB down
			if (aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN)
				|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				// DB/server is down, cache to disk.		 	
				TransactionCacheData laTransactionCacheData =
					new TransactionCacheData();
				laTransactionCacheData.setObj(laInetTransData);
				laTransactionCacheData.setProcName(
					TransactionCacheData.UPDATE);
				Vector lvTrans = new Vector();
				lvTrans.addElement(laTransactionCacheData);
				Transaction.writeToCache(lvTrans);
				cbIsLastTransCached = true;
			}
			else
			{
				// defect 3700, DB down
				// error is because of something else, report. 
				System.out.println(SVR_ERR + aeRTSEx.getMessage());
				RTSException leRTSValidEx = new RTSException();

				leRTSValidEx.addException(
				//new RTSException(972),
				new RTSException(
					ErrorsConstant
						.ERR_NUM_CONNECTION_FAILURE_HAS_OCCURRED),
					leRTSValidEx.getFirstComponent());
				leRTSValidEx.displayError(this);
				handleException(aeRTSEx);
				return false;
			}
		}
		return true;
	}

	/**
	 * This method initializes ivjstcLblAddress
	 * 
	 * @return JLabel
	 */
	private JLabel getAddress()
	{
		if (ivjstcLblAddress == null)
		{
			ivjstcLblAddress = new javax.swing.JLabel();
			ivjstcLblAddress.setSize(111, 14);
			ivjstcLblAddress.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblAddress.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblAddress.setText(RECIPIENT_ADDRESS);
			ivjstcLblAddress.setLocation(11, 53);
		}
		return ivjstcLblAddress;
	}

	/**
	 * Get updated address.
	 * 
	 * @return HashTable
	 */
	private Hashtable getAddressUpdate()
	{
		// when there is no address update, return null
		Hashtable lhtReturnValue = null;
		if (caInetData.isAddressChanged())
		{
			lhtReturnValue = new Hashtable();
			lhtReturnValue.put(
				"RecpntName",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getRecipientName());
			lhtReturnValue.put(
				"RenwlMailngSt1",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getSt1());
			lhtReturnValue.put(
				"RenwlMailngSt2",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getSt2());
			lhtReturnValue.put(
				"RenwlMailngCity",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getCity());
			lhtReturnValue.put(
				"RenwlMailngState",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getState());
			lhtReturnValue.put(
				"RenwlMailngZpCd",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getZpcd());
			lhtReturnValue.put(
				"RenwlMailngZpCd4",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getZpcdp4());
			lhtReturnValue.put(
				"CustPhoneNo",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getPhoneNumber());
		}
		return lhtReturnValue;
	}

	/**
	 * 
	 * Get the anual plate Indicator from the MainFrame Vehicle Data
	 * object.
	 * 
	 * @param aaMFVehicleData
	 * @return int
	 */
	protected int getAnnualPltIndi(MFVehicleData aaMFVehicleData)
	{
		String lsRegPltCd = aaMFVehicleData.getRegData().getRegPltCd();
		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);
		int liNewPltReplIndi = 0;
		if (laPlateTypeData.getAnnualPltIndi() != 0)
		{
			liNewPltReplIndi = 1;
		}
		return liNewPltReplIndi;
	}

	/**
	 * Return the RTSButton property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getBtnAddress()
	{
		if (ivjbtnAddress == null)
		{
			try
			{
				ivjbtnAddress = new RTSButton();
				ivjbtnAddress.setName("btnAddress");
				ivjbtnAddress.setMnemonic(KeyEvent.VK_A);
				ivjbtnAddress.setText(VC_ADDRESS);
				ivjbtnAddress.setBounds(373, 180, 167, 25);
				// user code begin {1}
				ivjbtnAddress.addActionListener(this);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnAddress;
	}
	/**
	 * This method initializes RTSButton Cancel.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getBtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			ivjbtnCancel = new RTSButton();
			ivjbtnCancel.setBounds(420, 410, 86, 26);
			ivjbtnCancel.setText(SystemControlBatchConstant.TXT_CANCEL);
			// Visible is false because they did not want the cancel
			// button on the screen but they wanted the ability to 
			// cancel using the exc key.
			ivjbtnCancel.setVisible(false);
			ivjbtnCancel.addActionListener(this);
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the RTSButton Continue property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getBtnContinue()
	{
		if (ivjbtnContinue == null)
		{
			try
			{
				ivjbtnContinue = new RTSButton();
				ivjbtnContinue.setName("btnContinue");
				ivjbtnContinue.setMnemonic(KeyEvent.VK_C);
				ivjbtnContinue.setText(CONTINUE);
				ivjbtnContinue.setBounds(227, 442, 90, 25);
				// user code begin {1}
				ivjbtnContinue.addActionListener(this);
				// defect 7889
				// Set the default button
				this.getRootPane().setDefaultButton(ivjbtnContinue);
				// end defect 7889
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnContinue;
	}

	/**
	 * Return the RTSButton Help property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getBtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setMnemonic(KeyEvent.VK_H);
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setBounds(323, 442, 90, 25);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the RTSButton Insurance property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getBtnInsurance()
	{
		if (ivjbtnInsurance == null)
		{
			try
			{
				ivjbtnInsurance = new RTSButton();
				ivjbtnInsurance.setName("btnInsurance");
				ivjbtnInsurance.setMnemonic(KeyEvent.VK_N);
				ivjbtnInsurance.setText(VIEW_INS);
				ivjbtnInsurance.setBounds(113, 180, 167, 25);
				// user code begin {1}
				ivjbtnInsurance.addActionListener(this);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjbtnInsurance;
	}

	/**
	 * Return the JCheckBox send email property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkboxSendEmail()
	{
		if (ivjchkboxSendEmail == null)
		{
			try
			{
				ivjchkboxSendEmail = new JCheckBox();
				ivjchkboxSendEmail.setBounds(100, 57, 88, 24);
				ivjchkboxSendEmail.setName("chkboxSendEmail");
				ivjchkboxSendEmail.setText(SND_EMAIL);
				// user code begin {1}
				ivjchkboxSendEmail.setMnemonic(KeyEvent.VK_E);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjchkboxSendEmail;
	}

	/**
	 * Return the JComboBox Decline property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcmbDecline()
	{
		if (ivjcmbDecline == null)
		{
			try
			{
				ivjcmbDecline = new JComboBox();
				ivjcmbDecline.setName("cmbDecline");
				ivjcmbDecline.setBounds(100, 81, 149, 25);
				ivjcmbDecline.addItem(CommonConstant.STR_SPACE_EMPTY);
				ivjcmbDecline.addItem(ADDRESS);
				ivjcmbDecline.addItem(INSURANCE);
				ivjcmbDecline.addItem(COUNTY);
				ivjcmbDecline.addItem(OTHER);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjcmbDecline;
	}

	/**
	 * Return the JComboBox Hold property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcmbHold()
	{
		if (ivjcmbHold == null)
		{
			try
			{
				ivjcmbHold = new JComboBox();
				ivjcmbHold.setName("cmbHold");
				ivjcmbHold.setBounds(100, 33, 149, 25);
				ivjcmbHold.addItem(CommonConstant.STR_SPACE_EMPTY);
				ivjcmbHold.addItem(ADDRESS);
				ivjcmbHold.addItem(INSURANCE);
				ivjcmbHold.addItem(OTHER);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjcmbHold;
	}

	/**
	 * Retrieve a transaction from server.
	 * 
	 * @return Object
	 */
	private Object getData()
	{
		int liFunctionID = 0;
		//create calling object and return type
		RegRenProcessingClientBusiness laRegRenProcessClientBus =
			new RegRenProcessingClientBusiness();
		Object laObj = null;
		//set function id to correct value
		// defect 6099 - NEXT_NEW - Process next record in "new" status
		if (ciProcessMode == NEXT_NEW)
		{
			liFunctionID = RegRenProcessingConstants.GET_NEXT_NEW;
		}
		else
		{
			// defect 6099 - NEXT_HOLD - Process next record in 
			// "hold" status
			if (ciProcessMode == NEXT_HOLD)
			{
				liFunctionID = RegRenProcessingConstants.GET_NEXT_HOLD;
			}
		}
		try
		{
			Hashtable lhtHashTable = new Hashtable();
			lhtHashTable.put(
				"OfcIssuanceNo",
				String.valueOf(SystemProperty.getOfficeIssuanceNo()));
			lhtHashTable.put(
				"TransWsId",
				SystemProperty.getWorkStationId()
					+ CommonConstant.STR_SPACE_EMPTY);
			lhtHashTable.put(
				"SubstaId",
				SystemProperty.getSubStationId()
					+ CommonConstant.STR_SPACE_EMPTY);
			// defect 6380 start
			// added Plateno from the frame, so we can check at the 
			// server to see if we are getting this record again 
			lhtHashTable.put("PlateNo", this.getlblPlateNo().getText());
			// defect 6380 end
			laObj =
				laRegRenProcessClientBus.processData(
					GeneralConstant.INTERNET_REG_REN_PROCESSING,
					liFunctionID,
					lhtHashTable);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("ERROR: " + aeRTSEx.getMessage());
			// defect 6099 - comment out 972 and write msg 618 - db 
			// down message here

			aeRTSEx.addException(
			//new RTSException(618),
			new RTSException(
				ErrorsConstant.ERR_NUM_DBSERVER_UNAVAILABLE),
				aeRTSEx.getFirstComponent());

			// DB Server is unavailable
			aeRTSEx.displayError(this);
			closeDlg();
			laObj = aeRTSEx;
			// end defect 6099
		}
		return laObj;
	}
	/**
	 * Get Data for Preview Receipt 
	 * 
	 * @return Vector
	 */
	private Vector getDataForPreviewReceipt() throws RTSException
	{
		String lsTransCdDesc =
			TransactionCodesCache.getTransCdDesc(
				caTransData.getTransCode());
		Transaction laTrans = new Transaction();
		CompleteTransactionData laCTData = (CompleteTransactionData)
			UtilityMethods.copy(caTransData);
		int liNewExpYr = 0; 
		try
		{
		 liNewExpYr = Integer.parseInt(
				caTransData
				.getRegFeesData()
				.getExpMoYrMin()
				.substring(
				3,
				7));
		}
		catch (NumberFormatException aeNFEx)
		{
			System.out.println("Bad Year: " + caTransData
				.getRegFeesData()
				.getExpMoYrMin()); 
		}
		
		laCTData.getVehicleInfo()
		.getRegData().setOwnrSuppliedExpYr(liNewExpYr);
		
		String lsFileName = laTrans.genPreviewRcpt(laCTData);
		ReportSearchData laRptSrchData = new ReportSearchData();
		laRptSrchData.initForClient(false);
		laRptSrchData.setIntKey2(0);
		laRptSrchData.setKey1(lsFileName);
		laRptSrchData.setKey3(" " + lsTransCdDesc);
		laRptSrchData.setData(lsFileName);
		laRptSrchData.setNextScreen(
			ReportConstant.RPR000_NEXT_SCREEN_CANCEL);
		Vector lvVector = new Vector();
		lvVector.add(laRptSrchData);
		return lvVector;
	}
	
	/**
	 * Get the Inventory Item Code from the Main Frame Vehicle Data
	 * object.
	 * 
	 * @param aaMFVehicleData
	 * @return String
	 */
	private String getInvItmCd(MFVehicleData aaMFVehicleData)
	{
		int liNewPltReplIndi = getAnnualPltIndi(aaMFVehicleData);
		String lsRegPltCd = aaMFVehicleData.getRegData().getRegPltCd();
		String lsRegStkrCd =
			aaMFVehicleData.getRegData().getRegStkrCd();
		String lsInvItmCd = lsRegStkrCd;
		if (liNewPltReplIndi == 1)
		{
			lsInvItmCd = lsRegPltCd;
		}
		return lsInvItmCd;
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
					getJlblDetailRec(),
					getJlblDetailRec().getName());
				getJDialogContentPane().add(
					getJlblStatus(),
					getJlblStatus().getName());
				getJDialogContentPane().add(
					getJpnlActionCom(),
					getJpnlActionCom().getName());
				getJDialogContentPane().add(
					getBtnContinue(),
					getBtnContinue().getName());
				getJDialogContentPane().add(
					getlblStatus(),
					getlblStatus().getName());
				getJDialogContentPane().add(
					getBtnHelp(),
					getBtnHelp().getName());
				// user code begin {1}
				// defect 7889
				// removed setNextFocusableComponent and added cancel
				// button
				getJDialogContentPane().add(
					getBtnCancel(),
					getBtnCancel().getName());
				// Added to handle arrowing through this group 
				ivjJDialogContentPane.add(getJpnlDetail(), null);
				// of buttons
				// defect 11127 
				ivjJDialogContentPane.add(getbtnPreviewReceipt(), null);
				// end defect 11127 
				RTSButtonGroup laBtnGrp = new RTSButtonGroup();
				laBtnGrp.add(getBtnContinue());
				laBtnGrp.add(getBtnHelp());
				// defect 11127 
				laBtnGrp.add(getbtnPreviewReceipt());
				// end defect 11127
				// end defect 7889
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJDialogContentPane;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblAppName()
	{
		if (ivjJlblAppName == null)
		{
			try
			{
				ivjJlblAppName = new JLabel();
				ivjJlblAppName.setName("JLabelAppName");
				ivjJlblAppName.setText(RECIPIENT_NAME);
				ivjJlblAppName.setBounds(8, 33, 113, 14);
				// user code begin {1}
				ivjJlblAppName.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblAppName.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblAppName;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblArrows1()
	{
		if (ivjJlblArrows1 == null)
		{
			try
			{
				ivjJlblArrows1 = new JLabel();
				ivjJlblArrows1.setBounds(76, 85, 14, 16);
				ivjJlblArrows1.setName("JLabelArrows1");
				ivjJlblArrows1.setText(">>");
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblArrows1;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblArrows2()
	{
		if (ivjJlblArrows2 == null)
		{
			try
			{
				ivjJlblArrows2 = new JLabel();
				ivjJlblArrows2.setBounds(76, 37, 14, 16);
				ivjJlblArrows2.setName("JLabelArrows2");
				ivjJlblArrows2.setText(">>");
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblArrows2;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblCarryingCap()
	{
		if (ivjJlblCarryingCap == null)
		{
			try
			{
				ivjJlblCarryingCap = new JLabel();
				ivjJlblCarryingCap.setName("JLabelCarryingCap");
				ivjJlblCarryingCap.setText("Carrying Capacity:");
				ivjJlblCarryingCap.setBounds(375, 93, 107, 14);
				// user code begin {1}
				ivjJlblCarryingCap.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblCarryingCap.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblCarryingCap;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblComments()
	{
		if (ivjJLabel10 == null)
		{
			try
			{
				ivjJLabel10 = new JLabel();
				ivjJLabel10.setBounds(8, 2, 72, 14);
				ivjJLabel10.setName("JLabelComments");
				ivjJLabel10.setText("Comments:");
				// user code begin {1}
				ivjJLabel10.setDisplayedMnemonic(KeyEvent.VK_M);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJLabel10;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblDetailRec()
	{
		if (ivjJlblDetailRec == null)
		{
			try
			{
				ivjJlblDetailRec = new JLabel();
				ivjJlblDetailRec.setName("JLabelDetailRec");
				ivjJlblDetailRec.setFont(
					new java.awt.Font("Dialog", 1, 12));
				ivjJlblDetailRec.setText("Vehicle Detail Record");
				ivjJlblDetailRec.setBounds(9, 24, 169, 17);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblDetailRec;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblDocNo()
	{
		if (ivjJlblDocNo == null)
		{
			try
			{
				ivjJlblDocNo = new JLabel();
				ivjJlblDocNo.setSize(87, 14);
				ivjJlblDocNo.setName("JLabelDocNo");
				ivjJlblDocNo.setText("Document No:");
				ivjJlblDocNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblDocNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjJlblDocNo.setLocation(395, 53);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblDocNo;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblEmptyWeight()
	{
		if (ivjJlblEmptyWeight == null)
		{
			try
			{
				ivjJlblEmptyWeight = new JLabel();
				ivjJlblEmptyWeight.setName("JLabelEmptyWeight");
				ivjJlblEmptyWeight.setText("Empty Weight:");
				ivjJlblEmptyWeight.setBounds(375, 73, 107, 14);
				// user code begin {1}
				ivjJlblEmptyWeight.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblEmptyWeight.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblEmptyWeight;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblGrossWeight()
	{
		if (ivjJlblGrossWeight == null)
		{
			try
			{
				ivjJlblGrossWeight = new JLabel();
				ivjJlblGrossWeight.setSize(107, 14);
				ivjJlblGrossWeight.setName("JLabelGrossWeight");
				ivjJlblGrossWeight.setText("Gross Weight:");
				ivjJlblGrossWeight.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblGrossWeight.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjJlblGrossWeight.setLocation(375, 113);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblGrossWeight;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblIssuanceDate()
	{
		if (ivjJlblIssuanceDate == null)
		{
			try
			{
				ivjJlblIssuanceDate = new JLabel();
				ivjJlblIssuanceDate.setName("JLabelIssuanceDate");
				ivjJlblIssuanceDate.setText("Title Issue Date:");
				ivjJlblIssuanceDate.setBounds(10, 93, 113, 14);
				// user code begin {1}
				ivjJlblIssuanceDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblIssuanceDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblIssuanceDate;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblName()
	{
		if (ivjJlblName == null)
		{
			try
			{
				ivjJlblName = new JLabel();
				ivjJlblName.setName("JLabelName");
				ivjJlblName.setFont(new java.awt.Font("Arial", 1, 12));
				ivjJlblName.setText(OWNER_NAME);
				ivjJlblName.setBounds(8, 13, 113, 14);
				// user code begin {1}
				ivjJlblName.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblName.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblName;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblNewExpDate()
	{
		if (ivjJlblNewExpDate == null)
		{
			try
			{
				ivjJlblNewExpDate = new JLabel();
				ivjJlblNewExpDate.setName("JLabelNewExpDate");
				ivjJlblNewExpDate.setText(NEW_EXP_MO_YR);
				ivjJlblNewExpDate.setBounds(10, 73, 113, 14);
				// user code begin {1}
				ivjJlblNewExpDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblNewExpDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblNewExpDate;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPlateAge()
	{
		if (ivjJlblPlateAge == null)
		{
			try
			{
				ivjJlblPlateAge = new JLabel();
				ivjJlblPlateAge.setName("JLabelPlateAge");
				ivjJlblPlateAge.setText("Plate Age:");
				ivjJlblPlateAge.setBounds(10, 113, 113, 14);
				// user code begin {1}
				ivjJlblPlateAge.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblPlateAge.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblPlateAge;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblPlateNo()
	{
		if (ivjJlblPlateNo == null)
		{
			try
			{
				ivjJlblPlateNo = new JLabel();
				ivjJlblPlateNo.setSize(107, 14);
				ivjJlblPlateNo.setName("JLabelPlateNo");
				ivjJlblPlateNo.setText("Plate No:");
				ivjJlblPlateNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblPlateNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjJlblPlateNo.setLocation(375, 13);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblPlateNo;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblStatus()
	{
		if (ivjJlblStatus == null)
		{
			try
			{
				ivjJlblStatus = new JLabel();
				ivjJlblStatus.setName("JLabelStatus");
				ivjJlblStatus.setText("Status:");
				ivjJlblStatus.setBounds(395, 26, 45, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblStatus;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblTonnage()
	{
		if (ivjJlblTonnage == null)
		{
			try
			{
				ivjJlblTonnage = new JLabel();
				ivjJlblTonnage.setName("JLabelTonnage");
				ivjJlblTonnage.setText("Tonnage:");
				ivjJlblTonnage.setBounds(375, 133, 107, 14);
				// user code begin {1}
				ivjJlblTonnage.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblTonnage.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblTonnage;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblTraceNo()
	{
		if (ivjJlblTraceNo == null)
		{
			try
			{
				ivjJlblTraceNo = new JLabel();
				ivjJlblTraceNo.setName("JLabelTraceNo");
				ivjJlblTraceNo.setText("Internet Trace No:");
				ivjJlblTraceNo.setBounds(10, 133, 113, 14);
				// user code begin {1}
				ivjJlblTraceNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblTraceNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblTraceNo;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblTransId()
	{
		if (ivjJlblTransId == null)
		{
			try
			{
				ivjJlblTransId = new JLabel();
				ivjJlblTransId.setName("JLabelTransId");
				ivjJlblTransId.setText("Transaction Id:");
				ivjJlblTransId.setBounds(10, 153, 113, 14);
				// user code begin {1}
				ivjJlblTransId.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblTransId.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblTransId;
	}

	/**
	 * Return the Label property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJlblVin()
	{
		if (ivjJlblVin == null)
		{
			try
			{
				ivjJlblVin = new JLabel();
				ivjJlblVin.setSize(107, 14);
				ivjJlblVin.setName("JLabelVin");
				ivjJlblVin.setText("VIN:");
				ivjJlblVin.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjJlblVin.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjJlblVin.setLocation(375, 33);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJlblVin;
	}

	/**
	 * This method initializes jPnlAction
	 * 
	 * @return JPanel
	 */
	private JPanel getJpnlAction()
	{
		if (jPnlAction == null)
		{
			jPnlAction = new javax.swing.JPanel();
			jPnlAction.setLayout(null);
			jPnlAction.add(getrbtnApprove(), null);
			jPnlAction.add(getrbtnHold(), null);
			jPnlAction.add(getrbtnDecline(), null);
			jPnlAction.add(getcmbHold(), null);
			jPnlAction.add(getJlblArrows1(), null);
			jPnlAction.add(getJlblArrows2(), null);
			jPnlAction.add(getcmbDecline(), null);
			jPnlAction.add(getchkboxSendEmail(), null);
			jPnlAction.setBounds(6, 13, 267, 116);
			jPnlAction.setName("JPanelAction");
		}
		return jPnlAction;
	}

	/**
	 * Return the Panel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJpnlActionCom()
	{
		if (ivjJpnlActionCom == null)
		{
			try
			{
				ivjJpnlActionCom = new JPanel();
				ivjJpnlActionCom.setName("JPanelActionCom");
				ivjJpnlActionCom.setLayout(null);
				ivjJpnlActionCom.setBounds(39, 289, 601, 136);
				ivjJpnlActionCom.add(
					getJpnlComments(),
					getJpnlComments().getName());
				ivjJpnlActionCom.add(getJpnlAction(), null);
				getJpnlActionCom().setBorder(
					BorderFactory.createTitledBorder(ACTION));
				setButtonGroup();
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJpnlActionCom;
	}

	/**
	 * This method initializes jPnlComments
	 * 
	 * @return JPanel
	 */
	private JPanel getJpnlComments()
	{
		if (jPnlComments == null)
		{
			jPnlComments = new JPanel();
			jPnlComments.setLayout(null);
			jPnlComments.add(
				getJScrollPaneComments(),
				getJScrollPaneComments().getName());
			jPnlComments.add(
				getJlblComments(),
				getJlblComments().getName());
			jPnlComments.setBounds(279, 13, 316, 116);
			jPnlComments.setName("JPanelComments");
		}
		return jPnlComments;
	}

	/**
	 * Return the JPanel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJpnlDetail()
	{
		if (ivjJpnlDetail == null)
		{
			try
			{
				ivjJpnlDetail = new JPanel();
				ivjJpnlDetail.setName("JPanelDetail");
				ivjJpnlDetail.setLayout(null);
				getJpnlDetail().add(
					getJlblName(),
					getJlblName().getName());
				getJpnlDetail().add(
					getJlblPlateNo(),
					getJlblPlateNo().getName());
				getJpnlDetail().add(
					getJlblVin(),
					getJlblVin().getName());
				getJpnlDetail().add(
					getJlblNewExpDate(),
					getJlblNewExpDate().getName());
				getJpnlDetail().add(
					getJlblPlateAge(),
					getJlblPlateAge().getName());
				getJpnlDetail().add(
					getJlblEmptyWeight(),
					getJlblEmptyWeight().getName());
				getJpnlDetail().add(
					getJlblIssuanceDate(),
					getJlblIssuanceDate().getName());
				getJpnlDetail().add(
					getJlblDocNo(),
					getJlblDocNo().getName());
				getJpnlDetail().add(
					getBtnInsurance(),
					getBtnInsurance().getName());
				getJpnlDetail().add(
					getBtnAddress(),
					getBtnAddress().getName());
				getJpnlDetail().add(
					getJlblTraceNo(),
					getJlblTraceNo().getName());
				getJpnlDetail().add(
					getJlblTransId(),
					getJlblTransId().getName());
				getJpnlDetail().add(
					getJlblAppName(),
					getJlblAppName().getName());
				getJpnlDetail().add(
					getJlblCarryingCap(),
					getJlblCarryingCap().getName());
				getJpnlDetail().add(
					getJlblGrossWeight(),
					getJlblGrossWeight().getName());
				getJpnlDetail().add(
					getJlblTonnage(),
					getJlblTonnage().getName());
				getJpnlDetail().add(
					getlblNameOnRecord(),
					getlblNameOnRecord().getName());
				getJpnlDetail().add(
					getlblApplicantName(),
					getlblApplicantName().getName());
				getJpnlDetail().add(
					getlblPlateNo(),
					getlblPlateNo().getName());
				getJpnlDetail().add(getlblVIN(), getlblVIN().getName());
				getJpnlDetail().add(
					getlblDocumentNo(),
					getlblDocumentNo().getName());
				getJpnlDetail().add(
					getlblNewExpDate(),
					getlblNewExpDate().getName());
				getJpnlDetail().add(
					getlblTitleIssuanceDate(),
					getlblTitleIssuanceDate().getName());
				getJpnlDetail().add(
					getlblPlateAge(),
					getlblPlateAge().getName());
				getJpnlDetail().add(
					getlblEmptyWt(),
					getlblEmptyWt().getName());
				getJpnlDetail().add(
					getlblCarryingCapacity(),
					getlblCarryingCapacity().getName());
				getJpnlDetail().add(
					getlblGrossWt(),
					getlblGrossWt().getName());
				getJpnlDetail().add(
					getlblTonnage(),
					getlblTonnage().getName());
				getJpnlDetail().add(
					getlblTraceNo(),
					getlblTraceNo().getName());
				getJpnlDetail().add(
					getlblTransID(),
					getlblTransID().getName());
				// user code begin {1}

				// defect 10420 
				ivjJpnlDetail.add(getAddress(), null);
				ivjJpnlDetail.add(getlblAddress(), null);
				// end defect 10420 

				ivjJpnlDetail.setBounds(8, 45, 666, 225);
				ivjJpnlDetail.setBorder(
					new javax.swing.border.EtchedBorder());
				// defect 7889
				// Added to handle arrowing through this group 
				// of buttons
				RTSButtonGroup laBtnGrp = new RTSButtonGroup();
				laBtnGrp.add(getBtnInsurance());
				laBtnGrp.add(getBtnAddress());
				// end defect 7889
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJpnlDetail;
	}
	/**
	 * Return the JScrollPane property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPaneComments()
	{
		if (ivjJScrlPnComments == null)
		{
			try
			{
				ivjJScrlPnComments = new JScrollPane();
				ivjJScrlPnComments.setBounds(8, 18, 302, 94);
				ivjJScrlPnComments.setName("JScrollPane2");
				ivjJScrlPnComments.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPaneComments().setViewportView(
					gettxtAreaComments());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJScrlPnComments;
	}

	/**
	 * This method initializes ivjlblAddress
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress()
	{
		if (ivjlblAddress == null)
		{
			ivjlblAddress = new javax.swing.JLabel();
			ivjlblAddress.setSize(264, 14);
			ivjlblAddress.setHorizontalTextPosition(
				javax.swing.SwingConstants.LEFT);
			ivjlblAddress.setHorizontalAlignment(
				javax.swing.SwingConstants.LEFT);
			ivjlblAddress.setLocation(129, 53);
		}
		return ivjlblAddress;
	}

	/**
	 * Return the lblApplicantName property value.
	 * 
	 * @return JLabel
	 */
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
				ivjlblApplicantName.setBounds(129, 33, 241, 14);
				// user code begin {1}
				ivjlblApplicantName.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblApplicantName.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblApplicantName;
	}

	/**
	 * Return the lblCarryingCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCarryingCapacity()
	{
		if (ivjlblCarryingCapacity == null)
		{
			try
			{
				ivjlblCarryingCapacity = new JLabel();
				ivjlblCarryingCapacity.setName("lblCarryingCapacity");
				ivjlblCarryingCapacity.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblCarryingCapacity.setBounds(493, 93, 165, 14);
				// user code begin {1}
				ivjlblCarryingCapacity.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblCarryingCapacity.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblCarryingCapacity;
	}

	/**
	 * Return the lblDocumentNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocumentNo()
	{
		if (ivjlblDocumentNo == null)
		{
			try
			{
				ivjlblDocumentNo = new JLabel();
				ivjlblDocumentNo.setSize(165, 14);
				ivjlblDocumentNo.setName("lblDocumentNo");
				ivjlblDocumentNo.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblDocumentNo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblDocumentNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblDocumentNo.setLocation(493, 53);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblDocumentNo;
	}

	/**
	 * Return the lblEmptyWt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEmptyWt()
	{
		if (ivjlblEmptyWt == null)
		{
			try
			{
				ivjlblEmptyWt = new JLabel();
				ivjlblEmptyWt.setName("lblEmptyWt");
				ivjlblEmptyWt.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblEmptyWt.setBounds(493, 73, 165, 14);
				// user code begin {1}
				ivjlblEmptyWt.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblEmptyWt.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblEmptyWt;
	}

	/**
	 * Return the lblGrossWt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblGrossWt()
	{
		if (ivjlblGrossWt == null)
		{
			try
			{
				ivjlblGrossWt = new JLabel();
				ivjlblGrossWt.setSize(165, 14);
				ivjlblGrossWt.setName("lblGrossWt");
				ivjlblGrossWt.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblGrossWt.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblGrossWt.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblGrossWt.setLocation(493, 113);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblGrossWt;
	}

	/**
	 * Return the lblNameOnRecord property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblNameOnRecord()
	{
		if (ivjlblNameOnRecord == null)
		{
			try
			{
				ivjlblNameOnRecord = new JLabel();
				ivjlblNameOnRecord.setName("lblNameOnRecord");
				ivjlblNameOnRecord.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblNameOnRecord.setBounds(129, 13, 241, 14);
				// user code begin {1}
				ivjlblNameOnRecord.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblNameOnRecord.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblNameOnRecord;
	}

	/**
	 * Return the lblNewExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblNewExpDate()
	{
		if (ivjlblNewExpDate == null)
		{
			try
			{
				ivjlblNewExpDate = new JLabel();
				ivjlblNewExpDate.setSize(189, 14);
				ivjlblNewExpDate.setName("lblNewExpDate");
				ivjlblNewExpDate.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblNewExpDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblNewExpDate.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblNewExpDate.setLocation(130, 73);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblNewExpDate;
	}

	/**
	 * Return the lblPlateAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateAge()
	{
		if (ivjlblPlateAge == null)
		{
			try
			{
				ivjlblPlateAge = new JLabel();
				ivjlblPlateAge.setSize(189, 14);
				ivjlblPlateAge.setName("lblPlateAge");
				ivjlblPlateAge.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblPlateAge.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblPlateAge.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblPlateAge.setLocation(130, 113);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblPlateAge;
	}

	/**
	 * Return the lblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateNo()
	{
		if (ivjlblPlateNo == null)
		{
			try
			{
				ivjlblPlateNo = new JLabel();
				ivjlblPlateNo.setSize(165, 14);
				ivjlblPlateNo.setName("lblPlateNo");
				ivjlblPlateNo.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblPlateNo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblPlateNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblPlateNo.setLocation(493, 13);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblPlateNo;
	}

	/**
	 * Return the lblStatus property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblStatus()
	{
		if (ivjlblStatus == null)
		{
			try
			{
				ivjlblStatus = new JLabel();
				ivjlblStatus.setName("lblStatus");
				ivjlblStatus.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblStatus.setBounds(447, 26, 227, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblStatus;
	}

	/**
	 * Return the lblTitleIssuanceDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTitleIssuanceDate()
	{
		if (ivjlblTitleIssuanceDate == null)
		{
			try
			{
				ivjlblTitleIssuanceDate = new JLabel();
				ivjlblTitleIssuanceDate.setSize(189, 14);
				ivjlblTitleIssuanceDate.setName("lblTitleIssuanceDate");
				ivjlblTitleIssuanceDate.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblTitleIssuanceDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblTitleIssuanceDate.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblTitleIssuanceDate.setLocation(130, 93);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblTitleIssuanceDate;
	}

	/**
	 * Return the lblTonnage property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTonnage()
	{
		if (ivjlblTonnage == null)
		{
			try
			{
				ivjlblTonnage = new JLabel();
				ivjlblTonnage.setName("lblTonnage");
				ivjlblTonnage.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblTonnage.setBounds(493, 133, 165, 14);
				// user code begin {1}
				ivjlblTonnage.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblTonnage.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblTonnage;
	}

	/**
	 * Return the lblTransactionID property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTraceNo()
	{
		if (ivjlblTraceNo == null)
		{
			try
			{
				ivjlblTraceNo = new JLabel();
				ivjlblTraceNo.setSize(189, 14);
				ivjlblTraceNo.setName("lblTraceNo");
				ivjlblTraceNo.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblTraceNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblTraceNo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblTraceNo.setLocation(130, 133);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblTraceNo;
	}

	/**
	 * Return the lblRTSTransID property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTransID()
	{
		if (ivjlblTransID == null)
		{
			try
			{
				ivjlblTransID = new JLabel();
				ivjlblTransID.setSize(189, 14);
				ivjlblTransID.setName("lblTransID");
				ivjlblTransID.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblTransID.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjlblTransID.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblTransID.setLocation(130, 153);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblTransID;
	}

	/**
	 * Return the lblVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setSize(165, 14);
				ivjlblVIN.setName("lblVIN");
				ivjlblVIN.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblVIN.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblVIN.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				// user code end
				ivjlblVIN.setLocation(493, 33);
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblVIN;
	}

	/**
	 * Called by methods setData and goToNext
	 * 
	 * @return Object
	 */
	private Object getNextRecord()
	{
		InternetRegRecData laIntRegRenData = null;
		Object laDataObj = getData();

		if (laDataObj instanceof RTSException)
		{
			// defect 6099 ==> return obj instead of 
			// lData(which would be null)
			return laDataObj;
		}

		// defect 8245
		if (laDataObj == null)
		{
			caInetData = null;
			caTransData = null;
		}
		else
		{
			// end defect 8245
			laIntRegRenData = getValidRecord(laDataObj);
			// end defect 6099

			return laIntRegRenData;
		}
		// There is no data obtained, and the DB/server is up,
		// need to consider switch mode
		if (userAgreeSwitch())
		{
			laDataObj = getData();
			if (laDataObj instanceof RTSException)
			{
				// Server/db down, cannot get any obj, after the message
				// displayed in getData(), should not prompt to switch, 
				// so ==> return.	
				return null;
			}
			// Try to get a valid record           
			// defect 6099 - check for instanceof Vector
			if (laDataObj instanceof Vector)
			{
				laIntRegRenData = getValidRecord(laDataObj);
			}
		}

		return laIntRegRenData;
	}

	/**
	 * Display the Next Record Dialog.
	 * 
	 * @return boolean
	 */
	private boolean getNextRecordDlg()
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.CTL001,
				RegRenProcessingConstants.OBTAIN_NEXT_974,
				"RTS000974");
		int leRetCode = leRTSEx.displayError(this);

		// defect 8245
		caTransData = null;
		caInetData = null;
		// end defect 8245

		if (leRetCode == RTSException.NO)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Return the rbtnApprove property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getrbtnApprove()
	{
		if (ivjrbtnApprove == null)
		{
			try
			{
				ivjrbtnApprove = new JRadioButton();
				ivjrbtnApprove.setBounds(5, 7, 72, 24);
				ivjrbtnApprove.setName("rbtnApprove");
				ivjrbtnApprove.setText(APPROVE);
				ivjrbtnApprove.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjrbtnApprove.setForeground(java.awt.Color.black);
				// user code begin {1}
				ivjrbtnApprove.setMnemonic(KeyEvent.VK_P);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjrbtnApprove;
	}

	/**
	 * Return the rbtnDecline property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getrbtnDecline()
	{
		if (ivjrbtnDecline == null)
		{
			try
			{
				ivjrbtnDecline = new JRadioButton();
				ivjrbtnDecline.setBounds(5, 81, 72, 24);
				ivjrbtnDecline.setName("rbtnDecline");
				ivjrbtnDecline.setText(DECLINE);
				ivjrbtnDecline.setMnemonic(KeyEvent.VK_D);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjrbtnDecline;
	}

	/**
	 * Return the rbtnHold property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getrbtnHold()
	{
		if (ivjrbtnHold == null)
		{
			try
			{
				ivjrbtnHold = new JRadioButton();
				ivjrbtnHold.setBounds(5, 33, 72, 24);
				ivjrbtnHold.setName("rbtnHold");
				ivjrbtnHold.setText(HOLD);
				// user code begin {1}
				ivjrbtnHold.setMnemonic(KeyEvent.VK_O);
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjrbtnHold;
	}

	/**
	 * Get the reson code for processing the record.
	 * 
	 * @param asReasonCode
	 * @return String
	 */
	private String getReasonCodeString(String asReasonCode)
	{
		String lsReasonCodeString = CommonConstant.STR_SPACE_EMPTY;

		if (asReasonCode
			.equalsIgnoreCase(RegRenProcessingConstants.RSN_ADDR))
		{
			lsReasonCodeString = "Address";
		}
		else if (
			asReasonCode.equalsIgnoreCase(
				RegRenProcessingConstants.RSN_INS))
		{
			lsReasonCodeString = "Insurance";
		}
		else if (
			asReasonCode.equalsIgnoreCase(
				RegRenProcessingConstants.RSN_CNTY))
		{
			lsReasonCodeString = "County";
		}
		else if (
			asReasonCode.equalsIgnoreCase(
				RegRenProcessingConstants.RSN_OTHER))
		{
			lsReasonCodeString = "Other";
		}

		return lsReasonCodeString;
	}
	/**
	 * Return the JTextArea property value.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAreaComments()
	{
		if (ivjtxtAreaComments == null)
		{
			try
			{
				// defect 7889
				// removed inner classes and used a prebuilt document
				// to handle the max characters in the text field.
				// Also added TAB and SHIFT+TAB handling for this field.
				ivjtxtAreaComments =
					new JTextArea(
						new DocumentNoAlt(
							RegRenProcessingConstants.MAX_COMMENTS));
				ivjtxtAreaComments.setName("txtAreaComments");
				ivjtxtAreaComments.setWrapStyleWord(true);
				ivjtxtAreaComments.setColumns(40);
				ivjtxtAreaComments.setRows(5);
				ivjtxtAreaComments.setLineWrap(true);
				ivjtxtAreaComments.setName("txtAreaComments");
				ivjtxtAreaComments.setFocusable(true);

				// added handling of TAB and SHIFT+TAB
				HashSet lhsKeys = new HashSet();
				lhsKeys.add(KeyStroke.getKeyStroke("TAB"));
				ivjtxtAreaComments.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					lhsKeys);
				lhsKeys.clear();
				lhsKeys.add(KeyStroke.getKeyStroke("shift TAB"));
				ivjtxtAreaComments.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					lhsKeys);
				// end defect 7889
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjtxtAreaComments;
	}

	/**
	 * This method gets a valid record for processing.
	 * 
	 * @param aaObject Object
	 * @return InternetRegRecData
	 */
	private InternetRegRecData getValidRecord(Object aaObject)
	{
		caInetData = null;
		caTransData = null;

		if (aaObject instanceof Vector)
		{
			Vector lvData = (Vector) aaObject;
			if (lvData.size() > 0)
			{
				caInetData = (InternetRegRecData) lvData.get(0);
				caTransData = (CompleteTransactionData) lvData.get(1);
				RegistrationVerify.verifyItrntComplTransData(
					caTransData,
					caInetData
						.getCompleteRegRenData()
						.getVehBaseData()
						.getPlateNo());

				// defect 10598 
				if (lvData.size() > 2)
				{
					cvInProcsTrans = (Vector) lvData.get(2);
				}
				cbShownPndng = false;
				// end defect 10598 

			}
		}
		return caInetData;
	}

	/**
	 * This method will go get the next reocrd for processing.
	 * Called by methods setData and actionPerformed.
	 * 
	 * @return boolean
	 */
	private boolean goToNext()
	{
		csPrevSelection = CommonConstant.STR_SPACE_EMPTY;

		// defect 6099
		InternetRegRecData laData = null;

		//move instantiation up, so usuable by whole method
		//end defect 6099
		// ciProcessMode == 1 - NEXT_NEW - Process next record in 
		// "new" status
		// ciProcessMode == 2 - NEXT_HOLD - Process next record in
		// "hold" status
		if (ciProcessMode == NEXT_NEW || ciProcessMode == NEXT_HOLD)
		{
			// ask if process the next record
			if (getNextRecordDlg())
			{
				// defect 6099
				// Seperate out the exception for better DB down 
				// error handling
				Object laNextRecord = getNextRecord();

				if (laNextRecord instanceof RTSException)
				{
					return false;
				}

				laData = (InternetRegRecData) laNextRecord;

				if (caInetData == null)
				{
					RTSException leValidEx = new RTSException();

					// defect 8749 
					leValidEx.addException(
					//new RTSException(975),
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_TRANS_IN_QUEUE),
						leValidEx.getFirstComponent());
					// end defect 8749 

					leValidEx.displayError(this);
					// defect 8245 	
					// caTransData is checked in ActionPerformed
					caTransData = null;
					// end defect 8245
					return false;
				}
				else
				{
					// defect 8245
					if (caTransData == null)
					{
						return false;
					}
					else
					{
						
						// defect 11127
						// Not used 
						//	csPrevPlateNo =
						//			laData
						//				.getCompleteRegRenData()
						//				.getVehBaseData()
						//				.getPlateNo();
						// end defect 11127 
						
						refreshData(laData);

						// going to the next record
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		// Uncomment the following lines to print uncaught exceptions 
		// to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7889
		// Handle GUI exceptions this was just ignored before
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
		// end defect 7897
	}

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
			setName("FrmProcessVehicleREG103");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(693, 510);
			setTitle("Vehicle Information and Processing     REG103");
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
		// user code begin {2}
		//trap window closing (else corrupts RTS Controller Stack)
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// user code end
	}

	/**
	 * Update the transaction info.
	 * 
	 * @return boolean
	 */
	private boolean processData()
	{
		//standard validation
		clearAllColor(this);
		RTSException leRTSValidEx = new RTSException();
		//create update keys object
		Hashtable lhtHashTable = new Hashtable();
		//set values to update
		lhtHashTable.put(
			"EmailAddr",
			caInetData
				.getCompleteRegRenData()
				.getVehUserData()
				.getEmail());
		String lsHoldCode = getcmbHold().getSelectedItem().toString();
		if (lsHoldCode.equalsIgnoreCase("Address"))
		{
			lsHoldCode = RegRenProcessingConstants.RSN_ADDR;
		}
		else if (lsHoldCode.equalsIgnoreCase("Insurance"))
		{
			lsHoldCode = RegRenProcessingConstants.RSN_INS;
		}
		else if (lsHoldCode.equalsIgnoreCase("Other"))
		{
			lsHoldCode = RegRenProcessingConstants.RSN_OTHER;
		}

		String lsDeclineCode =
			getcmbDecline().getSelectedItem().toString();

		if (lsDeclineCode.equalsIgnoreCase("Address"))
		{
			lsDeclineCode = RegRenProcessingConstants.RSN_ADDR;
		}
		else if (lsDeclineCode.equalsIgnoreCase("Insurance"))
		{
			lsDeclineCode = RegRenProcessingConstants.RSN_INS;
		}
		else if (lsDeclineCode.equalsIgnoreCase("County"))
		{
			lsDeclineCode = RegRenProcessingConstants.RSN_CNTY;
		}
		else if (lsDeclineCode.equalsIgnoreCase("Other"))
		{
			lsDeclineCode = RegRenProcessingConstants.RSN_OTHER;
		}

		// defect 7889
		// Consolidated nested if statements into an "if or"
		//comments are mandatory when hold/decline is of type 'other'
		if ((getrbtnHold().isSelected()
			|| getrbtnDecline().isSelected())
			&& (lsHoldCode.equals(RegRenProcessingConstants.RSN_OTHER)
				|| lsDeclineCode.equals(
					RegRenProcessingConstants.RSN_OTHER))
			&& (gettxtAreaComments().getText().length() == 0))
		{
			int liErrorCode;

			// defect 8749 
			if (lsHoldCode.equals(RegRenProcessingConstants.RSN_OTHER))
			{
				//liErrorCode = 965;
				liErrorCode =
					ErrorsConstant.ERR_NUM_COMMENT_REQD_FOR_HOLD;
			}
			else
			{
				//liErrorCode = 966;
				liErrorCode =
					ErrorsConstant.ERR_NUM_COMMENT_REQD_FOR_DECLINE;
			}
			// end defect 8749 

			clearAllColor(this);
			leRTSValidEx.addException(
				new RTSException(liErrorCode),
				gettxtAreaComments());
			leRTSValidEx.displayError(this);
			leRTSValidEx.getFirstComponent().requestFocus();
			return false;
		}
		// end defect 7889

		lhtHashTable.put(
			"CntyReasnTxt",
			gettxtAreaComments().getText());

		//get action item (approve, hold or decline)
		if (getrbtnApprove().isSelected())
		{
			lhtHashTable.put(
				"CntyStatusCd",
				CommonConstants.APPROVED
					+ CommonConstant.STR_SPACE_EMPTY);
			lhtHashTable.put("SendEmailIndi", "1");
		}
		else if (getrbtnHold().isSelected())
		{
			lhtHashTable.put(
				"CntyStatusCd",
				CommonConstants.HOLD + CommonConstant.STR_SPACE_EMPTY);
			//require reason for hold
			if (getcmbHold().getSelectedItem().toString().length()
				== 0)
			{
				clearAllColor(this);

				leRTSValidEx.addException(
				//new RTSException(963),
				new RTSException(
					ErrorsConstant.ERR_NUM_HOLD_REASN_REQD),
					getcmbHold());

				Toolkit.getDefaultToolkit().beep();
				leRTSValidEx.displayError(this);
				leRTSValidEx.getFirstComponent().requestFocus();
				return false;
			}
			lhtHashTable.put("CntyReasnCd", lsHoldCode);
			lhtHashTable.put(
				"SendEmailIndi",
				(getchkboxSendEmail().isSelected()) ? "1" : "0");
		}
		else if (getrbtnDecline().isSelected())
		{
			lhtHashTable.put(
				"CntyStatusCd",
				CommonConstants.DECLINED_REFUND_PENDING
					+ CommonConstant.STR_SPACE_EMPTY);
			lhtHashTable.put("SendEmailIndi", "1");

			//require reason for decline
			if (getcmbDecline().getSelectedItem().toString().length()
				== 0)
			{
				clearAllColor(this);

				leRTSValidEx.addException(
				//new RTSException(964),
				new RTSException(
					ErrorsConstant.ERR_NUM_DECLINE_REASN_REQD),
					getcmbDecline());

				// displayError will handle beep
				Toolkit.getDefaultToolkit().beep();
				leRTSValidEx.displayError(this);
				leRTSValidEx.getFirstComponent().requestFocus();
				return false;
			}
			lhtHashTable.put("CntyReasnCd", lsDeclineCode);
		}
		else
		{
			//no action was requested, abort
			clearAllColor(this);
			// defect 7125
			// Decline Combo Box must be enabled to be all Red. 
			getcmbDecline().setEnabled(true);
			// end defect 7125 

			// defect 8749 
			leRTSValidEx.addException(
			//new RTSException(967),
			new RTSException(
				ErrorsConstant.ERR_NUM_NO_ACTION_COMMAND_SPECIFIED),
				getcmbDecline());
			// end defect 8749 

			// displayError will handle beep
			Toolkit.getDefaultToolkit().beep();
			leRTSValidEx.displayError(this);
			leRTSValidEx.getFirstComponent().requestFocus();
			return false;
		}
		//add where clause keys	
		int liCountyID = SystemProperty.getOfficeIssuanceNo();
		lhtHashTable.put(
			"CntyIssuanceNo",
			Integer.toString(liCountyID));
		lhtHashTable.put(
			"RegPltNo",
			caInetData
				.getCompleteRegRenData()
				.getVehBaseData()
				.getPlateNo());
		lhtHashTable.put(
			"VIN",
			caInetData
				.getCompleteRegRenData()
				.getVehBaseData()
				.getVin());
		lhtHashTable.put(
			"DocNo",
			caInetData
				.getCompleteRegRenData()
				.getVehBaseData()
				.getDocNo());

		////////////////////////////////////////////////////////////////
		//add additional refund information if status = Declined
		if (getrbtnDecline().isSelected())
		{
			lhtHashTable.put(
				"ConvFee",
				caInetData.getCompleteRegRenData().getConvFeeString());
			lhtHashTable.put(
				"RefAmt",
				caInetData
					.getCompleteRegRenData()
					.getPaymentAmtString());
			lhtHashTable.put(
				"TraceNo",
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getTraceNumber());
			// defect 9878
			//			changes were backed out			
			// end defect 9878			
			lhtHashTable.put(
				"PymntOrderId",
				caInetData.getCompleteRegRenData().getPymntOrderId());
		}
		return doProcessData(lhtHashTable);
	}

	/**
	 * Refresh the data on the frame.
	 * 
	 * @param aaData
	 */
	private void refreshData(Object aaData)
	{
		if (aaData != null && aaData instanceof InternetRegRecData)
		{
			caInetData = (InternetRegRecData) aaData;
			// defect 8359 
			// Do not resetGUI if View/Change Address
			if (!cbGoingToAddress)
			{
				resetGUI();
			}
			// end defect 8359
			getlblNameOnRecord().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getOwnerName()
					.toUpperCase());
			getlblApplicantName().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getRecipientName()
					.toUpperCase());

			// defect 10420 
			getlblAddress().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress()
					.getCityStateCntryZip());
			// end defect 10420 

			//remember current plate no.
			// defect 11127 
			//			csPrevPlateNo =
			//				caInetData
			//					.getCompleteRegRenData()
			//					.getVehBaseData()
			//					.getPlateNo();
			// end defect 11127 
			getlblPlateNo().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehBaseData()
					.getPlateNo());
			getlblVIN().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehBaseData()
					.getVin());
			getlblDocumentNo().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehBaseData()
					.getDocNo());
			//calculate new expiration date
			// defect 9541	
			// defect 9565
			// use caInetData to compute exp mo/yr if caInetData is
			// not null
			int liNewExpYr = 0;
			int liNewExpMo = 0;
			// defect 10895
			// am getting caTransData for all statuses now
//			if (caTransData == null)
//			{
//
//			liNewExpYr =
//					Integer
//						.valueOf(
//							caInetData
//								.getCompleteRegRenData()
//								.getVehDescData()
//								.getExpYr())
//						.intValue()
//						+ 1;
//				liNewExpMo =
//					Integer
//						.valueOf(
//							caInetData
//								.getCompleteRegRenData()
//								.getVehDescData()
//								.getExpMo())
//						.intValue();
//			}
//			else
			if (caTransData != null)
			// end defect 10895
				// if (caTransData != null)
				{
				// end defect 9565
				liNewExpYr =
					Integer
						.valueOf(
							caTransData
								.getRegFeesData()
								.getExpMoYrMin()
								.substring(
								3,
								7))
						.intValue();
				liNewExpMo =
					Integer
						.valueOf(
							caTransData
								.getRegFeesData()
								.getExpMoYrMin()
								.substring(
								0,
								2))
						.intValue();
			}
			// end defect 9541		
			String lsNewExpDate =
				Integer.toString(liNewExpMo)
					+ "/"
					+ Integer.toString(liNewExpYr);
			getlblNewExpDate().setText(lsNewExpDate);
			getlblPlateAge().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getPlateAge());
			getlblEmptyWt().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getEmptyWeight());
			getlblCarryingCapacity().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getCarryingCapacity());
			getlblGrossWt().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getGrossWeight());
			getlblTonnage().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehDescData()
					.getTonnage());
			getlblTraceNo().setText(
				caInetData
					.getCompleteRegRenData()
					.getVehUserData()
					.getTraceNumber());
			//set date display (leave blank if invalid)
			if (Integer
				.parseInt(
					caInetData
						.getCompleteRegRenData()
						.getVehDescData()
						.getTitleIssDt())
				> 0)
			{
				RTSDate laTitleDate =
					new RTSDate(
						1,
						Integer.parseInt(
							caInetData
								.getCompleteRegRenData()
								.getVehDescData()
								.getTitleIssDt()));
				String lsTitleDt = laTitleDate.toString();
				getlblTitleIssuanceDate().setText(lsTitleDt);
			}

			//set status
			// defect 8359 
			// Do Not Reset Status if going to View/Change Address
			if (!cbGoingToAddress)
			{
				if (caInetData
					.getStatus()
					.equals(
						CommonConstants.UNPAID
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.UNPAID_LBL);
					getBtnAddress().setEnabled(false);
					getBtnInsurance().setEnabled(false);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.NEW
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.NEW_LBL);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.HOLD
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.HOLD_LBL);
					if (csPrevSelection.length() <= 1)
					{
						// when no selection, use what the data has.
						getrbtnHold().setSelected(true);
					}

					if (caInetData.getSendEmail() == 1)
					{
						getchkboxSendEmail().setSelected(true);
					}

					String lsHoldCode =
						getReasonCodeString(caInetData.getReasonCd());
					getcmbHold().setSelectedItem(lsHoldCode);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.DECLINED_REFUND_PENDING
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.DECL_PENDING_LBL);
					getrbtnDecline().setSelected(true);
					String lsDeclineCode =
						getReasonCodeString(caInetData.getReasonCd());
					getcmbDecline().setSelectedItem(lsDeclineCode);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.APPROVED
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.APPROVED_LBL);
					getrbtnApprove().setSelected(true);
					getlblTransID().setText(
						caInetData.getTransactionId());
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.DECLINED_REFUND_FAILED
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.DECL_FAILED_LBL);
					String lsDeclineCode =
						getReasonCodeString(caInetData.getReasonCd());
					getcmbDecline().setSelectedItem(lsDeclineCode);
					getrbtnDecline().setSelected(true);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.DECLINED_REFUND_APPROVED
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.DECL_SUCCESS_LBL);
					String lsDeclineCode =
						getReasonCodeString(caInetData.getReasonCd());
					getcmbDecline().setSelectedItem(lsDeclineCode);
					getrbtnDecline().setSelected(true);
				}
				else if (
					caInetData.getStatus().equals(
						CommonConstants.IN_PROCESS
							+ CommonConstant.STR_SPACE_EMPTY))
				{
					getlblStatus().setText(
						RegRenProcessingConstants.IN_PROC_LBL);
				}
			}
			// end defect 8359
			// restore previous selection
			resumePrevSelection();
			// disallow processing of these statuses
			if (viewOnly())
				// IN_PROCESS ones are allowed processing
			{
				//disable all editing
				cbViewOnly = true;
				disableEdit();
			}

			// PCR44, insurance not required, begin
			if (!caInetData
				.getCompleteRegRenData()
				.isInsuranceRequired())
			{
				getBtnInsurance().setEnabled(false);
			}
			// begin defect 6051 change
			else
			{
				getBtnInsurance().setEnabled(true);
			}
			// end defect 6051 change

			// defect 8359 
			// Do not reset Comments if Going to Address 
			// PCR44, insurance not required, end
			// Place here! location relevant -- looks like the java 
			// implementation is not that good.        
			// defect 4559
			// defect 9034
			// if there are reason comments present - show them
			//			if (!cbGoingToAddress)
			//			{
			gettxtAreaComments().setText(
				caInetData.getReasonComments());
			//			}
			// end defect 9034
			// end defect 8359 
			// defect 7889
			// Changed setVisible to setVisibleRTS
			//setVisible(true);

			// defect 7889
			// Changed setVisible to setVisibleRTS
			//setVisible(true);
			setVisibleRTS(true);
			// end defect 7889
		}
	}

	/**
	 * Reset the Graphic User Interface back to the way it was when 
	 * the screen was at first.
	 */
	private void resetGUI()
	{
		getlblNameOnRecord().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblApplicantName().setText(CommonConstant.STR_SPACE_EMPTY);

		// defect 10420 
		getlblAddress().setText(CommonConstant.STR_SPACE_EMPTY);
		// end defect 10420 

		getlblPlateNo().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblVIN().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblDocumentNo().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblNewExpDate().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblTitleIssuanceDate().setText(
			CommonConstant.STR_SPACE_EMPTY);
		getlblPlateAge().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblEmptyWt().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblCarryingCapacity().setText(
			CommonConstant.STR_SPACE_EMPTY);
		getlblGrossWt().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblTonnage().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblTraceNo().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblTransID().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblStatus().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtAreaComments().setText(CommonConstant.STR_SPACE_EMPTY);
		//must remove radio-buttons from group before deselecting
		//else one radio-button will always stay selected
		caBtnGroup.remove(getrbtnApprove());
		caBtnGroup.remove(getrbtnHold());
		caBtnGroup.remove(getrbtnDecline());
		getrbtnApprove().setSelected(false);
		getrbtnHold().setSelected(false);
		getrbtnDecline().setSelected(false);
		//add them back in
		setButtonGroup();
		getcmbHold().setSelectedIndex(0);
		getchkboxSendEmail().setSelected(false);
		getcmbDecline().setSelectedIndex(0);
	}

	/**
	 * When user go to View/Change Address Screen,
	 * the user's selection before going to
	 * the screen is preserved in prevSelection.
	 * When the user comes back, need to resume what
	 * it was.
	*/
	private void resumePrevSelection()
	{
		if (csPrevSelection.equalsIgnoreCase(APPROVE))
		{
			getrbtnApprove().setSelected(true);
		}
		if (csPrevSelection.equalsIgnoreCase(DECLINE))
		{
			getrbtnDecline().setSelected(true);
		}
		if (csPrevSelection.equalsIgnoreCase(HOLD))
		{
			getrbtnHold().setSelected(true);
		}
	}
	/**
	 * Set Data From Address Change 
	 *
	 */
	private void setDataFromAddressChange()
	{
		if (caInetData.getCompleteRegRenData() != null)
		{
			VehicleUserData laVehUserData = caInetData.getCompleteRegRenData().getVehUserData(); 
			AddressData laAddr = (AddressData) UtilityMethods.copy(laVehUserData.getAddress());
			RegistrationData laRegData = caTransData.getVehicleInfo().getRegData(); 
			laRegData.setRenwlMailAddr(laAddr); 
			laRegData.setRecpntEMail(laVehUserData.getEmail()); 
			laRegData.setRecpntName(laVehUserData.getRecipientName());
		}
	}

	/**
	 * For approve processing, prepare the inventory items and 
	 * transfer control to INV007.
	 */
	private void selectInventory()
	{
		//set county id
		caTransData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());

		//get vehicle
		MFVehicleData laMFVehicleData = caTransData.getVehicleInfo();
		MFVehicleData laMFVehicleDataOriginal =
			(MFVehicleData) UtilityMethods.copy(laMFVehicleData);
		caTransData.setOrgVehicleInfo(laMFVehicleDataOriginal);

		laMFVehicleData.getRegData().setRenwlMailAddr(
			caInetData
				.getCompleteRegRenData()
				.getVehUserData()
				.getAddress());

		laMFVehicleData.getRegData().setRecpntName(
			caInetData
				.getCompleteRegRenData()
				.getVehUserData()
				.getRecipientName());

		laMFVehicleData.getRegData().setResComptCntyNo(
			caInetData
				.getCompleteRegRenData()
				.getVehBaseData()
				.getOwnerCounty());

		laMFVehicleData.getRegData().setRecpntEMail(
			caInetData
				.getCompleteRegRenData()
				.getVehUserData()
				.getEmail());

		// defect 10508 
		laMFVehicleData.getRegData().setEMailRenwlReqCd(
			caInetData
				.getCompleteRegRenData()
				.getVehUserData()
				.getEMailRenwlReqCd());
		// end defect 10508 

		//update vehicle expiration date
		// defect 9541
		//int liNewRegExpYear =
		//	laMFVehicleData.getRegData().getRegExpYr() + 1;
		int liNewRegExpYear =
			Integer
				.valueOf(
					caTransData
						.getRegFeesData()
						.getExpMoYrMin()
						.substring(
						3,
						7))
				.intValue();
		// end defect 9541

		// add vehicle info
		caTransData.setVehicleInfo(laMFVehicleData);

		// defect 9119
		// defect 4301
		// Part 1, begin
		// (1) Phase 1 part: VCRegistrationItemsINV007, handle IRENEW
		// (2) Phase 2 part: 

		// defect 9541
		// int liToDfltMo = caTransData.getRegFeesData().getToMonthDflt();
		int liToDfltMo =
			Integer
				.valueOf(
					caTransData
						.getRegFeesData()
						.getExpMoYrMin()
						.substring(
						0,
						2))
				.intValue();
		// end defect 9541	

		if (liToDfltMo != 0)
		{
			caTransData.getVehicleInfo().getRegData().setRegExpMo(
				liToDfltMo);
			caTransData.setExpMo(liToDfltMo);
		}
		caTransData.setExpYr(liNewRegExpYear);
		// end defect 4301

		boolean lbAnnualSpclPlt = false;
		boolean lbSpclPlt = false;
		boolean lbStickerPrintable = false;

		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(
				laMFVehicleData.getRegData().getRegPltCd());

		if (laPlateTypeData != null)
		{
			lbSpclPlt =
				!laPlateTypeData.getPltOwnrshpCd().equals(
					SpecialPlatesConstant.VEHICLE);
			lbAnnualSpclPlt =
				lbSpclPlt && laPlateTypeData.getAnnualPltIndi() == 1;
		}

		// Vector for Inventory to be issued
		Vector lvInvItems = new Vector();

		if (!lbAnnualSpclPlt)
		{
			// Create Process Inventory Data Object; Add to Vector 
			ProcessInventoryData laInvData = new ProcessInventoryData();
			laInvData.setSubstaId(SystemProperty.getSubStationId());
			laInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laInvData.setTransWsId(
				String.valueOf(SystemProperty.getWorkStationId()));
			laInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
			laInvData.setItmCd(getInvItmCd(laMFVehicleData));
			laInvData.setInvItmYr(liNewRegExpYear);
			lvInvItems.add(laInvData);

			lbStickerPrintable =
				StickerPrintingUtilities.isStickerPrintable(laInvData);
		}

		boolean lbMandPltRepl = false;

		// defect 9541
		// If Annual Spcl Plt 
		//    Age >= Mandatory Replacement Age && 
		//       SpclPlt || NeedsProgramCd = R   
		if (laPlateTypeData != null)
		{
			if (lbAnnualSpclPlt
				|| ((laMFVehicleData.getRegData().getRegPltAge(true)
					>= laPlateTypeData.getMandPltReplAge())
					&& (lbSpclPlt
						|| laPlateTypeData.getNeedsProgramCd().equals(
							"R"))))
			{
				// end defect 9541		

				if (lbSpclPlt)
				{
					SpecialPlatesRegisData laSpclPltRegData =
						laMFVehicleData.getSpclPltRegisData();
					laSpclPltRegData.setMFGStatusCd(
						SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);
					laSpclPltRegData.setMFGDate(TransCdConstant.IRENEW);
					// Owner Supplied Info
					caTransData.setOwnrSuppliedPltNo(
						laSpclPltRegData.getRegPltNo());
					caTransData
						.getVehicleInfo()
						.getRegData()
						.setOwnrSuppliedExpYr(
						liNewRegExpYear);
					if (lbAnnualSpclPlt)
					{
						laSpclPltRegData.setInvItmYr(liNewRegExpYear);
					}
					RTSException leRTSEx =
						UtilityMethods.createSpclPltMfgInfoMsg(
							laSpclPltRegData);
					leRTSEx.displayError(this);
				}
				else
				{
					lbMandPltRepl = true;
					ProcessInventoryData laInvData2 =
						new ProcessInventoryData();
					laInvData2.setSubstaId(
						SystemProperty.getSubStationId());
					laInvData2.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laInvData2.setTransWsId(
						String.valueOf(
							SystemProperty.getWorkStationId()));
					laInvData2.setTransEmpId(
						SystemProperty.getCurrentEmpId());
					laInvData2.setItmCd(
						laMFVehicleData.getRegData().getRegPltCd());
					// do not set InvItmYr
					// defect 4301
					// Part 3, begin, in the RTS receipt, PrevPltNo not correct
					// Similar to REG003, assume the PrevPltNo can be changed 
					// here.
					laMFVehicleData.getRegData().setPrevPltNo(
						laMFVehicleData.getRegData().getRegPltNo());
					// end defect 4301

					// add inventory data to Vector    
					lvInvItems.add(laInvData2);
				}
				// end defect 9119
			}
		}

		// defect 9638				
		//if (laMFVehicleData.getRegData().getRegClassCd() == 10)
		if (laPlateTypeData.getAnnualPltIndi() == 1)
		{
			laMFVehicleData.getRegData().setPrevPltNo(
				laMFVehicleData.getRegData().getRegPltNo());
		}
		// end defect 9638			

		// end defect 9085

		//			// defect 4301
		//			// Part 4, begin
		//			if (getAnnualPltIndi(laMFVehicleData) == 1)
		//			{
		//				laMFVehicleData.getRegData().setPrevPltNo(
		//					laMFVehicleData.getRegData().getRegPltNo());
		//			}
		//			// end defect 4301

		// Vector of ProcessInventoryData holding the inventory that 
		// have been allocated.
		Vector lvInvAllocItems = new Vector();
		caTransData.setInvItms(lvInvItems);
		caTransData.setAllocInvItms(lvInvAllocItems);
		caTransData.setTransCode(TransCdConstant.IRENEW);

		// defect 7221
		// Call to server to Issue Inventory if not Special Plate 
		// AND either (non-printable sticker or must replace plate)  
		if (!lbSpclPlt && (!lbStickerPrintable || lbMandPltRepl))
		{
			RegRenProcessingClientBusiness laRegRenProcClinetBus =
				new RegRenProcessingClientBusiness();
			try
			{
				CompleteTransactionData laTransData =
					laRegRenProcClinetBus.prefillInventory(caTransData);
				if (laTransData instanceof CompleteTransactionData)
					caTransData = laTransData;
			}
			catch (RTSException aeRTSEx)
			{
				// defect 3700
				// DB down handling	    
				if (!(aeRTSEx
					.getMsgType()
					.equals(RTSException.SERVER_DOWN)
					|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)))
				{
					// end defect 3700

					// Errors other than DB/server down, report.
					aeRTSEx.displayError(this);
					return;
				}
			}
		}
		// end defect 7221
		// end defect 9119  

		// defect 7889
		// No need to check process mode because it always got set to
		// the same value anyway.
		caTransData.setInternetScreen(ScreenConstant.REG103);
		getController().processData(
			AbstractViewController.ENTER,
			caTransData);
		// end defect 7889
	}

	/**
	 * Sets up the button group.
	 */
	private void setButtonGroup()
	{
		if (caBtnGroup == null)
		{
			// defect 7889
			caBtnGroup = new RTSButtonGroup();
			// end defect 7889
		}

		caBtnGroup.add(getrbtnApprove());
		caBtnGroup.add(getrbtnHold());
		caBtnGroup.add(getrbtnDecline());

		getrbtnApprove().addItemListener(new ItemListener()
		{
			/**
			 * This handles Item state changes.
			 * 
			 * @param aaIE ItemEvent
			 */
			public void itemStateChanged(ItemEvent aaIE)
			{
				if (getrbtnApprove().isSelected())
				{
					csPrevSelection = APPROVE;
					getcmbHold().setSelectedIndex(0);
					getcmbHold().setEnabled(false);
					// resume to its original color
					// if set to red because of
					// previous non-selection 
					getcmbHold().setForeground(Color.black);
					getcmbHold().setBackground(
						new Color(204, 204, 204));
					getchkboxSendEmail().setSelected(false);
					getchkboxSendEmail().setEnabled(false);
					getcmbDecline().setSelectedIndex(0);
					getcmbDecline().setEnabled(false);
					getcmbDecline().setForeground(Color.black);
					getcmbDecline().setBackground(
						new Color(204, 204, 204));
					gettxtAreaComments().setEnabled(false);
					gettxtAreaComments().setText(
						CommonConstant.STR_SPACE_EMPTY);
				}
			}
		});

		getrbtnDecline().addItemListener(new ItemListener()
		{
			/**
			 * This handles Item state changes.
			 * 
			 * @param aaIE ItemEvent
			 */
			public void itemStateChanged(ItemEvent aaIE)
			{
				if (getrbtnDecline().isSelected())
				{
					csPrevSelection = DECLINE;
					getcmbHold().setSelectedIndex(0);
					getcmbHold().setEnabled(false);
					getcmbHold().setForeground(Color.black);
					getcmbHold().setBackground(
						new Color(204, 204, 204));
					getchkboxSendEmail().setSelected(false);
					getchkboxSendEmail().setEnabled(false);
					getcmbDecline().setEnabled(true);
					gettxtAreaComments().setEnabled(true);
					gettxtAreaComments().setText(
						CommonConstant.STR_SPACE_EMPTY);
					getJlblComments().setLabelFor(gettxtAreaComments());
					getcmbDecline().requestFocus();
				}
			}
		});

		getrbtnHold().addItemListener(new ItemListener()
		{
			/**
			 * This handles Item state changes.
			 * 
			 * @param aaIE ItemEvent
			 */
			public void itemStateChanged(ItemEvent aaIE)
			{
				if (getrbtnHold().isSelected())
				{
					csPrevSelection = HOLD;
					getcmbDecline().setSelectedIndex(0);
					getcmbDecline().setEnabled(false);
					getcmbDecline().setForeground(Color.black);
					getcmbDecline().setBackground(
						new Color(204, 204, 204));
					getcmbHold().setEnabled(true);
					getchkboxSendEmail().setEnabled(true);
					gettxtAreaComments().setEnabled(true);
					getJlblComments().setLabelFor(gettxtAreaComments());
					getcmbHold().requestFocus();
				}
			}
		});

		return;
	}

	/**
	 * Set the data from another frame or from menu.
	 * Called by methods initializeNext, by instantiation of this 
	 * class in FrmProcessVehicleREG103(obj), and from the VC
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		// defect 7080
		// Added try catch in order to add a finally to handle setting
		// the focus on Insurance when we leave the frame.  We leave the
		// frame only for approve and view/change address.  Hold and 
		// decline go through action performed.
		try
		{
			// When from view/change address, data alredy there.

			//defect 6099
			// moved up so usuable by whole method
			RTSException leValidEx = new RTSException();
			// end defect 6099
			if (aaData == null)
			{
				// defect 8245	
				aaData = (InternetRegRecData) getNextRecord();
				// when only the data record is missing-get next record
				if (caInetData != null && caTransData == null)
				{
					while (true)
					{
						if (goToNext())
						{
							return;
						}
						else
						{
							if (caInetData != null
								&& caTransData == null)
							{
								continue;
							}
							else
							{
								closeDlg();
								return;
							}
						}
					}
				}
				//end defect 8245

				// defect 6099
				// this "if" block pulled from bottom of method so the 
				// check can be made right away, and other checks below 
				// won't be made

				if (aaData == null)
				{
					// There is no record 	
					if (cbUserAgreeSwitch)
					{
						// when from the above getNextRecord(), if there 
						// is no data when user agrees to switch to another 
						// queue, give message.
						// defect 5333
						displayMessage(987);
						// end defect 5333
					}
					// process like a cancel key pressed
					closeDlg();
				}
				// end defect 6099
				// end defect 3700
			}

			if (aaData instanceof Boolean
				&& ((Boolean) aaData).booleanValue() == true)
			{
				// Went to Phase1 INV007 screen, return true.
				// if return false, will not come to here.
				// handled in the view controller.
				// acknowledge update       
				// defect 8749 
				leValidEx.addException(
				//new RTSException(978),
				new RTSException(ErrorsConstant.ERR_NUM_RECORD_UPDATED),
				// end defect 8749 
				leValidEx.getFirstComponent());
				leValidEx.displayError(this);

				// defect 6099
				// Check value of  the isDBReady, after the attempted update
				// to the DB from the INV screen
				if (RTSApplicationController.isDBReady())
				{
					if (goToNext())
					{
						return;
					}
					else
					{
						closeDlg();
					}
				}
				else
				{
					// defect 8749
					//displayMessage(618);
					displayMessage(
						ErrorsConstant.ERR_NUM_DBSERVER_UNAVAILABLE);
					// end defect 8749 
					closeDlg();
				}
			}
			// end defect 6099

			// defect 8247
			// We were looking for vector but now it is a hashtable
			// that contains the vector as well as the search keys used
			// when searching.
			//if (aaData instanceof Vector)
			if (aaData instanceof Hashtable)
			{
				// comes from FrmSearchResultsREG102
				// setting the search keys in a class level variable
				// for later use
				chtSearchParams = (Hashtable) aaData;
				aaData =
					acceptData(
						(Vector) chtSearchParams.get("ReturnValues"));

				if (aaData instanceof Vector)
				{
					aaData = acceptData((Vector) aaData);
				}
			}
			// end defect 8247

			// defect 6099
			// check for graph since refreshData only takes
			// InternetRegRecData
			if (aaData instanceof InternetRegRecData)
			{
				// end defect 6099
				// comes from FrmSearchResultsREG102
				refreshData(aaData);
			}
			//end defect 3700
		}
		// Since this frame can be ran in search mode and while
		// processing a queue therefore we have to decide how to 
		// display the msg.
		catch (Exception aeEx)
		{
			RTSException leNewEx =
				new RTSException(RTSException.SYSTEM_ERROR, aeEx);

			if (this.isVisible())
			{
				leNewEx.displayError(this);
			}
			else
			{
				RTSDialogBox laRTSDialogBox =
					getController().getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					leNewEx.displayError(laRTSDialogBox);
				}
				else
				{
					leNewEx.displayError(
						getController().getMediator().getDesktop());
				}
			}
		}
		finally
		{
			if (cbGoingToAddress)
			{
				getBtnAddress().requestFocus();
				cbGoingToAddress = false;
			}
			else
			{
				// defect 9034
				if (caInetData != null)
				{
					if (caInetData
						.getCompleteRegRenData()
						.isInsuranceRequired())
					{
						getBtnInsurance().requestFocus();
					}
					else
					{
						getBtnAddress().requestFocus();
					}
					// end defect 9034
				}
			}
		}
		// end defect 7080
	}

	/**
	 * This method sets the mode that we are in.
	 * 	NEW
	 * 	HOLD
	 * 	SEARCH
	 * 
	 * @param asMode String
	 */
	private void setMode(String asMode)
	{
		asMode = asMode.toUpperCase();
		// defect 6099 - changed var name from ciMode to ciProcessMode
		// defect 8736
		if (asMode.indexOf(PROCESS_NEW) > 0)
		{
			ciProcessMode = NEXT_NEW;
		}
		else if (asMode.indexOf(PROCESS_HOLD) > 0)
		{
			ciProcessMode = NEXT_HOLD;
		}
		else if (asMode.indexOf(PROCESS_SEARCH) > 0)
		{
			ciProcessMode = SEARCH;
		}
		// end defect 8736
	}

	/**
	 * This method will undo a checkout of a vehicle record 
	 * for processing.
	 */
	private void undoCheckout()
	{
		try
		{
			RegRenProcessingClientBusiness laRegRenProcClientBus =
				new RegRenProcessingClientBusiness();
			cbIsLastTransCached =
				!laRegRenProcClientBus.undoCheckout(caInetData);
		}
		catch (Exception aeEx)
		{
			// error other than db/server is down, report
			RTSException leRTSValidEx = new RTSException();
			// defect 8749 
			leRTSValidEx.addException(
			//new RTSException(972),
			new RTSException(
				ErrorsConstant.ERR_NUM_CONNECTION_FAILURE_HAS_OCCURRED),
				leRTSValidEx.getFirstComponent());
			// end defect 8749 
			leRTSValidEx.displayError(this);
		}
	}

	/**
	 * This method handles the internet processing queue when one 
	 * of the queues (new, hold) runs out of records.
	 *
	 * @return boolean
	 */
	private boolean userAgreeSwitch()
	{
		RTSException leRTSExDlgContinue;
		// defect 6099
		// ciProcessMode == 1 - NEXT_NEW - Process next record 
		// in "new" status
		// ciProcessMode == 2 - NEXT_HOLD - Process next record 
		// in "hold" status
		if (ciProcessMode == NEXT_NEW)
		{
			ciProcessMode = NEXT_HOLD;
			// defect 8749
			//leRTSExDlgContinue = new RTSException(976);
			leRTSExDlgContinue =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_NEW_HOLD_DISPLAYED);
			// end defect 8749
		}
		else
		{
			ciProcessMode = NEXT_NEW;
			// defect 8749 
			//leRTSExDlgContinue = new RTSException(977);
			leRTSExDlgContinue =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_HOLD_NEW_DISPLAYED);
			// end defect 8749 
		}

		int liChoice = 0;

		leRTSExDlgContinue.setBeep(RTSException.BEEP);

		if (this.isVisible())
		{
			liChoice = leRTSExDlgContinue.displayError(this);
		}
		else
		{
			RTSDialogBox laRTSDialogBox =
				getController().getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				liChoice =
					leRTSExDlgContinue.displayError(laRTSDialogBox);
			}
			else
			{
				// move beep so it happens when any of these
				// errors displays
				// if (dlgContinue.getCode() == 977)
				//    UtilityMethods.beep();
				// end defect 6531
				liChoice =
					leRTSExDlgContinue.displayError(
						getController().getMediator().getDesktop());
			}
		}

		if (liChoice == RTSException.NO)
		{
			cbUserAgreeSwitch = false;
		}
		else
		{
			cbUserAgreeSwitch = true;
		}

		return cbUserAgreeSwitch;
	}

	/**
	 * Determine if a transaction is view only, 
	 * 
	 * @return boolean
	 */
	private boolean viewOnly()
	{
		if (caInetData
			.getStatus()
			.equals(
				CommonConstants.APPROVED
					+ CommonConstant.STR_SPACE_EMPTY)
			|| caInetData.getStatus().equals(
				CommonConstants.DECLINED_REFUND_PENDING
					+ CommonConstant.STR_SPACE_EMPTY)
			|| caInetData.getStatus().equals(
				CommonConstants.DECLINED_REFUND_FAILED
					+ CommonConstant.STR_SPACE_EMPTY)
			|| caInetData.getStatus().equals(
				CommonConstants.DECLINED_REFUND_APPROVED
					+ CommonConstant.STR_SPACE_EMPTY)
			|| caInetData.getStatus().equals(
				CommonConstants.UNPAID
					+ CommonConstant.STR_SPACE_EMPTY))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
