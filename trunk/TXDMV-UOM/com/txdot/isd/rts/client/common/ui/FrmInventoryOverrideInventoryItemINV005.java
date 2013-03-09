package com.txdot.isd.rts.client.common.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BarCodeScanner;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 *
 * FrmInventoryOverrideInventoryItemINV005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Abernethy 	09/21/2001  Added comments
 * MAbs			04/16/2002	Inventory Override bug CQU100003483
 * MAbs			04/18/2002	Global change for startWorking() and 
 *							doneWorking() 
 * MAbs			05/02/2002	Inventory not allowed to have spaces 
 *							CQU100003755
 * MAbs			05/21/2002	Do duplicates allowed when cancelling off of
 *							INV029 CQU100004031
 * Ray Rowehl	07/27/2002	Defect CQU100004471.
 *							If override item is the same as before, do 
 *							not request it.
 * B Arredondo	12/18/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes in
 *							actionPerformed().
 * Min Wang		03/13/2002	Modified actionPerformed(). 
 *							Defect CQU100005560.
 * Ray Rowehl	03/14/2003	Defect CQU100005506
 * Ray Rowehl	05/13/2003 	make qty 1 before sending it off.
 *							modify actionPerformed()
 *							defect 6120
 * Min Wang     09/19/2003  Fixed missing inventory when Override the 
 *							prompted inventory with dummy inventory item
 *                          Modified actionPerformed().
 *                          Defect 6562. Version 5.1.5.
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * T Pederson	03/16/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/17/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getbuttonPanel
 * 							defect 8240 Ver 5.2.3
 * T Pederson	10/06/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * R Pilon		06/13/2012  Change to only log the exception when attempting to 
 * 							initialize the BarCodeScanner.
 * 							modify setData(Object)
 * 							defect 11071 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * Allows the user to override the Inventory Item Number.
 *
 * @version POS_700			06/13/2012
 * @author	Michael Abernethy
 * <br>Creation Date: 		07/13/2001 09:50:41
 */

