package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.MFInventoryAllocationData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmReceiveInvoiceINV004.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Name			Date		Description
 * MAbs			5/22/2002	Made focus outline larger and took away 
 *							scroll bars when not visible CQU100004058 
 *							CQU100004057
 * Ray Rowehl	06/05/2002	Allow regions to receive invoices just like 
 *							counties.
 *							Take out edit in validateInvcNo.
 *							Defect 4176.
 * Min Wang 	07/05/2002	CQU100004413 Added Code in the mousePressed 
 *							method to prevent deselecting destination 
 *							substation in the Verification On table.
 * Min Wang		05/07/2004	Set the default Radio Button to All Substations
 *							For Invoice Receive Verification. 
 *							This will allow us to ensure that the Inventory 
 *							being received does not already exist in the 
 *							county.
 *							modify setData()
 *							defect 6792 Ver 5.2.0 
 * Ray Rowehl	02/22/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 *  						modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3 
 * Ray Rowehl	08/12/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing for button panel
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		08/15/2005	Put cursor on the Invoice No. entry field 
 * 							regardless of any radio button selected.
 * 							modify getradioCntrl(), getradioStock(),
 * 							setpnlVerificationOn()
 * 							defect 7514 Ver 5.2.3 
 * Ray Rowehl	08/15/2005	Reflow the screen slightly to make more
 * 							room for text.
 * 							move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Remove set selected done in key processing 
 * 							for radio buttons.
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	08/24/2005	Remove focus code from keyReleased()
 * 							modify keyReleased()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3	 
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboDest()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/30/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Also setRequestFocus
 * 							to false for this frame so that the focus
 * 							would not move back to the radio button 
 * 							when a hot hey is pressed to activate a 
 * 							radio button.
 * 							remove keyReleased(), 
 * 								ciSelctdVerfctnOnRadioButton, 
 * 								carrVerfctnOnRadioButton
 * 							modify keyPressed(), getpnlRcveInto(), 
 * 								getpnlVerificationOn(), getradioCntrl()
 * 								getradioAllSubstas(), getradioDestOnly(),
 * 								getradioSelectSubstas(), initialize(),
 * 								getradioStock()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		03/31/2006  Add the abbreviation for number "No" from 
 * 							the invoice number.
 * 							modify visual Editor
 * 							modify getstcLblInvcNo()
 * 							defect 8653 Ver 5.2.3
 * Min Wang		04/10/2006	make more appropriate change to 
 * 							InventoryConstant.
 * 							modify getstcLblInvcNo()
 * 							defect 8653 Ver 5.2.3
 * Min Wang		05/22/2008	disable Substation Radio Button if 
 * 							no substations exist 
 * 							modify setData()
 * 							defect 8597 Ver Defect_POS_A
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV004 prompts for which invoice to receive into what office 
 * and which offices to validate the invoice against.
 *
 * @version	Defect_POS_A 	05/22/2008
 * @author	Charlie Walker
 * <br>Creation Date:		06/26/2001 14:29:37
 */

