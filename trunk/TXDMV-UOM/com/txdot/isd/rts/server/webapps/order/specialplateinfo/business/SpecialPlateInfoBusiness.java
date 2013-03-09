package com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.specialplateinfo
	.business;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetSpecialPlateQuery;
import com.txdot.isd.rts.server.db.InternetSpecialPlatesGroupingQuery;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.common
	.business
	.AbstractBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.specialplateinfo
	.data
	.SpecialPlateDesign;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.specialplateinfo
	.data
	.SpecialPlatesInfoRequest;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.order
	.specialplateinfo
	.data
	.SpecialPlatesInfoResponse;
import com.txdot.isd.rts.services.exception.RTSException;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.ServiceConstants;

/*
 * SpecialPlateInfoBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/06/2007	Created Class.
 * 							defect 9121 Ver Special Plates
 * Bob B.		08/15/2007	Add new method so code change there will not
 * 							affect production calls to this class.
 * 							Add getPltInfoForDev()
 * 							defect 9119 Ver Special Plates
 * Bob B.		08/23/2007	Removed PLPCPF from the hard-coded list of
 * 							appl fees.
 * 							Also changed DESIGN_WHOLE and 
 * 							DESIGN_WHOLE_TL to setPltLeftSpace(5) so the
 * 							typing of characters placed on these whole
 * 							plate images for PLP's fits 8 characters.
 * 							modify loadApplFeeExceptions(), 
 * 							loadPltDesignData()
 * 							defect 9119 Ver Special Plates
 * Bob B.		10/01/2007	Make plate designs fit for personalizing
 * 							remaining plates where the personalized 
 * 							characters typing area on the plate was
 * 							not good before this change.
 * 							add DESIGN_WHOLE_B, DESIGN_THIRD_B
 * 							modify loadPltDesignData()
 * 							defect 9119 Ver Special Plates
 * Bob B.		10/15/2007  Changed DESIGN_THIRD_MC parameters so when
 * 							personalized, the characters typed and 
 * 							placed in the plate	image fit nicely in 
 * 							the blank area of the 1/3 MC plate image.
 * 							modify loadPltDesignData()
 * 							defect 9119 Ver Special Plates 
 * Bob B.		12/03/2007  Added fee exceptions for PLPDLR and 
 * 							PLPDLRMC plates.
 *							modify loadApplFeeExceptions(),
 *							loadRegFeeExceptions()
 * 							defect 9461 Ver Special Plates 
 * Bob B.		01/23/2008  Add functionality for getting SP images
 * 							add getSPImages(), getSPImagenames(),
 * 							modify processData()
 * 							defect 9473 Ver Tres Amigos Prep
 * Bob B.		01/10/2011	Add call to query to pull together corssover
 * 							group and vendor URL.
 * 							modify getPltInfo()
 * 							defect 10693 Ver POS_670
 * Bob B.		02/06/2012	Make a change to the plate design for the 
 * 							1/3 Design Exp design plates, so the 
 * 							personalized characters start appearing on 
 * 							the plate more to the left, so the 5th and 
 * 							last character is not so close to the right 
 * 							edge to cause the image wrapping.
 * 							modify laPltDesign.setPltLeftSpace() for
 * 							DESIGN_THIRD_EXPIRATION
 * 							defect 11276 Ver POS 6_10_0
 * ---------------------------------------------------------------------
 */

/**
 * This is the business class that handles retrieving special plate 
 * info used to display on the web.  This can be anything from entire
 * groups to a single set of plates.
 *
 * @version	POS_6_10_0			02/06/2012
 * @author	Jeff Seifert
 * <br>Creation Date:			03/06/2007 13:40:00
 */
