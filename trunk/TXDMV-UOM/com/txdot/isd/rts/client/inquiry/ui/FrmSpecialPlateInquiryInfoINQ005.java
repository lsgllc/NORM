package com.txdot.isd.rts.client.inquiry.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InquiryConstants;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * FrmSpecialPlateInquiryInfoINQ005.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson	02/20/2007	Created
 * 							defect 9123 Ver Special Plates
 * K Harrell	03/27/2007	If all zeros, replace Phone No and OwnerId 
 * 							with empty string. Remove Effective Date.
 * 							Interpret MfgStatusCd 
 * 							delete ivjstcLblRegEffDt,getstcLblRegEffDt()
 * 							modify setData() 	
 * 							defect 9085 Ver Special Plates 
 * J Rue		04/05/2007	Add button panel.
 * 							Add Enter and Cancel functions
 * 							add getButtonPanel()
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/19/2007	Repositioned ECH Button Group.
 * 							Extended Screen to accommodate larger 
 * 							 e-mail address. Renamed label from 
 * 							 "Date" to "Mfg Request Date", moved to 
 * 							 next line. Correct Cancel function when 
 * 							 attached to Vehicle Record. Reformatted
 * 							 phone no to be consistent with input. 
 * 							delete caOrigVehInqData,caMFVehicleData
 * 							delete getOrigVehInqData() 
 * 							modify setData(), actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Set Exp Mo/Yr to " / " if 0/0
 * 							Do not print "," if no City
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Added temporary text for Help
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/25/2007	Add "InvItmYr" for annual Plate
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/08/2007	Focus not on Enter upon window activation
 * 							as gettxtOwnerInfo() was enabled. Added
 * 							disabled color.
 * 							modify gettxtOwnerInfo()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	01/07/2009	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setData()  
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	07/12/2009	Implement new Owner/Address methods
 * 							delete COMMA_SPACE	
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/10/2009	Naming Standardization for text area 
 * 							delete ivjtxtOwnerInfo, gettxtOwnerInfo()
 * 							add ivjtxtAOwnerInfo, gettxtAOwnerInfo()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	04/11/2010	modify Frame via Visual Editor
 * 							add ivjstcLblAuctionPrice,ivjlblAuctionPrice,
 * 							 ivjstcLblTerm,ivjlblTerm,ivjJPaneHQOnly,
 * 							 ivjstcLblResrvDate,ivjlblResrvDate,
 *							 ivjstcLblResrvReasn,ivjlblResrvReasnDesc,
 *							 ivjstcLblMarketingAllowed,ivjstcLblVendorDate,
 *							 ivjlblVendorTransDate,ivjstcLblFINDocNo,
 *							 ivjlblFINDocNo,ivjlblMarketingAllowedYorN, 
 *							 ivjJPaneHQOnly, ivjlblMarketingAllowedYorN, 
 *							 get methods. 
 *							add caSpclPltRegisData
 *							add setupHQOnlyFields() 
 *							delete KeyListener, keyReleased() 
 *							modify setData(), getJPanel3()
 *							  getFrmSpecialPlatesInfoINQ005ContentPane(), 
 *							  getlblPltAge()
 *							defect 10441 Ver POS_640 
 * K Harrell	11/14/2011	Implement VTR 275
 * 							modify actionPerformed()
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/13/2011	Set CTData NoMFRecs for HQ 
 * 							modify actionPerformed()
 * 							defect 11169 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * Class for screen INQ005. Displays Special Plate Info.
 *
 * @version	6.9.0 			12/13/2011	
 * @author	Todd Pederson
 * <br>Creation Date:		02/20/2007 11:32:38
 */
