package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.localoptions.PublishingReportData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPublishingSubstationUpdateAuthorityPUB002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * MAbs			05/14/2002	Enabled Help Button 
 * 							defect 3917
 * Min Wang		10/21/2002  Modified keyPressed() to handle Shift Tab. 
 *							defect 4881 
 * B Arredondo	03/12/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang		03/10/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang 	03/30/2005	Remove setNextFocusable's
 * 							defect 7891 ver 5.2.3
 * Min Wang		04/16/2005	Remove unused method
 * 							delete setDataToTxtArea()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		08/25/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	08/31/2008	Correct Implementation of Admin Logging 
 * 							add cvTblData,caPubTableModel,YES,NO,
 * 								REV_UPD_AUTH  
 * 							add getAdminLogData() 
 * 							delete cTblData,pubTableModel,RTS_LIENHOLDER,
 * 							  REVISE
 * 							delete getBuilderData() 
 * 							modify SUBSTASUBSCR,SUB
 * 							modify actionPerformed()   
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	08/22/2009	Implement new AdminLogData constructor(), 
 * 							RTSButtonGroup()  
 * 							add caRTSButtonGroup 
 * 							delete keyPressed() 
 * 							modify getAdminLogData(), initialize() 
 * 							defect 8628 Ver Defect_POS_F     
 * ---------------------------------------------------------------------
 */
/**
 * Frame for publishing update
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:		10/10/2001 11:11:53
 */
