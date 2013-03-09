package com.txdot.isd.rts.client.general.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.txdot.isd.rts.services.util.RTSDate;

/* 
 * RTSTimeField.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown 		10/01/2002	Fix CQU100004794. In method isHourValid(),  
 *                          isMinuteValid, and isSecondValid(), want to 
 *                          make sure the length of hours, minutes, and   
 *                          seconds entered is 2, to avoid number format
 * 							exceptions.
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							Hungarian notation for variables, etc. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	07/10/2010	add T/+/- actions on keyPressed
 * 							add checkIfColon(), checkIfPosAfterColon(),
 * 							 setColons(), isTimeEmpty()
 * 							delete YEAR_1900, YEAR_2099, PIVOT_VAL
 * 							delete checkIfSlash(), checkIfSlashColon(), 
 * 							setSlashes()
 * 							modify keyPressed()
 * 							defect 10545 Ver 6.5.0   
 * K Harrell	07/12/2010	Modification of time is a function of 
 * 							caret position.
 * 							modify setTime() 
 * 							defect 10545 Ver 6.5.0
 * K Harrell	07/22/2010	add get24HrTime()
 * 							defect 10545 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */
/**
 * A TextField with the values of "  :  :    " already in place.
 * <br>It handles everything associated with the entry and setting of 
 * dates and can be used exactly as a JTextField would be.
 * 
 * @version	6.5.0  		07/22/2010
 * @author	Ashish Mahajan
 * <br>Creation Date:	01/18/2002 10:30:43
 */
