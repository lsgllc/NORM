package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/* 
 * FrmInventoryItemNumberInputINV001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			05/02/2002	Inventory not allowed to have spaces 
 * 							defect 3755
 * MAbs			06/25/2002	Escaping back and forth will not insert 
 * 							extra items 
 * 							defect 4317
 * B Arredondo	12/18/2002	Made changes for the user help guide 
 *							modify actionPerformed()
 *							defect 5147. 
 * Min Wang 	03/13/2003	Modified actionPerformed(). Defect 
 * 							defect 5753
 * Ray Rowehl	05/13/2003 	When sending the inventory info, make the 
 * 							qty 1.
 *							modify actionPerformed()
 *							defect 6120
 * T Pederson	03/14/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel
 * 							defect 8240 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * T Pederson	07/22/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/06/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		04/06/2007	Added Inventory Item Number range lable to 
 * 							be used for testing.  This will stop us
 * 							from having to lookup the itemcode in the
 * 							spreadsheet.  The will only show up in
 * 							development mode.  If it brings back 
 * 							multiple Patterns then it will pick a random
 * 							one.
 * 							add ivjlblTestInvItemNo, 
 * 								getlblTestInvItemNo()
 * 							modify setData()
 * 							defect 9145 Ver Special Plates
 * J Rue		05/14/2007	Clean up unused variables
 * 							Remove builderData()
 * 							defect 9086 Ver Special Plates
 * R Pilon		06/13/2012  Change to only log the exception when attempting to 
 * 							initialize the BarCodeScanner.
 * 							modify setData(Object)
 * 							defect 11071 Ver 7.0.0
 * R Pilon		06/14/2012  Increase window size to prevent Enter/Cancel/Help
 * 							  button panel from being cutoff.
 * 							modify initialize()
 * 							defect 11378 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
 
/**
 * INV001 allows the user to type in a new Inventory Item Number
 *
 * @version	POS_700				06/14/2012
 * @author	Michael Abernethy
 * <br>Creation Date:			06/26/2001 14:51:07
 */

