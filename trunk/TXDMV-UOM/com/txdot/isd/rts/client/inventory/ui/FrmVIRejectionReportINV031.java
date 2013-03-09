package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmVIRejectionReportINV031.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * 
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV031 prompts for which date to run the online version of the
 * Virtual Inventory Rejection Report.
 *
 * @version	Special Plates		03/14/2006
 * @author	Min Wang
 * <br>Creation Date:	03/14/2007 09:44:44
 */

public class FrmVIRejectionReportINV031
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblEnterTheReportDate = null;
	private JPanel ivjFrmVIRejectionReportINV031ContentPane1 = null;
	private RTSDateField ivjtxtReportDate = null;
	private RTSDate caCurrDt = RTSDate.getCurrentDate();
	private static final int EARLIEST_DATE_SELECTED = 405;
	/**
	 * FrmVIRejectionReportINV031 constructor.
	 */
	public FrmVIRejectionReportINV031()
	{
		super();
		initialize();
	}

	/**
	 * FrmVIRejectionReportINV031 constructor.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmVIRejectionReportINV031(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmVIRejectionReportINV031 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmVIRejectionReportINV031(JFrame aaParent)
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
			// Returns all fields to their original color state
			clearAllColor(this);

			// Determines what actions to take when Enter, Cancel, or 
			// Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Validate the date field
				RTSDate laRptDt = gettxtReportDate().getDate();
				RTSException leRTSExMsg = new RTSException();
				RTSDate laCurrenDate = RTSDate.getCurrentDate();
				if (laRptDt == null)
				{
					leRTSExMsg.addException(
						new RTSException(
						ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
						gettxtReportDate());
				}
				if (!gettxtReportDate().isValidDate())
				{
					leRTSExMsg.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtReportDate());
				}
				if (gettxtReportDate().isValidDate()) 
				{	
					if (laRptDt.compareTo(laCurrenDate) > 0)
					{
						leRTSExMsg.addException(
						new RTSException(
						ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID),
						gettxtReportDate());
					}
				}
			// Validate that date is not more than 
			// 405 (13 months) from today. 
			if (((laRptDt.add(RTSDate.DATE, EARLIEST_DATE_SELECTED))
				.compareTo(laCurrenDate) <= 0))
			{
				leRTSExMsg.addException(
					new RTSException(
					ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
					gettxtReportDate());
			}
 			if (leRTSExMsg.isValidationError())
			{
				leRTSExMsg.displayError(this);
				leRTSExMsg.getFirstComponent().requestFocus();
				return;
			}

				// Capture the user input.
				RTSDate laReportDate = gettxtReportDate().getDate();
				ReportSearchData laRptSearchData =
					new ReportSearchData();
				laRptSearchData.setIntKey1(
					SystemProperty.getOfficeIssuanceNo());
				//laRptSearchData.setIntKey2(
				//	SystemProperty.getSubStationId());
				laRptSearchData.setIntKey2(0);
//				laRptSearchData.setIntKey3(
//					SystemProperty.getWorkStationId());
				laRptSearchData.setIntKey3(0);
				laRptSearchData.setKey1(
					SystemProperty.getCurrentEmpId());
				laRptSearchData.setKey2(
					String.valueOf(laReportDate.getAMDate()));

				getController().processData(
					AbstractViewController.ENTER,
					laRptSearchData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
//			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
//			{
//				RTSHelp.displayHelp(RTSHelp.INV031);
//			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * VAJ Builder Data
	 * 
	 * <p>This is no longer in sync with the class because of the 
	 * frame name change.
	 * 
	 * @deprecated
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private static void getBuilderData()
	{
		/*V1.1
		**start of data**
			D0CB838494G88G88G4FBD2DACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DA8DF0D4D7954E0CE0D151528E53214AA8EA6C6098BBD8D031B0A3EADAC7CBEDBBADAD1DE207CEF10CCE2A589FA7AACEEFF6B73BE99BE07991D31B42968121CDC37E8AB103C8C2DA12B6C27E9A92C8423E5DFD3B7B12377BB6EF5FA61BB40A675C1F37AF1B5D2469E8E63E396F1EFB6F39671EFB6EB9675E8D59FFE8C5C931EDBFA145A5047F1DAEA6E4735F8839E941465F891A7B52BABB317C3D8658
			C3DE179DD0FA204DBD86A06F7617C23D9E6A7946BA7B1F61FB9F39543F60A8FCE419E7G5AD687FDDF1FFD1E0281B10F0B561F3617C1398B50G348140CA14013C68A867749C286F625F08E29BA1018E6C33342E5CC2AF647DBABAD8391D76B96A28606DF9D047BE79D0EE6EC871FC01CA373031015787EEFA0CBC7B63A95BCE1C4B42DF2021130D9D602507B8EC2888BBF9246EB214E7BAC6C2E77DDA244AC886B423CA09872AF4A3DE65D795B98A556898941ABED115D09A9B5F51A3ED025064B794AD5AA547B4
			5DE8118C39DBD6E5CAEA3B58DBDDDBCDB6F662FA6E2CAB25F39132946ACC7687F61E6132AD9DF90C14BDF9561ECDF6F79713393EABA57B12323BCA37F5A16F0B0EB2BE4E00FA9E004DF90BC34CF93C9B76279BE97756E2DFAFFCA781CB3A05AC7F781B10256F03DF12A25F97EC3B3348D2584DE4585F1312E56583BD761729DD98C338379D50E6G968172G9B810500D728BD1E9CFC9D4AA61FF1DE0BC434E8D3AC562C8674FFE981D9056F46C6E86134AE592F6981C2184DFD372EB203BC820E77275BDB4AC6AE
			93E1A3F9DDCF100623676CA5995666ADBEE7AF67BCAA5FCF2D0D2D4B6D45F53583BD8C4881AC832C85ECFEDF2CEBE230EE2EF535C58DD90FCAEAF7429713F423B9212881D9676B6BF14C363ECCE063FC739E477432324E2A17AD2BD758DC997432E565C25B33DC9FE53DA93D3039BF17F56E4A5E197BC14FF9AF5AE1D39D5A61B978AE66B64A686FD17AD28B1D59780354EE311E8BED2517055D766D92EBE8F7ECA26E1AAFE6DC43326F5FE2D3AFB3F996DCCE5F5BC289FD4DAA20EF84EC836CGD4742D33B7G5A
			7B447E668F1E7C10F65BE159DBB6765FC38720141306AE35C5630694754B3D122EC8BED50E935E6763C3A83BEF5A38CF51736407B9AE48D1D9175031GF7A9A203A145D91F25C3FB300FAE53569EA5A24384119891FC178F6D03D21562467B31GF828D48F561EBB043A9C13FA01A3B0006F65FD287307294FD261FB9100696AE5BF5236C23D48544B21EBFB21FC8F8CDBD69B9BDB946A7DA4FDC24C7F3297FD090C7E32C50E4951GFC44F91B477BC2CAAFC14D147998256399B98F0247CDFA25F78F23838F2B16
			C5FBBDED178CC3D7FC89F4460621C4C32971E728AC71E947AFCE78998F200D15752F339F8154770B755D655DE7D9DF3B260DA4E2BD38F3DCCFF7D0BF5B8A7D3D7DA2BEDE2843F83920DF58692F7A041D366C5CCAAAB7FDA1231D363B4E595777B37BFC28BF7D2CF5D21E29B99E2D6D63F34C676C4F4F37BCCD67307F9F78A3185EBFA6235EF740BA4A2F40193BA274CEA48FBBEF4DD2DC96A76C128EBBA687D2D6467A7ED64AEEE3FF14B25B185D175946DE704DEEC3FBFC19EC0835157BB036E863D10BCD3E6A5B
			CB4FF0B44A9283F388E179472FFAD097CAD4B1AEAB0110ECF4AB13B2A149ABE8DBCF51B358815F01AB4216467FAAEC6948649632615A24ADD3BCDB7615DBED6B2FB05B387FCA4A1618CC877D28F3C8D5A2202873DAA2EAB07A91FA3EB91D7B1783014CFDAB82E80FA372846F6789B8897A91B5A11BEBDBA53D04BA561588EC959ED81E837E48E14D813703FC18AF56DFDDE7EF85303113575E00F2BE3996332775C11A63G9F8330782A3027650117AD67189B52F9CD65BC4C7D7A046C467D15C793B238E9BC686F
			19EBDCA26FCDEB6B656BE56D4BE4DC2FEA719139D7F13D0EFA110BE18CE134D7A82D981476D50708497FF2DB7359C07C660CFB0BA8384A76F4475574F8761B90722D98D8E72F9C90EB5F97347A3026F8DC73ABB4DC70FDF2BACE1139F260900DC0AA4D003BFA3ADB94C955C24DDA12709C372E8467C5FE6786C43E790E8B77A28975A5C8B36579FEF0261D7DB078A214C1C50FC31E941013C2277786D1B7983FB8D964F535D6DDADBA18B92FC3EC9860BEF5A0DDD7279514E3BB509D03C23656509E0B2EA08705F0
			9D021D94727CB6E4E7F64F6B3F8F15883BA742EFB6144C1613158FCE775915D0E7794F1DD4DF6786537322FCCA6F98CC4F0B5ADDA2AF72C2DB7E200853EF8E633A0E8F01BF9D926B3AB13C53322E8ECD099A661A4EA8F8F6B5DD89A9D1C1EB22B4484029B3E3316E925D63D6606985B05ACF1CC8DBG75E5C3A2B6EDF021EF48B56BBF267582335E656ACB5389767D5F462E1199F37CAF639853D16DB06EC7522C1F227589A27C5FB746519F84A1E10390529D137DE445B554D930C4783FB228FBG67GDEC08E40
			CEF97D4C09BC8C452036D7F08D6F65AD23042F834F73E9B2AF7FF8DE036B8766CDD139067B61BF382CFE38524BF2188EA883DEF10FDCCC779C57B58534B5DEEA27A57B53747A9D68D085A5EBAF1C560E67698431369FF4DA5B71BCBFE633365FBE2D3D44B21E1D7DFB0E0A7B6A0EFB1EA46AB7BF13755E3F434B4F3C44CA77595545EBA2F717B2BE77DD357A9077E747EC45D97874717181A97D7D60199328C36ABB0725B1A9D9959F074DEEECFC97952DF25FD8DA4AE5DFA6895F788BA71E5D7B21DEGA0BEF1B6
			EE1A55F7E482DB4B77DCC2A7EB6E7CA9D97B710B3617322C255C475650404B15E7D697277231C29D57930636850015008D0007G05BE61CBBEB10AB2C2BE76F6C0B11A8CC3720729A797E9DA2FA20F2748E8780C77EEF4B97C2C5CB70A4FD23D52B03FB5A1ABCD979C5E85D5DE728D037D76C8A1E27A5552F45D90FF4AEF6C076F6300B3969A731F034CFF7A197FF419F405524396BA732B3F26FB03759CE85B6697FE35A201F6599A007381D884D88ED89590FA592B9FE6FA49201337A13903A30837BFC36860B9
			BDFB5E3AC31F7B4ED892471869538CC30BA0FFC1FFA90E329BDA0C92994D9DC7BB81979FB6D8D7CEBF902777BCB9C8495C4FD3FB5D886B5A9A90FE7E063DC8F348F0B71685AD510934EA28371A34EF19B1E2C096B66D27FA4E01FAAEE003C957A8FDB3540BG65C1C1FF4E196E3B33C1701B2BDF0E258F1BE3570C78108DA5FC4CDC7D2C71056753096C7B7BC6E26E7D2DCF50BC897625E7A2A6B35A5F93B4D68703F1593423338937C9EB0BF7E8F1855DBE0B9341D26C2FCA93DA827A6786F10FF25C426FB7C03D
			831084AC86E00CE663BE3F977DC7C4CAAA11C404DECA6AC3F4FF299D94C25F2220683B38137605CB4C743E3954BE12703DG40E25F3DE66CDB8534FC40B600A38C776E306087BE8A90B77C6DBE75EDFCB46B894B2938467A341CC31FEFA40DD483C945C6342B66841C0BA8F543B5D3E11C7BCBAEEBDCE98933750621E46D6B1B2C6D793C3DD4916DE75A2D6D958AEB77C2497C46CA3779968B34BCC55CE5EA3C7846E44608AAE95CE8EC3C20AB01E6A954CEF70645096DEE91EB37A9A2CE9C5C03BCAB0661FE84E8
			9F96774064B1616F73EE5BC44AFF16797DF8EB4F8D3640B0337DC5432958357E019EBB4BA90E0D93D3EF41CE0C31D1F95CA3442CEAD1400B614350715A1A7A53758DF5276ACFD7FAD25FA7C47CD9B1EE0D258DAFAE4EFAF74967F2EC666514363A184932A9191265EBA51E6C32541DFA8B04282DBE4174760C192338C7C40E721AB3BDC74176D40E516DCC4FD12637AF1C1123CCEF7F760C9CC534733CA018A5AFE817FCA2AFF85405F26359E99D917B7A6FF72C3F8B2C5FFDC356B81EB3427437E2A4352FAA6CAB
			1B7F6EE3733F534199FAC71155003853BD53A46E2DDBC704AD1FFD632DF971351CC2A278465B45DD31DC95BE7FF677C7FB7FDE7F9147BDB76FF1ACE7E85DCBF38915658C45A63D135287ACF466930E6C91BEA18CED9BD511CBB8A651BE2AA3F02681814094E0B9E0EDC464938FA62A499CEF4EBDDA2C9D820CEAF98EE57C0FFFC0E3B870AAB0790D0C631BCA461C8B089D3A9613F5E302D1694B8657D5513CF3D2B66EF1D726F125D1E62F95D0D683DAGFD1C46E49F1E42332582ED91E085E0ADE0F3D42C635509
			B9FFEBF9BBB94BEFADC5CD332D27A8BAB31FE063126D330DAB5360AE27CD9F4B46DD44E7A37EFD59727D7A1E8F1B03FC9430B94E7FDF67E07B31F3B2FB1E33FFB2737B6BDFA6B33F3FFAA6E7FFFFBDBE197D7D7564644C7757B7BF58E565474CDA64D79F201D615B2BA2294AA4FD926B060E51904BE51F237E399E7634C593796D62D211877719340363A28FD6CDDA1866BEB9D0DFE15256F40A5CF80DA6ECD91F4A2E3B09294C3A3BBE15D9F78B27E67FFD6453D359F44B645B5DAC64F344044CB7368BDA0FC9FB
			26CA2CB7B7A6F23632D2FC63B1C0D8167B9D26FADA8B6D79GC67BA62595C07DD913F6016A4EB33A4EDEB9AAF8DD6F449CAE2C694A2496B5A435C9D5C2D13CCD713C7256CE916F7BE04C22D1245D5359B20B1D5A539006B108BFF1BED370FD01FFB3FE471A6762D7E261F73F059F523741776726713B2DFDAEFEAE8B3F6DB04621B3FED8AF076FEEA8BB519E298EAACE228E0EG3D9A407227327F90A272270F15E2FCB9CC53E70F9116BDACCF76E010EFBC4C7A1F3C1E6A3FEF7C90491AEF555456D7FAEA6ACF70
			F728AE129AB75879E146313E2BBAD19F98538E9116271712549F633D5B065FAC97FA22CC64C295DFFB1834BCF25176D712B9979A50195F4D53D339D0653DF7174E1CE77BBF59BC93747FDCB235E79FF307A3A5672612148ED6554E22035A9AC8186B5EBA41E35B8F1CE8875BC0CE9207BB8F608C20839086CC8116GF2816B818F818AGBBGEE83628B209E50EA887F51E3433731ECB11DDFB2684303AED95FFC9FF1E677BB0FBB673E1B7E5C19796D61A9674C3707271D195F9E0A1C5637877EFB9DFD778C015C
			FDE6BF1BCB507ACD1A4B758227DDD682C658246FF43D48692DB2CEA8687BDCA857B82575095F6BAC34FE71FB1D8B6F4B30E4F0BFE26CE17AFE9DD4D4157FBECD7BA8D138EE1B7AF8034A2844A40B5C55A68D65E67F0FF52B7F1FE040F9CF1192035418EF379E76F8EBBD249E737D5603FF6C91B72E133DB3ED2C69B30FBE12F72DE53F0D585A08B3CC5753C6CA46D1B1495875671FFF9E919FA57F2107E78B1C2108C18EE4768DE6B7BCC7D3E9B51D6CB627EF236B5FBAD657E8FF724BF69BF1405C4C9E5A0883B7
			95C45019A8F63007A47ECF826AB14471A6650DBB00B71CCF82EE469EA6954FBA5EB5709ED3E64A0154C739FFB8F15DFA67ACDA9849FA676C99E3FEA1BC167E9B50EB743728DC20EF9893E7EAD4FEB35B19025432BBA229AAC4251126E8G5FA91BA78CB995030F8511FFDC74610DBC97213FE58DDB467C97D0CB8788074251A3A68FGGGA8GGD0CB818294G94G88G88G4FBD2DAC074251A3A68FGGGA8GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GG
			GG81G81GBAGGGE08FGGGG
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
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// defect 7890 
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
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
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmVIRejectionReportINV031ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmVIRejectionReportINV031ContentPane1()
	{
		if (ivjFrmVIRejectionReportINV031ContentPane1
			== null)
		{
			try
			{
				ivjFrmVIRejectionReportINV031ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmVIRejectionReportINV031ContentPane1
					.setName(
					"FrmVIRejectionReportINV031ContentPane1");
				ivjFrmVIRejectionReportINV031ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmVIRejectionReportINV031ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(365, 220));
				ivjFrmVIRejectionReportINV031ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(365, 220));
				ivjFrmVIRejectionReportINV031ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java
					.awt
					.GridBagConstraints constraintsstcLblEnterTheReportDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterTheReportDate.gridx = 1;
				constraintsstcLblEnterTheReportDate.gridy = 1;
				constraintsstcLblEnterTheReportDate.ipadx = 35;
				constraintsstcLblEnterTheReportDate.insets =
					new java.awt.Insets(45, 103, 4, 103);
				getFrmVIRejectionReportINV031ContentPane1()
					.add(
					getstcLblEnterTheReportDate(),
					constraintsstcLblEnterTheReportDate);

				java.awt.GridBagConstraints constraintstxtReportDate =
					new java.awt.GridBagConstraints();
				constraintstxtReportDate.gridx = 1;
				constraintstxtReportDate.gridy = 2;
				constraintstxtReportDate.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtReportDate.weightx = 1.0;
				constraintstxtReportDate.ipadx = 77;
				constraintstxtReportDate.insets =
					new java.awt.Insets(4, 138, 11, 146);
				getFrmVIRejectionReportINV031ContentPane1()
					.add(
					gettxtReportDate(),
					constraintstxtReportDate);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 41;
				constraintsButtonPanel1.ipady = 40;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(12, 53, 35, 54);
				getFrmVIRejectionReportINV031ContentPane1()
					.add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmVIRejectionReportINV031ContentPane1;
	}

	/**
	 * Return the stcLblEnterTheReportDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEnterTheReportDate()
	{
		if (ivjstcLblEnterTheReportDate == null)
		{
			try
			{
				ivjstcLblEnterTheReportDate = new JLabel();
				ivjstcLblEnterTheReportDate.setName(
					"stcLblEnterTheReportDate");
				ivjstcLblEnterTheReportDate.setText(
					InventoryConstant.TXT_ENTER_REPORT_DATE_COLON);
				ivjstcLblEnterTheReportDate.setMaximumSize(
					new java.awt.Dimension(124, 14));
				ivjstcLblEnterTheReportDate.setMinimumSize(
					new java.awt.Dimension(124, 14));
				ivjstcLblEnterTheReportDate.setHorizontalAlignment(0);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblEnterTheReportDate;
	}

	/**
	 * Return the txtReportDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtReportDate()
	{
		if (ivjtxtReportDate == null)
		{
			try
			{
				ivjtxtReportDate = new RTSDateField();
				ivjtxtReportDate.setName("txtReportDate");
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtReportDate;
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
		// *  to stdout */
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
			setName(ScreenConstant.INV031_FRAME_NAME);
			setSize(365, 220);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV031_FRAME_TITLE);
			setContentPane(
				getFrmVIRejectionReportINV031ContentPane1());
		}
		catch (java.lang.Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	// defect 7890
	//	/**
	//	 * Allows for keyboard navigation of the button panel using the
	//	 * arrow keys.
	//	 *
	//	 * @param aaKE KeyEvent  
	//	 */
	//	public void keyPressed(java.awt.event.KeyEvent aaKE)
	//	{
	//		super.keyPressed(aaKE);
	//
	//		if (aaKE.getSource() instanceof RTSButton)
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
	//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
	//			{
	//				if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
	//			{
	//				if (getButtonPanel1().getBtnCancel().hasFocus())
	//				{
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//				{
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				}
	//				else if (getButtonPanel1().getBtnEnter().hasFocus())
	//				{
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				}
	//				aaKE.consume();
	//			}
	//		}
	//	}
	// end defect 7890 

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
			FrmVIRejectionReportINV031 laFrmVIRejectionReportINV031;
			laFrmVIRejectionReportINV031 =
				new FrmVIRejectionReportINV031();
			laFrmVIRejectionReportINV031.setModal(
				true);
			laFrmVIRejectionReportINV031
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmVIRejectionReportINV031.show();
			java.awt.Insets insets =
				laFrmVIRejectionReportINV031
					.getInsets();
			laFrmVIRejectionReportINV031.setSize(
				laFrmVIRejectionReportINV031
					.getWidth()
					+ insets.left
					+ insets.right,
				laFrmVIRejectionReportINV031
					.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmVIRejectionReportINV031
				.setVisibleRTS(
				true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * <p>The default date is Yesterday.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData == null)
		{
			gettxtReportDate().setDate(caCurrDt.add(RTSDate.DATE, -1));
		}
	}
	private boolean validateDate()
	{
		RTSException leRTSEx = new RTSException();
		RTSDate laReportDate = gettxtReportDate().getDate();
		RTSDate laCurrenDate = RTSDate.getCurrentDate();
		if (laReportDate == null)
		{
			 leRTSEx.addException(
				new RTSException(
				ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
				gettxtReportDate());
		}
		if (gettxtReportDate().isValidDate()) 
		{	
			if  (laReportDate.compareTo(laCurrenDate) > 0)
			{
				leRTSEx.addException(
					new RTSException(
					ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID),
					gettxtReportDate());
			}
			// Validate that date is not more than 
			// 405 (13 months) from today. 
			if (((laReportDate.add(RTSDate.DATE, EARLIEST_DATE_SELECTED))
				.compareTo(laCurrenDate) <= 0))
			{
			leRTSEx.addException(
				new RTSException(
				ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
				gettxtReportDate());
			}
		}
		return false;
	}
}
