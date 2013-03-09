package com.txdot.isd.rts.client.common.ui;

import java.awt.*;
import java.awt.event.*;
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

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmRecordFoundCTL005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking().
 * Btulsiani 	04/30/2002	Added next focusable component to table for 
 *							tabbing.
 * MAbs/TP		06/05/2002	MultiRecs in Archive CQU100004019
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * B Hargrove	03/16/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * T Pederson	04/13/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	05/20/2005	More code cleanup
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	05/27/2005	Remove selection made when using arrow keys
 * 							modify keyPressed()
 * 							defect 7885 Ver 5.2.3
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3
 * Ray Rowehl	07/05/2005	Work on constants.
 * 							Remove FocusListener sections.
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/09/2005	More work on constants.
 * 							Review method JavaDocs.
 * 							defect 7885 Ver 5.2.3
 * T Pederson	08/29/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		12/20/2005	Changed ButtonGroup to use RTSButtonGroup.
 * 							remove keyPressed() 
 * 							modify getSelectionPanel() 
 * 							defect 7885 Ver 5.2.3
 * T. Pederson	12/21/2005	Removed setting default focus. Focus will
 * 							automatically go to cancelled plate table.
 * 							delete windowActivated(), windowClosed(),
 * 							windowClosing(), windowDeactivated(),
 * 							windowDeiconified(), windowIconified() and 
 * 							windowOpened().
 * 							defect 8494 Ver 5.2.3
 * K Harrell	06/28/2009	Implement new OwnerData. Additional Cleanup.
 * 							delete getBuilderData()
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/13/2011	Change 'Cancelled' to 'Canceled' 
 * 							modify CTL005_FRM_TITLE,TXT_RECORD_IN_CANCELED
 * 							defect 11052 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * Frame Record Found CTL005
 *
 * @version 6.9.0  			12/13/2011
 * @author	Michael Abernethy
 * <br>Creation Date: 		07/13/2001 09:56:58
 */

