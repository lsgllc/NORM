package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.webapps.registrationrenewal.RegRenClientUtility;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 *
 * FrmVehicleSearchREG101.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		6/17/2002	Rebuilt visual composition editor view by 
 * 							going into VCE, bean, construct visuals from
 * 							source, resized the visual correctly, then
 * 							bean, regenerate the code (getBuilderData
 * 							method). Because this process can add
 * 							undesirable code, and the getBuilderData
 * 							method is what is needed for VCE, I saved
 * 							the getBuilderData method to an external
 * 							file, checked out the most recent version of
 * 							the class from Clear Case, and re-copied in
 * 							the getBuilderData method and saved it in
 * 							the class.
 * B Brown		10/23/2002	putting and accessing Phase 2 error msgs in
 * 							rts_err_msgs).  Canged methods: processData,
 * 							and validate search keys 
 * 							defect 4205
 * B Brown		11/20/2002  Rebuilt the combobox cmbstatus in VCE, so
 * 							the dropdown list would properly show
 * 							declined(Chargeback successfull) when chosen
 * 							defect 5050
 * B Brown		12/03/2002  Added hot keys to all fields. Added a
 * Clifford 	12/17/2002	setDisplayedMnemonic to the JLabel methods
 * 							and setLabelFor statement in the
 * 							corresponding get methods. Also changed the
 * 							fixHotKey method to enable the all hot keys
 * 							from within the two rts date fields.
 * 							defect 4542
 * Jeff S.		06/20/2005	Added a boolean to the hashtable of search 
 * 							params that helps determine if we are in a
 * 							view only situation.  This is used in the VC
 * 							REG103 to determine if we need to re-search.
 * 							modify processData()
 * 							defect 8247 Ver 5.2.3
 * Jeff S.		06/28/2005	Renamed class to show the frame number. Also
 * 							removed the keyListener methods because RTS
 * 							Dialog box handles all of the things that
 * 							where handled in those methods.  Tried to
 * 							make these classes match those frames in the
 * 							rest of RTS.
 *							defect 7889 ver 5.2.3
 * Jeff S.		07/12/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							Added Constants.
 * 							modify actionPerformed(), handleException()
 * 								keyReleased(), processData(), main()
 * 							deprecate getBuilderData(), dateError(), 
 * 								fixHotKey()
 *							defect 7889 ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/31/2005	Remove mnemonic for Cancel
 * 							modify getBtnCancel()
 * 							defect 8361 Ver 5.2.3 
 * K Harrell	10/10/2005	Assign Input type as RTSInputField.DEFAULT,
 * 							i.e. no edit.	
 * 							modify getTxtFirstName(),getTxtLastName(),
 * 							getTxtMiddleName()
 * 							defect 7127 Ver 5.2.3
 * K Harrell	02/11/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates  
 * Min Wang  	06/18/2007	modify fields and screen.
 * 							defect 8768 Ver Special Plates
 * B Brown		01/28/2009	Display a record not found message instead 
 * 							of an empty FrmSearchResultsREG102 screen 
 * 							when a record is not found on the database.
 * 							add dispNoRecsFound()
 * 							defect 8765 Ver Defect_POS_D
 * B Brown		01/30/2009	Move implementation of no record found error
 * 							message to VCVehicleSearchREG101.
 * 							deprecate dispNoRecsFound()
 * 							defect 8765 Ver Defect_POS_D
 * K Harrell	06/23/2009	Cursor Movement Keys did not work. Extreme 
 * 							additional class cleanup for naming 
 * 							standards. 
 * 							add caButtonGroup
 * 							delete SEARCH_KEY_ERROR_NO,  (950)
 * 							 SEARCH_DATE_ERROR_NO,       (981)
 * 							 SEARCH_THIRTY_ERROR_NO,     (959)
 * 							 SEARCH_NULL_END_DATE_ERROR_NO, (955)
 * 							 SEARCH_SIXTY_DAY_ERROR_NO,   (982)
 * 							 SEARCH_NULL_BEGIN_DATE_ERROR_NO, (954)
 * 							 SEARCH_TRANSID_LENGTH_ERROR_NO, (957)
 * 							 DATE_LBL, EMPTY_DATE
 * 							delete dispNoRecsFound()
 * 							modify getFrmVehicleSearchContentPane1(), 
 * 							 processData(), validateSearchKeys()
 * 							defect 8763 Ver Defect_POS_F  	
 * ---------------------------------------------------------------------
 */

/**
 * Allows the county office to search for internet registration renewals
 * by status, plato no transID, etc to find niternet regis renewals to
 * approve or disapprove, or to put on hold.
 *
 * @version	Defect_POS_F 	06/23/2009
 * @author	George Donoso
 * <br>Creation Date:		01/10/2002 09:28:14  
 */
