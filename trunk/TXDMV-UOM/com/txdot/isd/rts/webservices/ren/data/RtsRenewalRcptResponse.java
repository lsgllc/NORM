package com.txdot.isd.rts.webservices.ren.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsRenewalRcptResponse.java
 *
 * (c) Texas Department of Motor Vehicle 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/10/2010	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	12/30/2010	Add the Receipt Lines array.
 * 							We need to break up the lines so that they 
 * 							form a page.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	12/31/2010	Add the Receipt Elements array.
 * 							We are now getting deeper into hoe to format
 * 							the receipt correctly for Web Agent.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/07/2011	Changed up to only show the url string to 
 * 							pick up the receipt.
 * 							defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Receipt Response for Web Agent.
 *
 * @version	6.7.0			02/07/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		12/10/2010 10:00:42
 */

public class RtsRenewalRcptResponse extends RtsAbstractResponse
{
	private String csReceiptUrl;
	
	/**
	 * Get the Receipt URL String.
	 * 
	 * @return String
	 */
	public String getReceiptUrl()
	{
		return csReceiptUrl;
	}

	/**
	 * Set the Receipt URL String.
	 * 
	 * @param asReceiptUrl
	 */
	public void setReceiptUrl(String asReceiptUrl)
	{
		csReceiptUrl = asReceiptUrl;
	}
}
