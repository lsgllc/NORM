package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;

/*
 *
 * FrmDetailCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify keyReleased()
 *							defect 7897 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify setData()
 * 							defect 8479 Ver 5.2.3
 * K Harrell	06/18/2007	Present InventoryAllocationData when 
 * 							encounter InventoryAllociationUIData
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/16/2009	Show entries in Alphabetical order
 * 							modify setData()
 * 							defect 10191 Ver Defect_POS_G   
 * ---------------------------------------------------------------------
 */
/**
 * Shows the contents of the an invidual TransactionCacheData object.
 *
 * @version	Defect_POS_G	10/16/2009
 * @author	Michael Abernethy
 * <br>Creation Date:		10/02/2001 15:11:26
 */
public class FrmDetailCache
	extends RTSDialogBox
	implements ActionListener
{
	private JComboBox ivjJComboBox1 = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel2 = null;
	private JPanel ivjFrmDetailCacheContentPane = null;
	private RTSButton ivjbtnCancel = null;
	/**
	 * FrmDetailCache constructor.
	 */
	public FrmDetailCache()
	{
		super();
		initialize();
	}

	/**
	 * actionPerformed - Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		dispose();
	}
	/**
	 * getbtnCancel - Return the btnCancel property value.
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
	 * getJComboBox1 - Return the JComboBox1 property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBox1()
	{
		if (ivjJComboBox1 == null)
		{
			try
			{
				ivjJComboBox1 = new JComboBox();
				ivjJComboBox1.setName("JComboBox1");
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJComboBox1;
	}
	/**
	 * getJLabel1 - Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel1()
	{
		if (ivjJLabel1 == null)
		{
			try
			{
				ivjJLabel1 = new JLabel();
				ivjJLabel1.setName("JLabel1");
				ivjJLabel1.setText(
					SystemControlBatchConstant.TXT_CLASS_TYPE);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJLabel1;
	}
	/**
	 * getJLabel2 - Return the JLabel2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel2()
	{
		if (ivjJLabel2 == null)
		{
			try
			{
				ivjJLabel2 = new JLabel();
				ivjJLabel2.setName("JLabel2");
				ivjJLabel2.setText(
					SystemControlBatchConstant.TXT_ATTRIBUTES);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjJLabel2;
	}
	/**
	 * getFrmDetailCacheContentPane - Return the 
	 * FrmDetailCacheContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmDetailCacheContentPane()
	{
		if (ivjFrmDetailCacheContentPane == null)
		{
			try
			{
				ivjFrmDetailCacheContentPane = new JPanel();
				ivjFrmDetailCacheContentPane.setName(
					"RTSFrmDetailCacheContentPane");
				ivjFrmDetailCacheContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints laConstraintsJComboBox1 =
					new GridBagConstraints();
				laConstraintsJComboBox1.gridx = 1;
				laConstraintsJComboBox1.gridy = 3;
				laConstraintsJComboBox1.gridwidth = 2;
				laConstraintsJComboBox1.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintsJComboBox1.weightx = 1.0;
				laConstraintsJComboBox1.ipadx = 238;
				laConstraintsJComboBox1.insets =
					new Insets(6, 68, 11, 68);
				getFrmDetailCacheContentPane().add(
					getJComboBox1(),
					laConstraintsJComboBox1);

				GridBagConstraints laConstraintsJLabel1 =
					new GridBagConstraints();
				laConstraintsJLabel1.gridx = 1;
				laConstraintsJLabel1.gridy = 1;
				laConstraintsJLabel1.gridwidth = 2;
				laConstraintsJLabel1.ipadx = 415;
				laConstraintsJLabel1.insets =
					new Insets(21, 10, 11, 13);
				getFrmDetailCacheContentPane().add(
					getJLabel1(),
					laConstraintsJLabel1);

				GridBagConstraints laConstraintsJLabel2 =
					new GridBagConstraints();
				laConstraintsJLabel2.gridx = 1;
				laConstraintsJLabel2.gridy = 2;
				laConstraintsJLabel2.ipadx = 22;
				laConstraintsJLabel2.insets = new Insets(12, 68, 6, 26);
				getFrmDetailCacheContentPane().add(
					getJLabel2(),
					laConstraintsJLabel2);

				GridBagConstraints laConstraintsbtnCancel =
					new GridBagConstraints();
				laConstraintsbtnCancel.gridx = 2;
				laConstraintsbtnCancel.gridy = 4;
				laConstraintsbtnCancel.ipadx = 28;
				laConstraintsbtnCancel.insets =
					new Insets(11, 27, 46, 200);
				getFrmDetailCacheContentPane().add(
					getbtnCancel(),
					laConstraintsbtnCancel);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjFrmDetailCacheContentPane;
	}
	/**
	 * handleException - Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// Uncomment the following lines to print uncaught exceptions
		// to stdout
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7170
		// create exception and display it for GUI problems
		RTSException leRE =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRE.displayError(this);
		// end defect 7170
	}
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmDetailCache");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(500, 200);
			setTitle(SystemControlBatchConstant.TITLE_FRM_DETAIL_CACHE);
			setContentPane(getFrmDetailCacheContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
	}
	/**
	 * keyReleased - Called when a key is released
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			// defect 7897
			// Changed setVisible to setVisibleRTS
			//setVisible(false);
			setVisibleRTS(false);
			// end defect 7897
		}
	}
	/**
	 * setData - All subclasses must implement this method it sets the
	 * data on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			TransactionCacheData laTemp =
				(TransactionCacheData) aaDataObject;
			Object laObj = laTemp.getObj();
			if (laObj instanceof InventoryAllocationUIData)
			{
				laObj =
					((InventoryAllocationUIData) laObj)
						.convertToInventoryAllocationData();
			}
			Displayable laData = (Displayable) laObj;
			getJLabel1().setText(laTemp.getObj().getClass().getName());

			// defect 10191 
			// Sorted list
			TreeMap laTree = new TreeMap(laData.getAttributes());
			Vector lvV = new Vector(laTree.entrySet());
			// end defect 10191
			
			getJComboBox1().removeAllItems();

			for (int i = 0; i < lvV.size(); i++)
			{
				getJComboBox1().addItem(lvV.get(i));
			}
			
			if (getJComboBox1().isEnabled())
			{
				comboBoxHotKeyFix(getJComboBox1());
			}
		}
	}
}