public class SpecialPlateInfoBusiness extends AbstractBusiness
{
	public static final int GRP_PLP = 40;
	public static final int GRP_SOUVENIR = 90;
	// This must be above the Static call to load the data.
	private static Hashtable chtPltDesign = new Hashtable();
	private static Hashtable chtRegFeeExceptions = new Hashtable();
	private static Hashtable chtApplFeeExceptions = new Hashtable();
	static {
		loadPltDesignData();
		loadApplFeeExceptions();
		loadRegFeeExceptions();
	}
	private static final String DESIGN_BLANK = "BLANK";
	private static final String DESIGN_HALF = "1/2 DESIGN";
	private static final String DESIGN_NO_PLATE = "NO PLATE";
	private static final String DESIGN_QUARTER = "1/4 DESIGN";
	private static final String DESIGN_QUARTER_SS = "1/4 DESIGN SS";
	private static final String DESIGN_THIRD = "1/3 DESIGN";
	private static final String DESIGN_THIRD_EXPIRATION =
		"1/3 DESIGN EXP";
	private static final String DESIGN_THIRD_MC = "1/3 DESIGN MC";
	private static final String DESIGN_TWO_THIRDS = "2/3 DESIGN";
	private static final String DESIGN_WHOLE = "WHOLE";
	//Whole with an expiration
	private static final String DESIGN_WHOLE_EXP = "WHOLE EXP";
	private static final String DESIGN_WHOLE_MC = "WHOLE MC";
	//Whole with a top legend
	private static final String DESIGN_WHOLE_TL = "WHOLE TL";
	
	// defect 9119
	private static final String DESIGN_WHOLE_B = "WHOLE B";
	private static final String DESIGN_THIRD_B = "1/3 DESIGN B";
	// end defect 9119

	/**
	 * Loads the application exceptions.
	 */
	private static void loadApplFeeExceptions()
	{
		// Key = RegPltCd + F or RegPltCd + A
		chtApplFeeExceptions.put("FPF", "75.00");
		chtApplFeeExceptions.put("COTTONF", "8.00");
		chtApplFeeExceptions.put("COTTONA", "8.00");
		chtApplFeeExceptions.put("PPF", "5.30");
		// defect 9119
		//chtApplFeeExceptions.put("PLPCPF", "30.00");
		// end defect 9119
		// defect 9461
		// these fee adjustments include the $45 dealer fee
		// plus the .30 reflectorization fee
		chtApplFeeExceptions.put("PLPDLRF", "45.30");
		chtApplFeeExceptions.put("PLPDLRMCF", "45.30");
		// end defect 9461
	}

	/**
	 * Loads the registration exceptions.
	 */
	private static void loadRegFeeExceptions()
	{
		// Key = RegPltCd + F or RegPltCd + A
		chtRegFeeExceptions.put("MILITARPF", "50.30");
		chtRegFeeExceptions.put("APF", "50.30");
		chtRegFeeExceptions.put("ANTMCF", "50.30");
		chtRegFeeExceptions.put("FPF", "75.00");
		chtRegFeeExceptions.put("PPF", "5.30");
		chtRegFeeExceptions.put("PLPF", "40.00");
		chtRegFeeExceptions.put("PLPMCF", "40.00");
		chtRegFeeExceptions.put("PLPTTRLRF", "40.00");
		chtRegFeeExceptions.put("PLPTVTRLF", "40.00");
		chtRegFeeExceptions.put("PLPTRLRF", "40.00");
		chtRegFeeExceptions.put("PLPCPF", "30.00");
		// defect 9461
		// these fee adjustments include the $45 dealer fee
		// plus the .30 reflectorization fee 		
		chtRegFeeExceptions.put("PLPDLRF", "45.30");
		chtRegFeeExceptions.put("PLPDLRMCF", "45.30");
		// end defect 9461
	}

