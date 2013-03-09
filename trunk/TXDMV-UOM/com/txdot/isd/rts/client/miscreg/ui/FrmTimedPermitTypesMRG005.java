package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com
	.txdot
	.isd
	.rts
	.client
	.miscreg
	.business
	.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.data.CustomerData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.data.ScreenMRG006SavedData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmTimedPermitTypesMRG005.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	03/18/2005	Modify code for move to WSAD from VAJ.
 *							modify for Java 1.4 \ WSAD
 *							defect 7893 Ver 5.2.3
 * 	B Hargrove	04/01/2005	Comment out setNextFocusableComponent() 
 *							modify handleSameVehicle(), keyPressed()
 *							defect 7893 Ver 5.2.3
 * 	B Hargrove	04/15/2005	Define 'Etched Border' property to remove
 * 							warning msg. Increase size of radio button
 * 							text areas to get rid of '...'. Remove 
 * 							selection when using Arrow or Tab keys 
 * 							(do focus only). 
 *							modify visual composition, keyPressed()
 *							defect 7893 Ver 5.2.3
 * B Hargrove	07/15/2005	Remove unused variables.
 * 							Remove implements KeyListener
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel.
 * 							delete implements KeyListener
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out handling mnemonics in keyPressed
 * 							modify keyPressed()
 * 							defect 7893 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciSelctdRadioButton
 * 							modify getpnlRadioButtons(), initialize()
 * 							defect 7893 Ver 5.2.3
 * K Harrell	02/14/2006	Title & lower border
 * 							add RADIO_PANEL_TITLE
 * 							modify getpnlRadioButtons()
 * 							defect 7893 Ver 5.2.3 
 * K Harrell	05/24/2010	Visual Editor work as no longer require
 * 							same Vehicle (moved to MRG007). 
 * 							add caPrmtData, setDataToDataObject()
 * 							add ivjradioRegular, ivjJPanelRegularMotorcycle,
 * 							  ivjradioMotorcycle, ivjradioNone, get methods
 * 							add handleRegularMotorcycle()  
 * 							delete ItemListener, itemStateChanged() 
 * 							delete caSavedVehTimedPrmtData, caTimedPrmtData, 
 * 							 get/set methods
 * 							delete SAME_VEH 
 * 							delete ivjchkSameVehicle, get method
 * 							delete getTimedPrmtData 
 * 							delete getBuilderData(), handleSameVehicle(), 
 * 							 setInputData() 
 * 							modify getpnlRadioButtons(), actionPerformed(),
 * 							 getFrmTimedPermitTypesMRG005ContentPane1(), 
 * 							 getradio144HourPermit(), getradio72HourPermit(), 
 * 							 getradio30DayPermit(), getradioFactoryDeliver(), 
 * 							 getradioOneTripPermit(), 
 * 							 RADIO_PANEL_TITLE    
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/14/2010	Copy caPrmtData object prior to passing to VC 
 * 							modify actionPerformed()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	10/01/2010 	Implement ScreenMRG006SavedData
 * 							modify actionPerformed(),
 * 								setDataToDataObject() 
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	05/18/2011	clear BulkPrmtVendorId
 * 							modify setData() 
 * K Harrell 	06/22/2011	changes to handle Permit Modification
 *							modify actionPerformed(),setData(), 
 *							 setDataToDataObject(),
 *							 handleRegularMotorcycle(),
 *							 itemStateChanged()   	
 *        					defect 10844 Ver 6.8.0
 * K Harrell	09/02/2011	Clear Prior AuditTrailTransId,PrmtIssuanceId on 
 * 							Permit Application 
 * 							modify setData() 
 * 							defect 10989 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration timed permit types.
 *
 * @version	6.8.1			09/02/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 08:35:16
 */
