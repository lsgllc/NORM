package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * FrmInventoryProfileINV016.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Min Wang    	07/02/2003	Fixed defect 6062.Modified captureUserInput() 
 *                          and enableDisableYear() to make the Year 
 * 							field enabled for Antique, Antique tab, 
 * 							Military, and Antique MC plate.
 * Min Wang    	08/13/2003  Modified enableDisableYear() to Year field 
 * 							remains red when it is greyed out.
 *							Defect 6476. Ver 5.1.4
 * Min Wang    	08/18/2003	Modified captureUserInput()to fix Year is 
 * 							highlighted red when Year is greyed out.
 * 							Defect 6498. Ver 5.1.4
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify setcomboItmCdDesc(), setData()
 * 							Ver 5.2.0	
 * Ray Rowehl	02/19/2005	Do RTS 5.2.3 Code Cleanup
 * 							format source, rename fields, 
 * 							create constants
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/23/2005	Resized the radio buttons so all text
 * 							will display.  This was needed under WSAD.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/24/2005	remove setNextFocusables.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Fix tabbing
 * 							actually, this was fixed by the removing 
 * 							of all setNextFocusable() calls.
 * 							defect 7925 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3 
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							add ITEM_DESC
 * 							modify enableDisableYear(),
 * 							getstcLblItemCodeDescription(), 
 * 							setcomboItmCdDesc()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/10/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Setup to use default font and size when
 * 							doing bold.
 * 							Move constants to appropriate constants
 * 							classes.
 * 							modify getstcLblMaximumQuantity(),
 * 								getstcLblYear()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/18/2005	Remove selection from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	12/08/2005	Add keylisteners to buttons.			
 * 							modify getbtnAdd(), getbtnCancel(),
 * 							getbtnDelete(), getbtnHelp(),
 * 							getbtnRevise(), getbtnView()
 * 							defect 7890 Ver 5.2.3
 * Min Wang		12/13/2005	Fix the year field is not enabled when an 
 * 							item is selected that is suppose to have a 
 * 							year.
 * 							modify enableDisableYear()
 * 							defect 8472 Ver 5.2.2 Fix 8
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboEntityId(), 
 * 								setcomboItmCdDesc()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/29/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove carrRadioButton, ciSelctdRadioButton,
 * 								NUMBER_OF_RADIO_BUTTONS
 * 							add caRadioGrp
 * 							modify getpnlLayout(), actionPerformed(),
 * 								getradioCentral(), getradioDealer(),
 * 								getradioEmployee(), initialize(),
 * 								getradioSubcontractor(), 
 * 								getradioWorkstation(), keyPressed()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		01/20/2006	rename field 
 * 							modify enableDisableYear()
 * 							defect 8472 Ver 5.2.3
 * Mark Reyes	01/24/2006	Removed keylistener from buttons to fix the
 * 							escape problem.
 *							modify getbtnAdd(), getbtnCancel(),
 * 							getbtnDelete(), getbtnHelp(),
 * 							getbtnRevise(), getbtnView()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	02/06/2006	Modified for standard height of buttons 
 * 							modify getbtnAdd(),getbtnCancel(),
 * 							getbtnDelete(),getbtnHelp(),getbtnRevise(),
 * 							getbtnView()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		03/29/2006	Initialize EntityValue
 * 							modify setAdminLogData()
 * 							defect 8648 Ver 5.2.3
 * K Harrell	03/09/2007	Implement SystemProperty.isCounty()
 * 							setcomboListData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	09/04/2008	Standardize population of AdminLogData
 * 							Possibility of incorrect time being set in 
 * 							object. 
 * 							Add'l class cleanup.
 * 							add getAdminLogData() 
 * 							delete caALData
 * 							delete setAdminLogData()
 * 							modify actionPerformed() 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	06/25/2009	Implement new DealerData.  Implement 
 * 							 RTSButtonGroup.  
 * 							delete keyPressed()
 * 							modify setcomboListData(), 
 * 							  getFrmInventoryProfileINV016ContentPane1()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/22/2009	Implement new Contructor of AdminLogData
 * 							modify getAdminLogData() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify setcomboListData() 
 * 							defect 10161 Ver POS_640    
 * ---------------------------------------------------------------------
 */

/**
 * In the Profile event, frame INV016 handles the creation, 
 * modification, and deletion of inventory profiles for the various 
 * entities.
 *
 * @version	POS_640			02/18/2010	
 * @author	Sai Machavarapu 
 * <br>Creation Date:		06/28/2001
 */

