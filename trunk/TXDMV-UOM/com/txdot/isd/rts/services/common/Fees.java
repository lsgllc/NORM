package com.txdot.isd.rts.services.common;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * Fees.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa	04/11/2002	Changed calcWgtBsdFee()to make TireTypeFee=0
 * 							when cRegWtFeesData is null 
 * 							defect 3709
 * S Govindappa 05/17/2002  Changed the Fee Desc for Headquarter to be 
 * 							"Duplicate Receipt"
 * 							defect 4002 
 * S Govindappa 05/17/2002  Removed check for new plate desired from 
 * & Todd					whetherIssueInvForTtl() to fix the defect 
 * 							with the issuing inventory
 * 							defect 3978 
 * Govindappa  	05/22/2002 	Put a check for RegPeriodLength for zero 
 * & Todd                   value in asgnToMoYrNowPlusRplMinusOne() to  
 * 							prevent Arithemetic error
 * 							defect 4070 
 * S Govindappa 05/24/2002  Changed calcReg and calcRepl to call a  
 * 							different method in RegistrationPlateStickerCache  
 * 							when RegStkrCd is blank
 * 							defect 4075 
 * S Govindappa 08/02/2002	Removed null pointer error from 
 * 							setFrmMoYrAftrCurrRegExp() when 
 * 							RegPeriodLength=0
 * 							defect 4480 
 * S Govindappa 08/02/2002	Changed setToMoToYrForNonFxdMoYr and 
 * 							calcMaxToMoToYr to use 
 * 							carrCompTransData[0].getOfcIssuanceNo() 
 * 							instead of cWSOfficeIds.getOfcIssuanceNo().
 *							defect 4480. 
 * S Govindappa
 * K Harrell	10/08/2002  Changed caclRepl() not to lookup the 
 * 							RegistrationPlateStickerCache with 
 * 							Registration Sticker Code and Registration 
 * 							Plate Code.
 *							defect 4818.
 * S Govindappa
 * K Harrell	10/09/2002  If MinExpMo/Yr > MaxExpMo/Yr assign Max to 
 * 							Min ExpMo/Yr values in MngDispRecExpMoYr()
 *							defect 4830.  						
 * S Govindappa 10/10/2002  defect  4844. Changed reCalcFees() to 
 * 							intialize ciTransGregorianDate before using 
 * 							it to prevent null pointer error during 
 * 							recalculation of fees.
 * S Govindappa 10/15/2002  Set the credit remaining to zero for all  
 * 							cases in calcPermWght method.
 * 							defect 4706. 
 * S Govindappa 10/24/2002  Put a check for null pointer for lDieselFee  
 * 							in calcRegSurChrg().
 * 							defect  4940. 
 * S Govindappa 11/07/2002  Put a check for null value for cItemCdsData
 * 							in calcFees method.
 * 							defect  5073. 
 * S Govindappa 11/08/2002  Set the RegInvldindi to 1 for Antiques in 
 * 							Title events in calcFees method.
 * 							defect  4988. 
 * B Brown      11/13/2002  changed calcCommnFees method.
 *                          if commFeesData is null, use current date to
 * 							access common fees
 * 							new code for defect 4913 
 * S Govindappa 01/15/2002  Removed the changes made for fixing defect
 * 							# 4913 to further analize the defect.
 * S Govindappa 02/14/2002  Made changes to setFromMoFromYr(..) to check
 * 							for Model year change when calculating 
 * 							FromMo and FromYr values resulting in 
 * 							correct fees.
 * 							defect 5149. 
 * S Govindappa 02/14/2002  Made changes in calcFee(..) not to do 
 * 							registration invalid when changing from 
 * 							antique plate to a antique plate in Title 
 * 							event.
 * 							defect 5338. 
 * S Govindappa 02/19/2003  Made changes in setMinToMoToYr(..) and 
 * 							calcMaxToMoToYr(..) to set the default year 
 * 							to Curr Yr+1 for currently registered 
 * 							vehicles which have Combination, Cotton, 
 * 							Soil Conservation, Soil Conservation Trailer
 * 							or fertilizer plates. 
 * 							defect 5284(PCR49) 	
 * S Govindappa 02/20/2003
 *              02/21/2003  Use current date to calculate the common 
 * 							fees for modify registration.
 * 							defect 4913 
 * K Harrell    04/27/2003  Divide by 0 error  on Repl of RegClassCd 39
 *                          altered applyMultiYrAdj
 * 							defect 6001 
 * K Harrell	07/22/2003  Do not charge the ins fee on exch.
 *						    move csAcctItmCd before if in calcRegAddl()
 *						    modify calcRegAddl()
 *						    defect 6372
 * B Hargrove   08/04/2003	added checks to not charge fee if DTA or if 
 * 							GDN is present.
 *                          modify calcRegAddl()
 * 							defect 6457
 * K Harrell	08/06/2003  Add calculation for TERP
 *                          modify titleFeeSuite()
 * 							modify calcSalesTaxSCharge()
 * 							add calcTtlTERPFee()
 *						    defect 6447  	
 * K Harrell	08/07/2003  Altered calcSalesTaxSCharge 
 *							passing aiRTSCurrentDate to getTERPPrcnt
 *							defect 6447
 * J Rue		08/07/2003	turn on logic to charge TERP fee
 *							modify calTtlTERPFee()
 *							defect 6447 
 * B Hargrove   08/11/2003  Fix to Defect 6457 - wasn't charging fees  
 * 							other than INS.
 *                          modify calcRegAddl()
 * B Arredondo	08/27/2003	Added if statement to method calcSalesTaxSCharge()
 * 							to whether charge or not charge the percentage.
 *							defect 6448 
 * B Arredondo 	09/15/2003	Added code to handle TERP fee for corrected 
 * 							title and corrected title rejection.
 *							methods: calcTtlCorr(), calcTtlFee(), and 
 *							callFeeCalcTtlMthds().
 *							formatted method: titleFeeSuite()
 *							defect 6563 
 * Jeff R.		12/05/2003	HB002 (IRR52040003), requires to charge the 
 * 							$1.00 Insurance Fee for Dealer.
 *							method calcRegAddl()
 *							Defect 6676, Version 5.1.5 fix 1A
 * Jeff R.		12/16/2003	HB002 (IRR52040003), Determine whether to 
 * 							charge RegAddlFee, DTA.
 *							Start date January 11, 2003
 *							Defect 6676, Version 5.1.5 fix 1A,	method 
 *							calcRegAddl(), 
 *							add determineChrgRegAddlFee(), getDLRGDN()
 * K Harrell 	01/11/2004	Streamline code for HB002, (IRR52040003)-
 * 							Defect 6676
 *							modify determineChrgRegAddlFee()
 *							defect 6781, Ver 5.1.5 Fix 2
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * B Hargrove 	12/02/2004	Use Today's date for lookup for Temp Addl 
 *							Weight.
 *							modify calcMiscRegFees()
 *							defect 6501, Ver 5.2.2
 *							defect 6208,6501 Ver 5.2.2
 * B Hargrove 	12/29/2004	Simplify calcCommnFees() by always doing
 *							lookup.Then if no row found, do lookup using
 *							today's date and log this fact to RTSAPP.LOG
 *							and VSAM error report.
 *							Add Class Var 'cbErrorHasLogged' so that we
 *							only log error once.
 *							Add check for '# of months to charge > 0' 
 *							before calculating Registration fees
 *							(replaces bogus check for null Common fees).
 *							Clean up comments and Hungarian notation.
 *							Comment-out 'lVehData' reference (not used).	
 *							import com.txdot.isd.rts.services.exception.*
 *							add errmsg NO_COMMON_FEES_ROW_FOUND (2011)			
 *							modify calcCommnFees(),calcMiscRegFees(),
 *								   calcReg(), calcRegSurChrg()
 *							defect 6208, Ver 5.2.2
 * B Hargrove 	01/03/2005	Add check of 'Off Highway Use' to DTA calc. 
 *							Mirrors check in normal title (calcTtlFee()).
 *							modify calcDelrTtl()
 *							defect 7538, Ver 5.2.2
 * B Hargrove 	01/03/2005	Remove check for RegClassCd is STILL antique
 *							when determining to set Reg Invalid indi (it
 *							is enough to know that original regis was
 *							antique).
 *							Hungarian notation clean-up.
 *							modify calcFees()
 *							defect 6088, Ver 5.2.2
 * B Hargrove 	01/27/2005	Modify calculation for maximum ToYear for 
 *							new vehicle (should allow up to 36 months).
 *							Was allowing 48 months if current month is
 *							January.
 *							modify calcMaxToMoToYr()
 *							defect 7938, Ver 5.2.2
 * Ray Rowehl	02/07/2005	Moved to services.common since it is called
 * 							from both client and server side
 * 							moved to new package
 * 							defect 7705 Ver 5.2.3
 * B Hargrove 	03/10/2005	Move curly braces at end of 'else' block to
 *							include code into proper 'else' block (fix
 *							problem introduced by fix for defect 7538).
 *							modify calcDelrTtl()
 *							defect 7972, (orig Ver 5.2.2  02/15/2005)
 *							moved to WSAD   Ver 5.2.3
 * B Hargrove 	03/23/2005	Removed logging of msg 2011 to MF errlog and
 * 							do not log msg to RTSAPP.LOG if no MF record
 * 							found.
 * 							(see defect 6208)
 * 							modify calcCommnFees()
 * 							defect 8112  Ver 5.2.2 Fix 3
 * K Harrell	04/05/2005	Modifications for multiple Reg Additional
 *							Fees.  
 *							modify calcRegAddl()
 *							defect 8104  Ver 5.2.2 Fix 4
 * K Harrell	04/05/2005 	Delete unused variables.
 * 							delete cvTtlCorrTransCds,cvAddSalTaxTransCds,
 * 							cvDlrTtlTransCds  			
 *							modify calcCommnFees(), calcRepl(),
 * 							calcWgtBsdFee(),crdtRemaining(),
 *							whetherIssueInvForTtl()
 *							defect 8151 Ver 5.2.3 
 * K Harrell	04/07/2005	cCommonFees is not populated when Subcon
 *							calls calcRegAddlFees.Null pointer exception
 *							modify applyMultiYrAdj()
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	05/25/2005	Java 1.4 Work
 * 							deprecated testIfNotWithinWnd()
 * 							modified calls to testIfNotWithinWnd() to 
 * 							!testIfWithinWnd()
 * 							renamed calcDelrTtl() to calcDlrTtl(),
 * 							calcInquiry() calcVehInquiry(),
 * 							editDelrTtlExpMoYr() to editDlrTtlExpMoYr()
 * 							defect 8151 Ver 5.2.3
 * K Harrell	06/24/2005	Implementation of CLASS_TO_PLATE,PLT_TO_STKR
 * 							modify calcReg()
 * 							defect 8218 Ver 5.2.3  
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							modify Fees()
 * 							modify calcMiscRegFees()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	08/13/2005	Double item price & quantity when issue
 *							2 placards vs. adding add'l fee record. MF 
 *							cannot accept duplicate TR_FDS_Detail records 
 *							add adjDisPlcrdFee()
 *							modify asgnMiscFees(),calcMiscRegFee()
 *							defect 8325 Ver 5.2.2 Fix 6
 * J Zwiener	08/16/2005  Null pointer when not disabled event
 *							modify adjDisPlcrdFee()
 *							defect 8328 Ver 5.2.2 Fix 6  
 * B Hargrove	11/21/2005  Changes to handle Seasonal Ag plates. Most
 * 							changes needed because Reg Period Length = 1
 * 							(choice of 1 to 6 months registration).
 * 							add setDfltToMax(),
 * 							setToMoToYrToMinOfCurrExpDtOrMaxAllow() 
 *							modify applyMultiYrAdj(),
 *							asgnToMoYrNowPlusRplMinusOne(), 
 *							calcCommnFees(), calcFees(), 
 *							calcMaxToMoToYr(), callFeeCalcTtlMthds(), 
 *							setDfltMoYrFees(), setFrmMoYrAftrCurrRegExp(), 
 *							setToMoToYrForNonFxdMoYr()
 *							defect 8404 Ver 5.2.2 Fix 7  
 * B Hargrove	11/28/2005  Needed to add check for CommonFees = null 
 * 							and do a lookup to CommonFees before checking
 *							RegPeriodLngth  (see: 8404)
 *							modify callFeeCalcTtlMthds()
 *							defect 8438 Ver 5.2.2 Fix 7  
 * B Hargrove	12/05/2005  Default Mo\Yr on REG029 for Title in window
 * 							was incorrect. Found that a 'not' sign was
 * 							removed when checking for isRegExpired(). 
 *							modify setDfltMoYrFees()  
 *							defect 8404 Ver 5.2.2 Fix 7
 * B Hargrove	12/07/2005  Title, multi-year regis was setting ItemQty
 * 							to 3 (should always be 1). Found an assign
 * 							statement 'ciItmQty = 1' dropped from 
 * 							prior release code by mistake.
 *							modify asgnMiscFees()  
 *							defect 8235 Ver 5.2.3
 * B Hargrove	12/13/2005  Fixed check for 'don't need to set default
 * 							if not going to REG029'.
 *							modify calcFees(),setDfltMoYrFees()  
 *							defect 8470 Ver 5.2.2 Fix 7
 * B Hargrove	12/13/2005  Renew Antique charging > 1 yr of AddlRegFees.
 * 							Points out that we are using RegPeriodLngth
 * 							column of CommonFees for two different
 * 							purposes (ie: RPL = 1 for Seasonal Ag, 
 * 							RPL = 60 for Antique). See also : defect 8404
 * 							for Seasonal Ag.
 *							modify applyMultiYrAdj())  
 *							defect 8471 Ver 5.2.2 Fix 7
 * B Hargrove	12/21/2005  Skip DTA edit for 'must be 12 months' if 
 *							RegPeriodLngth < 12 (ie: Seasonal Ag).
 *							Final changes to Min\Max months of reg based
 *							on latest VTR requirements. 
 *							modify calcMaxToMoToYr(),
 *							editDelrTtlExpMoYr(), setDfltMoYrFees(),
 *							setToMoToYrForNonFxdMoYr()							 
 *							defect 8477, 8404 Ver 5.2.2 Fix 7
 * K Harrell	12/22/2005	Issue Inventory in Title if RegClassCd has
 * 							changed
 * 							modify whetherIssueInvForTtl()
 * 							defect 8482 Ver 5.2.2 Fix 7
 * K Harrell	01/03/2006	Restore call to asgnToMoToYrCEPlusMaxRegMo() 
 * 							for //8404 MY CASE 6,7 - MAX
 * 							calcMaxToMoToYr()
 * 							defect 8404 Ver 5.2.2 Fix 7
 * K Harrell	01/03/2006 	Restore assignment to caCommFeesData
 * 							when calculating CommonFeesData
 * 							defect 6208 Ver 5.2.2
 * J Zwiener	01/09/2006	Seasonal Ag temp addl wt fees not computing
 *							correctly.
 *							modify calcMiscRegFees()
 *							defect 8508 Ver 5.2.2 Fix 7
 * K Harrell	01/10/2006	Use indicator from Tow Truck to determine
 * 							if currently registered.
 * 							delete ciRegCurrIndi
 * 							modify calcMiscRegFees(),setMinToMoToYr()
 * 							defect 6801 Ver 5.2.3	
 * B Hargrove	02/03/2006	Use CommonFees lookup to find Regis Emission
 *							Fee Percent. 
 *							modify calcCorrectionFee(), calcPermWght(),
 *							calcSubcon(), callFeeCalcMthdBsdOnTransCd(),
 *							callFeeCalcTtlMthds()
 *							defect 8516 Ver 5.2.2 Fix 8
 * B Hargrove	04/14/2006	Null pointer in lookup to
 * 							VehicleDieselTonCache using 
 * 							laRegData.getRegEffDt(). If null, try again  
 *							using today's date. * Also found: look-up to
 *							caRegWtFeesData in calcWgtBsdFee() returned 
 *							null, so also do another look-up using today.  
 *							modify adjAddlDislFee(), calcWgtBsdFee()
 *							defect 8690 Ver 5.2.3
 * B Hargrove	09/12/2006	Problem with Exch to Seas Ag. Line left out
 * 							when moving from VAJ (5.2.2) to WSAD (5.2.3)
 *							modify setToMoToYrForNonFxdMoYr()
 *							defect 8928 Ver 5.2.5
 * B Hargrove 	09/28/2006 	Sales Tax Emissions was not charging for RPO
 * 							Combo. Change check of Veh Sales Price from
 * 							!= null to != null and > 0.
 * 							modify calcSalesTaxSCharge()
 *							defect 8608 Exempts
 * B Hargrove 	10/09/2006 	Counties can now do Exempts. Adjust Fee calc
 * 							now that it is not HQ only. Allow for the 
 * 							'Exempt Regis  0.00' fee.
 * 							Change all checks for Exempt RegClassCd (39)
 * 							to check for FeeCalcCat = 4 (No Regis Fees).
 * 							Change all assignements to object 
 * 							ZERO_DOLLAR to assignment to new Dollar(0).
 * 							modify calcDlrTtl(), calcMaxToMoToYr(), 
 * 							calcPermWght(),calcRepl(), calcTtlCorr(), calcTtlFee(), 
 * 							callFeeCalcMthdBsdOnTransCd(),
 * 							setMinToMoToYr(), testForRsnNotCallRegFeeSuite()
 *							defect 8900 Exempts
 * K Harrell	10/10/2006	Populate FeesData for Subcon when "0.00"
 * 							with Exempt AcctItmCd, etc.
 * 							modify calcSubCon()
 * 							defect 8900 Exempts 
 * K Harrell	10/10/2006	Do not charge registration fees when 
 * 							!Charge Fee on Reg039 
 * 							modify calcRegRnwl(),callFeeCalcTtlMthds()
 * 							defect 8900 Exempts
 * B Hargrove	10/12/2006	Also check Charge Fee indi = 0 before setting 
 * 							Addition Weight fee to 0.00 (ie: allow user 
 * 							to charge fee for).
 * 							Fix setting 'Exempt Regis Fee 0.00' so that
 * 							it displays for Regular and Standard Exempt. 
 * 							modify calcPermWght(),
 * 							testForRsnNotCallRegFeeSuite()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/13/2006	Removed checks for Exempts from calcDlrTtl()
 * 							because DTA cannot do Exempts. 
 * 							modify calcDlrTtl()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/19/2006	Handle charging\not charging fees in 
 * 							Apprehended Addl Wt (CORREGX) based on 
 * 							Charge Fee Indi.
 * 							No credit is given in Exchange when moving
 * 							from Exempt.
 * 							Do not charge reg fees (auto, reflect, etc)
 * 							if No Charge Fee Indi = 0.
 * 							modify calcCorrectionFee(), crdtRemaining(),
 * 							regFeeSuite()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/20/2006	Set Charge Fee Indi = 1 for Internet Renew. 
 * 							Set caRegisPenaltyFee to 0 if it is null.
 * 							modify calcFees(), crdtRemaining()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/25/2006	Make sure 'Exempt Reg  0.00' is created 
 * 							whenever No Charge Fee Indi = 1.
 * 							modify calcReg()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/25/2006	Remove the 'Regis Credit Remaining' row if 
 * 							it is zero. For Exempts, Max Credit may be 
 * 							0 because reg fees for Exempt with Charge 
 * 							Fee Indi = 0 will be 0.
 * 							modify setCrdtRmng()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/26/2006	Perm Addl Wt was using 'setMinToMoToYr()' to 
 * 							set To Month\Year for 'new weight'. It 
 * 							should instead always use current RegExpMo\Yr. 
 * 							modify calcPermWght()
 * 							defect 8532 Ver Exempts
 * B Hargrove	10/26/2006	Fix edit for 'do not calc credit if moving 
 * 							from Exempt' to allow credit calc if event 
 * 							is Apprehended or Perm Addl Weight.
 * 							Add check for 'is not Standard Exempt' in 
 * 							edit for whether weight has changed (was
 * 							checking for FeeCalcCat = 2 and we now have
 * 							FeeCalcCat = 4 for Standard Exempts).
 * 							Use a local object for OfcIssuanceNo when
 * 							changing county number for Apprehended so 
 * 							that the actual cache object is not changed. 
 * 							modify calcFees(), crdtRemaining(), 
 * 							reCalcFees(), testIfVehWgtNotChnged()
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/27/2006	Set CompTransData 'No Charge Fee Indi'
 * 							which was set = 'true' when Exempt was 
 * 							selected on TTL008.		
 * 							modify callFeeCalcTtlMthds() 
 * 							defect 8900 Ver Exempts
 * B Hargrove	10/30/2006	Fix calcCommnFees() to use ciFCalcRow 
 * 							instead of 0.
 * 							Do not show 'Credit is given for # months of 
 * 							registration' msg when Exchanging from an 
 * 							Exempt (no credit is given when moving from
 * 							Exempt).
 * 							modify calcCommnFees(), setNoOfMoRegCrdtMsg() 
 * B Hargrove	10/31/2006	Add call to setCrdtRmng() after call to  
 * 							regFeeSuite() when doing Title with Exchange.
 * 							(For excample, Exchanging to Standard Exempt
 * 							would otherwise never go thru setCrdtRmng().) 
 * 							modify callFeeCalcTtlMthds()
 * K Harrell	11/01/2006	Set EmissionSalesTax in VehMiscData object
 * 							modify calcSalesTaxSCharge()
 * B Hargrove	11/02/2006	Set ToMonth\Year when Exchanging   
 * 							from Standard Exempt (Min = Curr Mo\Yr, 
 * 							Max = Max Reg Period Length).
 * 							Make REG029 default Reg = Minimum when title
 * 							an Exempt. 
 * 							Do not charge Regis Emissions Fee if coming
 * 							from Exempt.
 * 							modify calcMaxToMoToYr(), calcRegSurChrg(),
 * 							setDfltMoYrFees(), setToMoToYrForNonFxdMoYr()
 * 							defect 8900 Ver Exempts 
 * B Hargrove	11/07/2006	Fix charging\not charging Title Fees based 
 * 							on individual Title\TERP\etc charge fee 
 * 							indis (NOT to be confused with charging Reg 
 * 							fee in Title event based on Exempt indi).  
 * 							modify callFeeCalcTtlMthds() 
 * 							defect 8900 Ver Exempts
 * B Hargrove	11/08/2006	Fix setting Maximum ToMonth\Year for Renew   
 * 							expired with Invalid Reason.
 * 							modify calcMaxToMoToYr()
 * 							defect 9008 Ver Exempts
 * B Hargrove	11/09/2006	Remove setting 'No charge' based on Exempt   
 * 							Indi inside 'Assign Misc Fees'. That has 
 * 							been taken care of other places, and things 
 * 							like Mail-In Fee need to charge even if  
 * 							Exempt Indi is set. 
 * 							Remove Mail-In Fee from group of Reg fees
 * 							that are not charged if Exempt Indi is set.
 * 							modify asgnMiscFees(), regFeeSuite()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/11/2006	Rollback portion of fix of 11/09. Broke No 
 * 							Charge DUPL, VEHINQ. 
 * 							modify asgnMiscFees(), calcMailIn()
 * 							defect 8900 Ver Exempts
 * B Hargrove	11/15/2006	Need to check for Exempt Indi = 0 when    
 * 							deciding to skip calling reg fee suite. If
 * 							Exempt, need to call to set Exempt Reg 0.00. 
 * 							modify testForRsnNotCallRegFeeSuite()  							
 * 							defect 8900 Ver Exempts
 * K Harrell	11/30/2006	Do not charge Exempt Registration in REJCOR
 * 							modify calcReg()
 * 							defect 9041  Ver Exempts
 * B Hargrove	12/07/2006	Code was removed in previous release by 
 * 							mistake. This setting of ciFCalcRow = 0 is 
 * 							important. Later on in calcFees(), we will 
 * 							call setDfltMoYrFees() which calls 
 * 							calcCommnFees(). If ciFCalcRow = 1, any 
 * 							vehicle with no 'original' registration 
 * 							(ie: DocTypeCd 13, 15, etc) will cause a 
 * 							null pointer exception.
 * 							modify regFeeSuite()
 * 							defect 9052  Ver Exempts
 * B Hargrove	01/31/2007	In title of originally Exempt vehicle,
 * 							calculate CRBF and CSF even if number of
 *							months to charge is the same (ie: apparent
 *							even exchange) because Exempt will have been
 *							marked 'reg invalid' (see: 9083)	
 * 							modify calcChildSafty(), calcRdBrdg()
 * 							defect 9101  Ver Region Credit Card
 * B Hargrove	01/22/2007	Historically in RTS I days, data coming from
 * 							the mainframe was in MfVeh{0}, and then as 
 * 							it was updated, the new data was in MfVeh{1}
 * 							or higher. 
 * 							In Fees, this was reversed; the original 
 * 							data was in FCalc{1}, and the updated data
 * 							was in FCalc{0}. 
 * 							This defect will remove this inconsistency:
 * 							New constants ORIG = 0, UPDT = 1.
 *							caCommFeesData[0] will be ORIG (original) data
 *							caCommFeesData[1] will be UPDT (updated) data
 *                          We use ciFCalcRow as index to this array.
 * 							I will also rename some objects for consistency:
 *							caMFVeh0/caMFVeh1 becomes caMFVehOrig/caMFVehUpdt
 * 							caReg0/caReg1 becomes caRegOrig/caRegUpdt
 *							caTtl0/caTtl1 becomes caTtlOrig/caTtlUpdt 
 *							caVeh0/caVeh1 becomes caVehOrig/caVehUpdt
 *							caVehMisc0/caVehMisc1 becomes caVehMiscOrig/caVehMiscUpdt
 *							add constants ORIG, UDPT
 * 							modify all refereences to objects listed above
 * 							defect 9084  Ver Special Plates
 * K Harrell	02/01/2007	Use PlateTypeCache vs. 
 * 									RegistrationRenewalsCache
 * 							modify calcRepl()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/09/2007	Placeholders for Subcon Fee Calculations.
 * 							add calcSubconSpclPltFees(),
 * 							  asgnSpclFeesToFeesData()
 * 							modify calcSubcon()
 * 							TO BE CONTINUED! 
 * 							defect 9085 Ver Special Plates  	
 * B Hargrove 	02/20/2007 	For Special Plates Full implementation,
 * 							remove all checks for InvProcsngCd (1/2/3).
 * 							Instead, use services.cache.PlateTypeCache.
 * 							isOutOfScopePlate() and isSpclPlate().
 * 							Adjust some Max ToMo\Yr edits that have now
 * 							changed. Set FromMo\Yr for Special Plate. 
 * 							Adjust Min and Max ToMo\Yr for Special Plate.
 * 							Update the Special Plate Exp Mo\Yr.
 * 							Show message for 'Months of Special Plate
 * 							Fee sold'.
 * 							add ciSpclPltExpMo, ciSpclPltExpYr,
 * 							ciSpclPltFromMo, ciSpclPltFromYr,
 * 							ciSpclPltNoMoCharge, csSpclPltMoSold,
 * 							adjMaxForSpclPlt(), adjMinForSpclPlt(),
 * 							calcSpclPlateFee(), setSpclPltFromMoFromYr() 
 * 							modify calcFees(), calcMaxMinFees(),
 * 							calcMaxToMoToYr(),
 * 							callFeeCalcMthdBsdOnTransCd(), 
 * 							mngDispRecExpMoYr(), reCalcFees(),
 * 							setDfltMoYrFees(), 
 * 							setDfltToCurrRegPlusOneRpl(), 
 * 							setDisCntyForTtl(), setFromMoFromYr(),
 * 							setToMoToYrForNonFxdMoYr(),
 *  						testForRsnNotCallRegFeeSuite()
 * 							defect 9126 Ver Special Plates
 *  K Harrell	02/13/2007	Use UPDT in new Subcon methods.
 * 							 modify calcSubconSpclPltFees(),
 * 							  asgnSpclFeesToFeesData()
 * 							defect 9085 Ver Special Plates
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify adjMaxForSpclPlt(), calcFees()
 * 							modify adjMinForSpclPlt(),
 * 							modify calcSpclPlateFee(),
 * 							modify setSpclPltFromMoFromYr()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/25/2007	Check for !null SpclPltRegisData to 
 * 							determine if Special Plate to resolve
 * 							null pointer non-vehicle based trans
 * 							modify calcFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/04/2007	Special Plates Replacement Processing
 * 							add caSpclPltRegOrig, caSpclPltRegUpd
 * 							modify calcRepl(), calcAutomation(),
 * 							 calcReflectn()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	03/05/2007 	Fix setting Special Plate FromMo\Yr. Use
 * 							Special Plates Number of Months to Charge to
 * 							prorate Special Plate Fee.
 * 							modify calcSpclPlateFee(),
 * 							setSpclPltFromMoFromYr()
 * 							defect 9126 Ver Special Plates
 * K Harrell	03/08/2007	No record found; Null pointer when isSpclPlt
 * 							on original registration.
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	06/28/2007 	Continue work on Special Plates.
 * K Harrell				add isLP()
 * 							modify calcMaxToMoYr(), setMinToMoToYr()	
 * 							defect 9126 Ver Special Plates
 * K Harrell	07/01/2007	Modify Subcon Calculation for Special 
 * 							Plates, FeeCalcCat = 4
 * 							modify calcSubcon()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/13/2007	Do not prorate "SpclPlt" portion of  fee 
 * 							where PLP and SpclPltPrortnIncrmnt = 12
 * 							modify calcSpclPlateFee()
 * 							defect 9085 Ver Special Plates
 * B Brown		07/16/2007  Undo the hardCoded setting a charge indi
 * 							for IRENEW.
 * 							modify calcSpclPlateFee() 
 * 							defect 9119 Ver Special Plates   
 * B Hargrove 	07/19/2007 	Continue work on Special Plates.
 * K Harrell				Skip reg fees if HQ
 * 							modify callFeeCalcMthdBsdOnTransCd()	
 * 							defect 9126 Ver Special Plates
 * K Harrell	07/31/2007	If Special Plates && 0, do not create
 * 							Exempt AcctItmCd.
 * 							modify calcSubcon(),calcSubconSpclPltFees()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove 	08/06/2007 	Add check for 'is Special Plate' when checking
 * 							for 'isLP(). 
 * 							By definition for LP, expiration must be the
 * 							the expiration that is on the plate.
 * 							Unfortunately an 'FO - Foreign Organization 
 * 							plate' IS an LP, but is NOT a special plate
 * 							so we CANNOT stay the same exp that is on 
 * 							the plate. Do not got thru adjMinForSpclPlt()
 * 							for an LP unless it is a Spcl Plt.  
 * 							modify setMinToMoToYr()	
 * 							defect 9226 Ver Special Plates
 * B Hargrove 	08/16/2007 	Clear out class variable for Base Reg Fee. 
 * 							This is calculated in methods based on 
 * 							FeeCalcCat, but if you Exchange to a 
 *							FeeCalcCat 4 (no regis fees) you skip around
 *							all of these and fall into code which uses 
 *							Base Reg Fee which may contain a Base Reg Fee
 *							from original RegClasscd.
 * 							modify calcReg()	
 * 							defect 9260 Ver Special Plates
 * K Harrell	08/21/2007	Comment out call to calcSubconSpclPltFees()
 * 							Reverting back to Exempts version of Subcon  
 * 							modify calcSubCon()
 * 							defect 9085 Ver Special Plates 
 * B Brown		09/12/2007	Change fees object reference so IRENEW's 
 * K Harrell				charge the mail-in fee
 * 							modify checkRegOnlyFees()
 * 							defect 9313 Ver Special Plates
 * K Harrell	10/12/2007	Restore calcSubconSpclPltFees 
 * 							modify calcSubcon()
 * 							defect 9362 Ver Special Plates 2
 * B Hargrove 	10/19/2007 	Add code to allow charging fees for Plate
 * 							Transfer Fee and Buyer Tag Fee.
 * 							add calcBuyerTagFee(), calcPltTrnsfrFee()
 * 							modify calcReg() 
 * 							defect 9366 Ver Special Plates 2
 * K Harrell	10/22/2007	Add code for above for REJCOR, CORTTL 
 * 							modify callFeeCalcTtlMthds()
 * 							  calcTtlCorr(), calcReg() 
 * 							defect 9368 Ver Special Plates 2 
 * B Hargrove 	10/25/2007 	Fix setting Maximum  and Minimum To Mo\Yr. 
 * 							For Renew, if vehicle exp 03/2008 and 
 * 							current date = 03/2008, it was prompting for
 * 							2008 sticker. Should be 2009.
 * 							modify calcMaxToMoToYr(), setMinToMoToYr() 
 * 							defect 9376 Ver Special Plates 2
 * K Harrell	11/07/2007	Use Current Date if AcctItmCd not found with
 * 							ciTransGregorianDate 
 * 							modify selectFrmAcctCdsTbl()
 * 							defect 9425 Ver Special Plates 2 
 * B Hargrove 	11/08/2007 	Add adjMinForSpclPlt() to 'fixed exp month' 
 * 							section of setMinToMoToYr(). 
 * 							For example, if currently 01/2008 and you 
 * 							title a vehicle with Combo plate valid thru
 * 							03/2009, minimum should be 03/2009, not 03/2008.
 * 							modify setMinToMoToYr() 
 * 							defect 9434 Ver Special Plates 2
 * K Harrell	11/12/2007	Do not charge DPS fee if have requested new
 * 							inventory w/o veh/reg change.
 * 							add testIfDPSFeesRequired()
 * 							modify determineChrgRegAddlFee()
 * 							defect 9425 Ver Special Plates 2
 * K Harrell	11/17/2007	Remove calculation of Plate Transfer free
 * 							from CORTTL 
 * 							modify calcTtlCorr()
 * 							defect 9368 Ver Special Plates 2  
 * B Hargrove 	11/20/2007 	Remove checks for 'TONLY' (Title Only - No 
 * 							Regis) in setting Min\Max. Add check for 
 * 							TONLY in deciding the calculate regis. 
 * 							Some clean-up for Special Plates.  
 * 							modify calcMaxToMoToYr(), 
 * 							setToMoToYrForNonFxdMoYr(), 
 * 							testForRsnNotCallRegFeeSuite()
 * 							deprecate testForTOnlyAndSetToMoToYr()	
 * 							defect 9337 Ver Special Plates 2
 * Jim Z		02/14/2008	Use current date to compute credit for
 * 							VehModlYr based fees.  This forces the 
 * 							computed credit amount to be always equal
 * 							to the new regis amount.
 * 							modify calcYrMdlBsdFee()
 * 							defect 9561 Ver 3 Amigos Prep
 * B Hargrove 	12/14/2007 	In Exempts release, we added a check so that
 * 							RegFeeSuite is skipped if FeeCalcCat = 4
 * 							(no Reg fees). The Replacement event has its
 * 							own method 'calcRepl()' which calculates
 * 							Reg fees, so also need to add check for  
 * 							FeeCalcCat = 4 before calculating Reg fees. 
 * 							modify calcRepl()
 * 							defect 8795 Ver Defect POS A
 * B Hargrove 	04/03/2008 	No credit is given for Farm Trailer.
 * 							Check for RegClassCd != 16.
 * 							modify crdtRemaining()
 * 							defect 9158 Ver Defect POS A
 * B Hargrove 	04/03/2008 	Vol Perm Addt'l weight is not charging the 
 * 							$1 Reg Fee-DPS fee.
 * 							Add calcRegAddl() to calcPermWght().
 * 							modify calcPermWght()
 * 							defect 9325 Ver Defect POS A
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9631 Ver Defect POS A
 * B Hargrove	04/29/2008	Fixing Min \ Max date calc for Tow Truck 
 * 							which is fixed expiration in January. 
 * 							Need to check if currently registered; if in
 * 							the window; if passed year boundary   
 * 							(ie: 'in the window' = months 12 or 01).
 * 							Also need to charge 2 years ($30.00) if not
 * 							registered, in window, and select Max date.
 * 							modify calcMaxToMoToYr(), 
 * 							callFeeCalcMthdBsdOnTransCd(), 
 * 							setMinToMoToYr() 
 * 							defect 9553 Ver Defect POS A
 * K Harrell	05/25/2008	Modify calculation for Title Trnsfr Penalty 
 * 							modify calcTtlTrnsfrPnlty()
 * 							defect 9584 Ver 3 Amigos PH B 
 * B Hargrove 	06/02/2008 	(see: defect 8795) Now, after further review,  
 * 							for Replacement event we want to charge   
 * 							Reglect\Automate fees unless   
 * 							isStandardExempt(). This method finds  
 * 							RegClassCds where RegPeriodLngth = 0.
 * 							Currently: 12 (Driver's Ed), 39 (Exempt - 
 * 							Not For Title Only), and 81 (Exempt Moped).
 * 							modify calcRepl()
 * 							defect 9695 Ver Defect POS A
 * B Hargrove 	06/05/2008 	There are now two TERP fee Acct Cds for Cnty   
 * 							Status Cd = 'A' counties. A vector of all   
 * 							current TERP fees for a Cnty Status Cd is
 * 							now returned from TitleTERPFeeCache. Print
 * 							each returned Title TERP Acct Cd \ Fee. 
 * 							modify calcTtlTERPFee()
 * 							defect 8552 Ver Defect POS A
 * B Hargrove	06/20/2008	Do not synch Reg \ Plate expiration 
 * 							and do not charge plate fee for Vendor Plate. 
 * 							modify adjMaxForSpclPlt(), 
 * 							adjMinForSpclPlt(), calcSpclPlateFee(),
 * 							calcSubCon(), setToMoToYrForNonFxdMoYr()
 * 							defect 9689 Ver MyPlates_POS 
 * K Harrell	07/09/2008	Use different AcctItmCd for Delinquent 
 * 							Penalty Fee according to TtlSignDate 
 * 							modify calcTtlTrnsfrPnlty()
 * 							defect 9742 Ver MyPlates_POS 
 * B Hargrove	07/15/2008	Add new local fee: Mobility Fee (NOT the  
 * 							same as Title TERP Mobility Fee from 
 * 							defect 8552). 
 * 							add calcMobility() 
 * 							modify calcSubCon(), regFeeSuite()
 * 							defect 9728 Ver MyPlates_POS
 * K Harrell	08/06/2008	Always use DELPEN for AcctItmCd for Title  
 * 							transactions where Dealer Entity selected.
 * 							add DEALER_CD 
 * 							modify calcTtlTrnsfrPnlty()
 * 							defect 9791 Ver MyPlates_POS
 * K Harrell	08/14/2008	Do not charge Buyer Tag Fee if TtlSigned
 * 							Date prior to BuyerTag Start Date.
 * 							modify calcBuyerTagFee() 
 * 							defect 9799 Ver MyPlates_POS  
 * B Hargrove 	09/03/2008 	Fix error in exchanging to fixed expiration 
 * 							month (ie: Combo). 
 * 							modify calcMaxToMoToYr(), setMinToMoToYr()
 * 							defect 9784 Ver Defect POS B
 * B Hargrove	09/12/2008	In look-up to CommonFees, use todays' date 
 * 							if RegClassCd is not transferable
 * 							(ie: Antique)
 * 							modify calcCommonFees()
 * 							defect 9633 Ver Defect POS B
 * K Harrell	10/21/2008	Add new Disabled Placard "Replace" transcds.
 * 							Alter mechanism to determine if Disabled Placard
 * 							 transactions (Add or Replace) 
 * 							modify cvMiscaRegTransCds
 * 							modify calcMiscRegFees() 
 * 							defect 9831 Ver Defect POS B
 * K Harrell	01/07/2009 	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify adjMaxForSpclPlt(), adjMinForSpclPlt(),
 * 							 setSpclPltFromMoFromYr()  
 *        					defect 9864 Ver Defect_POS_D
 * B Hargrove	06/24/2009	Application for Cotton Plate needs to charge 
 * 							the $8 plate application fee. 
 * 							This is not a Special Plate but does have a
 * 							plate fee on application.
 * 							PltOwnrshpCd = 'V', FirstSetApplFee > 0.00,
 * 							RegPltCd = 'COTTON', AcctItmCd = 'SPL0036'
 * 							add calcCottonPltApplFee()
 * 							modify regFeeSuite()
 * 							defect 9498 Ver Defect_POS_F
 * K Harrell	07/26/2009	Modify for new AcctItmCds, TransCds  
 * 							modify Fees(), calcMiscRegFees() 
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	09/07/2009	Added missing RPLTDC, RPLPDC in Fees()
 * 							(was o.k. in 6.0.1 - Defect_POS_E') 
 * 							modify Fees()
 * 							defect 10133 Ver Defect_POS_F 
 * B Hargrove	12/14/2009	Title, current regis not in window, exch to 
 * 							Seas Ag. Min-Max is keeping the current reg
 * 							(ie: 8 months remain). Should be 1 -6 mo
 * 							option. In Title section when not expired, 
 * 							check for RegPeriodLngth = 1 and set minimum 
 * 							to Curr MoYr instead of current RegExpMoYr.
 * 							modify setToMoToYrForNonFxdMoYr()
 * 							defect 10258 Ver Defect_POS_H
 * B Hargrove	02/04/2010	Need to also check for regular exempt when 
 * 							testing if vehicle has current regis.
 * 							Also move check for exempt above the check
 * 							for expired when setting 'to month\year'. 
 * 							modify calcSpclPlateFee(), 
 * 							setToMoToYrForNonFxdMoYr(), testNoCurrReg()
 * 							defect 10349 Ver 6.4.0
 * B Hargrove	02/17/2010 	Change lookup to Common Fees table. If the 
 * 							RTSEDffDt is in the future, use that instead
 * 							of current date (ie: on Renew in Aug, 
 * 							RTSEffDT = Sept).
 * 							modify calcCommnFees() 	
 * 							defect 10214  Ver 6.4.0 
 * B Hargrove	04/01/2010 	Modifications for new Vendor Plate rules.
 * 							modify adjMaxForSpclPlt(), calcSpclPlateFee(),
 * 							calcSubCon(), calcSubconSpclPltFees(),
 * 							setSpclPltFromMoFromYr() 	
 * 							defect 10357  Ver 6.4.0 
 * T Pederson	04/27/2010 	Modifications for new Vendor Plate rules
 * 							for subcontractor renewals. 
 * 							add addOneMonthForSpclPlt()
 * 							modify calcSubconSpclPltFees(), 
 * 							setSpclPltFromMoFromYr()
 *							defect 10392  Ver POS_640
 * B Hargrove	04/30/2010 	We may adjust minimum for Vendor Plate IFF
 * 							the Plate Validity Term = One Year.
 * 							add ciCurrentRegExpMo, ciCurrentRegExpYr
 * 							modify adjMinForSpclPlt()
 * 							defect 10357  Ver 6.4.0 
 * K Harrell	06/01/2010	add processing for Duplicate Permit 
 * 							 transactions
 * 							modify Fees(), calcMiscRegFees(), 
 * 							 lookUpAssgnResForTmdPrmts()
 * 							defect 10491 Ver 6.5.0 
 * B Hargrove	06/18/2010 	For IRENEW, we need to capture the Spcl Plt
 * 							Months Sold and the new PltExpMo\Yr. Also,
 * 							set Max = Min.
 * 							modify calcSpclPlateFee(), 
 * 							mngDispRecExpMoYr()
 * 							(copied from 6.4.0) 
 * 							defect 10357  Ver 6.5.0 
 * B Hargrove	07/06/2010 	For Vendor Plate, capture the Plt Months
 * 							Sold and the new PltExpMo\Yr. 
 * 							Also, set Max = Min for IRENEW.
 * 							modify calcSpclPlateFee(), 
 * 							mngDispRecExpMoYr() 
 * 							(copied from 6.4.0) 
 * 							defect 10523  Ver 6.5.0 
 * B Hargrove	07/08/2010 	If charging >= 12 months but < a full multi-
 * 							year plate renewal, set Plate Term to One  
 * 							Year.
 * 							modify calcSpclPlateFee()
 * 							(copied from 6.4.0) 
 * 							defect 10523  Ver 6.5.0 
 * K Harrell	07/07/2010	add logic for HQ in for Permit Transactions
 * 							modify calcMiscRegFees()
 * 							defect 10491 Ver 6.5.0 
 * B Hargrove 	07/28/2010  Add check for Multi-Year Official Plate.
 * 							modify adjMaxForSpclPlt(), isLP()
 * 							defect 10548 Ver 6.5.0
 * K Harrell	08/13/2010	modify isLP()
 * 							defect 10548 Ver 6.5.0 
 * B Hargrove 	10/11/2010  Token Trailer now does not print sticker.
 * 							Add check for Token Trailer (33)so it 
 * 							doesn't fall thru to print 'CORREG'.
 * 							(Also removed 2 artifact 'hijack' comments
 * 							from setMinToMoToYr())
 * 							modify calcReg()
 * 							defect 10623 Ver 6.6.0
 * K Harrell	10/28/2010	Seller Financed should use DELPEN vs. 
 * 							DELPEN1. 
 * 							add DEALER_SELLER_FINANCED_CD
 * 							modify calcTtlTrnsfrPnlty()
 * 							defect 10646 Ver 6.6.0 
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify calcReg()
 * 							defect 10623 Ver 6.6.0
 *  B Hargrove 	12/17/2010  Fee Simplification:
 * 							Add rtscls.cfg FeeSimplifyStartDate. 
 * 							After this date, all counties will pay 
 * 							the Automation Fee.
 * 							After this date, Reflectorization Fee 
 * 							will no longer be charged.
 * 							Replacement Acct Item code is now in 
 * 							Rts_Plt_Type ReplAcctItmCd (there are now 2
 * 							possible repl fees).
 * 							Add FeeCalcCat 5. Most old FeeCalcCat 1 and 
 * 							2 will now be FeeCalcCat 5 weight-based.
 * 							add rtscls.cfg FeeSimplifyStartDate,
 * 							calcRegClsGrpWgtBsdFee()
 * 							modify  calcAutomation(), calcReflectn(),
 * 							calcReg(), calcRepl(), setMFBaseActlFee(),
 * 							testIfVehWgtNotChnged()
 * 							defect 10685 Ver 6.7.0
 *  B Hargrove 	01/13/2011  Multi-year Registration:
 * 							Most vehicles are now allowed from 12 - 36
 * 							months of registration.
 * 							add calcMaxToMoToYrMYr(), 
 * 							setMaxAllowOrMaxMultiYr()
 * 							modify editDlrTtlExpMoYr(), 
 * 							mngDispRecExpMoYr(), 
 * 							setToMoToYrForNonFxdMoYr(),
 * 							deprecate calcMaxToMoToYr() 
 * 							defect 10697 Ver 6.7.0
 *  B Hargrove 	01/25/2011  Title a Combo (in window) and not renewing
 * 							was using the original RegExpDt + 1 as the
 * 							new RTSEffDT. Title without renew should be
 * 							using the original RegEffDt.
 * 							modify callFeeCalcTtlMthds()
 * 							defect 10732 Ver 6.7.0
 *  B Hargrove 	02/23/2011  RegClassCd 61 (Cotton Trk > 1) has always
 * 							been part of a 'special handling' hard-coded
 * 							group. It is now eligible for multi-year
 * 							regis, so is removed from this group. 
 * 							modify calcMaxToMoToYrMYr(), setMinToMoToYr() 
 * 							defect 10697 Ver 6.7.0
 *  B Hargrove 	03/09/2011  Changes need for Websub renewals:
 * 							Handle new renewal type 'WRENEW'.
 * 							All expired registrations will be 'Invalid 
 * 							reason'. 
 * 							modify calcFees(), calcMaxToMoToYrMYr(), 
 * 							calcSpclPlateFee(), checkRegOnlyFees(),
 * 							mngDispRecExpMoYr(), setDfltMoYrFees(),
 * 							setFromMoFromYr(), setMFBaseActlFee(),
 * 							setMinToMoToYr(), setToMoToYrForNonFxdMoYr() 
 * 							defect 10772 Ver 6.7.1
 *  B Hargrove 	03/21/2011  Needed to put check in for FeeCalcCat = 5
 * 							for Non-Res Ag Permit.
 * 							(Copied up from Rel 6.7.0)
 * 							modify nonResAgrPrmts()
 * 							defect 10685 Ver 6.7.0
 *  B Hargrove 	03/24/2011  For Fee Simplify, Tow Truck is not supposed
 * 							to charge the $15 (currrently hard-code).
 * 							(Copied up from Rel 6.7.0)
 * 							modify calcMiscRegFees(),
 * 							callFeeCalcMthdBsdOnTransCd(), reCalcFees() 
 * 							defect 10685 Ver 6.7.0
 *  B Hargrove 	04/07/2011  It was decided to set the RegEffDt back to 
 * 							month after original expiration month for an
 * 							expired registration with Invalid Reason.
 * 							(copied up from Rel 6.7.0)
 * 							modify calcReg() 
 * 							defect 10685 Ver 6.7.0
 *  B Hargrove 	05/05/2011  In DTA, for a currently registered 2005 or 
 * 							2008 vehicle, the look-up to pass-fees tbl
 * 							is returning the 'old' 58.50 row, not the
 * 							'current' 50.50 row, thus making ws fee too
 * 							high. Need to set RegEffDt = Today if regis
 * 							is being sold. 
 * 							(copied up from Rel 6.7.0)
 * 							modify calcDlrTtl() 
 * 							defect 10808 Ver 6.7.0
 *  B Hargrove 	05/26/2011  Because of the way lookup to pass-fees 
 * 							happens, this fix also needs to be made even
 * 							if no new regis is being sold.
 * 							(exported, patched into Rel 6.7.0)
 * 							(copied up from Rel 6.7.1)
 * 							modify calcDlrTtl() 
 * 							defect 10808 Ver 6.7.0
 *  B Hargrove 	06/20/2011  Exchange was setting RegExpDt back. It was 
 * 							finding minimum of Max Allow Reg Mos or 
 * 							current ExpDt. Should just be current ExpDt.
 * 							modify setToMoToYrForNonFxdMoYr()
 * 							defect 10899 Ver 6.8.0
 *  B Hargrove 	09/07/2011  We do want to charge County Fees even if 
 * 							it's a Special Plate.
 * 							modify setDisCntyForTtl()
 * 							defect 9106 Ver 6.8.1
 *  B Hargrove 	09/30/2011  Make 'even exchange' if RegClassCd and 
 * 							Weight have not changed, by using 'new 
 * 							RegEffDt' in Common Fees lookup for both
 * 							original and new registrations. This gets
 * 							around the fee rules\rate changes caused by
 * 							Fee Simplification (defect 10685).
 * 							(copied up from Rel 6.8.2)
 * 							modify calcCommnFees(), calcCorrectionFee(),
 * 							callFeeCalcMthdBsdOnTransCd(), callFeeCalcTtlMthds()
 * 							defect 11003 Ver 6.8.2
 * B Hargrove	09/30/2011  We need to use 'new RegEffDt' in table lookups.  
 *        					modify calcSpclPlateFee() 
 *        					defect 10948 Ver 6.9.0
 * B Hargrove	10/06/2011  If Dealer GDN is entered, we want all 
 * 							expired registrations to be 'Valid Reason'.
 * 							add VALID_REASON_CD
 *        					modify calcFees() 
 *        					defect 11054 Ver 6.9.0
 * B Hargrove	10/07/2011  Add check for testIfVehWgtNotChnged() before 
 * 							calcPermWght(). This method checks for 
 * 							FeeCalcCat = 2 or 5 AND weight has changed,
 * 							so will keep us from getting null pointer in
 * 							calculating fees for Disabled Vet
 * 							(RegClass 13, FeeCalcCat 4, which has no fees).
 *        					modify checkRegOnlyFees()
 *        					defect 10952 Ver 6.9.0
 * B Hargrove	10/10/2011  Removed un-used variables and references: 
 * 							delete ciExit, ciExpFromMonths, ciIssueCancel,
 * 							ciNoMoCharge, caCrdtRemaining, caDieselFee,
 * 							caItemCdsData, caVehMisc, caSpclPltRegOrig,
 * 							caTtlOrig, caVehMiscOrig, caVehMisc, 
 * 							laPltTypeData, lvFees
 *        					defect 10952 Ver 6.9.0
 * K Harrell	10/10/2011	Modify calculation of Disabled Placard Fees
 * 							modify adjDisPlcrdFee() 
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	10/10/2011	Implement RegTtlAddlInfoData.getPTOTrnsfrIndi()
 * 							modify calcPltTrnsfrFee() 
 * 							defect 11030 Ver 6.9.0 
 * K Harrell	10/16/2011	Implement RegTtlAddlInfoData.getChrgRbltSlvgFeeIndi()
 * 							add calcRbltSlvgFee() 
 * 							modify calcTtlCorr(), callFeeCalcTtlMthds(), 
 * 							  titleFeeSuite() 
 * 							defect 11051 Ver 6.9.0  
 * B Hargrove	11/04/2011  Max ToMo\Yr calc for fixed exp mo is incorrect. 
 * 							As-of Nov, it is calculating 03/2013, it 
 * 							should be 03/2012 (Combo RegClassCd 10 has
 * 							Max Allow Reg months = 15).
 *							modify calcMaxToMoToYrMYr()
 *        					defect 11143 Ver 6.9.0
 * B Hargrove	11/07/2011  Check appropriate 'new plates desired' 
 * 							variable set in TTL008 when setting inventory
 * 							for Title.
 * 							Note: see also defect 10951 for 'must chg plt'
 * 							stuff.
 *							modify whetherIssueInvForTtl()
 *        					defect 10999 Ver 6.9.0
 *  B Hargrove 	11/08/2011  Can now renew multi-year official plates thru 
 * 							internet, and we now have Web Agent renew. 
 * 							We need to add check for 'IRENEW' and 
 * 							'WRENEW' to 'isLP()'. 
 * 							See also 6.5.0 defect 10548.
 * 							modify isLP()
 * 							defect 11152 Ver 6.8.2
 * K Harrell	11/10/2011	added check for PTO to check for Issue 
 * 							Inventory with No Registration
 * 							modify whetherIssueInvForTtl()
 *        					defect 10999 Ver 6.9.0
 * K Harrell	11/14/2011	Implement VTR275
 * 							modify calcFees(), calcVehInquiryFees()
 * 							defect 11052 Ver 6.9.0   
 *  B Hargrove 	12/06/2011  FromMo/Yr was being calculated wrongly in 
 * 							calcMaxToMoToYrMYr() for expired reg > 1 yr ago. 
 * 							FromMo/Yr is already calculated correctly in 
 * 							setFromMoFromYr(), so just comment out the
 * 							'bad re-calculating'. 
 * 							modify calcMaxToMoToYrMYr()
 * 							defect 11167 Ver 6.9.0
 *  B Hargrove 	01/10/2012  When renewing expired Combo, the total fees 
 * 							are correct, but the split btw Regis Penalty, 
 * 							Combo Plt, and Regis Emissions Fee is incorrect. 
 * 							modify calcReg(), calcRegSurChrg()
 * 							defect 11171 Ver 6.10.0
 *  B Hargrove 	02/01/2012  Exchanging to expired multi-year official plt 
 * 							is not setting minimum correctly. 
 * 							Clean out and correct setting minimum. 
 * 							modify calcMaxToMoToYrMYr(), setMinToMoToYr()
 * 							defect 11249 Ver 6.10.0
 * B Hargrove 	02/02/2012  We need to issue sticker even if ONLY 
 * 							'New Plates Desired' was selected 
 * 							(ie: plt age < 7, Dealer GDN not entered, not 
 * 							extending registration).
 * 							modify whetherIssueInvForTtl()
 * 							(moved up from Rel 6.9.0)
 * 							defect 11264 Ver 6.9.0
 * B Hargrove 	02/14/2012  If a plate is issued even if no registration 
 * 							is extended (ie: PTO Eligible, in window, plt
 * 							ge < 7, Dealer GDN not entered, no renewal) 
 * 							we still want to charge the $1 Automation fee.
 * 							NOTE that we still do not want to 'break'
 * 							defect 10999 which keeps this same situation
 * 							from 'working like an exchange' which would
 * 							give credit and charge regis.
 * 							modify testForRsnNotCallRegFeeSuite()
 * 							(moved up from Rel 6.9.0)
 * 							defect 11282 Ver 6.9.0
 *  B Hargrove 	03/07/2012  Title a new Cotton Plt vehicle (FxdExpMo 3),
 *   						minimum was too far in future. Defect 11249  
 * 							used MaxAllow, should use RegPeriodLngth.
 * 							Also, should use 'new Reg Eff Date' to start
 * 							reg period calc, not 'always Today' (ie: Renew
 * 							starts after existing reg expiration). 
 * 							modify calcMaxToMoToYrMYr(), setMinToMoToYr()
 * 							defect 11298 Ver 6.10.0
 *  B Hargrove 	03/12/2012  Title a Trk registered far into the future,
 *  						and exch to Combo (FxdExpMo 3, not eligible
 *   						for multi-year registration). 
 *   						This should have Minimum Reg Period = 'next 
 *   						March' (even if it is this month) and  
 * 							Maximum Reg Period no greater than next year.
 * 							modify calcMaxToMoToYrMYr(), setMinToMoToYr()
 * 							defect 11303 Ver 6.10.0
 *  B Hargrove 	03/27/2012  Check for null caRegisPenaltyFee, set to 0. 
 *  						Defect 11171 reconfigured reg calc for Combo,   
 * 							and this field can now be null in Subcon Renew.
 * 							(copied up from Rel 6.10.0) 
 * 							modify calcRegSurChrg()
 * 							defect 11331 Ver 6.10.0
 * --------------------------------------------------------------------- 
 */
/**
 * This class is used to calculate the fees for all transactions.
 *
 * @version	6.10.0 				03/27/2012
 * @author	Sunil Govindappa
 * @since 						08/22/2001 09:30:48 
 * --------------------------------------------------------------------- 
 */

public class Fees
{
	// array 
	// This graph variable array holds complete vehicle information along
	// with the fees information. It is updated as new fees are calculated.
	private CompleteTransactionData carrCompTransData[] =
		new CompleteTransactionData[2];

	// boolean 
	private boolean cbErrorHasLogged = false;

	// int 
	private int ciChrgFeeIndi = 0;
	private int ciCorrtnEffMo = 0;
	private int ciCorrtnEffYr = 0;
	private int ciCurrentMonths = 0;
	private int ciCurrMo = 0;
	private int ciCurrYr = 0;
	private int ciDieselFeePrcnt = 0;
	private int ciEffectiveExpDate = 0;
	private int ciEffectiveExpDatePlusOne = 0;
//	private int ciExit = 0;
//	private int ciExpFromMonths = 0;
	private int ciExpirationMonths = 0;
	private int ciExpMaxMonths = 0;
	private int ciExpMinMonths = 0;
	private int ciFCalcRow = 0;
	private int ciFromMo = 0;
	private int ciFromYr = 0;
	private int ciInvItmCount = 0;
//	private int ciIssueCancel = 0;
	private int ciItmQty = 1;
	private int ciMustChangePltIndi = 0;
	private int ciNoMfVeh = 0;
//	private int ciNoMoCharge = 0;
	private int ciOffHwyUseIndi = 0;
	private int ciPeriodOptRowsread = 0;
	private int ciPrmtEffDate = 0;
	// defect 6801 
	// private int ciRegCurrIndi = 0;
	// end defect 6801 
	private int ciRegEffDate = 0;
	private int ciRegExpReason = 0;
	private int ciRegWaivedIndi = 0;
	// defect 9126
	private int ciSpclPltExpMo = 0;
	private int ciSpclPltExpYr = 0;
	private int ciSpclPltFromMo = 0;
	private int ciSpclPltFromYr = 0;
	private int ciSpclPltNoMoCharge = 0;
	// end defect 9126
	private int ciTempFromYear = 0;
	private int ciTempVehGrossWt = 0;
	private int ciToMonth = 0;
	private int ciToMonthMax = 0;
	private int ciToMonthMin = 0;
	private int ciToYear = 0;
	private int ciToYearMax = 0;
	private int ciToYearMin = 0;
	private int ciTransGregorianDate = 0;
	private int ciUnregisterVehIndi = 0;
	private int ciVehGross100Wt = 0;
	// defect 10357
	private int ciCurrentRegExpMo = 0;
	private int ciCurrentRegExpYr = 0;
	// end defect 10357

	// Object 
	private Dollar caBaseRegFee = null;
//	private Dollar caCrdtRemaining = null;
//	private Dollar caDieselFee = null;
	private Dollar caFeeTotalMax = null;
	private Dollar caFeeTotalMin = null;
	private Dollar caItmPrice = null;
	private Dollar caRegisPenaltyFee = null;

	private AccountCodesData caAcctCdData = null;
	private CommonFeesData caCommFeesData = null;
	private CountyCalendarYearData caCntyCalndrYr = null;
//	private ItemCodesData caItemCdsData = null;
	private OfficeIdsData caWSOfficeIds = null;
	private PassengerFeesData caPassFeeData = null;
	private PlateTypeData caPltTypeData = null;
	private RegistrationWeightFeesData caRegWtFeesData = null;
	private SalesTaxCategoryData caSalesTaxCatData = null;
	private VehicleDieselTonData caVehDislTonData = null;
//	private VehMiscData caVehMisc = null;
	// defect 10685
	private RegistrationClassFeeGroupData caRegClassFeeGrpData = null;
	// end defect 10685

	// defect 9084
	// rename objects for consistency	
	//private MFVehicleData caMFVeh0 = null;
	//private MFVehicleData caMFVeh1 = null;
	//private RegistrationData caReg0 = null;
	//private RegistrationData caReg1 = null;
	//private TitleData caTtl0 = null;
	//private TitleData caTtl1 = null;
	//private VehicleData caVeh0 = null;
	//private VehicleData caVeh1 = null;
	//private VehMiscData caVehMisc0 = null;
	//private VehMiscData caVehMisc1 = null;
	private MFVehicleData caMFVehOrig = null;
	private MFVehicleData caMFVehUpdt = null;
	private RegistrationData caRegOrig = null;
	private RegistrationData caRegUpdt = null;
//	private SpecialPlatesRegisData caSpclPltRegOrig = null;
	private SpecialPlatesRegisData caSpclPltRegUpd = null;
//	private TitleData caTtlOrig = null;
	private TitleData caTtlUpdt = null;
	private VehicleData caVehOrig = null;
	private VehicleData caVehUpdt = null;
//	private VehMiscData caVehMiscOrig = null;
	private VehMiscData caVehMiscUpdt = null;
	// end defect 9084

	// String
	private String csAcctItmCd = null;
	private String csExpMoYrMax = null;
	private String csExpMoYrMin = null;
	private String csTransCd = null;
	private String csTxtRegMoSold = null;
	// defect 9126
	private String csTxtSpclPltMoSold = null;
	// end defect 9126

	// Vector
	private Vector cvMiscaRegTransCds = null;
	private Vector cvPeriodOpt = null;
	private Vector cvRegOnlyTransCds = null;
	private Vector cvTtlTypsTransCds = null;

	// Constants
	private final static int CITATION_CHG_PNLTY = 3;
	private final static int VALID_REASON = 1;
	private final static int COUNTY = 3;
	private final static int HEADQUARTERS = 1;
	private final static int NO_COMMON_FEES_ROW_FOUND = 2011;
	private final static int ORIG = 0;
	private final static int REGION = 2;
	private final static int REGIS_EMI_START_DT = 20010901;
	private final static int SPECIAL_PLATES = 291;
	private final static int UPDT = 1;
	private final static Dollar ZERO_DOLLAR = new Dollar(0.0);
	// defect 9791
	private final static String DEALER_CD = "D";
	// end defect 9791
	

	// defect 10646 
	private final static String DEALER_SELLER_FINANCED_CD = "S";
	// end defect 10646 

	// defect 9498
	private final static String COTTON_ACCT = "SPL0036";
	private final static String COTTON_PLT = "COTTON";
	// end defect 9498
	
	// defect 11054
	private final static int VALID_REASON_CD = 1;
	// end defect 11054
	// defect 10772
	private final static int INVALID_REASON_CD = 2;
	// end defect 10772

	/**
	 *  Fees constructor which populates the Transaction Code vectors.
	 */
	public Fees()
	{
//		caVehMisc = new VehMiscData();
		//Populate Title transaction code vector
		cvTtlTypsTransCds = new Vector();
		cvTtlTypsTransCds.add(TransCdConstant.TITLE);
		cvTtlTypsTransCds.add(TransCdConstant.NONTTL);
		cvTtlTypsTransCds.add(TransCdConstant.REJCOR);
		cvTtlTypsTransCds.add(TransCdConstant.DTANTD);
		cvTtlTypsTransCds.add(TransCdConstant.DTANTK);
		cvTtlTypsTransCds.add(TransCdConstant.DTAORD);
		cvTtlTypsTransCds.add(TransCdConstant.DTAORK);
		cvTtlTypsTransCds.add(TransCdConstant.CORTTL);
		cvTtlTypsTransCds.add(TransCdConstant.ADLSTX);

		//Populate Registration transaction code vector
		cvRegOnlyTransCds = new Vector();
		cvRegOnlyTransCds.add(TransCdConstant.RENEW);
		cvRegOnlyTransCds.add(TransCdConstant.REPL);
		cvRegOnlyTransCds.add(TransCdConstant.SBRNW);
		cvRegOnlyTransCds.add(TransCdConstant.EXCH);
		cvRegOnlyTransCds.add(TransCdConstant.DUPL);
		cvRegOnlyTransCds.add(TransCdConstant.PAWT);
		cvRegOnlyTransCds.add(TransCdConstant.CORREG);
		cvRegOnlyTransCds.add(TransCdConstant.CORREGX);
		//For phase 2
		cvRegOnlyTransCds.add(TransCdConstant.IRENEW);
		// defect 10772
		cvRegOnlyTransCds.add(TransCdConstant.WRENEW);
		// end defect 10772

		//Populate Misc Registration transaction code vector
		cvMiscaRegTransCds = new Vector();
		cvMiscaRegTransCds.add(TransCdConstant.TOWP);

		// defect 10133 
		// Restore - earlier deleted w/ defect 8268
		cvMiscaRegTransCds.add(TransCdConstant.PDC);
		cvMiscaRegTransCds.add(TransCdConstant.TDC);
		cvMiscaRegTransCds.add(TransCdConstant.RPLPDC);
		cvMiscaRegTransCds.add(TransCdConstant.RPLTDC);
		//		cvMiscaRegTransCds.add(TransCdConstant.BPM);
		//		cvMiscaRegTransCds.add(TransCdConstant.BTM);
		//		cvMiscaRegTransCds.add(TransCdConstant.RPNM);
		//		cvMiscaRegTransCds.add(TransCdConstant.RTNM);
		//		// defect 9831 
		//		cvMiscaRegTransCds.add(TransCdConstant.RPBPM);
		//		cvMiscaRegTransCds.add(TransCdConstant.RPBTM);
		//		cvMiscaRegTransCds.add(TransCdConstant.RPRPNM);
		//		cvMiscaRegTransCds.add(TransCdConstant.RPRTNM);
		// end defect 9831
		// end defect 10133 

		// end defect 8268
		cvMiscaRegTransCds.add(TransCdConstant.TAWPT);
		cvMiscaRegTransCds.add(TransCdConstant.PT72);
		cvMiscaRegTransCds.add(TransCdConstant.PT144);
		cvMiscaRegTransCds.add(TransCdConstant.PT30);
		cvMiscaRegTransCds.add(TransCdConstant.NRIPT);
		cvMiscaRegTransCds.add(TransCdConstant.NROPT);
		cvMiscaRegTransCds.add(TransCdConstant.OTPT);
		cvMiscaRegTransCds.add(TransCdConstant.FDPT);
		// defect 10491 
		//cvMiscaRegTransCds.add(TransCdConstant.PT24);
		cvMiscaRegTransCds.add(TransCdConstant.PRMDUP);
		// end defect 10491   
		caBaseRegFee = new Dollar(0.0);
	}
	/**
	 * Add one month and adjust for year change.  
	 * 
	 */
	public void addOneMonthForSpclPltExp()
	{
		//Adjustment for Dec month is handled here.
		ciSpclPltFromMo = (ciSpclPltFromMo % 12) + 1;
		ciTempFromYear = (Math.max(ciSpclPltFromYr, (ciCurrYr - 1)));
		if (ciSpclPltFromMo == 1)
		{
			ciSpclPltFromYr = ciTempFromYear + 1;
		}
	}
	/**
	 * Adjust for Additional Diesel Fee.
	 * 
	 */
	private void adjAddlDislFee()
	{
		//Calculate diesel fee percent .
		//exempt certain types of vehicles under a certain weight (2 tons)
		if (caCommFeesData.getDieselChrgTonIndi() == 1)
		{
			MFVehicleData laMFVD =
				carrCompTransData[ciFCalcRow].getVehicleInfo();
			RegistrationData laRegData = laMFVD.getRegData();
			VehicleData laVehData = laMFVD.getVehicleData();
			int liRTSEffDate = laRegData.getRegEffDt();
			int liRegClassCd = laRegData.getRegClassCd();
			String lsRegClassCd = String.valueOf(liRegClassCd);
			Dollar laVehTon = laVehData.getVehTon();
			caVehDislTonData =
				VehicleDieselTonCache.getVehDieselTon(
					lsRegClassCd,
					laVehTon,
					liRTSEffDate);
			// defect 8690
			// if null, do look-up with today's date
			if (caVehDislTonData == null)
			{
				liRTSEffDate = new RTSDate().getYYYYMMDDDate();
				caVehDislTonData =
					VehicleDieselTonCache.getVehDieselTon(
						lsRegClassCd,
						laVehTon,
						liRTSEffDate);
			}
			if (caVehDislTonData != null)
			{
				ciDieselFeePrcnt =
					(carrCompTransData[ciFCalcRow]
						.getVehicleInfo()
						.getVehicleData())
						.getDieselIndi()
						* caVehDislTonData.getDieselFeePrcnt();
			}
			// end defect 8690					
		}
		else
		{
			ciDieselFeePrcnt =
				(carrCompTransData[ciFCalcRow]
					.getVehicleInfo()
					.getVehicleData())
					.getDieselIndi()
					* caCommFeesData.getDieselFeePrcnt();
		}
		Dollar laAnnualRegFee = new Dollar(0.0);
		if (carrCompTransData[ciFCalcRow].getAnnualRegFee() != null)
		{
			laAnnualRegFee =
				carrCompTransData[ciFCalcRow].getAnnualRegFee();
		}
		carrCompTransData[ciFCalcRow].setAnnualDieselFee(
			(new Dollar(ciDieselFeePrcnt / 100.0))
				.multiplyNoRound(laAnnualRegFee)
				.round());
		carrCompTransData[ciFCalcRow].setDieselFee(
			(new Dollar(ciDieselFeePrcnt / 100.0))
				.multiplyNoRound(caBaseRegFee)
				.round());
	}
	/**
	 * Adjust Disable Placard Fee.  
	 * 
	 * If two Placards are issued, then double price and quantity.
	 * MF cannot accept duplicate TR_FDS_Detail records.  
	 */
	public void adjDisPlcrdFee()
	{
		// defect 11050 
		// defect 8328
		// added check for null objects 
		if (carrCompTransData[UPDT] != null
			&& carrCompTransData[UPDT].getTimedPermitData() != null
			&& carrCompTransData[UPDT]
			     .getTimedPermitData().getNumPlacardsIssued() >1)
			// end defect 8328
		{
			ciItmQty = carrCompTransData[UPDT]
			     .getTimedPermitData().getNumPlacardsIssued();
			
			caItmPrice = caItmPrice.multiply(new Dollar(ciItmQty));
		}
		// end defect 11050 
	}

	/**
	 * Adjust Maximum ToMonth\Year for Special Plate
	 * Rule is that Maximum ToMonth\Year will be the maximum of 'maximum
	 * months as calculated already by the registration rules' OR
	 * the 'current expiration of the Special Plate'
	 * If 'Needs Program = 'L' (ie: a plate manufactured outside the 
	 * normal plate manufacture process such as State Judge or State
	 * Official), there is no choice on 'Min-Max Year', just use year
	 * that is on the Special Plate).
	 */
	private void adjMaxForSpclPlt()
	{
		if (caRegUpdt != null
			&& caRegUpdt.getRegPltCd() != null
			&& PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd()))
		{
			SpecialPlatesRegisData laSpecialPlatesRegData =
				caMFVehUpdt.getSpclPltRegisData();
			if (laSpecialPlatesRegData != null)
			{
				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
//				PlateTypeData laPltTypeData =
//					PlateTypeCache.getPlateType(
//						caRegUpdt.getRegPltCd());
				ciSpclPltExpMo = laSpecialPlatesRegData.getPltExpMo();
				ciSpclPltExpYr = laSpecialPlatesRegData.getPltExpYr();
				// end defect 9864

				// defect 9689
				// Do not adjust for Vendor Plate
				if (!PlateTypeCache
					.isVendorPlate(caRegUpdt.getRegPltCd()))
				{
					// defect 10548
					// us 'isLP()' to include Multi-Year Official Plates			
					//if (laPltTypeData
					//	.getNeedsProgramCd()
					//	.equals(SpecialPlatesConstant.LP_PLATE))
					if (isLP())
					{
						// end defect 10548			
						ciToMonth = ciToMonthMin;
						ciToYear = ciToYearMin;
					}
					else
					{

						if (ciSpclPltExpYr > ciToYear)
						{
							ciToMonth = ciSpclPltExpMo;
							ciToYear = ciSpclPltExpYr;
						}
						else if (
							ciSpclPltExpYr == ciToYear
								&& ciSpclPltExpMo > ciToMonth)
						{
							ciToMonth = ciSpclPltExpMo;
						}
					}
				}

				// For Vendor Plate, in window, do not allow to 
				// renew if Vendor Plate will be expired at beginning of
				// new reg period.
				// defect 10357
				// Allow renew of expired VP. Will (at least) prorate.
				//else if (testIfWithinWnd())
				//{
				//	if (CommonValidations
				//		.isPlateExpired(
				//			ciSpclPltExpMo,
				//			ciSpclPltExpYr,
				//			ciEffectiveExpDate))
				//	{
				//		ciToMonth = ciToMonthMin;
				//		ciToYear = ciToYearMin;
				//	}
				//}
				// end defect 10357
				// end defect 9689
			}
		}

	}

	/**
	 * Adjust Minimum ToMonth\Year for Special Plate
	 * Rule is that Minimum ToMonth\Year will be the maximum of 'minimum
	 * months as calculated already by the registration rules' OR
	 * the 'moving to' Special Plate expiration.
	 */
	private void adjMinForSpclPlt()
	{
		// defect 9689
		// Do not adjust for Vendor Plate
		// defect 10357
		// Can adjust for Vendor Plate, ONLY IF Plate Term = One Year
		if (caRegUpdt != null
			&& caRegUpdt.getRegPltCd() != null
			&& PlateTypeCache.isSpclPlate(
				caRegUpdt.getRegPltCd())
			//	&& !PlateTypeCache.isVendorPlate(caRegUpdt.getRegPltCd()))
			&& caMFVehUpdt.getSpclPltRegisData().getPltValidityTerm()
				== 1)
		{
			// end defect 10357
			// end defect 9689
			SpecialPlatesRegisData laSpecialPlatesRegData =
				caMFVehUpdt.getSpclPltRegisData();
			if (laSpecialPlatesRegData != null)
			{
				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
				ciSpclPltExpMo = laSpecialPlatesRegData.getPltExpMo();
				ciSpclPltExpYr = laSpecialPlatesRegData.getPltExpYr();
				// end defect 9864 

				if (ciSpclPltExpYr > ciToYear)
				{
					ciToMonth = ciSpclPltExpMo;
					ciToYear = ciSpclPltExpYr;
				}
				else if (
					ciSpclPltExpYr == ciToYear
						&& ciSpclPltExpMo > ciToMonth)
				{
					ciToMonth = ciSpclPltExpMo;
				}
			}
		}
	}

	/**
	 * Apply multiyear adjustment for fees.
	 * This method calculates the 'qualifier' used to multiply the
	 * Reg Additonal Fees (reflect, auto, etc) based on the 'integer
	 * number of years' of registration.
	 */
	private void applyMultiYrAdj()
	{
		// defect 6001 
		// Divide by 0 error  on Repl of RegClassCd 39
		// Added test for !RegPeriodLngth() == 0
		double ldItmQty = 0.0;
		//	defect 8104 
		if (caCommFeesData == null)
		{
			calcCommnFees();
		}
		// end defect 8104
		// defect 8471
		// Process this two different ways, depending on if
		// RegPeriodLngth < 12 
		if (!(caCommFeesData.getRegPeriodLngth() == 0))
		{
			if (caCommFeesData.getRegPeriodLngth() < 12)
			{
				ldItmQty = 1.0;
			}
			else
			{
				ldItmQty =
					(carrCompTransData[UPDT].getNoMoChrg()
						- carrCompTransData[ORIG].getNoMoChrg())
						/ caCommFeesData.getRegPeriodLngth();
			}
			if (ldItmQty > 1.0)
			{
				ciItmQty = (new Double(ldItmQty)).intValue();
			}
			else
			{
				ciItmQty = 1;
			}
			caItmPrice =
				((new Dollar(ciItmQty)).multiplyNoRound(caItmPrice))
					.round();
		}
		// end defect 6001, 8471 
	}
	/**
	 * Assign DisableCounty Fees
	 * 
	 */
	private void asgnDisCntyFees()
	{
		if (csTransCd.equals(TransCdConstant.EXCH))
		{
			carrCompTransData[UPDT].setDisableCtyFees(1);
		}
		//Test trans code = title, non-titled, reject correction, or DTA.
		//TEST if registration is current
		else if (
			!((csTransCd.equals(TransCdConstant.TITLE)
				|| csTransCd.equals(TransCdConstant.NONTTL)
				|| csTransCd.equals(TransCdConstant.REJCOR)
				|| csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
				&& !(isRegExpired())
				&& setDisCntyForTtl()))
		{
			carrCompTransData[UPDT].setDisableCtyFees(0);
		}
	}
	/**
	 * Assign Fiscal Year.
	 * 
	 */
	private void asgnFscalYr()
	{
		if (csTransCd.equals(TransCdConstant.SBRNW))
		{
			carrCompTransData[UPDT].setFsclYr(
				(caRegUpdt.getRegExpMo() == 1)
					? (caRegUpdt.getRegExpYr() - 1)
					: caRegUpdt.getRegExpYr());
			carrCompTransData[ORIG].setFsclYr(
				(caRegOrig.getRegExpMo() == 1)
					? (caRegOrig.getRegExpYr() - 1)
					: caRegOrig.getRegExpYr());
		}
		else if (
			caRegOrig.getRegExpMo() == 1
				&& ciCurrMo == 1
				&& caRegOrig.getRegExpYr() == ciCurrYr)
		{
			carrCompTransData[UPDT].setFsclYr(ciCurrYr - 1);
			carrCompTransData[ORIG].setFsclYr(
				carrCompTransData[UPDT].getFsclYr());
		}
		else
		{
			carrCompTransData[UPDT].setFsclYr(ciCurrYr);
			carrCompTransData[ORIG].setFsclYr(
				carrCompTransData[UPDT].getFsclYr());
		}
	}
	/**
	 * This method fetches the fees from the AccountCodesData
	 * 
	 */
	private void asgnMiscFees()
	{
		selectFrmAcctCdsTbl();
		int liNoChrgIndi =
			carrCompTransData[ciFCalcRow].getNoChrgIndi();
		Dollar laMiscFee = caAcctCdData.getMiscFee();
		if (laMiscFee.compareTo(ZERO_DOLLAR) > 0)
		{
			if (liNoChrgIndi == 1)
			{
				caItmPrice = new Dollar(0.0);
			}
			else
			{
				caItmPrice = caAcctCdData.getMiscFee();
			}
			ciItmQty = 1;
			// Adjust Disabled Placard Fee if Two Issued
			// MF cannot accept duplicate TR_FDS_Detail records 
			adjDisPlcrdFee();
			
			asgnResToFeesData();
		}
	}
	/**
	 * Assign results to FeesData object.
	 * 
	 */
	private void asgnResToFeesData()
	{
		selectFrmAcctCdsTbl();
		String lsAcctItmCdDesc = caAcctCdData.getAcctItmCdDesc();
		int liCrdtAllowedIndi = caAcctCdData.getCrdtAllowdIndi();
		// Populate the FeesData object and add it to 
		// carrCompTransData[UPDT] object.
		FeesData laFeesData = new FeesData();
		if (caItmPrice != null)
		{
			laFeesData.setItemPrice(caItmPrice.round());
		}
		laFeesData.setDesc(lsAcctItmCdDesc);
		laFeesData.setItmQty(ciItmQty);
		laFeesData.setAcctItmCd(csAcctItmCd);
		laFeesData.setCrdtAllowedIndi(liCrdtAllowedIndi);
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData.getVectFees();
		lvFeesData.add(laFeesData);

	}
	/**
	 * 
	 * asgnSpclFeesToFeesData()
	 * 
	 *
	 */
	private void asgnSpclFeesToFeesData()
	{
		// Populate the FeesData object and add it to 
		// carrCompTransData[0] object.
		FeesData laFeesData = new FeesData();
		if (caItmPrice != null)
		{
			laFeesData.setItemPrice(caItmPrice.round());
		}
		selectFrmAcctCdsTbl();
		laFeesData.setDesc(caAcctCdData.getAcctItmCdDesc());
		laFeesData.setItmQty(1);
		laFeesData.setAcctItmCd(csAcctItmCd);
		laFeesData.setCrdtAllowedIndi(caAcctCdData.getCrdtAllowdIndi());
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData.getVectFees();
		lvFeesData.add(laFeesData);

	}

	/**
	 * ASSIGN ToMonth/Year = current exp plus max allowable reg months.
	 * 
	 */
	private void asgnToMoToYrCEPlusMaxRegMo()
	{
		ciToMonth =
			(caRegOrig.getRegExpMo()
				+ caCommFeesData.getMaxallowbleRegMo()
				- 1)
				% 12
				+ 1;
		//Assign TempFromYear to not more than one year ago
		ciTempFromYear =
			(caRegOrig.getRegExpMo() >= ciCurrMo)
				? (ciCurrYr - 1)
				: ciCurrYr;
		ciTempFromYear =
			Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
		ciToYear =
			(new Double((caRegOrig.getRegExpMo()
				+ caCommFeesData.getMaxallowbleRegMo()
				- 1)
				/ 12.0))
				.intValue();
		ciToYear = ciTempFromYear + ciToYear;
	}
	/**
	 * ASSIGN ToMonth/Year = now plus max allowable reg months-1.
	 * 
	 */
	private void asgnToMoToYrNowPlusMaxRegMoMinusOne()
	{
		ciToMonth =
			((ciCurrMo + caCommFeesData.getMaxallowbleRegMo() - 2)
				% 12)
				+ 1;
		ciToYear =
			(new Double((ciCurrMo
				+ caCommFeesData.getMaxallowbleRegMo()
				- 2)
				/ 12.0))
				.intValue();
		ciToYear = ciCurrYr + ciToYear;
	}
	/**
	 * This method sets the ToMonth/ToYear to Now 
	 * plus reg period length - 1 .
	 */
	private void asgnToMoYrNowPlusRplMinusOne()
	{
		ciToMonth =
			((ciCurrMo + caCommFeesData.getRegPeriodLngth() - 2) % 12)
				+ 1;
		if (caCommFeesData.getRegPeriodLngth() != 0)
		{
			// defect 8404
			// This is strictly a date calculation. 
			// Should be dividing by 12 months, not Reg Period Length.
			ciToYear =
				(new Double((ciCurrMo
					+ caCommFeesData.getRegPeriodLngth()
					- 2)
				/// caCommFeesData.getRegPeriodLngth()))
					/ 12)).intValue();
			// end defect 8404
		}
		else
		{
			ciToYear = 0;
		}
		ciToYear = ciCurrYr + ciToYear;
	}
	/**
	 * Calculate the Title Fees 
	 * 
	 */
	private void assignTtl()
	{
		csAcctItmCd = AcctCdConstant.TITLE;
		if ((carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
			.getChrgTtlFeeIndi()
			== 1)
		{
			asgnMiscFees();
		}
	}
	/**
	 * Calculate Automation Fee.
	 * 
	 */
	private void calcAutomation()
	{
		// defect 9085
		if (carrCompTransData[ciFCalcRow].getInvItemCount() > 0
			|| (caSpclPltRegUpd != null
				&& caSpclPltRegUpd.getMfgSpclPltIndi() == 1))
		{
			retrieveCntyCalYr();
			if (caCntyCalndrYr != null)
			{
				// defect 10685
				// After certain date, all counties pay automation fee
				if ((caCntyCalndrYr.getCntySizeCd().trim())
					.equals("L") ||
						carrCompTransData[ciFCalcRow].getVehicleInfo().
						getRegData().getRegEffDt() >=
							SystemProperty.getFeeSimplifyStartDate().
								getYYYYMMDDDate())
				{
					// end defect 10685
					csAcctItmCd = "AUTOMATE";
					selectFrmAcctCdsTbl();
					if (((caAcctCdData.getMiscFee())
						.compareTo(ZERO_DOLLAR))
						> 0)
					{
						caItmPrice = caAcctCdData.getMiscFee();
						applyMultiYrAdj();
						asgnResToFeesData();
					}
					updateCustActlRegFee();
				}
			}
		}
		// end defect 9085
	}
	/**
	 * Calculate Child Safety.
	 * 
	 */
	private void calcChildSafty()
	{
		// Assign fee only if:
		//  - inventory is issued with this registration  &&
		//  - this is not an even registration exchange  &&
		//  - DisableCntyFees =  0
		// defect 9101
		// If originally Exempt, calculate CSF even if number of
		// months to charge is the same
		if (carrCompTransData[ciFCalcRow].getInvItemCount() > 0
			&& (carrCompTransData[ORIG].getNoMoChrg()
				!= carrCompTransData[UPDT].getNoMoChrg()
				|| caRegOrig.getExmptIndi() == 1)
			&& carrCompTransData[ciFCalcRow].getDisableCtyFees() == 0)
		{
			// end defect 9101
			calcCommnFees();

			if (caCommFeesData.getAddOnFeeExmptIndi() == 0)
			{
				retrieveCntyCalYr();

				if (caCntyCalndrYr != null
					&& (caCntyCalndrYr.getChildSaftyFee()).compareTo(
						ZERO_DOLLAR)
						> 0)
				{
					caItmPrice = caCntyCalndrYr.getChildSaftyFee();
					applyMultiYrAdj();
					csAcctItmCd = "CSF";
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
	}
	/**
	 * Look up Common Fees 
	 */
	private void calcCommnFees()
	{
		MFVehicleData laMFVD =
			carrCompTransData[ciFCalcRow].getVehicleInfo();
		RegistrationData laRegData = laMFVD.getRegData();
		int liRTSEffDate = laRegData.getRegEffDt();
		int liRegClassCd = laRegData.getRegClassCd();
		// defect 11003
		// If no RegClassCd change and no weight change,
		// do 'even exchange' (use new RegEffDt for both old and new regis)
		if (caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd() &&
			caRegOrig.getVehGrossWt() == caRegUpdt.getVehGrossWt())
		{
			liRTSEffDate = caRegUpdt.getRegEffDt();
		}
		// end defect 11003
		//Access CommonFeesCache only if CommonFee result is not valid
		// defect 6208
		// Always do lookup to get Common Fees from cache 
		//if (cCommFeesData == null ||
		//    (!(liRegClassCd == cCommFeesData.getRegClassCd() &&
		//       liRTSEffDate >= cCommFeesData.getRTSEffDate() &&
		//       liRTSEffDate <= cCommFeesData.getRTSEffEndDate())))
		//{
		//	  cCommFeesData = CommonFeesCache.getCommonFee(liRegClassCd, liRTSEffDate);
		//}

		caCommFeesData =
			CommonFeesCache.getCommonFee(liRegClassCd, liRTSEffDate);

		if (caCommFeesData == null)
			// Lookup w/ current date if no row found, and log message to
			// RTSAPP.LOG and VSAM error report to track this. Log message
			// only once (calcCommnFees is called multiple times). 
			//else
		{
			int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
			caCommFeesData = CommonFeesCache.getCommonFee(liRegClassCd,
				// defect 10214 
			// use max of current date or effective date					
			//liRTSCurrDate);
			Math.max(liRTSCurrDate, liRTSEffDate));
			// end defect 10214

			// defect 8112
			// only log error (once) if cases where MF record found
			//if (!cbErrorHasLogged)
			if (!cbErrorHasLogged && ciNoMfVeh > 0)
			{
				cbErrorHasLogged = true;
				Log.write(
					Log.SQL_EXCP,
					this,
					laRegData.getRegEffDt()
						+ CommonConstant.STR_SPACE_ONE
						+ laMFVD.getTitleData().getDocNo()
						+ CommonConstant.STR_SPACE_ONE
						+ carrCompTransData[UPDT].getTransCode()
						+ ErrorMessagesCache
							.getErrMsg(NO_COMMON_FEES_ROW_FOUND)
							.getErrMsgDesc());

				//RTSException leRTSException =
				//	new RTSException(NO_COMMON_FEES_ROW_FOUND);

				// defect 8112
				// do not log to MF errlog
				//ErrorMessagesData laErrorMsgData = 
				//ErrorMessagesCache.getErrMsg(NO_COMMON_FEES_ROW_FOUND);

				//MFErrLog laMFErrLog = new MFErrLog();
				//laMFErrLog.setErrorMsgData(laErrorMsgData);
				//Thread t = new Thread(laMFErrLog);
				//t.start();
				// end defect 8112
			}

		}

		if (caCommFeesData != null)
		{
			// defect 9633
			// If RegClassCd is not transferable, use Today's date
			if (caCommFeesData.getRegTrnsfrIndi() == 0)
			{
				int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
				caCommFeesData =
					CommonFeesCache.getCommonFee(liRegClassCd,
					// defect 10214 
		// use max of current date or effective date					
		//liRTSCurrDate);
	Math.max(liRTSCurrDate, liRTSEffDate));
				// end defect 10214
			}
			// end defect 9633

			//Used in REG029 date validations
			// defect 8900
			// This should be setting on ciFCalcRow, not 0
			//carrCompTransData[0].setFxdExpMo(
			//	caCommFeesData.getFxdExpMo());
			// defect 8404				
			//carrCompTransData[0].setRegPeriodLngth(
			//	caCommFeesData.getRegPeriodLngth());
			// defect 8404				
			carrCompTransData[ciFCalcRow].setFxdExpMo(
				caCommFeesData.getFxdExpMo());
			carrCompTransData[ciFCalcRow].setRegPeriodLngth(
				caCommFeesData.getRegPeriodLngth());
			// defect 8900				
		}
		// end defect 6208
	}
	/**
	 * Calculate correction fees
	 * 
	 */
	private void calcCorrectionFee()
	{
		//ASSIGN RegEffDate = RegEffDate from mainframe
		ciRegEffDate = caRegOrig.getRegEffDt();
		caRegUpdt.setRegEffDt(ciRegEffDate);
		//Init Core Fee module 
		ciFCalcRow = UPDT;
		carrCompTransData[ORIG].setCustActualRegFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(new Dollar(0.0));
		calcToknTrlr();
		// Test if registration expiration month has not changed
		// Test if vehicles weight has not changed
		if ((caRegUpdt.getRegExpMo() == caRegOrig.getRegExpMo())
			&& testIfVehWgtNotChnged()
			&& testIfVehYrMdlNotChanged())
		{
			// empty code block
		}
		else
		{
			//Calc reg correction 
			//Calc reg fees only if this is Apprehended (CORREGX) 
			if (csTransCd.equals(TransCdConstant.CORREGX))
			{
				setFromMoFromYr();
				//ASSIGN FCalc{#} fees vars for renew
				//ASSIGN FCalc{#}.FscalYr
				asgnFscalYr();
				if (ciChrgFeeIndi == 0)
				{
					carrCompTransData[UPDT].setNoChrgIndi(1);
				}
				carrCompTransData[UPDT].setMailFeeApplies(1);
				calcCommnFees();
				setMinToMoToYr();
				//Set up and call fee calc for renew
				//ASSIGN FCalc{0}.NoMoCharge .
				carrCompTransData[UPDT].setNoMoChrg(
					Math.max(
						0,
						(ciToYear * 12 + ciToMonth)
							- (ciFromYr * 12 + ciFromMo)
							+ 1));
				//Call fee calc procs needed for renew
				// defect 8900
				// Check Charge Fee Indi, not Exempt Indi				 		  
				//Test if exempt indicator is set 
				//if (caReg1.getExmptIndi() != 1)
				if (ciChrgFeeIndi == 1)
				{
					// end defect 8900					
					//Init Core Fee module 
					ciFCalcRow = UPDT;
					carrCompTransData[ORIG].setCustActualRegFee(
						new Dollar(0.0));
					carrCompTransData[UPDT].setCustActualRegFee(
						new Dollar(0.0));
					// defect 8516
					// Use lookup to CommonFees.EmissionPrcnt			
					//if (caReg0.getRegClassCd() != 10)
					int liRegClassCd = caRegOrig.getRegClassCd();
					int liRTSCurrDate = caRegOrig.getRegEffDt();
					// defect 11003
					// If no RegClassCd change and no weight change,
					// do 'even exchange' (use new RegEffDt for both old and new regis)
					if (caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd() &&
						caRegOrig.getVehGrossWt() == caRegUpdt.getVehGrossWt())
					{
						liRTSCurrDate = caRegUpdt.getRegEffDt();
					}
					// end defect 11003
					caCommFeesData =
						CommonFeesCache.getCommonFee(
							liRegClassCd,
							liRTSCurrDate);
					if (caCommFeesData == null)
					{
						(
							carrCompTransData[ORIG]
								.getRegTtlAddlInfoData())
								.setNoChrgRegEmiFeeIndi(
							1);
					}
					else
					{
						if ((caCommFeesData.getEmissionsPrcnt())
							.compareTo(ZERO_DOLLAR)
							== 0)
						{
							(
								carrCompTransData[ORIG]
									.getRegTtlAddlInfoData())
									.setNoChrgRegEmiFeeIndi(
								1);
						}
						else
						{
							(
								carrCompTransData[ORIG]
									.getRegTtlAddlInfoData())
									.setNoChrgRegEmiFeeIndi(
								0);
						}
					}
					// end defect 8516

					regFeeSuite();
					setMFBaseActlFee();
					setNoOfMoRegCrdtMsg();
					setCrdtRmng();
				}
				// defect 8900
				// Set Reg Amt = 0 for Exempt Indi and No Charge Fee Indi 				
				else if (
					caRegOrig.getExmptIndi() == 1
						&& ciChrgFeeIndi == 0)
				{
					csAcctItmCd = "CORREG";
					caItmPrice = new Dollar(0.0);
					asgnResToFeesData();
					return;
				}
				// end defect 8900
			}
		}
	}

	/**
	 * Calculate the non-Special Plate Cotton Plate Application fee. 
	 * 
	 */
	private void calcCottonPltApplFee()
	{
		// Assign plate application fee if new application 
		if (caRegUpdt.getRegPltCd().equals(COTTON_PLT))
		{
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(COTTON_PLT);
			if (laPltTypeData != null)
			{
				if (testNoCurrReg()
					|| !caRegOrig.getRegPltCd().equals(COTTON_PLT)
					|| csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.REJCOR)
					|| csTransCd.equals(TransCdConstant.DTAORD)
					|| csTransCd.equals(TransCdConstant.DTAORK))
				{
					csAcctItmCd = COTTON_ACCT;
					selectFrmAcctCdsTbl();
					caItmPrice = laPltTypeData.getFirstSetApplFee();
					ciItmQty = 1;
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
	}

	/**
	 * Calculate the Buyer Tag fee. 
	 * 
	 */
	private void calcBuyerTagFee()
	{
		// defect 9799 
		// Do not charge Fee if TtlSignDate prior to BuyerTagStartDate
		// If 'GDN' was entered, this is a Title transaction w/ GDN and
		// we should charge the Buyer Tag Fee.
		if (caTtlUpdt.getTtlSignDate()
			>= SystemProperty.getBuyerTagStartDate().getYYYYMMDDDate()
			&& carrCompTransData[ciFCalcRow]
				.getRegTtlAddlInfoData()
				.getChrgBuyerTagFeeIndi()
				== 1)
		{
			csAcctItmCd = "BUYERTAG";
			selectFrmAcctCdsTbl();
			caItmPrice = caAcctCdData.getMiscFee();
			ciItmQty = 1;
			asgnResToFeesData();
			updateCustActlRegFee();
		}
		// end defect 9799 
	}

	/**
	 * Calculate fees for dealer title event
	 */
	private void calcDlrTtl()
	{
		// defect 7538
		// Title for Off Hiway, Reg Waived, Spcl Plt Prog, 

		// defect 8900
		// Remove check for Exempts (DTA cannot do Exempts)
		//if (ciRegWaivedIndi == 1
		//	|| ciOffHwyUseIndi == 1
		//	|| (caVehMisc1 != null
		//		&& caVehMisc1.getSpclPltProgIndi() == 1)
		//	|| caReg1.getRegClassCd() == 39)
		if (ciRegWaivedIndi == 1
			|| ciOffHwyUseIndi == 1
			|| (caVehMiscUpdt != null
				&& caVehMiscUpdt.getSpclPltProgIndi() == 1))
		{
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			asgnFscalYr();
			callFeeCalcTtlMthds();
		}
		// end defect 8900			
		// end defect 7538, 7972
		else
		{
			// Set RegEffDate for Title (may change later for Title 
			// w/ Renewl/Exchange).
			// Test if new Vehicle
			if (ciNoMfVeh == 0
				|| (caRegOrig.getRegExpMo() == 0
					&& caRegOrig.getRegExpYr() == 0)
				|| isRegExpired())
			{
				ciRegEffDate = ciTransGregorianDate;
			}
			else
			{
				ciRegEffDate = caRegOrig.getRegEffDt();
			}
			caRegUpdt.setRegEffDt(ciRegEffDate);
			setFromMoFromYr();
			asgnFscalYr();

			// defect 8900
			// Remove this. DTA cannot do Exempts			
			//clear charge fee indicators for Headquarters 
			//if (caWSOfficeIds.getOfcIssuanceCd() == 1
			//	&& (caReg1.getExmptIndi() != 0))
			//if (caReg1.getExmptIndi() != 0)
			//{
			//	RegTtlAddlInfoData laRegTtlAddlInfoData =
			//		carrCompTransData[ciFCalcRow]
			//			.getRegTtlAddlInfoData();
			//	laRegTtlAddlInfoData.setChrgTtlFeeIndi(0);
			//	laRegTtlAddlInfoData.setChrgTrnsfrFeeIndi(0);
			//	laRegTtlAddlInfoData.setTrnsfrPnltyIndi(0);
			//	caVehMisc1.setNoChrgSalTaxEmiFeeIndi(1);
			//}
			// end defect 8900

			ciFCalcRow = UPDT;
			calcCommnFees();

			if (editDlrTtlExpMoYr())
			{
				// Set up and call fee calc for title
				carrCompTransData[UPDT].setNoMoChrg(
					Math.max(
						0,
						(ciToYear * 12 + ciToMonth)
							- (ciFromYr * 12 + ciFromMo)
							+ 1));
				asgnDisCntyFees();
				if (csTransCd.equals(TransCdConstant.REJCOR)
					&& caRegUpdt.getRegClassCd()
						== caRegOrig.getRegClassCd())
				{
					carrCompTransData[UPDT].setDisableAddlFees(1);
				}
				else
				{
					carrCompTransData[UPDT].setDisableAddlFees(0);
				}
				
				// defect 10808
				// Make sure update row RegEffDt = Today so it's correct
				// RegEffDt for lookup to rts_pass_fees row for model yr
				// Note: fix 9561 means we are always using today's date
				// in lookup to pass-fees
				//if (carrCompTransData[UPDT].getNoMoChrg() >= 12)
				//{				
					caRegUpdt.setRegEffDt(ciTransGregorianDate);
				//}
				// end defect 10808

				// defect 7972
				// Fix for defect 7538 to move this code into proper 
				// 'else' block. Set-up for whether or not to issue 
				// inventory for title 
				whetherIssueInvForTtl();
				callFeeCalcTtlMthds();
				setCrdtRmng();
			}
		}
		// end defect 7972    
	}

	/**
	 * Calculate duplicate receipt fee
	 * 
	 */
	private void calcDuplRecpt()
	{
		ciRegEffDate = caRegOrig.getRegEffDt();
		caRegUpdt.setRegEffDt(ciRegEffDate);
		if (ciChrgFeeIndi == 0)
		{
			carrCompTransData[UPDT].setNoChrgIndi(1);
		}
		//Init Core Fee module 
		ciFCalcRow = UPDT;
		carrCompTransData[ORIG].setCustActualRegFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(new Dollar(0.0));
		csAcctItmCd = "DUPL";
		if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
		{
			//Set Account Item Code for County
			csAcctItmCd = "DUPL";
		}
		else if (caWSOfficeIds.getOfcIssuanceCd() == REGION)
		{
			//Set Account Item Code for Region
			csAcctItmCd = "DUPLRC-R";
		}
		asgnMiscFees();
	}
	/**
	 * Calculate exchange fee.
	 * 
	 */
	private void calcExchg()
	{
		setFromMoFromYr();
		ciRegEffDate = ciTransGregorianDate;
		caRegUpdt.setRegEffDt(ciRegEffDate);
		if (ciChrgFeeIndi == 0)
		{
			carrCompTransData[UPDT].setNoChrgIndi(1);
		}
		asgnFscalYr();
		ciFCalcRow = UPDT;
		calcCommnFees();
		mngDispRecExpMoYr();
	}
	/**
	 * This is the method to be called for all fees calculation.
	 * This method delegates the Fees calculation to different methods
	 * depending on the transaction code which is a argument to this method.
	 * One of the argument is CompleteTransactionData object which contains all 
	 * the data values and indicators required for fees calculation.
	 * This method returns a CompleteTransactionData object containing the
	 * calculated fees.
	 * 
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  asTransCode String  
	 * @return CompleteTransactionData
	 */
	public CompleteTransactionData calcFees(
		String asTransCode,
		CompleteTransactionData aaCompTransData)
	{

		// Assign the CompleteTransaction object argument to the 
		// graph variable  carrCompTransData[ciFCalcRow]
		// Corresponds to Original Complete Transaction Data
		// defect 9084
		// Note:  row 0 corresponds to Original data, 
		//        row 1 corresponds to Updated data
		carrCompTransData[ORIG] = new CompleteTransactionData();
		// end defect 9084
		if (aaCompTransData.getOrgVehicleInfo() != null)
		{
			carrCompTransData[ORIG].setVehicleInfo(
				aaCompTransData.getOrgVehicleInfo());
		}
		ciNoMfVeh = aaCompTransData.getNoMFRecs();
		//Corresponds to Current or modified Complete Transaction Data
		carrCompTransData[UPDT] = aaCompTransData;
		ciInvItmCount = carrCompTransData[UPDT].getInvItemCount();
		if (aaCompTransData.getReCalcIndi() == 1)
		{
			return reCalcFees();
		}
		csTransCd = asTransCode;
		//Save the Inventory to be issued
		if (carrCompTransData[UPDT].getInvItms() != null)
		{
			Vector lvSavedInvItms =
				(Vector) UtilityMethods.copy(
					carrCompTransData[UPDT].getInvItms());
			carrCompTransData[UPDT].setSavedInvItms(lvSavedInvItms);
		}
		else
		{
			carrCompTransData[UPDT].setSavedInvItms(new Vector());
		}
		carrCompTransData[UPDT].setSavedInvItmCount(
			carrCompTransData[UPDT].getInvItemCount());
		caMFVehOrig = carrCompTransData[ORIG].getVehicleInfo();

		if (caMFVehOrig != null)
		{
			caVehOrig = caMFVehOrig.getVehicleData();
			caRegOrig = caMFVehOrig.getRegData();
//			caTtlOrig = caMFVehOrig.getTitleData();
//			caVehMiscOrig = carrCompTransData[ORIG].getVehMisc();
			// defect 9085 
//			caSpclPltRegOrig = caMFVehOrig.getSpclPltRegisData();
			// end defect 9085  
			//Calculate EffectiveExpDate and EffectiveExpDatePlusOne
			if (caRegOrig.getRegExpYr() != 0
				&& caRegOrig.getRegExpMo() != 0)
			{
				 if (caRegOrig.getRegExpMo() != 12)
				 {
					 ciEffectiveExpDatePlusOne =
					 (caRegOrig.getRegExpYr() * 10000)
					 + ((caRegOrig.getRegExpMo() + 1) * 100)
					  + 1;
				 }
				 else
				 {
					 ciEffectiveExpDatePlusOne =
					 ((caRegOrig.getRegExpYr() + 1) * 10000)
					 + 100
					 + 1;
				 }
				RTSDate laRTSEffExpDate =
					(
						new RTSDate(
							RTSDate.YYYYMMDD,
							ciEffectiveExpDatePlusOne)).add(
						RTSDate.DATE,
						-1);
				ciEffectiveExpDate = laRTSEffExpDate.getYYYYMMDDDate();
			}
		}
		caMFVehUpdt = carrCompTransData[UPDT].getVehicleInfo();
		if (caMFVehUpdt != null)
		{
			caVehUpdt = caMFVehUpdt.getVehicleData();
			caRegUpdt = caMFVehUpdt.getRegData();
			caTtlUpdt = caMFVehUpdt.getTitleData();
			caVehMiscUpdt = carrCompTransData[UPDT].getVehMisc();
			// defect 9085 
			caSpclPltRegUpd = caMFVehUpdt.getSpclPltRegisData();
			// end defect 9085  
			if (caTtlUpdt != null)
			{
				ciMustChangePltIndi = caTtlUpdt.getMustChangePltIndi();
			}
			if (caRegUpdt != null)
			{
				// Get info from ItemCodesCache for RegPltCd
				if (caRegUpdt.getRegPltCd() != null)
				{
//					caItemCdsData =
//						ItemCodesCache.getItmCd(
//							caRegUpdt.getRegPltCd());
				}
				ciUnregisterVehIndi = caRegUpdt.getUnregisterVehIndi();
				ciOffHwyUseIndi = caRegUpdt.getOffHwyUseIndi();
				ciRegWaivedIndi = caRegUpdt.getRegWaivedIndi();
			}
		}
		RegTtlAddlInfoData laRegTtlAddlInfo =
			carrCompTransData[UPDT].getRegTtlAddlInfoData();
		if (laRegTtlAddlInfo != null)
		{
			ciCorrtnEffMo = laRegTtlAddlInfo.getCorrtnEffMo();
			ciCorrtnEffYr = laRegTtlAddlInfo.getCorrtnEffYr();

			// defect 8900
			// defect 9119
			// We undid the set ciChrgFeeIndi = 1 for 
			// when (csTransCd.equals(TransCdConstant.IRENEW).
			// Where IRENEW gets the charge fee indi is in
			// SpecialPlatesRegisData, captured in
			// RegistrationRenewalServerBusiness.getVecFees()
			ciChrgFeeIndi = laRegTtlAddlInfo.getChrgFeeIndi();
			// end defect 9119	
			// end defect 8900

			
			// defect 10772
			//  For Websub, set all expired reg to 'Invalid Reason'.
			// defect 11054
			// If Dealer GDN is present, default 'Invalid' to �Valid Reason�
			ciRegExpReason = laRegTtlAddlInfo.getRegExpiredReason();			
			if (csTransCd.equals(TransCdConstant.WRENEW)
				&& isRegExpired())
			{
				ciRegExpReason = INVALID_REASON_CD;
			}
			else if (ciRegExpReason == INVALID_REASON_CD
				&& !getDLRGDN().equals(CommonConstant.STR_SPACE_EMPTY))
			{
				ciRegExpReason = VALID_REASON_CD;
			}
			//else
			//{
			//	ciRegExpReason = laRegTtlAddlInfo.getRegExpiredReason();
			//}
			// end defect 11054
			// end defect 10772
				
							
			// Charge Reg Penalty if Expiration Reason is 
			// Citation/Charge Penalty
			if (ciRegExpReason == CITATION_CHG_PNLTY)
			{
				carrCompTransData[UPDT].setRegPnltyChrgIndi(1);
			}
		}
		// Built TransGregorianDate in YYYYMMDD format
		if (carrCompTransData[UPDT].getTransactionDate() != 0)
		{
			ciTransGregorianDate =
				carrCompTransData[UPDT].getTransactionDate();
		}
		else
		{
			ciTransGregorianDate =
				(RTSDate.getCurrentDate()).getYYYYMMDDDate();
		}
		// defect 4988
		// Set the RegInvldindi to 1 for Antiques in Title events 
		if ((csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.DTANTD)
			|| csTransCd.equals(TransCdConstant.DTANTK)
			|| csTransCd.equals(TransCdConstant.DTAORD)
			|| csTransCd.equals(TransCdConstant.DTAORK))
			&& caRegOrig != null
			&& (caRegOrig.getRegClassCd() == 4
				|| caRegOrig.getRegClassCd() == 5))
			// defect 6088
			// We do not care if it is STILL antique			
			//&& caReg1 != null
			//&& !(caReg1.getRegClassCd() == 4
			//|| caReg1.getRegClassCd() == 5))
			// end defect 6088
		{
			caRegOrig.setRegInvldIndi(1);
		}
		// Set EffectiveDate to one day before transgregorian date if 
		// original regis invalid.
		if ((caRegOrig != null && caRegOrig.getRegInvldIndi() != 0)
			&& (caRegUpdt != null && caRegUpdt.getRegInvldIndi() != 1))
		{
			ciEffectiveExpDate =
				((new RTSDate(RTSDate.YYYYMMDD, ciTransGregorianDate))
					.add(RTSDate.DATE, -1))
					.getYYYYMMDDDate();
			ciEffectiveExpDatePlusOne = ciTransGregorianDate;
		}
		// Set the current date
		ciCurrMo =
			(new RTSDate(RTSDate.YYYYMMDD, ciTransGregorianDate))
				.getMonth();
		ciCurrYr =
			(new RTSDate(RTSDate.YYYYMMDD, ciTransGregorianDate))
				.getYear();

		// defect 8900
		// Use local object so that cache object is not changed				
		//caWSOfficeIds =
		//	OfficeIdsCache.getOfcId(
		//		carrCompTransData[UPDT].getOfcIssuanceNo());
		Object laOfficeIdsData =
			UtilityMethods.copy(
				OfficeIdsCache.getOfcId(
					carrCompTransData[UPDT].getOfcIssuanceNo()));
		caWSOfficeIds = (OfficeIdsData) laOfficeIdsData;
		// end defect 8900

		// Set office issuance number based on various conditions
		// For Apprehended vehicle
		if (laRegTtlAddlInfo != null
			&& laRegTtlAddlInfo.getApprhndFndsCntyNo() != 0)
		{
			caWSOfficeIds.setOfcIssuanceNo(
				laRegTtlAddlInfo.getApprhndFndsCntyNo());
		}
		else
		{
			caWSOfficeIds.setOfcIssuanceNo(
				carrCompTransData[UPDT].getOfcIssuanceNo());
			// If not a county, use office issuance number from 
			// retrieved vehicle record
			if (caRegUpdt != null
				&& caWSOfficeIds.getOfcIssuanceCd() != COUNTY)
			{
				caWSOfficeIds.setOfcIssuanceNo(
					caRegUpdt.getResComptCntyNo());
			}
		}
		if (cvTtlTypsTransCds.contains(csTransCd))
		{
			checkForTtlFees();
		}
		else if (cvRegOnlyTransCds.contains(csTransCd))
		{
			checkRegOnlyFees();
		}
		// defect 11052 
		else if (UtilityMethods.isVehInqTransCd(csTransCd))
		{
			// end defect 11052 
			calcVehInquiryFees();
		}
		else if (cvMiscaRegTransCds.contains(csTransCd))
		{
			calcMiscRegFees();
		}
		//Date validation indi required for REG029
		// defect 9126
		// Remove checks for InvProcsngCd to determine Special Plate
		// Determining Special Plate will now be done in REG029
		// handleExpDateValidation() 
		//if ((csTransCd.equals(TransCdConstant.TITLE)
		//	|| csTransCd.equals(TransCdConstant.NONTTL)
		//	|| csTransCd.equals(TransCdConstant.REJCOR))
		//	&& testIfWithinWnd()
		//	&& caItemCdsData != null
		//	&& caItemCdsData.getInvProcsngCd() != 2)
		//{
		//	carrCompTransData[UPDT].setTtlInWinInvPrCdNotEqTwoIndi(1);
		//}
		//else
		//{
		//	carrCompTransData[UPDT].setTtlInWinInvPrCdNotEqTwoIndi(0);
		//}
		// end defect 9126
		//set RegFeesData object
		RegFeesData laRegFeeData =
			carrCompTransData[UPDT].getRegFeesData();
		laRegFeeData.setFromMo(ciFromMo);
		laRegFeeData.setFromYr(ciFromYr);
		laRegFeeData.setToMonthMax(ciToMonthMax);
		laRegFeeData.setToMonthMin(ciToMonthMin);
		laRegFeeData.setToYearMax(ciToYearMax);
		laRegFeeData.setToYearMin(ciToYearMin);
		laRegFeeData.setExpMoYrMax(csExpMoYrMax);
		laRegFeeData.setExpMoYrMin(csExpMoYrMin);
		laRegFeeData.setFeeTotalMax(caFeeTotalMax);
		laRegFeeData.setFeeTotalMin(caFeeTotalMin);
		laRegFeeData.setExpMaxMonths(ciExpMaxMonths);
		laRegFeeData.setExpMinMonths(ciExpMinMonths);
		carrCompTransData[UPDT].setTransCode(csTransCd);
		if (caRegisPenaltyFee != null)
		{
			caRegisPenaltyFee = caRegisPenaltyFee.round();
		}
		carrCompTransData[UPDT].setRegisPenaltyFee(caRegisPenaltyFee);

		setDfltMoYrFees();

		laRegFeeData.setToMonthDflt(ciToMonth);
		laRegFeeData.setToYearDflt(ciToYear);

		//For Fee calculation - temporary additional weight
		carrCompTransData[UPDT].setPeriodOptVec(cvPeriodOpt);
		carrCompTransData[UPDT].setPrmtEffDate(ciPrmtEffDate);
		carrCompTransData[UPDT].setPeriodOptRowsread(
			ciPeriodOptRowsread);

		//Save the number of Months of credit
		carrCompTransData[UPDT].setNoMoCredit(
			carrCompTransData[ORIG].getNoMoChrg());
		carrCompTransData[UPDT].setTxtRegMoSold(csTxtRegMoSold);
		// defect 9126
		carrCompTransData[UPDT].setTxtSpclPltMoSold(csTxtSpclPltMoSold);
		// end defect 9126

		//change values for VehSalesTaxAmt and VehTotalSalesTaxPd for REJCOR
		if (csTransCd.equals(TransCdConstant.REJCOR)
			&& caTtlUpdt != null)
		{
			Dollar laSalesTaxPdAmt = caTtlUpdt.getSalesTaxPdAmt();
			carrCompTransData[UPDT].setVehSalesTaxAmt(new Dollar(0.0));
			carrCompTransData[UPDT].setVehTotalSalesTaxPd(
				laSalesTaxPdAmt);
		}
		//adjust RegEffDate to today if title in window renewed 
		// defect 8404
		// For Reg Period Length = 1 (ie: Seasonal Ag) you can 
		// 'renew' on a Title w/ Exch (ie: go up to 6 months of reg)
		// EVEN IF NOT IN WINDOW so must reset RegEffDt to today			
		if ((csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(
				TransCdConstant.REJCOR)) //&& testIfWithinWnd()
			&& (testIfWithinWnd()
				|| carrCompTransData[UPDT].getRegPeriodLngth() == 1)
			&& (carrCompTransData[UPDT].getNoMoChrg() > 0))
			// end defect 8404				
		{
			ciRegEffDate = ciTransGregorianDate;
			caRegUpdt.setRegEffDt(ciRegEffDate);
		}
		//Clear out registration variables for no regis record doc type codes
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR)
			|| csTransCd.equals(TransCdConstant.DTANTD)
			|| csTransCd.equals(TransCdConstant.DTANTK)
			|| csTransCd.equals(TransCdConstant.DTAORD)
			|| csTransCd.equals(TransCdConstant.DTAORK))
		{
			if (caTtlUpdt != null
				&& (caTtlUpdt.getDocTypeCd() == 2
					|| caTtlUpdt.getDocTypeCd() == 10
					|| caTtlUpdt.getDocTypeCd() == 11))
			{
				caRegUpdt.setRegPltNo(CommonConstant.STR_SPACE_EMPTY);
				caRegUpdt.setRegStkrNo(CommonConstant.STR_SPACE_EMPTY);
				caRegUpdt.setRegStkrCd(CommonConstant.STR_SPACE_EMPTY);
				caRegUpdt.setRegEffDt(0);
				caRegUpdt.setRegExpMo(0);
				caRegUpdt.setRegExpYr(0);
			}
		}

		// defect 8900
		// Set Expiration Month to 0 for Standard Exempt. Do not set
		// ExpYr to 0 because MF uses 'previous ExpYr' in this field.
		if (CommonFeesCache
			.isStandardExempt(caRegUpdt.getRegClassCd()))
		{
			caRegUpdt.setRegExpMo(0);
		}
		// end defect 8900

		//Set special plate indi for REG029 screen
		// defect 9126
		// Do not check InvProcsngCd. REG029 screen can check for if
		// SpclPltRegis object is not null
		//if (caItemCdsData != null
		//	&& caItemCdsData.getInvProcsngCd() == 2)
		//{
		//	carrCompTransData[UPDT].setSpecialPltIndi(1);
		//}
		//else
		//{
		//	carrCompTransData[UPDT].setSpecialPltIndi(0);
		//}
		// end defect 9126
		return carrCompTransData[UPDT];
	}
	/**
	 * Calculate base flat fee
	 * 
	 */
	private void calcFlatFee()
	{
		int liRegPrortnIncrmnt = caCommFeesData.getRegPrortnIncrmnt();
		double ldNoMoCharge =
			((carrCompTransData[ciFCalcRow].getNoMoChrg()
				+ liRegPrortnIncrmnt
				- 1)
				/ liRegPrortnIncrmnt)
				* liRegPrortnIncrmnt;
//		ciNoMoCharge = (new Double(ldNoMoCharge)).intValue();
		Dollar laRegFee = caCommFeesData.getRegFee();
		int liRegPeriodLngth = caCommFeesData.getRegPeriodLngth();
		Dollar laNoOfMonth = new Dollar(12.00);
		Dollar laNoMoCharge = new Dollar(ldNoMoCharge);
		caBaseRegFee =
			(laRegFee
				.multiplyNoRound(
					laNoMoCharge.divideNoRound(laNoOfMonth)))
				.round();
		Dollar laAnnualRegFee =
			(laRegFee
				.multiplyNoRound(laNoOfMonth)
				.divideNoRound(new Dollar(liRegPeriodLngth)))
				.round();
		carrCompTransData[ciFCalcRow].setAnnualRegFee(laAnnualRegFee);
	}
	/**
	 * Calculate the Vehicle Inquiry Fees
	 * 
	 */
	private void calcVehInquiryFees()
	{
		//Skip the fees calculation for headquarters
		if (caWSOfficeIds.getOfcIssuanceCd() != HEADQUARTERS)
		{
			//Test if inquiry is 'View and Print' and being done at a county
			RegTtlAddlInfoData laRegTtlAddlInfo =
				carrCompTransData[UPDT].getRegTtlAddlInfoData();
			if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
			{
				csAcctItmCd = "INQUIRY";
			}
			else
			{
				csAcctItmCd = "VEHINQ-R";
			}
			if (laRegTtlAddlInfo.getChrgFeeIndi() == 1)
			{
				carrCompTransData[UPDT].setNoChrgIndi(0);
			}
			else
			{
				carrCompTransData[UPDT].setNoChrgIndi(1);
			}
			int liRegEffDt = caRegUpdt.getRegEffDt();
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			//Init Core Fee module 
			ciFCalcRow = UPDT;
			carrCompTransData[ORIG].setCustActualRegFee(
				new Dollar(0.0));
			carrCompTransData[UPDT].setCustActualRegFee(
				new Dollar(0.0));
			asgnMiscFees();
			caRegUpdt.setRegEffDt(liRegEffDt);
			
			// defect 11052 
			if (carrCompTransData[UPDT].getTransCode().equals(TransCdConstant.VDSC) 
					||carrCompTransData[UPDT].getTransCode().equals(TransCdConstant.VDSCN))
				{
					csAcctItmCd = "VEHIQC-R";
					asgnMiscFees();
				}
			// end defect 11052 
		}
	}
	/**
	 * Calculate Mail-in Fee.
	 * 
	 */
	private void calcMailIn()
	{
		if ((carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
			.getProcsByMailIndi()
			== 1
			&& carrCompTransData[UPDT].getMailFeeApplies() == 1)
		{
			csAcctItmCd = "MAIL";
			// defect 8900 
			// asgnMiscFees();
			selectFrmAcctCdsTbl();
			caItmPrice = caAcctCdData.getMiscFee();
			ciItmQty = 1;
			asgnResToFeesData();
			// end defect 8900 
		}
	}
	/**
	 * Calculate Maximum, then Minimum fees                                                        
	 * 
	 */
	private void calcMaxMinFees()
	{

		ciToMonth = ciToMonthMax;
		ciToYear = ciToYearMax;

		callFeeCalcMthdBsdOnTransCd();

		setCrdtRmng();

		RegFeesData laRegFeesdata =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesdata.getVectFees();
		caFeeTotalMax = new Dollar(0.0);
		for (int i = 0; i < lvFeesData.size(); i++)
		{
			Dollar laItmPrice =
				(Dollar) ((FeesData) lvFeesData.elementAt(i))
					.getItemPrice();
			caFeeTotalMax = caFeeTotalMax.addNoRound(laItmPrice);
		}
		carrCompTransData[UPDT].setRegFeesData(new RegFeesData());

		ciToMonth = ciToMonthMin;
		ciToYear = ciToYearMin;

		callFeeCalcMthdBsdOnTransCd();

		setCrdtRmng();

		laRegFeesdata = carrCompTransData[UPDT].getRegFeesData();
		lvFeesData = laRegFeesdata.getVectFees();
		caFeeTotalMin = new Dollar(0.0);
		for (int i = 0; i < lvFeesData.size(); i++)
		{
			Dollar laItmPrice =
				(Dollar) ((FeesData) lvFeesData.elementAt(i))
					.getItemPrice();
			caFeeTotalMin = caFeeTotalMin.addNoRound(laItmPrice);
		}
	}
//	/**
//	 * 	Calc maximum ToMonth, ToYear
// 	 * 	@deprecated 
//	 */
//	private boolean calcMaxToMoToYr()
//	{
//		// If NOT Special Plates HQ section && out of Scope 
//		// defect 9126
//		// This edit should go away. Will not be here with Out of Scope. 
//		//if (carrCompTransData[UPDT].getOfcIssuanceNo() != SPECIAL_PLATES
//		//	//&& caItemCdsData.getInvProcsngCd() == 3)
//		//{
//		//	return true;
//		//}
//		// end defect 9126
//
//		// defect 5284.
//		// Set the default month and year to Curr Yr+1 for currently
//		// registered vehicles which have plates as listed below.
//		// Combinati0on Plate 10
//		// Fertilizer Plate 14
//		// Soil Conservation 31
//		// Soil Conservation Trailer Plate 32
//		// Cotton Plate 61 
//		//else if (
//
//		// defect 9337
//		// If 'TONLY' (Title Only - No Regis) there is no regis exp date
//		if (caRegUpdt.getRegClassCd()
//			== CommonConstant.TONLY_REGCLASSCD)
//		{
//			//Clear ToMonth/Year
//			ciToMonth = 0;
//			ciToYear = 0;
//			caRegUpdt.setRegExpMo(0);
//			caRegUpdt.setRegExpYr(0);
//			return true;
//		}
//		// end defect 9337
//
//		else if (
//			(csTransCd.equals(TransCdConstant.RENEW)
//				|| csTransCd.equals(TransCdConstant.IRENEW))
//				&& caRegOrig != null
//				&& caRegUpdt != null
//				&& caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd()
//				&& (caRegOrig.getRegClassCd() == 10
//					|| caRegOrig.getRegClassCd() == 14
//					|| caRegOrig.getRegClassCd() == 31
//					|| caRegOrig.getRegClassCd() == 32
//					|| caRegOrig.getRegClassCd() == 61)
//				&& !(isRegExpired()))
//		{
//			ciToMonth = caCommFeesData.getFxdExpMo();
//			ciToYear = ciCurrYr + 1;
//			return true;
//		}
//
//		// defect 8900
//		// If Standard Exempt, there is no expiration date
//		else if (
//			CommonFeesCache.isStandardExempt(
//				caRegUpdt.getRegClassCd()))
//		{
//			ciToMonth = 0;
//			ciToYear = 0;
//			return true;
//		}
//		// end defect 8900
//
//		// Set maximum if both fixed exp month and year 
//		else if (
//			caCommFeesData.getFxdExpMo() > 0
//				&& caCommFeesData.getFxdExpYr() > 0)
//		{
//			ciToMonth = caCommFeesData.getFxdExpMo();
//			ciToYear = caCommFeesData.getFxdExpYr();
//			return true;
//		}
//		// Set maximum for fixed exp month only
//		else if (caCommFeesData.getFxdExpMo() > 0)
//		{
//			ciToMonth = caCommFeesData.getFxdExpMo();
//
//			// defect 9784
//			// Fix edit for exchanging to fixed exp month.
//			// For Title or Exch and current month < fixed month,
//			//   year will be this year
//			// Else, (For renew, or Title or Exch w/ CurrMo >= FxdExpMo) 
//			//   maximum will be next year
//
//			// defect 9376
//			// Fix setting Maximum to Mo\Yr. For Renew,
//			// If vehicle exp 03/2008 and current date = 
//			// 03/2008, it was prompting for 2008 sticker.
//
//			// Cannot do Exch on expired plate, also cannot extend regis
//			// on Exch, therefore Max ToYear = the current Exp Year.
//			//			if (csTransCd.equals(TransCdConstant.EXCH))
//			//			{
//			//				// Set ToYear for a FxdExpMo Exchange
//			//				ciToYear =
//			//					ciToYearMin
//			//						+ (caCommFeesData.getRegPeriodLngth() / 12);
//			//				// Test If the ToYear/Mo more than 15 months from today .
//			//				if ((ciToYear * 12 + ciToMonth)
//			//					- (12 * ciCurrYr + ciCurrMo)
//			//					> 15)
//			//				{
//			//					ciToYear = ciToYearMin;
//			//				}
//			//			}
//			//			else
//			//				// defect 9008
//			//				// Expired with Invalid Reason starts from Curr Exp Mo.
//			//				{
//			//	ciToYear =
//			//		(new Double((ciCurrMo
//			//			+ caCommFeesData.getMaxallowbleRegMo()
//			//			- 2)
//			//			/ 12))
//			//			.intValue();
//			//	ciToYear = ciCurrYr + ciToYear;
//			//}
//			// defect 9085 
//			if (!isLP())
//			{
//				// If expired && valid reason
//				//			if (isRegExpired()
//				//			&& (ciRegExpReason == VALID_REASON
//				//				|| caCommFeesData.getRegPeriodLngth() == 1))
//				//		{
//				//			// ToMonth/Year = now plus max allowable reg months-1 .
//				//			ciToYear =
//				//				(new Double((ciCurrMo
//				//					+ caCommFeesData.getMaxallowbleRegMo()
//				//					- 2)
//				//					/ 12))
//				//					.intValue();
//				//			ciToYear = ciCurrYr + ciToYear;
//				//		}
//				//		else
//				//		{
//				// defect 9126
//				// This calculation should be TODAY plus max
//				// allowable reg months 
//				// ToMonth/Year = current exp plus max allowable reg months
//				//int liTempFromYear =
//				//	(caRegOrig.getRegExpMo() >= ciCurrMo)
//				//		? (ciCurrYr - 1)
//				//		: ciCurrYr;
//
//				// defect 9553
//				// Set MaxToYr for Tow Truck
//				if (csTransCd.equals(TransCdConstant.TOWP))
//				{
//					ciToYear = ciCurrYr + 1;
//					if (ciCurrMo == 12)
//					{
//						ciToYear = ciCurrYr + 2;
//					}
//				}
//				// end defect 9553
//				else
//				{
//					int liYrAdjust = 1;
//					if (ciCurrMo <= caCommFeesData.getFxdExpMo())
//					{
//						if (!(csTransCd.equals(TransCdConstant.RENEW)
//							|| csTransCd.equals(TransCdConstant.IRENEW)
//							|| csTransCd.equals(TransCdConstant.TITLE)
//							|| csTransCd.equals(TransCdConstant.NONTTL)
//							|| csTransCd.equals(TransCdConstant.REJCOR)))
//						{
//							liYrAdjust = 0;
//						}
//					}
//					ciToYear =
//						Math.max(
//							caRegOrig.getRegExpYr(),
//							(ciCurrYr + liYrAdjust));
//
//					//						int liTempFromYear = ciCurrYr;
//					//						if (isRegExpired())
//					//						{
//					//							if (ciCurrMo
//					//								<= caCommFeesData.getFxdExpMo())
//					//							{
//					//								liTempFromYear = ciCurrYr;
//					//							}
//					//							else
//					//							{
//					//								if (caRegOrig.getRegExpYr()
//					//									== ciCurrYr)
//					//								{
//					//									liTempFromYear = ciCurrYr - 1;
//					//								}
//					//								else
//					//								{
//					//									liTempFromYear = ciCurrYr;
//					//								}
//					//							}
//					//						}
//					//						else
//					//						{
//					//							liTempFromYear = ciCurrYr;
//					//						}
//					//
//					//						int liYearAdjust =
//					//							(new Double((caRegOrig.getRegExpMo()
//					//								+ caCommFeesData.getMaxallowbleRegMo()
//					//								- 2)
//					//								/ 12))
//					//								.intValue();
//					//						ciToYear = liTempFromYear + liYearAdjust;
//					//						// end defect 9126
//					//						//		}
//					//					}
//
//				}
//			}
//			// end defect 9376
//			// end defect 9784
//
//			// end defect 9085 
//			//end defect 9008
//
//			return true;
//
//		}
//
//		else if (csTransCd.equals(TransCdConstant.EXCH))
//		{
//			// Set maximum ToMonth/Year for EXCH 
//			if (carrCompTransData[UPDT].getOfcIssuanceNo()
//				== SPECIAL_PLATES)
//			{
//				//ASSIGN ToMonth/Year = Now plus 23 months .
//				ciToMonth = (ciCurrMo + 21) % 12 + 1;
//				ciToYear =
//					ciCurrYr
//						+ (new Double((ciCurrMo + 21) / 12)).intValue();
//			}
//
//			// defect 8900
//			// Exchanging from Standard Exempt, set Max ToMo\Yr
//			// to Now + Max Allowable Reg months-1
//			else if (
//				CommonFeesCache.isStandardExempt(
//					caRegOrig.getRegClassCd()))
//			{
//				asgnToMoToYrNowPlusMaxRegMoMinusOne();
//			}
//			// end defect 8900
//
//			//TEST for Special Plate
//			// defect 9126
//			// This edit goes away. Max ToMo\Yr will be the maximum
//			// of 'max months of registration' or current spcl plt ExpDt.  
//			//else if (caItemCdsData.getInvProcsngCd() == 2)
//			//{
//			//	//ASSIGN ToMonth/Year = Now plus reg period length + 3 
//			//	ciToMonth =
//			//		(ciCurrMo + caCommFeesData.getRegPeriodLngth() + 1)
//			//			% 12
//			//			+ 1;
//			//	ciToYear =
//			//		(new Double((ciCurrMo
//			//			+ caCommFeesData.getRegPeriodLngth()
//			//			+ 1)
//			//			/ caCommFeesData.getRegPeriodLngth()))
//			//			.intValue();
//			//	ciToYear = ciCurrYr + ciToYear;
//			//}
//			// end defect 9126
//			else
//			{
//				// defect 8404
//				// If RegPeriodLength = 1, set to Minimum of current exp
//				// OR Max allowable, else set to current exp
//				if (caCommFeesData.getRegPeriodLngth() == 1)
//				{
//					setToMoToYrToMinOfCurrExpDtOrMaxAllow();
//				}
//				else
//				{
//					//ASSIGN ToMonth/Year = current expiration
//					ciToMonth = caRegOrig.getRegExpMo();
//					ciToYear = caRegOrig.getRegExpYr();
//				}
//				// end defect 8404
//			}
//			return true;
//		}
//
//		else if (
//			csTransCd.equals(TransCdConstant.RENEW)
//				|| csTransCd.equals(TransCdConstant.IRENEW))
//		{
//			// Set maximum ToMonth/Year for RENEW 
//			// Test if RegPltNo = 'TONLY'
//
//			// defect 9337
//			// Do not need check for 'TONLY'. Fall thru naturally. 
//			//if (testForTOnlyAndSetToMoToYr())
//			//{
//			//}
//			// end defect 9337
//
//			//TEST if no current registration 
//			if (testNoCurrReg())
//			{
//				// ToMonth/Year = now plus max allowable reg months-1 
//				asgnToMoToYrNowPlusMaxRegMoMinusOne();
//			}
//			else if (testIfWithinWnd())
//			{
//				// ToMonth/Year = current exp plus max allowable reg months 
//				asgnToMoToYrCEPlusMaxRegMo();
//			}
//			// defect 8404
//			// 'Valid/Invalid' does not make sense for 
//			// RegPeriodLngth = 1 (ie: seasonal ag). Set maximum
//			// to Max Allowable Reg Months
//			//			
//			// If expired && valid reason
//			//else if (isRegExpired() && ciRegExpReason == VALID_REASON)
//			else if (
//				isRegExpired()
//					&& (ciRegExpReason == VALID_REASON
//						|| caCommFeesData.getRegPeriodLngth() == 1))
//				// end defect 8404				
//			{
//				// ToMonth/Year = now plus max allowable reg months-1 .
//				asgnToMoToYrNowPlusMaxRegMoMinusOne();
//			}
//			// end defect 8404
//			else
//			{
//				// ToMonth/Year = current exp plus max allowable reg months 
//				asgnToMoToYrCEPlusMaxRegMo();
//			}
//			return true;
//		}
//		// maximum ToMonth/Year for TransCd$ = TITLE, NONTTL, REJCOR, DTA 
//		else if (
//			csTransCd.equals(TransCdConstant.TITLE)
//				|| csTransCd.equals(TransCdConstant.NONTTL)
//				|| csTransCd.equals(TransCdConstant.REJCOR)
//				|| csTransCd.equals(TransCdConstant.DTANTD)
//				|| csTransCd.equals(TransCdConstant.DTANTK)
//				|| csTransCd.equals(TransCdConstant.DTAORD)
//				|| csTransCd.equals(TransCdConstant.DTAORK))
//		{
//			// Set maximum ToMonth/Year for TITLE, NONTTL, REJCOR, DTA
//			// Special Handling for REJCOR - Maximum.		
//			// defect 9126
//			// This edit goes away. Do not check for Special Plate. 
//			if (csTransCd.equals(TransCdConstant.REJCOR))
//				//&& caItemCdsData.getInvProcsngCd() != 2)
//			{
//				// ToMonth/Year to minimum 
//				ciToMonth = ciToMonthMin;
//				ciToYear = ciToYearMin;
//			}
//			// end defect 9126
//
//			// If new vehicle && eligible for full multi-REJCORyear regis
//			else if (
//				ciNoMfVeh == 0
//					&& caCommFeesData.getMaxMYrPeriodLngth() > 0
//					&& caVehUpdt.getVehModlYr() >= (ciCurrYr - 1)
//					&& caTtlUpdt.getOwnrShpEvidCd() == 6)
//			{
//				// ToMonth/Year = now plus max multi-year regis
//				ciToMonth =
//					(ciCurrMo
//						+ caCommFeesData.getMaxMYrPeriodLngth()
//						- 2)
//						% 12
//						+ 1;
//				// defect 7938
//				// ToYear = now plus max allowable reg months-1 
//				// ciToYear =
//				//	ciCurrYr
//				//		+ (new Double(cCommFeesData
//				//			.getMaxMYrPeriodLngth()
//				//			/ 12))
//				//			.intValue();
//				ciToYear =
//					(new Double((ciCurrMo
//						+ caCommFeesData.getMaxMYrPeriodLngth()
//						- 2)
//						/ 12.0))
//						.intValue();
//				ciToYear = ciCurrYr + ciToYear;
//				// end defect 7938	
//			}
//
//			// defect 8900
//			// For Exempt,set Max ToMo\Yr to Now + Max Allow Reg months-1
//			else if (caRegOrig.getExmptIndi() == 1)
//			{
//				asgnToMoToYrNowPlusMaxRegMoMinusOne();
//			}
//			// end defect 8900
//
//			// If NO current registration
//			else if (testNoCurrReg())
//			{
//				// ToMonth/Year = Now plus max allowable reg months - 1 
//				asgnToMoToYrNowPlusMaxRegMoMinusOne();
//			}
//			// If current && NOT within window && special plate 
//			// defect 9126
//			// This edit goes away. Max ToMo\Yr will be the maximum
//			// of 'max months of registration' or current spcl plt ExpDt.  
//			//else if (
//			//	!(isRegExpired())
//			//		&& !testIfWithinWnd()
//			//		&& caItemCdsData.getInvProcsngCd() == 2)
//			//	{
//			//	
//			//	// ToMonth/Year = Now plus reg period length + 3 
//			//	ciToMonth =
//			//		(ciCurrMo + caCommFeesData.getRegPeriodLngth() + 1)
//			//			% 12
//			//			+ 1;
//			//	ciToYear =
//			//		(new Double((ciCurrMo
//			//			+ caCommFeesData.getRegPeriodLngth()
//			//			+ 1)
//			//			/ caCommFeesData.getRegPeriodLngth()))
//			//			.intValue();
//			//	ciToYear = ciCurrYr + ciToYear;
//			//}
//			// end defect 9126
//
//			// If current && NOT within window && NOT special plate
//			// defect 9126
//			// Do not check for Special Plate. 
//			else if (!(isRegExpired()) && !testIfWithinWnd())
//				//&& caItemCdsData.getInvProcsngCd() == 1)
//			{
//				// end defect 9126
//				// defect 8404
//				// If RegPeriodLength = 1 (ie: seasonal ag) 
//				if (caCommFeesData.getRegPeriodLngth() == 1)
//				{
//					// Title with Exchange, set to min of curexp or max allow
//					if (caRegOrig.getRegClassCd()
//						!= caRegUpdt.getRegClassCd())
//					{
//						setToMoToYrToMinOfCurrExpDtOrMaxAllow();
//					}
//					else
//					{
//						// Title without Exchange, keep current expiration
//						// ToMonth/Year = current expiration 
//						ciToMonth = caRegOrig.getRegExpMo();
//						ciToYear = caRegOrig.getRegExpYr();
//					}
//				}
//				// end defect 8404
//			}
//			// If current && within window && (regular || special plate)
//			// defect 9126
//			// Do not check for 'Regular-or-Special' Plate. 
//			else if (!(isRegExpired()) && testIfWithinWnd())
//				//		&& (caItemCdsData.getInvProcsngCd() == 1
//				//			|| caItemCdsData.getInvProcsngCd() == 2))
//				// end defect 9126
//			{
//				// ToMonth/Year = Current exp plus max allowable reg months
//				asgnToMoToYrCEPlusMaxRegMo();
//			}
//			// If registration is expired
//			else if (isRegExpired())
//			{
//				if (ciRegExpReason == VALID_REASON)
//				{
//					// ToMonth/Year = Now plus max allowable reg months - 1 .
//					asgnToMoToYrNowPlusMaxRegMoMinusOne();
//				}
//				else
//				{
//					// ToMonth/Year = Current exp plus max allowable reg months 
//					asgnToMoToYrCEPlusMaxRegMo();
//				}
//			}
//
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 	Calc maximum ToMonth, ToYear - new Multi-year reg 
	 */
	private void calcMaxToMoToYrMYr()
	{
		// defect 10697
		// Calculating Maximum ToMo/Yr for Multi-year registration
		// Common Fees for Tow Truck is set in calcMiscRegFees()
		if (!csTransCd.equals(TransCdConstant.TOWP))
		{
			caCommFeesData =
				CommonFeesCache.getCommonFee(
					caRegUpdt.getRegClassCd(), ciTransGregorianDate);
		}
		
		// Default to Minimum
		// defect 11167
		// FromMo\Yr is already calculated in setFromMoFromYr()
		//int liFromMo = 0;
		//int liFromYr = 0;
		// end defect 11167
		ciToMonth = ciToMonthMin;
		ciToYear = ciToYearMin;

		// If 'TONLY' (Title Only - No Regis) there is no regis exp date
		// If Standard Exempt, there is no regis exp date
		if (caRegUpdt.getRegClassCd()
			== CommonConstant.TONLY_REGCLASSCD
			|| CommonFeesCache.isStandardExempt(
				caRegUpdt.getRegClassCd()))
		{
			//Clear ToMonth/Year
			ciToMonth = 0;
			ciToYear = 0;
		}
		
		// If Tow Truck Plate
		else if (csTransCd.equals(TransCdConstant.TOWP))
		{
			ciToYear = ciCurrYr + 1;
			if (ciCurrMo == 12)
			{
				ciToYear = ciCurrYr + 2;
			}
		}
		
		// If RegPeriodLength = 1 (ie: Seasonal Ag)
		else if (caCommFeesData.getRegPeriodLngth() == 1)
		{
			// if Renew In Window, can go Max Reg mos from current exp
			// defect 10772
			// add WRENEW
			if ((csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.IRENEW)
				|| csTransCd.equals(TransCdConstant.WRENEW))
				&& testIfWithinWnd())
			{
				// end defect 10772
				asgnToMoToYrCEPlusMaxRegMo();
			}
			else
			{  
				asgnToMoToYrNowPlusMaxRegMoMinusOne();
			}
		}
		
		// If Fixed ExpMo
		else if (caCommFeesData.getFxdExpMo() > 0)
		{
			ciToMonth = caCommFeesData.getFxdExpMo();
				
			// If Fixed ExpYr
			if (caCommFeesData.getFxdExpYr() > 0)
			{
				ciToYear = caCommFeesData.getFxdExpYr();
			}
			else
			{
				// Not Fixed Exp Yr
				// If Exchange or one of the 'special group',
				// use next avail year, else use full Multi-year 
				// defect 11303
				// We are, OR ARE CHANGING TO, this group.
				// Special Group = Annual Plates that are
				// NOT eligible for multi-year reg:
				// Combo, Disaster Rel, Fertilizer, Soil Cons
				if (csTransCd.equals(TransCdConstant.EXCH)
					//|| caRegOrig.getRegClassCd() == 10
					//|| caRegOrig.getRegClassCd() == 14
					//|| caRegOrig.getRegClassCd() == 31
					//|| caRegOrig.getRegClassCd() == 32)
					|| caRegUpdt.getRegClassCd() == 10
					|| caRegUpdt.getRegClassCd() == 11
					|| caRegUpdt.getRegClassCd() == 14
					|| caRegUpdt.getRegClassCd() == 31
					|| caRegUpdt.getRegClassCd() == 32
					|| caRegUpdt.getRegClassCd() == 77)
					// end defect 11303
					// defect 10697 
					//|| caRegOrig.getRegClassCd() == 61)
					// end defect 10697
				{
					// If we've already passed the month, or this is a
					// Renew of 'special group', use next yr
					ciToYear = ciCurrYr;
					if (ciCurrMo > caCommFeesData.getFxdExpMo()
						|| !csTransCd.equals(TransCdConstant.EXCH))
					{
						ciToYear = ciCurrYr + 1;
					}
				}
				else
				{
					int liRegPeriod = Math.max(
						caCommFeesData.getMaxMYrPeriodLngth(),
						caCommFeesData.getMaxallowbleRegMo());
					// defect 11143
					// Do correct calc of max year for 'today vs allowable months
					//ciToYear =
					//	(new Double((ciCurrMo
					//		+ liRegPeriod
					//		- 2)
					//		/ 12.0))
					//		.intValue();
					//ciToYear = ciCurrYr + ciToYear;
					// Get number of years in allowable reg period
					int liYears = liRegPeriod / 12;
					// defect 11298
					// Need to use either existing Efffective Exp Date or Today
					int liRegEffDate =
						Math.max(ciEffectiveExpDatePlusOne, ciTransGregorianDate);
					int liMonth =
						(new RTSDate(RTSDate.YYYYMMDD, liRegEffDate))
							.getMonth();
					int liYear =
						(new RTSDate(RTSDate.YYYYMMDD, liRegEffDate))
							.getYear();
					// end defect 11298
					//ciToYear = ciCurrYr + liYears;
					ciToYear = liYear + liYears;
					// Get number of months in 'max allowable month\year'
					int liMaxAllowMonths = ciToMonth + (ciToYear * 12);
					// Get number of months in 'current month\year'
					//int liCurrMonths = ciCurrMo + (ciCurrYr * 12);
					int liCurrMonths = liMonth + (liYear * 12);
					// Check if it's too far in future
					if (liMaxAllowMonths - liCurrMonths >= 
						caCommFeesData.getMaxallowbleRegMo())
					{
						// defect 11249
						//ciToYear = ciCurrYr + 
						//	(caCommFeesData.getMaxallowbleRegMo() - 1) / 12;
						ciToYear = ciToYear - 1;
						// end defect 11249
					}
					// end defect 11143
				}
			}
		}
		
		// If Exchange
		else if (csTransCd.equals(TransCdConstant.EXCH))
		{
			// Set to Minimum
			// Exchange should not normally extend registration
			ciToMonth = ciToMonthMin;
			ciToYear = ciToYearMin;

			// Special Plates branch can go to Max Multi-year			
			if (carrCompTransData[UPDT].getOfcIssuanceNo()
				== SPECIAL_PLATES)
			{
				ciToMonth =
					(ciCurrMo
						+ caCommFeesData.getMaxMYrPeriodLngth()
						- 2)
						% 12
						+ 1;
				ciToYear =
					(new Double((ciCurrMo
						+ caCommFeesData.getMaxMYrPeriodLngth()
						- 2)
						/ 12.0))
						.intValue();
				ciToYear = ciCurrYr + ciToYear;
			}
		}
		
		// If Renew, Title
		else
		{
			// Special Handling for REJCOR. Set to Minimum.		
			if (csTransCd.equals(TransCdConstant.REJCOR))
			{
				ciToMonth = ciToMonthMin;
				ciToYear = ciToYearMin;
			}
			
			// If new vehicle or no current registration
			else if (testNoCurrReg())
			{
				// defect 11167
				// FromMo\Yr is already calculated in setFromMoFromYr()
				//liFromMo = ciCurrMo;
				//liFromYr = ciCurrYr;
				//setMaxAllowOrMaxMultiYr(liFromMo, liFromYr);
				setMaxAllowOrMaxMultiYr(ciFromMo, ciFromYr);
				// end defect 11167
			}
			
			// If expired
			else if (isRegExpired())
			{
				// defect 11167
				// FromMo\Yr is already calculated in setFromMoFromYr()
				//if (ciRegExpReason == VALID_REASON)
				//{
				//	liFromMo = ciCurrMo;
				//	liFromYr = ciCurrYr;
				//}
				//else
				//{
				//	// Set to month after current expiration
				//	liFromMo =
				//		caRegOrig.getRegExpMo()
				//		% 12
				//		+ 1;
				//	// Expired year cannot be more than one year ago
				//	liFromYr = Math.max(
				//		caRegOrig.getRegExpYr(), ciCurrYr - 1);
				//	// If 'month after expir' = 1, must bump up year
				//	if (liFromMo == 1)
				//	{
				//		liFromYr = liFromYr + 1;
				//	}	
				//}
				//setMaxAllowOrMaxMultiYr(liFromMo, liFromYr);
				setMaxAllowOrMaxMultiYr(ciFromMo, ciFromYr);
				// end defect 11167
			}
			
			// If Current, not within window
			else if (!testIfWithinWnd())
			{
				ciToMonth = caRegOrig.getRegExpMo();
				ciToYear = caRegOrig.getRegExpYr();
			}
			
			// If Current, in window 
			else
			{
				// Set to month after current expiration
				// defect 11167
				// FromMo\Yr is already calculated in setFromMoFromYr()
				//liFromMo =
				//	caRegOrig.getRegExpMo()
				//	% 12
				//	+ 1;
				//liFromYr = caRegOrig.getRegExpYr();
				//// If 'month after expir' = 1, must bump up year
				//if (liFromMo == 1)
				//{
				//	liFromYr = liFromYr + 1;
				//}	
				//setMaxAllowOrMaxMultiYr(liFromMo, liFromYr);
				setMaxAllowOrMaxMultiYr(ciFromMo, ciFromYr);
				// end defect 11167
			}
		}
	}
	
	/**
	 * Calculate various miscellaneous registration fees .
	 */
	private void calcMiscRegFees()
	{
		if (csTransCd.equals(TransCdConstant.TOWP))
		{
			//Fee calculation - tow truck  
			//ASSIGN RegEffDate = Today
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			//Assign tow truck variables to variables used in common fee calc process
			caCommFeesData = new CommonFeesData();
			// defect 10685
			// For Fee Simplify, do not charge Tow Truck $15
			caCommFeesData.setRegFee(new Dollar(0.0));
			if (ciTransGregorianDate <
				SystemProperty.getFeeSimplifyStartDate().
					getYYYYMMDDDate())
			{
				caCommFeesData.setRegFee(new Dollar(15.0));
			}
			// end defect 10685
			caCommFeesData.setRegPeriodLngth(12);
			caCommFeesData.setRegPrortnIncrmnt(12);
			caCommFeesData.setMaxallowbleRegMo(15);
			caCommFeesData.setFxdExpMo(1);
			caCommFeesData.setFxdExpYr(0);
			caRegUpdt.setRegExpMo(caCommFeesData.getFxdExpMo());
			//ASSIGN Set FromMo/Yr for Tow Trucks
			// defect 6801 
			// Use indicator from TimedPermitData
			if (carrCompTransData[UPDT].getTimedPermitData() != null
				&& carrCompTransData[UPDT]
					.getTimedPermitData()
					.isRegistered())
			{
				//if (ciRegCurrIndi == 1)
				ciFromMo = caCommFeesData.getFxdExpMo() + 1;
				ciFromYr =
					((ciCurrMo <= caCommFeesData.getFxdExpMo())
						? ciCurrYr
						: ciCurrYr + 1);
			}
			// end defect 6801 
			else
			{
				//ASSIGN Set FromMo/Yr to today .
				ciFromMo = ciCurrMo;
				ciFromYr = ciCurrYr;
			}
			mngDispRecExpMoYr();
		}
		else if (
			// defect 9831 
		// defect 8268
		//csTransCd.equals(TransCdConstant.PDC)
		// 	|| csTransCd.equals(TransCdConstant.TDC))
		UtilityMethods
			.getEventType(csTransCd)
			.equals(TransCdConstant.RPL_DP_EVENT_TYPE)
				|| UtilityMethods.getEventType(csTransCd).equals(
					TransCdConstant.ADD_DP_EVENT_TYPE))
			//		csTransCd
			//			.equals(
			//			TransCdConstant.BPM)
			//				|| csTransCd.equals(TransCdConstant.BTM)
			//				|| csTransCd.equals(TransCdConstant.RPNM)
			//				|| csTransCd.equals(TransCdConstant.RTNM))
			// end defect 8268
			// end defect 9831 
		{
			//Fee calculation - disabled parking card.
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			if (ciChrgFeeIndi == 0)
			{
				carrCompTransData[UPDT].setNoChrgIndi(1);
			}
			//Init Core Fee module 
			ciFCalcRow = UPDT;
			carrCompTransData[ORIG].setCustActualRegFee(
				new Dollar(0.0));
			carrCompTransData[UPDT].setCustActualRegFee(
				new Dollar(0.0));

			// defect 10133 
			// Added Replace TransCds 
			if (csTransCd.equals(TransCdConstant.PDC)
				|| csTransCd.equals(TransCdConstant.TDC))
			{
				csAcctItmCd = csTransCd;
			}
			else if (csTransCd.equals(TransCdConstant.RPLPDC))
			{
				csAcctItmCd = AcctCdConstant.PDC;
			}
			else if (csTransCd.equals(TransCdConstant.RPLTDC))
			{
				csAcctItmCd = AcctCdConstant.TDC;
			}

			//			if (csTransCd.equals(TransCdConstant.BPM)
			//				|| csTransCd.equals(TransCdConstant.RPBPM))
			//			{
			//				csAcctItmCd = "BPM";
			//			}
			//			else if (
			//				csTransCd.equals(TransCdConstant.BTM)
			//					|| csTransCd.equals(TransCdConstant.RPBTM))
			//			{
			//				csAcctItmCd = "BTM";
			//			}
			//			else if (
			//				csTransCd.equals(TransCdConstant.RPNM)
			//					|| csTransCd.equals(TransCdConstant.RPRPNM))
			//			{
			//				csAcctItmCd = "RPNM";
			//			}
			//			else if (
			//				csTransCd.equals(TransCdConstant.RTNM)
			//					|| csTransCd.equals(TransCdConstant.RPRTNM))
			//			{
			//				csAcctItmCd = "RTNM";
			//			}
			// end defect 9831 
			// end defect 10133 

			// end defect 8268
			asgnMiscFees();
			// defect 8325 
			// Do not create duplicate entry in vector. Double
			// quantity / fee later. 
			//	// defect 8268
			//			if (carrCompTransData[UPDT]
			//				.getTimedPermitData()
			//				.getIssueTwoPlacardsIndi()
			//				== 1)
			//			{
			//				asgnMiscFees();
			//			}
			// // end defect 8268
			// end defect 8325 
		}
		else if (csTransCd.equals(TransCdConstant.TAWPT))
		{
			try
			{
				ciTempVehGrossWt =
					carrCompTransData[UPDT]
						.getTimedPermitData()
						.getTempVehGrossWt();
				// defect 6501
				// Use current date
				//ciRegEffDate = caReg0.getRegEffDt();
				ciRegEffDate = ciTransGregorianDate;
				// end defect 6501
				caRegUpdt.setRegEffDt(ciRegEffDate);
				Vector lvAcctCdsData =
					AccountCodesCache.getAcctCds(
						null,
						ciRegEffDate,
						AccountCodesCache.ITMCD_LIKE_CDVAR);
				calcCommnFees();

				// defect 8508
				//carrCompTransData[UPDT].setNoMoChrg(
				//	cCommFeesData.getRegPeriodLngth());
				if (caCommFeesData.getRegPeriodLngth() == 1)
				{
					carrCompTransData[UPDT].setNoMoChrg(12);
				}
				else
				{
					carrCompTransData[UPDT].setNoMoChrg(
						caCommFeesData.getRegPeriodLngth());
				}
				// end defect 8508

				//Init Core Fee module 
				ciFCalcRow = UPDT;
				carrCompTransData[ORIG].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setCustActualRegFee(
					new Dollar(0.0));
				calcReg();
				Vector lvFeesData =
					carrCompTransData[UPDT]
						.getRegFeesData()
						.getVectFees();

				Dollar laTotalOrigRegFee =
					((FeesData) lvFeesData.elementAt(0)).getItemPrice();
				RegistrationData laRegData =
					carrCompTransData[UPDT]
						.getVehicleInfo()
						.getRegData();

				// Change the Gross weight only for Registration Fee 
				// calculation and then reassign it to orginal value
				int liVehGrossWt = laRegData.getVehGrossWt();
				laRegData.setVehGrossWt(ciTempVehGrossWt);

				calcReg();
				laRegData.setVehGrossWt(liVehGrossWt);
				lvFeesData =
					carrCompTransData[UPDT]
						.getRegFeesData()
						.getVectFees();
				Dollar laTotalNewRegFee =
					((FeesData) lvFeesData.elementAt(1)).getItemPrice();
				Dollar laRegDifference =
					laTotalNewRegFee.subtractNoRound(laTotalOrigRegFee);

				setMFBaseActlFee();

				//Set up and display temp addl wt options screen 
				//SQL Select temp addl weight acct code table info
				lvAcctCdsData =
					AccountCodesCache.getAcctCds(
						null,
						ciRegEffDate,
						AccountCodesCache.ITMCD_LIKE_CDVAR);

				// Sort lvAcctCdsData in Ascending order according to the
				// PrmtFeePrcnt field
				sortAcctCdVector(lvAcctCdsData);

				//Set permit expiration dates .
				//INIT Q{#}.ExpDate
				RTSDate[] larrExpData = new RTSDate[6];
				larrExpData[0] = new RTSDate(ciCurrYr, 3, 31);
				larrExpData[1] = new RTSDate(ciCurrYr, 6, 30);
				larrExpData[2] = new RTSDate(ciCurrYr, 9, 30);
				larrExpData[3] = new RTSDate(ciCurrYr, 12, 31);
				larrExpData[4] = new RTSDate((ciCurrYr + 1), 3, 31);
				larrExpData[5] = new RTSDate((ciCurrYr + 1), 6, 30);

				//Set one month PrmtExpDate .
				cvPeriodOpt = new Vector();
				PeriodOpt laPeriodOpt = new PeriodOpt();
				cvPeriodOpt.add(laPeriodOpt);
				int liPrmtValidtyPeriod =
					((AccountCodesData) lvAcctCdsData.elementAt(0))
						.getPrmtValidtyPeriod();
				int liCurrDatePlusPrmtValDate =
					((new RTSDate(RTSDate.YYYYMMDD,
						ciTransGregorianDate))
						.add(RTSDate.DATE, liPrmtValidtyPeriod))
						.getYYYYMMDDDate();
				if (liCurrDatePlusPrmtValDate <= ciEffectiveExpDate)
				{
					laPeriodOpt.setPrmtExpDate(
						new RTSDate(
							RTSDate.YYYYMMDD,
							liCurrDatePlusPrmtValDate));
				}
				else
				{
					laPeriodOpt.setPrmtExpDate(
						new RTSDate(
							RTSDate.YYYYMMDD,
							ciEffectiveExpDate));
				}
				//PROC Set QStart 
				int liQStart = 0;
				if (ciCurrMo < 4)
				{
					liQStart = 0;
				}
				else if (ciCurrMo < 7)
				{
					liQStart = 1;
				}
				else if (ciCurrMo < 10)
				{
					liQStart = 2;
				}
				else
				{
					liQStart = 3;
				}
				//LOOP Set quarter PrmtExpDate 
				for (int PrmtExpRow = 1; PrmtExpRow < 4; PrmtExpRow++)
				{
					laPeriodOpt = new PeriodOpt();
					laPeriodOpt.setPrmtExpDate(
						larrExpData[(PrmtExpRow - 1) + liQStart]);
					int liPrmtExpDate =
						(laPeriodOpt.getPrmtExpDate())
							.getYYYYMMDDDate();
					if (liPrmtExpDate > ciEffectiveExpDate)
					{
						laPeriodOpt.setPrmtExpDate(
							new RTSDate(
								RTSDate.YYYYMMDD,
								ciEffectiveExpDate));
					}
					cvPeriodOpt.addElement(laPeriodOpt);
				}
				// ASSIGN MonthsTilExp using MfVeh{0}.RegExpMo/Yr .
				int liMonthsTilExp =
					(caRegOrig.getRegExpYr() * 12
						+ caRegOrig.getRegExpMo())
						- (ciCurrYr * 12 + ciCurrMo);

				// Calculate temp addl weight options.
				for (int PeriodOptRow = 0;
					PeriodOptRow < 4;
					PeriodOptRow++)
				{
					if ((ciPeriodOptRowsread == 2
						&& liMonthsTilExp <= 3)
						|| (ciPeriodOptRowsread == 3
							&& liMonthsTilExp <= 6))
					{
						break;
					}
					laPeriodOpt =
						(PeriodOpt) cvPeriodOpt.elementAt(PeriodOptRow);
					laPeriodOpt.setItmCd(
						((AccountCodesData) lvAcctCdsData
							.elementAt(PeriodOptRow))
							.getAcctItmCd());
					laPeriodOpt.setItmDesc(
						((AccountCodesData) lvAcctCdsData
							.elementAt(PeriodOptRow))
							.getAcctItmCdDesc());
					Dollar laPrmtFeePrcnt =
						((AccountCodesData) lvAcctCdsData
							.elementAt(PeriodOptRow))
							.getPrmtFeePrcnt();
					laPeriodOpt.setItmPrice(
						(laRegDifference
							.multiplyNoRound(
								laPrmtFeePrcnt.divideNoRound(
									new Dollar(100.0))))
							.round());
					ciPeriodOptRowsread++;
				}
				laPeriodOpt = (PeriodOpt) cvPeriodOpt.elementAt(0);
				laPeriodOpt.setPrmtValdityPeriod("1 MO");
				laPeriodOpt = (PeriodOpt) cvPeriodOpt.elementAt(1);
				laPeriodOpt.setPrmtValdityPeriod("1 QTR");
				laPeriodOpt = (PeriodOpt) cvPeriodOpt.elementAt(2);
				laPeriodOpt.setPrmtValdityPeriod("2 QTR");
				laPeriodOpt = (PeriodOpt) cvPeriodOpt.elementAt(3);
				laPeriodOpt.setPrmtValdityPeriod("3 QTR");
				Vector lvTempPeriodOpt = cvPeriodOpt;
				cvPeriodOpt = new Vector();
				for (int i = 0; i < lvTempPeriodOpt.size(); i++)
				{
					laPeriodOpt =
						(PeriodOpt) lvTempPeriodOpt.elementAt(i);
					if (laPeriodOpt.getItmPrice() != null)
					{
						cvPeriodOpt.add(laPeriodOpt);
					}
				}
				caRegUpdt.setRegEffDt(ciTransGregorianDate);
				ciPrmtEffDate = ciTransGregorianDate;
			}
			catch (Exception aeEx)
			{
				aeEx.printStackTrace();
			}
		}
		// defect 10491
		// Implement UtilityMethods.isPermitApplication();  
		// add check for PRMDUP 
		else if (
			UtilityMethods.isPermitApplication(csTransCd)
				|| csTransCd.equals(TransCdConstant.PRMDUP))
		{
			if (caWSOfficeIds.getOfcIssuanceCd() != HEADQUARTERS)
			{
				// end defect 10491 
				caRegUpdt.setRegEffDt(ciTransGregorianDate);

				//Init Core Fee module
				ciFCalcRow = UPDT;
				carrCompTransData[ORIG].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setCustActualRegFee(
					new Dollar(0.0));

				// defect 10491 
				if (csTransCd.equals(TransCdConstant.PRMDUP))
				{
					csAcctItmCd = AcctCdConstant.PRMDUP;

					if (caWSOfficeIds.getOfcIssuanceCd() == REGION)
					{
						csAcctItmCd = csAcctItmCd + "-R";
					}
					if (ciChrgFeeIndi == 0)
					{
						carrCompTransData[UPDT].setNoChrgIndi(1);
					}
					asgnMiscFees();
				}
				else if (csTransCd.equals(TransCdConstant.PT72))
				{
					// end defect 10491

					if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
					{
						csAcctItmCd = "72PT";
					}
					else
					{
						csAcctItmCd = "72PT-R";
					}
					//Look up and assign results for timed permits 
					lookUpAssgnResForTmdPrmts();
				}
				else if (csTransCd.equals(TransCdConstant.PT144))
				{
					if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
					{
						csAcctItmCd = "144PT";
					}
					else
					{
						csAcctItmCd = "144PT-R";
					}
					lookUpAssgnResForTmdPrmts();
				}
				else if (csTransCd.equals(TransCdConstant.OTPT))
				{
					if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
					{
						csAcctItmCd = "OTPT";
					}
					else
					{
						csAcctItmCd = "OTPT-R";
					}
					lookUpAssgnResForTmdPrmts();
				}
				else if (csTransCd.equals(TransCdConstant.FDPT))
				{
					csAcctItmCd = "FDPT";
					lookUpAssgnResForTmdPrmts();
				}
				// defect 10491 
				// 30MCPT does not exist 
				else if (csTransCd.equals(TransCdConstant.PT30))
					//|| csTransCd.equals("30MCPT"))
				{
					// end defect 10491 
					if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
					{
						csAcctItmCd = "30PT";
					}
					else
					{
						csAcctItmCd = "30PT-R";
					}
					lookUpAssgnResForTmdPrmts();
				}
			}
		}
		else if (
			csTransCd.equals(TransCdConstant.NRIPT)
				|| csTransCd.equals(TransCdConstant.NROPT))
		{
			//Fee calculation - non resident agric permit 
			//Init Core Fee module 
			ciFCalcRow = UPDT;
			carrCompTransData[ORIG].setCustActualRegFee(
				new Dollar(0.0));
			carrCompTransData[UPDT].setCustActualRegFee(
				new Dollar(0.0));
			//ASSIGN RegEffDate = Today
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			if (csTransCd.equals(TransCdConstant.NRIPT))
			{
				csAcctItmCd = "NRIPT";
			}
			else
			{
				csAcctItmCd = "NROPT";
			}
			nonResAgrPrmts();
		}
	}

	/**
	 * Calculate Mobility fee
	 * 
	 */
	private void calcMobility()
	{
		// Assign fee only if:
		//  - inventory is issued with this registration  &&
		//  - this is not an even registration exchange  &&
		//  - DisableCntyFees =  0
		// If originally Exempt, calculate CMF even if number of
		// months to charge is the same
		if (carrCompTransData[ciFCalcRow].getInvItemCount() > 0
			&& (carrCompTransData[ORIG].getNoMoChrg()
				!= carrCompTransData[UPDT].getNoMoChrg()
				|| caRegOrig.getExmptIndi() == 1)
			&& carrCompTransData[ciFCalcRow].getDisableCtyFees() == 0)
		{

			calcCommnFees();

			// If not exempt from additional fees 
			if (caCommFeesData != null
				&& caCommFeesData.getAddOnFeeExmptIndi() == 0)
			{
				retrieveCntyCalYr();
				// If CMF > 0 
				if (caCntyCalndrYr != null
					&& (caCntyCalndrYr.getMobilityFee()).compareTo(
						ZERO_DOLLAR)
						> 0)
				{
					caItmPrice = caCntyCalndrYr.getMobilityFee();
					applyMultiYrAdj();
					csAcctItmCd = "CMF";
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
	}

	/**
	 * Calculate permanent additional weight fee
	 * 
	 */
	private void calcPermWght()
	{

		// defect 8900
		// Add exempt 0.00 to fees due for Exempt
		if (caRegOrig.getExmptIndi() == 1 && ciChrgFeeIndi == 0)
		{
			csAcctItmCd = "AW";
			caItmPrice = new Dollar(0.0);
			asgnResToFeesData();
			return;
		}
		// end defect 8900

		setFromMoFromYr();
		//ASSIGN RegEffDate = Current Date  
		ciRegEffDate = ciTransGregorianDate;
		//ASSIGN FCalc{#} fees vars for exchange 	
		if (ciChrgFeeIndi == 0)
		{
			carrCompTransData[UPDT].setNoChrgIndi(1);
		}
		asgnFscalYr();
		//ASSIGN FCalc{0}.NoChrgRegisEmi, FCalc{1}.NoChrgRegisEmi 
		//carrCompTransData[0].setNoChrgRegisEmi(ciChrgRegisEmiIndi);			

		// defect 8516
		// Use lookup to CommonFees.EmissionPrcnt			
		//if (caReg0.getRegClassCd() != 10)
		int liRegClassCd = caRegOrig.getRegClassCd();
		int liRTSCurrDate = caRegOrig.getRegEffDt();
		caCommFeesData =
			CommonFeesCache.getCommonFee(liRegClassCd, liRTSCurrDate);
		if (caCommFeesData == null)
		{
			(
				carrCompTransData[ORIG]
					.getRegTtlAddlInfoData())
					.setNoChrgRegEmiFeeIndi(
				1);
		}
		else
		{
			if ((caCommFeesData.getEmissionsPrcnt())
				.compareTo(ZERO_DOLLAR)
				== 0)
			{
				(
					carrCompTransData[ORIG]
						.getRegTtlAddlInfoData())
						.setNoChrgRegEmiFeeIndi(
					1);
			}
			else
			{
				(
					carrCompTransData[ORIG]
						.getRegTtlAddlInfoData())
						.setNoChrgRegEmiFeeIndi(
					0);
			}
		}
		// end defect 8516

		//save the RegEffDate to reassign it back to caReg1
		int liRegEffDate = caRegUpdt.getRegEffDt();
		caRegUpdt.setRegEffDt(ciRegEffDate);
		calcCommnFees();

		// defect 8532
		// Should be using current exp mo\yr 
		//setMinToMoToYr();
		ciToMonth = caRegOrig.getRegExpMo();
		ciToYear = caRegOrig.getRegExpYr();
		// end defect 8532 

		//ASSIGN NoMoCharge 
		carrCompTransData[UPDT].setNoMoChrg(
			Math.max(
				0,
				((ciToYear * 12 + ciToMonth)
					- (ciFromYr * 12 + ciFromMo)
					+ 1)));
		//Init Core Fee module 
		ciFCalcRow = UPDT;
		carrCompTransData[ORIG].setCustActualRegFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(new Dollar(0.0));

		//Permanent Additional Weight
		ciFCalcRow = UPDT;
		calcReg();
		csAcctItmCd = "AW";
		selectFrmAcctCdsTbl();
		Vector lvFeesData =
			carrCompTransData[UPDT].getRegFeesData().getVectFees();
		if (lvFeesData.size() != 0)
		{
			FeesData lFeesData = (FeesData) lvFeesData.elementAt(0);
			lFeesData.setAcctItmCd(csAcctItmCd);
			lFeesData.setDesc(caAcctCdData.getAcctItmCdDesc());
		}
		calcRegSurChrg();

		ciFCalcRow = ORIG;
		calcReg();
		calcRegSurChrg();
		// defect 9325
		// Need to add the $1.00 Reg Fee-DPS fee for Perm Addl Wt.
		calcRegAddl();
		// end defect 9325		

		ciFCalcRow = UPDT;
		setCrdtRmng();
		// defect 4706
		// Set the credit remaining to zero for all cases
		carrCompTransData[UPDT].setCrdtRemaining(new Dollar(0.0));
		// Set the RegEffDate to original value
		caRegUpdt.setRegEffDt(liRegEffDate);
	}

	/**
	 * Calculate the Plate Transfer fee. 
	 * 
	 */
	private void calcPltTrnsfrFee()
	{
		// defect 11030 
		// Implement RegTtlAddlInfoData().getPTOTrnsfrIndi()
		
		//If Plate Transfer during Title, and Charge Fee Indi is set,
		// charge the Plate Transfer Fee.
		if (carrCompTransData[ciFCalcRow]
			.getRegTtlAddlInfoData()
			.getPTOTrnsfrIndi()
			== 1)
		{
			// end defect 11030 
			csAcctItmCd = "PLTTRNSF";
			selectFrmAcctCdsTbl();
			caItmPrice = caAcctCdData.getMiscFee();
			ciItmQty = 1;
			asgnResToFeesData();
			updateCustActlRegFee();
		}
	}
	/**
	 * Calculate the Plate Transfer fee. 
	 * 
	 */
	private void calcRbltSlvgFee()
	{
		if (carrCompTransData[ciFCalcRow]
			.getRegTtlAddlInfoData()
			.getChrgRbltSlvgFeeIndi()
			== 1)
		{
			csAcctItmCd = "RBLTFEE";
			selectFrmAcctCdsTbl();
			caItmPrice = caAcctCdData.getMiscFee();
			ciItmQty = 1;
			asgnResToFeesData();
			updateCustActlRegFee();
		}
	}

	/**
	 * Calculate Road and Bridge fee
	 * 
	 */
	private void calcRdBrdg()
	{
		// If inventory, not even exchange and disableCntyFees = 0
		// Fail if inventory is not being issued with this registration
		// defect 9101
		// If originally Exempt, calculate CRBF even if number of
		// months to charge is the same
		if (carrCompTransData[ciFCalcRow].getInvItemCount() > 0
			&& (carrCompTransData[ORIG].getNoMoChrg()
				!= carrCompTransData[UPDT].getNoMoChrg()
				|| caRegOrig.getExmptIndi() == 1)
			&& carrCompTransData[ciFCalcRow].getDisableCtyFees() == 0)
		{
			// end defect 9101
			calcCommnFees();

			// If not exempt from additional fees 
			if (caCommFeesData != null
				&& caCommFeesData.getAddOnFeeExmptIndi() == 0)
			{
				retrieveCntyCalYr();
				// If CRBF > 0 
				if (caCntyCalndrYr != null
					&& (caCntyCalndrYr.getCntyRdBrdgFee()).compareTo(
						ZERO_DOLLAR)
						> 0)
				{
					caItmPrice = caCntyCalndrYr.getCntyRdBrdgFee();
					applyMultiYrAdj();
					csAcctItmCd = "CRBF";
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
	}
	/**
	 * Calculate Reflectorization fee
	 * 
	 */
	private void calcReflectn()
	{
		// defect 9085
		// defect 10685
		// After certain date, Reflectorization fee is not longer charged
		if (carrCompTransData[ciFCalcRow].getVehicleInfo().
			getRegData().getRegEffDt() <
				SystemProperty.getFeeSimplifyStartDate().
					getYYYYMMDDDate())
		{
			if (carrCompTransData[ciFCalcRow].getInvItemCount() > 0
				|| (caSpclPltRegUpd != null
					&& caSpclPltRegUpd.getMfgSpclPltIndi() == 1))
			{
				calcCommnFees();
				if (((caCommFeesData.getReflectnFee())
					.compareTo(ZERO_DOLLAR))
					> 0)
				{
					caItmPrice = caCommFeesData.getReflectnFee();
					applyMultiYrAdj();
					csAcctItmCd = "REFLECT";
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
		// end defect 10685
		// end defect 9085 
	}
	/**
	 * Calculate Registration 
	 */
	private void calcReg()
	{
		// defect 10685
		// If expired and Invalid Reason, set RegEffDt depending on 
		// previous exp for lookup to common fees only! then set it back
		// so that 'today' prints as Effective Date on receipt.			
		//calcCommnFees();
		int liRegEffDt = caRegUpdt.getRegEffDt();
		if (isRegExpired() && ciRegExpReason != VALID_REASON)
		{
			String lsFromMo = String.valueOf(ciFromMo);
			if (ciFromMo < 10)
			{
				lsFromMo = "0" + lsFromMo;
			}
			String lsRegEffDt = 
				String.valueOf(ciFromYr) + lsFromMo + "01";
			caRegUpdt.setRegEffDt(Integer.parseInt(lsRegEffDt)); 
		}
		calcCommnFees();
		caRegUpdt.setRegEffDt(liRegEffDt);
		// end defect 10685
		
		// Calculate Base Registration Fees 
		// defect 6208
		// PassengerFeesData laPassFeesData = null;
		// RegistrationWeightFeesData laRegWgtFeesData = null;
		// Don't calculate Regis fees when there are 0 months to charge
		// Remove bogus check for Common Fees = null
		// add braces to 'if' statements as per standards

		// defect 9041
		// Never add "Exempt" for Correct Title Rejection (REJCOR)
		// defect 8900			
		// Add Exempt 0.00 to Fees Due if No Charge Fee Indi = 1
		// defect 9126
		// This should be looking for Exempt Indi, not No Charge Indi
		// ie:  this could be FeeCalcCat = 4 (no regis fees)
		//if (carrCompTransData[ciFCalcRow].getNoChrgIndi() == 1)

		// defect 9260
		// Clear out class variable for Base Reg Fee
		caBaseRegFee = new Dollar(0.0);
		// end defect 9260

		// defect 9366
		// Calculate Plate Transfer Fee if Not REJCOR  
		if (!csTransCd.equals(TransCdConstant.REJCOR))
		{
			calcPltTrnsfrFee();
		}
		// end defect 9366

		if (carrCompTransData[UPDT]
			.getVehicleInfo()
			.getRegData()
			.getExmptIndi()
			== 1
			&& carrCompTransData[UPDT].getNoChrgIndi() == 1)
		{
			if (!csTransCd.equals(TransCdConstant.REJCOR)
				&& ciFCalcRow == 1)
			{
				csAcctItmCd = "EXEMPT";
				caItmPrice = new Dollar(0.0);
				ciItmQty = 1;
				asgnResToFeesData();
				updateCustActlRegFee();
//				caDieselFee = carrCompTransData[UPDT].getDieselFee();
			}
		}
		// end defect 9126
		// end defect 8900
		// end defect 9041 

		else if (carrCompTransData[ciFCalcRow].getNoMoChrg() > 0)
		{
			//if (cCommFeesData != null)
			//{
			// end defect 6208
			if (caCommFeesData.getFeeCalcCat() == 1)
			{
				calcYrMdlBsdFee();
			}
			else if (caCommFeesData.getFeeCalcCat() == 2)
			{
				calcWgtBsdFee();
			}
			else if (caCommFeesData.getFeeCalcCat() == 3)
			{
				calcFlatFee();
			}
			// defect 10685
			else if (caCommFeesData.getFeeCalcCat() == 5)
			{
				calcRegClsGrpWgtBsdFee();
			}
			// end defect 10685			
			adjAddlDislFee();
			if (ciFCalcRow == ORIG)
			{
				crdtRemaining();
			}
			else
			{
				// Calculate registration penalty fee
				Dollar laDieselFee =
					carrCompTransData[ciFCalcRow].getDieselFee();
				Dollar laAnnualRegFee =
					carrCompTransData[ciFCalcRow].getAnnualRegFee();
				int liRegPnltyChrgIndi =
					carrCompTransData[ciFCalcRow].getRegPnltyChrgIndi();
				
				// defect 11171
				// The Penalty Fee is 'penalty fee percent' of the annual fee.
				// The Regis Fee is 'Base Fee' (Annual Fee * months) + Penalty Fee.
				// The Regis EMI Surcharge Fee (10% of Regis Fee) will be
				// calculated later in calcRegSurChrg().
				
//				caRegisPenaltyFee =
//					new Dollar(liRegPnltyChrgIndi)
//						.multiplyNoRound(
//							laAnnualRegFee.addNoRound(laDieselFee))
//						.multiplyNoRound(
//							new Dollar(
//								caCommFeesData.getRegPnltyFeePrcnt()
//									/ 100.0));
//				//Adjust regis penalty fee for emissions surcharge 
//				//TEST Charge registration emissions surcharge 
//				int liNoChrgRegEmiFeeIndi =
//					(carrCompTransData[ciFCalcRow]
//						.getRegTtlAddlInfoData())
//						.getNoChrgRegEmiFeeIndi();
//				if (caRegUpdt.getRegEffDt() >= REGIS_EMI_START_DT
//					&& !(liNoChrgRegEmiFeeIndi == 1))
//				{
//					caRegisPenaltyFee =
//						caRegisPenaltyFee.multiplyNoRound(
//							new Dollar(1.1));
//				}
//				if (caRegisPenaltyFee != null)
//				{
//					caRegisPenaltyFee = caRegisPenaltyFee.round();
//					//Assign registration fee
//					caItmPrice =
//						(
//							caBaseRegFee.addNoRound(
//								laDieselFee)).addNoRound(
//							caRegisPenaltyFee);
//				}
				
				// Registration Penalty fee 20%
				caRegisPenaltyFee =
				new Dollar(liRegPnltyChrgIndi)
					.multiplyNoRound(
						laAnnualRegFee.addNoRound(laDieselFee))
					.multiplyNoRound(
						new Dollar(
							caCommFeesData.getRegPnltyFeePrcnt()
								/ 100.0));
				
				// Reg fee = Base Reg Fee + Diesel Fee + Reg Penalty Fee
				// Note: Base Reg Fee = Annual Fee / 12 * NoMoCharge
				caItmPrice = caBaseRegFee
					.addNoRound(laDieselFee)
					.addNoRound(caRegisPenaltyFee);
				// end defect 11171
				
				// PROC Set ItmPrice to greater of registration fees or 
				// statutory minimum
				// Minimum fee not applicable in situations where credit
				// is calculated

				if (carrCompTransData[ORIG].getNoMoChrg() <= 0)
				{
					Dollar laMinRegFee = caCommFeesData.getMinRegFee();
					if (laMinRegFee.compareTo(caItmPrice) > 0)
					{
						caItmPrice = laMinRegFee;
					}
				}
				if (carrCompTransData[UPDT].getInvItemCount() > 0)
				{
					// defect 8218 
					// Retrieve PlateToSticker Data with or without 
					// sticker code
					MFVehicleData laMFVD =
						carrCompTransData[ciFCalcRow].getVehicleInfo();
					RegistrationData laRegData = laMFVD.getRegData();
					int liRTSEffDate = ciTransGregorianDate;
					int liRegClassCd = laRegData.getRegClassCd();
					String lsRegPltCd = laRegData.getRegPltCd();
					String lsRegStkrCd = laRegData.getRegStkrCd();
					if (lsRegStkrCd == null)
					{
						lsRegStkrCd = CommonConstant.STR_SPACE_EMPTY;
					}

					PlateToStickerData laPltToStkrData = null;
					if (!lsRegStkrCd
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						Vector lvPltToStkrData =
							RegistrationPlateStickerCache.getPltStkrs(
								liRegClassCd,
								lsRegPltCd,
								liRTSEffDate,
								lsRegStkrCd);

						if (lvPltToStkrData != null)
						{
							laPltToStkrData =
								(
									PlateToStickerData) lvPltToStkrData
										.elementAt(
									0);

						}
					}
					else
					{
						Vector lvPltToStkrData =
							RegistrationPlateStickerCache.getPltStkrs(
								liRegClassCd,
								lsRegPltCd,
								liRTSEffDate);

						if (lvPltToStkrData != null)
						{
							laPltToStkrData =
								(
									PlateToStickerData) lvPltToStkrData
										.elementAt(
									0);
						}

					}
					if (laPltToStkrData != null)
					{
						csAcctItmCd = laPltToStkrData.getAcctItmCd();
					}
					// end defect 8218 	
				}
				else
				{
					// defect 10623
					// Add check for Token Trailer. Tow Truck (even 
					// though it is not an annual plt, now has no stkr.
					// Note: until we get a 'token trailer fee' row in 
					// acct_itm_cds, just charge to 'plate sticker'						
//					if (carrCompTransData[ciFCalcRow]
//						.getVehicleInfo()
//						.getRegData()
//						.getRegClassCd()
//						== RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
//					{
//						csAcctItmCd = "US";
//					}
//					else
//					{
						csAcctItmCd = TransCdConstant.CORREG;
//					}
					// end defect 10623
				}
				ciItmQty = 1;
				asgnResToFeesData();
				updateCustActlRegFee();
//				caDieselFee = carrCompTransData[UPDT].getDieselFee();
			}
		}
	}
	/**
	 * Calculate Registration Additional Fees 
	 */
	private void calcRegAddl()
	{
		// defect 8104 
		MFVehicleData laMFVD =
			carrCompTransData[ciFCalcRow].getVehicleInfo();
		RegistrationData laRegData = laMFVD.getRegData();
		int liRTSEffDate = laRegData.getRegEffDt();
		int liRegClassCd = laRegData.getRegClassCd();

		// Retrieve all Reg Add'l Fees for RegClassCd/RTSEffDate 
		Vector lvRegAddlFeeData =
			RegistrationAdditionalFeeCache.getRegAddlFee(
				liRegClassCd,
				liRTSEffDate);

		// If NOT DisableAddlFees, add Reg Add'l Fees 
		if (carrCompTransData[UPDT].getDisableAddlFees() == 0)
		{
			for (int i = 0; i < lvRegAddlFeeData.size(); i++)
			{
				RegistrationAdditionalFeeData laRegAddlFee =
					(
						RegistrationAdditionalFeeData) lvRegAddlFeeData
							.get(
						i);

				csAcctItmCd = laRegAddlFee.getAddlFeeItmCd();

				if (determineChrgRegAddlFee())
				{
					caItmPrice = laRegAddlFee.getRegAddlFee();
					applyMultiYrAdj();
					asgnResToFeesData();
					updateCustActlRegFee();
				}
			}
		}
	}
	/**
	 * Calculate fee based on Reg Class Group and Weight  
	 *
	 */
	private void calcRegClsGrpWgtBsdFee()
	{
		RegistrationData laRegData = carrCompTransData[ciFCalcRow].
			getVehicleInfo().getRegData();
		String lsRegClassFeeGrpCd = caCommFeesData.getRegClassFeeGrpCd();
		int liVehGrossWt = laRegData.getVehGrossWt();
		caRegClassFeeGrpData =
			RegistrationClassFeeGroupCache.getRegisClass(
				lsRegClassFeeGrpCd,
				liVehGrossWt);
				
		carrCompTransData[ciFCalcRow].setAnnualRegFee(
			caRegClassFeeGrpData.getRegClassGrpRegFee().round());
			
		Dollar laNoMoCharge =
			new Dollar(carrCompTransData[ciFCalcRow].getNoMoChrg());
		double ldNoOfMonth = 12.00;
		Dollar laNoOfMonth = new Dollar(ldNoOfMonth);
		caBaseRegFee =
			(caRegClassFeeGrpData.getRegClassGrpRegFee()
				.multiplyNoRound(laNoMoCharge)
				.divideNoRound(laNoOfMonth))
				.round();
	}
	
	/**
	 * Calculate Registration fees for Subcontractor renewal 
	 * which is total collected - all other fees .
	 * 
	 */
	private void calcRegFeesForSubCon()
	{
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData.getVectFees();
		//Set AcctItmCd for Subcon 
		FeesData laFeesData = (FeesData) lvFeesData.elementAt(0);
		if (caRegUpdt.getRegStkrCd() != null
			&& !caRegUpdt.getRegStkrCd().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laFeesData.setAcctItmCd(caRegUpdt.getRegStkrCd());
		}
		else if (
			caRegUpdt.getRegPltCd() != null
				&& !caRegUpdt.getRegPltCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
		{
			laFeesData.setAcctItmCd(caRegUpdt.getRegPltCd());
		}
		// Subtract the Total Fees entered with the Fees calculated to 
		// obtain the registration fees
		Dollar laTotalOfFeesCalc = new Dollar(0.0);
		for (int i = 1; i < lvFeesData.size(); i++)
		{
			laFeesData = (FeesData) lvFeesData.elementAt(i);
			laTotalOfFeesCalc =
				laTotalOfFeesCalc.addNoRound(laFeesData.getItemPrice());
		}
		laFeesData = (FeesData) lvFeesData.elementAt(0);
		Dollar laRenwlTotalFee =
			carrCompTransData[UPDT].getSubConRenwlTotalFee();
		// Reg Fee will be the the remainder of the Total Fees minus
		// the sum of the addl fees 	
		laFeesData.setItemPrice(
			laRenwlTotalFee.subtractNoRound(laTotalOfFeesCalc));
		carrCompTransData[ciFCalcRow].setAnnualRegFee(
			laFeesData.getItemPrice());
	}
	/**
	 * Calculate registration renewal fee.
	 * 
	 */
	private void calcRegRnwl()
	{
		if (ciChrgFeeIndi == 0)
		{
			carrCompTransData[UPDT].setNoChrgIndi(1);
		}
		carrCompTransData[UPDT].setMailFeeApplies(1);
		//ASSIGN RegEffDate = max of EffectiveExpDate+1 or Today 
		ciRegEffDate =
			Math.max(ciEffectiveExpDatePlusOne, ciTransGregorianDate);
		caRegUpdt.setRegEffDt(ciRegEffDate);
		ciFCalcRow = UPDT;
		setFromMoFromYr();
		asgnFscalYr();
		calcCommnFees();
		mngDispRecExpMoYr();
	}
	/**
	 * Calculate Registration Surcharge 
	 */
	private void calcRegSurChrg()
	{
		// defect 6208
		// Only calculate when there are >0 months to charge
		if (carrCompTransData[ciFCalcRow].getNoMoChrg() > 0)
		{
			//TEST Charge registration emissions surcharge 
			int liNoChrgRegEmiFeeIndi =
				(carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
					.getNoChrgRegEmiFeeIndi();
			if (caRegUpdt.getRegEffDt() >= REGIS_EMI_START_DT
				&& !(liNoChrgRegEmiFeeIndi == 1))
			{
				if (ciFCalcRow == ORIG)
				{
					// defect 8900
					// Only charge if was not originally Exempt
					if (caRegOrig.getExmptIndi() == 0)
					{
						//Adjust Registration Surcharge if req'd
						Dollar laDieselFee = new Dollar(0.0);
						if (carrCompTransData[ciFCalcRow]
							.getDieselFee()
							!= null)
						{
							laDieselFee =
								carrCompTransData[ciFCalcRow]
									.getDieselFee();
						}
						caItmPrice =
							((new Dollar(0.1))
								.multiplyNoRound(
									caBaseRegFee.addNoRound(
										laDieselFee)))
								.round();
						if (caItmPrice.compareTo(ZERO_DOLLAR) >= 0)
						{
							//Adjust Registration Surcharge 
							RegFeesData laRegFeesData =
								carrCompTransData[UPDT]
									.getRegFeesData();
							Vector lvFeesData =
								laRegFeesData.getVectFees();
							int liRegisEMIRow = -1;
							Dollar laRegisEMIFee = null;
							FeesData laFeesData = null;
							for (int i = 0; i < lvFeesData.size(); i++)
							{
								laFeesData =
									(FeesData) lvFeesData.elementAt(i);
								String lsAcctItmCd =
									laFeesData.getAcctItmCd();
								if (lsAcctItmCd
									.equals(AcctCdConstant.REGISEMI))
								{
									liRegisEMIRow = i;
									laRegisEMIFee =
										laFeesData.getItemPrice();
									break;
								}
							}
							if (liRegisEMIRow >= 0)
							{
								laRegisEMIFee =
									laRegisEMIFee.subtractNoRound(
										caItmPrice);
								if (laRegisEMIFee
									.compareTo(ZERO_DOLLAR)
									> 0)
								{
									laFeesData.setItemPrice(
										laRegisEMIFee);
								}
								else
								{
									lvFeesData.removeElementAt(
										liRegisEMIRow);
								}
							}
						}
					}
					// end defect 8900
				}
				else
				{
					if (carrCompTransData[ciFCalcRow].getDieselFee()
						== null)
					{
						carrCompTransData[ciFCalcRow].setDieselFee(
							new Dollar(0.0));
					}
					int liNoMoCharge =
						carrCompTransData[ciFCalcRow].getNoMoChrg();
					
					// defect 11171
					// Calculate Regis Emission Fee correctly
					// see also: calcReg()
//					caBaseRegFee =
//						((carrCompTransData[ciFCalcRow]
//							.getAnnualRegFee())
//							.multiplyNoRound(new Dollar(liNoMoCharge)))
//							.divideNoRound(new Dollar(12.0))
//							.round();
//					caItmPrice =
//						(new Dollar(0.1)).multiplyNoRound(
//							caBaseRegFee.addNoRound(
//								carrCompTransData[ciFCalcRow]
//									.getDieselFee()));
//					caItmPrice =
//						(new Dollar(0.1)).multiplyNoRound(caBaseRegFee);
					
					//Reg fee = Annual Fee + Reg Penalty Fee
					// defect 11331
					// In defect 11171, reg calc for Combo was reconfigured. 
					// Now caRegisPenaltyFee can be null for Subcon Renew.
					if (caRegisPenaltyFee == null)
					{
						caRegisPenaltyFee = new Dollar(0);
					}
					// end defect 11331
					caBaseRegFee =
						((carrCompTransData[ciFCalcRow]
						.getAnnualRegFee())
						.multiplyNoRound(new Dollar(liNoMoCharge)))
						.divideNoRound(new Dollar(12.0))
						.round()
						.addNoRound(caRegisPenaltyFee);
					//Registration Emission Fee 10% of Reg Fee
					caItmPrice = 
						caBaseRegFee.multiplyNoRound(new Dollar(.1));
					//end defect 11171
					
					csAcctItmCd = AcctCdConstant.REGISEMI;
					if (caItmPrice.compareTo(ZERO_DOLLAR) > 0)
					{
						asgnResToFeesData();
					}
				}
			}
		}
		// end defect 6208
	}
	/**
	 * Calculate the registeration transfer fee. 
	 * 
	 */
	private void calcRegTrnsfr()
	{
		csAcctItmCd = AcctCdConstant.REG_TRNSFR;
		if ((carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
			.getChrgTrnsfrFeeIndi()
			== 1)
		{
			asgnMiscFees();
		}
	}
	/**
	 * Calculate replacement fee
	 * 
	 */
	private void calcRepl()
	{
		//Save the RegEffDate to reassign it back later
		int liRegEffDate = caRegUpdt.getRegEffDt();
		//ASSIGN RegEffDate = Current Date  
		ciRegEffDate = ciTransGregorianDate;
		asgnFscalYr();
		int liRTSEffDate = ciTransGregorianDate;
		int liRegClassCd = caRegUpdt.getRegClassCd();
		String lsRegPltCd = caRegUpdt.getRegPltCd();
		String lsRegStkrCd = caRegUpdt.getRegStkrCd();

		if (lsRegStkrCd == null)
		{
			lsRegStkrCd = CommonConstant.STR_SPACE_EMPTY;
		}

		ClassToPlateData laClassToPltData = null;

		try
		{
			Vector lvClassToPlate =
				ClassToPlateCache.getClassToPlate(
					liRegClassCd,
					lsRegPltCd,
					liRTSEffDate);

			if (lvClassToPlate != null)
			{
				laClassToPltData =
					(ClassToPlateData) lvClassToPlate.elementAt(0);
			}
		}
		catch (Exception aeEx)
		{
		}
		if (laClassToPltData != null)
		{
			// defect 10685
			// Set default replacement acct item code
			csAcctItmCd = "REPL";
			// end defect 10685
			
			// defect 9085 
			// Use Current plate vs. replacement plate
			// for fees 
			//String lsReplPltCd = laClassToPltData.getReplPltCd();
			// end defect 9085 
			if (ciChrgFeeIndi == 0)
			{
				carrCompTransData[UPDT].setNoChrgIndi(1);
			}
			caRegUpdt.setRegEffDt(ciRegEffDate);
			ciFCalcRow = UPDT;
			carrCompTransData[ORIG].setCustActualRegFee(
				new Dollar(0.0));
			carrCompTransData[UPDT].setCustActualRegFee(
				new Dollar(0.0));

			//Calculate Replacement
			// defect 8900
			// County has option on Exempt to charge fee			
			//if ((carrCompTransData[UPDT].getNoChrgIndi() == 1)
			//	|| (caReg1.getRegClassCd() == 39))
			if (carrCompTransData[UPDT].getNoChrgIndi() == 1)
			{
				caItmPrice = new Dollar(0.0);
			}
			// end defect 8900 
			else
			{
				// defect 9085 
				//
				// PlateTypeCache replaces RegistrationRenewalsCache
				// 
				// RegistrationRenewalsData laRegRenwlData =
				//	RegistrationRenewalsCache.getRegRenwl(lsReplPltCd);
				// caItmPrice = laRegRenwlData.getReplFee();

				PlateTypeData laPlateTypeData =
					PlateTypeCache.getPlateType(lsRegPltCd);

				if (caMFVehUpdt.isSpclPlt())
				{
					if (caSpclPltRegUpd.getChrgRemakeOnReplace())
					{
						caItmPrice = laPlateTypeData.getRemakeFee();
					}
					else
					{
						caItmPrice = laPlateTypeData.getReplFee();
					}
				}
				else
				{
					caItmPrice = laPlateTypeData.getReplFee();
				}
				// end defect 9085
				
				// defect 10685
				//csAcctItmCd = "REPL";
				String lsReplAcctItmCd = laPlateTypeData.
					getReplAcctItmCd();
				if (!lsReplAcctItmCd.equals(
					CommonConstant.STR_SPACE_EMPTY))
				{
					csAcctItmCd = lsReplAcctItmCd;
				}
				// end defect 10685
			}
			// defect 10685
			//csAcctItmCd = "REPL";
			// end defect 10685
			ciItmQty = 1;
			asgnResToFeesData();
		}

		// defect 8795
		// Add check for FeeCalcCat != 4 (no Reg fees)
		// defect 9695
		// Replace with check for NOT isSpecialPlate().
		ciFCalcRow = UPDT;
		calcCommnFees();
		if (carrCompTransData[UPDT].getNoChrgIndi()
			== 0 //&& caCommFeesData != null
		//&& (caCommFeesData.getFeeCalcCat() != 4)
			&& !CommonFeesCache.isStandardExempt(liRegClassCd))
		{
			// end defect 9695
			// end defect 8795
			calcReflectn();
			calcAutomation();
		}

		//ASSIGN ToMonth/Year = current expiration 
		ciToMonth = caRegOrig.getRegExpMo();
		ciToYear = caRegOrig.getRegExpYr();
		caRegUpdt.setRegEffDt(liRegEffDate);
	}
	/**
	 * Calculate sales tax fee.
	 * 
	 */
	private void calcSalesTax()
	{
		VehMiscData laVehMiscData =
			carrCompTransData[ciFCalcRow].getVehMisc();
		MFVehicleData laMFVehData =
			carrCompTransData[ciFCalcRow].getVehicleInfo();
		TitleData laTtlData = laMFVehData.getTitleData();
		Dollar laVehSalesPrice = laTtlData.getVehSalesPrice();
		Dollar laVehTradeInAllowance =
			laTtlData.getVehTradeinAllownce();
		if (laVehSalesPrice == null)
		{
			laVehSalesPrice = new Dollar(0.0);
		}
		if (laVehTradeInAllowance == null)
		{
			laVehTradeInAllowance = new Dollar(0.0);
		}
		Dollar laVehTaxAmt =
			laVehSalesPrice.subtractNoRound(laVehTradeInAllowance);
		carrCompTransData[ciFCalcRow].setVehTaxAmt(laVehTaxAmt);
		Dollar laVehSalesTaxAmt = null;
		carrCompTransData[ciFCalcRow].setVehSalesTaxAmt(
			new Dollar(0.0));
		if (laVehTaxAmt.compareTo(ZERO_DOLLAR) < 0)
		{
			carrCompTransData[ciFCalcRow].setVehTaxAmt(new Dollar(0.0));
			caItmPrice = new Dollar(0.0);
		}
		else
		{
			//Fetch the SalesTaxCategoryData from SalesTaxCategoryCache using
			//Sales Tax Category and Sales Tax Date and get the Fees value and Fees Description 
			int liSalesTaxDate = laVehMiscData.getSalesTaxDate();
			//TEST SalesTaxCat result still valid 
			if (caSalesTaxCatData == null
				|| (!((caSalesTaxCatData.getSalesTaxCat())
					.equals(laVehMiscData.getSalesTaxCat())
					&& (caSalesTaxCatData.getSalesTaxBegDate())
						<= liSalesTaxDate
					&& (caSalesTaxCatData.getSalesTaxEndDate())
						>= liSalesTaxDate)))
			{
				String lsSalesTaxCat = laVehMiscData.getSalesTaxCat();
				caSalesTaxCatData =
					SalesTaxCategoryCache.getSalesTaxCats(
						lsSalesTaxCat,
						liSalesTaxDate);
			}
			Dollar laSalesTaxFee = caSalesTaxCatData.getSalesTaxFee();
			Dollar laSalesTaxPrcnt =
				caSalesTaxCatData.getSalesTaxPrcnt();
			//Calculate the sales tax fee by using the following formula
			Dollar laVehSalesTaxParAmt =
				(
					laVehTaxAmt.multiplyNoRound(
						laSalesTaxPrcnt)).divideNoRound(
					new Dollar(100));
			if (laVehSalesTaxParAmt.compareTo(ZERO_DOLLAR) < 0)
			{
				laVehSalesTaxParAmt = new Dollar(0.0);
			}
			Dollar laTaxPdOthrState = laVehMiscData.getTaxPdOthrState();
			if (laTaxPdOthrState == null)
			{
				laTaxPdOthrState = new Dollar(0.0);
			}
			laVehSalesTaxAmt =
				(
					laVehSalesTaxParAmt.addNoRound(
						laSalesTaxFee)).subtractNoRound(
					laTaxPdOthrState);
			if (laVehSalesTaxAmt.compareTo(ZERO_DOLLAR) < 0)
			{
				laVehSalesTaxAmt = new Dollar(0.0);
			}
			carrCompTransData[ciFCalcRow].setVehSalesTaxAmt(
				laVehSalesTaxAmt);
			caItmPrice = laVehSalesTaxAmt;
		}
		csAcctItmCd = AcctCdConstant.SALES_TAX;
		ciItmQty = 1;
		asgnResToFeesData();
	}
	/**
	 * Calculates Sales Tax Penalty fee
	 * 
	 */
	private void calcSalesTaxPnlty()
	{
		// The Sales Tax Penality is calculated based on Vehicle Sales 
		// Tax Amount and Sales Tax Penality Charge percent. 
		int liSalesTaxPnltyChrgPrcnt =
			(carrCompTransData[ciFCalcRow].getVehMisc())
				.getSalesTaxPnltyPer();
		if (liSalesTaxPnltyChrgPrcnt != 0)
		{
			Dollar laVehSalesTaxAmt =
				carrCompTransData[ciFCalcRow].getVehSalesTaxAmt();
			caItmPrice =
				(
					new Dollar(
						liSalesTaxPnltyChrgPrcnt
							/ 100.0)).multiplyNoRound(
					laVehSalesTaxAmt);
			Dollar laOneDollar = new Dollar(1.0);
			if (caItmPrice.compareTo(ZERO_DOLLAR) > 0)
			{
				//max of ItmPrice and zero.
				if (caItmPrice.compareTo(laOneDollar) < 0)
				{
					caItmPrice = laOneDollar;
				}
			}
			else
			{
				caItmPrice = new Dollar(0.0);
			}
			if (caItmPrice.compareTo(ZERO_DOLLAR) != 0)
			{
				csAcctItmCd = AcctCdConstant.SALES_TAX_PNLTY;
				ciItmQty = 1;
				this.asgnResToFeesData();
			}
		}
	}
	/**
	 * Calculate SalesTax Surcharge fee.
	 * 
	 */
	private void calcSalesTaxSCharge()
	{
		// defect 6448
		// Added if statement so if checkbox is checked it will 
		// not be charged
		if (carrCompTransData[ciFCalcRow]
			.getVehMisc()
			.getNoChrgSalTaxEmiFeeIndi()
			== 0)
			// end defect 6448
		{
			//Calculate Sales Tax Surcharge
			Dollar laVehTaxAmt = new Dollar(0.0);
			Dollar laVehTradeInAllowance = new Dollar(0.0);
			MFVehicleData laMFVehData =
				carrCompTransData[ciFCalcRow].getVehicleInfo();
			TitleData laTtlData = laMFVehData.getTitleData();
			Dollar laVehSalesPrice = laTtlData.getVehSalesPrice();
			//Determine whether to use sales price or vehicle value
			// defect 8608
			// Also check for Veh Sales price is > 0
			//if (laVehSalesPrice != null)
			if (laVehSalesPrice != null
				&& laVehSalesPrice.compareTo(ZERO_DOLLAR) > 0)
			{
				laVehTradeInAllowance =
					laTtlData.getVehTradeinAllownce();
				laVehTaxAmt =
					laVehSalesPrice.subtractNoRound(
						laVehTradeInAllowance);
				carrCompTransData[ciFCalcRow].setVehTaxAmt(laVehTaxAmt);
			}
			// end defect 8608
			else
			{
				laVehTaxAmt = laTtlData.getVehicleValue();
			}
			VehMiscData laVehMisc =
				carrCompTransData[ciFCalcRow].getVehMisc();
			String lsSalesTaxCat = laVehMisc.getSalesTaxCat();
			int liVehGrossWt = laMFVehData.getRegData().getVehGrossWt();
			int liDieselIndi =
				laMFVehData.getVehicleData().getDieselIndi();
			int liVehModlYr =
				laMFVehData.getVehicleData().getVehModlYr();
			if ((laVehTaxAmt.compareTo(ZERO_DOLLAR)) < 0)
			{
				laVehTaxAmt = new Dollar(0.0);
				caItmPrice = new Dollar(0.0);
			}
			double ldVehTaxAmt =
				Double.parseDouble(laVehTaxAmt.getValue());
			int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
			TitleTERPPercentData laTtlTERPPData =
				TitleTERPPercentCache.getTERPPrcnt(
					lsSalesTaxCat,
					liRTSCurrDate,
					liDieselIndi,
					liVehModlYr,
					liVehGrossWt);
			if (laTtlTERPPData != null)
			{
				double ldTtlTERPPrcnt =
					Double.parseDouble(
						laTtlTERPPData.getTtlTERPPrcnt().getValue());
				caItmPrice =
					new Dollar(
						Math.max(
							0,
							(ldVehTaxAmt * ldTtlTERPPrcnt) / 100));
				//Assign Sales Tax Surcharge 
				csAcctItmCd = laTtlTERPPData.getTERPPrcntAcctItmCd();
				ciItmQty = 1;
				asgnResToFeesData();
				// defect 8903
				// Save Emission Sales Tax to be saved in MVFuncTrans
				laVehMisc.setEmissionSalesTax(caItmPrice);
				// end defect 8903 
			}
		}
		// defect 8903 
		// Resetting in case user 
		else
		{
			VehMiscData laVehMisc =
				carrCompTransData[ciFCalcRow].getVehMisc();
			laVehMisc.setEmissionSalesTax(new Dollar("0.00"));
		}
		// end defect 8903 
	}

	/**
	 * Calculate Special Plate fee.
	 * 
	 */
	private void calcSpclPlateFee()
	{
		Vector lvFees = new Vector();
		ciSpclPltNoMoCharge = 0;

		if (PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd()))
		{
			// defect 10357
			// Vendor Plate exp may be different than Reg exp
			// Vendor Plate exp can never be less than Reg exp
			int liToMonth = ciToMonth;
			int liToYear = ciToYear;
			if (PlateTypeCache.isVendorPlate(caRegUpdt.getRegPltCd()))
			{
				// For Renewal, if this is a 'synched' renew (reg exp =
				// plt exp), renew for that plate term
				// defect 10772
				// add WRENEW
				if ((csTransCd.equals(TransCdConstant.RENEW)
					|| csTransCd.equals(TransCdConstant.IRENEW)
					|| csTransCd.equals(TransCdConstant.SBRNW)
					|| csTransCd.equals(TransCdConstant.WRENEW))
					&& (caSpclPltRegUpd.getPltExpMo() == ciCurrentRegExpMo
						&& caSpclPltRegUpd.getPltExpYr()
							== ciCurrentRegExpYr))
				{
					// end defect 10772
					liToMonth = caSpclPltRegUpd.getPltExpMo();
					liToYear =
						caSpclPltRegUpd.getPltExpYr()
							+ caSpclPltRegUpd.getPltValidityTerm();
				}
				else
				{
					int liToPltMonths =
						(caSpclPltRegUpd.getPltExpYr() * 12)
							+ caSpclPltRegUpd.getPltExpMo();

					int liToRegMonths = (ciToYear * 12) + ciToMonth;

					if (liToPltMonths > liToRegMonths)
					{
						liToMonth = caSpclPltRegUpd.getPltExpMo();
						liToYear = caSpclPltRegUpd.getPltExpYr();
					}
				}
			}
			// end defect 10357

			// Assign Special Plate Number of Months to Charge 
			// Use Number of Months to Charge to determine fee
			ciSpclPltNoMoCharge =
				Math.max(
					0,
					(liToYear * 12 + liToMonth)
						- (ciSpclPltFromYr * 12 + ciSpclPltFromMo)
						+ 1);

			if (ciSpclPltNoMoCharge > 0)
			{
				// defect 10357 \ 10523

				// Needed for IRENEW, which does not go thru REG029:
				// Capture the Spcl Plt Months Sold and new PltExpMo\Yr
				// Need VP Validity Term
				// defect 10772
				// add WRENEW
				if (csTransCd.equals(TransCdConstant.IRENEW)
					|| csTransCd.equals(TransCdConstant.WRENEW))
				{
					// end defect 10772
					carrCompTransData[UPDT].setSpclPlateNoMoCharge(
						ciSpclPltNoMoCharge);
					caSpclPltRegUpd.setPltExpMo(liToMonth);
					caSpclPltRegUpd.setPltExpYr(liToYear);
				}
				// end defect 10357 \ 10523

				int liPltValidityTerm = 1;

				if (PlateTypeCache
					.isVendorPlate(caRegUpdt.getRegPltCd()))
				{
					// defect 10523
					// If charge >= 12 months plate but less than a full
					// multi-year term, set to One Year Term
					//liPltValidityTerm = caSpclPltRegUpd.
					//	getPltValidityTerm();
					if (ciSpclPltNoMoCharge >= 12
						&& ciSpclPltNoMoCharge
							< caSpclPltRegUpd.getPltValidityTerm() * 12)
					{
						liPltValidityTerm = 1;
					}
					else
					{
						liPltValidityTerm =
							caSpclPltRegUpd.getPltValidityTerm();
					}
					// end defect 10523
				}
				// defect 10948
				// Use new RegEffDt
				int liRTSEffDt = 
				(ciSpclPltFromYr * 10000)
				+ (ciSpclPltFromMo * 100)
				 + 1;
				// end defect 10948
				lvFees =
					PlateSurchargeCache.getPltSurcharge(
						caSpclPltRegUpd.getRegPltCd(),
						caSpclPltRegUpd.getOrgNo(),
						caSpclPltRegUpd.getAddlSetIndi(),
						0,
						// defect 10948
						//new RTSDate().getYYYYMMDDDate(),
						liRTSEffDt,
						// end defect 10948
						liPltValidityTerm);

				// Prorate fees based on number of months to charge for
				// all PLPs, or for RegPltCds that have proration
				// increment = 1.
				// Else, charge either '1 year' or '2 years' or '3 years'.
				caPltTypeData =
					PlateTypeCache.getPlateType(
						caSpclPltRegUpd.getRegPltCd());
				for (int i = 0; i < lvFees.size(); i++)
				{
					PlateSurchargeData laData =
						(PlateSurchargeData) lvFees.elementAt(i);
					//if (laData.getRegPltCd().startsWith("PLP")
					// defect 10357
					// Add Vendor Plate
					if (laData
						.getAcctItmCd()
						.startsWith(
							SpecialPlatesConstant.PLP_ACCTITMCD_PREFIX)
						|| caPltTypeData.getSpclPrortnIncrmnt() == 1
						|| PlateTypeCache.isVendorPlate(
							caRegUpdt.getRegPltCd()))
					{
						//end defect 10357							
						// defect 10357
						// For Vendor Plates, need to set the number of 
						// months to use for division (multiply 12 months
						// by the plate validity term)
						Dollar laMonths =
							new Dollar(
								liPltValidityTerm).multiplyNoRound(
								new Dollar(12.0));
						caItmPrice =
							((laData.getPltSurchargeFee())
								.multiplyNoRound(
									new Dollar(ciSpclPltNoMoCharge)))
							//		.divideNoRound(new Dollar(12.0))
	.divideNoRound(laMonths).round();
						// end defect 10357
						csAcctItmCd = laData.getAcctItmCd();
						asgnSpclFeesToFeesData();
						updateCustActlRegFee();
					}
					else
					{
						int liMonths = 0;
						if (ciSpclPltNoMoCharge <= 12)
						{
							liMonths = 12;
						}
						else if (ciSpclPltNoMoCharge <= 24)
						{
							liMonths = 24;
						}
						else
						{
							liMonths = 36;
						}
						caItmPrice =
							((laData.getPltSurchargeFee())
								.multiplyNoRound(new Dollar(liMonths)))
								.divideNoRound(new Dollar(12.0))
								.round();
						csAcctItmCd = laData.getAcctItmCd();
						asgnSpclFeesToFeesData();
						updateCustActlRegFee();
					}
				}
			}
		}
	}

	/**
	 * calcSubconSpclPltFees
	 */
	private void calcSubconSpclPltFees()
	{
//		Vector lvFees = new Vector();
		// defect 10392
		//if (carrCompTransData[UPDT]
		//	.getSubconRenwlData()
		//	.getSpclPltIndi()
		//	== 1
		//	&& !carrCompTransData[UPDT].getSubConRenwlTotalFee().equals(
		if (!carrCompTransData[UPDT]
			.getSubConRenwlTotalFee()
			.equals(CommonConstant.ZERO_DOLLAR))
		{
			// end defect 10392
			SubcontractorRenewalData laSBRNWData =
				carrCompTransData[UPDT].getSubconRenwlData();

			String lsSpclPltRegPltCd = laSBRNWData.getSpclPltRegPltCd();
			int liAddlSetIndi = laSBRNWData.getAddlSetIndi();
			String lsOrgNo = laSBRNWData.getOrgNo();

			// defect 10392
			caRegUpdt.setRegPltCd(lsSpclPltRegPltCd);
			caSpclPltRegUpd = new SpecialPlatesRegisData();
			caSpclPltRegUpd.setRegPltCd(lsSpclPltRegPltCd);
			caSpclPltRegUpd.setAddlSetIndi(liAddlSetIndi);
			caSpclPltRegUpd.setOrgNo(lsOrgNo);
			caSpclPltRegUpd.setPltExpMo(laSBRNWData.getPltExpMo());
			caSpclPltRegUpd.setPltExpYr(laSBRNWData.getPltExpYr());
			ciSpclPltFromMo = laSBRNWData.getPltExpMo();
			ciSpclPltFromYr = laSBRNWData.getPltExpYr();

			caSpclPltRegUpd.setPltValidityTerm(
				laSBRNWData.getPltVldtyTerm());

			// set the from month and year
			addOneMonthForSpclPltExp();

			ciCurrentRegExpMo = laSBRNWData.getRegExpMo();
			ciCurrentRegExpYr = laSBRNWData.getNewExpYr() - 1;

			// If no need to Renew, plate expiration does not change 
			if (laSBRNWData.getPltExpMo()
				+ laSBRNWData.getPltExpYr() * 12
				> laSBRNWData.getRegExpMo()
					+ laSBRNWData.getNewExpYr() * 12)
			{
				laSBRNWData.setPltNextExpMo(laSBRNWData.getPltExpMo());
				laSBRNWData.setPltNextExpYr(laSBRNWData.getPltExpYr());
				ciToYear = laSBRNWData.getPltExpYr();
				ciToMonth = laSBRNWData.getPltExpMo();
			}

			// Co-Term, plate expiration is bumped to new plate term
			else if (
				laSBRNWData.getPltExpMo() == laSBRNWData.getRegExpMo()
					&& laSBRNWData.getPltExpYr()
						== laSBRNWData.getNewExpYr() - 1)
			{
				laSBRNWData.setPltNextExpMo(laSBRNWData.getPltExpMo());
				laSBRNWData.setPltNextExpYr(
					laSBRNWData.getPltExpYr()
						+ laSBRNWData.getPltVldtyTerm());
				ciToYear =
					laSBRNWData.getPltExpYr()
						+ laSBRNWData.getPltVldtyTerm();
				ciToMonth = laSBRNWData.getPltExpMo();
			}
			// plate expiration is synched with reg expiration
			else
			{
				laSBRNWData.setPltNextExpMo(laSBRNWData.getRegExpMo());
				laSBRNWData.setPltNextExpYr(laSBRNWData.getNewExpYr());
				ciToYear = laSBRNWData.getNewExpYr();
				ciToMonth = laSBRNWData.getRegExpMo();
			}

			// calculate the special plate fee
			calcSpclPlateFee();

			//// defect 10357
			//// If Vendor Plate, look-up with Plate Validity Term
			//int liPltValidityTerm = 1;
			//if (PlateTypeCache
			//	.isVendorPlate(lsSpclPltRegPltCd))
			//{
			//	liPltValidityTerm = caMFVehUpdt.
			//		getSpclPltRegisData().getPltValidityTerm();
			//}
			//lvFees =
			//	PlateSurchargeCache.getPltSurcharge(
			//		lsSpclPltRegPltCd,
			//		lsOrgNo,
			//		liAddlSetIndi,
			//		0,
			//		new RTSDate().getYYYYMMDDDate(),
			//		liPltValidityTerm);
			//// end defect 10357

			//for (int i = 0; i < lvFees.size(); i++)
			//{
			//	PlateSurchargeData laData =
			//		(PlateSurchargeData) lvFees.elementAt(i);
			//	csAcctItmCd = laData.getAcctItmCd();
			//	caItmPrice = laData.getPltSurchargeFee();
			//	asgnSpclFeesToFeesData();
			//	updateCustActlRegFee();
			//}

			// end defect 10392
		}
	}
	/**
	 * Calculate subcontractor renewal fees
	 * 
	 */
	private void calcSubCon()
	{
		//ASSIGN RegEffDate = max of EffectiveExpDate+1 or Today 
		ciRegEffDate =
			Math.max(ciEffectiveExpDatePlusOne, ciTransGregorianDate);
		caRegUpdt.setRegEffDt(ciRegEffDate);
		caRegOrig = new RegistrationData();
		caRegOrig.setRegExpMo(caRegUpdt.getRegExpMo());
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData.getVectFees();
		boolean lbFeeCalcCat4 = false;
		if (carrCompTransData[UPDT].getSubConRenwlBarCdIndi() == 0)
		{
			// defect 8900
			// Moved earlier in code to use with Exempt Renewal 
			// Add empty FeesData object which is filled later with 
			// Registration Fee
			FeesData laFeesData = new FeesData();
			lvFeesData.add(laFeesData);

			int liRegClassCd = caRegUpdt.getRegClassCd();
			int liRTSCurrDate = caRegUpdt.getRegEffDt();
			caCommFeesData =
				CommonFeesCache.getCommonFee(
					liRegClassCd,
					liRTSCurrDate);

			if (caCommFeesData != null)
			{
				lbFeeCalcCat4 = caCommFeesData.getFeeCalcCat() == 4;
			}

			// If 0.00 entered as Fee, use "Exempt" AcctItmCd
			if (!lbFeeCalcCat4
				&& carrCompTransData[UPDT]
					.getSubConRenwlTotalFee()
					.compareTo(
					ZERO_DOLLAR)
					== 0)
			{
				laFeesData.setItemPrice(new Dollar(0.0));
				laFeesData.setAcctItmCd("EXEMPT");
				laFeesData.setItmQty(1);
			}
			else
			{
				//process fees for non-scanned renewals 
				//Init Core Fee module 
				ciFCalcRow = UPDT;
				carrCompTransData[ORIG].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setCustActualRegFee(
					new Dollar(0.0));
				//ASSIGN FCalc{#} fees vars for subcontractor renewal 
				carrCompTransData[UPDT].setInvItemCount(1);
				carrCompTransData[ORIG].setNoMoChrg(0);
				carrCompTransData[UPDT].setNoMoChrg(12);
				carrCompTransData[UPDT].setFsclYr(
					(caRegUpdt.getRegExpMo() == 1)
						? (caRegUpdt.getRegExpYr() - 1)
						: caRegUpdt.getRegExpYr());
				carrCompTransData[UPDT].setMailFeeApplies(1);
				if (ciChrgFeeIndi == 0)
				{
					carrCompTransData[UPDT].setNoChrgIndi(1);
				}

				// defect 8516
				// Use lookup to CommonFees.EmissionPrcnt			
				//if (caReg1.getRegClassCd() != 10)

				if (caCommFeesData == null)
				{
					(
						carrCompTransData[UPDT]
							.getRegTtlAddlInfoData())
							.setNoChrgRegEmiFeeIndi(
						1);
				}
				else
				{
					if ((caCommFeesData.getEmissionsPrcnt())
						.compareTo(ZERO_DOLLAR)
						== 0)
					{
						(
							carrCompTransData[UPDT]
								.getRegTtlAddlInfoData())
								.setNoChrgRegEmiFeeIndi(
							1);
					}
					else
					{
						(
							carrCompTransData[UPDT]
								.getRegTtlAddlInfoData())
								.setNoChrgRegEmiFeeIndi(
							0);
					}
				}
				// end defect 8516

				// defect 9085 
				if (!lbFeeCalcCat4)
				{
					calcRegAddl();
					calcReflectn();
					calcRdBrdg();
					calcChildSafty();
					// defect 9728
					calcMobility();
					// end defect 9728
					calcAutomation();
				}
				// defect 9362
				// Calculate Special Plate Fees if applicable
				// defect 9689
				// Do not calc plate fee for Vendor Plate
				// defect 10357
				// Calc plate fee for Vendor Plate
				if (carrCompTransData[UPDT]
					.getSubconRenwlData()
					.getSpclPltIndi()
					== 1)
					//	&& !PlateTypeCache.isVendorPlate(
					//		carrCompTransData[UPDT]
					//			.getSubconRenwlData()
					//			.getSpclPltRegPltCd()))
				{
					calcSubconSpclPltFees();
				}
				// end defect 10357
				// end defect 9689
				// end defect 9362

				//Do fake reg calc for purpose of feeding surcharge values 
				//For subcon, reg=total collected - all other fees 
				calcRegFeesForSubCon();
				if (!lbFeeCalcCat4)
				{
					carrCompTransData[ciFCalcRow].setAnnualRegFee(
						(
							carrCompTransData[ciFCalcRow]
								.getAnnualRegFee())
								.divideNoRound(
							new Dollar(1.1)));
					calcRegSurChrg();
					laFeesData = (FeesData) lvFeesData.elementAt(0);
					laFeesData.setItemPrice(new Dollar(0.0));
					//For subcon, reg=total collected - all other fees
					calcRegFeesForSubCon();
				}
			}
			// end defect 8900
			// end defect 9085 
		}
		carrCompTransData[UPDT].setDieselFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(
			carrCompTransData[UPDT].getSubConRenwlTotalFee());
		if (lvFeesData != null && lvFeesData.size() != 0)
		{
			FeesData lFeesData = (FeesData) lvFeesData.elementAt(0);
			carrCompTransData[UPDT].setCustBaseRegFee(
				lFeesData.getItemPrice());
		}
	}
	/**
	 * Calculate Token Trailer fee
	 * 
	 */
	private void calcToknTrlr()
	{
		RegTtlAddlInfoData lRegTtlAddlInfoData =
			carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData();
		if (lRegTtlAddlInfoData.getChrgAddlTknTrlrFeeIndi() == 1)
		{
			csAcctItmCd = "ADLTKN";
			asgnMiscFees();
		}
	}
	/**
	 * Calculate Title correction fee
	 * 
	 */
	private void calcTtlCorr()
	{
		//Save the RegEffDate to reassign it back later
		int liRegEffDate = caRegUpdt.getRegEffDt();
		//FCalc{#} fees vars for title 
		//calculate Fscal Fee 
		asgnFscalYr();

		//clear charge fee indicators for Headquarters

		// defect 8900
		// County can now do Exempts and have option to charge fees
		// Allow charge fee indis to handle charging, not Exempt Indi
		//if (caWSOfficeIds.getOfcIssuanceCd() == HEADQUARTERS
		//	&& (caReg1.getExmptIndi() != 0))
		//{
		//	RegTtlAddlInfoData laRegTtlAddlInfoData =
		//		carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData();
		//	laRegTtlAddlInfoData.setChrgTtlFeeIndi(0);
		//	// defect 6563
		//	// Add TitleTERPFee
		//	laRegTtlAddlInfoData.setChrgTtlTERPFeeIndi(0);
		//	// end defect 6563
		//	laRegTtlAddlInfoData.setChrgTrnsfrFeeIndi(0);
		//	laRegTtlAddlInfoData.setTrnsfrPnltyIndi(0);
		//	caVehMisc1.setNoChrgSalTaxEmiFeeIndi(1);
		//}
		// end defect 8900

		//ASSIGN RegEffDate = Current Date  
		ciRegEffDate = ciTransGregorianDate;
		caRegUpdt.setRegEffDt(ciRegEffDate);
		//Init Core Fee module 
		ciFCalcRow = UPDT;
		carrCompTransData[ORIG].setCustActualRegFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(new Dollar(0.0));
		assignTtl();
		// defect 6563
		// Add TitleTERPFee
		calcTtlTERPFee();
		// end defect 6563 
		calcTtlTrnsfrPnlty();
		calcRegTrnsfr();
		calcToknTrlr();
		// defect 9368
		calcBuyerTagFee();
		// end defect 9368
		
		// defect 11051 
		calcRbltSlvgFee(); 
		// end defect 11051
		
		//ASSIGN ToMonth/Year = current expiration 
		ciToMonth = caRegOrig.getRegExpMo();
		ciToYear = caRegOrig.getRegExpYr();
		//Reassign the saved RegEffDate date
		caRegUpdt.setRegEffDt(liRegEffDate);
	}
	/**
	 * Calculate the Title fee 
	 * 
	 */
	private void calcTtlFee()
	{
		// defect 8900
		// Check if Standard Exempt
		//if (ciRegWaivedIndi == 1
		//	|| ciOffHwyUseIndi == 1
		//	|| (caVehMisc1 != null
		//		&& caVehMisc1.getSpclPltProgIndi() == 1)
		//	|| caReg1.getRegClassCd() == 39)
		if (ciRegWaivedIndi == 1
			|| ciOffHwyUseIndi == 1
			|| (caVehMiscUpdt != null
				&& caVehMiscUpdt.getSpclPltProgIndi() == 1)
			|| (CommonFeesCache
				.isStandardExempt(caRegUpdt.getRegClassCd())))
		{
			// end defect 8900

			//Title for Off Hiway, Reg Waived, Spcl Plt Prog, Exempt RegClass 
			//ASSIGN RegEffDate = Today 
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			asgnFscalYr();
			//clear charge fee indicators for Headquarters
			if (caWSOfficeIds.getOfcIssuanceCd() == HEADQUARTERS
				&& (caRegUpdt.getExmptIndi() != 0))
			{
				RegTtlAddlInfoData lRegTtlAddlInfoData =
					carrCompTransData[ciFCalcRow]
						.getRegTtlAddlInfoData();
				lRegTtlAddlInfoData.setChrgTtlFeeIndi(0);
				// defect 6563
				//Add TitleTERPFee
				lRegTtlAddlInfoData.setChrgTtlTERPFeeIndi(0);
				// end defect 6563
				lRegTtlAddlInfoData.setChrgTrnsfrFeeIndi(0);
				lRegTtlAddlInfoData.setTrnsfrPnltyIndi(0);
				caVehMiscUpdt.setNoChrgSalTaxEmiFeeIndi(1);
			}
			callFeeCalcTtlMthds();
		}
		else
		{
			// set RegEffDate for Title (may change later for Title w/ 
			// Renewl/Exchange)
			// Test if new Vehicle  && Expired
			// defect 10685
			// Title should be RegEffDt = Today 
			//if (ciNoMfVeh == 0
			//	|| (caRegOrig.getRegExpMo() == 0
			//		&& caRegOrig.getRegExpYr() == 0)
			//	|| isRegExpired())
			//{
			//	//ASSIGN RegEffDate = Today 
			//	caRegUpdt.setRegEffDt(ciTransGregorianDate);
			//}
			//else
			//{
			//	ciRegEffDate = caRegOrig.getRegEffDt();
			//	caRegUpdt.setRegEffDt(ciRegEffDate);
			//}
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			// end defect 10685
			setFromMoFromYr();
			asgnFscalYr();

			// defect 8900
			// County can now do Exempts and have option to charge fees
			// Allow charge fee indis to handle charging, not Exempt Indi

			//clear charge fee indicators for Headquarters 
			//if (caWSOfficeIds.getOfcIssuanceCd() == HEADQUARTERS
			//	&& (caReg1.getExmptIndi() != 0))
			//{
			//	RegTtlAddlInfoData lRegTtlAddlInfoData =
			//		carrCompTransData[ciFCalcRow]
			//			.getRegTtlAddlInfoData();
			//	lRegTtlAddlInfoData.setChrgTtlFeeIndi(0);
			//	// defect 6563
			//	//Add TitleTERPFee
			//	lRegTtlAddlInfoData.setChrgTtlTERPFeeIndi(0);
			//	// end defect 6563
			//	lRegTtlAddlInfoData.setChrgTrnsfrFeeIndi(0);
			//	lRegTtlAddlInfoData.setTrnsfrPnltyIndi(0);
			//	caVehMisc1.setNoChrgSalTaxEmiFeeIndi(1);
			//	}
			// end defect 8900			

			//Look up Common Fees using FCalc{0} info 
			ciFCalcRow = UPDT;
			calcCommnFees();
			mngDispRecExpMoYr();
		}
	}
	/**
	 * Calculate Title TERP Fee
	 * 
	 */
	private void calcTtlTERPFee()
	{
		if ((carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
			.getChrgTtlTERPFeeIndi()
			== 1)
		{
			retrieveCntyCalYr();
			String lsCntyTERPStatusCd =
				caCntyCalndrYr.getCntyTERPStatusCd();
			int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();

			// Get TERPFee for County 
			// defect 8552
			// There are now two TERP fees for Cnty Status Cd = 'A' cntys.
			// TitleTERPFeeCache.getTERPFee() now returns a Vector.
			// Print each returned Title TERP Acct Cd \ Fee.

			//TitleTERPFeeData lTtlTERPFData =
			//	TitleTERPFeeCache.getTERPFee(
			//		lsCntyTERPStatusCd,
			//		liRTSCurrDate);
			//// Setup for Assigning TERP Fee  
			//caItmPrice = lTtlTERPFData.getTtlTERPFlatFee();
			//csAcctItmCd = lTtlTERPFData.getTERPAcctItmCd();
			//ciItmQty = 1;
			//asgnResToFeesData();

			Vector lvTtlTERP =
				TitleTERPFeeCache.getTERPFee(
					lsCntyTERPStatusCd,
					liRTSCurrDate);

			Object[] larrTemp = lvTtlTERP.toArray();

			for (int i = 0; i < larrTemp.length; i++)
			{
				TitleTERPFeeData laTitleTERP =
					(TitleTERPFeeData) larrTemp[i];

				caItmPrice = laTitleTERP.getTtlTERPFlatFee();
				csAcctItmCd = laTitleTERP.getTERPAcctItmCd();
				ciItmQty = 1;
				asgnResToFeesData();
			}
			// end defect 8552
		}
	}
	/**
	 * Calculate the Title transfer penality 
	 *  
	 */
	private void calcTtlTrnsfrPnlty()
	{
		if ((carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData())
			.getTrnsfrPnltyIndi()
			== 1)
		{
			// defect 9742, 9791
			
			// defect 10646 
			// Include Seller Financed  
			boolean lbDealer =
				caVehMiscUpdt.getTtlTrnsfrEntCd() != null
					&& (caVehMiscUpdt
						.getTtlTrnsfrEntCd()
						.equals(DEALER_CD)
						|| caVehMiscUpdt.getTtlTrnsfrEntCd().equals(
							DEALER_SELLER_FINANCED_CD));
			// end defect 10646 
			

			if (caMFVehUpdt.getTitleData().getTtlSignDate()
				< AcctCdConstant.TTL_TRNSFR_PNLTY_2008_START_DATE
				|| lbDealer)
			{
				csAcctItmCd = AcctCdConstant.TTL_TRNSFR_PNLTY;
			}
			else
			{
				csAcctItmCd = AcctCdConstant.TTL_TRNSFR_PNLTY_2008;
			}
			// end defect 9742, 9791

			// defect 9584 
			//asgnMiscFees();
			caItmPrice = caVehMiscUpdt.getTtlTrnsfrPnltyFee();
			asgnResToFeesData();
			updateCustActlRegFee();
			// end defect 9584 
		}
	}

	/**
	 * Calculate base weight fee  
	 *
	 */
	private void calcWgtBsdFee()
	{
		MFVehicleData laMFVD =
			carrCompTransData[ciFCalcRow].getVehicleInfo();
		RegistrationData laRegData = laMFVD.getRegData();
		int liRTSEffDate = laRegData.getRegEffDt();
		int liRegClassCd = laRegData.getRegClassCd();
		int liVehGrossWt = laRegData.getVehGrossWt();
		String lsTireTypeCd = laRegData.getTireTypeCd().toUpperCase();
		caRegWtFeesData =
			RegistrationWeightFeesCache.getRegWtFee(
				liRegClassCd,
				lsTireTypeCd,
				liRTSEffDate,
				liVehGrossWt);
		// defect 8690 
		// if null, do look-up with today's date
		if (caRegWtFeesData == null)
		{
			liRTSEffDate = new RTSDate().getYYYYMMDDDate();
			caRegWtFeesData =
				RegistrationWeightFeesCache.getRegWtFee(
					liRegClassCd,
					lsTireTypeCd,
					liRTSEffDate,
					liVehGrossWt);
		}
		// end defect 8690
		Double ldVehGross100Wt =
			new Double((liVehGrossWt + 99) / 100.0);
		ciVehGross100Wt = ldVehGross100Wt.intValue();
		Dollar laTireTypeFee = new Dollar(0.0);
		if (caRegWtFeesData != null)
		{
			laTireTypeFee = caRegWtFeesData.getTireTypeFee();
		}
		Dollar laAnnualRegFee =
			laTireTypeFee.multiplyNoRound(
				new Dollar(ciVehGross100Wt)).addNoRound(
				caCommFeesData.getRegFee());
		carrCompTransData[ciFCalcRow].setAnnualRegFee(
			laAnnualRegFee.round());
		Dollar laNoMoCharge =
			new Dollar(carrCompTransData[ciFCalcRow].getNoMoChrg());
		Dollar laNoOfMonth = new Dollar(12.00);
		caBaseRegFee =
			(laAnnualRegFee
				.multiplyNoRound(
					laNoMoCharge.divideNoRound(laNoOfMonth)))
				.round();
	}
	/**
	 * Calculate base year model fee
	 * 
	 */
	private void calcYrMdlBsdFee()
	{
		MFVehicleData laMFVD =
			carrCompTransData[ciFCalcRow].getVehicleInfo();
		RegistrationData laRegData = laMFVD.getRegData();
		VehicleData laVehData = laMFVD.getVehicleData();
		// defect 9561
		int liRegEffDate = 0;
		if (ciFCalcRow == ORIG)
		{
			liRegEffDate = ciTransGregorianDate;
		}
		else
		{
			liRegEffDate = laRegData.getRegEffDt();
		}
		// int liRegEffDate = laRegData.getRegEffDt();
		// end defect 9561
		int liRegClassCd = laRegData.getRegClassCd();
		String lsRegClassCd = String.valueOf(liRegClassCd);
		int liVehModlYr = laVehData.getVehModlYr();
		//Test PassFees result still valid?
		if (caPassFeeData == null
			|| (!(caPassFeeData.getRTSEffDate() <= liRegEffDate
				&& caPassFeeData.getRTSEffEndDate() >= liRegEffDate
				&& caPassFeeData.getRegClassCd() == liRegClassCd
				&& caPassFeeData.getBegModlYr() <= liVehModlYr
				&& caPassFeeData.getEndModlYr() >= liVehModlYr)))
		{
			caPassFeeData =
				PassengerFeesCache.getPassFee(
					lsRegClassCd,
					liRegEffDate,
					liVehModlYr);
		}
		int liRegPrortnIncrmnt = caCommFeesData.getRegPrortnIncrmnt();
		double ldNoMoCharge =
			((carrCompTransData[ciFCalcRow].getNoMoChrg()
				+ liRegPrortnIncrmnt
				- 1)
				/ liRegPrortnIncrmnt)
				* liRegPrortnIncrmnt;
//		ciNoMoCharge = (new Double(ldNoMoCharge)).intValue();
		Dollar laRegFee = caPassFeeData.getRegFee();
		double ldNoOfMonth = 12.00;
		Dollar laNoMoCharge = new Dollar(ldNoMoCharge);
		Dollar laNoOfMonth = new Dollar(ldNoOfMonth);
		caBaseRegFee =
			(laRegFee
				.multiplyNoRound(laNoMoCharge)
				.divideNoRound(laNoOfMonth))
				.round();
		carrCompTransData[ciFCalcRow].setAnnualRegFee(laRegFee);
	}
	/**
	 * Call correct fee calc procedures for TransCd 
	 * 
	 */
	private void callFeeCalcMthdBsdOnTransCd()
	{
		// ASSIGN Clear indis IssueCancel, Exit 
		// (user cancelled off inventory) 
//		ciIssueCancel = 0;
//		ciExit = 0;
		int liRegClassCd = caRegUpdt.getRegClassCd();
		// defect 8900
		// Add check for not Standard Exempt
		//if (csTransCd.equals(TransCdConstant.TITLE)
		//	|| csTransCd.equals(TransCdConstant.NONTTL)
		//	|| csTransCd.equals(TransCdConstant.REJCOR))
		if ((csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR))
			&& !(CommonFeesCache.isStandardExempt(liRegClassCd)))
		{
			// end defect 8900

			// Set up and call fee calc for title 
			// ASSIGN carrCompTransData[UPDT].NoMoCharge 
			// Determine if Reject correction should not charge fees
			if (csTransCd.equals(TransCdConstant.REJCOR)
				&& caRegOrig != null
				&& caRegUpdt != null
				&& (caRegOrig.getRegClassCd() == 4
					|| caRegOrig.getRegClassCd() == 5)
				&& caRegOrig.getRegInvldIndi() == 0
				&& caRegOrig.getRegPltCd() != null
				&& caRegUpdt.getRegPltCd() != null
				&& caRegOrig.getRegPltCd().equals(caRegUpdt.getRegPltCd())
				&& caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd())
			{
				caRegUpdt.setRegExpYr(ciToYear);
			}
			else
			{
				carrCompTransData[UPDT].setNoMoChrg(
					Math.max(
						0,
						(ciToYear * 12 + ciToMonth)
							- (ciFromYr * 12 + ciFromMo)
							+ 1));
			}

			asgnDisCntyFees();

			if (csTransCd.equals(TransCdConstant.REJCOR)
				&& caRegUpdt.getRegClassCd() == caRegOrig.getRegClassCd())
			{
				carrCompTransData[UPDT].setDisableAddlFees(1);
			}
			else
			{
				carrCompTransData[UPDT].setDisableAddlFees(0);
			}

			whetherIssueInvForTtl();

			callFeeCalcTtlMthds();
		}

		else if (csTransCd.equals(TransCdConstant.EXCH))
		{
			//Set up and call fee calc for exchange 
			//ASSIGN reset inventory table and count to original values .
			//ASSIGN FCalc{0}.NoMoCharge 
			carrCompTransData[UPDT].setNoMoChrg(
				Math.max(
					0,
					(ciToYear * 12 + ciToMonth)
						- (ciFromYr * 12 + ciFromMo)
						+ 1));

			asgnDisCntyFees();

			// defect 8900
			// Remove Exempt Indi check to handle 0.00 charge for Exempts
			//if (caReg1.getExmptIndi() != 1
			//	&& !(ciRegWaivedIndi == 1
			if (!(ciRegWaivedIndi == 1
				|| ciOffHwyUseIndi
					== 1) // Test for Out-of-scope inv
			// defect 9126
			// Do not check InvProcsngCd.Use method isOutOfScopePlate().
			// Also check for 'or is HQ' (ie: skip reg fees if HQ). 
			//&& caItemCdsData.getInvProcsngCd() != 3)
				&& !PlateTypeCache.isOutOfScopePlate(
					carrCompTransData[ciFCalcRow]
						.getVehicleInfo()
						.getRegData()
						.getRegPltCd())
				&& caWSOfficeIds.getOfcIssuanceCd() != HEADQUARTERS)
			{
				// end defect 9126
				// end defect 8900
				//Init Core Fee module 
				ciFCalcRow = UPDT;
				carrCompTransData[ORIG].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setInvItemCount(
					(ciInvItmCount > 0) ? 1 : 0);
				//ASSIGN FCalc{0}.NoChrgRegisEmi, FCalc{1}.NoChrgRegisEmi 
				//carrCompTransData[UPDT].setNoChrgRegisEmi(ciChrgRegisEmiIndi);			

				// defect 8516
				// Use lookup to CommonFees.EmissionPrcnt			
				//if (caReg0.getRegClassCd() != 10)
				liRegClassCd = caRegOrig.getRegClassCd();
				int liRTSCurrDate = caRegOrig.getRegEffDt();
				// defect 11003
				// If no RegClassCd change and no weight change,
				// do 'even exchange' (use new RegEffDt for both old and new regis)
				if (caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd() &&
					caRegOrig.getVehGrossWt() == caRegUpdt.getVehGrossWt())
				{
					liRTSCurrDate = caRegUpdt.getRegEffDt();
				}
				// end defect 11003
				caCommFeesData =
					CommonFeesCache.getCommonFee(
						liRegClassCd,
						liRTSCurrDate);
				if (caCommFeesData == null)
				{
					(
						carrCompTransData[ORIG]
							.getRegTtlAddlInfoData())
							.setNoChrgRegEmiFeeIndi(
						1);
				}
				else
				{
					if ((caCommFeesData.getEmissionsPrcnt())
						.compareTo(ZERO_DOLLAR)
						== 0)
					{
						(
							carrCompTransData[ORIG]
								.getRegTtlAddlInfoData())
								.setNoChrgRegEmiFeeIndi(
							1);
					}
					else
					{
						(
							carrCompTransData[ORIG]
								.getRegTtlAddlInfoData())
								.setNoChrgRegEmiFeeIndi(
							0);
					}
				}
				// end defect 8516

				regFeeSuite();

				setMFBaseActlFee();
			}

			setNoOfMoRegCrdtMsg();
		}
		else if (csTransCd.equals(TransCdConstant.TOWP))
		{

			carrCompTransData[UPDT].setNoMoChrg(
				Math.max(
					0,
					((ciToYear * 12 + ciToMonth)
						- (ciFromYr * 12 + ciFromMo)
						+ 1)));
			//Set up and call fee calc for tow truck 
			//ASSIGN reset inventory table and count to original values 
			//PROC Call S609 fee calc procs needed for tow truck

			// Test if exempt indicator is set 
			if (caRegUpdt.getExmptIndi() != 1)
			{
				//Init Core Fee module 
				ciFCalcRow = UPDT;
				carrCompTransData[ORIG].setCustActualRegFee(
					new Dollar(0.0));
				carrCompTransData[UPDT].setCustActualRegFee(
					new Dollar(0.0));
				//Tow Truck Small Plt (Regional office only) 
				csAcctItmCd = "TTS";
				if (caWSOfficeIds.getOfcIssuanceCd() == COUNTY)
				{
					csAcctItmCd = "TTS";
				}
				else
				{
					csAcctItmCd = "TTS-R";
				}
				// defect 10685
				// For Fee Simplify, do not charge Tow Truck $15
				caCommFeesData.setRegFee(new Dollar(0.0));
				if (ciTransGregorianDate <
					SystemProperty.getFeeSimplifyStartDate().
						getYYYYMMDDDate())
				{
					caCommFeesData.setRegFee(new Dollar(15.0));
				}
				// end defect 10685
				caCommFeesData.setRegPeriodLngth(12);
				calcFlatFee();
				// defect 9553
				// Have already calculated caBaseRegFee in calcFlatFee()
				// so don't need to call applyMultiYrAdj(). Fee may be 0 
				// for 'not in window' registration (because 'number of 
				// months to charge does not work for Tow Truck, so 
				// charge minimum of 15.00.
				//caItmPrice = caCommFeesData.getRegFee();
				//applyMultiYrAdj();
				caItmPrice = caBaseRegFee;
				if (caItmPrice.equals(new Dollar(0.0)))
				{
					caItmPrice = caCommFeesData.getRegFee();
				}
				// end defect 9553
				asgnResToFeesData();
				setMFBaseActlFee();
			}

			setNoOfMoRegCrdtMsg();
		}
		//Set up and call fee calc for renew
		else
		{
			//reset inventory table and count to original values. 
			//ASSIGN FCalc{0}.NoMoCharge 
			carrCompTransData[UPDT].setNoMoChrg(
				Math.max(
					0,
					(ciToYear * 12 + ciToMonth)
						- (ciFromYr * 12 + ciFromMo)
						+ 1));
			//Call fee calc procs needed for renew

			// defect 8900
			// Remove Exempt Indi check
			//Test if exempt indicator is set 
			//if (caReg1.getExmptIndi() != 1)
			//{
			//	//Init Core Fee module 
			//	ciFCalcRow = UPDT;
			//	carrCompTransData[0].setCustActualRegFee(new Dollar(0.0));
			//	carrCompTransData[1].setCustActualRegFee(new Dollar(0.0));
			//	regFeeSuite();
			//	setMFBaseActlFee();
			//	setNoOfMoRegCrdtMsg();
			//}

			//Init Core Fee module 
			ciFCalcRow = UPDT;
			carrCompTransData[ORIG].setCustActualRegFee(
				new Dollar(0.0));
			carrCompTransData[UPDT].setCustActualRegFee(
				new Dollar(0.0));
			// defect 9126
			// Check for 'not HQ' (ie: skip reg fees if HQ). 
			if (caWSOfficeIds.getOfcIssuanceCd() != HEADQUARTERS)
			{
				regFeeSuite();
			}
			// end defect 9126
			setMFBaseActlFee();
			setNoOfMoRegCrdtMsg();
			// end defect 8900
		}
	}
	/**
	 * Call fee calc procedures needed for title 
	 * 
	 */
	private void callFeeCalcTtlMthds()
	{
		//Init Core Fee module 
		ciFCalcRow = UPDT;
		carrCompTransData[ORIG].setCustActualRegFee(new Dollar(0.0));
		carrCompTransData[UPDT].setCustActualRegFee(new Dollar(0.0));

		// defect 8900
		// Use Exempt Indi to determine whether to charge Reg Fee for
		// Title (uses individual Charge Title\Charge TERP\etc for
		// whether to charge individual title type fees). 
		carrCompTransData[UPDT].setNoChrgIndi(0);
		// end defect 8900

		//If REJCOR (correct title rejection) suppress sales tax calc 
		if (csTransCd.equals(TransCdConstant.REJCOR))
		{
			RegTtlAddlInfoData lRegTtlAddlInfoData =
				carrCompTransData[ciFCalcRow].getRegTtlAddlInfoData();
			//Calc Title Fees 
			if (lRegTtlAddlInfoData.getChrgTtlFeeIndi() == 1)
			{
				assignTtl();
			}
			// defect 6563
			//Add Title TERP Fees
			if (lRegTtlAddlInfoData.getChrgTtlTERPFeeIndi() == 1)
			{
				calcTtlTERPFee();
			}
			// end defect 6563
			//Title Transfer Penalty 
			if (lRegTtlAddlInfoData.getTrnsfrPnltyIndi() == 1)
			{
				calcTtlTrnsfrPnlty();
			}
			//Registration Transfer 
			if (lRegTtlAddlInfoData.getChrgTrnsfrFeeIndi() == 1)
			{
				calcRegTrnsfr();
			}
			// defect 9368
			calcPltTrnsfrFee();
			calcBuyerTagFee();
			// end defect 9368 
			// defect 11051 
			calcRbltSlvgFee(); 
			// end defect 11051
		}
		else
		{
			titleFeeSuite();
		}
		//Call reg fee suite if needed 
		csTxtRegMoSold = CommonConstant.STR_SPACE_EMPTY;
		// defect 9126
		csTxtSpclPltMoSold = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9126
		if (testForRsnNotCallRegFeeSuite())
		{
		}
		else
		{
			//set RegEffDate for Title with Exchange depending on window
			// defect 8404
			// Check for RegPeriodLngth = 1 (ie: seasonal ag) and set 
			// RegEffDate to current date
			//if (testIfWithinWnd())
			// defect 8438
			// need to lookup common fees if it is null
			if (caCommFeesData == null)
			{
				calcCommnFees();
			}
			// end defect 8438
			if (testIfWithinWnd()
				&& caCommFeesData.getRegPeriodLngth() != 1)
				// end defect 8404
			{
				// defect 10732
				// Title within window does not have to Renew.  
				// Keep original RegEffDt.			
				//			 ciRegEffDate =
				//				 Math.max(
				//					 ciEffectiveExpDatePlusOne,
				//					 ciTransGregorianDate);
				ciRegEffDate = caRegOrig.getRegEffDt();
				// end defect 10732
			}
			else
			{
				caRegUpdt.setRegEffDt(ciTransGregorianDate);
			}
			//caReg1.setRegEffDt( ciRegEffDate );
			//?????
			//carrCompTransData[UPDT].setInvItemCount( ciInvItmCount > 0 ? 1:0 );
			//adjust InvItmCount for Dealer Title Form 31 
			if (csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
			{
				carrCompTransData[UPDT].setInvItemCount(
					ciInvItmCount > 1 ? 1 : 0);
			}
			//ASSIGN FCalc{0}.NoChrgRegisEmi, FCalc{1}.NoChrgRegisEmi
			//carrCompTransData[UPDT].setNoChrgRegisEmi(ciChrgRegisEmiIndi);			

			// defect 8516
			// Use lookup to CommonFees.EmissionPrcnt			
			//if (caReg0.getRegClassCd() != 10)
			int liRegClassCd = caRegOrig.getRegClassCd();
			int liRTSCurrDate = caRegOrig.getRegEffDt();
			// defect 11003
			// If no RegClassCd change and no weight change,
			// do 'even exchange' (use new RegEffDt for both old and new regis)
			if (caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd() &&
				caRegOrig.getVehGrossWt() == caRegUpdt.getVehGrossWt())
			{
				liRTSCurrDate = caRegUpdt.getRegEffDt();
			}
			// end defect 11003
			caCommFeesData =
				CommonFeesCache.getCommonFee(
					liRegClassCd,
					liRTSCurrDate);
			if (caCommFeesData == null)
			{
				(
					carrCompTransData[ORIG]
						.getRegTtlAddlInfoData())
						.setNoChrgRegEmiFeeIndi(
					1);
			}
			else
			{
				if (caCommFeesData
					.getEmissionsPrcnt()
					.compareTo(ZERO_DOLLAR)
					== 0)
				{
					(
						carrCompTransData[ORIG]
							.getRegTtlAddlInfoData())
							.setNoChrgRegEmiFeeIndi(
						1);
				}
				else
				{
					(
						carrCompTransData[ORIG]
							.getRegTtlAddlInfoData())
							.setNoChrgRegEmiFeeIndi(
						0);
				}
			}
			// end defect 8516

			// defect 8900
			if (caRegUpdt.getExmptIndi() == 1)
			{
				carrCompTransData[UPDT].setNoChrgIndi(1);
			}
			else
			{
				carrCompTransData[UPDT].setNoChrgIndi(0);
			}
			// end defect 8900

			regFeeSuite();
			//defect 8900
			// Set credit remaining for Title with Exchange 
			setCrdtRmng();
			// end defect 8900			
			setMFBaseActlFee();
			setNoOfMoRegCrdtMsg();
			//ressign InvItmCount for Dealer Title Form 31.
			if (csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
			{
				carrCompTransData[UPDT].setInvItemCount(ciInvItmCount);
			}
		}
	}
	/**
	 * Check for title fees  
	 * 
	 */
	private void checkForTtlFees()
	{
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR))
		{
			calcTtlFee();
		}
		else if (
			csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
		{
			calcDlrTtl();
		}
		else if (csTransCd.equals(TransCdConstant.CORTTL))
		{
			calcTtlCorr();
		}
	}
	/**
	 * Check for registration only fees 
	 * 
	 */
	private void checkRegOnlyFees()
	{
		if (csTransCd.equals(TransCdConstant.RENEW))
		{
			calcRegRnwl();
		}
		// defect 10772
		// Add WRENEW. Do not charge mail fee for WRENEW.
		else if (csTransCd.equals(TransCdConstant.IRENEW)
			|| csTransCd.equals(TransCdConstant.WRENEW))
		{
			if (csTransCd.equals(TransCdConstant.IRENEW))
			{
				// defect 9313
				//Always apply mail Fee
				(carrCompTransData[UPDT]
					.getRegTtlAddlInfoData())
					.setProcsByMailIndi(
					1);
				// end defect 9313
			}
						
			calcRegRnwl();
		}
		// end defect 10772
		else if (csTransCd.equals(TransCdConstant.EXCH))
		{
			calcExchg();
		}
		else if (csTransCd.equals(TransCdConstant.SBRNW))
		{
			calcSubCon();
		}
		else if (csTransCd.equals(TransCdConstant.DUPL))
		{
			calcDuplRecpt();
		}
		else if (csTransCd.equals(TransCdConstant.REPL))
		{
			calcRepl();
		}
		else if (csTransCd.equals(TransCdConstant.PAWT))
		{
			// defect 10952
			// Test if weight has changed. FeeCalcCat 4
			// (no regis) can come thru here to add weight, 
			// but it's not FeeCalcCat 2 or 5 (weight based) 
			// so does not need to calc reg fees.
			if (!testIfVehWgtNotChnged())
			{
				calcPermWght();
			}
		}
		//Test Apprehended 
		else if (csTransCd.equals(TransCdConstant.CORREGX))
		{
			calcCorrectionFee();
		}
		//Test trans code = correction
		else if (csTransCd.equals(TransCdConstant.CORREG))
		{
			calcCorrectionFee();
		}
	}
	/**
	 * Assign Credit Remaining 
	 * 
	 */
	private void crdtRemaining()
	{
		if (carrCompTransData[ORIG].getNoMoChrg() > 0)
		{
			MFVehicleData laMFVD =
				carrCompTransData[ORIG].getVehicleInfo();
			RegistrationData laRegData = laMFVD.getRegData();
			int liRegClassCd = laRegData.getRegClassCd();

			//TEST No reg credit given for antique
			// defect 9158
			// No credit is given for Farm Trailer (RegClassCd 16)
			if (liRegClassCd != 4
				&& liRegClassCd != 5
				&& liRegClassCd != 16)
			{
				// end defect 9158
				// defect 8900
				// No credit is given when moving from Exempt unless
				// this is an Apprehended or Perm Addl Weight event. 
				if (caRegOrig.getExmptIndi() != 1
					|| csTransCd.equals(TransCdConstant.PAWT)
					|| csTransCd.equals(TransCdConstant.CORREGX))
				{
					// set to zero if null
					if (caRegisPenaltyFee == null)
					{
						caRegisPenaltyFee = new Dollar(0.0);
					}
					Dollar laDieselFee =
						carrCompTransData[ciFCalcRow].getDieselFee();
					caItmPrice =
						(new Dollar(0.0)
							.subtractNoRound(caBaseRegFee)
							.subtractNoRound(laDieselFee)
							.subtractNoRound(caRegisPenaltyFee))
							.round();
					if (caItmPrice.compareTo(ZERO_DOLLAR) < 0)
					{
						csAcctItmCd = "CREDIT";
						ciItmQty = 1;
						asgnResToFeesData();
						updateCustActlRegFee();
					}
				}
				// end defect 8900
			}
		}
	}
	/**
	 * 
	 * Return boolean to denote whether charge Ins Fee ("Reg Fee-DPS") 
	 * 
	 * @return boolean 
	 */
	protected boolean testIfDPSFeesReqd()
	{
		boolean lbDPSFeesReqd =
			carrCompTransData[UPDT].getNoMoChrg()
				!= carrCompTransData[ORIG].getNoMoChrg()
				|| !caRegOrig.getRegPltCd().equals(
					caRegUpdt.getRegPltCd())
				|| !caRegOrig.getRegStkrCd().equals(
					caRegUpdt.getRegStkrCd())
				|| !caVehOrig.getVehClassCd().equals(
					caVehUpdt.getVehClassCd())
				|| caRegOrig.getRegClassCd() != caRegUpdt.getRegClassCd()
				|| !testIfVehWgtNotChnged()
				|| caVehOrig.getVehModlYr() != caVehOrig.getVehModlYr();

		return lbDPSFeesReqd;
	}

	/**
	 * Determine whether to charge RegAddlFee
	 *
	 * @return boolean
	 */
	protected boolean determineChrgRegAddlFee()
	{
		// If INS & (EXCH || DTA or Title with GDN date BEFORE 
		// DTAInsStartDate) do not charge RegAddlFee
		// Else charge
		// 
		boolean lbChrgRegAddlFeeIndi = true;

		// defect 9425 
		if (csAcctItmCd.equals("INS"))
		{
			if (csTransCd.equals(TransCdConstant.EXCH))
			{
				lbChrgRegAddlFeeIndi = false;
			}
			else if (
				UtilityMethods.getEventType(csTransCd).equals(
					TransCdConstant.TTL_EVENT_TYPE)
					|| UtilityMethods.getEventType(csTransCd).equals(
						TransCdConstant.DTA_EVENT_TYPE))
			{
				lbChrgRegAddlFeeIndi = testIfDPSFeesReqd();
			}

			//				// DTA Transaction || Title w/ GDN 
			//				if (csTransCd.equals(TransCdConstant.DTANTD)
			//					|| csTransCd.equals(TransCdConstant.DTANTK)
			//					|| csTransCd.equals(TransCdConstant.DTAORD)
			//					|| csTransCd.equals(TransCdConstant.DTAORK)
			//					|| getDLRGDN().length() > 0)
			//				{
			//					// Convert transDate int to RTSDate format
			//					RTSDate lTransDate =
			//						new RTSDate(
			//							RTSDate.YYYYMMDD,
			//							ciTransGregorianDate);
			//					// If transaction date is before INS start date, do not charge RegAddlFee
			//					if (lTransDate
			//						.compareTo(SystemProperty.getDTAInsStartDate())
			//						== -1)
			//					{
			//						lbChrgRegAddlFeeIndi = false;
			//					}
			//				}
		}
		// end defect 9425 

		// defect 6257
		// Do not charge Young Farmer Fee if registration has not been
		// extended and they were already a Farm plate.
		// ie: could be Addl Weight on existing Farm plate, or Title
		// Transfer on existing Farm plate w/out renewal
		// Not ready to do this defect. VTR questions about when to charge...
		// see: T:\RTS II Team\Projects\Fees Project\Fees Project Defects - consolidated.doc 		
		//			if (carrCompTransData[UPDT].getNoMoChrg() !=
		//					carrCompTransData[ORIG].getNoMoChrg() ||
		//				caRegOrig.getRegClassCd() != 
		//					caRegUpdt.getRegClassCd())
		//			{
		//				lbChrgRegAddlFeeIndi = true;
		//			}
		//			else
		// end defect 6257

		return lbChrgRegAddlFeeIndi;
	}
	/**
	 * Edit for dealer title ExpMo/Yr 
	 * 
	 */
	private boolean editDlrTtlExpMoYr()
	{
		// Use either DlrTrans{DlrTransRow}.NewRegExpMo/Yr or 
		// MfVeh{0}.RegExpMo/Yr 
		int liDTARegExpMo = 0;
		int liDTARegExpYr = 0;
		if (caRegUpdt.getRegExpMo() > 0)
		{
			liDTARegExpMo = caRegUpdt.getRegExpMo();
			liDTARegExpYr = caRegUpdt.getRegExpYr();
		}
		else
		{
			liDTARegExpMo = caRegOrig.getRegExpMo();
			liDTARegExpYr = caRegOrig.getRegExpYr();
			caRegUpdt.setRegExpMo(liDTARegExpMo);
			caRegUpdt.setRegExpYr(liDTARegExpYr);
		}
		//Set minimum ToMonth/ToYear
		setMinToMoToYr();
		int liMonthsTilExp = (ciToYear * 12) + ciToMonth;
		int liNewExpMonths = liDTARegExpYr * 12 + liDTARegExpMo;
		if (liNewExpMonths != liMonthsTilExp)
		{
			//edit for DTARegExpMo/Yr must be at least the minimum allowed
			if (liNewExpMonths <= liMonthsTilExp)
			{
				carrCompTransData[UPDT].setDTAErrorMsg(
					"The expiration month/year entered must be at least "
						+ ciToMonth
						+ "/"
						+ ciToYear);
				return false;
			}
			// edit for DTARegExpMo/Yr must not be greater than
			// maximum allowed
			//Set maximum ToMonth/ToYear
			// defect 10697
			// calcMaxToMoToYr() replaced by calcMaxToMoToYrMYr()
			//if (calcMaxToMoToYr())
			//{
				calcMaxToMoToYrMYr();

				// defect 9126
				// Adjust ToMonth\Yr if Special Plate
				adjMaxForSpclPlt();
				// end defect 9126

				//ASSIGN ToMonthMax, ToYearMax, ExpMoYrMax$, ExpMaxMonths
				ciToMonthMax = ciToMonth;
				ciToYearMax = ciToYear;
				if (ciToMonth < 10)
				{
					//For Display on screen
					csExpMoYrMax = "0" + ciToMonth + "/" + ciToYear;
				}
				else
				{
					csExpMoYrMax = ciToMonth + "/" + ciToYear;
				}
				ciExpMaxMonths = ciToYear * 12 + ciToMonth;
			//}
			
			// end defect 10697
			liMonthsTilExp = ciToYear * 12 + ciToMonth;
			if (liNewExpMonths > liMonthsTilExp)
			{
				carrCompTransData[UPDT].setDTAErrorMsg(
					"The expiration month/year entered can not be "
						+ "later than "
						+ ciToMonth
						+ "/"
						+ ciToYear);
				return false;
			}
			//ASSIGN ToMonth/ToYear from DTARegExpMo/Yr
			ciToMonth = liDTARegExpMo;
			ciToYear = liDTARegExpYr;
			//ASSIGN FCalc{0}.NoMoCharge 
			carrCompTransData[UPDT].setNoMoChrg(
				Math.max(
					0,
					(ciToYear * 12 + ciToMonth)
						- (ciFromYr * 12 + ciFromMo)
						+ 1));
			// defect 8477			
			// if RegPeriodLngth < 12, do not edit for 12 months			
			if (caCommFeesData.getRegPeriodLngth() >= 12)
			{
				//edit for registration charged must be at least 12 months 
				if (carrCompTransData[UPDT].getNoMoChrg() < 12)
				{
					carrCompTransData[UPDT].setDTAErrorMsg(
						"The expiration month/year entered does not"
							+ " charge at least a full year.");
					return false;
				}
			}
			// end defect 8477			
			//edit for DTARegExpMo vs CommonFees{0}.FxdExpMo 
			if (caCommFeesData.getFxdExpMo() != 0
				&& caCommFeesData.getFxdExpMo() != ciToMonth)
			{
				carrCompTransData[UPDT].setDTAErrorMsg(
					"The expiration month entered does not match "
						+ "fixed expiration month.");
				return false;
			}
			//edit for DTARegExpYr vs CommonFees{0}.FxdExpYr 
			if (caCommFeesData.getFxdExpYr() != 0
				&& caCommFeesData.getFxdExpYr() != ciToYear)
			{
				carrCompTransData[UPDT].setDTAErrorMsg(
					"The expiration year entered does not match"
						+ " fixed expiration month.");
				return false;
			}
		}
		return true;
	}
	/**
	 * Get DLR-GDN if exist.
	 * 
	 * @return String
	 */
	protected String getDLRGDN()
	{
		String lsDlrGdn = CommonConstant.STR_SPACE_EMPTY;
		if (carrCompTransData.length > 0
			&& carrCompTransData[UPDT].getVehicleInfo() != null
			&& carrCompTransData[UPDT].getVehicleInfo().getTitleData()
				!= null
			&& carrCompTransData[UPDT]
				.getVehicleInfo()
				.getTitleData()
				.getDlrGdn()
				!= null)
		{
			lsDlrGdn =
				carrCompTransData[UPDT]
					.getVehicleInfo()
					.getTitleData()
					.getDlrGdn();
		}
		return lsDlrGdn;
	}

	/**
	 * 
	 * Determine if is LP Plate 
	 * 
	 * @return boolean 
	 */
	private boolean isLP()
	{
		String lsRegPltCd = caRegUpdt.getRegPltCd();
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);
		if (laPltTypeData != null)
		{
			// defect 10548
			// Include Multi-Year Official Plates			
			// defect 11152 
			// Add checks for internet and web agent renew
			//return laPltTypeData.getNeedsProgramCd().equals(
			//		SpecialPlatesConstant.LP_PLATE);
			return laPltTypeData.getNeedsProgramCd().equals(
				SpecialPlatesConstant.LP_PLATE)
				|| (laPltTypeData.isMultiYrOfclPlt()
					&& !csTransCd.equals(TransCdConstant.RENEW)
					&& !csTransCd.equals(TransCdConstant.IRENEW)
					&& !csTransCd.equals(TransCdConstant.WRENEW));
			// end defect 11152 
			// end defect 10548 
		}
		else
		{
			return false;
		}
	}
	/**
	 * Determine whether registration is expired
	 * 
	 * @return boolean
	 */
	private boolean isRegExpired()
	{
		boolean lbRegIsExpired = false;
		try
		{
			if (caRegOrig.getRegInvldIndi() == 1
				|| CommonValidations.isRegistrationExpired(
					caRegOrig.getRegExpMo(),
					caRegOrig.getRegExpYr(),
					ciTransGregorianDate))
			{
				lbRegIsExpired = true;
			}

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		return lbRegIsExpired;
	}
	/**
	 * Look up and assign results for timed permits 
	 * 
	 */
	private void lookUpAssgnResForTmdPrmts()
	{
		//Look up Item Price for timed permits
		caAcctCdData =
			AccountCodesCache.getAcctCd(
				csAcctItmCd,
				caRegUpdt.getRegEffDt());
		caItmPrice = caAcctCdData.getPrmtFlatFee();
		ciItmQty = 1;
		asgnResToFeesData();

		// defect 10491 
		PermitData laPrmtData =
			(PermitData) carrCompTransData[UPDT].getTimedPermitData();
		laPrmtData.setAcctItmCd(csAcctItmCd);
		laPrmtData.setPrmtPdAmt(caItmPrice);
		// end defect 10491  			
	}

	/**
	 * Manage $Display Record expiration month and year 
	 * 
	 */
	private void mngDispRecExpMoYr()
	{
//		if (caRegUpdt.getRegPltCd() != null)
//		{
//			caItemCdsData =
//				ItemCodesCache.getItmCd(caRegUpdt.getRegPltCd());
//		}
		setMinToMoToYr();
		//Set maximum ToMonth/ToYear 
		//Calc maximum ToMonth, ToYear

		// defect 10357 \ 10523
		// IRENEW does not want Maximum, does not go to REG029
		//if (calcMaxToMoToYr())
		// defect 10772
		// add WRENEW
		if (csTransCd.equals(TransCdConstant.IRENEW)
			|| csTransCd.equals(TransCdConstant.WRENEW))
		{
			// end defect 10772
			ciToMonthMax = ciToMonthMin;
			ciToYearMax = ciToYearMin;
			ciExpMaxMonths = ciExpMinMonths;
		}
		// defect 10697
		// calcMaxToMoToYr() replaced by calcMaxToMoToYrMYr()
		//else if (calcMaxToMoToYr())
		else
		{
			// end defect 10357 \ 10523
			
			calcMaxToMoToYrMYr();
			// end defect 10697

			// defect 9126
			// Adjust maximum ToMonth\Yr if Special Plate
			if (caCommFeesData.getFxdExpMo() == 0
				&& caCommFeesData.getFxdExpYr() == 0)
			{
				adjMaxForSpclPlt();
			}
			// end defect 9126

			//ASSIGN ToMonthMax, ToYearMax, ExpMoYrMax, ExpMaxMonths 
			ciToMonthMax = ciToMonth;
			ciToYearMax = ciToYear;
			if (ciToMonth < 10)
			{
				//For Display on screen
				csExpMoYrMax = "0" + ciToMonth + "/" + ciToYear;
			}
			else
			{
				csExpMoYrMax = ciToMonth + "/" + ciToYear;
			}
			ciExpMaxMonths = ciToYear * 12 + ciToMonth;
		}
		
		// defect 4830
		if (ciExpMinMonths > ciExpMaxMonths)
		{
			ciToMonthMax = ciToMonthMin;
			ciToYearMax = ciToYearMin;
		}
		calcMaxMinFees();
	}
	/**
	 * Calculate fees for Non-Resident Agriculture Permits 
	 * (for in-state and out-of-state products) 
	 * 
	 */
	private void nonResAgrPrmts()
	{
		//Get permit fee percentage
		caAcctCdData =
			AccountCodesCache.getAcctCd(
				csAcctItmCd,
				caRegUpdt.getRegEffDt());
		calcCommnFees();
		carrCompTransData[ciFCalcRow].setNoMoChrg(
			caCommFeesData.getRegPeriodLngth());
		// defect 10685
		// FeeCalcCat = 5 is the new Fee Simplification method		
		// calcWgtBsdFee();
		if (caCommFeesData.getFeeCalcCat() == 5)
		{
			calcRegClsGrpWgtBsdFee();
		}
		else
		{
			calcWgtBsdFee();			
		}
		// end defect 10685
		adjAddlDislFee();
		Dollar laPrmtFeePrcnt = caAcctCdData.getPrmtFeePrcnt();
		Dollar laHundred = new Dollar(100.0);
		Dollar laDieselFee =
			carrCompTransData[ciFCalcRow].getDieselFee();
		caItmPrice =
			(laPrmtFeePrcnt.divideNoRound(laHundred))
				.multiplyNoRound(caBaseRegFee.addNoRound(laDieselFee))
				.round();
		ciItmQty = 1;
		asgnResToFeesData();
	}
	/**
	 * Recalculate the fees. This method is called from REG029 screen 
	 * on changing ExpMo/Yr.
	 */
	private CompleteTransactionData reCalcFees()
	{
		csTransCd = carrCompTransData[UPDT].getTransCode();
		caMFVehOrig = carrCompTransData[ORIG].getVehicleInfo();
		if (caMFVehOrig != null)
		{
			caVehOrig = caMFVehOrig.getVehicleData();
			caRegOrig = caMFVehOrig.getRegData();
//			caTtlOrig = caMFVehOrig.getTitleData();
//			caVehMiscOrig = carrCompTransData[ORIG].getVehMisc();
			if (caRegOrig.getRegExpYr() != 0
				&& caRegOrig.getRegExpMo() != 0)
			{
				if (caRegOrig.getRegExpMo() != 12)
				{
					ciEffectiveExpDatePlusOne =
						(caRegOrig.getRegExpYr() * 10000)
							+ ((caRegOrig.getRegExpMo() + 1) * 100)
							+ 1;
				}
				else
				{
					//ciEffectiveExpDatePlusOne = ((caReg0.getRegExpYr() + 1) * 10000) + (caReg0.getRegExpMo() * 100) + 1;
					ciEffectiveExpDatePlusOne =
						((caRegOrig.getRegExpYr() + 1) * 10000)
							+ 100
							+ 1;
				}
				// ciEffectiveExpDate = (caReg0.getRegExpYr() * 10000) + ((caReg0.getRegExpMo()) * 100) + 31;
				RTSDate laRTSEffExpDate =
					(
						new RTSDate(
							RTSDate.YYYYMMDD,
							ciEffectiveExpDatePlusOne)).add(
						RTSDate.DATE,
						-1);
				ciEffectiveExpDate = laRTSEffExpDate.getYYYYMMDDDate();
			}
		}
		caMFVehUpdt = carrCompTransData[UPDT].getVehicleInfo();
		if (caMFVehUpdt != null)
		{
			caVehUpdt = caMFVehUpdt.getVehicleData();
			caRegUpdt = caMFVehUpdt.getRegData();
			caTtlUpdt = caMFVehUpdt.getTitleData();
			caVehMiscUpdt = carrCompTransData[UPDT].getVehMisc();
			if (caTtlUpdt != null)
			{
				ciMustChangePltIndi = caTtlUpdt.getMustChangePltIndi();
			}
			if (caRegUpdt != null)
			{
				//Perform SQL for inventory processing code of plate
//				if (caRegUpdt.getRegPltCd() != null)
//				{
//					caItemCdsData =
//						ItemCodesCache.getItmCd(
//							caRegUpdt.getRegPltCd());
//				}
				ciUnregisterVehIndi = caRegUpdt.getUnregisterVehIndi();
				ciOffHwyUseIndi = caRegUpdt.getOffHwyUseIndi();
				ciRegWaivedIndi = caRegUpdt.getRegWaivedIndi();
			}
		}
		RegTtlAddlInfoData laRegTtlAddlInfo =
			carrCompTransData[UPDT].getRegTtlAddlInfoData();
		if (laRegTtlAddlInfo != null)
		{
			ciCorrtnEffMo = laRegTtlAddlInfo.getCorrtnEffMo();
			ciCorrtnEffYr = laRegTtlAddlInfo.getCorrtnEffYr();
			ciChrgFeeIndi = laRegTtlAddlInfo.getChrgFeeIndi();
			ciRegExpReason = laRegTtlAddlInfo.getRegExpiredReason();
			if (ciRegExpReason == 3)
			{
				carrCompTransData[UPDT].setRegPnltyChrgIndi(1);
			}
		}
		//Built TransGregorianDate in YYYYMMDD format
		if (carrCompTransData[UPDT].getTransactionDate() != 0)
		{
			ciTransGregorianDate =
				carrCompTransData[UPDT].getTransactionDate();
		}
		else
		{
			ciTransGregorianDate =
				(RTSDate.getCurrentDate()).getYYYYMMDDDate();
		}
		//Set the RegEffdate for Title
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR)
			&& testIfWithinWnd()
			&& (carrCompTransData[UPDT].getNoMoChrg() > 0))
		{
			// defect 8900
			// Check for Standard Exempt
			//if (ciRegWaivedIndi == 1
			//	|| ciOffHwyUseIndi == 1
			//	|| (caVehMisc1 != null
			//		&& caVehMisc1.getSpclPltProgIndi() == 1)
			//	|| caReg1.getRegClassCd() == 39)
			if (ciRegWaivedIndi == 1
				|| ciOffHwyUseIndi == 1
				|| (caVehMiscUpdt != null
					&& caVehMiscUpdt.getSpclPltProgIndi() == 1)
				|| (CommonFeesCache
					.isStandardExempt(caRegUpdt.getRegClassCd())))
			{
				// end defect 8900			
				//Title for Off Hiway, Reg Waived, Spcl Plt Prog, 
				//Exempt RegClass 
				//ASSIGN RegEffDate = Today 
				caRegUpdt.setRegEffDt(ciTransGregorianDate);
			}
			else
			{
				// set RegEffDate for Title (may change later 
				// for Title w/ Renewal/Exchange)
				// Test if new Vehicle  || expired
				// defect 10685
				// Title should be RegEffDt = Today 
				//if (ciNoMfVeh == 0
				//	|| (caRegOrig.getRegExpMo() == 0
				//		&& caRegOrig.getRegExpYr() == 0)
				//	|| isRegExpired())
				//{
				//	//ASSIGN RegEffDate = Today 
				//	caRegUpdt.setRegEffDt(ciTransGregorianDate);
				//}
				//else
				//{
				//	ciRegEffDate = caRegOrig.getRegEffDt();
				//	caRegUpdt.setRegEffDt(ciRegEffDate);
				//}
				caRegUpdt.setRegEffDt(ciTransGregorianDate);
				// end defect 10685
			}
		}
		//set the current date
		ciCurrMo =
			(new RTSDate(RTSDate.YYYYMMDD, ciTransGregorianDate))
				.getMonth();
		ciCurrYr =
			(new RTSDate(RTSDate.YYYYMMDD, ciTransGregorianDate))
				.getYear();

		// defect 8900
		// Use local object so that cache object is not changed				
		//caWSOfficeIds =
		//	OfficeIdsCache.getOfcId(
		//		carrCompTransData[UPDT].getOfcIssuanceNo());
		Object laOfficeIdsData =
			UtilityMethods.copy(
				OfficeIdsCache.getOfcId(
					carrCompTransData[UPDT].getOfcIssuanceNo()));
		caWSOfficeIds = (OfficeIdsData) laOfficeIdsData;
		// end defect 8900

		//Set office issuance number based on various conditions
		//For Apprehended vehicle
		if (laRegTtlAddlInfo != null
			&& laRegTtlAddlInfo.getApprhndFndsCntyNo() != 0)
		{
			caWSOfficeIds.setOfcIssuanceNo(
				laRegTtlAddlInfo.getApprhndFndsCntyNo());
		}
		else
		{
			caWSOfficeIds.setOfcIssuanceNo(
				carrCompTransData[UPDT].getOfcIssuanceNo());
			// If not a county, use office issuance number from 
			// retrieved vehicle record
			if (caRegUpdt != null
				&& caWSOfficeIds.getOfcIssuanceCd() != COUNTY)
			{
				caWSOfficeIds.setOfcIssuanceNo(
					caRegUpdt.getResComptCntyNo());
			}
		}
		//clear the RegFeesData 
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		laRegFeesData.setVectFees(new Vector());
		//ASSIGN FromMonth/Year
		ciFromYr =
			(carrCompTransData[UPDT].getRegFeesData()).getFromYr();
		ciFromMo =
			(carrCompTransData[UPDT].getRegFeesData()).getFromMo();

		// defect 9126			
		// Set up for Special Plates
		if (caRegUpdt != null
			&& caRegUpdt.getRegPltCd()
				!= null
					& PlateTypeCache.isSpclPlate(
						caRegUpdt.getRegPltCd()))
		{
//			caSpclPltRegOrig = caMFVehOrig.getSpclPltRegisData();
			caSpclPltRegUpd = caMFVehUpdt.getSpclPltRegisData();
			setSpclPltFromMoFromYr();
		}
		// end defect 9126

		//ASSIGN ToMonth/Year to values entered on Reg029 screen.
		ciToMonth = carrCompTransData[UPDT].getExpMo();
		ciToYear = carrCompTransData[UPDT].getExpYr();
		//Determine if Reject correction should not charge fees
		if (csTransCd.equals(TransCdConstant.REJCOR)
			&& caRegOrig != null
			&& caRegUpdt != null
			&& (caRegOrig.getRegClassCd() == 4
				|| caRegOrig.getRegClassCd() == 5)
			&& caRegOrig.getRegInvldIndi() == 0
			&& caRegOrig.getRegPltCd() != null
			&& caRegUpdt.getRegPltCd() != null
			&& caRegOrig.getRegPltCd().equals(caRegUpdt.getRegPltCd())
			&& caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd())
		{
			caRegUpdt.setRegExpYr(ciToYear);
		}
		else
		{
			//ASSIGN FCalc{0}.NoMoCharge 
			carrCompTransData[UPDT].setNoMoChrg(
				Math.max(
					0,
					(ciToYear * 12 + ciToMonth)
						- (ciFromYr * 12 + ciFromMo)
						+ 1));
		}
		//Get the Number of Months of Credit
		carrCompTransData[ORIG].setNoMoChrg(
			carrCompTransData[UPDT].getNoMoCredit());
		asgnFscalYr();
		if (csTransCd.equals(TransCdConstant.TOWP))
		{
			//ASSIGN RegEffDate = Today
			caRegUpdt.setRegEffDt(ciTransGregorianDate);
			//Assign tow truck variables to variables used in common 
			// fee calc process
			caCommFeesData = new CommonFeesData();
			// defect 10685
			// For Fee Simplify, do not charge Tow Truck $15
			caCommFeesData.setRegFee(new Dollar(0.0));
			if (ciTransGregorianDate <
				SystemProperty.getFeeSimplifyStartDate().
					getYYYYMMDDDate())
			{
				caCommFeesData.setRegFee(new Dollar(15.0));
			}
			// end defect 10685
			caCommFeesData.setRegPeriodLngth(12);
			caCommFeesData.setRegPrortnIncrmnt(12);
			caCommFeesData.setMaxallowbleRegMo(15);
			caCommFeesData.setFxdExpMo(1);
			caCommFeesData.setFxdExpYr(0);
			caRegUpdt.setRegExpMo(caCommFeesData.getFxdExpMo());
		}
		callFeeCalcMthdBsdOnTransCd();
		setCrdtRmng();
		//ASSIGN maximum fee total to screen field 
		RegFeesData laRegFeesData2 =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData2.getVectFees();
		caFeeTotalMax = new Dollar(0.0);
		for (int i = 0; i < lvFeesData.size(); i++)
		{
			Dollar laItmPrice =
				(Dollar) ((FeesData) lvFeesData.elementAt(i))
					.getItemPrice();
			caFeeTotalMax = caFeeTotalMax.addNoRound(laItmPrice);
		}
		//adjust RegEffDate to today if title in window renewed 
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR)
			&& testIfWithinWnd()
			&& (carrCompTransData[UPDT].getNoMoChrg() > 0))
		{
			ciRegEffDate = ciTransGregorianDate;
			caRegUpdt.setRegEffDt(ciRegEffDate);
		}
		carrCompTransData[UPDT].getRegFeesData().setToMonthDflt(
			ciToMonth);
		carrCompTransData[UPDT].getRegFeesData().setToYearDflt(
			ciToYear);
		carrCompTransData[UPDT].setTxtRegMoSold(csTxtRegMoSold);
		// defect 9126
		carrCompTransData[UPDT].setTxtSpclPltMoSold(csTxtSpclPltMoSold);
		// end defect 9126
		carrCompTransData[UPDT].setRegisPenaltyFee(caRegisPenaltyFee);
		//change values for VehSalesTaxAmt and VehTotalSalesTaxPd for REJCOR
		if (csTransCd.equals(TransCdConstant.REJCOR)
			&& caTtlUpdt != null)
		{
			Dollar laSalesTaxPdAmt = caTtlUpdt.getSalesTaxPdAmt();
			carrCompTransData[UPDT].setVehSalesTaxAmt(new Dollar(0.0));
			carrCompTransData[UPDT].setVehTotalSalesTaxPd(
				laSalesTaxPdAmt);
		}
		return carrCompTransData[UPDT];
	}
	/**
	 * Registration Fee Suite 
	 * 
	 */
	private void regFeeSuite()
	{
		//Initialize Returned Values 
		carrCompTransData[ciFCalcRow].setCustActualRegFee(
			new Dollar(0.0));
		carrCompTransData[ciFCalcRow].setAnnualDieselFee(
			new Dollar(0.0));
		carrCompTransData[ciFCalcRow].setAnnualRegFee(new Dollar(0.0));

		ciFCalcRow = UPDT;
		calcReg();

		// defect 9126
		// Calc Special Plate Fee

		// defect 10357
		ciCurrentRegExpMo = caMFVehOrig.getRegData().getRegExpMo();
		ciCurrentRegExpYr = caMFVehOrig.getRegData().getRegExpYr();
		// end defect 10357		
		calcSpclPlateFee();
		// end defect 9126

		// defect 9498
		// Non-Special Plate Cotton Plate application fee
		calcCottonPltApplFee();
		// end defect 9498

		// defect 8900
		// Do not charge Reg fees if Charge Fee Indi = 0 (Exempt)
		// defect 9126
		// Do not charge Reg fees if FeeCalcCat = 4 (No Regis Fees)
		if (carrCompTransData[UPDT].getNoChrgIndi() == 0
			&& caCommFeesData != null
			&& caCommFeesData.getFeeCalcCat() != 4)
		{
			calcRegSurChrg();
			calcRegAddl();
			calcReflectn();
			calcRdBrdg();
			calcChildSafty();
			// defect 9728
			calcMobility();
			// end defect 9728
			calcAutomation();
			calcToknTrlr();
		}
		// end defect 9126
		// end defect 8900
		calcMailIn();

		ciFCalcRow = ORIG;
		calcReg();
		calcRegSurChrg();

		// defect 9052
		// Code below was removed in previous release by mistake.
		// This setting of ciFCalcRow = UPDT is important. Later on in 
		// calcFees(), we will call setDfltMoYrFees() which calls
		// calcCommnFees(). If ciFCalcRow = ORIG, any vehicle with no 
		// 'original' registration (ie: DocTypeCd 13, 15, etc) will
		// cause a null pointer exception.
		//Look up Common Fees using FCalc{0} info 
		ciFCalcRow = UPDT;
		calcCommnFees();
		// end defect 9052			

	}
	/**
	 * Retrieve CntyCalYr data. 
	 * 
	 */
	private void retrieveCntyCalYr()
	{
		//fetch CntyCalndrYr if current CntyCalndrYr result is not valid?
		try
		{
			if (caCntyCalndrYr == null
				|| (!(caCntyCalndrYr.getOfcIssuanceNo()
					== caWSOfficeIds.getOfcIssuanceNo()
					&& caCntyCalndrYr.getFscalYr()
						== carrCompTransData[ciFCalcRow].getFsclYr())))
			{
				caCntyCalndrYr =
					CountyCalendarYearCache.getCntyCalndrYr(
						caWSOfficeIds.getOfcIssuanceNo(),
						carrCompTransData[ciFCalcRow].getFsclYr());
			}
		}
		catch (Exception aeEx)
		{
		}
	}
	/**
	 * Select from Account Code table 
	 * 
	 */
	private void selectFrmAcctCdsTbl()
	{
		caAcctCdData =
			AccountCodesCache.getAcctCd(
				csAcctItmCd,
				ciTransGregorianDate);

		// defect 9425 
		// Use current date if not found 
		if (caAcctCdData == null)
		{
			caAcctCdData =
				AccountCodesCache.getAcctCd(
					csAcctItmCd,
					new RTSDate().getYYYYMMDDDate());
		}
		// end defect 9425 
	}
	/**
	 * Set credit remaining 
	 * 
	 */
	private void setCrdtRmng()
	{
		//Apply Credit Remaining 
		int liCreditRow = 0;
		int liCreditAvailIndi = 0;
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		Vector lvFeesData = laRegFeesData.getVectFees();
		Dollar laMaxCredit = new Dollar(0.0);
		for (int i = 0; i < lvFeesData.size(); i++)
		{
			FeesData laFeesData = (FeesData) lvFeesData.elementAt(i);
			String lsAcctItmCd = laFeesData.getAcctItmCd();
			if (lsAcctItmCd.equals("CREDIT"))
			{
				liCreditRow = i;
				liCreditAvailIndi = 1;
			}
			else if (laFeesData.getCrdtAllowedIndi() == 1)
			{
				laMaxCredit =
					laMaxCredit.addNoRound(laFeesData.getItemPrice());
			}
		}
		if (liCreditAvailIndi == 1)
		{
			FeesData laFeesData =
				(FeesData) lvFeesData.elementAt(liCreditRow);
			Dollar laNegCreditFees = laFeesData.getItemPrice();
			Dollar laCreditFees =
				laNegCreditFees.multiplyNoRound(new Dollar(-1.0));
			if ((laMaxCredit.compareTo(laCreditFees)) >= 0)
			{
				carrCompTransData[UPDT].setCrdtRemaining(
					new Dollar(0.0));
			}
			else
			{
				Dollar laCrdtRemaining =
					(
						laNegCreditFees.addNoRound(
							laMaxCredit)).multiplyNoRound(
						new Dollar(-1.0));
				carrCompTransData[UPDT].setCrdtRemaining(
					laCrdtRemaining);
				laFeesData.setItemPrice(
					laMaxCredit.multiplyNoRound(new Dollar(-1.0)));
				if (caRegOrig.getRegClassCd()
					!= caRegUpdt.getRegClassCd())
				{
					carrCompTransData[UPDT].setCrdtRemaining(
						new Dollar(0.0));
				}
			}
			// defect 8900
			// Remove the 'Regis Credit Remaining' row if it is zero.
			// ie: For Exempts, Max Credit will be 0 because reg fees
			// for Exempt with Charge Fee Indi = 0 will be 0.
			if (laMaxCredit.compareTo(CommonConstant.ZERO_DOLLAR) <= 0)
			{
				lvFeesData.removeElementAt(liCreditRow);
			}
			// end defect 8900
		}
//		caCrdtRemaining = carrCompTransData[UPDT].getCrdtRemaining();
	}
	/**
	 * Set Default Month and Year and calculate the Default Fees 
	 * 
	 */
	private void setDfltMoYrFees()
	{
		// defect 8404, 8477
		// DTA does not go to REG029, so skip this method.
		// Check for RegPeriodLngth = 1 (ie: Seasonal Ag)
		// Default will be ToMonthMax\ToYearMax

		if (csTransCd.equals(TransCdConstant.DTANTD)
			|| csTransCd.equals(TransCdConstant.DTANTK)
			|| csTransCd.equals(TransCdConstant.DTAORD)
			|| csTransCd.equals(TransCdConstant.DTAORK))
		{
			return;
		}
		//Look up Common Fees using 'updated' info 
		ciFCalcRow = UPDT;
		if (caCommFeesData == null)
		{
			calcCommnFees();
		}
		if (ciNoMfVeh != 0 && ciExpMinMonths != ciExpMaxMonths)
		{
			if (caCommFeesData.getRegPeriodLngth() == 1)
			{
				if (ciExpMinMonths != 0 && ciExpMaxMonths != 0)
				{
					setDfltToMax();
				}
			}
			// end defect 8404, 8477

			// If new Vehicle && min exp months = max
			//else if (ciNoMfVeh != 0 && ciExpMinMonths != ciExpMaxMonths)

			//TEST Renewal cases that might change screen default
			// defect 9126
			// Do not check InvProcsngCd. Use new method isSpclPlate().
			// defect 10772
			// add WRENEW
			else if ((csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.IRENEW)
				|| csTransCd.equals(TransCdConstant.WRENEW))
				//&& caItemCdsData.getInvProcsngCd() == 2
					&& PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd())
					&& caRegOrig.getRegPltCd().equals(
						caRegUpdt.getRegPltCd())
					&& testIfWithinWnd())
			{
				// end defect 10772
				// end defect 9126
				//set up screen defaulted to current regis plus 1 reg period len 
				setDfltToCurrRegPlusOneRpl();
			}
			//TEST Title cases that might change screen default
			//Test trans code = title, non-titled, or reject correction
			// defect 8404
			// the 'not' sign was dropped in check for isRegExpired() 
			else if (
				(csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.NONTTL)
					|| csTransCd.equals(
						TransCdConstant
							.REJCOR)) // TEST if registration is current, within window
			//&& isRegExpired()
					&& !isRegExpired()
					&& testIfWithinWnd())
				// end defect 8404				
			{
				//Look up Common Fees using 'updated' info 
				ciFCalcRow = UPDT;
				calcCommnFees();
				// If NOT Fixed Plates && NOT REJCOR 
				// If PlateCode must change 
				//  Correct Title Rejection changed registration
				// defect 9126
				// Do not check InvProcsngCd. Use new method isSpclPlate(). 
				if ((!(caCommFeesData.getFxdExpMo() > 0
					|| caCommFeesData.getFxdExpYr() > 0))
					&& (!(csTransCd.equals(TransCdConstant.REJCOR))
						|| ciMustChangePltIndi == 1
						|| !(caRegOrig
							.getRegPltCd()
							.equals(caRegUpdt.getRegPltCd()))
						|| !(caRegOrig
							.getRegStkrCd()
							.equals(caRegUpdt.getRegStkrCd()))
						|| !(caVehOrig
							.getVehClassCd()
							.equals(caVehUpdt.getVehClassCd()))
						|| caRegOrig.getRegClassCd()
							!= caRegUpdt.getRegClassCd()
						|| !(testIfVehWgtNotChnged())
						|| caVehOrig.getVehModlYr()
							!= caVehOrig.getVehModlYr())
					//&& caItemCdsData.getInvProcsngCd() == 1
					&& !PlateTypeCache.isSpclPlate(
						caRegUpdt.getRegPltCd())
					&& ciUnregisterVehIndi == 0)
				{
					// end defect 9126
					//set up screen defaulted to current regis plus
					//1 reg period len
					// defect 8900
					// If coming from Exempt, keep default = Minimum
					if (caRegOrig.getExmptIndi() != 1)
					{
						setDfltToCurrRegPlusOneRpl();
					}
					// end defect 8900
				}
			}
		}
	}
	/**
	 * Set up screen defaulted to current regis plus 1 reg period len 
	 * 
	 */
	private void setDfltToCurrRegPlusOneRpl()
	{
		//ASSIGN ToMonth/Year = current expiration plus reg period length 
		//ToMonth:=Mod((MfVeh{0}.RegExpMo+CommonFees{0}.RegPeriodLngth-1),12)+1
		ciToMonth =
			(caRegOrig.getRegExpMo()
				+ caCommFeesData.getRegPeriodLngth()
				- 1)
				% 12
				+ 1;
		//ASSIGN TempFromYear to not more than one year ago
		ciTempFromYear =
			(caRegOrig.getRegExpMo() >= ciCurrMo)
				? (ciCurrYr - 1)
				: ciCurrYr;
		ciTempFromYear =
			Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
		//ToYear:=TempFromYear+Int(CommonFees{0}.RegPeriodLngth/12)
		ciToYear =
			ciTempFromYear
				+ (new Double(caCommFeesData.getRegPeriodLngth() / 12))
					.intValue();

		// defect 9126
		// Special Plates can possibly have a 'minimum ToMonth|Year'
		// farther out in the future. Set default to the maximum of this
		// already calculated minimum date, or the just calculated
		// default date			
		if (ciToYearMin >= ciToYear)
		{
			ciToYear = ciToYearMin;
			ciToMonth = ciToMonthMin;
		}
		// end defect 9126

		//Clear the FeesData object
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		laRegFeesData.setVectFees(new Vector());

		// Call correct fee calc procedures for TransCd 
		callFeeCalcMthdBsdOnTransCd();
		setCrdtRmng();
	}

	/**
		* Set the default Month and Year on REG029 screen
		* to the maximum To Month and To Year
		* 
		* @return bolean
		*/
	private void setDfltToMax()
	{
		// defect 8404  new method
		ciToMonth = ciToMonthMax;
		ciToYear = ciToYearMax;
		// This sets the default 'months to charge' message  
		// on REG029 and recalculates the fees
		// (see: setDfltToCurrRegPlusOneRpl()) 
		RegFeesData laRegFeesData =
			carrCompTransData[UPDT].getRegFeesData();
		laRegFeesData.setVectFees(new Vector());
		callFeeCalcMthdBsdOnTransCd();
		setCrdtRmng();
	}
	// end defect 8404

	/**
	 * Assign DisableCntyFees for Title Events.
	 * 
	 * @return bolean
	 */
	private boolean setDisCntyForTtl()
	{
		// defect 9126
		// Do not check InvProcsngCd. Use new method isSpclPlate(). 
		//if (caItemCdsData.getInvProcsngCd() == 2)
		// defect 9106
		// We DO want county fees even with Special Plate
		//if (PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd()))
		//{
		//	// end defect 9126
		//	carrCompTransData[UPDT].setDisableCtyFees(1);
		//	return true;
		//}
		// end defect 9106
		ciFCalcRow = UPDT;
		calcCommnFees();
		//TEST if Fixed Plates 
		if ((caCommFeesData.getFxdExpMo() > 0
			|| caCommFeesData.getFxdExpYr() > 0)
			&& carrCompTransData[UPDT].getNoMoChrg() < 12)
		{
			carrCompTransData[UPDT].setDisableCtyFees(1);
			return true;
		}
		return false;
	}
	/**
	 * Set FromMo/Yr to day after current regis expiration 
	 * 
	 */
	private void setFrmMoYrAftrCurrRegExp()
	{
		//Adjustment for Dec month is handled here.
		ciFromMo = (caRegOrig.getRegExpMo() % 12) + 1;
		//ASSIGN TempFromYear to not more than one year ago 
		ciTempFromYear =
			(caRegOrig.getRegExpMo() >= ciCurrMo)
				? (ciCurrYr - 1)
				: ciCurrYr;
		ciTempFromYear =
			Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
		//Adjustment for Dec month is handled here.
		if (caCommFeesData.getRegPeriodLngth() != 0)
		{
			// defect 8404
			// This is strictly a date calculation. 
			// dividing by 12 months, not Reg Period Length.
			ciFromYr = (new Double(caRegOrig.getRegExpMo()
				/// caCommFeesData.getRegPeriodLngth()))
	/ 12)).intValue();
			// end defect 8404                    
		}
		ciFromYr = ciTempFromYear + ciFromYr;
	}
	/**
	 * Set FromMo/FromYr, and possible creditable months 
	 * 
	 */
	private void setFromMoFromYr()
	{
		calcCommnFees();
		//ASSIGN Set FromMo/Yr to today
		ciFromMo = ciCurrMo;
		ciFromYr = ciCurrYr;
		//no current regis credit

		carrCompTransData[ORIG].setNoMoChrg(0);

		// defect 9126
		// If Special Plate, determine Special Plate FromMo/Yr
		if (PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd()))
		{
			carrCompTransData[ORIG].setSpclPlateNoMoCharge(0);
			setSpclPltFromMoFromYr();
		}
		// end defect 9126

		//TEST if originally marked 'registration invalid'
		//MfVeh{0}.RegInvldIndi=1|MfVeh{Row}.RegInvldIndi=1
		if (!(caRegOrig.getRegInvldIndi() == 1
			|| caRegUpdt.getRegInvldIndi() == 1))
		{
			//PROC if CORREGX reset FromMo/Yr to date apprehended 
			// weight corrected
			if (csTransCd.equals(TransCdConstant.CORREGX))
			{
				ciFromMo = ciCorrtnEffMo;
				ciFromYr = ciCorrtnEffYr;
				int liNoMoCharge =
					12 * (caRegOrig.getRegExpYr() - ciFromYr)
						+ (caRegOrig.getRegExpMo() - ciFromMo)
						+ 1;
				carrCompTransData[ORIG].setNoMoChrg(liNoMoCharge);
			}
			else if (testNoCurrReg())
			{
				// empty code block
			}
			// if expired and invalid reason reset FromMo/Yr to 
			// current exp+1
			// Test if registration is expired
			// Test NOT valid reason for late regis
			else if (isRegExpired() && ciRegExpReason != 1)
			{
				//ASSIGN Set FromMo/Yr to day after current regis expiration   
				setFrmMoYrAftrCurrRegExp();
			}
			//if Renewal in window reset FromMo/Yr to current exp+1
			// TEST if within window
			// defect 10772
			// add WRENEW 
			else if ((csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.IRENEW)
				|| csTransCd.equals(TransCdConstant.WRENEW))
					&& testIfWithinWnd())
			{
				// end defect 10772
				setFrmMoYrAftrCurrRegExp();
			}
			//if Title is current and regis has not changed reset FromMo/Yr to current exp+1 
			//Test trans code = title, non-titled, reject correction, or DTA
			//no credit is given if moving to Antique regclasses in Title 
			else if (
				(csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.NONTTL)
					|| csTransCd.equals(TransCdConstant.REJCOR)
					|| csTransCd.equals(TransCdConstant.DTANTD)
					|| csTransCd.equals(TransCdConstant.DTANTK)
					|| csTransCd.equals(TransCdConstant.DTAORD)
					|| csTransCd.equals(TransCdConstant.DTAORK))
					&& (caRegUpdt.getRegClassCd() == 4
						|| caRegUpdt.getRegClassCd() == 5))
			{
				// empty code block
			}
			else if (
				(csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.NONTTL)
					|| csTransCd.equals(TransCdConstant.REJCOR)
					|| csTransCd.equals(TransCdConstant.DTANTD)
					|| csTransCd.equals(TransCdConstant.DTANTK)
					|| csTransCd.equals(TransCdConstant.DTAORD)
					|| csTransCd.equals(TransCdConstant.DTAORK))
					&& !(isRegExpired())
					&& ciMustChangePltIndi == 0
					&& caRegOrig.getRegPltCd().equals(
						caRegUpdt.getRegPltCd())
					&& caRegOrig.getRegStkrCd().equals(
						caRegUpdt.getRegStkrCd())
					&& caVehOrig.getVehClassCd().equals(
						caVehUpdt.getVehClassCd())
					&& caRegOrig.getRegClassCd()
						== caRegUpdt.getRegClassCd()
					&& caVehOrig.getVehModlYr()
						== caVehUpdt.getVehModlYr()
					&& testIfVehWgtNotChnged())
			{
				// defect 8900
				// If was Exempt, keep FromMoYr set as current Mo\Yr
				if (caRegOrig.getExmptIndi() != 1)
				{
					setFrmMoYrAftrCurrRegExp();
				}
				// end defect 8900
			}
			else
			{
				//carrCompTransData[ORIG].NoMoCharge = remaining months 
				//of current regis 
				int liNoMoCharge =
					12 * (caRegOrig.getRegExpYr() - ciFromYr)
						+ (caRegOrig.getRegExpMo() - ciFromMo)
						+ 1;
				carrCompTransData[ORIG].setNoMoChrg(
					Math.max(0, liNoMoCharge));
			}
		}
		else
		{
//			ciExpFromMonths = ciFromYr * 12 + ciFromMo;
		}
	}
	
	/**
	 * Set the maximum allowable RegExpMo/Yr
	 * From Mo/Yr is either current data or current expiration 
	 * @param aiFromMo  int
	 * @param aiFromYr  int
	 */
	private void setMaxAllowOrMaxMultiYr(int aiFromMo, int aiFromYr)
	{
		int liRegPeriod = Math.max(
			caCommFeesData.getMaxMYrPeriodLngth(),
			caCommFeesData.getMaxallowbleRegMo());
		ciToMonth =
			(aiFromMo
				+ liRegPeriod
				- 2)
				% 12
				+ 1;
		ciToYear =
			(new Double((aiFromMo
				+ liRegPeriod
				- 2)
				/ 12.0))
				.intValue();
		ciToYear = aiFromYr + ciToYear;
	}
	
	/**
	 * Set mainframe base/actual fee 
	 * 
	 */
	private void setMFBaseActlFee()
	{
		//Set mainframe base reg fee to new annual registration fee 
		if (carrCompTransData[UPDT].getAnnualRegFee() != null)
			caRegUpdt.setCustBaseRegFee(
				carrCompTransData[UPDT].getAnnualRegFee().round());

		//Set mainframe base diesel fee to new annual diesel fee
		//defect 10685
		//Set to actual diesel fee for number of months charged,
		//not annual diesel fee (see: adjAddlDislFee())
		//if (carrCompTransData[UPDT].getAnnualDieselFee() != null)
		if (carrCompTransData[UPDT].getDieselFee() != null)
			caRegUpdt.setCustDieselFee(
		//		carrCompTransData[UPDT].getAnnualDieselFee().round());
				carrCompTransData[UPDT].getDieselFee().round());
		//end defect 10685

		// defect 10772
		// add WRENEW 
		if (csTransCd.equals(TransCdConstant.RENEW)
			|| csTransCd.equals(TransCdConstant.IRENEW)
			|| csTransCd.equals(TransCdConstant.WRENEW))
		{
			// end defect 10772
			caRegUpdt.setCustActlRegFee(
				carrCompTransData[UPDT].getCustActualRegFee());
		}
		//Test trans code = title, non-titled, reject correction, or DTA
		else if (
			csTransCd.equals(TransCdConstant.TITLE)
				|| csTransCd.equals(TransCdConstant.NONTTL)
				|| csTransCd.equals(TransCdConstant.REJCOR)
				|| csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
		{
			//TEST if Number of Months (Net) is > 0 
			if ((carrCompTransData[UPDT].getNoMoChrg()
				- carrCompTransData[ORIG].getNoMoChrg())
				> 0)
			{
				if (carrCompTransData[UPDT].getDisableCtyFees() == 0)
					caRegUpdt.setCustActlRegFee(
						carrCompTransData[UPDT].getCustActualRegFee());
			}
		}
	}
	/**
	 * Set minimum ToMonth/ToYear 
	 * 
	 */
	private void setMinToMoToYr()
	{
		//PROC Calc minimum ToMonth, ToYear
		// defect 5284
		//Set the default month and year to Curr Yr+1 for currently registered vehicles
		//which have following plates as listed below.

		// defect 10772
		// add WRENEW
		// defect 11303
		// This group is Annual Plates w/ FxdExpMo 3 that do not
		// allow Max 36 months. Minimum should be 'next available
		// '03', even if it's this month.
		// Also, should be looking at 'Updt', not 'Orig' (may have
		// Exchanged into this group).
		//if ((csTransCd.equals(TransCdConstant.RENEW)
		//	|| csTransCd.equals(TransCdConstant.IRENEW)
		//	|| csTransCd.equals(TransCdConstant.WRENEW))
		//	&& caRegOrig != null
		//	&& caRegUpdt != null
		//	&& caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd()
		//	&& (caRegOrig.getRegClassCd() == 10 //Combination plate
		//		|| caRegOrig.getRegClassCd() == 14 //Fertilizer plate
		//		|| caRegOrig.getRegClassCd() == 31 //Soil conservation 
		//		|| caRegOrig.getRegClassCd() == 32)//Soil conserv trlr
		//		// defect 10697 
		//		//|| caRegOrig.getRegClassCd() == 61) //Cotton plate
		//		// end defect 10697
		//	&& !(isRegExpired()))
		//{
		//	// end defect 10772
		//	ciToMonth = caCommFeesData.getFxdExpMo();
		//	ciToYear = ciCurrYr + 1;
		if (caRegUpdt.getRegClassCd() == 10
			|| caRegUpdt.getRegClassCd() == 11
			|| caRegUpdt.getRegClassCd() == 14
			|| caRegUpdt.getRegClassCd() == 31
			|| caRegUpdt.getRegClassCd() == 32
			|| caRegUpdt.getRegClassCd() == 77)
		{
			// end defect 11303
			// If we've already passed the month, or this is a
			// Renew of 'special group', use next yr
			ciToMonth = caCommFeesData.getFxdExpMo();
			ciToYear = ciCurrYr;
			if (ciCurrMo > ciToMonth
				|| csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.IRENEW)
				|| csTransCd.equals(TransCdConstant.WRENEW))
			{
				ciToYear = ciCurrYr + 1;
			}
		}

		// defect 8900
		// Standard Exempt (FeeCalcCat = 4) has no expiration
		// defect 9126 
		// Should be checking for Standard Exempt, not FeeCalcCat = 4
		//else if (
		//	caCommFeesData.getFeeCalcCat()
		//		== CommonConstant.NOREGISFEES)
		// If Standard Exempt, there is no expiration date
		else if (
			CommonFeesCache.isStandardExempt(
				caRegUpdt.getRegClassCd()))
		{
			ciToMonth = 0;
			ciToYear = 0;
		}
		// end defect 8900

		//Set minimum ToMonth/Yr for non-fixed month/year
		// defect 9085
		// For LP, expiration must be the same as that on the plate
		// defect 9226
		// Add check for 'is Special Plate' because, unfortunately:
		// an 'FO - Foreign Organization plate' IS an LP,
		// but is NOT a special plate (?!), so we CANNOT stay the same
		// exp that is on the plate. Do not got thru Adj for Spcl Plt.
		// defect 11249
		// Wasn't going thru 'setToMo...()' when FxdExpMo != 0 (MYSOP),
		// falling into adjMinForSpclPlt() w/out setting ciToMonth/Year.
//		else if (
//			(caCommFeesData.getFxdExpMo() == 0
//				&& setToMoToYrForNonFxdMoYr())
//				|| (isLP()
//					&& PlateTypeCache.isSpclPlate(
//						caRegUpdt.getRegPltCd())))
//		{
//			// defect 9126
//			// Adjust minimum ToMonth\Yr if Special Plate
//			adjMinForSpclPlt();
//			// end defect 9126
//		}
		else if (caCommFeesData.getFxdExpMo() == 0)
		{
			setToMoToYrForNonFxdMoYr();
			if (PlateTypeCache.isSpclPlate(
				caRegUpdt.getRegPltCd()))
			{
				adjMinForSpclPlt();
			}
		}
		// end defect 11249
		// end defect 9226
		// end defect 9085 
		else
		{
			//set minimum ToMonth/Yr for fixed month and/or year 
			ciToMonth = caCommFeesData.getFxdExpMo();
			if (caCommFeesData.getFxdExpYr() > 0)
			{
				ciToYear = caCommFeesData.getFxdExpYr();
			}
			else
			{
				// defect 9553
				// Set MinToYr for Tow Truck
				if (csTransCd.equals(TransCdConstant.TOWP))
				{
					ciToYear = ciCurrYr + 1;
					if (ciCurrMo == 12)
					{
						// if Tow Truck is currently registered
						if (carrCompTransData[UPDT]
							.getTimedPermitData()
							.isRegistered())
						{
							ciToYear = ciCurrYr + 2;
						}
					}
					else if (ciCurrMo == 1)
					{
						// if Tow Truck is not currently registered
						if (!carrCompTransData[UPDT]
							.getTimedPermitData()
							.isRegistered())
						{
							ciToYear = ciCurrYr;
						}
					}
				}
				// end defect 9553
				else
				{
					// defect 9376
					// Fix setting Minimum to Mo\Yr. For Renew, if
					// vehicle exp 03/2008 and current date = 03/2008,
					// it was prompting for 2008 sticker.
					//int liYrAdjust = (ciCurrMo <= ciToMonth) ? 0 : 1;

					// defect 9784
					// Fix edit for exchanging to fixed exp month.
					// For Title or Exch and current month < fixed month,
					//   year will be this year
					// For renew and current month < fixed month, 
					//   year will be this year only if reg is expired

					//if (isRegExpired())
					//{
					//	if (ciCurrMo <= caCommFeesData.getFxdExpMo())
					//	{
					//		liYrAdjust = 0;
					//	}
					//	else
					//	{
					//		liYrAdjust = 1;
					//	}
					//}
					//else
					//{
					//	if (csTransCd.equals(TransCdConstant.RENEW)
					//		|| csTransCd.equals(TransCdConstant.IRENEW))
					//	{
					//		liYrAdjust = 1;
					//	}
					//	else
					//	{
					//		liYrAdjust = 0;
					//	}
					//}
					//int liMaxYrsInFuture =
					//	(new Double(((caCommFeesData
					//		.getRegPeriodLngth()
					//		- 1)
					//	// / caCommFeesData.getRegPeriodLngth())
					//					/ 12) + liYrAdjust)).intValue();
					//	// end defect 6801 
					// For example, in Exch, you could be in either 2007 or  
					//// 2008 with current expiration expiring in 03/2008.
					//ciToYear =
					//	Math.max(
					//		caRegOrig.getRegExpYr(),
					//		(ciCurrYr + liMaxYrsInFuture));
					//// end defect 9376
					int liYrAdjust = 1;
					if (ciCurrMo <= caCommFeesData.getFxdExpMo())
					{
						// defect 10772
						// add WRENEW 
						if (!(csTransCd.equals(TransCdConstant.RENEW)
							|| csTransCd.equals(TransCdConstant.IRENEW)
							|| csTransCd.equals(TransCdConstant.WRENEW)))
						{
							liYrAdjust = 0;
						}
						else if (isRegExpired())
						{
							liYrAdjust = 0;
						}
					}
 
					ciToYear =
						Math.max(
							(isLP()
								? caSpclPltRegUpd.getPltExpYr()
								: caRegOrig.getRegExpYr()),
							(ciCurrYr + liYrAdjust));
					// defect 11249
					// Note: This may break the 'must be >= 12 months for renewal'.
					// For these fixed exp month cases (ie: MYSOP fixed 12) the minimum
					// calculated may be beyond the 'max allowable reg months'.
					//(ie: on 01/2012, renew veh expires 02/2012, exch to MYSOP.
					//(this will give min of 12/2012 (only 10 months).
					// defect 11298
					// Should use RegPeriodLngth so Minimum is not too far in future
					// Also should be using 'new Reg Eff Dt' vs always using 'Today'
					//int liRegPeriod = caCommFeesData.getMaxallowbleRegMo();
					int liRegPeriod = caCommFeesData.getRegPeriodLngth();
					int liRegEffDate =
						Math.max(ciEffectiveExpDatePlusOne, ciTransGregorianDate);
					int liMonth =
						(new RTSDate(RTSDate.YYYYMMDD, liRegEffDate))
							.getMonth();
					int liYear =
						(new RTSDate(RTSDate.YYYYMMDD, liRegEffDate))
							.getYear();
					// end defect 11298
					// Get number of years in allowable reg period
					int liYears = liRegPeriod / 12;
					//int ciTempYear = ciCurrYr + liYears;
					int ciTempYear = liYear + liYears;
					// Get number of months in 'max allowable month\year'
					int liMaxAllowMonths = ciToMonth + (ciTempYear * 12);
					// Get number of months in 'current month\year'
					//int liCurrMonths = ciCurrMo + (ciCurrYr * 12);
					int liCurrMonths = liMonth + (liYear * 12);
					// Check if it's too far in future
					if (liMaxAllowMonths - liCurrMonths >= 
						caCommFeesData.getMaxallowbleRegMo())
					{
						// NOTE : less than 12 month renew!
						ciToYear = ciTempYear - 1;
					}
					// end defect 11249
					
					// end defect 9784

					// defect 9434
					// Adjust minimum ToMonth\Yr if Special Plate
					// ie: We may need to bump the year up to next year if
					// Special Plate is already thru next year.
					adjMinForSpclPlt();
					// end defect 9434
				}
			}
		}
		//ASSIGN ToMonthMin, ToYearMin, ExpMoYrMin$, ExpMinMonths
		ciToYearMin = ciToYear;
		ciToMonthMin = ciToMonth;
		if (ciToMonth < 10)
		{
			//For Display on screen
			csExpMoYrMin = "0" + ciToMonth + "/" + ciToYear;
		}
		else
		{
			csExpMoYrMin = ciToMonth + "/" + ciToYear;
		}
		ciExpMinMonths = ciToYear * 12 + ciToMonth;
	}
	/**
	 * ASSIGN Set 'number of months of reg & credit' msg 
	 * 
	 */
	private void setNoOfMoRegCrdtMsg()
	{
		if (carrCompTransData[UPDT].getNoMoChrg() == 0)
		{
			csTxtRegMoSold = CommonConstant.STR_SPACE_EMPTY;
		}
		else
		{
			int liNoMoCharge = carrCompTransData[UPDT].getNoMoChrg();
			csTxtRegMoSold =
				"This reflects "
					+ liNoMoCharge
					+ " months of registration.";
			// defect 8900
			// No credit is given when moving from Exempt
			if (caRegOrig.getExmptIndi() != 1)
			{
				if (carrCompTransData[ORIG].getNoMoChrg() != 0)
				{
					liNoMoCharge =
						carrCompTransData[ORIG].getNoMoChrg();
					csTxtRegMoSold =
						csTxtRegMoSold
							+ " Credit is given for "
							+ liNoMoCharge;
					if (liNoMoCharge == 1)
					{
						csTxtRegMoSold =
							csTxtRegMoSold + " month of registration.";
					}
					else
					{
						csTxtRegMoSold =
							csTxtRegMoSold + " months of registration.";
					}
				}
			}
			// end defect 8900
		}

		// defect 9126
		// Set number of months of Special Plate Fee sold
		csTxtSpclPltMoSold = CommonConstant.STR_SPACE_EMPTY;
		if (ciSpclPltNoMoCharge > 0)
		{
			csTxtSpclPltMoSold = "This reflects " + ciSpclPltNoMoCharge;
			if (ciSpclPltNoMoCharge == 1)
			{
				csTxtSpclPltMoSold =
					csTxtSpclPltMoSold + " month of Plate Fee.";
			}
			else
			{
				csTxtSpclPltMoSold =
					csTxtSpclPltMoSold + " months of Plate Fee.";
			}
		}
		// end defect 9126
	}
	/**
	 * Set minimum ToMonth/Yr for non-fixed month/year                                
	 */
	private boolean setToMoToYrForNonFxdMoYr()
	{
		// defect 9337
		// If 'TONLY' (Title Only - No Regis) there is no regis exp date
		if (caRegUpdt.getRegClassCd()
			== CommonConstant.TONLY_REGCLASSCD)
		{
			//Clear ToMonth/Year
			ciToMonth = 0;
			ciToYear = 0;
			caRegUpdt.setRegExpMo(0);
			caRegUpdt.setRegExpYr(0);
			return true;
		}
		// end defect 9337

		//Special Handling for REJCOR - Minimum 
		else if (csTransCd.equals(TransCdConstant.REJCOR))
		{
			//Set ToMonth/Year depending on current regis                                          
			if (caRegOrig.getRegExpMo() == 0)
			{
				//ASSIGN ToMonth/Year = Now plus reg period length - 1  
				asgnToMoYrNowPlusRplMinusOne();
			}
			else
			{
				//ASSIGN ToMonth/Year = current expiration
				ciToMonth = caRegOrig.getRegExpMo();
				ciToYear = caRegOrig.getRegExpYr();
			}
			return true;
		}
		// defect 8900
		// If coming from Exempt, must set Min ToMo\Yr differently for
		// Exchange or Title
		//else if (testNoCurrReg())
		else if (testNoCurrReg() && caRegOrig.getExmptIndi() != 1)
		{
			asgnToMoYrNowPlusRplMinusOne();
			return true;
		}
		//Set minimum based on current registration 
		//minimum ToMonth/Year for TransCd$ = EXCH                                                                        
		else if (csTransCd.equals(TransCdConstant.EXCH))
		{
			//Test for special plate
			//Test if Special Plates HQ section
			// defect 9126
			// Do not check InvProcsngCd (do not check for Special Plate).  
			//if (caItemCdsData.getInvProcsngCd() == 2
			//if (PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd())
			//	|| carrCompTransData[UPDT].getOfcIssuanceNo()
			//		== SPECIAL_PLATES)
			//{
			//	// end defect 9126
			//	ciToMonth = ciCurrMo;
			//	ciToYear = ciCurrYr;
			//}
			// defect 8900
			// If Standard Exempt Exchanging to non-InvProcsngCd 2, set
			// minimum ToMo\Yr = Now + Reg Period Length-1
			//else if (
			// end defect 9126
			if (CommonFeesCache
				.isStandardExempt(caRegOrig.getRegClassCd()))
			{
				asgnToMoYrNowPlusRplMinusOne();
			}
			// end defect 8900
			// defect 10697
			// For Seasonal Ag, they should have a choice of 1-6 mo
			else if (CommonFeesCache.getCommonFee(
					caRegUpdt.getRegClassCd(),
					new RTSDate().getYYYYMMDDDate()).
					getRegPeriodLngth() == 1)
			{
				//ASSIGN ToMonth/Year = current date                                              	
				ciToMonth = ciCurrMo;
				ciToYear = ciCurrYr;
			}
			// end defect 10697
			else
			{
				//Assign to Current Expiration                                                            
				// defect 8928
				//Assign to Minumum of CurrExp or Max Allowable 
				// line left out when copy from VAJ to WSAD
				// ie: Rel 5.2.2 to Rel 5.2.3
				// should set to min of Curr Exp or Max Allowable				                                                           
				//ciToMonth = caReg0.getRegExpMo();
				//ciToYear = caReg0.getRegExpYr();
				// defect 10899
				// For Exchange, we should keep the current expiration
				//setToMoToYrToMinOfCurrExpDtOrMaxAllow();
				ciToMonth = caRegOrig.getRegExpMo();
				ciToYear = caRegOrig.getRegExpYr();
				// end defect 10899
				// end defect 8928
			}
			return true;
		}
		//minimum ToMonth/Year for TransCd$ = RENEW 
		// defect 10772
		// add WRENEW 
		else if (csTransCd.equals(TransCdConstant.RENEW)
			|| csTransCd.equals(TransCdConstant.IRENEW)
			|| csTransCd.equals(TransCdConstant.WRENEW))
		{
			// end defect 10772
			//Test for 'TONLY'  and assign ToMonth and ToYear for renew

			// defect 9337
			// Do not need check for 'TONLY'. Fall thru naturally. 
			//if ((caRegOrig.getRegPltNo()).equals("TONLY"))
			//{
			//	asgnToMoYrNowPlusRplMinusOne();
			//	return true;
			//}
			// end defect 9337

			// if registration current, within window
			if (!(isRegExpired()) && testIfWithinWnd())
			{
				// defect 8404
				//Check for RegPeriodLngth = 1 (ie: seasonal ag)
				//Set min ToMonth\Year to month after current RegExpMo
				if (caCommFeesData.getRegPeriodLngth() == 1)
				{
					//ASSIGN ToMonth/Year = current expiration plus one month                                              	
					ciToMonth =
						(caRegOrig.getRegExpMo() == 12)
							? 1
							: (caRegOrig.getRegExpMo() + 1);
					//ASSIGN TempFromYear to not more than one year ago
					ciTempFromYear =
						(caRegOrig.getRegExpMo() >= ciCurrMo)
							? (ciCurrYr - 1)
							: ciCurrYr;
					ciTempFromYear =
						Math.max(
							ciTempFromYear,
							caRegOrig.getRegExpYr());
					ciToYear =
						(caRegOrig.getRegExpMo() == 12)
							? (ciTempFromYear + 1)
							: ciTempFromYear;
					return true;
				}
				// end defect 8404
				// if current reg, within window && NOT Special Plate
				// defect 9126
				// Do not check InvProcsngCd. If current and in window
				// set to current exp plus reg period length
				else
				{
					// ASSIGN ToMonth/Year = current expiration plus 
					// reg period length                             	
					ciToMonth =
						((caRegOrig.getRegExpMo()
							+ caCommFeesData.getRegPeriodLngth()
							- 1)
							% 12)
							+ 1;
					//ASSIGN TempFromYear to not more than one year ago 
					ciTempFromYear =
						(caRegOrig.getRegExpMo() >= ciCurrMo)
							? (ciCurrYr - 1)
							: ciCurrYr;
					ciTempFromYear =
						Math.max(
							ciTempFromYear,
							caRegOrig.getRegExpYr());
					ciToYear =
						ciTempFromYear
							+ (new Double(caCommFeesData
								.getRegPeriodLngth()
								/ 12))
								.intValue();
					return true;
				}
				// if current reg, within window && Special Plate
				//else if (caItemCdsData.getInvProcsngCd() == 2)
				//{
				//	//ASSIGN ToMonth/Year = current expiration plus one month                                              	
				//	ciToMonth =
				//		(caRegOrig.getRegExpMo() == 12)
				//			? 1
				//			: (caRegOrig.getRegExpMo() + 1);
				//	//ASSIGN TempFromYear to not more than one year ago
				//	ciTempFromYear =
				//		(caRegOrig.getRegExpMo() >= ciCurrMo)
				//			? (ciCurrYr - 1)
				//			: ciCurrYr;
				//	ciTempFromYear =
				//		Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
				//	ciToYear =
				//		(caRegOrig.getRegExpMo() == 12)
				//			? (ciTempFromYear + 1)
				//			: ciTempFromYear;
				//	return true;
				//}
				// end defect 9126
			}
			// Registration is expired
			// defect 8404
			// 'Valid\Invalid' does not make sense for 
			// RegPeriodLngth = 1 (ie: seasonal ag)
			// need to set minimum to current month
			else if (caCommFeesData.getRegPeriodLngth() == 1)
			{
				ciToMonth = ciCurrMo;
				ciToYear = ciCurrYr;
				return true;
			}
			// end defect 8404				
			else if (isRegExpired() && ciRegExpReason == VALID_REASON)
			{
				//ASSIGN ToMonth/Year = Now plus reg period length - 1
				asgnToMoYrNowPlusRplMinusOne();
				return true;
			}
			// If registration is expired && NOT valid reason
			else if (isRegExpired() && ciRegExpReason != VALID_REASON)
			{
				// ASSIGN ToMonth/Year = current expiration plus reg 
				// period length                                    	
				ciToMonth =
					((caRegOrig.getRegExpMo()
						+ caCommFeesData.getRegPeriodLngth()
						- 1)
						% 12)
						+ 1;
				//ASSIGN TempFromYear to not more than one year ago 
				ciTempFromYear =
					(caRegOrig.getRegExpMo() >= ciCurrMo)
						? (ciCurrYr - 1)
						: ciCurrYr;
				ciTempFromYear =
					Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
				ciToYear =
					ciTempFromYear
						+ (new Double(caCommFeesData.getRegPeriodLngth()
							/ 12))
							.intValue();
				return true;
			}
			return false;
		}
		//minimum ToMonth/Year for TransCd$ = TITLE, NONTTL, REJCOR, DTA 
		//Test trans code = title, non-titled, reject correction, or DTA 
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR)
			|| csTransCd.equals(TransCdConstant.DTANTD)
			|| csTransCd.equals(TransCdConstant.DTANTK)
			|| csTransCd.equals(TransCdConstant.DTAORD)
			|| csTransCd.equals(TransCdConstant.DTAORK))
		{

//			caItemCdsData =
//				ItemCodesCache.getItmCd(caRegUpdt.getRegPltCd());

			// Set minimum ToMonth/Year for TITLE, NONTTL, REJCOR, DTA

			// TEST if registration is current

			// defect 9689
			// Do not adjust for Vendor Plate NOR SPECIAL PLATE!!
			// Re-do this WHOLE section: if not expired, set to current
			// exp and let the adjMinForSpclPlt() do the Special Plate
			// adjustment if needed

			// Test for special plate 
			// defect 9126
			// Do not check InvProcsngCd. Use new method isSpclPlate(). 
			// defect 10349
			// Move check for Exempt here from below check for expired.  
			if (caRegOrig.getExmptIndi() == 1)
			{
				asgnToMoYrNowPlusRplMinusOne();
				return true;
			}
			// end defect 10349
			//if (!(isRegExpired()))
			else if (!(isRegExpired()))
				//	&& caItemCdsData.getInvProcsngCd() == 2)
				//	&& PlateTypeCache.isSpclPlate(caRegUpdt.getRegPltCd()))
			{
				//ASSIGN ToMonth/Year = Max of Orig RegExp or PltExp
				// defect 10258
				// Seasonal Ag (regperiod = 1) should have min = 1 month
				//ciToMonth = caRegOrig.getRegExpMo();
				//ciToYear = caRegOrig.getRegExpYr();
				// If RegPeriodLength = 1 (ie: seasonal ag) 
				if (caCommFeesData.getRegPeriodLngth() == 1)
				{
					//ASSIGN ToMonth/Year = current month/year 
					ciToMonth = ciCurrMo;
					ciToYear = ciCurrYr;
				}
				else
				{
					ciToMonth = caRegOrig.getRegExpMo();
					ciToYear = caRegOrig.getRegExpYr();
				}
				// end defect 10258   

				//	SpecialPlatesRegisData laSpecialPlatesRegData =
				//		caMFVehUpdt.getSpclPltRegisData();
				//
				//	ciSpclPltExpMo = laSpecialPlatesRegData.getRegExpMo();
				//	ciSpclPltExpYr = laSpecialPlatesRegData.getRegExpYr();
				//
				//	if (ciSpclPltExpYr > ciToYear)
				//	{
				//		ciToMonth = ciSpclPltExpMo;
				//		ciToYear = ciSpclPltExpYr;
				//	}
				//	else if (
				//		ciSpclPltExpYr == ciToYear
				//			&& ciSpclPltExpMo > ciToMonth)
				//	{
				//		ciToMonth = ciSpclPltExpMo;
				//	}
				// end defect 9689

				return true;
			}

			// defect 8900
			// If Exempt, set minimum ToMo\Yr = Now + Reg Period Length-1
			//else if (caRegOrig.getExmptIndi() == 1)
			//{
			//	asgnToMoYrNowPlusRplMinusOne();
			//	return true;
			//}
			// end defect 8900

			// TEST if registration is current
			// Test for NOT special plate 
			// defect 9126
			// Do not check InvProcsngCd. Use new method isSpclPlate(). 
			else if (
				!(isRegExpired())
				//&& caItemCdsData.getInvProcsngCd() != 2)
					&& !PlateTypeCache.isSpclPlate(
						caRegUpdt.getRegPltCd()))
			{
				// end defect 9126
				// defect 8404
				// If RegPeriodLength = 1 (ie: seasonal ag), 
				if (caCommFeesData.getRegPeriodLngth() == 1)
				{
					if (!testIfWithinWnd()
						&& caRegOrig.getRegClassCd()
							!= caRegUpdt.getRegClassCd())
					{
						// Title not in window, with Exchange
						// set ToMonth\Year to the minimum of 
						// Current Expiration or Max Allowable Reg months
						setToMoToYrToMinOfCurrExpDtOrMaxAllow();
						return true;
					}
					else
					{
						//ASSIGN ToMonth/Year = current expiration 
						ciToMonth = caRegOrig.getRegExpMo();
						ciToYear = caRegOrig.getRegExpYr();
						return true;
					}
				}
				// end defect 8404
				else
				{
					//ASSIGN ToMonth/Year = current expiration 
					ciToMonth = caRegOrig.getRegExpMo();
					ciToYear = caRegOrig.getRegExpYr();
					return true;
				}
			}

			// If registration expired & valid reason 
			else if (isRegExpired() && ciRegExpReason == VALID_REASON)
			{
				asgnToMoYrNowPlusRplMinusOne();
				return true;
			}
			else if (isRegExpired() && ciRegExpReason != VALID_REASON)
			{
				//ASSIGN ToMonth/Year = current expiration plus reg period length 
				//ToMonth:=Mod((MfVeh{0}.RegExpMo+CommonFees{0}.RegPeriodLngth-1),12)+1
				ciToMonth =
					(caRegOrig.getRegExpMo()
						+ caCommFeesData.getRegPeriodLngth()
						- 1)
						% 12
						+ 1;
				//ASSIGN TempFromYear to not more than one year ago
				ciTempFromYear =
					(caRegOrig.getRegExpMo() >= ciCurrMo)
						? (ciCurrYr - 1)
						: ciCurrYr;
				ciTempFromYear =
					Math.max(ciTempFromYear, caRegOrig.getRegExpYr());
				//ToYear:=TempFromYear+Int(CommonFees{0}.RegPeriodLngth/12)
				ciToYear =
					ciTempFromYear
						+ (new Double(caCommFeesData.getRegPeriodLngth()
							/ 12))
							.intValue();
				return true;
			}
			return false;
		}
		//minimum ToMonth/Year = max of current exp or Now
		if (!(isRegExpired()))
		{
			//ASSIGN ToMonth/Year = current expiration 
			ciToMonth = caRegOrig.getRegExpMo();
			ciToYear = caRegOrig.getRegExpYr();
			return true;
		}
		else
		{
			//ASSIGN ToMonth/Year = current month/year 
			ciToMonth = ciCurrMo;
			ciToYear = ciCurrYr;
			return true;
		}
	}

	/**
		* Set To Month \ To Year to the minimum of Current Expiration 
		* or the Max Allowable Reg Months 
		* 
		*/
	private void setToMoToYrToMinOfCurrExpDtOrMaxAllow()
	{
		asgnToMoToYrNowPlusMaxRegMoMinusOne();
		// defect 8900
		// If coming from Standard Exempt, there is no 'original expdt'.
		// Use Max Allowable if Standard Exempt
		//if (ciToYear > caReg0.getRegExpYr()
		//	|| (ciToYear == caReg0.getRegExpYr()
		//		&& ciToMonth > caReg0.getRegExpMo()))
		if (caRegOrig.getRegExpYr() > 0
			&& caRegOrig.getRegExpMo() > 0
			&& ciToYear > caRegOrig.getRegExpYr()
			|| (ciToYear == caRegOrig.getRegExpYr()
				&& ciToMonth > caRegOrig.getRegExpMo()))
		{
			ciToMonth = caRegOrig.getRegExpMo();
			ciToYear = caRegOrig.getRegExpYr();
		}
		// end defect 8900
	}
	// end defect 8404	

	/**
	 * Set Special Plate From Month and Year
	 * Rule is Special Plate FromMo\Yr is the maximum of Today or the
	 * month after the 'target' Special Plate ExpMo\Yr   
	 */
	private void setSpclPltFromMoFromYr()
	{
		if (caRegUpdt != null
			&& caRegUpdt.getRegPltCd()
				!= null
					& PlateTypeCache.isSpclPlate(
						caRegUpdt.getRegPltCd()))
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				caMFVehUpdt.getSpclPltRegisData();
			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
			ciSpclPltFromMo = laSpclPltRegisData.getOrigPltExpMo();
			ciSpclPltFromYr = laSpclPltRegisData.getOrigPltExpYr();

			// If Special Plate registration is expired
			// defect 10357
			// Always charge Vendor Plate from end of current plate exp.
			//if (CommonValidations
			if (!PlateTypeCache.isVendorPlate(caRegUpdt.getRegPltCd())
				&& CommonValidations.isRegistrationExpired(
					laSpclPltRegisData.getPltExpMo(),
					laSpclPltRegisData.getPltExpYr(),
					ciTransGregorianDate))
			{
				// end defect 10357
				ciSpclPltFromMo = ciCurrMo;
				ciSpclPltFromYr = ciCurrYr;
			}
			// Else set FromMo\Yr to month after Spcl Plt ExpMo\Yr
			else
			{
				// defect 10392
				addOneMonthForSpclPltExp();

				// Moved to addOneMoForSpclPlt() method.
				////Adjustment for Dec month is handled here.
				//ciSpclPltFromMo =
				//	(ciSpclPltFromMo % 12) + 1;
				//// defect 10357					
				//ciTempFromYear = (Math.max(
				//	ciSpclPltFromYr, (ciCurrYr - 1)));
				//if (ciSpclPltFromMo == 1)
				//{
				//	ciSpclPltFromYr = ciSpclPltFromYr + 1;
				//}	
				// end defect 10392

				//ASSIGN TempFromYear to not more than one year ago 
				//ciTempFromYear =
				//	(ciSpclPltFromMo >= ciCurrMo)
				//		? (ciCurrYr - 1)
				//		: ciCurrYr;
				//ciTempFromYear =
				//	Math.max(
				//		ciTempFromYear,
				//		ciSpclPltFromYr);
				//Adjustment for Dec month is handled here.
				//ciSpclPltFromYr = 0;
				//if (ciSpclPltFromMo == 1)
				//{
				//	ciSpclPltFromYr = 1;
				//}
				//ciSpclPltFromYr = ciTempFromYear + ciSpclPltFromYr;
				// end defect 10357
			}
			// end defect 9864 
		}
	}

	/**
	 * Sort avAcctCdsData in Ascending order according to the 
	 * PrmtFeePrcnt field
	 */
	private void sortAcctCdVector(Vector avAcctCdsData)
	{
		int k, loc;
		int left = 0;
		int right = avAcctCdsData.size() - 1;
		for (k = left + 1; k <= right; k++)
		{
			AccountCodesData laAcctCdsData =
				(AccountCodesData) avAcctCdsData.get(k);
			loc = k;
			while (left < loc
				&& laAcctCdsData.getPrmtFeePrcnt().compareTo(
					((AccountCodesData) avAcctCdsData.get(loc - 1))
						.getPrmtFeePrcnt())
					< 0)
			{
				avAcctCdsData.set(loc, avAcctCdsData.get(loc - 1));
				loc--;
			}
			avAcctCdsData.set(loc, laAcctCdsData);
		}
	}
	/**
	 * Test for reasons *NOT* to call registration fee suite 
	 * 
	 */
	private boolean testForRsnNotCallRegFeeSuite()
	{
		boolean lbNotCallRegFeeSuite = false;

		// defect 9337
		// Do not call Reg Fee suite if 'TONLY' (Title Only - No Regis)
		if (ciRegWaivedIndi == 1
			|| ciOffHwyUseIndi == 1
			|| caRegUpdt.getRegClassCd()
				== CommonConstant.TONLY_REGCLASSCD)
		{
			lbNotCallRegFeeSuite = true;
		}
		// end defect 9337

		// Test for Out-of-scope inv
		// defect 9126
		// Do not check InvProcsngCd.Use method isOutOfScopePlate(). 
		//else if (
		//	caItemCdsData != null
		//		&& caItemCdsData.getInvProcsngCd() == 3)
		else if (
			PlateTypeCache.isOutOfScopePlate(caRegUpdt.getRegPltCd()))
		{
			lbNotCallRegFeeSuite = true;
		}
		// end defect 9126

		//TEST if regis is not being renewed, exchanged, or corrected
		//Test if number of months to charge for new regis = 0
		// defect 8900
		// Check for Exempt Indi = 0.
		// If so, need to call reg fee suite to set Exempt Reg 0.00.
		// defect 11282
		// Added check for 'if new plates desired and PTO Eligible
		// so that we can charge Automation fee if plate changes
		// even if nothing else changes (ie: no reg renew).
		else if (
			carrCompTransData[UPDT].getNoMoChrg() == 0
				&& !(carrCompTransData[ciFCalcRow].
					getRegTtlAddlInfoData().getNewPltDesrdIndi() == 1 
					&& caMFVehUpdt.isPTOEligible())
				&& ciMustChangePltIndi == 0
				&& caRegOrig.getRegPltCd() != null
				&& caRegOrig.getRegStkrCd() != null
				&& (caRegOrig.getRegPltCd()).equals(
					caRegUpdt.getRegPltCd())
				&& (caRegOrig.getRegStkrCd()).equals(
					caRegUpdt.getRegStkrCd())
				&& (caVehOrig.getVehClassCd()).equals(
					caVehUpdt.getVehClassCd())
				&& caRegOrig.getRegClassCd() == caRegUpdt.getRegClassCd()
				&& caVehOrig.getVehModlYr() == caVehUpdt.getVehModlYr()
				&& testIfVehWgtNotChnged()
				&& caRegUpdt.getExmptIndi() == 0)
		{
			// end 8900
			// end defect 11282			
			lbNotCallRegFeeSuite = true;
		}
		return lbNotCallRegFeeSuite;
	}

	// defect 9337
	// Do not need check for 'TONLY'
	/**
	* Test for Title Only(TONLY) and set ToMonth and ToYear for Renewal
	* @deprecated
	*/
	//	private boolean testForTOnlyAndSetToMoToYr()
	//	{
	//		if ((caRegOrig.getRegPltNo()).equals("TONLY"))
	//		{
	//			if (caCommFeesData.getMaxMYrPeriodLngth() > 0
	//				&& caVehUpdt.getVehModlYr() >= (ciCurrYr - 1))
	//			{
	//				//ASSIGN ToMonth/Year = now plus max multi-year regis
	//				ciToMonth =
	//					(ciCurrMo
	//						+ caCommFeesData.getMaxMYrPeriodLngth()
	//						- 2)
	//						% 12
	//						+ 1;
	//				ciToYear =
	//					ciCurrYr
	//						+ (new Double(caCommFeesData
	//							.getMaxMYrPeriodLngth()
	//							/ 12.0))
	//							.intValue();
	//			}
	//			else
	//			{
	//				//ASSIGN ToMonth/Year = now plus max allowable reg months-1                                        
	//				ciToMonth =
	//					((ciCurrMo
	//						+ caCommFeesData.getMaxallowbleRegMo()
	//						- 2)
	//						% 12)
	//						+ 1;
	//				ciToYear =
	//					ciCurrYr
	//						+ (new Double((ciCurrMo
	//							+ caCommFeesData.getMaxallowbleRegMo()
	//							- 2)
	//							/ 12.0))
	//							.intValue();
	//			}
	//			return true;
	//		}
	//		return false;
	//	}
	// end defect 9337

	/**
	 * Test if vehicles weight has not changed 
	 * 
	 */
	private boolean testIfVehWgtNotChnged()
	{
		boolean lbVehWtNotChanged = false;
		if (caRegOrig.getVehGrossWt() == caRegUpdt.getVehGrossWt())
		{
			lbVehWtNotChanged = true;
		}
		else
		{
			MFVehicleData laMFVD =
				carrCompTransData[ciFCalcRow].getVehicleInfo();
			RegistrationData laRegData = laMFVD.getRegData();
			int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
			int liRegClassCd = laRegData.getRegClassCd();
			//Select fee calc category from common fees 
			calcCommnFees();
			// defect 4913
			// Use current date to calculate the common fees.
			// defect 8900
			// This vehicle may be Standard Exempt (FeeCalcCat = 4)
			if (caCommFeesData == null)
			{
				caCommFeesData =
					CommonFeesCache.getCommonFee(
						liRegClassCd,
						liRTSCurrDate);
				int liFeeCalcCat = caCommFeesData.getFeeCalcCat();
				caCommFeesData = null;
				//if (liFeeCalcCat != 2)
				// defect 10685
				// FeeCalcCat 5 is also weight based 				
				if (liFeeCalcCat != 2
					&& liFeeCalcCat != 5
					&& !CommonFeesCache.isStandardExempt(liRegClassCd))
				{
					// end defect 10685
					lbVehWtNotChanged = true;
				}
			}
			//Test fee calc category not = 2 (weight based) 
			// This vehicle may be Standard Exempt (FeeCalcCat = 4)
			// defect 10685
			// FeeCalcCat 5 is also weight based 				
			else if (
				caCommFeesData.getFeeCalcCat() != 2
				&& caCommFeesData.getFeeCalcCat() != 5
				&& !CommonFeesCache.isStandardExempt(liRegClassCd))
			{
				// end defect 10685
				lbVehWtNotChanged = true;
			}
			// end defect 8900
		}
		return lbVehWtNotChanged;
	}
	/**
	 * Test if vehicles year model has not changed 
	 * 
	 */
	private boolean testIfVehYrMdlNotChanged()
	{
		boolean lbVehYrMdlNotChanged = false;
		if (caVehOrig.getVehModlYr() == caVehUpdt.getVehModlYr())
		{
			lbVehYrMdlNotChanged = true;
		}
		else
		{
			calcCommnFees();
			// defect 4913 
			// Use current date to calculate the common fees.
			if (caCommFeesData == null)
			{
				MFVehicleData laMFVD =
					carrCompTransData[ciFCalcRow].getVehicleInfo();
				RegistrationData laRegData = laMFVD.getRegData();
				int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
				int liRegClassCd = laRegData.getRegClassCd();
				caCommFeesData =
					CommonFeesCache.getCommonFee(
						liRegClassCd,
						liRTSCurrDate);
				int laFeeCalcCat = caCommFeesData.getFeeCalcCat();
				caCommFeesData = null;
				if (laFeeCalcCat != 1)
				{
					lbVehYrMdlNotChanged = true;
				}
			}
			//Test fee calc category not = 1 (year model based)
			else if (caCommFeesData.getFeeCalcCat() != 1)
			{
				lbVehYrMdlNotChanged = true;
			}
		}
		return lbVehYrMdlNotChanged;
	}
	/**
	 * Test if within window 
	 * 
	 * @return boolean
	 */
	private boolean testIfWithinWnd()
	{
		boolean lbWithinWindow = false;
		ciCurrentMonths = (ciCurrYr * 12) + ciCurrMo;
		ciExpirationMonths =
			(caRegOrig.getRegExpYr() * 12) + caRegOrig.getRegExpMo();
		if ((ciExpirationMonths - ciCurrentMonths < 3)
			&& (ciExpirationMonths - ciCurrentMonths >= 0))
		{
			lbWithinWindow = true;
		}
		return lbWithinWindow;
	}
	/**
	 * Test if no current registration 
	 * 
	 */
	private boolean testNoCurrReg()
	{
		boolean lbNoCurrReg = false;
		// defect 10349
		// Also check for regular exempt
		if (ciNoMfVeh == 0
			|| caRegOrig.getExmptIndi() == 1
			|| caRegOrig.getRegInvldIndi() == 1
			|| caRegUpdt.getRegInvldIndi() == 1
			|| (caVehMiscUpdt != null
				&& caVehMiscUpdt.getSpclPltProgIndi() == 1)
			|| ciUnregisterVehIndi == 1
			|| caRegOrig.getRegExpMo() == 0)
		{
			// end defect 10349
			lbNoCurrReg = true;
		}
		return lbNoCurrReg;

	}
	/**
	 * Title Fee Suite 
	 * 
	 */
	private void titleFeeSuite()
	{
		//Initialize Title Return Variables 
		carrCompTransData[ciFCalcRow].setVehTaxAmt(new Dollar(0.0));
		assignTtl();
		// defect 6447
		calcTtlTERPFee();
		calcTtlTrnsfrPnlty();
		calcSalesTax();
		//	VehMiscData lVehMiscData = carrCompTransData[ciFCalcRow].getVehMisc();
		//	if(  lVehMiscData != null
		//	  && lVehMiscData.getNoChrgSalTaxEmiFeeIndi() == 0 ){
		// SalesTax Surcharge
		// Allow calcSalesTaxSCharge() to determine if should charge. 
		// No longer have user option. 
		calcSalesTaxSCharge();
		// end defect 6447 
		calcSalesTaxPnlty();

		calcRegTrnsfr();
		// defect 9366
		calcBuyerTagFee();
		// end defect 9366
		
		// defect 11051 
		calcRbltSlvgFee(); 
		// end defect 11051
	}
	/**
	 * Update Customer Actual Registration Fee
	 * 
	 */
	private void updateCustActlRegFee()
	{
		Dollar laCustActulRegFee =
			carrCompTransData[ciFCalcRow].getCustActualRegFee();
		carrCompTransData[ciFCalcRow].setCustActualRegFee(
			caItmPrice.addNoRound(laCustActulRegFee));
	}
	/**
	 * Set-up for whether or not to issue inventory for title
	 * 
	 */
	public void whetherIssueInvForTtl()
	{
		//reset inventory table and count to original values
		ciInvItmCount = carrCompTransData[UPDT].getSavedInvItmCount();
		carrCompTransData[UPDT].setInvItemCount(ciInvItmCount);
		Vector lvSavedInvItms =
			(Vector) UtilityMethods.copy(
				carrCompTransData[UPDT].getSavedInvItms());
		carrCompTransData[UPDT].setInvItms(lvSavedInvItms);
		// defect 9085 
		// PlateTypeCache replaces RegistrationRenewalsCache
		// RegistrationRenewalsData laRegRenwlData = null;
		PlateTypeData laPlateTypeData = null;
		if (caRegUpdt != null && caRegUpdt.getRegPltCd() != null)
		{
			laPlateTypeData =
				PlateTypeCache.getPlateType(caRegUpdt.getRegPltCd());
			//			laRegRenwlData =
			//				RegistrationRenewalsCache.getRegRenwl(
			//					caReg1.getRegPltCd());
		}
		// defect 8482
		// Add verification of same RegClassCd  
		if ((carrCompTransData[UPDT].getNoMoChrg()
			- carrCompTransData[ORIG].getNoMoChrg()
			<= 0)
			&& caRegUpdt.getRegClassCd() == caRegOrig.getRegClassCd()
			&& caRegUpdt.getRegPltCd().equals(caRegOrig.getRegPltCd())
			&& ciMustChangePltIndi != 1 //&& laRegRenwlData != null
			//&& (laRegRenwlData.getAnnualPltIndi() != 1
			// defect 11264
			// We still want to issue a sticker if ONLY 'New Plates Desired' selected
			//(ie: even if plate age < 7, not extending reg, dealer gdn not entered)
			// Note: all of this ugly code in this whole section is trying to handle:
			// DTA needs to keep issuing Form31 even if we don't issue plate nor stkr			
			&& !(carrCompTransData[ciFCalcRow].
				getRegTtlAddlInfoData().getNewPltDesrdIndi() == 1 
				&& caMFVehUpdt.isPTOEligible())
			 // end defect 11264			
			&& laPlateTypeData != null
			&& (laPlateTypeData.getAnnualPltIndi() != 1
				|| !isRegExpired()))
			// end defect 9085 
		{
			// defect 10999
			// Note: see also defect 10951 for 'must chg plt' stuff
			// Add check for New Plates Desired Indi
			if (csTransCd.equals(TransCdConstant.DTANTD)
				|| csTransCd.equals(TransCdConstant.DTANTK)
				|| csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTAORK))
				// defect 11264
				// This is handled above
				//|| (carrCompTransData[ciFCalcRow].
				//	getRegTtlAddlInfoData().getNewPltDesrdIndi() == 1 
				//	&& caMFVehUpdt.isPTOEligible()))
				// end defect 11264
			{
				// end defect 10999
				ciInvItmCount = 1;
			}
			else
			{
				ciInvItmCount = 0;
			}
			carrCompTransData[UPDT].setInvItemCount(ciInvItmCount);
			Vector lvInvItms = carrCompTransData[UPDT].getInvItms();
			ProcessInventoryData laProcInvData = null;
			for (int i = 0; i < lvInvItms.size(); i++)
			{
				laProcInvData =
					(ProcessInventoryData) lvInvItms.elementAt(i);
				ItemCodesData laItmCdsData =
					ItemCodesCache.getItmCd(laProcInvData.getItmCd());
				//Remove Plate and Sticker from issue inventory vector
				// defect 10999
				// Note: see also defect 10951 for 'must chg plt' stuff
				// Add check for New Plates Desired Indi
				if (laItmCdsData.getItmTrckngType()
					.toUpperCase().equals("S") ||
					(laItmCdsData.getItmTrckngType()
						.toUpperCase().equals("P") &&
					carrCompTransData[ciFCalcRow].
						getRegTtlAddlInfoData().
						getNewPltDesrdIndi() == 0))
				{
					// end defect 10999
					lvInvItms.removeElementAt(i);
					i--;
				}
			}
		}
		// end defect 8482 
	}
}
