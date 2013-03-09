package com.txdot.isd.rts.client.misc.ui;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSelectReceiptCUS003.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class.
 * 							Ver 5.2.0
 * K Harrell	07/18/2004	Modify from multiple to single selection
 *							in Visual Composition
 *							defect 7320 Ver 5.2.1
 * K Harrell	10/27/2004	Set scroll bar at top when over 10 receipts
 *							modify actionPerformed()
 *							defect 7679 Ver 5.2.2
 * K Harrell	01/14/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							rename bean from CUS002 to CUS003 in VC
 *							defect 7694 Ver 5.2.3
 * J Zwiener	03/04/2005	Java 1.4 - no changes first pass
 * 							defect 7892 Ver 5.2.3
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Screen presents the list of receipts for reprint receipt. 
 * 
 * @version	5.2.3			06/22/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:		09/19/2002 14:34:05 
 */

public class FrmSelectReceiptCUS003
	extends RTSDialogBox
	implements ActionListener // KeyListener
{
	private static final String TXT_SHOW_ALL = "Show All";
	private static final String TXT_999 = "999";
	private static final String TXT_NO_TRANS =
		"No Transactions Run Today";
	private static final String CUS003_FRM_TITLE =
		"Select Receipt   CUS003";

	private RTSButton ivjbtnShowAll = null;
	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblNoTrans = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblCustno = null;
	private JLabel ivjlblName = null;
	private JLabel ivjlblTrans = null;
	private JList ivjlstTrans = null;
	/**
	 * FrmSelectReceiptCUS003 constructor comment.
	 */
	public FrmSelectReceiptCUS003()
	{
		super();
		initialize();
	}
	/**
	 * FrmSelectReceiptCUS003 constructor comment.
	 * @param aaOwner java.awt.Dialog
	 */
	public FrmSelectReceiptCUS003(java.awt.Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmSelectReceiptCUS003 constructor comment.
	 * @param aaOwner java.awt.Frame
	 */
	public FrmSelectReceiptCUS003(java.awt.Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 *
	 *@param e ActionEvent 
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (e.getSource() == getbuttonPanel().getBtnEnter())
			{
				String lsSelectedFile = "";
				if (getlstTrans().isVisible())
				{
					String lsSelectedReceipt =
						(String) getlstTrans().getSelectedValue();
					lsSelectedFile =
						lsSelectedReceipt.substring(
							0,
							lsSelectedReceipt.indexOf(" "));
				}
				else
				{
					lsSelectedFile = getlblCustno().getText();
				}
				while (lsSelectedFile.length() < 4)
				{
					lsSelectedFile = "0" + lsSelectedFile;
				}
				getController().processData(
					AbstractViewController.ENTER,
					lsSelectedFile);
			}
			else if (e.getSource() == getbtnShowAll())
			{
				getbtnShowAll().setVisible(false);
				getlblCustno().setVisible(false);
				getlblName().setVisible(false);
				getlblTrans().setVisible(false);
				getJScrollPane1().setVisible(true);
				getlstTrans().setVisible(true);
				getlstTrans().requestFocus();
				getlstTrans().setSelectedIndex(0);
				// defect 7679
				// set scroll bar at top of list
				JScrollBar vertScroll =
					getJScrollPane1().getVerticalScrollBar();
				vertScroll.setValue(0);
				// end defect 7679 
			}
			else if (e.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// defect 8177
			else if (e.getSource() == getbuttonPanel().getBtnHelp())
			{
				// if no receipts
				if (getstcLblNoTrans().isVisible())
				{
					RTSHelp.displayHelp(RTSHelp.CUS003C);
				}
				// if 'show all receipts' listbox is visible
				else if (getJScrollPane1().isVisible())
				{
					RTSHelp.displayHelp(RTSHelp.CUS003A);
				}
				// 'show last' receipt
				else
				{
					RTSHelp.displayHelp(RTSHelp.CUS003B);
				}
			}
			// end defect 8177
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Return the btnShowAll property value.
	 * @return com.txdot.isd.rts.client.general.ui.RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSButton getbtnShowAll()
	{
		if (ivjbtnShowAll == null)
		{
			try
			{
				ivjbtnShowAll =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnShowAll.setName("btnShowAll");
				ivjbtnShowAll.setMnemonic('s');
				ivjbtnShowAll.setText(TXT_SHOW_ALL);
				ivjbtnShowAll.setBounds(156, 176, 101, 25);
				// user code begin {1}
				ivjbtnShowAll.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnShowAll;
	}
	/**
	 * 
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88G6CF5ADB2GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BB8DD414D7F6E6035128E94D29ED29074DE1B7F64BE6B955B466686EFA9A5B26A7F66359F54FE6FB12832DF46BEE766438B9EEE39B53E61B0F3F000A0A11B638CBE4D4849498C1920491B7A0904590A0B0D192B5704D4CB74C875F4CB7197986869824777D5CEF3E791C41E80D67DC5E776EFB6F3EFB6F3B773E7B5E9B051272547C6C1C92C1484E97783F33590250F686B0FB5EBA0EB87FD0C601E078
			F7946020F039AE8F4A8550767C03998545C2FEF9A154D7C0FD846AEF4377A1E1CF7946FA78C8B0CFB234FD3F715C43734F33D744F98AE8FD70C891147B81128137812070D3B27F7811D20E4F825CFE7ECDA0BBC79052E91F152D2586FC4F272CDFB2AF13BEA3FD9EB8D2465BF7823E88F0C3G4AE7D11A3BA9F78BED4D3CFE7D61EF8ABBFF70F14EBEB21701BE42E6BEF687AFAD428BD491394236236721EC6D9F9F7D3DD375DA3530CB552CF250E58DE8C12BD311A51FE6754AC127B5A45BEC9F843C0312A2B935
			8149A949FE2D6313417AFAD8AAE1649A61FBE3EBA125A98855D0E7BC7EF8DFAB67E14578B78521B0A7B7910F2B2EB15EB6DD0B721834F4696E70F5C2FBB555B999D78875B6G467F837FEF7E9BF57EDFBD027CEFG7A2DB7886EA767483CBB21DE84750A9BA85779AA14EBE866F1E117C34BB910C02E85B718BCE9B722F2B54BCBF79F263674278EE28F5B202D70730C02B200B600910085G3F23B6BCA01E02325D21F528DE2F6AEB777BAFA82E40C72ACBD2605BE603960E83395400CB90181D7EFDEBED9CFE90
			12BF0F355128DDDD95502E52875204DDCF66666647112DF6C5EEEEBA2731717328ECCC2EC69711AB8B702F83148CE794548274GA843A8D70A58F8BB393AFD1A9470094AE0486197835A05102C3824G176F49A37349978F58383F3B63F1CCAFE90DC63DE4BE914AA90823174C344FF2968FB3FD248EC7754266FEBD615C9B07EFDD8F825B06099DDEECA5F6789CFCE7F39BE5780FA93EFAA40AE77E728235DBD2EF06361491345B217DA8C3CFDE0AD05CB091D706643F9D4F91C446CF29E8DE5B83EE921FBA81BF
			829086D88E3086E0B3C016086B7B22F87ACB5AED3FE1ED5958D4477FC2A9053500586D8BEA224FA9DD91833268D02420407BFC5BC1F8F7440C7BCB6ACFCE18632264138222A62BBE20AEFAA5B024A06B334EF1107489C83455AEFBA518406B97106ED30EC3D0AAE2D07B446F92B5A95A0335BF63A03A1C902FGC5A0G5F758E2273CC9A7F8478AE83E03AEA9DA5B88D6A4B9C28178B4E37207C908CDBD2EC36CE59C9589083D3B8FF2B0B4492E982E2D7276417FCAE788872368E576E28DE5C2A4EF3B745939F69
			G4603BA3E4FF510610106D14B58FE15360B1A96109DA1CD9A14B4CD760DC6478FD3DE02B16E9794380FFB080D2502DC6B81B669727D52F548A0DF0F2A0E077CF632F2DCCF0768BE1D895FBB9D3827FE54CA7658D2A75A694BC3E8271D7B7EC2287D299657CECBA08635BA19FD5A1DE6DF3BCAE9C66758FAEE688E7D7EF6D06A0C7AAC1BE357BD1FA357704D747EAD1968BD82F3A7832CF7225E0FCB9666EF974420049E5697009513DCD1ABE3FD8F3B935B18459D5F46AA5D71ED2C56BD3F8D3D6B0EE7C32C6D1C
			1B6CAD6A244FE013ED6E37288F7BFC92E5DA9FA230BD7FE5BB51056C13352B32EBD4528665E9C990F6B909ED3DC2FDF08BFCEF53EDE972B85AD245742390E77BF2626DE749FF6E4BE9F531756CF7C5ED097154BECAF48E49039794552106FC9A43DF247E4D71BC3E34F86277FD5FC36CF1DC1A627DBABCEF8AA40EA8A1C917ED0D381768B8A0FBE1290843723C71D439B1EF7483FFA447DC8B65A600DCCA6F9B53A4E6A6CAF2186DEC9DA33CE541181DGC5925A4ED31E43861F65C65321AA8ADB82FDED5265E232
			165297A189C2B2F16A8FF5F91E105FB235DD613231766F4BC4B645908FAAA4A25B1E7318F7117D02610ED25C2B70678F73F17F7D2858186F3A0223F1571100C7E26B97114CFBD709C26826GBEDD173DE94C982F5A03C155A9532D012F491BF9CD426DF24CD13A5BA8B44D9C308FF64A22220EDED04318DB2E20B10F44080D926616EF9CA5EB310B486B4EA828F6A3BFBF9F3B5526DE9A5B8325DB8E84A1A7F2C9E154690EB1229B32D7F1B466F0E70C3A727CC37C9C0EC01B1B47CF37D9D71FF9899FFE40A7693C
			0D0F9FB4688ACC8C3666D1D8C9646730124BEC1C575FD1725146850C11FFD6CEF220E4F77CFCA75D045F88F5166B840F519C48ED4E01E6A9FE1B5B1C83659F439CE8A7348D0D621E5C6FA3F2ED007A66D1146B2F14FD86397AD5592769B28DFB091F2A81F9D476A14EC5F110ED5340457635B5ECDF831A02A72300618AAD84D78275DA9D3768A84175C35D65413DE96A98098DFE3DFEBF2D2F506BFB0F5D4BFD054CE32C33B9AA6E699C3926BA535BA7BE32C6E13D7EA92DCF717E10DB157F8F14AE49AD069458E6
			5420546767E99CD9C78F515F4F281E4938759EBC87BEBEC9E20B9B92BD58FA867D12D358AC133E65960C1B4DD06F81D0G1681244B98B79FB9CAE8E832C66D38E40C5C815404841D272EF748DD02512BBBD4B81710044AA771783C7618B1BE8F0D315CA6826572B11C6735F2925F464489B1EC8DCE821F562B324F25CE82B51261C2B0696C9B6E6D33DF6A6E3DB85C57BB5C5153B758457DB9F552684F4DFB440431AFED0C2D6D86DEBE67F8B01B4520AE8B6AEDA734550E935C0FADA526B5BB1BA388D6A8D9FB6E
			D1E3BB717D1302317D3D18F6426B79DCE37BE8CCFB3EE1BC136B5993F80EFE7D59478465C9E742BB8CED1C5F1570525370E076263E374B981D9FDDB36A27F86507B9598968EC6163B3474DF7A12F1CA4EBC8633CE111EC364BE41D9526436D478A399C458A46F1758409B31720BE8290D6B04EFCE70274DD8E75F485630CBFD4836595F10C1FFC3ADC322686E83286487E008A562ECDB1AC5DBD39EF26D96E6E4C7AB785285F355FDBA52CFF60F84E5E84FA592CB0FDEC6725D07FE0B6A919AC16B051CD19B72320
			9960BA00863098A0550B322D8B919EA167A46C37EB1A6874509D8CD351AB32B499C593A7E534BB087B73F8701B5009C47A818A17E1D97A9CE3306B5845D18163562F0F19F533569B0DEF1B3C376EB3EC5FF8052EDB26176D9B87F4FCEEA541677922F8361F04E8FFD22F0536B49F4A3DE602505473DF36ECDC1EF593447EC92E8A31C31E2601EC90BA7AC679DEB4CD5AAB540C02B6G97C098E0190A34BF18BC46F49AC71FDD107CE26C4079CE0693677575615B1F29DB42A44FF0281A26FA89FD443F9FA63A56D4
			BFC5B2DCE7185898EC0B9E0DF565784BE1E2770A6426E83E8F9EA738B410EB030A7B60799338B7E629384F3DF6025CAD6479B38AACG4DG7DG9E0085GAB7C58EFF7A569178675CDG5B810A3EG3AG7DG7EAF305F4C897F177095B297FEB773397A79FC717AFB926077F35E595CAB3F545C7906EFB2EE9F775D83897A6F1D07D62E3E86C353096DE0F87A76F6601926B9A82C1FFD4AAFB11CF71A66C1EEF7D0526DADB0DD2C633A037DEAD0A6964E763B7263243FA2CE29A168DF98A0EB5EF49C77496530C6
			E9G19GBBGC82E53CF7B58CF145EC646C3428FBEA4FA8AEA4E9E07522B5A8322AF28377D0E7A6965C1E7G4EAFA4F6B7B07C0ECAEA5B1AEF5023CE36AB8A76FF33056894DCD7A3F7C5906AB9FE2D1D66568E25A39454FC2ACE7F512682066F953D92473DF0251061A8ABDB289CED15463DAFB940E45CGA54B99662A0DF21686D9FBA714C3G0C46C52B11061F77D993C49AB375C69ACF7376C2AD2320CCC39ACF1E0947473BFA7BF74F46EB3F2437EFEF0957FE9D5AD9DCAC29403BF9BF6052B44C49C60F11D8
			2E1F1BE407579A1A70DAC507E615A02C5BEC3DA1C5696166414FC957ABB0C74952F0FFDDC2691706403F81AE0570DC9F2E443DAD7DDBA902ABB97E5BC2167DFA4EC8083F2F0422FBFE053CF4B723B3B60EF44A3E73F55A16A887594847EFB350C19F067C411DA0FF68919D18BFCCD662B930E28265187D4078C63261407504FB747589FE2E1DB03C115C37F4B757F757BB71670FFA022E67F5557ADB47846AD92921EF9DD0AF1A4CA8281844F5349CEA946E707CFB01BA931F6B2695E5EE55E97E3E663DBB22F941
			601DA8439375A803B609B2641CA1B224C37DE900CCG1E371DC19EF66A389DE791D79BC65CF19B66F72EB046715FD65C5DBBC375DD0E936FF89C4BDB7E1B7AC9CA1865AD59BA3E004643B4831E796DF25DEF57C2DB6994662DC991E2479128A7832C81588470CB003CE94CCB8AE648336A3CF77BF6555F839B10E238F6E6747FF91650AF85DA6F828C81788112GD2F47A9F4F901BCB14A78F28AABDFC11F318A8031550B4A8A012D3BB53E124B21E8E57CCB33FF91A174FCFC777F1465B25B9926B36833EF086F2
			9000E6002199646B6BB337FD336A8A4F73E63521FA3E358D4FA44AD53328EFAE1BB15FFD3CC471A9B3663B0FC08D5EFD24C3DBF1C43FD7301A675720EDF1044D1B9239F5FEB6EEFD7DFCFC2F09B30E71B7C17D71690859A667A8FECB44EC1305E750A6B3212DED96797E7519F8732F1AE5732E676566D973FD7C3F1F0DB7AE0B775B494B421B66F15536F8634AF83F7A1B097414C736BC7E3D4F707DA795777ADD605639D7FF25F3B07BFFA812B82FDC91097FEE30B2927F5DA0B5B27F3BC1DAA4713B416A482D6F
			86AF4EA6666D65595B673C2F4E46BF7B7CE6765633CF4E4D78E71F421B371EFD7E086E791DE0B3761BF87659D509E71F919DF7186E49C9D0DFF59363CD7FDCE2192E4E4557770D39787AF64E4D7F96B8BE17E8BD98FFBF29C61E575FC43F7CC58D39274C546BBBE8FD07DE8F575CCB1B4CB4799D1B6361FBBA07795E12493E1266B045F310974D61FA7DAB45B5C37D120E3BF49AF5E457F1BF2AC7DCF88E7592A1BF1B617CAFB4FC6FBF735547092A2FF48EFE5EEF246FACA0EF3A2E0746A65DFEF41C1D66E019D0
			FFDD4795B5635862850873B66358B29D7787ADB836CD473DEF4331F69D17EA53FDED815E45BED6C96CD48367E1773D7FC616005F999F2842B3D59274CF86D88F70BC40AEFDFCFDB5F9B3722881F91A64A0CA3BA20F7A486D8C3F233EDC0D6718527BB28A985D1774735EA540DD8708G24GEC388F693ED2C663C7D0B31CA30F569A4FDF197731BB8F61EBB866F1EA07DE1F8489096CE473EF223897095CFA3F7F34464AEC4B094ABCFC9AF92B007E4DGC3GE100642FA17F3F504F1A6B898EE0BBC0517D998535
			77639C650D31F35C480D4EF139894777C07F910085G29G9B757163E767576BC2EAAFA43EE54198B647C3F951B9121AF10E22A4485FG5AGC6G96GB09A6BDAF0EDD21310C649D41446BA9B5258826D5BG7296025F83F4AE44F342FBD5C84335906F521DD5663BF4F29E338AF8977E47D5663B74061876E72A4CF7691FE49B5BDF2BB25F257F0A0FE73A932B496F950D6F8B0364EF74F921232B575EB5A060792978A44A10224B306A14D906C5B1BC7C63A9338CC7E3E4A82CB14B70CC4CFB022D46AC43B9BE
			1E1D8B1FBDC95EDA621D21652046799E2A45F35BF38B51EE36DAC9D9952C62FD961EC6590A9EC0591EB8ED162DB106771F1FB64BA646487EF63DD936E7E346F7571BE5EB616399CFC58D48D3274E132D414CD3A8062652E066E9F58CCF0FB418F95A9A533E25414C53EA4148133F89F90A68BCBD52B43F9DEFE91A5F0EF34E1AF93A96537E6ED9B3CF5DB1F61CF256B23F9D8FF4DF3CE4E77CBF560C7C2F5579FF3EF9FE7E77344C4FFFEB0B197F6F46685457E2667F5BB17C7F30654E7C701FECA843F6DD063D67
			6617617C3979E5D04F19E5D894537E0DF3E69946391F329C41D6F5F7F797B3276EEE5C0DDAB22EE2D17CFB75E6933EE79166D1CBCE5F5DFC879B6EEEDC28696E462DEE3E3BF1BF0BFB8E1D8FD84E235198B982FA8AAF42BC682FEBB18FDA26633656915C9A28EFDE0431F4E9AD39FF94C31A9A249707C13E77BEDA0B77EDD90BB0A778E9AD6E15BBG17379876C900CEG57E224779FF5742EDE623FA5A217A5823E659F30226DA7697D3FD7CBF308C0C8E27E71DF3558E763E2740F13F5E67F28097151EBF5E67F
			F8660DB75E0836BFD4E7760FFF09997FD41D59BF487CD19F2F341A5F724986B5E814D1318EF677DE6C699A9E6C6A696A30F7777572B5956A6EF4CD597FE5F839E68C4A74DF89791A70B61B4A42FF64E531D8586F3CB461F02FEA214923C8B989DA86C2BE0BDDF509D31AD0C8DEBEAC6D0A228939ADADAD1AF028C76C94E51FAC12AEDEC57685A16B925E9C683AB8505B03EF48A7670CEF6B3B96DEC8785B0332A57CFEE4097937C4BDF41D9540AFDE02E75B076B6A0444EF3D03DE9178941571F61F0B3C7BDE1852
			2468D976E3AB219F44BE3C113FFDD9F7733541B17F87D0CB8788CC7B6E856491GGE8B2GGD0CB818294G94G88G88G6CF5ADB2CC7B6E856491GGE8B2GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG9E92GGGG
		**end of data**/
	}
	/**
	 * Return the buttonPanel property value.
	 * @return com.txdot.isd.rts.client.general.ui.ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setBounds(87, 215, 239, 58);
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// defect 7892
				//ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				//ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				//ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				// end defect 7892
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setBounds(29, 16, 355, 195);
				getJScrollPane1().setViewportView(getlstTrans());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblCustno property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCustno()
	{
		if (ivjlblCustno == null)
		{
			try
			{
				ivjlblCustno = new javax.swing.JLabel();
				ivjlblCustno.setName("lblCustno");
				ivjlblCustno.setText(TXT_999);
				ivjlblCustno.setBounds(36, 24, 45, 14);
				ivjlblCustno.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblCustno;
	}
	/**
	 * Return the lblName property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblName()
	{
		if (ivjlblName == null)
		{
			try
			{
				ivjlblName = new javax.swing.JLabel();
				ivjlblName.setName("lblName");
				ivjlblName.setText("LaDainian Tomlinson");
				ivjlblName.setBounds(84, 24, 220, 14);
				ivjlblName.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblName;
	}
	/**
	 * Return the lblTrans property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblTrans()
	{
		if (ivjlblTrans == null)
		{
			try
			{
				ivjlblTrans = new javax.swing.JLabel();
				ivjlblTrans.setName("lblTrans");
				ivjlblTrans.setText("REGRNL");
				ivjlblTrans.setBounds(309, 24, 67, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblTrans;
	}
	/**
	 * Return the JList1 property value.
	 * @return javax.swing.JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JList getlstTrans()
	{
		if (ivjlstTrans == null)
		{
			try
			{
				ivjlstTrans = new javax.swing.JList();
				ivjlstTrans.setName("lstTrans");
				ivjlstTrans.setAutoscrolls(true);
				ivjlstTrans.setBounds(0, -29, 240, 149);
				ivjlstTrans.setSelectionMode(
					javax.swing.ListSelectionModel.SINGLE_SELECTION);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlstTrans;
	}
	/**
	 * Return the RTSDialogBoxContentPane property value.
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
					getstcLblNoTrans(),
					getstcLblNoTrans().getName());
				getRTSDialogBoxContentPane().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getRTSDialogBoxContentPane().add(
					getbtnShowAll(),
					getbtnShowAll().getName());
				getRTSDialogBoxContentPane().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
				getRTSDialogBoxContentPane().add(
					getlblCustno(),
					getlblCustno().getName());
				getRTSDialogBoxContentPane().add(
					getlblName(),
					getlblName().getName());
				getRTSDialogBoxContentPane().add(
					getlblTrans(),
					getlblTrans().getName());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblNoTrans property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblNoTrans()
	{
		if (ivjstcLblNoTrans == null)
		{
			try
			{
				ivjstcLblNoTrans = new javax.swing.JLabel();
				ivjstcLblNoTrans.setName("stcLblNoTrans");
				ivjstcLblNoTrans.setText(TXT_NO_TRANS);
				ivjstcLblNoTrans.setBounds(118, 24, 170, 14);
				ivjstcLblNoTrans.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblNoTrans;
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmSelectReceiptCUS003");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(414, 283);
			setTitle(CUS003_FRM_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	// defect 7892
	//	/**
	//	 * Key Pressed
	//	 * 
	//	 * @param e java.awt.event.KeyEvent
	//	 */
	//	public void keyPressed(KeyEvent e)
	//	{
	//		super.keyPressed(e);
	//		if (e.getKeyCode() == KeyEvent.VK_RIGHT
	//			|| e.getKeyCode() == KeyEvent.VK_DOWN)
	//		{
	//			if (getbuttonPanel().getBtnEnter().hasFocus())
	//			{
	//				getbuttonPanel().getBtnCancel().requestFocus();
	//			}
	//			else if (getbuttonPanel().getBtnCancel().hasFocus())
	//			{
	//				getbuttonPanel().getBtnHelp().requestFocus();
	//			}
	//			else if (getbuttonPanel().getBtnHelp().hasFocus())
	//			{
	//				getbuttonPanel().getBtnEnter().requestFocus();
	//			}
	//		}
	//		else if (
	//			e.getKeyCode() == KeyEvent.VK_LEFT
	//				|| e.getKeyCode() == KeyEvent.VK_UP)
	//		{
	//			if (getbuttonPanel().getBtnCancel().hasFocus())
	//			{
	//				getbuttonPanel().getBtnEnter().requestFocus();
	//			}
	//			else if (getbuttonPanel().getBtnHelp().hasFocus())
	//			{
	//				getbuttonPanel().getBtnCancel().requestFocus();
	//			}
	//			else if (getbuttonPanel().getBtnEnter().hasFocus())
	//			{
	//				getbuttonPanel().getBtnHelp().requestFocus();
	//			}
	//		}
	//	}
	// end defect 7892
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmSelectReceiptCUS003 laFrmSelectReceiptCUS003;
			laFrmSelectReceiptCUS003 = new FrmSelectReceiptCUS003();
			laFrmSelectReceiptCUS003.setModal(true);
			laFrmSelectReceiptCUS003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmSelectReceiptCUS003.show();
			java.awt.Insets insets =
				laFrmSelectReceiptCUS003.getInsets();
			laFrmSelectReceiptCUS003.setSize(
				laFrmSelectReceiptCUS003.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSelectReceiptCUS003.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSelectReceiptCUS003.setVisible(true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}
	/**
	 * All subclasses must implement this method - it sets the data on the screen 
	 * and is how the controller relays information to the view
	 *
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			Vector lvReceipts =
				(Vector) getController().directCall(
					MiscellaneousConstant.GET_LAST_CSN,
					null);
			// get last receipt from map
			if (lvReceipts != null && lvReceipts.size() > 0)
			{
				String lsLastCSN = (String) lvReceipts.get(0);
				if (lsLastCSN.startsWith("****"))
				{
					setDisabled();
				}
				else
				{
					String csn =
						lsLastCSN.substring(0, lsLastCSN.indexOf(" "));
					// int number = Integer.parseInt(csn);
					//
					//if (number == 0)
					//{
					//	setDisabled();
					//}
					//else
					//{
					getstcLblNoTrans().setVisible(false);
					getlblCustno().setText(csn);
					getlblName().setText(
						lsLastCSN.substring(
							lsLastCSN.indexOf(" ") + 3,
							lsLastCSN.lastIndexOf(" ")));
					getlblTrans().setText(
						lsLastCSN.substring(
							lsLastCSN.lastIndexOf(" ") + 1));
					getlstTrans().setVisible(false);
					getJScrollPane1().setVisible(false);
					getbtnShowAll().setEnabled(true);
					getlstTrans().setListData(lvReceipts);
					//if (lvReceipts.size() == 1 || UtilityMethods.isHeadquarters())
					if (lvReceipts.size() == 1)
					{
						getbtnShowAll().setEnabled(false);
					}
					//}
				}
			}
			else
			{
				setDisabled();
			}
		}
		catch (RTSException leRTSEx)
		{
			setDisabled();
			JDialog laJD = getController().getMediator().getParent();
			leRTSEx.displayError(laJD);
		}
	}
	/**
	 * Set Disabled
	 *  
	 */
	private void setDisabled()
	{
		getstcLblNoTrans().setVisible(true);
		getlstTrans().setVisible(false);
		getJScrollPane1().setVisible(false);
		getbtnShowAll().setEnabled(false);
		getlblCustno().setVisible(false);
		getlblName().setVisible(false);
		getlblTrans().setVisible(false);
		getbuttonPanel().getBtnEnter().setEnabled(false);
	}
}