public class FrmInventoryOverrideInventoryItemINV005
	extends RTSDialogBox
	implements BarCodeListener, ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JLabel ivjlblItem = null;
	private JLabel ivjlblNumber = null;
	private JLabel ivjstcLblEnterOverride = null;
	private JLabel ivjlblYr = null;
	private RTSInputField ivjtxtNumber = null;
	
	// Object
	private BarCodeScanner caBarcode; 
	private CompleteTransactionData caCompleteTransactionData;
	private ProcessInventoryData caProcessInventoryData;
	
	// String 
	private String csTrackingType;

	// Constants 
	private final static String PLT_TRCK_TYPE = "P";
	private final static String STKR_TRCK_TYPE = "S";
	private static final int MAX_PLATE_NO = 7;

	// Text Constants 
	private final static String FRM_NAME_INV005 = 
		"FrmInventoryOverrideInventoryItemINV005";
	private final static String FRM_TITLE_INV005 = 
		"Inventory - Override Inventory Item   INV005";
	private final static String TXT_ENTER_OVRID_INV = 
		"Enter override inventory item number to replace:";
	private final static String TXT_YOUVE_SCND = "YOU'VE SCANNED A ";
	
	/**
	 * FrmInventoryOverrideInventoryItemINV005 constructor
	 */
	public FrmInventoryOverrideInventoryItemINV005()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmInventoryOverrideInventoryItemINV005 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryOverrideInventoryItemINV005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * FrmInventoryOverrideInventoryItemINV005 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryOverrideInventoryItemINV005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{

		if (!startWorking() || !isVisible())
		{
			return;
		}

		try
		{
			clearAllColor(this);
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leEx = new RTSException();

				// VALIDATION
				String lsOverride =
					gettxtNumber().getText().toUpperCase();
				if (lsOverride.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					leEx.addException(
						new RTSException(150),
						gettxtNumber());
				}
				if (ValidateInventoryPattern
					.chkIfItmPLPOrOLDPLTOrROP(
						caProcessInventoryData.getItmCd()))
				{
					if (lsOverride.length() > MAX_PLATE_NO)
					{
						leEx.addException(
							new RTSException(150),
							gettxtNumber());
					}
				}
				if (leEx.isValidationError())
				{
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}
				// END VALIDATION				

				// If it's a sticker, strip the leading 0's
				if (csTrackingType.equals(STKR_TRCK_TYPE))
				{
					while (lsOverride.substring(0, 1).equals
							(CommonConstant.STR_ZERO))
						lsOverride =
							lsOverride.substring(
								1,
								lsOverride.length());
				}

				gettxtNumber().requestFocus();
				// defect CQU100004471
				// do not check on the item if it has not changed.
				// Treat it as a cancel, otherwise validate it.
				if (lsOverride.equals(getlblNumber().getText()))
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
				// end defect CQU100004471
				else
				{
					Map lhmMap = new HashMap();
					caProcessInventoryData.setInvItmEndNo(
						caProcessInventoryData.getInvItmNo());
					lhmMap.put("OLD_DATA", caProcessInventoryData);
					ProcessInventoryData laNewInvData =
						(ProcessInventoryData) UtilityMethods.copy(
							caProcessInventoryData);
					laNewInvData.setInvItmNo(lsOverride);
					laNewInvData.setInvItmEndNo(lsOverride);
					//defect 6562
					//force Calculation for new item
					laNewInvData.setPatrnSeqNo(0);
					laNewInvData.setEndPatrnSeqNo(0);
					laNewInvData.setCalcInv(false);
					//end defect 6562
					// defect 6120
					// set the qty to 1
					laNewInvData.setInvQty(1);
					// end defect 6120
					laNewInvData.setInvStatusCd(0);
					laNewInvData.setInvId(null);
					lhmMap.put("INV_DATA", laNewInvData);
					lhmMap.put("DATA", caCompleteTransactionData);
					getController().processData(
						AbstractViewController.ENTER,
						UtilityMethods.copy(lhmMap));
				}
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd =
					caCompleteTransactionData.getTransCode();

				if (lsTransCd.equals(TransCdConstant.RGNCOL))
				{
					RTSHelp.displayHelp(RTSHelp.INV005);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.INV005A);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Invoked when a barcode is scanned.
	 * 
	 * @param aaBCE BarCodeEvent
	 */
	public void barCodeScanned(BarCodeEvent aaBCE)
	{
		if (aaBCE.getBarCodeData() instanceof PlateBarCodeData)
		{
			if (csTrackingType.equals(STKR_TRCK_TYPE))
			{
				RTSException leEx = new RTSException(712);
				leEx.displayError(this);
				return;
			}
			PlateBarCodeData laPlateData =
				(PlateBarCodeData) aaBCE.getBarCodeData();
			if (Integer.parseInt(laPlateData.getItemYr())
				!= caProcessInventoryData.getInvItmYr()
				|| !laPlateData.getItemCd().trim().equals(
					caProcessInventoryData.getItmCd()))
			{
				ItemCodesData laItemData =
					ItemCodesCache.getItmCd(
						laPlateData.getItemCd().trim());
				String lsItmCdDesc = laItemData.getItmCdDesc();
				String[] lsMsg = new String[1];
				if (Integer.parseInt(laPlateData.getItemYr()) == 0)
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laPlateData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE 
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE 
							+ lsItmCdDesc;
				}
				else
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laPlateData.getItemYr()
							+ CommonConstant.STR_SPACE_ONE 
							+ laPlateData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE 
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE 
							+ lsItmCdDesc;
				}
				RTSException leEx = new RTSException(715, lsMsg);
				leEx.displayError(this);
				return;
			}
			gettxtNumber().setText(laPlateData.getItemNo());
			caProcessInventoryData.setIsBarCodeScanned(true);
		}
		else if (aaBCE.getBarCodeData() instanceof StickerBarCodeData)
		{
			if (csTrackingType.equals(PLT_TRCK_TYPE))
			{
				RTSException leEx = new RTSException(713);
				leEx.displayError(this);
				return;
			}
			StickerBarCodeData laStickerData =
				(StickerBarCodeData) aaBCE.getBarCodeData();
			if (Integer.parseInt(laStickerData.getItemYr())
				!= caProcessInventoryData.getInvItmYr()
				|| !laStickerData.getItemCd().trim().equals(
					caProcessInventoryData.getItmCd()))
			{
				ItemCodesData laItemData =
					ItemCodesCache.getItmCd(
						laStickerData.getItemCd().trim());
				String lsItmCdDesc = laItemData.getItmCdDesc();
				String[] lsMsg = new String[1];
				if (Integer.parseInt(laStickerData.getItemYr()) == 0)
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laStickerData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE 
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE 
							+ lsItmCdDesc;
				}
				else
				{
					lsMsg[0] =
						TXT_YOUVE_SCND
							+ laStickerData.getItemYr()
							+ CommonConstant.STR_SPACE_ONE 
							+ laStickerData.getItemCd().trim()
							+ CommonConstant.STR_SPACE_ONE 
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE 
							+ lsItmCdDesc;
				}
				RTSException leEx = new RTSException(715, lsMsg);
				leEx.displayError(this);
				return;
			}
			gettxtNumber().setText(laStickerData.getItemNo());
			caProcessInventoryData.setIsBarCodeScanned(true);
		}
		else if (aaBCE.getBarCodeData() instanceof RenewalBarCodeData)
		{
			RTSException leEx = new RTSException(714);
			leEx.displayError(this);
			return;
		}
	}
	
	/**
	 * getBuilderData 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GB2EDD4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8DF0D4D755D42943B88C836AD83EFE99A7DF259AB595BE0DCACBD46A20B2EDC763C87D7AB931C3157AC52D0A36544C18FE73126CA621ECA07C28E10AE5A98192C842121FC2B7A964E7C3D84C1FE401B4CD86DE32AF3BAF7BF65F327BB65904001EF3FF5EBE1237C95399B2F3F25FBD775EF34FB9775CF34E3DF7058307D32D79858784A15FAA303F337902E02B9104BFAB0FE5F1DC5621A61BE078BB
			8230D778394F8265D6E833381BECE5C25AE08954DF017A3A4EA65B5F60FB3F7058603A137011E41EE550962C9F79417C737C544F67A9A6756E896C349BA097F0077E5EE4737F9D6717A64A993E856A3B59B7C2FE01A0585F44BE4561F283BE1D75333DC94B124358E74944A1566E877C9200EC006DEFA6E86EA45CBD68BC476AC32BD205D71EBAD9D00AF39968F368BBCC47EA2C348BDFA10AA894D22FF920EC6E8B784E0F2A41DAAD6ED535DAB96A2D0DE8515AD1C516C2D08DCDC021C626EAFDB159693C98893A
			B926F7C20AC4E42F24A35C1A94F477DCF3B89A3791D9EA42A5E49E10956A146F9F15B6B33EBE95C89514FFF695A5633B52CE796D32A778FEE55703C5F9F6247D85328EB8EEB954B781D019068F76ADA893B48455D06D2265794384172765C813ED639144352848C79C3ED782249D61F23EFB024BB9F0FB2D10BB52DD30A7091C163728FC55EFA5644CD8751C65A0313731F13499BF34AD87C8855884308D40F2344976863173A3B21AF96B0856CE84EB8D075B94EF24DB75CA8AFCBB1D5042F0D724D1B5629584EA
			4BF561EA93FEB89CBAFA2F9DA7EC6F3A40EDAF7D4A67040A9D5DC5D69359167C2C3B4841E8380EA6E423F295A8A8179770CBGD6832C834882582E4B95106B97124B9D5224C8C8D47AE3A3E1B1223545E445ABC518FCEBA766134F8C6838EFAEFA9C55CBDA3DD1AF59DF6DA9A8B651CBF6DAFD012D026A2322A22197BA772BC967F6D54CDD8F62A7AA508EB3089D8E40F7BE33D10A7F9641279B70F47FDCA4F82CE7C21B7D98375B015DDC06AE4B43C25919DED399D6FF47D530749865A765586C35FDBE08BEECB3
			60378164819CB85EE4EBG6883709E676B7BF5797487345BBE435A52316A78DF211462DAC4F407221A989A152E0991D99CD12428407A4447117711FB465DA17BE994666814C2D2C454E4B58454452084069425FDBEB63E977BC4A4526A110392CC908C8B1C6E0A717DD0AAE2D43B9A760A1A1468C15BBFBD0E3A1C902F81C5A0G5F9947D167EE628F1742F7AAG5555AE95F13958FE026BE5EFE09F147FG4316942753A50FA29BE2E40A4F3FCBC1DFA22117F2C9E1A964050FA8EB5B276CCC68E5CC55F9AEA7F8
			5CA36D40F8D4473F296C25F820E154B2EFBFCE5AC5CD0B48A3B1CD6A17B4CD8E799263EB89AF51FB36DFD4E0FBBC0CB6D681F2B583F46972FDC359EF102FCBD583313087D70E6929F492F5F2937A47CF70387B040AF1B8658437535F8DF0BBF515BEAAD4BF5BEDEA27A9600332CED07B4CB9B1FB2FEDA2B493F3F42983EC0E456C7D457996094CD1F89F68A3D03D47EE215E4BAA1BEC55GDD15DC6FFFD46CF43F3509D1096F305E882C1864CDD8996D1B93CCEEE33F8F1A5B580E20390D7DB9B83F8D95064CEC08
			363D9E425822CE068CB639A7340F6C61D0C8A2CC6BC3841A837C4E033A10C332F6DD767AA42DDF1E16A0CF28C45B3AC97620973E63155C16A60FF2DB2A187EAFE1630D4A8233F8367A514E022CCAEA9B5BAA9336C4F9AAA37ED4D224A0A82ADD0D05B40A7FAB595F8C4F7CCBC9583C6F6EB05AE3C01AE27D76050B8474A3CACC52E5DBA76EC29DC764A0AC95EED816DBD6CC9AF3CBCBD5138D7350CBD08ED5715C68CA7884149FB065E136746588722A810DE5G29D55C16D6048F9A76B0B322F6D5D1681268EBF5
			7BD6992E2DF4ABA6010B46CD7E8FDD3E0F5C5AB72B6D9A131536FF7C964A2A987C437AAA14F5D24FABB1FED05C8A02DB858A5BD14863F1F719B1C776C6DBCC5795A12F0A2EE7594959316C97D1246BGFC4BC9AE3BB5E274DF2D5128BAAA13D041562848723630D08E6AA351C7A1C96895CF3FCB9695555726464136C82E390278C01443F312671AED5338964B213E86A0D367A7A3B257469E0F3C8E65189C09C20E641562DC275F0EA0CD0CDD8C4DF33ABA232E366C6BB14D699036B09D654C51D5D58C79301CEA
			32151F623CB923FB8D3A027C93C2358FD612731393AD24B64F6AAFC52D5C66856EB3BFB909B9D175A9F33F5C42709ED67A2124394F57093EE6CE4D4E09B6D2BD1E1A1D93DD1A66B9D18A348955BCC69FA6F93F976ADAB5176B812D54A0D71FAA07B4DD265A986EDBB5A27B64904755939CE45F441151B877C53246CB01E69AG45954491178975ADBA6E01A912A3C05DD643F1CF911C9D6A8DB5BCFEE5CE237FE8516B5FA3F52FDE6F199EE06D77AF3E79266FE7FC233A2C1840F50B6B75CAD21FE2736E971E1C7C
			8B4AAD0D09B185C2119A15FA43AC55837C269A54DFB651BD0EDBD6437D63FFCF223F9903E49042D3FFD89A95D2EA316F41B8772DA5D0BF84508CB084B083C067DCBD05B4022AD7D474B956CEF1BF35CD6F77826927499A59832F54621D4369BB024E3B47093CDF567DC13B8AA79CCC4DC2581F7A7ABFCD9BFD7D3953B4CF9A02B2FC1A4F7372A47A46F1F1C20C57C6A7C11E5A6BF2482BCE82BD4C16419139FAEF7474FAAE39FBBAEF74765CE86F6A6D6FE03EA0F552688B4E3D6ECC6AB7171DE6E7GD6EE6B9773
			297F3A90677A5D82EDB687621E0E9FB00CC5BED7C107DAA8E97BEB7734235F28920C6D27270C6D48EBFF2131DD3C275DEA98CF65FA7A98BF1367BD1DA6A8DFEBCEFAE762F1D0F9623CFCCB4C37158A45D4375F1B42FD25C5E292257B43770D7AAAFB3822A0BF895DF50CDE26E376DD4C4BD53826A4E698964D693C0CAB2FD01D3EF83B0449355D41E342572C6833AA4FC0FC8298B843FD565A3B58B78C7565E7384F526E1C02721AB84ECED59DDE99E2BEC984815997D1E117E8D394CBA2B15B530F44BF5CF97849
			E22E6F0CA79E9156FFACAFFFD7923D241F217AF80615E5C744FCAC29AC5F3D0A3A49033672BA58FBG9EGBF4012BAAE5B06BB48A3643348FE2B260923FE928DF91AFBCD16A693E85C5C14F64EB5641452483FFBAC19FE0042E5D8165E11F108E09E51A7F0FF776B5B33F573293A04FFCD2B1B6BEBE98C2AA942F54B2C23B128D447B7937CD6831E46268DE41D31EE2907FDDE2F477E3BC8D34F2D6932B1F9B65C457D00F9B0F893F91AB840FE689872D159F7DC4776EC20158BF028214966GF0B5F05A177F7596
			552909BEBBA031663E044F77032B494F8C4FDEDD783C7E7CD54CD9C6D4CDD303C81F6337DDC5DDEBEA18A0A96ED7D75146A0447AB55A151FCD2E225DAB5298C153D819BFC34E13A01756406367C72DBC4E2EEE6071313E886DAC936A59G5B81CA9A1BECF600F30D3CCF9B695385F585E0B9C0BAC09640F63D4F13561BEC2DF17CFC7B8467A1BEFCD6BFFB12F148D3A69BC3674A56675A39401E343232F496FED7127EC81F47F12A5B572EA7DFDF4B40426BDBBAC072D4D8974FD4D8E2E71C8112AB0D0DC5A55D0E
			7EB6D026635C51BEB5AA2365D25FDBBE037D95F1CA0DC17F1937F1AD6BACBC9E16C13D92208F400BGF1F89369733035BC091CBC966D971A4BB02F0FEA23DDA3CA87DEAC726391EBBFDF01BAD1C694BCA5B11C62C73DEBF12DA7969CA1E7D40AFF62G6A9536240677CB6042997E6C5BBBA90DD97D2BA3D60AFF8FF12E3348F33C48984FECE729BC8EA8E97B6BC1337665FA7B9D3F31BD1535EF031266919BA423CEF2D9FB73B9505FB9CE6309E933B97CFAFBCD0FD97B8C34D33FB5E8617774AB8137799C4F354E
			5C44BBC2BD5E550A131A5359894AEE93FDDDE4F5E94CFB44427388619D9E7332884DF3D06F83703F434F717163BC5E24FF6EE1A1673766EF8BDBBD0D85CB5E21FBE84DBB09B83CF955F396CAE7BC40699C7A52FF1236E4F942D6B65E62CC5061FB8DE27AD812185EA50E70183E3628046DB517134BF16722710DA4F3CFE3C13238B94366CDB14C9F87B9A86D56A0271D6D64FA5F3D7B2C30483329BB940EE997E5C9713233E9061F53BD5044691EBF5538A83AED061D414F3CAFCBBCDF6EEA62EBDDCC4EE4CB211E
			821081405EE226B98F4F68B8EF8F47D91A39AF3EE979F0EFG218FB96E730B9EC7633E0B447766E69A7773F5FCB741DFB260693E7A093E2FBC50363E1967FDAB8651668E3D8BB18B208F408F308CE04D3BBC2E278FB804856E5DBDEA388B9C3DE23892E679527BC8FFBD505A82108BD0F63E49D68150FC1E53FFF5G5FED126519D7D4159CE770E4A34AE091A40D0808F80226BA4CA8B253E15FF9EA63DED6464FA7E2A4656D4AB07A226500CF835888709CC02E4ED75BFB8B3EA7F54467F9CF3A98B4632B7C82FB
			3FE3E54B0584DFF45C63FE9C6739E09E537D8CBFB3E7DC2EB41FAD2DB421C7ED66B659B3EBAE4C362525843F6E42ECDBAA1D66361489ED76963EF7DE1DB61BFFE98B1DF7B5AB53DBE673AD77180D5B487AEDE1E54E1CF117497BBC7D6EB7FCE7DB86963927963FEFF75C57B9287DBDB418BCFF7A4C20791D7A1AC173BB75AF8E4EFF277E154164F76A0F8F4E3DD37F437B49F94BFDFF615CAEFF48BCF7AF9E1A1B3B5B064CF377FDC3F3F37727C9FC4C83DB3135725C5DD14CF377F29D37C6C11C9F6A4BDA79FE9F
			9CCEAE13B8EC2EEF5F30393E0343733F134506132D8765EFB84865C851F97ED6104B1126638E7B399C39BAAE0B60ECED30B75BB86E204C47B668389BA4FEAE05FA0A0E5BB3467B2DEB637B7A0169445E7A3861FB7F747D5FE75577F58E76FEF81B6BDAE867FA885D66FA306938BFF69BED066216109CE2887AA8FA3F97FB7858380EFBAD04384D6DF41DE88E7F73A2F2AF84C6C56F75ECB7D037A5AE6C5FD96C1A076FC2867C4C1583F49170AE1078E9789EE65FF42E8D648D552FC664E90C23CA2BA27BC2F8C2E7
			F70F4FAABCEF6E037959BD634D84BF4F86676347AA709C561C1FA5C063D76C1B4EF7494F67DB6E627A78BFBF5210B1FD547B7DBF690779F4A674233CB86405F8A9B670720CAB418B36ED05FACD3ED1B79364DEAEA8463B2410CF736BF32FA07B83473D244FFDFCEC21F559E918BF8F47F4503931DE815F3720EC9778DD6EC3E2C2478F5E1E0FF6F1929D3F417468G5A6EFB567C211EFB6D6B7BA3093972FA961243EC2E230CFEB313A3B15781B519FD9DA66DB02F1078237C7CFAD490785D5F8BB663994A75BFF9
			79FC2E593CDD6AE079C4C7628CE37F65CFAD140F17947C9D1E719E3B9F7FA72E315BBBFABC9DD7F49EBC98CE188CB77C9FF4AC6D7F97997943373B39B2EE3FC0696F75A568BFAA95C0F946E15EBFB90D86FFC2E7C1B9713DF29C3FE9AEFBB7006B1985BAF989206CE2132D922099408360871881D88910863091E08BC08EC09E002D93E285C0C3A70F05273DF8971EACA7E787EFC3A64E635AA78249E368FF84964E8B9E89186785E90139F941E381733C602B81E3DEC0F16B83E84FBE60FBC06F77CD9D771E0E7B
			8E2109386B321778820A5FCC78C27CA589A7546FB089DF1384B740F0BFB6605EE3386782F88F85A2439661E3FF96407DB9A6AB8A2D3FC07A48212824697A78A561D18E0B863EFF236310EF7A5BD557D8A798E37AEF852BA6D46B4F73769ABB3F1332A710F89DE5375B693B3D860E836F2E6CAA6F287F0453AEE3479039F832EB2ABDA20595F1D4F2EA42BEFC9C0A7A710CEF0FEA72E8C00AE8C2D12323316EFA3BA6D86A9C0E3AE8C45843EFEAEA5DC4262D93754E42DFFC3630C0303805A23A46EE41028B65962C
			E49D5DC2A12CF19CFFBB05F7980D8C5E085E926E6AD146424026E3D15F78DB853C0F376AD8DC49AE86A5F3A86C3A0702156DBCC21779242AFF995FE9F29FFCA3693B56A5B775C5C36E59EF5C7FCB6257EA40E738795E2A7257A45BDBF0246C8F0A0A829EB4509A72629BC25B14A6A5724A677DC8BF4A7B30C65A76A239DB9CB70C79B7D0CB8788DAE52AE14190GGB4ADGGD0CB818294G94G88G88GB2EDD4B0DAE52AE14190GGB4ADGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2
			A0E4E1F4E1D0CB8586GGGG81G81GBAGGG7B90GGGG
		**end of data**/
	}
	
	/**
	 * Return the ButtonPanel1 property value
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// defect 8240
				// remove keyListerners on ButtonPanel
				// ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				// ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				// ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 8240
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbuttonPanel;
	}
	
	/**
	 * Return the JDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(new GridBagLayout());

				GridBagConstraints constraintsstcLblEnterOverride =
					new GridBagConstraints();
				constraintsstcLblEnterOverride.gridx = 1;
				constraintsstcLblEnterOverride.gridy = 1;
				constraintsstcLblEnterOverride.gridwidth = 3;
				constraintsstcLblEnterOverride.ipadx = 46;
				constraintsstcLblEnterOverride.insets =
					new Insets(27, 53, 11, 53);
				getJDialogBoxContentPane().add(
					getstcLblEnterOverride(),
					constraintsstcLblEnterOverride);

				GridBagConstraints constraintslblItem =
					new GridBagConstraints();
				constraintslblItem.gridx = 2;
				constraintslblItem.gridy = 2;
				constraintslblItem.ipadx = 116;
				constraintslblItem.insets = new Insets(12, 4, 5, 4);
				getJDialogBoxContentPane().add(
					getlblItem(),
					constraintslblItem);

				GridBagConstraints constraintstxtNumber =
					new GridBagConstraints();
				constraintstxtNumber.gridx = 2;
				constraintstxtNumber.gridy = 3;
				constraintstxtNumber.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtNumber.weightx = 1.0;
				constraintstxtNumber.ipadx = 150;
				constraintstxtNumber.insets = new Insets(5, 39, 7, 37);
				getJDialogBoxContentPane().add(
					gettxtNumber(),
					constraintstxtNumber);

				GridBagConstraints constraintsbuttonPanel =
					new GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 4;
				constraintsbuttonPanel.gridwidth = 3;
				constraintsbuttonPanel.fill = GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 56;
				constraintsbuttonPanel.ipady = 33;
				constraintsbuttonPanel.insets =
					new Insets(8, 76, 9, 76);
				getJDialogBoxContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);

				GridBagConstraints constraintslblNumber =
					new GridBagConstraints();
				constraintslblNumber.gridx = 3;
				constraintslblNumber.gridy = 2;
				constraintslblNumber.anchor = GridBagConstraints.EAST;
				constraintslblNumber.ipadx = 36;
				constraintslblNumber.insets = new Insets(12, 4, 5, 11);
				getJDialogBoxContentPane().add(
					getlblNumber(),
					constraintslblNumber);

				GridBagConstraints constraintslblYr =
					new GridBagConstraints();
				constraintslblYr.gridx = 1;
				constraintslblYr.gridy = 2;
				constraintslblYr.anchor = GridBagConstraints.WEST;
				constraintslblYr.ipadx = 56;
				constraintslblYr.insets = new Insets(12, 8, 5, 4);
				getJDialogBoxContentPane().add(
					getlblYr(),
					constraintslblYr);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogBoxContentPane;
	}
	
	/**
	 * Return the lblItem property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblItem()
	{
		if (ivjlblItem == null)
		{
			try
			{
				ivjlblItem = new JLabel();
				ivjlblItem.setName("lblItem");
				ivjlblItem.setText("Windshield sticker");
				ivjlblItem.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblItem;
	}
	
	/**
	 * Return the lblNumber property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblNumber()
	{
		if (ivjlblNumber == null)
		{
			try
			{
				ivjlblNumber = new JLabel();
				ivjlblNumber.setName("lblNumber");
				ivjlblNumber.setText("5052WC");
				ivjlblNumber.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblNumber;
	}
	
	/**
	 * Return the lblYr property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblYr()
	{
		if (ivjlblYr == null)
		{
			try
			{
				ivjlblYr = new JLabel();
				ivjlblYr.setName("lblYr");
				ivjlblYr.setText("2002");
				ivjlblYr.setHorizontalAlignment(SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblYr;
	}
	
	/**
	 * Return the stcLblEnterOverride property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEnterOverride()
	{
		if (ivjstcLblEnterOverride == null)
		{
			try
			{
				ivjstcLblEnterOverride = new JLabel();
				ivjstcLblEnterOverride.setName("stcLblEnterOverride");
				ivjstcLblEnterOverride.setText(TXT_ENTER_OVRID_INV);
				ivjstcLblEnterOverride.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblEnterOverride;
	}
	
	/**
	 * Return the txtNumber property value.
	 * 
	 * @return JInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtNumber()
	{
		if (ivjtxtNumber == null)
		{
			try
			{
				ivjtxtNumber = new RTSInputField();
				ivjtxtNumber.setName("txtNumber");
				ivjtxtNumber.setInput(6);
				ivjtxtNumber.setMaxLength(10);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtNumber;
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
	}
	
	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_INV005);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(425, 200);
			setModal(true);
			setTitle(FRM_TITLE_INV005);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInventoryOverrideInventoryItemINV005 
				laFrmInventoryOverrideInventoryItemINV005;
			laFrmInventoryOverrideInventoryItemINV005 =
				new FrmInventoryOverrideInventoryItemINV005();
			laFrmInventoryOverrideInventoryItemINV005.setModal(true);
			laFrmInventoryOverrideInventoryItemINV005
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryOverrideInventoryItemINV005.show();
			Insets laInsets =
				laFrmInventoryOverrideInventoryItemINV005.getInsets();
			laFrmInventoryOverrideInventoryItemINV005.setSize(
				laFrmInventoryOverrideInventoryItemINV005.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmInventoryOverrideInventoryItemINV005.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmInventoryOverrideInventoryItemINV005.setVisible(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leException.printStackTrace(System.out);
		}
	}
	
	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the
	 * view
	 * 
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			caBarcode =
				getController()
					.getMediator()
					.getAppController()
					.getBarCodeScanner();
			caBarcode.addBarCodeListener(this);
		}
		catch (RTSException leRTSEx)
		{
			// defect 11071
			Log.write(Log.DEBUG, this, leRTSEx.getDetailMsg());
			// end defect 11071
		}

		HashMap lhmMap = (HashMap) aaDataObject;
		caCompleteTransactionData =
			(CompleteTransactionData) lhmMap.get("DATA");
		int liSelected = ((Integer) lhmMap.get("SELECTED")).intValue();

		caProcessInventoryData =
			(ProcessInventoryData) caCompleteTransactionData
				.getAllocInvItms()
				.get(
				liSelected);
		if (caProcessInventoryData.getInvItmYr() == 0)
		{
			getlblYr().setText(CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			getlblYr().setText(
				Integer.toString(caProcessInventoryData.getInvItmYr()));
		}
		ItemCodesData laItemData =
			ItemCodesCache.getItmCd(caProcessInventoryData.getItmCd());
		csTrackingType = laItemData.getItmTrckngType();
		getlblItem().setText(laItemData.getItmCdDesc());
		getlblNumber().setText(caProcessInventoryData.getInvItmNo());
	}
}
