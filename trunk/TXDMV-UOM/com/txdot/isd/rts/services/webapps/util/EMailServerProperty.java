package com.txdot.isd.rts.services.webapps.util;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Insert the type's description here.
 * Creation date: (1/28/02 9:54:16 AM)
 * @author: George Donoso
 */
 
public class EMailServerProperty 
{
	//name of pop3 email server
	private static String sSMTPServer;

	//specify file name and location
	private static final String FILE_SRC = "txdotrts/email.properties";

	private static Properties pInfo = new Properties();
/**
 * POP3ServerProperty constructor comment.
 */
public EMailServerProperty() 
{
	super();
}
public static String getSMTPServer()
{
	return sSMTPServer;
}
private static void init()
{
	try{

		//read input file	
		FileInputStream is = new FileInputStream(FILE_SRC);
		pInfo.load(is);

		setProperties();
		is.close();
	}
	catch (Exception e) 
	{
		e.printStackTrace();
 	}		
}
private static void setProperties()
{
	try{

		//set values
		sSMTPServer = pInfo.getProperty("mail.smtp.host");

	}
	catch (Exception e) 
	{
		e.printStackTrace();
 	}		
}
}
