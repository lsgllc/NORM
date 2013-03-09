package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI.BasicCaret;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * FrmLienDisplayTTL005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * B Arredondo	09/06/2002	Defect 4651--Modified code in setData() so 
 * 							that the state was being
 *							displayed instead of null and the entire zip
 *							code with a hypen in between.
 * S Govindappa 02/14/2002   Fixing defect# 4898. Modified the background
 * 							color of the text to match with the 
 * 							application and made the Enter button a 
 * 							RTSButton.
 * J Seifert	12/08/2003	Adjusted 3 text areas in Visual
 *							Comp from 88 Height to 92 Height to fit all
 *							data.
 *							Defect# 6588 ver. 5.1.5.2
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * S Johnston	03/30/2005	Set the initial focus on this frame to the
 * 							Enter button by removing focusability from
 * 							the three JTextArea components
 * 							Modify gettxtAFirstLien(),
 * 							gettxtASecondLien(), gettxtAThirdLien()
 * 							defect 7333 Ver 5.2.3
 * J Rue		04/04/2005	Cleanup code,No tabbing issues
 * 							defect 7898 Ver 5.2.3
 * J Rue		06/14/2005	Increase height of TEXT boxes so to display
 * 							all of text. Comma "," look like period "."
 * 							modify Used Visual editor	
 * 							defect 8067 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3 
 * J Rue		08/30/2005	Set display foreground to black
 * 							used Visual Editor
 * 							defect 7898 Ver 5.2.3 
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/16/2005	Replace ", " and COMMA_SPACE
 * 							with CommonConstant.STR_COMMA_SPACE
 * 							modify setData()
 * 							defect 7898 Ver 5.2.3 
 * T. Pederson	12/21/2005	Removed WindowListener and associated
 * 							methods (not being used).
 * 							delete windowActivated(), windowClosed(),
 * 							windowClosing(), windowDeactivated(),
 * 							windowDeiconified(), windowIconified() and 
 * 							windowOpened().
 * 							defect 8494 Ver 5.2.3
 * K Harrell	01/24/2006	Add logic for "ESC" processing.
 * 							add implements KeyListener
 * 							add keyReleased() 
 * 							defect 7898 Ver 5.2.3 
 * J Rue		04/06/2007	Add Enter/Cancel/Help button panel
 * 							Set Help enabled to false
 * 							add getButtonPanel()
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	06/27/2007	Enable Help; Present msg "... not available
 * 							at this time. 
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/26/2008	ESC processing should be the same as "CANCEL" 
 * 							vs. "ENTER". 
 * 							modify keyReleased()
 * 							defect 8863 Ver Defect_POS_B 
 * K Harrell	03/05/2009	Add ID: XXXXXXXXXXX when PermLienhldrId 
 * 							assigned. 
 * 							add buildDateLine()
 * 							modify setData() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	04/10/2009	Enhance for corrupt records: LienDate = 0, 
 * 							Invalid PermLienhldrId 
 * 							modify setData(), buildDateLine()  
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	07/01/2009	Implement new LienholderData, methods
 * 							modify setData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/16/2009	delete KeyListener
 * 							delete keyReleased() 
 * 							defect 10290 Ver Defect_POS_H
 * --------------------------------------------------------------------- 
 */

/**
 * This form is used to display existing lien information.
 *
 * @version	Defect_POS_H	12/16/2009
 * @author	Marx Rajangam
 * <br>Creation Date:		06/24/2001 11:48:51
 */
