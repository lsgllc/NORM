package com.txdot.isd.rts.client.general.ui;

import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * RTSInputField.java
 *
 * c Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/26/2001 	Added cbIsManagingFocus, setManagingFocus;
 *							modified keyPressed
 * J Seifert	12/10/2003	Added isEmpty() to return true or false
 *							if the field has data entered in it.
 *							Defect# 6311, 6422
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	08/06/2005	Add DEFAULT type for field definition.
 * 							Organize imports.  Formatting.
 * 							Add white space between methods.
 * 							Remove isManagingFocus() since it is 
 * 							deprecated and un-used.
 * 							Add ToDo to come back and look at 
 * 							keylistener.
 * 							add DEFAULT
 * 							delete isManagingFocus()
 * 							defect 7885 Ver 5.2.3
 * Min Wang		09/27/2006  Add NAME_FIELD to handle Name Fields.
 * 							add NAME_FIELD
 * 							modify createDefaultModel()
 * 							defect 8938 Ver FallAdminTables
 * K Harrell	07/18/2009	add isValidCountyNo(), isValidOwnerId(), 
 * 							 isValidState(), isValidZpcd(), 
 * 							 isValidZpcdP4() 
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	11/30/2009	add isValidMonth()
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * An RTSInputField is a modified JTextField which allows the following:
 * <ul>
 * <li>the RTSInputField ignores all text typed while the Alt button
 *  is pressed.  This is ideal for the TXDOT application where mnemonics
 * <li> are used extensively the RTSInputField allows the user to 
 * specify what type on input is allowed.  For example, the user 
 * can specifiy that only numerals be allowed, or only upper-case 
 * letters be allowed.  Should a character get typed that the 
 * RTSInputField does not allow, it will simply not appear on the screen.
 * <li>the RTSInputField allows the user to specify the maximum number 
 * of characters allowed.  For example, when entering license plates, a 
 * useful function would be to allow only 6 characters.
 * <li>the RTSInputField is very open to reuse.  Should a condition 
 * arise which is not addressed by the pre-made input choices,
 * one can be created and passed to the RTSInputField.
 * </ul>
 * 
 * @version	Defect_POS_H	12/09/2009 
 * @author	Michael Abernethy
 * <br>Creation Date:		08/09/2001 13:55:33
 */

public class RTSInputField extends JTextField implements KeyListener
{
	private int ciLength; 
	private int ciTypeLimited;
	public final static int DEFAULT = -1;
	public final static int ALPHA_ONLY = 0;
	public final static int NUMERIC_ONLY = 1;
	public final static int LOWER_CASE_ONLY = 2;
	public final static int UPPER_CASE_ONLY = 3;
	public final static int ALPHANUMERIC_ONLY = 4;
	public final static int DOLLAR_ONLY = 5;
	public final static int ALPHANUMERIC_NOSPACE = 6;
	// defect 8938
	public final static int NAME_FIELD = 7;
	// end defect 8938
	
	private AbstractDocument caDocument;

	private boolean cbIsManagingFocus = false;

	/**
	 * Creates a JInputField that allows unlimited number of 
	 * alphanumerics.
	 */
	public RTSInputField()
	{
		this(DEFAULT, Integer.MAX_VALUE);
	}

	/**
	 * A JInputField that allows unlimited number of specified input 
	 * types.
	 * 
	 * @param aiType int the type of input allowed
	 */
	public RTSInputField(int aiType)
	{
		this(aiType, Integer.MAX_VALUE);
	}

	/**
	 * A JInputField which allows a specified number of a certain input 
	 * type.
	 * 
	 * @param aiType int type of input allowed
	 * @param aiMaxLength int max number of characters allowed
	 */
	public RTSInputField(int aiType, int aiMaxLength)
	{
		super();
		ciTypeLimited = aiType;
		ciLength = aiMaxLength;
		addKeyListener(this);
		setDocument(createDefaultModel());
	}

	/**
	 * Creates a custom JInputField which allows a new input type should
	 * the one needed not be specified already by a constant.
	 * 
	 * @param aaAbstractDocument AbstractDocument - the input requirements
	 */
	public RTSInputField(AbstractDocument aaAbstractDocument)
	{
		this(aaAbstractDocument, Integer.MAX_VALUE);
	}

	/**
	 * Creates a custom JInputField which allows a new input type should
	 * the one needed not be specified already.
	 * 
	 * @param aaAbstractDocument AbstractDocument - the input requirements
	 * @param aiMaxLength int - the maximum ciLength
	 */
	public RTSInputField(
		AbstractDocument aaAbstractDocument,
		int aiMaxLength)
	{
		super();
		caDocument = aaAbstractDocument;
		ciLength = aiMaxLength;
		ciTypeLimited = DEFAULT;
		addKeyListener(this);
		setDocument(aaAbstractDocument);
	}

