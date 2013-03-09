package com.txdot.isd.rts.client.common.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmPlateKeySelectionKEY008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()  
 * M Wang	   	11/20/2002 	Fixed CQU100004994. Modified keyPressed() 
 *							and getPlateNo() to allow to use arrow keys 
 *							after user have input the plate number.
 * B Arredondo	12/23/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes in
 *							actionPerformed().
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * B Hargrove	03/16/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getButtonPanel1
 * 							defect 8240 Ver 5.2.3
 * T Pederson	08/29/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * J Zwiener	06/26/2009	Remove "Plate is 7 Digits" message
 * 							delete MAX_PLATE_NO
 * 							Also general code cleanup
 * 							modify plateKeyValidation()
 * 							defect 10091 Ver Defect_POS_F
 * K Harrell	12/16/2009	Class Cleanup. Implement UtilityMethods.isDTA()
 * 							add ivjtxtPlateNo, get method
 * 							add buildGSD(), validateData()
 * 							delete caDlrDataContainer
 * 							delete cvDlrTitlDataObjs, get method
 * 							delete plateKeyValidation(),processEnter() 
 * 							delete ivjPlateNo, get methods.
 * 							modify setData(), actionPerformed() 
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	01/25/2010	Do not throw Exception on empty Plate No. 
 * 							add getDlrTtlData() 
 * 							delete validateData() 
 * 							modify actionPerformed(), buildGSD() 
 * 							defect 10339 Ver Defect_POS_H 
 * ---------------------------------------------------------------------
 */
/**
 * This frame is used to enter plate number.
 *
 * @version Defect_POS_H 	01/25/2010
 * @author	Administrator 
 * <br>Creation Date:		07/15/2001 12:34:30
 */

