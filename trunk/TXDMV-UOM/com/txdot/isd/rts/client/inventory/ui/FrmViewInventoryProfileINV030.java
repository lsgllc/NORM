package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmViewInventoryProfileINV030.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames modal
 *							to each other
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields.
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/24/2005	Resize radio buttons to fit on developer 
 * 							screen.
 * 							also remove all setNextFocusables.
 * 							defect 7890 Ver 5.2.3
 * Min Wang 	08/01/2005 	Remove item code from screen.
 * 							modify gettblInvProfile()
 * 							defect 8269 Ver 5.2.2. Fix 6
 * Ray Rowehl	08/12/2005	Cleanup pass
 * 							Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/15/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Remove selection from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		12/30/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove ciSelctdRadioButton, carrRadioButton
 * 							modify keyPressed(), getpnlRadioButtons(),
 * 								getradioAll(), getradioCentral(),
 * 								getradioDealer(), getradioEmployee(),
 * 								getradioSubcontractor(), initialize(), 
 * 								getradioWorkstation()
 * 							defect 7890 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * In the Profile Report event, frame INV030 displays all the entities 
 * that have inventory profiles for various item codes.
 *
 * @version 5.2.3 			12/30/2005
 * @author 	Sai Machavarapu
 * <br>Creation date:		06/27/2001 8:59:54
 */

