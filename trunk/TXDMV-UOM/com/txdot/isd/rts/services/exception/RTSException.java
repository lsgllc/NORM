package com.txdot.isd.rts.services.exception;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.txdot.isd.rts.client.desktop.RTSDeskTop;

import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*  
 * RTSException.java
 *
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------	 	
 * N Ting		09/05/2001	Add comments, change hungarian notation,
 * 							added message types, new constructor
 * N Ting		09/06/2001	Add accessors to fields, override 
 *							printStackTrace
 * N Ting		09/10/2001	Add type SERVER_DOWN and DB_DOWN
 * N Ting		09/16/2001	Add type CHAIN_EXCEPTION,
 * 							Location and Size of Msg Box based on
 * 							screen size.
 * N Ting		05/28/2002	Fix CQU100004108,change mapping for 
 *							SERVER_DOWN, DB_DOWN
 * R Rowehl		08/04/2002	Fix CQU100004572.  Write java error 
 *							exceptions to the log.
 * R Rowehl		03/06/2003	Fix CQU100005709.  allow display of details 
 *							for TR exceptions
 * R Rowehl		07/21/2003	Allow 1 string constructor to just take a 
 *							string describing the error
 *							modify RTSException(String)
 *							defect 6059
 * K Harrell	09/29/2003	Defect 6614
 *							Do not write to log on DB Down or Server 
 *							Down scenarios
 *							modified RTSException(String,Exception)
 * Min Wang		02/03/2004  If it is Windows Platform, do not set the 
 *							focus on the previous component.
 *							modify	displayError(JDialog), 
 *							displayError(JFrame)
 *							defect 6847  Ver 5.1.6
 * K Harrell	03/28/2004	remove reference to isWindowsPlatform()
 *							modify	displayError(JDialog), 
 *							displayError(JFrame)
 *							defect 6955  Ver 5.2.0 
 * Jeff S.		05/04/2005	Added constructor to handle throwable.
 * 							add RTSException()
 * 							defect 8190 Ver. 5.2.2 Fix 4
 * Ray Rowehl	02/11/2005	Change displayError to use setVisibleMsg()
 * 							modify displayError(JDialog), 
 * 								displayError(JFrame)
 * 							defect 7701 Ver 5.2.3
 * Ray Rowehl	04/06/2005	Change to remove RTS GUI Components.
 * 							Use Java equivilents
 * 							modify getFirstEnabledComponent()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3   
 * Jeff S.		06/28/2005	Added the ability to display an error msg
 * 							when you are not inside of a frame.
 * 							add caRTSDesktop, displayError(), 
 * 								getRTSDesktop(), setRTSDesktop() 
 * 							defect 8270 Ver 5.2.3 
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							If the parent frame is null when displaying
 * 							the message use displayError().
 * 							remove CTL001_TITLE
 * 							modify displayError(JFrame), 
 * 								displayError(JDialog), 
 * 								RTSException(String, String, String)
 * 							defect 8756 Ver 5.2.3
 * Ray Rowehl	08/11/2006	Dispose and null out the MsgDialog and
 * 							return a copy of the dislog return instead
 * 							of a reference.
 * 							modify displayError(),
 * 								displayError(JDialog),
 * 								displayError(JFrame),
 * 							    writeExceptionToLog()
 * 							defect 8851 Ver 5.2.4
 * Ray Rowehl	08/14/2006	Do setVisibleMsg() and dispose()
 * 							modify displayError(),
 * 								displayError(JFrame)
 * 							defect 8851 Ver 5.2.4
 * Min Wang		09/04/2008	log error message to the rtsapp.log 
 * 							for the invoice not found.
 * 							modify writeExceptionToLog()
 * 							defect 8266 Ver Defect_POS_B
 * K Harrell	12/16/2009	On add, does not assign ErrorMsgFiller if
 * 							 1st element.
 * 							modify addException() 
 * 							defect 10309 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**  
 * The RTS Exception contains various constructors for constructing
 * exceptions which display the error messages in different formats.
 *
 * @version	Defect_POS_H	12/16/2009
 * @author	Nancy Ting
 * <br>Creation Date:		08/02/2001 10:27:30
 */

public class RTSException extends Exception implements Serializable
{
	/**
	 * Message type for the RTS system
	 * Used to construct Display Message Title
	 */
	public static final String SystemErrorType = "RTS";

	/* Message Type */
	/**
	 * Error Message type - Server Down
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String SERVER_DOWN = "SD";

	/**
	 * Error Message type - DB Down
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String DB_DOWN = "DD";

	/**
	 * Error Message type - MQ Error
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String MQ_ERROR = "MQ";

	/**
	 * Error Message type - DB Down
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String MF_DOWN = "MFD";

	/**
	 * Error Message type - CTL001
	 * Icon: question
	 * Buttons: YES, NO
	 */
	public final static String CTL001 = "CTL001";

	/**
	 * Error Message type - Java Error
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String JAVA_ERROR = "JE";

	/**
	 * Error Message type - DB Error
	 * Icon: cross
	 * Buttons: OK, Details
	 */
	public final static String DB_ERROR = "DB";

	/**
	 * Error Message type - System Error
	 * Icon: none
	 * Buttons: OK
	 */
	public final static String SYSTEM_ERROR = "SE";

	/**
	 * Error Message type - Failure Message
	 * Icon: prohibited
	 * Buttons: OK
	 */
	public final static String FAILURE_MESSAGE = "FM";

	/**
	 * Error Message type - Warning Message
	 * Icon: warning
	 * Buttons: OK
	 */
	public final static String WARNING_MESSAGE = "WM";

	/**
	 * Error Message type - Information Message
	 * Icon: info
	 * Buttons: OK
	 */
	public final static String INFORMATION_MESSAGE = "IM";

	/**
	 * Error Message type - Failure Message
	 * Icon: prohibited
	 * Buttons: OK, Help
	 */
	public final static String FAILURE_HELP_MESSAGE = "FM_HLP";

	/**
	 * Error Message type - Warning Message
	 * Icon: warning
	 * Buttons: OK, Help
	 */
	public final static String WARNING_HELP_MESSAGE = "WM_HLP";

	/**
	 * Error Message type - Information Message
	 * Icon: info
	 * Buttons: OK, Help
	 */
	public final static String INFORMATION_HELP_MESSAGE = "IM_HLP";

