package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmManualEntryDTA009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Rajangam				validation updated
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes in
 *							actionPerformed().
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change class number and all references 
 * 							from DTA008 to DTA009.
 * 							class/Super FrmManualEntryDTA009
 * 							modify actionPerformed()
 * 							defect 6963 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3            
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/12/2006	Frame Title cleanup.  Referencing wrong 
 * 							constant.
 * 							modify initialize()
 * 							defect 7898 Ver 5.2.3  
 * J Rue		01/13/2006	Center the 2 dashes between DlrID and SeqNo
 * 							modify getstcLbldash()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	08/04/2009	Implement new DealerData.
 * 							add validateFields()  
 * 							modify setDataToDataObject(), actionPerformed()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/16/2009	DTA cleanup.
 * 							delete caDlrData 
 * 							modify setDataToDataObject()
 * 							defect 10290 Ver Defect_POS_H   
 * ---------------------------------------------------------------------
 */

/**
 * Manual key entry of dealer id and sequence number. 
 *
 * @version	Defect_POS_H 	12/16/2009
 * @author	A Yang
 * <br>Creation Date:		07/13/2001 09:50:41
 */

public class FrmManualEntryDTA009
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmManualEntryDTA009ContentPane1 = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDealerId = null;
	private JLabel ivjstcLblEntertheBatch = null;
	private JLabel ivjstcLblSequenceNo = null;
	private RTSInputField ivjtxtDealerId = null;
	private RTSInputField ivjtxtSequenceNumber = null;

	// Objects 
	private DealerTitleData caDlrTtlData = null;

	// Constants 
	private final static String DEALER_ID = "Dealer Id";
	private final static int DLRID_MAX_LEN = 3;
	private final static String ENTER_BATCH_NO =
		"Enter the Batch Number: ";
	private final static int SEQNO_MAX_LEN = 3;
	private final static String SEQUENCE_NUMBER = "Sequence Number ";
	private final static String TWO_DASHES =
		CommonConstant.STR_DASH + CommonConstant.STR_DASH;

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
			FrmManualEntryDTA009 laFrmManualEntryDTA009;
			laFrmManualEntryDTA009 = new FrmManualEntryDTA009();
			laFrmManualEntryDTA009.setModal(true);
			laFrmManualEntryDTA009
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmManualEntryDTA009.show();
			java.awt.Insets insets = laFrmManualEntryDTA009.getInsets();
			laFrmManualEntryDTA009.setSize(
				laFrmManualEntryDTA009.getWidth()
					+ insets.left
					+ insets.right,
				laFrmManualEntryDTA009.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmManualEntryDTA009.setVisibleRTS(true);
		}
		catch (Throwable leTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_EXCEPT_IN_MAIN);
			leTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	* FrmManualEntryDTA009 constructor
	*/
	public FrmManualEntryDTA009()
	{
		super();
		initialize();
	}

	/**
	 * FrmManualEntryDTA009 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmManualEntryDTA009(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmManualEntryDTA009 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmManualEntryDTA009(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is pressed
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{

		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateFields())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						caDlrTtlData);
				}
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
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.DTA009);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return ivjButtonPanel1 property value.
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
				ivjButtonPanel1.setBounds(48, 139, 273, 44);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the ivjFrmManualEntryDTA009ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmManualEntryDTA009ContentPane1()
	{
		if (ivjFrmManualEntryDTA009ContentPane1 == null)
		{
			try
			{
				ivjFrmManualEntryDTA009ContentPane1 = new JPanel();
				ivjFrmManualEntryDTA009ContentPane1.setName(
					"ivjFrmManualEntryDTA009ContentPane1");
				ivjFrmManualEntryDTA009ContentPane1.setLayout(null);
				getFrmManualEntryDTA009ContentPane1().add(
					getstcLblEntertheBatch(),
					getstcLblEntertheBatch().getName());
				getFrmManualEntryDTA009ContentPane1().add(
					getstcLblDealerId(),
					getstcLblDealerId().getName());
				getFrmManualEntryDTA009ContentPane1().add(
					gettxtDealerId(),
					gettxtDealerId().getName());
				getFrmManualEntryDTA009ContentPane1().add(
					getstcLbldash(),
					getstcLbldash().getName());
				getFrmManualEntryDTA009ContentPane1().add(
					getJLabel1(),
					getJLabel1().getName());
				getFrmManualEntryDTA009ContentPane1().add(
					gettxtSequenceNumber(),
					gettxtSequenceNumber().getName());
				getFrmManualEntryDTA009ContentPane1().add(
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
		return ivjFrmManualEntryDTA009ContentPane1;
	}

	/**
	 * Return ivjstcLblSequenceNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel1()
	{
		if (ivjstcLblSequenceNo == null)
		{
			try
			{
				ivjstcLblSequenceNo = new JLabel();
				ivjstcLblSequenceNo.setSize(108, 20);
				ivjstcLblSequenceNo.setName("ivjstcLblSequenceNo");
				ivjstcLblSequenceNo.setText(SEQUENCE_NUMBER);
				ivjstcLblSequenceNo.setLocation(200, 58);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSequenceNo;
	}

	/**
	 * Return the ivjstcLblDash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLbldash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setSize(18, 20);
				ivjstcLblDash.setName("ivjstcLblDash");
				ivjstcLblDash.setText(TWO_DASHES);
				ivjstcLblDash.setLocation(183, 87);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * Return the ivjstcLblDealerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerId()
	{
		if (ivjstcLblDealerId == null)
		{
			try
			{
				ivjstcLblDealerId = new JLabel();
				ivjstcLblDealerId.setSize(50, 20);
				ivjstcLblDealerId.setName("ivjstcLblDealerId");
				ivjstcLblDealerId.setText(DEALER_ID);
				ivjstcLblDealerId.setLocation(123, 58);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDealerId;
	}

	/**
	 * Return the ivjstcLblEntertheBatch property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEntertheBatch()
	{
		if (ivjstcLblEntertheBatch == null)
		{
			try
			{
				ivjstcLblEntertheBatch = new JLabel();
				ivjstcLblEntertheBatch.setSize(141, 20);
				ivjstcLblEntertheBatch.setName(
					"ivjstcLblEntertheBatch");
				ivjstcLblEntertheBatch.setText(ENTER_BATCH_NO);
				ivjstcLblEntertheBatch.setLocation(32, 24);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEntertheBatch;
	}

	/**
	 * Return the ivjtxtDealerId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDealerId()
	{
		if (ivjtxtDealerId == null)
		{
			try
			{
				ivjtxtDealerId = new RTSInputField();
				ivjtxtDealerId.setSize(30, 20);
				ivjtxtDealerId.setName("ivjtxtDealerId");
				ivjtxtDealerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtDealerId.setMaxLength(DLRID_MAX_LEN);
				// user code begin {1}
				ivjtxtDealerId.setLocation(143, 89);
				ivjtxtDealerId.setText(" ");
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtDealerId;
	}

	/**
	 * Return the ivjtxtSequenceNumber property value.
	 * 
	 */
	private RTSInputField gettxtSequenceNumber()
	{
		if (ivjtxtSequenceNumber == null)
		{
			try
			{
				ivjtxtSequenceNumber = new RTSInputField();
				ivjtxtSequenceNumber.setSize(30, 20);
				ivjtxtSequenceNumber.setName("ivjtxtSequenceNumber");
				ivjtxtSequenceNumber.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtSequenceNumber.setMaxLength(SEQNO_MAX_LEN);
				// user code begin {1}
				ivjtxtSequenceNumber.setLocation(200, 89);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtSequenceNumber;
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
			setName(ScreenConstant.DTA009_FRAME_NAME);
			setTitle(ScreenConstant.DTA009_FRAME_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(372, 217);
			setContentPane(getFrmManualEntryDTA009ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		// empty block of code
	}

	/**
	 * Convert data on screen to DealerTitleData
	 */
	private void setDataToDataObject()
	{
		// defect 10290 
		caDlrTtlData = new DealerTitleData();
		caDlrTtlData.setDealerId(
			Integer.parseInt(gettxtDealerId().getText()));
		caDlrTtlData.setDealerId(gettxtDealerId().getText());
		caDlrTtlData.setDealerSeqNo(gettxtSequenceNumber().getText());
		// end defect 10290 
	}

	/** 
	 * Validate fields on screen
	 */
	private boolean validateFields()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();
		String lsDealerId = gettxtDealerId().getText();
		String lsSeqNo = gettxtSequenceNumber().getText();

		if (lsDealerId.length() == 0)
		{
			// 181
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_ENTER_VALID_BATCH_NO),
				gettxtDealerId());
		}
		else if (lsDealerId.length() != DLRID_MAX_LEN)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtDealerId());
		}

		if (lsSeqNo.length() == 0)
		{
			// 181 
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_ENTER_VALID_BATCH_NO),
				gettxtSequenceNumber());
		}
		else if (lsSeqNo.length() != SEQNO_MAX_LEN)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtSequenceNumber());
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
