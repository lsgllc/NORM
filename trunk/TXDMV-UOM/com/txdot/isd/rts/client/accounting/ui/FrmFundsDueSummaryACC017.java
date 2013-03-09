package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.data.FundsDueDataList;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/* 
 * FrmFundsDueSummaryACC017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * RHicks   	07/23/2002	Added dialog prompt for help desk
 * RHicks   	07/29/2002	Remove dialog prompt for help desk
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	renaming of elements within 
 * 							FundsDueDataList Object
 * 							modify setData()
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
 * ACC017 present the Funds Due Summary
 * 
 * @version	5.2.3			08/10/2005  
 * @author	Michael Abernethy
 * <br>Creation Date:		06/12/2001 09:19:21 
 */

public class FrmFundsDueSummaryACC017
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JLabel ivjlblAmountDue = null;
	private JLabel ivjlblRemittanceAmount = null;
	private JLabel ivjstcLblSelect = null;
	private JLabel ivjstcLblTotal = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSButton ivjbtnRemit = null;
	private RTSTable ivjtblFundsDue = null;
	private TMACC017 caTableModel;
	
	// int 
	private int ciSelectedRow;

	// Object 
	private FundsDueDataList caFundsDueDataList;

	private final static String CANCEL = "Cancel";
	private final static String DEFLT_AMT = "0000000000.00";
	private final static String DEFLT_ZERO_DOLLAR = "0.00";
	private final static String ENTER = "Enter";
	private final static String ERRMSG_SINGLE_PAY = 	
		"A single payment cannot span across multiple Funds Report years.";
	private final static String ERRMSG_WARNING = "Warning";
	private final static String REMIT_FUNDS = "Remit Funds";
	private final static String SELECT_RPT = 	
		"Select a report using arrow keys and press enter:";
	private final static String TITLE_ACC017 = 	
		"Funds Due Summary   ACC017";
	private final static String TOTALS = "Totals:";

	/**
	 * FrmFundsDueSummary constructor comment.
	 * 
	 */
	public FrmFundsDueSummaryACC017()
	{
		super();
		initialize();
	}
	/**
	 * Creates a ACC017 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmFundsDueSummaryACC017(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC017 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmFundsDueSummaryACC017(JFrame aaParent)
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
			if (aaAE.getSource() == getbtnEnter())
			{
				ciSelectedRow = gettblFundsDue().getSelectedRow();
				FundsDueData laFundsDueData = new FundsDueData();
				laFundsDueData.setFundsDueDate(
					(RTSDate) gettblFundsDue().getModel().getValueAt(
						gettblFundsDue().getSelectedRow(),
						0));
				laFundsDueData.setFundsReportDate(
					(RTSDate) gettblFundsDue().getModel().getValueAt(
						gettblFundsDue().getSelectedRow(),
						1));
				laFundsDueData.setReportingDate(
					(RTSDate) gettblFundsDue().getModel().getValueAt(
						gettblFundsDue().getSelectedRow(),
						2));
				caFundsDueDataList.setSelectedRecord(laFundsDueData);
				if (laFundsDueData.getFundsReportDate().getYear()
					== caFundsDueDataList.getFundsReportYear()
					|| caFundsDueDataList.getFundsReportYear() == 0)
				{
					getController().processData(
						AbstractViewController.ENTER,
						caFundsDueDataList);
				}
				else
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.WARNING_MESSAGE,
							ERRMSG_SINGLE_PAY,
							ERRMSG_WARNING);
					leRTSEx.displayError(this);
					return;
				}
			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caFundsDueDataList);
			}
			else if (aaAE.getSource() == getbtnRemit())
			{
				getController().processData(
					VCFundsDueSummaryACC017.REMIT_FUNDS,
					caFundsDueDataList);
			}

		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Updates the total amount due on the screen
	 * 
	 * @return String
	 */
	private String getAllDueAmount()
	{
		Vector lvVector = caFundsDueDataList.getFundsDue();
		Dollar laDollar = new Dollar(DEFLT_ZERO_DOLLAR);
		int liSize = lvVector.size();
		for (int i = 0; i < liSize; i++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) lvVector.get(i);
			laDollar = laDollar.add(laFundsDueData.getDueAmount());
		}
		return laDollar.toString();
	}
	/**
	 * Updates the total amount remitted
	 * 
	 * @return String
	 */
	private String getAllRemittance()
	{
		Vector lvVector = caFundsDueDataList.getFundsDue();
		Dollar laDollar = new Dollar(DEFLT_ZERO_DOLLAR);
		int liSize = lvVector.size();
		for (int i = 0; i < liSize; i++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) lvVector.get(i);
			laDollar = laDollar.add(laFundsDueData.getRemitAmount());
		}
		return laDollar.toString();
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
				getRootPane().setDefaultButton(ivjbtnEnter);
				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.addKeyListener(this);
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
	 * Return the btnRemit property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRemit()
	{
		if (ivjbtnRemit == null)
		{
			try
			{
				ivjbtnRemit = new RTSButton();
				ivjbtnRemit.setName("btnRemit");
				ivjbtnRemit.setMnemonic('R');
				ivjbtnRemit.setText(REMIT_FUNDS);
				// user code begin {1}
				ivjbtnRemit.addActionListener(this);
				ivjbtnRemit.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnRemit;
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
			D0CB838494G88G88G24CDF9ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DB8DD4D557152629E913A96DA293D7E332F04A34E672C7DAB2A193B363F4ECE2A6B4B10D1DE5923BE2D6C9E33B3232E8A40913904564BC7ED1D084C422C41F91A3F678F9A8AAA2A83F8A080070E2484F84AE6FDD606A655D1777AE02E9BA193D4F4FFD1747FDD03B4CDBEBF36E794EB97B6C334FBEE76FF36E05946E09CA37259492E2CBA77CF74CC6083B1610FB9EF82CC0E0C9EB575A08693797E89B
			39D81D86E9D662DA5B3C830936BCF2FAEF9664CBA09F3BBF5136831E8B48593D61C760A1D4BFD0167C59C3F74F5E4F15C3221FCC1A778D66C03A85488ED8ABD084D09E793B41E20E0FC1FE8BFFC6322590D27CAA56F95ED3EC42D772FAE92F32B4095639FD3004178FC1FE8AE8B9D07C2B811E59D43A6B6BEAF97E331B1EA36B9EFFA2A5977BB2719754789AEB6B66291D742720A2D24903FB76C1DA5FFBE9742464F2E9939EDD710C56551D760D1F1E70387D2D93F27F447838643BC2D63E063225EAD9349DA111
			10E7F27CBA371E77B37F52F3A42D74C7992164A8FE1D75DF7FFAC00E251BF626A63C0E3CFFC5750A6D3E02FCAC9013F11D54A6E4F4E963157A14DB53AB953F3B52277BABDD2AA2FB744A007015930A157CCDADAD0E6AE328F3BA0E22A1B18E473A353618F59473E07FE3709C8634E01D98DF4381B13E01BF3FCC1206DFC9599AE2FC6B5631F1157CF7E0FC0D1FF8B63DCB6DA63D8667FEG4A3E820AG0A85DA89348EE897355725077E84E953305E220D0FEB1EA62F37D9F57B4EE9EED905673ABAA861D81F6C52
			FCEEC218CDE6E9659672884AF9E33ABD86EC68A3A2ECA83A6F856298094AC837985BC0F3D4061D7328F9A3B0B6B62EFFBC0263F2826E853A81A88AE8A9D07C9BE2DC1B8E9F1EEBDC9D9EDD76F9A435FFE258AB79746689C5F54BBEBE3E1503330D4F0AD83B5FDDF5BB2617BB8F1B75327A7E35A91996FAD9FD674A14E42E0F42B783FAE1FD6F9A8A55F74D1BB367036A79CD3A0FD0BBEC07E79B37D1064F27F82489E76B260462180F46FAC942EE873608B1F425BDCD722A93AD473060615F2768C9CC1E70246039
			7D4BB15C0B96833E92A881A86D2D3536D220C6A067DBE2FEBFBCDC75D75AED2FE9EED95BADB5C5104AD33ACF6A7078F5496312AFC8BEC59AD6E5BF61F5F654206C43535A1550756402BE4E489E59A7690A66816E52388C0664E7F57657EC43BABE1916BA15F199BA987792413722861D0CAA79758F3DEEC91783B5D879119A5465E56982F08486703C68AD54799E3A8F6A709C8144F4F5F7ADE2AFC11E2C97FA0929490774AC98362C565535AAAE948337D34E7F6EA338174817E18FEB153D32478D8FFEDEF65F11
			6C00DEC6B4C36687A90EEB248584779B783F9F5946F060E15632A8FF1416CB3A6ED306A7F439DF56F13F8C34FF124A621F367C7C042F7163E8E305B02EF220FAE3FC970E9618465725E917A63CCE1CB92E27C3F41F6D057AE36B057F0C7020BF8DDFAF6C741581E1272D3971247C69D7AC6D3435BDAAA3FABD334FE56B03575AED14E7200F77BD83966BABFC3D753A3BDAE2FD1C306CA3759A70C7E2FAAFBC01FACFG3933B62C3515ED90FA3F69281D2D37E649AF0B9556630399135D81ABE3F53FFBAC340D7D60
			18350D4DBFE6EDE3379D1B5D06FEFC4C4A06D859DD475037E8139E13CD7E74D8BEDD439E0FCC05B61A90667BDFF122AE940F22FF2438C7E53DDF79D206F95E0036F5142E41C6F8F6EE9036B439CF58D269172B495241DF26D8793385373F14923D015942528D81DBE2B27D2C96F5AE2B72B8A82A85438A06BFC057B747797EF247F16B3A779EC7FB3CA4DF61756EBB1EC1F09FD1A7E4E3ECB152E65431CF41788497AC0F91BB064CB1E2BC480771E4E972DADBED320809AE37235EAC42B4EEB7676AD12EDE286F86
			7AAAD958CDC55D3B26754A8D26C5D3D526FEE3DEF61E4843F9143F1810E1BB4685FD56984B5E9379C1E5977838D8794193B8AE5534974CCF46F11DB3E2C774958CF3D3ECADAC28DF26895FFBAE4F9C573A7DAFD84EA1522AE4B6F7894941FE6B3A86641B33F12D2DE423987B2293663D2A496F57DC8AF58BFCBEB2520E1239624CD16AE9D49AE476B97BDB95C955C61B35A991D75ECF77BB5C9F9C9BC5DC3955C563CA480781ADB064692F1FE9CF83751BA09DD1FCFE08075C721450696775289B74D39C96715BA1
			332E42862D63B724180DFC6F5C982C2B3813A8C7BC606B8C59FED33F4D24AB08B541AD0F42CC8AF93E5B104A6C1B673F5710AE6C1B087DF1F1487887A96BED9EAB735481A90BF33ECD75557AF6F07C73FD0ABB5F8E0EFF363BC47C630632383705BF7E79A99CD779BB602F5E9163321F4CB50D2BD7D3BC3AB126A7CF629A55FC4A28629158FFD18CA2ED3AE9B11F76941D63AE606986E2D877B0E2B7C0BE5240EE21D88C64173DA37C520DAE5C97D698791BE9BE49486FF2891F02F52E059F196937EA0CBE2E152F
			B293535BA327F00E260C7CE3B48F5B93DFEB77CD629EB182419A380FFE2F6CA2B1844F70FFB0763E9248BB00FA013CC091C0291417BC0CBC46B53724229D59305DD743E2EFC9B26A7D145653951D5AED9754A3647E530457ABA0BFCCC7BB6994670E5398B936E81043704903ED58FE3C4CE55E0FA3008F46993110468131F51FA3E571F5978FD8F18A4AD4A7951A5AE11FFB61CFA57225656A34F2DCEF8709397C9F261563FAFFB14DDCDEB9ECAECFB735E7FB43937B4539B5790997097A4073A16F8706009F2673
			F89A3571E15B1A7307D2191EEE9EC69B55FD93B2639BB5E95EF3EAB7BD97F2FFDE42792D60E9CE55A35CCFBF752919C75EED0F2658C27048CAE5ED8BF99A383BF87DA04E8B5D2FAFCA172529CA7FA43D3C684459D3593CA43B44FE5C8E6D59FEFCF184770BB048AFC08C082D0BED525FF6D67CC52650F36C3FBECF96FFE7E14A6690E30963E3F8092735498F53146DD7BFEE47719427A55AEA0086003CC07300A20158BACABA0DB2C22C5766D674A6DD17DCE354FB08907002A2CF86E0DCD80C77239DF4AF82BE4B
			8DDE274E7C8F2497240B7CE406ACB59F6584E02FCF734ABE7D8AC3290767F2AE6D44D1B39E571F599F0A87C853895352B3FC913C07D39AA5E27F31394DFACEGF9C8FA223157951A1E85317DFFBD1D37B2A837717542704D2354AF18F066973C34BE664320ACA7A3117B05A74F623AD9G79D820D5C089C0A4D368A56E4C7BCC47967AED03A0947688BC256AC2877734070E2F7F39FD6E336D3F35234F9F56F4DD9BC77E827FC5BB6ACB573C94E458FF36234D010B9A53D9D50EAFEF4779D565918AB36C718E541D
			912B33A96475FF5D01BA40389AA29865CB9A8675C3C54FA873DD45EEFA178ABAF1GB1EC3BCC63ED48BB8D2CF6C4784229CC310E48289D52C8484782AD87DA83148CD412857C005A330CB5C76BFE8679B020A82025C071C0A4BB51E687EA4C96F5AF0F04C10A7C04DD2C491CE9ABD604F2E03FD6E56D9C97728517C7867541E439154A129934977CB532605E35B5C459B61E627E11EBD19E6C2F59DCE6F406363FDFF54EEDFF4FF652B8946C46F9454BEDE7F5A70D05C6C67C32E167AFF46699D8073FD773ABE8B5
			4CC7D439B3288D7B6805031ACD6F635D68B7063205BFAE17310EAADD51A600E75AA62C53A21BFDEB8C54DD8514104DF45EC56B740C94DB68422C9308C77763DA6F6CF77960AC037BFFB5435B77A16E57DDDD43EA3F0C279C06AFFAF73301BBB59DA28906A794E3FDF5D8ED9A47439C9C2C987EF9DB81437B64F1C527FAE4B5F83BCBB4069E56BDED98CD70B665598CEB416A2AA1EBF9912FCBF971B35F28F90C73E15C9893AE05F4A5D069E65443A7A74D7EBE0157495BAC4A470B2C4AD5237CF81DD5F918D17E4E
			57D665730D7212E96D6F5C4C4ADFB24A277CD66549C6793DE72D4A73F204FD6463F646FDEA03EC0E8F9CFF3EBD856BD87954529CE6ABB5B9019803458755C3F8F7E94487155224DED7F74627381B2551AEEA05ACC6D0E4913B39F3C40CF00BCC6FDAA19F8B349C0849B575017075517F74B4497B407ADDC73C731914B5DC1E2C5CC05C627C4433097139F8C970A9396799DA96AAB6EB656D47CCFC44BE12CAC65B5EA316B1D0E0A590F1B67D562808056E4C95633C3B9C47998F79A4203CAD09DCF72F0F04082D3A
			24E191DB5DC4ED9E6B17ED917A794BE9733B20A5DB1F89998B8DEDE163895B9298D79A680771DED4A4F8AF5DA2E2ECEF1D10BF494012D30C7376D6A13B3D60B0394AF3FC7384B8DF8F57D33F514F58D6514F4F7C820BB23006A2012D5AAAE28BF28E773C3CBC91CB784EE2AC91AA46EA515CB2733A422FF8642B0DC1F98CC4E31D52BC9683598C3C10628E934EE2230532F857D68FE5D1F9C27E8F3AF15E5336016F852A856A85728205EF93E3FA682C034CF1576F543CDD6010D453B5B4639F56037C2300579220
			F820A4201CFC58A372857F542E4333682CCF5368718CCFEA128233C843B11F04A7F926436F0C8A9D56824F2E7C697EFBA8BFD4FC393B82F52267A7865DBB14D1FC1E89E7BA0CA997778E7321ACA9DF44A9BFBCE9B5071F9530FE3D85B37BE73AF939977747F9D09E853498E885D0E20150CBCF571C6F505A26E6F907F6D71115DCF905CC1E524219F231F693F5D66DEAF97DAE1E8E195A73B8BF552A1D4E6B5D405348996D86E6749783F51671FA2BC24A996B8FEE17GF549F6FEFE5C9E2ADD738C3D14C15DFADE
			3F57229D1B0F34F3984F78272D5FEDA344603B53747C93596ADD4135A2564712EF348FB6662A6E50316651EE6BF789755D566F921A3BE7FF1750599D7ADD42796E196F92EE6C89ADDBC44F5CF1702DBD5667309F754CBC07AD6A31BE075D5DE3BE0731B352C26AF75DE0C7B7EC976726B7A8968979E586F6FECC1C25D6ED97FBC9E9857A1332A2112F2479C6A37FAF6557EA4E318FEB3BFA6A1A75E1F55F4846FEAF0DA55CA003DEA47411FFCA68A32CC86C012F75061E6337FA2D6D6F1DDEEB7BCBBFBF7B7B525C
			73216C13493734C8483C4210F96F8EA173EA838B2F93754AF7886C0FF522DE2D015D76B53DC3067C1D86D61CAA5AAEB130AB2922ED1C013DF0FCE6DB0D76D1DC8C7D968B6CC1C316C683ABBEA178F5995893B45E0904FC0C019568226DB28353F551F60501FDDCA47A353FAB3005062EEA8C6C6DA2513671DDF186089D456F36AE4BBEDDF1019B23C1EC33646B55D445F5055F75BEC5578E5EF5C6986DF24750070EE9BE65CB0C8BE63494774487699AEBGDA81ED5BF7A2B6B656BA074D62DE134D1F8B21CD9950
			F61E2F07E7999665EFED62FC37E097463C7A30AA5E4D71FB64410AE97748508E6FBDD6C03AE62798473213742E950C185DD7AF6A03ACE1F29EBCB5171CC2D6CC1321CDB2179363163CDD09369B79B3637DD8FF00F76D0E59F827733DA0B5846F92603DF59A6F171D815EE9A7E6631DB9876F32DDAC2E9731D781396F6B02E9F35D24AA239E3C2563F37C84DDC7B8DFB55016493373C2C01E08545964491EC31EA16019BBCD1E6F50B5B56E11A1A8D3DCCC0605F562CC299AB2DC77D1C0062AD9F512EA32B7AB9926
			006737274970BB7FCC995E728B996699B25CBE901061FB7ADCF3BE1B8C9140534F1FD9FF575175FCBBD02451DF5C47017E364FEAE3193C3FD0769B8DBC8FCF9B739FC6EBA0B59DCFABCD275E4A8BEDFD4E0E16262E417E163E1E2E2E66263E41267E416E36365636D6563EF16C68AC6D5BFB7ABABE6969F6DAF3A0E48F6581E3A76217F84A8AE3FAA01F92A26EB6DE48B11F7DDB1FDD18320FD81F11976F62F788A501B3F26B73B7E7B29D5DB1F108DC65B9173D0E67675CDBAAD0CEDC370DA562BE600E86BC53AC
			027C922015C0B347A456F87A7F861E4BBE8FBC1F9DA4813B7C5A9E2463FE7CAB2E507D15FD9DE75A1D2C1DE7A850AE36AE406FA6A900676981BCF6B8005FDA0469013E833CEC179B4B5839AADBC63BCB0031E43D873E902895E8G488BB48FE881D0B450B22055C009C0E93B93ED45C0E5C075C03D3BC58C5276A97A1ED067D3FE41E4BA150AB8AA3282971F9B78CC6D363E0B8E87BC0A1745DA54096358EA0BB2F6D65DF59267AFF1F770BDC0A94513F7875F835C5BA06E813276A45A54BDE20C6F4921632CD2F9
			6ED83AC2360E258F4BB3E369DA59BA16BEA94FFC2751A2236D0F020E870CFA6D867631011D23BC917BC8F16BE3865ECF65C23CDD468E857EB115EB12E283621B8B9376B147DC748E9B068C6EC7349DC377CFC694D5E5F91556D1BCFED9B774718515D171CAA639AF9B984A4DE2221F53B5398266A1E10F916FD55135E7CF34955A851664C02C9672838656D6CD758579F0837BFB0AC559593CB36CFE0A4581364628F731DE44E2C9F6F13658FB1298367BA753737D8D5F7CD9F679B75A875BB31E2E40E7E9C2577A
			E43F72250CB7A942173DD2A16E76CA760A58AC220AFFF7C06F72597B0BBB2AC4CC6704FAAAD08C50B2A0BC2331F6DB2B719C619F53A6AF703836CB7160D291B1E93570532B0D3EBE2BAE60ED5A0DB80735E2EDAE9BED920DB6F7D56F6463E9416FDD6596CD1D987778CF7B3471F3CCE8D66F716ABCEA1F3A6CF3492A5A6F15DC30A5B33EAFD23EE96F43B3907BA663DB75E20C3500F58129C061C0516F0B3EAFD561F76B53FCA975B515CD9FBAFB867B5A7ABBBEE99B6CEA6AF78E3674F4FDF82EDB6CBFCFD509BB
			346540ABED1F38432D4D44FF5B382AEF7E1D67023E715F6190BA2A5FA77CDB1B035E71C1BE9CE851BEA1FFE3B576BBA229FE59300F6FCF4318DF79BD3D53E165C37578FE41A3CF5A456B054AD68562D07CD0384CD1EEF79CE4F25CD1AF6E32176C935F067C36BE785B104DA966EFBB36B584FF9B0263881437B484FF9BF26334EFC3A69A023F8D916D597AFA3AEA2EFB66F57B2C5EFB3EB8E73B5D166B394471375DEBBFC467AB2FB4581FBDCC711252E0FFF656A17CD9991405170A3DAC3A5E5CFFA260E97BE7FF
			F77C9E1D17258539F42D288A5EB853AF4D0DB17D87DE037367B85373F947B73FBFDE7CC67BE0BE6191EA374520A734AD0645D14C81985AABDB2B5287B9447816003ADB0617CC6586F061EE897B3F2C45D58F43A4C57F11246BE4A7BDA75B6120EC67DF315A6DF63616F5D24D5E5A5AA53BCF76EABE5DBE610785EF17FCBEED520E5F575AA50F5B6E7549FE3FDD46AD2DCEA719740D2E9F1EF29D462F52611819CF272FEBF51241DE516AE4B3FDA7EB279279FDE42B3132BB28BB0F3FFC38AE75451F242610348E12
			41421B8E1206B1CA87C927A1CC87C90570E68A7F45G4382DC06F8044974FFC17E0F8651999C45C5DDE220F8E5F0191726CDC39159B4835D4C17DAG45BAF55CF0F326216F71F4BA0A965BC0D19F491F3903B57D60B74FC68F3696261094986A184C4616BE0973CBC747FF6CCCF370EB737B5A246B17047CA6AC6EG0FE18F844EB4ACCE178ED1FBABCB34551409B8743E6A8A927A7B227EF1C9D5E12AAFB5F95C782DD17395DD8E5C6F0D9CC27EFED10797323271C3A877C5D31B7F87D0CB8788F72F817D6394G
			G0CB8GGD0CB818294G94G88G88G24CDF9ACF72F817D6394GG0CB8GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG9D95GGGG
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

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.gridwidth = 4;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 562;
				constraintsJScrollPane1.ipady = 239;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(7, 20, 4, 21);
				getJInternalFrameContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsstcLblSelect =
					new java.awt.GridBagConstraints();
				constraintsstcLblSelect.gridx = 1;
				constraintsstcLblSelect.gridy = 1;
				constraintsstcLblSelect.gridwidth = 2;
				constraintsstcLblSelect.ipadx = 68;
				constraintsstcLblSelect.insets =
					new java.awt.Insets(28, 20, 6, 17);
				getJInternalFrameContentPane().add(
					getstcLblSelect(),
					constraintsstcLblSelect);

				java.awt.GridBagConstraints constraintsstcLblTotal =
					new java.awt.GridBagConstraints();
				constraintsstcLblTotal.gridx = 2;
				constraintsstcLblTotal.gridy = 3;
				constraintsstcLblTotal.ipadx = 7;
				constraintsstcLblTotal.insets =
					new java.awt.Insets(4, 118, 11, 6);
				getJInternalFrameContentPane().add(
					getstcLblTotal(),
					constraintsstcLblTotal);

				java.awt.GridBagConstraints constraintslblAmountDue =
					new java.awt.GridBagConstraints();
				constraintslblAmountDue.gridx = 3;
				constraintslblAmountDue.gridy = 3;
				constraintslblAmountDue.ipadx = 2;
				constraintslblAmountDue.insets =
					new java.awt.Insets(4, 6, 11, 10);
				getJInternalFrameContentPane().add(
					getlblAmountDue(),
					constraintslblAmountDue);

				java
					.awt
					.GridBagConstraints constraintslblRemittanceAmount =
					new java.awt.GridBagConstraints();
				constraintslblRemittanceAmount.gridx = 4;
				constraintslblRemittanceAmount.gridy = 3;
				constraintslblRemittanceAmount.ipadx = 6;
				constraintslblRemittanceAmount.insets =
					new java.awt.Insets(4, 11, 11, 30);
				getJInternalFrameContentPane().add(
					getlblRemittanceAmount(),
					constraintslblRemittanceAmount);

				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 1;
				constraintsbtnEnter.gridy = 4;
				constraintsbtnEnter.ipadx = 65;
				constraintsbtnEnter.insets =
					new java.awt.Insets(12, 58, 10, 29);
				getJInternalFrameContentPane().add(
					getbtnEnter(),
					constraintsbtnEnter);

				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 2;
				constraintsbtnCancel.gridy = 4;
				constraintsbtnCancel.ipadx = 57;
				constraintsbtnCancel.insets =
					new java.awt.Insets(12, 29, 10, 10);
				getJInternalFrameContentPane().add(
					getbtnCancel(),
					constraintsbtnCancel);

				java.awt.GridBagConstraints constraintsbtnRemit =
					new java.awt.GridBagConstraints();
				constraintsbtnRemit.gridx = 3;
				constraintsbtnRemit.gridy = 4;
				constraintsbtnRemit.gridwidth = 2;
				constraintsbtnRemit.ipadx = 25;
				constraintsbtnRemit.insets =
					new java.awt.Insets(12, 48, 10, 61);
				getJInternalFrameContentPane().add(
					getbtnRemit(),
					constraintsbtnRemit);
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
				getJScrollPane1().setViewportView(gettblFundsDue());
				// user code begin {1}
				ivjJScrollPane1.getViewport().setBackground(
					Color.white);
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
	 * Return the lblRemittanceAmount property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblRemittanceAmount()
	{
		if (ivjlblRemittanceAmount == null)
		{
			try
			{
				ivjlblRemittanceAmount = new javax.swing.JLabel();
				ivjlblRemittanceAmount.setName("lblRemittanceAmount");
				ivjlblRemittanceAmount.setText(DEFLT_AMT);
				ivjlblRemittanceAmount.setHorizontalAlignment(
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
		return ivjlblRemittanceAmount;
	}
	/**
	 * Return the stcLblSelect property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblSelect()
	{
		if (ivjstcLblSelect == null)
		{
			try
			{
				ivjstcLblSelect = new javax.swing.JLabel();
				ivjstcLblSelect.setName("stcLblSelect");
				ivjstcLblSelect.setText(SELECT_RPT);
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
		return ivjstcLblSelect;
	}
	/**
	 * Return the stcLblTotal property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotal()
	{
		if (ivjstcLblTotal == null)
		{
			try
			{
				ivjstcLblTotal = new javax.swing.JLabel();
				ivjstcLblTotal.setName("stcLblTotal");
				ivjstcLblTotal.setText(TOTALS);
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
		return ivjstcLblTotal;
	}
	/**
	 * Return the tblFundsDue property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblFundsDue()
	{
		if (ivjtblFundsDue == null)
		{
			try
			{
				ivjtblFundsDue = new RTSTable();
				ivjtblFundsDue.setName("tblFundsDue");
				getJScrollPane1().setColumnHeaderView(
					ivjtblFundsDue.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblFundsDue.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblFundsDue.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC017());
				ivjtblFundsDue.setShowVerticalLines(false);
				ivjtblFundsDue.setShowHorizontalLines(false);
				ivjtblFundsDue.setAutoCreateColumnsFromModel(false);
				ivjtblFundsDue.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblFundsDue.setBounds(0, 0, 580, 261);
				// user code begin {1}
				caTableModel = (TMACC017) ivjtblFundsDue.getModel();
				TableColumn laTblColumnA =
					ivjtblFundsDue.getColumn(
						ivjtblFundsDue.getColumnName(0));
				laTblColumnA.setPreferredWidth(115);
				TableColumn laTblColumnB =
					ivjtblFundsDue.getColumn(
						ivjtblFundsDue.getColumnName(1));
				laTblColumnB.setPreferredWidth(115);
				TableColumn laTblColumnC =
					ivjtblFundsDue.getColumn(
						ivjtblFundsDue.getColumnName(2));
				laTblColumnC.setPreferredWidth(115);
				TableColumn laTblColumnD =
					ivjtblFundsDue.getColumn(
						ivjtblFundsDue.getColumnName(3));
				laTblColumnD.setPreferredWidth(114);
				TableColumn laTblColumnE =
					ivjtblFundsDue.getColumn(
						ivjtblFundsDue.getColumnName(4));
				laTblColumnE.setPreferredWidth(114);
				ivjtblFundsDue.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblFundsDue.init();
				laTblColumnA.setCellRenderer(
					ivjtblFundsDue.setColumnAlignment(RTSTable.CENTER));
				laTblColumnB.setCellRenderer(
					ivjtblFundsDue.setColumnAlignment(RTSTable.CENTER));
				laTblColumnC.setCellRenderer(
					ivjtblFundsDue.setColumnAlignment(RTSTable.CENTER));
				laTblColumnD.setCellRenderer(
					ivjtblFundsDue.setColumnAlignment(RTSTable.RIGHT));
				laTblColumnE.setCellRenderer(
					ivjtblFundsDue.setColumnAlignment(RTSTable.RIGHT));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblFundsDue;
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
			// user code end
			setName("FrmFundsDueSummary");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(625, 400);
			setModal(true);
			setTitle(TITLE_ACC017);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		gettblFundsDue().requestFocus();
		ciSelectedRow = -1;
		// user code end
	}
	/**
	 * Handles the key navigation in the button panel
	 * 
	 * @param aaKE	KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (getbtnEnter().hasFocus())
			{
				if (getbtnRemit().isEnabled())
				{
					getbtnRemit().requestFocus();
				}

				else
				{
					getbtnCancel().requestFocus();
				}

			}
			else if (getbtnCancel().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
			else if (getbtnRemit().hasFocus())
			{
				getbtnCancel().requestFocus();
			}
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (getbtnEnter().hasFocus())
			{
				getbtnCancel().requestFocus();
			}
			else if (getbtnCancel().hasFocus())
			{
				if (getbtnRemit().isEnabled())
				{
					getbtnRemit().requestFocus();
				}
				else
				{
					getbtnEnter().requestFocus();
				}
			}
			else if (getbtnRemit().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
		}
	}
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmFundsDueSummaryACC017 aaFrmACC017;
			aaFrmACC017 = new FrmFundsDueSummaryACC017();
			aaFrmACC017.setModal(true);
			aaFrmACC017
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aaFrmACC017.show();
			java.awt.Insets insets = aaFrmACC017.getInsets();
			aaFrmACC017.setSize(
				aaFrmACC017.getWidth() + insets.left + insets.right,
				aaFrmACC017.getHeight() + insets.top + insets.bottom);
			aaFrmACC017.setVisible(true);
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
		caFundsDueDataList = (FundsDueDataList) aaObject;
		caTableModel.add(caFundsDueDataList.getFundsDue());

		getlblAmountDue().setText(getAllDueAmount());
		getlblRemittanceAmount().setText(getAllRemittance());

		if (caFundsDueDataList.getFundsDue().size() > 0)
		{
			if (ciSelectedRow == -1)
			{
				gettblFundsDue().setRowSelectionInterval(0, 0);
			}
			else
			{
				gettblFundsDue().setRowSelectionInterval(
					ciSelectedRow,
					ciSelectedRow);
			}
		}

		if (caFundsDueDataList.getFundsDue().size() == 0)
		{
			getbtnEnter().setEnabled(false);
		}

		if (Double.parseDouble(getlblRemittanceAmount().getText())
			<= 0)
		{
			getbtnRemit().setEnabled(false);
		}
		else
		{
			getbtnRemit().setEnabled(true);
		}

		gettblFundsDue().requestFocus();

	}
}