	/**
	 * Error Message type - Failure Message
	 * Icon: prohibited
	 * Buttons: Yes, No, Cancel
	 */
	public final static String FAILURE_VERIFICATION = "FV";

	/**
	 * Error Message type - Warning Message
	 * Icon: warning
	 * Buttons: Yes, No, Cancel
	 */
	public final static String WARNING_VERIFICATION = "WV";

	/**
	 * Error Message type - Information Message
	 * Icon: info
	 * Buttons: Yes, No, Cancel
	 */
	public final static String INFORMATION_VERIFICATION = "IV";

	/**
	 * Error Message type - Decision Message
	 * Icon: question
	 * Buttons: Yes, No, Cancel
	 */
	public final static String DECISION_VERIFICATION = "DV";

	/**
	 * Error Message type - Confirmation Message
	 * Icon: confirm
	 * Buttons: Yes, No, Cancel
	 */
	public final static String CONFIRMATION_VERIFICATION = "CV";

	/**
	 * Error Message type - Failure Message
	 * Icon: prohibited
	 * Buttons: Enter, Cancel, Help
	 */
	public final static String FAILURE_VALIDATION = "FVA";

	/**
	 * Error Message type - Warning Message
	 * Icon: warning
	 * Buttons: Enter, Cancel, Help
	 */
	public final static String WARNING_VALIDATION = "WVA";

	/**
	 * Error Message type - Information Message
	 * Icon: info
	 * Buttons: Enter, Cancel, Help
	 */
	public final static String INFORMATION_VALIDATION = "IVA";

	/**
	 * Error Message type - Decision Message
	 * Icon: info
	 * Buttons: Enter, Cancel, Help
	 */
	public final static String DECISION_VALIDATION = "DVA";

	/**
	 * Error Message type - Confirmation Message
	 * Icon: confirm
	 * Buttons: Enter, Cancel, Help
	 */
	public final static String CONFIRMATION_VALIDATION = "CVA";
	/**
	 * Error Message type - Exit
	 * Icon: info
	 * Buttons: Exit
	 */
	public final static String EXIT_LOOP = "EL";
	/**
	 * Error Message type - Exit
	 * Icon: info
	 * Buttons: Exit
	 */
	public final static String TR_ERROR = "TR";

	/**
	 * Chain Exception
	 *
	 * Using the constructor RTSException(Vector aarrRTSException),
	 * the chain of RTSException will be displayed when displayError() 
	 * is being called.  When the array of RTSException is exhausted, 
	 * the displayError will return RTSException.OK to the caller.
	 */
	private final static String CHAIN = "C";

	/* User Response, return to user after displayError */
	/**
	 * Button Type - OK
	 */
	public final static int OK = 1;

	/**
	 * Button Type - YES
	 */
	public final static int YES = 2;

	/**
	 * Button Type - NO
	 */
	public final static int NO = 3;

	/**
	 * Button Type - CANCEL
	 */
	public final static int CANCEL = 4;

	/**
	 * Button Type - ENTER
	 */
	public final static int ENTER = 5;

	/**
	 * Button Type - EXIT
	 */
	public final static int EXIT = 6;

	/**
	 * Button Type - CONTINUE
	 */
	public final static int CONTINUE = 7;

	//internal use only.  display types (named iconName_buttonNameList)
	private final static String CROSS_OK_DETAILS = "cross_OK_DETAILS";
	private final static String NONE_OK = "none_OK";
	//when RTS code not found

	private final static String PROHIBITED_OK = "prohibited_OK";
	private final static String PROHIBITED_OK_HELP =
		"prohibited_OK_HELP";
	private final static String WARNING_OK = "warning_OK";
	private final static String WARNING_OK_HELP = "warning_OK_HELP";
	private final static String INFORMATION_OK = "info_OK";
	private final static String INFORMATION_OK_HELP = "info_OK_HELP";

	private final static String PROHIBITED_YES_NO_CANCEL =
		"prohibited_YES_NO_CANCEL";
	private final static String WARNING_YES_NO_CANCEL =
		"warning_YES_NO_CANCEL";
	private final static String INFORMATION_YES_NO_CANCEL =
		"info_YES_NO_CANCEL";
	private final static String DECISION_YES_NO_CANCEL =
		"question_YES_NO_CANCEL";
	private final static String CONFIRMATION_YES_NO_CANCEL =
		"confirm_YES_NO_CANCEL";

	private final static String PROHIBITED_ENTER_CANCEL_HELP =
		"prohibited_ENTER_CANCEL_HELP";
	private final static String WARNING_ENTER_CANCEL_HELP =
		"warning_ENTER_CANCEL_HELP";
	private final static String INFORMATION_ENTER_CANCEL_HELP =
		"info_ENTER_CANCEL_HELP";
	private final static String DECISION_ENTER_CANCEL_HELP =
		"question_ENTER_CANCEL_HELP";
	private final static String CONFIRMATION_ENTER_CANCEL_HELP =
		"confirm_ENTER_CANCEL_HELP";
	private final static String QUESTION_YES_NO = "question_YES_NO";
	private final static String INFORMATION_EXIT = "info_EXIT";

