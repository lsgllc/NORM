package com.txdot.isd.rts.client.misc.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPrintDestinationCTL009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Salvi		10/01/2002	Made this frame modal.
 *							defect 4805
 * B Tulsiani	04/26/2002	Changed SetSelectedRow to RowSelectIntv
 * K Harrell   	05/29/2002  Altered CashWsId to WsId
 *							defect 4143
 * K Harrell	01/14/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							Sort workstation list for set print dest. 
 *							modify setData()
 *							defect 7813  Ver 5.2.3
 * J Zwiener	03/01/2005	Java 1.4
 * 							defect 7892 Ver 5.2.3
 * K Harrell	04/28/2005	Tabbing work. Comment out focusGained
 * 							deprecate focusGained() 
 * 							defect 6453  Ver 5.2.3
 * K Harrell	06/21/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3        
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Jeff S.		11/03/2005	Remove sort of assigned WS ids since it is
 * 							done in the getter of Assigned WS ids.
 * 							modify setData()
 * 							defect 8418 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * Screen presents list of selected print destinations
 *
 * @version	5.2.3			11/03/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmPrintDestinationCTL009
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener // KeyListener,
{
	private static final String TXT_CNFRM_CHNG =
		"Are you sure you want to change the print destination?";
	private static final String TXT_CURR_DEST = "Current Destination:";
	private static final String TXT_NEW_DEST = "New Destination:";
	private static final String CTL009_FRM_TITLE =
		"Print Destination  CTL009";
	private static final String TXT_LOCAL_PRINTER = "Local Printer";

	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjlblCurrentDestination = null;
	private JLabel ivjlblNewDestination = null;
	private JLabel ivjstcLblCurrentDestination = null;
	private JLabel ivjstcLblNewDestination = null;
	private JPanel ivjFrmPrintDestinationCTL009ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblPrinters = null;
	private TMCTL009 printersTableModel = null;

	// Vector 
	private Vector cvPrinters = null;

	/**
	 * FrmPrintDestinationCTL009 constructor comment.
	 */
	public FrmPrintDestinationCTL009()
	{
		super();
		initialize();
	}
	/**
	 * FrmPrintDestinationCTL009 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmPrintDestinationCTL009(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmPrintDestinationCTL009 constructor comment.
	 *
	 * @param aaParent JFrame
	 */
	public FrmPrintDestinationCTL009(JFrame aaParent)
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
		//Code to avoid clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{ //field validation
			clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				int liRow = gettblPrinters().getSelectedRow();

				//Prompt user to verify they want to change destination
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						TXT_CNFRM_CHNG,
						null);

				int liRetCode = leRTSEx.displayError(this);

				//Update cache with new print destination
				if (liRetCode == RTSException.YES)
				{
					AssignedWorkstationIdsData laAWSIDData =
						(AssignedWorkstationIdsData) cvPrinters.get(
							liRow);
					int liOfcIssuanceNo =
						SystemProperty.getOfficeIssuanceNo();
					int liSubStaId = SystemProperty.getSubStationId();
					int liWsId = SystemProperty.getWorkStationId();
					int liRedirPrtWsId = laAWSIDData.getWsId();
					AssignedWorkstationIdsCache.putAsgndWsIds(
						liOfcIssuanceNo,
						liSubStaId,
						liWsId,
						liRedirPrtWsId);
					getController().processData(
						AbstractViewController.ENTER,
						null);
				}
				else
				{
					return;
				}
			}
			//If user presses Cancel
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			//If user presses Help
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.CTL009);
			}
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx1 =
				new RTSException(RTSException.FAILURE_MESSAGE, aeRTSEx);
			leRTSEx1.displayError(this);
			leRTSEx1 = null;
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * 
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88G69FAC1ADGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BB8BD0D4D716D6ADC91C0A491A0A1BE292375629B29BF6065D18ACC9CC4AC9B4EBF6190959E0C693D3519A5DE42A98E232E6C2E61C992B72E81A2665E3D07107282828205861A7A2C1A350A01F66A334C014088D7D04272FBF69FEAD689259F36FFD67756B676B76B3256529776E39771CFB4F39671E4FFD8D173BE7D6DA0AA11763D252B8795F71940ECB28663852D97FD50F387EBDC986CE75EFAF40
			C64ED8EA0467A874F575A599F238CDDB4D501E00F6C9EF12E19B3CEF663E58B33B8ADE424CB381FD9B262DFBA072BCBFEB40F952E9FBE7FF96BCB7GCC815CEC00B5FB487C957D5BE5FCBC34B7486F84D2006B0C82B266793E6DAAFCE5819BB7C5FE966CA5E3BA7A73657E6500A7E312814CFB03BC575355BDD0D3AD37879E7B00CB7E75CCC3B619CB459FE1D4261DA6BF8B38C283D1C4AAA7241C06E7ED575511D3439E17C59AF7FAA40B60F7DAFC125FB2AC8A3CDB3238847F30A5A05454B479DCDDBE41ADD9F9
			3FA4389D1260F1B758ED2525A75651F5AF6BB3D31E9C37905AEC0D0B33EB65B54C387A81D79458E38A3746537B585A1C7B02EBDC7B50BCD36EFE42FB0C6A1C504D067E65GEC4E577AF14EFE98370C0E7D38170C1D8E6FB1G737763DACE9E4235747FDF9237F668B543D7E15612DF4856E0AD8C2E655279B7CCBB687E9FEDA4FBB8857AE6834C85D88E308EA07FC012E1A7353B440643702C9B129ABCAE1747DD6775560BCEDF3B47490B70DED383BDB22E1B9F76781C9C47ECEBC5DF094EFA94FBB990EAD7C1DB
			68635096E23BD7F176178ACCE9BA32CDBCDEE0EA16F98C9C884A46647AD59311EB827051GF3G628192814C87D12EF2EB456D64EAF1CB3C4F6D90FB82C3DE07CF2A8F8822137749727D589FC9BEBDE0F48F5FB59D53CBCC05DAAF4B7FFDBEBD43DA3DAC0FF95AD0F810692376E0D0AFEC6E05E1679EB8F86BFE5073FC105861E09F31C3AF3C2748B64A708245AFD461190DFFC66D163417C0DF75A1345B7E8DA8034D38144BA9DB20ABC3746BAF9AE69EE26B09BB245D5B271B09CFD98978F5G39C560A381ECG
			23G93C5383FBFDA3F3EC33B6DD26DAD232DEA5C8ACFFEDC72B9DA5CFE49619E66FB9DBE41B1A472FECE9E73EDA3D97BD0881D151E27E118638C6F66FD542FGF7070B87C37233B16D0D9B49989FCFFB6D028BDC10436565106F39464D70949DFE691C576910786088567FFDA35165B5C7AFF08486703E280868BC197A2C1970BE9700696A1D96024BA9CEB2949523DE16B5EE02E79B98B6AF5654D805E1328C076FBA4E7FCE9371A57CB51EF8C7AF6FF6420BDF6EFB3FE9FDD0AF17BD4A1AFFCB71640CB440427D
			8A7EF7CD9B999EF828350C7D1F50FE07A47904210044777092F86311A07D97F4AD7E1063676764B35ECAEC2C96643283B89579EE1E592C124F6671DC8DF86DE467E4BDFDC07D6CB80C1FDE0CF1106FA3F1B12E986D7453FE34D3EB76CA2EE469EB3AF6BA7A53825372E2E61F49455A336621BC03F37C29377FAE4F3D96620A754FE8503764747D2DF324C668E3FABFE3A3FA4FBF8C679A207FB06A7D65B3856C3C55BB7CBC1E30CE9F6C986F8CDA999B7BECF3F89BFB31D95F4666B56B5B586B4D11ED68574DFAB6
			447A96B713586299F32BEC725D66CD748C3B5DBCDD34C24231BC63D3BB5105609624BE41B942CBBD428D9EFC56E1E2DB1954363C709EF598EDE9ECBF5AD2610D655C7C41A78DFA71AC7A69178D4B8F337DDDF3B8E8CBECCD6F35901D73A26F82C5B5F882EE0961D75073AD63E57F72F62B7E58252D449E2F725765F16F371AB862C744G2F48B64711C9566F93DC30D564404A395E9F7A5539DE4E91082710975A61B98A40E89FAEA07691B631D26CE7CABBD95F94201B8190FB846D67D25B8E5539158D2741A30A
			EC9B147DE93765107D64FF887060164941EED3E4325BB6E97AFAE579D87F000D48A72AFC4242A3C43EFDCA6EC5E286431D20381F4241FA4408B138BDC71D27BA7D4F6B6EA581F3895B437C92ED7CFA2103702D853C2D84E5FF4F2676D9F5FE3FE7D8203A137745E4A429FF645CF604C69C1126375D769E2B6090BDA3751EF10EE3796540F7E659CFDCAA417CF26E85329751501E83902F2C674176DB6D6A61768CF8DE96FCFE480B1C7CB86A7431F6229B922FE4B466F165EADD4D1E231F4791D8A96BA87996DDE5
			DD23716BE8122170A82EAD2BE323CAD710F3C2F89E011D44753C52114A6CDCEE3F5A1106F64E211FF4FC97A9872BBE4A56E1131F176049721D128122AF6928B68F2A227828235ABC68558B18874D003E35C7B1AE0F0C913986ACC9865102F2B98359AA393ABCF0089419B6DEA3E7556393C684B76236D29CE45C54F93158B6CE77F882F84EGE038BF1DA73839242D60CEDFA0381550CE36E0FCAA9FA07E017B9A5B3534DD293497DE78DBE3CBB8884E39623E4D112A68D29CA3FBB42E343D348D690FFC561E9BA3
			BE62B2A4ED90C6FA3C7CB0377AEB22333F7F8EFDE0BF34C500E9G31G0BGD2A92F8D97888F1747699061E9ADA5F487AE20EF29AC45F1FDF41CA4C854EE271612DA7DE744F5F12C1F638EF091FCE903878A9312D13979B28E6BF157005AC7AFAAE5794D9AF89A4B3046FC015A8711B79FF076B2027B703BDC0D1ED641G8BBCD97F16736AFEF2760AB9F57F1910FEF2764F252A7B5F8B69CFD351B3BF7166812CE5573D79A1A73E70EFE1EB7F19E56CBC4E171F1726ACCD09DB35BF03692C768231D7499760995F
			D9E3EA7FD31D919B56D72F1579E515336722671626B09EEFDFD07348F97289FA3E75F8D8E55A2EF26D3D446745E4DF286F3E623866983778474030EAEADA497609ECDF1E9EB04B7A08AAC75F5CDFCEFC47BCE8AF81D8DD0EE76415083ECB8F18AC8B52D14FF12F2C6066BE68B6E4061145D841E4A8141FEBEFAECDA1CF663B1E4AA57258214F8BB083A096E0BE40328A74E51FEDA2EB047C2F51A9C8F51264989E2511845342DE019F8B2249A1E33CDF5DC274309A7818AB11D77566A3706CF5DC112BB542526323
			1CG596573F8F91FF41DE1E934175779B2A96164B3FEF37301F0BCE0B52D30AD1DC3D7A012589DA39C46081F8FE875DCD4994C3FEB613D8B40294231D810C077ED3C124502EC857FDB0A1F29423398F11162C9FBBE74159C439831F89BB9B751500E83D886309A002BC23DAC583C0F69D8C73F0D10180213A0152B04BA181D9BBE677EC56E6D6B5D38DC927F07BC1264F1917ECA3C4DA57A12BCDE0AE43857F2094DC1389A1558D0997FC6AE59DF113FCC518C779FDB086E147C1DEDA1666EDB088EC82E8D590CF0
			033A409E98689EE1F1EC81BDB779201352AA0CEDFF3CC8F0F6E80B8A6EE49541CD01F63402BBD80E31B2368A4F563B65443693201D8C10FB9CEA9C00FEG6FF19C73BBBAE6AA34E3G628192814C5530F755B846D9FEC967DC91BE780EF3D44B3C911FFBBCFC0D2785B657246EDC192AF7629F53E4BF102D213F5339641AE1DBF89BDA396D76B6747BEDB42F043D37DF774A7B7F69B61A5BDC3E6C6795DB7DFCDB0E02EB71F7F97C8259F96667178D107122633AA7G6397D6133D7CDFA57659206D840886088528
			D4E2F376A051094BB1AE3882AEDA344971F9102CA96BC412E17B899CEB2F22E34170C2470A540E6CB06E9240349354364A37C7509B46F3B246AF8D5B064406004F470784F59C132B0C696047F47A3790397598286659484604B2D07AD7ECA53E2635E75887F595CD9F100E606B83F0365DB4AC15E5559059F6D628E3771293CC1FEB61497ABFEE536B3757E07FC5CE2FFFD4693F112E57BF81FDAC273199553A1DDE437A6741B3C1611117256611A80FA979864EAF752FEFFD0F776D2781B77E8D562BA37BC8CCD0
			EAAAE148E589DCF3D99CC312050770D0D3539190C51BECE6F28D3565A266CFF33E41B85DF01170CF04B6D78BB6D40BF57F78C10C37317F3C142B9C537FDE30527E0C2134161DF9FBEDB0F7104E3FE1E2F961F09E276C1F250A64CDEEFE2C002492964718E4318A607949C5E0D1F9D965714A6A0A2A4A63A5854177E24CAF9F5C996433333CA0BC1FE3A70E81034A72E2A647152BA8C77EAF5E25EB8B171FCD17579F271283F3BE4803AE074903EC0EA14C033AABB08FCAD67478E3137A3B4B3C2F1E891B37141C14
			6B361341798BC10F0C77BE9B721EF6927361FF609037D041BD170E38F5A795DBE95C253F7660C9D2ECA44A08F5F665A9341107CC44C6C4E8C7814CBC05FCCF9DB9465DCDDD2FBA214AFCE7334C320E63CEE17C794D777776EDE255BD5265DD3CDBBA16933DC44F4E6AD3C9CADE4D7073A9FE2D8A4F4EF24BC5BC4B46EF410FFC0B3992370B5856A2E8A782F027619C82D48358CFE33EB4F5FBA9F71B6F81F60F578641CDD4DDD5B37E67F7937E2340AB8AA086E0BE409200558A7F37F6126FC9617254EE0F0756EB
			24F4F3883023B4BD73B9C8154FF458D72127C32E0E59F2AEBC0BGEA6B027981DBDBFC9E71FFDD00978122GE6824C2B43F56D483F6DF72E4671885F398E59745635DCDE479AF89A6BC15F7541F5B13AA7B9BD3A92F95CE9F85A81C6EF21FBB1DD0FEEC29EB79D1E3381665EC27793A352AD023E157521F9D4F27D2DF99533353F5233E9EE505E692425937C76866D1D4EA3A63C53A9023EE98DF866FA33A21D01B5B04EE88DDD83237B65C5CEF97FCF55FB67457BF597921CFB743E4E416CEF7D2E70B9E34EAE7D
			3B7E2D3B746F7A73F7C53E6BAF5C957E2E7F482EDB6F7A07F707DF5B155D374FE77D3B75EB226B3BEF2D09EE6E562F0922F62BEBA2D61B38E99C5C8E76D2E445FAA52D926B1ADA85B7729556B5DD8AEEEEAB56B5CEAB7A00043C70F23E17273F87AB72747760433C485F74BE4E8B37C7ECFD27ECA85BD4E54D89B614ED2602BB3B97E51B23600ECC22EC8B955CF39C72CBD4F01BD210DF3202FBD829814D0D08BBE3C2FE798A6E2DF464F7DA41A5E7A0BF1B02335A115FA58537A39579C98AEE964D872221BDCF41
			7D196292203DD641595310B62B89FDCACABA3953AAD55A19345D2C349FB75D6B79A5BC6F4CC7445E739C113EA5A2B059DDD9A83B58047AF8B49B75BB3D896B3287AA689D9F98AB2B471E2267E4B6744F81E0B88BBDA7CB203DD241CD2767A46B8C44A9GF61F7BF6AB5696D64075830C83C4834CBD03F4A354AE17C17BA38577E70AB3B643FBB362EED0DB2D05F6F3B3722F1FC47E9260268244G4483A412B64D498364278CF2ED798D35D9F38B54ADAD48B70E5A6CE9E85BDA106FC593729D875CB400D800C524
			3F85790E5587796E4A243237026C2D4857C5ED578A6D2ED664DB1A0AFCA7GB79DE08EC082401AD6547FCBC672DB34EB3CCF9206A1DD2049EF3D4357659105616B72FC6FD05BBE8A105306F4B9E9A4D7997578049BA47F39059257DAC24F40C9002EB6DC5755B4DC578C4045G4483A48218ED485FDFC6EA82E9C82477654467337A31B6DDDDBF965958BD4DA8BCA7EC48BFAACB618F389800F800C4B2369D7575923545A2E8D736230E1FEC60B82C2756D510D831CF2E4C8BD425F74DBE2C3FFEE98D0E7FE3769E
			AEEC7DD5D6D6D1D2D0D6D9AC53FD5198247B5756BB21E3F541972DD82BDEEA473B7E7DED5A3BFE424F4261DD7D39B66DDD7F11107E075A34F77D1F07743F5C263D6B2F137B19AC1EB3C1D96269375AFD9DA00C2E3EE21A03E31B766E8EAFB7A9D70958B25D1BADC13A0249BB21E37A9A1DC4FDC5AB7AFAE2B2323E62A7357A9A89697F4B24D6DF0BC37ACB38487A7A34B5A84B142A8832D4066AE023ADC8D7EB3A93BA39F6B5218E92949DBCEF0A2C030FCC11EDE6EB3AD687BDA15F171A5335BA882428F5D0599E
			1465047D4EF5505791245B10FA67BA68CDC59D18BBD087D12911F570E2EAE4BBC8CC0D2C03BC23D687E3F2BFDB7F6F0D64BB1F6A6A41223AF93074B6F65BDB9A6AEC03BD8D5D1DB6DBFDDD77E01D2D2F6E3B9E053EA12DAA82FDF3E7F74B794E8EFBA8071E410E46C6EB23957572D89A6A65342217575234FAF9B6C46E4F5234FA7947107E7DA62DDE9698C27C11C92B1793B2BD3BBFF9A6D04E5D656FA2584FBEE419A41A6B117B2C718E3C2B9949A4755DF2E8A783E4F5623D504F33D07E42CE14FFD916D6FECF
			08FC9959DA7907C3763DB6DBABFFEA087C6EEC2D7C7F92629FDE3C9A34717F6E0D909B4A4B0E1794D49CAB16F33A3F345E5BBD0FEB725E68FEE23AB73A8B2977C677C45A3D51DDCFA7396EG6C2F37D3BFBF0E82FC345C979BE68C0179BAFD6CDE6183EADFCBBA35F7DE9FD17C4ACE6D1D17AB936F3CD6C3DFD797667E4FC63CEF508316D7BFC5EDBE8E78A4F4E12E3D0662BE0276DA8567D9CF3F2D1D05FA73AC6266D1DC97343D8AEEBD2D0D224E32F5B35C618C655B5AD9DC6FAA122249FACED23D7BB36F7FBD
			47347BBA87BBFF4F2713F7C7C072F473FE6186CFEE58B02FFDB39D6F04934EE24E1AEE16FFABC26FF259F72C5DE64CC54D5DC906820081GAFC033D283BD381E78917F28E72CD74E43ED021BDCC948F34DDA0FFEEAEAB7527CE67DE61926D9494199952379C42119295094E4644972B4105FAA738D9EB160F27B1BFC9ED7BBDBB44B1F6BB2F04DF300F6A140C7G461EA4C3E18F727A1FF472B784A171077AE1CB5DB9FB67E0F7E3CF4B7946C1DBDD0FFD302153F62E3D83ED3F490CF7EDD660B5510377661FEEA2
			3F533B433F7530374B3FC0E4FE7CD1B34A9A5703BEFF9E1DE7B5340D76A4C33EDD29F7561379AEBBC4BF2F6C33B1C30DE3BCC7B2708E3E520E31611FB234316131905F3FB8C39B9B481A027D29195A5890CAFFACD39B9B101E593C4DFCBB7FA05A756EA34F5F16EE376E99FB7C36F47A7E30F4BD517DD43B569F1E207899F62DBFFC5D0C7EF09674994F21FF91B354730F82FE62DC64BB0EDDF4DF66EFA671DFF00B82391D273F5CD7E42A221F20587B372A77F8737D77D92B6E6B9C4CCF7F9D355B6920A7E2DB8C
			B71562E2E49CBBEF0E03D91C72BB0CBE41A90DB2FC7EF8F6905F4C133BCDE8333FD77B51329D68E3BF6152A4EE872D4B55F5DEC181BB14FE9F376956FC256E51775F0B9D34669AB8C38BE79A250CDB3834B1B27338F7724BAF3FA4607F013BC98D618F9CE7680119697FDC3DAADC99CAE47FA134A5F1B95AA2B4E440E0045651FBE86D87F81FFD5BAC1ED47F3EEC6D830F047D0D5A0CBEE64BB1FD5A5F487E351446EE40F37DF85F5BD6CA6EDF427D5E29476590C508GD76B5CCE725B277A6B929F3C5F654A88FF
			BF0E11BBD9DFFA99D97795954D7F83D0CB8788F412350DB594GG78B8GGD0CB818294G94G88G88G69FAC1ADF412350DB594GG78B8GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGEF94GGGG
		**end of data**/
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(84, 177, 257, 55);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// defect 7892
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7892
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the FrmPrintDestinationCTL009ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmPrintDestinationCTL009ContentPane1()
	{
		if (ivjFrmPrintDestinationCTL009ContentPane1 == null)
		{
			try
			{
				ivjFrmPrintDestinationCTL009ContentPane1 =
					new JPanel();
				ivjFrmPrintDestinationCTL009ContentPane1.setName(
					"FrmPrintDestinationCTL009ContentPane1");
				ivjFrmPrintDestinationCTL009ContentPane1.setLayout(
					null);
				ivjFrmPrintDestinationCTL009ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmPrintDestinationCTL009ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(595, 242));
				getFrmPrintDestinationCTL009ContentPane1().add(
					getstcLblCurrentDestination(),
					getstcLblCurrentDestination().getName());
				getFrmPrintDestinationCTL009ContentPane1().add(
					getstcLblNewDestination(),
					getstcLblNewDestination().getName());
				getFrmPrintDestinationCTL009ContentPane1().add(
					getlblCurrentDestination(),
					getlblCurrentDestination().getName());
				getFrmPrintDestinationCTL009ContentPane1().add(
					getlblNewDestination(),
					getlblNewDestination().getName());
				getFrmPrintDestinationCTL009ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getFrmPrintDestinationCTL009ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjFrmPrintDestinationCTL009ContentPane1;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
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
				ivjJScrollPane1.setBounds(28, 78, 352, 87);
				getJScrollPane1().setViewportView(gettblPrinters());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblCurrentDestination property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCurrentDestination()
	{
		if (ivjlblCurrentDestination == null)
		{
			try
			{
				ivjlblCurrentDestination = new JLabel();
				ivjlblCurrentDestination.setName(
					"lblCurrentDestination");
				ivjlblCurrentDestination.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblCurrentDestination.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblCurrentDestination.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblCurrentDestination.setBounds(186, 21, 169, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblCurrentDestination;
	}
	/**
	 * Return the lblNewDestination property value.
	 * 
	 * @return JLabel
	 */
	private javax.swing.JLabel getlblNewDestination()
	{
		if (ivjlblNewDestination == null)
		{
			try
			{
				ivjlblNewDestination = new javax.swing.JLabel();
				ivjlblNewDestination.setName("lblNewDestination");
				ivjlblNewDestination.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblNewDestination.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblNewDestination.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblNewDestination.setBounds(187, 51, 169, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjlblNewDestination;
	}
	/**
	 * Return the stcLblCurrentDestination property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCurrentDestination()
	{
		if (ivjstcLblCurrentDestination == null)
		{
			try
			{
				ivjstcLblCurrentDestination = new JLabel();
				ivjstcLblCurrentDestination.setName(
					"stcLblCurrentDestination");
				ivjstcLblCurrentDestination.setText(TXT_CURR_DEST);
				ivjstcLblCurrentDestination.setMaximumSize(
					new java.awt.Dimension(113, 14));
				ivjstcLblCurrentDestination.setMinimumSize(
					new java.awt.Dimension(113, 14));
				ivjstcLblCurrentDestination.setBounds(32, 21, 121, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjstcLblCurrentDestination;
	}
	/**
	 * Return the stcLblNewDestination property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNewDestination()
	{
		if (ivjstcLblNewDestination == null)
		{
			try
			{
				ivjstcLblNewDestination = new JLabel();
				ivjstcLblNewDestination.setName("stcLblNewDestination");
				ivjstcLblNewDestination.setText(TXT_NEW_DEST);
				ivjstcLblNewDestination.setMaximumSize(
					new java.awt.Dimension(95, 14));
				ivjstcLblNewDestination.setMinimumSize(
					new java.awt.Dimension(95, 14));
				ivjstcLblNewDestination.setBounds(32, 51, 104, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjstcLblNewDestination;
	}
	/**
	 * Return the tblPrinters property value.
	 * 
	 * @return RTSTable
	 */

	private RTSTable gettblPrinters()
	{
		if (ivjtblPrinters == null)
		{
			try
			{
				ivjtblPrinters = new RTSTable();
				ivjtblPrinters.setName("tblPrinters");
				getJScrollPane1().setColumnHeaderView(
					ivjtblPrinters.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblPrinters.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblPrinters.setModel(
					new com.txdot.isd.rts.client.misc.ui.TMCTL009());
				ivjtblPrinters.setShowVerticalLines(false);
				ivjtblPrinters.setShowHorizontalLines(false);
				ivjtblPrinters.setAutoCreateColumnsFromModel(false);
				ivjtblPrinters.setBounds(0, 0, 200, 200);
				// user code begin {1}
				printersTableModel =
					(TMCTL009) ivjtblPrinters.getModel();
				TableColumn laTableColumnA =
					ivjtblPrinters.getColumn(
						ivjtblPrinters.getColumnName(0));
				laTableColumnA.setPreferredWidth(115);
				TableColumn laTableColumnB =
					ivjtblPrinters.getColumn(
						ivjtblPrinters.getColumnName(1));
				laTableColumnB.setPreferredWidth(115);
				ivjtblPrinters.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblPrinters.init();
				ivjtblPrinters.addActionListener(this);
				ivjtblPrinters
					.getSelectionModel()
					.addListSelectionListener(
					this);
				ivjtblPrinters.setBackground(Color.white);
				//				ivjtblPrinters.setNextFocusableComponent(
				//					getButtonPanel1().getBtnEnter());
				// user code end
			}
			catch (Throwable aeThrowable)
			{
				// user code begin {2}
				// user code end
				handleException(aeThrowable);
			}
		}
		return ivjtblPrinters;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// Uncomment the following lines to print uncaught exceptions 
		// to stdout
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7892
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7892
	}
	/**
	 * Initialize the class.
	 */

	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmPrintDestinationCTL009");
			setSize(426, 240);
			setModal(true);
			setTitle(CTL009_FRM_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(getFrmPrintDestinationCTL009ContentPane1());
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
		// user code begin {2}
		// user code end
	}
	// defect 7892
	//	/**
	//	 * Key Pressed Action
	//	 *
	//	 * @param e java.awt.event.KeyEvent
	//	 * @deprecated
	//	 */
	//		public void keyPressed(KeyEvent e)
	//		{
	//			if (e.getKeyCode() == KeyEvent.VK_RIGHT
	//				|| e.getKeyCode() == KeyEvent.VK_DOWN)
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
	//			}
	//			else if (
	//				e.getKeyCode() == KeyEvent.VK_LEFT
	//					|| e.getKeyCode() == KeyEvent.VK_UP)
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
	//			}
	//			super.keyPressed(e);
	//		}
	// end defect 7892
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param aarrargs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPrintDestinationCTL009 laFrmPrintDestinationCTL009;
			laFrmPrintDestinationCTL009 =
				new FrmPrintDestinationCTL009();
			laFrmPrintDestinationCTL009.setModal(true);
			laFrmPrintDestinationCTL009
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmPrintDestinationCTL009.show();
			java.awt.Insets insets =
				laFrmPrintDestinationCTL009.getInsets();
			laFrmPrintDestinationCTL009.setSize(
				laFrmPrintDestinationCTL009.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPrintDestinationCTL009.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPrintDestinationCTL009.setVisible(true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
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
		try
		{ //Get list of printers from cache  
			int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
			int liSubStaId = SystemProperty.getSubStationId();
			cvPrinters =
				AssignedWorkstationIdsCache.getAsgndWsIds(
					liOfcIssuanceNo,
					liSubStaId);
			//Set local printer from the list of returned printers
			for (int i = 0; i < cvPrinters.size(); i++)
			{
				AssignedWorkstationIdsData laAWSIData =
					(AssignedWorkstationIdsData) cvPrinters.get(i);
				// defect 4143
				// if (SystemProperty.getWorkStationId() == 
				//							laAWSIData.getCashWsId())
				// end defect 4143
				if (SystemProperty.getWorkStationId()
					== laAWSIData.getWsId())
				{
					laAWSIData.setCPName(TXT_LOCAL_PRINTER);
				}
			}
			//Set current destination of the workstation
			for (int i = 0; i < cvPrinters.size(); i++)
			{
				AssignedWorkstationIdsData laAWSIData =
					(AssignedWorkstationIdsData) cvPrinters.get(i);
				// defect 4143 
				// if (SystemProperty.getWorkStationId() ==  
				//							laAWSIData.getCashWsId() )
				// end defect 4143
				if (SystemProperty.getWorkStationId()
					== laAWSIData.getWsId())
				{
					for (int j = 0; j < cvPrinters.size(); j++)
					{
						AssignedWorkstationIdsData laAWSIData2 =
							(
								AssignedWorkstationIdsData) cvPrinters
									.get(
								j);
						// defect  4143 
						// if (laAWSIData.getRedirPrtWsId() == 
						//					laAWSIData2.getCashWsId() )
						// end defect 4143
						if (laAWSIData.getRedirPrtWsId()
							== laAWSIData2.getWsId())
						{
							getlblCurrentDestination().setText(
								laAWSIData2.getCPName());
						}
					}
				}
			}
			//Add list of printers to table
			// defect 8418
			// Sort moved up to the getter in WS IDS cache.
			// defect 7813
			// Present list of workstations in order by wsid 
			//UtilityMethods.sort(cvPrinters);
			// end defect 7813 
			// end defect 8418
			printersTableModel.add(cvPrinters);
			//Loop through list of printers to find the row number of 
			//the current destination
			int liRow = 0;
			for (int i = 0; i < ivjtblPrinters.getRowCount(); i++)
			{
				if (getlblCurrentDestination()
					.getText()
					.equals(ivjtblPrinters.getValueAt(i, 1)))
				{
					liRow = i;
					break;
				}
			}
			//Set row selected to the current destination
			gettblPrinters().setRowSelectionInterval(liRow, liRow);
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx1 =
				new RTSException(RTSException.FAILURE_MESSAGE, aeRTSEx);
			leRTSEx1.displayError(this);
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx1 =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTSEx1.displayError(this);
			leRTSEx1 = null;
		}
	}
	/** 
	 * Called whenever the value of the selection changes.
	 *
	 * @param aaLSE ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		int liRow = gettblPrinters().getSelectedRow();
		getlblNewDestination().setText(
			(String) ivjtblPrinters.getValueAt(liRow, 1));
	}
}