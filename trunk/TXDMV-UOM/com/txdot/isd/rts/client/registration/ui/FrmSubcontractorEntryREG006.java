package com.txdot.isd.rts.client.registration.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.SubcontractorCache;
import com.txdot.isd.rts.services.data.SubcontractorData;
import com.txdot.isd.rts.services.data.SubcontractorHdrData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * SubcontractorEntryREG006.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()
 * N Ting		05/09/2002	 set beep when date is invalid 
 * 							defect 3833
 * K Harrell	03/20/2004	5.2.0 Merge. See PCR 34 comments. Formated.
 * 							Ver 5.2.0	
 * T Pederson	09/03/2004	On diskette entry, default issue date to 
 * 							current date in setData().	
 * 							Defect 7537 Ver 5.2.1
 * K Harrell	10/04/2004	Do not sort retrieved inventory
 *							modify setData()
 *							defect 7586 Ver 5.2.1 
 * B Hargrove	03/10/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	03/31/2005	Comment out setNextFocusableComponent() 
 *							modify getScrollPaneTable()
 *							defect 7894 Ver 5.2.3
 * K Harrell	05/09/2005	Java 1.4 Work
 * 							renamed constants 
 * 							modify gettxtSubcontractorId() 
 * 							defect 8020,8189 Ver 5.2.3
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                 
 * B Hargrove	07/01/2005	modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	12/02/2005	Remove reference to desktop, etc.
 * 							modify setData() 
 * 							defect 7894 Ver 5.2.3
 * K Harrell	12/14/2005	Remove reference to getchkPreSubconReport()
 * 							delete getchkPreSubconReport() 
 * 							modify getFrmSubcontractorEntryREG006ContentPane1()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	04/17/2006 	Shift-tab not working from Subcontractor Id
 * 							modify keyPressed()
 * 							defect 4733 Ver 5.2.3  
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove CTL001_TITLE
 * 							modify enterDateConfirmation()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							Present up to 5 Lines of Subcontractor Data
 * 							Info.  Enlarge Inventory Table.
 * 							add ivjlblSubconInfoLine1,ivjlblSubconInfoLine2,
 * 							 ivjlblSubconInfoLine3,ivjlblSubconInfoLine4,
 * 							 ivjlblSubconInfoLine5, get methods
 * 							delete Comparator, compare()  
 * 							delete ivjlblSubconCity, ivjlblSubconName, 
 * 							 ivjlblSubconSt,ivjlblSubconZp, get methods
 * 							delete getBuilderData() 
 * 							modify getJPanel1(), setData(), initialize(), 
 * 							 getstcLblSubcontractorId(), getJPane1(),
 * 							 cleanUpInvalidSubcon(), actionPerformed()
 * 							defect 10161 Ver POS_640  
 * K Harrell	03/15/2010	Validate that Issue Date Year greater than 
 * 							current year -1 
 * 							delete ERROR_MSG, ERROR_TITLE
 * 							add caToday, validateIssueDate()
 * 							delete validateDate() 
 * 							defect 10355 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**   
 * Frame for SubcontractorEntryREG006
 *
 * @version	POS_640  	03/15/2010
 * @author 	Administrator
 * <br>Creation Date:	06/26/2001 
 * 
 */
public class FrmSubcontractorEntryREG006
	extends RTSDialogBox
	implements ActionListener
