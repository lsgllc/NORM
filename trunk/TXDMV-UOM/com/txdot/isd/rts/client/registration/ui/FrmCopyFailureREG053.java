package com.txdot.isd.rts.client.registration.ui;

import java.awt.event.ActionListener;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
/*
 * FrameCopyFailureREG053.java
 * 
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class. Ver 5.2.0	
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							add import com.txdot.isd.rts.services.util.
 * 								RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	07/15/2005	Modify code for move to Java 1.4
 * 							Bring code to standards and remove unused
 * 							variables.
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	12/14/2005	Arrow buttons were selecting 
 * 							renamed ciSelctdRadioButton to 
 * 							 ciRadioButtonWithFocus
 * 							modify keyPressed()
 * 							defect 7894 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciRadioButtonWithFocus
 * 							modify initialize(), getradioManualEnter(),
 * 								getradioQuit()
 * 							defect 7894 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/** 
 * This frame is used to notify the Subcontractor Event user of 
 * diskette copy failure. 
 * 
 * @version	5.2.3		01/03/2006
 * @author	Administrator
 * <br>Creation Date:	09/05/2001 13:30:59
*/
public class FrmCopyFailureREG053
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
	
	private final static String MANUALLY_ENTER = 
		"Manually enter Subcontractor Renewals";
	private final static String MSGTEXT_1 = 
		"The Subcontractor Renewal records file could";
	private final static String MSGTEXT_2 = 
		"not be copied to this workstation";
	private final static String QUIT_AND_RETURN = 
		"Quit & Return to Main Menu";
	private final static String SELECT_CHOICE = "Select Choice:";
	private final static String TITLE_REG053 = 
		"Copy Failure    REG053";
	
	// defect 7894
	//	/**
	//	 * Array used allow for correct keyboard navigation
	//	 */
	//	private JRadioButton[] carrRadioButton = new JRadioButton[2];
	//	/**
	//	 * Int used to specify which radio button is selected
	//	 */
	//	private int ciRadioButtonWithFocus = 0;
	// end defect 7894
	/**
	 * FrmCopyFailureREG053 constructor.
	 */
	public FrmCopyFailureREG053()
	{
		super();
		initialize();
	}
	/**
	 * FrmCopyFailureREG053 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCopyFailureREG053(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmCopyFailureREG053 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCopyFailureREG053(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when Enter/Cancel/Help is pressed
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
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
						VCCopyFailureREG053.MANUAL_ENTRY,
						getController().getData());
				}
				else if (getradioQuit().isSelected())
				{
					getController().processData(
						AbstractViewController.CANCEL,
						getController().getData());
				}
			}
			else if (aaAE.getSource() ==
				 getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				// defect 8177
				RTSHelp.displayHelp(RTSHelp.REG053);
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Get Builder Data
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		
		**start of data**
		
			D0CB838494G88G88GE09C87ADGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BAEDD0DCD516E3ED9C791151244C380C135DE147B84B0CB8E6C6E60AF2D34AD829595498C75CF2F6D333ECCDE60C06A4881D04CCC84475F5839188891F86F0A0E9880CC090B16A8EE24C042F98A3A1A4E0448A25B674839E3C6E57F63F8E0D006CB97763756B47EBB0A9C355297B6EB9771EFB4E39671EF36EED0452E3D159D6DB29A0D833857677B72BA0C43FAE88F19BD63E47F1B91BD3ED026E2F9A
		
			20C878E9DB963465C05BF458E2AB94B21AF2205F847DD2681741F709E0FDAD71B5788833CEAC50F65E3137EC71F5721DFC1D17C8DFA9480776B0C08B6086G22G8A055B8A4A99BEG7D436C9B418ADC4F7C914774B6166B70DB583826BF523672CFB8660785150C9E8178A8G8BC0561F02BCF309F4373635327E504AD5C25AEFDE359D4235F47CB9B8595C88565A058708A1B205C73BCEC1FB2AEFF26C740862EAD683CEC5ED16FC4EE62F6AEB9E11A5512DB62B12AA0B4DFE292D6D2C575529F8264FBAA4596F
		
			953B865ADBDA5ECEA7B22B0DB9041FA044C31F4A776421D3EC7DD5132B0444043D196164BB75B4156B7327037265D7BF1AD73885F9BFC66C0D7322003E89004A7E53764EA5E5770AE312CF75BAD4C9F11B28F0397B7D16375F94288EA505DC0781D877A93276ED8D3876F278DE8330EE8B576D3DD739EE03B37788694367ED85E1F4ABFF066AF4661920EED9338F67D590DF0A94519FE6019685908B3089A09D207C59D45BDF088F77BB9B21ED9FD6BB9517CBF137FBBC9D3253FBC1F10AB2FC3735810561AE0BA3
		
			0A57A98854CF279B1BCC64615072EC280F867D6AE3017BD54C653B05AD723E4CEC935D16DD58175945F88CBE9B540D6A7556986A95G7CAA00980075GDBG3236F23D7E497966D2FAFD60D6C52F5BA1777B07BD8E2F5A6117E42768E57ABDD93018FEE6C0677D613A67D13BC43F2937CB62039F12F8E034CBE2F40B4D3E155A23F5EB50AEF46D7242F0EB8FEEDD389F8833DB518F1FA7FEA843371579A845E7B7A2BEDE0727E766BBE4BC76930076D692775B4143DC071E2C5B0542B7FACCF508FC345D9619C465
		
			D91BE45C5B3FC8981F92811F8ED038AD55D68FF08E408990584677B749797A57745BBE5D5E52397FA29E01D68C40517D406DD39D6E91710A43ABB906E551A730B1BF91D1766110F98F1073B482EB3CAF3AC5FA6E013B43A502A3796818B8318847F8C5C29D10DCA2AC6072881CEF3CD80236F678540FBCCE07AA86C7D07A8691EDF94DF185B88283785E308DED3E024440C8780E852036BAB701387C6D29365A6D5CAE1D234550FE880EAD4AEDEDDD52880A6170CE73755F9B43D8A2DE037855A5FAC43793BEFC0C
		
			56B5169B344B282249FC1E60710CF40260BE8D5FB7D6C47140C3EFE5CE9FA4F4072AFA25E13FAA760B2AAA39470273BFA73278C20E1FCF203A7DFD92FD2C95746A8378DC53AFED2CC427DF0F22CC7ABD8338F34CCE2F13B82B4278655BF9CE35B6E20EDD3B1D7BE94AA077532EC35F931AFEF75E54CF53A186A5EC277E19345DF8568E921E41B56EEC98544DDD3B5D7C3C5DA850B5E2C256487C8679A3D03B7FDCC63B976EC835B581746C60F6FFFD5CCE4FDB0743A77293F6518BBBA6BA03DEC64756C961FD2CC9
		
			B277311312390F35CA0B7B5099494C07A86DFD89F30BB2655679E40FD4CC4E305BAD922135A9824D7BA983E88B49AD299FCB4EB1D16D173E94E11FF720EF7DABB903CE788E6C603EB4F51C7BD26D175F9562BF2B3319653348FB3BEC89BB28CFEC599174A5AA5305893439A80BAEB0D42762F72BACB713734D70AC3EF4CF1A0F7DF0927DF1D21CE663AECE9E90B00E48FED153ED2D63A05A58AB39E02B7040323A71EC213EEE4CCA867D20F6BC876D90C0A6BDF7D5D0608AE6858E751B9DAE14CB057191GD1495C
		
			EF9E1B2C501DD766B01D0AACD373EB7B92A79762BE0ADF78C58847F820BF54F4F9C4AEB6502EB03DA87D5FE554CB56450238E4546BE12DEE44DCC1F13FA338D4B8D07F1845F36F05C2FD2D6B743DE73A07889949F46F8A1FB366AD3B02FCDBGFF66B92EFB07CB9F2B5AFDBEE5C4A2E9016D47012C7F93162AB147C82611C9F9F9F9203FCBF2484AD8079290D8DD79EFA45EE1FC98F80E5715F5A5242E047E9A00F51ABCBBDD8B7D694F2EBCE8C7A52F8F6AA1279860B64DF021EDB0CFB1B42F5FCE6AED95B3E6DE
		
			3FA1BC45EC143440D6DFF8D00E2C9428C3D338ECD36EA21D2D20560434BC86BB496529D3B229FF33FE03124D7DDB6071B11E54BFCDA96631770C81BFGFDDA67FC0F58EBBC45D87F5CC3700194E37DF3220457BF91C0CBCE6179F86C8B542BAF9572D5AA576BE16510CE2FBEC5F22B1ACE57BCF8C6952FB4A639B9EE06602052A6C10B66347FA2FB6C811E2BGA82E3988F151500F57F07797A3EE8B7453D3F9DE7AC1894605AC8B6F7F18746B357E9BA50342574F91CB812E33B0EF1D7FC657B066BDEAB7798B5C
		
			23005677107EB49BDFA224943E8C2DD39CF578E5C8B10ACF3C68E1A59C6017D950FE39442668D7BD96FE8F7C59944616D1A872A06D74FB4491E1E8A70E6DAE62B1F3830CCD8448G28845A291DBCE62EAFC69E64068B6D1A1DF87FEFC79119CCE91978B820BF551D8A5CC93018F20BACB67F3EC49F1B13F6523AA6FFD72A4D3E0B2FE3AFC23F1FF0DCF3841AFDD3A0E77347125B29CC81B72CEEA118F4FD505FF7313F7B330B3D1FF5764087BD4BD1D37A335C1A773730F12FF5975D579E56EE197854CA634FA431
		
			994ED98634F53B903737285430DF9520F9B334145ED02C276339EF9074742BA1F4147513CCBD7DBBA174EC5DFC2A57E3F57C7E1C7158EAC17E45BBE15FAE32F6D3FD6AD99BF867D36B7A5B9F4D23FC1E382A37CF615D55A48E1871791C4D17F79B5FC0F6B7607E11982F5B203636733847B235E1F1A9773B285DBC068F9D419833897A96001CB49EE3FEB50DE36D50BF1546E34C3783A7203D6218E0B71EEE2724AADED22481324F2B00272B5394CBB2A7BB173FAD3A313B6A2FDE62760DFD68AEA16E5B7B2C8743
		
			58E5A80D5AE31635052D1FDA31253A1C18C15B44GED83C0B2C0569E4889GADFB38EE0F87D0C628B5D17CF6D5F50C0C136C45CB50AB12B895C46381253CFBE7D1D24A23E43AAE1CFD0043F958160B439310F1869CE3820FD9E525C65BF46D89463641BD8BF38C4D9989E45F64BDB4E764EA787FA178889D1E6612E102477E9A20657C196B7D6BE96429553DF45B18BE0FCF237FE30D8AF1C37A128431FE98689E233ADF1DC33A8C3CA2GE2G62811E52F8DF7A32065A54441E5DD07472384157FBEBA6FCBD7F6E
		
			4C52F7692E992CB10695D5D5DC48DF3BD74E202DD545C3102CEE1DC1DBC0CA9CD769D0067FE8867DDE96C7891A65C0F2EEAC20D756DE1E839B0F703CD83F1767387FBC0235E58F741DG33GABB45A5304960D738112G7253B9ED7C08BEEF615C107D5675D7306F1E3DDF7F5EC75738A5E40DEC13B17A33E7167F9683EA3B1139707BB7B137747E7966C85D88F69F187608949718A3354B6828CF547CE4EE2ED043FD606BD3FC92FAA64DD15D25B8DEF6CCABFE983FAA1D4438D21E5BDA20FFAE1D458AE831BEC9
		
			A4E35A0F14AF229F651DB0037147270E748C4B67C407738D0697FC2178138CFF67D534AB0D40FC6C19DEBC079DFE70D1B7A58C6D43754B4A743926F69F156D9C3414EE2BB0232F0116666F0B15467C0D74B820EF54465CD69B72EE4FF8546F07988FC063C45B4B7C1DFAGF04B77737AE42C86E31BF6071006DD4D7EEB2EE647305AACC218EBEB6B754BF28FB33BC0F34B9461359EF8A482246D67F75940DFF9DC0F79516DC252FF183F29EF9EB8E94BFF1E6ED54B7341FCE719F918654D09C94E27723EBB88ADDC
		
			7E15597C95BABE7C9CC06E9C8D13BBFB9C43BCF77616715C1970BC57E36E2C7E3701F585A7ED6172D3CE864B7F99417523C08F4A7B1D8A4EFB9C6894F7DBA53FEFC5E570DC6DBFC6F2B574ADGB9AF703DB9FD62ED61FA6EF7BA6F93787D6E11DAFE07353F4057FB2A9E579B043E87A062859E2F4E3CFCE3EF563D57BD0F66349D440F22DEC8556AA20ACFA378989D1E7A701C6643F1C02B7CDFDE535CB10F7B7CAD682F8608855888108C1073A24FD945DF3588CB3C778EA81E9E88F2326EA912725FA6A07FCA60
		
			558AB0842082C4GC4E97C073E425F8B4255D01795059465D81FBBA458B112A23D8E3C4DD19B76171959B076C56AD79BDEDC189728EC7D784321108874749751C696DBBD40B9GAAD774D7CB7E0E519DD864F70C5395E6F20D83FF320F2CDD68934BAB7573D64038B53A31F1A673685EEFA3E7E1A3502D6C6CB01FA0784DBABC7509990247FE9A50862D96664B6BEB977349F556F06B1FA33FCDA4D8AD861F3CC47009BABCDD7F77755CA7130056E3636B6F446BA23345FE5D77BF3C7CCD5EE54DED3F7A262EC17D
		
			AAF2BEFCCD7003F973B75AFB674D5FE86F1BDF7C0D76417970EF348F4DAFFC235DA50417ED1F30F43DE2331A571BB956057566A12BF93DD9E2DDD8EFCE10DC3396FCA49E00621EA8637566C69BBF33D7ED61659F36195BF65CE6EEDB37ED715FD82E5942591E4A77D185175922497C4B8AAEF306063B0764B5FB264576D6A647D92BF8FD5D2561BA0E723963197C2C249EC35CF268C7EB387D84978F7D4D9A6E16E357635748AF9CAD5A04C657387B3A56D82C16B6566207B51B7EF7AD37011045F56B45A788A643
		
			473A6F4D75B7BFDE6C3B29EB507A7816B2FCB7D441F169BB1600DD769676EC992FDFEB419E2DG03G8100C8GBA3EB7BBB87E718ABEBE8E689B8112817233AD36A6G3A5EC315F8DF98D63C4459E33371FFF01E2C44C5FB601B5E9F9A2BB81FF440717BC361818B39475135BEB94A4754837E94409040AC406A83FC2D55AE3C37F89DCEC93960F07B9DF2B77EE7G25AD4F4E6534CBFE89EBE8DA77B75975F57FBA36E69272A774219AB3FA865209DC3B341A3F70A50B2D96208B4089308C0056FE79657CEDED8D
		
			60685BD7DB3971ED2DCA503F0D496546373557C2687FDCEEFCDBAB88F91BCBA8B73E2D5D4B66D319F6D7F01992B5192ADFD9DC267ED70CB2FD92C23F759523CCAB2DFAFAD525D126E758FC3AC7DBAB0720F50BD3F67D9BA8751D6681FCFEF5F610CEFD18B15D6A4838B6262149DE53AF626FD3764EF1C59A915BEA9A587F65087A77401699AB719F33FBC5E98E0DCF352C4577D2DAEFDC2E526EF1C0DB8EC0EB7ABB88FEBD74938152F2388F4C9F65764E4F65763E7F28515E770558738FC70D763EA704DEF44CE8
		
			6FA7C2FC207318515E1758FCFA2FB952F1D4B03D17DD46E3C2EFB15CDFC66C7C3E54154BF53A330663E6B5DCF99D57B3D65373EC1DD14F2990BDCE579B7594C37C46D3EF547341907A8F6B0DFA7E187127F135287C466ED36926B577E2C06F82278FE0FCCB4E65F5444AECFC278BF7A76904BBGFD75636B8ED65D183C2357BD0F565419C751E705BCE32D1DCF7079F946DA7B3B1A6F1783EDB60F674EE5C7EFCC6EF13BF15EE6601916C74FDC4E41F0B5C2E40DF1DEA50CEDE1633B424E2B2A3BDEB9596F4087B0
		
			1779C276AA970F907BAE56FDFF3F6A66578B31B7F58DDAA34D13F3BF8836148F725AF1031D57225F52F08D553C16DA2E619E2861F357EA385DB5FC6EFA8DB7F01C4FCDB848F379DDE48D17DB044B32B4C2FF573B1F603E8F10F61057A3EB50942C9EB9D34DF3F1D63E45D68E508AB08890G20BCB61175B10EC666F39EF539C19E1347B90FE8202F83C88448G28BC44E5DBEF4776558B2F3258B8DA4DE35E29C3BC662D2AB6463C507C7D6BEAE34C8B4D3569554618F7A8A436B75698E35E89C11FCBD3EB080C17
		
			380CAB0FF399D5CD46C70EAFAE634E63C6997BC36847EB0DB276074878D1EDB899297FCED65FD83C386B78754E237F677D77F7FE81ED4CF3C236AA9C417FFF3133FF0031439F7DA798D5E89A98976D7D7E6191458DF73B918854764BA2549B8E596EA57FB96B330FCA32E89FD17C32D3952A5D0AEA9F462EC7921DF6D5312B63124FBE25F8A761D60B6F662AD0C72BD1F95AAEE2C1EA4E5D278A2FE0E5EA7FABE0D43F570D3CAEB8A4377D0268768B7CB73B06F9FD6D12FEEBF9585FFAAB8BE87CEBE1ED707FB66E
		
			FD175C09819FD140F3541EB65C7BF03F2D753BC0FC55A1CF363B1D78BBDB4734AA866F36BFF997797B7898C624341F3D0BF2CF68667CBFD0CB878897FC4487BD91GG50AFGGD0CB818294G94G88G88GE09C87AD97FC4487BD91GG50AFGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGF791GGGG
		
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
				ivjButtonPanel1 =
					new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(102, 178, 217, 59);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}			
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
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
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}
	/**
	 * Return the radioManualEnter property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioManualEnter()
	{
		if (ivjradioManualEnter == null)
		{
			try
			{
				ivjradioManualEnter = new javax.swing.JRadioButton();
				ivjradioManualEnter.setName("radioManualEnter");
				ivjradioManualEnter.setMnemonic('M');
				ivjradioManualEnter.setText(MANUALLY_ENTER);
				ivjradioManualEnter.setBounds(21, 14, 262, 22);
				// user code begin {1}
				// defect 7894
				// remove key listener
				//ivjradioManualEnter.addKeyListener(this);
				// remove action listener
				//ivjradioManualEnter.addActionListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioManualEnter;
	}
	/**
	 * Return the radioQuit property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioQuit()
	{
		if (ivjradioQuit == null)
		{
			try
			{
				ivjradioQuit = new javax.swing.JRadioButton();
				ivjradioQuit.setName("radioQuit");
				ivjradioQuit.setMnemonic('Q');
				ivjradioQuit.setText(QUIT_AND_RETURN);
				ivjradioQuit.setBounds(21, 41, 297, 22);
				// user code begin {1}
				// defect 7894
				// remove key listener
				//ivjradioQuit.addKeyListener(this);
				// remove action listener
				//ivjradioQuit.addActionListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioQuit;
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
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblHead1 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblHead1()
	{
		if (ivjstcLblHead1 == null)
		{
			try
			{
				ivjstcLblHead1 = new javax.swing.JLabel();
				ivjstcLblHead1.setName("stcLblHead1");
				ivjstcLblHead1.setText(MSGTEXT_1);
				ivjstcLblHead1.setBounds(79, 22, 263, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblHead1;
	}
	/**
	 * Return the stcLblHead2 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblHead2()
	{
		if (ivjstcLblHead2 == null)
		{
			try
			{
				ivjstcLblHead2 = new javax.swing.JLabel();
				ivjstcLblHead2.setName("stcLblHead2");
				ivjstcLblHead2.setText(MSGTEXT_2);
				ivjstcLblHead2.setBounds(110, 44, 200, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblHead2;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception java.lang.Throwable
	
	 */
	private void handleException(java.lang.Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
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
			// defect 7894
			//carrRadioButton[0] = getradioManualEnter();
			//carrRadioButton[1] = getradioQuit();
			// end defect 7894
			// user code end
			setName("FrmCopyFailureREG053");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			//setDefaultCloseOperation(
			//	javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(428, 268);
			setTitle(TITLE_REG053);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (java.lang.Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());
		// defect 7894
		// Changed ButtonGroup to RTSButtonGroup
		RTSButtonGroup laButtonGroup = new RTSButtonGroup();
		laButtonGroup.add(getradioManualEnter());
		laButtonGroup.add(getradioQuit());
		// end defect 7894
		getradioManualEnter().setSelected(true);
		// user code end
	}
