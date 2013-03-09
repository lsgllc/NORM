package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmTransferOtherScreenTTL040.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			Made changes for validations
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * Jeff S.		01/06/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed()
 * 							modify initialize(), getradioOther(),
 * 								getradioTexasTransfer()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/17/2006	Modified screen via Visual Composition to 
 * 							accommodate "Other (MCO, O/S Title, Etc.)"
 * 							modify getradioOther()
 * 							defect 7898 Ver 5.2.3 
 * --------------------------------------------------------------------
 */
/**
 * This form is used in mainframe unavailable title to determine whether
 * vehicle data needs to be captured or not.
 *
 * @version	5.2.3			01/17/2006
 * @author	Marx Rajangam
 * <br>Creation Date:		06/26/2001 10:51:15
 */
public class FrmTransferOtherScreenTTL040
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	// defect 7898
	// Changed from ButtonGroup to RTSButtonGroup
	private RTSButtonGroup caButtonGroup;
	// end defect 7898
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmTransferOtherScreenTTL040ContentPane1 = null;
	private JRadioButton ivjradioOther = null;
	private JRadioButton ivjradioTexasTransfer = null;
	private VehicleInquiryData caData;
	// private int ciSelectedNum = 0;

	// Constant String
	private final static String OTHER_MCO_OS = 
		"Other (MCO, O/S Title, Etc.)";
	private final static String TEXAS_TRANSFER = "Texas Transfer";

	/**
	 * FrmTransferOtherScreenTTL040 constructor
	 */
	public FrmTransferOtherScreenTTL040()
	{
		super();
		initialize();
	}
	/**
	 * FrmTransferOtherScreenTTL040 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmTransferOtherScreenTTL040(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmTransferOtherScreenTTL040 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTransferOtherScreenTTL040(JFrame aaParent)
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
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}
		// RTSException leRTSEx = new RTSException();
		try
		{
			if (aaAE.getActionCommand().equals(
				CommonConstant.BTN_TXT_ENTER))
			{
				if (getradioTexasTransfer().isSelected())
				{
					getController().processData(
						VCTransferOtherScreenTTL040.TEXAS_TRANS,
						caData);
				}
				else if (getradioOther().isSelected())
				{
					getController().processData(
						VCTransferOtherScreenTTL040.OTHER,
						caData);
				}
			}
			else if (aaAE.getActionCommand().equals(
				CommonConstant.BTN_TXT_CANCEL))
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getActionCommand().equals(
				CommonConstant.BTN_TXT_HELP))
			{
				RTSHelp.displayHelp(RTSHelp.TTL040);
			}
		}
		finally
		{
			doneWorking();
		}
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
			D0CB838494G88G88GCA06D3ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBA8BF0D4D5163A2BAE56B0EEE6B665324AA8EA149082040F088898B00E7C64D30BB3F10DD538E34D62D6C6591532585DCCF1939292B0C0831950040C530BE1B641CC8810CF275309C9E810CFE2A2E9B038F107CE7A11BCF82F3B277B45B490E04F39775D572F9F5D0DB0BADDF56A3DFB6E6F7C6EB9671E572468F7637232F30A88494EA36A2FBE1B1014C3041474CDAC603802F1321968FE7F8D300308
			CD3970B4C35F6A953259C4BEBE0893AA213DEA39EC5E8D6F3BC87941318761A54ABE8A74D5F43CF7B276BEBD75FC1FFC5ACE39DA884FEDG5500F3820C9B077BAF3CEAD671C1E8EFD35F9132F388B971840E59DDEF56615FD64755BC411E2B6818DFDCADD57B63200DE39261B96709501A1BA9F5775B2CEA3B77C7EF10F50BFF10339577522D4F2174C9B637C5FDDA482E9C9444C63275AB87BC6DDD17871A7B3DF21592F4FB15AAB1602E72AB012AFEC994BCCA15A2AA12D0B5A45AEC477D32536F7284AE8A7ECE
			E5D070F7777B854163F436D757D427BC0934B7559750F5C18650E6F42E5CEAD769083F7C86A9DDBC21A09A1D6BD47A4CCF05689CB7F1FE7E4227F06D42EB056A3C9EE8478130BD1D57781E058932F9F482623A6AF02C835EFB811484CECB537FF0DAFA2E7DABD95FB7B9E7FB94DAD6A4B09A56A704E8E9F9FCD36E9EEA83EFDBD10FB5CF4B669EG89A08EA085E08540DEEAFB0F579F00E7EB1FF254AB4BDECF2B4F57A6397D9DDE37A0413B4D86BDAA6E0C506F753B89E17655D6DF99019E8EEB1E8E372D10BDFC
			C938BDA41FF90B1CA81CD8109701B75F1A2402ACF58D53B3A15E98DF7758102FEA4037833881C6G46824CF9067375CE7D115B71F5522388FE0FCB6A9E6A73397CCA5B10A839853F4A5F4EEB31780B84EC1E650E67B139A49E514BA5E37A141C7C88F249C80C4FC9D76531D6A79736777928FB1B465F2A8FFA2647239D26D1BBAC0377EC55C699FE994587F5F8E66367A89E5B2320AFF3BC375B1EED1C07765C570869D0CAC49E46ACF8BA27AB1151B318E854ED2F9D7DCABC60D3G96822C86A093E4F3A9C07584
			2E5F9A6B41EFE937DDBA5D32393F329643D388AAFE57C9CFC0F1F97A05F3AE3F686A1304GD147FCE0C55A7B42667D97BDCF7D3047E702C7703B94516B0155DD32G0694E0E3F29BF660983FC0FB1D22AC4086320F70F58B9BF641D3F2851433BE37CB91C2A3D8FFD1834A729B57B9D891960077FBA6204C4FD31F55856F92G135503CD08CB03F606A6173FED58894F53E05802E433B944FEA443653F4277FF50063EC47886FC26C370899EB73C84543EB85B6610DCAEFAB51A9F26F8BCA3C7017000069FEB5B41
			703006DE4A3CBF01763B9445AF768DA9C23720A822E7A0B4BF19529288BBFE81221E71E634313540D7C112ECB6A7F17EEA9BF76978EB77FAAF8F791C28B9D5CEBE6AE7ABE13CA309474245F5989B8713381D3E5B436D5431F5B529FCEDF2C4BB6DFDB729A0AE0959E7E212712C2D22EB0676682D6B3943F39F8B8613424FAB5BE368BB5DE323214D647E7E27A8779530FFA6C061C4AEF7DF23051D37B6D7C060A72C538F9A935CA1ABE3E3AF5A235B18EC0FECE381FBE49B3BE20FEDC3775AA359906B9BE5475862
			9D7668EC7207761D748CFBBC82A5DA1BC2D82E712E93E5A1FAC465CB51BDA0A85D62D5817C59C4EADB748C16413BF5A23725610F39AD15DD4DA029FFFCB0A7D2BC9B73F4E2CE5CC426530409A1DBE2B4BD5404B297A4C186C19D758EF994060F2767DB452B7E65016648E3C7B723BDDE962E2863621AB79174A35210207116627A90E56C97E5D0959ED8B55F7B1BEB7AFCAF8D68435C30A099FCAEG1BBB738B744B31F2ABE6BF152DC8DF8D4CBB81F0A1195BCF6127FBF467D6B51C23DEC9E2EA5074739F1F1AD0
			1F421F0684F04BF830CFEBBC11161D063EF3AAFF2C3F20857913F4BEA118CC795372BD0C998C7798453D8787EBD1AE0F419DA6FD2E6A8EBC91D11788F312198ED7A49B63D7E3832E3B9670E492677D2FDB74BE2BB59070760BB4BC28FA5914DBC7EE175F8E5008A3518C770C335BA13AA46FC01BB78877901ADF7E9B75FB38DE51A41EDF665EC0DD34C35B8DB02251F33875D63B2AEF5D824F0B22BFGF911DB88F219B63522ECB0DE29E81E479D564BEACCDE64BC8EA1FE126AC3A799E5F52F0D46AF4027EB347D
			583AC3A7AB48B9A1BC8F00A6B9BDAD8D9B191D2BEDC7C39E37F3427D6487B1F3304CC9613E15CC164DAC5F89D2F995CDB666C1FFC571E5130DF950169BBC8F2A013EB113F9DC9EEEC43E3220DDB8057375FA43D69DDFDDDE5123E8BCBDE043336A750B83220763FEC0F110F1D36745E25B5691DC3794562C81E0385157915783EDE98A0FC5B7EE20AF88EA6DD1341D2C35CDB73E0BB8026BC74233BDAA3E13BD0CB10A831351C8A36AA328356F216DAB047B3B2943688FAEC20286A1235BA77493F78A4A2CEC047B
			3B9540435BG3900AF83F0GEC24EBBDF59D572037CA14DD8A5E394F349115A6BC4F65A436BFBC6A057B85A6C69E619073334F5F507B5935A9ACC7A91AAA1B4B2772FB62A92AE3642385F0412954AEC70A8CB279918C28C25EE9FFD2D8BF1E1F8A226F4F886B4773BBB7C75FAFDD577767696633333EE4BF3F0FE6AD7985111E7DC954BBFC6AD4E693EF2B4FD11F4C4C16467ED3BEDBE7797F6AFD0669117BF332232CD3BA0D4DAF1FE62C853CDF01B2243E72126B9BD730AAB08C4A3559CE211CA555975EA8D0E9
			6F1D46FD619B35F8D663211D8C10B60D1F054E113B3B2F4D4F67B219B17BCDB26B012FF3BE0C42CB064AC3966765404C6CD03E751FC7111F4A6990AF81A4007BG46G24CC673E636F5B10C648370E39C525D5D1DC7D0354F373B46C1CA88C0750E868EC6D5F9EA35A59F833EDBFBC4F39AE2937A2DC4A6B67AB1C82D1F676DD82EF6BF48D904D0F5EB04AA6EDFA684C2743FBA6G199142B1FF59C96F07C5B3183F4C5370DFD1FC398E4F7C68DBD4B75836C35F38995C0F9EB80EF6197B2CEC36GB8GFAG24E7
			39DCACC777B139C41049B1C85E6084626DCE61B25868081E17EEF15C7ECE384B01B1324F2BA8DE99576778BD8E14176275D1A443FD64C0BB8117BE283021AA3E4C0136A98997A9DA756BB791B78A789A7BAC776B47B6A3AE955A699A6EC0B362B2211DBB1363A6547218E01E49ED7AEC9D626C506E52F097A8EE905A23B55C3F54467251B81F3FF74D0C6C6BE3815B03443C33687DFB5E9D2E9F0A83070FC757EB43715B6B356DB84D07C09F4EABBE0161BEBBCEE372450B81C1331F7663A68DF7B250658D081844
			31F8D0F69347CB2EAB5EA198DF769C6A26B91F7B7BE410DF9A40FA00C26843D84C6675EEC63FA13B0222BCA4534B468CBAD7217A1760BD789C9F5B5ACC474265A4FC6CF9EA976B61BDF716EC2E1E053854DAF38C393175029528A366B98FE9F18D4FE25B90583807F530587563BAE36CC29E023057E800AC3A27A46B63C842ACB6E68DBC59B93F184FEB230E67A19FFD1E5FAD867621EF5272DF31CF2E9A7AC62EF275A9D58238A91B6D73A1C9EAD7C52B663B1373F91C1C71BC777199F47DCCE8674E063B4BECFE
			C78B7E1E7B6A64E7DEA365CBA357F6D7BB9F4E29194D6C3CE7F6A86EB89F5F144B627F5993C4135D5EE60C0F9EE15802E1284AB52CD4B9C470C0D834A9BFFC28263E46FA242E263E5292FA2F6031E35C30BE8EDA37BC9A751E912F521128BE57754E4CE6343CF8B2C44BE33516683454DB8F8391357595EC6FCB1739ACCAA77E0C7297AD162FD6774CFDA1A40BED1A7C97D3FF027663F8014B7F309D65BF826DF800649738FE1BBF29A5F7F23F51D91F365FD3B23F5F243D4075FA7D383EE6BFE77B43D1E3F0164A
			C3691C90AF3920D76623247C3B2B75DE3D4BF9536FF89E0B1F1BB623FCFB673078192D6137D37C208E4F4E5BBFEB67CD013E74B9BC2EB61C26B954DC0827GBE00D1G09GE9F3F9ECFD6BF8B539CDFD55697535035315F425BF367E576D282BF4D8EB1D365EA3A7BEA1D1728FC0F6793DBE412FDCE1D8FA2BD7732307BA105622F96057G1CG9240BDGE3667135B71DBA92E36DB3DEAFCD42B19FF709E0E5B4AD703B70864674313CCE2F0FE4D8B7F5DEA85E2D1AF7EB6CE3F2FF065A7B1AF946BCE7AA45E74D
			B366B9D6BB4FF3CCAF4AE665C59E079365D876108E63B2DF8C270149E67D6768EFC9AA78BA009A00F6GA9154B654C295BFE83B9960C718D24109EB3767E9B5D7B1A7C6F328E99790CE497FC1FFB28F17FF4743C61646948F54A4E53116B14E7CF472ED3FEFDBAFA1D52FD7A56BAE5DF7BB67DFA6CC828FDE2BB6A9DEB1422CB922F52D2D2B78C748C2877050E68FCBD56F17BFCE8FCC764BCF7D2472DF96E340E48F96E2C0EDB735CDF5218B19A6CF3DCAA4FDF3B2D3486846DF48D778F8DBC274DCA65E7A4590E
			B84BFC595CB21F631E23389EE887B5DC337D4F319BDCDB5F364C0FD4077F724F5A4318679AEB9CCC86AF4ADC86E366F3392C76707CFF4EFCFE46C9E7F4DD67F7C636612D1D11EDF8D7E76C6FB9FBBB2359B84BC3FEDD37095A0D1FCEE2B9F01176DF0295C0739A005285604B97B03932F9172C37664E77DA793C938BD86E0C6F97609D6FB02C0E1352207D178170238112811682E4AE60B9B3A1D8B3763B5C2257A984DD81DE2E8955F13670FEDA41813FCDF359F76C7ADC3670A5362F75A51E6BFED192293F7DA5
			CE5B4FE4CE5B8540A9G71GB3GD661137238590B7ADC87ED1246F3E4FBBD2FA9D6824E1D46F328BC0B16A36B6A9ED5CC58B65BB125FFD0F0372922D7F3267B6AF90EBCB60D67E853687A2B201D89107BB25FF70215678A2500F33E4C477F1262C7C3BB81E04E4B3CCEE5EF64EB2FFA195729AEB59A6BD4BFA17ABA5358C6E31DEAEFD87F32C6E31DEAFED81D0B580DF52A1B6AFCA62362360FA24848E6BBF586554B32CAAE9B8B7D3602B4947E1473B42A84F1188F7BB4DC162C7179D34E67810F114F71E1FC74
			FA0CFC2E8C6BFF48E36433B52C9E176AB1727929BA1F657E0FF4916D6C9C297B9D09F18F018B48115A0AEF751F8DEA472FC472F7DD371D7711214D66BDF85BF9C6E079536F294D153FE24CF38FD2FC4DAB46BCF7D6BD375D9668CBF8054702A9563B4B4F675E71BC353E5500F494ACB4525DCF71C58B0DF4E7EAE74E82FD77AD64F437B65E9D5D7B1B0CF1E98D2C1935D07D9F4DA276ACDB945231F5D1643ACEBB60AFGA83AFE36478FCB0CFB0C02B1E355F1A96AB3AD4A3AFAC807B1190BA259DB3667CE797FAD
			4173103A8B4F2E6811C44C76693FB434B5AB714A2D3E576A5E6755FF7F396C4F7F82FBDCE88871A46A5E7F5D7A7D6F7DE1437739874BFDBE237E1AAC164DC50BF9BE94C7F1655036EA387D4D3CEE5A2E61A6EDC75C85E80FE8382DA64445C1BBC1431DB3711CEB16069B5E41F32E8C8D37EE8F7D460B34AC6138A9A51C3EB28D77FE09C61F06F36E6574B9B45C233F617475E8386AE2CE0B24613E584DE909DB42731B7F23B210BD82DC7044FE16D78CD15C840084ED5CF87ABFD48512C1B6E6AE9DB3837AD31770
			FC64F3AA3B8CE82F516625ED47B365229F69719A697238F19E9D7F66F63EC651D2BE3E0C4AB0A060F7FD414D46B51A7877776A25BCBF12CDBCBFEA811C93C081088718311453B30E4AFE9534D7EB6BFFD80277978F70F15C5BBF9440FB8B106573FA842C731BDF25712384F9ADFC152F13CE7570B8C0AF60D88C1D7B85517838CEF5A2C1DFD01B13C6F512G6D14D7B94D1F95F31AB3G379677DA8639ACC057B2BE6FAA55118F5AA34BB84D231BA03C93EB8D0F5F355D0470787DD1B3561477515C54421353FDEC
			DC0FB3B46E496DB1627C21C3D863BBDC210E5DE40A34A66BBBE322F6609F92D8FE73F7A61E5FA4AE63794DFC13B13F79835167A76F1B0C794D6E307E7DBB0C79CDE7D89E77658EE3FE53C07479CD5C59905F6F6C7976FC5BCB8861B556A6C1DFE32D3EA664ECA4D16A1C4B547FD3AE8B55D859FA6F15205E6A9BB8DD9F1C8B5135F8E7AC3A6AEB99DDEC5E411E503C7D4518AB6D23F78DCBF947514ECACB67416ECA0B933FB4D7C29ADFD5310F6728F2B1570DE5B9574D234531F573EA31D1B767437AB79447564D
			215D31F5F366FC0817CE795B48004508B126BB0D692C463561BC5AEB4FF2FE5F7D4733784DAFDA2D8F23BE2B04705864AF3E53FD55EF05DF611DB4202E457E273F267157A807DF11BC056C66676AA0D32545423E7A877CE4672D52E8FD68751FA77F51D114C3F2CE12CD038216CBCE123CE1512D8C86FDB7B7EC580090789339016A4E8658380D374847G2FAB2454E0B32A49D8284DA8E4DB78F157E6233B7445E8CD39F565B0EB8C9F2C1D2F0A1B7A73357E7E11285FB04AD632F3E5DDE97C7F4F859AEF944047
			2D647ACDE97AC3B47D3676A95D32CB129417F439556346EF56EDD794A1D43F901AF17DG9F23F632BE2999693E241B737FD0CB8788BC4E20A47F91GG58B1GGD0CB818294G94G88G88GCA06D3ACBC4E20A47F91GG58B1GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGB992GGGG
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
				ivjButtonPanel1.setBounds(31, 104, 288, 83);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// defect 8240
				// remove keyListeners for ButtonPanel inside Frame
				// ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				// ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				// ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 8240
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
	 * Return the FrmTransferOtherScreenTTL040ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmTransferOtherScreenTTL040ContentPane1()
	{
		if (ivjFrmTransferOtherScreenTTL040ContentPane1 == null)
		{
			try
			{
				ivjFrmTransferOtherScreenTTL040ContentPane1 =
					new JPanel();
				ivjFrmTransferOtherScreenTTL040ContentPane1.setName(
					"FrmTransferOtherScreenTTL040ContentPane1");
				ivjFrmTransferOtherScreenTTL040ContentPane1.setLayout(
					null);
				ivjFrmTransferOtherScreenTTL040ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmTransferOtherScreenTTL040ContentPane1
					.setMinimumSize(
					new Dimension(782, 257));
				getFrmTransferOtherScreenTTL040ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmTransferOtherScreenTTL040ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmTransferOtherScreenTTL040ContentPane1;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(new EtchedBorder());
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(63, 13, 224, 78);
				getJPanel1().add(
					getradioTexasTransfer(),
					getradioTexasTransfer().getName());
				getJPanel1().add(
					getradioOther(),
					getradioOther().getName());
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
		return ivjJPanel1;
	}
	/**
	 * Return the radioOther property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioOther()
	{
		if (ivjradioOther == null)
		{
			try
			{
				ivjradioOther = new JRadioButton();
				ivjradioOther.setName("radioOther");
				ivjradioOther.setMnemonic(79);
				ivjradioOther.setText(OTHER_MCO_OS);
				ivjradioOther.setMaximumSize(new Dimension(175, 22));
				ivjradioOther.setActionCommand(OTHER_MCO_OS);
				ivjradioOther.setBounds(28, 44, 190, 22);
				ivjradioOther.setMinimumSize(new Dimension(175, 22));
				// user code begin {1}
				// defect 7898
				//ivjradioOther.addActionListener(this);
				//ivjradioOther.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioOther;
	}
	/**
	 * Return the radioTexasTransfer property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioTexasTransfer()
	{
		if (ivjradioTexasTransfer == null)
		{
			try
			{
				ivjradioTexasTransfer = new JRadioButton();
				ivjradioTexasTransfer.setName("radioTexasTransfer");
				ivjradioTexasTransfer.setMnemonic(84);
				ivjradioTexasTransfer.setText(TEXAS_TRANSFER);
				ivjradioTexasTransfer.setMaximumSize(
					new Dimension(112, 22));
				ivjradioTexasTransfer.setActionCommand(TEXAS_TRANSFER);
				ivjradioTexasTransfer.setSelected(true);
				ivjradioTexasTransfer.setBounds(28, 11, 167, 22);
				ivjradioTexasTransfer.setMinimumSize(
					new Dimension(112, 22));
				// user code begin {1}
				// defect 7898
				//ivjradioTexasTransfer.addActionListener(this);
				//ivjradioTexasTransfer.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioTexasTransfer;
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
			setName(ScreenConstant.TTL040_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(350, 200);
			setTitle(ScreenConstant.TTL040_FRAME_TITLE);
			setContentPane(
				getFrmTransferOtherScreenTTL040ContentPane1());
		}
		catch (Throwable aeException)
		{
			handleException(aeException);
		}
		// user code begin {2}
		// defect 7898
		// Changed from ButtonGroup to RTSButtonGroup
		caButtonGroup = new RTSButtonGroup();
		// end defect 7898
		caButtonGroup.add(getradioOther());
		caButtonGroup.add(getradioTexasTransfer());
		getradioTexasTransfer().setSelected(true);
		// user code end
	}
//	/**
//	 * Invoked when a key has been pressed.
//	 * 
//	 * @param aaKE KeyEvent
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			if (aaKE.getKeyCode() == KeyEvent.VK_LEFT
//				|| aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//			{
//				if (getradioTexasTransfer().hasFocus())
//				{
//					getradioOther().setSelected(true);
//					getradioTexasTransfer().setSelected(false);
//					getradioOther().requestFocus();
//				}
//				else
//				{
//					getradioOther().setSelected(false);
//					getradioTexasTransfer().setSelected(true);
//					getradioTexasTransfer().requestFocus();
//				}
//			}
//		}
//		// defect 8240
//		// arrow keys now handled inside ButtonPanel
//		//		if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//		//			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//		//		{
//		//			if (getButtonPanel1().getBtnEnter().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnCancel().requestFocus();
//		//			}
//		//			else if (getButtonPanel1().getBtnCancel().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnHelp().requestFocus();
//		//			}
//		//			else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnEnter().requestFocus();
//		//			}
//		//		}
//		//		else if (
//		//			aaKE.getKeyCode() == KeyEvent.VK_LEFT
//		//				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
//		//		{
//		//			if (getButtonPanel1().getBtnCancel().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnEnter().requestFocus();
//		//			}
//		//			else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnCancel().requestFocus();
//		//			}
//		//			else if (getButtonPanel1().getBtnEnter().hasFocus())
//		//			{
//		//				getButtonPanel1().getBtnHelp().requestFocus();
//		//			}
//		//		}
//		// end defect 8240
//	}
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
			FrmTransferOtherScreenTTL040 laFrmTransferOtherScreenTTL040;
			laFrmTransferOtherScreenTTL040 =
				new FrmTransferOtherScreenTTL040();
			laFrmTransferOtherScreenTTL040.setModal(true);
			laFrmTransferOtherScreenTTL040
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmTransferOtherScreenTTL040.show();
			Insets laInsets =
				laFrmTransferOtherScreenTTL040.getInsets();
			laFrmTransferOtherScreenTTL040.setSize(
				laFrmTransferOtherScreenTTL040.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmTransferOtherScreenTTL040.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmTransferOtherScreenTTL040.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeException.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		caData = (VehicleInquiryData) aaDataObject;
	}
}
