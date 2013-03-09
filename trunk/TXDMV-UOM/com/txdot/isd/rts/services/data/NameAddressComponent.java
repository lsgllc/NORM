package com.txdot.isd.rts.services.data;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * NameAddressComponent.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/17/2009	Created
 * 							defect 10127 Defect_POS_F
 * K Harrell	12/28/2009	Add validation for acceptable MF characters
 * 							modify constructors, validateAddressFields()
 * 							defect 10299 Defect_POS_H
 * K Harrell	10/15/2011	Add validation for acceptable Country 
 * 							characters 
 * 							modify constructor, validateAddressFields()
 * 							defect 11004 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * NameAddressComponent is used to provide common validation for 
 * Name Address Fields. 
 *
 * @version	6.9.0 	10/15/2011
 * @author	Kathy Harrell
 * @since 			07/17/2009
 */
public class NameAddressComponent
{
	JCheckBox cachkUSA;
	JLabel castcLblDash;
	RTSInputField catxtCity;
	RTSInputField catxtCntry;
	RTSInputField catxtCntryZpcd;
	RTSInputField catxtName1;
	RTSInputField catxtName2;
	RTSInputField catxtState;
	RTSInputField catxtStreet1;
	RTSInputField catxtStreet2;
	RTSInputField catxtZpcd;
	RTSInputField catxtZpcdP4;

	// boolean
	boolean cbTXDefaultState;

	// int 
	int ciAddrErrMsgNo;
	int ciNameErrMsgNo;

	// Vector 
	// defect 10299 
	Vector cvMFValidation = new Vector();
	// end defect 10299  

	/**
	 * NameAddressComponent constructor comment.
	 */
	public NameAddressComponent()
	{
		super();
	}

	/**
	 * NameAddressComponent constructor comment.
	 */
	public NameAddressComponent(
		RTSInputField aatxtStreet1,
		RTSInputField aatxtStreet2,
		RTSInputField aatxtCity,
		RTSInputField aatxtState,
		RTSInputField aatxtZpcd,
		RTSInputField aatxtZpcdp4,
		int aiAddrErrMsgNo)
	{
		super();
		// defect 10299 
		cvMFValidation.add(aatxtStreet1);
		cvMFValidation.add(aatxtStreet2);
		cvMFValidation.add(aatxtCity);
		cvMFValidation.add(aatxtState);
		// end defect 10299 
		settxtStreet1(aatxtStreet1);
		settxtStreet2(aatxtStreet2);
		settxtCity(aatxtCity);
		settxtState(aatxtState);
		settxtZpcd(aatxtZpcd);
		settxtZpcdP4(aatxtZpcdp4);
		setAddrErrMsgNo(aiAddrErrMsgNo);
	}

	/**
	 * NameAddressComponent constructor comment.
	 */
	public NameAddressComponent(
		RTSInputField aatxtName1,
		RTSInputField aatxtName2,
		RTSInputField aatxtStreet1,
		RTSInputField aatxtStreet2,
		RTSInputField aatxtCity,
		RTSInputField aatxtState,
		RTSInputField aatxtZpcd,
		RTSInputField aatxtZpcdp4,
		RTSInputField aatxtCntry,
		RTSInputField aatxtCntryZpcd,
		JCheckBox aaUSAChkbx,
		JLabel aastcLblDash,
		int aiNameErrMsgNo,
		int aiAddrErrMsgNo,
		boolean abTXDefaultState)
	{
		super();
		// defect 10299 
		cvMFValidation.add(aatxtName1);
		cvMFValidation.add(aatxtName2);
		cvMFValidation.add(aatxtStreet1);
		cvMFValidation.add(aatxtStreet2);
		cvMFValidation.add(aatxtCity);
		cvMFValidation.add(aatxtState);
		// defect 11004
		// Omit Country; New Validation.
		//cvMFValidation.add(aatxtCntry);
		// end defect 11004
		cvMFValidation.add(aatxtCntryZpcd);
		// end defect 10299 

		settxtName1(aatxtName1);
		settxtName2(aatxtName2);
		settxtStreet1(aatxtStreet1);
		settxtStreet2(aatxtStreet2);
		settxtCity(aatxtCity);
		settxtState(aatxtState);
		settxtZpcd(aatxtZpcd);
		settxtZpcdP4(aatxtZpcdp4);
		settxtCntry(aatxtCntry);
		settxtCntryZpcd(aatxtCntryZpcd);
		setchkUSA(aaUSAChkbx);
		setstcLblDash(aastcLblDash);
		setNameErrMsgNo(aiNameErrMsgNo);
		setAddrErrMsgNo(aiAddrErrMsgNo);
		setTXDefaultState(abTXDefaultState);
	}

