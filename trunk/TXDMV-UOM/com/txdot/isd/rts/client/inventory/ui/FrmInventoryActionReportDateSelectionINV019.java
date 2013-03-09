package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryActionReportDateSelectionINV019.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Min Wang		05/13/2002	Changed the default focus to be on the date
 * 							entry field.
 *							defect 3860
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename to be frame INV019
 * 							deprecate builderData()
 * 							modify actionPerformed()
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	04/15/2005	Content Pane name also need to change
 * 							modify 
 * 			getFrmInventoryActionReportDateSelectionINV019ContentPane1()
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	08/09/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Remove key processing for button panel.
 * 							Organize imports.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/25/2009	Screen cleanup via Visual Editor.
 * 							Screen layout null vs. GridBayLayout
 * 							Implement ReportSearchData.initForClient() 
 * 							add caRptSearchData 
 * 							add validateData(), setDataToDataObject() 
 * 							delete getBuilderData()
 * 							modify actionPerformed()
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV019 prompts for which date to run the online version of the
 * Inventory Action Report.
 *
 * @version	Defect_POS_F	08/25/2009
 * @author	Sunil Govindappa
 * <br>Creation Date:		06/28/2001 09:44:44
 */

public class FrmInventoryActionReportDateSelectionINV019
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblEnterTheReportDate = null;
	private JPanel ivjFrmInventoryActionReportDateSelectionINV019ContentPane1 =
		null;
	private RTSDateField ivjtxtReportDate = null;
	private RTSDate caCurrDt = RTSDate.getCurrentDate();

	// defect 8628 
	private ReportSearchData caRptSearchData = new ReportSearchData();
	// end defect 8628 

	/**
	 * FrmInventoryActionReportDateSelectionINV019 constructor comment.
	 */
	public FrmInventoryActionReportDateSelectionINV019()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryActionReportDateSelectionINV019 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryActionReportDateSelectionINV019(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryActionReportDateSelectionINV019 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryActionReportDateSelectionINV019(JFrame aaParent)
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
			clearAllColor(this);

			// ENTER 
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// defect 8628 
				if (!validateData())
				{
					return;
				}

				setDataToDataObject();

				getController().processData(
					AbstractViewController.ENTER,
					caRptSearchData);
				// end defect 8628 
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV019);
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
				ivjButtonPanel1.setBounds(55, 123, 250, 45);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmInventoryActionReportDateSelectionINV020ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmInventoryActionReportDateSelectionINV019ContentPane1()
	{
		if (ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
			== null)
		{
			try
			{
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.setName(
					"FrmInventoryActionReportDateSelectionINV019ContentPane1");
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.setLayout(
					null);
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(365, 220));
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(365, 220));
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.add(
					getstcLblEnterTheReportDate(),
					null);
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.add(
					gettxtReportDate(),
					null);
				ivjFrmInventoryActionReportDateSelectionINV019ContentPane1
					.add(
					getButtonPanel1(),
					null);
				// user code begin {1} 
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmInventoryActionReportDateSelectionINV019ContentPane1;
	}

	/**
	 * Return the stcLblEnterTheReportDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterTheReportDate()
	{
		if (ivjstcLblEnterTheReportDate == null)
		{
			try
			{
				ivjstcLblEnterTheReportDate = new JLabel();
				ivjstcLblEnterTheReportDate.setSize(151, 20);
				ivjstcLblEnterTheReportDate.setName(
					"stcLblEnterTheReportDate");
				ivjstcLblEnterTheReportDate.setText(
					InventoryConstant.TXT_ENTER_REPORT_DATE_COLON);
				ivjstcLblEnterTheReportDate.setMaximumSize(
					new java.awt.Dimension(124, 14));
				ivjstcLblEnterTheReportDate.setMinimumSize(
					new java.awt.Dimension(124, 14));
				ivjstcLblEnterTheReportDate.setHorizontalAlignment(0);
				ivjstcLblEnterTheReportDate.setLocation(110, 32);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblEnterTheReportDate;
	}

	/**
	 * Return the txtReportDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtReportDate()
	{
		if (ivjtxtReportDate == null)
		{
			try
			{
				ivjtxtReportDate = new RTSDateField();
				ivjtxtReportDate.setSize(73, 20);
				ivjtxtReportDate.setName("txtReportDate");
				ivjtxtReportDate.setLocation(147, 75);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtReportDate;
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
			setName(ScreenConstant.INV019_FRAME_NAME);
			setSize(372, 197);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV019_FRAME_TITLE);
			setContentPane(
				getFrmInventoryActionReportDateSelectionINV019ContentPane1());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmInventoryActionReportDateSelectionINV019 laFrmInventoryActionReportDateSelectionINV019;
			laFrmInventoryActionReportDateSelectionINV019 =
				new FrmInventoryActionReportDateSelectionINV019();
			laFrmInventoryActionReportDateSelectionINV019.setModal(
				true);
			laFrmInventoryActionReportDateSelectionINV019
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryActionReportDateSelectionINV019.show();
			java.awt.Insets insets =
				laFrmInventoryActionReportDateSelectionINV019
					.getInsets();
			laFrmInventoryActionReportDateSelectionINV019.setSize(
				laFrmInventoryActionReportDateSelectionINV019
					.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryActionReportDateSelectionINV019
					.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryActionReportDateSelectionINV019
				.setVisibleRTS(
				true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * <p>The default date is Yesterday.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData == null)
		{
			gettxtReportDate().setDate(caCurrDt.add(RTSDate.DATE, -1));
		}
	}
	
	/** 
	 * Set Data to Data Object
	 * 
	 */
	private void setDataToDataObject()
	{
		caRptSearchData.initForClient(ReportConstant.ONLINE);
		RTSDate laReportDate = gettxtReportDate().getDate();
		caRptSearchData.setKey2(
			String.valueOf(laReportDate.getAMDate()));
	}

	/** 
	 * Validate Date provided
	 * 
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;
		RTSDate laRptDt = gettxtReportDate().getDate();
		RTSException leRTSExMsg = new RTSException();
		if (!gettxtReportDate().isValidDate())
		{
			leRTSExMsg.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtReportDate());
		}
		else if (
			(laRptDt.add(RTSDate.DATE, 11)).compareTo(caCurrDt) <= 0
				|| laRptDt.compareTo(caCurrDt) > 0)
		{
			leRTSExMsg.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID_11_DAYS),
				gettxtReportDate());
		}
		if (leRTSExMsg.isValidationError())
		{
			leRTSExMsg.displayError(this);
			leRTSExMsg.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;

	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,23"
