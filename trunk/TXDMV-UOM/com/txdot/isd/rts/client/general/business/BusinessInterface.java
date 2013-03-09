package com.txdot.isd.rts.client.general.business;

import com.txdot.isd.rts.client.accounting.business.AccountingClientBusiness;
import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.funds.business.FundsClientBusiness;
import com.txdot.isd.rts.client.inventory.business.InventoryClientBusiness;
import com.txdot.isd.rts.client.localoptions.business.LocalOptionsClientBusiness;
import com.txdot.isd.rts.client.misc.business.MiscClientBusiness;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.reports.business.ReportsClientBusiness;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientBusiness;
import com.txdot.isd.rts.client.title.business.TitleClientBusiness;
import com.txdot.isd.rts.client.webapps.registrationrenewal.RegRenProcessingClientBusiness;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * BusinessInterface.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	08/18/2005	Code clean-up for Java 1.4. Format,  
 * 							Hungarian notation, etc.  
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		04/02/2007	Added MessagesClientBusiness
 * 							add caMessBusiness
 * 							modify BusinessInterface(), finalize(),
 * 								processData()
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	02/27/2007	Add business method for Special Plates
 * 							add caSpclPltsBusiness
 * 							modify constructor, finalize(), processData()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	08/16/2007	Fix merge problem.
 * 							closing bracket out of place.
 * 							modify BusinessInterface()
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */
 
/**
 * The branch from RTSMediator to the individual Business Modules
 * 
 * @version	Special Plates	08/16/2007
 * @author	Michael Abernethy
 * <br>Creation Date:		09/05/2001 11:04:36
 */

public class BusinessInterface
{
	private final static String ERRMSG_NAME_NOT_FOUND =
		"Module Name Not Found";

	private AccountingClientBusiness caAccountingBusiness;
	private LocalOptionsClientBusiness caLocaloptionsBusiness;
	private CommonClientBusiness caCommonBusiness;
	private InventoryClientBusiness caInventoryBusiness;
	private FundsClientBusiness caFundsBusiness;
	private RegistrationClientBusiness caRegistrationBusiness;
	private ReportsClientBusiness caReportsBusiness;
	private TitleClientBusiness caTitleBusiness;
	private MiscClientBusiness caMiscBusiness;
	private MiscellaneousRegClientBusiness caMiscRegBusiness;
	private RegRenProcessingClientBusiness caProcBusiness;
	// defect 9085 
	private SpecialPlatesClientBusiness caSpclPltsBusiness;
	// end defect 9085 

	// defect 7768
	private com.txdot.isd.rts.client.help.business.
		HelpClientBusiness caHelpBusiness;
	// end defect 7768
	/**
	 * BusinessInterface constructor comment.
	 */
	public BusinessInterface()
	{
		super();
		caAccountingBusiness =
			new AccountingClientBusiness();
		caLocaloptionsBusiness =
			new LocalOptionsClientBusiness();
		caCommonBusiness =
			new CommonClientBusiness();
		caInventoryBusiness =
			new InventoryClientBusiness();
		caFundsBusiness =
			new FundsClientBusiness();
		caRegistrationBusiness =
			new RegistrationClientBusiness();
		caReportsBusiness =
			new ReportsClientBusiness();
		caTitleBusiness =
			new TitleClientBusiness();
		caMiscBusiness =
			new MiscClientBusiness();
		caMiscRegBusiness =
			new MiscellaneousRegClientBusiness();
		caProcBusiness =
			new RegRenProcessingClientBusiness();

		// defect 9085 
		caSpclPltsBusiness =
			new SpecialPlatesClientBusiness();
		// end defect 9085 	

		// defect 7768
		caHelpBusiness = 
			new com.txdot.isd.rts.client.help.business.
				HelpClientBusiness();
		// end defect 7768
	}
	/**
	 * Insert the method's description here.
	 */
	protected final void finalize()
	{
		caAccountingBusiness = null;
		caCommonBusiness = null;
		caFundsBusiness = null;
		caInventoryBusiness = null;
		caLocaloptionsBusiness = null;
		caRegistrationBusiness = null;
		caReportsBusiness = null;
		caMiscRegBusiness = null;
		caProcBusiness = null;
		// defect 7768
		caHelpBusiness = null;
		// end defect 7768
		// defect 9085
		caSpclPltsBusiness = null; 
		// end defect 9085
	}

	/**
	 * Calls processInventory() on the specified business layer object
	 *
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData int
	 * @return java.lang.Object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws com.txdot.isd.rts.services.exception.RTSException
	{
		switch (aiModuleName)
		{
			case GeneralConstant.ACCOUNTING :
				return caAccountingBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.COMMON :
				return caCommonBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.FUNDS :
				return caFundsBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.GENERAL :
				return null;

			case GeneralConstant.INQUIRY :
				return null;

			case GeneralConstant.INVENTORY :
				return caInventoryBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.MISC :
				return caMiscBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.LOCAL_OPTIONS :
				return caLocaloptionsBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.MISCELLANEOUSREG :
				return caMiscRegBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.REGISTRATION :
				return caRegistrationBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.REPORTS :
				return caReportsBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.SYSTEMCONTROLBATCH :
				return null;

			case GeneralConstant.TITLE :
				return caTitleBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.INTERNET_ADDRESS_CHANGE :
				return caProcBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.INTERNET_REG_REN :
				return caProcBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);

			case GeneralConstant.INTERNET_REG_REN_PROCESSING :
				return caProcBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);
					
			// defect 7768
			case GeneralConstant.HELP :
				return caHelpBusiness.processData(
					aiModuleName,
					aiFunctionId,
					aaData);
			// end defect 7768
					
			// defect 9085 	
			case GeneralConstant.SPECIALPLATES : 
				return caSpclPltsBusiness.processData(
							aiModuleName,
							aiFunctionId,
							aaData);
			// end defect 9085		

			default :
				throw new RTSException(
					RTSException.JAVA_ERROR,
					new Exception(ERRMSG_NAME_NOT_FOUND));
		}
	}
}
