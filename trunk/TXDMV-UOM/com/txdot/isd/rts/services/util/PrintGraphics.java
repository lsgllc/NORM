package com.txdot.isd.rts.services.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.test.common.VehicleInquiry.TestGetRecordByVIN;

/*
 * PrintGraphics.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * B Woodson    11/01/2011  new class
 *                          defect 11052 - VTR-275 Form Project @ POS
 * K Harrell	11/20/2011  added logic for Special Plate 
 * 							modify createVehicleDataJPG() 
 * 							defect 11052 Ver 6.9.0
 * B Woodson	01/27/2012	modify CERTIFIED_275_FORM, NONCERTIFIED_275_FORM
 * 							defect 11250 Ver 6.10.0 
 * K Harrell	02/01/2012	Force VTR275 to tray 2 
 * B Woodson                add TRAY2 
 *  						modify printForm275JPG() 
 *  						defect 11273 Ver 6.9.0   
 * B Woodson    06/25/12    modify CERTIFIED_275_FORM
 *                          defect 11379 Ver 7.0.0                         
 * ---------------------------------------------------------------------
 */

/** 
 * Utility for creating and printing form 275 jpg's 
 * 
 * @version	7.0.0 		06/25/12 
 * @author	Buck Woodson 
 * @Creation Date:		11/01/2011  
 */
public class PrintGraphics 
{
	
	//defect 11379
	//Win7 driver prints too dark so we modified to point to a jpg with a lighter state seal.
	//public static final String CERTIFIED_275_FORM = "images/FM275RC_merge_TXwtrmk4.JPG";
	public static final String CERTIFIED_275_FORM = "images/FM275RC_merge_TXwtrmk.JPG";
	public static final String NONCERTIFIED_275_FORM = "images/FM275R.JPG";
	//end defect 11379
	
	// defect 11273
	private static final String TRAY2 = "Tray 2";
	// end defect 11273 

