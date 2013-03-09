package com.txdot.isd.rts.client.common.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmSetAsideCUS002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Joe Peters	10/22/2001	Added functionality code
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()      
 * R Rowehl		08/22/2002	Defect(CQU100004650) Removed ATL+E on the 
 * 							screen CUS002.
 * Ray Rowehl	02/08/2005	Changed import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	03/15/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	08/02/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/05/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Frame Set Aside CUS002
 *
 * @version	5.2.3			10/05/2005
 * @author	Ashish Mahajan
 * <br>Creation Date:		06/26/2001 15:15:01
 */

public class FrmSetAsideCUS002
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JLabel ivjstcLblEnterCustomerName = null;
	private JPanel ivjFrmSetAsideCUS002ContentPane1 = null;
	private RTSInputField ivjtxtCustName = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;

	// Text Constants 
	private final static String FRM_NAME_CUS002 = "FrmSetAsideCUS002";
	private final static String FRM_TITLE_CUS002 = 
		"Set Aside       CUS002";
	private final static String TXT_ENT_CUST_NM = 
		"Enter Customer Name:";
	private final static String TXT_ENTER = "Enter";
	private final static String TXT_CANCEL = "Cancel";

	/**
	 * FrmSetAsideCUS002 constructor.
	 */
	public FrmSetAsideCUS002()
	{
		super();
		initialize();
	}

	/**
	 * FrmSetAsideCUS002 constructor.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmSetAsideCUS002(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSetAsideCUS002 constructor.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmSetAsideCUS002(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel is pressed
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);
			if (aaAE.getSource() == getbtnEnter())
			{
				String lsName = gettxtCustName().getText().trim();
				if (lsName.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(150),
						gettxtCustName());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				getController().processData(
					AbstractViewController.ENTER,
					lsName);

			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
		}
		finally
		{
			doneWorking();
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
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(TXT_CANCEL);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnEnter property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setName("btnEnter");
				ivjbtnEnter.setText(TXT_ENTER);
				// user code begin {1}
				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.addKeyListener(this);
				getRootPane().setDefaultButton(ivjbtnEnter);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnEnter;
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
			D0CB838494G88G88G7BE496ADGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DA8FD0D45739C7038909D4CD44046425EFFC56F6FCAD1D1A29FDC3D35EBC6CB32D2FF1726C547CFB252DCE53995B5A0C73C227B6C34BE1E1950CEED0E9C29A12EC92735496A9A02930E2D47E893A9494B40424158BFBE1AF5C5D3B5EFB9796D860FD5FB9775C3DAC3B201989B35F1CFB3EF34EF73E731D6FFC5F6F1C05143C355A1EE7ABA1A44FCE0C3F53F904E47E1910EF6E38B948F9BB7D398EE279
			FB9B6890B9D3D8G659EE85B7AE02E231874CF96C27D00126BC8CA4DF53C8A5FC74850E46084FC441927955AFE3BE3E27D7C737CFC104F330F567B9A8EC0099D0BE12E9A208485678FB5149A7C9E28BB0CEF24BC9BA14EG7649A9AF3570B7997D8E84D819C67B2CECA8B35A3DD047BE89D02684A2B20B28F6CBDC35C67D13FBFFC8323FB7E2BB08F3D964F34AB646165EE425133CC08D11CFDEFE5385E5FD67485087830A2FC28FF9943DC252BC952A2ED58C48126857AB20412778AB02124B55227A3AC47D02A6
			F94446EBDD15155504A45DC41D5FB9D9C865915289F5265F778F569B73270C7C1024CEE7664753EF0B2157EE0BFE094AD3792BD5141DC16D0D63AA216E85E2F396B472B91F831EC7C35E7B6558378C3EEB005C9A57656CB12ECB4F64AF481E7EE53617636832CEE3BAEC55A23AFC3233507E9A5D7B2097776F001E6B2884EA827282A5812D83FA1D7ADC65501F203C502FB7D243DD88849AE40F5A2EF8C4993EDDAEE8B1F8D74581C57590427C6A584972987AF04A54E77BD4448FBEA45C8F522E6EA01BB2B7665B
			E32CED57439B733799B2F66A113531F59D9A46F59586F39DC7016A013A01F42014A0DF574450290556556657C555AF48DD417E00206A8DC1C976082A313EEFB74C373ED84446ED3D6DF14CAE6BCED96D1275756536FDB16C1235CE372D8FB2FBEC8EC66C4266AEEA0CB7774E605C7D20E7F9947D70DF299F66C01FBC43C7997F6B145FB99A61B39FBFC379D86F0336CD235CEFFB9CFC8D6E02A7C9F1550A18EBC87D76925B71B1264F7931683D7D558846939978C9C02B0152011E814A86AA8C717DFDEC68AF3768
			371D163DE5E3273D2FC0A906F4D5E873EB3A609F902F8B2AA4744B22C60CBEF7F9D1777ED9631269F99A00B9AE0AFED195F4C97103F441A702A3E92C4FAA6FA16C230A3435DB7209B001AFC0385C4FFB0F607A84CD3F9670883A9869415A3F68C5DB0E8A57C1A288C02B0650669F5018F59C3E1B00182D7E68C35E43D05FE05A45A99D0672B2B836A83BDCCD52G2AA12863FC7EBF8EE3AC91C7A1D6B60981516F018F4DE8FBFB38A8E217C145547998656399E98445B513FFF2789063038C2B15F97B7B34DD50F5
			D56A8F6AA284E3DD728FC5461F253AE8330E1FC60CB36EC79F5B8C6B5A8E345BDC5FF7060FD85667D61411E0209BF74E30131346590271DCC759B84F01DFAB471CF8FE1C7B6973BD5CCF1B8EEEA765CFAE0B69274F2C59185FB74E7CB3B49EFD563EC9E5C66638D05EF31B675EE168F46B312577D34D11FF0B7D185D9395347BEA50AB83A8EB1C5BBDEF44494EDB0320097C04F52830E322A762E52C6FEFC6627B586FC7E27BD8019C5B47764B737B50E1B9168F313657E44CAD4A185F6213EF4A076999767BC52A
			34B904B00C71FCB75AC272CB7A0712E7C8543B24899176F982FD6B9ABD0359E01342896ECBE36FF2DFBABA11C5B2EFC836D879AC750BC9363E8936577AC4441718CEEF79506622AC7A40D00DCA502FB37EFB74FC9BFCA33E3C6A0F5D778DBF7A6308B8EE74FB5B3F17E09C11032239367542CBE8E3D57241D66101B5F05E69C6AB4ECB817D9093BE8365AEA0B6B6258B77A18624B27CE6F3G75AA184CF51482D5CEF23F793C7FB54BF9B59C26D111E5E6FEF3DF6ED10AF19F451BC19142B19E684B66DA6ED58EC7
			35DDB756455A9FD4F0DD32A59634CE623AE49367E12EE03CBBA86FF5B8D0D78AF86EEDAF36E2D30F36A8669EA2A5CF323DDBBD991D37FEF79365E682FF2B39761200B5D6DD50B4E5C022E941580F3D85FFA58B6159A11AE9E48AE82FF6F7B5C9022C8CB5A8A10EE57F0546BB14775CA447159B1AA92E8C832E84F20739BECF84667A538F827B219C14D48D7010C78CF11BBE9BC05BE01EB2589C3FD5DBEDD572E8EC7C0664899B31B39CED2BC68D75C8817EBAD3376E1B07AC36822C89E9F988F6126BE3D37319FF
			9B753D2A1D7BB7617171CBF1718FD2C6F8F62C5D8AF506F30E37203D1E8BC7631FEA4A4F8EC7631FC71BB97EA91882CCBD4573F11D0E6B5A82752CA93E2EFFD28FDA565529C8FE5DDCD32B06E7D4D125A1494FF96D1487C81B86AD16537E1D6E71AE10D9B08DE713726A1BA99E00FA7DB44FC195AD9883DA4DFAAD2D6BE6BD33B9DE7E40B1371BD7E26728E7634EF12BF9E8BEE2B67AC0477D88197586DA9FA7BC4EBDB20671E0900099240A2E00B8C04AE750E65B4CB8178A6BCD835A82348B68G3467D3D9DD4D
			A8C317F46AFB7A8C5E310FCC92C3FFBC4FEF1098F130D1014B8422A03FD8C5787DD9EA314655749906C932205CBD436F05AF523DC57D8B93F29D679328BFB615C459CD008E95D032765ED96DF8EE7059A0521EBC2B9D4F6DC51B353D2C595AEE370CE7E77C7177787DB367711F92799BD3F16F6AC98946DB00D1E61ED81357F4684B05CC4EFFFFEC0D95458F09363CB8F21EB3466FCE083E733FF082EDC8E36430B0AA04AA34B158D4176B921ADAB6E2E0CB21217B51C5BC8636CF61997D846ABAD072A2FE86F267
			0D9131082DE563BEEE138D0F7E08245FF95676D21C352CDD44563049A8FB0E2F490B602BEF05F1BD5950D63C9862B3109B288FA83418470C1EC9549170D533C752AF683AB06025911B432E6B12B896E12303B359BF1EA2661978674977203CAE8C9B37A0942528DC42A5B0E5C77FB0C459EEE108187133A55AB6490BA3F1E1B5FCEFG5AEC6131B839115ABAEBB10B13C5A67F894A5FE361337859C579D8AF398356FD870F1F3F1CC63F4C027A9E2032C44091C0CD095CAEBBA65FE1F609E113E6GEBF08871B627
			F39BEC8A4747210F07973E83EE8BE3EE6CD7F4DD7121FC4E7F1FB05ACBD7821449F8BF8963FAA014FBF55655607FAC0C3EA90B0314ED44F36A2FFD30AEBD114773DFF7A2AF956A6BCD5E748D64ED02FA1649FBFD0A67025D095C273FBB055829E4C92E2396289348330437ED23EDE128BF8C148114E536FDB4F5AB778914990B4F673172CAACF5B607FACBF35863707799FE92AF0FB09B3FB09DFF1FDF1CDEF81F73E7A8AE027D699E8F080CD7B8C3F3736020A61A7EF4F026586435E91D0AA6A118E3F9E1DB8B76
			1705F1A5887D3DF762DEBD7FB79E7FF340AEA5402B876A84421C4C469D3802F144A704A4DF50C7AF9BFB685823D7D02775701DF1A76F3B6A866D8B171359FD3FCA7D247E2EDCC7AB10FE9745AED325A43E3DF9AE43F7E7CD9FF0774B4D78985698544055C575EF02CFB47A3C74AA4E2907F4EC33706F23B66B577DF4A0634D08C50C5708E61661BB1B6AD2B8ED4DB799F73175EF071235BFD1EBED2FCFE26D89C93CFDD51535BD45E85FE6369FBA972BFDA714AC5E3C52415FE4F300D7F3B73F53D47561DB1319DB
			AA04B15D653A28CA1E86E148CDF7126517FFF4709CAD5F4D73CBB21539966A99C05B6E667741507F71BC117625A7495A04586F485B3BC7ED3B6EE6E725781EC84E8B6CAC34B34CD1FA11187B245C405C6C97471CDC4D0AA689229FBE909D2F2E2AB9DDD3FB6A2FB5274B1D116F93BCEF2D9E3366605A7D13F16FB6EEC30FBE23AC7FF8CD9E53E5F1EBC41762A967BC3A3CDFD96E2CFA3F72841BFBF8045B226CABCF517545439129461C9996DB7038G98E0B08E86F08B7D9C831C1B6698A0679E3E97D3AD56B77D
			0C17C763666C1A25EC5E6E2511791D3097ECFE0763B4394DFBDA1BBF9054DBA4D1769877B4FB9D57AFE5A977BF6711D337AB37A1884948EF485CD205B2B1C6EED94A71E46BB9FE9FACC866F13E2C63533DE57F69D30E9B386DF18CAF7CDBA71E297AE4069772CC7EFFD0FE2B054F4E75FD66396E063674E40EA3F25A50DF4ABE8779902893488B14843476F39CCB2C1929A48B3C9FF7AB81B7A4955972344964BBAF230D53C156B6D35E5FAF7E194441DB406CD41500286A630CCBDFAF8CBCF845CD71A048A9DE86
			3189488D548794DA46E57FD76B29F9E4DFD594FA6F40AB08A00137D098248AF853E47B51B49DEBBF121799EF99D0EEG5A3CAC123B8D2C5ACE71987077G95AF4FF59C87EADD4E757AC35B023F31B40766790DE5FF9D6A65DD9E19B7EC79DEE1FC2FDD9E0DA93EDF953D1EF46833456837FD79DC3C4246351F0BE507EC23FF610AF863B2BA09797D1865BB3563533C475E5ED9FA64B31D036D237DD2FC7CF460D26C775A4317E23F5716DE1A7F3D56F9A97EFB6D31CBF35FEB5F3B6C304AE3C747E8AB3F0C7E09EF
			3512A0CB9374E92D8BBA7A07D8FB2FBB7E3A84774238F0589D9B7F875CF371FF509D9B7FCF3A67627F0FE92EAA83FFABDF41F17D7FCEF37CFF5E64F534F37C5FED723EF10663FF6F8AFE8E7F33BD7EBA3F579EFB7F3636475E3F275A677FCD67476D7176176977B64DF369205B96D367E14A5B8E75BDA66F9197DFC7593D1CD7C773198C75C413371372D2211EE672AA2B69DBAB54371A3CD555DCDE1649731C637D4A6F633C474E71FE3577F1DC2DCE53F7B2B0924353C9F4AF1220BD850871DE27F8B79D6A1BCC
			DE086E4FEE2867G352E24B979CC53BC67AD5FA03BD19E07B1C8FFA0AC0F552FE47F8B425F9B0F105ABABB7571GDEAB8C3C5FC16D198BFFABD672B5AC2AC3EC63857F135187E147589D6441BAFE8FD88DFD53D73298C7D258FD10693C5935104EF60B4E25A9911D11D78E7597722CBAF7D621EF411D46AD7A07F42F2937176E65D9205A942EF7AE65797CA2A490E90075FB251A636DD6335F45EA3EC78C137E3D83174174CF2BDEC87FFD965BC75B3C9B66F85778E63293BAA332334E4DA75B3E006CBE10FDE416
			6CAFDCA1261DFECE6F266F509B14135F3D1CF849F23D437BFF67EA24FFD37BDBA4AE762E2A2E856CDDF34298F7355BB24EF5AB635EA26CCE083667FFCC5652EB784DB0750E83D6CC5F7474D99B37DD34ED6494835327C4B035FB7F5379CC2741BA9B14A72BB8165BD8475F543321FF79AA0E111F38D9C766E27F0A00AC8CD6748B1AB460F2B5E0518D1EF12D6DA87DEE351C82C258FD6D77D43E87640601D200662EF777B9365EDFDF0F58EE268A75BCD5496BA72BA3F6A96D09FC1F79883F99EEBA510BBE1386F3
			EC865A85D4F0FF2E2394289228892887C886CA84FA98E8BD50A620E700F68265G95BF40637B56EB78BE9B8F7F9997D68B6A6358613BF58B6170238F4C46BCEC5C138B0EFBB32A4EF03A6AC29B1FFFA09A3FCFD23E7B01E87CDE6062783D875AB24C751E6D0D1F4F1AFA974E5B6D3D3173F6D76F5C3C5D539BBBEF7F23571A3799EF2097FDF2886C5DE376731A3C0FCC1E0F4AC45E07120746BF465728DE48EF95F1C24E9F27FA0DD1DE8F7F9F958B6FA303F7C7AF3EB141123D0A4A47A675E2DC9C14E459789F95
			5AC772EB22EE5A63DE2A239490ACFA5FEF72D06FB7283FE79EB9C8474A925EA668FF3918BE9142FF3DB33E97D93E773BBEFB8C7C6EE7BA877B5F4B2F6CBD830E1876ABE25749AB00EA1C74479DA77BE33F71E8AAB9BCB700DED8766C53E9B71ACAEC4456C676FA690635917B986EFCA8B083C99AC93BC92629275F8F7422CE6CB4686BE4AFFBAA53C9C92CACE00EC2BD9322EAAAF95954260D6E777651D32E7C1FFEA15FC68AC095667FED24G1D98B4D219E679607F2100E1DB444F09C047286CA242FECDBAEC34
			6CB339689FF8574373FAF89697B9B5F48C4B8FA7E62C6FCDFB16FC92773D2964A196FB0EBF947D7BF0F5911EBD8F70939E62B1E1F161C9927F770CAE1FA04B3AA00FDC70FB70370D06F1DD0CE05A53C5A8DF63FD0CC6633744A254FB58B2667F81D0CB878806G816D1F90GG68ABGGD0CB818294G94G88G88G7BE496AD06G816D1F90GG68ABGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG5990GGGG
		**end of data**/
	}

	/**
	 * Return the FrmSetAsideCUS002ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getFrmSetAsideCUS002ContentPane1()
	{
		if (ivjFrmSetAsideCUS002ContentPane1 == null)
		{
			try
			{
				ivjFrmSetAsideCUS002ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmSetAsideCUS002ContentPane1.setName(
					"FrmSetAsideCUS002ContentPane1");
				ivjFrmSetAsideCUS002ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmSetAsideCUS002ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSetAsideCUS002ContentPane1.setMinimumSize(
					new java.awt.Dimension(250, 150));

				java
					.awt
					.GridBagConstraints constraintsstcLblEnterCustomerName =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterCustomerName.gridx = 1;
				constraintsstcLblEnterCustomerName.gridy = 1;
				constraintsstcLblEnterCustomerName.gridwidth = 2;
				constraintsstcLblEnterCustomerName.ipadx = 11;
				constraintsstcLblEnterCustomerName.insets =
					new java.awt.Insets(39, 20, 7, 132);
				getFrmSetAsideCUS002ContentPane1().add(
					getstcLblEnterCustomerName(),
					constraintsstcLblEnterCustomerName);

				java.awt.GridBagConstraints constraintstxtCustName =
					new java.awt.GridBagConstraints();
				constraintstxtCustName.gridx = 1;
				constraintstxtCustName.gridy = 2;
				constraintstxtCustName.gridwidth = 2;
				constraintstxtCustName.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtCustName.weightx = 1.0;
				constraintstxtCustName.ipadx = 249;
				constraintstxtCustName.insets =
					new java.awt.Insets(7, 18, 17, 20);
				getFrmSetAsideCUS002ContentPane1().add(
					gettxtCustName(),
					constraintstxtCustName);

				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 1;
				constraintsbtnEnter.gridy = 3;
				constraintsbtnEnter.ipadx = 17;
				constraintsbtnEnter.insets =
					new java.awt.Insets(18, 51, 29, 12);
				getFrmSetAsideCUS002ContentPane1().add(
					getbtnEnter(),
					constraintsbtnEnter);

				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 2;
				constraintsbtnCancel.gridy = 3;
				constraintsbtnCancel.ipadx = 9;
				constraintsbtnCancel.insets =
					new java.awt.Insets(18, 12, 29, 52);
				getFrmSetAsideCUS002ContentPane1().add(
					getbtnCancel(),
					constraintsbtnCancel);
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
		return ivjFrmSetAsideCUS002ContentPane1;
	}

	/**
	 * Return the stcLblEnterCustomerName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEnterCustomerName()
	{
		if (ivjstcLblEnterCustomerName == null)
		{
			try
			{
				ivjstcLblEnterCustomerName = new javax.swing.JLabel();
				ivjstcLblEnterCustomerName.setName(
					"stcLblEnterCustomerName");
				ivjstcLblEnterCustomerName.setText(TXT_ENT_CUST_NM);
				ivjstcLblEnterCustomerName.setMaximumSize(
					new java.awt.Dimension(128, 14));
				ivjstcLblEnterCustomerName.setMinimumSize(
					new java.awt.Dimension(128, 14));
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
		return ivjstcLblEnterCustomerName;
	}

	/**
	 * Return the txtCustName property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtCustName()
	{
		if (ivjtxtCustName == null)
		{
			try
			{
				ivjtxtCustName = new RTSInputField();
				ivjtxtCustName.setName("txtCustName");
				ivjtxtCustName.setInput(-1);
				ivjtxtCustName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustName.setMaxLength(30);
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
		return ivjtxtCustName;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7885
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7885
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
			setName(FRM_NAME_CUS002);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(291, 176);
			setTitle(FRM_TITLE_CUS002);
			setContentPane(getFrmSetAsideCUS002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Handles the navigation between the buttons of the ButtonPanel
	 * 
	 * @param aaKe java.awt.event.KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (getbtnEnter().hasFocus())
			{
				getbtnCancel().requestFocus();
				return;
			}
			if (getbtnCancel().hasFocus())
			{
				getbtnEnter().requestFocus();
				return;
			}

		}
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSetAsideCUS002 laFrmSetAsideCUS002;
			laFrmSetAsideCUS002 = new FrmSetAsideCUS002();
			laFrmSetAsideCUS002.setModal(true);
			laFrmSetAsideCUS002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmSetAsideCUS002.show();
			java.awt.Insets laInsets = laFrmSetAsideCUS002.getInsets();
			laFrmSetAsideCUS002.setSize(
				laFrmSetAsideCUS002.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSetAsideCUS002.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7885
			//aFrmSetAsideCUS002.setVisible(true);
			laFrmSetAsideCUS002.setVisibleRTS(true);
			// defect 7885
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Use static information in Transaction class to populate
	 * customer name text field.
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		if (Transaction.getTransactionHeaderData() != null
			&& Transaction.getTransactionHeaderData().getTransName()
				!= null)
		{
			gettxtCustName().setText(
				Transaction.getTransactionHeaderData().getTransName());
		}
	}
}
