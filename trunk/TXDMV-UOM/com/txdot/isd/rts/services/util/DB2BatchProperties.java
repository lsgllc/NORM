package com.txdot.isd.rts.services.util;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

/*
 *
 * DB2BatchProperties.java
 *
 * (c) Texas Department of Transportation 2003
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/27/2005	Code cleanup
 * 							JavaDoc standards, rename fields
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	09/27/2006	Modify to work with SendTrans.  Need to load
 * 							the properties file as a resourcestream
 * 							so the class path will be searched.
 * 							Note that the save is still relative to
 * 							class position.
 * 							modify readProperties()
 * 							defect 8553 Ver FallAdminTables
 * Ray Rowehl	10/19/2007	Check to see if the file input stream is 
 * 							null before attempting to use.
 * 							Print a message indicating an exception 
 * 							happened.
 * 							modify readProperties()
 * 							defect 9381 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Allows DBA to edit DB2 Batch Properties.
 * Also used to help load DB2 Batch Properties for DB2 Batch.
 *
 * @version	Special Plates	10/19/2007
 * @author	Richard Hicks
 * <br>Creation Date:		06/17/2002 11:34:20
 */

public class DB2BatchProperties implements ActionListener
{
	/**  
	 * Shift value used by encryption / decryption algortihms 
	 */
	public final static int SHIFT_VALUE = 31;

	private static final String BUTTON_LABEL_EXIT = "Exit";
	private static final String BUTTON_LABEL_SAVE = "Save";
	private static final String FRAME_NAME = "DB2 Batch Properties";
	private static final String MSG_ERROR_PREFIX = "Error: ";
	private static final String MSG_PROP_SAVED =
		"Properties file has been saved.";

	public final static String DB2_HOSTNAME = "db2.hostname";
	public final static String DB2_USERID = "db2.userid";
	public final static String DB2_PASSWORD = "db2.password";
	public final static String DB2_SCRIPT1 = "db2.script1";
	public final static String DB2_SCRIPT2 = "db2.script2";
	private final static String FILENAME = "cfg/DB2Batch.properties";

	private JFrame caFrame = null;
	private JTextArea caJTArea = null;
	private JButton caSaveButton = null;
	private JButton caExitButton = null;

	private java.lang.String csHostname;
	private java.lang.String csUserid;
	private java.lang.String csPassword;
	private java.lang.String csDb2Script1;
	private java.lang.String csDb2Script2;

	/**
	 * DB2BatchProperties constructor comment.
	 */
	public DB2BatchProperties()
	{
		super();
	}
	
	/**
	 * Perform action requests
	 * 
	 * @param aaAE - ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (aaAE.getSource() == caSaveButton)
		{
			String laRC = saveProperties();
			JOptionPane.showMessageDialog(caFrame, laRC);
		}
		else if (aaAE.getSource() == caExitButton)
		{
			System.exit(0);
		}
	}
	
	/**
	 * Encrypt the string.
	 * 
	 * @return int
	 * @param asPassword - String
	 */
	private int[] encrypt(String asPassword)
	{
		char[] larrPWChars = new char[asPassword.length()];
		asPassword.getChars(0, asPassword.length(), larrPWChars, 0);

		int[] larrInts = new int[larrPWChars.length];

		for (int i = 0; i < larrPWChars.length; i++)
		{
			larrInts[i] = ((int) larrPWChars[i]) + SHIFT_VALUE;
		}

		return larrInts;
	}
	
	/**
	 * Return the first DB2 Script file name.
	 * 
	 * @return String
	 */
	public java.lang.String getDb2Script1()
	{
		return csDb2Script1;
	}
	
	/**
	 * Return the second DB2 Script file name.
	 *  
	 * @return String
	 */
	public java.lang.String getDb2Script2()
	{
		return csDb2Script2;
	}
	
	/**
	 * Return the HostName.
	 * 
	 * @return String
	 */
	public String getHostname()
	{
		return csHostname;
	}
	
	/**
	 * Return the DBA Script password.
	 * 
	 * @return String
	 */
	public String getPassword()
	{
		return csPassword;
	}
	
	/**
	 * Return UserId.
	 *  
	 * @return String
	 */
	public String getUserid()
	{
		return csUserid;
	}
	
	/**
	 * Initialize the frame
	 */
	private void init()
	{
		caSaveButton = new JButton(BUTTON_LABEL_SAVE);
		caExitButton = new JButton(BUTTON_LABEL_EXIT);

		caSaveButton.addActionListener(this);
		caExitButton.addActionListener(this);
	}
	
