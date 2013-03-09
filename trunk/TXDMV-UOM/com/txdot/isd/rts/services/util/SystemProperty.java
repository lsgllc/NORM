package com.txdot.isd.rts.services.util;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * SystemProperty.java
 *
 * (c) Texas Department of Motor Vehicles 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/22/2002	Changed password expiration to retrieve from
 *							SystemProperty CQU100004085
 * MAbs			06/04/2002	Installed wait time for printer CQU100004200 
 * RHicks		07/23/2002	Added property to support help desk
 * Ray Rowehl	07/27/2002	Added RebootTime property to decide when to 
 *							reboot.
 *							CQU100004425
 * Ray Rowehl	08/19/2002	Add Properties for defining purge days.
 *							CQU100004619. This also effects CQU100004601.
 * K Harrell	08/27/2002	CQU100004596.  Add parameter for Subcon 
 *							Transaction Wait
 *				08/29/2002	Added "static" to setSubconTransWaitTime 
 *							(for consistency: Not used)
 * S Govindappa	10/10/2002	Fixing defect# 4858. Removed the parameter 
 *							SubconTransWaitTime. Added a new parameter 
 *							CommServerBatchDelta with 45 as default 
 *							value. This parameter represents the number 
 *							of minutes prior to batch schedule that a 
 *							Comm Server and a non Data Server should 
 *							refresh/reboot
 * Ray Rowehl	10/17/2002	Report where an integer property is
 *							defaulted. also trim all integers.
 *							defect CQU100004876
 * RHicks		12/03/2002	Return only LogFileName (not dir + name) 
 *							from getter
 * Ray Rowehl	01/4/2003	Do not report the set error on 
 *							CommServerBatchDelta to the log.
 *							defect CQU100005203
 * S Govindappa	02/10/2003	Fixing defect # 5235. Added timeouts 
 *							parameters ConnectTimeOutServUp and 
 *							ConnectTimeOutServDown for checking server 
 *							connectivity before sending any information 
 *							to server.
 * Ray Rowehl	02/20/2003	Setup properties to allow redirect of 
 *							printing.
 *							defect CQU10005252
 * K Harrell	04/07/2003 	Defect CQU10005928 - Incorrect assignment of 
 *							PurgeInternet.
 * Min Wang		04/22/2003 	Make sure RTS Directory is created before
 *							using other directories. 
 *							Added setRTSDirectory(), getRTSDirectory().
 *							Modified initialize(), getRTSAppDirectory(),
 *							getReportDirectory, setHelpDir, 
 *							setReceiptsDirectory, setReportsDirectory,  
 *							setRTSApplDirectory, setSetAsideDirectory, 
 *							and rtscls.cfg. 
 *							defect 5920
 * K Harrell.	07/20/2003 	Defect 6272 Add variables PurgeInetSummary 
 *							& PurgeInetRenewal and associated 
 *							getters/setters. Remove variable 
 *							PurgeInternet and associated getter/setter
 *							modified initialize to include new variable
 *							names, remove PurgeInternet
 *							alter default for purgeInetAddr to 30 days
 * Jeff S.		08/28/2003	Added DefaultBrowserPath to the Static cfg 
 *							file for opening the default browser for 
 *							Help.
 *							getDefaultBrowserPath(), 
 *							setDefaultBrowserPath(),
 *							initialize() - Defect# 5972 - ver5.1.6
 * Jeff S.		09/25/2003	Moved DefaultBrowserPath to the Dynamic CFG 
 *							file. Made path simpler.
 *							initialize(),setDefaultBrowserPath()
 *							Defect# 5972 - ver5.1.6
 * Jeff R.		12/05/2004	Read DTA Charge $1:00 Insurance Fee start 
 *							date. If Insurance Date is not in RTSCLS.CFG,
 *							use default 20040111 - 
 *							Defect 6676, Version 5.1.5 fix 1A
 *							method initialize()
 * K Harrell	12/22/2004	Read CacheVersionNo from Version.cfg
 *							added class variable CacheVersionNo
 *							added getCacheVersionNo(), 
 *							setCacheVersionNo()
 *							modified initialize()
 *							Defect 6555 Version 5.1.5 Fix 2
 * Ray Rowehl	10/21/2003	Add a parameter to set the AD ou for 
 *							development and testing.
 *							This will be null under Production..
 *							modify initialize()
 *							add ssAdOu, getAdOu(), setAdOu()
 *							defect 6445  ver 5.1.6
 *							defect 6445 Ver 5.1.6 
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * Jeff S.		02/23/2004	Values in methods below are not need anymore
 *							b/c MFReports are printed through the Print
 *							class using LPR instead of FTP.
 *							Both MFReportsPrintServer & MFReportsPrinter
 *							need to be removed when the depecated
 *							methods are removed. Added value to the 
 *							config for PrintStatus not handled through 
 *							production status.
 *							modify initialize()
 *							deprecate getMFReportsPrintServer()
 *							deprecate getMFReportsPrinter()
 *							defect 6648, 6698 Ver 5.1.6
 * Jeff S.		03/01/2004	Added PrintStatus to handle printing instead
 *							of using prodStatus.
 *							add getPrintStatus(), setPrintStatus()
 *							modify initialize()
 *							defect 6771 Ver 5.1.6
 * Jeff S.		03/12/2004	Changed the defaultBrowserPath's default
 *							from d: to c: b/c XP image has browser
 *							installed on the c drive.
 *							modify setDefaultBrowserPath(String)
 *							defect 6931 Ver 5.1.6
 * K Harrell	04/27/2004	Add variables for cumRcptLogFileName and
 *							voidTransLogFileName as well as associated
 *							get/set methods.
 *							modify initialize()
 *							defect 7019  Ver 5.2.0
 * K Harrell	07/18/2004	Add variable for PurgeReprntStkr
 *							add getPurgeReprntStkr(),
 *							setPurgeReprntStkr(),
 *							modify intialize()
 *							defect 7340  Ver 5.2.1
 * Ray Rowehl	07/29/2004	Add Properties for processing RSPS Jump
 *							Drive.
 *							add csCountyRSPSDirectory, 
 *								ssFlashDriveSourceDirectory, 
 *								csRSPSLogDir, 
 *								ssRSPSUpdateSuffix, ssRSPSLogSuffix
 *								getCountyRSPSDirectory(), 
 *								getFlashDriveSourceDirectory(),
 *								getRSPSLogDir(), getRSPSLogSuffix(),
 *								getRSPSUpdateSuffix(), 
 *								setCountyRSPSDirectory(),
 *								setFlashDriveSourceDirectory(), 
 *								setRSPSLogDir(),
 *								setRSPSLogSuffix(), 
 *								setRSPSUpdateSuffix()
 *							modify initialize()							
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/03/2004	put the create of the rsps logs dir in the
 *							getter to work properly.
 *							modify getRSPSLogDir(), setRSPSLogDir()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	06/20/2005	Cleanup before doing code changes.
 * 							Moved Constants up to class level.
 * 							Changed writes that happen before log
 * 							file name is established to do system.out
 * 							instead on server side.
 * 							Delete Deprecated and unused methods.
 * 							delete ssMFReportsPrintServer, 
 * 								ssMFReportsPrinter, ssPasswordExpire, 
 * 								ssRebootTime, ssShutDownTime
 * 							delete getMFReportsPrinter(), 
 * 								getMFReportsPrintServer(), 
 * 								getPasswordExpire(), getRebootTime(),
 * 								getShutDownTime(), 
 * 								setMFReportsPrinter(), 
 * 								setMFReportsPrintServer(),
 * 								setPasswordExpire(), setShutDownTime()
 * 							modify initialize()
 * 							defect 8248 Ver 5.2.3
 * Ray Rowehl	06/21/2005	Add indicator to determine if communications
 * 							are secured and by what methodology.
 * 							add ssCommunicationsMode,
 * 								HTTP_MODE, HTTPS_JKS_MODE
 * 							add getCommunicationsMode(),
 * 								setCommunicationsMode()
 * 							modify initialize()
 * 							defect 8248 Ver 5.2.3
 * Jeff S.		06/30/2005	Added properties used to stager the reboots
 * 							add DEFAULTREBOOTSTAGGERTIME, 
 * 								DEFAULTSTAGGERGROUPNUMBER, 
 * 								DEFAULT_STAGGER_TIME_MSG, 
 * 								DEFAULT_REBOOT_NUM_MSG,
 * 								getRebootNumber, getRebootStaggerTime,
 * 								setRebootNumber, setRebootStaggerTime
 * 							modify initialize()
 * 							defect 8259 Ver 5.2.3
 * Min Wang		07/11/2005	Added property for JavaVersion with getters
 * 							and setter.
 * 							add ssJavaVersion
 * 							add getJavaVersion(), isJavaVersionCorrect(),
 * 								setJavaVersion()
 * 							modify initialize()
 * 							defect 8242 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add constants to be used to determine if we 
 * 							are in production mode or not.
 * 							add APP_DEV_STATUS, APP_PROD_STATUS
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		09/08/2005	Added list of files to be pruned to the cls.
 * 							add svFilesToPrune, getFilesToPrune(), 
 * 								setFilesToPrune()
 * 							modify initialize()
 * 							defect 8367 Ver 5.2.3
 * Jeff S.		09/26/2005	Added list of file suffix that are valid to
 * 							be moved from the flash drive logs dir to 
 * 							the d:\rts\rsps\logs dir before update.
 * 							add svRSPSValidSuffix, setRSPSValidSuffix(), 
 * 								isRSPSValidSuffix()
 * 							modify initialize()
 * 							deprecate setRSPSLogSuffix(), 
 * 								getRSPSLogSuffix()
 * 							defect 8322 Ver 5.2.3
 * Jeff S.		12/05/2005	Forgot to add initialize to getter.  This is
 * 							needed in order to run Prune stand alone.
 * 							modify getFilesToPrune()
 * 							defect 8367 Ver 5.2.3
 * Ray Rowehl	12/06/2005	Add a parameter to server.cfg to allow for
 * 							setting the context provider URL.  
 * 							This is used in batch mode to help with 
 * 							the lookup of the datasource.
 * 							The property name in Server.cfg is 
 * 								"ContextProviderURL".
 * 							add ssContextProviderURL
 * 							add getContextProviderURL(), 
 * 								setContextProviderURL()
 * 							modify initialize()
 * 							defect 8461 Ver 5.2.3
 * Ray Rowehl	02/15/2006	Change the lookup of the cfg files to use
 * 							getResourceAsStream() instead of using a 
 * 							straight file system.
 * 							This change is mainly to support SendTrans.
 * 							modify initialize()
 * 							defect 8553 Ver 5.2.3
 * Ray Rowehl	02/17/2006	Found that when the application is launched
 * 							as a service (Production style) that 
 * 							the new look ups did not work.
 * 							reverting back to the old style load for
 * 							client side.			
 * 							modify initialize()
 * 							defect 8553 Ver 5.2.3
 * Ray Rowehl	04/25/2006	Remove the changes introduced on 12/15/2005
 * 							to add the "ContextProviderURL".
 * 							remove ssContextProviderURL
 * 							remove getContextProviderURL(), 
 * 								setContextProviderURL()
 * 							modify initialize()
 * 							defect 8461 Ver 5.2.3
 * Ray Rowehl	06/14/2006	Do a System.out.println when setting the 
 * 							default values for purge.
 * 							modify setPurgeAdminLog(), 
 * 								setPurgeAdminTables(),
 * 								setPurgeInetAddrChng(),
 * 								setPurgeInetRenewal(),
 * 								setPurgeInetSummary(), setPurgeInvHist(),
 * 								setPurgeReprntStkr(), setPurgeSecLog(),
 * 								setPurgeTrans()
 * 							defect 8823 Ver 5.2.3
 * Ray Rowehl	06/15/2006	Finding that all setters need to write their
 * 							messages to System.out.
 * 							modify setCacheVersionNo(), 
 * 								setDtaInsStartDate(),
 * 								setMainFrameVersion(), setMFDebug(),
 * 								setMFJGate(), setMFJGatewayPort(),
 * 								setOfficeIssuanceNo(), setPrintStatus(),
 * 								setProdStatus(), setRebootNumber(), 
 * 								setRebootStaggerTime(), 
 * 								setRemoteListenerPort(),
 * 								setSubStationId(), setWorkStationId()
 * 							defect 8823 Ver 5.2.3
 * Ray Rowehl	09/11/2006	Add processing for DBUserBatch and 
 * 							DBPasswordBatch.  These properties will
 * 							allow server side batch to occur while
 * 							we are in restricted database use mode.
 * 							This class is in need of a review of 
 * 							constants definition.  Should be done later.
 * 							Did some minor old code cleanup.
 * 							Format source.
 * 							add ssDBUserBatch, ssDBPasswordBatch
 * 							add getDBPasswordBatch(), getDBUserBatch(),
 * 								setDBPasswordBatch(), setDBUserBatch()
 * 							modify initialize()
 * 							defect 8923 Ver 5.2.5
 * Bob Brown	09/12/2006	Add processing for Host, Channel, 
 * 							QueueManager, and Port.  These properties 
 * 							will allow server side MQ process to occur 
 *							in WAS5 application client mode. 
 * 							add ssMQHost, ssMQChannel, ssMQQueueManager, 
 * 							ssQueueName, ssMQBackupQueueName, ssMQPort,
 * 							add getMQHostName(), getMQChannelName(), 
 * 							getMQQueueManager(), getMQQueueName(),
 * 							getMQBackupQueueName(), getMQPort()
 * 							add setMQHostName(), setMQChannelName(), 
 * 							setMQQueueManager(), setMQQueueName(), 
 * 							setMQBackupQueueName(), setMQPort()
 * 							modify initialize()
 * 							defect 8914 Ver 5.2.5
 * Ray Rowehl	09/13/2006	Re-merge.
 * 							defect 8923 Ver 5.2.5
 * Bob Brown	09/14/2006	Re-merge.
 * 							defect 8914 Ver 5.2.5
 * Jeff S.		09/15/2006	Added two new properties to the server.cfg.
 * 							Used to lookup presumptive value.
 * 							add ssBlackBookCustPrefix, ssBlackBookURL,
 * 								DEFAULT_BLACKBOOK_CUST_PREFIX
 * 							add getBlackBookCustPrefix(), 
 * 								getBlackBookURL(), setBlackBookURL(),
 * 								setBlackBookCustPrefix()
 * 							modify initialize()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	09/25/2006	Add processing for StaticTablePilot. This 
 * 							property will be used to point the server
 * 							at "Pilot" versions of the static tables.
 * 							Add processing for PurgeExmptAudit, the
 * 							number of days to keep RTS_EXMPT_AUDIT data
 * 							add DEFAULTPURGEEXMPTAUDIT, 
 * 								DEFAULT_PURGE_EXMPT_AUDIT_MSG,
 * 								USING_STATIC_PILOT_TABLE_MSG
 * 							add sbStaticTablePilot, siPurgeExmptAudit
 * 							add isStaticTablePilot(),getPurgeExmptAudit(),
 * 							  setStaticTablePilot(String),
 * 							  setStaticTablePilot(boolean),
 * 							  setPurgeExmptAudit()
 * 							modify initialize() 
 * 							defect 8900 Ver 5.3.0
 * Ray Rowehl	09/28/2006	Add properties for SendTrans DB UserId and
 * 							password.
 * 							add ssDBPasswordSendTrans, ssDBUserSendTrans
 * 							add getDBPasswordSendTrans(), 
 * 								getDBUserSendTrans(), 
 * 								setDBPasswordSendTrans(),
 * 								setDBUserSendTrans()
 * 							modify initialize()
 * 							defect 8933 Ver FallAdminTables
 * Ray Rowehl	10/11/2006	Found that the initialize() was not setup 
 * 							correctly for SendTrans db user and password.
 * 							modify initialize()
 * 							defect 8933 Ver FallAdminTables
 * Ray Rowehl	10/16/2006	Allow MfAccessTest to set the MFTransVersion.
 * 							add setMFTransVersion()
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	10/17/2006	Rename MFTransVersion to be 
 * 							MFInterfaceVersionCode.
 * 							delete MFInterfaceVersionCode
 * 							add getMFInterfaceVersionCode(), 
 * 								setMFInterfaceVersionCode()
 * 							delete getMFTransVersion(), 
 * 								getMFTransVersion()
 * 							modify initialize()
 * 							defect 6701 Ver Exempts
 * J Ralph		10/18/2006  Add two new properties for Exempts export
 * 							add ssExportsDirectory							
 * 							add getExportsDirectory(), setExportsDiretory()
 * 							modify initialize()
 * 							defect 8900 Ver Exempts
 * J Ralph		10/18/2006	Merge prolong adjustment
 * 							defect 8900 Ver Exempts   
 * Ray Rowehl	11/07/2006	Initialize the CommunicationsMode for HTTPS
 * 							as needed.  This is so we get the HTTPS mode.
 * 							add isHTTPMode()
 * 							modify initialize()
 * 							defect 8248 Ver Exempts
 * Ray Rowehl	11/07/2006	Revamp the settings for HTTP Mode to reflect
 * 							going into HTTPS Mode.
 * 							Remove some unused fields.
 * 							modify HTTPS_JKS_MODE
 * 							modify isHTTPMode()
 * 							defect 8248 Ver Exempts
 * Jeff S.		04/02/2007	Add AbstractProperty for mail directory and gateway.
 * 							add DEFAULT_MAIL_DIR, ssMailDir, 
 * 								DEFAULT_MAIL_GATEWAY, ssMailGateway,
 * 								setMailDir(), getMailDir(), 
 * 								setMailGateway(), getMailGateway()
 * 							modify initialize()
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	02/06/2007	Add new property values for Special Plates
 * 							add siPurgeSpclPltTransHstry, 
 * 							  siPurgeSpclPltRejLog,   
 * 							  DEFAULT_PURGE_SPCL_PLT_TRANS_HSTRY_MSG,
 * 							  DEFAULTPURGESPCLPLTTRANSHSTRY,
 * 							  DEFAULT_PURGE_SPCL_PLT_REJ_LOG_MSG,
 * 							  DEFAULTPURGESPCLPLTREJLOG, 
 * 							  getPurgeSpclPltTransHstry(),
 * 							  setPurgeSpclPltTransHstry(),
 * 							  getPurgeSpclPltRejLog(),
 * 							  setPurgeSpclPltRejLog()
 * 							deleted constants no longer in use 
 * 							modify initialize()
 * 							defect 9085  Ver Special Plates
 * K Harrell	03/09/2007	add isCounty(),isRegion(),isHQ(),
 *       					 get/setOfficeIssuanceCd()
 * 							add HQ_OFFICE_CD, REGION_OFFICE_CD,
 * 							 COUNTY_OFFICE_CD
 * 							defect 9085 Ver Special Plates 
 * Ray Rowehl	04/19/2007	Allow a second lookup for server.cfg if 
 * 							it can be loaded as a resource.  This is 
 * 							required for the new deployment mechanism.
 * 							modify initialize()
 * 							defect 9116 Ver Special Plates
 * Jeff S.		06/26/2007	Added two new properties to the server.cfg.
 * 							Used to lookup presumptive value.
 * 							add siPurgeInetSPAPPIncomp, 
 * 								siPurgeInetSPAPPComp,
 * 								DEFAULTPURGEINETSPAPPINCOMP,
 * 								DEFAULTPURGEINETSPAPPCOMP,
 * 								DEFAULT_PURGE_INET_SPAPP_INCOMP_MSG,
 * 								DEFAULT_PURGE_INET_SPAPP_COMP_MSG
 * 							add getPurgeInetSPAPPIncomp(), 
 * 								getPurgeInetSPAPPComp(), 
 * 								setPurgeInetSPAPPIncomp(),
 * 								setPurgeInetSPAPPComp()
 * 							modify initialize()
 * 							defect 9121 Ver Special Plates
 * K Harrell	10/12/2007	Add processing for PTO Start Date 
 * 							add DEFAULT_PTO_START_DATE, 
 * 								DEFAULT_PTO_START_DATE_MSG
 * 							add saPTOStartDate 
 * 							add getPTOStartDate(), setPTOStartDate()
 * 							modify initialize()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	10/22/2007	Add start date for Buyer Tab 
 * 							add DEFAULT_BUYER_TAG_START_DATE, 
 *  						DEFAULT_BUYER_TAG_START_DATE_MSG,
 * 							add saBuyerTagStartDate 
 * 							add getBuyerTagStartDate, setBuyerTagStartDate()
 * 							modify initialize()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	10/26/2007	Modified DEFAULT_BUYER_TAG_START_DATE
 * 							per note from Suzanne/Conrad
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	11/13/2007	DTAInsuranceDate no longer required. 
 * 							delete saDTAInsStartDate, 
 * 							 DEFAULT_DTA_INS_DATE, 
 * 							 DEFAULT_DTA_INS_DATE_MSG
 * 							delete getDTAInsStartDate(), 
 * 							 setDTAInsStartDate() 
 * 							modify initialize() 
 * 							defect 9440 Ver Special Plates 2  
 * 							defect 9368 Ver Special Plates 2 
 * Ray Rowehl	11/27/2007	Add Log4j initialization
 * 							add RTS_LOG4J_PROPERTY_FILE_NAME,
 * 								saLog4jSystemPropLog,
 * 								sbLog4jWatch,
 * 								siLog4jWatchTime
 * 							add initLog4jServerSide(), 
 * 								isLog4jWatch(),
 * 								setLog4jWatch(boolean),
 * 								setLog4jWatch(String)
 * 							modify initialize()
 * 							defect 9441 Ver Special Plates 2 
 * Ray Rowehl	12/11/2007	Add NullPtr guard when loading log4j 
 * 							properties.
 * 							modify initLog4jServerSide()
 * 							defect 9441 Ver Special Plates 2
 * Ray Rowehl	12/14/2007	The log4j file name has improper case in it.
 * 							modify RTS_LOG4J_PROPERTY_FILE_NAME
 * 							Defect 9441 Ver Special Plates 2
 * Ray Rowehl	04/15/2008	Add a server side property for storing the 
 * 							URL for the Texas Sure web service.
 * 							add ssTexasSureURL
 * 							add getTexasSureURL(), setTexasSureURL()
 * 							modify initialize()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	04/22/2008	Add property for IVTRS Patterned Plate hold 
 * 							release days offset.
 * 							add DEFAULT_PURGE_VI_IVTRS_MSG,
 * 								siPurgeViIvtrsReleaseDays
 * 							add getPurgeViIvtrsReleaseDays(),
 * 								setViPurgeIvtrsReleaseDays(int),
 * 								setViPurgeIvtrsReleaseDays(String)
 *							modify initialize()
 * 							defect 9606 Ver FRVP
 * Ray Rowehl	04/23/2008	Add a switch that controls whether or not
 * 							we call the TexasSure server to verify 
 * 							insurance.
 * 							The default is false.
 * 							add sbCheckInsurance
 * 							add isCheckInsurance(), 
 * 								setCheckInsurance(boolean),
 * 								setCheckInsurance(String)
 *							modify initialize()
 * 							defect 9606 Ver FRVP
 * Ray Rowehl	06/24/2008	Add parameters for Vendor Plates Office
 * 							and Workstation.
 * 							Also sort members.
 * 							add DEFAULT_VP_WSID, DEFAULT_VP_OFFICE, 
 * 								siVpOfcIssuanceNo, siVpWsId
 * 							add getVpOfcIssuanceNo(), getVpWsId(),
 * 								setVpOfcIssuanceNo(), setVpWsId()
 * 							modify initialize()
 * 							defect 9675 Ver MyPlates_POS
 * K Harrell	06/23/2008	Add boolean to determine if show delinquent
 * 							days in Title.
 * 							add sbShowDelqntDays
 * 							add isShowDelqntDays(),
 * 							 setShowDelqntDays(boolean), 
 * 							 setShowDelqntDays(String) 
 * 							modify initialize() 
 * 							defect 9724 Ver Defect POS A 
 * K Harrell	08/26/2008	Add parameter for frequency for reporting
 * 							SendTrans transactions 
 * 							add siSendTransRptFreq, 
 * 							 DEFAULTSENDTRANSRPTFREQ
 * 							add getSendTransRptFreq(),
 * 							 setSendTransRptFreq(int),
 *							 setSendTransRptFreq(String)
 *							modify initialize() 
 * 							defect 6440 Ver Defect POS B
 * K Harrell	10/27/2008	Add parameter for MVDIExportFileName
 * 							add ssMVDIExportFileName, get/set methods,
 * 							 DEFAULT_MVDI_EXPORT_FTP_FILENAME
 * 							modify initialize()
 * 							defect 9831 Ver Defect POS B 
 * J Rue		09/11/2008	Add SendTransFileLoc
 * 							add ssSendTransFileLoc
 * 							add getSendTransFileLoc(),
 * 								setSendTransFileLoc()
 * 							modify intialize()
 * 							defect 8984 Ver ELT_MfAccess
 * K Harrell	11/02/2008	Add add'l parameters for MVDIExport 
 * 							add DEFAULT_MVDI_EXPORT_FTP_SERVER_NAME,
 * 							  DEFAULT_MVDI_EXPORT_FTP_USERID,
 *  						  DEFAULT_MVDI_EXPORT_FTP_PASSWORD,
 * 							  DEFAULT_MVDI_EXPORT_FTP_DIRECTORY
 * 							add ssMVDIExportFTPServerName,
 * 							  ssMVDIExportFTPUserId,
 *     						  ssMVDIExportFTPPassword,
 *  						  ssMVDIExportFTPDirectory, get/set methods
 * 							modify initialize()
 *							defect 9831 Ver Defect_POS_B
 * K Harrell	11/08/2008	Add parameter for max number of returned 
 * 							records on Disabled Placard Search.
 * 							add DEFAULT_DSABLD_PLCRD_MAX_RCDS  
 * 							add siDsabldPlcrdMaxRcds, get/set methods 
 * 							modify initialize() 
 * 							defect 9831 Ver Defect_POS_B 
 * Ray Rowehl	12/17/2008	Correcting merge issues!
 * 							add setOfficeIssuanceNo(int)
 * 							change getMVDIExportFTPDirectory()()
 * 							defect MERGE Ver Defect_POS_C
 * J Rue		12/19/2008	Add BuildSendTransReviseDate
 * 							add ssBuildSendTransReviseDate
 * 							add getBuildSendTransReviseDate(),
 * 								setBuildSendTransReviseDate()
 * 							modify initialize()
 * 							defect 8984 Ver ELT_MfAccess
 * K Harrell	01/21/2009	Add parameters, methods for DP Cust purge
 * 							Added "purge" where missing 
 * 							Ordered purge calls by alpha order
 * 							 add DEFAULT_PURGE_SET_DELINDI_DSABLD_PLCRD_CUST_MSG
 * 							  DEFAULT_PURGE_DSABLD_PLCRD_CUST_MSG
 * 							 add siPurgeSetDelIndiDsabldPlcrdCust,
 * 							  siPurgeDsabldPlcrdCust
 * 							 add getPurgeDsabldPlcrdCust(), 
 * 							  getPurgeSetDelIndiDsabldPlcrdCust(),
 * 							  setPurgeDsabldPlcrdCust(), 
 * 							  setPurgeSetDelIndiDsabldPlcrdCust()
 * 							 modify initialize()
 * 							defect 9889 Ver Defect_POS_D 
 * K Harrell	01/21/2009	Add parameters for Web Services purge
 * 							 add DEFAULT_PURGE_WEB_SERVICES_MSG,
 * 								DEFAULTPURGEWEBSERVICES 
 * 							 add siPurgeWebServices
 * 							 add getPurgeWebServices(), 
 * 							   setPurgeWebServices() 
 * 							 modify initialize()
 * 							defect 9803 Ver Defect_POS_D 
 * J Rue		03/03/2009	Add FundsUpdateFileLoc and 
 * 							FundsUpdateReviseDate
 * 							add ssFundsUpdateFileLoc, 
 * 								FundsUpdateReviseDate
 * 							add getFundsUpdateFileLoc(),
 * 								setFundsUpdateFileLoc(),
 * 								getFundsUpdateReviseDate(),
 * 								setFundsUpdateReviseDate()
 * 							modify initialize()
 * 							defect 8984 Ver Defect_POS_D 
 * B Hargrove   06/10/2009 	Add Flashdrive option to DTA and Subcon.
 * 							add ssExternalMedia,
 * 							getExternalMedia(), setExternalMedia()
 * 							defect 10075 Ver Defect_POS_F
 * K Harrell	06/09/2009	Add parameters, methods for Internet 
 * 							Deposit Recon, Internet Deposit Recon Hstry
 * 							add DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_MSG,
 * 							DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_HSTRY_MSG,
 * 							DEFAULTPURGEINETDEPOSITRECON,
 * 							DEFAULTPURGEINETDEPOSITRECONHSTRY,
 * 							siPurgeInetDepositRecon,
 * 							siPurgeInetDepositReconHstry
 * 							add getPurgeInetDepositRecon(),
 * 							 getPurgeInetDepositReconHstry(), 
 * 							 setPurgeInetDepositRecon(),
 * 							 setPurgeInetDepositReconHstry()
 * 							modify initialize() 
 * 							defect 9955 Ver Defect_POS_F
 * K Harrell	06/15/2009	add isDevStatus() 
 * 							defect 10086 Ver Defect_POS_F 
 * B Hargrove   08/07/2009 	Add start date for DMV agency. 
 * 							add DEFAULT_DMV_START_DATE, 
 *  						DEFAULT_DMV_START_DATE_MSG,
 * 							saDMVStartDate, getDMVStartDate(), 
 * 							setDMVStartDate()
 * 							defect 10155 Ver Defect_POS_F
 * K Harrell	08/17/2009	add sbClientServer
 * 							add isClientServer(), setClientServer()
 * 							delete sbWorkstation, setWorkstation()  
 * 							defect 8628 Ver Defect_POS_F 
 * B Hargrove   09/02/2009 	Reset default DMV date to 20091101. 
 * 							defect 10155 Ver Defect_POS_F
 * K Harrell	03/10/2010	Implement Purge for ETitle History.  Modify 
 * 							default AdminLog Purge to 180 vs. 90.  
 * 							add DEFAULTPURGEETTLHSTRY, 
 * 							 DEFAULT_PURGE_ETTL_HSTRY_MSG, 
 * 							 siPurgeETtlHstry, get/set methods
 * 							modify initialize(), DEFAULTPURGEADMINLOG
 * 							defect 10231 Ver POS_640
 * K Harrell	03/24/2010	Add variables for managing DB2 performance 
 * 							add sbResetFundsDB2QueryOptimization, 
 * 							 siResetFundsDB2QueryOptimizationLevel, 
 * 							 get/set methods
 * 							add DEFAULT_RESET_FUNDS_DB2_QRY_OPT,
 * 							 DEFAULT_RESET_FUNDS_DB2_QRY_OPT_LVL
 * 							modify initialize()  
 * 							defect 10413 Ver POS_640    
 * K Harrell	04/03/2010	Implement Purge for Workstation Status Log
 * 							add DEFAULTPURGEWSSTATUSLOG, 
 * 							  DEFAULT_PURGE_WS_STATUS_LOG_MSG, 
 * 							  siPurgeWSStatusLog, get/set methods
 * 							modify initialize() 
 * 							defect 8087 Ver POS_640 
 * K Harrell	04/03/2010	Implement Purge for Itrnt_Trans_Del_Log
 * 							add DEFAULTPURGEITRNTTRANSDELLOG, 
 * 							  DEFAULT_PURGE_ITRNT_TRANS_DEL_LOG, 
 * 							  siPurgeItrntTransDelLog, get/set methods
 * 							modify initialize() 
 * 							defect 10421  Ver POS_640 
 * K Harrell	06/09/2010	add DEFAULTMFPERMITSTARTDATE, 
 * 							 DEFAULT_MFPERMIT_START_DATE_MSG, 
 * 							 saMFPrmtStartDate, get/set methods 
 * 							modify initialize()
 * 							defect 10491 Ver 6.5.0
 * B Brown		07/29/2010  add DEFAULT_EREMINDER_FROM_EMAIL 
 * 							ssEReminderFromEmail, get/set methods 
 * 							modify initialize()
 * 							defect 10512 Ver 6.5.0
 * K Harrell	07/31/2010	add DEFAULT_VSAM_LOG_ERR_MSG,
 * 							 DEFAULT_DB2_LOG_REQ_MSG,
 * 							 DEFAULT_PURGE_MF_REQ_HSTRY_MSG,
 * 							 DEFAULTVSAMLOGERR,
 * 							 DEFAULTDB2LOGMFREQ,
 * 							 DEFAULTPURGEMFREQHSTRY
 *							add siPurgeMFReqHstry, sbVSAMLogMFErr,
 *							 sbDB2LogMFReq, get/set methods
 * 							modify initialize()
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	08/03/2010	add DB2_LOGGING_NONE, 
 * 							   DB2_LOGGING_ERROR_ONLY,
 *							   DB2_LOGGING_ALL_MF_REQ, 
 *							   DEFAULTDB2LOGMFREQCD  
 * 							add siDB2LogMFReqCd, get/ set methods
 * 							delete sbDB2LogMFReq, get/set methods
 * 							delete DEFAULTDB2LOGMFREQ
 * 							modify initialize(), isDB2LogMFReq() 
 * 							defect 10462 Ver 6.5.0
 * K Harrell	09/14/2010	SystemProperty for MyPlates URL
 * 							add DEFAULT_MYPLATES_URL_MSG, 
 * 							  DEFAULT_MYPLATES_URL  
 * 							add ssMyPlatesURL, get/set methods 
 * 							modify initialize() 
 * 							defect 10575 Ver 6.6.0 
 * B Brown		10/08/2010  change DEFAULT_EREMINDER_FROM_EMAIL
 * 							to DEFAULT_EREMINDER_EMAIL 
 * 							rename get/set methods 
 * 							modify initialize()
 * 							defect 10614 Ver 6.5.0
 * K Harrell	12/27/2010	Implement Purge for Spcl_Plt_Prmt
 * 							add DEFAULTPURGESPCLPLTPRMT 
 * 							  DEFAULT_PURGE_SPCL_PLT_PRMT, 
 * 							  siPurgeSpclPltPrmt, get/set methods
 * 							modify initialize() 
 * 							defect 10700  Ver 6.7.0 
 * T Pederson   01/31/2011 	Add start date for mandatory Vehicle Color 
 * 							capture. 
 * 							add DEFAULT_VEH_COLOR_START_DATE, 
 *  						DEFAULT_VEH_COLOR_START_DATE_MSG,
 * 							saVehColorStartDate, getVehColorStartDate(), 
 * 							setVehColorStartDate()
 * 							modify initialize() 
 * 							defect 10689  Ver 6.7.0 
 * B Hargrove   02/07/2011 	Add start date for Fee Simplification. 
 * 							add DEFAULT_FEE_SIMPLIFY_START_DATE, 
 *  						DEFAULT_FEE_SIMPLIFY_START_DATE_MSG,
 * 							saFeeSimplifyStartDate, getFeeSimplifyStartDate(), 
 * 							setFeeSimplifyStartDate()
 * 							modify initialize() 
 * 							defect 10685  Ver 6.7.0 
 * M Reyes		02/16/2011	add DEFAULT_eDIR_SERVER
 * 							sseDirServer, get/set methods
 * 							modify initialize()
 * 							defect 10670 Ver 6.7.0
 * B Woodson 04/07/2011     defect 10797 - correct the WebAgent URL
 *                          ssWebPort, get & set methods
 *                          modify initialize to read WebPort from rtscls.cfg
 *                           and set ssWebPort	
 * 
 *                          ssWebProtocol, get & set methods
 *                          modify initialize to read WebProtocol from rtscls.cfg
 *                           and set ssWebProtocol
 * 
 * B Woodson 04/25/2011     defect 10797 - Change WebPort and WebProtocol 
 *                           to WebAgentPort and WebAgentProtocol
 * 
 * B Woodson 04/25/2011     defect 10797 - added WebAgentHost, getter, setter, 
 *                          modify initialize to read WebPort from rtscls.cfg
 *                          and set ssWebHost
 * R Pilon   	05/10/2011 	Add proxy user id and password for eDirectory. 
 *							add	DEFAULT_EDIR_PASSWORD, ssEDirPassword, 
 *							DEFAULT_EDIR_USER_ID, ssEDirUserId, 
 *							getEDirPassword(), setEDirPassword(), 
 *							getEDirUserId(), setEDirUserId()	
 *							modify initialize()				
 * 							defect 10670 Ver 671
 * R Pilon   	05/18/2011 	Add proxy user id, password and server for 
 * 							Active Directory. 
 *							add	DEFAULT_ACTV_DIR_PASSWORD, ssActiveDirPassword,
 *							 DEFAULT_ACTV_DIR_SERVER, ssActiveDirServer,
 *							 DEFAULT_ACTV_DIR_USER_ID, ssActiveDirUserId,
 *							 getActiveDirPassword(), setActiveDirPassword(),
 *							 getActiveDirServer(), setActiveDirServer(),
 *							 getActiveDirUserId(), setActiveDirUserId()
 *							modify initialize()				
 * 							defect 10670 Ver 671
 * K Harrell	06/15/2011	add siMaxFraudReportDays, get/set Method
 * 							add DEFAULT_MAX_FRAUD_REPORT_DAYS,
 * 							  DEFAULT_MAX_FRAUD_REPORT_DAYS_MSG 
 * 							modify initialize()
 * 							defect 10900 Ver 6.8.0  
 * B Hargrove   07/15/2011 	Add start date for Dealer Invalidate Registration.
 * 							Add start date for Do Not Default Title Trans Dt. 
 * 							add DEFAULT_DEALER_INVALIDATE_REG_START_DATE, 
 *  						DEFAULT_DEALER_INVALIDATE_REG_START_DATE_MSG,
 * 							DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE,
 * 							DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE_MSG,
 * 							saDlrInvalidRegStartDate, 
 * 							getDlrInvalidRegStartDate(), 
 * 							setDlrInvalidRegStartDate()
 * 							saNoDefaultTtlTransDtStartDate, 
 * 							getNoDefaultTtlTransDtStartDate(), 
 * 							setNoDefaultTtlTransDtStartDate()
 * 							modify initialize() 
 * 							defect 10949  Ver 6.8.0
 * Ray Rowehl	08/10/2011	Add parameter for TexasSure time out.
 * 							Re-flow initialize to simplify that method.
 * 							add DEFAULT_TEXAS_SURE_TIMEOUT,
 * 								siTexasSureTimeOut
 * 							add getTexasSureTimeOut(),
 * 								initializeClientSideDynamic(),
 * 								initializeClientSideStatic(), 
 * 								initializeServerCfg(),
 * 								initializeVersion(),
 * 								setTexasSureTimeOut(int),
 * 								setTexasSureTimeOut(String)
 * 							modify initialize()
 * 							defect 10119 Ver 6.8.1 
 * Buck Woodson	08/26/2011	add sbTexasSureHang
 *                              siTexasSureTestThreadCount 
 *                          add emulatingTexasSureHang() 
 *                              getTexasSureTestThreadCount()
 *                              setTexasSureHang(boolean)
 *                              setTexasSureHang(String)
 *                              setTexasSureTestThreadCount(int)
 *                              setTexasSureTestThreadCount(String)
 *                          modify initializeClientSideStatic()
 *                                 initializeServerCfg()
 *                          defect 10119 Ver 6.8.1
 * K Harrell	10/10/2011	add siMaxAddDsabldPlcrds, get/set Method
 * 							add DEFAULT_MAX_ADD_DSABLD_PLCRDS
 * 							  DEFAULT_MAX_ADD_DSABLD_PLCRDS_MSG 
 * 							modify initializeClientSideStatic()()
 * 							defect 11050 Ver 6.9.0                          
 * R Pilon   	10/19/2011 	Add user id, password, application name and service
 * 							endpoint for the soaprsps service 
 *							add	DEFAULT_SOAPRSPS_ENDPOINT, 
 *							 DEFAULT_SOAPRSPS_APPLICATION,
 *							 DEFAULT_SOAPRSPS_PASSWORD, DEFAULT_SOAPRSPS_USERID, 
 *							 ssSoapRspsEndpoint, ssSoapRspsApplication(), 
 *							 ssSoapRspsUserId(), ssSoapRspsPassword(), 
 *							 getSoapRspsEndpoint, getSoapRspsApplication(), 
 *							 getSoapRspsUserId(), getSoapRspsPassword(), 
 *							 setSoapRspsEndpoint(), setSoapRspsApplication(), 
 *							 setSoapRspsUserId(), setSoapRspsPassword()
 *							modify initialize()				
 * 							defect 11116 Ver 6.8.2
 * B Woodson    12/09/2011  add ssWebAgentPath, getWebAgentPath(), setWebAgentPath()
 *                          modify initializeClientSideStatic()
 *                          defect 11174 Ver 6.9.0
 * K Harrell	01/16/2012	add DEFAULT_DTA_REJECT_SURVIVOR_DATE 
 * 							add siDTARejectSurvivorDate, get/set method
 * 							modify initializeClientSideStatic() 
 * 							defect 10827 Ver 6.10.0 
 * R Pilon		03/26/2012	Add properties for GUI log file
 * 							add ssGUILogFileName, getGUILogFileName(), 
 * 							  setGUILogFileName()
 * 							modify initializeClientSideDynamic()
 * 							defect 11318 Ver 7.0.0
 * Min Wang		04/05/2012	Modify the Java Version string to use	
 * 							specification.  This makes the check
 * 							build independent.
 * 							modify JAVAVERSIONSTRING
 * 							defect 10129 Ver 7.0.0
 * R Pilon		04/11/2012	Changes for seperation of back end and gui 
 * 							  listening ports.
 * 							add siRemoteListenerPortBE, siRemoteListenerPortGUI,
 * 							  DEFAULTREMOTELISTENERPORTBE, DEFAULTREMOTELISTENERPORTGUI,
 * 							  getRemoteListenerPortBE(), getRemoteListenerPortGUI()
 * 							  setRemoteListenerPortBE(), setRemoteListenerPortGUI()
 * 							delete siRemoteListenerPort, DEFAULTREMOTELISTENERPORT,
 * 							  getRemoteListenerPort(), setRemoteListenerPort()
 * 							modify initializeClientSideStatic()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		03/26/2012	Add default properties for the GUI standard out
 * 							  and standard err log files.
 * 							add DEFAULT_GUI_LOG_FILENAME
 * 							modify initializeClientSideDynamic(), 
 * 							  setGUILogFileName()
 * 							defect 11318 Ver 7.0.0
 * R Pilon		07/05/2012	Add property used by RTSMainGUI for number of retry 
 * 							  attempts when attempting to connect to  
 * 							  RTSMainBE.
 * 							add siGUISendCacheCommRetryAttempts, 
 * 							  DEFAULT_GUI_SENDCACHE_COMM_RETRY_ATTEMPTS,
 * 							  getGUISendCacheCommRetryAttempts(),
 * 							  setGUISendCacheCommRetryAttempts(String)
 * 							modify initializeClientSideDynamic()
 * 							defect 11407 Ver 7.0.0
 * R Pilon		08/08/2012	Change default number of retry attempts from 2 
 * 							  to 30.
 * 							modify DEFAULT_GUI_SENDCACHE_COMM_RETRY_ATTEMPTS
 * 							defect 11407 Ver 7.0.0
 *--------------------------------------------------------------------
 */

