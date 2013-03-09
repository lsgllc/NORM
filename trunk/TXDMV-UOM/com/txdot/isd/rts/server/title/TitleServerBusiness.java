package com.txdot.isd.rts.server.title;

import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.PresumptiveValueData;
import com.txdot.isd.rts.services.data.TitleInProcessData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

import com.txdot.isd.rts.server.common.business.VehicleInquiry;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.title.blackbook.BlackBookClient;

/*
 * TitleServerBusiness.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/03/2003	Fixed defect 4588
 *							Put ClientHost into MFAccess.
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/06/2005	deprecate getDealerData(),getLienHolderData()
 * 							modify processData()
 * 							defect 8283 Ver 5.2.3                    
 * T Pederson	09/08/2006	Added case for presumptive value to 
 * 							processData() and added method to get 
 * 							private party value
 * 							add getPrivatePartyValue()
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	07/03/2009	delete getDealerData(), getLienholderData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/04/2010	modify getTitleInProcess() 
 * 							defect 10598 Ver 6.6.0 
 *----------------------------------------------------------------------
 */

/**
 * Title Server Business 
 *  
 * @version	6.6.0 			10/04/2010
 * @author	Ashish Mahajan
 * <br>Creation Date:		09/24/2001 17:25:15
 */
public class TitleServerBusiness
{
	private String csClientHost = "Unknown";

	/**
	 * TitleServerBusiness constructor comment.
	 */
	public TitleServerBusiness()
	{
		super();
	}

	/**
	 * TitleServerBusiness constructor comment.
	 * 
	 * @param String asClientHost
	 */
	public TitleServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Get Number of Records from DocNo
	 *  
	 * @param aaData Object
	 * @return Object  
	 * @throws RTSException
	 */
	private Object getNumDocRecords(Object aaData) throws RTSException
	{
		MfAccess laMFA = new MfAccess(csClientHost);
		//Test for  NUMBER OF RECORDS from DocNo
		GeneralSearchData laGenSearchData = (GeneralSearchData) aaData;

		int i = laMFA.voidTransactions(laGenSearchData);

		return new Integer(i);
	}

	/**
	  * Make call to blackbook client to get presumptive value
	  * 
	  * @param aaData Object
	  * @return Object
	  */
	private Object getPrivatePartyValue(Object aaData)
		throws RTSException
	{
		BlackBookClient laClient =
			new BlackBookClient((PresumptiveValueData) aaData);
		return laClient.getPrivatePartyValue();
	}

	/**
	 * Get Title in Process
	 * 
	 * @param aaData Object
	 * @return TitleInProcessData  
	 * @throws RTSException
	 */
	private TitleInProcessData getTitleInProcess(Object aaData)
		throws RTSException
	{
		MfAccess laMFA = new MfAccess(csClientHost);

		// defect 10598		
		// Get DocNo from GSD, use Key2() to be consistent w/ getVeh() 
		GeneralSearchData laGSD = (GeneralSearchData) aaData;
	
		String lsDocNo = laGSD.getKey2();
		
		TitleInProcessData laTIPData =
			laMFA.retrieveTitleInProcess(lsDocNo);

		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		Vector lvTotalInProcsTrans = new Vector();

		VehicleInquiry laVehInq = new VehicleInquiry();
		laVehInq.updtInProcsTrans(laVehInqData, laGSD);
		
		if (laVehInqData.hasInProcsTrans())
		{
			Vector lvInProcs = laVehInqData.getInProcsTransDataList();

			for (int i = 0; i < lvInProcs.size(); i++)
			{
				lvTotalInProcsTrans.addElement(lvInProcs.elementAt(i));
			}
		}

		if (laTIPData.getVIN() != null)
		{
			laGSD.setKey1(CommonConstant.VIN);
			laGSD.setKey2(laTIPData.getVIN());
			Vector lvInProcs = laVehInqData.getInProcsTransDataList();

			for (int i = 0; i < lvInProcs.size(); i++)
			{
				if (!lvTotalInProcsTrans
					.contains(lvInProcs.elementAt(i)))
				{
					lvTotalInProcsTrans.addElement(
						lvInProcs.elementAt(i));
				}
			}
		}

		laTIPData.setInProcsTransDataList(lvTotalInProcsTrans);
		// end defect 10598  

		return laTIPData;
	}

	/**
	 * Process Data
	 * 
	 * @return Object
	 * @param aiFunctionId int
	 * @param aaData Object 
	 * @throws RTSException
	 */
	public Object processData(int aiFunctionId, Object aaData)
		throws RTSException
	{

		switch (aiFunctionId)
		{

			case TitleConstant.GET_NUM_DOC_RECORD :
				{
					return getNumDocRecords(aaData);
				}

			case TitleConstant.DELETE_TITLE_IN_PROCESS :
				{
					return getTitleInProcess(aaData);
				}

				// defect 8926 
			case TitleConstant.GET_PRIVATE_PARTY_VALUE :
				{
					return getPrivatePartyValue(aaData);
				}
				// end defect 8926 
		}
		return null;
	}
}