public class FrmReceiveInvoiceINV004
	extends RTSDialogBox
	implements ActionListener, ItemListener, MouseListener//, KeyListener
{
	private JRadioButton ivjradioStock = null;
	private JPanel ivjpnlVerificationOn = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JComboBox ivjcomboDest = null;
	private JPanel ivjpnlRcveInto = null;
	private JRadioButton ivjradioCntrl = null;
	private JRadioButton ivjradioDestOnly = null;
	private JRadioButton ivjradioSelectSubstas = null;
	private JRadioButton ivjradioAllSubstas = null;
	private JLabel ivjstcLblDest = null;
	private JLabel ivjstcLblInvcNo = null;
	private RTSInputField ivjtxtInvcNo = null;
	private JPanel ivjFrmRcveInvcINV004ContentPane1 = null;
	private RTSTable ivjtblSubstaName = null;
	private TMINV004 caTableModel = null;

	/**
	 * Vector used to store the Substations for the combo box
	 */
	private Vector cvSubstaData = new Vector();

	/**
	 * Vector used to store the Substations for the list box
	 */
	private Vector cvSubstaName = new Vector();

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private MFInventoryAllocationData caMFInvAlloctnData =
		new MFInventoryAllocationData();

	/**
	 * GeneralSearchData object used to send the invoice number to the 
	 * MF
	 */
	private GeneralSearchData caGSData = new GeneralSearchData();

	// defect 7890
	//	/**
	//	 * Int used to specify which Verification On radio button is 
	//	 * selected
	//	 */
	//	private int ciSelctdVerfctnOnRadioButton = 0;
	//	/**
	//	 * Array used to store the Verification On radio buttons
	//	 */
	//	private JRadioButton[] carrVerfctnOnRadioButton =
	//		new JRadioButton[CommonConstant.ELEMENT_3];
	// end defect 7890



	//	/**
	//	 * Constant that define the length of the invoice number
	//	 */
	//	private final int INVOICE_NUMBER_LENGTH = 6;

	/**
	 * Stores the border used for the list box
	 */
	private Border caBorder = null;

	/**
	 * Stores the row in the table which corresponds to the combobox, 
	 * so that row in the table can't be deselected.
	 */
	private int ciDestRowIndx = 0;

	/**
	 * FRMReceiveInvoiceINV004 constructor comment.
	 */
	public FrmReceiveInvoiceINV004()
	{
		super();
		initialize();
	}

	/**
	 * FrmReceiveInvoiceINV004 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmReceiveInvoiceINV004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmReceiveInvoiceINV004 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmReceiveInvoiceINV004(JFrame aaParent)
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

			if (aaAE.getSource() instanceof JRadioButton)
			{
				setpnlVerificationOn();
			}

			// Determines what actions to take when Enter, Cancel, 
			// or Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Validate the invoice number
				if (!validateInvcNo())
				{
					return;
				}

				// Store the user input.
				captureUserInput();

				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				lvDataOut.addElement(caGSData);

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);

			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caMFInvAlloctnData);

			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV004);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the user input so it can be sent to the next screen.
	 */
	private void captureUserInput()
	{
		gettxtInvcNo().requestFocus();
		if (!gettxtInvcNo()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			// Create the General Search Data object to send the 
			// necessary information to the MF in order to retrieve 
			// the invoice information.
			caGSData.setKey1(gettxtInvcNo().getText().toUpperCase());
			caGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			caGSData.setKey5(MFLogError.getErrorString());

			//int liSelctdDestSubstaIndx =
			//	getcomboDest().getSelectedIndex();
			caGSData.setIntKey2(SystemProperty.getSubStationId());
			caMFInvAlloctnData.getMFInvAckData().setDest(
				(String) getcomboDest().getSelectedItem());
			if (getradioDestOnly().isSelected())
			{
				Vector lvData = new Vector(CommonConstant.ELEMENT_1);
				lvData.addElement(
					new Integer(getcomboDest().getSelectedIndex()));
				caMFInvAlloctnData.setSelctdSubstaIndx(lvData);
				caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
					InventoryConstant.DEST_ONLY);
			}
			else if (getradioAllSubstas().isSelected())
			{
				Vector lvData = new Vector(cvSubstaName.size());
				for (int i = 0; i < cvSubstaName.size(); i++)
				{
					lvData.addElement(new Integer(i));
				}
				caMFInvAlloctnData.setSelctdSubstaIndx(lvData);
				caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
					InventoryConstant.ALL_SUBSTAS);
			}
			else if (getradioSelectSubstas().isSelected())
			{
				caMFInvAlloctnData.setSelctdSubstaIndx(
					new Vector(
						gettblSubstaName().getSelectedRowNumbers()));
				caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
					InventoryConstant.SELCTD_SUBSTAS);
			}

			if (getradioCntrl().isSelected())
			{
				caMFInvAlloctnData.getMFInvAckData().setRcveInto(
					InventoryConstant.STR_CENTRAL);
			}
			else if (getradioStock().isSelected())
			{
				caMFInvAlloctnData.getMFInvAckData().setRcveInto(
					InventoryConstant.STR_STOCK);
			}
		}
		else
		{
			gettxtInvcNo().requestFocus();
			RTSException leRTSExMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_INVALID_INVOICE_NUMBER);
			leRTSExMsg.displayError(this);
		}
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
			D0CB838494G88G88G4E0236ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DDC8FDCD4D576B7B343568A4B0ACA4B0AD6AAABCD5A2835A233FE7E41D22356B65B255F5A2E5B1A59EFAD5C5F72DB5FA08A9AAAA9A9999A22A5A4927FD4C2AA10C145C6A5C5344D1401F94048B0B34EBCE4D02A5FB9773EF35F1BE1E6504A6573B91F776E777EBB675CF36FBD771EB7C8699F8EDBE4C8CC17A443A2C97D5BE110245A1312E4BA7B7C5B044DBA9BEF10F4FF9B005E153E5D1D844F791057
			70E23CA1CD0A5912826954F37106A9317106B5703ECA1A3AA56A9BF88950CFA864BD79524DD30277337ABD6AA719255FBF198A4F65C0E950D789D01FF358FF61498C95BF8669656ABB12A1D112F23A31CCCED1068E9F2716CB6F664F246F310C69E4261A6F84BC84E8BA506CEE2D4DA50C3B4B4ACA557471C1AFC973A6CECADC06FD695AA7B2FD4F6BBA55E716F4B8919531D02AD8D4854F725A3616AF1A9C6D790A4762D0722DEECB3ECBF167B7592C329D1276B370F0383A72BB2CE5E53B8F6DBFA4B74956B3F2
			0D7D0C435AA4579C28AFA8A81162986F870AD2D83B129481E94E6754E565AA9F035BDE1266FE943AB0901F1BFE607C6D7DC163B36EC85F0573FF4036BB185E31DE88242301388C9F1F3EE099DC6DC1E4F874A449D07EE33CE14C0F08198B31EF855E8782057FC832FD3E19E4BBF66EB5E9FE631C449581E4137AF019F27AE83225CE7DAE65FDE6D3372FC63B30C0DE88509020E820B8A06912F843874C163FFB6F93F8D6B6AAD50E76F6073D52695CE5333876BBAC328D5E4B4AA0C745C0B8074BA2C95CDE8F9765
			796107A86392EFDB556C6B28C476B56250ABD2E66B20050B7C48E6B48EDA182736D1F109A69B176B0B8C142B8170EE2041C0A300E200E68B39063FD75C1BDCB5F6C5F6594D36439D0DCE33CB5955E135D9E417AA5F3913416473C73C5E008B2E477592D12C574B0C51EFA4A67B514B0C085844143E2ABE7AEAFA61FD0F8D58F7C55F1E6301E4690BF678BC3343BAF8B728B64A71BFB2BCC20773B95342F0CCC7C1DE6625E4374716138C26246725344239FEE5889B7BEAE21FCBB9BFE1177A0E6D5A3538CE0D83BC
			8EE8BED0DA3FF8C381108928219F0D6F7F3C7769F95AED2DEEECF95DFBD62F06276CD1DC669A3BDBB15B1B64FA334BEAEE3449EEC9AD93399AF9EF742A77B01BCFCD50479E59AE3B4C0A55E107564D6DB298121B17993B7ADDAC6312D9EE1D35DD068E5A1D9235BBF175AAF85A4CEE650853E2D6E42D844F1F369AF5F946DC8FADC28370BE329F6A7C94DB837B407B90A02E2BBD9FA0B68F52291711DE762FD9894F2F4030E5DBD919515A04EC18DDDD547F1E8CDCCBE4DC380C32D336DB60452D669D48D8226925
			59A1F82EE3B84E11AAE05CAD70FFE73C4BF1E8C32FE54AEFE479E6C5F1D99BBB9479302CA8D6FB0BD67F9463456DB57D5C12BA47B3514632C02ED2203DC23E3FE72C5249E7F2B85ABA1CF5B8F22A1EDEE46B6CF1A82FDCC6FB6BCB05385706DDC6F6BA67985929F1D91C14777C9C3FF6DA30EF5042B117F17B0C3D4CF72E3D4A5A547AE8AFBC96E40E07DD66FF7EDD585A52BFE89F8BFFC67BC8DC6F2356215E93005F0C7E207B7E24777C773378FC5BE5F64BB4438E3AE044E40BE6E53C6C263501EDEC4BDA7FB6
			D638563F0D6DD89B5C063EDC6B4F06F85E6E353837B8BA6DBA1B7CEA6DCAB6076DF619B1AD2AC8FC5F1FD3073A305A2D4AD12B25C5D68EDB4F4A1264690F36F5891B033570EE69CF36543911ECE9535999D2744989097E763330BBE7A50E694FEDE0CAFF4D16B8CF268FD06732CDEE87C5D5B9BA6C8A478F31792D626A7AE24C74DFF6DFA65AE31B5C2516BB1039D842F54456A18B59C61A5FC19D3B2C6DB0D4B8E1D57FF16EC93D7FB89778C3DF3364F218F7C08BD9FB52A274FD8379B85CF6DEDB0F3CB5C09D85
			E86065E4BB93B35F574DD955E82A9CB69B9F82B1B68F2DCB433114CFF74830A463247ECA4873583A15BEF9752AECBC7F69F5A81BCD379E04DF0E3205895F91778B0E5D4F30B7E0D2CDC8227DF7FF1A5E6F3538DF71BB0EC833AE676317F0396F5E75E196361B9992EF488BA159AB576B572BCA375B51E4E5DB03BAA60B1336CB3D7919ADEC3731B1AF73D05DE12355ECF3346CF2F8A455377CF783760B6DD50410EF595E08E36101F4A8D00460E76E7A1EB675567A2570EC363A5C6093D9E48F69741F6BD1B738D7
			29B079F0C5FADD594C7EFDB8246890F57D8C7155153C8179188BF852G626D4C076F6AF4850686DBF38B0CA47133A9EBA137F1B51D133508ECDC22B572E4C3D03FF40077BA1C87E96E6B3C4274D5B14057877A9B43EB87787AC06EC672019AA0EF5CG5A133F4DC639F22E0035738A126B512CE5BA39EA9DD63BA2E4BA3581672943E5ED315A89F3B08C3CED36F071FD6D8C9B633D50E68390475EB2A3568F52E1D750BE7417C6DC8742C57ACD161EA252DD0D4176902C77737729EB027671F377A92E2306EC9C8F
			0FC8DBD83ACB2235EED4A72E874D601C41F6F158A9B7C9A13FC21DDD59C0EB5DBC10B585309C203DC02D3F2235AE35915BD02C8A333DA93F4273FBE32E24723ECA3A6640BA092F05CDECA1EC622BE015838E9468895965C2094E6659CD7A35B5FDA077CBCA61E9826273F7B49BDB645F825810014C9E9B52FDF476A81C7773614973D7197579B8EF10C1AD3F5AAB9F676D28A4FD7E73DE790BF475799C1F74911DC193A67DC932BD78F240FB001983394D24DC491F2136E906B18F77DD485BF976DB7DDA11F653D3
			0906G6DD42875CDD77A5EA77CBD97F5485648D366B3E6CF3E3B9386352CEC9F2A5A262E01CDA9AA6FFD2E24B5B0CC46B99A8569A9C0332E24B99095F40D70C7DC16A712C9A7110F3CACC5759F1A78CEGD9242BD49954E7CE5BB40366E3AD5D027254C21E8DA894A882E88CD06CD5627C1A0BBC020FD5ED31AA150AE2EEEAE5ABB639DE75D639D3035140F95BDB311ABAA7DE4F7D881E7566D36AC9881BF2382805FD204A030D27E0152DB337C8E27DEC72554D2C2B34B5A1815E532E0EB7EC3ADA4378BA19590C
			3AAE3D1A2F13AB841E4770869D4E574FDB5858E0BA847212C2E97DAC5B0AF6598752C3002201E201E60512DE8ABE4966FA712313EAF058E0926209CEA19D2C5A925897DD3B2577F36046AD38B7B6BA9445510E6D931E3B857525B81C8CD4FD64ADE8A7301437AA3C280A1730BBC41B5C4CE0F5BDE7761AB4887C6CC1341E8F31A1D68E69BA01259A91EB05F49F01B54B34978C9EC4B61D5D0C6766A8C8CF811A85A4DD83ED8395G55DEC3650AD839D6C807GC5G0D871A8514FAAD143D164A054B4EGF3815B52
			2731CFAF5B3F06771FF14D796DA585EA39A9C216A783763D42A76D3B17ACD16D781D731C574BA4ED7D63E335EBEBE0FB29595A3B3D9C5A4A7CAB98673AAE274C3123DB599E5F5C6C1605DD9E5F1AA6309AF72D43EDC527106FAFD7B6E1F91B394B518165A32E45B19F5E265FC76AG6B868A3D166B0E579BEDC3395B4D9EEBFBC7BBBB38148EE66716F666EBC15904EBE90F5BE8E4E5612063DDF6A73337EEF88F810AE45877F2C6909D7276A66663FCF6ABCD26C69BEE09879C9C7F179347C471A89C94659F5F3B
			D414B74AEE0570B3DFE3F9F0059B9D9CD6713773585945EE63FB2E62D0716A236FF23CDEF6D91B2DCD4C37BEE897757EF294795A558173574E360B42126BD0A6EB33FE7FCBGB951C74A02BC1EDF3CD31F5FFD9D4F0F96792D757E6A470A7C489AFD7EAC35FE5A7560EFF473F96D6B97E07EA648AF95E5A6F57AABD38B7996D16668E6FD196E6BD5BE6199FBBD5DE597B6601C977BE73E39D3A9AB5B6332DAF619DBCC4C4A789EBA390D7C105957539E7A4FB6545F269B628D65C0F5B750395773B16D05A3FE7D3C
			1430597F7DFBDC5D8B09369BD43F6086EDDF4F1A7ADD8A772B0CF8ACD3476A7F2A507F304B1DD944E63E518AABBCDE04659495166CA8A9AD5EDE32A3AFCBFB4F253DF9D8275E4FA8DD7AD24073DB0C4AC71C7ACC98BA4D40F9392FC06365DBB9ABB0AF0C09629DC5393C6FD3ED240B4CFBDEE072857215B242F81F65E11AAEE87D81BF27B9001FE3B2B7121FB32C99478867C2F7980D45773B753110B1ABDE886817C42A7D466A7A1FB2653B945E7F7265BB248BBC0B56581D9D4AEE2BEC3328E751D3BB11BFDCC3
			53EFA47F7656FA6259F3A359E40B6D837F32D661544743AD2FBB4A04F5F10E0F946D990D54DE7ACDE427FD0B50CECD1036GF55FC4E5ABADD4F630A8BBE0A7160D0174CC20041B0827AF36EC3BA079F52B0AC4E7712BB753D9BC7DE66A6F3E82469B24ADC05DB753BEB72D6DC27DD06ED3B9EC585E601B39CFE59038076143F4B877358CB60AF10D003C34A164EB7138218269D0208820E820B82004A164EF3D3995A7C550FB76BA0753849B26CDF7854C5BB79523AE5206469B7206D2FB83BE5DA28570C9812CF5
			B91C32CB6962A83B61D1FD666FCB10D79334538AB490A89CA88AE80AE87B2D0262A0ED9FF2B858598C0FE9E6AB0CA2F395DDE6BC0D73713833595FF84C9C4A674AFCF8265E82FB84D049AD71BAFF7EAF5BF02D5F8B1885289BE8B0D064AD628E283057D8D435A7C8AC4A315377BC9DFB0B66DB4D3A25271F156087C3CA3B95769820D2A0532D3C0C6A6F557B133DC1AD2340B384E88850C82FFA3FB17972B68E7227834D8612067967A35D8F1E83D8B9D02DAE0F77D1E35473E601FC0F2E4CC0F89F8694B94C77CC
			72B6DB93468F6373E111408D45084F5061FC1E043072181E8BF9F53751BC5DED51779F85F84CEDDADFF170BE8FA865F65F7E7F0F2DB3193773FE1688FCE9B9BBAB69F05E7F552CBC266BA0AF6AF66AFF7066E06BC4669D60375F612DCF5ECFB60B53996F70DDA736B03C768E5FF5A232005609631097FD87755F014B1BBA96BF685E13ED416EF9FEAE71BE32AEEA9FEA6C21A8304FDFDA64BFDE71C5117FF80531A8F83C42D494B8DEF13828E73C626B62657A76781228669DAB46F587E395D6334DFA16393E0721
			203D056777AF89AC5715A53D1FE52EAF71FF769DD252736CFBDB097F336F70121EE75FB14C0759948E6BE8B81DE93FEE21336FDE01FDDACEE75F63824BBF0218935221820B2B2773F0F8B82D39EFEC8BAC7B5B5B7C0FE942B67FE31A38BDF81CF36976C0E34E799B44FC22F940DB6A1D4473F4A6DB8124778AAC111D9D0FC35AA9302F3612EC7D845646F4908B697982FB04E1A93F067D695704A5B13D14C03ACEE03F2DA75D378A4CF414F45F2DE115247BD001352FA15D078BEC549A62AFCAE053194F16B2BC5E
			10B91C307587902B00F41D40AC879173C0BAD4E037FFC6FA899758EF73900B02F42C40EA76D13F3384F6C597E225913007C49096D9C53A32882C01790F8A24C3847620116EE342847669EE524B9801FD301E74B2C5E00DF5444B4C88DAB393D80C2964AE58E76FA2AC1979C396C8779358D5C50156B72CFB3E6B18A9C0D95E477000FDDC48DD7C22GB85741F79652E978DD2417FBAD2453C801CD12C9275385762318673385265DF1C9F7132E9616631DC9AD24EDC0A1C091A2AF0D65C5C3BA8EC83A876641BD14
			F7DD8F7F89895B5027EB6F762F4390950F88104F7B384FEF9F3EBA8B246FE47539A4280E6F59CCBAB15EC3FA7AF7815977E0A1EF8E069FD43E8AF56F0F94DC7C7D7A390B5A87BF1BBF574C3E3D00851C5FD97D156DA7612893A00E1DE57BC98C24278B6C983333249170BE026E412A0FE0FC365561321E45F3012D52E6ED31632D32FA9F76D4BD45954A210E8D6BCA717546DEF85413896AA03DA7F15F2A815E0B55F74EC344CEBC1F36425EE9437D93F696FE67973D136EF6A221CE4C88753B4EFB795DAD2F7BC6
			397FBB3F644F116FBCA8DBFAAF15FDFFAB5A23950F6C7064658A3612FC16FBC93E462D4164DB64A31FF22FA69F6605C0FA0017FCFD0F202FE107715A6DE86AF0230F81A7A127430EEA16C65E0BFD8EBD02E3BAD870BA754892AE17C93637A82D025F3F32FD8E6FD76609329F54879F43EDE28C136EA3DB184930BCCC8B2C036D03F510EE50B0368F8E04F418402EDFC36D0D3B0FF4B6FECDB01DA577E29331504EBAAF1D3DE142BBE0191DE0698EF871FE3207B8A8BF7FBE757B487B799DBE2F370D6D33AEF4F8DC
			320B57B39D905F90C3D9677D3C5E982FFA678EB2DF4F5C545662F2F458AD246FEB599E9D8BE5636EA779A731BDDA9A897E4CC82ABFAD8F474469121BE517CB36704FE654B67E46766FD2A8EB9CC97AEA5897CCDFCBFC74659C69EDE37DA0BD41205757EDDD7E6F339FE6BEC1A8149F8244ED6D65AA3435710277CEA36A3A5161E2CE9DAFF315914B4C04B2DC07FF907735B9234007812C821F23F86CC4DDDF183FE0834CB30AB0A37397A2A19DBD0AE46F2A6BED7D504F3159D0CFFA004B0E69F4F87F5302858BEE
			G627C07FF02E367B2DB2C0EAA3B623211CEA4C9600795C7D39B78946CEE7A33E27D5DF45683DC865383F4F73D642C3F7C0687C8FF7DADEC7DFD00F4730245B7B69185F9B18FF0BB63FA1847767319004DFD00F4934156633C51109ECD3A79DE6EED6DD9241BCF962867992D69E660E8BE0FA68B5D3C5F380CF400710403F6DB17301922A3ABA82F5206D7AFF08EF22BF9467A15C2F7B2FE28C459EA7E6CDCBCAB6FBB5C64F258ECAC644FF5E7AC77559D465542C613EE3FC87617BFC2647F5368AFBFFAB45D77FF
			3DD12F6B5851BCDF0A0CB724C7128E47EE6ECD0749BA9D66C13D0AC8CD07F5705E81CF3C8B6663FDE9810EF783607C1E7C994CD275302F85751B2DFE8E16F540D10671D9DE6E4DD22D5C267275D2607B74423C2C4212DC2A17DE26555BF864FC6A7132B36AF1BDA8817AD035FF36CE29FF3CED53CEFC6777662F246A6F6D0D3F9B1A38D172FFEFEE0BD4574CC86D5EBCA5EFD98A3FB71935133E851A8779F98F52FD72B327BF13FA5EF167BBED66667CC6335B5AD4D6368B9FF530B39E2959445E776A3CG680F5D
			9FFF446E0BAC50EEB7506087E99CFED32E69A9BEEFDDE0BD1564E59597640A32FB368656535CAFF95BD5DFEAED0FBE02B237480A884B6508384B6732BE6ED2F06E4E440581F4B862C1D5078FEAF197F51DE26336DD74E72A47F815DEE57BD0C5AC4C10FF28E64F5EBA4E63DD959A0F55C7314DECE44EEAE7B7A1E8083CDCCC25D62E32B218BD6D703243B7F6E9759A571CCFBDDE765EB524DF5E4ECA1C0E6A9DDB7EFE3215592007CD3FA1DBB12E5B9950D65AE5F0EA4A4A76630342A37CEEFB53111AC3325B5161
			EA12D5DBE9E36D9F07F6952001C03CDF65G759B8E588C51EF54164F4F4BC6295FC32AE7216F196CF4946BA37DA11883C0460748CEE3F6EBFA3C6933F4F5EC198E2B9CB607AB3F33552A482A4E77E8E5C76685517936EDF9D9C545E22CD656E8753E58F7BE75825B7E36FD5AFAF16CAB2D5D3B2A0234DBD820364B4B7E3DAA0D4F99DD48BAA7D75D2F0D5B54F66A709BB34BAE66F964A0D02DB4358AA0B7AFABDB7D5CB68B6B17E5678ABBDADCAD6EEAC14F039F22F85701EA0A6146BCC47127EED6B6A9AA5E1089
			D492C563F23FC913ADF5FDFEE0598246DEF9BB5376EB6D9C2D4B8A2623D21CA8A5397EF49FF8BC5EBA00A9EE2F2BF72D77E32FE5E57B8E61D64DA3F2940BF396D3AC5294C57A29B8CB5848A8727F06D8CA4FFBBC741F99E49D04C5A617371BE0A13D27BFCCFA7F04FD63E4023485287BE1527B7CEFB4FDB94BFF3A5EBF7ED6EBA7E83C3A84B74A2202528B54FB5DF1CCF1794E15135E86BFCC3A6CC8A6EC3E400A2A89BB7E080875D62F73BFE61A77A47A9930113E570EFE0474585201FA1C8F69345F029F84F4E1
			F24C78CE53530DAC464F63F693CF6042DC816D9D876A839486B482E89C50F42059C05218F8C386D08110916898108D289BA894A8FC8C5DB98F5A0973B3D07CD17D60C297F524BB13B9BBFB0B83C70D713EFBD26F98FB2D375EA74D63BFB7B2FFA2F60CEFDC68F606470D710D8BEDA92735E6B66455BECA71630286748BA39FA5797BFF199DC47EAA07C5665189623FE47FCF13FBD72F757C4B7DC736B70EFF54D76ED98C0FFD54D76E537BC96EB848B3BEC677EBE78F7CB43EC3FA2D671F6F0F8E325F61BD664BF7
			BE43A39F73657B218344F7B464EDFA1C78CE2D7EE93FE9DC7B936B9D3E60FADC2E39EC3F8AF95CD75EFFB0FC70633E727695FB61B048CB0A96F14BE25F3B48D6486B0ED63FC7D21F61513D5F33C7C119982038E85F58F475D95FBE66C11914A7F839CC75D970C46FFD98214CB1A05B933EFD98641F26C77E0DED1FA7FC75B81C61831F7055E301CCFA8C033C04A7C80FFD4AFDE5EC988B7E65D84EE7087A9CB236F799C7C219F1C053477A4AF8B659370F59D0C6FAD23D73D41FB9CF765EC7B914298532BC695B47
			0EEA5FBEBCD0E620DAEE187A0CBC0FBE46C31999C0F3FB74F15546E0E315B42EE75BFCCCDEE2FBDC46B85F5F1F3C4A701CF13E3FBFE96FA0FF3E9472060C23B5788FA78247BDDFBD51FB4CF74E897FB15FF9A7FA46FC7FF142FF4CF74189FD4C17E3C9A75197E801BD7018A837C4E05F88EC7949A5AAF654EAE1773E9CCFBF39D445774A58A161EBCFA2DF1D8CBB26E29F6A30EF6837C3A7710E8FC4EE457BD4759B6E1338FFB5DBEDB61E5E4A4AD86DEED9917AA8E2BCDA1DE69D5FBB8406FC2FE363903DEA992B
			EB336297C66C974242A6EEE567F47E9E21FBAF6CB12FFEC962FD6C3B28FD285F37B7205E5CDEFB7B2B7BB5B967685EBB77DFFC19C37F83FDECB92849D422FBFF7440456F7B057FC09FEFD7EBB2AD503D0774D8C3FF793EEF3B28FD7038698D6CECA8BD95EFC8FFCAFCC742309CC8178ACCA921F835C9E0738C08B5C03ADBE00793A9B69BAA306A9C0AEB47896C7EAE0A574E93D84BD962A569E9426EB9CB3CE4882C6C9C7112A7303BB790AF9582B3E493AFF582FBE6AD7192AE30531D44CB54535A6FBE34313ED7
			773EDD3E7863FE70227621FEDFD2CE3A9EAF747067F252758C01F9BEA7DD4F9558FD4D246B247FA2AC2505F41DA1300305246BD6013D3A18F45DAD30A149444BC00145A593AF4384D619C23C0C90583AC262A5DAE0BF9492AFB1827B73E262A5F33C784646C83C94886C1BEA6225C2E0C32B0917BA017D2B1AF8E99058EB95440BA2302FAA0917BE820B79C1FCBBA3307959444BCC015D39116A4E9BCFF679CA876E554689147E9BCBB708343B6317329D6C433FFD8E7C457A70779D883F1F380F2DC16DF6395DE1
			37B6295FA0B06CF220FE93684EAA3E587B9B08E5EC1D9A8C7943A6D0FCEDA84322A0BDCE54FB077DB6414CFEF80F1FA31BB1542B4668B6A5D29BF3A7D0AC69D46205FC6B10B2914EBE933538F666C46D0C417B3033B5B18770120994CF1C5E4546F2A2719D4A563E61A82F40E299B69852438456445643C8C8C78B6C398DD4EE76C412A1A37BC2622D3EB2C813628D4B3CE408E7EBE9AA609913C8061BBA19FDCE92B1F2362660F7902D82DB4156948F247B896CB60605E3BB827BDDB3151BB109E478306567FCF3
			12G6D2C70124143560A1498587FE2C8066549A8C3C58C717194DB0BAE453109213B4B174C12E86394DB171C1057AD727F5508791C676B8AAF64BB92DF1EA3E2786FDBB41ED7B0FEA2810F96BCCFE2F17A5902673EECFD3A8747E7B271344122713C194568B3A1AFC7649BE50D67464A1F739DC75DE4FEB726717C96DB739AG37CDA61EB7ECE47F0FE6B20D6D180D67E31F54F794540B192C45EB63607DB3F819AC09D820856DD5E9D4C35A874C6DA8A00F9FE71E51470FE7CF665F4965BD836B7EB334C64CA8CE
			0467769DF44F5C52AC09366D6CBB5F6CAAB8D63A4CB6AD56FBC50BD6662F06E031326DF9D9C5C5A2CE32BF51373D40315B57DB357734D3DAFF61DD675F5F1CAEBC8373380C77CF86F3C41961E7D17E52E26A2B38CD6BEB5BB99C07EC76BDC5361AEF36E97937EE8896FBAA7562A5E1C34F367A35EBB28E37EB6DB6F71EFF3BA3BBFB17713F4B599817D2DF939DDADF4E4F715B87FDACB58B3F1B907154571DDA59BB1ABF8C4CD7E1618E4E189A67ED895E6E87AECD769A3756476F971FFF9F1B12D7F2593D4CDF13
			FBC6B213FB9B75F37A0C564F4A42207D9473FEF83DDBBBB5DE47FA34B6EEB01ECF9B3C6CAB46603C5ED84D6CD070FA43F62D1FBFD6846B2764B3BD2F0F6C50F8FDD977CD466B59675386AF3BE3E3A0DEAFAC96A331EFBB785ABF3C23C83A405F9B72FF6D23460A6A4E50F7F8471E21D8D17847F8AF968A69882068E748BF7C668C5E4BCDFF467F5D5EEC4025E7E1AFFC5677B7BC991B597D5B333EF7289B98DE722C6F9D6A5D9F539DEA856405BFCBBE2D13711AB2857C70A9043D51C9FEEE0540560A7321C5E0E6
			06A9109EB80564D93F9975485FB76B5EC7FEFC714FE993AEEA9F5C5EAEBF03B6520E3F1113687B47DB445EB58C749085B49FA8F5AA1CC540DF657F5FE531074AEC823C9468981087A8ECAA6D3FD54C5F0C1ACA7B1D270B6E9C596788926D5D21CC6F53215C4C29749D4553E7685EB5E9DA3C21FC9A45B8DFBBDD522BADFB7F3F503A7D7C5F46F0DBDE5EC971E3E59A59F2F9A776B582526300E6CC2398EB1EA7C51DC37326517F0B31F8FC7FDF8C9AE63EC47F6F65FA0F6F7F0BF9F3410285DA7EF80F6F7F0B2974
			2A7F3F9E5F7F97033C660B717A268B5B0475C17FD3FBDC9E54F52719FDF7454BDE71F120EFC038FD451C79E971355F77DA4FB73E466B2D6A7CE9711511ECFC721E73DD9B9EE6F869F33EEB432ECEDA9B0C1097719C4D65C20F3E7F54D87049E20347A0545FDE32BD083FBF2EFB5F5BF97157G73C56D032FFBFB191D1700AE5096B9361FE1A640D039FC6E19591A2B7E8E0E058DB81E69D9266194B610787FF07EC7D5DFD884C63CAEADD2240FEBE8E2E6ED4D52BE3861DFB4629F1F28EE17343267C6D9F9550B3F
			9BF152181EA8A556C80BDBD9AFB55222CEE443637C91A699127B3474830E3C81B8C83CDC115E51FDB0C9F9CC724FFDD2AEE905683306C5A6624E9417AD7C539D8B9325A46810C7EAEA24A48C37C03FAE5E7FC2456174B8A5BE53D18B68C9640936F19EA431947D7284E7A6FEF5172BC8297A0F683C58297213CAD6BB49D49B49831617FB4FED431DEBAECD3B3A38A3D1CA59ADADEEB65B1C2DE6E0157D63B61374CE230BFF6F88E595A94D04F5BF053A1F323ABBCDD2E213DB11BE5070A644F96EE12739C906A2CB
			ACE6D79BEB81D9CA7BD6DA526CEAE9E49DA8D262373B3F05EED98A33D71A2425CAAB7B9D386CD194E90D56F4ADFE1721E66CE47A13E07096B8591F909EADE8071F942A02FDE985047FD5B9472B4CD21E723588A99448A66A1B553142084A32GB526009DCD96B5960BB9C9B5B005B3G5D453BB1D711D6D45AECD95CEF43E1F57BC3D6738F1DF5E00EBBB7ABCB11D676480052496A11CDF005E70EDDDE7D23112C50A427B20A34089DF93C50B2D1F329DA8B3F1FDB64DD864C43777060D520C3B40216088E6B129E
			5DF7F825CEFB55D8623746E92F54F5BDEA2C6AD1C3AB437E918650FBE2422D70B6AA7A0EB677C77DF73473AF9B93707F17043F4877062897FD7F276221BD38B64E437FDB7FFB0A875F3EFB2B9478FF009DEEB75BEC0A5956D6E937607F835B5525485A6FDE0F6E41765DD4C64D64F9A776A05F27F4F57E9FD0CB8788FF798DEAD6A0GGF8DFGGD0CB818294G94G88G88G4E0236ACFF798DEAD6A0GGF8DFGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GG
			GG81G81GBAGGG10A0GGGG
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
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the comboDestination property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboDest()
	{
		if (ivjcomboDest == null)
		{
			try
			{
				ivjcomboDest = new JComboBox();
				ivjcomboDest.setName("comboDest");
				ivjcomboDest.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboDest.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboDest.setBackground(java.awt.Color.white);
				ivjcomboDest.setMaximumSize(
					new java.awt.Dimension(55, 23));
				ivjcomboDest.setPreferredSize(
					new java.awt.Dimension(55, 23));
				ivjcomboDest.setMinimumSize(
					new java.awt.Dimension(55, 23));
				//ivjcomboDest.setNextFocusableComponent(
				//	getpnlRcveInto());
				// user code begin {1}
				getcomboDest().addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboDest;
	}

	/**
	 * Return the FRMReceiveInvoiceINV004ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmRcveInvcINV004ContentPane1()
	{
		if (ivjFrmRcveInvcINV004ContentPane1 == null)
		{
			try
			{
				ivjFrmRcveInvcINV004ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmRcveInvcINV004ContentPane1.setName(
					"FrmRcveInvcINV004ContentPane1");
				ivjFrmRcveInvcINV004ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmRcveInvcINV004ContentPane1.setMaximumSize(
					new java.awt.Dimension(647, 372));
				ivjFrmRcveInvcINV004ContentPane1.setMinimumSize(
					new java.awt.Dimension(647, 372));

				java.awt.GridBagConstraints constraintsstcLblInvcNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvcNo.gridx = 1;
				constraintsstcLblInvcNo.gridy = 1;
				constraintsstcLblInvcNo.ipadx = 25;
				constraintsstcLblInvcNo.insets =
					new java.awt.Insets(44, 13, 17, 2);
				getFrmRcveInvcINV004ContentPane1().add(
					getstcLblInvcNo(),
					constraintsstcLblInvcNo);

				java.awt.GridBagConstraints constraintstxtInvcNo =
					new java.awt.GridBagConstraints();
				constraintstxtInvcNo.gridx = 2;
				constraintstxtInvcNo.gridy = 1;
				constraintstxtInvcNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtInvcNo.weightx = 1.0;
				constraintstxtInvcNo.ipadx = 68;
				constraintstxtInvcNo.insets =
					new java.awt.Insets(41, 3, 14, 28);
				getFrmRcveInvcINV004ContentPane1().add(
					gettxtInvcNo(),
					constraintstxtInvcNo);

				java.awt.GridBagConstraints constraintsstcLblDest =
					new java.awt.GridBagConstraints();
				constraintsstcLblDest.gridx = 3;
				constraintsstcLblDest.gridy = 1;
				constraintsstcLblDest.ipadx = 14;
				constraintsstcLblDest.insets =
					new java.awt.Insets(44, 28, 17, 3);
				getFrmRcveInvcINV004ContentPane1().add(
					getstcLblDest(),
					constraintsstcLblDest);

				java.awt.GridBagConstraints constraintscomboDest =
					new java.awt.GridBagConstraints();
				constraintscomboDest.gridx = 4;
				constraintscomboDest.gridy = 1;
				constraintscomboDest.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintscomboDest.weightx = 1.0;
				constraintscomboDest.ipadx = 228;
				constraintscomboDest.insets =
					new java.awt.Insets(40, 4, 12, 42);
				getFrmRcveInvcINV004ContentPane1().add(
					getcomboDest(),
					constraintscomboDest);

				java.awt.GridBagConstraints constraintspnlRcveInto =
					new java.awt.GridBagConstraints();
				constraintspnlRcveInto.gridx = 1;
				constraintspnlRcveInto.gridy = 2;
				constraintspnlRcveInto.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlRcveInto.weightx = 1.0;
				constraintspnlRcveInto.weighty = 1.0;
				constraintspnlRcveInto.ipadx = -15;
				constraintspnlRcveInto.ipady = -53;
				constraintspnlRcveInto.insets =
					new java.awt.Insets(13, 2, 97, 6);
				getFrmRcveInvcINV004ContentPane1().add(
					getpnlRcveInto(),
					constraintspnlRcveInto);

				java
					.awt
					.GridBagConstraints constraintspnlVerificationOn =
					new java.awt.GridBagConstraints();
				constraintspnlVerificationOn.gridx = 2;
				constraintspnlVerificationOn.gridy = 2;
				constraintspnlVerificationOn.gridwidth = 3;
				constraintspnlVerificationOn.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlVerificationOn.weightx = 1.0;
				constraintspnlVerificationOn.weighty = 1.0;
				constraintspnlVerificationOn.ipadx = 14;
				constraintspnlVerificationOn.ipady = -26;
				constraintspnlVerificationOn.insets =
					new java.awt.Insets(13, 7, 6, 17);
				getFrmRcveInvcINV004ContentPane1().add(
					getpnlVerificationOn(),
					constraintspnlVerificationOn);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 2;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.gridwidth = 3;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 58;
				constraintsButtonPanel1.ipady = 27;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(7, 54, 5, 170);
				getFrmRcveInvcINV004ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjFrmRcveInvcINV004ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				getJScrollPane1().setViewportView(gettblSubstaName());
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
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlRcveInto()
	{
		if (ivjpnlRcveInto == null)
		{
			try
			{
				ivjpnlRcveInto = new JPanel();
				ivjpnlRcveInto.setName("pnlRcveInto");
				ivjpnlRcveInto.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_RECEIVE_INTO_COLON));
				ivjpnlRcveInto.setLayout(new java.awt.GridBagLayout());
				ivjpnlRcveInto.setMaximumSize(
					new java.awt.Dimension(125, 144));
				ivjpnlRcveInto.setMinimumSize(
					new java.awt.Dimension(125, 144));

				java.awt.GridBagConstraints constraintsradioCntrl =
					new java.awt.GridBagConstraints();
				constraintsradioCntrl.gridx = 1;
				constraintsradioCntrl.gridy = 1;
				constraintsradioCntrl.insets =
					new java.awt.Insets(10, 14, 5, 15);
				getpnlRcveInto().add(
					getradioCntrl(),
					constraintsradioCntrl);

				java.awt.GridBagConstraints constraintsradioStock =
					new java.awt.GridBagConstraints();
				constraintsradioStock.gridx = 1;
				constraintsradioStock.gridy = 2;
				constraintsradioStock.ipadx = 6;
				constraintsradioStock.insets =
					new java.awt.Insets(5, 15, 10, 16);
				getpnlRcveInto().add(
					getradioStock(),
					constraintsradioStock);
				// user code begin {1}
				// Add the radio buttons to a button group so they 
				// are mutually exclusive.
				// defect 7890
				// Changed from a ButtonGroup to a RTSButtonGroup
				RTSButtonGroup laRadioGrpRcveInto = new RTSButtonGroup();
				laRadioGrpRcveInto.add(getradioCntrl());
				laRadioGrpRcveInto.add(getradioStock());
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlRcveInto;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlVerificationOn()
	{
		if (ivjpnlVerificationOn == null)
		{
			try
			{
				ivjpnlVerificationOn = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints4 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints4.insets =
					new java.awt.Insets(4, 20, 15, 21);
				consGridBagConstraints4.ipady = -289;
				consGridBagConstraints4.ipadx = -23;
				consGridBagConstraints4.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints4.weighty = 1.0;
				consGridBagConstraints4.weightx = 1.0;
				consGridBagConstraints4.gridwidth = 3;
				consGridBagConstraints4.gridy = 1;
				consGridBagConstraints4.gridx = 0;
				consGridBagConstraints2.insets =
					new java.awt.Insets(10, 9, 3, 9);
				consGridBagConstraints2.ipady = -2;
				consGridBagConstraints2.ipadx = 7;
				consGridBagConstraints2.gridy = 0;
				consGridBagConstraints2.gridx = 1;
				consGridBagConstraints1.insets =
					new java.awt.Insets(10, 18, 3, 9);
				consGridBagConstraints1.ipady = -2;
				consGridBagConstraints1.ipadx = 7;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				consGridBagConstraints3.insets =
					new java.awt.Insets(10, 9, 3, 9);
				consGridBagConstraints3.ipady = -2;
				consGridBagConstraints3.ipadx = 21;
				consGridBagConstraints3.gridy = 0;
				consGridBagConstraints3.gridx = 2;
				ivjpnlVerificationOn.setName("pnlVerificationOn");
				ivjpnlVerificationOn.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_VERIFICATION_ON_COLON));
				ivjpnlVerificationOn.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlVerificationOn.add(
					getradioDestOnly(),
					consGridBagConstraints1);
				ivjpnlVerificationOn.add(
					getradioAllSubstas(),
					consGridBagConstraints2);
				ivjpnlVerificationOn.add(
					getradioSelectSubstas(),
					consGridBagConstraints3);
				ivjpnlVerificationOn.add(
					getJScrollPane1(),
					consGridBagConstraints4);
				ivjpnlVerificationOn.setMaximumSize(
					new java.awt.Dimension(461, 208));
				ivjpnlVerificationOn.setMinimumSize(
					new java.awt.Dimension(461, 208));

				// Add the radio buttons to a button group so they are
				// mutually exclusive.
				// defect 7890
				// Changed from ButtonGroup to RTS buttonGroup
				RTSButtonGroup laRadioGrpVerificationOn = 
					new RTSButtonGroup();
				laRadioGrpVerificationOn.add(getradioDestOnly());
				laRadioGrpVerificationOn.add(getradioAllSubstas());
				laRadioGrpVerificationOn.add(getradioSelectSubstas());
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlVerificationOn;
	}

	/**
	 * Return the radioAllDestinations property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioAllSubstas()
	{
		if (ivjradioAllSubstas == null)
		{
			try
			{
				ivjradioAllSubstas = new JRadioButton();
				ivjradioAllSubstas.setName("radioAllSubstas");
				ivjradioAllSubstas.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjradioAllSubstas.setText(
					InventoryConstant.TXT_ALL_SUBSTATIONS);
				ivjradioAllSubstas.setMaximumSize(
					new java.awt.Dimension(113, 22));
				ivjradioAllSubstas.setActionCommand(
					InventoryConstant.TXT_ALL_SUBSTATIONS);
				ivjradioAllSubstas.setMinimumSize(
					new java.awt.Dimension(113, 22));
				//ivjradioAllSubstas.setNextFocusableComponent(
				//	gettblSubstaName());
				// user code begin {1}
				getradioAllSubstas().addActionListener(this);
				// defect 7890
				//getradioAllSubstas().addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioAllSubstas;
	}

	/**
	 * Return the radioCentral property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioCntrl()
	{
		if (ivjradioCntrl == null)
		{
			try
			{
				ivjradioCntrl = new JRadioButton();
				ivjradioCntrl.setName("radioCntrl");
				ivjradioCntrl.setMnemonic(java.awt.event.KeyEvent.VK_C);
				ivjradioCntrl.setText(InventoryConstant.TXT_CENTRAL);
				ivjradioCntrl.setMaximumSize(
					new java.awt.Dimension(66, 22));
				ivjradioCntrl.setActionCommand(
					InventoryConstant.TXT_CENTRAL);
				ivjradioCntrl.setMinimumSize(
					new java.awt.Dimension(66, 22));
				//ivjradioCntrl.setNextFocusableComponent(
				//	getpnlVerificationOn());
				// user code begin {1}
				// defect 7890
				//getradioCntrl().addKeyListener(this);
				// end defect 7890
				// defect 7514
				getradioCntrl().addActionListener(this);
				// end defect 7514
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCntrl;
	}

	/**
	 * Return the radioDestinationOnly property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioDestOnly()
	{
		if (ivjradioDestOnly == null)
		{
			try
			{
				ivjradioDestOnly = new JRadioButton();
				ivjradioDestOnly.setName("radioDestOnly");
				ivjradioDestOnly.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjradioDestOnly.setText(
					InventoryConstant.TXT_DESTINATION_ONLY);
				ivjradioDestOnly.setMaximumSize(
					new java.awt.Dimension(117, 22));
				ivjradioDestOnly.setActionCommand(
					InventoryConstant.TXT_DESTINATION_ONLY);
				ivjradioDestOnly.setMinimumSize(
					new java.awt.Dimension(117, 22));
				//ivjradioDestOnly.setNextFocusableComponent(
				//	gettblSubstaName());
				// user code begin {1}
				getradioDestOnly().addActionListener(this);
				// defect 7890
				//getradioDestOnly().addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDestOnly;
	}

	/**
	 * Return the radioSelectedDestinations property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSelectSubstas()
	{
		if (ivjradioSelectSubstas == null)
		{
			try
			{
				ivjradioSelectSubstas = new JRadioButton();
				ivjradioSelectSubstas.setName("radioSelectSubstas");
				ivjradioSelectSubstas.setMnemonic(java.awt.event.KeyEvent.VK_U);
				ivjradioSelectSubstas.setText(
					InventoryConstant.TXT_SELECT_SUBSTATIONS);
				ivjradioSelectSubstas.setMaximumSize(
					new java.awt.Dimension(149, 22));
				ivjradioSelectSubstas.setActionCommand(
					InventoryConstant.TXT_SELECT_SUBSTATIONS);
				ivjradioSelectSubstas.setMinimumSize(
					new java.awt.Dimension(149, 22));
				//ivjradioSelectSubstas.setNextFocusableComponent(
				//	gettblSubstaName());
				// user code begin {1}
				getradioSelectSubstas().addActionListener(this);
				// defect 7890
				//getradioSelectSubstas().addKeyListener(this);
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSelectSubstas;
	}

	/**
	 * Return the radioStock property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioStock()
	{
		if (ivjradioStock == null)
		{
			try
			{
				ivjradioStock = new JRadioButton();
				ivjradioStock.setName("radioStock");
				ivjradioStock.setMnemonic(java.awt.event.KeyEvent.VK_S);
				ivjradioStock.setText(InventoryConstant.STR_STOCK);
				ivjradioStock.setMaximumSize(
					new java.awt.Dimension(58, 22));
				ivjradioStock.setActionCommand(
					InventoryConstant.STR_STOCK);
				ivjradioStock.setMinimumSize(
					new java.awt.Dimension(58, 22));
				//ivjradioStock.setNextFocusableComponent(
				//	getpnlVerificationOn());
				// user code begin {1}
				// defect 7890
				//getradioStock().addKeyListener(this);
				// end defect 7890
				// defect 7514
				getradioStock().addActionListener(this);
				// end defect 7514
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioStock;
	}

	/**
	 * Return the stcLblDestination property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDest()
	{
		if (ivjstcLblDest == null)
		{
			try
			{
				ivjstcLblDest = new JLabel();
				ivjstcLblDest.setName("stcLblDest");
				ivjstcLblDest.setText(
					InventoryConstant.TXT_DESTINATION_COLON);
				ivjstcLblDest.setMaximumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDest.setMinimumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDest.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblDest;
	}

	/**
	 * Return the stcLblInvoiceNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvcNo()
	{
		if (ivjstcLblInvcNo == null)
		{
			try
			{
				ivjstcLblInvcNo = new JLabel();
				ivjstcLblInvcNo.setName("stcLblInvcNo");
				ivjstcLblInvcNo.setText(InventoryConstant.TXT_INVOICE_NO_COLON);
				ivjstcLblInvcNo.setMaximumSize(
					new java.awt.Dimension(61, 14));
				ivjstcLblInvcNo.setMinimumSize(
					new java.awt.Dimension(61, 14));
				ivjstcLblInvcNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblInvcNo;
	}

	/**
	 * Return the tblSubstaName property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSubstaName()
	{
		if (ivjtblSubstaName == null)
		{
			try
			{
				ivjtblSubstaName = new RTSTable();
				ivjtblSubstaName.setName("tblSubstaName");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSubstaName.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSubstaName.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.inventory
						.ui
						.TMINV004());
				ivjtblSubstaName.setBounds(0, -8, 160, 128);
				//ivjtblSubstaName.setNextFocusableComponent(
				//	getButtonPanel1());
				ivjtblSubstaName.setGridColor(java.awt.Color.white);
				// user code begin {1}
				caTableModel = (TMINV004) ivjtblSubstaName.getModel();
				TableColumn laTCa =
					ivjtblSubstaName.getColumn(
						ivjtblSubstaName.getColumnName(0));
				laTCa.sizeWidthToFit();
				ivjtblSubstaName.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSubstaName.init();

				laTCa.setCellRenderer(
					ivjtblSubstaName.setColumnAlignment(RTSTable.LEFT));

				ivjtblSubstaName.addMouseListener(this);
				ivjtblSubstaName.addActionListener(this);
				//ivjtblSubstaName.setNextFocusableComponent(
				//	getButtonPanel1().getBtnEnter());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSubstaName;
	}

	/**
	 * Return the txtInvoiceNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtInvcNo()
	{
		if (ivjtxtInvcNo == null)
		{
			try
			{
				ivjtxtInvcNo = new RTSInputField();
				ivjtxtInvcNo.setName("txtInvcNo");
				ivjtxtInvcNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtInvcNo.setMaximumSize(
					new java.awt.Dimension(4, 30));
				ivjtxtInvcNo.setInput(RTSInputField.ALPHANUMERIC_ONLY);
				//ivjtxtInvcNo.setNextFocusableComponent(getcomboDest());
				ivjtxtInvcNo.setMaxLength(
					InventoryConstant.INVOICE_NUMBER_LENGTH);
				ivjtxtInvcNo.setHorizontalAlignment(
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
		return ivjtxtInvcNo;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
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
			// defect 7890
			// Initializes the Radio Button arrays with all the 
			// radio buttons.
			//carrVerfctnOnRadioButton[CommonConstant.ELEMENT_0] =
			//	getradioDestOnly();
			//carrVerfctnOnRadioButton[CommonConstant.ELEMENT_1] =
			//	getradioAllSubstas();
			//carrVerfctnOnRadioButton[CommonConstant.ELEMENT_2] =
			//	getradioSelectSubstas();
			// end defect 7890
			// user code end
			setName(ScreenConstant.INV004_FRAME_NAME);
			setSize(600, 350);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV004_FRAME_TITLE);
			setContentPane(getFrmRcveInvcINV004ContentPane1());
			
			// defect 7890
			// This is set to false so that when a hot key is pressed to
			// activate a radio button the focus is moved to the text
			// field and not back to the radio button.
			setRequestFocus(false);
			// defect 7890
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * When a different substation is select in the destination combo 
	 * box, this method clears the rows selected in the Select 
	 * Substation(s) table and selects the row corrisponding to 
	 * the destination substation.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		ciDestRowIndx = getcomboDest().getSelectedIndex();
		gettblSubstaName().unselectAllRows();
		gettblSubstaName().setSelectedRow(ciDestRowIndx);
	}

	/**
	 * Handles Key events for this frame.
	 *
	 * @param aaKE KeyEvent  
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		// defect 7890
		//if (aaKE.getSource() instanceof JRadioButton)
		//{
		//	if (aaKE.getSource() == getradioCntrl()
		//		|| aaKE.getSource() == getradioStock())
		//	{
		//		if (aaKE.getKeyCode() == KeyEvent.VK_UP
		//			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
		//			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
		//			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		//		{
		//			if (getradioCntrl().hasFocus())
		//			{
		//				//getradioStock().setSelected(true);
		//				getradioStock().requestFocus();
		//			}
		//			else
		//			{
		//				//getradioCntrl().setSelected(true);
		//				getradioCntrl().requestFocus();
		//			}
		//		}
		//	}
		//	else
		//	{
		//		for (int i = 0; i < 3; i++)
		//		{
		//			if (carrVerfctnOnRadioButton[i].hasFocus())
		//			{
		//				ciSelctdVerfctnOnRadioButton = i;
		//				break;
		//			}
		//		}
		//		if (aaKE.getKeyCode() == KeyEvent.VK_UP
		//			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
		//		{
		//			if (ciSelctdVerfctnOnRadioButton == 0)
		//			{
		//				ciSelctdVerfctnOnRadioButton = 2;
		//			}
		//			else
		//			{
		//				ciSelctdVerfctnOnRadioButton =
		//					ciSelctdVerfctnOnRadioButton - 1;
		//			}
		//		}
		//		else if (
		//			aaKE.getKeyCode() == KeyEvent.VK_DOWN
		//				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		//		{
		//			if (ciSelctdVerfctnOnRadioButton == 2)
		//			{
		//				ciSelctdVerfctnOnRadioButton = 0;
		//			}
		//			else
		//			{
		//				ciSelctdVerfctnOnRadioButton =
		//					ciSelctdVerfctnOnRadioButton + 1;
		//			}
		//		}
		//		//carrVerfctnOnRadioButton[ciSelctdVerfctnOnRadioButton]
		//		//	.setSelected(
		//		//	true);
		//		carrVerfctnOnRadioButton[ciSelctdVerfctnOnRadioButton]
		//			.requestFocus();
		//		// defect 7890
		//		// do not do this for cursor movement
		//		//setpnlVerificationOn();
		//		// end defect 7890
		//	}
		//}
		//else if (aaKE.getSource() instanceof RTSButton)
		//{
		//	if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
		//		|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		//	{
		//		if (getButtonPanel1().getBtnEnter().hasFocus())
		//		{
		//			getButtonPanel1().getBtnCancel().requestFocus();
		//		}
		//		else if (getButtonPanel1().getBtnCancel().hasFocus())
		//		{
		//			getButtonPanel1().getBtnHelp().requestFocus();
		//		}
		//		else if (getButtonPanel1().getBtnHelp().hasFocus())
		//		{
		//			getButtonPanel1().getBtnEnter().requestFocus();
		//		}
		//		aaKE.consume();
		//	}
		//	else if (
		//		aaKE.getKeyCode() == KeyEvent.VK_LEFT
		//			|| aaKE.getKeyCode() == KeyEvent.VK_UP)
		//	{
		//		if (getButtonPanel1().getBtnCancel().hasFocus())
		//		{
		//			getButtonPanel1().getBtnEnter().requestFocus();
		//		}
		//		else if (getButtonPanel1().getBtnHelp().hasFocus())
		//		{
		//			getButtonPanel1().getBtnCancel().requestFocus();
		//		}
		//		else if (getButtonPanel1().getBtnEnter().hasFocus())
		//		{
		//			getButtonPanel1().getBtnHelp().requestFocus();
		//		}
		//		aaKE.consume();
		//	}
		//}
		//Code that prevents the destination substation from being
		// unselected in the table
		//else 
		// end defect 7890
		if (aaKE.getSource() instanceof RTSTable)
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_SPACE)
			{
				if (gettblSubstaName().getSelectedRow()
					== ciDestRowIndx)
				{
					gettblSubstaName().setSelectedRow(ciDestRowIndx);
				}
				aaKE.consume();
			}
		}
	}

//	/**
//	 * Handles KeyReleased Events
//	 *
//	 * @param aaKE KeyEvent  
//	 */
//	public void keyReleased(KeyEvent aaKE)
//	{
//		super.keyReleased(aaKE);
//
//		// defect 7890 
//		// this code is no longer required as of 5.2.3!!!
//		// This code is also in setpnlVerificationOn().  It has to be
//		// here because when super.keyPressed() is called, the 
//		// 'Select Substation(s)' radio button requests focus and 
//		// overrides the focus that was set to either the table or 
//		// input field.  This only really applies when the user uses
//		// the mneomonics.
//		//if (aaKE.getSource() instanceof JRadioButton)
//		//{
//		//	if (getradioSelectSubstas().hasFocus())
//		//	{
//		//		if (gettxtInvcNo()
//		//			.getText()
//		//			.equals(CommonConstant.STR_SPACE_EMPTY))
//		//		{
//		//			gettxtInvcNo().requestFocus();
//		//		}
//		//		else
//		//		{
//		//			gettblSubstaName().requestFocus();
//		//		}
//		//	}
//		//}
//		// end defect 7890 
//	}

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
			FrmReceiveInvoiceINV004 laFrmReceiveInvoiceINV004;
			laFrmReceiveInvoiceINV004 = new FrmReceiveInvoiceINV004();
			laFrmReceiveInvoiceINV004.setModal(true);
			laFrmReceiveInvoiceINV004
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmReceiveInvoiceINV004.show();
			java.awt.Insets insets =
				laFrmReceiveInvoiceINV004.getInsets();
			laFrmReceiveInvoiceINV004.setSize(
				laFrmReceiveInvoiceINV004.getWidth()
					+ insets.left
					+ insets.right,
				laFrmReceiveInvoiceINV004.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmReceiveInvoiceINV004.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseClicked(MouseEvent aaME)
	{
		// Code that prevents the destination substation from being
		// unselected in the table
		if (aaME.getSource() instanceof RTSTable)
		{
			gettblSubstaName().setSelectedRow(ciDestRowIndx);
			aaME.consume();
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseEntered(MouseEvent aaME)
	{
		// empty code block
	}

	/**
	 * Invoked when the mouse exits a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseExited(MouseEvent aaME)
	{
		// empty code block
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mousePressed(MouseEvent aaME)
	{
		// Code that prevents the destination substation from being 
		// unselected in the table
		if (aaME.getSource() instanceof RTSTable)
		{
			gettblSubstaName().setSelectedRow(ciDestRowIndx);
			aaME.consume();
		}
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseReleased(MouseEvent aaME)
	{
		// empty code block
	}

	/**
	 * Takes the exception thrown from the server, and determines 
	 * which field should turn red and receive focus.
	 * 
	 * @param aRTSExcptn RTSException
	 */
	public void procsExcptn(RTSException aeRTSEx)
	{
		RTSException leRTSEx = new RTSException();

		// If the exception is a validation exception from the 
		// inventory server logic, then perform the color change, 
		// but if not then just display the error.
		if (aeRTSEx.getCode()
			== ErrorsConstant.ERR_NUM_INVOICE_NOT_ON_MF
			|| aeRTSEx.getCode()
				== ErrorsConstant.ERR_NUM_INVOICE_NOT_THIS_COUNTY
			|| aeRTSEx.getCode()
				== ErrorsConstant.ERR_NUM_INVOICE_ALREADY_RECEIVED)
		{
			leRTSEx.addException(aeRTSEx, gettxtInvcNo());
		}
		else if (aeRTSEx.getMsgType().equals(RTSException.MF_DOWN))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN),
				gettxtInvcNo());
		}
		else
		{
			aeRTSEx.displayError(this);
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
		}
	}

	/**
	 * Initialize the destination combo box.
	 */
	private void setcomboDest()
	{

		SubstationData laSubstaData = new SubstationData();

		getcomboDest().removeAllItems();
		for (int i = 0; i < cvSubstaData.size(); i++)
		{
			laSubstaData = (SubstationData) cvSubstaData.get(i);
			getcomboDest().addItem(laSubstaData.getSubstaName());

			// Create vector of Substation names for the table
			cvSubstaName.add(laSubstaData.getSubstaName());
		}
		// defect 8479
		comboBoxHotKeyFix(getcomboDest());
		// end defect 8479
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{

		if (aaData == null)
		{
			return;
		}
		Vector lvDataIn = (Vector) aaData;

		cvSubstaData =
			(Vector) lvDataIn.elementAt(CommonConstant.ELEMENT_0);
		caMFInvAlloctnData =
			(MFInventoryAllocationData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_1);

		setcomboDest();

		getradioCntrl().setSelected(true);
		
		// defect 8597
		if(cvSubstaData.size() == 1)
		{
				getradioAllSubstas().setEnabled(false);
				getradioSelectSubstas().setEnabled(false);
				getradioDestOnly().setSelected(true);
				getcomboDest().setEnabled(false);
		}
		else
		{
			//defect 6792
			//getradioDestOnly().setSelected(true);
			getradioAllSubstas().setSelected(true);
			//end defect 6792
		}
		// defect 8597
		 
		caBorder = getJScrollPane1().getBorder();
		getJScrollPane1().setBorder(null);
		gettblSubstaName().setVisible(false);
		gettxtInvcNo().requestFocus();
	}

	/**
	 * Depending on which radio button is selected, this method 
	 * hides/displays the table on the Verification On panel.
	 */
	private void setpnlVerificationOn()
	{

		if (getradioDestOnly().isSelected())
		{
			getJScrollPane1().setBorder(null);
			gettblSubstaName().setVisible(false);
			getJScrollPane1().setVisible(false);
			caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
				getradioDestOnly().getActionCommand());

		}
		else if (getradioAllSubstas().isSelected())
		{
			getJScrollPane1().setBorder(null);
			gettblSubstaName().setVisible(false);
			getJScrollPane1().setVisible(false);
			caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
				getradioAllSubstas().getActionCommand());

		}
		else if (getradioSelectSubstas().isSelected())
		{
			getJScrollPane1().setBorder(caBorder);
			gettblSubstaName().setVisible(true);
			getJScrollPane1().setVisible(true);
			caTableModel.add(cvSubstaName);

			// Select the destination substation in the list box
			//		ciDestRowIndx = getcomboDest().getSelectedIndex();
			//		gettblSubstaName().setSelectedRow(ciDestRowIndx);

			caMFInvAlloctnData.getMFInvAckData().setVerificationOn(
				getradioSelectSubstas().getActionCommand());
			if (gettxtInvcNo()
				.getText()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				gettxtInvcNo().requestFocus();
			}
			else
			{
				gettblSubstaName().requestFocus();
			}
		}

		if (getradioCntrl().isSelected())
		{
			caMFInvAlloctnData.getMFInvAckData().setRcveInto(
				getradioCntrl().getActionCommand());
		}
		else if (getradioStock().isSelected())
		{
			caMFInvAlloctnData.getMFInvAckData().setRcveInto(
				getradioStock().getActionCommand());
		}
		// defect 7514
		gettxtInvcNo().requestFocus();
		// end defect 7514
	}

	/**
	 * Validate that the invoice number is 6 characters long and it 
	 * only contains numbers except for the first position which can 
	 * contain letters.
	 *
	 * <p>This previous requirement that regional office not receive
	 * dummy invoices is removed by defect 4176.
	 */
	private boolean validateInvcNo()
	{
		boolean lbValidNo = false;

		if (!gettxtInvcNo()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			String lsAlphabet =
				new String(CommonConstant.VALID_LETTERS);
			String lsIntegers = new String(CommonConstant.VALID_INTS);
			String lsInvcNo = gettxtInvcNo().getText().toUpperCase();
			if (lsInvcNo.length()
				== InventoryConstant.INVOICE_NUMBER_LENGTH)
			{
				lbValidNo = true;
				for (int i = 0;
					i < InventoryConstant.INVOICE_NUMBER_LENGTH;
					i++)
				{
					String lChar = lsInvcNo.substring(i, i + 1);
					if (i == 0
						&& lsIntegers.indexOf(lChar)
							== CommonConstant.NOT_FOUND
						&& lsAlphabet.indexOf(lChar)
							== CommonConstant.NOT_FOUND)
					{
						lbValidNo = false;
						break;
					}
					else if (
						i != 0
							&& lsIntegers.indexOf(lChar)
								== CommonConstant.NOT_FOUND)
					{
						lbValidNo = false;
						break;
					}
				}
			}

			// defect 4176
			// Left in place as documentation of business rules.
			// This is not true in RTS I after 4.3.0.  Regions are 
			// now allowed to receive
			// any kind of invoice that the county can.
			//
			// If this is a regional county office, make sure it is 
			// a receive dummy invoices
			//
			// end defect 4176
		}
		else
		{
			lbValidNo = false;
		}

		if (lbValidNo)
		{
			return true;
		}
		else
		{
			RTSException leRTSExMsg = new RTSException();
			leRTSExMsg.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INVALID_INVOICE_NUMBER),
				gettxtInvcNo());
			leRTSExMsg.displayError(this);
			leRTSExMsg.getFirstComponent().requestFocus();
			return false;
		}
	}

	/**
	 * Used to remove the border of the JScrollPane when the window 
	 * opens.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent aaWE)
	{
		super.windowOpened(aaWE);

		// The only purpose of this code residing here is to remove
		// the border of the JScrollPane.  Without this code, when 
		// the screen is initialized the table is not visible but the
		// border is still visible.
		setpnlVerificationOn();
	}
}