	/**
	 * Clear All Color 
	 * 
	 * @param aaComponent
	 */
	public void clearAllColor(Component aaComponent)
	{
		if (aaComponent instanceof JTextField
			|| aaComponent instanceof JRadioButton
			|| aaComponent instanceof JCheckBox
			|| aaComponent instanceof JComboBox)
		{
			if (aaComponent.isEnabled())
			{
				//If component has different color, change it to normal 
				//color. The colors are being set in RTSException.
				if (aaComponent.getForeground().equals(Color.white)
					&& aaComponent.getBackground().equals(
						RTSException.ERR_COLOR))
				{
					aaComponent.setForeground(Color.black);
					if (aaComponent instanceof JTextField
						|| aaComponent instanceof JComboBox)
					{
						aaComponent.setBackground(Color.white);
					}
					if (aaComponent instanceof JCheckBox
						|| aaComponent instanceof JRadioButton)
					{
						aaComponent.setBackground(
							new Color(204, 204, 204));
					}
				}
			}
		}
	}

	/**
	 * Get value of cachkUSA
	 * 
	 * @return JCheckBox
	 */
	public JCheckBox getchkUSA()
	{
		return cachkUSA;
	}

	/**
	 * Get value of castcLblDash
	 * 
	 * @return JLabel
	 */
	public JLabel getstcLblDash()
	{
		return castcLblDash;
	}

	/**
	 * Get value of ciAddrErrMsgNo
	 * 
	 * @return int
	 */
	public int gettxtAddrErrMsgNo()
	{
		return ciAddrErrMsgNo;
	}

	/**
	 * Get value of catxtCity
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtCity()
	{
		return catxtCity;
	}

	/**
	 * Get value of catxtCntry
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtCntry()
	{
		return catxtCntry;
	}

	/**
	 * Get value of catxtCntryZpcd
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtCntryZpcd()
	{
		return catxtCntryZpcd;
	}

	/**
	 * Get value of catxtName1
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtName1()
	{
		return catxtName1;
	}

	/**
	 * Get value of catxtName2
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtName2()
	{
		return catxtName2;
	}

	/**
	 * Get value of ciNameErrMsgNo
	 * 
	 * @return int
	 */
	public int gettxtNameErrMsgNo()
	{
		return ciNameErrMsgNo;
	}

	/**
	 * Get value of catxtState
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtState()
	{
		return catxtState;
	}

	/**
	 * Get value of catxtStreet1
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtStreet1()
	{
		return catxtStreet1;
	}

	/**
	 * Get value of catxtStreet2
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtStreet2()
	{
		return catxtStreet2;
	}

	/**
	 * Get value of catxtZpcd
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtZpcd()
	{
		return catxtZpcd;
	}

	/**
	 * Get value of catxtZpcdP4
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField gettxtZpcdP4()
	{
		return catxtZpcdP4;
	}

	/**
	 * Return a boolean if USA Address Data was not entered.
	 *  
	 * @return boolean
	 */
	public boolean isNameAddressEmpty()
	{
		return (
			gettxtName1().isEmpty()
				&& gettxtName2().isEmpty()
				&& isUSAAddressEmpty()
				&& gettxtCntry().isEmpty()
				&& gettxtCntryZpcd().isEmpty());
	}

	/**
	 * Get value of isTXDefaultState 
	 * Used upon initial setting, refresh 
	 * 
	 * @return boolean 
	 */
	public boolean isTXDefaultState()
	{
		return cbTXDefaultState;
	}

	/**
	 * Return a boolean if USA Address Data was not entered.
	 *  
	 * @return boolean
	 */
	public boolean isUSAAddressEmpty()
	{
		return (
			gettxtStreet1().isEmpty()
				&& gettxtStreet2().isEmpty()
				&& gettxtCity().isEmpty()
				&& gettxtState().isEmpty()
				&& gettxtZpcd().isEmpty()
				&& gettxtZpcdP4().isEmpty());
	}