public class FrmLienDisplayTTL005
	extends RTSDialogBox
	implements ActionListener
	// defect 10290 
	//, KeyListener
	// end defect 10290 
{
	private ButtonPanel ivjButtonPanel = null;
	private JPanel ivjFrmLienDisplayTTL005ContentPane1 = null;
	private JTextArea ivjtxtAFirstLien = null;
	private JTextArea ivjtxtASecondLien = null;
	private JTextArea ivjtxtAThirdLien = null;
	private JLabel ivjstcLblAddlLiens = null;

	private VehicleInquiryData caVehInqData = null;

	// Constant String
	private final static String ADDL_LIENS_EXIST =
		"ADDITIONAL LIEN(S) EXIST";
	private static final String FIRSTLIENDATE = "1st Lien Date: ";
	private static final String SECONDLIENDATE = "2nd Lien Date: ";
	private static final String THIRDLIENDATE = "3rd Lien Date: ";
	private final static String UNKNOWN = "UNKNOWN";
	private final static String NEXT_LINE =
		CommonConstant.SYSTEM_LINE_SEPARATOR;

	/**
	 * FrmLienDisplayTTL005 default constructor
	 */
	public FrmLienDisplayTTL005()
	{
		super();
		initialize();
	}

	/**
	 * FrmLienDisplayTTL005 one arg constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmLienDisplayTTL005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmLienDisplayTTL005 one arg constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmLienDisplayTTL005(JFrame aaParent)
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
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Build Date Line
	 * 
	 * @param asString
	 * @return String
	 */
	private String buildDateLine(String asString, String asPermLienId)
	{
		return (
			UtilityMethods.addPaddingRight(asString, 45, " ")
				+ TitleClientUtilityMethods.getPermLienhldrLabel(
					asPermLienId));
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
				ivjButtonPanel.setBounds(54, 353, 216, 36);

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
	 * Return the ivjFrmLienDisplayTTL005ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmLienDisplayTTL005ContentPane1()
	{
		if (ivjFrmLienDisplayTTL005ContentPane1 == null)
		{
			try
			{
				ivjFrmLienDisplayTTL005ContentPane1 = new JPanel();
				ivjFrmLienDisplayTTL005ContentPane1.setName(
					"ivjFrmLienDisplayTTL005ContentPane1");
				ivjFrmLienDisplayTTL005ContentPane1.setLayout(null);
				ivjFrmLienDisplayTTL005ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmLienDisplayTTL005ContentPane1.setMinimumSize(
					new Dimension(0, 0));
				ivjFrmLienDisplayTTL005ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				getFrmLienDisplayTTL005ContentPane1().add(
					gettxtAFirstLien(),
					gettxtAFirstLien().getName());

				getFrmLienDisplayTTL005ContentPane1().add(
					getstcLblAddlLiens(),
					getstcLblAddlLiens().getName());

				getFrmLienDisplayTTL005ContentPane1().add(
					gettxtASecondLien(),
					gettxtASecondLien().getName());

				getFrmLienDisplayTTL005ContentPane1().add(
					gettxtAThirdLien(),
					gettxtAThirdLien().getName());

				getFrmLienDisplayTTL005ContentPane1().add(
					getButtonPanel(),
					getButtonPanel().getName());
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
		return ivjFrmLienDisplayTTL005ContentPane1;
	}

	/**
	 * Return the ivjstcLblAddlLiens property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddlLiens()
	{
		if (ivjstcLblAddlLiens == null)
		{
			try
			{
				ivjstcLblAddlLiens = new JLabel();
				ivjstcLblAddlLiens.setName("ivjstcLblAddlLiens");
				ivjstcLblAddlLiens.setText(ADDL_LIENS_EXIST);
				ivjstcLblAddlLiens.setBounds(87, 333, 154, 14);
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
		return ivjstcLblAddlLiens;
	}

	/**
	 * Return the ivjtxtAFirstLien property value.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAFirstLien()
	{
		if (ivjtxtAFirstLien == null)
		{
			try
			{
				BasicCaret ivjLocalCaret;
				ivjLocalCaret = new BasicCaret();
				ivjLocalCaret.setBlinkRate(500);
				ivjtxtAFirstLien = new JTextArea();
				ivjtxtAFirstLien.setName("ivjtxtAFirstLien");
				ivjtxtAFirstLien.setHighlighter(new BasicHighlighter());
				ivjtxtAFirstLien.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtAFirstLien.setCaret(ivjLocalCaret);
				ivjtxtAFirstLien.setBackground(
					new Color(204, 204, 204));
				ivjtxtAFirstLien.setForeground(java.awt.Color.black);
				ivjtxtAFirstLien.setFont(new Font("Arial", 1, 12));
				ivjtxtAFirstLien.setEditable(false);
				ivjtxtAFirstLien.setMinimumSize(
					new Dimension(177, 294));
				ivjtxtAFirstLien.setBounds(22, 12, 315, 98);
				ivjtxtAFirstLien.setFocusable(false);
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
		return ivjtxtAFirstLien;
	}

	/**
	 * Return the ivjtxtASecondLien property value.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtASecondLien()
	{
		if (ivjtxtASecondLien == null)
		{
			try
			{
				BasicCaret ivjLocalCaret1;
				ivjLocalCaret1 = new BasicCaret();
				ivjLocalCaret1.setBlinkRate(500);
				ivjtxtASecondLien = new JTextArea();
				ivjtxtASecondLien.setName("ivjtxtASecondLien");
				ivjtxtASecondLien.setHighlighter(
					new BasicHighlighter());
				ivjtxtASecondLien.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtASecondLien.setCaret(ivjLocalCaret1);
				ivjtxtASecondLien.setBackground(
					new Color(204, 204, 204));
				ivjtxtASecondLien.setForeground(java.awt.Color.black);
				ivjtxtASecondLien.setFont(new Font("Arial", 1, 12));
				ivjtxtASecondLien.setEditable(false);
				ivjtxtASecondLien.setMinimumSize(
					new Dimension(177, 294));
				ivjtxtASecondLien.setBounds(22, 116, 315, 98);
				ivjtxtASecondLien.setFocusable(false);
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
		return ivjtxtASecondLien;
	}

	/**
	 * Return the ivjtxtAThirdLien property value.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtAThirdLien()
	{
		if (ivjtxtAThirdLien == null)
		{
			try
			{
				BasicCaret ivjLocalCaret2;
				ivjLocalCaret2 = new BasicCaret();
				ivjLocalCaret2.setBlinkRate(500);
				ivjtxtAThirdLien = new JTextArea();
				ivjtxtAThirdLien.setName("ivjtxtAThirdLien");
				ivjtxtAThirdLien.setHighlighter(new BasicHighlighter());
				ivjtxtAThirdLien.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtAThirdLien.setCaret(ivjLocalCaret2);
				ivjtxtAThirdLien.setBackground(
					new Color(204, 204, 204));
				ivjtxtAThirdLien.setForeground(java.awt.Color.black);
				ivjtxtAThirdLien.setFont(new Font("Arial", 1, 12));
				ivjtxtAThirdLien.setEditable(false);
				ivjtxtAThirdLien.setMinimumSize(
					new Dimension(177, 294));
				ivjtxtAThirdLien.setBounds(22, 220, 315, 99);
				ivjtxtAThirdLien.setFocusable(false);
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
		return ivjtxtAThirdLien;
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
			setName(ScreenConstant.TTL005_FRANE_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(351, 424);
			setTitle(ScreenConstant.TTL005_TITLE);
			setContentPane(getFrmLienDisplayTTL005ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel().getBtnEnter());
		getButtonPanel().getBtnEnter().setRequestFocusEnabled(true);
		getButtonPanel().getBtnEnter().requestFocus();
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
			FrmLienDisplayTTL005 laFrmLienDisplayTTL005;
			laFrmLienDisplayTTL005 = new FrmLienDisplayTTL005();
			laFrmLienDisplayTTL005.setModal(true);
			laFrmLienDisplayTTL005
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmLienDisplayTTL005.show();
			Insets insets = laFrmLienDisplayTTL005.getInsets();
			laFrmLienDisplayTTL005.setSize(
				laFrmLienDisplayTTL005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmLienDisplayTTL005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmLienDisplayTTL005.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
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
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;

			TitleData laTtlData =
				caVehInqData.getMfVehicleData().getTitleData();

			// defect 10112 
			// Use getXXXXStringBuffer() for Name, Address 
			// Present ID: XXXXXXXXXXX if PermLienhldrId 
			// Use Data Vectors 

			// FIRST LIEN
			// defect 10112 
			LienholderData laLienData =
				laTtlData.getLienholderData(TitleConstant.LIENHLDR1);

			String lsDate = UNKNOWN;
			if (laLienData.getLienDate() != 0)
			{
				lsDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						laLienData.getLienDate())
						.toString();
			}
			String lsText =
				buildDateLine(
					FIRSTLIENDATE + lsDate,
					laTtlData.getPermLienHldrId1())
					+ NEXT_LINE;

			StringBuffer lsNameAddr = new StringBuffer();

			lsNameAddr.append(
				lsText + laLienData.getNameAddressStringBuffer());

			gettxtAFirstLien().setText(lsNameAddr.toString());

			// SECOND LIEN 
			laLienData =
				laTtlData.getLienholderData(TitleConstant.LIENHLDR2);

			if (laLienData.isPopulated())
			{
				lsDate = UNKNOWN;

				if (laLienData.getLienDate() != 0)
				{
					lsDate =
						lsDate =
							new RTSDate(
								RTSDate.YYYYMMDD,
								laLienData.getLienDate())
								.toString();
				}

				String lsText2 =
					buildDateLine(
						SECONDLIENDATE + lsDate,
						laTtlData.getPermLienHldrId2())
						+ NEXT_LINE;

				lsNameAddr = new StringBuffer();
				lsNameAddr.append(
					lsText2 + laLienData.getNameAddressStringBuffer());

				gettxtASecondLien().setText(lsNameAddr.toString());
			}

			// THIRD LIEN 
			laLienData =
				laTtlData.getLienholderData(TitleConstant.LIENHLDR3);

			if (laLienData.isPopulated())
			{
				lsDate = UNKNOWN;

				if (laLienData.getLienDate() != 0)
				{
					lsDate =
						new RTSDate(
							RTSDate.YYYYMMDD,
							laLienData.getLienDate())
							.toString();
				}

				String lsText3 =
					buildDateLine(
						THIRDLIENDATE + lsDate,
						laTtlData.getPermLienHldrId3())
						+ NEXT_LINE;

				lsNameAddr = new StringBuffer();
				lsNameAddr.append(
					lsText3 + laLienData.getNameAddressStringBuffer());

				gettxtAThirdLien().setText(lsNameAddr.toString());
			}
			// end defect 10112 

			// display Additional Liens exist if applicable
			if (laTtlData.getAddlLienRecrdIndi() == 0)
			{
				getstcLblAddlLiens().setVisible(false);
			}
		} // end if (aaDataObject != null)
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"