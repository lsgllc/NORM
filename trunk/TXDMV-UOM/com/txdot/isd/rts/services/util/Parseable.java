package com.txdot.isd.rts.services.util;

/**
 *
 * Parseable.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  Imported new class.
 * 							Vern 5.2.0
 * J Rue		05/21/2008	Add new interface methods
 * 							add setRecType, setField(int, Str, int)
 * 							defect 9656 Ver Defect_POS_A			  
 * ---------------------------------------------------------------------
 */
/**
 * This class provides the methods for 
 * 
 * @version	Defect_POS_A	05/21/2008 
 * @author	Michael Abernethy
 * <p>Creation Date:		08/06/2002
 */
public interface Parseable
{
	// defect 9656
	//	No longer reference
	/**
	 * Insert the method's description here.
	 * @return java.lang.String
	 * @param aiFieldNum int
	 */
	 String getField(int aiFieldNum);
	/**
	 * Insert the method's description here.
	 * @param aiFieldNum int
	 * @param asValue java.lang.String
	 */
		void setField(int aiFieldNum, String asValue);
	// end defect 9656
	/**
	 * setField() for post MF Version U.
	 * 
	 * @param asIndex int
	 * @param asData java.lang.String
	 * @param asFileType java.lang.String
	 */
	void setField(int asIndex, String asData, int asFileType);
	// defect 9656
	//	Interface to set Record Type from DTA diskette
	/**
	 * setField() for post MF Version U.
	 * 
	 * @param aiRectype int
	 */
	void setRecType(int aiRectype);
	// end defect 9656
}
