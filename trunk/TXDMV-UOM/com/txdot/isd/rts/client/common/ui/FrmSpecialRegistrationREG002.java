package com.txdot.isd.rts.client.common.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.data.CommonFeesData;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmSpecialRegistrationREG002.java
 *
 * (c) Texas Department of Transportation 2001
 *---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()
 * Ray Rowehl	05/05/2003	Change to not show INQ007 if this is VEHINQ 
 * 							and HeadQuarters.
 *							modify actionPerformed()
 *							defect 6065
 * Ray Rowehl	05/07/2003	Change to populate customer number for 
 * 							VEHINQ.  New finding.
 *							modify actionPerformed()
 *							defect 6065.
 * B Hargrove	03/15/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
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
 * B Hargrove	09/27/2005	Change more text strings to constants.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/06/2005	Remove set color
 * 							modify getlblName1(),getlblName2() 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	10/25/2006	Do not show "0" for Exp Mo or Yr
 * 							modify setData()
 * 							defect 8900 Ver Exempts
 * K Harrell	04/08/2007	Use SystemProperty.isHQ()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/11/2008	Remove Sticker Logic 
 * 							add TXT_CNCLD_PLT_FOR_VIN
 * 							delete TXT_CNCLD_PLTSTK, TXT_STK_TYPE
 * 							delete ivjlblStickerType,
 * 							     ivjstcLblStickerType
 * 							delete getlblStickerType(), 
 * 								 getstcLblStickerType()
 * 							modify DFLT_DRVED, TXT_CNCLD_PLT 
 * 							modify getstcLblCancelled()
 * 							defect 7143 Ver Defect POS A
 * K Harrell	04/25/2008	Add Doc No to Screen when Cancelled Plates
 * 							add ivjlblDocNo, ivjstcLblCancelledPlateDocNo
 * 							 getDocNo(), getstcLblCancelledPlateDocNo(), 
 * 							 TXT_CNCLD_PLT_VIN, TXT_CNCLD_PLT_DOC_NO
 * 							delete TXT_CNCLD_PLTSTK 
 * 							modify DFLT_DRVED, setData()
 * 							defect 9641 Ver Defect POS A
 * K Harrell	06/28/2009	Implement new OwnerData. Additional Cleanup
 * 							add DFLT_OWNR_ST1, DFLT_OWNR_ST2,
 * 							  DFLT_OWNR_CITY_ST_ZIP 
 * 							modify actionPerformed(), 
 * 							 getScrollPaneTable(), getlblAddress1(), 
 * 							 getlblAddress2(), getlblCityStateZip() 
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	11/14/2011	Accommodate VTR 275 
 * 							modify actionPerformed() 
 * 							defect 11052 Ver 6.9.0
 *----------------------------------------------------------------------
 */
/**
 * Frame Special Registration REG002
 *
 * @version 6.9.0		11/14/2011	
 * @author	Administrator
 * <br>Creation Date: 	06/25/2001 13:21:53 
 */

