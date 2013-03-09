package com.txdot.isd.rts.client.accounting.ui;import java.awt.Dialog;import java.awt.Toolkit;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.util.HashMap;import javax.swing.*;import javax.swing.table.TableColumn;import com.txdot.isd.rts.client.general.ui.AbstractViewController;import com.txdot.isd.rts.client.general.ui.ButtonPanel;import com.txdot.isd.rts.client.general.ui.RTSDialogBox;import com.txdot.isd.rts.client.general.ui.RTSTable;import com.txdot.isd.rts.services.data.FundsDueData;import com.txdot.isd.rts.services.data.FundsPaymentData;import com.txdot.isd.rts.services.data.FundsPaymentDataList;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.RTSHelp;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.AccountingConstant;import com.txdot.isd.rts.services.util.constants.ScreenConstant;/* * * FrmFundsDetailsACC024.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * M Abernethy	09/07/2001  Added comments * MAbs			04/18/2002	Global change for startWorking() and  * 							doneWorking() * K Harrell 	11/21/2002 	Inquiry by Funds Due Date not showing data  * 							when no payments * 							defect 5053  * K Harrell	03/25/2004	JavaDoc Cleanup * 							Ver 5.2.0 * Ray Rowehl	03/21/2005	use getters for controller * 							modify actionPerformed() * 							defect 7884 Ver 5.2.3 * K Harrell	05/19/2005	renaming of elements within  * 							FundsPaymentDataList Object * 							modify setData() * 							defect 7899 Ver 5.2.3 * K Harrell	07/22/2005	Java 1.4 Work * 							defect 7884 Ver 5.2.3    * B Hargrove	08/10/2005	Modify to do nothing is user clicks the  * 							Windows 'Close' icon. * 							modify initialize()  * 							defect 6897 Ver 5.2.3 * K Harrell	08/17/2005	Remove keylistener from buttonPanel * 							components.  * 							modify getbuttonPanel()  * 							defect 8240 Ver 5.2.3 * K Harrell	07/01/2007	Enlarge column for FundsCategory * 							delete getBuilderData() * 							modify gettblFundsDetails() , initialize() * 							defect 9085 Ver Special Plates  * ---------------------------------------------------------------------  *//** * ACC024 is used in Funds Inquiry. *  * @version Special Plates	07/01/2007  * @author 	Michael Abernethy * <br>Creation Date:		06/12/2001 11:27:13 *//* &FrmFundsDetailsACC024& */public class FrmFundsDetailsACC024	extends RTSDialogBox	implements ActionListener{/* &FrmFundsDetailsACC024'ivjbuttonPanel& */	private ButtonPanel ivjbuttonPanel = null;/* &FrmFundsDetailsACC024'ivjlblFundsReportDate& */	private JLabel ivjlblFundsReportDate = null;/* &FrmFundsDetailsACC024'ivjstcLblFundsReportDate& */	private JLabel ivjstcLblFundsReportDate = null;/* &FrmFundsDetailsACC024'ivjJLabel1& */	private JLabel ivjJLabel1 = null;/* &FrmFundsDetailsACC024'ivjJInternalFrameContentPane& */	private JPanel ivjJInternalFrameContentPane = null;/* &FrmFundsDetailsACC024'ivjJScrollPane1& */	private JScrollPane ivjJScrollPane1 = null;/* &FrmFundsDetailsACC024'ivjJScrollPane2& */	private JScrollPane ivjJScrollPane2 = null;/* &FrmFundsDetailsACC024'ivjtblFundsDetails& */	private RTSTable ivjtblFundsDetails = null;/* &FrmFundsDetailsACC024'ivjtblPayments& */	private RTSTable ivjtblPayments = null;/* &FrmFundsDetailsACC024'caTableModelA& */	private TMACC024A caTableModelA;/* &FrmFundsDetailsACC024'caTableModelB& */	private TMACC024B caTableModelB;		// Object /* &FrmFundsDetailsACC024'caFundsPaymentDataList& */	private FundsPaymentDataList caFundsPaymentDataList;/* &FrmFundsDetailsACC024'DEFLT_DATE& */    private final static String DEFLT_DATE = "04/14/2001";/* &FrmFundsDetailsACC024'FUNDS_RPT_DT& */	private final static String FUNDS_RPT_DT = "Funds Report Date:";	/* &FrmFundsDetailsACC024'PAYMENTS& */	private final static String PAYMENTS = "Payments";/* &FrmFundsDetailsACC024'TITLE_ACC024& */	private final static String TITLE_ACC024 = 		"Funds Details/Payments   ACC024";	/**	 * FrmFundsDetails constructor comment.	 *//* &FrmFundsDetailsACC024.FrmFundsDetailsACC024& */	public FrmFundsDetailsACC024()	{		super();		initialize();	}	/**	 * Creates a ACC024 with parent	 * 	 * @param aaParent	Dialog 	 *//* &FrmFundsDetailsACC024.FrmFundsDetailsACC024$1& */	public FrmFundsDetailsACC024(Dialog aaParent)	{		super(aaParent);		initialize();	}	/**	 * Creates a ACC024 with parent	 * 	 * @param aaParent	JFrame 	 *//* &FrmFundsDetailsACC024.FrmFundsDetailsACC024$2& */	public FrmFundsDetailsACC024(JFrame aaParent)	{		super(aaParent);		initialize();	}	/**	 * Invoked when an action occurs.	 * 	 * @param aaAE	ActionEvent	 *//* &FrmFundsDetailsACC024.actionPerformed& */	public void actionPerformed(ActionEvent aaAE)	{		if (!startWorking())		{			return;		}		try		{			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())			{				getController().processData(					AbstractViewController.ENTER,					caFundsPaymentDataList);			}			else if (				aaAE.getSource() == getbuttonPanel().getBtnCancel())			{				getController().processData(					AbstractViewController.CANCEL,					caFundsPaymentDataList);			}			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())			{				RTSHelp.displayHelp(RTSHelp.ACC024);			}		}		finally		{			doneWorking();		}	}	/**	 * Return the ButtonPanel1 property value.	 * 	 * @return ButtonPanel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getbuttonPanel& */	private ButtonPanel getbuttonPanel()	{		if (ivjbuttonPanel == null)		{			try			{				ivjbuttonPanel = new ButtonPanel();				ivjbuttonPanel.setName("buttonPanel");				// user code begin {1}				ivjbuttonPanel.addActionListener(this);				ivjbuttonPanel.setAsDefault(this);				// defect 8240 				// ivjbuttonPanel.getBtnEnter().addKeyListener(this);				// ivjbuttonPanel.getBtnCancel().addKeyListener(this);				// ivjbuttonPanel.getBtnHelp().addKeyListener(this);				// end defect 8240 				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjbuttonPanel;	}	/**	 * Return the JInternalFrameContentPane property value.	 * 	 * @return javax.swing.JPanel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getJInternalFrameContentPane& */	private javax.swing.JPanel getJInternalFrameContentPane()	{		if (ivjJInternalFrameContentPane == null)		{			try			{				ivjJInternalFrameContentPane = new javax.swing.JPanel();				ivjJInternalFrameContentPane.setName(					"JInternalFrameContentPane");				ivjJInternalFrameContentPane.setLayout(					new java.awt.GridBagLayout());				java					.awt					.GridBagConstraints constraintsstcLblFundsReportDate =					new java.awt.GridBagConstraints();				constraintsstcLblFundsReportDate.gridx = 1;				constraintsstcLblFundsReportDate.gridy = 1;				constraintsstcLblFundsReportDate.ipadx = 9;				constraintsstcLblFundsReportDate.insets =					new java.awt.Insets(18, 23, 5, 5);				getJInternalFrameContentPane().add(					getstcLblFundsReportDate(),					constraintsstcLblFundsReportDate);				java					.awt					.GridBagConstraints constraintslblFundsReportDate =					new java.awt.GridBagConstraints();				constraintslblFundsReportDate.gridx = 2;				constraintslblFundsReportDate.gridy = 1;				constraintslblFundsReportDate.ipadx = 10;				constraintslblFundsReportDate.insets =					new java.awt.Insets(18, 6, 5, 378);				getJInternalFrameContentPane().add(					getlblFundsReportDate(),					constraintslblFundsReportDate);				java.awt.GridBagConstraints constraintsJScrollPane1 =					new java.awt.GridBagConstraints();				constraintsJScrollPane1.gridx = 1;				constraintsJScrollPane1.gridy = 2;				constraintsJScrollPane1.gridwidth = 2;				constraintsJScrollPane1.fill =					java.awt.GridBagConstraints.BOTH;				constraintsJScrollPane1.weightx = 1.0;				constraintsJScrollPane1.weighty = 1.0;				constraintsJScrollPane1.ipadx = 532;				constraintsJScrollPane1.ipady = 137;				constraintsJScrollPane1.insets =					new java.awt.Insets(6, 23, 8, 23);				getJInternalFrameContentPane().add(					getJScrollPane1(),					constraintsJScrollPane1);				java.awt.GridBagConstraints constraintsJScrollPane2 =					new java.awt.GridBagConstraints();				constraintsJScrollPane2.gridx = 1;				constraintsJScrollPane2.gridy = 4;				constraintsJScrollPane2.gridwidth = 2;				constraintsJScrollPane2.fill =					java.awt.GridBagConstraints.BOTH;				constraintsJScrollPane2.weightx = 1.0;				constraintsJScrollPane2.weighty = 1.0;				constraintsJScrollPane2.ipadx = 532;				constraintsJScrollPane2.ipady = 118;				constraintsJScrollPane2.insets =					new java.awt.Insets(3, 23, 3, 23);				getJInternalFrameContentPane().add(					getJScrollPane2(),					constraintsJScrollPane2);				java.awt.GridBagConstraints constraintsbuttonPanel =					new java.awt.GridBagConstraints();				constraintsbuttonPanel.gridx = 1;				constraintsbuttonPanel.gridy = 5;				constraintsbuttonPanel.gridwidth = 2;				constraintsbuttonPanel.fill =					java.awt.GridBagConstraints.BOTH;				constraintsbuttonPanel.weightx = 1.0;				constraintsbuttonPanel.weighty = 1.0;				constraintsbuttonPanel.ipadx = 114;				constraintsbuttonPanel.ipady = 15;				constraintsbuttonPanel.insets =					new java.awt.Insets(4, 134, 7, 135);				getJInternalFrameContentPane().add(					getbuttonPanel(),					constraintsbuttonPanel);				java.awt.GridBagConstraints constraintsJLabel1 =					new java.awt.GridBagConstraints();				constraintsJLabel1.gridx = 1;				constraintsJLabel1.gridy = 3;				constraintsJLabel1.gridwidth = 2;				constraintsJLabel1.ipadx = 99;				constraintsJLabel1.insets =					new java.awt.Insets(8, 25, 3, 419);				getJInternalFrameContentPane().add(					getJLabel1(),					constraintsJLabel1);				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJInternalFrameContentPane;	}	/**	 * Return the JLabel1 property value.	 * 	 * @return javax.swing.JLabel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getJLabel1& */	private javax.swing.JLabel getJLabel1()	{		if (ivjJLabel1 == null)		{			try			{				ivjJLabel1 = new javax.swing.JLabel();				ivjJLabel1.setName("JLabel1");				ivjJLabel1.setText(PAYMENTS);				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJLabel1;	}	/**	 * Return the JScrollPane1 property value.	 * 	 * @return javax.swing.JScrollPane	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getJScrollPane1& */	private javax.swing.JScrollPane getJScrollPane1()	{		if (ivjJScrollPane1 == null)		{			try			{				ivjJScrollPane1 = new javax.swing.JScrollPane();				ivjJScrollPane1.setName("JScrollPane1");				ivjJScrollPane1.setVerticalScrollBarPolicy(					javax						.swing						.JScrollPane						.VERTICAL_SCROLLBAR_AS_NEEDED);				ivjJScrollPane1.setHorizontalScrollBarPolicy(					javax						.swing						.JScrollPane						.HORIZONTAL_SCROLLBAR_AS_NEEDED);				getJScrollPane1().setViewportView(gettblFundsDetails());				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJScrollPane1;	}	/**	 * Return the JScrollPane2 property value.	 * 	 * @return javax.swing.JScrollPane	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getJScrollPane2& */	private javax.swing.JScrollPane getJScrollPane2()	{		if (ivjJScrollPane2 == null)		{			try			{				ivjJScrollPane2 = new javax.swing.JScrollPane();				ivjJScrollPane2.setName("JScrollPane2");				ivjJScrollPane2.setVerticalScrollBarPolicy(					javax						.swing						.JScrollPane						.VERTICAL_SCROLLBAR_AS_NEEDED);				ivjJScrollPane2.setHorizontalScrollBarPolicy(					javax						.swing						.JScrollPane						.HORIZONTAL_SCROLLBAR_AS_NEEDED);				getJScrollPane2().setViewportView(gettblPayments());				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJScrollPane2;	}	/**	 * Return the JLabel2 property value.	 * 	 * @return javax.swing.JLabel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getlblFundsReportDate& */	private javax.swing.JLabel getlblFundsReportDate()	{		if (ivjlblFundsReportDate == null)		{			try			{				ivjlblFundsReportDate = new javax.swing.JLabel();				ivjlblFundsReportDate.setName("lblFundsReportDate");				ivjlblFundsReportDate.setText(DEFLT_DATE);				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjlblFundsReportDate;	}	/**	 * Return the JLabel1 property value.	 * 	 * @return javax.swing.JLabel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.getstcLblFundsReportDate& */	private javax.swing.JLabel getstcLblFundsReportDate()	{		if (ivjstcLblFundsReportDate == null)		{			try			{				ivjstcLblFundsReportDate = new javax.swing.JLabel();				ivjstcLblFundsReportDate.setName(					"stcLblFundsReportDate");				ivjstcLblFundsReportDate.setText(FUNDS_RPT_DT);				ivjstcLblFundsReportDate.setHorizontalAlignment(					javax.swing.SwingConstants.LEFT);				// user code begin {1}				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjstcLblFundsReportDate;	}	/**	 * Return the ScrollPaneTable property value.	 * 	 * @return RTSTable	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.gettblFundsDetails& */	private RTSTable gettblFundsDetails()	{		if (ivjtblFundsDetails == null)		{			try			{				ivjtblFundsDetails = new RTSTable();				ivjtblFundsDetails.setName("tblFundsDetails");				getJScrollPane1().setColumnHeaderView(					ivjtblFundsDetails.getTableHeader());				getJScrollPane1().getViewport().setScrollMode(					JViewport.BACKINGSTORE_SCROLL_MODE);				ivjtblFundsDetails.setAutoResizeMode(					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);				ivjtblFundsDetails.setModel(					new com						.txdot						.isd						.rts						.client						.accounting						.ui						.TMACC024A());				ivjtblFundsDetails.setShowVerticalLines(false);				ivjtblFundsDetails.setShowHorizontalLines(false);				ivjtblFundsDetails.setIntercellSpacing(					new java.awt.Dimension(0, 0));				ivjtblFundsDetails.setBounds(0, 0, 230, 230);				// user code begin {1}				caTableModelA =					(TMACC024A) ivjtblFundsDetails.getModel();				TableColumn laTableColumnA =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(0));				laTableColumnA.setPreferredWidth(90);				TableColumn laTableColumnB =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(1));				laTableColumnB.setPreferredWidth(100);				TableColumn laTableColumnC =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(2));				laTableColumnC.setPreferredWidth(130);				TableColumn laTableColumnD =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(3));				laTableColumnD.setPreferredWidth(90);				TableColumn laTableColumnE =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(4));				laTableColumnE.setPreferredWidth(75);				TableColumn laTableColumnF =					ivjtblFundsDetails.getColumn(						ivjtblFundsDetails.getColumnName(5));				laTableColumnF.setPreferredWidth(75);				ivjtblFundsDetails.setSelectionMode(					ListSelectionModel.SINGLE_SELECTION);				ivjtblFundsDetails.init();				laTableColumnA.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.LEFT));				laTableColumnB.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				laTableColumnC.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				laTableColumnD.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				laTableColumnE.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.RIGHT));				laTableColumnF.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.RIGHT));				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtblFundsDetails;	}	/**	 * Return the ScrollPaneTable1 property value.	 * 	 * @return RTSTable	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.gettblPayments& */	private RTSTable gettblPayments()	{		if (ivjtblPayments == null)		{			try			{				ivjtblPayments = new RTSTable();				ivjtblPayments.setName("tblPayments");				getJScrollPane2().setColumnHeaderView(					ivjtblPayments.getTableHeader());				getJScrollPane2().getViewport().setScrollMode(					JViewport.BACKINGSTORE_SCROLL_MODE);				ivjtblPayments.setAutoResizeMode(					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);				ivjtblPayments.setModel(					new com						.txdot						.isd						.rts						.client						.accounting						.ui						.TMACC024B());				ivjtblPayments.setShowVerticalLines(false);				ivjtblPayments.setShowHorizontalLines(false);				ivjtblPayments.setIntercellSpacing(					new java.awt.Dimension(0, 0));				ivjtblPayments.setBounds(0, 0, 200, 200);				// user code begin {1}				caTableModelB = (TMACC024B) ivjtblPayments.getModel();				TableColumn laTableColumnA =					ivjtblPayments.getColumn(						ivjtblPayments.getColumnName(0));				laTableColumnA.setPreferredWidth(89);				TableColumn laTableColumnB =					ivjtblPayments.getColumn(						ivjtblPayments.getColumnName(1));				laTableColumnB.setPreferredWidth(100);				TableColumn laTableColumnC =					ivjtblPayments.getColumn(						ivjtblPayments.getColumnName(2));				laTableColumnC.setPreferredWidth(90);				TableColumn laTableColumnD =					ivjtblPayments.getColumn(						ivjtblPayments.getColumnName(3));				laTableColumnD.setPreferredWidth(110);				TableColumn laTableColumnE =					ivjtblPayments.getColumn(						ivjtblPayments.getColumnName(4));				laTableColumnE.setPreferredWidth(100);				ivjtblPayments.setSelectionMode(					ListSelectionModel.SINGLE_SELECTION);				ivjtblPayments.init();				laTableColumnA.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.RIGHT));				laTableColumnB.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				laTableColumnC.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.RIGHT));				laTableColumnD.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				laTableColumnE.setCellRenderer(					ivjtblFundsDetails.setColumnAlignment(						RTSTable.CENTER));				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtblPayments;	}	/**	 * Called whenever the part throws an exception.	 * 	 * @param aeException	Throwable 	 *//* &FrmFundsDetailsACC024.handleException& */	private void handleException(Throwable aeException)	{		RTSException leRTSEx =			new RTSException(				RTSException.JAVA_ERROR,				(Exception) aeException);		leRTSEx.displayError(this);	}	/**	 * Initialize the class.	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmFundsDetailsACC024.initialize& */	private void initialize()	{		try		{			// user code begin {1}			setLocation(				Toolkit.getDefaultToolkit().getScreenSize().width / 2					- getSize().width / 2,				Toolkit.getDefaultToolkit().getScreenSize().height / 2					- getSize().height / 2);			// user code end			setName("FrmFundsDetails");			// defect 6897			// Do nothing if user clicks 'Close' Icon			setDefaultCloseOperation(				WindowConstants.DO_NOTHING_ON_CLOSE);			// end defect 6897			// defect 9085 			setSize(630, 450);			// end defect 9085 			setModal(true);			setTitle(TITLE_ACC024);			setContentPane(getJInternalFrameContentPane());		}		catch (Throwable aeIVJEx)		{			handleException(aeIVJEx);		}		// user code begin {2}		// user code end	}		/**	 * main entrypoint - starts the part when it is run as an application	 * 	 * @param aarrArgs	String[]	 *//* &FrmFundsDetailsACC024.main& */	public static void main(String[] aarrArgs)	{		try		{			FrmFundsDetailsACC024 laFrmACC024 =				new FrmFundsDetailsACC024();			laFrmACC024.setVisible(true);		}		catch (Throwable aeException)		{			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);			aeException.printStackTrace(System.out);		}	}	/**	 * all subclasses must implement this method - it sets the data on 	 * the screen and is how the controller relays information	 * to the view	 * 	 * @param aaObject	Object	 *//* &FrmFundsDetailsACC024.setData& */	public void setData(Object aaObject)	{		HashMap lhmMap = (HashMap) aaObject;		caFundsPaymentDataList =			(FundsPaymentDataList) UtilityMethods.copy(				lhmMap.get(AccountingConstant.DATA));		caTableModelA.add(caFundsPaymentDataList.getFundsDue());		caTableModelB.add(caFundsPaymentDataList.getFundsPymnt());		if (caFundsPaymentDataList.getFundsDue().size() > 0)		{			gettblFundsDetails().setRowSelectionInterval(0, 0);		}		if (caFundsPaymentDataList.getFundsPymnt().size() > 0)		{			gettblPayments().setRowSelectionInterval(0, 0);		}		// defect 5053		if (caFundsPaymentDataList.getFundsPymnt().size() > 0)		{			getlblFundsReportDate().setText(				((FundsPaymentData) caFundsPaymentDataList					.getFundsPymnt()					.get(0))					.getFundsReportDate()					.toString());		}		else		{			getlblFundsReportDate().setText(				((FundsDueData) caFundsPaymentDataList					.getFundsDue()					.get(0))					.getFundsReportDate()					.toString());		}	}}/* #FrmFundsDetailsACC024# */