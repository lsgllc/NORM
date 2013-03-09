package com.txdot.isd.rts.client.common.ui;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmItemNumberNotFoundINV003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking() 
 * N Ting		04/29/2002	Fix CQU100003704.  Change setData to show 
 * 							inventory item year from ProcessInventoryData
 * 							object
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 * 							user help guide so had to make changes
 *							in actionPerformed().
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify actionPerformed(),handleCancel(),
 *							setData()
 * 							Ver 5.2.0	
 * T Pederson	03/16/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3
 * K Harrell	04/28/2005	Renamed from INV014 
 * 							defect 6966 Ver 5.2.3  
 * B Hargrove	05/27/2005	Help \ User Guide changes for Subcon.
 * 							see also : RTSHelp()
 * 							(fix merged in from VAJ where this sceen
 * 							 was named INV014 - defect 6966)
 * 							modify actionPerformed()
 * 							defect 8177 Ver 5.2.2 Fix 5
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * S Johnston	06/27/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3  
 * T Pederson	07/22/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/06/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Frame Item Number Not Found INV003
 * 
 * @version	5.2.3			10/06/2005 
 * @author	Nancy Ting
 * <br>Creation Date:		11/20/2001		
 */

public class FrmItemNumberNotFoundINV003
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkReuseVoidedInvItm = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel11 = null;
	private JLabel ivjlblAllocatedPerson = null;
	private JLabel ivjstcLblAllocatedTo = null;
	private JLabel ivjlblInvItmCd = null;
	private JLabel ivjlblInvItmCdDesc = null;
	private JLabel ivjlblInvItmNo = null;
	private JLabel ivjstcLblInvMsg = null;
	private JLabel ivjlblInvYr = null;
	private JPanel ivjRTSDialogBoxContentPane = null;

	//Object
	ProcessInventoryData caCurrentProcessInventoryData;
	SubcontractorRenewalCacheData caSubcontractorRenewalCacheData;

	// Text Constants 
	private final static String FRM_NAME_INV003 = 
		"FrmItemNumberNotFoundINV003";
	private final static String FRM_TITLE_INV003 = 
		"Inventory - Item Number Not Found   INV003";
	private final static String TXT_REUSED_VOID = 
		"Re-used Voided Inventory Item";
	private final static String TXT_ITM_ALLOC = 
		"The above item already allocated to:";
	private final static String TXT_INV_NOT_FOUND = 
		"The Inventory Item Number cannot be found.  " + 
		"Confirm the Item Number:";
	
	/**
	 * FrmItemNumberNotFoundINV003 constructor.
	 */
	public FrmItemNumberNotFoundINV003()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmItemNumberNotFoundINV003 constructor.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmItemNumberNotFoundINV003(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * FrmItemNumberNotFoundINV003 constructor.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmItemNumberNotFoundINV003(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * Invoked when Enter/Cancel/Help is pressed.
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
			clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// PCR 34
				//if (caSubcontractorRenewalCacheData != null) {
				//	if (getchkReuseVoidedInvItm().isSelected()){
				//		caCurrentProcessInventoryData.setInvLocIdCd("V");
				//		caCurrentProcessInventoryData.setInvId("0");
				//	}
				//	caSubcontractorRenewalCacheData.setINV014ProcessInventoryData(caCurrentProcessInventoryData);
				//  if (caSubcontractorRenewalCacheData.getBtndisplay()
				//        == SubcontractorRenewalCacheData.PLT_STKR) {
				//        if (caSubcontractorRenewalCacheData.getINV014CurrentInvType()
				//            == SubcontractorRenewalCacheData.STKR) {
				//            caSubcontractorRenewalCacheData.setNextVC(
				//                VCRegistrationSubcontractorRenewalREG007.DONE_INV);
				//         } else {
				//             caSubcontractorRenewalCacheData.setNextVC(
				//                 VCRegistrationSubcontractorRenewalREG007.VALIDATE_STKR);
				//         }
				//     } else {
				//         caSubcontractorRenewalCacheData.setNextVC(
				//             VCRegistrationSubcontractorRenewalREG007.DONE_INV);
				//     }
				if (getchkReuseVoidedInvItm().isSelected())
				{
					caSubcontractorRenewalCacheData
						.getINV003ProcessInventoryData()
						.setInvLocIdCd(InventoryConstant.VOID);
					caSubcontractorRenewalCacheData
						.getINV003ProcessInventoryData()
						.setInvId(CommonConstant.STR_ZERO);
				}
				// End PCR 34
				getController().processData(
					AbstractViewController.ENTER,
					caSubcontractorRenewalCacheData);
			} // Enter
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				handleCancel();
			} // Cancel
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				// defect 8177
				if (caSubcontractorRenewalCacheData.getSubconDiskData()
					!= null)
				{
					// diskette entry chosen on REG050
					RTSHelp.displayHelp(RTSHelp.INV003A);
				}
				else
				{
					// keyboard entry chosen on REG050
					RTSHelp.displayHelp(RTSHelp.INV003B);
				}
				// end defect 8177
			} // Help
		}
		finally
		{
			doneWorking();
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
			D0CB838494G88G88G6E21F4ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DB8DD494D716E6A6E6B3E7E332BA431910B9E643E44D44B392C3A2A8D122C4097107A8B120C4CDC2CE5CDD4E9C62B8BB6EAE3B1B1DA99A7CCB8CBFCAE6489135859DB5A2D103CA98A723828AC810C88F20A12BCDF75194D4F7355D057C586A5E77DE5D6A6A223A9D99651CFB2A5E3D773D773D7B6EFB773E5785D734ABF2FD362908633257F34A5F09EC0E1B7FA5477D66BFBAB311D770F3DFA127795B
			8DD4480DBF1E8BCFB3113D67ABAC60F26BB7C139924A6B5E75957E9E5E37F1F137E3CE40CB10FE46026C719F3F9A9F3A1F3F2F43FEB65272E05B96F86681D5812F93E8524FC97F635A0A95FE98147314F7C259A60EEB19CCF4E2F30AB57CCCC56F64E476DCC5F5A63715A87288A8931DE8F8A6CE7637391922FB303ACAA9F70EDF492DDB68B3FDCC7A52340F34A70A552DD71EE6AE57C48C1143ED6E2EA57D3774B93E32C94E8AF950AE49950257DE61113D95B6D160DDF285881C122B22DF282EBE6BF1B6483C33
			395F5945FB1AA5792C546F32B7B4379DBED29997C5302F4C59C45B859BC01961FC7D63138A0E703E151CFDFCF9CEB01C9FA03EE77DB8FDFF593DA179D952F68A35BB29F7954A91C02C4F12F66C33E80A2FB0FC8A612DB1915DA6F837837926A016BF6DC3AC9DB7FE41E5F5BDE94A8F02A5ED8A4340BD674752E9BB3861D36A833FBBC76671A4482E8249C091C0F1C0E9C05B296F3DDE7799BCCFF749B55400275D6EB3225D7335E467C5F82F2E8609423B485BA40F1D6318FF2567149B60C1DA7BDC20EF797D211D
			C3FF083A78AEB76E0DC3B96B8D4696775C211C5CE856C6F134FFECECDCAF5413F1D5815F82A4820D811A8814980D635AFE760BBB0D2B41A5739E17D5EC6D6FF2DBBD7219FEC134739EE5FCAB5AC20D4F08D83D0C3B2E476CB2698B2DDDD6CD0DB4EDB4304B2AC98F1A5295FBE4E96C427A5E5D9E2C6F6267C74E87DD534F93BF6C4AA1FED88E6F590A0FB23EC879E19ABE7371F554EFC9F99C4856BD0FFE5B1107E3E84ACD658A0E7C54F08C91F3FEEC6AF80161F13F201F5BC79B493EB28178F1C049C099C01B26
			7A8A77GD5CD45796DBE7B79DF6937AD1A39E5F5F71D7B9DBC79C159E3EDF0F9E52B4B4637D9BD0235CB643D1C22337F9C415E95D06F90DDCFB668639C6F62BDD6D910DC503A554903A3F9194E71F3E4F336F9F8AA3588CE9EBAF03AB9EC772BF35B60A9DA3D72DF5CF62B4C7BB5187C6CB9E24B6B56B6E8919A00779FCCA5B67FCF3AE7F5403B8C44ECB573BC61A5C1F935EA17174FED0567F9F0ECDE2C2E2E95EC840655B3047D4F2CA7FB89FF9D764CDA5E4D3B6C7062D5E4F36AB77B6D52AD291867D3BED9A3
			B5G5C2B72975797B2BE342135B24A17D139D516BDC2D73F4C377232AC389C7E7AEFD3AC5E0065676558580A73080FE5113147788A4DB1B8BEC743B64D781AA4292F5FEDA1B32758290A6E3315205F94033130A70744C6F78C7A691A8E74535A0F53397254A78D7DF4E26C211C0898661F51B17A356621ED7A7B78EFD347DD2E7BE0640E9939D6D99F5BF26ED59FB986BCE6770773095D53G43BA2022D8347BD38DE6365E4ED83DBC2E308B9E18B15E6E77B2269B51985C471EECB47631279B0DFDECF2E3E89F1A
			5AE864C3CCB623114496E940257149590DDB699AF639F88ADA2D4231DCE30D0558C2F089F23BE0F770F22BB04CF31CA51678D6DEAE712DF2F8BF950B3EB4D0063E34E7F89517F0E558E4944FA21EFE5C94914B66B5AA566FCB8CD342F9E2F3DE641DE0289A48D6E4461FC7573742D776171DF9463AFB7208BF7671C30A5E7E3C8D9C59C744FEDE9DDB3475A3E2E30F600429A28BD64977AE34EB733DA440C7F24302E9306782313A7F7389E9B7C4EA2578CFE28141F7926A35G75CCC37F19187729E65DAA0ED3A3
			09A21B86F5FE4667971079642F757330AD1305FDDE9DD3C47ED61D2CCD999F13C7661371091ABDA1EC9A995F5EDC4C3DC84CE03C131477CBD8D88BF2B186FFDD204DD56D5E9F9A4EA52144E9EC8E5326696357FF9512F633003FE93A1A0B94E8772C53DE2FE493E8F8D066E5C36EF16ECE792D03C69C11A63897AD2D3502D5149CE724C14815E8FE79A25D7748BED1B29D734B324BE4AEACD096014628F896960C742BA5859F4233DB70F8A1AF327303E8535482E29B922F94B666F1C73536CACFB24E6388CD182E
			6C215375367A73B69A3F001F2EE2EBA9AC54588AF2CE884F8E18C944731B429C6667CA5934F5BD7AB9077B64AC1A872D1BEE1C73EC0A8B6417C01965BB0FD1FB9D0E5367C1BF257CD3F17ABC6840E54C03DAC0969307F179D89199D7710B3E427DAF62389E5B7A31E6DCAD12601255B155EEA3EBD572888E41053C864A030C1BEEDEAC362552B92E02B6BB01982F3C03705C242C72A6DFA23CA8A8A73C087169094BE4FFC8D24B4F5072DA357C7965FB95DB7214FEC6462F7AFB5A07BE06B13BD59711B99AD44BDF
			517210223F0D4BEC7F841EF63E5B5AAFC228113C7C8537124A813FE986315FC6EAD362D7C5B3F07FFBFE006CAD5D106CC178E9F573B6EE4FCC22DB5301FB67B8501D881488349AA897642C4FB917C89B32A0D3BF96E712737B9982CF41E45ACD6E81342B3AC6023389C92ADC3C32C727DD566E5151B3D9FE13824F8C351F1D9D446FFB2D572D03955E8140D951AE386C52G34C632DC58CCEA9BDADBAE3456DD3950FC252689DE58DA0E9C502E652A8F47865D7738F8B62FC54AF36C152959EC7F91284DC81DA610
			0D09A73CFF6FA8524DD7210963AA6049647BAEE965E45D9F603472CB81F202551B2B15BF9CA0DF2F294F4635E8AF1E23BFD8749EA746BE9A746EA1C9994FDA65E92E181A9DBFE657E6564E12EF3576A97849F5D3F610F6763F446AD73E243F43787581FA96A7FB3CE6022A2B9B499C0B4C06855F225F753C04FBF8B5CF7618C8A84781A53F04FB4C1C92221B81655CD93847BC347DA0BC5B2C3D4A49274EAE481207A6EB40EC71C860697290635248292C4B240E511DD9DF5E08760D1971969777F74D260F0258E5
			4FACE60FDA6599D7BEB51BBC59D84AF7925B48A09B8794831484148E14250EED71F60291F2CE827F34ACDBEDBDB4FAE1AA5AA67083FEB6D9202C6D8B3B88D256C6D1495EE07601969AE1DAAEF475C24431D89D9C6ED91FFC2B374D16597E3D4DBCFBE40CE1B1E38A1D374A59ACE6ECD479B3A83FC943E73164946513329DE473E76338DFADA1ED2A79AF1BB6BCFF15907FA739AA6C9B42B05D44DAC15165E0E36FB493F9F9022F309EC8848A838ACF4036AF6EA8E5B6B530E79DA43F38EFE0FF47F6864F6BCF6E3C
			73193AEEA749B13AA4D9161C24FD641F5FC946ADCBEE4AD472001D4447A0A47648CCD561376DA4FEAF725D142D44C03AEE22E1DC09899883B7719897B392B04645D09BEFF9994E13C015AFA3BF014D89145DC061AA7FAC2F0DD724CE009FE84A15AF9B679A210875F1A5200F75BA1D9C1D7E5D7641ECE6B7871F37BE731D67CDB653FC916CED99F2730CB7E426B9CBF73717D77D631639C065B5F8DBA42FC0BC1245261AEF093EE89D127AC13FF88E19236FEC5A189687B64C846266B0DBDE25BA3B78629063E3ED
			678F925B301D7408427B4360A6BF6F88637DE9104C7319FE70BF979330587AEBDE9FDBC9DEB4897A0F87E2BA3D3211CE8A48B3D41D98CE2B434DE5BA35706C180BF90423146CC1EA2EAFF4B9AB7A2FBBAB2CDDF2858F5BD1F5F5F33FA8B6A9E6D2F27BAF6D98DB5DF3B1AEB86CE4AC13201CG14B6974F2303FF40BDB96AE7291C7BC7467758691607CCEB67AA31BC519F2B5C36039BD8BB3DFD58CE4964E5D496ACF636A875E5CDBBF9AA76035D9853E29291FBC28F411E89655CD7FC05A52F609D79D3B26AD63E
			023A7FBBC4F4C5A80F811A708A5A712B0347383BB9B3E9E6DD4516496139B0C66D6F0F74EEB3934A3973G5BBC5C8B3E325FED0CE47B7DCABACF157358FE1F2D72D7D37EA98D1F4581279DFFBFE882D974BC0C77352544370ADE75959E862A873A8A9486B461D55C4F376DBE4C5D61CE54A2391BE0A390B557F52C7DFFACA36DC7C3DBC9C0EB01B64D077183D54E47763F5BCD6E5403659797A509A62CA4F7358AE0F99ABEBCD6F252E1B6BC5C2D37E17DFC7F3E59B9BFD8AC2D9408CD5C73192DBED279E7A8FF1C
			064FECB80F7A97A9C7032CE801FA6611757D8FAE7077B9F6C1307E2F8D539CEC01BE165F247C38857AD81EB30C31BC99E4158B317FDFF121FCA8E2E1307E1F23BE9935D06FC3F1149F37D06FC327CD68C309A05B3F887BFF574EF198F7FE21F9775A6F65394CB84E0D3D2FFDB07F7C3AB4F81C33149A5FBBDEAEB53EF73CDA9A7A5E3127B4783D23D4BA725E711932605826145DB986CFAFB34E1DDEAA9B19BB4DA9B34E1D66170D4C1D4E517DF9AC78C864A24C1D9EF1E06E14287ACE35C0F2498CA8F3C93E42E2
			20CA207AA41457D379D5A80781C582A5G252B72241EFBB17F24EF02C15B3F5F477E791E7441628BEB135871A3054FECB30B46A86EB558D7DEC3FBAD9CC4FBD53D06639D20FBC0A7148701A2D47E83EC4F00F29AD0164A5FBC7C37E2A7FD68F958BF6B2374EF6E43A8874DD1EC26645D59E80B624558EF2C09D367E816667D5CBD7B4DA4783EF3753E7641724A7F6B9E195B76F7E3AEF8926C90271C8FAC70CE4E996C6EE82283F5C4600781C582A5G25AF46BB1D9BDB687DEB1758603A7E35572176597765C7AA
			3FC1F65658957E23367CC0FEAD6F35A932D8FAAF48E417BC0CF725B8201DE6092BDAC2E31B203DDB6116A8386149640BC5A3F918AA0FF5994943D579843711FC12AAFF40E3A40FDF02F6C915C75A7C058134E772923F4DB31630F39D33793B032843A5439A862A8432G49495876BFB87318CDCE0B22E4334A3C5DA2215D9D7B376AE4AD3071134BC3A6FF211260367574DD647B3DFC1BA458F9BBB3AD9B5734E17D386819B399477D044FC8BEC9150F39251547A7B3796AE49C5F2EEC9CDF9670363C6EABAC87EA
			829201D8FE70C4B76687E3011778BA663EABA87F83A897AD05FACBB1AFDF684073C76DD23C5B4BF2686F761E6034F7F31F75686F76F68448BBFB74F7FB7B826E769E6D515F6D6DD36AAB77E95DC16E531A2CDDF81F96A360F94237944FC7B74FEA3F73094FFFA8681DD54CD266BFA9CB7D679AB31C0FD85B6B1D6A9D719BE827CC97721AD45E9BEE640DD1F9B3BC480BFF8367E445G4E49BA601527601C7C1E1ED96DD07691DE8A3631F18867A4AA85676458D068B9F18C6967A4B5C07E78D068B9D9B494ECCE98
			267707F1DC2BD51CA9BE6415242AE737366D46F3D753435B7A480FDA2C6E4CDB781B16A5955B2B4B46F10FCD45F1FB3375637EA6E0DCCFE76B477DC836D63EBCDBBF6E2981724DA67D382D0A1C4518FF9AF12E796BE25373F7DD0F657D4D8E629309297A73C0BB65A7276A4F83696AFADF8532D34BB0AEAFF10C8E770B02D13D094B143B5DE5A37390F646346CA5F6CC82F98690379CF284202A65F83E6C5CF347EF366A86C3FC3375215388D747F206C7DC9E6C3B0ADF390C6A05A97A61C16B25390D6ACDD27463
			03561B6DB12A172C682F8EDAAFFDE0F47335662E6BB1FFFA186EBDD94B75FE96CE7939E9FABF5BB108FED6GB2B98D7DEC4B5068F07F5B30D13D34B4E61F4C34E0F6FA53E7D48F586C3E6B4DE07592EE696B6D875D130AFECB50FA0D59239B1F0524F34A7BF71A77FF693E7FF971EF6FEB9F6CAC10CC632F9D6CA63F09E725E33DF8BE88FBD3DD77FB031F0DEDFB0D4F6D42DE63F33B34B774B7E983FB031D6B993E5DCE44BCD1457CFDA7E20ED6F91BA454CBD1F90204FA69AA6F17EE544BDD013C16EB28D72072
			16F9D02FD2651D70225E29953816468F7A7D64A71A7732417B6FB3A76FEB9FEC4C1BEF209D6C2A9DFAEF209D5CAAEF2D8F7542D55EB7B7D1AFD265AD27B9C4A21453D41E79B6564DD4F95F3B0DF533D660B960DF85720D048C4E47FE334FA3690E723BFFD3AF6659A5ABFD050701DA005CC06300187E117DFE7D471D289F8D72C42055C0392BA0EFDF057A0F9F706B07C928DF8B728E20C1208820B8D5FF41E7FEFD47B554CF83F9A6501637A00F84EAFA8B75B38F7A751B3D6A599364E1C011C089C0692AFE7EA1
			3F7E77EE2867263761FCGD489E481125FC67D2395FE7D1E1B289F8E72C9C07301B200B63D0376DDC367466962A1588BB6766D45FABA8F4F826DFF875B6D7D5C5FEE44EDEC378A64CDC487E8ACD054BB18A32675E10E18748E66089BC4FD0E98F89E299275B9622EGF92F284F91EF64E86593C4FD0E184FE9F36359CE44141B0E185EF70646344B2547F4A9C07E0DCB0F69C2007C81179E5309GCCBF139053C995D30A949A5386F7E8CCD5EEBD26198139F42FDB0FE9E6G26C72EA1A6D945B473DAE8CC6FDF8B
			0DE917C70F69FC209DBDFACC13CDDACC32D7BDEB29181E72064614628D0DE9032C4774EA209DE5BD2603B9DACC4F5CC0CC2BD5CC29B7C2E35A6853E37AB4C07E25CF0F690900396B7369B1FD9CB0F70F5EC4CCA56FA226781B21B12D39999A1379169E530EGCC97EF69B1ADD764EC0FA939CD1E7B5A76A998E75DC60C9695E366EDBD46C38198F6F2FA0CDF8748DBB8BD464782B0FE1F53E32C8BB8BF3E55B73A7CB151703CF367FA132551557B613551559B720E2EDE540D51557B514D51551BFB7BEE6B317F5F
			ABB876DBF8C63D4F2D17394F9ADCA40F15BCC366C3E6722D3419FDACEDEE16E4B37DDC5AECB633AF26E52E5652431B7D95346AB62B4B85B53AF8F3B729D4E1B657C82EEE4163B44B240EDF33DA66CC6D2DB277C9FB6B217606665A567A063A26DAF32B252166FC5DC5194BBDFA7870D1195BF8740B93470F3657485C9E522935CB3A4E1B85520CD57470D67B90BC158BCD33ACC12379F04436C9AE483BEDG4EFCC466F6DC648F75FBC14EEEB5F54015EF428E5C56FEAB177520B3683704356F3173D447FB7A6F02
			FFF0022C51F060C73F07E7043463E4CD867BF62B558968E52B58F75AE5A75FF11D9912F97F1960119324FDAF6AA8C2A68BBFC1F077EA6A7CBFD0CB8788F533CBDA1793GG70B9GGD0CB818294G94G88G88G6E21F4ABF533CBDA1793GG70B9GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG5193GGGG
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
				ivjButtonPanel1.setBounds(176, 198, 217, 40);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the chkReuseVoidedInvItm property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReuseVoidedInvItm()
	{
		if (ivjchkReuseVoidedInvItm == null)
		{
			try
			{
				ivjchkReuseVoidedInvItm = new JCheckBox();
				ivjchkReuseVoidedInvItm.setName("chkReuseVoidedInvItm");
				ivjchkReuseVoidedInvItm.setMnemonic('V');
				ivjchkReuseVoidedInvItm.setText(TXT_REUSED_VOID);
				ivjchkReuseVoidedInvItm.setBounds(162, 38, 203, 22);
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
		return ivjchkReuseVoidedInvItm;
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
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(19, 14, 531, 78);
				getJPanel1().add(
					getstcLblInvMsg(),
					getstcLblInvMsg().getName());
				getJPanel1().add(
					getlblInvItmCd(),
					getlblInvItmCd().getName());
				getJPanel1().add(
					getlblInvItmCdDesc(),
					getlblInvItmCdDesc().getName());
				getJPanel1().add(
					getlblInvYr(),
					getlblInvYr().getName());
				getJPanel1().add(
					getlblInvItmNo(),
					getlblInvItmNo().getName());
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
	 * Return the JPanel11 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel11()
	{
		if (ivjJPanel11 == null)
		{
			try
			{
				ivjJPanel11 = new JPanel();
				ivjJPanel11.setName("JPanel11");
				ivjJPanel11.setLayout(null);
				ivjJPanel11.setBounds(19, 106, 531, 78);
				getJPanel11().add(
					getstcLblAllocatedTo(),
					getstcLblAllocatedTo().getName());
				getJPanel11().add(
					getlblAllocatedPerson(),
					getlblAllocatedPerson().getName());
				getJPanel11().add(
					getchkReuseVoidedInvItm(),
					getchkReuseVoidedInvItm().getName());
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
		return ivjJPanel11;
	}
	
	/**
	 * Return the lblAllocatedPerson property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblAllocatedPerson()
	{
		if (ivjlblAllocatedPerson == null)
		{
			try
			{
				ivjlblAllocatedPerson = new JLabel();
				ivjlblAllocatedPerson.setName("lblAllocatedPerson");
				ivjlblAllocatedPerson.setText("Subcontractor 1");
				ivjlblAllocatedPerson.setBounds(235, 12, 142, 14);
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
		return ivjlblAllocatedPerson;
	}
	
	/**
	 * Return the lblAllocatedTo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblAllocatedTo()
	{
		if (ivjstcLblAllocatedTo == null)
		{
			try
			{
				ivjstcLblAllocatedTo = new JLabel();
				ivjstcLblAllocatedTo.setName("stcLblAllocatedTo");
				ivjstcLblAllocatedTo.setText(TXT_ITM_ALLOC);
				ivjstcLblAllocatedTo.setBounds(6, 12, 206, 14);
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
		return ivjstcLblAllocatedTo;
	}
	
	/**
	 * Return the lblInvItmCd property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvItmCd()
	{
		if (ivjlblInvItmCd == null)
		{
			try
			{
				ivjlblInvItmCd = new JLabel();
				ivjlblInvItmCd.setName("lblInvItmCd");
				ivjlblInvItmCd.setText("WS");
				ivjlblInvItmCd.setBounds(27, 44, 39, 14);
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
		return ivjlblInvItmCd;
	}
	
	/**
	 * Return the lblInvItmCdDesc property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvItmCdDesc()
	{
		if (ivjlblInvItmCdDesc == null)
		{
			try
			{
				ivjlblInvItmCdDesc = new JLabel();
				ivjlblInvItmCdDesc.setName("lblInvItmCdDesc");
				ivjlblInvItmCdDesc.setText("WS-WINDSHIELD STICKER");
				ivjlblInvItmCdDesc.setBounds(93, 44, 221, 14);
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
		return ivjlblInvItmCdDesc;
	}
	
	/**
	 * Return the lblInvItmNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvItmNo()
	{
		if (ivjlblInvItmNo == null)
		{
			try
			{
				ivjlblInvItmNo = new JLabel();
				ivjlblInvItmNo.setName("lblInvItmNo");
				ivjlblInvItmNo.setText("23762WC");
				ivjlblInvItmNo.setBounds(405, 44, 99, 14);
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
		return ivjlblInvItmNo;
	}
	
	/**
	 * Return the lblInvMsg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvMsg()
	{
		if (ivjstcLblInvMsg == null)
		{
			try
			{
				ivjstcLblInvMsg = new JLabel();
				ivjstcLblInvMsg.setName("stcLblInvMsg");
				ivjstcLblInvMsg.setText(TXT_INV_NOT_FOUND);
				ivjstcLblInvMsg.setBounds(7, 15, 445, 14);
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
		return ivjstcLblInvMsg;
	}
	
	/**
	 * Return the lblInvYr property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvYr()
	{
		if (ivjlblInvYr == null)
		{
			try
			{
				ivjlblInvYr = new JLabel();
				ivjlblInvYr.setName("lblInvYr");
				ivjlblInvYr.setText("2002");
				ivjlblInvYr.setBounds(341, 44, 37, 14);
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
		return ivjlblInvYr;
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
					getJPanel1(),
					getJPanel1().getName());
				getRTSDialogBoxContentPane().add(
					getJPanel11(),
					getJPanel11().getName());
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
	 * Handle Cancel event
	 */
	private void handleCancel()
	{
		// PCR 34
		//SubcontractorRenewalCacheData.setINV014CurrentInvType(0);
		//caSubcontractorRenewalCacheData.setINV014AllocatedName(null);
		//caSubcontractorRenewalCacheData.setINV014Voided(false);
		//if (caSubcontractorRenewalCacheData != null) {
		caSubcontractorRenewalCacheData.resetVC();
		getController().processData(
			AbstractViewController.CANCEL,
			caSubcontractorRenewalCacheData);
		//}
		// End PCR 34
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
			setName(FRM_NAME_INV003);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			//setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(569, 264);
			setTitle(FRM_TITLE_INV003);
			setContentPane(getRTSDialogBoxContentPane());
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
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmItemNumberNotFoundINV003 laFrmItemNumberNotFoundINV003;
			laFrmItemNumberNotFoundINV003 =
				new FrmItemNumberNotFoundINV003();
			laFrmItemNumberNotFoundINV003.setModal(true);
			laFrmItemNumberNotFoundINV003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmItemNumberNotFoundINV003.show();
			Insets laInsets = laFrmItemNumberNotFoundINV003.getInsets();
			laFrmItemNumberNotFoundINV003.setSize(
				laFrmItemNumberNotFoundINV003.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmItemNumberNotFoundINV003.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmItemNumberNotFoundINV003.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	
	/**
	 * Receives SubcontractorRenewalCacheData and use it to populate 
	 * the screen
	 * 
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof SubcontractorRenewalCacheData)
		{
			caSubcontractorRenewalCacheData =
				(SubcontractorRenewalCacheData) aaDataObject;
			SubcontractorRenewalData laSubcontractorRenewalData =
				caSubcontractorRenewalCacheData
					.getTempSubconRenewalData();
			// PCR 34    
			//if (caSubcontractorRenewalCacheData.getINV014CurrentInvType()
			//    == SubcontractorRenewalCacheData.PLT) {
			getlblInvItmCd().setText(
				laSubcontractorRenewalData.getPltItmCd());
			ItemCodesData laItemCodesData =
				ItemCodesCache.getItmCd(
					laSubcontractorRenewalData.getPltItmCd());
			if (laItemCodesData != null)
			{
				getlblInvItmCdDesc().setText(
					laSubcontractorRenewalData.getPltItmCd()
						+ CommonConstant.STR_DASH
						+ laItemCodesData.getItmCdDesc());
			}
			//getlblInvYr().setText(String.valueOf(lSubcontractorRenewalData.getNewExpYr()));
			getlblInvItmNo().setText(
				laSubcontractorRenewalData.getNewPltNo());
			//caCurrentProcessInventoryData = lSubcontractorRenewalData.getProcInvPlt();
			//if (caCurrentProcessInventoryData != null){
			//	int liYr = caCurrentProcessInventoryData.getInvItmYr();
			//	if (liYr == 0){
			//		getlblInvYr().setText("");
			//	} else {
			//		getlblInvYr().setText(String.valueOf(liYr));
			//	}
			//} else {
			//	getlblInvYr().setText("");
			//}
			//if has a sticker code, then no item year
			if (laSubcontractorRenewalData.getStkrItmCd() != null)
			{
				getlblInvYr().setText(CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				getlblInvYr().setText(
					String.valueOf(
						laSubcontractorRenewalData.getNewExpYr()));
			}
			//} else {
			//    getlblInvItmCd().setText(lSubcontractorRenewalData.getStkrItmCd());
			//    ItemCodesData lItemCodesData =
			//        ItemCodesCache.getItmCd(lSubcontractorRenewalData.getStkrItmCd());
			//    if (lItemCodesData != null) {
			//        getlblInvItmCdDesc().setText(
			//            lSubcontractorRenewalData.getStkrItmCd() + "-" + lItemCodesData.getItmCdDesc());
			//    }
			//    getlblInvYr().setText(String.valueOf(lSubcontractorRenewalData.getNewExpYr()));
			//getlblInvItmNo().setText(lSubcontractorRenewalData.getNewStkrNo());
			//caCurrentProcessInventoryData = lSubcontractorRenewalData.getProcInvStkr();	        
			// End PCR 34
			//}
			if (caSubcontractorRenewalCacheData
				.getINV003AllocatedName()
				!= null)
			{
				getlblAllocatedPerson().setText(
					caSubcontractorRenewalCacheData
						.getINV003AllocatedName());
			}
			else
			{
				getlblAllocatedPerson().setVisible(false);
				getstcLblAllocatedTo().setVisible(false);
			}
			if (caSubcontractorRenewalCacheData.isINV003Voided())
			{
				getchkReuseVoidedInvItm().setSelected(true);
			}
		}
	}
}
