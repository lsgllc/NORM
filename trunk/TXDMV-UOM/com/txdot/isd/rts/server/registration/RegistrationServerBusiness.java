package com.txdot.isd.rts.server.registration;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * RegistrationServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify processData()
 * 							Ver 5.2.0
 * K Harrell	05/02/2005	Rename INV014 to INV003
 * 							modify processData()  
 * 							defect 6966 Ver 5.2.3 	 
 * ---------------------------------------------------------------------
 */
/**
 * The RegistrationServerBusiness dispatch the incoming request to
 * the function request on the server side for Registration events.  
 * It also returns the result back to the caller
 * 
 * @version	5.2.3		05/02/2005  
 * @author	Nancy Ting
 * <br>Creation Date:	10/18/2001 
 */
public class RegistrationServerBusiness
{
	/**
	 * RegistrationServerBusiness constructor comment.
	 */
	public RegistrationServerBusiness()
	{
		super();
	}
	/**
	 * Dispatches methods according to function id
	 * 
	 * @param  aiModule String
	 * @param  aiFunctionId int
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			//Subcontractor redirect
			case RegistrationConstant.GET_SUBCON_ALLOCATED_INV :
			case RegistrationConstant.GET_NEXT_INV :
			case RegistrationConstant.CANCEL_SUBCON :
			case RegistrationConstant.CANCEL_HELD_SUBCON :
			case RegistrationConstant
				.DEL_SELECTED_SUBCON_RENWL_RECORD :
			case RegistrationConstant.VALIDATE_PLT :
			case RegistrationConstant.VALIDATE_STKR :
				// PCR 34
			case RegistrationConstant.PROCESS_SUBCON_RENWL :
				// defect 6966 
			case RegistrationConstant.RELEASE_INV003_ITM :
				// end defect 6966 
			case RegistrationConstant.CHECK_DISK_INVENTORY :
				// End PCR 34
				SubcontractorRenewalServerBusiness lSubcontractorRenewalServerBusiness =
					new SubcontractorRenewalServerBusiness();
				return lSubcontractorRenewalServerBusiness.processData(
					aiModule,
					aiFunctionId,
					aaData);
		}
		return null;

	}
}
