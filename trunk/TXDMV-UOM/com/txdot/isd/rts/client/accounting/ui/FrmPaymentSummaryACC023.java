package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsPaymentData;
import com.txdot.isd.rts.services.data.FundsPaymentDataList;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPaymentSummaryACC023.java
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * S Govindappa 01/27/2003	Fixing defect 5309. Increased the width of 
 * 							the table to display more characters for 
 * 							"Check No" column.
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							modify gettblPaymentSummary()
 * 							defect 7884 Ver 5.2.3
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	Element renaming in FundsPaymentDataList 
 * 							modify setData()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/25/2005	Java 1.4 Work
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
 * ACC023 is the summary for Funds Inquiry
 * 
 * @version	5.2.3			08/17/2005  
 * @author	Michael Abernethy
 * <br>Creation Date:		06/12/2001 11:12:27
 */
public class FrmPaymentSummaryACC023
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblPaymentSummary = null;
	private TMACC023 caTableModel;

	// Object 
	private FundsPaymentDataList caFundsPaymentDataList;
	
	private final static String TITLE_ACC023 = "Payment Summary   ACC023";
	private final static String ERRMSG_DISPLAYED = " are displayed";
	private final static String ERRMSG_SEARCH = 
		"Your search for payments has exceeded the retrieval limit, " +		"thus payments only through ";
	private final static String ERRMSG_TOO_MANY ="Too Many Records";
	
	/**
	 * FrmPaymentSummary constructor comment.
	 */
	public FrmPaymentSummaryACC023()
	{
		super();
		initialize();
	}
	/**
	 * Creates a ACC023 with the parent
	 * 
	 * @param aaParent	Dialog 
	 */
	public FrmPaymentSummaryACC023(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC023 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmPaymentSummaryACC023(JFrame aaParent)
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
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				String lsTraceNo =
					((String) gettblPaymentSummary()
						.getModel()
						.getValueAt(
							gettblPaymentSummary().getSelectedRow(),
							0))
						.trim();
				HashMap lhmMap = new HashMap();
				lhmMap.put(
					AccountingConstant.DATA,
					caFundsPaymentDataList);
				lhmMap.put(
					AccountingConstant.TRACE_NO,
					new Integer(lsTraceNo));
				getController().processData(
					AbstractViewController.ENTER,
					lhmMap);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caFundsPaymentDataList);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC023);
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
			D0CB838494G88G88GA10ABBAEGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BA8DD4D4571924CBDA37F5F374146E521C2C259B52501453352D693AADE7E333769C22C60902BFC1DBC0E20D415F18331EAEE74F0599A22900B38CA0897E1044F40DC102119803A299980C980C88C4C92255474C831EBCE6A6B38F86CC2A7BFD77674DE3181135D14E79F86FFE775E6FFEFF777B3EFB5F1032FDF185F979E5046495907E77DE9EA1DF9DA2A4761D950F8B5C5564ACABB17C3D8AE0A58B
			EB4C7074C21F7905AC2B05CCEBAA04F6B4340FED4D326E02F79BF9E4DF42DB7092E11DA8681B5EDB757737DE2723C12C331D3647EE94413384E08A60928136A5637A53EFD4F2FC8A34CB78BBC2DEBE2C75840EB16F2BB460CFBC4146297CF9E99E0EC93C311B77A783BE83208E700EF9C11AAFD16E6EEDB8465B172627132D73DEB19563DA867A8262795CE47E2CA67F120F0AB0111C332FA19F9D037DA7A52753B36C5694F7FFC343A95FD007B4B6A43B352E6121A149B7C6483179485B41FD05F49E7CC11B7171
			D471893ECE4CE0BA097E494208FCFCB51F2D9FB7BF484731583F1ABA96A06DB42AD71C17817D1500E3BC6E34B9840FCE4FD02DB66A72E8350A5FD56B537C35CED581AEEB034C578EABE178373734543DF3044930672610A18656A8FB92F10DFBF16D85504E8648DDA0E4EB7CDFA1DB4F176B48365EC6534E883275BC49E40AFAB2A81B634C67AF3DCCFDE665F134FB9274E5GEC8528DC88FE8A5083D0C5FD751F9A8E42535E2B35F80606BCEE3B575B2C3AFCE7BDAED905770686686138CE5969713988E17E7876
			3E1AB07C8870AE9C6F0BC17F39C8047FA4F6E6926DF20A29A00CEC6AFBA926E8CEA3F6E1D0B6A657E8A34AB59B7029GB9G66C5D9567DG0EC5C22E4C06A31349556656E41FDBD23B06FB3D12CFEB9ED6D4176C6372255F3815FC61004DFB763667B13DA49CB16AE565AFCE1A3607514B4A0457CDD797B1FD0CAE8A6A052D7D5A4DC8EB47AE1AE88F0424C56807BF23FE18886FF95CC7997E5794CFD202F836E7DAA89E5B966853D2045F761488995A4DE944F238A92C8C310F571B1627B0FEB625045A363B8963
			506E2740CF81BAG86G26G44834CFECA583766785B7FCF3F6DB058164DFD66F885BC65D14DA735397D1A64F64A97A41FA2752A321F70B19B0EA36F3D6366BDCF7713935678D0F64BBEC9D3BCEE20AE8D4960C8FEB6A677B886F327CF263D5D4A108C8B8CF90920EBBEE1C37F167C5AA7DE17244941912C3F68846AF2C43AG9401G3C97AEC61D1F22B1F0B9483F9500696A1E8F90578E7D2E45C2AF5FBAD18A4F0F4031E535214121B8118D9ACBD97FBD0D98CB649108D28E59AB3BDD706267FDD39ADF8A6A25
			4F2373FC9F45639EE981467DBABE26514A70C04328E5517FG6D17B44D27748EEBF2172CE12C8C4E0F273C7847EDBFBF61FB5C0EBEB68AF2CD83085365BBF452E6102F5D63999C76F62365381E5E27F1F6963CAFD8ACF2673F6D45DC3AE93170538DBD42CF9D459924A62DB12C1FFA9BD3CC16A54CBF8FAC895DEB3F21B403EB346D6D394DFD9F89104F703125770E2DE18A03E3FAFF2E99755E81722AGD317883D8F1C2CE67B2DD9724BE2071D730145E4D7504B58583FB4C576B139A93C0F8DB6057731AF1AEE
			6DC3E3CD61FC0875C5B5E1EE71845C861F3C3729146EE137DB26CC6BD3884B7B9B3AD1970ADB51AEAA2EFED96BD22E4BE067A568DB17699E0C077759CB04AF85DE973E347F7ACAB267CAE558FC967B50FBA6CBAA336B7E5420AFB11E3E79816ADCD6E52C9DDA3024E07829F4FFF3BC0FAFB76C6147C65B519F8765B1BEEE0A7DC502F1C49D16F559E6CABBD047BE850B935C303CBE6C38E92C8F9D409F5612D36079C02A2807566FC0BB04A951385F6CF7A0DF33E0FCB2C0C62A709BF3734B067D4A9D2645232ACC
			7D3ADDB6B7DB500E729743B204E35C509F6932ECEBAE8D693B4065E27D248565D28D31E0DBAA4A2569F5A3668A067B8E45ED048D7504D9645E3396E3CD6B72570535A14209B4E63B0E34503C75E9AB527582BEAACD487EE50BB1D6597DFE0FD321E9015B63C573D1B2D90D59CFB30DCA8B4C4E6EAE07A2291E7EE64F28280DFFC5639D460718B4D1D7D664533A925A59G39BABF879D937D69ED471F605927787CD08F3964D12153F79D289B4CD39CAD6A37F70C3A1AFDA1FC7D06D03614474E25213A7A41A96443
			81781E250237198E2BC1D7D0EBC2DA6E87CB8AFE7E52EAE27E4D5B3D2D85423F09080F0FC52CFFE8CD497908624F98F832BA675BD4DF89CBC36B1F980A1F3DB4347E591DAF6A1FE4682BD9A6727177BEC43926C1BBEE19106B3FDA0B8DF2F5F89437264B94FF8A7728472774ABEE017BB145C125CD03964BE9CB290DE7824DE4G067BA48FF1B950A64B85EE864515C17B40F211172665E3DC28575B775376A53D7DDA3E48A9B866CE641609F96B383EC6383C73F501696D7E8F51C623FAFB86EDE3FCE2FB6DE781
			0C91FDD02CC17A68724ACED23682F536DE0FFDD1A0FF8C40AC00D4002DGA6CACB258778A10FCBD2510F683C3F4B933165528AB16ED1BACED3B46A37B3D76039FEAA12A62CDFBB0AFEF2C61CBBCEE1655862019616CFEE1C4362717CFCE3BC5E3A0255B215CF032F8130FDEFA305FC5FB9G775553083313B243BCDC73D578D78BCF566F9F570F7B6DCDE26CFFE8DCBF6E77474C467EF7730C7D850679ACB64CFFC31CDBF3672FA66A23B5914F64F31FE63E3616BFE35D67734DCF941B191E6E4FC39F55FC43B223
			9B97B0461CE3FFBA98B166D427B3FA757CE90EBB1F4FE8AC7A5CC843F2FF19A9AF828D154F9DCD8F3D37F87ECD348B0D57572491E934569F209797E750FAAA330B49D448F51318AE627121E20C9759504EC55CCA3177AA495FF6D67C4DF62167D93FDAC5E6FFB3A7FFC784D9EAD6B2995A7933EE68FCBEBED93C7A520272FC85FD31GC9G29GEB8148AA913F1EAAC19E2156EBF5A91ADD53A467G4D9E22843C204801A09AB79623BD43CAE391502953E9D56E44EB318B52B5FEB2C3129E9F2584488E1F47AB7B
			34B10625991E7379FDCC7FA28F6CFCA3928D60668C1865DC6FB5489E5DD2BF91712750942A670ED541D867DAB5B16E31787FC3EA3751D5AC7E976878C40A1FEA40333CF00462319D87FD665F093C70A39B6E1B81E8C783A4GA481247CCE686507BBDFE3BA8E235FD6A8C2A1C660A9D593BA0836C42E2F3FE3197CEC9BE3411C5F6B51B44F905257E328857525F93C9449F0BF30204FC10A9A5058D00EFF480276D565BE0AE63887299E75DA1D19100FFF440ABA403A9AAA9865BAAD033AE0203B1F652EFDF45F64
			00CE486FC5BE7B6FA21177767FDE6C19070B91F79A5AD7F55C4FA9EE945A8F6838770B3F6ED9896963BA1173DA6757DE034515DB65351F5AA25B7AD15B64367E8F9B2D7940C65DE3DEEE277F34513A232F4FAF6BBE15EC336838B6FF07472F2005D8BCBEEA42712AB466990671E599744C3043182366001E568264F2FDDD22E3CA0BABEFA1BB237DDDFA7FFA264B6903B3814651438C7F470B9853400DB43CE301704AF3E376B81A71308E56B2A9704C8660671900F1CCAE9F73C1E61635BBD31CE78EDF45FBA6BD
			1657CA812D2161C31F62EA167A5B2924AC1E579409BC2BE60AF87ED991729C8F6DA40054CCF1969C7D33084B09BFCAA33BFF9A7ECEBA237B8DD3CEA65313A5AB18E3FA4EFC7E9223F3EDD050597D132534AFD29EED67733D86BAC13F6AEF5DC342662B203609B8C76CAE91670819D9C24EDFDAD14E3550A62B217ED8ADF4F7722DF7496D1CDB1A8DD6946BCD89087A29EE35D86F5BFBF03D81E8C781442E96FB79F0516D66C9962797D2BB4DDC4D62F41E0EDFC671C986BC0B5FD70B443700E4682B4996717B3EF2
			7427E950CE84188B3092A0972072999143AF175511C96EA33BBD5EF658482A612A0C51DFDB0174EB0056E9G95A09AA096E026CE7F61B23C4F0E14BBBBBD9EDAC2E2B5A9A920F91AB2FC921EB6188EDFAD91BA1C8BB417BFB3BE2E65BC93A9879EAC25F740EBC24FC67594EFD993FAB6CA320A33D1B574C52F91B67C07402DEC38F6CD24757F077AC46E1AD09BEE27F75005FF88356134BD4206E550B77A8731FEAA95033D2FB23C77955D29BB3548F96266DDDD03FF47A90F1CA7E614073FF30BAF8FFF6776C879
			2D6F5CFEDE9E794E6D5F4BA75E39ED2E084C5B360A49F3D85E2E70754A76DD936B1522DD616B1552DD46FA05559747E84CDB89BE82C10E579C53A92E8C5AA7F4DCCE0928C3BA568AFF2AAB45B3CA1C5EEE206DB9FA7BF1EB284DF16C1D752B7489EB5C4973B513BDBEA0E44F56E5B36FA5BA8F450677FF5CF37777523F5E55B5D86DF12E843F630F40E1C4F1CAAACB164D122F4323AA4EB1FE7E3FC6FD824F3F754F0A73FFC2894673810FCF390EF1F842CCF1F7B0174EEDC45F0539B94F52F363CE47A4FC99E1B6
			4CD9GD04E5B99702E00839E4E97B2EC2D40BD2C752A936EEBD90DB4D8EA2C116A57C12C045ACD036714F50227DF87A663294040D39C4C1B35AE48D3B23C97C3A05E236B751E12FAF89ACA11DAC3A5D2FB2135333B2D455EFE252B2573DCFBFB333D730A3D6B4A472D2D0ED6871B1FDEF2749673CF1F6BEC7B6C5C475D61A990320F52A0745E047DEDEA8A07E377B6A9289EFE5E4EACB256E30EE5B9792F137075D86ABAFE477FDC38FA6C6161F748ED5E77326BECDEB3BDD80AFCE2ADF869B9D1B37D7388A633B9
			50DE8E3089607664ECF885DB2C8EB9D00576AE4C01739640E9002BG9A40D400B800D9G49G99G5BG0A56E7D92B816A819CGBDG6AFAD133143F0C7BA3D24D420BEDC325A276F8C2A96E71A8209333BEFC4CCCGFCD204BE04945E179DE68C2B9F2C81545B367521F545AB94EF5E90DAD7BCB8A26A8A8B74E99B040CA72AA26736162A49F3DBFBD5785C56D9B5B137DD2C8A1F5BAED7CDBC0B77D6214F75030EFB74F17DBA6ED39D27D21A083B283834819D6F23FCA17E340C8B8A7CA865ABC0F1BD9C77D783
			6ED30E7BC6951E99C1E48807E26E372AB0C776A92A4A6FAA6898458DC7CEDD9F53A80F0AD7B2707DBD9D07FC333CB40F6E0565E0072D9BC41E5EC74F3A059B332C3BB78A1C3982F19FC07B2A0EBBCFF19A34E3F45C8C0ACB0076DC9D3778A3117B16EF9439EFD70068FEDAEDF87F71485D4FFD0F5F55B5D80C4DAC45F7E9D873F44AFE653A0CA79811377ED8AA4E08399BC54EBBEA63F76374EC476E864E5AC4EE295F14E5ED871886908F0075911BD7C57313FF4093384073ECBB1CDB3046E4EB3DDBA14E5DC91B
			441C018A9B1FF3DA4F31EC961B738DFDCE0ABE67518A8B75B1CD76B9E5D56D72CACEC89CEC7CC2BABE9B46EE82E05F836E99963CD7EE06B3A0C0BB008AB0F53320E93561EF264665A19A27EB6D1FF41F3B52595A5576D96B15F6FBD7771516F36D1F1C7DD8441074B2F1B6CA84DA591B45F97A5E13131F27C3FEEB56FD167D380C085C70FA3910FD7796119B4E1763FA5D50D6812237887E5BABF05DBEC9754B3A5D3FBF8E47F24397183AF9FF72C7F8DF62168355623A245621C0E101BFD2B9D0D7D3DD77A66363
			61E1414783DB44F7093461506F9253730C5F95360F04FE174079417E77C7C23FCB0C1FBFB8927ADDC24CE77BA633EC327B0354AD6373841B37F152F9FB436E538365FF5BFD45EF293DB6ED894DC98BA9BEF7CBE8CE3AD8AEF2D26173E06367C50CCA9C3E5D75D90DB647D6CC770A2A60CD827D1513AE53FCAC6B78FB2A617DF2795D0FFBA3F7F58D761B5D4BEFBD864F447524C0A3153CE62E66C5F3F5F5B55BEDFE9F59293BFF9B4DDB99A3C79ACC2B9FB46593F39BF9116571B6E246E45CC68AE82EEEA3A64863
			23789BB04CFF18D771EE53667F025C2056C30F0F62D0GEB14CE5CE2767BD6ACCB3C62A84BA779304C004C689684B0E7D34A6C0F552BEF5EB4FE6B5AF66F0E085F4BCE3F406FEBDF887DC67F5BC3380FE381BF6B853D1EBBF408C47E7652B5A429AAC464C13B5B055FE11A47B4B9F8E7B06F9052770BB13C13FFE3BA04FCDFB34C79BFD0CB87881C1FCD944190GG24ADGGD0CB818294G94G88G88GA10ABBAE1C1FCD944190GG24ADGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2
			A0E4E1F4E1D0CB8586GGGG81G81GBAGGG7B90GGGG
		**end of data**/
	}
	/**
	 * Return the ButtonPanel1 property value.
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
				ivjbuttonPanel.setAsDefault(this);
				ivjbuttonPanel.addActionListener(this);
				// defect 8240
				//ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				//ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				//ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				// end defect 8240 
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
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
				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 564;
				constraintsJScrollPane1.ipady = 280;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(19, 10, 4, 11);
				getJInternalFrameContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 134;
				constraintsbuttonPanel.ipady = 14;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(5, 128, 17, 128);
				getJInternalFrameContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJInternalFrameContentPane;
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
				getJScrollPane1().setViewportView(
					gettblPaymentSummary());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the tblPaymentSummary property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblPaymentSummary()
	{
		if (ivjtblPaymentSummary == null)
		{
			try
			{
				ivjtblPaymentSummary = new RTSTable();
				ivjtblPaymentSummary.setName("tblPaymentSummary");
				getJScrollPane1().setColumnHeaderView(
					ivjtblPaymentSummary.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblPaymentSummary.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblPaymentSummary.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC023());
				ivjtblPaymentSummary.setShowVerticalLines(false);
				ivjtblPaymentSummary.setShowHorizontalLines(false);
				ivjtblPaymentSummary.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblPaymentSummary.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel =
					(TMACC023) ivjtblPaymentSummary.getModel();
				TableColumn laTableColumnA =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(0));
				laTableColumnA.setPreferredWidth(89);
				TableColumn laTableColumnB =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(1));
				laTableColumnB.setPreferredWidth(100);
				TableColumn laTableColumnC =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(2));
				laTableColumnC.setPreferredWidth(90);
				TableColumn laTableColumnD =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(3));
				laTableColumnD.setPreferredWidth(110);
				TableColumn laTableColumnE =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(4));
				laTableColumnE.setPreferredWidth(100);
				TableColumn laTableColumnF =
					ivjtblPaymentSummary.getColumn(
						ivjtblPaymentSummary.getColumnName(5));
				laTableColumnF.setPreferredWidth(60);
				ivjtblPaymentSummary.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblPaymentSummary.init();
				laTableColumnA.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.RIGHT));
				laTableColumnB.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.CENTER));
				laTableColumnC.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.RIGHT));
				laTableColumnD.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnE.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.CENTER));
				laTableColumnF.setCellRenderer(
					ivjtblPaymentSummary.setColumnAlignment(
						RTSTable.RIGHT));
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblPaymentSummary;
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
			setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
					- getSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2
					- getSize().height / 2);
			// user code end
			setName("FrmPaymentSummary");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(607, 400);
			setModal(true);
			setTitle(TITLE_ACC023);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
	}
	
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
			FrmPaymentSummaryACC023 laFrameACC023 =
				new FrmPaymentSummaryACC023();
			laFrameACC023.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject	Object 
	 */
	public void setData(Object aaDataObject)
	{
		// All information will come in a map
		HashMap lhmMap = (HashMap) aaDataObject;
		caFundsPaymentDataList =
			(FundsPaymentDataList) UtilityMethods.copy(
				lhmMap.get(AccountingConstant.DATA));
		caTableModel.add(caFundsPaymentDataList.getFundsPymnt());
		// If more than 200 records were detected, display to the user 
		// that this has occurred
		if (caFundsPaymentDataList.isTooManyRecords())
		{
			RTSException leRTSEx =
				new RTSException(RTSException.WARNING_MESSAGE,
					ERRMSG_SEARCH
						+ ((FundsPaymentData) caFundsPaymentDataList
							.getFundsPymnt().get(0))
							.getFundsReportDate()
						+ ERRMSG_DISPLAYED,
					ERRMSG_TOO_MANY);
			leRTSEx.displayError(this);
		}
		if (caFundsPaymentDataList.getFundsPymnt().size() > 0)
		{
			gettblPaymentSummary().setRowSelectionInterval(0, 0);
		}
	}
}
