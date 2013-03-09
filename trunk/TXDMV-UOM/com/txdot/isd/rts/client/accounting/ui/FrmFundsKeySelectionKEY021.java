package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmFundsKeySelectionKEY021.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * M Abernethy 	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * MAbs			06/25/2002	Making sure selected radio button has 
 *							textfield enabled CQU100004346
 * RHicks   	07/29/2002	Add help desk support
 * S Govindappa 02/27/2003 	Fix defect# 5561. Made changes in visual 
 *							composition to Check No text field to accept
 *							both alpha and numericals as input.
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * J Zwiener	11/30/2004	Red fields not clearing when changing radio
 *							buttons
 *							remove all focusListeners from getradio...()
 *							modify declaration
 *							modify actionPerformed(),setRadioButtons(),
 *							  keyPressed()
 *							deprecated focusGained(), focusLost()
 *							reformat all methods
 *							defect 6887 Ver 5.2.2
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/22/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * Min Wang		11/14/2005	date field data disappears when press 
 * 							shift key.
 * 							modify keyPressed() 	
 * 							defect 6522 Ver 5.2.3
 * K Harrell	12/09/2005	Arrow Keys should not Select.  Also, set
 * 							max length for trace no to 9 vs. infinity.
 * 							Restored clearAllColor to actionPerformed()
 * 							modify gettxtTrace(),keyPressed()
 * 							defect 8467  (traceNo)
 * 							defect 7884 Ver 5.2.3 
 * Jeff S.		12/20/2005	Removed Enter Cancel and added ButtonPanel.
 * 							Changed ButtonGroup to RTSButtonGroup which
 * 							handles all of the arrowing.
 * 							removed CANCEL, ENTER, cbKeyPressed, 
 * 								keyPressed(), keyReleased(), 
 * 								getbtnCancel(), getbtnEnter()
 * 							add getButtonPanel()
 * 							modify getradioCheck(), getradioFunds(),
 * 								getradioPayment(), getradioTrace(),
 * 								getJDialogBoxContentPane()
 * 							defect 7884 Ver 5.2.3
 * T. Pederson	12/21/2005	Removed setting default focus. Focus will
 * 							automatically go to first radio button.
 * 							delete windowActivated()
 * 							defect 8494 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * KEY021 is the initial screen in Funds inquiry
 * 
 * @version 5.2.3			12/21/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		07/15/2001 12:38:40 
 */

