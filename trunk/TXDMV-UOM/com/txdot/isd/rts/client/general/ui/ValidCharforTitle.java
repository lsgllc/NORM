package com.txdot.isd.rts.client.general.ui;

import javax.swing.text.*;
/*
 * DocumentValidChar.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		12/08/2006	Allows letters, numbers, and valid char 
 * 							to be entered in the input field for title.
 * 							new class
 * 							defect 10299 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * Allows letters, numbers, and valid characters to be entered
 *
 * @version	Defect_POS_H	12/08/2009
 * @author	MWANG
 * <br>Creation Date:		12/08/2009 08:10:00
 */
public class ValidCharforTitle extends AbstractDocument
{
	/**
	 * Creates a DocumentValidChar Document.
	 */
	public ValidCharforTitle(int maxLength)
	{
		super();
		this.ciMaxLength = maxLength;
	}
		
	/**
	 * Filters the text.
	 * 
	 * @param aiOffs int
	 * @param asStr String
	 * @param aaAttr AttributeSet
	 * @throws BadLocationException exception description.
	 */
	public void insertString
	(
		int aiOffs,
		String asStr,
		AttributeSet aaAttr)
		throws BadLocationException
		{
			String lsTemp = "";
			for (int i = 0; i < asStr.length(); i++)
			{
				char lchChar = asStr.charAt(i);
				if (Character.isDigit(lchChar)
					|| Character.isLetter(lchChar)
					|| Character.isSpaceChar(lchChar)
					|| lchChar == '#'
					|| lchChar == '('
					|| lchChar == ')'
					|| lchChar == '-'
					|| lchChar == ':'
					|| lchChar == ';'
					|| lchChar == '?'
					|| lchChar == ','
					|| lchChar == '.'
					|| lchChar == '&'
					|| lchChar == '*'
					|| lchChar == '/'
					|| lchChar == '\''
					|| lchChar == '+'
					
					)
				{
					lchChar = Character.toUpperCase(lchChar);
					lsTemp += lchChar;
				}
			}
			int liTempOffs = aiOffs;
			for (int i = 0; i < lsTemp.length(); i++)
			{
				char lchChar = lsTemp.charAt(i);
				if (getLength() < ciMaxLength)
				{
					super.insertString(
						liTempOffs,
						new String("" + lchChar),
						aaAttr);
					liTempOffs++;
				}
				else if (liTempOffs == ciMaxLength)
				{
					com.txdot.isd.rts.services.util.UtilityMethods.beep();
				}
			}
		}
}
