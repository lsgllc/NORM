package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmItemNumberNotFoundINV029.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs		    04/18/2002	Global change for startWorking() and 
 *                          doneWorking()
 * Min Wang	    09/06/2002	change INV029 to not show message 
 * 							when inventory is in stock.
 * 							defect 4713 
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 *							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the user help guide  
 *							modify actionPerformed()
 *							defect 5147. 
 * Min Wang		03/13/2003	Modified actionPerformed() in 
 *							FrmItemNumberNotFoundINV029. 
 *							defect 623
 * Ray Rowehl	03/14/2003	defect 5490
 * Min Wang		06/21/2004	Inv item issued from Stock should show
 *							on biar report (part A) and 
 *							should show 'U-ITEM NUMBER NOT FOUND' on 
 *							inventory detail report. 
 *							modify actionPerformed()
 *							defect 6871 Ver 5.2.1  
 * T Pederson	03/16/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	05/27/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.3
 * S Johnston	06/17/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel()
 * 							defect 8240 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * T Pederson	07/28/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/02/2005	Remove item code from screen.
 *							Set Item Code label visible(false).
 *							Also comment out set in setData.
 *							modify getBuilderData(), getlblItemCode(),
 *								setData()
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/25/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/16/2009	Class Cleanup. Implement UtilityMethods.isDTA()
 * 							modify actionPerformed() 
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * INV029 confirms that the Inventory Number was not found in the DB
 *
 * @version	Defect_POS_H	12/16/2009
 * @author	Michael Abernethy
 * <br>Creation Date:		04/18/2002 10:00:00
 */

