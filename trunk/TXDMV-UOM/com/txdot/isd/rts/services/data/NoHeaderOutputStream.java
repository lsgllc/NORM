package com.txdot.isd.rts.services.data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;

/*
 *
 * NoHeaderOutputStream.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	02/18/2005	Move to services.data.
 * 							Source cleanup
 * 							defect 7705 Ver 5.2.3
 * K Harrell	04/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */

/**
 * NoHeaderOutputStream extends
 * ObjectOutputStream by overriding the writeStreamHeader() method.
 * This object is used by Transaction.cacheIO() to append serialized
 * objects to a file, without breaking the ability to read the
 * serialized object with ObjectInputStream.<p>
 * This <a href="http://forum.java.sun.com/thread.jsp?thread=97646&forum=54&message=914769">
 * link </a> gives a plausible use for the override.<br>
 * <i>Comments by Steven Haskett</i>
 *
 * @version	5.2.3		04/13/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	10/23/2001 13:30:25 
 */

public class NoHeaderOutputStream extends ObjectOutputStream
{
	/**
	 * NoHeaderOutputStream constructor comment.
	 * 
	 * @param aaOut OutputStream
	 * @throws IOException 
	 */
	public NoHeaderOutputStream(OutputStream aaOut) throws IOException
	{
		super(aaOut);
	}
	/**
	 * Overrides super's writeStreamHeader. Does not call super
	 * (which would write the STREAM_MAGIC and STREAM_VERSION),
	 * but rather writes TC_RESET (x79).
	 * 
	 * @throws IOException
	 */
	protected void writeStreamHeader() throws IOException
	{
		write(ObjectStreamConstants.TC_RESET);
	}
}
