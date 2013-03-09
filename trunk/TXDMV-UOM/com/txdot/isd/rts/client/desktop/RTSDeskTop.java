package com.txdot.isd.rts.client.desktop;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.RTSDynamicMenu;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *  RTSDeskTop.java
 *
 *  (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/11/2002	Fixing bug for first menu disabling and 
 * 							moving focus to first enabled menuitem 
 * 							defect 3430
 * MAbs			04/16/2002 	above bug still not fixed yet but this 
 * 							version is closer to being correct
 * 							defect 3511
 * MAbs			04/19/2002 	Making RTSDesktop unresizable 
 * 							defect 3576
 * MAbs			05/06/2002	Fixing Desktop to handle OS/2 problems when 
 * 							first menu is grayed out
 * MAbs			05/06/2002	Making Internet button only enabled in 
 * 							ofcCode == 3 
 * 							defect 3774
 * Mabs			05/10/2002	Making focus go to Pending Trans when 
 * 							visible 
 * 							defect 3876
 * MAbs			05/14/2002	Making RTSDesktop unable to be closed 
 * 							defect 3857
 * N Ting		08/12/2002	Add Permissions to Driver's Ed
 * MAbs			08/26/2002	PCR 41 (VCE doesn't match this code!)
 * Ray Rowehl	09/03/2002	Disable Funds Menu Items that have Hot Keys
 * 							defect 4679
 *							when on Pending Trans Panel.
 * Ray Rowehl	09/03/2002	Use repaintForTransaction after Logon
 *							if pendingTransPanel is visible.
 *							defect 4696
 * Mabs        	09/11/2002  Handle initial DB down event from SendCache
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	11/11/2002	Broken as part of PCR41 Integration
 *							defect 4696
 * Ray Rowehl	12/18/2002	change reset method to always check for 
 * 							printimmediate
 *							defect 4892
 * S Govindappa 02/10/2003  Made changes in repaintForTransaction(..) 
 * 							to check whether Internet renewal 
 * 							transaction is cumulative or not and enable
 * 							the Internet renewal menu accordingly.
 * 							defect 5332
 * Ray Rowehl	02/20/2003	add isDbUp to repaint menu items
 * 							defect 5492
 * Ray Rowehl	04/01/2003  Clean up VCE problems
 * 							defect 5906
 * Ray Rowehl	07/07/2003	Do not turn on DB light on server up status.
 *							Use proper naming of passed boolean.
 *							method setServerStatus(), setMFStatus(), 
 *							setDBStatus()
 *							defect 6110
 * K Harrell	10/16/2003	Use isDBReady() vs. isDBUp()
 *							modified setData()
 *							defect 6614 Ver 5.1.5 fix 1
 * Ray Rowehl	08/22/2003	Add Logoff Menu Item
 *							Deprecate onOS2 method and replace it with 
 *							isWindowsPlatform.
 *							add getmenuItemShutdown(), 
 *							getmenuItemLogOff()
 *							modify focusGained(), 
 *							getmenuItemLogon(), getmenuLogon(),
 *							getmenuItemDriverEd()
 *							getmenuAccounting(), 
 *							getmenuAdminFunctions(), 
 *							getmenuCashDrawerOperations(), 
 *							getmenuCustomer(), getmenuFunds(),
 *							getmenuFundsManagement(), 
 *							getmenuFundsManagement(),
 *							getmenuInternetRenewal(), 
 *							getmenuInventory(),
 *							getmenuLocalOptions(), getmenuMisc(), 
 *							getmenuMiscRegistration(),
 *							getmenuPrintImmediate(), 
 *							getmenuRegistrationOnly(),
 *							getmenuReports(), getmenuSecurity(),
 *							getmenuSecurityReports(), 
 *							getmenuTitleRegistration()
 *							getmenuItemDriverEd(),
 *							handleException(),
 *							repaintMenuItems()
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	02/04/2004	Add log write to handleException
 *							modify handleException()
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * K Harrell	03/28/2004	Remove check for OS Platform
 *							see methods listed for 8/22/2003
 *							defect 6955 Ver 5.2.0
 * K Harrell	04/02/2004	5.2.0 Merge.  See PCR 34 Comments. 
 * 							Remove references to QuickCntrAccs &
 *							 QuickCntrRptAccs
 *							add ivjmenuItemReprintSticker
 *							add getmenuItemReprintSticker() 
 * 							modify getmenuReports()
 * 							modify repaintMenuItems()
 *							deprecate getmenuItemQuickCounterReport()
 *							deprecate getmenuItemQuickCounter()
 *							Ver 5.2.0
 * K Harrell	05/12/2004	5.2.0 Subcon Merge cont'd
 *							getmenuItemSubcontractorRenewal()
 *							Ver 5.2.0
 * K Harrell	05/13/2004	Disable DTA/Subcon in 5.2.0
 *							modify repaintMenuItems()
 *							defect 7101 Ver 5.2.0
 * K Harrell	05/18/2004	delete onOS2()
 *							defect 6955 Ver 5.2.0
 * K Harrell	06/13/2004	No longer enable Misc, Complete Vehicle
 *							Transaction or Set Print Destination as 
 *							a function of reprint receipt access
 * 							modify repaintMenuItems()
 *							defect 7158 Ver 5.2.0
 * Min Wang		07/20/2004	Add menu item RSPS Status Update
 *							add getmenuItemRSPSUpdates()
 *							modify repaintMenuItems()
 *							defect 7310 Ver 5.2.1
 * K Harrell	07/29/2004	Make Reprint Sticker Report available in
 *							Region
 *							modify repaintMenuItems() 
 *							defect 7385  Ver 5.2.1 
 * K Harrell	10/25/2004	Make all Accounting events available while  
 *							in DB Down.
 *							modify repaintMenuItems() 
 *							defect 6705  Ver 5.2.2
 * K Harrell	10/28/2004	Make Detail Reports and CloseOut Statistics
 *							available for shared cash drawer.
 *							modify repaintMenuItems() 
 *							defect 7685  Ver 5.2.2
 * Min Wang 	03/14/2004	Fix showing blue text where black is 
 * 							expected.
 * 							modify getBtnInternet()
 * 							defect 7904 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove the BlankLine menu items.
 * 							RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename FrmInventoryActionReportDateSelectionINV020
 * 							to be FrmInventoryActionReportDateSelectionINV019.
 * 							modify getmenuItemInventoryActionReport()
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Change constant for INV015b to INV015
 * 							modify getmenuItemInventoryHistory()
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	04/15/2005	Also needed to rename INV020 
 * 							screen constant.
 * 							modify getmenuItemInquiry()
 * 							defect 6964 Ver 5.2.3
 * Min Wang 	05/24/2005  Change hot key 'R' to 'U' for 
 * 							RSPS Updates under Local Options. 
 * 							modify getmenuItemRSPSUpdates()
 * 							defect 8084 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3
 * Jeff S.		06/28/2005	Added the ability to display an error when
 * 							you are not inside a frame. Had to tell 
 * 							RTSException who the desktop is so that it
 * 							can tie the MSGDialog to it.
 * 							modify initialize()
 * 							defect 8270 Ver 5.2.3
 * K Harrell	07/12/2005	Remove checks for 5.2.0 for Subcon & DTA
 * 							modify repaintMenuItems() 
 * 							defect 7878 Ver 5.2.3 
 * B Hargrove	07/13/2005	Change verbiage for Driver's Ed so that 
 * 							menu and event header match.
 * 							Change text strings to CONSTANTS.
 * 							Comment out all duplicated calls to 
 * 							setActionCommand(). 
 * 							modify getmenuItemDriverEd()   
 * 							defect 7836  Ver 5.2.3
 * Ray Rowehl	07/16/2005	Change how version number and date are
 * 							picked up.  Use SystemProperty.
 * 							Add a few constant changes.
 * 							Remove some unused setFont()'s.
 * 							modify getlblDate(), getstcLblVersion(),
 * 								handleException()
 * 							defect 7885 Ver 5.2.3
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							modify repaintForTransaction()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * Jeff S.		08/16/2005	Renamed CP### Screen Constants to REG###
 * 							modify getmenuItemProcessHold()
 * 								getmenuItemProcessNew()
 * 								getmenuItemReports()
 * 								getmenuItemSearch()
 * 							defect 7889 Ver 5.2.3
 * T Pederson	09/14/2005	Make internet count button not focusable
 * 							modify getbtnInternet()
 * 							defect 6516 Ver 5.2.3
 * K Harrell	10/07/2005	Use character vs. numeric to set mnemonic
 * 							defect 8297 Ver 5.2.3  
 * K Harrell	10/20/2005	Do not enable Title Package in moreTrans
 * 							when DB Down. Extensive refactoring.
 * 							add	assignLocalOptionMenuFlags(),
 * 							disableMenusBeforeLogon(),
 * 							enableAccountingMenu(),enableCustomerMenu(),
 * 							enableFundsMenu(),enableInventoryMenu(),
 * 							enableLocalOptionsMenu(),enableLogonMenu(),
 * 							enableMiscMenu(),enableReportsMenu(),
 * 							enableAcountingMenuInMoreTrans(),
 * 							enableCustomerMenuInMoreTrans(),
 * 							isEnabledInMoreTrans(),isCounty(),isHQ(),
 * 							isRegion(),isMainOffice()
 * 							add ciOfcIssuanceCd,ciOfcIssuanceNo,
 * 							ciSubstaId, ciWsId, cbCreditCheck,
 * 							cbDealerCheck,cbLienholderCheck, cbServer,
 * 							cbSharedCashDrawer,cbSubconCheck,
 * 							cbTreatAsServer, HQ_OFFICE_CD,
 * 							REGION_OFFICE_CD,COUNTY_OFFICE_CD,
 * 							MAIN_OFFICE_SUBSTAID
 * 							delete setServerStatus()
 * 							modify repaintForTransaction()
 * 							repaintMenuItems(),setComputerInformation() 
 * 							defect 8337 Ver 5.2.3
 * K Harrell	11/02/2005	Modify Menu titles for "Close Out for the Day",
 * 							"Close Out Statistics", "Rerun Countywide" 
 *  						modify CLOSEOUT, CLOSEOUT_STATS,RERUN_CNTYWIDE
 * 							defect 8379 Ver 5.2.3  
 * Jeff S.		12/12/2005	When Desktop is activated and the pending
 * 							panel is not visible we need to put focus
 * 							on the Desktop menu bar.  This fixed the 
 * 							problem with the hot keys not working.
 * 							add requestFocusMenuBar()
 * 							defect 7885 Ver 5.2.3  
 * Jeff S.		01/17/2006	Added class for workaround for java bug 
 * 							4911422.  Problem with Jmenu and loosing 
 * 							focus after showing a modal JDialog.  Fixed 
 * 							in Java 1.5.  8520 was created to back this
 * 							change out for Java 1.5.
 * 							modify initialize()
 * 							defect 8519 Ver 5.2.3
 * Jeff S.		10/10/2006	Enable Drivers Education Plt. for County and
 * 							Region.  Added handling for when at the 
 * 							Pending Trans Panel. Task 41.
 * 							modify enableCustomerMenu(), 
 * 								enableCustomerMenuOnPendingTrans()
 * 							defect 8900 Ver Exempts
 * Jeff S.		10/10/2006	Add Exempt Audit Report menu item
 * 							under Reports. Task 43.
 * 							add ivjmenuItemExemptAuditReport, 
 * 								EXEMPT_AUDIT_RPT
 * 							add getmenuItemExemptAuditReport()
 * 							modify getmenuReports(), enableReportsMenu()
 * 							defect 8900 Ver Exempts
 * Jeff S.		10/12/2006	Add the ability to launch IE from the 
 * 							desktop. Task 42 - Launch Exempt agency list
 * 							add ivjRTSDynamicMenuHelp, HELP
 * 							add getmenuHelp()
 * 							modify disableMenusBeforeLogon(), 
 * 								getRTSDeskTopJMenuBar(), repaint(), 
 * 								repaintMenuItems()
 * 							defect 8900 Ver Exempts
 * Jeff S.		04/02/2007	Made the desktop size to screen size -1 
 * 							so that the taskbar is not covered and the 
 * 							balloon can be used.
 * 							modify initialize()
 * 							defect 7768 Ver Broadcast Message
 * J Rue/		08/07/2007	Set menuItemVoidTransaction to false
 * K Harrell				modify repaintForTransaction()
 * 							defect 9060 Ver Broadcast Messaging
 * K Harrell	02/13/2007	Add menu for Special Plates
 * 							add SPCL_PLT_APP,SPCL_PLT_RENEW,
 * 							 	SPCL_PLT_REVISE,SPCL_PLT_RESERVE,
 * 								SPCL_PLT_UNACC,SPCL_PLT_DEL,SPCL_PLT_RPTS 
 * 							add ivjmenuSpecialPlates,
 * 								ivjmenuItemSpecialPlatesApplication,
 * 								ivjmenuItemSpecialPlatesRenewPlateOnly,
 *								ivjmenuItemSpecialPlatesRevise,
 *								ivjmenuItemSpecialPlatesReserve, 
 *								ivjmenuItemSpecialPlatesUnacceptable,  
 *								ivjmenuItemSpecialPlatesDelete,
 *								ivjmenuItemSpecialPlatesReports, 
 *								ivjJSeparatorSpecialPlate0, 
 *								ivjJSeparatorSpecialPlate1,
 *								ivjJSeparatorSpecialPlate2 
 * 							add getMenuSpecialPlates(),
 * 							  getmenuItemSpecialPlatesApplication(),
 * 							  getmenuItemSpecialPlatesDelete(),
 * 							  getmenuItemSpecialPlatesRenewPlateOnly(),
 * 							  getmenuItemSpecialPlatesReserve(),
 * 							  getmenuItemSpecialPlatesRevise(),
 * 							  getmenuItemSpecialPlatesUnacceptable(),
 * 							  getmenuItemSpecialPlatesReports()
 * 							modify getmenuCustomer(), enableCustomerMenu(),
 * 							  enableCustomerMenuOnPending (),
 * 							  disableMenusBeforeLogon() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/13/2007	Void available via mnemonic ("O") while on 
 * 							 Pending Transaction Screen.
 * 							modify repaintForTransaction()
 * 							defect 9060 Ver Special Plates 
 * J Rue		02/14/2007	Add setActionCommand() function
 * 							modify getmenuItemSpecialPlatesReports()
 * 							modify getmenuItemSpecialPlatesDelete()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/22/2007	Additional work for Special Plates
 * 							modify enableCustomerMenu(), 
 * 							 enableCustomerMenuOnPendingTrans()
 * 							defect 9085 Ver Special Plates 
 * J Rue		02/23/2007	Change SPL003 to KEY002
 * 							modify getmenuItemSpecialPlatesRenewPlateOnly()
 * 							modify getmenuItemSpecialPlatesRevise()
 * 							modify getmenuItemSpecialPlatesReserve()
 * 							modify getmenuItemSpecialPlatesUnacceptable()
 * 							modify getmenuItemSpecialPlatesDelete()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/27/2007	Renamed SPL004 to SPL003
 * 							modify getmenuItemSpecialPlatesReports()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	03/08/2007	Allow Inv Action Report at HQ.  Only check 
 * 							security access.
 * 							modify enableInventoryMenu()
 * 							defect 9116 Ver Special Plates
 * K Harrell	03/09/2007	Implement SystemProperty isCounty(), isHQ(), 
 * 							isRegion()
 * 							defect 9085 Ver Special Plates 
 * Min Wang		03/13/2007	Add menu item for VI rejection Report.
 * 							add getmenuItemVIRejectionReport(),
 * 								ivjmenuItemVIRejectionReport,
 * 								VI_REJECTION_RPT
 * 							modify getmenuInventory()
 * 							defect 9117 Ver Special Plates
 * Min Wang		05/02/2007  disable menu item VI rejection Report for 
 * 							county.
 * 							modify enableInventoryMenu()
 *  						defect 9117 Ver Special Plates
 * Min Wang		05/14/2007	change VI Rejection Report to
 * 							PLP Request Rejection Report.
 * 							modify getmenuInventory()
 * 								   getmenuItemVIRejectionReport()
 * 							defect 9117 Ver Special Plates
 * K Harrell	07/02/2007	Allow those authorized to use Print Immediate (in
 * 							(Region as well as County).  Do not enable 
 * 							CompleteVehicleTransaction @ HQ. 
 * 							modify enableMiscMenu()
 * 							defect 9157 Ver Special Plates
 * K Harrell	04/26/2008	Remove COA from menus / 
 * 							  Comment out references
 * 							add SALVAGE_COA
 * 							delete SALVAGE
 * 							delete ivjmenuItemCOA, COA
 * 							delete getmenuItemCOA()
 * 							modify enableCustomerMenu(),
 * 							   enableCustomerMenuOnPendingTrans(),
 * 							   getmenuTitleRegistration()
 * 							defect 9642 Ver POS_Defect_A
 * K Harrell	05/07/2008	Enable Funds,Funds Balance Reports for HQ
 * 							modify enableFundsMenu()
 * 							defect 9653 Ver POS_Defect_A
 * K Harrell	05/13/2008	Only show publishing report if substations 
 * 							exist.  Only show publishing update if 
 * 							currently publishing and main office.
 * 							delete setServerStatus()
 * 							modify enableLocalOptionsMenu(), 
 * 							  setServerPlusEnabled() 
 * 							defect 8550 Ver POS_Defect_A
 * K Harrell	06/09/2008	Only HQ should have access to Salvage. 
 * 							modify enableCustomerMenu() 
 * 							defect 9701 Ver POS_Defect_A
 * K Harrell	09/10/2008	Implement new Local Options Reporting 
 * 							authorities 
 * 							modify enableLocalOptionsMenu() 
 * 							defect 9710 Ver POS_Defect_B 
 * K Harrell	10/27/2008  Modifications for DISABLED PLACARD
 * 							delete ivjmenuItemDisabledParking, 
 * 							 getmenuItemDisabledParking(),
 * 							 DISABLE_PARK
 * 							add ivjMenuDisabledPlacard, 
 * 							  ivjMenuItemPlacardManagement,
 *                            ivjMenuItemPlacardInquiry, 
 * 							  ivjMenuItemPlacardReport, get methods.
 *							  DISABLE_PLACARD
 *							modify enableCustomerMenu(),  
 *							  enableCustomerMenuOnPendingTrans(), 
 *							  getmenuMiscRegistration()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	01/13/2009	Implement constants to more accurately reflect
 * 							server functions:  BATCH vs. DATA, CODE vs. 
 * 							COMM. Ensure all Workstation Labels have NO 
 * 							SPACE at the end.  
 * 							add CODE_BATCHSVR, CODE_SERVER, BATCH_SERVER,
 * 							  COMMA_SPACE 
 * 							delete COMM_DATASVR,COMM_SERVER,DATA_SERVER,
 * 							  COMMA 
 *							modify setComputerInformation(), COUNTY  
 * 							defect 8821 Ver Defect_POS_D
 * K Harrell	02/25/2009	add menu items for Electronic Title Report,
 * 							Certified Lienholder Report. 
 * 							Also, update mnemonics to use KeyEvent 
 * 							 constants. 
 * 							add ETITLE_RPT, CERTFD_LIENHLDR_RPT 
 *							add ivjmenuItemCertifiedLienholderReport, 
 *							  ivjmenuItemElectronicTitleReport, 
 *							  ivjJSeparatorReports0,
 *							  ivjJSeparatorReports1
 *							add getmenuItemCertifiedLienholderReport(),
 *							  getmenuItemElectronicTitleReport(), 
 *							  getJSeparatorReports0(),
 *							  getJSeparatorReports1()
 * 							delete ivjJSeparator17, getivjJSeparator17() 	
 *      					modify getmenuReports(), getmenuLocalOptions(),
 * 							  enableReportsMenu(), enableLocalOptionsMenu()
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	07/25/2009	HB3095: No longer collect Mobility status. 
 * 							Return to PDC transcd for default.
 * 							modify enableCustomerMenuOnPendingTrans(), 
 * 							 getmenuItemDisabledPlacardManagement() 
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	08/16/2009	Implement SystemProperty.isClientServer()
 * 							modify setComputerInformation()
 * 							defect 8628 Ver Defect_POS_F   		 
 * B Hargrove	09/18/2009	Use new logos at cutover for new DMV agency.
 * 							add IMAGE_DMV_BKGRND, IMAGE_DMV_LOGO
 * 							modify getJLabel1(), initialize()
 * 							defect 10155 Ver Defect_POS_F
 * K Harrell	10/13/2009	Provide option of Sort order on Local Options
 * 							add SORT_BY_ID, SORT_BY_NAME
 * 							add ivjmenuCertifiedLienholderReport, 
 * 							 ivjmenuDealerReport, 
 * 							 ivjmenuLienholderReport,
 * 							 ivjmenuSubcontractorReport, 
 * 							 ivjmenuItemCertifiedLienhldrRptIdSort,
 *  						 ivjmenuItemCertifiedLienhldrRptNameSort,
 * 							 ivjmenuItemDealerRptIdSort, 
 * 							 ivjmenuItemDealerRptNameSort,
 * 							 ivjmenuItemLienholderReportIdSort, 
 * 							 ivjmenuItemLienholderReportNameSort,
 * 							 get methods 
 * 							delete ivjmenuItemCertifiedLienholderReport,   
 * 							 ivjmenuItemDealerReport, 
 * 							 ivjmenuItemLienholderReport,
 * 							 ivjmenuItemSubcontractorReport,
 * 							 get methods
 * 							modify enableLocalOptionsMenu(), 
 * 							 getmenuLocalOptions()   
 * 							defect 10250 Ver Defect_POS_G
 * Ray Rowehl	10/19/2009	Add logging to show when the menu is 
 * 							repainted.  This was done to help show how 
 * 							big the slow logon problem is.
 * 							modify repaint(), repaintMenuItems()
 * 							defect 10255 Ver Defect_POS_G 	
 * K Harrell	02/27/2010	Only enable Internet Renewal if County 
 * 							modify enableCustomerMenu() 
 * 							defect 10387 Ver POS_640 	
 * K Harrell	04/07/2010	Attempt to avoid repaint during Disabled
 * 							Placard Processing when interrupted by Timer.    
 * 							modify setData() 
 * 							defect 10407 Ver POS_640 
 * K Harrell	04/23/2010	Add menu items for MyPlates.com, NICUSA.com
 * 							add ivjmenuItemNICUSA, ivjmenuItemMyPlates, 
 * 							 get/set methods
 * 							add NICUSA, MYPLATES 
 * 							modify enableCustomerMenu(), 
 * 							 getmenuInternetRenewal(), 
 * 							 getmenuSpecialPlates() 
 * 							defect 10461 Ver POS_640
 * K Harrell	04/27/2010	Modify url for NICUSA
 * 							modify getmenuItemNICUSA() 
 * 							defect 10461 Ver POS_640  
 * K Harrell	05/05/2010	Modify url for MYPLATES
 * 							modify getmenuItemMyPlates() 
 * 							defect 10461 Ver POS_640
 * K Harrell	05/24/2010	Modify for Timed Permit
 * 							add ivjmenuTimedPermit,
 * 							 ivjmenuItemPermitInquiry, 
 * 							 ivjmenuItemPermitApplication, 
 * 							 ivjmenuItemPermitDuplicateReceipt, 
 * 							 get methods 
 * 							delete ivjmenuItemTimedPermit, get method
 * 							modify getmenuMiscRegistration(), 
 * 							 enableCustomerMenu,
 * 							 enableCustomerMenuOnPendingTrans()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/30/2010	Use PRMINQ for Prmt Inquiry vs. INQ
 * 							modify ivjmenuItemPermitInquiry()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	09/20/2010	Implement Dynamic RTS MyPlates URL
 * 							modify getmenuItemMyPlates()  
 * 							defect 10575 Ver 6.6.0 
 * K Harrell	12/26/2010	Modify for Duplicate Special Plate Insignia
 * 							add ivjmenuItemSpecialPlatesDuplicatePermit, 
 * 							 get method
 * 							modify enableCustomerMenu(), 
 * 							 getmenuSpecialPlates() 
 * 							defect 10700 Ver 6.7.0  
 * K Harrell	01/14/2011	Modify for WebAgent 
 * 							add ivjJSeparator27, get method 
 * 							add ivjmenuItemWebAgent, get method 
 * 							modify getmenuRegistrationOnly(), 
 * 							 enableCustomerMenu()
 * 							defect 10733 Ver 6.7.0
 * K Harrell	01/20/2011	Modify for Batch Print Management
 * 							add BATCH_RPT_MGMT 
 * 							add ivjmenuItemBatchRptMgmt, get method
 * 							modify getmenuLocalOptions(), 
 * 							 enableLocalOptionsMenu()
 *  						defect 10701 Ver 6.7.0 
 * B Woodson 	04/07/2011	correct the WebAgent URL
 * 							modify getmenuItemWebAgent
 * 							defect 10797 Ver 6.7.1 
 * B Woodson 	04/25/2011 	Change WebPort and WebProtocol 
 *                           to WebAgentPort and WebAgentProtocol
 *      					defect 10797 Ver 6.7.1
 * K Harrell	05/28/2011	Modify for Timed Permit - Modify 
 * 							add ivjmenuItemPermitModify, get method
 * 							modify enableCustomerMenu(),
 * 							 enableCustomerMenuOnPendingTrans(),
 * 							 getmenuTimedPermit()
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	06/10/2011	add Suspected Fraud Report
 * 							Disable Reports Menu if in MoreTrans 
 * 							add ivjmenuItemSuspectedFraudReport, 
 * 								get method
 * 							delete ivjJSeparatorReports0,
 * 							 ivjJSeparatorReports1, get methods  
 * 							modify enableReportsMenu(), getmenuReports(),
 * 							 repaintForTransaction() 
 * 							defect 10900 Ver 6.8.0 
 * R Pilon		06/10/2011	Implement Special Plate Inquiry
 * 							add getmenuItemSpecialPlatesInquiry(), 
 * 							  ivjmenuItemSpecialPlatesInquiry
 * 							modify enableCustomerMenu(), 
 * 							  getmenuSpecialPlates()
 * 							defect 10820 Ver 6.8.0
 * K Harrell	06/15/2011	Make Fraud Report available to HQ
 * 							modify enableReportsMenu() 
 * 							defect 10900 Ver 6.8.0 
 * M Reyes		09/06/2011	Windows 7 has focus issue
 * 							modify initialize()
 * 							defect 10984 Ver 6.8.1
 * K Harrell	11/14/2011	Assign CTL-7 to Special Plates Inquiry 
 * 							modify getmenuItemSpecialPlatesInquiry()
 * 							defect 11052 Ver 6.9.0 
 * B Woodson    12/08/2011  add WebAgent path parameter
 * 							modify getmenuItemWebAgent() 
 * 							defect 11174   Ver 6.9.0
 * M Reyes		03/28/2012	Enable the "X" on RTSDesktop
 * 							modify initialize()
 * 							defect 11325 Ver 7.0.0
 * M Reyes		04/17/2012	Resize the Desktop
 * 							modify initialize()
 * 							defect 11336 Ver 7.0.0
 * M Reyes		05/04/2012	Change Desktop Logon/Logoff
 * 							modify getmenuItemLogon(), getmenuLogon()
 * 							defect 11354 Ver 7.0.0
 * M Reyes		05/08/2012	Move the desktop down and right so the
 * 							border shows in windows bubble view.
 * 							modify initialize()
 * 							defect 11354 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * The GUI Desktop of RTS II.
 * 
 * <p>This is the parent frame to all other dialogs.
 *
 * @version	7.0.0   			05/08/2012
 * @author	Michael Abernethy
 * <br>Creation Date:			08/06/2001 13:02:54	
 */
public class RTSDeskTop extends JFrame implements FocusListener
{
	// defect 10250 
	private final static String SORT_BY_ID = "Sort by Id";
	private final static String SORT_BY_NAME = "Sort by Name";
	// end defect 10250 

	private final static String ACCOUNTING = "Accounting";
	private final static String ADDL_SALES_TAX = "Additional Sales Tax";
	private final static String ADDLCOL_TIMELAG =
		"Additional Collections/Time Lag";
	private final static String ADDR_CHG_PRT_RENEW =
		"Address Change/Print Renewal";
	private final static String ADMIN_FUNCS =
		"Administrative Functions";
	private final static String ALLOCATE = "Allocate";
	private final static String BATCH_SERVER = "BATCH SERVER";
	// defect 10701 
	private final static String BATCH_RPT_MGMT =
		"Batch Report Management";
	// end defect 10701 

	private final static String CASH_DRWR_OPS =
		"Cash Drawer Operations";
	private final static String CCO = "CCO";
	private final static String CENTER = "Center";
	private static final String CERTFD_LIENHLDR_RPT =
		"Certified Lienholder Report";
	private final static String CLOSE_OUT_FOR_THE_DAY =
		"Close Out for the Day";
	private final static String CLOSEOUT_STATS = "Closeout Statistics";
	private final static String CNTY_FUNDS_REMIT =
		"County Funds Remittance";
	private final static String CODE_BATCHSVR = "CODE/BATCH SERVER";
	private final static String CODE_SERVER = "CODE SERVER";
	private final static String COMMA_SPACE = ", ";
	private final static String COMPLETE_VEH_TRANS =
		"Complete Vehicle Transactions";
	private final static String CORRECT_TTLREJ =
		"Correct Title Rejection";
	private final static String COUNTY = " COUNTY";
	private final static String CRDT_CARD_FEE_UPD =
		"Credit Card Fee Update";
	private final static String CURR_INT_RENEW_CT =
		"Current Internet Renewal Count: 0";
	private final static String CURR_STAT = "Current Status";
	private final static String CUSTOMER = "Customer";
	private final static String DATA_SRVR = "Data Server";
	private final static String DEALER_RPT = "Dealer Report";
	private final static String DEALER_TITLES = "Dealer Titles";
	private final static String DEALER_UPDS = "Dealer Updates";
	private final static String DEDUCT_HOTCHK_CRDT =
		"Deduct Hot Check Credit";
	private final static String DELETE = "Delete";
	private final static String DELETE_TIP = "Delete Title In Process";
	public final static String DELIM = "#";
	private final static String DETAIL_RPTS = "Detail Reports";
	private final static String DFLT_DATE = "12/10/2001";
	private final static String DFLT_OFC_WS =
		"MCCLENNAN COUNTY, MCCLENNAN COUNTY MAIN OFFICE, DATA SERVER G54FRT5";
	private final static String DIR_EAST = "East";
	// end defect 8900
	private final static String DIR_SOUTH = "South";
	private final static String DIR_WEST = "West";
	// defect 9831 
	private final static String DISABLE_PLACARD = "Disabled Placard";
	private final static String DUP_RCPT = "Duplicate Receipt";
	private final static String EMPL_SECURITY = "Employee Security";
	private final static String EMPL_SECURITY_RPT =
		"Employee Security Report";

	private final static String ERRMSG_ASSGND_WSID =
		"Error retrieving Assigned Workstation Id";
	private final static String ERRMSG_HOST_NAME =
		"Error retrieving Host Name";

	// defect 9971 
	private static final String ETITLE_RPT = "Electronic Title Report";
	private final static String EVENT_SECURITY_RPT =
		"Event Security Report";
	private final static String EXCHANGE = "Exchange";

	// defect 8900
	// Task 43 - Add Exempt Audit Report menu item under reports.
	private static final String EXEMPT_AUDIT_RPT =
		"Exempt Audit Report";
	private final static String FUNDS = "Funds";
	private final static String FUNDS_BAL_RPTS =
		"Funds Balance Reports";
	private final static String FUNDS_INQ = "Funds Inquiry";
	private final static String FUNDS_MGMNT = "Funds Management";
	// defect 8900
	// Task 42 - Add Help menu
	private final static String HELP = "Help";
	// end defect 8900
	private final static String HOLD_RELEASE = "Hold/Release";
	private final static String HOTCHK_CREDIT = "Hot Check Credit";
	private final static String HOTCHK_REDEEM = "Hot Check Redeemed";
	private final static String IMAGE_DMV_BKGRND =
		"/images/bkgrnd_dmv.jpg";

	// defect 10155
	private final static String IMAGE_DMV_LOGO = "/images/logo_dmv.GIF";
	private final static String IMAGE_LOGO = "/images/logo.gif";
	private final static String IMAGE_OFF = "/images/off.gif";
	private final static String IMAGE_ON = "/images/on.gif";
	private final static String IMAGE_TXDOT_BKGRND =
		"/images/Txdotbackground.jpg";
	// end defect 10155

	private final static String INQUIRY = "Inquiry";
	private final static String INTERNET_RENEW = "Internet Renewal";
	private final static String INV_ACTION_RPT =
		"Inventory Action Report";
	// end defect 9117
	private final static String INV_HIST = "Inventory History";
	private final static String INVENTORY = "Inventory";
	private final static String ISSUE_DRVR_ED_PLT =
		"Issue Driver Education Plate";
	private final static String ITEMS_SEIZED = "Item(s) Seized";
	private final static String LIENHLDR_RPT = "Lienholder Report";
	private final static String LIENHLDR_UPDS = "Lienholder Updates";
	private final static String LOCAL_OPTS = "Local Options";
	// defect 11354
	private final static String LOGOFF = "Exit Desktop";
	private final static String LOGON = "Exit";
	// end defect 11354

	//	Constants 
	private final static int MAIN_OFFICE_SUBSTAID = 0;
	private final static String MISC = "Miscellaneous";
	private final static String MISC_REG = "Miscellaneous Registration";
	private final static String MODIFY = "Modify";
	// defect 10461 
	private final static String MYPLATES = "MyPlates.com";
	private final static String NICUSA = "NICUSA.com";
	// end defect 10461  

	private final static String NEXT = "Next";
	private final static String NEXT_CUST = "Next Customer";
	private final static String NONRES_AG_PERMIT =
		"Non-Resident Agriculture Permit";
	private final static String PAY_ACCT_UPDS =
		"Payment Account Updates";
	private final static String PLATE_OPTS = "Plate Options";
	private final static String PRINT_IMMED = "Print Immediate";

	public final static int PRINT_IMMEDIATE_NEXT = 2;
	public final static int PRINT_IMMEDIATE_OFF = 0;
	public final static int PRINT_IMMEDIATE_ON = 1;
	private final static String PROCESS_HOLD = "Process HOLD";
	private final static String PROCESS_NEW = "Process NEW";
	private final static String PROFILE = "Profile";
	private final static String PROFILE_RPT = "Profile Report";
	private final static String PUBLISH_RPT = "Publishing Report";
	private final static String PUBLISH_UPD = "Publishing Update";
	private final static String REC_RETRIEV = "Record Retrieval";
	private final static String RECEIVE_INVOICE = "Receive Invoice";
	private final static String REFUND = "Refund";
	private final static String REG_COLLECT = "Regional Collection";
	private final static String REG_ONLY = "Registration Only";
	private final static String RENEW = "Renewal";
	private final static String REPL = "Replacement";
	private final static String REPORTS = "Reports";
	private final static String REPRNT_RCPT = "Reprint Receipt";
	private final static String REPRNT_RPTS = "Reprint Reports";
	private final static String REPRNT_STKR_RPT =
		"Reprint Sticker Report";
	// end defect 9085 

	// defect 8379 	
	private final static String RERUN_CNTYWIDE = "Rerun Countywide";
	// end defect 8379 
	private final static String RERUN_SUBSUM =
		"Rerun Substation Summary";
	// defect 8379 	
	private final static String RESET_CLSOUT_INDI =
		"Reset Closeout Indicator";
	// end defect 8379 	
	private final static String RSPS_STAT_UPD = "RSPS Status Updates";
	private final static String RTS_DESKTOP = "RTSDeskTop";
	private final static String RTS_VER = "RTS Version";
	private final static String SALESTAX_ALLOC_RPT =
		"Sales Tax Allocation Report";
	// defect 9642 
	private final static String SALVAGE_COA = "Salvage/COA";
	// end defect 9642 
	private final static String SEARCH = "Search";
	private final static String SECUR_CHG_RPT =
		"Security Change Report";
	private final static String SECUR_RPTS = "Security Reports";
	private final static String SECURITY = "Security";
	private final static String SET_PRT_DEST = "Set Print Destination";

	// defect 9085 
	private final static String SPCL_PLT_APP = "Application";
	private final static String SPCL_PLT_DEL = "Delete";
	private final static String SPCL_PLT_RENEW = "Renew Plate Only";
	private final static String SPCL_PLT_RESERVE = "Reserve";
	private final static String SPCL_PLT_REVISE = "Revise";
	private final static String SPCL_PLT_RPTS = "Reports";
	private final static String SPCL_PLT_UNACC = "Unacceptable";
	private final static String SRVRPLUS_DISABLD =
		"ServerPlus Disabled";
	private final static String SRVRPLUS_ENABLD = "ServerPlus Enabled";
	private final static String STAT_CHG = "Status Change";
	private final static String SUBCON_RENEW = "Subcontractor Renewal";
	private final static String SUBCON_RPT = "Subcontractor Report";
	private final static String SUBCON_UPDS = "Subcontractor Updates";
	private final static String SUPER_OVERRIDE = "Supervisor Override";
	private final static String TEMP_ADDL_WT =
		"Temporary Additional Weight";
	private final static String TIMED_PRMT = "Timed Permit";

	private final static String TOW_TRUCK = "Tow Truck";
	private final static String TTL_APPL = "Title Application";
	private final static String TTL_PKG_RPT = "Title Package Report";
	private final static String TTL_REG = "Title/Registration";
	private final static String VEH_INFO = "Vehicle Information";
	// defect 9117
	private final static String VI_REJECTION_RPT =
		"PLP Request Rejection Report";
	private final static String VOID_TRANS = "Void Transaction";
	// defect 10733 
	private final static String WEBAGENT = "WebAgent";
	// end defect 10733 
	private final static String WORKSTATION = "WORKSTATION";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			RTSDeskTop laRTSDeskTop;
			laRTSDeskTop = new RTSDeskTop();
			laRTSDeskTop
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laRTSDeskTop.show();
			java.awt.Insets laInsets = laRTSDeskTop.getInsets();
			laRTSDeskTop.setSize(
				laRTSDeskTop.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laRTSDeskTop.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laRTSDeskTop.setVisible(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
		}
	}
	private java.util.Vector actionComponents;
	private javax.swing.ButtonGroup bgPrintImmediate;
	private javax.swing.ButtonGroup bgServerPlus;

	// defect 9642 
	// private JMenuItem ivjmenuItemCOA = null;
	// private final static String COA = "COA";
	// end defect 9642

	// Object 
	private SecurityData caSecurityData;

	// boolean
	private boolean cbCreditCheck = false;
	private boolean cbDealerCheck = false;
	private boolean cbLienholderCheck = false;
	private boolean cbServer = false;
	private boolean cbSharedCashDrawer = false;
	private boolean cbSubconCheck = false;
	private boolean cbTreatAsServer = false;

	// int
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciWsId;
	private JButton ivjbtnInternet = null;
	private JPanel ivjimagePanel = null;
	private JPanel ivjJDesktopPane1 = null;
	private JPanel ivjJFrameContentPane = null;
	private JLabel ivjJLabel1 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel6 = null;
	private JSeparator ivjJSeparator1 = null;
	private JSeparator ivjJSeparator10 = null;
	private JSeparator ivjJSeparator11 = null;
	private JSeparator ivjJSeparator12 = null;
	private JSeparator ivjJSeparator13 = null;
	private JSeparator ivjJSeparator14 = null;
	private JSeparator ivjJSeparator15 = null;
	private JSeparator ivjJSeparator18 = null;
	private JSeparator ivjJSeparator19 = null;
	private JSeparator ivjJSeparator2 = null;
	private JSeparator ivjJSeparator20 = null;
	private JSeparator ivjJSeparator21 = null;
	private JSeparator ivjJSeparator22 = null;
	private JSeparator ivjJSeparator23 = null;
	private JSeparator ivjJSeparator24 = null;
	private JSeparator ivjJSeparator25 = null;
	private JSeparator ivjJSeparator26 = null;
	// defect 10733 
	private JSeparator ivjJSeparator27 = null;
	// end defect 10733 
	private JSeparator ivjJSeparator3 = null;
	private JSeparator ivjJSeparator5 = null;
	private JSeparator ivjJSeparator7 = null;
	private JSeparator ivjJSeparator8 = null;
	private JSeparator ivjJSeparator9 = null;
	// defect 10900
	//private JSeparator ivjJSeparatorReports0 = null;
	//private JSeparator ivjJSeparatorReports1 = null;
	// end defect 10900
	private JSeparator ivjJSeparatorSpecialPlate0 = null;
	private JSeparator ivjJSeparatorSpecialPlate1 = null;
	private JSeparator ivjJSeparatorSpecialPlate2 = null;
	private JLabel ivjlblCountyString = null;
	private JLabel ivjlblDataServer = null;
	private JLabel ivjlblDate = null;
	private JLabel ivjlblPrintImmediate = null;
	private JLabel ivjlblRecordRetrieval = null;
	private JMenu ivjmenuAccounting = null;
	private JMenu ivjmenuAdminFunctions = null;
	private JMenu ivjmenuCashDrawerOperations = null;
	private JMenu ivjmenuCustomer = null;

	// defect 10701 
	private JMenuItem ivjmenuItemBatchRptMgmt = null;
	// end defect 10701 

	// defect 10250 
	private JMenu ivjmenuCertifiedLienholderReport = null;
	private JMenu ivjmenuDealerReport = null;
	private JMenu ivjmenuLienholderReport = null;
	private JMenu ivjmenuSubcontractorReport = null;

	private JMenuItem ivjmenuItemCertifiedLienhldrRptIdSort = null;
	private JMenuItem ivjmenuItemCertifiedRptNameSort = null;
	private JMenuItem ivjmenuItemDealerRptIdSort = null;
	private JMenuItem ivjmenuItemDealerRptNameSort = null;
	private JMenuItem ivjmenuItemLienholderReportIdSort = null;
	private JMenuItem ivjmenuItemLienholderReportNameSort = null;
	private JMenuItem ivjmenuItemSubcontractorReportIdSort = null;
	private JMenuItem ivjmenuItemSubcontractorReportNameSort = null;
	// end defect 10250 

	private JMenu ivjMenuDisabledPlacard = null;
	private JMenu ivjmenuFunds = null;
	private JMenu ivjmenuFundsManagement = null;
	private JMenu ivjmenuInquiry = null;
	private JMenu ivjmenuInternetRenewal = null;
	private JMenu ivjmenuInventory = null;
	private JMenuItem ivjmenuItemAdditionalCollections = null;
	private JMenuItem ivjmenuItemAdditionalSalesTax = null;
	private JMenuItem ivjmenuItemAddressChange = null;
	private JMenuItem ivjmenuItemAllocate = null;
	private JMenuItem ivjmenuItemCCOCCDO = null;
	private JMenuItem ivjmenuItemCloseOutDay = null;
	private JMenuItem ivjmenuItemCloseOutStats = null;
	private JMenuItem ivjmenuItemCompleteVehicleTransaction = null;
	private JMenuItem ivjmenuItemCorrectTitleRejection = null;
	private JMenuItem ivjmenuItemCountyFundsRemitance = null;
	private JMenuItem ivjmenuItemCreditCard = null;
	private JMenuItem ivjmenuItemCurrentStatus = null;
	private JMenuItem ivjmenuItemDealerTitles = null;
	private JMenuItem ivjmenuItemDealerUpdates = null;
	private JMenuItem ivjmenuItemDeductHotCheck = null;
	private JMenuItem ivjmenuItemDelete = null;
	private JMenuItem ivjmenuItemDeleteTitle = null;
	private JMenuItem ivjmenuItemDetailReports = null;
	private JMenuItem ivjmenuItemDriverEd = null;
	private JMenuItem ivjmenuItemDuplicateReceipt = null;
	private JMenuItem ivjmenuItemElectronicTitleReport = null;
	private JMenuItem ivjmenuItemEmployeeSecurity = null;
	private JMenuItem ivjmenuItemEmployeeSecurityReport = null;
	private JMenuItem ivjmenuItemEventSecurityReport = null;
	private JMenuItem ivjmenuItemExchange = null;
	private JMenuItem ivjmenuItemExemptAuditReport = null;
	private JMenuItem ivjmenuItemFundsBalanceReport = null;
	private JMenuItem ivjmenuItemFundsInquiry = null;
	private JMenuItem ivjmenuItemHoldRelease = null;
	private JMenuItem ivjmenuItemHotCheckCredit = null;
	private JMenuItem ivjmenuItemHotCheckRedeemed = null;
	private JMenuItem ivjmenuItemInquiry = null;
	private JMenuItem ivjmenuItemInventoryActionReport = null;
	private JMenuItem ivjmenuItemInventoryHistory = null;
	private JMenuItem ivjmenuItemItemSeized = null;
	private JMenuItem ivjmenuItemLienholderUpdates = null;
	private JMenuItem ivjmenuItemLogon = null;
	private JMenuItem ivjmenuItemModify = null;
	private JRadioButtonMenuItem ivjmenuItemNextCustomer = null;
	private JMenuItem ivjmenuItemNonResidentAgPermit = null;
	private JRadioButtonMenuItem ivjmenuItemOff = null;
	private JRadioButtonMenuItem ivjmenuItemOn = null;
	private JMenuItem ivjmenuItemPaymentAccountUpdates = null;
	private JMenuItem ivjmenuItemPlacardInquiry = null;
	private JMenuItem ivjmenuItemPlacardManagement = null;
	private JMenuItem ivjmenuItemPlacardReport = null;
	private JMenuItem ivjmenuItemPlateOptions = null;
	private JMenuItem ivjmenuItemProcessHold = null;
	private JMenuItem ivjmenuItemProcessNew = null;
	private JMenuItem ivjmenuItemProfile = null;
	private JMenuItem ivjmenuItemProfileReport = null;
	private JMenuItem ivjmenuItemPublishingReport = null;
	private JMenuItem ivjmenuItemPublishingUpdate = null;
	private JMenuItem ivjmenuItemReceiveInvoice = null;
	private JMenuItem ivjmenuItemRefund = null;
	private JMenuItem ivjmenuItemRegionalCollection = null;
	// defect 10900
	private JMenuItem ivjmenuItemSuspectedFraudReport = null;
	// end defect 10900
	private JMenuItem ivjmenuItemRenewal = null;
	private JMenuItem ivjmenuItemReplacement = null;
	private JMenuItem ivjmenuItemReports = null;
	// defect 10461
	private JMenuItem ivjmenuItemNICUSA = null;
	private JMenuItem ivjmenuItemMyPlates = null;
	// end defect 10461
	// defect 10491 
	private JMenu ivjMenuTimedPermit = null;
	private JMenuItem ivjmenuItemPermitApplication = null;
	private JMenuItem ivjmenuItemPermitInquiry = null;
	private JMenuItem ivjmenuItemPermitDuplicateReceipt = null;
	// end defect 10491 

	// defect 10844 
	private JMenuItem ivjmenuItemPermitModify = null;
	// end defect 10844 

	private JMenuItem ivjmenuItemReprintReceipt = null;
	private JMenuItem ivjmenuItemReprintReports = null;
	private JMenuItem ivjmenuItemReprintStickerReport = null;
	private JMenuItem ivjmenuItemRerunCountyWide = null;
	private JMenuItem ivjmenuItemRerunSubstation = null;
	private JMenuItem ivjmenuItemResetClose = null;
	private JMenuItem ivjmenuItemRSPSUpdates = null;
	private JMenuItem ivjmenuItemSalesTax = null;
	private JMenuItem ivjmenuItemSalvage = null;
	private JMenuItem ivjmenuItemSearch = null;
	private JMenuItem ivjmenuItemSecurityChangeReport = null;
	private JRadioButtonMenuItem ivjmenuItemServerPlusDisabled = null;
	private JRadioButtonMenuItem ivjmenuItemServerPlusEnabled = null;
	private JMenuItem ivjmenuItemSetPrintDestination = null;
	private JMenuItem ivjmenuItemSpecialPlatesApplication = null;
	private JMenuItem ivjmenuItemSpecialPlatesDelete = null;
	// defect 10700 
	private JMenuItem ivjmenuItemSpecialPlatesDuplicatePermit = null;
	// end defect 10700 
	// defect 10820
	private JMenuItem ivjmenuItemSpecialPlatesInquiry = null;
	// end defect 10820 
	private JMenuItem ivjmenuItemSpecialPlatesRenewPlateOnly = null;
	private JMenuItem ivjmenuItemSpecialPlatesReports = null;
	private JMenuItem ivjmenuItemSpecialPlatesReserve = null;
	private JMenuItem ivjmenuItemSpecialPlatesRevise = null;
	private JMenuItem ivjmenuItemSpecialPlatesUnacceptable = null;
	private JMenuItem ivjmenuItemStatusChange = null;
	private JMenuItem ivjmenuItemSubcontractorRenewal = null;
	private JMenuItem ivjmenuItemSubcontractorUpdates = null;
	private JMenuItem ivjmenuItemSupervisorOverride = null;
	private JMenuItem ivjmenuItemTempAdditionalWeight = null;
	private JMenuItem ivjmenuItemTitleApplication = null;
	private JMenuItem ivjmenuItemTitlePackageReport = null;
	private JMenuItem ivjmenuItemTowTruck = null;
	private JMenuItem ivjmenuItemVehicleInformation = null;
	private JMenuItem ivjmenuItemVIRejectionReport = null;
	// defect 10733 
	private JMenuItem ivjmenuItemWebAgent = null;
	// end defect 10733
	private JMenuItem ivjmenuItemVoidTransaction = null;
	private JMenu ivjmenuLocalOptions = null;
	private JMenu ivjmenuLogon = null;
	private JMenu ivjmenuMisc = null;
	private JMenu ivjmenuMiscRegistration = null;
	private JMenu ivjmenuPrintImmediate = null;
	private JMenu ivjmenuRegistrationOnly = null;
	private JMenu ivjmenuReports = null;
	private JMenu ivjmenuSecurity = null;
	private JMenu ivjmenuSecurityReports = null;
	private JMenu ivjmenuSpecialPlates = null;
	private JMenu ivjmenuTitleRegistration = null;
	private PendingTransPanel ivjpendingPanel = null;
	private JMenuBar ivjRTSDeskTopJMenuBar = null;
	// Task 42 - Add the Dynamic Help menu
	private RTSDynamicMenu ivjRTSDynamicMenuHelp = null;
	private JLabel ivjstcLblDataServer = null;
	private JLabel ivjstcLblPrintImmediate = null;
	private JLabel ivjstcLblRecordRetrieval = null;
	private JLabel ivjstcLblVersion = null;
	private RadioMenuActionListener radioMenuActionListener;

	/**
	 * RTSDeskTop constructor comment.
	 */
	public RTSDeskTop()
	{
		super();
		initialize();
	}
	/**
	 * RTSDeskTop constructor comment.
	 * 
	 * @param asTitle String
	 */
	public RTSDeskTop(String asTitle)
	{
		super(asTitle);
	}

	/**
	 * Handle adding of action listener.
	 *  
	 * @param aaAL ActionListener
	 */
	public void addActionListener(ActionListener aaAL)
	{
		int liSize = actionComponents.size();
		for (int i = 0; i < liSize; i++)
		{
			AbstractButton laAB =
				(AbstractButton) actionComponents.get(i);
			laAB.addActionListener(aaAL);
		}
	}
	/**
	 * 
	 * Assign booleans to be used in enable LocalOptionsMenu 
	 * to determine if entity updateable from this location 
	 * 
	 */
	private void assignLocalOptionMenuFlags(boolean abDBStatus)
	{
		cbDealerCheck = false;
		cbLienholderCheck = false;
		cbSubconCheck = false;
		cbCreditCheck = false;

		boolean lbDealerUpdateable = false;
		boolean lbLienholderUpdateable = false;
		boolean lbSubconUpdateable = false;
		boolean lbCreditUpdateable = false;

		java.util.Vector lvSubStaList =
			SubstationSubscriptionCache.getSubstaSubscr(
				ciOfcIssuanceNo,
				ciSubstaId);

		// Main Office always has authority to update
		if (isMainOffice())
		{
			lbDealerUpdateable = true;
			lbLienholderUpdateable = true;
			lbSubconUpdateable = true;
			lbCreditUpdateable = true;
		}
		// Not Main Office & No Subscriptions
		else if (
			(lvSubStaList == null || lvSubStaList.size() == 0)
				&& !isMainOffice())
		{
			lbDealerUpdateable = false;
			lbLienholderUpdateable = false;
			lbSubconUpdateable = false;
			lbCreditUpdateable = false;
		}
		// Substation has authorization to update something 
		else
		{
			// Substations can update Dealer/Subcon/Lienholder/Credit
			// if so specified in Substation Subscription Table 

			for (int i = 0; i < lvSubStaList.size(); i++)
			{
				SubstationSubscriptionData laSubData =
					(SubstationSubscriptionData) lvSubStaList.get(i);

				// Dealer 
				if (laSubData
					.getTblName()
					.equals(SubstationSubscriptionCache.DEALER))
				{
					lbDealerUpdateable =
						int2bool(laSubData.getTblUpdtIndi());
				}
				// Lienholder
				else if (
					laSubData.getTblName().equals(
						SubstationSubscriptionCache.LIENHOLDER))
				{
					lbLienholderUpdateable =
						int2bool(laSubData.getTblUpdtIndi());
				}
				// Subcon 
				else if (
					laSubData.getTblName().equals(
						SubstationSubscriptionCache.SUBCON))
				{
					lbSubconUpdateable =
						int2bool(laSubData.getTblUpdtIndi());
				}
				// Credit Card Fee 
				else if (
					laSubData.getTblName().equals(
						SubstationSubscriptionCache.CREDIT))
				{
					lbCreditUpdateable =
						int2bool(laSubData.getTblUpdtIndi());
				}
			}
		}
		// If ServerPlus is On, workstation can process those events
		// previously designated as ServerOnly 

		cbDealerCheck =
			lbDealerUpdateable && cbTreatAsServer && abDBStatus;

		cbLienholderCheck =
			lbLienholderUpdateable && cbTreatAsServer && abDBStatus;

		cbSubconCheck =
			lbSubconUpdateable && cbTreatAsServer && abDBStatus;

		cbCreditCheck =
			lbCreditUpdateable && cbTreatAsServer && abDBStatus;
	}
	/**
	 * 
	 * Disable all Menu Items  
	 *
	 */

	private void disableMenusBeforeLogon()
	{
		getmenuAccounting().setEnabled(false);
		getmenuAdminFunctions().setEnabled(false);
		getmenuCashDrawerOperations().setEnabled(false);
		getmenuCustomer().setEnabled(false);
		getmenuFunds().setEnabled(false);
		getmenuFundsManagement().setEnabled(false);
		getmenuInquiry().setEnabled(false);
		getmenuInternetRenewal().setEnabled(false);
		getmenuInventory().setEnabled(false);
		getmenuLocalOptions().setEnabled(false);
		getmenuLogon().setEnabled(true);
		getmenuMisc().setEnabled(false);
		getmenuMiscRegistration().setEnabled(false);
		// defect 9085 
		getmenuSpecialPlates().setEnabled(false);
		// end defect 9085 
		getmenuPrintImmediate().setEnabled(false);
		getmenuRegistrationOnly().setEnabled(false);
		getmenuReports().setEnabled(false);
		getmenuSecurity().setEnabled(false);
		getmenuSecurityReports().setEnabled(false);
		getmenuTitleRegistration().setEnabled(false);
		// defect 8900
		// Task 42 - Add menu help
		getmenuHelp().setEnabled(false);
		// end defect 8900
	}

	/**
	 * 
	 * Method description
	 * 
	 * @param aaSecData SecurityData
	 */
	private void enableAccountingMenu(SecurityData aaSecData)
	{
		// ACCOUNTING
		getmenuAccounting().setEnabled(
			int2bool(aaSecData.getAcctAccs()));

		// FNDREM
		getmenuItemCountyFundsRemitance().setEnabled(
			int2bool(aaSecData.getFundsRemitAccs()));

		// FNDINQ
		getmenuItemFundsInquiry().setEnabled(
			int2bool(aaSecData.getFundsInqAccs()));

		// REFUND // RFCASH 
		getmenuItemRefund().setEnabled(
			int2bool(aaSecData.getRefAccs()));

		// HOTCK
		getmenuItemHotCheckCredit().setEnabled(
			int2bool(aaSecData.getHotCkCrdtAccs()));

		// CKREDM
		getmenuItemHotCheckRedeemed().setEnabled(
			int2bool(aaSecData.getHotCkRedemdAccs()));

		// HOTDED
		getmenuItemDeductHotCheck().setEnabled(
			int2bool(aaSecData.getModfyHotCkAccs()));

		// HCKITM
		getmenuItemItemSeized().setEnabled(
			int2bool(aaSecData.getItmSeizdAccs()));

		// ADLCOL
		getmenuItemAdditionalCollections().setEnabled(
			SystemProperty.isCounty()
				&& int2bool(aaSecData.getFundsAdjAccs()));

		// RGNCOL
		getmenuItemRegionalCollection().setEnabled(
			SystemProperty.isRegion()
				&& int2bool(aaSecData.getRegnlColltnAccs()));
	}

	/**
	 * 
	 * Enable Accounting Menu in More Trans
	 * 
	 */
	private void enableAccountingMenuOnPendingTrans()
	{
		// FNDREM 				
		getmenuItemCountyFundsRemitance().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.FNDREM)
				&& getmenuItemCountyFundsRemitance().isEnabled());

		// FNDINQ 
		getmenuItemFundsInquiry().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.FNDINQ)
				&& getmenuItemFundsInquiry().isEnabled());

		// REFUND && RFCASH				
		getmenuItemRefund().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.REFUND)
				&& getmenuItemRefund().isEnabled());

		// HOTCK				
		getmenuItemHotCheckCredit().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.HOTCK)
				&& getmenuItemHotCheckCredit().isEnabled());

		// CKREDM 
		getmenuItemHotCheckRedeemed().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.CKREDM)
				&& getmenuItemHotCheckRedeemed().isEnabled());

		// HOTDED 
		getmenuItemDeductHotCheck().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.HOTDED)
				&& getmenuItemDeductHotCheck().isEnabled());

		//	HOTITM
		getmenuItemItemSeized().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.HCKITM)
				&& getmenuItemItemSeized().isEnabled());

		// ADLCOL 	
		getmenuItemAdditionalCollections().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.ADLCOL)
				&& getmenuItemAdditionalCollections().isEnabled());

		// RGNCOL 		
		getmenuItemRegionalCollection().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.RGNCOL)
				&& getmenuItemRegionalCollection().isEnabled());

	}
	/**
	 * Enable Customer Menu
	 *  
	 * @param aaSecData SecurityData
	 * @param abDBStatus boolean  
	 */
	private void enableCustomerMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		getmenuCustomer().setEnabled(
			int2bool(aaSecData.getCustServAccs()));

		// REGISTRATION  		
		getmenuRegistrationOnly().setEnabled(
			int2bool(aaSecData.getRegOnlyAccs()));

		// IRENEW  	
		// defect 10387
		// Check for isCounty() to be removed w/ defect 10388
		// after 6.4.0  

		boolean lbEnable =
			SystemProperty.isCounty()
				&& abDBStatus
				&& int2bool(aaSecData.getItrntRenwlAccs());

		getbtnInternet().setEnabled(lbEnable);
		getmenuInternetRenewal().setEnabled(lbEnable);
		getmenuItemProcessNew().setEnabled(lbEnable);
		getmenuItemProcessHold().setEnabled(lbEnable);
		getmenuItemSearch().setEnabled(lbEnable);
		getmenuItemReports().setEnabled(lbEnable);
		// end defect 10387
		// defect 10461 
		getmenuItemNICUSA().setEnabled(
			int2bool(aaSecData.getItrntRenwlAccs()));
		// end defect 10461 

		// RENEW
		getmenuItemRenewal().setEnabled(
			int2bool(aaSecData.getRenwlAccs()));

		// DUPL	
		getmenuItemDuplicateReceipt().setEnabled(
			int2bool(aaSecData.getDuplAccs()));

		// EXCH	
		getmenuItemExchange().setEnabled(
			int2bool(aaSecData.getExchAccs()));

		// REPL	
		getmenuItemReplacement().setEnabled(
			int2bool(aaSecData.getReplAccs()));

		// CORREG	
		getmenuItemModify().setEnabled(
			int2bool(aaSecData.getModfyAccs()));

		// WEBAGENT
		// defect 10733 
		getmenuItemWebAgent().setEnabled(
			int2bool(aaSecData.getWebAgntAccs()));
		// end defect 10733 

		// SBRNW	
		getmenuItemSubcontractorRenewal().setEnabled(
			int2bool(aaSecData.getSubconRenwlAccs()));

		// ADDR	
		getmenuItemAddressChange().setEnabled(
			int2bool(aaSecData.getAddrChngAccs()));

		// DRVED	
		// defect 8900
		//getmenuItemDriverEd().setEnabled(
		//	isHQ() && int2bool(aaSecData.getIssueDrvsEdAccs()));
		getmenuItemDriverEd().setEnabled(
			int2bool(aaSecData.getIssueDrvsEdAccs()));
		// end defect 8900

		// TITLE & REG  
		getmenuTitleRegistration().setEnabled(
			int2bool(aaSecData.getTtlRegAccs()));
		// TITLE	
		getmenuItemTitleApplication().setEnabled(
			int2bool(aaSecData.getTtlApplAccs()));

		// REJCOR
		getmenuItemCorrectTitleRejection().setEnabled(
			int2bool(aaSecData.getCorrTtlRejAccs()));

		// DTAORD	
		getmenuItemDealerTitles().setEnabled(
			int2bool(aaSecData.getDlrTtlAccs()));

		// CCO	
		getmenuItemCCOCCDO().setEnabled(
			int2bool(aaSecData.getCCOAccs()));

		// defect 9642 
		// COA	
		//getmenuItemCOA().setEnabled(int2bool(aaSecData.getCOAAccs()));
		// end defect 9642 

		// defect 9701 
		// Added check for HQ for 5.7.0 transition 
		// New:  Unavailable to Regions
		// SLVG
		getmenuItemSalvage().setEnabled(
			SystemProperty.isHQ() && int2bool(aaSecData.getSalvAccs()));
		// end defect 9701 

		// DELTIP	
		getmenuItemDeleteTitle().setEnabled(
			abDBStatus && int2bool(aaSecData.getDelTtlInProcsAccs()));

		// STAT	
		getmenuItemStatusChange().setEnabled(
			int2bool(aaSecData.getStatusChngAccs()));

		// ADLSTX 
		getmenuItemAdditionalSalesTax().setEnabled(
			int2bool(aaSecData.getAdjSalesTaxAccs()));

		// INQUIRY  
		getmenuInquiry().setEnabled(int2bool(aaSecData.getInqAccs()));

		// VEHINQ 
		getmenuItemVehicleInformation().setEnabled(
			int2bool(aaSecData.getInqAccs()));

		// Plate Options	
		getmenuItemPlateOptions().setEnabled(
			int2bool(aaSecData.getInqAccs()));

		// MISC REG  
		getmenuMiscRegistration().setEnabled(
			int2bool(aaSecData.getMiscRegAccs()));

		// 	144PT, etc.
		// defect 10491 
		getmenuTimedPermit().setEnabled(
			int2bool(aaSecData.getTimedPrmtAccs()));
		getmenuItemPermitInquiry().setEnabled(
			int2bool(aaSecData.getTimedPrmtAccs()));
		getmenuItemPermitApplication().setEnabled(
			int2bool(aaSecData.getTimedPrmtAccs()));

		// defect 10844 
		getmenuItemPermitModify().setEnabled(
			int2bool(aaSecData.getModfyTimedPrmtAccs()));
		// end defect 10844

		getmenuItemPermitInquiry().setEnabled(
			int2bool(aaSecData.getTimedPrmtAccs()));
		// end defect 10491 

		// defect 9831  
		// NEW DISABLED PLACARD 
		// Not available in DB Server Down  	
		getmenuDisabledPlacard().setEnabled(
			abDBStatus
				&& (int2bool(aaSecData.getDsabldPersnAccs())
					|| int2bool(aaSecData.getDsabldPlcrdRptAccs())
					|| int2bool(aaSecData.getDsabldPlcrdInqAccs())));

		getmenuItemDisabledPlacardInquiry().setEnabled(
			abDBStatus && int2bool(aaSecData.getDsabldPlcrdInqAccs()));

		getmenuItemDisabledPlacardManagement().setEnabled(
			abDBStatus && int2bool(aaSecData.getDsabldPersnAccs()));

		getmenuItemDisabledPlacardReport().setEnabled(
			abDBStatus && int2bool(aaSecData.getDsabldPlcrdRptAccs()));
		// end defect 9831 

		// TAWPT	
		getmenuItemTempAdditionalWeight().setEnabled(
			int2bool(aaSecData.getTempAddlWtAccs()));

		// NROPT	
		getmenuItemNonResidentAgPermit().setEnabled(
			int2bool(aaSecData.getNonResPrmtAccs()));

		// TOWP 	
		getmenuItemTowTruck().setEnabled(
			int2bool(aaSecData.getTowTrkAccs()));

		// defect 9085
		// SPECIAL PLATES 
		// Not available in DB Server Down

		// defect 10700 
		// Special Plates menu item must now be available whenever 
		// the DB is available 
		getmenuSpecialPlates().setEnabled(
		//abDBStatus && int2bool(aaSecData.getSpclPltAccs()));
		abDBStatus);
		// end defect 10700 

		// SPAPPL
		getmenuItemSpecialPlatesApplication().setEnabled(
			abDBStatus && int2bool(aaSecData.getSpclPltApplAccs()));

		// defect 10461 
		getmenuItemMyPlates().setEnabled(
			int2bool(aaSecData.getSpclPltApplAccs()));
		// end defect 10461 

		// SPRNW 
		getmenuItemSpecialPlatesRenewPlateOnly().setEnabled(
			abDBStatus && int2bool(aaSecData.getSpclPltRenwPltAccs()));

		// SPREV		
		getmenuItemSpecialPlatesRevise().setEnabled(
			abDBStatus
				&& int2bool(aaSecData.getSpclPltRevisePltAccs()));

		// defect 10700 
		// DPSPPT 
		getmenuItemSpecialPlatesDuplicatePermit().setEnabled(
			abDBStatus);
		// end defect 10700 

		// defect 10820
		// special plates inquiry - temporary trans code 
		// SPINQ
		getmenuItemSpecialPlatesInquiry().setEnabled(
			int2bool(aaSecData.getInqAccs())
				&& int2bool(aaSecData.getPltNoAccs()));
		// end defect 10820 

		// SPRSRV				
		getmenuItemSpecialPlatesReserve().setEnabled(
			SystemProperty.isHQ()
				&& abDBStatus
				&& int2bool(aaSecData.getSpclPltResrvPltAccs()));

		// SPUNAC			
		getmenuItemSpecialPlatesUnacceptable().setEnabled(
			SystemProperty.isHQ()
				&& abDBStatus
				&& int2bool(aaSecData.getSpclPltUnAccptblPltAccs()));

		// SPDEL		
		getmenuItemSpecialPlatesDelete().setEnabled(
			SystemProperty.isHQ()
				&& abDBStatus
				&& int2bool(aaSecData.getSpclPltDelPltAccs()));

		// Special Plates Reports 
		getmenuItemSpecialPlatesReports().setEnabled(
			abDBStatus && int2bool(aaSecData.getSpclPltRptsAccs()));
		// end defect 9085 

	}

	/**
	 * 
	 * Enable Customer Menu in More Trans 
	 * 
	 */
	private void enableCustomerMenuOnPendingTrans()
	{
		// IRENEW 
		getmenuInternetRenewal().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.IRENEW)
				&& getmenuInternetRenewal().isEnabled());

		// RENEW 
		getmenuItemRenewal().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.RENEW)
				&& getmenuItemRenewal().isEnabled());

		// DUPL 
		getmenuItemDuplicateReceipt().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.DUPL)
				&& getmenuItemDuplicateReceipt().isEnabled());

		// EXCH 
		getmenuItemExchange().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.EXCH)
				&& getmenuItemExchange().isEnabled());

		// REPL 
		getmenuItemReplacement().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.REPL)
				&& getmenuItemReplacement().isEnabled());

		// CORREG
		getmenuItemModify().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.CORREG)
				&& getmenuItemModify().isEnabled());

		// WEBAGENT 
		// No Action Required 

		// SBRNW		
		getmenuItemSubcontractorRenewal().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.SBRNW)
				&& getmenuItemSubcontractorRenewal().isEnabled());

		// ADDR 		
		getmenuItemAddressChange().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.ADDR)
				&& getmenuItemAddressChange().isEnabled());

		// defect 8900
		// Handle in more trans.
		// DRVED		
		getmenuItemDriverEd().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.DRVED)
				&& getmenuItemDriverEd().isEnabled());
		// end defect 8900

		// TITLE   
		getmenuItemTitleApplication().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.TITLE)
				&& getmenuItemTitleApplication().isEnabled());

		// REJCOR 
		getmenuItemCorrectTitleRejection().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.REJCOR)
				&& getmenuItemCorrectTitleRejection().isEnabled());

		// DTAORK 		
		getmenuItemDealerTitles().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.DTAORK)
				&& getmenuItemDealerTitles().isEnabled());

		// CCO 
		getmenuItemCCOCCDO().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.CCO)
				&& getmenuItemCCOCCDO().isEnabled());

		// defect 9642 
		// COA
		//		getmenuItemCOA().setEnabled(
		//			isEnabledInMoreTrans(TransCdConstant.COA)
		//				&& getmenuItemCOA().isEnabled());
		// endable 9642 

		// SLVG 
		getmenuItemSalvage().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.SCOT)
				&& getmenuItemSalvage().isEnabled());

		// DELTIP 
		getmenuItemDeleteTitle().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.DELTIP)
				&& getmenuItemDeleteTitle().isEnabled());

		// STATRF 	
		getmenuItemStatusChange().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.STATRF)
				&& getmenuItemStatusChange().isEnabled());

		// ADLSTX 	
		getmenuItemAdditionalSalesTax().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.ADLSTX)
				&& getmenuItemAdditionalSalesTax().isEnabled());

		// VEHINQ 	
		getmenuItemVehicleInformation().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.VEHINQ)
				&& getmenuItemVehicleInformation().isEnabled());

		// TIMED PERMIT - Inquiry, Application, Duplicate Receipt 	
		getmenuTimedPermit().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.PT144)
				&& getmenuTimedPermit().isEnabled());

		getmenuItemPermitInquiry().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.PT144)
				&& getmenuItemPermitInquiry().isEnabled());

		getmenuItemPermitApplication().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.PT144)
				&& getmenuItemPermitApplication().isEnabled());

		// defect 10844 
		getmenuItemPermitModify().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.MODPT)
				&& getmenuItemPermitModify().isEnabled());
		// end defect 10844 

		getmenuItemPermitDuplicateReceipt().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.PRMDUP)
				&& getmenuItemPermitDuplicateReceipt().isEnabled());
		// end defect 10491

		boolean lbDBStatus = RTSApplicationController.isDBReady();

		// DISABLED PLACARD INQ / MGMT / RPT
		// defect 10133   
		// Use new TransCds 
		// defect 9831 
		getmenuDisabledPlacard().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.PDC)
				&& getmenuDisabledPlacard().isEnabled());

		getmenuItemDisabledPlacardInquiry().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.PDC)
				&& getmenuItemDisabledPlacardManagement().isEnabled());

		getmenuItemDisabledPlacardManagement().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.PDC)
				&& getmenuItemDisabledPlacardManagement().isEnabled());

		getmenuItemDisabledPlacardReport().setEnabled(false);
		// end defect 9831 
		// end defect 10133 

		// TAWPT 
		getmenuItemTempAdditionalWeight().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.TAWPT)
				&& getmenuItemTempAdditionalWeight().isEnabled());

		// NROPT
		getmenuItemNonResidentAgPermit().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.NROPT)
				&& getmenuItemNonResidentAgPermit().isEnabled());

		// TOWP
		getmenuItemTowTruck().setEnabled(
			isEnabledInMoreTrans(TransCdConstant.TOWP)
				&& getmenuItemTowTruck().isEnabled());

		// defect 9085
		boolean lbSpclPlatesEnable =
			isEnabledInMoreTrans(TransCdConstant.SPAPPL)
				|| isEnabledInMoreTrans(TransCdConstant.SPRNW)
				|| isEnabledInMoreTrans(TransCdConstant.SPREV)
				|| isEnabledInMoreTrans(TransCdConstant.SPRSRV)
				|| isEnabledInMoreTrans(TransCdConstant.SPUNAC)
				|| isEnabledInMoreTrans(TransCdConstant.SPDEL);

		// Special Plates Menu 
		getmenuSpecialPlates().setEnabled(
			lbDBStatus
				&& getmenuSpecialPlates().isEnabled()
				&& lbSpclPlatesEnable);

		// SPAPPL 
		getmenuItemSpecialPlatesApplication().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPAPPL)
				&& getmenuItemSpecialPlatesApplication().isEnabled());

		// SPRNW 
		getmenuItemSpecialPlatesRenewPlateOnly().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPRNW)
				&& getmenuItemSpecialPlatesRenewPlateOnly().isEnabled());

		// SPREV 
		getmenuItemSpecialPlatesRevise().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPREV)
				&& getmenuItemSpecialPlatesRevise().isEnabled());

		// SPRSRV 
		getmenuItemSpecialPlatesReserve().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPRSRV)
				&& getmenuItemSpecialPlatesReserve().isEnabled());

		// SPUNAC 
		getmenuItemSpecialPlatesUnacceptable().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPUNAC)
				&& getmenuItemSpecialPlatesUnacceptable().isEnabled());

		// SPDEL 
		getmenuItemSpecialPlatesDelete().setEnabled(
			lbDBStatus
				&& isEnabledInMoreTrans(TransCdConstant.SPDEL)
				&& getmenuItemSpecialPlatesDelete().isEnabled());

		// Special Plates Application Reports 
		getmenuItemSpecialPlatesReports().setEnabled(false);
		// end defect 9085

	}

	/**
	 * Enable Funds Menu
	 * 
	 * @param aaSecData SecurityData
	 * @param abDBStatus boolean 
	 */
	private void enableFundsMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{

		// FUNDS  
		// defect 9653 
		// Enable Funds for HQ 
		getmenuFunds().setEnabled(abDBStatus
		//&& !SystemProperty.isHQ()
		&& (int2bool(aaSecData.getFundsBalAccs())
			|| int2bool(aaSecData.getCashOperAccs())
			|| int2bool(aaSecData.getFundsMgmtAccs())));
		// end defect 9653 

		// CASH DRAWER OPERATIONS  
		getmenuCashDrawerOperations().setEnabled(
			abDBStatus && int2bool(aaSecData.getCashOperAccs()));

		// Close Out
		getmenuItemCloseOutDay().setEnabled(
			abDBStatus
				&& int2bool(aaSecData.getCashOperAccs())
				&& (!cbSharedCashDrawer || cbServer));

		// Current Satus 		
		getmenuItemCurrentStatus().setEnabled(
			abDBStatus
				&& int2bool(aaSecData.getCashOperAccs())
				&& (!cbSharedCashDrawer || cbServer));

		// Detail Reports		
		getmenuItemDetailReports().setEnabled(
			abDBStatus && int2bool(aaSecData.getCashOperAccs()));

		// CloseOut Statistics 	
		getmenuItemCloseOutStats().setEnabled(
			int2bool(aaSecData.getCashOperAccs()));

		// FUNDS BALANCE 	
		getmenuItemFundsBalanceReport().setEnabled(
			abDBStatus && int2bool(aaSecData.getFundsBalAccs()));

		// FUNDS MANAGEMENT
		getmenuFundsManagement().setEnabled(
			abDBStatus
				&& cbTreatAsServer
				&& int2bool(aaSecData.getFundsMgmtAccs()));

		// Reset Closeout Indicator 		
		getmenuItemResetClose().setEnabled(
			cbTreatAsServer && int2bool(aaSecData.getFundsMgmtAccs()));

		// Rerun CountyWide 		
		getmenuItemRerunCountyWide().setEnabled(
			abDBStatus
				&& isMainOffice()
				&& cbTreatAsServer
				&& int2bool(aaSecData.getFundsMgmtAccs()));

		// Rerun Substation Summary 		
		getmenuItemRerunSubstation().setEnabled(
			abDBStatus
				&& cbTreatAsServer
				&& int2bool(aaSecData.getFundsMgmtAccs()));

	}

	/**
	 * 
	 * Enable Inventory Menu
	 * 
	 * @param aaSecData SecurityData
	 * @param abDBStatus boolean 
	 */
	private void enableInventoryMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		// INVENTORY 
		getmenuInventory().setEnabled(
			abDBStatus && int2bool(aaSecData.getInvAccs()));

		if (abDBStatus)
		{
			// INVALL 	
			getmenuItemAllocate().setEnabled(
				int2bool(aaSecData.getInvAllocAccs()));

			// INVRCV	
			getmenuItemReceiveInvoice().setEnabled(
				cbTreatAsServer
					&& isMainOffice()
					&& int2bool(aaSecData.getInvAckAccs()));

			// Inventory Inquiry		
			getmenuItemInquiry().setEnabled(
				int2bool(aaSecData.getInvInqAccs()));

			// Inventory Profile 	
			getmenuItemProfile().setEnabled(
				int2bool(aaSecData.getInvProfileAccs()));

			// Inventory Profile Reprt 			
			getmenuItemProfileReport().setEnabled(
				int2bool(aaSecData.getInvProfileAccs()));

			// Inventory Hold / Release 	
			getmenuItemHoldRelease().setEnabled(
				int2bool(aaSecData.getInvHldRlseAccs()));

			// INVDEL 	
			getmenuItemDelete().setEnabled(
				int2bool(aaSecData.getInvDelAccs()));

			// defect 9117
			// VI Rejection Report
			getmenuItemVIRejectionReport().setEnabled(
				SystemProperty.isHQ());
			// end defect 9117		

			// Inventory Action Report 	
			// defect 9116
			// allow action report at hq
			getmenuItemInventoryActionReport().setEnabled(
				int2bool(aaSecData.getInvActionAccs()));
			//	!isHQ() && int2bool(aaSecData.getInvActionAccs()));
			// end defect 9116

			// Inventory History Report 	
			getmenuItemInventoryHistory().setEnabled(
				SystemProperty.isRegion());
		}

	}

	/**
	 *  Enable LocalOptionsMenu
	 * 
	 * @param aaSecData SecurityData 
	 * @param abDBStatus boolean
	 */
	private void enableLocalOptionsMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		// Cannot access Local Options when DB is down except for
		// RSPS Update 
		boolean lbEnableLocalOptions =
			int2bool(aaSecData.getRSPSUpdtAccs())
				|| (int2bool(aaSecData.getLocalOptionsAccs())
					&& abDBStatus);

		getmenuLocalOptions().setEnabled(lbEnableLocalOptions);

		if (lbEnableLocalOptions)
		{
			// Set booleans for use   
			assignLocalOptionMenuFlags(abDBStatus);

			// LOCAL OPTIONS REPORTS
			// defect 10250  
			// Dealer Report
			getmenuDealerReport().setEnabled(
				abDBStatus
					&& (int2bool(aaSecData.getDlrAccs())
						|| (int2bool(aaSecData.getDlrRptAccs()))));

			// Subcontractor Report	
			getmenuSubcontractorReport().setEnabled(
				abDBStatus
					&& (int2bool(aaSecData.getSubconAccs())
						|| int2bool(aaSecData.getSubconRptAccs())));

			// Lienholder Report 	
			getmenuLienholderReport().setEnabled(
				abDBStatus
					&& (int2bool(aaSecData.getLienHldrAccs())
						|| int2bool(aaSecData.getLienHldrRptAccs())));

			getmenuCertifiedLienholderReport().setEnabled(
				abDBStatus
					&& (int2bool(aaSecData.getLienHldrAccs())
						|| int2bool(aaSecData.getLienHldrRptAccs())));
			// defect 10250

			// LOCAL OPTIONS UPDATES 
			// Dealer Updates	
			getmenuItemDealerUpdates().setEnabled(
				cbDealerCheck && int2bool(aaSecData.getDlrAccs()));

			// Checks take in consideration DbStatus 
			// Subcontractor Updates	
			getmenuItemSubcontractorUpdates().setEnabled(
				cbSubconCheck && int2bool(aaSecData.getSubconAccs()));

			// Lienholder Updates	
			getmenuItemLienholderUpdates().setEnabled(
				cbLienholderCheck
					&& int2bool(aaSecData.getLienHldrAccs()));

			// Payment Accounts Update 
			getmenuItemPaymentAccountUpdates().setEnabled(
				abDBStatus && int2bool(aaSecData.getFundsRemitAccs()));

			// Credit Card Fee 	
			getmenuItemCreditCard().setEnabled(
				cbCreditCheck
					&& int2bool(aaSecData.getCrdtCardFeeAccs()));

			// Batch Report Management 	
			getmenuItemBatchRptMgmt().setEnabled(
				abDBStatus
					&& int2bool(aaSecData.getBatchRptMgmtAccs()));
			// end defect 10701 

			// SECURITY 
			boolean lbEnableSecurity =
				abDBStatus
					&& int2bool(aaSecData.getSecrtyAccs())
					&& cbTreatAsServer;

			getmenuSecurity().setEnabled(lbEnableSecurity);

			// Employee Security	
			getmenuItemEmployeeSecurity().setEnabled(lbEnableSecurity);

			// Supervisor Override Code 
			getmenuItemSupervisorOverride().setEnabled(
				lbEnableSecurity);

			// Security Change Report 
			getmenuItemSecurityChangeReport().setEnabled(
				lbEnableSecurity);

			// SECURITY REPORTS
			getmenuSecurityReports().setEnabled(
				abDBStatus && int2bool(aaSecData.getSecrtyAccs()));

			// Employee Security 	
			getmenuItemEmployeeSecurityReport().setEnabled(
				abDBStatus
					&& int2bool(aaSecData.getEmpSecrtyRptAccs()));

			// Event Security 
			getmenuItemEventSecurityReport().setEnabled(
				abDBStatus
					&& int2bool(aaSecData.getEmpSecrtyRptAccs()));

			// ADMIN
			boolean lbEnableAdmin =
				cbTreatAsServer
					&& abDBStatus
					&& int2bool(aaSecData.getAdminAccs());

			getmenuAdminFunctions().setEnabled(lbEnableAdmin);

			if (lbEnableAdmin)
			{
				// defect 8550
				// Do not enable report if no substations
				Vector lvSubsta =
					SubstationCache.getSubsta(
						SystemProperty.getOfficeIssuanceNo());

				getmenuItemPublishingReport().setEnabled(
					lvSubsta.size() > 1);

				// Do not enable update if !(Publishing && Main Office)
				boolean lbPublishingActive =
					!SubstationSubscriptionCache.isEmpty();

				//getmenuItemPublishingUpdate().setEnabled(isMainOffice());
				getmenuItemPublishingUpdate().setEnabled(
					lbPublishingActive && isMainOffice());

				// No longer needed; incorporated into setServerPlusEnabled()	
				// getmenuItemServerPlusDisabled().setEnabled(cbServer);
				// getmenuItemServerPlusEnabled().setEnabled(cbServer);
				// end defect 8550

				setServerPlusEnabled(
					RTSApplicationController.isServerPlus());
			}

			// RSPS Update 	
			getmenuItemRSPSUpdates().setEnabled(
				int2bool(aaSecData.getRSPSUpdtAccs()));
		}
	}
	/**
	 * Enable Logon menu 
	 */
	private void enableLogonMenu()
	{
		getmenuLogon().setEnabled(true);
		getmenuItemLogon().setEnabled(true);
	}
	/**
	 * Enable Misc Menu
	 * 
	 * @param aaSecData SecurityData
	 * @param abDBStatus boolean   
	 */
	private void enableMiscMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		getmenuMisc().setEnabled(aaSecData.getUserName() != null);

		// Reprint Receipt 
		getmenuItemReprintReceipt().setEnabled(
			int2bool(aaSecData.getReprntRcptAccs()));

		// Complete Vehicle Transaction 
		// defect 9157	
		// Complete Vehicle Transaction doesn't make sense for HQ
		getmenuItemCompleteVehicleTransaction().setEnabled(
			abDBStatus && !SystemProperty.isHQ());
		// end defect 9157 

		// Void 
		getmenuItemVoidTransaction().setEnabled(
			abDBStatus && int2bool(aaSecData.getVoidTransAccs()));

		// Set Print Destination 	
		getmenuItemSetPrintDestination().setEnabled(true);

		// Print Immediate 
		// defect 9157 
		// Use Print Immediate where authorized 
		getmenuPrintImmediate().setEnabled(
			int2bool(aaSecData.getPrntImmedAccs()));
		// end defect 9157
	}
	/**
	 * Enable Reports Menu
	 * 
	 * @param aaSecData SecurityData
	 * @param abDBStatus boolean 
	 */
	private void enableReportsMenu(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		// Reports
		// defect 10900 
		// HQ may not have "Reports" access @ start of 6.8.0  
		getmenuReports().setEnabled(
			int2bool(aaSecData.getRptsAccs()) || SystemProperty.isHQ());
		// end defect 10900 

		// Sales Tax 
		getmenuItemSalesTax().setEnabled(
			!SystemProperty.isHQ()
				&& int2bool(aaSecData.getCntyRptsAccs()));

		// Reprint Reports 	
		getmenuItemReprintReports().setEnabled(
			int2bool(aaSecData.getReprntRptAccs()));

		// Title Package Report 	
		getmenuItemTitlePackageReport().setEnabled(
			abDBStatus && !SystemProperty.isHQ());

		// Reprint Sticker Report 
		getmenuItemReprintStickerReport().setEnabled(
			abDBStatus
				&& (!SystemProperty.isCounty())
				&& int2bool(aaSecData.getReprntStkrRptAccs()));

		// defect 8900
		// Task 43
		// Exempt Audit Report 
		getmenuItemExemptAuditReport().setEnabled(
			abDBStatus && int2bool(aaSecData.getExmptAuditRptAccs()));
		// end defect 8900

		// defect 9971 
		getmenuItemElectronicTitleReport().setEnabled(
			abDBStatus && int2bool(aaSecData.getETtlRptAccs()));
		// end defect 9971  

		// defect 10900 
		getmenuItemSuspectedFraudReport().setEnabled(
			abDBStatus && !SystemProperty.isCounty());
		// end defect 10900  
	}

	/**
	 * Finish out the class.
	 */
	protected final void finalize()
	{
		actionComponents.removeAllElements();
		actionComponents = null;
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// only issue event if component is enabled.
		if (aaFE.getComponent().isEnabled())
		{
			KeyEvent laKE =
				new KeyEvent(
					aaFE.getComponent(),
					KeyEvent.KEY_PRESSED,
					java
						.util
						.Calendar
						.getInstance()
						.getTime()
						.getTime(),
					0,
					KeyEvent.VK_DOWN,
					KeyEvent.CHAR_UNDEFINED);
			Toolkit
				.getDefaultToolkit()
				.getSystemEventQueue()
				.postEvent(
				laKE);
		}
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		// empty code block
	}

	/**
	 * Return the btnInternet property value.
	 * 
	 * <p>Uses font to make the text bold.
	 * 
	 * @return JButton
	 */

	private JButton getbtnInternet()
	{
		if (ivjbtnInternet == null)
		{
			try
			{
				ivjbtnInternet = new javax.swing.JButton();
				ivjbtnInternet.setName("btnInternet");
				ivjbtnInternet.setMnemonic(KeyEvent.VK_U);
				ivjbtnInternet.setText(CURR_INT_RENEW_CT);
				//defect 7904
				//ivjbtnInternet.setForeground(new java.awt.Color(102,102,153));
				ivjbtnInternet.setForeground(
					new java.awt.Color(0, 0, 0));
				//end defect 7904
				ivjbtnInternet.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjbtnInternet.setFocusPainted(false);
				ivjbtnInternet.setBorderPainted(false);
				ivjbtnInternet.setFont(
					new java.awt.Font("Arial", Font.BOLD, 12));
				ivjbtnInternet.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				ivjbtnInternet.addActionListener(
					new InternetListener(this));
				ivjbtnInternet.setFocusable(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnInternet;
	}
	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getimagePanel()
	{
		if (ivjimagePanel == null)
		{
			try
			{
				ivjimagePanel = new javax.swing.JPanel();
				ivjimagePanel.setName("imagePanel");
				ivjimagePanel.setLayout(new java.awt.GridBagLayout());
				ivjimagePanel.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjimagePanel.setMinimumSize(
					new java.awt.Dimension(618, 334));

				java.awt.GridBagConstraints constraintsJLabel1 =
					new java.awt.GridBagConstraints();
				constraintsJLabel1.gridx = 1;
				constraintsJLabel1.gridy = 1;
				constraintsJLabel1.ipadx = 7;
				constraintsJLabel1.ipady = 10;
				constraintsJLabel1.insets =
					new java.awt.Insets(17, 98, 18, 99);
				getimagePanel().add(getJLabel1(), constraintsJLabel1);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjimagePanel;
	}
	/**
	 * Return the JDesktopPane1 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJDesktopPane1()
	{
		if (ivjJDesktopPane1 == null)
		{
			try
			{
				ivjJDesktopPane1 = new javax.swing.JPanel();
				ivjJDesktopPane1.setName("JDesktopPane1");
				ivjJDesktopPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjJDesktopPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJDesktopPane1.setMinimumSize(
					new java.awt.Dimension(640, 163));

				java.awt.GridBagConstraints constraintsimagePanel =
					new java.awt.GridBagConstraints();
				constraintsimagePanel.gridx = 0;
				constraintsimagePanel.gridy = 0;
				constraintsimagePanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsimagePanel.weightx = 1.0;
				constraintsimagePanel.weighty = 1.0;
				constraintsimagePanel.ipady = -127;
				constraintsimagePanel.insets =
					new java.awt.Insets(12, 11, 2, 11);
				getJDesktopPane1().add(
					getimagePanel(),
					constraintsimagePanel);

				java.awt.GridBagConstraints constraintsJPanel4 =
					new java.awt.GridBagConstraints();
				constraintsJPanel4.gridx = 0;
				constraintsJPanel4.gridy = 2;
				constraintsJPanel4.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel4.weightx = 1.0;
				constraintsJPanel4.ipadx = 154;
				constraintsJPanel4.ipady = 19;
				constraintsJPanel4.insets =
					new java.awt.Insets(6, 11, 8, 12);
				getJDesktopPane1().add(
					getJPanel4(),
					constraintsJPanel4);

				java.awt.GridBagConstraints constraintspendingPanel =
					new java.awt.GridBagConstraints();
				constraintspendingPanel.gridx = 0;
				constraintspendingPanel.gridy = 1;
				constraintspendingPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspendingPanel.weightx = 1.0;
				constraintspendingPanel.weighty = 1.0;
				constraintspendingPanel.ipadx = -2;
				constraintspendingPanel.ipady = -197;
				constraintspendingPanel.insets =
					new java.awt.Insets(3, 11, 6, 11);
				getJDesktopPane1().add(
					getpendingPanel(),
					constraintspendingPanel);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJDesktopPane1;
	}
	/**
	 * Return the JFrameContentPane property value.
	 * 
	 * @return JPanel
	 */

	private javax.swing.JPanel getJFrameContentPane()
	{
		if (ivjJFrameContentPane == null)
		{
			try
			{
				ivjJFrameContentPane = new javax.swing.JPanel();
				ivjJFrameContentPane.setName("JFrameContentPane");
				ivjJFrameContentPane.setLayout(
					getJFrameContentPaneBorderLayout());
				ivjJFrameContentPane.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJFrameContentPane.setMinimumSize(
					new java.awt.Dimension(640, 163));
				ivjJFrameContentPane.setBounds(0, 0, 0, 0);
				getJFrameContentPane().add(getJDesktopPane1(), CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJFrameContentPane;
	}
	/**
	 * Return the JFrameContentPaneBorderLayout property value.
	 * 
	 * @return BorderLayout
	 */

	private BorderLayout getJFrameContentPaneBorderLayout()
	{
		java.awt.BorderLayout ivjJFrameContentPaneBorderLayout = null;
		try
		{
			/* Create part */
			ivjJFrameContentPaneBorderLayout =
				new java.awt.BorderLayout();
			ivjJFrameContentPaneBorderLayout.setVgap(0);
			ivjJFrameContentPaneBorderLayout.setHgap(0);
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		return ivjJFrameContentPaneBorderLayout;
	}
	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getJLabel1()
	{
		if (ivjJLabel1 == null)
		{
			try
			{
				ivjJLabel1 = new javax.swing.JLabel();
				ivjJLabel1.setName("JLabel1");
				// defect 10155
				// Change to DMV background on DMV Start Date
				//ivjJLabel1.setIcon(
				//	new javax.swing.ImageIcon(
				//		getClass().getResource(IMAGE_TXDOT_BKGRND)));
				if (RTSDate.getCurrentDate().getYYYYMMDDDate()
					>= SystemProperty.getDMVStartDate().getYYYYMMDDDate())
				{
					ivjJLabel1.setIcon(
						new javax.swing.ImageIcon(
							getClass().getResource(IMAGE_DMV_BKGRND)));
				}
				else
				{
					ivjJLabel1.setIcon(
						new javax.swing.ImageIcon(
							getClass().getResource(
								IMAGE_TXDOT_BKGRND)));
				}
				// end defect 10155
				ivjJLabel1.setMaximumSize(
					new java.awt.Dimension(414, 289));
				ivjJLabel1.setMinimumSize(
					new java.awt.Dimension(414, 289));
				ivjJLabel1.setDisabledIcon(new javax.swing.ImageIcon());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJLabel1;
	}
	/**
	 * Return the JPanel4 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new javax.swing.JPanel();
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setLayout(new java.awt.BorderLayout());
				ivjJPanel4.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel4.setMinimumSize(
					new java.awt.Dimension(463, 78));
				getJPanel4().add(getJPanel5(), DIR_WEST);
				getJPanel4().add(getJPanel6(), DIR_EAST);
				getJPanel4().add(getlblCountyString(), DIR_SOUTH);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel4;
	}

	///**
	// * Return the JPanel4BorderLayout property value.
	// * 
	// * @return BorderLayout
	// */
	///* WARNING: THIS METHOD WILL BE REGENERATED. */
	//private BorderLayout getJPanel4BorderLayout()
	//{
	//	java.awt.BorderLayout ivjJPanel4BorderLayout = null;
	//	try
	//	{
	//		/* Create part */
	//		ivjJPanel4BorderLayout = new java.awt.BorderLayout();
	//		ivjJPanel4BorderLayout.setVgap(0);
	//		ivjJPanel4BorderLayout.setHgap(0);
	//	}
	//	catch (Throwable aeIVJEx)
	//	{
	//		//handleException(aeIVJEx);
	//	}
	//	return ivjJPanel4BorderLayout;
	//}
	/**
	 * Return the JPanel5 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			try
			{
				ivjJPanel5 = new javax.swing.JPanel();
				ivjJPanel5.setName("JPanel5");
				ivjJPanel5.setLayout(new java.awt.GridBagLayout());
				ivjJPanel5.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel5.setMinimumSize(
					new java.awt.Dimension(193, 64));

				java.awt.GridBagConstraints constraintsstcLblVersion =
					new java.awt.GridBagConstraints();
				constraintsstcLblVersion.gridx = 1;
				constraintsstcLblVersion.gridy = 1;
				constraintsstcLblVersion.anchor =
					java.awt.GridBagConstraints.WEST;
				constraintsstcLblVersion.ipadx = 110;
				constraintsstcLblVersion.insets =
					new java.awt.Insets(34, 19, 5, 10);
				getJPanel5().add(
					getstcLblVersion(),
					constraintsstcLblVersion);

				java.awt.GridBagConstraints constraintslblDate =
					new java.awt.GridBagConstraints();
				constraintslblDate.gridx = 1;
				constraintslblDate.gridy = 2;
				constraintslblDate.anchor =
					java.awt.GridBagConstraints.WEST;
				constraintslblDate.ipadx = 110;
				constraintslblDate.insets =
					new java.awt.Insets(5, 19, 11, 10);
				getJPanel5().add(getlblDate(), constraintslblDate);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel5;
	}
	/**
	 * Return the JPanel6 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel6()
	{
		if (ivjJPanel6 == null)
		{
			try
			{
				ivjJPanel6 = new javax.swing.JPanel();
				ivjJPanel6.setName("JPanel6");
				ivjJPanel6.setLayout(new java.awt.GridBagLayout());
				ivjJPanel6.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel6.setMinimumSize(
					new java.awt.Dimension(193, 50));

				java
					.awt
					.GridBagConstraints constraintslblPrintImmediate =
					new java.awt.GridBagConstraints();
				constraintslblPrintImmediate.gridx = 2;
				constraintslblPrintImmediate.gridy = 1;
				constraintslblPrintImmediate.weighty = 1.0;
				constraintslblPrintImmediate.ipadx = 29;
				constraintslblPrintImmediate.insets =
					new java.awt.Insets(5, 10, 1, 9);
				getJPanel6().add(
					getlblPrintImmediate(),
					constraintslblPrintImmediate);
				java
					.awt
					.GridBagConstraints constraintsstcLblPrintImmediate =
					new java.awt.GridBagConstraints();
				constraintsstcLblPrintImmediate.gridx = 1;
				constraintsstcLblPrintImmediate.gridy = 1;
				constraintsstcLblPrintImmediate.weighty = 1.0;
				constraintsstcLblPrintImmediate.ipadx = 5;
				constraintsstcLblPrintImmediate.insets =
					new java.awt.Insets(5, 237, 1, 10);
				getJPanel6().add(
					getstcLblPrintImmediate(),
					constraintsstcLblPrintImmediate);

				java.awt.GridBagConstraints constraintsstcLblDataServer =
					new java.awt.GridBagConstraints();
				constraintsstcLblDataServer.gridx = 1;
				constraintsstcLblDataServer.gridy = 2;
				constraintsstcLblDataServer.anchor =
					java.awt.GridBagConstraints.SOUTH;
				constraintsstcLblDataServer.ipadx = 15;
				constraintsstcLblDataServer.insets =
					new java.awt.Insets(1, 250, 1, 10);
				getJPanel6().add(
					getstcLblDataServer(),
					constraintsstcLblDataServer);

				java.awt.GridBagConstraints constraintslblDataServer =
					new java.awt.GridBagConstraints();
				constraintslblDataServer.gridx = 2;
				constraintslblDataServer.gridy = 2;
				constraintslblDataServer.anchor =
					java.awt.GridBagConstraints.SOUTH;
				constraintslblDataServer.ipadx = 29;
				constraintslblDataServer.ipady = -2;
				constraintslblDataServer.insets =
					new java.awt.Insets(1, 15, 1, 14);
				getJPanel6().add(
					getlblDataServer(),
					constraintslblDataServer);
				java
					.awt
					.GridBagConstraints constraintslblRecordRetrieval =
					new java.awt.GridBagConstraints();
				constraintslblRecordRetrieval.gridx = 2;
				constraintslblRecordRetrieval.gridy = 3;
				constraintslblRecordRetrieval.anchor =
					java.awt.GridBagConstraints.SOUTH;
				constraintslblRecordRetrieval.ipadx = 29;
				constraintslblRecordRetrieval.ipady = -2;
				constraintslblRecordRetrieval.insets =
					new java.awt.Insets(1, 15, 1, 14);
				getJPanel6().add(
					getlblRecordRetrieval(),
					constraintslblRecordRetrieval);
				java
					.awt
					.GridBagConstraints constraintsstcLblRecordRetrieval =
					new java.awt.GridBagConstraints();
				constraintsstcLblRecordRetrieval.gridx = 1;
				constraintsstcLblRecordRetrieval.gridy = 3;
				constraintsstcLblRecordRetrieval.anchor =
					java.awt.GridBagConstraints.SOUTH;
				constraintsstcLblRecordRetrieval.ipadx = 28;
				constraintsstcLblRecordRetrieval.insets =
					new java.awt.Insets(1, 210, 1, 10);
				getJPanel6().add(
					getstcLblRecordRetrieval(),
					constraintsstcLblRecordRetrieval);

				java.awt.GridBagConstraints constraintsbtnInternet =
					new java.awt.GridBagConstraints();
				constraintsbtnInternet.gridx = 1;
				constraintsbtnInternet.gridy = 4;
				constraintsbtnInternet.gridwidth = 2;
				constraintsbtnInternet.anchor =
					java.awt.GridBagConstraints.SOUTH;
				constraintsbtnInternet.ipadx = 13;
				constraintsbtnInternet.ipady = -9;
				constraintsbtnInternet.insets =
					new java.awt.Insets(1, 169, 5, 9);
				getJPanel6().add(
					getbtnInternet(),
					constraintsbtnInternet);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel6;
	}
	/**
	 * Return the JSeparator1 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator1()
	{
		if (ivjJSeparator1 == null)
		{
			try
			{
				ivjJSeparator1 = new javax.swing.JSeparator();
				ivjJSeparator1.setName("JSeparator1");
				//ivjJSeparator1.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator1;
	}
	/**
	 * Return the JSeparator10 property value
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator10()
	{
		if (ivjJSeparator10 == null)
		{
			try
			{
				ivjJSeparator10 = new javax.swing.JSeparator();
				ivjJSeparator10.setName("JSeparator10");
				//ivjJSeparator10.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator10;
	}
	/**
	 * Return the JSeparator11 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator11()
	{
		if (ivjJSeparator11 == null)
		{
			try
			{
				ivjJSeparator11 = new javax.swing.JSeparator();
				ivjJSeparator11.setName("JSeparator11");
				//ivjJSeparator11.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator11;
	}
	/**
	 * Return the JSeparator12 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator12()
	{
		if (ivjJSeparator12 == null)
		{
			try
			{
				ivjJSeparator12 = new javax.swing.JSeparator();
				ivjJSeparator12.setName("JSeparator12");
				//ivjJSeparator12.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator12;
	}
	/**
	 * Return the JSeparator13 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator13()
	{
		if (ivjJSeparator13 == null)
		{
			try
			{
				ivjJSeparator13 = new javax.swing.JSeparator();
				ivjJSeparator13.setName("JSeparator13");
				//ivjJSeparator13.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator13;
	}
	/**
	 * Return the JSeparator14 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator14()
	{
		if (ivjJSeparator14 == null)
		{
			try
			{
				ivjJSeparator14 = new javax.swing.JSeparator();
				ivjJSeparator14.setName("JSeparator14");
				//ivjJSeparator14.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator14;
	}
	/**
	 * Return the JSeparator15 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator15()
	{
		if (ivjJSeparator15 == null)
		{
			try
			{
				ivjJSeparator15 = new javax.swing.JSeparator();
				ivjJSeparator15.setName("JSeparator15");
				//ivjJSeparator15.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator15;
	}

	//	/**
	//	 * Return the JSeparator17 property value.
	//	 * 
	//	 * @return JSeparator
	//	 */
	//
	//	private JSeparator getJSeparator17()
	//	{
	//		if (ivjJSeparator17 == null)
	//		{
	//			try
	//			{
	//				ivjJSeparator17 = new javax.swing.JSeparator();
	//				ivjJSeparator17.setName("JSeparator17");
	//				//ivjJSeparator17.setFont(
	//				//	new java.awt.Font("dialog", 0, 12));
	//				// user code begin {1}
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjJSeparator17;
	//	}

	/**
	 * Return the JSeparator18 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator18()
	{
		if (ivjJSeparator18 == null)
		{
			try
			{
				ivjJSeparator18 = new javax.swing.JSeparator();
				ivjJSeparator18.setName("JSeparator18");
				//ivjJSeparator18.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator18;
	}
	/**
	 * Return the JSeparator19 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator19()
	{
		if (ivjJSeparator19 == null)
		{
			try
			{
				ivjJSeparator19 = new javax.swing.JSeparator();
				ivjJSeparator19.setName("JSeparator19");
				//ivjJSeparator19.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator19;
	}
	/**
	 * Return the JSeparator2 property value.
	 * @return JSeparator
	 */

	private JSeparator getJSeparator2()
	{
		if (ivjJSeparator2 == null)
		{
			try
			{
				ivjJSeparator2 = new javax.swing.JSeparator();
				ivjJSeparator2.setName("JSeparator2");
				//ivjJSeparator2.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator2;
	}
	/**
	 * Return the JSeparator20 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator20()
	{
		if (ivjJSeparator20 == null)
		{
			try
			{
				ivjJSeparator20 = new javax.swing.JSeparator();
				ivjJSeparator20.setName("JSeparator20");
				//ivjJSeparator20.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator20;
	}
	/**
	 * Return the JSeparator21 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator21()
	{
		if (ivjJSeparator21 == null)
		{
			try
			{
				ivjJSeparator21 = new javax.swing.JSeparator();
				ivjJSeparator21.setName("JSeparator21");
				//ivjJSeparator21.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator21;
	}
	/**
	 * Return the JSeparator22 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator22()
	{
		if (ivjJSeparator22 == null)
		{
			try
			{
				ivjJSeparator22 = new javax.swing.JSeparator();
				ivjJSeparator22.setName("JSeparator22");
				//ivjJSeparator22.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator22;
	}
	/**
	 * Return the JSeparator23 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator23()
	{
		if (ivjJSeparator23 == null)
		{
			try
			{
				ivjJSeparator23 = new javax.swing.JSeparator();
				ivjJSeparator23.setName("JSeparator23");
				//ivjJSeparator23.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator23;
	}
	/**
	 * Return the JSeparator24 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator24()
	{
		if (ivjJSeparator24 == null)
		{
			try
			{
				ivjJSeparator24 = new javax.swing.JSeparator();
				ivjJSeparator24.setName("JSeparator24");
				//ivjJSeparator24.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator24;
	}
	/**
	 * Return the JSeparator25 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator25()
	{
		if (ivjJSeparator25 == null)
		{
			try
			{
				ivjJSeparator25 = new javax.swing.JSeparator();
				ivjJSeparator25.setName("JSeparator25");
				//ivjJSeparator25.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator25;
	}
	/**
	 * Return the JSeparator26 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator26()
	{
		if (ivjJSeparator26 == null)
		{
			try
			{
				ivjJSeparator26 = new javax.swing.JSeparator();
				ivjJSeparator26.setName("JSeparator26");
				//ivjJSeparator26.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator26;
	}
	/**
	 * Return the JSeparator27 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparator27()
	{
		if (ivjJSeparator27 == null)
		{
			try
			{
				ivjJSeparator27 = new javax.swing.JSeparator();
				ivjJSeparator27.setName("JSeparator27");
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator27;
	}

	/**
	 * Return the JSeparator3 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparator3()
	{
		if (ivjJSeparator3 == null)
		{
			try
			{
				ivjJSeparator3 = new javax.swing.JSeparator();
				ivjJSeparator3.setName("JSeparator3");
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator3;
	}
	/**
	 * Return the JSeparator5 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator5()
	{
		if (ivjJSeparator5 == null)
		{
			try
			{
				ivjJSeparator5 = new javax.swing.JSeparator();
				ivjJSeparator5.setName("JSeparator5");
				//ivjJSeparator5.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator5;
	}
	/**
	 * Return the JSeparator7 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator7()
	{
		if (ivjJSeparator7 == null)
		{
			try
			{
				ivjJSeparator7 = new javax.swing.JSeparator();
				ivjJSeparator7.setName("JSeparator7");
				//ivjJSeparator7.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator7;
	}
	/**
	 * Return the JSeparator8 property value.
	 * 
	 * @return JSeparator
	 */

	private JSeparator getJSeparator8()
	{
		if (ivjJSeparator8 == null)
		{
			try
			{
				ivjJSeparator8 = new javax.swing.JSeparator();
				ivjJSeparator8.setName("JSeparator8");
				//ivjJSeparator8.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator8;
	}
	/**
	 * Return the JSeparator9 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparator9()
	{
		if (ivjJSeparator9 == null)
		{
			try
			{
				ivjJSeparator9 = new javax.swing.JSeparator();
				ivjJSeparator9.setName("JSeparator9");
				//ivjJSeparator9.setFont(
				//	new java.awt.Font("dialog", 0, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJSeparator9;
	}

	/**
	 * Return the ivjJSeparatorSpecialPlate0 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparatorSpecialPlate0()
	{
		if (ivjJSeparatorSpecialPlate0 == null)
		{
			try
			{
				ivjJSeparatorSpecialPlate0 =
					new javax.swing.JSeparator();
				ivjJSeparatorSpecialPlate0.setName(
					"ivjJSeparatorSpecialPlate0");
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjJSeparatorSpecialPlate0;
	}
	/**
	 * Return the ivjJSeparatorSpecialPlate1 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparatorSpecialPlate1()
	{
		if (ivjJSeparatorSpecialPlate1 == null)
		{
			try
			{
				ivjJSeparatorSpecialPlate1 =
					new javax.swing.JSeparator();
				ivjJSeparatorSpecialPlate1.setName(
					"ivjJSeparatorSpecialPlate1");
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjJSeparatorSpecialPlate1;
	}
	/**
	 * Return the ivjJSeparatorSpecialPlate2 property value.
	 * 
	 * @return JSeparator
	 */
	private JSeparator getJSeparatorSpecialPlate2()
	{
		if (ivjJSeparatorSpecialPlate2 == null)
		{
			try
			{
				ivjJSeparatorSpecialPlate2 =
					new javax.swing.JSeparator();
				ivjJSeparatorSpecialPlate2.setName(
					"ivjJSeparatorSpecialPlate2");
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjJSeparatorSpecialPlate2;
	}
	/**
	 * Return the lblCountyString property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblCountyString()
	{
		if (ivjlblCountyString == null)
		{
			try
			{
				ivjlblCountyString = new javax.swing.JLabel();
				ivjlblCountyString.setName("lblCountyString");
				ivjlblCountyString.setText(DFLT_OFC_WS);
				ivjlblCountyString.setMaximumSize(
					new java.awt.Dimension(463, 14));
				ivjlblCountyString.setMinimumSize(
					new java.awt.Dimension(463, 14));
				ivjlblCountyString.setHorizontalAlignment(0);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCountyString;
	}
	/**
	 * Return the lblDataServer property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblDataServer()
	{
		if (ivjlblDataServer == null)
		{
			try
			{
				ivjlblDataServer = new javax.swing.JLabel();
				ivjlblDataServer.setName("lblDataServer");
				ivjlblDataServer.setIcon(
					new javax.swing.ImageIcon(
						getClass().getResource(IMAGE_OFF)));
				ivjlblDataServer.setMaximumSize(
					new java.awt.Dimension(16, 16));
				ivjlblDataServer.setMinimumSize(
					new java.awt.Dimension(16, 16));
				ivjlblDataServer.setDisabledIcon(
					new javax.swing.ImageIcon());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblDataServer;
	}
	/**
	 * Return the lblDate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblDate()
	{
		if (ivjlblDate == null)
		{
			try
			{
				ivjlblDate = new javax.swing.JLabel();
				ivjlblDate.setName("lblDate");
				ivjlblDate.setText(DFLT_DATE);
				ivjlblDate.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjlblDate.setMinimumSize(
					new java.awt.Dimension(62, 14));
				// user code begin {1}
				// defect 7885
				//ivjlblDate.setText(Version.getDate());
				ivjlblDate.setText(SystemProperty.getVersionDate());
				// end defect 7885
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblDate;
	}
	/**
	 * Return the lblPrintImmediate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblPrintImmediate()
	{
		if (ivjlblPrintImmediate == null)
		{
			try
			{
				ivjlblPrintImmediate = new javax.swing.JLabel();
				ivjlblPrintImmediate.setName("lblPrintImmediate");
				ivjlblPrintImmediate.setText(NEXT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblPrintImmediate;
	}
	/**
	 * Return the lblRecordRetrieval property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblRecordRetrieval()
	{
		if (ivjlblRecordRetrieval == null)
		{
			try
			{
				ivjlblRecordRetrieval = new javax.swing.JLabel();
				ivjlblRecordRetrieval.setName("lblRecordRetrieval");
				ivjlblRecordRetrieval.setIcon(
					new javax.swing.ImageIcon(
						getClass().getResource(IMAGE_OFF)));
				ivjlblRecordRetrieval.setMaximumSize(
					new java.awt.Dimension(16, 16));
				ivjlblRecordRetrieval.setMinimumSize(
					new java.awt.Dimension(16, 16));
				ivjlblRecordRetrieval.setDisabledIcon(
					new javax.swing.ImageIcon());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblRecordRetrieval;
	}
	/**
	 * Return the menuAccounting property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuAccounting()
	{
		if (ivjmenuAccounting == null)
		{
			try
			{
				ivjmenuAccounting = new javax.swing.JMenu();
				ivjmenuAccounting.setName("menuAccounting");
				ivjmenuAccounting.setMnemonic(KeyEvent.VK_A);
				ivjmenuAccounting.setText(ACCOUNTING);
				ivjmenuAccounting.setActionCommand(ACCOUNTING);
				ivjmenuAccounting.add(
					getmenuItemCountyFundsRemitance());
				ivjmenuAccounting.add(getmenuItemFundsInquiry());
				ivjmenuAccounting.add(getJSeparator23());
				ivjmenuAccounting.add(getmenuItemRefund());
				ivjmenuAccounting.add(getJSeparator24());
				ivjmenuAccounting.add(getmenuItemHotCheckCredit());
				ivjmenuAccounting.add(getmenuItemHotCheckRedeemed());
				ivjmenuAccounting.add(getmenuItemDeductHotCheck());
				ivjmenuAccounting.add(getJSeparator25());
				ivjmenuAccounting.add(getmenuItemItemSeized());
				ivjmenuAccounting.add(getJSeparator26());
				ivjmenuAccounting.add(
					getmenuItemAdditionalCollections());
				ivjmenuAccounting.add(getmenuItemRegionalCollection());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuAccounting;
	}
	/**
	 * Return the menuAdminFunctions property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuAdminFunctions()
	{
		if (ivjmenuAdminFunctions == null)
		{
			try
			{
				ivjmenuAdminFunctions = new javax.swing.JMenu();
				ivjmenuAdminFunctions.setName("menuAdminFunctions");
				ivjmenuAdminFunctions.setMnemonic(KeyEvent.VK_A);
				ivjmenuAdminFunctions.setText(ADMIN_FUNCS);
				ivjmenuAdminFunctions.setActionCommand(ADMIN_FUNCS);
				ivjmenuAdminFunctions.add(
					getmenuItemPublishingReport());
				ivjmenuAdminFunctions.add(
					getmenuItemPublishingUpdate());
				ivjmenuAdminFunctions.add(new JSeparator());
				ivjmenuAdminFunctions.add(
					getmenuItemServerPlusEnabled());
				ivjmenuAdminFunctions.add(
					getmenuItemServerPlusDisabled());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuAdminFunctions;
	}
	/**
	 * Return the menuCashDrawerOperations property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuCashDrawerOperations()
	{
		if (ivjmenuCashDrawerOperations == null)
		{
			try
			{
				ivjmenuCashDrawerOperations = new javax.swing.JMenu();
				ivjmenuCashDrawerOperations.setName(
					"menuCashDrawerOperations");
				ivjmenuCashDrawerOperations.setMnemonic(KeyEvent.VK_C);
				ivjmenuCashDrawerOperations.setText(CASH_DRWR_OPS);
				ivjmenuCashDrawerOperations.setActionCommand(
					CASH_DRWR_OPS);
				ivjmenuCashDrawerOperations.add(
					getmenuItemCloseOutDay());
				ivjmenuCashDrawerOperations.add(
					getmenuItemCurrentStatus());
				ivjmenuCashDrawerOperations.add(
					getmenuItemDetailReports());
				ivjmenuCashDrawerOperations.add(
					getmenuItemCloseOutStats());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuCashDrawerOperations;
	}

	/**
	 * Return the menuCertifiedLienholderReport property value.
	 * 
	 * @return JMenu
	 */
	private JMenu getmenuCertifiedLienholderReport()
	{
		if (ivjmenuCertifiedLienholderReport == null)
		{
			try
			{
				ivjmenuCertifiedLienholderReport =
					new javax.swing.JMenu();

				ivjmenuCertifiedLienholderReport.setName(
					"ivjmenuCertifiedLienholderReport");

				ivjmenuCertifiedLienholderReport.setMnemonic(
					KeyEvent.VK_T);

				ivjmenuCertifiedLienholderReport.setText(
					CERTFD_LIENHLDR_RPT);

				// user code begin {1}
				ivjmenuCertifiedLienholderReport.add(
					getmenuItemCertifiedLienhldrRptIdSort());

				ivjmenuCertifiedLienholderReport.add(
					getmenuItemCertifiedRptNameSort());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuCertifiedLienholderReport;
	}
	/**
	 * Return the JMenu1 property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuCustomer()
	{
		if (ivjmenuCustomer == null)
		{
			try
			{
				ivjmenuCustomer = new javax.swing.JMenu();
				ivjmenuCustomer.setName("menuCustomer");
				ivjmenuCustomer.setMnemonic(KeyEvent.VK_C);
				ivjmenuCustomer.setText(CUSTOMER);
				ivjmenuCustomer.setActionCommand(CUSTOMER);
				ivjmenuCustomer.add(getmenuRegistrationOnly());
				ivjmenuCustomer.add(getJSeparator5());
				ivjmenuCustomer.add(getmenuTitleRegistration());
				ivjmenuCustomer.add(getJSeparator10());
				ivjmenuCustomer.add(getmenuInquiry());
				ivjmenuCustomer.add(getJSeparator11());
				ivjmenuCustomer.add(getmenuMiscRegistration());
				// defect 9085
				ivjmenuCustomer.add(getJSeparatorSpecialPlate0());
				ivjmenuCustomer.add(getmenuSpecialPlates());
				// end defect 9085
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuCustomer;
	}

	/**
	 * Return the ivjmenuDealerReport property value.
	 * 
	 * @return JMenu
	 */
	private JMenu getmenuDealerReport()
	{
		if (ivjmenuDealerReport == null)
		{
			try
			{
				ivjmenuDealerReport = new javax.swing.JMenu();

				ivjmenuDealerReport.setName("ivjmenuDealerReport");

				ivjmenuDealerReport.setMnemonic(KeyEvent.VK_E);

				ivjmenuDealerReport.setText(DEALER_RPT);

				// user code begin {1}
				ivjmenuDealerReport.add(getmenuItemDealerRptIdSort());

				ivjmenuDealerReport.add(getmenuItemDealerRptNameSort());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuDealerReport;
	}

	/**
	 * Return the menuDisabledPlacard property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuDisabledPlacard()
	{
		if (ivjMenuDisabledPlacard == null)
		{
			try
			{
				ivjMenuDisabledPlacard = new javax.swing.JMenu();
				ivjMenuDisabledPlacard.setName(
					"menuItemDisabledPlacard");
				ivjMenuDisabledPlacard.setMnemonic(KeyEvent.VK_D);
				ivjMenuDisabledPlacard.setText(DISABLE_PLACARD);

				// user code begin {1}
				ivjMenuDisabledPlacard.add(
					getmenuItemDisabledPlacardInquiry());

				ivjMenuDisabledPlacard.add(
					getmenuItemDisabledPlacardManagement());

				ivjMenuDisabledPlacard.add(
					getmenuItemDisabledPlacardReport());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjMenuDisabledPlacard;
	}
	/**
	 * Return the menuFunds property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuFunds()
	{
		if (ivjmenuFunds == null)
		{
			try
			{
				ivjmenuFunds = new javax.swing.JMenu();
				ivjmenuFunds.setName("menuFunds");
				ivjmenuFunds.setMnemonic(KeyEvent.VK_F);
				ivjmenuFunds.setText(FUNDS);
				ivjmenuFunds.setActionCommand(FUNDS);
				ivjmenuFunds.add(getmenuCashDrawerOperations());
				ivjmenuFunds.add(getmenuItemFundsBalanceReport());
				ivjmenuFunds.add(getmenuFundsManagement());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuFunds;
	}
	/**
	 * Return the menuFundsManagement property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuFundsManagement()
	{
		if (ivjmenuFundsManagement == null)
		{
			try
			{
				ivjmenuFundsManagement = new javax.swing.JMenu();
				ivjmenuFundsManagement.setName("menuFundsManagement");
				ivjmenuFundsManagement.setMnemonic(KeyEvent.VK_M);
				ivjmenuFundsManagement.setText(FUNDS_MGMNT);
				ivjmenuFundsManagement.setActionCommand(FUNDS_MGMNT);
				ivjmenuFundsManagement.add(getmenuItemResetClose());
				ivjmenuFundsManagement.add(
					getmenuItemRerunCountyWide());
				ivjmenuFundsManagement.add(
					getmenuItemRerunSubstation());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuFundsManagement;
	}
	/**
	 * Return the ivjRTSDynamicMenuHelp property value.
	 * 
	 * This is a dynamic menu the is loaded from an XML file.
	 * 
	 * @return JMenu
	 */
	private JMenu getmenuHelp()
	{
		if (ivjRTSDynamicMenuHelp == null)
		{
			try
			{
				ivjRTSDynamicMenuHelp =
					new RTSDynamicMenu(HELP, actionComponents);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDynamicMenuHelp;
	}
	/**
	 * Return the menuInquiry property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuInquiry()
	{
		if (ivjmenuInquiry == null)
		{
			try
			{
				ivjmenuInquiry = new javax.swing.JMenu();
				ivjmenuInquiry.setName("menuInquiry");
				ivjmenuInquiry.setMnemonic(KeyEvent.VK_I);
				ivjmenuInquiry.setText(INQUIRY);
				ivjmenuInquiry.setActionCommand(INQUIRY);
				ivjmenuInquiry.add(getmenuItemVehicleInformation());
				ivjmenuInquiry.add(getmenuItemPlateOptions());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuInquiry;
	}
	/**
	 * Return the menuInternetRenewal property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuInternetRenewal()
	{
		if (ivjmenuInternetRenewal == null)
		{
			try
			{
				ivjmenuInternetRenewal = new javax.swing.JMenu();
				ivjmenuInternetRenewal.setName("menuInternetRenewal");
				ivjmenuInternetRenewal.setMnemonic(KeyEvent.VK_W);
				ivjmenuInternetRenewal.setText(INTERNET_RENEW);
				ivjmenuInternetRenewal.setActionCommand("");
				ivjmenuInternetRenewal.add(getmenuItemProcessNew());
				ivjmenuInternetRenewal.add(getmenuItemProcessHold());
				ivjmenuInternetRenewal.add(getmenuItemSearch());
				ivjmenuInternetRenewal.add(getmenuItemReports());
				// defect 10461 
				ivjmenuInternetRenewal.add(getmenuItemNICUSA());
				// end defect 10461 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuInternetRenewal;
	}
	/**
	 * Return the menuInventory property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuInventory()
	{
		if (ivjmenuInventory == null)
		{
			try
			{
				ivjmenuInventory = new javax.swing.JMenu();
				ivjmenuInventory.setName("menuInventory");
				ivjmenuInventory.setMnemonic(KeyEvent.VK_I);
				ivjmenuInventory.setText(INVENTORY);
				ivjmenuInventory.setActionCommand(INVENTORY);
				ivjmenuInventory.add(getmenuItemAllocate());
				ivjmenuInventory.add(getmenuItemReceiveInvoice());
				ivjmenuInventory.add(getmenuItemInquiry());
				ivjmenuInventory.add(getmenuItemProfile());
				ivjmenuInventory.add(getmenuItemProfileReport());
				ivjmenuInventory.add(getmenuItemHoldRelease());
				ivjmenuInventory.add(getmenuItemDelete());
				ivjmenuInventory.add(
					getmenuItemInventoryActionReport());
				// defect 9117
				ivjmenuInventory.add(getmenuItemVIRejectionReport());
				// defect 9117
				ivjmenuInventory.add(getmenuItemInventoryHistory());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuInventory;
	}
	/**
	 * Return the menuItemAdditionalCollections property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemAdditionalCollections()
	{
		if (ivjmenuItemAdditionalCollections == null)
		{
			try
			{
				ivjmenuItemAdditionalCollections =
					new javax.swing.JMenuItem();
				ivjmenuItemAdditionalCollections.setName(
					"menuItemAdditionalCollections");
				ivjmenuItemAdditionalCollections.setMnemonic(
					KeyEvent.VK_D);
				ivjmenuItemAdditionalCollections.setText(
					ADDLCOL_TIMELAG);
				ivjmenuItemAdditionalCollections.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_G,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemAdditionalCollections);
				ivjmenuItemAdditionalCollections.setActionCommand(
					ScreenConstant.ACC001
						+ DELIM
						+ TransCdConstant.ADLCOL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemAdditionalCollections;
	}
	/**
	 * Return the menuItemAdditionalSalesTax property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemAdditionalSalesTax()
	{
		if (ivjmenuItemAdditionalSalesTax == null)
		{
			try
			{
				ivjmenuItemAdditionalSalesTax =
					new javax.swing.JMenuItem();
				ivjmenuItemAdditionalSalesTax.setName(
					"menuItemAdditionalSalesTax");
				ivjmenuItemAdditionalSalesTax.setMnemonic(
					KeyEvent.VK_A);
				ivjmenuItemAdditionalSalesTax.setText(ADDL_SALES_TAX);
				ivjmenuItemAdditionalSalesTax.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_L,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemAdditionalSalesTax);
				ivjmenuItemAdditionalSalesTax.setActionCommand(
					ScreenConstant.TTL033);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemAdditionalSalesTax;
	}
	/**
	 * Return the menuItemAddressChange property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemAddressChange()
	{
		if (ivjmenuItemAddressChange == null)
		{
			try
			{
				ivjmenuItemAddressChange = new javax.swing.JMenuItem();
				ivjmenuItemAddressChange.setName(
					"menuItemAddressChange");
				ivjmenuItemAddressChange.setMnemonic(KeyEvent.VK_A);
				ivjmenuItemAddressChange.setText(ADDR_CHG_PRT_RENEW);
				ivjmenuItemAddressChange.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_C,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemAddressChange);
				ivjmenuItemAddressChange.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.ADDR);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemAddressChange;
	}
	/**
	 * Return the menuItemAllocate property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemAllocate()
	{
		if (ivjmenuItemAllocate == null)
		{
			try
			{
				ivjmenuItemAllocate = new javax.swing.JMenuItem();
				ivjmenuItemAllocate.setName("menuItemAllocate");
				ivjmenuItemAllocate.setMnemonic(KeyEvent.VK_A);
				ivjmenuItemAllocate.setText(ALLOCATE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemAllocate);
				ivjmenuItemAllocate.setActionCommand(
					ScreenConstant.INV009);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemAllocate;
	}

	/**
	 * Return the menuItemBatchRptMgmt property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemBatchRptMgmt()
	{
		if (ivjmenuItemBatchRptMgmt == null)
		{
			try
			{
				ivjmenuItemBatchRptMgmt = new javax.swing.JMenuItem();
				ivjmenuItemBatchRptMgmt.setName("ItemBatchRptMgmt");
				ivjmenuItemBatchRptMgmt.setText(BATCH_RPT_MGMT);
				ivjmenuItemBatchRptMgmt.setContentAreaFilled(true);
				ivjmenuItemBatchRptMgmt.setMnemonic(KeyEvent.VK_M);
				// user code begin {1}
				actionComponents.add(ivjmenuItemBatchRptMgmt);
				ivjmenuItemBatchRptMgmt.setActionCommand(
					ScreenConstant.OPT008);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemBatchRptMgmt;
	}

	/**
	 * Return the menuItemCCOCCDO property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemCCOCCDO()
	{
		if (ivjmenuItemCCOCCDO == null)
		{
			try
			{
				ivjmenuItemCCOCCDO = new javax.swing.JMenuItem();
				ivjmenuItemCCOCCDO.setName("menuItemCCOCCDO");
				ivjmenuItemCCOCCDO.setMnemonic(KeyEvent.VK_C);
				ivjmenuItemCCOCCDO.setText(CCO);
				ivjmenuItemCCOCCDO.setContentAreaFilled(true);
				// user code begin {1}
				actionComponents.add(ivjmenuItemCCOCCDO);
				ivjmenuItemCCOCCDO.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.CCO);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCCOCCDO;
	}

	/**
	 * Return the ivjmenuItemCertifiedLienhldrRptIdSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemCertifiedLienhldrRptIdSort()
	{
		if (ivjmenuItemCertifiedLienhldrRptIdSort == null)
		{
			try
			{
				ivjmenuItemCertifiedLienhldrRptIdSort =
					new javax.swing.JMenuItem();
				ivjmenuItemCertifiedLienhldrRptIdSort.setName(
					"ivjmenuItemCertifiedRptIdSort");
				ivjmenuItemCertifiedLienhldrRptIdSort.setText(
					SORT_BY_ID);

				// user code begin {1}
				actionComponents.add(
					ivjmenuItemCertifiedLienhldrRptIdSort);
				ivjmenuItemCertifiedLienhldrRptIdSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.CERTFD_LIENHLDR_RPT_ID_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCertifiedLienhldrRptIdSort;
	}

	/**
	 * Return the ivjmenuItemCertifiedRptNameSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemCertifiedRptNameSort()
	{
		if (ivjmenuItemCertifiedRptNameSort == null)
		{
			try
			{
				ivjmenuItemCertifiedRptNameSort =
					new javax.swing.JMenuItem();
				ivjmenuItemCertifiedRptNameSort.setName(
					"ivjmenuItemCertifiedRptNameSort");
				ivjmenuItemCertifiedRptNameSort.setText(SORT_BY_NAME);

				ivjmenuItemCertifiedRptNameSort.setMnemonic(
					java.awt.event.KeyEvent.VK_N);

				// user code begin {1}
				actionComponents.add(ivjmenuItemCertifiedRptNameSort);
				ivjmenuItemCertifiedRptNameSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.CERTFD_LIENHLDR_RPT_NAME_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCertifiedRptNameSort;
	}

	/**
	 * Return the menuItemCloseOutDay property value.
	 * 
	 * @return JMenuItem
	 */
	private javax.swing.JMenuItem getmenuItemCloseOutDay()
	{
		if (ivjmenuItemCloseOutDay == null)
		{
			try
			{
				ivjmenuItemCloseOutDay = new javax.swing.JMenuItem();
				ivjmenuItemCloseOutDay.setName("menuItemCloseOutDay");
				ivjmenuItemCloseOutDay.setMnemonic(KeyEvent.VK_C);
				ivjmenuItemCloseOutDay.setText(CLOSE_OUT_FOR_THE_DAY);
				ivjmenuItemCloseOutDay.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_Z,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemCloseOutDay);
				ivjmenuItemCloseOutDay.setActionCommand(
					ScreenConstant.FUN001
						+ DELIM
						+ TransCdConstant.CLOSE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCloseOutDay;
	}
	/**
	 * Return the menuItemCloseOutStats property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCloseOutStats()
	{
		if (ivjmenuItemCloseOutStats == null)
		{
			try
			{
				ivjmenuItemCloseOutStats = new javax.swing.JMenuItem();
				ivjmenuItemCloseOutStats.setName(
					"menuItemCloseOutStats");
				ivjmenuItemCloseOutStats.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemCloseOutStats.setText(CLOSEOUT_STATS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemCloseOutStats);
				ivjmenuItemCloseOutStats.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.CLOSEOUT_STATS_RPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCloseOutStats;
	}

	//	/**
	//	 * Return the menuItemCOA property value.
	//	 * 
	//	 * @return JMenuItem
	//	 */
	//
	//	private JMenuItem getmenuItemCOA()
	//	{
	//		if (ivjmenuItemCOA == null)
	//		{
	//			try
	//			{
	//				ivjmenuItemCOA = new javax.swing.JMenuItem();
	//				ivjmenuItemCOA.setName("menuItemCOA");
	//				//ivjmenuItemCOA.setMnemonic(79);
	//				ivjmenuItemCOA.setMnemonic('o');
	//				ivjmenuItemCOA.setText(COA);
	//				// user code begin {1}
	//				actionComponents.add(ivjmenuItemCOA);
	//				ivjmenuItemCOA.setActionCommand(
	//					ScreenConstant.KEY006
	//						+ DELIM
	//						+ TransCdConstant.COA);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjmenuItemCOA;
	//	}

	/**
	 * Return the menuItemCompleteVehicleTransaction property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCompleteVehicleTransaction()
	{
		if (ivjmenuItemCompleteVehicleTransaction == null)
		{
			try
			{
				ivjmenuItemCompleteVehicleTransaction =
					new javax.swing.JMenuItem();
				ivjmenuItemCompleteVehicleTransaction.setName(
					"menuItemCompleteVehicleTransaction");
				ivjmenuItemCompleteVehicleTransaction.setMnemonic(
					KeyEvent.VK_C);
				ivjmenuItemCompleteVehicleTransaction.setText(
					COMPLETE_VEH_TRANS);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemCompleteVehicleTransaction);
				ivjmenuItemCompleteVehicleTransaction.setActionCommand(
					ScreenConstant.CUS001);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCompleteVehicleTransaction;
	}
	/**
	 * Return the menuItemCorrectTitleRejection property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCorrectTitleRejection()
	{
		if (ivjmenuItemCorrectTitleRejection == null)
		{
			try
			{
				ivjmenuItemCorrectTitleRejection =
					new javax.swing.JMenuItem();
				ivjmenuItemCorrectTitleRejection.setName(
					"menuItemCorrectTitleRejection");
				ivjmenuItemCorrectTitleRejection.setMnemonic(
					KeyEvent.VK_E);
				ivjmenuItemCorrectTitleRejection.setText(
					CORRECT_TTLREJ);
				ivjmenuItemCorrectTitleRejection.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_E,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemCorrectTitleRejection);
				ivjmenuItemCorrectTitleRejection.setActionCommand(
					ScreenConstant.KEY006
						+ DELIM
						+ TransCdConstant.REJCOR);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCorrectTitleRejection;
	}
	/**
	 * Return the menuItemCountyFundsRemittance property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCountyFundsRemitance()
	{
		if (ivjmenuItemCountyFundsRemitance == null)
		{
			try
			{
				ivjmenuItemCountyFundsRemitance =
					new javax.swing.JMenuItem();
				ivjmenuItemCountyFundsRemitance.setName(
					"menuItemCountyFundsRemitance");
				ivjmenuItemCountyFundsRemitance.setMnemonic(
					KeyEvent.VK_R);
				ivjmenuItemCountyFundsRemitance.setText(
					CNTY_FUNDS_REMIT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemCountyFundsRemitance);
				ivjmenuItemCountyFundsRemitance.setActionCommand(
					ScreenConstant.ACC017);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCountyFundsRemitance;
	}
	/**
	 * Return the menuItemCreditCard property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCreditCard()
	{
		if (ivjmenuItemCreditCard == null)
		{
			try
			{
				ivjmenuItemCreditCard = new javax.swing.JMenuItem();
				ivjmenuItemCreditCard.setName("menuItemCreditCard");
				ivjmenuItemCreditCard.setMnemonic(KeyEvent.VK_F);
				ivjmenuItemCreditCard.setText(CRDT_CARD_FEE_UPD);
				// user code begin {1}
				actionComponents.add(ivjmenuItemCreditCard);
				ivjmenuItemCreditCard.setActionCommand(
					ScreenConstant.OPT006);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCreditCard;
	}
	/**
	 * Return the menuItemCurrentStatus property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemCurrentStatus()
	{
		if (ivjmenuItemCurrentStatus == null)
		{
			try
			{
				ivjmenuItemCurrentStatus = new javax.swing.JMenuItem();
				ivjmenuItemCurrentStatus.setName(
					"menuItemCurrentStatus");
				ivjmenuItemCurrentStatus.setMnemonic(KeyEvent.VK_U);
				ivjmenuItemCurrentStatus.setText(CURR_STAT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemCurrentStatus);
				ivjmenuItemCurrentStatus.setActionCommand(
					ScreenConstant.FUN006
						+ DELIM
						+ TransCdConstant.CURRENT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemCurrentStatus;
	}

	/**
	 * Return the ivjmenuItemDealerRptIdSort property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDealerRptIdSort()
	{
		if (ivjmenuItemDealerRptIdSort == null)
		{
			try
			{
				ivjmenuItemDealerRptIdSort =
					new javax.swing.JMenuItem();
				ivjmenuItemDealerRptIdSort.setName(
					"ivjmenuItemDealerRptIdSort");
				ivjmenuItemDealerRptIdSort.setText(SORT_BY_ID);
				ivjmenuItemDealerRptIdSort.setMnemonic(KeyEvent.VK_I);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDealerRptIdSort);
				ivjmenuItemDealerRptIdSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.DEALER_RPT_ID_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDealerRptIdSort;
	}

	/**
	 * Return the ivjmenuItemDealerRptNameSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemDealerRptNameSort()
	{
		if (ivjmenuItemDealerRptNameSort == null)
		{
			try
			{
				ivjmenuItemDealerRptNameSort =
					new javax.swing.JMenuItem();
				ivjmenuItemDealerRptNameSort.setName(
					"ivjmenuItemDealerRptNameSort");
				ivjmenuItemDealerRptNameSort.setText(SORT_BY_NAME);
				ivjmenuItemDealerRptNameSort.setMnemonic(KeyEvent.VK_N);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDealerRptNameSort);
				ivjmenuItemDealerRptNameSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.DEALER_RPT_ALPHA_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDealerRptNameSort;
	}

	/**
	 * Return the menuItemDealerTitles property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDealerTitles()
	{
		if (ivjmenuItemDealerTitles == null)
		{
			try
			{
				ivjmenuItemDealerTitles = new javax.swing.JMenuItem();
				ivjmenuItemDealerTitles.setName("menuItemDealerTitles");
				//ivjmenuItemDealerTitles.setMnemonic(68);
				ivjmenuItemDealerTitles.setMnemonic(KeyEvent.VK_D);
				ivjmenuItemDealerTitles.setText(DEALER_TITLES);
				ivjmenuItemDealerTitles.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_D,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemDealerTitles);
				ivjmenuItemDealerTitles.setActionCommand(
					ScreenConstant.DTA001
						+ DELIM
						+ TransCdConstant.DTAORD);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDealerTitles;
	}
	/**
	 * Return the menuItemDealerUpdates property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDealerUpdates()
	{
		if (ivjmenuItemDealerUpdates == null)
		{
			try
			{
				ivjmenuItemDealerUpdates = new javax.swing.JMenuItem();
				ivjmenuItemDealerUpdates.setName(
					"menuItemDealerUpdates");
				ivjmenuItemDealerUpdates.setMnemonic(KeyEvent.VK_D);
				ivjmenuItemDealerUpdates.setText(DEALER_UPDS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDealerUpdates);
				ivjmenuItemDealerUpdates.setActionCommand(
					ScreenConstant.OPT001);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDealerUpdates;
	}
	/**
	 * Return the menuItemDeductHotCheck property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDeductHotCheck()
	{
		if (ivjmenuItemDeductHotCheck == null)
		{
			try
			{
				ivjmenuItemDeductHotCheck = new javax.swing.JMenuItem();
				ivjmenuItemDeductHotCheck.setName(
					"menuItemDeductHotCheck");
				ivjmenuItemDeductHotCheck.setMnemonic(KeyEvent.VK_E);
				ivjmenuItemDeductHotCheck.setText(DEDUCT_HOTCHK_CRDT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDeductHotCheck);
				ivjmenuItemDeductHotCheck.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.HOTDED);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDeductHotCheck;
	}
	/**
	 * Return the menuItemDelete property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDelete()
	{
		if (ivjmenuItemDelete == null)
		{
			try
			{
				ivjmenuItemDelete = new javax.swing.JMenuItem();
				ivjmenuItemDelete.setName("menuItemDelete");
				ivjmenuItemDelete.setMnemonic(KeyEvent.VK_D);
				ivjmenuItemDelete.setText(DELETE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDelete);
				ivjmenuItemDelete.setActionCommand(
					ScreenConstant.INV026);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDelete;
	}
	/**
	 * Return the menuItemDeleteTitle property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDeleteTitle()
	{
		if (ivjmenuItemDeleteTitle == null)
		{
			try
			{
				ivjmenuItemDeleteTitle = new javax.swing.JMenuItem();
				ivjmenuItemDeleteTitle.setName("menuItemDeleteTitle");
				ivjmenuItemDeleteTitle.setMnemonic(KeyEvent.VK_L);
				ivjmenuItemDeleteTitle.setText(DELETE_TIP);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDeleteTitle);
				ivjmenuItemDeleteTitle.setActionCommand(
					ScreenConstant.KEY007);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDeleteTitle;
	}
	/**
	 * Return the menuItemDetailReports property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDetailReports()
	{
		if (ivjmenuItemDetailReports == null)
		{
			try
			{
				ivjmenuItemDetailReports = new javax.swing.JMenuItem();
				ivjmenuItemDetailReports.setName(
					"menuItemDetailReports");
				ivjmenuItemDetailReports.setMnemonic(KeyEvent.VK_D);
				ivjmenuItemDetailReports.setText(DETAIL_RPTS);
				ivjmenuItemDetailReports.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_1,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemDetailReports);
				ivjmenuItemDetailReports.setActionCommand(
					ScreenConstant.FUN006
						+ DELIM
						+ TransCdConstant.DETAIL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDetailReports;
	}

	/**
	 * Return the ivjmenuItemPlacardManagement property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemDisabledPlacardInquiry()
	{
		if (ivjmenuItemPlacardInquiry == null)
		{
			try
			{
				ivjmenuItemPlacardInquiry = new javax.swing.JMenuItem();
				ivjmenuItemPlacardInquiry.setName(
					"menuItemPlacardInquiry");
				ivjmenuItemPlacardInquiry.setMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjmenuItemPlacardInquiry.setText("Placard Inquiry");
				ivjmenuItemPlacardInquiry.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_2,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemPlacardInquiry);
				ivjmenuItemPlacardInquiry.setActionCommand(
					ScreenConstant.MRG020
						+ DELIM
						+ MiscellaneousRegConstant.INQ);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPlacardInquiry;
	}
	// defect 9831 
	//	/**
	//	 * Return the menuItemDisabledParking property value.
	//	 * 
	//	 * @return JMenuItem
	//	 */
	//
	//	private JMenuItem getmenuItemDisabledParkingAdd()
	//	{
	//		if (ivjmenuItemDisabledParkingAdd == null)
	//		{
	//			try
	//			{
	//				ivjmenuItemDisabledParkingAdd =
	//					new javax.swing.JMenuItem();
	//				ivjmenuItemDisabledParkingAdd.setName(
	//					"menuItemDisabledParkingAdd");
	//				ivjmenuItemDisabledParkingAdd.setMnemonic('d');
	//				ivjmenuItemDisabledParkingAdd.setText("ADD Current");
	//				// user code begin {1}
	//				actionComponents.add(ivjmenuItemDisabledParkingAdd);
	//				ivjmenuItemDisabledParkingAdd.setActionCommand(
	//					ScreenConstant.MRG002);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjmenuItemDisabledParkingAdd;
	//	}
	// end defect 9831 

	/**
	 * Return the ivjmenuItemPlacardManagement property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDisabledPlacardManagement()
	{
		if (ivjmenuItemPlacardManagement == null)
		{
			try
			{
				ivjmenuItemPlacardManagement =
					new javax.swing.JMenuItem();
				ivjmenuItemPlacardManagement.setName(
					"menuItemDisabledParkingApplication");
				ivjmenuItemPlacardManagement.setMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjmenuItemPlacardManagement.setText(
					"Placard Management");
				ivjmenuItemPlacardManagement.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_B,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemPlacardManagement);
				// defect 10133 
				ivjmenuItemPlacardManagement.setActionCommand(
					ScreenConstant.MRG020
						+ DELIM
						+ TransCdConstant.PDC);
				// end defect 10133 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPlacardManagement;
	}

	/**
	 * Return the menuItemDisabledParking property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemDisabledPlacardReport()
	{
		if (ivjmenuItemPlacardReport == null)
		{
			try
			{
				ivjmenuItemPlacardReport = new javax.swing.JMenuItem();
				ivjmenuItemPlacardReport.setName(
					"menuItemDisabledPlacardReport");
				ivjmenuItemPlacardReport.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjmenuItemPlacardReport.setText("Report");
				// user code begin {1}
				actionComponents.add(ivjmenuItemPlacardReport);
				ivjmenuItemPlacardReport.setActionCommand(
					ScreenConstant.MRG027);
				ivjmenuItemPlacardReport.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_9,
						Event.CTRL_MASK));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPlacardReport;
	}

	//	/**
	//	 * Return the menuItemDisabledParking property value.
	//	 * 
	//	 * @return JMenuItem
	//	 */
	//
	//	private JMenuItem getmenuItemDisabledParkingReport()
	//	{
	//		if (ivjmenuItemDisabledParkingReport == null)
	//		{
	//			try
	//			{
	//				ivjmenuItemDisabledParkingReport =
	//					new javax.swing.JMenuItem();
	//				ivjmenuItemDisabledParkingReport.setName(
	//					"menuItemDisabledParkingSearch");
	//				//ivjmenuItemDisabledParkingReport.setMnemonic('d');
	//				ivjmenuItemDisabledParkingReport.setText("Report");
	//				// user code begin {1}
	//				actionComponents.add(ivjmenuItemDisabledParkingReport);
	//				ivjmenuItemDisabledParkingReport.setActionCommand(
	//					ScreenConstant.MRG002);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjmenuItemDisabledParkingReport;
	//	}
	/**
	 * Return the menuItemDriverEd property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDriverEd()
	{
		if (ivjmenuItemDriverEd == null)
		{
			try
			{
				ivjmenuItemDriverEd = new javax.swing.JMenuItem();
				ivjmenuItemDriverEd.setName("menuItemDriverEd");
				ivjmenuItemDriverEd.setMnemonic(KeyEvent.VK_I);
				ivjmenuItemDriverEd.setText(ISSUE_DRVR_ED_PLT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemDriverEd);
				// defect 6445
				// Set the appropriate menu item.
				ivjmenuItemDriverEd.setActionCommand(
					ScreenConstant.REG032);
				// end defect 6445
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDriverEd;
	}
	/**
	 * Return the menuItemDuplicateReceipt property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemDuplicateReceipt()
	{
		if (ivjmenuItemDuplicateReceipt == null)
		{
			try
			{
				ivjmenuItemDuplicateReceipt =
					new javax.swing.JMenuItem();
				ivjmenuItemDuplicateReceipt.setName(
					"menuItemDuplicateReceipt");
				ivjmenuItemDuplicateReceipt.setMnemonic(KeyEvent.VK_D);
				ivjmenuItemDuplicateReceipt.setText(DUP_RCPT);
				ivjmenuItemDuplicateReceipt.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_F,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemDuplicateReceipt);
				ivjmenuItemDuplicateReceipt.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.DUPL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemDuplicateReceipt;
	}

	/**
	 * Return the menuItemElectronicTitleReport property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemElectronicTitleReport()
	{
		if (ivjmenuItemElectronicTitleReport == null)
		{
			try
			{
				ivjmenuItemElectronicTitleReport =
					new javax.swing.JMenuItem();
				ivjmenuItemElectronicTitleReport.setMnemonic(
					KeyEvent.VK_L);
				ivjmenuItemElectronicTitleReport.setText(ETITLE_RPT);
				actionComponents.add(ivjmenuItemElectronicTitleReport);
				ivjmenuItemElectronicTitleReport.setActionCommand(
					ScreenConstant.RPR006);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemElectronicTitleReport;
	}
	/**
	 * Return the menuItemEmployeeSecurity property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemEmployeeSecurity()
	{
		if (ivjmenuItemEmployeeSecurity == null)
		{
			try
			{
				ivjmenuItemEmployeeSecurity =
					new javax.swing.JMenuItem();
				ivjmenuItemEmployeeSecurity.setName(
					"menuItemEmployeeSecurity");
				ivjmenuItemEmployeeSecurity.setMnemonic(KeyEvent.VK_E);
				ivjmenuItemEmployeeSecurity.setText(EMPL_SECURITY);
				// user code begin {1}
				actionComponents.add(ivjmenuItemEmployeeSecurity);
				ivjmenuItemEmployeeSecurity.setActionCommand(
					ScreenConstant.SEC005);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemEmployeeSecurity;
	}
	/**
	 * Return the menuItemEmployeeSecurityReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemEmployeeSecurityReport()
	{
		if (ivjmenuItemEmployeeSecurityReport == null)
		{
			try
			{
				ivjmenuItemEmployeeSecurityReport =
					new javax.swing.JMenuItem();
				ivjmenuItemEmployeeSecurityReport.setName(
					"menuItemEmployeeSecurityReport");
				ivjmenuItemEmployeeSecurityReport.setMnemonic(
					KeyEvent.VK_M);
				ivjmenuItemEmployeeSecurityReport.setText(
					EMPL_SECURITY_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemEmployeeSecurityReport);
				ivjmenuItemEmployeeSecurityReport.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.EMPSEC_RPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemEmployeeSecurityReport;
	}
	/**
	 * Return the menuItemEventSecurityReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemEventSecurityReport()
	{
		if (ivjmenuItemEventSecurityReport == null)
		{
			try
			{
				ivjmenuItemEventSecurityReport =
					new javax.swing.JMenuItem();
				ivjmenuItemEventSecurityReport.setName(
					"menuItemEventSecurityReport");
				ivjmenuItemEventSecurityReport.setMnemonic(
					KeyEvent.VK_V);
				ivjmenuItemEventSecurityReport.setText(
					EVENT_SECURITY_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemEventSecurityReport);
				ivjmenuItemEventSecurityReport.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.EVTSEC_RPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemEventSecurityReport;
	}
	/**
	 * Return the menuItemExchange property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemExchange()
	{
		if (ivjmenuItemExchange == null)
		{
			try
			{
				ivjmenuItemExchange = new javax.swing.JMenuItem();
				ivjmenuItemExchange.setName("menuItemExchange");
				ivjmenuItemExchange.setMnemonic(KeyEvent.VK_X);
				ivjmenuItemExchange.setText(EXCHANGE);
				ivjmenuItemExchange.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_X,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemExchange);
				ivjmenuItemExchange.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.EXCH);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemExchange;
	}
	/**
	 * Return the menuItemExemptAuditReport property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemExemptAuditReport()
	{
		if (ivjmenuItemExemptAuditReport == null)
		{
			try
			{
				ivjmenuItemExemptAuditReport =
					new javax.swing.JMenuItem();
				ivjmenuItemExemptAuditReport.setMnemonic(KeyEvent.VK_X);
				ivjmenuItemExemptAuditReport.setText(EXEMPT_AUDIT_RPT);
				actionComponents.add(ivjmenuItemExemptAuditReport);
				ivjmenuItemExemptAuditReport.setActionCommand(
					ScreenConstant.RPR005);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemExemptAuditReport;
	}
	/**
	 * Return the menuItemFundsBalanceReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemFundsBalanceReport()
	{
		if (ivjmenuItemFundsBalanceReport == null)
		{
			try
			{
				ivjmenuItemFundsBalanceReport =
					new javax.swing.JMenuItem();
				ivjmenuItemFundsBalanceReport.setName(
					"menuItemFundsBalanceReport");
				ivjmenuItemFundsBalanceReport.setMnemonic(
					KeyEvent.VK_F);
				ivjmenuItemFundsBalanceReport.setText(FUNDS_BAL_RPTS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemFundsBalanceReport);
				ivjmenuItemFundsBalanceReport.setActionCommand(
					ScreenConstant.FUN006
						+ DELIM
						+ TransCdConstant.BALANCE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemFundsBalanceReport;
	}
	/**
	 * Return the menuItemFundsInquiry property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemFundsInquiry()
	{
		if (ivjmenuItemFundsInquiry == null)
		{
			try
			{
				ivjmenuItemFundsInquiry = new javax.swing.JMenuItem();
				ivjmenuItemFundsInquiry.setName("menuItemFundsInquiry");
				ivjmenuItemFundsInquiry.setMnemonic(KeyEvent.VK_F);
				ivjmenuItemFundsInquiry.setText(FUNDS_INQ);
				// user code begin {1}
				actionComponents.add(ivjmenuItemFundsInquiry);
				ivjmenuItemFundsInquiry.setActionCommand(
					ScreenConstant.KEY021);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemFundsInquiry;
	}
	/**
	 * Return the menuItemHoldRelease property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemHoldRelease()
	{
		if (ivjmenuItemHoldRelease == null)
		{
			try
			{
				ivjmenuItemHoldRelease = new javax.swing.JMenuItem();
				ivjmenuItemHoldRelease.setName("menuItemHoldRelease");
				ivjmenuItemHoldRelease.setMnemonic(KeyEvent.VK_H);
				ivjmenuItemHoldRelease.setText(HOLD_RELEASE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemHoldRelease);
				ivjmenuItemHoldRelease.setActionCommand(
					ScreenConstant.INV017);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemHoldRelease;
	}
	/**
	 * Return the menuItemHotCheckCredit property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemHotCheckCredit()
	{
		if (ivjmenuItemHotCheckCredit == null)
		{
			try
			{
				ivjmenuItemHotCheckCredit = new javax.swing.JMenuItem();
				ivjmenuItemHotCheckCredit.setName(
					"menuItemHotCheckCredit");
				ivjmenuItemHotCheckCredit.setMnemonic(KeyEvent.VK_H);
				ivjmenuItemHotCheckCredit.setText(HOTCHK_CREDIT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemHotCheckCredit);
				ivjmenuItemHotCheckCredit.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.HOTCK);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemHotCheckCredit;
	}
	/**
	 * Return the menuItemHotCheckRedeemed property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemHotCheckRedeemed()
	{
		if (ivjmenuItemHotCheckRedeemed == null)
		{
			try
			{
				ivjmenuItemHotCheckRedeemed =
					new javax.swing.JMenuItem();
				ivjmenuItemHotCheckRedeemed.setName(
					"menuItemHotCheckRedeemed");
				ivjmenuItemHotCheckRedeemed.setMnemonic(KeyEvent.VK_C);
				ivjmenuItemHotCheckRedeemed.setText(HOTCHK_REDEEM);
				// user code begin {1}
				actionComponents.add(ivjmenuItemHotCheckRedeemed);
				ivjmenuItemHotCheckRedeemed.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.CKREDM);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemHotCheckRedeemed;
	}
	/**
	 * Return the menuItemInquiry property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemInquiry()
	{
		if (ivjmenuItemInquiry == null)
		{
			try
			{
				ivjmenuItemInquiry = new javax.swing.JMenuItem();
				ivjmenuItemInquiry.setName("menuItemInquiry");
				ivjmenuItemInquiry.setMnemonic(KeyEvent.VK_I);
				ivjmenuItemInquiry.setText(INQUIRY);
				// user code begin {1}
				actionComponents.add(ivjmenuItemInquiry);
				// defect 6964
				ivjmenuItemInquiry.setActionCommand(
					ScreenConstant.INV020);
				// end defect 6964
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemInquiry;
	}
	/**
	 * Return the menuItemInventoryActionReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemInventoryActionReport()
	{
		if (ivjmenuItemInventoryActionReport == null)
		{
			try
			{
				ivjmenuItemInventoryActionReport =
					new javax.swing.JMenuItem();
				ivjmenuItemInventoryActionReport.setName(
					"menuItemInventoryActionReport");
				ivjmenuItemInventoryActionReport.setMnemonic(
					KeyEvent.VK_T);
				ivjmenuItemInventoryActionReport.setText(
					INV_ACTION_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemInventoryActionReport);
				ivjmenuItemInventoryActionReport.setActionCommand(
					ScreenConstant.INV019);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemInventoryActionReport;
	}
	// end defect 9117

	/**
	 * Return the menuItemInventoryHistory property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemInventoryHistory()
	{
		if (ivjmenuItemInventoryHistory == null)
		{
			try
			{
				ivjmenuItemInventoryHistory =
					new javax.swing.JMenuItem();
				ivjmenuItemInventoryHistory.setName(
					"menuItemInventoryHistory");
				ivjmenuItemInventoryHistory.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemInventoryHistory.setText(INV_HIST);
				// user code begin {1}
				actionComponents.add(ivjmenuItemInventoryHistory);
				ivjmenuItemInventoryHistory.setActionCommand(
				// defect 6965
				ScreenConstant.INV015);
				// end defect 6965
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemInventoryHistory;
	}
	/**
	 * Return the menuItemItemSeized property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemItemSeized()
	{
		if (ivjmenuItemItemSeized == null)
		{
			try
			{
				ivjmenuItemItemSeized = new javax.swing.JMenuItem();
				ivjmenuItemItemSeized.setName("menuItemItemSeized");
				ivjmenuItemItemSeized.setMnemonic(KeyEvent.VK_I);
				ivjmenuItemItemSeized.setText(ITEMS_SEIZED);
				// user code begin {1}
				actionComponents.add(ivjmenuItemItemSeized);
				ivjmenuItemItemSeized.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.HCKITM);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemItemSeized;
	}
	/**
	 * Return the ivjmenuItemLienholderReportIdSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemLienholderReportIdSort()
	{
		if (ivjmenuItemLienholderReportIdSort == null)
		{
			try
			{
				ivjmenuItemLienholderReportIdSort =
					new javax.swing.JMenuItem();
				ivjmenuItemLienholderReportIdSort.setName(
					"ivjmenuItemLienholderReportIdSort");
				ivjmenuItemLienholderReportIdSort.setMnemonic(
					KeyEvent.VK_I);
				ivjmenuItemLienholderReportIdSort.setText(SORT_BY_ID);
				// user code begin {1}
				actionComponents.add(ivjmenuItemLienholderReportIdSort);
				ivjmenuItemLienholderReportIdSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.LIENHLDR_RPT_ID_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemLienholderReportIdSort;
	}

	/**
	 * Return the ivjmenuItemLienholderReportNameSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemLienholderReportNameSort()
	{
		if (ivjmenuItemLienholderReportNameSort == null)
		{
			try
			{
				ivjmenuItemLienholderReportNameSort =
					new javax.swing.JMenuItem();
				ivjmenuItemLienholderReportNameSort.setName(
					"ivjmenuItemLienholderReportNameSort");
				ivjmenuItemLienholderReportNameSort.setMnemonic(
					KeyEvent.VK_N);
				ivjmenuItemLienholderReportNameSort.setText(
					SORT_BY_NAME);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemLienholderReportNameSort);
				ivjmenuItemLienholderReportNameSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.LIENHLDR_RPT_ALPHA_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemLienholderReportNameSort;
	}
	/**
	 * Return the menuItemLienholderUpdates property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemLienholderUpdates()
	{
		if (ivjmenuItemLienholderUpdates == null)
		{
			try
			{
				ivjmenuItemLienholderUpdates =
					new javax.swing.JMenuItem();
				ivjmenuItemLienholderUpdates.setName(
					"menuItemLienholderUpdates");
				ivjmenuItemLienholderUpdates.setMnemonic(KeyEvent.VK_L);
				ivjmenuItemLienholderUpdates.setText(LIENHLDR_UPDS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemLienholderUpdates);
				ivjmenuItemLienholderUpdates.setActionCommand(
					ScreenConstant.OPT002);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemLienholderUpdates;
	}
	/**
	 * Return the menuItemLogon property value.
	 * 
	 * @return JMenuItem
	 */

	public JMenuItem getmenuItemLogon()
	{
		if (ivjmenuItemLogon == null)
		{
			try
			{
				ivjmenuItemLogon = new javax.swing.JMenuItem();
				ivjmenuItemLogon.setName("menuItemLogon");
				// defect 11354
				ivjmenuItemLogon.setMnemonic(KeyEvent.VK_X);
				// end defect 11354
				ivjmenuItemLogon.setVisible(true);
				// user code begin {1}
				actionComponents.add(ivjmenuItemLogon);
				// defect 6955
				ivjmenuItemLogon.setText(LOGOFF);
				ivjmenuItemLogon.setActionCommand(
					ScreenConstant.LOGOFF);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemLogon;
	}
	/**
	 * Return the menuItemModify property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemModify()
	{
		if (ivjmenuItemModify == null)
		{
			try
			{
				ivjmenuItemModify = new javax.swing.JMenuItem();
				ivjmenuItemModify.setName("menuItemModify");
				ivjmenuItemModify.setMnemonic(KeyEvent.VK_M);
				ivjmenuItemModify.setText(MODIFY);
				ivjmenuItemModify.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_M,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemModify);
				ivjmenuItemModify.setActionCommand(
					ScreenConstant.REG024);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemModify;
	}

	/**
	 * Return the ivjmenuItemMyPlates property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemMyPlates()
	{
		if (ivjmenuItemMyPlates == null)
		{
			try
			{
				ivjmenuItemMyPlates = new javax.swing.JMenuItem();
				ivjmenuItemMyPlates.setName("ivjmenuItemMyPlates");
				ivjmenuItemMyPlates.setMnemonic(KeyEvent.VK_M);
				ivjmenuItemMyPlates.setText(MYPLATES);
				// user code begin {1}
				actionComponents.add(ivjmenuItemMyPlates);

				// defect 10575
				ivjmenuItemMyPlates.setActionCommand(
				//"Launch#remotehttp:http://rts.myplates.com");
				"Launch#remotehttp:http://"
					+ SystemProperty.getMyPlatesURL());
				// end defect 10575 

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemMyPlates;
	}

	/**
	 * Return the menuItemNextCustomer property value.
	 * 
	 * @return JRadioButtonMenuItem
	 */
	private JRadioButtonMenuItem getmenuItemNextCustomer()
	{
		if (ivjmenuItemNextCustomer == null)
		{
			try
			{
				ivjmenuItemNextCustomer =
					new javax.swing.JRadioButtonMenuItem();
				ivjmenuItemNextCustomer.setName("menuItemNextCustomer");
				ivjmenuItemNextCustomer.setMnemonic(KeyEvent.VK_N);
				ivjmenuItemNextCustomer.setText(NEXT_CUST);
				ivjmenuItemNextCustomer.setEnabled(false);
				// user code begin {1}
				ivjmenuItemNextCustomer.setActionCommand(
					RadioMenuActionListener.NEXT_CUSTOMER);

				ivjmenuItemNextCustomer.addActionListener(
					radioMenuActionListener);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemNextCustomer;
	}
	/**
	 * Return the ivjmenuItemNICUSA property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemNICUSA()
	{
		if (ivjmenuItemNICUSA == null)
		{
			try
			{
				ivjmenuItemNICUSA = new javax.swing.JMenuItem();
				ivjmenuItemNICUSA.setName("ivjmenuItemNICUSA");
				ivjmenuItemNICUSA.setMnemonic(KeyEvent.VK_U);
				ivjmenuItemNICUSA.setText(NICUSA);
				// user code begin {1}
				actionComponents.add(ivjmenuItemNICUSA);
				ivjmenuItemNICUSA.setActionCommand(
				//"Launch#remotehttp:http://www.nicusa.com/login.aspx");
				"Launch#remotehttp:https://tpe2admin.cdc.nicusa.com/texas/Login.aspx");
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemNICUSA;
	}

	/**
	 * Return the menuItemNonResidentAgPermit property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemNonResidentAgPermit()
	{
		if (ivjmenuItemNonResidentAgPermit == null)
		{
			try
			{
				ivjmenuItemNonResidentAgPermit =
					new javax.swing.JMenuItem();
				ivjmenuItemNonResidentAgPermit.setName(
					"menuItemNonResidentAgPermit");
				ivjmenuItemNonResidentAgPermit.setMnemonic(
					KeyEvent.VK_N);
				ivjmenuItemNonResidentAgPermit.setText(
					NONRES_AG_PERMIT);
				ivjmenuItemNonResidentAgPermit.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_N,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemNonResidentAgPermit);
				ivjmenuItemNonResidentAgPermit.setActionCommand(
					ScreenConstant.MRG013);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemNonResidentAgPermit;
	}
	/**
	 * Return the menuItemOff property value.
	 * 
	 * @return JRadioButtonMenuItem
	 */

	private JRadioButtonMenuItem getmenuItemOff()
	{
		if (ivjmenuItemOff == null)
		{
			try
			{
				ivjmenuItemOff = new javax.swing.JRadioButtonMenuItem();
				ivjmenuItemOff.setName("menuItemOff");
				ivjmenuItemOff.setSelected(false);
				ivjmenuItemOff.setMnemonic(KeyEvent.VK_F);
				ivjmenuItemOff.setText(
					RadioMenuActionListener.PRINT_IMMEDIATE_OFF);
				ivjmenuItemOff.setEnabled(false);
				// user code begin {1}
				ivjmenuItemOff.setActionCommand(
					RadioMenuActionListener.PRINT_IMMEDIATE_OFF);
				ivjmenuItemOff.addActionListener(
					radioMenuActionListener);
				bgPrintImmediate.add(ivjmenuItemOff);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemOff;
	}
	/**
	 * Return the menuItemOn property value.
	 * 
	 * @return JRadioButtonMenuItem
	 */

	private JRadioButtonMenuItem getmenuItemOn()
	{
		if (ivjmenuItemOn == null)
		{
			try
			{
				ivjmenuItemOn = new javax.swing.JRadioButtonMenuItem();
				ivjmenuItemOn.setName("menuItemOn");
				ivjmenuItemOn.setMnemonic(KeyEvent.VK_O);
				ivjmenuItemOn.setText(
					RadioMenuActionListener.PRINT_IMMEDIATE_ON);
				ivjmenuItemOn.setEnabled(false);
				// user code begin {1}
				ivjmenuItemOn.setActionCommand(
					RadioMenuActionListener.PRINT_IMMEDIATE_ON);
				ivjmenuItemOn.addActionListener(
					radioMenuActionListener);
				bgPrintImmediate.add(ivjmenuItemOn);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemOn;
	}
	/**
	 * Return the menuItemPaymentAccountUpdates property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPaymentAccountUpdates()
	{
		if (ivjmenuItemPaymentAccountUpdates == null)
		{
			try
			{
				ivjmenuItemPaymentAccountUpdates =
					new javax.swing.JMenuItem();
				ivjmenuItemPaymentAccountUpdates.setName(
					"menuItemPaymentAccountUpdates");
				ivjmenuItemPaymentAccountUpdates.setMnemonic(
					KeyEvent.VK_P);
				ivjmenuItemPaymentAccountUpdates.setText(PAY_ACCT_UPDS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemPaymentAccountUpdates);
				ivjmenuItemPaymentAccountUpdates.setActionCommand(
					ScreenConstant.OPT004);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPaymentAccountUpdates;
	}

	/**
	 * Return the ivjmenuItemPermitApplication property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPermitApplication()
	{
		if (ivjmenuItemPermitApplication == null)
		{
			try
			{
				ivjmenuItemPermitApplication =
					new javax.swing.JMenuItem();
				ivjmenuItemPermitApplication.setName(
					"ivjmenuItemPermitApplication");
				ivjmenuItemPermitApplication.setMnemonic(KeyEvent.VK_A);
				ivjmenuItemPermitApplication.setText(
					"Permit Application");
				ivjmenuItemPermitApplication.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_T,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemPermitApplication);
				ivjmenuItemPermitApplication.setActionCommand(
					ScreenConstant.MRG007
						+ DELIM
						+ TransCdConstant.PT72);

				//	ivjmenuItemPermitApplication.setActionCommand(
				//	ScreenConstant.MRG005);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPermitApplication;
	}

	/**
	 * Return the ivjmenuItemPermitInquiry property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPermitInquiry()
	{
		if (ivjmenuItemPermitInquiry == null)
		{
			try
			{
				ivjmenuItemPermitInquiry = new javax.swing.JMenuItem();
				ivjmenuItemPermitInquiry.setName(
					"ivjmenuItemPermitInquiry");
				ivjmenuItemPermitInquiry.setMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjmenuItemPermitInquiry.setText("Permit Inquiry");
				ivjmenuItemPermitInquiry.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_3,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemPermitInquiry);
				ivjmenuItemPermitInquiry.setActionCommand(
					ScreenConstant.MRG002
						+ DELIM
						+ MiscellaneousRegConstant.PRMINQ);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPermitInquiry;
	}
	/**
	 * Return the ivjmenuItemPermitModify property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPermitModify()
	{
		if (ivjmenuItemPermitModify == null)
		{
			try
			{
				ivjmenuItemPermitModify = new javax.swing.JMenuItem();
				ivjmenuItemPermitModify.setName(
					"ivjmenuItemPermitModify");
				ivjmenuItemPermitModify.setMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjmenuItemPermitModify.setText("Permit Modify");
				// user code begin {1}
				actionComponents.add(ivjmenuItemPermitModify);
				ivjmenuItemPermitModify.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_Y,
						Event.CTRL_MASK));
				ivjmenuItemPermitModify.setActionCommand(
					ScreenConstant.MRG002
						+ DELIM
						+ TransCdConstant.MODPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPermitModify;
	}

	/**
	 * Return the ivjmenuItemPermitDuplicateReceipt property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPermitDuplicateReceipt()
	{
		if (ivjmenuItemPermitDuplicateReceipt == null)
		{
			try
			{
				ivjmenuItemPermitDuplicateReceipt =
					new javax.swing.JMenuItem();
				ivjmenuItemPermitDuplicateReceipt.setName(
					"ivjmenuItemPermitDuplicateReceipt");
				ivjmenuItemPermitDuplicateReceipt.setMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjmenuItemPermitDuplicateReceipt.setText(
					"Permit Duplicate Receipt");
				ivjmenuItemPermitDuplicateReceipt.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_8,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemPermitDuplicateReceipt);
				ivjmenuItemPermitDuplicateReceipt.setActionCommand(
					ScreenConstant.MRG002
						+ DELIM
						+ TransCdConstant.PRMDUP);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPermitDuplicateReceipt;
	}

	/**
	 * Return the menuItemPlateOptions property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemPlateOptions()
	{
		if (ivjmenuItemPlateOptions == null)
		{
			try
			{
				ivjmenuItemPlateOptions = new javax.swing.JMenuItem();
				ivjmenuItemPlateOptions.setName("menuItemPlateOptions");
				ivjmenuItemPlateOptions.setMnemonic(KeyEvent.VK_O);
				ivjmenuItemPlateOptions.setText(PLATE_OPTS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemPlateOptions);
				ivjmenuItemPlateOptions.setActionCommand(
					ScreenConstant.REG008
						+ DELIM
						+ TransCdConstant.PLTOPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPlateOptions;
	}
	/**
	 * Return the menuItemProcessHold property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemProcessHold()
	{
		if (ivjmenuItemProcessHold == null)
		{
			try
			{
				ivjmenuItemProcessHold = new javax.swing.JMenuItem();
				ivjmenuItemProcessHold.setName("menuItemProcessHold");
				ivjmenuItemProcessHold.setMnemonic(KeyEvent.VK_H);
				ivjmenuItemProcessHold.setText(PROCESS_HOLD);
				// user code begin {1}
				actionComponents.add(ivjmenuItemProcessHold);

				// defect 7889
				// Renamed CP009 to REG103
				ivjmenuItemProcessHold.setActionCommand(
					ScreenConstant.REG103
						+ DELIM
						+ TransCdConstant.IRENEW);
				// end defect 7889

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemProcessHold;
	}
	/**
	 * Return the menuItemProcessNew property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemProcessNew()
	{
		if (ivjmenuItemProcessNew == null)
		{
			try
			{
				ivjmenuItemProcessNew = new javax.swing.JMenuItem();
				ivjmenuItemProcessNew.setName("menuItemProcessNew");
				ivjmenuItemProcessNew.setMnemonic(KeyEvent.VK_N);
				ivjmenuItemProcessNew.setText(PROCESS_NEW);
				// user code begin {1}
				actionComponents.add(ivjmenuItemProcessNew);
				// defect 7889
				// Renamed CP008 to REG103
				ivjmenuItemProcessNew.setActionCommand(
					ScreenConstant.REG103
						+ DELIM
						+ TransCdConstant.IRENEW);
				// end defect 7889
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemProcessNew;
	}
	/**
	 * Return the menuItemProfile property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemProfile()
	{
		if (ivjmenuItemProfile == null)
		{
			try
			{
				ivjmenuItemProfile = new javax.swing.JMenuItem();
				ivjmenuItemProfile.setName("menuItemProfile");
				ivjmenuItemProfile.setMnemonic(KeyEvent.VK_P);
				ivjmenuItemProfile.setText(PROFILE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemProfile);
				ivjmenuItemProfile.setActionCommand(
					ScreenConstant.INV016);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemProfile;
	}
	/**
	 * Return the menuItemProfileReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemProfileReport()
	{
		if (ivjmenuItemProfileReport == null)
		{
			try
			{
				ivjmenuItemProfileReport = new javax.swing.JMenuItem();
				ivjmenuItemProfileReport.setName(
					"menuItemProfileReport");
				ivjmenuItemProfileReport.setMnemonic(KeyEvent.VK_E);
				ivjmenuItemProfileReport.setText(PROFILE_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemProfileReport);
				ivjmenuItemProfileReport.setActionCommand(
					ScreenConstant.INV030);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemProfileReport;
	}
	/**
	 * Return the menuItemPublishingReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemPublishingReport()
	{
		if (ivjmenuItemPublishingReport == null)
		{
			try
			{
				ivjmenuItemPublishingReport =
					new javax.swing.JMenuItem();
				ivjmenuItemPublishingReport.setName(
					"menuItemPublishingReport");
				ivjmenuItemPublishingReport.setMnemonic(KeyEvent.VK_P);
				ivjmenuItemPublishingReport.setText(PUBLISH_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemPublishingReport);
				ivjmenuItemPublishingReport.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.PUBLISHING_RPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPublishingReport;
	}
	/**
	 * Return the menuItemPublishingUpdate property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemPublishingUpdate()
	{
		if (ivjmenuItemPublishingUpdate == null)
		{
			try
			{
				ivjmenuItemPublishingUpdate =
					new javax.swing.JMenuItem();
				ivjmenuItemPublishingUpdate.setName(
					"menuItemPublishingUpdate");
				ivjmenuItemPublishingUpdate.setMnemonic(KeyEvent.VK_U);
				ivjmenuItemPublishingUpdate.setText(PUBLISH_UPD);
				// user code begin {1}
				actionComponents.add(ivjmenuItemPublishingUpdate);
				ivjmenuItemPublishingUpdate.setActionCommand(
					ScreenConstant.PUB002);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemPublishingUpdate;
	}
	/**
	 * Return the menuItemReceiveInvoice property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemReceiveInvoice()
	{
		if (ivjmenuItemReceiveInvoice == null)
		{
			try
			{
				ivjmenuItemReceiveInvoice = new javax.swing.JMenuItem();
				ivjmenuItemReceiveInvoice.setName(
					"menuItemReceiveInvoice");
				ivjmenuItemReceiveInvoice.setMnemonic(KeyEvent.VK_R);
				ivjmenuItemReceiveInvoice.setText(RECEIVE_INVOICE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemReceiveInvoice);
				ivjmenuItemReceiveInvoice.setActionCommand(
					ScreenConstant.INV004);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReceiveInvoice;
	}
	/**
	 * Return the menuItemRefund property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRefund()
	{
		if (ivjmenuItemRefund == null)
		{
			try
			{
				ivjmenuItemRefund = new javax.swing.JMenuItem();
				ivjmenuItemRefund.setName("menuItemRefund");
				ivjmenuItemRefund.setMnemonic(KeyEvent.VK_U);
				ivjmenuItemRefund.setText(REFUND);
				// user code begin {1}
				actionComponents.add(ivjmenuItemRefund);
				ivjmenuItemRefund.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.REFUND);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRefund;
	}
	/**
	 * Return the menuItemRegionalCollection property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRegionalCollection()
	{
		if (ivjmenuItemRegionalCollection == null)
		{
			try
			{
				ivjmenuItemRegionalCollection =
					new javax.swing.JMenuItem();
				ivjmenuItemRegionalCollection.setName(
					"menuItemRegionalCollection");
				ivjmenuItemRegionalCollection.setMnemonic(
					KeyEvent.VK_G);
				ivjmenuItemRegionalCollection.setText(REG_COLLECT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemRegionalCollection);
				ivjmenuItemRegionalCollection.setActionCommand(
					ScreenConstant.ACC002
						+ DELIM
						+ TransCdConstant.RGNCOL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRegionalCollection;
	}

	/**
	 * Return the menuItemSuspectedFraudReport property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSuspectedFraudReport()
	{
		if (ivjmenuItemSuspectedFraudReport == null)
		{
			try
			{
				ivjmenuItemSuspectedFraudReport =
					new javax.swing.JMenuItem();
				ivjmenuItemSuspectedFraudReport.setMnemonic(
					KeyEvent.VK_F);
				ivjmenuItemSuspectedFraudReport.setText(
					"Suspected Fraud Report");
				actionComponents.add(ivjmenuItemSuspectedFraudReport);
				ivjmenuItemSuspectedFraudReport.setActionCommand(
					ScreenConstant.RPR009);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSuspectedFraudReport;
	}

	/**
	 * Return the menuItemRenewal property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRenewal()
	{
		if (ivjmenuItemRenewal == null)
		{
			try
			{
				ivjmenuItemRenewal = new javax.swing.JMenuItem();
				ivjmenuItemRenewal.setName("menuItemRenewal");
				ivjmenuItemRenewal.setMnemonic(KeyEvent.VK_R);
				ivjmenuItemRenewal.setText(RENEW);
				ivjmenuItemRenewal.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_R,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemRenewal);
				ivjmenuItemRenewal.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.RENEW);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRenewal;
	}
	/**
	 * Return the menuItemReplacement property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemReplacement()
	{
		if (ivjmenuItemReplacement == null)
		{
			try
			{
				ivjmenuItemReplacement = new javax.swing.JMenuItem();
				ivjmenuItemReplacement.setName("menuItemReplacement");
				ivjmenuItemReplacement.setMnemonic(KeyEvent.VK_P);
				ivjmenuItemReplacement.setText(REPL);
				ivjmenuItemReplacement.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_P,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemReplacement);
				ivjmenuItemReplacement.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.REPL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReplacement;
	}
	/**
	 * Return the menuItemReports property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemReports()
	{
		if (ivjmenuItemReports == null)
		{
			try
			{
				ivjmenuItemReports = new javax.swing.JMenuItem();
				ivjmenuItemReports.setName("menuItemReports");
				ivjmenuItemReports.setMnemonic(KeyEvent.VK_R);
				ivjmenuItemReports.setText(REPORTS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemReports);
				// defect 7889
				// renamed from CP007 to REG106
				ivjmenuItemReports.setActionCommand(
					ScreenConstant.REG106
						+ DELIM
						+ TransCdConstant.IRENEW);
				// end defect 7889
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReports;
	}

	/**
	 * Return the menuItemReprintReceipt property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemReprintReceipt()
	{
		if (ivjmenuItemReprintReceipt == null)
		{
			try
			{
				ivjmenuItemReprintReceipt = new javax.swing.JMenuItem();
				ivjmenuItemReprintReceipt.setName(
					"menuItemReprintReceipt");
				ivjmenuItemReprintReceipt.setMnemonic(KeyEvent.VK_R);
				ivjmenuItemReprintReceipt.setText(REPRNT_RCPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemReprintReceipt);
				ivjmenuItemReprintReceipt.setActionCommand(
					ScreenConstant.CUS003);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReprintReceipt;
	}
	/**
	 * Return the menuItemReprintReports property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemReprintReports()
	{
		if (ivjmenuItemReprintReports == null)
		{
			try
			{
				ivjmenuItemReprintReports = new javax.swing.JMenuItem();
				ivjmenuItemReprintReports.setName(
					"menuItemReprintReports");
				ivjmenuItemReprintReports.setMnemonic(KeyEvent.VK_P);
				ivjmenuItemReprintReports.setText(REPRNT_RPTS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemReprintReports);
				ivjmenuItemReprintReports.setActionCommand(
					ScreenConstant.RPR002);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReprintReports;
	}
	/**
	 * Return the menuItemQuickCounterReport property value.
	 * 
	 * @return JMenuItem
	 * @deprecated
	 */

	private JMenuItem getmenuItemReprintStickerReport()
	{
		if (ivjmenuItemReprintStickerReport == null)
		{
			try
			{
				ivjmenuItemReprintStickerReport =
					new javax.swing.JMenuItem();
				ivjmenuItemReprintStickerReport.setName(
					"menuItemReprintStickerReport");
				ivjmenuItemReprintStickerReport.setMnemonic(
					KeyEvent.VK_C);
				ivjmenuItemReprintStickerReport.setText(
					REPRNT_STKR_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemReprintStickerReport);
				ivjmenuItemReprintStickerReport.setActionCommand(
					ScreenConstant.RPR004);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemReprintStickerReport;
	}
	/**
	 * Return the menuItemRerunCountyWide property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRerunCountyWide()
	{
		if (ivjmenuItemRerunCountyWide == null)
		{
			try
			{
				ivjmenuItemRerunCountyWide =
					new javax.swing.JMenuItem();
				ivjmenuItemRerunCountyWide.setName(
					"menuItemRerunCountyWide");
				ivjmenuItemRerunCountyWide.setMnemonic(KeyEvent.VK_W);
				ivjmenuItemRerunCountyWide.setText(RERUN_CNTYWIDE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemRerunCountyWide);
				ivjmenuItemRerunCountyWide.setActionCommand(
					ScreenConstant.FUN012);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRerunCountyWide;
	}
	/**
	 * Return the menuItemRerunSubstation property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRerunSubstation()
	{
		if (ivjmenuItemRerunSubstation == null)
		{
			try
			{
				ivjmenuItemRerunSubstation =
					new javax.swing.JMenuItem();
				ivjmenuItemRerunSubstation.setName(
					"menuItemRerunSubstation");
				ivjmenuItemRerunSubstation.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemRerunSubstation.setText(RERUN_SUBSUM);
				// user code begin {1}
				actionComponents.add(ivjmenuItemRerunSubstation);
				ivjmenuItemRerunSubstation.setActionCommand(
					ScreenConstant.FUN010);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRerunSubstation;
	}
	/**
	 * Return the menuItemResetClose property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemResetClose()
	{
		if (ivjmenuItemResetClose == null)
		{
			try
			{
				ivjmenuItemResetClose = new javax.swing.JMenuItem();
				ivjmenuItemResetClose.setName("menuItemResetClose");
				ivjmenuItemResetClose.setMnemonic(KeyEvent.VK_O);
				ivjmenuItemResetClose.setText(RESET_CLSOUT_INDI);
				// user code begin {1}
				actionComponents.add(ivjmenuItemResetClose);
				ivjmenuItemResetClose.setActionCommand(
					ScreenConstant.FUN008);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemResetClose;
	}
	/**
	 * Return the menuItemRSPSUpdates property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemRSPSUpdates()
	{
		if (ivjmenuItemRSPSUpdates == null)
		{
			try
			{
				ivjmenuItemRSPSUpdates = new javax.swing.JMenuItem();
				ivjmenuItemRSPSUpdates.setName("menuItemRSPSUpdates");
				//defect 8084
				ivjmenuItemRSPSUpdates.setMnemonic(KeyEvent.VK_U);
				//end defect 8084
				ivjmenuItemRSPSUpdates.setText(RSPS_STAT_UPD);
				// user code begin {1}
				actionComponents.add(ivjmenuItemRSPSUpdates);
				ivjmenuItemRSPSUpdates.setActionCommand(
					ScreenConstant.RSP001);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemRSPSUpdates;
	}
	/**
	 * Return the menuItemSalesTax property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSalesTax()
	{
		if (ivjmenuItemSalesTax == null)
		{
			try
			{
				ivjmenuItemSalesTax = new javax.swing.JMenuItem();
				ivjmenuItemSalesTax.setName("menuItemSalesTax");
				//ivjmenuItemSalesTax.setMnemonic(83);
				ivjmenuItemSalesTax.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemSalesTax.setText(SALESTAX_ALLOC_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSalesTax);
				ivjmenuItemSalesTax.setActionCommand(
					ScreenConstant.KEY003);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSalesTax;
	}
	/**
	 * Return the menuItemSalvage property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSalvage()
	{
		if (ivjmenuItemSalvage == null)
		{
			try
			{
				ivjmenuItemSalvage = new javax.swing.JMenuItem();
				ivjmenuItemSalvage.setName("menuItemSalvage");
				ivjmenuItemSalvage.setMnemonic(KeyEvent.VK_V);
				ivjmenuItemSalvage.setText(SALVAGE_COA);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSalvage);
				ivjmenuItemSalvage.setActionCommand(
					ScreenConstant.KEY006
						+ DELIM
						+ TransCdConstant.SCOT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSalvage;
	}
	/**
	 * Return the menuItemSearch property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSearch()
	{
		if (ivjmenuItemSearch == null)
		{
			try
			{
				ivjmenuItemSearch = new javax.swing.JMenuItem();
				ivjmenuItemSearch.setName("menuItemSearch");
				ivjmenuItemSearch.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemSearch.setText(SEARCH);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSearch);
				// defect 7889
				// Renamed from CP001 to REG101
				ivjmenuItemSearch.setActionCommand(
					ScreenConstant.REG101
						+ DELIM
						+ TransCdConstant.IRENEW);
				// end defect 7889
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSearch;
	}
	/**
	 * Return the menuItemSecurityChangeReport property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSecurityChangeReport()
	{
		if (ivjmenuItemSecurityChangeReport == null)
		{
			try
			{
				ivjmenuItemSecurityChangeReport =
					new javax.swing.JMenuItem();
				ivjmenuItemSecurityChangeReport.setName(
					"menuItemSecurityChangeReport");
				ivjmenuItemSecurityChangeReport.setMnemonic(
					KeyEvent.VK_C);
				ivjmenuItemSecurityChangeReport.setText(SECUR_CHG_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSecurityChangeReport);
				ivjmenuItemSecurityChangeReport.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.SECCHG_RPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSecurityChangeReport;
	}
	/**
	 * Return the menuItemServerPlusDisabled property value.
	 * 
	 * @return JRadioButtonMenuItem
	 */

	private JRadioButtonMenuItem getmenuItemServerPlusDisabled()
	{
		if (ivjmenuItemServerPlusDisabled == null)
		{
			try
			{
				ivjmenuItemServerPlusDisabled =
					new javax.swing.JRadioButtonMenuItem();
				ivjmenuItemServerPlusDisabled.setName(
					"menuItemServerPlusDisabled");
				ivjmenuItemServerPlusDisabled.setMnemonic(
					KeyEvent.VK_D);
				ivjmenuItemServerPlusDisabled.setText(SRVRPLUS_DISABLD);
				// user code begin {1}
				actionComponents.add(ivjmenuItemServerPlusDisabled);
				ivjmenuItemServerPlusDisabled.addActionListener(
					radioMenuActionListener);
				bgServerPlus.add(ivjmenuItemServerPlusDisabled);
				ivjmenuItemServerPlusDisabled.setActionCommand(
					ScreenConstant.SEC020
						+ DELIM
						+ TransCdConstant.SP_DIS);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemServerPlusDisabled;
	}
	/**
	 * Return the JRadioButtonMenuItem1 property value.
	 * 
	 * @return JRadioButtonMenuItem
	 */

	private JRadioButtonMenuItem getmenuItemServerPlusEnabled()
	{
		if (ivjmenuItemServerPlusEnabled == null)
		{
			try
			{
				ivjmenuItemServerPlusEnabled =
					new javax.swing.JRadioButtonMenuItem();
				ivjmenuItemServerPlusEnabled.setName(
					"menuItemServerPlusEnabled");
				ivjmenuItemServerPlusEnabled.setMnemonic(KeyEvent.VK_E);
				ivjmenuItemServerPlusEnabled.setText(SRVRPLUS_ENABLD);
				ivjmenuItemServerPlusEnabled.setEnabled(false);
				// user code begin {1}
				actionComponents.add(ivjmenuItemServerPlusEnabled);
				ivjmenuItemServerPlusEnabled.addActionListener(
					radioMenuActionListener);
				bgServerPlus.add(ivjmenuItemServerPlusEnabled);
				ivjmenuItemServerPlusEnabled.setActionCommand(
					ScreenConstant.SEC020
						+ DELIM
						+ TransCdConstant.SP_ENA);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemServerPlusEnabled;
	}
	/**
	 * Return the menuItemSetPrintDestination property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSetPrintDestination()
	{
		if (ivjmenuItemSetPrintDestination == null)
		{
			try
			{
				ivjmenuItemSetPrintDestination =
					new javax.swing.JMenuItem();
				ivjmenuItemSetPrintDestination.setName(
					"menuItemSetPrintDestination");
				ivjmenuItemSetPrintDestination.setMnemonic(
					KeyEvent.VK_S);
				ivjmenuItemSetPrintDestination.setText(SET_PRT_DEST);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSetPrintDestination);
				ivjmenuItemSetPrintDestination.setActionCommand(
					ScreenConstant.CTL009);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSetPrintDestination;
	}
	/**
	 * Return the menuItemSpecialPlatesApplication property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesApplication()
	{
		if (ivjmenuItemSpecialPlatesApplication == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesApplication =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesApplication.setName(
					"menuItemSpecialPlatesApplication");
				ivjmenuItemSpecialPlatesApplication.setMnemonic(
					KeyEvent.VK_A);
				ivjmenuItemSpecialPlatesApplication.setText(
					SPCL_PLT_APP);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSpecialPlatesApplication);
				ivjmenuItemSpecialPlatesApplication.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.SPAPPL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesApplication;
	}
	/**
	 * Return the menuItemSpecialPlatesDelete property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSpecialPlatesDelete()
	{
		if (ivjmenuItemSpecialPlatesDelete == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesDelete =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesDelete.setName(
					"menuItemSpecialPlatesDelete");
				ivjmenuItemSpecialPlatesDelete.setMnemonic(
					KeyEvent.VK_D);
				ivjmenuItemSpecialPlatesDelete.setText(SPCL_PLT_DEL);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSpecialPlatesDelete);
				ivjmenuItemSpecialPlatesDelete.setActionCommand(
					ScreenConstant.KEY002
						+ DELIM
						+ TransCdConstant.SPDEL);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesDelete;
	}
	/**
	 * Return the menuItemSpecialPlatesDuplicatePermit property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSpecialPlatesDuplicatePermit()
	{
		if (ivjmenuItemSpecialPlatesDuplicatePermit == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesDuplicatePermit =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesDuplicatePermit.setName(
					"menuItemSpecialPlatesDuplicatePermit");
				ivjmenuItemSpecialPlatesDuplicatePermit.setMnemonic(
					KeyEvent.VK_U);
				ivjmenuItemSpecialPlatesDuplicatePermit.setText(
					"Duplicate Insignia");
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSpecialPlatesDuplicatePermit);
				ivjmenuItemSpecialPlatesDuplicatePermit
					.setActionCommand(
					ScreenConstant.SPL005
						+ DELIM
						+ TransCdConstant.DPSPPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesDuplicatePermit;
	}
	// defect 10820
	/**
	 * Return the menuItemSpecialPlatesInquiry property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSpecialPlatesInquiry()
	{
		if (ivjmenuItemSpecialPlatesInquiry == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesInquiry =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesInquiry.setName(
					"menuItemSpecialPlatesInquiry");
				ivjmenuItemSpecialPlatesInquiry.setMnemonic(
					KeyEvent.VK_I);
				ivjmenuItemSpecialPlatesInquiry.setText(INQUIRY);
				
				// defect 11052 
				ivjmenuItemSpecialPlatesInquiry.setAccelerator(
						KeyStroke.getKeyStroke(
							KeyEvent.VK_7,
							Event.CTRL_MASK));
				// end defect 11052 
				
				// user code begin {1}
				actionComponents.add(ivjmenuItemSpecialPlatesInquiry);
				ivjmenuItemSpecialPlatesInquiry.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.SPINQ);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesInquiry;
	}
	// end defect 10820
	/**
	 * Return the menuItemSpecialPlatesRenewPlateOnly property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesRenewPlateOnly()
	{
		if (ivjmenuItemSpecialPlatesRenewPlateOnly == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesRenewPlateOnly =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesRenewPlateOnly.setName(
					"menuItemSpecialPlatesRenewPlateOnly");
				ivjmenuItemSpecialPlatesRenewPlateOnly.setMnemonic(
					KeyEvent.VK_N);
				ivjmenuItemSpecialPlatesRenewPlateOnly.setText(
					SPCL_PLT_RENEW);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSpecialPlatesRenewPlateOnly);
				ivjmenuItemSpecialPlatesRenewPlateOnly
					.setActionCommand(
					ScreenConstant.KEY002
						+ DELIM
						+ TransCdConstant.SPRNW);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesRenewPlateOnly;
	}

	/**
	 * Return the menuItemSpecialPlatesReports property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesReports()
	{
		if (ivjmenuItemSpecialPlatesReports == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesReports =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesReports.setName(
					"menuItemSpecialPlatesReports");
				ivjmenuItemSpecialPlatesReports.setMnemonic(
					KeyEvent.VK_R);
				ivjmenuItemSpecialPlatesReports.setText(SPCL_PLT_RPTS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSpecialPlatesReports);
				ivjmenuItemSpecialPlatesReports.setActionCommand(
					ScreenConstant.SPL003);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesReports;
	}

	/**
	 * Return the menuItemSpecialPlatesReserve property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesReserve()
	{
		if (ivjmenuItemSpecialPlatesReserve == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesReserve =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesReserve.setName(
					"menuItemSpecialPlatesReserve");
				ivjmenuItemSpecialPlatesReserve.setMnemonic(
					KeyEvent.VK_S);
				ivjmenuItemSpecialPlatesReserve.setText(
					SPCL_PLT_RESERVE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSpecialPlatesReserve);
				ivjmenuItemSpecialPlatesReserve.setActionCommand(
					ScreenConstant.KEY002
						+ DELIM
						+ TransCdConstant.SPRSRV);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesReserve;
	}

	/**
	 * Return the menuItemSpecialPlatesRevise property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesRevise()
	{
		if (ivjmenuItemSpecialPlatesRevise == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesRevise =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesRevise.setName(
					"menuItemSpecialPlatesRevise");
				ivjmenuItemSpecialPlatesRevise.setMnemonic(
					KeyEvent.VK_V);
				ivjmenuItemSpecialPlatesRevise.setText(SPCL_PLT_REVISE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSpecialPlatesRevise);
				ivjmenuItemSpecialPlatesRevise.setActionCommand(
					ScreenConstant.KEY002
						+ DELIM
						+ TransCdConstant.SPREV);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesRevise;
	}
	/**
	 * Return the menuItemSpecialPlatesUnacceptable property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSpecialPlatesUnacceptable()
	{
		if (ivjmenuItemSpecialPlatesUnacceptable == null)
		{
			try
			{
				ivjmenuItemSpecialPlatesUnacceptable =
					new javax.swing.JMenuItem();
				ivjmenuItemSpecialPlatesUnacceptable.setName(
					"menuItemSpecialPlatesUnacceptable");
				ivjmenuItemSpecialPlatesUnacceptable.setMnemonic(
					KeyEvent.VK_U);
				ivjmenuItemSpecialPlatesUnacceptable.setText(
					SPCL_PLT_UNACC);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSpecialPlatesUnacceptable);
				ivjmenuItemSpecialPlatesUnacceptable.setActionCommand(
					ScreenConstant.KEY002
						+ DELIM
						+ TransCdConstant.SPUNAC);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSpecialPlatesUnacceptable;
	}

	/**
	 * Return the menuItemStatusChange property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemStatusChange()
	{
		if (ivjmenuItemStatusChange == null)
		{
			try
			{
				ivjmenuItemStatusChange = new javax.swing.JMenuItem();
				ivjmenuItemStatusChange.setName("menuItemStatusChange");
				ivjmenuItemStatusChange.setMnemonic(KeyEvent.VK_S);
				ivjmenuItemStatusChange.setText(STAT_CHG);
				ivjmenuItemStatusChange.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_U,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemStatusChange);
				ivjmenuItemStatusChange.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.STAT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemStatusChange;
	}
	/**
	 * Return the menuItemSubcontractorRenewal property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSubcontractorRenewal()
	{
		if (ivjmenuItemSubcontractorRenewal == null)
		{
			try
			{
				ivjmenuItemSubcontractorRenewal =
					new javax.swing.JMenuItem();
				ivjmenuItemSubcontractorRenewal.setName(
					"menuItemSubcontractorRenewal");
				ivjmenuItemSubcontractorRenewal.setMnemonic(
					KeyEvent.VK_S);
				ivjmenuItemSubcontractorRenewal.setText(SUBCON_RENEW);
				ivjmenuItemSubcontractorRenewal.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_S,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemSubcontractorRenewal);
				// PCR 34
				ivjmenuItemSubcontractorRenewal.setActionCommand(
					ScreenConstant.REG050
						+ DELIM
						+ TransCdConstant.SBRNW);
				// End PCR 34	
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSubcontractorRenewal;
	}

	/**
	 * Return the ivjmenuItemSubcontractorReportIdSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSubcontractorReportIdSort()
	{
		if (ivjmenuItemSubcontractorReportIdSort == null)
		{
			try
			{
				ivjmenuItemSubcontractorReportIdSort =
					new javax.swing.JMenuItem();
				ivjmenuItemSubcontractorReportIdSort.setName(
					"ivjmenuItemSubcontractorReportIdSort");
				ivjmenuItemSubcontractorReportIdSort.setMnemonic(
					KeyEvent.VK_I);
				ivjmenuItemSubcontractorReportIdSort.setText(
					SORT_BY_ID);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSubcontractorReportIdSort);
				ivjmenuItemSubcontractorReportIdSort.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.SUBCON_RPT_ID_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSubcontractorReportIdSort;
	}

	/**
	 * Return the ivjmenuItemSubcontractorReportNameSort property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemSubcontractorReportNameSort()
	{
		if (ivjmenuItemSubcontractorReportNameSort == null)
		{
			try
			{
				ivjmenuItemSubcontractorReportNameSort =
					new javax.swing.JMenuItem();
				ivjmenuItemSubcontractorReportNameSort.setName(
					"ivjmenuItemSubcontractorReportNameSort");
				ivjmenuItemSubcontractorReportNameSort.setMnemonic(
					KeyEvent.VK_N);
				ivjmenuItemSubcontractorReportNameSort.setText(
					SORT_BY_NAME);
				// user code begin {1}
				actionComponents.add(
					ivjmenuItemSubcontractorReportNameSort);
				ivjmenuItemSubcontractorReportNameSort
					.setActionCommand(
					ScreenConstant.RPR000
						+ DELIM
						+ TransCdConstant.SUBCON_RPT_NAME_SORT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSubcontractorReportNameSort;
	}
	/**
	 * Return the menuItemSubcontractorUpdates property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSubcontractorUpdates()
	{
		if (ivjmenuItemSubcontractorUpdates == null)
		{
			try
			{
				ivjmenuItemSubcontractorUpdates =
					new javax.swing.JMenuItem();
				ivjmenuItemSubcontractorUpdates.setName(
					"menuItemSubcontractorUpdates");
				ivjmenuItemSubcontractorUpdates.setMnemonic(
					KeyEvent.VK_S);
				ivjmenuItemSubcontractorUpdates.setText(SUBCON_UPDS);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSubcontractorUpdates);
				ivjmenuItemSubcontractorUpdates.setActionCommand(
					ScreenConstant.OPT003);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSubcontractorUpdates;
	}
	/**
	 * Return the menuItemSupervisorOverride property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemSupervisorOverride()
	{
		if (ivjmenuItemSupervisorOverride == null)
		{
			try
			{
				ivjmenuItemSupervisorOverride =
					new javax.swing.JMenuItem();
				ivjmenuItemSupervisorOverride.setName(
					"menuItemSupervisorOverride");
				ivjmenuItemSupervisorOverride.setMnemonic(
					KeyEvent.VK_S);
				ivjmenuItemSupervisorOverride.setText(SUPER_OVERRIDE);
				// user code begin {1}
				actionComponents.add(ivjmenuItemSupervisorOverride);
				ivjmenuItemSupervisorOverride.setActionCommand(
					ScreenConstant.SEC017);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemSupervisorOverride;
	}
	/**
	 * Return the menuItemTempAdditionalWeight property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemTempAdditionalWeight()
	{
		if (ivjmenuItemTempAdditionalWeight == null)
		{
			try
			{
				ivjmenuItemTempAdditionalWeight =
					new javax.swing.JMenuItem();
				ivjmenuItemTempAdditionalWeight.setName(
					"menuItemTempAdditionalWeight");
				ivjmenuItemTempAdditionalWeight.setMnemonic(
					KeyEvent.VK_A);
				ivjmenuItemTempAdditionalWeight.setText(TEMP_ADDL_WT);
				ivjmenuItemTempAdditionalWeight.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_W,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemTempAdditionalWeight);
				ivjmenuItemTempAdditionalWeight.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.TAWPT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemTempAdditionalWeight;
	}

	/**
	 * Return the menuItemTitleApplication property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemTitleApplication()
	{
		if (ivjmenuItemTitleApplication == null)
		{
			try
			{
				ivjmenuItemTitleApplication =
					new javax.swing.JMenuItem();
				ivjmenuItemTitleApplication.setName(
					"menuItemTitleApplication");
				ivjmenuItemTitleApplication.setMnemonic(KeyEvent.VK_T);
				ivjmenuItemTitleApplication.setText(TTL_APPL);
				ivjmenuItemTitleApplication.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_A,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemTitleApplication);
				ivjmenuItemTitleApplication.setActionCommand(
					ScreenConstant.KEY006
						+ DELIM
						+ TransCdConstant.TITLE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemTitleApplication;
	}
	/**
	 * Return the menuItem property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemTitlePackageReport()
	{
		if (ivjmenuItemTitlePackageReport == null)
		{
			try
			{
				ivjmenuItemTitlePackageReport =
					new javax.swing.JMenuItem();
				ivjmenuItemTitlePackageReport.setName(
					"menuItemTitlePackageReport");
				ivjmenuItemTitlePackageReport.setMnemonic(
					KeyEvent.VK_T);
				ivjmenuItemTitlePackageReport.setText(TTL_PKG_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemTitlePackageReport);
				ivjmenuItemTitlePackageReport.setActionCommand(
					ScreenConstant.RPR003);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemTitlePackageReport;
	}
	/**
	 * Return the menuItemTowTruck property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemTowTruck()
	{
		if (ivjmenuItemTowTruck == null)
		{
			try
			{
				ivjmenuItemTowTruck = new javax.swing.JMenuItem();
				ivjmenuItemTowTruck.setName("menuItemTowTruck");
				ivjmenuItemTowTruck.setMnemonic(KeyEvent.VK_T);
				ivjmenuItemTowTruck.setText(TOW_TRUCK);
				ivjmenuItemTowTruck.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_K,
						Event.CTRL_MASK));
				//ivjmenuItemTowTruck.setActionCommand("Tow Truck");
				// user code begin {1}
				actionComponents.add(ivjmenuItemTowTruck);
				ivjmenuItemTowTruck.setActionCommand(
					ScreenConstant.MRG004);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemTowTruck;
	}
	/**
	 * Return the menuItemVehicleInformation property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemVehicleInformation()
	{
		if (ivjmenuItemVehicleInformation == null)
		{
			try
			{
				ivjmenuItemVehicleInformation =
					new javax.swing.JMenuItem();
				ivjmenuItemVehicleInformation.setName(
					"menuItemVehicleInformation");
				ivjmenuItemVehicleInformation.setMnemonic(
					KeyEvent.VK_V);
				ivjmenuItemVehicleInformation.setText(VEH_INFO);
				ivjmenuItemVehicleInformation.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_V,
						Event.CTRL_MASK));
				//ivjmenuItemVehicleInformation.setActionCommand(
				//	"com.txdot.isd.rts.client.common.ui.VCInquiryKeySelectionKEY001#VEHINQ");
				// user code begin {1}
				actionComponents.add(ivjmenuItemVehicleInformation);
				ivjmenuItemVehicleInformation.setActionCommand(
					ScreenConstant.KEY001
						+ DELIM
						+ TransCdConstant.VEHINQ);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemVehicleInformation;
	}
	// defect 9117
	/**
		 * Return the getmenuItemVIRejectionReport property value.
		 * 
		 * @return JMenuItem
		 */

	private JMenuItem getmenuItemVIRejectionReport()
	{
		if (ivjmenuItemVIRejectionReport == null)
		{
			try
			{
				ivjmenuItemVIRejectionReport =
					new javax.swing.JMenuItem();
				ivjmenuItemVIRejectionReport.setName(
					"menuItemVIRejectionReport");
				ivjmenuItemVIRejectionReport.setMnemonic(KeyEvent.VK_L);
				ivjmenuItemVIRejectionReport.setText(VI_REJECTION_RPT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemVIRejectionReport);
				ivjmenuItemVIRejectionReport.setActionCommand(
					ScreenConstant.INV031);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemVIRejectionReport;
	}
	/**
	 * Return the menuItemVoidTransaction property value.
	 * 
	 * @return JMenuItem
	 */

	private JMenuItem getmenuItemVoidTransaction()
	{
		if (ivjmenuItemVoidTransaction == null)
		{
			try
			{
				ivjmenuItemVoidTransaction =
					new javax.swing.JMenuItem();
				ivjmenuItemVoidTransaction.setName(
					"menuItemVoidTransaction");
				ivjmenuItemVoidTransaction.setMnemonic(KeyEvent.VK_V);
				ivjmenuItemVoidTransaction.setText(VOID_TRANS);
				ivjmenuItemVoidTransaction.setAccelerator(
					KeyStroke.getKeyStroke(
						KeyEvent.VK_O,
						Event.CTRL_MASK));
				// user code begin {1}
				actionComponents.add(ivjmenuItemVoidTransaction);
				ivjmenuItemVoidTransaction.setActionCommand(
					ScreenConstant.VOI001);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemVoidTransaction;
	}
	/**
	 * Return the ivjmenuItemWebAgent property value.
	 * 
	 * @return JMenuItem
	 */
	private JMenuItem getmenuItemWebAgent()
	{
		if (ivjmenuItemWebAgent == null)
		{
			try
			{
				ivjmenuItemWebAgent = new javax.swing.JMenuItem();
				ivjmenuItemWebAgent.setName("ivjmenuItemWebAgent");
				ivjmenuItemWebAgent.setMnemonic(KeyEvent.VK_B);
				ivjmenuItemWebAgent.setText(WEBAGENT);
				// user code begin {1}
				actionComponents.add(ivjmenuItemWebAgent);

				//begin defect 10797
				String lsServerLoc = SystemProperty.getWebAgentHost();

				if (!(UtilityMethods
					.isEmpty(SystemProperty.getWebAgentPort())))
				{
					lsServerLoc =
						SystemProperty.getWebAgentHost()
							+ ":"
							+ SystemProperty.getWebAgentPort();
				}

				ivjmenuItemWebAgent.setActionCommand(
					"Launch#remotehttp:"
						+ SystemProperty.getWebAgentProtocol()
						+ "://"
						+ lsServerLoc
						+ "/"
						
						// defect 11174
						//+ "WebAgent"
						+ SystemProperty.getWebAgentPath()
						// end 11174
						
						+ "/login.do?method=signin&transWsid="
						+ SystemProperty.getWorkStationId());
				//end defect 10797

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuItemWebAgent;
	}
	/**
	 * Return the ivjmenuLienholderReport property value.
	 * 
	 * @return JMenu
	 */
	private JMenu getmenuLienholderReport()
	{
		if (ivjmenuLienholderReport == null)
		{
			try
			{
				ivjmenuLienholderReport = new javax.swing.JMenu();

				ivjmenuLienholderReport.setName(
					"ivjmenuLienholderReport");

				ivjmenuLienholderReport.setMnemonic(KeyEvent.VK_H);

				ivjmenuLienholderReport.setText(LIENHLDR_RPT);

				// user code begin {1}
				ivjmenuLienholderReport.add(
					getmenuItemLienholderReportIdSort());

				ivjmenuLienholderReport.add(
					getmenuItemLienholderReportNameSort());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuLienholderReport;
	}
	/**
	 * Return the menuLocalOptions property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuLocalOptions()
	{
		if (ivjmenuLocalOptions == null)
		{
			try
			{
				ivjmenuLocalOptions = new javax.swing.JMenu();
				ivjmenuLocalOptions.setName("menuLocalOptions");
				ivjmenuLocalOptions.setMnemonic(KeyEvent.VK_L);
				ivjmenuLocalOptions.setText(LOCAL_OPTS);
				ivjmenuLocalOptions.setActionCommand(LOCAL_OPTS);
				// defect 10250
				ivjmenuLocalOptions.add(getmenuDealerReport());
				ivjmenuLocalOptions.add(getmenuSubcontractorReport());
				ivjmenuLocalOptions.add(getmenuLienholderReport());
				ivjmenuLocalOptions.add(
					getmenuCertifiedLienholderReport());
				// end defect 10250  
				ivjmenuLocalOptions.add(getJSeparator18());
				ivjmenuLocalOptions.add(getmenuItemDealerUpdates());
				ivjmenuLocalOptions.add(
					getmenuItemSubcontractorUpdates());
				ivjmenuLocalOptions.add(getmenuItemLienholderUpdates());
				ivjmenuLocalOptions.add(
					getmenuItemPaymentAccountUpdates());
				ivjmenuLocalOptions.add(getmenuItemCreditCard());
				ivjmenuLocalOptions.add(getmenuItemRSPSUpdates());
				// defect 10701 
				ivjmenuLocalOptions.add(new JSeparator());
				ivjmenuLocalOptions.add(getmenuItemBatchRptMgmt());
				// end defect 10701
				ivjmenuLocalOptions.add(getJSeparator20());
				ivjmenuLocalOptions.add(getmenuSecurity());
				ivjmenuLocalOptions.add(getJSeparator21());
				ivjmenuLocalOptions.add(getmenuSecurityReports());
				ivjmenuLocalOptions.add(getJSeparator22());
				ivjmenuLocalOptions.add(getmenuAdminFunctions());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuLocalOptions;
	}
	/**
	 * Return the menuLogon property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuLogon()
	{
		if (ivjmenuLogon == null)
		{
			try
			{
				ivjmenuLogon = new javax.swing.JMenu();
				ivjmenuLogon.setName("menuLogon");
				// defect 11354
				ivjmenuLogon.setMnemonic(KeyEvent.VK_E);
				// end defect 11354
				ivjmenuLogon.setText(LOGON);
				ivjmenuLogon.setActionCommand(LOGON);
				ivjmenuLogon.add(getmenuItemLogon());
				// user code begin {1}
				actionComponents.add(ivjmenuLogon);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuLogon;
	}
	/**
	 * Return the menuMisc property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuMisc()
	{
		if (ivjmenuMisc == null)
		{
			try
			{
				ivjmenuMisc = new javax.swing.JMenu();
				ivjmenuMisc.setName("menuMisc");
				ivjmenuMisc.setMnemonic(KeyEvent.VK_M);
				ivjmenuMisc.setText(MISC);
				ivjmenuMisc.setActionCommand(MISC);
				ivjmenuMisc.add(getmenuItemReprintReceipt());
				ivjmenuMisc.add(getJSeparator12());
				ivjmenuMisc.add(
					getmenuItemCompleteVehicleTransaction());
				ivjmenuMisc.add(getJSeparator13());
				ivjmenuMisc.add(getmenuItemVoidTransaction());
				ivjmenuMisc.add(getJSeparator14());
				ivjmenuMisc.add(getmenuItemSetPrintDestination());
				ivjmenuMisc.add(getJSeparator15());
				ivjmenuMisc.add(getmenuPrintImmediate());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuMisc;
	}
	/**
	 * Return the menuMiscRegistation property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuMiscRegistration()
	{
		if (ivjmenuMiscRegistration == null)
		{
			try
			{
				ivjmenuMiscRegistration = new javax.swing.JMenu();
				ivjmenuMiscRegistration.setName("menuMiscRegistration");
				ivjmenuMiscRegistration.setMnemonic(KeyEvent.VK_M);
				ivjmenuMiscRegistration.setText(MISC_REG);
				ivjmenuMiscRegistration.setActionCommand(MISC_REG);

				// defect 10491 
				ivjmenuMiscRegistration.add(getmenuTimedPermit());
				// end defect 10491

				ivjmenuMiscRegistration.add(
					getmenuItemTempAdditionalWeight());
				ivjmenuMiscRegistration.add(
					getmenuItemNonResidentAgPermit());
				ivjmenuMiscRegistration.add(getmenuItemTowTruck());
				// defect 9831 ;
				ivjmenuMiscRegistration.add(getmenuDisabledPlacard());
				// end defect 9831 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuMiscRegistration;
	}
	/**
	 * Return the menuPrintImmediate property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuPrintImmediate()
	{
		if (ivjmenuPrintImmediate == null)
		{
			try
			{
				ivjmenuPrintImmediate = new javax.swing.JMenu();
				ivjmenuPrintImmediate.setName("menuPrintImmediate");
				ivjmenuPrintImmediate.setMnemonic(KeyEvent.VK_P);
				ivjmenuPrintImmediate.setText(PRINT_IMMED);
				ivjmenuPrintImmediate.setActionCommand(PRINT_IMMED);
				ivjmenuPrintImmediate.add(getmenuItemOff());
				ivjmenuPrintImmediate.add(getmenuItemNextCustomer());
				ivjmenuPrintImmediate.add(getmenuItemOn());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuPrintImmediate;
	}
	/**
	 * Return the menuRegistrationOnly property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuRegistrationOnly()
	{
		if (ivjmenuRegistrationOnly == null)
		{
			try
			{
				ivjmenuRegistrationOnly = new javax.swing.JMenu();
				ivjmenuRegistrationOnly.setName("menuRegistrationOnly");
				ivjmenuRegistrationOnly.setMnemonic(KeyEvent.VK_R);
				ivjmenuRegistrationOnly.setText(REG_ONLY);
				ivjmenuRegistrationOnly.setActionCommand(REG_ONLY);
				ivjmenuRegistrationOnly.add(getmenuInternetRenewal());
				ivjmenuRegistrationOnly.add(getmenuItemRenewal());
				ivjmenuRegistrationOnly.add(
					getmenuItemDuplicateReceipt());
				ivjmenuRegistrationOnly.add(getmenuItemExchange());
				ivjmenuRegistrationOnly.add(getmenuItemReplacement());
				ivjmenuRegistrationOnly.add(getmenuItemModify());
				// defect 10733
				ivjmenuRegistrationOnly.add(getJSeparator27());
				ivjmenuRegistrationOnly.add(getmenuItemWebAgent());
				// end defect 10733
				ivjmenuRegistrationOnly.add(getJSeparator1());
				ivjmenuRegistrationOnly.add(
					getmenuItemSubcontractorRenewal());
				ivjmenuRegistrationOnly.add(getJSeparator2());
				ivjmenuRegistrationOnly.add(getmenuItemAddressChange());
				ivjmenuRegistrationOnly.add(getJSeparator3());
				ivjmenuRegistrationOnly.add(getmenuItemDriverEd());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuRegistrationOnly;
	}
	/**
	 * Return the menuReports property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuReports()
	{
		if (ivjmenuReports == null)
		{
			try
			{
				ivjmenuReports = new javax.swing.JMenu();
				ivjmenuReports.setName("menuReports");
				ivjmenuReports.setMnemonic(KeyEvent.VK_R);
				ivjmenuReports.setText(REPORTS);
				ivjmenuReports.setActionCommand(REPORTS);
				ivjmenuReports.add(getmenuItemSalesTax());
				// Reorder items in list  
				ivjmenuReports.add(new JSeparator());
				ivjmenuReports.add(getmenuItemReprintReports());
				ivjmenuReports.add(getmenuItemReprintStickerReport());
				ivjmenuReports.add(getmenuItemExemptAuditReport());
				ivjmenuReports.add(new JSeparator());
				ivjmenuReports.add(getmenuItemTitlePackageReport());
				ivjmenuReports.add(getmenuItemElectronicTitleReport());
				// defect 10900
				ivjmenuReports.add(new JSeparator());
				ivjmenuReports.add(getmenuItemSuspectedFraudReport());
				// end defect 10900
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuReports;
	}
	/**
	 * Return the menuSecurity property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuSecurity()
	{
		if (ivjmenuSecurity == null)
		{
			try
			{
				ivjmenuSecurity = new javax.swing.JMenu();
				ivjmenuSecurity.setName("menuSecurity");
				ivjmenuSecurity.setMnemonic(KeyEvent.VK_C);
				ivjmenuSecurity.setText(SECURITY);
				ivjmenuSecurity.setActionCommand(SECURITY);
				ivjmenuSecurity.add(getmenuItemEmployeeSecurity());
				ivjmenuSecurity.add(getmenuItemSupervisorOverride());
				ivjmenuSecurity.add(getJSeparator19());
				ivjmenuSecurity.add(getmenuItemSecurityChangeReport());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuSecurity;
	}
	/**
	 * Return the menuSecurityReports property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuSecurityReports()
	{
		if (ivjmenuSecurityReports == null)
		{
			try
			{
				ivjmenuSecurityReports = new javax.swing.JMenu();
				ivjmenuSecurityReports.setName("menuSecurityReports");
				ivjmenuSecurityReports.setMnemonic(KeyEvent.VK_R);
				ivjmenuSecurityReports.setText(SECUR_RPTS);
				ivjmenuSecurityReports.setActionCommand(SECUR_RPTS);
				ivjmenuSecurityReports.add(
					getmenuItemEmployeeSecurityReport());
				ivjmenuSecurityReports.add(
					getmenuItemEventSecurityReport());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuSecurityReports;
	}
	/**
	 * Return the menuMiscRegistation property value.
	 * 
	 * @return JMenu
	 */

	private JMenu getmenuSpecialPlates()
	{
		if (ivjmenuSpecialPlates == null)
		{
			try
			{
				ivjmenuSpecialPlates = new javax.swing.JMenu();
				ivjmenuSpecialPlates.setName("menuSpecialPlates");
				ivjmenuSpecialPlates.setMnemonic(KeyEvent.VK_S);
				ivjmenuSpecialPlates.setText("Special Plates");
				ivjmenuSpecialPlates.setActionCommand("Special Plates");
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesApplication());
				// defect 10461 
				ivjmenuSpecialPlates.add(getmenuItemMyPlates());
				// end defect 10461  
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesRenewPlateOnly());
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesRevise());
				// defect 10700 
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesDuplicatePermit());
				// end defect 10700 
				// defect 10820
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesInquiry());
				// end defect 10820 
				ivjmenuSpecialPlates.add(getJSeparatorSpecialPlate1());
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesReserve());
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesUnacceptable());
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesDelete());
				ivjmenuSpecialPlates.add(getJSeparatorSpecialPlate2());
				ivjmenuSpecialPlates.add(
					getmenuItemSpecialPlatesReports());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuSpecialPlates;
	}
	/**
	 * Return the ivjmenuSubcontractorReport property value.
	 * 
	 * @return JMenu
	 */
	private JMenu getmenuSubcontractorReport()
	{
		if (ivjmenuSubcontractorReport == null)
		{
			try
			{
				ivjmenuSubcontractorReport = new javax.swing.JMenu();

				ivjmenuSubcontractorReport.setName(
					"ivjmenuSubcontractorReport");

				ivjmenuSubcontractorReport.setMnemonic(KeyEvent.VK_B);

				ivjmenuSubcontractorReport.setText(SUBCON_RPT);

				// user code begin {1}
				ivjmenuSubcontractorReport.add(
					getmenuItemSubcontractorReportIdSort());

				ivjmenuSubcontractorReport.add(
					getmenuItemSubcontractorReportNameSort());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuSubcontractorReport;
	}

	/**
	 * Return the menuTitleRegistration property value.
	 * @return JMenu
	 */
	private JMenu getmenuTitleRegistration()
	{
		if (ivjmenuTitleRegistration == null)
		{
			try
			{
				ivjmenuTitleRegistration = new javax.swing.JMenu();
				ivjmenuTitleRegistration.setName(
					"menuTitleRegistration");
				ivjmenuTitleRegistration.setMnemonic(KeyEvent.VK_T);
				ivjmenuTitleRegistration.setText(TTL_REG);
				ivjmenuTitleRegistration.setActionCommand(TTL_REG);
				ivjmenuTitleRegistration.add(
					getmenuItemTitleApplication());
				ivjmenuTitleRegistration.add(
					getmenuItemCorrectTitleRejection());
				ivjmenuTitleRegistration.add(getmenuItemDealerTitles());
				ivjmenuTitleRegistration.add(getJSeparator7());
				ivjmenuTitleRegistration.add(getmenuItemCCOCCDO());
				// defect 9642 
				// No longer present COA 
				//ivjmenuTitleRegistration.add(getmenuItemCOA());
				// end defect defect 9642 
				ivjmenuTitleRegistration.add(getmenuItemSalvage());
				ivjmenuTitleRegistration.add(getJSeparator8());
				ivjmenuTitleRegistration.add(getmenuItemDeleteTitle());
				ivjmenuTitleRegistration.add(getmenuItemStatusChange());
				ivjmenuTitleRegistration.add(getJSeparator9());
				ivjmenuTitleRegistration.add(
					getmenuItemAdditionalSalesTax());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjmenuTitleRegistration;
	}

	/**
	 * Return the menuTimedPermit property value.
	 * @return JMenu
	 */
	private JMenu getmenuTimedPermit()
	{
		if (ivjMenuTimedPermit == null)
		{
			try
			{
				ivjMenuTimedPermit = new javax.swing.JMenu();
				ivjMenuTimedPermit.setName("ivjMenuTimedPermit");
				ivjMenuTimedPermit.setMnemonic(KeyEvent.VK_T);
				ivjMenuTimedPermit.setText(TIMED_PRMT);
				ivjMenuTimedPermit.setActionCommand(TIMED_PRMT);
				ivjMenuTimedPermit.add(getmenuItemPermitInquiry());
				ivjMenuTimedPermit.add(getmenuItemPermitApplication());
				// defect 10844 
				ivjMenuTimedPermit.add(getmenuItemPermitModify());
				// end defect 10844 

				ivjMenuTimedPermit.add(
					getmenuItemPermitDuplicateReceipt());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjMenuTimedPermit;
	}

	/**
	 * Return the PendingTransPanel1 property value.
	 * 
	 * @return PendingTransPanel
	 */

	private PendingTransPanel getpendingPanel()
	{
		if (ivjpendingPanel == null)
		{
			try
			{
				ivjpendingPanel =
					new com
						.txdot
						.isd
						.rts
						.client
						.desktop
						.PendingTransPanel();
				ivjpendingPanel.setName("pendingPanel");
				// user code begin {1}
				ivjpendingPanel.setDesktop(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpendingPanel;
	}
	/**
	 * Return the PendingTransPanel.
	 * 
	 * @return JPanel
	 */
	public PendingTransPanel getPendingTransPanel()
	{
		return getpendingPanel();
	}
	/**
	 * Return the RTSDeskTopJMenuBar property value.
	 * 
	 * @return JMenuBar
	 */

	private JMenuBar getRTSDeskTopJMenuBar()
	{
		if (ivjRTSDeskTopJMenuBar == null)
		{
			try
			{
				ivjRTSDeskTopJMenuBar = new javax.swing.JMenuBar();
				ivjRTSDeskTopJMenuBar.setName("RTSDeskTopJMenuBar");
				ivjRTSDeskTopJMenuBar.setSelectionModel(
					new javax.swing.DefaultSingleSelectionModel());
				ivjRTSDeskTopJMenuBar.add(getmenuCustomer());
				ivjRTSDeskTopJMenuBar.add(getmenuMisc());
				ivjRTSDeskTopJMenuBar.add(getmenuReports());
				ivjRTSDeskTopJMenuBar.add(getmenuLocalOptions());
				ivjRTSDeskTopJMenuBar.add(getmenuAccounting());
				ivjRTSDeskTopJMenuBar.add(getmenuInventory());
				ivjRTSDeskTopJMenuBar.add(getmenuFunds());
				ivjRTSDeskTopJMenuBar.add(getmenuLogon());
				// defect 8900
				// Task 42 - Add help menu
				ivjRTSDeskTopJMenuBar.add(getmenuHelp());
				// end defect 8900
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDeskTopJMenuBar;
	}
	/**
	 * Return the Security Data object.
	 * 
	 * @return SecurityData
	 */
	public SecurityData getSecurityData()
	{
		return caSecurityData;
	}
	/**
	 * Return the stcLblDataServer property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblDataServer()
	{
		if (ivjstcLblDataServer == null)
		{
			try
			{
				ivjstcLblDataServer = new javax.swing.JLabel();
				ivjstcLblDataServer.setName("stcLblDataServer");
				ivjstcLblDataServer.setText(DATA_SRVR);
				ivjstcLblDataServer.setMaximumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDataServer.setMinimumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDataServer.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDataServer;
	}
	/**
	 * Return the stcLblPrintImmediate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPrintImmediate()
	{
		if (ivjstcLblPrintImmediate == null)
		{
			try
			{
				ivjstcLblPrintImmediate = new javax.swing.JLabel();
				ivjstcLblPrintImmediate.setName("stcLblPrintImmediate");
				ivjstcLblPrintImmediate.setText(PRINT_IMMED);
				ivjstcLblPrintImmediate.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPrintImmediate;
	}
	/**
	 * Return the stcLblRecordRetrieval property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblRecordRetrieval()
	{
		if (ivjstcLblRecordRetrieval == null)
		{
			try
			{
				ivjstcLblRecordRetrieval = new javax.swing.JLabel();
				ivjstcLblRecordRetrieval.setName(
					"stcLblRecordRetrieval");
				ivjstcLblRecordRetrieval.setText(REC_RETRIEV);
				ivjstcLblRecordRetrieval.setMaximumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblRecordRetrieval.setMinimumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblRecordRetrieval.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRecordRetrieval;
	}
	/**
	 * Return the stcLblVersion property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblVersion()
	{
		if (ivjstcLblVersion == null)
		{
			try
			{
				ivjstcLblVersion = new javax.swing.JLabel();
				ivjstcLblVersion.setName("stcLblVersion");
				ivjstcLblVersion.setPreferredSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblVersion.setText(RTS_VER);
				ivjstcLblVersion.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblVersion.setMinimumSize(
					new java.awt.Dimension(62, 14));
				// user code begin {1}
				// defect 7885
				ivjstcLblVersion.setText(
					ivjstcLblVersion.getText()
						+ CommonConstant.STR_SPACE_ONE
						+ SystemProperty.getVersionNo());
				//		+ Version.getVersion());
				// end defect 7885
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVersion;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		// Log uncaught exceptions.
		// defect 7885
		System.out.println(ScreenConstant.MSG_UNCAUGHT_EXECPTION);
		aeThrowable.printStackTrace(System.out);
		Log.write(
			Log.SQL_EXCP,
			this,
			ScreenConstant.MSG_FRAME_EXCEPTION + aeThrowable);
		// end defect 7885
	}
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// defect 8519
			// Added for a temp fix to the menu problem with Java 1.4
			UIManager.getDefaults().put(
				WorkaroundMenuItemUI.MENUITEM_UI,
				WorkaroundMenuItemUI.class.getName());
			// end defect 8519
			actionComponents = new java.util.Vector();
			radioMenuActionListener = new RadioMenuActionListener(this);
			bgPrintImmediate = new ButtonGroup();
			bgServerPlus = new ButtonGroup();
			// user code end
			setName(RTS_DESKTOP);
			// defect 11325
			setDefaultCloseOperation(
				javax.swing.WindowConstants.EXIT_ON_CLOSE);
			// end defect 11325
			setJMenuBar(getRTSDeskTopJMenuBar());
			// defect 10155
			// Change to DMV logo on DMV Start Date
			//setIconImage(
			//	Toolkit.getDefaultToolkit().getImage(
			//		getClass().getResource(IMAGE_LOGO)));
			if (RTSDate.getCurrentDate().getYYYYMMDDDate()
				>= SystemProperty.getDMVStartDate().getYYYYMMDDDate())
			{
				setIconImage(
					Toolkit.getDefaultToolkit().getImage(
						getClass().getResource(IMAGE_DMV_LOGO)));
			}
			else
			{
				setIconImage(
					Toolkit.getDefaultToolkit().getImage(
						getClass().getResource(IMAGE_LOGO)));
			}
			// end defect 10155
			// defect 11336
			setBounds(
				new java.awt.Rectangle(
					3,
					3,
					ScreenConstant.DESKTOP_DEV_WIDTH,
					ScreenConstant.DESKTOP_DEV_HEIGHT));
			// end defect 11336
			setSize(
				ScreenConstant.DESKTOP_DEV_WIDTH,
				ScreenConstant.DESKTOP_DEV_HEIGHT);
			setTitle(ScreenConstant.RTS_TITLE_STRING);
			setContentPane(getJFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// defect 11336
		setLocation(
				((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.width
				/ 2
				- getSize().width / 2)),
			((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.height
				/ 2
				- getSize().height / 2)));
		
		// defect 6445
		// allow developers to work with smaller desktop
		// production will fit the whole screen.
		//if (SystemProperty.getProdStatus() == 0)
		//{
			// defect 10984
			// defect 7768
			// added -1 so that the windows taksbar can be used to popup
			// a balloon when the user gets a new meail message.
			//setSize(
				//Toolkit.getDefaultToolkit().getScreenSize().width,
				//Toolkit.getDefaultToolkit().getScreenSize().height - 25);
			// end defect 7768
			// end defect 10984
		//}
		// end defect 6445
		// end defect 11336
		setResizable(false);
		// defect 11325
		setDefaultCloseOperation(
			javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// end defect 11325
		// defect 8337 	
		// Set Record Retrieval and Data Server Lights On Independently
		// setServerStatus(true);
		// setMFStatus(true);	
		setRcdRetrievalIconStatus(true);
		setDataServerIconStatus(true);
		// end defect 8337  
		getpendingPanel().setVisible(false);
		if (!int2bool(SystemProperty.getPrintImmediateIndi()))
		{
			getmenuItemOff().setSelected(true);
			setPrintImmediate(PRINT_IMMEDIATE_OFF);
		}
		else
		{
			getmenuItemOn().setSelected(true);
			setPrintImmediate(PRINT_IMMEDIATE_ON);
		}
		setComputerInformation();

		// defect 8270
		// tell the RTSException class who the desktop is in
		// case we need to tie an error message to it.
		RTSException.setRTSDesktop(this);
		// end defect 8270
		// user code end
	}
	/**
	 * Convert an int to a boolean.
	 * <br>0 is false.
	 * <br>Non-zero is true.
	 * 
	 * @param aiIndi int
	 * @return boolean
	 */
	private boolean int2bool(int aiIndi)
	{
		return (aiIndi != 0);
	}

	/**
	 * Return if menu item should be enabled in More Trans
	 * 
	 * @param asTransCd String 
	 * @return boolean 
	 */
	private boolean isEnabledInMoreTrans(String asTransCd)
	{
		try
		{
			TransactionCodesData laTransData =
				TransactionCodesCache.getTransCd(asTransCd);
			return int2bool(laTransData.getCumulativeTransCd());
		}
		catch (RTSException aeRTSEx)
		{
			return false;
		}
	}

	/**
	 * Determine if Main Office 
	 * 
	 * @return boolean 
	 */
	private boolean isMainOffice()
	{
		return ciSubstaId == MAIN_OFFICE_SUBSTAID;
	}

	/**
	 * Remove action listener components.
	 *  
	 * @param aaAL ActionListener
	 */
	public void removeActionListener(ActionListener aaAL)
	{
		int liSize = actionComponents.size();
		for (int i = 0; i < liSize; i++)
		{
			AbstractButton laAB =
				(AbstractButton) actionComponents.get(i);
			laAB.removeActionListener(aaAL);
		}
	}

	/**
	 * Repaints the JMenuBar - hack since it doesn't repaint itself 
	 * cleanly in the current JVM
	 */
	public void repaint()
	{
		// TODO review to see if we can remove this "hack"
		super.repaint();
		getmenuAccounting().setPopupMenuVisible(false);
		getmenuAdminFunctions().setPopupMenuVisible(false);
		getmenuCashDrawerOperations().setPopupMenuVisible(false);
		getmenuCustomer().setPopupMenuVisible(false);
		getmenuFunds().setPopupMenuVisible(false);
		getmenuFundsManagement().setPopupMenuVisible(false);
		getmenuInquiry().setPopupMenuVisible(false);
		getmenuInternetRenewal().setPopupMenuVisible(false);
		getmenuInventory().setPopupMenuVisible(false);
		getmenuLogon().setPopupMenuVisible(false);
		getmenuLocalOptions().setPopupMenuVisible(false);
		getmenuMisc().setPopupMenuVisible(false);
		getmenuMiscRegistration().setPopupMenuVisible(false);
		getmenuSpecialPlates().setPopupMenuVisible(false);
		getmenuPrintImmediate().setPopupMenuVisible(false);
		getmenuRegistrationOnly().setPopupMenuVisible(false);
		getmenuReports().setPopupMenuVisible(false);
		getmenuSecurity().setPopupMenuVisible(false);
		getmenuSecurityReports().setPopupMenuVisible(false);
		getmenuTitleRegistration().setPopupMenuVisible(false);
		// defect 8900
		// Task 42 - Add menu help
		getmenuHelp().setPopupMenuVisible(false);
		// end defect 8900

		// defect 10255
		Log.write(
			Log.SQL_EXCP,
			this,
			"Desktop Menu Repaint Hack called.");
		// end defect 10255
	}

	/**
	 * Repaints the menubar when a transaction is in the 
	 * PendingTransPanel
	 */
	private void repaintForTransaction()
	{
		// Note: Need to disable all events where menu is disabled
		// but the menu item has CTLR key

		// Disable the menu items for more trans
		getmenuMisc().setEnabled(false);
		getmenuLocalOptions().setEnabled(false);
		getmenuInventory().setEnabled(false);
		getmenuFunds().setEnabled(false);

		// defect 10900 
		// defect 8337 
		// getmenuItemTitlePackageReport().setEnabled(false);
		// getmenuItemReprintStickerReport().setEnabled(false);
		// end defect 8337
		getmenuReports().setEnabled(false);
		// end defect 10900

		enableCustomerMenuOnPendingTrans();
		enableAccountingMenuOnPendingTrans();
		// Disable those with CTL hot keys as still honored 
		// when higher menu disabled.

		// defect 9060
		// CTL+ O  (under Misc)
		getmenuItemVoidTransaction().setEnabled(false);
		// end defect 9060
		// CTL+ Z  (under Funds)
		getmenuItemCloseOutDay().setEnabled(false);
		// CTL+ 1  (under Funds)
		getmenuItemDetailReports().setEnabled(false);
		// defect 9060
		// CTL+ O  (under Misc)
		getmenuItemVoidTransaction().setEnabled(false);
		// end defect 9060		
	}

	/**
	 * Repaints the menubar when someone logs in
	 * 
	 * @param aaSecData SecurityData
	 * @param abDbStatus boolean
	 */
	public void repaintMenuItems(
		SecurityData aaSecData,
		boolean abDBStatus)
	{
		// make sure menu is disabled when aaSecData is null
		if (aaSecData == null || aaSecData.getUserName() == null)
		{
			disableMenusBeforeLogon();
		}
		else
		{
			enableCustomerMenu(aaSecData, abDBStatus);
			enableMiscMenu(aaSecData, abDBStatus);
			enableReportsMenu(aaSecData, abDBStatus);
			enableLocalOptionsMenu(aaSecData, abDBStatus);
			enableAccountingMenu(aaSecData);
			enableInventoryMenu(aaSecData, abDBStatus);
			enableFundsMenu(aaSecData, abDBStatus);
			enableLogonMenu();

			// defect 8900
			// Task 42 - Add menu help
			getmenuHelp().setEnabled(true);
			// end defect 8900
		}

		// defect 10255
		Log.write(
			Log.SQL_EXCP,
			this,
			"Desktop Menu has been repainted.");
		// end defect 10255
	}
	/**
	 * This was added so that the Application Controller could have a
	 * access to requesting focus on the menu bar.  This was a fix that
	 * was done to allow the hot keys to keep working.  Something was 
	 * causing focus to get lost when the pending panel is added then
	 * removed.  This method was created rather than making 
	 * getRTSDeskTopJMenuBar() public.
	 */
	public void requestFocusMenuBar()
	{
		getRTSDeskTopJMenuBar().requestFocus();
	}
	/**
	 * Resets the title of the desktop and the Print Immediate status
	 */
	public void reset()
	{
		setTitle(ScreenConstant.RTS_TITLE_STRING);
		if (getpendingPanel().isVisible())
		{
			getpendingPanel().getEnterButton().requestFocus();
		}
		if (SystemProperty.getPrintImmediateIndi()
			== PRINT_IMMEDIATE_NEXT
			&& !getpendingPanel().isVisible())
		{
			SystemProperty.setPrintImmediateIndi(PRINT_IMMEDIATE_OFF);
			setPrintImmediate(PRINT_IMMEDIATE_OFF);
			try
			{
				SystemProperty.updatePrintImmediateProperty("N");
			}
			catch (RTSException aeRTSEx)
			{
				// empty catch block
			}
		}
		// defect CQU100004892
		// change to make sure print immediate is always painted ok.
		setPrintImmediate(SystemProperty.getPrintImmediateIndi());
		// end defect 4892
	}
	/**
	 * Hack around the Ctrl+A issues with PendingTransPanel
	 */
	public void selectTitleApp()
	{
		getmenuItemTitleApplication().doClick();
	}

	/**
	 * Writes the Computer information on the desktop
	 */
	private void setComputerInformation()
	{
		String lsCompInfo = CommonConstant.STR_SPACE_EMPTY;

		// Workstation Information from System AbstractProperty
		ciOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
		ciSubstaId = SystemProperty.getSubStationId();
		ciWsId = SystemProperty.getWorkStationId();

		// OfficeIdsData from Cache  
		OfficeIdsData laOfficeData =
			OfficeIdsCache.getOfcId(
				SystemProperty.getOfficeIssuanceNo());

		if (SystemProperty.isCounty())
		{
			lsCompInfo =
				lsCompInfo
					+ laOfficeData.getOfcName()
					+ COUNTY
					+ COMMA_SPACE;
		}
		else
		{
			lsCompInfo =
				lsCompInfo + laOfficeData.getOfcName() + COMMA_SPACE;
		}

		// SubstationData from Cache  
		SubstationData laSubData =
			SubstationCache.getSubsta(ciOfcIssuanceNo, ciSubstaId);

		if (laSubData != null)
		{
			lsCompInfo =
				lsCompInfo + laSubData.getSubstaName() + COMMA_SPACE;
		}

		// AssignedWorkstationIdsData from Cache 
		try
		{
			AssignedWorkstationIdsData laWSData =
				AssignedWorkstationIdsCache.getAsgndWsId(
					ciOfcIssuanceNo,
					ciSubstaId,
					SystemProperty.getWorkStationId());

			// defect 8628 
			cbServer = SystemProperty.isClientServer();
			// end defect 8628  
			if (ciWsId != laWSData.getCashWsId())
			{
				cbSharedCashDrawer = true;
			}

			// ServerPlus can only change at startup
			cbTreatAsServer =
				cbServer || RTSApplicationController.isServerPlus();

			// defect 8821 
			// Implement "CODE" vs "COMM", "BATCH" vs. "DATA"
			// Assign Desktop Verbiage 
			if (SystemProperty.isCommServer())
			{
				if (cbServer)
				{
					lsCompInfo += CODE_BATCHSVR;
				}
				else
				{
					lsCompInfo += CODE_SERVER;
				}
			}
			else if (cbServer)
			{
				lsCompInfo += BATCH_SERVER;
			}
			// end defect 8821 
			else
			{
				lsCompInfo += WORKSTATION;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.APPLICATION, this, ERRMSG_ASSGND_WSID);
		}
		try
		{
			// defect 8821 
			// Use "," vs. " " between Workstation Type and Host Name
			lsCompInfo += COMMA_SPACE
				+ java.net.InetAddress.getLocalHost().getHostName();
			// end defect 8821 
		}
		catch (java.net.UnknownHostException aeUHEx)
		{
			Log.write(Log.APPLICATION, this, ERRMSG_HOST_NAME);
		}
		getlblCountyString().setText(lsCompInfo);
	}

	/**
	 * Sets the data on the desktop when either someone logs in, 
	 * or the PendingTransPanel is to be shown
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		// After someone logs on
		if (aaData instanceof SecurityData)
		{
			SecurityData laSecData = (SecurityData) aaData;
			SystemProperty.setCurrentEmpId(laSecData.getEmpId());
			caSecurityData = laSecData;
			// defect 6614   
			// Use isDBReady
			setDataServerIconStatus(
				RTSApplicationController.isDBReady());
			// end defect 6614

			// defect 4696
			// repaintForTransaction if Pending Trans is visible
			if (getpendingPanel().isVisible())
			{
				repaintForTransaction();
			}
			else
			{
				// defect 6614  
				// Use isDBReady()
				repaintMenuItems(
					laSecData,
					RTSApplicationController.isDBReady());
				// end defect 6614
			}
			// end defect 4696
		}
		// defect 10407
		else
		{
			boolean lbDTA = false;

			if (aaData instanceof Vector)
			{
				Vector lvData = (Vector) aaData;

				if (!lvData.isEmpty()
					&& lvData.elementAt(0) instanceof DealerTitleData)
				{
					lbDTA = true;
				}
			}

			// After a transaction is completed
			if (aaData instanceof CompleteTransactionData
				|| aaData instanceof CompleteVehicleTransactionData
				//|| aaData instanceof java.util.Vector)
				|| lbDTA)
			{
				// end defect 10407 
				repaintForTransaction();
				setImagePanelVisible(false);
				getpendingPanel().setData(aaData);
			}
		}
	}

	/**
	 * Repaints the DB lights
	 * 
	 * @param abNewStatus boolean
	 */
	public void setDataServerIconStatus(boolean abNewStatus)
	{
		if (abNewStatus)
		{
			getlblDataServer().setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(IMAGE_ON)));
		}
		else
		{
			getlblDataServer().setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(IMAGE_OFF)));
		}
	}
	/**
	 * Sets the TXDOT image visibility
	 * 
	 * @param abVisible boolean
	 */
	public void setImagePanelVisible(boolean abVisible)
	{
		getimagePanel().setVisible(abVisible);
		getpendingPanel().setVisible(!abVisible);
	}
	/**
	 * Sets the Print Immediate status
	 * 
	 * @param abStatus boolean
	 */
	public void setPrintImmediate(int abStatus)
	{
		if (abStatus == PRINT_IMMEDIATE_ON)
		{
			getmenuItemOff().setEnabled(true);
			getmenuItemOn().setEnabled(false);
			getmenuItemNextCustomer().setEnabled(false);
			getmenuItemNextCustomer().setSelected(false);
			getlblPrintImmediate().setText(
				RadioMenuActionListener.PRINT_IMMEDIATE_ON);
			getstcLblPrintImmediate().setText(PRINT_IMMED);
		}
		else if (abStatus == PRINT_IMMEDIATE_OFF)
		{
			getmenuItemOff().setEnabled(false);
			getmenuItemOn().setEnabled(true);
			getmenuItemNextCustomer().setEnabled(true);
			getmenuItemNextCustomer().setSelected(false);
			getlblPrintImmediate().setText("");
			getstcLblPrintImmediate().setText("");
		}
		else if (abStatus == PRINT_IMMEDIATE_NEXT)
		{
			getmenuItemOff().setEnabled(false);
			getmenuItemOff().setSelected(true);
			getmenuItemOn().setEnabled(true);
			getmenuItemNextCustomer().setEnabled(true);
			getlblPrintImmediate().setText(NEXT);
			getstcLblPrintImmediate().setText(PRINT_IMMED);
		}
	}
	/**
	 * Repaints the MF lights
	 * 
	 * @param abNewStatus boolean
	 */
	public void setRcdRetrievalIconStatus(boolean abNewStatus)
	{
		if (abNewStatus)
		{
			getlblRecordRetrieval().setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(IMAGE_ON)));
		}
		else
		{
			getlblRecordRetrieval().setIcon(
				new javax.swing.ImageIcon(
					getClass().getResource(IMAGE_OFF)));
		}
	}

	/**
	 * Sets the Server Plus status
	 * 
	 * @param abEnabled boolean
	 */
	public void setServerPlusEnabled(boolean abEnabled)
	{
		// defect 8550 
		// Incorporated cbServer as part of parameter
		getmenuItemServerPlusEnabled().setEnabled(
			!abEnabled && cbServer);
		getmenuItemServerPlusDisabled().setEnabled(
			abEnabled && cbServer);
		// end defect 8550

		getmenuItemServerPlusEnabled().setSelected(abEnabled);
		getmenuItemServerPlusDisabled().setSelected(!abEnabled);
	}

}