	/**
	 * Refresh screen for Local Options 
	 * 
	 */
	public void refreshScreen()
	{
		gettxtName1().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtName2().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtStreet1().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtStreet2().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtCity().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcdP4().setText(CommonConstant.STR_SPACE_EMPTY);
		if (gettxtState().isEnabled())
		{
			gettxtState().setText(
				isTXDefaultState()
					? CommonConstant.STR_TX
					: CommonConstant.STR_SPACE_EMPTY);
		}

		if (getchkUSA() != null)
		{
			gettxtCntry().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtCntryZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
			getchkUSA().setSelected(true);
		}
	}

	/**
	 * Reset Non-Owner Address
	 * 
	 */
	public void resetNonOwnerAddr()
	{
		clearAllColor(gettxtStreet1());
		clearAllColor(gettxtStreet2());
		clearAllColor(gettxtCity());
		clearAllColor(gettxtState());
		clearAllColor(gettxtZpcd());
		clearAllColor(gettxtZpcdP4());
		gettxtStreet1().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtStreet2().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtCity().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcdP4().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtState().setText(CommonConstant.STR_SPACE_EMPTY);
	}

	/**
	 * Reset screen per USA checkbox selection  
	 * 
	 * @param abSelected
	 */
	public void resetPerUSA(boolean abSelected)
	{
		clearAllColor(gettxtCntry());
		clearAllColor(gettxtCntryZpcd());
		clearAllColor(gettxtState());
		clearAllColor(gettxtZpcd());
		clearAllColor(gettxtZpcdP4());
		clearAllColor(getchkUSA());

		if (abSelected)
		{
			String lsDefaultState =
				cbTXDefaultState
					? CommonConstant.STR_TX
					: CommonConstant.STR_SPACE_EMPTY;
			gettxtState().setText(lsDefaultState);
			gettxtZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtZpcdP4().setText(CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			gettxtState().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtCntry().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtCntryZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
		}
		setUSAAddressDisplay(abSelected);
		gettxtStreet1().requestFocus();
	}

	/**
	 * Return Empty String if null, else trimmed value  
	 * 
	 * @param abValue
	 * @return String 
	 */
	private String returnEmptyIfNull(String abValue)
	{
		return abValue == null
			? CommonConstant.STR_SPACE_EMPTY
			: abValue.trim();
	}

	/**
	 * Return "TX" if empty 
	 * 
	 * @param asState
	 * @return String 
	 */
	private String returnTXIfEmpty(String asState)
	{
		asState = returnEmptyIfNull(asState);

		if (asState.length() == 0 && cbTXDefaultState)
		{
			asState = CommonConstant.STR_TX;
		}
		return asState;
	}

	/**
	 * Set Address Data 
	 * 
	 * @param aaNameAddrData
	 */
	public void setAddressDataToDisplay(AddressData aaAddrData)
	{

		// Street1
		gettxtStreet1().setText(returnEmptyIfNull(aaAddrData.getSt1()));

		// Street2 
		gettxtStreet2().setText(returnEmptyIfNull(aaAddrData.getSt2()));

		// City 	
		gettxtCity().setText(returnEmptyIfNull(aaAddrData.getCity()));

		boolean lbUSA = aaAddrData.isUSA();

		if (getchkUSA() != null)
		{
			getchkUSA().setSelected(lbUSA);
			setUSAAddressDisplay(lbUSA);
		}

		if (lbUSA)
		{
			gettxtState().setText(
				returnTXIfEmpty(aaAddrData.getState()));
			gettxtZpcd().setText(
				returnEmptyIfNull(aaAddrData.getZpcd()));
			gettxtZpcdP4().setText(
				returnEmptyIfNull(aaAddrData.getZpcdp4()));
		}
		else
		{
			gettxtCntry().setText(
				returnEmptyIfNull(aaAddrData.getCntry()));
			gettxtCntryZpcd().setText(
				returnEmptyIfNull(aaAddrData.getCntryZpcd()));
		}

	}

	/**
	 * Assign Address values from screen
	 */
	public void setAddressToDataObject(AddressData aaAddressData)
	{
		aaAddressData.setSt1(gettxtStreet1().getText().trim());
		aaAddressData.setSt2(gettxtStreet2().getText().trim());
		aaAddressData.setCity(gettxtCity().getText().trim());

		if (getchkUSA() == null || getchkUSA().isSelected())
		{
			aaAddressData.setState(gettxtState().getText());
			aaAddressData.setZpcd(gettxtZpcd().getText());
			aaAddressData.setZpcdp4(gettxtZpcdP4().getText());
			aaAddressData.setCntry(CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			aaAddressData.setCntry(gettxtCntry().getText().trim());
			aaAddressData.setCntryZpcd(gettxtCntryZpcd().getText());
			aaAddressData.setState(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Set value of cachkUSA 
	 * 
	 * @param box
	 */
	public void setchkUSA(JCheckBox box)
	{
		cachkUSA = box;
	}

	/**
	 * 
	 * Enable/Disable Name/Address fields  
	 * 
	 */
	public void setEnabled(boolean abEnable)
	{
		getchkUSA().setEnabled(abEnable);
		gettxtName1().setEnabled(abEnable);
		gettxtName2().setEnabled(abEnable);
		gettxtStreet1().setEnabled(abEnable);
		gettxtStreet2().setEnabled(abEnable);
		gettxtCity().setEnabled(abEnable);
		if (getchkUSA().isSelected())
		{
			gettxtState().setEnabled(abEnable);
			gettxtZpcd().setEnabled(abEnable);
			gettxtZpcdP4().setEnabled(abEnable);
		}
		else
		{
			gettxtCntry().setEnabled(abEnable);
			gettxtCntryZpcd().setEnabled(abEnable);
		}

	}

	/**
	 * Set Name/Address Data to Display 
	 * 
	 * @param NameAddressData
	 */
	public void setNameAddressDataToDisplay(NameAddressData aaNameAddrData)
	{
		// Name1 
		gettxtName1().setText(
			returnEmptyIfNull(aaNameAddrData.getName1()));

		if (gettxtName2() != null)
		{
			gettxtName2().setText(
				returnEmptyIfNull(aaNameAddrData.getName2()));
		}
		setAddressDataToDisplay(aaNameAddrData.getAddressData());
	}

	/**
	 * Assign Name/Address values from screen
	 */
	public void setNameAddressToDataObject(NameAddressData aaNameAddrData)
	{
		aaNameAddrData.setName1(gettxtName1().getText().trim());
		aaNameAddrData.setName2(gettxtName2().getText().trim());
		setAddressToDataObject(aaNameAddrData.getAddressData());
	}

	/**
	 * Set Screen for Non USA Address 
	 */
	private void setNonUSAAddressDisplay(boolean abVisible)
	{
		gettxtCntry().setVisible(abVisible);
		gettxtCntryZpcd().setVisible(abVisible);
		gettxtCntry().setEnabled(abVisible);
		gettxtCntryZpcd().setEnabled(abVisible);
	}

	/**
	 * Set value of castcLblDash
	 * 
	 * @param label
	 */
	public void setstcLblDash(JLabel label)
	{
		castcLblDash = label;
	}

	/**
	 * Set value of cbTXDefaultState
	 * 
	 * @param b
	 */
	public void setTXDefaultState(boolean b)
	{
		cbTXDefaultState = b;
	}

	/**
	 * Set value of ciAddrErrMsgNo
	 * 
	 * @param i
	 */
	public void setAddrErrMsgNo(int i)
	{
		ciAddrErrMsgNo = i;
	}

	/**
	 * Set value of catxtCity
	 * 
	 * @param aatxtCity
	 */
	public void settxtCity(RTSInputField aatxtCity)
	{
		catxtCity = aatxtCity;
	}

	/**
	 * Set value of catxtCntry
	 * 
	 * @param aatxtCntry
	 */
	public void settxtCntry(RTSInputField aatxtCntry)
	{
		catxtCntry = aatxtCntry;
	}

	/**
	 * Set value of catxtCntryZpcd
	 * 
	 * @param aatxtCntryZpcd
	 */
	public void settxtCntryZpcd(RTSInputField aatxtCntryZpcd)
	{
		catxtCntryZpcd = aatxtCntryZpcd;
	}

	/**
	 * Set value of catxtName1
	 * 
	 * @param aatxtName1
	 */
	public void settxtName1(RTSInputField aatxtName1)
	{
		catxtName1 = aatxtName1;
	}

	/**
	 * Set value of catxtName2
	 * 
	 * @param aatxtName2
	 */
	public void settxtName2(RTSInputField aatxtName2)
	{
		catxtName2 = aatxtName2;
	}

	/**
	 * Set value of ciNameErrMsgNo
	 * 
	 * @param aiNameErrMsgNo
	 */
	public void setNameErrMsgNo(int aiNameErrMsgNo)
	{
		ciNameErrMsgNo = aiNameErrMsgNo;
	}

	/**
	 * Set value of catxtState
	 * 
	 * @param aatxtState
	 */
	public void settxtState(RTSInputField aatxtState)
	{
		catxtState = aatxtState;
	}

	/**
	 * Set value of catxtStreet1
	 * 
	 * @param aatxtStreet1
	 */
	public void settxtStreet1(RTSInputField aatxtStreet1)
	{
		catxtStreet1 = aatxtStreet1;
	}

	/**
	 * Set value of catxtStreet2
	 * 
	 * @param aatxtStreet2ld
	 */
	public void settxtStreet2(RTSInputField aatxtStreet2ld)
	{
		catxtStreet2 = aatxtStreet2ld;
	}

	/**
	 * Set value of aatxtZpcd
	 * 
	 * @param aatxtZpcd
	 */
	public void settxtZpcd(RTSInputField aatxtZpcd)
	{
		catxtZpcd = aatxtZpcd;
	}

	/**
	 * Set value of catxtZpcdP4
	 * 
	 * @param aatxtZpcdP4
	 */
	public void settxtZpcdP4(RTSInputField aatxtZpcdP4)
	{
		catxtZpcdP4 = aatxtZpcdP4;
	}

	/**
	 * Set visible/invisible based on parameter.
	 *
	 * @param abVisible boolean 
	 */
	private void setUSAAddressDisplay(boolean abVisible)
	{
		gettxtState().setVisible(abVisible);
		gettxtZpcd().setVisible(abVisible);
		getstcLblDash().setVisible(abVisible);
		gettxtZpcdP4().setVisible(abVisible);
		setNonUSAAddressDisplay(!abVisible);
	}

	/**
	 * Validate Address Fields
	 *
	 * @param aeRTSEx 
	 */
	public void validateAddressFields(RTSException aeRTSEx)
	{
		// defect 10299 
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvMFValidation,
			aeRTSEx);
		// end defect 10299 

		// Street1
		if (gettxtStreet1().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(ciAddrErrMsgNo),
				gettxtStreet1());
		}

		// City 
		if (gettxtCity().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(ciAddrErrMsgNo),
				gettxtCity());
		}

		// USA 
		if (getchkUSA() == null || getchkUSA().isSelected())
		{
			// State 
			if (gettxtState().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(ciAddrErrMsgNo),
					gettxtState());
			}
			else if (!gettxtState().isValidState())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INVALID_STATE_REENTER),
					gettxtState());
			}

