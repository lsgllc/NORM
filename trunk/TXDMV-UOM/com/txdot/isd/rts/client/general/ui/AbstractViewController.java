package com.txdot.isd.rts.client.general.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * 
 * AbstractViewController.java
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Fix CQU100003529
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * S Govindappa 12/16/2002	Added new method 
 * 							getPreviousControllers().
 *							defect 5147 
 * K Harrell	03/19/2004	JavaDoc Cleanup. 
 * 							add directCall(int,Object)
 *							delete directCall(int)
 * 							Ver 5.2.0
 * Ray Rowehl	05/17/2004	Add check to avoid nullpointer on frame.
 *							merge into 5.2.0
 *							modify close()
 *							defect 7030 Ver 5.2.0
 * T Pederson	02/04/2005	Change setView to use setVisibleRTS for 
 * 							Java 1.4.
 *							modify setView()
 *							defect 7701 Ver 5.2.3
 * Ray Rowehl	02/16/2005	Add getters and setters for frame, data,
 * 							and transcode.
 * 							add setData(), setFrame(), 
 * 							setNextController(), setTransCode()
 * 							defect 8018 Ver 5.2.3
 * Ray Rowehl	03/17/2005	Allow controller to set PreviousControllers.
 *							This allows the controller to avoid calling
 *							super.setView()
 * 							add setPreviousControllers()
 * 							defect 8018 Ver 5.2.3
 * Ray Rowehl	03/19/2005	Refactor the public fields and make them 
 * 							private.  All children should be using the 
 * 							getters and setters now.
 * 							defect 8018 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */

/**
 * Super class for all ViewController objects
 * <p>The AbstractViewController handles many roles in the application
 * <ul><li>It defines constants which handle every possibility for the 
 * 			next screen.
 *	<ul><li>PREVIOUS - to go back to the previous screen with new caData
 *			<li>CURRENT - to stay on the current screen with new caData
 *			<li>NEXT - to move to another screen with new caData
 *			<li>FINAL - the final screen on a sequence will close 
 *							every screen currently displayed
 *			<li>CANCEL - to go back to the previous screen 
 *							discarding the current caData
 *      </ul>
 *      <li>It contains a reference to <code>caNextController, caData, 
 * 			caFrame, ciDirectionFlow, and cvPreviousControllers<code> so that
 *			any implementing subclasses do not have to handle their 
 *			implementation.
 *		<li>It handles setting up the RTSEvent/Listener model
 *	    <li>It controls the visibility of its caFrame.
 * </ul>
 * 
 * @version	5.2.3			03/19/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		10/06/2001 10:36:00
 */

public abstract class AbstractViewController
{
	// Direction Flow Values
	/**
	 * When navigation is going to the page immediately preceding the 
	 * current one.
	 */
	public final static int PREVIOUS = 0;
	/**
	 * When navigation is to stay on the same screen.
	 */
	public final static int CURRENT = 1;
	/**
	 * When navigation is moving to a different screen than the 
	 * current one.
	 */
	public final static int NEXT = 2;
	/**
	 * When navigation is at the last screen in a sequence and wishes 
	 * every screen to close.
	 */
	public final static int FINAL = 3;
	/**
	 * When the cancel button is pressed and navigation returns to the 
	 * page immediately preceding the current one.  
	 * This is different from PREVIOUS in that PREVIOUS sends any 
	 * updated caData to the previous screen, while CANCEL discards any 
	 * changes made to the caData and gives the previous screen its old 
	 * unchanged caData back.
	 */
	public final static int CANCEL = 4;
	/**
	 * Return back to Desktop
	 */
	public final static int DESKTOP = 5;
	/**
	 * Making a call to another caFrame.
	 */
	public final static int DIRECT_CALL = 6;
	
	private String caNextController;
	//Will push all screen till it reaches controller/screen indicated
	//by previous controller.
	private String caPreviousController = null;
	private Object caData;
	private RTSDialogBox caFrame;
	private int ciDirectionFlow;
	private Vector cvPreviousControllers;
	private RTSMediator caMediator;
	private String csTransCode;
	private boolean cbAcceptingInput = true;
	
	/**
	 * Enter button selected
	 */
	public final static int ENTER = 1;
	/**
	 * Help button selected
	 */
	public final static int HELP = 2;
	/**
	 * Search button selected
	 */
	public final static int SEARCH = 3;
	/**
	 * Delete button selected
	 */
	public final static int DELETE = 5;
	/**
	 * Void button selected
	 */
	public final static int VOID = 6;
	
	/**
	 * Called by its subclasses.
	 */
	public AbstractViewController()
	{
		super();
	}
	
	/**
	 * Closes the ViewController
	 */
	public void close()
	{
		if (caFrame != null)
		{
			// defect 7701
			caFrame.setVisibleRTS(false);
			// end defect 7701

			// defect 7030
			// make sure frame is not null before disposing
			if (caFrame != null)
			{
				caFrame.dispose();
			}
			// end defect 7030
		}
	}
	/**
	 * Intended to allow a direct call to a class.
	 * <p>This method is not implemented at this time!
	 * 
	 * @return Object
	 * @param aiCommand int
	 * @throws RTSException The exception description.
	 */
	public Object directCall(int aiCommand, Object aaData)
		throws RTSException
	{
		return null;
	}
	
