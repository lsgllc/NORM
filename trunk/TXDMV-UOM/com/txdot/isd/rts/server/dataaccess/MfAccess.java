package com.txdot.isd.rts.server.dataaccess;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Vector;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;

import com.txdot.isd.rts.services.cache.PaymentStatusCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.ApplicationControlConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MessageConstants;

import com.txdot.isd.rts.server.db.MFRequest;
import com.txdot.isd.rts.server.systemcontrolbatch.MFTrans;

/*
 *
 * MfAccess.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam 08/28/2001	Creating the MfAccess Class
 * Marx Rajangam 10/02/2001	Changing the comments and formats
 * 							after the baseline
 * Marx Rajangam 11/30/2001	FundsUpdate changed
 * Marx Rajangam 11/30/2001	ActiveInactive changed to make REGIS calls
 * Richard Hicks 01/11/2002 Enable unique identifier for TSQX name per 
 * 							graph
 * Marx Rajangam 01/15/2002	Changed resizeStringtolength() to truncate 
 * 							if String longer than specified length
 * Marx Rajangam 04/10/2002	Adding javadoc, comments and code cleanup
 * Marx Rajangam 04/10/2002	Adding VSAM file updates on failure. This
 * 							error logging should be done at indivudual 
 * 							request method level and not in 
 * 							<code>getMfResponse()</code>
 * 							or in <code>sendMfUpdate()</code> as the 
 * 							county number, wsid, etc are required to 
 * 							print the error log in the VSAM.
 * Marx Rajangam 04/24/2002	Added OfcIssuanceNo, WSID, transamdate and 
 * 							transtime to the error log in case of a mf
 * 							update failure
 * Marx Rajangam 04/30/2002	Changed <code>handleMFError()</code> to
 * 							log error level as <b>FP</b>. 
 * 							defect 3736
 * Marx Rajangam 05/08/2002	Changed <code>getValuesFromFundsDtlData</code>
 * 							and <code>getValuesFromFundFuncData</code>
 * 							to use <code>getValuesFromDollar</code> to 
 * 							convert dollar values to MF strings.
 * MAbs 		10/22/2002 Altered INSERT_AT_END to INSERT_AT_BEGINNING
 * K Harrell               for ofcissuanceno in sendFundsUpdateToMF
 * 							defect 4779
 * Ray Rowehl	02/20/2003	Get netname through constructor.
 *							defect 4588
 * K Harrell	02/27/2003	Assign WsLUName to "MFAccess" on 
 * 							handleMFError
 * 							defect 4928
 * Ray Rowehl	03/05/2003	Add date and time stamp message before 
 * 							stack traces
 * 							defect 5653
 * K Harrell    06/05/2003  Send Trans, Trans_Payment and LogFuncTrans 
 * 							together
 *                          method sendTransaction()
 * 							defect 6227
 * Ray Rowehl	06/17/2003	Make transids public so test tool can 
 * 							change them to 99.
 * Ray Rowehl	07/27/2003	add a method to just do VINA for 
 * 							MfAccessTest.
 *							new method retrieveVehicleFromVINAOnly()
 *							for MFAccessTest.
 * Ray Rowehl	05/17/2004	Add a constant for the 99 trans
 *							add csR99
 *							defect 6785 Ver 5.2.0
 * Ray Rowehl	03/05/2005	Allow the ServerId to be two characters.
 * 							format source, organize imports,
 * 							rename fields.
 * 							modify getTsqNumber()
 * 							defect 7937 Ver 5.2.3
 * Ray Rowehl	04/08/2005	Further code cleanup
 * 							defect 7937 Ver 5.2.3
 * Ray Rowehl	04/14/2005	Field name cleanup.  Constants.
 * 							More work to be done yet.
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	04/18/2005	Further testing with ServerId's longer than
 * 							two positions showed a need to update the
 * 							JavaDoc for the method.
 * 							modify getTsqNumber()
 * 							defect 7937 Ver 5.2.3
 * K Harrell	05/19/2005	FundsPaymentDataList, FundsUpdateData
 * 							Object element renaming
 * 							modify retrieveFundsPaymentDataList(),
 * 							setUpdateToMF()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	09/14/2005	Remove Sticker Logic. Remove asStickerNo
 * 							from setupMfKeys() arguments and all calls
 * 							modify setupMfKeys(),
 * 							retrieveVehicleFromActiveInactive(),
 * 							retrieveVehicleFromArchive() 
 * 							defect 7144 Ver 5.2.3       
 * T Pederson	09/28/2006	Added call for converting plate number i's 
 * 							and o's to 1's and 0's. 
 * 							modify retrieveVehicleFromActiveInactive(),
 * 							retrieveVehicleFromArchive() and
 * 							retrieveVehicleBySpecialOwner()
 * 							defect 8902 Ver Exempts
 * 							defect 7144 Ver 5.2.3
 * J Rue		09/27/2006	Change methods from private to public so
 * 							new G and T classes can access
 * 							modify getStringFromZonedDecimal()
 * 							modify trimMfString()
 * 							defect 6701 Ver 5.3.0 
 * J Rue		10/06/2006	Clean up Special Regis 
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							modify retrieveVehicleBySpecialOwner()
 * 							defect 6701 Ver 5.3.0 
 * Ray Rowehl	10/10/2006	Work on converting SendTrans buffer handling
 * 							to use new buffer definition classes.
 * 							Make some data handling methods public to 
 * 							used in the new classes.
 * 							Did some minor code cleanup.
 * 							delete getValuesFromFundFuncTrans(),
 * 								getValuesFromFundsDtlTransactionData(),
 * 								getValuesFromInvFuncTransData(),
 * 								getValuesFromLogonFunc(),
 * 								getValuesFromMVFuncTransactionData(),
 * 								getValuesFromTransactionData(),
 * 								getValuesFromTransactionPaymentData(),
 * 								getValuesFromTransInvDetailData()
 * 							modify getStringFromZonedDecimal(), 
 * 								getValueFromDollar(), sendTransaction(),
 * 								setZonedDecimalFromInt(),  
 * 								setZonedDecimalFromNegativeNumber(),
 * 								setZonedDecimalFromPositiveNumbers()
 * 							defect 6701 Ver Exempts  
 * Ray Rowehl	10/14/2006	Convert Inventory Invoice Retrieval to use 
 * 							new formatting scheme.
 * 							delete setInvoiceFromMf()
 * 							deprecate retrieveTimeDate()
 * 							modify retrieveInvoice()
 * 							defect 6701 Ver Exempts  
 * J Rue		10/16/2006	Replace all local and class "" with 
 * 								CommonConstant.STR_SPACE_EMPTY
 * 							modify setMultipleFundsPaymentDataFromMf()
 * 							defect 6701 Ver Exempts   
 * J Rue		10/16/2006	Added call for converting plate number i's 
 * 							and o's to 1's and 0's. 
 * 							modify retrieveVehicleBySpecialOwner()
 * 							defect  8902 Ver Exempts 
 * J Rue		10/17/2006	Add try/catch to handle RTSException
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 6701 Ver Exempts 
 * J Rue		10/17/2006	repalce local string with Application 
 * 							constants    
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Add call to get VINA data from MfbaVinaG()
 * 							modify setMFVehicleDataFromMfResponse()
 * 							defect 6701, Ver Exempts
 * Ray Rowehl	10/17/2006	Rename of SystemProperty.getMfTransVersion
 * 							to getMfInterfaceVersionCode()
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Move setMultipleRegistrationDataFromMf()
 * 							to data.MfbaMuptRegisG
 * 							deprecate setMultipleRegistrationDataFromMf()
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/19/2006	Reverse order of if statement
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move V version to services.data cless
 * 							modify setMFVehicleDataFromMfResponse()
 * 							deprecate setRegistrationDataFromMf
 * 								setVehicleInquiryDataFromSpecialOwnerResponse()
 * 								setVehicleDataFromMf()
 * 								setTitleDataFromMf()
 * 								setMfVehicleDataFromVINAResponse()
 * 								setMultipleRegistrationDataFromMf()
 * 								setMFPartialDataFromMfResponse()
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	10/27/2006	Add a check to detect CICS.JGATE message.
 * 							If it occurs, this is an error.
 * 							modify getMfResponse(), sendUpdateToMF()
 * 							defect 6701 Ver Exempts
 * J Rue		10/27/2006	Move TIP to data package
 * 							modify retrieveTitleInProcess()
 * 							deprecate setTitleInProcessDataFromMfResponse()
 * 							Create method to set DocNoKey
 * 							add setDocNoKey()
 * 							modify retrieveVehicleFromActiveInactive()
 * 								   retrieveVehicleFromArchive()
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	11/03/2006	Add logic to ensure the CTG connection is 
 * 							closed and released.
 * 							modify getMfResponse(), sendUpdateToMF()
 * 							defect 6701 Ver Exempts
 * J Rue		11/03/2006	Move FundPayment to data package 
 * 							modify retrieveFundsPaymentDataList()
 * 							deprecated retrieveFundsPaymentDataList()
 * 							defect 6701 Ver Exempts
 * J Rue		11/10/2006	Use JGateErrorFlag to test for JGate 
 * 							problems. 
 * 							Check Mf Version Code 
 * 							If error log go mainframe down
 * 							modify getMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		11/13/2006	Move MF version test to before MF call
 * 							modify getMfResponse()
 * 							defect 6701 Ver Exempts
 * J Rue		01/23/2007	Change getMultipleRegJunkIndi to public
 * 							method, call by searchMfActiveInact
 * 							modify getMultipleRegJunkIndi()
 * 							modify setDocNoKey()
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 9086 Ver Special Plates 
 * J Rue		01/23/2007	New process that retrieves MF records in
 * 							Active/Inactive/Acrchive, 
 * 							return buffer string.
 * 							add retrieveVehicleFromMF()
 * 							defect 9086 Ver Special Plates Full
 * J Rue		01/23/2007	New process that removes the header from the
 * 							Mfresponse record
 * 							add getMfTtlRegResponce()
 * 							defect 9086 Ver Special Plates 
 * J Rue		01/24/2007	Note in JavaDoc: This method will be 
 * 							depercated when Special Plates 
 *        					goes statewide.
 * 							modify setupMfkeys()
 * 							defect 9086 Ver Special Plates Full
 * J Rue		01/24/2007	New method for Special Plate MF key setup.
 * 							ReqType, RegStkrNo, OfcissuanceNo
 * 							were removed. SpclRegId replaced RegExpYr
 * 							add setupMfKeysSpclPlt()
 * 							defect 9086 Ver special Plates 
 * J Rue		01/31/2007	Add Special Plates, make call to parse data
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							defect 9086 Ver Special PLates
 * J Rue		02/02/2007	Turn off check for MFVersion U
 * 							modify getMultipleRegJunkIndi()
 * 							defect 9086 Ver 9086
 * J Rue		02/06/2007	Update header set Key
 * 							modify retrieveOwner()	
 * 							defect 9086 Vef Special Plates
 * J Rue		02/06/2007	Add asSpclPltRegisResponse to 
 * 							setVehicleInquiryDataFromMfResponse(). 
 * 							Update all calling methods
 * 							modify setVehicleInquiryDataFromMfResponse()
 * 							modify retriveJunkRecords()
 * 							modify retrieveVehicleFromVINAOnly()
 * 							modify retrieveVehicleFromVINA()
 * 							modify retrieveVehicleFromArchive()
 * 							modify retrieveVehicleFromActiveInactive()
 * 							modify retrieveVehicleBySpecialOwner()
 * 							defect 9086 Ver Special PLates
 * J Rue		02/08/2007	Create MF Search by OwmrId, VIN
 * 							add retrieveMfRecordsByOwnrId()	
 * 							add retrieveMfRecordsByVIN()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/12/2007	Convert remaining inquires to new MfKeys
 * 							modify retrieveFundsPaymentDataList()
 * 							modify retrieveFundsRemittanceRecord()
 * 							modify retrieveInvoice()
 * 							modify retrieveNumberofDocuments()
 * 							modify retrieveOwner()
 * 							modify retrieveTitleInProcess()
 * 							modify retrieveVehicleBySpecialOwner()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/15/2007	Add comments to R08 call
 * 							modify retrieveVehicleFromMF()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/22/2007	Rename setSpclPltRegis() to 
 * 							setSpclPltRegisData()
 * 							modify setMFVehicleDataFromMfResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/28/2007	Null SpclPltRegisData if MF return string
 * 							is empty.
 * 							Clean up code, SendTrans
 * 							modify setMFVehicleDataFromMfResponse()
 * 							modify sendTrans()
 * 							defect 9086 Ver Special Plates
 * Ray Rowehl	03/06/2007	Change key setup to pass vin as vin to 
 * 							VINA instead as docno.
 * 							search for xxxx and TODO
 * 							modify retrieveVehicleFromVINA(),
 * 								retrieveVehicleFromVINAOnly()
 * 							defect xxxx Ver Special Plates
 * K Harrell	04/03/2007	Cleanup 
 *  J Rue		04/18/2007	Add exception to handle MF down
 * 							modify retrieveVehicleFromMF(),
 * 							modify retrieveMfRecordsByDocNo(),
 * 							modify retrieveMfRecordsByRegPltNo()
 * 							defect 9085/9085 Ver Special Plates
 * J Rue		04/26/2007	Move Funds Remittance processing to 
 * 							services.data
 * 							modify 	retrieveFundsPaymentDataList()	
 * 							modify retrieveFundsRemittanceRecord()
 * 							deprecate setMultipleFundsDueDataFromMf()
 * 							deprecate retrieveVehicleFromVINA(),
 * 							deprecate retrieveVehicleFromVINAOnly()
 * 							defect 8983 Ver Special Plates
 * J Rue		04/27/2007	Move set data to services.data
 * 							Get Outputlength from MfResponse is called
 * 							from APPCHeader
 * 							modify retrieveOwner(), voidTransactions()
 * 							 retrieveVehicleFromVINA(),
 * 							 retrieveVehicleFromVINAOnly(),
 * 							 retrieveVehicleFromArchive(),
 * 							 getMfTtlRegResponce(), printTrace()
 * 							 retrieveVehicleFromActiveInactive(),
 * 							 retrieveVehicleBySpecialOwner()
 * 							deprecate setOwnerDataFromMfOwner()
 * 							defect 8983 Ver Special Plates
 * J Rue		05/14/2007	Correct search key header for RegPltNo and
 * 							VIN
 * 							modify retrieveVehicleFromArchive()
 * 							defect 8984 Ver special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * J Rue		10/30/2007	Add MF_NET_NAME, MfKeys and CICS error code
 * 							modify getMfResponse()
 * 							defect 9403 Ver Special Plates
 * J Rue		10/31/2007	Add try/catch box for ConnectionFlow
 * 							ECIRequest, JavaGateway
 * 							modify getConnection(), getMfResponse()
 * 							defect 9403 Ver Special Plates
 * J Rue		11/07/2007	Remove isInboundDataLength() and
 * 							 getInboundDataLength()
 * 							modify MfResponse()
 * 							defect 9403 Ver Special Plates
 * J Rue		11/12/2007	Add additional logging for connection
 * 							issues
 * 							modify MfResponse()
 * 							defect 9403 Ver Special Plates 
 * Ray Rowehl	11/13/2007	Modification to get running in development
 * 							again.  See "RRR" TODO block.
 * 							Need check for > 2 instead of < 2..
 * 							modify getMfResponse()
 * 							defect 9403 Ver Special Plates
 * J Rue		04/03/2008	Adjust MULTIPLE_REG_INDI_POSITION_V and
 * 							JNK_INDI_POSITION_V for MF version V
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/04/2008	Adjust DOC_NO_OFFSET_V
 * 							modify setDocNoKey()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/30/2008	Adjust offsets for MultRegIndi and JunkIndi
 * 							Adjust offset for DocNo
 * 							modify setDocNoKey()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/06/2008	Re-order method and class variables
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/18/2008	Remove header from MfResponse
 * 							modify retrieveTitleInProcess()	
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/25/2008	Adjust offset from 148 to 149 to capture the
 * 							TraceNo
 * 							modify sendFundsUpdateToMF()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/15/2008	Add call to read test records for SendTrans 
 * 							buffer for processing.
 * 							modify sendTransaction()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/22/2008	Add ofcissuanceno, wsid, transamdate, and
 * 							transtime to sendtrans vector for sendtrans
 * 							testing.
 * 							modify sendTransaction()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/25/2008	Set SnedTrans adjusted time length = 6
 * 							modify sendTransaction()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/26/2008	Get adjusted TransTime. This will replace 
 * 							the current time stamp (part of the TransId)
 * 							on the SendTrans records.
 * 							add resetTransId()
 * 							modify sendTransaction()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		11/06/2008	Adjust offsets for T version
 * 							modify MULTIPLE_REG_INDI_POSITION_T,
 * 								JNK_INDI_POSITION_T, setDocNoKey()
 * 							defect 9833 Ver Defect_POS_B
 * J Rue		01/16/2009	Adjust Mult and Junk offsets
 * 							modify setDocNoKey(),
 * 							 MULTIPLE_REG_INDI_POSITION_U,
 *  						 JNK_INDI_POSITION_U
 * 							defect 9655  Ver Defect_POS_D
 * J Rue		01/29/2009	Get DocNo from Key2 (VehInqTest)
 * 							modify retrieveNumberofDocuments()
 * 							defect 8984 Ver Defect_POS_D
 * J Rue		02/26/2009	Adjust offset for ELT
 * 							modify MULTIPLE_REG_INDI_POSITION_V,
 * 								JNK_INDI_POSITION_V
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	03/18/2009	Assign Certified Lienholder Data from 
 * 							Cache. 
 * 							modify setMFVehicleDataFromMfResponse() 
 * 							defect 9987 Ver Defect_POS_E
 * K Harrell	07/02/2009	delete deprecated method
 * 							removed 2nd parameter where not used 
 * 							delete setMultipleFundsDueDataFromMf(),
 * 							 setOwnerDataFromMfOwner() 
 * 							modify retrieveFundsPaymentDataList(),
 *  						 retrieveFundsRemittanceRecord(), 
 *  						 sendFundsUpdateToMF()
 * 							defect 10112 Ver Defect_POS_F 
 * J Rue		07/07/2009	Move defect 6701 constants to class level
 * 							from getMfResponse(). Write RTSJGate error 
 * 							to VSAM file for both vehicle inquiry and
 * 							SendTrans
 * 							modify getMfResponse(), sendUpdateToMF(),
 * 								handleMfError()
 * 							defect 10121 Ver Defect_POS_F
 * J Rue		07/08/2009	Add check if to reset TransTime for 
 * 							MfAccessTest testing duplictaes. 
 * 							modify sendTransaction()
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		10/06/2009	Adjust offsets for T version. 
 * 							Move ERROR_CODE_OFFSET and 
 * 							MF_ERROR_TYPE_CD_LENGTH to class level
 * 							Set variable NO_OF_RECS_OFFSET to constants.
 * 							add MF_ERROR_CD_OFFSET, MF_ERROR_CD_LENGTH,
 * 								NO_OF_RECS_OFFSET 
 * 							modify getMfResponse(),sendFundsUpdateToMF(),
 * 								sendTransaction(), handleMfError(),
 * 								retrieveNumberofDocuments(),
 * 								voidTransactions(), setDocNoKey()
 * 								MULTIPLE_REG_INDI_POSITION_T,
 * 								JNK_INDI_POSITION_T, setDocNoKey()
 * 							defect 10244 Ver Defect_POS_G
 * J Rue		10/16/2009	Copy code from V version to update 
 * 							sendFundsUpdateToMF and to retain comments.
 * 							modify sendFundsUpdateToMF()
 * 							defect 10244 Ver Defect_POS_G   
 * J Rue		10/21/2009	Adjust offset retrieve by DocNo
 * 							modify setDocNoKey()
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/10/2010	Change lsFundsDueDate to "00000000".
 * 							defect 10406 Ver POS_640
 * M Reyes		03/11/2010	Modify MULTIPLE_REG_INDI_POSITION_U,
 * 							JNK_INDI_POSITION_U and DOC_NO_OFFSET_U
 * 							defect 10378 Ver POS_640
 * K Harrell	06/10/2010	Changes for Temp Permit
 * 							Refactor methods to correct spelling errors
 * 							(Responce => Response, retrve => retrieve)  
 * 							add retrievePermit(), setMFPermitData(),
 * 							 setupMFKeysSpclPlt(21 parms)
 * 							modify setupMFKeysSpclPlt(15 parms) 
 * 							delete retrieveTimeDate(), setupMfKeys() 
 * 							defect 10492 Ver 6.5.0  
 * K Harrell	06/23/2010 Modify MULTIPLE_REG_INDI_POSITION_V,
 * 							JNK_INDI_POSITION_V and DOC_NO_OFFSET_V 
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	08/02/2010	Add DB2 logging for each MF Request, 
 * 							 excluding SendTrans/Send Error to VSAM
 * 							add APPC_HDR_ERR_CD, INIT_CONNECT_ERR_CD,
 * 							 SETUP_COMM_AREA_ERR_CD, INCORRECT_MF_VERSION_ERR_CD, 
 * 							 RETRY_CONNECT_ERR_CD, 
 * 							 RETRY_CONNECT_NOT_OPEN_ERR_CD,
 * 							 CONNECT_FLOW_ERR_CD,
 * 							 CONNECT_FLOW_INVALID_RC_ERR_CD,
 * 							 ECI_REQUEST_ERR_CD,
 * 							 RTS_JGATE_ERR_CD,
 * 							 MF_IO_EXCEPTION_ERR_CD,
 * 							 MF_NULL_PTR_ERR_CD, 
 * 							 MF_UNKNOWN_ERR_CD 
 * 							add caMFReqData
 * 							modify getMfResponse(), handleMfError() , 
 * 							 sendFundsUpdateToMF(), sendUpdateToMF(), 
 * 							 setupMfKeysSpclPlt()
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	08/03/2010	modify to recognize different levels of 
 * 							DB2 logging. 
 * 							modify getMfResponse(), sendUpdateToMF()
 * 							defect 10462 Ver 6.5.0
 * K Harrell	08/10/2010  Additional work to improve MfAccess 
 * 							processing
 * 							add MFACCESS_ERROR 
 * 							delete MFACCESS_ACCESS
 * 							modify getMfResponse(), getConnection()
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	08/12/2010	Add logging for null data 
 * 							modify setupMfKeysSpclPlt() 
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	08/31/2010	delete caVehInq
 * 							add rtnNoOfRecsMF()
 * 							modify retrieveNumberofDocuments(), 
 * 							  retrieveVehicleFromActiveInactive(),
 * 							  retrieveVehicleFromArchive(), 
 * 							  retrieveVehicleFromVINA(), 
 * 							  voidTransactions() 
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	09/02/2010	Right padding on PrmtNo
 * 							modify setupMfKeysSpclPlt()
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		10/06/2010	Modify MULTIPLE_REG_INDI_POSITION_U,
 * 							JNK_INDI_POSITION_U, MFKEYS_LENGTH
 * 							and DOC_NO_OFFSET_U
 * 							Modify setupMfKeysSpclPlt()
 * 							defect 10595 Ver POS_660
 * K Harrell	10/20/2010	Add logging when over 4 seconds in  
 * 							getMfResponse() 
 * 							modify getMfResponse()
 * 							defect 10637 Ver 6.6.0 
 * K Harrell	11/08/2010	Add logging before/after check connection
 * 							modify getMfResponse()
 * 							defect 10637 Ver 6.6.0
 * M Reyes		01/19/2011	modify MULTIPLE_REG_INDI_POSITION_U,
 * 							JNK_INDI_POSITION_U
 * 							defect 10710 Ver POS_670 
 * K Harrell	11/02/2011	assign MULTIPLE_REG_INDI_POSITION_X, 
 * 							 DOC_NO_OFFSET_X from MfbaTitlex
 * 							modify MULTIPLE_REG_INDI_POSITION_T,
 * 							 MULTIPLE_REG_INDI_POSITION_U,
 * 							 MULTIPLE_REG_INDI_POSITION_V  
 *							modify DOC_NO_OFFSET_T,DOC_NO_OFFSET_U, 
 *							 DOC_NO_OFFSET_V 
 * 							defect 11045 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Executes CICS transactions on the TxDOT mainframe. Used by the
 * server side business layer. 
 *
 * @version	6.9.0 				11/02/2011
 * @author	Marx Rajangam
 * <br>Creation Date:			08/28/2001 16:32:32
 */

public class MfAccess
{
	// defect 10244
	final int MF_ERROR_TYPE_CD_OFFSET = 132;
	final int MF_ERROR_TYPE_CD_LENGTH = 2;
	final int MF_ERROR_TYPE_MSG_OFFSET = 171;
	final int MF_ERROR_TYPE_MSG_LENGTH = 85;
	final int MF_ERROR_CD_OFFSET = 142;
	final int MF_ERROR_CD_LENGTH = 3;
	final int NO_OF_RECS_OFFSET = 145;
	// end defect 10244
	// defect 6701
	//	JGate error flag offset
	final int JGATE_ERROR_FLAG_OFFSET = 68;
	final int JGATE_ERROR_FLAG_LENGTH = 1;
	final int JGATE_ERROR_MESSAGE_OFFSET = 128;
	final int JGATE_ERROR_MESSAGE_LENGTH = 80;
	final int HEADER_TRANSID_OFFSET = 0;
	final int HEADER_TRANSID_LENGTH = 4;
	final String XXXX = "XXXX";
	// end defect 6701

	//
	private final static String MF_ADABAS_ERROR = "03";
	private final static String MF_CICS_ERROR = "02";
	private final static String MF_LOGIC_ERROR = "01";
	private final static String MF_PROGRAM_ERROR = "04";

	// defect 10462 
	private MFRequestData caMFReqData;

	// Constants to Track MF Error Types
	// Following for Comments Only 
	/**
	 * MF_LOGIC_ERR_CD: 1
	 */
	//	private final static int MF_LOGIC_ERR_CD =
	//		Integer.parseInt(MF_LOGIC_ERROR);
	/**
	 * MF_CICS_ERR_CD: 2
	 */
	//	private final static int MF_CICS_ERR_CD =
	//		Integer.parseInt(MF_CICS_ERROR);

	/**
	 * MF ADABAS_ERR_CD: 3
	 */
	//	private final static int MF_ADABAS_ERR_CD =
	//		Integer.parseInt(MF_ADABAS_ERROR);

	/**
	 * MF_PROGRAM_ERR_CD: 4
	 */
	//	private final static int MF_PROGRAM_ERR_CD =
	//		Integer.parseInt(MF_PROGRAM_ERROR);

	/**
	 * APPC_HDR_ERR_CD: 5
	 */
	private final static int APPC_HDR_ERR_CD = 5;

	/**
	 *  SETUP_COMM_AREA_ERR_CD: 6
	 */
	private final static int SETUP_COMM_AREA_ERR_CD = 6;

	/**
	 * INCORRECT_MF_VERSION_ERR_CD: 7
	 */
	private final static int INCORRECT_MF_VERSION_ERR_CD = 7;

	/**
	 * RETRY_CONNECT_ERR_CD: 8
	 */
	private final static int RETRY_CONNECT_ERR_CD = 8;

	/**
	 * RETRY_CONNECT_NOT_OPEN_ERR_CD: 9
	 */
	private final static int RETRY_CONNECT_NOT_OPEN_ERR_CD = 9;

	/**
	 * CONNECT_FLOW_ERR_CD: 10
	 */
	private final static int CONNECT_FLOW_ERR_CD = 10;

	/**
	 * CONNECT_FLOW_INVALID_RC_ERR_CD: 11
	 */
	private final static int CONNECT_FLOW_INVALID_RC_ERR_CD = 11;

	/**
	 * ECI_REQUEST_ERR_CD: 12
	 */
	private final static int ECI_REQUEST_ERR_CD = 12;

	/**
	 * RTS_JGATE_ERR_CD: 13
	 */
	private final static int RTS_JGATE_ERR_CD = 13;

	/**
	 * MF_IO_EXCEPTION_ERR_CD: 14
	 */
	private final static int MF_IO_EXCEPTION_ERR_CD = 14;

	/**
	 * MF_NULL_PTR_ERR_CD: 15
	 */
	private final static int MF_NULL_PTR_ERR_CD = 15;

	/**
	 * MF_UNKNOWN_ERR_CD: 16
	 */
	private final static int MF_UNKNOWN_ERR_CD = 16;
	// end defect 10462 

	/**
	 * MfAccessTest adjusted SendTrans time length
	 */
	private final static int SENDTRANS_ADJ_TIME_LEN = 6;
	/**
	 * MfAccessTest SendTrans record count of all the files
	 */
	private final static int SENDTRANS_RECORD_COUNT_INDEX = 0;
	/**
	 * MfAccessTest SendTrans data
	 */
	private final static int SENDTRANS_DATA_INDEX = 1;
	/**
	 * Name of the system property retrieved from the JVM running an 
	 * application server. This property name is used in retrieving 
	 * the ServerId property set for each application server sending
	 * requests to Mainframe. 
	 */
	private static final String SERVER_ID_SYSTEM_PROPERTY_NAME =
		"ServerId";
	/**
	 * Send Write Error Log request to MainFrame.
	 * 
	 * @see #logError()
	 */
	public static String RSE = "RSE";
	// defect 9403
	private final static String SEARCH_CRITERIA =
		": Search Criteria - ";
	/** 
	* Holds the JavaGatWay port number
	*/
	private static int siJavaGatewayPort = 0;
	/**
	 * MF Debug mode indicator. If enabled, will print trace of the 
	 * request sent to the mainframe and the response from mainframe. 
	 */
	private static int siMFDebugMode = 0;
	// defect 10462 
	//	static int siPrematureClose = 0;
	//	static int siServerConnectionAttempts = 0;
	// end defect 10462 

	/**
	 * The suffix of the Temporary storage queue name. This suffix 
	 * creates 9999 unique ids. 
	 */
	private static int siTSQNumber = 0;
	/**
	 * Size of the input buffer sent to the mainframe. 
	 */
	/**
	 * JavaGateway address. Requests to mainframe are sent to the 
	 * javagateway and then they get routed to the mainframe. 
	 */
	private static String ssJavaGateway =
		CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Name of the MF JavaGate routing program called
	 */
	private static String ssMFJGate = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Name of the MF Server. The java gate sends the request to this 
	 * server.
	 */
	private static String ssMFServer = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Mainframe Timeout limit set for the javagate.
	*/
	private static String ssMFTimeout = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * The version code of the mainframe transaction that should be used
	 * to process the request. 
	 */
	private static String ssMFTransactionVersion =
		CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Server Id for the application server in a Clustered environment.
	 * Used to create unique TSQ Names when more than one application
	 * server is sending requests to the mainframe. 
	 */
	private static String ssServerId = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Offset for the transaction version in the buffer.
	 */
	private static final int TRANS_VERSION_OFFSET = 1;
	public final static int TRANSACTION_FAILED = 1;
	public final static int TRANSACTION_SUCCESSFUL = 0;
	/**
	* The prefix of the Temporary storage queue name. 
	*/
	private final static String TSQX_NAME = "TSQX";