public class FrmSpecialPlateInquiryInfoINQ005
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjFrmSpecialPlatesInfoINQ005ContentPane = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JLabel ivjstcLblMfgPltNo = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblOrg = null;
	private JLabel ivjstcLblPltAge = null;
	private JLabel ivjstcLblPltNo = null;
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjstcLblStatus = null;
	private JLabel ivjstcLblPlateType = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjlblPltAge = null;
	private JLabel ivjlblPltNo = null;
	private JLabel ivjlblPltType = null;
	private JLabel ivjlblOwnrId = null;
	private JLabel ivjlblOrg = null;
	private JLabel ivjlblStatus = null;
	private JLabel ivjlblEMail = null;
	private JLabel ivjlblPhoneNo = null;
	private JLabel ivjlblMfgPltNo = null;
	private JLabel ivjstcLblMfgDate = null;
	private JLabel ivjlblMfgDate = null;
	private JLabel ivjstcLblApplDate = null;
	private JLabel ivjlblApplDate = null;
	private JLabel ivjstcLblExpDate = null;
	private JLabel ivjlblExpDate = null;
	private JLabel ivjstcLblCntyOfRes = null;
	private JLabel ivjlblCntyOfRes = null;
	private JTextArea ivjtxtAOwnerInfo = null;
	private JLabel ivjstcLblAddlSetOrdered = null;
	private JLabel ivjstcLblDealerNo = null;
	private JLabel ivjlblDealerNo = null;
	private ButtonPanel ivjButtonPanel1 = null;

	// Objects
	private VehicleInquiryData caVehicleInquiryData = null;

	// defect 10441 
	private SpecialPlatesRegisData caSpclPltRegisData = null;

	private JLabel ivjstcLblAuctionPrice = null;
	private JLabel ivjlblAuctionPrice = null;
	private JLabel ivjstcLblTerm = null;
	private JLabel ivjlblTerm = null;
	private JPanel ivjJPaneHQOnly = null;
	private JLabel ivjstcLblResrvDate = null;
	private JLabel ivjlblResrvDate = null;
	private JLabel ivjstcLblResrvReasn = null;
	private JLabel ivjlblResrvReasnDesc = null;
	private JLabel ivjstcLblMarketingAllowed = null;
	private JLabel ivjstcLblVendorDate = null;
	private JLabel ivjlblVendorTransDate = null;
	private JLabel ivjstcLblFINDocNo = null;
	private JLabel ivjlblFINDocNo = null;
	private JLabel ivjlblMarketingAllowedYorN = null;
	// end defect 10441 

	/**
	 * FrmSpecialPlateInquiryInfoINQ005 constructor.
	 */
	public FrmSpecialPlateInquiryInfoINQ005()
	{
		super();
		initialize();
	}
	/**
	 * FrmSpecialPlateInquiryInfoINQ005 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmSpecialPlateInquiryInfoINQ005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmSpecialPlateInquiryInfoINQ005 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateInquiryInfoINQ005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs. Pass the call to controller
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
			// If Not Special Plates Only, treat Enter as Cancel 
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel()
				|| (aaAE.getSource() == getButtonPanel1().getBtnEnter()
					&& !caVehicleInquiryData
						.getMfVehicleData()
						.isSPRecordOnlyVehInq()))
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				boolean lbHQ = SystemProperty.isHQ();

				// defect 11052
				if (getController().getTransCode().equals(TransCdConstant.VEHINQ)) 
				{
					int liPrintOption = caVehicleInquiryData.getPrintOptions();

					if (!lbHQ && liPrintOption != VehicleInquiryData.VIEW_ONLY)
					{
						// end defect 11052 
						getController().processData(
								VCSpecialPlateInquiryInfoINQ005.INQ007,
								caVehicleInquiryData);
					}
					else
					{
						CompleteTransactionData laCompTransData =
							new CompleteTransactionData();
						laCompTransData.setOrgVehicleInfo(
								caVehicleInquiryData.getMfVehicleData());
						laCompTransData.setVehicleInfo(
								caVehicleInquiryData.getMfVehicleData());
						
						// defect 11052 
						laCompTransData.setTransCode(
								//getController().getTransCode());
								UtilityMethods.getVehInqTransCd(liPrintOption));
						laCompTransData.setPrintOptions(liPrintOption);
						// end 11052
						
						// defect 11169 
						laCompTransData.setNoMFRecs(caVehicleInquiryData.getNoMFRecs()); 
						// end defect 11169 
						
						getController().processData(
								AbstractViewController.ENTER,
								laCompTransData);
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
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setSize(216, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(255, 367);
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
	 * Return the FrmSpecialPlatesInfoINQ005ContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSpecialPlatesInfoINQ005ContentPane()
	{
		if (ivjFrmSpecialPlatesInfoINQ005ContentPane == null)
		{
			try
			{
				ivjFrmSpecialPlatesInfoINQ005ContentPane = new JPanel();
				ivjFrmSpecialPlatesInfoINQ005ContentPane.setName(
					"FrmSpecialPlatesInfoINQ005ContentPane1");
				ivjFrmSpecialPlatesInfoINQ005ContentPane.setLayout(
					null);
				ivjFrmSpecialPlatesInfoINQ005ContentPane
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmSpecialPlatesInfoINQ005ContentPane
					.setMinimumSize(
					new Dimension(588, 386));
				getFrmSpecialPlatesInfoINQ005ContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSpecialPlatesInfoINQ005ContentPane().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmSpecialPlatesInfoINQ005ContentPane().add(
					getJPanel3(),
					getJPanel3().getName());

				// defect 10441 
				getFrmSpecialPlatesInfoINQ005ContentPane().add(
					getJPaneHQOnly(),
					getJPaneHQOnly().getName());
				// end defect 10441 
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjFrmSpecialPlatesInfoINQ005ContentPane;
	}
	/**
	 * Return the JPanel2 property value.
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
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_OWNER));
				ivjJPanel2.setLayout(null);
				getJPanel2().add(
					getstcLblOwnerId(),
					getstcLblOwnerId().getName());
				getJPanel2().add(
					getlblOwnrId(),
					getlblOwnrId().getName());
				ivjJPanel2.add(gettxtAOwnerInfo(), null);
				ivjJPanel2.add(getstcLblEMail(), null);
				ivjJPanel2.add(getlblEMail(), null);
				ivjJPanel2.add(getstcLblPhoneNo(), null);
				ivjJPanel2.add(getlblPhoneNo(), null);
				ivjJPanel2.add(getstcLblDealerNo(), null);
				ivjJPanel2.add(getlblDealerNo(), null);
				ivjJPanel2.setSize(330, 257);
				ivjJPanel2.setLocation(380, 18);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel2;
	}
	/**
	 * Return the JPanel3 property value.
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
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InquiryConstants.TXT_SPECIAL_PLATE));
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(15, 18, 360, 257);
				getJPanel3().add(
					getstcLblPltNo(),
					getstcLblPltNo().getName());
				getJPanel3().add(
					getstcLblPltAge(),
					getstcLblPltAge().getName());
				getJPanel3().add(
					getstcLblPlateType(),
					getstcLblPlateType().getName());
				getJPanel3().add(
					getlblPltNo(),
					getlblPltNo().getName());
				getJPanel3().add(
					getlblPltAge(),
					getlblPltAge().getName());
				getJPanel3().add(
					getlblPltType(),
					getlblPltType().getName());
				ivjJPanel3.add(getstcLblOrg(), null);
				ivjJPanel3.add(getlblOrg(), null);
				ivjJPanel3.add(getstcLblStatus(), null);
				ivjJPanel3.add(getlblStatus(), null);
				ivjJPanel3.add(getstcLblMfgPltNo(), null);
				ivjJPanel3.add(getlblMfgPltNo(), null);
				ivjJPanel3.add(getstcLblMfgDate(), null);
				ivjJPanel3.add(getlblMfgDate(), null);
				ivjJPanel3.add(getstcLblApplDate(), null);
				ivjJPanel3.add(getlblApplDate(), null);
				ivjJPanel3.add(getstcLblExpDate(), null);
				ivjJPanel3.add(getlblExpDate(), null);
				ivjJPanel3.add(getstcLblCntyOfRes(), null);
				ivjJPanel3.add(getlblCntyOfRes(), null);
				ivjJPanel3.add(getstcLblAddlSetOrdered(), null);

				// defect 10411 
				ivjJPanel3.add(getstcLblAuctionPrice(), null);
				ivjJPanel3.add(getlblAuctionPrice(), null);
				ivjJPanel3.add(getstcLblTerm(), null);
				ivjJPanel3.add(getlblTerm(), null);
				// end defect 10411 
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel3;
	}
	/**
	 * Return the stcLblMfgPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMfgPltNo()
	{
		if (ivjstcLblMfgPltNo == null)
		{
			try
			{
				ivjstcLblMfgPltNo = new JLabel();
				ivjstcLblMfgPltNo.setBounds(14, 122, 100, 20);
				ivjstcLblMfgPltNo.setName("stcLblMfgPltNo");
				ivjstcLblMfgPltNo.setText("Mfg Plate No:");
				ivjstcLblMfgPltNo.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblMfgPltNo;
	}
	/**
	 * Return the stcLblOwnerId property value.
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
				ivjstcLblOwnerId.setSize(35, 20);
				ivjstcLblOwnerId.setName("stcLblOwnerId");
				ivjstcLblOwnerId.setText("Id:");
				ivjstcLblOwnerId.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblOwnerId.setHorizontalTextPosition(
					SwingConstants.CENTER);
				ivjstcLblOwnerId.setLocation(191, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblOwnerId;
	}
	/**
	 * Return the stcLblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrg()
	{
		if (ivjstcLblOrg == null)
		{
			try
			{
				ivjstcLblOrg = new JLabel();
				ivjstcLblOrg.setSize(88, 20);
				ivjstcLblOrg.setName("stcLblOrg");
				ivjstcLblOrg.setText("Organization:");
				ivjstcLblOrg.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblOrg.setHorizontalAlignment(
					SwingConstants.LEADING);
				ivjstcLblOrg.setHorizontalTextPosition(
					SwingConstants.TRAILING);
				ivjstcLblOrg.setLocation(26, 82);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblOrg;
	}
	/**
	 * Return the lblPltAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPltAge()
	{
		if (ivjlblPltAge == null)
		{
			try
			{
				ivjlblPltAge = new JLabel();
				ivjlblPltAge.setName("lblPltAge");
				ivjlblPltAge.setText(" ");
				// defect 10441
				ivjlblPltAge.setBounds(254, 22, 21, 20);
				ivjlblPltAge.setComponentOrientation(
					java.awt.ComponentOrientation.RIGHT_TO_LEFT);
				// end defect 10441 
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPltAge;
	}
	/**
	 * Return the lblPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPltNo()
	{
		if (ivjlblPltNo == null)
		{
			try
			{
				ivjlblPltNo = new JLabel();
				ivjlblPltNo.setSize(76, 20);
				ivjlblPltNo.setName("lblPltNo");
				ivjlblPltNo.setText("");
				ivjlblPltNo.setLocation(125, 22);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPltNo;
	}
	/**
	 * Return the lblPltType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPltType()
	{
		if (ivjlblPltType == null)
		{
			try
			{
				ivjlblPltType = new JLabel();
				ivjlblPltType.setSize(230, 20);
				ivjlblPltType.setName("lblPltType");
				ivjlblPltType.setText("");
				ivjlblPltType.setLocation(125, 62);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPltType;
	}
	/**
	 * Return the stcLblPltAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPltAge()
	{
		if (ivjstcLblPltAge == null)
		{
			try
			{
				ivjstcLblPltAge = new JLabel();
				ivjstcLblPltAge.setName("stcLblPltAge");
				ivjstcLblPltAge.setText("Age:");
				ivjstcLblPltAge.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPltAge.setBounds(222, 22, 29, 20);
				ivjstcLblPltAge.setEnabled(true);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPltAge;
	}
	/**
	 * Return the stcLblPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPltNo()
	{
		if (ivjstcLblPltNo == null)
		{
			try
			{
				ivjstcLblPltNo = new JLabel();
				ivjstcLblPltNo.setSize(88, 20);
				ivjstcLblPltNo.setName("stcLblPltNo");
				ivjstcLblPltNo.setText("Plate No:");
				ivjstcLblPltNo.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPltNo.setLocation(25, 22);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPltNo;
	}
	/**
	 * Return the lblOwnrId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOwnrId()
	{
		if (ivjlblOwnrId == null)
		{
			try
			{
				ivjlblOwnrId = new JLabel();
				ivjlblOwnrId.setSize(90, 20);
				ivjlblOwnrId.setName("lblOwnrId");
				ivjlblOwnrId.setText("");
				ivjlblOwnrId.setComponentOrientation(
					java.awt.ComponentOrientation.UNKNOWN);
				ivjlblOwnrId.setLocation(233, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblOwnrId;
	}
	/**
	 * Return the lblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOrg()
	{
		if (ivjlblOrg == null)
		{
			try
			{
				ivjlblOrg = new JLabel();
				ivjlblOrg.setSize(230, 20);
				ivjlblOrg.setName("lblOrg");
				ivjlblOrg.setText("");
				ivjlblOrg.setComponentOrientation(
					java.awt.ComponentOrientation.UNKNOWN);
				ivjlblOrg.setLocation(125, 82);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblOrg;
	}
	/**
	 * Return the stcLblEMail property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			try
			{
				ivjstcLblEMail = new JLabel();
				ivjstcLblEMail.setSize(59, 20);
				ivjstcLblEMail.setName("stcLblEMail");
				ivjstcLblEMail.setText("E-Mail:");
				ivjstcLblEMail.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblEMail.setLocation(18, 192);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEMail;
	}
	/**
	 * Return the lblReqType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblStatus()
	{
		if (ivjlblStatus == null)
		{
			try
			{
				ivjlblStatus = new JLabel();
				ivjlblStatus.setSize(131, 20);
				ivjlblStatus.setName("lblStatus");
				ivjlblStatus.setText("");
				ivjlblStatus.setComponentOrientation(
					java.awt.ComponentOrientation.UNKNOWN);
				ivjlblStatus.setLocation(125, 102);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblStatus;
	}
	/**
	 * Return the lblEMail property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEMail()
	{
		if (ivjlblEMail == null)
		{
			try
			{
				ivjlblEMail = new JLabel();
				ivjlblEMail.setSize(234, 20);
				ivjlblEMail.setName("lblEMail");
				ivjlblEMail.setText("");
				ivjlblEMail.setComponentOrientation(
					java.awt.ComponentOrientation.UNKNOWN);
				ivjlblEMail.setLocation(88, 192);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblEMail;
	}
	/**
	 * Return the lblPhoneNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPhoneNo()
	{
		if (ivjlblPhoneNo == null)
		{
			try
			{
				ivjlblPhoneNo = new JLabel();
				ivjlblPhoneNo.setSize(125, 20);
				ivjlblPhoneNo.setName("lblPhoneNo");
				ivjlblPhoneNo.setText("");
				ivjlblPhoneNo.setComponentOrientation(
					java.awt.ComponentOrientation.UNKNOWN);
				ivjlblPhoneNo.setLocation(88, 212);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjlblPhoneNo;
	}
	/**
	 * Return the stcLblSlsTaxPaid property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStatus()
	{
		if (ivjstcLblStatus == null)
		{
			try
			{
				ivjstcLblStatus = new JLabel();
				ivjstcLblStatus.setBounds(24, 102, 90, 20);
				ivjstcLblStatus.setName("stcLblStatus");
				ivjstcLblStatus.setText("Status:");
				ivjstcLblStatus.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblStatus;
	}
	/**
	 * Return the stcLblTireType property value.
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
				ivjstcLblPlateType.setName("stcLblPlateType");
				ivjstcLblPlateType.setText("Plate Type:");
				ivjstcLblPlateType.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPlateType.setBounds(26, 62, 88, 20);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPlateType;
	}
	/**
	 * Return the stcLblTrdIn property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhoneNo()
	{
		if (ivjstcLblPhoneNo == null)
		{
			try
			{
				ivjstcLblPhoneNo = new JLabel();
				ivjstcLblPhoneNo.setSize(59, 20);
				ivjstcLblPhoneNo.setName("stcLblPhoneNo");
				ivjstcLblPhoneNo.setText("Phone No:");
				ivjstcLblPhoneNo.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblPhoneNo.setLocation(18, 212);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPhoneNo;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRE =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRE.displayError(this);
	}
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmSpecialPlateInquiryInfoINQ005");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(728, 437);
			setTitle(InquiryConstants.TITLE_FRM_INQ005);
			setContentPane(getFrmSpecialPlatesInfoINQ005ContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
	}

	// defect 10441 
	//	/**
	//	 *  Process KeyReleasedEvents.
	//	 * 
	//	 * @param aaKE KeyEvent
	//	 */
	//	public void keyReleased(KeyEvent aaKE)
	//	{
	//		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
	//		{
	//			getController().processData(
	//				AbstractViewController.CANCEL,
	//				getController().getData());
	//		}
	//	}
	// end defect 10441 

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
			FrmSpecialPlateInquiryInfoINQ005 laFrmSpecialPlatesInfo;
			laFrmSpecialPlatesInfo =
				new FrmSpecialPlateInquiryInfoINQ005();
			laFrmSpecialPlatesInfo.setModal(true);
			laFrmSpecialPlatesInfo
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSpecialPlatesInfo.show();
			Insets laInsets = laFrmSpecialPlatesInfo.getInsets();
			laFrmSpecialPlatesInfo.setSize(
				laFrmSpecialPlatesInfo.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSpecialPlatesInfo.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSpecialPlatesInfo.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * Return the lblMfgPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblMfgPltNo()
	{
		if (ivjlblMfgPltNo == null)
		{
			ivjlblMfgPltNo = new JLabel();
			ivjlblMfgPltNo.setSize(76, 20);
			ivjlblMfgPltNo.setText("");
			ivjlblMfgPltNo.setComponentOrientation(
				java.awt.ComponentOrientation.UNKNOWN);
			ivjlblMfgPltNo.setName("lblMfgPltNo");
			ivjlblMfgPltNo.setLocation(125, 122);
		}
		return ivjlblMfgPltNo;
	}
	/**
	 * Return the stcLblMfgDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMfgDate()
	{
		if (ivjstcLblMfgDate == null)
		{
			ivjstcLblMfgDate = new JLabel();
			ivjstcLblMfgDate.setBounds(1, 142, 113, 20);
			ivjstcLblMfgDate.setText("Mfg Request Date:");
			ivjstcLblMfgDate.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		}
		return ivjstcLblMfgDate;
	}
	/**
	 * Return the lblMfgDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblMfgDate()
	{
		if (ivjlblMfgDate == null)
		{
			ivjlblMfgDate = new JLabel();
			ivjlblMfgDate.setSize(66, 20);
			ivjlblMfgDate.setText("");
			ivjlblMfgDate.setComponentOrientation(
				java.awt.ComponentOrientation.UNKNOWN);
			ivjlblMfgDate.setName("lblMfgDate");
			ivjlblMfgDate.setLocation(125, 142);
		}
		return ivjlblMfgDate;
	}
	/**
	 * Return the stcLblApplDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblApplDate()
	{
		if (ivjstcLblApplDate == null)
		{
			ivjstcLblApplDate = new JLabel();
			ivjstcLblApplDate.setSize(100, 20);
			ivjstcLblApplDate.setText("Application Date:");
			ivjstcLblApplDate.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblApplDate.setLocation(14, 212);
		}
		return ivjstcLblApplDate;
	}
	/**
	 * Return the lblApplDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblApplDate()
	{
		if (ivjlblApplDate == null)
		{
			ivjlblApplDate = new JLabel();
			ivjlblApplDate.setSize(131, 20);
			ivjlblApplDate.setText("");
			ivjlblApplDate.setName("lblApplDate");
			ivjlblApplDate.setLocation(125, 212);
		}
		return ivjlblApplDate;
	}
	/**
	 * Return the stcLblExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpDate()
	{
		if (ivjstcLblExpDate == null)
		{
			ivjstcLblExpDate = new JLabel();
			ivjstcLblExpDate.setSize(80, 20);
			ivjstcLblExpDate.setText("Expires:");
			ivjstcLblExpDate.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblExpDate.setLocation(34, 42);
		}
		return ivjstcLblExpDate;
	}
	/**
	 * Return the lblExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpDate()
	{
		if (ivjlblExpDate == null)
		{
			ivjlblExpDate = new JLabel();
			ivjlblExpDate.setSize(77, 20);
			ivjlblExpDate.setText("");
			ivjlblExpDate.setName("lblExpDate");
			ivjlblExpDate.setLocation(125, 42);
		}
		return ivjlblExpDate;
	}
	/**
	 * Return the stcLblCntyOfRes property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCntyOfRes()
	{
		if (ivjstcLblCntyOfRes == null)
		{
			ivjstcLblCntyOfRes = new JLabel();
			ivjstcLblCntyOfRes.setSize(100, 20);
			ivjstcLblCntyOfRes.setText("County:");
			ivjstcLblCntyOfRes.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblCntyOfRes.setLocation(14, 232);
		}
		return ivjstcLblCntyOfRes;
	}
	/**
	 * Return the lblCntyOfRes property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCntyOfRes()
	{
		if (ivjlblCntyOfRes == null)
		{
			ivjlblCntyOfRes = new JLabel();
			ivjlblCntyOfRes.setSize(171, 20);
			ivjlblCntyOfRes.setText("");
			ivjlblCntyOfRes.setName("lblCntyOfRes");
			ivjlblCntyOfRes.setLocation(125, 232);
		}
		return ivjlblCntyOfRes;
	}

	/**
	 * Return the ivjtxtAOwnerInfo property value.
	 * Arial is the default font on all frames.  The font consturctor is
	 * used here in order to make the text bold.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAOwnerInfo()
	{
		if (ivjtxtAOwnerInfo == null)
		{
			ivjtxtAOwnerInfo = new JTextArea();
			ivjtxtAOwnerInfo.setBounds(19, 40, 304, 118);
			ivjtxtAOwnerInfo.setBackground(
				new java.awt.Color(204, 204, 204));
			ivjtxtAOwnerInfo.setDisabledTextColor(new Color(0, 0, 0));
			ivjtxtAOwnerInfo.setFont(new Font("Arial", Font.BOLD, 12));
			ivjtxtAOwnerInfo.setEditable(false);
			ivjtxtAOwnerInfo.setEnabled(false);
		}
		return ivjtxtAOwnerInfo;
	}

	/**
	 * Return the stcLblAddlSetOrdered property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddlSetOrdered()
	{
		if (ivjstcLblAddlSetOrdered == null)
		{
			ivjstcLblAddlSetOrdered = new javax.swing.JLabel();
			ivjstcLblAddlSetOrdered.setSize(108, 20);
			ivjstcLblAddlSetOrdered.setText("ADDITIONAL SET");
			ivjstcLblAddlSetOrdered.setForeground(java.awt.Color.red);
			ivjstcLblAddlSetOrdered.setLocation(126, 189);
		}
		return ivjstcLblAddlSetOrdered;
	}

	/**
	 * Return the stcLblDealerNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerNo()
	{
		if (ivjstcLblDealerNo == null)
		{
			ivjstcLblDealerNo = new JLabel();
			ivjstcLblDealerNo.setSize(59, 20);
			ivjstcLblDealerNo.setText("Dealer No:");
			ivjstcLblDealerNo.setLocation(18, 232);
		}
		return ivjstcLblDealerNo;
	}

	/**
	 * Return the lblDealerNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerNo()
	{
		if (ivjlblDealerNo == null)
		{
			ivjlblDealerNo = new JLabel();
			ivjlblDealerNo.setBounds(88, 232, 125, 20);
			ivjlblDealerNo.setText("");
		}
		return ivjlblDealerNo;
	}

	/**
	 * setData
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			caVehicleInquiryData = (VehicleInquiryData) aaDataObject;

			// defect 10441
			// Use Class Object, caSpclPltRegisData 
			// Use assignment vs. set methods
			caSpclPltRegisData =
				(SpecialPlatesRegisData) caVehicleInquiryData
					.getMfVehicleData()
					.getSpclPltRegisData();

			if (caSpclPltRegisData != null)
			{
				OwnerData laOwnerData =
					caSpclPltRegisData.getOwnrData();

				// Plate No
				getlblPltNo().setText(caSpclPltRegisData.getRegPltNo());

				// Plate Age 
				int liAge = caSpclPltRegisData.getRegPltAge(false);
				getlblPltAge().setText(" " + liAge);

				// Exp Mo/Yr 
				// Do not present "0" for Exp Mo/Yr
				String lsPltExpMo =
					caSpclPltRegisData.getPltExpMo() == 0
						? " "
						: "" + caSpclPltRegisData.getPltExpMo();

				String lsPltExpYr =
					caSpclPltRegisData.getPltExpYr() == 0
						? " "
						: "" + caSpclPltRegisData.getPltExpYr();

				// Always print "/" 
				getlblExpDate().setText(lsPltExpMo + "/" + lsPltExpYr);

				// Plate Description
				// Use new ItemCodesCache.getItmCdDesc() 
				String lsRegPltCd = caSpclPltRegisData.getRegPltCd();

				String lsItmCdDesc =
					ItemCodesCache.getItmCdDesc(lsRegPltCd);

				PlateTypeData laPltTypeData =
					PlateTypeCache.getPlateType(lsRegPltCd);

				if (laPltTypeData != null
					&& laPltTypeData.isAnnualPlt())
				{
					lsItmCdDesc = lsItmCdDesc + "    " + lsPltExpYr;
				}
				getlblPltType().setText(lsItmCdDesc);
				// end defect 10441 

				// Organization Name
				String lsOrgName =
					OrganizationNumberCache.getOrgName(
						caSpclPltRegisData.getRegPltCd(),
						caSpclPltRegisData.getOrgNo());
				getlblOrg().setText(lsOrgName);

				// Office Name 
				int liOfcNo = caSpclPltRegisData.getResComptCntyNo();

				// defect 10441 
				// Use new OfficeIdsCache.getOfcName() 
				String lsOfcName = OfficeIdsCache.getOfcName(liOfcNo);
				getlblCntyOfRes().setText(liOfcNo + "   " + lsOfcName);
				// end defect 10441 

				// Status 
				getlblStatus().setText(
					(
						String) SpecialPlatesConstant
							.INTERPRET_STATUSCDS
							.get(
						caSpclPltRegisData.getMFGStatusCd()));

				// MfgPlate No  
				getlblMfgPltNo().setText(
					caSpclPltRegisData.getMfgPltNo());

				// defect 10441 
				// Add Term / Auction Price 
				getlblTerm().setText(
					"" + caSpclPltRegisData.getPltValidityTerm());

				if (!caSpclPltRegisData
					.getAuctnPdAmt()
					.equals(new Dollar(0)))
				{
					getlblAuctionPrice().setText(
						caSpclPltRegisData.getAuctnPdAmt().toString());
				}

				// Use new method getFormattedDate() 
				// Format Manufactured Date
				getlblMfgDate().setText(
					getFormattedDate(caSpclPltRegisData.getMFGDate()));

				// Format Plate Application Date
				getlblApplDate().setText(
					getFormattedDate(
						caSpclPltRegisData.getPltApplDate()));
				// end defect 10441 

				// Additional Set ordered text
				getstcLblAddlSetOrdered().setVisible(
					caSpclPltRegisData.getAddlSetIndi() != 0);

				// Set the Owner data text area  
				if (laOwnerData != null)
				{
					// Owner Id 
					String lsOwnrId = laOwnerData.getOwnrId();
					boolean lbAllZeros =
						UtilityMethods.isAllZeros(lsOwnrId);

					getlblOwnrId().setText(
						lbAllZeros ? "" : lsOwnrId.trim());

					// defect 10112 
					// Name and Address
					gettxtAOwnerInfo().setText(
						laOwnerData
							.getNameAddressStringBuffer()
							.toString());
					// end defect 10112 
				}

				getlblEMail().setText(
					caSpclPltRegisData.getPltOwnrEMail());

				// Format phone number
				String lsPltOwnrPhoneNo =
					caSpclPltRegisData.getPltOwnrPhoneNo();

				if (UtilityMethods.isAllZeros(lsPltOwnrPhoneNo))
				{
					lsPltOwnrPhoneNo = "";
				}
				else
				{
					lsPltOwnrPhoneNo =
						UtilityMethods.addPadding(
							lsPltOwnrPhoneNo,
							10,
							" ");
					lsPltOwnrPhoneNo =
						lsPltOwnrPhoneNo.substring(0, 3)
							+ "-"
							+ lsPltOwnrPhoneNo.substring(3, 6)
							+ "-"
							+ lsPltOwnrPhoneNo.substring(6);
				}
				getlblPhoneNo().setText(lsPltOwnrPhoneNo);

				// defect 10441
				// Dealer License Number
				String lsDlrGDN = caSpclPltRegisData.getPltOwnrDlrGDN();

				if (!UtilityMethods.isAllZeros(lsDlrGDN))
				{
					getlblDealerNo().setText(lsDlrGDN);
				}
				setupHQOnlyFields();
				// end defect 10441 
			}
		}
		catch (Exception aeEx)
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Data error",
				"ERROR").displayError(
				this);
		}
	}

	/**
	 * This method initializes ivjstcLblAuctionPrice
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAuctionPrice()
	{
		if (ivjstcLblAuctionPrice == null)
		{
			ivjstcLblAuctionPrice = new JLabel();
			ivjstcLblAuctionPrice.setBounds(10, 162, 102, 23);
			ivjstcLblAuctionPrice.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblAuctionPrice.setText("Auction Price:");
		}
		return ivjstcLblAuctionPrice;
	}

	/**
	 * This method initializes ivjlblAuctionPrice
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAuctionPrice()
	{
		if (ivjlblAuctionPrice == null)
		{
			ivjlblAuctionPrice = new JLabel();
			ivjlblAuctionPrice.setSize(114, 23);
			ivjlblAuctionPrice.setLocation(125, 162);
		}
		return ivjlblAuctionPrice;
	}

	/**
	 * This method initializes ivjstcLblTerm
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTerm()
	{
		if (ivjstcLblTerm == null)
		{
			ivjstcLblTerm = new JLabel();
			ivjstcLblTerm.setBounds(217, 42, 34, 20);
			ivjstcLblTerm.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblTerm.setText("Term:");
		}
		return ivjstcLblTerm;
	}

	/**
	 * This method initializes ivjlblTerm
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTerm()
	{
		if (ivjlblTerm == null)
		{
			ivjlblTerm = new JLabel();
			ivjlblTerm.setBounds(254, 42, 21, 20);
			ivjlblTerm.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		}
		return ivjlblTerm;
	}

	/**
	 * This method initializes ivjJPaneHQOnly
	 * 
	 * @return JPanel
	 */
	private JPanel getJPaneHQOnly()
	{
		if (ivjJPaneHQOnly == null)
		{
			ivjJPaneHQOnly = new JPanel();
			ivjJPaneHQOnly.setBorder(
				new TitledBorder(new EtchedBorder(), "HQ Only:"));
			ivjJPaneHQOnly.setLayout(null);
			ivjJPaneHQOnly.add(getstcLblResrvDate(), null);
			ivjJPaneHQOnly.add(getlblResrvDate(), null);
			ivjJPaneHQOnly.add(getstcLblResrvReasn(), null);
			ivjJPaneHQOnly.add(getlblResrvReasnDesc(), null);
			ivjJPaneHQOnly.add(getstcLblMarketingAllowed(), null);
			ivjJPaneHQOnly.add(getstcLblVendorDate(), null);
			ivjJPaneHQOnly.add(getlblVendorTransDate(), null);
			ivjJPaneHQOnly.add(getstcLblFINDocNo(), null);
			ivjJPaneHQOnly.add(getlblFINDocNo(), null);
			ivjJPaneHQOnly.add(getlblMarketingAllowedYorN(), null);
			ivjJPaneHQOnly.setBounds(17, 276, 695, 88);
		}
		return ivjJPaneHQOnly;
	}

	/**
	 * This method initializes ivjstcLblResrvDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblResrvDate()
	{
		if (ivjstcLblResrvDate == null)
		{
			ivjstcLblResrvDate = new JLabel();
			ivjstcLblResrvDate.setSize(84, 20);
			ivjstcLblResrvDate.setText("Reserve Date:");
			ivjstcLblResrvDate.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblResrvDate.setLocation(30, 20);
		}
		return ivjstcLblResrvDate;
	}

	/**
	 * This method initializes ivjlblResrvDate
	 * 
	 * @return JLabel
	 */
	private JLabel getlblResrvDate()
	{
		if (ivjlblResrvDate == null)
		{
			ivjlblResrvDate = new JLabel();
			ivjlblResrvDate.setBounds(123, 20, 66, 21);
		}
		return ivjlblResrvDate;
	}

	/**
	 * This method initializes ivjstcLblResrvReasn
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblResrvReasn()
	{
		if (ivjstcLblResrvReasn == null)
		{
			ivjstcLblResrvReasn = new JLabel();
			ivjstcLblResrvReasn.setSize(111, 20);
			ivjstcLblResrvReasn.setText("Reserve Reason:");
			ivjstcLblResrvReasn.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblResrvReasn.setLocation(3, 40);
		}
		return ivjstcLblResrvReasn;
	}

	/**
	 * This method initializes ivjlblResrvReasn
	 * 
	 * @return JLabel
	 */
	private JLabel getlblResrvReasnDesc()
	{
		if (ivjlblResrvReasnDesc == null)
		{
			ivjlblResrvReasnDesc = new JLabel();
			ivjlblResrvReasnDesc.setSize(125, 20);
			ivjlblResrvReasnDesc.setLocation(123, 41);
		}
		return ivjlblResrvReasnDesc;
	}

	/**
	 * This method initializes ivjlblMarketingAllowed
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMarketingAllowed()
	{
		if (ivjstcLblMarketingAllowed == null)
		{
			ivjstcLblMarketingAllowed = new JLabel();
			ivjstcLblMarketingAllowed.setSize(111, 20);
			ivjstcLblMarketingAllowed.setText("Marketing Allowed:");
			ivjstcLblMarketingAllowed.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblMarketingAllowed.setLocation(3, 61);
		}
		return ivjstcLblMarketingAllowed;
	}

	/**
	 * This method initializes ivjstcLblVendorDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVendorDate()
	{
		if (ivjstcLblVendorDate == null)
		{
			ivjstcLblVendorDate = new JLabel();
			ivjstcLblVendorDate.setSize(117, 20);
			ivjstcLblVendorDate.setText("Vendor Trans Date:");
			ivjstcLblVendorDate.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblVendorDate.setLocation(325, 21);
		}
		return ivjstcLblVendorDate;
	}

	/**
	 * This method initializes ivjlblVendorDate
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVendorTransDate()
	{
		if (ivjlblVendorTransDate == null)
		{
			ivjlblVendorTransDate = new JLabel();
			ivjlblVendorTransDate.setBounds(453, 21, 66, 20);
		}
		return ivjlblVendorTransDate;
	}

	/**
	 * This method initializes ivjstcLblFINDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFINDocNo()
	{
		if (ivjstcLblFINDocNo == null)
		{
			ivjstcLblFINDocNo = new JLabel();
			ivjstcLblFINDocNo.setBounds(366, 40, 76, 21);
			ivjstcLblFINDocNo.setComponentOrientation(
				java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			ivjstcLblFINDocNo.setText("FIN Doc No:");
		}
		return ivjstcLblFINDocNo;
	}

	/**
	 * This method initializes ivjlblFINDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblFINDocNo()
	{
		if (ivjlblFINDocNo == null)
		{
			ivjlblFINDocNo = new JLabel();
			ivjlblFINDocNo.setBounds(453, 41, 107, 20);
		}
		return ivjlblFINDocNo;
	}

	/** 
	 * Return string formatted for date
	 * 
	 * @return String 
	 */
	private String getFormattedDate(int aiDate)
	{
		String lsDate = new String();

		if (aiDate > 0)
		{
			RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD, aiDate);
			lsDate = laRTSDate.toString();
		}
		return lsDate;
	}

	/**
	 * Setup HQ Only fields 
	 *
	 */
	private void setupHQOnlyFields()
	{
		boolean lbHQ = SystemProperty.isHQ();
		getJPaneHQOnly().setVisible(lbHQ);
		getstcLblResrvDate().setVisible(lbHQ);
		getlblResrvDate().setVisible(lbHQ);
		getlblResrvReasnDesc().setVisible(lbHQ);
		getstcLblVendorDate().setVisible(lbHQ);
		getstcLblMarketingAllowed().setVisible(lbHQ);
		getlblVendorTransDate().setVisible(lbHQ);
		getstcLblFINDocNo().setVisible(lbHQ);
		getlblFINDocNo().setVisible(lbHQ);
		getstcLblMarketingAllowed().setVisible(lbHQ);
		getlblMarketingAllowedYorN().setVisible(lbHQ);

		if (lbHQ)
		{
			// Reserve Date 
			getlblResrvDate().setText(
				getFormattedDate(caSpclPltRegisData.getResrvEffDate()));

			// Reserve Reason Description  
			String lsResrvCd = caSpclPltRegisData.getResrvReasnCd();
			if (lsResrvCd != null && lsResrvCd.trim().length() != 0)
			{
				IndicatorDescriptionsData laData =
					IndicatorDescriptionsCache.getIndiDesc(
						SpecialPlatesConstant.RESRVREASN_INDINAME,
						lsResrvCd);

				if (laData != null)
				{
					getlblResrvReasnDesc().setText(
						laData.getIndiDesc());
				}
			}

			// Vendor Date 
			getlblVendorTransDate().setText(
				getFormattedDate(
					caSpclPltRegisData.getVendorTransDate()));

			// Marketing Allowed
			String lsYorN =
				caSpclPltRegisData.isMktngAllowd() ? "YES" : "NO";
			getlblMarketingAllowedYorN().setText(lsYorN);

			// FIN Doc No 
			if (caSpclPltRegisData.getFINDocNo()!= null)
			{
				getlblFINDocNo().setText(caSpclPltRegisData.getFINDocNo());
			}
		}
	}

	/**
	 * This method initializes ivjlblMarketingAllowedYorN
	 * 
	 * @return JLabel
	 */
	private JLabel getlblMarketingAllowedYorN()
	{
		if (ivjlblMarketingAllowedYorN == null)
		{
			ivjlblMarketingAllowedYorN = new JLabel();
			ivjlblMarketingAllowedYorN.setBounds(123, 62, 39, 19);
		}
		return ivjlblMarketingAllowedYorN;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"