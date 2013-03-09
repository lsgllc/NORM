package com.txdot.isd.rts.services.util.constants;

/*
 * ErrorsConstant.java
 *
 * (c) Texas Department of Transportation 2005
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/16/2005	New class 
 * 							defect 7890 Ver 5.2.3
 * J Rue		08/16/2005	Add ERR_MSG_MAIN_JDIALOG
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/17/2005	Add ERR_MSG_PRICE_RANGE_99_99
 * 							ERR_MSG_INVALID_FEE_TITLE,
 * 							ERR_MSG_DATA_MISSING_VC_REG029,
 * 							ERR_MSG_ERROR_TITLE
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	08/23/2005	Add more errors from CommonServerBusiness
 * 							defect 7705 Ver 5.2.3
 * J Rue		12/16/2005	Add common title error Confirmation Screen
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							remove ERR_TITLE_CONFIRMATION_SCR
 * 							defect 8756 Ver 5.2.3
 * K Harrell	09/14/2006	Add ERR_NUM_SPV_SVC_UNAVAILABLE
 * 							defect 8926 Ver 5.2.5
 * Ray Rowehl	02/09/2007	Add error number for VI Unavailable.
 * 							add ERR_NUM_VI_UNAVAILABLE
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/15/2007	Add error number for VI Unavailable.
 * 							add ERR_NUM_VI_NEXTITEM_UNAVAILABLE,
 * 								ERR_NUM_VI_PER_EXISTS_VI, 
 * 								ERR_NUM_VI_PER_MATCHES_PTRN, 
 * 								ERR_NUM_VI_PER_HAS_INVALID_LTR, 
 * 								ERR_NUM_VI_PER_EXISTS_MF, 
 * 								ERR_NUM_VI_PER_EXISTS_OBJECTIONABLE, 
 * 								ERR_NUM_VI_PER_EXISTS_RESERVED
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/15/2007	Add a generic PLP rejection message to 
 * 							return to the user per VTR business 
 * 							requirement to not inform POS clerk about 
 * 							specific nature of rejection.
 * 							add ERR_NUM_VI_ITEM_NOT_AVAILABLE,
 * 								ERR_NUM_VI_INTERSECTION,
 * 								ERR_NUM_WRONG_OBJECT_TYPE
 * 							defect 9116 Ver Special Plates
 * K Harrell	06/18/2007	add ERR_HELP_IS_NOT_AVAILABLE
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	02/07/2008	Add constants for V21 responses
 * 							add ERR_NUM_V21_SUCCESS, 
 * 								ERR_NUM_V21_MULTIPLE_RECS,
 * 								ERR_NUM_V21_NOT_AVAILABLE,
 * 								ERR_NUM_V21_NOT_FOUND,
 * 								ERR_NUM_V21_INVALID_REQUEST
 * 							defect 9502 Ver 3_Amigos_PH_A  
 * B Hargrove	02/15/2008	Add constant for V21 responses
 * 							add ERR_NUM_V21_REGEXPYRMO_DO_NOT_MATCH
 * 							defect 9502 Ver 3_Amigos_PH_A  
 * B Hargrove	06/16/2008	Add error message for Vendor Plates: 
 * 							Plate is expired, cannot continue. 
 * 							add ERR_NUM_EXPIRED_VENDOR_PLATE
 * 							defect 9689 Ver Defect MyPlates_POS
 * Ray Rowehl	07/11/2008	Add error message for when a reserved plate 
 * 							requested is not reserved for the office.
 * 							add ERR_NUM_VI_PER_EXISTS_RESERVED_DIFFERENT_COUNTY
 * 							defect 9679 Ver MyPlates_POS
 * J Rue		01/16/2009	Add DTA error messages
 *							add CORRUPTDISK, MEDIA_ID_ERRMSG, 
 *							INPUT_MEDIA_FAILURE_ERRMSG, RSPSNOTMATCHDTA1,
 *							RSPSNOTMATCHDTA2, RSPSUPDTERROR, DISKNOTUPDT
 * 							defect 8912 Ver Defect_POS_D
 * J Rue		01/29/2009	Remove CORRUPTDISK. It is not needed.
 * 							delete CORRUPTDISK
 * 							defect 8912 Ver Defect_POS_D
 * K Harrell	02/09/2009	add Error Constants for Internet Report 
 * 							Processing. 
 * 							add
 * 							 ERR_NUM_DATE_NOT_VALID,  			(733)  
 * 							 ERR_NUM_MORE_THAN_30_DAYS_IN_PAST, (956)
 * 							 ERR_NUM_DATE_RANGE_TOO_LARGE, 		(958)
 * 							 ERR_NUM_RECORD_CHECKED_OUT, 		(962) 
 * 							 ERR_NUM_ERROR_HAS_OCCURRED, 		(973)
 * 							 ERR_NUM_NULL_OBJECT 				(983) 
 * 							defect 9935 Ver Defect_POS_D  
 * K Harrell	02/19/2009  add ERR_NUM_BEGIN_DEPOSIT_DATE_TOO_EARLY (781)
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	03/03/2009	add ERR_NUM_INCOMPLETE_LIEN_DATA (188)
 * 							defect 9974 Ver Defect_POS_E 
 * K Harrell	03/07/2009	add ERR_MSG_FEE_OUTSIDE_CCO_RANGE, 
 * 							ERR_NUM_CCO_WITHIN_PAST_TWO_WEEKS  (4)
 * 							ERR_NUM_DOC_TYPE_UNAVAILABLE_FOR_CCO (100)
 * 							delete ERR_MSG_PRICE_RANGE_99_9 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	03/10/2009	add ERR_NUM_NO_LIEN_ON_VEHICLE,   (83)
 * 							ERR_NUM_VERIFY_LIENS_RELEASED     (366) 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	03/16/2009	add ERR_NUM_CERTFD_LIENHLDR_NOT_AVAIL(805)
 * 							add ERR_NUM_ETTL_MAX_ONE_LIEN  (806) 
 * 							defect 9974 Ver Defect_POS_E 
 * B Brown		03/20/2009	add ERR_NUM_RECON_HSTRY_INSERT_ERROR,            
 *							ERR_NUM_FTP_PROPERTIES_FILE_ERROR,      
 * 							ERR_NUM_DEPOSIT_FILE_STRING_TOKENIZER_ERROR,                                                 
 * 							ERR_NUM_DEPOSIT_PROPERTIES_FILE_LOAD_ERROR,   
 *							ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR,    
 *							ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR,   
 *							ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR,        
 *							ERR_NUM_TRACE_NUM_EXISTS_ON_PREVIOUS_FILE_ERROR,
 *							ERR_NUM_DEPOSIT_FILE_RENAME_ERROR      
 *							defect 9986 Ver Defect_POS_E
 * K Harrell	03/24/2009	add ERR_NUM_AT_LEAST_ONE_TRANS_TYPE (807)
 * 							ERR_NUM_AT_LEAST_ONE_LIENHLDR  (808)
 * 							defect 9972 Ver Defect_POS_E 
 * K Harrell	05/21/2009	add ERR_NUM_CANNOT_CHANGE_BONDED_TITLE,	(16)
 *							ERR_NUM_RECORD_CANNOT_BE_RETRIEVED_CONTINUE, (20)
 *							ERR_NUM_NO_RECORD_FOUND, 			(57)
 *							ERR_NUM_TOO_MANY_RCDS_FOUND,  		(148)
 *							ERR_NUM_TRANS_INVALID_FOR_SPCL_REG, (149)
 *							ERR_NUM_NO_RECORD_FOUND_CONTINUE, 	(211)
 *							ERR_NUM_PLT_OR_STKR_CANCELLED, 		(537)
 *							ERR_NUM_PLT_CANCELLED  				(729)
 * 							defect 10036   Ver Defect_POS_F 
 * K Harrell	05/29/2009	add VEH_DATA_MISSING_EXCEPTION_TITLE, 
 *							VEH_DATA_MISSING_EXCEPTION,
 *							VEH_DATA_MISSING_EXCEPTION_TXT_TO_REPLACE,
 *							ERR_NUM_INVALID_REG_NO_ACTION, 		(161)
 *							ERR_NUM_GROSS_WT_INVALID_CHNG_WT,   (418)
 * 							ERR_NUM_GROSS_WT_INVALID_FOR_REG_TIRE,(419)
 * 							ERR_NUM_ACTION_INVALID_LGL_REST,	(428)
 *							ERR_NUM_INVALID_DOC_TYPE,			(429)
 *							ERR_NUM_ACTION_INVALID_OFF_HWY,		(466)
 *							ERR_NUM_ACTION_INVALID_SALV_CERT,	(467)
 * 							ERR_NUM_ACTION_INVALID_COA,			(468)
 *							ERR_NUM_ACTION_INVALID_OS_APP,		(469)
 * 							ERR_NUM_ACTION_INVALID_STOLEN,		(470)
 *   						ERR_NUM_DBSERVER_UNAVAILABLE, 		(618)
 * 							ERR_NUM_ACTION_INVALID_NON_REPAIR,	(654)
 *							ERR_NUM_ACTION_INVALID_SALV_VEH_TITLE,(655)
 *   						ERR_NUM_NO_RECORD_SELECTED,			(960)
 *    						ERR_NUM_HOLD_REASN_REQD, 			(963)
 *							ERR_NUM_DECLINE_REASN_REQD,			(964)
 *							ERR_NUM_COMMENT_REQD_FOR_HOLD,		(965)
 *							ERR_NUM_COMMENT_REQD_FOR_DECLINE,	(966)
 *							ERR_NUM_NO_ACTION_COMMAND_SPECIFIED, (967)
 *							ERR_NUM_CONNECTION_FAILURE_HAS_OCCURRED,(972)
 *							ERR_NUM_NO_TRANS_IN_QUEUE,			(975)
 *							ERR_NUM_NO_NEW_HOLD_DISPLAYED,		(976)
 *							ERR_NUM_NO_HOLD_NEW_DISPLAYED,		(977)
 *							ERR_NUM_NULL_INTERNET_DATA			(986)
 *							defect 8749 Ver Defect_POS_F
 * K Harrell	06/03/2009	add ERR_NUM_VEH_TONNAGE_INVALID,  	(201)
 * 							ERR_NUM_GROSS_WT_EXCEEDS_MAX		(89)
 * 							defect 8414 Ver Defect_POS_F 
 * B Hargrove	06/03/2009	Make msgs work for flashdrive or diskette.
 * 							add ERR_NUM_EXTERNAL_MEDIA_NOT_FOUND,
 * 							ERR_NUM_BOTH_DEVICES_IN_USE
 * 							refactor/rename/modify: 
 * 							RSPSIDRSPSDISKNO / MEDIA_ID_ERRMSG/, 
 * 							DISKDRIVEFAILURE / INPUT_MEDIA_FAILURE_ERRMSG
 * 							modify RSPSUPDTERROR, RSPSNOTMATCHDTA1,
 * 							INSERT_MEDIA_MSG  
 * 							defect 10075 Ver Defect_POS_F
 * K Harrell	06/09/2009	add ERR_NUM_ENTER_SEARCH_PARAMETERS, (950)
 * 							ERR_NUM_BEGIN_DATE_INCORRECT_FORMAT, (954)
 * 							ERR_NUM_END_DATE_INCORRECT_FORMAT,	 (955)
 * 							ERR_NUM_TRANSACTION_ID_INCORRECT_FORMAT, (957)
 * 							ERR_NUM_DATE_WITHIN_30_DAYS,		 (959)
 * 							ERR_NUM_INVALID_BEGIN_END, 			 (981)
 * 							ERR_NUM_OVER_60_DAYS_IN_PAST 		 (982)
 * 							defect 8763	Ver Defect_POS_F 
 * K Harrell	06/11/2009	add ERR_NUM_CANNOT_PERFORM_REJCOR
 * 							defect 10035 Ver Defect_POS_F		 (17)
 * K Harrell	06/12/2009	add ERR_NUM_NO_REPORTS_THAT_CAN_PRINT
 * 							defect 10086 Ver Defect_POS_F		(389)
 * K Harrell	06/17/2009	add ERR_NUM_DATE_GT_MAX_RPT_DATE     (782)
 * 							defect 1011 Ver Defect_POS_F
 * K Harrell	06/22/2009	add ERR_NUM_CANNOT_RETRIEVE_CANNOT_VOID,(331)
 * 							ERR_NUM_CANNOT_FIND_SUBCONTRACTOR_ID, (701)
 * 							ERR_NUM_FEE_BELOW_MIN_REG_FEE (706)
 * 							defect 10023 Ver Defect_POS_F
 * Ray Rowehl	07/07/2009	Add error numbers for logon.
 * 							Clean out old defect markers since they did
 * 							not hold position on sort.
 * 							add ERR_NUM_AD_DLL_MISSING,
 * 								ERR_NUM_LOGIN_SUCCESS_FROM_CACHE,
 * 								ERR_NUM_USERNAME_NOT_VALID
 * 							defect 10103 Ver Defect_POS_F
 * K Harrell	07/03/2009	add ERR_NUM_INCOMPLETE_OWNR_DATA,
 * 							 ERR_NUM_INCOMPLETE_PREV_OWNR_DATA,	(189)
 * 							 ERR_NUM_INCOMPLETE_REN_ADDR_DATA, (191)
 *							 ERR_NUM_INCOMPLETE_VEH_ADDR_DATA, (192)
 *							 ERR_NUM_RPO_CANCELLED (205) 
 *							 ERR_NUM_TMP_GROSS_NOT_OVER_80000  (473)
 *							defect 10112 Ver Defect_POS_F
 * K Harrell	07/14/2009	add ERR_NUM_EMPTY_WT_INVALID, (2) 
 *							 ERR_NUM_REPL_CANNOT_BE_PERFORMED, (48)		
 * 							 ERR_NUM_REG_CHG_REQD_GO_TO_ADDL_INFO, (92) 
 *							 ERR_NUM_AUTH_CODE_INVALID, (116) 
 *							 ERR_NUM_INCOMPLETE_ADDR_DATA, (186) 
 *							 ERR_NUM_MUST_INCR_CARRYING_CAPACITY, (200)
 *							 ERR_NUM_OUTSTANDING_HOT_CHECK, (231) 
 *							 ERR_NUM_CLASS_PLT_STKR_INVALID_TO_ADDL_INFO, 
 *							     (411) 
 * 							 ERR_NUM_VEH_WT_CHG_REQD_GO_TO_ADDL_INFO, (423) 
 * 							 ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE, (738) 
 * 							 ERR_NUM_VALID_STATE_IN_CNTRY_FIELD (809)
 * 							defect 10127 Ver Defect_POS_F
 * B Brown		12/10/2009	add ERR_NUM_DEP_REC_DATA_NOT_IN_NAME (2211) 
 *							ERR_NUM_FILE_NAME_DATE_NOT_EQUAL_DEP_DATE 
 *								(2211)                                       
 *							ERR_NUM_DEPOSIT_FILE_MISSING (2213)     
 *							ERR_NUM_FTP_CONNECTION_FAILED (22140    
 *							ERR_NUM_FTP_FAILED_LOGIN (2215)         
 *							ERR_NUM_FTP_FAILED_TRANSFER_TYPE (2216) 
 *							ERR_NUM_FTP_FAILED_ACTIVE_MODE (2217)	 
 *							ERR_NUM_DEPOSIT_PROPERTIES_FILE_MISSING 
 *								(2218)						                                         
 *							ERR_NUM_DEPOSIT_FILE_EMPTY (2219)	 
 *							ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD (2220)
 *							ERR_NUM_DEPOSIT_COUNTY_NUMBER_ERROR (2221)
 *							ERR_NUM_DEPOSIT_INVALID_CREDIT_CARD_TYPE
 *								(2222) 
 *							ERR_NUM_DEPOSIT_INVALID_TRACE_NUMBER (2223)
 *							ERR_NUM_DEPOSIT_LAST_4_CREDIT_CARD (2224)
 *							ERR_NUM_DEPOSIT_INVALID_PAYMNT_AMT (2225)
 *							ERR_NUM_TOL_DATE_NOT_YYYYMMDD (2226)    
 *							ERR_NUM_DEPOSIT_WRONG_NUM_FIELDS (2227) 
 *							ERR_NUM_DEPOSIT_RECON_HSTRY_UPDATE_ERROR 
 *								(2228)
 *							ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR(2229)
 *							ERR_NUM_STATIC_LOAD_ERROR(2230)         
 *							defect 10262 Ver Defect_POS_H
 * K Harrell	12/15/2009	add ERR_MSG_DTA_RECORDS_DO_NOT_CONFORM,
 * 							 ERR_MSG_DTA_MORE_THAN_MAX_TRANS,
 * 							 ERR_MSG_DTA_DISKETTE_INVALID_DEALERID,
 * 							 ERR_MSG_DTA_KEYBOARD_INVALID_DEALERID,
 * 							 ERR_MSG_DTA_TRANS_DATE_CANNOT_BE_IN_FUTURE,
 *							 ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_PLT,
 *							 ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_STKR,
 *							 ERR_MSG_DTA_TRANS_MORE_THAN_60_DAYS_OLD
 *							defect 10290 Ver Defect_POS_H
 * K Harrell	03/12/2010	add ERR_MSG_DATE_CANNOT_BE_IN_FUTURE
 * 							delete ERR_MSG_DTA_TRANS_DATE_CANNOT_BE_IN_FUTURE
 * 							defect 10355 Ver POS_640 
 * K Harrell	03/22/2010	add ERR_MSG_ADDRESS_UPDATED_UPON_APPROVAL 
 * 							defect 10372 Ver POS_640
 * K Harrell	04/21/2010	add ERR_MSG_NO_REG_FOR_ATV
 * 							defect 10453 Ver POS_640  
 * K Harrell	04/21/2010	add ERR_MSG_NO_REG_FOR_ATV_ROV
 * 							delete ERR_MSG_NO_REG_FOR_ATV
 * 							defect 10453 Ver POS_640  
 * B Hargrove	04/29/2010	add ERR_NUM_PLATENO_TOO_LONG
 * 							defect 10402 Ver POS_640  
 * Min Wang		06/22/2010  modify ERR_NUM_INVOICE_NOT_ON_MF
 * 							defect 8265 Ver 6.5.0
 * K Harrell	07/06/2010	add ERR_NUM_THREE_OR_MORE_30_DAY_PERMITS, 
 * 							 ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS, 
 *							 ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED_REFINE,
 * 							 ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED,  
 * 							 ERR_NUM_PRMT_CNTRY_OF_REG_INVALID,
 *							 ERR_NUM_PERMIT_HAS_EXPIRED,
 *							 ERR_NUM_THREE_OR_MORE_30_DAY_PERMITS 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/10/2010	add ERR_NUM_INCOMPLETE_APPL_DATA
 * 							defect 10491 Ver 6.5.0 
 * B Brown		07/21/2010	add ERR_NUM_BUILD_EMAIL_MESSAGE_ERROR
 * 							ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST
 * 							ERR_NUM_INVALID_REG_EXP_MO
 * 							ERR_NUM_INVALID_REG_EXP_YR
 * 							ERR_NUM_INVALID_VEHICLE_MAKE
 * 							ERR_NUM_INVALID_COUNTY
 * 							defect 10512 Ver 6.5.0 
 * K Harrell	10/18/2010	add ERR_NUM_DO_NOT_ALLOW_CCO_TTL_IN_PROCS
 * 							defect 10624 Ver 6.6.0 
 * K Harrell	10/22/2010	add ERR_NUM_CCO_WITHIN_SPECIFIED_INTERVAL
 * 							delete ERR_NUM_CCO_WITHIN_PAST_TWO_WEEKS 
 *							defect 10639 Ver 6.6.0
 * K Harrell	12/09/2010  add ERR_NUM_TEMP_INVALID_FOR_INST
 * 							defect 10679 Ver 6.7.0
 * K Harrell	12/10/2010  add ERR_NUM_PAYMENT_IN_ERROR
 * 							defect 10348 Ver 6.7.0
 * K Harrell	12/27/2010	add ERR_NUM_EXPIRED_INSIGNIA
 * 							defect 10700 Ver 6.7.0   
 * K Harrell	01/12/2011	add ERR_NUM_LIEN_DATE_INVALID
 * 							defect 10631 Ver 6.7.0
 * K Harrell 	01/29/2011	add ERR_NUM_WEBAGNT_TRANS_EXIST, 
 * 							 ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL
 * 							defect 10734 Ver 6.7.0  
 * K Harrell	02/03/2011	add ERR_NUM_MULTIPLE_OF_BASE_FEE
 * 							defect 10741 Ver 6.7.0 
 * K Harrell	03/09/2011 	add ERR_NUM_WEBAGNT_LOOKUP_EXCEPTION, (2300)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_NO_RECORD_FOUND, (2301)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_MULT_RECORDS_FOUND, (2302)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_NO_MATCH_LAST_4_VIN, (2303)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_AGENT_NOT_AUTHORIZED_FOR_COUNTY, (2304)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_AGENT_MUST_SCAN, (2305)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_REGISTRATION_TOO_FAR_IN_ADVANCE, (2306)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_REGISTRATION_EXPIRED_BEYOND_AUTH, (2307)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_HARDSTOP_EXISTS, (2308)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_INPROCESS_TRANS_EXISTS, (2309)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_INVALID_DOCTYPECD, (2310)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_LOCATION_NOT_AUTHORIZED, (2311)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_GROSS_WT_EXCESSIVE, (2312)
 * 							 ERR_NUM_WEBAGNT_LOOKUP_REGCLASSCD_NOT_ELIGIBLE, (2313)
 * 							defect 10768 Ver 6.7.1 
 * Min Wang		03/11/2011   add MSG_LENGTH_100
 * 							defect 10760 Ver 6.7.1
 * Ray Rowehl	03/17/2011	Add constants for Web Agent Session Messages
 * 							add ERR_NUM_WEBAGNT_SESSION_ERR
 * 							defect 10760 Ver 6.7.1
 * K Harrell	03/15/2011	...working
 * 							defect 10768 Ver 6.7.1 
 * Ray Rowehl	03/18/2011	Add more Web Agent Session error numbers.
 * 							add ERR_NUM_SESSION_TIMEOUT
 * 							defect 10760 Ver 6.7.1
 * Min Wang		3/21/2011	add ERR_MSG_MAX_LNG_WEB_SERVICES
 *                          delete MSG_LENGTH_100
 * 							defect 10760 Ver 6.7.1
 * K Harrell	03/29/2011	add ERR_NUM_WEBAGNT_BATCH_TOO_MANY_ROWS
 * 							defect 10768 Ver 6.7.1 
 * Ray Rowehl	04/04/2011	Working on Authentication.
 * 							add ERR_NUM_WEBAGNT_AUTHENTICATION_ERR, 
 * 								ERR_NUM_WEBAGNT_AUTHORIZATION_ERR
 * 							delete ERR_NUM_WEBAGNT_AUTH_ERR
 * 							defect 10760 Ver 6.7.1
 * K Harrell	04/05/2011	add ERR_NUM_WEBAGNT_BATCH_START_END_TOO_FAR_APART
 * 							defect 10785 Ver 6.7.1  
 * K Harrell	04/12/2011	add ERR_NUM_WEBAGNT_INVALID_LOOKUP_REQUEST,
 * 							 ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_NO_WEB_AGENCY_AUTH,
 * 							 ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_AGENCY_BATCH_HAS_NO_TRANS,
 * 							 ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_AVAIL_FOR_REQUEST,
 * 							 ERR_NUM_WEBAGNT_AGENT_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_INVALID_OFC_WSID_DATA,
 * 							 ERR_NUM_WEBAGNT_RTS_SECURITY_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_APPROVAL_NOT_SUCCESSFUL,
 * 							 ERR_NUM_WEBAGNT_AGENCY_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_INVALID_TRANS_HDR
 * 							defect 10785 Ver 6.7.1  
 * K Harrell	04/18/2011	add ERR_NUM_WEBAGNT_TRANS_NOT_FOUND,
 * 							 ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL_TO_VOID
 * 							defect 10785 Ver 6.7.1
 * Ray Rowehl	04/28/2011	Split up 2321 into multiple error numbers. 
 * 							add ERR_NUM_WEBAGNT_AUTH_TAB_ERR,
 * 								ERR_NUM_WEBAGNT_AUTH_TAB2_ERR,
 * 								ERR_NUM_WEBAGNT_SESSION_MULT_ERR
 * 							defect 10670 Ver 6.7.1
 * K Harrell	04/28/2011	add ERR_NUM_WEBAGNT_NO_TRANS_AVAIL_TO_ACCPT
 * 							defect 10785 Ver 6.7.1
 * Ray Rowehl	05/02/2011	Add error message number for eDir server
 * 							error.
 * 							add ERR_NUM_WEBAGNT_EDIR_SOAP_ERR
 * 							defect 10670 Ver 6.7.1
 * Richard Pilon 05/06/2011	Add error message numbers for eDir errors.
 * 							add ERR_NUM_WEBAGNT_EDIR_PWD_EXPIRED
 * 							add ERR_NUM_WEBAGNT_EDIR_NO_REPSONSES
 * 							add ERR_NUM_WEBAGNT_EDIR_LOCKED
 * 							defect 10670 Ver 671
 * R Pilon		06/13/2011	Add error message number for null credentials
 * 							add ERR_NUM_USERID_PWD_REQUIRED
 * 							defect 10670 Ver 6.8.0
 * K Harrell	06/15/2011	add ERR_NUM_DATE_RANGE_CANNOT_EXCEED
 * 							defect 10900 Ver 6.8.0 
 * K Harrell	06/19/2011	add ERR_NUM_CANNOT_MODIFY_VENDOR_ISSUED, 
 * 							 ERR_NUM_NOT_AVAIL_FOR_REPRINT,
 *  						 ERR_NUM_CANNOT_BE_MORE_THAN_12_MONTHS_FUTURE
 * 							defect 10844 Ver 6.8.0 
 * K McKee		08/15/2011	add MSG_UNSUBMITTED_BATCHES_FOR_DELETED_AGENCY
 * 							defect 10729 Ver 6.8.1 
 * K McKee      09/09/2011  added Web Agent error messages
 * 							added USERNAME_AND_PASSWORD_MUST_BE_ENTERED (5002)
 * 							added USERNAME_MUST_BE_ENTERED (5003)
 * 							added PASSWORD_MUST_BE_ENTERED (5004)
 * 							added NO_AGENCY_IS_FOUND_FOR_THE_USER_ENTERED  (5005)
 * 							added PLEASE_CHECK_I_ACCEPT_OR_PRESS_THE_CANCEL_BUTTON (5006)
 * 							added PLEASE_ENTER_THE_LICENSE_PLATE_NUMBER (5007)
 * 							added PLEASE_ENTER_THE_LAST_FOUR_CHARACTERS_OF_THE_VIN (5008) 
 * 							added PLEASE_SCAN_THE_RENEWAL_NOTICE_BARCODE (5009)
 * 							added NOT_ENOUGH_DATA_WAS_SCANNED_TO_PERFORM_A_SEARCH (5010)
 * 							added INPUT_METHOD_USED_IS_NOT_VALID (5011)
 * 							added RENEWAL_HAS_ALREADY_BEEN_PROCESSED (5012)	
 * 							added SELECT_RESPONSE_FOR_CITATION_ISSUED_QUESTION (5013)
 * 							added EXPIRED_LICENSE_CITATION (5014)
 * 							added SELECT_RESPONSE_FOR_VALID_INSURANCE (5015)
 * 							added PROOF_OF_INSURANCE_ERR (5016)
 * 							added SELECT_AGENCY_LOCATION (5017)
 * 							added USERNAME_OR_FIRST_AND_LAST_NAME_MUST_BE_NTERED (5018)	
 * 							added AGENCYID_NAME_OR_ZIP_MUST_BE_ENTERED (5019)
 * 							added NO_USER_WAS_FOUND (5020)
 * 							added FIRST_NAME_AND_LAST_NAME_MUST_BE_ENTERED(5021)
 * 							added USER_NAME_MUST_BE_DIFFERENT_THAN_THE_LOGGED_IN_USER_NAME (5022)
 * 							added MAXIMUM_SUBMIT_COUNT_MUST_BE_LESS_THAN_OR_EQUAL_TO_1000 (5023)
 * 							added AGENT_IS_ALREADY_ASSIGNED_TO_THIS_LOCATION (5024)
 * 							added LOCATION_IS_ALREADY_ASSIGNED (5025)
 *                          added NO_LOCATION_WAS_FOUND_FOR_THE_CRITERIA_ENTERED (5026)
 *                          defect 10729 Ver 6.8.1 
 * K McKee      09/13/2011  added Log Func Trans Not Found error message
 * 							added LOGIN_INFO_NOT_FOUND_IN_LOG_FUNCTION_TRANS (2347) 
 *							defect 10729 Ver 6.8.1 
 * K Harrell	10/10/2011	add ERR_NUM_NUM_PLACARDS_OUTSIDE_RANGE (29)
 * 							defect 11050 Ver 6.9.0  
 * K Harrell	10/15/2011	add ERR_NUM_MIN_FIELD_LENGTH_2 (39)
 * 							defect 11004 Ver 6.9.0 
 * K Harrell	10/15/2011	add ERR_NUM_CARRYING_CAPACITY_GTE_THIRD_EMPTY_WT,(13)
 *                          ERR_NUM_PLATES_ONLY_AVAILABLE_FROM_VTR, (18)
 *                          ERR_NUM_PLATES_NON_TRANSFERRABLE, (84)
 *                          ERR_NUM_INCREASE_GROSS_TO_MIN_FOR_FIXED_WT, (90)
 *                          ERR_NUM_TRAILER_MUST_BE_TITLED_IF_NOT_JUST_FARM ,(96)
 *                          ERR_NUM_MUST_INCREASE_GROSS_WT_TO_TITLE_TRAILER, (142)
 *                          ERR_NUM_MINOR_MAJOR_COLORS_CANNOT_BE_SAME, (160)
 *	                        ERR_NUM_VIN_REQUIRED_TO_TITLE, (176)
 *                          ERR_NUM_GROSS_WT_INVALID_FOR_FARM_TRAILER, (202)
 *                          ERR_NUM_TRAVEL_TRAILER_MUST_BE_LT_40_FT, (203)
 *                          ERR_NUM_TITLE_TYPE_INVALID_FOR_VEHCLASS, (472)
 *							ERR_NUM_TRAILER_MUST_BE_TITLED_OR_REG_FARM_TRAILER, (580)
 *							ERR_NUM_TONNAGE_LE_2_FOR_DISABLED (2010)
 *     						defect 11126 Ver 6.9.0
 * K Harrell	10/16/2011	add ERR_NUM_MUST_SELECT_REBUILT_SALVAGE(41), 
 * 							 ERR_NUM_OWNERSHIP_EVIDENCE_MUST_BE_SALVAGE(42) 
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	10/17/2011	add ERR_NUM_MUST_START_OR_END_WITH_DV (43) 
 * 							defect 11061 Ver 6.9.0 
 * K Harrell	10/24/2011	add ERR_NUM_MUST_CHANGE_REGIS (208) 
 * 							defect 11126 Ver 6.9.0
 * K Harrell	11/07/2011	add ERR_NUM_INVALID_WEIGHT_RANGE (40), 
 * 							  ERR_NUM_CARRYING_CAPACITY_CHG_REQD (431)
 * 							defect 10959 Ver 6.9.0    
 * K Harrell	12/08/2011  add ERR_NUM_MUST_SELECT_REBUILT_SALVAGE_FEE (44),
 * 							  ERR_NUM_OWNERSHIP_EVIDENCE_CANNOT_BE_SALVAGE (51)
 * 							defect 11051 Ver 6.9.0     
 * K Harrell	12/16/2011	delete  ERR_NUM_OWNERSHIP_EVIDENCE_MUST_BE_SALVAGE(42), 
 *                      	  ERR_NUM_MUST_SELECT_REBUILT_SALVAGE_FEE (44),
 * 							  ERR_NUM_OWNERSHIP_EVIDENCE_CANNOT_BE_SALVAGE (51)
 * 							defect 11051 Ver 6.9.0 
 * K McKee		01/09/2012  add PHONE_NUMBER_MUST_BE_10_NUMERIC_DIGITS, (5028)
 * 								AT_LEAST_ONE_ACTION_FOR_USER_OR_ADMINISTRATOR_MUST_BE_SELECTED, (5029)
 *		 						THERE_WAS_A_PROBLEM_WITH_THE_FIELD_AS_ENTERED, (5030)
 *								FIELD_MUST_BE_LESS_THAN_ONE_HNDRED_THOUSAND_DOLLARS, (5031) 
 *								THIS_SYSTEMS_LAST_CONNECTION_WAS_TO_LOCATION_X, (5032) 
 *								IF_USER_NAME_IS_ENTERED_THEN_FIRST_AND_LAST_NAME_MUST_BE_BLANK, (5033) 
 *								IF_LOCATION_ID_IS_ENTERED_THEN_LOCATION_NAME_AND_ZIP_CODE_MUST_BE_BLANK,  (5034) 
 *							defect 10729 Ver 6.10.0
 * K Harrell	01/16/2012	add ERR_NUM_MUST_REJECT_DTA_TRANS_WITH_SURVIVOR (42) 
 * 							defect 10827 Ver 6.10.0 
 * R Pilon		01/20/2012	add ERR_NUM_EDIR_PROXY_USER_AUTH_FAILURE, 
 * 							  ERR_NUM_WEBAGNT_UNKNOWN_AUTH_LDAP_ERR,
 * 							  ERR_NUM_WEBAGNT_EDIR_PWD_TEMPORARY
 * 							defect 11190 Ver 6.10.0
 * B Woodson	01/24/2012	add ERR_NUM_ACTION_INVALID_EXPORT
 * 							defect 11228 Ver 6.10.0
 * K Harrell	02/05/2012	add ERR_NUM_DATE_RANGE_CANNOT_EXCEED_15_DAYS (744) 
 * 							defect 11214 Ver 6.10.0 
 * K McKEe      02/07/2012  add ERR_NUM_WEBAGNT_NO_PLATE_FOUND_FOR_COUNTY(2351)
 * 							defect 11239
 * B Woodson	02/13/2012	add ERR_NUM_ACTION_INVALID_EXPORT_LEGAL_RESTRAINT
 * 							defect 11228 Ver 6.10.0
 * R Pilon		05/23/2012	add ERR_NUM_SOCKET_CONNECTION_EXCEPTION
 * 							defect 11073 Ver 7.0.0
 * --------------------------------------------------------------------- 
 */