public class FrmFundsKeySelectionKEY021
	extends RTSDialogBox
	implements ActionListener//, KeyListener defect 7884
{
	private JPanel ivjJDialogBoxContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioCheck = null;
	private JRadioButton ivjradioFunds = null;
	private JRadioButton ivjradioPayment = null;
	private JRadioButton ivjradioTrace = null;
	private RTSDateField ivjtxtFunds = null;
	private RTSDateField ivjtxtPayment = null;
	private RTSInputField ivjtxtCheck = null;
	private RTSInputField ivjtxtTrace = null;
	private ButtonPanel caButtonPanel = null;

	// boolean 
	// defect 8494
	//private boolean cbAlreadyShown;
	// end defect 8494
	// defect 7884
	//private boolean cbKeyPressed;
	// end defect 7884

	// Object 
	private GeneralSearchData caGSData;

	// Constants 
	// defect 7884
	//private final static String CANCEL = "Cancel";
	//private final static String ENTER = "Enter";
	// end defect 7884
	private final static String CHECK_NO = "Check No:";
	private final static String FUNDS_RPT_DT = "Funds Report Date:";
	private final static String ON = "ON";
	private final static String PAYMENT_DT = "Payment Date:";
	private final static String SELECT_KEY = "Select key:";
	private final static String TITLE_KEY021 =
		"Funds Key Selection   KEY021";
	private final static String TRACE_NO = "Trace No:";
	private final static String HELP_DSK_OFID = "HelpDeskOfcId";

	/**
	 * FrmFundsKeySelectionKEY021 constructor comment.
	 */

	public FrmFundsKeySelectionKEY021()
	{
		super();
		initialize();
	}
	/**
	 * Creates a KEY021 with the parent
	 * 
	 * @param aaParent	Dialog
	 */

	public FrmFundsKeySelectionKEY021(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a KEY021 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */

	public FrmFundsKeySelectionKEY021(JFrame aaParent)
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
		if (aaAE.getSource() instanceof JRadioButton)
		{
			setRadioButtons();
			return;
		}
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);
			// defect 7884
			// Use ButtonPanel
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			// end defect 7884
			{
				if (getradioTrace().isSelected())
				{
					RTSException leRTSEx = new RTSException();
					int liTraceNo = 0;
					try
					{
						liTraceNo =
							Integer.parseInt(gettxtTrace().getText());
					}
					catch (NumberFormatException aeNFEx)
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtTrace());
					}
					if (gettxtTrace().getText().equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtTrace());
					}
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}
					if (SystemProperty.getHelpDeskFlag().equals(ON))
					{
						caGSData.setIntKey1(
							Integer.parseInt(
								System.getProperty(HELP_DSK_OFID)));
					}
					else
					{
						caGSData.setIntKey1(
							SystemProperty.getOfficeIssuanceNo());
					}
					caGSData.setKey2(gettxtTrace().getText());
					caGSData.setKey1(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.CommonConstant
							.TRACE_NO);
					caGSData.setKey3(MFLogError.getErrorString());
					getButtonPanel().getBtnEnter().requestFocus();
					getController().processData(
						VCFundsKeySelectionKEY021.TRACE,
						caGSData);
				}
				else if (getradioPayment().isSelected())
				{
					RTSException leRTSEx = new RTSException();
					if (!gettxtPayment().isValidDate())
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtPayment());
					}
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}
					if (SystemProperty.getHelpDeskFlag().equals(ON))
					{
						caGSData.setIntKey1(
							Integer.parseInt(
								System.getProperty(HELP_DSK_OFID)));
					}
					else
					{
						caGSData.setIntKey1(
							SystemProperty.getOfficeIssuanceNo());
					}
					caGSData.setDate1(gettxtPayment().getDate());
					caGSData.setKey1(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.CommonConstant
							.FUNDS_PAYMENT_DATE);
					getButtonPanel().getBtnEnter().requestFocus();
					getController().processData(
						VCFundsKeySelectionKEY021.PAYMENT,
						caGSData);
				}
				else if (getradioFunds().isSelected())
				{
					RTSException leRTSEx = new RTSException();
					if (!gettxtFunds().isValidDate())
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtFunds());
					}
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}
					if (SystemProperty.getHelpDeskFlag().equals(ON))
					{
						caGSData.setIntKey1(
							Integer.parseInt(
								System.getProperty(HELP_DSK_OFID)));
					}
					else
					{
						caGSData.setIntKey1(
							SystemProperty.getOfficeIssuanceNo());
					}
					caGSData.setDate1(gettxtFunds().getDate());
					caGSData.setKey1(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.CommonConstant
							.FUNDS_REPORT_DATE);
					getButtonPanel().getBtnEnter().requestFocus();
					getController().processData(
						VCFundsKeySelectionKEY021.FUNDS,
						caGSData);
				}
				else if (getradioCheck().isSelected())
				{
					RTSException leRTSEx = new RTSException();
					if (gettxtCheck().getText().equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtCheck());
					}
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}
					if (SystemProperty.getHelpDeskFlag().equals(ON))
					{
						caGSData.setIntKey1(
							Integer.parseInt(
								System.getProperty(HELP_DSK_OFID)));
					}
					else
					{
						caGSData.setIntKey1(
							SystemProperty.getOfficeIssuanceNo());
					}
					caGSData.setKey2(gettxtCheck().getText());
					caGSData.setKey1(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.CommonConstant
							.CHECK_NO);
					// defect 7884
					// Use ButtonPanel
					getButtonPanel().getBtnEnter().requestFocus();
					// end defect 7884
					getController().processData(
						VCFundsKeySelectionKEY021.CHECK,
						caGSData);
				}
			}
			// defect 7884
			// Use ButtonPanel
			if (aaAE.getSource() == getButtonPanel().getBtnCancel())
			// end defect 7884
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caGSData);
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
			D0CB838494G88G88GEED9D4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DB8DD0DC57F556B4F20DEBB71226A4D1EC55218EF654B1534836B2D5BC0A2BA60CC3ED25A6D3E52246EA0AD3B5C3D345F5E2E50AE77078717F23BFE4E11BD82A05E4ACCBB6A61205E50C25849690A0AD30400A9F8BDBFA6CBE1637FA3BEF357B00C5C238675C7B4E5D47323BC81E0119B36F5D6F5EFB4EBD671EFB4F39F79FD2654114223C7CCAC94AAB124C3F8F73A42966E3C932BDB469A62C34A9D4
			A0D97E8E816D157E7AC6A1BC2B204EF9ADD4D0A195DFAD05F2AD14534221025760FD1F34632A51GAFF16458216ED79BDF7A5744F2FE55C5F2CAD8796170CEF86E86BA0E3201D21AD07E9361AA938FC3F937790E1417AFC96EE6ECF3EA364A02EFB55BB5C01D9DE84406ED7EB3FC402CCF83AC9D288A70DA9BEF5B8A4FB2B63A5B9A9B4CF6A3ABD2246DCF3C133F8BE5D97893251AFDB34CE73574B0B3C40154323B831E279DD7BCFFF4693EBAA36C560DBAB5642E8B9A21BA1726AAFE23CEF63974893F217ABDF5
			1E89353171EC50F7F6426F8E1DD7267B95CDF1992A6EBF5FB6FC7C7D938DADB87E1159D2469B7E201C4E30CD33E83399F8CF86CAEBA19DFE3C6B34B94664ABA91256EBAF0E27C3E9AB9FFBEDEBC4076DBBEEAB5E520A7CF7CF6FB47BE9D0CE85627AB564355F0AFE09551338FE835324DFF59B6937FE86658FC1B9G14A470AF5DC0BC854A6B00B25BC86FB3EF135EC3573FAD650CF6656F0923F7F69B573762ECC46F35ABB6977E1E7962E48F7A139D6A82C0CB01D683E5GE5836DE7EB60C477BB70EC9EB5DAF4
			1FCF77B78782B64D9D6C525D0A866F0D0DD0E3E2FD0ACB8F422A63FEAE4F5646988F1154BE57471138EE039279E5DA5F83D24D6F3A0A0BE26836645F3A0AABCD9EC75BA33AB95B51EE9D1F235DBA60BD4F6C4B71DE062F3460FC7EFFB6CB731F8AF5259DE467215DB496FB615DD245091E7C1298E3D9797D56FC2D030FE1E9C7E4AC5C460EDE34719A40B782ED854A85BA5099AAB85DC9B63E5EFDF2A19BF77A8DA56817357E0951009CB4EC932A66D602262DAB279359BA9671FE07EF399F374B6A1356B95A72C8
			F7CC3BECD9FDB2FF28135BA35099318B17FDB12E6C651D73FDE337184363ECCD24F672B9AC90F8A34357DAF0BE375FE7EB8B4B69D0F7749C6C392C7D7F3276F71D0B76910979424AF351BEF2478D61A3D0D7FABED440677759BE5CEFB5A8A78125G2D835A8C349D2854CEF37CF8779FEEF29DB9AC734B7BFE5E73AABC15309114BB7DA1C3763B1481B9284A231A92124CB6C9BDE85751B97D7E122DEF9748E8D77CCAD0464D88384BBE851CA9445BFC35E7AF3689AA2C56297A9490608BC844775E1EFD281F9CB2
			AE845C3221C4DA707A87FA50A7A6658160888CF0555851DEE1360F9F0577D6BB59EA3F9331D5D0DEAB6CF24871B2BC4F03F3ABDAE3E32B4AF6CCB9B8CDBE5A7D39A47C6E59A1723B56DD77CB35BF690A69F72BE0EF4830F39F4A32C77B5DA063475F33E607EE517FEFFD0D3D3028B238DD6F38003ED95A95AA2881EA6DA25B0EB82A394F596410C2DE56938487D15C91CB7336433D98C314C908E92DCAC0713B61A5E44E71655E32087F0D6942B746980E7BD18BB8C8C860DA6FDE0E838F2BB7D33D416AE543882A
			239306522F9898BFA37DE758D8C2F3363A10296B1D83286B8868E8GAD953A96756D337811DD572FCC841C38C2CCFF8C32383B925A2F6922DC6C944B81B709F293ABC76AB3E6E86E304D627847B3C2C6412279600BB7A2BEDFBC9BF9DFF6E3717DFF75224A60F31F53975FF7DF6A0B6D3BC57D31FDF7C7FFE25F5C579F4BB7F95D6B7D182B68D3FE0B2F9F6C477D4D257B7DBC9994DDA41EE3BE6BC4BDD43FEA8C2AEE0FE2742B5794B843F4234FAEE3FBE8AE78DEC5B765A4D3EF529ED8F36DEF248D170E66474A
			0FD65E5F1EBF5E4DFDF626BB328772B1D5BBF18DC1FA6A83C335E07A4A71A3ECFFB67190DFEF0F8DE25BD540E38DD0C6B72D377B1CD8CF6B4D5CD4DAF44D4CF9057E4BAED4203D142B938A04ADDC10109AD36C3970F2D45D002CCDC4E25A3D97F640D3332CE50E2F9808BD7E2F8D607A3DA2785FB3D0ACE1EC62BC393D574848B390D4FD30756166E41E2D6E18361E2D32C0CFBC075574048A9AFA68BCD0F270F5F8A6C871A5C78F72FAC01CE1B066F36CFB8C7B91B0AAA920F856D5E1BD0339C3CDB16792A93907
			4F616A1E687C7279A1641B8178669E1A1BD7862DFBE1F3A8243BD4965ECD7B258FDA6719E37FB4D88E4FB1B59802DC532D04E98E1E9A4431E27CB7E1530E45052724054EC69E16F5E86CE054676CEFD5E5CD7758F4E0424FC24F33BD9979EF6B21B34F43ACDF2BF1C0FE8C64F4105F8F3EEF355526173BE31E8B10B487375192C7342DBE7A04659F002FF1102D4E8D6D35588A4EB010DEF94053445EB2D440FD4AAC678E9511CFC934476530DCB65D91BBC6ECB671ED66D36A259C74884BA9ABFB23F3D91E635674
			C667326BC4AEDB8FF5ABFBA92F7A43C536CFC0F9E79F6935E2F817C5AF072E7A8D21539FBF41F52187D50F6AA72C05E1F002E31B970F19FB2EA15F8340338908E39E06B9212C896CB75791DB8E65D401B54EA0368E4A9B7BA81E5632783A39EFEE7C4D956547D8AC447A450BC7BFE5B296A356C662D17DC51C3730A8B7307234A9977659EB2F40532D0C49939A049FBD2474844C749C7D3B9F6D574C6C0C7D2A7BE97FF9E88A772EB1C860A1A475879417D46F44363357E86FCA0136EB01B681ED872A04FAAE33F4
			86F978F4372C899987E7E81F1A916DFAD9BBC3B5589A384B09F7DAEF0FCAE26CAF3E03E3BFA756F20B8EA7D3CC737C581E6F251E9B563DB45349F32E6D70AC3DC0F2062E615E6815A765F0DDE88A2FCF86D53FDB1F82FEF84201CD243567D2F70F3323333B7DD2CF772596FBCFFF1B39EF26CCD97702067246387BE675856ECB7566F3EB3F1C47771ED4E1DF8D6AD2AFA0767175CACBDF9C67F03EA4554113577B6654633EF1CC3256FFE3CEBD0E75D001357E47F36A0BAC7D39DECF3EC577BA39CF2E16346F1C0E
			FBA7374D5427E200BF4307653C34CA96DA403627E6F0DD9941890573FD6A2255DE95F757646745615BE172F38EC45F75BDFF8C67146D7716C9EBECBC07B32FF11BDEE0B18E5BA78D50FE7ED6A96ED99B203C996839815A333E654236250390E387E94F52C65F0567006CB5CF42EDEE5550036C608220A32843AAB126B94AB285F3CD17DC7FE277987FD8C276DE7B68FD523ABF4F4D5B9147AE2D035C9EEE73D9F1C84E43A757E52F0236C9063AB5C01B003681C9C321022AA1526D11D19CA31C3FF0784D06A13B46
			D97425E359002ACCC5E0DC5C1CF77D980E1473F8E1742DF876818E67E0DAFAC63D90411C32C7227D6E13F977F035C3117D35E9E87EDE4BE35057593CB907F88CAA91787D8C57ACB80FCD6FB29C4BCB21AEDB681D66C21EA21F6653E66A73088B5783663E301B28575886588F8D7D9E2EFB33876B6D432102F1203B00D200568E936F8F5D0739CDE358338D12E95ACBC85E6BCA7CF342A1E561BB16F7944C11C6F54350FD481F70638A6AED688186F26C833676A0440E9B3C2909FF2C205FEB4A9803F92C3C4A56CD
			A66835F518626773A594E7CBBF2178788DE663DAA8B781B9857EA01F93A8AF87DAA3709325094EDE48A7519AA1990963693904B2960A33C5A61F4471345393FF5E9C1E0567ED584372DD3037F3BA20F06CB38F4B014646C20A708F1727C2E01DA1079ED251A3791E1AB40B6DB5F9DA1F007627AF629C5DD7CCF1AE936C148DD481F88D9066B4CBD91B72522A847AF15E5B4E228D78CEFA42442AEDB866D1435F06B73F9CFB70FC9947DAD0CD0C63BC5E7E3CB4BA5E227C0DA0BF8B48BE42F234F6EB6C49B14758B0
			C275AFDE0CD51F8CF5FCFDBFD1CC771F2B814B9A211C6444E53C7F94F121CE1EB29A9B5B032A5BA6FB6C4CDABCB63CDCC271366AD30A8DCDA548538165F120A51FD22E9FBEC27BF25A83F7CB593F0E7D3BCE1673037C151FF27FDC7FE9A4DEE52C58DC4879F82F901F838F5E436A6245539C337F014FA2FC7049477EEA994535114FE86C7BFCB876E4A827A176995923FA5FC969964FC5368958D07C262C239D25661A587299596346D96B6FDC6B77FC109FAFCED5DEB2731DCB91BD42E08F4E3BFE04F89B17E80F
			48AC7EE23F9D7C7C8B76FB2D64D67B7138331679CA5A25687B7A6FB1FC5D25687B7A33A5642F69D0F774B2659D93AA2E153B203C9AE88350E6209C20CA1962CA0E67383440DD3DD38F58E1C352AC571B1C7FD3DE64FF94F835820D83AD81CA86CA937CED6368A371721CBEDDE769B4E656328A1E4242D8D046939C37617E32E89BEE10A37B74A6B9DECC7FA67359EC195BEAEF44368C2F9C0D605C063FE7B8161B20EE63A8794CFB9D517207DCF09EF41967EE57FC796679DB43BDA6996A57GE5GE581C9EE324B
			1747977C9D2CAD1C60F7300FC62247D58D3C6B5DFCBC9D6E7963627D9E4507797E834B3B3DE4716FC715C515416DBA2B460F1F3729316FC76FD4E35F0F2ED0935F0FFEDD0DFFBF1A224E3F9F5D6C0DBF36FF77AE9C5B333D31F332E73D73F332673D31F332973C73F332774BA8A79BF1D3CE16D6CEB95992B72D053F75D5E3BBA8E7836582D5AA2102DA20D620A12000C2ED9FE5ED17C2B995A89DE8ABD06198648CC02DC06EB1EAFB50378457243270DD481025CDED0C7659963F4F343C37A6600DFAC446EDE3E3
			A93B89FF5BE3F95F91233E4872EE4D8DF7AD38CE68CE2A15454725E02715E3B4AF37B3AC8D4A6B85165DC1F315B1C67BCB33965F57BA34586B20DB0B3D8E1CDA625F38C634F86B040F4FB1C2FAD4F9E84CFFB5C2FA548AEC7B886951G984FBBAF16D13E176461F9E78ABC57826199C09A47B6BFA8EFCDE05382130A4C67C6681B85740AD95E6E0944D31EFBB6D662BA8B4AEED5F786E517C278D89D0E4F889B8C65587F746D24368EF99A0FE59C9FA9A967EDCD14F87C29CD70E6B76B0437B7183C994A313F53C5
			5B16F145F5055ABA1A4C368C65B96DEFFD561C36E31C5B4798271C777F3C567AA433FE2328DF22466A1FA56AB78ED86B371B758DAA55FF5B9E2B3FDD543FDB9F2B7FE5D13F4C96F37C22BE69F42C71A729F44736A300B671799588262A0B5F7979BAA88FDFA55AB9836C3E881C1F5F777D33CF42BF66C7AFF5AC64C764C3C82B01A752AB260C7570FE4CFC677227C7C8FE8654F1F93F71C7646D78B4113C228564EDD2F91E9749833FD4C6F9E09654D5F9E1FED8AE7CE4990D4361251C7E8556B6894AA9C06B008E
			B27BC8D229C4FF4F7DA99646753893BF1BB06F161E59E9BDFB347EB4B76FCDA9F64E1E69B5F375AB111C3D7A17BFAB64FA3EEAFBC30AF96F5607CB40BCBA18E707E3FE7ABDE5688A1DC7F66F7ED03A4573C827BFB0E11CD5954DED72F5FA695C307C8A596F6FD542B2042C470F5C7AEFC23221F0D1262C02C162ABE9A46B17F6429A84B6DACFD8C0E00F5B88DBAD30FFB80D984622AC0D7CFDA9FE36A84D1FC73A6F7E5DD5C922FDE579C8842F426D5B0CC7F74C4B41EFAE5F7B5A976C17F94B7DF8EE6FABC71F2E
			72C51F1BA699DE630BBEB73DA4564BF128CB765139295840F5198365CABF5896488164861A81DA8E148A348EA893A89BA89728C2075C86289E2883E8C427D81C9940FDA15E99483CC0301CFC488EF5FE34C3C00F64AECB61FD25D9CE5363657843DE6C37A1C6BD52A64033236AF83F75EA345DF321DDC500372D894413D7BA18E83E9A201F9D683225BF6F775F7668FE8634C9B25B2D0AAB4FD31FC85E9A68178E34F91E3CA7ED517D36C19B69AAEFD7F9B51E3C7453586FE80CFA245300BB005CC0E1338D6F9742
			70ED3E4FD85E0B4A977F2C7546224AB06F180D84F74C46CD5CB19BF16E180D98F74CC61CBBE643FA1E61D81301FB3487565510E857A630E1011DE3BC919BD45D4638409DECDC08F7A8A8107081B62EA9068D1958C58BB6CC5F1099F8FF8AAA0F6BC16A2B9818F70C291A464BAAEB237AC30AA16C61E7E3D48332E55CA1016138F91E5C46E25FDD60EBA9D7C56E2CD30E3DD6E0E78214E3E7886C3EG65581B8496E0B12FA688FBDD10306FFA291DDDE087D99CDC85658D827B971564E68A6CB5AF495DAA30306077
			DC045FA0E1CDA142F63178379C4A29829B34135CF5824B6FA2399982BB5BC5F237886CDBDDA4A3DBE03375621CE190765BE6121BA4308FEDA4F71540FEE1A339E982BB52C2F2B7886C76961231D1E0CF1FA6BB9F1890B6B8C36D0E8B2C3C1D4632C4E0BD6DB416E401FD05E129D0DEBFC171FE0479C608FD2C2338F99E9A7253EF5F19500EE307199F7C97D0568465263F1840BD0A67265F8D5C4C19AADEEE7A9C707CD8321E29EE77B2BF46F4CB227C78DEAF0DABF7024EF2195E05F2E22B5C1D139139D8D78D65
			276748BD2562BE64070D682C6E1A88617E53227B823A1F1D49BAA6D1E62F0AEB33F612EC134BFC7A99DCE313E41B5727A236D916F00C8B1D93D6CD72394A937CCE8D5E52B9A0CA677573F42E31631E2478D1D937E96B0F6D62AC826D395ECB6C28779621773BEC2D7C88A8C768FDA69C517B312E0574AEC920F769943FE309687D157A057436722B1C1A2B775129085EFC7C7BED24E37D9469F85906BAF6CC110EBB583A7CA110FB0AF41C1C0E68F8EF4BC2BA16A55051G1E9F4C99D3A1DB5FF86733E40AFC7C54
			190574364AD89E25F76ABC3DCF36135EEB045E61F654BBFD0A760534F664691343F6456FE1310E77CDE7FDB10EE70936FF6127EFAAF87923GBE5FB65EB64F86EFDC17042FFD470BF2CFC8314E98F4D6344FC45AFBD43405C7B168673E2347A4BAC71ED12C5FEE9C3FDE18CF6BA45AC64FCD195F1705A35F3AF07E7B3CEC2C819A6B436CFB6078E37B49ECE4ECFDF6346F182C059401FD559EEB3C3C4E5645E40D112C1C4F934B3A149711753EED3E2CC9DBFCD907DB18AC97497A337C882F7DE710712904326FAF
			0834BF57BEDF76175B634BFE3A9D7963591A1F0F7A8A51BF0E0769FC33EC863FCD08F73EE9515D8A7F52C164D105187B04CC0C7F7F61C36377001034DFCBC506F4005D01D51FD7262B45D7275555557C43D3C3AAE263B324E27E53F0A8A86D211FE36BBAD9C215B5F9323160BF3ED910AF95F6CA45BC416C148AB1CB6C140AD89259A995C0029946DB8CBC843EE9D2E5682AB44B54461FFA6DC0C730459C9411EEC48D290C5D84D6F76B0D06344B3C692B46F3BE14DF66DA74A981BDA840B2F6E5079D84CB74FD0C
			D1E8430382ADB7E771BD20EA0116DA5A164D61C0ED4B6735AD39093607ACFCCB85FA5852364CDC2347BE372E511C5B76477D9EED65B5731CFAAD7A5B54E4768DC8B66005574907365EF8AF1E8FB50F9A7DBED9538CD93B526CF76337BF36E9C3317C8EB30B7CC354462CB4FF071945F1FBADFD7E9FD0CB8788666E5FDA1B94GGCCBBGGD0CB818294G94G88G88GEED9D4B0666E5FDA1B94GGCCBBGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81
			G81GBAGGG5594GGGG
		**end of data**/
	}
	/**
	 * This method initializes the ENTER, CANCEL, HELP panel.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (caButtonPanel == null)
		{
			caButtonPanel = new ButtonPanel();
			// have help button before.
			caButtonPanel.getBtnHelp().setVisible(false);
			caButtonPanel.setAsDefault(this);
			caButtonPanel.addActionListener(this);
		}
		return caButtonPanel;
	}
	/**
	 * Return the JDialogBoxContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new javax.swing.JPanel();
				java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
				consGridBagConstraints8.insets = new java.awt.Insets(7,35,15,35);
				consGridBagConstraints8.ipady = 13;
				consGridBagConstraints8.ipadx = 106;
				consGridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints8.weighty = 1.0;
				consGridBagConstraints8.weightx = 1.0;
				consGridBagConstraints8.gridy = 1;
				consGridBagConstraints8.gridx = 0;
				consGridBagConstraints7.insets = new java.awt.Insets(20,35,7,35);
				consGridBagConstraints7.ipady = -36;
				consGridBagConstraints7.ipadx = -118;
				consGridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints7.weighty = 1.0;
				consGridBagConstraints7.weightx = 1.0;
				consGridBagConstraints7.gridy = 0;
				consGridBagConstraints7.gridx = 0;
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(new java.awt.GridBagLayout());
				ivjJDialogBoxContentPane.add(getJPanel1(), consGridBagConstraints7);
				ivjJDialogBoxContentPane.add(getButtonPanel(), consGridBagConstraints8);
				RTSButtonGroup laButtonGrp = new RTSButtonGroup();
				// end defect 7884
				laButtonGrp.add(getradioTrace());
				laButtonGrp.add(getradioPayment());
				laButtonGrp.add(getradioFunds());
				laButtonGrp.add(getradioCheck());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJDialogBoxContentPane;
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
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				java.awt.GridBagConstraints constraintsradioTrace =
					new java.awt.GridBagConstraints();
				constraintsradioTrace.gridx = 1;
				constraintsradioTrace.gridy = 1;
				constraintsradioTrace.ipadx = 29;
				constraintsradioTrace.insets =
					new java.awt.Insets(31, 22, 9, 34);
				getJPanel1().add(
					getradioTrace(),
					constraintsradioTrace);
				java.awt.GridBagConstraints constraintstxtTrace =
					new java.awt.GridBagConstraints();
				constraintstxtTrace.gridx = 2;
				constraintstxtTrace.gridy = 1;
				constraintstxtTrace.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtTrace.weightx = 1.0;
				constraintstxtTrace.ipadx = 109;
				constraintstxtTrace.insets =
					new java.awt.Insets(32, 8, 10, 45);
				getJPanel1().add(gettxtTrace(), constraintstxtTrace);
				java.awt.GridBagConstraints constraintsradioPayment =
					new java.awt.GridBagConstraints();
				constraintsradioPayment.gridx = 1;
				constraintsradioPayment.gridy = 2;
				constraintsradioPayment.ipadx = 1;
				constraintsradioPayment.insets =
					new java.awt.Insets(9, 22, 8, 34);
				getJPanel1().add(
					getradioPayment(),
					constraintsradioPayment);
				java.awt.GridBagConstraints constraintstxtPayment =
					new java.awt.GridBagConstraints();
				constraintstxtPayment.gridx = 2;
				constraintstxtPayment.gridy = 2;
				constraintstxtPayment.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtPayment.weightx = 1.0;
				constraintstxtPayment.ipadx = 109;
				constraintstxtPayment.insets =
					new java.awt.Insets(10, 8, 9, 45);
				getJPanel1().add(
					gettxtPayment(),
					constraintstxtPayment);
				java.awt.GridBagConstraints constraintsradioFunds =
					new java.awt.GridBagConstraints();
				constraintsradioFunds.gridx = 1;
				constraintsradioFunds.gridy = 3;
				constraintsradioFunds.ipadx = 3;
				constraintsradioFunds.insets =
					new java.awt.Insets(9, 22, 8, 7);
				getJPanel1().add(
					getradioFunds(),
					constraintsradioFunds);
				java.awt.GridBagConstraints constraintstxtFunds =
					new java.awt.GridBagConstraints();
				constraintstxtFunds.gridx = 2;
				constraintstxtFunds.gridy = 3;
				constraintstxtFunds.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtFunds.weightx = 1.0;
				constraintstxtFunds.ipadx = 109;
				constraintstxtFunds.insets =
					new java.awt.Insets(10, 8, 9, 45);
				getJPanel1().add(gettxtFunds(), constraintstxtFunds);
				java.awt.GridBagConstraints constraintsradioCheck =
					new java.awt.GridBagConstraints();
				constraintsradioCheck.gridx = 1;
				constraintsradioCheck.gridy = 4;
				constraintsradioCheck.ipadx = 26;
				constraintsradioCheck.insets =
					new java.awt.Insets(8, 22, 33, 34);
				getJPanel1().add(
					getradioCheck(),
					constraintsradioCheck);
				java.awt.GridBagConstraints constraintstxtCheck =
					new java.awt.GridBagConstraints();
				constraintstxtCheck.gridx = 2;
				constraintstxtCheck.gridy = 4;
				constraintstxtCheck.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtCheck.weightx = 1.0;
				constraintstxtCheck.ipadx = 109;
				constraintstxtCheck.insets =
					new java.awt.Insets(9, 8, 34, 45);
				getJPanel1().add(gettxtCheck(), constraintstxtCheck);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), SELECT_KEY);
				ivjJPanel1.setBorder(laBorder);
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
	 * Return the radioCheck property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioCheck()
	{
		if (ivjradioCheck == null)
		{
			try
			{
				ivjradioCheck = new javax.swing.JRadioButton();
				ivjradioCheck.setName("radioCheck");
				ivjradioCheck.setMnemonic(KeyEvent.VK_C);
				ivjradioCheck.setText(CHECK_NO);
				// user code begin {1}
				ivjradioCheck.addActionListener(this);
				// defect 7884
				//ivjradioCheck.addKeyListener(this);
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCheck;
	}
	/**
	 * Return the radioFunds property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioFunds()
	{
		if (ivjradioFunds == null)
		{
			try
			{
				ivjradioFunds = new javax.swing.JRadioButton();
				ivjradioFunds.setName("radioFunds");
				ivjradioFunds.setMnemonic(KeyEvent.VK_F);
				ivjradioFunds.setText(FUNDS_RPT_DT);
				// user code begin {1}
				ivjradioFunds.addActionListener(this);
				// defect 7884
				//ivjradioFunds.addKeyListener(this);
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioFunds;
	}
	/**
	 * Return the radioPayment property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioPayment()
	{
		if (ivjradioPayment == null)
		{
			try
			{
				ivjradioPayment = new javax.swing.JRadioButton();
				ivjradioPayment.setName("radioPayment");
				ivjradioPayment.setMnemonic(KeyEvent.VK_P);
				ivjradioPayment.setText(PAYMENT_DT);
				// user code begin {1}
				ivjradioPayment.addActionListener(this);
				// defect 7884
				//ivjradioPayment.addKeyListener(this);
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPayment;
	}
	/**
	 * Return the radioTrace property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioTrace()
	{
		if (ivjradioTrace == null)
		{
			try
			{
				ivjradioTrace = new javax.swing.JRadioButton();
				ivjradioTrace.setName("radioTrace");
				ivjradioTrace.setSelected(true);
				ivjradioTrace.setMnemonic(KeyEvent.VK_T);
				ivjradioTrace.setText(TRACE_NO);
				// user code begin {1}
				ivjradioTrace.addActionListener(this);
				// defect 7884
				//ivjradioTrace.addKeyListener(this);
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioTrace;
	}
	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtCheck()
	{
		if (ivjtxtCheck == null)
		{
			try
			{
				ivjtxtCheck = new RTSInputField();
				ivjtxtCheck.setName("txtCheck");
				ivjtxtCheck.setInput(6);
				ivjtxtCheck.setEnabled(false);
				ivjtxtCheck.setMaxLength(7);
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
		return ivjtxtCheck;
	}
	/**
	 * Return the JInputField3 property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSDateField gettxtFunds()
	{
		if (ivjtxtFunds == null)
		{
			try
			{
				ivjtxtFunds = new RTSDateField();
				ivjtxtFunds.setName("txtFunds");
				ivjtxtFunds.setEnabled(false);
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
		return ivjtxtFunds;
	}
	/**
	 * Return the JInputField2 property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSDateField gettxtPayment()
	{
		if (ivjtxtPayment == null)
		{
			try
			{
				ivjtxtPayment = new RTSDateField();
				ivjtxtPayment.setName("txtPayment");
				ivjtxtPayment.setEnabled(false);
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
		return ivjtxtPayment;
	}
	/**
	 * Return the RTSInputFieldproperty value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtTrace()
	{
		if (ivjtxtTrace == null)
		{
			try
			{
				ivjtxtTrace = new RTSInputField();
				ivjtxtTrace.setName("txtTrace");
				ivjtxtTrace.setInput(1);
				// defect 8467
				ivjtxtTrace.setMaxLength(9);
				// end defect 8467 
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
		return ivjtxtTrace;
	}
	/**
	* Called whenever the part throws an exception.
	* 
	* @param aeException 
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
			setName("FrmFundsKeySelectionKEY021");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(400, 300);
			setModal(true);
			setTitle(TITLE_KEY021);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		caGSData = new GeneralSearchData();
		// user code end
	}
//	/**
//	 * Invoked when a key has been pressed.
//	 * 
//	 * @param aaKE
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		// to fix bug - screen was capturing residual key press from menu
//		cbKeyPressed = true;
//		// Handles key navigation of buttons
//		super.keyPressed(aaKE);
//		if (aaKE.getSource() instanceof RTSButton
//			&& (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
//				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT))
//		{
//			if (getbtnEnter().hasFocus())
//			{
//				getbtnCancel().requestFocus();
//			}
//			else if (getbtnCancel().hasFocus())
//			{
//				getbtnEnter().requestFocus();
//			}
//		}
//		// Handles key navigation of radiobuttons
//		// defect 7884 
//		// Only requestFocus() vs. setSelected() when use cursor 
//		// movement keys
//		if (aaKE.getSource() instanceof JRadioButton
//			&& aaKE.getKeyCode() != KeyEvent.VK_ENTER)
//		{
//			// defect 6887
//			// added the following code to replace code commented out below
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (aaKE.getSource() == getradioTrace())
//				{
//					//getradioCheck().setSelected(true);
//					getradioCheck().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioPayment())
//				{
//					//getradioTrace().setSelected(true);
//					getradioTrace().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioFunds())
//				{
//					//getradioPayment().setSelected(true);
//					getradioPayment().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioCheck())
//				{
//					//getradioFunds().setSelected(true);
//					getradioFunds().requestFocus();
//				}
//				// defect 6522
//				//setRadioButtons();
//				// end defect 6522
//			}
//			else if (
//				aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (aaKE.getSource() == getradioTrace())
//				{
//					//getradioPayment().setSelected(true);
//					getradioPayment().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioPayment())
//				{
//					//getradioFunds().setSelected(true);
//					getradioFunds().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioFunds())
//				{
//					//getradioCheck().setSelected(true);
//					getradioCheck().requestFocus();
//				}
//				else if (aaKE.getSource() == getradioCheck())
//				{
//					//getradioTrace().setSelected(true);
//					getradioTrace().requestFocus();
//				}
//				// defect 6522
//				//setRadioButtons();
//				// end defect 6522
//			}
//			// defect 6522
//			//setRadioButtons();
//			// end defect 6522
//			// end defect 7884 
//		}
//
//	}
//	/**
//	 * Detect keys released.
//	 * 
//	 * @param aaKE KeyEvent
//	 */
//
//	public void keyReleased(KeyEvent aaKE)
//	{
//		//Excecute this function only if any key is pressed. This will 
//		//avoid passing of events from desktop.
//		try
//		{
//			if (cbKeyPressed)
//			{
//				super.keyReleased(aaKE);
//			}
//		}
//		finally
//		{
//			cbKeyPressed = false;
//		}
//	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */

	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmFundsKeySelectionKEY021 aaFrmKEY021;
			aaFrmKEY021 = new FrmFundsKeySelectionKEY021();
			aaFrmKEY021.setModal(true);
			aaFrmKEY021
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			aaFrmKEY021.show();
			java.awt.Insets laInsets = aaFrmKEY021.getInsets();
			aaFrmKEY021.setSize(
				aaFrmKEY021.getWidth() + laInsets.left + laInsets.right,
				aaFrmKEY021.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			aaFrmKEY021.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
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
		// empty code block
	}
	/**
	 * For radio buttons & entry fields, clear text, enable/disable and 
	 * set focus.
	 */
	private void setRadioButtons()
	{
		// defect 6887
		clearAllColor(this);
		// end defect 6887

		gettxtTrace().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPayment().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFunds().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtCheck().setText(CommonConstant.STR_SPACE_EMPTY);

		gettxtCheck().setEnabled(getradioCheck().isSelected());
		gettxtFunds().setEnabled(getradioFunds().isSelected());
		gettxtPayment().setEnabled(getradioPayment().isSelected());
		gettxtTrace().setEnabled(getradioTrace().isSelected());
		
		// defect 7884
		// Allow RTSDialogBox to handle  
		//defect 6887
		//		if (getradioTrace().isSelected())
		//		{
		//			getradioTrace().requestFocus();
		//			//getbtnCancel().setNextFocusableComponent(
		//				getradioTrace());
		//		}
		//		else if (getradioPayment().isSelected())
		//		{
		//			getradioPayment().requestFocus();
		//			//getbtnCancel().setNextFocusableComponent(
		//				getradioPayment());
		//		}
		//		else if (getradioFunds().isSelected())
		//		{
		//			getradioFunds().requestFocus();
		//			//getbtnCancel().setNextFocusableComponent(
		//				getradioFunds());
		//		}
		//		else if (getradioCheck().isSelected())
		//		{
		//			getradioCheck().requestFocus();
		//			//getbtnCancel().setNextFocusableComponent(
		//				getradioCheck());
		//		}
		// getRootPane().setDefaultButton(ivjbtnEnter);
		// end defect 7884 
		
		// end defect 6887
	}
// defect 8494
//	/**
//	 * Puts focus on the trace radio button when first coming in
//	 * 
//	 * @param aaWE WindowEvent
//	 */
//
//	public void windowActivated(WindowEvent aaWE)
//	{
//		if (!cbAlreadyShown)
//		{
//			cbAlreadyShown = true;
//			getradioTrace().requestFocus();
//			super.windowActivated(aaWE);
//		}
//	}
// end defect 8494
}
