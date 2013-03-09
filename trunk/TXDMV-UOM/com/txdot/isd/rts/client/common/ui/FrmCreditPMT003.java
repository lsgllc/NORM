package com.txdot.isd.rts.client.common.ui;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/* 
 * FrmCreditPMT003.java
 * 
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking() 
 * T Pederson	02/25/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * T Pederson	07/14/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/22/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/27/2005	Add logic for "ESC" processing, i.e. return
 * 							to PMT004. Add'l variable cleanup.
 * 							add implements KeyListener
 * 							add keyReleased() 
 * 							defect 8486 Ver 5.2.3	 
 * ---------------------------------------------------------------------
 */
/**
 * Screen for Credit PMT003
 * 
 * @version 5.2.3			12/27/2005
 * @author	Nancy Ting
 * <br>Creation Date: 		06/26/2001 15:01:28 
 */

public class FrmCreditPMT003
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JLabel ivjstcLblEnterCredit = null;
	private JPanel ivjFrmCreditPMT003ContentPane1 = null;
	private RTSButton ivjbtnOK = null;
	private RTSInputField ivjtxtEnterCredit = null;

	// boolean 
	private boolean cbContainsCredit;

	// Data objects
	private CompleteTransactionData caCompleteTransactionData = null;
	private Dollar caDollarMaxCredit = null;
	private FeesData caFeesDataCredit = null;

	// Vector
	private Vector cvFeesData = null;

	// Exception Messages
	private final static String ssTitle = "ERROR!";
	private final static String ssErrorMsg =
		"Incorrect field entry.  Please re-enter";

	// Constants
	private final static String CREDIT_CODE = "CRDTUSED";
	private final static String DOT = ".";
	private final static int CREDIT_INDI_ON = 1;
	private final static int DECIMAL_NUMBER = 2;
	private final static int MAX_CREDIT = 10000;

	// Text Constants
	private final static String FRM_NAME_PMT003 = "FrmCreditPMT003";
	private final static String FRM_TITLE_PMT003 =
		"Credit             PMT003";
	private final static String TXT_OK = "OK";
	private final static String TXT_ENTER_CRDT = "Enter Credit:";
	private final static String TXT_ACCT_CD_NULL =
		"Account Code null for date: ";
	private final static String TXT_ERROR = "ERROR";

	/**
	 * FrmCreditPMT003 constructor.
	 */
	public FrmCreditPMT003()
	{
		super();
		initialize();
	}

	/**
	 * FrmCreditPMT003 constructor.
	 * @param owner javax.swing.Frame
	 */
	public FrmCreditPMT003(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmCreditPMT003 constructor.
	 * 
	 * @param aaOwner javax.swing.JFrame
	 */
	public FrmCreditPMT003(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when OK button is pressed
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (aaAE.getSource() == getbtnOK())
		{
			handleOK();
		}
	}

	/**
	 * Display Error Message Box
	 * 
	 */
	protected void displayError()
	{
		RTSException leEx = new RTSException();
		leEx.addException(
			new RTSException(
				RTSException.WARNING_MESSAGE,
				ssErrorMsg,
				ssTitle),
			gettxtEnterCredit());
		leEx.displayError(this);
		leEx.getFirstComponent().requestFocus();
		return;
	}

	/**
	 * Return the btnOK property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnOK()
	{
		if (ivjbtnOK == null)
		{
			try
			{
				ivjbtnOK = new RTSButton();
				ivjbtnOK.setName("btnOK");
				ivjbtnOK.setText(TXT_OK);
				// user code begin {1}
				ivjbtnOK.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnOK);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnOK;
	}

	/**
	 * Get Builder Data
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GABF115ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8BD0DC57F5F2AD93CDABCB2A07F614D41EEA9C391593BA45934771341AD6F655091C2829BA159DF52C4C6044CD5DC44DA80DDA3BAD0DAE0BC07CD9FEE2BDD6239562338B88160FE0D91045E745CFAB0B0A150CE3D271E09F7060313B5EFD08C50E651EF3BFEF9FE897ACCF4C4C19776E39771DFB4E3967FB97D2FAF6E7C61A291410348C427F5A53881928A5640B8FDB7FDE60464EA6D808616F9CC0
			91A9BBFD921E33B017B218E0B193B9BB8B463F0671117E84CBB93C171039ECEF8D3C4459279E66EA1AB7162F3D4F2D9C314FA9BAFE36BF8F1E85G9BG378B6058D95C7FBB7D960E5F8F63827E0E10E6A2E43395577C27E6B160DB2DECDD04BFAD67F04DBF771F617307G0FEB0E8210F3D11A59143BC7DCCEBE9E7F5DD74871EF4D1B72F1AF83FD81937C5B0D7CE9A5BF230AC8A79F96A20D2E117919F726828B8DDA4497509A14302FA124059B26D4C576EB8DB0319070B7ACAAAED7FFE821AFA47B94ED641A57
			61E8B9CE7935AEE6D1DA046C01B1636D6F723B785E09732F106E2736E446634DD941F89A2F087236E1EFE3EE5EF924DDBC10473F5B817307G581E3F9C90FB0E403A03F4ED0B06EBB7417BCE00674E8BDEAE5588DE46BE7A91F9F3F20B29B08EAF25150C072E4AA8AF6E97BBF25F22677E7DDCBC3B884C6DGC886B884F09C2034AA4172B6353747F340F4C94F2456C71556938C762A3E503500CFD6615D6502990E3BA1CF85C2BEC218CDD5AD564760C7C0FD55CAFB0A5A40FBC458C0520DD7097A632D1999B1E4
			CB7C66564CEECE43DB95150D49F5AF9F6552G3F8DE0B740DE0094GD2AD647A271C1675649A72EBF248AF29230B13C1A92475AEAA2ACF8EF179AC83EB49978B58F7558F7C9D534B6E9623DE0EFCED2B69D48C3D9C592D194ED4B3FD34D6C77542763E9DF7EFEF757D67C17D389A6D700F299D4E42FB9A37D106FF1A627798704C46BBA89E4787E04ED1A36CF62CC04860B97992B1B7ED0BA94376979EB1A556B0FE12EAD61F6DAF8BB1169C867CF100BCDB022592408DB08120594479B667B6FECA3B9DB11CAD7B
			7647392761A9C73410B4648FEB12FFCA3EA50594E9D21543042FB91E0B3CCF2E786EBF28BFCD419E8332DF8EC91A927083F5E9C186C38A33B5263CA2DC9312692CD7D910E10305A091F4F372CA6029CAE16DE650A7E9F2F4851BAF4EC3DD5E11EE81C5A0G6F7BEC2873AE9A339261BD9900696A8BE644E55993ACD63B504B23F945703C8A06AD2BAE17DB19C2B6245032587F8B7998CB64BB90A75DF2D0767B60A54C673664E7C775B29D50F9CE24F87411BEE0BC2C633F14DF4470C04328E5B17F841D17B4AD24
			CCAEEA72282CE90AFFA67A7DD7A8AF61956E97A65C470B5046DAC1AE8F4038AEDFCBC109C1BECFA0B03F98746249F1BD5D21F1B6886BB759C57E7B938D73E112DD5869CF46041D3A73D3C87DCBDBE25A69063F591A395F4E6CB3453E5A573EC1E9C677382C0DBD205FFF9AC8627B32BD46BE17BD527913697D274528772CDA70BBGF72D503B3F404A7C2DD78A4B42432E076044E4DF544A58DADFE1FC9B1BAF0CEDE39F96463631254235ED681342D8B64466928AB13784167C861B7C1D42E26A43FE3FCC1956BF
			A12C3E780997F52178956DBD45B7A3EB234ADD996A10DA342DD16A0363709E2C953634F4DE58D2655DA3E46FED4594AB1FEDFFF213E9FFADBB4BA335D1DBE2BCEDB6234EE5D5DEGC57585967D9A43BFC67D1B63F9FC59D894FB6DEF9721BD4E4B4BFC5D1622CC02F1C4DD14F5591225DC54F1C8D90023C20765B55E2383469AAF957843FA303BAE41628560FA390CFADED5CEF11B29ADC51ED4D83B81E0FB1D30191C62378C3E4A0D25AF202ACC757A193CD1EC46B314BFDC14A19423B3DF556578F9F1712A39DB
			DCA6B61FD902B22906B810DC07B24D69B59E668906FB08622640197253C55E3DE6B656243E70C3B14F8F61F89DBB372C7A55B96BF6995235815ED9AFE4FF28449827FA42614014C2D382BF0B4C13EDE43DBAF606E699159632B73C23EEC5D283B33D018870C2EB4AB7E82CC3FA1EFAD1D3668C63D9EC04718E00E7F4FE9C25775B5245529CF8CEAB21B054C2BEB9A2F45AD90A3A419C455122F6EBB66AEA4FEB31EBB70483DCC7A977696A495348C7AA604D9784EF4F169699F485F5A6246499B8C9410F2FAC1D59
			B69F4F16E5885BA6A2B6262ED9F7D5DED8D97FB8E14CEA1CFBD4DF1E8B2BEB1FCD94BFFEE1F56DE39E96350F8AF387AF08DC7C8765A8D7EB83444C86A157FF17659B649A89A8FECD1769AB2751BF83A1E5C6718B5C1FD29CD459B4E031FCD6B208F43DC0D385E038470690378D463B9AC47E59B80C7E1F2C0F375071E1FDFCFA78B711BB10FEACBC5B2371B732C7FA9CBC53514EF2BC0F08BE5EC5474BC444382717B09ECCC3D186E9E2B4A8CF11ED0D28335AC19163D2C186B3609CGA3GC100F4CA6B29A12421
			A99A353D430D58DBAF7F02F01ECA781E2BE2E0DFG5A882CFF7CF2930F27FFB9EC0C27E79C2C9669062757A17A4191FA2E483B8A38DD8EEA0B03252B74108F8B9A60496613D74C234F5809F17E7BAB6651E71FDE71FDE848B81FE1781E7977372BC45F197A6D9F90756B9F476D4F0FBA189D181B5813381EC8CBFC38AD1D51795B8F0CF1427CA5591496074EA07F5E5B343A4F7F199DF5C863631CF4C70AB4041760C0DD2EE1D4344A635FB06A904FFBD31308FF0FF722FF6E0571E100E3CD427EEF8EFE363E6C79
			D3C2A74F7C597748F38917CC39F1E449EAE6B25878F33B730934E8DDF54E0A720C411C86309DA099E09F4011E691AF7E629748A354D5D7403AFAB4CD1A1A2551DA14DB3794F9A90AC663E6345DF86D437D616459AAF85E1266F877032482A1C1E198D4F9FDF28EA22CD71AA1FA6C9CB66A669870F332A56A6796F82F8768B660D80CAC6AC2DDFBDBD80CBC25637F0762D5831E454EAF53334171C618FB3DC5444E7F2DC03B7434A6D8E681B6836C84F826D568E560EC8553CB8C1DDC01A28DDC903BB8CD6820569A
			3F7EEC322E5F7B392C1897A7831A96D8C07A825FEDC55E35C010A2996E0A956D8442782C4616F27CD5AB5A26AACFD3B40F65D791F79064FA3DD5447259D11A8FE1EC3EA8F00F5F243984469E9DF70E664D8998C7F45CFC27488D1BAF8ABBAF273ACF0671C10023BA7EBC4513B66895813A5B847E294EB13EDE1C6D410B31E3BC757BB56662670597689E46381E314EB78F328753777B95714FFC22E27DB3D7AAE8FD84E765DD8E4A3C672920B9FAFABAAC6B36952EB06B382170C8A02CE0D14772C36DD5349BC9D5
			E67CE823A0742EB6BC27DEFA6E93702E3509DC7349A852D6256540A2507636633A5F9F95F963F5D8C7GE783688240BC4E3EFB66A6461F85A922ACACAE5066046DF1005A4C8ED83B3BDD2CF51E26EB2119D939F6045A52EB306E98004509385F6A342C21E3117FB0670435A94F24FA85AF4FD89E54675FF661DAAD222D18E4F3D5C5F866131A7F7AD5023191773C55E54CC793CEA667C6F832791FFFEC1CCF6173159DE27E3F660C734E8EB69F0167D94EAF4A497ED2AEB3BE46C74F4393D3CA033424B574859602
			81BF965BFDD75E75DE39F13B47B3B4706EB5F86771FF6712B117B9F3BE0A5B9FEC6EE03E3113BF5579A752D85C533C62CE78B04C19DD2227EA1A403BAEBD47A1C3AE57C0C87175CAB39EEA99AC4F6D710ADAE150A5725C0FA84D8DB05E8E106C927DE824DA642B24BFFA09F83E9A7B9EBB45FB47345F4578BC6A0A665E3197BBF2D95D735715C4BF576253D8A37865A52B2EB73782960E97D43666265676D6E7CBDBEBFB3DB57AEE9775536BD5D1BAD2671978F49C75D6071D49B0B7AFE4B8735517A9DF716A90DB
			A7635D539915C1449728A1266354909EE9D254900FF621EEF15F4D1DC2079F779BFF8B58D3F8A7EE4E5F4777FD4D30FF9668106DDFD050CE9E30479B72879735FEC5D6FD3C473BF88F79C379AB3B045DD8CBDA9E14EE6FA2A4B3BF2759BCAF7A46CDDDA2875757A0EEAF0C0FF4093C1BF93E0E4429A5GB9928A846510364C3034AB67717E2E77335D1DEF7A0C5FBD7F405F31BA657FEE228F25F631BAA5CD474FD0FC5625A81E7971B15D0FCBE1AEF2C9686ED7B63433A3B0FE93407C8E44ED00C1G5FBBC21757
			4FBB48BA77555EC0508389CCB5DC25B27A5F2035F084E8A582BC87F09060B500D41DFEE3F54B9AE7F5A390209D8AB6AB128216C1CB2610049DA95361E3DD31F4E83E4C6C3A921ECEG4F65E87E67F5ED9D466089407F9AA091A0896040E54157C7556B7E8EF3A532466FB02EFB31FA4A2386BEC8F76C1A24F4955E4647DD709C69B65E2B364F47125D4757C4E2506735E48D3E6BF7134C85781C3386750277088A9484F769554FA8AC744F30BDFDB7093E7F02617D5F3D1F479D6FCAFFA93C79F96E41641F36452F
			4796EC316F00433658F7404B36356F009F32453F835EE43B7F8EF82FBDBEEF5F342FDFAB9E34476E8FDE365F5F9F3CE20F5D9F3CEA3F3FBFF80A66224DE0EFBB3AC50D7F4EA5518BA46B3897B3C5AF30CF476DAA963D4061EE61FB763A78F2BA6AE21FC1DBDD6CB3682ADB7B379EF7DD3CB3E27C0D53BCE66B01B852A3F8FE1E625CB09E57F12EB251A385F5DCC21910ED230E3BB60738C3B0BE2E637E106232FA2147699538F7E92EEB0531DB47FDEDDE509B699535725EAE3CAF53C0A12CC6FE191E45CE18CF82
			E03899FA967BE1FCC8477DABBD8B5217E04983986BC35C1F973B57700F74D56004EF904A884BAD9EF87F95FFE73C056F895ED8CD75D7982E785C61FB748EB79012E7C201C53FCF749C4728EEF7822D24BE416B945563819827683CBEDE369E2F9986DE4D6EA82F082F0471528ADEFDF3D8F74F86C24ADD4CB5EA0F6806B8DF7766C43D65F08B5DBFBB07E368E9BC32FFC61BA5C2066F5035E843E37ADAF9FE25AEFEF881F541E479567CFA321CDAC36F33EE164FE91DCEE9E7B7C6755CFE897928201D0D1535B62E
			8A3E2E5E91DDB716019706F1EA5CF6A8F21BDA755A5857947D6E0B451F663B7B7B9AC16BF8B3A1624E6A55BCE37D6C7E6EA553F9923BFE555CAC376DF3C76B575DA5DF4FE4B437DC0C7257DC063F43987AA6B590EAGB5446269986F356E16D510183D560D2101C12F21550AC9037170F4DB14075F1BBB436971D23EE1588B06C04BE5C6132D7B47B91435DD574DA1FC65754A8BCE3411D41057F285FAEBGAF40AC408600C4005DGFBG8E819C85A07D8916D2G9B00934083B051AFE2ECC78B5E21462B3BF8B3
			E7283674DC610C9F1B3F67DCBF877D5099BB879DF55E1F03FE6A0C1D035EF09AF3904325BA512FE7006FB1FDDDDA07403D2F63B2BB32B96EBD45C7FD166173BAF2B8FED0468D853E388379DA2238B10EABB7605E6738B39DF8AF82A2C388915F1E6BC01F1FD6D4150D2B689A459F16B5DD9F3514C7A5A89978EE54F148B77B7F3ADB257F86461174AFA4C3A3EFB1BF369A7F5825FEB8C40A6F77431EAD7F705D245B6ED293B18D114CD94A779049D8C285C4021F1CB8F182A17CA139C71D0D7EE964242341615008
			697AD50D642F089F7ACAF43D7ED52390A954B9982222265CE9F1257F604B69A6F29236E7C7BFC4CE62798197A146CDBA9CFD847F5DA4031BBCDE373C85548AD67AEE5A13658F1B3736AC1AC8D6BF491C1654602C84D4680FEA9E12BBC949E11F20EA446C41EF9B615BC67AED27071826429AF9BB0A1FC2BC1B9D8DCAD3B2AC4976C921F9CA81854B7B00E4CF07E6A66986200D8F7ABF00ED6988278BBCA4C71B257D89583DC64A2324C750EF78C4A7950D1CB88125887D23CA4B6611E39860BAC0260E45B377GDC
			63ABD8FC34FFE23C53F973117138F7BA15C3ACAEBA07D67F0E19C7FF6F53G3FEDC84402715397624582E8F3C697A4D5050CB85F637761BDFC6F32A6C76B2B22F2249F96EB78A41B3314E77338A73E79FFD0CB878898B24B870F90GG08AAGGD0CB818294G94G88G88GABF115AB98B24B870F90GG08AAGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG4990GGGG
		**end of data**/
	}

	/**
	 * Return the FrmCreditPMT003ContentPane1 property value.
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getFrmCreditPMT003ContentPane1()
	{
		if (ivjFrmCreditPMT003ContentPane1 == null)
		{
			try
			{
				ivjFrmCreditPMT003ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmCreditPMT003ContentPane1.setName(
					"FrmCreditPMT003ContentPane1");
				ivjFrmCreditPMT003ContentPane1.setAlignmentX(
					java.awt.Component.CENTER_ALIGNMENT);
				ivjFrmCreditPMT003ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmCreditPMT003ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCreditPMT003ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				java
					.awt
					.GridBagConstraints constraintsstcLblEnterCredit =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterCredit.gridx = 1;
				constraintsstcLblEnterCredit.gridy = 1;
				constraintsstcLblEnterCredit.ipadx = 46;
				constraintsstcLblEnterCredit.insets =
					new java.awt.Insets(25, 25, 5, 75);
				getFrmCreditPMT003ContentPane1().add(
					getstcLblEnterCredit(),
					constraintsstcLblEnterCredit);
				java.awt.GridBagConstraints constraintstxtEnterCredit =
					new java.awt.GridBagConstraints();
				constraintstxtEnterCredit.gridx = 1;
				constraintstxtEnterCredit.gridy = 2;
				constraintstxtEnterCredit.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtEnterCredit.weightx = 1.0;
				constraintstxtEnterCredit.ipadx = 59;
				constraintstxtEnterCredit.insets =
					new java.awt.Insets(6, 74, 8, 79);
				getFrmCreditPMT003ContentPane1().add(
					gettxtEnterCredit(),
					constraintstxtEnterCredit);
				java.awt.GridBagConstraints constraintsbtnOK =
					new java.awt.GridBagConstraints();
				constraintsbtnOK.gridx = 1;
				constraintsbtnOK.gridy = 3;
				constraintsbtnOK.ipadx = 12;
				constraintsbtnOK.insets =
					new java.awt.Insets(8, 76, 23, 77);
				getFrmCreditPMT003ContentPane1().add(
					getbtnOK(),
					constraintsbtnOK);
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
		return ivjFrmCreditPMT003ContentPane1;
	}

	/**
	 * Return the stcLblEnterCredit property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEnterCredit()
	{
		if (ivjstcLblEnterCredit == null)
		{
			try
			{
				ivjstcLblEnterCredit = new javax.swing.JLabel();
				ivjstcLblEnterCredit.setName("stcLblEnterCredit");
				ivjstcLblEnterCredit.setText(TXT_ENTER_CRDT);
				ivjstcLblEnterCredit.setMaximumSize(
					new java.awt.Dimension(70, 14));
				ivjstcLblEnterCredit.setMinimumSize(
					new java.awt.Dimension(70, 14));
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
		return ivjstcLblEnterCredit;
	}

	/**
	 * Return the txtEnterCredit property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEnterCredit()
	{
		if (ivjtxtEnterCredit == null)
		{
			try
			{
				ivjtxtEnterCredit = new RTSInputField();
				ivjtxtEnterCredit.setName("txtEnterCredit");
				ivjtxtEnterCredit.setText("0.00");
				ivjtxtEnterCredit.setForeground(java.awt.Color.red);
				ivjtxtEnterCredit.setAlignmentX(
					java.awt.Component.RIGHT_ALIGNMENT);
				ivjtxtEnterCredit.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				ivjtxtEnterCredit.setMaxLength(7);
				// user code begin {1}
				ivjtxtEnterCredit.setInput(5);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtEnterCredit;
	}

	/**
	 * Called whenever the part throws an exception.
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
	 * handles ok event
	 */
	protected void handleOK()
	{
		if (!startWorking())
			return;
		try
		{
			clearAllColor(this);
			//Validation
			double ldCredit;
			Dollar laDollarCredit = null;
			String lsTxtCredit = gettxtEnterCredit().getText().trim();
			//number validation
			try
			{
				if (lsTxtCredit.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					ldCredit = 0;
				}
				else
				{
					ldCredit = Double.parseDouble(lsTxtCredit);
				}
			}
			catch (NumberFormatException aeNFEx)
			{
				displayError();
				return;
			}
			//range validation
			if (ldCredit >= MAX_CREDIT)
			{
				displayError();
				return;
			}
			//decimal validation
			int liDotIndex = lsTxtCredit.indexOf(DOT);
			if (liDotIndex != -1)
			{
				int liStringLength = lsTxtCredit.length();
				if ((liStringLength - liDotIndex - 1) > DECIMAL_NUMBER)
				{
					displayError();
					return;
				}
			}
			laDollarCredit = new Dollar(ldCredit);
			//credit limit validation
			if (laDollarCredit.compareTo(caDollarMaxCredit) == 1)
			{
				RTSException leEx = new RTSException();
				leEx.addException(
					new RTSException(143),
					gettxtEnterCredit());
				leEx.displayError(this);
				leEx.getFirstComponent().requestFocus();
				return;
			}
			//Add data to caCompleteTransactionData    
			if (cbContainsCredit)
			{
				if (ldCredit == 0)
				{
					//remove credit
					cvFeesData.remove(caFeesDataCredit);
				}
				else
				{
					caFeesDataCredit.setItemPrice(
						new Dollar(
							CommonConstant.STR_ZERO_DOLLAR).subtract(
							new Dollar(ldCredit)));
				}
			}
			else
			{
				if (ldCredit != 0)
				{
					//populate FeesData
					MFVehicleData laMFVehicleData =
						caCompleteTransactionData.getVehicleInfo();
					if (laMFVehicleData != null)
					{
						RegistrationData laRegistrationData =
							laMFVehicleData.getRegData();
						if (laRegistrationData != null)
						{
							int liRTSEffDate =
								laRegistrationData.getRegEffDt();
							AccountCodesData laAccountCodesData =
								AccountCodesCache.getAcctCd(
									CREDIT_CODE,
									liRTSEffDate);
							//shows to user if account code not found
							if (laAccountCodesData == null)
							{
								RTSException leEx = new RTSException();
								leEx.addException(
									new RTSException(
										RTSException.FAILURE_MESSAGE,
										TXT_ACCT_CD_NULL + liRTSEffDate,
										TXT_ERROR),
									gettxtEnterCredit());
								leEx.displayError(this);
								leEx.getFirstComponent().requestFocus();
								return;
							}
							caFeesDataCredit = new FeesData();
							caFeesDataCredit.setItmQty(1);
							caFeesDataCredit.setAcctItmCd(CREDIT_CODE);
							caFeesDataCredit.setItemPrice(
								new Dollar(
									CommonConstant
										.STR_ZERO_DOLLAR)
										.subtract(
									new Dollar(ldCredit)));
							caFeesDataCredit.setDesc(
								laAccountCodesData.getAcctItmCdDesc());
							caFeesDataCredit.setCrdtAllowedIndi(
								laAccountCodesData.getCrdtAllowdIndi());
							//add fee to vector
							cvFeesData.addElement(caFeesDataCredit);
						}
					}
				}
			}
			getController().processData(
				AbstractViewController.ENTER,
				caCompleteTransactionData);
		}
		finally
		{
			doneWorking();
		}
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
			setName(FRM_NAME_PMT003);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(216, 134);
			setTitle(FRM_TITLE_PMT003);
			setContentPane(getFrmCreditPMT003ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCreditPMT003 laFrmCreditPMT003;
			laFrmCreditPMT003 = new FrmCreditPMT003();
			laFrmCreditPMT003.setModal(true);
			laFrmCreditPMT003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmCreditPMT003.show();
			java.awt.Insets insets = laFrmCreditPMT003.getInsets();
			laFrmCreditPMT003.setSize(
				laFrmCreditPMT003.getWidth()
					+ insets.left
					+ insets.right,
				laFrmCreditPMT003.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmCreditPMT003.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	/**
	 * Process KeyReleasedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getController().processData(
				AbstractViewController.CANCEL,
				caCompleteTransactionData);
		}
	}
	/**
	 * Sets the Data on PMT003
	 * <p>The aaDataObject will be of type CompleteTransactionData.  
	 * It will loop through FeesData to find out if a credit exists, 
	 * and display it in the textbox.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		caCompleteTransactionData =
			(CompleteTransactionData) UtilityMethods.copy(aaDataObject);
		cvFeesData =
			caCompleteTransactionData.getRegFeesData().getVectFees();
		caDollarMaxCredit = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		for (int i = 0; i < cvFeesData.size(); i++)
		{
			FeesData laFeesData = (FeesData) cvFeesData.get(i);
			//add the item price to maxcredit if creditindi is 1
			if (laFeesData.getCrdtAllowedIndi() == CREDIT_INDI_ON)
			{
				caDollarMaxCredit =
					caDollarMaxCredit.add(laFeesData.getItemPrice());
			}
			//Find the current credit amount
			if (laFeesData.getAcctItmCd().equals(CREDIT_CODE)
				&& (!cbContainsCredit))
			{
				caFeesDataCredit = laFeesData;
				gettxtEnterCredit().setText(
					new Dollar(CommonConstant.STR_ZERO_DOLLAR)
						.subtract(laFeesData.getItemPrice())
						.toString());
				cbContainsCredit = true;
			}
		}
	}
}