/**
 * This class holds constants for Error Numbers and Message Text.
 *
 * @version	POS_700		05/23/2012
 * @author	Ray Rowehl
 * @since 			08/16/2005 07:01:00
 */

public class ErrorsConstant
{
	/**
	 * @value "Confirmation Screen   CTL001"
	 */
	public final static String CONFIRMATION_SCREEN =
		"Confirmation Screen";

	public static final String ERR_HELP_IS_NOT_AVAILABLE =
		"HELP IS NOT AVAILABLE AT THIS TIME.";

	// defect 10372 
	public final static int ERR_MSG_ADDRESS_UPDATED_UPON_APPROVAL = 988;
	// end defect 10372 

	/**
	 * @value "Data missing for IsNextVCREG029"
	 */
	public final static String ERR_MSG_DATA_MISSING_VC_REG029 =
		"Data missing for IsNextVCREG029";

	// defect 10290 
	public final static int ERR_MSG_DTA_RECORDS_DO_NOT_CONFORM = 237;
	public final static int ERR_MSG_DTA_MORE_THAN_MAX_TRANS = 262;
	public final static int ERR_MSG_DTA_DISKETTE_INVALID_DEALERID = 734;
	public final static int ERR_MSG_DTA_KEYBOARD_INVALID_DEALERID = 508;
	public final static int ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_PLT =
		538;
	public final static int ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_STKR =
		743;
	public final static int ERR_MSG_DTA_TRANS_MORE_THAN_60_DAYS_OLD =
		268;
	// end defect 10290