public class FrmVehicleSearchREG101
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnReset = null;
	private RTSButton ivjbtnSearch = null;
	private JComboBox ivjcomboStatus = null;
	private JPanel ivjFrmVehicleSearchContentPane1 = null;
	private JPanel ivjJPanelSearchItems = null;
	private JLabel ivjstclblBeginDate = null;
	private JLabel ivjstclblEndDate = null;
	private JLabel ivjstclblFName = null;
	private JLabel ivjstclblMName = null;
	private JLabel ivjstclblName = null;
	private JLabel ivjstclblPlateNo = null;
	private JLabel ivjstclblStatus = null;
	private JLabel ivjstclblTraceNo = null;
	private JLabel ivjstclblTransId = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private RTSInputField ivjtxtFirstName = null;
	private RTSInputField ivjtxtLastName = null;
	private RTSInputField ivjtxtLicensePlate = null;
	private RTSInputField ivjtxtMiddleName = null;
	private RTSInputField ivjtxtTraceNumber = null;
	private RTSInputField ivjtxtTransactionID = null;

	private Object caData = null;

	// defect 8763
	RTSButtonGroup caButtonGroup = new RTSButtonGroup();
	// end defect 8763 

	private static final String BEGIN_LBL = "Begin Date:";
	private static final String CANCEL = "Cancel";
	private static final String CNTYSTATUSCD = "CntyStatusCd";
	private static final String EMPTY_STRING = "";
	private static final String END_LBL = "End Date:";
	private static final String ENDDATE = "EndDate";
	private static final String FIRST_NAME_LBL = "First Name:";
	private static final String FRM_TITLE =
		"Vehicle Record Search     REG101";
	private static final String FRSTNAME = "FrstName";
	private static final String HELP = "Help";
	private static final String ITRNTTRACENO = "ItrntTraceNo";
	private static final String LAST_CO_NAME_LBL =
		"Last Name/Company Name:";
	private static final String LASTNAME = "LastName";
	private static final String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of JDialog";
	private static final String MIDDLE_NAME_LBL = "Middle Name:";
	private static final String MIDLNAME = "MidlName";
	private static final String OFCISSUANCENO = "OfcIssuanceNo";
	private static final String PLATENO_LBL = "Plate No:";
	private static final String REGPLTNO = "RegPltNo";
	private static final String RESET = "Reset";
	private static final String SEARCH = "Search";
	private static final String SEARCH_LBL = "Enter Search Criteria:";
	private static final String SPACE = " ";
	private static final String STARTDATE = "StartDate";
	private static final String STATUS_LBL = "Status:";
	private static final String THIRTY = "30";
	private static final String TRACENO_LBL = "Internet Trace No:";
	private static final String TRANSID = "TransId";
	private static final String TRANSID_LBL = "Transaction Id:";
	private static final String VIEWONLY = "ViewOnly";

	// defect 8763 
	//private static final String DATE_LBL = "Date";
	//
	//	private static final int SEARCH_KEY_ERROR_NO = 950;
	//	private static final int SEARCH_DATE_ERROR_NO = 981;
	//	private static final int SEARCH_THIRTY_ERROR_NO = 959;
	//	private static final int SEARCH_NULL_END_DATE_ERROR_NO = 955;
	//	private static final int SEARCH_SIXTY_DAY_ERROR_NO = 982;
	//	private static final int SEARCH_NULL_BEGIN_DATE_ERROR_NO = 954;
	//	private static final int SEARCH_TRANSID_LENGTH_ERROR_NO = 957;
	// end defect 8763

	/**            
	 * main entrypoint starts the part when it is run as an application
	 *            
	 * @param aarrArgs String[]            
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmVehicleSearchREG101 aaFrmVehicleSearch;
			aaFrmVehicleSearch = new FrmVehicleSearchREG101();
			aaFrmVehicleSearch.setModal(true);
			aaFrmVehicleSearch.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			aaFrmVehicleSearch.show();
			Insets insets = aaFrmVehicleSearch.getInsets();
			aaFrmVehicleSearch.setSize(
				aaFrmVehicleSearch.getWidth()
					+ insets.left
					+ insets.right,
				aaFrmVehicleSearch.getHeight()
					+ insets.top
					+ insets.bottom);
			aaFrmVehicleSearch.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(MAIN_EXCEPTION_MSG);
			aeEx.printStackTrace(System.out);
		}
	}

	/**            
	 * FrmVehicleSearchREG101 constructor 
	 */
	public FrmVehicleSearchREG101()
	{
		super();
		initialize();
	}

	/**            
	 * FrmVehicleSearchREG101 constructor 
	 *             
	 * @param aaOwner Dialog            
	 */
	public FrmVehicleSearchREG101(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**            
	 * FrmVehicleSearchREG101 constructor
	 *         
	 * @param aaOwner Frame            
	 */
	public FrmVehicleSearchREG101(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Handles action events that occur on components that add the
	 * action listener.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caData);
			}
			else if (aaAE.getSource() == getbtnSearch())
			{
				processData();
			}
			else if (aaAE.getSource() == getbtnReset())
			{
				reset();
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG101);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**            
	 * Return the ivjbtnCancel property value.   
	 *          
	 * @return RTSButton         
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("ivjbtnCancel");
				ivjbtnCancel.setText(CANCEL);
				ivjbtnCancel.setMaximumSize(new Dimension(73, 25));
				ivjbtnCancel.setMinimumSize(new Dimension(73, 25));
				ivjbtnCancel.setActionCommand(CANCEL);
				ivjbtnCancel.setBounds(264, 379, 90, 25);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnCancel;
	}

	/**            
	 * Return the ivjbtnHelp property value.      
	 *       
	 * @return RTSButton            
	 */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("ivjbtnHelp");
				ivjbtnHelp.setMnemonic(KeyEvent.VK_H);
				ivjbtnHelp.setText(HELP);
				ivjbtnHelp.setMaximumSize(new Dimension(59, 25));
				ivjbtnHelp.setMinimumSize(new Dimension(59, 25));
				ivjbtnHelp.setActionCommand(HELP);
				ivjbtnHelp.setBounds(366, 379, 90, 25);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnHelp;
	}

	/**   
	 * Return the ivjbtnReset property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnReset()
	{
		if (ivjbtnReset == null)
		{
			try
			{
				ivjbtnReset = new RTSButton();
				ivjbtnReset.setName("ivjbtnReset");
				ivjbtnReset.setMnemonic(KeyEvent.VK_R);
				ivjbtnReset.setText(RESET);
				ivjbtnReset.setMaximumSize(new Dimension(67, 25));
				ivjbtnReset.setMinimumSize(new Dimension(67, 25));
				ivjbtnReset.setActionCommand(RESET);
				ivjbtnReset.setBounds(162, 379, 90, 25);
				// user code begin {1}
				ivjbtnReset.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnReset;
	}

	/**    
	 * Return the ivjbtnSearch property value.  
	 *   
	 * @return RTSButton  
	 */
	private RTSButton getbtnSearch()
	{
		if (ivjbtnSearch == null)
		{
			try
			{
				ivjbtnSearch = new RTSButton();
				ivjbtnSearch.setName("ivjbtnSearch");
				ivjbtnSearch.setMnemonic(KeyEvent.VK_S);
				ivjbtnSearch.setText(SEARCH);
				ivjbtnSearch.setMaximumSize(new Dimension(75, 25));
				ivjbtnSearch.setMinimumSize(new Dimension(75, 25));
				ivjbtnSearch.setActionCommand(SEARCH);
				ivjbtnSearch.setBounds(60, 379, 90, 25);
				// user code begin {1}
				ivjbtnSearch.addActionListener(this);
				this.getRootPane().setDefaultButton(ivjbtnSearch);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnSearch;
	}

	/**
	 * Return the ivjcomboStatus property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboStatus()
	{
		if (ivjcomboStatus == null)
		{
			try
			{
				ivjcomboStatus = new JComboBox();
				ivjcomboStatus.setName("ivjcomboStatus");
				ivjcomboStatus.setBounds(38, 235, 225, 21);
				ivjcomboStatus.setSelectedIndex(-1);
				// user code begin {1}
				getcomboStatus().addItem(EMPTY_STRING);
				getcomboStatus().addItem(
					RegRenProcessingConstants.NEW_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.APPROVED_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.HOLD_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.DECL_PENDING_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.DECL_FAILED_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.DECL_SUCCESS_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.DECL_ALL_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.UNPAID_LBL);
				getcomboStatus().addItem(
					RegRenProcessingConstants.IN_PROC_LBL);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcomboStatus;
	}

	/**
	 * This is the ContentPane.  The method adds all the components to
	 * the content pane.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmVehicleSearchContentPane1()
	{
		if (ivjFrmVehicleSearchContentPane1 == null)
		{
			try
			{
				ivjFrmVehicleSearchContentPane1 = new JPanel();
				ivjFrmVehicleSearchContentPane1.setName(
					"ivjFrmVehicleSearchContentPane1");
				ivjFrmVehicleSearchContentPane1.setLayout(null);
				ivjFrmVehicleSearchContentPane1.setBounds(0, 0, 0, 0);
				ivjFrmVehicleSearchContentPane1.setMinimumSize(
					new Dimension(0, 0));
				getFrmVehicleSearchContentPane1().add(
					getJPanelSearchItems(),
					getJPanelSearchItems().getName());
				getFrmVehicleSearchContentPane1().add(
					getbtnSearch(),
					getbtnSearch().getName());
				getFrmVehicleSearchContentPane1().add(
					getbtnReset(),
					getbtnReset().getName());
				getFrmVehicleSearchContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmVehicleSearchContentPane1().add(
					getbtnHelp(),
					getbtnHelp().getName());
				// user code begin {1}
				//	defect 8763
				// Add to caButtonGroup for cursor movement
				caButtonGroup = new RTSButtonGroup();
				caButtonGroup.add(getbtnSearch());
				caButtonGroup.add(getbtnReset());
				caButtonGroup.add(getbtnCancel());
				caButtonGroup.add(getbtnHelp());				
				// end defect 8763 
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjFrmVehicleSearchContentPane1;
	}

	/**
	 * This is the ivjJPanelSearchItems that holds all of the search fields.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelSearchItems()
	{
		if (ivjJPanelSearchItems == null)
		{
			try
			{
				ivjJPanelSearchItems = new JPanel();
				ivjJPanelSearchItems.setName("ivjJPanelSearchItems");
				ivjJPanelSearchItems.setLayout(null);
				ivjJPanelSearchItems.setBounds(18, 20, 483, 349);

				ivjJPanelSearchItems.setMinimumSize(
					new Dimension(430, 293));
				getJPanelSearchItems().add(
					getstclblPlateNo(),
					getstclblPlateNo().getName());
				getJPanelSearchItems().add(
					getstclblTransId(),
					getstclblTransId().getName());
				getJPanelSearchItems().add(
					getstclblLastName(),
					getstclblLastName().getName());
				getJPanelSearchItems().add(
					getstclblStatus(),
					getstclblStatus().getName());
				getJPanelSearchItems().add(
					gettxtLicensePlate(),
					gettxtLicensePlate().getName());
				getJPanelSearchItems().add(
					gettxtTransactionID(),
					gettxtTransactionID().getName());
				getJPanelSearchItems().add(
					gettxtLastName(),
					gettxtLastName().getName());
				getJPanelSearchItems().add(
					getstclblBeginDate(),
					getstclblBeginDate().getName());
				getJPanelSearchItems().add(
					getstclblEndDate(),
					getstclblEndDate().getName());
				getJPanelSearchItems().add(
					getstclblTraceNo(),
					getstclblTraceNo().getName());
				getJPanelSearchItems().add(
					gettxtTraceNumber(),
					gettxtTraceNumber().getName());
				getJPanelSearchItems().add(
					gettxtFirstName(),
					gettxtFirstName().getName());
				getJPanelSearchItems().add(
					getstclblFirstName(),
					getstclblFirstName().getName());
				getJPanelSearchItems().add(
					getstclblMiddleName(),
					getstclblMiddleName().getName());
				getJPanelSearchItems().add(
					gettxtMiddleName(),
					gettxtMiddleName().getName());
				getJPanelSearchItems().add(
					gettxtBeginDate(),
					gettxtBeginDate().getName());
				getJPanelSearchItems().add(
					gettxtEndDate(),
					gettxtEndDate().getName());
				getJPanelSearchItems().add(
					getcomboStatus(),
					getcomboStatus().getName());
				// user code begin {1}
				ivjJPanelSearchItems.setOpaque(false);
				ivjJPanelSearchItems.setBorder(
					new TitledBorder(new EtchedBorder(), SEARCH_LBL));
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanelSearchItems;
	}

	/**    
	 * Return the ivjstclblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblBeginDate()
	{
		if (ivjstclblBeginDate == null)
		{
			try
			{
				ivjstclblBeginDate = new JLabel();
				ivjstclblBeginDate.setName("ivjstclblBeginDate");
				ivjstclblBeginDate.setText(BEGIN_LBL);
				ivjstclblBeginDate.setMaximumSize(
					new Dimension(35, 14));
				ivjstclblBeginDate.setBounds(273, 235, 72, 21);
				ivjstclblBeginDate.setMinimumSize(
					new Dimension(35, 14));
				// user code begin {1}
				ivjstclblBeginDate.setDisplayedMnemonic(KeyEvent.VK_B);
				ivjstclblBeginDate.setLabelFor(gettxtBeginDate());
				ivjstclblBeginDate.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjstclblBeginDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblBeginDate;
	}

	/**
	 * Return the ivjstclblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblEndDate()
	{
		if (ivjstclblEndDate == null)
		{
			try
			{
				ivjstclblEndDate = new JLabel();
				ivjstclblEndDate.setName("ivjstclblEndDate");
				ivjstclblEndDate.setText(END_LBL);
				ivjstclblEndDate.setMaximumSize(new Dimension(24, 14));
				ivjstclblEndDate.setMinimumSize(new Dimension(24, 14));
				ivjstclblEndDate.setBounds(273, 261, 72, 21);
				// user code begin {1}
				ivjstclblEndDate.setDisplayedMnemonic(KeyEvent.VK_E);
				ivjstclblEndDate.setLabelFor(gettxtEndDate());
				ivjstclblEndDate.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjstclblEndDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblEndDate;
	}

	/**
	 * Return the ivjstclblFName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblFirstName()
	{
		if (ivjstclblFName == null)
		{
			try
			{
				ivjstclblFName = new JLabel();
				ivjstclblFName.setName("ivjstclblFName");
				ivjstclblFName.setText(FIRST_NAME_LBL);
				ivjstclblFName.setMaximumSize(new Dimension(64, 14));
				ivjstclblFName.setMinimumSize(new Dimension(64, 14));
				ivjstclblFName.setBounds(129, 106, 67, 21);
				// user code begin {1}
				ivjstclblFName.setDisplayedMnemonic(KeyEvent.VK_F);
				ivjstclblFName.setLabelFor(gettxtFirstName());
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblFName;
	}

	/**
	 * Return the ivjstclblName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblLastName()
	{
		if (ivjstclblName == null)
		{
			try
			{
				ivjstclblName = new JLabel();
				ivjstclblName.setName("ivjstclblName");
				ivjstclblName.setText(LAST_CO_NAME_LBL);
				ivjstclblName.setMaximumSize(new Dimension(156, 14));
				ivjstclblName.setMinimumSize(new Dimension(156, 14));
				ivjstclblName.setBounds(37, 162, 159, 21);
				// user code begin {1}
				ivjstclblName.setDisplayedMnemonic(KeyEvent.VK_L);
				ivjstclblName.setLabelFor(gettxtLastName());
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblName;
	}

	/**
	 * Return the ivjstclblMName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblMiddleName()
	{
		if (ivjstclblMName == null)
		{
			try
			{
				ivjstclblMName = new JLabel();
				ivjstclblMName.setName("ivjstclblMName");
				ivjstclblMName.setText(MIDDLE_NAME_LBL);
				ivjstclblMName.setMaximumSize(new Dimension(76, 14));
				ivjstclblMName.setMinimumSize(new Dimension(76, 14));
				ivjstclblMName.setBounds(117, 133, 80, 21);
				// user code begin {1}
				ivjstclblMName.setDisplayedMnemonic(KeyEvent.VK_M);
				ivjstclblMName.setLabelFor(gettxtMiddleName());
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblMName;
	}

	/**
	 * Return the ivjstclblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblPlateNo()
	{
		if (ivjstclblPlateNo == null)
		{
			try
			{
				ivjstclblPlateNo = new JLabel();
				ivjstclblPlateNo.setName("ivjstclblPlateNo");
				ivjstclblPlateNo.setText(PLATENO_LBL);
				ivjstclblPlateNo.setMaximumSize(new Dimension(50, 14));
				ivjstclblPlateNo.setMinimumSize(new Dimension(50, 14));
				ivjstclblPlateNo.setBounds(145, 22, 51, 21);
				// user code begin {1}
				ivjstclblPlateNo.setDisplayedMnemonic(KeyEvent.VK_P);
				ivjstclblPlateNo.setLabelFor(gettxtLicensePlate());
				// user code end
				ivjstclblPlateNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblPlateNo;
	}

	/**
	 * Return the ivjstclblStatus property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblStatus()
	{
		if (ivjstclblStatus == null)
		{
			try
			{
				ivjstclblStatus = new JLabel();
				ivjstclblStatus.setName("ivjstclblStatus");
				ivjstclblStatus.setText(STATUS_LBL);
				ivjstclblStatus.setMaximumSize(new Dimension(40, 14));
				ivjstclblStatus.setMinimumSize(new Dimension(40, 14));
				ivjstclblStatus.setBounds(38, 210, 43, 21);
				// user code begin {1}
				ivjstclblStatus.setDisplayedMnemonic(KeyEvent.VK_A);
				ivjstclblStatus.setLabelFor(getcomboStatus());
				ivjstclblStatus.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstclblStatus.setHorizontalTextPosition(
					SwingConstants.LEFT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblStatus;
	}

	/**
	 * Return the ivjstclblTraceNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblTraceNo()
	{
		if (ivjstclblTraceNo == null)
		{
			try
			{
				ivjstclblTraceNo = new JLabel();
				ivjstclblTraceNo.setName("ivjstclblTraceNo");
				ivjstclblTraceNo.setText(TRACENO_LBL);
				ivjstclblTraceNo.setMaximumSize(new Dimension(101, 14));
				ivjstclblTraceNo.setMinimumSize(new Dimension(101, 14));
				ivjstclblTraceNo.setBounds(93, 52, 101, 21);
				// user code begin {1}
				ivjstclblTraceNo.setDisplayedMnemonic(KeyEvent.VK_I);
				ivjstclblTraceNo.setLabelFor(gettxtTraceNumber());
				// user code end
				ivjstclblTraceNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblTraceNo;
	}

	/**    
	 * Return the ivjstclblTransId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblTransId()
	{
		if (ivjstclblTransId == null)
		{
			try
			{
				ivjstclblTransId = new JLabel();
				ivjstclblTransId.setName("ivjstclblTransId");
				ivjstclblTransId.setText(TRANSID_LBL);
				ivjstclblTransId.setMaximumSize(new Dimension(49, 14));
				ivjstclblTransId.setMinimumSize(new Dimension(49, 14));
				ivjstclblTransId.setBounds(99, 78, 97, 21);
				// user code begin {1}
				ivjstclblTransId.setDisplayedMnemonic(KeyEvent.VK_T);
				ivjstclblTransId.setLabelFor(gettxtTransactionID());
				// user code end
				ivjstclblTransId.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblTransId;
	}

	/**
	 * Return the ivjtxtBeginDate property value
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			try
			{
				ivjtxtBeginDate = new RTSDateField();
				ivjtxtBeginDate.setName("ivjtxtBeginDate");
				ivjtxtBeginDate.setMonthYrOnly(false);
				ivjtxtBeginDate.setColumns(10);
				ivjtxtBeginDate.setBounds(350, 235, 70, 21);
				// user code begin {1}
				ivjtxtBeginDate.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtBeginDate;
	}

	/**
	 * Return the ivjtxtEndDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setName("ivjtxtEndDate");
				ivjtxtEndDate.setMonthYrOnly(false);
				ivjtxtEndDate.setColumns(10);
				ivjtxtEndDate.setBounds(350, 261, 70, 21);
				// user code begin {1}
				ivjtxtEndDate.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtEndDate;
	}

	/**
	 * Return the gettxtFirstName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtFirstName()
	{
		if (ivjtxtFirstName == null)
		{
			try
			{
				ivjtxtFirstName = new RTSInputField();
				ivjtxtFirstName.setName("ivjtxtFirstName");
				ivjtxtFirstName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtFirstName.setBounds(202, 106, 165, 21);
				// user code begin {1}
				ivjtxtFirstName.setMaxLength(
					RegRenProcessingConstants.MAX_FNAME);
				ivjtxtFirstName.addActionListener(this);
				ivjtxtFirstName.setInput(RTSInputField.DEFAULT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtFirstName;
	}

	/**
	 * Return the gettxtLastName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLastName()
	{
		if (ivjtxtLastName == null)
		{
			try
			{
				ivjtxtLastName = new RTSInputField();
				ivjtxtLastName.setName("ivjtxtLastName");
				ivjtxtLastName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLastName.setBounds(202, 162, 165, 21);
				// user code begin {1}
				ivjtxtLastName.setMaxLength(
					RegRenProcessingConstants.MAX_LNAME);
				ivjtxtLastName.addActionListener(this);
				ivjtxtLastName.setInput(RTSInputField.DEFAULT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtLastName;
	}

	/**
	 * Return the ivjtxtLicensePlate property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLicensePlate()
	{
		if (ivjtxtLicensePlate == null)
		{
			try
			{
				ivjtxtLicensePlate = new RTSInputField();
				ivjtxtLicensePlate.setName("ivjtxtLicensePlate");
				ivjtxtLicensePlate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLicensePlate.setBounds(202, 22, 165, 21);
				// user code begin {1}
				ivjtxtLicensePlate.addActionListener(this);
				ivjtxtLicensePlate.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtLicensePlate.setMaxLength(
					RegRenProcessingConstants.MAX_PLATENO);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtLicensePlate;
	}

	/**
	 * Return the ivjtxtMiddleName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMiddleName()
	{
		if (ivjtxtMiddleName == null)
		{
			try
			{
				ivjtxtMiddleName = new RTSInputField();
				ivjtxtMiddleName.setName("ivjtxtMiddleName");
				ivjtxtMiddleName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMiddleName.setBounds(202, 133, 165, 21);
				// user code begin {1}
				ivjtxtMiddleName.setMaxLength(
					RegRenProcessingConstants.MAX_FNAME);
				ivjtxtMiddleName.addActionListener(this);
				//	defect 7127 
				ivjtxtMiddleName.setInput(RTSInputField.DEFAULT);
				// end defect 7127
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtMiddleName;
	}

	/**
	 * Return the ivjtxtTraceNumber property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTraceNumber()
	{
		if (ivjtxtTraceNumber == null)
		{
			try
			{
				ivjtxtTraceNumber = new RTSInputField();
				ivjtxtTraceNumber.setName("ivjtxtTraceNumber");
				ivjtxtTraceNumber.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtTraceNumber.setBounds(202, 50, 165, 21);
				// user code begin {1}
				ivjtxtTraceNumber.addActionListener(this);
				ivjtxtTraceNumber.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtTraceNumber.setPreferredSize(null);
				ivjtxtTraceNumber.setMaxLength(15);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtTraceNumber;
	}

	/**
	 * Return the ivjtxtTransactionID property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTransactionID()
	{
		if (ivjtxtTransactionID == null)
		{
			try
			{
				ivjtxtTransactionID = new RTSInputField();
				ivjtxtTransactionID.setName("ivjtxtTransactionID");
				ivjtxtTransactionID.setHighlighter(
					new BasicHighlighter());
				ivjtxtTransactionID.setBounds(202, 78, 165, 21);
				// user code begin {1}
				ivjtxtTransactionID.addActionListener(this);
				ivjtxtTransactionID.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtTransactionID.setMaxLength(
					RegRenProcessingConstants.MAX_TRANSID);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtTransactionID;
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
			setName("FrmVehicleSearchREG101");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(526, 440);
			setTitle(FRM_TITLE);
			setContentPane(getFrmVehicleSearchContentPane1());
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
	 * Process the data.
	 * 
	 * @return Object
	 */
	public Object processData()
	{
		if (!validateSearchKeys())
		{
			return null;
		}
		//standard validation
		clearAllColor(this);
		RTSException leValidEx = new RTSException();
		//create search object with keys
		Hashtable lhtSearchKeys = new Hashtable();
		lhtSearchKeys.put(
			OFCISSUANCENO,
			String.valueOf(SystemProperty.getOfficeIssuanceNo()));
		//populate selected fields	
		if (gettxtLicensePlate().getText().trim().length() > 0)
			lhtSearchKeys.put(
				REGPLTNO,
				gettxtLicensePlate().getText().toUpperCase());
		if (gettxtFirstName().getText().trim().length() > 0)
			lhtSearchKeys.put(
				FRSTNAME,
				gettxtFirstName().getText().toUpperCase());
		if (gettxtMiddleName().getText().trim().length() > 0)
			lhtSearchKeys.put(
				MIDLNAME,
				gettxtMiddleName().getText().toUpperCase());
		if (gettxtLastName().getText().trim().length() > 0)
			lhtSearchKeys.put(
				LASTNAME,
				gettxtLastName().getText().toUpperCase());
		if (gettxtTraceNumber().getText().trim().length() > 0)
			lhtSearchKeys.put(
				ITRNTTRACENO,
				gettxtTraceNumber().getText().toUpperCase());

		// defect 8763
		// Use Error Constants  
		if (gettxtTransactionID().getText().length() > 0)
		{
			if (gettxtTransactionID().getText().length()
				!= RegRenProcessingConstants.MAX_TRANSID)
			{
				leValidEx.addException(new RTSException(
				//SEARCH_TRANSID_LENGTH_ERROR_NO), (957)
				ErrorsConstant.ERR_NUM_TRANSACTION_ID_INCORRECT_FORMAT),
					gettxtTransactionID());
				leValidEx.displayError(this);
				leValidEx.getFirstComponent().requestFocus();
				return null;
			}
			lhtSearchKeys.put(
				TRANSID,
				gettxtTransactionID().getText().toUpperCase());
		}

		// defect 8247
		// Used to determine if we need to research when
		// returning to the search results screen. We are defaulting
		// this value to false and the choosen cntystatuscd will
		// determine this below.
		lhtSearchKeys.put(VIEWONLY, new Boolean(false));
		// end defect 8247

		if (!getcomboStatus()
			.getSelectedItem()
			.toString()
			.equals(EMPTY_STRING))
		{
			String lsTemp =
				getcomboStatus().getSelectedItem().toString();
			//perform translation to int code
			if (lsTemp
				.equalsIgnoreCase(RegRenProcessingConstants.UNPAID_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.UNPAID + EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.NEW_LBL))
			{
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.NEW + EMPTY_STRING);
				lhtSearchKeys.put(VIEWONLY, new Boolean(false));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.HOLD_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.HOLD + EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(false));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.DECL_PENDING_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.DECLINED_REFUND_PENDING
						+ EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.APPROVED_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.APPROVED + EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.DECL_FAILED_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.DECLINED_REFUND_FAILED
						+ EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.DECL_ALL_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					RegRenProcessingConstants.DECL_ALL_LBL);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.DECL_SUCCESS_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.DECLINED_REFUND_APPROVED
						+ EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(true));
				// end defect 8247
			}
			else if (
				lsTemp.equalsIgnoreCase(
					RegRenProcessingConstants.IN_PROC_LBL))
			{
				lhtSearchKeys.put(
					CNTYSTATUSCD,
					CommonConstants.IN_PROCESS + EMPTY_STRING);
				// defect 8247
				// Used to determine if we need to research when
				// returning to the search results screen
				lhtSearchKeys.put(VIEWONLY, new Boolean(false));
				// end defect 8247
			}
		}
		String lsBegin = null;
		String lsEnd = null;
		if (!gettxtBeginDate()
			.getText()
			.substring(0, 2)
			.equals(SPACE + SPACE))
		{
			lsBegin =
				RegRenClientUtility.validateDate(gettxtBeginDate());
			if (lsBegin == null)
			{
				leValidEx.addException(new RTSException(
				//SEARCH_NULL_BEGIN_DATE_ERROR_NO),  (954)
				ErrorsConstant.ERR_NUM_BEGIN_DATE_INCORRECT_FORMAT),
					gettxtBeginDate());
				leValidEx.displayError(this);
				leValidEx.getFirstComponent().requestFocus();
				return null;
			}
			if (RegRenClientUtility
				.dateTooOld(gettxtBeginDate().getDate(), 60))
			{
				// more than 60 days old.
				leValidEx.addException(new RTSException(
				//SEARCH_SIXTY_DAY_ERROR_NO),  (982)
				ErrorsConstant.ERR_NUM_OVER_60_DAYS_IN_PAST),
					gettxtBeginDate());
				leValidEx.displayError(this);
				leValidEx.getFirstComponent().requestFocus();
				return null;
			}
			lhtSearchKeys.put(STARTDATE, lsBegin);
		}
		if (!gettxtEndDate()
			.getText()
			.substring(0, 2)
			.equals(SPACE + SPACE))
		{
			lsEnd = RegRenClientUtility.validateDate(gettxtEndDate());
			if (lsEnd == null)
			{
				leValidEx.addException(new RTSException(
				//SEARCH_NULL_END_DATE_ERROR_NO),   (955)
				ErrorsConstant.ERR_NUM_END_DATE_INCORRECT_FORMAT),
					gettxtEndDate());
				leValidEx.displayError(this);
				leValidEx.getFirstComponent().requestFocus();
				return null;
			}
			else if (lsEnd.equals(THIRTY))
			{
				leValidEx.addException(new RTSException(
				//SEARCH_THIRTY_ERROR_NO), (959)
				ErrorsConstant.ERR_NUM_DATE_WITHIN_30_DAYS),
					gettxtBeginDate());
				leValidEx.displayError(this);
				leValidEx.getFirstComponent().requestFocus();
				return null;
			}
			//begin date required constraint removed.......
			lhtSearchKeys.put(ENDDATE, lsEnd);
		}
		if (lsBegin != null
			&& lsEnd != null
			&& (new Long(lsEnd)).longValue()
				< (new Long(lsBegin)).longValue())
		{
			leValidEx.addException(new RTSException(
			//SEARCH_DATE_ERROR_NO),  (981) 
			ErrorsConstant.ERR_NUM_INVALID_BEGIN_END),
				gettxtBeginDate());
			leValidEx.displayError(this);
			leValidEx.getFirstComponent().requestFocus();
			return null;
		}
		// end defect 8763 
		getController().processData(
			AbstractViewController.ENTER,
			lhtSearchKeys);
		return null;
	}

	/**
	 * Resets the fields to blank when the reset button is clicked.
	 */
	public void reset()
	{
		clearAllColor(this);
		gettxtLicensePlate().setText(EMPTY_STRING);
		gettxtTraceNumber().setText(EMPTY_STRING);
		gettxtTransactionID().setText(EMPTY_STRING);
		gettxtFirstName().setText(EMPTY_STRING);
		gettxtMiddleName().setText(EMPTY_STRING);
		gettxtLastName().setText(EMPTY_STRING);
		gettxtBeginDate().setDate(null);
		gettxtEndDate().setDate(null);
		getcomboStatus().setSelectedIndex(0);
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
		// empty code block
	}

	/**
	 * Validate all of the Search Keys that where entered.
	 * 
	 * @return boolean
	 */
	public boolean validateSearchKeys()
	{
		//standard validation
		clearAllColor(this);
		RTSException leValidEx = new RTSException();
		if (gettxtLicensePlate().getText().trim().length() == 0
			&& gettxtTraceNumber().getText().trim().length() == 0
			&& gettxtTransactionID().getText().trim().length() == 0
			&& gettxtFirstName().getText().trim().length() == 0
			&& gettxtMiddleName().getText().trim().length() == 0
			&& gettxtLastName().getText().trim().length() == 0
			// defect 8763 
			&& gettxtBeginDate().isDateEmpty()
			&& gettxtEndDate().isDateEmpty()
			// end defect 8763
			&& getcomboStatus().getSelectedItem().equals(EMPTY_STRING))
		{
			//handle no selection
			clearAllColor(this);
			// defect 8763 
			leValidEx.addException(new RTSException(
			//SEARCH_KEY_ERROR_NO),
			ErrorsConstant.ERR_NUM_ENTER_SEARCH_PARAMETERS),
				gettxtLicensePlate());
			// end defect 8763 
			leValidEx.displayError(this);
			leValidEx.getFirstComponent().requestFocus();
			return false;
		}
		return true;
	}
}