public class FrmPlateKeySelectionKEY008
	extends RTSDialogBox
	implements ActionListener
{

	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JLabel ivjstcLblIf = null;
	private RTSInputField ivjtxtPlateNo = null;

	// String 
	private String csTransCd = null;

	//	Object 
	private DealerTitleData caDlrTtlData = null;

	// Text Constants 
	private final static String FRM_NAME_KEY008 =
		"FrmPlateKeySelectionKEY008";
	private final static String FRM_TITLE_KEY008 =
		"Plate Key Selection   KEY008";
	private final static String TXT_ENTER_PLT_IF_APPL =
		"If applicable, enter plate number:";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPlateKeySelectionKEY008 laFrmPlateKeySelectionKEY008;
			laFrmPlateKeySelectionKEY008 =
				new FrmPlateKeySelectionKEY008();
			laFrmPlateKeySelectionKEY008.setModal(true);
			laFrmPlateKeySelectionKEY008
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPlateKeySelectionKEY008.show();
			Insets laInsets = laFrmPlateKeySelectionKEY008.getInsets();
			laFrmPlateKeySelectionKEY008.setSize(
				laFrmPlateKeySelectionKEY008.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmPlateKeySelectionKEY008.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmPlateKeySelectionKEY008.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmPlateKeySelectionKEY008 constructor
	 */
	public FrmPlateKeySelectionKEY008()
	{
		super();
		initialize();
	}

	/**
	 * FrmPlateKeySelectionKEY008 constructor
	 * 
	 * @param JDialog aaParent
	 */
	public FrmPlateKeySelectionKEY008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPlateKeySelectionKEY008 constructor
	 * 
	 * @param JFrame aaParent
	 */
	public FrmPlateKeySelectionKEY008(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * Enter will perform some validation and pass control on to the 
	 * business layer
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			// defect 10339 
			// Empty Plate No is valid 
			// defect 10290
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// if (validateData())	
				//	{
				getController().processData(
					AbstractViewController.ENTER,
					buildGSD());
				//	}
				//	// end defect 10290 
				// end defect 10339 
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
				// defect 10290 
				// -- Remove NONTTL at this point 
				// -- Add DTA  
				if (csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.REJCOR)
					|| UtilityMethods.isDTA(csTransCd))
				{
					// end defect 10290 
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.KEY008);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.KEY008A);
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
	 * This method is used to assign the search variables to 
	 * GeneralSearchData after a user has pressed enter.
	 * 
	 * @return GeneralSearchData
	 */
	private GeneralSearchData buildGSD()
	{
		GeneralSearchData laGSD = new GeneralSearchData();
		laGSD.setKey1(CommonConstant.REG_PLATE_NO);
		laGSD.setKey2(gettxtPlateNo().getText());

		// defect 10339 
		if (gettxtPlateNo().isEmpty())
		{
			laGSD.setIntKey2(CommonConstant.KEY008_NO_PLT_FLG);
		}
		// end defect 10339 

		return laGSD;
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
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
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

				GridBagConstraints constraintsstcLblIf =
					new GridBagConstraints();
				constraintsstcLblIf.gridx = 1;
				constraintsstcLblIf.gridy = 1;
				constraintsstcLblIf.ipadx = 11;
				constraintsstcLblIf.insets = new Insets(31, 20, 13, 8);
				getJDialogBoxContentPane().add(
					getstcLblIf(),
					constraintsstcLblIf);

				GridBagConstraints constraintsPlateNo =
					new GridBagConstraints();
				constraintsPlateNo.gridx = 2;
				constraintsPlateNo.gridy = 1;
				constraintsPlateNo.fill = GridBagConstraints.HORIZONTAL;
				constraintsPlateNo.weightx = 1.0;
				constraintsPlateNo.ipadx = 85;
				constraintsPlateNo.insets = new Insets(28, 9, 10, 28);
				getJDialogBoxContentPane().add(
					gettxtPlateNo(),
					constraintsPlateNo);

				GridBagConstraints constraintsButtonPanel1 =
					new GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 2;
				constraintsButtonPanel1.gridwidth = 2;
				constraintsButtonPanel1.fill = GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 34;
				constraintsButtonPanel1.ipady = 31;
				constraintsButtonPanel1.insets =
					new Insets(11, 49, 15, 50);
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
	 * Return the ivjstcLblIf property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIf()
	{
		if (ivjstcLblIf == null)
		{
			try
			{
				ivjstcLblIf = new JLabel();
				ivjstcLblIf.setName("ivjstcLblIf");
				ivjstcLblIf.setText(TXT_ENTER_PLT_IF_APPL);
				ivjstcLblIf.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblIf;
	}

	/**
	 * Return the ivjtxtPlateNo property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setName("ivjtxtPlateNo");
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setMaxLength(CommonConstant.LENGTH_PLTNO);
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
		return ivjtxtPlateNo;
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
			setName(FRM_NAME_KEY008);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(350, 150);
			setModal(true);
			setTitle(FRM_TITLE_KEY008);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());
		// user code end
	}

	/**
	 * Return DealerTitleData
	 * 
	 * @return caDlrTtlData
	 */
	public DealerTitleData getDlrTtlData()
	{
		return caDlrTtlData;
	}

	/**
	 * Set up variable if it is a DTA event
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();

		// defect 10290 
		if (UtilityMethods.isDTA(csTransCd)
			&& aaDataObject instanceof DealerTitleData)
		{
			caDlrTtlData = (DealerTitleData) aaDataObject;

			// end defect 10290 
			MFVehicleData laMFVehData = caDlrTtlData.getMFVehicleData();

			if (laMFVehData != null)
			{
				RegistrationData laRegData = laMFVehData.getRegData();

				if (laRegData != null)
				{
					String lsPlate = laRegData.getRegPltNo();

					// defect 10290
					if (!UtilityMethods.isEmpty(lsPlate))
					{
						// end defect 10290 
						gettxtPlateNo().setText(lsPlate.trim());
					}
				}
			}
		}
		gettxtPlateNo().requestFocus();
	}

	//	/**
	//	 * This method is used to validate the format of a Plate No
	//	 *  
	//	 * @param GeneralSearchData aaSearchData
	//	 * @return GeneralSearchData
	//	 */
	//	private GeneralSearchData plateKeyValidation(GeneralSearchData aaSearchData)
	//	{
	//		if (aaSearchData.getKey2() == null)
	//		{
	//			aaSearchData.setIntKey2(2);
	//		}
	//		// defect 10091
	//		//  Comment out to prevent to confirm message stating plate
	//		//  is 7 digits 
	//		//else if (aaSearchData.getKey2().length() == MAX_PLATE_NO)
	//		//{
	//		//	aaSearchData.setIntKey2(1);
	//		//}
	//		else if (
	//			aaSearchData.getKey2().length()
	//				> CommonConstant.LENGTH_PLTNO)
	//		{
	//			aaSearchData.setIntKey2(2);
	//		}
	//		return aaSearchData;
	//		// end defect 10091
	//	}
	//
	//	/**
	//	 * This method is used to assign the search variables to 
	//	 * GeneralSearchData after a user has pressed enter.
	//	 * 
	//	 * @return GeneralSearchData
	//	 */
	//	private GeneralSearchData processEnter()
	//	{
	//		String lsPlateNo;
	//		GeneralSearchData laSearchCriteria = new GeneralSearchData();
	//
	//		if (!ivjPlateNo
	//			.getText()
	//			.trim()
	//			.equals(CommonConstant.STR_SPACE_EMPTY))
	//		{
	//			lsPlateNo = ivjPlateNo.getText().trim();
	//			lsPlateNo = lsPlateNo.toUpperCase();
	//		}
	//		else
	//		{
	//			lsPlateNo = null;
	//		}
	//
	//		laSearchCriteria.setKey1(CommonConstant.REG_PLATE_NO);
	//		laSearchCriteria.setKey2(lsPlateNo);
	//		laSearchCriteria = plateKeyValidation(laSearchCriteria);
	//		return laSearchCriteria;
	//	}

}