	// defect 10355 
	// public final static int ERR_MSG_DTA_TRANS_DATE_CANNOT_BE_IN_FUTURE
	public final static int ERR_MSG_DATE_CANNOT_BE_IN_FUTURE = 427;
	// end defect 10355   

	public final static String ERR_MSG_ERROR_TITLE = "ERROR";
	/**
	 * @value "Exception occurred in main()"
	 */
	public static final String ERR_MSG_EXCEPT_IN_MAIN =
		"Exception occurred in main() of com.txdot.isd.rts."
			+ "client.general.ui.RTSDialogBox";

	/**
	 * @value "Fee must be in the range of $0.00 to $99.99"
	 */
	public final static String ERR_MSG_FEE_OUTSIDE_CCO_RANGE =
		"Fee must be in the range of $0.00 to $99.99";

	/**
	 * Help is not available for this event at this time 
	 */
	public final static String ERR_MSG_HELP_NOT_AVAILABLE =
		"Help is not available for this event at this time.";

	public final static String ERR_MSG_HELP_NOT_AVAILABLE_TTL =
		"Help Not Available    HLP001";

	public final static String ERR_MSG_INVALID_FEE_TITLE =
		"Invalid Fee";

	/**
	 * @value "Item already rejected or deleted!"
	 */
	public static final String ERR_MSG_ITEM_REJ_OR_DEL_ALREADY =
		"Item already rejected or deleted!";
	/**
	 * @value "Some items failed in validation."
	 */
	public static final String ERR_MSG_ITEMS_FAILED =
		"Some items failed in validation.";
	/**
	 * @value "Exception occurred in main() of javax.swing.JDialog"
	 */
	public final static String ERR_MSG_MAIN_JDIALOG =
		"Exception occurred in main() of javax.swing.JDialog";

