package com.txdot.isd.rts.client.registration.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;

import com.txdot.isd.rts.services.cache.RegistrationPlateStickerCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmStickerSelectionREG001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking(),
 * 							doneWorking()
 * MAbs			0/25/2002	Always reverts to original choice 
 * 							defect 4326 
 * B Hargrove	03/10/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	03/31/2005	Comment out setNextFocusableComponent() 
 *							modify gettblStkrTypes()
 *							defect 7894 Ver 5.2.3
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * K Harrell	06/14/2004  ClassToPlate, PlateToSticker Implementation
 * 							reorganized imports
 * 							delete DISABLED_PERSON_PLT, 
 * 							DISABLED_MOTORCYCLE_PLT
 * 							delete getOrigVehInqData(),getRegValidData(),
 * 							getStkrsData(),getVehInqData(),
 * 							setStkrsData()
 * 							renamed cvStkrsData to cvPltToStkrData 
 * 							modify actionPerformed(),setData()
 * 							defect 8218 Ver 5.2.3   
 * B Hargrove	06/21/2005	modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 *							defect 7894 Ver 5.2.3
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationClientUtilityMethods class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.client.reg.business.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	07/19/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T. Pederson	12/21/2005	Moved setting default focus to initialize.
 * 							delete windowOpened()
 * 							modify initialize() 	
 * 							defect 8494 Ver 5.2.3
 * K Harrell	03/14/2007	Work for Special Plates 
 * 							modify actionPerformed(),prepInv()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/24/2007	Working ....
 * 							defect 9085  Ver Special Plates
 * K Harrell	01/14/2009	Modify to use SINGLE_SELECTION vs. 
							SINGLE_INTERVAL_SELECTION
							delete comments from Java 1.4 upgrade 
							modify gettblStkrTypes() 
							defect 9796 Ver Defect_POS_D
 * --------------------------------------------------------------------- 
 */
/**
 * This form presents the sticker selections, either windshield sticker
 * or plate sticker during the Replacement event.
 *
 * @version	Defect_POS_D	01/14/2009
 * @author	Joseph Kwik
 * <br>Creation Date:		06/25/2001 13:16:44
 */