/**
 * System Properties are loaded from the cfg files. 
 * 
 * <p>System Properties are initialized depending if we are on the 
 * Application Server or a Client Workstation.
 * 
 * <ul>
 * <li>Client Worstation properties are loaded from:
 * 
 * <ul>
 * <li>RTSCLS.cfg - Properties that are common for the Substation.
 * <li>RTSCLD.cfg - Properties that can be unique to the Workstation.
 * <li>Version.cfg - Properties related to the version of code.
 * <eul>
 * 
 * <li>Application Server properties are loaded from:
 * 
 * <ul>
 * <li>Server.cfg - Properties releated to the Server.
 * <li>Version.cfg - Properties related to the version of code.
 * <eul>
 * 
 * <eul>
 * 
 * The warning is to make sure the appropriate property is being
 * used at the appropriate time.
 * 
 * <p>The first time this class is entered via a getter, the 
 * initialize method is called to setup the properties from the 
 * appropriate cfg files.
 *
 * @version	POS_700			08/08/2012
 * @author	Nancy Ting
 * @since					08/06/2001 09:45:50
 */

public class SystemProperty
{
	public static final int APP_DEV_STATUS = 1;
	public static final int APP_PROD_STATUS = 0;
	private static final String CLIENT_DYNAMIC_CONFIG = "cfg/rtscld";
	private static final String CLIENT_STATIC_CONFIG = "cfg/rtscls";
	private static final int COUNTY_OFFICE_CD = 3;

	/**
	 * Default RSPS Directory
	 */
	private static String COUNTY_RSPS_DIRECTORY = "D:\\RTS\\RSPS";
	/**
	 * Default RSPS Log Directory
	 */
	private static String COUNTY_RSPS_LOG_DIRECTORY = "logs";

	private static final int DATE_LENGTH_8 = 8;

	// defect 10670
	private static final String DEFAULT_ACTV_DIR_PASSWORD = "rts4txdot";
	private static final String DEFAULT_ACTV_DIR_SERVER =
		"ldap://txdot-dc2:389";
	private static final String DEFAULT_ACTV_DIR_USER_ID =
		"isd-rts-ftp";
	// end defect 10670

	// defect 10827 
	private static final int DEFAULT_DTA_REJECT_SURVIVOR_DATE = 99991231; 
	// end defect 10827 
	
	private static final String DEFAULT_BASE_DIRECTORY = "D:\\rts\\";

	private static String DEFAULT_BLACKBOOK_CUST_PREFIX = "prod";
	private static final String DEFAULT_BROWSER_PATH_VALUE =
		"\"c:\\Program Files\\Internet Explorer\\iexplore.exe\"";
	private static final String DEFAULT_BUYER_TAG_START_DATE =
		"20080301";
	private static final String DEFAULT_BUYER_TAG_START_DATE_MSG =
		"Invalid Buyer Tag start date. Default used.";

	private static final String DEFAULT_CACHE_VERSION_MSG =
		" Using Default value for CacheVersionNo";
	private static final int DEFAULT_CONNECT_TIMEOUT_SERVER_DOWN = 3000;
	private static final int DEFAULT_CONNECT_TIMEOUT_SERVER_UP = 45000;
	private static final String DEFAULT_CUMLOG = "cumrcpt.log";

	// defect 10670
	private static final String DEFAULT_EDIR_USER_ID = "RSPSadmin";
	private static final String DEFAULT_EDIR_PASSWORD = "Password123";
	// end defect 10670

	// defect 11318
	private static final String DEFAULT_GUI_LOG_FILENAME = "rtsapp_GUI.";
	// end defect 11318

	private static final String DEFAULT_HELP_DESK_FLAG = "OFF";
	private static final String DEFAULT_LOG_DIR_MSG =
		" Using Default value for LogFileDir";
	private static final String DEFAULT_LOG_FILE_DIR = "log/";

	private static final String DEFAULT_MAIL_DIR =
		"D:\\rts\\messages\\";
	private static final String DEFAULT_MAIL_GATEWAY =
		"wt-rts-pas1.dot.state.tx.us";
	private static final String DEFAULT_MAXLOGFILESIZE_MSG =
		" Using Default value for MaxLogFileSize";

	// defect 10575 
	private static final String DEFAULT_MYPLATES_URL_MSG =
		"Using Default value for MyPlates URL";
	private static final String DEFAULT_MYPLATES_URL =
		"rts.myplates.com";
	//	defect 9831 
	private static final String DEFAULT_MVDI_EXPORT_FTP_SERVER_NAME =
		"TXDOT-HQ44";
	private static final String DEFAULT_MVDI_EXPORT_FTP_USERID =
		"rtsftp";
	private static final String DEFAULT_MVDI_EXPORT_FTP_PASSWORD =
		"rtsftp12";
	private static final String DEFAULT_MVDI_EXPORT_FTP_DIRECTORY =
		"Placards/UnitTest";
	private static final String DEFAULT_MVDI_EXPORT_FTP_FILENAME =
		"MVDIExport";
	private static final int DEFAULT_DSABLD_PLCRD_MAX_RCDS = 500;
	// end defect 9831 

	// defect 10900 
	private static int siMaxFraudReportDays;
	private static final int DEFAULT_MAX_FRAUD_REPORT_DAYS = 366;
	private static final String DEFAULT_MAX_FRAUD_REPORT_DAYS_MSG =
		" Using Default value for Max Fraud Report Days";
	// end defect 10900 
	
	// defect 11050 
	private static int siMaxAddDsabldPlcrds;
	private static final int DEFAULT_MAX_ADD_DSABLD_PLCRDS
	= 5;
	private static final String  DEFAULT_MAX_ADD_DSABLD_PLCRDS_MSG  =
		" Using Default value for Max Add Disabled Placards";
	// end defect 11050 

	private static final int DEFAULT_PRINT_WAIT_TIME = 15;

	private static final String DEFAULT_PTO_START_DATE = "20080101";
	private static final String DEFAULT_PTO_START_DATE_MSG =
		"Invalid PTO start date. Default used.";
	private static final String DEFAULT_PURGE_ADMIN_MSG =
		" Using Default value for Admin Log Purge";
	private static final String DEFAULT_PURGE_ADMIN_TABLES_MSG =
		" Using Default value for Admin Tables Purge";
	private static final String DEFAULT_PURGE_EXMPT_AUDIT_MSG =
		" Using Default value for Exempt Audit Purge";
	private static final String DEFAULT_PURGE_INET_ADDR_CHG_MSG =
		" Using Default value for Internet Address Change Purge";
	private static final String DEFAULT_PURGE_INET_REN_MSG =
		" Using Default value for Internet Renewal Purge";
	private static final String DEFAULT_PURGE_INET_SPAPP_COMP_MSG =
		" Using Default value for Internet Special Plate Complete "
			+ "Trans Purge";
	private static final String DEFAULT_PURGE_INET_SPAPP_INCOMP_MSG =
		" Using Default value for Internet Special Plate Incomplete "
			+ "Trans Purge";
	private static final String DEFAULT_PURGE_INET_SUMM_MSG =
		" Using Default value for Internet Summary Purge";
	private static final String DEFAULT_PURGE_INV_HIST_MSG =
		" Using Default value for Inv. History Purge";
	private static final String DEFAULT_PURGE_REPRINT_STICKER_MSG =
		" Using Default value for Reprint Sticker Purge";
	private static final String DEFAULT_PURGE_SEC_LOG_MSG =
		" Using Default value for Security Log Purge";
	private static final String DEFAULT_PURGE_SPCL_PLT_REJ_LOG_MSG =
		" Using Default value for Special Plate Rejection Log Purge";
	private static final String DEFAULT_PURGE_SPCL_PLT_TRANS_HSTRY_MSG =
		" Using Default value for Special Plate Trans History Purge";

	// defect 9803 
	private static final String DEFAULT_PURGE_WEB_SERVICES_MSG =
		" Using Default value for Web Services Tables";
	// end defect 9803 

	// defect 9889 
	private static final String DEFAULT_PURGE_SET_DELINDI_DSABLD_PLCRD_CUST_MSG =
		" Using Default value for Disabled Placard Customer Purge - Set DeleteIndi";

	private static final String DEFAULT_PURGE_DSABLD_PLCRD_CUST_MSG =
		" Using Default value for Disabled Placard Customer Purge";
	// end defect 9889

	// defect 9955 
	private static final String DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_MSG =
		" Using Default value for Internet Deposit Recon Purge";
	private static final String DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_HSTRY_MSG =
		" Using Default value for Internet Deposit Recon History Purge";
	// end defect 9955 

	private static final String DEFAULT_PURGE_TRANS_MSG =
		" Using Default value for Transaction Tables Purge";

	private static final String DEFAULT_PURGE_VI_IVTRS_MSG =
		" Using Default value 0 for IVTRS VI Release Days Offset";

	// defect 10231 
	private static final String DEFAULT_PURGE_ETTL_HSTRY_MSG =
		" Using Default value for ETitle History Purge";
	// end defect 10231  

	// defect 8087 
	private static final String DEFAULT_PURGE_WS_STATUS_LOG_MSG =
		" Using Default value for Workstation Status Log Purge";
	// end defect 8087 

	// defect 10421 
	private static final String DEFAULT_PURGE_ITRNT_TRANS_DEL_LOG_MSG =
		" Using Default value for Internet Transaction Delete Log Purge";
	// end defect 10421

	private static final String DEFAULT_REBOOT_NUM_MSG =
		" Using Default value for rebootNumber";
	private static final String DEFAULT_RECEIPT_DIR = "rcpt\\";
	private static final String DEFAULT_RPT_DIR = "rpt\\";
	private static final String DEFAULT_RTSAPPL_DIR = "rtsappl\\";
	private static final String DEFAULT_SETASIDE_DIR = "setaside\\";
	private static final String DEFAULT_STAGGER_TIME_MSG =
		" Using Default value for rebootStagerTime";
	private static final String DEFAULT_TRACE_LEVEL_MSG =
		" Using Default value for logTraceLevel";
	private static final String DEFAULT_UG_SUBDIR = "ug\\";
	private static final String DEFAULT_VOID_TRANS_LOG_FILE =
		"voidtrans.log";
	// defect 9675
	private static final int DEFAULT_VP_OFFICE = 294;
	private static final int DEFAULT_VP_WSID = 999;
	// end defect 9675

	private static final int DEFAULTCACHEVERSIONNO = 0;
	private static final int DEFAULTCOMMSERVERBATCHDELTA = 45;
	private static final int DEFAULTLOGTRACELEVEL = 4;
	private static final int DEFAULTMAXLOGFILESIZE = 100;
	private static final int DEFAULTMFDEBUG = 0;
	private static final int DEFAULTMFGATEWAYPORT = 0;
	private static final int DEFAULTOFFICEISSUANCENO = -1;
	private static final int DEFAULTPRINTSTATUS = 0;
	private static final int DEFAULTPRODSTATUS = 0;

	// defect 10231 
	//private static final int DEFAULTPURGEADMINLOG = 90;
	private static final int DEFAULTPURGEADMINLOG = 180;
	// end defect 10231 

	// defect 8087 
	private static final int DEFAULTPURGEWSSTATUSLOG = 90;
	// end defect 8087 

	// defect 10421 
	private static final int DEFAULTPURGEITRNTTRANSDELLOG = 60;
	// end defect 10421 

	private static final int DEFAULTPURGEADMINTABLES = 30;

	private static final int DEFAULTPURGEEXMPTAUDIT = 400;
	private static final int DEFAULTPURGEINETADDRCHNG = 30;
	private static final int DEFAULTPURGEINETRENEWAL = 60;
	private static final int DEFAULTPURGEINETSPAPPCOMP = 60;

