package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmRegistrationItemsINV007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * MAbs			04/25/2002	Changed plate age calculation CQU100003622
 * MAbs			04/26/2002	Changed plate age calculation CQU100003622
 * MAbs			05/22/2002	Added Internet code in case it's not sticker 
 *							CQU100004041
 * MAbs			06/14/2002	Corrected September CQU100004290
 * J Rue		11/14/2002	Check if frame is not visable, 
 *							do not execute actionPerformed. 
 *							modify actionPerformed()
 *							defect 4866
 * B Arredondo	12/19/2002	Made changes for the user help guide so had
 *							to make changes
 *							modify actionPerformed().
 *							defect 5147 
 * Ray Rowehl	03/14/2003	Work on freeze problem.
 *							defect 5372
 * K Harrell	11/30/2004	Remove plate age calculation
 *							move to Transaction.populateMVTrans()
 *							Also remove other sticker related logic.
 *							modify actionPerformed()
 *							defect 7737 Ver 5.2.2 
 * B Hargrove	03/16/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * T Pederson	04/14/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel
 * 							defect 8240 Ver 5.2.3
 * T Pederson	08/02/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/30/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFIRM_ACT
 * 							modify actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * T Pederson	10/13/2006	Added condition for handling driver ed
 * 							exempts.
 * 							modify actionPerformed()
 * 							defect 8900 Ver Exempts 
 * K Harrell	04/08/2007	Use SystemProperty.isHQ()
 * 							delete getBuilderData()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/26/2007	Use CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	06/13/2007	Disable Override checkbox if Special Plate 
 * 							and not REPL and not SPAPPI
 * 							modify setData() 
 * 							defect 9085 Ver Special Plates  
 * K Harrell	10/22/2007	Disable Override checkbox if SPAPPI 
 * 							modify setData() 
 * 							defect 9386 Ver Special Plates 2   
 * K Harrell	12/16/2009	Modify to accommodate new DTA coding, i.e. 
 * 							single DlrTtlData object vs. vector 
 * 							add saveInvForDTADlrTtlData()
 * 							delete saveInvForDTAVector()
 * 							modify gettblItems() 
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * INV007 shows the Issue Inventory Summary table
 *
 * @version	Defect_POS_H		12/16/2009
 * @author	Michael Abernethy
 * <br>Creation Date:			07/13/2001 12:46:17
 */