public class FrmInventoryProfileINV016
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnRevise = null;
	private RTSButton ivjbtnView = null;
	private JCheckBox ivjchkPromtForNextItem = null;
	private JComboBox ivjcomboEntityId = null;
	private JComboBox ivjcomboItmCdDesc = null;
	private JPanel ivjFrmInventoryProfileINV016ContentPane1 = null;
	private JLabel ivjlblEntityId = null;
	private JPanel ivjpnlEntitySelection = null;
	private JPanel ivjpnlItemType = null;
	private JPanel ivjpnlLayout = null;
	private JRadioButton ivjradioCentral = null;
	private JRadioButton ivjradioDealer = null;
	private JRadioButton ivjradioEmployee = null;
	private JRadioButton ivjradioSubcontractor = null;
	private JRadioButton ivjradioWorkstation = null;
	private JLabel ivjstcLblItemCodeDescription = null;
	private JLabel ivjstcLblMaximumQuantity = null;
	private JLabel ivjstcLblMinimumQuantity = null;
	private JLabel ivjstcLblYear = null;
	private RTSInputField ivjtxtMaxQty = null;
	private RTSInputField ivjtxtMinQty = null;
	private RTSInputField ivjtxtYear = null;

	/**
	 * Data object used to store the Subcontractors, Workstations, 
	 * Dealers, and Employees for the combo box, and the Substations
	 */
	private AllocationDbData caAlloctnDbData = new AllocationDbData();

	/**
	* InventoryAllocationUIData object used to collect the UI data
	*/
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * Data object used to store the current inventory profile
	 */
	private InventoryProfileData caIPData = new InventoryProfileData();

	private RTSButtonGroup caRadioGrp = null;
	private boolean cbInit = true;
	private boolean cbIPNull = false;

	/**
	 * Vector used to store the Dealers for the combo box
	 */
	private Vector cvDlrComboData = new Vector();

	/**
	 * Vector used to store the Employees for the combo box
	 */
	private Vector cvEmpComboData = new Vector();

	/**
	 * Vector used to store the inventory patterns returned from cache
	 */
	private Vector cvInvPatrns = new Vector();

	/**
	 * Vector used to store the inventory item codes returned from cache
	 */
	private Vector cvItmCdsData = new Vector();

	/**
	 * Vector used to store the Subcontractors for the combo box
	 */
	private Vector cvSubconComboData = new Vector();

	/**
	 * Vector used to store the Workstations for the combo box
	 */
	private Vector cvWsComboData = new Vector();

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInventoryProfileINV016 laFrmInventoryProfileINV016;
			laFrmInventoryProfileINV016 =
				new FrmInventoryProfileINV016();
			laFrmInventoryProfileINV016.setModal(true);
			laFrmInventoryProfileINV016
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laWE)
				{
					System.exit(0);
				}
			});
			laFrmInventoryProfileINV016.show();
			java.awt.Insets insets =
				laFrmInventoryProfileINV016.getInsets();
			laFrmInventoryProfileINV016.setSize(
				laFrmInventoryProfileINV016.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryProfileINV016.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryProfileINV016.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmInventoryProfileINV016 constructor comment.
	 */
	public FrmInventoryProfileINV016()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryProfileINV016 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryProfileINV016(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryProfileINV016 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryProfileINV016(JFrame aaParent)
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
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// Returns all fields to their original color state
			clearAllColor(this);
			if (aaAE.getSource() instanceof JRadioButton)
			{
				setpnlEntityId();
				if (!cbInit)
				{
					getController().processData(
						InventoryConstant.GET_INVENTORY_PROFILE,
						caIPData);
					if (cbIPNull)
					{
						setProfileStatusOnFrame(null);
					}
					else
					{
						setProfileStatusOnFrame(caIPData);
					}
				}
			}
			else if (aaAE.getSource() == getbtnAdd())
			{
				if (captureUserInput())
				{
					Vector lvSendData = new Vector();
					lvSendData.add(caIPData);
					// defect 8595 
					lvSendData.add(
						getAdminLogData(
							CommonConstant.TXT_ADMIN_LOG_ADD));
					// end defect 8595 
					getController().processData(
						InventoryConstant.ADD_INVENTORY_PROFILE,
						lvSendData);
					getbtnAdd().setEnabled(false);
					getbtnDelete().setEnabled(true);
					getbtnRevise().setEnabled(true);
					gettxtMinQty().setText(
						Integer.toString(caIPData.getMinQty()));
					gettxtMaxQty().setText(
						Integer.toString(caIPData.getMaxQty()));
					caRadioGrp.setFocusOnSelected();
				}
				else
				{
					return;
				}
			}
			else if (aaAE.getSource() == getbtnRevise())
			{
				if (captureUserInput())
				{
					Vector lvSendData = new Vector();
					lvSendData.add(caIPData);
					// defect 8595 
					lvSendData.add(
						getAdminLogData(
							CommonConstant.TXT_ADMIN_LOG_REVISE));
					// end defect 8595 
					getController().processData(
						InventoryConstant.REVISE_INVENTORY_PROFILE,
						lvSendData);
					getbtnAdd().setEnabled(false);
					getbtnDelete().setEnabled(true);
					getbtnRevise().setEnabled(true);
					gettxtMinQty().setText(
						Integer.toString(caIPData.getMinQty()));
					gettxtMaxQty().setText(
						Integer.toString(caIPData.getMaxQty()));
					caRadioGrp.setFocusOnSelected();
				}
				else
				{
					return;
				}
			}
			else if (aaAE.getSource() == getbtnDelete())
			{
				Vector lvSendData = new Vector();
				lvSendData.add(caIPData);
				// defect 8595 
				lvSendData.add(
					getAdminLogData(
						CommonConstant.TXT_ADMIN_LOG_DELETE));
				// end defect 8595 
				getController().processData(
					InventoryConstant.DELETE_INVENTORY_PROFILE,
					lvSendData);
				getbtnAdd().setEnabled(true);
				getbtnDelete().setEnabled(false);
				getbtnRevise().setEnabled(false);
				gettxtMaxQty().setText(CommonConstant.STR_SPACE_EMPTY);
				gettxtMinQty().setText(CommonConstant.STR_SPACE_EMPTY);
				caRadioGrp.setFocusOnSelected();
			}
			else if (aaAE.getSource() == getbtnView())
			{
				getController().processData(
					VCInventoryProfileINV016.VIEW,
					caIPData.getEntity());
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV016);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Captures the user input before sending the data to the business 
	 * layer for processing.
	 * 
	 * <p>Returns a boolean indicating success.
	 * 
	 * @return boolean
	 */
	private boolean captureUserInput()
	{
		int liMinQty = 0;
		int liMaxQty = 0;
		if (!gettxtMinQty()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			liMinQty = Integer.parseInt(gettxtMinQty().getText());
		}
		if (!gettxtMaxQty()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			liMaxQty = Integer.parseInt(gettxtMaxQty().getText());
		}
		if (liMaxQty < liMinQty)
		{
			RTSException leRTSException = new RTSException();
			leRTSException.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MAX_QTY_IS_LESS_THAN_MIN),
				gettxtMaxQty());
			leRTSException.displayError(this);
			leRTSException.getFirstComponent().requestFocus();
			return false;
		}
		caIPData.setMinQty(liMinQty);
		caIPData.setMaxQty(liMaxQty);
		caIPData.setInvItmYr(Integer.parseInt(gettxtYear().getText()));

		// Pass InventoryPatternsCache.NO_YEAR into getInvPatrns() to 
		// decide if item year should be enabled.
		Vector lvCachePtrn = null;
		lvCachePtrn =
			InventoryPatternsCache.getInvPatrns(
				caIPData.getItmCd(),
				caIPData.getInvItmYr());
		if ((lvCachePtrn == null || lvCachePtrn.size() < 1)
			&& gettxtYear().isEnabled())
		{
			RTSException leRTSException = new RTSException();
			leRTSException.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_ITM_YEAR_NOT_VALID),
				gettxtYear());
			leRTSException.displayError(this);
			leRTSException.getFirstComponent().requestFocus();
			return false;
		}

		if (getchkPromtForNextItem().isSelected())
		{
			caIPData.setNextAvailIndi(1);
		}
		else
		{
			caIPData.setNextAvailIndi(0);
		}
		return true;
	}

	/**
	 * Based on ItemCode, year is enabled or disabled and set to 
	 * the current year + 1.
	 */
	private void enableDisableYear()
	{
		String lsItmCdDesc = new String(CommonConstant.STR_SPACE_EMPTY);
		lsItmCdDesc = (String) getcomboItmCdDesc().getSelectedItem();
		// defect 8269
		//laStr = laStr.substring(0, laStr.indexOf(" "));

		String lsItmCd = CommonConstant.STR_SPACE_EMPTY;
		Vector lvData = new Vector();
		try
		{
			Vector lvData3 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_SPECIAL,
					CommonConstant.STR_SPACE_ONE);
			Vector lvData2 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_RESTRICTED,
					CommonConstant.STR_SPACE_ONE);
			Vector lvData1 =
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_NORMAL,
					CommonConstant.STR_SPACE_ONE);
			lvData.addAll(lvData3);
			lvData.addAll(lvData2);
			lvData.addAll(lvData1);
		}
		catch (RTSException aeRTSEx)
		{
			// no action to take for this
		}
		for (Iterator laItemSearch = lvData.iterator();
			laItemSearch.hasNext();
			)
		{
			ItemCodesData laTempItemCode =
				(ItemCodesData) laItemSearch.next();
			if (laTempItemCode.getItmCdDesc().equals(lsItmCdDesc))
			{
				lsItmCd = laTempItemCode.getItmCd();
				break;
			}
		}
		caIPData.setItmCd(lsItmCd);
		// end defect 8269

		InventoryPatternsData laInvPatt = null;
		RTSDate laDate = new RTSDate();

		// Pass InventoryPatternsCache.NO_YEAR into getInvPatrns() 
		// to decide item year enable or disable.
		// cvInvPatrns = InventoryPatternsCache.getInvPatrns(lsItmCdDesc, 
		// (lDate.getYear()+1));

		// defect 8472
		//cvInvPatrns =
		//	InventoryPatternsCache.getInvPatrns(
		//		lsItmCdDesc,
		//		InventoryPatternsCache.NO_YEAR);
		cvInvPatrns =
			InventoryPatternsCache.getInvPatrns(
				lsItmCd,
				InventoryPatternsCache.NO_YEAR);
		// end defect 8472

		clearAllColor(this);
		boolean lbItmCdFound = false;
		if (cvInvPatrns != null)
		{
			for (int i = 0; i < cvInvPatrns.size(); i++)
			{
				laInvPatt =
					(InventoryPatternsData) cvInvPatrns.elementAt(i);
				// defect 8472
				//if (laStr.equals(laInvPatt.getItmCd()))
				if (lsItmCd.equals(laInvPatt.getItmCd()))
				{
					lbItmCdFound = true;
					if (laInvPatt.getInvItmYr() == 0)
					{
						gettxtYear().setText(CommonConstant.STR_ZERO);
						gettxtYear().setEnabled(false);
						getstcLblYear().setEnabled(false);
					}
					else
					{
						gettxtYear().setEnabled(true);
						getstcLblYear().setEnabled(true);
						gettxtYear().setText(
							String.valueOf(laDate.getYear() + 1));
					}
				}
				// end defect 8472
				if (lbItmCdFound)
				{
					break;
				}
			}
		}
		if (!lbItmCdFound)
		{
			gettxtYear().setText(CommonConstant.STR_ZERO);
			gettxtYear().setEnabled(false);
			getstcLblYear().setEnabled(false);
		}
		caIPData.setInvItmYr(Integer.parseInt(gettxtYear().getText()));
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * <p>We do not take any action on this event here.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// empty code block
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (aaFE.getSource() == gettxtYear())
		{
			if (!gettxtYear().isEnabled())
			{
				return;
			}
			String lsYear = gettxtYear().getText();
			if (lsYear.length() != CommonConstant.LENGTH_YEAR)
			{
				return;
			}
			int liYear = Integer.parseInt(lsYear);
			if (caIPData.getInvItmYr() != liYear)
			{
				caIPData.setInvItmYr(liYear);
				if (!cbInit)
				{
					getController().processData(
						InventoryConstant.GET_INVENTORY_PROFILE,
						caIPData);
					if (cbIPNull)
					{
						setProfileStatusOnFrame(null);
					}
					else
					{
						setProfileStatusOnFrame(caIPData);
					}
				}
			}
		}
	}

	/**
	 * Returns Admin Log data object for writing to Admin Log.
	 * 
	 * @param asAction
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(String asAction)
	{
		// defect 8628 
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		StringBuffer lsStr = new StringBuffer();
		laAdminLogData.setEntity(InventoryConstant.TXT_INVPROFILE);
		laAdminLogData.setAction(asAction);
		lsStr = new StringBuffer();
		lsStr.append(caIPData.getEntity());
		lsStr.append(CommonConstant.STR_DASH);
		lsStr.append(caIPData.getId());
		lsStr.append(CommonConstant.STR_DASH);
		lsStr.append(caIPData.getItmCd());
		lsStr.append(CommonConstant.STR_DASH);
		lsStr.append(caIPData.getInvItmYr());
		laAdminLogData.setEntityValue(lsStr.toString());
		return laAdminLogData;
	}

	/**
	 * Return the ivjbtnAdd property value.
	 * 
	 * @return RTSButton
	 */

	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setName("ivjbtnAdd");
				ivjbtnAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMaximumSize(
					new java.awt.Dimension(57, 25));
				ivjbtnAdd.setActionCommand(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setBounds(20, 298, 73, 25);
				ivjbtnAdd.setMinimumSize(
					new java.awt.Dimension(57, 25));
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
		return ivjbtnAdd;
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
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMaximumSize(
					new java.awt.Dimension(73, 25));
				ivjbtnCancel.setMinimumSize(
					new java.awt.Dimension(73, 25));
				ivjbtnCancel.setActionCommand(
					CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setBounds(391, 298, 73, 25);
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
		return ivjbtnCancel;
	}

	/**
	 * Return the ivjbtnDelete property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			try
			{
				ivjbtnDelete = new RTSButton();
				ivjbtnDelete.setName("ivjbtnDelete");
				ivjbtnDelete.setMnemonic(java.awt.event.KeyEvent.VK_L);
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMaximumSize(
					new java.awt.Dimension(71, 25));
				ivjbtnDelete.setActionCommand(
					CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setBounds(178, 298, 78, 25);
				ivjbtnDelete.setMinimumSize(
					new java.awt.Dimension(71, 25));
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
		return ivjbtnDelete;
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
				ivjbtnHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setMaximumSize(
					new java.awt.Dimension(59, 25));
				ivjbtnHelp.setActionCommand(
					CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setBounds(470, 298, 73, 25);
				ivjbtnHelp.setMinimumSize(
					new java.awt.Dimension(59, 25));
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
		return ivjbtnHelp;
	}

	/**
	 * Return the ivjbtnRevise property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setName("ivjbtnRevise");
				ivjbtnRevise.setMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMaximumSize(
					new java.awt.Dimension(73, 25));
				ivjbtnRevise.setActionCommand(
					CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setBounds(99, 298, 73, 25);
				ivjbtnRevise.setMinimumSize(
					new java.awt.Dimension(73, 25));
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
		return ivjbtnRevise;
	}

	/**
	 * Return the ivjbtnView property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnView()
	{
		if (ivjbtnView == null)
		{
			try
			{
				ivjbtnView = new RTSButton();
				ivjbtnView.setName("ivjbtnView");
				ivjbtnView.setMnemonic(java.awt.event.KeyEvent.VK_V);
				ivjbtnView.setText(CommonConstant.BTN_TXT_VIEW);
				ivjbtnView.setMaximumSize(
					new java.awt.Dimension(63, 25));
				ivjbtnView.setActionCommand(
					CommonConstant.BTN_TXT_VIEW);
				ivjbtnView.setBounds(311, 298, 73, 25);
				ivjbtnView.setMinimumSize(
					new java.awt.Dimension(63, 25));
				// user code begin {1}
				this.getRootPane().setDefaultButton(getbtnView());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnView;
	}

	/**
	 * Return the ivjchkPromtForNextItem property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPromtForNextItem()
	{
		if (ivjchkPromtForNextItem == null)
		{
			try
			{
				ivjchkPromtForNextItem = new JCheckBox();
				ivjchkPromtForNextItem.setName("ivjchkPromtForNextItem");
				ivjchkPromtForNextItem.setMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjchkPromtForNextItem.setText(
					InventoryConstant.TXT_PROMPT_FOR_NEXT_ITEM_COLON);
				ivjchkPromtForNextItem.setHorizontalTextPosition(
					javax.swing.SwingConstants.LEFT);
				ivjchkPromtForNextItem.setBounds(49, 255, 167, 23);
				ivjchkPromtForNextItem.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjchkPromtForNextItem;
	}

	/**
	 * Return the ivjcomboEntityId property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboEntityId()
	{
		if (ivjcomboEntityId == null)
		{
			try
			{
				ivjcomboEntityId = new JComboBox();
				ivjcomboEntityId.setName("ivjcomboEntityId");
				ivjcomboEntityId.setBackground(java.awt.Color.white);
				ivjcomboEntityId.setBounds(145, 78, 316, 23);
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
		return ivjcomboEntityId;
	}

	/**
	 * Return the ivjcomboItmCdDesc property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboItmCdDesc()
	{
		if (ivjcomboItmCdDesc == null)
		{
			try
			{
				ivjcomboItmCdDesc = new JComboBox();
				ivjcomboItmCdDesc.setName("ivjcomboItmCdDesc");
				ivjcomboItmCdDesc.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboItmCdDesc.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboItmCdDesc.setBackground(java.awt.Color.white);
				ivjcomboItmCdDesc.setBounds(15, 31, 322, 23);
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
		return ivjcomboItmCdDesc;
	}

	/**
	 * Return the ivjFrmInventoryProfileINV016ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmInventoryProfileINV016ContentPane1()
	{
		if (ivjFrmInventoryProfileINV016ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryProfileINV016ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryProfileINV016ContentPane1.setName(
					"ivjFrmInventoryProfileINV016ContentPane1");
				ivjFrmInventoryProfileINV016ContentPane1.setLayout(
					null);
				ivjFrmInventoryProfileINV016ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(588, 412));
				ivjFrmInventoryProfileINV016ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(588, 412));
				ivjFrmInventoryProfileINV016ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmInventoryProfileINV016ContentPane1().add(
					getpnlEntitySelection(),
					getpnlEntitySelection().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getpnlItemType(),
					getpnlItemType().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getstcLblMinimumQuantity(),
					getstcLblMinimumQuantity().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getstcLblMaximumQuantity(),
					getstcLblMaximumQuantity().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					gettxtMaxQty(),
					gettxtMaxQty().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					gettxtMinQty(),
					gettxtMinQty().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getchkPromtForNextItem(),
					getchkPromtForNextItem().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnAdd(),
					getbtnAdd().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnDelete(),
					getbtnDelete().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnView(),
					getbtnView().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmInventoryProfileINV016ContentPane1().add(
					getbtnHelp(),
					getbtnHelp().getName());
				// user code begin {1}
				getbtnAdd().addActionListener(this);
				getbtnRevise().addActionListener(this);
				getbtnDelete().addActionListener(this);
				getbtnView().addActionListener(this);
				getbtnCancel().addActionListener(this);
				getbtnHelp().addActionListener(this);
				
				// defect 10112 
				RTSButtonGroup laButtonGroup = new RTSButtonGroup(); 
				laButtonGroup.add(getbtnAdd());
				laButtonGroup.add(getbtnRevise());
				laButtonGroup.add(getbtnDelete());
				laButtonGroup.add(getbtnView());
				laButtonGroup.add(getbtnCancel());
				laButtonGroup.add(getbtnHelp());
				// end defect 10112 
				
				getcomboItmCdDesc().addItemListener(this);
				getcomboEntityId().addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmInventoryProfileINV016ContentPane1;
	}

	/**
	 * Return the ivjlblEntityId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEntityId()
	{
		if (ivjlblEntityId == null)
		{
			try
			{
				ivjlblEntityId = new JLabel();
				ivjlblEntityId.setName("ivjlblEntityId");
				ivjlblEntityId.setText(InventoryConstant.TXT_ENTITY_ID);
				ivjlblEntityId.setBounds(6, 82, 129, 14);
				ivjlblEntityId.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblEntityId;
	}

	/**
	 * Return the ivjpnlEntitySelection property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlEntitySelection()
	{
		if (ivjpnlEntitySelection == null)
		{
			try
			{
				ivjpnlEntitySelection = new JPanel();
				ivjpnlEntitySelection.setName("ivjpnlEntitySelection");
				ivjpnlEntitySelection.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_ENTITY_SELECTION_COLON));
				ivjpnlEntitySelection.setLayout(null);
				ivjpnlEntitySelection.setMaximumSize(
					new java.awt.Dimension(520, 88));
				ivjpnlEntitySelection.setMinimumSize(
					new java.awt.Dimension(520, 88));
				ivjpnlEntitySelection.setBounds(26, 12, 517, 111);
				getpnlEntitySelection().add(
					getlblEntityId(),
					getlblEntityId().getName());
				getpnlEntitySelection().add(
					getcomboEntityId(),
					getcomboEntityId().getName());
				getpnlEntitySelection().add(
					getpnlLayout(),
					getpnlLayout().getName());
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
		return ivjpnlEntitySelection;
	}

	/**
	 * Return the ivjpnlItemType property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlItemType()
	{
		if (ivjpnlItemType == null)
		{
			try
			{
				ivjpnlItemType = new JPanel();
				ivjpnlItemType.setName("ivjpnlItemType");
				ivjpnlItemType.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_ITEM_TYPE_COLON));
				ivjpnlItemType.setLayout(null);
				ivjpnlItemType.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlItemType.setMinimumSize(
					new java.awt.Dimension(520, 110));
				ivjpnlItemType.setBounds(26, 130, 517, 66);
				getpnlItemType().add(
					getcomboItmCdDesc(),
					getcomboItmCdDesc().getName());
				getpnlItemType().add(
					getstcLblYear(),
					getstcLblYear().getName());
				getpnlItemType().add(
					gettxtYear(),
					gettxtYear().getName());
				getpnlItemType().add(
					getstcLblItemCodeDescription(),
					getstcLblItemCodeDescription().getName());
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
		return ivjpnlItemType;
	}

	/**
	 * Return the ivjpnlLayout property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlLayout()
	{
		if (ivjpnlLayout == null)
		{
			try
			{
				ivjpnlLayout = new JPanel();
				ivjpnlLayout.setName("ivjpnlLayout");
				ivjpnlLayout.setLayout(null);
				ivjpnlLayout.setBounds(5, 17, 507, 55);
				getpnlLayout().add(
					getradioCentral(),
					getradioCentral().getName());
				getpnlLayout().add(
					getradioWorkstation(),
					getradioWorkstation().getName());
				getpnlLayout().add(
					getradioSubcontractor(),
					getradioSubcontractor().getName());
				getpnlLayout().add(
					getradioDealer(),
					getradioDealer().getName());
				getpnlLayout().add(
					getradioEmployee(),
					getradioEmployee().getName());
				// user code begin {1}
				// defect 7890
				caRadioGrp = new RTSButtonGroup();
				caRadioGrp.add(getradioCentral());
				caRadioGrp.add(getradioWorkstation());
				caRadioGrp.add(getradioSubcontractor());
				caRadioGrp.add(getradioDealer());
				caRadioGrp.add(getradioEmployee());
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlLayout;
	}

	/**
	 * Return the ivjradioCentral property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCentral()
	{
		if (ivjradioCentral == null)
		{
			try
			{
				ivjradioCentral = new JRadioButton();
				ivjradioCentral.setName("ivjradioCentral");
				ivjradioCentral.setMnemonic(
					java.awt.event.KeyEvent.VK_C);
				ivjradioCentral.setText(InventoryConstant.TXT_CENTRAL);
				ivjradioCentral.setMaximumSize(
					new java.awt.Dimension(66, 22));
				ivjradioCentral.setActionCommand(
					InventoryConstant.TXT_CENTRAL);
				ivjradioCentral.setBounds(16, 21, 73, 22);
				ivjradioCentral.setMinimumSize(
					new java.awt.Dimension(66, 22));
				// user code begin {1}
				ivjradioCentral.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCentral;
	}

	/**
	 * Return the ivjradioDealer property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDealer()
	{
		if (ivjradioDealer == null)
		{
			try
			{
				ivjradioDealer = new JRadioButton();
				ivjradioDealer.setName("ivjradioDealer");
				ivjradioDealer.setMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjradioDealer.setText(InventoryConstant.TXT_DEALER);
				ivjradioDealer.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjradioDealer.setActionCommand(
					InventoryConstant.TXT_DEALER);
				ivjradioDealer.setBounds(331, 21, 69, 22);
				ivjradioDealer.setMinimumSize(
					new java.awt.Dimension(62, 22));
				// user code begin {1}
				ivjradioDealer.addActionListener(this);
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
	 * Return the ivjradioEmployee property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioEmployee()
	{
		if (ivjradioEmployee == null)
		{
			try
			{
				ivjradioEmployee = new JRadioButton();
				ivjradioEmployee.setName("ivjradioEmployee");
				ivjradioEmployee.setMnemonic(
					java.awt.event.KeyEvent.VK_E);
				ivjradioEmployee.setText(
					InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmployee.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmployee.setActionCommand(
					InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmployee.setBounds(408, 21, 87, 22);
				ivjradioEmployee.setMinimumSize(
					new java.awt.Dimension(80, 22));
				// user code begin {1}
				ivjradioEmployee.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioEmployee;
	}

	/**
	 * Return the ivjradioSubcontractor property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSubcontractor()
	{
		if (ivjradioSubcontractor == null)
		{
			try
			{
				ivjradioSubcontractor = new JRadioButton();
				ivjradioSubcontractor.setName("ivjradioSubcontractor");
				ivjradioSubcontractor.setMnemonic(
					java.awt.event.KeyEvent.VK_U);
				ivjradioSubcontractor.setText(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcontractor.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjradioSubcontractor.setActionCommand(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcontractor.setBounds(207, 21, 108, 22);
				ivjradioSubcontractor.setMinimumSize(
					new java.awt.Dimension(107, 22));
				// user code begin {1}
				ivjradioSubcontractor.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSubcontractor;
	}

	/**
	 * Return the ivjradioWorkstation property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioWorkstation()
	{
		if (ivjradioWorkstation == null)
		{
			try
			{
				ivjradioWorkstation = new JRadioButton();
				ivjradioWorkstation.setName("ivjradioWorkstation");
				ivjradioWorkstation.setMnemonic(87);
				ivjradioWorkstation.setText(
					InventoryConstant.TXT_WORKSTATION);
				ivjradioWorkstation.setMaximumSize(
					new java.awt.Dimension(95, 22));
				ivjradioWorkstation.setActionCommand(
					InventoryConstant.TXT_WORKSTATION);
				ivjradioWorkstation.setBounds(97, 21, 104, 22);
				ivjradioWorkstation.setMinimumSize(
					new java.awt.Dimension(95, 22));
				// user code begin {1}
				ivjradioWorkstation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioWorkstation;
	}

	/**
	 * Return the ivjstcLblItemCodeDescription property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblItemCodeDescription()
	{
		if (ivjstcLblItemCodeDescription == null)
		{
			try
			{
				ivjstcLblItemCodeDescription = new JLabel();
				ivjstcLblItemCodeDescription.setName(
					"ivjstcLblItemCodeDescription");
				ivjstcLblItemCodeDescription.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjstcLblItemCodeDescription.setText(
					InventoryConstant.TXT_ITEM_DESCRIPTION);
				ivjstcLblItemCodeDescription.setMaximumSize(
					new java.awt.Dimension(135, 14));
				ivjstcLblItemCodeDescription.setLabelFor(
					getcomboItmCdDesc());
				ivjstcLblItemCodeDescription.setBounds(18, 11, 225, 18);
				ivjstcLblItemCodeDescription.setMinimumSize(
					new java.awt.Dimension(135, 14));
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
		return ivjstcLblItemCodeDescription;
	}

	/**
	 * Return the ivjstcLblMaximumQuantity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMaximumQuantity()
	{
		if (ivjstcLblMaximumQuantity == null)
		{
			try
			{
				ivjstcLblMaximumQuantity = new JLabel();
				ivjstcLblMaximumQuantity.setName(
					"ivjstcLblMaximumQuantity");
				ivjstcLblMaximumQuantity.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjstcLblMaximumQuantity.setText(
					InventoryConstant.TXT_MAX_QTY_COLON);
				ivjstcLblMaximumQuantity.setMaximumSize(
					new java.awt.Dimension(109, 14));
				ivjstcLblMaximumQuantity.setLabelFor(gettxtMaxQty());
				ivjstcLblMaximumQuantity.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
				ivjstcLblMaximumQuantity.setBounds(73, 210, 119, 20);
				ivjstcLblMaximumQuantity.setMinimumSize(
					new java.awt.Dimension(109, 14));
				ivjstcLblMaximumQuantity.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblMaximumQuantity;
	}

	/**
	 * Return the ivjstcLblMinimumQuantity property value.
	 * 
	 * @return JLabel
	 */
	private javax.swing.JLabel getstcLblMinimumQuantity()
	{
		if (ivjstcLblMinimumQuantity == null)
		{
			try
			{
				ivjstcLblMinimumQuantity = new JLabel();
				ivjstcLblMinimumQuantity.setName(
					"ivjstcLblMinimumQuantity");
				ivjstcLblMinimumQuantity.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Q);
				ivjstcLblMinimumQuantity.setText(
					InventoryConstant.TXT_MIN_QTY_COLON);
				ivjstcLblMinimumQuantity.setMaximumSize(
					new java.awt.Dimension(105, 14));
				ivjstcLblMinimumQuantity.setLabelFor(gettxtMinQty());
				ivjstcLblMinimumQuantity.setBounds(76, 234, 116, 20);
				ivjstcLblMinimumQuantity.setMinimumSize(
					new java.awt.Dimension(105, 14));
				ivjstcLblMinimumQuantity.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblMinimumQuantity;
	}

	/**
	 * Return the ivjstcLblYear property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblYear()
	{
		if (ivjstcLblYear == null)
		{
			try
			{
				ivjstcLblYear = new JLabel();
				ivjstcLblYear.setName("ivjstcLblYear");
				ivjstcLblYear.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Y);
				ivjstcLblYear.setText(InventoryConstant.TXT_YEAR_COLON);
				ivjstcLblYear.setMaximumSize(
					new java.awt.Dimension(29, 14));
				ivjstcLblYear.setLabelFor(gettxtYear());
				ivjstcLblYear.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
				ivjstcLblYear.setBounds(362, 31, 35, 20);
				ivjstcLblYear.setMinimumSize(
					new java.awt.Dimension(29, 14));
				ivjstcLblYear.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblYear;
	}

	/**
	 * Return the ivjtxtMaxQty property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMaxQty()
	{
		if (ivjtxtMaxQty == null)
		{
			try
			{
				ivjtxtMaxQty = new RTSInputField();
				ivjtxtMaxQty.setName("ivjtxtMaxQty");
				ivjtxtMaxQty.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMaxQty.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtMaxQty.setBounds(204, 210, 85, 20);
				ivjtxtMaxQty.setMaxLength(
					InventoryConstant.MAX_QTY_LENGTH);
				ivjtxtMaxQty.setHorizontalAlignment(
					javax.swing.JTextField.LEFT);
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
		return ivjtxtMaxQty;
	}

	/**
	 * Return the ivjtxtMinQty property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMinQty()
	{
		if (ivjtxtMinQty == null)
		{
			try
			{
				ivjtxtMinQty = new RTSInputField();
				ivjtxtMinQty.setName("ivjtxtMinQty");
				ivjtxtMinQty.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMinQty.setInput(1);
				ivjtxtMinQty.setBounds(204, 234, 85, 20);
				ivjtxtMinQty.setMaxLength(
					InventoryConstant.MAX_QTY_LENGTH);
				ivjtxtMinQty.setHorizontalAlignment(
					javax.swing.JTextField.LEFT);
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
		return ivjtxtMinQty;
	}

	/**
	 * Return the ivjtxtYear property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setName("ivjtxtYear");
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYear.setBounds(403, 31, 59, 20);
				ivjtxtYear.setMaxLength(CommonConstant.LENGTH_YEAR);
				// user code begin {1}
				ivjtxtYear.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtYear;
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
			setName(ScreenConstant.INV016_FRAME_NAME);
			setSize(570, 366);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV016_FRAME_TITLE);
			setContentPane(getFrmInventoryProfileINV016ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
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
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// Code to prevent multiple button clicks
		if (isWorking())
		{
			return;
		}
		else
		{
			setWorking(true);
		}
		try
		{
			if (!cbInit)
			{
				if (aaIE.getSource() == getcomboItmCdDesc()
					&& aaIE.getStateChange() == ItemEvent.SELECTED)
				{
					enableDisableYear();
					getController().processData(
						InventoryConstant.GET_INVENTORY_PROFILE,
						caIPData);
					if (cbIPNull)
					{
						setProfileStatusOnFrame(null);
					}
					else
					{
						setProfileStatusOnFrame(caIPData);
					}
				}
				else if (
					aaIE.getSource() == getcomboEntityId()
						&& aaIE.getStateChange() == ItemEvent.SELECTED)
				{
					if (!getradioCentral().isSelected())
					{
						if (getradioEmployee().isSelected()
							&& getcomboEntityId()
								.getSelectedItem()
								.equals(
								InventoryConstant.TXT_ALL_EMPLOYEES))
						{
							caIPData.setId(
								InventoryConstant.TXT_DEFAULT);
						}
						else
						{
							String laStr =
								new String(
									CommonConstant.STR_SPACE_EMPTY);
							laStr =
								(String) getcomboEntityId()
									.getSelectedItem();
							if (!getradioWorkstation().isSelected())
							{
								laStr =
									laStr.substring(
										0,
										laStr.indexOf(
											CommonConstant
												.STR_SPACE_ONE));
							}
							caIPData.setId(laStr);
						}
					}
					else
					{
						caIPData.setId(CommonConstant.STR_ZERO);
					}
					getController().processData(
						InventoryConstant.GET_INVENTORY_PROFILE,
						caIPData);
					if (cbIPNull)
					{
						setProfileStatusOnFrame(null);
					}
					else
					{
						setProfileStatusOnFrame(caIPData);
					}
				}
			}
		}
		finally
		{
			setWorking(false);
		}
	}

	/**
	 * Used to fill the Select FROM Entity Id combobox with the 
	 * appropriate info depending on which radio button is selected.
	 */
	private void setcomboEntityId()
	{
		if (getradioCentral().isSelected())
		{
			getcomboEntityId().removeAllItems();
		}
		else if (getradioWorkstation().isSelected())
		{
			getcomboEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvWsComboData));
			getcomboEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboEntityId());
			// end defect 8479
		}
		else if (getradioSubcontractor().isSelected())
		{
			getcomboEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvSubconComboData));
			getcomboEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboEntityId());
			// end defect 8479
		}
		else if (getradioDealer().isSelected())
		{
			getcomboEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvDlrComboData));
			getcomboEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboEntityId());
			// end defect 8479
		}
		else if (getradioEmployee().isSelected())
		{
			getcomboEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvEmpComboData));
			getcomboEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboEntityId());
			// end defect 8479
		}

		if (!getradioCentral().isSelected())
		{
			if (getradioEmployee().isSelected()
				&& getcomboEntityId().getSelectedItem().equals(
					InventoryConstant.TXT_ALL_EMPLOYEES))
				caIPData.setId(InventoryConstant.TXT_DEFAULT);
			else
			{
				String lStr =
					new String(CommonConstant.STR_SPACE_EMPTY);
				lStr = (String) getcomboEntityId().getSelectedItem();
				if (!getradioWorkstation().isSelected())
				{
					lStr =
						lStr.substring(
							0,
							lStr.indexOf(CommonConstant.STR_SPACE_ONE));
				}
				caIPData.setId(lStr);
			}
		}
		else
		{
			caIPData.setId(CommonConstant.STR_ZERO);
		}
	}

	/**
	 * Sets the item code description combo box
	 */
	private void setcomboItmCdDesc()
	{
		Vector lvSort = new Vector();
		String lsStr = new String(CommonConstant.STR_SPACE_EMPTY);
		ItemCodesData laItemCodesData = new ItemCodesData();
		try
		{
			cvItmCdsData.addAll(
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_NORMAL,
					CommonConstant.STR_SPACE_EMPTY,
					true));
			cvItmCdsData.addAll(
				ItemCodesCache.getItmCds(
					ItemCodesCache.PROCSNGCD,
					InventoryConstant.INV_PROCSNGCD_SPECIAL,
					CommonConstant.STR_SPACE_EMPTY,
					true));

			// Check to see if a regional office
			// defect 9085 
			if (!SystemProperty.isCounty())
				//if (!UtilityMethods.isCounty())
			{
				cvItmCdsData.addAll(
					ItemCodesCache.getItmCds(
						ItemCodesCache.PROCSNGCD,
						InventoryConstant.INV_PROCSNGCD_RESTRICTED,
						CommonConstant.STR_SPACE_EMPTY,
						true));
			}
			// end defect 9085 
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		for (int i = 0; i < cvItmCdsData.size(); i++)
		{
			lsStr = CommonConstant.STR_SPACE_EMPTY;
			laItemCodesData = (ItemCodesData) cvItmCdsData.elementAt(i);
			//defect 8269
			//lsStr = laItemCodesData.getItmCd();
			//lsStr += " - ";
			//lsStr += laItemCodesData.getItmCdDesc();
			lsStr = laItemCodesData.getItmCdDesc();
			lvSort.addElement(lsStr);
			// end defect 8269
		}
		UtilityMethods.sort(lvSort);
		ComboBoxModel laComboModel = new DefaultComboBoxModel(lvSort);
		getcomboItmCdDesc().setModel(laComboModel);
		getcomboItmCdDesc().setSelectedItem(
			InventoryConstant.DEFAULT_SELECTION);
		// defect 8479
		comboBoxHotKeyFix(getcomboItmCdDesc());
		// end defect 8479
	}

	/**
	 * Used to concatenate the required fields to create the list data
	 * for the combo box.
	 * 
	 * @param  aaAlloctnDbData AllocationDbData
	 */
	private void setcomboListData(AllocationDbData aaAlloctnDbData)
	{
		// Concatenate subcontractor information
		Vector lvSubconData = aaAlloctnDbData.getSubconWrap();
		if (lvSubconData != null)
		{
			for (int i = 0; i < lvSubconData.size(); i++)
			{
				// defect 10161 
				String lsSubcon =
					new String(
						((SubcontractorData) lvSubconData.get(i))
							.getId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((SubcontractorData) lvSubconData.get(i))
								.getName1());
				// end defect 10161 
				cvSubconComboData.addElement(lsSubcon);
			}
		}
		else
		{
			cvSubconComboData.addElement(
				CommonConstant.STR_SPACE_EMPTY);
		}
		// Concatenate workstation information
		Vector lvWsData = aaAlloctnDbData.getWsWrap();
		if (lvWsData != null)
		{
			for (int i = 0; i < lvWsData.size(); i++)
			{
				int liWs =
					((AssignedWorkstationIdsData) lvWsData.get(i))
						.getWsId();
				cvWsComboData.add(String.valueOf(liWs));
			}
		}
		else
		{
			cvWsComboData.addElement(CommonConstant.STR_SPACE_EMPTY);
		}
		// Concatenate dealer information
		Vector lvDlrData = aaAlloctnDbData.getDlrWrap();
		if (lvDlrData != null)
		{
			for (int i = 0; i < lvDlrData.size(); i++)
			{
				// defect 10112 
				String lsDlr =
					new String(
						((DealerData) lvDlrData.get(i)).getId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((DealerData) lvDlrData.get(i)).getName1());
				// end defect 10112 

				cvDlrComboData.addElement(lsDlr);
			}
		}
		else
		{
			cvDlrComboData.addElement(CommonConstant.STR_SPACE_EMPTY);
		}
		// Concatenate employee information
		Vector lvEmpData = aaAlloctnDbData.getSecrtyWrap();
		cvEmpComboData.addElement(InventoryConstant.TXT_ALL_EMPLOYEES);
		if (lvEmpData != null)
		{
			for (int i = 0; i < lvEmpData.size(); i++)
			{
				String lsEmp =
					new String(
						((SecurityData) lvEmpData.get(i)).getEmpId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpLastName()
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpFirstName());
				if (((SecurityData) lvEmpData.get(i)).getEmpMI()
					!= null)
				{
					lsEmp =
						lsEmp
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpMI();
				}
				cvEmpComboData.addElement(lsEmp);
			}
		}
		else
		{
			cvDlrComboData.addElement(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (cbInit)
		{
			if (aaData == null)
			{
				cbInit = false;
				return;
			}
			Vector laDataIn = (Vector) aaData;
			if (laDataIn.get(CommonConstant.ELEMENT_0) != null)
			{
				caIPData =
					(InventoryProfileData) laDataIn.elementAt(
						CommonConstant.ELEMENT_0);
			}
			else
			{
				cbIPNull = true;
				caIPData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				caIPData.setSubstaId(SystemProperty.getSubStationId());
				caIPData.setEntity(InventoryConstant.CHAR_C);
				caIPData.setId(CommonConstant.STR_ZERO);
				caIPData.setItmCd(InventoryConstant.DEFAULT_ABBR);
				RTSDate laDate = new RTSDate();
				caIPData.setInvItmYr(laDate.getYear() + 1);
				getchkPromtForNextItem().setSelected(true);
			}
			caAlloctnDbData =
				(AllocationDbData) laDataIn.elementAt(
					CommonConstant.ELEMENT_1);
			caInvAlloctnUIData =
				(InventoryAllocationUIData) laDataIn.elementAt(
					CommonConstant.ELEMENT_2);
			getradioCentral().setSelected(true);
			getradioCentral().requestFocus();
			setcomboListData(caAlloctnDbData);
			setcomboItmCdDesc();
			enableDisableYear();
			setpnlEntityId();

			if (cbIPNull)
			{
				setProfileStatusOnFrame(null);
			}
			else
			{
				setProfileStatusOnFrame(caIPData);
			}
			cbInit = false;
			return;
		}
		else
		{
			if (aaData != null && !(((Vector) aaData).isEmpty()))
			{
				caIPData =
					(InventoryProfileData) ((Vector) aaData).elementAt(
						CommonConstant.ELEMENT_0);
				cbIPNull = false;
			}
			else
			{
				cbIPNull = true;
			}
		}
	}

	/**
	 * Set the Entity value
	 */
	private void setEntity()
	{
		if (getradioCentral().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_C);
			caIPData.setId(CommonConstant.STR_ZERO);
		}
		else if (getradioWorkstation().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_W);
		}
		else if (getradioSubcontractor().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_S);
		}
		else if (getradioDealer().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_D);
		}
		else if (getradioEmployee().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_E);
		}
	}

	/**
	 * Depending on which radio button is selected, this method sets 
	 * the border of of the Entity Id panel and calls setcomboEntityId().
	 */
	private void setpnlEntityId()
	{
		if (getradioCentral().isSelected())
		{
			getlblEntityId().setEnabled(false);
			getlblEntityId().setVisible(false);
			getcomboEntityId().setEnabled(false);
			getcomboEntityId().setVisible(false);
			setEntity();
			setcomboEntityId();
		}
		else if (getradioWorkstation().isSelected())
		{
			getlblEntityId().setText(
				InventoryConstant.TXT_WORKSTATION_ID_COLON);
			getlblEntityId().setEnabled(true);
			getlblEntityId().setVisible(true);
			getcomboEntityId().setEnabled(true);
			getcomboEntityId().setVisible(true);
			setEntity();
			setcomboEntityId();
		}
		else if (getradioSubcontractor().isSelected())
		{
			getlblEntityId().setText(
				InventoryConstant.TXT_SUBCONTRACTOR_ID_COLON);
			getlblEntityId().setEnabled(true);
			getlblEntityId().setVisible(true);
			getcomboEntityId().setEnabled(true);
			getcomboEntityId().setVisible(true);
			setEntity();
			setcomboEntityId();
		}
		else if (getradioDealer().isSelected())
		{
			getlblEntityId().setText(
				InventoryConstant.TXT_DEALER_ID_COLON);
			getlblEntityId().setEnabled(true);
			getlblEntityId().setVisible(true);
			getcomboEntityId().setEnabled(true);
			getcomboEntityId().setVisible(true);
			setEntity();
			setcomboEntityId();
		}
		else if (getradioEmployee().isSelected())
		{
			getlblEntityId().setText(
				InventoryConstant.TXT_EMPLOYEE_ID_COLON);
			getlblEntityId().setEnabled(true);
			getlblEntityId().setVisible(true);
			getcomboEntityId().setEnabled(true);
			getcomboEntityId().setVisible(true);
			setEntity();
			setcomboEntityId();
		}
	}

	/**
	 * Displays the inventory profile on screen.
	 *  
	 * @param aaIPD InventoryProfileData
	 */
	private void setProfileStatusOnFrame(InventoryProfileData aaIPD)
	{
		if (aaIPD != null)
		{
			gettxtMinQty().setText(Integer.toString(aaIPD.getMinQty()));
			gettxtMaxQty().setText(Integer.toString(aaIPD.getMaxQty()));
			if (aaIPD.getNextAvailIndi() == 1)
			{
				getchkPromtForNextItem().setSelected(true);
			}
			else
			{
				getchkPromtForNextItem().setSelected(false);
			}
			gettxtYear().setText(Integer.toString(aaIPD.getInvItmYr()));
			getbtnAdd().setEnabled(false);
			getbtnRevise().setEnabled(true);
			getbtnDelete().setEnabled(true);
		}
		else
		{
			gettxtMinQty().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtMaxQty().setText(CommonConstant.STR_SPACE_EMPTY);
			getbtnAdd().setEnabled(true);
			getbtnRevise().setEnabled(false);
			getbtnDelete().setEnabled(false);
		}
	}
}
