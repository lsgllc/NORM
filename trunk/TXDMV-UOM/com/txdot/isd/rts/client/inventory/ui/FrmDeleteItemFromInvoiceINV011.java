package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.MFInventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmDeleteItemFromInvoiceINV011.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Cleanup
 *							organize imports, format source,
 *							rename fields
 *							add setVisibleRTS()
 *							delete setVisible()
 *							modify handleException()
 *							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3 
 * Min Wang		08/01/2005  Remove item code from screen.
 * 							add TXT_ITEM, TXT_ITEM_DESC
 * 							modify getlblItmCdDesc(), 
 * 							getstcLblItmCdDesc(), setData()	
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	08/08/2005  Cleanup pass.
 * 							Remove Key action items.
 * 							work on constants.
 * 							Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV011 allows for items to be deleted from an invoice in the 
 * Receive Invoice event.
 *
 * @version	5.2.3		08/13/2005
 * @author	Charlie Walker
 * <br>Creation Date:	06/27/2001 15:30:13
 */

public class FrmDeleteItemFromInvoiceINV011
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjlblEndNo = null;
	private JLabel ivjstcLblEndNo = null;
	private JPanel ivjFrmDeleteItemFromInvoiceINV011ContentPane1 = null;
	private JLabel ivjlblBegNo = null;
	private JLabel ivjlblItmCdDesc = null;
	private JLabel ivjlblQty = null;
	private JLabel ivjlblYr = null;
	private JLabel ivjstcLblBegNo = null;
	private JLabel ivjstcLblItmCdDesc = null;
	private JLabel ivjstcLblQty = null;
	private JLabel ivjstcLblYr = null;

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private MFInventoryAllocationData caMFInvAlloctnData =
		new MFInventoryAllocationData();

	/**
	 * Vector used to store the Substations for the combo box
	 */
	private Vector cvSubstaData = new Vector();

	/**
	 * Flag to check if exception thrown in the server business layer
	 */
	public boolean cbExThrown = false;

	/**
	 * FrmDeleteItemFromInvoiceINV011 constructor comment.
	 */
	public FrmDeleteItemFromInvoiceINV011()
	{
		super();
		initialize();
	}

	/**
	 * FrmDeleteItemFromInvoiceINV011 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmDeleteItemFromInvoiceINV011(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmDeleteItemFromInvoiceINV011 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDeleteItemFromInvoiceINV011(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// Determines what actions to take when Enter, Cancel, or 
			// Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Reset the exception throw flag to false.
				cbExThrown = false;
				caMFInvAlloctnData.setCalcInv(false);
				caMFInvAlloctnData.setTransCd(InventoryConstant.DEL);

				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
				if (!cbExThrown)
				{
					dispConfirmationBox();
				}

			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caMFInvAlloctnData);

			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV011);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Display Confirmation dialog.
	 */
	private void dispConfirmationBox()
	{
		String lsMsgType = new String();
		String lsMsg = new String();
		int liYesNo = 0;

		// Prompts the user if these are the correct values.
		lsMsgType = RTSException.CTL001;
		lsMsg = InventoryConstant.TXT_CORRECTVALUES_QUESTION;
		RTSException leRTSExMsg =
			new RTSException(lsMsgType, lsMsg, null);

		// Set the position of the confirmation box so it doesn't 
		// cover the field entries
		int liFrmVertPos = (int) this.getLocation().getY();
		leRTSExMsg.setMsgLoc(
			RTSException.CENTER_HORIZONTAL,
			liFrmVertPos - 200);

		liYesNo = leRTSExMsg.displayError(this);

		if (liYesNo == RTSException.YES)
		{
			caMFInvAlloctnData.setCalcInv(true);
			caMFInvAlloctnData.setTransCd(InventoryConstant.DEL);

			Vector lvDataOut = new Vector();
			lvDataOut.addElement(cvSubstaData);
			lvDataOut.addElement(caMFInvAlloctnData);

			getController().processData(
				VCDeleteItemFromInvoiceINV011.YES,
				lvDataOut);
		}
		else if (liYesNo == RTSException.NO)
		{
			caMFInvAlloctnData.setCalcInv(false);
			return;
		}
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
			D0CB838494G88G88G0931A3ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DB8DD4D4C71656934DF0B6EE561DF14EB83B1C9CA7135DE1B76C0C904CE1934FAE330719B109B3F1F7CDE2A2B1E8542092C3949593921FAD88367C34CAC61238C90F1AC4A3A1607F32A481E99494897FC2EC931B6E8734B65DCD77833A8D605E2AFA375E6B66F5236C2C67DC6B55F76B5EDBF5DF552DDB759A216843B9595BF5C50230BDDB107F1D59AE88C9DF884214ED0F44A39675DC0CDED07D7B93
			509EE12DB98B4AF860C597456A8DC2736EDC28AF047A0C3D317A7D703CCF683A94D7818FA16C4CG5E0AF577EF8DEF67399A3413C36BD956BCA88B009681168ED435A4866C9F30964BF8B1548B64E7C25BF50210C65BBCB9D62C4227496D1264F264F952660C75004CBF8BF5522689CA5B730A4EDD34F753AB4E4AF573DFAF95521F3C294BA736D47A1192E5599439B48A9B29A3F6889FD8AFC2D959F43377CB2B2B3FD47259DCD2295DEBAB75C85ED22B43AEBA216A9C02426571178E5AABAA4EFB7ACD22C31444
			FAC96CBF6FF17557BB07DCF62BD85F58D1D6DEAE881DAF10B1240D65D27DE08B6A2C3F7F1EDFA977E7564D254234CFEF9AC275B77285564F0497147ED635CFB53817925D57297F091CG7533C04CE61495ED4681F6B209E0BB46C85BD428E7C17DC09276650BCF30AF1D5F2F95B23A6F579506680BAF0975E1CE1252172C292E2CF769DC305612771984BC614598FD9150D920CEA09F507BF48EFEE07A944A6AEE2946555F6FF2D63B5D679CB64FE517CDF440F3C585F0E42CC5343ABCB6C1E073EC53D809C6FF10
			E63E98B84714F9F1C540F9915D32DC186BF09832B54636244EE1F8C856917F22B2B6B62EED67493816G1E8E1437ACC6FF984884E4DB06636AB31D18E8DC75CEC974B8AD0E5641EE3745A31D9B34BBEC22C79EDF0FB55C78340849C9F7AD477C92F5C26D1724C77ECA1723611724A8C9B7E59973476CE50ADF186DFF8AE9BBFE597877C175ADA373F0184E4305703CDD1E238C1FCAF163CB8A4E6678AE0A13FA9970E63D04733633G475010750CE0A81F29B906597FB6DD1771926B0FA1B9785DAE2DA371259270
			CEA0B7D084D094D0A25012E4FC3F0F18BE3F43F95B24FA37CC56D17BC7A8C51F643154BB3D1245E995BBAC9E3B255BA1FA85394DE0AD69FBF7005CAD3A1E2CE063026894BD96496EF202F6CBBF849C0F17353927F68FE96391A9374D5EAF0201FE3700FA6F2B5D87254362155A5DB60BA4AAAD987FC7354417C3168E50888A607948F262733C5BA4FEE440780B204EFC15FB11E08E28CFDB0EFEA9B83F974ACBB031C5C7C50549EEA55D30F87CE8BF778209A5A2891AA651ADBAED706015F90597F6A9FE69F171BE
			3FC3F132C6EA2063DE0E3FFFE18F43C1075A4B48BFC47996C972583B87A531D514A43B33D711BFC67B628DD8FEDEC1DE6397499C1B8D631A8B14404797F7E11FEAFC8DAE574DC1F79BF9F3321F92E91CDD8C4F294BF1CF9C9DA3FB24E1854E53751DB8CFCD7949C249B377EB4E5344C60721F2851B1FEDAB02575A8F28CE45C661D867DD2E7BD0E4D8B1FE2DB29BC77EECB6F6E8E04C6F1E866277916843EC207895687757AE9859FABBE7710A38423E76409B93ED4AACE3EDD755051EE329F55AF3ECC31D769C4B
			288BBF0704FA2DB944F8B975E4EFF18DBBD5F3B22FFEAFDD43CE27C8BB4DC58416F32CEFA33E30BB6D52953B2DD714DA6D37C4D8F7AB485CBAC45760A2F8DE49675270A11CCB07EFA589895F59F5DA7B596C3F0B50D53E4C5EEB534B4ADCE2FD4A27F39D7216FEF0D40DEB50A9B1FC9FDD5FB2AE47176CCB5AEDF7DFA273712668175B95DE5AA910B8629894795866DAF6939FFB6C7D702A480215733E25D6F55E67067E119CB18E4A85C0CCF6DB7987D0CE14DF31B97470E55247E410CD874ADB09F3683ECB6F2A
			562EBCF9EADC8E87FB957C9D799A8C641D0A8303A204E63238AF71F15DEE589B446B10474878BFE8A0E3F42862426115E40C9537B17FA27B86431AA9168D0B2BBF8B77614B86F55EEA73CE55FC1F047AD64AF9714A60BDAC3911680D84BC1A0FDDF7D99D372A3DDE1755CE3788795D6C4CBAADCC146B7652DD47C1B35D1636D6135D62F0751EF379B02F2D355171GA52C449C3333073C0B8CB25ED5B17AA32B30BFB117474F2DC7AF1383C60F5D63055C48A67A502773AF935F10BDCB06B117BB2E761550231D4B
			912ADAA54751D5413E7A380974438D78B45E37B30DFBD43E023C933668DEF813581FD50DBB58DC176BEB9A33F12E8B98ABAF5A4265E133D7854658E828331CE7A175D7422A60DC68D90AAFDA951C8BFD5B0339D0B270CC2BF1EFBE50CC469585757855B82E312F73D563EAF2591D929F5331A632DEDD9EFB2F5D09D89945A06B26810C6DEF967A0E97004EE4A006A50B844BFA85566DAB389F7DC78F098707F9BD09564D3CEE6B19E8AFA132135DDF64FDF7C29BDA7B501D9273511F1A497B70717AC7346E97B066
			458C13F85083C99AEC9B2DEE51AA9425901F79EC98732640B8E7814581AD86CA875AC1F5ED91098E49AE5139E7CEA1676FB2B2A4017185011C1D4247449A971CB2C8FA64944B711C1D5F2B0E37F3D2D82E1288651294BCAFBEC05FB399CB9AE0C7565039E9AB8A721D9D9A14C24978EF8770499ABAAA2879E5221ACF5670EF32547C04GFE36CA1E2D7727BE42F3E966D3AF8B0EF923A14F746EB5EC6E4C12CB61538737C7C83F5A4174BCFDD59DB78CFFA36A36075033D016DF32A678EEE063D162C39AAFEFD806
			AC3ED26FB03C600A0A0B44538EB99E76664AFD4FDB0B71B025152CD793544DC052DADC8FBF8F9BAF34080D6557B968133847DE94626F7DC237BB44D8E62CE5E308164B59C79E5C2E64DDCB3A68D99DF899C08756C1FF016A002E2F4378B158C97A88F9D72D4DAED5CB12455AC723B726E39DF6F1D80149E4E73A5F7CC6606B63714E0F206C305C10CFC7C4154B039AAE02AB3F6E3E8191374D52AB70D85A9B6C9BE91D929BA260B992E82E8AE3B1B3320D78BAF19D0B197BB9BE176219AA1C455275745D107A4954
			98FDDAAABF6718493C0CFE95729120D4A0E1BD1C035623DF5E6EBC487C226113DAC862E08D12D31E04BED85E95BABFCD691A78EC78FA9759A73BDD12646AA77A915F52C57CA5395C94E4D8E69719A790567BA456D446F55F50331B58C3E1B9365379DA8663B22D47587E53810275C1FD8A47DE27E71CD9D00F66583C965C979256631CFE300D3C3715D04F84AAFEADC6DF82E482B283C9C0B30066G4581ADF88D659E25F2C9D04F84BA1096232F84B2830DGC5824D87CA824A84BA70BA4A25374E4578301E5989
			DE838BE40C74E932F1DF0B62D4E322B1D5AEE748E509C6DF0AE50C757DD09B761D44175D9AB6768B7F77BDCB0B5E81A2F134D02E6F9314780DF1A6D8A6C7D06F0186F368793C5FBC71FCB61AE9AE8873304D6F9665FD54CC73111E9E2F48574D27E683476A3DCDAE2F1DA430ECAF747512768E0B5FB5886D63DEA7F37207AE5C67AA215E8694819489E466FB6C5C81626BFE0B4F5EBF58CF8FDB268DC4F6811D771950B6F783364D355136F0B88BECEB24FBE584BC4F82E27B7B4296341DG58E220F4425B88F1F5
			A36A03FD81685CA02CE3A75DB64A99FE1FC84F141235215BD1AF755758CC2257AA6F791543391C5765E158FBF40DB32CD9728B189F3CF1F0B7474F093D0DAE1EB7A8F82D534671CBB6529F074AA8438DCD598C6F72F05D365DBB99C68DB2CCF7AA17E154985A12B1EAA8FAA30D9FED6AFD3EEDA3FB9F3321E47C9FDDD57353E5FEF1BA729BDB3478A51C6F3825452F627CEC0F963F0D739F73E9717BB87F9D3F96FFC4695F95AD7ECC4E7FCB13967FA14EBFD72D450F677C18C2AD7EC26033FD6CB197FE83C8862C
			F8931E21FB8F123D171F716C5D7D2503C37D2516EE29D404ED382222F15061E8101710FC265339B18FEC5A04B9CC191B680F007A9C2078CDF897617B98F311683FFFC618D9227D8DA339EDC837E8930BFD691B143C2AF82AAB0B6538FB3F9578BCDBE1AD0552A98E9BC91AD5EA9916CACDF658E1496564116365A74F1CBCFB627449B3A5C66579A866C6F30655F95ED97D77A14F52E51BD9BFCCF269387A60F65617116F143EBCDBED8C5D17D3A7CEC1A74ACE9DE5FE5B5002FE9B590C793331B5B8FFA6FCB2BE46
			EFE98D4E1F837953DA037367C07E232D4179B372597B7F7258A961EE4E782A4844674311F6BC6347ED4677BEFADE7D6DEAFE61D0489CB4D576EF5E96653DAF18624A12F36A1E90B9F503259BF36AA72FE2CE5D30854FAA53292F094DE99C7B79AD4466F32C5203D8BA47DE77A1D61201D8039FED58B8B67D8A360B64D8228931459C73D7A31637953114C244CCDBB1D7C9F0CD6E7B4E9FA6A917633EDBB916ABFFB3C06FE33732DCF9BB472D940FD061AC761CF6E36C19853C0C2D18C3BFFC1D3CDB739BB1FA9F50
			CC20A82085C0AB5F40BC7ADF4DE542845FD45ADC6E86C8B49C2A6BFE267FE98B7DBE813A0A5EC4FDD73EBDA604B8EBG5864F139C50F64E7A83D4513FD3525E5F2BE7E2CBD1C5CC968D75D1B01F9A06B7B9EABD937E6607900E682C581AD6063C87CEE42EF09353EB05F921F3E2A552F9439AF19D09AB2E37407B3153EB1393F507443D939DD83145701A42008CCF5AE6B9C55120B145B4C05B291E889D0EA00DC35C7CBCEF80B35A9027288D0A5D053DBEA394DBEADB91B5C4687658C20B9C0F181F24DFEAD3985
			F21BA4A85300325E9E6717ABDAF245F23B92A82B005A007A5ED65BFB4224A5B7A237198965C3C071C08B8364462A35641265B669D066EE03F390D059B6355C2B05DAF2A639CDA7148E20A9C03382642E8F887C592EFA2EF47F393E7F04DEC36D7F2FB664EFF857C31FD79E3F2E7DFD685757353F8F7D6EFA786FC3CFDF8F7DFD68596B633F8FAD369428753130A47316DAC81CA05F066C96077D963D3EEF05064EDEF686B7356399BCEA9B1E4B1F3A02E775055BB01E3CEB8DBDFE23D5FB7C9FDB3547DFE28D7F8D
			7624B514FFD87FD6537DBC997A16467BBCE646BB07ACA1D646DE2D44F194F36C7DE69CEF8947FE56027A1AB8D6560A7AEC9CFBE7887579B8365A077AE6F0AC076691F3213E08E353C75146CAC55F985A48605816E63411379D31FFF0238DA3471E9C98EF235303B6CC3CDD0A87EDF4F2ACC10FB69C9C7BF698EDCC6158ED5FF89B79FE347190EF9769C79B719C7BEDAE1FC39C7BE7EEA319E3AFE8586863B632F408BD41ED98B86628C59B07B976FE875AB84B311FDDC1FDE60EFD5E097A5C9C934E213EE99CAB2F
			C1FD33B956E29A5F67E4935A184F5BD557220D859C1BDF0AB612B8367AAB341146311C6A71B6FEDC0DB60AF6A036399AED98B9F62E91ED1C64D8FB815A286358D061F89BD9863461606DC4835A70F1EC59CD34B103E33F98C19BF3B836E0D4ED035DF3DCED47FB0EC4E83784A88328B8ABD6FF96085D0F2453F75689F59B90BBCB3DF515FC8F12A05030B67B681A1F897CC8AE778B0AA5C2FD91477E78853D1701FAEEB67661F8B37661A4E0F5C0FDC091C0D159A877A43B1F047AE20EF55138108275F40EFDC963
			02F1E72C3EE4A76AFF4B077ABB81F3G4DG0A86DA3893477341ADF25751677258EF391C1245D16D3077BA49CD30FCC7F476961EA9D2F7222DDFD2FF9667446A8F67F07BB40ED4C23D0EE3C65AF7896AD3F230CF89EE6CD3B4E089C049C0C2EE2C5E180BF2D3E93C2802FA834776F889E6033A1BE3EBE88C0904FAD4AE6A9F9CC67D0B81CB814A5D8573G28EE974A1DE66B986A9247FEC33188284F6258EBB4FEA4C0FD61AE543F086B4FGACCF8F7E86EAGF2G3133F7068D58327FB6D03DB300B7DB0FFAFFCC
			57E4A254976979DC68C03D190099F6476A4B001A005C3B79DC2073F1A654A3B956C9E3C8B45463B9F614461015D0CF5B0D7A6BEAD07F01BC508D548464861A19077DFE3FC769778D5A4739401B1F07FAD7513811827574BCBE473EC23D467CD87DC920B6A089E8D6BE6A7D32D7517BA16DDB9C709272D16FCD9AABD2211E110FFA9D852877F0812CC920CEA09F506C8254EB69D3745ECF63C3BC70928BD0EF0E03E0E9D04FACC03D7FB202FA0F94466AAB014CC0A3C011C06C5CBB359D6FC06683466EA8FE559EFC
			87B2AA286FB01637875F012C8A606F6B883E83D993708D7261AB41F7A0A7E4BE9B6BCF6D020077B9C3D7BF94425CAD1DA9B11EB8F5D4161BFFC311134C645CF610FCEFB55640A94C781911DC16FA6CEE12119D6436B65DD4E4D6D40631D5FE62F809317CA45AAA6ED7648C4DF7A2477C5D590C7ECE677E3E37B9585F7F95604F7866E0FFDF88602FED8E767772GFF9FE8897677A1C16D6F7FF1AAE3B956CA6E0C8FF609964FC1643F6CD2787A218F423C0F53EC28325C4E81C5EE296FCE64180F8E7951C7A58674
			D133BF3C0F6E71077751BCFF300F82792F7803FD74090E71D97F3F3EE504D2F565D55ACA7E474F065E52167A8BF5EDEC5CC7BD4A388B4748F740034D035048AE7939CFE7FA15B6294DE1FCC36ECDD5BEFDC4D2649EF05F099C7369EAFEC7EC63BEAD9A886F53738361FD6A9B887669C0G7F9F8702FDFA1FCEBD6F1E9DD42DBD8F7553B931576EB4B63A381F368EA9ED6669430C77D4D9001F8E7894395661BB11E3FE7A5BE174D3A477539343617D7466F0F8BFFD668B76533CG7EF73EE0BF3D3BC36D272F7C4A
			D8FEC2574641DA270DF809713BEFA97C47F3430C7544FF8778E87A770A5CAF4379084BB19FED66BEDA4CFDF458975EC79D3E70BE0A1840C70F0D73511AG9FC50DA8E33154060D47656AB83EE5D411B3F45C091C9C47BB5087F9FBF89C6F18A00EF7847B601EG7E5AF17BE6D09C9F37EF86467177EEABE3B956C9F27703DD9EF90EBC502670B6D507F95765478366486F5B95390FEB6EC40E7947D9037EB1F17FC45604774F9FEA427BC7B0050F6147CD21E2B89BCBDA07B2161852BB5F477629FE4F35742B3B58
			472A7877A56E03662A8976312A70BE18D7956C83EFG7F152A70B177F417B216B30D61760F40F97040B70A5C4502BB11E3BE78E1817AA00E7BA021A03C8FB694047741614270312223B0FCBC7D2DD9994BFF5E8C97774A83FC30661AA297B9F2A7F24C87ABC75087295C87FBC7437B40B49A5E87C32361FD90B59AAADE32BB5CFB43FE8751A2A6F72DF9F2F2C7B46F71A716DBF7575F44185C1C6149493DB9C939EB9D13137356CC521FD513137BC9416464560C5C2D9C7B7B5543D77769486F7204ECC9783C9E7F
			90131E46486FE10D7400C6FE91EB14FF92EBA47F586FE23D9EE16F7855D5FD7F734FC5FFE7AA52893AFAE1E71FC8FEB6D7AFE48F5BEDD21F4FFDFB5B36ED043C834298DDF23F97845DFEC9482F1760A0E8130F01123023D972CB42CE48B7A10F028716D0F1A93231BEAF897BC31C9FF9BBB250E901B5C948A22727G703D401AA4646201A00071F9E08D1AE1BE9C40B89ED89B5FFB82FF9226F6CFE0CD92F4DD017D4D8BD3FBAFCC6D53B0358F83EB726FBF0E5ED67F7EA3E33AB9646FBF520AE269772F2CF7E203
			7EBEE079B5FA0786389908FD53A83F7619907AB72C2D7D9607C332B8EED6BBED64772C677C1228FCAFD9FD0D6877E29B1949F82FDEA37D3E211279DFD0CB878836359865AA95GG84BEGGD0CB818294G94G88G88G0931A3AC36359865AA95GG84BEGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGE495GGGG
		**end of data**/
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(153, 114, 244, 62);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// defect 7890 
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7890 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmDeleteItemFromInvoiceINV011ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmDeleteItemFromInvoiceINV011ContentPane1()
	{
		if (ivjFrmDeleteItemFromInvoiceINV011ContentPane1 == null)
		{
			try
			{
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1 =
					new JPanel();
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1.setName(
					"FrmDeleteItemFromInvoiceINV011ContentPane1");
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1
					.setLayout(
					null);
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(535, 205));
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(535, 205));
				ivjFrmDeleteItemFromInvoiceINV011ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getstcLblItmCdDesc(),
					getstcLblItmCdDesc().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getstcLblYr(),
					getstcLblYr().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getstcLblQty(),
					getstcLblQty().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getstcLblBegNo(),
					getstcLblBegNo().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getstcLblEndNo(),
					getstcLblEndNo().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getlblItmCdDesc(),
					getlblItmCdDesc().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getlblYr(),
					getlblYr().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getlblQty(),
					getlblQty().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getlblBegNo(),
					getlblBegNo().getName());
				getFrmDeleteItemFromInvoiceINV011ContentPane1().add(
					getlblEndNo(),
					getlblEndNo().getName());
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
		return ivjFrmDeleteItemFromInvoiceINV011ContentPane1;
	}

	/**
	 * Return the lblBeginNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblBegNo()
	{
		if (ivjlblBegNo == null)
		{
			try
			{
				ivjlblBegNo = new JLabel();
				ivjlblBegNo.setName("lblBegNo");
				ivjlblBegNo.setText(InventoryConstant.TXT_BEGIN_NO);
				ivjlblBegNo.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjlblBegNo.setMinimumSize(
					new java.awt.Dimension(42, 14));
				ivjlblBegNo.setBounds(396, 65, 90, 14);
				ivjlblBegNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblBegNo;
	}

	/**
	 * Return the lblEndNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEndNo()
	{
		if (ivjlblEndNo == null)
		{
			try
			{
				ivjlblEndNo = new JLabel();
				ivjlblEndNo.setName("lblEndNo");
				ivjlblEndNo.setText(InventoryConstant.TXT_END_NO);
				ivjlblEndNo.setMaximumSize(
					new java.awt.Dimension(31, 14));
				ivjlblEndNo.setMinimumSize(
					new java.awt.Dimension(31, 14));
				ivjlblEndNo.setBounds(500, 65, 90, 14);
				ivjlblEndNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblEndNo;
	}

	/**
	 * Return the lblItemCodeDesctiption property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblItmCdDesc()
	{
		if (ivjlblItmCdDesc == null)
		{
			try
			{
				ivjlblItmCdDesc = new JLabel();
				ivjlblItmCdDesc.setName("lblItmCdDesc");
				// defect 8269
				//ivjlblItmCdDesc.setText("Itm Cd - Desc");
				ivjlblItmCdDesc.setText(InventoryConstant.TXT_ITEM_DESCRIPTION);
				// end defect 8269
				ivjlblItmCdDesc.setMaximumSize(
					new java.awt.Dimension(131, 14));
				ivjlblItmCdDesc.setMinimumSize(
					new java.awt.Dimension(131, 14));
				ivjlblItmCdDesc.setBounds(17, 65, 240, 14);
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
		return ivjlblItmCdDesc;
	}

	/**
	 * Return the lblQuantity property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblQty()
	{
		if (ivjlblQty == null)
		{
			try
			{
				ivjlblQty = new JLabel();
				ivjlblQty.setName("lblQty");
				ivjlblQty.setText(InventoryConstant.QTY);
				ivjlblQty.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjlblQty.setMinimumSize(
					new java.awt.Dimension(26, 14));
				ivjlblQty.setBounds(325, 65, 57, 14);
				ivjlblQty.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblQty;
	}

	/**
	 * Return the lblYear property value.
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
				ivjlblYr.setText(InventoryConstant.YR);
				ivjlblYr.setMaximumSize(new java.awt.Dimension(12, 14));
				ivjlblYr.setMinimumSize(new java.awt.Dimension(12, 14));
				ivjlblYr.setBounds(275, 65, 35, 14);
				ivjlblYr.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblYr;
	}

	/**
	 * Return the stcLblBeginNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblBegNo()
	{
		if (ivjstcLblBegNo == null)
		{
			try
			{
				ivjstcLblBegNo = new JLabel();
				ivjstcLblBegNo.setName("stcLblBegNo");
				ivjstcLblBegNo.setText(InventoryConstant.TXT_BEGIN_NO);
				ivjstcLblBegNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblBegNo.setBounds(396, 35, 90, 14);
				ivjstcLblBegNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblBegNo;
	}

	/**
	 * Return the stcLblEndNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEndNo()
	{
		if (ivjstcLblEndNo == null)
		{
			try
			{
				ivjstcLblEndNo = new JLabel();
				ivjstcLblEndNo.setName("stcLblEndNo");
				ivjstcLblEndNo.setText(InventoryConstant.TXT_END_NO);
				ivjstcLblEndNo.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjstcLblEndNo.setMinimumSize(
					new java.awt.Dimension(39, 14));
				ivjstcLblEndNo.setBounds(500, 35, 90, 14);
				ivjstcLblEndNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblEndNo;
	}

	/**
	 * Return the stcLblItemCodeDescription property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblItmCdDesc()
	{
		if (ivjstcLblItmCdDesc == null)
		{
			try
			{
				ivjstcLblItmCdDesc = new JLabel();
				ivjstcLblItmCdDesc.setName("stcLblItmCdDesc");
				// defect 8269
				//ivjstcLblItmCdDesc.setText("Item Code - Description");
				ivjstcLblItmCdDesc.setText(InventoryConstant.TXT_ITEM_DESCRIPTION);
				// end defect 8269
				ivjstcLblItmCdDesc.setMaximumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setMinimumSize(
					new java.awt.Dimension(132, 14));
				ivjstcLblItmCdDesc.setBounds(17, 35, 240, 14);
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
		return ivjstcLblItmCdDesc;
	}

	/**
	 * Return the stcLblQuantity property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblQty()
	{
		if (ivjstcLblQty == null)
		{
			try
			{
				ivjstcLblQty = new JLabel();
				ivjstcLblQty.setName("stcLblQty");
				ivjstcLblQty.setText(InventoryConstant.TXT_QUANTITY);
				ivjstcLblQty.setMaximumSize(
					new java.awt.Dimension(47, 14));
				ivjstcLblQty.setMinimumSize(
					new java.awt.Dimension(47, 14));
				ivjstcLblQty.setBounds(325, 35, 56, 14);
				ivjstcLblQty.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblQty;
	}

	/**
	 * Return the stcLblYear property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYr()
	{
		if (ivjstcLblYr == null)
		{
			try
			{
				ivjstcLblYr = new JLabel();
				ivjstcLblYr.setName("stcLblYr");
				ivjstcLblYr.setText(InventoryConstant.TXT_YEAR);
				ivjstcLblYr.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYr.setMinimumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYr.setBounds(275, 35, 35, 14);
				ivjstcLblYr.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblYr;
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
			// user code end
			setName(ScreenConstant.INV011_FRAME_NAME);
			setSize(610, 180);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV011_FRAME_TITLE);
			setContentPane(
				getFrmDeleteItemFromInvoiceINV011ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	// defect 7890
	//	/**
	//	 * Allows for keyboard navigation of the button panel using the 
	//	 * arrow keys.
	//	 *
	//	 * @param aaKE KeyEvent  
	//	 */
	//	public void keyPressed(KeyEvent aaKE)
	//	{
	//		super.keyPressed(aaKE);
	//
	//		if (aaKE.getSource() instanceof RTSButton)
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
	//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
	//			{
	//				if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
	//			{
	//				if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//		}
	//	}
	// end defect 7890 

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDeleteItemFromInvoiceINV011 laFrmDeleteItemFromInvoiceINV011;
			laFrmDeleteItemFromInvoiceINV011 =
				new FrmDeleteItemFromInvoiceINV011();
			laFrmDeleteItemFromInvoiceINV011.setModal(true);
			laFrmDeleteItemFromInvoiceINV011
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmDeleteItemFromInvoiceINV011.show();
			java.awt.Insets insets =
				laFrmDeleteItemFromInvoiceINV011.getInsets();
			laFrmDeleteItemFromInvoiceINV011.setSize(
				laFrmDeleteItemFromInvoiceINV011.getWidth()
					+ insets.left
					+ insets.right,
				laFrmDeleteItemFromInvoiceINV011.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmDeleteItemFromInvoiceINV011.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		Vector lvDataIn = (Vector) aaData;
		cvSubstaData =
			(Vector) lvDataIn.elementAt(CommonConstant.ELEMENT_0);
		caMFInvAlloctnData =
			(MFInventoryAllocationData) lvDataIn.get(
				CommonConstant.ELEMENT_1);

		if (!caMFInvAlloctnData.getCalcInv())
		{
			// Display the selected item from INV002
			int liSelctdInvcItm = caMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationData laInvAlloctnData =
				(InventoryAllocationData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdInvcItm);
			getlblBegNo().setText(laInvAlloctnData.getInvItmNo());
			getlblEndNo().setText(laInvAlloctnData.getInvItmEndNo());
			getlblQty().setText(
				String.valueOf(laInvAlloctnData.getInvQty()));
			ItemCodesData laICD =
				ItemCodesCache.getItmCd(laInvAlloctnData.getItmCd());

			// defect 8269
			//getlblItmCdDesc().setText(
			//	laICD.getItmCd() + " - " + laICD.getItmCdDesc());
			getlblItmCdDesc().setText(laICD.getItmCdDesc());
			// end defect 8269

			if (laInvAlloctnData.getInvItmYr() == 0)
			{
				getlblYr().setText(CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				getlblYr().setText(
					String.valueOf(laInvAlloctnData.getInvItmYr()));
			}
			return;
		}
		else if (caMFInvAlloctnData.getCalcInv())
		{
			int liSelctdIndx = caMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationData laInvAlloctnData =
				(InventoryAllocationData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdIndx);
			getlblBegNo().setText(laInvAlloctnData.getInvItmNo());
			getlblEndNo().setText(laInvAlloctnData.getInvItmEndNo());
			getlblQty().setText(
				String.valueOf(laInvAlloctnData.getInvQty()));
			if (laInvAlloctnData.getInvItmYr() == 0)
			{
				getlblYr().setText(CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				getlblYr().setText(
					String.valueOf(laInvAlloctnData.getInvItmYr()));
			}
		}
	}

	/**
	 * Sets the frame location as centered horizontally and one third 
	 * from the bottom of the screen.
	 * 
	 * @param abVisible boolean
	 */
	public void setVisibleRTS(boolean abVisible)
	{
		if (abVisible)
		{
			setManagingLocation(true);
			// Set the position of the frame so the field entries are 
			// visible when the confirmation box is displayed
			int liFrmHorzPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.width
					/ 2
					- getSize().width / 2);
			int liFrmVertPos =
				(int) (java
					.awt
					.Toolkit
					.getDefaultToolkit()
					.getScreenSize()
					.height
					* 2
					/ 3
					- getSize().height / 2);
			this.setLocation(liFrmHorzPos, liFrmVertPos);
		}
		super.setVisibleRTS(abVisible);
	}
}