	// defect 10453 
	public final static int ERR_MSG_NO_REG_FOR_ATV_ROV = 2022;
	// end defect 10453 

	/**
	 * @value "No valid items in the invoice "
	 */
	public final static String ERR_MSG_NO_VALID_ITEMS_TO_RECEIVE =
		"No valid items in the invoice ";

	/**
	 * @value "There is no item to receive."
	 */
	public final static String ERR_MSG_THERE_IS_NO_ITEM_TO_RECEIVE =
		"There is no item to receive.";

	// defect 10127 
	/**
	 * Empty Weight is Invalid 
	 */
	public final static int ERR_NUM_EMPTY_WT_INVALID = 2;
	// end defect 10127 

	// defect 10624 
	/**
	 * Do not allow CCO w/ Title In Process 
	 */
	public final static int ERR_NUM_DO_NOT_ALLOW_CCO_TTL_IN_PROCS = 5;
	// end defect 10624 
	
	// defect 11051 
	 public final static int ERR_NUM_MUST_SELECT_REBUILT_SALVAGE = 41; 
//	 public final static int ERR_NUM_OWNERSHIP_EVIDENCE_MUST_BE_SALVAGE = 42;
//	 public final static int ERR_NUM_MUST_SELECT_REBUILT_SALVAGE_FEE = 44;
//	 public final static int  ERR_NUM_OWNERSHIP_EVIDENCE_CANNOT_BE_SALVAGE = 51;
	 // end defect 11051
	 
	 // defect 10827 
	 public final static int ERR_NUM_MUST_REJECT_DTA_TRANS_WITH_SURVIVOR = 42; 
	 
	 // end defect 10827 
	 
	 // defect 11061 
	 public final static int ERR_NUM_MUST_START_OR_END_WITH_DV = 43; 
	 // end defect 11061 
	 

	 
	// defect 10624 
	/**
	 * Replacement cannot be performed 
	 */
	public final static int ERR_NUM_REPL_CANNOT_BE_PERFORMED = 48;
	// end defect 10624 

	/**
	 * Registration can't be purchased in advance 
	 */
	public final static int ERR_NUM_NO_REG_IN_ADVANCE = 50;
	

	//	defect 10127
	/**
	 * Registration Change Required - Go to Addl Info 
	 */
	public final static int ERR_NUM_REG_CHG_REQD_GO_TO_ADDL_INFO = 92;
	// end defect 10127 

	// defect 10127 
	/**
	 * Authorizaton Code Invalid
	 */
	public final static int ERR_NUM_AUTH_CODE_INVALID = 116;
	// end defect 10127 
	
	// defect 11126 
	public final static int ERR_NUM_CARRYING_CAPACITY_GTE_THIRD_EMPTY_WT = 13;
	public final static int ERR_NUM_PLATES_ONLY_AVAILABLE_FROM_VTR = 18;
	public final static int ERR_NUM_PLATES_NON_TRANSFERRABLE = 84;
	public final static int ERR_NUM_INCREASE_GROSS_TO_MIN_FOR_FIXED_WT = 90;
	public final static int ERR_NUM_TRAILER_MUST_BE_TITLED_IF_NOT_JUST_FARM = 96;
	public final static int ERR_NUM_MUST_INCREASE_GROSS_WT_TO_TITLE_TRAILER= 142;
	public final static int ERR_NUM_MINOR_MAJOR_COLORS_CANNOT_BE_SAME = 160;
	public final static int ERR_NUM_VIN_REQUIRED_TO_TITLE = 176;
	public final static int ERR_NUM_GROSS_WT_INVALID_FOR_FARM_TRAILER = 202;
	public final static int ERR_NUM_TRAVEL_TRAILER_MUST_BE_LT_40_FT = 203;
	public final static int ERR_NUM_TITLE_TYPE_INVALID_FOR_VEHCLASS= 472;
	public final static int ERR_NUM_TRAILER_MUST_BE_TITLED_OR_REG_FARM_TRAILER = 580;
	public final static int ERR_NUM_TONNAGE_LE_2_FOR_DISABLED = 2010;
	// end defect 11126 
	
	// defect 10112
	/**	
	 * TX Invalid.  Registration must be issued by another state
	 */
	public final static int ERR_NUM_TX_INVALID_ANOTHER_STATE = 162;

	/**
	 * Enter a Valid Batch No
	 */
	public final static int ERR_NUM_ENTER_VALID_BATCH_NO = 181;
	// end defect 10112 

	public final static int ERR_NUM_NO_RECORDS_IN_DB = 182;

	// defect 10127 
	/**
	 * Incomplete Address Specified 
	 */
	public final static int ERR_NUM_INCOMPLETE_ADDR_DATA = 186;
	// end defect 10127  

	//	defect 10491 
	/**
	 * Incomplete Applicant Data
	 */
	public final static int ERR_NUM_INCOMPLETE_APPL_DATA = 184;
	// end defect 10491 

	public final static int ERR_NUM_INCOMPLETE_LIEN_DATA = 188;

	// defect 10112
	/**
	 * Incomplete Owner Data
	 */
	public final static int ERR_NUM_INCOMPLETE_OWNR_DATA = 189;

	/**
	 * Incomplete Previous Owner Data
	 */
	public final static int ERR_NUM_INCOMPLETE_PREV_OWNR_DATA = 190;

	/**
	 * Incomplete Renewal Address Data
	 */
	public final static int ERR_NUM_INCOMPLETE_REN_ADDR_DATA = 191;

	/**
	 * Incomplete Vehicle Address Data
	 */
	public final static int ERR_NUM_INCOMPLETE_VEH_ADDR_DATA = 192;
	// end defect 10112

	// defect 10127 
	public final static int ERR_NUM_MUST_INCR_CARRYING_CAPACITY = 200;
	// end defect 10127
	
	// defect 10112 
	/**
	 * RPO Cancelled  
	 */
	public final static int ERR_NUM_RPO_CANCELLED = 205;
	// end defect 10112
	
	// defect 11126 
	public final static int ERR_NUM_MUST_CHANGE_REGIS = 208; 
	// end defect 11126 


	// defect 10127 
	/**
	 * Error Number for Outstanding Hot Check 
	 */
	public final static int ERR_NUM_OUTSTANDING_HOT_CHECK = 231;
	// end defect 10127 

	// defect 10127 
	/**
	 * Class/Plate/Sticker is invalid - Go to Addl Info 
	 */
	public final static int ERR_NUM_CLASS_PLT_STKR_INVALID_TO_ADDL_INFO =
		411;
	// end defect 10127 

	// defect 10127
	/**
	 * Veh Weight must be changed - Go to Addl Info 
	 */
	public final static int ERR_NUM_VEH_WT_CHG_REQD_GO_TO_ADDL_INFO =
		423;
	// end defect 10127 
	
	// defect 10959 
	/**
	 * Carrying Capacity must be changed 
	 */
	public final static int ERR_NUM_CARRYING_CAPACITY_CHG_REQD = 431;
	// end defect 10959 

	/* 
	 * ACTION INVALID - COA 
	 */
	public final static int ERR_NUM_ACTION_INVALID_COA = 468;
	
	//defect 11228
	/* 
	 * ACTION INVALID - EXPORT 
	 */
	public final static int ERR_NUM_ACTION_INVALID_EXPORT = 53;
	
	/* 
	 * ACTION INVALID - "EXPORT" in Legal Restraint 
	 */
	public final static int ERR_NUM_ACTION_INVALID_EXPORT_LEGAL_RESTRAINT = 44;
	//end defect 11228

