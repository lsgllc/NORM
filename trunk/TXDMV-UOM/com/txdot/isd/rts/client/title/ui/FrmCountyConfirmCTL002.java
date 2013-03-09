package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 *
 * FrmCountyConfirmCTL002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Rajangam				validations update
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * B Arredondo	03/04/2004	Did not regenerate code after changing
 *							defaultCloseOperation.
 *							defect 6897 Ver 5.1.6
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue 		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	08/29/2005	Remove reference to color
 * 							modify getstcLblCountyName(),
 * 							 getstcLblCountyNo()  
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to confirm the county.
 *
 * @version	5.2.3 			11/03/2005
 * @author	Ashish Mahajan
 * <br>Creation Date:		11/29/2001 16:39:59
 */

public class FrmCountyConfirmCTL002
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblCountyName = null;
	private JLabel ivjstcLblCountyNo = null;
	private JLabel ivjstcLblVerify = null;
	private RTSButton caRTSBtnNo = null;
	private RTSButton caBtnTes = null;
	private int ciRet = 0;
	
	// int constants
	public static final int YES = 1;
	public static final int NO = 2;
	
	// defect 7898
	//	Constants for setText
	private final static String STR_NO = "No";
	private final static String STR_YES = "Yes";
	private final static String JLABEL1 = "JLabel1";
	private final static String JLABEL2 = "JLabel2";
	private final static String COUNTY_NAME = "County Name:";
	private final static String COUNTY_NO = "County No:";
	private final static String VERIFY_COUNTY_NAME = 
		"Verify county name:";
	
	
	private JLabel ivjlblCounty = null;
	private JLabel ivjlblCountyName = null;
	/**
	 * FrmCountyConfirmCTL002 constructor comment.
	 */
	public FrmCountyConfirmCTL002()
	{
		super();
		initialize();
	}
	/**
	 * FrmCountyConfirmCTL002 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmCountyConfirmCTL002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmCountyConfirmCTL002 constructor comment.
	 * 
	 * @param aaParent JFrame
	 * @param asCntyNo java.lang.String
	 * @param asCntyName java.lang.String
	 */
	public FrmCountyConfirmCTL002(
		JFrame aaParent,
		String asCntyNo,
		String asCntyName)
	{
		super(aaParent);
		initialize();
		getlblCounty().setText(asCntyNo);
		getlblCountyName().setText(asCntyName);

	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}

		try
		{
			if (aaAE.getSource() == getbtnYes())
			{
				ciRet = YES;
			}
			else if (aaAE.getSource() == getbtnNo())
			{
				ciRet = NO;
			}
			setVisibleRTS(false);
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Display window
	 * 
	 * @return int
	 */
	public int displayWindow()
	{
		setVisibleRTS(true);
		return ciRet;
	}
	/**
	 * Return the btnNo property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnNo()
	{
		if (caRTSBtnNo == null)
		{
			try
			{
				caRTSBtnNo =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				caRTSBtnNo.setName("btnNo");
				caRTSBtnNo.setMnemonic('N');
				caRTSBtnNo.setText(STR_NO);
				caRTSBtnNo.setBounds(181, 124, 67, 25);
				// user code begin {1}
				caRTSBtnNo.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return caRTSBtnNo;
	}
	/**
	 * Return the btnYes property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnYes()
	{
		if (caBtnTes == null)
		{
			try
			{
				caBtnTes =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				caBtnTes.setName("btnYes");
				caBtnTes.setMnemonic('Y');
				caBtnTes.setText(STR_YES);
				caBtnTes.setBounds(54, 124, 73, 25);
				// user code begin {1}
				caBtnTes.addActionListener(this);
				getRootPane().setDefaultButton(caBtnTes);
				// user code end
			}
			catch (java.lang.Throwable aeTHRWEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeTHRWEx);
			}
		}
		return caBtnTes;
	}
	/**
	 * getBuilderData - used by Visual Age for Java
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GA6F4E4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8BD4D4D712449045C894BF93D10941EF4804B943CE1C9DF78E6438F3BC3B1E09B343E44DEF0FE7431EF5F61D0DC91C09334B4C38E7AF78EB7934A4B88AC264A3C9D420ED68B6C370077291CD8B9CE99523D9FD508FE8696E57F6BFC2EB54EC557D3C7E500D1F1D751C723EDBF5EFD55D3AF52B6A5E066CACCB5C12153D13102CAD047F7BF396A1AB8F915272CFB7F482177ECD1A1E847C2BG7800C4
			9F598CED865016FFB7CDDFC01E2D45899B205F73FC1AFEB7FC97120535BD357091C10E91E887621FD74616337C0010330D7697EF4F039687E6822E74CE1AFE708E4AFFE9FB9147B7C1DF4F3F91323289493B0BE36A8F978560137938CC5E4653B1EFEEAF65F4AB74F10C835A283BFE1E5B29F6CFB4B6707E552909E443CF4FE667232CG7E82B2785C4D77D8DBCEDE210658C40E749F0776E857504071BE45E9D0FDB6C5B5583DB603C7759A7A9CF659259AD43B6A108D43764646B60F33D999F629371A95D73F9D
			BAD60B51D8CF08FA8F756EB92C23BC019F74190E3F4CBF4AF518B114C8FCBDBF5B9AC94725DC375581BA12422D5BE23FC55E59B9F9FCDEA9742DGCC66599CA1B39DF0DDD1089BB263D89DFC1783B4C489DD0EFDAAF46979667BA43357193DA302AEB322188E29D1FEDDD2F3EA730A697EF7D5639EE6822D7431B43D91408A20824CGA821FE17FB70A034A7FB55E64569D4DCA75D6ED38E1B671CE2139D705D5888940EBBAF77A99E9BA14C373EBADC93C69F4DDF9E8B76AB3FAFDCA24297124FAFA6460DBF5F3A
			A54C5AE2577CFC6BB24EE355E37E3531F52D2B41F5EDGFC4138B47DFE00A6006BGD163443AAE9CB45DEFDDE7DC2A6CF1C90E6E61DE3764D1CF8D5B9DB6594357274B99EBFD61004D5B75507318DD12CC01F6D97D22ABFBDB983B2CCE6A4ECE98476C31E41C5FAECCF6E7C4592B460D5E8FFA1E47219F3EC57DF0ADFCE7F19FE578DFD37C51477DF86663B7A99E7B962025BCAE7C36C7AF56E059BC1B9454A9E157907F93DB764EE826CFCDF4685E2EB0E0CC69813C8A908B3090E0B9C0864006E8313F09878FBD
			205FF6856CAD1BFB39FA973432CF75C8E7DCDED5F2754997A50FDD6AF548DE424774D6236E3DC1738668F96A839927E5176C11D43B62826E12D386C77232B16E6A8FF00CC726D42B5DA903G271B883E3E6AC2E89D12D73D6036C92A6C9F4168D155E84B2F250B40919840F74B93E8F315462C1D70ED84E0367A5DA14445C0BF7189E117BFD6FF886D97605832233131455E07EAC81EDBC27E6FEAB01648DFC33CEC115D324B869FDECE5BD8335DEF17FEC553B93B9671F8C61AC1F12F065755FE407040A3504A02
			DEC069122AFA6C3D432A5CAD2B2A5DB5601FDFD40B3AF8030E1F1770B3EEC49FDB826BDA8930DADB5F3C5A420075D994E5E858ED451D63F66A27F1F6BDFC6B460BBC78ABB366451A7142CF5F6D91FE5A123F08543C6A8C6B27C990033A46B37F9C9C9FFA565E23BC7DB2C64CBD914EDE4D7870E76F61E3CBDCC4991B7E0F7C91185D2F55235D63C067E4001563055DD757163373F6CA724A6204F5FAE047E41B5F4B5858D78C11FD6CCDC3F89FFB4B905E47FEE5985B075EB10473A1C65BE0405C220C3882FC7277
			068F6999F639E42A34B60530BA63DDAB5A426E322B176C3681D96D365F1621A61A003EB5091E41B5E013CC4D17C67689DF2A3ABD0FAC3B561E9DAE1F45AF9A486E1A407C40B6416FCBCC27BF9CC21B4B8E59890622F58B4393234E0F677165374670E3B30D680FC372ADBEEE23F1AB41B8629816353525C839E8E30F5D89DB05871657FAF7F382EB3DE8508F6B4295502E86E0F30B8FE04C0CD4D7B15F09B721EE9BE00EAE86EA1A9861BB3E3A6200B34B1D26D9F1B85896E8FBA3579760DE4AB707E58849F8283F
			505663287FB004F6112F0D5155FADC1BA3A09EB444605A66EB359E668B067B9B0A1B838642421C6D633902409A55663D9EF69F916E44307D0B0B894DDDBFB3A35F9440AF53567EDEFDE03CBA6975AAFDF61A9A781EEC5D7C9939DFDDBBC0330D03D636672D5DADF649218C1CD2FC221E7DB79A73B0C62C0A91356579FC5C0B3C09E97AAA00239305BEF3CD23FDEA01A987DA58CFAF54C4B659A7EC7A3C89ED03390A23C58DD79FE82B0C62DF042D61902C93F97C1C98EA2B424FD00FE84047EB3AD519BF88309554
			1B101A87E0A705BE2F18B7B19F677D574CDB040F9391A357519AA8F9E278183BAC843F8A7A2C5679BBEA2F3593C3EB20F40A4F1C98DA8375640B9AC877A45CFB1E94B9F97B1FF1DD69504FF8D22CEB68F0FE403A3A943BCB5556D474991ED345E39F303B846EA30A03EA1B86AE1657B665523C86BCF7CECA53B35C950A5B8F7DA68D770FF90833C15FB7C964261579989B22357EEB341F24757FA7BFD2DEF994C0B923F35748DFD4C6E83EE3F65363D51978347E0734FF0B0FAFA4FBF27F0436117B25E187249945
			ABF73AF999077B980B76BBCCED0A73D6CE92316F2FC6B03674C3218729275BAD77118CBA5613AB6226857A578122G92811681B0193F49C39E74E60AFB75945E5B7F759851F4AAA8407BFF602906502A52024AA573783CCD9F981F9DCF315AA68E5A05CF89B916DC0CEFB7242FA51F41BB82FA9AAE59DDB6E58438E1058B41242573DAC727357DCC4769EB1D9D571AAD1D5D2D7CBCA70E841E67061CAFA3463E5427585E267356D2A5E53198942F59CDB7997CF1B2622A72F6066C59D498E0001651AF8651716C9F
			A0017418A0BA6AAAE58752FF9CC45F92B01F2D6B650F45BDFA634BC94471432B915F9DE2A63375A473B663375733D60F3F331571794515C07B944CEE494E0A40E78D1F3FFEF2687B457B87F08FE91C8F5824464633384F8EE64393FA9DDFC755949147E30BB04EDC053E8A90BBC544197E93B8F6A17417CD91F1267AF8B53497259B7C6653EA332B0A0796EB006C72A8606D6AAD062559131F4DBF66BE5A1D75EF37897BAE7971FC32F4FCE9F6EE843BE4CCE176A0F12C757D66FA9636ECAD69CDE89BA350ACGEE
			0098008400A5F1E2ED1E6328A3541C287EC9D5157A86E986932568C53BBC62C763A1E53C5FBF09E2990F2547BF0EE49F60F0963625337786E49D2BB4C0C45CEA51075AE6C51CBF3E2D0E9B1DE7D85EA8A846FDDB9F4772468E8DDFCE71C5D37DF816CF2651FD46FE9350D227EA39638472546ADF36EDFCBD1E93687FD82BC26C305F26012C9B863A86585AC9B3529B2625692DGBE00D80044E90277FF1C28E4B68DE34FD6A8FEC56C90725EEC0ADC57E7B45D7FCE7D6BA62CB3FA95D5D51C48DF605FEBC2DB2B0A
			1BA2F96D5E04BE86E9F1D0E5C3B97E8FCD68778E391F22F99E2467A6955615BECD6441AF0BC4EE5CB0CD6439DD457836D0B49D6E50G9600C100A80084001469E25CA7F45CF2682F8158BC83EAA80096G9BC0748CB1AE3E886F75028F72443DCA197EE0773DA2AE9B3F939EF00EBF1F7E084A5E464F6296CE5B7100BCB09667726F7C07B8777C5E5F9C598FF6B45F5F8F0A1AE98D8A7BE73D6516F94D53CC6B207EFE2F2C79DBE5F30106BB636DD23CF674F0166FBCFA9C6F10EEA943B0DE779D9AABF709BC3990
			76A8F5860F4550E22D53C5475CACAA9AE3FD0C7741FEBC0BDE354F526B38A8FB6C7D37983E532357706C9A542108B9DFDEAD8825C9CE19511E3EC26BE8C150641CBA199F0847899A6D028E57572B3AAE235D986E6C37DB988E4512F43A1E2F0A83F3E07CF758DAD321E574B9A56168C5CF8B7A1132F0749A0D7EC5C5B8FA13C64FB20723DBB5FA01B110BE78B423A7E974267AF074D4E8D9DC3C31DB3C272783CEB7D354E48315984B35FB133D57E9987E5AE910FAD5038CE13D3131E3586130F077A0AC17BEC7F9
			B5810F9EG77CCF10777FDA272D8726259E4430670EF7F99568B59B1B3F91DB0531F5F53F3EA73981F9BC302CF698BF3A8ADD23D31164F4F0B777391B58C548A7D91EA850B54ABEA059945624EE709976B385B96789BC66A0E8B9173F1B4173BB0C0FE942C0371FE25C470DE93AFFE3F701489DC69AC019BD72163F1442A619A4C8297BBCB6CD5F92109BC649D7754B0A489973F5F1EB68A594BB5B9A3758237F11608476E5D0F76E6FF6F216731DCDEC2F336F9B64B65D99A3E0A621B82704C173F27793285E8A9
			33C5ADE7EE457D5EB987F288C0BB40F500A80004B9A2D77F2859C86E735EEDD55C96884A0E0027D846FFF29B72CF81DEAB8156836892527425G668441BF2B8577A8D26DF8DED16805846FA6129DF60A16869E89EF324C06330A435930BD017957D55E3A937CF90E6916F99A4FFFB4609381168224832C5774BA57F25F5FF1DAFDE37C0E73FAC9B83D8A1EE1FAD4713661993FDE7C3EDA96EE1E050F3B4EDBF5543C8995E2DECCGAD01FF27C03B9C600DD1730E19453CF51CC66642DDFFAE7B5E8F6D5139616E55
			96E3281EDDB04E46477A660E2EAB583CBB752173E2E1ECA29F3FA4623C8AF4A57EFDB0607B455DFF497BFDF859AB7FDFE5B0FFBB569A3936E9E98D7FF6FD36B57C5BF5D76B58EF57BD2D115F2E2F350EFE3B1E5996D93739ED772F3B1EEB8BDFFFFF2FEDF47D7D033670757752365175F78A4DC5AB40E75E18AB6A6FBF6D9175779A8D77994DAB3AE753747B1F95671B34C7DE53367670764EEF8FEF6F427631FF0FAAE90F349FCC3FF7CB44BA0EBEABF46EAC956BE857F02F6D25B600FE2C061BDCA666A6EA3877
			4A445CE48DD7D0B1FA6EDC0ACB077E9A8D3736C2704B54F0E7AA853F4D0982276E23B868B7E93845740D578A7DC18DE7B58B99F7B45CAC0A0B03FE1206FB5BAC6426EAB84BE1A1F70506B3FDAE642E53F0FDB4CF13F990376689DC32D1705B2F61726B84BF33061BDEAF6676E838576B45DC0706EBB009393EF9626D6B6F0B71BDD385C7E16FEB734FA0AB76C6D733C7547B89B0BE85A09DE0BDC041FCB17FDC893E518FAA9E7BED4CEF0E138E7B008BEF437CCDF0007A857A1C9166B0997FFE56AF233FD448388A
			F4B7C09CC08AC03AA6631D3DE34B58D6A6E42C57E464DB7CB236168B99258B20CEG6882F08344AD90B2FED8C16FD0EE69A63E91B03EABAB443BDC4A82417758B9BF5F6B15026FF2203F811089D034B0CD5F30D0701D340F7EBE20F864818F5CD4EC0277826AFBBDB04E260DCDB3E1AC11DD98F8ECFC5CAA13788DA68E46B15CDD3350ABD11B1BE2C6B912304CE5A1E7859D3B84462DD8A856F03D433F06BE6DEF15B2011E37886EAFG96G47A2A16FAA7557D86847AF9272EED35FF43AE4A8AC6CFDEC5CD40AFB
			916D31C848FA3C53AF2B2ACE48DA85743548FFB10C83E8DFACE425D23FFD8677FB316031384B4F2353A4F878009E8B108C3092E05DE2D15F1E5FA36C13771CF8CF0D5A937A1E7A9189FC8F7D411E5077546AA07A1ABD216F297F95C45FD5927A1EDA4C694CDE77CA4AC9709BF3B77E6FFFE2BEFF66F43B15699FDBAA74EF5174AF5FBB367E6777066AFFAD089E3DB7D47FC5C1743F5E9B2A7F47C17A6FADFBF07DEB4A057EF7B47D3FAC9FDB7F5872D07D278651DFAA9FDB7F7565E36B7FAF95086B179CDE4C47CC
			4F7115624DF8092667CB1521FA6E8B16D3992A67D7C1744AFD21FA76853D5BDB77056A79AA1F4FFC7DF57C19056BBC47D4862DCB9EA9C73B9B249155502CB8944F7EBAE3FD8D05432683FC6C5BA69A47BD437C5DE406B9F03DE59487B10108FF99A75838374C7B783EF27ECE376242CBE5F3EB0735757C351316B327BB4E41B737D9E703585BF51A4D6EB60CED336FB70C3D377F5CB0364D8ABF9F5BE647F67BED767E218737D912915BCFAFFF4A57F636CE2C2DAAC92C4DD7973AB6D3106E5F2D8BDD5B6F03682B
			6BC257B6BD082E2F8FDD1BB348EF7F211E6A58A1F49CB6899D2FEABAAEB00D2D632B2631F55CE68E55F1C2907DF3F3280EC3FCFF585D62521EC7FBBB784F0F9EEDDEDD79234D1BD499B8EF8658AFA9E96C37E3B64F5A70E872BA6B9EED5EED53434EE3755DFC9A4FD2E1CDF8FE986EB90ADB8538FDC43C11C91F60DB8A7F6D63125D268EB2FC29AF5F0FEF17717E82FD76B756EFFF7E9570CCFE07ECD1C991FBF3AD67FF48D28E7F589F332824103D7916773191AE49A9B72AA407CF68D02013A7BA0C360D3D4C55
			E9DF40E553E5592B126C8E456BA17A60F31C35E8776385D3CC4359C457C636C2FCF38FCAE748967A078C96125BCBEFDD784662D0C90185679E023907685CA396125D870CCB7C78BE44B3EA37DB6A13E148F61B6499229CEE12FBE47B95323D5FB350CB8500CED75A2E00D85AC3F22E0564280374ED87CAE8156C76336E42979ECEB86243DF9C48FF77774FF253FFDE1C2C8B4691715B6001EF83FFB34DFC62C8445F14F5AFB07FADFDA174EFC43EB9CA6BCB402BAF087B624BC7704F64A37D06576D149C8E282E07
			CE3AEC78FB5E29DB2A6C3F9FC69DC57EDEB1069399ED42D1547BC6401C7F85D0CB878832EAF86E6D91GG74AFGGD0CB818294G94G88G88GA6F4E4B032EAF86E6D91GG74AFGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGA792GGGG
		**end of data**/
	}
	/**
	 * Return the lblCounty property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCounty()
	{
		if (ivjlblCounty == null)
		{
			try
			{
				ivjlblCounty = new javax.swing.JLabel();
				ivjlblCounty.setName("lblCounty");
				ivjlblCounty.setOpaque(false);
				ivjlblCounty.setText(JLABEL1);
				ivjlblCounty.setBounds(106, 46, 177, 17);
				//ivjlblCounty.setForeground(
				//	new java.awt.Color(102, 102, 153));
				ivjlblCounty.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCounty;
	}
	/**
	 * Return the lblCountyName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCountyName()
	{
		if (ivjlblCountyName == null)
		{
			try
			{
				ivjlblCountyName = new javax.swing.JLabel();
				ivjlblCountyName.setName("lblCountyName");
				ivjlblCountyName.setOpaque(false);
				ivjlblCountyName.setAlignmentY(
					java.awt.Component.CENTER_ALIGNMENT);
				ivjlblCountyName.setText(JLABEL2);
				ivjlblCountyName.setBounds(106, 72, 177, 17);
				//ivjlblCountyName.setForeground(
				//	new java.awt.Color(102, 102, 153));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCountyName;
	}
	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new javax.swing.JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					getstcLblVerify(),
					getstcLblVerify().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblCountyNo(),
					getstcLblCountyNo().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblCountyName(),
					getstcLblCountyName().getName());
				getRTSDialogBoxContentPane().add(
					getlblCounty(),
					getlblCounty().getName());
				getRTSDialogBoxContentPane().add(
					getlblCountyName(),
					getlblCountyName().getName());
				getRTSDialogBoxContentPane().add(
					getbtnYes(),
					getbtnYes().getName());
				getRTSDialogBoxContentPane().add(
					getbtnNo(),
					getbtnNo().getName());
				// user code begin {1}
				getbtnYes().addKeyListener(this);
				getbtnNo().addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblCountyName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCountyName()
	{
		if (ivjstcLblCountyName == null)
		{
			try
			{
				ivjstcLblCountyName = new javax.swing.JLabel();
				ivjstcLblCountyName.setName("stcLblCountyName");
				ivjstcLblCountyName.setText(COUNTY_NAME);
				ivjstcLblCountyName.setBounds(18, 72, 82, 14);
				ivjstcLblCountyName.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyName;
	}
	/**
	 * Return the stcLblCountyNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCountyNo()
	{
		if (ivjstcLblCountyNo == null)
		{
			try
			{
				ivjstcLblCountyNo = new javax.swing.JLabel();
				ivjstcLblCountyNo.setName("stcLblCountyNo");
				ivjstcLblCountyNo.setText(COUNTY_NO);
				ivjstcLblCountyNo.setBounds(18, 47, 82, 14);
				ivjstcLblCountyNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyNo;
	}
	/**
	 * Return the stcLblVerify property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblVerify()
	{
		if (ivjstcLblVerify == null)
		{
			try
			{
				ivjstcLblVerify = new javax.swing.JLabel();
				ivjstcLblVerify.setName("stcLblVerify");
				ivjstcLblVerify.setText(VERIFY_COUNTY_NAME);
				ivjstcLblVerify.setBounds(12, 21, 126, 14);
				ivjstcLblVerify.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVerify;
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param aeException java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
	{
		// defect 7898
		//	Added RTSException
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
			setName("FrmCountyConfirmCTL002");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(304, 200);
			setTitle("County Confirm    CTL002");
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (java.lang.Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * perform action on keyPressed.
	 * 
	 * @param aaKE java.awt.event.KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{

		super.keyPressed(aaKE);

		if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		{
			if (getbtnYes().hasFocus())
			{
				getbtnNo().requestFocus();
			}
			else if (getbtnNo().hasFocus())
			{
				getbtnYes().requestFocus();
			}
		}
	}
	/**
	 * perform action on keyReleased.
	 * 
	 * @param aaKE java.awt.event.KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{

		super.keyReleased(aaKE);

		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getbtnNo().doClick();
		}
	}
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmCountyConfirmCTL002 laFrmCountyConfirmCTL002;
			laFrmCountyConfirmCTL002 = new FrmCountyConfirmCTL002();
			laFrmCountyConfirmCTL002.setModal(true);
			laFrmCountyConfirmCTL002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laWE)
				{
					System.exit(0);
				};
			});
			laFrmCountyConfirmCTL002.show();
			java.awt.Insets insets =
				laFrmCountyConfirmCTL002.getInsets();
			laFrmCountyConfirmCTL002.setSize(
				laFrmCountyConfirmCTL002.getWidth()
					+ insets.left
					+ insets.right,
				laFrmCountyConfirmCTL002.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmCountyConfirmCTL002.setVisible(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_EXCEPT_IN_MAIN);
			aeTHRWEx.printStackTrace(System.out);
		}
	}
	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view.
	 * 
	 * @param aaObject 
	 */
	public void setData(Object aaObject)
	{
		// empty code block
	}
}
