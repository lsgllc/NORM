package com.txdot.isd.rts.services.exception;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * MsgDialog.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Name			MM/DD/YYYY	defect description
 * B Arredondo	12/18/2002	Fixing Defect# 5147. Made changes for the 
 * 							user help guideso had to make changes
 *							in actionPerformed().
 * Min Wang    	07/09/2002	Fixing defect 6329. Prevent multiple enter.
 * 							modified actionPerformed()
 * Min Wang		02/11/2005	Use the system specified color or font for
 * 							showing RTSException message.
 * 							format source, organize imports. 
 * 							modified construct()							 
 *							defect 7904  Ver 5.2.3
 * Ray Rowehl	02/11/2005	Change setVisible method. 
 * 							add setVisibleMsg()
 * 							delete setVisible()
 * 							defect 7701 Ver 5.2.3
 * Ray Rowehl	02/11/2005	Update handleException
 * 							modify handleException()
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	02/15/2005	Modify constructors to pass title and modal
 * 							modify constructors(JDialog, *)
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		07/21/2006	Added a call to a native method to fix the 
 * 							focus problem with the JRE.  This fixes the 
 * 							problem when they cancel from one JDialog to 
 * 							another JDialog.
 * 							Sun Internal Review ID (704517).
 * 							add dispose()
 * 							defect 8756 Ver 5.2.3
 * Ray Rowehl	06/12/2012	Do not call the focus fix! 
 * 							modify dispose()
 * 							defect 11337 Ver POS_700
 * Ray Rowehl	06/12/2012	Use the default font.
 * 							delete getBuilderData()
 * 							modify gettxtMessage()
 * 							defect 11375 Ver POS_700
 * ---------------------------------------------------------------------
 */

/**
 * Error Message Dialog called by RTSException.
 *
 * @version	POS_700			06/12/2012
 * @author	Michael Abernethy
 * @since					11/02/2001 15:21:51
 */

