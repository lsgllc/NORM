package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryItemRangeDeleteINV025.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source.
 * 							rename fields.
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3 
 * Min Wang		08/01/2005	Remove item code from screen.
 * 				 			modify captureUserInput(), 
 * 							defect 8269 Ver 5.2.2 Fix 6    
 * Ray Rowehl	08/10/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * In the Delete event, frame INV025 is displayed when the range to be 
 * deleted spans multiple ranges on the database.  Prompts for which 
 * ranges to delete.
 *
 * @version 5.2.3 			10/28/2005
 * @author	Charlie Walker
 * <br>Creation Date: 		06/28/2001 13:01:48
 */

public class FrmInventoryItemRangeDeleteINV025
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JCheckBox ivjchkSelectAllRnges = null;
	private RTSTable ivjtblSelectRnge = null;
	private TMINV025 caTableModel = null;

	/**
	 * Vector used to store the inventory ranges to be displayed on 
	 * INV025
	 */
	private Vector cvInvAlloctnUIData = new Vector();

	/**
	 * InventoryAllocationUIData object that should be deleted
	 */
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * Vector used to store the inventory ranges selected for deletion
	 */
	private Vector cvDelInvAlloctnUIData = new Vector();

	/**
	 * FrmInventoryItemRangeDeleteINV025 constructor comment.
	 */
	public FrmInventoryItemRangeDeleteINV025()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryItemRangeDeleteINV025 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryItemRangeDeleteINV025(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryItemRangeDeleteINV025 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryItemRangeDeleteINV025(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
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
			// If user selects SelectAllItem(s) checkbox
			if (aaAE.getSource() == getchkSelectAllRnges())
			{
				if (getchkSelectAllRnges().isSelected())
				{
					gettblSelectRnge().selectAllRows(
						gettblSelectRnge().getRowCount());
				}
				else
				{
					gettblSelectRnge().unselectAllRows();
				}
			}
			// Determines what actions to take when Allocate, Enter, 
			// Cancel, or Help are pressed.
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				if (!captureUserInput())
				{
					return;
				}

				// Display a delete confirmation box
				if (!dispConfirmationBox())
				{
					return;
				}

				getController().processData(
					AbstractViewController.ENTER,
					cvDelInvAlloctnUIData);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV025);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the Users's Unput.
	 */
	private boolean captureUserInput()
	{
		Vector lvSelctdRows =
			new Vector(gettblSelectRnge().getSelectedRowNumbers());

		// Verify that at least one row has been selected
		if (lvSelctdRows.size() < 1)
		{
			RTSException leRTSExMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_NOTHING_SELECTED_TO_DELETE);
			leRTSExMsg.displayError(this);
			gettblSelectRnge().requestFocus();
			return false;
		}

		// Store the inventory rows selected
		for (int i = lvSelctdRows.size(); i > 0; i--)
		{
			String lsRow = lvSelctdRows.get(i - 1).toString();
			int liRow = Integer.parseInt(lsRow);
			caInvAlloctnUIData =
				(InventoryAllocationUIData) cvInvAlloctnUIData.get(
					liRow);
			// Create the itm code description
			ItemCodesData laICD =
				ItemCodesCache.getItmCd(caInvAlloctnUIData.getItmCd());
			// defect 8269
			//caInvAlloctnUIData.setItmCdDesc(
			//	caInvAlloctnUIData.getItmCd()
			//		+ " - "
			//		+ laICD.getItmCdDesc());
			caInvAlloctnUIData.setItmCdDesc(laICD.getItmCdDesc());
			// end defect 8269
			cvDelInvAlloctnUIData.addElement(caInvAlloctnUIData);
		}
		return true;
	}

	/**
	 * Display the Confirmation Box.
	 * If the user selects Yes, return true.
	 * Return false in all other situations.
	 * 
	 * @return boolean
	 */
	private boolean dispConfirmationBox()
	{
		String lsMsgType = new String();
		String lsMsg = new String();
		int liYesNo = 0;
		boolean lbReturn = false;

		// Prompts the user if these are the correct values.
		lsMsgType = RTSException.CTL001;
		lsMsg = ErrorsConstant.MSG_INV_DELETE_CONFIRM;
		RTSException leRTSExMsg =
			new RTSException(lsMsgType, lsMsg, null);
		liYesNo = leRTSExMsg.displayError(this);

		if (liYesNo == RTSException.YES)
		{
			lbReturn = true;
		}
		return lbReturn;
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
			D0CB838494G88G88GB7F3D4B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BB8BF0D457F572B8241A06A4B0C392EA54D4E9300DDD1A601AF8E807F6C8C3D247E6EA6C5006F814B1C9E94BA436C327F243CC15786AAF4C0245A7369CBE16C1A0BEC28890A0EBA5507F47CAABCB0BD8C032F852BE6DBE696D3E555B375ADD71F14F39775D37CF2BDD8928514C197B6E391F7339671EF36E3DAB326743545C4C2CBD04E466927D6FC2A6A11633047C7649407B9CD7D0EC3590535FC7G
			BBC90FB3874AA2E82B6931DA8A09B8DEG75B22827F4DBADB8F8B7999DF7D540C782BAB5507666522F59E7267344E71CCEBE2DBF9542CEBBGAAG67GC8ADC67A5F8F956978B054F7685F8819D904D4FD04FD3EBCD0E442EF547B15FC444A620358A7AD34CFEFCF827CC200F400020351B937D26E66D4D769757E7929A47D0563D95B1116E9FE8E9EFD6CDC3DACA6FFC39511CD5A7ACEC3D953BD663EA879A6C41F2628116A6A96555B4EEB6D1A686D91FCEE31C914C5CDEC6F6A2BB8F38E46E4D0DE1F9DA82073
			90329A6A0C2F17375768F4970C25927993DBDEA23E6A8EB1FEDC0722FC25EF1B13372784674E8DDB74F10B21BD8D0071BCBA5949F99ED63C65DA5825E865D240D52EEA0172E1D9823E4B8DE14A03527D4853996672B8010775140F9F75A39F7360FB89402A92AEE7EDA91753F96B3BE44BD0E7563B89645CF7184957F4B8AA27BCF7FD4E87541ECA2F20CDA4C15BE20095GE9G99G7B0ED8ADFB299D3F7159F1A86B0734C6456BD5FC75FEFF036CD2BB9517A843F7F5B53468389EF1D8D1DD04B0DBDDB1D09607
			9F8E15C7265AE954362E92EEDBCBFB1EA0A5EFF566654611AD69671DF9ED7A9C7DC72232B139FE5007F25D867CC200E5G4F83EC82A8A865F215B94E4EA6D73BCF93D51FA07786077C022AB584A559A52A3AFC5B42B349978F58388F6EFB9C534B1233E63D24BD5B15159FC7AFE9CB4EE61514B2FD5414C67542E877A4245DDFBAFDBD281ECB518E2B87508E7D701D295BA843B7D17CEA931E6D1FB554EE313E8E5A2A0EF23BF56E60B258F29EA305276DF1E5D8780FCDD9A9C799BF4B0F462E6DDFB851C7EDGFC
			86401EE3D6CB99008D4083F07B98DF5F106354BD5AED37E9ED59580AAB6FC1A906B5D5E87785B441B7AC76892AA48C49E20068FDBE3E023C8FCD99F7116E27E12051AA7AC4D550A44587338BDE586EEA0075E93D32937B28A2EDF5C8DE9188F87D044FEB3F329BCAD988E8D77CAEC19323BDD87B55AB284B89218FE6048960FB4DB15479CB549F2640778AG262BB55790D7F85CEAB9FA1C6B6515AB3B203C8C06AD4A5555CD52B032A12891CEFFCD9F7A9291FDD6136897FDAE788868EDBF695B9A554B08E2707C
			B30A47BD52880C878C7CEBFDBB999E66B0EB19377FA7ED97B4CD1506021A58ABEA1A64F3C7477F06729218327D82C45F63575146EAC0AE87004B10AFFCF537C9BE1B220C857D8EDCB9DDCF927533E168BF6FB80F2BF58398E7179F67F67A26135BE9537647C9593FF44635539470C19B0FB37B4CB89E3B57BA681CD19A2F74BB936C3D6547636F3D7B77AD3FCEC8A37B7FB9BF82537BA23A37CBCED8ADF5GB7CFF03DBFFB35186D3786A1A0729DE6D7E145C4D7544AD85F271C09EDEC19B33E0DBD670CEFE3AB1D
			B35B50EAE7BC9BE2EDAFBAB136A8A11F49A657BAF751BD6C730914E9E388E1B9401B8E54056413342B124BADEA3D5224C8C86A89342D7F20FBB08C5FF3CFF0DB8A9D6236D4B279AD32EA70E8D63CF8367071562C0DA7189DECB99135A546534B57D06710B5F8C1D10DCA5027B17CCF697E56713AFF79717578FDDF3A0E76B8A6C674FEBF391EC7500F48C15110ED19308DF52CCADED8AA5C30FA6E1894B6670EC5E5D68B66197DD07A4BF8EE54F897F356FB49F7189D2D2331A3994627GACAF63F6E43F7101E97F
			6A86542848B2DB8EE31D3E55DF086BAA0E87C5F04F3841AF9B32AD695F955356274B295BD8BF4AA91BFDC3994A79BBA327445841F02F5EC45CFCD056DB59BC96F7961A73DFD720AE6E1AA2941E54736C1331F1EC6BA04E5B84F847C9AE7B0981336F2A8F8414E1090689FDFD72F24E135972CFB70DBCB24DBEFB9C3DCD12A0AB6E86A54C734CDFD17F07F2F8CE72BC731B37F0ADD2213E82E02D414F2B83536D6B3501F7209C1154G64C7AEB14CF57A6F83B8A746AD9D4D7339B3E6DD2D5B5595B71FC358246BA8
			E31A2E6CF48D0A4AA15617F35EEEFE3A5324AB48BDA1CC3BE1A5B9BFBB8633193D6B75DD03395C5E8977173F2179D0DDF9FC7F6B50719E3D3C8DA54BFBF65D26F9F9F9ECBE340F62D34BE373214537F8BE348C5A761C627139D9207E826A492738DCCF8EEEB749552DC8BE4D1049F193772C22CAEE4947F157A88EB2EF6A44D80C4B0B603C8BE14E15G8C77A9452D057AA6837773C944D9AA2C16628A0EBBF28BF115D0EF2B60312B68B67A8E07D1BFC86B1AD1FF7AD62238732000342646369FFC6EB44CB11169
			32C340F58B9B75AEDA0F68FDF7137D11BFC06992C704208CE1C88908F63F1E6681FEDD856A2F0A6A9947AD2C603E7119907A1B91C884A1B4757A45E13274B4768DC638DFAD02FA99008DC086188B40E826CF629CDE45A548860DED37381FCAB77A555E42FE1A2451BD10F3866F93EC9EE270BEA423CF6CE07E20D101238D66E4BE115F8F745EB67B7836B3ACBF1221CCBA43E958A36897C70589A1DC9E8801AC65D7A51FCB8941F418A50393EA328FF6599DED6DDD2D03762E41C61B3D37D9778329A1339F28FA27
			BA214FDCF8062D7DD23D5C58ABE4B25F3548506DC6E85BF796F12513FBE2567411ACC24A21E46D572634234FB8C64C6DDF1A520E3C9E4EB1377F7D1476DC53F8A6570B0779D9BC6345A5C47EDECD423B9057D9A6CF32DE068F8A190BADA41F697660AD3A2754204866FD6906D9DF050F15E4E5A618F715BE5F1A3331F7AC7FFD8C5714468B5322D5D7F760424BCC2775F78AF439C8A50F87BB7351DFD5C13D9B40D3E91CA7A46C1B847514CA6E2F66FBCEC059A70C6A2729E61784411FA600006CD69558A1DA04E1
			E9945677736FA38FF68E7EFEBE5777723F7BCB32620B99195B9268E5E5A553C71ADE96FEA4E4E249E4B9BF063A311C033391008D4085908618F70E4B365E03BCC29E0B6C57EB1AB06C2111102737FD12980A22F1E333392F61D655FD488169F0A27D408C9D30AC7621D108DE8E41CD382FEB39932B1B4567227EF0453969710A451FB9F45D561CE371A75F404F27780DA6BC0BCB6FD3BC56B3206D66F9AE774B924EE96454EC59F4F956CB389FB07F85CFA2CDD267578B9DFDEEA67B08975BD743DC9BGF2AED8AD
			C5GE597785C7D238719CE6368339992EA6ECBB83DCBE3094F8AADE3331F536DE3182F8CA91A26F8F1FE0E3FB206F2EB0A1FA2996E7A985A9804D70F463A6A781BE3E87732B8C251ACCE8E50FD5386F275DF60317335BC9EE313AE705878A52A6345D0DF8D10E6603FC17169D05FD3E535D4D5F17C7E7CFB0DF3B8D7223655B1ED0B75BA23F17E1EE9644F50169BD3677114694C67CD3CEEC16F6C6BF657CBF3CF5037A36297996ED1AF4DFFC6C68222E19F495EC283579E68D68292DAA473295A9D6CAF8B91A588
			7D1F2F42B57ACAAE0FF37DD057GD2G1661B746560F314F7F64954DA0B71B7B1981FA0F63996B9571E8D2AF4BBDF05489986D5F8C607AF6748E2BF0F6C1FFFB1A616BBFC1FCC3906C51C75D70698514668B7966B8637C18711384A50B4F0FA9E61E52747602EA0803553C4FBFF94CFDAA2BD91FB014F381581EFFAE57385B86DCDAB54FD1CE5F44FBB5A3D6148BA12D3A3AD515DC8D025BC6B54862456FF3F98C5EE3657122AC8F6734C15D85F05B4A4F3E61A35CD7AFFD62B132690D7877719B9C673296D819FD
			2E30C6E3D8525C75B9EC1E51B1BE4F3E3FDAC45B924558F4FDFCD1CDF49EEE2B5964E25FDE92B79EB6FA4461B1BCA4B199B38B789949D943E53953E2FEDBD8796E392CC4F1E71ECEFB1909078A10057160EE5E9F1F07282590FED6FBC561FCEC2E613A8EE9286B2335D6CB9D0033162F5F459367487D1C8DCD96E850FB5B43F381FFAD27371B5E77AC017AAA4455F2BF35B277416E3FDFF840F159F977BB0E453ED4EA1B1BEBD96C4BB470CFD1FC06894F764719BC3EBF8AAEC27EFB1167BEEC7F2D057A6BGB917
			E05F8154GB8AF7158365F07CFFEB35EB9BB943F8D1C22EC3A8EE573277BF1FE9966CA86D88A309AE0BD40E6E3FE378F5FAC9265DABD0AC2B3FACC6E8589D61A06D2D54093A453E1DEC12C8EF36AA0BE57B1BBAD03322EEEEA5CE03CFD2A22EFF2C01B9FA099A095E0D51DF177671B75AD25B9BC43DBCA6DB867EB834C199E43C3C1BD683AFEAA0E2D612F69DE28280FBD073FC571F575316770BB9ABF07F7C35B72FAEE4BB952CC36D554G79CBC3BC7AFFC277C6DDC32CED4D23F8DBC32CEDFD504FED4B89EDAB
			9BB87DA7B0CC693A782E693BAA6F73BE73CE5FD71FBCD49A4CBE3E2DA44E851ED6625F77AED3625F77BE274CFC5F7B8FCA627B5E9FAA536FFBFF67CF4CDB4E786CF94A76717879656E716979E551F87C7C724078747CF2BF0DC1450DD6CBE5A34FAFC7A82E8D6ACE8377E2814FB9DD862E0A460D65D0DF5F487768081AD8CE2F9AFF8D82EA7CB50828B33F6BBC22A6DAA3461F034A3B89F833B4F11E7F1A620A215EE6608AD4AEDB3F01BB20F15916B6717D73DF9A1E1DD79A757F257575C67DD16DDEED9C67F830
			FD14F24FB49E64AD06493ED7627A48B0747137B738BEEC4DDC5EAF7793032F2F1B3EBFCC78BE747979132A07CA0365501554663DBE91020DB44C6E44AED35C23G7D4D7C3E6ACE813DD38143E677E1C5F815CA66B6E37FDF96B44DC2478CB20CC1407BBF6A67613BD37F566F46947495BE2055228C878368A3A09A7A951F08E759F4CAF3D4C1FE56997C0D0D63EF05A6C4D51306A1B4528430C1D03B95D99A0E68F7FDDF247B826F1C2ADA783855639817BD0AAACDE22C1FB6125F93EE20E3EB8142B016497D21BA
			1B5C39A639D7423835ADCCEE2CEF046FC570F1A9134B54CA775AE603373943181BEBC3B2BB9761210870BB46322079FCD2584A4E3E5DD03ADAB9FFE72459784BB771F79B464DEB0D7237983EFBF5FDB17EFEF98379DB564A799BAA4032343E14FCC86B10DF10689F331324A0FE333C7F97967359236927991907C87C3CFFDD2B7E5653964D7B57EFF8B50751CDD2707E558D2E502C1A2347B81DDA51FC5FD7F1ABA7ABBB811DFEFDFE39ADFABF4AE87C79F8A519FABFEBBAD51477B577B85A9B6BED033D0DBDF61B
			2D213EE7303EF7302B39392939090D7F6D787999463759FB5A2F5B3B9C71E7A0F1743979D2BC9C53F1F50898FBE7C05B49F4E3BE9623EAF4DBFCB2F8065C673B97FB56534FB52EA0524773DACA9BBF576C71E0AE368E6A2F836434DBAD77477FC0B84A7F169B6FB17E27DD872090AC073E9CC13B5E87F42AG7A81B400E400D40065G4F8324812483D8BA2C16E200CA00B6GA7008C10544163798F03281FC4E7817D60EEBA81F0BE1E19447D304018671D0903B34C5388B9BC3B5D637EDD8AE22E3C24A3FE1CDC
			897835G9B7576AD9D7C4DAC196A5BF2B9B6D71ECF71C517E3F365BFFAF82EDC82ED4917B93F031144F94CC8E476FCCD09444F57021169795AE4A4FE3E1694B167EB8CB7A702366A863DBB0DFEFFEA602E99382F52B991F7D5F2E99E837FB54A97625BC4A448710BA8DFA10AF36A38D4936E1A0EFBBC028787909942829F7BF48457FAC412E5D67F8E6DA3798222E66863D94A236497CCFC2FB0F0D13E3FB709E3833AAD305CA3096E2B57E1ED080D67A3EFCF709C2544409507F90E52E6605C9467043A5F407D42
			0D38AF58227754BB49D7689B16FEE7EA7AAE76BC7CDC666307CA03790EEF847577BDFA8F436EA83FE3444925200B55GB91DF0CE87401C163D9D3EE77C66378270CDGAE00AFGAC86E0312FBD0C6B3F3A13473E3BE1EEFB0D0A0CE6226789DF27EB31917AEDB67AEEF17F916DA928A90D78DBB5C8E664205797E8D195EFA7E3166D53FF8D72B3F5E1972CEF973F0FFA2F89E3517D7CD6565149FEA242FC760189FE4F185445FD36ED82E92D007ADA00CDG2CEFCD0877A5F67EAEB89A0AFD971C1FE9FE57CB8945
			3E8B6278E87BBF07E25F85270EA76158F7C1BE1E69EFB01CA8263178714E9DE2747D199B6762572E65F4F14AC39EC993199D2685E73B7B2A32CF7547EC5C2FE69DF7A02E5D9F1AF8303B36D5F4FD9C76D89F7FE30A3FE90F7571BD935C474B5036564E4FAF75217B253F1F6AF255EE4CBDA51FAC61AD987D9526A1530FA608717D12695BB971707D4878C325417E5F60113B8BC011CB5FA0399AB9E27C862B98FF04D5CCFF05D54CFE06D50CFFEC23E9E4A74BD60AA1DDE11D8281153CEB98E2BB0D949BA64ED6E7
			7F5B373333C8CEBB49E311331D64E078EBA739B4BA3613EC081CE17C95ABC68948F4B3BF05728F01F1F217E469D86493FDD61E014555CA56A18FA86F183A85B29FFF7F51422F1E8DE611029612B7A248FE0FG9469EFF8ECE45B90A50D571C32C68AEDB876940CBDC547DAEDA4EBB820113DD17CB062D9EB2FDF9896214BD617200E519910257C9BE46B086A9E2284B412F52365861025B5EC5EEAA36FE89EFA3D8AA906C65E0FCE5D0DA9165EE025EAA0EF431F1F7E317C7E58E7667CFE4B1C3D89FF7F10544376
			72021E585FC135535F7B2D857C6BBDBC9F7AB367C91278FD3957AB48B21C90476AFDAEFCEBEE08E8E2749E276B9A4E9F60FD74C67DFD749A72BDEA9A73FFD0CB87885D5D14E05492GGB4B3GGD0CB818294G94G88G88GB7F3D4B05D5D14E05492GGB4B3GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG8E93GGGG
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
				//ivjButtonPanel1.setNextFocusableComponent(
				//	getchkSelectAllRnges());
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
	 * Return the chkSelectAllRanges property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSelectAllRnges()
	{
		if (ivjchkSelectAllRnges == null)
		{
			try
			{
				ivjchkSelectAllRnges = new JCheckBox();
				ivjchkSelectAllRnges.setName("chkSelectAllRnges");
				ivjchkSelectAllRnges.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjchkSelectAllRnges.setText(
					InventoryConstant.TXT_SELECT_ALL_RANGES);
				//ivjchkSelectAllRnges.setNextFocusableComponent(
				//	gettblSelectRnge());
				// user code begin {1}
				ivjchkSelectAllRnges.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllRnges;
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
				ivjJDialogContentPane.setLayout(
					new java.awt.GridBagLayout());

				java
					.awt
					.GridBagConstraints constraintschkSelectAllRnges =
					new java.awt.GridBagConstraints();
				constraintschkSelectAllRnges.gridx = 1;
				constraintschkSelectAllRnges.gridy = 1;
				constraintschkSelectAllRnges.ipadx = 38;
				constraintschkSelectAllRnges.insets =
					new java.awt.Insets(21, 18, 6, 350);
				getJDialogContentPane().add(
					getchkSelectAllRnges(),
					constraintschkSelectAllRnges);

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 461;
				constraintsJScrollPane1.ipady = 159;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(6, 23, 6, 24);
				getJDialogContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 41;
				constraintsButtonPanel1.ipady = 20;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(6, 136, 13, 136);
				getJDialogContentPane().add(
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
		return ivjJDialogContentPane;
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
				//ivjJScrollPane1.setNextFocusableComponent(
				//	getButtonPanel1());
				getJScrollPane1().setViewportView(gettblSelectRnge());
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
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSelectRnge()
	{
		if (ivjtblSelectRnge == null)
		{
			try
			{
				ivjtblSelectRnge = new RTSTable();
				ivjtblSelectRnge.setName("tblSelectRnge");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectRnge.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectRnge.setModel(new TMINV025());
				ivjtblSelectRnge.setBounds(0, 0, 200, 200);
				//ivjtblSelectRnge.setNextFocusableComponent(
				//	getButtonPanel1());
				ivjtblSelectRnge.setGridColor(java.awt.Color.white);
				ivjtblSelectRnge.setAutoCreateColumnsFromModel(false);
				// user code begin {1}
				caTableModel = (TMINV025) ivjtblSelectRnge.getModel();
				TableColumn laTCa =
					ivjtblSelectRnge.getColumn(
						ivjtblSelectRnge.getColumnName(0));
				laTCa.setPreferredWidth(175);
				TableColumn laTCb =
					ivjtblSelectRnge.getColumn(
						ivjtblSelectRnge.getColumnName(1));
				laTCb.setPreferredWidth(40);
				TableColumn laTCc =
					ivjtblSelectRnge.getColumn(
						ivjtblSelectRnge.getColumnName(2));
				laTCc.setPreferredWidth(60);
				TableColumn laTCd =
					ivjtblSelectRnge.getColumn(
						ivjtblSelectRnge.getColumnName(3));
				laTCd.setPreferredWidth(85);
				TableColumn laTCe =
					ivjtblSelectRnge.getColumn(
						ivjtblSelectRnge.getColumnName(4));
				laTCe.setPreferredWidth(85);
				ivjtblSelectRnge.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectRnge.init();
				laTCa.setCellRenderer(
					ivjtblSelectRnge.setColumnAlignment(RTSTable.LEFT));
				laTCb.setCellRenderer(
					ivjtblSelectRnge.setColumnAlignment(
						RTSTable.RIGHT));
				laTCc.setCellRenderer(
					ivjtblSelectRnge.setColumnAlignment(
						RTSTable.RIGHT));
				laTCd.setCellRenderer(
					ivjtblSelectRnge.setColumnAlignment(
						RTSTable.RIGHT));
				laTCe.setCellRenderer(
					ivjtblSelectRnge.setColumnAlignment(
						RTSTable.RIGHT));
				ivjtblSelectRnge.addMultipleSelectionListener(this);
				//ivjtblSelectRnge.setNextFocusableComponent(
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
		return ivjtblSelectRnge;
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
			setName(ScreenConstant.INV025_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(530, 320);
			setModal(true);
			setTitle(ScreenConstant.INV025_FRAME_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

//	defect 7890
//	 This method is not longer used due to 8240
//	/**
//	 * Allows for keyboard navigation of the button panel using the
//	 * arrow keys.
//	 *
//	 * @param aaKE KeyEvent  
//	 */
//	public void keyPressed(KeyEvent aaKE)
//	{
//		super.keyPressed(aaKE);
		// Used to determine whether or not all the rows on the table 
		// are selected, and if they are then set the check box to 
		// selected.  And visa versa.
//		if (aaKE.getSource() instanceof RTSTable)
//		{
//			if (aaKE.getKeyCode() == KeyEvent.VK_SPACE)
//			{
//				Vector lvSelctdRows =
//					new Vector(
//						gettblSelectRnge().getSelectedRowNumbers());
//				if (lvSelctdRows.size()
//					== gettblSelectRnge().getRowCount())
//				{
//					getchkSelectAllRnges().setSelected(true);
//				}
//				else
//				{
//					getchkSelectAllRnges().setSelected(false);
//				}
//			}
//		}
		// defect 7890
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
		// end defect 7890
//	}
//	end defect 7890

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
			FrmInventoryItemRangeDeleteINV025 laFrmInventoryItemRangeDeleteINV025;
			laFrmInventoryItemRangeDeleteINV025 =
				new FrmInventoryItemRangeDeleteINV025();
			laFrmInventoryItemRangeDeleteINV025.setModal(true);
			laFrmInventoryItemRangeDeleteINV025
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryItemRangeDeleteINV025.show();
			java.awt.Insets insets =
				laFrmInventoryItemRangeDeleteINV025.getInsets();
			laFrmInventoryItemRangeDeleteINV025.setSize(
				laFrmInventoryItemRangeDeleteINV025.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryItemRangeDeleteINV025.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryItemRangeDeleteINV025.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
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
		cvInvAlloctnUIData = (Vector) aaData;
		caTableModel.add(cvInvAlloctnUIData);
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE  ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}

		Vector lvSelctdRows =
			new Vector(gettblSelectRnge().getSelectedRowNumbers());
		if (lvSelctdRows.size() == gettblSelectRnge().getRowCount())
		{
			getchkSelectAllRnges().setSelected(true);
		}
		else
		{
			getchkSelectAllRnges().setSelected(false);
		}
	}
}
