package com.txdot.isd.rts.server.cris;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * TestClientRequestor.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	06/18/2004	Initial writing.
 * Bob Brown    12/17/2004  Modify TestClientRequestor(String)
 *                          Added a check for conn.getHeaderField 
 *                          containing "not found".
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * TestClientRequestor is the class used for testing the head request
 *
 * The client is required to pass in license plate number, username, and 
 * password, via a HEAD request.
 * The CrisMfAccess.getMFRec method returns the DPS required fields to the requestor.
 *
 * @version	5.2.3		05/02/2005
 * @author: Bob Brown
 * <p>Creation Date: 	06/18/2004 04:20:00
 */

public class TestClientRequestor
{

	/**
	 * Test Client Requestor
	 */
	public TestClientRequestor()
	{
		super();
	}
	/**
	 * Test Client Requestor
	 * 
	 * @param String asURL
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public TestClientRequestor(String asURL)
		throws MalformedURLException, IOException
	{
		URL laURL = new URL(asURL);

		//String[] testPlates	= {"2NVF49","729U3W","BBP83N","BHR03T","AYUH","A01359","C16HJN","C23TWM","AA0ES","AA5A","YGC35D","YGL81R"};
		//String[] testPlates	= {"2NVF49","729U3W"};

		//for (int x=0; x<testPlates.length; x++)
		//{

		try
		{

			HttpURLConnection laConn =
				(HttpURLConnection) laURL.openConnection();

			// set headers, for example: user-agent
			laConn.setRequestProperty(
				"User-Agent",
				"Java Application (RTS v5.0.0)");
			laConn.setRequestProperty(
				"Accept",
				"image/gif, image/x-xbitmap,image/jpeg, image/pjpeg, application/vnd.ms-powerpoint,application/vnd.ms-excel, application/msword, application/x-comet,*/*");
			laConn.setRequestProperty("Content-Type", "text/html");
			//conn.setRequestProperty("Content-Length", "" + postParams.length());
			laConn.setRequestMethod("HEAD");
			//conn.setRequestProperty("REG_PLATE_NO","YGC35D");

			//the plate 707U3X conatins more than 1 active MF record
			//conn.setRequestProperty("REG_PLATE_NO","707U3X");
			laConn.setRequestProperty("REG_PLATE_NO", "XXXBOB");
			laConn.setRequestProperty("USERNAME", "Cris601");
			laConn.setRequestProperty("PASSWORD", "mf73767");
			//conn.setRequestProperty("REG_PLATE_NO",testPlates[x]);
			laConn.setDoOutput(true);
			laConn.setUseCaches(false);

			// since the request method is Head, there is no data to post to output
			/*
			OutputStream out = conn.getOutputStream();
			out.write(postParams.getBytes());
			out.flush();
			out.close();
			 */