	/**
	 * A method to create the RTSInputField's Document.
	 * <br>Should not be called explicitly
	 * 
	 * @return javax.swing.text.Document
	 */
	public Document createDefaultModel()
	{
		// do not need to code else because we are returning out of
		// each if statement.
		if (ciTypeLimited == ALPHA_ONLY)
		{
			caDocument = new DocumentAlphaOnly(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == NUMERIC_ONLY)
		{
			caDocument = new DocumentNumericOnly(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == LOWER_CASE_ONLY)
		{
			caDocument = new DocumentLowerOnly(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == UPPER_CASE_ONLY)
		{
			caDocument = new DocumentUpperOnly(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == ALPHANUMERIC_ONLY)
		{
			caDocument = new DocumentAlphaNumOnly(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == ALPHANUMERIC_NOSPACE)
		{
			caDocument = new DocumentAlphaNoSpace(ciLength);
			return caDocument;
		}
		if (ciTypeLimited == DOLLAR_ONLY)
		{
			caDocument = new DocumentDollarOnly(ciLength);
			return caDocument;
		}
		// defect 8938
		if (ciTypeLimited == NAME_FIELD)
		{
			caDocument = new DocumentName(ciLength);
			return caDocument;
		}
		// end defect 8938
		else
		{
			// type is DEFAULT
			caDocument = new DocumentNoAlt(ciLength);
			return caDocument;
		}
	}

	/**
	 * Returns the input type.
	 * 
	 * @return int
	 */
	public int getInput()
	{
		return ciTypeLimited;
	}

	/**
	 * Returns the maximum ciLength of this RTSInputField
	 * 
	 * @return int
	 */
	public int getMaxLength()
	{
		return ciLength;
	}

	/**
	* Returns true or false depending on if this RTSInputField
	* has any data entered into it other than spaces
	* 
	* @return boolean
	*/
	public boolean isEmpty()
	{
		boolean lbReturn = false;
		try
		{
			if (this.getText() == null
				|| this.getText().trim().length() < 1)
			{
				lbReturn = true;
			}
		}
		catch (NullPointerException leNPEx)
		{
			// no value change is required.  just return false.
		}
		return lbReturn;
	}

	/**
	 * Is Valid County No 
	 */
	public boolean isValidCountyNo()
	{
		boolean lbValid = false;

		try
		{
			int liCntyNo = Integer.parseInt(getText());

			if (liCntyNo >= CommonConstant.MIN_COUNTY_NO
				&& liCntyNo <= CommonConstant.MAX_COUNTY_NO)
			{
				lbValid = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{

		}
		return lbValid;
	}

	/**
	 * Is Valid Month 
	 */
	public boolean isValidMonth()
	{
		boolean lbValid = false;

		try
		{
			int liCntyNo = Integer.parseInt(getText());

			if (liCntyNo >= 1 && liCntyNo <= 12)
			{
				lbValid = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{

		}
		return lbValid;
	}

	/**
	 *  Is Valid OwnerId
	 */
	public boolean isValidOwnerId()
	{
		return (
			getText().length() == 0
				|| getText().length() == CommonConstant.LENGTH_OWNERID);
	}

	/**
	 *  Is Valid State
	 */
	public boolean isValidState()
	{
		return CommonValidations.isValidState(getText().trim());
	}

	/**
	 * Is Valid Zpcd  
	 */
	public boolean isValidZpcd()
	{
		return (getText().length() == CommonConstant.LENGTH_ZIPCODE);
	}

	/**
	 * Is Valid ZpcdP4 
	 */
	public boolean isValidZpcdP4()
	{
		return (
			getText().length() == 0
				|| getText().length() == CommonConstant.LENGTH_ZIP_PLUS_4);

	}

	// defect 7885
	//	/**
	//	 * Override this method and return true if your JComponent manages 
	//	 * focus. If your component manages focus, the focus manager will 
	//	 * handle your component's children. All key event will be sent to 
	//	 * your key listener including TAB and SHIFT+TAB. CONTROL + TAB and 
	//	 * CONTROL + SHIFT + TAB will move the focus to the next / previous 
	//	 * component.
	//	 */
	//	public boolean isManagingFocus()
	//	{
	//		return cbIsManagingFocus;
	//	}
	// end defect 7885

	// TODO if we are not using keypressed, why define it?
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		// empty code block
	}

	/**
	 * Invoked when a key has been released.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyReleased(java.awt.event.KeyEvent aaKE)
	{
		// empty code block
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyTyped(java.awt.event.KeyEvent aaKE)
	{
		// empty code block
	}

	/**
	 * Sets the input type this RTSInputField will allow
	 * <br>Constants should be used to pass in this value 
	 * (e.g. RTSInputField.ALPHA_ONLY)
	 * 
	 * @param aiInputType int the input type
	 */
	public void setInput(int aiInputType)
	{
		ciTypeLimited = aiInputType;
		setDocument(createDefaultModel());
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
	 * Sets the maximum number of characters allowed in this RTSInputField
	 * 
	 * @param aiMaxLength int the maximum number of characters
	 */
	public void setMaxLength(int aiMaxLength)
	{
		ciLength = aiMaxLength;
		setDocument(createDefaultModel());
	}
}
