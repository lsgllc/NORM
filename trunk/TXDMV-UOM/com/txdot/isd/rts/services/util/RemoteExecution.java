package com.txdot.isd.rts.services.util;

import java.io.*;
import com.oroinc.net.bsd.*;

/**
 * Insert the type's description here.
 * Creation date: (6/13/02 8:41:37 PM)
 * @author: Administrator
 */
public class RemoteExecution {
	private String hostname;
	private String userid;
	private String password;
/**
 * RemoteExecition constructor comment.
 */
public RemoteExecution(String hostname, String userid, String password) {
	super();
	this.hostname = hostname;
	this.userid = userid;
	this.password = password;
}
/**
 * Insert the method's description here.
 * Creation date: (6/13/02 8:45:04 PM)
 * @return boolean
 * @param program java.lang.String
 */
public String executeProgram(String program) 
{
    RExecClient client = new RExecClient();
	String result = null;
    
    try {
      client.connect(hostname);
    } catch(IOException e) {
      return result;
    }

    try {
      client.rexec(userid, password, program);
    } catch(IOException e) {
      try {
		client.disconnect();
      } catch(IOException f) {
      }
      return result;
    }


    // Parse output returned to see if successful
	result = getRexecOutput(client);

    
    try {
      client.disconnect();
    } catch(IOException e) {
    }
	
	return result;
}
/**
 * Insert the method's description here.
 * Creation date: (6/14/02 9:51:54 AM)
 * @return java.lang.String
 */
private String getRexecOutput(RExecClient client) {
	String result = null;

	try
	{

	InputStream ios = client.getInputStream();
	InputStreamReader isr = new InputStreamReader(ios);
	BufferedReader br = new BufferedReader(isr);
	String temp;

		
	while ((temp = br.readLine()) != null)
	{
		result = temp;
		System.out.println(result);
	}

	br.close();
	isr.close();
	ios.close();

	
	} catch (IOException ioex)
	{
		ioex.printStackTrace();
		return null;
	}


	return result;
}
/**
 * Insert the method's description here.
 * Creation date: (6/14/02 9:58:28 AM)
 * @param args java.lang.String[]
 */
public static void main(String[] args) 
{
	//  args[0] = hostname
	//  args[1] = userid
	//  args[2] = password
	//  args[3] = program string
	
	RemoteExecution rexec = new RemoteExecution(args[0],
												args[1],
												args[2]);
												
	String result = rexec.executeProgram(args[3]);


	if ( (result == null) || !(result.trim().equalsIgnoreCase("exit: 0")) )
		System.out.println("Error executing script.");
	else
		System.out.println("Script executed successfully.");
	

	
}
}
