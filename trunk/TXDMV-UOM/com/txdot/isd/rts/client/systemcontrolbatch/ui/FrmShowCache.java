package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;

/*
 *
 * FrmShowCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/15/2005	Replaced call to setBackingStoreEnabled
 * 							to setScrollMode because the first was 
 * 							deprecated.  Also added display of GUI 
 * 							exceptions.
 *							modify getcacheTable(), handleException(), 
 *								main()
 *							defect 7897 Ver 5.2.3
 * Ray Rowehl	03/28/2005	Fixed a few execution problems.
 * 							defect 7897 Ver 5.2.3
 * S Johnston	07/08/2005	Java 1.4 Cleanup and constants creation
 * 							defect 7897 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	02/05/2007	Enlarge frame to enable viewing of 
 * 							"SpecialRegistrationTransactionFunction"
 * 							when viewing cache.
 * 							modified via Visual Editor
 * 							defect 9085  Ver Special Plates
 * ---------------------------------------------------------------------
 */
/**
 * Shows the transaction cache objects and transaction times
 *
 * @version	Special Plates	02/05/2007
 * @author	Michael Abernethy
 * <br>Creation Date:		09/20/2001 10:27:15
 */
public class FrmShowCache
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjFrmShowCacheContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnCancel = null;
	private RTSTable ivjcacheTable = null;
	private TMShowCache caTableModel;
	private Vector cvData;
	/**
	 * FrmShowCache constructor.
	 */
	public FrmShowCache()
	{
		super();
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaEvent ActionEvent
	 */
	public void actionPerformed(ActionEvent aaEvent)
	{
		if (aaEvent.getSource() == getbtnCancel())
		{
			getController().processData(
				AbstractViewController.CANCEL,
				null);
		}
		else if (aaEvent.getSource() == getcacheTable())
		{
			TransactionCacheData laTempData =
				(TransactionCacheData) cvData.get(
					getcacheTable().getSelectedRow());
			getController().processData(
				AbstractViewController.ENTER,
				laTempData);
		}
	}
	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setMnemonic('c');
				ivjbtnCancel.setText(
					SystemControlBatchConstant.TXT_CANCEL);
				ivjbtnCancel.addActionListener(this);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjbtnCancel;
	}
	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getcacheTable()
	{
		if (ivjcacheTable == null)
		{
			try
			{
				ivjcacheTable = new RTSTable();
				ivjcacheTable.setName("cacheTable");
				getJScrollPane1().setColumnHeaderView(
					ivjcacheTable.getTableHeader());
				// defect 7897
				//getJScrollPane1().getViewport().
				//setBackingStoreEnabled(true);
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7897
				ivjcacheTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjcacheTable.setModel(new TMShowCache());
				ivjcacheTable.setShowVerticalLines(false);
				ivjcacheTable.setShowHorizontalLines(false);
				ivjcacheTable.setIntercellSpacing(new Dimension(0, 0));
				ivjcacheTable.setBounds(0, 0, 200, 200);
				caTableModel = (TMShowCache) ivjcacheTable.getModel();
				TableColumn laTableColA =
					ivjcacheTable.getColumn(
						ivjcacheTable.getColumnName(2));
				laTableColA.setPreferredWidth(270);
				TableColumn laTableColB =
					ivjcacheTable.getColumn(
						ivjcacheTable.getColumnName(1));
				laTableColB.setPreferredWidth(80);
				TableColumn laTableColC =
					ivjcacheTable.getColumn(
						ivjcacheTable.getColumnName(0));
				laTableColC.setPreferredWidth(80);
				TableColumn laTableColD =
					ivjcacheTable.getColumn(
						ivjcacheTable.getColumnName(3));
				laTableColD.setPreferredWidth(60);
				ivjcacheTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjcacheTable.init();
				laTableColA.setCellRenderer(
					ivjcacheTable.setColumnAlignment(RTSTable.LEFT));
				laTableColB.setCellRenderer(
					ivjcacheTable.setColumnAlignment(RTSTable.CENTER));
				laTableColC.setCellRenderer(
					ivjcacheTable.setColumnAlignment(RTSTable.CENTER));
				laTableColD.setCellRenderer(
					ivjcacheTable.setColumnAlignment(RTSTable.CENTER));
				ivjcacheTable.addActionListener(this);
				ivjcacheTable.setBackground(Color.white);
			}
			catch (Throwable aeThrowable)
			{
				handleException(aeThrowable);
			}
		}
		return ivjcacheTable;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(getcacheTable());
			}
			catch (Throwable aeThrowable)
			{
				handleException(aeThrowable);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmShowCacheContentPane()
	{
		if (ivjFrmShowCacheContentPane == null)
		{
			try
			{
				ivjFrmShowCacheContentPane = new JPanel();
				ivjFrmShowCacheContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjFrmShowCacheContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints laConstraintsJScrollPane1 =
					new GridBagConstraints();
				laConstraintsJScrollPane1.gridx = 1;
				laConstraintsJScrollPane1.gridy = 1;
				laConstraintsJScrollPane1.fill =
					GridBagConstraints.BOTH;
				laConstraintsJScrollPane1.weightx = 1.0;
				laConstraintsJScrollPane1.weighty = 1.0;
				laConstraintsJScrollPane1.ipadx = 490;
				laConstraintsJScrollPane1.ipady = 286;
				laConstraintsJScrollPane1.insets =
					new Insets(17, 19, 9, 19);
				getFrmShowCacheContentPane().add(
					getJScrollPane1(),
					laConstraintsJScrollPane1);

				GridBagConstraints laConstraintsbtnCancel =
					new GridBagConstraints();
				laConstraintsbtnCancel.gridx = 1;
				laConstraintsbtnCancel.gridy = 2;
				laConstraintsbtnCancel.ipadx = 56;
				laConstraintsbtnCancel.insets =
					new Insets(10, 210, 27, 211);
				getFrmShowCacheContentPane().add(
					getbtnCancel(),
					laConstraintsbtnCancel);
			}
			catch (Throwable aeThrowable)
			{
				handleException(aeThrowable);
			}
		}
		return ivjFrmShowCacheContentPane;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7897
		// Handle GUI exceptions this was just ignored before
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
		// end defect 7897
	}
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmShowCache");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(653, 400);
			setModal(true);
			setTitle(SystemControlBatchConstant.TITLE_FRM_SHOW_CACHE);
			setContentPane(getFrmShowCacheContentPane());
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
	}
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmShowCache laFrmShowCache;
			laFrmShowCache = new FrmShowCache();
			laFrmShowCache.setModal(true);
			laFrmShowCache.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmShowCache.show();
			Insets laInsets = laFrmShowCache.getInsets();
			laFrmShowCache.setSize(
				laFrmShowCache.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmShowCache.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7897
			// Changed to setVisibleRTS
			//laFrmShowCache.setVisible(true);
			laFrmShowCache.setVisibleRTS(true);
			// end defect 7897
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of com.txdot.isd.rts."
					+ "client.general.ui.RTSDialogBox");
			aeThrowable.printStackTrace(System.out);
		}
	}
	/**
	 * Set the Frame's Controller.
	 * 
	 * @param aaController AbstractViewController
	 */
	public void setController(AbstractViewController aaController)
	{
		super.setController(aaController);
	}
	/**
	 * All subclasses must implement this method - it sets the cvData 
	 * on the screen and is how the controller relays information to the
	 * view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			Vector lvVector = (Vector) aaDataObject;
			cvData = new Vector();
			for (int i = 0; i < lvVector.size(); i++)
			{
				Vector lvTemp = (Vector) lvVector.get(i);
				for (int j = 0; j < lvTemp.size(); j++)
				{
					cvData.add(lvTemp.get(j));
				}
			}
			caTableModel.add(cvData);
			if (cvData.size() > 0)
			{
				getcacheTable().setRowSelectionInterval(0, 0);
			}
		}
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