			try
			{
				laConn.getInputStream();
				/*
				The following code is for load testing the header data returned
				for (int x=0;x<10;x++)
				{
					StringBuffer returnString = new StringBuffer();
					//returnString.append(conn.getResponseMessage());
						returnString.append(conn.getHeaderField("Status"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Vin"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Model"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Year"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Make"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("VehClass"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("BodyType"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("GrossWeight"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("OwnerName1"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("OwnerName2"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Street1"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Street2"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("City"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("Country"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("State"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("ZipCode/first5"+Integer.toString(x)));
						returnString.append(conn.getHeaderField("ZipCode/last4"+Integer.toString(x)));
					}
					
						returnString.append(conn.getHeaderField("Status"));
						returnString.append(conn.getHeaderField("Vin"));
						returnString.append(conn.getHeaderField("Model"));
						returnString.append(conn.getHeaderField("Year"));
						returnString.append(conn.getHeaderField("Make"));
						returnString.append(conn.getHeaderField("VehClass"));
						returnString.append(conn.getHeaderField("BodyType"));
						returnString.append(conn.getHeaderField("GrossWeight"));
						returnString.append(conn.getHeaderField("OwnerName1"));
						returnString.append(conn.getHeaderField("OwnerName2"));
						returnString.append(conn.getHeaderField("Street1"));
						returnString.append(conn.getHeaderField("Street2"));
						returnString.append(conn.getHeaderField("City"));
						returnString.append(conn.getHeaderField("Country"));
						returnString.append(conn.getHeaderField("State"));
						returnString.append(conn.getHeaderField("ZipCode/first5"));
						returnString.append(conn.getHeaderField("ZipCode/last4"));
				
						System.out.println(returnString);
						*/

				//System.out.println("Plate no= " + testPlates[x]);

				if ((laConn
					.getHeaderField("Status")
					.indexOf("Found more than 1")
					> -1)
					|| // 12/17/2004 change
				 (
						laConn.getHeaderField("Status").indexOf(
							"not found")
							> -1))
					// end 12/17/2004 change
				{
					System.out.println(
						"Status= " + laConn.getHeaderField("Status"));
				}
				else
				{
					System.out.println(
						"Plate= " + laConn.getHeaderField("Plate"));
					System.out.println(
						"Status= " + laConn.getHeaderField("Status"));
					System.out.println(
						"Vin= " + laConn.getHeaderField("Vin"));
					System.out.println(
						"Model= " + laConn.getHeaderField("Model"));
					System.out.println(
						"Year= " + laConn.getHeaderField("Year"));
					System.out.println(
						"Make= " + laConn.getHeaderField("Make"));
					System.out.println(
						"VehClass= " + laConn.getHeaderField("VehClass"));
					System.out.println(
						"BodyType= " + laConn.getHeaderField("BodyType"));
					System.out.println(
						"GrossWeight= "
							+ laConn.getHeaderField("GrossWeight"));
					/*
					System.out.println("OwnerName1= " + conn.getHeaderField("OwnerName1"));
					System.out.println("OwnerName2= " + conn.getHeaderField("OwnerName2"));
					
					System.out.println("RecipientName= " + conn.getHeaderField("RecipientName"));
					*/
					System.out.println(
						"Name1= " + laConn.getHeaderField("Name1"));
					System.out.println(
						"Name2= " + laConn.getHeaderField("Name2"));

					System.out.println(
						"Street1= " + laConn.getHeaderField("Street1"));
					System.out.println(
						"Street2= " + laConn.getHeaderField("Street2"));
					System.out.println(
						"City= " + laConn.getHeaderField("City"));
					System.out.println(
						"Country= " + laConn.getHeaderField("Country"));
					System.out.println(
						"State= " + laConn.getHeaderField("State"));
					System.out.println(
						"ZipCode/first5= "
							+ laConn.getHeaderField("ZipCode/first5"));
					System.out.println(
						"ZipCode/last4= "
							+ laConn.getHeaderField("ZipCode/last4"));
					System.out.println("****** end of record *******");
					/*
					System.out.println("Recipient_Street1= " + conn.getHeaderField("Recipient_Street1"));
					System.out.println("Recipient_Street2= " + conn.getHeaderField("Recipient_Street2"));
					System.out.println("Recipient_City= " + conn.getHeaderField("Recipient_City"));
					System.out.println("Recipient_Country= " + conn.getHeaderField("Recipient_Country"));
					System.out.println("Recipient_State= " + conn.getHeaderField("Recipient_State"));
					System.out.println("Recipient_ZipCode/first5= " + conn.getHeaderField("Recipient_ZipCode/first5"));
					System.out.println("Recipient_ZipCode/last4= " +conn.getHeaderField("Recipient_ZipCode/last4"));
					*/
				}

				laConn.disconnect();

			}

			catch (Exception leEx)
			{
				leEx.printStackTrace();
			}

		}

		catch (IOException leIOEx)
		{
			System.err.println(
				"CrisServlet error "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).getTime()
					+ " Exception occurred in main() of com.txdot." +						"isd.rts.server.cris.TestClientRequestor"
					+ "==============");

			leIOEx.printStackTrace(System.out);
		}

		//}

	}
	public static void main(String[] args)
	{

		String strURL =
			"http://localhost:8080/servlet/com.txdot.isd.rts.server.cris.CrisServlet";
		//String strURL="http://tx-rts-ts1:8081/servlet/com.txdot.isd.rts.server.cris.CrisServlet";
		//String strURL="http://tx-rts-appl/servlet/com.txdot.isd.rts.server.cris.CrisServlet";

		try
		{
			TestClientRequestor laTCR = new TestClientRequestor(strURL);
		}
		catch (Exception leEx)
		{
			System.err.println(strURL + ":");
			leEx.printStackTrace(System.err);
		}
	}
}
