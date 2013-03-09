package com.txdot.isd.rts.services.reports;

import java.awt.*;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * FrmPreviewReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException()
 *							defect 7896 Ver 5.2.3	  	
 * ---------------------------------------------------------------------
 */
/**
 * FrmPreviewReport
 * 
 * @version	5.2.3			06/30/2005
 * @author 	Administrator
 * <br>Creation Date:		08/15/2001
 */
public class FrmPreviewReport extends RTSDialogBox implements ActionListener
{
	private ButtonPanel ivjbtnPanel = null;
	private int lineCount = 1;
	private int pageNum = 1;
	private int pages = 1;
	private int linesPerPage = 77;
	private JTextArea ivjJTextArea1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnNext = null;
	private RTSButton ivjbtnPrev = null;
	private RTSButton ivjbtnPrint = null;
	private String fileName;
	private String[] sb = new String[1000];

	/**
	 * FrmPreviewReport constructor
	 */
	public FrmPreviewReport()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmPreviewReport(Dialog aaOwner)
	{
		super(aaOwner);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmPreviewReport(Dialog aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmPreviewReport(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param abModal Dialog
	 * @param abModal boolean
	 */
	public FrmPreviewReport(Dialog aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmPreviewReport(Frame aaOwner)
	{
		super(aaOwner);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmPreviewReport(Frame aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmPreviewReport(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmPreviewReport(Frame aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
	}
	
	/**
	 * FrmPreviewReport constructor
	 * 
	 * @param asFName
	 */
	public FrmPreviewReport(String asFName)
	{
		super();
		initialize();
		fileName = asFName;
		initPage();
	}
	
	/**
	 * actionPerformed
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// System.out.println("here");
		if (aaAE.getSource() == ivjbtnPrint)
		{
			// empty code block
		}
		else if (aaAE.getSource() == ivjbtnPanel.getBtnEnter())
		{
			handleEnterBtn();
		}
		else if (aaAE.getSource() == ivjbtnPrev)
		{
			handlePrevBtn();
		}
		else if (aaAE.getSource() == ivjbtnNext)
		{
			handleNextBtn();
		}
		else if (aaAE.getSource() == ivjbtnPanel.getBtnCancel())
		{
			handleCancelBtn();
		}
		else if (aaAE.getSource() == ivjbtnPanel.getBtnHelp())
		{
			handleEnterBtn();
		}
		else
		{
			System.out.println(aaAE.getSource());
		}
	}
	
	/**
	 * Return the btnNext property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnNext()
	{
		if (ivjbtnNext == null)
		{
			try
			{
				ivjbtnNext = new RTSButton();
				ivjbtnNext.setName("btnNext");
				ivjbtnNext.setMnemonic('n');
				ivjbtnNext.setText("Next");
				// user code begin {1}
				ivjbtnNext.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnNext;
	}
	
	/**
	 * Return the btnEnter property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbtnPanel()
	{
		if (ivjbtnPanel == null)
		{
			try
			{
				ivjbtnPanel = new ButtonPanel();
				ivjbtnPanel.setName("btnPanel");
				ivjbtnPanel.setLayout(new GridBagLayout());
				// user code begin {1}
				ivjbtnPanel.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnPanel;
	}
	
	/**
	 * Return the btnPrev property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnPrev()
	{
		if (ivjbtnPrev == null)
		{
			try
			{
				ivjbtnPrev = new RTSButton();
				ivjbtnPrev.setName("btnPrev");
				ivjbtnPrev.setMnemonic('v');
				ivjbtnPrev.setText("Prev ");
				// user code begin {1}
				ivjbtnPrev.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnPrev;
	}
	
	/**
	 * Return the btnPrint property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnPrint()
	{
		if (ivjbtnPrint == null)
		{
			try
			{
				ivjbtnPrint = new RTSButton();
				ivjbtnPrint.setName("btnPrint");
				ivjbtnPrint.setMnemonic('p');
				ivjbtnPrint.setText("Print");
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnPrint;
	}
	
	/**
	 * getBuilderData used by Visual Age for Java
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	//private static void getBuilderData()
	//{
		/*V1.1
		**start of data**
			D0CB838494G88G88G810E96ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DBEBF0945715F62D711A9FDA475EA5AEB9214A725ACE48863C38E0933C263C44213258D692EC639817E1A35BCA429299AB8E1BA80EEA391A11C0E00CC668B5FAC0E4A3D952CA5603C790324CE3B4009E03A450G42BCC7B3ADCDCBBD5343CCCB9A31F83D675C5B3727B56A118CE5D4F5683E673E4EB9775CF33EFB27A179FFC9484AB065931211C5543F039904588F90F25FC7493F633C6C23B6B3517D
			FD8814CB7E30AB931ED628D3941B59C2B2BE4B06F29D145303B6F3913C67114D1FAD5F8BAFB166198BF53E034F2F1CFE1EEF6F6073EC2165595E6D705C815488BCA7D042D11CBF41EBD579E1A86FD05F11B2CC049CBD06ED8E6E366A7849EA3B3AE36C19ED43B6CFFB4B547A5940CFGDA8314FAACB266D6AA5DBDAD4DEA7952832F130D4F5FB4FD00F369466754EBE3FD9575D9CEB2CDE888B33960E901E7EB6F4850E1376C2BD7429ED92997C31E7A20922AF7CB2260D76A03C2C04662285852F2A2686B8D8AE3
			22B0FE06F2C187AA73655D59F4BCC266C319497762872D6A7CF3C6DEA7CB1FB91C93CB3E6AB6A61723ADA2DF7ACB4DB969EDB8F6B235B7760B0372F2A0A6FBEA49A7DCF6A6F7C8880E09EEA144A5B692D71537CE64729E355B4C4B6C488B6C42F94270FEBF50BCBB576333AA2EC77F7F2EA7E9838F1BF2E26811F9DCDD4F6391BD66ACF06694D33F2932605AFB21AE8EA881E8B9D0B2D066891B39147A6B1BBB7E871E478614B65967137D4782811B64891E12BD02846FADADD023724E88EEB968A1047964406EBA
			83F9B8151D186C0F919FBACF388F4DBF7386D9C70E66E49968E6D9FBA427D19D43FEA2229B536B473928178778F7814583AD84DA89142A69D51733EFA63DBA7C0A90743B243E510100AB2858C6C549A384D57D56FB27534F08D83F34DB6E476CB2EF1F5EAEEB96A518369858E54D3CFBCC5BCFB2FBD41C0C58054D5DA4461A5BFEF26AFAA0F9CFD2FF27FE588F6F992A0FB27E864A1F2F63337DB1C679D8DE8AF5656D5CEF7BF7F09D9C192F90CB53A306BA44BF7B20E9D6BB13E7EEFB745ABE1C0731E885701301
			520172BBEC667DC03DC01E8E3E3E63B98DDF51EFFBF5EB4B7AB6DA8A61A90415202B439FD2DCFE37F04E9594DD831290A2EA1BC39614FDE0D23F43F4BF39E10E1302DF883A94D1764368AE1FG0E94E2EDCEDAF231CDD02035CE51A740843EG616376D87260A939C24A590047258811962C7E3C85EDB966BA87A342G703E3883EDBEC1635DACF8CFGE236CA4CC7DE9A56F7F23B3CE459894FAEF0ECC1EAE9310BEE9443951C6073A766E2AC9146A04E5A0500607740CBC82DFBB9F7EB44AE0332A673EB140FFB
			248D848FE97CDF6666B2BE0C2137B22F7F8D2DF7A9CAD09C98D504BEC1D1C47FD0247FBBD41650246D97A26A9EAFC09F2BG3DDA019C1AFE611DF9BA7D9C32BCB29AF0624A29F6EA27F1769A348FF7727C391E666D391D5CCFB774F3BF35FF10C46ADEF95850CF535FBC1233241379674A4E683D36110E991963662EFE037DB53753F85F5DAA31B93EE9B80779EB989F09597D2FC5E877B410BB3F8B76DD97377B221D65EC3F59DCA1016F301EA02C186009F899EB7B3D3C58BE36B04F5847FE10E76CE3CB732677
			2165F9C6BE446ADE48435CA20F7BF5BE39B2EFA75D43FE3FC005563A901663B7B8519622DFD44E0B1EA1C16993EF8800E33A50376E27FB50816F573A38AF0D6F613ED4F1E38DD9F6750649A81F45BF7E10E9C9977305442E08AFB119DE4CC71B8B126083C3354923FE0571DF257BDB652B716539826336BFAFC0FF9C91A654F6AF97ECA698C724D1C153ED216BFD34F1D07441D26106D5B162D5D10F91D3C0BE44130D8E1B596E6058673DDC8BBC23618D7399590AB2DD02368120598E6EB3BD05453A3D2ABACB1B
			ACC94C745A1A34966158A1617A28G2198B7F31726C7DB514E283AF32ACE2C3E2B88F512F4F1A04101BAFDD7430718A798EF560748FB87B653F319BC6F1E3268B12DA77477066B07146CE06B1666084ED90F95633856D3B6F375A92E7BCF0A74F16AD8A8A43BC51A925435581C79D7B2938E9E22D9C622B8780C334FAE3AA4F948A607B926FC0E46BA0C8D2D27B8265CA761DAA8D00E83FACC13E754BA5517EED83741F3D08C06G8BF904B03769DDD6348D66A8154D315BDE3D2D1A5FBBE2085D10161ED2636629
			E8DB2DAFC1B9D200CF3A39ECFF36666AEC85B893D272902CA41767FB45E6665BEAF9C1F19677ED42E3634FA876496FB60E3755D17CD6A8B30C73DDEA2F5E6EE86C73A465DF6B0E46BE07A40EFD82D0372A1B676224D2542B3947E6EE6F61FA59CABE506955AB0BFEC553691D925C1FF2D09C927D1C371172GE553004572592F689A774318B701982FF598F977C3F91E46FBFA84F909D0CE6A61B9E90104B1A1C5AB7F1016AD3D3CFCC4B24AA737CBB84F541C557F354E911D6B185DFED50AEB94564A7FC94B93EA
			7BBC12AE964053A38C3AC6A5C8AFF2C8688928708D7D3C976DD7C4ED0AFE353F17473CA747B1368C824003145397905C44FE9A5B768D73F839905AAE87CA864A043A8AA0B667CBA3B806A2AA540F63CE63B97F090F08A6D375A53CAF506F6AB6994EA388247C029A1757C97A383C72B443B49B6119FD064FD3BF0CFEBF6C9AF3056BC363A0E77DF9516F1147E1B4C43690CC6C9DFD3DBDFD472F76F4DFEDF3408B5B4B8963137672363818F12F7C8CDB577D6AB32EFBD1860BBF9354E658C7023A474EA0AFEBA4BF
			EA3DCE43BF7570E47587A755633E2FA17AFAF1D2BD4A7A4DCCFD7D37A755E7697AB33DDE2864E76774975EA2523F5C97730EA2C5554752274628DD0BB29C4B8E6630F1FEFED1EF9F4B3746CC99B146E9D77BBB7B226FBAFEDF036BC7E33CEE01DADABAF10DA5E643F29F773B59FDBC064F994798338C4A2B01D27BF80C292F403659CE70B3A70FB13F7F28961E67DC436AE96738C7D464A085E8406C8D4A60694A8463524C296E4BB54337F7CE7D5196EE5F457F3A16AC795BEE537BB16CE2F7B2FBF854671252C5
			9978E43A44D722ED66C05DC220D5C0A9C0642C4DECBD4BF5EB5903B2824EC4710FA90A4B6D25590B434FF300C3A2EC5C20EC6C27BFC6C955DC3627B216FDE004CED8161E01E148B8CE579061B12B5297ED1B3A331158F6746C549C43F2C6F698574DF916650CAC0D1FC779DE9D1F6512EF51F54672CD28CB52745ED701E3EA1817AD1B2ACFCB857ABF62D3089B628D9A447A2021FF08693E228A6B6D67G23824D820A871AFF0E0F7DCC45C74C268676BC8E0017478DBE5FA31531317C3C4A194F51CFD6A2469810
			95C57661781C7F43CA3435A287A8D3457A1568E310923D8AEB2A721F2DC43F1704C14AD6F3A05DB7ABC02FB567F88E9C9867F9111C67B9EEC798F995D0EE55F84514578BE5C563A50CCF1727306FD759A395B15A31B91E1AF60E2C2F388FE31D775473CDD56C75FA35EA6675CA2A22B8916C6C1C8888EAFE2D22D8E5F0B0A4E8FE313E4A2271BAC23DF2C8C4CFE4B9294F076DA55704BC8A6DB37BF1EDFC23BC1745030D96822D86CAD16D6520EDA6462DB3681DC77E9440BD5359678E42B98263E89363BFDE00B6
			E61135C94D1FDDE17DF8F3F08E48534B60398A0835F166C767D8EC13827564024D4C76E957983F8F36822F77824787CD57702ECA0B6F752EF1252565E4D07458DCC38E2ABD0B717FB04673646C8BBC467F080E398C4A2B01D2AF7073E478E39EDF67FF679532E219719DF61273DED376676A5D6F67113CF35782271A3F06C778B8E55F7F852D0B15873DEA7FB85DB85C9F4DE46878AEE21843A2ABC078F9646EB0BFF3A5FE4E755CDA08FAE6DE34194B0076833136F179BC6FB9AE723633E95BD9D01E8B3478A25F
			1B2747EE6FAE766AED760B3F65FEAC4E9F2071BC71A20B73999A7FB0652F567119DF7D3B66D749D057FE0967790594B74C07728A20A420B4A06BE5305FE59E473F2CEEA4B35CFFBA6500838201243B1AE363EF2B45715BE1AC8F50EC20842045C0095A784FD5637DF9ACDCF1C616A9D0C54C6A926138C253C65005A79C75B72B70F4B6CC3A9CAB07FEC1FDE063656873583D4C0F2EC41F47F28A79F9AC9F6A42D7384FBC109FBD7F1AAB11B9D32F441ABF1D4DF3154D33D563EF2577E265BABE1B7FBE5A9E4B2DD0
			17F0154F7F69B8517C492EFB7F695857F99EB2765FDF5F51B918FFBCD593BB4FAC2BB13E637BC90D719DDFE24D74F7FC2BEAE25F713DDEB3750EAF2FB636ECA535B36740BD3546182526F6AAE6292FB546AC7BEB27E21607439C1FE4DE63FEF2F7A11E491DDA796FE8D9524A8505F7EAADA3F3565F31B9F0DFB25D6751F89F87F3A6DC63F82D20105BE339A60B095E85663BEC66EA20D620DE972F4B21F59EA84F821A8734C22B1BB3C5876C0B4FDE57643D1E6F0AF1C794039F59D78B264C31C5FDEEB5505BE81C
			69B0DE59G5A6487E17B8C765453BA10B99D28C0ADDBE00C0B60C00F1AB8DE3A12033E5C69840FBF9694DCCDF0722773BCD328BFC7FB0007F8B8EE005FAB7CDB7ED48C778B8DAB2D03F6448DEB84546AE6F6E37257954EA4FF16CEFEA774753AA3725FF433787A1BCD1BB6B1B90EE7200F8CA87EDEB863A8AA3EAC60BCC4FB0CE789EE616D04B1D577E7AF293CEE50DE553B2DC82F77ECB7BB832CF0F3BCB8E6556341B5EAFD3987F2AF909B232C54E88C07D6FF224C287E9A547F054A85E7F8427FD20F987158FD
			51EFB10529676EB7366B711C7D55EE539EE20C67948FDB0F651E881EF3BE706196E6771FFE017149G4FF18FD1315CFF95F2FC166E6158740B93133E2F4839B76659FF3F205E09889199D682B6E5B9733F73B96E198375098247537B769613A9F87A04A40FBBD4B7D4E5BBD14071F466A0470E8A7DDD24954ACEA0E970D6ED7DC8BD51FC33BCEF9F39453BF95BA8A48B3FAA5F0DA2FEAFBFEB106BF6387640AD0D4986248E2E697D04152F49D2CDEF310462A5A8EF9F3219AB06B83E6EAB55FE9B50F8DF14F11EB2
			443166B79AF03F4E07728A209420CCAFE0CD20C6A0BBD0BF1084B48BE8AE50C22065C02B0156812583D9C40E9B9BBF417B38D838D1BD9C6950A20F2D6F95CE0797ABC466C74DE2A4C6B379DB1BF02FB9006F853A8BA89EE809A64F0DCFE67C8E64F8F81A6FC0B28BEE6F8C3173B67BF55DF2BF06CF57D17FDFADC61FBDFEC77949E2745963C6815F0329D0679C66F9744B22687917C0DD62B033FB5270549C497AAD32C6775B28EB1BBD928BEF2F257EDBB692AD772FA93FFAA4DAEE39044B3D9F6A62C7385C7D25
			517327C31DC5D23F1511E2497DB7E5517D1A212DC3EDFF4D209F732BC79BE2E34CFFEA1899E3AEEAB04618CFB5CC45184FB498E34C658DFA0C291E5B9AB0BE8C41BE6B575A7DCC63DD50F8AF53B111F7DE74A8DE0D7F9A158B796D82CE28FD6BC1659A273CFE15172C63DD603F3DB4A0EEG153DF210775D500018F6D014A4D6FE1736917DA1C1516C71C7AA2398F06964CE57F8A8B7430C39C59CB3AA924711FBE9DC0C473244773D3DA9767AF4B5991FCFCEB7991FCF4EB7CD7F1D4B5526D8679766E39B905E28
			6FFF543D5FD5F0674F028F5D51B9187D9F24EB3282EC3FC6DB13DFD2DEAA14090F736CD63EF65BB55E73145788E53B46BBD8425BF9FDFCFF3FD59231DB0A6EFD34644E5B703EBBBA8753D9AE65B60C536C3028145BB0C1636517F15B4C57F88F15713EABB55E5AB25EB7D9639D5E457B26223D69EF1955C59C6F667B814F833583F901E67B79D920430A75A1C17DEE23CD16F0EF335FCD87687A4D03360B35768B685A7B7C82A4DC514D5AAD273CB7001234F6FFA842382040C6E4986CBF704BE9126A4776972DB3
			5F853FE11DFAC6C9537AB54B6C1E39971E9E1945F1B6F6F749F4E3B3F963296D5F863A89FD19FC997BA37255164DBC4633F48D568245E9E3D49C080CF1F657F4E3B04C56DD44316EDC197F46FA33A87AB75613997A5FC81FA80A7E0D957BC76A130A22FFE3BDB6297FCEEB74EF2C3CBF53614787093686AF6EC4CC6D97464BB9E42F277ED13D37F1FF9D258387EA54362F976133AAD0251EE9D6DC567FD6DBDEFB41700EBA6D72AC53AAF57D526448F9428BE79A06894A0BB9A6288B40DA87B85E7596EB368BF03C
			7CA0ED3B8A4AA9C0195739AF7EF3A915ED2CCA157535B2DA76D729B120EF8AAE1A0998BEFD339957388C66E9863A8424G4D86CA384EF3551666EA929BB33343CF84B2B3F9BEAE3EBDFC397FB67B79EE399F43F15BA8DEDBFCBD9A5F65D17E326B5178EE2E361609D0579864B67AEDF36CFC7EEE734CF86B4F4D46F8AB6350D43C357910B15E5AFEC80F3718DDAC3ADCD42CFB7F4762BB1F171E3E23F3307F4F727DCE9196E37E5BA4CBA1BB54EF7F4A5947FF21A04951B6FD87C5F3C9E37BDA4CEFBDEAB6114C8E
			3219D93B03E42249BAC89635E887B103354378C5340930BF5446943ACE7E0F2EBB5A986F79683D3EC23268458FFD8AE3658A49444B9C18FA4764F81371F8515D16EF6C9BB5116C93E47320CB8AF8DDB0A57DC84CC15E9F20F3636F9412C2AC8E6C5BGFD9BE85FCF9D44648EA924B442F7A31F557685DCEE811AEC753802A3F48414B47BA259BA989C9A2093A844F47144C518161630FA1B03ECD33C74779190D7A1C51121FBF1932B951FD2BB10CD09091B82740F707B1E1AAF755F1F245D630B79FD4E92C57D2E
			D4097E566EED7A8DE476284DDCB14A7751E73B717F30447A9E224F6712A445A50D9C73FB705B885B04A2C4F0703B3BF17C90EF23D6323ABF6DC63907F5FD7E9FD0CB8788A3225FFA5692GGECB5GGD0CB818294G94G88G88G810E96ABA3225FFA5692GGECB5GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG9093GGGG
		**end of data**/
	//}
	
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
				ivjJPanel1.setLayout(new FlowLayout());
				getJPanel1().add(
					getbtnPrint(),
					getbtnPrint().getName());
				getJPanel1().add(
					getbtnPanel(),
					getbtnPanel().getName());
				getJPanel1().add(getbtnPrev(), getbtnPrev().getName());
				getJPanel1().add(getbtnNext(), getbtnNext().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}
	
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				getJScrollPane1().setViewportView(getJTextArea1());
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	
	/**
	 * Return the JTextArea1 property value.
	 * 
	 * @return JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JTextArea getJTextArea1()
	{
		if (ivjJTextArea1 == null)
		{
			try
			{
				ivjJTextArea1 = new JTextArea();
				ivjJTextArea1.setName("JTextArea1");
				ivjJTextArea1.setBounds(0, 0, 160, 163);
				ivjJTextArea1.setSelectionColor(
					new Color(204, 204, 255));
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextArea1;
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
				ivjRTSDialogBoxContentPane.setLayout(
					new GridBagLayout());

				GridBagConstraints laConstraintsJScrollPane1 =
					new GridBagConstraints();
				laConstraintsJScrollPane1.gridx = 1;
				laConstraintsJScrollPane1.gridy = 1;
				laConstraintsJScrollPane1.fill =
					GridBagConstraints.BOTH;
				laConstraintsJScrollPane1.weightx = 1.0;
				laConstraintsJScrollPane1.weighty = 1.0;
				laConstraintsJScrollPane1.ipadx = 557;
				laConstraintsJScrollPane1.ipady = 337;
				laConstraintsJScrollPane1.insets =
					new Insets(2, 0, 2, 7);
				getRTSDialogBoxContentPane().add(
					getJScrollPane1(),
					laConstraintsJScrollPane1);

				GridBagConstraints laConstraintsJPanel1 =
					new GridBagConstraints();
				laConstraintsJPanel1.gridx = 1;
				laConstraintsJPanel1.gridy = 2;
				laConstraintsJPanel1.fill = GridBagConstraints.BOTH;
				laConstraintsJPanel1.weightx = 1.0;
				laConstraintsJPanel1.weighty = 1.0;
				laConstraintsJPanel1.ipady = -13;
				laConstraintsJPanel1.insets = new Insets(3, 16, 8, 12);
				getRTSDialogBoxContentPane().add(
					getJPanel1(),
					laConstraintsJPanel1);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	
	/** 
	 * handleCancelBtn
	 */
	private void handleCancelBtn()
	{
		System.exit(0);
	}
	
	/**
	 * handleEnterBtn
	 */
	private void handleEnterBtn()
	{
		// empty code block
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{

		/* Uncomment the following lines to print uncaught exceptions
		 * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7170
		// create exception and display it for GUI problems
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
		// end defect 7170
	}
	
	/**
	 * handleNextBtn
	 */
	public void handleNextBtn()
	{
		ivjJTextArea1.setText("");
		pageNum++;
		for (int i = (pageNum - 1) * linesPerPage + 1;
			i < pageNum * linesPerPage + 1;
			i++)
		{
			ivjJTextArea1.append(sb[i]);
		}
		if (pageNum < pages)
		{
			ivjbtnNext.setEnabled(true);
		}
		else
		{
			ivjbtnNext.setEnabled(false);
		}
		ivjbtnPrev.setEnabled(true);
	}
	
	/**
	 * handlePrevBtn
	 */
	public void handlePrevBtn()
	{
		ivjJTextArea1.setText("");
		pageNum--;
		for (int i = (pageNum - 1) * linesPerPage + 1;
			i < pageNum * linesPerPage + 1;
			i++)
		{
			ivjJTextArea1.append(sb[i]);

		}
		if (pageNum < 2)
		{
			ivjbtnPrev.setEnabled(false);
		}
		else
		{
			ivjbtnPrev.setEnabled(true);
		}
		ivjbtnNext.setEnabled(true);
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
			setName("FrmPreviewReport");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//setSize(586, 424);
			setSize(
				(int) this.getToolkit().getScreenSize().getWidth(),
				(int) this.getToolkit().getScreenSize().getHeight());
			setTitle("Preview Report");
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * initPage
	 */
	public void initPage()
	{
		File laFile;
		FileReader laIn = null;
		BufferedReader laBr = null;
		String lsLine = null;
		ivjbtnPrev.setEnabled(false);
		ivjbtnNext.setEnabled(false);
		try
		{
			laFile = new File(fileName); // Create a file object
			laIn = new FileReader(laFile);
			laBr = new BufferedReader(laIn);

			ivjJTextArea1.setText(""); // Clear the text area
			ivjJTextArea1.setEditable(false);
			ivjJTextArea1.setFont(
				new Font("MonoSpaced", Font.PLAIN, 10));
			while ((lsLine = laBr.readLine()) != null)
			{
				sb[lineCount] = lsLine + "\r\n";
				lineCount++;
				if (lineCount > linesPerPage)
				{
					ivjbtnNext.setEnabled(true);
				}
			}
			if ((lineCount - 1) % linesPerPage == 0)
			{
				pages = (lineCount - 1) / linesPerPage;
			}
			else
			{
				pages = (lineCount - 1) / linesPerPage + 1;
			}
			for (int i = 1; i <= linesPerPage; i++)
			{
				ivjJTextArea1.append(sb[i]);
			}
			// textarea.setFont(new Font("MonoSpaced", Font.PLAIN, 12));
			this.setTitle("FileViewer: " + fileName);
			// Set the window title
			ivjJTextArea1.setCaretPosition(0); // Go to start of file
		}
		// Display messages if something goes wrong
		catch (IOException aeIOEx)
		{
			ivjJTextArea1.setText(
				aeIOEx.getClass().getName()
					+ ": "
					+ aeIOEx.getMessage());
			this.setTitle(
				"FileViewer: " + fileName + ": I/O Exception");
		}
		// Always be sure to close the input stream!
		finally
		{
			try
			{
				if (laIn != null)
				{
					laIn.close();
				}
			}
			catch (IOException aeIOEx)
			{
				// empty code block
			}
		}
	}
	
	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport("c:\\test.txt");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			Insets laInsets = laFrmPreviewReport.getInsets();
			laFrmPreviewReport.setSize(
				laFrmPreviewReport.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmPreviewReport.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7590
			// changed to setVisibleRTS(boolean)
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of .RTSDialogBox");
			aeEx.printStackTrace(System.out);
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
		// empty code block
	}
}