public class FrmRegistrationItemsINV007
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel = null;
	private JCheckBox ivjchkOverride = null;
	private JPanel ivjFrmRegistrationItemsINV007ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblExpirationMonth = null;
	private JLabel ivjstcLblExpirationMonth = null;
	private RTSTable ivjtblItems = null;
	private TMINV007 caTMINV007;

	// Object 
	private CompleteTransactionData caCompTransData;

	// Vector 
	private Vector cvTableData;

	// Constants 
	private final static String FRM_NAME_INV007 =
		"FrmRegistrationItemsINV007";
	private final static String FRM_TITLE_INV007 =
		"Inventory - Registration Items         INV007";
	private final static String TXT_EXP_MO = "Expiration Month:";
	private final static String TXT_OVRD_ITM_NO = "Override Item No";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRegistrationItemsINV007 laFrmRegistrationItemsINV007;
			laFrmRegistrationItemsINV007 =
				new FrmRegistrationItemsINV007();
			laFrmRegistrationItemsINV007.setModal(true);
			laFrmRegistrationItemsINV007
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmRegistrationItemsINV007.show();
			Insets laInsets = laFrmRegistrationItemsINV007.getInsets();
			laFrmRegistrationItemsINV007.setSize(
				laFrmRegistrationItemsINV007.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRegistrationItemsINV007.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRegistrationItemsINV007.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmRegistrationItemsINV007 constructor
	 */
	public FrmRegistrationItemsINV007()
	{
		super();
		initialize();
	}

	/**
	 * FrmRegistrationItemsINV007 constructor
	 * 
	 * @param JDialog aaParent
	 */
	public FrmRegistrationItemsINV007(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmRegistrationItemsINV007 constructor
	 * 
	 * @param aaParent JDialog
	 * @param abBoolean boolean 
	 */
	public FrmRegistrationItemsINV007(
		JDialog aaParent,
		boolean abBoolean)
	{
		super(aaParent, abBoolean);
		initialize();
	}

	/**
	 * FrmRegistrationItemsINV007 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmRegistrationItemsINV007(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// defect 10290
		// remove 	|| isVisible() == false 
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getchkOverride())
			{
				getchkOverride().setSelected(false);
				HashMap lhmMap = new HashMap();
				lhmMap.put("DATA", caCompTransData);
				lhmMap.put(
					"SELECTED",
					new Integer(gettblItems().getSelectedRow()));
				gettblItems().requestFocus();
				getController().processData(
					VCRegistrationItemsINV007.OVERRIDE,
					lhmMap);
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				CompleteTransactionData laCopyData =
					(CompleteTransactionData) UtilityMethods.copy(
						caCompTransData);
				// defect 7737
				// Move plate age calculation to Transaction.populateMVTrans ()
				// as stickers no longer go through INV007; Only plate. 
				// 
				// boolean containsSticker = false;
				// Vector stickerRecord = new Vector();

				// Loop through each item in the table
				for (int i = 0; i < cvTableData.size(); i++)
				{
					ProcessInventoryData laInvData =
						(ProcessInventoryData) laCopyData
							.getAllocInvItms()
							.get(
							i);
					laInvData.setInvItmEndNo(laInvData.getInvItmNo());

					// update for Tow Truck if they don't supply Tow 
					// Truck Plate Number
					if (laCopyData
						.getTransCode()
						.equals(TransCdConstant.TOWP)
						&& laInvData.getItmCd().equals(
							TransCdConstant.TOWP))
					{
						laCopyData.getTimedPermitData().setTowTrkPltNo(
							laInvData.getInvItmNo());
					}
					// update owner supplied fields
					if (SystemProperty.isHQ())
					{
						laCopyData.setOwnrSuppliedPltNo(
							laInvData.getInvItmNo());
					}
				}

				if (SystemProperty.isHQ())
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);

					int liResponse = leRTSEx.displayError(this);

					if (liResponse == RTSException.YES)
					{
						getController().processData(
							VCRegistrationItemsINV007.HQ,
							laCopyData);
					}
					else
					{
						caCompTransData = laCopyData;
					}
				}
				// end defect 9085 
				else if (
					caCompTransData.getTransCode().equals(
						TransCdConstant.IRENEW))
				{
					getController().processData(
						VCRegistrationItemsINV007.INTERNET,
						laCopyData);
				}
				// defect 8900 
				else if (
					caCompTransData.getTransCode().equals(
						TransCdConstant.DRVED))
				{
					getController().processData(
						VCRegistrationItemsINV007.ISSDRVED,
						laCopyData);
				}
				// end defect 8900 
				else
				{
					// defect 10290 
					saveInvForDTADlrTtlData(
						(CompleteTransactionData) laCopyData);
					// end defect 10290 

					gettblItems().requestFocus();

					getController().processData(
						VCRegistrationItemsINV007.PLATE,
						laCopyData);
				}
				//}
				// end defect 7737 
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
				String lsTransCd = caCompTransData.getTransCode();

				if (lsTransCd.equals(TransCdConstant.RGNCOL))
				{
					RTSHelp.displayHelp(RTSHelp.INV007);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.INV007A);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjButtonPanel property value.
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
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the ivjchkOverride property value
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkOverride()
	{
		if (ivjchkOverride == null)
		{
			try
			{
				ivjchkOverride = new JCheckBox();
				ivjchkOverride.setName("ivjchkOverride");
				ivjchkOverride.setMnemonic(KeyEvent.VK_O);
				ivjchkOverride.setText(TXT_OVRD_ITM_NO);
				ivjchkOverride.setMaximumSize(new Dimension(120, 22));
				ivjchkOverride.setActionCommand(TXT_OVRD_ITM_NO);
				ivjchkOverride.setMinimumSize(new Dimension(120, 22));
				ivjchkOverride.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjchkOverride.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkOverride;
	}

	/**
	 * getData
	 * 
	 * @return Object
	 */
	public Object getData()
	{
		return caCompTransData;
	}

	/**
	 * Return the ivjFrmRegistrationItemsINV007ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmRegistrationItemsINV007ContentPane1()
	{
		if (ivjFrmRegistrationItemsINV007ContentPane1 == null)
		{
			try
			{
				ivjFrmRegistrationItemsINV007ContentPane1 =
					new JPanel();
				ivjFrmRegistrationItemsINV007ContentPane1.setName(
					"ivjFrmRegistrationItemsINV007ContentPane1");
				ivjFrmRegistrationItemsINV007ContentPane1.setLayout(
					new GridBagLayout());
				ivjFrmRegistrationItemsINV007ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmRegistrationItemsINV007ContentPane1
					.setMinimumSize(
					new Dimension(480, 220));

				GridBagConstraints constraintsstcLblExpirationMonth =
					new GridBagConstraints();
				constraintsstcLblExpirationMonth.gridx = 1;
				constraintsstcLblExpirationMonth.gridy = 1;
				constraintsstcLblExpirationMonth.ipadx = 3;
				constraintsstcLblExpirationMonth.insets =
					new Insets(19, 22, 5, 3);
				getFrmRegistrationItemsINV007ContentPane1().add(
					getstcLblExpirationMonth(),
					constraintsstcLblExpirationMonth);

				GridBagConstraints constraintslblExpirationMonth =
					new GridBagConstraints();
				constraintslblExpirationMonth.gridx = 2;
				constraintslblExpirationMonth.gridy = 1;
				constraintslblExpirationMonth.anchor =
					GridBagConstraints.WEST;
				constraintslblExpirationMonth.ipadx = 46;
				constraintslblExpirationMonth.insets =
					new Insets(19, 3, 5, 190);
				getFrmRegistrationItemsINV007ContentPane1().add(
					getlblExpirationMonth(),
					constraintslblExpirationMonth);

				GridBagConstraints constraintsJScrollPane1 =
					new GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.gridwidth = 2;
				constraintsJScrollPane1.fill = GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 334;
				constraintsJScrollPane1.ipady = 103;
				constraintsJScrollPane1.insets =
					new Insets(5, 22, 4, 22);
				getFrmRegistrationItemsINV007ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				GridBagConstraints constraintschkOverride =
					new GridBagConstraints();
				constraintschkOverride.gridx = 2;
				constraintschkOverride.gridy = 3;
				constraintschkOverride.ipadx = 7;
				constraintschkOverride.insets =
					new Insets(5, 10, 6, 137);
				getFrmRegistrationItemsINV007ContentPane1().add(
					getchkOverride(),
					constraintschkOverride);

				GridBagConstraints constraintsbuttonPanel =
					new GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 4;
				constraintsbuttonPanel.gridwidth = 2;
				constraintsbuttonPanel.fill = GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 45;
				constraintsbuttonPanel.ipady = 32;
				constraintsbuttonPanel.insets =
					new Insets(6, 69, 18, 69);
				getFrmRegistrationItemsINV007ContentPane1().add(
					getButtonPanel(),
					constraintsbuttonPanel);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmRegistrationItemsINV007ContentPane1;
	}

	/**
	 * Return the ivjJScrollPane1 property value
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(gettblItems());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjlblExpirationMonth property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationMonth()
	{
		if (ivjlblExpirationMonth == null)
		{
			try
			{
				ivjlblExpirationMonth = new JLabel();
				ivjlblExpirationMonth.setName("ivjlblExpirationMonth");
				ivjlblExpirationMonth.setText("Month");
				ivjlblExpirationMonth.setMaximumSize(
					new Dimension(35, 14));
				ivjlblExpirationMonth.setMinimumSize(
					new Dimension(35, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblExpirationMonth;
	}

	/**
	 * Return the ivjstcLblExpirationMonth property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpirationMonth()
	{
		if (ivjstcLblExpirationMonth == null)
		{
			try
			{
				ivjstcLblExpirationMonth = new JLabel();
				ivjstcLblExpirationMonth.setName(
					"ivjstcLblExpirationMonth");
				ivjstcLblExpirationMonth.setText(TXT_EXP_MO);
				ivjstcLblExpirationMonth.setMaximumSize(
					new Dimension(98, 14));
				ivjstcLblExpirationMonth.setMinimumSize(
					new Dimension(98, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblExpirationMonth;
	}

	/**
	 * getTableData
	 * 
	 * @param avVector Vector
	 * @return Vector
	 */
	private Vector getTableData(Vector avVector)
	{
		Vector lvVector = new Vector();

		for (int i = 0; i < avVector.size(); i++)
		{
			ProcessInventoryData lvProcInvData =
				(ProcessInventoryData) avVector.get(i);

			INV007TableData laTempData = new INV007TableData();

			laTempData.setItemNo(lvProcInvData.getInvItmNo());

			if (lvProcInvData.getInvItmYr() != 0)
			{
				laTempData.setYear(
					Integer.toString(lvProcInvData.getInvItmYr()));
			}
			else
			{
				laTempData.setYear(CommonConstant.STR_SPACE_EMPTY);
			}

			ItemCodesData laItemData =
				ItemCodesCache.getItmCd(lvProcInvData.getItmCd());

			laTempData.setDesc(laItemData.getItmCdDesc());
			lvVector.add(laTempData);
		}
		return lvVector;
	}

	/**
	 * Return the ivjtblItems property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblItems()
	{
		if (ivjtblItems == null)
		{
			try
			{
				ivjtblItems = new RTSTable();
				ivjtblItems.setName("ivjtblItems");
				getJScrollPane1().setColumnHeaderView(
					ivjtblItems.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblItems.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblItems.setModel(new TMINV007());
				ivjtblItems.setShowVerticalLines(false);
				ivjtblItems.setShowHorizontalLines(false);
				ivjtblItems.setIntercellSpacing(new Dimension(0, 0));
				ivjtblItems.setBounds(0, 0, 183, 200);

				// user code begin {1}
				caTMINV007 = (TMINV007) ivjtblItems.getModel();

				// defect 10290
				// Implement Constants  
				// Year
				TableColumn a =
					ivjtblItems.getColumn(
						ivjtblItems.getColumnName(
							CommonConstant.INV007_COL_YEAR));
				a.setPreferredWidth(100);

				// Description 
				TableColumn b =
					ivjtblItems.getColumn(
						ivjtblItems.getColumnName(
							CommonConstant.INV007_COL_ITMDESC));
				b.setPreferredWidth(210);

				// Item Number  
				TableColumn c =
					ivjtblItems.getColumn(
						ivjtblItems.getColumnName(
							CommonConstant.INV007_COL_ITMNO));
				// end defect 10290 
				c.setPreferredWidth(100);

				ivjtblItems.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblItems.init();
				a.setCellRenderer(
					ivjtblItems.setColumnAlignment(RTSTable.CENTER));
				b.setCellRenderer(
					ivjtblItems.setColumnAlignment(RTSTable.LEFT));
				c.setCellRenderer(
					ivjtblItems.setColumnAlignment(RTSTable.RIGHT));
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtblItems;
	}

	/**
	 * Called whenever the part throws an exception
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
	 * Initialize the class
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_INV007);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(450, 300);
			setTitle(FRM_TITLE_INV007);
			setContentPane(getFrmRegistrationItemsINV007ContentPane1());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * saveInvForDTADlrTtlData
	 * 
	 * @param aaCTData CompleteTransactionData
	 */
	private void saveInvForDTADlrTtlData(CompleteTransactionData aaCTData)
	{
		if (UtilityMethods.isDTA(aaCTData.getTransCode()))
		{
			DealerTitleData laDlrTtlData = aaCTData.getDlrTtlData();
			laDlrTtlData.setInventoryData(aaCTData.getAllocInvItms());
		}
	}

	//	/**
	//	 * saveInvForDTAVector
	//	 * 
	//	 * @param aaCompTransData CompleteTransactionData
	//	 */
	//	private void saveInvForDTAVector(CompleteTransactionData aaCompTransData)
	//	{
	//		if (aaCompTransData
	//			.getTransCode()
	//			.equals(TransCdConstant.DTANTD)
	//			|| aaCompTransData.getTransCode().equals(
	//				TransCdConstant.DTANTK)
	//			|| aaCompTransData.getTransCode().equals(
	//				TransCdConstant.DTAORD)
	//			|| aaCompTransData.getTransCode().equals(
	//				TransCdConstant.DTAORK))
	//		{
	//
	//			Vector lvDlrTtlDataObjs =
	//				aaCompTransData.getDlrTtlDataObjs();
	//			int liIndex = 0;
	//			for (int i = 0; i < lvDlrTtlDataObjs.size(); i++)
	//			{
	//				if (((DealerTitleData) lvDlrTtlDataObjs.get(i))
	//					.isProcessed())
	//				{
	//					liIndex = i;
	//				}
	//			}
	//			DealerTitleData laCurrDlr =
	//				(DealerTitleData) lvDlrTtlDataObjs.get(liIndex);
	//			laCurrDlr.setInventoryData(
	//				aaCompTransData.getAllocInvItms());
	//		}
	//	}

	/**
	 * all subclasses must implement this method - it sets the 
	 * caCompTransData on the screen and is how the controller relays 
	 * information to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		caCompTransData = (CompleteTransactionData) aaDataObject;

		if (caCompTransData.getVehicleInfo() != null
			&& caCompTransData.getVehicleInfo().getRegData() != null)
		{
			setMonth(caCompTransData.getRegFeesData().getToMonthDflt());
		}
		else
		{
			getlblExpirationMonth().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}

		cvTableData = getTableData(caCompTransData.getAllocInvItms());
		caTMINV007.add(cvTableData);

		if (cvTableData.size() > 0)
		{
			gettblItems().setRowSelectionInterval(0, 0);

			// defect 9085 
			// Do not allow user to respecify RegPltNo if Special and 
			// not Replacement 
			InventoryAllocationData laInvAllocData =
				(InventoryAllocationData) caCompTransData
					.getAllocInvItms()
					.elementAt(
					0);
			String lsRegPltCd = laInvAllocData.getItmCd();
			String lsTransCd = caCompTransData.getTransCode();
			if (PlateTypeCache.isSpclPlate(lsRegPltCd))
			{
				// defect 9386
				// disable if SPAPPI 
				if (!lsTransCd.equals(TransCdConstant.REPL))
					// && (!lsTransCd.equals(TransCdConstant.SPAPPI))
				{
					getchkOverride().setEnabled(false);
				}
				// end defect 9386 
			}
			// end defect 9085 
		}
	}

	/**
	 * Sets the month name
	 * 
	 * @param aiMonth int
	 */
	private void setMonth(int aiMonth)
	{
		DateFormatSymbols laDateFS = new DateFormatSymbols();
		String larMonths[] = laDateFS.getMonths();
		if (aiMonth > 0 && aiMonth < 13)
		{
			getlblExpirationMonth().setText(larMonths[aiMonth - 1]);
		}
		else
		{
			getlblExpirationMonth().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
	}
}
