package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.OwnershipEvidenceCodesCache;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.OwnershipEvidenceCodesData;
import com.txdot.isd.rts.services.data.RegTtlAddlInfoData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmOwnershipEvidenceTTL011.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * S Govindappa	08/02/2002	Fixed CQU100004469. Made changes to 
 * 							actionPerformed method to set 
 * 							PriorCCOIssueIndi when Title or Corrected 
 * 							Title rejection is done on a corrected title
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 * 							user help guide so had to make changes
 *							in actionPerformed().
 * J Rue		02/20/2003	Defect 5265, Added conditional block to 
 * 							reduce the occurance of ClassCastException 
 * 							error. method setData()
 * K Harrell    05/08/2003  Defect 6085 HQ should show CTL001 screen, 
 * 							should not show PMT004 method 
 * 							actionPerformed()wiener    12/16/2003      
 * 							Defect 6718 When corrected Title and 
 * 							Surrendered Evid is a CCO: Do not carry 
 * 							forward CCOIssueDateevidence is CCO, and 
 * 							always set PriorCCOIssueIndi to 1.
 *                          Method: actionPerformed.  IRR 52040002
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Clean up code
 * 							Comment out unused varaibles, 
 * 							setNextFocusableCompenent()
 * 							deprecated insertSort()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3  
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3                
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/18/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * B Hargrove	01/11/2006	Mis-used variable in edit of Rebuilt Salv 
 * 							75-94 vs Tex Salv Cert.
 * 							modify actionPerformed() 
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/17/2006	Display err msg 551 when choose Owner Evidence
 *							Non Surrendered (RPO) (21) and 
 *							DocTypeCd !=5 (RPO)
 *							add RPO_DOC_TYPE_CD, RPO_OWNRSHP_EVID_CD
 *							modify actionPerformed()
 *							defect 8370 Ver 5.2.2 Fix 8 
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRM_ACTION
 * 							modify actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	05/18/2007	Use SystemProperty.isHQ(); 
 * 							Use CommonConstant.TXT_COMPLETE_TRANSACTION_QUESTION
 * 							Enlarge table to not break across elements
 * 							modify actionPerformed(),gettblOwnrEvid()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/16/2011	Add additional validation for Salvage 
 * 							add validateData(), isSalvageEvidence()
 * 							modify actionPerformed() 
 * 							defect 11051 Ver 6.9.0 	
 * K Harrell	10/29/2011	add isSalvageEvidence()
 * 							modify validateData()
 * 							defect 11051 Ver 6.9.0
 * K Harrell	11/22/2011	If DTA and OwnerShipEvidence <0, do not
 * 							 preselect
 * 							modify setData(), actionPerformed()  
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	12/08/2011	Implement new Rebuilt Salvage Requirements 
 * 							modify validateData()
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	12/16/2011	Implement new Rebuilt Salvage Requirements(2)
 * 							Remove validation logic for Ownership Evidence  
 * 							modify validateData()
 * 							defect 11051 Ver 6.9.0
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to capture the ownership evidence submitted 
 * with the title application.
 *
 * @version	6.9.0 	 		12/16/2011
 * @author	Marx Rajangam
 * <br>Creation Date:		06/26/2001 21:30:57
 */
