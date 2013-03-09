package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmItemSeizedACC005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001	Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			06/27/2002	Arrow keys work for checkboxes 
 * 							CQU100004346
 * M Listberger	11/19/2002  added code in keyPressed(KeyEvent) to 
 * 							regulate the focus of the fields for items 
 * 							seized sticker and plates.  An additional 
 * 							focus was displayed before the cursor moved 
 * 							to the selected field.  
 * 							defect 4895.
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
 * ACC005 is the item seized screen
 * 
 * @version 5.2.3			08/17/2005
 * @author	Michael Abernethy 
 * <br>Creation Date:		06/12/2001 08:47:23  
 */

public class FrmItemSeizedACC005
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JCheckBox ivjchkPlate = null;
	private JCheckBox ivjchkSticker = null;
	private JLabel ivjstcLblItemSeized = null;
	private JPanel ivjJInternalFrameContentPane = null;

	// Object 
	private MFVehicleData caMFVehData;
	
	private final static String ITEMS_SEIZED = "Indicate Item(s) Seized:";
	private final static String PLATES_SEIZED = "Plate(s) seized";
	private final static String STICKER_SEIZED = "Sticker seized";
	private final static String TITLE_ACC005 = "Item(s) Seized   ACC005";

	/**
	 * FrmItemSeized constructor comment.
	 */
	public FrmItemSeizedACC005()
	{
		super();
		initialize();
	}
	/**
	 * Creates the ACC005 with the parent
	 * 
	 * @param aaParent	Dialog 
	 */
	public FrmItemSeizedACC005(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates the ACC005 with the parent
	 * 
	 * @param aaParent	JFrame
	 */
	public FrmItemSeizedACC005(JFrame aaParent)
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
			clearAllColor(this);
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leRTSEx = new RTSException();
				if ((!isStickerSeized()) && (!isPlateSeized()))
				{
					leRTSEx.addException(
						new RTSException(603),
						getchkPlate());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				MFVehicleData laMFVehData =
					(MFVehicleData) UtilityMethods.copy(caMFVehData);

				if (isStickerSeized())
				{
					laMFVehData.getRegData().setStkrSeizdIndi(1);
				}
				if (isPlateSeized())
				{
					laMFVehData.getRegData().setPltSeizedIndi(1);
				}

				laMFVehData.getRegData().setRegHotCkIndi(0);
				laMFVehData.getTitleData().setTtlHotCkIndi(0);

				CompleteTransactionData laCTData =
					new CompleteTransactionData();
				laCTData.setVehicleInfo(laMFVehData);
				laCTData.setOrgVehicleInfo(caMFVehData);
				laCTData.setTransCode(getController().getTransCode());

				getController().processData(
					AbstractViewController.ENTER,
					laCTData);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC005);
			}

		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 *  Get Builder Data
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88GEFD1EEACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E1359A8BF0D455153141C6258EF528450ACE7C356994EDDAF00CAD5321ED2625B136D4C5F120E3D49C51091AAA9DF14CB8F7B7090418448D84886C82B17F4F9202FC44B4AA1090894692C9048592616D6ECB72485BFD6B6E8B899835675C4F5B174DEE824E1819B3775DF36FBD777C6EB9675E8DA9591E10EB311690E249A57C6FBD8BA1C9B50444AD715CA3F06936D48BB17D3D83D0CC9EBA1C83EDA60CA5
			3F17EA31116266BC68679427DA966ECB35EC026F75E4E3F3D295FC4458A79E46BC2117EC136F5363927B2C217D4A2A82E80BG72E0AFA740E8B16E5FD2D54A71DD50AF625F8896ABA1D9F40E33325404DF40672D646DA36BF04E49AA879FEF03FEAFC0A24002F5E11AEBA9F7531B7773FE6F0F1711D52957D98BF1AF93FD810E75EC6D815E161175D6D4C4B6394D3D8D5A162E21018FA50FC79B766B0AFF203939BD686B50E5DF372CDC123DE00F754857714ABC3A0610D168B39E7ED958427718B5340C1073FF4E
			0E4543AA3EF7E9C9180716DF5EEBDDD4023427D11D62BA9774E782B07E52B69D907CF9B4DF23BE6A5574C6A564ED8C6A21C60F2A48FE3DB14CF8633092417B41565626263D0C7FC5D5027F028D2916999B90A7D7603EED705D8B20EF90F2FDD0A364F2FD75BCD96D3E477AF68C3996EFE0722C5E9016EB613185564D54D752CF213D77ECCC3538GD40019GC9G0B816C54C7EFF655C3FB502D37EABE1F66BF98889CD23D41CFB52F2C42F7F3B30CF05CF1592385419E4C8FCFD6BA2370A3E0654671BE98761B13
			C4784D5C63CF1234CBA95939D1E4F3F8D23233B88D5B2630ECCC2E31B3A8D79360BB813CGE3GF3G96EC92F2BD635ABD15DC9DFEDD8E7AA535FB589D10027A21E1C5754AC1AE5F55D51349978D583AD9D73C0E69A5F137D9AF4B677D5A3AA60ADE16A74E32AE657A48B469056D7DE04C3DED25936D013027947D70F16A071570ED61BE4A70CFD37C28894F4E4B9045E3BF9E46B2CA055F3A0A048C1DB9CB08EDD7D2D499E67F69AEEB57E6464F6066C85B3A7AB07E4C82FC9240A200F4G32A55562GE85AA26C
			5BF4EA67E57AED174936EC6DF327B6C2AB0F6AC129439F52A53FC7BEA18595492D4AA142673CFC8AF9F70FDB77AABDCF9E5863E359AF87A5DD517CC0DD724960C8A1B6071CAE46B9C1190E76A8BE99B6708508201BF79A6690D58A691F873C12AE07E730F15BE95465856984D0848270BDED8B6A5CCF63DF97FC2BGCCD7533FC0DC8A74179BFA396E74BAE8BF814716556666B64503ECC8410BE27F69E7B0164897A0F6354981596F050F909F1BF1E6EDD8AF7D1A41738D940FE72495988F997859E70A999EE818
			35AC46936838246BC145BD2C4B5D320EF1B23CBE1172929AF77CC2041F713368E319A0D71EBD55D2EA97726D6ADDEF122FD353060683BDE8B92E2795B44EBAE1FE1BDD644CA7AAB0078E5A051F3E6892FE5AD618CE1CCB6E096A27E96E146CB8BB734FB9764833769C25995E634B8A57951E7BA24E53654716EB3F539E5917B90F697D25F328779460AB9D00B8045E555EB2F65E8EC9A1D91C30E3C130186C8DFB991B6B6E0B6DE3CADFF49F537A227B58F05F64BE74F5DFB49FE2E3F1FD18DB34913F49A72F69DB
			C74F305FAFD3260DA504657C97FBD0970ADF51CFAA5E81D96F06C48E350B83FDEBBABD0325706DF488DF9A29903ED4F9E9B9D9F8769AEB34FCB67B2EC4EB1C0359FA36A36CCB0C27EB3FC01D4B2A6C83C535E2B94170B3697966F89EDF2EBA9BFDEE7CD97447A179A21FB7636C1B846308BAAC9B32A5C9EF210E030A8FCC05871657052FD7196B42E4608FEB48AC14F92B280562BE435AAA22B463BED36BC61EEAE1EE8BC04FD661B3F967B61B4EAAF716D6CDD5196A8D1B7C679C9641A1794BE199C2B19E66CF8C
			B9324E2D0B98BB41E5E263B9D4A6559487542DA8539C23DE44BC41F073A86EA5D0D4E136483B1F5A4CF52CB7F4DBD47BA1A4EDE5F6CB599A19337ABCC8F78560D79932FFAB19635441D0C873A8B4A5F0DB3C19330FCCD5DB8E50AC23524272F8CFF71BA2295A40A1EDD45444DFD76771581037CD54140F5722AD8EC05F859058A678E9F4CF74255D6EFCE87B15E088EAA12FBCAAF47A3E9BF503B90A23C56D762ED9D7697D51EBB70478EDBCEEEE0B54559DDE64A39970E986EF735D45A6DDC11D89A9F9GACA978
			F1FB3219EF73FE3FA7D778B69131712A6A496A2EF44E47AA5E66EDCF35309A6753BA5417E3FBE46DF312621D5BA3EB1FA7EAC56D5382E3895BC5AE3EC926773F32D4CBC919106BF5CF21C92EAECD716B06CC09DEBC1FDAD099D07C82F7B745C115CD83964BE7B7D19BD7824D96G06AB2DC55CB9686B86CE2238F8684FA993B9293B8EE3C222516F237D45C67F19DA11CFF04E1566154B4BD9EF98FBDCEE9E3A92E0FA3BD5C69B0D9A7D5BE99FCA9EFE56FEB302B1229F8AB5C89D5D8159C3625FC11DA5D50B3837
			8A644F83DCAD409100C100ECCAEBE7AD5270E9DEC9C5BF226B5AEBC5ECD9EC4C9B2573F4C527FEDBD90EF7F99D1790B6DE3E9D7D642838F334E355582AC18FCBA7BF2EE131787ABAF3AC9EACE7F54C8CE893G5839FF147A844A188C384CF2446554141856611E87E1C2A334ECFC5F38F1BCEFF544BCBEB8EE9C4F7BA92BF9FC753871DC53FA969B9E2892F7562C871EA66A3D3744FC8F58DF41FC2D17372E0CF916951D77D9191E7AEA51C77560304C68A60C18E34E7E7C1BE34667D91CDEE205085373F90E7E47
			99B38D5B4F26DBADB1E8247335999511EF952FD03B50F8FDDE3AA00DB606C668E345D1341E4A6C72173ABC2E1B32CA910FDFE840F86102FEGA03ED21C3DE513462BE840E479639A2167793F7B97C93E3A4F7AD68CD992AA198C8BF91B797CBC5A32F8657123BC19B0D6D08577AA00A3G3DGFA1508DF772B48A354F9073D0AFED057A54FA04D9E227CBB2148A3E1B49EACC67B2BFC9A0B004E1D86AD62EB007604F41E5F4A1024962414G59955482F2D03F4830B443F3BEAF20F87CDC377B2AE251GEE0E02D9
			0E394FC3766811860808BF8F54C56AB925AA9C6B16D6CD0CFBAC7ECBCE34DBC6950B7FEB8D3C1F62732A43F81697DE21F646FEA50C45D70B3C700382BCB799504F2B013A9160GC0CF0D504BB53EF22663A87ABD8C85A844883C216AC2872ABFF6ED9D72CFFD2F9D73E34EF7EB3A2E79103E40DF65C7FD69DA00A2996E07FE1487D254204E26F27C0F7CE8DFD56E27E8061B160F3AB36AF4E6C2C1A79FF500B5B5D4B04AA5DA86F543C47FG4BDDCF51F393G1D445508FC767B86117792EB441999F0E21D18867D
			15GD9GA535E2CC27E3CE68F78228GB30C31579A4C77A6DC0BB4C4BF2BE66A5C077BCC1E6756B94DFB3018B1FE4E651C6F49735CF58531EDFFC34154363F39005600E0331E0B81EE370482DA07747707E443476EAA3099380ED01796D250E2ACBE3FD60773D56922B68C73F36A50D636FAF34ECA82DDAD854860BA6B24F39EEAA81DC4BEC67B31D6FA8F50BD1DEEB5FC4D6079530E3CFB86073AD4C917996E191D798C572DAB1EA12C19987EFF47B1E8009B6A78BE83611924D19E921D669CB68BF0D88BA5C34B
			467B47653812FAB65ED2AF46138EC79B6F0216656E7AB1330CDEBE1E886D02FAF10F5AF58E5F370CBC52A80D684D4D9F87956FA1E92013EA1565120CFA11639F299739E48B0DB1858D2916CA001686F1879D2D96B9E16E4F1710D5F274F770741E9B2DBD8D4CA6238D617CB6775882AB23F3FEC850F17C6AD1BA96AB074F676B171A68047D7E157E983932D3F20BDC7904D3644AD22710636BF673FB7E0237EF0C195B3A1CEC5F00B33C7FCA10036D7F6189FB747DDB87E54F90DE20988F3754087BD312D354D64F
			9E963815CEE1338F9B7612AB392B9DB2F91E382B3947C44DD836C35873376534B600FEGA0FE0708D77F2E3F525A0065266B29FFA46CE03949E260FFCA71F3CDF816334A9A446F9E49B066E894B96B3EA2345FB4684F81188F108610811037D3642D6642A6B2457BEB0F966804E0251A1E86997DE29B52F7G2D7DG3DGBAC0BCC002C1BF2E905F6FE3558B47B50D164DD8C1CB8AE81E26492004B7AC2643954EE8BA1C3F13794B221D9363B92FDD0A710CAF077155G36A63893G9CE992FCADAE1C72F7054323
			137C2EF0FBCDA4DF03C0FB2C0971B333E9A2DFECDD466148F5F7425CE4BEBFAD663A0131C96F45D156B11F2926EF7C2B1AA2FDE9A74513DD113E14DCAEFC29G46823B04AF4F42320F73F20B693B3A613B3CE5DEAE30BDBE7ADE77E07E3030A8F6CE7EEBD174374E870B223FF5BEDCB479DB67322258EF1DCF95CDFC6B5CE20B4D5BBB36296B05FADB74DA3149B631D65CEB0BDEAB7E57E62E95D9ED77BA0D7D0F000F2C58A56A3DF3BBC43D1739CB1C359F9447667F5A62683A1DD99CDD37B796CF7EDB452D4531
			F44F783B1F660A836FC26D7E2E6059DAAB6498B4F01EDAA107EE60325B455A9483F77747E26DD283D7F4C42CDDE1603E999338BD3B4519F2D6044F51DE53779F4A3F7FB37558773A872B5F566D20EFGE0DC760ED205CF29644E5D280F119DEDD32CB7C39C2CC158C0D8EC1A8D5F5D7C1B6D35185A556717A118AB9E763E73AC455D86108173998F3E12B08F17EA2762A1F7929E32603B02FF337D1C54BFFE811037C76CB7F3C3F83FAD562976DBB349FE25C073FD7E4D687DE4ECAAFAEBCD74AAE17D7EBD0C9E76
			BB613B8F5A8FG36D39D825FA47C47784F45FF0161EF96CF96186B4A36477A2CC27E483DDDFB789B4E1E71F5A533559BB55856F45570BDB4ACA378D8EBBB9D6B96E3B7E0BA0F42DF66C751F0ECCD6A966C31BAA0590EBACA87BE32G4A76028EGBA813CG23GB3G92G128196832CGD88DD0308FEAA900A60036FDA2169D5D046FE5316A99FE81B0D5B1A2AE2559E34740076DD34770657668B17CA97B44987E2CBDFA8CFF41BE713E7F329DFDFCG78F69973DEB5F0278CDC96250938130AD79FB470D6C7BE
			479F11F1C36377F18772B5C2F1AE0EABB461CEF15CBA875EC3C164C1ADA856EEF2E05C68D7D415756DF40E6207EB2C210FB287722884A4935FD5868E79E67F3BF522AD835A39AF10DC1DEC424BE8ED282E0CDDC84B4A4A58FF76682434436FD5BCF0A1AD9BBF25D9A7EF530BAA22C2942513A2FECD6508D0103CAD2E000D9D1449748B3B1B331F3EA35BCAF2BA481B4CE89DA487B55FC1F229E1BAC8B698EDB4409D9FFF9C738164073EA45F109CFE2CBE8340A31AE3E0F3B876F3G2B01DD4BA960F148E1E7356E
			DB73DB696A69E3B15FDB335E637F1F33BF72B71E3F51F8568978417D427FB7376F08657FD0B2F77BA4D555A5F568205F0B6FF807AE6AF2B8F77F3D9D6907449CBE48461634A35F67CDEB7E8FD0CB87885165FEE87A8EGG58A7GGD0CB818294G94G88G88GEFD1EEAC5165FEE87A8EGG58A7GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGB48FGGGG
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
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				//ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				//ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				//ivjbuttonPanel.getBtnHelp().addKeyListener(this);
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
	 * Return the chkPlate property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkPlate()
	{
		if (ivjchkPlate == null)
		{
			try
			{
				ivjchkPlate = new javax.swing.JCheckBox();
				ivjchkPlate.setName("chkPlate");
				ivjchkPlate.setMnemonic('P');
				ivjchkPlate.setText(PLATES_SEIZED);
				// user code begin {1}
				ivjchkPlate.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPlate;
	}
	/**
	 * Return the chkSticker property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkSticker()
	{
		if (ivjchkSticker == null)
		{
			try
			{
				ivjchkSticker = new javax.swing.JCheckBox();
				ivjchkSticker.setName("chkSticker");
				ivjchkSticker.setMnemonic('S');
				ivjchkSticker.setText(STICKER_SEIZED);
				// user code begin {1}
				ivjchkSticker.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSticker;
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

				java.awt.GridBagConstraints constraintsstcLblItemSeized =
					new java.awt.GridBagConstraints();
				constraintsstcLblItemSeized.gridx = 1;
				constraintsstcLblItemSeized.gridy = 1;
				constraintsstcLblItemSeized.ipadx = 41;
				constraintsstcLblItemSeized.insets =
					new java.awt.Insets(10, 18, 3, 109);
				getJInternalFrameContentPane().add(
					getstcLblItemSeized(),
					constraintsstcLblItemSeized);

				java.awt.GridBagConstraints constraintschkPlate =
					new java.awt.GridBagConstraints();
				constraintschkPlate.gridx = 1;
				constraintschkPlate.gridy = 2;
				constraintschkPlate.ipadx = 17;
				constraintschkPlate.insets =
					new java.awt.Insets(4, 81, 1, 93);
				getJInternalFrameContentPane().add(
					getchkPlate(),
					constraintschkPlate);

				java.awt.GridBagConstraints constraintschkSticker =
					new java.awt.GridBagConstraints();
				constraintschkSticker.gridx = 1;
				constraintschkSticker.gridy = 3;
				constraintschkSticker.ipadx = 28;
				constraintschkSticker.insets =
					new java.awt.Insets(2, 81, 4, 85);
				getJInternalFrameContentPane().add(
					getchkSticker(),
					constraintschkSticker);

				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 4;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 46;
				constraintsbuttonPanel.ipady = 30;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(4, 18, 9, 19);
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
	 * Return the stcLblItemSeized property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblItemSeized()
	{
		if (ivjstcLblItemSeized == null)
		{
			try
			{
				ivjstcLblItemSeized = new javax.swing.JLabel();
				ivjstcLblItemSeized.setName("stcLblItemSeized");
				ivjstcLblItemSeized.setText(ITEMS_SEIZED);
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
		return ivjstcLblItemSeized;
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
			setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
					- getSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2
					- getSize().height / 2);
			// user code end
			setName("FrmItemSeized");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(300, 160);
			setModal(true);
			setTitle(TITLE_ACC005);
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
	 * returns true if the plate seized check box is selected
	 * 
	 * @return boolean
	 */
	private boolean isPlateSeized()
	{
		return getchkPlate().isSelected();
	}
	/**
	 * returns true if the sticker check box is selected
	 * 
	 * @return boolean
	 */
	private boolean isStickerSeized()
	{
		return getchkSticker().isSelected();
	}
	/**
	 * Handles the key navigation of the button panel
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		if (aaKE.getSource() instanceof JCheckBox)
		{
			// defect 4895
			if (aaKE.getKeyCode() != KeyEvent.VK_ALT)
			{

				// needed to correct cursor placement when pressing 
				// short cut keys of P, S, Up, Down, Left, 
				// Right Arrow keys.
				if (getchkPlate().hasFocus())
				{
					if (aaKE.getKeyCode() == KeyEvent.VK_S
						|| aaKE.getKeyCode() == KeyEvent.VK_UP
						|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
						|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
						|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
					{
						getchkSticker().requestFocus();
					}
				}
				else
				{
					if (getchkSticker().hasFocus())
					{
						if (aaKE.getKeyCode() == KeyEvent.VK_P
							|| aaKE.getKeyCode() == KeyEvent.VK_UP
							|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
							|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
							|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
						{
							getchkPlate().requestFocus();
						}
					}
				}

			} // end defect 4895 
		}
	}
		
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmItemSeizedACC005 frame = new FrmItemSeizedACC005();
			frame.setVisible(true);
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
		caMFVehData = (MFVehicleData) UtilityMethods.copy(aaDataObject);
		getchkSticker().setSelected(
			caMFVehData.getRegData().getStkrSeizdIndi() == 1);
		getchkPlate().setSelected(
			caMFVehData.getRegData().getPltSeizedIndi() == 1);
	}
}