	/**
	 * ACTION INVALID - LEGAL RESTRAINT 
	 */
	public final static int ERR_NUM_ACTION_INVALID_LGL_REST = 428;

	/**
	 * ACTION INVALID - NON_REPAIR 
	 */
	public final static int ERR_NUM_ACTION_INVALID_NON_REPAIR = 654;

	/**
	 * ACTION INVALID - OFF_HIGHWAY 
	 */
	public final static int ERR_NUM_ACTION_INVALID_OFF_HWY = 466;

	/**
	 * ACTION INVALID - OUT OF STATE REGISTERED APPORTIONED 
	 */
	public final static int ERR_NUM_ACTION_INVALID_OS_APP = 469;

	/**
	 * ACTION INVALID - SALV CERT
	 */
	public final static int ERR_NUM_ACTION_INVALID_SALV_CERT = 467;

	// defect 10112
	/**
	 * Temp Gross weight can't exceed 80,000 
	 */
	public final static int ERR_NUM_TMP_GROSS_NOT_OVER_80000 = 473;
	// end defect 10112

	/**
	 * ACTION INVALID - SALV VEH TITLE 
	 */
	public final static int ERR_NUM_ACTION_INVALID_SALV_VEH_TITLE = 655;

	/**
	 * ACTION INVALID - STOLEN  (INSURANCE, NO REGIS) 
	 */
	public final static int ERR_NUM_ACTION_INVALID_STOLEN = 470;

	// defect 10103
	/**
	 * Active Directory JNI DLL is missing.
	 * 
	 * <p>AbstractValue is 750.
	 */
	public final static int ERR_NUM_AD_DLL_MISSING = 750;
	// end defect 10103

	// defect 11073
	/**
	 * ConnectionException attempting socket connection used to fire 
	 * thread event between RTSMainBE and RTSMainGUI.
	 */
	public final static int ERR_NUM_SOCKET_CONNECTION_EXCEPTION = 783;
	// end defect 11073

	/**
	 * Clerk must select at least one lienholder for ETtl Report
	 */
	public final static int ERR_NUM_AT_LEAST_ONE_LIENHLDR = 808;

	/**
	 * Clerk must select at least one transaction type for ETtl Report
	 */
	public final static int ERR_NUM_AT_LEAST_ONE_TRANS_TYPE = 807;

	/**
	 * Begin Date after End Date
	 */
	public final static int ERR_NUM_BEGIN_DATE_AFTER_END_DATE = 585;

	/**
	 * Begin Date IncorrectFormat
	 */
	public final static int ERR_NUM_BEGIN_DATE_INCORRECT_FORMAT = 954;

	/**
	 * Deposit Reconciliation Date must be >= 1/14/2009
	 */
	public final static int ERR_NUM_BEGIN_DEPOSIT_DATE_TOO_EARLY = 781;

	/**
	 * Body Style is not valid  
	 */
	public final static int ERR_NUM_BODY_STYLE_NOT_VALID = 2009;

	/**
	 * Both diskette and flashdrive external media has been inserted.
	 * One must be removed before continuing.
	 * 
	 * <p>AbstractValue is 1015.
	 */
	public final static int ERR_NUM_BOTH_DEVICES_IN_USE = 1015;

	/**
	 * Error Number that indicates bonded title can't be changed 
	 */
	public final static int ERR_NUM_CANNOT_CHANGE_BONDED_TITLE = 16;

	/**
	 * CANNOT FIND SUBCONTRACTOR ID 
	 */
	public final static int ERR_NUM_CANNOT_FIND_SUBCONTRACTOR_ID = 701;

	public final static int ERR_NUM_CANNOT_PERFORM_REJCOR = 17;

	/**
	 * Trans cannot be Voided as it cannot be retrieved.
	 */
	public final static int ERR_NUM_CANNOT_RETRIEVE_CANNOT_VOID = 331;

	/**
	 * Error Number that CCO Issued within past given interval (30 days) 
	 */
	// defect 10639 
	//public final static int ERR_NUM_CCO_WITHIN_PAST_TWO_WEEKS = 4;
	public final static int ERR_NUM_CCO_WITHIN_SPECIFIED_INTERVAL = 4;
	// end defect 10639 

	/**
	 * Certified Lienholder is not Available 
	 */
	public final static int ERR_NUM_CERTFD_LIENHLDR_NOT_AVAIL = 805;

	/**
	 * Must enter a comment for Decline 
	 */
	public final static int ERR_NUM_COMMENT_REQD_FOR_DECLINE = 966;

	/**
	 * Must enter a comment for Hold 
	 */
	public final static int ERR_NUM_COMMENT_REQD_FOR_HOLD = 965;

	/**
	 * Error Number that indicates the item number computed is invalid. 
	 */
	public final static int ERR_NUM_COMPUTED_NUMBER_INVALID = 10;

	/**
	 * Connection Failure has occurred 
	 */
	public final static int ERR_NUM_CONNECTION_FAILURE_HAS_OCCURRED =
		972;

	/**
	 * Error Number that indicates the item range could not be 
	 * computed.
	 */
	public final static int ERR_NUM_COULD_NOT_COMPUTE = 11;

	/**
	/**
	 * Date entered is greater than max date available for report. 
	 */
	public final static int ERR_NUM_DATE_GT_MAX_RPT_DATE = 782;

	/**
	 * Invalid Date
	 */
	public final static int ERR_NUM_DATE_NOT_VALID = 733;

	/**
	 * Date Range Over 31 Days  
	 */
	public final static int ERR_NUM_DATE_RANGE_INVALID = 739;

	// defect 11214 
	/**
	 * Date Range Over 15 Days  
	 */
	public final static int ERR_NUM_DATE_RANGE_CANNOT_EXCEED_15_DAYS = 744;
	// end defect 11214 

	
	/**
	 * Date range can only be 11 days into the past.
	 */
	public final static int ERR_NUM_DATE_RANGE_INVALID_11_DAYS = 581;

	/**
	 * Range is too large 
	 */
	public final static int ERR_NUM_DATE_RANGE_TOO_LARGE = 958;

	/**
	 * Date not within 30 days 
	 */
	public final static int ERR_NUM_DATE_WITHIN_30_DAYS = 959;

	/**
	 * DATABASE SERVER UNAVAILABLE
	 */
	public final static int ERR_NUM_DBSERVER_UNAVAILABLE = 618;

	/**
	 * Must enter Decline Reason 
	 */
	public final static int ERR_NUM_DECLINE_REASN_REQD = 964;

	public final static int ERR_NUM_RECON_HSTRY_INSERT_ERROR = 2200;
	public final static int ERR_NUM_COULD_NOT_GET_MAX_DEPOSITRECONHSTRY =
		2201;
	public final static int ERR_NUM_FTP_PROPERTIES_FILE_ERROR = 2202;
	public final static int ERR_NUM_DEPOSIT_FILE_STRING_TOKENIZER_ERROR =
		2203;
	public final static int ERR_NUM_DEPOSIT_PROPERTIES_FILE_LOAD_ERROR =
		2204;
	public final static int ERR_NUM_DEPOSIT_FILE_VALUES_PARSING_ERROR =
		2205;
	public final static int ERR_NUM_DEPOSIT_FILE_RETRIEVAL_ERROR = 2206;
	public final static int ERR_NUM_DEPOSIT_FILE_SEND_EMAIL_ERROR =
		2207;

	public final static int ERR_NUM_TRACE_NUM_EXISTS_ON_PREVIOUS_FILE_ERROR =
		2209;
	public final static int ERR_NUM_DEPOSIT_FILE_RENAME_ERROR = 2210;
	// defect 10262
	public final static int ERR_NUM_DEP_REC_DATA_NOT_IN_NAME = 2211;
	public final static int ERR_NUM_FILE_NAME_DATE_NOT_EQUAL_DEP_DATE =
		2212;
	public final static int ERR_NUM_DEPOSIT_FILE_MISSING = 2213;
	public final static int ERR_NUM_FTP_CONNECTION_FAILED = 2214;
	public final static int ERR_NUM_FTP_FAILED_LOGIN = 2215;
	public final static int ERR_NUM_FTP_FAILED_TRANSFER_TYPE = 2216;
	public final static int ERR_NUM_FTP_FAILED_ACTIVE_MODE = 2217;
	public final static int ERR_NUM_DEPOSIT_PROPERTIES_FILE_MISSING =
		2218;
	public final static int ERR_NUM_DEPOSIT_FILE_EMPTY = 2219;
	public final static int ERR_NUM_DEPOSIT_DATE_NOT_YYYYMMDD = 2220;
	public final static int ERR_NUM_DEPOSIT_COUNTY_NUMBER_ERROR = 2221;
	public final static int ERR_NUM_DEPOSIT_INVALID_CREDIT_CARD_TYPE =
		2222;
	public final static int ERR_NUM_DEPOSIT_INVALID_TRACE_NUMBER = 2223;
	public final static int ERR_NUM_DEPOSIT_LAST_4_CREDIT_CARD = 2224;
	public final static int ERR_NUM_DEPOSIT_INVALID_PAYMNT_AMT = 2225;
	public final static int ERR_NUM_TOL_DATE_NOT_YYYYMMDD = 2226;
	public final static int ERR_NUM_DEPOSIT_WRONG_NUM_FIELDS = 2227;
	public final static int ERR_NUM_DEPOSIT_RECON_HSTRY_UPDATE_ERROR =
		2228;
	public final static int ERR_NUM_DEPOSIT_MOVE_REMOTE_FILE_ERROR =
		2229;
	public final static int ERR_NUM_STATIC_LOAD_ERROR = 2230;
	// end defect 10262

	// defect 10491
	public final static int ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS = 20;
	public final static int ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED_REFINE =
		745;
	public final static int ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED = 746;
	public final static int ERR_NUM_PRMT_CNTRY_OF_REG_INVALID = 747;
	public final static int ERR_NUM_PERMIT_HAS_EXPIRED = 2121;
	public final static int ERR_NUM_THREE_OR_MORE_30_DAY_PERMITS = 2122;
	// end defect 10491 	
	
	// defect 10844
	public final static int ERR_NUM_NOT_AVAIL_FOR_REPRINT = 334; 
	public final static int ERR_NUM_CANNOT_MODIFY_VENDOR_ISSUED = 2123;
	public final static int ERR_NUM_CANNOT_BE_MORE_THAN_12_MONTHS_FUTURE = 2124;
	// end defect 10844 
	

	/**
	 * DocType unavailable for CCO
	 */
	public final static int ERR_NUM_DOC_TYPE_UNAVAILABLE_FOR_CCO = 100;

	/**
	 * Employee Id length can not be less then two characters.
	 */
	public final static int ERR_NUM_EMP_ID_TOO_SHORT = 753;

	/**
	 * End Date Incorrect Format
	 */
	public final static int ERR_NUM_END_DATE_INCORRECT_FORMAT = 955;

	/**
	 * Enter Search Parameter
	 */
	public final static int ERR_NUM_ENTER_SEARCH_PARAMETERS = 950;

	/**
	 * Item Exists in database when working with Invoice.
	 */
	public final static int ERR_NUM_ENTER_VIN_OR_CHECK_NO_VIN = 147;

	/**
	 * The Entity is invalid.
	 */
	public final static int ERR_NUM_ENTITY_INVALID = 601;

