package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.data.FundsDueDataList;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmFundsRemittanceACC018.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			04/18/2002	Multiple Line Items overlap each other's 
 * 							information 
 * 							defect 3571
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the user help guide so had
 * 							to make changes in actionPerformed().
 * 							modify actionPerformed()
 * 							defect 5147. 
 * S Govindappa 01/31/2002  Retain the cursor in the table at the 
 * 							selected item after partial payment. 
 * 							modify setData()
 * 							defect 5353 
 * S Govindappa 02/27/2002  defect 5353 again.  Same method. 
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/22/2005	Java 1.4 Work 
 * 							defect 7884 Ver 5.2.3
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel() 
 * 							defect 8240 Ver 5.2.3 
 * --------------------------------------------------------------------- 
 */

/**
 * ACC018 is the Detailed screen for a Funds Remittance record
 *
 * @version	5.2.3			08/17/2004
 * @author	Michael Abernethy
 * <br>Creation Date:		06/12/2001 9:48:44
 */

public class FrmFundsRemittanceACC018
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjlblAmountDue = null;
	private JLabel ivjlblAmountToRemit = null;
	private JLabel ivjlblFundsReport = null;
	private JLabel ivjlblReportingDate = null;
	private JLabel ivjstcLblFundsReport = null;
	private JLabel ivjstcLblPressPay = null;
	private JLabel ivjstcLblReportingDate = null;
	private JLabel ivjstcLblTotals = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnPayInFull = null;
	private RTSTable ivjtblRemittance = null;
	private TMACC018 caTableModel;

	// int 
	private int ciSelectedRow = 0;

	// Object 
	private FundsDueDataList caFundsDueDataList;
	
	private static final String DEFLT_AMT = "000000.00";
	private static final String DEFLT_DT = "04/23/01";
	private static final String FUNDS_RPT_DT = "Funds Report Date:";
	private static final String PAY_IN_FULL = "Pay In Full";
	private static final String PRESS_PAY = 
		"Press \'Pay In Full\' or Select a Line Item";
	private static final String TITLE_ACC018 = 
		"Funds Remittance/Acknowledgement   ACC018";
	private static final String RPT_DT = "Reporting Date:";
	private static final String TOTALS = "Totals:";
	private static final String ZERO_DOLLAR = "0.00";

	/**
	 * FrmFundsRemittance constructor comment.
	 */
	public FrmFundsRemittanceACC018()
	{
		super();
		initialize();
	}
	/**
	 * Creates a ACC018 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmFundsRemittanceACC018(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC018 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmFundsRemittanceACC018(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE	ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			ciSelectedRow = gettblRemittance().getSelectedRow();
			if (aaAE.getSource() == gettblRemittance())
			{
				FundsDueData laFundsDueData =
					caFundsDueDataList.getSelectedRecord();
				String lsTempFunCat =
					((String) gettblRemittance()
						.getModel()
						.getValueAt(
							gettblRemittance().getSelectedRow(),
							0))
						.trim();
				String lsTempDueAmount =
					((String) gettblRemittance()
						.getModel()
						.getValueAt(
							gettblRemittance().getSelectedRow(),
							2))
						.trim();
				String lsTempRemitAmount =
					((String) gettblRemittance()
						.getModel()
						.getValueAt(
							gettblRemittance().getSelectedRow(),
							3))
						.trim();
				laFundsDueData.setFundsCategory(lsTempFunCat);
				laFundsDueData.setDueAmount(
					new Dollar(lsTempDueAmount));
				laFundsDueData.setRemitAmount(
					new Dollar(lsTempRemitAmount));
				caFundsDueDataList.setSelectedRecord(laFundsDueData);
				getController().processData(
					VCFundsRemittanceACC018.ITEM_SELECTED,
					caFundsDueDataList);
			}
			else if (aaAE.getSource() == getbtnPayInFull())
			{
				Vector lvVector = caFundsDueDataList.getFundsDue();
				FundsDueData selectedRecord =
					caFundsDueDataList.getSelectedRecord();
				for (int i = 0; i < lvVector.size(); i++)
				{
					FundsDueData laFundsDueData =
						(FundsDueData) lvVector.get(i);
					if (laFundsDueData
						.getFundsDueDate()
						.equals(selectedRecord.getFundsDueDate())
						&& laFundsDueData.getFundsReportDate().equals(
							selectedRecord.getFundsReportDate())
						&& laFundsDueData.getReportingDate().equals(
							selectedRecord.getReportingDate()))
					{
						laFundsDueData.setRemitAmount(
							laFundsDueData.getDueAmount());
					}
				}
				getController().processData(
					VCFundsRemittanceACC018.PAY_IN_FULL,
					caFundsDueDataList);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				getController().processData(
					AbstractViewController.ENTER,
					caFundsDueDataList);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caFundsDueDataList);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC018);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Sets the total amount to remit
	 * 
	 * @param  avVector	Vector
	 * @return String
	 */
	private String getAmountToRemit(Vector avVector)
	{
		Dollar laDollarAmountToRemit = new Dollar(ZERO_DOLLAR);
		for (int i = 0; i < avVector.size(); i++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) avVector.get(i);
			laDollarAmountToRemit =
				laDollarAmountToRemit.add(
					laFundsDueData.getRemitAmount());
		}
		return laDollarAmountToRemit.toString();
	}
	/**
	 * Return the btnPayInFull property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnPayInFull()
	{
		if (ivjbtnPayInFull == null)
		{
			try
			{
				ivjbtnPayInFull = new RTSButton();
				ivjbtnPayInFull.setName("btnPayInFull");
				ivjbtnPayInFull.setMnemonic('P');
				ivjbtnPayInFull.setText(PAY_IN_FULL);
				// user code begin {1}
				ivjbtnPayInFull.addActionListener(this);
				ivjbtnPayInFull.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnPayInFull;
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
		
			D0CB838494G88G88GBCF712ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBC8DD8D4E53A5435343252322B1996959AB72948482C35DD5BF56F5A665E6C494C1EE5F76DD6FB5D1BDBD46EAE7766F3BB9CE6G0D94034CB63529CCB18D07C0E111CD405120A839B0A98A86434C91868FB3634CC1C079396F7B7D4D61F08634554B733C1E733D5F4F7B733D5F7B3D5F7B1DD14A79A05A1AA267C8D20AD5E2FFC5A912546C10246C9501F7B8EED9D016A55D5F07G6B246D3996F8A6C3
		
			DDE016ACE7C9AF66E4C0B9B3A44B0589327CAE3CE7CBC9B99B4B61A5821D95E5507F81655F0627F3D9AF2713CE4AF59D1970DC83100534G22C2C87FE44786062F07729A760E108223E6E858E6FA51869DFE9EEB3702BD1711B617F7ECE475B5D0EE83188330D08B0F390AF0F7D9C9B1ABB70E7F353462170F49EFA1AD5D789C6C1D342F13BDEDD21D0C0AC81572B6FE8C4F521A132DDF385CEEFF27CF737ADACBCA8E86BB8EF67ABC21DA25432BE9AE1FDB1166F5A2EF8B0AB2C8BFC99A8BE54A4793EF15B2BA93
		
			CE7EDA2AFC7EADCBA4BEB2CED37ABBCF0779A8CC79712A4453B876BDC42F58CF05F28CG65F1D46F814E235B5F1127F5FB7CDA1EB764498BEA21BC376AD5FCDADE18793CCE2F997F657B775B73D1DFD40E3FF4F0B9ECDD329C5D05385F6DC17A75709EG985345657B7BB6AEDF7D597F1412DBDE1257C6106F79AEAAD7E6F7D83E7A3D6ED56F913BD9570FF3DF89F5AAC094C08C40BC0067815EA7763A2077D3F81637E87B7D9D9DFEDFF9A0D021FA025F78BD0A8A6FA5A5D043F0350A5B9F74C8923549A70AF61A
		
			7043E1E577E0FB8C5B5091095BD0DC6D331236F52D45EAA25B94F92DA52B070E3125A7AC9B156B2E81144B8178E600EE00F1G71G8BFB38DC6B7B8AC6122B4A27A9C11FCB2D6BEC893802DAC527D775A8C1A65F7D9D4349E786345F4F4E3B9F55CBEC01DEAF09773DCC563ED1AF09310B6465CC9F16B3E13DD05AABA3525EF2E668FCA0D41EC1BB2CABC2BBAC067794E62394FF1C60476A70F45D3CC06C964BD320CEBA4B6D36FE8D17215A721414151FE4AA43241F3EA0FB4ED2FEFA4F9A67760A9474C531001F
		
			8710883082A027D7166DG0EDEBE3F2E3EDD67E837B53A3925FD7F5A3F9E1ECA3796F4D579C2E4359EF6853D2E96D589C92C4D477D48FB4B20FE39E4BD390146170ACF893AB42F5F87233BBA94B02490ED135F3F8E5B8495D26B74F6A8C020A3A071F17F561F8DCF5595523E8BF8DC1A92EEC16B4B7BD11727DD07E1C498G5EA774224E1791BF680177A8G2A2BD9B26216C0B9C968E5CE7F5B707C9A8CDBD1CBCA9CDEB732618A76F07A33865017A8274107B91400627340CB0855BDBC30AA2C1793FE41734F88
		
			9E5748FEE0BCA4700F8E2C23F898C32FE5DE7F8429F7E9DA505B5229A9F50A067EB25C7F9942CBE850728BC9EC0D27220DD97AE4F9A3404EBEAEDF0B142D132F5A6FBF5999F0624CB1BDE57BD0A7E5503E3E0F6F1FC70BF0BF6D6D63F67AD2BD37D347DBCB251DCF3DE4EA27E3B256DA2674D17BCC68B32EB5B799B3CC6345BD7567396EC7025E3E701A25B4ACC5971AC62A6E1D6A5DE6C53DA7826DE4008D7DDC6FD3C1F6325EAADCA1052F30C3C118B1459336B25A767A1448B6B6B9455C4622D34CEDECFA4A70
		
			B6F4F70A198D513A7BC970656F7269EC72211437499A767994423468A2513D7FA5A76A426B73EAC73C1ED6C52B731ED1E01E7B51365EA0EB508E6F0EFEEECBDD9FF3DB5AF2A6D11A5B7428EC361FCD1A36CC1E52CF67B33EBFECCB14279F4928F3C5D5BAC0D17BB12C2078C7487AE6F866DF6ECBB5EF7BE0AA5A63C9250735FBB8B5CDC2BF22F6AAC236F8571B2863203783268A97AC0B91BF7569E34405409F4613B983321CBB40E3A2F78B6A4DA4CCE3F6F3B48D79AA0376CEGF5005B4D5FAC6F6956ABB3187D
		
			FED5256A977332551A0573281C6AD4E0D4DC50DF8BD9BE333EED28BB4C6422753B2DA8172A73852386D02E7545BC66423D0262BEA33817E1C1BDEA61FB6FB7D97A385693FA56F48E91668F5039CB9CB06EDB57246338AB811F9945E50F376AFDD5F9A864F7FB49364066A34D32C79AA94EECA5BB0DCA024CDAE71D436BD27D2D957EEE9ED7CEA07E8E7D03AD0A4715C98192D7C2B9GB0C6705314B6541EBCE92B61F9429B8CC1BC64D13A39CE5B53D0B738CFB1B40F5FBE576BEA5E8766719BC2F494731DD1C6DD
		
			AD4AC0BE96827EF9415B8BE96BF43A02D8932C2B95E61273F3FDFAAA35EFD61E18EE6576ADF17FF823EF3858AB197111F98963871EB44E711CC2FD95DEE20CFFFC846F3844987F7CB940639FA7544D3E046F473FDC05F2ED3C94762BCB39DCBBB35E5249D563777AB4A153B39938C67DC1EF2B5747F13FA5B80834095322FBDABC1963D2985389C0F13B82086B0672D8016BA638E8A8A7DC4A772576D36897660A32C64A4BC4F9E5006FA95866C26EAD61FD6B1DD31CC66A859EFF0D5065632BF00E3AC579C9D206
		
			30072D35FB3B50C71C00E08D360F3A006216227F89F5768B617BB221EC83A883E8860882C8A5E39D8C60989DFE0FCBC5BBA27D0E8738EFD9A65ADDCB5AE9DE0D58ED69A8BC5B4FBAAD31F659529525E8A7DF71F347C10C9C777B210461138F7BD0FFBC6D145E9FC70D2231CC8CBCE78350F57FC7E293A863C240E5DC0638777DB93AFEC8338E7E4943F1C97DB7036AF13DED17747583036AF13DDFE151572F8D686B2D3A7E54B7BC7689BF372EFC6CB9C93D7FB7917383B517D1DB5358B35977D44A163D0BD7D1BD
		
			E98134D1AD582950F1233B74BE27F8752FA37A67B8B65EDC7674C4BF15C247F83CC1BFC656641F13B5EDB6C6B26BEB395C183BF8EDBB4E8B71576D2E532E6E3CD097C9DEFC05332752F9794D298C261B624B39BFF677223F88C0F98C62AE67EB6F55E17D1599D0D99EC967FACEF868B7526C4B274AEFC610E5B613E189FB0E6BF8AA851F54DF0D3623BC9923E5F98BC0A5C0B30086B0F6B477DF2F1FC69EA156BB6071EA6530673B5B486E41C34043DE25AB0C4605C5473EB31F78A298E70E982B22EB87BC8F3B5A
		
			59498C07748749C8002C897A83CAD06B21D83243B3BE27FD0ED233FDE957A71146GEE3E02E9B954528E3B075355AAF17F736F270CFACE9C9D76F5C923077ABD6A7F5F6DA3E710B1547F3FAB70BB883ED407277B02174CB316B52833DF417705198538EE16C3B963CA589BG4AG1CD7F23D5C356BA32AE3937D9E00A094FC841ED2B52E03F176487175C47B48E75BE8BB6E79ADFECD73F76078A25636233EB4FF00A0A9EE069DED8E3628B60DB6E578FB6DB83F2AF20220A96E1EFC541D0855699472F3E7BE6AG
		
			63EA08E03CE7C898D4878DFD2DF46F7AAF32EE822013D1D7727DAC3F176FFB31D772B533298F67F281141783E4DC85B1AA00832091A0GB086A086E08E40222BF83FFC52AF894AD9E3E1EEGEAG82G93GE683A4822C8458F835AC17DE4D7B79FB636109E3696DA3061511A672E2341FB106760E2B4EFDEFDDF40EEDF3193C4B8D74B3044CB7935E5FB1744BE6CF744DE67B71BAC3794DF364FB8D337DF375E3E843D683BEDD5857C38511ED7C110211ED7C1785A456855BF4768418FDBEDEC0622D93A7C20AD8
		
			CB0B8A32842EAAD4638FF951B269BE149544762A2B475F896D67DFCD4ECAE7785E5C88E58DE08AC0BCG46D4D957E01B4A5E8D4348CE473EAB9B759C525C55ADAABBD2857CC1CD544FDB0FEDD5C3A52D7BDDE33668CBEBE099B9DC1A426A3FD033E85FC11534EEF98F7A282F6A5CC1B803613E15CF71F9FE54BD5D497259B8CBDAF06EE1A9EB1867028A061F38F70D20DF93D4C2219AD78F676D2FA427CE6B1CFE4D2506A8BED74D24BC15F7602155514978B95C1E2547BB7D64E8B90E68D16D53479B7557D09D77
		
			421356BFF94C2CFE0928DF7A39D97DF2D1EFC9B32B172E853FC26230F2D9BF0FB957527AB2F8BAC59BD723B1D643B6AA54C709B60F27695BCCE263A442B369DAA14BA6B3DEB246717A6C20D97DC6D13F40343FDD545F50EED66F00FA3AF7E51C65F7A17558E79CBF7767B7E3BED644BCF92EAE2D24644B2057D361EA2DA6AB0346BDB5E7F9BCBAFF9C0FFB7A491852F8D95EGE09F4FF3A65DDBF97C92B77DA94979FB737B1B254E45F265F86A5B547161D8CC5D6BDEC547E9BF494759B8E3B1290B94EF46327E8B
		
			F4632C11785960359391623AEAD78B0F6BEE6F63676C4C6B389CFD877577D0F356AE0E9807B92EE37168F5E17A9BC18EBA766A869EB346DC47637BF78BB83DC4015B1B465B65DC4F67277540A6F35E43ABDC6239029E190FD9FDBD1F23AD7BF00EC6C1F98AC042751C560D0D1C56825176C58B0907A6005D8194CE60BCFC31E337F4BEF90C8A1DC7613C25277373D47584CE4F3609708665A9G8993B8EF2BB78B5E846E2BA0174DF2834725EE6638D201DB504EF15A8DFC1FDCF376075D75EC7C017D3EBB6FFEB4
		
			6EFC152C27389BE4F1F621785789FE368ECF5774A8312667C1DD6EBF73F874773B515E47C2B996E0AE409200E4001C09BCA675965A25916ED71C7EC0B5EC502AAE75CF47AFA3F74E39B01683208DA08AE082C01C987F3EC23C1F0BF49628757B491198CF47AEAFD88E890103AE4C1ED09D46761969F06EC43A3E96316732096158081DCB0A518F2E847C06C96013GAA815AA6F13E5EA89C715E70C077B07706EB9B4C789AB30972B1E55250580E59D201D93FF856FE9EFBAE5175277D4A524C7AADE76D249BA351
		
			6B17F93F9CE813EBE8D7EA520F5A5AA462AFEAEEB4664AEEA378669B0D3932CF7771DCD9G6A965DA8E253C6A35F451343B42BA7C7225FC46EA71AA79B69FB89BEB059C87F958B279F85F54FCF6674B35307DB0315B7C522BF017824461B0CEBF0AA41375DE4DC03DBB771B528C15D121BB87D351B4D6817CE21F4EB26C41A37C3C133FE9E563E1BBD47CEB15AC906A93DE856AE01BD678F6977C43BD93FC456AE09BDADD30D7D124FCA62BD25B77CFE65598BFD07B5D4C737DFD49A54CF3C3CBB7279644F3B4D6F
		
			38D66EB63F6312778CFF4735FACF64BB2EF5FB065EF1D595C5662D26E864335351A2737CC0D351507C002748BCBF50DE244F8F50737CCFC88C3581ECA5F7AABF63EF6967E77CC201739DC35CA8A847CC65FEF9E6F1E4199EACB65777CF0A4D757D7362616F2DFFD59CE9BEA8FFDB9B389C33854F239B389C73842E2000374B3919637E3B103733895CADD6127B0072B801BB12467B46885C6CB45EB7DE60EEA0B11DF40BACEF390563DEA238E2A8B78BDCD5AA6F2B5DA2FC70BE4C334E9365D8D2DEAA4A76FDE66B
		
			885BDF3835DAE5CA63C25D93D0596FA7312CA59A767CE82E0F426F39BECA23393C0A95659FAB4AFED20E91653FD80673AB586F9F57C716F5B89A7F28DE286C9F278BDB9532CD46E384771F3A771D1BAE3E3FBEF0D1E930B53BD92C45DB451A5DAC5622401D8872F681017BC510376B953856CD08DBG656582770120A15D46F1D102C626405512B3C863ED7A3E4B5AF95FDE512E6AA46FBBC6604E6CA15F7CC0B979B6FE6F34380F5C17008324F7CEBF7F9A67EF676D644C586F98411E741089FD90B0E708B10B8D
		
			5E7732F7CA6B32E318FBEA73873DE7B0B6D74BD5EF2B8FB3566CDE6C96623B516FEA50177273DAF5181F1B9B066347BA82BF51705E4A5E69586B3F8D0F3DEE77C832DA0799BB8E5E5D03E41DE59DDE5605D6AE6BECA16B41C3E1FE9ECF9BC9566178F9DEA7AB65A70EFCE3FCDA89EADEB79CBF48813F42952C712BDEF78F6369974477633DDC526D5CBE2C296F8D124358135FE9FEC876083F8358E3E4194A501CBA12CED7B140F78D7A0D0921B2106F83617D99F8E9CA61B214F8B0C723E1EEB27C89893B77FCE6
		
			1FBECF969BC373734B6039B2064B3220F1E8CE76B911034B1986FC83B8815A26515CBE15636B6FC71203674349D9817BCE8B4B918B6FDF4273B3B19723BF42383EC5735538FA2AFC87BBD5156659222D7ABCDB8263E145B44EC7C97AC8B601FC24B3BA9953E5F96374B09FF6F8FF0571C16D6D419A5D5A5ABC120C69BA1BB35ADBA90CFDEA505ACA7C5F70583F8D0E44771B4358F2A50C7D59A0DB6E5FB47C5A1A34092FAD67F43E365E288D73B3F1D8D9538D7CC4CD277C3C4FF818G6572C17CF85BA371C3E997
		
			B943341BCF8EC7FB1501F63401F68214778F22BDFE4F703A1831076BE2AE747DC022F7B8A46EE3FF1447BFFDA7C97C0E7859CCFDEE547174D479E349BCB739FCBA0DDBCA6F8867A65B9E7CEBAA730147EC70542596736A70DFFEA5994A2B2D7A3252C969B79D8973F2A9EE69CC3E7F301E7B98530E067B7DD6AEB47453E5B5738E9F28F5D66DAF2FEE2A5BDFFB283A3A223C36293C2E695B83879C879C34FFF16A1EE17AD79E2A2DBAF668DB27798812E42667213824FD146F7947A4092F2DBBBA71427A3C3E6B22
		
			1F2D31DC683AB29C1F7849BBF8AE746E8674B7CB201C8410914B7376EF9E6779859B606AE3F9CE3660737724A1F97B032A3F2B1A39CEC62B6EB84F5B0F0B6534AEA1E34E0772D20064D8B31BCB5A97D997AFB604F5E14BAEB8DFDD5074B0636FE9AB4F57E67D4B7972D1FBBCEC0F1D5F075FEFEF8A5B58BD1B4E45B6A353785766616D628F2EB02D1F6EB997DAB4CF79F48B7A18CA105983B086A09AA081E08140D20095G99F7C26C8AD08CD08D508C20810C82188490F7A7BFBF179C4618A0D23E15DD6868322C
		
			BCB6DFDF068BF9AE0C3368CE73734DB2402F0CD00710B5037D0EE3C6241C5AC70D647787B30CB9359D846F18E14C29A5B4F01B2F013A04997C8C336E788F4B47FF70837B551FF7BFAA571F487A5A90E71477FF88FECB1CD15E51E20D5B21EEC29C17B751EA241F8CF519F7B1BFFE57D0FDD3DBF831A5F2AE6555161173C32F37186707D25CC373C3E9EE737CD026FB6877A36B5C68B3DA41966BC53B7582F7D4605EA7E3A26E08572335897C076E558CDF29A0C10E5FC6786AA238FA065B29439DE538FCB74616A0
		
			B2EC453C6F9EB71E2DCEF8D5152DA352466B8BA91A5047BE4223B76052716D90B8641B1E5D0A09DFAF04F929390B1F672EA6B88F143B85AE3382F1D320BCC7600E925CC2A8A7895CF402335C8DBE60EE0E3B7F0848275D4D6DA437D192B61B2FFBFF2861621F6D1F7CFF20717A7130CC166F436FD79F3F783427DFD49AF4CEDFA3FBE0BD4C27AA6639D464872384EE2A488F0E3D1B1F178E1561B82ECE4DDF2B043CE7943C4962673D36B23E37460B76B1E56CDBD7F2B7CB4FD7BFAA63672BE550CE3AC7169DG75
		
			G18AB247DBE2EA05F3334793B8E3373E9355707AE03517AFB850FB3827770BE010AEC5627D2443E34976DF32568B3CA74F938823F977162A734EEC5D56B82AEB7EC21347DE252FE92340D81205F77AEB842F9DF007C82C87130DEG4A6279182B095EBAFCCA075F67F53333B0415D8A509C4F4F9FBF7620ED8562499E14B672B7BE83E5785B4AC171A70973724A3FF39EEA2ABDD0D7F56CC0D3F5F91D33E97F216A6F3E79167B7B674A781D55EC2015944FE33AA569A35F339BFE176A7C4670BB549D65DC273977
		
			72F86FE8B939D303F2B74038FB39DE1C95C87704CB8DA9C226DB876128DEDEB9A1097A99C770FB911F52E563E1E71E438BE7997CB1DB2EFD274D3E1D72716A914EC76C3D7C7BE5DB3D717B6571A97A6F0FB7B4983FDF467E617A2F9A0C5FAF8F6E5F5BE07CFE1977E76705B4FCEE2B59464ED0730F6B3FE7366D08B57D3EA47978137252FB59BD584C70994A89E7A86AA37ED0B6D21CD0BCF370DE4D724DA3765BECA8333CFD798F0BCBFEC56C40B9539817AC2676B1539817B4150B5FCFC35D4219A2CFFE64FC69
		
			5318B72B8D57D461FD30BF82E8GE3812281E65F4763585D2D39D264B8169EEE42E16CA6B237F333F15E3CBE550BDF9410DFEF8ADDBD09E9G761E28FBD74AAF7E5E51FFD1E9D0FD3D5796B95633350D9C6BEDEFB30F75F6358D0D758A5B4CE33D12B6FD2CC77F1F04462D3B40F3447DD132EA52F6720D23AD1C915CD56EBE6973F7297879A666C1ECB69BF5E31A74B6EF0BDFB65A703B4692CD4A30976E7A3CE017BDDF13560AEF9ECDAA37132F95ED5BEADCBD36AA1F8D33F65BEC7E202DCE411F2359DCB65CFD
		
			ECD51A5221C969746BC598E0151D7C65596D7A77D0D0DAABDCDC950987171EAEA8C9FD6E76D4D932D4C9E9D4E7D51285852F122CC4AFD5D2AA682C9BFF8F0CF1A47ACA74C93650A9291FF81DB406499C044D5035CD9F0465ED4358D42677EDG96014D510D10B1881B435AE68C9AE1939BF755A0EAFB99B6F590B6130DBD980BA36019B3D5135E54A99C747676502D217C1AE71E0EEBF26448128C2AEBD3282E2CDD985F93ED51BF6A17378F68FF4B11FC591C083F87C918453EEB18E57C8D1AEDBB091D9F00D860
		
			816EDB823918230C745B023A8E172A02C91CAC77F970F78695BD1A923ED37EE4BB0E9F62EDD8A5BBEBECC73E5BF5FD7E8FD0CB8788534C2BD8F797GG08C4GGD0CB818294G94G88G88GBCF712AC534C2BD8F797GG08C4GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG3197GGGG
		
		**end of data**/
	}
	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				// defect 8240 
				// ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				// ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				// ivjbuttonPanel.getBtnHelp().addKeyListener(this);
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
		return ivjbuttonPanel;
	}
	/**
	 * Returns the amount due
	 * 
	 * @param  avVector	Vector
	 * @return String 
	 */
	private String getDueAmount(Vector avVector)
	{
		Dollar laDollarDueAmount = new Dollar(ZERO_DOLLAR);
		for (int i = 0; i < avVector.size(); i++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) avVector.get(i);
			laDollarDueAmount =
				laDollarDueAmount.add(laFundsDueData.getDueAmount());
		}
		return laDollarDueAmount.toString();
	}
	/**
	 * Return the JInternalFrameContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJInternalFrameContentPane()
	{
		if (ivjJInternalFrameContentPane == null)
		{
			try
			{
				ivjJInternalFrameContentPane = new javax.swing.JPanel();
				ivjJInternalFrameContentPane.setName(
					"JInternalFrameContentPane");
				ivjJInternalFrameContentPane.setLayout(
					new java.awt.GridBagLayout());
				java
					.awt
					.GridBagConstraints constraintsstcLblFundsReport =
					new java.awt.GridBagConstraints();
				constraintsstcLblFundsReport.gridx = 1;
				constraintsstcLblFundsReport.gridy = 1;
				constraintsstcLblFundsReport.ipadx = 29;
				constraintsstcLblFundsReport.insets =
					new java.awt.Insets(32, 28, 6, 20);
				getJInternalFrameContentPane().add(
					getstcLblFundsReport(),
					constraintsstcLblFundsReport);
				java.awt.GridBagConstraints constraintslblFundsReport =
					new java.awt.GridBagConstraints();
				constraintslblFundsReport.gridx = 2;
				constraintslblFundsReport.gridy = 1;
				constraintslblFundsReport.ipadx = 51;
				constraintslblFundsReport.insets =
					new java.awt.Insets(32, 20, 6, 12);
				getJInternalFrameContentPane().add(
					getlblFundsReport(),
					constraintslblFundsReport);
				java
					.awt
					.GridBagConstraints constraintsstcLblReportingDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblReportingDate.gridx = 1;
				constraintsstcLblReportingDate.gridy = 2;
				constraintsstcLblReportingDate.ipadx = 49;
				constraintsstcLblReportingDate.insets =
					new java.awt.Insets(7, 28, 8, 20);
				getJInternalFrameContentPane().add(
					getstcLblReportingDate(),
					constraintsstcLblReportingDate);
				java.awt.GridBagConstraints constraintslblReportingDate =
					new java.awt.GridBagConstraints();
				constraintslblReportingDate.gridx = 2;
				constraintslblReportingDate.gridy = 2;
				constraintslblReportingDate.ipadx = 51;
				constraintslblReportingDate.insets =
					new java.awt.Insets(7, 20, 8, 12);
				getJInternalFrameContentPane().add(
					getlblReportingDate(),
					constraintslblReportingDate);
				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 4;
				constraintsJScrollPane1.gridwidth = 4;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 492;
				constraintsJScrollPane1.ipady = 175;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(4, 27, 3, 39);
				getJInternalFrameContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 6;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = 35;
				constraintsJPanel1.ipady = 22;
				constraintsJPanel1.insets =
					new java.awt.Insets(8, 28, 20, 20);
				getJInternalFrameContentPane().add(
					getJPanel1(),
					constraintsJPanel1);
				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 2;
				constraintsbuttonPanel.gridy = 6;
				constraintsbuttonPanel.gridwidth = 3;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 120;
				constraintsbuttonPanel.ipady = 22;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(8, 20, 20, 39);
				getJInternalFrameContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				java.awt.GridBagConstraints constraintsstcLblPressPay =
					new java.awt.GridBagConstraints();
				constraintsstcLblPressPay.gridx = 1;
				constraintsstcLblPressPay.gridy = 3;
				constraintsstcLblPressPay.gridwidth = 2;
				constraintsstcLblPressPay.ipadx = 56;
				constraintsstcLblPressPay.insets =
					new java.awt.Insets(9, 29, 3, 12);
				getJInternalFrameContentPane().add(
					getstcLblPressPay(),
					constraintsstcLblPressPay);
				java.awt.GridBagConstraints constraintsstcLblTotals =
					new java.awt.GridBagConstraints();
				constraintsstcLblTotals.gridx = 2;
				constraintsstcLblTotals.gridy = 5;
				constraintsstcLblTotals.ipadx = 22;
				constraintsstcLblTotals.insets =
					new java.awt.Insets(3, 59, 8, 12);
				getJInternalFrameContentPane().add(
					getstcLblTotals(),
					constraintsstcLblTotals);
				java.awt.GridBagConstraints constraintslblAmountDue =
					new java.awt.GridBagConstraints();
				constraintslblAmountDue.gridx = 3;
				constraintslblAmountDue.gridy = 5;
				constraintslblAmountDue.anchor =
					java.awt.GridBagConstraints.EAST;
				constraintslblAmountDue.weightx = 1.0;
				constraintslblAmountDue.ipadx = 32;
				constraintslblAmountDue.insets =
					new java.awt.Insets(3, 12, 8, 28);
				getJInternalFrameContentPane().add(
					getlblAmountDue(),
					constraintslblAmountDue);
				java.awt.GridBagConstraints constraintslblAmountToRemit =
					new java.awt.GridBagConstraints();
				constraintslblAmountToRemit.gridx = 4;
				constraintslblAmountToRemit.gridy = 5;
				constraintslblAmountToRemit.anchor =
					java.awt.GridBagConstraints.EAST;
				constraintslblAmountToRemit.weightx = 1.0;
				constraintslblAmountToRemit.ipadx = 33;
				constraintslblAmountToRemit.insets =
					new java.awt.Insets(3, 29, 8, 41);
				getJInternalFrameContentPane().add(
					getlblAmountToRemit(),
					constraintslblAmountToRemit);
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
		return ivjJInternalFrameContentPane;
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
				ivjJPanel1.setLayout(new java.awt.FlowLayout());
				getJPanel1().add(
					getbtnPayInFull(),
					getbtnPayInFull().getName());
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
	 * Return the JScrollPane1 property value.
	 * 
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
				getJScrollPane1().setViewportView(gettblRemittance());
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
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblAmountDue property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblAmountDue()
	{
		if (ivjlblAmountDue == null)
		{
			try
			{
				ivjlblAmountDue = new javax.swing.JLabel();
				ivjlblAmountDue.setName("lblAmountDue");
				ivjlblAmountDue.setText(DEFLT_AMT);
				ivjlblAmountDue.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblAmountDue;
	}
	/**
	 * Return the lblAmountToRemit property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblAmountToRemit()
	{
		if (ivjlblAmountToRemit == null)
		{
			try
			{
				ivjlblAmountToRemit = new javax.swing.JLabel();
				ivjlblAmountToRemit.setName("lblAmountToRemit");
				ivjlblAmountToRemit.setText(DEFLT_AMT);
				ivjlblAmountToRemit.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjlblAmountToRemit;
	}
	/**
	 * Return the lblFundsReport property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblFundsReport()
	{
		if (ivjlblFundsReport == null)
		{
			try
			{
				ivjlblFundsReport = new javax.swing.JLabel();
				ivjlblFundsReport.setName("lblFundsReport");
				ivjlblFundsReport.setText(DEFLT_DT);
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
		return ivjlblFundsReport;
	}
	/**
	 * Return the lblReportingDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblReportingDate()
	{
		if (ivjlblReportingDate == null)
		{
			try
			{
				ivjlblReportingDate = new javax.swing.JLabel();
				ivjlblReportingDate.setName("lblReportingDate");
				ivjlblReportingDate.setText(DEFLT_DT);
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
		return ivjlblReportingDate;
	}
	/**
	 * Returns the matching records from the one selected in ACC017
	 * 
	 * @return Vector
	 */
	private Vector getMatchingRecords()
	{
		Vector lvVector = new Vector();
		for (int i = 0; i < caFundsDueDataList.getFundsDue().size(); i++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) caFundsDueDataList.getFundsDue().get(i);
			if (laFundsDueData
				.getFundsDueDate()
				.equals(
					caFundsDueDataList
						.getSelectedRecord()
						.getFundsDueDate())
				&& laFundsDueData.getFundsReportDate().equals(
					caFundsDueDataList
						.getSelectedRecord()
						.getFundsReportDate())
				&& laFundsDueData.getReportingDate().equals(
					caFundsDueDataList
						.getSelectedRecord()
						.getReportingDate()))
			{
				lvVector.add(laFundsDueData);
			}
		}
		return lvVector;
	}
	/**
	 * Return the stcLblFundsReport property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblFundsReport()
	{
		if (ivjstcLblFundsReport == null)
		{
			try
			{
				ivjstcLblFundsReport = new javax.swing.JLabel();
				ivjstcLblFundsReport.setName("stcLblFundsReport");
				ivjstcLblFundsReport.setText(FUNDS_RPT_DT);
				ivjstcLblFundsReport.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblFundsReport;
	}
	/**
	 * Return the stcLblPressPay property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblPressPay()
	{
		if (ivjstcLblPressPay == null)
		{
			try
			{
				ivjstcLblPressPay = new javax.swing.JLabel();
				ivjstcLblPressPay.setName("stcLblPressPay");
				ivjstcLblPressPay.setText(PRESS_PAY);
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
		return ivjstcLblPressPay;
	}
	/**
	 * Return the stcLblReportingDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblReportingDate()
	{
		if (ivjstcLblReportingDate == null)
		{
			try
			{
				ivjstcLblReportingDate = new javax.swing.JLabel();
				ivjstcLblReportingDate.setName("stcLblReportingDate");
				ivjstcLblReportingDate.setText(RPT_DT);
				ivjstcLblReportingDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblReportingDate;
	}
	/**
	 * Return the stcLblTotals property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotals()
	{
		if (ivjstcLblTotals == null)
		{
			try
			{
				ivjstcLblTotals = new javax.swing.JLabel();
				ivjstcLblTotals.setName("stcLblTotals");
				ivjstcLblTotals.setText(TOTALS);
				ivjstcLblTotals.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblTotals;
	}
	/**
	 * Return the tblRemittance property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblRemittance()
	{
		if (ivjtblRemittance == null)
		{
			try
			{
				ivjtblRemittance = new RTSTable();
				ivjtblRemittance.setName("tblRemittance");
				getJScrollPane1().setColumnHeaderView(
					ivjtblRemittance.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);

				ivjtblRemittance.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblRemittance.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC018());
				ivjtblRemittance.setShowVerticalLines(false);
				ivjtblRemittance.setShowHorizontalLines(false);
				ivjtblRemittance.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblRemittance.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel = (TMACC018) ivjtblRemittance.getModel();
				TableColumn laTableModelA =
					ivjtblRemittance.getColumn(
						ivjtblRemittance.getColumnName(0));
				laTableModelA.setPreferredWidth(130);
				TableColumn laTableModelB =
					ivjtblRemittance.getColumn(
						ivjtblRemittance.getColumnName(1));
				laTableModelB.setPreferredWidth(130);
				TableColumn laTableModelC =
					ivjtblRemittance.getColumn(
						ivjtblRemittance.getColumnName(2));
				laTableModelC.setPreferredWidth(130);
				TableColumn laTableModelD =
					ivjtblRemittance.getColumn(
						ivjtblRemittance.getColumnName(3));
				laTableModelD.setPreferredWidth(130);
				ivjtblRemittance.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblRemittance.init();
				laTableModelA.setCellRenderer(
					ivjtblRemittance.setColumnAlignment(RTSTable.LEFT));
				laTableModelB.setCellRenderer(
					ivjtblRemittance.setColumnAlignment(
						RTSTable.CENTER));
				laTableModelC.setCellRenderer(
					ivjtblRemittance.setColumnAlignment(
						RTSTable.RIGHT));
				laTableModelD.setCellRenderer(
					ivjtblRemittance.setColumnAlignment(
						RTSTable.RIGHT));
				ivjtblRemittance.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblRemittance;
	}
	/**
	* Called whenever the part throws an exception.
	* 
	* @param aeException	Throwable 
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
			setLocation(10, 10);
			// user code end
			setName("FrmFundsRemittance");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(580, 425);
			setModal(true);
			setTitle(TITLE_ACC018);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Handles the key navigation of the button panel
	 * 
	 * @param aaKE 
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (getbuttonPanel().getBtnHelp().hasFocus())
			{
				getbtnPayInFull().requestFocus();
			}
			else if (getbtnPayInFull().hasFocus())
			{
				getbuttonPanel().getBtnEnter().requestFocus();
			}
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		{
			if (getbtnPayInFull().hasFocus())
			{
				getbuttonPanel().getBtnHelp().requestFocus();
			}
			else if (getbuttonPanel().getBtnEnter().hasFocus())
			{
				getbtnPayInFull().requestFocus();
			}
		}
	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmFundsRemittanceACC018 aaFrmACC018 =
				new FrmFundsRemittanceACC018();
			aaFrmACC018.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 * 
	 * @param aaObject	Object 
	 */
	public void setData(Object aaObject)
	{
		caFundsDueDataList =
			(FundsDueDataList) UtilityMethods.copy(aaObject);
		Vector lvMatchingRecs = getMatchingRecords();
		caTableModel.add(lvMatchingRecs);
		caFundsDueDataList.setFundsReportYear(
			caFundsDueDataList
				.getSelectedRecord()
				.getFundsReportDate()
				.getYear());
		getlblAmountToRemit().setText(getAmountToRemit(lvMatchingRecs));
		getlblAmountDue().setText(getDueAmount(lvMatchingRecs));
		getlblFundsReport().setText(
			caFundsDueDataList
				.getSelectedRecord()
				.getFundsReportDate()
				.toString());
		getlblReportingDate().setText(
			caFundsDueDataList
				.getSelectedRecord()
				.getReportingDate()
				.toString());
		gettblRemittance().requestFocus();
		if (lvMatchingRecs.size() > 0)
		{
			gettblRemittance().setRowSelectionInterval(
				ciSelectedRow,
				ciSelectedRow);
		}
	}
}
