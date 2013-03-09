package com.txdot.isd.rts.services.util;

import org.w3c.dom.Node;

/*
 * XMLDataInterface.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		08/08/2006	Created Class.
 * 							defect 8451 Ver 5.2.4
 * ---------------------------------------------------------------------
 */
 
/**
 * This class provides the methods used by XMLLoader class to load
 * a data class with data from an XML document.
 * 
 * @version	5.2.4			08/08/2006
 * @author	Jeff Seifert
 * <p>Creation Date:		03/06/2006
 */
public interface XMLDataInterface
{
	/**
	 * Used to load the data class that uses this interface with data 
	 * from an XML file.
	 * 
	 * @param aaNode Node
	 */
	void setFields(Node aaNode);
}