	private static final int DEFAULTPURGEINETSPAPPINCOMP = 30;

	private static final int DEFAULTPURGEINETSUMMARY = 12;
	private static final int DEFAULTPURGEINVHIST = 400;

	private static final int DEFAULTPURGEREPRNTSTKR = 400;
	private static final int DEFAULTPURGESECLOG = 180;
	private static final int DEFAULTPURGESPCLPLTREJLOG = 400;
	private static final int DEFAULTPURGESPCLPLTTRANSHSTRY = 400;

	// defect 9889 
	private static final int DEFAULTPURGESETDELINDIDSABLDPLCRDCUST = 30;
	private static final int DEFAULTPURGEDSABLDPLCRDCUST = 400;
	// end defect 9889 

	// defect 9803 
	private static final int DEFAULTPURGEWEBSERVICES = 400;
	// end defect 9803 

	// defect 9955 
	private static final int DEFAULTPURGEINETDEPOSITRECON = 400;
	private static final int DEFAULTPURGEINETDEPOSITRECONHSTRY = 400;
	// end defect 9955 

	// defect 10231 
	private static final int DEFAULTPURGEETTLHSTRY = 800;
	// end defect 10231 

	// defect 10700 
	private static final int DEFAULTPURGESPCLPLTPRMT = 180;
	private static final String DEFAULT_SPCL_PLT_PRMT_MSG =
		" Using Default value for Special Plate Permit Purge";
	// end defect 10700 

	private static final int DEFAULTPURGETRANS = 30;
	private static final int DEFAULTREBOOTSTAGGERTIME = 5;
	// defect 11073
	private static final int DEFAULTREMOTELISTENERPORTBE = 1353;
	private static final int DEFAULTREMOTELISTENERPORTGUI = 1354;
	// end defect 11073
	private static final int DEFAULTSTAGGERGROUPNUMBER = 8;
	// defect 6440
	private static final int DEFAULTSENDTRANSRPTFREQ = 2000;
	// end defect 6440  
	private static final int DEFAULTSUBSTATIONID = -1;
	private static final int DEFAULTWORKSTATIONID = -1;
	private static final String FILE_EXTENSION = "cfg";

	/**
	 * Default RSPS Flash Drive Source Dircetory
	 */
	private static String FLASHDRIVESOURCEDIRECTORY = "F:\\";

	private static final int HQ_OFFICE_CD = 1;

	public static final String HTTP_MODE = "Y";
	public static final String HTTPS_JKS_MODE = "N";

	private static final String JAVAVERSIONNOTMATCHERR =
		"Java Version does not match ";
	// defect 10129
	//private static final String JAVAVERSIONSTRING = "java.version";
	private static final String JAVAVERSIONSTRING = "java.specification.version";
	// end defect 10129
	private static final String MFGATE_NOT_SET_MSG = " MFGate not set";
	private static final String MRE_MSG =
		"Missing Resource Exception in SystemProperties:";
	private static final String PROPERTY_PRINT_IMMEDIATE =
		"PrintImmediate=";
	private static final int REGION_OFFICE_CD = 2;