public class FrmTimedPermitTypesMRG005
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmTimedPermitTypesMRG005ContentPane1 = null;
	private JPanel ivjpnlRadioButtons = null;
	private JRadioButton ivjradio144HourPermit = null;
	private JRadioButton ivjradio30DayPermit = null;
	private JRadioButton ivjradio72HourPermit = null;
	private JRadioButton ivjradioFactoryDeliver = null;
	private JRadioButton ivjradioOneTripPermit = null;

	//	defect 10491
	private JRadioButton ivjradioRegular = null;
	private JPanel ivjJPanelRegularMotorcycle = null;
	private JRadioButton ivjradioMotorcycle = null;
	private JRadioButton ivjradioNone = null;

	private PermitData caPrmtData = null;
	private int ciNo30DayPrmts = 0;
	// defect 10491 

	// defect 10844 
	private boolean cbModifyPermit = false;
	// end defect 10844  

	private final static String PERMIT_144_HOUR = "144-Hour Permit";
	private final static String PERMIT_30_DAY = "30 Day Permit";
	private final static String PERMIT_72_HOUR = "72-Hour Permit";

	private final static String PERMIT_FACTORY = "Factory Delivery";
	private final static String PERMIT_ONE_TRIP = "One Trip Permit";
	private final static String RADIO_PANEL_TITLE = "Select One:";

	private final static String TITLE_MRG005 =
		"Timed Permit Types          MRG005";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTimedPermitTypesMRG005 laFrmTimedPermitTypesMRG005;
			laFrmTimedPermitTypesMRG005 =
				new FrmTimedPermitTypesMRG005();
			laFrmTimedPermitTypesMRG005.setModal(true);
			laFrmTimedPermitTypesMRG005
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmTimedPermitTypesMRG005.show();
			java.awt.Insets insets =
				laFrmTimedPermitTypesMRG005.getInsets();
			laFrmTimedPermitTypesMRG005.setSize(
				laFrmTimedPermitTypesMRG005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTimedPermitTypesMRG005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmTimedPermitTypesMRG005.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTimedPermitTypesMRG005 constructor comment.
	 */
	public FrmTimedPermitTypesMRG005()
	{
		super();
		initialize();
	}

	/**
	 * FrmTimedPermitTypesMRG005 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmTimedPermitTypesMRG005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTimedPermitTypesMRG005 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTimedPermitTypesMRG005(JFrame aaParent)
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

			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// defect 10491 
				if (validateData())
				{
					setDataToDataObject();

					PermitData laPrmtData =
						(PermitData) UtilityMethods.copy(caPrmtData);

					if (cbModifyPermit)
					{
						getController().processData(
							VCTimedPermitTypesMRG005.MRG006,
							laPrmtData);
					}
					else
					{
						laPrmtData.setVIAllocData(
							MiscellaneousRegClientUtilityMethods
								.setupPrmtInvAlloc(
								laPrmtData.getItmCd()));

						getController().processData(
							InventoryConstant.INV_GET_NEXT_VI_ITEM_NO,
							laPrmtData);
					}
					// end defect 10844 
				}
				// end defect 10491 
			}

			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				// defect 10592
				// Clear saved data if cancel off MRG005  
				getController().getMediator().closeVault(
					ScreenConstant.MRG006,
					null);
				// end defect 10592 

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}

			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.MRG005);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
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
				ivjButtonPanel1.setBounds(12, 302, 229, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmTimedPermitTypesMRG005ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTimedPermitTypesMRG005ContentPane1()
	{
		if (ivjFrmTimedPermitTypesMRG005ContentPane1 == null)
		{
			try
			{
				ivjFrmTimedPermitTypesMRG005ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmTimedPermitTypesMRG005ContentPane1.setName(
					"FrmTimedPermitTypesMRG005ContentPane1");
				// defect 10491 
				// Was GridBagLayout 
				ivjFrmTimedPermitTypesMRG005ContentPane1.setLayout(
					null);
				// end defect 10491 
				ivjFrmTimedPermitTypesMRG005ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTimedPermitTypesMRG005ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(562, 316));

				java.awt.GridBagConstraints constraintschkSameVehicle =
					new java.awt.GridBagConstraints();
				constraintschkSameVehicle.gridx = 1;
				constraintschkSameVehicle.gridy = 2;
				constraintschkSameVehicle.ipadx = 17;
				constraintschkSameVehicle.insets =
					new java.awt.Insets(4, 136, 2, 19);

				// defect 10491 
				//	getFrmTimedPermitTypesMRG005ContentPane1().add(
				//		getchkSameVehicle(),
				//		constraintschkSameVehicle);
				// end defect 10491 

				ivjFrmTimedPermitTypesMRG005ContentPane1.add(
					getpnlRadioButtons(),
					null);
				ivjFrmTimedPermitTypesMRG005ContentPane1.add(
					getButtonPanel1(),
					null);
				// user code end
				ivjFrmTimedPermitTypesMRG005ContentPane1.add(
					getJPanelRegularMotorcycle(),
					null);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmTimedPermitTypesMRG005ContentPane1;
	}

	/**
	 * This method initializes ivjJPanelRegularMotorcycle
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelRegularMotorcycle()
	{
		if (ivjJPanelRegularMotorcycle == null)
		{
			ivjJPanelRegularMotorcycle = new javax.swing.JPanel();
			ivjJPanelRegularMotorcycle.setLayout(null);
			ivjJPanelRegularMotorcycle.add(getradioRegular(), null);
			ivjJPanelRegularMotorcycle.add(getradioMotorcycle(), null);
			ivjJPanelRegularMotorcycle.add(getradioNone(), null);
			RTSButtonGroup laRadioGrp = new RTSButtonGroup();
			laRadioGrp.add(getradioRegular());
			laRadioGrp.add(getradioMotorcycle());
			laRadioGrp.add(getradioNone());
			ivjJPanelRegularMotorcycle.setBounds(45, 219, 170, 71);
			ivjJPanelRegularMotorcycle.setBorder(
				new TitledBorder(new EtchedBorder(), ""));
			ivjJPanelRegularMotorcycle.setEnabled(false);
		}
		return ivjJPanelRegularMotorcycle;
	}

	/**
	 * Return the pnlRadioButtons
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlRadioButtons()
	{
		if (ivjpnlRadioButtons == null)
		{
			try
			{
				ivjpnlRadioButtons = new javax.swing.JPanel();
				ivjpnlRadioButtons.setName("ivjpnlRadioButtons");
				ivjpnlRadioButtons.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						RADIO_PANEL_TITLE));

				// defect 10491 
				ivjpnlRadioButtons.setLayout(null);
				// end defect 10491 

				ivjpnlRadioButtons.add(getradio72HourPermit(), null);
				ivjpnlRadioButtons.add(getradio144HourPermit(), null);
				ivjpnlRadioButtons.add(getradioOneTripPermit(), null);
				ivjpnlRadioButtons.add(getradioFactoryDeliver(), null);
				ivjpnlRadioButtons.add(getradio30DayPermit(), null);
				ivjpnlRadioButtons.setBounds(45, 19, 170, 194);
				ivjpnlRadioButtons.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlRadioButtons.setMinimumSize(
					new java.awt.Dimension(200, 200));

				// Add the radio buttons to a button group so they are 
				// mutually exclusive.
				// defect 7893
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradio72HourPermit());
				laRadioGrp.add(getradio144HourPermit());
				laRadioGrp.add(getradioOneTripPermit());
				laRadioGrp.add(getradioFactoryDeliver());
				laRadioGrp.add(getradio30DayPermit());

				// end defect 7893
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlRadioButtons;
	}

	/**
	 * Return the radio144HourPermit
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradio144HourPermit()
	{
		if (ivjradio144HourPermit == null)
		{
			try
			{
				ivjradio144HourPermit = new JRadioButton();
				ivjradio144HourPermit.setBounds(18, 62, 141, 22);
				ivjradio144HourPermit.setName("ivjradio144HourPermit");
				ivjradio144HourPermit.setMnemonic('1');
				ivjradio144HourPermit.setText(PERMIT_144_HOUR);
				ivjradio144HourPermit.setMaximumSize(
					new java.awt.Dimension(118, 22));
				ivjradio144HourPermit.setActionCommand(PERMIT_144_HOUR);
				ivjradio144HourPermit.setMinimumSize(
					new java.awt.Dimension(118, 22));
				// user code begin {1}
				ivjradio144HourPermit.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradio144HourPermit;
	}

	/**
	 * Return the radio30DayPermit
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getradio30DayPermit()
	{
		if (ivjradio30DayPermit == null)
		{
			try
			{
				ivjradio30DayPermit = new javax.swing.JRadioButton();
				ivjradio30DayPermit.setBounds(18, 158, 126, 22);
				ivjradio30DayPermit.setName("ivjradio30DayPermit");
				ivjradio30DayPermit.setMnemonic(51);
				ivjradio30DayPermit.setText(PERMIT_30_DAY);
				ivjradio30DayPermit.setMaximumSize(
					new java.awt.Dimension(104, 22));
				ivjradio30DayPermit.setActionCommand(PERMIT_30_DAY);
				ivjradio30DayPermit.setMinimumSize(
					new java.awt.Dimension(104, 22));
				// user code begin {1}
				ivjradio30DayPermit.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradio30DayPermit;
	}

	/**
	 * Return the radio72HourPermit
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getradio72HourPermit()
	{
		if (ivjradio72HourPermit == null)
		{
			try
			{
				ivjradio72HourPermit = new javax.swing.JRadioButton();
				ivjradio72HourPermit.setBounds(18, 30, 133, 22);
				ivjradio72HourPermit.setName("ivjradio72HourPermit");
				ivjradio72HourPermit.setMnemonic(55);
				ivjradio72HourPermit.setText(PERMIT_72_HOUR);
				ivjradio72HourPermit.setMaximumSize(
					new java.awt.Dimension(111, 22));
				ivjradio72HourPermit.setActionCommand(PERMIT_72_HOUR);
				ivjradio72HourPermit.setSelected(true);
				ivjradio72HourPermit.setMinimumSize(
					new java.awt.Dimension(111, 22));
				// user code begin {1}
				ivjradio72HourPermit.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradio72HourPermit;
	}

	/**
	 * Return the radioFactoryDeliver
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioFactoryDeliver()
	{
		if (ivjradioFactoryDeliver == null)
		{
			try
			{
				ivjradioFactoryDeliver = new JRadioButton();
				ivjradioFactoryDeliver.setBounds(18, 126, 118, 22);
				ivjradioFactoryDeliver.setName(
					"ivjradioFactoryDeliver");
				ivjradioFactoryDeliver.setMnemonic(70);
				ivjradioFactoryDeliver.setText(PERMIT_FACTORY);
				ivjradioFactoryDeliver.setMaximumSize(
					new java.awt.Dimension(115, 22));
				ivjradioFactoryDeliver.setActionCommand(PERMIT_FACTORY);
				ivjradioFactoryDeliver.setMinimumSize(
					new java.awt.Dimension(115, 22));
				// user code begin {1}
				ivjradioFactoryDeliver.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioFactoryDeliver;
	}

	/**
	 * This method initializes ivjradioMotorcycle
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioMotorcycle()
	{
		if (ivjradioMotorcycle == null)
		{
			ivjradioMotorcycle = new JRadioButton();
			ivjradioMotorcycle.setBounds(18, 39, 122, 22);
			ivjradioMotorcycle.setName("ivjradioMotorcycle");
			ivjradioMotorcycle.setText("Motorcycle");
			ivjradioMotorcycle.setMnemonic(
				java.awt.event.KeyEvent.VK_M);
			ivjradioMotorcycle.addItemListener(this);
		}
		return ivjradioMotorcycle;
	}

	/**
	 * This method initializes ivjradioNone
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioNone()
	{
		if (ivjradioNone == null)
		{
			ivjradioNone = new JRadioButton();
			ivjradioNone.setBounds(125, 26, 21, 21);
			ivjradioNone.setName("ivjradioNone");
			ivjradioNone.setVisible(false);
			ivjradioNone.setEnabled(false);
			ivjradioNone.setSelected(true);
		}
		return ivjradioNone;
	}

	/**
	 * Return the ivjradioOneTripPermit
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioOneTripPermit()
	{
		if (ivjradioOneTripPermit == null)
		{
			try
			{
				ivjradioOneTripPermit = new javax.swing.JRadioButton();
				ivjradioOneTripPermit.setBounds(18, 94, 130, 22);
				ivjradioOneTripPermit.setName("ivjradioOneTripPermit");
				ivjradioOneTripPermit.setMnemonic(79);
				ivjradioOneTripPermit.setText(PERMIT_ONE_TRIP);
				ivjradioOneTripPermit.setMaximumSize(
					new java.awt.Dimension(114, 22));
				ivjradioOneTripPermit.setActionCommand(PERMIT_ONE_TRIP);
				ivjradioOneTripPermit.setMinimumSize(
					new java.awt.Dimension(114, 22));
				// user code begin {1}
				ivjradioOneTripPermit.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioOneTripPermit;
	}
	/**
	 * This method initializes ivjradioRegular
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getradioRegular()
	{
		if (ivjradioRegular == null)
		{
			ivjradioRegular = new JRadioButton();
			ivjradioRegular.setBounds(18, 13, 144, 21);
			ivjradioRegular.setText("Regular");
			ivjradioRegular.setName("ivjradioNone");
			ivjradioRegular.setMnemonic(java.awt.event.KeyEvent.VK_R);
			ivjradioRegular.addItemListener(this);
		}
		return ivjradioRegular;
	}

	/**
	 * Handle Exception
	 *  
	 * @param Throwable aeException
	 */
	private void handleException(Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7893
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7893
	}

	/**
	 * Handle Regular / Motorcycle 
	 * 
	 * @param abEnable 
	 */
	private void handleRegularMotorcycle(boolean abEnable)
	{
		getJPanelRegularMotorcycle().setEnabled(abEnable);
		getradioRegular().setEnabled(abEnable);
		getradioMotorcycle().setEnabled(abEnable);

		// defect 10844 
		if (abEnable
			&& (caPrmtData.isMotorcycle() || caPrmtData.isRegular()))
		{
			getradioMotorcycle().setSelected(caPrmtData.isMotorcycle());
			getradioRegular().setSelected(caPrmtData.isRegular());
		}
		else
		{
			getradioNone().setSelected(true);
		}
		// end defect 10844  
	}

	/**
	 * Initialize
	 * 
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmTimedPermitTypesMRG005");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(263, 373);
			setTitle(TITLE_MRG005);
			setContentPane(getFrmTimedPermitTypesMRG005ContentPane1());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Invoked when an item has been selected or deselected.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		clearAllColor(this);

		if (aaIE.getSource().equals(getradio144HourPermit())
			|| aaIE.getSource().equals(getradio30DayPermit())
			|| aaIE.getSource().equals(getradio72HourPermit())
			|| aaIE.getSource().equals(getradioOneTripPermit())
			|| aaIE.getSource().equals(getradioFactoryDeliver()))
		{
			if (aaIE.getStateChange() == ItemEvent.SELECTED)
			{
				handleRegularMotorcycle(
					getradioOneTripPermit().isSelected()
						|| getradio30DayPermit().isSelected());
			}
		}
		// defect 10844
		// TODO Consider resetting isMotorcycleByVINA()
		else if (
			aaIE.getSource().equals(getradioRegular())
				&& aaIE.getStateChange() == ItemEvent.SELECTED)
		{
			// 
		}
		// end defect 10844
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null && aaData instanceof PermitData)
		{

			// defect 10844 
			caPrmtData = (PermitData) UtilityMethods.copy(aaData);
			// end defect 10844 

			ciNo30DayPrmts = caPrmtData.getNo30DayPrmts();

			// defect 10844 
			cbModifyPermit =
				getController().getTransCode().equals(
					TransCdConstant.MODPT);

			if (cbModifyPermit)
			{
				getradio72HourPermit().setEnabled(false);
				getradio144HourPermit().setEnabled(false);
				getradioFactoryDeliver().setEnabled(false);

				if (caPrmtData.isOTPT() || caPrmtData.is30DayPT())
				{
					getradioOneTripPermit().setSelected(
						caPrmtData.isOTPT());
					getradioOneTripPermit().setEnabled(
						caPrmtData.isOTPT());
					getradio30DayPermit().setSelected(
						caPrmtData.is30DayPT());
					getradio30DayPermit().setEnabled(
						caPrmtData.is30DayPT());
					getradioRegular().setSelected(
						caPrmtData.isRegular());
					getradioRegular().setEnabled(true);
					getradioMotorcycle().setSelected(
						caPrmtData.isMotorcycle());
					getradioMotorcycle().setEnabled(true);
				}
			}
			else
			{
				caPrmtData.setBulkPrmtVendorId(new String());
				// defect 10989 
				caPrmtData.setAuditTrailTransId(new String());
				caPrmtData.setPrmtIssuanceId(new String());
				// end defect 10989 

				// end defect 10844 
				if (caPrmtData.isSameVeh())
				{
					getradioFactoryDeliver().setEnabled(false);
				}
				getJPanelRegularMotorcycle().setEnabled(false);
				getradioNone().setSelected(true);
				getradioRegular().setEnabled(false);
				getradioMotorcycle().setEnabled(false);
				// defect 10844 
			}
			// end defect 10844 
		}
	}

	/**
	 * Assign Data to Data Object for MRG006 
	 * 
	 */
	private void setDataToDataObject()
	{
		String lsItmCd = new String();
		String lsTimedPrmtType = new String();
		int liNo30DayPrmts = 0;

		// defect 10592
		// defect 10844 
		if (!cbModifyPermit)
		{
			caPrmtData.setOneTripData(null);
		}
		// end defect 10844 

		Object laMRG006 =
			getController().getMediator().openVault(
				ScreenConstant.MRG006);

		if (laMRG006 != null
			&& laMRG006 instanceof ScreenMRG006SavedData)
		{
			ScreenMRG006SavedData laMRG006Data =
				(ScreenMRG006SavedData) laMRG006;

			caPrmtData = laMRG006Data.getPrmtData();

			if (!getradioOneTripPermit().isSelected())
			{
				caPrmtData.setOneTripData(null);
			}
		}
		// defect 10844 
		else if (!caPrmtData.isSameVeh() && !cbModifyPermit)
		{
			// end defect 10844 
			caPrmtData.setCustomerData(new CustomerData());
		}
		// end defect 10592 

		if (getradio72HourPermit().isSelected())
		{
			lsTimedPrmtType = TransCdConstant.PT72;
			lsItmCd = TransCdConstant.PT72;
		}
		else if (getradio144HourPermit().isSelected())
		{
			lsTimedPrmtType = TransCdConstant.PT144;
			lsItmCd = TransCdConstant.PT144;
		}
		else if (getradioOneTripPermit().isSelected())
		{
			lsTimedPrmtType = TransCdConstant.OTPT;
			lsItmCd =
				getradioRegular().isSelected()
					? TransCdConstant.OTPT
					: "OTMCPT";
		}
		else if (getradioFactoryDeliver().isSelected())
		{
			lsTimedPrmtType = TransCdConstant.FDPT;
			lsItmCd = TransCdConstant.FDPT;
		}
		else if (getradio30DayPermit().isSelected())
		{
			lsTimedPrmtType = TransCdConstant.PT30;
			lsItmCd =
				getradioRegular().isSelected()
					? TransCdConstant.PT30
					: "30MCPT";
			liNo30DayPrmts = 1;
		}
		caPrmtData.setTimedPrmtType(lsTimedPrmtType);
		caPrmtData.setItmCd(lsItmCd);
		caPrmtData.setNo30DayPrmts(ciNo30DayPrmts + liNo30DayPrmts);

		// defect 10844 
		if (getradioMotorcycle().isSelected()
			|| getradioRegular().isSelected())
		{
			caPrmtData.setVehTypeCd(
				getradioMotorcycle().isSelected()
					? MiscellaneousRegConstant
						.PERMIT_MOTORCYCLE_VEHTYPECD
					: MiscellaneousRegConstant.PERMIT_REGULAR_VEHTYPECD);
		}
		// end defect 10844 
	}

	/**
	 * Validate Data
	 *
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbValid = true;

		if (getradio30DayPermit().isSelected()
			|| getradioOneTripPermit().isSelected())
		{
			if (getradioNone().isSelected())
			{
				RTSException leRTSEx = new RTSException();
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getradioRegular());

				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getradioMotorcycle());

				leRTSEx.displayError(this);
				lbValid = false;
			}
			else if (
				getradio30DayPermit().isSelected()
					&& caPrmtData.getNo30DayPrmts()
						>= MiscellaneousRegConstant
							.MIN_30_DAY_PERMITS_FOR_MSG)
			{
				new RTSException(
					ErrorsConstant
						.ERR_NUM_THREE_OR_MORE_30_DAY_PERMITS)
						.displayError(
					this);
			}
		}
		return lbValid;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="36,10"