	private final static String CICS_ERROR_CD =
		": CICS Error Code\\Text - ";
	/**
	 * Size of Comm Area set up for Communicating with CICS
	 */
	private final static int COMM_AREA_LENGTH = 32500;
	/**
	 * Name of the MF Database where the record is retrieved 
	 * from/updated. 
	 */
	private static String csMFDBName = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Prefix set in BufferDescArea for FundsUpdate  
	 */
	private final static String FDSUPDATE_DESC_AREA_PREFIX =
		"FDSUPDA 001FDSUPDB ";
	/**
	 * Flag for Yes
	 */
	private final static String FLAG_YES = "Y";

	/**
	 * The MF Error codes returned by mainframe. 
	 */
	private final static String MF_SUCCESSFUL = "00";

	// defect 10462 
	/**
	 * Connection error to mainfarme
	 */
	private final static String MFACCESS_ERROR = "MfAccess Error: ";
	// end defect 10462 

	/**
	 * MULTIPLE_REG_INDI and JNK_INDI for MfInterfaceVersionCode
	 */
	// defect 11045
	// Reference MfbaTitleX for MultRegIndi
	private final static int MULTIPLE_REG_INDI_POSITION_T =  
		MfbaTitleT.MULTIPLE_REG_INDI_OFFSET;
	
	private final static int MULTIPLE_REG_INDI_POSITION_U = 
		MfbaTitleU.MULTIPLE_REG_INDI_OFFSET;

	private final static int MULTIPLE_REG_INDI_POSITION_V = 
		MfbaTitleV.MULTIPLE_REG_INDI_OFFSET;
	
	/**
	 * DOCNO OFFSET per MF Version 
	 */
    // Reference MfbaTitleX for DocNo
	private final static int DOC_NO_OFFSET_T = MfbaTitleT.DOC_NO_OFFSET; 
	private final static int DOC_NO_OFFSET_U = MfbaTitleU.DOC_NO_OFFSET;
	private final static int DOC_NO_OFFSET_V = MfbaTitleV.DOC_NO_OFFSET;
	// end defect 11045
	
	private final static int DOC_NO_LENGTH = 17;

	//  JNK_INDI_POSITION is not defined in any MfbaXXXData class
	private final static int JNK_INDI_POSITION_T = 1423;
	private final static int JNK_INDI_POSITION_U = 1423;
	private final static int JNK_INDI_POSITION_V = 1423;


	/**
	 * Initialize the static fields.
	 */
	static {
		ssJavaGateway = SystemProperty.getMFJGateway();
		siJavaGatewayPort = SystemProperty.getMFJGatewayPort();
		ssMFTimeout = SystemProperty.getMFTimeout();
		csMFDBName = SystemProperty.getMFDBName();
		ssMFTransactionVersion =
			SystemProperty.getMFInterfaceVersionCode();
		ssMFJGate = SystemProperty.getMFJGate();
		ssMFServer = SystemProperty.getMFServer();
		siMFDebugMode = SystemProperty.getMFDebug();
		ssServerId = System.getProperty(SERVER_ID_SYSTEM_PROPERTY_NAME);
	}

	// Identifiers for Mainframe Programs
	/**
	 * Retrieve Vehicle Info by DocNo.
	 * 
	 * @see #retrieveVehicleFromActiveInactive() 
	 * @see #retrieveVehicleFromArchive()
	 */
	public static String R01 = "R01";

	/**
	 * Retrieve PermitData by: 
	 *  - PermitNo 
	 *  - VIN
	 *  - CustLstName
	 *  - CustBsnName
	 *  - PrmtIssuanceId 
	 */
	public static String R02 = "R02";

	/**
	 * Retrieve Vehicle Info by Hoops Reg Plate No. 
	 *  - 
	 * @see #retrieveVehicleFromActiveInactive()
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveVehicleFromVINA()
	 */
	public static String R03 = "R03";

	/**
	 * Retrieve Vehicle Info by VIN.
	 * 
	 * @see #retrieveVehicleFromActiveInactive()
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveVehicleFromVINA()
	 */
	public static String R04 = "R04";

	/**
	 * Retrieve Vehicle Info by SpecRegId.
	 * 
	 * @see #retrieveVehicleFromActiveInactive() 
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveMfRecordsFromActiveInactive()
	 */
	public static String R05 = "R05";

	/**
	 * Not Used.
	 */
	public static String R06 = "R06";

	/**
	 * Voids selected Transaction(s) from Mainframe.
	 * Uses DocNo for key.
	 * 
	 * @see #voidTransactions()
	 */
	public static String R07 = "R07";

	/**
	 * Retrieves the Mainframe Date and Time.
	 * This has been replaced by 
	 * 		Retrieves the Mainframe Special Plts Regis by HOOPSRegPltNo
	 * @see #retrieveVehicleFromActiveInactiveU()
	 * 
	 * @see #retrieveTimeDate()  (Note: deprecated)
	 */
	public static String R08 = "R08";

	/**
	 * Retrieve Title In Process.
	 * 
	 * @see #retrieveTitleInProcess()
	 */
	public static String R09 = "R09";

	/**
	 * Gets the Vina definition for a VIN.
	 * 
	 * <p>Note: Usage for retrieveVehicleFromVINA() is that R10 is only 
	 * used when the VIN is not already on our Mainframe.
	 * 
	 * <p>Note: retrieveVehicleFromVINAOnly() is not used.
	 * 
	 * <p>Note: retrieveVehicleFromVINA() and retrieveVehicleFromVINAOnly()
	 * has been replace with retrieveMfRecordsByVIN(GSD, TransactionId).
	 * 
	 * @see #retrieveVehicleFromVINA()
	 * @see #retrieveVehicleFromVINAOnly()
	 */
	public static String R10 = "R10";

	/**
	 * Get the number of Documents matching the request.
	 * 
	 * <p>This is not used in POS application.
	 * 
	 * @see #retrieveNumberofDocuments()
	 */
	public static String R12 = "R12";

	/**
	 * Retrieve Funds Payment Data list.
	 * 
	 * @see #retrieveFundsPaymentDataList()
	 */
	public static String R13 = "R13";

	/**
	 * Retrieve a Funds Remittance Record.
	 * 
	 * @see #retrieveFundsRemittanceRecord()
	 */
	public static String R14 = "R14";

	/**
	 * Retrieve a Funds Payment list.
	 * 
	 * @see #FundsPaymentDataList()
	 */
	public static String R15 = "R15";

	/**
	 * Retrieve Inventory Invoice Acknowledgement.
	 * 
	 * @see #retrieveInvoice()
	 */
	public static String R17 = "R17";

	/**
	 * Retrieve Owner.
	 * 
	 * @see #retrieveOwner()
	 */
	public static String R18 = "R18";

	/**
	 * Retrieve Vehicle Info From Spec-Regis and Canc_Plates (HoopsRegPltNo).
	 * 
	 * @see #retrieveVehicleBySpecialOwner()
	 */
	public static String R19 = "R19";

	/**
	 * Retrieve Vehicle Info From Spec-Regis and Canc_Plates (OwnerId).
	 * 
	 * @see #retrieveVehicleBySpecialOwner()
	 */
	public static String R20 = "R20";

	/**
	 * Retrieve Inventory Invoice Detail Records.
	 * 
	 * @see #retrieveInvoice()
	 */
	public static String R26 = "R26";

	/**
	 * Retrieve Junk Records.
	 * 
	 * <p>Note: retriveJunkRecords is not used.
	 * 
	 * @see #retrieveVehicleFromActiveInactive()
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveVehicleFromVINA()
	 * @see #retriveJunkRecords()
	 */
	public static String R28 = "R28";

	/**
	 * Sends Funds Update to Mainframe.
	 * 
	 * <p>Confused usage?  POS also uses to help resolve multi-regis 
	 * return if not using DocNo?  Must not be using this path!
	 * 
	 * @see #retrieveVehicleFromActiveInactive()
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveVehicleFromVINA()
	 * @see #sendFundsUpdateToMF()
	 */
	public static String R29 = "R29";

	/**
	 * Retrieves a Funds Payment list.
	 * 
	 * <p>Search by Reporting Date.
	 * 
	 * @see #retrieveFundsPaymentDataList()
	 */
	public static String R30 = "R30";

	/**
	 * Retrieves a Funds Payment list.
	 * 
	 * <p>Search by TraceNo.
	 * 
	 * @see #retrieveFundsPaymentDataList()
	 */
	public static String R31 = "R31";

	/**
	 * Retrieves a Funds Payment list.
	 * 
	 * <p>Search by Payment Date.
	 * 
	 * @see #retrieveFundsPaymentDataList()
	 */
	public static String R32 = "R32";

	/**
	 * Retrieves Vehicle Info by DocNo.  Used to resolve multiple
	 * Regis records.
	 * 
	 * @see #retrieveVehicleFromActiveInactive()
	 * @see #retrieveVehicleFromArchive()
	 * @see #retrieveVehicleFromVINA()
	 */
	public static String R33 = "R33";

	/**
	 * Sends Transaction Data to Mainframe.
	 * "SendTrans".
	 * 
	 * @see #sendTransaction()
	 */
	public static String R77 = "R77";

	/**
	 * R99 is used by Mainframe to test new code without impacting 
	 * other users.  This replaces the transaction being tested by 
	 * using a special testing interface (MfAccessTest).
	 */
	public static String R99 = "R99";

	/**
	 * MfAccess constructor. This is the default constructor used by all
	 * the server side business layer methods. 
	 */
	public MfAccess()
	{
		super();
		// set up client name as the server name
		MF_NET_NAME = "UNKNOWN";
		try
		{
			MF_NET_NAME = InetAddress.getLocalHost().getHostName();
		}
		catch (java.net.UnknownHostException aeEx)
		{
			// Goes to server std.out
			System.out.println(
				"UnknownHost Exception in MainFrameAccess");
		}
	}
	/**
	 * MfAccess constructor. This is the default constructor used by all
	 * the server side business layer methods. 
	 */
	public MfAccess(String asClientName)
	{
		super();
		MF_NET_NAME = asClientName;
	}
	/**
	 * Constructor called by the Mf Access Test tool. Since the system
	 * properties are not available when the MfAceess class is created
	 * by the test tool, need to provide all necessary parameters. 
	 * This method should not be used by the server side business 
	 * layer classes of RTS II. 
	 * 
	 * @param lsJavaGateway java.lang.String
	 * @param liJavaGatewayPort int
	 * @param lsMFTimeout java.lang.String
	 * @param lsMFDBName java.lang.String
	 * @param lsMFTransactionVersion java.lang.String
	 * @param lsMFServer java.lang.String
	 * @param lsMFJGate java.lang.String
	 * @param liMFDegbugMode int
	 * @param asClientName String
	 */
	public MfAccess(
		String asJavaGateway,
		int aiJavaGatewayPort,
		String asMFTimeout,
		String asMFDBName,
		String asMFTransactionVersion,
		String asMFServer,
		String asMFJGate,
		int aiMFDegbugMode,
		String asClientName)
	{
		super();
		ssJavaGateway = asJavaGateway;
		siJavaGatewayPort = aiJavaGatewayPort;
		ssMFTimeout = asMFTimeout;
		csMFDBName = asMFDBName;
		ssMFTransactionVersion = asMFTransactionVersion;
		ssMFServer = asMFServer;
		ssMFJGate = asMFJGate;
		siMFDebugMode = aiMFDegbugMode;
		MF_NET_NAME = asClientName;
	}