// defect 10161 
//, Comparator
// end defect 10161 
{
	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjFrmSubcontractorEntryREG006ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblSubconId = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblIssueDate = null;
	private JLabel ivjstcLblSubcontractorId = null;
	private RTSDateField ivjtxtIssueDate = null;
	private RTSInputField ivjtxtSubcontractorId = null;

	// defect 10161 
	private JLabel ivjlblSubconInfoLine1 = null;
	private JLabel ivjlblSubconInfoLine2 = null;
	private JLabel ivjlblSubconInfoLine3 = null;
	private JLabel ivjlblSubconInfoLine4 = null;
	private JLabel ivjlblSubconInfoLine5 = null;

	// Vector 
	private Vector cvInfoLines = new Vector(5);
	// end defect 10161

	// boolean
	private boolean cbDisketteEntry = false;
	private boolean cbSubconIdError = false;

	// int 
	private int ciStoredSubconId;

	//Object
	private SubcontractorRenewalCacheData caSubconCacheData;

	// defect 10355 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());
	// end defect 10355 

	// Constant
	private static final String CTL001_MSG =
		"Issue Date is over 90 days old.\n Confirm issue date.";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSubcontractorEntryREG006 laFrmSubcontractorEntryREG006;
			laFrmSubcontractorEntryREG006 =
				new FrmSubcontractorEntryREG006();
			laFrmSubcontractorEntryREG006.setModal(true);
			laFrmSubcontractorEntryREG006
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSubcontractorEntryREG006.show();
			java.awt.Insets laInsets =
				laFrmSubcontractorEntryREG006.getInsets();
			laFrmSubcontractorEntryREG006.setSize(
				laFrmSubcontractorEntryREG006.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSubcontractorEntryREG006.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSubcontractorEntryREG006.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSubcontractorEntryREG006 constructor .
	 */
	public FrmSubcontractorEntryREG006()
	{
		super();
		initialize();
	}

	/**
	 * FrmSubcontractorEntryREG006 constructor comment.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmSubcontractorEntryREG006(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSubcontractorEntryREG006 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmSubcontractorEntryREG006(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is pressed
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			// ENTER 
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				if (!cbDisketteEntry)
				{
					//validate SubconId
					// defect 10161 
					// Implement isEmpty() 
					if (ciStoredSubconId == 0
						|| gettxtSubcontractorId().isEmpty()
						|| (!gettxtSubcontractorId().isEmpty()
							&& ciStoredSubconId
								!= Integer.parseInt(
									gettxtSubcontractorId()
										.getText())))
						// end defect 10161 
					{
						try
						{
							handleEnterSubconId();
						}
						catch (RTSException aeRTSEx)
						{
							RTSException leRTSEx2 = new RTSException();
							leRTSEx2.addException(
								aeRTSEx,
								gettxtSubcontractorId());
							leRTSEx2.displayError(this);
							cleanUpInvalidSubcon();
							leRTSEx2.getFirstComponent().requestFocus();
							return;
						}
					}
				}
				try
				{
					// Validate date and set date to header
					caSubconCacheData
						.getSubcontractorHdrData()
						.setSubconIssueDate(
						validateIssueDate());
				}
				catch (RTSException aeRTSEx)
				{
					RTSException leRTSEx2 = new RTSException();
					leRTSEx2.addException(aeRTSEx, gettxtIssueDate());
					leRTSEx2.setBeep(RTSException.BEEP);
					leRTSEx2.displayError(this);
					leRTSEx2.getFirstComponent().requestFocus();
					return;
				}
				if (enterDateConfirmation())
				{
					getController().processData(
						AbstractViewController.ENTER,
						caSubconCacheData);
				}
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.REG006);
				if (cbDisketteEntry)
				{
					RTSHelp.displayHelp(RTSHelp.REG006A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG006B);
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Clean up screen after an invalid 
	 * 
	 */
	private void cleanUpInvalidSubcon()
	{
		// defect 10161 
		getlblSubconInfoLine1().setText("");
		getlblSubconInfoLine2().setText("");
		getlblSubconInfoLine3().setText("");
		getlblSubconInfoLine4().setText("");
		getlblSubconInfoLine5().setText("");
		// end defect 10161 

		((TMREG006) getScrollPaneTable().getModel()).add(new Vector());
		gettxtSubcontractorId().requestFocus();
	}

	//	/**
	//	 * Compares its two arguments for order.  Returns a negative integer,
	//	 * zero, or a positive integer as the first argument is less than,
	//	 * equal to, or greater than the second.<p>
	//	 *
	//	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	//	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	//	 * implies that <tt>compare(x, y)</tt> must throw an exception if and only
	//	 * if <tt>compare(y, x)</tt> throws an exception.)<p>
	//	 *
	//	 * The implementor must also ensure that the relation is transitive:
	//	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	//	 * <tt>compare(x, z)&gt;0</tt>.<p>
	//	 *
	//	 * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
	//	 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
	//	 * <tt>z</tt>.<p>
	//	 *
	//	 * It is generally the case, but <i>not</i> strictly required that 
	//	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
	//	 * any comparator that violates this condition should clearly indicate
	//	 * this fact.  The recommended language is "Note: this comparator
	//	 * imposes orderings that are inconsistent with equals."
	//	 * 
	//	 * @param aaObj1 Object
	//	 * @param aaObj2 Object
	//	 * @return int 	    a negative integer, zero, or a positive integer 
	//	 * 	         	    as thefirst argument is less than, equal to, or 
	//	 * 					greater than the second.
	//	 * @throws ClassCastException   if the arguments' types prevent them
	//	 * 	                            from being compared
	//	 */
	//	public int compare(Object aaObj1, Object aaObj2)
	//	{
	//		InventoryAllocationData laData1 =
	//			(InventoryAllocationData) aaObj1;
	//		InventoryAllocationData laData2 =
	//			(InventoryAllocationData) aaObj2;
	//		if (laData1.getInvItmYr() != laData2.getInvItmYr())
	//		{
	//			if (laData1.getInvItmYr() == 0)
	//			{
	//				return -1;
	//			}
	//			if (laData2.getInvItmYr() == 0)
	//			{
	//				return 1;
	//			}
	//			if (laData1.getInvItmYr() > laData2.getInvItmYr())
	//			{
	//				return 1;
	//			}
	//			else
	//			{
	//				return -1;
	//			}
	//		}
	//		else
	//		{
	//			int liCompare = 0;
	//			liCompare =
	//				laData1.getItmCd().compareTo(laData2.getItmCd());
	//			if (liCompare != 0)
	//			{
	//				return liCompare;
	//			}
	//			else
	//			{
	//				return UtilityMethods
	//					.addPadding(
	//						new String[] { laData1.getInvItmNo()},
	//						new int[] { 10 },
	//						"0")
	//					.compareTo(
	//						UtilityMethods.addPadding(
	//							new String[] { laData2.getInvItmNo()},
	//							new int[] { 10 },
	//							"0"));
	//			}
	//		}
	//	}

	/**
	 * Validate date
	 * 
	 * @return boolean
	 */
	private boolean enterDateConfirmation()
	{
		RTSDate laRTSDate = gettxtIssueDate().getDate();
		RTSDate laRTSDateToday = new RTSDate();
		if (laRTSDate.add(RTSDate.DATE, 90).compareTo(laRTSDateToday)
			== -1)
		{
			// defect 8756
			// Used common constant for CTL001 title
			RTSException leRTSEx =
				new RTSException(
					RTSException.CTL001,
					CTL001_MSG,
					ScreenConstant.CTL001_FRM_TITLE);
			// end defect 8756
			int liStatus = leRTSEx.displayError(this);
			if (liStatus == RTSException.YES)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setBounds(133, 382, 294, 38);
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjbuttonPanel.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * Return the FrmSubcontractorEntryREG006ContentPane1 property value
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax
		.swing
		.JPanel getFrmSubcontractorEntryREG006ContentPane1()
	{
		if (ivjFrmSubcontractorEntryREG006ContentPane1 == null)
		{
			try
			{
				ivjFrmSubcontractorEntryREG006ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmSubcontractorEntryREG006ContentPane1.setName(
					"FrmSubcontractorEntryREG006ContentPane1");
				ivjFrmSubcontractorEntryREG006ContentPane1.setLayout(
					null);
				ivjFrmSubcontractorEntryREG006ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSubcontractorEntryREG006ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(592, 426));
				ivjFrmSubcontractorEntryREG006ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					gettxtSubcontractorId(),
					gettxtSubcontractorId().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					gettxtIssueDate(),
					gettxtIssueDate().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getstcLblIssueDate(),
					getstcLblIssueDate().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getstcLblSubcontractorId(),
					getstcLblSubcontractorId().getName());
				getFrmSubcontractorEntryREG006ContentPane1().add(
					getlblSubconId(),
					getlblSubconId().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmSubcontractorEntryREG006ContentPane1;
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
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						"Subcontractor:"));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(330, 125));
				ivjJPanel1.setBounds(204, 14, 357, 113);

				// defect 10161 
				getJPanel1().add(
					getlblSubconInfoLine1(),
					getlblSubconInfoLine1().getName());
				getJPanel1().add(
					getlblSubconInfoLine2(),
					getlblSubconInfoLine2().getName());
				getJPanel1().add(
					getlblSubconInfoLine3(),
					getlblSubconInfoLine3().getName());
				getJPanel1().add(
					getlblSubconInfoLine4(),
					getlblSubconInfoLine4().getName());
				getJPanel1().add(
					getlblSubconInfoLine5(),
					getlblSubconInfoLine5().getName());
				// end defect 10161  

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
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
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setBounds(8, 176, 562, 194);
				getJScrollPane1().setViewportView(getScrollPaneTable());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the tmplblSubconId property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblSubconId()
	{
		if (ivjlblSubconId == null)
		{
			try
			{
				ivjlblSubconId = new javax.swing.JLabel();
				ivjlblSubconId.setSize(35, 20);
				ivjlblSubconId.setName("ivjlblSubconId");
				ivjlblSubconId.setText("");
				ivjlblSubconId.setMaximumSize(
					new java.awt.Dimension(141, 14));
				ivjlblSubconId.setMinimumSize(
					new java.awt.Dimension(141, 14));
				ivjlblSubconId.setHorizontalAlignment(4);
				ivjlblSubconId.setLocation(138, 38);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjlblSubconId;
	}

	/**
	 * Return the ivjlblSubconInfoLine1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblSubconInfoLine1()
	{
		if (ivjlblSubconInfoLine1 == null)
		{
			try
			{
				ivjlblSubconInfoLine1 = new javax.swing.JLabel();
				ivjlblSubconInfoLine1.setSize(270, 16);
				ivjlblSubconInfoLine1.setName("ivjlblSubconInfoLine1");
				ivjlblSubconInfoLine1.setMaximumSize(
					new java.awt.Dimension(178, 14));
				ivjlblSubconInfoLine1.setMinimumSize(
					new java.awt.Dimension(178, 14));
				ivjlblSubconInfoLine1.setLocation(22, 21);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjlblSubconInfoLine1;
	}

	/**
	 * This method initializes ivjtxtSubconInfoLine2
	 *  
	 * @return JLabel
	 */
	private JLabel getlblSubconInfoLine2()
	{
		if (ivjlblSubconInfoLine2 == null)
		{
			ivjlblSubconInfoLine2 = new JLabel();
			ivjlblSubconInfoLine2.setName("ivjtxtSubconInfoLine2");
			ivjlblSubconInfoLine2.setSize(270, 16);
			ivjlblSubconInfoLine2.setLocation(22, 38);
		}
		return ivjlblSubconInfoLine2;
	}

	/**
	 * Return the ivjlblSubconInfoLine3 property value.
	 *  
	 * @return JLabel
	 */
	private JLabel getlblSubconInfoLine3()
	{
		if (ivjlblSubconInfoLine3 == null)
		{
			try
			{
				ivjlblSubconInfoLine3 = new javax.swing.JLabel();
				ivjlblSubconInfoLine3.setSize(270, 16);
				ivjlblSubconInfoLine3.setName("ivjlblSubconInfoLine3");
				ivjlblSubconInfoLine3.setText("");
				ivjlblSubconInfoLine3.setMaximumSize(
					new java.awt.Dimension(73, 14));
				ivjlblSubconInfoLine3.setMinimumSize(
					new java.awt.Dimension(73, 14));
				ivjlblSubconInfoLine3.setLocation(22, 55);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjlblSubconInfoLine3;
	}

	/**
	 * Return the ivjlblSubconInfoLine4 property value.
	 *  
	 * @return JLabel
	 */
	private JLabel getlblSubconInfoLine4()
	{
		if (ivjlblSubconInfoLine4 == null)
		{
			try
			{
				ivjlblSubconInfoLine4 = new javax.swing.JLabel();
				ivjlblSubconInfoLine4.setSize(270, 16);
				ivjlblSubconInfoLine4.setName("ivjlblSubconInfoLine4");
				ivjlblSubconInfoLine4.setText("");
				ivjlblSubconInfoLine4.setMaximumSize(
					new java.awt.Dimension(36, 14));
				ivjlblSubconInfoLine4.setMinimumSize(
					new java.awt.Dimension(36, 14));
				ivjlblSubconInfoLine4.setLocation(22, 72);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjlblSubconInfoLine4;
	}

	/**
	 * Return the ivjlblSubconInfoLine5 property value.
	 *  
	 * @return JLabel
	 */
	private JLabel getlblSubconInfoLine5()
	{
		if (ivjlblSubconInfoLine5 == null)
		{
			try
			{
				ivjlblSubconInfoLine5 = new javax.swing.JLabel();
				ivjlblSubconInfoLine5.setSize(270, 16);
				ivjlblSubconInfoLine5.setName("ivjlblSubconInfoLine5");
				ivjlblSubconInfoLine5.setText("");
				ivjlblSubconInfoLine5.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjlblSubconInfoLine5.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjlblSubconInfoLine5.setLocation(22, 89);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjlblSubconInfoLine5;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				getJScrollPane1().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7894
				ivjScrollPaneTable.setModel(new TMREG006());
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				// user code begin {1}
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				TableColumn laYrTableColumn =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				TableColumn laItmCodeTableColumn =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				TableColumn laQtyTableColumn =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(2));
				TableColumn laBeginNoTableColumn =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(3));
				TableColumn laEndNoTableColumn =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(4));
				laYrTableColumn.setPreferredWidth(50);
				laItmCodeTableColumn.setPreferredWidth(250);
				laQtyTableColumn.setPreferredWidth(70);
				laBeginNoTableColumn.setPreferredWidth(100);
				laEndNoTableColumn.setPreferredWidth(100);
				DefaultTableCellRenderer laDefaultTableCellRenderer =
					new DefaultTableCellRenderer();
				laDefaultTableCellRenderer.setHorizontalAlignment(
					SwingConstants.CENTER);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				laYrTableColumn.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.CENTER));
				laItmCodeTableColumn.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				laQtyTableColumn.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.RIGHT));
				laBeginNoTableColumn.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.CENTER));
				laEndNoTableColumn.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.CENTER));
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the stcLblIssueDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblIssueDate()
	{
		if (ivjstcLblIssueDate == null)
		{
			try
			{
				ivjstcLblIssueDate = new javax.swing.JLabel();
				ivjstcLblIssueDate.setSize(149, 20);
				ivjstcLblIssueDate.setName("stcLblIssueDate");
				ivjstcLblIssueDate.setText("Issue Date(MM/DD/YYYY):");
				ivjstcLblIssueDate.setMaximumSize(
					new java.awt.Dimension(141, 14));
				ivjstcLblIssueDate.setMinimumSize(
					new java.awt.Dimension(141, 14));
				ivjstcLblIssueDate.setHorizontalAlignment(4);
				ivjstcLblIssueDate.setLocation(325, 145);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblIssueDate;
	}

	/**
	 * Return the stcLblSubcontractorID property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSubcontractorId()
	{
		if (ivjstcLblSubcontractorId == null)
		{
			try
			{
				ivjstcLblSubcontractorId = new javax.swing.JLabel();
				ivjstcLblSubcontractorId.setName(
					"stcLblSubcontractorId");

				// defect 10161 
				// Add colon 
				ivjstcLblSubcontractorId.setText("Subcontractor Id:");
				// end defect 10161
				ivjstcLblSubcontractorId.setMaximumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setMinimumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setHorizontalAlignment(4);
				ivjstcLblSubcontractorId.setBounds(19, 38, 107, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblSubcontractorId;
	}

	/**
	 * Return the txtIssueDate property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSDateField gettxtIssueDate()
	{
		if (ivjtxtIssueDate == null)
		{
			try
			{
				ivjtxtIssueDate = new RTSDateField();
				ivjtxtIssueDate.setSize(70, 20);
				ivjtxtIssueDate.setName("txtIssueDate");
				ivjtxtIssueDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtIssueDate.setText("  /  /    ");
				ivjtxtIssueDate.setColumns(10);
				// user code begin {1}
				ivjtxtIssueDate.setLocation(491, 145);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtIssueDate;
	}

	/**
	 * Return the txtSubcontractorId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSubcontractorId()
	{
		if (ivjtxtSubcontractorId == null)
		{
			try
			{
				ivjtxtSubcontractorId = new RTSInputField();
				ivjtxtSubcontractorId.setSize(35, 20);
				ivjtxtSubcontractorId.setName("txtSubcontractorId");
				ivjtxtSubcontractorId.setMaxLength(3);
				// user code begin {1}
				ivjtxtSubcontractorId.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtSubcontractorId.setLocation(138, 38);
				ivjtxtSubcontractorId.setManagingFocus(true);
				ivjtxtSubcontractorId.setFocusTraversalKeysEnabled(
					false);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtSubcontractorId;
	}

	/**
	 * PCR34
	 * Validate the subcon id entered by the user.<br>
	 * Populate screen with subcontractor info, as well as inventory
	 * associated with the subcontractor if the subcontractor is found
	 * 
	 * @throws RTSException
	 */
	private void handleEnterSubconId() throws RTSException
	{
		String lsSubconId = gettxtSubcontractorId().getText().trim();
		if (lsSubconId.trim().equals(""))
		{
			throw new RTSException(150);
		}
		SubcontractorData laSubcontractorData =
			SubcontractorCache.getSubcon(
				SystemProperty.getOfficeIssuanceNo(),
				SystemProperty.getSubStationId(),
				Integer.parseInt(gettxtSubcontractorId().getText()));

		if (laSubcontractorData == null)
		{
			throw new RTSException(701);
		}
		else
		{
			ciStoredSubconId =
				Integer.parseInt(gettxtSubcontractorId().getText());

			caSubconCacheData.setSubconInfo(laSubcontractorData);

			// Retrieve the inventory allocated for this subcontractor
			getController().processData(
				VCSubcontractorEntryREG006.GET_ALLOCATED_INV,
				caSubconCacheData);
		}
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmSubcontractorEntryREG006");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(580, 455);
			setTitle("Subcontractor Entry          REG006");
			setContentPane(
				getFrmSubcontractorEntryREG006ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}

		// defect 10161 
		cvInfoLines.addElement(getlblSubconInfoLine1());
		cvInfoLines.addElement(getlblSubconInfoLine2());
		cvInfoLines.addElement(getlblSubconInfoLine3());
		cvInfoLines.addElement(getlblSubconInfoLine4());
		cvInfoLines.addElement(getlblSubconInfoLine5());

		for (int i = 0; i < 5; i++)
		{
			((JLabel) cvInfoLines.elementAt(i)).setText("");
		}
		// end defect 10161

		// user code end
	}

	/**
	 * Handle the keyPressed event
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		// defect 4733 
		// Shift Tab was not working from SubcontractorId field 
		if (aaKE.getKeyCode() == KeyEvent.VK_TAB
			&& aaKE.getSource() == gettxtSubcontractorId())
		{
			if (!aaKE.isShiftDown())
			{
				clearAllColor(this);
				try
				{
					handleEnterSubconId();
					gettxtIssueDate().requestFocus();
				}
				catch (RTSException aeRTSEx)
				{
					cbSubconIdError = true;
					RTSException leRTSEx2 = new RTSException();
					leRTSEx2.addException(
						aeRTSEx,
						gettxtSubcontractorId());
					leRTSEx2.displayError(this);
					cleanUpInvalidSubcon();
					leRTSEx2.getFirstComponent().requestFocus();
				}
				aaKE.consume();
			}
			else
			{
				getbuttonPanel().getBtnHelp().requestFocus();
			}
		}
		// end defect 4733 
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view<br>
	 * Gets SubcontractorInventoryData
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			caSubconCacheData =
				(SubcontractorRenewalCacheData) UtilityMethods.copy(
					aaDataObject);

			// defect 10161 
			// Reduce number of conditionals; Implement new cvInfoLines
			//  for presentation of Subcontractor Info 
			if (caSubconCacheData.getSubconDiskData() != null)
			{
				cbDisketteEntry = true;

				getlblSubconId().setText(
					String.valueOf(
						caSubconCacheData.getSubconInfo().getId()));

				// defect 7537
				// set issue date to current date
				gettxtIssueDate().setDate(RTSDate.getCurrentDate());
				// end defect 7537
			}
			else
			{
				gettxtSubcontractorId().setText(
					String.valueOf(
						caSubconCacheData.getSubconInfo().getId()));

				int liIssueDate =
					caSubconCacheData
						.getSubcontractorHdrData()
						.getSubconIssueDate();

				if (liIssueDate != 0)
				{
					gettxtIssueDate().setDate(
						new RTSDate(RTSDate.YYYYMMDD, liIssueDate));
				}
			}

			// Populate the on screen subcontractor information        
			SubcontractorData laSubcontractorData =
				caSubconCacheData.getSubconInfo();

			if (laSubcontractorData != null)
			{
				Vector lvData =
					laSubcontractorData.getNameAddressVector();

				for (int i = 0; i < lvData.size(); i++)
				{
					((JLabel) cvInfoLines.elementAt(i)).setText(
						(String) lvData.elementAt(i));
				}
			}

			// Populate inventory allocation table
			Vector lvInvAllocation =
				caSubconCacheData.getSubconAllocatedInventory();

			if (lvInvAllocation != null)
			{
				// defect 7586
				// Do not sort retrieved inventory. Sorted in SQL
				// end defect 7586 
				((TMREG006) getScrollPaneTable().getModel()).add(
					lvInvAllocation);
			}
			else
			{
				RTSException leRTSEx = new RTSException(492);
				leRTSEx.displayError(this);
				((TMREG006) getScrollPaneTable().getModel()).add(
					new Vector());
			}
		}
		else
		{
			// On 1st Entry 
			caSubconCacheData = new SubcontractorRenewalCacheData();
			caSubconCacheData.setSubconInfo(new SubcontractorData());
			SubcontractorHdrData laSubconHdrData =
				new SubcontractorHdrData();
			caSubconCacheData.setSubcontractorHdrData(laSubconHdrData);
		}

		getlblSubconId().setVisible(cbDisketteEntry);
		gettxtSubcontractorId().setVisible(!cbDisketteEntry);
		gettxtSubcontractorId().setEnabled(!cbDisketteEntry);
		// end defect 10161 
	}

//	/**
//	 * PCR34
//	 * Validate date
//	 *  
//	 * @return int
//	 * @throws RTSException
//	 */
//	private int validateDate() throws RTSException
//	{
//		if (!gettxtIssueDate().isValidDate())
//		{
//			RTSException leRTSEx =
//				new RTSException(
//					RTSException.WARNING_MESSAGE,
//					ERROR_MSG,
//					ERROR_TITLE);
//			throw leRTSEx;
//		}
//		RTSDate laRTSDate = gettxtIssueDate().getDate();
//		if (laRTSDate == null)
//		{
//			throw new RTSException(150);
//		}
//		RTSDate laRTSDateToday = new RTSDate();
//		if (laRTSDateToday.compareTo(laRTSDate) == -1)
//		{
//			throw new RTSException(427);
//		}
//		return Integer.parseInt(
//			UtilityMethods.addPadding(
//				new String[] {
//					String.valueOf(laRTSDate.getYear()),
//					String.valueOf(laRTSDate.getMonth()),
//					String.valueOf(laRTSDate.getDate())},
//				new int[] { 4, 2, 2 },
//				"0"));
//		//cSubcontractorInventoryData.setIssueDate(Integer.parseInt(lsDateStr));
//	}

	/**
	 * PCR34
	 * Validate Issue date
	 *  
	 * @return int
	 * @throws RTSException
	 */
	private int validateIssueDate() throws RTSException
	{
		int liErrMsgNo = 0;
		RTSDate laIssueDate = new RTSDate();
		laIssueDate = gettxtIssueDate().getDate();

		if (!gettxtIssueDate().isValidDate())
		{
			liErrMsgNo = ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
		}
		else
		{
			// Today < Issue Date 
			if (caToday.compareTo(laIssueDate) == -1)
			{
				liErrMsgNo =
					ErrorsConstant.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE;
			}
			// IssueDate Year < Current Year -1 
			else if (laIssueDate.getYear() < caToday.getYear() - 1)
			{
				liErrMsgNo = ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			}
		}
		if (liErrMsgNo != 0)
		{
			RTSException leRTSEx = new RTSException();
			leRTSEx.addException(
				new RTSException(liErrMsgNo),
				gettxtIssueDate());
			throw leRTSEx;
		}
		return laIssueDate.getYYYYMMDDDate();
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