public class FrmOwnershipEvidenceTTL011
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblSelectPrimaryEvidence = null;
	private JPanel ivjFrmOwnershipEvidenceTTL011ContentPane1 = null;

	private RTSTable ivjScrollPaneTable = null;
	private JScrollPane ivjtblOwnrEvid = null;
	private TMTTL011 caOwnrTblModl = null;
	private Vector cvOwnrEvidData = null;

	// Constant int
	//defect 8370 
	private static final int RPO_DOC_TYPE_CD = 5;
	private static final int RPO_OWNRSHP_EVID_CD = 21;
	// end defect 8370

	// Constant String
	private final static String SEL_PRIMARY_EVDNC_SURR =
		"Select primary evidence surrendered:";
	private final static String OFS_SALV_CERT =
		"OUT-OF-STATE SALVAGE CERT.";
	private final static String TX_SALV_CERT =
		"TEXAS SALVAGE CERTIFICATE";
	private final static String TX_SALV_CERT_TTL =
		"TX SALV CERT OF TITLE";
	private final static String OFS_SALV_CERT_TTL =
		"O/S SALV CERT OF TITLE";
	private final static String CCO_SALV_CERT_TTL =
		"CCO-TX SALV CERT OF TITLE";
	private final static String TX_DUPL_SALV_CERT =
		"TEXAS DUPLICATE SALVAGE CERT.";
	private final static String TX_NON_REPAIR_CERT_TTL =
		"TX NONREPAIR CERT OF TITLE";
	private final static String CCO_TX_NON_REPAIR_CERT_TTL =
		"CCO-TX NONREPAIR CERT OF TITLE";
	private final static String DATA_MISSING_FOR_ISNEXTVCREG029 =
		"Data missing for IsNextVCREG029";

	private VehicleInquiryData caVehInqData = null;
	/**
	 * FrmOwnershipEvidenceTTL011 constructor
	 */
	public FrmOwnershipEvidenceTTL011()
	{
		super();
		initialize();
	}
	/**
	 * FrmOwnershipEvidenceTTL011 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmOwnershipEvidenceTTL011(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmOwnershipEvidenceTTL011 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmOwnershipEvidenceTTL011(JFrame aaParent)
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
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				int liSelRow = getScrollPaneTable().getSelectedRow();

				// defect 11051 
				if (liSelRow <0)
				{
					new RTSException(47).displayError(this); 	
				}
				else
				{
					// end defect 11051 
					OwnershipEvidenceCodesData laDataSelected =
						(
								OwnershipEvidenceCodesData) cvOwnrEvidData
								.elementAt(
										liSelRow);

					if (laDataSelected != null)
					{
						// defect 11051 
						String lsOwnrshpEvidCdDesc = 
							laDataSelected.getOwnrshpEvidCdDesc().toUpperCase();
						// end defect 11051 

						int liIndi =
							caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.getTtlTypeIndi();

						// defect 6718
						if (laDataSelected.getOwnrshpEvidCd() == 12
								&& liIndi == TitleTypes.INT_CORRECTED)
						{
							caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setPriorCCOIssueIndi(
									1);
						}
						else
						{
							// defect 8370
							// Present error if choose "None Surrendered (RPO)"
							// as Ownership Evidence if DocType is not RPO
							if (laDataSelected.getOwnrshpEvidCd()
									== RPO_OWNRSHP_EVID_CD)
							{
								if (caVehInqData
										.getMfVehicleData()
										.getTitleData()
										.getDocTypeCd()
										!= RPO_DOC_TYPE_CD)
								{
									RTSException leRTSEx =
										new RTSException(551);
									leRTSEx.displayError(this);
									getScrollPaneTable().requestFocus();
									return;
								}
							}
							// end defect 8370 

							caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setPriorCCOIssueIndi(
									0);
						}
						caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.setCcoIssueDate(
								0);

						// defect 6718          
						//if (dataSelected.getOwnrshpEvidCd() == 12
						//	&& iIndi == com.txdot.isd.rts.services.util.
						//		constants.TitleTypes.INT_CORRECTED)
						//{
						//	if (caVehInqData.getMfVehicleData().getTitleData()
						//		.getCcoIssueDate() == 0)
						//	{
						//		FrmCCOIssueDateTTL044 issDt = 
						//			new FrmCCOIssueDateTTL044(caVehInqData);
						//                      
						//		if (issDt.getBtnPrs() == 
						//			FrmCCOIssueDateTTL044.CANCEL)
						//		return;
						//	}
						//	else
						//	{
						//		caVehInqData.getMfVehicleData().
						//			getTitleData().
						//	setPriorCCOIssueIndi(1);
						//	}
						//}
						//else
						//{
						//	caVehInqData.getMfVehicleData().getTitleData().
						//		setCcoIssueDate(0);
						//	caVehInqData.getMfVehicleData().getTitleData().
						//	setPriorCCOIssueIndi(0);
						//}
						// end defect 6718

						// defect 11051 
						if (validateData(lsOwnrshpEvidCdDesc))
						{
							// end defect 11051 

							//Set Bonded Title code based on ownership evidence code
							setBndedTtlCode();

							// set the ownership evidence code in title data
							caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setOwnrShpEvidCd(
									laDataSelected.getOwnrshpEvidCd());

							// Present Confirmation Msg for HQ CORTTL,
							if (SystemProperty.isHQ()
									&& getController().getTransCode().equals(
											TransCdConstant.CORTTL))
							{
								RTSException leRTSExMSG =
									new RTSException(
											RTSException.CTL001,
											CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
											ScreenConstant.CTL001_FRM_TITLE);

								// defect 11051 
								if (leRTSExMSG.displayError(this) == RTSException.NO)
								{
									// end defect 11051 
									return;
								}
							}

							getController().processData(
									AbstractViewController.ENTER,
									caVehInqData);
							getScrollPaneTable().requestFocus();
						}
					}
				}
			}
			else if (
					aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
						AbstractViewController.CANCEL,
						getController().getData());
			}
			else if (
					aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.DTAORK)
						|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL011B);
				}
				else if (
						lsTransCd.equals(TransCdConstant.DTANTD)
						|| lsTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL011C);
				}
				else if (
						lsTransCd.equals(TransCdConstant.TITLE)
						|| lsTransCd.equals(TransCdConstant.NONTTL)
						|| lsTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL011A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL011D);
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
				ivjButtonPanel1.setBounds(36, 282, 278, 70);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// defect 7898
				// Comment out setNextFocusableComponent()
				//ivjButtonPanel1.getBtnHelp().setNextFocusableComponent(
				//	getScrollPaneTable());
				// end defect 7898
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
	 * Return the FrmOwnershipEvidenceTTL011ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmOwnershipEvidenceTTL011ContentPane1()
	{
		if (ivjFrmOwnershipEvidenceTTL011ContentPane1 == null)
		{
			try
			{
				ivjFrmOwnershipEvidenceTTL011ContentPane1 =
					new JPanel();
				ivjFrmOwnershipEvidenceTTL011ContentPane1.setName(
					"FrmOwnershipEvidenceTTL011ContentPane1");
				ivjFrmOwnershipEvidenceTTL011ContentPane1.setLayout(
					null);
				ivjFrmOwnershipEvidenceTTL011ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmOwnershipEvidenceTTL011ContentPane1
					.setMinimumSize(
					new Dimension(0, 0));
				getFrmOwnershipEvidenceTTL011ContentPane1().add(
					getstcLblSelectPrimaryEvidence(),
					getstcLblSelectPrimaryEvidence().getName());
				getFrmOwnershipEvidenceTTL011ContentPane1().add(
					gettblOwnrEvid(),
					gettblOwnrEvid().getName());
				getFrmOwnershipEvidenceTTL011ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmOwnershipEvidenceTTL011ContentPane1;
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
				gettblOwnrEvid().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				// defect 7898
				//	setBackingStoreEnabled is deprecated
				//gettblOwnrEvid().getViewport().setBackingStoreEnabled(
				//	true);
				// end defect 7898
				ivjScrollPaneTable.setModel(new TMTTL011());
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				// user code begin {1}
				caOwnrTblModl =
					(TMTTL011) ivjScrollPaneTable.getModel();
				TableColumn a =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				a.setPreferredWidth(25);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable
					.getSelectionModel()
					.addListSelectionListener(
					this);
				ivjScrollPaneTable.setBackground(Color.white);
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
	 * Return the stcLblSelectPrimaryEvidence property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSelectPrimaryEvidence()
	{
		if (ivjstcLblSelectPrimaryEvidence == null)
		{
			try
			{
				ivjstcLblSelectPrimaryEvidence = new JLabel();
				ivjstcLblSelectPrimaryEvidence.setName(
					"stcLblSelectPrimaryEvidence");
				ivjstcLblSelectPrimaryEvidence.setText(
					SEL_PRIMARY_EVDNC_SURR);
				ivjstcLblSelectPrimaryEvidence.setMaximumSize(
					new Dimension(214, 14));
				ivjstcLblSelectPrimaryEvidence.setMinimumSize(
					new Dimension(214, 14));
				ivjstcLblSelectPrimaryEvidence.setBounds(
					55,
					34,
					227,
					21);
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
		return ivjstcLblSelectPrimaryEvidence;
	}
	/**
	 * Return the tblOwnrEvid property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane gettblOwnrEvid()
	{
		if (ivjtblOwnrEvid == null)
		{
			try
			{
				ivjtblOwnrEvid = new JScrollPane();
				ivjtblOwnrEvid.setName("tblOwnrEvid");
				ivjtblOwnrEvid.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjtblOwnrEvid.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjtblOwnrEvid.setBounds(36, 66, 274, 195);
				gettblOwnrEvid().setViewportView(getScrollPaneTable());
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
		return ivjtblOwnrEvid;
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
			setName(ScreenConstant.TTL011_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(338, 371);
			setTitle(ScreenConstant.TTL011_FRAME_TITLE);
			setContentPane(getFrmOwnershipEvidenceTTL011ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Return boolean to denote if selected Ownership Evidence is of 
	 * Salvage Type 
	 *
	 * @return boolean 
	 */
	public static boolean isSalvageEvidence(String asOwnrshpEvidCdDesc)
	{
		return (asOwnrshpEvidCdDesc
				.equals(TX_SALV_CERT)
				|| asOwnrshpEvidCdDesc
				.equals(OFS_SALV_CERT)
					|| asOwnrshpEvidCdDesc
					.equals(
						TX_SALV_CERT_TTL)
						||asOwnrshpEvidCdDesc
						.equals(
								TX_NON_REPAIR_CERT_TTL)
								|| asOwnrshpEvidCdDesc
								.equals(
										OFS_SALV_CERT_TTL)
										|| asOwnrshpEvidCdDesc
										.equals(
												CCO_SALV_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														CCO_TX_NON_REPAIR_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																TX_DUPL_SALV_CERT));
	}
	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Insert code to start the application here.
		try
		{
			FrmOwnershipEvidenceTTL011 laFrmOwnershipEvidenceTTL011;
			laFrmOwnershipEvidenceTTL011 =
				new FrmOwnershipEvidenceTTL011();
			laFrmOwnershipEvidenceTTL011.setModal(true);
			laFrmOwnershipEvidenceTTL011
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laRTSDBox)
				{
					System.exit(0);
				}
			});
			laFrmOwnershipEvidenceTTL011.show();
			Insets laInsets = laFrmOwnershipEvidenceTTL011.getInsets();
			laFrmOwnershipEvidenceTTL011.setSize(
				laFrmOwnershipEvidenceTTL011.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmOwnershipEvidenceTTL011.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmOwnershipEvidenceTTL011.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}
	/**
	 * setBndedTtlCode
	 */
	private void setBndedTtlCode()
	{
		if (caVehInqData != null)
		{
			String lsTransCd = getController().getTransCode();
			if (lsTransCd.equals(TransCdConstant.TITLE)
				|| lsTransCd.equals(TransCdConstant.CORTTL)
				|| lsTransCd.equals(TransCdConstant.REJCOR)
				|| lsTransCd.equals(TransCdConstant.DTAORD)
				|| lsTransCd.equals(TransCdConstant.DTAORK))
			{
				int liSelRow = getScrollPaneTable().getSelectedRow();
				OwnershipEvidenceCodesData aaDataSelected =
					(
						OwnershipEvidenceCodesData) cvOwnrEvidData
							.elementAt(
						liSelRow);
				if (aaDataSelected.getBndedTtlCdReqd() == 1)
				{
					caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.setBndedTtlCd(
						"B");
					caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.setTtlExmnIndi(
						1);
				}
			}
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
		if (aaDataObject != null && aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
					{
						// first element is flag whether to go to REG029
						getController().processData(
							VCSalesTaxTTL012.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCSalesTaxTTL012.REDIRECT_NEXT_VC,
							lvIsNextVCREG029);
					}
				}
				else
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						DATA_MISSING_FOR_ISNEXTVCREG029,
						CommonConstant.STR_ERROR).displayError(
						this);
					return;
				}
			}
		}
		else if (aaDataObject != null)
		{

			if (getController()
				.getTransCode()
				.equals(TransCdConstant.STATJK))
			{
				try
				{
					caVehInqData = (VehicleInquiryData) aaDataObject;
					cvOwnrEvidData =
						OwnershipEvidenceCodesCache
							.getOwnrEvidCdsByEvidSurrCd(
							OwnershipEvidenceCodesCache.GET_B_AND_J);
					if (caOwnrTblModl != null)
					{
						caOwnrTblModl.add(cvOwnrEvidData);
					}
					getScrollPaneTable().setRowSelectionInterval(0, 0);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(this);
				}

			}
			else
			{
				caVehInqData = (VehicleInquiryData) aaDataObject;

				// defect 5265
				// Add conditional for null TitleValidObj
				if (caVehInqData.getValidationObject() != null)
				{
					TitleValidObj laValidObj =
						(TitleValidObj) caVehInqData
						.getValidationObject();
					cvOwnrEvidData =
						(Vector) laValidObj.getOwnrEvidCds();

					if (((RegTtlAddlInfoData) laValidObj
							.getRegTtlAddData())
							.getAddlEviSurndIndi()
							== 0)
					{
						// @TODO Review conditional logic
						// sort by frequency order
						// this is being done by cache?
					}
					else
					{
						// sort by importance order
						Collections.sort(cvOwnrEvidData);
					}

					if (caOwnrTblModl != null)
					{
						caOwnrTblModl.add(cvOwnrEvidData);
					}

					int liCd =
						caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.getOwnrShpEvidCd();
					
					boolean lbOwnrEvidCdNotFound = true;

					// defect 11051 
					if (!(liCd <0 && UtilityMethods.isDTA(getController().getTransCode()))) 
					{
						for (int liIndex = 0;
						liIndex < cvOwnrEvidData.size();
						liIndex++)
						{
							OwnershipEvidenceCodesData lalOECD =
								(
										OwnershipEvidenceCodesData) cvOwnrEvidData
										.get(
												liIndex);
							if (lalOECD.getOwnrshpEvidCd() == liCd)
							{
								getScrollPaneTable()
								.setRowSelectionInterval(
										liIndex,
										liIndex);
								lbOwnrEvidCdNotFound = false;
								break;
							}
						}
						if (lbOwnrEvidCdNotFound)
						{
							getScrollPaneTable().setRowSelectionInterval(
									0,
									0);
						}
					} // end defect 5265
					// end defect 11051 
				}
			}
		}
	}
	/** 
	 * Validate Ownership Evidence 
	 * 
	 * @param asOwnrshpEvidCdDesc
	 * @return boolean 
	 */
	private boolean validateData(String asOwnrshpEvidCdDesc)
	{
		RTSException leRTSEx = null; 
		boolean lbValid = true; 

		String lsRecondCd =
			caVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.getRecondCd();

		if (caVehInqData.getNoMFRecs() == 0)
		{
			if (lsRecondCd
					.equals(TitleConstant.REBUILT_SALVAGE_95_PLUS))
			{
				if (asOwnrshpEvidCdDesc
						.equals(OFS_SALV_CERT)
						|| asOwnrshpEvidCdDesc
						.equals(
								TX_SALV_CERT)
								|| asOwnrshpEvidCdDesc
								.equals(
										TX_SALV_CERT_TTL)
										|| asOwnrshpEvidCdDesc
										.equals(
												OFS_SALV_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														CCO_SALV_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																TX_DUPL_SALV_CERT))
				{
					leRTSEx = new RTSException(656);
				}
			}
			else if (
					lsRecondCd.equals(
							TitleConstant.REBUILT_SALVAGE_ISSUED))
			{
				if (asOwnrshpEvidCdDesc
						.equals(TX_SALV_CERT)
						|| asOwnrshpEvidCdDesc
						.equals(
								TX_SALV_CERT_TTL)
								|| asOwnrshpEvidCdDesc
								.equals(
										TX_NON_REPAIR_CERT_TTL)
										|| asOwnrshpEvidCdDesc
										.equals(
												CCO_SALV_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														CCO_TX_NON_REPAIR_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																TX_DUPL_SALV_CERT))
				{
					leRTSEx = new RTSException(656);
				}
			}
			else if (
					lsRecondCd.equals(
							TitleConstant.REBUILT_SALVAGE_75_94))
			{
				if (asOwnrshpEvidCdDesc
						.equals(OFS_SALV_CERT)
						|| asOwnrshpEvidCdDesc
						.equals(
								// defect 7898
								//TX_SALV_CERT_TTL)
								TX_SALV_CERT) // end 7898
								|| asOwnrshpEvidCdDesc
								.equals(
										TX_NON_REPAIR_CERT_TTL)
										|| asOwnrshpEvidCdDesc
										.equals(
												OFS_SALV_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														CCO_TX_NON_REPAIR_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																TX_DUPL_SALV_CERT))
				{
					leRTSEx = new RTSException(656);
				}
			}
			else if (
					lsRecondCd.equals(
							TitleConstant.SALVAGE_LOSS_UNKNOWN))
			{
				if (asOwnrshpEvidCdDesc
						.equals(OFS_SALV_CERT)
						|| asOwnrshpEvidCdDesc
						.equals(
								TX_SALV_CERT_TTL)
								||asOwnrshpEvidCdDesc
								.equals(
										TX_NON_REPAIR_CERT_TTL)
										|| asOwnrshpEvidCdDesc
										.equals(
												OFS_SALV_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														CCO_SALV_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																CCO_TX_NON_REPAIR_CERT_TTL))
				{
					leRTSEx = new RTSException(656);
				}
			}
			else if (
					lsRecondCd.equals(
							TitleConstant.NOT_REBUILT))
			{
				if (asOwnrshpEvidCdDesc
						.equals(TX_SALV_CERT)
						|| asOwnrshpEvidCdDesc
						.equals(
								OFS_SALV_CERT)
								|| asOwnrshpEvidCdDesc
								.equals(
										TX_SALV_CERT_TTL)
										||asOwnrshpEvidCdDesc
										.equals(
												TX_NON_REPAIR_CERT_TTL)
												|| asOwnrshpEvidCdDesc
												.equals(
														OFS_SALV_CERT_TTL)
														|| asOwnrshpEvidCdDesc
														.equals(
																CCO_SALV_CERT_TTL)
																|| asOwnrshpEvidCdDesc
																.equals(
																		CCO_TX_NON_REPAIR_CERT_TTL)
																		|| asOwnrshpEvidCdDesc
																		.equals(
																				TX_DUPL_SALV_CERT))
				{
					leRTSEx = new RTSException(656);
				}
			}
		}
		if (leRTSEx != null)
		{
			leRTSEx.displayError(this); 
			lbValid = false; 
		}
		return lbValid; 
	}
	
	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLE the event that characterizes the change.
	  */
	public void valueChanged(ListSelectionEvent aaLE)
	{
		// defect 7898
		//	Variable liValue is not used
		//int liValue = 0;
		// end defect 7898
	}
}