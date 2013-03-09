package com.txdot.isd.rts.client.misc.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmTransactionKeyVOI001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	05/08/2002	Added request focus for bug #3815
 * K Harrell	04/23/2004	Disallow void from another workstation
 *							modify actionPerformed()
 *							defect 7040   Ver 5.2.0
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2005	Modify to do nothing if user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	04/08/2007	Cleanup; Make Void Error Handling consistent
 * 							with RTS Standards.
 * 							delete getBuilderData()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/12/2007	In development, prepopulate TransactionKey
 * 							field with OfcIssuanceNo,TransWsId,
 * 							TransAmDate
 * 							add populateTransIdInDev()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/13/2007	Make available prepopulation of TransId 
 * 							to production. 
 * 							add prepopulateTransId()
 * 							delete populateTransIdInDev()
 * 							modify setData()
 * 							defect 9413 Ver Special Plates 2
 * ---------------------------------------------------------------------
 */

/** 
 * Screen prompts user to enter a transaction id to be voided
 * 
 * @version	Special Plates	2	11/13/2007
 * @author	Bobby Tulsiani
 * <br>Creation Date:			09/05/2001 13:30:59 
 */

public class FrmTransactionKeyVOI001
	extends RTSDialogBox
	implements ActionListener
{
	
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmTransactionKeyVOI001ContentPane1 = null;
	private JLabel ivjstcLblEnter = null;
	private JLabel ivjstcLblTransactionId = null;
	private RTSInputField ivjtxtTransactionId = null;
	
	// Static 
	private static final String TXT_ENTER_FOLLOWING =
		"Enter the following:";
	private static final String TXT_TRANSID = "Transaction Id:";
	private static final String VOI001_FRM_TITLE =
		"Transaction Key      VOI001";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmTransactionKeyVOI001 aaFrmTransactionKeyVOI001;
			aaFrmTransactionKeyVOI001 = new FrmTransactionKeyVOI001();
			aaFrmTransactionKeyVOI001.setModal(true);
			aaFrmTransactionKeyVOI001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aaFrmTransactionKeyVOI001.show();
			java.awt.Insets insets =
				aaFrmTransactionKeyVOI001.getInsets();
			aaFrmTransactionKeyVOI001.setSize(
				aaFrmTransactionKeyVOI001.getWidth()
					+ insets.left
					+ insets.right,
				aaFrmTransactionKeyVOI001.getHeight()
					+ insets.top
					+ insets.bottom);
			aaFrmTransactionKeyVOI001.setVisible(true);
		}
		catch (Throwable aeThEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTransactionKeyVOI001 constructor comment.
	 */
	public FrmTransactionKeyVOI001()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmTransactionKeyVOI001 constructor comment.
	 * 
	 * @param owner java.awt.Dialog
	 */
	public FrmTransactionKeyVOI001(Dialog owner)
	{
		super(owner);
		initialize();
	}
	
	/**
	 * FrmTransactionKeyVOI001 constructor comment.
	 */
	public FrmTransactionKeyVOI001(JFrame parent)
	{
		super(parent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE 
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			//field validation
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter()
				|| aaAE.getSource() == ivjtxtTransactionId)
			{
				String lsTransactionId =
					gettxtTransactionId().getText();

				// defect 9085 
				// Make void error handling consistent
				// Validate inputted id is greater 0 and < 17
				//	if (lsTransactionId.length() == 0)
				//	{
				//		RTSException InvalidInput = new RTSException(175);
				//		InvalidInput.displayError(this);
				//		gettxtTransactionId().requestFocus();
				//		return;
				//	}

				RTSException leRTSException = new RTSException();
				if (lsTransactionId.length() != 17)
				{
					leRTSException.addException(
						new RTSException(150),
						gettxtTransactionId());
				}
				else
				{
					// defect 7040
					// disallow void from another ofcissuanceno,workstation
					int liOfcIssuanceNo =
						Integer.parseInt(
							lsTransactionId.substring(0, 3));

					int liTransWsId =
						Integer.parseInt(
							lsTransactionId.substring(3, 6));

					if (SystemProperty.getOfficeIssuanceNo()
						!= liOfcIssuanceNo
						|| SystemProperty.getWorkStationId()
							!= liTransWsId)
					{
						leRTSException.addException(
							new RTSException(352),
							gettxtTransactionId());
					}
				}
				if (leRTSException.isValidationError())
				{
					leRTSException.displayError(this);
					leRTSException.getFirstComponent().requestFocus();
					return;
				}
				// end defect 7040
				// end defect 9085 

				//Set parameters into GeneralSearchData
				GeneralSearchData lGenSearchData =
					new GeneralSearchData();
				lGenSearchData.setKey1(lsTransactionId);
				lGenSearchData.setIntKey1(
					SystemProperty.getSubStationId());
				lGenSearchData.setIntKey2(
					SystemProperty.getOfficeIssuanceNo());

				//Fire enter command	
				getController().processData(
					AbstractViewController.ENTER,
					lGenSearchData);
			}

			//If user presses Cancel
			else if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
				getController().processData(
					AbstractViewController.CANCEL,
					null);

			//If user presses Help
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
				RTSHelp.displayHelp(RTSHelp.VOI001);
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
				ivjButtonPanel1.setSize(265, 42);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.setLocation(78, 119);
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
	 * Return the FrmTransactionKeyVOI001ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFrmTransactionKeyVOI001ContentPane1()
	{
		if (ivjFrmTransactionKeyVOI001ContentPane1 == null)
		{
			try
			{
				ivjFrmTransactionKeyVOI001ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmTransactionKeyVOI001ContentPane1.setName(
					"FrmTransactionKeyVOI001ContentPane1");
				ivjFrmTransactionKeyVOI001ContentPane1.setLayout(null);
				ivjFrmTransactionKeyVOI001ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTransactionKeyVOI001ContentPane1.setMinimumSize(
					new java.awt.Dimension(425, 200));
				getFrmTransactionKeyVOI001ContentPane1().add(
					getstcLblEnter(),
					getstcLblEnter().getName());
				getFrmTransactionKeyVOI001ContentPane1().add(
					getstcLblTransactionId(),
					getstcLblTransactionId().getName());
				getFrmTransactionKeyVOI001ContentPane1().add(
					gettxtTransactionId(),
					gettxtTransactionId().getName());
				getFrmTransactionKeyVOI001ContentPane1().add(
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
		return ivjFrmTransactionKeyVOI001ContentPane1;
	}
	
	/**
	 * Return the stcLblEnter property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblEnter()
	{
		if (ivjstcLblEnter == null)
		{
			try
			{
				ivjstcLblEnter = new javax.swing.JLabel();
				ivjstcLblEnter.setSize(111, 24);
				ivjstcLblEnter.setName("stcLblEnter");
				ivjstcLblEnter.setText(TXT_ENTER_FOLLOWING);
				ivjstcLblEnter.setMaximumSize(
					new java.awt.Dimension(108, 14));
				ivjstcLblEnter.setMinimumSize(
					new java.awt.Dimension(108, 14));
				ivjstcLblEnter.setLocation(34, 23);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEnter;
	}
	
	/**
	 * Return the stcLblTransactionId property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblTransactionId()
	{
		if (ivjstcLblTransactionId == null)
		{
			try
			{
				ivjstcLblTransactionId = new javax.swing.JLabel();
				ivjstcLblTransactionId.setSize(85, 24);
				ivjstcLblTransactionId.setName("stcLblTransactionId");
				ivjstcLblTransactionId.setText(TXT_TRANSID);
				ivjstcLblTransactionId.setMaximumSize(
					new java.awt.Dimension(84, 14));
				ivjstcLblTransactionId.setMinimumSize(
					new java.awt.Dimension(84, 14));
				ivjstcLblTransactionId.setLocation(85, 68);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTransactionId;
	}
	
	/**
	 * Return the txtTransactionId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTransactionId()
	{
		if (ivjtxtTransactionId == null)
		{
			try
			{
				ivjtxtTransactionId = new RTSInputField();
				ivjtxtTransactionId.setSize(152, 24);
				ivjtxtTransactionId.setName("txtTransactionId");
				ivjtxtTransactionId.setInput(1);
				ivjtxtTransactionId.setMaxLength(17);
				// user code begin {1}
				ivjtxtTransactionId.setLocation(175, 68);
				ivjtxtTransactionId.setText("");
				ivjtxtTransactionId.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTransactionId;
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
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
			setName("FrmTransactionKeyVOI001");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(413, 198);
			setTitle(VOI001_FRM_TITLE);
			setContentPane(getFrmTransactionKeyVOI001ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * Prepopulate TransId  
	 */
	private void prepopulateTransId()
	{
			String lsTransId =
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(
							SystemProperty.getOfficeIssuanceNo()),
						String.valueOf(
							SystemProperty.getWorkStationId()),
						String.valueOf(new RTSDate().getAMDate())},
					new int[] {
						CommonConstant.LENGTH_OFFICE_ISSUANCENO,
						CommonConstant.LENGTH_TRANS_WSID,
						CommonConstant.LENGTH_TRANSAMDATE },
					CommonConstant.STR_ZERO);

			gettxtTransactionId().setText(lsTransId);

	}
	
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object aaData)
	{
		try
		{
			// defect 9413 
			prepopulateTransId();
			// end defect 9413 
			gettxtTransactionId().requestFocus();
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