public class FrmRecordFoundCTL005
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JScrollPane ivjJScrollPane11 = null;
	private JLabel ivjlblPlate = null;
	private JRadioButton ivjradioArchive = null;
	private JRadioButton ivjradioRecordNA = null;
	private JPanel ivjSelectionPanel = null;
	private JLabel ivjstcLblKey = null;
	private JLabel ivjstcLblRecordArchive = null;
	private JLabel ivjstcLblRecordFound = null;
	private RTSTable ivjtblArchiveFile = null;
	private RTSTable ivjtblCancelledPlate = null;
	private RTSButtonGroup caRadioGroup;
	private TMCTL005 caTableModelArchive;
	private TMCTL005 caTableModelCancelled;

	// Object
	private GeneralSearchData caSearchData = new GeneralSearchData();
	private VehicleInquiryData caArchiveRecord =
		new VehicleInquiryData();
	private VehicleInquiryData caCancelledRecord =
		new VehicleInquiryData();

	// Constants 
	private static final String CTL005_FRM_BEAN_NAME =
		"FrmRecordFoundCTL005";
	// defect 11052 
	// Cancelled to Canceled
	private static final String CTL005_FRM_TITLE =
		"Record(s) found on Canceled Plate and Archive file   CTL005";
	    //"Record(s) found on Cancelled Plate and Archive file   CTL005";
	// end defect 11052 

	private static final String TXT_ACTION_NA = "NA";
	private static final String TXT_ACTION_USEARCHIVE = "UseArchive";
	private static final String TXT_BORDER_SELECT_ACTION =
		"Select action for record(s) found:";
	private static final String TXT_DOC_NO = "Doc No";
	private static final String TXT_EQUAL_SIGN = " = ";
	private static final String TXT_KEY = "Key =";
	private static final String TXT_PLATE = "Plate";
	private static final String TXT_RECORD_IN_ARCHIVE =
		"Record found in Archive file:";
	// defect 11052 
	// Cancelled to Canceled 
	private static final String TXT_RECORD_IN_CANCELED =
		"Record found in Canceled Plate file:";
		//"Record found in Cancelled Plate file:";
	// end defect 11052 
	private static final String TXT_RECORD_NA = "Record not applicable";
	private static final String TXT_STICKER_NO = "Sticker No";
	private static final String TXT_USE_ARCHIVE_REC =
		"Use Archive file record";
	private static final String TXT_VIN = "VIN";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRecordFoundCTL005 laFrmRecordFoundCTL005;
			laFrmRecordFoundCTL005 = new FrmRecordFoundCTL005();
			laFrmRecordFoundCTL005.setModal(true);
			laFrmRecordFoundCTL005
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmRecordFoundCTL005.show();
			Insets laInsets = laFrmRecordFoundCTL005.getInsets();
			laFrmRecordFoundCTL005.setSize(
				laFrmRecordFoundCTL005.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRecordFoundCTL005.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRecordFoundCTL005.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * FrmRecordFoundCTL005 constructor comment
	 */
	public FrmRecordFoundCTL005()
	{
		super();
		initialize();
	}

	/**
	 * FrmRecordFoundCTL005 constructor comment
	 * 
	 * @param aaParent JDialog
	 */
	public FrmRecordFoundCTL005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmRecordFoundCTL005 constructor comment
	 * 
	 * @param aaParent JFrame
	 */
	public FrmRecordFoundCTL005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is selected
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
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (getradioArchive().isSelected())
				{
					if (caArchiveRecord.getPartialDataList().size()
						> 0)
					{
						getController().processData(
							VCRecordFoundCTL005.MULTI,
							caArchiveRecord);
					}
					else
					{
						//Indicate no records found
						caArchiveRecord.setNoMFRecs(0);
						
						//Indicate Archive already searched
						caArchiveRecord.setSearchArchiveIndi(1);
						getController().processData(
							VCRecordFoundCTL005.SEARCH_ARCHIVE,
							caSearchData);
					}
				}
				else if (getradioRecordNA().isSelected())
				{
					caArchiveRecord.setNoMFRecs(0);
					getController().processData(
						AbstractViewController.ENTER,
						caArchiveRecord);
				}
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
				RTSHelp.displayHelp(RTSHelp.CTL005);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjButtonPanel1 property value
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setLayout(new FlowLayout());
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the ivjJDialogBoxContentPane property value
	 * 
	 * @return JPanel
	 */
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				ivjJDialogBoxContentPane.setName(
					"ivjJDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(new GridBagLayout());

				GridBagConstraints constraintsstcLblKey =
					new GridBagConstraints();
				constraintsstcLblKey.gridx = 1;
				constraintsstcLblKey.gridy = 1;
				constraintsstcLblKey.ipadx = 1;
				constraintsstcLblKey.insets = new Insets(18, 20, 7, 3);
				getJDialogBoxContentPane().add(
					getstcLblKey(),
					constraintsstcLblKey);

				GridBagConstraints constraintslblPlate =
					new GridBagConstraints();
				constraintslblPlate.gridx = 2;
				constraintslblPlate.gridy = 1;
				constraintslblPlate.ipadx = 261;
				constraintslblPlate.insets = new Insets(18, 3, 7, 278);
				getJDialogBoxContentPane().add(
					getlblPlate(),
					constraintslblPlate);

				GridBagConstraints constraintsstcLblRecordFound =
					new GridBagConstraints();
				constraintsstcLblRecordFound.gridx = 1;
				constraintsstcLblRecordFound.gridy = 2;
				constraintsstcLblRecordFound.gridwidth = 2;
				constraintsstcLblRecordFound.ipadx = 39;
				constraintsstcLblRecordFound.insets =
					new Insets(7, 20, 0, 338);
				getJDialogBoxContentPane().add(
					getstcLblRecordFound(),
					constraintsstcLblRecordFound);

				GridBagConstraints constraintsJScrollPane1 =
					new GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 3;
				constraintsJScrollPane1.gridwidth = 2;
				constraintsJScrollPane1.fill = GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 537;
				constraintsJScrollPane1.ipady = 60;
				constraintsJScrollPane1.insets =
					new Insets(1, 20, 3, 21);
				getJDialogBoxContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				GridBagConstraints constraintsstcLblRecordArchive =
					new GridBagConstraints();
				constraintsstcLblRecordArchive.gridx = 1;
				constraintsstcLblRecordArchive.gridy = 4;
				constraintsstcLblRecordArchive.gridwidth = 2;
				constraintsstcLblRecordArchive.ipadx = 89;
				constraintsstcLblRecordArchive.insets =
					new Insets(4, 20, 2, 333);
				getJDialogBoxContentPane().add(
					getstcLblRecordArchive(),
					constraintsstcLblRecordArchive);

				GridBagConstraints constraintsJScrollPane11 =
					new GridBagConstraints();
				constraintsJScrollPane11.gridx = 1;
				constraintsJScrollPane11.gridy = 5;
				constraintsJScrollPane11.gridwidth = 2;
				constraintsJScrollPane11.fill = GridBagConstraints.BOTH;
				constraintsJScrollPane11.weightx = 1.0;
				constraintsJScrollPane11.weighty = 1.0;
				constraintsJScrollPane11.ipadx = 537;
				constraintsJScrollPane11.ipady = 60;
				constraintsJScrollPane11.insets =
					new Insets(3, 20, 3, 21);
				getJDialogBoxContentPane().add(
					getJScrollPane11(),
					constraintsJScrollPane11);

				GridBagConstraints constraintsSelectionPanel =
					new GridBagConstraints();
				constraintsSelectionPanel.gridx = 1;
				constraintsSelectionPanel.gridy = 6;
				constraintsSelectionPanel.gridwidth = 2;
				constraintsSelectionPanel.fill =
					GridBagConstraints.BOTH;
				constraintsSelectionPanel.weightx = 1.0;
				constraintsSelectionPanel.weighty = 1.0;
				constraintsSelectionPanel.insets =
					new Insets(3, 21, 1, 33);
				getJDialogBoxContentPane().add(
					getSelectionPanel(),
					constraintsSelectionPanel);

				GridBagConstraints constraintsButtonPanel1 =
					new GridBagConstraints();
				constraintsButtonPanel1.gridx = 2;
				constraintsButtonPanel1.gridy = 7;
				constraintsButtonPanel1.fill = GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 121;
				constraintsButtonPanel1.ipady = 19;
				constraintsButtonPanel1.insets =
					new Insets(2, 53, 6, 154);
				getJDialogBoxContentPane().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjJDialogBoxContentPane;
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
				getJScrollPane1().setViewportView(
					gettblCancelledPlate());
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
	 * Return the ivjJScrollPane11 property value
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane11()
	{
		if (ivjJScrollPane11 == null)
		{
			try
			{
				ivjJScrollPane11 = new JScrollPane();
				ivjJScrollPane11.setName("ivjJScrollPane11");
				ivjJScrollPane11.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane11.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane11().setViewportView(gettblArchiveFile());
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
		return ivjJScrollPane11;
	}

	/**
	 * Return the ivjlblPlate property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlate()
	{
		if (ivjlblPlate == null)
		{
			try
			{
				ivjlblPlate = new JLabel();
				ivjlblPlate.setName("ivjlblPlate");
				ivjlblPlate.setText("");
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
		return ivjlblPlate;
	}

	/**
	 * Return the ivjradioArchive property value
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioArchive()
	{
		if (ivjradioArchive == null)
		{
			try
			{
				ivjradioArchive = new JRadioButton();
				ivjradioArchive.setName("ivjradioArchive");
				ivjradioArchive.setMnemonic(KeyEvent.VK_A);
				ivjradioArchive.setText(TXT_USE_ARCHIVE_REC);
				ivjradioArchive.setActionCommand(TXT_ACTION_USEARCHIVE);
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
		return ivjradioArchive;
	}

	/**
	 * Return the ivjradioRecordNA property value
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioRecordNA()
	{
		if (ivjradioRecordNA == null)
		{
			try
			{
				ivjradioRecordNA = new JRadioButton();
				ivjradioRecordNA.setName("ivjradioRecordNA");
				ivjradioRecordNA.setSelected(true);
				ivjradioRecordNA.setMnemonic(KeyEvent.VK_R);
				ivjradioRecordNA.setText(TXT_RECORD_NA);
				ivjradioRecordNA.setActionCommand(TXT_ACTION_NA);
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
		return ivjradioRecordNA;
	}

	/**
	 * Return ivjSelectionPanel property value
	 * 
	 * @return JPanel
	 */
	private JPanel getSelectionPanel()
	{
		if (ivjSelectionPanel == null)
		{
			try
			{
				ivjSelectionPanel = new JPanel();
				ivjSelectionPanel.setName("ivjSelectionPanel");
				ivjSelectionPanel.setLayout(new GridBagLayout());

				GridBagConstraints constraintsradioRecordNA =
					new GridBagConstraints();
				constraintsradioRecordNA.gridx = 1;
				constraintsradioRecordNA.gridy = 1;
				constraintsradioRecordNA.ipadx = 28;
				constraintsradioRecordNA.insets =
					new Insets(9, 199, 5, 171);
				getSelectionPanel().add(
					getradioRecordNA(),
					constraintsradioRecordNA);

				GridBagConstraints constraintsradioArchive =
					new GridBagConstraints();
				constraintsradioArchive.gridx = 1;
				constraintsradioArchive.gridy = 2;
				constraintsradioArchive.insets =
					new Insets(5, 199, 9, 193);
				getSelectionPanel().add(
					getradioArchive(),
					constraintsradioArchive);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						TXT_BORDER_SELECT_ACTION);
				ivjSelectionPanel.setBorder(laBorder);
				caRadioGroup = new RTSButtonGroup();
				caRadioGroup.add(getradioArchive());
				caRadioGroup.add(getradioRecordNA());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjSelectionPanel;
	}

	/**
	 * Return the ivjstcLblKey property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblKey()
	{
		if (ivjstcLblKey == null)
		{
			try
			{
				ivjstcLblKey = new JLabel();
				ivjstcLblKey.setName("ivjstcLblKey");
				ivjstcLblKey.setText(TXT_KEY);
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
		return ivjstcLblKey;
	}

	/**
	 * Return the ivjstcLblRecordArchive property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRecordArchive()
	{
		if (ivjstcLblRecordArchive == null)
		{
			try
			{
				ivjstcLblRecordArchive = new JLabel();
				ivjstcLblRecordArchive.setName(
					"ivjstcLblRecordArchive");
				ivjstcLblRecordArchive.setText(TXT_RECORD_IN_ARCHIVE);
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
		return ivjstcLblRecordArchive;
	}

	/**
	 * Return the ivjstcLblRecordFound property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRecordFound()
	{
		if (ivjstcLblRecordFound == null)
		{
			try
			{
				ivjstcLblRecordFound = new JLabel();
				ivjstcLblRecordFound.setName("ivjstcLblRecordFound");
				ivjstcLblRecordFound.setText(TXT_RECORD_IN_CANCELED);
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
		return ivjstcLblRecordFound;
	}

	/**
	 * Return the ivjtblArchiveFile property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblArchiveFile()
	{
		if (ivjtblArchiveFile == null)
		{
			try
			{
				ivjtblArchiveFile = new RTSTable();
				ivjtblArchiveFile.setName("ivjtblArchiveFile");
				getJScrollPane11().setColumnHeaderView(
					ivjtblArchiveFile.getTableHeader());
				getJScrollPane11().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblArchiveFile.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblArchiveFile.setModel(new TMCTL005());
				ivjtblArchiveFile.setShowVerticalLines(false);
				ivjtblArchiveFile.setShowHorizontalLines(false);
				ivjtblArchiveFile.setAutoCreateColumnsFromModel(false);
				ivjtblArchiveFile.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModelArchive =
					(TMCTL005) ivjtblArchiveFile.getModel();
				TableColumn laTableColumnA =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(0));
				laTableColumnA.setWidth(50);
				laTableColumnA.setMaxWidth(50);
				laTableColumnA.setMinWidth(50);
				TableColumn laTableColumnB =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(1));
				laTableColumnB.setWidth(40);
				laTableColumnB.setMaxWidth(40);
				laTableColumnB.setMinWidth(40);
				TableColumn laTableColumnC =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(2));
				laTableColumnC.setWidth(165);
				laTableColumnC.setMaxWidth(165);
				laTableColumnC.setMinWidth(165);
				TableColumn laTableColumnD =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(3));
				laTableColumnD.setWidth(60);
				laTableColumnD.setMaxWidth(60);
				laTableColumnD.setMinWidth(60);
				TableColumn laTableColumnE =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(4));
				laTableColumnE.setWidth(45);
				laTableColumnE.setMaxWidth(45);
				laTableColumnE.setMinWidth(45);
				TableColumn laTableColumnF =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(5));
				laTableColumnF.setWidth(45);
				laTableColumnF.setMaxWidth(45);
				laTableColumnF.setMinWidth(45);
				TableColumn laTableColumnG =
					ivjtblArchiveFile.getColumn(
						ivjtblArchiveFile.getColumnName(6));
				laTableColumnG.setWidth(147);
				laTableColumnG.setMaxWidth(147);
				laTableColumnG.setMinWidth(147);
				ivjtblArchiveFile.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblArchiveFile.init();
				laTableColumnA.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnC.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnD.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnE.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnF.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnG.setCellRenderer(
					ivjtblArchiveFile.setColumnAlignment(
						RTSTable.LEFT));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblArchiveFile;
	}

	/**
	 * Return ivjtblCancelledPlate property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblCancelledPlate()
	{
		if (ivjtblCancelledPlate == null)
		{
			try
			{
				ivjtblCancelledPlate = new RTSTable();
				ivjtblCancelledPlate.setName("ivjtblCancelledPlate");
				getJScrollPane1().setColumnHeaderView(
					ivjtblCancelledPlate.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCancelledPlate.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCancelledPlate.setModel(new TMCTL005());
				ivjtblCancelledPlate.setShowVerticalLines(false);
				ivjtblCancelledPlate.setShowHorizontalLines(false);
				ivjtblCancelledPlate.setAutoCreateColumnsFromModel(
					false);
				ivjtblCancelledPlate.setIntercellSpacing(
					new Dimension(0, 0));
				ivjtblCancelledPlate.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModelCancelled =
					(TMCTL005) ivjtblCancelledPlate.getModel();
				TableColumn laTableColumnA =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(0));
				laTableColumnA.setWidth(50);
				laTableColumnA.setMaxWidth(50);
				laTableColumnA.setMinWidth(50);
				TableColumn laTableColumnB =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(1));
				laTableColumnB.setWidth(40);
				laTableColumnB.setMaxWidth(40);
				laTableColumnB.setMinWidth(40);
				TableColumn laTableColumnC =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(2));
				laTableColumnC.setWidth(165);
				laTableColumnC.setMaxWidth(165);
				laTableColumnC.setMinWidth(165);
				TableColumn laTableColumnD =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(3));
				laTableColumnD.setWidth(60);
				laTableColumnD.setMaxWidth(60);
				laTableColumnD.setMinWidth(60);
				TableColumn laTableColumnE =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(4));
				laTableColumnE.setWidth(45);
				laTableColumnE.setMaxWidth(45);
				laTableColumnE.setMinWidth(45);
				TableColumn laTableColumnF =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(5));
				laTableColumnF.setWidth(45);
				laTableColumnF.setMaxWidth(45);
				laTableColumnF.setMinWidth(45);
				TableColumn laTableColumnG =
					ivjtblCancelledPlate.getColumn(
						ivjtblCancelledPlate.getColumnName(6));
				laTableColumnG.setWidth(147);
				laTableColumnG.setMaxWidth(147);
				laTableColumnG.setMinWidth(147);
				ivjtblCancelledPlate.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblCancelledPlate.init();
				laTableColumnA.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnC.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnD.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnE.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnF.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnG.setCellRenderer(
					ivjtblCancelledPlate.setColumnAlignment(
						RTSTable.LEFT));
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtblCancelledPlate;
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
	 * Initialize the class
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(CTL005_FRM_BEAN_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(600, 400);
			setModal(true);
			setTitle(CTL005_FRM_TITLE);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		addWindowListener(this);
		// user code end
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
		String lsKeyName = null;
		Vector lvVector = (Vector) UtilityMethods.copy(aaDataObject);
		caCancelledRecord = (VehicleInquiryData) lvVector.elementAt(0);
		caArchiveRecord = (VehicleInquiryData) lvVector.elementAt(1);
		if (caArchiveRecord.getMfVehicleData() == null)
		{
			MFVehicleData laMfVeh = new MFVehicleData();
			laMfVeh.setVehicleData(new VehicleData());
			laMfVeh.getVehicleData().setVehModlYr(0);
			laMfVeh.getVehicleData().setVehMk(
				CommonConstant.STR_SPACE_EMPTY);
			laMfVeh.setRegData(new RegistrationData());
			laMfVeh.getRegData().setCancPltIndi(0);
			laMfVeh.getVehicleData().setVin(
				CommonConstant.STR_SPACE_EMPTY);
			laMfVeh.getRegData().setRegPltNo(
				((MFPartialData) caArchiveRecord
					.getPartialDataList()
					.get(0))
					.getRegPltNo());
			laMfVeh.setOwnerData(new OwnerData());
			
			// defect 10112 
			laMfVeh.getOwnerData().setName1(
				CommonConstant.STR_SPACE_EMPTY);
			// end defect 10112
			  
			caArchiveRecord.setMfVehicleData(laMfVeh);
		}
		caSearchData = (GeneralSearchData) lvVector.elementAt(2);
		if (caSearchData.getKey1().equals(CommonConstant.DOC_NO))
		{
			lsKeyName = TXT_DOC_NO;
		}
		else if (
			caSearchData.getKey1().equals(CommonConstant.REG_PLATE_NO))
		{
			lsKeyName = TXT_PLATE;
		}
		else if (caSearchData.getKey1().equals(CommonConstant.VIN))
		{
			lsKeyName = TXT_VIN;
		}
		else if (
			caSearchData.getKey1().equals(
				CommonConstant.REG_STICKER_NO))
		{
			lsKeyName = TXT_STICKER_NO;
		}

		getlblPlate().setText(
			lsKeyName + TXT_EQUAL_SIGN + caSearchData.getKey2());

		caTableModelCancelled.add(caCancelledRecord);
		if (caCancelledRecord != null)
		{
			gettblCancelledPlate().setRowSelectionInterval(0, 0);
		}
		caTableModelArchive.add(caArchiveRecord);
		if (caArchiveRecord != null)
		{
			gettblArchiveFile().setRowSelectionInterval(0, 0);
		}
	}
}