	/**
	 * @param laImg
	 * @param asOutputFilePath
	 * @param aaVehicleData
	 * @return boolean 
	 */
	private boolean createVehicleDataJPG(BufferedImage aaImg, String asOutputFilePath, Object aaVehicleData)
	{
		boolean lbFilecreated = false;
		
		Graphics laGraphics = null;
		if (aaImg != null)
		{
			laGraphics = aaImg.getGraphics();
		}
		
		if (laGraphics != null && aaVehicleData != null) 
		{
			
			//set font and color
		    Font laFont = new Font("Courier New", Font.PLAIN, 22);
		    laGraphics.setFont(laFont);
		    ((Graphics2D)laGraphics).setPaint(Color.black);
		    
		    VehicleDataGraphicsHelper laHelper = null;
			if (aaVehicleData instanceof CompleteTransactionData)
			{
				laHelper = new VehicleDataGraphicsHelper(((CompleteTransactionData)aaVehicleData));
			}
			else if (aaVehicleData instanceof VehicleInquiryData)
			{
				laHelper = new VehicleDataGraphicsHelper(((VehicleInquiryData)aaVehicleData).getMfVehicleData());
			}
			else
			{
				return false;
			}
			
			boolean lbDataDrawnSuccessfully = false; 
			MFVehicleData laMfVehData = laHelper.getMFVehicleData(); 
			
			if (laMfVehData != null && laMfVehData.isSPRecordOnlyVehInq())
			{
				lbDataDrawnSuccessfully = laHelper.drawSpecialPlateOnly(laGraphics);		
			}
			else 
			{
				lbDataDrawnSuccessfully = laHelper.drawVehicleInquiry(laGraphics);			
			}
			
			laGraphics.dispose();
			
			if (!lbDataDrawnSuccessfully)
			{
				return false;
			}
			
			//save the graphics to file
			File laOutputfile = new File(asOutputFilePath);
		    try
			{
				ImageIO.write(aaImg, "JPEG", laOutputfile);
				lbFilecreated = true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return lbFilecreated;
	}

	/**
	 * @param asFormPath
	 * @return BufferedImage
	 */
	public BufferedImage getImage(String asFormPath)
	{
		BufferedImage laImg = null;
		
		//form image
	    InputStream laFileServer =
	    	PrintGraphics
	    		.class
	    		.getClassLoader()
	    		.getResourceAsStream(asFormPath);
				
		if (laFileServer == null)
		{
			return null;
		}
		
	    try
		{
			laImg = ImageIO.read(laFileServer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return laImg;
	}	

	/**Need to add -Xmx500M as VM argument in debug to successfully run this main method.
	 * @param args
	 */
	public static void main(String[] args) //throws Exception
	{
		try
		{
			RTSApplicationController.setDBUp(true);
			com.txdot.isd.rts.services.communication.Comm.setIsServer(false);
			com.txdot.isd.rts.services.util.SystemProperty.initialize();
			CacheManager.loadCache();
		}
		catch (RTSException e)
		{
			e.printStackTrace();
		}
			   
		PrintGraphics laPrintGraphics = new PrintGraphics();
		//String lsFormPath = "D://backup//11052 - VTR-275 Form Project @ POS//images//certified_275_merged_25_opacity.JPG";
		String lsFormPath = CERTIFIED_275_FORM;
		String lsOutputFilePath = "D://backup//11052 - VTR-275 Form Project @ POS//images//merged_modified.JPG";
		
		//server must be running
		//2 lines of remarks 1XP6DB9X4KD601612
//		//3 lines of remarks 2GTEC19V511189083
		//3 liens 1FMDU32X6SZB54594
		//2 colors  2G1WW12E959171407
		//1 color 2D4RN4DE2AR236388
		//David's 2GTEC19V11189083, JTHLA1KS7B0051382
		VehicleInquiryData laVehInqData = (new TestGetRecordByVIN()).getVehicleInquiryDataByVIN("2GTEC19V511189083");
		if (laVehInqData == null)
		{
			System.out.println("No VehicleInquiryData, aborting...");
			return;
		}
				
		BufferedImage laImg = laPrintGraphics.getImage(lsFormPath);
		if (laImg == null)
		{
			System.out.println("No BufferedImage, check path, aborting...");
			return;
		}


		if (laPrintGraphics.createVehicleDataJPG(laImg, lsOutputFilePath, laVehInqData)) 
		{
			boolean lbPrinted = laPrintGraphics.printForm275JPG(lsOutputFilePath);
			System.out.println(lbPrinted);
		}
	}

	/**
	 * @param lsOutputFilePath
	 * @return boolean 
	 */
	public boolean printForm275JPG(String lsOutputFilePath)
	{
		boolean lbPrintSuccess = false;
		
		//print the saved graphics with data
		PrintRequestAttributeSet laPrintAttributes = new HashPrintRequestAttributeSet();
		HashDocAttributeSet laDocAttributes = new HashDocAttributeSet();
		
		laPrintAttributes.add(new Copies(1));

		laPrintAttributes.add(MediaSizeName.NA_LETTER);
		laDocAttributes.add(MediaSizeName.NA_LETTER);
		
		MediaSize laMediaSize =
		    MediaSize.getMediaSizeForName(MediaSizeName.NA_LETTER);
		float[] farrSize = laMediaSize.getSize(MediaSize.INCH);
		
		laDocAttributes.add(new MediaPrintableArea(0, 0, farrSize[0], farrSize[1],
		    MediaPrintableArea.INCH));		    

		//other ways to get the PrintService, some buggy, see http://www.exampledepot.com/egs/javax.print/DiscoverAll.html
		//PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

		PrintService laPrintService = PrintServiceLookup.lookupDefaultPrintService();
		
		// defect 11273 
		// Add logic to force to Tray 2 
		Media [] larrMedia = (Media [])laPrintService.getSupportedAttributeValues(Media.class, null, null); 

		for (int liCount = 0; liCount < larrMedia.length; ++liCount) 
		{
			if (SystemProperty.isDevStatus())
			{
				System.out.println (larrMedia [liCount].getValue() + ": |"+ larrMedia [liCount].toString() + "|");
			}
			if (larrMedia [liCount] instanceof MediaTray)
			{
				String lsTray = larrMedia [liCount].toString().trim(); 
				if (lsTray.equals(TRAY2))
				{
					// NOTE: It appears that adding to Print Attributes is not 
					// required in the XP / Java 1.4.2 environment.   
					laPrintAttributes.add(larrMedia [liCount]);
					
					laDocAttributes.add(larrMedia [liCount]);
					break; 
				}
			}
		}
		// end defect 11273 
		
		DocPrintJob laPrintJob = laPrintService.createPrintJob();

		FileInputStream pfsInquiryImage = null;
		try
		{
			pfsInquiryImage = new FileInputStream(lsOutputFilePath);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return lbPrintSuccess;
		}
		Doc laDoc = new SimpleDoc(pfsInquiryImage, DocFlavor.INPUT_STREAM.JPEG, laDocAttributes);

		try
		{
			laPrintJob.print(laDoc, laPrintAttributes);
			lbPrintSuccess = true;
		}
		catch (PrintException e)
		{
			e.printStackTrace();
		}
		
		//cleanup
		try
		{
			pfsInquiryImage.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			pfsInquiryImage = null;
		}
		return lbPrintSuccess;
	}

	/**
	 * @param aaCompTransData
	 * @param asReceiptPath
	 * @param asReceiptFilename (optional)
	 * @return boolean 
	 */
	public boolean createForm275(CompleteTransactionData aaCompTransData, 
			String asReceiptPath, String asReceiptFilename)
	{
		if (aaCompTransData == null ||
				asReceiptPath == null)
		{
			return false;
		}
		String lsTransCd = aaCompTransData.getTransCode();
		
		String lsOutputFilePath = null;
		if (asReceiptFilename != null)
		{
			lsOutputFilePath = asReceiptPath + asReceiptFilename;
		}
		else
		{
			lsOutputFilePath = asReceiptPath + aaCompTransData.getTransId() + ".JPG";
		}
		
		String lsFormPath = null;
		
		if (lsTransCd.equals(TransCdConstant.VDSC) || lsTransCd.equals(TransCdConstant.VDSCN))
		{
			lsFormPath = CERTIFIED_275_FORM;
		} 
		else if (lsTransCd.equals(TransCdConstant.VDS) || lsTransCd.equals(TransCdConstant.VDSN))
		{
			lsFormPath = NONCERTIFIED_275_FORM;
		}
		else
		{
			return false;
		}
		
		BufferedImage laImg = getImage(lsFormPath);
		if (laImg == null)
		{
			System.out.println("No BufferedImage, check path, aborting...");
			return false;
		}
		
		if (createVehicleDataJPG(laImg, lsOutputFilePath, aaCompTransData)) 
		{
//			boolean lbPrinted = printVehicleDataJPG(lsOutputFilePath);
//			System.out.println(lbPrinted);
//			return lbPrinted;
			return true;
		}
		
		return false;
	}

}
