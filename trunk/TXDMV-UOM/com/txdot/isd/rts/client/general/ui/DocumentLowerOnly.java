package com.txdot.isd.rts.client.general.ui;

import javax.swing.text.*;

/*
 * DocumentLowerOnly.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs 		04/16/2002	Changed method call to UtilityMethods.beep()
 * B Hargrove	08/18/2005	Java 1.4 code changes.
 * 							Format comments, Hungarian notation for 
 * 							variables, etc. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Allows only lowercase characters
 * 
 * @version	5.2.3		08/18/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	09/05/2001 11:04:36
 */
public class DocumentLowerOnly extends AbstractDocument
{
	/**
	 * Creates a DocumentLowerOnly.
	 * 
	 * @param aiMaxLength int
	 */
	public DocumentLowerOnly(int aiMaxLength)
	{
		super();
		this.ciMaxLength = aiMaxLength;
	}
	/**
	 * Filters the text.
	 * @param aiOffs int
	 * @param asStr String
	 * @param aaAttr AttributeSet
	 * @throws BadLocationException exception description.
	 */
	public void insertString(
		int aiOffs,
		String asStr,
		AttributeSet aaAttr)
		throws javax.swing.text.BadLocationException
	{
		String lsTemp = "";
		for (int i = 0; i < asStr.length(); i++)
		{
			char lchChar = asStr.charAt(i);
			if (Character.isLowerCase(lchChar))
			{
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
