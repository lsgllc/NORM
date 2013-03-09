package com.txdot.isd.rts.client.general.ui;

import com.txdot.isd.rts.services.util.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/*
 *
 * RTSTable.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	03/19/2002 	Added isManagingFocus, setManagingFocus;
 * MAbs			04/23/2002	No resizing of Table columns 
 * 							defect 3618
 * MAbs			04/24/2002	Changed mouse table selection model 
 * 							defect 3619
 * MAbs			05/02/2002	Fixed table selection functionality to 
 * 							repaint quicker 
 * 							defect 3750
 * MAbs			05/15/2002	add code for selecting row when using mouse
 * 							clicks
 * 							defect 3944
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	03/28/2005	Fix tabbing on keyPressed.
 * 							Also do RTS 5.2.3 Code Cleanup
 * 							delete isManagingFocus()
 * 							modify keyPressed()
 * 							defect 7885 Ver 5.2.3
 * S Johnston	04/05/2005	Fixed the TAB key in JTable
 * 							modify init()
 * 							defect 6859 Ver 5.2.3
 * S Johnston	06/15/2005	Added a check for is selected row greater
 * 							than the number of rows
 * 							modify focusGained()
 * 							defect 8131 Ver 5.2.3
 * Jeff S.		02/09/2006	Added public method to allow the 
 * 							ciMultipleRowFocus field to be reset.  This
 * 							is b/c in reprint reports we want the cursor
 * 							location to always start at the top when
 * 							changing categories.
 * 							add clearLastRowFocus()
 * 							defect 8541 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * The RTSTable has much the same functionality as a JTable but handles
 * painting and keyboard events that reflect the current RTS system.
 *
 * @version	5.2.3			02/09/2006
 * @author	Michael Abernethy
 * <br>Creation Date:		08/06/2001 11:10:35
 */