	/**
	 * Error has occurred 
	 */
	public final static int ERR_NUM_ERROR_HAS_OCCURRED = 973;

	/**
	 * An Electronic Title is not available with more than one lien
	 */
	public final static int ERR_NUM_ETTL_MAX_ONE_LIEN = 806;

	public final static int ERR_NUM_EXPIRED_VENDOR_PLATE = 2120;

	/**
	 * Neither diskette nor flashdrive external media has been inserted.
	 * 
	 * <p>AbstractValue is 1014.
	 */
	public final static int ERR_NUM_EXTERNAL_MEDIA_NOT_FOUND = 1014;

	/**
	 * FEE BELOW MINIMUM REG FEE 
	 */
	public final static int ERR_NUM_FEE_BELOW_MIN_REG_FEE = 706;

	/**
	 * The field entry is invalid.
	 */
	public final static int ERR_NUM_FIELD_ENTRY_INVALID = 150;

	/**
	 * Gross weight exceeds 80,000 
	 */
	public final static int ERR_NUM_GROSS_WT_EXCEEDS_MAX = 89;

	/**
	 * Minimum Gross Weight Invalid - Select Change Veh Wt 
	 */
	public final static int ERR_NUM_GROSS_WT_INVALID_CHNG_WT = 418;

	/**
	 * Minimum Gross Weight Invalid for RegClass/Tire 
	 */
	public final static int ERR_NUM_GROSS_WT_INVALID_FOR_REG_TIRE = 419;

	/**
	 * The History date entered is invalid
	 */
	public final static int ERR_NUM_HIST_DATE_INVALID = 736;

	/**
	 * Must enter Hold Reason 
	 */
	public final static int ERR_NUM_HOLD_REASN_REQD = 963;

	/**
	 * THE RANGE SPECIFIED IS NOT COMPLETE ON THE database.
	 */
	public final static int ERR_NUM_INCOMPLETE_RANGE = 605;

	/**
	 * The Inventory Inquiry history begin or end date is incorrect.
	 */
	public final static int ERR_NUM_INV_INQUIRY_DATE_INCORRECT = 600;

	/**
	 * Invalid Begin or End Date 
	 */
	public final static int ERR_NUM_INVALID_BEGIN_END = 981;

	/**
	 * DOC TYPE INVALID 
	 */
	public final static int ERR_NUM_INVALID_DOC_TYPE = 429;

	/**
	 * The Invoice Number entered is not valid.
	 */
	public final static int ERR_NUM_INVALID_INVOICE_NUMBER = 154;

	/**
	 * The quantity does not fall within the valid range.
	 */
	public final static int ERR_NUM_INVALID_QTY = 591;

	/**
	 * Action Cannot be Performed; Reg Invalid;
	 */
	public final static int ERR_NUM_INVALID_REG_NO_ACTION = 161;

	/**
	 * Invalid State - Please Reenter  
	 */
	public final static int ERR_NUM_INVALID_STATE_REENTER = 2004;

	/**
	 * The Invoice specified has already been received.
	 */
	public final static int ERR_NUM_INVOICE_ALREADY_RECEIVED = 602;

	/**
	 * Error Number that indicates that the Invoice could 
	 * not be found on the MainFrame.
	 */
	// defect 8265
	//public final static int ERR_NUM_INVOICE_NOT_ON_MF = 1;
	public final static int ERR_NUM_INVOICE_NOT_ON_MF = 990;
	// end defect 8265

	/**
	 * The Invoice Number being received is not for this county.
	 */
	public final static int ERR_NUM_INVOICE_NOT_THIS_COUNTY = 285;

	/**
	 * THE INVENTORY ITEM IS CURRENTLY ON HOLD.
	 */
	public final static int ERR_NUM_ITEM_ON_HOLD = 594;

	/**
	 * THE ITEMS ON RTS "HOLD" COULD NOT BE RETURNED TO INVENTORY 
	 * - CONTACT VTR HELP DESK.
	 */
	public final static int ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN = 593;

	/**
	 * Error Number that indicates the item code and year combination
	 * could not be found in Item Code Patterns. 
	 */
	public final static int ERR_NUM_ITM_CD_YR_NOT_IN_PATTERNS = 102;

	/**
	 * Item Exists in database when working with Invoice.
	 */
	public final static int ERR_NUM_ITM_EXISTS = 130;

	/**
	 * Item Year entered is not valid.
	 */
	public final static int ERR_NUM_ITM_YEAR_NOT_VALID = 153;

	// defect 10103
	/**
	 * User was logged in through cache validation.
	 * Database was not available.
	 * 
	 * <p>AbstractValue is 773.
	 * <p>This was previously 618.  Database down.
	 */
	public static final int ERR_NUM_LOGIN_SUCCESS_FROM_CACHE = 773;
	// end defect 10103
	
	// defect 10774
	public final static int ERR_NUM_DATE_RANGE_CANNOT_EXCEED = 774;
	// end defect 10774  

	/**
	 * The Maximum quantity is less than the Minimum quantity.
	 * This is not allowed.
	 */
	public final static int ERR_NUM_MAX_QTY_IS_LESS_THAN_MIN = 87;

	/**
	 * Model Year is not valid  
	 */
	public final static int ERR_NUM_MODEL_YEAR_NOT_VALID = 2006;

	/**
	 * Begin Date more than 30 days in the past
	 */
	public final static int ERR_NUM_MORE_THAN_30_DAYS_IN_PAST = 956;

	/**
	 * The user must select a county.
	 */
	public final static int ERR_NUM_MUST_SELECT_A_COUNTY = 737;

	// defect 10127 
	/**
	 * The user must go to Additional Info to Select Exp Reason
	 */
	public final static int ERR_NUM_MUST_SELECT_EXP_REASON = 735;
	/**
	 * Renewal Notice cannot be printed.
	 */
	public final static int ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE = 738;
	// end defect 10127 

	/**
	// defect 10127 
	/**
	 * Present error when valid state in cntry field 
	 */
	public final static int ERR_NUM_VALID_STATE_IN_CNTRY_FIELD = 809;
	// end defect 10127 

	// defect 8763
	/**
	 * No Action Command Specified 
	 */
	public final static int ERR_NUM_NO_ACTION_COMMAND_SPECIFIED = 967;

	/**
	 * There were no check boxes selected.
	 */
	public final static int ERR_NUM_NO_CHKBOXES_SELECTED = 599;

	/**
	 * No Hold In Queue, New Displayed 
	 */
	public final static int ERR_NUM_NO_HOLD_NEW_DISPLAYED = 977;

	/**
	 * No items were selected.
	 */
	public final static int ERR_NUM_NO_ITEMS_SELECTED = 598;

	/**
	 * Error Number that indicates no lien recorded on the vehicle 
	 */
	public final static int ERR_NUM_NO_LIEN_ON_VEHICLE = 83;

	/**
	 * No New In Queue, Hold Displayed 
	 */
	public final static int ERR_NUM_NO_NEW_HOLD_DISPLAYED = 976;

	/**
	 * Error Number that indicates no record was found 
	 */
	public final static int ERR_NUM_NO_RECORD_FOUND = 57;

	/**
	 * Error Number that indicates no record was found. 
	 * Continue Processing. 
	 */
	public final static int ERR_NUM_NO_RECORD_FOUND_CONTINUE = 211;

	/**
	 * No Record Selected 
	 */
	public final static int ERR_NUM_NO_RECORD_SELECTED = 960;

	/**
	 * NO REPORTS SELECTED
	 */
	public final static int ERR_NUM_NO_REPORTS_SELECTED = 614;

	/**
	 * No Reports (in category) that can be printed
	 */
	public final static int ERR_NUM_NO_REPORTS_THAT_CAN_PRINT = 389;

	/**
	 * No Transactions Available in Queue 
	 */
	public final static int ERR_NUM_NO_TRANS_IN_QUEUE = 975;

	/**
	 * There are no Transactions.  No report to view.
	 */
	public final static int ERR_NUM_NO_TRANS_NO_REPORT = 596;

	/**
	 * User must select some inventory to delete.
	 */
	public final static int ERR_NUM_NOTHING_SELECTED_TO_DELETE = 588;

	/**
	 * Null Internet Data 
	 */
	public final static int ERR_NUM_NULL_INTERNET_DATA = 986;

	/**
	 * Null Object Returned 
	 */
	public final static int ERR_NUM_NULL_OBJECT = 983;

	/**
	 * Odometer invalid based on TIMA.
	 */
	public final static int ERR_NUM_ODOMETER_INVALID_BASED_ON_TIMA =
		711;

	/**
	 * AT LEAST ONE SELECTION MUST BE PICKED. 
	 */
	public final static int ERR_NUM_ONE_SELECTION_MUST_BE_PICKED = 653;
	// end defect 8749 

	/**
	 * ONLY ONE SELECTION CAN BE MADE 
	 */
	public final static int ERR_NUM_ONLY_ONE_SELECTION_CAN_BE_MADE =
		652;

	/**
	 * Begin Date more than 60 days in past  
	 */
	public final static int ERR_NUM_OVER_60_DAYS_IN_PAST = 982;

	/**
	 * THE PATTERN SEQUENCE CODES DO NOT MATCH.
	 */
	public final static int ERR_NUM_PATRN_SEQ_NOT_RIGHT = 607;

	/**
	 * Plate Cancelled. Processing cannot continue.
	 */
	public final static int ERR_NUM_PLT_CANCELLED = 729;

	/**
	 * Plate or Sticker Cancelled. Processing cannot continue.
	 */
	public final static int ERR_NUM_PLT_OR_STKR_CANCELLED = 537;

	/**
	 * Permit Date cannot be less than current date
	 */
	public final static int ERR_NUM_PRMT_DATE_NOT_LESS_THAN_CURRENT =
		169;
	// defect 10262
	// public final static int ERR_NUM_RECON_HSTRY_INSERT_ERROR = 2200;
	// end defect 10262 

	/**
	 * Error Number that indicates the record could not be retrieved
	 * but that the request can be processed. 
	 */
	public final static int ERR_NUM_RECORD_CANNOT_BE_RETRIEVED_CONTINUE =
		20;

	/**
	 * Record checked out by someone else 
	 */
	public final static int ERR_NUM_RECORD_CHECKED_OUT = 962;

	/**
	 * Unable to retreive records from MainFrame.
	 */
	public final static int ERR_NUM_RECORD_RETRIEVAL_DOWN = 1;

	/**
	 * Record has been updated 
	 */
	public final static int ERR_NUM_RECORD_UPDATED = 978;

	/**
	 * Error Number that indicates that Salvage Certificate cannot be
	 * issued for COA. 
	 */
	public final static int ERR_NUM_SALVAGE_NOT_AVAILABLE_FOR_COA = 24;

	/**
	 * Error Number that indicates that Salvage Certificate cannot be
	 * issued for Non_Titled. 
	 */
	public final static int ERR_NUM_SALVAGE_NOT_AVAILABLE_FOR_NONTTL =
		97;

	/**
	 * SPV Service Unavailable 
	 */
	public final static int ERR_NUM_SPV_SVC_UNAVAILABLE = 2012;

	/**
	 * The new Supervisor Override Code cannot be the same as the 
	 * Current Code.
	 */
	public final static int ERR_NUM_SUPER_OVERRIDE_CODE_SAME = 410;