public class FrmStickerSelectionREG001
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjstcLblSelectStickerType = null;
	private JPanel ivjFrmStickerSelectionREG001ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblStkrTypes = null;
	private TMREG001 caStkrTypesTableModel;

	// Objects
	private CompleteTransactionData caCompTransData = null;
	private RegistrationValidationData caRegValidData = null;
	private VehicleInquiryData caOrigVehicleInquiryData = null;
	private VehicleInquiryData caVehicleInquiryData = null;

	// Replacement values to handle changes
	private VehicleInquiryData caReplVehicleInquiryData = null;
	private SpecialPlatesRegisData caReplSpclPltRegisData = null;
	private RegistrationValidationData caReplRegValidData = null;
	private RegistrationData caReplRegisData = null;

	// Vector 
	private Vector cvPltToStkrData = new Vector();

	// String 
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String SELECT = "Select Sticker Type:";
	private final static String TITLE_REG001 =
		"Sticker Selection   REG001";

	/**
	 * FrmStickerSelectionREG001 constructor comment.
	 */

	public FrmStickerSelectionREG001()
	{
		super();
		initialize();
	}
	/**
	 * FrmStickerSelectionREG001 constructor with parent.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmStickerSelectionREG001(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmStickerSelectionREG001 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmStickerSelectionREG001(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		// Determines what actions to take when Enter, Cancel, or Help
		// are pressed.
		if (!startWorking())
		{
			return;
		}

		try
		{
			//field validation
			clearAllColor(this);
			if (aaAE.getSource() == ivjbuttonPanel.getBtnEnter())
			{
				createReplVehInqDataCopy();

				for (int i = 0; i < ivjtblStkrTypes.getRowCount(); i++)
				{
					if (ivjtblStkrTypes.isRowSelected(i) == true)
					{
						// defect 8218 
						// Data of type PlateToStickerData
						PlateToStickerData laPltToStkrData =
							(PlateToStickerData) cvPltToStkrData.get(i);

						// Set sticker code equal to replacement
						// sticker code
						caReplVehicleInquiryData
							.getMfVehicleData()
							.getRegData()
							.setRegStkrCd(
							laPltToStkrData.getRegStkrCd());
						// end defect 8218 	

						caReplRegValidData.setEnterOnStkrSelection(
							true);
						break;
					}
				}
				// populate inventory with data
				prepInv();

				// defect 9085 
				if (!(caReplRegValidData.getReplPltOptions()
					&& caReplRegValidData.getNewPltReplIndi() == 1))
				{
					// Calculate fees
					caCompTransData =
						RegistrationClientUtilityMethods.prepFees(
							caReplVehicleInquiryData,
							caRegValidData.getOrigVehInqData());

					getController().processData(
						AbstractViewController.ENTER,
						caCompTransData);
				}
				else
				{
					getController().processData(
						VCStickerSelectionREG001.PLATE_SELECTION,
						caReplVehicleInquiryData);

				}
				// end defect 9085 
			}

			else if (aaAE.getSource() == ivjbuttonPanel.getBtnCancel())
			{
				(
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject())
						.setEnterOnStkrSelection(
					false);
				getController().processData(
					AbstractViewController.CANCEL,
					caOrigVehicleInquiryData);
			}

			else if (aaAE.getSource() == ivjbuttonPanel.getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.REG001);
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.REG001);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG001A);
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * 
	 * Create Copy of Veh InquiryData 
	 * 
	 */
	private void createReplVehInqDataCopy()
	{
		// Copy VehicleInquiryData 
		caReplVehicleInquiryData =
			(VehicleInquiryData) UtilityMethods.copy(
				caVehicleInquiryData);

		// Copy Validation Object
		caReplRegValidData =
			(RegistrationValidationData) UtilityMethods.copy(
				caVehicleInquiryData.getValidationObject());

		// Set Validation Object
		caReplVehicleInquiryData.setValidationObject(
			caReplRegValidData);

		// Copy Registration Data
		caReplRegisData =
			(RegistrationData) UtilityMethods.copy(
				caVehicleInquiryData.getMfVehicleData().getRegData());

		// Set Registration Data
		caReplVehicleInquiryData.getMfVehicleData().setRegData(
			caReplRegisData);

		// Copy Special Plates Regis Data 
		caReplSpclPltRegisData =
			(SpecialPlatesRegisData) UtilityMethods.copy(
				caVehicleInquiryData
					.getMfVehicleData()
					.getSpclPltRegisData());

		// Set Special Plates Regis Data 
		caReplVehicleInquiryData
			.getMfVehicleData()
			.setSpclPltRegisData(
			caReplSpclPltRegisData);
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
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjbuttonPanel.setAsDefault(this);
				ivjbuttonPanel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * Return the FrmStickerSelectionREG001ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax
		.swing
		.JPanel getFrmStickerSelectionREG001ContentPane1()
	{
		if (ivjFrmStickerSelectionREG001ContentPane1 == null)
		{
			try
			{
				ivjFrmStickerSelectionREG001ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmStickerSelectionREG001ContentPane1.setName(
					"FrmStickerSelectionREG001ContentPane1");
				ivjFrmStickerSelectionREG001ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmStickerSelectionREG001ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmStickerSelectionREG001ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(300, 225));

				java
					.awt
					.GridBagConstraints constraintsstcLblSelectStickerType =
					new java.awt.GridBagConstraints();
				constraintsstcLblSelectStickerType.gridx = 0;
				constraintsstcLblSelectStickerType.gridy = 0;
				constraintsstcLblSelectStickerType.ipadx = 30;
				constraintsstcLblSelectStickerType.insets =
					new java.awt.Insets(23, 26, 2, 131);
				getFrmStickerSelectionREG001ContentPane1().add(
					getstcLblSelectStickerType(),
					constraintsstcLblSelectStickerType);

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 0;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 225;
				constraintsJScrollPane1.ipady = 78;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(3, 26, 8, 27);
				getFrmStickerSelectionREG001ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 0;
				constraintsbuttonPanel.gridy = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 29;
				constraintsbuttonPanel.ipady = 19;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(8, 27, 13, 27);
				getFrmStickerSelectionREG001ContentPane1().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmStickerSelectionREG001ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */

	private javax.swing.JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setOpaque(true);
				getJScrollPane1().setViewportView(gettblStkrTypes());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the stcLblSelectStickerType property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblSelectStickerType()
	{
		if (ivjstcLblSelectStickerType == null)
		{
			try
			{
				ivjstcLblSelectStickerType = new javax.swing.JLabel();
				ivjstcLblSelectStickerType.setName(
					"stcLblSelectStickerType");
				ivjstcLblSelectStickerType.setText(SELECT);
				ivjstcLblSelectStickerType.setMaximumSize(
					new java.awt.Dimension(113, 14));
				ivjstcLblSelectStickerType.setMinimumSize(
					new java.awt.Dimension(113, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblSelectStickerType;
	}

	/**
	 * Return the tblStkrTypes property value.
	 * 
	 * @return RTSTable
	 */

	private RTSTable gettblStkrTypes()
	{
		if (ivjtblStkrTypes == null)
		{
			try
			{
				ivjtblStkrTypes =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblStkrTypes.setName("tblStkrTypes");
				getJScrollPane1().setColumnHeaderView(
					ivjtblStkrTypes.getTableHeader());
				// defect 7894
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7894
				ivjtblStkrTypes.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.registration
						.ui
						.TMREG001());
				ivjtblStkrTypes.setPreferredSize(
					new java.awt.Dimension(222, 87));
				ivjtblStkrTypes.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblStkrTypes.setBounds(0, 11, 222, 87);
				// user code begin {1}
				caStkrTypesTableModel =
					(TMREG001) ivjtblStkrTypes.getModel();

				// defect 9796 
				// Use SINGLE_SELECTION so that only one row can 
				// be selected. 
				ivjtblStkrTypes.setSelectionMode(
				// ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				ListSelectionModel.SINGLE_SELECTION);
				// end defect 9796 

				ivjtblStkrTypes.init();
				ivjtblStkrTypes.addActionListener(this);
				ivjtblStkrTypes.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtblStkrTypes;
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
			// defect 8494
			// Moved from windowOpened
			setDefaultFocusField(gettblStkrTypes());
			// end defect 8494
			// user code end
			setName("FrmStickerSelectionREG001");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(300, 225);
			setTitle(TITLE_REG001);
			setContentPane(getFrmStickerSelectionREG001ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}

		// user code end
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param args String[]
	 */

	public static void main(String[] args)
	{

		try
		{

			FrmStickerSelectionREG001 laFrmStickerSelectionREG001;

			laFrmStickerSelectionREG001 =
				new FrmStickerSelectionREG001();

			laFrmStickerSelectionREG001.setModal(true);

			laFrmStickerSelectionREG001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});

			laFrmStickerSelectionREG001.show();

			java.awt.Insets laInsets =
				laFrmStickerSelectionREG001.getInsets();

			laFrmStickerSelectionREG001.setSize(
				laFrmStickerSelectionREG001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmStickerSelectionREG001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);

			laFrmStickerSelectionREG001.setVisibleRTS(true);

		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);

			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Handle creation of inventory items needed for inventory module.
	 * 
	 */
	private void prepInv()
	{
		caReplRegValidData.setInvItms(null);

		RegistrationClientUtilityMethods.addItmCdToInv(
			caReplRegValidData,
			caReplVehicleInquiryData
				.getMfVehicleData()
				.getRegData()
				.getRegStkrCd());

		// defect 9085
		//RegistrationClientUtilityMethods.procsDsabldPersnPlt(
		//	caVehicleInquiryData);

		if (caReplRegValidData.getNewPltReplIndi() == 1
			&& !caReplRegValidData.getReplPltOptions())
		{
			RegistrationClientBusiness.procsReplPltInv(
				caReplVehicleInquiryData);
		}
		// end defect 9085 
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */

	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					// first element is flag whether to go to REG029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
					{
						getController().processData(
							VCStickerSelectionREG001
								.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCStickerSelectionREG001.REDIRECT_NEXT_VC,
							lvIsNextVCREG029);
					}
				}
				else
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						ERRMSG_DATA_MISS,
						ERRMSG_ERROR).displayError(
						this);
				}
			}
		}
		else if (aaDataObject instanceof VehicleInquiryData)
		{
			caOrigVehicleInquiryData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);

			caVehicleInquiryData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);

			caRegValidData =
				(RegistrationValidationData) caVehicleInquiryData
					.getValidationObject();

			RegistrationData laRegData =
				caVehicleInquiryData.getMfVehicleData().getRegData();

			// Determine if replacement plate code is present in cache
			cvPltToStkrData =
				RegistrationPlateStickerCache.getPltStkrs(
					laRegData.getRegClassCd(),
					laRegData.getRegPltCd(),
					caVehicleInquiryData.getRTSEffDt());

			if (cvPltToStkrData != null)
			{
				int liNumStkrTypes = cvPltToStkrData.size();

				if (liNumStkrTypes == 1)
				{
					//getController().processData(
					// AbstractViewController.CANCEL, caOrigVehicleInquiryData);
					return;
				}
				else
				{
					int liRowToSelect = 0;
					Vector lvRows = new Vector();
					for (int i = 0; i < liNumStkrTypes; i++)
					{
						PlateToStickerData laData =
							(PlateToStickerData) cvPltToStkrData.get(i);

						lvRows.add(laData.getRegStkrCdDesc());

						if (laData
							.getRegStkrCd()
							.equals(
								caOrigVehicleInquiryData
									.getMfVehicleData()
									.getRegData()
									.getRegStkrCd()))
						{
							liRowToSelect = i;
						}
					}
					caStkrTypesTableModel.add(lvRows);

					if (lvRows.size() > 0)
					{
						gettblStkrTypes().setRowSelectionInterval(
							liRowToSelect,
							liRowToSelect);
					}
				}
			}
		}
	}
}