	/**
	 * Launch the frame.
	 * 
	 * @param aarrArgs - String[]
	 */
	public final static void main(String[] aarrArgs)
	{
		DB2BatchProperties laDbProp = new DB2BatchProperties();
		laDbProp.init();
		laDbProp.caFrame = new JFrame(FRAME_NAME);
		laDbProp.caFrame.getContentPane().setLayout(new BorderLayout());
		laDbProp.caJTArea = new JTextArea();
		JScrollPane laScroll = new JScrollPane();
		laScroll.setViewportView(laDbProp.caJTArea);
		laScroll.setVerticalScrollBarPolicy(
			javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		laScroll.setHorizontalScrollBarPolicy(
			javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		laDbProp.caJTArea.setText(laDbProp.readProperties());
		laDbProp.setProperties();
		JPanel laButtons = new JPanel();
		laButtons.setLayout(new FlowLayout());
		laButtons.add(laDbProp.caSaveButton);
		laButtons.add(laDbProp.caExitButton);
		laDbProp.caFrame.getContentPane().add(
			BorderLayout.CENTER,
			laScroll);
		laDbProp.caFrame.getContentPane().add(
			BorderLayout.SOUTH,
			laButtons);
		laDbProp.caFrame.setSize(600, 400);
		laDbProp.caFrame.setVisible(true);
	}
	
	/**
	 * Reaf the Properties into memory
	 * 
	 * @return String
	 */
	private String readProperties()
	{
		char[] larrResult = null;

		try
		{
			//  Create the FileInputStream to open the input file
			InputStream laFIS =
				DB2BatchProperties
					.class
					.getClassLoader()
					.getResourceAsStream(
					FILENAME);
			
			// defect 9381
			// Confirm that we opened the FIS
			if (laFIS == null)
			{
				System.out.println("Got a NullPointer on finding DB2Batch.properties.  Throw it from readProperties.");
				throw new NullPointerException();
			}
			// end defect 9381

			// Wrapped the FileInputStream with a BufferedReader to 
			// allow the reading of completes lines, rather than 
			// individual characters
			DataInputStream laDIS = new DataInputStream(laFIS);

			char[] larrTemp = new char[1024];
			int liIndex = 0;
			while (laDIS.available() > 0)
			{
				larrTemp[liIndex] =
					(char) (laDIS.readInt() - SHIFT_VALUE);
				++liIndex;
			}

			//  Close the input file
			laDIS.close();
			laFIS.close();

			larrResult = new char[liIndex];
			for (int i = 0; i < larrResult.length; i++)
			{
				larrResult[i] = larrTemp[i];
			}
			
			return (new String(larrResult));
		}
		catch (Exception aeEx)
		{
			// defect 9381
			System.out.println("Got an Exception reading in DB2Batch.properties " + aeEx.getClass());
			// end defect 9381
			aeEx.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Persist the Properties back to the file.
	 * 
	 * @return String
	 */
	private String saveProperties()
	{
		String lsMessage = null;

		try
		{
			FileOutputStream laFOS =
				new FileOutputStream(FILENAME, false);

			// Wrapped the FileOutputStream with a BufferedReader to 
			// allow the reading of completes lines, rather than 
			// individual characters
			DataOutputStream laDOS = new DataOutputStream(laFOS);
			int[] larrText = encrypt(caJTArea.getText());
			for (int i = 0; i < larrText.length; i++)
			{
				laDOS.writeInt(larrText[i]);
			}

			//  Close the output file
			laDOS.flush();
			laDOS.close();
			laFOS.close();
			lsMessage = MSG_PROP_SAVED;
		}
		catch (Exception aeEx)
		{
			lsMessage = MSG_ERROR_PREFIX + aeEx.getMessage();
			aeEx.printStackTrace();
		}

		return lsMessage;
	}
	
	/**
	 * Set the value for the first DB2 Script.
	 * 
	 * @param asNewDb2Script1 - String
	 */
	public void setDb2Script1(String asNewDb2Script1)
	{
		csDb2Script1 = asNewDb2Script1;
	}
	
	/**
	 * Set the value for the second DBA Script.
	 * 
	 * @param asNewDb2Script2 - String
	 */
	public void setDb2Script2(String asNewDb2Script2)
	{
		csDb2Script2 = asNewDb2Script2;
	}
	
	/**
	 * Set the value for HostName.
	 * 
	 * @param asNewHostname - String
	 */
	public void setHostname(String asNewHostname)
	{
		csHostname = asNewHostname;
	}
	
	/**
	 * Set the value for the Password.
	 * 
	 * @param asNewPassword - String
	 */
	public void setPassword(String asNewPassword)
	{
		csPassword = asNewPassword;
	}
	
	/**
	 * Set the Properties.
	 */
	public void setProperties()
	{
		String lsProperties = readProperties();
		StringTokenizer laST =
			new StringTokenizer(lsProperties, " =\t\n\r\f");
		while (laST.hasMoreTokens())
		{
			System.setProperty(laST.nextToken(), laST.nextToken());
		}
	}
	
	/**
	 * Set the value for UserId.
	 * 
	 * @param asNewUserid - String
	 */
	public void setUserid(String asNewUserid)
	{
		csUserid = asNewUserid;
	}
}