	private static Hashtable shtMsgTypeToDisplay = new Hashtable();
	static {
		shtMsgTypeToDisplay.put(JAVA_ERROR, CROSS_OK_DETAILS);
		shtMsgTypeToDisplay.put(DB_ERROR, CROSS_OK_DETAILS);
		shtMsgTypeToDisplay.put(SERVER_DOWN, WARNING_OK);
		shtMsgTypeToDisplay.put(DB_DOWN, WARNING_OK);
		shtMsgTypeToDisplay.put(MF_DOWN, WARNING_OK);
		shtMsgTypeToDisplay.put(TR_ERROR, CROSS_OK_DETAILS);

		shtMsgTypeToDisplay.put(SYSTEM_ERROR, NONE_OK);

		shtMsgTypeToDisplay.put(FAILURE_MESSAGE, PROHIBITED_OK);
		shtMsgTypeToDisplay.put(WARNING_MESSAGE, WARNING_OK);
		shtMsgTypeToDisplay.put(INFORMATION_MESSAGE, INFORMATION_OK);

		shtMsgTypeToDisplay.put(
			FAILURE_HELP_MESSAGE,
			PROHIBITED_OK_HELP);
		shtMsgTypeToDisplay.put(WARNING_HELP_MESSAGE, WARNING_OK_HELP);
		shtMsgTypeToDisplay.put(
			INFORMATION_HELP_MESSAGE,
			INFORMATION_OK_HELP);

		shtMsgTypeToDisplay.put(
			FAILURE_VERIFICATION,
			PROHIBITED_YES_NO_CANCEL);
		shtMsgTypeToDisplay.put(
			WARNING_VERIFICATION,
			WARNING_YES_NO_CANCEL);
		shtMsgTypeToDisplay.put(
			INFORMATION_VERIFICATION,
			INFORMATION_YES_NO_CANCEL);
		shtMsgTypeToDisplay.put(
			DECISION_VERIFICATION,
			DECISION_YES_NO_CANCEL);
		shtMsgTypeToDisplay.put(
			CONFIRMATION_VERIFICATION,
			CONFIRMATION_YES_NO_CANCEL);

		shtMsgTypeToDisplay.put(
			FAILURE_VALIDATION,
			PROHIBITED_ENTER_CANCEL_HELP);
		shtMsgTypeToDisplay.put(
			WARNING_VALIDATION,
			WARNING_ENTER_CANCEL_HELP);
		shtMsgTypeToDisplay.put(
			INFORMATION_VALIDATION,
			INFORMATION_ENTER_CANCEL_HELP);
		shtMsgTypeToDisplay.put(
			DECISION_VALIDATION,
			DECISION_ENTER_CANCEL_HELP);
		shtMsgTypeToDisplay.put(
			CONFIRMATION_VALIDATION,
			CONFIRMATION_ENTER_CANCEL_HELP);

		shtMsgTypeToDisplay.put(
			ErrorMessagesData.FATAL_PROCESSING,
			PROHIBITED_OK_HELP);
		shtMsgTypeToDisplay.put(
			ErrorMessagesData.FAILED_VALIDATION,
			PROHIBITED_OK_HELP);
		shtMsgTypeToDisplay.put(
			ErrorMessagesData.INFORMATION,
			INFORMATION_OK_HELP);
		shtMsgTypeToDisplay.put(
			ErrorMessagesData.WARNING,
			WARNING_OK_HELP);
		shtMsgTypeToDisplay.put(
			ErrorMessagesData.CONFIRMATION,
			CONFIRMATION_YES_NO_CANCEL);
		shtMsgTypeToDisplay.put(CTL001, QUESTION_YES_NO);
		shtMsgTypeToDisplay.put(EXIT_LOOP, INFORMATION_EXIT);

	}

	/**
	 * Message Type.  Each message type corresponds to a predefined
	 * display type
	 */
	protected String csMsgType = "";

	/**
	 * code from RTS Error
	 */
	protected int ciCode;
	protected String csMessage = "";
	//used for JAVA_ERROR & DB_ERROR (stack trace)
	protected String csDetailMsg = "";
	//used for error code exception
	String[] carrErrorMsgFiller;

	/**
	* Used to populate the title of the exception dialog
	*/
	protected String csTitle = "UNKNOWN";
	protected transient MsgDialog caMsgDialog;

	/**
	 * RTSException stack
	 */
	protected Vector cvRTSException = null;

	/**
	 * Component stack. List of components which have error when enter 
	 * was pressed.
	 */
	protected Vector cvComponents = new Vector();

	/**
	 * Error Color. This is the background color for the components if 
	 * there is any input error.
	 */
	public static Color ERR_COLOR = new Color(255, 0, 0);

	//Message to DB down, Server down or MF Down
	public static final String DB_DOWN_MSG =
		"Database is currently down.";
	public static final String SERVER_DOWN_MSG =
		"Server is currently down.";
	public static final String MF_DOWN_MSG =
		"Mainframe is currently unavailable.";

	private final static long serialVersionUID = 8252385973628178462L;
	private int ciDefaultKey;

	private transient Component caComp = null;
	//this component is used to return back the focus in OS/2
	private boolean cbHTML = false;
	private int ciCount; //for exit loop timer count, in milliseconds

	private boolean cbUserSuppliedMsgLoc;
	private int ciMsgLocX;
	private int ciMsgLocY;

	private boolean cbUserSuppliedMsgDimension;
	private int ciWidth;
	private int ciHeight;

	private String csAnchor;
	public static final String CENTER_HORIZONTAL = "horizontal";
	public static final String CENTER_VERTICAL = "vertical";

	private int ciBeep;
	public static final int BEEP = 1;
	public static final int NO_BEEP = 2;

	private String csHelpURL;
	
	// defect 8270
	// Used to display an error tied to the desktop when you get an
	// exception and we are not in a frame.
	private static RTSDeskTop caRTSDesktop;
	// end defect 8270
	