public class FrmPublishingSubstationUpdateAuthorityPUB002
	extends RTSDialogBox
	implements ActionListener, WindowListener, ListSelectionListener
{
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnRevise = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSTable ivjScrollPaneTable = null;
	private JScrollPane ivjtblPub = null;
	private TMPUB002 caPubTableModel = null;

	// defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628  

	private Vector cvTblData = null;

	// Constants 
	private final static String SECURITY = "RTS_SECURITY";
	private final static String REVISE_UPDT_AUTH = "RevUpdAuth";
	private final static String SUBSTASUBSCR = "Substa Subscr";
	private final static String SUB = "SUB ";
	private final static String YES = "YES";
	private final static String NO = "NO";
	private final static String PUB002_FRAME_TITLE =
		"Publishing Substation Update Authority    PUB002";

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002()
	{
		super();
		initialize();
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmPublishingSubstationUpdateAuthorityPUB002(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
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
			clearAllColor(this);
			if (aaAE.getSource() == getbtnRevise())
			{
				int liSel = getScrollPaneTable().getSelectedRow();

				// defect 8595
				// Correct RTS_ADMIN_LOG processing
				PublishingReportData laScreenPublishData =
					(PublishingReportData) cvTblData.get(liSel);

				PublishingReportData laNewPublishRptData =
					(PublishingReportData) UtilityMethods.copy(
						laScreenPublishData);

				// TblUpdtIndi: 0 -> 1; 1-> 0
				int liNewValue =
					(laScreenPublishData.getTblUpdtIndi() + 1) % 2;
				RTSDate laRTSDate = new RTSDate();
				if (laScreenPublishData.getTblName().equals(SECURITY))
				{
					laScreenPublishData.setChngTimestmp(laRTSDate);
				}
				else
				{
					laNewPublishRptData.setTblUpdtIndi(liNewValue);
					laNewPublishRptData.setChngTimestmp(laRTSDate);
					Vector lvData = new Vector();
					lvData.add(laNewPublishRptData);
					lvData.add(getAdminLogData(laNewPublishRptData));
					getController().processData(
						VCPublishingSubstationUpdateAuthorityPUB002
							.REVISE,
						lvData);
					if (RTSApplicationController.isDBReady())
					{
						laScreenPublishData.setTblUpdtIndi(liNewValue);
						laScreenPublishData.setChngTimestmp(laRTSDate);
					}
				}
				// end defect 8595
				getScrollPaneTable().repaint();

			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.PUB002);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return populated AdminLogData
	 * 
	 * @param aaPubRptData 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(PublishingReportData aaPublishRptData)
	{
		// defect 8628 
		AdministrationLogData laAdminlogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 
		laAdminlogData.setAction(REVISE_UPDT_AUTH);
		laAdminlogData.setEntity(SUBSTASUBSCR);
		String lsTblName = aaPublishRptData.getTblName();
		String lsEntVal =
			SUB
				+ UtilityMethods.addPadding(
					Integer.toString(aaPublishRptData.getSubstaId()),
					2,
					" ")
				+ " "
				+ UtilityMethods.addPaddingRight(
					lsTblName.substring(4),
					13,
					" ")
				+ " "
				+ (aaPublishRptData.getTblUpdtIndi() == 1 ? YES : NO);
		laAdminlogData.setEntityValue(lsEntVal);
		return laAdminlogData;
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setBounds(260, 413, 91, 25);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnHelp property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setMnemonic('H');
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setBounds(445, 413, 77, 25);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the btnRevise property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setName("btnRevise");
				ivjbtnRevise.setMnemonic('R');
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setBounds(92, 413, 91, 25);
				// user code begin {1}
				ivjbtnRevise.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRevise;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return  JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
					gettblPub(),
					gettblPub().getName());
				getRTSDialogBoxContentPane().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getRTSDialogBoxContentPane().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getRTSDialogBoxContentPane().add(
					getbtnHelp(),
					getbtnHelp().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				gettblPub().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				gettblPub().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjScrollPaneTable.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_OFF);
				ivjScrollPaneTable.setModel(new TMPUB002());
				ivjScrollPaneTable.setOpaque(true);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setGridColor(java.awt.Color.white);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new Dimension(0, 0));
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caPubTableModel =
					(TMPUB002) ivjScrollPaneTable.getModel();
				TableColumn a =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				a.setPreferredWidth(25);
				TableColumn b =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				b.setPreferredWidth(200);
				TableColumn c =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(2));
				c.setPreferredWidth(150);
				TableColumn d =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(3));
				d.setPreferredWidth(222);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable
					.getSelectionModel()
					.addListSelectionListener(
					this);
				ivjScrollPaneTable.setBackground(Color.white);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the tblPub property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane gettblPub()
	{
		if (ivjtblPub == null)
		{
			try
			{
				ivjtblPub = new JScrollPane();
				ivjtblPub.setName("tblPub");
				ivjtblPub.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjtblPub.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjtblPub.setBackground(java.awt.Color.white);
				ivjtblPub.setBounds(24, 43, 600, 352);
				gettblPub().setViewportView(getScrollPaneTable());
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblPub;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			addWindowListener(this);
			// user code end
			setName("FrmPublishingSubstationUpdateAuthorityPUB002");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(640, 480);
			setTitle(PUB002_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// defect 8628 
		caRTSButtonGroup.add(getbtnRevise()); 
		caRTSButtonGroup.add(getbtnCancel());
		caRTSButtonGroup.add(getbtnHelp());
		// end defect 8628 
		getScrollPaneTable().requestFocus();
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPublishingSubstationUpdateAuthorityPUB002 laFrmPublishingSubstationUpdateAuthorityPUB002;
			laFrmPublishingSubstationUpdateAuthorityPUB002 =
				new FrmPublishingSubstationUpdateAuthorityPUB002();
			laFrmPublishingSubstationUpdateAuthorityPUB002.setModal(
				true);
			laFrmPublishingSubstationUpdateAuthorityPUB002
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aeWE)
				{
					System.exit(0);
				};
			});
			laFrmPublishingSubstationUpdateAuthorityPUB002.show();
			Insets insets =
				laFrmPublishingSubstationUpdateAuthorityPUB002
					.getInsets();
			laFrmPublishingSubstationUpdateAuthorityPUB002.setSize(
				laFrmPublishingSubstationUpdateAuthorityPUB002
					.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPublishingSubstationUpdateAuthorityPUB002
					.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPublishingSubstationUpdateAuthorityPUB002
				.setVisibleRTS(
				true);
		}
		catch (Throwable leIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			Vector lvData = (Vector) aaDataObject;
			cvTblData = lvData;
			caPubTableModel.add(cvTblData);
			if (cvTblData != null && cvTblData.size() > 0)
			{
				getScrollPaneTable().setRowSelectionInterval(0, 0);
			}
		}
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aeLSE ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aeLSE)
	{
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aeWE WindowEvent  
	 */
	public void windowActivated(WindowEvent aeWE)
	{
		if (cvTblData == null)
		{
			GeneralSearchData laGSData = new GeneralSearchData();
			laGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			getController().processData(
				AbstractViewController.SEARCH,
				laGSData);
		}
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowClosed(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
		 * @param aeWE WindowEvent 
	 */
	public void windowClosing(java.awt.event.WindowEvent e)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowDeactivated(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowDeiconified(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowIconified(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowOpened(WindowEvent aeWE)
	{
		//empty code block
	}
}
