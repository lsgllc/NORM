package com.txdot.isd.rts.client.general.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * ButtonPanel.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	04/03/2005	RTS 5.2.3 Code Cleanup
 * 							Organize imports, format source
 * 							remove setNextFocusable's
 * 							defect 7885 Ver 5.2.3
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							add keyPressed, keyReleased, keyTyped
 * 							defect 8240 Ver 5.2.3
 * B Hargrove	08/18/2005	RTS 5.2.3 Code Cleanup - replace strings 
 * 							with constants.
 * 							modify ButtonPanel()
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Contains the commonly used "Enter" button, "Cancel" button, and 
 * "Help" button <br>It can be treated like a common JButton as it 
 * throws ActionEvents and allows the addition of ActionListeners
 * <br>In addition it sets its own ActionCommands
 * 
 * @version	5.2.3			08/18/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		08/03/2001 16:02:21
 */
public class ButtonPanel extends JPanel implements KeyListener
{
	private RTSButton ivjbtnEnter;
	private RTSButton ivjbtnCancel;
	private RTSButton ivjbtnHelp;
	/**
	 * Creates a ButtonPanel with 3 RTSButtons
	 */
	public ButtonPanel()
	{
		super();
		setRequestFocusEnabled(false);

		setLayout(new FlowLayout());

		ivjbtnEnter = new RTSButton(CommonConstant.BTN_TXT_ENTER);
		ivjbtnEnter.setActionCommand(CommonConstant.BTN_TXT_ENTER);
		ivjbtnEnter.addKeyListener(this);

		ivjbtnCancel = new RTSButton(CommonConstant.BTN_TXT_CANCEL);
		ivjbtnCancel.setActionCommand(CommonConstant.BTN_TXT_CANCEL);
		ivjbtnCancel.addKeyListener(this);

		ivjbtnHelp = new RTSButton(CommonConstant.BTN_TXT_HELP);
		ivjbtnHelp.setMnemonic('H');
		ivjbtnHelp.setActionCommand(CommonConstant.BTN_TXT_HELP);
		ivjbtnHelp.addKeyListener(this);

		add(ivjbtnEnter);
		add(ivjbtnCancel);
		add(ivjbtnHelp);
	}
	/**
	 * convenience method to add actionlistener to all components of the
	 * buttonpanel
	 * 
	 * @param aaAL ActionListener
	 */
	public void addActionListener(ActionListener aaAL)
	{
		ivjbtnCancel.addActionListener(aaAL);
		ivjbtnEnter.addActionListener(aaAL);
		ivjbtnHelp.addActionListener(aaAL);
	}
	/**
	 * Returns the "Cancel" button.
	 * 
	 * @return RTSButton
	 */
	public RTSButton getBtnCancel()
	{
		return ivjbtnCancel;
	}
	/**
	 * Returns the "Enter" button.
	 * 
	 * @return RTSButton
	 */
	public RTSButton getBtnEnter()
	{
		return ivjbtnEnter;
	}
	/**
	 * Returns the "Help" button.
	 * 
	 * @return RTSButton
	 */
	public RTSButton getBtnHelp()
	{
		return ivjbtnHelp;
	}
	/**
	 * keyPressed
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// defect 8240
		// added handling of arrows keys on ButtonPanel
		if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			// Specify action for RIGHT ARROW KEY on BUTTON PANEL
			if (getBtnEnter().hasFocus())
			{
				// Go to Cancel if Enabled
				if (getBtnCancel().isEnabled())
				{
					getBtnCancel().requestFocus();
				}
				// If Cancel disabled go to Help if enabled
				else if (getBtnHelp().isEnabled())
				{
					getBtnHelp().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
			else if (getBtnCancel().hasFocus())
			{
				// Go to Help if Enabled
				if (getBtnHelp().isEnabled())
				{
					getBtnHelp().requestFocus();
				}
				// If Help disabled go to Enter if enabled
				else if (getBtnEnter().isEnabled())
				{
					getBtnEnter().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
			else if (getBtnHelp().hasFocus())
			{
				// Go to Enter if Enabled
				if (getBtnEnter().isEnabled())
				{
					getBtnEnter().requestFocus();
				}
				// If Enter disabled go to Cancel if enabled
				else if (getBtnCancel().isEnabled())
				{
					getBtnCancel().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		{
			// Specify action for LEFT ARROW KEY on BUTTON PANEL
			if (getBtnEnter().hasFocus())
			{
				// Go to Help if Enabled
				if (getBtnHelp().isEnabled())
				{
					getBtnHelp().requestFocus();
				}
				// If Help disabled go to Cancel if enabled
				else if (getBtnCancel().isEnabled())
				{
					getBtnCancel().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
			else if (getBtnCancel().hasFocus())
			{
				// Go to Enter if Enabled
				if (getBtnEnter().isEnabled())
				{
					getBtnEnter().requestFocus();
				}
				// If Enter disabled go to Help if enabled
				else if (getBtnHelp().isEnabled())
				{
					getBtnHelp().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
			else if (getBtnHelp().hasFocus())
			{
				// Go to Cancel if Enabled
				if (getBtnCancel().isEnabled())
				{
					getBtnCancel().requestFocus();
				}
				// If Cancel disabled go to Enter if enabled
				else if (getBtnEnter().isEnabled())
				{
					getBtnEnter().requestFocus();
				}
				else
				{
					// Else don't change focus
				}
			}
		}
		// end defect 8240
	}
	/**
	 * keyReleased
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// empty code block
	}
	/**
	 * keyTyped
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// empty code block
	}
	/**
	 * convenience method to remove actionlistener from all components
	 * in buttonpanel
	 * 
	 * @param aaAL ActionListener
	 */
	public void removeActionListener(ActionListener aaAL)
	{
		ivjbtnCancel.removeActionListener(aaAL);
		ivjbtnEnter.removeActionListener(aaAL);
		ivjbtnHelp.removeActionListener(aaAL);
	}
	/**
	 * Passing a JDialog which contains the ButtonPanel will make the 
	 * Enter button the default button for the entire JDialog
	 * 
	 * @param aaParent JDialog
	 */
	public void setAsDefault(JDialog aaParent)
	{
		aaParent.getRootPane().setDefaultButton(ivjbtnEnter);
	}
}