	private static final String RTS_LOG4J_PROPERTY_FILE_NAME =
		"cfg/RTSLog4J-config-MDC.properties";
	// defect 9724 
	private static final String SHOWING_DELQNT_DAYS_MSG =
		"Showing Delinquent Days";
	// end defect 9724 
	private static RTSDate saBuyerTagStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20080101);

	// defect 10155 
	private static final String DEFAULT_DMV_START_DATE = "20091101";
	private static final String DEFAULT_DMV_START_DATE_MSG =
		"Invalid DMV start date. Default used.";
	private static RTSDate saDMVStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20091101);
	// end defect 10155 

	//	defect 10491 
	private static final String DEFAULTMFPERMITSTARTDATE = "20100901";
	private static final String DEFAULT_MFPERMIT_START_DATE_MSG =
		" Using Default value for MF Permit Start Date";
	private static RTSDate saMFPrmtStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20100901);
	// end defect 10491 

	// defect 10512
	// defect 10614
	//	private static final String DEFAULT_EREMINDER_FROM_EMAIL =
	//		"VTR_RTS-EReminder@txdmv.gov";
	//	private static String ssEReminderFromEmail;
	// end defect 10512

	// defect 10614
	private static final String DEFAULT_EREMINDER_EMAIL =
		"VTR_RTS-EReminder@txdmv.gov";
	//		"eReminder@txdmv.gov";
	private static String ssEReminderEmail;
	// end defect 10614

	// defect 10670
	private static final String DEFAULT_eDIR_SERVER =
		"ldap://144.45.211.121:389";
	private static String sseDirServer;
	// end defect 10670

	// defect 10685 
	private static final String DEFAULT_FEE_SIMPLIFY_START_DATE =
		"20110901";
	private static final String DEFAULT_FEE_SIMPLIFY_START_DATE_MSG =
		"Invalid Fee Simplification start date. Default used.";
	private static RTSDate saFeeSimplifyStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20110901);
	// end defect 10685 

	// defect 10689 
	private static final String DEFAULT_VEH_COLOR_START_DATE =
		"20110701";
	private static final String DEFAULT_VEHCOLOR_START_DATE_MSG =
		"Invalid Mandatory Vehicle Color start date. Default used.";
	private static RTSDate saVehColorStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20110701);
	// end defect 10689 

	// defect 10949 
	private static final String DEFAULT_DEALER_INVALIDATE_REG_START_DATE =
		"20110901";
	private static final String DEFAULT_DEALER_INVALIDATE_REG_START_DATE_MSG =
		"Invalid Dealer Invalidate Reg start date. Default used.";
	private static RTSDate saDlrInvalidRegStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20110901);
	private static final String DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE  =
		"20110901";
	private static final String DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE_MSG =
		"Invalid No Default Title Trans Dt start date. Default used.";
	private static RTSDate saNoDefaultTtlTransDtStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20110901);
	// end defect 10949 

	public static org.apache.log4j.Logger saLog4jSystemPropLog;

	private static RTSDate saPTOStartDate =
		new RTSDate(RTSDate.YYYYMMDD, 20080101);

	// defect 10413 
	private static boolean sbResetFundsDB2QueryOptimization;
	private static int siResetFundsDB2QueryOptimizationLevel;
	private static final boolean DEFAULT_RESET_FUNDS_DB2_QRY_OPT =
		false;
	private static final int DEFAULT_RESET_FUNDS_DB2_QRY_OPT_LVL = 0;
	// end defect 10413

	// defect 11116
	private static final String DEFAULT_SOAPRSPS_ENDPOINT = 
		"http://144.45.211.125/cgi-bin/RSPS/SOAPRSPS.cgi";
	private static final String DEFAULT_SOAPRSPS_APPLICATION = "SOAPRSPS";
	private static final String DEFAULT_SOAPRSPS_PASSWORD = "RSPSadmin";
	private static final String DEFAULT_SOAPRSPS_USERID = "Password123";
	// end defect 11116
	
	// defect 11407
	private static final int DEFAULT_GUI_SENDCACHE_COMM_RETRY_ATTEMPTS = 30;
	// end defect 11407

	private static boolean sbCheckInsurance = false;
	
		// property values
	private static boolean sbCommServer;
	private static boolean sbClientServer;
	private static boolean sbCounty;
	private static boolean sbHQ;
	private static boolean sbInitialized = false;
	private static boolean sbLog4jWatch = false;
	private static boolean sbRegion;

	private static boolean sbStaticTablePilot = false;
	
	//private static boolean sbWorkstation;

	// defect 9724 
	private static boolean sbShowDelqntDays = false;
	// end defect 9724 

	private static final String SERVER_CONFIG = "cfg/server";

	private static int siCacheVersionNo;

	//	This parameter represents the number of minutes prior to batch 
	// schedule that a Comm Server and a non Data Server should 
	// refresh/reboot
	private static int siCommServerBatchDelta;
	private static int siConnectTimeOutServDown;
	private static int siConnectTimeOutServUp;
	private static int siLog4jWatchTime = 0;
	private static int siLogTraceLevel;

	/**
	 * This is the version of the application to be used on MainFrame 
	 * side.
	 */
	private static int siMainFrameVersion;

	private static int siMaxLogFileSize;
	private static int siMFDebug;
	private static int siMFJGatewayPort;
	private static int siOfficeIssuanceCd;
	private static int siOfficeIssuanceNo;
	private static int siPrintImmediateIndi;
	private static int siPrintStatus;

	//	Status to determine if production enviromemt
	private static int siProdStatus;
	private static int siPurgeAdminLog = 90;
	private static int siPurgeAdminTables = 30;

	private static int siPurgeExmptAudit = 400;
	private static int siPurgeInetAddrChng = 365;
	private static int siPurgeInetRenewal = 60;
	private static int siPurgeInetSPAPPComp;
	private static int siPurgeInetSPAPPIncomp = 30;
	private static int siPurgeInetSummary = 365;
	private static int siPurgeInvHist = 400;
	private static int siPurgeReprntStkr = 400;
	private static int siPurgeSecLog = 180;
	private static int siPurgeSpclPltRejLog = 400;

	// defect 9889
	private static int siPurgeSetDelIndiDsabldPlcrdCust = 30;
	private static int siPurgeDsabldPlcrdCust = 180;
	// end defect 9889

	// deposit 9955
	private static int siPurgeInetDepositRecon = 400;
	private static int siPurgeInetDepositReconHstry = 400;
	// end defefect 9955 

	// defect 9803 
	private static int siPurgeWebServices = 400;
	// end defect 9803 

	// defect 10231 
	private static int siPurgeETtlHstry = 800;
	// end defect 10231  

	// defect 8087 
	private static int siPurgeWSStatusLog = 90;
	// end defect 8087 

	// defect 10421 
	private static int siPurgeItrntTransDelLog = 60;
	// end defect 10421 

	private static int siPurgeSpclPltTransHstry = 400;
	private static int siPurgeTrans = 60;

	// defect 10700 
	private static int siPurgeSpclPltPrmt = 180;
	// end defect 10700 

	private static int siPurgeViIvtrsReleaseDays = 20;
	private static int siRebootNumber;
	private static int siRebootStaggerTime;

	//	Redirect Substa and WsId
	private static int siRedirectSubstaId = Integer.MIN_VALUE;
	private static int siRedirectWsId = Integer.MIN_VALUE;
	// defect 11073
	private static int siRemoteListenerPortBE;
	private static int siRemoteListenerPortGUI;
	// end defect 11073
	private static int siSubStationId;

	// defect 6440 
	private static int siSendTransRptFreq;
	// end	defect 6440

	// defect 9675
	private static int siVpOfcIssuanceNo;
	private static int siVpWsId;
	// end defect 9675
	private static int siWorkStationId;

	// defect 11407
	private static int siGUISendCacheCommRetryAttempts;
	// end defect 11407
	
	// defect 10075
	private static String ssExternalMedia;
	// end defect 10075

	// defect 10670
	private static String ssActiveDirPassword;
	private static String ssActiveDirServer;
	private static String ssActiveDirUserId;
	// end defect 10670

	//	add ssAdOu field to handle development Directory Processing
	private static String ssAdOu;
	private static String ssBarcodePort;
	private static String ssBatchErrorFileName;
	private static String ssBatchLogFileName;
	private static String ssBlackBookCustPrefix;
	// defect 9831 
	private static String ssMVDIExportFTPServerName;
	private static String ssMVDIExportFTPUserId;
	private static String ssMVDIExportFTPPassword;
	private static String ssMVDIExportFTPDirectory;
	private static String ssMVDIExportFTPFileName;
	private static int siDsabldPlcrdMaxRcds;
	// end defect 9831 
	// end defect 9831

	// defect 10827 
	private static int siDTARejectSurvivorDate;
	// end defect 10827 
	
	// defect 8984
	private static String ssSendTransFileLoc;
	private static String ssBuildSendTransReviseDate;
	// end defect 8984

	// defct 9965
	private static String ssFundsUpdateFileLoc;
	private static String ssFundsUpdateReviseDate;
	// end defect 9965

	private static String ssBlackBookURL;

	private static String ssCacheDirectory;
	private static String ssCashdrawerPort;
	//	This is RTS Application root directory.
	private static String ssCommServerAppDirectory;
	//	This is the directory where code and cache will be stored at 
	// comm server
	private static String ssCommServerName;

	// defect 10575
	private static String ssMyPlatesURL;
	// end defect 10575 

	/**
	 * This value determines if we communicating in secure mode 
	 * or not.
	 */
	private static String ssCommunicationsMode;

	/**
	 * This is the directory for the RSPS Stuff on the 
	 * county pos machine.
	 */
	private static String ssCountyRSPSDirectory;

	private static String ssCumRcptLogFileName;
	private static String ssCurrentEmpId;
	private static String ssDatasource;

	// add new properties and re-align the old ones.
	private static String ssDBPassword;
	private static String ssDBPasswordBatch;

	private static String ssDBPasswordSendTrans;

	private static String ssDBUser;
	private static String ssDBUserBatch;
	private static String ssDBUserSendTrans;

	private static String ssDealerTitleDirectory;
	//	Added to Static cfg file
	private static String ssDefaultBrowserPath;

	// defect 10670
	private static String ssEDirUserId;
	private static String ssEDirPassword;
	// end defect 10670

	// Added for Exempts export	
	private static String ssExportsDirectory;

	/**
	 * RSPS Flash Drive.
	 * <br>This is where refreesh is putting all RSPS updates
	 * destined for the laptop. 
	 * This is where the RSPS Updates Info is coming from.
	 */
	private static String ssFlashDriveSourceDirectory;

	private static String ssFontName;
	//	Name of the font which will be used by all swing conponents.
	private static String ssFontSize;
	// This is the name of user which will be used to ftp files from 
	// comm server.
	private static String ssFtpPassword;
	//	This is the name of comm server. It could be IP address of 
	// network name which is accessible through TCP/IP
	private static String ssFtpUser;
	// defect 11318
	private static String ssGUILogFileName;
	// end defect 11318
	private static String ssHelpDeskFlag;
	private static String ssHelpDir;
	private static String ssImagesDir;
	private static String ssImageType;

	private static String ssJavaVersion;
	private static String ssLogFileDir;
	private static String ssLogFileExt;
	private static String ssLogFileName;
	private static String ssMailDir;
	private static String ssMailGateway;
	private static String ssMFDBName;
	/**
	 * Version for the MainFrame Interface between MfAccess and the 
	 * CICS - COBOL programs.
	 */
	private static String ssMFInterfaceVersionCode;
	private static String ssMFJGate;

	// these fields had to be re-added after the merge!
	private static String ssMFJGateway;
	private static String ssMFServer;
	private static String ssMFTimeout;
	private static String ssMQBackupQueueName;
	private static String ssMQChannelName;

	private static String ssMQHostName;
	private static String ssMQPort;
	private static String ssMQQueueManager;
	private static String ssMQQueueName;

	private static String ssPrintWaitTime;
	private static String ssReceiptsDirectory;
	private static String ssReportsDirectory;
	/**
	 * This is the directory for the rsps logs.
	 */
	private static String ssRSPSLogDir;

	/**
	  * RSPS Suffix for Log Files.
	  */
	private static String ssRSPSLogSuffix;

	/**
	 * RSPS Suffix for Update Files.
	 */
	private static String ssRSPSUpdateSuffix;

	private static String ssRtsAppDirectory;
	//	This is RTS base directory
	private static String ssRtsBaseDirectory;
	private static String ssSendCacheLogFileName;
	private static String ssServletHost;
	private static String ssServletName;
	private static String ssServletPort;
	private static String ssSetAsideDir;
	
	// defect 11116
	private static String ssSoapRspsEndpoint;
	private static String ssSoapRspsApplication;
	private static String ssSoapRspsUserId;
	private static String ssSoapRspsPassword;
	// end defect 11116

	private static String ssTexasSureURL;

	private static String ssTransDir;
	private static String ssVersionDate;
	private static String ssVersionNo;
	private static String ssVoidTransLogFileName;

	private static Vector svFilesToPrune = new Vector();

	private static Vector svRSPSValidSuffix = new Vector();
	private static final String USING_STATIC_PILOT_TABLE_MSG =
		"Using Static Pilot Table";

	// cfg file settings
	private static final String VERSION_CONFIG = "cfg/version";
	private static final String YES = "y";

	// defect 10462
	public static final int DB2_LOGGING_NONE = 0;
	public static final int DB2_LOGGING_ERROR_ONLY = 1;
	public static final int DB2_LOGGING_ALL_MF_REQ = 2;
	private static final String DEFAULT_VSAM_LOG_ERR_MSG =
		" Using Default value for VSAM Logging ";
	private static final String DEFAULT_DB2_LOG_REQ_MSG =
		" Using Default value for DB2 Logging MF Requests";
	private static final String DEFAULT_PURGE_MF_REQ_HSTRY_MSG =
		" Using Default value for MF Req Hstry Purge";
	private static final boolean DEFAULTVSAMLOGERR = true;
	private static final int DEFAULTDB2LOGMFREQCD = 0;
	private static int DEFAULTPURGEMFREQHSTRY = 180;
	private static int siPurgeMFReqHstry = 180;
	private static boolean sbVSAMLogMFErr = true;
	private static int siDB2LogMFReqCd = 0;
	// end defect 10462 

	//defect 10797
	private static String ssWebAgentPort;
	private static String ssWebAgentProtocol;
	private static String ssWebAgentHost;
	//end defect 10797
	
	//defect 11174
	private static String ssWebAgentPath;
	//end defect 11174
	
	// defect 10119
	// Make the default time out 10 seconds.
	private static int DEFAULT_TEXAS_SURE_TIMEOUT = 10000;
	private static boolean sbTexasSureHang = false;
	private static int siTexasSureTestThreadCount = 0;
	private static int siTexasSureTimeOut = 0;	
	// end defect 10119

	// defect 10119
	/**
	 * Returns the value indicating if we are emulating a TexasSure 
	 * hang condition or not.
	 * 
	 * @return boolean
	 */
	public static boolean emulatingTexasSureHang()
	{
		initialize();
		return sbTexasSureHang;
	}
	// defect 10119
	
	// defect 10670
	/**
	 * Return the password for the proxy user id used for Active 
	 * Directory access.
	 * 
	 * @return ssActiveDirPassword
	 */
	public static String getActiveDirPassword()
	{
		initialize();
		return ssActiveDirPassword;
	}

	/**
	 * Return the proxy user id used for Active Directory access.
	 * 
	 * @return ssActiveDirServer
	 */
	public static String getActiveDirServer()
	{
		initialize();
		return ssActiveDirServer;
	}

	/**
	 * Return the proxy user id used for Active Directory access.
	 * 
	 * @return ssActiveDirUserId
	 */
	public static String getActiveDirUserId()
	{
		initialize();
		return ssActiveDirUserId;
	}
	// end defect 10670

	/**
	 * Returns the AdOu.
	 * 
	 * @return String - AdOu 
	 */
	public static String getAdOu()
	{
		initialize();
		return ssAdOu;
	}

	/**
	 * Returns the BarCode Port
	 *
	 * @return String
	 */
	public static String getBarcodePort()
	{
		initialize();
		return ssBarcodePort;
	}

	/**
	 * Return BatchErrorFileName.
	 *
	 * @return String
	 */
	public static String getBatchErrorFileName()
	{
		initialize();
		return ssBatchLogFileName;
	}

	/**
	 * Return BatchLogFileName.
	 *
	 * @return String
	 */
	public static String getBatchLogFileName()
	{
		initialize();
		return ssBatchLogFileName;
	}

	/**
	 * Gets the Black Book Customer Prefix used by Presumptive AbstractValue.
	 * 
	 * 	This is the value that is sent to BlackBook to log who made the 
	 * 	request.  The format of this value is Prefix + -Office.
	 * 
	 * @return String
	 */
	public static String getBlackBookCustPrefix()
	{
		initialize();
		return ssBlackBookCustPrefix;
	}

	/**
	 * Gets the url used to retrieve the Presumptive AbstractValue from
	 * BlackBook.
	 * 
	 * @return String
	 */
	public static String getBlackBookURL()
	{
		initialize();
		return ssBlackBookURL;
	}
	
	/**
	 * Return the start date for Buyer Tag processing
	 *
	 * @return RTSDate
	 */
	public static RTSDate getBuyerTagStartDate()
	{
		initialize();
		return saBuyerTagStartDate;
	}

	/**
	 * Return CacheDirectory.
	 * 
	 * <p>Also creates the Directory if it does not exist.
	 *
	 * @return String
	 */
	public static String getCacheDirectory()
	{
		initialize();

		File laFile = new File(ssCacheDirectory);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}

		return ssCacheDirectory;
	}

	/**
	 * Return the Version Number.
	 * 
	 * @return int
	 */
	public static int getCacheVersionNo()
	{
		initialize();
		return siCacheVersionNo;
	}

	/**
	 * Return the CashDrawer Port
	 *
	 * @return String
	 */
	public static String getCashdrawerPortName()
	{
		initialize();
		return ssCashdrawerPort;
	}

	/**
	 * Return the CommServerAppDirectory.
	 * 
	 * @return String
	 */
	public static String getCommServerAppDirectory()
	{
		initialize();
		return ssCommServerAppDirectory;
	}

	/**
	 * Return the CommServerBatchDelta.
	 * This is used to determine how long after the Comm Server the 
	 * other workstations reboot.
	 *
	 * @return int
	 */
	public static int getCommServerBatchDelta()
	{
		initialize();
		return siCommServerBatchDelta;
	}

	/**
	 * Return the CommServerName (CodeServer).
	 * This is the control point where the workstation will 
	 * refresh from.
	 * 
	 * @return String
	 */
	public static String getCommServerName()
	{
		initialize();
		return ssCommServerName;
	}

	/**
	 * Return Communications Mode.
	 * 
	 * @return String
	 */
	public static String getCommunicationsMode()
	{
		initialize();
		return ssCommunicationsMode;
	}

	/**
	 * Return ConnectTimeOutServDown.
	 * This is how long we will wait for send to server to return before
	 * throwing an error.  This value is in effect when the Server 
	 * is marked as down.
	 *
	 * @return int
	 */
	public static int getConnectTimeOutServDown()
	{
		initialize();
		return siConnectTimeOutServDown;
	}

	/**
	 * Return ConnectTimeOutServUp.
	 * This is how long we will wait for send to server to return before
	 * throwing an error.  This value is in effect when the Server 
	 * is marked as up.
	 *
	 * @return int
	 */
	public static int getConnectTimeOutServUp()
	{
		initialize();
		return siConnectTimeOutServUp;
	}

	/**
	 * Return the RSPS Working Directory.
	 * 
	 * @return String
	 */
	public static String getCountyRSPSDirectory()
	{
		initialize();
		return ssCountyRSPSDirectory;
	}

	/**
	 * Returns cumRcptLogFileName
	 *
	 * @return String
	 */
	public static String getCumRcptLogFileName()
	{
		initialize();
		return ssCumRcptLogFileName;
	}

	/**
	 * Returns the currently logged on Employee.
	 *
	 * @return String
	 */
	public static String getCurrentEmpId()
	{
		initialize();
		return ssCurrentEmpId;
	}

	/**
	 * Return the Data Source name.
	 *
	 * @return String
	 */
	public static String getDatasource()
	{
		initialize();
		return ssDatasource;
	}
	
	/**
	 * Returns int to determine level of DB2 Logging
	 *   0 - No Logging
	 *   1 - Errors Only 
	 *   2 - All Requests   
	 *
	 * @return int 
	 */
	public static int getDB2LogMFReqCd()
	{
		initialize();
		return siDB2LogMFReqCd;
	}

	/**
	 * Return the Database Password for the application.
	 *
	 * @return String
	 */
	public static String getDBPassword()
	{
		initialize();
		return ssDBPassword;
	}

	/**
	 * Returns the Database Password to be used when in Database 
	 * Restricted Mode (Batch).
	 * 
	 * @return String
	 */
	public static String getDBPasswordBatch()
	{
		initialize();
		return ssDBPasswordBatch;
	}

	/**
	 * Get the SendTrans DB Password.
	 * 
	 * @return String
	 */
	public static String getDBPasswordSendTrans()
	{
		initialize();
		return ssDBPasswordSendTrans;
	}

	/**
	 * Return the Database User for the Application.
	 *
	 * @return String
	 */
	public static String getDBUser()
	{
		initialize();
		return ssDBUser;
	}

	/**
	 * Returns the Database UserId to be used when in Database 
	 * Restricted Mode (Batch).
	 * 
	 * @return String
	 */
	public static String getDBUserBatch()
	{
		initialize();
		return ssDBUserBatch;
	}

	/**
	 * Get the SendTrans DB UserId.
	 * 
	 * @return String
	 */
	public static String getDBUserSendTrans()
	{
		initialize();
		return ssDBUserSendTrans;
	}
	
	/**
	 * Return the start date for Dealer Invalidate Registration
	 *
	 * @return RTSDate
	 */
	public static RTSDate getDlrInvalidRegStartDate()
	{
		initialize();
		return saDlrInvalidRegStartDate;
	}

	/**
	 * Return the Default Browser Path.
	 * Used to define what browser to use to launch help.
	 *
	 * @return String
	 */
	public static java.lang.String getDefaultBrowserPath()
	{
		initialize();
		return ssDefaultBrowserPath;
	}
	/**
	 * Return the start date for new DMV agency
	 *
	 * @return RTSDate
	 */
	public static RTSDate getDMVStartDate()
	{
		initialize();
		return saDMVStartDate;
	}

	// defect 10670
	/**
	 * Return the password for the proxy user id used for eDirectory 
	 * access.
	 * 
	 * @return ssEDirPassword
	 */
	public static String getEDirPassword()
	{
		initialize();
		return ssEDirPassword;
	}

	/**
	 * Return the proxy user id used for eDirectory access.
	 * 
	 * @return ssEDirUserId
	 */
	public static String getEDirUserId()
	{
		initialize();
		return ssEDirUserId;
	}
	// end defect 10670

	/**
	 * Return the start date for new Fee Simplification rules
	 *
	 * @return RTSDate
	 */
	public static RTSDate getFeeSimplifyStartDate()
	{
		initialize();
		return saFeeSimplifyStartDate;
	}

	/**
	 * Return the Exports Directory.
	 *
	 * @return String
	 */
	public static String getExportsDirectory()
	{
		initialize();
		return ssExportsDirectory;
	}

	/**
	 * Return the DsabldPlcrd Max Rcds
	 *
	 * @return int 
	 */
	public static int getDsabldPlcrdMaxRcds()
	{
		initialize();
		return siDsabldPlcrdMaxRcds;
	}
	/**
	 * Return the DTA Reject Survivor Date
	 *
	 * @return int 
	 */
	public static int getDTARejectSurvivorDate()
	{
		initialize();
		return siDTARejectSurvivorDate;
	}
	
	/**
	 * Return the purge days for Purging ETtl Hstry Table rows.
	 *
	 * @return int
	 */
	public static int getPurgeETtlHstry()
	{
		initialize();
		return siPurgeETtlHstry;
	}

	/**
	 * 
	 * Return Type of External Media in Use Indicator
	 * 
	 * @return String
	 */
	public static String getExternalMedia()
	{
		initialize();
		return ssExternalMedia;
	}

	/**
	 * Retruns the vector of files to be pruned.
	 * 
	 * @return Vector
	 */
	public static Vector getFilesToPrune()
	{
		initialize();
		return svFilesToPrune;
	}

	/**
	 * Return the RSPS Flash Drive Directory.
	 * 
	 * @return String
	 */
	public static String getFlashDriveSourceDirectory()
	{
		initialize();
		return ssFlashDriveSourceDirectory;
	}

	/**
	 * Return the Font name used for the application.
	 *
	 * @return String
	 */
	public static String getFontName()
	{
		initialize();
		return ssFontName;
	}

	/**
	 * Return the Font Size used for the application.
	 * 
	 * @return String
	 */
	public static String getFontSize()
	{
		initialize();
		return ssFontSize;
	}

	/**
	 * Return the Password used to connect to the Code Server for
	 * refresh.
	 * 
	 * @return String
	 */
	public static String getFtpPassword()
	{
		initialize();
		return ssFtpPassword;
	}

	/**
	 * Return the UserName used to connect to the Code Server for
	 * referesh.
	 * 
	 * @return String
	 */
	public static String getFtpUser()
	{
		initialize();
		return ssFtpUser;
	}

	/**
	 * Return the GUI Log File Name.
	 *
	 * @return String
	 */
	public static String getGUILogFileName()
	{
		initialize();
		return ssGUILogFileName;
	}

	/**
	 * Return the number of attempts for RTSMainGUI to attempt to connect to
	 * RTSMainBE. 
	 * 
	 * @return int
	 */
	public static int getGUISendCacheCommRetryAttempts()
	{
		initialize();
		return siGUISendCacheCommRetryAttempts;
	}
	
	/**
	 * Return the Help Desk Flag.
	 * This flag allows the help desk to connect to any county and 
	 * look at their Accounting data from MainFrame.
	 * 
	 * @return String
	 */
	public static String getHelpDeskFlag()
	{
		initialize();
		return ssHelpDeskFlag;
	}

	/**
	 * Returns the UsersGuide Directory.
	 * path is full address.
	 *
	 * @return java.lang.String
	 */
	public static String getHelpDir()
	{
		initialize();
		return ssHelpDir;
	}

	/**
	 * Return the Images Directory.
	 * Path is relative the application root.
	 * 
	 * <p>If the directory does not exist, it is created.
	 * 
	 * @return String
	 */
	public static String getImagesDir()
	{
		initialize();

		File laFile = new File(ssImagesDir);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}

		return ssImagesDir;
	}

	/**
	 * Return the Image Type.
	 *
	 * @return String
	 */
	public static String getImageType()
	{
		initialize();
		return ssImageType;
	}

	/**
	 * Return the Java Version.
	 *
	 * @return String
	 */
	public static String getJavaVersion()
	{
		initialize();
		return ssJavaVersion;
	}

	/**
	 * Get the Log4j Watch Time Interval.
	 * 
	 * @return int
	 */
	public static int getLog4jWatchTime()
	{
		return siLog4jWatchTime;
	}

	/**
	 * Return the Log File Extention.
	 * 
	 * @return String
	 */
	public static String getLogFileExt()
	{
		initialize();
		return ssLogFileExt;
	}

	/**
	 * Return the Log File Name.
	 *
	 * @return String
	 */
	public static String getLogFileName()
	{
		initialize();
		return ssLogFileName;
	}

	/**
	 * Return the Log Trace Level.
	 * 
	 * @return int
	 */
	public static int getLogTraceLevel()
	{
		initialize();
		return siLogTraceLevel;
	}

	/**
	 * Gets the mail directory.  Used by the messenger service.
	 * This method will also check to make sure that the directory
	 * exists and will create it if it does not.
	 * 
	 * @return String
	 */
	public static String getMailDir()
	{
		initialize();

		// Create the message directory if it does not exist.
		try
		{
			File laMailDir = new File(ssMailDir);
			if (!laMailDir.exists())
			{
				laMailDir.mkdir();
			}
			laMailDir = null;
		}
		catch (Exception aeEx)
		{
			// Create an RTS Exception so that it will be logged.
			new RTSException(RTSException.JAVA_ERROR, aeEx);
		}

		return ssMailDir;
	}

	/**
	 * Returns the mail gateway address.
	 * 
	 * @return String
	 */
	public static String getMailGateway()
	{
		initialize();
		return ssMailGateway;
	}

	/**
	 * Return the MainFrame Version.
	 * This is the Version of MainFrame code that POS is sending data 
	 * to.
	 * 
	 * @return int
	 */
	public static int getMainFrameVersion()
	{
		initialize();
		return siMainFrameVersion;
	}

	/**
	 * Return the Max Fraud Report Days
	 *
	 * @return int
	 */
	public static int getMaxFraudReportDays()
	{
		initialize();
		return siMaxFraudReportDays;
	}

	/**
	 * Return the Max Log File Size.
	 * Log files should be archived or pruned at this size.
	 *
	 * @return int
	 */
	public static int getMaxLogFileSize()
	{
		initialize();
		return siMaxLogFileSize;
	}

	/**
	 * Return the MainFrame Adabas database to use.
	 * 
	 * @return String
	 */
	public static String getMFDBName()
	{
		initialize();
		return ssMFDBName;
	}

	/**
	 * Return the MainFrame Debug indicator.
	 * This indicates if MainFrame is to retain TS Queues.
	 * 
	 * <p>Only used for debugging.
	 * 
	 * @return int
	 */
	public static int getMFDebug()
	{
		initialize();
		return siMFDebug;
	}
	/**
	 * Return the MainFrame CICS Interface Version.
	 * Set to reflect the version of CICS COBOL programs POS 
	 * is interfacing with.
	 * 
	 * <p>Possible values are T, U, and V.
	 * 
	 * @return String
	 */
	public static String getMFInterfaceVersionCode()
	{
		initialize();
		return ssMFInterfaceVersionCode;
	}

	/**
	 * Return the RTS Java Gateway address.
	 * 
	 * @return String
	 */
	public static String getMFJGate()
	{
		initialize();
		return ssMFJGate;
	}

	/**
	 * Return the MainFrame Gateway address.
	 * 
	 * @return String
	 */
	public static String getMFJGateway()
	{
		initialize();
		return ssMFJGateway;
	}

	/**
	 * Return the MainFrame Gateway Port.
	 *
	 * @return int
	 */
	public static int getMFJGatewayPort()
	{
		initialize();
		return siMFJGatewayPort;
	}

	/**
	 * Return the start date for MF Permit Retrieval
	 *
	 * @return RTSDate
	 */
	public static RTSDate getMFPrmtStartDate()
	{
		initialize();
		return saMFPrmtStartDate;
	}

	/**
	 * Return MainFrame Server.
	 * This is the CICS Region used.
	 * 
	 * @return String
	 */
	public static String getMFServer()
	{
		initialize();
		return ssMFServer;
	}

	/**
	 * Return MainFrame Timeout.
	 * 
	 * @return String
	 */
	public static String getMFTimeout()
	{
		initialize();
		return ssMFTimeout;
	}

	/**
	 * Returns ssMQBackupQueueName used by the MQSave process
	 * 
	 * @return String
	 */
	public static String getMQBackupQueueName()
	{
		initialize();
		return ssMQBackupQueueName;
	}
	/**
	 * Returns ssMQChannelName used by the MQ process
	 * 
	 * @return String
	 */
	public static String getMQChannelName()
	{
		initialize();
		return ssMQChannelName;
	}

	/**
	 * Returns ssMQHostName used by the MQ process
	 * 
	 * @return String
	 */
	public static String getMQHostName()
	{
		initialize();
		return ssMQHostName;
	}

	/**
	 * Returns ssMQPort used by the MQ process
	 * 
	 * @return String
	 */
	public static String getMQPort()
	{
		initialize();
		return ssMQPort;
	}

	/**
	 * Returns ssMQQueueManager used by the MQ process
	 * 
	 * @return Sting
	 */
	public static String getMQQueueManager()
	{
		initialize();
		return ssMQQueueManager;
	}

	/**
	 * Returns ssQueueName
	 * 
	 * @return String
	 */
	public static String getMQQueueName()
	{
		initialize();
		return ssMQQueueName;
	}

	/** Returns ssMVDIExportFTPDirectory
	 * 
	 * @ return String
	 */
	public static String getMVDIExportFTPDirectory()
	{
		initialize();
		return ssMVDIExportFTPDirectory;
	}

	/**
	 * Returns ssMVDIExportFTPFileName
	 * 
	 * @return String
	 */
	public static String getMVDIExportFTPFileName()
	{
		initialize();
		return ssMVDIExportFTPFileName;
	}

	/**
	 * Returns ssMVDIExportFTPUserId
	 * 
	 * @return String
	 */
	public static String getMVDIExportFTPUserId()
	{
		initialize();
		return ssMVDIExportFTPUserId;
	}

	/**
	 * Returns ssMVDIExportFTPPassword
	 * 
	 * @return String
	 */
	public static String getMVDIExportFTPPassword()
	{
		initialize();
		return ssMVDIExportFTPPassword;
	}

	/**
	 * Returns ssMVDIExportFTPServerName
	 * 
	 * @return String
	 */
	public static String getMVDIExportFTPServerName()
	{
		initialize();
		return ssMVDIExportFTPServerName;
	}
	
	/**
	 * Return the start date for No Default Title Trans Date
	 *
	 * @return RTSDate
	 */
	public static RTSDate getNoDefaultTtlTransDtStartDate()
	{
		initialize();
		return saNoDefaultTtlTransDtStartDate;
	}

	/**
	 * Return the Office Issuance Code for this workstation.
	 *
	 * @return int
	 */
	public static int getOfficeIssuanceCd()
	{
		initialize();
		return siOfficeIssuanceCd;
	}

	/**
	 * Return the Office Issuance Number for this workstation.
	 *
	 * @return int
	 */
	public static int getOfficeIssuanceNo()
	{
		initialize();
		return siOfficeIssuanceNo;
	}

	/**
	 * Return Print Immediate Indicator.
	 * 
	 * @return int
	 */
	public static int getPrintImmediateIndi()
	{
		initialize();
		return siPrintImmediateIndi;
	}

	/**
	 * Return Print Status.
	 *
	 * @return int
	 */
	public static int getPrintStatus()
	{
		initialize();
		return siPrintStatus;
	}

	/**
	 * Return Print Wait Time.
	 * This is how long reboot waits for batch to run.
	 *
	 * @return int
	 */
	public static int getPrintWaitTime()
	{
		try
		{
			return Integer.parseInt(ssPrintWaitTime);
		}
		catch (NumberFormatException aeNFE)
		{
			return DEFAULT_PRINT_WAIT_TIME;
		}
	}

	/**
	 * Return Production Status.
	 * 
	 * <p>Values:
	 * <ul>0 - Full Production 
	 * <ul>1 - Test / Development Mode.
	 * Desktop does not take up the whole screen.
	 * <eul>
	 *
	 * @return int
	 */
	public static int getProdStatus()
	{
		initialize();
		return siProdStatus;
	}

	/**
	 * Return the start date for PTO processing
	 *
	 * @return RTSDate
	 */
	public static RTSDate getPTOStartDate()
	{
		initialize();
		return saPTOStartDate;
	}

	/**
	 * Return the purge days for Purging Admin Log rows.
	 *
	 * @return int
	 */
	public static int getPurgeAdminLog()
	{
		initialize();
		return siPurgeAdminLog;
	}

	/**
	 * Return the purge days for Purging Admin Table rows where 
	 * Deleteindi <> 0.
	 *
	 * @return int
	 */
	public static int getPurgeAdminTables()
	{
		initialize();
		return siPurgeAdminTables;
	}

	/**
	 * Return the purge days for Purging Exempt Audit Table rows 
	 *
	 * @return int
	 */
	public static int getPurgeExmptAudit()
	{
		initialize();
		return siPurgeExmptAudit;
	}

	/**
	 * Return the purge days for Purging Disabled Placard Cust 
	 *
	 * @return int
	 */
	public static int getPurgeDsabldPlcrdCust()
	{
		initialize();
		return siPurgeDsabldPlcrdCust;
	}

	/**
	 * Return the purge days for Purging Internet Address Change rows.
	 *
	 * @return int
	 */
	public static int getPurgeInetAddrChng()
	{
		initialize();
		return siPurgeInetAddrChng;
	}

	/**
	 * Return the purge days for Purging Internet Deposit Recon 
	 *
	 * @return int
	 */
	public static int getPurgeInetDepositRecon()
	{
		return siPurgeInetDepositRecon;
	}

	/**
	 * Return the purge days for Purging Internet Deposit Recon Hstry
	 *
	 * @return int
	 */
	public static int getPurgeInetDepositReconHstry()
	{
		return siPurgeInetDepositReconHstry;
	}

	/**
	 * Return the purge days for Purging Internet Renewal rows.
	 *
	 * @return int
	 */
	public static int getPurgeInetRenewal()
	{
		return siPurgeInetRenewal;
	}

	/**
	 * Return the purge days for Purging Incomplete Internet Special 
	 * plate rows.
	 *
	 * @return int
	 */
	public static int getPurgeInetSPAPPComp()
	{
		return siPurgeInetSPAPPComp;
	}

	/**
	 * Return the purge days for Purging Complete Internet Special 
	 * plate rows.
	 *
	 * @return int
	 */
	public static int getPurgeInetSPAPPIncomp()
	{
		return siPurgeInetSPAPPIncomp;
	}

	/**
	 * Return the purge days for Purging InternetSummary rows.
	 *
	 * @return int
	 */
	public static int getPurgeInetSummary()
	{
		return siPurgeInetSummary;
	}

	/**
	 * Return the purge days for Purging InvHist Table rows.
	 *
	 * @return int
	 */
	public static int getPurgeInvHist()
	{
		initialize();
		return siPurgeInvHist;
	}

	/**
	 * Return the purge days for Purging Mainframe Request History
	 *
	 * @return int
	 */
	public static int getPurgeMFReqHstry()
	{
		initialize();
		return siPurgeMFReqHstry;
	}

	/**
	 * Return the purge days for Purging Reprint Sticker Table rows.
	 *
	 * @return int
	 */
	public static int getPurgeReprntStkr()
	{
		initialize();
		return siPurgeReprntStkr;
	}

	/**
	 * Return the purge days for Purging SecurityLog Table rows.
	 *
	 * @return int
	 */
	public static int getPurgeSecLog()
	{
		initialize();
		return siPurgeSecLog;
	}

	/**
	 * Return the purge days for setting DeleteIndi for Disabled 
	 * Placard Cust
	 *
	 * @return int
	 */
	public static int getPurgeSetDelIndiDsabldPlcrdCust()
	{
		initialize();
		return siPurgeSetDelIndiDsabldPlcrdCust;
	}

	/**
	 * Return value of siPurgeSpclPltRejLog 
	 * 
	 * @return int
	 */
	public static int getPurgeSpclPltRejLog()
	{
		initialize();
		return siPurgeSpclPltRejLog;
	}

	/**
	 * Return the purge days for Purging Special Plates Transaction
	 * History rows
	 *
	 * @return int
	 */
	public static int getPurgeSpclPltTransHstry()
	{
		initialize();
		return siPurgeSpclPltTransHstry;
	}

	/**
	 * Return the purge days for Purging Transaction Table rows.
	 *
	 * @return int
	 */
	public static int getPurgeTrans()
	{
		initialize();
		return siPurgeTrans;
	}

	/**
	 * Return the purge days for Purging Web Services Tables
	 *
	 * @return int
	 */
	public static int getPurgeWebServices()
	{
		initialize();
		return siPurgeWebServices;
	}

	/**
	 * Get the number of days to Offset before releasing VI on hold 
	 * for IVTRS.
	 * 
	 * @return int
	 */
	public static int getPurgeViIvtrsReleaseDays()
	{
		initialize();
		return siPurgeViIvtrsReleaseDays;
	}

	/**
	 * Returns the number of workstations in a group that will be
	 * rebooted at the same time.
	 * 
	 * @return int
	 */
	public static int getRebootNumber()
	{
		initialize();
		return siRebootNumber;
	}

	/**
	 * Returns the reboot stager time in minutes.
	 * 
	 * @return int
	 */
	public static int getRebootStaggerTime()
	{
		initialize();
		return siRebootStaggerTime;
	}

	/**
	 * Return the Receipts Directory.
	 *
	 * @return String
	 */
	public static String getReceiptsDirectory()
	{
		initialize();
		return ssReceiptsDirectory;
	}

	/**
	 * Return the RedirectSubstaId.
	 *
	 * @return int
	 */
	public static int getRedirectSubStaId()
	{
		initialize();
		return siRedirectSubstaId;
	}

	/**
	 * Return the RedirectWsId.
	 *
	 * @return int
	 */
	public static int getRedirectWsId()
	{
		initialize();
		return siRedirectWsId;
	}

	/**
	 * Return the Remote Listener Port back end service.
	 *
	 * @return int
	 */
	public static int getRemoteListenerPortBE()
	{
		initialize();
		return siRemoteListenerPortBE;
	}

	/**
	 * Return the Remote Listener Port for the gui.
	 *
	 * @return int
	 */
	public static int getRemoteListenerPortGUI()
	{
		initialize();
		return siRemoteListenerPortGUI;
	}

	/**
	 * Return the Reports Directory.
	 *
	 * @return String
	 */
	public static String getReportsDirectory()
	{
		initialize();
		return ssReportsDirectory;
	}

	/** 
	 * Return siResetFundsDB2QueryOptimizationLevel
	 * 
	 * This int is the DB2 Query Optimization we want to use vs. 
	 *   the default set for the DB2 server. 
	 */
	public static int getResetFundsDB2QueryOptimizationLevel()
	{
		initialize();
		return siResetFundsDB2QueryOptimizationLevel;
	}

	/**
	 * get the RSPS Log Dir.
	 * 
	 * @return String
	 */
	public static String getRSPSLogDir()
	{
		initialize();

		// check to see if the directory was created.
		// CountyRSPSDirectory must be initialized first.
		// This is only done on the client.
		if (!Comm.isServer())
		{
			File laFile =
				new File(getCountyRSPSDirectory() + ssRSPSLogDir);
			if (!Comm.isServer() && !laFile.exists())
			{
				laFile.mkdir();
			}
		}

		return ssRSPSLogDir;
	}

	/**
	 * Return the RSPS Log Suffix
	 * 
	 * @return String
	 * @deprecated
	 */
	public static java.lang.String getRSPSLogSuffix()
	{
		initialize();
		return ssRSPSLogSuffix;
	}

	/**
	 * get RSPS Update Suffix
	 * 
	 * @return String
	 */
	public static String getRSPSUpdateSuffix()
	{
		initialize();
		return ssRSPSUpdateSuffix;
	}

	/**
	 * Return the RTS Application Directory.
	 * 
	 * @return String
	 */
	public static String getRTSAppDirectory()
	{
		initialize();
		return ssRtsAppDirectory;
	}

	/**
	 * Return the RTS Base Directory value.
	 *
	 * @return String
	 */
	public static String getRTSDirectory()
	{
		initialize();
		return ssRtsBaseDirectory;
	}

	/**
	 * 
	 * Return SendTrans Reporting Frequency
	 * 
	 * @return int
	 */
	public static int getSendTransRptFreq()
	{
		initialize();
		return siSendTransRptFreq;
	}

	/**
	 * Return the SendCacheLogFileName.
	 *
	 * @return String
	 */
	public static String getSendCacheLogFileName()
	{
		initialize();
		return ssSendCacheLogFileName;
	}

	/**
	 * Return the Servlet Host Name.
	 * 
	 * @return String
	 */
	public static String getServletHost()
	{
		initialize();
		return ssServletHost;
	}

	/**
	 * Return the Servlet Name.
	 *
	 * @return String
	 */
	public static String getServletName()
	{
		initialize();
		return ssServletName;
	}

	/**
	 * Return the Servlet Port.
	 *
	 * @return String
	 */
	public static String getServletPort()
	{
		initialize();
		return ssServletPort;
	}

	/**
	 * Return the Set Aside Directory
	 *
	 * @return String
	 */
	public static String getSetAsideDir()
	{
		initialize();
		return ssSetAsideDir;
	}

	// defect 11116
	/**
	 * Return the SOAPRSPS service endpoint.
	 * 
	 * @return ssSoapRspsEndpoint;
	 */
	public static String getSoapRspsEndpoint()
	{
		initialize();
		return ssSoapRspsEndpoint;
	}

	/**
	 * Return the SOAPRSPS service application name.
	 * 
	 * @return ssSoapRspsApplication;
	 */
	public static String getSoapRspsApplication()
	{
		initialize();
		return ssSoapRspsApplication;
	}

	/**
	 * Return the SOAPRSPS service user id.
	 * 
	 * @return ssSoapRspsUserId;
	 */
	public static String getSoapRspsUserId()
	{
		initialize();
		return ssSoapRspsUserId;
	}

	/**
	 * Return the SOAPRSPS service password.
	 * 
	 * @return ssSoapRspsPassword;
	 */
	public static String getSoapRspsPassword()
	{
		initialize();
		return ssSoapRspsPassword;
	}
	// end defect 11116

	/**
	 * Return the SubStation Id.
	 *
	 * @return int
	 */
	public static int getSubStationId()
	{
		initialize();
		return siSubStationId;
	}
	
	/**
	 * Get the Texas Sure Test Thread Count parameter.
	 * 
	 * @return int
	 */
	public static int getTexasSureTestThreadCount()
	{
	    initialize();
	    return siTexasSureTestThreadCount;
	}

	/**
	 * Get the Texas Sure Timeout parameter.
	 * 
	 * @return int
	 */
	public static int getTexasSureTimeOut()
	{
	    initialize();
	    return siTexasSureTimeOut;
	}
	
	/**
	 * Gets the URL String for the Texas Sure URL.
	 * 
	 * @return String
	 */
	public static String getTexasSureURL()
	{
		initialize();
		return ssTexasSureURL;
	}

	/**
	 * Return the Title Directory.
	 *
	 * @return String
	 */
	public static String getTitleDirectory()
	{
		initialize();
		return ssDealerTitleDirectory;
	}

	/**
	 * Return the purge days for Purging Transaction Tables rows.
	 *
	 * @return int
	 */
	public static int getTrans()
	{
		initialize();
		return siPurgeTrans;
	}

	/**
	 * Return the Transaction Directory.
	 * 
	 * @return String
	 */
	public static String getTransactionDirectory()
	{
		initialize();

		File laFile = new File(ssTransDir);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}

		return ssTransDir;
	}

	/**
	 * Return the start date for Mandatory Vehicle Color capture
	 *
	 * @return RTSDate
	 */
	public static RTSDate getVehColorStartDate()
	{
		initialize();
		return saVehColorStartDate;
	}

	/**
	 * Return the Version Date.
	 * This value helps indicate the fix level.
	 * 
	 * @return String
	 */
	public static String getVersionDate()
	{
		initialize();
		return ssVersionDate;
	}

	/**
	 * Return the Version Number.
	 * 
	 * @return String
	 */
	public static String getVersionNo()
	{
		initialize();
		return ssVersionNo;
	}

	/**
	 * Return the voidTransLogFileName
	 *
	 * @return String
	 */
	public static String getVoidTransLogFileName()
	{
		initialize();
		return ssVoidTransLogFileName;
	}

	/**
	 * Get the Vendor Plates Office Issuance Number.
	 * 
	 * @return int
	 */
	public static int getVpOfcIssuanceNo()
	{
		initialize();
		return siVpOfcIssuanceNo;
	}

	/**
	 * Get the Vendor Plates Workstion Id.
	 * 
	 * @return int
	 */
	public static int getVpWsId()
	{
		initialize();
		return siVpWsId;
	}

	/**
	 * Return the Work Station Id.
	 *
	 * @return int
	 */
	public static int getWorkStationId()
	{
		initialize();
		return siWorkStationId;
	}

	/** 
	 * Return siPurgeWSStatusLog
	 */
	public static int getPurgeWSStatusLog()
	{
		initialize();
		return siPurgeWSStatusLog;
	}
	
	/**
	 * Return the WebAgent Port.
	 *
	 * @return String
	 * per defect 10797 - correct the WebAgent URL
	 */
	public static String getWebAgentPort()
	{
		initialize();
		return ssWebAgentPort;
	}

	/**
	 * Return the WebAgent Protocol.
	 *
	 * @return String
	 * per defect 10797 - correct the WebAgent URL
	 */
	public static String getWebAgentProtocol()
	{
		initialize();
		return ssWebAgentProtocol;
	}

	/**
	 * Return the WebAgent Host.
	 *
	 * @return String
	 * per defect 10797 - correct the WebAgent URL
	 */
	public static String getWebAgentHost()
	{
		initialize();
		return ssWebAgentHost;
	}
	
	/**
	 * Return the WebAgent Path.
	 * @return String
	 */
	public static String getWebAgentPath()
	{
		initialize();
		return ssWebAgentPath;
	}	

	/**
	 * Load all the System Properties if they have not already been 
	 * loaded.
	 */
	public static void initialize()
	{
		try
		{
			if (!sbInitialized)
			{
				if (com
					.txdot
					.isd
					.rts
					.services
					.communication
					.Comm
					.isServer())
				{
					//////////////////////////////////////////////////
					// Server config
					initializeServerCfg();
				}
				else
				{
					/////////////////////////////////////////////
					// Client config

					/////////////////////////////////////////////
					// Dynamic (RTSCLD)
					initializeClientSideDynamic();

					////////////////////////////////////////////            
					// Static
					initializeClientSideStatic();

					// End Client
					/////////////////////////////////////////////////
				}

				/////////////////////////////////////////////////
				// Version config - Common to Server and Client

				initializeVersion();
				// End Version
				/////////////////////////////////////////////////////

				// Properties have now been initialized.
				sbInitialized = true;
			}
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}
		catch (MissingResourceException aeMissingResourceException)
		{
			System.err.println(MRE_MSG);
			aeMissingResourceException.printStackTrace();
		}
	}



	/**
	 * Initialize from CLD.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void initializeClientSideDynamic() throws FileNotFoundException, IOException
	{
	    FileInputStream laFileDynamicClient =
	    	new FileInputStream(
	    		CLIENT_DYNAMIC_CONFIG
	    			+ "."
	    			+ FILE_EXTENSION);
	    // found that when the client is launched as a 
	    // service, this does not work.
	    // removing for now.
	    ClassLoader laCL =
	    	SystemProperty.class.getClassLoader();
	    if (laCL == null)
	    {
	    	System.out.println("Class Loader is null!!");
	    }
	    
	    Properties laDynamicProp = new Properties();
	    laDynamicProp.load(laFileDynamicClient);

	    // convert setting properties to use setter methods
	    // trim all integers and handle numberFormatExceptions
	    // report all null properties.
	    // Log level information has to be set first to
	    // avoid null.log
	    setProdStatus(
	    	laDynamicProp.getProperty("ProdStatus"));
	    setMaxLogFileSize(
	    	laDynamicProp.getProperty("MaxLogFileSize"));
	    setLogTraceLevel(
	    	laDynamicProp.getProperty("TraceLevel"));
	    setLogFileDir(laDynamicProp.getProperty("LogDir"));
	    // defect 11318
	    setGUILogFileName(laDynamicProp.getProperty("GUILogFileName"));
	    // end defect 11318
	    // defect 11407
		setGUISendCacheCommRetryAttempts(laDynamicProp
				.getProperty("GuiSendCacheCommRetryAttempts"));
		// end defect 11407
	    ssLogFileName =
	    	laDynamicProp.getProperty("LogFileName");
	    ssLogFileExt =
	    	laDynamicProp.getProperty("LogFileExt");
	    ssBatchLogFileName =
	    	laDynamicProp.getProperty("BatchLogFileName");
	    ssBatchErrorFileName =
	    	laDynamicProp.getProperty("BatchErrorFileName");
	    ssSendCacheLogFileName =
	    	laDynamicProp.getProperty(
	    		"SendCacheLogFileName");
	    setCumRcptLogFileName(
	    	laDynamicProp.getProperty(
	    		"CumRcptLogFileName"));

	    // get Show Delinquent Days AbstractProperty
	    setShowDelqntDays(
	    	laDynamicProp.getProperty("ShowDelqntDays"));
	    
	    setVoidTransLogFileName(
	    	laDynamicProp.getProperty(
	    		"VoidTransLogFileName"));
	    setIsCommServer(
	    	laDynamicProp.getProperty("IsCommServer"));

	    setOfficeIssuanceNo(
	    	laDynamicProp.getProperty("OfficeIssuanceNo"));
	    setSubStationId(
	    	laDynamicProp.getProperty("SubStationId"));
	    setWorkStationId(
	    	laDynamicProp.getProperty("WorkStationId"));
	    setPrintImmediateIndi(
	    	laDynamicProp.getProperty("PrintImmediate"));
	    ssBarcodePort =
	    	laDynamicProp.getProperty("BarcodePort");
	    ssCashdrawerPort =
	    	laDynamicProp.getProperty("CashdrawerPort");
	    setCommServerBatchDelta(
	    	laDynamicProp.getProperty(
	    		"CommServerBatchDelta"));
	    // added value to cld to to handle print status only
	    setPrintStatus(
	    	laDynamicProp.getProperty("PrintStatus"));

	    setHelpDeskFlag(
	    	laDynamicProp.getProperty("HelpDeskFlag"));
	    // convert these to setters
	    setConnectTimeOutServDown(
	    	laDynamicProp.getProperty(
	    		"ConnectTimeOutServDown"));
	    setConnectTimeOutServUp(
	    	laDynamicProp.getProperty(
	    		"ConnectTimeOutServUp"));

	    // set up redirect Print Parameters
	    setRedirectSubstaId(
	    	laDynamicProp.getProperty("RedirectSubstaId"));
	    setRedirectWsId(
	    	laDynamicProp.getProperty("RedirectWsId"));

	    // Added DefaultBrowserPath to the Static cfg file 
	    // so that it can be changed easier.
	    setDefaultBrowserPath(
	    	laDynamicProp.getProperty(
	    		"DefaultBrowserPath"));
	}
	
	private static void initializeClientSideStatic() throws FileNotFoundException, IOException
	{
		FileInputStream laFileStaticClient =
			new FileInputStream(
				CLIENT_STATIC_CONFIG
					+ "."
					+ FILE_EXTENSION);
	
		Properties laStaticProp = new Properties();
		laStaticProp.load(laFileStaticClient);

		// Setup a base directory for RTS
		setRTSDirectory(
			laStaticProp.getProperty("RTSDirectory"));
		// setup RTSAPPL directory
		setRTSAppDirectory(
			laStaticProp.getProperty("RTSAppDirectory"));
		setReportsDirectory(
			laStaticProp.getProperty("ReportsDirectory"));
		setReceiptsDirectory(
			laStaticProp.getProperty("ReceiptsDirectory"));
		// defect 8900
		// Added for Exempts export
		setExportsDirectory(
			laStaticProp.getProperty("ExportsDirectory"));
		// end defect 8900
		setTitleDirectory(
			laStaticProp.getProperty(
				"DealerTitleDirectory"));
		ssTransDir =
			laStaticProp.getProperty("TransactionDir");
		ssServletHost =
			laStaticProp.getProperty("ServletHost");
		ssServletName =
			laStaticProp.getProperty("ServletName");
		ssServletPort =
			laStaticProp.getProperty("ServletPort");
		ssImagesDir =
			laStaticProp.getProperty("ImagesDirectory");
		ssImageType = laStaticProp.getProperty("ImageType");
		ssCacheDirectory =
			laStaticProp.getProperty("CacheDirectory");
		ssCommServerAppDirectory =
			laStaticProp.getProperty(
				"CommServerAppDirectory");
		ssCommServerName =
			laStaticProp.getProperty("CommServerName");
		ssFtpUser = laStaticProp.getProperty("FtpUser");
		ssFtpPassword =
			laStaticProp.getProperty("FtpPassword");

		// defect 11073
		setRemoteListenerPortBE(
			laStaticProp.getProperty("RemoteListenerPortBE"));
		setRemoteListenerPortGUI(
				laStaticProp.getProperty("RemoteListenerPortGUI"));
		// end defect 11073
		setFontName(laStaticProp.getProperty("FontName"));
		setFontSize(laStaticProp.getProperty("FontSize"));
		setHelpDir(laStaticProp.getProperty("HelpDir"));
		setSetAsideDir(
			laStaticProp.getProperty("SetAsideDirectory"));
		
		setPTOStartDate(
			laStaticProp.getProperty("PTOStartDate"));
		setBuyerTagStartDate(
			laStaticProp.getProperty("BuyerTagStartDate"));
		
		setDMVStartDate(
			laStaticProp.getProperty("DMVStartDate"));
		
		setFeeSimplifyStartDate(
			laStaticProp.getProperty(
				"FeeSimplifyStartDate"));
		
		setVehColorStartDate(
			laStaticProp.getProperty("VehColorStartDate"));
		
		setMFPrmtStartDate(
			laStaticProp.getProperty("MFPrmtStartDate"));
		
		setDlrInvalidRegStartDate(
			laStaticProp.getProperty("DlrInvalidRegStartDate"));
		setNoDefaultTtlTransDtStartDate(
			laStaticProp.getProperty("NoDefaultTtlTransDtStartDate"));
		
		// defect 10900
		setMaxFraudReportDays(laStaticProp.getProperty("MaxFraudReportDays"));
		// end defect 10900
		
		// Set up the AD OU for development
		setAdOu(laStaticProp.getProperty("AdOu"));
		ssPrintWaitTime =
			laStaticProp.getProperty("PrintWaitTime");
		// set the RSPS Properties for RSPS Update
		setCountyRSPSDirectory(
			laStaticProp.getProperty(
				"CountyRSPSDirectory"));
		setRSPSLogDir(
			laStaticProp.getProperty("RSPSLogsDir"));
		setFlashDriveSourceDirectory(
			laStaticProp.getProperty(
				"FlashDriveSourceDirectory"));
		setRSPSValidSuffix(
			laStaticProp.getProperty("RSPSValidSuffix"));
		setRSPSUpdateSuffix(
			laStaticProp.getProperty("RSPSUpdateSuffix"));

		setCommunicationsMode(
			laStaticProp.getProperty("HTTPMode"));

		// Added to stagger the reboots
		setRebootNumber(
			laStaticProp.getProperty("RebootNumber"));
		setRebootStaggerTime(
			laStaticProp.getProperty("RebootStaggerTime"));
		setFilesToPrune(
			laStaticProp.getProperty("LogFilesToPrune"));
		
		setMailDir(laStaticProp.getProperty("MailDir"));
		setMailGateway(
			laStaticProp.getProperty("MailGateway"));
		
		// set SendTrans testing
		setSendTransFileLoc(
			laStaticProp.getProperty("SendTransFileLoc"));
		setBuildSendTransReviseDate(
			laStaticProp.getProperty(
				"BuildSendTransReviseDate"));
		
		//	Save FundsUpdateFileLoc
		setFundsUpdateFileLoc(
			laStaticProp.getProperty("FundsUpdateFileLoc"));
		setFundsUpdateReviseDate(
			laStaticProp.getProperty(
				"FundsUpdateReviseDate"));
		
		setMyPlatesURL(
			laStaticProp.getProperty("MyPlatesURL"));
		
		//defect 10797
		setWebAgentPort(
			laStaticProp.getProperty("WebAgentPort"));

		setWebAgentProtocol(
			laStaticProp.getProperty("WebAgentProtocol"));

		setWebAgentHost(
			laStaticProp.getProperty("WebAgentHost"));
		//end defect 10797
		
		//defect 11174
		setWebAgentPath(
				laStaticProp.getProperty("WebAgentPath"));
		//end defect 11174

		setTexasSureTestThreadCount(
				laStaticProp.getProperty("TexasSureTestThreadCount"));
		
		// defect 11050 
		setMaxAddDsabldPlcrds(
				laStaticProp.getProperty("MaxAddDsabldPlcrds"));
		// end defect 11050
		
		// defect 10827 
		setDTARejectSurvivorDate(
				laStaticProp.getProperty("DTARejectSurvivorDate"));
		// end defect 10827 
	}

	/**
	 * Initialize Server Side.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	private static void initializeServerCfg() throws FileNotFoundException, IOException, UnknownHostException
	{
	    // initialize Log4j environment
	    initLog4jServerSide();
	    
	    // use class loader instead
	    //FileInputStream laFileServer =
	    //	new FileInputStream(
	    //		SERVER_CONFIG + "." + FILE_EXTENSION);
	    InputStream laFileServer = null;

	    // try loading as a resource first
	    laFileServer =
	    	SystemProperty
	    		.class
	    		.getClassLoader()
	    		.getResourceAsStream(
	    		SERVER_CONFIG + "." + FILE_EXTENSION);

	    // if not found, see if we can load from user.dir		
	    if (laFileServer == null)
	    {
	    	System.out.println(
	    		"Loading server.cfg from user.dir.");
	    	laFileServer =
	    		new FileInputStream(
	    			SERVER_CONFIG + "." + FILE_EXTENSION);
	    }

	    Properties laServerPrp = new Properties();
	    laServerPrp.load(laFileServer);

	    setMaxLogFileSize(
	    	laServerPrp.getProperty("MaxLogFileSize"));
	    setLogTraceLevel(
	    	laServerPrp.getProperty("TraceLevel"));

	    ssLogFileDir = laServerPrp.getProperty("LogDir");
	    ssLogFileName =
	    	laServerPrp.getProperty("LogFileName");
	    ssLogFileExt =
	    	laServerPrp.getProperty("LogFileExt");
	    ssBatchLogFileName =
	    	laServerPrp.getProperty("BatchLogFileName");
	    ssBatchErrorFileName =
	    	laServerPrp.getProperty("BatchErrorFileName");
	    ssSendCacheLogFileName =
	    	laServerPrp.getProperty("SendCacheLogFileName");

	    ssMFJGateway =
	    	laServerPrp.getProperty("MFJGateway");
	    setMFJGatewayPort(
	    	laServerPrp.getProperty("MFJGatewayPort"));
	    ssMFDBName = laServerPrp.getProperty("MFDBName");
	    ssMFTimeout = laServerPrp.getProperty("MFTimeout");
	    
	    ssMFInterfaceVersionCode =
	    	laServerPrp.getProperty(
	    		"MFInterfaceVersionCode");
	    
	    ssDBUser = laServerPrp.getProperty("DBUser");
	    ssDBPassword =
	    	laServerPrp.getProperty("DBPassword");

	    setDBPasswordBatch(
	    	laServerPrp.getProperty("DBPasswordBatch"));
	    setDBUserBatch(
	    	laServerPrp.getProperty("DBUserBatch"));

	    setDBPasswordSendTrans(
	    	laServerPrp.getProperty("DBPasswordSendTrans"));
	    setDBUserSendTrans(
	    	laServerPrp.getProperty("DBUserSendTrans"));

	    setMQHostName(
	    	java
	    		.net
	    		.InetAddress
	    		.getLocalHost()
	    		.getHostName());

	    setMQChannelName(
	    	laServerPrp.getProperty("MQChannelName"));

	    setMQQueueManager(
	    	laServerPrp.getProperty("MQQueueManager"));

	    setMQQueueName(
	    	laServerPrp.getProperty("MQQueueName"));

	    setMQBackupQueueName(
	    	laServerPrp.getProperty("MQBackupQueueName"));

	    setMQPort(laServerPrp.getProperty("MQPort"));

	    ssDatasource =
	    	laServerPrp.getProperty("Datasource");

	    setDBPasswordBatch(
	    	laServerPrp.getProperty("DBPasswordBatch"));
	    setDBUserBatch(
	    	laServerPrp.getProperty("DBUserBatch"));

	    setMFServer(laServerPrp.getProperty("MFServer"));
	    setMFJGate(laServerPrp.getProperty("MFJGate"));
	    setMFDebug(laServerPrp.getProperty("MFDEBUG"));

	    setMVDIExportFTPFileName(
	    	laServerPrp.getProperty(
	    		"MVDIExportFTPFileName"));
	    setMVDIExportFTPServerName(
	    	laServerPrp.getProperty(
	    		"MVDIExportFTPServerName"));
	    setMVDIExportFTPUserId(
	    	laServerPrp.getProperty("MVDIExportFTPUserId"));
	    setMVDIExportFTPPassword(
	    	laServerPrp.getProperty(
	    		"MVDIExportFTPPassword"));
	    setMVDIExportFTPDirectory(
	    	laServerPrp.getProperty(
	    		"MVDIExportFTPDirectory"));
	    setDsabldPlcrdMaxRcds(
	    	laServerPrp.getProperty(
	    		"aiDsabldPlcrdMaxRcds"));

	    // get the purge days values
	    setPurgeAdminLog(
	    	laServerPrp.getProperty("PurgeAdminLog"));
	    setPurgeAdminTables(
	    	laServerPrp.getProperty("PurgeAdminTables"));

	    // Set Purge Parameters for Disabled Placard Customer
	    setPurgeSetDelIndiDsabldPlcrdCust(
	    	laServerPrp.getProperty(
	    		"PurgeSetDelIndiDsabldPlcrdCust"));
	    setPurgeDsabldPlcrdCust(
	    	laServerPrp.getProperty(
	    		"PurgeDsabldPlcrdCust"));

	    setPurgeExmptAudit(
	    	laServerPrp.getProperty("PurgeExmptAudit"));
	    setPurgeInetAddrChng(
	    	laServerPrp.getProperty("PurgeInetAddrChng"));
	    setPurgeInetRenewal(
	    	laServerPrp.getProperty("PurgeInetRenewal"));
	    setPurgeInetSPAPPComp(
	    	laServerPrp.getProperty("PurgeInetSPAPPComp"));
	    setPurgeInetSPAPPIncomp(
	    	laServerPrp.getProperty(
	    		"PurgeInetSPAPPIncomp"));
	    setPurgeInetSummary(
	    	laServerPrp.getProperty("PurgeInetSummary"));
	    setPurgeInvHist(
	    	laServerPrp.getProperty("PurgeInvHist"));
	    setPurgeReprntStkr(
	    	laServerPrp.getProperty("PurgeReprntStkr"));
	    setPurgeSecLog(
	    	laServerPrp.getProperty("PurgeSecLog"));
	    setPurgeTrans(
	    	laServerPrp.getProperty("PurgeTrans"));

	    setPurgeInetDepositRecon(
	    	laServerPrp.getProperty(
	    		"PurgeInetDepositRecon"));
	    setPurgeInetDepositReconHstry(
	    	laServerPrp.getProperty(
	    		"PurgeInetDepositReconHstry"));

	    // set purge for Web Services Tables 
	    setPurgeWebServices(
	    	laServerPrp.getProperty("PurgeWebServices"));

	    // get Static Table Pilot indicator	
	    setStaticTablePilot(
	    	laServerPrp.getProperty("StaticTablePilot"));

	    setPurgeSpclPltTransHstry(
	    	laServerPrp.getProperty(
	    		"PurgeSpclPltTransHstry"));

	    setPurgeSpclPltRejLog(
	    	laServerPrp.getProperty("PurgeSpclPltRejLog"));

	    setPurgeViIvtrsReleaseDays(
	    	laServerPrp.getProperty(
	    		"PurgeViIvtrsReleaseDays"));

	    setPurgeETtlHstry(
	    	laServerPrp.getProperty("PurgeETtlHstry"));

	    setPurgeWSStatusLog(
	    	laServerPrp.getProperty("PurgeWSStatusLog"));
	    
	    setPurgeItrntTransDelLog(
	    	laServerPrp.getProperty(
	    		"PurgeItrntTransDelLog"));

	    // defect 10700 
	    setPurgeSpclPltPrmt(
	    	laServerPrp.getProperty("PurgeSpclPltPrmt"));
	    // end defect 10700

	    // boolean - Do we reset for Funds?  
	    setResetFundsDB2QueryOptimization(
	    	laServerPrp.getProperty(
	    		"ResetFundsDB2QueryOptimization"));

	    // int - Level to reset to  
	    setResetFundsDB2QueryOptimizationLevel(
	    	laServerPrp.getProperty(
	    		"ResetFundsDB2QueryOptimizationLevel"));
	    // end defect 10413 

	    // Added for presumptive value.
	    setBlackBookCustPrefix(
	    	laServerPrp.getProperty("BlackBookCustPrefix"));
	    setBlackBookURL(
	    	laServerPrp.getProperty("BlackBookURL"));

	    setTexasSureURL(
	    	laServerPrp.getProperty("TexasSureURL"));
	    setCheckInsurance(
	    	laServerPrp.getProperty("CheckInsurance"));
	    
	    setTexasSureHang(laServerPrp.getProperty("TexasSureHang"));
	    
	    // defect 10119
	    setTexasSureTimeOut(
	    	laServerPrp.getProperty("TexasSureTimeout"));
	    // end defect 10119

	    setVpOfcIssuanceNo(
	    	laServerPrp.getProperty("VPOfcIssuanceNo"));
	    setVpWsId(laServerPrp.getProperty("VPWsId"));
	    
	    setSendTransRptFreq(
	    	laServerPrp.getProperty("SendTransRptFreq"));
	    
	    setPurgeMFReqHstry(
	    	laServerPrp.getProperty("PurgeMFReqHstry"));
	    setDB2LogMFReqCd(
	    	laServerPrp.getProperty("DB2LogMFReqCd"));
	    setVSAMLogMFErr(
	    	laServerPrp.getProperty("VSAMLogMFErr"));
	    
	    setEReminderEmail(
	    	laServerPrp.getProperty("EReminderEmail"));
	    
	    // defect 10670
	    seteDirServer(
	    	laServerPrp.getProperty("eDirServer"));
	    // end defect 10670
	    // defect 10670
	    setEDirUserId(
	    	laServerPrp.getProperty("eDirUserId"));
	    setEDirPassword(
	    	laServerPrp.getProperty("eDirPassword"));
	    
	    setActiveDirPassword(
	    	laServerPrp.getProperty("ActiveDirPassword"));
	    setActiveDirServer(
	    	laServerPrp.getProperty("ActiveDirServer"));
	    setActiveDirUserId(
	    	laServerPrp.getProperty("ActiveDirUserId"));
	    // end defect 10670

	    // defect 11116
	    setSoapRspsEndpoint(
	    	laServerPrp.getProperty("soaprspsEndpoint"));
	    setSoapRspsApplication(
	    	laServerPrp.getProperty("soaprspsApplication"));
	    setSoapRspsUserId(
	    	laServerPrp.getProperty("soaprspsUserId"));
	    setSoapRspsPassword(
	    	laServerPrp.getProperty("soaprspsPassword"));
	    // end defect 11116
}
	
	/**
	 * Initialize Version information.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void initializeVersion() throws FileNotFoundException, IOException
	{
	    Properties laVersionProp = new Properties();
	    if (Comm.isServer())
	    {
	    	InputStream laFileVersion = null;
	    	laFileVersion =
	    		SystemProperty
	    			.class
	    			.getClassLoader()
	    			.getResourceAsStream(
	    			VERSION_CONFIG + "." + FILE_EXTENSION);

	    	if (laFileVersion == null)
	    	{
	    		System.out.println(
	    			"Loading version from user.dir.");
	    		laFileVersion =
	    			new FileInputStream(
	    				VERSION_CONFIG + "." + FILE_EXTENSION);
	    	}
	    	laVersionProp.load(laFileVersion);
	    }
	    else
	    {
	    	FileInputStream laFileVersion =
	    		new FileInputStream(
	    			VERSION_CONFIG + "." + FILE_EXTENSION);
	    	laVersionProp.load(laFileVersion);
	    }

	    ssVersionNo = laVersionProp.getProperty("VersionNo");
	    ssVersionDate =
	    	laVersionProp.getProperty("VersionDate");
	    setMainFrameVersion(
	    	laVersionProp.getProperty("MainFrameVersion"));
	    setCacheVersionNo(
	    	laVersionProp.getProperty("CacheVersionNo"));
	    setJavaVersion(
	    	laVersionProp.getProperty("JavaVersion"));
	}



	/**
	 * Initializes the Log4j environment for Server Side.
	 */
	private static void initLog4jServerSide()
	{
		InputStream laLog4jCfgIS = null;

		try
		{
			// open the configuration file
			laLog4jCfgIS =
				SystemProperty
					.class
					.getClassLoader()
					.getResourceAsStream(
					RTS_LOG4J_PROPERTY_FILE_NAME);

			// make sure we found the file
			// if not load the alternative way.
			if (laLog4jCfgIS == null)
			{
				System.out.println(
					"Loading "
						+ RTS_LOG4J_PROPERTY_FILE_NAME
						+ " from user.dir.");
				laLog4jCfgIS =
					new FileInputStream(RTS_LOG4J_PROPERTY_FILE_NAME);
			}

			// only attempt to load the properties if file was found
			if (laLog4jCfgIS != null)
			{
				// load the Log4jWatch property
				Properties laLog4jPrp = new Properties();
				laLog4jPrp.load(laLog4jCfgIS);
				setLog4jWatch(laLog4jPrp.getProperty("log4j_watch"));
				setLog4jWatchTime(
					laLog4jPrp.getProperty("log4j_watch_time"));

				laLog4jCfgIS.close();
			}
			else
			{
				setLog4jWatch(false);
				System.out.println(
					"Could not load "
						+ RTS_LOG4J_PROPERTY_FILE_NAME
						+ " for watch properties.");
			}

			// locate file for configurator
			File laLog4jConfigFile;

			URL laFileUrl =
				SystemProperty.class.getClassLoader().getResource(
					RTS_LOG4J_PROPERTY_FILE_NAME);

			if (laFileUrl != null)
			{
				laLog4jConfigFile =
					new File(
						URLDecoder.decode(
							laFileUrl.getPath(),
							"UTF-8").substring(
							1));

				// Configure Log4j
				if (isLog4jWatch())
				{
					if (getLog4jWatchTime() > 0)
					{
						// Set to refresh on the specified time cycle.
						PropertyConfigurator.configureAndWatch(
							laLog4jConfigFile.getAbsolutePath(),
							getLog4jWatchTime());
					}
					else
					{
						// Refresh at the defualt time cycle
						PropertyConfigurator.configureAndWatch(
							laLog4jConfigFile.getAbsolutePath());
					}
				}
				else
				{
					// Set and forget.  No refresh of log4j properties.
					PropertyConfigurator.configure(
						laLog4jConfigFile.getAbsolutePath());
				}

			}

			// open the System AbstractProperty Logger
			saLog4jSystemPropLog = Logger.getLogger("SystemProperty");

			if (saLog4jSystemPropLog == null)
			{
				saLog4jSystemPropLog = Logger.getRootLogger();
				saLog4jSystemPropLog.info(
					"Config entry for SystemProperty not found");
			}

			// Log that we are open
			saLog4jSystemPropLog.info("Log4j initialized.");
			saLog4jSystemPropLog.debug(
				"Running in Watch Mode " + isLog4jWatch());
		}
		catch (Exception aeEx)
		{
			System.out.println(
				"Could not initialize Log4j for Server Side!!");
			aeEx.printStackTrace();
		}
		catch (Throwable aeThrow)
		{
			System.out.println("Got a throwable!!");
			aeThrow.printStackTrace();
		}
	}

	/**
	 * Returns the value indicating if we should call the TexasSure 
	 * server or not.
	 * 
	 * @return boolean
	 */
	public static boolean isCheckInsurance()
	{
		initialize();
		return sbCheckInsurance;
	}
	
	/**
	 * Returns the Code Server boolean.
	 * If true, this is a code server.
	 *
	 * @return boolean
	 */
	public static boolean isCommServer()
	{
		initialize();
		return sbCommServer;
	}
	/**
	 * Returns the County boolean.
	 * If true, this is at a County location.
	 * 
	 * @return boolean
	 */
	public static boolean isCounty()
	{
		initialize();
		return sbCounty;
	}



	/**
	 * Returns true if Development Status 
	 *
	 * @return boolean
	 */
	public static boolean isDevStatus()
	{
		return siProdStatus == APP_DEV_STATUS;
	}

	/**
	 * Returns the HQ boolean.
	 * If true, this is at a HQ location.
	 * 
	 * @return boolean
	 */
	public static boolean isHQ()
	{
		initialize();
		return sbHQ;
	}

	/**
	 * Returns boolean indicating if we are running normal HTTP mode 
	 * or not.
	 * 
	 * <p>
	 * <ul>
	 * <li>true - Running in Normal HTTP mode.
	 * <li>false - Running in HTTPS mode for SSL.
	 * <eul>
	 * 
	 * @return boolean
	 */
	public static boolean isHTTPMode()
	{
		boolean lbHttpMode = true;

		if (getCommunicationsMode().equalsIgnoreCase(HTTPS_JKS_MODE))
		{
			lbHttpMode = false;
		}
		return lbHttpMode;
	}

	/**
	 * Returns true, if the JavaVersion is right.
	 * Returns false, if the JavaVersion is wrong.
	 * Write to log if version is wrong.
	 *
	 * @return boolean
	 */
	public static boolean isJavaVersionCorrect()
	{
		boolean lbCorrect = true;
		if (SystemProperty.getJavaVersion() == null
			|| !SystemProperty.getJavaVersion().equalsIgnoreCase(
				System.getProperty(JAVAVERSIONSTRING)))
		{
			Log.write(
				Log.START_END,
				new RTSException(),
				JAVAVERSIONNOTMATCHERR
					+ SystemProperty.getJavaVersion()
					+ " "
					+ System.getProperty(JAVAVERSIONSTRING));
			lbCorrect = false;
		}
		return lbCorrect;
	}

	/**
	 * Returns boolean showing if we are watching for Log4j parameter
	 * changes.
	 * 
	 * @return boolean
	 */
	public static boolean isLog4jWatch()
	{
		return sbLog4jWatch;
	}

	/**
	 * Returns the Region boolean.
	 * If true, this is at a Region location.
	 * 
	 * @return boolean
	 */
	public static boolean isRegion()
	{
		initialize();
		return sbRegion;
	}

	/** 
	 * Return sbResetFundsDB2QueryOptimization
	 * 
	 * Returns boolean to denote whether we reset the optimization level
	 *  in Funds (for Closeout) 
	 */
	public static boolean isResetFundsDB2QueryOptimization()
	{
		initialize();
		return sbResetFundsDB2QueryOptimization;
	}

	/**
	 * Retruns if filename that is passed contains a valid suffix.
	 * 
	 * @param asFileName String
	 * @return boolean
	 */
	public static boolean isRSPSValidSuffix(String asFileName)
	{
		initialize();
		if (asFileName != null && asFileName.length() > 0)
		{
			for (int i = 0; i < svRSPSValidSuffix.size(); i++)
			{
				// If the filename contains an graph of the vector
				// item
				if (asFileName
					.toUpperCase()
					.indexOf(
						String
							.valueOf(svRSPSValidSuffix.get(i))
							.toUpperCase())
					> 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the ShowDelqntDays boolean 
	 * If true, then show
	 * 
	 * return boolean   
	 */
	public static boolean isShowDelqntPnlty()
	{
		initialize();
		return sbShowDelqntDays;
	}

	/** Returns the Static Table is Pilot
	 * If true, selection will be from PILOT.RTS_TABLENAME
	 * vs. RTS.RTS_TABLENAME
	 * 
	 * @return boolean
	 */
	public static boolean isStaticTablePilot()
	{
		initialize();
		return sbStaticTablePilot;
	}

	/** 
	 * Return boolean denote if is Client Server
	 * 
	 * @return boolean 
	 */
	public static boolean isClientServer()
	{
		initialize();
		return sbClientServer;
	}

	/** 
	 * Is DB2 Log MF Req 
	 * 
	 * @return boolean 
	 */
	public static boolean isDB2LogMFReq()
	{
		initialize();
		return getDB2LogMFReqCd() > DB2_LOGGING_NONE;
	}

	/**
	 * Returns true if Logging MF Errors to VSAM  
	 *
	 * @return boolean
	 */
	public static boolean isVSAMLogMFErr()
	{
		initialize();
		return sbVSAMLogMFErr;
	}

	/**
	 * Returns the Workstation boolean.
	 * If true, this is a workstation.
	 * 
	 * @return boolean
	 */
	public static boolean isWorkstation()
	{
		initialize();
		return !sbClientServer;
	}

	/**
	 * Used to run stand alone testing of Properties.
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		System.out.println(SystemProperty.getServletHost());
		System.out.println(SystemProperty.getServletName());
		System.out.println(SystemProperty.getServletPort());
		System.out.println(SystemProperty.getVersionDate());
	}

	// defect 10670
	/**
	 * Set the password for the proxy user id used for Active Directory 
	 * access.
	 * 
	 * @param asActiveDirPassword
	 */
	private static void setActiveDirPassword(String asActiveDirPassword)
	{
		if (asActiveDirPassword == null)
		{
			ssActiveDirPassword = DEFAULT_ACTV_DIR_PASSWORD;
		}
		else
		{
			ssActiveDirPassword = asActiveDirPassword;
		}
	}

	/**
	 * Set the server used for Active Directory access.
	 * 
	 * @param asActiveDirServer
	 */
	private static void setActiveDirServer(String asActiveDirServer)
	{
		if (asActiveDirServer == null)
		{
			ssActiveDirServer = DEFAULT_ACTV_DIR_SERVER;
		}
		else
		{
			ssActiveDirServer = asActiveDirServer;
		}
	}

	/**
	 * Set the proxy user id used for Active Directory access.
	 * 
	 * @param asActiveDirUserId
	 */
	private static void setActiveDirUserId(String asActiveDirUserId)
	{
		if (asActiveDirUserId == null)
		{
			ssActiveDirUserId = DEFAULT_ACTV_DIR_USER_ID;
		}
		else
		{
			ssActiveDirUserId = asActiveDirUserId;
		}
	}
	// end defect 10670

	/**
	 * Set value for AdOu.
	 * 
	 * <p>If this value is set, the application is not running in 
	 * normal production mode for Local Options Employee Security.
	 * 
	 * @param asAdOu - String
	 */
	private static void setAdOu(String asAdOu)
	{
		if (asAdOu != null)
		{
			ssAdOu = asAdOu.trim();
		}
		else
		{
			ssAdOu = null;
		}
	}

	/**
	 * Sets the Black Book Customer Prefix used by Presumptive AbstractValue.
	 * 
	 * 	This is the value that is sent to BlackBook to log who made the 
	 * 	request.  The format of this value is Prefix + -Office.
	 * 
	 * 	If this value is not present in the server.cfg we will default
	 * 	this value.
	 * 
	 * @param asBlackBookCustPrefix String
	 */
	private static void setBlackBookCustPrefix(String asBlackBookCustPrefix)
	{
		if (asBlackBookCustPrefix != null)
		{
			ssBlackBookCustPrefix = asBlackBookCustPrefix;
		}
		else
		{
			ssBlackBookCustPrefix = DEFAULT_BLACKBOOK_CUST_PREFIX;
		}
	}

	/**
	 * Sets the url used to retrieve the Presumptive AbstractValue from
	 * BlackBook.
	 * 
	 * 	If there is not a value then we will default it to an empty
	 * 	String.
	 * 
	 * @param asBlackBookURL String
	 */
	private static void setBlackBookURL(String asBlackBookURL)
	{
		if (asBlackBookURL != null)
		{
			ssBlackBookURL = asBlackBookURL;
		}
		else
		{
			ssBlackBookURL = CommonConstant.STR_SPACE_EMPTY;
		}
	}

	/**
	 * Set the BuyerTag Start Date.
	 * If Buyer Tag Start Date is not in RTSCLS.CFG, 
	 * use default 20080101
	 * 
	 * @param asBuyerTagStartDate - String
	 */
	private static void setBuyerTagStartDate(String asBuyerTagStartDate)
	{
		if (asBuyerTagStartDate != null
			&& asBuyerTagStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saBuyerTagStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(asBuyerTagStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_BUYER_TAG_START_DATE_MSG);
				saBuyerTagStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(DEFAULT_BUYER_TAG_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding("Buyer Tag Start: ", 17, " ")
					+ saBuyerTagStartDate.getYYYYMMDDDate());
		}
	}

	/**
	 * setCacheVersionNo
	 *
	 * @param asNewCacheVersionNo - String
	 */
	private static void setCacheVersionNo(String asNewCacheVersionNo)
	{
		try
		{
			siCacheVersionNo =
				Integer.parseInt(asNewCacheVersionNo.trim());
		}
		catch (Throwable aeThowable)
		{
			siCacheVersionNo = DEFAULTCACHEVERSIONNO;
			System.out.println(DEFAULT_CACHE_VERSION_MSG);
		}
	}

	/**
	 * Set the CheckInsurance boolean.
	 * 
	 * @param abCheckInsurance
	 */
	private static void setCheckInsurance(boolean abCheckInsurance)
	{
		sbCheckInsurance = abCheckInsurance;
	}
	
	/**
	 * Set the CheckInsurance boolean.
	 * 
	 * @param abCheckInsurance
	 */
	private static void setCheckInsurance(String asCheckInsurance)
	{
		if (asCheckInsurance != null
			&& asCheckInsurance.trim().equalsIgnoreCase("TRUE"))
		{
			setCheckInsurance(true);
		}
		else
		{
			setCheckInsurance(false);
		}
	}
	
	/**
	 * Set CommServerBatchDelta.
	 *
	 * @param asCommServerBatchDelta - String
	 */
	private static void setCommServerBatchDelta(String asCommServerBatchDelta)
	{
		try
		{
			siCommServerBatchDelta =
				Integer.parseInt(asCommServerBatchDelta.trim());
		}
		catch (Throwable aeThrowable)
		{
			siCommServerBatchDelta = DEFAULTCOMMSERVERBATCHDELTA;
		}
	}

	/**
	 * Set the Communuications Mode
	 * 
	 * @param asNewMode - String
	 */
	private static void setCommunicationsMode(String asNewMode)
	{
		if (asNewMode != null)
		{
			ssCommunicationsMode = asNewMode;
		}
		else
		{
			ssCommunicationsMode = HTTP_MODE;
		}
	}

	/**
	 * Set the Connect TimeOut for Server Down
	 * 
	 * @param asConnectTimeOutServDown - String
	 */
	private static void setConnectTimeOutServDown(String asConnectTimeOutServDown)
	{
		if (asConnectTimeOutServDown == null)
		{
			siConnectTimeOutServDown =
				DEFAULT_CONNECT_TIMEOUT_SERVER_DOWN;
		}
		else
		{
			try
			{
				siConnectTimeOutServDown =
					Integer.parseInt(asConnectTimeOutServDown.trim());
			}
			catch (NumberFormatException aeNFE)
			{
				siConnectTimeOutServDown =
					DEFAULT_CONNECT_TIMEOUT_SERVER_DOWN;
			}
		}
	}

	/**
	 * Set the Connect Time Out for Server Up
	 * 
	 * @param asConnectTimeOutServUp - String
	 */
	private static void setConnectTimeOutServUp(String asConnectTimeOutServUp)
	{
		if (asConnectTimeOutServUp == null)
		{
			siConnectTimeOutServUp = DEFAULT_CONNECT_TIMEOUT_SERVER_UP;
		}
		else
		{
			try
			{
				siConnectTimeOutServUp =
					Integer.parseInt(asConnectTimeOutServUp.trim());
			}
			catch (NumberFormatException aeNFE)
			{
				siConnectTimeOutServUp =
					DEFAULT_CONNECT_TIMEOUT_SERVER_UP;
			}
		}
	}

	/**
	 * Set the County RSPS Directory AbstractValue
	 * 
	 * @param asRSPSDir - String
	 */
	private static void setCountyRSPSDirectory(String asCountyRSPSDirectory)
	{
		if (asCountyRSPSDirectory != null)
		{
			ssCountyRSPSDirectory = asCountyRSPSDirectory.trim();
		}
		else
		{
			ssCountyRSPSDirectory = COUNTY_RSPS_DIRECTORY;
		}

		File laFile = new File(ssCountyRSPSDirectory);

		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}
	}

	/**
	 * Set the CumRcptLogFileName. 
	 *
	 * @param asCumRcptLogFileName - String
	 */
	private static void setCumRcptLogFileName(String asCumRcptLogFileName)
	{
		if (asCumRcptLogFileName != null)
		{
			ssCumRcptLogFileName = asCumRcptLogFileName;
		}
		else
		{
			ssCumRcptLogFileName = DEFAULT_CUMLOG;
		}
	}

	/**
	 * Set the current employee.
	 * 
	 * <p>Set by outside classes to track the current employee.
	 * <br>Also used by RTSWebAppsServlet!
	 *
	 * @param asCurrentEmpId - String
	 */
	public static void setCurrentEmpId(String asCurrentEmpId)
	{
		ssCurrentEmpId = asCurrentEmpId;
	}

	/**
	 * Sets indi for Logging MF Requests to DB2
	 * 
	 * @param asDB2LogMFReqCd
	 */
	private static void setDB2LogMFReqCd(String asDB2LogMFReqCd)
	{
		if (asDB2LogMFReqCd != null)
		{
			try
			{
				siDB2LogMFReqCd =
					Integer.parseInt(asDB2LogMFReqCd.trim());
			}
			catch (NumberFormatException aeNPEx)
			{
				siDB2LogMFReqCd = DEFAULTDB2LOGMFREQCD;
				System.out.println(DEFAULT_DB2_LOG_REQ_MSG);
			}
		}
		if (asDB2LogMFReqCd == null
			|| (siDB2LogMFReqCd < DB2_LOGGING_NONE
				|| siDB2LogMFReqCd > DB2_LOGGING_ALL_MF_REQ))
		{
			siDB2LogMFReqCd = DEFAULTDB2LOGMFREQCD;
			System.out.println(DEFAULT_DB2_LOG_REQ_MSG);
		}
	}

	/**
	 * Sets the Database Password to be used when in Database Restricted
	 * Mode (Batch).
	 * 
	 * <p>Uses the normal Database Password if there is not a Batch 
	 * Password provided.
	 * 
	 * @param asDBPasswordBatch
	 */
	private static void setDBPasswordBatch(String asDBPasswordBatch)
	{
		if (asDBPasswordBatch != null)
		{
			ssDBPasswordBatch = asDBPasswordBatch;
		}
		else
		{
			ssDBPasswordBatch = ssDBPassword;
		}
	}

	/**
	 * Set the Database Password used for SendTrans.
	 * 
	 * <p>Used the normal Database Password if there is not a SendTrans
	 * Password provided.
	 * 
	 * @param asDBPasswordSendTrans
	 */
	private static void setDBPasswordSendTrans(String asDBPasswordSendTrans)
	{
		if (asDBPasswordSendTrans != null)
		{
			ssDBPasswordSendTrans = asDBPasswordSendTrans;
		}
		else
		{
			ssDBPasswordSendTrans = ssDBPassword;
		}
	}

	/**
	 * Sets the Database UserId to be used when in Database Restricted
	 * Mode (Batch).
	 * 
	 * <p>Uses the normal Database UserId if there is not a Batch UserId 
	 * provided.
	 * 
	 * @param asDBUserBatch
	 */
	private static void setDBUserBatch(String asDBUserBatch)
	{
		if (asDBUserBatch != null)
		{
			ssDBUserBatch = asDBUserBatch;
		}
		else
		{
			ssDBUserBatch = ssDBUser;
		}
	}

	/**
	 * Set the Database UserId to be used for SendTrans.
	 * 
	 * <p>Uses the normal Database UserId if there is not a SendTrans 
	 * UserId provided.
	 * 
	 * @param asDBUserSendTrans
	 */
	private static void setDBUserSendTrans(String asDBUserSendTrans)
	{
		if (asDBUserSendTrans != null)
		{
			ssDBUserSendTrans = asDBUserSendTrans;
		}
		else
		{
			ssDBUserSendTrans = ssDBUser;
		}
	}

	/**
	 * Set the Dealer Invalidate Registration.
	 * If Dealer Invalidate Registration Start Date is not in RTSCLS.CFG, 
	 * use default 20110901
	 * 
	 * @param asBuyerTagStartDate - String
	 */
	private static void setDlrInvalidRegStartDate(
		String asDlrInvalidRegStartDate)
	{
		if (asDlrInvalidRegStartDate != null
			&& asDlrInvalidRegStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saDlrInvalidRegStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							asDlrInvalidRegStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(
					DEFAULT_DEALER_INVALIDATE_REG_START_DATE_MSG);
				saDlrInvalidRegStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							DEFAULT_DEALER_INVALIDATE_REG_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding(
					"Dealer Invalidate Reg Start: ", 29, " ")
					+ saDlrInvalidRegStartDate.getYYYYMMDDDate());
		}
	}

	/**
	 * Set the DefaultBrowserPath used to display help.
	 *
	 * @param asDefaultBrowserPath String
	 */
	private static void setDefaultBrowserPath(String asDefaultBrowserPath)
	{
		if (asDefaultBrowserPath != null)
		{
			// Changed this from DefaultBrowserPath = aDefaultBrowserPath to below
			// to make adding default browser to the cfg simpler.
			ssDefaultBrowserPath = "\"" + asDefaultBrowserPath + "\"";
		}
		else
		{
			ssDefaultBrowserPath = DEFAULT_BROWSER_PATH_VALUE;
		}
	}

	/**
	 * Set the Maximum Number of Customer Records returned 
	 * for Disabled Placard
	 * 
	 * @param asDsabldPlcrdMaxRcds
	 */
	private static void setDsabldPlcrdMaxRcds(String asDsabldPlcrdMaxRcds)
	{
		if (asDsabldPlcrdMaxRcds != null)
		{
			try
			{
				setDsabldPlcrdMaxRcds(
					Integer.parseInt(asDsabldPlcrdMaxRcds));
			}
			catch (NumberFormatException aeNFEx)
			{
				setDsabldPlcrdMaxRcds(DEFAULT_DSABLD_PLCRD_MAX_RCDS);
			}
		}
		else
		{
			setDsabldPlcrdMaxRcds(DEFAULT_DSABLD_PLCRD_MAX_RCDS);
		}
	}
	
	/**
	 * Set the Maximum Number of Customer Records returned 
	 * for Disabled Placard
	 * 
	 * @param asDsabldPlcrdMaxRcds
	 */
	private static void setDTARejectSurvivorDate(String asDTARejectSurvivorDate)
	{
		if (asDTARejectSurvivorDate != null)
		{
			try
			{
				setDTARejectSurvivorDate(
					Integer.parseInt(asDTARejectSurvivorDate));
			}
			catch (NumberFormatException aeNFEx)
			{
				setDTARejectSurvivorDate(DEFAULT_DTA_REJECT_SURVIVOR_DATE);
			}
		}
		else
		{
			setDTARejectSurvivorDate(DEFAULT_DTA_REJECT_SURVIVOR_DATE);
		}
	}

	/**
	 * Set new DMV agency Start Date.
	 * If DMV Start Date is not in RTSCLS.CFG, use default
	 * 
	 * @param asDMVStartDate - String
	 */
	private static void setDMVStartDate(String asDMVStartDate)
	{
		if (asDMVStartDate != null
			&& asDMVStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saDMVStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(asDMVStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_DMV_START_DATE_MSG);
				saDMVStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(DEFAULT_DMV_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding("DMV Start: ", 17, " ")
					+ saDMVStartDate.getYYYYMMDDDate());
		}
	}

	// defect 10670
	/**
	 * Set the password for the proxy user id used for eDirectory 
	 * access.
	 * 
	 * @param asEDirPassword
	 */
	private static void setEDirPassword(String asEDirPassword)
	{
		if (asEDirPassword == null)
		{
			ssEDirPassword = DEFAULT_EDIR_PASSWORD;
		}
		else
		{
			ssEDirPassword = asEDirPassword;
		}
	}

	/**
	 * Set the proxy user id used for eDirectory access.
	 * 
	 * @param asEDirUserId
	 */
	private static void setEDirUserId(String asEDirUserId)
	{
		if (asEDirUserId == null)
		{
			ssEDirUserId = DEFAULT_EDIR_USER_ID;
		}
		else
		{
			ssEDirUserId = asEDirUserId;
		}
	}
	// end defect 10670

	/**
	 * Set new Fee Simplication rules Start Date.
	 * If Fee Simplication Start Date is not in rtscls.cfg, use default
	 * 
	 * @param asFeeSimplifyStartDate - String
	 */
	private static void setFeeSimplifyStartDate(String asFeeSimplifyStartDate)
	{
		if (asFeeSimplifyStartDate != null
			&& asFeeSimplifyStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saFeeSimplifyStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							asFeeSimplifyStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_FEE_SIMPLIFY_START_DATE_MSG);
				saFeeSimplifyStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							DEFAULT_FEE_SIMPLIFY_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding(
					"Fee Simplify Start: ",
					20,
					" ")
					+ saFeeSimplifyStartDate.getYYYYMMDDDate());
		}
	}

	/**
	 * Set PurgeETtlHstry days.
	 *
	 * @param asNewPurgeETtlHstry - String
	 */
	private static void setPurgeETtlHstry(String asNewPurgeETtlHstry)
	{
		try
		{
			siPurgeETtlHstry =
				Integer.parseInt(asNewPurgeETtlHstry.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeETtlHstry = DEFAULTPURGEETTLHSTRY;
			System.out.println(DEFAULT_PURGE_ETTL_HSTRY_MSG);
		}
	}
	/**
	 * Set PurgeETtlHstry days. 
	 * 
	 *
	 * @parameter aiNewETtlHstry int
	 */
	public static void setPurgeETtlHstry(int aiNewETtlHstry)
	{
		siPurgeETtlHstry = aiNewETtlHstry;
	}

	/**
	 * Set the drive letter + filename of External Media in use 
	 * 
	 * @param asExternalMedia
	 */
	public static void setExternalMedia(String asExternalMedia)
	{
		ssExternalMedia = asExternalMedia;
	}

	/**
	 * Set value for Exports Directory.
	 *
	 * @param asExportDirectory - String
	 */
	private static void setExportsDirectory(String asExportDirectory)
	{
		// Make sure exportsDirectory directory is created before reference.
		// Use property to get base directory.
		if (asExportDirectory != null)
		{
			ssExportsDirectory = asExportDirectory.trim();
			File laFile = new File(ssExportsDirectory);
			if (!Comm.isServer() && !laFile.exists())
			{
				laFile.mkdir();
			}
		}
		else
		{
			ssExportsDirectory = null;
		}
	}

	/**
	 * Set the Maximum Number of Customer Records returned 
	 * for Disabled Placard
	 * 
	 * @param aiDsabldPlcrdMaxRcds
	 */
	private static void setDsabldPlcrdMaxRcds(int aiDsabldPlcrdMaxRcds)
	{
		siDsabldPlcrdMaxRcds = aiDsabldPlcrdMaxRcds;
	}
	
	/**
	 * Set the Maximum Number of Customer Records returned 
	 * for Disabled Placard
	 * 
	 * @param aiDsabldPlcrdMaxRcds
	 */
	private static void setDTARejectSurvivorDate(int aiDTARejectSurvivorDate)
	{
		siDTARejectSurvivorDate = aiDTARejectSurvivorDate;
	}

	/**
	 * Takes a list of files to be pruned that are separated by comma's
	 * and loads them into a vector.
	 * 
	 * @param asListOfFiles String
	 */
	private static void setFilesToPrune(String asListOfFiles)
	{
		if (asListOfFiles != null)
		{
			svFilesToPrune.addAll(
				Arrays.asList(
					asListOfFiles.split(CommonConstant.STR_COMMA)));
		}
	}

	/**
	 * set the FlashDriveSourceDirectory value
	 * 
	 * @param asFlashDriveSourceDirectory - String
	 */
	private static void setFlashDriveSourceDirectory(String asFlashDriveSourceDirectory)
	{
		if (asFlashDriveSourceDirectory != null)
		{
			ssFlashDriveSourceDirectory = asFlashDriveSourceDirectory;
		}
		else
		{
			ssFlashDriveSourceDirectory = FLASHDRIVESOURCEDIRECTORY;
		}
	}

	/**
	 * Set the font name.
	 * 
	 * <p>This is currently not used in the application.
	 * The frames do not reference this value.
	 * 
	 * @param asFontName - String
	 */
	private static void setFontName(String asFontName)
	{
		ssFontName = asFontName;
	}

	/**
	 * Set the font size.
	 * 
	 * <p>This is currently not used in the application.
	 * The frames do not reference this value.
	 *
	 * @param asFontSize - String
	 */
	private static void setFontSize(String asFontSize)
	{
		ssFontSize = asFontSize;
	}

	/**
	 * Set the GUI Log File Name.
	 * Defaults to "rtsapp_gui.".
	 *
	 * @param asGUILogFileName - String
	 */
	public static void setGUILogFileName(String asGUILogFileName)
	{
		if (asGUILogFileName == null)
		{
			ssGUILogFileName = new String(DEFAULT_GUI_LOG_FILENAME);
		}
		else
		{
			ssGUILogFileName = asGUILogFileName;
		}
	}

	/**
	 * Set the number of attempts for RTSMainGUI to attempt to connect to
	 * RTSMainBE.
	 * 
	 * @param aiGUISendCacheCommRetryAttempts - String
	 */
	public static void setGUISendCacheCommRetryAttempts(
			String aiGUISendCacheCommRetryAttempts)
	{
		try
		{
			siGUISendCacheCommRetryAttempts = Integer
					.parseInt(aiGUISendCacheCommRetryAttempts.trim());
		}
		catch (Throwable aeThrowable)
		{
			siGUISendCacheCommRetryAttempts = DEFAULT_GUI_SENDCACHE_COMM_RETRY_ATTEMPTS;
		}
	}

	/**
	 * Set the HelpDeskFlag.
	 * Defaults to off.
	 *
	 * @param asHelpDeskFlag - String
	 */
	private static void setHelpDeskFlag(
		java.lang.String asHelpDeskFlag)
	{
		if (asHelpDeskFlag == null)
		{
			ssHelpDeskFlag = new String(DEFAULT_HELP_DESK_FLAG);
		}
		else
		{
			ssHelpDeskFlag = asHelpDeskFlag;
		}
	}

	/**
	 * Set the Help Directory.
	 *
	 * @param aHelpDir - String
	 */
	private static void setHelpDir(String aHelpDir)
	{
		// Make sure helpDir directory is created before reference.
		if (aHelpDir != null)
		{
			ssHelpDir = aHelpDir.trim();
		}
		else
		{
			ssHelpDir = getRTSAppDirectory() + DEFAULT_UG_SUBDIR;
		}

		File laFile = new File(ssHelpDir);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}
	}

	/**
	 * Set CommServer boolean.
	 * 
	 * @param asCommServer String
	 */
	private static void setIsCommServer(java.lang.String asCommServer)
	{
		if ((asCommServer != null)
			&& (asCommServer.toLowerCase().trim().equals(YES)))
		{
			sbCommServer = true;
		}
		else
		{
			sbCommServer = false;
		}
	}

	/**
	 * Set Java Version.
	 * 
	 * @param asJavaVersion String
	 */
	private static void setJavaVersion(String asJavaVersion)
	{
		ssJavaVersion = asJavaVersion;
	}

	/**
	 * Sets the Log4j boolean
	 * 
	 * @param abLog4jWatch
	 */
	private static void setLog4jWatch(boolean abLog4jWatch)
	{
		sbLog4jWatch = abLog4jWatch;
	}

	/**
	 * Sets the Log4j boolean based on a string passed in
	 * 
	 * @param asLog4jWatch
	 */
	private static void setLog4jWatch(String asLog4jWatch)
	{
		if (asLog4jWatch != null
			&& asLog4jWatch.equalsIgnoreCase("TRUE"))
		{
			setLog4jWatch(true);
		}
		else
		{
			setLog4jWatch(false);
		}
	}

	/**
	 * Set the Log4j Watch Time Interval.
	 * 
	 * <p>Takes a string and converts it.
	 * If string is null, default to 0.
	 * 
	 * @param asLog4jWatchTime
	 */
	private static void setLog4jWatchTime(String asLog4jWatchTime)
	{
		if (asLog4jWatchTime != null)
		{
			try
			{
				siLog4jWatchTime = Integer.parseInt(asLog4jWatchTime);
			}
			catch (NumberFormatException aeNFEx)
			{
				siLog4jWatchTime = 0;
			}
		}
		else
		{
			siLog4jWatchTime = 0;
		}
	}

	/**
	 * Set the logFileDir string.
	 *
	 * @param asLogFileDir - String
	 */
	private static void setLogFileDir(String asLogFileDir)
	{
		if (asLogFileDir != null)
		{
			ssLogFileDir = asLogFileDir;
		}
		else
		{
			ssLogFileDir = DEFAULT_LOG_FILE_DIR;
			System.out.println(DEFAULT_LOG_DIR_MSG);
		}
	}

	/**
	 * Sets LogTraceLevel.
	 * 
	 * @param asLogTraceLevel - String
	 */
	private static void setLogTraceLevel(String asLogTraceLevel)
	{
		try
		{
			siLogTraceLevel = Integer.parseInt(asLogTraceLevel.trim());
		}
		catch (Throwable aeThrowable)
		{
			siLogTraceLevel = DEFAULTLOGTRACELEVEL;
			System.out.println(DEFAULT_TRACE_LEVEL_MSG);
		}
	}

	/**
	 * Sets the email directory used by the messenger service.
	 * If there is not a value in the cls for this value then use the
	 * default.
	 * 
	 * @param asMailDir String
	 */
	private static void setMailDir(String asMailDir)
	{
		if (asMailDir == null || asMailDir.length() == 0)
		{
			ssMailDir = DEFAULT_MAIL_DIR;
		}
		else
		{
			ssMailDir = asMailDir.trim();
		}
	}

	/**
	 * Sets the mail gateway address.  If this value is not there then
	 * we use the default value.
	 * 
	 * @param asMailGateway String
	 */
	private static void setMailGateway(String asMailGateway)
	{

		if (asMailGateway == null || asMailGateway.length() == 0)
		{
			ssMailGateway = DEFAULT_MAIL_GATEWAY;
		}
		else
		{
			ssMailGateway = asMailGateway.trim();
		}
	}

	/**
	 * Sets the MainFrameVersion.
	 *
	 * @param asMainFrameVersion - String
	 */
	private static void setMainFrameVersion(String asMainFrameVersion)
	{
		try
		{
			siMainFrameVersion =
				Integer.parseInt(asMainFrameVersion.trim());
		}
		catch (Throwable aeThrowable)
		{
			siMainFrameVersion = 0;
		}
	}
	/**
	 * Set MaxFraudReportDays
	 *
	 * @param asMaxFraudReportDays - String
	 */
	private static void setMaxFraudReportDays(String asMaxFraudReportDays)
	{
		try
		{
			siMaxFraudReportDays =
				Integer.parseInt(asMaxFraudReportDays.trim());
		}
		catch (Throwable aeThrowable)
		{
			siMaxFraudReportDays = DEFAULT_MAX_FRAUD_REPORT_DAYS;
			System.out.println(DEFAULT_MAX_FRAUD_REPORT_DAYS_MSG);
		}
	}

	/**
	 * Set MaxLogFileSize.
	 *
	 * @param asMaxLogFileSize - String
	 */
	private static void setMaxLogFileSize(String asMaxLogFileSize)
	{
		try
		{
			siMaxLogFileSize =
				Integer.parseInt(asMaxLogFileSize.trim());
		}
		catch (Throwable aeThrowable)
		{
			siMaxLogFileSize = DEFAULTMAXLOGFILESIZE;
			System.out.println(DEFAULT_MAXLOGFILESIZE_MSG);
		}
	}

	/**
	 * Set MF Debug.
	 * 
	 * @param aMFDebug int
	 */
	private static void setMFDebug(int aiMFDebug)
	{
		siMFDebug = aiMFDebug;
	}

	/**
	 * Get the MFDebug property and call the int setter.
	 * 
	 * @param asMFDebug - int
	 */
	private static void setMFDebug(String asMFDebug)
	{
		try
		{
			setMFDebug(Integer.parseInt(asMFDebug.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setMFDebug(DEFAULTMFDEBUG);
		}
	}

	/**
	 * Sets the MF CICS COBOL Version.
	 * This is used by MfAccessTest.
	 * 
	 * @param asNewMFInterfaceVersionCode String
	 */
	public static void setMFInterfaceVersionCode(String asNewMFInterfaceVersionCode)
	{
		ssMFInterfaceVersionCode = asNewMFInterfaceVersionCode;
	}

	/**
	 * Set MF Java Gate.
	 * 
	 * @param asMFJGate - String
	 */
	private static void setMFJGate(String asMFJGate)
	{
		if (asMFJGate != null)
		{
			ssMFJGate = asMFJGate.trim();
		}
		else
		{
			System.out.println(MFGATE_NOT_SET_MSG);
		}
	}

	/**
	 * set MFJGatewayPort.
	 * 
	 * @param aiMFJGatewayPort - int
	 */
	private static void setMFJGatewayPort(int aiMFJGatewayPort)
	{
		siMFJGatewayPort = aiMFJGatewayPort;
	}

	/**
	 * Get the MFJGatewayPort property and call the int setter.
	 * 
	 * @param aMFJGatewayPort int
	 */
	private static void setMFJGatewayPort(String asMFJGatewayPort)
	{
		try
		{
			setMFJGatewayPort(
				Integer.parseInt(asMFJGatewayPort.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setMFJGatewayPort(DEFAULTMFGATEWAYPORT);
		}
	}
	/**
	 * Set MF Permit Retrieval Start Date.
	 * If MF Permit Retrieval Start Date is not in RTSCLS.CFG, use default
	 * 
	 * @param asMFPrmtStartDate - String
	 */
	private static void setMFPrmtStartDate(String asMFPrmtStartDate)
	{
		if (asMFPrmtStartDate != null
			&& asMFPrmtStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saMFPrmtStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(asMFPrmtStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_MFPERMIT_START_DATE_MSG);
				saMFPrmtStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(DEFAULTMFPERMITSTARTDATE));
			}
		}
		else
		{
			saMFPrmtStartDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					Integer.parseInt(DEFAULTMFPERMITSTARTDATE));
		}
		System.out.println(
			UtilityMethods.addPadding("MF Permit Start: ", 17, " ")
				+ saMFPrmtStartDate.getYYYYMMDDDate());
	}

	/**
	 * Set the MF Server.
	 * 
	 * @param asNewMFServer - String
	 */
	private static void setMFServer(String asNewMFServer)
	{
		ssMFServer = asNewMFServer;
	}

	/**
	 * Method description
	 * 
	 * @param asMQBackupQueueName String
	 */
	public static void setMQBackupQueueName(String asMQBackupQueueName)
	{
		ssMQBackupQueueName = asMQBackupQueueName;
	}

	/**
	 * Sets ssMQChannelName
	 * 
	 * @param asMQChannelName string
	 */
	public static void setMQChannelName(String asMQChannelName)
	{
		ssMQChannelName = asMQChannelName;
	}

	/**
	 * Sets ssMQHostName
	 * 
	 * @param asMQHostName String
	 */
	public static void setMQHostName(String asMQHostName)
	{
		ssMQHostName = asMQHostName;
	}

	/**
	 * Sets ssMQPort
	 * 
	 * @param asMQPort String
	 */
	public static void setMQPort(String asMQPort)
	{
		ssMQPort = asMQPort;
	}

	/**
	 * Sets ssMQQueueManager
	 * 
	 * @param asMQQueueManage String
	 */
	public static void setMQQueueManager(String asMQQueueManager)
	{

		ssMQQueueManager = asMQQueueManager;

	}

	/**
	 * Set ssQueueName
	 * 
	 * @param asMQQueueName String
	 */
	public static void setMQQueueName(String asMQQueueName)
	{
		ssMQQueueName = asMQQueueName;
	}

	/** 
	 * Set ssMVDIExportFTPDirectory
	 * 
	 * @param asMVDIExportFTPDirectory
	 */
	public static void setMVDIExportFTPDirectory(String asMVDIExportFTPDirectory)
	{
		ssMVDIExportFTPDirectory = asMVDIExportFTPDirectory;

		if (ssMVDIExportFTPDirectory == null)
		{
			ssMVDIExportFTPDirectory =
				DEFAULT_MVDI_EXPORT_FTP_DIRECTORY;
		}
	}

	/** 
	 * Set ssMVDIExportFTPFileName
	 * 
	 * @param asMVDIExportFTPFileName
	 */
	public static void setMVDIExportFTPFileName(String asMVDIExportFTPFileName)
	{
		ssMVDIExportFTPFileName = asMVDIExportFTPFileName;

		if (ssMVDIExportFTPFileName == null)
		{
			ssMVDIExportFTPFileName = DEFAULT_MVDI_EXPORT_FTP_FILENAME;
		}
	}
	/** 
	 * Set ssMVDIExportFTPUserId
	 * 
	 * @param asMVDIExportFTPUserId
	 */
	public static void setMVDIExportFTPUserId(String asMVDIExportFTPUserId)
	{
		ssMVDIExportFTPUserId = asMVDIExportFTPUserId;

		if (ssMVDIExportFTPUserId == null)
		{
			ssMVDIExportFTPUserId = DEFAULT_MVDI_EXPORT_FTP_USERID;
		}
	}

	/** 
	 * Set ssMVDIExportFTPPassword
	 * 
	 * @param asMVDIExportFTPPassword
	 */
	public static void setMVDIExportFTPPassword(String asMVDIExportFTPPassword)
	{
		ssMVDIExportFTPPassword = asMVDIExportFTPPassword;

		if (ssMVDIExportFTPPassword == null)
		{
			ssMVDIExportFTPPassword = DEFAULT_MVDI_EXPORT_FTP_PASSWORD;
		}
	}

	/** 
	 * Set ssMVDIExportFTPServerName
	 * 
	 * @param asMVDIExportFTPServerName
	 */
	public static void setMVDIExportFTPServerName(String asMVDIExportFTPServerName)
	{
		ssMVDIExportFTPServerName = asMVDIExportFTPServerName;

		if (ssMVDIExportFTPServerName == null)
		{
			ssMVDIExportFTPServerName =
				DEFAULT_MVDI_EXPORT_FTP_SERVER_NAME;
		}
	}

	/**
	 * Set the No Default Title Trans Date Start Date.
	 * If No Default Title Trans Date Start Date is not in RTSCLS.CFG, 
	 * use default 20110901
	 * 
	 * @param asBuyerTagStartDate - String
	 */
	private static void setNoDefaultTtlTransDtStartDate(
		String asNoDefaultTtlTransDtStartDate)
	{
		if (asNoDefaultTtlTransDtStartDate != null
			&& asNoDefaultTtlTransDtStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saNoDefaultTtlTransDtStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							asNoDefaultTtlTransDtStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(
					DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE_MSG);
				saNoDefaultTtlTransDtStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(
							DEFAULT_NO_DEFAULT_TTLTRANS_DT_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding(
					"No Default Title Trans Dt Start: ", 33, " ")
					+ saNoDefaultTtlTransDtStartDate.getYYYYMMDDDate());
		}
	}

	/**
	 * Sets the Office Issuance Code
	 * 
	 * @param aiOfficeIssuanceCd
	 */
	public static void setOfficeIssuanceCd(int aiOfficeIssuanceCd)
	{
		siOfficeIssuanceCd = aiOfficeIssuanceCd;
		sbHQ = (siOfficeIssuanceCd == HQ_OFFICE_CD);
		sbRegion = (siOfficeIssuanceCd == REGION_OFFICE_CD);
		sbCounty = (siOfficeIssuanceCd == COUNTY_OFFICE_CD);
	}

	/**
	 * Sets the Office Issuance Number.
	 * 
	 * <p>Also used by RTSWebAppsServlet.
	 *
	 * @param aiOfficeIssuanceNo - int
	 */
	public static void setOfficeIssuanceNo(int aiOfficeIssuanceNo)
	{
		siOfficeIssuanceNo = aiOfficeIssuanceNo;
	}

	/**
	
	/**
	 * Gets the Office Issuance Number property and 
	 * calls the setter for int.
	 *
	 * @param asOfficeIssuanceNo - String
	 */
	private static void setOfficeIssuanceNo(String asOfficeIssuanceNo)
	{
		try
		{
			setOfficeIssuanceNo(
				Integer.parseInt(asOfficeIssuanceNo.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setOfficeIssuanceNo(DEFAULTOFFICEISSUANCENO);
		}
	}

	/**
	 * Sets the printImmediateIndi.
	 * 
	 * <p>This is also called from desktop.
	 *
	 * @param aPrintImmediateIndiInt int
	 */
	public static void setPrintImmediateIndi(int aPrintImmediateIndiInt)
	{
		siPrintImmediateIndi = aPrintImmediateIndiInt;
	}

	/**
	 * Gets the printImmediateIndi property and calls the int setter.
	 *
	 * @param asPrintImmediateIndi - String
	 */
	public static void setPrintImmediateIndi(String asPrintImmediateIndi)
	{
		if (asPrintImmediateIndi != null
			&& asPrintImmediateIndi.trim().equalsIgnoreCase(YES))
		{
			setPrintImmediateIndi(1);
		}
		else
		{
			// the default value is 0
			setPrintImmediateIndi(0);
		}
	}

	/**
	 * Set Print Status.
	 *
	 * @param asNewPrintStatus - String
	 */
	private static void setPrintStatus(String asNewPrintStatus)
	{
		try
		{
			siPrintStatus = Integer.parseInt(asNewPrintStatus.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPrintStatus = DEFAULTPRINTSTATUS;
		}
	}

	/**
	 * Set ProdStatus.
	 *
	 * @param asNewProdStatus String
	 */
	private static void setProdStatus(String asNewProdStatus)
	{
		try
		{
			siProdStatus = Integer.parseInt(asNewProdStatus.trim());
		}
		catch (Throwable aeThrowable)
		{
			siProdStatus = DEFAULTPRODSTATUS;
		}
	}

	/**
	 * Set the PTO Processing Start Date.
	 * If PTO Start Date is not in RTSCLS.CFG, use default 20080101
	 * 
	 * @param asPTOStartDate - String
	 */
	private static void setPTOStartDate(String asPTOStartDate)
	{
		if (asPTOStartDate != null
			&& asPTOStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saPTOStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(asPTOStartDate.trim()));

				System.out.println(saPTOStartDate.getYYYYMMDDDate());
			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_PTO_START_DATE_MSG);
				saPTOStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(DEFAULT_PTO_START_DATE));
			}
		}
	}

	/**
	 * Set PurgeAdminLog days.
	 *
	 * @param asNewPurgeAdminLog - String
	 */
	private static void setPurgeAdminLog(String asNewPurgeAdminLog)
	{
		try
		{
			siPurgeAdminLog =
				Integer.parseInt(asNewPurgeAdminLog.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeAdminLog = DEFAULTPURGEADMINLOG;
			System.out.println(DEFAULT_PURGE_ADMIN_MSG);
		}
	}

	/**
	 * Set PurgeAdminTables days.
	 *
	 * @param asNewPurgeAdminTables - String
	 */
	private static void setPurgeAdminTables(String asNewPurgeAdminTables)
	{
		try
		{
			siPurgeAdminTables =
				Integer.parseInt(asNewPurgeAdminTables.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeAdminTables = DEFAULTPURGEADMINTABLES;
			System.out.println(DEFAULT_PURGE_ADMIN_TABLES_MSG);
		}
	}

	/**
	 * Set PurgeDsabldPlcrdCust days.
	 *
	 * @param asPurgeDsabldPlcrdCust - String
	 */
	private static void setPurgeDsabldPlcrdCust(String asPurgeDsabldPlcrdCust)
	{
		try
		{
			siPurgeDsabldPlcrdCust =
				Integer.parseInt(asPurgeDsabldPlcrdCust.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeDsabldPlcrdCust = DEFAULTPURGEDSABLDPLCRDCUST;
			System.out.println(DEFAULT_PURGE_DSABLD_PLCRD_CUST_MSG);
		}
	}

	/**
	 * Set PurgeExmptAudit days.
	 *
	 * @param asPurgeExmptAuditt - String
	 */
	private static void setPurgeExmptAudit(String asPurgeExmptAudit)
	{
		try
		{
			siPurgeExmptAudit =
				Integer.parseInt(asPurgeExmptAudit.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeExmptAudit = DEFAULTPURGEEXMPTAUDIT;
			System.out.println(DEFAULT_PURGE_EXMPT_AUDIT_MSG);
		}
	}

	/**
	 * Set PurgeInetAddrChng days.
	 * 
	 * @param newPurgeInetAddrChng - String
	 */
	private static void setPurgeInetAddrChng(String asNewPurgeInetAddrChng)
	{
		try
		{
			siPurgeInetAddrChng =
				Integer.parseInt(asNewPurgeInetAddrChng.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetAddrChng = DEFAULTPURGEINETADDRCHNG;
			System.out.println(DEFAULT_PURGE_INET_ADDR_CHG_MSG);
		}
	}

	/**
		 * 
		 * Set PurgeInetDepositRecon days
		 * 
		 * @param asPurgeDsabldPlcrdCust
		 */
	private static void setPurgeInetDepositRecon(String asPurgeInetDepositRecon)
	{
		try
		{
			siPurgeInetDepositRecon =
				Integer.parseInt(asPurgeInetDepositRecon.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetDepositRecon = DEFAULTPURGEINETDEPOSITRECON;
			System.out.println(DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_MSG);
		}
	}

	/**
	 * 
	 * Set PurgeInetDepositRecon History days
	 * 
	 * @param asPurgeDsabldPlcrdCust
	 */
	private static void setPurgeInetDepositReconHstry(String asPurgeInetDepositReconHstry)
	{
		try
		{
			siPurgeInetDepositReconHstry =
				Integer.parseInt(asPurgeInetDepositReconHstry.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetDepositReconHstry =
				DEFAULTPURGEINETDEPOSITRECONHSTRY;
			System.out.println(
				DEFAULT_PURGE_ITRNT_DEPOSIT_RECON_HSTRY_MSG);
		}
	}

	/**
	 * Set PurgeInetRenewal days.
	 *
	 * @param asNewPurgeInetRenewal - String
	 */
	private static void setPurgeInetRenewal(String asNewPurgeInetRenewal)
	{
		try
		{
			siPurgeInetRenewal =
				Integer.parseInt(asNewPurgeInetRenewal.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetRenewal = DEFAULTPURGEINETRENEWAL;
			System.out.println(DEFAULT_PURGE_INET_REN_MSG);
		}
	}

	/**
	 * Set PurgeInetSPAPPIncomp days.
	 *
	 * @param asPurgeInetSPAPPIncomp - String
	 */
	private static void setPurgeInetSPAPPComp(String asPurgeInetSPAPPComp)
	{
		try
		{
			siPurgeInetSPAPPComp =
				Integer.parseInt(asPurgeInetSPAPPComp.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetSPAPPComp = DEFAULTPURGEINETSPAPPCOMP;
			System.out.println(DEFAULT_PURGE_INET_SPAPP_COMP_MSG);
		}
	}

	/**
	 * Set PurgeInetSPAPPIncomp days.
	 *
	 * @param asPurgeInetSPAPPIncomp - String
	 */
	private static void setPurgeInetSPAPPIncomp(String asPurgeInetSPAPPIncomp)
	{
		try
		{
			siPurgeInetSPAPPIncomp =
				Integer.parseInt(asPurgeInetSPAPPIncomp.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetSPAPPIncomp = DEFAULTPURGEINETSPAPPINCOMP;
			System.out.println(DEFAULT_PURGE_INET_SPAPP_INCOMP_MSG);
		}
	}

	/**
	 * Set PurgeInetRenewal days.
	 *
	 * @param asNewPurgeInetSummary - String
	 */
	private static void setPurgeInetSummary(String asNewPurgeInetSummary)
	{
		try
		{
			siPurgeInetSummary =
				Integer.parseInt(asNewPurgeInetSummary.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInetSummary = DEFAULTPURGEINETSUMMARY;
			System.out.println(DEFAULT_PURGE_INET_SUMM_MSG);
		}
	}

	/**
	 * Set PurgeInvHist days.
	 *
	 * @param asNewPurgeInvHist - String
	 */
	private static void setPurgeInvHist(String asNewPurgeInvHist)
	{
		try
		{
			siPurgeInvHist = Integer.parseInt(asNewPurgeInvHist.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeInvHist = DEFAULTPURGEINVHIST;
			System.out.println(DEFAULT_PURGE_INV_HIST_MSG);
		}
	}

	/**
	 * Set PurgeItrntTransDelLog days.
	 *
	 * @param asPurgeItrntTransDelLog - String
	 */
	private static void setPurgeItrntTransDelLog(String asPurgeItrntTransDelLog)
	{
		try
		{
			siPurgeItrntTransDelLog =
				Integer.parseInt(asPurgeItrntTransDelLog.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeItrntTransDelLog = DEFAULTPURGEITRNTTRANSDELLOG;
			System.out.println(DEFAULT_PURGE_ITRNT_TRANS_DEL_LOG_MSG);
		}
	}

	/**
	 * Return the purge days for Purging Itrnt Trans Del Log rows.
	 *
	 * @return int
	 */
	public static int getPurgeItrntTransDelLog()
	{
		initialize();
		return siPurgeItrntTransDelLog;
	}

	/**
	 * Set PurgeReprntStkr days.
	 *
	 * @param asNewPurgeReprntStkr - String
	 */
	private static void setPurgeReprntStkr(String asNewPurgeReprntStkr)
	{
		try
		{
			siPurgeReprntStkr =
				Integer.parseInt(asNewPurgeReprntStkr.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeReprntStkr = DEFAULTPURGEREPRNTSTKR;
			System.out.println(DEFAULT_PURGE_REPRINT_STICKER_MSG);
		}
	}

	/**
	 * Set PurgeMFReqHstry days.
	 *
	 * @param asPurgeMFReqHstry - String
	 */
	private static void setPurgeMFReqHstry(String asPurgeMFReqHstry)
	{
		try
		{
			siPurgeMFReqHstry =
				Integer.parseInt(asPurgeMFReqHstry.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeMFReqHstry = DEFAULTPURGEMFREQHSTRY;
			System.out.println(DEFAULT_PURGE_MF_REQ_HSTRY_MSG);
		}
	}

	/**
	 * Set PurgeSecLog days.
	 *
	 * @param asNewPurgeSecLog - String
	 */
	private static void setPurgeSecLog(String asNewPurgeSecLog)
	{
		try
		{
			siPurgeSecLog = Integer.parseInt(asNewPurgeSecLog.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeSecLog = DEFAULTPURGESECLOG;
			System.out.println(DEFAULT_PURGE_SEC_LOG_MSG);
		}
	}
	/**
	 * Set Purge SetDelIndiDsabldPlcrdCust days.
	 *
	 * @param asPurgeSetDelIndiDsabldPlcrdCust - String
	 */
	private static void setPurgeSetDelIndiDsabldPlcrdCust(String asPurgeSetDelIndiDsabldPlcrdCust)
	{
		try
		{
			siPurgeSetDelIndiDsabldPlcrdCust =
				Integer.parseInt(
					asPurgeSetDelIndiDsabldPlcrdCust.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeSetDelIndiDsabldPlcrdCust =
				DEFAULTPURGESETDELINDIDSABLDPLCRDCUST;
			System.out.println(
				DEFAULT_PURGE_SET_DELINDI_DSABLD_PLCRD_CUST_MSG);
		}
	}
	/**
	 * Set value of siPurgeSpclPltRejLog
	 * 
	 * @param asPurgeSpclPltRejLog
	 */
	private static void setPurgeSpclPltRejLog(String asPurgeSpclPltRejLog)
	{
		try
		{
			siPurgeSpclPltRejLog =
				Integer.parseInt(asPurgeSpclPltRejLog.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeSpclPltRejLog = DEFAULTPURGESPCLPLTREJLOG;
			System.out.println(DEFAULT_PURGE_SPCL_PLT_REJ_LOG_MSG);
		}
	}

	/**
	 * Set PurgeSpclPltTransHstry days.
	 *
	 * @param asNewPurgeInvHist - String
	 */
	private static void setPurgeSpclPltTransHstry(String asPurgeSpclPltTransHstry)
	{
		try
		{
			siPurgeSpclPltTransHstry =
				Integer.parseInt(asPurgeSpclPltTransHstry.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeSpclPltTransHstry = DEFAULTPURGESPCLPLTTRANSHSTRY;
			System.out.println(DEFAULT_PURGE_SPCL_PLT_TRANS_HSTRY_MSG);
		}
	}

	/**
	 * Set PurgeTrans days.
	 *
	 * @param asNewPurgeTrans - String
	 */
	private static void setPurgeTrans(String asNewPurgeTrans)
	{
		try
		{
			siPurgeTrans = Integer.parseInt(asNewPurgeTrans.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeTrans = DEFAULTPURGETRANS;
			System.out.println(DEFAULT_PURGE_TRANS_MSG);
		}
	}

	/**
	 * Set PurgeTrans days.
	 *
	 * @param asNewPurgeTrans - String
	 */
	private static void setPurgeWebServices(String asNewPurgeWebServices)
	{
		try
		{
			siPurgeWebServices =
				Integer.parseInt(asNewPurgeWebServices.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeWebServices = DEFAULTPURGEWEBSERVICES;
			System.out.println(DEFAULT_PURGE_WEB_SERVICES_MSG);
		}
	}

	/**
	 * Set the number of days to Offset before releasing VI on hold 
	 * for IVTRS.
	 * 
	 * @param aiPurgeViIvtrsReleaseDays
	 */
	private static void setPurgeViIvtrsReleaseDays(int aiPurgeViIvtrsReleaseDays)
	{
		siPurgeViIvtrsReleaseDays = aiPurgeViIvtrsReleaseDays;
	}

	/**
	 * Parse the string holding the ViIvtrsReleaseDays and call the 
	 * setter method with the resulting int.
	 * 
	 * @param asPurgeViIvtrsReleaseDays
	 */
	private static void setPurgeViIvtrsReleaseDays(String asPurgeViIvtrsReleaseDays)
	{
		try
		{
			setPurgeViIvtrsReleaseDays(
				Integer.parseInt(asPurgeViIvtrsReleaseDays.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setPurgeViIvtrsReleaseDays(0);
			System.out.println(DEFAULT_PURGE_VI_IVTRS_MSG);
		}
	}

	/**
	 * Sets the number in each group to stager.
	 * 
	 * @param asRebootNumber String
	 */
	private static void setRebootNumber(String asRebootNumber)
	{
		try
		{
			siRebootNumber = Integer.parseInt(asRebootNumber.trim());
		}
		catch (Throwable aeThrowable)
		{
			siRebootNumber = DEFAULTSTAGGERGROUPNUMBER;
			System.out.println(DEFAULT_REBOOT_NUM_MSG);
		}
	}

	/**
	 * Sets the reboot stagger time to use.
	 * 
	 * @param asRebootStaggerTime String
	 */
	private static void setRebootStaggerTime(String asRebootStaggerTime)
	{
		try
		{
			siRebootStaggerTime =
				Integer.parseInt(asRebootStaggerTime.trim());
		}
		catch (Throwable aeThrowable)
		{
			siRebootStaggerTime = DEFAULTREBOOTSTAGGERTIME;
			System.out.println(DEFAULT_STAGGER_TIME_MSG);
		}
	}

	/**
	 * Set Receipt directory value.
	 *
	 * @param asReceiptsDirectory - String
	 */
	private static void setReceiptsDirectory(String asReceiptsDirectory)
	{
		// Make sure receiptsDirectory directory is created before reference.
		// Use property to get base directory.
		// set up a default receiptsDirectory directory.
		if (asReceiptsDirectory != null)
		{
			ssReceiptsDirectory = asReceiptsDirectory.trim();
		}
		else
		{
			//String DEFAULT_RECEIPTS_DIRECTORY =
			//	getRTSDirectory() + "rcpt\\";
			ssReceiptsDirectory =
				getRTSDirectory() + DEFAULT_RECEIPT_DIR;
		}

		File laFile = new File(ssReceiptsDirectory);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}
	}

	/**
	 * Set redirect Substa Id property and call int setter.
	 * 
	 * @param asSubstaId - String
	 */
	private static void setRedirectSubstaId(String asSubstaId)
	{
		try
		{
			siRedirectSubstaId = (Integer.parseInt(asSubstaId.trim()));
		}
		catch (Throwable aeThrowable)
		{
			siRedirectSubstaId = Integer.MIN_VALUE;
		}
	}

	/**
	 * Set redirect Workstation Id property and call int setter.
	 * 
	 * @param asWsId - String
	 */
	private static void setRedirectWsId(String asWsId)
	{
		try
		{
			siRedirectWsId = (Integer.parseInt(asWsId.trim()));
		}
		catch (Throwable aeThrowable)
		{
			siRedirectWsId = Integer.MIN_VALUE;
		}
	}

	/**
	 * Sets the remoteListenerPort for the back end service.
	 * 
	 * @param asRemoteListenerPortBE - String
	 */
	private static void setRemoteListenerPortBE(String asRemoteListenerPortBE)
	{
		try
		{
			siRemoteListenerPortBE =
				(Integer.parseInt(asRemoteListenerPortBE.trim()));
		}
		catch (Throwable aeThrowable)
		{
			siRemoteListenerPortBE = DEFAULTREMOTELISTENERPORTBE;
		}
	}

	/**
	 * Sets the remoteListenerPort for the gui.
	 * 
	 * @param asRemoteListenerPortgGUI - String
	 */
	private static void setRemoteListenerPortGUI(String asRemoteListenerPortgGUI)
	{
		try
		{
			siRemoteListenerPortGUI =
				(Integer.parseInt(asRemoteListenerPortgGUI.trim()));
		}
		catch (Throwable aeThrowable)
		{
			siRemoteListenerPortGUI = DEFAULTREMOTELISTENERPORTGUI;
		}
	}

	/**
	 * Set value for Reports Directory.
	 *
	 * @param asReportDirectory - String
	 */
	private static void setReportsDirectory(String asReportDirectory)
	{
		// Make sure reportsDirectory directory is created before reference.
		// Use property to get base directory.
		// set up a default reportsDirectory directory.
		if (asReportDirectory != null)
		{
			ssReportsDirectory = asReportDirectory.trim();
		}
		else
		{
			//String DEFAULT_REPORT_DIRECTORY =
			//	getRTSDirectory() + "rpt\\";
			ssReportsDirectory = getRTSDirectory() + DEFAULT_RPT_DIR;
		}

		File laFile = new File(ssReportsDirectory);
		if (!Comm.isServer() && !laFile.exists())
		{
			laFile.mkdir();
		}
	}

	/**
	 * Sets sbResetFundsDB2QueryOptimization
	 * 
	 * This boolean determines whether we reset the optimization level
	 *  in Funds (for Closeout) 
	 */
	private static void setResetFundsDB2QueryOptimization(boolean abResetFundsDB2QueryOptimization)
	{
		sbResetFundsDB2QueryOptimization =
			abResetFundsDB2QueryOptimization;
	}

	/**
	 * Sets sbResetFundsDB2QueryOptimization - true / false
	 * 
	 * If true, the DB2 Current Query Optimization Level will be reset 
	 * in Funds (for Closeout)  
	 * 
	 * @param asResetFundsDB2QueryOptimzation
	 */
	private static void setResetFundsDB2QueryOptimization(String asResetFundsDB2QueryOptimzation)
	{
		if (asResetFundsDB2QueryOptimzation != null
			&& asResetFundsDB2QueryOptimzation.trim().equalsIgnoreCase(
				"TRUE"))
		{
			setResetFundsDB2QueryOptimization(true);
		}
		else
		{
			setResetFundsDB2QueryOptimization(DEFAULT_RESET_FUNDS_DB2_QRY_OPT);
		}
	}

	/**
	 *  Sets siResetFundsDB2QueryOptimizationLevel
	 * 
	 * @param asResetFundsDB2QueryOptimizationLevel - String
	 */
	public static void setResetFundsDB2QueryOptimizationLevel(String asResetFundsDB2QueryOptimizationLevel)
	{
		try
		{
			siResetFundsDB2QueryOptimizationLevel =
				(Integer
					.parseInt(
						asResetFundsDB2QueryOptimizationLevel.trim()));
		}
		catch (Throwable aeThrowable)
		{
			siResetFundsDB2QueryOptimizationLevel =
				DEFAULT_RESET_FUNDS_DB2_QRY_OPT_LVL;
		}
	}

	/**
	 * Sets the RSPSLogDir value
	 * 
	 * @param asRSPSLogDir - String
	 */
	private static void setRSPSLogDir(java.lang.String asRSPSLogDir)
	{
		if (asRSPSLogDir != null)
		{
			ssRSPSLogDir = asRSPSLogDir.trim();
		}
		else
		{
			ssRSPSLogDir = COUNTY_RSPS_LOG_DIRECTORY;
		}

	}

	/**
	 * Set the ssRSPSUpdateSuffix value
	 * 
	 * @param asRSPSUpdateSuffix String
	 */
	private static void setRSPSUpdateSuffix(String asRSPSUpdateSuffix)
	{
		if (asRSPSUpdateSuffix != null)
		{
			ssRSPSUpdateSuffix = asRSPSUpdateSuffix.trim();
		}
		else
		{
			ssRSPSUpdateSuffix = null;
		}
	}

	/**
	 * Method loads the vector of valid RSPS update suffix into a 
	 * vector.
	 * 
	 * @param vector
	 */
	private static void setRSPSValidSuffix(String asList)
	{
		if (asList != null && asList.length() > 0)
		{
			svRSPSValidSuffix.addAll(
				Arrays.asList(asList.split(CommonConstant.STR_COMMA)));
		}
	}

	/**
	 * set value for RTSAppl directory.
	 *
	 * @param asRTSAppDirectory - String
	 */

	private static void setRTSAppDirectory(String asRTSAppDirectory)
	{
		// Make sure RTSAppl directory is created before reference.
		// Use property to get base directory.
		// set up a default RTSAppl directory.
		if (asRTSAppDirectory != null)
		{
			ssRtsAppDirectory = asRTSAppDirectory.trim();
		}
		else
		{
			String DEFAULT_RTSAPP_DIRECTORY =
				getRTSDirectory() + DEFAULT_RTSAPPL_DIR;
			ssRtsAppDirectory = DEFAULT_RTSAPP_DIRECTORY;
		}

		if (!Comm.isServer())
		{
			File laFile = new File(ssRtsAppDirectory);
			if (!laFile.exists())
			{
				laFile.mkdir();
			}
		}
	}

	/**
	 * set value for base RTS directory.
	 *
	 * @param asRTSDirectory - String
	 */
	private static void setRTSDirectory(String asRTSDirectory)
	{
		// Make sure rtsBaseDirectory is created before reference
		// set up a default rtsBaseDirectory
		if (asRTSDirectory != null)
		{
			ssRtsBaseDirectory = asRTSDirectory.trim();
		}
		else
		{
			ssRtsBaseDirectory = DEFAULT_BASE_DIRECTORY;
		}

		if (!Comm.isServer())
		{
			File laFile = new File(ssRtsBaseDirectory);
			if (!laFile.exists())
			{
				laFile.mkdir();
			}
		}
	}

	/**
	 * Sets the SendTransRptFreq
	 *
	 * @param aiSndTrnsRptFreq - int 
	 */

	public static void setSendTransRptFreq(int aiSendTransRptFreq)
	{
		siSendTransRptFreq = aiSendTransRptFreq;
	}

	/**
	 * Sets the SendTransRptFreq
	 *
	 * @param asSendTransRptFreq - String
	 */
	private static void setSendTransRptFreq(String asSendTransRptFreq)
	{
		try
		{
			setSendTransRptFreq(
				Integer.parseInt(asSendTransRptFreq.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setSendTransRptFreq(DEFAULTSENDTRANSRPTFREQ);
		}
	}

	/**
	 * Set Client Server boolean 
	 * 
	 */
	public static void setClientServer()
	{
		sbClientServer =
			AssignedWorkstationIdsCache.isServer(
				getOfficeIssuanceNo(),
				getSubStationId(),
				getWorkStationId());
	}

	/**
	 * Set set-aside directory
	 * 
	 * @param asSetAsideDir - String
	 */
	private static void setSetAsideDir(java.lang.String asSetAsideDir)
	{
		// Make sure setaside directory is created before reference.
		// Use property to get base directory.
		// set up a default setaside directory.
		if (asSetAsideDir != null)
		{
			ssSetAsideDir = asSetAsideDir.trim();
		}
		else
		{
			String DEFAULT_SETASIDE_DIRECTORY =
				getRTSDirectory() + DEFAULT_SETASIDE_DIR;
			ssSetAsideDir = DEFAULT_SETASIDE_DIRECTORY;
		}

		if (!Comm.isServer())
		{
			File laFile = new File(ssSetAsideDir);
			if (!laFile.exists())
			{
				laFile.mkdir();
			}
		}
	}

	/**
	 * Gets the Show Delqnt Days property and calls the boolean setter.
	 *
	 * @param asShowDelqntDays String
	 */
	public static void setShowDelqntDays(String asShowDelqntDays)
	{
		if (asShowDelqntDays != null
			&& asShowDelqntDays.trim().equalsIgnoreCase(YES))
		{
			System.out.println(SHOWING_DELQNT_DAYS_MSG);
			setShowDelqntDays(true);
		}
		else
		{
			// the default value is false
			setShowDelqntDays(false);
		}
	}

	/**
	 * Sets the Show Delqnt Days property 
	 *
	 * @param abShowDelqntDays String
	 */
	public static void setShowDelqntDays(boolean abShowDelqntDays)
	{
		sbShowDelqntDays = abShowDelqntDays;
	}

	// defect 11116
	/**
	 * Set the SOAPRSPS service endpoint.
	 * 
	 * @param asSoapRspsEndpoint
	 */
	private static void setSoapRspsEndpoint(String asSoapRspsEndpoint)
	{
		if (asSoapRspsEndpoint == null)
		{
			ssSoapRspsEndpoint = DEFAULT_SOAPRSPS_ENDPOINT;
		}
		else
		{
			ssSoapRspsEndpoint = asSoapRspsEndpoint;
		}
	}

	/**
	 * Set SOAPRSPS service application name.
	 * 
	 * @param asSoapRspsApplication
	 */
	private static void setSoapRspsApplication(String asSoapRspsApplication)
	{
		if (asSoapRspsApplication == null)
		{
			ssSoapRspsApplication = DEFAULT_SOAPRSPS_APPLICATION;
		}
		else
		{
			ssSoapRspsApplication = asSoapRspsApplication;
		}
	}

	/**
	 * Set the SOAPRSPS service user id.
	 * 
	 * @param asSoapRspsUserId
	 */
	private static void setSoapRspsUserId(String asSoapRspsUserId)
	{
		if (asSoapRspsUserId == null)
		{
			ssSoapRspsUserId = DEFAULT_SOAPRSPS_USERID;
		}
		else
		{
			ssSoapRspsUserId = asSoapRspsUserId;
		}
	}

	/**
	 * Set the SOAPRSPS service password.
	 * 
	 * @param asSoapRspsPassword
	 */
	private static void setSoapRspsPassword(String asSoapRspsPassword)
	{
		if (asSoapRspsPassword == null)
		{
			ssSoapRspsPassword = DEFAULT_SOAPRSPS_PASSWORD;
		}
		else
		{
			ssSoapRspsPassword = asSoapRspsPassword;
		}
	}
	// end defect 11116

	/** Sets the Static Table Pilot property 
	 *
	 * @param abStaticTablePilot String
	 */
	public static void setStaticTablePilot(boolean abStaticTablePilot)
	{
		sbStaticTablePilot = abStaticTablePilot;
	}
	/**
	 * Gets the Static Table Pilot property and calls the boolean setter.
	 *
	 * @param asStaticTablePilot String
	 */
	public static void setStaticTablePilot(String asStaticTablePilot)
	{
		if (asStaticTablePilot != null
			&& asStaticTablePilot.trim().equalsIgnoreCase(YES))
		{
			System.out.println(USING_STATIC_PILOT_TABLE_MSG);
			setStaticTablePilot(true);
		}
		else
		{
			// the default value is false
			setStaticTablePilot(false);
		}
	}

	/**
	 * Set the SubstaId.
	 * 
	 * <p>Also used by RTSWebAppsServlet.
	 * 
	 * @param aiSubstaId - int
	 */
	public static void setSubStationId(int aiSubstaId)
	{
		siSubStationId = aiSubstaId;
	}

	/**
	 * Set the SubstaId.
	 *
	 * @param asSubstaId - String
	 */
	private static void setSubStationId(String asSubstaId)
	{
		try
		{
			setSubStationId(Integer.parseInt(asSubstaId.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setSubStationId(DEFAULTSUBSTATIONID);
		}
	}
	
	/**
	 * Set the TexasSureHang boolean.
	 * 
	 * @param abTexasSureHang
	 */
	private static void setTexasSureHang(boolean abTexasSureHang)
	{
		sbTexasSureHang = abTexasSureHang;
	}
	
	/**
	 * Set the TexasSureHang boolean.
	 * 
	 * @param asTexasSureHang
	 */
	private static void setTexasSureHang(String asTexasSureHang)
	{
		if (asTexasSureHang != null
			&& asTexasSureHang.trim().equalsIgnoreCase("TRUE"))
		{
			setTexasSureHang(true);
		}
		else
		{
			setTexasSureHang(false);
		}
	}
	
	/**
	 * Set the Texas Sure Test Thread Count parameter.
	 * 
	 * @param aiTexasSureTestThreadCount
	 */
	private static void setTexasSureTestThreadCount(int aiTexasSureTestThreadCount)
	{
	    SystemProperty.siTexasSureTestThreadCount = aiTexasSureTestThreadCount;
	}
	
	/**
	 * Set the Texas Sure Test Thread Count parameter from a String.
	 * 
	 * @param aiTexasSureTimeOut
	 */
	private static void setTexasSureTestThreadCount(String asTexasSureTestThreadCount)
	{
	    try
	    {
	    	setTexasSureTestThreadCount(Integer.parseInt(asTexasSureTestThreadCount));
	    }
	    catch (NumberFormatException aeNFEx)
	    {
	    	setTexasSureTestThreadCount(0);
	    }
	}

	/**
	 * Set the Texas Sure Timeout parameter.
	 * 
	 * @param aiTexasSureTimeOut
	 */
	private static void setTexasSureTimeOut(int aiTexasSureTimeOut)
	{
	    SystemProperty.siTexasSureTimeOut = aiTexasSureTimeOut;
	}
	
	/**
	 * Set the Texas Sure Timeout parameter from a String.
	 * 
	 * @param aiTexasSureTimeOut
	 */
	private static void setTexasSureTimeOut(String asTexasSureTimeOut)
	{
	    try
	    {
	    	setTexasSureTimeOut(Integer.parseInt(asTexasSureTimeOut));
	    }
	    catch (NumberFormatException aeNFEx)
	    {
	    	setTexasSureTimeOut(DEFAULT_TEXAS_SURE_TIMEOUT);
	    }
	}
	
	/**
	 * Sets the URL String for the Texas Sure URL.
	 * 
	 * @param asTexasSureURL
	 */
	public static void setTexasSureURL(String asTexasSureURL)
	{
		if (asTexasSureURL != null)
		{
			ssTexasSureURL = asTexasSureURL;
		}
		else
		{
			ssTexasSureURL = null;
		}
	}

	/**
	 * Set the Title Directory.
	 *
	 * @param asTitleDirectory - String
	 */
	private static void setTitleDirectory(String asTitleDirectory)
	{
		ssDealerTitleDirectory = asTitleDirectory;
	}

	/**
	 * Set Vehicle Color mandatory capture Start Date.
	 * If Vehicle Color Start Date is not in rtscls.cfg, use default
	 * of current date
	 * 
	 * @param asVehColorStartDate - String
	 */
	private static void setVehColorStartDate(String asVehColorStartDate)
	{
		if (asVehColorStartDate != null
			&& asVehColorStartDate.trim().length() == DATE_LENGTH_8)
		{
			try
			{
				saVehColorStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(asVehColorStartDate.trim()));

			}
			catch (NumberFormatException aeNFE)
			{
				System.out.println(DEFAULT_VEHCOLOR_START_DATE_MSG);
				saVehColorStartDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						Integer.parseInt(DEFAULT_VEH_COLOR_START_DATE));
			}
			System.out.println(
				UtilityMethods.addPadding("Veh Color Start: ", 17, " ")
					+ saVehColorStartDate.getYYYYMMDDDate());
		}
	}

	/**
	 * This method sets the voidTransLogFileName
	 * 
	 * @param asVoidTransLogFileName - String
	 */
	private static void setVoidTransLogFileName(String asVoidTransLogFileName)
	{
		if (asVoidTransLogFileName != null)
		{
			ssVoidTransLogFileName = asVoidTransLogFileName;
		}
		else
		{
			ssVoidTransLogFileName = DEFAULT_VOID_TRANS_LOG_FILE;
		}
	}

	/**
	 * Set the Vendor Plates Office Issuance Number.
	 * 
	 * @param aiVpOfcIssuanceNo
	 */
	private static void setVpOfcIssuanceNo(int aiVpOfcIssuanceNo)
	{
		siVpOfcIssuanceNo = aiVpOfcIssuanceNo;
	}

	/**
	 * Set the Vendor Plates Office Issuance Number.
	 * 
	 * @param asVpOfcIssuanceNo
	 */
	private static void setVpOfcIssuanceNo(String asVpOfcIssuanceNo)
	{
		try
		{
			setVpOfcIssuanceNo(
				Integer.valueOf(asVpOfcIssuanceNo.trim()).intValue());
		}
		catch (NumberFormatException aeNFE)
		{
			setVpOfcIssuanceNo(DEFAULT_VP_OFFICE);
		}
	}

	/**
	 * Set the Vendor Plates Workstion Id.
	 * 
	 * @param aiVpWsId
	 */
	private static void setVpWsId(int aiVpWsId)
	{
		siVpWsId = aiVpWsId;
	}

	/**
	 * Set the Vendor Plates Workstion Id.
	 * 
	 * @param aiVpWsId
	 */
	private static void setVpWsId(String asVpWsId)
	{
		try
		{
			setVpWsId(Integer.valueOf(asVpWsId.trim()).intValue());
		}
		catch (NumberFormatException aeNFE)
		{
			setVpWsId(DEFAULT_VP_WSID);
		}
	}

	/**
	 * 
	 * Sets indi for Logging MF Errors to VSAM
	 * 
	 * @param asVSAMLogMFErr
	 */
	private static void setVSAMLogMFErr(String asVSAMLogMFErr)
	{
		if (asVSAMLogMFErr != null)
		{
			sbVSAMLogMFErr =
				asVSAMLogMFErr.trim().equalsIgnoreCase(YES);
		}
		else
		{
			sbVSAMLogMFErr = DEFAULTVSAMLOGERR;
			System.out.println(DEFAULT_VSAM_LOG_ERR_MSG);
		}
	}

	/**
	 * Set workStationId
	 * 
	 * <p>Also used by RTSWebAppsServlet.
	 *
	 * @parameter aWsId int
	 */
	public static void setWorkStationId(int aWsId)
	{
		siWorkStationId = aWsId;
	}

	/**
	 * Get workStationId property and call int setter.
	 *
	 * @param asWsId - String
	 */
	public static void setWorkStationId(String asWsId)
	{
		try
		{
			setWorkStationId(Integer.parseInt(asWsId.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setWorkStationId(DEFAULTWORKSTATIONID);
		}
	}

	/**
	 * Updates the PrintImmediate AbstractProperty.
	 * 
	 * <p>Set from other classes.
	 * 
	 * @param asValue - String
	 */
	public static void updatePrintImmediateProperty(String asValue)
		throws RTSException
	{
		String lsPrintImmediate = PROPERTY_PRINT_IMMEDIATE;

		File laDynamicPropFile =
			new File(CLIENT_DYNAMIC_CONFIG + "." + FILE_EXTENSION);
		try
		{
			FileInputStream laFIS =
				new FileInputStream(laDynamicPropFile);
			BufferedReader lsBufRdr =
				new BufferedReader(new InputStreamReader(laFIS));
			String lsLine = null;
			StringBuffer lsBuildString = new StringBuffer();

			while ((lsLine = lsBufRdr.readLine()) != null)
			{
				int liNdx = lsLine.indexOf(lsPrintImmediate);
				if (liNdx != -1)
				{
					lsLine = lsPrintImmediate + asValue;
				}
				lsBuildString.append(lsLine + "\n");
			}

			lsBufRdr.close();
			laFIS.close();

			FileOutputStream saFileOutputStream =
				new FileOutputStream(
					CLIENT_DYNAMIC_CONFIG + "." + FILE_EXTENSION,
					false);
			PrintWriter saPrintWriter =
				new PrintWriter(saFileOutputStream);
			saPrintWriter.println(lsBuildString.toString());

			saPrintWriter.flush();
			saPrintWriter.close();
			saFileOutputStream.close();
		}
		catch (IOException aeIOE)
		{
			aeIOE.printStackTrace();
			throw (new RTSException(RTSException.JAVA_ERROR, aeIOE));
		}
	}
	/**
	 * Gets SendTransFileLoc
	 * 
	 * <p>Only used for debugging.
	 * 
	 * @return String
	 */
	public static String getSendTransFileLoc()
	{
		initialize();
		return ssSendTransFileLoc;
	}
	/**
	 * Sets SendTransFileLoc
	 * 
	 * @param asSendTransFileLoc
	 */
	public static void setSendTransFileLoc(String asSendTransFileLoc)
	{
		ssSendTransFileLoc = asSendTransFileLoc;
	}

	/**
	 * Gets BiuldSendTransReviseDate
	 * 
	 * <p>Only used for debugging.
	 * 
	 * @return String
	 */
	public static String getBuildSendTransReviseDate()
	{
		initialize();
		return ssBuildSendTransReviseDate;
	}
	/**
	 * Sets BuildSendTransReviseDate
	 * 
	 * @param asBuildSendTransReviseDate
	 */
	public static void setBuildSendTransReviseDate(String asBuildSendTransReviseDate)
	{
		ssBuildSendTransReviseDate = asBuildSendTransReviseDate;
	}

	/**
	 * Get FundsUpdateFileLoc
	 * 
	 * @return ssFundsUpdateFileLoc
	 */
	public static String getFundsUpdateFileLoc()
	{
		initialize();
		return ssFundsUpdateFileLoc;
	}
	/**
	 * Set FundsUpdateFileLoc
	 * 
	 * @param asFundsUpdateFileLoc
	 */
	public static void setFundsUpdateFileLoc(String asFundsUpdateFileLoc)
	{
		ssFundsUpdateFileLoc = asFundsUpdateFileLoc;
	}
	/**
	 * Gets BuildFundsUpdateReviseDate
	 * 
	 * <p>Only used for debugging.
	 * 
	 * @return String
	 */
	public static String getFundsUpdateReviseDate()
	{
		initialize();
		return ssFundsUpdateReviseDate;
	}
	/**
	 * Sets BuildFundsUpdateReviseDate
	 * 
	 * @param asBuildFundsUpdtReviseDate
	 */
	public static void setFundsUpdateReviseDate(String asBuildFundsUpdtReviseDate)
	{
		ssFundsUpdateReviseDate = asBuildFundsUpdtReviseDate;
	}

	/**
	 * Set PurgeWSStatusLog days.
	 *
	 * @param asPurgeWSStatusLog - String
	 */
	private static void setPurgeWSStatusLog(String asPurgeWSStatusLog)
	{
		try
		{
			siPurgeWSStatusLog =
				Integer.parseInt(asPurgeWSStatusLog.trim());
		}
		catch (Throwable aeThrowable)
		{
			siPurgeWSStatusLog = DEFAULTPURGEWSSTATUSLOG;
			System.out.println(DEFAULT_PURGE_WS_STATUS_LOG_MSG);
		}
	}

	/**
	 * get EReminderFromEmail
	 * 
	 * @return String
	 */
	public static String getEReminderEmail()
	{
		return ssEReminderEmail;
	}

	/**
	 * set EReminderFromEmail
	 * 
	 * @param asEReminderFromEmail String
	 */
	public static void setEReminderEmail(String asEReminderEmail)
	{
		ssEReminderEmail = asEReminderEmail;
	}
	// defect 10670
	/**
	 * get eDirServer
	 * 
	 * @return String
	 */
	public static String geteDirServer()
	{
		return sseDirServer;
	}

	/**
	 * set eDirServer
	 * 
	 * @param aseDirServer String
	 */
	public static void seteDirServer(String aseDirServer)
	{
		if (aseDirServer != null)
		{
			sseDirServer = aseDirServer.trim();
		}
		else
		{
			//String DEFAULT_eDIR_SERVER
			sseDirServer = DEFAULT_eDIR_SERVER;
		}
	}

	// end defect 10670

	/**
	 * Return value of ssMyPlatesURL
	 * 
	 * @return String 
	 */
	public static String getMyPlatesURL()
	{
		initialize();
		return ssMyPlatesURL;
	}

	/**
	 * Set value of ssMyPlatesURL
	 * 
	 * @param asMyPlatesURL
	 */
	public static void setMyPlatesURL(String asMyPlatesURL)
	{
		if (asMyPlatesURL != null)
		{
			ssMyPlatesURL = asMyPlatesURL.trim();
		}
		else
		{
			ssMyPlatesURL = DEFAULT_MYPLATES_URL;
			System.out.println(DEFAULT_MYPLATES_URL_MSG);
		}
	}

	/**
	 * Get value of siPurgeSpclPltPrmt
	 * 
	 * @return int 
	 */
	public static int getPurgeSpclPltPrmt()
	{
		initialize();
		return siPurgeSpclPltPrmt;
	}

	/**
	 * Set value of siPurgeSpclPltPrmt
	 * 
	 * @param aiPurgeSpclPltPrmt
	 */
	public static void setPurgeSpclPltPrmt(int aiPurgeSpclPltPrmt)
	{
		siPurgeSpclPltPrmt = aiPurgeSpclPltPrmt;
	}

	/**
	 * Set value of siPurgeSpclPltPrmt
	 * 
	 * @param asPurgeSpclPltPrmt
	 */
	public static void setPurgeSpclPltPrmt(String asPurgeSpclPltPrmt)
	{
		try
		{
			setPurgeSpclPltPrmt(
				Integer.parseInt(asPurgeSpclPltPrmt.trim()));
		}
		catch (Throwable aeThrowable)
		{
			setPurgeSpclPltPrmt(DEFAULTPURGESPCLPLTPRMT);
			System.out.println(DEFAULT_SPCL_PLT_PRMT_MSG);
		}
	}

	//begin defect 11174
	/**
	 * set the WebAgentPath
	 * 
	 * @param lsPath
	 */
	private static void setWebAgentPath(String lsPath)
	{
		ssWebAgentPath = lsPath;

		//missing property
		if (UtilityMethods.isEmpty(lsPath))
		{
			ssWebAgentPath = "WebAgent";
		}
		else
		{
			ssWebAgentPath = lsPath.trim();
		}

	}
	//end defect 11174
	

	//begin defect 10797
	/**
	 * set the WebAgentProtocol
	 * 
	 * @param lsProtocol
	 */
	private static void setWebAgentProtocol(String lsProtocol)
	{
		ssWebAgentProtocol = lsProtocol;

		//missing property
		if (UtilityMethods.isEmpty(lsProtocol))
		{
			if (SystemProperty.isDevStatus())
			{
				ssWebAgentProtocol = "http";
			}
			else
			{
				ssWebAgentProtocol = "https";
			}
		}

	}

	/**
	 * set the WebAgentPort
	 * 
	 * @param string
	 */
	private static void setWebAgentPort(String lsPort)
	{
		ssWebAgentPort = lsPort;

		//missing property
		//note that empty String is not necessarily misconfigured if default port desired
		if (lsPort == null)
		{
			if (SystemProperty.isDevStatus())
			{
				ssWebAgentPort = "9081";
			}
			else
			{
				ssWebAgentPort = "";
			}
		}

	}

	/**
	 * set the WebAgentHost
	 * 
	 * @param string
	 */
	private static void setWebAgentHost(String lsHost)
	{
		ssWebAgentHost = lsHost;

		//missing property
		if (lsHost == null)
		{
			if (SystemProperty.isDevStatus())
			{
				ssWebAgentHost = "localhost";
			}
			else
			{
				ssWebAgentHost = "stage.rts.texasonline.state.tx.us";
			}
		}

	}
	//end defect 10797

	/**
	 * @return int
	 */
	public static int getMaxAddDsabldPlcrds()
	{
		return siMaxAddDsabldPlcrds;
	}

	/**
	 * @param asMaxAddDsabldPlcrds 
	 */
	public static void setMaxAddDsabldPlcrds(String asMaxAddDsabldPlcrds)
	{
		try
		{
			siMaxAddDsabldPlcrds = Integer.parseInt(asMaxAddDsabldPlcrds.trim());
		}
		catch (Throwable aeThrowable)
		{
			siMaxAddDsabldPlcrds = DEFAULT_MAX_ADD_DSABLD_PLCRDS;
			System.out.println(DEFAULT_MAX_ADD_DSABLD_PLCRDS_MSG );
		}
	}


}
