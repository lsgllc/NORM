package com.txdot.isd.rts.client.help.ui;

import java.awt.Dialog;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.BroadcastMsgConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmMessagesMES001.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/03/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * This screen is used to reply to a message that has been sent to an 
 * RTS machine.  The only focusable compnent on this frame should be 
 * the JTextArea where the message is typed.  Messages that are sent 
 * from this frame will be sent to the senders Groupwise Inbox.
 *
 * @version	Broadcast Message	04/03/2007
 * @author	Jeff Seifert
 * <br>Creation Date:			04/12/2006 11:02:59
 */
public class FrmReplyMES002
	extends RTSDialogBox
	implements ActionListener, MouseListener, FocusListener
{
	private static final String ARROWS = ">>>";
	private static final String CANCEL_ICON = "/images/cancel.gif";
	private static final String CANCEL_TIP = "Exit";
	private static final String CONFIRM_CANCEL = "Confim Cancel!";
	private static final String CONFIRM_CANCEL_MSG =
		"Are you sure that you want cancel.";
	private static final String CONFIRM_SEND = "Confim Send!";
	private static final String CONFIRM_SEND_MSG =
		"Are you sure that you want send this message.";
	private static final String DIALOG = "Dialog";
	private static final String RE = "Re: ";

	private MessageData caData;
	private javax.swing.JLabel jLabelFrom = null;
	private javax.swing.JLabel jLabelFromValue = null;
	private javax.swing.JLabel jLabelSubject = null;
	private javax.swing.JLabel jLabelSubjectValue = null;
	private javax.swing.JLabel jLabelTo = null;
	private javax.swing.JLabel jLabelToValue = null;
	private javax.swing.JPanel jPanelButtons = null;
	private javax.swing.JPanel jPanelContentPane = null;
	private javax.swing.JPanel jPanelMessInfo = null;
	private javax.swing.JScrollPane jScrollPaneText = null;
	private javax.swing.JTextArea jTextAreaText = null;
	private javax.swing.JToolBar jToolBarMess = null;
	private RTSButton rTSButtonCancel = null;
	private RTSButton rTSButtonSend = null;

	/**
	 * Used to run standalone.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmReplyMES002 laFrmReplyMES002;
			laFrmReplyMES002 = new FrmReplyMES002();
			laFrmReplyMES002.setModal(true);
			laFrmReplyMES002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmReplyMES002.show();
			java.awt.Insets insets = laFrmReplyMES002.getInsets();
			laFrmReplyMES002.setSize(
				laFrmReplyMES002.getWidth()
					+ insets.left
					+ insets.right,
				laFrmReplyMES002.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmReplyMES002.setVisible(true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}

	/**
	 * This method initializes 
	 * 
	 */
	public FrmReplyMES002()
	{
		super();
		initialize();
	}

	/**
	 * FrmMessagesMES001.java Constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReplyMES002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmMessagesMES001.java Constructor
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmReplyMES002(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Handles action Events
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
			if (aaAE.getSource() == getRTSButtonCancel())
			{
				if (new RTSException(RTSException.CTL001,
					CONFIRM_CANCEL_MSG,
					CONFIRM_CANCEL)
					.displayError(this)
					== RTSException.YES)
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
			}
			else if (aaAE.getSource() == getRTSButtonSend())
			{
				if (new RTSException(RTSException.CTL001,
					CONFIRM_SEND_MSG,
					CONFIRM_SEND)
					.displayError(this)
					== RTSException.YES)
				{
					MessageData laMessage =
						(MessageData) UtilityMethods.copy(caData);
					laMessage.setMessage(getJTextAreaText().getText());
					laMessage.setSubject(
						getJLabelSubjectValue().getText());
					getController().processData(
						BroadcastMsgConstants.SND_MESSAGE,
						laMessage);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Handles Focus events.  Used to set the border painted on the
	 * buttons.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		if (((JButton) aaFE.getSource()).isEnabled())
		{
			((JButton) aaFE.getSource()).setBorderPainted(true);
		}
	}

	/**
	 * Handles Focus events.  Used to set the border painted on the
	 * buttons.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (((JButton) aaFE.getSource()).isEnabled())
		{
			((JButton) aaFE.getSource()).setBorderPainted(false);
		}
	}

	/**
	 * This method initializes jLabelFrom
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelFrom()
	{
		if (jLabelFrom == null)
		{
			jLabelFrom = new javax.swing.JLabel();
			jLabelFrom.setBounds(3, 1, 49, 20);
			jLabelFrom.setText(BroadcastMsgConstants.FROM_LABEL);
			jLabelFrom.setForeground(java.awt.Color.white);
			jLabelFrom.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			jLabelFrom.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
		}
		return jLabelFrom;
	}

	/**
	 * This method initializes jLabelFromValue
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelFromValue()
	{
		if (jLabelFromValue == null)
		{
			jLabelFromValue = new javax.swing.JLabel();
			jLabelFromValue.setBounds(60, 1, 285, 20);
			jLabelFromValue.setForeground(java.awt.Color.white);
			jLabelFromValue.setFont(
				new java.awt.Font(DIALOG, java.awt.Font.PLAIN, 12));
		}
		return jLabelFromValue;
	}

	/**
	 * This method initializes jLabelSubject
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelSubject()
	{
		if (jLabelSubject == null)
		{
			jLabelSubject = new javax.swing.JLabel();
			jLabelSubject.setBounds(3, 43, 49, 20);
			jLabelSubject.setText(BroadcastMsgConstants.SUB_LABEL);
			jLabelSubject.setForeground(java.awt.Color.white);
			jLabelSubject.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			jLabelSubject.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
		}
		return jLabelSubject;
	}

	/**
	 * This method initializes jLabelSubjectValue
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelSubjectValue()
	{
		if (jLabelSubjectValue == null)
		{
			jLabelSubjectValue = new javax.swing.JLabel();
			jLabelSubjectValue.setBounds(60, 43, 285, 20);
			jLabelSubjectValue.setForeground(java.awt.Color.white);
			jLabelSubjectValue.setFont(
				new java.awt.Font(DIALOG, java.awt.Font.PLAIN, 12));
		}
		return jLabelSubjectValue;
	}

	/**
	 * This method initializes jLabelTo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelTo()
	{
		if (jLabelTo == null)
		{
			jLabelTo = new javax.swing.JLabel();
			jLabelTo.setBounds(3, 22, 49, 20);
			jLabelTo.setText(BroadcastMsgConstants.TO_LABEL);
			jLabelTo.setForeground(java.awt.Color.white);
			jLabelTo.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			jLabelTo.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
		}
		return jLabelTo;
	}

	/**
	 * This method initializes jLabelToValue
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelToValue()
	{
		if (jLabelToValue == null)
		{
			jLabelToValue = new javax.swing.JLabel();
			jLabelToValue.setBounds(60, 22, 285, 20);
			jLabelToValue.setForeground(java.awt.Color.white);
			jLabelToValue.setFont(
				new java.awt.Font(DIALOG, java.awt.Font.PLAIN, 12));
		}
		return jLabelToValue;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelButtons()
	{
		if (jPanelButtons == null)
		{
			jPanelButtons = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout1 =
				new java.awt.FlowLayout();
			layFlowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			layFlowLayout1.setHgap(0);
			layFlowLayout1.setVgap(0);
			jPanelButtons.setLayout(layFlowLayout1);
			jPanelButtons.add(getRTSButtonSend(), null);
			jPanelButtons.add(getRTSButtonCancel(), null);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jPanelContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelContentPane()
	{
		if (jPanelContentPane == null)
		{
			jPanelContentPane = new javax.swing.JPanel();
			jPanelContentPane.setLayout(new java.awt.BorderLayout());
			jPanelContentPane.add(
				getJToolBarMess(),
				java.awt.BorderLayout.NORTH);
			jPanelContentPane.add(
				getJPanelMessInfo(),
				java.awt.BorderLayout.CENTER);
			jPanelContentPane.add(
				getJScrollPaneText(),
				java.awt.BorderLayout.SOUTH);
		}
		return jPanelContentPane;
	}

	/**
	 * This method initializes jPanelMessInfo
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelMessInfo()
	{
		if (jPanelMessInfo == null)
		{
			jPanelMessInfo = new javax.swing.JPanel();
			jPanelMessInfo.setLayout(null);
			jPanelMessInfo.add(getJLabelFrom(), null);
			jPanelMessInfo.add(getJLabelTo(), null);
			jPanelMessInfo.add(getJLabelSubject(), null);
			jPanelMessInfo.add(getJLabelFromValue(), null);
			jPanelMessInfo.add(getJLabelToValue(), null);
			jPanelMessInfo.add(getJLabelSubjectValue(), null);
			jPanelMessInfo.setPreferredSize(
				new java.awt.Dimension(500, 90));
			jPanelMessInfo.setBackground(java.awt.Color.gray);
		}
		return jPanelMessInfo;
	}

	/**
	 * This method initializes jScrollPaneText
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPaneText()
	{
		if (jScrollPaneText == null)
		{
			jScrollPaneText = new javax.swing.JScrollPane();
			jScrollPaneText.setViewportView(getJTextAreaText());
			jScrollPaneText.setPreferredSize(
				new java.awt.Dimension(500, 175));
			jScrollPaneText.setHorizontalScrollBarPolicy(
				javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPaneText;
	}

	/**
	 * This method initializes jTextAreaText
	 * 
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getJTextAreaText()
	{
		if (jTextAreaText == null)
		{
			jTextAreaText = new javax.swing.JTextArea();
			jTextAreaText.setMargin(new java.awt.Insets(2, 4, 4, 2));
			jTextAreaText.setLineWrap(true);
			jTextAreaText.setWrapStyleWord(true);
			jTextAreaText.addKeyListener(this);
		}
		return jTextAreaText;
	}

	/**
	 * This method initializes jToolBarMess
	 * 
	 * @return javax.swing.JToolBar
	 */
	private javax.swing.JToolBar getJToolBarMess()
	{
		if (jToolBarMess == null)
		{
			jToolBarMess = new javax.swing.JToolBar();
			jToolBarMess.add(getJPanelButtons());
			jToolBarMess.setFloatable(false);
			jToolBarMess.setOrientation(
				javax.swing.JToolBar.HORIZONTAL);
			jToolBarMess.setPreferredSize(
				new java.awt.Dimension(500, 35));
			jToolBarMess.setFocusable(false);
		}
		return jToolBarMess;
	}

	/**
	 * This method initializes rTSButtonCancel
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonCancel()
	{
		if (rTSButtonCancel == null)
		{
			rTSButtonCancel = new RTSButton();
			rTSButtonCancel.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(CANCEL_ICON)));
			rTSButtonCancel.setFocusable(true);
			rTSButtonCancel.setBorderPainted(false);
			rTSButtonCancel.setToolTipText(CANCEL_TIP);
			rTSButtonCancel.setPreferredSize(
				new java.awt.Dimension(28, 28));
			rTSButtonCancel.addActionListener(this);
			rTSButtonCancel.addMouseListener(this);
			rTSButtonCancel.addFocusListener(this);
			rTSButtonCancel.setFocusPainted(false);
		}
		return rTSButtonCancel;
	}

	/**
	 * This method initializes jButtonSend
	 * 
	 * @return javax.swing.JButton
	 */
	private RTSButton getRTSButtonSend()
	{
		if (rTSButtonSend == null)
		{
			rTSButtonSend = new RTSButton();
			rTSButtonSend.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(
						BroadcastMsgConstants.SEND_ICON)));
			rTSButtonSend.setFocusable(true);
			rTSButtonSend.setBorderPainted(false);
			rTSButtonSend.setToolTipText(
				BroadcastMsgConstants.SEND_TIP);
			rTSButtonSend.setPreferredSize(
				new java.awt.Dimension(28, 28));
			rTSButtonSend.addActionListener(this);
			rTSButtonSend.addMouseListener(this);
			rTSButtonSend.addFocusListener(this);
			rTSButtonSend.setFocusPainted(false);
		}
		return rTSButtonSend;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setContentPane(getJPanelContentPane());
		this.setSize(500, 300);
		this.setTitle(ScreenConstant.MES002_FRAME_TITLE);
		RTSButtonGroup laRTSButtonGrp = new RTSButtonGroup();
		laRTSButtonGrp.add(getRTSButtonSend());
		laRTSButtonGrp.add(getRTSButtonCancel());
	}

	/**
	 * Handles ESC.  We can't use the one build into RTSDialogBox b/c
	 * we do not have an RTSButton for Cancel.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getRTSButtonCancel().doClick();
			aaKE.consume();
		}
	}

	/**
	 * Handles Mouse events.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseClicked(MouseEvent aaME)
	{
		// EMPTY
	}

	/**
	 * Handles Mouse events.  Used to add the painted border on the 
	 * buttons when mouse enters the button.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseEntered(MouseEvent aaME)
	{
		if (((JButton) aaME.getSource()).isEnabled())
		{
			((JButton) aaME.getSource()).setBorderPainted(true);
		}
	}

	/**
	 * Handles Mouse events.  Used to remove the painted border on the 
	 * buttons when mouse exits the button.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseExited(MouseEvent aaME)
	{
		if (((JButton) aaME.getSource()).isEnabled()
			&& !((JButton) aaME.getSource()).hasFocus())
		{
			((JButton) aaME.getSource()).setBorderPainted(false);
		}
	}

	/**
	 * Handles Mouse events.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mousePressed(MouseEvent aaME)
	{
		// EMPTY
	}

	/**
	 * Handles Mouse events.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseReleased(MouseEvent aaME)
	{
		// EMPTY
	}

	/**
	 * Sets the frame up
	 * 
	 * @param aaData Onject
	 */
	public void setData(Object aaData)
	{
		if (aaData != null && aaData instanceof MessageData)
		{
			caData = (MessageData) aaData;
			MessageData laMess = (MessageData) aaData;
			getJLabelFromValue().setText(laMess.getTo());
			getJLabelSubjectValue().setText(RE + laMess.getSubject());
			String lsFromName =
				laMess.getFrom().substring(
					0,
					laMess.getFrom().indexOf(CommonConstant.STR_AT));
			getJLabelToValue().setText(lsFromName);
			getJTextAreaText().setText(
				CommonConstant.SYSTEM_LINE_SEPARATOR
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ ARROWS
					+ CommonConstant.STR_SPACE_ONE
					+ lsFromName
					+ CommonConstant.STR_SPACE_ONE
					+ laMess.getDate()
					+ CommonConstant.STR_SPACE_ONE
					+ laMess.getDate().getTime()
					+ CommonConstant.STR_SPACE_ONE
					+ ARROWS
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ laMess.getMessage());
			getJTextAreaText().setCaretPosition(0);
		}
	}
}