	/**
	 * Default Constructor
	 */
	public RTSException()
	{
		super();
	}
	/**
	 * Construct the error dialog using the error code
	 * @param aiErrorCode error code
	 */
	public RTSException(int aiErrorCode)
	{
		ciCode = aiErrorCode;
	}
	/**
	 * Construct the exception using the error code, and fill in the 
	 * ? in the message with the array string provided
	 * @param aiErrorCode error code
	 * @param aarrErrorMsgFiller array of string fillers
	 */
	public RTSException(int aiErrorCode, String[] aarrErrorMsgFiller)
	{
		ciCode = aiErrorCode;
		carrErrorMsgFiller = aarrErrorMsgFiller;

	}
	/**
	 * Exception for Java or DB Error
	 * @param asMsgType the message type 
	 * @param aException the exception
	 */
	public RTSException(String asMsgType)
	{
		super();

		csMsgType = asMsgType;
		csTitle = "";

		if (asMsgType.equals(MF_DOWN))
		{
			csMessage = MF_DOWN_MSG;
		}
		else if (asMsgType.equals(SERVER_DOWN))
		{
			csMessage = SERVER_DOWN_MSG;
		}
		else if (asMsgType.equals(DB_DOWN))
		{
			csMessage = DB_DOWN_MSG;
		}
		// defect 6059
		// allow a message of string to be passed in
		else if (
			!asMsgType.equals(MQ_ERROR)
				&& !asMsgType.equals(JAVA_ERROR)
				&& !asMsgType.equals(DB_ERROR)
				&& !asMsgType.equals(SYSTEM_ERROR)
				&& !asMsgType.equals(FAILURE_MESSAGE)
				&& !asMsgType.equals(WARNING_MESSAGE)
				&& !asMsgType.equals(INFORMATION_MESSAGE)
				&& !asMsgType.equals(FAILURE_HELP_MESSAGE)
				&& !asMsgType.equals(FAILURE_VERIFICATION)
				&& !asMsgType.equals(WARNING_VERIFICATION)
				&& !asMsgType.equals(INFORMATION_VERIFICATION)
				&& !asMsgType.equals(DECISION_VERIFICATION)
				&& !asMsgType.equals(CONFIRMATION_VERIFICATION)
				&& !asMsgType.equals(FAILURE_VALIDATION)
				&& !asMsgType.equals(WARNING_VALIDATION)
				&& !asMsgType.equals(INFORMATION_VALIDATION)
				&& !asMsgType.equals(DECISION_VALIDATION)
				&& !asMsgType.equals(CONFIRMATION_VALIDATION)
				&& !asMsgType.equals(EXIT_LOOP)
				&& !asMsgType.equals(TR_ERROR))
		{
			csMsgType = SYSTEM_ERROR;
			csMessage = asMsgType;
		}
		// end defect 6059
		else
		{
			csMessage = "ERROR!";
		}

		// call write to log
		writeExceptionToLog();
	}
	/**
	 * Exception for Java or DB Error
	 * 
	 * @param asMsgType the message type 
	 * @param aeException the exception
	 */
	public RTSException(String asMsgType, Exception aeException)
	{
		super();
		csMsgType = asMsgType;
		boolean lbWriteToLog = true;
		
		if (csMsgType.equals(DB_DOWN) || csMsgType.equals(SERVER_DOWN))
		{
			csMessage =
				"There is a problem with the server connection.  " +
				"Some of the functionality will not be available in " +
				"offline mode";
			csTitle = "ERROR";
			lbWriteToLog = false;
		}
		else
		{
			csTitle = "System Error";
			csMessage =
				"A System Error has occurred. \n\nPlease contact your" +
				" System Administrator.";
		}

		if (aeException instanceof RTSException
			&& ((RTSException) aeException).getCode() != 0)
		{
			ErrorMessagesData laErrorMessagesData =
				ErrorMessagesCache.getErrMsg(
					((RTSException) aeException).getCode());
			if (laErrorMessagesData != null)
			{
				csDetailMsg = laErrorMessagesData.getErrMsgDesc();
			}

		}
		else
		{
			StringWriter laStringWriter = new StringWriter();
			PrintWriter laPrintWriter = new PrintWriter(laStringWriter);
			aeException.printStackTrace(laPrintWriter);
			csDetailMsg = laStringWriter.toString();
		}

		// call write to log
		if (lbWriteToLog)
		{
			writeExceptionToLog();
		}
	}
	/**
	 * Exception for Java or DB Error with the ability to send throwable
	 * 
	 * @param asMsgType the message type 
	 * @param aeThrowable the exception
	 */
	public RTSException(String asMsgType, Throwable aeThrowable)
	{
		super();
		csMsgType = asMsgType;
		boolean lbWriteToLog = true;
		
		if (csMsgType.equals(DB_DOWN) || csMsgType.equals(SERVER_DOWN))
		{
			csMessage =
				"There is a problem with the server connection.  " +
				"Some of the functionality will not be available in " +
				"offline mode";
			csTitle = "ERROR";
			lbWriteToLog = false;
		}
		else
		{
			csTitle = "System Error";
			csMessage =
				"A System Error has occurred. \n\nPlease contact your" +
				" System Administrator.";
		}

		StringWriter laStringWriter = new StringWriter();
		PrintWriter laPrintWriter = new PrintWriter(laStringWriter);
		aeThrowable.printStackTrace(laPrintWriter);
		csDetailMsg = laStringWriter.toString();

		// call write to log
		if (lbWriteToLog)
		{
			writeExceptionToLog();
		}
	}
	/**
	 * Construct the exception by specifying the message type (which 
	 * corresponds to a specific dialog box type), message and title
	 * 
	 * @param asMsgType message type
	 * @param asMsg the message
	 * @param asTitle the title
	 */
	public RTSException(String asMsgType, String asMsg, String asTitle)
	{
		super();
		csMsgType = asMsgType;
		csMessage = asMsg;
		csTitle = asTitle;
		if ((asTitle == null || asTitle.trim().equals(""))
			&& asMsgType.equals(CTL001))
		{
			// defect 8756
			// Used common constant for CTL001 title
			//csTitle = "CONFIRM ACTION   CTL001";
			csTitle = ScreenConstant.CTL001_FRM_TITLE;
			// end defect 8756
		}

	}
	/**
	 * Construct the exception by specifying the message type (which 
	 * corresponds to a specific dialog box type), message and title.
	 * 
	 * <br>If abHTML is true, then the textarea will in HTML format.
	 * 
	 * @param asMsgType message type
	 * @param asMsg the message
	 * @param asTitle the title
	 */
	public RTSException(
		String asMsgType,
		String asMsg,
		String asTitle,
		boolean abHTML)
	{
		this(asMsgType, asMsg, asTitle);
		cbHTML = abHTML;
	}
	/**
	 * Construct the exception by specifying the message type (which 
	 * corresponds to a specific dialog box type), message and title
	 * 
	 * <br>If abHTML is true, then the textarea will in HTML format
	 * 
	 * @param asMsgType message type
	 * @param asMsg the message
	 * @param asTitle the title
	 * @param abHTML
	 * @param aiCountMs int
	 */
	public RTSException(
		String asMsgType,
		String asMsg,
		String asTitle,
		boolean abHTML,
		int aiCountMs)
	{
		this(asMsgType, asMsg, asTitle, abHTML);
		ciCount = aiCountMs;
	}
	/**
	* Chain Exception
	*
	* The chain of RTSException will be displayed when displayError() 
	* is being called.
	* When all the elements in the RTSException Vector is displayed 
	* exhausted, control will be returned back to the user.
	*
	* <P>Note that you should add the RTSExceptions in the vector using
	* addElement() to ensure that the RTSExceptions is inserted in 
	* sequential order.
	*
	* <P>Example:<br>
	* <Pre>
	* Vector exceptionVector = new Vector();
	* if (input1.equals("")){
	* 	exceptionVector.addElement(new RTSException(1));
	* }
	* if (input2.equals("")){
	* 	exceptionVector.addElement(new RTSException(2));
	* }
	* if (input3.equals("")){
	* 	exceptionVector.addElement(new RTSException(3));
	* }
	*
	* if (exceptionVector.size() > 0)
	* 	throw new RTSException(exceptionVector());
	* </Pre>
	*
	* @param avRTSException a vector of RTSException
	*/
	public RTSException(Vector avRTSException)
	{
		csMsgType = CHAIN;
		cvRTSException = avRTSException;
	}
	/**
	 * This method is used to get the reference to the desktop.
	 * 
	 * @return RTSDeskTop
	 */
	public static RTSDeskTop getRTSDesktop()
	{
		return caRTSDesktop;
	}