	/**
	 * Error Number that indicates too many record found with key  
	 */
	public final static int ERR_NUM_TOO_MANY_RCDS_FOUND = 148;
	// defect 10262
	//	public final static int ERR_NUM_TRACE_NUM_EXISTS_ON_PREVIOUS_FILE_ERROR =
	//		2209;
	// end defect 10262	

	/**
	 * Trans invalid for Special Registration  
	 */
	public final static int ERR_NUM_TRANS_INVALID_FOR_SPCL_REG = 149;

	/**
	 * Enter Transaction Id Incorrect Format
	 */
	public final static int ERR_NUM_TRANSACTION_ID_INCORRECT_FORMAT =
		957;

	// defect 10103
	/**
	 * UserName is not valid in security table.
	 * 
	 * <p>AbstractValue is 54.
	 */
	public static final int ERR_NUM_USERNAME_NOT_VALID = 54;
	// end defect 10103

	public final static int ERR_NUM_V21_INVALID_REQUEST = 2103;
	public final static int ERR_NUM_V21_MULTIPLE_RECS = 2104;
	public final static int ERR_NUM_V21_NOT_AVAILABLE = 2101;
	public final static int ERR_NUM_V21_NOT_FOUND = 2102;
	public final static int ERR_NUM_V21_REGEXPYRMO_DO_NOT_MATCH = 2105;

	public final static int ERR_NUM_V21_SUCCESS = 2100;

	/**
	 * Error Number that indicates Vehicle Tonnage Invalid 
	 */
	public final static int ERR_NUM_VEH_TONNAGE_INVALID = 201;

	/**
	 * Verify all Liens released
	 */
	public final static int ERR_NUM_VERIFY_LIENS_RELEASED = 366;

	/**
	 * The Item intersects with an existing item.
	 * Can not Insert!
	 * 
	 * <p>AbstractValue is 1011.
	 */
	public final static int ERR_NUM_VI_INTERSECTION = 1011;

	/**
	 * The Item is requested is not available.
	 * This is the generic message used on PLP Validations
	 * returned to the user instead of the specific ones entered 
	 * on the log file.
	 * 
	 * <p>AbstractValue is 1010.
	 */
	public final static int ERR_NUM_VI_ITEM_NOT_AVAILABLE = 1010;

	/**
	 * The Item requested is not available to requestor.
	 * It is not Reserved.
	 * 
	 * <p>AbstractValue is 1009.
	 */
	public final static int ERR_NUM_VI_ITEM_NOT_RESERVED = 1009;

	/**
	 * There is no Virtual Inventory available for this request.
	 * 
	 * <p>AbstractValue is 1002.
	 */
	public final static int ERR_NUM_VI_NEXTITEM_UNAVAILABLE = 1002;

	/**
	 * Personalized Plate matches an existing plate.
	 * 
	 * <p>AbstractValue is 1006.
	 */
	public final static int ERR_NUM_VI_PER_EXISTS_MF = 1006;

	/**
	 * Personalized Plate matches an Objectionable plate.
	 * 
	 * <p>AbstractValue is 1007.
	 */
	public final static int ERR_NUM_VI_PER_EXISTS_OBJECTIONABLE = 1007;

	/**
	 * Personalized Plate matches a Reserved plate.
	 * 
	 * <p>AbstractValue is 1008.
	 */
	public final static int ERR_NUM_VI_PER_EXISTS_RESERVED = 1008;

	/**
	 * Personalized Plate matches a Reserved plate reserved for a 
	 * different office.
	 * 
	 * <p>AbstractValue is 1013.
	 */
	public final static int ERR_NUM_VI_PER_EXISTS_RESERVED_DIFFERENT_COUNTY =
		1013;

	/**
	 * Personalized Plate exists in Virtual Inventory.
	 * 
	 * <p>AbstractValue is 1003.
	 */
	public final static int ERR_NUM_VI_PER_EXISTS_VI = 1003;

	/**
	 * Personalized Plate has an Invalid Letter Combination.
	 * 
	 * <p>AbstractValue is 1005.
	 */
	public final static int ERR_NUM_VI_PER_HAS_INVALID_LTR = 1005;

	/**
	 * Personalized Plate matches an existing pattern.
	 * 
	 * <p>AbstractValue is 1004.
	 */
	public final static int ERR_NUM_VI_PER_MATCHES_PTRN = 1004;

	/**
	 * Virtual Inventory is not available.
	 * 
	 * <p>AbstractValue is 1001.
	 */
	public final static int ERR_NUM_VI_UNAVAILABLE = 1001;

	/**
	 * VIN is not 17 digits
	 */
	public final static int ERR_NUM_VIN_NOT_17_DIGITS = 555;

	/**
	 * The object sent would have caused a ClassCast Exception.
	 * The exception was prevented, but this is a fatal error.
	 * RTSException sent back to requestor.
	 * 
	 * <p>AbstractValue is 1012.
	 */
	public final static int ERR_NUM_WRONG_OBJECT_TYPE = 1012;

	/**
	 * ZIP CODE must be 5 digits  
	 */
	public final static int ERR_NUM_ZIP_CODE_MUST_BE_5_DIGITS = 2007;

	/**
	 * ZIP CODE P4 must be 4 digits  
	 */
	public final static int ERR_NUM_ZIP_CODEP4_MUST_BE_4_DIGITS = 2008;

	// defect 10402	
	/**
	 * Plate Number must be less than or equal to 7 characters  
	 */
	public final static int ERR_NUM_PLATENO_TOO_LONG = 2014;
	// end defect 10402	

	//	Update message
	//public static final String DISKDRIVEFAILURE =
	public static final String INPUT_MEDIA_FAILURE_ERRMSG =
		//"Diskette is corrupt or the dealer file DLRTITLE.DAT does "
	"Dealer external media is corrupt or the dealer file "
		+ "DLRTITLE.DAT does not exist. "
		+ "Contact the RTS help Desk for further assistance.";
	// Add error message constant
	//public static final String DISKERROR =
	public static final String INSERT_MEDIA_MSG =
		//"Insert diskette containing title application in Diskette Drive \"A\"";
	"Insert external media containing title application";

	/**
	 * DTA Error messages.
	 */
	// defect 10075
	//public static final String RSPSIDRSPSDISKNO =
	public static final String MEDIA_ID_ERRMSG =
		//"The RSPSId or the RSPS Disk Number is not"
	"The RSPSId or the RSPS Sequence Number is not"
		+ " consistent for all records.";

	/**
	 * @value "Are these values correct?"
	 */
	public static final String MSG_ARE_VALUES_CORRECT =
		"Are these values correct?";

	/**
	 * @value "Note: Deletion occurs when the item appears in the area 
	 * below."
	 */
	public static final String MSG_DELETE_AREA_WARNING =
		"Note: Deletion occurs when the item appears in the area below.";

	/**
	 * @value "Inventory Item will now be deleted. Do you want to 
	 * continue?"
	 */
	public static final String MSG_INV_DELETE_CONFIRM =
		"Inventory Item will now be deleted. Do you want to continue?";

	/**
	 * @value "The items below have been successfully allocated during 
	 * this Allocation Session"
	 */
	public static final String MSG_ITEMS_SUCCESS_ALLOC =
		"The items below have been successfully allocated during "
			+ "this Allocation Session";

	public static final String MSG_NO_DEALERS_EXIST =
		"NO DEALERS EXIST";

	public static final String MSG_NO_RECORDS_FOUND =
		"NO RECORDS WERE FOUND ON THE DATABASE";

	public final static String MSG_NO_SUBCONS_EXIST =
		"NO SUBCONTRACTORS EXIST";

	/**
	 * @value "Unexpected problem" 
	 */
	public static final String MSG_UNEXPECTED_PROBLEM =
		" Unexpected problem ";

	public static final String RSPSNOTMATCHDTA1 =
		//"The records on the diskette do not match the original dealer records."
	"The records on the external media do not match the original "
		+ "dealer records."
		//+ " \n Please insert diskette \n";
	+" \n Please insert input media \n";
	public static final String RSPSNOTMATCHDTA2 = " with RSPS ID of \n";

	public static final String RSPSUPDTERROR =
		//"Error occurred when updating RSPS information on diskette. "
		//	+ "Dealer diskette will not be updated.";
	"Error occurred when updating RSPS information on external media. "
		+ "Dealer data on external media will not be updated.";

	public static String VEH_DATA_MISSING_EXCEPTION =
		"An incomplete data "
			+ "problem has occurred with plate number PLTNO. "
			+ "Contact VTR Help Desk.";

	// IRenew transaction processing; Vehicle Data missing (ITRNT_DATA)
	public static final String VEH_DATA_MISSING_EXCEPTION_TITLE =
		"Vehicle Data Missing";

	public static String VEH_DATA_MISSING_EXCEPTION_TXT_TO_REPLACE =
		"PLTNO";
	// defect 10512
	public final static int ERR_NUM_BUILD_EMAIL_MESSAGE_ERROR = 2231;
	public final static int ERR_NUM_ERROR_RETRIEVING_EMAIL_LIST = 2232;
	public final static int ERR_NUM_INVALID_REG_EXP_MO = 2233;
	public final static int ERR_NUM_INVALID_REG_EXP_YR = 2234;
	// the below 2 were already in rts_err_msgs, but not in ErrorsConstant
	public final static int ERR_NUM_INVALID_VEHICLE_MAKE = 93;
	public final static int ERR_NUM_INVALID_COUNTY = 173;
	// end defect 10512	

	// defect 10679 
	public final static int ERR_NUM_TEMP_INVALID_FOR_INST = 25;
	// end defect 10679 

	// defect 10348 
	public final static int ERR_NUM_PAYMENT_IN_ERROR = 30;
	// end defect 10348

	// defect 10700 
	public final static int ERR_NUM_EXPIRED_INSIGNIA = 26;
	// end defect 10700  

	// defect 11050 
	public final static int ERR_NUM_NUM_PLACARDS_OUTSIDE_RANGE = 29;
	// end defect 11050
	
	// defect 10959 
	public final static int ERR_NUM_INVALID_WEIGHT_RANGE = 40;
	// end defect 10959
	
	// defect 10631 
	public final static int ERR_NUM_LIEN_DATE_INVALID = 32;
	// end defect 10631  

	//	defect 10741 
	public final static int ERR_NUM_MULTIPLE_OF_BASE_FEE = 33;
	// end defect 10741  

	// defect 10734 
	public final static int ERR_NUM_WEBAGNT_TRANS_EXIST = 35;
	public final static int ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL = 38;
	// end defect 10734 
	
	// defect 11004 
	public final static int ERR_NUM_MIN_FIELD_LENGTH_2 = 39;
	// end defect 11004

