package com.txdot.isd.rts.client.general.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 *
 * RTSDialogBox.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RS Chandel	01/08/2002	Added code to check if default button is 
 * 							enabled, only then fire doClick.
 * J Kwik		03/28/2002  Comment consuming of ALT_MASK in keyReleased
 * 							 (not needed) per Chandel.
 * N Ting		04/17/2002	Fix CQU100003529
 * RS Chandel	04/26/2002	Fix CQU100003671. Do not request focus 
 * 							depending upon flag
 * MAbs			06/03/2002	Changed keyreleased to check for VK_A 
 * 							instead of 'a'  CQU100004189
 * T Pederson	02/04/2005	Add setVisibleRTS for Java 1.4.
 *							add setVisibleRTS()
 *							delete setVisible()
 *							defect 7701 Ver 5.2.3
 * Ray Rowehl	02/18/2005	Add setters and getters where needed
 * 							add getController(), isWorking()
 * 							defect 8018 Ver 5.2.3
 * Ray Rowehl	03/19/2005	Rename the public fields and make them 
 * 							private.  All children should be using 
 * 							getters and setters now.
 * 							defect 8018 Ver 5.2.3
 * J Zwiener	06/07/2005	set cbRequestFocus = false to stop setting
 * 							focus for hot keys.
 * 							rename method setRequestFocus from
 * 							setIsRequestFocus.
 * 							method/variable name cleanup.
 * 							defect 8215 Ver 5.2.3
 * B Hargrove	08/19/2005	Java 1.4 code changes.
 * 							defect 7885 Ver 5.2.3
 * B Brown		10/04/2005	Add JTextArea to the list of Components that
 * 							don't fire off hot keys.
 * 							modify keyReleased(KeyEvent)
 * 							defect 8355 Ver 5.2.3
 * K Harrell	12/08/2005	Restored initial value of cbRequestFocus to 
 * 							true. Rollback of 8215 
 * 							Ver 5.2.3 
 * Jeff S.		12/14/2005	Added a temp fix for the JComboBox problem.
 * 							See method for description.
 * 							add comboBoxHotKeyFix()
 * 							defect 8479 Ver 5.2.3
 * Jeff S.		12/21/2005	Added common way to set default focus for
 * 							a frame.
 * 							add getDefaultFocusField(), 
 * 								setDefaultFocusField(), 
 * 								caDefaultFocusField
 * 							modify windowOpened()
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		02/02/2006	Needed access to local variable, made 
 * 							getter public.
 * 							modify getDefaultFocusField()
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		07/20/2006	Added a call to a native method to fix the 
 * 							focus problem with the JRE.  This fixes the 
 * 							problem when they cancel from one JDialog to 
 * 							another JDialog.
 * 							Sun Internal Review ID (704517).
 * 							modify setVisibleRTS
 * 							defect 8756 Ver 5.2.3 
 * Ray Rowehl	04/16/2012	Comment out the Java 1.4.2 focus fix.
 * 							modify setVisibleRTS()
 * 							defect 11337 Ver POS_700 
 * R Pilon		06/07/2012	Move the requestFocus() logic from windowOpened()
 * 							  to windowActivated() to prevent the screen 
 * 							  lockup found when doing the "stapler test" after
 * 							  migrating to the 1.6 JRE.
 * 							modify windowActivated(WindowEvent), 
 * 							  windowOpend(WindowEvent)
 * 							defect 11371 Ver 7.0.0
 * R Pilon		07/23/2012	Backout the change for defect 11371.  This change
 * 							  was implemented for the 1.6 JRE, but behaves
 * 							  differently for the 1.7 JRE.  The descision is
 * 							  to migrate directly to the 1.7 JRE.
 * 							modify windowActivated(WindowEvent), 
 * 							  windowOpend(WindowEvent)
 * 							defect 11371 Ver 7.0.0
 *----------------------------------------------------------------------
 */

/**
 * Superclass for all screens in the RTS system
 * 
 * @version	POS_700 		07/23/2012
 * @author	Michael Abernethy
 * @since 					10/06/2001 14:25:00
 */

