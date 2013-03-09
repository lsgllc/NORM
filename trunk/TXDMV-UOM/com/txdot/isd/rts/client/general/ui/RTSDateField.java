package com.txdot.isd.rts.client.general.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.txdot.isd.rts.services.util.RTSDate;

/* 
 * RTSDateField.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/30/2002	Checked dates for given month (ie 2/29) 
 * 							CQU100004156
 * B Hargrove	08/19/2005	Java 1.4 code cleanup. Format, 
 * 							Hungarian notation for variables, etc. 
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	06/17/2010 	Added method to check if date in field 
 * 							is in the future.  
 * 							add isFutureDate()  
 *							defect 10504  Ver POS_650
 * K Harrell	07/10/2010	add T/+/- actions on keyPressed
 * 							modify keyPressed()
 * 							defect 10545 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * A TextField with the values of "  /  /    " already in place.
 * <br>It handles everything associated with the entry and setting of 
 * dates and can be used exactly as a JTextField would be.
 * 
 * @version	POS_650		07/10/2010
 * @author	Ashish Mahajan
 * <br>Creation Date:	08/09/2001 13:55:33
 */
public class RTSDateField
	extends javax.swing.JTextField
	implements KeyListener, FocusListener
{

	String csStrText = null;
	char[] chArr = null;

	public static final int YEAR_1900 = 1900;
	public static final int YEAR_2099 = 2099;
	public static final int PIVOT_VAL = 50;

	private int ciCharNum;

	private boolean cbMonthYrOnly = false;
	private boolean cbIsManagingFocus = false;

	/**
	 * Creates a JDateTextField
	 */
	public RTSDateField()
	{
		super();
		initialize();
		addKeyListener(this);
		addFocusListener(this);
	}

	/**
	 * Check if slash
	 */
	private boolean checkIfSlash()
	{
		int liNIndex = getCaretPosition();
		if (isMonthYrOnly())
		{
			if (liNIndex == 2)
			{
				return true;
			}
		}
		else
		{
			if (liNIndex == 2 || liNIndex == 5)
			{
				return true;
			}
		}

		return false;
	}
	/**
	 * Check if slash for back space
	 *  
	 * @return boolean
	 * @param aiNIndex int 
	 */
	private boolean checkIfSlashForBackSpc(int aiNIndex)
	{
		if (isMonthYrOnly())
		{
			if (aiNIndex == 3)
			{
				return true;
			}
		}
		else
		{
			if (aiNIndex == 3 || aiNIndex == 6)
			{
				return true;
			}
		}

		return false;
	}
	/**
	* Set the caret position to the first character when focus is gained
	* 
	* @param aaFE FocusEvent 
	*/
	public void focusGained(FocusEvent aaFE)
	{
		setCaretPosition(0);
	}
	/**
	* Do validations when focus is lost. if the year is in YY format,
	* change format to YYYY using pivot value
	* 
	* @param aaFE FocusEvent
	*/
	public void focusLost(FocusEvent aaFE)
	{
		if ((!cbMonthYrOnly
			&& chArr[0] != ' '
			&& chArr[1] != ' '
			&& chArr[2] != ' '
			&& chArr[3] != ' '
			&& chArr[4] != ' '
			&& chArr[5] != ' '
			&& chArr[6] != ' '
			&& chArr[7] != ' '
			&& chArr[8] == ' '
			&& chArr[9] == ' ')
			|| (cbMonthYrOnly
				&& chArr[0] != ' '
				&& chArr[1] != ' '
				&& chArr[2] != ' '
				&& chArr[3] != ' '
				&& chArr[4] != ' '
				&& chArr[5] == ' '
				&& chArr[6] == ' '))
		{
			chArr[ciCharNum - 1] = chArr[ciCharNum - 3];
			chArr[ciCharNum - 2] = chArr[ciCharNum - 4];
			String last2Digits =
				"" + chArr[ciCharNum - 2] + chArr[ciCharNum - 1];
			int liYearSuffix = Integer.parseInt(last2Digits);
			if (liYearSuffix < PIVOT_VAL)
			{
				chArr[ciCharNum - 4] = '2';
				chArr[ciCharNum - 3] = '0';
			}
			else
			{
				chArr[ciCharNum - 4] = '1';
				chArr[ciCharNum - 3] = '9';
			}
		}
		//isYearValid();
		setText(new String(chArr));

	}
	/**
	 * Returns the RTSDate in the JDateTextField
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDate()
	{
		String lsMonth = "" + chArr[0] + chArr[1];
		String lsDay = null;
		String lsYyear = null;

		if (isMonthYrOnly())
		{
			lsDay = "15";
			lsYyear = "" + chArr[3] + chArr[4] + chArr[5] + chArr[6];
		}
		else
		{
			lsDay = "" + chArr[3] + chArr[4];
			lsYyear = "" + chArr[6] + chArr[7] + chArr[8] + chArr[9];
		}

		if (isValidDate())
		{
			return new RTSDate(
				Integer.parseInt(lsYyear.trim()),
				Integer.parseInt(lsMonth.trim()),
				Integer.parseInt(lsDay.trim()));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get Day
	 * 
	 * @return String
	 */
	private String getDay()
	{
		if (isMonthYrOnly())
		{
			return "15";
		}
		else
		{
			return new String("" + chArr[3] + chArr[4]);
		}
	}

	/**
	 * Get Month
	 * 
	 * @return String
	 */
	private String getMonth()
	{
		return new String("" + chArr[0] + chArr[1]);

	}

	/**
	 * Get Year
	 * 
	 * @return String
	 */
	private String getYear()
	{
		if (isMonthYrOnly())
		{
			return new String(
				"" + chArr[3] + chArr[4] + chArr[5] + chArr[6]);
		}
		else
		{
			return new String(
				"" + chArr[6] + chArr[7] + chArr[8] + chArr[9]);
		}
		//return strYr;

	}

	/**
	* initialize the fields and do initial settings for char array
	*/
	private void initialize()
	{
		csStrText = new String();
		if (cbMonthYrOnly)
		{
			ciCharNum = 7;
		}
		else
		{
			ciCharNum = 10;
		}

		setColumns(ciCharNum);
		chArr = new char[ciCharNum];
		for (int i = 0; i < ciCharNum; i++)
			chArr[i] = ' ';

		setSlashes();
		/*
		chArr[2] = '/';
		
		if (!cbMonthYrOnly) {
		    chArr[5] = '/';
		}
		*/

		setText(new String(chArr));
		setCaretPosition(0);

	}

	/**
	 * Is Date Empty
	 * 
	 * @return boolean
	 */
	public boolean isDateEmpty()
	{
		boolean lbRet = false;
		String lsStrDt = new String(chArr);
		lsStrDt = lsStrDt.replace('/', ' ');
		lsStrDt = lsStrDt.trim();
		if (lsStrDt.length() < 1)
		{
			lbRet = true;
		}
		return lbRet;
	}
	/** 
	 * 
	 * Is Day Valid
	 * 
	 * @return boolean 
	 */
	private boolean isDayValid()
	{
		boolean lbRVal = false;
		String lsStrDay = getDay();
		lsStrDay = lsStrDay.trim();
		if (lsStrDay.length() > 0)
		{
			try
			{
				int liDay = Integer.parseInt(lsStrDay);
				int liMonth = Integer.parseInt(getMonth().trim());
				if (liMonth == 1
					|| liMonth == 3
					|| liMonth == 5
					|| liMonth == 7
					|| liMonth == 8
					|| liMonth == 10
					|| liMonth == 12)
				{
					if (liDay > 0 && liDay <= 31)
					{
						lbRVal = true;
					}
				}
				else if (liMonth == 2)
				{
					if (liDay > 0 && liDay <= 29)
					{
						lbRVal = true;
					}
				}
				else if (
					liMonth == 4
						|| liMonth == 6
						|| liMonth == 9
						|| liMonth == 11)
				{
					if (liDay > 0 && liDay <= 30)
					{
						lbRVal = true;
					}
				}
			}
			catch (NumberFormatException leNFEx)
			{
				lbRVal = false;
			}
		}
		return lbRVal;

	}
	/**
	 * Is Day Valid
	 *  
	 * @return boolean
	 * @param aiDay int
	 */
	public static boolean isDayValid(int aiDay)
	{
		boolean lbRet = false;
		if (aiDay > 0 && aiDay <= 31)
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	* Check if the date showing in the datefield is a future date 
	* (i.e. the date is greater than todays date).
	* If return value is true, the date in this field is in
	* the future.
	* 
	* @return boolean
	*/
	public boolean isFutureDate()
	{
		boolean lbRVal = false;
		RTSDate laCurrDt = RTSDate.getCurrentDate();
		if (getDate().compareTo(laCurrDt) > 0)
		{
			lbRVal = true;
		}
		return lbRVal;

	}

	/**
	 * Override this method and return true if your JComponent manages 
	 * focus. If your component manages focus, the focus manager will 
	 * handle your component's children. All key event will be sent to 
	 * your key listener including TAB and SHIFT+TAB. CONTROL + TAB and 
	 * CONTROL + SHIFT + TAB  will move the focus to the next / previous
	 * component.
	 * 
	 * @return boolean
	 */
	public boolean isManagingFocus()
	{
		return cbIsManagingFocus;
	}

	/**
	 * Is month valid
	 * 
	 * @return boolean
	 */
	private boolean isMonthValid()
	{
		boolean rbVal = false;
		String lsStrMon = getMonth();
		lsStrMon = lsStrMon.trim();
		if (lsStrMon.length() > 0)
		{
			try
			{
				int liNMonth = Integer.parseInt(lsStrMon);
				if (liNMonth >= 1 && liNMonth <= 12)
				{
					rbVal = true;
				}
			}
			catch (NumberFormatException leNFEx)
			{
				rbVal = false;
			}
		}
		return rbVal;

	}
	/**
	 * Is month valid
	 * 
	 * @param aiMth int
	 * @return boolean
	 */
	public static boolean isMonthValid(int aiMth)
	{
		boolean lbRet = false;
		if (aiMth >= 1 && aiMth <= 12)
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	 * Is Month Year Only
	 * 
	 * @return boolean
	 */
	public boolean isMonthYrOnly()
	{
		return cbMonthYrOnly;
	}

	/**
	* Check if the current date showing in the datefield is a valid date
	* If return value is false, the window containing this field should
	* display error message and set focus back to this control.
	* 
	* @return boolean
	*/
	public boolean isValidDate()
	{
		boolean lbRVal = true;
		//Use short circuit. If one is false dont need to evaluate others
		if (!isMonthValid() || !isDayValid() || !isYearValid())
		{
			lbRVal = false;
		}
		return lbRVal;

	}

	/**
	 * Is Valid Date
	 *  
	 * @return boolean
	 * @param asStrDt String
	 */
	public static boolean isValidDate(String asStrDt)
	{
		boolean lbRet = false;
		if (asStrDt != null)
		{
			if (asStrDt.length() == 8)
			{
				try
				{
					String lsStrYr = asStrDt.substring(0, 4);
					String lsStrM = asStrDt.substring(4, 6);
					String lsStrD = asStrDt.substring(6, 8);

					int liYr = Integer.parseInt(lsStrYr);
					int liM = Integer.parseInt(lsStrM);
					int liD = Integer.parseInt(lsStrD);
					if (isDayValid(liD)
						&& isMonthValid(liM)
						&& isYearValid(liYr))
					{
						lbRet = true;
					}
				}
				catch (NumberFormatException leNFEx)
				{
					lbRet = false;
				}
			}
		}
		return lbRet;
	}

	/**
	 * Is year valid
	 * 
	 * @return boolean
	 */
	private boolean isYearValid()
	{
		boolean lbRVal = false;

		String lsStrYr = getYear();
		lsStrYr = lsStrYr.trim();

		try
		{
			int liNYr = Integer.parseInt(lsStrYr);

			if (lsStrYr.length() == 4)
			{
				if (liNYr >= YEAR_1900 && liNYr <= YEAR_2099)
				{
					lbRVal = true;
				}
			}
			else if (lsStrYr.length() == 2)
			{
				if (isMonthYrOnly())
				{
					chArr[5] = lsStrYr.charAt(0);
					chArr[6] = lsStrYr.charAt(1);
					if (liNYr > PIVOT_VAL)
					{
						chArr[3] = '1';
						chArr[4] = '9';

					}
					else
					{
						chArr[3] = '2';
						chArr[4] = '0';
					}
				}
				else
				{
					chArr[8] = lsStrYr.charAt(0);
					chArr[9] = lsStrYr.charAt(1);
					if (liNYr > PIVOT_VAL)
					{
						chArr[6] = '1';
						chArr[7] = '9';

					}
					else
					{
						chArr[6] = '2';
						chArr[7] = '0';
					}
				}
				lbRVal = true;
			}
		}
		catch (NumberFormatException leNFEx)
		{
			lbRVal = false;
		}
		return lbRVal;
	}

	/**
	 * Is Year Valid
	 *  
	 * @return boolean
	 * @param aiYr int
	 */
	public static boolean isYearValid(int aiYr)
	{
		boolean lbRet = false;
		if (aiYr >= YEAR_1900 && aiYr <= YEAR_2099)
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{

		char lchKeyChar = aaKE.getKeyChar();

		int liNIndex = getCaretPosition();

		try
		{
			if (lchKeyChar == KeyEvent.VK_DELETE)
			{
				liNIndex++;
				lchKeyChar = KeyEvent.VK_BACK_SPACE;
			}
			// defect 10545 
			else if (!Character.isDigit(lchKeyChar))
			{
				if (isDateEmpty()
					&& (lchKeyChar == KeyEvent.VK_T || lchKeyChar == 't'))
				{
					setDate(new RTSDate());
				}
				else if (
					(lchKeyChar == '+' || lchKeyChar == '-')
						&& isValidDate())
				{
					RTSDate laDate = getDate();
					int liDelta = lchKeyChar == '+' ? 1 : -1;
					laDate = laDate.add(RTSDate.DATE, liDelta);
					int liCaretPos = getCaretPosition(); 
					setDate(laDate);
					setCaretPosition(liCaretPos); 
				}
			}
			// end defect 10545 

			if (liNIndex < ciCharNum && liNIndex >= 0)
			{
				switch (lchKeyChar)
				{
					case KeyEvent.VK_BACK_SPACE :
						{
							String lsStrSelec = getSelectedText();
							if (lsStrSelec != null)
							{
								int liSt = getSelectionStart();
								int liEnd = getSelectionEnd();
								for (int i = liSt; i < liEnd; i++)
								{
									if (i >= 0 && i < ciCharNum)
									{
										chArr[i] = ' ';
									}
								}
								setSlashes();
								refreshDate(new String(chArr));
								setCaretPosition(liNIndex);
							}
							else
							{
								if (liNIndex > 0)
								{
									//if(!checkIfSlash())
									if (
										!checkIfSlashForBackSpc(liNIndex))
									{
										chArr[liNIndex - 1] = ' ';
										//chArr[nIndex] = ' ';
										liNIndex--;
										refreshDate(new String(chArr));
										setCaretPosition(liNIndex);
									}
									else
									{
										//chArr[nIndex-1]=' ';
										chArr[liNIndex - 2] = ' ';
										liNIndex = liNIndex - 2;
										refreshDate(new String(chArr));
										setCaretPosition(liNIndex);
									}
								}
							}
							//refreshDate(new String(chArr), nIndex);

							break;
						}
					default :
						{
							if (!Character.isDigit(lchKeyChar))
							{
								break;
							}
							if (checkIfSlash())
							{
								liNIndex++;
							}
							chArr[liNIndex] = lchKeyChar;
							refreshDate(new String(chArr));
							setCaretPosition(liNIndex + 1);
							break;
						}
				}
			}
			if (liNIndex == ciCharNum
				&& lchKeyChar == KeyEvent.VK_BACK_SPACE)
			{
				chArr[liNIndex - 1] = ' ';
				liNIndex--;
				refreshDate(new String(chArr));
				setCaretPosition(liNIndex);

			}
			if (aaKE.getKeyChar() == KeyEvent.VK_DELETE)
			{
				if (isMonthYrOnly())
				{
					if (liNIndex == 1)
					{
						setCaretPosition(liNIndex + 2);
					}
					else
					{
						setCaretPosition(liNIndex + 1);
					}
				}
				else
				{
					if (liNIndex == 1 || liNIndex == 4)
					{
						setCaretPosition(liNIndex + 2);
					}
					else
					{
						setCaretPosition(liNIndex + 1);
					}
				}

				//if (wasSlashForDelete)
				//	setCaretPosition(nIndex+2);
				//	else
				//	setCaretPosition(nIndex+1);
			}
		}
		catch (ArrayIndexOutOfBoundsException leAIOOBEx)
		{
			// empty code block
		}
		catch (Exception leEx)
		{
			// empty code block
		}
		finally
		{
			if (!(aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_HOME
				|| aaKE.getKeyCode() == KeyEvent.VK_END))
			{
				aaKE.consume();
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
		//User might have selected all the text and hit back space to 
		//delete all the text

		String lsStrTxt = getText();
		if (lsStrTxt.length() < 1)
		{
			initialize();
		}
		/*
		else
		{
			String strTemp = getText();	
			char ch = e.getKeyChar();
		
			int nIndex = getCaretPosition();
		
			try
			{
				//if(!Character.isDigit(ch))
				//	return;
					
				if(nIndex<ciCharNum)
				{
					switch(ch)
					{
						case KeyEvent.VK_BACK_SPACE :
						{
							if(!checkIfSlash())
							{
								chArr[nIndex] = ' ';
								nIndex--;
							}
							else
							{
								chArr[nIndex-1]=' ';
								nIndex = nIndex-2;
							}
							refreshDate(new String(chArr), nIndex);
							
							break;
						}
						default :
						{
							if(!Character.isDigit(ch))
								break;
		
							if(checkIfSlash())
								nIndex++;
							chArr[nIndex-1] = ch;
							refreshDate(new String(chArr), nIndex-1);
							break;
							
						}
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException eR)
			{
				System.out.println("Exception:" + nIndex);
		
			}
			catch(Exception eR)
			{
				System.out.println("Exception of---:");
			}
			finally
			{
				e.consume();
			}
		
		}
		*/
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		aaKE.consume();
		/*
		
		String strTemp = getText();	
		char ch = e.getKeyChar();
		
		int nIndex = getCaretPosition();
		try
		{
			//if(!Character.isDigit(ch))
			//	return;
				
			if(nIndex<ciCharNum)
			{
				switch(ch)
				{
					case KeyEvent.VK_BACK_SPACE :
					{
						if(!checkIfSlash())
						{
							chArr[nIndex] = ' ';
							nIndex--;
						}
						else
						{
							chArr[nIndex-1]=' ';
							nIndex = nIndex-2;
						}
						refreshDate(new String(chArr), nIndex);
						
						break;
					}
					default :
					{
						if(!Character.isDigit(ch))
							break;
		
						if(checkIfSlash())
							nIndex++;
						chArr[nIndex] = ch;
						refreshDate(new String(chArr), nIndex);
						break;
						
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException eR)
		{
			System.out.println("Exception:" + nIndex);
		
		}
		catch(Exception eR)
		{
			System.out.println("Exception of---:");
		}
		finally
		{
			e.consume();
		}
		*/
	}

	/**
	 * Refresh date
	 * 
	 * @param asStrTemp String
	 */
	private void refreshDate(String asStrTemp)
	{
		asStrTemp = new String(chArr);
		setText(asStrTemp);
		asStrTemp = null;
	}

	/**
	 * Sets the RTSDate into the JDateTextField
	 * 
	 * @param aaDate RTSDate
	 */
	public void setDate(RTSDate aaDate)
	{
		if (aaDate == null)
		{
			initialize();
			return;
		}
		String lsMonth = Integer.toString(aaDate.getMonth());
		String lsDay = Integer.toString(aaDate.getDate());
		String lsYear = Integer.toString(aaDate.getYear());

		if (lsMonth.length() == 2)
		{
			chArr[0] = lsMonth.charAt(0);
			chArr[1] = lsMonth.charAt(1);
		}
		else
		{
			chArr[0] = '0';
			chArr[1] = lsMonth.charAt(0);
		}

		if (!isMonthYrOnly())
		{
			if (lsDay.length() == 2)
			{
				chArr[3] = lsDay.charAt(0);
				chArr[4] = lsDay.charAt(1);
			}
			else
			{
				chArr[3] = '0';
				chArr[4] = lsDay.charAt(0);
			}
		}

		if (isMonthYrOnly())
		{
			chArr[3] = lsYear.charAt(0);
			chArr[4] = lsYear.charAt(1);
			chArr[5] = lsYear.charAt(2);
			chArr[6] = lsYear.charAt(3);

		}
		else
		{
			chArr[6] = lsYear.charAt(0);
			chArr[7] = lsYear.charAt(1);
			chArr[8] = lsYear.charAt(2);
			chArr[9] = lsYear.charAt(3);
		}

		csStrText = new String(chArr);
		setText(csStrText);
		setCaretPosition(0);
	}

	/**
	 * Change Managing Focus
	 * 
	 * @param abIsManagingFocus boolean
	 */
	public void setManagingFocus(boolean abIsManagingFocus)
	{
		cbIsManagingFocus = abIsManagingFocus;
	}

	/**
	 * Set Month Year Only
	 * 
	 * @param abNewMonthYrOnly boolean
	 */
	public void setMonthYrOnly(boolean abNewMonthYrOnly)
	{
		cbMonthYrOnly = abNewMonthYrOnly;
		initialize();
	}

	/**
	 * Set Slashes
	 *  
	 */
	private void setSlashes()
	{
		chArr[2] = '/';

		if (!cbMonthYrOnly)
		{
			chArr[5] = '/';
		}
	}

	/**
	 * Set Text
	 * 
	 * @param asText String
	 */
	public void setText(String asText)
	{
		if (asText.equals(""))
		{
			for (int i = 0; i < ciCharNum; i++)
			{
				chArr[i] = ' ';
			}
			chArr[2] = '/';

			if (!cbMonthYrOnly)
			{
				chArr[5] = '/';
			}
			asText = new String(chArr);
		}

		super.setText(asText);
	}
}