	// defect 10768 
	public final static int ERR_NUM_WEBAGNT_GENERAL_ERROR = 2300;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_NO_RECORD_FOUND =
		2301;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_MULT_RECORDS_FOUND =
		2302;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_NO_MATCH_LAST_4_VIN =
		2303;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_AGNT_NOT_AUTHORIZED_FOR_COUNTY =
		2304;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_AGNT_MUST_SCAN =
		2305;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_REG_TOO_FAR_IN_ADVANCE =
		2306;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_REG_EXPIRED_BEYOND_AUTH =
		2307;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_HARDSTOP_EXISTS =
		2308;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_INPROCESS_TRANS_EXISTS =
		2309;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_INVALID_DOCTYPECD =
		2310;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_LOC_NOT_AUTHORIZED =
		2311;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_GROSS_WT_EXCESSIVE =
		2312;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_REGCLASSCD_NOT_ELIGIBLE =
		2313;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_LOC_NOT_AUTHORIZED_FOR_INV =
		2314;
	public final static int ERR_NUM_WEBAGNT_RENEWAL_RECEIPT_ERROR =
		2315;
	public final static int ERR_NUM_WEBAGNT_LOOKUP_AGNT_MUST_KEYBOARD =
		2319;
	public final static int ERR_NUM_WEBAGNT_BATCH_TOO_MANY_ROWS = 2320;
	public final static int ERR_NUM_WEBAGNT_BATCH_START_END_TOO_FAR_APART =
		2322;
	public final static int ERR_NUM_WEBAGNT_INVALID_LOOKUP_REQUEST =
		2323;
	public final static int ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND =
		2324;
	public final static int ERR_NUM_WEBAGNT_NO_WEB_AGENCY_AUTH = 2325;
	public final static int ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_FOUND =
		2326;
	public final static int ERR_NUM_WEBAGNT_AGENCY_BATCH_HAS_NO_TRANS =
		2327;
	public final static int ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_AVAIL_FOR_REQUEST =
		2328;
	public final static int ERR_NUM_WEBAGNT_AGENT_NOT_FOUND = 2329;
	public final static int ERR_NUM_WEBAGNT_INVALID_OFC_WSID_DATA =
		2330;
	public final static int ERR_NUM_WEBAGNT_RTS_SECURITY_NOT_FOUND =
		2331;
	public final static int ERR_NUM_WEBAGNT_APPROVAL_NOT_SUCCESSFUL =
		2332;
	public final static int ERR_NUM_WEBAGNT_AGENCY_NOT_FOUND = 2333;
	public final static int ERR_NUM_WEBAGNT_INVALID_TRANS_HDR = 2334;
	public final static int ERR_NUM_WEBAGNT_TRANS_NOT_FOUND = 2335;
	public final static int ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL_TO_VOID = 2336;
	public final static int ERR_NUM_WEBAGNT_NO_TRANS_AVAIL_TO_ACCPT = 2337;
	// end defect 10768

	// defect 10760
	/**
	 * The Web Agent Session is not valid.
	 */
	public final static int ERR_NUM_WEBAGNT_SESSION_ERR = 2316;

	/**
	 * Agent does not have authorization to perform requested action.
	 */
	public final static int ERR_NUM_WEBAGNT_AUTHORIZATION_ERR = 2317;

	/**
	 * Session Time Out on POS Side.
	 */
	public final static int ERR_NUM_SESSION_TIMEOUT = 2318;

	/**
	 * Agent failed authentication LDAP lookup.
	 */
	public final static int ERR_NUM_WEBAGNT_AUTH_LDAP_ERR = 2321;
	
	/**
	 * Agent failed authentication because there is no Agent Record.
	 */
	public final static int ERR_NUM_WEBAGNT_AUTH_TAB_ERR = 2338;
	
	/**
	 * Agent failed authentication because there is no Agent Record.
	 */
	public final static int ERR_NUM_WEBAGNT_AUTH_TAB2_ERR = 2339;
	
	/**
	 * The Web Agent Session is not valid multiple.
	 */
	public final static int ERR_NUM_WEBAGNT_SESSION_MULT_ERR = 2340;
	
	/**
	 * eDir Server Error.
	 */
	public final static int ERR_NUM_WEBAGNT_EDIR_SOAP_ERR = 2341;

	public final static int ERR_MSG_MAX_LNG_WEB_SERVICES = 100;
	// end defect 10760
	
	// defect 10670
	/**
	 * eDirectory password is expired or temporary.
	 */
	public final static int ERR_NUM_WEBAGNT_EDIR_PWD_EXPIRED = 2342;

	/**
	 * eDirectory security responses are missing.
	 */
	public final static int ERR_NUM_WEBAGNT_EDIR_NO_REPSONSES = 2343;

	/**
	 * eDirectory account is locked.
	 */
	public final static int ERR_NUM_WEBAGNT_EDIR_LOCKED = 2344;
	// end defect 10670

	// defect 10670
	/**
	 * user id and/or password are required
	 */
	public final static int ERR_NUM_USERID_PWD_REQUIRED = 2345;
	// end defect 10670
	
	// defect 10729
	/**
	 * Unsubmitted batches exist for agency delete
	 */
	public final static int MSG_UNSUBMITTED_BATCHES_FOR_DELETED_AGENCY = 2346;
	/**
	 * Logon info not found in the RTS.RTS_LOG_FUNC_TRANS table
	 */
	public final static int  LOGIN_INFO_NOT_FOUND_IN_LOG_FUNCTION_TRANS = 2347;
	// end defect 10729
	
	// defect 11190
	/**
	 * Proxy eDirectory userid authorization failure. This proxy userid is used
	 * to bind to eDir to retrieve cn for a txdot-v21alias.
	 */
	public final static int ERR_NUM_EDIR_PROXY_USER_AUTH_FAILURE = 2348;
	/**
	 * Agent failed authentication LDAP lookup for unknown reason.
	 */
	public final static int ERR_NUM_WEBAGNT_UNKNOWN_AUTH_LDAP_ERR = 2349;
	/**
	 * eDirectory password is temporary.
	 */
	public final static int ERR_NUM_WEBAGNT_EDIR_PWD_TEMPORARY = 2350;
	// end defect 11190
	
	// defect 11239
	/**
	 * eDirectory password is temporary.
	 */
	public final static int ERR_NUM_WEBAGNT_NO_PLATE_FOUND_FOR_COUNTY = 2351;
	// end defect 11239
	// defect 10729  -- Web Agent validation messages
	/**
	 * Field must be numeric
	 */
	public final static int MUST_BE_NUMERIC = 5001;	
	/**
	 * User name and password must be entered for login
	 */
	public final static int USERNAME_AND_PASSWORD_MUST_BE_ENTERED = 5002;
	/**
	 * User name must be entered for login
	 */
	public final static int USERNAME_MUST_BE_ENTERED = 5003;	
	
	/**
	 * Password must be entered for login
	 */
	public final static int PASSWORD_MUST_BE_ENTERED = 5004;
	
	/**
	 * No agency was found for the search
	 */
	public final static int NO_AGENCY_IS_FOUND_FOR_THE_USER_ENTERED = 5005;
	/**
	 * Please accept the disclaimer
	 */
	public final static int PLEASE_CHECK_I_ACCEPT_OR_PRESS_THE_CANCEL_BUTTON = 5006;
	/**
	 * Please accept the license plaste number
	 */
	public final static int PLEASE_ENTER_THE_LICENSE_PLATE_NUMBER = 5007;
	/**
	 * Please accept the last 4 characters of the VIN
	 */
	public final static int PLEASE_ENTER_THE_LAST_FOUR_CHARACTERS_OF_THE_VIN = 5008;
	/**
	 * Please scan the renewal notice barcode
	 */
	public final static int PLEASE_SCAN_THE_RENEWAL_NOTICE_BARCODE = 5009;
	/**
	 * Not enough data for scan
	 */
	public final static int NOT_ENOUGH_DATA_WAS_SCANNED_TO_PERFORM_A_SEARCH = 5010;
	/**
	 * The input method used is not a valid method.
	 */
	public final static int INPUT_METHOD_USED_IS_NOT_VALID = 5011;
	/**
	 * This renewal has already been processed.
	 */
	public final static int RENEWAL_HAS_ALREADY_BEEN_PROCESSED = 5012;
	/**
	 * Please select the response for the citatin issued
	 */
	public final static int SELECT_RESPONSE_FOR_CITATION_ISSUED_QUESTION = 5013;
	/**
	 * Expired license citation was issued so renewal may not be processed.
	 */
	public final static int EXPIRED_LICENSE_CITATION = 5014;
	/**
	 * Please select the appropriate response for valid insurance.
	 */
	public final static int SELECT_RESPONSE_FOR_VALID_INSURANCE = 5015;
	/**
	 * Cannot process renewal without proof of insurance.
	 */
	public final static int PROOF_OF_INSURANCE_ERR = 5016;
	/**
	 * Please select an agency location and press continue.
	 */
	public final static int SELECT_AGENCY_LOCATION = 5017;
	/**
	 * User name or first and last name must be entered for search.
	 */
	public final static int USERNAME_OR_FIRST_AND_LAST_NAME_MUST_BE_NTERED = 5018;
	/**
	 * Agency id, name, or zip code must be entered to search.
	 */
	public final static int AGENCYID_NAME_OR_ZIP_MUST_BE_ENTERED = 5019;
	/**
	 * No user was found for the criteria entered.
	 */
	public final static int NO_USER_WAS_FOUND = 5020;	
	/**
	 * First name and last name must be entered
	 */
	public final static int FIRST_NAME_AND_LAST_NAME_MUST_BE_ENTERED = 5021;
	/**
	 * Username must be different thant he logged in user name
	 */
	public final static int USER_NAME_MUST_BE_DIFFERENT_THAN_THE_LOGGED_IN_USER_NAME = 5022;
	/**
	 * Maxium submit count must be less than or equal to 1000
	 */
	public final static int MAXIMUM_SUBMIT_COUNT_MUST_BE_LESS_THAN_OR_EQUAL_TO_1000 = 5023;
	/**
	 * Agent is already assigned to this location
	 */ 
	public final static int AGENT_IS_ALREADY_ASSIGNED_TO_THIS_LOCATION = 5024;
	/**
	 * Location is already assigned 
	 */ 
	public final static int LOCATION_IS_ALREADY_ASSIGNED = 5025;
	/**
	 * No location was found for the criteria entered
	 */ 
	public final static int NO_LOCATION_WAS_FOUND_FOR_THE_CRITERIA_ENTERED = 5026;
	/**
	 * Maximum submit days must be from 1 to 14
	 */ 
	public final static int MAXIMUM_SUBMIT_DAYS_MUST_BE_FROM_1_TO_7 = 5027;
	/**
	 * Phone number must be 10 numeric digits
	 */ 
	public final static int PHONE_NUMBER_MUST_BE_10_NUMERIC_DIGITS = 5028;
	/**
	 * At least one action for user or admistrator must be selected
	 */ 
	public final static int AT_LEAST_ONE_ACTION_FOR_USER_OR_ADMINISTRATOR_MUST_BE_SELECTED = 5029;
	/**
	 * There was a problem with the {0} as entered, please re-enter the amount
	 */ 
	public final static int THERE_WAS_A_PROBLEM_WITH_THE_FIELD_AS_ENTERED = 5030;
	/**
	 * The {0} must be less than $100,000; pleae re-enter the amount
	 */ 
	public final static int FIELD_MUST_BE_LESS_THAN_ONE_HNDRED_THOUSAND_DOLLARS = 5031; 
	/**
	 * This sytem's last connection was to loacation {0}
	 */ 
	public final static int THIS_SYSTEMS_LAST_CONNECTION_WAS_TO_LOCATION_X = 5032; 
	/**
	 * If user name is entered, then first and last name must be blank
	 */ 
	public final static int IF_USER_NAME_IS_ENTERED_THEN_FIRST_AND_LAST_NAME_MUST_BE_BLANK = 5033; 
	/**
	 * If location id is entered, then location name and zip code must be blank
	 */ 
	public final static int  IF_LOCATION_ID_IS_ENTERED_THEN_LOCATION_NAME_AND_ZIP_CODE_MUST_BE_BLANK = 5034; 
	
}