	/**
	 * Loads the design type table.
	 */
	private static void loadPltDesignData()
	{
		SpecialPlateDesign laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_BLANK);
		laPltDesign.setPltHeight(172);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(16);
		laPltDesign.setPltTopSpace(62);
		laPltDesign.setPltCharHeight(67);
		laPltDesign.setPltCharWidth(42);
		laPltDesign.setPltFillX(9);
		laPltDesign.setPltFillY(53);
		laPltDesign.setPltFillWidth(262);
		laPltDesign.setPltFillHeight(87);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_THIRD);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(114);
		laPltDesign.setPltTopSpace(40);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		// defect 9119 
		// laPltDesign.setPltFillX(109);
		laPltDesign.setPltFillX(108);
		// end defect 9119
		laPltDesign.setPltFillY(41);
		laPltDesign.setPltFillWidth(180);
		laPltDesign.setPltFillHeight(75);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);
		
		// defect 9119
		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_THIRD_B);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(114);
		laPltDesign.setPltTopSpace(40);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(111);
		laPltDesign.setPltFillY(41);
		laPltDesign.setPltFillWidth(180);
		laPltDesign.setPltFillHeight(75);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);
		// end defect 9119

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_THIRD_EXPIRATION);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		// defect 11276
		//laPltDesign.setPltLeftSpace(118);
		laPltDesign.setPltLeftSpace(100);
		// end defect 11276
		laPltDesign.setPltTopSpace(53);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(95);
		// defect 9119
		// laPltDesign.setPltFillY(45);
		laPltDesign.setPltFillY(48);
		// end defect 9119
		laPltDesign.setPltFillWidth(180);
		// defect 9119
		// laPltDesign.setPltFillHeight(73);
		laPltDesign.setPltFillHeight(71);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_TWO_THIRDS);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(200);
		laPltDesign.setPltTopSpace(50);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(200);
		laPltDesign.setPltFillY(50);
		laPltDesign.setPltFillWidth(90);
		laPltDesign.setPltFillHeight(75);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_QUARTER);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(82);
		laPltDesign.setPltTopSpace(48);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(78);
		// defect 9119
		// laPltDesign.setPltFillY(40);
		laPltDesign.setPltFillY(47);
		// end defect 9119
		laPltDesign.setPltFillWidth(208);
		// defect 9119
		// laPltDesign.setPltFillHeight(77);
		laPltDesign.setPltFillHeight(70);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_QUARTER_SS);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(82);
		laPltDesign.setPltTopSpace(48);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(74);
		laPltDesign.setPltFillY(41);
		laPltDesign.setPltFillWidth(217);
		// defect 9119
		// laPltDesign.setPltFillHeight(76);
		laPltDesign.setPltFillHeight(74);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_HALF);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(139);
		laPltDesign.setPltTopSpace(61);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(122);
		laPltDesign.setPltFillY(41);
		laPltDesign.setPltFillWidth(170);
		laPltDesign.setPltFillHeight(89);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_NO_PLATE);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_THIRD_MC);
		laPltDesign.setPltHeight(172);
		laPltDesign.setPltWidth(300);
		// defect 9119
		// laPltDesign.setPltLeftSpace(121);
		laPltDesign.setPltLeftSpace(107);
		// laPltDesign.setPltTopSpace(57);
		laPltDesign.setPltTopSpace(67);
		// end defect 9119
		laPltDesign.setPltCharHeight(67);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(102);
		// defect 9119
		// laPltDesign.setPltFillY(54);
		laPltDesign.setPltFillY(62);
		// end defect 9119
		laPltDesign.setPltFillWidth(162);
		// defect 9119
		// laPltDesign.setPltFillHeight(88);
		laPltDesign.setPltFillHeight(74);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_WHOLE);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		// defect 9119
		// set the PltLeftSpace to 5 so 8 characters can fit on 
		// the whole plate when choosing plp characters
		//laPltDesign.setPltLeftSpace(15);
		laPltDesign.setPltLeftSpace(5);
		//laPltDesign.setPltTopSpace(39);
		laPltDesign.setPltTopSpace(57);
		// laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharHeight(60);
		// end defect 9119	
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(14);
		// defect 9119
		// laPltDesign.setPltFillY(40);
		laPltDesign.setPltFillY(56);
		// end defect 9119
		laPltDesign.setPltFillWidth(274);
		// defect 9119
		laPltDesign.setPltFillHeight(65);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);
		
		// defect 9119
		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_WHOLE_B);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(5);
		laPltDesign.setPltTopSpace(50);
		laPltDesign.setPltCharHeight(60);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(14);
		laPltDesign.setPltFillY(44);
		laPltDesign.setPltFillWidth(274);
		laPltDesign.setPltFillHeight(67);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);
		// end defectg 9119

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_WHOLE_EXP);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(15);
		laPltDesign.setPltTopSpace(39);
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(14);
		laPltDesign.setPltFillY(46);
		laPltDesign.setPltFillWidth(274);
		laPltDesign.setPltFillHeight(77);
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_WHOLE_TL);
		laPltDesign.setPltHeight(147);
		laPltDesign.setPltWidth(300);
		// defect 9119
		// set the PltLeftSpace to 5 so 8 characters can fit on 
		// the whole plate when choosing plp characters
		//laPltDesign.setPltLeftSpace(15);
		laPltDesign.setPltLeftSpace(5);
		// laPltDesign.setPltTopSpace(39);
		laPltDesign.setPltTopSpace(57);
		// end defect 9119
		laPltDesign.setPltCharHeight(62);
		laPltDesign.setPltCharWidth(30);
		laPltDesign.setPltFillX(14);
		// defect 9119
		// laPltDesign.setPltFillY(59);
		laPltDesign.setPltFillY(57);
		// end defect 9119
		laPltDesign.setPltFillWidth(274);
		// defect 9119
		// laPltDesign.setPltFillHeight(68);
		laPltDesign.setPltFillHeight(69);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);

		laPltDesign = new SpecialPlateDesign();
		laPltDesign.setPltDesign(DESIGN_WHOLE_MC);
		laPltDesign.setPltHeight(172);
		laPltDesign.setPltWidth(300);
		laPltDesign.setPltLeftSpace(16);
		laPltDesign.setPltTopSpace(62);
		laPltDesign.setPltCharHeight(67);
		laPltDesign.setPltCharWidth(42);
		// defect 9119
		// laPltDesign.setPltFillX(15);
		laPltDesign.setPltFillX(7);
		// laPltDesign.setPltFillY(53);
		laPltDesign.setPltFillY(59);
		// laPltDesign.setPltFillWidth(251);
		laPltDesign.setPltFillWidth(254);
		// laPltDesign.setPltFillHeight(85);
		laPltDesign.setPltFillHeight(78);
		// end defect 9119
		chtPltDesign.put(laPltDesign.getPltDesign(), laPltDesign);
	}

	/**
	 * Gets group info from the DB
	 * 
	 * @param aaInfoReq SpecialPlatesInfoRequest
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] getGroupInfo(SpecialPlatesInfoRequest aaInfoReq)
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			Vector lvResult = new Vector();
			laDBAccess.beginTransaction();
			InternetSpecialPlatesGroupingQuery laIntSPPlGrpQry =
				new InternetSpecialPlatesGroupingQuery(laDBAccess);
			lvResult =
				laIntSPPlGrpQry.qrySPGrpInfo(
					Integer.toString(aaInfoReq.getGrpId()));
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			// If there are results then setup the array else
			// return null
			if (lvResult.size() != 0)
			{
				larrSPInfoResp =
					new SpecialPlatesInfoResponse[lvResult.size()];

				for (int i = 0; i < lvResult.size(); i++)
				{
					larrSPInfoResp[i] =
						(SpecialPlatesInfoResponse) lvResult.elementAt(
							i);
				}
			}
		}
		catch (Exception aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
		}

		return larrSPInfoResp;
	}

	/**
	 * Return the SpecialPlatesInfoResponse array with just
	 * the needed information to build the menu.  Return both the
	 * spanish and english version.
	 * 
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] getMenuSPInfoArray()
		throws RTSException
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			laDBAccess.beginTransaction();
			Vector lvResult = new Vector();
			InternetSpecialPlatesGroupingQuery laIntSPPlQry =
				new InternetSpecialPlatesGroupingQuery(laDBAccess);
			lvResult = laIntSPPlQry.qrySPAppGrpPlt();
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			larrSPInfoResp =
				new SpecialPlatesInfoResponse[lvResult.size()];

			for (int i = 0; i < lvResult.size(); i++)
			{
				larrSPInfoResp[i] =
					(SpecialPlatesInfoResponse) lvResult.elementAt(i);
			}
		}

		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
		}

		return larrSPInfoResp;
	}
	
	/**
	 * Return the SpecialPlatesInfoResponse array with just
	 * the needed information for the SP images
	 * 
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] 
		getSPImages(SpecialPlatesInfoRequest aaInfoReq)
		throws RTSException
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			laDBAccess.beginTransaction();
			Vector lvResult = new Vector();
			InternetSpecialPlateQuery laIntSPPlQry =
				new InternetSpecialPlateQuery(laDBAccess);
			lvResult = laIntSPPlQry.qrySPImages(aaInfoReq.getPltImage());
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			larrSPInfoResp =
				new SpecialPlatesInfoResponse[lvResult.size()];

			for (int i = 0; i < lvResult.size(); i++)
			{
				larrSPInfoResp[i] =
					(SpecialPlatesInfoResponse) lvResult.elementAt(i);
				byte[] lbarrImage =
					(byte[]) larrSPInfoResp[i].getPlateImageObject();
				System.out.println(
					" query Size of "
						+ larrSPInfoResp[i].getPltImage()
						+ " = "
						+ lbarrImage.length);		
			}
		}

		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
		}

		return larrSPInfoResp;
	}
	
	/**
	 * Return the SpecialPlatesInfoResponse array with just
	 * the needed information for the SP images
	 * 
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] getSPImageNames()
		throws RTSException
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			laDBAccess.beginTransaction();
			Vector lvResult = new Vector();
			InternetSpecialPlateQuery laIntSPPlQry =
				new InternetSpecialPlateQuery(laDBAccess);
			lvResult = laIntSPPlQry.qrySPImageNames();
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			larrSPInfoResp =
				new SpecialPlatesInfoResponse[lvResult.size()];

			for (int i = 0; i < lvResult.size(); i++)
			{
				larrSPInfoResp[i] =
					(SpecialPlatesInfoResponse) lvResult.elementAt(i);
			}
					
		}
					

		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
		}

		return larrSPInfoResp;
	}

	/**
	 * Gets the Plate Design Object for the given plate design type.
	 * This determines the size of the image and the left and top 
	 * starting positions
	 * 
	 * @param asDesignType String
	 * @return SpecialPlateDesign
	 */
	public SpecialPlateDesign getPlateDesignData(String asDesignType)
	{
		// TODO remove before prod.
		loadPltDesignData();

		if (asDesignType == null || asDesignType.length() == 0)
		{
			asDesignType = DESIGN_BLANK;
		}
		return (SpecialPlateDesign) chtPltDesign.get(asDesignType);
	}

	/**
	 * Retrieves the plate and group information for the given plate
	 * id that was passed in the request.
	 * 
	 * @param aaInfoReq aaInfoReq
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] getPltInfo(SpecialPlatesInfoRequest aaInfoReq)
		throws RTSException
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		DatabaseAccess laDBAccess = new DatabaseAccess();
		int liErrorNum = 0;

		try
		{
			laDBAccess.beginTransaction();
			Vector lvResult = new Vector();
			InternetSpecialPlateQuery laIntSPPlQry =
				new InternetSpecialPlateQuery(laDBAccess);

			if (laIntSPPlQry
				.doesRegPltCdExist(
					aaInfoReq.getGrpId(),
					aaInfoReq.getPltId()))
			{
				lvResult =
					laIntSPPlQry.qryCompletePltInfo(
						aaInfoReq.getGrpId(),
						aaInfoReq.getPltId(),
						aaInfoReq.getPltDesign());
						
				SpecialPlatesInfoResponse laSPInfoResp =
					(SpecialPlatesInfoResponse) lvResult.elementAt(0);
				// defect 10693			
				if (laSPInfoResp.getCrossoverIndi()==1)
				{
					InternetSpecialPlatesGroupingQuery laSPGroup =
						new InternetSpecialPlatesGroupingQuery(laDBAccess);
					Vector lvSPGrpInfoResp = new Vector();	
					lvSPGrpInfoResp = laSPGroup.qrySPGrpInfo("120");
					SpecialPlatesInfoResponse laSPGrpInfoResp =
						(SpecialPlatesInfoResponse) lvSPGrpInfoResp.elementAt(0);
					laSPGrpInfoResp.setGrpDesc(laSPGrpInfoResp.getGrpDesc().replaceFirst("#URL#",laSPInfoResp.getVendorPlateURL()));	
					laSPGrpInfoResp.setVendorPlateURL(laSPInfoResp.getVendorPlateURL());
					lvResult.clear();
					lvResult.add(laSPGrpInfoResp);
				}
				// end defect 10693		
			}
			else
			{
				lvResult =
					laIntSPPlQry.qrySPAppGrpPlt(
						aaInfoReq.getGrpId(),
						aaInfoReq.getPltId());
			}

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			// If there are results then setup the array else
			// return null
			if (lvResult.size() != 0)
			{
				larrSPInfoResp =
					new SpecialPlatesInfoResponse[lvResult.size()];
				for (int i = 0; i < lvResult.size(); i++)
				{
					larrSPInfoResp[i] =
						(SpecialPlatesInfoResponse) lvResult.elementAt(
							i);
					liErrorNum++;
				}
			}
		}
		catch (Exception aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			aeRTSEx.printStackTrace();
		}

		return larrSPInfoResp;
	}

	/**
	 * Retrieves the plate and group information for the given plate
	 * id that was passed in the request.
	 * 
	 * @param aaInfoReq aaInfoReq
	 * @return SpecialPlatesInfoResponse[]
	 */
	private SpecialPlatesInfoResponse[] getPltInfoForDev(SpecialPlatesInfoRequest aaInfoReq)
		throws RTSException
	{
		SpecialPlatesInfoResponse[] larrSPInfoResp = null;
		SpecialPlatesInfoResponse[] larrSPInfoRespPLPAbleOnline = null;		
		DatabaseAccess laDBAccess = new DatabaseAccess();
		int liPLPAbleOnline = 0;
		int liX = -1;

		try
		{
			laDBAccess.beginTransaction();
			Vector lvResult = new Vector();
			InternetSpecialPlateQuery laIntSPPlQry =
				new InternetSpecialPlateQuery(laDBAccess);
				
			lvResult =
				laIntSPPlQry.qryCompletePltInfo(
					aaInfoReq.getGrpId(),
					aaInfoReq.getPltId(),
					aaInfoReq.getPltDesign());	

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			// If there are results then setup the array else
			// return null
			if (lvResult.size() != 0)
			{
				larrSPInfoResp =
					new SpecialPlatesInfoResponse[lvResult.size()];
				for (int i = 0; i < lvResult.size(); i++)
				{
					larrSPInfoResp[i] =
						(SpecialPlatesInfoResponse) lvResult.elementAt(
							i);
					if (larrSPInfoResp[i].isUserPltNoAllowedIndi() &&
						larrSPInfoResp[i].isOrderOnline() &&
						larrSPInfoResp[i].getGrpId() != 90)
					{
						liPLPAbleOnline++;	
					}		
				}
				
				larrSPInfoRespPLPAbleOnline =
									new SpecialPlatesInfoResponse[liPLPAbleOnline];
				for (int i = 0; i < lvResult.size(); i++)
				{
					if (larrSPInfoResp[i].isUserPltNoAllowedIndi() &&
						larrSPInfoResp[i].isOrderOnline()&&
						larrSPInfoResp[i].getGrpId() != 90)
					{
						liX++;
						larrSPInfoRespPLPAbleOnline[liX] = larrSPInfoResp[i];	
					}
				}
			}
		}
		catch (Exception aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}
			// If Exception create a response size of 1 and return
			// the error.
			larrSPInfoResp = new SpecialPlatesInfoResponse[1];
			larrSPInfoResp[0] = new SpecialPlatesInfoResponse();
			setError(larrSPInfoResp[0], aeRTSEx);
			aeRTSEx.printStackTrace();
		}

		return larrSPInfoRespPLPAbleOnline;
	}

	/**
	 * Handles the hard coded Application fee exceptions.
	 * 
	 * @param asRegPltCdPlusIndi String
	 * @param asCurrentApplFee String
	 * @return String
	 */
	public String handleApplFeeExceptions(
		String asRegPltCdPlusIndi,
		String asCurrentApplFee)
	{
		// Key = RegPltCd + Indicatior
		// (F for First Set and A for Additonal Set)
		String lsValue =
			(String) chtApplFeeExceptions.get(asRegPltCdPlusIndi);
		if (lsValue == null || lsValue.length() == 0)
		{
			return asCurrentApplFee;
		}
		else
		{
			return lsValue;
		}
	}

	/**
	 * Handles the hard coded Registration fee exceptions.
	 * 
	 * @param asRegPltCdPlusIndi String
	 * @param asCurrentRegFee String
	 * @return String
	 */
	public String handleRegFeeExceptions(
		String asRegPltCdPlusIndi,
		String asCurrentRegFee)
	{
		// Key = RegPltCd + Indicatior
		// (F for First Set and A for Additonal Set)
		String lsValue =
			(String) chtRegFeeExceptions.get(asRegPltCdPlusIndi);
		if (lsValue == null || lsValue.length() == 0)
		{
			return asCurrentRegFee;
		}
		else
		{
			return lsValue;
		}
	}

	/**
	 * Business interface for Special Plate Info.
	 * 
	 * @param aaObject Object
	 */
	public Object processData(Object aaObject)
	{
		Object laObject = null;
		try
		{
			SpecialPlatesInfoRequest laInfoReq =
				(SpecialPlatesInfoRequest) aaObject;

			switch (laInfoReq.getAction())
			{
				case ServiceConstants.SPI_ACTION_GET_PLATE_INFO :
					laObject = getPltInfo(laInfoReq);
					break;
				case ServiceConstants
					.SPI_ACTION_GET_PLATE_INFO_FOR_DEV :
					laObject = getPltInfoForDev(laInfoReq);
					break;
				case ServiceConstants.SPI_ACTION_GET_GROUP_INFO :
					laObject = getGroupInfo(laInfoReq);
				case ServiceConstants.SPI_ACTION_GET_GROUPS :
					break;
				case ServiceConstants.SPI_ACTION_GET_MENU_ITEMS :
					laObject = getMenuSPInfoArray();
				// defect 9473
					break;	
				case ServiceConstants.SPI_ACTION_GET_PLATE_IMAGES :
					laObject = getSPImages(laInfoReq);
					break;
				case ServiceConstants.SPI_ACTION_GET_PLATE_IMAGE_NAMES :
					laObject = getSPImageNames();
					break;	
				// end defect 9473	
				default :
					//TODO Log
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return laObject;
	}
}