package com.txdot.isd.rts.client.desktop;

import com.txdot.isd.rts.services.util.*;

/*
 * RadioMenuActionListener.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							Format comments, Hungarian notation for 
 * 							variables. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * An ActionListener for the radio menu items in the RTSDesktop
 * 
 * @version	5.2.3		04/26/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	10/11/2001 9:15:06
 */
public class RadioMenuActionListener
	implements java.awt.event.ActionListener
{
	private RTSDeskTop caDesktop;
	public final static java.lang.String PRINT_IMMEDIATE_ON = "ON";
	public final static java.lang.String PRINT_IMMEDIATE_OFF = "OFF";
	public final static java.lang.String NEXT_CUSTOMER = "NEXT";
	/**
	 * Creates a RadioMenuActionListener with an graph of the
	 * caDesktop
	 * 
	 * @param RTSDeskTop aaDesktop
	 */
	public RadioMenuActionListener(RTSDeskTop aaDesktop)
	{
		super();
		this.caDesktop = aaDesktop;
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (aaAE.getActionCommand().equals(PRINT_IMMEDIATE_OFF))
		{
			caDesktop.setPrintImmediate(RTSDeskTop.PRINT_IMMEDIATE_OFF);
			SystemProperty.setPrintImmediateIndi(0);
			try
			{
				SystemProperty.updatePrintImmediateProperty("N");
			}
			catch (com.txdot.isd.rts.services.exception.RTSException
					 leRTSEx)
			{
				// empty code block
			}
		}
		else if (aaAE.getActionCommand().equals(PRINT_IMMEDIATE_ON))
		{
			caDesktop.setPrintImmediate(RTSDeskTop.PRINT_IMMEDIATE_ON);
			SystemProperty.setPrintImmediateIndi(1);
			try
			{
				SystemProperty.updatePrintImmediateProperty("Y");
			}
			catch (com.txdot.isd.rts.services.exception.RTSException 
					leRTSEx)
			{
				// empty code block
			}
		}
		else if (aaAE.getActionCommand().equals(NEXT_CUSTOMER))
		{
			// already on Next Customer - deselect it
			if (SystemProperty.getPrintImmediateIndi() == 2)
			{
				caDesktop.setPrintImmediate(
					RTSDeskTop.PRINT_IMMEDIATE_OFF);
				SystemProperty.setPrintImmediateIndi(0);
				try
				{
					SystemProperty.updatePrintImmediateProperty("N");
				}
				catch (com.txdot.isd.rts.services.exception.RTSException
						leRTSEx)
				{
					// empty code block
				}
			}
			// not on yet, select it
			else
			{
				caDesktop.setPrintImmediate(
					RTSDeskTop.PRINT_IMMEDIATE_NEXT);
				SystemProperty.setPrintImmediateIndi(2);
				try
				{
					SystemProperty.updatePrintImmediateProperty("N");
				}
				catch (com.txdot.isd.rts.services.exception.RTSException
						leRTSEx)
				{
					// empty code block
				}
			}

		}
		else if (aaAE.getActionCommand().equals(
				com.txdot.isd.rts.services.util.constants.
				ScreenConstant.SEC020 + RTSDeskTop.DELIM +
					com.txdot.isd.rts.services.util.constants.
					TransCdConstant.SP_DIS))
		{
			caDesktop.setServerPlusEnabled(false);
		}
		else if (aaAE.getActionCommand().equals(
				com.txdot.isd.rts.services.util.constants.
					ScreenConstant.SEC020 + RTSDeskTop.DELIM +
					com.txdot.isd.rts.services.util.constants.
					TransCdConstant.SP_ENA))
		{
			caDesktop.setServerPlusEnabled(true);
		}
	}
}
