package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmItemsOnHoldINV017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames modal
 *							to each other
 * MAbs			05/10/2002	Made button sizes uniform CQU100003849
 * Min Wang		05/14/2002	CQU100003895 Made blue highlight line 
 *							around "View Inv on Hold Report" complete.
 * Min Wang		08/20/2002	CQU100004632 Fixed the cursor never lands on
 *							the View Inventory on Hold Reprot check box.
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang    	06/23/2004	Make shift+tabing work.
 *							modify getbtnHold(), getbtnRelease(),  
 *							getchkViewInvOnHldRep(), gettblItmOnHld(),
 *							VCE change
 *							defect 6413 Ver 5.2.1
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	03/28/2005	Remove all setNextFocusableComponent's
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Re-arrange order of basic gui components.
 * 							This seems to be a factor in getting tab
 * 							to work properly.
 * 							defect 6413 Ver 5.2.3	
 * Min Wang		08/01/2005  Remove item code from screen.
 * 							modify gettblItmOnHld()
 * 							defect 8263 Ver 5.2.2. Fix 6	
 * Ray Rowehl	08/10/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	08/15/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * In the Hold/Release event, frame INV017 summarizes all the inventory 
 * items in the database that are currently on a user hold.
 *
 * @version 5.2.3		12/21/2005
 * @author	Sai Machavarapu 
 * <br>Creation Date	06/28/2001 9:11:38 
 */

