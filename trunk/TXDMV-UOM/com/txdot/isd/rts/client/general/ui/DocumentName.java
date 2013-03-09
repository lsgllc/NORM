package com.txdot.isd.rts.client.general.ui;

import javax.swing.text.*;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * DocumentName.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/02/2006	Allows letters, numbers, spaces, and dashes 
 * 							to be entered in the input field.
 * 							new class
 * 							defect 8938 Ver FallAdminTables
 * ---------------------------------------------------------------------
 */

/**
 * Allows special characters to be entered
 *
 * @version	FallAdminTables 10/02/2006
 * @author	Min Wang
 * <br>Creation Date:		09/29/2006 15:30:00
 */
public class DocumentName extends AbstractDocument
{

	/**
	 * Creates a DocumentName Document.
	*/
	public DocumentName(int maxLength)
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
	public void insertString(
		int aiOffs,
		String asStr,
		AttributeSet aaAttr)
		throws BadLocationException
	{
		String lsTemp = CommonConstant.STR_SPACE_EMPTY;
		for (int i = 0; i < asStr.length(); i++)
		{
			char lchChar = asStr.charAt(i);
			if (Character.isDigit(lchChar)
				|| Character.isLetter(lchChar)
				|| Character.isSpaceChar(lchChar)
				|| lchChar == CommonConstant.CHAR_DASH)
			{
				lchChar = Character.toUpperCase(lchChar);
				lsTemp = lsTemp + lchChar;
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
					new String(
						CommonConstant.STR_SPACE_EMPTY + lchChar),
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
