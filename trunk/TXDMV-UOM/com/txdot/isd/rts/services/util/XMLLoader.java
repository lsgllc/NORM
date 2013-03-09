package com.txdot.isd.rts.services.util;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.xml.XMLSecurity;
/*
 *
 * XMLLoader.java
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
 * This class provides the methods for loading the XML values into a
 * data class that uses the interface XMLDataInterface.
 * 
 * @version	5.2.4			08/08/2006
 * @author	Jeff Seifert
 * <p>Creation Date:		03/05/2006
 */
public class XMLLoader
{
	private static final String ERROR_MSG = 
		"Error loading/decrypting the XML file.";
	private static final boolean DECRYPT_XML = true;
	
	/**
	 * Method that decrypts the XML file an loops through the elements
	 * that match the search field.
	 * 
	 * @param asFileName String
	 * @param aaXMLData XMLDataInterface
	 * @param asSearchField String
	 * @return java.util.Vector
	 * @throws RTSException
	 */
	public synchronized static Vector load(
		String asFileName,
		XMLDataInterface aaXMLData,
		String asSearchField)
		throws RTSException
	{
		Vector lvVct = new Vector();
		try
		{
			Document laDoc;
			if (DECRYPT_XML)
			{
				laDoc = XMLSecurity.loadAndDecryptFile(asFileName);
			}
			else
			{
				laDoc = XMLSecurity.parseXMLFile(asFileName);
			}
			
			NodeList laNodes =
				laDoc.getElementsByTagName(asSearchField);
			for (int i = 0; i < laNodes.getLength(); i++)
			{
				XMLDataInterface laRecord =
					(XMLDataInterface) UtilityMethods.copy(
						aaXMLData);
				laRecord.setFields(laNodes.item(i));
				lvVct.add(laRecord);
			}
		}
		catch (Exception aeEx)
		{
			RTSException laRTSEx = 
					new RTSException(RTSException.JAVA_ERROR, aeEx);
			laRTSEx.setMessage(ERROR_MSG);
			throw laRTSEx;
		}

		return lvVct;
	}
}