	/**
	 * Closes out the frame and data.
	 */
	protected final void finalize()
	{
		caData = null;
		caNextController = null;
		cvPreviousControllers = null;
		csTransCode = null;
		// TODO Only do this if frame is not null already.
		if (caFrame != null)
		{
			// defect 7701
			caFrame.setVisibleRTS(false);
			// end defect 7701
			caFrame.dispose();
			caFrame = null;
		}
	}
	
	/**
	 * Returns the caData object.
	 * 
	 * @return Object
	 */
	public Object getData()
	{
		return caData;
	}
	
	/**
	 * Returns the constant value of the direction flow, either CANCEL, 
	 * CURRENT, FINAL, NEXT, or PREVIOUS.
	 * 
	 * @return int
	 */
	public int getDirectionFlow()
	{
		return ciDirectionFlow;
	}
	
	/**
	 * Returns the Frame
	 * 
	 * @return RTSDialogBox
	 */
	public RTSDialogBox getFrame()
	{
		return caFrame;
	}
	
	/**
	 * Returns the graph of RTSMediator
	 * 
	 * @return RTSMediator
	 */
	public RTSMediator getMediator()
	{
		return caMediator;
	}
	
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int
	 */
	public abstract int getModuleName();
	
	/**
	 * Returns the name of the next controller, which the RTSMediator 
	 * will use to construct the next screen.
	 * 
	 * @return String
	 */
	public String getNextController()
	{
		return caNextController;
	}
	
	/**
	 * Returns the previous controller.
	 * 
	 * @return String
	 */
	public String getPreviousController()
	{
		return caPreviousController;
	}
	
	/**
	 * Returns the Vector of previous controllers.
	 * 
	 * @return Vector
	 */
	public Vector getPreviousControllers()
	{
		return cvPreviousControllers;
	}
	
	/**
	 * Returns the transcode.
	 * <p>Return empty string if the value is null.
	 * 
	 * @return String
	 */
	public String getTransCode()
	{
		if (csTransCode == null)
		{
			return "";
		}
		else
		{
			return csTransCode;
		}
	}
	
	/**
	 * Returns boolean indicating whether or not the controller is 
	 * allowing input.
	 * 
	 * @return boolean
	 */
	public boolean isAcceptingInput()
	{
		return cbAcceptingInput;
	}
	
	/**
	 * All subclasses must override this method to handle caData coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the caData to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public abstract void processData(int aiCommand, Object aaData);
	
	/**
	 * Sets the Accepting Input value.
	 * 
	 * @param abNewAcceptingInput boolean
	 */
	public void setAcceptingInput(boolean abNewAcceptingInput)
	{
		cbAcceptingInput = abNewAcceptingInput;
	}
	
	/**
	 * Sets the value for Mediator.
	 * 
	 * @param aaMediator RTSMediator
	 */
	public void setMediator(RTSMediator aaMediator)
	{
		caMediator = aaMediator;
	}
	
	/**
	 * Sets the previous controller
	 * 
	 * @param asNewController String
	 */
	public void setPreviousController(String asNewController)
	{
		caPreviousController = asNewController;
	}
	
	/**
	 * Returns the Vector of previous controllers.
	 * 
	 * @param avPreviousControllers Vector
	 */
	public void setPreviousControllers(Vector avPreviousControllers)
	{
		cvPreviousControllers = avPreviousControllers;
	}
	
	/**
	 * Handles the common tasks involved in displaying a JDialogBox.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		caData = aaData;
		cvPreviousControllers = avPreviousControllers;
		csTransCode = asTransCode;
		caFrame.setController(this);
		caFrame.setData(aaData);
		// defect 7701
		// Change setVisible to setVisibleRTS
		if (!caFrame.isVisible())
		{
			caFrame.setVisibleRTS(true);
		}
		//frame.setVisible(true);
		// end defect 7701
	}
	/**
	 * Sets the Data object
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		caData = aaData;
	}

	/**
	 * Sets the Direction Flow
	 * 
	 * @param aiDirectionFlow int
	 */
	public void setDirectionFlow(int aiDirectionFlow)
	{
		ciDirectionFlow = aiDirectionFlow;
	}

	/**
	 * Sets the Fame AbstractValue
	 * 
	 * @param aaRTSDB RTSDialogBox
	 */
	public void setFrame(RTSDialogBox aaRTSDB)
	{
		caFrame = aaRTSDB;
	}

	/**
	 * Sets the NextController value.
	 * 
	 * @param asNextController String
	 */
	public void setNextController(String asNextController)
	{
		caNextController = asNextController;
	}
	
	/**
	 * Set the TransCode value
	 * 
	 * @param asTransCode String
	 */
	public void setTransCode(String asTransCode)
	{
		csTransCode = asTransCode;
	}
}