	/**
	 * This method sets the Desktop in this class.  The dektop is used
	 * to tie msgdialog to the parent.
	 * 
	 * @param aaRTSDesktop
	 */
	public static void setRTSDesktop(RTSDeskTop aaRTSDesktop)
	{
		caRTSDesktop = aaRTSDesktop;
	}
	/**
	 * addException to this class
	 * 
	 * @param aeRTSEx RTSException
	 * @param aaComponent Component
	 */
	public void addException(RTSException aeRTSEx, Component aaComponent)
	{
		if (aeRTSEx == null)
		{
			return;
		}
		//If this is the first message, save it for displaying.
		if (cvComponents.size() < 1)
		{
			this.ciCode = aeRTSEx.ciCode;
			this.csDetailMsg = aeRTSEx.csDetailMsg;
			this.csMessage = aeRTSEx.csMessage;
			this.csMsgType = aeRTSEx.csMsgType;
			this.csTitle = aeRTSEx.csTitle;
			// defect 10309 
			this.carrErrorMsgFiller = aeRTSEx.carrErrorMsgFiller;
			// end defect 10309  
		}
		cvComponents.addElement(aaComponent);

	}
	/**
	 * Change the color of components
	 * 
	 */
	private void changeColor()
	{

		int liSize = cvComponents.size();
		for (int i = 0; i < liSize; i++)
		{
			Component laComp = (Component) cvComponents.elementAt(i);
			//process for radio button.
			if (laComp instanceof JRadioButton)
			{
				((JRadioButton) laComp).setBackground(ERR_COLOR);
				((JRadioButton) laComp).setForeground(Color.white);
			}

			//process for check boxes.
			if (laComp instanceof JCheckBox)
			{
				((JCheckBox) laComp).setBackground(ERR_COLOR);
				((JCheckBox) laComp).setForeground(Color.white);

			}
			//process for Labels.
			if (laComp instanceof JTextField)
			{
				((JTextField) laComp).setBackground(ERR_COLOR);
				((JTextField) laComp).setForeground(Color.white);
			}
			//process for combo boxes.
			if (laComp instanceof JComboBox)
			{
				((JComboBox) laComp).setBackground(ERR_COLOR);
				((JComboBox) laComp).setForeground(Color.white);
			}

		}

	}
	/**
	 * Create the error title based on the code
	 * 
	 * @return the error title
	 * @param aiErrorCode the error code
	 */
	public static String createErrorTitle(int aiErrorCode)
	{
		int liTitleLength = 9;
		String lsTitle = RTSException.SystemErrorType;
		int liCodeLength = String.valueOf(aiErrorCode).length();
		int liFillerLength =
			liTitleLength - (lsTitle.length() + liCodeLength);
		for (int i = 0; i < liFillerLength; i++)
		{
			lsTitle += "0";
		}
		lsTitle += aiErrorCode;
		return lsTitle;
	}
	/**
	 * This method displays the message dialog and returns the code that 
	 * indicates the user selection. i.e. whether user has pressed yes, 
	 * no cancel etc. on the dialog.
	 * 
	 * @return the button pressed 
	 * 
	 * @param aJDialog the parent JDialog
	 */
	public int displayError(JDialog aaJDialog)
	{
		
		// defect 8756
		// this was done so that no null parent frames are used.
		if (aaJDialog == null)
		{
			return displayError();
		}
		// end defect 8756

		// Get the component which has the focus. This method call 
		// will set the fc variable.
		getFocusComponent(aaJDialog);

		if (ciCode != 0)
		{
			populateErrorMsg();
		}

		// change the color of the components which have been added in 
		// the existing.
		changeColor();
		//
		if (csMsgType.equals(ErrorMessagesData.LOG))
		{
			return RTSException.OK;
		}
		else if (csMsgType.equals(RTSException.CHAIN))
		{
			int liReturnStatus = OK;
			if (cvRTSException != null)
			{
				for (int i = 0; i < cvRTSException.size(); i++)
				{
					liReturnStatus =
						(
							(RTSException) cvRTSException.get(
								i)).displayError(
							aaJDialog);
				}
			}

			return liReturnStatus;
		}
		else
		{
			boolean lbModal = true;

			if (!shtMsgTypeToDisplay.containsKey(csMsgType))
			{
				csMessage =
					"The message type: "
						+ csMsgType
						+ " does not exist.";
				csMsgType = NONE_OK;
				csTitle = "Message Type Not Found";

			}

			String lsDisplayType =
				(String) shtMsgTypeToDisplay.get(csMsgType);

			if (lsDisplayType.equals(RTSException.CROSS_OK_DETAILS))
			{
				if (aaJDialog == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);

				}
				else
				{
					caMsgDialog =
						new MsgDialog(
							aaJDialog,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);

				}
			}
			else
			{
				if (aaJDialog == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);

				}
				else
				{
					caMsgDialog =
						new MsgDialog(
							aaJDialog,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);

				}

			}
			if (lsDisplayType.equals("info_EXIT"))
			{
				caMsgDialog.setTimer(true);
				caMsgDialog.setTimeCount(ciCount);
			}

			if (cbUserSuppliedMsgLoc)
			{
				if (csAnchor != null)
				{
					if (csAnchor.equals(CENTER_HORIZONTAL))
					{
						caMsgDialog.setLocation(
							(int) caMsgDialog.getLocation().getX(),
							ciMsgLocY);
					}
					else if (csAnchor.equals(CENTER_VERTICAL))
					{
						caMsgDialog.setLocation(
							ciMsgLocX,
							(int) caMsgDialog.getLocation().getY());
					}

				}
				else
				{
					caMsgDialog.setLocation(ciMsgLocX, ciMsgLocY);
				}
			}

			if (cbUserSuppliedMsgDimension)
			{
				caMsgDialog.setSize(ciWidth, ciHeight);
			}

			if (emitSound())
			{
				caMsgDialog.setBeep(true);
			}