public class RTSTimeField
	extends javax.swing.JTextField
	implements KeyListener, FocusListener
{
	private String csStrText = null;
	private char[] chArr = null;

	// defect 10545 
	// public static final int YEAR_1900 = 1900;
	// public static final int YEAR_2099 = 2099;
	// public static final int PIVOT_VAL = 50;
	// end defect 10545

	/**
	 * RTSTimeField constructor
	 */
	public RTSTimeField()
	{
		super();
		initialize();
		addKeyListener(this);
		addFocusListener(this);
	}

	/**
	 * Check if Colon
	 * 
	 * @return boolean
	 */
	private boolean checkIfColon()
	{
		int liNIndex = getCaretPosition();

		return liNIndex == 2 || liNIndex == 5;
	}

	/**
	 * Check If Positioned after Colon
	 *  
	 * @return boolean
	 */
	private boolean checkIfPosAfterColon()
	{
		int liNIndex = getCaretPosition();

		return liNIndex == 3 || liNIndex == 6;
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
		setText(new String(chArr));
	}

	/**
	 * Get Hour
	 *  
	 * @return String
	 */
	private String getHour()
	{
		return new String("" + chArr[0] + chArr[1]);
	}

	/**
	 * Returns the int value of Time
	 * 
	 * @return int  
	 */
	public int get24HrTime()
	{
		return Integer.parseInt(getHour() + getMinute() + getSecond());
	}

	/**
	 * Get Minute
	 *  
	 * @return String
	 */
	private String getMinute()
	{
		return new String("" + chArr[3] + chArr[4]);
	}

	/**
	 * Get Second
	 *  
	 * @return String
	 */
	private String getSecond()
	{
		return new String("" + chArr[6] + chArr[7]);
	}

	/**
	 * Get Text
	 * 
	 * @return String
	 */
	public String getText()
	{
		return super.getText();
	}

	/**
	 * Returns the RTSTime in the JDateTextField
	 * @return String 
	 */
	public String getTime()
	{
		String lsHour = "" + chArr[0] + chArr[1];
		String lsMinute = "" + chArr[3] + chArr[4];
		String lsSecond = "" + chArr[6] + chArr[7];
		String lsTime = null;
		lsTime = lsHour + ":" + lsMinute + ":" + lsSecond;
		return lsTime;
	}

	/**
	* initialize the fields and do initial settings for char array
	*/
	private void initialize()
	{
		setColumns(8);
		csStrText = new String();
		chArr = new char[8];
		for (int i = 0; i < 8; i++)
		{
			chArr[i] = ' ';
		}
		chArr[2] = ':';
		chArr[5] = ':';
		setText(new String(chArr));
		setCaretPosition(0);
	}

	/**
	 * Is Hour Valid
	 *  
	 * @return boolean
	 */
	public boolean isHourValid()
	{
		boolean lbVal = false;
		String lsHour = getHour();
		lsHour = lsHour.trim();
		// fix 4794
		if (lsHour.length() == 2)
		{
			int liHour = Integer.parseInt(lsHour);
			if (liHour >= 0 && liHour < 24)
			{
				lbVal = true;
			}
		}
		return lbVal;

	}
	/**
	 * Is Minute Valid
	 *  
	 * @return boolean
	 */

	public boolean isMinuteValid()
	{
		boolean lbVal = false;
		String lsMinute = getMinute();
		lsMinute = lsMinute.trim();
		// fix 4794
		if (lsMinute.length() == 2)
		{
			int liMinute = Integer.parseInt(lsMinute);
			if (liMinute >= 0 && liMinute < 60)
			{
				lbVal = true;
			}
		}
		return lbVal;

	}
	/**
	 * Is Second Valid
	 *  
	 * @return boolean
	 */
	public boolean isSecondValid()
	{
		boolean lbVal = false;
		String lsSecond = getSecond();
		lsSecond = lsSecond.trim();
		// fix 4794
		if (lsSecond.length() == 2)
		{
			int liSecond = Integer.parseInt(lsSecond);
			if (liSecond >= 0 && liSecond < 60)
			{
				lbVal = true;
			}
		}
		return lbVal;

	}

	/**
	 * Is Time Empty
	 * 
	 * @return boolean
	 */
	public boolean isTimeEmpty()
	{
		boolean lbRet = false;
		String lsStrDt = new String(chArr);
		lsStrDt = lsStrDt.replace(':', ' ');
		lsStrDt = lsStrDt.trim();
		if (lsStrDt.length() < 1)
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	 * Check if the current date showing in the datefield is a valid date
	 * If return value is false, the window containing this field should
	 * display error message and set focus back to this control.
	 * 
	 * @return boolean
	 */
	public boolean isValidTime()
	{
		boolean lbVal = true;
		//Use short circuit. If one is false don't evaluate others
		if (!isHourValid() || !isMinuteValid() || !isSecondValid())
		{
			lbVal = false;
		}
		return lbVal;

	}
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		char lchKeyChar = aaKE.getKeyChar();
		int liIndex = getCaretPosition();

		try
		{
			// defect 10545 
			if (isTimeEmpty()
				&& (lchKeyChar == KeyEvent.VK_T || lchKeyChar == 't'))
			{
				setTime(new RTSDate().getClockTimeNoMs());
			}
			else if (
				isValidTime()
					&& (lchKeyChar == '+' || lchKeyChar == '-'))
			{
				RTSDate laDate = new RTSDate();
				laDate.setTime(get24HrTime());
				int liDelta = lchKeyChar == '+' ? 1 : -1;
				int liComponent = RTSDate.HOUR;
				int liCaretPos = getCaretPosition();  
				if (liCaretPos >5) 
				{
					liComponent = RTSDate.SECOND;
				} 
				else if (liCaretPos >2) 
				{
					liComponent = RTSDate.MINUTE; 
				}
				laDate = laDate.add(liComponent, liDelta);
				setTime(laDate.getClockTimeNoMs());
				setCaretPosition(liCaretPos); 
			}
			// end defect 10545 

			if (liIndex < 8 && liIndex >= 0)
			{
				switch (lchKeyChar)
				{
					case KeyEvent.VK_BACK_SPACE :
						{
							String lsSelec = getSelectedText();
							if (lsSelec != null)
							{
								int liSt = getSelectionStart();
								int liEnd = getSelectionEnd();
								for (int i = liSt; i < liEnd; i++)
								{
									if (i >= 0 && i < 8)
									{
										chArr[i] = ' ';
									}
								}
								setColons();
								refreshDate(new String(chArr), liIndex);
								setCaretPosition(liIndex);

							}
							else
							{
								if (liIndex > 0)
								{
									// Replace w/ space if NOT after colon 
									if (!checkIfPosAfterColon())
									{
										chArr[liIndex - 1] = ' ';
										liIndex--;
										refreshDate(
											new String(chArr),
											liIndex);
										setCaretPosition(liIndex);
									}
									else
									{
										chArr[liIndex - 2] = ' ';
										liIndex = liIndex - 2;
										refreshDate(
											new String(chArr),
											liIndex);
										setCaretPosition(liIndex);
									}
								}
							}
							break;
						}
					default :
						{
							if (!Character.isDigit(lchKeyChar))
							{
								break;
							}
							if (checkIfColon())
							{
								liIndex++;
							}
							chArr[liIndex] = lchKeyChar;
							refreshDate(new String(chArr), liIndex);
							setCaretPosition(liIndex + 1);
							break;

						}
				}
			}

			if (liIndex == 8 && lchKeyChar == KeyEvent.VK_BACK_SPACE)
			{
				chArr[liIndex - 1] = ' ';
				liIndex--;
				refreshDate(new String(chArr), liIndex);
				setCaretPosition(liIndex);

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
				|| aaKE.getKeyCode() == KeyEvent.VK_END
				|| aaKE.getKeyCode() == KeyEvent.VK_DELETE))
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
	public void keyReleased(java.awt.event.KeyEvent aaKE)
	{
		String lsText = getText();
		if (lsText.length() < 1)
		{
			initialize();
		}
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(java.awt.event.KeyEvent aaKE)
	{
		aaKE.consume();
	}

	/**
	 * Refresh Date
	 * 
	 * @param asTemp String
	 * @param aiIndex int
	 */
	private void refreshDate(String asTemp, int aiIndex)
	{
		asTemp = new String(chArr);
		setText(asTemp);
		setCaretPosition(aiIndex + 1);
		asTemp = null;
	}

	/**
	 * Set Colons
	 */
	private void setColons()
	{
		chArr[2] = ':';
		chArr[5] = ':';
	}

	/**
	 * Set Time
	 * 
	 * @param asTime String
	 */
	public void setTime(String asTime)
	{
		if (asTime != null)
		{
			String lsHr = asTime.substring(0, 2);
			String lsMin = asTime.substring(3, 5);
			String lsSec = asTime.substring(6, 8);

			if (lsHr.length() == 2)
			{
				chArr[0] = lsHr.charAt(0);
				chArr[1] = lsHr.charAt(1);
			}
			if (lsMin.length() == 2)
			{
				chArr[3] = lsMin.charAt(0);
				chArr[4] = lsMin.charAt(1);
			}
			if (lsSec.length() == 2)
			{
				chArr[6] = lsSec.charAt(0);
				chArr[7] = lsSec.charAt(1);
			}
		}
		csStrText = new String(chArr);
		setText(csStrText);
		setCaretPosition(0);
	}
}
