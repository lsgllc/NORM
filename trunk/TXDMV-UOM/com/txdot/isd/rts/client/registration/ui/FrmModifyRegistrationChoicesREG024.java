package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.RegistrationModifyData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmModifyRegistrationChoicesREG024.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and
 * 							doneWorking()
 * MAbs			05/28/2002	Making sure focus is always on Voluntary
 * 							CQU100004126
 * B Hargrove	05/13/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							add import com.txdot.isd.rts.services.util.
 * 								UtilityMethods
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 * 							Further cleanup to standards (if-else braces)
 * 							and remove unused variables, methods.
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	07/18/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out handling mnemonics in keyPressed
 * 							modify keyPressed()
 * 							defect 7894 Ver 5.2.3
 * J Ralph		12/15/2005	Fixed arrow key handling to follow 5.2.3
 * 							standard.
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3   
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing. Removed uneeded 
 * 							listeners.
 * 							remove keyPressed(), keyReleased(),
 * 								ciSelctdRadioButton, carrRadioButton, 
 * 								cbIsKeyPressed, focusGained(),
 * 								focusLost()
 * 							modify getpnlSelectOne(), initialize(), 
 * 								getradioApprehended(), 
 * 								getradioRegistration(), 
 * 								getradioVoluntary()
 * 							defect 7894 Ver 5.2.3 
 * ---------------------------------------------------------------------
 *
 */
/**
 * Modify Registration Choices REG024
 * This form presents the choices for the Modify event. The choices are:
 * 1) Voluntary Permanent Add'l Weight, 2) Apprehended Permanent
 *  Add'l Weight or 3) Registration Correction.
 * 
 * @version		5.2.3	01/03/2006 
 * @author 		Joseph Kwik
 * <br>Creation date:	6/26/2001 15:34:43 
 */