	/**
	 * This is a method used for TESTING purposes only. To avoid using
	 * the Connection Pool, this method is called directly from the
	 * the transaction methods in MfAccess. Returns a JavaGateway object 
	 * from as a connection object. 
	 * <p> If there is a problem in getting a connection, throws an
	 * <code>RTSException</code> with message type set as 
	 * <code>MF_DOWN</code>
	 * 
	 * @throws RTSException MF_DOWN RTSException
	 * @return com.ibm.ctg.client.JavaGateway
	 */
	private JavaGateway getConnection() throws RTSException
	{
		// defect 10462 
		// Streamline 
		JavaGateway laJavaGate = null;

		try
		{
			// Get MF Connection
			// JavaGateway gate = new JavaGateway("144.45.192.202",4080);
			laJavaGate =
				new JavaGateway(ssJavaGateway, siJavaGatewayPort);

			// Ensure the Protocol is TCP
			if (!laJavaGate.getProtocol().equalsIgnoreCase("TCP"))
			{
				throw new NullPointerException(
					"Protocol - " + laJavaGate.getProtocol());
			}
			return laJavaGate;
		}
		catch (IOException aeIOException)
		{
			// Create/Throw MFDown Exception 
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN, aeIOException);
			leRTSEx.setMessage(RTSException.MF_DOWN_MSG);
			leRTSEx.printStackTrace();
			throw leRTSEx;
		}
		catch (NullPointerException aeNPEx)
		{
			String lsErr = CommonConstant.STR_SPACE_EMPTY;

			if (laJavaGate != null)
			{
				lsErr =
					MFACCESS_ERROR
						+ "Connection to JavaGateway failed"
						+ ": MF_NET_NAME - "
						+ MF_NET_NAME
						+ ": JavaGate Protocol - "
						+ laJavaGate.getProtocol()
						+ ": JavaGate Port - "
						+ laJavaGate.getPort()
						+ ": JavaGate ProtocolProperties - "
						+ laJavaGate.getProtocolProperties().toString()
						+ ": JavaGateway var. "
						+ ssJavaGateway
						+ " JavaGatewayPort var. "
						+ siJavaGatewayPort;
			}
			else
			{
				lsErr =
					"Connection to JavaGateway failed "
						+ ": MF_NET_NAME - "
						+ MF_NET_NAME
						+ " JavaGate Protocol - null"
						+ " JavaGate Port - null"
						+ " JavaGate ProtocolProperties - null"
						+ " JavaGateway var. "
						+ ssJavaGateway
						+ " JavaGatewayPort var. "
						+ siJavaGatewayPort;
			}
			System.err.println(lsErr);

			// throw aeNPEx; 

			// Create/Throw MFDown Exception 
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN, aeNPEx);
			leRTSEx.setMessage(RTSException.MF_DOWN_MSG);
			leRTSEx.printStackTrace();
			throw leRTSEx;
			// end defect 10462 
		}
	}

	/**
	 * Communicates with the Mainframe with given arguments and returns
	 * the response from mainframe to the calling method.
	 * <p>MFKeys is defined as
	 * 
	 * @param lsTransactionId  String   Mainframe TransactionId
	 * @param lsMfKeys         String   Input Buffer
	 * @throws RTSException    MF_DOWN RTSException
	 * @return String
	 */
	private String getMfResponse(
		String asTransactionId,
		String asMfKeys)
		throws RTSException
	{
		// defect 10462 
		int liErrorCode = MF_UNKNOWN_ERR_CD;
		RTSDate laRTSReqDate = new RTSDate();
		int liSuccessfulIndi = 0;
		boolean lbECIRequestSuccess = false;
		boolean lbConnectionFlowSuccess = false;
		byte[] larrCommArea = new byte[1];
		byte[] larrMfHeader = new byte[1];

		// defect 10637 
		RTSDate laSetupHdrReqDate = null;
		RTSDate laSetupHdrRespDate = null;
		RTSDate laConnectCloseRespDate = null;
		// end defect 10637

		RTSDate laConnectReqDate = null;
		RTSDate laConnectRespDate = null;

		RTSDate laConnectCheckReqDate = null;
		RTSDate laConnectCheckRespDate = null;

		RTSDate laFlowReqDate = null;
		RTSDate laFlowRespDate = null;
		RTSDate laConnectCloseReqDate = null;
		int liConnectRetry = 0;
		// end defect 10462 

		// Create the header in APPC header record 
		APPCHeader appcHeader = new APPCHeader();

		// Set the CICS Transaction Id with the proper version
		StringBuffer laTransId = new StringBuffer(asTransactionId);
		laTransId.insert(TRANS_VERSION_OFFSET, ssMFTransactionVersion);
		asTransactionId = laTransId.toString();

		// Input from POS to mainframe
		String lsBufferDescArea = CommonConstant.STR_SPACE_EMPTY;

		// defect 10462
		String lsErrorAppend =
			MF_NET_NAME
				+ SEARCH_CRITERIA
				+ asMfKeys
				+ ": TransactionId - "
				+ asTransactionId;

		String lsMfKeysLength = new String();

		String lsLastAction = "ResizeStringToLength";
		try
		{
			lsMfKeysLength =
				resizeStringtoLength(
					Integer.toString(asMfKeys.length()),
					appcHeader.getHDINTLEN(),
					'0',
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING);
		}
		catch (NullPointerException aeNPEx)
		{
			System.err.println(
				MFACCESS_ERROR
					+ lsLastAction
					+ " - NullPointerException. "
					+ lsErrorAppend);
			throw aeNPEx;
		}

		// Try around all calls; Log to DB2 included in finally
		try
		{
			try
			{
				lsLastAction = "Set Mainframe Header";

				// defect 10637 
				laSetupHdrReqDate = new RTSDate();
				// end defect 10637 

				appcHeader.setMainframeHeader(
					asTransactionId,
					getTsqName(),
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					MfAccess.ssMFTimeout,
					MfAccess.FLAG_YES,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					getNetName(),
					lsMfKeysLength,
					csMFDBName,
					lsBufferDescArea);
			}
			catch (NullPointerException aeNPEx)
			{
				liErrorCode = APPC_HDR_ERR_CD;

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - NullPointerException. "
						+ lsErrorAppend);

				throw aeNPEx;
			}
			catch (Exception aeEx)
			{
				liErrorCode = APPC_HDR_ERR_CD;

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - Exception. "
						+ lsErrorAppend);

				throw aeEx;
			}

			// defect 10637 
			laSetupHdrRespDate = new RTSDate();
			// end defect 10637 

			lsLastAction = "Create Comm Area Array";
			larrCommArea = new byte[COMM_AREA_LENGTH];

			lsLastAction = "Create MF Header Array";
			larrMfHeader = appcHeader.getAPPCHeaderRecord();

			lsLastAction = "Setup Comm Area";
			try
			{
				larrCommArea =
					setupCommArea(larrCommArea, larrMfHeader, asMfKeys);
			}
			catch (NullPointerException aeNPEx)
			{
				liErrorCode = SETUP_COMM_AREA_ERR_CD;

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - NullPointerException. "
						+ lsErrorAppend
						+ ": CommArea var. - "
						+ larrCommArea.toString()
						+ ": MfHeader var - "
						+ larrMfHeader.toString());
				throw aeNPEx;
			}
			catch (Exception aeEx)
			{
				liErrorCode = SETUP_COMM_AREA_ERR_CD;

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - Exception. "
						+ lsErrorAppend
						+ ": CommArea var. - "
						+ larrCommArea.toString()
						+ ": MfHeader var - "
						+ larrMfHeader.toString());
				throw aeEx;
			}

			if (siMFDebugMode == 1)
			{
				lsLastAction = "Print Request (Input) in Debug";
				printTrace("INPUT:", larrCommArea);
			}

			lsLastAction = "Test MF Version Cd";

			// TODO look at a better to do this check
			// Current MF Versions in use: T, U, V
			if (!(SystemProperty
				.getMFInterfaceVersionCode()
				.equals(ApplicationControlConstants.SC_MFA_VERSION_T)
				|| SystemProperty.getMFInterfaceVersionCode().equals(
					ApplicationControlConstants.SC_MFA_VERSION_V)
				|| SystemProperty.getMFInterfaceVersionCode().equals(
					ApplicationControlConstants.SC_MFA_VERSION_U)))
			{
				liErrorCode = INCORRECT_MF_VERSION_ERR_CD;

				String lsError =
					MFACCESS_ERROR
						+ lsLastAction
						+ SystemProperty.getMFInterfaceVersionCode()
						+ MessageConstants.SE_MF_VERSION_NOT_IMPLEMENTED;

				Log.write(Log.SQL_EXCP, this, lsError);

				System.err.println(lsError);
				throw new IOException();
			}

			lsLastAction = "Setup ECIRequest";

			// Create the request object to send to JavaGate
			ECIRequest laRequest = getRequest(larrCommArea);
			lbECIRequestSuccess = true;

			String lsMfResponse = CommonConstant.STR_SPACE_ONE;

			// Get a connection to the mainframe
			// Retry when unsuccessful
			lsLastAction = "1st getConnection";
			try
			{
				laConnectReqDate = new RTSDate();
				caConnection = getConnection();
				laConnectRespDate = new RTSDate();
			}
			catch (RTSException aeRTSEx)
			{
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - RTSException. "
						+ lsErrorAppend);

				aeRTSEx.printStackTrace();
			}

			lsLastAction = "Test Status of 1st getConnection attempt";

			laConnectCheckReqDate = new RTSDate();

			if (caConnection == null || !caConnection.isOpen())
			{
				laConnectCheckRespDate = new RTSDate();

				String lsAppend =
					caConnection == null
						? " Connection is null."
						: " Connection is closed.";

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - Initial getConnection unsuccessful. Retrying. "
						+ lsAppend
						+ lsErrorAppend);

				// Retry getConnection
				lsLastAction = "2nd getConnection attempt";
				try
				{
					liConnectRetry = 1;
					laConnectReqDate = new RTSDate();
					caConnection = getConnection();
					laConnectRespDate = new RTSDate();
				}
				catch (RTSException aeRTSEx)
				{
					liErrorCode = RETRY_CONNECT_ERR_CD;

					System.err.println(
						MFACCESS_ERROR
							+ lsLastAction
							+ " - RTSException. "
							+ lsErrorAppend);

					aeRTSEx.printStackTrace();
					throw aeRTSEx;
				}
				lsLastAction = "Test 2nd getConnection attempt";

				if (caConnection == null || !caConnection.isOpen())
				{
					liErrorCode = RETRY_CONNECT_NOT_OPEN_ERR_CD;

					lsAppend =
						caConnection == null
							? " Connection is null. "
							: " Connection is closed. ";

					System.err.println(
						MFACCESS_ERROR
							+ lsLastAction
							+ " - "
							+ "2nd getConnection unsuccessful. "
							+ lsAppend
							+ lsErrorAppend);

					throw new IOException("2nd attempt failed to connect to server");
				}
			}
			if (laConnectCheckRespDate == null)
			{
				laConnectCheckRespDate = new RTSDate();
			}

			lsLastAction = "Connection.flow()";
			int liRtnCd = -1;
			try
			{
				laFlowReqDate = new RTSDate();
				liRtnCd = caConnection.flow(laRequest);
				laFlowRespDate = new RTSDate();
			}
			catch (NullPointerException aeNPEx)
			{
				liErrorCode = CONNECT_FLOW_ERR_CD;

				boolean lbConnectionNull = (caConnection == null);
				boolean lbConnectionClose =
					caConnection != null && !caConnection.isOpen();

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - NullPointerException. "
						+ "ConnectionClose\\Connection Null : "
						+ lbConnectionClose
						+ CommonConstant.STR_BACK_SLASH
						+ lbConnectionNull
						+ " "
						+ lsErrorAppend);

				throw aeNPEx;
			}
			catch (IOException aeIOEx)
			{
				liErrorCode = CONNECT_FLOW_ERR_CD;

				boolean lbConnectionNull = (caConnection == null);
				boolean lbConnectionClose =
					caConnection != null && !caConnection.isOpen();

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - IOException. "
						+ "ConnectionClose\\Connection Null : "
						+ lbConnectionClose
						+ CommonConstant.STR_BACK_SLASH
						+ lbConnectionNull
						+ " "
						+ lsErrorAppend);

				throw aeIOEx;
			}

			lsLastAction = "Test ECIRequest RC";
			if (liRtnCd != 0)
			{
				liErrorCode = CONNECT_FLOW_INVALID_RC_ERR_CD;
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - ECIRequest.flow return code unsuccessful: "
						+ String.valueOf(liRtnCd)
						+ ". "
						+ lsErrorAppend);

				Log.write(
					Log.SQL_EXCP,
					this,
					"(MfAccess) ECIRequest.flow return code unsuccessful: "
						+ String.valueOf(liRtnCd));

				RTSException leRTSEx =
					new RTSException(RTSException.MF_DOWN);
				leRTSEx.setMessage(String.valueOf(liRtnCd));
				throw leRTSEx;
			}
			lbConnectionFlowSuccess = true;

			lsLastAction = "Test Connection status before close";
			if (caConnection == null || !caConnection.isOpen())
			{
				String lsAppend =
					caConnection == null
						? " Connection is null."
						: " Connection is closed.";

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - "
						+ lsAppend
						+ "Premature Close. "
						+ lsErrorAppend);
			}
			else
			{
				try
				{
					lsLastAction =
						"Test if Connection is open before close.";

					laConnectCloseReqDate = new RTSDate();

					if (caConnection.isOpen())
					{
						lsLastAction = "Close Connection. ";
						caConnection.close();

						// defect 10637 
						laConnectCloseRespDate = new RTSDate();
						// end defect 10637 
					}
				}
				catch (IOException aeIOEx)
				{
					System.err.println(
						MFACCESS_ERROR
							+ lsLastAction
							+ " - Connection close failed. "
							+ lsErrorAppend);
					// Do not throw Exception
				}
			}

			lsLastAction = "New String from CommArea";
			lsMfResponse = new String(laRequest.Commarea);

			//If ECIRequest response code is not 0 or if Test is greater than 2,
			// then throw MF down
			lsLastAction = "Test ECIRequest RC\\CICS RC";

			if (!(laRequest.getRc() == 0 && laRequest.getCicsRc() == 0)
				|| siTest > 2)
			{
				liErrorCode = ECI_REQUEST_ERR_CD;
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - ECIRequest Code\\Text - "
						+ laRequest.getRc()
						+ CommonConstant.STR_BACK_SLASH
						+ laRequest.getRcString()
						+ ": "
						+ CICS_ERROR_CD
						+ laRequest.getCicsRc()
						+ laRequest.getCicsRcString()
						+ ". "
						+ lsErrorAppend);

				//	Add Client Name, MfKeys (GSD) and CICS Error Code
				//	to log. Note: MfKeys containg type of search and key
				Log.write(
					Log.SQL_EXCP,
					this,
					"(MfAccess) ECIRequest to JavaGateway Failed "
						+ CommonConstant.STR_SPACE_ONE
						+ "ECIRequest Code\\Text - "
						+ laRequest.getRc()
						+ CommonConstant.STR_BACK_SLASH
						+ laRequest.getRcString()
						+ ": MF_NET_NAME - "
						+ MF_NET_NAME
						+ SEARCH_CRITERIA
						+ asMfKeys
						+ CICS_ERROR_CD
						+ laRequest.getCicsRc()
						+ laRequest.getCicsRcString());

				siTest++;
				RTSException leRTSEx =
					new RTSException(RTSException.MF_DOWN);
				leRTSEx.setMessage(String.valueOf(laRequest.getRc()));
				throw leRTSEx;
			}
			siTest = 0;

			lsLastAction = "Check RTSJGate Error";
			try
			{
				checkRTSJGateErr(lsMfResponse);
			}
			catch (IOException aeIOException)
			{
				liErrorCode = RTS_JGATE_ERR_CD;

				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - I/O Exception. "
						+ lsErrorAppend);

				// Create/throw MF Down RTSException 
				RTSException laRTSE =
					new RTSException(
						RTSException.MF_DOWN_MSG,
						aeIOException);
				laRTSE.setMsgType(RTSException.MF_DOWN);

				laRTSE.printStackTrace();
				throw laRTSE;
			}

			if (siMFDebugMode == 1)
			{
				lsLastAction = "Print Response (Output) in Debug";
				printTrace("OUTPUT:", lsMfResponse);
			}

			lsLastAction = "Check MF Error Code";

			String lsMfErrCodeReturned =
				lsMfResponse.substring(
					MF_ERROR_TYPE_CD_OFFSET,
					MF_ERROR_TYPE_CD_OFFSET + MF_ERROR_TYPE_CD_LENGTH);

			if (lsMfErrCodeReturned.equals(MF_LOGIC_ERROR)
				|| lsMfErrCodeReturned.equals(MF_CICS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_ADABAS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_PROGRAM_ERROR))
			{
				liErrorCode = Integer.parseInt(lsMfErrCodeReturned);

				String lsTransAMDate =
					Integer.toString((new RTSDate()).getAMDate());
				String lsTransAMTime = (new RTSDate()).getTime();
				handleMfError(
					lsMfResponse,
					0,
					0,
					lsTransAMDate,
					lsTransAMTime);
				throw new IOException();
			}

			liErrorCode = 0;
			liSuccessfulIndi = 1;
			return lsMfResponse;
		}
		catch (IOException aeIOException)
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.MF_DOWN_MSG,
					aeIOException);

			leRTSEx.setMsgType(RTSException.MF_DOWN);

			if (liErrorCode == MF_UNKNOWN_ERR_CD)
			{
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - IOException exit point ECIRequest\\"
						+ "Connection Flow "
						+ lbECIRequestSuccess
						+ CommonConstant.STR_BACK_SLASH
						+ lbConnectionFlowSuccess
						+ ". "
						+ lsErrorAppend);
				liErrorCode = MF_IO_EXCEPTION_ERR_CD;
				leRTSEx.printStackTrace();
			}
			throw leRTSEx;
		}
		// defect 9403
		// Print the error to SysErr and then throw the exception
		catch (NullPointerException aeNPEx)
		{
			// Throw MF Down RTSException  
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN_MSG, aeNPEx);
			leRTSEx.setMsgType(RTSException.MF_DOWN);

			if (liErrorCode == MF_UNKNOWN_ERR_CD)
			{
				liErrorCode = MF_NULL_PTR_ERR_CD;
				leRTSEx.printStackTrace();
			}

			// Check if ECI or Connection Flow failed (in that order) 
			if (!lbECIRequestSuccess)
			{
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - Request sent to CTGJavaGate failed "
						+ lsErrorAppend
						+ ": CommArea var. - "
						+ larrCommArea.toString());
			}
			else if (!lbConnectionFlowSuccess)
			{
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - Connection.flow failed "
						+ lsErrorAppend);
			}
			else if (liErrorCode == MF_NULL_PTR_ERR_CD)
			{
				System.err.println(
					MFACCESS_ERROR
						+ lsLastAction
						+ " - NullPointerException "
						+ lsErrorAppend);

			}
			throw leRTSEx;
		}
		catch (Exception aeEx)
		{
			System.err.println(
				MFACCESS_ERROR
					+ lsLastAction
					+ " - Exception "
					+ lsErrorAppend);

			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN_MSG, aeEx);
			leRTSEx.setMsgType(RTSException.MF_DOWN);
			throw leRTSEx;
		}
		// end defect 10462 
		finally
		{
			// Make sure connection properly closed 
			if (caConnection != null)
			{
				if (caConnection.isOpen())
				{
					try
					{
						caConnection.close();
					}
					catch (IOException aeIOEx)
					{
						aeIOEx.printStackTrace();
					}
				}
			}
			// null out caConnection to make sure
			caConnection = null;

			// defect 10637 
			RTSDate laRTSRespDate = new RTSDate();
			// end defect 10637 

			// defect 10462 
			if (SystemProperty.isDB2LogMFReq() && caMFReqData != null)
			{
				if (SystemProperty.getDB2LogMFReqCd()
					== SystemProperty.DB2_LOGGING_ALL_MF_REQ
					|| liSuccessfulIndi == 0)
				{
					caMFReqData.setCICSTransId(asTransactionId);
					caMFReqData.setReqTimeStmp(laRTSReqDate);

					// defect 10637 
					caMFReqData.setRespTimestmp(laRTSRespDate);
					// end defect 10637 

					caMFReqData.setSuccessfulIndi(liSuccessfulIndi);
					caMFReqData.setRetryNo(liConnectRetry);
					caMFReqData.setErrMsgCd(liErrorCode);
					logDB2MFReq();
				}
			}

			// defect 10637 
			boolean lbLogLongResp = false;

			if (laRTSRespDate != null && laRTSReqDate != null)
			{
				lbLogLongResp =
					laRTSReqDate.add(RTSDate.SECOND, 4).compareTo(
						laRTSRespDate)
						< 0;
			}

			if (liErrorCode != 0 || lbLogLongResp)
			{
				String lsSep = CommonConstant.SYSTEM_LINE_SEPARATOR;

				String lsReqDate =
					laRTSReqDate == null
						? "null"
						: laRTSReqDate.getDB2Date() + lsSep;

				String lsRespDate =
					laRTSRespDate == null
						? "null"
						: laRTSRespDate.getDB2Date() + lsSep;

				String lsSetupHdrReqDate =
					laSetupHdrReqDate == null
						? "null"
						: laSetupHdrReqDate.getDB2Date() + lsSep;
				String lsSetupHdrRespDate =
					laSetupHdrRespDate == null
						? "null"
						: laSetupHdrRespDate.getDB2Date() + lsSep;

				String lsConnReqDate =
					laConnectReqDate == null
						? "null"
						: laConnectReqDate.getDB2Date() + lsSep;

				String lsConnRespDate =
					laConnectRespDate == null
						? "null"
						: laConnectReqDate.getDB2Date() + lsSep;

				String lsConnChkReqDate =
					laConnectCheckReqDate == null
						? "null"
						: laConnectCheckReqDate.getDB2Date() + lsSep;

				String lsConnChkRespDate =
					laConnectCheckRespDate == null
						? "null"
						: laConnectCheckRespDate.getDB2Date() + lsSep;

				String lsFlowReqDate =
					laFlowReqDate == null
						? "null"
						: laFlowReqDate.getDB2Date() + lsSep;

				String lsFlowRespDate =
					laFlowRespDate == null
						? "null"
						: laFlowRespDate.getDB2Date() + lsSep;

				String lsCloseReqDate =
					laConnectCloseReqDate == null
						? "null"
						: laConnectCloseReqDate.getDB2Date() + lsSep;

				String lsCloseRespDate =
					laConnectCloseRespDate == null
						? "null"
						: laConnectCloseRespDate.getDB2Date() + lsSep;

				String lsBuff = UtilityMethods.addPadding(" ", 58, " ");
				String lsError =
					lsBuff
						+ "    EntryTime: "
						+ lsReqDate
						+ lsBuff
						+ "  SetupHdrReq: "
						+ lsSetupHdrReqDate
						+ lsBuff
						+ " SetupHdrResp: "
						+ lsSetupHdrRespDate
						+ lsBuff
						+ "   ConnectReq: "
						+ lsConnReqDate
						+ lsBuff
						+ "  ConnectResp: "
						+ lsConnRespDate
						+ lsBuff
						+ "   ConnChkReq: "
						+ lsConnChkReqDate
						+ lsBuff
						+ "  ConnChkResp: "
						+ lsConnChkRespDate
						+ lsBuff
						+ "      FlowReq: "
						+ lsFlowReqDate
						+ lsBuff
						+ "     FlowResp: "
						+ lsFlowRespDate
						+ lsBuff
						+ "     CloseReq: "
						+ lsCloseReqDate
						+ lsBuff
						+ "    CloseResp: "
						+ lsCloseRespDate
						+ lsBuff
						+ "     ExitTime: "
						+ lsRespDate;

				if (liErrorCode != 0)
				{
					lsError =
						MFACCESS_ERROR
							+ lsLastAction
							+ lsSep
							+ lsError
							+ " -- "
							+ lsErrorAppend;
				}
				else
				{
					lsError = "LONG RESPONSE TIME: " + lsSep + lsError;
				}

				System.err.println(lsError);
				// defect 10637 
			}
			// end defect 10462 
		}
		// end defect 6701
	}

	/**
	 * Returns the temporary storage queue name suffix. Part of this 
	 * suffix is an integer from 0 to 999 if the ServerId has a 
	 * length of one.  See the Special Notes below for other 
	 * considerations.
	 * 
	 * <p>Every time a request is sent to the mainframe, a new suffix 
	 * is created.
	 * 
	 * <p>Special Notes:
	 * <ul>
	 * <li>If ServerId has a length of two, the number can only have
	 * two positions.
	 * <li>If ServerId has a length of three, the number can only have
	 * one position.
	 * <li>If ServerId has a length of four, the number is no longer
	 * meaningful.
	 * <li>If ServerId is longer than four positions, the result will be 
	 * truncated back to just four positions later in the process.
	 * <eul> 
	 * 
	 * @return String
	 */
	public synchronized static String getTsqNumber()
	{
		// defect 7937
		// Set the suffix number for the next tsq name
		// Note that if the ServerId length is more than 3
		// the queue number becomes meaningless. 
		if ((siTSQNumber == 999 && ssServerId.length() == 1)
			|| (siTSQNumber == 99 && ssServerId.length() == 2)
			|| (siTSQNumber == 9 && ssServerId.length() == 3)
			|| (ssServerId.length() > 3))
		{
			// end defect 7937
			siTSQNumber = 1;
		}
		else
		{
			// normal increment of tsq number
			siTSQNumber = siTSQNumber + 1;
		}
		StringBuffer lsQueueNAme =
			new StringBuffer(String.valueOf(siTSQNumber));
		// defect 7937
		// pad the queue number based on 4 less the ServerId length
		// the end result will be something like R001 or RR01.
		while (lsQueueNAme.length() < 4 - ssServerId.length())
		{
			// end defect 7937
			lsQueueNAme.insert(0, "0");
		}
		return lsQueueNAme.toString();
	}
	/**
	 * This is  FOR TESTING PURPOSES ONLY.
	 * <p>USAGE: For using this main method for testing purposes, 
	 * please comment out the <code>initialized=true</code> in 
	 * <code>SystemProperty.initialize()</code>.
	 * This is required because, the static code in 
	 * <code>MfAccess</code> class initialises the 
	 * <code>SystemProperty</code> class. However, since at that time
	 * the env is not set up to be server, <code>SystemProperty</code>
	 * class does not know which .cfg file to load. So in this method,
	 * the server env is set up and the properties are loaded. 
	 * To reinitialize the <code>SystemProperty</code> class
	 * we need to comment the above statement. 
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		// sets the env to be server 
		// (for SystemProperty to load server.cfg) 
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		ssJavaGateway = SystemProperty.getMFJGateway();
		siJavaGatewayPort = SystemProperty.getMFJGatewayPort();
		ssMFTimeout = SystemProperty.getMFTimeout();
		csMFDBName = SystemProperty.getMFDBName();
		ssMFTransactionVersion =
			SystemProperty.getMFInterfaceVersionCode();
		ssMFJGate = SystemProperty.getMFJGate();
		ssMFServer = SystemProperty.getMFServer();
		siMFDebugMode = SystemProperty.getMFDebug();
		// set up client name
		try
		{
			InetAddress.getLocalHost().getHostName();
		}
		catch (java.net.UnknownHostException aeEx)
		{
			System.out.println(
				"UnknownHost Exception in MainFrameAccess");
		}

		//THIS PART FOR ARCHIVE
		GeneralSearchData gsd = new GeneralSearchData();
		//gsd.setKey1(CommonConstant.DOC_NO); 
		//gsd.setKey2("00000000006298264");
		//gsd.setKey2("00000000000010666");
		//gsd.setKey2("00000000000006029");
		gsd.setKey1(CommonConstant.REG_PLATE_NO);
		//gsd.setKey2("3ZL172");
		//gsd.setKey2(CommonConstant.STR_SPACE_EMPTY);
		//gsd.setKey1("VIN");
		//gsd.setKey2(CommonConstant.STR_SPACE_EMPTY); 
		//gsd.setIntKey1(1);
		//gsd.setKey2("dl45bj");
		//gsd.setKey2("tx243a");
		gsd.setKey2("dv2554");
		//END OF ARCHIVE
		//
		//THIS FOLLOWING KEY FOR PARTIAL FROM ARCHIVE
		//gsd.setKey2("2AF183");
		//VehicleInquiryData vid = mfa.retrieveVehicleFromArchive(gsd);        	
		//END OF PARTIAL
		//test for FUNDS PAYMENT 
		//search with FundsReport date without rptngdate
		//search with Check no
		//End of test for FUNDS PAYMENT 
		//Test for TIME AND DATE
		//test for VINA
		//End of test for SPCLOWNR
		//test for INVOICE ACK
		//End of Test for TITLE IN PROCESS
		//End of test for LOGTRAN
	}
	/**
	 * Return DocNo Key
	 * Determine by SC_MFA_VERSION
	 * 
	 * @param asMfTtlRegResponse   String - Buffer string from MF
	 * @return String
	 */
	public static String setDocNoKey(String asMfTtlRegResponse)
	{
		String lsMfTtlRegResponse = asMfTtlRegResponse;
		String lsKey = CommonConstant.STR_SPACE_EMPTY;

		// V version - Current
		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lsKey =
				lsMfTtlRegResponse.substring(
					DOC_NO_OFFSET_V,
					DOC_NO_OFFSET_V + DOC_NO_LENGTH);
		}
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			lsKey =
				lsMfTtlRegResponse.substring(
					DOC_NO_OFFSET_T,
					DOC_NO_OFFSET_T + DOC_NO_LENGTH);
		}
		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lsKey =
				lsMfTtlRegResponse.substring(
					DOC_NO_OFFSET_U,
					DOC_NO_OFFSET_U + DOC_NO_LENGTH);
		}

		//	Invalid CICS Version
		else
		{
			// TODO Error handling for MFVersion 
		}

		return lsKey;
	}
	// end defect 9403
	/**
	 * Holds the connection to the CICS transaction gateway
	 */
	private com.ibm.ctg.client.JavaGateway caConnection;

	// defect 10462 
	//	/**
	//	 * Access programs from Vehicle Inquiry class
	//	 */
	//	VehicleInquiry caVehInq = new VehicleInquiry();
	// end defect 10462 

	/**
	 * The mainframe error string returned from mainframe. 
	 */
	private String csMFError = CommonConstant.STR_SPACE_EMPTY;
	/**
	 * Temporary Storage Queue name. This is the queue on the mainframe 
	 * javagate where the request is stored before being passed to 
	 * the mainframe.
	 */
	private java.lang.String csTSQName;
	/**
	 * Name to identify the origination of a mainframe retrieve/update
	 * request. 
	 */
	private String MF_NET_NAME = "MFACCESS";
	int siTest = 0;

	/******************************************************************/
	/**
	 * Remove header from MfResponse.
	 * Return vehicle inquiry data
	 * 
	 * @param asMfResponse	String - Header and Vehicle data
	 * @return String
	 */
	public String getMfTtlRegResponse(String asMfResponse)
	{
		APPCHeader laAppcHeader = new APPCHeader();
		final int liHEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		//Pick the output length from commarea header
		//		int liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
		//		int liOutputLengthLength = laAppcHeader.getHDOUTLEN();
		int liOutputLength = 0;

		// TTL/REG buffer (for all events - not just ttl/reg event)
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;

		if ((asMfResponse != null)
			&& !(asMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// defect 8983
			//	Get output length from APPcHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(asMfResponse);
			//			liOutputLength =
			//				Integer.parseInt(
			//					asMfResponse.substring(
			//						liOutputLengthOffset,
			//						liOutputLengthOffset + liOutputLengthLength));
			// end defect 8983

			//This is the result without the header	
			lsMfTtlRegResponse =
				asMfResponse.substring(
					liHEADER_LENGTH,
					liHEADER_LENGTH + liOutputLength);
		}

		return lsMfTtlRegResponse;
	}
	/**
	 * Return MultipleRegis or Junk Indi
	 * Determine by SC_MFA_VERSION
	 * boolean determines MultiRegis or junk offset 
	 * 
	 * @param asMfTtlRegResponse   String - Buffer string from MF
	 * @param aiOffSetValue		   int - offset value, MultRegis or Junk
	 * @return char
	 */
	public char getMultipleRegJunkIndi(
		String asMfTtlRegResponse,
		boolean abMultiRegis)
	{
		char lcMultipleRegJunkIndi = '0';
		String lsMfTtlRegResponse = asMfTtlRegResponse;

		// V version - Current
		if (SystemProperty
			.getMFInterfaceVersionCode()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			if (abMultiRegis)
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						MULTIPLE_REG_INDI_POSITION_V);
			}
			else
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						JNK_INDI_POSITION_V);
			}
		}
		// T version - Exempts/TERPS
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			if (abMultiRegis)
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						MULTIPLE_REG_INDI_POSITION_T);
			}
			else
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						JNK_INDI_POSITION_T);
			}
		}
		//	U version - Special Plates
		else if (
			SystemProperty.getMFInterfaceVersionCode().equals(
				ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			if (abMultiRegis)
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						MULTIPLE_REG_INDI_POSITION_U);
			}
			else
			{
				lcMultipleRegJunkIndi =
					getStringFromZonedDecimal(
						lsMfTtlRegResponse).charAt(
						JNK_INDI_POSITION_U);
			}
		}

		//	Invalid CICS Version
		else
		{
			// Turn off throws RTSException
			// Turn off throw RTSException until Client can resolve RTS 
			// Mainframe Down
			//
			//	Throw RTSException to set RTS Mainframe Down		
			//			throw new RTSException(
			//				RTSException.MF_DOWN,
			//				SystemProperty.getMFInterfaceVersionCode()
			//					+ MessageConstants.SE_MF_VERSION_NOT_IMPLEMENTED,
			//				MessageConstants.SF_MFACCESS_ERROR);
		}

		return lcMultipleRegJunkIndi;
	}
	/**
	 * Returns the netname of the client machine based on parameter 
	 * passed in constructor.
	 *
	 * @return String
	 */
	private String getNetName() throws RTSException
	{
		int liNETNAME_LENGTH = 8;
		char lchBufferChar = ' ';
		String lsNetName =
			resizeStringtoLength(
				MF_NET_NAME,
				liNETNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END);
		return lsNetName;
	}
	/**
	 * Sets the request object to be sent to the mainframe
	 * <p>The return from MainFrame calls are expected to be less 
	 * than 32000 bytes.  Thus we do not allow for extentions.
	 * 
	 * server name, program name and commarea set.
	 * @param aarrCommArea The CommArea  
	 * @return com.ibm.ctg.client.ECIRequest The request object with
	 */
	private ECIRequest getRequest(byte[] aarrCommArea)
	{
		String lsProgram = ssMFJGate;
		String lsServer = ssMFServer;
		// Create the request object to send to JavaGate
		ECIRequest laRequest = new ECIRequest();
		laRequest.Server = lsServer;
		laRequest.Userid = CommonConstant.STR_SPACE_EMPTY;
		laRequest.Password = CommonConstant.STR_SPACE_EMPTY;
		laRequest.Call_Type = ECIRequest.ECI_SYNC;
		//At present all mainframe calls are less than 32000 Bytes.
		//Hence there is no need to extend the request
		laRequest.Extend_Mode = ECIRequest.ECI_NO_EXTEND;
		laRequest.Program = lsProgram;
		laRequest.Commarea = aarrCommArea;
		laRequest.Commarea_Length = aarrCommArea.length;
		return laRequest;
	}
	/**
	 * This method takes care of the special character that appears
	 * at the end of a zoned decimal from Mainframe (in the TS Queue
	 * the zoned decimals have a last byte sign byte that shows up as
	 * a special character in the mainframe response from comm area). 
	 * 
	 * @param param String Zoned Decimal string that might 
	 * have special characters
	 * @return java.lang.String String that has no special characters
	 */
	public String getStringFromZonedDecimal(String lsMfZonedDecimal)
	{
		StringBuffer lsMfConvertedString = new StringBuffer();
		lsMfConvertedString.append(lsMfZonedDecimal);
		//convert positive zoned decimals 
		int liMfZonedDecimalLastCharPosition =
			lsMfZonedDecimal.length() - 1;
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== '{')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'0');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'A')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'1');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'B')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'2');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'C')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'3');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'D')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'4');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'E')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'5');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'F')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'6');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'G')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'7');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'H')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'8');
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'I')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'9');
		}
		//convert negative zoned decimal numbers		
		boolean lbIsNegative = false;
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== '}')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'0');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'J')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'1');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'K')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'2');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'L')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'3');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'M')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'4');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'N')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'5');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'O')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'6');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'P')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'7');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'Q')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'8');
			lbIsNegative = true;
		}
		if (lsMfConvertedString
			.charAt(liMfZonedDecimalLastCharPosition)
			== 'R')
		{
			lsMfConvertedString.setCharAt(
				liMfZonedDecimalLastCharPosition,
				'9');
			lbIsNegative = true;
		}
		//if the number is negative add the negative sign in front
		if (lbIsNegative)
		{
			lsMfConvertedString.insert(0, '-');
		}
		return lsMfConvertedString.toString();
	}
	/**
	 * Returns the tsqname. Constructs the queue name from the prefix 
	 * (<code>TSQX_NAME</code>), <code> Cluster ID </code> and the 
	 * suffix (<code>getTsqNumber</code>). 
	 * 
	 * @return csTSQName String
	 */
	public String getTsqName()
	{
		csTSQName = TSQX_NAME + ssServerId + getTsqNumber();
		return csTSQName;
	}
	/**
	 * Returns the mainframe string equivalent of a RTS Dollar value. 
	 * 
	 * @param aaValue Dollar Dollar to be sent to MF
	 * @param aiLength int length of the buffer for the dollar value 
	 * @param achBufferInt Character empty spaces
	 * @param lsBufferAddPos String position to add buffer chars in 
	 * the MF buffer
	 * @return java.lang.String
	 */
	public String getValueFromDollar(
		Dollar aaValue,
		int aiLength,
		char achBufferInt,
		String asBufferAddPos)
	{
		String lsResizedValueToSendToMF =
			CommonConstant.STR_SPACE_EMPTY;
		String lsDollarValue = aaValue.toString();
		StringBuffer laDollarBuff = new StringBuffer();
		if (lsDollarValue.indexOf("-") == -1)
		{
			int liDecimalPtPosition = lsDollarValue.indexOf(".");
			laDollarBuff = new StringBuffer(lsDollarValue);
			laDollarBuff.replace(
				liDecimalPtPosition,
				liDecimalPtPosition + 1,
				CommonConstant.STR_SPACE_EMPTY);
			lsResizedValueToSendToMF =
				resizeStringtoLength(
					laDollarBuff.toString(),
					aiLength,
					achBufferInt,
					asBufferAddPos);
		}
		else
		{
			//for negative dollars
			int liDecimalPtPosition = lsDollarValue.indexOf(".");
			int liMinusSignPosition = lsDollarValue.indexOf("-");
			laDollarBuff = new StringBuffer(lsDollarValue);
			laDollarBuff.replace(
				liDecimalPtPosition,
				liDecimalPtPosition + 1,
				CommonConstant.STR_SPACE_EMPTY);
			laDollarBuff.replace(
				liMinusSignPosition,
				liMinusSignPosition + 1,
				CommonConstant.STR_SPACE_EMPTY);
			//get the zoned Deciaml
			String lsZonedDecimal =
				setZonedDecimalFromNegativeNumber(
					laDollarBuff.toString());
			lsResizedValueToSendToMF =
				resizeStringtoLength(
					lsZonedDecimal,
					aiLength,
					achBufferInt,
					asBufferAddPos);
		}
		return lsResizedValueToSendToMF;
	}
	/**
	 * Logs the errors thrown by Mf in VSAM file. Should be used only
	 * by the mainframe update programs (for this method sets the error
	 * level explicitly). Mainframe queries should use 
	 * <code>logError</code> method. 
	 * 
	 * @param asMfResponse String
	 * @param aiOfcIssuanceNo int
	 * @param aiWSID int
	 * @param asTransAMDate String 
	 * @param asTransTime String 
	 */
	public void handleMfError(
		String asMfResponse,
		int aiOfcIssuanceNo,
		int aiWSID,
		String asTransAMDate,
		String asTransTime)
	{
		if (SystemProperty.isVSAMLogMFErr())
		{
			//Offsets for MF Update error codes
			// defect 10244
			//	Move variables to class level
			//		final int liMF_ERROR_CODE_OFFSET = 132;
			//		final int liMF_ERROR_CODE_LENGTH = 2;
			//		final int liMF_ERROR_MSG_OFFSET = 148;
			//		final int liMF_ERROR_MSG_LENGTH = 89;
			// end defect 10244
			try
			{
				if (asMfResponse != null)
				{
					// defect 10121
					// Get the RTSJGate error code returned. 
					String lsJGateErrCodeReturned =
						asMfResponse.substring(
							JGATE_ERROR_FLAG_OFFSET,
							JGATE_ERROR_FLAG_OFFSET
								+ JGATE_ERROR_FLAG_LENGTH);
					// end defect 10121

					// Get CICS\COBOL error code
					// defect 10244
					//	Reference variables at class level
					String lsMfErrCodeReturned =
						asMfResponse.substring(
							MF_ERROR_TYPE_CD_OFFSET,
							MF_ERROR_TYPE_CD_OFFSET
								+ MF_ERROR_TYPE_CD_LENGTH);

					// Get error message
					String lsErrorMesg =
						asMfResponse.substring(
							MF_ERROR_TYPE_MSG_OFFSET,
							MF_ERROR_TYPE_MSG_OFFSET
								+ MF_ERROR_TYPE_MSG_LENGTH);
					// end defect 10244

					// defect 10121 
					//	add lsJGateErrCodeReturned = Y 						
					//if a Logic, Cics, ADABAS, Mf error or RTSJGate
					//	log it in VSAM
					if (lsMfErrCodeReturned.equals(MF_LOGIC_ERROR)
						|| lsMfErrCodeReturned.equals(MF_CICS_ERROR)
						|| lsMfErrCodeReturned.equals(MF_ADABAS_ERROR)
						|| lsMfErrCodeReturned.equals(MF_PROGRAM_ERROR)
						|| lsJGateErrCodeReturned.equals("Y"))
					{
						MFErrorData laMFErrorData = new MFErrorData();
						laMFErrorData.setComptCntyNo(aiOfcIssuanceNo);
						laMFErrorData.setErrLvl("FP");
						laMFErrorData.setErrMsg(lsErrorMesg);
						laMFErrorData.setErrNo(
							CommonConstant.STR_SPACE_EMPTY);
						laMFErrorData.setMfDate(new RTSDate());
						laMFErrorData.setMfTime(
							CommonConstant.STR_SPACE_EMPTY);
						laMFErrorData.setModuleName(
							asTransAMDate + asTransTime);
						laMFErrorData.setPCDate(
							(new RTSDate()).toString());
						laMFErrorData.setPCTime(
							(new RTSDate()).getClockTime());
						// Assign WsLUName to "MFAccess" 
						laMFErrorData.setWsLUName(MF_NET_NAME);
						laMFErrorData.setWsName(
							Integer.toString(aiWSID));
						logError(laMFErrorData);
					}
					// end defect 10121
				}
			}
			catch (Exception aeEx)
			{
				// write to WAS error out just to log it
				aeEx.printStackTrace();
			}
		}
	}

	/**
	 * Log DB2 MF Request 
	 * 
	 */
	private void logDB2MFReq()
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			MFRequest laMFReq = new MFRequest(laDBA);
			laDBA.beginTransaction();

			laMFReq.insMFRequest(caMFReqData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (Exception aeEx)
		{
			System.err.println("DBAccess Error");

			try
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (Exception aeEx1)
			{

			}
		}
		laDBA = null;
	}

	/**
	 * Logs an error to the TxDOT mainframe for use by the VTR help 
	 * desk personnel
	 * 
	 * @param aaMFErrorData MFErrorData Error data object. 
	 * @throws RTSException
	 */
	public void logError(MFErrorData aaMFErrorData) throws RTSException
	{
		//define the MF update string
		StringBuffer lsMfErrData = new StringBuffer();
		//define the buffers
		char lchBufferChar = ' ';
		//char lchBufferInt = '0';
		//define all lengths
		final int MF_DATE_LENGTH = 10;
		final int MF_TIME_LENGTH = 8;
		final int COMPT_CNTY_NO_LENGTH = 3;
		final int WS_NAME_LENGTH = 3;
		final int WS_LUNAME_LENGTH = 8;
		final int PCDATE_LENGTH = 10;
		final int PCTIME_LENGTH = 8;
		final int MODULE_NAME_LENGTH = 14;
		final int ERR_NO_LENGTH = 9;
		final int ERR_LVL_LENGTH = 2;
		final int ERR_MSG_LENGTH = 100;
		//code to extract values from Error Object and put in
		//update String
		if (aaMFErrorData.getMfDate() != null)
		{
			lsMfErrData.append(
				resizeStringtoLength(
					aaMFErrorData.getMfDate().toString(),
					MF_DATE_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		}
		else
		{
			lsMfErrData.append(
				resizeStringtoLength(
					CommonConstant.STR_SPACE_EMPTY,
					MF_DATE_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		}
		if (aaMFErrorData.getMfTime() != null)
		{
			lsMfErrData.append(
				resizeStringtoLength(
					aaMFErrorData.getMfTime(),
					MF_TIME_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		}
		else
		{
			lsMfErrData.append(
				resizeStringtoLength(
					CommonConstant.STR_SPACE_EMPTY,
					MF_TIME_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		}
		lsMfErrData.append(
			resizeStringtoLength(
				Integer.toString(aaMFErrorData.getComptCntyNo()),
				COMPT_CNTY_NO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getWsName(),
				WS_NAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getWsLUName(),
				WS_LUNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getPCDate().toString(),
				PCDATE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getPCTime(),
				PCTIME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getModuleName(),
				MODULE_NAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getErrNo(),
				ERR_NO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getErrLvl(),
				ERR_LVL_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		lsMfErrData.append(
			resizeStringtoLength(
				aaMFErrorData.getErrMsg(),
				ERR_MSG_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		//define all update inputs
		String lsUpdateInput = lsMfErrData.toString();
		String lsTransactionId = MfAccess.RSE;
		String lsBufferDescArea = CommonConstant.STR_SPACE_EMPTY;
		//send update to mainframe
		try
		{
			sendUpdateToMF(
				lsTransactionId,
				lsBufferDescArea,
				lsUpdateInput);
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN_MSG, aeEx);
			leRTSEx.setMsgType(RTSException.MF_DOWN);
			throw leRTSEx;
		}
	}
	/**
	 * 
	 * Version T and V format
	 * @deprecated
	 */
	//	private void oldMfKeyFormat()
	//	{
	//		lsMfKeys =
	//			setupMfKeys(
	//				0,
	//				lsKeyType,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				0,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				liComptCntyNo,
	//				lsFundsReportDate,
	//				lsRptngDate,
	//				lsDummyDate,
	//				lsDummyDate,
	//				0,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				0,
	//				0);		
	//	}
	/**
	 * Prints the trace of the mainframe transaction
	 * 
	 * @param asHeading String Specifies the heading 
	 * (could be request or reposnse) 
	 * @param aaPrintObject Object Object to be printed. If String,
	 * print output else print input (in the case of 
	 * <code>comm area</code>. 
	 */
	private void printTrace(String asHeading, Object aaPrintObject)
	{
		System.out.println(asHeading);
		String lsPrintObject = CommonConstant.STR_SPACE_EMPTY;
		String lsHeader = CommonConstant.STR_SPACE_EMPTY;
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		int liHeaderLength = 256;

		try
		{
			//create the file to write
			String lsMFTraceFileName = "MFTrace.txt";
			// create the file for SendTrans buffer area

			//The FileWriter constructor appends to the file by default 
			//	(boolean append in the
			//FileWriter constructor is set to true). 
			PrintWriter laMFTraceFile =
				new PrintWriter(
					new FileWriter(lsMFTraceFileName, true),
					true);

			// Seperate Header from SendTrans
			if (aaPrintObject instanceof byte[])
			{
				laMFTraceFile.println("THIS IS THE INPUT");
				lsPrintObject = new String((byte[]) aaPrintObject);
				//Pick the input length from commarea header
				APPCHeader laAppcHeader = new APPCHeader();
				int liInputLengthOffset = laAppcHeader.getHDINOFFSET();
				int liInputLengthLength = laAppcHeader.getHDINTLEN();
				int liInputLength =
					Integer.parseInt(
						lsPrintObject.substring(
							liInputLengthOffset,
							liInputLengthOffset + liInputLengthLength));
				if (lsPrintObject.length() >= liHeaderLength)
				{
					lsHeader =
						lsPrintObject.substring(0, liHeaderLength);
					lsMfResponse =
						lsPrintObject.substring(
							liHeaderLength,
							liHeaderLength + liInputLength);
				}
			}
			// Seperate Header from MfResponse
			else if (aaPrintObject instanceof String)
			{
				laMFTraceFile.println("THIS IS THE OUTPUT");
				lsPrintObject = (String) aaPrintObject;
				//Pick the output length from commarea header
				// defect 8983
				//	Get output length from APPcHeader
				//	Set number of records
				APPCHeader laAppcHeader = new APPCHeader();
				int liOutputLength =
					laAppcHeader.rtnMfOutputLength(lsPrintObject);
				//				int liOutputLengthOffset =
				//					laAppcHeader.getHDOUTOFFSET();
				//				int liOutputLengthLength = laAppcHeader.getHDOUTLEN();
				//				int liOutputLength =
				//					Integer.parseInt(
				//						lsPrintObject.substring(
				//							liOutputLengthOffset,
				//							liOutputLengthOffset
				//								+ liOutputLengthLength));
				// end defect 8983

				if (lsPrintObject.length() >= liHeaderLength)
				{
					lsHeader =
						lsPrintObject.substring(0, liHeaderLength);
					lsMfResponse =
						lsPrintObject.substring(
							liHeaderLength,
							liHeaderLength + liOutputLength);
				}
			}
			//after printing the string, start looping into it,
			//pick up each char. convert to ebcdic or whatever and
			//store low order into one array and the higher order into 
			//another array.  print the higher order bytes as characters
			//in the line foll. the string then print the lower order 
			//bytes chars.
			laMFTraceFile.println("THIS IS THE HEADER");
			System.out.println("THIS IS THE HEADER");
			int liLength = lsHeader.length();
			byte larrHeaderASCII[] = lsHeader.getBytes();
			char larrHighOrderBytes[] = new char[liLength];
			char larrLowOrderBytes[] = new char[liLength];
			for (int i = 0; i < larrHeaderASCII.length; i++)
			{
				int liEbcdic = TRT.ASCIIToEBCDIC(larrHeaderASCII[i]);
				String lsStr = Integer.toHexString(liEbcdic);
				if (lsStr.length() > 1)
				{
					larrHighOrderBytes[i] = lsStr.charAt(0);
					larrLowOrderBytes[i] = lsStr.charAt(1);
				}
				else
				{
					larrHighOrderBytes[i] = '0';
					larrLowOrderBytes[i] = lsStr.charAt(0);
				}
			}
			//get the number of complete loops for 50 char lines
			int liLoops = lsHeader.length() / 50;
			for (int i = 0; i < liLoops; i++)
			{
				laMFTraceFile.println(
					lsHeader.substring(i * 50, (i + 1) * 50));
				System.out.println(
					lsHeader.substring(i * 50, (i + 1) * 50));
				for (int j = i * 50; j < (i + 1) * 50; j++)
				{
					laMFTraceFile.print(larrHighOrderBytes[j]);
					System.out.print(larrHighOrderBytes[j]);
				}
				laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
				System.out.println(CommonConstant.STR_SPACE_EMPTY);
				for (int j = i * 50; j < (i + 1) * 50; j++)
				{
					laMFTraceFile.print(larrLowOrderBytes[j]);
					System.out.print(larrLowOrderBytes[j]);
				}
				// Set EOL
				laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
				System.out.println(CommonConstant.STR_SPACE_EMPTY);
			}

			// Print last 3 lines (Data, HighOrder, LowOrder)
			laMFTraceFile.println(
				lsHeader.substring(liLoops * 50, lsHeader.length()));
			System.out.println(
				lsHeader.substring(liLoops * 50, lsHeader.length()));
			for (int j = liLoops * 50; j < lsHeader.length(); j++)
			{
				laMFTraceFile.print(larrHighOrderBytes[j]);
				System.out.print(larrHighOrderBytes[j]);
			}
			laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
			System.out.println(CommonConstant.STR_SPACE_EMPTY);
			for (int j = liLoops * 50; j < lsHeader.length(); j++)
			{
				laMFTraceFile.print(larrLowOrderBytes[j]);
				System.out.print(larrLowOrderBytes[j]);
			}
			// print EOL
			laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
			System.out.println(CommonConstant.STR_SPACE_EMPTY);

			for (int j = 0; j < 3; j++)
			{
				laMFTraceFile.println();
				System.out.println();
			}
			// Printing the Response
			// Inquiry
			if (aaPrintObject instanceof String)
			{
				laMFTraceFile.println("MfRESPONSE: ");
				System.out.println("MfRESPONSE: ");
			}
			// SendTrans
			else
			{
				laMFTraceFile.println("MfKEYS:");
				System.out.println("MfKEYS:");
			}
			liLength = lsMfResponse.length();
			byte laMfResponseASCII[] = lsMfResponse.getBytes();
			larrHighOrderBytes = new char[liLength];
			larrLowOrderBytes = new char[liLength];
			//System.out.println("this is the byte array");
			for (int i = 0; i < laMfResponseASCII.length; i++)
			{
				int liEbcdic = TRT.ASCIIToEBCDIC(laMfResponseASCII[i]);
				String lsStr = Integer.toHexString(liEbcdic);
				if (lsStr.length() > 1)
				{
					larrHighOrderBytes[i] = lsStr.charAt(0);
					larrLowOrderBytes[i] = lsStr.charAt(1);
				}
				else
				{
					larrHighOrderBytes[i] = '0';
					larrLowOrderBytes[i] = lsStr.charAt(0);
				}
			}
			//get the number of complete loops for 50 char lines
			liLoops = lsMfResponse.length() / 50;
			for (int i = 0; i < liLoops; i++)
			{
				laMFTraceFile.println(
					lsMfResponse.substring(i * 50, (i + 1) * 50));
				System.out.println(
					lsMfResponse.substring(i * 50, (i + 1) * 50));
				for (int j = i * 50; j < (i + 1) * 50; j++)
				{
					laMFTraceFile.print(larrHighOrderBytes[j]);
					System.out.print(larrHighOrderBytes[j]);
				}
				laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
				System.out.println(CommonConstant.STR_SPACE_EMPTY);
				for (int j = i * 50; j < (i + 1) * 50; j++)
				{
					laMFTraceFile.print(larrLowOrderBytes[j]);
					System.out.print(larrLowOrderBytes[j]);
				}
				laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
				System.out.println(CommonConstant.STR_SPACE_EMPTY);
			}
			laMFTraceFile.println(
				lsMfResponse.substring(
					liLoops * 50,
					lsMfResponse.length()));
			System.out.println(
				lsMfResponse.substring(
					liLoops * 50,
					lsMfResponse.length()));
			for (int j = liLoops * 50; j < lsMfResponse.length(); j++)
			{
				laMFTraceFile.print(larrHighOrderBytes[j]);
				System.out.print(larrHighOrderBytes[j]);
			}
			laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
			System.out.println(CommonConstant.STR_SPACE_EMPTY);
			for (int j = liLoops * 50; j < lsMfResponse.length(); j++)
			{
				laMFTraceFile.print(larrLowOrderBytes[j]);
				System.out.print(larrLowOrderBytes[j]);
			}
			laMFTraceFile.println(CommonConstant.STR_SPACE_EMPTY);
			System.out.println(CommonConstant.STR_SPACE_EMPTY);
			for (int j = 0; j < 3; j++)
			{
				laMFTraceFile.println();
				System.out.println();
			}
			//close the trace file
			laMFTraceFile.close();
		}
		catch (java.io.IOException aeIOEx)
		{
			System.err.println(
				"MfAccess error "
					+ MF_NET_NAME
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());
			aeIOEx.printStackTrace();
		}
	}
	/**
	 * Resizes a string to the length. Can insert <code>0</code>s or
	 * spaces. Can insert at the end or at the beginning. 
	 * 
	 * @param asValue String String to be resized
	 * @param aiLength int Length of string in the mainframe buffer 
	 * @param achBufferChar char Character to fill the empty spaces
	 * @param asInsertPosition String Position to insert characters
	 * @return String String resized string
	 */
	public String resizeStringtoLength(
		String asValue,
		int aiLength,
		char achBufferChar,
		String asInsertPosition)
	{
		//create the return object
		String lsResizedString = CommonConstant.STR_SPACE_EMPTY;
		if (asValue == null)
		{
			asValue = CommonConstant.STR_SPACE_EMPTY;
		}
		StringBuffer lsResizedStr = new StringBuffer(asValue);
		int liFillLength = aiLength - asValue.length();
		boolean lbInsertAtEnd =
			asInsertPosition.equals(
				ApplicationControlConstants.SC_MFA_INSERT_AT_END);
		boolean lbInsertAtBeginning =
			asInsertPosition.equals(
				ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING);
		int liInsertPosition = Integer.MIN_VALUE;
		if (liFillLength < 0)
		{
			// if the actual length is more than the Length to be set, 
			// truncate at the end
			lsResizedStr =
				new StringBuffer(asValue.substring(0, aiLength));
		}
		else if (lbInsertAtEnd)
		{
			liInsertPosition = asValue.length();
			for (int i = liInsertPosition; i < aiLength; i++)
			{
				lsResizedStr.insert(i, achBufferChar);
			}
		}
		else if (lbInsertAtBeginning)
		{
			liInsertPosition = 0;
			for (int i = liInsertPosition;
				(i + asValue.length() < aiLength);
				i++)
			{
				lsResizedStr.insert(i, achBufferChar);
			}
		}
		lsResizedString = lsResizedStr.toString();
		return lsResizedString;
	}
	/**
	 * Retrieves the funds payment data list from the mainframe. Gets the
	 * key type and the key value to retrieve the result set. 
	 * 
	 * @param searchData GeneralSearchData
	 * @return FundsPaymentDataList
	 * @throws RTSException 
	 */
	public FundsPaymentDataList retrieveFundsPaymentDataList(GeneralSearchData aaGSD)
		throws RTSException
	{
		//create the return object
		FundsPaymentDataList laFundsPaymentDataList =
			new FundsPaymentDataList();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = CommonConstant.STR_SPACE_EMPTY;
		int liTraceNo = 0;
		String lsCheckNo = CommonConstant.STR_SPACE_EMPTY;
		RTSDate laFundsReportDate = new RTSDate();
		RTSDate laFundsPaymentDate = new RTSDate();
		RTSDate laRptngDate = new RTSDate();
		final String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsKeyType = aaGSD.getKey1();
		int liComptCntyNo = aaGSD.getIntKey1();
		// defect 9086
		//	Get Tier search, Active/Inactive
		int liTier = aaGSD.getIntKey2();
		// end defect 9086
		String lsFundsPaymentDataResponse =
			CommonConstant.STR_SPACE_EMPTY;
		if (lsKeyType.equals(GeneralSearchData.FUNDS))
		{
			//lsKeyType = "FDSRPTDATE"; 
			laFundsReportDate = aaGSD.getDate1();
			String lsFundsReportDate =
				Integer.toString(laFundsReportDate.getYYYYMMDDDate());
			String lsRptngDate = "00000000";
			if (aaGSD.getDate2() != null)
			{
				laRptngDate = aaGSD.getDate2();
				lsRptngDate =
					Integer.toString(laRptngDate.getYYYYMMDDDate());
			}
			//lsTransactionId = "R13";
			lsTransactionId = MfAccess.R13;
			// defect 9086
			//	New format for APPCHeader key
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsFundsReportDate,
					lsRptngDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086

			//set the funds due data list first
			String lsFundsDueDataResponse =
				CommonConstant.STR_SPACE_EMPTY;

			// If getConnect, MFInterfaceVersionCode, ECIRequest or 
			//	JGate fail, throw MF down
			try
			{
				lsFundsDueDataResponse =
					getMfResponse(lsTransactionId, lsMfKeys);
			}
			catch (Exception aeRTSEx)
			{
				RTSException laRTSE =
					new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
				laRTSE.setMsgType(RTSException.MF_DOWN);
				throw laRTSE;
			}

			// defect 8983
			//	Process is move to services.data
			/**************************************************************/
			// Move MF string data into Funds payment Data Objects
			MfbaFundsRemittanceG laFundsRemitt =
				new MfbaFundsRemittanceG();
			laFundsPaymentDataList.setVectorFundsDue(
				laFundsRemitt.setMultipleFundsPaymentDataFromMf(
					lsFundsDueDataResponse));
			laFundsRemitt = null;

			//			laFundsPaymentDataList.setVectorFundsDue(
			//				setMultipleFundsDueDataFromMf(lsFundsDueDataResponse));
			// end defect 8983
			/**************************************************************/

			//set keys for the payment mainframe call
			//lsTransactionId = "R30";
			lsTransactionId = MfAccess.R30;
			// defect 9086
			//	New format for APPCHeader key
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsFundsReportDate,
					lsRptngDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(GeneralSearchData.PAYMENT))
		{
			//lsKeyType = "FDSPMTDATE";
			laFundsPaymentDate = aaGSD.getDate1();
			String lsFundsPaymentDate =
				Integer.toString(laFundsPaymentDate.getYYYYMMDDDate());
			//lsTransactionId = "R32";
			lsTransactionId = MfAccess.R32;
			// defect 9086
			//	New format for APPCHeader key
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsFundsPaymentDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(GeneralSearchData.TRACE))
		{
			//lsKeyType = "FDSTRACENO";
			liTraceNo = Integer.valueOf(aaGSD.getKey2()).intValue();
			//lsTransactionId = "R31";
			lsTransactionId = MfAccess.R31;
			// defect 9086
			//	New format for APPCHeader key
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					liTraceNo,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(GeneralSearchData.CHECK_NO))
		{
			//lsKeyType = "FDSCHECKNO";
			lsCheckNo = aaGSD.getKey2();
			//lsTransactionId = "R15";
			lsTransactionId = MfAccess.R15;
			// defect 9086
			//	New format for APPCHeader key
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					lsCheckNo,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		//try the funds payment call
		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsFundsPaymentDataResponse =
				getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			laRTSE.setMsgType(RTSException.MF_DOWN);
			throw laRTSE;
		}

		// Move MF string data into Funds payment Data Objects
		MfbaFundsPaymentG laFundsPayment = new MfbaFundsPaymentG();
		laFundsPaymentDataList.setFundsPymnt(
			laFundsPayment.setMultipleFundsPaymentDataFromMf(
				lsFundsPaymentDataResponse));
		laFundsPayment = null;

		return laFundsPaymentDataList;
	}
	/**
	 * Retrieves the funds remittance record from the TxDOT mainframe
	 * 
	 * @param liComptCntyNo int 
	 * @return FundsDueDataList
	 * @throws RTSException 
	 */
	public FundsDueDataList retrieveFundsRemittanceRecord(int aiComptCntyNo)
		throws RTSException
	{
		//create the return object
		FundsDueDataList laFundsDueDataList = new FundsDueDataList();
		// define all the input keys
		// For FundsRemittance retrieval the COBOL program needs
		// FDSDUEDATE in MFReqKey (MFKeys Buffer) and the dummy value
		// 99999999 in FundsDueDate (MFkeys buffer) along with the actual
		// search key ComptCntyNo (MfKeys buffer) which is passed to 
		// this method by the calling method
		//String lsMfReqKey = "FDSDUEDATE";
		String lsMfReqKey = CommonConstant.FUNDS_DUE_DATE;
		// defect 10406
		//String lsFundsDueDate = "99999999";
		String lsFundsDueDate = "00000000";
		// end defect 10406
		RTSDate laDummyDate = new RTSDate();
		String lsDummyDate =
			Integer.toString(laDummyDate.getYYYYMMDDDate());
		lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		//String lsTransactionId = "R14";
		String lsTransactionId = MfAccess.R14;
		// defect 9086
		//	New format for APPCHeader key
		//	Get Tier search, Active/Inactive
		int liTier = 0;
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsMfReqKey,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				aiComptCntyNo,
				lsDummyDate,
				lsDummyDate,
				lsFundsDueDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086
		String lsFundsDueDataResponse = CommonConstant.STR_SPACE_EMPTY;

		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsFundsDueDataResponse =
				getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			laRTSE.setMsgType(RTSException.MF_DOWN);
			throw laRTSE;
		}

		// defect 8983
		//	Process is move to services.data
		/**************************************************************/
		// Move MF string data into Funds payment Data Objects
		MfbaFundsRemittanceG laFundsRemitt = new MfbaFundsRemittanceG();
		laFundsDueDataList.setFundsDue(
			laFundsRemitt.setMultipleFundsPaymentDataFromMf(
				lsFundsDueDataResponse));
		laFundsRemitt = null;
		//		laFundsDueDataList.setFundsDue(
		//			setMultipleFundsDueDataFromMf(lsFundsDueDataResponse));
		// end defect 8983
		/**************************************************************/

		return laFundsDueDataList;
	}
	/**
	 * Retrieves the invoice items from the invoice tracking file on the
	 * TxDOT mainframe.
	 *
	 * @param aaGSD GeneralSearchData Contains the key type, the
	 * actual key and the county number for retrieving the invoice items.
	 * @return MFInventoryAllocationData
	 * @throws RTSException 
	 */
	public MFInventoryAllocationData retrieveInvoice(GeneralSearchData aaGSD)
		throws RTSException
	{
		//create the return object
		MFInventoryAllocationData laMFInventoryAllocationData =
			new MFInventoryAllocationData();
		//get the invoice number from the general Search data
		String lsKeyType = CommonConstant.INVOICE_NO;
		String lsKey = aaGSD.getKey1();
		int liComptCntyNo = aaGSD.getIntKey1();
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//define the response 
		String lsMfInvoiceAckResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfInvoiceItemsResponse =
			CommonConstant.STR_SPACE_EMPTY;
		//Make the INVACK call
		//String lsTransactionId = "R17";
		String lsTransactionId = MfAccess.R17;
		// defect 9086
		//	New format for APPCHeader key
		//	Get Tier search, Active/Inactive
		int liTier = aaGSD.getIntKey2();
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				liComptCntyNo,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				lsKey,
				liTier);
		// end defect 9086

		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsMfInvoiceAckResponse =
				getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException leRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			leRTSE.setMsgType(RTSException.MF_DOWN);
			throw leRTSE;
		}
		//Make a INVCITMS call
		//set transaction - no need to change Mfkeys
		lsTransactionId = MfAccess.R26;
		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsMfInvoiceItemsResponse =
				getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException leRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			leRTSE.setMsgType(RTSException.MF_DOWN);
			throw leRTSE;
		}
		// defect 6701
		// parse the mf response into the data object
		MfbaInventoryInvoiceG laInventoryInvoiceG =
			new MfbaInventoryInvoiceG();
		laMFInventoryAllocationData =
			laInventoryInvoiceG.formatInventoryInvoiceData(
				this,
				lsMfInvoiceAckResponse,
				lsMfInvoiceItemsResponse);
		// Do not use the old style anymore!
		//convert mf response to data object
		//laMFInventoryAllocationData =
		//	setInvoiceFromMf(
		//		lsMfInvoiceAckResponse,
		//		lsMfInvoiceItemsResponse);
		// end defect 6701

		//return the result
		return laMFInventoryAllocationData;
	}

	//	
	/**********************************************************************************************/
	/**
	 * Retrieves a vehicle record from the Active/Inactive/Archive
	 *  files on the TxDOT mainframe by DocNo
	 *
	 * @param aaGSD GeneralSearchData 
	 * @param asCICSTransId String - CICS TransactionId
	 * @return VehicleInquiryData
		*/
	public String retrieveMfRecordsByDocNo(
		GeneralSearchData aaGSD,
		String asCICSTransId)
		throws RTSException
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		// Searched Archive
		int liSearchArchive = aaGSD.getIntKey2();

		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//	REGIS buffer
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();

		//set the MfKeys sent to MF: DocNo is set	
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				lsKey,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liSearchArchive);

		try
		{
			lsMfResponse = getMfResponse(asCICSTransId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{

			throw aeRTSEx;
		}

		return lsMfResponse;
	}
	/**********************************************************************************************/
	/**
	 * Retrieves a vehicle record from the Active/Inactive/Archive
	 *  files on the TxDOT mainframe by OwnrId
	 *
	 * @param aaGSD GeneralSearchData 
	 * @param asCICSTransId String - CICS TransactionId
	 * @return VehicleInquiryData
		*/
	public String retrieveMfRecordsByOwnrId(
		GeneralSearchData aaGSD,
		String asCICSTransId)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		// Searched Archive
		int liSearchArchive = aaGSD.getIntKey2();

		//For setting the MF_Down field in the VehInqData
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//	REGIS buffer
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();

		//set the MfKeys sent to MF: OwnrId is set	
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				lsKey,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liSearchArchive);

		try
		{
			lsMfResponse = getMfResponse(asCICSTransId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			return lsMfResponse;
		}

		return lsMfResponse;
	}

	//	
	/**********************************************************************************************/
	/**
	 * Retrieves a vehicle record from the Active/Inactive/Archive
	 *  files on the TxDOT mainframe by RegPltNo
	 *
	 * @param aaGSD GeneralSearchData 
	 * @param asCICSTransId String - CICS TransactionId
	 * @return VehicleInquiryData
		*/
	public String retrieveMfRecordsByRegPltNo(
		GeneralSearchData aaGSD,
		String asCICSTransId)
		throws RTSException
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		// Searched Archive
		int liSearchArchive = aaGSD.getIntKey2();

		//For setting the MF_Down field in the VehInqData
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//	REGIS buffer
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();

		//set the MfKeys sent to MF: RegPltNo is set	
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				lsKey,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liSearchArchive);

		try
		{
			lsMfResponse = getMfResponse(asCICSTransId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}

		return lsMfResponse;
	}
	/**********************************************************************************************/
	/**
	 * Retrieves a vehicle record from the Active/Inactive/Archive
	 *  files on the TxDOT mainframe by OwnrId
	 *
	 * @param aaGSD GeneralSearchData 
	 * @param asCICSTransId String - CICS TransactionId
	 * @return VehicleInquiryData
		*/
	public String retrieveMfRecordsByVIN(
		GeneralSearchData aaGSD,
		String asCICSTransId)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		//	Search Active/Inactive
		int liTier = aaGSD.getIntKey2();

		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//	REGIS buffer
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();

		//set the MfKeys sent to MF: VIN is set	
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsKey,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);

		try
		{
			lsMfResponse = getMfResponse(asCICSTransId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			return lsMfResponse;
		}

		return lsMfResponse;
	}
	/**
	 * Retrieves the number of documents for the given doc no from 
	 * the mainframe. 
	 * 
	 * @param aaGSD GeneralSearchData 
	 * Contains the doc no for searching the mainframe.
	 * @return int The number of documents for the given doc no 
	 * @throws RTSException 
	 */
	public int retrieveNumberofDocuments(GeneralSearchData aaGSD)
		throws RTSException
	{
		// defect 10244
		//	Set variable to constant
		//		int liNO_OF_RECS_OFFSET = 0;
		final int NO_OF_RECS_OFFSET = 0;
		// end defect 10244
		//create the return object
		int liNoOfRecs = 0;
		String lsMfReqKey = aaGSD.getKey1();
		// defect 8984
		//	Get DocNo from Key2
		String lsDocumentNumber = aaGSD.getKey2();
		// end defect 8984
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsTransactionId = MfAccess.R12;
		// defect 9086
		//	New format for APPCHeader key
		//	Get Tier search, Active/Inactive
		int liTier = aaGSD.getIntKey2();
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsMfReqKey,
				lsDocumentNumber,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086
		String lsNumberOfDocsResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException leRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			leRTSE.setMsgType(RTSException.MF_DOWN);
			throw leRTSE;
		}
		// even though the search is by doc no, there could be multiple
		// records that could be returned. 
		APPCHeader laAppcHeader = new APPCHeader();
		final int liHEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		//remove the header from the response
		lsNumberOfDocsResponse =
			lsMfResponse.substring(liHEADER_LENGTH);

		// defect 10462 
		// Get number of records from header
		liNoOfRecs =
			rtnNoOfRecsMF(lsNumberOfDocsResponse, NO_OF_RECS_OFFSET);
		// end defect 10462 
		return liNoOfRecs;
	}

	/**
	 * Retrieves the owner from the mainframe using owner id
	 * 
	 * @param aaGSD GeneralSearchData
	 * @return OwnerData
	 * @throws RTSException MF_DOWN Exception
	 */
	public OwnerData retrieveOwner(GeneralSearchData aaGSD)
		throws RTSException
	{
		//create the return object
		OwnerData laOwnerData = new OwnerData();
		//get the invoice number from the general Search data
		String lsKeyType = CommonConstant.OWNER_ID;
		String lsKey = aaGSD.getKey4(); // Search key
		int liTier = aaGSD.getIntKey2(); // Active/Inactive

		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		//define the response 
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		//		String lsMfOwnerResponse = CommonConstant.STR_SPACE_EMPTY;
		//Make the OWNR Call
		String lsTransactionId = MfAccess.R18;

		// deefct 9086
		//	New header format for MF
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				lsKey,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086

		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			leRTSE.setMsgType(RTSException.MF_DOWN);
			throw leRTSE;
		}

		// defect 8983
		//	Move process to services.data
		//convert mf response to data object
		MfbaOwnerFromMFOwnerG laOwnrFromOwnr =
			new MfbaOwnerFromMFOwnerG();
		laOwnerData =
			laOwnrFromOwnr.setOwnerDataFromMfOwner(lsMfResponse);
		laOwnrFromOwnr = null;
		//			laOwnerData = setOwnerDataFromMfOwner(lsMfOwnerResponse);
		// end defect 8983
		//		}
		//return the result
		return laOwnerData;
	}
	//	/**
	//	 * Retrieves the current time and date from the TxDOT mainframe
	//	 * 
	//	 * @throws RTSException 
	//	 * @return RTSDate
	//	 * @deprecated
	//	 */
	//	public RTSDate retrieveTimeDate() throws RTSException
	//	{
	//		// define the offsets and lengths of various fields in 
	//		// lsMfResponse from MF
	//		final int liHOUR_OFFSET = 256;
	//		final int liHOUR_LENGTH = 2;
	//		final int liMINUTE_OFFSET = 258;
	//		final int liMINUTE_LENGTH = 2;
	//		final int liSECOND_OFFSET = 260;
	//		final int liSECOND_LENGTH = 2;
	//		final int liYEAR_OFFSET = 262;
	//		final int liYEAR_LENGTH = 4;
	//		final int liMONTH_OFFSET = 266;
	//		final int liMONTH_LENGTH = 2;
	//		final int liDAY_OFFSET = 268;
	//		final int liDAY_LENGTH = 2;
	//		final int liMILLISECOND = 0;
	//		//Get the appc header to get the length
	//		//set up the transaction ID
	//		//String lsTransactionId = "R08";
	//		String lsTransactionId = MfAccess.R08;
	//		// set up the keys to be sent to the mainframe
	//		RTSDate laDummyDate = new RTSDate();
	//		String lsDummyDate =
	//			Integer.toString(laDummyDate.getYYYYMMDDDate());
	//		String lsMfKeys =
	//			setupMfKeys(
	//				0,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				0,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				0,
	//				lsDummyDate,
	//				lsDummyDate,
	//				lsDummyDate,
	//				lsDummyDate,
	//				0,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				CommonConstant.STR_SPACE_EMPTY,
	//				0,
	//				0);
	//		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
	//		RTSDate laMfDate = new RTSDate();
	//		//Get response from mainframe
	//		try
	//		{
	//			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
	//		}
	//		catch (Exception aeRTSEx)
	//		{
	//			RTSException leRTSE =
	//				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
	//			leRTSE.setMsgType(RTSException.MF_DOWN);
	//			throw leRTSE;
	//		}
	//		//set the return object with values obtained from mainframe
	//		int liHour =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liHOUR_OFFSET,
	//						liHOUR_OFFSET + liHOUR_LENGTH))
	//				.intValue();
	//		int liMinute =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liMINUTE_OFFSET,
	//						liMINUTE_OFFSET + liMINUTE_LENGTH))
	//				.intValue();
	//		int liSecond =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liSECOND_OFFSET,
	//						liSECOND_OFFSET + liSECOND_LENGTH))
	//				.intValue();
	//		int liYear =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liYEAR_OFFSET,
	//						liYEAR_OFFSET + liYEAR_LENGTH))
	//				.intValue();
	//		int liMonth =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liMONTH_OFFSET,
	//						liMONTH_OFFSET + liMONTH_LENGTH))
	//				.intValue();
	//		int liDay =
	//			Integer
	//				.valueOf(
	//					lsMfResponse.substring(
	//						liDAY_OFFSET,
	//						liDAY_OFFSET + liDAY_LENGTH))
	//				.intValue();
	//		//create the return object 
	//		laMfDate =
	//			new RTSDate(
	//				liYear,
	//				liMonth,
	//				liDay,
	//				liHour,
	//				liMinute,
	//				liSecond,
	//				liMILLISECOND);
	//		return laMfDate;
	//	}

	/**
	 * Retrieves Permit Data using one of the following keys:  
	 *   1) PrmtIssuanceId
	 *   2) PrmtNo
	 *   3) CustLstName
	 *   4) CustBsnName
	 * with the possible additional arguments
	 *   1) BeginDate
	 *   2) EndDate  
	 * 
	 * @param  aaGSD
	 * @return String 
	 */
	public String retrievePermit(GeneralSearchData aaGSD)
		throws RTSException
	{
		// PermitSearchKeys will analyze the GSD and assign  
		// fields accordingly 
		PermitSearchKeys laPrmtSrchKeys = new PermitSearchKeys(aaGSD);

		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();

		// set the MfKeys sent to MF	
		lsMfKeys =
			setupMfKeysSpclPlt(
				aaGSD.getKey1(),
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				laPrmtSrchKeys.getPrmtVIN(),
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				laPrmtSrchKeys.getPrmtIssuanceId(),
				laPrmtSrchKeys.getPrmtNo(),
				laPrmtSrchKeys.getCustLstName(),
				laPrmtSrchKeys.getCustBsnName(),
				laPrmtSrchKeys.getBeginDate(),
				laPrmtSrchKeys.getEndDate());
		try
		{
			lsMfResponse = getMfResponse(R02, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		return lsMfResponse;
	}

	/**
	 * Deletes the record associated with a title that exists in the
	 * process file on the TxDOT mainframe
	 * 
	 * @param asDocumentNumber java.lang.String
	 * @return TitleInProcessData
	 * @throws RTSException 
	 */
	public TitleInProcessData retrieveTitleInProcess(String asDocumentNumber)
		throws RTSException
	{
		//create the return object
		TitleInProcessData laTitleInProcessData =
			new TitleInProcessData();
		String lsMfReqKey = CommonConstant.DOC_NO;
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsTransactionId = MfAccess.R09;
		// defect 9086
		//	New MfKeys format for Special Plates 
		//set the MfKeys sent to MF: DocNo is set
		int liTier = 0; // Active/Inactive	
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsMfReqKey,
				asDocumentNumber,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086

		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		// If getConnect, MFInterfaceVersionCode, ECIRequest or 
		//	JGate fail, throw MF down
		try
		{
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeRTSEx);
			laRTSE.setMsgType(RTSException.MF_DOWN);
			throw laRTSE;
		}

		// defect 6701
		//	Make call to services.data class
		MfbaTitleInProcessG laTitleInProcessG =
			new MfbaTitleInProcessG();

		// defect 9557
		//	Remove the header from the data string.
		String lsTtlRegisResponse = getMfTtlRegResponse(lsMfResponse);
		laTitleInProcessData =
			laTitleInProcessG.setTitleInProcessDataFromMfResponse(
				lsTtlRegisResponse);
		//		laTitleInProcessData =
		//			laTitleInProcessG.setTitleInProcessDataFromMfResponse(
		//				lsMfResponse);
		// end defect 9557
		// Clean up variables
		laTitleInProcessG = null;
		// end defect 6701

		return laTitleInProcessData;
	}
	/**
	 * Retrieves a vehicle record from the special owner file on the 
	 * TxDOT mainframe
	 *
	 * <p>Uses Key 1 and Key 2
	 * 
	 * @param aaGSD GeneralSearchData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData retrieveVehicleBySpecialOwner(GeneralSearchData aaGSD)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		int liComptCntyNo = aaGSD.getIntKey1();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		final int liMF_DOWN = 1;
		APPCHeader laAppcHeader = new APPCHeader();
		final int liHEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		int liOutputLength = 0;
		//create the return object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		//* Define the response strings
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9086

		// defect 6701
		// Offsets have been move to the class level
		//final int MULTIPLE_REG_INDI_POSITION = 1449;
		//final int JNK_INDI_POSITION = 968;
		// end defect 6701

		// set up the keys to be sent to the mainframe
		RTSDate laDummyDate = new RTSDate();
		String lsDummyDate =
			Integer.toString(laDummyDate.getYYYYMMDDDate());
		String lsMfKeys = new String();
		// defect 9086
		//	New MfKeys format for Special Plates 
		//set the MfKeys sent to MF: DocNo is set
		int liTier = aaGSD.getIntKey2(); // Active/Inactive	
		if (lsKeyType.equals(CommonConstant.REG_PLATE_NO))
		{
			//lsKeyType = "REGPLTNO";
			//lsTransactionId = "R19";
			lsTransactionId = MfAccess.R19;
			//set the up the input to be sent to the mainframe
			//set key type and regpltno field
			// defect 8902
			// Added call for converting plate number i's 
			//	and o's to 1's and 0's. 
			lsKey = CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
			// end defect 8902
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(CommonConstant.OWNER_ID))
		{
			lsTransactionId = MfAccess.R20;
			//set the up the input to be sent to the mainframe
			//set key type and ownerid field
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					liComptCntyNo,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		//get the information from mainframe
		try
		{
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeRTSEx)
		{
			laVehicleInquiryData.setMfDown(liMF_DOWN);
			return laVehicleInquiryData;
		}
		if ((lsMfResponse != null)
			&& !(lsMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// defect 8983
			//	Get output length from APPcHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(lsMfResponse);
			//			liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
			//			liOutputLengthLength = laAppcHeader.getHDOUTLEN();
			//			liOutputLength =
			//				Integer.parseInt(
			//					lsMfResponse.substring(
			//						liOutputLengthOffset,
			//						liOutputLengthOffset + liOutputLengthLength));
			// end defect 8983

			lsMfSpecialOwnerResponse =
				lsMfResponse.substring(
					liHEADER_LENGTH,
					liHEADER_LENGTH + liOutputLength);
		}
		laVehicleInquiryData = new VehicleInquiryData();
		//create the VehicleInquiryInformation
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);
		return laVehicleInquiryData;
	}
	/**
	 * Retrieves a vehicle record from the Active/Inactive files on 
	 * the TxDOT mainframe
	 *
	 * <p>Uses Key 1 and Key 2
	 * 
	 * @param aaGSD GeneralSearchData 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData retrieveVehicleFromActiveInactive(GeneralSearchData aaGSD)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		//create retRn object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		//For setting the MF_Down field in the VehInqData
		final int liMF_DOWN = 1;
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//Get the header record length
		APPCHeader laAppcHeader = new APPCHeader();
		final int liHEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		//		int liOutputLengthOffset = 0;
		//		int liOutputLengthLength = 0;
		int liOutputLength = 0;

		// defect 6701
		// Offsets have been move to the class level
		//final int MULTIPLE_REG_INDI_POSITION = 1449;
		//final int JNK_INDI_POSITION = 968;
		// end defect 6701

		// defect 10244
		// Move variable to class level
		//		final int NO_OF_RECS_OFFSET = 145;
		// end defect 10244
		//		final int NO_OF_RECS_LENGTH = 3;

		// defect 6701
		//	Move to setDocNoKey()
		//		final int DOC_NO_OFFSET = 941;
		//		final int DOC_NO_LENGTH = 17;
		// end defect 6701

		// Define all response strings 
		//	String obtained from a MF call
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		// TTL/REG buffer (for all events - not just ttl/reg event)
		//	REGIS buffer
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		//	Junk buffer
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		//	VINA buffer 
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		//	SPCL OWNR buffer
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		//	New MfKeys format for Special Plates 
		//set the MfKeys sent to MF: DocNo is set
		int liTier = aaGSD.getIntKey2(); // Active/Inactive	

		// set up the keys to be sent to the mainframe
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();
		//		String lsDocNo = CommonConstant.STR_SPACE_EMPTY;
		if (lsKeyType.equals(CommonConstant.DOC_NO))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R01;
			//set the MfKeys sent to MF: DocNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					lsKey,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(CommonConstant.REG_PLATE_NO))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R03;
			// defect 8902
			lsKey = CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
			// end defect 8902
			//set the MfKeys sent to MF: RegPltNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(CommonConstant.VIN))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R04;
			//set the MfKeys sent to MF: VIN is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsKey,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		try
		{
			//Get Response from MF
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			laVehicleInquiryData.setMfDown(liMF_DOWN);
			return laVehicleInquiryData;
		}
		if ((lsMfResponse != null)
			&& !(lsMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// defect 8983
			//	Get output length from APPcHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(lsMfResponse);
			//Pick the output length from commarea header
			//			liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
			//			liOutputLengthLength = laAppcHeader.getHDOUTLEN();
			//			liOutputLength =
			//				Integer.parseInt(
			//					lsMfResponse.substring(
			//						liOutputLengthOffset,
			//						liOutputLengthOffset + liOutputLengthLength));
			// end defect 8983

			//This is the result without the header	
			lsMfTtlRegResponse =
				lsMfResponse.substring(
					liHEADER_LENGTH,
					liHEADER_LENGTH + liOutputLength);
		}
		// check for REGIS event
		// check whether NoOfRecs = 1 and MultipleRegIndi = '1'
		if ((lsMfTtlRegResponse != null)
			&& (!lsMfTtlRegResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// Get number of records from header
			// defect 10462 
			int liNoOfRecs =
				rtnNoOfRecsMF(lsMfResponse, NO_OF_RECS_OFFSET);
			// end defect 10462 

			if (liNoOfRecs == 1)
			{
				// defect 6701
				//	Get offset for MultipleRegIndi 
				//
				//	Set MultipleReg boolean 
				boolean lbMultipleReg = true;
				char lcMultipleRegIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				//				lcMultipleRegIndi =
				//					getStringFromZonedDecimal(
				//						lsMfTtlRegResponse).charAt(
				//							MULTIPLE_REG_INDI_POSITION);
				// end defect 6701

				if ((liNoOfRecs == 1) && (lcMultipleRegIndi == '1'))
				{
					lsKeyType = CommonConstant.DOC_NO;
					// defect 6701
					//	Set DocNo key bases on MF version 
					//
					lsKey = setDocNoKey(lsMfTtlRegResponse);
					//					lsKey =
					//						lsMfTtlRegResponse.substring(
					//							DOC_NO_OFFSET,
					//							DOC_NO_OFFSET + DOC_NO_LENGTH);
					// end defect 6701

					if (lsKeyType.equals(CommonConstant.DOC_NO))
					{
						lsTransactionId = MfAccess.R33;
						//set the MfKeys sent to MF: DocNo is set	
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								lsKey,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					else if (
						lsKeyType.equals(CommonConstant.REG_PLATE_NO))
					{
						// TODO this must be the wrong transaction!
						lsTransactionId = MfAccess.R29;
						//set the MfKeys sent to MF: RegPltNo is set	
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								lsKey,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					try
					{
						lsMfRegisResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSEx)
					{
						laVehicleInquiryData.setMfDown(liMF_DOWN);
						return laVehicleInquiryData;
					}
				}
				// Get JUNK if JnkIndi = 1 and NoOfRecs = 1
				// Make the call with DocNo obtained from the prev call 
				//
				// defect 6701
				//	Get JunkIndi from getMultipleRegJunkIndi
				//	Set MultipleReg boolean 
				lbMultipleReg = false;
				char lcJunkIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				//				lcJunkIndi =
				//					getStringFromZonedDecimal(
				//						lsMfTtlRegResponse).charAt(
				//						JNK_INDI_POSITION);
				// end defect 6701
				if ((liNoOfRecs == 1) && (lcJunkIndi == '1'))
				{
					//lsTransactionId = "R28";
					lsTransactionId = MfAccess.R28;
					//CHANGE: HAVE TO SET MFKEYS WITH DOC NO AS KEY.
					lsKeyType = CommonConstant.DOC_NO;

					// defect 6701
					//	Set DocNo key bases on MF version 
					//
					lsKey = setDocNoKey(lsMfTtlRegResponse);
					//					lsKey =
					//						lsMfTtlRegResponse.substring(
					//							DOC_NO_OFFSET,
					//							DOC_NO_OFFSET + DOC_NO_LENGTH);
					// end defect 6701

					//set the MfKeys sent to MF: DocNo is set	
					lsMfKeys =
						setupMfKeysSpclPlt(
							lsKeyType,
							lsKey,
							CommonConstant.STR_SPACE_EMPTY,
							CommonConstant.STR_SPACE_EMPTY,
							0,
							CommonConstant.STR_SPACE_EMPTY,
							0,
							lsDummyDate,
							lsDummyDate,
							lsDummyDate,
							lsDummyDate,
							0,
							CommonConstant.STR_SPACE_EMPTY,
							CommonConstant.STR_SPACE_EMPTY,
							liTier);
					// end defect 9086
					try
					{
						lsMfJunkResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSE)
					{
						laVehicleInquiryData.setMfDown(liMF_DOWN);
						return laVehicleInquiryData;
					}
				}
			} // end of NoOfRecs == 1
			//code for MFPartial here
			if (liNoOfRecs > 1)
			{
				lsMfPartialResponse = lsMfResponse;
				lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
			}
		} // end of if 
		//WHY DO I NEED THIS? I HAVE ALREADY SET THE MFDOWN 
		// AND RETURNED THE OBJECT IF THERE IS AN EXCEPTION! 
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);
		return laVehicleInquiryData;
	}
	//	
	/**********************************************************************************************/

	/**
	 * Retrieves a vehicle record from the archive file on the TxDOT
	 * mainframe
	 * Uses Key1 and Key2
	 * 
	 * @param aaGSD GeneralSearchData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData retrieveVehicleFromArchive(GeneralSearchData aaGSD)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		//	Search Active/Inactive/Archive
		int liTier = aaGSD.getIntKey2();

		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//create retRn object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		final int MF_DOWN = 1;
		APPCHeader laAppcHeader = new APPCHeader();
		final int HEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		int liOutputLength = 0;

		// defect 6701
		// Offsets have been move to the class level
		//final int MULTIPLE_REG_INDI_POSITION = 1449;
		//final int JNK_INDI_POSITION = 968;
		// end defect 6701

		// defect 10244
		//	Move valiable to class level
		// final int NO_OF_RECS_OFFSET = 145;
		// end defect 10244
		// defect 6701
		//	Move to setDocNoKey()
		//		final int DOC_NO_OFFSET = 941;
		//		final int DOC_NO_LENGTH = 17;
		// end defect 6701
		// Define all response strings 
		//	String obtained from a MF call
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		// TTL/REG buffer (for all events - not just ttl/reg event)
		//	REGIS buffer
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		//	Junk buffer
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		//	VINA buffer 
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		//	SPCL OWNR buffer
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9086

		// set up the keys to be sent to the mainframe
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();
		if (lsKeyType.equals(CommonConstant.DOC_NO))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R01;
			// defect 9086
			//set the MfKeys sent to MF: DocNo is set
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					lsKey,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(CommonConstant.REG_PLATE_NO))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R03;
			// defect 8902
			lsKey = CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
			//	New format for MfKeys
			//	set the MfKeys sent to MF: RegPltNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}
		else if (lsKeyType.equals(CommonConstant.VIN))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R04;
			// defect 9086
			//	New format for MfKeys
			//	set the MfKeys sent to MF: VIN is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsKey,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
			// end defect 9086
		}

		try
		{
			//Get Response from MF
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			laVehicleInquiryData.setMfDown(MF_DOWN);
			return laVehicleInquiryData;
		}
		if ((lsMfResponse != null)
			&& !(lsMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// defect 8983
			//	Get output length from APPcHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(lsMfResponse);
			//Pick the output length from commarea header
			//			liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
			//			liOutputLengthLength = laAppcHeader.getHDOUTLEN();
			//			liOutputLength =
			//				Integer.parseInt(
			//					lsMfResponse.substring(
			//						liOutputLengthOffset,
			//						liOutputLengthOffset + liOutputLengthLength));
			// end defect 8983

			//This is the result without the header	
			lsMfTtlRegResponse =
				lsMfResponse.substring(
					HEADER_LENGTH,
					HEADER_LENGTH + liOutputLength);
		}
		// check for REGIS event
		// check whether NoOfRecs = 1 and MultipleRegIndi = '1'
		if ((lsMfTtlRegResponse != null)
			&& (!lsMfTtlRegResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// Get number of records from header
			// defect 10462 
			int liNoOfRecs =
				rtnNoOfRecsMF(lsMfResponse, NO_OF_RECS_OFFSET);
			// end defect 10462 

			if (liNoOfRecs == 1)
			{
				// defect 6701
				//	Get MultipleRegIndi from getMultipleRegJunkIndi
				//	Set MultipleReg boolean 
				boolean lbMultipleReg = true;
				char lcMultipleRegIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				//				lcMultipleRegIndi =
				//					getStringFromZonedDecimal(
				//						lsMfTtlRegResponse).charAt(
				//						MULTIPLE_REG_INDI_POSITION);
				// end defect 6701
				if ((liNoOfRecs == 1) && (lcMultipleRegIndi == '1'))
				{
					if (lsKeyType.equals(CommonConstant.DOC_NO))
					{
						//lsKeyType = "DOCNO"; 
						//lsTransactionId = "R33";
						lsTransactionId = MfAccess.R33;
						// defect 9086
						//	New format for MfKeys
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								lsKey,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					else if (
						lsKeyType.equals(CommonConstant.REG_PLATE_NO))
					{
						//lsKeyType = "REGPLTNO"; 
						// TODO this must be the wrong transaction id!
						//lsTransactionId = "R29";
						lsTransactionId = MfAccess.R29;
						// defect 9086
						//	New format for MfKeys
						//	set the MfKeys sent to MF: RegPltNo is set	
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								lsKey,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					try
					{
						lsMfRegisResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSEx)
					{
						laVehicleInquiryData.setMfDown(MF_DOWN);
						return laVehicleInquiryData;
					}
				}
				// Get JUNK if JnkIndi = 1 and NoOfRecs = 1
				// defect 6701
				//	Get MultipleRegIndi from getMultipleRegJunkIndi
				//	Set MultipleReg boolean 
				lbMultipleReg = false;
				char lcJunkIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				//				char lcJunkIndi =
				//					getStringFromZonedDecimal(
				//						lsMfTtlRegResponse).charAt(
				//						JNK_INDI_POSITION);
				// end defect 6701
				if ((liNoOfRecs == 1) && (lcJunkIndi == '1'))
				{
					//lsTransactionId = "R28";
					lsTransactionId = MfAccess.R28;
					//CHANGE: HAVE TO SET MFKEYS WITH DOC NO AS KEY. 
					lsKeyType = CommonConstant.DOC_NO;

					// defect 6701
					//	Call setDocNo() to set DocNo key
					lsKey = setDocNoKey(lsMfTtlRegResponse);
					//					lsKey =
					//						lsMfTtlRegResponse.substring(
					//							DOC_NO_OFFSET,
					//							DOC_NO_OFFSET + DOC_NO_LENGTH);
					// end defect 6701

					//set the MfKeys sent to MF: DocNo is set	
					// defect 9086
					//	New format for MfKeys
					// Junk nevers search archive
					lsMfKeys =
						setupMfKeysSpclPlt(
							lsKeyType,
							lsKey,
							CommonConstant.STR_SPACE_EMPTY,
							CommonConstant.STR_SPACE_EMPTY,
							0,
							CommonConstant.STR_SPACE_EMPTY,
							0,
							lsDummyDate,
							lsDummyDate,
							lsDummyDate,
							lsDummyDate,
							0,
							CommonConstant.STR_SPACE_EMPTY,
							CommonConstant.STR_SPACE_EMPTY,
							0);
					// end defect 9086
					try
					{
						lsMfJunkResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSEx)
					{
						laVehicleInquiryData.setMfDown(MF_DOWN);
						return laVehicleInquiryData;
					}
				}
			} // end of if NoOfRecs == 1
			//Add Code for MfPartial Here
			else if (liNoOfRecs > 1)
			{
				lsMfPartialResponse = lsMfResponse;
				lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
			}
		} // end of if 
		//create the VehicleInquiryInformation
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);
		return laVehicleInquiryData;
	}

	// defect 9086
	//	Retrieves a vehicle record from the Active/Inactive files on
	//	TxDOT mainframe
	/**********************************************************************************************/
	/**
	/**
	 * Retrieves a vehicle record from the Active/Inactive files on 
	 * the TxDOT mainframe.
	 * Return buffer String
	 *
	 * <p>Uses Key 1 Transaction Id
	 * <p>Uses Key 2 Search Key
	 * <p>Uses Key 3 TransCd
	 * 
	 * @param aaGSD GeneralSearchData 
	 * @return String
	 */
	public String retrieveVehicleFromMF(GeneralSearchData aaGSD)
		throws RTSException
	{
		String lsKeyType = aaGSD.getKey1(); // type of search
		String lsKey = aaGSD.getKey2(); // Search key
		String lsTransCd = aaGSD.getKey3(); // TransCd
		int liTier = aaGSD.getIntKey2(); // Active/Inactive/Archive

		//create return object
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		//Set the MFError String
		csMFError = aaGSD.getKey5();

		// Define all response strings 
		//	String obtained from a MF call
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;

		// set up the keys to be sent to the mainframe
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsMfKeys = new String();
		if (lsKeyType.equals(CommonConstant.DOC_NO))
		{
			// Set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R01;
			//set the MfKeys sent to MF: DocNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					lsKey,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		else if (lsKeyType.equals(CommonConstant.VIN))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R04;
			//set the MfKeys sent to MF: VIN is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsKey,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		else if (lsKeyType.equals(CommonConstant.SPCL_REG_ID))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R05;
			//set the MfKeys sent to MF: SPCLREGID is set
			// Convert lsKey to int
			int liKey = Integer.parseInt(lsKey);
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liKey,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		else if (
			// search R08 if RegPltNo and 
		//	TransCd = Special Plates (Excluding SPAPPL)
		//	SPAPPL will receive records from Title/Regis by RegPltNo
		//	first. Then get SpclRegId and search by SpclRegId
		lsKeyType
			.equals(
			CommonConstant.REG_PLATE_NO)
				&& UtilityMethods.isSpecialPlates(lsTransCd)
				&& !(UtilityMethods.isSPAPPL(lsTransCd)))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R08;
			lsKey = CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
			//set the MfKeys sent to MF: RegPltNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		else if (lsKeyType.equals(CommonConstant.REG_PLATE_NO))
		{
			//set the transaction Id to be sent to the MF
			lsTransactionId = MfAccess.R03;
			// defect 8902
			//	Convert to HOOPSRegPltNo
			lsKey = CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
			// end defect 8902
			//set the MfKeys sent to MF: RegPltNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}

		try
		{
			// Get Response from MF
			// Throw RTSException if MFDown
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}

		// Return MF data string. Will be parsed in VehicleInquiry
		return lsMfResponse;
	}
	/**
	 * Retrieves a vehicle record from VINA on the TxDOT mainframe
	 *
	 * Uses Key1 and Key2 
	 * 
	 * @param aaGSD GeneralSearchData
	 * @return VehicleInquiryData
	 * @deprecated
	 */
	public VehicleInquiryData retrieveVehicleFromVINA(GeneralSearchData aaGSD)
	{
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		int liTier = aaGSD.getIntKey2();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		//Set the MF_Down value
		final int liMF_DOWN = 1;
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//get appcheader to ge the length of appcheader
		APPCHeader laAppcHeader = new APPCHeader();
		final int HEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		//		int liOutputLengthOffset = 0;
		//		int liOutputLengthLength = 0;
		int liOutputLength = 0;
		int liNoOfRecs = Integer.MIN_VALUE;
		//		final int NO_OF_RECS_LENGTH = 3;

		// Define OFFSETS of indicators in the Mainframe response
		// defect 6701
		// Offsets have been move to the class level
		//final int MULTIPLE_REG_INDI_POSITION = 1449;
		//final int JNK_INDI_POSITION = 968;
		// end defect 6701
		// Define the response strings 
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9086

		//create the return object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		// set up the keys to be sent to the mainframe
		RTSDate laDummyDate = new RTSDate();
		String lsDummyDate =
			Integer.toString(laDummyDate.getYYYYMMDDDate());
		String lsMfKeys = new String();

		if (lsKeyType.equals(CommonConstant.REG_PLATE_NO))
		{
			//lsKeyType = "REGPLTNO"; 
			//set the transaction Id to be sent to the MF
			//lsTransactionId = "R03";
			lsTransactionId = MfAccess.R03;
			//set the MfKeys sent to MF: RegPltNo is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsKey,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		else if (lsKeyType.equals(CommonConstant.VIN))
		{
			//set the transaction Id to be sent to the MF
			//lsTransactionId = "R04";
			lsTransactionId = MfAccess.R04;
			//set the MfKeys sent to MF: VIN is set	
			lsMfKeys =
				setupMfKeysSpclPlt(
					lsKeyType,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					0,
					lsKey,
					0,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					lsDummyDate,
					0,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					liTier);
		}
		try
		{
			//Get Response from MF
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSEx)
		{
			laVehicleInquiryData.setMfDown(liMF_DOWN);
			return laVehicleInquiryData;
		}

		// Get number of records return from MF call
		if ((lsMfResponse != null)
			&& !(lsMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			//Pick the output length from commarea header
			// defect 8983
			//	Get output length from APPcHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(lsMfResponse);

			// Get number of records from header
			// defect 10462 
			liNoOfRecs = rtnNoOfRecsMF(lsMfResponse, NO_OF_RECS_OFFSET);
			// end defect 10462 
			// end defect 8983
		}

		// We should not try VINA unless we have a vin!
		// Check whether Online VINA is needed
		if (liNoOfRecs == 0 && lsKeyType.equals(CommonConstant.VIN))
		{
			//In any case, get the VINA number
			//lsTransactionId = "R10";
			lsTransactionId = MfAccess.R10;
			try
			{
				lsMfVINAResponse =
					getMfResponse(lsTransactionId, lsMfKeys);
			}
			catch (RTSException aeRTSEx)
			{
				laVehicleInquiryData.setMfDown(liMF_DOWN);
				return laVehicleInquiryData;
			}
			if ((lsMfVINAResponse != null)
				&& !(lsMfVINAResponse
					.equals(CommonConstant.STR_SPACE_EMPTY)))
			{
				// defect 8983
				//	Get output length from APPCHeader
				//	Set number of records
				liOutputLength =
					laAppcHeader.rtnMfOutputLength(lsMfVINAResponse);
				//				liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
				//				liOutputLengthLength = laAppcHeader.getHDOUTLEN();
				//				liOutputLength =
				//					Integer.parseInt(
				//						lsMfVINAResponse.substring(
				//							liOutputLengthOffset,
				//							liOutputLengthOffset
				//								+ liOutputLengthLength));
				lsMfVINAResponse =
					lsMfVINAResponse.substring(
						HEADER_LENGTH,
						HEADER_LENGTH + liOutputLength);
			}
		}
		if ((lsMfTtlRegResponse != null)
			&& !(lsMfTtlRegResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// check for REGIS event
			// check whether NoOfRecs = 1 and MultipleRegIndi = '1'
			if (liNoOfRecs == 1)
			{
				// defect 6701
				//	Get MultipleRegIndi from getMultipleRegJunkIndi
				//	Set MultipleReg boolean 
				boolean lbMultipleReg = true;
				char lcMultipleRegIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				//				char lcMultipleRegIndi =
				//					getStringFromZonedDecimal(
				//						lsMfTtlRegResponse).charAt(
				//						MULTIPLE_REG_INDI_POSITION);
				// end defect 6701
				if ((liNoOfRecs == 1) && (lcMultipleRegIndi == '1'))
				{
					if (lsKeyType.equals(CommonConstant.DOC_NO))
					{
						//lsKeyType = "DOCNO"; 
						lsTransactionId = MfAccess.R33;
						//set the MfKeys sent to MF: DocNo is set	
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								lsKey,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					else if (
						lsKeyType.equals(CommonConstant.REG_PLATE_NO))
					{
						//lsKeyType = "REGPLTNO";
						// TODO this must be the wrong transaction id.
						lsTransactionId = MfAccess.R29;
						//set the MfKeys sent to MF: RegPltNo is set	
						lsMfKeys =
							setupMfKeysSpclPlt(
								lsKeyType,
								lsKey,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								0,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								lsDummyDate,
								0,
								CommonConstant.STR_SPACE_EMPTY,
								CommonConstant.STR_SPACE_EMPTY,
								liTier);
						// end defect 9086
					}
					try
					{
						lsMfRegisResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSEx)
					{
						laVehicleInquiryData.setMfDown(liMF_DOWN);
						return laVehicleInquiryData;
					}
				}
				// Get JUNK if JnkIndi = 1 and NoOfRecs = 1
				// defect 6701
				//	Get MultipleRegIndi from getMultipleRegJunkIndi
				//	Set MultipleReg boolean 
				lbMultipleReg = false;
				char lcJunkIndi =
					getMultipleRegJunkIndi(
						lsMfTtlRegResponse,
						lbMultipleReg);
				// end defect 6701
				if ((liNoOfRecs == 1) && (lcJunkIndi == '1'))
				{
					lsTransactionId = MfAccess.R28;
					//CHANGE: Have to set doc no from prev response as key
					try
					{
						lsMfJunkResponse =
							getMfResponse(lsTransactionId, lsMfKeys);
					}
					catch (RTSException aeRTSEx)
					{
						laVehicleInquiryData.setMfDown(liMF_DOWN);
						return laVehicleInquiryData;
					}
				}
			} // end of NoOfRecs == 1
			else if (liNoOfRecs > 1)
			{
				lsMfPartialResponse = lsMfResponse;
				lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
			}
		} // end of if
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);
		//set VINAExists based on the response 
		if (lsMfVINAResponse == null
			|| lsMfVINAResponse.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			laVehicleInquiryData.setVINAExists(false);
		}
		else
		{
			laVehicleInquiryData.setVINAExists(true);
		}
		return laVehicleInquiryData;
	}
	/**
	 * Retrieves VINA information from the TxDOT mainframe
	 *
	 * Uses Key1 and Key2 
	 * 
	 * @param aaGSD GeneralSearchData 
	 * @return VehicleInquiryData
	 * @deprecated
	 */
	public VehicleInquiryData retrieveVehicleFromVINAOnly(GeneralSearchData aaGSD)
	{
		//TODO review use of this method!  It is not called.
		String lsKeyType = aaGSD.getKey1();
		String lsKey = aaGSD.getKey2();
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		//Set the MF_Down value
		final int MF_DOWN = 1;
		//Set the MFError String
		csMFError = aaGSD.getKey5();
		//get appcheader to ge the length of appcheader
		APPCHeader laAppcHeader = new APPCHeader();
		final int liHEADER_LENGTH =
			laAppcHeader.getAPPCHeaderRecord().length;
		int liOutputLength = 0;
		/*
		 * Define OFFSETS of indicators in the Mainframe response
		 */
		// defect 6701
		// Offsets have been move to the class level
		//final int MULTIPLE_REG_INDI_POSITION = 1449;
		//final int JNK_INDI_POSITION = 968;
		// end defect 6701
		/*
		 * Define the response strings 
		 */
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9086

		//create the return object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		// set up the keys to be sent to the mainframe
		RTSDate laRTSDummyDate = new RTSDate();
		String lsDummyDate =
			Integer.toString(laRTSDummyDate.getYYYYMMDDDate());
		String lsMfKeys = new String();
		//set the up the input to be sent to the mainframe
		// defect 9086
		//	New format for MfKeys
		//		Active/Inactive
		int liTier = aaGSD.getIntKey2();
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsKey,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);

		// end defect 9086
		lsTransactionId = MfAccess.R10;
		try
		{
			lsMfVINAResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (RTSException aeRTSE)
		{
			laVehicleInquiryData.setMfDown(MF_DOWN);
			return laVehicleInquiryData;
		}
		if ((lsMfVINAResponse != null)
			&& !(lsMfVINAResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// defect 8983
			//	Get output length from APPCHeader
			//	Set number of records
			liOutputLength =
				laAppcHeader.rtnMfOutputLength(lsMfVINAResponse);
			//			liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
			//			liOutputLengthLength = laAppcHeader.getHDOUTLEN();
			//			liOutputLength =
			//				Integer.parseInt(
			//					lsMfVINAResponse.substring(
			//						liOutputLengthOffset,
			//						liOutputLengthOffset + liOutputLengthLength));
			// end defect 8983

			lsMfVINAResponse =
				lsMfVINAResponse.substring(
					liHEADER_LENGTH,
					liHEADER_LENGTH + liOutputLength);
		}
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);
		//set VINAExists based on the response 
		if (lsMfVINAResponse == null
			|| lsMfVINAResponse.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			laVehicleInquiryData.setVINAExists(false);
		}
		else
		{
			laVehicleInquiryData.setVINAExists(true);
		}
		return laVehicleInquiryData;
	}
	/**
	 * Retrieves the junk records associated with a vehicle
	 * 
	 * @param aaVehicle VehicleInquiryData
	 * @param asMFError String 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData retrieveJunkRecords(
		VehicleInquiryData aaVehicle,
		String asMFError)
	{
		String lsKeyType = CommonConstant.DOC_NO;
		String lsKey =
			aaVehicle.getMfVehicleData().getTitleData().getDocNo();
		//String lsTransactionId = "R28";
		String lsTransactionId = MfAccess.R28;
		//Set the MF_DOWN value
		final int MF_DOWN = 1;
		//Set the MFError String
		csMFError = asMFError;
		// Define the response strings 
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;

		// defect 9086
		//	Create a empty SpclPltRegis String
		String lsSpclPltRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9086

		//create the return object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		// set up the keys to be sent to the mainframe
		RTSDate laRTSDummyDate = new RTSDate();
		String lsDummyDate =
			Integer.toString(laRTSDummyDate.getYYYYMMDDDate());
		String lsMfKeys = new String();
		//set up the input to be sent to the mainframe
		// defect 9086
		//	New format for MfKeys
		int liTier = 0; // Search Active/Inactive
		lsMfKeys =
			setupMfKeysSpclPlt(
				lsKeyType,
				lsKey,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086
		try
		{
			//Get Response from MF
			lsMfJunkResponse = getMfResponse(lsTransactionId, lsMfKeys);
			laVehicleInquiryData =
				setVehicleInquiryDataFromMfResponse(
					lsMfTtlRegResponse,
					lsMfVINAResponse,
					lsMfRegisResponse,
					lsMfSpecialOwnerResponse,
					lsMfJunkResponse,
					lsMfPartialResponse,
					lsSpclPltRegisResponse);
		}
		catch (RTSException aeRTSE)
		{
			laVehicleInquiryData.setMfDown(MF_DOWN);
			return laVehicleInquiryData;
		}
		laVehicleInquiryData =
			setVehicleInquiryDataFromMfResponse(
				lsMfTtlRegResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsSpclPltRegisResponse);

		return laVehicleInquiryData;
	}

	/**
	 * Set Number Of records from MFAccess call
	 * 
	 * @param asMfResponse	String - Header/Vehicle data
	 * @param aiOffSet	int - Record Offset
	 * @return int
	 */
	public int rtnNoOfRecsMF(String asMfResponse, int aiOffSet)
	{
		int liNoOfRecs = 0;
		final int NO_OF_RECS_OFFSET = aiOffSet;
		final int NO_OF_RECS_LENGTH = 3;
		liNoOfRecs =
			Integer
				.valueOf(
					getStringFromZonedDecimal(
						asMfResponse.substring(
							NO_OF_RECS_OFFSET,
							NO_OF_RECS_OFFSET + NO_OF_RECS_LENGTH)))
				.intValue();

		return liNoOfRecs;
	}

	/**
	 * Sends FundsUpdate data to the mainframe and gets the TraceNo back
	 * 
	 * @param aaFundsUpdateData FundsUpdateData
	 * @return int
	 */
	public int sendFundsUpdateToMF(FundsUpdateData aaFundsUpdateData)
		throws RTSException
	{
		// defect 10244
		//	Copy code from V version to update sendFundsUpdateToMF and 
		//	to retain comments. end defect marker at end of method.
		// defect 9557
		//	 Adjust offset from 148 to 149
		//	Rename variables iERR_MSG_AREA_OFFSET, 
		//	lsFundsUpdateResponse to there usage names
		//			final int liERR_MSG_AREA_OFFSET = 149;
		final int TRACE_NO_OFFSET = 149;
		// end defect 9557

		// Set the update data to be sent
		//define the lenths and offsets of fields for FDSUPDA
		final int COMPT_CNTY_NO_LENGTH = 3;
		final int FUNDS_PYMNT_DATE_LENGTH = 8;
		final int TRACE_NO_LENGTH = 9;
		final int TRANS_EMP_ID_LENGTH = 7;
		final int OFC_ISSUANCE_NO_LENGTH = 3;
		final int PYMNT_TYPE_CD_LENGTH = 2;
		final int PYMNT_STATUS_CD_LENGTH = 1;
		final int CK_NO_LENGTH = 7;
		//define lengths for FUDSUPDB
		final int FUNDS_RPT_DATE_LENGTH = 8;
		final int RPTNG_DATE_LENGTH = 8;
		final int PYMNT_AMT_LENGTH = 11;
		final int FUNDS_CAT_LENGTH = 14;

		//create the return object
		int liTraceNo = Integer.MIN_VALUE;

		//define return value
		// defect 10462
		if (SystemProperty.isDB2LogMFReq())
		{
			caMFReqData = new MFRequestData();
			caMFReqData.setReqKey("FDSUPDA");
			String lsStatusCd = aaFundsUpdateData.getPaymentStatusCd();
			lsStatusCd =
				lsStatusCd.equals(PaymentStatusCodesCache.REMITTED)
					? "REMIT"
					: "VOID";
			caMFReqData.setParm1(lsStatusCd);
			caMFReqData.setDateParm1(
				aaFundsUpdateData.getFundsPaymentDate());
		}
		// end defect 10462  

		// define the TransactionId to be R29
		String lsTransactionId = R29;

		//define the strings to be sent to MF
		String lsBufferDescArea = CommonConstant.STR_SPACE_EMPTY;

		//this defines the format of the input
		String lsUpdateInput = CommonConstant.STR_SPACE_EMPTY;

		StringBuffer laFundsUpdate = new StringBuffer();

		char lchBufferChar = ' ';
		char lchBufferInt = '0';

		//define the length of No of FDSUPDB field
		final int liNO_OF_FDSUPDB_LENGTH = 3;

		// Create the Buffer Desc Area
		//find number of FDSUPDB records
		int liNoOfRecords = aaFundsUpdateData.getFundsDue().size();
		String lsNoOfRecords = Integer.toString(liNoOfRecords);

		//set the number of records field
		lsNoOfRecords =
			resizeStringtoLength(
				lsNoOfRecords,
				liNO_OF_FDSUPDB_LENGTH,
				'0',
				ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING);

		//set Buffer Desc Area 
		lsBufferDescArea = FDSUPDATE_DESC_AREA_PREFIX + lsNoOfRecords;

		//fill up the data for FUDSUPDA
		laFundsUpdate.append(
			resizeStringtoLength(
				Integer.toString(aaFundsUpdateData.getComptCountyNo()),
				COMPT_CNTY_NO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//date field
		laFundsUpdate.append(
			resizeStringtoLength(
				Integer.toString(
					aaFundsUpdateData
						.getFundsPaymentDate()
						.getYYYYMMDDDate()),
				FUNDS_PYMNT_DATE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		String lsAccntNoCd =
			setZonedDecimalFromInt(aaFundsUpdateData.getAccountNoCd());
		laFundsUpdate.append(
			lsAccntNoCd.charAt(lsAccntNoCd.length() - 1));
		laFundsUpdate.append(
			resizeStringtoLength(
				Integer.toString(aaFundsUpdateData.getTraceNo()),
				TRACE_NO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		laFundsUpdate.append(
			resizeStringtoLength(
				aaFundsUpdateData.getTransEmpId(),
				TRANS_EMP_ID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		laFundsUpdate.append(
			resizeStringtoLength(
				Integer.toString(aaFundsUpdateData.getOfcIssuanceNo()),
				OFC_ISSUANCE_NO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		laFundsUpdate.append(
			resizeStringtoLength(
				Integer.toString(aaFundsUpdateData.getPaymentTypeCd()),
				PYMNT_TYPE_CD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		laFundsUpdate.append(
			resizeStringtoLength(
				aaFundsUpdateData.getPaymentStatusCd(),
				PYMNT_STATUS_CD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		laFundsUpdate.append(
			resizeStringtoLength(
				aaFundsUpdateData.getCheckNo(),
				CK_NO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//create the dummy object to point to each data obj in the vector
		FundsDueData laFundsDueData = new FundsDueData();

		//get data from FUDSUPDBs and put them in update string
		for (int i = 0;
			i < aaFundsUpdateData.getFundsDue().size();
			i++)
		{
			//get the objects one by one
			laFundsDueData =
				(FundsDueData) aaFundsUpdateData
					.getFundsDue()
					.elementAt(
					i);
			//get the FUDSUPDB data and put them in the string
			//date fields
			laFundsUpdate.append(
				resizeStringtoLength(
					Integer.toString(
						laFundsDueData
							.getFundsReportDate()
							.getYYYYMMDDDate()),
					FUNDS_RPT_DATE_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
			laFundsUpdate.append(
				resizeStringtoLength(
					Integer.toString(
						laFundsDueData
							.getReportingDate()
							.getYYYYMMDDDate()),
					RPTNG_DATE_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
			//dollar amount
			String lsRemitAmt =
				(laFundsDueData.getRemitAmount()).toString();
			StringBuffer laRemitAmt = new StringBuffer();
			if (lsRemitAmt.indexOf("-") == -1)
			{
				int liDecimalPtPosition = lsRemitAmt.indexOf(".");
				laRemitAmt = new StringBuffer(lsRemitAmt);
				laRemitAmt.replace(
					liDecimalPtPosition,
					liDecimalPtPosition + 1,
					CommonConstant.STR_SPACE_EMPTY);
				laFundsUpdate.append(
					resizeStringtoLength(
						laRemitAmt.toString(),
						PYMNT_AMT_LENGTH,
						lchBufferInt,
						ApplicationControlConstants
							.SC_MFA_INSERT_AT_BEGINNING));
			}
			else
			{
				//for negative numbers
				int liDecimalPtPosition = lsRemitAmt.indexOf(".");
				int liMinusSignPosition = lsRemitAmt.indexOf("-");
				laRemitAmt = new StringBuffer(lsRemitAmt);
				laRemitAmt.replace(
					liDecimalPtPosition,
					liDecimalPtPosition + 1,
					CommonConstant.STR_SPACE_EMPTY);
				laRemitAmt.replace(
					liMinusSignPosition,
					liMinusSignPosition + 1,
					CommonConstant.STR_SPACE_EMPTY);
				//get the zoned Deciaml
				String lsZonedDecimal =
					setZonedDecimalFromNegativeNumber(
						laRemitAmt.toString());
				laFundsUpdate.append(
					resizeStringtoLength(
						lsZonedDecimal,
						PYMNT_AMT_LENGTH,
						lchBufferInt,
						ApplicationControlConstants
							.SC_MFA_INSERT_AT_BEGINNING));
			}
			laFundsUpdate.append(
				resizeStringtoLength(
					laFundsDueData.getFundsCategory(),
					FUNDS_CAT_LENGTH,
					lchBufferChar,
					ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		}

		//this is the update string sent to the mainframe
		lsUpdateInput = laFundsUpdate.toString();
		String lsMFResponse = CommonConstant.STR_SPACE_EMPTY;
		try
		{
			// Send Funds update to mainframe
			// Capture the mainframe response
			lsMFResponse =
				sendUpdateToMF(
					lsTransactionId,
					lsBufferDescArea,
					lsUpdateInput);
		}
		catch (Exception aeException)
		{
			RTSException laRTSE =
				new RTSException(RTSException.MF_DOWN_MSG, aeException);
			laRTSE.setMsgType(RTSException.MF_DOWN);
			throw laRTSE;
		}

		// Check the error code returned. If not successful, do not 
		// check for the trace no.
		String lsMfErrCodeReturned =
			lsMFResponse.substring(
				MF_ERROR_TYPE_CD_OFFSET,
				MF_ERROR_TYPE_CD_OFFSET + MF_ERROR_TYPE_CD_LENGTH);

		// defect 10462 
		if (!lsMfErrCodeReturned.equals(MF_SUCCESSFUL))
		{
			if (lsMfErrCodeReturned.equals(MF_LOGIC_ERROR)
				|| lsMfErrCodeReturned.equals(MF_CICS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_ADABAS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_PROGRAM_ERROR))
			{
				//get the county number, WSID, transamdate and transamtime
				int liOfcIssuanceNo =
					aaFundsUpdateData.getComptCountyNo();
				int liWSID = 0;
				String lsTransAMDate =
					Integer.toString((new RTSDate()).getAMDate());
				String lsTransAMTime = CommonConstant.STR_SPACE_EMPTY;
				handleMfError(
					lsMFResponse,
					liOfcIssuanceNo,
					liWSID,
					lsTransAMDate,
					lsTransAMTime);
			}
		}
		else
		{
			// end defect 10462 
			// Output comes in the header errormessage area. so define the
			// and offset and pick up the trace no.
			boolean lbValidOutput = true;

			String lsTraceNo =
				lsMFResponse.substring(
					TRACE_NO_OFFSET,
					TRACE_NO_OFFSET + TRACE_NO_LENGTH);

			for (int i = 0; i < lsTraceNo.length(); i++)
			{
				if (!(Character.isDigit(lsTraceNo.charAt(i))))
				{
					lbValidOutput = false;
				}
			}

			// defect 10244
			// if there was output from the mainframe, get it
			if (lbValidOutput)
			{
				liTraceNo = Integer.parseInt(lsTraceNo);
			}
			// end defect 10244
		}
		return liTraceNo;
	}

	/**
	 * Get adjusted TransTime. This will replace the current time 
	 * stamp (part of the TransId) on the SendTrans records.
	 * 
	 * @param avData	Vector, Hender and SendTrans data
	 * @return Vector
	 */
	private Vector resetTransId(Vector avData)
	{
		// Total number of bytes in a SendTrans records
		int liTotalBytes = 0;
		// Number of bytes for TransId - TransTime
		int liTransIdLen = 17;
		// Number of bytes for TransTime
		int liTransTimeLen = 6;
		Vector lvResponse = avData;

		// Header file, number of records per file
		String lsRecCount =
			(String) lvResponse.get(SENDTRANS_RECORD_COUNT_INDEX);
		// SendTrans data
		String lsData = (String) lvResponse.get(SENDTRANS_DATA_INDEX);

		// Get adjusted TransTime. This will replace the current time 
		//	stamp (part of the TransId) on the SendTrans records.
		RTSDate laRTSDate = new RTSDate();
		String lsAdjTransTime =
			UtilityMethods.addPadding(
				String.valueOf(laRTSDate.getSecond())
					+ String.valueOf(laRTSDate.getMillisecond())
					+ String.valueOf(laRTSDate.getMicroseconds()),
				SENDTRANS_ADJ_TIME_LEN,
				CommonConstant.STR_ZERO);

		// Get record counts for each file
		int liTransCnt = Integer.parseInt(lsRecCount.substring(8, 10));
		int liMvFuncTransCnt =
			Integer.parseInt(lsRecCount.substring(18, 20));
		int liSRFuncTransCnt =
			Integer.parseInt(lsRecCount.substring(28, 30));
		int liInvFuncCnt =
			Integer.parseInt(lsRecCount.substring(38, 40));
		int liFundFuncCnt =
			Integer.parseInt(lsRecCount.substring(48, 50));
		int liTRInvDTLCnt =
			Integer.parseInt(lsRecCount.substring(58, 60));
		int liTRFDSDTLCnt =
			Integer.parseInt(lsRecCount.substring(68, 70));
		// Payment and LogFuncTrans does not have a TransId.
		//	No adjustment needed for these files 

		// Start the process to replace the time stamp for each TransId
		// Trans
		if (liTransCnt == 1)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			String lsData1 = lsData.substring(0, liBytes);
			String lsData2 = lsData.substring(liBytes + liTransTimeLen);
			lsData = lsData1 + lsAdjTransTime + lsData2;
			// Add file byte length to total byte accumulator
			liTotalBytes = liTotalBytes + 384;
		}
		// MvFuncTrans
		if (liMvFuncTransCnt == 1)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			String lsData1 = lsData.substring(0, liBytes);
			String lsData2 = lsData.substring(liBytes + liTransTimeLen);
			lsData = lsData1 + lsAdjTransTime + lsData2;
			// Add file byte length to total byte accumulator
			liTotalBytes = liTotalBytes + 1656;
		}
		// SRFuncTrans
		if (liSRFuncTransCnt == 1)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			String lsData1 = lsData.substring(0, liBytes);
			String lsData2 = lsData.substring(liBytes + liTransTimeLen);
			lsData = lsData1 + lsAdjTransTime + lsData2;
			// Add file byte length to total byte accumulator
			liTotalBytes = liTotalBytes + 414;
		}
		// InvFuncTrans
		if (liInvFuncCnt == 1)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			String lsData1 = lsData.substring(0, liBytes);
			String lsData2 = lsData.substring(liBytes + liTransTimeLen);
			lsData = lsData1 + lsAdjTransTime + lsData2;
			// Add file byte length to total byte accumulator
			liTotalBytes = liTotalBytes + 47;
		}
		// FundFunc
		if (liFundFuncCnt == 1)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			String lsData1 = lsData.substring(0, liBytes);
			String lsData2 = lsData.substring(liBytes + liTransTimeLen);
			lsData = lsData1 + lsAdjTransTime + lsData2;
			// Add file byte length to total byte accumulator
			liTotalBytes = liTotalBytes + 133;
		}
		// Set TRInvDTL, This file can have up to 99 records
		if (liTRInvDTLCnt > 0)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			for (int liIndex = 0; liIndex < liTRInvDTLCnt; liIndex++)
			{
				String lsData1 = lsData.substring(0, liBytes);
				String lsData2 =
					lsData.substring(liBytes + liTransTimeLen);
				lsData = lsData1 + lsAdjTransTime + lsData2;
				// Add file byte length to total byte accumulator
				liTotalBytes = liTotalBytes + 90;
			}
		}
		// Set TRFDSDTL, This file can have up to 99 records
		if (liTRFDSDTLCnt > 0)
		{
			int liBytes = liTotalBytes + liTransIdLen;
			for (int liIndex = 0; liIndex < liTRFDSDTLCnt; liIndex++)
			{
				String lsData1 = lsData.substring(0, liBytes);
				String lsData2 =
					lsData.substring(liBytes + liTransTimeLen);
				lsData = lsData1 + lsAdjTransTime + lsData2;
				// Add file byte length to total byte accumulator
				liTotalBytes = liTotalBytes + 93;
			}
		}

		lvResponse.remove(1);
		lvResponse.add(1, lsData);
		return lvResponse;
	}
	/**
	 * Posts transactions stored in the database to the TxDOT mainframe
	 * 
	 * @param aaMFTrans MFTrans
	 * @return int
	 * @throws RTSException
	 */
	public int sendTransaction(MFTrans aaMFTrans) throws RTSException
	{
		//define return value
		int liIsSuccess = MfAccess.TRANSACTION_FAILED;
		//define the TransactionId
		String lsTransactionId = MfAccess.R77;
		//Offsets for MF Update error codes
		// defect 10244
		//	Move variables to class level
		//		final int MF_ERROR_TYPE_CD_OFFSET = 132;
		//		final int MF_ERROR_TYPE_CD_LENGTH = 2;
		// end defect 10244
		Vector lvResponse = null;
		String lsBufferDescArea = CommonConstant.STR_SPACE_EMPTY;
		String lsUpdateInput = CommonConstant.STR_SPACE_EMPTY;

		// defetc 8984
		// SendTrans testing
		if (aaMFTrans.getType() == MFTrans.SENDTRANSTEST)
		{
			// Get SendTrans record.
			// format of Vector
			// 0 - String containing record counts
			// 1 - String containing actual data
			// 2 - containing ofcissuanceno (Empty)
			// 3 - containing workstationid (Empty)
			// 4 - containing trans amdate (Empty)
			// 5 - containing trans time (Empty)
			lvResponse =
				FileUtil.copyFileToVector(aaMFTrans.getFileName());

			lsUpdateInput = (String) lvResponse.elementAt(1);
			// Add items 2 - 5 to vector
			//	2 - Ofcissuanceno
			lvResponse.add(new Integer(lsUpdateInput.substring(6, 9)));
			//	3 - workstationid
			lvResponse.add(new Integer(lsUpdateInput.substring(9, 12)));
			//	4 - trams amdate
			lvResponse.add(lsUpdateInput.substring(12, 18));
			//	5 - transtime
			lvResponse.add(lsUpdateInput.substring(17, 22));

			// defect 10080
			//	If 	laMFTest.csChangeTransTime = 1 then reset TransTime
			//	else leave as is for duplication transaction testing
			if (aaMFTrans.csChangeTransTime == 1)
			{
				// defect 8984
				// Reset TransTime to make transaction unique
				lvResponse = resetTransId(lvResponse);
				// end defect 8984
			}
			// end defect 10080
		}
		// end defect 8984
		else
		{
			// !!!! THIS CODE BLOCK EXCUTES ONLY IF SENDTRANS IS RUNNING
			// defect 6701
			// call new routine to format data
			MfbaSendTransG laMfbaSendTransG = new MfbaSendTransG();
			// format of Vector
			// 0 - String containing record counts
			// 1 - String containing actual data
			// 2 - int containing ofcissuanceno 
			// 3 - int containing workstationid
			// 4 - String containing trans amdate
			// 5 - String containing trans time
			lvResponse =
				laMfbaSendTransG.formatSendTransData(this, aaMFTrans);
		}
		// defect 8984
		//	Add check lvResponse != null.
		//	If null then SendTrans testing (MfAccessTest)
		if (lvResponse.size() > 4)
		{
			// end defect 8984
			// parse the data responses out of the vector
			lsBufferDescArea = (String) lvResponse.elementAt(0);
			lsUpdateInput = (String) lvResponse.elementAt(1);
			//make the mainframe call
			try
			{
				String lsMfResponse =
					sendUpdateToMF(
						lsTransactionId,
						lsBufferDescArea,
						lsUpdateInput);
				String lsMfErrCodeReturned =
					lsMfResponse.substring(
						MF_ERROR_TYPE_CD_OFFSET,
						MF_ERROR_TYPE_CD_OFFSET
							+ MF_ERROR_TYPE_CD_LENGTH);
				if (lsMfErrCodeReturned.equals(MF_SUCCESSFUL))
				{
					liIsSuccess = MfAccess.TRANSACTION_SUCCESSFUL;
				}
				else
				{
					if (lsMfErrCodeReturned.equals(MF_LOGIC_ERROR)
						|| lsMfErrCodeReturned.equals(MF_CICS_ERROR)
						|| lsMfErrCodeReturned.equals(MF_ADABAS_ERROR)
						|| lsMfErrCodeReturned.equals(MF_PROGRAM_ERROR))
					{
						liIsSuccess = MfAccess.TRANSACTION_FAILED;
						// get the county number, WSID, 
						//  transamdate and transamtime
						// defect 6701
						// parse the error data out of the vector
						int liOfcIssuanceNo =
							((Integer) lvResponse.elementAt(2))
								.intValue();
						int liWSID =
							((Integer) lvResponse.elementAt(3))
								.intValue();
						String lsTransAMDate =
							(String) lvResponse.elementAt(4);
						String lsTransAMTime =
							(String) lvResponse.elementAt(5);
						// end defect 6701
						try
						{
							handleMfError(
								lsMfResponse,
								liOfcIssuanceNo,
								liWSID,
								lsTransAMDate,
								lsTransAMTime);
							// defect 6701
							//	}
							// end defect 6701
						}
						catch (Exception aeEx)
						{
							RTSException leRTSEx2 =
								new RTSException(
									RTSException.JAVA_ERROR,
									aeEx);
							throw leRTSEx2;
						}
					}
					else
					{
						liIsSuccess = MfAccess.TRANSACTION_FAILED;
					}
				}
			}
			catch (RTSException aeRTSE)
			{
				System.err.println(
					"MfAccess error "
						+ MF_NET_NAME
						+ " "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
				aeRTSE.printStackTrace();
				liIsSuccess = MfAccess.TRANSACTION_FAILED;
			}
			// defect 6701
		} // end of if
		else
		{
			// If the Vector is empty, we should log it.
			System.err.println(
				"MfAccess error "
					+ MF_NET_NAME
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime()
					+ "Empty Vector for SendTrans");
		}
		// end defect 6701
		return liIsSuccess;
	}
	/**
	 * Sends updates to the mainframe. Used by RU77 and RU29 based
	 * transactions.
	 * 
	 * @param asTransactionId  String  
	 * @param asBufferDescArea String 
	 * @param asUpdateInput String	
	 * @return String  
	 * @throws RTSException MF_DOWN Exception.
	 */
	private String sendUpdateToMF(
		String asTransactionId,
		String asBufferDescArea,
		String asUpdateInput)
		throws RTSException
	{
		// defect 10462 
		int liSuccessfulIndi = 0;
		int liErrorCode = MF_UNKNOWN_ERR_CD;
		RTSDate laReqDate = new RTSDate();
		// end defect 10462  

		// Create the header in APPC header record 
		APPCHeader appcHeader = new APPCHeader();

		// Get the update length to set it in Input Length field 
		//  in the header
		String lsUpdateInputLength =
			resizeStringtoLength(
				Integer.toString(asUpdateInput.length()),
				appcHeader.getHDINTLEN(),
				'0',
				ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING);

		// Set the Transaction Id with the proper version
		StringBuffer laTransId = new StringBuffer(asTransactionId);
		laTransId.insert(TRANS_VERSION_OFFSET, ssMFTransactionVersion);
		asTransactionId = laTransId.toString();

		// defect 10462 
		// Try around all calls; Log to DB2 included in finally
		try
		{
			// Set APPC Header 
			appcHeader.setMainframeHeader(
				asTransactionId,
				getTsqName(),
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				MfAccess.ssMFTimeout,
				MfAccess.FLAG_YES,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				getNetName(),
				lsUpdateInputLength,
				csMFDBName,
				asBufferDescArea);

			try
			{
				caConnection = getConnection();
			}
			catch (RTSException aeRTSE)
			{
				liErrorCode = RETRY_CONNECT_ERR_CD;

				System.err.println(
					"MfAccess error "
						+ MF_NET_NAME
						+ " "
						+ (new RTSDate()).getYYYYMMDDDate()
						+ " "
						+ (new RTSDate()).get24HrTime());
				aeRTSE.printStackTrace();

				throw aeRTSE;
			}

			// initialize comm area based on the definition of the
			// APPC header record already set by
			// <code>setMainframeHeader</code> and the 
			// <code>UpdateInput</code> field.
			byte[] larrCommArea = new byte[COMM_AREA_LENGTH];
			byte[] MfHeader = appcHeader.getAPPCHeaderRecord();

			larrCommArea =
				setupCommArea(larrCommArea, MfHeader, asUpdateInput);

			if (siMFDebugMode == 1)
			{
				printTrace("INPUT:", larrCommArea);
			}

			ECIRequest laRequest = getRequest(larrCommArea);

			caConnection.flow(laRequest);

			String lsMfResponse = new String(laRequest.Commarea);

			caConnection.close();

			if (laRequest.getRc() != 0)
			{
				liErrorCode = ECI_REQUEST_ERR_CD;

				String lsDate =
					Integer.toString((new RTSDate().getYYYYMMDDDate()));
				String lsTime = (new RTSDate()).getClockTime();
				Log.write(
					Log.SQL_EXCP,
					this,
					"ECIRequest to JavaGateway Failed at "
						+ lsDate
						+ " "
						+ lsTime
						+ " "
						+ laRequest.getRcString());
				throw new IOException();
			}

			// TODO (KPH) This processing needs to be synched w/ 
			//             getMfResponse()  (defect 10559) 
			String lsMfErrCodeReturned =
				lsMfResponse.substring(
					MF_ERROR_TYPE_CD_OFFSET,
					MF_ERROR_TYPE_CD_OFFSET + MF_ERROR_TYPE_CD_LENGTH);

			if (lsMfErrCodeReturned.equals(MF_LOGIC_ERROR)
				|| lsMfErrCodeReturned.equals(MF_CICS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_ADABAS_ERROR)
				|| lsMfErrCodeReturned.equals(MF_PROGRAM_ERROR))
			{
				liErrorCode = Integer.parseInt(lsMfErrCodeReturned);
				throw new IOException();
			}

			// defect 10121
			//	Check for RTSJGate error. At end throws IOException
			checkRTSJGateErr(lsMfResponse);
			// end defect 10121

			// allow mf output to console in debug mode for sending
			// if in debug mode, print the response
			if (siMFDebugMode == 1)
			{
				printTrace("OUTPUT:", lsMfResponse);
			}
			// end defect 6701

			liSuccessfulIndi = 1;
			liErrorCode = 0;
			return lsMfResponse;
		}
		// end defect 10462 
		catch (IOException aeIOException)
		{
			// defect 10462 
			if (liErrorCode == MF_UNKNOWN_ERR_CD)
			{
				liErrorCode = MF_IO_EXCEPTION_ERR_CD;
			}
			// end defect 10462 

			// create MF Down Exception 
			RTSException laRTSE =
				new RTSException(
					RTSException.MF_DOWN_MSG,
					aeIOException);
			laRTSE.setMsgType(RTSException.MF_DOWN);
			laRTSE.printStackTrace();

			//Print the error to stderr and then throw the exception
			System.err.println(
				"MfAccess error "
					+ MF_NET_NAME
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).get24HrTime());

			throw laRTSE;
		}
		finally
		{
			// Make sure we close out the connection properly
			if (caConnection != null)
			{
				if (caConnection.isOpen())
				{
					try
					{
						caConnection.close();
					}
					catch (IOException aeIOEx)
					{
						// Just print to system err and continue.
						aeIOEx.printStackTrace();
					}
				}
			}
			// defect 10462 
			// Only log Funds Updates to DB2 
			if (SystemProperty.isDB2LogMFReq()
				&& asTransactionId.substring(2).equals("29")
				&& caMFReqData != null)
			{
				if (SystemProperty.getDB2LogMFReqCd()
					== SystemProperty.DB2_LOGGING_ALL_MF_REQ
					|| liSuccessfulIndi == 0)
				{
					caMFReqData.setCICSTransId(asTransactionId);
					caMFReqData.setSuccessfulIndi(liSuccessfulIndi);
					caMFReqData.setReqTimeStmp(laReqDate);
					caMFReqData.setRespTimestmp(new RTSDate());
					caMFReqData.setErrMsgCd(liErrorCode);
					logDB2MFReq();
				}
			}
			// end defect 10462 

			// null out the connection to make sure
			caConnection = null;

		}
		// end defect 6701
	}

	/**
	 * Check if RTSJGate was return from mainframe.
	 * If true, log to server log and 
	 * @param asMfResponse
	 * @throws IOException
	 */
	private void checkRTSJGateErr(String asMfResponse)
		throws IOException
	{
		// defect 10121
		//	Use return code from CICS to check RTSJGate return code
		//	Log error to VSAM file
		// defect 6701
		// check for CICS.J in the output.  This indicates there
		// was a CICS or JGATE problem.
		//
		// Use JGATE_ERROR_FLAG to test for error. jr
		// Get the error code returned. 
		String lsJGateErrCodeReturned =
			asMfResponse.substring(
				JGATE_ERROR_FLAG_OFFSET,
				JGATE_ERROR_FLAG_OFFSET + JGATE_ERROR_FLAG_LENGTH);

		if (lsJGateErrCodeReturned.equals("Y"))
		{
			// defect 6701
			//	Get CICS - JGate error message
			String lsJGateErrMsgsReturned =
				asMfResponse.substring(
					JGATE_ERROR_MESSAGE_OFFSET,
					JGATE_ERROR_MESSAGE_OFFSET
						+ JGATE_ERROR_MESSAGE_LENGTH);
			//	Get Header TransId
			String lsHeaderTransIdReturned =
				asMfResponse.substring(
					HEADER_TRANSID_OFFSET,
					HEADER_TRANSID_OFFSET + HEADER_TRANSID_LENGTH);

			// Replace "XXXX" with TransId
			lsJGateErrMsgsReturned =
				UtilityMethods.replaceString(
					lsJGateErrMsgsReturned,
					XXXX,
					lsHeaderTransIdReturned.substring(0, 4));
			// end defect 6701
			String lsDate =
				Integer.toString((new RTSDate().getYYYYMMDDDate()));
			String lsTime = (new RTSDate()).getClockTime();

			// print the response for review
			Log.write(Log.SQL_EXCP, this, asMfResponse);

			// print description message
			//"There was a CICS JGATE problem "
			Log.write(
				Log.SQL_EXCP,
				this,
				lsJGateErrMsgsReturned
					+ lsDate
					+ CommonConstant.STR_SPACE_TWO
					+ lsTime);

			// Commented out because this is logged in catch. jr
			// print the response for review
			//Log.write(Log.SQL_EXCP, this, lsMfResponse);
			System.err.println("JGateErrCodeReturned");

			// defect 10121
			//	Write error to the VSAM file
			String lsTransAMDate =
				Integer.toString((new RTSDate()).getAMDate());
			String lsTransAMTime = (new RTSDate()).getTime();
			handleMfError(
				asMfResponse,
				0,
				0,
				lsTransAMDate,
				lsTransAMTime);
			// end defect 10121

			throw new IOException();
		}
	}

	/**
	 * Assign data from MF to PermitData
	 * 
	 * @param asMfResponse
	 * @param aiNumRecs
	 * @return PermitData 
	 */
	public PermitData setMFPermitDataFromMfResponse(
		String asMfResponse,
		int aiNumRecs)
	{
		PermitData laPrmtData = new PermitData();

		if (aiNumRecs == 1)
		{
			MfbaPermitG laMfbaPermitG = new MfbaPermitG();
			laPrmtData =
				laMfbaPermitG.setPermitDataFromMfResponse(asMfResponse);
		}
		else
		{
			MfbaPartialPermitDataG laMfbaPartialPermitG =
				new MfbaPartialPermitDataG();

			Vector lvPartial =
				laMfbaPartialPermitG
					.setMfPartialPermitDatafromMfResponse(
					asMfResponse);
			laPrmtData.setPartialPrmtDataList(lvPartial);
		}
		laPrmtData.setNoMFRecs(aiNumRecs);

		return laPrmtData;
	}

	/**
	 * Sets and returns the MFVehicleData object from the mainframe
	 * response. 
	 * 
	 * @param asMfTtlRegResponse String
	 * @param asMfVINAResponse String
	 * @param asMfSpecialOwnerResponse String
	 * @param asMfJunkResponse String
	 * @param asSpclPltRegis	String
	 * 
	 * @return MFVehicleData
	 */
	private MFVehicleData setMFVehicleDataFromMfResponse(
		String asMfResponse,
		String asMfVINAResponse,
		String asMfSpecialOwnerResponse,
		String asMfJunkResponse,
		String asSpclPltRegisResponse)
		throws RTSException
	{
		// Construct MFVehicleData
		//create the Return object
		MFVehicleData laMFVehicleData = new MFVehicleData();
		//Create Data objects to be added to MfVehicleData
		VehicleData laVehicleData = new VehicleData();
		RegistrationData laRegistrationData = new RegistrationData();
		OwnerData laOwnerData = new OwnerData();
		TitleData laTitleData = new TitleData();
		//SalvageData laSalvageData = new SalvageData();
		Vector laSalvageContainer = new Vector();
		// Special Plate regis
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();

		//	Remove header from MfResponse
		String lsMfTtlRegResponse = getMfTtlRegResponse(asMfResponse);

		//	Check for Special Plate regis data
		//	add data to data object
		// check if Special Plate data
		if (asSpclPltRegisResponse != null
			&& !(asSpclPltRegisResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// Remove header
			String lsMfSpecialPltRegisResponse =
				getMfTtlRegResponse(asSpclPltRegisResponse);
			// Get the Special Plate objects from the main frame 
			//	response
			MfbaSpecialPlateRegisG laMfbaSpclPltRegisG =
				new MfbaSpecialPlateRegisG();
			laSpclPltRegisData =
				laMfbaSpclPltRegisG.setSpecialPlatesRegisDataFromMF(
					lsMfSpecialPltRegisResponse);
			// Clean up objects
			laMfbaSpclPltRegisG = null;
		}
		// end defect 9086	

		if (lsMfTtlRegResponse != null
			&& !(lsMfTtlRegResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			// Get the VehicleData objects from the main frame 
			//	response
			MfbaVehicleG laMfbaVehicleG = new MfbaVehicleG();
			laVehicleData =
				laMfbaVehicleG.setVehicleDataFromMF(lsMfTtlRegResponse);
			// Clean up of objects
			laMfbaVehicleG = null;
			// This is the registration data that is obtained from 
			// the first mainframe call this would be set anyway.
			// also note that this is set from lsMfResponse (RT01) 
			// NOT getREGIS (Ru33)
			//
			// Get the RegistrationData obejects from the main frame
			//	response
			MfbaRegistrationG laMfbaRegistrationData =
				new MfbaRegistrationG();
			laRegistrationData =
				laMfbaRegistrationData.setRegistrationDataFromMF(
					lsMfTtlRegResponse);
			// Clean up of object
			laMfbaRegistrationData = null;
			// Set the owner data from MfResponse
			MfbaOwnerG laMfbaOwnerData = new MfbaOwnerG();
			laOwnerData =
				laMfbaOwnerData.setOwnerDataFromMF(lsMfTtlRegResponse);
			// Clean up of object
			laMfbaOwnerData = null;
			// Set the title data from MfResponse
			MfbaTitleG laMfbaTitleDataG = new MfbaTitleG();
			laTitleData =
				laMfbaTitleDataG.setTitleDataFromMF(lsMfTtlRegResponse);
			// Clean up of object
			laMfbaTitleDataG = null;
		}

		// Parse Junk data from MFJunkResponse
		if (!(asMfJunkResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfJunkResponse != null))
		{
			MfbaSalvageG laSalvageG = new MfbaSalvageG();
			laSalvageContainer =
				laSalvageG.setMultipleSalvageDataFromMf(
					asMfJunkResponse);
			// Clean up of object
			laSalvageG = null;
			int liJunkIndi = 1;
			laMFVehicleData.setJnkIndi(liJunkIndi);
		}

		else
		{
			SalvageData laSalvageData = new SalvageData();
			laSalvageContainer.addElement(laSalvageData);
		}

		// Parse VINA data from MFResponse
		//	Return object
		if (!(asMfVINAResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfVINAResponse != null)
			&& lsMfTtlRegResponse.equals(CommonConstant.STR_SPACE_EMPTY)
			&& asMfSpecialOwnerResponse.equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			MfbaVinaG laVinaG = new MfbaVinaG();
			laMFVehicleData =
				laVinaG.setMfVehicleDataFromVINAResponse(
					asMfVINAResponse);
			// Clean up of object
			laVinaG = null;
			return laMFVehicleData;
		}

		//}
		// end defect 6701
		// Add all data objects to MfVehicleData
		// Add VehicleData
		laMFVehicleData.setVehicleData(laVehicleData);
		//add Registration data
		laMFVehicleData.setRegData(laRegistrationData);
		// Add OwnerData
		laMFVehicleData.setOwnerData(laOwnerData);
		// Add TitleData
		// defect 9987
		laTitleData.setupCertfdLienInfo();
		// end defect 9987 
		laMFVehicleData.setTitleData(laTitleData);
		// Add SalvageData
		laMFVehicleData.setVctSalvage(laSalvageContainer);
		// defect 9086
		//	Add Special Plt Regis to MfVehicleData
		//	Change getSpclPlatesReg() to getSpclPltRegisData()
		//	Set SpclPltRegisData = null if no data
		if (asSpclPltRegisResponse == null
			|| (asSpclPltRegisResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			laMFVehicleData.setSpclPltRegisData(null);
		}
		else
		{
			laMFVehicleData.setSpclPltRegisData(laSpclPltRegisData);
		}
		// end defect 9086

		// Return result
		return laMFVehicleData;
	}
	//	/**
	//	 * Sets and returns multiple funds due data objects from the
	//	 * mainframe response string. 
	//	 * 
	//	 * @return FundsDueDataList
	//	 * @param asMfFundsARResponse String
	//	 * @deprecated
	//	 */
	//	private Vector setMultipleFundsDueDataFromMf(String asMfFundsARResponse)
	//	{
	//		// create the retrun object 
	//		Vector laFundsDueDataContainer = new Vector();
	//		// the length of one FUNDS A/R record from mainframe. 
	//		final int FUNDSAR_RECORD_LENGTH = 72;
	//		// Find the number of elements from output length. the # 
	//		//  in header is not correct. 	
	//		final int OUPUT_LENGTH_OFFSET = 115;
	//		final int OUTPUT_LENGTH_LENGTH = 5;
	//		final int NUMBER_OF_ELEMENTS =
	//			Integer.parseInt(
	//				asMfFundsARResponse.substring(
	//					OUPUT_LENGTH_OFFSET,
	//					OUPUT_LENGTH_OFFSET + OUTPUT_LENGTH_LENGTH))
	//				/ FUNDSAR_RECORD_LENGTH;
	//		//remove the header from the response
	//		final int liHEADER_LENGTH = 256;
	//		asMfFundsARResponse =
	//			asMfFundsARResponse.substring(liHEADER_LENGTH);
	//		//define all offsets and lengths
	//		final int COMPT_CNTY_NO_OFFSET = 0;
	//		final int COMPT_CNTY_NO_LENGTH = 3;
	//		final int FUNDS_RPT_DATE_OFFSET = 3;
	//		final int FUNDS_RPT_DATE_LENGTH = 8;
	//		final int RPTNG_DATE_OFFSET = 11;
	//		final int RPTNG_DATE_LENGTH = 8;
	//		final int FUNDS_CAT_OFFSET = 19;
	//		final int FUNDS_CAT_LENGTH = 14;
	//		final int FUNDS_RCVNG_ENT_OFFSET = 33;
	//		final int FUNDS_RCVNG_ENT_LENGTH = 9;
	//		final int FUNDS_RCVD_AMT_OFFSET = 42;
	//		final int FUNDS_RCVD_AMT_LENGTH = 11;
	//		final int FUNDS_RCVD_AMT_DECIMAL = 2;
	//		final int FUNDS_DUE_DATE_OFFSET = 53;
	//		final int FUNDS_DUE_DATE_LENGTH = 8;
	//		final int ENT_DUE_AMT_OFFSET = 61;
	//		final int ENT_DUE_AMT_LENGTH = 11;
	//		final int ENT_DUE_AMT_DECIMAL = 2;
	//		// the offset based on the record that is picked from the mf
	//		//  response
	//		int liRecordOffset = 0;
	//		if (!(asMfFundsARResponse
	//			.equals(CommonConstant.STR_SPACE_EMPTY))
	//			&& (asMfFundsARResponse != null))
	//		{
	//			for (int i = 0; i < NUMBER_OF_ELEMENTS; i++)
	//			{
	//				//set the record offset
	//				liRecordOffset = i * FUNDSAR_RECORD_LENGTH;
	//				//create registration data
	//				FundsDueData laFundsDueData = new FundsDueData();
	//				//set all values from FundsDueData
	//				laFundsDueData.setComptCountyNo(
	//					Integer
	//						.valueOf(
	//							getStringFromZonedDecimal(
	//								asMfFundsARResponse.substring(
	//									liRecordOffset
	//										+ COMPT_CNTY_NO_OFFSET,
	//									liRecordOffset
	//										+ COMPT_CNTY_NO_OFFSET
	//										+ COMPT_CNTY_NO_LENGTH)))
	//						.toString());
	//				laFundsDueData.setFundsCategory(
	//					trimMfString(
	//						asMfFundsARResponse.substring(
	//							liRecordOffset + FUNDS_CAT_OFFSET,
	//							liRecordOffset
	//								+ FUNDS_CAT_OFFSET
	//								+ FUNDS_CAT_LENGTH)));
	//				laFundsDueData.setFundsReceivingEntity(
	//					trimMfString(
	//						asMfFundsARResponse.substring(
	//							liRecordOffset + FUNDS_RCVNG_ENT_OFFSET,
	//							liRecordOffset
	//								+ FUNDS_RCVNG_ENT_OFFSET
	//								+ FUNDS_RCVNG_ENT_LENGTH)));
	//				//set all dates
	//				laFundsDueData.setFundsReportDate(
	//					new RTSDate(
	//						RTSDate.YYYYMMDD,
	//						Integer
	//							.valueOf(
	//								getStringFromZonedDecimal(
	//									asMfFundsARResponse.substring(
	//										liRecordOffset
	//											+ FUNDS_RPT_DATE_OFFSET,
	//										liRecordOffset
	//											+ FUNDS_RPT_DATE_OFFSET
	//											+ FUNDS_RPT_DATE_LENGTH)))
	//							.intValue()));
	//				laFundsDueData.setReportingDate(
	//					new RTSDate(
	//						RTSDate.YYYYMMDD,
	//						Integer
	//							.valueOf(
	//								getStringFromZonedDecimal(
	//									asMfFundsARResponse.substring(
	//										liRecordOffset
	//											+ RPTNG_DATE_OFFSET,
	//										liRecordOffset
	//											+ RPTNG_DATE_OFFSET
	//											+ RPTNG_DATE_LENGTH)))
	//							.intValue()));
	//				laFundsDueData.setFundsDueDate(
	//					new RTSDate(
	//						RTSDate.YYYYMMDD,
	//						Integer
	//							.valueOf(
	//								getStringFromZonedDecimal(
	//									asMfFundsARResponse.substring(
	//										liRecordOffset
	//											+ FUNDS_DUE_DATE_OFFSET,
	//										liRecordOffset
	//											+ FUNDS_DUE_DATE_OFFSET
	//											+ FUNDS_DUE_DATE_LENGTH)))
	//							.intValue()));
	//				//set all amuonts
	//				StringBuffer laFundsRcvdAmtBuffer =
	//					new StringBuffer(
	//						getStringFromZonedDecimal(
	//							asMfFundsARResponse.substring(
	//								liRecordOffset + FUNDS_RCVD_AMT_OFFSET,
	//								liRecordOffset
	//									+ FUNDS_RCVD_AMT_OFFSET
	//									+ FUNDS_RCVD_AMT_LENGTH)));
	//				if (!(laFundsRcvdAmtBuffer.charAt(0) == '-'))
	//				{
	//					laFundsRcvdAmtBuffer.insert(
	//						FUNDS_RCVD_AMT_LENGTH - FUNDS_RCVD_AMT_DECIMAL,
	//						'.');
	//				}
	//				else
	//				{
	//					laFundsRcvdAmtBuffer.insert(
	//						FUNDS_RCVD_AMT_LENGTH
	//							- FUNDS_RCVD_AMT_DECIMAL
	//							+ 1,
	//						'.');
	//				}
	//				Dollar laFundsRcvdAmt =
	//					new Dollar(laFundsRcvdAmtBuffer.toString());
	//				laFundsDueData.setFundsReceivedAmount(laFundsRcvdAmt);
	//				StringBuffer laEntDueAmtBuffer =
	//					new StringBuffer(
	//						getStringFromZonedDecimal(
	//							asMfFundsARResponse.substring(
	//								liRecordOffset + ENT_DUE_AMT_OFFSET,
	//								liRecordOffset
	//									+ ENT_DUE_AMT_OFFSET
	//									+ ENT_DUE_AMT_LENGTH)));
	//				if (!(laEntDueAmtBuffer.charAt(0) == '-'))
	//				{
	//					laEntDueAmtBuffer.insert(
	//						ENT_DUE_AMT_LENGTH - ENT_DUE_AMT_DECIMAL,
	//						'.');
	//				}
	//				else
	//				{
	//					laEntDueAmtBuffer.insert(
	//						ENT_DUE_AMT_LENGTH - ENT_DUE_AMT_DECIMAL + 1,
	//						'.');
	//				}
	//				Dollar laEntDueAmt =
	//					new Dollar(laEntDueAmtBuffer.toString());
	//				laFundsDueData.setEntDueAmount(laEntDueAmt);
	//				laFundsDueDataContainer.addElement(laFundsDueData);
	//			}
	//		} // end of if
	//		return laFundsDueDataContainer;
	//	}
	//	/**
	//	 * Sets and returns owner data from MF Owner mainframe response. 
	//	 * 
	//	 * @return OwnerData
	//	 * @param asMfOwnerResponse String
	//	 * @deprecated
	//	 */
	//	private OwnerData setOwnerDataFromMfOwner(String asMfOwnerResponse)
	//	{
	//		//create the return object
	//		OwnerData laOwnerData = new OwnerData();
	//		if (!(asMfOwnerResponse.equals(CommonConstant.STR_SPACE_EMPTY))
	//			&& (asMfOwnerResponse != null))
	//		{
	//			//define all lengths and offsets (from doc 660)
	//			final int OWNR_ID_OFFSET = 0;
	//			final int OWNR_ID_LENGTH = 9;
	//			final int OWNR_TTL_NAME1_OFFSET = 9;
	//			final int OWNR_TTL_NAME1_LENGTH = 30;
	//			final int OWNR_TTL_NAME2_OFFSET = 39;
	//			final int OWNR_TTL_NAME2_LENGTH = 30;
	//			final int OWNR_ST1_OFFSET = 69;
	//			final int OWNR_ST1_LENGTH = 30;
	//			final int OWNR_ST2_OFFSET = 99;
	//			final int OWNR_ST2_LENGTH = 30;
	//			final int OWNR_CITY_OFFSET = 129;
	//			final int OWNR_CITY_LENGTH = 19;
	//			final int OWNR_STATE_OFFSET = 148;
	//			final int OWNR_STATE_LENGTH = 2;
	//			final int OWNR_ZPCD_OFFSET = 150;
	//			final int OWNR_ZPCD_LENGTH = 5;
	//			final int OWNR_ZPCDP4_OFFSET = 155;
	//			final int OWNR_ZPCDP4_LENGTH = 4;
	//			final int OWNR_CNTRY_OFFSET = 159;
	//			final int OWNR_CNTRY_LENGTH = 4;
	//			final int PRIVACY_OPT_CD_OFFSET = 163;
	//			final int PRIVACY_OPT_CD_LENGTH = 1;
	//			laOwnerData.setOwnrId(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_ID_OFFSET,
	//						OWNR_ID_OFFSET + OWNR_ID_LENGTH)));
	//			laOwnerData.setName1(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_TTL_NAME1_OFFSET,
	//						OWNR_TTL_NAME1_OFFSET
	//							+ OWNR_TTL_NAME1_LENGTH)));
	//			laOwnerData.setName2(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_TTL_NAME2_OFFSET,
	//						OWNR_TTL_NAME2_OFFSET
	//							+ OWNR_TTL_NAME2_LENGTH)));
	//			laOwnerData.setPrivacyOptCd(
	//				Integer
	//					.valueOf(
	//						getStringFromZonedDecimal(
	//							asMfOwnerResponse.substring(
	//								PRIVACY_OPT_CD_OFFSET,
	//								PRIVACY_OPT_CD_OFFSET
	//									+ PRIVACY_OPT_CD_LENGTH)))
	//					.intValue());
	//			//set the address
	//			AddressData laAddressData = new AddressData();
	//			laAddressData.setCity(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_CITY_OFFSET,
	//						OWNR_CITY_OFFSET + OWNR_CITY_LENGTH)));
	//			laAddressData.setCntry(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_CNTRY_OFFSET,
	//						OWNR_CNTRY_OFFSET + OWNR_CNTRY_LENGTH)));
	//			laAddressData.setSt1(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_ST1_OFFSET,
	//						OWNR_ST1_OFFSET + OWNR_ST1_LENGTH)));
	//			laAddressData.setSt2(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_ST2_OFFSET,
	//						OWNR_ST2_OFFSET + OWNR_ST2_LENGTH)));
	//			laAddressData.setState(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_STATE_OFFSET,
	//						OWNR_STATE_OFFSET + OWNR_STATE_LENGTH)));
	//			laAddressData.setZpcd(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_ZPCD_OFFSET,
	//						OWNR_ZPCD_OFFSET + OWNR_ZPCD_LENGTH)));
	//			laAddressData.setZpcdp4(
	//				trimMfString(
	//					asMfOwnerResponse.substring(
	//						OWNR_ZPCDP4_OFFSET,
	//						OWNR_ZPCDP4_OFFSET + OWNR_ZPCDP4_LENGTH)));
	//			laOwnerData.setAddressData(laAddressData);
	//		} // end of if
	//		return laOwnerData;
	//	}

	/**
	 * Sets the tsqname. 
	 * 
	 * @param aaNewTsqName String
	 */
	public void setTsqName(java.lang.String aaNewTsqName)
	{
		csTSQName = aaNewTsqName;
	}
	/**
	 * Prepares the commarea to be sent to the Mainframe. Initializes
	 * ommarea and copies the header information. 
	 * 
	 * @return byte[]
	 * @param aarrCommArea byte[] CommArea that needs to be prepared 
	 * and sent to the Mainframe. 
	 * @param aarrMainframeHeader byte[] Header information copied to 
	 * CommArea
	 * @param asPOSInput Contains the string that represents the POS 
	 * Input. This could be either MfKeys (in case of a Inquiry) or
	 * UserData (in case of an update). 
	 */
	private byte[] setupCommArea(
		byte[] aarrCommArea,
		byte[] aarrMainframeHeader,
		String asPOSInput)
	{
		//initialize commarea
		for (int i = 0; i < aarrCommArea.length; i++)
		{
			aarrCommArea[i] = Byte.parseByte("00");
		}
		//copy the header in commarea
		for (int i = 0; i < aarrMainframeHeader.length; i++)
		{
			aarrCommArea[i] = aarrMainframeHeader[i];
		}
		//convert the POS input to all uppercase
		asPOSInput = asPOSInput.toUpperCase();
		//copy the POSInput into the comm area
		byte[] laPOSInput = asPOSInput.getBytes();
		//make a byte array first
		int liEndofUserData =
			laPOSInput.length + aarrMainframeHeader.length;
		for (int i = aarrMainframeHeader.length;
			i < liEndofUserData;
			i++)
		{
			aarrCommArea[i] =
				laPOSInput[i - aarrMainframeHeader.length];
		}
		return aarrCommArea;
	}

	//	/**
	//	 * Sets up the MfKeys record that is sent to the mainframe for
	//	 * queries.
	//	 * <p>May return null.
	//	 * 
	//	 * Note: This method will be depercated when Special Plates full
	//	 *        	  goes statewide.
	//	 * 
	//	 * @return String
	//	 * @param aiMFReqType int Mainframe request type (always 00)
	//	 * @param asMFReqKey  String Mainframe request key type for search
	//	 * @param asDocNo String Document number for vehicle (on server)
	//	 * @param asOwnrId String Tax Id (not used for look up anymore) 
	//	 * @param asRegPltNo String Registration Plate Number
	//	 * @param aiRegExpYr int Registration Expiry Year
	//	 * @param asVIN String VIN number of vehicle
	//	 * @param aiComptCntyNo int Comlputing County Number
	//	 * @param asFundsRptDate String Funds Report Date
	//	 * @param asRptngDate String Reporting Date
	//	 * @param asFundsDueDate String Funds Due Date
	//	 * @param asFundsPymntDate String Funds Payment Date
	//	 * @param aiTraceNo int Trace Number (not used anymore)
	//	 * @param asCkNo String Check number (not used anmore) 
	//	 * @param asInvcNo String Invoice Number 
	//	 * @param aiOfcIssuanceNo int Office Issuance Number 
	//	 * @param aiMFArchiveIndi int Mainframe Archive Indicater. If
	//	 * <code>1</code>, archive is searched. by default <code>0</code>. 	
	//	 */
	//	private String setupMfKeys(
	//		int aiMFReqType,
	//		String asMFReqKey,
	//		String asDocNo,
	//		String asOwnrId,
	//		String asRegPltNo,
	//		int aiRegExpYr,
	//		String asVIN,
	//		int aiComptCntyNo,
	//		String asFundsRptDate,
	//		String asRptngDate,
	//		String asFundsDueDate,
	//		String asFundsPymntDate,
	//		int aiTraceNo,
	//		String asCkNo,
	//		String asInvcNo,
	//		int aiOfcIssuanceNo,
	//		int aiMFArchiveIndi)
	//	{
	//		final int MFREQTYPE_LENGTH = 2;
	//		final int MFREQKEY_LENGTH = 10;
	//		final int DOCNO_LENGTH = 17;
	//		final int OWNRID_LENGTH = 9;
	//		final int REGPLTNO_LENGTH = 7;
	//		final int REGEXPYR_LENGTH = 4;
	//		final int VIN_LENGTH = 22;
	//		final int COMPTCNTYNO_LENGTH = 3;
	//		final int FUNDSRPTDATE_LENGTH = 8;
	//		final int RPTNGDATE_LENGTH = 8;
	//		final int FUNDSDUEDATE_LENGTH = 8;
	//		final int FUNDSPYMNTDATE_LENGTH = 8;
	//		final int TRACENO_LENGTH = 9;
	//		final int CKNO_LENGTH = 7;
	//		final int INVCNO_LENGTH = 6;
	//		final int OFCISSUANCENO_LENGTH = 3;
	//		final int MFARCHIVEINDI_LENGTH = 1;
	//		final int MFKEYS_LENGTH = 142;
	//		// defect 7144 
	//		//final int REGSTKRNO_LENGTH = 10;
	//		// Required 10 positions for Sticker NO
	//		final String BLANK_STKR_NO = "          ";
	//		// end defect 7144 
	//		StringBuffer laMfKeysBuffer = new StringBuffer(MFKEYS_LENGTH);
	//		// set MFReqType
	//		StringBuffer laMFReqType =
	//			new StringBuffer(Integer.toString(aiMFReqType));
	//		switch (laMFReqType.length())
	//		{
	//			case MFREQTYPE_LENGTH - 1 :
	//				{
	//					laMfKeysBuffer.append("0");
	//					laMfKeysBuffer.append(laMFReqType.toString());
	//					break;
	//				}
	//			case MFREQTYPE_LENGTH :
	//				{
	//					laMfKeysBuffer.append(laMFReqType.toString());
	//					break;
	//				}
	//			default :
	//				System.out.println("MfReqType length is > 2 or <1");
	//				return null;
	//		}
	//		//this following condition can be removed at a later point if we 
	//		// know that other codes are possible. 
	//		if (!laMfKeysBuffer.toString().equals("00"))
	//		{
	//			System.out.println("error in MfReqType code. not = 00");
	//			return null;
	//		}
	//		// set doc no
	//		int liMFReqKeyPadLen = MFREQKEY_LENGTH - asMFReqKey.length();
	//		if (liMFReqKeyPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asMFReqKey);
	//			for (int i = 0; i < liMFReqKeyPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liMFReqKeyPadLen < 0)
	//		{
	//			System.out.println("MFReqKey No Too long");
	//			return null;
	//		}
	//		// set doc no
	//		int liDocNoPadLen = DOCNO_LENGTH - asDocNo.length();
	//		if (liDocNoPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asDocNo);
	//			for (int i = 0; i < liDocNoPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liDocNoPadLen < 0)
	//		{
	//			System.out.println("Doc No Too long");
	//			return null;
	//		}
	//		// set owner id
	//		int liOwnrIdPadLen = OWNRID_LENGTH - asOwnrId.length();
	//		if (liOwnrIdPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asOwnrId);
	//			for (int i = 0; i < liOwnrIdPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liOwnrIdPadLen < 0)
	//		{
	//			System.out.println("Owner ID Too long");
	//			return null;
	//		}
	//		// set RegPltNo
	//		int liRegPltNoPadLen = REGPLTNO_LENGTH - asRegPltNo.length();
	//		if (liRegPltNoPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asRegPltNo);
	//			for (int i = 0; i < liRegPltNoPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liRegPltNoPadLen < 0)
	//		{
	//			System.out.println("Reg Plate No Too long");
	//			return null;
	//		}
	//		// set RegExpYr
	//		String lsRegExpYr = Integer.toString(aiRegExpYr);
	//		int liRegExpYrPadLen = REGEXPYR_LENGTH - lsRegExpYr.length();
	//		if (liRegExpYrPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(lsRegExpYr);
	//			for (int i = 0; i < liRegExpYrPadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//		}
	//		else if (liRegExpYrPadLen < 0)
	//		{
	//			System.out.println("Exp Yr No Too long");
	//			return null;
	//		}
	//		// set VIN
	//		int liVINPadLen = VIN_LENGTH - asVIN.length();
	//		if (liVINPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asVIN);
	//			for (int i = 0; i < liVINPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liVINPadLen < 0)
	//		{
	//			System.out.println("VIN Too long");
	//			return null;
	//		}
	//		// defect 7144 
	//		// set RegStkrNo 
	//		laMfKeysBuffer.append(BLANK_STKR_NO);
	//		// end defect 7144 
	//		// set ComptCntyNo
	//		String lsComptCntyNo = Integer.toString(aiComptCntyNo);
	//		int liComptCntyNoPadLen =
	//			COMPTCNTYNO_LENGTH - lsComptCntyNo.length();
	//		if (liComptCntyNoPadLen >= 0)
	//		{
	//			for (int i = 0; i < liComptCntyNoPadLen; i++)
	//			{
	//				lsComptCntyNo = "0" + lsComptCntyNo;
	//			}
	//		}
	//		laMfKeysBuffer.append(lsComptCntyNo);
	//		if (liComptCntyNoPadLen < 0)
	//		{
	//			System.out.println("ComptCntyNo is Too long");
	//			return null;
	//		}
	//		// set FundsRptDate
	//		int liFundsRptDatePadLen =
	//			FUNDSRPTDATE_LENGTH - asFundsRptDate.length();
	//		if (liFundsRptDatePadLen == 0)
	//		{
	//			laMfKeysBuffer.append(asFundsRptDate);
	//		}
	//		else
	//		{
	//			for (int i = 0; i < liFundsRptDatePadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//			//since dates are zoned decimals for MF
	//		}
	//		// set RptngDate
	//		int liRptngDatePadLen = RPTNGDATE_LENGTH - asRptngDate.length();
	//		if (liRptngDatePadLen == 0)
	//		{
	//			laMfKeysBuffer.append(asRptngDate);
	//		}
	//		else
	//		{
	//			for (int i = 0; i < liRptngDatePadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//			//since dates are zoned decimals for MF
	//		}
	//		// set FundsDueDate
	//		int liFundsDueDatePadLen =
	//			FUNDSDUEDATE_LENGTH - asFundsDueDate.length();
	//		if (liFundsDueDatePadLen == 0)
	//		{
	//			laMfKeysBuffer.append(asFundsDueDate);
	//		}
	//		else
	//		{
	//			for (int i = 0; i < liFundsDueDatePadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//			//since dates are zoned decimals for MF
	//		}
	//		// set FundsPymntDate
	//		int liFundsPymntDatePadLen =
	//			FUNDSPYMNTDATE_LENGTH - asFundsPymntDate.length();
	//		if (liFundsPymntDatePadLen == 0)
	//		{
	//			laMfKeysBuffer.append(asFundsPymntDate);
	//		}
	//		else
	//		{
	//			for (int i = 0; i < liFundsPymntDatePadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//			//since dates are zoned decimals for MF
	//		}
	//		// set Trace no
	//		String lsTraceNo = Integer.toString(aiTraceNo);
	//		lsTraceNo =
	//			resizeStringtoLength(
	//				lsTraceNo,
	//				TRACENO_LENGTH,
	//				'0',
	//				ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING);
	//		laMfKeysBuffer.append(lsTraceNo);
	//		// set Ck no
	//		int liCkNoPadLen = CKNO_LENGTH - asCkNo.length();
	//		if (liCkNoPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asCkNo);
	//			for (int i = 0; i < liCkNoPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liCkNoPadLen < 0)
	//		{
	//			System.out.println("Ck No Too long");
	//			return null;
	//		}
	//		// set Invc no
	//		int liInvcNoPadLen = INVCNO_LENGTH - asInvcNo.length();
	//		if (liInvcNoPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(asInvcNo);
	//			for (int i = 0; i < liInvcNoPadLen; i++)
	//			{
	//				laMfKeysBuffer.append(" ");
	//			}
	//		}
	//		else if (liInvcNoPadLen < 0)
	//		{
	//			System.out.println("Invc No Too long");
	//			return null;
	//		}
	//		// set OfcIssuanceNo
	//		String lsOfcIssuanceNo = Integer.toString(aiOfcIssuanceNo);
	//		int liOfcIssuanceNoPadLen =
	//			OFCISSUANCENO_LENGTH - lsOfcIssuanceNo.length();
	//		if (liOfcIssuanceNoPadLen >= 0)
	//		{
	//			laMfKeysBuffer.append(lsOfcIssuanceNo);
	//			for (int i = 0; i < liOfcIssuanceNoPadLen; i++)
	//			{
	//				laMfKeysBuffer.append("0");
	//			}
	//		}
	//		else if (liOfcIssuanceNoPadLen < 0)
	//		{
	//			System.out.println("OfcIssuanceNO is Too long");
	//			return null;
	//		}
	//		// set MFArchiveIndi
	//		String lsMFArchiveIndi = Integer.toString(aiMFArchiveIndi);
	//		switch (lsMFArchiveIndi.length())
	//		{
	//			case MFARCHIVEINDI_LENGTH :
	//				{
	//					laMfKeysBuffer.append(lsMFArchiveIndi);
	//					break;
	//				}
	//			default :
	//				{
	//					System.out.println("MFArchiveIndi length is != 1");
	//					return null;
	//				}
	//		}
	//		String lsMfKeys = new String(laMfKeysBuffer);
	//		return lsMfKeys;
	//	}

	/**
	 * Sets up the MfKeys record that is sent to the mainframe for
	 * queries.
	 * <p>May return null.
	 * 
	 * Note: setupMfKeysSpclPlt() handles all Special Plates inquiries.
	 * 
	 * @return String
	 * @param asMFReqKey  String Mainframe request key type for search
	 * @param asDocNo String Document number for vehicle (on server)
	 * @param asOwnrId String Tax Id (not used for look up anymore) 
	 * @param asRegPltNo String Registration Plate Number
	 * @param aiSpclRegId int Special PLates Reg Id
	 * @param asVIN String VIN number of vehicle
	 * @param aiComptCntyNo int Comlputing County Number
	 * @param asFundsRptDate String Funds Report Date
	 * @param asRptngDate String Reporting Date
	 * @param asFundsDueDate String Funds Due Date
	 * @param asFundsPymntDate String Funds Payment Date
	 * @param aiTraceNo int Trace Number (not used anymore)
	 * @param asCkNo String Check number (not used anmore) 
	 * @param asInvcNo String Invoice Number 
	 * @param aiMFArchiveIndi int Mainframe Archive Indicater. If
	 * <code>1</code>, archive is searched. by default <code>0</code>. 	
	 */
	private String setupMfKeysSpclPlt(
		String asMFReqKey,
		String asDocNo,
		String asOwnrId,
		String asHOOPSRegPltNo,
		int aiSpclRegId,
		String asVIN,
		int aiComptCntyNo,
		String asFundsRptDate,
		String asRptngDate,
		String asFundsDueDate,
		String asFundsPymntDate,
		int aiTraceNo,
		String asCkNo,
		String asInvcNo,
		int aiMFArchiveIndi)
	{
		return setupMfKeysSpclPlt(
			asMFReqKey,
			asDocNo,
			asOwnrId,
			asHOOPSRegPltNo,
			aiSpclRegId,
			asVIN,
			aiComptCntyNo,
			asFundsRptDate,
			asRptngDate,
			asFundsDueDate,
			asFundsPymntDate,
			aiTraceNo,
			asCkNo,
			asInvcNo,
			aiMFArchiveIndi,
			new String(),
			new String(),
			new String(),
			new String(),
			0,
			0);
	}

	/**
	 * Sets up the MfKeys record that is sent to the mainframe for
	 * queries.
	 * <p>May return null.
	 * 
	 * Note: setupMfKeysSpclPlt() handles all Special Plates inquiries.
	 * 
	 * @param asMFReqKey  String Mainframe request key type for search
	 * @param asDocNo String Document number for vehicle (on server)
	 * @param asOwnrId String Tax Id (not used for look up anymore) 
	 * @param asRegPltNo String Registration Plate Number
	 * @param aiSpclRegId int Special PLates Reg Id
	 * @param asVIN String VIN number of vehicle
	 * @param aiComptCntyNo int Comlputing County Number
	 * @param asFundsRptDate String Funds Report Date
	 * @param asRptngDate String Reporting Date
	 * @param asFundsDueDate String Funds Due Date
	 * @param asFundsPymntDate String Funds Payment Date
	 * @param aiTraceNo int Trace Number (not used anymore)
	 * @param asCkNo String Check number (not used anmore) 
	 * @param asInvcNo String Invoice Number 
	 * @param aiMFArchiveIndi int Mainframe Archive Indicater. If
	 * <code>1</code>, archive is searched. by default <code>0</code>. 
	 * @param asPrmtNo
	 * @param asPrmtVIN
	 * @param asCustLstName
	 * @param asCustBsnName
	 * @param aiBeginDate
	 * @param aiEndDate 
	 * 
	 * @return String 	
	 */
	private String setupMfKeysSpclPlt(
		String asMFReqKey,
		String asDocNo,
		String asOwnrId,
		String asHOOPSRegPltNo,
		int aiSpclRegId,
		String asVIN,
		int aiComptCntyNo,
		String asFundsRptDate,
		String asRptngDate,
		String asFundsDueDate,
		String asFundsPymntDate,
		int aiTraceNo,
		String asCkNo,
		String asInvcNo,
		int aiMFArchiveIndi,
		String asPrmtIssuanceId,
		String asPrmtNo,
		String asCustLstName,
		String asCustBsnName,
		int aiBeginDate,
		int aiEndDate)
	{
		MFKeysData laMFKeysData =
			new MFKeysData(
				asMFReqKey,
				asDocNo,
				asOwnrId,
				asHOOPSRegPltNo,
				aiSpclRegId,
				asVIN,
				aiComptCntyNo,
				asFundsRptDate,
				asRptngDate,
				asFundsDueDate,
				asFundsPymntDate,
				aiTraceNo,
				asCkNo,
				asInvcNo,
				aiMFArchiveIndi,
				asPrmtIssuanceId,
				asPrmtNo,
				asCustLstName,
				asCustBsnName,
				aiBeginDate,
				aiEndDate);

		// defect 10462 
		if (SystemProperty.isDB2LogMFReq())
		{
			caMFReqData = laMFKeysData.getMFReqData();
		}
		// end defect 10462 

		final int MFREQKEY_LENGTH = 10;
		final int DOCNO_LENGTH = 17;
		final int OWNRID_LENGTH = 9;
		final int REGPLTNO_LENGTH = 7;
		final int SPCLREGID_LENGTH = 9;
		final int VIN_LENGTH = 22;
		final int COMPTCNTYNO_LENGTH = 3;
		final int FUNDSRPTDATE_LENGTH = 8;
		final int RPTNGDATE_LENGTH = 8;
		final int FUNDSDUEDATE_LENGTH = 8;
		final int FUNDSPYMNTDATE_LENGTH = 8;
		final int TRACENO_LENGTH = 9;
		final int CKNO_LENGTH = 7;
		final int INVCNO_LENGTH = 6;
		final int MFARCHIVEINDI_LENGTH = 1;

		// defect 10492
		final int DATE_LENGTH = 8;
		final int PRMTNO_LENGTH = CommonConstant.LENGTH_PERMIT_NO;
		final int CUSTLSTNAME_LENGTH = CommonConstant.LENGTH_LAST_NAME;
		final int CUSTBSNNAME_LENGTH =
			CommonConstant.LENGTH_BUSINESS_NAME;
		final int PRMISSUANCEID_LENGTH = CommonConstant.LENGTH_DOCNO;
		// defect 10595
		//final int MFKEYS_LENGTH = 135;
		final int MFKEYS_LENGTH = 254;
		//final int MFKEYS_LENGTH;
		//boolean lb15 =
		//SystemProperty.getMFInterfaceVersionCode().equals("V");

		//if (lb15)
		//{
		//MFKEYS_LENGTH = 254;
		//}
		//else
		//{
		//MFKEYS_LENGTH = 135;
		//}
		// end defect 10492
		// end defect 10595 

		StringBuffer laMfKeysBuffer = new StringBuffer(MFKEYS_LENGTH);

		// set KeyType
		int liMFReqKeyPadLen = MFREQKEY_LENGTH - asMFReqKey.length();
		if (liMFReqKeyPadLen >= 0)
		{
			laMfKeysBuffer.append(asMFReqKey);
			for (int i = 0; i < liMFReqKeyPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liMFReqKeyPadLen < 0)
		{
			// defect 10462 
			// System.out.println("MFReqKey No Too long");
			laMFKeysData.writeKeysToSysErrLog("MFReqKey Too Long. ");
			// end defect 10462 
			return null;
		}

		// set doc no
		int liDocNoPadLen = DOCNO_LENGTH - asDocNo.length();
		if (liDocNoPadLen >= 0)
		{
			laMfKeysBuffer.append(asDocNo);
			for (int i = 0; i < liDocNoPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liDocNoPadLen < 0)
		{
			// defect 10462 
			//System.out.println("Doc No Too long");
			laMFKeysData.writeKeysToSysErrLog("Doc No Too Long. ");
			// end defect 10462 
			return null;
		}

		// set owner id
		int liOwnrIdPadLen = OWNRID_LENGTH - asOwnrId.length();
		if (liOwnrIdPadLen >= 0)
		{
			laMfKeysBuffer.append(asOwnrId);
			for (int i = 0; i < liOwnrIdPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liOwnrIdPadLen < 0)
		{
			// defect 10462 
			// System.out.println("Owner ID Too long");
			laMFKeysData.writeKeysToSysErrLog("Owner ID Too Long. ");
			// end defect 10462 
			return null;
		}

		// set HOOPSRegPltNo
		int liRegPltNoPadLen =
			REGPLTNO_LENGTH - asHOOPSRegPltNo.length();
		if (liRegPltNoPadLen >= 0)
		{
			laMfKeysBuffer.append(asHOOPSRegPltNo);
			for (int i = 0; i < liRegPltNoPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liRegPltNoPadLen < 0)
		{
			// defect 10462 
			// System.out.println("Reg Plate No Too long");
			laMFKeysData.writeKeysToSysErrLog(
				"Reg Plate No Too Long. ");
			// end defect 10462 

			return null;
		}

		// defect 9086
		//	set SpclRegId.
		//	Must contain leading zeros.
		String lsSpclRegId = Integer.toString(aiSpclRegId);
		int liSpclRegIdPadLen = SPCLREGID_LENGTH - lsSpclRegId.length();
		if (liSpclRegIdPadLen >= 0)
		{
			if (liSpclRegIdPadLen >= 0)
			{
				for (int i = 0; i < liSpclRegIdPadLen; i++)
				{
					lsSpclRegId = "0" + lsSpclRegId;
				}
			}
		}
		else if (liSpclRegIdPadLen < 0)
		{
			// defect 10462 
			// System.out.println("SpclRegId No Too long");
			laMFKeysData.writeKeysToSysErrLog(
				"SpclRegId No Too Long. ");
			// end defect 10462 
			return null;
		}
		laMfKeysBuffer.append(lsSpclRegId);

		// set VIN
		int liVINPadLen = VIN_LENGTH - asVIN.length();
		if (liVINPadLen >= 0)
		{
			laMfKeysBuffer.append(asVIN);
			for (int i = 0; i < liVINPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liVINPadLen < 0)
		{
			// defect 10462 
			// System.out.println("VIN Too long");
			laMFKeysData.writeKeysToSysErrLog("VIN Too Long. ");
			// end defect 10462 

			return null;
		}

		// set ComptCntyNo
		String lsComptCntyNo = Integer.toString(aiComptCntyNo);
		int liComptCntyNoPadLen =
			COMPTCNTYNO_LENGTH - lsComptCntyNo.length();
		if (liComptCntyNoPadLen >= 0)
		{
			for (int i = 0; i < liComptCntyNoPadLen; i++)
			{
				lsComptCntyNo = "0" + lsComptCntyNo;
			}
		}
		laMfKeysBuffer.append(lsComptCntyNo);
		if (liComptCntyNoPadLen < 0)
		{
			// defect 10462 
			// System.out.println("ComptCntyNo is Too long");
			laMFKeysData.writeKeysToSysErrLog("ComptCntyNo Too Long. ");
			// end defect 10462 
			return null;
		}

		// set FundsRptDate
		int liFundsRptDatePadLen =
			FUNDSRPTDATE_LENGTH - asFundsRptDate.length();
		if (liFundsRptDatePadLen == 0)
		{
			laMfKeysBuffer.append(asFundsRptDate);
		}
		else
		{
			for (int i = 0; i < liFundsRptDatePadLen; i++)
			{
				laMfKeysBuffer.append("0");
			}
			//since dates are zoned decimals for MF
		}

		// set RptngDate
		int liRptngDatePadLen = RPTNGDATE_LENGTH - asRptngDate.length();
		if (liRptngDatePadLen == 0)
		{
			laMfKeysBuffer.append(asRptngDate);
		}
		else
		{
			for (int i = 0; i < liRptngDatePadLen; i++)
			{
				laMfKeysBuffer.append("0");
			}
			//since dates are zoned decimals for MF
		}

		// set FundsDueDate
		int liFundsDueDatePadLen =
			FUNDSDUEDATE_LENGTH - asFundsDueDate.length();
		if (liFundsDueDatePadLen == 0)
		{
			laMfKeysBuffer.append(asFundsDueDate);
		}
		else
		{
			for (int i = 0; i < liFundsDueDatePadLen; i++)
			{
				laMfKeysBuffer.append("0");
			}
			//since dates are zoned decimals for MF
		}

		// set FundsPymntDate
		int liFundsPymntDatePadLen =
			FUNDSPYMNTDATE_LENGTH - asFundsPymntDate.length();
		if (liFundsPymntDatePadLen == 0)
		{
			laMfKeysBuffer.append(asFundsPymntDate);
		}
		else
		{
			for (int i = 0; i < liFundsPymntDatePadLen; i++)
			{
				laMfKeysBuffer.append("0");
			}
			//since dates are zoned decimals for MF
		}

		// set Trace no
		String lsTraceNo = Integer.toString(aiTraceNo);
		lsTraceNo =
			resizeStringtoLength(
				lsTraceNo,
				TRACENO_LENGTH,
				'0',
				ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING);
		laMfKeysBuffer.append(lsTraceNo);

		// set Ck no
		int liCkNoPadLen = CKNO_LENGTH - asCkNo.length();
		if (liCkNoPadLen >= 0)
		{
			laMfKeysBuffer.append(asCkNo);
			for (int i = 0; i < liCkNoPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liCkNoPadLen < 0)
		{
			// defect 10462 
			// System.out.println("Ck No is Too long");
			laMFKeysData.writeKeysToSysErrLog("Ck No Too Long. ");
			// end defect 10462
			return null;
		}

		// set Invc no
		int liInvcNoPadLen = INVCNO_LENGTH - asInvcNo.length();
		if (liInvcNoPadLen >= 0)
		{
			laMfKeysBuffer.append(asInvcNo);
			for (int i = 0; i < liInvcNoPadLen; i++)
			{
				laMfKeysBuffer.append(CommonConstant.STR_SPACE_ONE);
			}
		}
		else if (liInvcNoPadLen < 0)
		{
			// defect 10462 
			// System.out.println("Invc No Too long");
			laMFKeysData.writeKeysToSysErrLog("Invc No Too Long. ");
			// end defect 10462
			return null;
		}

		// set MFArchiveIndi
		String lsMFArchiveIndi = Integer.toString(aiMFArchiveIndi);
		switch (lsMFArchiveIndi.length())
		{
			case MFARCHIVEINDI_LENGTH :
				{
					laMfKeysBuffer.append(lsMFArchiveIndi);
					break;
				}
			default :
				{
					// defect 10462 
					// System.out.println("MFArchiveIndi length is != 1");
					laMFKeysData.writeKeysToSysErrLog(
						"MFArchiveIndi length is != 1. ");
					// end defect 10462
					return null;
				}
		}
		// defect 10492
		// defect 10595
		//if (SystemProperty.getMFInterfaceVersionCode().equals("V"))
		//{
		// PERMIT NO  
		asPrmtNo = asPrmtNo == null ? new String() : asPrmtNo;
		asPrmtNo =
			UtilityMethods.addPaddingRight(
				asPrmtNo,
				PRMTNO_LENGTH,
				" ");

		// PERMIT ID  
		asPrmtIssuanceId =
			asPrmtIssuanceId == null ? new String() : asPrmtIssuanceId;
		asPrmtIssuanceId =
			UtilityMethods.addPaddingRight(
				asPrmtIssuanceId,
				PRMISSUANCEID_LENGTH,
				" ");

		// CUST LST NAME  
		asCustLstName =
			asCustLstName == null ? new String() : asCustLstName;
		asCustLstName =
			UtilityMethods.addPaddingRight(
				asCustLstName,
				CUSTLSTNAME_LENGTH,
				" ");

		// CUST BSN NAME  
		asCustBsnName =
			asCustBsnName == null ? new String() : asCustBsnName;
		asCustBsnName =
			UtilityMethods.addPaddingRight(
				asCustBsnName,
				CUSTBSNNAME_LENGTH,
				" ");

		// BEGIN DATE 
		String lsBeginDate = "00000000";
		if (aiBeginDate != 0)
		{
			lsBeginDate = Integer.toString(aiBeginDate);
		}
		String lsEndDate = "00000000";
		if (aiEndDate != 0)
		{
			lsEndDate = Integer.toString(aiEndDate);
		}

		if (asPrmtNo.length() > PRMTNO_LENGTH
			|| asPrmtIssuanceId.length() > DOCNO_LENGTH
			|| asCustLstName.length() > CUSTLSTNAME_LENGTH
			|| asCustBsnName.length() > CUSTBSNNAME_LENGTH
			|| lsBeginDate.length() != DATE_LENGTH
			|| lsEndDate.length() != DATE_LENGTH)
		{

			// defect 10462 
			// System.out.println("Permit Data is invalid");
			laMFKeysData.writeKeysToSysErrLog("Permit Data Invalid. ");
			// end defect 10462
			return null;
		}

		laMfKeysBuffer.append(asPrmtIssuanceId);
		laMfKeysBuffer.append(asPrmtNo);
		laMfKeysBuffer.append(asCustLstName);
		laMfKeysBuffer.append(asCustBsnName);
		laMfKeysBuffer.append(lsBeginDate);
		laMfKeysBuffer.append(lsEndDate);

		//}
		// end defect 10492
		// end defect 10595

		String lsMfKeys = new String(laMfKeysBuffer);
		return lsMfKeys;
	}

	/**
	 * Sets and returns the Vehicle Inquiry data object from the 
	 * mainframe response. 
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.VehicleInquiryData
	 * @param lsMfResponse java.lang.String
	 */
	public VehicleInquiryData setVehicleInquiryDataFromMfResponse(
		String asMfResponse,
		String asMfVINAResponse,
		String asMfRegisResponse,
		String asMfSpecialOwnerResponse,
		String asMfJunkResponse,
		String asMfPartialResponse,
		String asSpclPltRegisResponse)
	{
		//Setup all Offsets and lengths 
		//Create the return object
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		// In case Special Owner response is not null, get 
		// VehicleInquiryData from OwnerSpecialResponse
		// This has to be done here as Special owner response 
		// has fields from different data objects and they cannot be 
		// consructed in SetMFVehicleData
		//
		if (asMfSpecialOwnerResponse != null
			&& !(asMfSpecialOwnerResponse
				.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			MfbaSpecialOwnerG laSpecialOwnerG = new MfbaSpecialOwnerG();
			laVehicleInquiryData =
				laSpecialOwnerG
					.setVehicleInquiryDataFromSpecialOwnerResponse(
					asMfSpecialOwnerResponse);
			laSpecialOwnerG = null;
		}
		else if (
			(asMfPartialResponse != null)
				&& !(asMfPartialResponse
					.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			//defect 9086
			//	Get CICS Transid to determine if partial records are 
			//	from Special Plts Regis, CICS-COBOL R08
			//
			// Get CICS TransId
			String lsTransId =
				"R" + asMfPartialResponse.substring(2, 4);
			if (lsTransId.equals(MfAccess.R08))
			{
				MfbaPartialSpclPltDataG laPartialDataG =
					new MfbaPartialSpclPltDataG();
				laVehicleInquiryData.setPartialSpclPltsDataList(
					laPartialDataG
						.setMFPartialSpclRegisDataFromMfResponse(
						asMfPartialResponse));
				laPartialDataG = null;

			}
			else
			{
				MfbaPartialDataG laPartialDataG =
					new MfbaPartialDataG();
				laVehicleInquiryData.setPartialDataList(
					laPartialDataG.setMFPartialDataFromMfResponse(
						asMfPartialResponse));
				laPartialDataG = null;
			}
			// end defect 6701
		}
		else
		{
			//create the vehicle data
			// defect 6701
			//	Review how to handle RTSException
			try
			{
				MFVehicleData laMFVehicleData =
					setMFVehicleDataFromMfResponse(
						asMfResponse,
						asMfVINAResponse,
						asMfSpecialOwnerResponse,
						asMfJunkResponse,
						asSpclPltRegisResponse);
				//add this vehicledata to the VehicleInquiry
				laVehicleInquiryData.setMfVehicleData(laMFVehicleData);
			}
			catch (RTSException leRTSExc)
			{
				laVehicleInquiryData.setMfVehicleData(
					new MFVehicleData());
			}
			// if the multiple reg indi was set, this string should 
			// not be empty
			if (asMfRegisResponse != null
				&& !(asMfRegisResponse
					.equals(CommonConstant.STR_SPACE_EMPTY)))
			{
				// defect 6701
				//	Move to .data.MfbaMulpRegisG
				MfbaMultRegisG laMultRegisG = new MfbaMultRegisG();
				laVehicleInquiryData.setPartialDataList(
					laMultRegisG.setMultipleRegistrationDataFromMf(
						asMfRegisResponse));
				laMultRegisG = null;
				// end defect 6701
			}
		}
		// Return the vehicle Inquiry Data
		return laVehicleInquiryData;
	}

	/**
	 * Used to convert integers to Pcked Decimal format. Handles only
	 * integers (both positive and negative)
	 * 
	 * @return String
	 * @param liValue int
	 */
	public String setZonedDecimalFromInt(int aiValue)
	{
		String lsZonedDecimal = CommonConstant.STR_SPACE_EMPTY;
		if (aiValue < 0)
		{
			String lsValue = Integer.toString(aiValue);
			//remove the - ve sign
			StringBuffer laValue = new StringBuffer(lsValue);
			int liMinusSignPosition = lsValue.indexOf("-");
			laValue.replace(
				liMinusSignPosition,
				liMinusSignPosition + 1,
				CommonConstant.STR_SPACE_EMPTY);
			//get the zoned Deciaml
			lsZonedDecimal =
				setZonedDecimalFromNegativeNumber(laValue.toString());
		}
		else if (aiValue >= 0)
		{
			String lsValue = Integer.toString(aiValue);
			//get the zoned Deciaml
			lsZonedDecimal =
				setZonedDecimalFromPositiveNumbers(lsValue);
		}
		return lsZonedDecimal;
	}
	/**
	 * Used to convert negative integers to Pcked Decimal format. 
	 * 
	 * @return String
	 * @param asValue String
	 */
	public String setZonedDecimalFromNegativeNumber(String asValue)
	{
		StringBuffer lsValueBuf = new StringBuffer(asValue);
		//convert positive zoned decimals
		int liZonedDecimalSignPosition = asValue.length() - 1;
		//convert negative zoned decimal numbers				
		if (asValue.charAt(liZonedDecimalSignPosition) == '0')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, '}');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '1')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'J');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '2')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'K');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '3')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'L');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '4')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'M');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '5')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'N');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '6')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'O');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '7')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'P');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '8')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'Q');
		}
		else if (asValue.charAt(liZonedDecimalSignPosition) == '9')
		{
			lsValueBuf.setCharAt(liZonedDecimalSignPosition, 'R');
		}
		return lsValueBuf.toString().trim();
	}
	/**
	 * Used to convert positive integers to Pcked Decimal format. 
	 * 	
	 * @return String
	 * @param asValue String
	 */
	public String setZonedDecimalFromPositiveNumbers(String asValue)
	{
		StringBuffer laValueBuf = new StringBuffer(asValue);
		//convert positive zoned decimals
		int liMfZonedDecimalLastCharPosition = laValueBuf.length() - 1;
		if (asValue.charAt(liMfZonedDecimalLastCharPosition) == '0')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, '{');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '1')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'A');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '2')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'B');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '3')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'C');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '4')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'D');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '5')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'E');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '6')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'F');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '7')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'G');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '8')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'H');
		}
		else if (
			asValue.charAt(liMfZonedDecimalLastCharPosition) == '9')
		{
			laValueBuf.setCharAt(liMfZonedDecimalLastCharPosition, 'I');
		}
		return laValueBuf.toString().trim();
	}
	/**
	 * Trims the input string if not null.
	 * 
	 * @return String
	 * @param asMfString String
	 */
	public String trimMfString(String asMfString)
	{
		String lsTrimmedMfString = CommonConstant.STR_SPACE_EMPTY;
		if (asMfString != null)
		{
			lsTrimmedMfString = asMfString.trim();
		}
		return lsTrimmedMfString;
	}
	/**
	 * Voids the selected transactions from the TxDOT mainframe
	 * 
	 * @param transactions java.util.Vector
	 * @return int
	 */
	public int voidTransactions(GeneralSearchData aaGSD)
		throws RTSException
	{
		// defect 10244
		// 	Set variable to a constant
		//		int li_NO_OF_RECS_OFFSET = 0;
		final int NO_OF_RECS_OFFSET = 0;
		// end defect 10244

		//create the return object
		int liNoOfRecs = 0;
		String lsMfReqKey = CommonConstant.DOC_NO;
		String lsDocumentNumber = aaGSD.getKey1();
		String lsDummyDate = CommonConstant.STR_SPACE_EMPTY;
		String lsTransactionId = MfAccess.R07;
		// defect 9086
		//	New format for MfKeys
		int liTier = aaGSD.getIntKey2(); // Search Active/Inactive
		String lsMfKeys =
			setupMfKeysSpclPlt(
				lsMfReqKey,
				lsDocumentNumber,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				0,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				lsDummyDate,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				liTier);
		// end defect 9086

		String lsVoidResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		try
		{
			lsMfResponse = getMfResponse(lsTransactionId, lsMfKeys);
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN_MSG, aeEx);
			leRTSEx.setMsgType(RTSException.MF_DOWN);
			throw leRTSEx;
		}
		// even though the search is by doc no, there could be multiple
		// records that could be returned. 

		// defect 8983
		//	Return length of record
		APPCHeader laAppcHeader = new APPCHeader();
		//get the length of output from MF
		int liOutputLength =
			laAppcHeader.rtnMfOutputLength(lsMfResponse);
		//		final int liOutputLengthOffset = laAppcHeader.getHDOUTOFFSET();
		//		final int liOutputLengthLength = laAppcHeader.getHDOUTLEN();
		//		int liOutputLength =
		//			Integer.parseInt(
		//				lsMfResponse.substring(
		//					liOutputLengthOffset,
		//					liOutputLengthOffset + liOutputLengthLength));
		// end defect 8983

		if (liOutputLength != 0)
		{
			//remove the header from the response
			lsVoidResponse =
				lsMfResponse.substring(
					laAppcHeader.getAPPCHeaderRecord().length);

			// Get number of records from header
			// defect 10462 
			liNoOfRecs =
				rtnNoOfRecsMF(lsVoidResponse, NO_OF_RECS_OFFSET);
			// end defect 10462 
		}
		else
		{
			liNoOfRecs = 0;
		}
		//return the result
		return liNoOfRecs;
	}
}