//	/**
//	 * Determines which radio button is currently has focus.
//	 * Then depending on which arrow key is pressed, it requests focus.
//	 *
//	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
//	 */
//	public void keyPressed(java.awt.event.KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			for (int i = 0; i < 2; i++)
//			{
//				if (carrRadioButton[i].hasFocus())
//				{
//					ciRadioButtonWithFocus = i;
//					break;
//				}
//			}
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (ciRadioButtonWithFocus == 0)
//				{
//					ciRadioButtonWithFocus = 1;
//				}
//				else
//				{
//					ciRadioButtonWithFocus--;
//				}
//			}
//			else if (
//				aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (ciRadioButtonWithFocus == 1)
//				{
//					ciRadioButtonWithFocus = 0;
//				}
//				else
//				{
//					ciRadioButtonWithFocus++;
//				}
//			}
//			//carrRadioButton[ciSelctdRadioButton].setSelected(true);
//			carrRadioButton[ciRadioButtonWithFocus].requestFocus();
//		}
//		// defect 7894
//		// remove arrow key handling. Done in ButtonPanel
//		//else if (aaKE.getSource() instanceof RTSButton)
//		//{
//		//	if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//		//		|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//		//	{
//		//		if (getButtonPanel1().getBtnEnter().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnCancel().requestFocus();
//		//		}
//		//		else if (getButtonPanel1().getBtnCancel().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnHelp().requestFocus();
//		//		}
//		//		else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnEnter().requestFocus();
//		//		}
//		//		aaKE.consume();
//		//	}
//		//	else if (
//		//		aaKE.getKeyCode() == KeyEvent.VK_LEFT
//		//			|| aaKE.getKeyCode() == KeyEvent.VK_UP)
//		//	{
//		//		if (getButtonPanel1().getBtnCancel().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnEnter().requestFocus();
//		//		}
//		//		else if (getButtonPanel1().getBtnHelp().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnCancel().requestFocus();
//		//		}
//		//		else if (getButtonPanel1().getBtnEnter().hasFocus())
//		//		{
//		//			getButtonPanel1().getBtnHelp().requestFocus();
//		//		}
//		//		aaKE.consume();
//		//	}
//		//}
//		// end defect 7894
//	}
	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		//empty code block
	}
}