public class FrmSpecialRegistrationREG002
	extends RTSDialogBox
	implements ActionListener
{

	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjFrmSpecialRegistrationREG002ContentPane1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblAddress1 = null;
	private JLabel ivjlblAddress2 = null;
	private JLabel ivjlblCityStateZip = null;
	private JLabel ivjlblDocNo = null;
	private JLabel ivjlblEffDate = null;
	private JLabel ivjlblExpDate = null;
	private JLabel ivjlblIssueDate = null;
	private JLabel ivjlblName1 = null;
	private JLabel ivjlblName2 = null;
	private JLabel ivjlblOwnerID = null;
	private JLabel ivjlblPlateNo = null;
	private JLabel ivjlblPlateType = null;
	private JLabel ivjlblRegClass = null;
	private JLabel ivjlblVIN = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcExpDate = null;
	private JLabel ivjstcLblCancelled = null;
	private JLabel ivjstcLblCancelledDocNo = null;
	private JLabel ivjstcLblEffDate = null;
	private JLabel ivjstcLblIssueDate = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblPlateType = null;
	private JLabel ivjstcLblRegClass = null;
	private TMREG002 caTableModel;

	// String 
	private String csTransCd;

	// Object 
	private VehicleInquiryData caVehData;

	// Constants
	private final static String DFLT_CHARS6 = "XXXXXX";
	private final static String DFLT_DATE = "06/02/2002";
	private final static String DFLT_DRVED = "DRIVER EDUCATION";
	private final static String DFLT_OWNR_NM1 = "OWNER NAME1";
	private final static String DFLT_OWNR_NM2 = "OWNER NAME2";
	// defect 10112 
	private final static String DFLT_OWNR_ST1 = "STREET1";
	private final static String DFLT_OWNR_ST2 = "STREET2";
	private final static String DFLT_OWNR_CITY_ST_ZIP = "CITY, ST 77001";
	// end defect 10112 
	private final static String DFLT_VIN = "VIN";
	private final static String FRM_NAME_REG002 =
		"FrmSpecialRegistrationREG002";
	private final static String FRM_TITLE_REG002 =
		"Special Owner        REG002";
	private final static String TXT_CNCLD_PLT = "** Canceled Plate **";
	private final static String TXT_CNCLD_PLT_DOC_NO =
		"Canceled Plate Doc No: ";
	private final static String TXT_CNCLD_PLT_VIN =
		"Canceled Plate VIN: ";
	private final static String TXT_EFF_DT = "Eff Date:";
	private final static String TXT_EXP_DT = "Exp Date:";
	private final static String TXT_ISS_DT = "Issue Date:";
	private final static String TXT_OWN_ID = "Owner Id:";
	private final static String TXT_PLT_NO = "Plate No:";
	private final static String TXT_PLT_TYPE = "Plate Type:";
	private final static String TXT_REG_CLS = "Reg Class:";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSpecialRegistrationREG002 laFrmSpecialRegistrationREG002;
			laFrmSpecialRegistrationREG002 =
				new FrmSpecialRegistrationREG002();
			laFrmSpecialRegistrationREG002.setModal(true);
			laFrmSpecialRegistrationREG002
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});

			laFrmSpecialRegistrationREG002.show();
			Insets laInsets =
				laFrmSpecialRegistrationREG002.getInsets();
			laFrmSpecialRegistrationREG002.setSize(
				laFrmSpecialRegistrationREG002.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSpecialRegistrationREG002.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSpecialRegistrationREG002.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSpecialRegistrationREG002 constructor
	 */
	public FrmSpecialRegistrationREG002()
	{
		super();
		initialize();
	}

	/**
	 * FrmSpecialRegistrationREG002 constructor
	 * 
	 * @param aaParent JDialog 
	 */
	public FrmSpecialRegistrationREG002(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSpecialRegistrationREG002 constructor
	 * 
	 * @param aaParent JFrame 
	 */
	public FrmSpecialRegistrationREG002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is pressed
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
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				// defect 10112 
				// This is VehInq only screen 
				// if (csTransCd.equals(TransCdConstant.VEHINQ))
				//{
				boolean lbHQ = SystemProperty.isHQ();

				int liPrintOption = caVehData.getPrintOptions();

				// If !HQ & (View & Print || View,Print&Charge Fee)  
				// defect 11052
				if (!lbHQ && liPrintOption != VehicleInquiryData.VIEW_ONLY) 
				//			&& (liPrintOption == VehicleInquiryData.VIEW_AND_PRINT
				//				|| liPrintOption
				//					== VehicleInquiryData
				//						.CHARGE_FEE_VIEW_AND_PRINT))
				{
					// end defect 11052 
					getController().processData(
						VCSpecialRegistrationREG002.INQ007,
						caVehData);
				}
				else
				{
					// end defect 10112 
					CompleteTransactionData laCompTransData =
						new CompleteTransactionData();
					laCompTransData.setNoMFRecs(
						caVehData.getNoMFRecs());
					laCompTransData.setOrgVehicleInfo(
						caVehData.getMfVehicleData());
					laCompTransData.setVehicleInfo(
						caVehData.getMfVehicleData());
					// defect 11052 
					laCompTransData.setTransCode(
						//getController().getTransCode());
							UtilityMethods.getVehInqTransCd(liPrintOption)); 
					// end defect 11052 
							
					laCompTransData.setPrintOptions(liPrintOption);

					// Set the customer name for HQ VEHINQ
					if (caVehData
						.getMfVehicleData()
						.getRegData()
						.getRecpntName()
						!= null)
					{
						laCompTransData.setCustName(
							caVehData
								.getMfVehicleData()
								.getRegData()
								.getRecpntName());
					}
					// defect 10112 
					else if (
						caVehData.getMfVehicleData().getOwnerData()
							!= null
							&& caVehData
								.getMfVehicleData()
								.getOwnerData()
								.getName1()
								!= null)
					{
						laCompTransData.setCustName(
							caVehData
								.getMfVehicleData()
								.getOwnerData()
								.getName1());
					}
					// end defect 10112 
					else if (
						caVehData
							.getMfVehicleData()
							.getRegData()
							.getCancPltIndi()
							> 0)
					{
						laCompTransData.setCustName(TXT_CNCLD_PLT);
					}
					else
					{
						laCompTransData.setCustName(
							CommonConstant.STR_SPACE_EMPTY);
					}
					getController().processData(
						AbstractViewController.ENTER,
						laCompTransData);
				}
			}
			// defect 10112 
			//}
			// end defect 10112
			if (aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG002);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbuttonPanel property value.
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
				ivjbuttonPanel.setName("ivjbuttonPanel");
				ivjbuttonPanel.setBounds(147, 392, 317, 42);
				ivjbuttonPanel.setMinimumSize(new Dimension(217, 35));
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
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
		return ivjbuttonPanel;
	}

	/**
	 * Return the ivjFrmSpecialRegistrationREG002ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSpecialRegistrationREG002ContentPane1()
	{
		if (ivjFrmSpecialRegistrationREG002ContentPane1 == null)
		{
			try
			{
				ivjFrmSpecialRegistrationREG002ContentPane1 =
					new JPanel();
				ivjFrmSpecialRegistrationREG002ContentPane1.setName(
					"ivjFrmSpecialRegistrationREG002ContentPane1");
				ivjFrmSpecialRegistrationREG002ContentPane1.setLayout(
					null);
				ivjFrmSpecialRegistrationREG002ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmSpecialRegistrationREG002ContentPane1
					.setMinimumSize(
					new Dimension(586, 458));
				getFrmSpecialRegistrationREG002ContentPane1().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
				getFrmSpecialRegistrationREG002ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getFrmSpecialRegistrationREG002ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmSpecialRegistrationREG002ContentPane1().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmSpecialRegistrationREG002ContentPane1().add(
					getJPanel4(),
					getJPanel4().getName());
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
		return ivjFrmSpecialRegistrationREG002ContentPane1;
	}

	/**
	 * Return the ivjJPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setLayout(new GridBagLayout());
				ivjJPanel2.setBounds(36, 119, 254, 140);

				GridBagConstraints constraintsstcLblOwnerId =
					new GridBagConstraints();
				constraintsstcLblOwnerId.gridx = 1;
				constraintsstcLblOwnerId.gridy = 2;
				constraintsstcLblOwnerId.gridheight = 2;
				constraintsstcLblOwnerId.ipadx = 40;
				constraintsstcLblOwnerId.insets =
					new Insets(2, 5, 2, 19);
				getJPanel2().add(
					getstcLblOwnerId(),
					constraintsstcLblOwnerId);

				GridBagConstraints constraintslblName1 =
					new GridBagConstraints();
				constraintslblName1.gridx = 1;
				constraintslblName1.gridy = 4;
				constraintslblName1.gridwidth = 2;
				constraintslblName1.ipadx = 150;
				constraintslblName1.insets = new Insets(2, 5, 2, 9);
				getJPanel2().add(getlblName1(), constraintslblName1);

				GridBagConstraints constraintslblPlateNo =
					new GridBagConstraints();
				constraintslblPlateNo.gridx = 2;
				constraintslblPlateNo.gridy = 1;
				constraintslblPlateNo.gridheight = 2;
				constraintslblPlateNo.ipadx = 43;
				constraintslblPlateNo.ipady = 1;
				constraintslblPlateNo.insets = new Insets(8, 20, 0, 24);
				getJPanel2().add(
					getlblPlateNo(),
					constraintslblPlateNo);

				GridBagConstraints constraintslblOwnerID =
					new GridBagConstraints();
				constraintslblOwnerID.gridx = 2;
				constraintslblOwnerID.gridy = 3;
				constraintslblOwnerID.ipadx = 53;
				constraintslblOwnerID.ipady = -2;
				constraintslblOwnerID.insets = new Insets(0, 20, 3, 14);
				getJPanel2().add(
					getlblOwnerID(),
					constraintslblOwnerID);

				GridBagConstraints constraintsstcLblPlateNo =
					new GridBagConstraints();
				constraintsstcLblPlateNo.gridx = 1;
				constraintsstcLblPlateNo.gridy = 1;
				constraintsstcLblPlateNo.ipadx = 44;
				constraintsstcLblPlateNo.insets =
					new Insets(5, 5, 1, 19);
				getJPanel2().add(
					getstcLblPlateNo(),
					constraintsstcLblPlateNo);

				GridBagConstraints constraintslblAddress1 =
					new GridBagConstraints();
				constraintslblAddress1.gridx = 1;
				constraintslblAddress1.gridy = 6;
				constraintslblAddress1.gridwidth = 2;
				constraintslblAddress1.ipadx = 120;
				constraintslblAddress1.insets = new Insets(2, 5, 5, 9);
				getJPanel2().add(
					getlblAddress1(),
					constraintslblAddress1);

				GridBagConstraints constraintslblAddress2 =
					new GridBagConstraints();
				constraintslblAddress2.gridx = 1;
				constraintslblAddress2.gridy = 7;
				constraintslblAddress2.gridwidth = 2;
				constraintslblAddress2.ipadx = 120;
				constraintslblAddress2.insets = new Insets(5, 5, 2, 9);
				getJPanel2().add(
					getlblAddress2(),
					constraintslblAddress2);

				GridBagConstraints constraintslblCityStateZip =
					new GridBagConstraints();
				constraintslblCityStateZip.gridx = 1;
				constraintslblCityStateZip.gridy = 8;
				constraintslblCityStateZip.gridwidth = 2;
				constraintslblCityStateZip.ipadx = 120;
				constraintslblCityStateZip.ipady = 3;
				constraintslblCityStateZip.insets =
					new Insets(3, 5, 4, 9);
				getJPanel2().add(
					getlblCityStateZip(),
					constraintslblCityStateZip);

				GridBagConstraints constraintslblName2 =
					new GridBagConstraints();
				constraintslblName2.gridx = 1;
				constraintslblName2.gridy = 5;
				constraintslblName2.gridwidth = 2;
				constraintslblName2.ipadx = 150;
				constraintslblName2.insets = new Insets(2, 5, 2, 9);
				getJPanel2().add(getlblName2(), constraintslblName2);
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
		return ivjJPanel2;
	}

	/**
	 * Return the ivjJPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(311, 120, 231, 129);
				getJPanel3().add(
					getlblExpDate(),
					getlblExpDate().getName());
				getJPanel3().add(
					getstcExpDate(),
					getstcExpDate().getName());
				getJPanel3().add(
					getstcLblEffDate(),
					getstcLblEffDate().getName());
				getJPanel3().add(
					getstcLblIssueDate(),
					getstcLblIssueDate().getName());
				getJPanel3().add(
					getlblEffDate(),
					getlblEffDate().getName());
				getJPanel3().add(
					getlblIssueDate(),
					getlblIssueDate().getName());
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
		return ivjJPanel3;
	}

	/**
	 * Return the ivjJPanel4 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new JPanel();
				ivjJPanel4.setName("ivjJPanel4");
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setBounds(36, 261, 502, 125);

				ivjJPanel4.add(getstcLblRegClass(), null);
				ivjJPanel4.add(getstcLblPlateType(), null);
				ivjJPanel4.add(getlblRegClass(), null);
				ivjJPanel4.add(getlblPlateType(), null);
				ivjJPanel4.add(getstcLblCancelled(), null);
				ivjJPanel4.add(getlblVIN(), null);
				ivjJPanel4.add(getstcLblCancelledDocNo(), null);
				ivjJPanel4.add(getlblDocNo(), null);
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
		return ivjJPanel4;
	}

	/**
	 * Return the JScrollPane1 property value.
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
				ivjJScrollPane1.setBounds(36, 29, 506, 79);
				getJScrollPane1().setViewportView(getScrollPaneTable());
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
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjlblAddress1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress1()
	{
		if (ivjlblAddress1 == null)
		{
			try
			{
				ivjlblAddress1 = new JLabel();
				ivjlblAddress1.setName("ivjlblAddress1");
				// defect 10112 
				ivjlblAddress1.setText(DFLT_OWNR_ST1);
				// end defect 10112
				ivjlblAddress1.setMaximumSize(new Dimension(120, 14));
				ivjlblAddress1.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblAddress1.setVerticalTextPosition(
					SwingConstants.TOP);
				ivjlblAddress1.setPreferredSize(new Dimension(120, 14));
				ivjlblAddress1.setVerticalAlignment(SwingConstants.TOP);
				ivjlblAddress1.setMinimumSize(new Dimension(120, 14));
				ivjlblAddress1.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblAddress1;
	}

	/**
	 * Return the ivjlblAddress2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress2()
	{
		if (ivjlblAddress2 == null)
		{
			try
			{
				ivjlblAddress2 = new JLabel();
				ivjlblAddress2.setName("ivjlblAddress2");
				// defect 10112
				ivjlblAddress2.setText(DFLT_OWNR_ST2);
				// end defect 10112 
				ivjlblAddress2.setMaximumSize(new Dimension(120, 14));
				ivjlblAddress2.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblAddress2.setPreferredSize(new Dimension(120, 14));
				ivjlblAddress2.setMinimumSize(new Dimension(120, 14));
				ivjlblAddress2.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblAddress2;
	}

	/**
	 * Return the ivjlblCityStateZip property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCityStateZip()
	{
		if (ivjlblCityStateZip == null)
		{
			try
			{
				ivjlblCityStateZip = new JLabel();
				ivjlblCityStateZip.setName("lblCityStateZip");
				// defect 10112 
				ivjlblCityStateZip.setText(DFLT_OWNR_CITY_ST_ZIP);
				// end defect 10112 
				ivjlblCityStateZip.setMaximumSize(
					new Dimension(120, 14));
				ivjlblCityStateZip.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblCityStateZip.setVerticalTextPosition(
					SwingConstants.BOTTOM);
				ivjlblCityStateZip.setPreferredSize(
					new Dimension(120, 14));
				ivjlblCityStateZip.setVerticalAlignment(
					SwingConstants.BOTTOM);
				ivjlblCityStateZip.setMinimumSize(
					new Dimension(120, 14));
				ivjlblCityStateZip.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjlblCityStateZip.setCursor(
					new Cursor(Cursor.S_RESIZE_CURSOR));
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
		return ivjlblCityStateZip;
	}

	/**
	 * This method initializes ivjlblDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocNo()
	{
		if (ivjlblDocNo == null)
		{
			ivjlblDocNo = new JLabel();
			ivjlblDocNo.setBounds(176, 92, 154, 20);
			ivjlblDocNo.setText("DOCNO");
		}
		return ivjlblDocNo;
	}

	/**
	 * Return the ivjlblEffDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEffDate()
	{
		if (ivjlblEffDate == null)
		{
			try
			{
				ivjlblEffDate = new JLabel();
				ivjlblEffDate.setName("ivjlblEffDate");
				ivjlblEffDate.setText(CommonConstant.STR_ZERO);
				ivjlblEffDate.setMaximumSize(new Dimension(48, 14));
				ivjlblEffDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblEffDate.setPreferredSize(new Dimension(20, 14));
				ivjlblEffDate.setBounds(142, 25, 62, 14);
				ivjlblEffDate.setMinimumSize(new Dimension(48, 14));
				ivjlblEffDate.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblEffDate;
	}

	/**
	 * Return the ivjlblExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpDate()
	{
		if (ivjlblExpDate == null)
		{
			try
			{
				ivjlblExpDate = new JLabel();
				ivjlblExpDate.setName("ivjlblExpDate");
				ivjlblExpDate.setText(CommonConstant.STR_ZERO);
				ivjlblExpDate.setMaximumSize(new Dimension(48, 14));
				ivjlblExpDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblExpDate.setPreferredSize(new Dimension(20, 14));
				ivjlblExpDate.setVerticalAlignment(SwingConstants.TOP);
				ivjlblExpDate.setBounds(142, 6, 62, 14);
				ivjlblExpDate.setMinimumSize(new Dimension(48, 14));
				ivjlblExpDate.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblExpDate;
	}

	/**
	 * Return the ivjlblIssueDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssueDate()
	{
		if (ivjlblIssueDate == null)
		{
			try
			{
				ivjlblIssueDate = new JLabel();
				ivjlblIssueDate.setName("ivjlblIssueDate");
				ivjlblIssueDate.setText(DFLT_DATE);
				ivjlblIssueDate.setMaximumSize(new Dimension(48, 14));
				ivjlblIssueDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblIssueDate.setVerticalTextPosition(
					SwingConstants.BOTTOM);
				ivjlblIssueDate.setPreferredSize(new Dimension(20, 14));
				ivjlblIssueDate.setVerticalAlignment(
					SwingConstants.BOTTOM);
				ivjlblIssueDate.setBounds(142, 44, 62, 14);
				ivjlblIssueDate.setMinimumSize(new Dimension(48, 14));
				ivjlblIssueDate.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblIssueDate;
	}

	/**
	 * Return the ivjlblName1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblName1()
	{
		if (ivjlblName1 == null)
		{
			try
			{
				ivjlblName1 = new JLabel();
				ivjlblName1.setName("ivjlblName1");
				ivjlblName1.setText(DFLT_OWNR_NM1);
				ivjlblName1.setMaximumSize(new Dimension(90, 14));
				ivjlblName1.setPreferredSize(new Dimension(90, 14));
				ivjlblName1.setFont(new Font("Arial", 1, 12));
				ivjlblName1.setMinimumSize(new Dimension(90, 14));
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
		return ivjlblName1;
	}

	/**
	 * Return the ivjlblName2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblName2()
	{
		if (ivjlblName2 == null)
		{
			try
			{
				ivjlblName2 = new JLabel();
				ivjlblName2.setName("ivjlblName2");
				ivjlblName2.setText(DFLT_OWNR_NM2);
				ivjlblName2.setMaximumSize(new Dimension(90, 14));
				ivjlblName2.setPreferredSize(new Dimension(90, 14));
				ivjlblName2.setFont(new Font("Arial", 1, 12));
				ivjlblName2.setMinimumSize(new Dimension(90, 14));
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
		return ivjlblName2;
	}

	/**
	 * Return the ivjlblOwnerID property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOwnerID()
	{
		if (ivjlblOwnerID == null)
		{
			try
			{
				ivjlblOwnerID = new JLabel();
				ivjlblOwnerID.setName("ivjlblOwnerID");
				ivjlblOwnerID.setText(CommonConstant.STR_ZERO);
				ivjlblOwnerID.setMaximumSize(new Dimension(49, 14));
				ivjlblOwnerID.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblOwnerID.setMinimumSize(new Dimension(49, 14));
				ivjlblOwnerID.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblOwnerID;
	}

	/**
	 * Return the ivjlblPlateNo property value.
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
				ivjlblPlateNo.setName("ivjlblPlateNo");
				ivjlblPlateNo.setText(CommonConstant.STR_ZERO);
				ivjlblPlateNo.setMaximumSize(new Dimension(49, 14));
				ivjlblPlateNo.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblPlateNo.setVerticalTextPosition(
					SwingConstants.TOP);
				ivjlblPlateNo.setVerticalAlignment(SwingConstants.TOP);
				ivjlblPlateNo.setMinimumSize(new Dimension(49, 14));
				ivjlblPlateNo.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblPlateNo;
	}

	/**
	 * Return the ivjlblPlateType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateType()
	{
		if (ivjlblPlateType == null)
		{
			try
			{
				ivjlblPlateType = new JLabel();
				ivjlblPlateType.setBounds(112, 40, 52, 17);
				ivjlblPlateType.setName("ivjlblPlateType");
				ivjlblPlateType.setText(DFLT_CHARS6);
				ivjlblPlateType.setMaximumSize(new Dimension(46, 14));
				ivjlblPlateType.setMinimumSize(new Dimension(46, 14));
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
		return ivjlblPlateType;
	}

	/**
	 * Return the ivjlblRegClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegClass()
	{
		if (ivjlblRegClass == null)
		{
			try
			{
				ivjlblRegClass = new JLabel();
				ivjlblRegClass.setBounds(112, 21, 370, 14);
				ivjlblRegClass.setName("ivjlblRegClass");
				ivjlblRegClass.setText(DFLT_DRVED);
				ivjlblRegClass.setMaximumSize(new Dimension(122, 14));
				ivjlblRegClass.setVerticalTextPosition(
					SwingConstants.TOP);
				ivjlblRegClass.setVerticalAlignment(SwingConstants.TOP);
				ivjlblRegClass.setMinimumSize(new Dimension(122, 14));
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
		return ivjlblRegClass;
	}

	/**
	 * Return the ivjlblVIN property value.
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
				ivjlblVIN.setSize(154, 20);
				ivjlblVIN.setName("ivjlblVIN");
				ivjlblVIN.setText(DFLT_VIN);
				ivjlblVIN.setMaximumSize(new Dimension(46, 14));
				ivjlblVIN.setVerticalTextPosition(
					SwingConstants.BOTTOM);
				ivjlblVIN.setVerticalAlignment(SwingConstants.BOTTOM);
				ivjlblVIN.setMinimumSize(new Dimension(46, 14));
				ivjlblVIN.setLocation(176, 68);
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
		return ivjlblVIN;
	}

	/**
	 * Return the ivjScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ivjScrollPaneTable");
				getJScrollPane1().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7885
				ivjScrollPaneTable.setModel(
					new com.txdot.isd.rts.client.common.ui.TMREG002());
				ivjScrollPaneTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel = (TMREG002) ivjScrollPaneTable.getModel();
				
				// defect 10112 
				TableColumn laOwnerName =
					ivjScrollPaneTable
						.getColumn(ivjScrollPaneTable.getColumnName(
						//0));
						CommonConstant.REG002_COL_OWNRNAME));

				TableColumn laPlateNo =
					ivjScrollPaneTable
						.getColumn(ivjScrollPaneTable.getColumnName(
						//1));
						CommonConstant.REG002_COL_PLTNO));
				// end defect 10112 
				
				laOwnerName.setPreferredWidth(300);
				laPlateNo.setPreferredWidth(200);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				laOwnerName.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				laPlateNo.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
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
		return ivjScrollPaneTable;
	}

	/**
	 * Return the ivjstcExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcExpDate()
	{
		if (ivjstcExpDate == null)
		{
			try
			{
				ivjstcExpDate = new JLabel();
				ivjstcExpDate.setName("ivjstcExpDate");
				ivjstcExpDate.setText(TXT_EXP_DT);
				ivjstcExpDate.setMaximumSize(new Dimension(50, 14));
				ivjstcExpDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcExpDate.setPreferredSize(new Dimension(50, 14));
				ivjstcExpDate.setVerticalAlignment(SwingConstants.TOP);
				ivjstcExpDate.setBounds(42, 6, 88, 14);
				ivjstcExpDate.setMinimumSize(new Dimension(50, 14));
				ivjstcExpDate.setHorizontalAlignment(
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
		return ivjstcExpDate;
	}

	/**
	 * Return the ivjstcLblCancelled property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCancelled()
	{
		if (ivjstcLblCancelled == null)
		{
			try
			{
				ivjstcLblCancelled = new JLabel();
				ivjstcLblCancelled.setSize(161, 20);
				ivjstcLblCancelled.setName("ivjstcLblCancelled");
				// defect 7143
				ivjstcLblCancelled.setText("Canceled Plate VIN: ");
				// end defect 7143 
				ivjstcLblCancelled.setMaximumSize(
					new Dimension(62, 14));
				ivjstcLblCancelled.setVerticalTextPosition(
					SwingConstants.BOTTOM);
				ivjstcLblCancelled.setVerticalAlignment(
					SwingConstants.BOTTOM);
				ivjstcLblCancelled.setMinimumSize(
					new Dimension(62, 14));
				// user code begin {1}
				ivjstcLblCancelled.setLocation(7, 68);
				// empty code block
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// empty code block
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCancelled;
	}

	/**
	 * This method initializes ivjstcLblCancelledDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCancelledDocNo()
	{
		if (ivjstcLblCancelledDocNo == null)
		{
			ivjstcLblCancelledDocNo = new JLabel();
			ivjstcLblCancelledDocNo.setSize(160, 20);
			ivjstcLblCancelledDocNo.setText("Canceled Plate Doc No: ");
			ivjstcLblCancelledDocNo.setLocation(7, 92);
		}
		return ivjstcLblCancelledDocNo;
	}

	/**
	 * Return the ivjstcLblEffDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEffDate()
	{
		if (ivjstcLblEffDate == null)
		{
			try
			{
				ivjstcLblEffDate = new JLabel();
				ivjstcLblEffDate.setName("ivjstcLblEffDate");
				ivjstcLblEffDate.setText(TXT_EFF_DT);
				ivjstcLblEffDate.setMaximumSize(new Dimension(50, 14));
				ivjstcLblEffDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblEffDate.setPreferredSize(
					new Dimension(50, 14));
				ivjstcLblEffDate.setBounds(42, 25, 88, 14);
				ivjstcLblEffDate.setMinimumSize(new Dimension(50, 14));
				ivjstcLblEffDate.setHorizontalAlignment(
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
		return ivjstcLblEffDate;
	}

	/**
	 * Return the ivjstcLblIssueDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIssueDate()
	{
		if (ivjstcLblIssueDate == null)
		{
			try
			{
				ivjstcLblIssueDate = new JLabel();
				ivjstcLblIssueDate.setName("ivjstcLblIssueDate");
				ivjstcLblIssueDate.setText(TXT_ISS_DT);
				ivjstcLblIssueDate.setMaximumSize(
					new Dimension(50, 14));
				ivjstcLblIssueDate.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblIssueDate.setVerticalTextPosition(
					SwingConstants.BOTTOM);
				ivjstcLblIssueDate.setPreferredSize(
					new Dimension(50, 14));
				ivjstcLblIssueDate.setVerticalAlignment(
					SwingConstants.BOTTOM);
				ivjstcLblIssueDate.setBounds(42, 44, 88, 14);
				ivjstcLblIssueDate.setMinimumSize(
					new Dimension(50, 14));
				ivjstcLblIssueDate.setHorizontalAlignment(
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
		return ivjstcLblIssueDate;
	}

	/**
	 * Return the ivjstcLblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new JLabel();
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setText(TXT_OWN_ID);
				ivjstcLblOwnerId.setMaximumSize(new Dimension(54, 14));
				ivjstcLblOwnerId.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblOwnerId.setMinimumSize(new Dimension(54, 14));
				ivjstcLblOwnerId.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjstcLblOwnerId;
	}

	/**
	 * Return the ivjstcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setName("ivjstcLblPlateNo");
				ivjstcLblPlateNo.setText(TXT_PLT_NO);
				ivjstcLblPlateNo.setMaximumSize(new Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblPlateNo.setVerticalTextPosition(
					SwingConstants.TOP);
				ivjstcLblPlateNo.setVerticalAlignment(
					SwingConstants.TOP);
				ivjstcLblPlateNo.setMinimumSize(new Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the ivjstcLblPlateType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateType()
	{
		if (ivjstcLblPlateType == null)
		{
			try
			{
				ivjstcLblPlateType = new JLabel();
				ivjstcLblPlateType.setSize(76, 20);
				ivjstcLblPlateType.setName("ivjstcLblPlateType");
				ivjstcLblPlateType.setText(TXT_PLT_TYPE);
				ivjstcLblPlateType.setMaximumSize(
					new Dimension(62, 14));
				ivjstcLblPlateType.setMinimumSize(
					new Dimension(62, 14));
				ivjstcLblPlateType.setLocation(7, 44);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// empty code block
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPlateType;
	}

	/**
	 * Return the ivjstcLblRegClass property value.
	 * @return JLabel
	 */
	private JLabel getstcLblRegClass()
	{
		if (ivjstcLblRegClass == null)
		{
			try
			{
				ivjstcLblRegClass = new JLabel();
				ivjstcLblRegClass.setSize(76, 20);
				ivjstcLblRegClass.setName("ivjstcLblRegClass");
				ivjstcLblRegClass.setText(TXT_REG_CLS);
				ivjstcLblRegClass.setMaximumSize(new Dimension(60, 14));
				ivjstcLblRegClass.setVerticalTextPosition(
					SwingConstants.TOP);
				ivjstcLblRegClass.setVerticalAlignment(
					SwingConstants.TOP);
				ivjstcLblRegClass.setMinimumSize(new Dimension(60, 14));
				ivjstcLblRegClass.setLocation(7, 21);
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
		return ivjstcLblRegClass;
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
			setName(FRM_NAME_REG002);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(608, 474);
			setTitle(FRM_TITLE_REG002);
			setContentPane(
				getFrmSpecialRegistrationREG002ContentPane1());
			// user code end
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Get VehicleInquiryData object to populate screen.
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		caVehData = (VehicleInquiryData) aaDataObject;
		csTransCd = getController().getTransCode();
		String lsCityStZip;
		RTSDate laEffDt;
		RTSDate laIssDt;
		int liRTSEffDt;

		if (csTransCd.equals(TransCdConstant.VEHINQ))
		{
			//Set field values
			ivjlblPlateNo.setText(
				caVehData
					.getMfVehicleData()
					.getRegData()
					.getRegPltNo());
			// defect 7143 
			// KEY001 does not provide access to Cancelled Stickers			
			if (caVehData
				.getMfVehicleData()
				.getRegData()
				.getCancPltIndi()
				== 1)
			{
				ivjstcLblOwnerId.setVisible(false);
				ivjlblOwnerID.setVisible(false);
				ivjlblName1.setVisible(false);
				ivjlblName2.setVisible(false);
				ivjstcLblIssueDate.setVisible(false);
				ivjlblIssueDate.setVisible(false);
				ivjlblAddress1.setVisible(false);
				ivjlblAddress2.setVisible(false);
				ivjlblCityStateZip.setVisible(false);
				ivjstcLblRegClass.setVisible(false);
				ivjlblRegClass.setVisible(false);
				ivjstcLblPlateType.setVisible(false);
				ivjlblPlateType.setVisible(false);
				ivjlblVIN.setText(
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getCancPltVin());

				laEffDt =
					new RTSDate(
						1,
						caVehData
							.getMfVehicleData()
							.getRegData()
							.getCanclPltDt());
				ivjlblEffDate.setText(laEffDt.toString());

				ivjlblExpDate.setText(
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getCancRegExpMo()
						+ CommonConstant.STR_SLASH);

				// defect 9641 
				getlblDocNo().setText(
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getCancPltDocNo());
				// end defect 9641 
			}
			else
			{
				// defect 9641 
				getstcLblCancelledDocNo().setVisible(false);
				getlblDocNo().setVisible(false);
				// end defect 9641  
				ivjstcLblCancelled.setVisible(false);
				ivjlblVIN.setVisible(false);
				ivjlblOwnerID.setText(
					caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getOwnrId());
				ivjlblName1.setText(
					caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getName1());
				if (caVehData
					.getMfVehicleData()
					.getOwnerData()
					.getName2()
					!= null
					&& !caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getName2()
						.trim()
						.equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					ivjlblName2.setText(
						caVehData
							.getMfVehicleData()
							.getOwnerData()
							.getName2());
				}
				else
				{
					ivjlblName2.setVisible(false);
				}

				ivjlblAddress1.setText(
					caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getAddressData()
						.getSt1());
				if (caVehData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getSt2()
					!= null
					&& !caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getAddressData()
						.getSt2()
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					ivjlblAddress2.setText(
						caVehData
							.getMfVehicleData()
							.getOwnerData()
							.getAddressData()
							.getSt2());
				}
				else
				{
					ivjlblAddress2.setVisible(false);
				}
				lsCityStZip =
					caVehData
						.getMfVehicleData()
						.getOwnerData()
						.getAddressData()
						.getCity()
						+ ", "
						+ caVehData
							.getMfVehicleData()
							.getOwnerData()
							.getAddressData()
							.getState()
						+ CommonConstant.STR_SPACE_TWO
						+ caVehData
							.getMfVehicleData()
							.getOwnerData()
							.getAddressData()
							.getZpcd()
						+ CommonConstant.STR_DASH
						+ caVehData
							.getMfVehicleData()
							.getOwnerData()
							.getAddressData()
							.getZpcdp4();
				ivjlblCityStateZip.setText(lsCityStZip);
				ivjlblPlateType.setText(
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd());
				if (caVehData
					.getMfVehicleData()
					.getRegData()
					.getRegClassCd()
					!= 0)
				{
					liRTSEffDt =
						RTSDate.getCurrentDate().getYYYYMMDDDate();
					CommonFeesData lRegData =
						CommonFeesCache.getCommonFee(
							caVehData
								.getMfVehicleData()
								.getRegData()
								.getRegClassCd(),
							liRTSEffDt);
					ivjlblRegClass.setText(
						lRegData.getRegClassCdDesc());
				}
				laEffDt =
					new RTSDate(
						1,
						caVehData
							.getMfVehicleData()
							.getRegData()
							.getRegEffDt());
				ivjlblEffDate.setText(laEffDt.toString());
				laIssDt =
					new RTSDate(
						1,
						caVehData
							.getMfVehicleData()
							.getRegData()
							.getRegIssueDt());
				ivjlblIssueDate.setText(laIssDt.toString());

				// defect 8900
				String lsRegExpMoYr =
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getRegExpMo()
						+ CommonConstant.STR_SLASH;
				if (lsRegExpMoYr.equals("0/"))
				{
					lsRegExpMoYr = " /";
				}
				int liRegExpMoYr =
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getRegExpYr();

				if (liRegExpMoYr != 0)
				{
					lsRegExpMoYr = lsRegExpMoYr + liRegExpMoYr;
				}
				ivjlblExpDate.setText(lsRegExpMoYr);
				// end defect 8900 
			}
			// end defect 7143 
		}
		else
		{
			ivjlblAddress1.setVisible(false);
			ivjlblAddress2.setVisible(false);
			ivjlblCityStateZip.setVisible(false);
		}

		caTableModel.add(caVehData);
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
