package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * FrmReprintStickerRPR004.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class. Ver 5.2.0
 * K Harrell	07/29/2004	Allow for start date and end date to be
 *							the same. Present error if start date
 *							greater than 400 days prior. 
 *							modify actionPerformed()
 *							defect 7385  Ver 5.2.1
 * K Harrell	07/30/2004	Default start date to current date
 *							modify setData()
 *							defect 7385  Ver 5.2.1
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel
 * 							defect 8240 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/20/2009	Standardize frame objects, screen text. 
 * 							Use "Begin" vs. "Start"  
 * 							Add logging to Admin Log.  Add mnemonics. 
 * 							add validateData(), ivjstcLblBeginDate,
 * 							 ivjtxtBeginDate, get methods
 * 							delete ivjstcLblStartDate, ivjtxtStartDate,
 * 							 get methods. 
 * 							modify actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * Frame RPR004 is used for reprinting stickers
 * 
 * @version	Defect_POS_F	08/20/2009
 * @author  Michael Abernethy
 * <br>Creation Date:		08/20/2002 
 */
public class FrmReprintStickerRPR004
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblEndDate = null;
	private RTSDateField ivjtxtEndDate = null;
	// defect 8628
	private RTSDateField ivjtxtBeginDate = null;
	private JLabel ivjstcLblBeginDate = null;
	private static final String ADMIN_LOG_ENTITY = "ReprintSticker";
	// end defect 8628

	/**
	 * FrmReprintReportRPR004 constructor
	 */
	public FrmReprintStickerRPR004()
	{
		super();
		initialize();
	}

	/**
	 * FrmReprintReportRPR004 constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReprintStickerRPR004(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReprintReportRPR004 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmReprintStickerRPR004(Frame aaOwner)
	{
		super(aaOwner);
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
			clearAllColor(this);

			// ENTER 
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				// defect 8628 
				if (!validateData())
				{
					return;
				}
				// end defect 8628 

				RTSDate laBeginDate = gettxtBeginDate().getDate();
				RTSDate laEndDate = gettxtEndDate().getDate();

				Map laMap = new HashMap();
				laMap.put(ReportConstant.BEGIN_DATE, laBeginDate);
				laMap.put(ReportConstant.END_DATE, laEndDate);

				// defect 8628 
				// Add AdminLogData to hash map to insert @ server 
				AdministrationLogData laAdminLogData =
					new AdministrationLogData(ReportConstant.CLIENT);
				laAdminLogData.setEntity(ADMIN_LOG_ENTITY);
				laAdminLogData.setEntityValue(
					laBeginDate.toString().substring(0, 6)
						+ laBeginDate.toString().substring(8, 10)
						+ CommonConstant.STR_DASH
						+ laEndDate.toString().substring(0, 6)
						+ laEndDate.toString().substring(8, 10));
				laAdminLogData.setAction(CommonConstant.TXT_REPORT);
				laMap.put(ReportConstant.ADMIN_LOG, laAdminLogData);
				// end defect 8628 

				getController().processData(
					AbstractViewController.ENTER,
					laMap);
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV001);
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
				ivjButtonPanel.setBounds(29, 107, 259, 34);
				ivjButtonPanel.setName("ivjButtonPanel");
				// user code begin {1}
				ivjButtonPanel.addActionListener(this);
				ivjButtonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblBeginDate(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblEndDate(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtBeginDate(), null);
				ivjRTSDialogBoxContentPane.add(gettxtEndDate(), null);
				ivjRTSDialogBoxContentPane.add(getButtonPanel(), null);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the stcLblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			try
			{
				ivjstcLblEndDate = new JLabel();
				ivjstcLblEndDate.setName("stcLblEndDate");
				ivjstcLblEndDate.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				ivjstcLblEndDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblEndDate.setLabelFor(gettxtEndDate());
				ivjstcLblEndDate.setSize(71, 20);
				ivjstcLblEndDate.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				ivjstcLblEndDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				ivjstcLblEndDate.setLocation(62, 65);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEndDate;
	}

	/**
	 * Return the ivjstcLblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				ivjstcLblBeginDate.setSize(75, 20);
				ivjstcLblBeginDate.setName("ivjstcLblBeginDate");
				ivjstcLblBeginDate.setText(
					InventoryConstant.TXT_BEGIN_DATE_COLON);
				ivjstcLblBeginDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblBeginDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_B);

				ivjstcLblBeginDate.setLocation(58, 34);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblBeginDate;
	}

	/**
	 * Return the txtEndDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setBounds(157, 65, 72, 20);
				ivjtxtEndDate.setName("txtEndDate");
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtEndDate;
	}

	/**
	 * Return the ivjtxtBeginDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			try
			{
				ivjtxtBeginDate = new RTSDateField();
				ivjtxtBeginDate.setBounds(157, 34, 72, 20);
				ivjtxtBeginDate.setName("ivjtxtBeginDate");

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtBeginDate;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
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
			setName("FrmReprintReportRPR004");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(325, 183);
			setTitle("Reprint Sticker Report   RPR004");
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReprintStickerRPR004 laFrmReprintReportRPR004;
			laFrmReprintReportRPR004 = new FrmReprintStickerRPR004();
			laFrmReprintReportRPR004.setModal(true);
			laFrmReprintReportRPR004
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmReprintReportRPR004.show();
			Insets laInsets = laFrmReprintReportRPR004.getInsets();
			laFrmReprintReportRPR004.setSize(
				laFrmReprintReportRPR004.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReprintReportRPR004.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7590
			// changed setVisible to setVisibleRTS
			laFrmReprintReportRPR004.setVisibleRTS(true);
			// end defect 7590
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		RTSDate laCurrentDay = RTSDate.getCurrentDate();
		gettxtBeginDate().setDate(laCurrentDay);
		gettxtEndDate().setDate(laCurrentDay);
	}

	/**
	 * Validate Data
	 *
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		// TODO Create common validation routine for Begin, End Dates! 
		
		RTSException leRTSEx = new RTSException();

		RTSDate laBeginDate = null;
		RTSDate laEndDate = null;
		
		if (!gettxtBeginDate().isValidDate())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtBeginDate());
		}
		else
		{
			laBeginDate = gettxtBeginDate().getDate();
		}
		
		if (!gettxtEndDate().isValidDate())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEndDate());
		}
		else
		{
			laEndDate = gettxtEndDate().getDate();
		}
		
		if (!leRTSEx.isValidationError())
		{
			// defect 7385
			//if (startDate.compareTo(endDate) != -1)
			if (laBeginDate.compareTo(laEndDate) > 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtEndDate());
			}
			
			RTSDate laEarliestReprntDate =
				RTSDate.getCurrentDate().add(RTSDate.DATE, -399);
				
			if (laBeginDate.compareTo(laEarliestReprntDate) < 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtBeginDate());
			}
			// end defect 7385
			
			if (laEndDate.compareTo(RTSDate.getCurrentDate()) == 1)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtEndDate());
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;

	}
}
