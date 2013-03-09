package com.txdot.isd.rts.client.title.ui;

import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmCopyFailureDTA005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Rajangam				validations update
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking() 
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * J Rue		02/24/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/21/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * J Rue		12/14/2005	Fixed arrow key handling to follow 5.2.3
 * 							standard.
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3    
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciSelctdRadioButton
 * 							modify initialize(), getradioManualEnter(),
 * 								getradioQuit()
 * 							defect 7898 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */
/**
 * Frame Copy Failure DTA005
 *
 * @version	5.2.3 			01/04/2006
 * @author	Ashish Mahajan
 * <br>Creation Date:		10/31/2001 17:17:04
 */
public class FrmCopyFailureDTA005
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioManualEnter = null;
	private JRadioButton ivjradioQuit = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblHead1 = null;
	private JLabel ivjstcLblHead2 = null;
	
	/**
	 * Headers, Comments, Text for gui
	 */
	private final static String SELECT_CHOICE = "Select Choice:";
	private final static String BTN_TEXT_MANUAL_ENTER_DTA = 
		"Manually enter Dealer Title applications";
	private final static String BTN_TEXT_QUIT_RTN_MAIN_MENU = 
		"Quit & Return to Main Menu";
	private final static String LBL_TEXT_HEADER_1 =
		"The Dealer Title Application file could";
		private final static String LBL_TEXT_HEADER_2 = 
			"not be copied to this workstation";
	// defect 7898
	//	/**
	//	 * Array used allow for correct keyboard navigation
	//	 */
	//	private JRadioButton[] carrRadioButton = new JRadioButton[2];
	//	/**
	//	 * Int used to specify which radio button is selected during 
	//	 * keyboard navigation
	//	 */
	//	private int ciSelctdRadioButton = 0;
	// end defect 7898
	/**
	 * FrmCopyFailureDTA005 constructor
	 */
	public FrmCopyFailureDTA005()
	{
		super();
		initialize();
	}
	/**
	 * FrmCopyFailureDTA005 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCopyFailureDTA005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmCopyFailureDTA005 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCopyFailureDTA005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when Enter/Cancel/Help is pressed
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (getradioManualEnter().isSelected())
				{
					getController().processData(
						VCCopyFailureDTA005.MANUAL_ENTRY,
						getController().getData());
				}
				else if (getradioQuit().isSelected())
				{
					getController().processData(
						AbstractViewController.CANCEL,
						getController().getData());
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.DTA005);
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
			D0CB838494G88G88GB4F8D4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8BF094D7F2CA85DFD19562126B08C3592422DCB8DB0E3918BBAB896563D2BA1763E0C39D7802ABCACA2922F2D40AD4B8C7D746A95D95C95E2EBE00D90985C968479D8B81DB5832AC9FF6C2D47219150481A3A1A12D0510D61F516EC89A6D6C4EEAF6D6DAA1AC257B3DF9333323D9F110E32B3AE6DE77FB7D3A7B756B6E77E6C965292C9207331290C78951FF7F63A0E460C3C2BE2ADF75B9479D6A71
			3A0869F79A609829A645702C815A6AC52F4BCD5EB9F2885A0D505E7A1557D58D6F95E4C94D7CGDE524CB38534AF9E3DFEFD65F9BEF670F98E52F657A2F6AA87E8861C8FA02B87679797EBF4FC825A657ABB0243C948251B58675C699A93FE0F5E2F71A6FBEE27FDB296CF6A74555046BED9704C391964F918CA77C84BC53D6DFFAC0B6CFF75BDE7994EE5624F2132170D3D28BFBD64BB5490C564450ED6F8367604A6FF95D042CDDAA228E8CDD2AC5824EA312600AC09912DC913B4D9EC0ACBADAD1F2B61F6A5BA
			7F39A049F1D56C70F91B1BBF496EC5192F1FBEC479919205B6136F3532D6FD7E3521ACA25F6CACCDA75FBEDDAEF7DFD23E7DC79EA94D6DC35E4FD0FB63389EE82F86E0F37EE4114FD96C733AD67990B762413E2D706E8310FDDC16CF5F65328CACBCC78A473B1CC75348325547E4A870A5E5115764955752354FF5627AB5FE69F575G8481D683E483EC852827BE3760F88F1E5EF12DDD89071508B79AED1303EA179294E5F8EFE9810A0E6B95830A9AA40479D54F69C69BF9B86C7EB2552712FEF00BF0BF58587B
			94B97BD6D7E9090DEE997F50D53ADF67F128BF299B534BDF0CFAB5G3E83408F20812C85486967FA7D1563423D743A9651C4B5A248FD71712820EAEDF1C98E0A2A2E5FF30BAB69E787EC5C776EFB9C33CB768533DD721F3F61BCE8E3177C6C8B4E1D3ABD761A6C42667E3734F39F3A35FCBD68DE3E05FE78C36A07A7615D217BA84367D3FC5404E7BE9E24F8ECE7GED4FAD6E378365DC074E62A7087B23EEDB9D563D58613CBA4064999B302E6D45D20CA7EBG1F8D108B1087D088D0F95B6BEA384D57576D7C70
			57745B9E535A32312FB8CF40D3CCE82AF0AD925304C8C06C97D4C998174598517B7C104ABE1EB26EEF69FE8A409CD74408288A1A24C4003B90964111E22C4FEE67B16C230A146A1342A2CC900E924EF72F33821E3290533E0C8685CDCC76E07482A75AF2D668870E40G5E9337516613B4E6DD0577A0G33D54FA06236C0FB17E117D051F1F8DE8747966516168EA900E2886ABC1FDFAC46D8A24EC22C6C9023E2A488AFB11DA6979FCE5AE5C2B1E4D6A99E77C8BB889EB3700962E38C8FBC4CD666748CCA97B4CD
			1546631A58A7EA1A9419CC0EDFCDE50925EC3F98E13ADD99C29F5B8BFA11C150FF106BF72C244224DF27220462D19F2E1CEE2732A53449D9685FBA48F3E01E87F36258A07753B786381FF614FD13B43E5EE56B276BA186E58CB27FDCB7E85DEB7FC8F9A66718768C2459FBE30376FB6F7EE34BEF271D23687F499F01597D85BF5ABD97E44E87B8E0583D33444376DB1B9093798E6BD6E14544E0524BD85F365274BEF62D545E473ACB6DFD6C4B5215FDE838544E0798ADC8F30BB297B17924D4FA1C6E61C8C424C2
			9BC3882BB15E7021AD2408245D1202132256A75D91E11D07503796699E2C047706A16ECBF37F45FD696C1DBFA239238DCE3BFC366E1BD71C99C34C8F568EA5FDC957F190EDAE4AE2988C552E44A39A438F517D2D63757852B5E45F37EF887DB1A44E6B7DEE8D15920CA3F2DCB4F45BA49CC19B2BD298168AB72CDE671DDDB257F9B9A09F56040570F4793DAEB6367B83A8C809DDCD457C664DE1146BAC743F88506367FE73B7C335267D2ABBCC3BA24B4C7C463A647A5D380E62CCDC04F00C9B7A3A214BDF7B0FDB
			E87D3ADE0C3E430FFA4926D8B065C73DFE4F2873B0D7B05CC60AFB8C0C73D6914F3DDDEEF3ED9A0CDD32DDC31A1B7DEC6DF27D563C55B00AFCF7833E40507D0BE1F32C7246E2CAC022E9C1DF0F52627FA6772AE7A7E92611E9C55B6B6B6B1084D919ECD392D0B1503A72D19A6FB0BE948F733A72278EDAD7C25B87A08FF3F9FEBA3C5C1F7EE378EDF8CEC8EA8C6A21201860B6AD9EC15BE01E52513CFE7B2559D63B0E5F302D5F90D68D6B31F358EA2B48984A1183782D06ECCBA347CC3602DA935272A42CA41767
			0351A2665FFA7B2351926E5F0447472FAFE17D13BFEC9FEF8BAC7862912F0B55B9E1A75A2BE644DA7F4CD1FC4308357E79CF872FFFAE82ED7D884F47C18175A22350E7146B7572E819C92F9EC50AE806CE31B15C230AAACDCA91A327D29CD45AB4E8311C76A7F40DBD40338300611C84F183500E9A385FF2A0EEAD3433C7F9DE0AB9B1AE64986D2FE8BB4FE817B853651487811CE7F95E7A45EFF48EEB2E53633A00EB14B05AE15A1E577BD7106FAFD541B3A8CE88F199D20C92933B23FA89076BB8C66BDDEAE79C
			F7E014473DEF4FE1EC1900A28F52CEDFD48C903700FDEB890F19F7E1ECA640A6001DG85GEC4EF5745CCACF2438D6821E572FCD91C3267DC5F86EB76F6AF6854EA4D8CCC5C4BDB67F29539C1B578B2C2E490567AE014F7366924637E9E1D6C8B44566C04E26DBD2A4284C81B72CEEA118F4F40F5E6876DD3DF6634AE8770D51764E6E3E4B7AFE4E1AB36F670BEF37240DFD7B843636E41CBD7774898E9603BEA35CEE9701969DC75CB6D2E9D933DAA7A1CD70E474FFCF21635EBFCF4C748F9DE6BA4AFA3B48CC97
			D268A52671CC2FED6778997A4036ECA27FD9EB5A7B064D3ABE797AB3F1C6F0E49EA687991F9DC3E67B381FB86BF42461D3995063DC40FAEF7166F9DCC39A67CD0B545272852E334CEC78CF4EC33A9E3EG0F63974AB04E2C01768600AD819EE7DE11306FAEE89784F81CF9F46AFDF8768B537A2967F2D052941596EA006CD19570F6ED1EE1E976547766B748031DD73FFF105BB76705EF104DDFBB60B81246AE6EA033C7337EF41F969C78E43ADC8821ED4600F697A08BE0B340F600BDC12E5B6BD3A8A3541BA83E
			D7530440944DE03C8C6D174439A49AB7A9635DAF23240CC73DF4AE1DFD00439730AC5D635310F5FC42A461F16BDF1CD65B948613714DA5AE4FB3ACEFBCCD574DA3323CF1584067D0FC2B894F72497FD2BC36870036C564FA7FC0C21EC66D4B16CD5767F5897D9F6BD4089D529D9A487A20E3E412699E88A35DB3817182E08CC083D8BD41F95F1EBE43ECEAE34F4BD07872584167EB8D252F695BC377BECFF706304E98D7B4CD89A3FF0E6F8D212DB5A5CA11FA6D9AC2DBC0DA1C52D8D7DE6B07506FE5F1022275BC
			D804388D205766891E879DE5BCB766CF70BC772DB22CAF492457D583508C50B149E9AFD01A9F5AF7815683649A344F4A4C398B471A573B59546E505F49642FFF5EE3F38C244CD1E253473C776CF260CA205F8B04532F1F9A3E777AAD04E96D88F6774DC7C57DFC9F26754B44C4CCB47C6411305B40DD0B75A8B189BD1365295AA26CAF8B73CA9C7ADF1D22B16E884FEFFB40EE87F45BB5838DEB14CC5A27262CE6857D986F1D8B98FFE2DA20F3DC3EAA8A410FF4BC092562FF29633FBE04F6E59118773DF48377E1
			DB9CFCB44288EE8967FF234C1CEB72F4598E20EC147EF7C76D6883C0E3B97C1D8AEB8EC73A8C748C234FC83D394FBA09754907E781G0B9347DC7CEE7950B40C1B66B54A64990CED46B9C29A8FB745E743CD423856A4C218EBE939911765CE5D6C0465169B14D782F8E482E4CF7373EC629D9E57B7BE7584597BE37BFB705D3ECF1C3953FA9DB01D4CF799EB720A991F699067F37259A7A9ADDD7E750458780ED0128F5F8710BBA752644ECEE11C674EF365BCF72E8AF1BD3E7A5CFC1F3F6568A74EF479E90BBEEF
			3EE97EE6500371F63BB96F13B23F4B9F2960E7AE1F4CF3F567891A2B213D81E00B4C5766D76FFFC26E670CE772BE424FF80F5773F36CAEE33EE7CF61FC2E3057F59620B54C63D5356B416E2D4F5D77B81653DE24FE648B331C66B0705BA8FE4C04E7BE6CB3FCD8865A4EB02FE9FEDF41F57EB862F575GCC81E4GE481EC09701CD597E9A677387375A951CE88F23269BA12717FC994796F84DE7BG5C0A5755G508A60D3B87FE0843F99242B21FA95059666D8238B922C98CD112A00A7BAE643774A6DECB825B0
			3F3A2BAC4F8BCC36A19563C7A650B7823C84108FF04010EBD3641E5FB2AEA7D67816D16536136BE4D40F355165F231F1018A73388E68B7E06AAB5B0CE3EB1FCB77C2C6142DFDC952A7A8FE2D894FFC221762311D8DB4578C7765A76BD7724968CC3A79CF516F932BE62CBE59C07119B3D61F7C76A96E136B01C6D4BE7FD67AFD153D3FE6FA9FF47DA64F3376360FBD54B918DFE5A969EB0227957BFB5AEFA97677344FABAB5F53FED7C9FFCF7B22327C1E76E7517432B9E66EDD2F3CBDE3DFEF3AE716571BD5B376
			75E67D4C72FA53CBF3CD90FCC4D3F93D79D4B92FB7B3D43EE783EAFA7927D5FB5BC6D5FB5B4E2AABFFE7D9D453591E49576866B2EFB0E47EDDB717F913013BC3735AAEE86FB5F0F9953C3EAEB4F035D5FC6C49985FABAF1FC05CA5E87B8D5CEB9487678A57DA83B7F062FE7C9A79252379EDE8EC0E197B1AE325DA5ADA0B179BB67DCEBD37C10E215BB98FB1E4E8B23D7F4529079FAFF6BE54B9D8FDBCD00EF7079AB8AE3B4BBA086524FE9F763DF2DE3F6601BD768138B42F2B91209300756F0BA77BEFF0737EB2
			50B3G32GF281F6EB7CDE6B318ABCAF0CABAAF57629B80E4B2EC099883C3373032312735998CF1E9FDE0A33F39C1B2B390A774987FC81721905B3A0C04FAC1FEBBDBD3728C2D0D23A04C8DC10AF633F8398EDCD49E1CE3B9917D0E9D67797D51B6B7E68AC1BF37DACBF97DC283523EF1A65F23DE2547CAF81AE8F209020E64E6B3AB847EB4D9F15733B3501B9FE37F66C2875EE6D6744FCB756FE54FA3776C18ABDFE54FA3776DD07197E74D16B5D5A737AF8A6532BEEAED326A1D321FBE5195EBBE61569F68A5D
			FF4CAA53E3A9B26DBFEE15E91BBE1E2D51DFD67861999167BC66FBD066BBCDBE3C028D3651C6C31ABE173540145167FC2367CC1F085F28BC6DD30A9490DB4E1C577F1BA31A6F831B970A1DC5A44DFFF36618AF664DA56FCBD93DF13E52B847A5609C1760B57D8445A7201D89101D60BE50DF456D1D1B6076DED5E5357733A9767C73AA2B3D7FB8053E374AEA6F9DA97733F5A72C763E290FE7671A93ED3FA0366732DE5CA66C94437D25251A1F178A8D1D26EBB92EF91E637E351E6BB9B54F757C79C92B1EF3A9FA
			D41D326AA926784D15D3D6BD1FCF214F1F326A790C4E1F45553D36B57D3D63718E5B1AFBA5E0E70176D90CEFD9733C0EF8BC0E77F4694EA46DF086E037FEFC5E8FAB9FCC5ECF6FFB9C2B297F1E7AEC4E3C35567E9145674EDBEBED59707D6DC0EB3E43F367D05503497DD935F55CDA6019FD076D39ADF7525588CA2DF55CCE683BC76FDF18F65C7E954FA4F64056714AAC6632D84ADA6DC657535F7F59742ED6BE7CFA61F79E6A9C2CC66A277B5E3560F5F996F86D7807553C967D5840119ADECBDDB2F0DF2B65E3
			03866E55DABEB6E1605E2F63E3D7AD70DC9E26F304A3A29C1625G7B36370A621E8448DE607548DB98CA74FA243A0667624DC05F8E308F40F597EA98G4663B9BABF46514E3B1C473979A40F36BA4E438F74A8C0A640A6001DF739ECCFD663735D2EF7755878E98D0FF985F7F94C8B55D8E3DEEA7E7E039AEB4CCB4D35BBEA2CB12FACA536BBEA2DB16FFDE24E25AF57D299EFF299276A380C8DDFF1999F2FDBD9462DF5D6997BD268BF2B334A5817A2E3E3FDBA99197F3655BCD83C88575D6FB8765F6CE7963186
			5A78E3D2221193789F980F7EA7988F7C589FE1B4722EEFCA74F40802AC2A9EDAF3F8A0D04BD200FE1D71CCC80088A8F1B928115391C5730CE3B3AA09C10F26F834A9A96619D354909CE8313BC69AD8A1AA4FFBC42CC5D3998BC946B10D54E1DD6AF94753ABEAF1B5026C3A84A9626992A3F17DFB5D79A5F35DD278C8FD5A6F3C5B97D96C5B3DE87D5FC6B97DFF4149A5588FCBBCBF0D50F5CF77DD2DAF8C72EB029C72C602780D2DEDDE9313675A8AFA4F9B63FDF422FE5767C03927CDE37E8FD0CB8788469D61F6
			7E90GG00AFGGD0CB818294G94G88G88GB4F8D4B0469D61F67E90GG00AFGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGB891GGGG
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
				ivjButtonPanel1.setBounds(84, 178, 217, 59);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
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
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECT_CHOICE));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(40, 81, 359, 85);
				getJPanel1().add(
					getradioManualEnter(),
					getradioManualEnter().getName());
				getJPanel1().add(
					getradioQuit(),
					getradioQuit().getName());
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
	 * Return the radioManualEnter property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioManualEnter()
	{
		if (ivjradioManualEnter == null)
		{
			try
			{
				ivjradioManualEnter = new JRadioButton();
				ivjradioManualEnter.setName("radioManualEnter");
				ivjradioManualEnter.setMnemonic(KeyEvent.VK_M);
				ivjradioManualEnter.setText(BTN_TEXT_MANUAL_ENTER_DTA);
				ivjradioManualEnter.setBounds(21, 14, 262, 22);
				// user code begin {1}
				// defect 7898
				// remove action listener
				//ivjradioManualEnter.addActionListener(this);
				// remove key listener
				//ivjradioManualEnter.addKeyListener(this);
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
		return ivjradioManualEnter;
	}
	/**
	 * Return the radioQuit property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioQuit()
	{
		if (ivjradioQuit == null)
		{
			try
			{
				ivjradioQuit = new JRadioButton();
				ivjradioQuit.setName("radioQuit");
				ivjradioQuit.setMnemonic('Q');
				ivjradioQuit.setText(BTN_TEXT_QUIT_RTN_MAIN_MENU);
				ivjradioQuit.setBounds(21, 41, 297, 22);
				// user code begin {1}
				// defect 7898
				// remove action listener
				//ivjradioQuit.addActionListener(this);
				// remove key listener
				//ivjradioQuit.addKeyListener(this);
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
		return ivjradioQuit;
	}
	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					getstcLblHead1(),
					getstcLblHead1().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblHead2(),
					getstcLblHead2().getName());
				getRTSDialogBoxContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				getRTSDialogBoxContentPane().add(
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
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblHead1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblHead1()
	{
		if (ivjstcLblHead1 == null)
		{
			try
			{
				ivjstcLblHead1 = new JLabel();
				ivjstcLblHead1.setName("stcLblHead1");
				ivjstcLblHead1.setText(LBL_TEXT_HEADER_1);
				ivjstcLblHead1.setBounds(89, 22, 221, 14);
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
		return ivjstcLblHead1;
	}
	/**
	 * Return the stcLblHead2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblHead2()
	{
		if (ivjstcLblHead2 == null)
		{
			try
			{
				ivjstcLblHead2 = new JLabel();
				ivjstcLblHead2.setName("stcLblHead2");
				ivjstcLblHead2.setText(LBL_TEXT_HEADER_2);
				ivjstcLblHead2.setBounds(101, 44, 200, 14);
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
		return ivjstcLblHead2;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7898
		//	Add RTSException
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7898
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
			// defect 7898
			//carrRadioButton[0] = getradioManualEnter();
			//carrRadioButton[1] = getradioQuit();
			// end defect 7898
			// user code end
			setName(ScreenConstant.DTA005_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(428, 268);
			setTitle(ScreenConstant.DTA005_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());
		// defect 7898
		// Changed from ButtonGroup to RTSButtonGroup
		RTSButtonGroup laBtnGrp = new RTSButtonGroup();
		// end defect 7898
		laBtnGrp.add(getradioManualEnter());
		laBtnGrp.add(getradioQuit());

		getradioManualEnter().setSelected(true);
		// user code end
	}
//	/**
//	 * Determines which radio button is currently selected.  
//	 * Then depending on which arrow key is pressed, it sets that radio
//	 * button selected and requests focus.
//	 *
//	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			for (int liIndex = 0; liIndex < 2; liIndex++)
//			{
//				// defect 7898
//				//	Use hasFocus() to determine arrow key position
//				//if (carrRadioButton[liIndex].isSelected())
//				if (carrRadioButton[liIndex].hasFocus())
//				{
//					ciSelctdRadioButton = liIndex;
//					break;
//				}
//				// end defect 7898
//			}
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (ciSelctdRadioButton == 0)
//				{
//					ciSelctdRadioButton = 1;
//				}
//				else
//				{
//					ciSelctdRadioButton--;
//				}
//			}
//			else if (
//				aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (ciSelctdRadioButton == 1)
//				{
//					ciSelctdRadioButton = 0;
//				}
//				else
//				{
//					ciSelctdRadioButton++;
//				}
//			}
//			// defect 7898
//			// Arrow Keys to follow 5.2.3 standard
//			//	Do not select button
//			//carrRadioButton[ciSelctdRadioButton].setSelected(true);
//			// end defect 7898
//			carrRadioButton[ciSelctdRadioButton].requestFocus();
//		}
//		// defect 8240
//		// Code now handled inside of ButtonPanel
//		//		else if (aaKE.getSource() instanceof RTSButton)
//		//		{
//		//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//		//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//		//			{
//		//				if (getButtonPanel1().getBtnEnter().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnCancel().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnCancel().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnHelp().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnEnter().requestFocus();
//		//				}
//		//				aaKE.consume();
//		//			}
//		//			else if (
//		//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
//		//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
//		//			{
//		//				if (getButtonPanel1().getBtnCancel().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnEnter().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnCancel().requestFocus();
//		//				}
//		//				else if (getButtonPanel1().getBtnEnter().hasFocus())
//		//				{
//		//					getButtonPanel1().getBtnHelp().requestFocus();
//		//				}
//		//				aaKE.consume();
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
			FrmCopyFailureDTA005 laFrmCopyFailureDTA005;
			laFrmCopyFailureDTA005 = new FrmCopyFailureDTA005();
			laFrmCopyFailureDTA005.setModal(true);
			laFrmCopyFailureDTA005
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCopyFailureDTA005.show();
			Insets insets = laFrmCopyFailureDTA005.getInsets();
			laFrmCopyFailureDTA005.setSize(
				laFrmCopyFailureDTA005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmCopyFailureDTA005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmCopyFailureDTA005.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_EXCEPT_IN_MAIN);
			aeTHRWEx.printStackTrace(System.out);
		}
	}
	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// empty code block
	}
}