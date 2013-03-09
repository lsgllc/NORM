package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.data.FundsDueDataList;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmEnterDetailRemittanceACC019.java
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
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							JavaDoc/Formatting/Variable Renaming
 * 							defect 7884 Ver 5.2.3 
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	renaming of elements within 
 * 							FundsDueDataList Object
 * 							modify actionPerformed()
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	07/22/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * ACC019 allows entry of line item prices in Funds Remittance.
 * 
 * @version 5.2.3		08/10/2005
 * @author	Michael Abernethy 
 * <br>Creation Date:	06/12/2001 10:03:05 
 */
public class FrmEnterDetailRemittanceACC019
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JPanel ivjJInternalFrameContentPane = null;
	private JLabel ivjlblAmountDue = null;
	private JLabel ivjlblCategory = null;
	private JLabel ivjstcLblAmountDue = null;
	private JLabel ivjstcLblCategory = null;
	private JLabel ivjstcLblRemittance = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSInputField ivjtxtRemittance = null;

	// Object 
	private FundsDueDataList caFundsDueDataList;
	
	private final static String AMT_DUE = "Amount Due:";
	private final static String CANCEL = "Cancel";
	private final static String CATEGORY = "Category:";
	private final static String DEFLT_AMT = "000000.00";
	private final static String DEFLT_CAT = "TITLECOMP";
	private final static String ENTER = "Enter";
	private final static String REMIT = "Remittance:";
	private final static String TITLE_ACC019 = 
		"Enter Detail Remittance   ACC019";
	
	/**
	 * FrmEnterDetailRemittance constructor comment.
	 */
	public FrmEnterDetailRemittanceACC019()
	{
		super();
		initialize();
	}
	
	/**
	 * Creates a ACC019 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmEnterDetailRemittanceACC019(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC019 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmEnterDetailRemittanceACC019(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE	java.awt.event.ActionEvent
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
				RTSException leRTSEx = new RTSException();
				// VALIDATION
				double ldRemit = 0.0;
				try
				{
					ldRemit = Double.parseDouble(getRemittance());
				}
				catch (NumberFormatException aeNFEx)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtRemittance());
				}
				double ldDueAmount =
					Double.parseDouble(
						caFundsDueDataList
							.getSelectedRecord()
							.getDueAmount()
							.getValue());
				if (ldRemit > 0 && ldDueAmount <= 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtRemittance());
				}
				else if (ldRemit < 0 && ldDueAmount > 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtRemittance());
				}
				else if (ldRemit > 0 && ldDueAmount < ldRemit)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtRemittance());
				}
				else if (ldRemit < 0 && ldDueAmount > ldRemit)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtRemittance());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				// END VALIDATION
				// UPDATE REMIT IN RECORD
				Dollar laNewRemit = new Dollar(ldRemit);
				//Vector vector = data.getVector();
				FundsDueData laSelectedRecord =
					caFundsDueDataList.getSelectedRecord();
				for (int i = 0;
					i < caFundsDueDataList.getFundsDue().size();
					i++)
				{
					FundsDueData laTempRecord =
						(FundsDueData) caFundsDueDataList
							.getFundsDue()
							.get(
							i);
					if (laTempRecord
						.getFundsDueDate()
						.equals(laSelectedRecord.getFundsDueDate())
						&& laTempRecord.getFundsReportDate().equals(
							laSelectedRecord.getFundsReportDate())
						&& laTempRecord.getReportingDate().equals(
							laSelectedRecord.getReportingDate())
						&& laTempRecord.getFundsCategory().trim().equals(
							laSelectedRecord
								.getFundsCategory()
								.trim()))
					{
						laTempRecord.setRemitAmount(laNewRemit);
						caFundsDueDataList.getFundsDue().set(
							i,
							laTempRecord);
						break;
					}
				}
				// END UPDATE
				getController().processData(
					AbstractViewController.ENTER,
					caFundsDueDataList);
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caFundsDueDataList);
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
				ivjbtnCancel.setText(CANCEL);
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
				ivjbtnEnter.setText(ENTER);
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
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GADC813ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8BF0D457F5344535267187FBE822BA1EB1F5082BB6EA8737F146634883CDE9C6315D89496018464AD48E34A6ADF6B4315A5204CE2FD63B528A7D113034C61895080F8CA832F1EC31C8209F9801GC5DAA19113D8CF3BCF3B8FBD6DDBF61F50A24B50F36E673DA7692DE4BC8EB3077B6E39671CFB6E39671EF36EDD116ABDABDCF90EEAC272DC047FFBBF0F10CDC789E99E149A852E1DF8EA0865DFBD
			C0A5D9BD6504368F46FA0FFAEAAA087F1A9B7AC168E79F7654A0C39539F2ADFA98BE124CF39646DA0A274FADBC4F63ADE21EC25A3FB5D282ED99C098F077GECA0B87F97C7EAB9FE95744B78B7C21E83E8731066ADF72D85DF1747687C3C6DF5A04D63A3F5C28E60B3813C00FF57E14AAC225A5D65EB617D2B8F3CC8F21F99F71462DC96798216F35EA7F96BA52FD1C3641344E5B4F2EB5FF868249488E81391DD0904FC3EB331095391DD0EF54B3A24287D7204226BD2A4A0836D21FC54314F6D267C046400CC26
			4F77CADB45FC63AF12707A6F64A753C74DE7FAAC4BB7751979E536335D0932FFC86D0BFC6BE05C8DB8266B4A1273C25700B65124A7021A5E244403CDB1BD5E94D095B922B7190BE81AD4122F23232B2B796D63FCADFBA5311625B017530538C0A16A10817D75GDB1CE20DED87459A07BF7EF732F574BEC7F912B51EF53135C5DD669A2BEF5F6D74D09FEA73219F244358BA00CDG6E82CF4DA10033G3B286FEEEDF98B5A0ED13DCB1B1850A29D51E8279A0CDD5002328A5FBE9F0CF0DC3F9C50E2C1C218FF8E3A
			0F5868A3E024E036EF1A7EB4C404BF25773FC4163F7EEC3E4BE6ED99BFFBA6DF67B2D28A4C3531F57D7389DCD79A605782E4816482D497027F960AF5F59EBF36583AFAF053A212BAB0B99A15E2FA672422866598DFDF7648C26B338346776A9D73B13B249D335AA56B6F6EF7945A58A5ABCDF77495B2FB048BCD3B30398FCA4966CEA91C3F9FF42EC2744397289FBE8C5FF95CC7997EA50A4F31605959D1A89E7B446D29893A055F8E1709B574BA1FA795EFAF33DDC36A376EF2E43A19BE596E39FB7BC49B462512
			A270D300F600E1008440F20034A2313F779E7F6527745BBE4B5EB25EB33E57211593FACC6A094469D93DAC4594E9D415630453DC72216E2333787C74BC85E00E8F64089C13F4C50B00F4E9C286C70AB31A5F7AAA11A6A653D13FB2A1438493D1A2644A3EAAE8D5A92E8FC60312AE1B94ECFC4207363CA1DD8609A0G3E3796214DD59A8BB361FB93G33550FCFA22EF53B27463FDD58E533EF8734674131E555676BD6822806943BA9667F71890CA5728D08E35DF2D40E8461A34E47FEF22248344B18E6687CD30A
			47B352850A478D7CFF1E28E4F810E13532987FDFBAAE69FACC991D546581D94718E9723BDAD117782C6397A77C0C1FC29F8B433A16GACB356F7DFEB15E5FD3D1AB6BE9975634EF1BB1D24F1F6A550E7EC973974C70518DB33378BBFFDF5D878E9F7E9B6B9727CFD36FE1A367519FCE7B1734F3A6239E76DE5AA531CC3AB9C36B9DFB8671D440205E3CB0A6D9C791F03FC84E6775E8E34FBB72CF784E0C931307BF7DA3D6C3CF5CAF1D91C30CBB158B1B9E8FA9923DD5D165C473E5DE66FE34F35597B58775B9676
			219F365979909BFB298DF30BB6953178643F356D20E7B892112952868BE1797FD5BF5AC209A87A10928C497A00B28D85491AE2742DBF22E7B0953E530B05AFCD6D933E54B01DC556FC24B86C72D96AD7D39C4E92668B35A526AFB11DDEBE09B617D5F9828C55052585433FC24FB747737872AF276CE97F75947A6338FC13537D64D48141B822CE4A465AD6C945E86318B281DB058716570B031235DEEC817D30361C01F6D909280B2ABFC61AE4258E731D3BBAD137B46049GD8DFA2FCC7EE77D84EACF71AAECDD5
			599698FB535FD101FBA9DF1F14C1A69E6A7346FA2EF46C18B3F6192F0D0DCB9D38B655928F3614605A9EB2EAC84C978C77B8457D948CD61AAF726F058AEB1D9B0C7F016DBEA2B417327D6BAE1D1B3B1E6A22F783406B25E26DD99D56F855910FEB810526863EA7854E7748E275E608E69B15961B7D7E01EEC5D235D0271690F572EA9A73B0C624140A5AF2D79077E28D74B7G64987AFC21F33ECFAD6B5C8E6D18920BC3CD9414934226DF6CC45BE02E62E8D1433DE3355526F27B9A8E41DD4663E759DCDB115328
			C78B60FB4B04EE15DD1596DBC13D893E95D2E543BFD6F765B39F677DFF68F2899FA7A2C62ECDDA83A10CF0BDF45E26C04BEA1D7B293D9EAE1BDB837D994527174D2D016A0322864A00B1EF3948497FC55735947A4B4B453A063BCBAD6B6A531408EE2C49F59A4F2996D3C2CAC46036D39CD45BB4F0313C76A24BEBA0B3830061FE93C0DCB674F38D5CD7A84EDD6129292B903969CF03989B8E997DC7E83F4F689F8C0A3C02B41FC7AE191F3B5A0DB9BE2FFCE585E637DF1C46BDCA987D7F23FDA8FD78D9FBFC8AE3
			449894EC10C28622F20038AB51E6BFB3621F8E6BCF81C8834884588C10CFE5DD8F200C89ADA82968C714AFA5A0E2CBDF2520FB0A52690ACE7DF6E5A55E75F3FB89272BA219BE7413F3626EF1862B47AE8DFAD8C2C510074564F5C1EBCC5EDC496A19129D60EBG6C5C570E3A7939EB81DCF487624E0CD6DB78F04E87002089DAB61E18B50E678D6F666678DF4D9A47737E3443BA6E8BD847DD96FE969B1E5DAF6E2E5B1E5DC854A7BEC97ADE70648E66EBD93CDD337D513CA7DF799FA73353A38174D1BDB6A9B339
			AB262CB127E57B47C963F3F59513F70837C7DA9F4DE3B23E7B21D5C645C332A3AF090C914E2BD64DFD4BF82D917705466BEB528DA951941F220F9967F077D436AFEE594DED3322CA4463AF04B0DEEC00FE8E003BDA1C3D5D231F6D3E787705424EDFFF6AC76449BFEEF394A7D90B371A2D211D3743C50F66E14B6255460B381EA80C5D83308AA093A08BA037DA442F6397D0C72877CE87953D83B2FEA0CC3307A883AFAB721409460345E4977451D83453D35330D348DAF2811F4AAECB57786D8CC5EAB1AA8910FD
			B1ADAA47741B8CCBB3BC57B36F5738EADEC3DE581FCC86E8F38E36655268B5489EFEA9C4C47CA915675A39FB27996B06F74E0FFBAC7E6F8A633E29BBD97C2FB470C7A8BE5502E7F961813A4F58DF8BE3FD2F0B3CD0560F67A68B7ADB816AEABCB54DG5DB542AEC59777B29B5B5877B4942290A370262A8B9BEC39183C46FE6D6262775BED97B1670FEA3A2ECD20FC01F7DCC2FB69DA14A2992E70927A9C2428304EC8B93E74926E2FAA0FD1B4439577216D0CFA1DEDA1275F5107B6405A9AAA98E51A16C183C098
			89315C55C04F4D8858C42F91796CCAC8643D549AF1E66A43DEE8B3203F81A097202296E2A2C0BB40D500E82D20FD075226C0BF8DA093E0B3005B8374GE781429EC17B2550925C47DA668BA89FE5D654AE1E93231C86F5319BC7BD2FFE8AB9E63EFC0A6AD2CA4CB8DE4C4F7EE27C1596B9F66398174ABF059CB32F3E5E1F5C57F677AF6EEB877AE94D89BE623F9965FEF2381F56BDE3E3F1597069667E8A8357936F53628AFA884B87A7E424D7251B5AA45067FA50B77AE511A3D3203F82A093A08BGEB1B56B710
			A69A2ADDE0FDCC76153D789D57833D23EA17244BA18D5FCB5858FF9FAFB746BAA6703E558D8E4D46768CD598E366DD080D5D0E537B08C5A043D75EAAE1F8C398431F2D433D56937A2CF7F0B67674AD345F289E21F7AF06FB68DC9143F5A1A956A499F43D5742567C3C748DE60BB4E859785A285D38F317987F324BEE3C56987FB0E2B7FE44983F5CE0B75EEE0C7FEA46BA6E5F4546D30D7154D97253797896E37C651BF663248E66207137F9CC3C6BD7832E37CE5CDF5F263FF798F93BC91A52FD3E8FE2CA30D38A
			75D22FE239BBB1A6EA2A25F5A2F7BFC6E5E6C0FFBD4016BAF177CF9C90B9B87D3167097E837B5FA2327DB79C4E5D3CA659ED569307EE5F6DE4F22E0D8BB9F5DF7B819DCBD6B385B9FF0AC50EB853D01B0CA529CDFA25D1D11B7CF9D85C95B3F70BF5FCF2467A1BCAC6790D243584F9134F7B26B97F06DBF7BB196CDFC4056C11B7C50D7AFD1740259A3887A382374E405D5BA0F06EBDE24F4A4A5EA7F7F8B76E09C4A775B30A2C86795DD81D91F25B8D395E2AE3F7AA37F39292E5044B7C2E21FFE20F507F4DE901
			DB35C764137746BE5BEF885D1F116F13BB66E3354C9B34E6593C0755B2F986FE9F4567DA706C0C259BE70CF8BDB5C12F28F1BA7D68FF19505F84308D203A5ED373AEC0DF3D28F37E313F19AC72EE6F57223D10ECD44B13B2137F70A04A8F022CA5GAB81B2G5681ECB664178F605EA62BAF7BB50DDE337046A5A93043342C0AC9F8A3E7B6FCACECE743ED754C5FAB763236E12F194BF92DBB04F1298570FEG95E0A9404A3DC22F8F87967DBD6AF4E2015F2348F5BB3D56F2BDB670F60BC5AF4677024B0E0F6CE3
			F4553CBD34EFAE5F17A2F6FC2D1C2E0F3741F9FC8FB65871A5B85DBD3CDDB10F6F7A8C72FDFD1FFD0D144971D97378564F53B387E81C8D7C2D35E13EBC4637EFFAAE5F9120ED67747E24FCDE3CD2706FC64B775F0E593D4DFFDE4066F87677BA877353737E6435DE3F5F7E6DFE58EF7FF67FBB7F42EF77E37E64EF77937E79EF77DF9DCC2EDB7A6062F568930376F71EEF8E4E3F732C99343F73FCFB50FA67E1F71427E9CE8D034FA49A443DE52FA26EADCB8D5CC7B4BF2E07FEEE03088BE5C3495734F3485E5E3B
			066C6DDDBF34706FF10DC349760369D7F3DD2C433DDF681C38AE56D1ED60C6DC56F5B05C2BAE419BB4681EA8943CD183670D4C677D1B0860DDE35015EB0277B983372BE1BE6FDFB6885E3A838237FD3F60BDE460FE7E71FC5E94DA9BA400669E036EFBB302F70501FB176A17837D0A0382779F9457G7D9683F7CC933A749A386546BA068D1C0F568AE9504FB0F0FFB8ADF85799380D5302B76B20F8DFDC9F266FE260946CED714B9892C86FA124BF2EF4AFF03E7367409160C158C9F8EE076FC67E4D663A91455F
			AE42DACC1946FC29F628CAA802AF937C7DF39975E7744565404B7459F84554677E6B0B6963DAC01FB570DD453F196C028FCD594F95AEA63BF081594F417741D9325F3BEA4ABE20ADA45B2A331D6C8D70FDFB166CAB3FB5E5576FDFCC76C2B659845FD3337668119924D7306CA562DD7A9B540731064F81FAB66F66194566355A0BB41A7362D8B5740F65D967BD9FD958B76408708DEF2370DD87BD8B9391998AA0A54068BC1A38EF1DED94BEB473BB53DEDFDBF0AF72799C496C65EF1C6B533F1E1E2FC3F8DA68B0
			E2687048C7268EBF1FDE4CF68B6920B61A5FFB288EF047A166BFB6DF1DC408F85BFD2944FA9F6CFE214D310F585F47920D2CCE49ECB46FE32BD29EACE0EB7D0B2897DA4BD530E9G7F97CF0971267E1E8F4E7A597C8F0F1A3A2C8E185FB475716FBF99A3069D7FE9867579156D1AF2CEB17A3F8E1974B3113AB93A1C73C39023D7B5268723EB564C795605CD7E5B53A67E7DEB784D6A6E7D6338B764AD087FG6DG43GAA40D200D400F400B5G9BGF2G1C07BDB535GC7GDA817AG0207C5BE16689F04A63B
			BF7087834B2DC1645663634973F87B786235C94F38FDEDF2E9FCFEED629F372FCDFEB3BE7FBD7623F1744F9068BDEC50858D5C958327D019089BD202FA5840EBD4AF441F15F1C2011F24FACDD15CB047CDDBF0D7B86E76B83E5B4112A1D4885E256318434694D5E57D94CA23C46232EE5863DE2A239215ACFABFE860D0EF76373787EE0C00A3263FC2DCBA715277B1AF7B63842F79106675FA595FE26A24C8BC4779F4D24C1E603C5D13B276CCFA689579FB7C3D273BAEDD68036FE67A2F2939D9A7AEBA05CE8A58
			33DBBCC64A45F3D2D38FDDC0760DE33E7C0DDF49F790E78FA9E09B5AC31C38ABBD44C5B72D07644306A6227C403F8300975B7A78F5F20B9EA304439CDBE4E07131F1BF47969BD89AE2897B99EE36048FG1AE7C940E7A5A727F59BD864EE65872D5C40621FA59F87B80921064525465B565F4F365EF5B569EFF04B0E32F83472685C5F7D3F11203FD983FE5BD1F1668A92C7131DB938968FCCC82A8A99E63CA392445FF6BAEF6A32D973FEB301726302068F3231EFA50AF84C91BC7F8FD0CB8788285C45E37B90G
			G8CAEGGD0CB818294G94G88G88GADC813AB285C45E37B90GG8CAEGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGB591GGGG
		**end of data**/
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
				java.awt.GridBagConstraints constraintsstcLblCategory =
					new java.awt.GridBagConstraints();
				constraintsstcLblCategory.gridx = 1;
				constraintsstcLblCategory.gridy = 1;
				constraintsstcLblCategory.ipadx = 20;
				constraintsstcLblCategory.insets =
					new java.awt.Insets(33, 50, 9, 24);
				getJInternalFrameContentPane().add(
					getstcLblCategory(),
					constraintsstcLblCategory);
				java.awt.GridBagConstraints constraintsstcLblAmountDue =
					new java.awt.GridBagConstraints();
				constraintsstcLblAmountDue.gridx = 1;
				constraintsstcLblAmountDue.gridy = 2;
				constraintsstcLblAmountDue.ipadx = 9;
				constraintsstcLblAmountDue.insets =
					new java.awt.Insets(9, 43, 8, 24);
				getJInternalFrameContentPane().add(
					getstcLblAmountDue(),
					constraintsstcLblAmountDue);
				java.awt.GridBagConstraints constraintsstcLblRemittance =
					new java.awt.GridBagConstraints();
				constraintsstcLblRemittance.gridx = 1;
				constraintsstcLblRemittance.gridy = 3;
				constraintsstcLblRemittance.ipadx = 19;
				constraintsstcLblRemittance.insets =
					new java.awt.Insets(11, 37, 16, 24);
				getJInternalFrameContentPane().add(
					getstcLblRemittance(),
					constraintsstcLblRemittance);
				java.awt.GridBagConstraints constraintslblCategory =
					new java.awt.GridBagConstraints();
				constraintslblCategory.gridx = 2;
				constraintslblCategory.gridy = 1;
				constraintslblCategory.ipadx = 63;
				constraintslblCategory.insets =
					new java.awt.Insets(33, 32, 9, 41);
				getJInternalFrameContentPane().add(
					getlblCategory(),
					constraintslblCategory);
				java.awt.GridBagConstraints constraintslblAmountDue =
					new java.awt.GridBagConstraints();
				constraintslblAmountDue.gridx = 2;
				constraintslblAmountDue.gridy = 2;
				constraintslblAmountDue.ipadx = 59;
				constraintslblAmountDue.insets =
					new java.awt.Insets(9, 71, 8, 48);
				getJInternalFrameContentPane().add(
					getlblAmountDue(),
					constraintslblAmountDue);
				java.awt.GridBagConstraints constraintstxtRemittance =
					new java.awt.GridBagConstraints();
				constraintstxtRemittance.gridx = 2;
				constraintstxtRemittance.gridy = 3;
				constraintstxtRemittance.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtRemittance.weightx = 1.0;
				constraintstxtRemittance.ipadx = 153;
				constraintstxtRemittance.insets =
					new java.awt.Insets(8, 4, 13, 41);
				getJInternalFrameContentPane().add(
					gettxtRemittance(),
					constraintstxtRemittance);
				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 1;
				constraintsbtnEnter.gridy = 4;
				constraintsbtnEnter.ipadx = 20;
				constraintsbtnEnter.insets =
					new java.awt.Insets(15, 60, 32, 3);
				getJInternalFrameContentPane().add(
					getbtnEnter(),
					constraintsbtnEnter);
				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 2;
				constraintsbtnCancel.gridy = 4;
				constraintsbtnCancel.ipadx = 10;
				constraintsbtnCancel.insets =
					new java.awt.Insets(14, 57, 33, 62);
				getJInternalFrameContentPane().add(
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
		return ivjJInternalFrameContentPane;
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
	 * Return the lblCategory property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCategory()
	{
		if (ivjlblCategory == null)
		{
			try
			{
				ivjlblCategory = new javax.swing.JLabel();
				ivjlblCategory.setName("lblCategory");
				ivjlblCategory.setText(DEFLT_CAT);
				ivjlblCategory.setHorizontalAlignment(
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
		return ivjlblCategory;
	}
	/**
	 * returns the text in the remittance text field
	 * 
	 * @return String
	 */
	private String getRemittance()
	{
		return gettxtRemittance().getText();
	}
	/**
	 * Return the stcLblAmountDue property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAmountDue()
	{
		if (ivjstcLblAmountDue == null)
		{
			try
			{
				ivjstcLblAmountDue = new javax.swing.JLabel();
				ivjstcLblAmountDue.setName("stcLblAmountDue");
				ivjstcLblAmountDue.setText(AMT_DUE);
				ivjstcLblAmountDue.setHorizontalAlignment(
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
		return ivjstcLblAmountDue;
	}
	/**
	 * Return the stcLblCategory property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCategory()
	{
		if (ivjstcLblCategory == null)
		{
			try
			{
				ivjstcLblCategory = new javax.swing.JLabel();
				ivjstcLblCategory.setName("stcLblCategory");
				ivjstcLblCategory.setText(CATEGORY);
				ivjstcLblCategory.setHorizontalAlignment(
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
		return ivjstcLblCategory;
	}
	/**
	 * Return the stcLblRemittance property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblRemittance()
	{
		if (ivjstcLblRemittance == null)
		{
			try
			{
				ivjstcLblRemittance = new javax.swing.JLabel();
				ivjstcLblRemittance.setName("stcLblRemittance");
				ivjstcLblRemittance.setText(REMIT);
				ivjstcLblRemittance.setHorizontalAlignment(
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
		return ivjstcLblRemittance;
	}
	/**
	 * Return the txtRemittance property value.
	 * 
	 * @return util.JInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtRemittance()
	{
		if (ivjtxtRemittance == null)
		{
			try
			{
				ivjtxtRemittance = new RTSInputField();
				ivjtxtRemittance.setName("txtRemittance");
				ivjtxtRemittance.setInput(5);
				ivjtxtRemittance.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
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
		return ivjtxtRemittance;
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
			setName("FrmEnterDetailRemittance");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(350, 200);
			setModal(true);
			setTitle(TITLE_ACC019);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param aaKE	KeyEvent
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
			}
			else if (getbtnCancel().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
		}
	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs	String[] 
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmEnterDetailRemittanceACC019 laFrmACC019 =
				new FrmEnterDetailRemittanceACC019();
			laFrmACC019.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	/**
	 * sets the text for the amount due label
	 * 
	 * @param asAmountDue	String 
	 */
	private void setAmountDue(String asAmountDue)
	{
		getlblAmountDue().setText(asAmountDue);
	}
	/**
	 * sets the text for the category label
	 * 
	 * @param asCategory	String 
	 */
	private void setCategory(String asCategory)
	{
		getlblCategory().setText(asCategory);
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaObject	Object
	 */
	public void setData(Object aaObject)
	{
		caFundsDueDataList = (FundsDueDataList) aaObject;
		setCategory(
			caFundsDueDataList.getSelectedRecord().getFundsCategory());
		setAmountDue(
			caFundsDueDataList
				.getSelectedRecord()
				.getDueAmount()
				.toString());
		double ldAmount =
			Double.parseDouble(
				caFundsDueDataList
					.getSelectedRecord()
					.getRemitAmount()
					.toString());
		if (ldAmount > 0.0)
		{
			gettxtRemittance().setText(
				caFundsDueDataList
					.getSelectedRecord()
					.getRemitAmount()
					.toString());
		}
		gettxtRemittance().requestFocus();
	}
}