public class FrmViewInventoryProfileINV030
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnPrint = null;
	private JPanel ivjpnlRadioButtons = null;
	private JRadioButton ivjradioAll = null;
	private JRadioButton ivjradioCentral = null;
	private JRadioButton ivjradioDealer = null;
	private JRadioButton ivjradioEmployee = null;
	private JRadioButton ivjradioSubcontractor = null;
	private JRadioButton ivjradioWorkstation = null;
	private JScrollPane ivjJScrollPane1 = null;
	
	// defect 7890
	///**
	// * Int used to specify which radio button is selected during 
	// * keyboard navigation
	// */
	//private int ciSelctdRadioButton = 0;
	//	/**
	//	 * Array used allow for correct keyboard navigation
	//	 */
	//	private JRadioButton[] carrRadioButton =
	//		new JRadioButton[CommonConstant.ELEMENT_6];
	// end defect 7890

	private JPanel ivjfrmViewInventoryProfileINV030ContentPane = null;
	private RTSTable ivjtblInvProfile = null;
	private TMINV030 caTableModel = null;

	/**
	 *  Stores the Inventory Profiles for the selected entity
	 */
	Vector cvIPVct = new Vector();

	/**
	 * Stores the current inventory profile to send to the database
	 */
	InventoryProfileData caIPData = new InventoryProfileData();

	private boolean cbInit = true;

	/**
	 * String that stores which entity radio button should have focus 
	 * when the screen is initialized.  Primarily used when coming 
	 * from INV016.
	 */
	String csEntity = new String();

	/**
	 * FrmViewInventoryProfileINV030 constructor comment.
	 */
	public FrmViewInventoryProfileINV030()
	{
		super();
		initialize();
	}

	/**
	 * FrmViewInventoryProfileINV030 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmViewInventoryProfileINV030(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmViewInventoryProfileINV030 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmViewInventoryProfileINV030(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaWE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaWE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}

		try
		{
			if (aaWE.getSource() instanceof JRadioButton)
			{
				getInvProfile();
			}
			else if (aaWE.getSource() == getbtnPrint())
			{
				getController().processData(
					VCViewInventoryProfileINV030.PRINT,
					cvIPVct);
			}
			else if (aaWE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaWE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV030);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Add the Item Code Desc to the Vector.
	 * 
	 * @param avVctIn Vector
	 */
	private void addItmCdDesc(Vector avVctIn)
	{
		cvIPVct.removeAllElements();

		for (int i = 0; i < avVctIn.size(); i++)
		{
			String lsStr = new String();
			InventoryProfileData laIPData =
				(InventoryProfileData) avVctIn.get(i);
			lsStr = laIPData.getItmCd();
			laIPData.setItmCdDesc(
				ItemCodesCache.getItmCd(lsStr).getItmCdDesc());
			cvIPVct.add(laIPData);
		}
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setBounds(261, 263, 74, 22);
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				//ivjbtnCancel.setNextFocusableComponent(getbtnHelp());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnHelp property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setBounds(364, 263, 66, 22);
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the btnPrint property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnPrint()
	{
		if (ivjbtnPrint == null)
		{
			try
			{
				ivjbtnPrint = new RTSButton();
				ivjbtnPrint.setBounds(150, 263, 74, 22);
				ivjbtnPrint.setName("btnPrint");
				ivjbtnPrint.setMnemonic(java.awt.event.KeyEvent.VK_P);
				ivjbtnPrint.setText(CommonConstant.BTN_TXT_PRINT);
				//ivjbtnPrint.setNextFocusableComponent(getbtnCancel());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnPrint;
	}

	/**
	 * VAJ Builder Data
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88G90F5D4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BC8BF8D4E71554D20B1F6836AE1F26ADFE52CA95B5EAD46A32355B0FEE631A352C56B5EA740BEB5C32CA95155DD20BAB5ABFA101C0A6ED8AA1D0CBE92CE0C34548A3EDB18D4DEB7218B0F9B0B3F9G81F21339195C644E5C494C9D12C0C8761C7FB5B7131984D8617B4EF767BF7F63BC7E731F7F1CF3EFA0452FAC4B4B4EA9A6A4BB0F70FFEFE593E23393F2778757DC9238346AEA9B317C7BBD408B44
			FFBE971E99503779DD5536A2723E69FCE82F07F66F02EA5B9E783D0B9CE97156400F84F42A202F7D2DDF1E1A1B4E5F3FAD68EC236D7B7CB868B9008D00AB2E29367555A07D877DFBB97E84341F633F9132F38859C8473CD759EB41AF67633278B3239647FC4B3F0F77D7C15B83108C303AB63A66F64A5DED15A7F83B774EE5E463D7DF4FA9C4DA16758514563139F67EACA51F270A58C22AFA7F0CF4BAC6FC6FE801CBEA40B4C29315150DA1FF17260EB98426A3E48CEA3A6AE86BBAF27C880CDFD907FCFECE4927
			EB90B289ED4653B78A2BB84DA5A34B08FE5A39B591CF1BB8AF7B6A23BCEDAC38ED6B1AFADC7B37D447B82F975ACBG983F7B36BB843F83063F5C9C779AE639967616074CF079002E813F65D2107208F62D32B48604ACC576EA5B9DF6442576A18F4D703B8FE05CAEE4BC791A1031674AE749267E561C67934818EEE732ED36C7E55497E7643EC86D681513E88BA79A40DE8102GCBGD68224833CC46D77FBEF3F8E4F5AFE335E707B0DC0EDB0D827FBC32D06D75561F7E5A574F01CCB9DB0C2DEC2180DFED1B99C
			079F816B9BE65AE754263A093029E45747480127DB37664511ED4177DB3712C636C6F1E3D4B6A657175EC139AAG5F81B08430GE099406AC6A157012A637349658818EAA820686EC8FFD08919F591CD772AA1AEDFEEE0AE7962811B37733A67B13D2CB8EE55CB66975AF2364551CB660A63B919DC9F9BADFAE134CFA524DD5CB4FBBF289FE9C2BBBC21209D16416FECEE238CDFC7711396BCBBBBDF25F60B6D45503721C958ED4FF3C286E76E5D2468E8FBDC99127E591E63E9E67C684D31FB7B219A74CDC900DF
			893086E0ADC02E235AD68AD061907BEBD47D659A6D36433237ECEE594992F82A63E6C8F18442269298D03B141026746BEA1870B1C7CFA26F7DB3663DC94F53G50E8D283EAC8B1B5A3G2BABFE958CA94C46D41FFC814704D45A6B517CAA90708709D8376964AE14DB891B1DC12FE22A51912C3F7DA46A72125285AB428270FB2183F57EEF5497FA60F7900069AA3596F1E9504E12FA796A491D70BC85062D6A1515F6EDG59D0C29302FE6ABB68CBD474D5F6B528863C70A34C7B9EFBE7FBD4AF030664799B940F
			E7249E988FCB7CF75EF90161E18D2B16C57F8FE83FE21AA12DBFE22AEE55B43500AFBA7F471417700C6397A67C0C57210DED8439ECAD5536FDADC23EE075AE0BFCCE43980984BD38F3DCCF7D544F9E01714DAD62BEFDC7417BD5EF91F67A739EE12776427B4961EF3546355325600312DA18FD2634441E35E63AE6144663FDBD57F96E2F85F4CE1751F872265058421FCC6F65F654FB86505D88D0FCCA687DD355256C3C55A9E1D51C3076906C186A0DDA999BFBEFCDE29BDBD1935F46BED3935F46D6564CEDC38F
			554433A15677E88D5EAD46D840E213E9B5BB69998E84D44A341CC2587D7FF38F6AC28BE8E63766752926DB3B2C4239BB053635161E4143703B6614302531BF88DBBAF079DE327AE2D9CE3C7BAC697E261C24D3ECAF13CFC5ED0971742F3528F3D5D77D2028FAA392B0997E6B74FCF3BC77AF0F54459F7B15BA344791F5020FFB2CEEABC1BF22C7D4A9DB0AD200BA8EE9FE58AABC30BCE61C88D8E346B5409F4617C54EEA5B8127080B5E3F73E5F84E976BB09B72B5A00FD5B03783E048A9EC28297EC54B596546D3
			EF68BA5B8A39C79576A25CD3F5B42202EB4643FDCA4A7536FDE7CCDF97171175575BD1C65D629796B8D146DF4BD8926F8D065B590F38BBC1D1CFEF9177F0EB11B5667506EB626EA7C22A1347544E58BBECE9A32E3B8970792DC276076DD63FD59B8E9B839A3DA2785EEC4DFD134C97F77A682D2353285365F15BB5C5B7FCF5463808B11F203E8F655857AAE24C8F0D62DEF8202D83AC147C789BE65BD628E187BC8735D098E2A32FBAAEF4BA5E00EB621D4551A216BBE655D57A4E3638319C75252D5C0F36466A6A
			BF1B100FB5004F123CBD557002C5D790F742956D031D947C5C5B380559BAEF5F5F18A7EC1D88DF390E46C29BDB6347BD79EDBCB7604FA370E4B1CFC18875E5EF0B0D05F6D33C27ADB6967A702808053C501756A66E660CE66AAB5A2BEDD56DC22E4A26C20BDC9D0696B025CCBFEE4273EA04B41F969038759487D1B7F5E06CFEFB166E3193568C82B0DC3701380550DEAAF15F8EA2EEB53453A56E15D1442D037666F6F1EF3D90C23F115FA15AAF51F615ECFFF474C66F9CDCF33EB10C46C3B7CCE34BB50CE13A7C
			FEB36E5B38EC7F1036A7789A3BC809319B77D09DD4A2BADCC1C6D8ED8F72908F6D378375F7146A996775F5883F7839B174B70390844235648E2A8344BC0DE383067029D9B0F6A3C0B160ABGBCG0C664F0238065F70AA3A2411BBAA7C54C3F25C1B23B84E54CCFA86561C469A42136DC4723E74C09965E18E5FDCEFC03203D1DAC0E5BE5F99327A7CA2970B17AA706992F41B8D7415434AA5E53CBCBC867215F7EB812FB1860BE1548C0E495EFE312D5D536CE8EB3A585EF6315E596EEE603EE1591855B71C58D1
			19500F7639181D187C39562DE4B3FF36C46ABB997A563A90D79AAC0E597F0F6790D20E73E83FEBC6BF7A11C3445A3FF0C6BF727AE42E357F0BB37A73AC7319DC0F9D9439796647D6907D012A04B51143EEA6CF87FF0E3F2AE42FAEA05B18EEDF9A45F3E606A2AADB776B672C7AAA3A7BC0CEF602F59771751638E3EBADCF9D42BD25F708E553AAABDBF05BF526534AF0BE17AB4DAD6E08562B34A86529366D85B86291BECCBB0DE31B216D75889FD65E71A7F8F6A943BC3BEA70EAE0ECB4A084A4589C1C9AF302E1
			694D4C4F78B3460D65450FEC937ADE7960FDE4553BB7E797A4504BC28F5347F27EAC7A3D120DCFA64BB7DD281BF4685BGD05CD9EDBB8CD0835053A9E40BF4A00F9057A27B3526298C8C515BD1043BF83C22E8BC6CEC6DDF3A11D3364617BA8EA6528F2C50825B525EBF8CB71AC77191617F2A43313A51BB233EF3C167ECBF4A6E24C7683EAD69E4F7D20144FF1D62D7DA706C2EF2D3BC3653216FC41710BBF09A5714B1B65BB6AECF64B41E870C07A15A50AED307680601819F13FDCBA776AF0535D681E482ECG
			486D96EB7F6A742BCC27F17459G813670A502DE26ABF16E7004EB7E3CFD3D8BE318FE43B48DBF2EAF707F63C2DD1BC610A2996E99975A98DC39C3A69B4A713FF1215D6B6AA0C5333B330B1E1BD2102B22DB5C279913625E6D69967765D72E22BEA6211D841082108A10A97B3FC57BB7C03B388776G2087E03CC7741F395A43674FF7BED2E246E472F6AA271D783E8DD19A57F2F7EE0BE997D84EE6DE02B9CC07851D0977F1E7677C7B78D2A70DCFC17F1E09204AF0251DB4C69A9C8C2B52DE8EF696C91CA35CE1
			04B534D066E37DE19C2FAB93C68446671E41BD3B7D0A387712C067A9G99G6B81B0666923E3FEB1A946B84F4C56A3DB7B5797D03EE0C0F7A9DE4D280B003D85423C6F1E7DF84EDA5C83A148ED50779EE578AFD39B6AB7839DF0FECC065BFFE0BB4355239AF439FE5B05ED8C5F2C6AC11E276D180A3D331167954063C3GEC0C5FE19D13FE060DA1E7415EG961C25795E9E6B9DF778AC9B234B7ECF9F0B571FA67BB3F6446B4F04A773B31F3DA2EA6B9BGD7F6CE44CAC77B3036A76F27F2E54C2C2CEC8AE95EBA
			4567243B446E28E72E48B86B1C38238ECE621AC950CE81C8BBA7726F71BF0A7BA179E3F713F5BF0B7FBEA04B73C6CE56B93617393D51FBF341620CDCC6F377D5F1AF56740A3507C7445A7BBEF58F9D1F682E9F67EBAE352CCD6FAC3AF65DD4BEBFCB193DC21E4FFBD11E6273F08F81584F8B9D799ADEA6F16F7028B549F56FF708F5FB4F0BF51FBACD6F84E8A783244AF5CBF79DA75719DFB2236634D276085CB2733C504F55C66B3B18071EFFA3A7515DDCFC01692562C2D4BFC7246E579E93F218504FF04F9708
			7DC83EA0FC6383D7EE2CA67F688D4E5BFC5D73587DFB9735553597587D1BAD7177D17C868B1E1D17BFCD0A7332997A7AAE0A786B2DEEBC4B29504E825884D0548771990033CF5C2F8FF6E25DE54EBA384788BA41916B1692AD136FF7D72D7275411AE65F5C778E636B4E9E64EB910CDD8E1086108530914026883EF23B505E924509AE4320398826A50A86D6C74300100299B1632DE94A4ADBA92CDB21C4F9EBD69245C42F2B284BDE056938D0627FCA718BACF8267BD5749CE2FBB97495758BDB7B24C3501F845C
			9D7DB369AD6FCFC45F674275D67547568FC6A9BE2DBF36FE702B5322FE1081FDD583525677445A5EB268DBB940682641B3F3E0A68FECEFFEFD06469750E773D65B8EGD48174FA453EF4F44D7BDE2CE1FC0E77E2CF9E0BF7A6A63D0C17BB60395CBB13AFB62F38A05E3CD5FC6C9A6FECFD32F99F19A472778A4B6F2313B72396BE13F64BCD2541762326BBF11C54549D3FD65E5A9D3FD66E6E1E3BD6FE2EBBF12D3C3F5BDAABE7716DD1FAA7E6415E0C648A5C1B943789F07F14A3563D3BA7318C77764C9F6BFD32
			A7FE4C1E52B3BBE6FF20A7FE4C7E251E59B17B85FA6F962B900328A2E6DFB9A562789392D7CEC49C5F2B0A73F7C4C59F178C6DD400ACGB28879B1C0452098D3CD47B4C3FB88E091408A00B400F5F24C435EBE5421E53E5586AAF8DB50797F596BF7A9AD7C0DBAAA0899D3481F68EB92456E5782CCE70F5058A35F87710BCF6871F7AD082B01761044C5DA04EEA7FDC2A7FE9756BE56487698ED2F136DE7CF7FAD4F9D52192D2B3D2E3FA50D58DC0A493E0646CC3621EADB691050C751DE44D5C05BAEF123FB050E
			BAA42E1346C44B201DBAA4FC771633094F590E3371FDC55159783E22646C5C6FEE771FCD64CB98FF643810A3DD722C9C97F2E4C95CADBB049CC7B4017BD30118DBA3F10B8B44DC27A6E2D6650FF8B7725AC33766B50778B7BA6305D1FC330A3E00083CA9B205B9DF3F9122422C55685AD3E8DFCB61B74B813FAF732832E116835640538300F97932E19C53C06C736C7796EECB7854E14E82005D3C3DE4185D734FBC7B6C330C2F51E914A50449E2BD289C42EC06FF2B31C8603B0D5088F848E87EFA7148CE5187F7
			7100015386A00A923276159608FE3B2A68A0B0476FA9947886FFD0B7A6D4D56A67BDC772C5DF2D2E33FCAE22DA7339F5DC8F45A3A25FAB990E57DFA67BC37EF87DD532FF4A9D2F3FC376BFB69D2F5FAB7BD5922FFFFCC45429B39C78AD5EA5B5E4EA83903650C429CE89F5983AB6B0416B6ACFD1FF05355C45F25E51960C19060C10F6996350D9B3C5CD3E157A301368CBC604FDACEE194FBE72AC76D124C3AC26C76D63847C3E850C7265EC31AF6F5A0FFBE6766B0E40A5DE39A7621B3E95AEEBCEEF57D96DE8A1
			BC13F4A1CB038FF97487D4886A3481467753547F5C8B10A2473D5FC76B4E60A858FB04FB4E91BC1FD426C5FB6713C94803309A56DC4DE5C2DBCF57191F7A15347947B43C7B83C02B51980804714E0728B3E884D42C773133B9F58C794902390C1FA7FA237C0C9F1F0B1F3CF978594CE5C6FE982DCF6E206F3A7C0256568BD1DA9FAD180B5636F9E815421A2F4C107D3E29CAAA7BD829875F5BF86B28C7AAABC5CC03B9B0A4B1078E3F5A90B0B5F322542D729761152F169E92756F132A753D4011AB39B9DBC80277
			8DFEF64FD579236FD1D80E11B7AD72CB9D7A167BC50E5DBAAD6AAC19FED1BF38C247968760DE87308718DC786EBA1F08FF9B2A636118CE9F3E08AD467B8FED567A097DBB1B337F1000775EG0F0382517AC0C65677F2193FAF295BCF6256C4AC35B7D193596993F50A3581A96730FCAFEE885CA77C82671138875C8237D862F2084025CA5CD2095BEC08F3352325024CFCD7E6295814F7B538BC0E7ADA67C5F73D2B5D692C2BF5DD2CF5DFECEBE830B75859FCEF4B1BF34CEFEEF7B94E3637F9622FC048756DD119
			C20878A6646311E364BA6BC26C338A2E6FACDAAB473824A2A8EC68DFCAE82D8E5AA9GE9C121A35BDEFC3E5671DA9CFE45FB4DD37D04881F3864985A02CFB5E5F9346C1098F76BC0F45C6A9DB6B98E4B25E50758180FF9F18C4B0B5DAA75B3C191CBB9AE60FB09C47509FA480759DB8E91AF96CC5FD88D69459B1C673A6EF92C4E708BFAFEB786E3EBCF1BA81E0C46561EEE113E4186FD41D1D1FBDAB8087EB09D5A9BG0AC3608FGEAGFAGF40085GCBG1281D281B2G568310B02C8BF0G60C4D8462EFD07
			6650B7AF82DB2AC0C28FFBFC31FAF0421AFDE17E4E981E0B427162FB57F06C3C25B0AE050FCD05E7C65CF91F71474EDB8F6308493FC506E71999EF5E036E58F9D5B02E030F7542F3BC6E3C578F464EDB8C63167131AB611996F75E3DA4F6DEA60C5B404766C6A0570C441B77DFB46FB99CC91CEB56C01F87E088E0F20EF1F7C05F7218FEE6EB15FBE88DAC92EB0375941F9A093541B5A542869F073E0ACB42E63AD50C6543B34E615353C44A72DB4B6F5B27EFFEBDE859CD2541E4CE9ACC1C63FDE4F07EDA4A0A41
			7835144F8C4E2E25FCE1B0FEAD659F872D35947E7D5AA05E97BEB827BDF25C2392F7C66252681A0823891B44FF0D7205F81130B1FCBA65EB0C62FAB86E3B965C990E7B0FC1348590F988739F7EBEF890FDBA044ABA7F060A0E5182E1550C3E8F26BCEAC1454277FFCB9C724DEBC0BE112BA7DD92B96918CF64294992176993F96AAA09DB29099CB7FFCC6034E1315EDE097B7430D86F3044BDB7A256BBA1F1AF7A45FA0BA46E4BFE491F44ED8FC87EA4EEB8A079133803EE495F3840FD43AD7913385D9E491F44DD
			72C87EA4AEB4AD7913385A0312BF093B6D2064CF62FEF4D072A7F1B6A2791B9038D5C472A7F11B0964CF624EE7CB7EA42E58A1791338EDD45EA4E8A7CB5C3B29EC0FC3FB0344FD1362F2AFD75B76DD9638819B7D269C5A9D9217C2F15E4B4C17F11D5A84AF0B6538C00960A5653228EF556C097AAD0765773F175CFC9F76531BCA03651D3D740C5C8E307A324835AE21AB603956D33EEB292D88F89C56C0903956DA780DF9967DCD733A9FE96887B6G1E517FGBDE71F85A83AA2686F9F0E52BFB8B2DFDEB9977D
			D2D8731999746B7CC87F7095C17F67742C7E9440A969BBC72274EF894C17EB4EC53F77CA2C7CEDEE24BFA4697FAF357577GAC12748DBDCA7FFD1E3968EF1F07FE52AC7A77CCA37D65127EB0BD636F85C81574BF600F527F4D413968974CC3BFFD167E5FA0B4361774FFC87D442DC8F0D250FFA09025EFE64FC53FF09E7AC51331721FF4A07DFD13A24F596F665FF5526FB9D84D718D372861D441B88F40920095GD877E673BE6C411AEBF848986B6235AC2796402B1D1F2BFF7008FCFD352475930F38EF6B8D9D
			2FC6BEB607FEBF138E633264584B36DDFC7DE6D973E294581CA5B6313E6D2A1873955B6F6029C4CC239E7F26C985AA91FFA05C98B27C2DCCC0763D6BBA1B102F9466D6GF8GCC00A5GCCCF9FAAC1BD2D14EB07FDB49F2C1FAB9F5C07FFBA45733C4D9A56C3A14F1BD1A6431C1075FFE2182E577D9A1FDBBD9A1D5B631FFBEE9E2D393E9691F37BC35139DE775CF3DF705039F6B17736F0F46E6726671EFB4BC1BA37C14C7D02991DFB0A4CBDF793FD3ED6AB66FEBB921D6BF6A41A4BE26E73EE715E79F1580F7C
			A9D1F37F6BCE2CE3DC4F5F3FFADA59A7467C1B943730211AA9D18F78A625B50EED0095D3E27F3F66C11A030A9E0EFE576C9F376C992DBDE145CCFCAED4CE0D3C6C689B478FF3B84447FE4AE6DD0B7172DC21BCAFD3627B61D53BE33F9F3EB35B7A7D6FCFF647FEBF0C73237D7BCAE23F9F1EB93F2DA4767BE1B11F71796912C2A2EA9574B3B05C92FEB78F4C4AA1E3E153D43C1CEEF45EF97BE35A7C9B07F96745824B631ABC34C6B89D1B5FF5D0FC69F4ECFE77DD37486F8EC35F9D53A28EDA387BFA693FCC7538
			FA97FB7F23EB7885897DEBCCA9D32B07485FDD165FBFF05F7C58EA53CD25417E3F015B4F5D19C3C8724FC81EC97EAC3F75AF7DF3A9FE1CDC9AC57083DF0A7F5879B4C99EB5B813ECE545B41364E235AC9CA24F4B73652049D356256315DB1E7868169C126BA0DBD9B269A0391891BAC89ECD989DE48BA41363681DB131FB1E436F4323E40ADE47DBB95B78FA2BC0E2B1817BA0GBA27EDB3303B04AAE7E0F14D0791EB12ED7C3D19C98AACEF4ACCD2B8635D98C8475E0519A4DF3C7CB24916DADD176B628145A297
			9A652E9958421B08FD11E2C36439193EAC7B7EBD37967D5D71C88E49EFA4DB41FF85079450B17DE3A6A7A968274A466F23C06EA2A74E7D8B4C7D8B1D7B3613648C04CD72D294BF00F8566B8EAA83AA8C596ED5C2A3F485E4E94FB932FDB0646B2784CC12F32E719C1025AD6CFE51C9F618C3743BACF59CCCE5CFF4698E2C4B710E37694613E76F3A6B2EA07DC7C44D7F503475FD45265BDECA78F79FB537B2DF6639B576EF423C9750EF5F817864DBC5ADE865F97CFFA492FDD76F76AB3A8E514CC8ED400B5F5857
			CD18EA74DD37F681578F0BB13C137FBD718564FB58B2677FGD0CB878808E57A61A798GGB4C4GGD0CB818294G94G88G88G90F5D4B008E57A61A798GGB4C4GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGE198GGGG
		**end of data**/
	}

	/**
	 * Return the JDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getfrmViewInventoryProfileINV030ContentPane()
	{
		if (ivjfrmViewInventoryProfileINV030ContentPane == null)
		{
			try
			{
				ivjfrmViewInventoryProfileINV030ContentPane =
					new javax.swing.JPanel();
				ivjfrmViewInventoryProfileINV030ContentPane.setName(
					"frmViewInventoryProfileINV030ContentPane");
				ivjfrmViewInventoryProfileINV030ContentPane.setLayout(
					null);

				ivjfrmViewInventoryProfileINV030ContentPane.add(
					getpnlRadioButtons(),
					null);
				ivjfrmViewInventoryProfileINV030ContentPane.add(
					getJScrollPane1(),
					null);
				ivjfrmViewInventoryProfileINV030ContentPane.add(
					getbtnPrint(),
					null);
				ivjfrmViewInventoryProfileINV030ContentPane.add(
					getbtnCancel(),
					null);
				ivjfrmViewInventoryProfileINV030ContentPane.add(
					getbtnHelp(),
					null);
				getbtnPrint().addActionListener(this);
				getbtnCancel().addActionListener(this);
				getbtnHelp().addActionListener(this);

				getbtnPrint().addKeyListener(this);
				getbtnCancel().addKeyListener(this);
				getbtnHelp().addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjfrmViewInventoryProfileINV030ContentPane;
	}

	/**
	 * Set the Inventory Profile Type based on the radio button 
	 * selected
	 */
	private void getInvProfile()
	{
		if (getradioCentral().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_C);
		}
		else if (getradioWorkstation().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_W);
		}
		else if (getradioSubcontractor().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_S);
		}
		else if (getradioDealer().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_D);
		}

		else if (getradioEmployee().isSelected())
		{
			caIPData.setEntity(InventoryConstant.CHAR_E);
		}
		else if (getradioAll().isSelected())
		{
			caIPData.setEntity(null);
		}

		getController().processData(
			InventoryConstant.GET_INVENTORY_PROFILE,
			caIPData);
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(gettblInvProfile());
				// user code begin {1}
				ivjJScrollPane1.setBounds(14, 77, 590, 173);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the pnlRadioButtons property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlRadioButtons()
	{
		if (ivjpnlRadioButtons == null)
		{
			try
			{
				ivjpnlRadioButtons = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints4 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints5 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints6 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints5.insets =
					new java.awt.Insets(8, 8, 9, 8);
				consGridBagConstraints5.ipadx = 3;
				consGridBagConstraints5.gridy = 0;
				consGridBagConstraints5.gridx = 4;
				consGridBagConstraints2.insets =
					new java.awt.Insets(8, 9, 9, 8);
				consGridBagConstraints2.ipadx = 3;
				consGridBagConstraints2.gridy = 0;
				consGridBagConstraints2.gridx = 1;
				consGridBagConstraints6.insets =
					new java.awt.Insets(8, 9, 9, 12);
				consGridBagConstraints6.ipadx = 4;
				consGridBagConstraints6.gridy = 0;
				consGridBagConstraints6.gridx = 5;
				consGridBagConstraints4.insets =
					new java.awt.Insets(8, 8, 9, 7);
				consGridBagConstraints4.ipadx = 6;
				consGridBagConstraints4.gridy = 0;
				consGridBagConstraints4.gridx = 3;
				consGridBagConstraints1.insets =
					new java.awt.Insets(8, 21, 9, 8);
				consGridBagConstraints1.ipadx = 4;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				consGridBagConstraints3.insets =
					new java.awt.Insets(8, 9, 9, 8);
				consGridBagConstraints3.ipadx = 5;
				consGridBagConstraints3.gridy = 0;
				consGridBagConstraints3.gridx = 2;
				ivjpnlRadioButtons.setName("pnlRadioButtons");
				ivjpnlRadioButtons.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_ENTITY_SELECTION_COLON));
				ivjpnlRadioButtons.setLayout(
					new java.awt.GridBagLayout());

				ivjpnlRadioButtons.add(
					getradioCentral(),
					consGridBagConstraints1);
				ivjpnlRadioButtons.add(
					getradioWorkstation(),
					consGridBagConstraints2);
				ivjpnlRadioButtons.add(
					getradioSubcontractor(),
					consGridBagConstraints3);
				ivjpnlRadioButtons.add(
					getradioDealer(),
					consGridBagConstraints4);
				ivjpnlRadioButtons.add(
					getradioEmployee(),
					consGridBagConstraints5);
				ivjpnlRadioButtons.add(
					getradioAll(),
					consGridBagConstraints6);
				ivjpnlRadioButtons.setBounds(13, 17, 590, 48);
				// defect 7890
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioCentral());
				laRadioGrp.add(getradioWorkstation());
				laRadioGrp.add(getradioSubcontractor());
				laRadioGrp.add(getradioDealer());
				laRadioGrp.add(getradioEmployee());
				laRadioGrp.add(getradioAll());
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjpnlRadioButtons;
	}

	/**
	 * Return the radioAll property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioAll()
	{
		if (ivjradioAll == null)
		{
			try
			{
				ivjradioAll = new JRadioButton();
				ivjradioAll.setName("radioAll");
				ivjradioAll.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjradioAll.setText(InventoryConstant.TXT_ALL);
				//ivjradioAll.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioAll.addActionListener(this);
				// defect 7890
				//ivjradioAll.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioAll;
	}

	/**
	 * Return the radioCentral property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioCentral()
	{
		if (ivjradioCentral == null)
		{
			try
			{
				ivjradioCentral = new JRadioButton();
				ivjradioCentral.setName("radioCentral");
				ivjradioCentral.setMnemonic(java.awt.event.KeyEvent.VK_C);
				ivjradioCentral.setText(InventoryConstant.TXT_CENTRAL);
				//ivjradioCentral.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioCentral.addActionListener(this);
				// defect 7890
				//ivjradioCentral.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioCentral;
	}

	/**
	 * Return the radioDealer property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioDealer()
	{
		if (ivjradioDealer == null)
		{
			try
			{
				ivjradioDealer = new JRadioButton();
				ivjradioDealer.setName("radioDealer");
				ivjradioDealer.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjradioDealer.setText(InventoryConstant.TXT_DEALER);
				//ivjradioDealer.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioDealer.addActionListener(this);
				// defect 7890
				//ivjradioDealer.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioDealer;
	}

	/**
	 * Return the radioEmployee property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioEmployee()
	{
		if (ivjradioEmployee == null)
		{
			try
			{
				ivjradioEmployee = new JRadioButton();
				ivjradioEmployee.setName("radioEmployee");
				ivjradioEmployee.setMnemonic(java.awt.event.KeyEvent.VK_E);
				ivjradioEmployee.setText(
					InventoryConstant.TXT_EMPLOYEE);
				//ivjradioEmployee.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioEmployee.addActionListener(this);
				// defect 7890
				//ivjradioEmployee.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioEmployee;
	}

	/**
	 * Return the radioSubcontractor property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSubcontractor()
	{
		if (ivjradioSubcontractor == null)
		{
			try
			{
				ivjradioSubcontractor = new JRadioButton();
				ivjradioSubcontractor.setName("radioSubcontractor");
				ivjradioSubcontractor.setMnemonic(java.awt.event.KeyEvent.VK_U);
				ivjradioSubcontractor.setText(
					InventoryConstant.TXT_SUBCONTRACTOR);
				//ivjradioSubcontractor.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioSubcontractor.addActionListener(this);
				// defect 7890
				//ivjradioSubcontractor.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioSubcontractor;
	}

	/**
	 * Return the radioWorkstation property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioWorkstation()
	{
		if (ivjradioWorkstation == null)
		{
			try
			{
				ivjradioWorkstation = new JRadioButton();
				ivjradioWorkstation.setName("radioWorkstation");
				ivjradioWorkstation.setMnemonic(java.awt.event.KeyEvent.VK_W);
				ivjradioWorkstation.setText(
					InventoryConstant.TXT_WORKSTATION);
				//ivjradioWorkstation.setNextFocusableComponent(
				//	gettblInvProfile());
				// user code begin {1}
				ivjradioWorkstation.addActionListener(this);
				// defect 7890
				//ivjradioWorkstation.addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioWorkstation;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblInvProfile()
	{
		if (ivjtblInvProfile == null)
		{
			try
			{
				ivjtblInvProfile = new RTSTable();
				ivjtblInvProfile.setName("tblInvProfile");
				getJScrollPane1().setColumnHeaderView(
					ivjtblInvProfile.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblInvProfile.setModel(new TMINV030());
				ivjtblInvProfile.setShowVerticalLines(false);
				ivjtblInvProfile.setGridColor(
					new java.awt.Color(153, 153, 153));
				ivjtblInvProfile.setShowHorizontalLines(false);
				ivjtblInvProfile.setAutoCreateColumnsFromModel(false);
				ivjtblInvProfile.setBounds(0, 0, 200, 200);
				//ivjtblInvProfile.setNextFocusableComponent(
				//	getbtnPrint());
				// user code begin {1}
				caTableModel = (TMINV030) ivjtblInvProfile.getModel();

				TableColumn laTCa =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(0));
				laTCa.setPreferredWidth(70);
				TableColumn laTCb =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(1));
				laTCb.setPreferredWidth(200);

				TableColumn laTCc =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(2));
				laTCc.setPreferredWidth(30);

				TableColumn laTCd =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(3));
				laTCd.setPreferredWidth(50);

				TableColumn laTCe =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(4));
				laTCe.setPreferredWidth(50);

				TableColumn laTCf =
					ivjtblInvProfile.getColumn(
						ivjtblInvProfile.getColumnName(5));
				laTCf.setPreferredWidth(50);

				ivjtblInvProfile.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblInvProfile.init();

				laTCa.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(RTSTable.LEFT));
				laTCb.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(RTSTable.LEFT));
				laTCc.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(
						RTSTable.CENTER));
				laTCd.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(
						RTSTable.RIGHT));
				laTCe.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(
						RTSTable.RIGHT));
				laTCf.setCellRenderer(
					ivjtblInvProfile.setColumnAlignment(
						RTSTable.CENTER));
				//ivjtblInvProfile.setNextFocusableComponent(
				//	getbtnPrint());
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblInvProfile;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		///* Uncomment the following lines to print uncaught exceptions
		// * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
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
			// defect 7890
			//carrRadioButton[0] = getradioCentral();
			//carrRadioButton[1] = getradioWorkstation();
			//carrRadioButton[2] = getradioSubcontractor();
			//carrRadioButton[3] = getradioDealer();
			//carrRadioButton[4] = getradioEmployee();
			//carrRadioButton[5] = getradioAll();
			// end defect 7890
			// user code end
			setName(ScreenConstant.INV030_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(628, 339);
			setModal(true);
			setTitle(ScreenConstant.INV030_FRAME_TITLE);
			setContentPane(
				getfrmViewInventoryProfileINV030ContentPane());
		}
		catch (java.lang.Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Handles KeyPressed events for this frame.
	 *
	 * @param aaKE KeyEvent  
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
// defect 7890
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			for (int i = 0; i < 6; i++)
//			{
//				if (carrRadioButton[i].hasFocus())
//				{
//					ciSelctdRadioButton = i;
//					break;
//				}
//			}
//
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (ciSelctdRadioButton == 0)
//				{
//					ciSelctdRadioButton = 5;
//				}
//				else
//				{
//					ciSelctdRadioButton = ciSelctdRadioButton - 1;
//				}
//			}
//			else if (
//				aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (ciSelctdRadioButton == 5)
//				{
//					ciSelctdRadioButton = 0;
//				}
//				else
//				{
//					ciSelctdRadioButton = ciSelctdRadioButton + 1;
//				}
//			}
//			//carrRadioButton[ciSelctdRadioButton].setSelected(true);
//			carrRadioButton[ciSelctdRadioButton].requestFocus();
//			getInvProfile();
//		}
//		else 
// end defect 7890
		if (aaKE.getSource() instanceof RTSButton)
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				if (getbtnPrint().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnCancel().hasFocus())
				{
					getbtnHelp().requestFocus();
				}
				else if (getbtnHelp().hasFocus())
				{
					getbtnPrint().requestFocus();
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnPrint().hasFocus())
				{
					getbtnHelp().requestFocus();
				}
				else if (getbtnHelp().hasFocus())
				{
					getbtnCancel().requestFocus();
				}
				else if (getbtnCancel().hasFocus())
				{
					getbtnPrint().requestFocus();
				}
				aaKE.consume();
			}
		}
	}

	/**
	 * main entrypoint - starts the part when it is run as an
	 * application
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmViewInventoryProfileINV030 laFrmViewInventoryProfileINV030;
			laFrmViewInventoryProfileINV030 =
				new FrmViewInventoryProfileINV030();
			laFrmViewInventoryProfileINV030.setModal(true);
			laFrmViewInventoryProfileINV030
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmViewInventoryProfileINV030.show();
			java.awt.Insets insets =
				laFrmViewInventoryProfileINV030.getInsets();
			laFrmViewInventoryProfileINV030.setSize(
				laFrmViewInventoryProfileINV030.getWidth()
					+ insets.left
					+ insets.right,
				laFrmViewInventoryProfileINV030.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmViewInventoryProfileINV030.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (cbInit)
		{
			Vector lvDataIn = (Vector) aaData;
			csEntity = (String) lvDataIn.get(CommonConstant.ELEMENT_0);

			// defect 8494
			// Moved from windowOpened
			// This code is required to set the focus on the correct radio
			// button when entering the frame from INV016.
			if (csEntity.equals(InventoryConstant.CHAR_C))
			{
				getradioCentral().setSelected(true);
				setDefaultFocusField(getradioCentral());
			}
			else if (csEntity.equals(InventoryConstant.CHAR_W))
			{
				getradioWorkstation().setSelected(true);
				setDefaultFocusField(getradioWorkstation());
			}
			else if (csEntity.equals(InventoryConstant.CHAR_S))
			{
				getradioSubcontractor().setSelected(true);
				setDefaultFocusField(getradioSubcontractor());
			}
			else if (csEntity.equals(InventoryConstant.CHAR_D))
			{
				getradioDealer().setSelected(true);
				setDefaultFocusField(getradioDealer());
			}
			else if (csEntity.equals(InventoryConstant.CHAR_E))
			{
				getradioEmployee().setSelected(true);
				setDefaultFocusField(getradioEmployee());
			}
			// end defect 8494

			caIPData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			caIPData.setSubstaId(SystemProperty.getSubStationId());
			caIPData.setEntity(InventoryConstant.CHAR_C);
			caIPData.setId(null);
			caIPData.setItmCd(null);
			caIPData.setInvItmYr(Integer.MIN_VALUE);
			cbInit = false;
			addItmCdDesc(
				(Vector) lvDataIn.get(CommonConstant.ELEMENT_1));
			UtilityMethods.sort(cvIPVct);
			caTableModel.add(cvIPVct);
			if (gettblInvProfile().getRowCount()
				> CommonConstant.ELEMENT_0)
			{
				gettblInvProfile().setRowSelectionInterval(0, 0);
				setDefaultFocusField(gettblInvProfile());
			}
			return;
		}
		else
		{
			addItmCdDesc((Vector) aaData);
			UtilityMethods.sort(cvIPVct);
			caTableModel.add(cvIPVct);
			if (gettblInvProfile().getRowCount()
				> CommonConstant.ELEMENT_0)
			{
				gettblInvProfile().setRowSelectionInterval(0, 0);
				gettblInvProfile().requestFocus();
			}
		}
	}

// defect 8494
// Moved to setData.
//	/** 
//	 * Handle Window Opened Events.
//	 * Sets the focus on the appropriate radio button.
//	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
//	 * 
//	 * @param aaWE WindowEvent
//	 */
//	public void windowOpened(WindowEvent aaWE)
//	{
//		super.windowOpened(aaWE);
//
//		// This code is required to set the focus on the correct radio
//		// button when entering the frame from INV016.
//		if (csEntity.equals(InventoryConstant.CHAR_C))
//		{
//			getradioCentral().setSelected(true);
//			getradioCentral().requestFocus();
//		}
//		else if (csEntity.equals(InventoryConstant.CHAR_W))
//		{
//			getradioWorkstation().setSelected(true);
//			getradioWorkstation().requestFocus();
//		}
//		else if (csEntity.equals(InventoryConstant.CHAR_S))
//		{
//			getradioSubcontractor().setSelected(true);
//			getradioSubcontractor().requestFocus();
//		}
//		else if (csEntity.equals(InventoryConstant.CHAR_D))
//		{
//			getradioDealer().setSelected(true);
//			getradioDealer().requestFocus();
//		}
//		else if (csEntity.equals(InventoryConstant.CHAR_E))
//		{
//			getradioEmployee().setSelected(true);
//			getradioEmployee().requestFocus();
//		}
//	}
// end defect 8494
}