public abstract class RTSDialogBox
	extends javax.swing.JDialog
	implements KeyListener, ContainerListener, WindowListener
{
	/**
	 * The AbstractViewController for this frame
	 */
	private AbstractViewController caController;
	/**
	 * Boolean that reflects the current processing status of the Frame.
	 * Field is used to allow only one click at a time on any button.
	 */
	private boolean cbWorking;
	/**
	 * Help Button
	 */
	protected JButton helpButton = null;
	/**
	 * Cancel Button
	 */
	protected JButton cancelButton = null;
	/**
	 * Enter Button
	 */
	protected Component enterButton = null;
	/**
	 * Boolean to indicate of escape has already been pressed.
	 * This field is provided to control the cancel action by  
	 * individual frame.
	 */
	private boolean cbEscapeWorking;
	/**
	 * Component to receive focus next.
	 */
	private Component caNextFocus = null;
	/**
	 * RTSTable
	 */
	private RTSTable caRT = null;
	/**
	 * Allows a frame's location to be set somewhere besides the middle
	 * of the screen
	 */
	private boolean cbManagingLocation = false;
	/**
	 * Boolean to indicate of escape has already been pressed.
	 */
	private boolean cbCancelPressed = true;
	// TODO is this comment accurate?

	/**
	 * This will be used to request focus for hot keys. Always true,
	 * default feature is to request focus.
	 * multiple times when esc is pressed very very quickly.?
	 */
	private boolean cbRequestFocus = true;

	/**
	 * This will be used to set default focus to this component when the
	 * window is opened.  We where doing different implimentations of 
	 * this at the frame level and moved it here for consistency.  This 
	 * will also allow the window listener to be removed from the frame.
	 */
	private Component caDefaultFocusField = null;

	/**
	 * Creates an RTSDialogBox.
	 */
	public RTSDialogBox()
	{
		super();
		initialize();
	}
	
	/**
	 * Creates an RTSDialogBox.
	 * Sets modal as true, and calls the (Dialog, boolean) constructor.
	 * 
	 * @param aaOwner Dialog
	 */
	public RTSDialogBox(Dialog aaOwner)
	{
		super(aaOwner, true);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public RTSDialogBox(Dialog aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public RTSDialogBox(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public RTSDialogBox(Dialog aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * Sets Modal to true and calls the (Frame, boolean) constructor.
	 * 
	 * @param aaOwner Frame
	 */
	public RTSDialogBox(Frame aaOwner)
	{
		super(aaOwner, true);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public RTSDialogBox(Frame aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public RTSDialogBox(Frame aaOwner, String asTitle, boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
		initialize();
	}

	/**
	 * Creates an RTSDialogBox.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public RTSDialogBox(Frame aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
		initialize();
	}

	/**
	 * Add Key and Container Listeners for frame.
	 * 
	 * @param aaComp Component
	 */
	private void addKeyAndContainerListenerRecursively(Component aaComp)
	{
		// To be on the safe side, try to remove KeyListener first just 
		// in case it has been added before.
		// If not, it won't do any harm
		aaComp.removeKeyListener(this);
		// Add KeyListener to the Component passed as an argument
		aaComp.addKeyListener(this);

		if (aaComp instanceof Container)
		{
			//Component c is a Container. The following cast is safe.
			Container laCont = (Container) aaComp;

			// To be on the safe side, try to remove ContainerListener 
			// first just in case it has been added before.
			//If not, it won't do any harm
			laCont.removeContainerListener(this);
			//Add ContainerListener to the Container.
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
	 * Clears color from a component.
	 * Will clear whole frame if that component is provided.
	 * 
	 * @param aaComponent Component
	 */
	public void clearAllColor(Component aaComponent)
	{
		if (aaComponent instanceof JTextField
			|| aaComponent instanceof JRadioButton
			|| aaComponent instanceof JCheckBox
			|| aaComponent instanceof JComboBox)
		{
			if (aaComponent.isEnabled())
			{
				//If component has different color, change it to normal 
				//color. The colors are being set in RTSException.
				if (aaComponent.getForeground().equals(Color.white)
					&& aaComponent.getBackground().equals(
						RTSException.ERR_COLOR))
				{
					aaComponent.setForeground(Color.black);
					if (aaComponent instanceof JTextField
						|| aaComponent instanceof JComboBox)
					{
						aaComponent.setBackground(Color.white);
					}
					if (aaComponent instanceof JCheckBox
						|| aaComponent instanceof JRadioButton)
					{
						aaComponent.setBackground(
							new Color(204, 204, 204));
					}
				}
			}
		}
		else
		{
			if (aaComponent instanceof Container)
			{
				Container laCont = (Container) aaComponent;
				Component[] larrChildren = laCont.getComponents();

				for (int i = 0; i < larrChildren.length; i++)
				{
					clearAllColor(larrChildren[i]);
				}
			}
		}
	}

	/**
	 * Add component to frame.
	 * 
	 * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
	 * 
	 * @param aaCE ContainerEvent
	 */
	public void componentAdded(ContainerEvent aaCE)
	{
		addKeyAndContainerListenerRecursively(aaCE.getChild());
	}

	/** 
	 * Remove component from frame.
	 * 
	 * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
	 * 
	 * @param aaCE ContainerEvent
	 */
	public void componentRemoved(ContainerEvent aaCE)
	{
		removeKeyAndContainerListenerRecursively(aaCE.getChild());
	}
	
	/**
	 * Call after action performed.
	 * Turns off cbWorking.
	 */
	protected void doneWorking()
	{
		cbWorking = false;
	}
	
	/**
	 * Get the Cancel Button component.
	 * 
	 * @param aaComp Component
	 */
	public void getCancelButton(Component aaComp)
	{
		// If cancel button is set in the form, no need to get it.
		// This will be done when cancel button is other button 
		// like a No button.
		if (cancelButton != null)
		{
			return;
		}

		JButton laJButton = null;
		if (aaComp instanceof Container)
		{

			Container laCont = (Container) aaComp;

			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getCancelButton(larrChildren[i]);
			}
		}

		//Get the cancel button.
		if (aaComp instanceof JButton)
		{
			laJButton = ((JButton) aaComp);
			if (laJButton.getText().equals("Cancel"))
			{
				cancelButton = laJButton;
			}
		}
	}
	
	/**
	 * Gets the Controller for this Frame
	 * 
	 * @return AbstractViewController
	 */
	public AbstractViewController getController()
	{
		return caController;
	}

	/**
	 * Gets the field that will have focus by default when the JDialog
	 * is opened.
	 * 
	 * @return Component
	 */
	public Component getDefaultFocusField()
	{
		return caDefaultFocusField;
	}
	
	/**
	 * Method to get the enter button for the form
	 * 
	 * @param aaComp
	 */
	private void getEnterButton(Component aaComp)
	{

		//If enter button is set in the form, no need to get it.
		// This will be done when Enter button is other button like a 
		// No button.
		if (enterButton != null)
		{
			return;
		}
		JButton laJButton = null;
		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			Component[] larrChildren = laCont.getComponents();
			for (int i = 0; i < larrChildren.length; i++)
			{
				getEnterButton(larrChildren[i]);
			}
		}

		//Get the enter button.
		if (aaComp instanceof JButton)
		{
			laJButton = ((JButton) aaComp);
			if (laJButton.getText().equals("Enter"))
			{
				enterButton = laJButton;
			}
		}
	}
	
	/**
	 * This method is called by mediator to get the enter button of 
	 * the form.  This method is added for the OS/2. OS/2 does not 
	 * put focus on frames by default (sometimes)
	 * 
	 * @return Component
	 */
	public Component getEnterButtonForFocus()
	{
		enterButton = null;
		getFocusComponent(this);
		//if there is a component which has focus, return that component.
		if (enterButton == null)
		{
			getEnterButton(this);
		}
		return enterButton;
	}
	
	/**
	 * Sets the focus on the "enter" button if it is not already set.
	 * 
	 * @param aaComp Component
	 */
	public void getFocusComponent(Component aaComp)
	{
		// If enter button is set in the form, no need to get it.
		// This will be done when Enter button is other button like a 
		// No button.
		if (enterButton != null)
		{
			return;
		}

		if (aaComp instanceof Container)
		{

			Container laCont = (Container) aaComp;

			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getEnterButton(larrChildren[i]);
			}
		}

		//Get the focus control .
		if (aaComp.hasFocus())
		{
			enterButton = aaComp;
		}
	}
	
	/**
	 * Get the Help Button
	 * 
	 * @param aaComp Component
	 */
	public void getHelpButton(Component aaComp)
	{
		JButton laJButton = null;
		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getHelpButton(larrChildren[i]);
			}
		}

		//get help button
		if (aaComp instanceof JButton)
		{
			laJButton = ((JButton) aaComp);
			if (laJButton.getText().equals("Help"))
			{
				helpButton = laJButton;
			}
		}
	}
	
	/**
	 * Get the previous table component
	 * 
	 * @param aaComp Component
	 */
	public void getPrevTableComponent(Component aaComp)
	{
		if (caNextFocus != null)
		{
			return;
		}

		Component laComp = null;
		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				getPrevTableComponent(larrChildren[i]);
			}
		}

		//get next focusable component
		if (aaComp instanceof JComponent)
		{
			laComp = ((JComponent) aaComp).getNextFocusableComponent();
			if (laComp != null && laComp.equals(caRT))
			{
				{
					if (aaComp.isFocusTraversable()
						&& aaComp.isEnabled()
						&& aaComp.isVisible())
					{
						caNextFocus = aaComp;
					}
				}
			}
		}
	}
	
	/**
	 * Initialize the frame
	 */
	private void initialize()
	{
		setDefaultCloseOperation(
			javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setResizable(false);
		addKeyAndContainerListenerRecursively(this);
		addWindowListener(this);

	}
	
	/**
	 * Returns a boolean indicating that the frame will be located 
	 * in a position other than the middle of the screen.
	 * 
	 * @return boolean
	 */
	public boolean isManagingLocation()
	{
		return cbManagingLocation;
	}

	/**
	 * Returns the cbWorking boolean.
	 * 
	 * @return boolean
	 */
	public boolean isWorking()
	{
		return cbWorking;
	}

	/**
	 * Processes KeyPressedEvents
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_TAB
			&& aaKE.getModifiers() != KeyEvent.SHIFT_MASK)
		{
			if (aaKE.getSource() instanceof RTSTable)
			{
				Component laComp =
					((RTSTable) aaKE.getSource())
						.getNextFocusableComponent();
				if (laComp != null)
				{
					laComp.requestFocus();
				}
			}
		}
		if (aaKE.getKeyCode() == KeyEvent.VK_TAB
			&& aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
		{
			if (aaKE.getSource() instanceof RTSTable)
			{
				caRT = (RTSTable) aaKE.getSource();
				caNextFocus = null;
				getPrevTableComponent(this);
				if (caNextFocus != null)
				{
					caNextFocus.requestFocus();
				}
			}
		}

		// If any default button is set for the form, click default 
		// button. Ignore this event if origin is any button. 
		// Buttons will take care of thenselves.
		if (aaKE.getKeyCode() == KeyEvent.VK_ENTER
			&& !(aaKE.getComponent() instanceof RTSButton))
		{
			JRootPane laJRootPane = this.getRootPane();
			if (laJRootPane != null)
			{
				JButton laJButton = laJRootPane.getDefaultButton();
				if (laJButton != null)
				{
					if (laJButton.isEnabled())
					{
						laJButton.requestFocus();
						laJButton.doClick();
						aaKE.consume();
					}
				}
			}
		}
	}
	
	/**
	 * Process KeyReleasedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		try
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				if (!cbCancelPressed)
				{
					return;
				}
				else
				{
					cbCancelPressed = false;
				}

				if (cbEscapeWorking)
				{
					return;
				}
				cbEscapeWorking = true;
				getCancelButton(this);
				if (cancelButton != null && cancelButton.isEnabled())
				{
					cancelButton.doClick();
				}
				cbEscapeWorking = false;
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_F1)
			{
				getHelpButton(this);
				if (helpButton != null && helpButton.isEnabled())
				{
					helpButton.doClick();
				}
			}

			int liLM = aaKE.getModifiers();

			// Only char is pressed and component is combo box or table,
			// do not do anything
			if (liLM != KeyEvent.CTRL_MASK
				&& liLM != KeyEvent.ALT_MASK
				&& (aaKE.getSource() instanceof MetalComboBoxButton
					|| aaKE.getSource() instanceof RTSTable
					|| aaKE.getSource() instanceof JComboBox
				// defect 8355
					|| aaKE.getSource() instanceof JTextArea))
				// end defect 8355
			{
				return;
			}

			// If cursor is in input field and no ctrl or alt is 
			// pressed, discard it.
			// CTRL +V, CTRL C SHIFT in text box should be discarded.
			if (aaKE.getSource() instanceof RTSInputField
				|| aaKE.getSource() instanceof RTSTextArea
				|| aaKE.getSource() instanceof RTSPasswordField
				|| aaKE.getSource() instanceof RTSDateField
				|| aaKE.getSource() instanceof RTSTimeField)
			{
				if ((liLM == KeyEvent.CTRL_MASK
					&& (aaKE.getKeyCode() == (int) 'v'
						|| aaKE.getKeyCode() == (int) 'V'
						|| aaKE.getKeyCode() == (int) 'c'
						|| aaKE.getKeyCode() == (int) 'C'
						|| aaKE.getKeyCode() == (int) 'x'
						|| aaKE.getKeyCode() == (int) 'X'
						|| aaKE.getKeyCode() == (int) 'z'
						|| aaKE.getKeyCode() == (int) 'Z'))
					|| aaKE.getModifiers() == 0
					|| aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
				{
					return;
				}
			}
			if ((aaKE.getKeyCode() >= KeyEvent.VK_A
				&& aaKE.getKeyCode() <= KeyEvent.VK_Z)
				|| (aaKE.getKeyCode() >= KeyEvent.VK_0
					&& aaKE.getKeyCode() <= KeyEvent.VK_9))
			{
				processRequest(aaKE, this, aaKE.getKeyCode());
			}
		}
		finally
		{
			// reset the cancel pressed flag only if cancel action was
			// aborted by the user. This would happen
			// if user is prompted for yes no kind of choice. Checked
			// for the visibility of the frame bcos
			// even after calling super.dispose(), frame still exists.
			if (this.isVisible())
			{
				cbCancelPressed = true;
			}
		}
	}
	
	/**
	 * Handle KeyTypedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// empty code block
		// must be implemented, but we do not have any logic need here
	}
	
	/**
	 * Handle othere KeyEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	protected void processKeyEvent(KeyEvent aaKE)
	{
		// empty code block
		// must be implemented, but we do not have any logic need here.
	}
	
	/**
	 * Process a KeyEvent for a Component.
	 * 
	 * @param e java.awt.event.KeyEvent
	 */
	public void processRequest(
		KeyEvent aaKE,
		Component aaComp,
		int aiCha)
	{
		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			Component[] larrChildren = laCont.getComponents();
			for (int i = 0; i < larrChildren.length; i++)
			{
				processRequest(aaKE, larrChildren[i], aiCha);
			}
		}
		String lsStr1 = String.valueOf((char) aiCha).toUpperCase();

		//process for radio button.
		if (aaComp instanceof JRadioButton)
		{
			int liLCH = ((JRadioButton) aaComp).getMnemonic();
			String lsStr2 = String.valueOf((char) liLCH).toUpperCase();
			if (lsStr2.equals(lsStr1))
			{
				if (aaComp.isEnabled())
				{
					((JRadioButton) aaComp).setSelected(true);
					((JRadioButton) aaComp).doClick();
					//Commented to avoid requesting focus
					if (cbRequestFocus)
					{
						((JRadioButton) aaComp).requestFocus();
					}
				}
			}
		}

		// Process for RTSButton or JButton      
		if (aaComp instanceof RTSButton || aaComp instanceof JButton)
		{
			int liLCH = ((JButton) aaComp).getMnemonic();
			String lsStr2 = String.valueOf((char) liLCH).toUpperCase();

			if (lsStr2.equals(lsStr1))
			{
				if (aaComp.isEnabled())
				{
					((JButton) aaComp).doClick();
				}
			}
		}
		//process for check boxes.
		if (aaComp instanceof JCheckBox)
		{
			int liLCH = ((JCheckBox) aaComp).getMnemonic();
			String lsStr2 = String.valueOf((char) liLCH).toUpperCase();
			if (lsStr2.equals(lsStr1))
			{
				if (aaComp.isEnabled())
				{
					JCheckBox laJCheckBox = (JCheckBox) aaComp;
					laJCheckBox.doClick();
					if (cbRequestFocus)
					{
						laJCheckBox.requestFocus();
					}
				}
			}
		}
		//process for Labels.
		if (aaComp instanceof JLabel)
		{
			int liLCH = ((JLabel) aaComp).getDisplayedMnemonic();
			String lsStr2 = String.valueOf((char) liLCH).toUpperCase();
			if (lsStr2.equals(lsStr1))
			{
				JLabel laJLabel = (JLabel) aaComp;
				Component com = laJLabel.getLabelFor();
				if (com != null)
				{
					if (com.isEnabled())
					{
						com.requestFocus();
					}
				}
			}
		}
	}

	/**
	 * Remove the key and container listeners recursively.
	 * 
	 * @param aaComp Component
	 */
	private void removeKeyAndContainerListenerRecursively(Component aaComp)
	{
		aaComp.removeKeyListener(this);

		if (aaComp instanceof Container)
		{
			Container laCont = (Container) aaComp;
			laCont.removeContainerListener(this);
			Component[] larrChildren = laCont.getComponents();

			for (int i = 0; i < larrChildren.length; i++)
			{
				removeKeyAndContainerListenerRecursively(larrChildren[i]);
			}
		}
	}
	
	/**
	 * Sets the controller for the RTSDialogBox
	 * 
	 * @param aaNewController Controller the view controller
	 */
	public void setController(AbstractViewController aaNewController)
	{
		caController = aaNewController;
	}
	
	/**
	 * All subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public abstract void setData(Object aaData);
	
	/**
	 * Sets the filed that will have focus by default when the JDialog
	 * is opened.  This should only be used in the special situations
	 * where the default focus field is not the actual first field on
	 * the screen by default.
	 * 
	 * @param aaComponent Component
	 */
	public void setDefaultFocusField(Component aaComponent)
	{
		caDefaultFocusField = aaComponent;
	}
	
	/**
	 * Set the RequestFocus boolean.
	 * 
	 * @param abNewRequestFocus boolean
	 */
	public void setRequestFocus(boolean abNewRequestFocus)
	{
		cbRequestFocus = abNewRequestFocus;
	}
	
	/**
	 * Set the boolean indicating if the frame is managing 
	 * its own location.
	 * 
	 * @param abManagingLocation boolean
	 */
	public void setManagingLocation(boolean abManagingLocation)
	{
		cbManagingLocation = abManagingLocation;
	}
	
	/**
	 * Sets the frame visible or not visible depending on the 
	 * boolean value.
	 * 
	 * @param abVisible boolean
	 */
	public void setVisibleRTS(boolean abVisible)
	{
		if (abVisible)
		{
			// if the frame is not managing location, put the 
			// frame at the center of the screen
			if (!isManagingLocation())
			{
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
						- getSize().height / 2)));
			}
			super.setVisible(true);
		}
		else
		{
			if (this
				instanceof com.txdot.isd.rts.services.util.event.BarCodeListener)
			{
				try
				{
					getController()
						.getMediator()
						.getAppController()
						.getBarCodeScanner()
						.removeBarCodeListener(
							(com
								.txdot
								.isd
								.rts
								.services
								.util
								.event
								.BarCodeListener) this);
				}
				catch (RTSException aeRTSEx)
				{
					// empty code block
					// assume the exception just means we could not
					// remove the barcode listener because it did not
					// exist.
				}
			}
			// defect 8756
			// If we are setting a window visible to false and it is not
			// the JFrame desktop we will run the JNI focus fix passing
			// the title of the window we are going to.  The JNI code
			// will only set the window to enabled if it is currentl
			// disabled.
			//super.dispose();
			// defect 11337
			//Window laOwner = getOwner();
			// end defect 11337
			super.dispose();
			
			// defect 11337
			//if (laOwner != null && laOwner instanceof JDialog)
			//{
			//	JniAdInterface.focusFix(((JDialog) laOwner).getTitle());
			//}
			// end defect 11337
			// end defect 8756
		}
	}

	/**
	 * Set the cbWorking boolean
	 * 
	 * @param abWorking boolean
	 */
	public void setWorking(boolean abWorking)
	{
		cbWorking = abWorking;
	}

	/**
	 * Call before action performed
	 * 
	 * @return boolean
	 */
	protected boolean startWorking()
	{
		if (cbWorking
			|| (getController() != null
				&& !getController().isAcceptingInput()))
		{
			return false;
		}
		else
		{
			setWorking(true);
			return true;
		}
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * <p>Reactivate the BarCodeListener for this frame.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		if (this instanceof BarCodeListener)
		{
			try
			{
				getController()
					.getMediator()
					.getAppController()
					.getBarCodeScanner()
					.addBarCodeListener((BarCodeListener) this);
			}
			catch (RTSException aeRTSEx)
			{
				// empty code block
				// it was reported already
			}
		}

		// defect 11371
		// Only move focus to the default focus field if it is visible
		// and enabled.
//		if (getDefaultFocusField() != null
//			&& getDefaultFocusField().isVisible()
//			&& getDefaultFocusField().isEnabled())
//		{
//			getDefaultFocusField().requestFocus();
//			
//			// set the default focus field to null so that it will not
//			// continue to get focus whenever the window is activated
//			setDefaultFocusField(null);
//		}
		// end defect 11371
	}
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * <p>We are not doing anything in this case.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(WindowEvent aaWE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * <p>We are not doing anything in this case.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosing(WindowEvent aaWE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * <p>Deactivate the BarCode Listener since we will not be on 
	 * this frame.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeactivated(WindowEvent aaWE)
	{
		if (this instanceof BarCodeListener)
		{
			try
			{
				getController()
					.getMediator()
					.getAppController()
					.getBarCodeScanner()
					.removeBarCodeListener((BarCodeListener) this);
			}
			catch (RTSException aeRTSEx)
			{
				// empty code block
				// assume that it is already reported.
			}
		}
	}
	
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * <p>Do nothing in this case.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeiconified(WindowEvent aaWE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * <p>Do nothing in this case.
	 * 
	 * @see Frame#setIconImage
	 * @param aaWE WindowEvent
	 */
	public void windowIconified(WindowEvent aaWE)
	{
		// empty code block
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>Sets focus to the default field.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		//defect 8494
		// Only move focus to the default focus field if it is visible
		// and enabled.
		// defect 11371
		if (getDefaultFocusField() != null
			&& getDefaultFocusField().isVisible()
			&& getDefaultFocusField().isEnabled())
		{
			getDefaultFocusField().requestFocus();
		}
		// end defect 11371
		// end defect 8494
	}
	
	/**
	 * Used to fix the combo box problem where the hot keys will not
	 * activate the next item in the list that matches the key that
	 * was pressed.
	 * <p>See emample below for the only situation where this occurs:
	 * <br>PASS - Default selected
	 * <br>PASS-TRK
	 * <br>TRACTOR
	 * <p>If PASS-TRK was the default selected then there would not be
	 * a problem.  This is only a problem when you load a JComboBox
	 * from a string that is held inside a data object.  If you load a
	 * JComboBox from string literals then this is not a problem.
	 * <p>The fix is to remove all of the listeners.  Look for an item
	 * that starts with a letter other than the one that is selected by
	 * default. Call selectWithKeyChar(char) with the char that was 
	 * found.  Call selectWithKeyChar(char) sending it back to the 
	 * first char of the default.  Set the defaulted selected back to 
	 * what it was and add the listeners back.
	 * 
	 * <p> A java bug has been submitted to Sun under Review ID: 603749
	 * 
	 * @param aeJCombo JComboBox
	 * @param asDefaultItem String
	 */
	public void comboBoxHotKeyFix(JComboBox aeJCombo)
	{
		if (aeJCombo == null)
		{
			return;
		}

		int liItemCount = aeJCombo.getItemCount();
		// No need to do the hot key fix if there is only one item in
		// then item list
		if (liItemCount > 1)
		{
			// Get the default Item so that we can set it back to the
			// default after we apply the work around.
			if (aeJCombo.getSelectedItem() == null)
			{
				return;
			}
			String lsDefaultItem =
				String.valueOf(aeJCombo.getSelectedItem());
			char lchDefaultFirstChar = lsDefaultItem.charAt(0);
			char lchNextItemFirstChar = Character.UNASSIGNED;
			// Get the first letter of an item that does not start with
			// the same letter as the default item.
			for (int i = 0; i < liItemCount; i++)
			{
				if (aeJCombo.getItemAt(i) == null)
				{
					continue;
				}
				// If the first letter of the default item does not 
				// match the first letter of the item then use this 
				// letter as the next letter.
				if (lchDefaultFirstChar
					!= String.valueOf(aeJCombo.getItemAt(i)).charAt(0))
				{
					lchNextItemFirstChar =
						String.valueOf(aeJCombo.getItemAt(i)).charAt(0);
					break;
				}
			}

			// If we found another char to use apply fix else return
			if (lchNextItemFirstChar != Character.UNASSIGNED)
			{
				// Remove the action listeners
				ActionListener[] laActionListeners =
					aeJCombo.getActionListeners();
				if (laActionListeners.length > 0)
				{
					for (int i = 0; i < laActionListeners.length; i++)
					{
						aeJCombo.removeActionListener(
							laActionListeners[i]);
					}
				}

				// Remove the item listeners
				ItemListener[] laItemListeners =
					aeJCombo.getItemListeners();
				if (laItemListeners.length > 0)
				{
					for (int i = 0; i < laItemListeners.length; i++)
					{
						aeJCombo.removeItemListener(laItemListeners[i]);
					}
				}

				// Remove the focus listeners
				FocusListener[] laFocusListeners =
					aeJCombo.getFocusListeners();
				if (laFocusListeners.length > 0)
				{
					for (int i = 0; i < laFocusListeners.length; i++)
					{
						aeJCombo.removeFocusListener(
							laFocusListeners[i]);
					}
				}

				// This is the actual fix
				aeJCombo.selectWithKeyChar(lchNextItemFirstChar);
				aeJCombo.selectWithKeyChar(lchDefaultFirstChar);
				// Set the default selected back
				aeJCombo.setSelectedItem(lsDefaultItem);

				// Add the action listeners back
				if (laActionListeners.length > 0)
				{
					for (int i = 0; i < laActionListeners.length; i++)
					{
						aeJCombo.addActionListener(
							laActionListeners[i]);
					}
				}

				// Add the item listeners back
				if (laItemListeners.length > 0)
				{
					for (int i = 0; i < laItemListeners.length; i++)
					{
						aeJCombo.addItemListener(laItemListeners[i]);
					}
				}

				// Add the focus listeners back
				if (laFocusListeners.length > 0)
				{
					for (int i = 0; i < laFocusListeners.length; i++)
					{
						aeJCombo.addFocusListener(laFocusListeners[i]);
					}
				}
			}
		}
	}
}
