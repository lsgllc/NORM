package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.txdot.isd.rts.client.desktop.RTSDeskTop;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.PresumptiveValueData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmPresumptiveValueTTL045.java 
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson	09/08/2006	New class.
 * 							defect 8926 Ver 5.2.5
 * K Harrell	05/20/2009	Widened screen, lengthened ivjlblMake.  
 * 							Sorted members. Additional screen cleanup.
 * 							Modify via Visual Editor. 
 * 							defect 9134 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to display the presumptive value returned for 
 * a VIN.
 *
 * @version	Defect_POS_F	05/20/2009
 * @author	Todd Pederson
 * <br>Creation Date:		09/08/2006 10:05:41
 */
public class FrmPresumptiveValueTTL045
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel buttonPanel = null;
	private JPanel jPanel = null;
	private JLabel lblMake = null;
	private JLabel lblPresumpValue = null;
	private JLabel lblVIN = null;
	private JLabel lblYearModel = null;
	private JLabel stcLblPresumpValue = null;
	private JLabel stcLblVIN = null;
	private JLabel stcLblYearMake = null;

	private final static String DOLLARSIGN = "$";
	private final static Dollar EIGHTYPERCENT = new Dollar(0.8);
	private final static String NOVALUE = "NO VALUE FOR VIN";
	private final static String PRESUMPVAL = "80% Presumptive AbstractValue:";
	private final static String VIN = "VIN:";
	private final static String YEAR_MAKE = "Year/Make:";
	private final static Dollar ZERODOLLAR = new Dollar(0.00);

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPresumptiveValueTTL045 laFrmPresumptiveValueTTL045;
			laFrmPresumptiveValueTTL045 =
				new FrmPresumptiveValueTTL045();
			laFrmPresumptiveValueTTL045.setModal(true);
			laFrmPresumptiveValueTTL045
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laWE)
				{
					System.exit(0);
				}
			});
			laFrmPresumptiveValueTTL045.show();
			java.awt.Insets insets =
				laFrmPresumptiveValueTTL045.getInsets();
			laFrmPresumptiveValueTTL045.setSize(
				laFrmPresumptiveValueTTL045.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPresumptiveValueTTL045.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPresumptiveValueTTL045.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmPresumptiveValueTTL045.java Constructor 
	 * 
	 */
	public FrmPresumptiveValueTTL045()
	{
		super();
		initialize();
	}

	/**
	 * FrmPresumptiveValueTTL045.java Constructor
	 * 
	 * @param aaParent RTSDeskTop
	 */
	public FrmPresumptiveValueTTL045(RTSDeskTop aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPresumptiveValueTTL045.java Constructor
	 * 
	 * @param aaParent RTSDialogBox
	 */
	public FrmPresumptiveValueTTL045(RTSDialogBox aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when user performs an action.  The user completes the  
	 * screen by making appropriate selections/entries and pressing 
	 * enter.
	 *
	 * Cancel or Help buttons respectively result in destroying the 
	 * window or providing appropriate help.
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
			if (aaAE.getSource() == getButtonPanel().getBtnCancel()
				|| aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL045);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ButtonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (buttonPanel == null)
		{
			buttonPanel = new ButtonPanel();
			buttonPanel.setSize(216, 36);
			buttonPanel.setLocation(80, 113);
			buttonPanel.addActionListener(this);
		}
		return buttonPanel;
	}

	/**
	 * Return the JPanel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getButtonPanel(), null);
			jPanel.add(getstcLblPresumpValue(), null);
			jPanel.add(getlblPresumpValue(), null);
			jPanel.add(getstcLblVIN(), null);
			jPanel.add(getstcLblYearMake(), null);
			jPanel.add(getlblVIN(), null);
			jPanel.add(getlblMake(), null);
			jPanel.add(getlblYearModel(), null);
		}
		return jPanel;
	}

	/**
	 * Return the lblMake property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getlblMake()
	{
		if (lblMake == null)
		{
			lblMake = new JLabel();
			lblMake.setSize(154, 20);
			lblMake.setText("FORD");
			lblMake.setLocation(215, 20);
		}
		return lblMake;
	}

	/**
	 * Return the lblPresumpValue property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPresumpValue()
	{
		if (lblPresumpValue == null)
		{
			lblPresumpValue = new JLabel();
			lblPresumpValue.setSize(156, 20);
			lblPresumpValue.setText("$ 1500.00");
			lblPresumpValue.setLocation(181, 76);
		}
		return lblPresumpValue;
	}

	/**
	 * Return the lblVIN property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVIN()
	{
		if (lblVIN == null)
		{
			lblVIN = new JLabel();
			lblVIN.setSize(155, 20);
			lblVIN.setText("1FDEE14F4EHA75431");
			lblVIN.setLocation(181, 48);
		}
		return lblVIN;
	}

	/**
	 * Return the lblYearModel property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getlblYearModel()
	{
		if (lblYearModel == null)
		{
			lblYearModel = new JLabel();
			lblYearModel.setSize(28, 20);
			lblYearModel.setText("2005");
			lblYearModel.setLocation(181, 20);
		}
		return lblYearModel;
	}

	/**
	 * Return the stcLblPresumpValue property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPresumpValue()
	{
		if (stcLblPresumpValue == null)
		{
			stcLblPresumpValue = new JLabel();
			stcLblPresumpValue.setBounds(27, 76, 143, 20);
			stcLblPresumpValue.setText(PRESUMPVAL);
			stcLblPresumpValue.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return stcLblPresumpValue;
	}

	/**
	 * Return the stcLblVIN property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVIN()
	{
		if (stcLblVIN == null)
		{
			stcLblVIN = new JLabel();
			stcLblVIN.setBounds(90, 48, 80, 20);
			stcLblVIN.setText(VIN);
			stcLblVIN.setHorizontalTextPosition(
				SwingConstants.TRAILING);
			stcLblVIN.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return stcLblVIN;
	}

	/**
	 * Return the stcLblYearMake property value. 
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblYearMake()
	{
		if (stcLblYearMake == null)
		{
			stcLblYearMake = new JLabel();
			stcLblYearMake.setSize(80, 20);
			stcLblYearMake.setText(YEAR_MAKE);
			stcLblYearMake.setHorizontalAlignment(SwingConstants.RIGHT);
			stcLblYearMake.setLocation(90, 20);
		}
		return stcLblYearMake;
	}

	/**
	 * Initialize the class.
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setContentPane(getJPanel());
		this.setSize(380, 186);
		this.setLocation(0, 0);
		setTitle(ScreenConstant.TTL045_FRAME_TITLE);

	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 *
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null && aaData instanceof PresumptiveValueData)
		{
			PresumptiveValueData laPresumptData =
				(PresumptiveValueData) aaData;
			Dollar ldPrivatePartyValue =
				laPresumptData.getPrivatePartyValue();
			if (ldPrivatePartyValue.equals(ZERODOLLAR))
			{
				getlblPresumpValue().setText(NOVALUE);
			}
			else
			{
				Dollar ldEightyPerCentValue =
					ldPrivatePartyValue.multiply(EIGHTYPERCENT);
				getlblPresumpValue().setText(
					DOLLARSIGN
						+ CommonConstant.STR_SPACE_ONE
						+ String.valueOf(ldEightyPerCentValue));
			}
			if (laPresumptData.getVIN() != null
				&& laPresumptData.getVIN().length() > 0)
			{
				getlblVIN().setText(
					laPresumptData.getVIN().toUpperCase());
			}
			else
			{
				getlblVIN().setText(CommonConstant.STR_SPACE_EMPTY);
			}
			getlblMake().setText(
				laPresumptData.getVehMk().toUpperCase());
			if (laPresumptData.getVehModlYr() > 0)
			{
				getlblYearModel().setText(
					String.valueOf(laPresumptData.getVehModlYr()));
			}
			else
			{
				getlblYearModel().setText(
					CommonConstant.STR_SPACE_EMPTY);
			}
		}
		else
		{
			getlblMake().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblYearModel().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblVIN().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblPresumpValue().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Sets the frame location as centered horizontally and one third
	 * from the bottom of the screen.
	 * 
	 * @param abVisible boolean
	 */
	public void setVisibleRTS(boolean abVisible)
	{
		if (abVisible)
		{
			setManagingLocation(true);
			// Set the position of the frame so the field entries 
			// are visible when the confirmation box is displayed
			int liFrmHorzPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.width
					/ 2
					- getSize().width / 2);
			int liFrmVertPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.height
					* 2
					/ 3
					- getSize().height / 2);
			this.setLocation(liFrmHorzPos, liFrmVertPos);
			super.setVisibleRTS(true);
		}
		else
		{
			super.setVisibleRTS(false);
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