public class FrmItemNumberNotFoundINV029
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel = null;
	private JCheckBox ivjchkVoided = null;
	private JLabel ivjstcLblSentence1 = null;
	private JLabel ivjlblItemCode = null;
	private JLabel ivjlblItemDescription = null;
	private JLabel ivjlblItemNumber = null;
	private JPanel ivjFrmItemNumberNotFoundINV029ContentPane1 = null;
	private JLabel ivjlblEntityId = null;
	private JLabel ivjlblEntityTxt = null;
	private JLabel ivjlblYear = null;
	private JLabel ivjstcLblAlreadyAllocted = null;
	private JPanel ivjpanelEntityInfo = null;
	private JPanel ivjpanelItemInfo = null;

	// boolean 
	private boolean cbNotInDB;
	
	// Object
	private CompleteTransactionData caCompleteTransactionData; 
	private ProcessInventoryData caProcessInventoryData;
	private ProcessInventoryData caOldData;

	// Text Constants 
	private final static String FRM_NAME_INV029 = 
		"FrmItemNumberNotFoundINV029";
	private final static String FRM_TITLE_INV029 = 
		"Inventory - Item Number Not Found        INV029";
	private final static String TXT_REUSED_VOID = 
		"Re-used Voided Inventory Item";
	private final static String TXT_ITM_ALLOC = 
		"The above item already allocated to:";
	private final static String TXT_INV_NOT_FOUND = 
		"The Inventory Item Number cannot be found.  " + 
		"Confirm the Item Number:";
	private final static String TXT_DEALER = "Dealer";
	private final static String TXT_WORKSTATION = "Workstation";
	private final static String TXT_EMPLOYEE = "Employee";
	private final static String TXT_CENTRAL = "Central";
	private final static String TXT_SUBCON = "Subcontractor";
	private final static String TXT_UNKNOWN = "Unknown";
	
	/**
	 * FrmItemNumberNotFoundINV029 constructor
	 */
	public FrmItemNumberNotFoundINV029()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmItemNumberNotFoundINV029 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmItemNumberNotFoundINV029(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * FrmItemNumberNotFoundINV029 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmItemNumberNotFoundINV029(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs
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
			
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				if (getchkVoided().isSelected())
				{
					caProcessInventoryData.setInvLocIdCd(
						InventoryConstant.VOID);
					caProcessInventoryData.setInvStatusCd(
						CommonConstant.NOT_EXISTED_ITEM_REUSED);
					caProcessInventoryData.setInvId(
						CommonConstant.STR_ZERO);
				}
				else
				{
					//defect 6871
					if (cbNotInDB
						|| caProcessInventoryData
							.getInvLocIdCd().equalsIgnoreCase(
								InventoryConstant.CHAR_STOCK))
					{
						//end defect 6871
						caProcessInventoryData.setInvId(
							CommonConstant.STR_ZERO);
						caProcessInventoryData.setInvLocIdCd(
							InventoryConstant.NOT_FOUND);
						caProcessInventoryData.setInvStatusCd(
							CommonConstant.NOT_EXISTED_ITEM);
					}
					else
					{
						caProcessInventoryData.setInvStatusCd(
							CommonConstant.MISMATCHED_ITEM);
					}
				}
				Map laMap = new HashMap();
				laMap.put("DATA", caCompleteTransactionData);
				laMap.put("INV_DATA", caProcessInventoryData);
				laMap.put("OLD_DATA", caOldData);
				getController().processData(
					AbstractViewController.ENTER,
					laMap);
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				String lsTransCd =
					caCompleteTransactionData.getTransCode();

				// defect 8177
				// INV029 not used for Subcon, use INV003
				//if (lsTransCd.equals(TransCdConstant.SBRNW))
				//{
				//	RTSHelp.displayHelp(RTSHelp.INV029A);
				//}
				//else if (
				// end defect 8177
				if (UtilityMethods.isDTA(lsTransCd))
				{
					RTSHelp.displayHelp(RTSHelp.INV029F);
				}
				else if (lsTransCd.equals(TransCdConstant.PT72))
				{
					RTSHelp.displayHelp(RTSHelp.INV029G);
				}
				else if (lsTransCd.equals(TransCdConstant.PT144))
				{
					RTSHelp.displayHelp(RTSHelp.INV029H);
				}
				else if (lsTransCd.equals(TransCdConstant.OTPT))
				{
					RTSHelp.displayHelp(RTSHelp.INV029I);
				}
				else if (lsTransCd.equals(TransCdConstant.PT30))
				{
					RTSHelp.displayHelp(RTSHelp.INV029J);
				}
				else if (
					lsTransCd.equals(TransCdConstant.NRIPT)
						|| lsTransCd.equals(TransCdConstant.NROPT))
				{
					RTSHelp.displayHelp(RTSHelp.INV029K);
				}
				else if (lsTransCd.equals(TransCdConstant.TOWP))
				{
					RTSHelp.displayHelp(RTSHelp.INV029L);
				}
				else if (lsTransCd.equals(TransCdConstant.FDPT))
				{
					RTSHelp.displayHelp(RTSHelp.INV029Q);
				}
				else if (UtilityMethods.isMFUP())
				{
					if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.INV029D);
					}
					else if (lsTransCd.equals(TransCdConstant.TITLE))
					{
						RTSHelp.displayHelp(RTSHelp.INV029E);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.INV029B);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.INV029C);
					}
				}
				else
				{
					if (lsTransCd.equals(TransCdConstant.TITLE))
					{
						RTSHelp.displayHelp(RTSHelp.INV029M);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.INV029N);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.INV029O);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.INV029P);
					}
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Return the ivjButtonPanel property value
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (ivjButtonPanel == null)
		{
			try
			{
				ivjButtonPanel = new ButtonPanel();
				ivjButtonPanel.setName("ivjButtonPanel");
				ivjButtonPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel.addActionListener(this);
				ivjButtonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel;
	}
	
	/**
	 * Return the ivjchkVoided property value
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkVoided()
	{
		if (ivjchkVoided == null)
		{
			try
			{
				ivjchkVoided = new JCheckBox();
				ivjchkVoided.setName("ivjchkVoided");
				ivjchkVoided.setMnemonic(86);
				ivjchkVoided.setText(TXT_REUSED_VOID);
				ivjchkVoided.setMaximumSize(new Dimension(197, 22));
				ivjchkVoided.setActionCommand(TXT_REUSED_VOID);
				ivjchkVoided.setMinimumSize(new Dimension(197, 22));
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
		return ivjchkVoided;
	}
	
	/**
	 * Returns the CompleteTransactionData object
	 * 
	 * @return Object
	 */
	public Object getCompleteTransactionData()
	{
		return caProcessInventoryData;
	}
	
	/**
	 * Return the ivjFrmItemNumberNotFoundINV029ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmItemNumberNotFoundINV029ContentPane1()
	{
		if (ivjFrmItemNumberNotFoundINV029ContentPane1 == null)
		{
			try
			{
				ivjFrmItemNumberNotFoundINV029ContentPane1 =
					new JPanel();
				ivjFrmItemNumberNotFoundINV029ContentPane1.setName(
					"ivjFrmItemNumberNotFoundINV029ContentPane1");
				ivjFrmItemNumberNotFoundINV029ContentPane1.setLayout(
					new GridBagLayout());
				ivjFrmItemNumberNotFoundINV029ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmItemNumberNotFoundINV029ContentPane1
					.setMinimumSize(
					new Dimension(525, 236));

				GridBagConstraints constraintsstcLblSentence1 =
					new GridBagConstraints();
				constraintsstcLblSentence1.gridx = 1;
				constraintsstcLblSentence1.gridy = 1;
				constraintsstcLblSentence1.ipadx = 24;
				constraintsstcLblSentence1.insets =
					new Insets(19, 36, 2, 37);
				getFrmItemNumberNotFoundINV029ContentPane1().add(
					getstcLblSentence1(),
					constraintsstcLblSentence1);

				GridBagConstraints constraintschkVoided =
					new GridBagConstraints();
				constraintschkVoided.gridx = 1;
				constraintschkVoided.gridy = 4;
				constraintschkVoided.ipadx = 36;
				constraintschkVoided.insets =
					new Insets(2, 133, 6, 134);
				getFrmItemNumberNotFoundINV029ContentPane1().add(
					getchkVoided(),
					constraintschkVoided);

				GridBagConstraints constraintsbuttonPanel =
					new GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 5;
				constraintsbuttonPanel.fill =
					GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 87;
				constraintsbuttonPanel.ipady = 34;
				constraintsbuttonPanel.insets =
					new Insets(6, 98, 32, 98);
				getFrmItemNumberNotFoundINV029ContentPane1().add(
					getButtonPanel(),
					constraintsbuttonPanel);

				GridBagConstraints constraintspanelItemInfo =
					new GridBagConstraints();
				constraintspanelItemInfo.gridx = 1;
				constraintspanelItemInfo.gridy = 2;
				constraintspanelItemInfo.fill =
					GridBagConstraints.BOTH;
				constraintspanelItemInfo.weightx = 1.0;
				constraintspanelItemInfo.weighty = 1.0;
				constraintspanelItemInfo.insets =
					new Insets(2, 7, 5, 6);
				getFrmItemNumberNotFoundINV029ContentPane1().add(
					getpanelItemInfo(),
					constraintspanelItemInfo);

				GridBagConstraints constraintspanelEntityInfo =
					new GridBagConstraints();
				constraintspanelEntityInfo.gridx = 1;
				constraintspanelEntityInfo.gridy = 3;
				constraintspanelEntityInfo.fill =
					GridBagConstraints.BOTH;
				constraintspanelEntityInfo.weightx = 1.0;
				constraintspanelEntityInfo.weighty = 1.0;
				constraintspanelEntityInfo.insets =
					new Insets(6, 7, 2, 6);
				getFrmItemNumberNotFoundINV029ContentPane1().add(
					getpanelEntityInfo(),
					constraintspanelEntityInfo);
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
		return ivjFrmItemNumberNotFoundINV029ContentPane1;
	}
	
	/**
	 * Return the ivjlblEntityId property value
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
				ivjlblEntityId.setText("EntityId");
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
	 * Return the ivjlblEntityTxt property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEntityTxt()
	{
		if (ivjlblEntityTxt == null)
		{
			try
			{
				ivjlblEntityTxt = new JLabel();
				ivjlblEntityTxt.setName("ivjlblEntityTxt");
				ivjlblEntityTxt.setText("EntityTxt");
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
		return ivjlblEntityTxt;
	}
	
	/**
	 * Return the ivjlblItemCode property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblItemCode()
	{
		if (ivjlblItemCode == null)
		{
			try
			{
				ivjlblItemCode = new JLabel();
				ivjlblItemCode.setName("ivjlblItemCode");
				ivjlblItemCode.setText("Item Code");
				ivjlblItemCode.setMaximumSize(
					new Dimension(57, 14));
				ivjlblItemCode.setVisible(false);
				ivjlblItemCode.setMinimumSize(
					new Dimension(57, 14));
				ivjlblItemCode.setHorizontalAlignment(
					SwingConstants.CENTER);
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
		return ivjlblItemCode;
	}
	
	/**
	 * Return the ivjlblItemDescription property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblItemDescription()
	{
		if (ivjlblItemDescription == null)
		{
			try
			{
				ivjlblItemDescription = new JLabel();
				ivjlblItemDescription.setName("ivjlblItemDescription");
				ivjlblItemDescription.setText("Item Description");
				ivjlblItemDescription.setMaximumSize(
					new Dimension(93, 14));
				ivjlblItemDescription.setMinimumSize(
					new Dimension(93, 14));
				ivjlblItemDescription.setHorizontalAlignment(
					SwingConstants.CENTER);
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
		return ivjlblItemDescription;
	}
	
	/**
	 * Return the ivjlblItemNumber property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblItemNumber()
	{
		if (ivjlblItemNumber == null)
		{
			try
			{
				ivjlblItemNumber = new JLabel();
				ivjlblItemNumber.setName("ivjlblItemNumber");
				ivjlblItemNumber.setText("Item #");
				ivjlblItemNumber.setMaximumSize(
					new Dimension(35, 14));
				ivjlblItemNumber.setMinimumSize(
					new Dimension(35, 14));
				ivjlblItemNumber.setHorizontalAlignment(
					SwingConstants.CENTER);
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
		return ivjlblItemNumber;
	}
	
	/**
	 * Return the ivjlblYear property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblYear()
	{
		if (ivjlblYear == null)
		{
			try
			{
				ivjlblYear = new JLabel();
				ivjlblYear.setName("ivjlblYear");
				ivjlblYear.setText("Item Yr");
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
		return ivjlblYear;
	}
	
	/**
	 * Return the ivjpanelEntityInfo property value
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelEntityInfo()
	{
		if (ivjpanelEntityInfo == null)
		{
			try
			{
				ivjpanelEntityInfo = new JPanel();
				ivjpanelEntityInfo.setName("ivjpanelEntityInfo");
				ivjpanelEntityInfo.setLayout(
					new GridBagLayout());

				GridBagConstraints constraintsstcLblAlreadyAllocted =
					new GridBagConstraints();
				constraintsstcLblAlreadyAllocted.gridx = 1;
				constraintsstcLblAlreadyAllocted.gridy = 1;
				constraintsstcLblAlreadyAllocted.ipadx = 21;
				constraintsstcLblAlreadyAllocted.insets =
					new Insets(7, 33, 8, 16);
				getpanelEntityInfo().add(
					getstcLblAlreadyAllocted(),
					constraintsstcLblAlreadyAllocted);

				GridBagConstraints constraintslblEntityTxt =
					new GridBagConstraints();
				constraintslblEntityTxt.gridx = 2;
				constraintslblEntityTxt.gridy = 1;
				constraintslblEntityTxt.ipadx = 29;
				constraintslblEntityTxt.insets =
					new Insets(7, 17, 8, 16);
				getpanelEntityInfo().add(
					getlblEntityTxt(),
					constraintslblEntityTxt);

				GridBagConstraints constraintslblEntityId =
					new GridBagConstraints();
				constraintslblEntityId.gridx = 3;
				constraintslblEntityId.gridy = 1;
				constraintslblEntityId.ipadx = 9;
				constraintslblEntityId.insets =
					new Insets(7, 17, 8, 36);
				getpanelEntityInfo().add(
					getlblEntityId(),
					constraintslblEntityId);
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
		return ivjpanelEntityInfo;
	}
	
	/**
	 * Return the ivjpanelItemInfo property value
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelItemInfo()
	{
		if (ivjpanelItemInfo == null)
		{
			try
			{
				ivjpanelItemInfo = new JPanel();
				ivjpanelItemInfo.setName("ivjpanelItemInfo");
				ivjpanelItemInfo.setLayout(
					new GridBagLayout());

				GridBagConstraints constraintslblItemCode =
					new GridBagConstraints();
				constraintslblItemCode.gridx = 1;
				constraintslblItemCode.gridy = 1;
				constraintslblItemCode.ipadx = 18;
				constraintslblItemCode.insets =
					new Insets(10, 6, 10, 3);
				getpanelItemInfo().add(
					getlblItemCode(),
					constraintslblItemCode);

				GridBagConstraints constraintslblItemDescription =
					new GridBagConstraints();
				constraintslblItemDescription.gridx = 2;
				constraintslblItemDescription.gridy = 1;
				constraintslblItemDescription.ipadx = 161;
				constraintslblItemDescription.insets =
					new Insets(10, 3, 10, 3);
				getpanelItemInfo().add(
					getlblItemDescription(),
					constraintslblItemDescription);

				GridBagConstraints constraintslblYear =
					new GridBagConstraints();
				constraintslblYear.gridx = 3;
				constraintslblYear.gridy = 1;
				constraintslblYear.ipadx = 10;
				constraintslblYear.insets =
					new Insets(10, 3, 10, 3);
				getpanelItemInfo().add(
					getlblYear(),
					constraintslblYear);

				GridBagConstraints constraintslblItemNumber =
					new GridBagConstraints();
				constraintslblItemNumber.gridx = 4;
				constraintslblItemNumber.gridy = 1;
				constraintslblItemNumber.ipadx = 43;
				constraintslblItemNumber.insets =
					new Insets(10, 3, 10, 6);
				getpanelItemInfo().add(
					getlblItemNumber(),
					constraintslblItemNumber);
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
		return ivjpanelItemInfo;
	}
	
	/**
	 * Return the ivjstcLblAlreadyAllocted property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAlreadyAllocted()
	{
		if (ivjstcLblAlreadyAllocted == null)
		{
			try
			{
				ivjstcLblAlreadyAllocted = new JLabel();
				ivjstcLblAlreadyAllocted.setName(
					"ivjstcLblAlreadyAllocted");
				ivjstcLblAlreadyAllocted.setText(TXT_ITM_ALLOC);
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
		return ivjstcLblAlreadyAllocted;
	}
	
	/**
	 * Return the ivjstcLblSentence1 property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSentence1()
	{
		if (ivjstcLblSentence1 == null)
		{
			try
			{
				ivjstcLblSentence1 = new JLabel();
				ivjstcLblSentence1.setName("ivjstcLblSentence1");
				ivjstcLblSentence1.setText(TXT_INV_NOT_FOUND);
				ivjstcLblSentence1.setMaximumSize(
					new Dimension(403, 14));
				ivjstcLblSentence1.setMinimumSize(
					new Dimension(403, 14));
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
		return ivjstcLblSentence1;
	}
	
	/**
	 * Called whenever the part throws an exception
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
			setName(FRM_NAME_INV029);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(500, 250);
			setTitle(FRM_TITLE_INV029);
			setContentPane(
				getFrmItemNumberNotFoundINV029ContentPane1());
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
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmItemNumberNotFoundINV029 laFrmItemNumberNotFoundINV029;
			laFrmItemNumberNotFoundINV029 =
				new FrmItemNumberNotFoundINV029();
			laFrmItemNumberNotFoundINV029.setModal(true);
			laFrmItemNumberNotFoundINV029
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(
					WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmItemNumberNotFoundINV029.show();
			Insets laInsets =
				laFrmItemNumberNotFoundINV029.getInsets();
			laFrmItemNumberNotFoundINV029.setSize(
				laFrmItemNumberNotFoundINV029.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmItemNumberNotFoundINV029.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmItemNumberNotFoundINV029.setVisibleRTS(true);
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
		Map laMap = (Map) aaDataObject;
		caCompleteTransactionData =
			(CompleteTransactionData) laMap.get("DATA");
		caProcessInventoryData =
			(ProcessInventoryData) laMap.get("INV_DATA");
		caOldData = (ProcessInventoryData) laMap.get("OLD_DATA");

		ItemCodesData laItemCodesData =
			ItemCodesCache.getItmCd(caProcessInventoryData.getItmCd());
		getlblItemDescription().setText(laItemCodesData.getItmCdDesc());

		String lsCode = CommonConstant.STR_SPACE_EMPTY;
		
		// Determine if the item was already allocated or was not in 
		// the DB
		if (caProcessInventoryData.getInvItmYr() != 0)
		{
			getlblYear().setText(
				Integer.toString(caProcessInventoryData.getInvItmYr()));
		}
		else
		{
			getlblYear().setText(CommonConstant.STR_SPACE_EMPTY);
		}
		getlblItemNumber().setText(
			caProcessInventoryData.getInvItmNo());

		cbNotInDB = (caProcessInventoryData.getInvId() == null);
		if (caProcessInventoryData.isPreviouslyVoidedItem())
		{
			getchkVoided().setSelected(true);
			cbNotInDB = true;
		}

		if (cbNotInDB)
		{
			getlblEntityId().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblEntityTxt().setText(CommonConstant.STR_SPACE_EMPTY);
			getstcLblAlreadyAllocted().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			getlblEntityId().setText(caProcessInventoryData.getInvId());
			lsCode = caProcessInventoryData.getInvLocIdCd();
			if (lsCode.equals(InventoryConstant.CHAR_D))
			{
				getlblEntityTxt().setText(TXT_DEALER);
			}
			else if (lsCode.equals(InventoryConstant.CHAR_W))
			{
				getlblEntityTxt().setText(TXT_WORKSTATION);
			}
			else if (lsCode.equals(InventoryConstant.CHAR_E))
			{
				getlblEntityTxt().setText(TXT_EMPLOYEE);
			}
			else if (lsCode.equals(InventoryConstant.CHAR_C))
			{
				getlblEntityTxt().setText(TXT_CENTRAL);
			}
			else if (lsCode.equals(InventoryConstant.CHAR_S))
			{
				getlblEntityTxt().setText(TXT_SUBCON);
			}
			// defect 4713
			else if (lsCode.equals(InventoryConstant.CHAR_A))
			{
				getlblEntityId().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getlblEntityTxt().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getstcLblAlreadyAllocted().setText(
					CommonConstant.STR_SPACE_EMPTY);
			}
			//end defect 4713
			else
			{
				getlblEntityTxt().setText(TXT_UNKNOWN);
			}
		}
	}
}