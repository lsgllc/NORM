package com.txdot.isd.rts.client.general.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.txdot.isd.rts.services.util.UtilityMethods;

/* 
 * RTSPhoneField.java
 * 
 * (c) Texas Department of Transportation  2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		06/17/2005	Added class to be used by the county 
 * 							processing address info screen.  This type
 * 							of processing was beeing handled in the 
 * 							local Keylistener methods.  I have created 
 * 							this class as a mimic of RTSDateField.
 * 							defect 7889 Ver 5.2.3
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							Hungarian notation, etc. 
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	07/22/2007	add isPhoneNoAllZeros()
 * 							defect 9085 Ver Special Plates 
 * J Zwiener	02/03/2009	Verify all integers in last 4 of phone no.
 * 							modify isNumberValid()
 * 							defect 9589 Ver Defect_POS_D
 * K Harrell	10/22/2010	add getPhoneText() 
 * 							defect 10592 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */
/**
 * A TextField with the values of "   -   -    " already in place.
 * <br>It handles everything associated with the entry and setting of 
 * a phone number and can be used exactly as a JTextField would be.
 * 
 * @version	6.6.0			10/22/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		06/07/2005 13:20:37
 */
public class RTSPhoneField
	extends JTextField
	implements KeyListener, FocusListener
{
	String csStrText = null;
	char[] chArr = null;
	private boolean cbPrefixNumberOnly = false;
	private int ciCharNum;

	/**
	 * Creates a JDateTextField
	 */
	public RTSPhoneField()
	{
		super();
		initialize();
		addKeyListener(this);
		addFocusListener(this);
	}
	/**
	 * Check is the caret postion is at a dash.
	 * This uses the index position.  Exact position + 1.
	 * 
	 * @return boolean
	 */
	private boolean checkIfDash()
	{
		int liNIndex = getCaretPosition();
		if (isPrefixNumberOnly())
		{
			if (liNIndex == 3)
			{
				return true;
			}
		}
		else
		{
			if (liNIndex == 3 || liNIndex == 7)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Check position to see if it should be a '-'.
	 * This uses the exact position index + 1.
	 *  
	 * @param aiNIndex int
	 * @return boolean
	 */
	private boolean checkIfDashForBackSpc(int aiNIndex)
	{
		if (isPrefixNumberOnly())
		{
			if (aiNIndex == 4)
			{
				return true;
			}
		}
		else
		{
			if (aiNIndex == 4 || aiNIndex == 8)
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
	* Do nothing when focus is lost.
	* 
	* @param aaFE FocusEvent
	*/
	public void focusLost(FocusEvent aaFE)
	{
	}
	/**
	 * Returns the Phone Number in the TextField the format 
	 * returned will be 10 digits ie:5123109074.
	 * 
	 * If the phone number is not valid then it will return "".
	 * 
	 * @return String
	 */
	public String getPhoneNo()
	{
		if (isValidPhoneNo())
		{
			return getAreaCode() + getPrefix() + getNumber();
		}
		else
		{
			return "";
		}
	}
	/**
	 * Returns the Phone Number without dashes(even if it is invalid) 
	 * 
	 * @return String
	 */
	public String getPhoneText()
	{
		String lsPhone = getText();
		lsPhone = lsPhone.replaceAll("-",""); 
		return lsPhone.trim(); 
	}

	/**
	 * Returns the phone number prefix.  If the field is set to only
	 * display the prefix and the number then we have to get the results
	 * from a different location in the character array.
	 * 
	 * @return String
	 */
	private String getPrefix()
	{
		if (isPrefixNumberOnly())
		{
			return new String("" + chArr[0] + chArr[1] + chArr[2]);
		}
		else
		{
			return new String("" + chArr[4] + chArr[5] + chArr[6]);
		}
	}
	/**
	 * Returns the area code.  If this field has set to only display the
	 * prefix and number then return "".
	 * 
	 * @return String
	 */
	private String getAreaCode()
	{
		if (isPrefixNumberOnly())
		{
			return "";
		}
		else
		{
			return new String("" + chArr[0] + chArr[1] + chArr[2]);
		}
	}
	/**
	 * Gets the phone number - the last four digits. If the field is set
	 * to only display the prefix and the number then we have to get 
	 * the results from a different location in the character array.
	 * 
	 * @return String
	 */
	private String getNumber()
	{
		if (isPrefixNumberOnly())
		{
			return new String(
				"" + chArr[3] + chArr[4] + chArr[5] + chArr[6]);
		}
		else
		{
			return new String(
				"" + chArr[8] + chArr[9] + chArr[10] + chArr[11]);
		}
	}
	/**
	* initialize the fields and do initial settings for char array
	*/
	private void initialize()
	{
		csStrText = new String();
		if (cbPrefixNumberOnly)
		{
			// 000-0000
			ciCharNum = 8;
		}
		else
		{
			// 000-000-0000
			ciCharNum = 12;
		}

		setColumns(ciCharNum);
		chArr = new char[ciCharNum];
		for (int i = 0; i < ciCharNum; i++)
		{
			chArr[i] = ' ';
		}

		setDashes();
		setText(new String(chArr));
		setCaretPosition(0);

	}

	/**
	 * Is Phone number field all Zeroes
	 * 
	 * @return boolean
	 */
	public boolean isPhoneNoAllZeros()
	{
		boolean lbRet = false;
		String lsStrPhone = new String(chArr);
		lsStrPhone = lsStrPhone.replace('-', '0');
		lsStrPhone = lsStrPhone.trim();
		if (lsStrPhone.length() > 2
			&& UtilityMethods.isAllZeros(lsStrPhone))
		{
			lbRet = true;
		}
		return lbRet;
	}
	/**
	 * Is Phone number field Empty?
	 * 
	 * @return boolean
	 */
	public boolean isPhoneNoEmpty()
	{
		boolean lbRet = false;
		String lsStrPhone = new String(chArr);
		lsStrPhone = lsStrPhone.replace('-', ' ');
		lsStrPhone = lsStrPhone.trim();
		if (lsStrPhone.length() < 1)
		{
			lbRet = true;
		}
		return lbRet;
	}
	/**
	 * This methord returns if the phone number extention is valid.
	 * 
	 * @return boolean
	 */
	private boolean isExtensionValid()
	{
		boolean lbRVal = true;
		String lsStrPrefix = getPrefix().trim();
		if (lsStrPrefix.length() == 3)
		{
			try
			{
				Integer.parseInt(lsStrPrefix);
			}
			catch (NumberFormatException leNFEx)
			{
				lbRVal = false;
			}
		}
		else
		{
			lbRVal = false;
		}
		return lbRVal;
	}
	/**
	 * Checks to make sure that the area code is valid.  A valid area
	 * code is all numbers and the length is 3.  We have to check to 
	 * make sure that we are not leaving out the area code.
	 * 
	 * @return boolean
	 */
	private boolean isAreaCodeValid()
	{
		boolean rbVal = true;

		// If we are only showing prefix and number then return true
		if (!isPrefixNumberOnly())
		{
			String lsStrAreaCode = getAreaCode().trim();
			if (lsStrAreaCode.length() == 3)
			{
				try
				{
					Integer.parseInt(lsStrAreaCode);
				}
				catch (NumberFormatException leNFEx)
				{
					rbVal = false;
				}
			}
			else
			{
				rbVal = false;
			}
		}
		return rbVal;

	}
	/**
	 * Is it prefix and number only.
	 * ie: 578-3100
	 * 
	 * @return boolean
	 */
	public boolean isPrefixNumberOnly()
	{
		return cbPrefixNumberOnly;
	}
	/**
	* Check if the current phone number showing in the field is a valid 
	* Phone number.
	* 
	* If return value is false, the window containing this field should
	* display error message and set focus back to this control.
	* 
	* @return boolean
	*/
	public boolean isValidPhoneNo()
	{
		boolean lbRVal = true;
		if (!isAreaCodeValid()
			|| !isExtensionValid()
			|| !isNumberValid())
		{
			lbRVal = false;
		}
		return lbRVal;
	}
	/**
	 * Is the phone number value valid.
	 * 
	 * @return boolean
	 */
	private boolean isNumberValid()
	{
		boolean lbRVal = false;

		String lsStrNumber = getNumber();
		lsStrNumber = lsStrNumber.trim();

		try
		{
			// defect 9589
			Integer.parseInt(lsStrNumber);
			// end defect 9589
			if (lsStrNumber.length() == 4)
			{
				lbRVal = true;
			}
			else
			{
				lbRVal = false;
			}
		}
		catch (NumberFormatException leNFEx)
		{
			lbRVal = false;
		}
		return lbRVal;
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
				// If you hit del and you are at the end, or the next
				// character is a ' ' and you have not selected anything
				// then just consume the del.
				if ((liNIndex == ciCharNum || chArr[liNIndex] == ' ')
					&& getSelectedText() == null)
				{
					aaKE.consume();
					return;
				}
				// Only add to the liNIndex when you are not at the end
				if (liNIndex != ciCharNum)
				{
					liNIndex++;
				}
				lchKeyChar = KeyEvent.VK_BACK_SPACE;
			}

			if (liNIndex <= ciCharNum && liNIndex >= 0)
			{
				switch (lchKeyChar)
				{
					case KeyEvent.VK_BACK_SPACE :
						{
							String lsStrSelec = getSelectedText();
							// We are trying to delete a selected area.
							if (lsStrSelec != null)
							{
								// Loop through the selection and replace
								// the section with a space
								int liSt = getSelectionStart();
								int liEnd = getSelectionEnd();
								for (int i = liSt; i < liEnd; i++)
								{
									if (i >= 0 && i < ciCharNum)
										chArr[i] = ' ';
								}
								// Put the dashes back
								setDashes();
								refreshPhoneNum(
									new String(chArr),
									liNIndex);

								// Set the Caret position
								// If the selection was on the entire
								// thing or the starting position is the
								// first position then return to the 
								// first position.
								if ((liEnd - liSt) == ciCharNum
									|| liSt == 0)
								{
									liNIndex = 0;
								}
								else
								{
									// If the slection is not at the 
									// beginning then you want to go to
									// that point to begin typing.
									liNIndex = liSt;
								}

								// All of the values are subtracted by 
								// one since 1 will be added at the 
								// bottom if the key event is del.
								if (aaKE.getKeyChar()
									== KeyEvent.VK_DELETE)
								{
									liNIndex--;
								}
								else
								{
									setCaretPosition(liNIndex);
								}
							}
							// We do not have an area selected treat it
							// as trying to delete on character.
							else
							{
								// No need to do anything is you are at
								// the beginning
								if (liNIndex > 0)
								{
									// Verify we are not trying to 
									// delete a dash.
									if (
										!checkIfDashForBackSpc(liNIndex))
									{
										chArr[liNIndex - 1] = ' ';
										liNIndex--;
										refreshPhoneNum(
											new String(chArr),
											liNIndex);
										setCaretPosition(liNIndex);
									}
									else
									{
										chArr[liNIndex - 2] = ' ';
										liNIndex = liNIndex - 2;
										refreshPhoneNum(
											new String(chArr),
											liNIndex);
										setCaretPosition(liNIndex);
									}
								}
							}
							break;
						}
						// Eat all other character other than digits
					default :
						{
							if (!Character.isDigit(lchKeyChar)
								|| liNIndex == ciCharNum)
							{
								break;
							}

							if (checkIfDash())
							{
								liNIndex++;
							}
							chArr[liNIndex] = lchKeyChar;
							refreshPhoneNum(
								new String(chArr),
								liNIndex);
							setCaretPosition(liNIndex + 1);
							break;
						}
				}
			}

			// If you are at the end and you hit backspace.  Delete the
			// last character and move to that position.
			if (liNIndex == ciCharNum
				&& lchKeyChar == KeyEvent.VK_BACK_SPACE)
			{
				chArr[liNIndex - 1] = ' ';
				liNIndex--;
				refreshPhoneNum(new String(chArr), liNIndex);
				setCaretPosition(liNIndex);

			}
			// Any time delete is done and it is not consumed above
			// There is a check to see where to move next.  This makes 
			// sure that you do not end up on a dash.
			if (aaKE.getKeyChar() == KeyEvent.VK_DELETE)
			{
				if (isPrefixNumberOnly())
				{
					if (liNIndex == 2)
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
					// If the index is on a '-' move past it
					if (liNIndex == 2 || liNIndex == 6)
					{
						setCaretPosition(liNIndex + 2);
					}
					// else just more to the next spot
					else
					{
						setCaretPosition(liNIndex + 1);
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException leAIOOBEx)
		{
			leAIOOBEx.printStackTrace();
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}
		finally
		{
			if (!(aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_HOME
				|| aaKE.getKeyCode() == KeyEvent.VK_END))
				aaKE.consume();
		}

	}
	/**
	 * Invoked when a key has been released.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyReleased(java.awt.event.KeyEvent aaKE)
	{
		//User might have selected all the text and hit back space to 
		//delete all the text
		String lsStrTxt = getText();
		if (lsStrTxt.length() < 1)
		{
			initialize();
		}
	}
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyTyped(java.awt.event.KeyEvent aaKE)
	{
		aaKE.consume();
	}
	/**
	 * Refreshes the text field with the new values.
	 * 
	 * @param asStrTemp
	 * @param aiNIndex
	 */
	private void refreshPhoneNum(String asStrTemp, int aiNIndex)
	{
		asStrTemp = new String(chArr);
		setText(asStrTemp);
		asStrTemp = null;
	}
	/**
	 * Set that you only want to show the Prefix and number only.
	 * ie: 573-9978
	 * 
	 * @param abNewProfixNumberOnly boolean
	 */
	public void setPrefixNumberOnly(boolean abNewProfixNumberOnly)
	{
		cbPrefixNumberOnly = abNewProfixNumberOnly;
		initialize();
	}
	/**
	 * Sets the Phone number in the TextField.  If you have set 
	 * PrefixNumberOnly then it will take the first three characters 
	 * and set then as the prefix and the last four as the number.
	 * 
	 * If you want the area code (default) then the first theree are
	 * the area code. The next three are the prefix.  The last four are
	 * put in the number field.
	 * 
	 * Format:
	 * 5122827884
	 * cbPrefixNumberOnly = true
	 * 2827884
	 * 
	 * 
	 * 
	 * @param asPhoneNo String
	 */
	public void setPhoneNo(String asPhoneNo)
	{
		if (asPhoneNo == null || asPhoneNo.length() == 0)
		{
			initialize();
			return;
		}

		String lsAreaCode = "";
		String lsExtension = "";
		String lsNumber = "";

		try
		{
			if (isPrefixNumberOnly())
			{
				//Extension
				if (asPhoneNo.length() >= 3)
				{
					lsExtension = asPhoneNo.substring(0, 3);
					Integer.parseInt(lsExtension);
					chArr[0] = lsExtension.charAt(0);
					chArr[1] = lsExtension.charAt(1);
					chArr[2] = lsExtension.charAt(2);
				}
				else
				{
					lsExtension =
						asPhoneNo.substring(0, asPhoneNo.length());
					Integer.parseInt(lsExtension);

					for (int i = 0; i < lsExtension.length(); i++)
					{
						chArr[i] = lsExtension.charAt(i);
					}
				}

				// Number
				if (asPhoneNo.length() >= 7)
				{
					lsNumber = asPhoneNo.substring(3, 7);
					Integer.parseInt(lsNumber);
					chArr[4] = lsNumber.charAt(0);
					chArr[5] = lsNumber.charAt(1);
					chArr[6] = lsNumber.charAt(2);
				}
				else if (asPhoneNo.length() > 3)
				{
					lsNumber =
						asPhoneNo.substring(3, asPhoneNo.length());
					Integer.parseInt(lsNumber);

					for (int i = 0; i < lsNumber.length(); i++)
					{
						chArr[4 + i] = lsNumber.charAt(i);
					}
				}
			}
			else
			{
				//Area Code
				if (asPhoneNo.length() >= 3)
				{
					lsAreaCode = asPhoneNo.substring(0, 3);
					Integer.parseInt(lsAreaCode);
					chArr[0] = lsAreaCode.charAt(0);
					chArr[1] = lsAreaCode.charAt(1);
					chArr[2] = lsAreaCode.charAt(2);
				}
				else
				{
					lsAreaCode =
						asPhoneNo.substring(0, asPhoneNo.length());
					Integer.parseInt(lsAreaCode);

					for (int i = 0; i < lsAreaCode.length(); i++)
					{
						chArr[i] = lsAreaCode.charAt(i);
					}
				}

				//Extension
				if (asPhoneNo.length() >= 6)
				{
					lsExtension = asPhoneNo.substring(3, 6);
					Integer.parseInt(lsExtension);
					chArr[4] = lsExtension.charAt(0);
					chArr[5] = lsExtension.charAt(1);
					chArr[6] = lsExtension.charAt(2);
				}
				else if (asPhoneNo.length() > 3)
				{
					lsExtension =
						asPhoneNo.substring(3, asPhoneNo.length());
					Integer.parseInt(lsExtension);

					for (int i = 0; i < lsExtension.length(); i++)
					{
						chArr[4 + i] = lsExtension.charAt(i);
					}
				}

				//Number
				if (asPhoneNo.length() >= 10)
				{
					lsNumber = asPhoneNo.substring(6, 10);
					Integer.parseInt(lsNumber);
					chArr[8] = lsNumber.charAt(0);
					chArr[9] = lsNumber.charAt(1);
					chArr[10] = lsNumber.charAt(2);
					chArr[11] = lsNumber.charAt(3);
				}
				else if (asPhoneNo.length() > 6)
				{
					lsNumber =
						asPhoneNo.substring(6, asPhoneNo.length());
					Integer.parseInt(lsNumber);

					for (int i = 0; i < lsNumber.length(); i++)
					{
						chArr[i + 8] = lsExtension.charAt(i);
					}
				}
			}
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}

		csStrText = new String(chArr);
		setText(csStrText);
		setCaretPosition(0);
	}
	/**
	 * Sets the Dashes in the text.
	 * ie: "000-000-0000"
	 */
	private void setDashes()
	{
		chArr[3] = '-';

		if (!isPrefixNumberOnly())
		{
			chArr[7] = '-';
		}
	}
}