public class RTSTable
	extends JTable
	implements KeyListener, MouseListener, FocusListener
{
	private Vector cvListeners;
	private Vector cvSelectedRows;
	public final static int RIGHT = JLabel.RIGHT;
	public final static int LEFT = JLabel.LEFT;
	public final static int CENTER = JLabel.CENTER;
	private boolean cbTraversable = true;
	private boolean cbCharSelect = true;
	//This variable is used to select the row for pressed char.
	private int ciFirstClick = -1;
	private Vector cvListListeners;
	private boolean cbManagingFocus = false;
	private int ciMultipleRowFocus;
	/**
	 * Creates an RTSTable.
	 */
	public RTSTable()
	{
		super();
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param aaRowData Object[][]
	 * @param aaColumnNames Object[]
	 */
	public RTSTable(
		Object[][] aaRowData,
		Object[] aaColumnNames)
	{
		super(aaRowData, aaColumnNames);
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param aiNumRows int
	 * @param aiNumColumns int
	 */
	public RTSTable(int aiNumRows, int aiNumColumns)
	{
		super(aiNumRows, aiNumColumns);
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param avRowData Vector
	 * @param avColumnNames Vector
	 */
	public RTSTable(
		Vector avRowData,
		Vector avColumnNames)
	{
		super(avRowData, avColumnNames);
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param aaTM TableModel
	 */
	public RTSTable(TableModel aaTM)
	{
		super(aaTM);
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param aaTM TableModel
	 * @param aaTCM TableColumnModel
	 */
	public RTSTable(
		TableModel aaTM,
		TableColumnModel aaTCM)
	{
		super(aaTM, aaTCM);
		if (cvListeners == null)
		{
			cvListeners = new java.util.Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Creates an RTSTable.
	 * 
	 * @param aaTM TableModel
	 * @param aaTCM TableColumnModel
	 * @param aaLSM ListSelectionModel
	 */
	public RTSTable(
		TableModel aaTM,
		TableColumnModel aaTCM,
		ListSelectionModel aaLSM)
	{
		super(aaTM, aaTCM, aaLSM);
		if (cvListeners == null)
		{
			cvListeners = new Vector();
		}
		if (cvListListeners == null)
		{
			cvListListeners = new Vector();
		}
	}
	/**
	 * Adds an ActionListener to the RTSTable - an ActionEvent is thrown
	 *  when a row is highlighted and Enter is pressed.
	 * 
	 * @param aaAL ActionListener
	 */
	public void addActionListener(ActionListener aaAL)
	{
		if (!cvListeners.contains(aaAL))
		{
			cvListeners.add(aaAL);
		}
	}
	/**
	 * Allows listeners to listen to selection events in 
	 * MULTIPLE_INTERVAL_SELECTION tables
	 * 
	 * @param aaLSL ListSelectionListener
	 */
	public void addMultipleSelectionListener(ListSelectionListener aaLSL)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			if (!cvListListeners.contains(aaLSL))
			{
				cvListListeners.add(aaLSL);
			}
		}
		else
		{
			getSelectionModel().addListSelectionListener(aaLSL);
		}
	}
	/**
	 * This clears the last row that was selected so that the default
	 * selection when a table is reloaded is the first item.
	 */
	public void clearLastRowFocus()
	{
		ciMultipleRowFocus = 0;
	}
	/**
	 * Returns the default table header object which is
	 * a JTableHeader.  Subclass can override this
	 * method to return a different table header object
	 *
	 * @return the default table header object
	 */
	protected JTableHeader createDefaultTableHeader()
	{
		return new RTSTableHeader(columnModel);
	}
	/**
	 * Calls the ActionListener methods of listening classes.
	 */
	private void fireActionEvent()
	{
		ActionEvent laAE =
			new ActionEvent(
				this,
				ActionEvent.ACTION_PERFORMED,
				"Table Event");
		int liSize = cvListeners.size();
		for (int i = 0; i < liSize; i++)
		{
			((ActionListener) cvListeners.get(i)).actionPerformed(laAE);
		}
	}
	/**
	 * Fires a ListSelectionEvent
	 * 
	 * @param aaLSE ListSelectionEvent
	 */
	private void fireListSelectionEvent(ListSelectionEvent aaLSE)
	{
		for (int i = 0; i < cvListListeners.size(); i++)
		{
			(
				(ListSelectionListener) cvListListeners.get(
					i)).valueChanged(
				aaLSE);
		}
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
			&& this.getRowCount() != 0)
		{
			// defect 8131
			// check if the selected row is greater than number of rows
			if ( ciMultipleRowFocus > this.getRowCount())
			{
				setRowSelectionInterval(
					(this.getRowCount() - 1),
					(this.getRowCount() - 1) );
			}
			else 
			{
				setRowSelectionInterval(
					ciMultipleRowFocus,
					ciMultipleRowFocus);
			}
			// end defect 8131
		}
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			ciMultipleRowFocus = getSelectedRow();
			clearSelection();
		}
	}
	/**
	 * Returns a Collection of the selected rows when the table has 
	 * mutliple selection enabled.
	 * 
	 * @return Set
	 */
	public Set getSelectedRowNumbers()
	{
		return new HashSet(cvSelectedRows);
	}
	/**
	 * Returns an array of the selected rows when multiple selection 
	 * is enabled.
	 * 
	 * @return int[]
	 */
	public int[] getSelectedRows()
	{
		int[] larrNums = new int[getSelectedRowNumbers().size()];
		Vector lvSelectedRows =
			new java.util.Vector(getSelectedRowNumbers());
		for (int i = 0; i < larrNums.length; i++)
		{
			larrNums[i] = ((Integer) lvSelectedRows.get(i)).intValue();
		}
		return larrNums;
	}
	/**
	 * Handles setting all the final steps of the RTSTable.  Every time
	 * a RTSTable is created, a call to this method should also occur,
	 * after the columns and table model are created.
	 */
	public void init()
	{
		addKeyListener(this);
		addMouseListener(this);
		addFocusListener(this);
		setShowVerticalLines(false);
		setShowHorizontalLines(false);
		setIntercellSpacing(new java.awt.Dimension(0, 0));
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		configureEnclosingScrollPane();
		getSelectionModel().addListSelectionListener(this);
		// defect 6859
		// fixed tabbing issue with JTable
		HashSet lhsKeys = new HashSet();
		lhsKeys.add(KeyStroke.getKeyStroke("TAB"));
		this.setFocusTraversalKeys(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
			lhsKeys);
		lhsKeys.clear();
		lhsKeys.add(KeyStroke.getKeyStroke("shift TAB"));        
		this.setFocusTraversalKeys(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, lhsKeys);
		// end defect 6859
		cvSelectedRows = new Vector();
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.SINGLE_SELECTION)
		{
			SingleRowRenderer laSRR = new SingleRowRenderer();
			for (int i = 0; i < getColumnCount(); i++)
			{
				TableColumn laTC = getColumn(getColumnName(i));
				laTC.setCellRenderer(laSRR);
			}
		}
		else if (
			getSelectionModel().getSelectionMode()
				== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			MultipleRowRenderer laMRR = new MultipleRowRenderer();
			for (int i = 0; i < getColumnCount(); i++)
			{
				TableColumn laTC = getColumn(getColumnName(i));
				laTC.setCellRenderer(laMRR);
			}
		}
	}
	/**
	 * Returns the value of a flag that indicates whether
	 * this component can be traversed using
	 * Tab or Shift-Tab keyboard focus traversal.  If this method
	 * returns "false", this component may still request the keyboard
	 * focus using <code>requestFocus()</code>, but it will not
	 * automatically be assigned focus during tab traversal.
	 * @return    <code>true</code> if this component is
	 *            focus-traverable; <code>false</code> otherwise.
	 * @since     JDK1.1
	 */
	public boolean isFocusTraversable()
	{
		// TODO Can we remove this method?  Research.
		return cbTraversable;
	}
	///**
	// * Override this method and return true if your JComponent manages
	// * focus. If your component manages focus, the focus manager will 
	// * handle your component's children. All key event will be sent to
	// * your key listener including TAB and SHIFT+TAB. CONTROL + TAB 
	// * and CONTROL + SHIFT + TAB will move the focus to the next / 
	// * previous component.
	// */
	// TODO this method is not required!
	//public boolean isManagingFocus()
	//{
	//	return true;
	//}
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{

		if (aaKE.getKeyCode() == KeyEvent.VK_ENTER)
		{
			fireActionEvent();
			aaKE.consume();
		}
		if (aaKE.getKeyCode() == KeyEvent.VK_TAB)
		{
			// TODO First pass of this process. Seems we could do better
			JDialog laComp1 = (JDialog) getFocusCycleRootAncestor();
			Container laComp2 = laComp1.getContentPane();
			Component[] laList = laComp2.getComponents();
			Component laMe = (Component) this;
			Component laParent = laMe.getParent();

			while (!(laParent instanceof JScrollPane))
			{
				laParent = laParent.getParent();
			}

			if (aaKE.getModifiers() != KeyEvent.SHIFT_MASK)
			{
				// doing tab.  go forwards.
				for (int i = 0; i < laList.length; i++)
				{
					Component laCheck = laList[i];
					if (laCheck == laParent)
					{
						i = i + 1;
						if (i >= laList.length)
						{
							i = 0;
						}
						Component laNext = laList[i];
						laNext.requestFocus();
						break;
					}
				}
			}
			else
			{
				// Doing shift tab.  go backwards.
				for (int i = laList.length -1; i >= 0; i--)
				{
					Component laCheck = laList[i];
					if (laCheck == laParent)
					{
						i = i - 1;
						if (i < 0)
						{
							i = laList.length - 1;
						}
						Component laNext = laList[i];
						laNext.requestFocus();
						break;
					}
				}
			}

			aaKE.consume();
		}
		if (aaKE.getKeyCode() == KeyEvent.VK_SPACE
			&& getSelectionModel().getSelectionMode()
				== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			if (isRowSelected(getSelectedRow())
				&& cvSelectedRows.contains(
					new Integer(getSelectedRow())))
			{
				cvSelectedRows.remove(new Integer(getSelectedRow()));
			}
			else if (
				isRowSelected(getSelectedRow())
					&& !cvSelectedRows.contains(
						new Integer(getSelectedRow())))
			{
				cvSelectedRows.add(new Integer(getSelectedRow()));
			}
			repaint();
			ciFirstClick = -1;
			fireListSelectionEvent(
				new ListSelectionEvent(
					this,
					getSelectedRow(),
					getSelectedRow(),
					false));
		}

		// If any character is pressed in table, select the row with 
		// first column having that char as first char.
		int liModifier = aaKE.getModifiers();
		if (liModifier == KeyEvent.SHIFT_MASK 
			|| liModifier == KeyEvent.CTRL_MASK)
		{
			aaKE.consume();
			return;
		}
		if (liModifier != KeyEvent.ALT_MASK)
		{
			int liSelectedRow = getSelectedRow();
			if (liSelectedRow == -1)
			{
				liSelectedRow = 0;
			}

			if (aaKE.getKeyCode() >= '0'
				&& aaKE.getKeyCode() <= 'z'
				&& cbCharSelect)
			{
				setRowForChar(aaKE.getKeyChar(), liSelectedRow);
			}
		}
	}
	/**
	 * Invoked when a key has been released.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// empty code block
	}
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// empty code block
	}
	/**
	 * Invoked when the mouse has been clicked on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseClicked(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Invoked when a mouse button is pressed on a component and then 
	 * dragged.  Mouse drag events will continue to be delivered to
	 * the component where the first originated until the mouse button is
	 * released (regardless of whether the mouse position is within the
	 * bounds of the component).
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseDragged(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Invoked when the mouse enters a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseEntered(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Invoked when the mouse exits a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseExited(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Invoked when the mouse button has been moved on a component
	 * (with no buttons no down).
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseMoved(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Invoked when a mouse button has been pressed on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mousePressed(MouseEvent aaME)
	{
		if (getSelectionModel().getSelectionMode()
			!= ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			return;
		}
		int liClickedRow = rowAtPoint(aaME.getPoint());

		// Use this code to match AM table functionality
		if (cvSelectedRows.contains(new Integer(liClickedRow)))
		{
			cvSelectedRows.remove(new Integer(liClickedRow));
		}
		else if (!cvSelectedRows.contains(new Integer(liClickedRow)))
		{
			cvSelectedRows.add(new Integer(liClickedRow));
		}

		fireListSelectionEvent(
			new ListSelectionEvent(
				this,
				getSelectedRow(),
				getSelectedRow(),
				false));

		cvSelectedRows = new Vector(getSelectedRowNumbers());

		repaint();
		aaME.consume();
	}
	/**
	 * Invoked when a mouse button has been released on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseReleased(MouseEvent aaME)
	{
		// empty code block
	}
	/**
	 * Removes an ActionListener to the RTSTable - an ActionEvent is 
	 * thrown when a row is highlighted and Enter is pressed.
	 * 
	 * @param aaAL ActionListener
	 */
	public void removeActionListener(ActionListener aaAL)
	{
		cvListeners.remove(aaAL);
	}
	/**
	 * Removes the Multiple Selection Listener
	 * 
	 * @param aaLSL ListSelectionListener
	 */
	public void removeMultipleSelectionListener(ListSelectionListener aaLSL)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
		{
			cvListListeners.remove(aaLSL);
		}
		else
		{
			getSelectionModel().removeListSelectionListener(aaLSL);
		}
	}
	/**
	 * Removes the given row from selection
	 * 
	 * @param aiRow int
	 */
	public void removeSelectedRow(int aiRow)
	{
		cvSelectedRows.remove(new Integer(aiRow));
		repaint();
	}
	/**
	 * Removes the given rows from selection
	 * 
	 * @param avRows Vector
	 */
	public void removeSelectedRows(Vector avRows)
	{
		cvSelectedRows.remove(avRows);
		repaint();
	}
	/**
	 * Selects all the rows
	 * 
	 * @param aiRowCount int
	 */
	public void selectAllRows(int aiRowCount)
	{
		cvSelectedRows.clear();
		for (int i = 0; i < aiRowCount; i++)
		{
			cvSelectedRows.add(new Integer(i));
		}
		repaint();
	}
	/**
	 * To enable auto select of rows. 
	 * 
	 * @param abFlag boolean
	 */
	public void setAutoRowSelect(boolean abFlag)
	{
		cbCharSelect = abFlag;
	}
	/**
	 * Sets the column alignment of a table column
	 * 
	 * @param aiAlign int
	 * @return TableCellRenderer
	 */
	public TableCellRenderer setColumnAlignment(int aiAlign)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.SINGLE_SELECTION)
		{
			SingleRowRenderer laSRR = new SingleRowRenderer();
			laSRR.setHorizontalAlignment(aiAlign);
			return laSRR;
		}
		else
		{
			MultipleRowRenderer laMRR = new MultipleRowRenderer();
			laMRR.setHorizontalAlignment(aiAlign);
			return laMRR;
		}
	}
	/**
	 * Set the column alignment.
	 * 
	 * @param aiColumn int
	 * @param aiAlign int
	 */
	public void setColumnAlignment(int aiColumn, int aiAlign)
	{
		if (getSelectionModel().getSelectionMode()
			== ListSelectionModel.SINGLE_SELECTION)
		{
			TableColumn laTC = getColumn(getColumnName(aiColumn));
			SingleRowRenderer laSRR = new SingleRowRenderer();
			laSRR.setHorizontalAlignment(aiAlign);
			laTC.setCellRenderer(laSRR);
		}
		else
		{
			TableColumn laTC = getColumn(getColumnName(aiColumn));
			MultipleRowRenderer laMRR = new MultipleRowRenderer();
			laMRR.setHorizontalAlignment(aiAlign);
			laTC.setCellRenderer(laMRR);
		}
	}
	/**
	 * Set the Column size.
	 * Convert the double to an int.
	 * 
	 * @param aiColumn int
	 * @param adSize double
	 */
	public void setColumnSize(int aiColumn, double adSize)
	{
		setColumnSize(aiColumn, (int) (adSize * getWidth()));
	}
	/**
	 * Set the column size.
	 * 
	 * @param aiColumn int
	 * @param aiSize int
	 */
	public void setColumnSize(int aiColumn, int aiSize)
	{
		TableColumn laTC = getColumn(getColumnName(aiColumn));
		laTC.setPreferredWidth(aiSize);
	}
	/**
	 * Change Managing Focus
	 * 
	 * @param abManagingFocus boolean
	 */
	public void setManagingFocus(boolean abManagingFocus)
	{
		cbManagingFocus = abManagingFocus;
	}
	/**
	 * This method selects the row in a table according to the pressed
	 * char. Checks only in first column.
	 *  
	 * @param achChar char
	 * @param aiSelRow int
	 */
	private void setRowForChar(char achChar, int aiSelRow)
	{
		String lsStr = null;
		achChar = Character.toLowerCase(achChar);

		int liRequiredRowNum = -1;
		TableModel laTM = this.getModel();
		int liRowCount = this.getRowCount();
		//Check only is first row is type of RTSDate, Integer or String.

		if (aiSelRow + 1 < liRowCount)
		{
			for (int i = aiSelRow + 1; i < liRowCount; i++)
			{
				Object laObj = laTM.getValueAt(i, 0);
				if (laObj != null)
				{
					laObj.getClass();
					if (laObj instanceof String)
					{
						lsStr = ((String) laObj);
					}
					else if (laObj instanceof Integer)
					{
						lsStr =
							String.valueOf(((Integer) laObj).intValue());
					}
					else if (laObj instanceof RTSDate)
					{
						lsStr = 
							String.valueOf(((RTSDate) laObj).toString());
					}
					else
					{
						break;
					}

					if (lsStr.length() > 0)
					{
						char lchConvertedChar =
							Character.toLowerCase(lsStr.trim().charAt(0));
						if (lchConvertedChar == achChar)
						{
							liRequiredRowNum = i;
							break;
						}
					}
				}
			}
		}
		//If current selected row is anyrow after first row and char 
		//is not found, start from 0th row. so call this method again.
		if (liRequiredRowNum == -1 && aiSelRow != -1)
		{
			setRowForChar(achChar, -1);
		}
		//If any row is found for the specified cahr,
		//Select the row and display the line.
		if (liRequiredRowNum != -1)
		{
			//selectedRows.clear();
			setRowSelectionInterval(liRequiredRowNum, liRequiredRowNum);
			//This is done to make first or last line of selection visible.
			if (aiSelRow == -1)
			{
				scrollRectToVisible(
					new Rectangle(
						0,
						getRowHeight() * liRequiredRowNum,
						1,
						1));
			}
			else
			{
				scrollRectToVisible(
					new Rectangle(
						0,
						getRowHeight() * (liRequiredRowNum + 1),
						1,
						1));
			}
		}
	}
	/**
	 * Selects the given row
	 * 
	 * @param aiRow int
	 */
	public void setSelectedRow(int aiRow)
	{
		cvSelectedRows.add(new Integer(aiRow));
		cvSelectedRows = new Vector(getSelectedRowNumbers());
		repaint();
	}
	/**
	 * Selects the given rows
	 * 
	 * @param avRows Vector
	 */
	public void setSelectedRows(Vector avRows)
	{
		cvSelectedRows.add(avRows);
		cvSelectedRows = new Vector(getSelectedRowNumbers());
		repaint();
	}
	/**
	 * Sets the tableHeader working with this JTable to <I>newHeader</I>.
	 * It is legal to have a <B>null</B> tableHeader.
	 *
	 * @see     #getTableHeader
	 * <br>description: The JTableHeader graph which renders the
	 * column headers.
	 * 
	 * @param aaNewHeader RTSTableHeader
	 */
	public void setTableHeader(RTSTableHeader aaNewHeader)
	{
		if (tableHeader != aaNewHeader)
		{
			// Release the old header
			if (tableHeader != null)
			{
				tableHeader.setTable(null);
			}

			tableHeader = aaNewHeader;
			if (tableHeader != null)
			{
				tableHeader.setTable(this);
			}
		}
	}
	/**
	 * Set the value to determine if this is focus traversable.
	 * 
	 * @param abTraversable boolean
	 */
	public void setTraversable(boolean abTraversable)
	{
		cbTraversable = abTraversable;
	}
	/**
	 * Unselects all the rows
	 */
	public void unselectAllRows()
	{
		cvSelectedRows.clear();
		repaint();
	}
}