public class FrmItemsOnHoldINV017
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnHold = null;
	private RTSButton ivjbtnRelease = null;
	private JPanel ivjJDialogContentPane = null;
	private ButtonPanel ivjButtonPanel = null;
	private JCheckBox ivjchkViewInvOnHldRep = null;
	private JScrollPane ivjJScrollPane = null;
	private RTSTable ivjtblItmOnHld = null;
	private TMINV017 caTableModel = null;
	private Vector cvIAVector = null;
	private boolean cbInit = true;

	/**
	 * Flag to determine whether an item has been either placed on hold 
	 * or released
	 */
	private boolean cbDisableCancel = false;

	/**
	 * Workaround code. This flag is added to indicate that a key is 
	 * pressed. This is being done becasue key released on desktop is 
	 * getting passed to this frame.
	 */
	private boolean cbKeyPressed;

	private JPanel ivjJPanel1 = null;

	/**
	 * FrmItemsOnHoldINV017 constructor comment.
	 */
	public FrmItemsOnHoldINV017()
	{
		super();
		initialize();
	}

	/**
	 * FrmItemsOnHoldINV017 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmItemsOnHoldINV017(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmItemsOnHoldINV017 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmItemsOnHoldINV017(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getbtnHold())
			{
				InventoryAllocationUIData laInvAllocUIData =
					new InventoryAllocationUIData();
				laInvAllocUIData.setCalcInv(false);
				laInvAllocUIData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laInvAllocUIData.setSubstaId(
					SystemProperty.getSubStationId());
				getController().processData(
					VCItemsOnHoldINV017.HOLD_INVENTORY_ITEM,
					laInvAllocUIData);
			}
			else if (aaAE.getSource() == getbtnRelease())
			{
				int i = gettblItmOnHld().getSelectedRow();
				if (i >= 0)
				{
					InventoryAllocationData laIAData =
						(InventoryAllocationData) cvIAVector.get(i);
					laIAData.setInvStatusCd(
						InventoryConstant.HOLD_INV_NOT);
					ProcessInventoryData laPIData =
						new ProcessInventoryData();
					laPIData =
						laPIData.convertFromInvAlloctnData(laIAData);
					getController().processData(
						InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
						laPIData);

					cvIAVector.remove(i);
					caTableModel.add(cvIAVector);
					if (cvIAVector.isEmpty())
					{
						getbtnRelease().setEnabled(false);
						getbtnHold().requestFocus();
					}
					else
					{
						getbtnRelease().setEnabled(true);
						if (i >= cvIAVector.size())
						{
							gettblItmOnHld().setRowSelectionInterval(
								i - 1,
								i - 1);
						}
						else
						{
							gettblItmOnHld().setRowSelectionInterval(
								i,
								i);
						}
						gettblItmOnHld().requestFocus();
					}

					// If an item has been either placed on hold or 
					// released, disable the cancel button so the user
					// can't cancel
					getchkViewInvOnHldRep().setSelected(true);
					getButtonPanel().getBtnCancel().setEnabled(false);
					cbDisableCancel = true;
				}
				else
				{
					return;
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnEnter()
					&& getchkViewInvOnHldRep().isSelected())
			{
				getController().processData(
					VCItemsOnHoldINV017.GENERATE_HOLD_REPORT,
					cvIAVector);
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnEnter()
					&& !getchkViewInvOnHldRep().isSelected())
			{
				getController().processData(
					AbstractViewController.ENTER,
					cvIAVector);
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV017);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnHold property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnHold()
	{
		if (ivjbtnHold == null)
		{
			try
			{
				ivjbtnHold = new RTSButton();
				ivjbtnHold.setName("btnHold");
				ivjbtnHold.setMnemonic(java.awt.event.KeyEvent.VK_O);
				ivjbtnHold.setText(CommonConstant.BTN_TXT_HOLD);
				//ivjbtnHold.setNextFocusableComponent(getbtnRelease());
				// user code begin {1}
				ivjbtnHold.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnHold;
	}

	/**
	 * Return the btnRelease property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRelease()
	{
		if (ivjbtnRelease == null)
		{
			try
			{
				ivjbtnRelease = new RTSButton();
				ivjbtnRelease.setName("btnRelease");
				ivjbtnRelease.setMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjbtnRelease.setText(
					CommonConstant.BTN_TXT_RELEASE);
				//ivjbtnRelease.setNextFocusableComponent(
				//	getButtonPanel());
				// user code begin {1}
				ivjbtnRelease.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRelease;
	}

	/**
	 * VAJ Builder Data
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88G72F1D4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBB8DD4D46715F4E37618DD3B51BD1C543A6E89E9E24A266444EDEC62EE4DD95335D9373109E9CCEB3B7454EC4C093B4764508DB9CBB7F473B17C8DE4CC0812CAA2E95020A2A222E0B48CDA79E7F084E4A60891058773E09EBEE60619878C68A6FB6F777366B14CC0B1B51CF3F96F3B5F5F3D773B5F7D795EB7A47F43384CB4D3BEA1E919047F1DCCA3E441E9C2FE7D420EAE015B722155C28CFFFB815E
			A1DD2E8CF8A6C15D229E2BA50F44661A211CD1E4352C7E4CEA598D6F3B48AB1D71557092E51E98285BBD71400559677922C74C13CD4BF70FE32337G4CB0D799C0308867FFF43C0063BB207C96FFC7C8B361E058E654D6E0402F616D367367E65A66A763053C3E854AFDG89GEB7706464C21545DDEFD0A17FB164411649F151AF660DC067185946DE3FD9B783308FC178AA21D9C5BDD8C4F1A0EEBC3E7944F386C513C7E496A6AA67F08CD13C7826D1E962F6A3235F5D59CBB39EA9F52768D1B1976A364861499
			9D4F6C28617344DE0BA36A85FBD6B4BAD2787C059F0568C8FE73762C8D9F61582FD339E23F9EA84782B09A9F4FBDABE89C700E14EBC117D7ABD7822EF23F96A89FD095203ADCA73EFCCC190D7E7A89C1FFDE3155323898F18F34603CAD705E8790AC96FC1DBEA8786A3E7E90C969BFEFFABB8ADF1B0A99BF2945A13E54C51BB35E237A7266D5DC73D37B2D169EG9FC0AC40AA00CDGFB281EBE51D38A4F5AFE2D5EBBB26275547AFCF52A4BFF5E6B12D5F82F2E069A0E6B14873CFE97A1CC97175B4AA250A3E07B
			7E69FA98521D0BC468CEC2677D24783573D91991F80B796579ACF2000D11FFA0449B636B5B3D48D79560BBG5CGB1GF1GEB8F883E722ED44E45174D2349FE0F24BA467AFD12DF2B9BD3D4176C677C25CD4C46DFA4E07D5E1EF7BFA61778CA23DC929FEEB3E5C710CBE2FC25A9114BA359A097B6F7CB5439738F4EDC8F04D387D18FFFEFC3BDAC0177B42E238CDFC271B78CF836DFD6D23D4572A228CBBAA874367BAD4103BDE39949BB569E110725BFE8B0B9CB98BDEAC9785A5EA6218DDA8A78D5G9BG3682
			E49C32DA0AG2A8E09753DFC65689F29379D0635E5FD732F7E8E1EF2D073CBB6CFC013BC83F21764D724FED58E905E66032BC8FB7F34FE7B68FE9A00B91AE50F6C17B4456B015125919994A9405A9C3E7A8E36714B3456290C48B041080F08F1AB2F6EC23E250076294FA5E9F228852B37DEC5D90ECBDDB0A28CG6F8B8E214C9F26764F896FBEGA62B157D08DB8F65AD3ADC9E393A931E67C031E5353A3AC199C0B2A47F2418FFE5AF5A92990DD6036C13BDAEF889703AC7FBF3C2F299746AB4BFC67138C76A01
			70000E7F675EF7989E46B0CAD954BFC96BA5CD73AB7DE31A6C10B5CD718C057ABFCBE989CC5BFE81427778G6AD8B270E5A935DA8ACB85FFBDFD3B8C7C593D5EEBE3BEA72E9C17D3AF353395503E25D4784DE29B7AD135D46869AB5DC2CF9BF62CA0E54F1D0F28274B4186ADAAE57AF9DFE978DEAB27E30666F82425FB1E7B5E88EAE9647DB95D36BC7D2566C81F231E497DFD99653E81685986E0BEAC647E0D3EA2365F6A2400ACF6D83B9FD6CCF605340C357D3A94DD47E22548BA36CC0A2CE3F1526CBA94AFC5
			52A1D67710043E45BB61B16864AAE9A75D439E0FCC09563B906673DFF122AC940F22DDD4DCC33266D026E4D8674328DBBF22FB30885E2B8E8BDD1A78C868D27154BDE4EDEF09A912BFDB3A22593468B0DB633843A1DDE2B4FD2F9FE5AE2B7288882A5EBB6651987E7BF4FFF3BC37AF8F8EC4EE7B70G6A63B5F91237FBF4A00B209DD147E41D3715521BA8E33FB282CB059B16470663934658F0AD5007F1E4C61955D2D0A6E2218369904412C8718D539B47A052D5816D9BGFA4A045ED43A5EB36CD72EB075DED5
			E562575725C84E43F514C747E4B04738214F693C9C14F70655F5F13ED87DD19979D28D36C0ABC33E1E57E3C674958C17C2F1CBC0B82F258B5FFBBE4F984F3A82E7A32EA1426AB236F69B4A427D5642A19CF7BB60D3F55E9F100D362AB690708EA854AD7075484A78184C95DF8ED1CF235268325369E8D0A455BBD4678D9242624AAD545EA19FF9C7C4DC99BBC563CAA87781840F88FAAE8D4E54272B0339709CD47C810807DCF2D04854B508E3221F62E8913F9DB74AEA534E3608719B4262A35CF69E891755D3EE
			24E3AD60B769347DEA709D032CA0568437BC84ABA968797AD0BA53EFDEDEB214A9741B887B78A20DFF369D09ECCBD3B9BE2F1CBD0B6149629C73F5145729727078E7A74537140747BF4B26C47C538DF5EB4A05BFDE27D07BF054EA29B8AA78BA605EE1602B432BF8B41D27E75D38C73DFEE5C871885CE60A03C81B9AAD6653D2699A1F05B1DDG8C678CA2AE886545BA6E59C9442D0272FA9DD7B80538C4A8A79D953E6A2D6BE8ABD275F281AD97D608F25C541771B391E5FFB434A6EC0EFF78134FE174EFCC167F
			2A603A8575720FE9F912375DC5F6855F0527CB9E1446D4F0BB5E005C6E63E19D60EFD4207C4A291C315F598AE18B7FFE826D4DA084FE600A9CBEF900B80FE15BEBC1E1C757C25BCDG49G79D0D78540667C4FC99CE3446B12D4FD0E34A9E127166B6DAA27301D26E8F48FA49C437301E7B10C6434BF5006B631155903FAAF24B29803F930B13569366BC61B1EF40C45C379472D161263E20E46A05A45E1E9DC8A1687A60017720B0A47651D0061B0AA86A354505E5B566EEC3135B577363775565B5B9D0D5C8E44
			CD986D40295C6A28B6736CF13676CE7E5C6A1052186DDA224BF691542DBB0E388FA6734356749993A16570E4756D536A5F61902A7F5A34FA24753799467A072755A34D7BF9FF4657137BC56E1D7AE4BCD13FD79375EC43D2497829604F60BEA96DBE8B49E632AD1842BD257947E4B66E53170D724ADBD6ECCA0BB22E1B0F27D5061F193CFA8857147A8B4322D5D73762422BCC26A7EF18B9DF7115429FE4E7213D5A826594003CAAE12F9CFEECDB826506AAE12F8AC68F4333CB9A6659D323CB513CFE9A70813243
			6F059D22CDB2AC7542FCBFA787EFAE6FFDBCDB48FB55BFFE0B2C7E735434B7234845D545643100BF7376CAE978E43CBC96C0592C023A0DG49G169310B3821CBAA1F86B9DC59AA1EEC572EBB5CD9AF0D3CFA84259AEC51E8821F1E3333137204966B6E445687EE8720191DAE1D95A7B0741FBB925A1A2ECDD4D0DF05958CF046CE15F09197E0A791F6BF45D34934C7F64687805943F5800E7FEE98745E3B9816A72BE96FCF77BF1CCBD06E64B4679697563FE40F897AC09B2C50D1F839AFA06986FAF0FE13D86E3
			AD86D885309EA0D19F7B857FBEA6538872EC04GDA5892B15F93016839410F83F36765BF8DE03C526F55B46F880EAF70098114356675D1A443BD9FC0D900FBF5EB2CA9473F94C03DD765C10AE6FE52C177CD8A70E5B9A9FC67F3194247D61C943E71EBD99827BA212C81AC8238CF2F7BABDA379A4A1B81D2810ACE093ADDD93379BB9C43D8DE94D69673C56B4F66A81BF50E1C88FD427DE766AC7D196CDE9B0B3EFE29E3F32FDF66B80DC1C16E4EC91F4CF03963B48E9A9C8C483A1E3CBD1E2763EC018EEFC0C14D
			E436F5788636D725C96F9834DFF98A57EAC10670F78DD06E81D8G3094G631A945AE6FBE641AC7C313177FEC273C377B5B486B64FB8E4BA2A2BD376097A3253E81F6A46C07FBC546CF27C65A64437BA867C105BD0C74C707599A8E7E6210F119B1FA09DBF48B27A20331FB09A5D70E43E7B21E97958BADEEF2E06E735E8932F985BD4D533B6FD705474B6BF1743E3GEC33886A63G184DC84890E75F2B8117D4ADE21CE3FDF88E277B1AF2E9C22B2EEE76AB2EBAE948CEA54F7CCDF2067061A5D661EF76E56218
			FDD08EA24EAAF2656081E16B936EDFC6363D9C793CFE0B7304A95E4A73ACEB488746AC5A1C4146993EA646A97C4E5F503AE8BE5ADC437A1755044691FEBF1D1C695AC3A27A53FA37BCF08D13AC4663EF3210C71CC32D913C7CDF13715B431A37CF1822792D0DFC6EA4838D95408B27617089B21F9C5028F9A287FCE9C2787C0A53E28DDEF3639A38219C83307434D85721468FA273EC50DCBD3724798C6A6FCAFD5CB49F0E3B8D4A640C55D2G405A7E4C2D53F0C6343D2B1152G65980025E704AD7CEE464D1D2D
			2F3D49FE3F19F7BF66E76F22FA3C728C7333E9BA7EEE0ADFE340333DD412A97652FA28AB7B03083376523CE0B11493G5681A4822482941C95FE743E718AB247F936536B330361D58DC72DEC7C6B13B8FE990C55822082ACGD88A30D29F7F15F17C9E92AD2E6B74FAE976000904240036D1376D17B0DBE5B27C5F993EE59D0C3D79ECF41F34BD2C0E54726FB97CD9D29B2AE7FC3CB805F63186705DGBE0005G713502076A09B93F69B486E7792633FD46771175869A92EB2345E10A026B1AD49B2E87FE0ACF2D
			8D5703575DC28F4CF5309EF5C26FDFD4447C9B813735EE7AFCA9F551669F23FB4ED29FFEBE7185459756071FCFE47B4479C49954AD2E9773A73A4379CF2E8F4DE9EE08B6FFEE931D27014D1329635F25788A831E4D3F1C6EFBACB7C0DDFC0318FFB97D864E5ED7985E4BB37F54E781B3EDC073AD1D0369E7E9B0FAECF4AC98795C7BE4B072397769606C675E0D4168675E76604CF36F851351E97B4B49396336584948F177324919F1775D1311636E9513B3636E22EC44D99AA1D6EE94F137C2F1D5D0EE50F1CFD1
			DC8714DDBA2E40CC63F3A8AF55F12AD9446C89BA2E157A47D4A897B689DB7272D4F4F9A4CFC5DE2B7F190A3CD6647A6C5F414C57232DA523AF1D1E03D4GED8DCD0266BF1B94FC381B447E71AA21BD93B43C27A937FE7F3CFFCB6760671CB45ED88D7CA66AF278F498F1C9D0AEE896381543C2B6E54DC2B657FDF85EF2C3AF5FCE4B31FAF9076F4B500F634EAD2383DFEA0E393E4FB15EDF25F156EA20E703AE8F3FAE0FA41D5F3CA6FAE65CE235B8DB84EE8F4529D0DE24633E55B8974D587FE678B92B4F717DB9
			6718AD6F1CCBE6AC4FBAC96D4308C70640C099E0672AED9477A0C0C20BB8730C492667F2303959196A5DB708BE460B34FDC046EFD3320B0D111AAD721BB57A98C74D381F47E53F268CC8EA2D2A8CF9705C030873DC1BD9745988FD0AEC083B4D5CB0077EE49A78A1508761DDBECEBE3C7F760DB75EF8CF27D51A40EFCBEE2FDF1942184DC08523613A1E9338ED026E7703B37366E37A19F5AC34CBG5884304D464EB0986D5F1C1C0D764CF05ADB2D167CD6C6BB16CB609D633693BA6D3B06F79B6447920FBA495F
			61D51501C9F16ECEF57A90C0D52B20DF9DFEEF9A4F61BD057C97523E27F11DDB858F7FB2BC9B8F0276EC7EDC8D7DB698F858826F8F01627CB2CD7050FF8473BD2DDF35E9A3740C001F3BBF62B3665C5BDB59B9D059B92B656CB941C72EB2F39D766A79FC77B9368EBEF8AEB8474E10988FF74C4903F19D16C35F1567C2BC2C03777D535661CE6A3B7BB57AB117632C5D398C57895BC08A0073E6E796671B0CE7969BB9DDF9F62B055547B4C72AAF368B1EE5FAAEFE306B205081B352BAA829B885477519715E2371
			FB04837F07BEC754B736B99BBB214D0734BD442CA4744776F37667784EF27467AD46F302061F2526FDC4A26768276C4C46AD3407F2744DDBFE1141687A6B098FE7274B5E586464316F2520087D5742D8A9F611F7D79EC75D8DBBCFE9D23D93F62E86BC673EC77FA6D2F1DE6451CF53B15DD00E81D88AB0137F24BFC442B119FC7E8551E55CB8DC95460BA173AF6FEA6CF45A6AEB6D3D0E7A4EF63B3D2E36333756515B5658585058407A5FBD7C71AC7DDB5ABBED1F3537B9A30F90E95DE6237B9EDAE2727B7658F1
			B24FEF656CAAG176BABBE7DACC3176B81FA57E59B14C99B4497ED62CC4C2608B52CG1C3BCD1CF1ACD1445A443509B17E09365D8C65A4GF37BFC57666934900FC53BAA674BA3BB8E62BCAEE892BC96358B1A9FED96386EF6F19E7118896D48E2A8A7GEC845886108A105FE13514G54GF4G3881E2GE281628156GECG5882105CA1E2E97B67E827231DAD70C3C34309020817FA677D4D08759B3949FE774F3B9F4BF36BA7F10D4D974273FFBB4567DF884F7F1F53EDC09154AD38A06220D7FD74FE4205A8
			7793B8BE2FB3D44F66D9C6753438B3FC7EFBA93E22B3FC7EB2D94CDF83F5F11DE27EF6656664F665A67B5DB56FFE0C2F4AE124BF43914EEF8D4567B942797D213EDF8B21AE46A1783D23A9FC7E2DD01762E07235B8E72E836B3726B93CDF91342D626DDBA274E37B6099D3741CF233E96E9C7BF9D3649C7BA5534C9CFB3BA9F20E7DDFA6E30E4DF07FCD3F4D8F41BE6C565B256A38CBBAAEADBD0763AEAAAE4D2D633353F3B93EC5468985FEC7BA52B5C1F15D9C3753003B44F13B53B15E8116A1F493FD8B53B18E
			9FD4D4155F7F22ED94CFC056F4F96CCFC79A951FE4203BD447DD52574B06219FFF6FB43C7FDB705667454937F48E165F650E621A76005E791CA2673B43AFF23ED89D77G7D2E399A4A9B1D7ADD9EB9A40F3FB33C9F17EF3DEC4EFD85F3FCB29C62294E707E64D7F0A63275ABB8930923BEFDAB2CE70A3E4E7FC1F116CF2D1642CF852E2DD968C3090EFB36D974356B385DAD22EF1F0E8B36083E6A27A2DE5FBB4A6FA9516FCCACC7B9B5AAF2144550AE9EE09BC0AAG1E3330BBD0F1FE51A63F8BE881E881F083AC
			6C92E3BF9E90F6265E2B22C960F95B4F297E46C33BD5FA5BC71BE66675CFD15C2FGB6696DC873743C7E1BD8A4DB3B303D5CBC5BF7DF1E67D3D93D88D0F69162A2BA46B31921B16EEC19ED8C66077AC64539FB8D0C21DD94313F644618FBBE3FA3F21EB77EF6A8FED4441D8997C55C79D3BA57D6A8278264F70BF86F893FE8DB56AD6ED4A57B436FD4ADC9B35E09BA90883FD3057DC3755D0170BBD5537B7FC5A07CCE15684F6478935CAEBC7F304858D624C065D4814AA75C0AA673B318A61AA7FA8F729CEE7D95
			63FD2C224359916FE325DCB9EEF2F5739C2EBB1443ED57F338DDADF4DC7FC13EF787C7670ACD56F4CF77732C1FB6E73F5F0715D93F87676C978EAC66E975537C22BBBC96F2D07C566E70D8A8F1D444C2C9D0673CA47C40137E794E4FF205DF1BD16FD743B81BGD2G72BE033C9E2066B3917F7F4EDCC222477FAC418D057F9FD0DDD83B8B57D171288AFEF5243F2851E55501A6013FDFB23C7F7B682D37692F5F52B978F784F374B8B1ADE76EB8B1B7A7F21C1817B3B3CEFCB7A7F21C38A74798A7325F2C5E5B7F
			C3D01C0417C926C60AED42A0959DA922F77E0B3C1EA2BCA8AA42BFE61EB47281DE13A98A3595EDBAE51F972C1B1F3C2DEFF59B0D6D360CD7D6273FF0EF3A09E458C8966341C6B210909B4924F45AC8BA7090444B2EF89E04BFE1C5DD498D0C124FE9A907E39132F5ECB62F217EEDDADBF1B3A3DBA399C81DC632796996907856F4DB1436E277EDF9F7D60E19083909E48DCA2A4FAD81E17422391D3C59CFA9446F632AC6726C5877A874BDCA7BDA6D44B49050481E90FEG712C56611386E4E81263127C576888C8
			E36EE512B3689F6A2793E844F43969B2CCCBCBD8ED3113DC4DCD3F4B03CEE8E4F7E8688E5CCC3C42CA25C55E00BF9F7DA3628C6C509746FB27A9376F09FACFF7E38F3F1F5B93FEDF7F3CCC6F35DF01BDFFC56C793FF59D093667EB7BB54708242A1A24DE2B753870CEE45D24A6073E17F54AB8FEC03461157CB78A743E6C30214F7F83D0CB8788F7470557F195GG2CBDGGD0CB818294G94G88G88G72F1D4B0F7470557F195GG2CBDGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2
			A0E4E1F4E1D0CB8586GGGG81G81GBAGGG2B95GGGG
		**end of data**/
	}

	/**
	 * Return the ButtonPanel1 property value.
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
				ivjButtonPanel = new ButtonPanel();
				ivjButtonPanel.setName("ButtonPanel");
				ivjButtonPanel.setLayout(new java.awt.FlowLayout());
				//ivjButtonPanel.setNextFocusableComponent(
				ivjButtonPanel.setBounds(183, 276, 238, 49);
				//	getchkViewInvOnHldRep());
				// user code begin {1}
				ivjButtonPanel.setAsDefault(this);
				// defect 7890
				//ivjButtonPanel.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel.getBtnCancel().addKeyListener(this);
				//ivjButtonPanel.getBtnHelp().addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the chkViewInventoryOnHoldReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkViewInvOnHldRep()
	{
		if (ivjchkViewInvOnHldRep == null)
		{
			try
			{
				ivjchkViewInvOnHldRep = new JCheckBox();
				ivjchkViewInvOnHldRep.setBounds(425, 285, 199, 22);
				ivjchkViewInvOnHldRep.setName("chkViewInvOnHldRep");
				ivjchkViewInvOnHldRep.setMnemonic(java.awt.event.KeyEvent.VK_V);
				ivjchkViewInvOnHldRep.setText(
					InventoryConstant.TXT_VIEW_INV_HOLD_RPT);
				ivjchkViewInvOnHldRep.setSelected(false);
				ivjchkViewInvOnHldRep.setVerticalAlignment(
					javax.swing.SwingConstants.CENTER);
				//ivjchkViewInvOnHldRep.setNextFocusableComponent(
				//	gettblItmOnHld());
				ivjchkViewInvOnHldRep.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				ivjchkViewInvOnHldRep.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkViewInvOnHldRep;
	}

	/**
	 * Return the JDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJDialogContentPane()
	{
		if (ivjJDialogContentPane == null)
		{
			try
			{
				ivjJDialogContentPane = new JPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);

				ivjJDialogContentPane.add(getJScrollPane(), null);
				ivjJDialogContentPane.add(getJPanel1(), null);
				ivjJDialogContentPane.add(getButtonPanel(), null);
				ivjJDialogContentPane.add(
					getchkViewInvOnHldRep(),
					null);
				getbtnHold().addActionListener(this);
				getbtnRelease().addActionListener(this);
				getButtonPanel().addActionListener(this);

				getbtnHold().addKeyListener(this);
				getbtnRelease().addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjJDialogContentPane;
	}

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
				ivjJPanel1.setLayout(new java.awt.FlowLayout());
				getJPanel1().add(getbtnHold(), getbtnHold().getName());
				getJPanel1().add(
					getbtnRelease(),
					getbtnRelease().getName());
				// user code begin {1}
				ivjJPanel1.setBounds(11, 276, 169, 49);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
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
	private JScrollPane getJScrollPane()
	{
		if (ivjJScrollPane == null)
		{
			try
			{
				ivjJScrollPane = new JScrollPane();
				ivjJScrollPane.setName("JScrollPane");
				ivjJScrollPane.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				//ivjJScrollPane.setNextFocusableComponent(getbtnHold());
				getJScrollPane().setViewportView(gettblItmOnHld());
				// user code begin {1}
				ivjJScrollPane.setBounds(9, 12, 613, 250);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjJScrollPane;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblItmOnHld()
	{
		if (ivjtblItmOnHld == null)
		{
			try
			{
				ivjtblItmOnHld = new RTSTable();
				ivjtblItmOnHld.setName("tblItmOnHld");
				getJScrollPane().setColumnHeaderView(
					ivjtblItmOnHld.getTableHeader());
				getJScrollPane().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblItmOnHld.setModel(new TMINV017());
				ivjtblItmOnHld.setBounds(0, 0, 200, 200);
				//ivjtblItmOnHld.setNextFocusableComponent(getbtnHold());
				ivjtblItmOnHld.setGridColor(java.awt.Color.white);
				// user code begin {1}
				caTableModel = (TMINV017) ivjtblItmOnHld.getModel();

				TableColumn laTCa =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(0));
				laTCa.setPreferredWidth(90);
				// defect 8269
				//TableColumn laTCb =
				//	ivjtblItmOnHld.getColumn(
				//		ivjtblItmOnHld.getColumnName(1));
				//laTCb.setPreferredWidth(70);

				TableColumn laTCb =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(1));
				laTCb.setPreferredWidth(200);

				TableColumn laTCc =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(2));
				laTCc.setPreferredWidth(50);

				TableColumn laTCd =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(3));
				laTCd.setPreferredWidth(60);

				TableColumn laTCe =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(4));
				laTCe.setPreferredWidth(90);

				TableColumn laTCf =
					ivjtblItmOnHld.getColumn(
						ivjtblItmOnHld.getColumnName(5));
				laTCf.setPreferredWidth(90);
				// end defect 8269
				ivjtblItmOnHld.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblItmOnHld.init();

				laTCa.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.LEFT));
				// defect 8269
				//laTCb.setCellRenderer(
				//	ivjtblItmOnHld.setColumnAlignment(RTSTable.LEFT));
				// end defect 8269
				laTCb.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.LEFT));
				laTCc.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.RIGHT));
				laTCd.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.RIGHT));
				laTCe.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.RIGHT));
				laTCf.setCellRenderer(
					ivjtblItmOnHld.setColumnAlignment(RTSTable.RIGHT));
				ivjtblItmOnHld.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblItmOnHld;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		///* Uncomment the following lines to print uncaught exceptions 
		// * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
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
			setName(ScreenConstant.INV017_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(638, 360);
			setModal(true);
			setTitle(ScreenConstant.INV017_FRAME_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (java.lang.Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Method to see if the cancel button has been disabled.  Used on
	 * the vc to determine whether or not to perform the cancel code
	 * when the esc key is pressed.
	 * 
	 * @return boolean
	 */
	public boolean isCancelEnabled()
	{
		// defect 7890
		return !cbDisableCancel;
		//if (!cbDisableCancel)
		//{
		//	return true;
		//}
		//else
		//{
		//	return false;
		//}
		// end defect 7890
	}

	/**
	 * Allows for keyboard navigation of the button panel using the 
	 * arrow keys.
	 *
	 * @param aaKE KeyEvent  
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		//to Indicate that key is pressed.
		cbKeyPressed = true;

		super.keyPressed(aaKE);

		if (aaKE.getSource() instanceof RTSButton)
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				// defect 7890
				//if (getButtonPanel().getBtnEnter().hasFocus())
				//{
				//	if (getButtonPanel().getBtnCancel().isEnabled())
				//	{
				//		getButtonPanel().getBtnCancel().requestFocus();
				//	}
				//	else
				//	{
				//		getButtonPanel().getBtnHelp().requestFocus();
				//	}
				//}
				//else if (getButtonPanel().getBtnCancel().hasFocus())
				//{
				//	getButtonPanel().getBtnHelp().requestFocus();
				//}
				//else if (getButtonPanel().getBtnHelp().hasFocus())
				//{
				//	getButtonPanel().getBtnEnter().requestFocus();
				//}
				// end defect 7890
				if (getbtnHold().hasFocus())
				{
					if (getbtnRelease().isEnabled())
					{
						getbtnRelease().requestFocus();
					}
				}
				else if (getbtnRelease().hasFocus())
				{
					getbtnHold().requestFocus();
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				// defect 7890
				//if (getButtonPanel().getBtnCancel().hasFocus())
				//{
				//	getButtonPanel().getBtnEnter().requestFocus();
				//}
				//else if (getButtonPanel().getBtnHelp().hasFocus())
				//{
				//	if (getButtonPanel().getBtnCancel().isEnabled())
				//	{
				//		getButtonPanel().getBtnCancel().requestFocus();
				//	}
				//	else
				//	{
				//		getButtonPanel().getBtnEnter().requestFocus();
				//	}
				//}
				//else if (getButtonPanel().getBtnEnter().hasFocus())
				//{
				//	getButtonPanel().getBtnHelp().requestFocus();
				//}
				// end defect 7890
				if (getbtnHold().hasFocus())
				{
					if (getbtnRelease().isEnabled())
					{
						getbtnRelease().requestFocus();
					}
				}
				else if (getbtnRelease().hasFocus())
				{
					getbtnHold().requestFocus();
				}
				aaKE.consume();
			}
		}
	}

	/**
	 * Handle Key Release Events.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{

		// Excecute this function only if any key is pressed. This 
		// will avoid passing of events from desktop.
		try
		{
			if (cbKeyPressed)
			{
				super.keyReleased(aaKE);
			}
		}
		finally
		{
			cbKeyPressed = false;
		}
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmItemsOnHoldINV017 laFrmItemsOnHoldINV017;
			laFrmItemsOnHoldINV017 = new FrmItemsOnHoldINV017();
			laFrmItemsOnHoldINV017.setModal(true);
			laFrmItemsOnHoldINV017
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmItemsOnHoldINV017.show();
			java.awt.Insets insets = laFrmItemsOnHoldINV017.getInsets();
			laFrmItemsOnHoldINV017.setSize(
				laFrmItemsOnHoldINV017.getWidth()
					+ insets.left
					+ insets.right,
				laFrmItemsOnHoldINV017.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmItemsOnHoldINV017.setVisibleRTS(true);
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
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (cbInit)
		{
			cbInit = false;
			cvIAVector = (Vector) aaData;
			caTableModel.add(cvIAVector);
			// defect 8494
			if (gettblItmOnHld().getRowCount() > CommonConstant.ELEMENT_0)
			{
				getbtnRelease().setEnabled(true);
				gettblItmOnHld().setRowSelectionInterval(
					CommonConstant.ELEMENT_0,
					CommonConstant.ELEMENT_0);
			}
			else
			{
				getbtnRelease().setEnabled(false);
				setDefaultFocusField(getbtnHold());
			}
			// end defect 8494
		}
		else
		{
			if (aaData != null)
			{
				Vector lvDataIn = (Vector) aaData;
				for (int i = 0; i < lvDataIn.size(); i++)
				{
					cvIAVector.add(
						(InventoryAllocationData) lvDataIn.get(i));
				}
				caTableModel.add(cvIAVector);
				if (gettblItmOnHld().getRowCount()
					> CommonConstant.ELEMENT_0)
				{
					getbtnRelease().setEnabled(true);
					gettblItmOnHld().setRowSelectionInterval(
						CommonConstant.ELEMENT_0,
						CommonConstant.ELEMENT_0);
					gettblItmOnHld().requestFocus();
				}
				else
				{
					getbtnRelease().setEnabled(false);
					getbtnHold().requestFocus();
				}

				// If an item has been either placed on hold or 
				// released, disable the cancel button so the user 
				// can't cancel
				getchkViewInvOnHldRep().setSelected(true);
				getButtonPanel().getBtnCancel().setEnabled(false);
				cbDisableCancel = true;
			}
		}
	}

// defect 8494
//	/**
//	 * If there are invoice items returned from the mf set focus on 
//	 * the table.  If not put focus on the add button.
//	 * 
//	 * @param aaWE WindowEvent
//	 */
//	public void windowOpened(java.awt.event.WindowEvent aaWE)
//	{
//		super.windowOpened(aaWE);
//
//		if (gettblItmOnHld().getRowCount() > CommonConstant.ELEMENT_0)
//		{
//			getbtnRelease().setEnabled(true);
//			gettblItmOnHld().setRowSelectionInterval(
//				CommonConstant.ELEMENT_0,
//				CommonConstant.ELEMENT_0);
//			gettblItmOnHld().requestFocus();
//		}
//		else
//		{
//			getbtnRelease().setEnabled(false);
//			getbtnHold().requestFocus();
//		}
//	}
// end defect 8494
}