public class FrmInventoryItemNumberInputINV001
	extends RTSDialogBox
	implements BarCodeListener, ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjlblEnterInventoryItemNumber = null;
	private JPanel ivjFRMItemNumberInputINV001ContentPane1 = null;
	private JLabel ivjlblInvItem = null;
	private RTSInputField ivjtxtEnterInventoryItem = null;
	
	// Object
	private BarCodeScanner caBarcode; 
	private CompleteTransactionData caCompleteTransactionData;
	private ProcessInventoryData caProcessInventoryData;
	
	// String 
	private String csTrackingType;

	// Constants 
	private final static String PLT_TRCK_TYPE = "P";
	private final static String STKR_TRCK_TYPE = "S";
	private static final int MAX_PLATE_NO = 7;
	
	// Text Constants 
	private final static String FRM_NAME_INV001 = 
		"FRMItemNumberInputINV001";
	private final static String FRM_TITLE_INV001 = 
		"Inventory - Item Number Input        INV001";
	private final static String TXT_YOUVE_SCND = "YOU'VE SCANNED A ";
	private final static String TXT_ENTER_INV = 
		"Enter Inventory Item Number for";
	private final static String STR_DTA = "DTA";
	
	private javax.swing.JLabel ivjlblTestInvItemNo = null;
	/**
	 * FRMItemNumberInputINV001 constructor
	 */
	public FrmInventoryItemNumberInputINV001()
	{
		super();
		initialize();
	}
	
	/**
	 * FRMItemNumberInputINV001 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryItemNumberInputINV001(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * FRMItemNumberInputINV001 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryItemNumberInputINV001(JFrame aaParent)
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

		if (!startWorking() || !isVisible())
			return;

		try
		{
			clearAllColor(this);
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leEx = new RTSException();
				// VALIDATION
				String lsItemNum =
					gettxtEnterInventoryItem()
						.getText()
						.toUpperCase()
						.trim();
				if (lsItemNum.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					leEx.addException(
						new RTSException(150),
						gettxtEnterInventoryItem());
				}
				if (ValidateInventoryPattern
					.chkIfItmPLPOrOLDPLTOrROP(
						caProcessInventoryData.getItmCd()))
				{
					if (lsItemNum.length() > MAX_PLATE_NO)
					{
						leEx.addException(
							new RTSException(150),
							gettxtEnterInventoryItem());
					}
				}
				if (leEx.isValidationError())
				{
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}
				// END VALIDATION

				// If it's a sticker, strip the leading 0's
				if (csTrackingType.equals(STKR_TRCK_TYPE))
				{
					while (lsItemNum.substring(0, 1).equals
								(CommonConstant.STR_ZERO))
						lsItemNum =
							lsItemNum.substring(1, lsItemNum.length());
				}

				gettxtEnterInventoryItem().requestFocus();
				Map lhmMap = new HashMap();
				caProcessInventoryData.setInvItmNo(lsItemNum);
				caProcessInventoryData.setInvItmEndNo(lsItemNum);
				// defect 6120
				// make the qty 1
				caProcessInventoryData.setInvQty(1);
				// end defect 6120
				lhmMap.put("INV_DATA", caProcessInventoryData);
				lhmMap.put("DATA", caCompleteTransactionData);
				getController().processData(
					AbstractViewController.ENTER,
					UtilityMethods.copy(lhmMap));
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd =
					caCompleteTransactionData.getTransCode();

				if (lsTransCd.equals(TransCdConstant.RGNCOL))
				{
					RTSHelp.displayHelp(RTSHelp.INV001);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.INV001A);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Invoked when a barcode is scanned
	 * 
	 * @param aaBCE BarCodeEvent
	 */
	public void barCodeScanned(BarCodeEvent aaBCE)
	{
		if (aaBCE.getBarCodeData() instanceof PlateBarCodeData)
		{
			if (csTrackingType.equals(InventoryConstant.CHAR_S))
			{
				RTSException leEx = new RTSException(712);
				leEx.displayError(this);
				return;
			}
			PlateBarCodeData laPlateData =
				(PlateBarCodeData) aaBCE.getBarCodeData();
			if (Integer.parseInt(laPlateData.getItemYr())
				!= caProcessInventoryData.getInvItmYr()
				|| !laPlateData.getItemCd().trim().equals(
					caProcessInventoryData.getItmCd()))
			{
				ItemCodesData laItemData =
					ItemCodesCache.getItmCd(
						laPlateData.getItemCd().trim());
				String lsItmCdDesc = laItemData.getItmCdDesc();
				String[] lsMsg = new String[1];
				if (Integer.parseInt(laPlateData.getItemYr()) == 0)
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laPlateData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ lsItmCdDesc;
				}
				else
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laPlateData.getItemYr()
							+ CommonConstant.STR_SPACE_ONE
							+ laPlateData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ lsItmCdDesc;
				}
				RTSException leEx = new RTSException(715, lsMsg);
				leEx.displayError(this);
				return;
			}
			gettxtEnterInventoryItem().setText(laPlateData.getItemNo());
			caProcessInventoryData.setIsBarCodeScanned(true);
		}
		else if (aaBCE.getBarCodeData() instanceof StickerBarCodeData)
		{
			if (csTrackingType.equals(PLT_TRCK_TYPE))
			{
				RTSException leEx = new RTSException(713);
				leEx.displayError(this);
				return;
			}
			StickerBarCodeData laStickerData =
				(StickerBarCodeData) aaBCE.getBarCodeData();
			if (Integer.parseInt(laStickerData.getItemYr())
				!= caProcessInventoryData.getInvItmYr()
				|| !laStickerData.getItemCd().trim().equals(
					caProcessInventoryData.getItmCd()))
			{
				ItemCodesData laItemData =
					ItemCodesCache.getItmCd(
						laStickerData.getItemCd().trim());
				String lsItmCdDesc = laItemData.getItmCdDesc();
				String[] lsMsg = new String[1];
				if (Integer.parseInt(laStickerData.getItemYr()) == 0)
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laStickerData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ lsItmCdDesc;
				}
				else
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laStickerData.getItemYr()
							+ CommonConstant.STR_SPACE_ONE
							+ laStickerData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ lsItmCdDesc;
				}
				RTSException leEx = new RTSException(715, lsMsg);
				leEx.displayError(this);
				return;
			}
			gettxtEnterInventoryItem().setText(
				laStickerData.getItemNo());
			caProcessInventoryData.setIsBarCodeScanned(true);
		}
		else if (aaBCE.getBarCodeData() instanceof RenewalBarCodeData)
		{
			RTSException leEx = new RTSException(714);
			leEx.displayError(this);
			return;
		}
	}
	

	/**
	 * Return the ButtonPanel1 property value
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
				ivjbuttonPanel.setAsDefault(this);
				ivjbuttonPanel.addActionListener(this);
				// defect 8240
				// remove keyListeners on ButtonPanel
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
		return ivjbuttonPanel;
	}
	
	/**
	 * Returns the CompleteTransactionData object.
	 * 
	 * @return Object
	 */
	public Object getCompleteTransactionData()
	{
		return caCompleteTransactionData;
	}
	
	/**
	 * Return the FRMItemNumberInputINV001ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFRMItemNumberInputINV001ContentPane1()
	{
		if (ivjFRMItemNumberInputINV001ContentPane1 == null)
		{
			try
			{
				ivjFRMItemNumberInputINV001ContentPane1 = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints25 = new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
				consGridBagConstraints21.insets = new java.awt.Insets(1,130,9,130);
				consGridBagConstraints21.ipady = -1;
				consGridBagConstraints21.ipadx = 178;
				consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints21.weightx = 1.0;
				consGridBagConstraints21.gridwidth = 2;
				consGridBagConstraints21.gridy = 2;
				consGridBagConstraints21.gridx = 0;
				consGridBagConstraints25.insets = new java.awt.Insets(2,130,1,130);
				consGridBagConstraints25.ipady = 16;
				consGridBagConstraints25.ipadx = 182;
				consGridBagConstraints25.gridwidth = 2;
				consGridBagConstraints25.gridy = 1;
				consGridBagConstraints25.gridx = 0;
				consGridBagConstraints23.insets = new java.awt.Insets(26,2,2,17);
				consGridBagConstraints23.ipadx = 9;
				consGridBagConstraints23.gridy = 0;
				consGridBagConstraints23.gridx = 1;
				consGridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
				consGridBagConstraints22.insets = new java.awt.Insets(9,65,12,67);
				consGridBagConstraints22.ipady = -1;
				consGridBagConstraints22.ipadx = 94;
				consGridBagConstraints22.fill = java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints22.weighty = 1.0;
				consGridBagConstraints22.weightx = 1.0;
				consGridBagConstraints22.gridwidth = 2;
				consGridBagConstraints22.gridy = 3;
				consGridBagConstraints22.gridx = 0;
				consGridBagConstraints24.insets = new java.awt.Insets(26,10,2,1);
				consGridBagConstraints24.ipadx = 8;
				consGridBagConstraints24.gridy = 0;
				consGridBagConstraints24.gridx = 0;
				ivjFRMItemNumberInputINV001ContentPane1.setName(
					"FRMItemNumberInputINV001ContentPane1");
				ivjFRMItemNumberInputINV001ContentPane1.setLayout(new java.awt.GridBagLayout());
				ivjFRMItemNumberInputINV001ContentPane1.add(gettxtEnterInventoryItem(), consGridBagConstraints21);
				ivjFRMItemNumberInputINV001ContentPane1.add(getbuttonPanel(), consGridBagConstraints22);
				ivjFRMItemNumberInputINV001ContentPane1.add(getlblInvItem(), consGridBagConstraints23);
				ivjFRMItemNumberInputINV001ContentPane1.add(getlblEnterInventoryItemNumber(), consGridBagConstraints24);
				ivjFRMItemNumberInputINV001ContentPane1.add(getlblTestInvItemNo(), consGridBagConstraints25);
				ivjFRMItemNumberInputINV001ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFRMItemNumberInputINV001ContentPane1.setMinimumSize(
					new Dimension(500, 200));

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFRMItemNumberInputINV001ContentPane1;
	}
	
	/**
	 * Return the lblEnterInventoryItemNumber property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEnterInventoryItemNumber()
	{
		if (ivjlblEnterInventoryItemNumber == null)
		{
			try
			{
				ivjlblEnterInventoryItemNumber = new JLabel();
				ivjlblEnterInventoryItemNumber.setName(
					"lblEnterInventoryItemNumber");
				ivjlblEnterInventoryItemNumber.setText(
					TXT_ENTER_INV);
				ivjlblEnterInventoryItemNumber.setHorizontalAlignment(
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
		return ivjlblEnterInventoryItemNumber;
	}
	
	/**
	 * Return the lblInvItem property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvItem()
	{
		if (ivjlblInvItem == null)
		{
			try
			{
				ivjlblInvItem = new JLabel();
				ivjlblInvItem.setName("lblInvItem");
				ivjlblInvItem.setText(
					"2002 DISABLED WINDSHIELD STICKER");
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
		return ivjlblInvItem;
	}
	
	/**
	 * This method initializes ivjlblTestInvItemNo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblTestInvItemNo()
	{
		if (ivjlblTestInvItemNo == null)
		{
			ivjlblTestInvItemNo = new javax.swing.JLabel();
			ivjlblTestInvItemNo.setText(CommonConstant.STR_SPACE_EMPTY);
			// This label is just for testing in development mode
			if (SystemProperty.getProdStatus()
				!= SystemProperty.APP_PROD_STATUS)
			{
				// Adjust the size a bit so that it all fits
				
				setSize(450, (int)getSize().getHeight() + 10);
				ivjlblTestInvItemNo.setVisible(true);
			}
			else
			{
				ivjlblTestInvItemNo.setVisible(false);
			}
			// end defect 9145
		}
		return ivjlblTestInvItemNo;
	}
	
	/**
	 * Return the txtEnterInventoryItem property value.
	 * 
	 * @return JTextField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEnterInventoryItem()
	{
		if (ivjtxtEnterInventoryItem == null)
		{
			try
			{
				ivjtxtEnterInventoryItem = new RTSInputField();
				ivjtxtEnterInventoryItem.setName(
					"txtEnterInventoryItem");
				ivjtxtEnterInventoryItem.setInput(6);
				ivjtxtEnterInventoryItem.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEnterInventoryItem.setMaxLength(10);
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
		return ivjtxtEnterInventoryItem;
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
			setName(FRM_NAME_INV001);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			// defect 11378
			setSize(450, 190);
			// end defect 11378
			setTitle(FRM_TITLE_INV001);
			setContentPane(getFRMItemNumberInputINV001ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * main entrypoint - starts the part when it is run as 
	 * an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInventoryItemNumberInputINV001 
				laFrmInventoryItemNumberInputINV001;
			laFrmInventoryItemNumberInputINV001 =
				new FrmInventoryItemNumberInputINV001();
			laFrmInventoryItemNumberInputINV001.setModal(true);
			laFrmInventoryItemNumberInputINV001
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryItemNumberInputINV001.show();
			Insets laInsets =
				laFrmInventoryItemNumberInputINV001.getInsets();
			laFrmInventoryItemNumberInputINV001.setSize(
				laFrmInventoryItemNumberInputINV001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmInventoryItemNumberInputINV001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmInventoryItemNumberInputINV001.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
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
		Map lmMap = (Map) aaDataObject;
		caCompleteTransactionData =
			(CompleteTransactionData) lmMap.get("DATA");
		caProcessInventoryData =
			(ProcessInventoryData) ((Vector) lmMap.get("ALLOC")).get(0);

		// remove the object since it will be allocated
		 ((Vector) lmMap.get("ALLOC")).remove(0);

		// Set the data on the screen
		ItemCodesData laItemData =
			ItemCodesCache.getItmCd(caProcessInventoryData.getItmCd());
		if (caProcessInventoryData.getInvItmYr() == 0)
		{
			getlblInvItem().setText(laItemData.getItmCdDesc() + 
				CommonConstant.STR_COLON);
		}
		else
		{
			getlblInvItem().setText(
				caProcessInventoryData.getInvItmYr()
					+ CommonConstant.STR_SPACE_ONE
					+ laItemData.getItmCdDesc()
					+ CommonConstant.STR_COLON);
		}

		csTrackingType = laItemData.getItmTrckngType();
		try
		{
			caBarcode =
				getController()
					.getMediator()
					.getAppController()
					.getBarCodeScanner();
			caBarcode.addBarCodeListener(this);
		}
		catch (RTSException aeEx)
		{
			// defect 11071
			Log.write(Log.DEBUG, this, aeEx.getDetailMsg());
			// end defect 11071
		}

		// Handle this screen special if it's a DTA event
		if (caCompleteTransactionData.getTransCode().startsWith(STR_DTA))
		{
			Map lhmDataMap = new HashMap();
			lhmDataMap.put("DATA", caCompleteTransactionData);
			lhmDataMap.put("INV_DATA", caProcessInventoryData);
			getController().processData(
				VCInventoryItemNumberInputINV001.ENTER,
				lhmDataMap);
		}
		
		// defect 9145
		// This label is just for testing in development mode
		if (SystemProperty.getProdStatus()
			!= SystemProperty.APP_PROD_STATUS
			&& caProcessInventoryData != null
			&& caProcessInventoryData.getItmCd() != null)
		{
			Vector lvInvPatCache =
				InventoryPatternsCache.getInvPatrns(
					caProcessInventoryData.getItmCd(),
					caProcessInventoryData.getInvItmYr());
			if (lvInvPatCache != null && lvInvPatCache.size() > 0)
			{
				int liRandomNum =
					new Random().nextInt(lvInvPatCache.size());
				InventoryPatternsData laInvPat =
					(InventoryPatternsData) lvInvPatCache.get(
						liRandomNum);
				String lsTestInvItmNo = laInvPat.getInvItmNo();
				String lsTestInvItmEndNo = laInvPat.getInvItmEndNo();
				getlblTestInvItemNo().setText(
					lsTestInvItmNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_DASH
						+ CommonConstant.STR_SPACE_ONE
						+ lsTestInvItmEndNo);
			}
		}
		// end defect 9145
	}
}