			// Zpcd
			if (gettxtZpcd().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(ciAddrErrMsgNo),
					gettxtZpcd());
			}
			else if (!gettxtZpcd().isValidZpcd())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_ZIP_CODE_MUST_BE_5_DIGITS),
					gettxtZpcd());
			}

			// Zpcdp4 
			if (!gettxtZpcdP4().isValidZpcdP4())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_ZIP_CODEP4_MUST_BE_4_DIGITS),
					gettxtZpcdP4());
			}
		}
		// Non-USA Address 
		else
		{
			// Cntry
			// defect 11004 
			if (gettxtCntry().isValidState())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
					gettxtCntry());

				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
					getchkUSA());
			}
			else 
			{
				CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtCntry(), 
						aeRTSEx, TitleConstant.REQUIRED); 
			}
			// end defect 11004 
			
			// CntryZpcd 
			if (gettxtCntryZpcd().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(ciAddrErrMsgNo),
					gettxtCntryZpcd());
			}
		}

	}

	/** 
	 * Validate Fields on Screen
	 *
	 * @param aeRTSEx
	 */
	public void validateNameAddressFields(RTSException aeRTSEx)
	{
		// Name1 
		if (gettxtName1() != null && gettxtName1().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(ciNameErrMsgNo),
				gettxtName1());
		}
		validateAddressFields(aeRTSEx);
	}

}