public class FrmModifyRegistrationChoicesREG024
	extends RTSDialogBox
	implements ActionListener//, WindowListener, FocusListener
{
	private JRadioButton ivjradioApprehended = null;
	private JRadioButton ivjradioRegistration = null;
	private JRadioButton ivjradioVoluntary = null;
	private JPanel ivjFrmModifyRegistrationChoicesREG024ContentPane1 =
		null;
	private JPanel ivjpnlSelectOne = null;
	// defect 7894
	// Int used to specify which radio button is selected
	//private int ciSelctdRadioButton = 0;
	// Array used allow for correct keyboard navigation
	//private JRadioButton[] carrRadioButton = new JRadioButton[3];
	//private boolean cbIsKeyPressed;
	// end defect 7894
	
	private ButtonPanel ivjButtonPanel = null;
	private RegistrationModifyData caRegModifyData =
		new RegistrationModifyData();

	
	private final static String APPR_PERM_ADDL_WT = 
		"Apprehended Permanent Add'l Weight";
	private final static String REG_CORRECT = "Registration Correction";
	private final static String SELECT_ONE = "Select one:";
	private final static String TITLE_REG024 = 
		"Modify Registration Choices   REG024";
	private final static String VOL_PERM_ADDL_WT = 
		"Voluntary Permanent Add'l Weight";
	
	/**
	 * FrmModifyRegistrationChoicesREG024 constructor comment.
	 */

	public FrmModifyRegistrationChoicesREG024()
	{
		super();
		initialize();
	}

	/**
	 * FrmModifyRegistrationChoicesREG024 constructor with parent.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmModifyRegistrationChoicesREG024(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmModifyRegistrationChoicesREG024 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmModifyRegistrationChoicesREG024(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		// Determines actions when Enter, Cancel, or Help are pressed.
		// Code to avoid actionPerformed being executed more than once 
		// when clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{ 
			//field validation
			clearAllColor(this);
			if (aaAE.getSource() == ivjButtonPanel.getBtnEnter())
			{
				if (getradioVoluntary().isSelected())
				{
					caRegModifyData.setRegModifyType(
						RegistrationConstant.REG_MODIFY_VOLUNTARY);
				}
				if (getradioApprehended().isSelected())
				{
					caRegModifyData.setRegModifyType(
						RegistrationConstant.REG_MODIFY_APPREHENDED);
				}
				if (getradioRegistration().isSelected())
				{
					caRegModifyData.setRegModifyType(
						RegistrationConstant.REG_MODIFY_REG);
				}
				getController().processData(
					AbstractViewController.ENTER,
					caRegModifyData);
			}

			else if (aaAE.getSource() == ivjButtonPanel.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);

			}
			else if (aaAE.getSource() == ivjButtonPanel.getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.REG024);
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.REG024);
				}
				else 
				{
					if (getradioVoluntary().isSelected())
					{
						RTSHelp.displayHelp(RTSHelp.REG024A);
					}
					else if (getradioApprehended().isSelected())
					{
						RTSHelp.displayHelp(RTSHelp.REG024B);
					}
					else if (getradioRegistration().isSelected())
					{
						RTSHelp.displayHelp(RTSHelp.REG024C);
					}
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}
//	/**
//	 * @see java.awt.event.FocusListener
//	 * 
//	 * @param aaFE FocusEvent
//	 */
//	public void focusGained(java.awt.event.FocusEvent aaFE)
//	{
//		// empty code block
//	}
//	/**
//	 * @see java.awt.event.FocusListener
//	 * 
//	 * @param aaFE FocusEvent
//	 */
//	public void focusLost(java.awt.event.FocusEvent aaFE)
//	{
//		// empty code block
//	}
	
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
			D0CB838494G88G88G4A00C3ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DB8FD4D45719A72DD6CFE3DC133A895D1A04341A45D3F6CBAB593A3BF68F5BE3F7ED352758B58959189CF3E2DA1B1326E4173325A71E4DE3G99958D8A987F909C89BA0CA08EB20383A211FFB2A01891881214BDF1E49E7074B1B319F948782749FE5F3D6F5E7798E67853D3BD67BB6F5D5F7D73FD773B5F7D6E6FBEC62170E8DCCE2621D090B2F384755F59CCC1683389421562411FB2AC36347B0020
			7BF78CE41FF06ED0B6BC63212E303F7BC03ED05FEE04729AA86775F59FF88F5E8B045CDD2D557092C5CFB1547DFAF15D5669759436B3BD39247C4A7BF9705C8B128C58D6906B87287F8F6F9FD4F1A314772A6FA81986C158C65BCC9C54610A5AEE137AD44A30CDE1C931DAEF05B236F142F330CC9BF3A731EEFE3DC3AD8FBE7C22107E33FB06BD28CBB7BE13B5EA5FAD6A53A4E49B5091D9C2FC7695F8B638EE0EB48E79462A14104727D4C9C1CFD5C089D68D491268D52A82620894D482EEC572F92B46257A7A0B
			0131EE1FC79A3E5D232BE99975C9C3E230272D5DFA8616CFF09D47399C8D99099EC1B888E5EA7746BD8D2ADDCBEF3EA848B7D20A22593D74B835F775F14DEE6B7AEDC523A7F06C73A5F9EA3F8C283782C6F54EAFE1BA93G2BB4A3F6E6825BEE0572F610C2B333653C1959B2F077F5A1637A1306F7235862B7D39BE24D1AAD73DEFE2D68900909B8A72E6BA6284BG49AB87FBC0DCA0FE10A3A4967F58FE921ECD5715965F58184F5B6477B74B1EGF8D214613D3E9EEAD42CC79C7285BC02C0632DACD499419EA6
			8B4BA7471A969FD785969F89BD2F8831899B0BF2A24CADF9614622E56A9809655A5C683C9ABAF1DEA9003F8112ED69BED08C52GB2E8E173DA51DEB3533CBA3C0A98703A655E716BFEF7C0E99E17E40F98D06757D2B25D7CA2896DF7E54E7D28DF62EB74FE593CAA4E109B41AF1B63679BC2966A0FA5964DAFD47757236ACE34CCDD8F3247AD9807CF13B8DC8B6F19EA0CD2BC116087CFEAB80DF1874131DC8EF50BCE32389D584B6650157D2C10DF7DD444B9447E78D1C35AC9EA0F31A2FCED3B8C18E7EC003BC0
			C6C1E2C062C0560024D43075CDEBBFBD4B38F569561676DD694404AA06A0C9F4F8030A5BBBA4763B83127B3AAC8685354D8F1CE87B75C97DFEC876539068E8973DA24DB0B03AFBCC04C08A52B6494EFD58A6A0125ABEE9CC8485E3FE010D3B4ED9GCF599DD4BE76FB5C0A28352075A9CE7465ADF7BF0C888320EFAA516767C84ECA037967C1197A6AF696E29EA85F2BE4FE3954319F1E17A030C5393E3ED59AC2B35C015BCCFFDBA7669271966450D651AFFABD7092D46B3ABAF7EAFE9976F11BFB880EFB24858C
			8FF2FC20F39F45E18C3D17D97DB5D26FD61400F4FDDC91FBC5C5113CA3DA7FD1E2CBF052768B8AF4EE7EEC0C31A5B02FF81055FCFEEFF59668665765735D9C7777614A29FEFA0A64598D703E35121D0D8993F8D69ACF31B8FDF30045E96B1EADC26533CFC60C5304459B0B9C27E8FC3ACE056F35FFA0E3EABA9AA78666386FE7ABE8B755F1753E69403DCC7D7EEA8E7ABD84BA1702A41DE2FE2F3CE4227B2D599D94598E7BA8GABA6FA34A823ED7B8D51E36C7F8C11E3EC4890B946A44374B17439A1D28C513A90
			61933E892FAEA67FE458CF763057AB9223F99701F20FB77B509F12D7D22ECA1E91D169156E080210FE8AE32B076C418D703E0547524487AC164A6EEC9612BF3BE308F41E452E782641D1C5E3282BCA0BA5EA5397D968F3D1964740D1AD3EF12FC271F9E4FF2B381ADFEEE5C7EEFBAF9B637126F8DBED97133DC340BCA20F0BFCEE096E5D686300B486CB059BD665FF2BCB747CCF867B10ABA642F3AD886D7B3029941E33A1D9B40E0AF3514E4D50BF8D47BB4D62E8FB76A15D7ED5832845A74BF4B978BA7DA6A79F
			57D57CFCDC04740C9B7C921FDBDA4E7E303AFEF51E34BEA38767A96BF343E91C2744F99F1E9D940BA158C6F056CF3258D95C1D2F67301E60A391579445F31A2EE568F478B9F645086346829E4F679E5C214FDDCD4120EFC8A29ED457E7C7F62DB0936F9DA1A70FCCA8EFCFDFEF2B6416FDA34D3E90633625C7D1AF0E3766B4631976E3389669D04E33F69FA833B2FBCAF3274617B9F797BC0725C0907811C78CB11F1E4AC55F603925420C4F1D517BEA5B0A48FC8E25412A66D2EB382F9E5B09F6480047F05B62F3
			7769FC855C930E6991D8C9E6CF3FB10B463BDA7E54184362DDE0DC4CFCF4BAAE3654BA199345C319721E1544DFEB2C61FCE89541B7D843795039E30C8FED063A8B556CFCFEE8974EEBB914132A593CFEE75C231B174BA7F995BE27E5BBF14F7A82520864E558D3848366CD1298BD639A499A2F05B1B703D06CC7A64404B3F00FB8434E0B6F9D431CE06265FFA465C1DEEEBA7617B8CFF07CC8B8557169DFC4C7D6941C7A6811DD389EA1DEFE1414EF8BAC6FFDFF827341B090B5B8BAFA7D6210D0D803BE7B78A84B
			FBB1B00725A0C9A01BC052C13248D81BCCB806A2A9A47686EB70AE7EB00626C06B8521CA18CDDEEC7141ED83E912D72CE6776E570F6973EEDC8D65ACEB6119DA436E0DF749DA63FC52GAB371178BCDA98661B1FE7D2BBE87D0B136A51D80B202F5FE3525763BE7E1DC1DF7F78247A9CDDFF3A67579FE777536D6BDF9564E796C73D637BEDB4B6162ACF477B2BB2D39E7BCD919D6797FF54670E7C3F39E5480CB24E8635FF2AAD7CDB41FFDB5007A4E75EF05FF2072A0293304875751D68E9D94D0925C65576BCBB
			4B098BEC38E7DB21BC882258591E0819B6E7C492BA17FF4DE5BEC97A27170455DF3BEC589DE5AE0B6CF48E896AB365482ACCFC521CD1D70073598CF599A045355D87ECA0CE10EB35AC07BC378FED847E55661114A6C5F18F0D128C4EE8D93FA4CEE8B086BC9DFB9017D55DA3457B0F43335FFDC33DA561D03EG9B2193DC7951759B10F57B5CA3824F2725613ED1EA353D3F905E1701A46AB01AB75FA63EDEDBCB73E68E47F75891CF5561B41FBEC45AE3798D28F31DE579D4AA4289A4C1B985A49D4468G9FB918
			DFBE5BDFCA7D9241A7EDC066E08F62EDCFE1BEE8AE084ED3BB8AE63EA3769460D9F95D27A83EB19C1F5F0B8A50DF0A4FCFC0157796E01CC0EA9FD5E8D346FB8B4859A78E93D84D6FA4DE9DB02FAE874B6F8BAA9073C3F9A1479EAEC4EC991413B876310D1D8D6B9CAC26B36D083D81656CBA0665912C984A97B8768DDB34DC0DFDE73397F07CE8F56ADDAC2A0EE8B9BEDCF22629D36F84C55157B5D4B4733ABED0C4F8912CC75FED3FC8312F951133F9F8B8A87278F930A81FE39DC117AFA8E15A276742472558DE
			F65F760DC37B55F538B63E13AC5FDF03F2C875D59CBC8779F973C485660DB1F7C89A9B9FA3178FE7BD7615AB49798E6FF9752CEDFCA1E98B171549ED13C9DCAC0277D81035343F6D60B4FE2363FDB2027B4E6F15FBC524639FF9C50AA7FD01FA1A47A14E3DA42D2A67D723BD7C7C42F9641D03D884D9F28E5B1C35684F9247B95A669EBC69DEFF6CA47BDE1A0A6D9B58FD237A9AFE8762F93D4ABD2154573787A4CF33FB240BF8156671CD243F937ADD8389B530FBD968844B4789CFBFAB0464485FF33774ADB0AC
			E9206B1054201DAD31AF3FD6C44F7AA0FE32D77D7315C2BC833D62040919D455AAC116418FB565E72AEDE7ED0E1ADA5B59CA1376EEE167C35C047E2CF36CDA9475CE11265A11FD1EBE17DFDE15C9EDF96920E6CB274D945D169A879AE137D9286E9BB719AF0A3F7B9C19DF34733AC155F96D3C668BFC52F374964999988BF16759B9FA035C73D2211C86E2ECE46B57D8E19766F21751C797536735303B0C29112D6B1FAE6A3F532FF9F7C154F3F620D13D1BB5EAF331413A52BC747849BF6F7B6EF7675C0F1EF54E
			8A74536AC6FA56E5F27C8A41576AF03AAF7E136F0B942833FD484E402B07B188E2211C843281E4ABC8768558F39758B9E8BAE095E678B65A6773F7C10214F51F6D6878F70F204FEDB0160B0F377A3D8AA18AD7G509570794500F21B2264A62EF21904E234558F63AC82C984D9873299A41D0FDDF628E61A31FBFCBEC21911BD3BA50896F204875CF8EB226BF1572EDF8FE3937001A66D8C3242FBAB48C0D3B8A7F919442D5CC47D1E4B715789BECF07537570939C4B4B20CEE8E6E7E650B2DDBCF820DD28F97219
			C8FDF320847362A2280B87C986C98549E8E6FE719F1A716F97ED21E97EFEF19917CCFD3F2AFB5FF272FEFDC35476489F6E2B8E755B726168E77C4861485F98478EC77E4698BCBC7DB7463B0723FFE37C4A61295F98E30E6C550FC73704DA3760882EBBFEDF145C32F407DC94FB2121F70456BFD39CFDDEEB0AE766AEBFA90E4CC95797CF6524A94511B969F345D3B969A3A4779FEC013C5F4238662F6C0C27B6F04CFC167154EB9CFB301C71D405E3F53B99CFDD58427652D6FA16C0B99DA43F1561E98437C25985
			6267782D5C70B8433E61710263C40A237488ED274672DFCD5191092766C699E3BA2ECA4EA4620B2FDB18AF1634B27F6C73B11FEDE9E5B92124A4FAEC9CAF099C7395A511E33E3AE47A3F5D5415C45B9314FF2C346FA0F196A01DECED04F75A717BD2BA58CB796E3B350C9F065AB43E3B845E715EC1392A0B44CBBCE0C9ED8CCBA2711286656DA00397C93B5D2D5378B1ABCC4A21CF83C811DAF6DD544ED7765D867B855C9E49576F134761F8413F11313AFCD68739B6A00E222BBD2CEE9F2B53FF6B41F9913E7B01
			D14FF717DF24F3CD3948783033ADD27DD6DE3F37BAD2FD46C5660727ACB379A1C767073CF60827F64D8FB6F83F8A4F33A0545775A44EAEG5E554E6E954F59716CC1EE59075F3DBC4DE4154B91E8D306C6B9E029B415522B024967956BCBADAA57BDAF6A392E75EE3FA1CB084C51C26DB4A69636EBDC171E37E7EB997FD98DF5E96D0CEFAA84AFF7423D95E4G04F2E327C960EBFB2F3CCA18BBCF27635CBC260DD3355B94FD1C9A3B2352D4ED37DA0E2AF1C16EE764DF5A0711B0BA7EE27CC4217A6915BCBDAFEF
			FD7E326103A8FE72BB29FF121D9A17AD0BDFD7C4577AC0F30990719BD88F06A9E5560CD3FFCA7EAE017CBF43493EB1FE594630418E0635D5B3AC9930394DF1CF9916687BB347B5FF96CD9C1DEEDDE0C1EA6C96159FDDBF0139242C13653E75A56838E8BC3185F891BDC5D95EBEDF7B67717BCBF36EC7F963B692170E4EF0FE7F9641DBBB43797D9D9E5FAE28CB6AE47C7E9BE65C5F87AF81678799G71034C83D98612883296A495A48D4458857981448A528A5287B28AF22F0B796DBFCAAD5378CD3DD06BD8B573
			43E74670F3EF890C393CCB7D6D137A5C50B573F721AD50A6BD2C9D55B12FADDC07319B6653CD5BDA55E7EB774CBA86200D5C9DC9C7E7F5380E98E837D4ED9B2FBE574CC2C78A34599A560E7AF878841E0541C97177A67EA9CFFD7F3D6EBD26767E736FEF5ED79DF44EFFEB0E4EC57E5EBCB3CF7D21B9B2CF7D11F9AACF7D31B9B2CFDDE75673D40A7D5C0CB9F18462FA00377BA547BE615873E4CC442ECA9EE51463AF913B90F70A2810612F923BA688B620E25BF458A7AA76DBB34682CCF95497E0FD7F4B0C39EC
			D812E5DA7E9F52C67286C5057B63EDE22364F76B6C964A9906F6D3DE78G495F999007F91799D77C9941CCD036F24C33137170D60E2DF4A0B6GE505E3FB6BB88F67D860BC631E299CFB3A0D69FD03E32F37B13D42C78C0B39487496F2ECC19D53DB4E3157385E860EBDF11069DD44B1391A690D6358AD9B531B4431DDF626F79D47CEDB185E4D9CFB10F36DB40E85ECCC6F859763C0314427E3DE91AE5F529065CA5FA3585F017471F62F98F1ED9588F65A6677240D876A7DAE46E79FA2EB918765845EAF5901E7
			2B1B7C68812F78EE2FC7E01C6C85879BA35545F85F713A3970DF63954823D7A86F43B3468A6F5D6ABB5571AD32468D00BB2FB0BBFFC156732BB89F0E9510751C8765A59CDBC930E4A8EF60D8E29DEB17F105593CFDC61B75DC353C87E221C73359856F27A7597C6FA4BE86819F6DE1FA7F1A44478A10259CFB122C67F2A8A7F22C0644C7AA1437F2EC40425A6577B21B3FBDA33F4E55596C00FEDD3D1A4D5760FD45E4BF93DBE440C33DEC6D7F4DE8403E350CA34D47A3DA65C883BB11671472BB0949A586A0B8A0
			3B1A1ABC9E33EC3ACA52CDA96B9BFFD26B7B04E3DAFED58704770CC5EDEB281B2D1E29BCAFB2C66DD9DF213D3FD92959D651B6FB5BFE55067E293533F176D5E963A89711DC166A6EE551AC27FDED27353E776A263341D62D3761053A596A191BFF3EE5555E7F25DA336DAD6B6CED8B58C87C1CE1637454E86388F66411257A3B2929459788501FD715F25D63B62DCF19E59A5D557633FA5D8FD8E69AFFEE7E0835EB6F7FDC2B593432FCB6B6517FCFF1773D37E143A57CD648D104B27ABB8C53E48369AFB1CCA693
			7DB1C6B0A06C1FBAEC5362971ECF782C3550A098BA049D23E4F5BB041C896487A17F176F3C738ECA70F3618B323F7F973679D7E9E9CA5D23D0B78CDA5E65DABA48E93F65D6CDFD562B5F49B288592002321F8EA19BA98CE88AD00DD940FEC2780BE176D58919D6BE999BF9835E9B6AC06A31855708B747979441B45306669D10223D4887C5D538DBCF214A49ED14596C02C9DD5E1AB26C09C93612B60A70DE14A01A546EF1BED63EBA5607D346FADC3DCFDB3E54FFF74818BF9675EFEC837D14474B7DD3FE0BF618
			FC0F82FCEDBF3BC37D7A903AA55AEFA5FA475C322C38651BCDDE8F7EEE227936A2EA5F59169D4671032C0DDA2972E472BD7A062E4F7F83D0CB87883DFD6C184D92GGFCB4GGD0CB818294G94G88G88G4A00C3AB3DFD6C184D92GGFCB4GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG8793GGGG
		**end of data**/
	}
	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private ButtonPanel getButtonPanel()
	{
		if (ivjButtonPanel == null)
		{
			try
			{
				ivjButtonPanel =
					new ButtonPanel();
				ivjButtonPanel.setName("ButtonPanel");
				ivjButtonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel.setAsDefault(this);
				ivjButtonPanel.addActionListener(this);
				// defect 7894
				// remove key listener
				//ivjButtonPanel.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel.getBtnHelp().addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel;
	}
	/**
	 * Return the FrmModifyRegistrationChoicesREG024ContentPane1
	 * property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmModifyRegistrationChoicesREG024ContentPane1()
	{
		if (ivjFrmModifyRegistrationChoicesREG024ContentPane1 == null)
		{
			try
			{
				ivjFrmModifyRegistrationChoicesREG024ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmModifyRegistrationChoicesREG024ContentPane1
					.setName(
					"FrmModifyRegistrationChoicesREG024ContentPane1");
				ivjFrmModifyRegistrationChoicesREG024ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmModifyRegistrationChoicesREG024ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmModifyRegistrationChoicesREG024ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(387, 254));

				java.awt.GridBagConstraints constraintspnlSelectOne =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOne.gridx = 1;
				constraintspnlSelectOne.gridy = 1;
				constraintspnlSelectOne.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOne.weightx = 1.0;
				constraintspnlSelectOne.weighty = 1.0;
				constraintspnlSelectOne.insets =
					new java.awt.Insets(16, 14, 7, 15);
				getFrmModifyRegistrationChoicesREG024ContentPane1()
					.add(
					getpnlSelectOne(),
					constraintspnlSelectOne);

				java.awt.GridBagConstraints constraintsButtonPanel =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel.gridx = 1;
				constraintsButtonPanel.gridy = 2;
				constraintsButtonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel.weightx = 1.0;
				constraintsButtonPanel.weighty = 1.0;
				constraintsButtonPanel.ipadx = 43;
				constraintsButtonPanel.ipady = 27;
				constraintsButtonPanel.insets =
					new java.awt.Insets(7, 57, 12, 58);
				getFrmModifyRegistrationChoicesREG024ContentPane1()
					.add(
					getButtonPanel(),
					constraintsButtonPanel);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmModifyRegistrationChoicesREG024ContentPane1;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JPanel getpnlSelectOne()
	{
		if (ivjpnlSelectOne == null)
		{
			try
			{
				ivjpnlSelectOne = new javax.swing.JPanel();
				ivjpnlSelectOne.setName("pnlSelectOne");
				ivjpnlSelectOne.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECT_ONE));
				ivjpnlSelectOne.setLayout(new java.awt.GridBagLayout());
				ivjpnlSelectOne.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlSelectOne.setMinimumSize(
					new java.awt.Dimension(358, 150));

				java.awt.GridBagConstraints constraintsradioVoluntary =
					new java.awt.GridBagConstraints();
				constraintsradioVoluntary.gridx = 1;
				constraintsradioVoluntary.gridy = 1;
				constraintsradioVoluntary.ipadx = 38;
				constraintsradioVoluntary.insets =
					new java.awt.Insets(14, 44, 7, 45);
				getpnlSelectOne().add(
					getradioVoluntary(),
					constraintsradioVoluntary);

				java.awt.GridBagConstraints constraintsradioApprehended =
					new java.awt.GridBagConstraints();
				constraintsradioApprehended.gridx = 1;
				constraintsradioApprehended.gridy = 2;
				constraintsradioApprehended.ipadx = 23;
				constraintsradioApprehended.insets =
					new java.awt.Insets(7, 44, 7, 38);
				getpnlSelectOne().add(
					getradioApprehended(),
					constraintsradioApprehended);

				java
					.awt
					.GridBagConstraints constraintsradioRegistration =
					new java.awt.GridBagConstraints();
				constraintsradioRegistration.gridx = 1;
				constraintsradioRegistration.gridy = 3;
				constraintsradioRegistration.ipadx = 82;
				constraintsradioRegistration.insets =
					new java.awt.Insets(7, 44, 16, 63);
				getpnlSelectOne().add(
					getradioRegistration(),
					constraintsradioRegistration);
				// user code begin {1}
				// defect 7894
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioVoluntary());
				laRadioGrp.add(getradioApprehended());
				laRadioGrp.add(getradioRegistration());
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjpnlSelectOne;
	}
	/**
	 * Return the radioApprehended property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioApprehended()
	{
		if (ivjradioApprehended == null)
		{
			try
			{
				ivjradioApprehended = new javax.swing.JRadioButton();
				ivjradioApprehended.setName("radioApprehended");
				ivjradioApprehended.setMnemonic(KeyEvent.VK_A);
				ivjradioApprehended.setText(
					APPR_PERM_ADDL_WT);
				ivjradioApprehended.setMaximumSize(
					new java.awt.Dimension(241, 22));
				ivjradioApprehended.setActionCommand(
					APPR_PERM_ADDL_WT);
				ivjradioApprehended.setMinimumSize(
					new java.awt.Dimension(241, 22));
				// defect 7894 
				//ivjradioApprehended.setNextFocusableComponent(
				//	getButtonPanel());
				// end defect 7894
				// user code begin {1}
				
				// defect 7894
				// remove key listener
				//ivjradioApprehended.addKeyListener(this);
				// remove focus listener
				//ivjradioApprehended.addFocusListener(this);
				// remove action listener
				//ivjradioApprehended.addActionListener(this);
				// end defect 7894
				

				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioApprehended;
	}
	/**
	 * Return the radioRegistration property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioRegistration()
	{
		if (ivjradioRegistration == null)
		{
			try
			{
				ivjradioRegistration = new javax.swing.JRadioButton();
				ivjradioRegistration.setName("radioRegistration");
				ivjradioRegistration.setMnemonic(KeyEvent.VK_R);
				ivjradioRegistration.setText(REG_CORRECT);
				ivjradioRegistration.setMaximumSize(
					new java.awt.Dimension(157, 22));
				ivjradioRegistration.setActionCommand(
					REG_CORRECT);
				ivjradioRegistration.setMinimumSize(
					new java.awt.Dimension(157, 22));
				// defect 7894
				//ivjradioRegistration.setNextFocusableComponent(
				//	getButtonPanel());
				// end defect 7894
				// user code begin {1}
				// defect 7894
				// remove key listener
				//ivjradioRegistration.addKeyListener(this);
				// remove action listener
				//ivjradioRegistration.addActionListener(this);
				// remove focus listener
				//ivjradioRegistration.addFocusListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioRegistration;
	}
	/**
	 * Return the radioVoluntary property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioVoluntary()
	{
		if (ivjradioVoluntary == null)
		{
			try
			{
				ivjradioVoluntary = new javax.swing.JRadioButton();
				ivjradioVoluntary.setName("radioVoluntary");
				ivjradioVoluntary.setMnemonic(KeyEvent.VK_V);
				ivjradioVoluntary.setText(
					VOL_PERM_ADDL_WT);
				ivjradioVoluntary.setMaximumSize(
					new java.awt.Dimension(219, 22));
				ivjradioVoluntary.setActionCommand(
					VOL_PERM_ADDL_WT);
				ivjradioVoluntary.setMinimumSize(
					new java.awt.Dimension(219, 22));
				// defect 7894
				//ivjradioVoluntary.setNextFocusableComponent(
				//	getButtonPanel());.
				// end defect 7894
				// user code begin {1}
				// defect 7894
				// remove key listener
				//ivjradioVoluntary.addKeyListener(this);
				// remove action listener
				//ivjradioVoluntary.addActionListener(this);
				// remove focus listener
				//ivjradioVoluntary.addFocusListener(this);
				// end defect 7894
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioVoluntary;
	}
	
	// defect 7894
	// remove unused method
	///**
	// * Insert the method's description here.
	// * 
	// * @return RegistrationModifyData
	// */
	//private RegistrationModifyData getRegModifyData()
	//{
	//	return caRegModifyData;
	//}
	// end defect 7894
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException java.lang.Throwable
	 */

	private void handleException(java.lang.Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
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
			// defect 7894
			//carrRadioButton[0] = getradioVoluntary();
			//carrRadioButton[1] = getradioApprehended();
			//carrRadioButton[2] = getradioRegistration();
			// end defect 7894
			
			getradioVoluntary().setSelected(true);
			getradioVoluntary().requestFocus();
			addWindowListener(this);
			// user code end
			setName("FrmModifyRegistrationChoicesREG024");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(375, 230);
			setTitle(TITLE_REG024);
			setContentPane(
				getFrmModifyRegistrationChoicesREG024ContentPane1());
		}
		catch (java.lang.Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}

		// user code end
	}
//	/**
//	 * Determines which radio button is currently selected.
//	 * Then depending on which arrow key is pressed, it sets that radio
//	 * button selected and requests focus.
//	 *
//	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		// Screen was capturing residual key press from menu
//		cbIsKeyPressed = true;
//
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			for (int i = 0; i < 3; i++)
//			{
//				// defect 7898
//				// Arrow Keys to follow 5.2.3 standard
//				// if (carrRadioButton[i].isSelected())
//				// end defect 7898
//				if (carrRadioButton[i].hasFocus())
//				{
//					ciSelctdRadioButton = i;
//					break;
//				}
//			}
//
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (ciSelctdRadioButton == 0)
//				{
//					ciSelctdRadioButton = 2;
//				}
//				else
//				{
//					ciSelctdRadioButton--;
//				}
//			}
//			else if (aaKE.getKeyCode() == KeyEvent.VK_DOWN ||
//				aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (ciSelctdRadioButton == 2)
//				{
//					ciSelctdRadioButton = 0;
//				}
//				else
//				{
//					ciSelctdRadioButton++;
//				}
//			}
//
//// defect 7894
//// Mnemonics handled by getters for radio buttons, not necessary here
////			else if ((aaKE.getModifiers() == KeyEvent.ALT_MASK &&
////				(aaKE.getKeyCode() == (int) 'v' ||
////				 aaKE.getKeyCode() == (int) 'V')) ||
////				 aaKE.getKeyCode() == (int) 'v' ||
////				 aaKE.getKeyCode() == (int) 'V')
////			{
////				ciSelctdRadioButton = 0;
////			}
////			else if ((aaKE.getModifiers() == KeyEvent.ALT_MASK &&
////				(aaKE.getKeyCode() == (int) 'a' ||
////				 aaKE.getKeyCode() == (int) 'A')) ||
////				 aaKE.getKeyCode() == (int) 'a' ||
////				 aaKE.getKeyCode() == (int) 'A')
////			{
////				ciSelctdRadioButton = 1;
////			}
////			else if ((aaKE.getModifiers() == KeyEvent.ALT_MASK &&
////				(aaKE.getKeyCode() == (int) 'r' ||
////				 aaKE.getKeyCode() == (int) 'R')) ||
////				 aaKE.getKeyCode() == (int) 'r' ||
////				 aaKE.getKeyCode() == (int) 'R')
////			{
////				ciSelctdRadioButton = 2;
////			}
//// end defect 7894
//
//			if (ciSelctdRadioButton == 0)
//			{
//				// defect 7898
//				// getradioVoluntary().setSelected(true);
//				// end defect 7898
//				getradioVoluntary().requestFocus();
//				// defect 7894
//				//getButtonPanel().setNextFocusableComponent(
//				//	getradioVoluntary());
//				// end defect 7894
//			}
//			else if (ciSelctdRadioButton == 1)
//			{
//				// defect 7898
//				// getradioApprehended().setSelected(true);
//			    // end defect 7898
//				getradioApprehended().requestFocus();
//				// defect 7894
//				//getButtonPanel().setNextFocusableComponent(
//				//	getradioApprehended());
//				// end defect 7894
//
//			}
//			else if (ciSelctdRadioButton == 2)
//			{
//				// defect 7898
//				// getradioRegistration().setSelected(true);
//				// end defect 7898
//				getradioRegistration().requestFocus();
//				// defect 7894
//				//getButtonPanel().setNextFocusableComponent(
//				//	getradioRegistration());
//				// end defect 7894
//
//			}
//		}
//
//		// defect 7894
//		// Remove arrow key handling. Done in ButtonPanel.
//		//if (aaKE.getSource() instanceof RTSButton)
//		//{
//		//
//		//	if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
//		//		|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
//		//	{
//		//		if (getButtonPanel().getBtnEnter().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnCancel().requestFocus();
//		//		}
//		//		else if (getButtonPanel().getBtnCancel().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnHelp().requestFocus();
//		//		}
//		//		else if (getButtonPanel().getBtnHelp().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnEnter().requestFocus();
//		//		}
//		//		aaKE.consume();
//		//	}
//		//	else if (aaKE.getKeyCode() == KeyEvent.VK_LEFT ||
//		//		aaKE.getKeyCode() == KeyEvent.VK_UP)
//		//	{
//		//		if (getButtonPanel().getBtnCancel().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnEnter().requestFocus();
//		//		}
//		//		else if (getButtonPanel().getBtnHelp().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnCancel().requestFocus();
//		//		}
//		//		else if (getButtonPanel().getBtnEnter().hasFocus())
//		//		{
//		//			getButtonPanel().getBtnHelp().requestFocus();
//		//		}
//		//		aaKE.consume();
//		//	}
//		//}
//		// end defect 7894
//	}
//	/**
//	 * Insert the method's description here.
//	 * 
//	 * @param aaKE KeyEvent
//	 */
//	public void keyReleased(KeyEvent aaKE)
//	{
//		// Excecute this function only if any key is pressed. This will
//		// avoid passing of events from desktop.
//		try
//		{
//			if (cbIsKeyPressed)
//			{
//				super.keyReleased(aaKE);
//			}
//		}
//		finally
//		{
//			cbIsKeyPressed = false;
//		}
//	}
	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */

	public static void main(java.lang.String[] aarrArgs)
	{

		try
		{

			FrmModifyRegistrationChoicesREG024
			 laFrmModifyRegistrationChoicesREG024;

			laFrmModifyRegistrationChoicesREG024 =
				new FrmModifyRegistrationChoicesREG024();

			laFrmModifyRegistrationChoicesREG024.setModal(true);

			laFrmModifyRegistrationChoicesREG024
				.addWindowListener(new java.awt.event.WindowAdapter()
			{

				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{

					System.exit(0);

				};

			});

			laFrmModifyRegistrationChoicesREG024.show();

			java.awt.Insets laInsets =
				laFrmModifyRegistrationChoicesREG024.getInsets();

			laFrmModifyRegistrationChoicesREG024.setSize(
				laFrmModifyRegistrationChoicesREG024.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmModifyRegistrationChoicesREG024.getHeight()
					+ laInsets.top
					+ laInsets.bottom);

			laFrmModifyRegistrationChoicesREG024.setVisibleRTS(true);

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
	 * @param aaDataObject Object
	 */

	public void setData(Object aaDataObject)
	{
		// empty code block
	}
	
	// defect 7894
	// remove unused method
	///**
	// * Insert the method's description here.
	// * 
	// * @param aaNewRegModifyData RegistrationModifyData
	// */
	//private void setRegModifyData(
	//	RegistrationModifyData aaNewRegModifyData)
	//{
	//	caRegModifyData = aaNewRegModifyData;
	//}
	// end defect 7894
	
	
}