			caMsgDialog.setHelpString(csHelpURL);

			// defect 7701
			// use the msgDialog's setVisible
			caMsgDialog.setVisibleMsg(true);
			// end defect 7701

			if (caComp == null)
			{
				// If there was no component which had focus, 
				// get first component or enter button which is enabled 
				// and try to get enter button also.
				getFirstEnabledComponent(aaJDialog);
			}
			
			// defect 8851
			// return an independent variable from dialog.
			int liReturnStatus = caMsgDialog.getReturnStatus();
			
			// null out the dialog if it is still here.
			if (caMsgDialog != null)
			{
				caMsgDialog.setVisibleMsg(false);
				caMsgDialog.dispose();
				caMsgDialog = null;
			}
			
			return liReturnStatus;
			//return caMsgDialog.getReturnStatus();
			// end defect 8851
		}

	}
	/**
	 * This method displays the message dialog and returns the code that 
	 * indicates the user selection. i.e. whether user has pressed yes, 
	 * no cancel etc. on the dialog.
	 * 
	 * @return the button pressed 
	 * @param aaJFrame the parent JFrame
	 */
	public int displayError(JFrame aaJFrame)
	{
		
		// defect 8756
		// this was done so that no null parent frames are used.
		if (aaJFrame == null)
		{
			return displayError();
		}
		// end defect 8756
		
		// Get the component which has the focus. This method call will
		// set the fc variable.
		getFocusComponent(aaJFrame);
		if (ciCode != 0)
		{
			populateErrorMsg();
		}
		// change the color of the components which have been added in 
		// the existing.
		changeColor();
		//
		if (csMsgType.equals(ErrorMessagesData.LOG))
		{
			return RTSException.OK;
		}
		else if (csMsgType.equals(RTSException.CHAIN))
		{
			int lReturnStatus = OK;
			if (cvRTSException != null)
			{
				for (int i = 0; i < cvRTSException.size(); i++)
				{
					lReturnStatus = ((RTSException) cvRTSException.get(
									i)).displayError(aaJFrame);
				}
			}
			return lReturnStatus;
		}
		else
		{
			boolean lbModal = true;
			if (!shtMsgTypeToDisplay.containsKey(csMsgType))
			{
				csMessage =
					"The message type: "
						+ csMsgType
						+ " does not exist.";
				csMsgType = NONE_OK;
				csTitle = "Message Type Not Found";
			}
			String lsDisplayType =
				(String) shtMsgTypeToDisplay.get(csMsgType);
			if (lsDisplayType.equals(RTSException.CROSS_OK_DETAILS))
			{
				if (aaJFrame == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
				else
				{
					caMsgDialog =
						new MsgDialog(
							aaJFrame,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
			}
			else
			{
				if (aaJFrame == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
				else
				{
					caMsgDialog =
						new MsgDialog(
							aaJFrame,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
			}
			if (lsDisplayType.equals("info_EXIT"))
			{
				caMsgDialog.setTimer(true);
				caMsgDialog.setTimeCount(ciCount);
			}
			if (cbUserSuppliedMsgLoc)
			{
				if (csAnchor != null)
				{
					if (csAnchor.equals(CENTER_HORIZONTAL))
					{
						caMsgDialog.setLocation(
							(int) caMsgDialog.getLocation().getX(),
							ciMsgLocY);
					}
					else if (csAnchor.equals(CENTER_VERTICAL))
					{
						caMsgDialog.setLocation(
							ciMsgLocX,
							(int) caMsgDialog.getLocation().getY());
					}
				}
				else
				{
					caMsgDialog.setLocation(ciMsgLocX, ciMsgLocY);
				}
			}
			if (cbUserSuppliedMsgDimension)
			{
				caMsgDialog.setSize(ciWidth, ciHeight);
			}
			if (emitSound())
			{
				caMsgDialog.setBeep(true);
			}
			// defect 7701
			caMsgDialog.setVisibleMsg(true);
			// end defect 7701
			if (caComp == null)
			{
				//If there was no component which had focus, 
				//get first component which is enabled.
				getFirstEnabledComponent(aaJFrame);
			}
			
			// defect 8851
			// return an independent variable from dialog.
			int liReturnStatus = caMsgDialog.getReturnStatus();
			
			// null out the dialog if it is still here.
			if (caMsgDialog != null)
			{
				caMsgDialog.setVisibleMsg(false);
				caMsgDialog.dispose();
				caMsgDialog = null;
			}
			
			return liReturnStatus;
			//return caMsgDialog.getReturnStatus();
			// end defect 8851
		}
	}
	/**
	 * This method displays the message dialog and returns the code that 
	 * indicates the user selection. i.e. whether user has pressed yes, 
	 * no cancel etc. on the dialog.
	 * 
	 * This method will always tie the Message Dialog to the desktop. It
	 * is to be only used when you don't have a frame to tie to.
	 * 
	 * @return int 
	 */
	public int displayError()
	{		
		if (ciCode != 0)
		{
			populateErrorMsg();
		}
		if (csMsgType.equals(ErrorMessagesData.LOG))
		{
			return RTSException.OK;
		}
		else
		{
			boolean lbModal = true;
			if (!shtMsgTypeToDisplay.containsKey(csMsgType))
			{
				csMessage =
					"The message type: "
						+ csMsgType
						+ " does not exist.";
				csMsgType = NONE_OK;
				csTitle = "Message Type Not Found";
			}
			String lsDisplayType =
				(String) shtMsgTypeToDisplay.get(csMsgType);
			if (lsDisplayType.equals(RTSException.CROSS_OK_DETAILS))
			{
				if (caRTSDesktop == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
				else
				{
					
					caMsgDialog =
						new MsgDialog(
							(JFrame)caRTSDesktop,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							csDetailMsg,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
			}
			else
			{
				if (caRTSDesktop == null)
				{
					caMsgDialog =
						new MsgDialog(
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
				else
				{
					caMsgDialog =
						new MsgDialog(
							(JFrame)caRTSDesktop,
							lbModal,
							lsDisplayType,
							csTitle,
							csMessage,
							ciCode,
							ciDefaultKey,
							cbHTML);
				}
			}
			if (lsDisplayType.equals("info_EXIT"))
			{
				caMsgDialog.setTimer(true);
				caMsgDialog.setTimeCount(ciCount);
			}
			if (cbUserSuppliedMsgLoc)
			{
				if (csAnchor != null)
				{
					if (csAnchor.equals(CENTER_HORIZONTAL))
					{
						caMsgDialog.setLocation(
							(int) caMsgDialog.getLocation().getX(),
							ciMsgLocY);
					}
					else if (csAnchor.equals(CENTER_VERTICAL))
					{
						caMsgDialog.setLocation(
							ciMsgLocX,
							(int) caMsgDialog.getLocation().getY());
					}
				}
				else
				{
					caMsgDialog.setLocation(ciMsgLocX, ciMsgLocY);
				}
			}
			if (cbUserSuppliedMsgDimension)
			{
				caMsgDialog.setSize(ciWidth, ciHeight);
			}
			if (emitSound())
			{
				caMsgDialog.setBeep(true);
			}
			// defect 7701
			caMsgDialog.setVisibleMsg(true);
			// end defect 7701
			
			// defect 8851
			// return an independent variable from dialog.
			int liReturnStatus = caMsgDialog.getReturnStatus();
			
			// null out the dialog if it is still here.
			if (caMsgDialog != null)
			{
				caMsgDialog.setVisibleMsg(false);
				caMsgDialog.dispose();
				caMsgDialog = null;
			}
			
			return liReturnStatus;
			//return caMsgDialog.getReturnStatus();
			// end defect 8851
		}
	}
	/**
	 * Indicate whether sound will be emitted
	 * 
	 * @return boolean
	 */
	private boolean emitSound()
	{
		if (ciBeep != 0)
		{
			//user specified beep or not
			if (ciBeep == BEEP)
			{
				return true;
			}
			else if (ciBeep == NO_BEEP)
			{
				return false;
			}
		}

		if (csMsgType != null && csMsgType.equals(CTL001))
		{
			return true;
		}

		if (ciCode != 0)
		{
			ErrorMessagesData laErrorMsgData =
				ErrorMessagesCache.getErrMsg(ciCode);
			if (laErrorMsgData != null
				&& laErrorMsgData.getErrMsgCat() != null)
			{
				if (laErrorMsgData
					.getErrMsgCat()
					.equals(ErrorMessagesData.FATAL_PROCESSING)
					|| laErrorMsgData.getErrMsgCat().equals(
						ErrorMessagesData.FAILED_VALIDATION)
					|| laErrorMsgData.getErrMsgCat().equals(
						ErrorMessagesData.WARNING))
				{
					return true;
				}
			}
		}

		return false;
	}
	/**
	 * Get the error code
	 * 
	 * @return int error code
	 */
	public int getCode()
	{
		return ciCode;
	}
	/**
	 * Get the detail message
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDetailMsg()
	{
		return csDetailMsg;
	}
	/**
	 * Get the first component in the added list of exception
	 * 
	 * @return java.awt.Component
	 */
	public Component getFirstComponent()
	{
		if (cvComponents.size() > 0)
		{
			return (Component) cvComponents.elementAt(0);
		}
		else
		{
			return null;
		}
	}
	/**
	 * This method sets the caComp variable with the first enabled 
	 * component in the frame.  This method also sets the value of 
	 * enter button of the calling form, if there is any and is visible.
	 * 
	 * @param aaComp
	 */
	public void getFirstEnabledComponent(Component aaComp)
	{
		if (aaComp instanceof Container)
		{

			Container laCont = (Container) aaComp;

			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getFirstEnabledComponent(larrChildren[i]);
			}
		}
		if (aaComp == null)
		{
			return;
		}

		// defect 7885
		//Get the enter button.
		if (aaComp.isEnabled() && aaComp instanceof JButton)
		{
			JButton laRTSBtn = ((JButton) aaComp);
			if (laRTSBtn.getText().equals("Enter"))
			{
				caComp = laRTSBtn;
				return;
			}
		}
		// The first focusable component or enter button is already 
		// selected, return.
		if (caComp != null)
		{
			return;
		}

		if (aaComp.isEnabled()
			&& (aaComp instanceof JTable
				|| aaComp instanceof JButton
				|| aaComp instanceof JTextField
				|| aaComp instanceof JRadioButton
				|| aaComp instanceof JComboBox
				|| aaComp instanceof JCheckBox
				|| aaComp instanceof MetalComboBoxButton))
		{
			caComp = aaComp;
			return;
		}
		// end defect 7885
	}
	/**
	 * This method sets the variable caComp with the component which 
	 * has focus.
	 * 
	 * @param aaComp Component
	 */
	public void getFocusComponent(Component aaComp)
	{
		if (aaComp instanceof Container)
		{

			Container laCont = (Container) aaComp;

			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getFocusComponent(larrChildren[i]);
			}
		}
		if (aaComp == null)
		{
			return;
		}
		if (aaComp.hasFocus())
		{
			caComp = aaComp;
			return;
		}
	}
	/**
	 * Overrides super class
	 * Returns the error message string of this throwable object.
	 *
	 * @return  the error message string of this <code>Throwable</code> 
	 *          object if it was {@link #Throwable(String) created} with  
	 *          an error message string; or <code>null</code> if it was 
	 *          {@link #Throwable() created} with no error message. 
	 *            
	 */
	public String getMessage()
	{
		return csMessage;
	}
	/**
	 * Get message type
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMsgType()
	{
		return csMsgType;
	}
	/**
	 * Get the title of the message box
	 * 
	 * @return title of message box
	 */
	public java.lang.String getTitle()
	{
		return csTitle;
	}
	/**
	 * Indicate if it is a validation error
	 *  
	 * @return boolean
	 */
	public boolean isValidationError()
	{
		//If there is any validation error to display
		if (cvComponents.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Populate Error Message based on ciCode 
	 * 
	 */
	private void populateErrorMsg()
	{
		ErrorMessagesData laErrorMsgData =
			ErrorMessagesCache.getErrMsg(ciCode);
		if (laErrorMsgData == null)
		{
			csMessage =
				"The error message number: "
					+ ciCode
					+ " you requested does not have a valid " +
					"ErrorMsgCat in the RTS Database.  Contact the " +
					"RTS Technical Support Representative to add " +
					"this message to the database!";
			csMsgType = SYSTEM_ERROR;
			csTitle = "Message Category Not Found";
		}
		else
		{
			csMsgType = laErrorMsgData.getErrMsgCat();
			csTitle = createErrorTitle(ciCode);
			csMessage = laErrorMsgData.getErrMsgDesc();

			if (laErrorMsgData.getPCErrLogIndi() == 1)
			{
				PCErrorLog laPCErroLog = new PCErrorLog();
				laPCErroLog.setErrorMsgData(laErrorMsgData);
				Thread laPCThread = new Thread(laPCErroLog);
				laPCThread.start();
			}
			if (laErrorMsgData.getMFErrLogIndi() == 1)
			{
				MFErrLog laMFErrLog = new MFErrLog();
				laMFErrLog.setErrorMsgData(laErrorMsgData);
				Thread laMFThread = new Thread(laMFErrLog);
				laMFThread.start();
			}

			if (carrErrorMsgFiller != null)
			{
				int liArraySizeCount = carrErrorMsgFiller.length;
				int liArrayCounter = 0;
				StringTokenizer laStringTokenizer =
					new StringTokenizer(csMessage, "?");
				StringBuffer lsStringBuffer = new StringBuffer();
				while (laStringTokenizer.hasMoreTokens())
				{
					lsStringBuffer.append(laStringTokenizer.nextToken());
					if (liArrayCounter < liArraySizeCount)
					{
						lsStringBuffer.append(
							carrErrorMsgFiller[liArrayCounter]);
						liArrayCounter = liArrayCounter + 1;
					}
				}
				csMessage = lsStringBuffer.toString();
			}
		}
	}
	/**
	 * Override the printStackTrace() method of super
	 */
	public void printStackTrace()
	{
		synchronized (System.err)
		{
			System.err.println(csDetailMsg);
		}
	}
	/**
	 * Override the printStackTrace(PrintStream aPrintStream) 
	 * method of super
	 * 
	 * @param aaPrintStream  PrintStream
	 */
	public void printStackTrace(PrintStream aaPrintStream)
	{
		synchronized (aaPrintStream)
		{
			aaPrintStream.print(csDetailMsg);
		}
	}
	/**
	 * Override the printStackTrace(PrintStream aPrintWriter) 
	 * method of super
	 * 
	 * @param aaPrintWriter PrintWriter
	 */
	public void printStackTrace(PrintWriter aaPrintWriter)
	{
		synchronized (aaPrintWriter)
		{
			aaPrintWriter.print(csDetailMsg);
		}
	}
	/**
	 * Set beep
	 * 
	 * @param aiNewBeep boolean
	 */
	public void setBeep(int aiNewBeep)
	{
		ciBeep = aiNewBeep;
	}
	/**
	 * set the error code
	 * 
	 * @param aiCode error code
	 */
	public void setCode(int aiCode)
	{
		ciCode = aiCode;
	}
	/**
	 * set default key
	 * 
	 * @param aiNewDefaultKey int
	 */
	public void setDefaultKey(int aiNewDefaultKey)
	{
		ciDefaultKey = aiNewDefaultKey;
	}
	/**
	 * Set the stack trace of the exception
	 * 
	 * @param asDetailMsg Detailed Message of the exception
	 */
	public void setDetailMsg(java.lang.String asDetailMsg)
	{
		csDetailMsg = asDetailMsg;
	}
	/**
	 * Set the help URL
	 * 
	 * @param asNewHelpURL java.lang.String
	 */
	public void setHelpURL(java.lang.String asNewHelpURL)
	{
		csHelpURL = asNewHelpURL;
	}
	/**
	 * Set the message of the exception
	 * 
	 * @param asMsg Detailed Message of the exception
	 */
	public void setMessage(java.lang.String asMsg)
	{
		csMessage = asMsg;
	}
	/**
	 * Set message dimension
	 * 
	 * @param x int
	 * @param y int
	 */
	public void setMsgDimsnion(int aiWidth, int aiHeight)
	{
		cbUserSuppliedMsgDimension = true;
		ciWidth = aiWidth;
		ciHeight = aiHeight;
	}
	/**
	 * Set Message location
	 * 
	 * @param aiX int
	 * @param aiY int
	 */
	public void setMsgLoc(int aiX, int aiY)
	{
		cbUserSuppliedMsgLoc = true;
		ciMsgLocX = aiX;
		ciMsgLocY = aiY;
	}
	/**
	 * Set message location and anchor
	 * 
	 * @param asAnchor String
	 * @param aiCoOrdinate int
	 */
	public void setMsgLoc(String asAnchor, int aiCoOrdinate)
	{
		cbUserSuppliedMsgLoc = true;
		csAnchor = asAnchor;
		if (asAnchor.equals(CENTER_HORIZONTAL))
		{
			ciMsgLocY = aiCoOrdinate;
		}
		else if (asAnchor.equals(CENTER_VERTICAL))
		{
			ciMsgLocX = aiCoOrdinate;
		}

	}
	/**
	 * Set the message type
	 * 
	 * @param asMsgType message type
	 */
	public void setMsgType(java.lang.String asMsgType)
	{
		csMsgType = asMsgType;
	}
	/**
	 * Set the title of the message box
	 * 
	 * @param asTitle title of the message box
	 */
	public void setTitle(java.lang.String asTitle)
	{
		csTitle = asTitle;
	}
	/**
	 * Write the exception to the log.
	 * Use PCErrorLog to avoid hang on file io.
	 */
	public void writeExceptionToLog()
	{
		// form up the object to send to PCErrorLog
		ErrorMessagesData laErrorMsgData = new ErrorMessagesData();
		
		// defect 8266
		if (ciCode > 0)
		{
			laErrorMsgData.setErrMsgDesc(ErrorMessagesCache.getErrMsg(ciCode) + " " + ciCode);
		}
		else
		{
			// bring in the detail of the message.  Include the detail.
			laErrorMsgData.setErrMsgDesc(csMessage + "\n" + csDetailMsg);
		}
		// end defect 8266

		// Set the error message number to zero
		laErrorMsgData.setErrMsgNo(new Integer(0).intValue());

		// set PCErrorIndi to 1
		laErrorMsgData.setPCErrLogIndi(new Integer(1).intValue());

		// set the message type
		laErrorMsgData.setErrMsgType(csMsgType);



		// write the message to the log
		PCErrorLog pcErroLog = new PCErrorLog();
		pcErroLog.setErrorMsgData(laErrorMsgData);
		Thread laPCThread = new Thread(pcErroLog);
		laPCThread.start();
		
		// defect 8851
		//	Set variables to null
		laPCThread = null;
		pcErroLog = null;
		laErrorMsgData = null;
		// end defect 8851
	}
}