package com.txdot.isd.rts.client.help.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;
import com.txdot.isd.rts.client.general.ui.RTSTextArea;
import com.txdot.isd.rts.client.help.business.MessageHandler;
import com.txdot.isd.rts.services.data.MessageData;
import com.txdot.isd.rts.services.exception.RTSException;
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
 * Frame that manages the RTS messages.  This fame is updated 
 * automaticlly when it is visible with any new messages that are
 * received.
 *
 * @version	Broadcast Message	04/03/2007
 * @author	Jeff Seifert
 * <br>Creation Date:			03/24/2006 10:02:00
 */
public class FrmMessagesMES001
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, MouseListener, FocusListener
{
	private static final String CONFIRM_DEL = "Confim Delete!";
	private static final String CONFIRM_DEL_MES =
		"Are you sure that you want to delete this message.";
	private static final String DEL_ICON = "/images/delete.gif";
	private static final String DEL_TIP = "Delete Message";
	private static final String DIALOG = "Dialog";
	private static final String EXIT_ICON = "/images/exit.gif";
	private static final String EXIT_TIP = "Exit";
	private static final String MARK_READ_ICON =
		"/images/mark-read.gif";
	private static final String MARK_READ_TIP = "Mark as Read";
	private static final String NEXT_ICON = "/images/next-message.gif";
	private static final String NEXT_TIP = "Next Message";
	private static final String PREV_ICON =
		"/images/previous-message.gif";
	private static final String PREV_TIP = "Previous Message";
	private static final String PRINT_ICON =
		"/images/print-message.gif";
	private static final String PRINT_TIP = "Print Message";
	private static final String REPLY_ICON = "/images/reply.gif";
	private static final String REPLY_TIP = "Reply to Message";

	/**
	 * Used to run standalone.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmMessagesMES001 laFrmMessagesMES001;
			laFrmMessagesMES001 = new FrmMessagesMES001();
			laFrmMessagesMES001.setModal(true);
			laFrmMessagesMES001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmMessagesMES001.show();
			java.awt.Insets insets = laFrmMessagesMES001.getInsets();
			laFrmMessagesMES001.setSize(
				laFrmMessagesMES001.getWidth()
					+ insets.left
					+ insets.right,
				laFrmMessagesMES001.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmMessagesMES001.setVisible(true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}
	
	private TMMES001 caTMModel;
	private javax.swing.JLabel jLabelDate = null;
	private javax.swing.JLabel jLabelFrom = null;
	private javax.swing.JLabel jLabelFromValue = null;
	private javax.swing.JLabel jLabelSubject = null;
	private javax.swing.JLabel jLabelSubjectValue = null;
	private javax.swing.JLabel jLabelTo = null;
	private javax.swing.JLabel jLabelToValue = null;
	private javax.swing.JPanel jPanelLeft = null;
	private javax.swing.JPanel jPanelMess = null;
	private javax.swing.JPanel jPanelMessInfo = null;
	private javax.swing.JPanel jPanelRight = null;
	private javax.swing.JScrollPane jScrollPaneMessList = null;
	private javax.swing.JScrollPane jScrollPaneText = null;
	private javax.swing.JSplitPane jSplitPaneContentPane = null;
	private javax.swing.JSplitPane jSplitPaneMess = null;
	private RTSTable jTableMessList = null;
	private RTSTextArea jTextAreaText = null;
	private javax.swing.JToolBar jToolBarMess = null;
	private RTSButton rTSButtonDel = null;
	private RTSButton rTSButtonExit = null;
	private RTSButton rTSButtonMarkRead = null;
	private RTSButton rTSButtonNext = null;
	private RTSButton rTSButtonPrev = null;
	private RTSButton rTSButtonPrint = null;
	private RTSButton rTSButtonReply = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public FrmMessagesMES001()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmMessagesMES001.java Constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmMessagesMES001(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * FrmMessagesMES001.java Constructor
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmMessagesMES001(JFrame aaOwner)
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
			int liRow = getJTableMessList().getSelectedRow();

			if (aaAE.getSource() == getRTSButtonNext())
			{
				getJTableMessList().unselectAllRows();
				getJTableMessList().setRowSelectionInterval(
					liRow + 1,
					liRow + 1);
				getJTableMessList().requestFocus();
			}
			else if (aaAE.getSource() == getRTSButtonPrev())
			{
				getJTableMessList().unselectAllRows();
				getJTableMessList().setRowSelectionInterval(
					liRow - 1,
					liRow - 1);
				getJTableMessList().requestFocus();
			}
			else if (aaAE.getSource() == getRTSButtonExit())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getRTSButtonDel())
			{
				if (new RTSException(RTSException.CTL001,
					CONFIRM_DEL_MES,
					CONFIRM_DEL)
					.displayError(this)
					== RTSException.YES)
				{
					MessageData laSelectedMess =
						(MessageData) caTMModel.getRow(
							getJTableMessList().getSelectedRow());
					getController().processData(
						BroadcastMsgConstants.DEL_MESSAGE,
						laSelectedMess.getMessageID());
				}
			}
			else if (aaAE.getSource() == getRTSButtonPrint())
			{
				MessageData laSelectedMess =
					(MessageData) caTMModel.getRow(
						getJTableMessList().getSelectedRow());
				getController().processData(
					BroadcastMsgConstants.PRINT_MESSAGE,
					laSelectedMess);
			}
			else if (aaAE.getSource() == getRTSButtonMarkRead())
			{
				MessageData laSelectedMess =
					(MessageData) caTMModel.getRow(
						getJTableMessList().getSelectedRow());
				getController().processData(
					BroadcastMsgConstants.MARK_MESS_READ,
					laSelectedMess.getMessageID());
			}
			else if (aaAE.getSource() == getRTSButtonReply())
			{
				MessageData laSelectedMess =
					(MessageData) caTMModel.getRow(
						getJTableMessList().getSelectedRow());
				getController().processData(
					BroadcastMsgConstants.REPLY_TO_MESSAGE,
					laSelectedMess);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Clears all of the fields that show the contents of an email. This
	 * is used when when all mail is deleted.
	 */
	private void clearMailFields()
	{
		getJLabelDate().setText(CommonConstant.STR_SPACE_EMPTY);
		getJLabelFromValue().setText(CommonConstant.STR_SPACE_EMPTY);
		getJLabelSubjectValue().setText(CommonConstant.STR_SPACE_EMPTY);
		getJLabelToValue().setText(CommonConstant.STR_SPACE_EMPTY);
		getJTextAreaText().setText(CommonConstant.STR_SPACE_EMPTY);
	}

	/**
	 * Disable and enable all of the buttons when there are messages or
	 * when all of the messages have been deleted.
	 * 
	 * @param abEnableValue boolean
	 */
	private void enableButtons(boolean abEnableValue)
	{
		getRTSButtonDel().setEnabled(abEnableValue);
		getRTSButtonMarkRead().setEnabled(abEnableValue);
		getRTSButtonPrint().setEnabled(abEnableValue);
		getRTSButtonReply().setEnabled(abEnableValue);

		// If the button that was just clicked is set enabled to
		// false then we must unpaint the border
		if (!abEnableValue)
		{
			getRTSButtonDel().setBorderPainted(abEnableValue);
			getRTSButtonMarkRead().setBorderPainted(abEnableValue);
			getRTSButtonPrint().setBorderPainted(abEnableValue);
			getRTSButtonReply().setBorderPainted(abEnableValue);
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
	 * This method initializes jLabelDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelDate()
	{
		if (jLabelDate == null)
		{
			jLabelDate = new javax.swing.JLabel();
			jLabelDate.setBounds(459, 1, 121, 20);
			jLabelDate.setFont(
				new java.awt.Font(DIALOG, java.awt.Font.PLAIN, 12));
			jLabelDate.setForeground(java.awt.Color.white);
			jLabelDate.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			jLabelDate.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
		}
		return jLabelDate;
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
			jLabelFromValue.setBounds(60, 1, 392, 20);
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
			jLabelSubjectValue.setBounds(60, 43, 392, 20);
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
			jLabelToValue.setBounds(60, 22, 392, 20);
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
	private javax.swing.JPanel getJPanelLeft()
	{
		if (jPanelLeft == null)
		{
			jPanelLeft = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout1 =
				new java.awt.FlowLayout();
			layFlowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			layFlowLayout1.setHgap(0);
			layFlowLayout1.setVgap(0);
			jPanelLeft.setLayout(layFlowLayout1);
			jPanelLeft.add(getRTSButtonReply(), null);
			jPanelLeft.add(getRTSButtonDel(), null);
			jPanelLeft.add(getRTSButtonMarkRead(), null);
			jPanelLeft.add(getRTSButtonPrint(), null);
			jPanelLeft.add(getRTSButtonPrev(), null);
			jPanelLeft.add(getRTSButtonNext(), null);
		}
		return jPanelLeft;
	}
	
	/**
	 * This method initializes jPanelMess
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelMess()
	{
		if (jPanelMess == null)
		{
			jPanelMess = new javax.swing.JPanel();
			jPanelMess.setLayout(new java.awt.BorderLayout());
			jPanelMess.add(
				getJPanelMessInfo(),
				java.awt.BorderLayout.NORTH);
			jPanelMess.add(
				getJScrollPaneText(),
				java.awt.BorderLayout.CENTER);
		}
		return jPanelMess;
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
			jPanelMessInfo.add(getJLabelDate(), null);
			jPanelMessInfo.add(getJLabelFromValue(), null);
			jPanelMessInfo.add(getJLabelToValue(), null);
			jPanelMessInfo.add(getJLabelSubjectValue(), null);
			jPanelMessInfo.setPreferredSize(
				new java.awt.Dimension(1, 65));
			jPanelMessInfo.setBackground(java.awt.Color.gray);
		}
		return jPanelMessInfo;
	}
	
	/**
	 * This method initializes jPanelRight
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelRight()
	{
		if (jPanelRight == null)
		{
			jPanelRight = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout1 =
				new java.awt.FlowLayout();
			layFlowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
			layFlowLayout1.setHgap(0);
			layFlowLayout1.setVgap(0);
			jPanelRight.setLayout(layFlowLayout1);
			jPanelRight.add(getRTSButtonExit(), null);
		}
		return jPanelRight;
	}
	
	/**
	 * This method initializes jScrollPaneMessList
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPaneMessList()
	{
		if (jScrollPaneMessList == null)
		{
			jScrollPaneMessList = new javax.swing.JScrollPane();
			jScrollPaneMessList.setViewportView(getJTableMessList());
			jScrollPaneMessList.setHorizontalScrollBarPolicy(
				javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPaneMessList;
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
			jScrollPaneText.setAutoscrolls(true);
			jScrollPaneText.setOpaque(true);
			jScrollPaneText.setViewportView(getJTextAreaText());
			jScrollPaneText.setPreferredSize(
				new java.awt.Dimension(600, 25));
			jScrollPaneText.setHorizontalScrollBarPolicy(
				javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPaneText;
	}
	
	/**
	 * This method initializes jSplitPaneContentPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private javax.swing.JSplitPane getJSplitPaneContentPane()
	{
		if (jSplitPaneContentPane == null)
		{
			jSplitPaneContentPane = new javax.swing.JSplitPane();
			jSplitPaneContentPane.setTopComponent(
				getJScrollPaneMessList());
			jSplitPaneContentPane.setBottomComponent(
				getJSplitPaneMess());
			jSplitPaneContentPane.setOrientation(
				javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPaneContentPane.setDividerSize(0);
			jSplitPaneContentPane.setDividerLocation(200);
		}
		return jSplitPaneContentPane;
	}
	
	/**
	 * This method initializes jSplitPaneMess
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private javax.swing.JSplitPane getJSplitPaneMess()
	{
		if (jSplitPaneMess == null)
		{
			jSplitPaneMess = new javax.swing.JSplitPane();
			jSplitPaneMess.setTopComponent(getJToolBarMess());
			jSplitPaneMess.setBottomComponent(getJPanelMess());
			jSplitPaneMess.setOrientation(
				javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPaneMess.setDividerSize(0);
			jSplitPaneMess.setDividerLocation(40);
		}
		return jSplitPaneMess;
	}
	
	/**
	 * This method initializes jTableMessList
	 * 
	 * @return javax.swing.JTable
	 */
	private RTSTable getJTableMessList()
	{
		if (jTableMessList == null)
		{
			jTableMessList = new RTSTable();
			getJScrollPaneMessList().setColumnHeaderView(
				jTableMessList.getTableHeader());
			jTableMessList.setModel(new TMMES001());
			caTMModel = (TMMES001) jTableMessList.getModel();
			getJScrollPaneMessList().getViewport().setScrollMode(
				JViewport.BACKINGSTORE_SCROLL_MODE);
			jTableMessList.setRowHeight(18);
			jTableMessList.setAutoCreateColumnsFromModel(false);
			jTableMessList.setIntercellSpacing(new Dimension(0, 0));
			jTableMessList.setColumnSize(0, 15);
			jTableMessList.setColumnSize(1, 25);
			jTableMessList.setColumnSize(2, 115);
			jTableMessList.setColumnSize(3, 325);
			jTableMessList.setColumnSize(4, 135);
			jTableMessList.setAutoResizeMode(
				javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			jTableMessList.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
			jTableMessList.init();

			jTableMessList
				.getSelectionModel()
				.addListSelectionListener(
				this);
			jTableMessList.addKeyListener(this);
		}
		return jTableMessList;
	}
	
	/**
	 * This method initializes jTextAreaText
	 * 
	 * @return RTSTextArea
	 */
	private RTSTextArea getJTextAreaText()
	{
		if (jTextAreaText == null)
		{
			jTextAreaText = new RTSTextArea();
			jTextAreaText.setEditable(false);
			jTextAreaText.setMargin(new java.awt.Insets(2, 4, 4, 2));
			jTextAreaText.setLineWrap(true);
			jTextAreaText.setWrapStyleWord(true);
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
			jToolBarMess.add(getJPanelLeft());
			jToolBarMess.add(getJPanelRight());
			jToolBarMess.setFloatable(false);
			jToolBarMess.setOrientation(
				javax.swing.JToolBar.HORIZONTAL);
			jToolBarMess.setFocusable(false);
		}
		return jToolBarMess;
	}
	
	/**
	 * This method initializes rTSButtonDel
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonDel()
	{
		if (rTSButtonDel == null)
		{
			rTSButtonDel = new RTSButton();
			rTSButtonDel.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(DEL_ICON)));
			rTSButtonDel.setFocusable(true);
			rTSButtonDel.setBorderPainted(false);
			rTSButtonDel.setToolTipText(DEL_TIP);
			rTSButtonDel.addActionListener(this);
			rTSButtonDel.addMouseListener(this);
			rTSButtonDel.addFocusListener(this);
			rTSButtonDel.setFocusPainted(false);

		}
		return rTSButtonDel;
	}
	
	/**
	 * This method initializes rTSButtonExit
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonExit()
	{
		if (rTSButtonExit == null)
		{
			rTSButtonExit = new RTSButton();
			rTSButtonExit.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(EXIT_ICON)));
			rTSButtonExit.setFocusable(true);
			rTSButtonExit.setBorderPainted(false);
			rTSButtonExit.setToolTipText(EXIT_TIP);
			rTSButtonExit.addActionListener(this);
			rTSButtonExit.addMouseListener(this);
			rTSButtonExit.addFocusListener(this);
			rTSButtonExit.setFocusPainted(false);
		}
		return rTSButtonExit;
	}
	
	/**
	 * This method initializes rTSButtonMarkRead
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonMarkRead()
	{
		if (rTSButtonMarkRead == null)
		{
			rTSButtonMarkRead = new RTSButton();
			rTSButtonMarkRead.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(MARK_READ_ICON)));
			rTSButtonMarkRead.setBorderPainted(false);
			rTSButtonMarkRead.setToolTipText(MARK_READ_TIP);
			rTSButtonMarkRead.setFocusable(true);
			rTSButtonMarkRead.addActionListener(this);
			rTSButtonMarkRead.addMouseListener(this);
			rTSButtonMarkRead.addFocusListener(this);
			rTSButtonMarkRead.setFocusPainted(false);
		}
		return rTSButtonMarkRead;
	}
	
	/**
	 * This method initializes rTSButtonNext
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonNext()
	{
		if (rTSButtonNext == null)
		{
			rTSButtonNext = new RTSButton();
			rTSButtonNext.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(NEXT_ICON)));
			rTSButtonNext.setBorderPainted(false);
			rTSButtonNext.setToolTipText(NEXT_TIP);
			rTSButtonNext.setFocusable(true);
			rTSButtonNext.addActionListener(this);
			rTSButtonNext.addMouseListener(this);
			rTSButtonNext.addFocusListener(this);
			rTSButtonNext.setFocusPainted(false);
		}
		return rTSButtonNext;
	}
	
	/**
	 * This method initializes rTSButtonPrev
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonPrev()
	{
		if (rTSButtonPrev == null)
		{
			rTSButtonPrev = new RTSButton();
			rTSButtonPrev.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(PREV_ICON)));
			rTSButtonPrev.setBorderPainted(false);
			rTSButtonPrev.setToolTipText(PREV_TIP);
			rTSButtonPrev.setFocusable(true);
			rTSButtonPrev.addActionListener(this);
			rTSButtonPrev.addMouseListener(this);
			rTSButtonPrev.addFocusListener(this);
			rTSButtonPrev.setFocusPainted(false);
		}
		return rTSButtonPrev;
	}
	
	/**
	 * This method initializes rTSButtonPrint
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonPrint()
	{
		if (rTSButtonPrint == null)
		{
			rTSButtonPrint = new RTSButton();
			rTSButtonPrint.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(PRINT_ICON)));
			rTSButtonPrint.setFocusable(true);
			rTSButtonPrint.setBorderPainted(false);
			rTSButtonPrint.setToolTipText(PRINT_TIP);
			rTSButtonPrint.addActionListener(this);
			rTSButtonPrint.addMouseListener(this);
			rTSButtonPrint.addFocusListener(this);
			rTSButtonPrint.setFocusPainted(false);
		}
		return rTSButtonPrint;
	}
	
	/**
	 * This method initializes rTSButtonReply
	 * 
	 * @return RTSButton
	 */
	private RTSButton getRTSButtonReply()
	{
		if (rTSButtonReply == null)
		{
			rTSButtonReply = new RTSButton();
			rTSButtonReply.setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(REPLY_ICON)));
			rTSButtonReply.setFocusable(true);
			rTSButtonReply.setBorderPainted(false);
			rTSButtonReply.setEnabled(false);
			rTSButtonReply.setToolTipText(REPLY_TIP);
			rTSButtonReply.addActionListener(this);
			rTSButtonReply.addMouseListener(this);
			rTSButtonReply.addFocusListener(this);
			rTSButtonReply.setFocusPainted(false);
		}
		return rTSButtonReply;
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setContentPane(getJSplitPaneContentPane());
		this.setSize(600, 500);
		this.setTitle(ScreenConstant.MES001_FRAME_TITLE);
		addWindowListener(this);
		RTSButtonGroup laRTSButtonGrp = new RTSButtonGroup();
		laRTSButtonGrp.add(getRTSButtonReply());
		laRTSButtonGrp.add(getRTSButtonDel());
		laRTSButtonGrp.add(getRTSButtonMarkRead());
		laRTSButtonGrp.add(getRTSButtonPrint());
		laRTSButtonGrp.add(getRTSButtonPrev());
		laRTSButtonGrp.add(getRTSButtonNext());
		laRTSButtonGrp.add(getRTSButtonExit());
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
			getRTSButtonExit().doClick();
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
		if (aaData != null && aaData instanceof Vector)
		{
			String lsSelMessageId = CommonConstant.STR_SPACE_EMPTY;
			int liSelectedRow = 0;
			if (isVisible() && caTMModel.getRowCount() > 0)
			{
				MessageData laSelectedMess =
					(MessageData) caTMModel.getRow(
						getJTableMessList().getSelectedRow());
				lsSelMessageId = laSelectedMess.getMessageID();
				caTMModel.add((Vector) aaData);
				for (int i = 0; i < caTMModel.getRowCount(); i++)
				{
					if (((MessageData) caTMModel.getRow(i))
						.getMessageID()
						.equals(lsSelMessageId))
					{
						liSelectedRow = i;
						break;
					}
				}
			}
			else
			{
				caTMModel.add((Vector) aaData);
			}

			if (((Vector) aaData).size() > 0)
			{
				enableButtons(true);
				setTableSelection(liSelectedRow);
				updateNextPrev(liSelectedRow, ((Vector) aaData).size());
			}
			else
			{
				enableButtons(false);
				clearMailFields();
				updateNextPrev(0, ((Vector) aaData).size());
			}
		}
	}
	
	/**
	 * Sets the table selection to a certain row that is passed.
	 * 
	 * @param aiSelectedRow int
	 */
	private void setTableSelection(int aiSelectedRow)
	{
		getJTableMessList().unselectAllRows();
		getJTableMessList().setRowSelectionInterval(
			aiSelectedRow,
			aiSelectedRow);
		getJTableMessList().requestFocus();
		getJScrollPaneMessList().getViewport().setViewPosition(
			new Point());
	}
	
	/**
	 * Update the next and previous buttons.
	 * 
	 * @param aiRow int
	 * @param aiRowCount int
	 */
	private void updateNextPrev(int aiRow, int aiRowCount)
	{
		if (aiRowCount <= 1)
		{
			getRTSButtonPrev().setEnabled(false);
			getRTSButtonPrev().setBorderPainted(false);
			getRTSButtonNext().setEnabled(false);
			getRTSButtonNext().setBorderPainted(false);
		}
		else
		{
			if (aiRow + 1 == aiRowCount)
			{
				getRTSButtonNext().setEnabled(false);
				getRTSButtonNext().setBorderPainted(false);
			}
			else
			{
				getRTSButtonNext().setEnabled(true);
			}

			if (aiRow + 1 > 1)
			{
				getRTSButtonPrev().setEnabled(true);
			}
			else
			{
				getRTSButtonPrev().setEnabled(false);
				getRTSButtonPrev().setBorderPainted(false);
			}
		}
	}
	
	/**
	 * When you select something in the table this method is called.
	 * 
	 * @param aaLSE ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		int liRow = getJTableMessList().getSelectedRow();
		if (liRow > -1)
		{
			MessageData laMess = (MessageData) caTMModel.getRow(liRow);
			int liRowCount = caTMModel.getRowCount();

			getJLabelFromValue().setText(
				laMess.getFrom().substring(
					0,
					laMess.getFrom().indexOf(CommonConstant.STR_AT)));
			getJLabelSubjectValue().setText(laMess.getSubject());
			getJLabelDate().setText(
				laMess.getDate()
					+ CommonConstant.STR_SPACE_ONE
					+ laMess.getDate().getTime());
			getJLabelToValue().setText(laMess.getTo());
			getJTextAreaText().setText(laMess.getMessage());
			if (laMess.isOpened())
			{
				getRTSButtonMarkRead().setEnabled(false);
				getRTSButtonMarkRead().setBorderPainted(false);
			}
			else
			{
				getRTSButtonMarkRead().setEnabled(true);
			}

			if (laMess.isReplyable())
			{
				getRTSButtonReply().setEnabled(true);
			}
			else
			{
				getRTSButtonReply().setEnabled(false);
				getRTSButtonReply().setBorderPainted(false);
			}

			updateNextPrev(liRow, liRowCount);
		}
	}
	
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(WindowEvent aaWE)
	{
		MessageHandler.setMsgClientVisible(false);
		super.windowClosed(aaWE);
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		MessageHandler.setMsgClientVisible(true);
		super.windowOpened(aaWE);
	}
}