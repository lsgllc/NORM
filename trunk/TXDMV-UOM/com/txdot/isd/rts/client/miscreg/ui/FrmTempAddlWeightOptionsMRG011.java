package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.AddlWtTableData;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.data.PeriodOpt;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmTempAddlWeightOptionsMRG011.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 * 							doneWorking()   
 * B Hargrove	03/18/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  						Comment out setNextFocusableComponent()
 * 							Remove unused variables.
 * 							Remove keyPressed()  (arrow key handling is
 * 							done in ButtonPanel.
 * 							modify getButtonPanel1(),gettblAddtWt()
 * 							delete implements KeyListener 
 *							defect 7893 Ver 5.2.3
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							add import com.txdot.isd.rts.services.util.
 * 								UtilityMethods;
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	07/14/2005	Refactor\Move 
 * 							MiscellaneousRegClientUtilityMethods class  
 *							to com.txdot.isd.rts.client.miscreg.business.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Temporary Additional Weight duration options.
 *
 * @version	5.2.3			08/12/2005
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class FrmTempAddlWeightOptionsMRG011
	extends RTSDialogBox
	implements ActionListener, FocusListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjlblPermitEffDate = null;
	private JLabel ivjstcLblPermitEffDate = null;
	private JLabel ivjstcLblSelectOne = null;
	private JPanel ivjFrmTempAddlWeightOptionsMRG011ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblAddtWt = null;
	private TMMRG011 tableModel;
	private Vector cvTableData;
	private CompleteTransactionData caCompTransData = null;
	private java.util.Vector cvPeriodOptions = null;
	
	private final static String EFF_DATE = "Eff Date";
	private final static String PERMIT_EFF_DATE = "Permit Eff Date:";
	private final static String TITLE_MRG011 = 
		"Temp Addl Weight Options          MRG011";
	private final static String SELECT_ONE = "Select one:";	
	

	/**
	 * FrmTempAddlWeightOptionsMRG011 constructor comment.
	 */
	public FrmTempAddlWeightOptionsMRG011()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmTempAddlWeightOptionsMRG011 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmTempAddlWeightOptionsMRG011(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * FrmTempAddlWeightOptionsMRG011 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTempAddlWeightOptionsMRG011(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		// Determines what actions to take when Enter, Cancel, or Help
		// are pressed.
		// Code to avoid actionPerformed being executed more than once
		// when clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{ //field validation
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				int liRowCnt = ivjtblAddtWt.getRowCount();
				for (int i = 0; i < liRowCnt; i++)
				{
					if (getAddtWt().isRowSelected(i) == true)
					{
						copyElementForPmt004(i);
						break;
					}
				}

				getController().processData(
					AbstractViewController.ENTER,
					caCompTransData);
			}

			else if (aaAE.getSource() == 
				getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caCompTransData);
			}

			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.MRG011);
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.MRG011A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.MRG011B);
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Populate table with period options data.
	 */
	private void convertAddlWtToTable()
	{
		if (cvTableData == null)
		{
			cvTableData = new Vector();
		}
		cvTableData.removeAllElements();
		for (int i = 0; i < cvPeriodOptions.size(); i++)
		{
			PeriodOpt laTempData = (PeriodOpt) cvPeriodOptions.get(i);
			if (laTempData.getItmPrice() != null)
			{
				AddlWtTableData laAddlWtData = new AddlWtTableData();
				laAddlWtData.setPeriod(laTempData.
					getPrmtValdityPeriod());
				laAddlWtData.setExpirationDate(laTempData.
					getPrmtExpDate());
				laAddlWtData.setAmount(laTempData.getItmPrice());
				cvTableData.add(laAddlWtData);
			}
		}
	}
	
	/**
	 * Populate CompleteTransactionData with selected data in table.
	 * 
	 * @param aiElementNum int
	 */
	private void copyElementForPmt004(int aiElementNum)
	{
		// defect 7893
		// remove unused variable
		//Vector lvPeriodOpt = new Vector();
		// end defect 7893
		PeriodOpt laPerOpt = new PeriodOpt();
		laPerOpt.setPrmtValdityPeriod(
			((String) ivjtblAddtWt
				.getModel()
				.getValueAt(aiElementNum, 0))
				.trim());

		String lsExpDt =(String) ivjtblAddtWt.getModel().
			getValueAt(aiElementNum, 1);
		// convert string date to RTSDate type
		RTSDate laDate =
			new RTSDate(
				Integer.parseInt(
					lsExpDt.substring(6, lsExpDt.length()).trim()),
				Integer.parseInt(lsExpDt.substring(0, 2)),
				Integer.parseInt(lsExpDt.substring(3, 5)));

		laPerOpt.setPrmtExpDate(laDate);
		// defect 7893
		// remove unused variable
		//Dollar laFee = new Dollar((String) ivjtblAddtWt.getModel().
		//	getValueAt(aiElementNum,2));
		// end defect 7893
		for (int i = 0; i < ivjtblAddtWt.getRowCount(); i++)
		{
			if (ivjtblAddtWt.isRowSelected(i) == true)
			{
				laPerOpt = (PeriodOpt) cvPeriodOptions.elementAt(i);
				break;
			}

		}
		//TimedPermitData lTimedPrmtData = new TimedPermitData();
		caCompTransData.getTimedPermitData().setRTSDateExpDt(laDate);
		//caCompTransData.setTimedPermitData(lTimedPrmtData);
		FeesData lFeesData = new FeesData();
		lFeesData.setItemPrice(laPerOpt.getItmPrice());
		lFeesData.setAcctItmCd(laPerOpt.getItmCd());
		lFeesData.setDesc(laPerOpt.getItmDesc());
		lFeesData.setItmQty(1);
		Vector lvFeesData = new Vector();
		lvFeesData.add(lFeesData);
		caCompTransData.getRegFeesData().setVectFees(lvFeesData);

	}
	
	/**
	 * Focus Gained
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// empty code block
	}
	
	/**
	 * Focus Lost
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// empty code block
	}
	
	/**
	 * Return AddtWt property value.
	 * 
	 * @return RTSTable
	 */
	public RTSTable getAddtWt()
	{
		return ivjtblAddtWt;
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
			D0CB838494G88G88G5C0AE4ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BBEDF0D35715F417E458AD6D26895BE1D3F657191005CD1D066D3A9DA7CB925A1299521054691206EE482EB3218D133AAD1D70435B71CC2EE4812658310305AD9340029862AF94DB96E00CF17CA530C13605A5408132D8361E6D874F12101E31G87761CFB5FFD6FC9C8F6C8864F1CF96F1EFB4F3D671EFBBE6F130939BCE52341E8A6443011A8FF078D04B49EA6A432672587B9EEFDF90605687E76G
			FCC07E2E44048DEB0645FDB643D2C452B765C1538C6DB95E8CCBA93C9713E7B60DB99019F81DD66833BF7904FD7AF5DE594E5759CC5B511EFCF83E8FE081DCB32EE745759F6831A878C1E83F2F3CA3980C04B8766018174AACBA7CCAE51CF58FFBAE23E33E5F33D3691F02B60E19874F45FB34B937D06E6EEBEAD45A973F73BA597073854682DCCBB7BF073C3D0C36D6F9DAC905918511CB9E3C59854FE677155116616038CD0E7A0332CD0C78EDE1B9E29B16C4A1A05B464548F0D89835CD08CDCD474363DEE1BC
			546677CB6784F1F4CC6E8B49E2B0906969BFD1D7DF3FE0AF727FEED99E1D1B90BF34992F2F94B4AB3C4C3B72BA71BFDC39B9992F198A0F260FB4DEDB7F1CE5CA7B8867DEC5E50FF41D501E82E0EB6E6C65EBE683AEDA0138CDE5B8368E5E3BG86ABB8AF47BE663C8C5CFC07E48FBDE7ACCC424B528A46435A8A0D1779EF3FECAA23FA70F9A91E25F5DF062593E0GE08AE08140D2008F297ED9CA2A6159B6A4F7844747030136D028DD7207FB02FEC1027726A668D1F07D42F0B06CA70469D8F6D9ED82FEB82C5A
			972BDF1ACE1CA3DCA7D27B5FA459372AB6EFCC30373AEBD51B57A9F3646C53764676753F965C574E7D9916C6GB74098409C00457B793E06CA9AE65AD7F7C09642811F6419988A7942F27B04A8790530323F7F6A19EEFF0900512D3BEBBAA617458DFA392C7977E5464D896432E671E346657B19BC566C576442565E531BEC6D1C7DF71E87257D987570865543A2F8B7A8BA4A7033A95E2F43B39D4F27F8EC4B50377AE32E37836F73BD38CC2F12227A1FA45C437C1F7E5858DC497871D6461FEDF08AFD4BAC40AF
			GC883D889108510D715E159D9454F770912CF3E245E3AF5E74BE8C7CACB60A9C465302FBB9011FD01E1612CAFAC7A06A4A1C214B141D264FDA806CE2676B48CEB1C9082C258070E85E6770D8B20C891B6660B528FF0CCD8203DDEF1DC008546C3044FFBDFE9B1BCA5DFC4BE93727BE4C19B417A3FD50A323C66BB8BB34284B8368AE5DE38837DC7B33C8FGB0D9AD7882F1CB203DC215CB1EE59BBCCF01E28BD2D3D327B80CEC784257797A46B274A542B5701B1DC2C8887861A52274EDA95B2249E5A42872DCC8
			71E8A39D40F8C445EFAF7B0061E18E3D14F97FAE5A6F136530B8B4A18B9EC116454028C67F5C8D64A592E3FE9142767650AD5431B5302F8DG26EA3E3F2E1B453A7D390241AB93A1AF1E1CA227C66AE7ADB03E2E1A4743D9E5989F3D55DCCF7FB040753433A013543E7ADCC2BD1540874D2AE67AB93FBA5E567E164E292D312DEC60AE6DBE9E3C5509EDD473ADB5DFFB0D5CE97A185CB791147BB260E58DC00EAA773F3EE9E576566E0B885C427A42F0E202DF53B2B6767AD4F29D1BB515D8476E1FCA2CE3F32726
			5721F9D309F40875FDEF8AE3CBF0B22053491429ED5406838101B22D129016EF7C410B32908322FCCE740F8A32C73CA1005D5520EE6D27B6E806774A9A2ECB139FF1DD2A383106AC3B7430B1D1BC1B7F58D346D9B56CFC6755E83A44F8FA648B1439A08963A0280E60C4C0E678C5543E953C62DF7E61D662317FFC8B75710AF0DD9977682DCD847D08B4A128FBDB625B0AB28E0B63F0D4E830CA4E57572B4F795200BF4C8F336119DF1BE1E1344F1C598D4F6933AB2EC31F9A10478A20ED84F057F29DAA3FDD2633
			DDC5F9BA0212440EC2BD23FCD204E7AADC1D9040B523F11FD277D5CC3645751DD576487ABFA438C7C9679746EAF10F47F6707C8B638643F9A96EB1B02E4FCDBC8E779469F3D6FF6407894F134669DAA5A72E0D0FE13291674D847CFAF56F5FA5FA3F5596098407C59AA2143359E4BAC4E64AF3C7E95411E81E5B6F75F40ABEA9B85A9E0C9242F24C27286F4379CC87F80E39520DE7518CEDAF00F4007363B45C29DBA78DFF016708980EC0EE649722DC26EE834A86E3160266391CDDAF2B2EE862DC8EE17681450F
			9E081755533948C79A60D7283C3DE878C0A7AB48BBA1C40F42C9F2FE2E9BF3192EAB6DAF6899D1DDA75CD7BE1BB49722395301D8DF3B9E5AAC67E9BD0D72B25962F3211394EF364567C23FF473DC28827A6658F8ECDEE242FDE5C1BB47467735AD37C037AFF7D08C486A1E1E4BC5FB8D0645D1B140F14BA98E32EE6A40D8FCFB151EF17EA799168AG06733A9157856D0B1F70F8F47CB47A03B1356D266D94353D5A7DF5E3C9B250567C53BDDBA3D7155B0FCCF89ED1357D94EDDFA75C677D5BA47A0391C852A0EC
			F8C242B09928C319E529BEEFB9703B9AA09B4082FD0DG39F42EABEE1CC396E52AFBA9F5D8FBBF0D539156CF8856CE537B440EA0D499189E8504FADEE3FF63345E5F2E2DE339CADEBD6437753CDE5C561727581283F0D2BD554DBEF31C8C8A21EE3741137507E37A51062A083EFFE1CCBF5A70ABB1748E373EFF230E1E597B0B7BF8DD1A7362DBC47A518F125673E9754C3ED6AB4F1462678DA6FB06094D730B8BFA3FD17470F70D06A4739459997DCEFB7C3D403BD5A8C36AAFAF7B2E79223648A49CF0D353C914
			3424784353F98A6FEEBB77077E863457B950CE81C837F3DDAD6D7BEAF55BCFB6F31924BD7D86C93FBFE05C1AE4AF994A9E56AB4F46ED4F9B343CEB2800566A8D9916E600C100A840DC00058D5CFF3C3795F9043C4B699765B6D9768D0FD16F4D53313322B02921D159595C7761B20AFDB8737741732C6F32D29D61D441B01F61A40832EF68B2F8DC2FEF14283E74F43CEC529BB47F39925EB381B668F04CE72E26324EF3B01F39D5457F1662ABF5F866CBAFD3BC363B20AF5541FD6903052817C587B3AC35GEEG
			BFC074A0174B1C7C3DCCAE89E46204A48EEC902BBC194B60CAC1727CB4DCB0F3EDF833G6364D0D0160363B8BF47FF23G65A587C3942964358528A7E1748BECA84FEB8BD0B7A5E10422955FCE75F59E6CEB71C16E5B8BAEA2EEA534572AB8F285F15950AEBA44F175F6B5AE9C62BA7D5F8DA837AEE87B81E68324G242B7DD934BF835A6B810A8E039F82709E667D2106C47E9B67094725AB382E38BE3F32E6A27BE1EB7CCD42B5B6A740EDA51A5F5363B7AA3618E88DA66B078B131F77A305B31F776305B4D702
			F372DE8F898C77E4A10D57A3A391C1552B3442A295579DF187A3A2868296AB32CE63F849F7BDB88163B38F537B3F73BC8E48501E8B10863082G63B4232B38087B9E77C5457109F1DA0C049A1136196AC5A53CBB9A795852ABF4AC94AF31E3BF217A12866F4BGD87C6B32733533GC70EC07E83508AB0F8044F877185A0A28F3B06A40F00D9FADFC0D0E3DF315A67964263226C9C99697449DABF4AC20A6BE4FDAF9DC3BBBF69998EC36ECFA3A327C1FC7B8458D5003A717A74A3486B9E07BEAE4DBA426433901E
			2C3FF4B0D1BFE9627DDBAEA46AB7C3BF0B4705D7757138320975FB61B9264EE1B4444C210CD98ECF663BEC67793D6FEA40959D65F5D37DA05EE729F14A661B141B1ACE04C5FF3BEF54C53501452AEB67F93C6FBA4AE355C2BA67ACE84F87D8F214571C517DBC66247E4B2BA434A071DDF526F711F145D1E6FBD9C73578197A764BA6164BAC4094C1B92FBFDD46B89F90A62D1CCDDB27881E94AF21AA6D750E430E4606C30E4335D66D3D0A474014C9FDBCEF7CCBEA521A29321971512CBC731D4F9B98AF1B0AB5DE
			765B2D49F9F1B49E84A69A8ED6B139856CDCEEF2B34F1396586373A46C57721C176D71F9D2ECBFE9084F13E27B6D8D71F9926FD7F21311A439094BB744F31381874FCDD21B7939FEF1DC7F8DE2E96122243944DAC5FE26E35A39DE04F3E5F3778E7239BB0F71FC32758247CD9D633AB96A5C1518CF4DB2892FADCFDCC5FA744BE94738FE7E638E544FACE81BDAA0F7ED617336549CA4F7D34B6AACDEDD6F08012F67E8617148F17E2B5D4937FFC53A1BF7CD47721B17AF20DC2496165F98D47CEB949F5561196FF8
			DC759D3321EFDD8B4FFB8E1AD10FDC1F029F821885B09FA09DE05527BC7769AE2AA3B35C03FB03A19784BFC9F7C54B667FECBB4AF89D4CE5EA65733D79C18DC9129F82529D8E060430FC1DE1694D0BA22B087D2B4978F3C7A23A0AD6A5C706278BE030D54BA1986F5FABC19B14817FG40E200E5G196ABEFA0BE77C7E630CCE737D47B31808AF524678B04333922039CD630B51B9AFA422F3AB637C700C824C3D034EF5155325C0DFDADBEC4E34226D4E9CCA5129DD741E3CAD7E3E62CD0A4FEA0B3F2FF8F4873F
			2F48013E41F6EEDBAD06694EEFB90CDB8D10559E4F772A8BC4FDFFC3773E647C3D2A7D3535DF3827EBB0BDEAB1A74F9D1D6644F759274C096F32CF1B273F4B7E4C1C7CAEFB50FC67DD767FEDFFDFBF9FB3C725CF580E7601775822CF92EF502BC68F8C8C0C32FAC1267107F440FB872FA1369C6435C6050AFBBF42EB0DC695773907579A2E8EEEEF0F16A4175363A509653424A4311CFEDCB27DF725E7CB12491171F7F9106FED014AF34EA05F5B9295B739116FED390ACB8F733D2DD6F197AF7079AABBB9AE6B82
			1F2FD945C5EE72795CAAEE55F9BE1FDF458D53D81A866D55AAEEA945E5C15B6C6438D21517CAA73753B7F661BDDED4ED3FCD5B8F286DFF5DB113CDA06D57333B1F4E3846F45F19E6823677B3863E77B4D59EBF5C4D65EBBA4EF1BF30506FFD50EEBE4E6B16EFBAD0A6B2A89C2BFF9E213A6E027E81G0633D3DD1721BDCB459D263A1E8A6DF4G5389EAA71E4EE9F865F1BCD70135C00730DD41E743BB4FAF599A8FD1BDF4415C83A7783A7BFC080BC2FB360ACB213A39845ACBCFF0DEFE911E09170D5370328E66
			A8086165BE2A439BGCF3A783A5BE9DDE901F6050ADBC0755A8D6D0BDD1C175F1D1F0E175C99F81983739C51492E185C380A3F073A864905B88CA119A6206D3E30BBA809435795FE7F1E5A885EC94E6B62E77D1FD7B19F988B06459B4100FCA7A55F6B9FA96DB100D56A9EECB34AD33F8FCBF70625361B6D815B2D70DEE584FF2D6EA1EAC7FFA68FC9EDFE3FFC8E6F76D9FDE9592D2FAFDD5D6C1EE5AEBCD33AB9AF12E1A6DEB66BF8C9873A15BADEB2619D6F7576287BFD1D5ACA9660333B392C8C846FBA82E097
			47034393913C63006CA8948C88F8B756FA12669396341D8AF8E7F559DF515F1BB1FA63C1541F3D6CAA408AE4CDF86F4646BDD6260D4B0D6CA6C96B37C38D3556FAC79527DB31C3236B77FC993AF2426E8A28CFD67E58DC84CBED25A6FDB31F57D2BB3F7FAB5279DA407891C972ED6AA473072BCEEA35541C6A8C25C67E63AE0D3F8D03786DF1AF3B5130BAC7C62CF82701C2E0E3AB4B35315246E976525898A303D3D60D6E097017278B6C51767E1D3D5A9C3F3D007713FBB903AAFFE99FE9E346EECE330E439133
			4EEB959A5D8B673FBCDD4BFE0DAE75AA7E4CD1D7E65AF4D5266D2C335F5B5D51663A6469686FF3395A5B7AAF35F9AE75BA1D1D4ECEC63FA9FCE89A7A2E3E7E6E73FD3D5E44B3A45219751FA642B13EFD9F93555696CD600FAF6F6A3BAC7B6C2C5431EEEA07189B651F62F5F39F3D678AC1FB8EC08A405D71B73BD2BB77C5D51A0C9F2573A43D43B9DCEBEDB8D8250C7DBD5575D1C81D383B2AD46FF30E897A7B1C3A1B570C7C8EA55EF652CFB11BD9F1CA3B4FE175596AEA74E75900B73BA0F784F083788126G9E
			GD888108E1081308EA087202887628E0083208B60E20F7A3DFAA7EEB4D9DD2ADC28510B7E300FFDD7E5394A195D182B04E01E59BD09731E7900CFCD520730CC69DB854FF5F163D82D75B3FA1EB9BD71757ECA0A4F6B0D2F774F90DE6F1B21AF5A4B6B7D16DA743DEBFA791E5F295ABB4D1EBB2016E599BD4F0B332A1367606FD64FFC4F1DD31D783B0631664E6F9A1BEB92FF57A82851FF57D03EFB5720AD0D42998D2863CAD55CE7AAEEA71D93F167C43FBC266277567CC541F7518F2C9CDFC9791A24388185F7
			C0077BCC4159EB706E9D368CE98227BDDC03329E91A509350F52B1E2A0A2482ABCDAA90FE24827635B296210EF250E20B63E9E4EAD3F0F6725ABA94E8AED070ABBE5C55CG3423AAEEBE454D05F62A0A7B8D45AD03769A957742AD1E8F2FD7F16F5C66392F594D737F9728853377DF6A5EAF10FBDF0F4B77F48DA67F76DABC270822774C17FDFBB7767BA664E03F9091EF88F84B437320C53B79BDE2051B67C1EF6D46B39C473BA0423F457CD94D51AE42B899A08DE085G56D60CEE480A39FCE4ACB8F9D649D3DD
			E2G4DC7D9EB4A4A6F22B355351E3496ABB4DDEA0E4A28984D8BAA4D8615A647DAC4F5D196424302A4F9C23EE1582D121F6F4171F927614CCF73EFB91937B86F4D00F38304G9EGD88240723A1D8475BD63B4DF63595D785B0518F8CA63162D6D0C376FD23F5353FD5EF949556671DE6A68F31D6969653EEBEF39FAFF84F3B97A791DEF0FF566B8297F1D3E37077FE60C45494B65DC8ED2BF0F137369DA8B213D94E0D5BF677DA1AB2EB96213A28261BA109903E331F161812276BFF78B1343A431312E56DA27FC
			CF58F41B7311554F3FA7B8EE47FFCF40EF63B6423F870C5C0E7F1EB09C53BF7FF67C770436185FDD7C7CF6326F890CF7BB4DDFA0D66BBF0DD5D65990A3871C28A7903C198DD6164FF48FDB61090DE10C6E500CF43B935AFCF0C63AF8E031B15F0AE75B6A090F195BA95E65090F197FD44EE366G74AD75F0FF3775765D2E3F0B4AF5D9F18135B189EC31037D22DB5D53B6ABD15FBF543DAFAC3F77BE7419FB3A867B1F237FB17F8788B27577E423CCAA7137CBD67C7112157DFA492A7CFC492A7EB1C30D044936BB
			CD276D5B3FFEAD75D22759C80C5DE453981D211BEC1C44789D8D5DFE6F3D7790A2D749ADFAF63F03A37C0DCC366ACAC8358F797BB42E95A6056A1A5DB4B6E7DEEBE84AFD6B51DCA3B14102ACD769A6A6CCB8E05DB0DBBF97F215A87E669EED1966D038EF19944517EEB14B8DA4E8EDD6A6799CG9D28C9A6F91CBAE6F8ED0296A7B67189D4AC5AF53982F9AA96A9DBF05FGDB547930E87B355AE2FFACCF2F3A2D4F53336F7BD6526F2E734EB21BDFFCB67E77177717A207EB81CF4E713C73D7A568B8137DC648B3
			6E13A408E2D75A82FE7C3DD07BF5D9506EEE3FD90A73C77898251375BDC87F3761320E667F81D0CB878830D4B559CC94GGE0B7GGD0CB818294G94G88G88G5C0AE4AC30D4B559CC94GGE0B7GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG0694GGGG
		**end of data**/
	}
	
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 =
					new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// defect 7893
				//ivjButtonPanel1.setNextFocusableComponent(
				//	gettblAddtWt());
				// end defect 7893
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @return CompleteTransactionData
	 */
	public CompleteTransactionData getCompTransData()
	{
		return caCompTransData;
	}
	
	/**
	 * Return the FrmTempAddlWeightOptionsMRG011ContentPane1 
	 * property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel 
		getFrmTempAddlWeightOptionsMRG011ContentPane1()
	{
		if (ivjFrmTempAddlWeightOptionsMRG011ContentPane1 == null)
		{
			try
			{
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1.setName(
					"FrmTempAddlWeightOptionsMRG011ContentPane1");
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(485, 235));
				ivjFrmTempAddlWeightOptionsMRG011ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintsstcLblSelectOne =
					new java.awt.GridBagConstraints();
				constraintsstcLblSelectOne.gridx = 0;
				constraintsstcLblSelectOne.gridy = 0;
				constraintsstcLblSelectOne.ipadx = 8;
				constraintsstcLblSelectOne.insets =
					new java.awt.Insets(19, 34, 8, 86);
				getFrmTempAddlWeightOptionsMRG011ContentPane1().add(
					getstcLblSelectOne(),
					constraintsstcLblSelectOne);

				java
					.awt
					.GridBagConstraints constraintsstcLblPermitEffDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblPermitEffDate.gridx = 0;
				constraintsstcLblPermitEffDate.gridy = 2;
				constraintsstcLblPermitEffDate.ipadx = 6;
				constraintsstcLblPermitEffDate.insets =
					new java.awt.Insets(4, 93, 8, 4);
				getFrmTempAddlWeightOptionsMRG011ContentPane1().add(
					getstcLblPermitEffDate(),
					constraintsstcLblPermitEffDate);

				java.awt.GridBagConstraints 
					constraintslblPermitEffDate =
					new java.awt.GridBagConstraints();
				constraintslblPermitEffDate.gridx = 1;
				constraintslblPermitEffDate.gridy = 2;
				constraintslblPermitEffDate.ipadx = 28;
				constraintslblPermitEffDate.insets =
					new java.awt.Insets(4, 4, 8, 93);
				getFrmTempAddlWeightOptionsMRG011ContentPane1().add(
					getlblPermitEffDate(),
					constraintslblPermitEffDate);

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 0;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.gridwidth = 2;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 276;
				constraintsJScrollPane1.ipady = 57;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(8, 31, 3, 31);
				getFrmTempAddlWeightOptionsMRG011ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 0;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.gridwidth = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 18;
				constraintsButtonPanel1.ipady = 16;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(9, 62, 14, 63);
				getFrmTempAddlWeightOptionsMRG011ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmTempAddlWeightOptionsMRG011ContentPane1;
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
				getJScrollPane1().setViewportView(gettblAddtWt());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJScrollPane1;
	}
	
	/**
	 * Return the lblPermitEffDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblPermitEffDate()
	{
		if (ivjlblPermitEffDate == null)
		{
			try
			{
				ivjlblPermitEffDate = new javax.swing.JLabel();
				ivjlblPermitEffDate.setName("lblPermitEffDate");
				ivjlblPermitEffDate.setText(EFF_DATE);
				ivjlblPermitEffDate.setMaximumSize(
					new java.awt.Dimension(44, 14));
				ivjlblPermitEffDate.setMinimumSize(
					new java.awt.Dimension(44, 14));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblPermitEffDate;
	}
	
	/**
	 * Return PeriodOptions vector.
	 * 
	 * @return java.util.Vector
	 */
	public java.util.Vector getPeriodOptions()
	{
		return cvPeriodOptions;
	}
	
	/**
	 * Return the stcLblPermitEffDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblPermitEffDate()
	{
		if (ivjstcLblPermitEffDate == null)
		{
			try
			{
				ivjstcLblPermitEffDate = new javax.swing.JLabel();
				ivjstcLblPermitEffDate.setName("stcLblPermitEffDate");
				ivjstcLblPermitEffDate.setText(PERMIT_EFF_DATE);
				ivjstcLblPermitEffDate.setMaximumSize(
					new java.awt.Dimension(88, 14));
				ivjstcLblPermitEffDate.setMinimumSize(
					new java.awt.Dimension(88, 14));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPermitEffDate;
	}
	
	/**
	 * Return the stcLblSelectOne property value.
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblSelectOne()
	{
		if (ivjstcLblSelectOne == null)
		{
			try
			{
				ivjstcLblSelectOne = new javax.swing.JLabel();
				ivjstcLblSelectOne.setName("stcLblSelectOne");
				ivjstcLblSelectOne.setText(SELECT_ONE);
				ivjstcLblSelectOne.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblSelectOne.setMinimumSize(
					new java.awt.Dimension(63, 14));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblSelectOne;
	}
	
	/**
	 * Return the tblAddtWt property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblAddtWt()
	{
		if (ivjtblAddtWt == null)
		{
			try
			{
				ivjtblAddtWt =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblAddtWt.setName("tblAddtWt");
				getJScrollPane1().setColumnHeaderView(
					ivjtblAddtWt.getTableHeader());
				// defect 7893
				//getJScrollPane1().getViewport().setBackingStoreEnabled(true);
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7893
				ivjtblAddtWt.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblAddtWt.setModel(
					new com.txdot.isd.rts.client.miscreg.ui.TMMRG011());
				ivjtblAddtWt.setShowVerticalLines(false);
				ivjtblAddtWt.setShowHorizontalLines(false);
				ivjtblAddtWt.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblAddtWt.setBounds(0, 49, 288, 51);
				// defect 7893
				//ivjtblAddtWt.setNextFocusableComponent(
				//	getButtonPanel1().getBtnEnter());
				// end defect 7893
				// user code begin {1}
				tableModel = (TMMRG011) ivjtblAddtWt.getModel();
				TableColumn a =
					ivjtblAddtWt.getColumn(
						ivjtblAddtWt.getColumnName(0));
				a.setPreferredWidth(100);
				TableColumn b =
					ivjtblAddtWt.getColumn(
						ivjtblAddtWt.getColumnName(1));
				b.setPreferredWidth(100);
				TableColumn c =
					ivjtblAddtWt.getColumn(
						ivjtblAddtWt.getColumnName(2));
				c.setPreferredWidth(100);
				ivjtblAddtWt.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblAddtWt.init();
				a.setCellRenderer(
					ivjtblAddtWt.setColumnAlignment(RTSTable.LEFT));
				b.setCellRenderer(
					ivjtblAddtWt.setColumnAlignment(RTSTable.CENTER));
				c.setCellRenderer(
					ivjtblAddtWt.setColumnAlignment(RTSTable.RIGHT));
				ivjtblAddtWt
					.getSelectionModel()
					.addListSelectionListener(
					this);
				ivjtblAddtWt.addActionListener(this);

				// user code end
			}
			catch (java.lang.Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtblAddtWt;
	}
	
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
		// defect 7893
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7893
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
			setName("FrmTempAddlWeightOptionsMRG011");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(360, 263);
			setTitle(TITLE_MRG011);
			setContentPane(
				getFrmTempAddlWeightOptionsMRG011ContentPane1());
		}
		catch (java.lang.Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}
	
	// defect 7893
	// Arrow key handling is done in ButtonPanel.
	///**
	// * Handles the navigation between the buttons of the ButtonPanel
	// * @param aaKE java.awt.event.KeyEvent
	// */
	//public void keyPressed(KeyEvent aaKE)
	//{
	//
	//	if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
	//		|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
	//	{
	//		if (getButtonPanel1().getBtnEnter().hasFocus())
	//			getButtonPanel1().getBtnCancel().requestFocus();
	//		else if (getButtonPanel1().getBtnCancel().hasFocus())
	//			getButtonPanel1().getBtnHelp().requestFocus();
	//		else if (getButtonPanel1().getBtnHelp().hasFocus())
	//			getButtonPanel1().getBtnEnter().requestFocus();
	//	}
	//	else if (
	//		aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//			|| aaKE.getKeyCode() == KeyEvent.VK_UP)
	//	{
	//		if (getButtonPanel1().getBtnCancel().hasFocus())
	//			getButtonPanel1().getBtnEnter().requestFocus();
	//		else if (getButtonPanel1().getBtnHelp().hasFocus())
	//			getButtonPanel1().getBtnCancel().requestFocus();
	//		else if (getButtonPanel1().getBtnEnter().hasFocus())
	//			getButtonPanel1().getBtnHelp().requestFocus();
	//	}
	//	super.keyPressed(aaKE);
	//}
	// end defect 7893
	
	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmTempAddlWeightOptionsMRG011 
				laFrmTempAddlWeightOptionsMRG011;
			laFrmTempAddlWeightOptionsMRG011 =
				new FrmTempAddlWeightOptionsMRG011();
			laFrmTempAddlWeightOptionsMRG011.setModal(true);
			laFrmTempAddlWeightOptionsMRG011
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmTempAddlWeightOptionsMRG011.show();
			java.awt.Insets laInsets =
				laFrmTempAddlWeightOptionsMRG011.getInsets();
			laFrmTempAddlWeightOptionsMRG011.setSize(
				laFrmTempAddlWeightOptionsMRG011.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmTempAddlWeightOptionsMRG011.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			//defect 7893
			//aFrmTempAddlWeightOptionsMRG011.setVisible(true);
			laFrmTempAddlWeightOptionsMRG011.setVisibleRTS(true);
			//end defect 7893
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @param aaNewCompTransData CompleteTransactionData
	 */
	public void setCompTransData(CompleteTransactionData 
		aaNewCompTransData)
	{
		caCompTransData = aaNewCompTransData;
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
		if (aaDataObject instanceof CompleteTransactionData)
		{
			setCompTransData((CompleteTransactionData) aaDataObject);
			setPeriodOptions(caCompTransData.getPeriodOptVec());
			convertAddlWtToTable();
			tableModel.add(cvTableData);

			if (cvTableData.size() > 0)
			{
				gettblAddtWt().setRowSelectionInterval(0, 0);
			}
			getlblPermitEffDate().setText(
				caCompTransData
					.getTimedPermitData()
					.getRTSDateEffDt()
					.toString());
		}
		gettblAddtWt().requestFocus();
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @param aaNewPeriodOptions Vector
	 */
	public void setPeriodOptions(Vector aaNewPeriodOptions)
	{
		cvPeriodOptions = aaNewPeriodOptions;
	}
	
	/**
	 * AbstractValue Changed
	 * 
	 * @param aaLSE javax.swing.event.ListSelectionEvent
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent aaLSE)
	{
		//empty code block
	}
}
