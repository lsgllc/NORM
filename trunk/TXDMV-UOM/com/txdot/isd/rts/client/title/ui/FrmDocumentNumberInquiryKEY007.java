package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmDocumentNumberInquiryKEY007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Rajangam				validations update
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
 * J Rue		03/24/2005	Change key007 to KEY007 in class name
 * 							Code Cleanup
 * 							modify class name, super
 * 							defect 6967 Ver 5.2.3
 * J Rue		03/25/2005	Set baen name to KEY007
 * 							modify initialize()
 * 							defect 6967 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/21/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	10/04/2010	delete DOCNO_MAX_LEN
 * 							delete getBuilderData(), main()
 * 							modify gettxtDocNo()
 * 							defect 10598 Ver 6.6.0     
 * ---------------------------------------------------------------------
 */
/**
 * Searches for the document based on the doc no. 
 *
 * @version	6.6.0			10/04/2010
 * @author	Marx Rajangam
 * <br>Creation Date:		12/03/2001 10:39:04
 */
public class FrmDocumentNumberInquiryKEY007
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblEnterDocumentNumber = null;
	private RTSInputField ivjtxtDocNo = null;

	// defect 10598 
	// Constants int
	// private final static int DOCNO_MAX_LEN = 17;
	// end defect 10598 

	// Constants String
	private final static String ENTER_DOCNO = "Enter Document Number";

	/**
	 * FrmDocumentNumberInquiryKEY007 constructor
	 */
	public FrmDocumentNumberInquiryKEY007()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmDocumentNumberInquiryKEY007 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDocumentNumberInquiryKEY007(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * FrmDocumentNumberInquiryKEY007 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDocumentNumberInquiryKEY007(JFrame aaParent)
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
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}
		RTSException leRTSEx = new RTSException();
		try
		{
			//Clear All Fields
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				String lsDocNo = gettxtDocNo().getText().trim();
				if (lsDocNo.length() != CommonConstant.LENGTH_DOCNO)
				{
					leRTSEx.addException(
						new RTSException(315),
						gettxtDocNo());

				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				getController().processData(
					AbstractViewController.ENTER,
					lsDocNo);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.KEY007);
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(64, 131, 296, 44);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
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
				getRTSDialogBoxContentPane().add(
					getstcLblEnterDocumentNumber(),
					getstcLblEnterDocumentNumber().getName());
				getRTSDialogBoxContentPane().add(
					gettxtDocNo(),
					gettxtDocNo().getName());
				getRTSDialogBoxContentPane().add(
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
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblEnterDocumentNumber property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterDocumentNumber()
	{
		if (ivjstcLblEnterDocumentNumber == null)
		{
			try
			{
				ivjstcLblEnterDocumentNumber = new JLabel();
				ivjstcLblEnterDocumentNumber.setName(
					"stcLblEnterDocumentNumber");
				ivjstcLblEnterDocumentNumber.setText(ENTER_DOCNO);
				ivjstcLblEnterDocumentNumber.setBounds(
					143,
					54,
					146,
					14);
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
		return ivjstcLblEnterDocumentNumber;
	}
	/**
	 * Return the txtDocNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDocNo()
	{
		if (ivjtxtDocNo == null)
		{
			try
			{
				ivjtxtDocNo = new RTSInputField();
				ivjtxtDocNo.setName("txtDocNo");
				ivjtxtDocNo.setBounds(131, 81, 167, 20);
				// defect 10598 
				ivjtxtDocNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtDocNo.setMaxLength(CommonConstant.LENGTH_DOCNO);
				// end defect 10598  
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
		return ivjtxtDocNo;
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
			// defect 6967
			//	Change Key007 to uppercase KEY007
			setName(ScreenConstant.KEY007_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(426, 214);
			setTitle(ScreenConstant.KEY007_FRAME_TITLE);
			// end defect 6967
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		// Reset the text field to blank if the previous transaction was
		// processed.  If cancel was hit on TTL042, then the previous 
		// doc no is retained in the text field.
		if (aaDataObject instanceof CompleteTransactionData)
		{
			gettxtDocNo().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtDocNo().requestFocus();
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"