public class MsgDialog
	extends JDialog
	implements ActionListener, ContainerListener, KeyListener, 
			WindowListener
{
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnCancel =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnDetails =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnHelp =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnNo =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnOk =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnYes =
		null;
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JScrollPane ivjJScrollPane2 = null;
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSEditorPane ivjtxtMessage =
		null;
	private JLabel ivjicon = null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnEnter =
		null;
	private int ciReturnStatus;
	private int ciErrorCode;
	private JTextArea ivjtxtDetailMessage = null;
	private boolean cbDetailShowing;
	private int ciDefaultKey;
	private boolean cbEnableCustomKeyListeners = false;
	private String csDisplayType = null;
	private boolean cbHTML = false;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjbtnExit =
		null;
	private Timer caTimer = null;
	private int ciTimeCount;
	private boolean cbTimer;
	private boolean cbWorking;
	private boolean cbBeep;
	private String csHelpString;
	
	/**
	 * MsgDialog constructor .
	 */
	public MsgDialog()
	{
		super();
		initialize();
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JDialog aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super(aaParent, asTitle, abModal);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JDialog aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super(aaParent, asTitle, abModal);
		cbHTML = abHTML;
		initialize();
		this.ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param lsDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JDialog aaParent,
		boolean abModal,
		String lsDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super(aaParent, asTitle, abModal);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(lsDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JDialog aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super(aaParent, asTitle, abModal);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JFrame aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super(aaParent);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JFrame aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super(aaParent);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean 
	 */
	public MsgDialog(
		javax.swing.JFrame aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super(aaParent);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(asDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * MsgDialog constructor .
	 * @param aaParent javax.swing.JDialog
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		javax.swing.JFrame aaParent,
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super(aaParent);
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * MsgDialog constructor .
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super();
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super();
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, "");
	}
	
	/**
	 * MsgDialog constructor .
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param aiDefaultKey int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		int aiDefaultKey,
		boolean abHTML)
	{
		super();
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		ciDefaultKey = aiDefaultKey;
		construct(asDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * MsgDialog constructor .
	 * @param abModal boolean
	 * @param asDisplayType java.lang.String
	 * @param asTitle java.lang.String
	 * @param asMessage java.lang.String
	 * @param asDetailMessage java.lang.String
	 * @param aiErrorCode int
	 * @param abHTML boolean
	 */
	public MsgDialog(
		boolean abModal,
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage,
		int aiErrorCode,
		boolean abHTML)
	{
		super();
		cbHTML = abHTML;
		initialize();
		ciErrorCode = aiErrorCode;
		construct(asDisplayType, asTitle, asMessage, asDetailMessage);
	}
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param 
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//defect 6329
		//prevent multiple enter. 
		if (cbWorking || !isVisible())
		{
			// end defect 6329
			return;
		}
		else
		{
			cbWorking = true;
		}
		try
		{
			if (aaAE.getSource() == getbtnCancel())
			{
				ciReturnStatus = RTSException.CANCEL;
				dispose();
			}
			else if (aaAE.getSource() == getbtnDetails())
			{
				if (cbDetailShowing)
				{
					setSize(450, 250);
					getJScrollPane2().setVisible(false);
					gettxtDetailMessage().setVisible(false);
					repaint();
					setVisibleMsg(true);
					getbtnDetails().setText("Details >>>");
					cbDetailShowing = false;
				}
				else
				{
					setSize(450, 450);
					getJScrollPane2().setVisible(true);
					gettxtDetailMessage().setVisible(true);
					repaint();
					setVisibleMsg(true);
					getbtnDetails().setText("Details <<<");
					cbDetailShowing = true;
				}
			}
			else if (aaAE.getSource() == getbtnEnter())
			{
				ciReturnStatus = RTSException.ENTER;
				dispose();
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				String lsTitle = getTitle();
				if (lsTitle != null
					&& lsTitle.equals(GeneralConstant.DTA004))
				{
					RTSHelp.displayHelp(RTSHelp.DTA004);
				}
				else if (csHelpString != null)
				{
					RTSHelp.displayHelp(csHelpString);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.ERR001);
				}
			}
			else if (aaAE.getSource() == getbtnNo())
			{
				ciReturnStatus = RTSException.NO;
				dispose();
			}
			else if (aaAE.getSource() == getbtnOk())
			{
				ciReturnStatus = RTSException.OK;
				dispose();
			}
			else if (aaAE.getSource() == getbtnYes())
			{
				ciReturnStatus = RTSException.YES;
				dispose();
			}
			else if (aaAE.getSource() == getbtnExit())
			{
				ciReturnStatus = RTSException.EXIT;
				dispose();
			}
			else if (caTimer != null && aaAE.getSource() == caTimer)
			{
				ciReturnStatus = RTSException.CONTINUE;
				caTimer.stop();
				dispose();
			}
		}
		finally
		{
			cbWorking = false;
		}
	}
	
	/**
	 * Add Key and Container Listeners
	 * 
	 * @param aaC
	 */
	private void addKeyAndContainerListenerRecursively(Component aaC)
	{
		// To be on the safe side, try to remove KeyListener first just 
		// in case it has been added before.
		// If not, it won't do any harm
		aaC.removeKeyListener(this);
		//Add KeyListener to the Component passed as an argument
		aaC.addKeyListener(this);

		if (aaC instanceof Container)
		{

			//Component c is a Container. The following cast is safe.
			Container laCont = (Container) aaC;

			// To be on the safe side, try to remove ContainerListener
			// first just in case it has been added before.
			// If not, it won't do any harm
			laCont.removeContainerListener(this);
			// Add ContainerListener to the Container.
			laCont.addContainerListener(this);

			//Get the Container's array of children Components.
			Component[] larrChildren = laCont.getComponents();

			//For every child repeat the above operation.
			for (int i = 0; i < larrChildren.length; i++)
			{
				addKeyAndContainerListenerRecursively(larrChildren[i]);
			}
		}
	}
	
	/**
	 * Invoked when a component has been added to the container.
	 * 
	 * @param aaCE ContainerEvent
	 */
	public void componentAdded(java.awt.event.ContainerEvent aaCE)
	{
		addKeyAndContainerListenerRecursively(aaCE.getChild());
	}
	
	/**
	 * Invoked when a component has been removed from the container.
	 * 
	 * @param aaCE ContainerEvent
	 */
	public void componentRemoved(java.awt.event.ContainerEvent aaCE)
	{
		removeKeyAndContainerListenerRecursively(aaCE.getChild());
	}
	
	/**
	 * Set visibility of components according to type
	 * 
	 * @param asDisplayType String
	 * @param asTitle String
	 * @param asMessage String
	 * @param asDetailMessage String
	 */
	private void construct(
		String asDisplayType,
		String asTitle,
		String asMessage,
		String asDetailMessage)
	{
		setTitle(asTitle);
		csDisplayType = asDisplayType;

		//determine what icon to display
		String lsIconName =
			asDisplayType.substring(0, asDisplayType.indexOf("_"));
		if (lsIconName.equals("cross"))
		{
			int liIndex = (int) (Math.random() * 2) + 1;
			lsIconName = lsIconName + liIndex;
		}

		if (lsIconName.equals("none"))
		{
			geticon().setText("");
		}
		else
		{
			geticon().setText("");
			URL laURL =
				getClass().getResource(
					SystemProperty.getImagesDir()
						+ lsIconName
						+ "."
						+ SystemProperty.getImageType());
			if (laURL != null)
			{
				ImageIcon laIcon = new ImageIcon(laURL);
				if (laIcon != null)
					geticon().setIcon(laIcon);
			}
		}

		// Set the HTML message if message is not already in html format
		if (!cbHTML)
		{
			//defect 7904
			//message = "<font color=" + "\"" + "#4e4e98" + "\">" + 
			// message + "</font>";
			asMessage =
				"<font color="
					+ "\""
					+ "black"
					+ "\">"
					+ asMessage
					+ "</font>";
			//end defect 7904
		}
		gettxtMessage().setText(asMessage);
		gettxtMessage().getFont();
		gettxtDetailMessage().setText(asDetailMessage);

		if (asDisplayType.endsWith("OK_DETAILS"))
		{
			getbtnCancel().setVisible(false);
			getbtnDetails().setVisible(true);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(false);
			getbtnNo().setVisible(false);
			getbtnOk().setVisible(true);
			getbtnYes().setVisible(false);
			getbtnExit().setVisible(false);
		}
		else if (asDisplayType.endsWith("OK_HELP"))
		{
			getbtnCancel().setVisible(false);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(true);
			getbtnNo().setVisible(false);
			getbtnOk().setVisible(true);
			getbtnYes().setVisible(false);
			getbtnExit().setVisible(false);

		}
		else if (asDisplayType.endsWith("YES_NO_CANCEL"))
		{
			getbtnCancel().setVisible(true);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(false);
			getbtnNo().setVisible(true);
			getbtnOk().setVisible(false);
			getbtnYes().setVisible(true);
			cbEnableCustomKeyListeners = true;
			getbtnExit().setVisible(false);

		}
		else if (asDisplayType.endsWith("ENTER_CANCEL_HELP"))
		{
			getbtnCancel().setVisible(true);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(true);
			getbtnHelp().setVisible(true);
			getbtnNo().setVisible(false);
			getbtnOk().setVisible(false);
			getbtnYes().setVisible(false);
			getbtnExit().setVisible(false);
		}
		else if (asDisplayType.endsWith("OK"))
		{
			getbtnCancel().setVisible(false);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(false);
			getbtnNo().setVisible(false);
			getbtnOk().setVisible(true);
			getbtnYes().setVisible(false);
			getbtnExit().setVisible(false);

			getRootPane().setDefaultButton(getbtnOk());

		}
		else if (asDisplayType.endsWith("YES_NO"))
		{
			getbtnCancel().setVisible(false);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(false);
			getbtnNo().setVisible(true);
			getbtnOk().setVisible(false);
			getbtnYes().setVisible(true);
			getbtnExit().setVisible(false);
			cbEnableCustomKeyListeners = true;

		}
		else if (asDisplayType.endsWith("EXIT"))
		{
			getbtnCancel().setVisible(false);
			getbtnDetails().setVisible(false);
			getbtnEnter().setVisible(false);
			getbtnHelp().setVisible(false);
			getbtnNo().setVisible(false);
			getbtnOk().setVisible(false);
			getbtnYes().setVisible(false);
			getbtnExit().setVisible(true);
			getRootPane().setDefaultButton(getbtnExit());
		}

	}
	
	/**
	 * This overrides the JDialog dispose method so that we did not have
	 * to code the focus fix handle function all over the place.
	 */
	public void dispose()
	{
		// defect 8756
		// If we are setting a window visible to false and it is not
		// the JFrame desktop we will run the JNI focus fix passing
		// the title of the window we are going to.  The JNI code
		// will only set the window to enabled if it is currentl
		// disabled.
		// defect 11337
		//Window laOwner = getOwner();
		// end defct 11337
		super.dispose();
		
		// defect 11337
		//if (laOwner != null && laOwner instanceof JDialog)
		//{
		//	JniAdInterface.focusFix(((JDialog)laOwner).getTitle());
		//}
		// end defect 11337
		// end defect 8756
	}
	
	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText("Cancel");
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnCancel;
	}
	
	/**
	 * Return the btnDetails property value.
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSButton getbtnDetails()
	{
		if (ivjbtnDetails == null)
		{
			try
			{
				ivjbtnDetails =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnDetails.setName("btnDetails");
				ivjbtnDetails.setText("Details >>>");
				// user code begin {1}
				ivjbtnDetails.addActionListener(this);
				ivjbtnDetails.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnDetails;
	}
	
	/**
	 * Return the btnEnter property value.
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnEnter.setName("btnEnter");
				ivjbtnEnter.setText("Enter");
				// user code begin {1}
				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnEnter;
	}
	
	/**
	 * Return the btnExit property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnExit()
	{
		if (ivjbtnExit == null)
		{
			try
			{
				ivjbtnExit =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnExit.setName("btnExit");
				ivjbtnExit.setMnemonic('e');
				ivjbtnExit.setText("Exit");
				// user code begin {1}
				ivjbtnExit.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnExit;
	}
	
	/**
	 * Return the btnHelp property value.
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setMnemonic('h');
				ivjbtnHelp.setText("Help");
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				ivjbtnHelp.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnHelp;
	}
	
	/**
	 * Return the btnNo property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnNo()
	{
		if (ivjbtnNo == null)
		{
			try
			{
				ivjbtnNo =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnNo.setName("btnNo");
				ivjbtnNo.setMnemonic('n');
				ivjbtnNo.setText("No");
				// user code begin {1}
				ivjbtnNo.addActionListener(this);
				ivjbtnNo.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnNo;
	}
	
	/**
	 * Return the btnOk property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnOk()
	{
		if (ivjbtnOk == null)
		{
			try
			{
				ivjbtnOk =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnOk.setName("btnOk");
				ivjbtnOk.setText("OK");
				// user code begin {1}
				ivjbtnOk.addActionListener(this);
				ivjbtnOk.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnOk;
	}
	
	/**
	 * Return the btnYes property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnYes()
	{
		if (ivjbtnYes == null)
		{
			try
			{
				ivjbtnYes =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnYes.setName("btnYes");
				ivjbtnYes.setMnemonic('y');
				ivjbtnYes.setText("Yes");
				// user code begin {1}
				ivjbtnYes.addActionListener(this);
				ivjbtnYes.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnYes;
	}
	
	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel geticon()
	{
		if (ivjicon == null)
		{
			try
			{
				ivjicon = new javax.swing.JLabel();
				ivjicon.setName("icon");
				ivjicon.setPreferredSize(
					new java.awt.Dimension(54, 54));
				ivjicon.setText("Label");
				ivjicon.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				ivjicon.setHorizontalTextPosition(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjicon;
	}
	
	/**
	 * Return the JDialogContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJDialogContentPane()
	{
		if (ivjJDialogContentPane == null)
		{
			try
			{
				ivjJDialogContentPane = new javax.swing.JPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(
					new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 2;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 303;
				constraintsJScrollPane1.ipady = 103;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(24, 6, 10, 27);
				getJDialogContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsicon =
					new java.awt.GridBagConstraints();
				constraintsicon.gridx = 1;
				constraintsicon.gridy = 1;
				constraintsicon.weighty = 1.0;
				constraintsicon.ipadx = 23;
				constraintsicon.ipady = 40;
				constraintsicon.insets =
					new java.awt.Insets(24, 32, 81, 6);
				getJDialogContentPane().add(geticon(), constraintsicon);

				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 2;
				constraintsJPanel1.gridwidth = 2;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = -92;
				constraintsJPanel1.ipady = 32;
				constraintsJPanel1.insets =
					new java.awt.Insets(10, 26, 21, 27);
				getJDialogContentPane().add(
					getJPanel1(),
					constraintsJPanel1);

				java.awt.GridBagConstraints constraintsJScrollPane2 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane2.gridx = 1;
				constraintsJScrollPane2.gridy = 3;
				constraintsJScrollPane2.gridwidth = 2;
				constraintsJScrollPane2.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane2.weightx = 1.0;
				constraintsJScrollPane2.ipadx = 416;
				constraintsJScrollPane2.ipady = 145;
				constraintsJScrollPane2.insets =
					new java.awt.Insets(22, 6, 0, 6);
				getJDialogContentPane().add(
					getJScrollPane2(),
					constraintsJScrollPane2);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogContentPane;
	}
	
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.FlowLayout());
				getJPanel1().add(getbtnYes(), getbtnYes().getName());
				getJPanel1().add(getbtnNo(), getbtnNo().getName());
				getJPanel1().add(getbtnOk(), getbtnOk().getName());
				getJPanel1().add(
					getbtnEnter(),
					getbtnEnter().getName());
				getJPanel1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getJPanel1().add(getbtnHelp(), getbtnHelp().getName());
				getJPanel1().add(
					getbtnDetails(),
					getbtnDetails().getName());
				getJPanel1().add(getbtnExit(), getbtnExit().getName());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}
	
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setRequestFocusEnabled(false);
				getJScrollPane1().setViewportView(gettxtMessage());
				// user code begin {1}
				ivjJScrollPane1.setBorder(null);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	
	/**
	 * Return the JScrollPane2 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane2()
	{
		if (ivjJScrollPane2 == null)
		{
			try
			{
				ivjJScrollPane2 = new javax.swing.JScrollPane();
				ivjJScrollPane2.setName("JScrollPane2");
				ivjJScrollPane2.setVisible(false);
				getJScrollPane2().setViewportView(
					gettxtDetailMessage());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane2;
	}
	
	/**
	 * Get the return status.  The button that is clicked by the user. 
	 *  
	 * @return int
	 */
	public int getReturnStatus()
	{
		return ciReturnStatus;
	}
	
	/**
	 * Return the JTextArea1 property value.
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTextArea gettxtDetailMessage()
	{
		if (ivjtxtDetailMessage == null)
		{
			try
			{
				ivjtxtDetailMessage = new javax.swing.JTextArea();
				ivjtxtDetailMessage.setName("txtDetailMessage");
				ivjtxtDetailMessage.setLineWrap(false);
				ivjtxtDetailMessage.setWrapStyleWord(false);
				ivjtxtDetailMessage.setBounds(0, 0, 160, 120);
				ivjtxtDetailMessage.setVisible(false);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtDetailMessage;
	}
	
	/**
	 * Return the txtMessage property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSEditorPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSEditorPane gettxtMessage()
	{
		if (ivjtxtMessage == null)
		{
			try
			{
				ivjtxtMessage =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSEditorPane();
				ivjtxtMessage.setName("txtMessage");
				ivjtxtMessage.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjtxtMessage.setForeground(
					new java.awt.Color(78, 78, 152));
				// defect 11375
				//ivjtxtMessage.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				ivjtxtMessage.setFont(new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
				// end defect 11375
				ivjtxtMessage.setBounds(0, 0, 81, 120);
				ivjtxtMessage.setEditable(false);
				ivjtxtMessage.setEnabled(true);
				ivjtxtMessage.setRequestFocusEnabled(false);
				// user code begin {1}
				// if (cbHTML){ //always use html format. but if cbhtml
				// is set, do not add color etc.
				ivjtxtMessage.setContentType("text/html");
				//}

				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtMessage;
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
	{
		// defect 7885
		// Show the exception to the user for action
		/* Uncomment the following lines to print uncaught exceptions
		 * to stdout 
		 */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx = new RTSException(
									RTSException.JAVA_ERROR,
									(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7885
	}
	
	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("MsgDialog2");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(450, 250);
			setModal(true);
			setResizable(false);
			setContentPane(getJDialogContentPane());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		cbDetailShowing = false;
		setLocation(
			((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.width
				/ 2
				- getSize().width / 2)),
			((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.height
				/ 2
				- getSize().height / 2))
				- 50);
		addKeyAndContainerListenerRecursively(this);
		addWindowListener(this);
		// user code end
	}
	
	/**
	 * Indicate if the dialog should beep when it shows up
	 * 
	 * @return boolean
	 */
	private boolean isBeep()
	{
		return cbBeep;
	}
	
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE - KeyEvent
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		if (csDisplayType.endsWith("OK_DETAILS"))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnOk().hasFocus())
				{
					getbtnDetails().requestFocus();
				}
				else if (getbtnDetails().hasFocus())
				{
					getbtnOk().requestFocus();
				}
				aaKE.consume();
			}
		}
		else if (csDisplayType.endsWith("OK_HELP"))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnOk().hasFocus())
				{
					getbtnHelp().requestFocus();
				}
				else if (getbtnHelp().hasFocus())
				{
					getbtnOk().requestFocus();
				}
				aaKE.consume();
			}

		}
		else if (csDisplayType.endsWith("YES_NO_CANCEL"))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				if (getbtnYes().hasFocus())
				{
					getbtnNo().requestFocus();
				}
				else if (getbtnNo().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnCancel().hasFocus())
				{
					getbtnYes().requestFocus();
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnNo().hasFocus())
				{
					getbtnYes().requestFocus();
				}
				else if (getbtnCancel().hasFocus())
				{
					getbtnNo().requestFocus();
				}
				else if (getbtnYes().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				aaKE.consume();
			}

		}
		else if (csDisplayType.endsWith("ENTER_CANCEL_HELP"))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				if (getbtnEnter().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnCancel().hasFocus())
				{
					getbtnHelp().requestFocus();
				}
				else if (getbtnHelp().hasFocus())
				{
					getbtnEnter().requestFocus();
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnCancel().hasFocus())
				{
					getbtnEnter().requestFocus();
				}
				else if (getbtnHelp().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnEnter().hasFocus())
				{
					getbtnHelp().requestFocus();
				}
				aaKE.consume();
			}

		}
		else if (csDisplayType.endsWith("YES_NO"))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnYes().hasFocus())
				{
					getbtnNo().requestFocus();
				}
				else if (getbtnNo().hasFocus())
				{
					getbtnYes().requestFocus();
				}
				aaKE.consume();
			}

		}

		if (aaKE.getKeyCode() == KeyEvent.VK_F1
			&& getbtnHelp().isVisible())
		{
			getbtnHelp().doClick();
		}
	}
	
	/**
	 * Invoked when a key has been released.
	 * 
	 * @param aaKE - KeyEvent
	 */
	public void keyReleased(java.awt.event.KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			if (getbtnCancel().isVisible())
			{
				ciReturnStatus = RTSException.CANCEL;
				aaKE.consume();
				this.dispose();
			}
		}

		if (aaKE.getKeyCode() == KeyEvent.VK_Y
			&& cbEnableCustomKeyListeners)
		{
			ciReturnStatus = RTSException.YES;
			dispose();

		}

		if (aaKE.getKeyCode() == KeyEvent.VK_N
			&& cbEnableCustomKeyListeners)
		{
			ciReturnStatus = RTSException.NO;
			dispose();
		}

	}
	
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE - KeyEvent
	 */
	public void keyTyped(java.awt.event.KeyEvent aaKE)
	{
	}
	
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args)
	{
		MsgDialog laTestDialog = null;
		try
		{
			laTestDialog = new MsgDialog();
			MsgDialog laMsgDialog2 = new MsgDialog(
									laTestDialog,
									true,
									"question_YES_NO",
									"Test MsgDialog",
									"Test Message",
									0,
									0,
									false);
			laMsgDialog2.setModal(true);
			laMsgDialog2
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			//laMsgDialog2.show();
			java.awt.Insets insets = laMsgDialog2.getInsets();
			laMsgDialog2.setSize(
				laMsgDialog2.getWidth() + insets.left + insets.right,
				laMsgDialog2.getHeight() + insets.top + insets.bottom);
			laMsgDialog2.setVisibleMsg(true);
		}
		catch (Throwable leException)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JDialog");
			leException.printStackTrace(System.out);
		}
		if (laTestDialog != null)
		{
			laTestDialog.dispose();
		}
	}
	
	/**
	 * Remove Key and Container Listeners
	 * 
	 * @param aaC
	 */
	// The following function is the same as the function above with the
	// exception that it does exactly the opposite - removes this Dialog
	// from the listener lists of Components.

	private void removeKeyAndContainerListenerRecursively(Component aaC)
	{
		aaC.removeKeyListener(this);

		if (aaC instanceof Container)
		{
			Container cont = (Container) aaC;
			cont.removeContainerListener(this);
			Component[] children = cont.getComponents();
			for (int i = 0; i < children.length; i++)
			{
				removeKeyAndContainerListenerRecursively(children[i]);
			}
		}
	}
	
	/**
	 * Set if the dialog should beep when it shows up
	 *
	 * @param abBeep boolean
	 */
	public void setBeep(boolean abBeep)
	{
		cbBeep = abBeep;
	}
	
	/**
	 * set default key
	 * 
	 * @param aiNewDefaultKey int
	 */
	public void setDefaultKey(int aiNewDefaultKey)
	{
		ciDefaultKey = aiNewDefaultKey;
		getbtnNo().requestFocus();
	}
	
	/**
	 * Set help string
	 * 
	 * @param asNewHelpString java.lang.String
	 */
	public void setHelpString(java.lang.String asNewHelpString)
	{
		csHelpString = asNewHelpString;
	}
	
	/**
	 * set time count
	 * 
	 * @param aiNewTimeCount int
	 */
	public void setTimeCount(int aiNewTimeCount)
	{
		ciTimeCount = aiNewTimeCount;
	}
	
	/**
	 * set timer
	 * 
	 * @param abNewTimer boolean
	 */
	public void setTimer(boolean abNewTimer)
	{
		cbTimer = abNewTimer;
	}
	
	/**
	 * Shows or hides this component depending on the value of parameter
	 * <code>b</code>.
	 * @param b  If <code>true</code>, shows this component; 
	 * otherwise, hides this component.
	 * @see #isVisible
	 * @since JDK1.1
	 * 
	 * @param lbVisible - boolean
	 */
	public void setVisibleMsg(boolean lbVisible)
	{
		if (cbTimer)
		{
			caTimer = new javax.swing.Timer(ciTimeCount, this);
			caTimer.start();
		}
		// defect 7885
		// use setvisible instead of show.
		super.setVisible(lbVisible);
		// end defect 7885
	}
	
	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowClosed(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowClosing(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowDeactivated(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowDeiconified(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * @see Frame#setIconImage
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowIconified(java.awt.event.WindowEvent aaWE)
	{
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE - WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent aaWE)
	{
		switch (ciDefaultKey)
		{
			case RTSException.OK :
				if (getbtnOk().isVisible())
				{
					getbtnOk().requestFocus();
				}
				break;

			case RTSException.YES :
				if (getbtnYes().isVisible())
				{
					getbtnYes().requestFocus();
				}
				break;

			case RTSException.NO :
				if (getbtnNo().isVisible())
				{
					getbtnNo().requestFocus();
				}
				break;

			case RTSException.CANCEL :
				if (getbtnCancel().isVisible())
				{
					getbtnCancel().requestFocus();
				}
				break;

		}
		if (cbBeep)
		{
			com.txdot.isd.rts.services.util.UtilityMethods.beep();
		}
	}
}
