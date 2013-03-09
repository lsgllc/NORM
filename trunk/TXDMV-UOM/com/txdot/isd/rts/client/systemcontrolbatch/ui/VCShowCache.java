package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * VCShowCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name        	Date        Description
 * Min Wang		11/11/2002	Fixed CQU100004746. Modified getValueAt() 
 * 							to add Void as a valid transaction type.
 * Ray Rowehl	03/28/2005	Fixed a few execution problems
 * 							defect 7897 Ver 5.2.3
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				Changed setVisible to setVisibleRTS
 * 							modify processData(), setMediator()
 *							defect 7897 ver 5.2.3
 *  --------------------------------------------------------------------
 */
/**
 * The VC for the Show Cache
 * 
 * @version	5.2.3			07/08/2005
 * @author	Michael Abernethy
 * <br>Creation Date: 		09/20/2001 10:34:08
 */
public class VCShowCache extends AbstractViewController
{
	private ShowCache caMediator;
	/**
	 * VCShowCache constructor.
	 */
	public VCShowCache()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * processData
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 *	with the VC
	 * @param aaData Object - the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER:
			{
				FrmDetailCache laDetailCache = new FrmDetailCache();
				laDetailCache.setData(aaData);
				// defect 7897
				// Changed setVisible to setVisibleRTS
				//laDetailCache.setVisible(true);
				laDetailCache.setVisibleRTS(true);
				// end defect 7897
				break;
			}
			case CANCEL:
			{
				getFrame().dispose();
				System.exit(0);
				break;
			}
		}
	}
	/**
	 * setMediator
	 * 
	 * @param aaMediator ShowCache
	 */
	public void setMediator(ShowCache aaMediator)
	{
		caMediator = aaMediator;
	}
	/**
	 * setView
	 * 
	 * @param aaData Object
	 */
	public void setView(Object aaData)
	{
		if (getFrame() == null)
		{
			setFrame(new FrmShowCache());
		}
		FrmShowCache laCacheFrame = (FrmShowCache) getFrame();
		laCacheFrame.setController(this);
		laCacheFrame.setData(aaData);
		// defect 7897
		// Changed setVisible to setVisibleRTS
		//laCacheFrame.setVisible(true);
		laCacheFrame.setVisibleRTS(true);
		// end defect 7897
	}
}
