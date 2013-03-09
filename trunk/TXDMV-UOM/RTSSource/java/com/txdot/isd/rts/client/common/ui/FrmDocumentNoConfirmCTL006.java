package com.txdot.isd.rts.client.common.ui;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.Insets;import java.awt.event.*;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JPanel;import javax.swing.WindowConstants;import com.txdot.isd.rts.client.general.ui.AbstractViewController;import com.txdot.isd.rts.client.general.ui.ButtonPanel;import com.txdot.isd.rts.client.general.ui.RTSDialogBox;import com.txdot.isd.rts.client.general.ui.RTSInputField;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.RTSHelp;import com.txdot.isd.rts.services.util.constants.ScreenConstant;import com.txdot.isd.rts.services.util.constants.TransCdConstant;/* * * FrmDocumentNoConfirmCTL006.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------ -----------	-------------------------------------------- * N Ting		04/17/2002	Global change for startWorking() and  *							doneWorking() * MAbs			06/03/2002	CQU100004192 * B Arredondo	02/23/2004	Modifiy visual composition to change *							defaultCloseOperation to DO_NOTHING_ON_CLOSE *							defect 6897 Ver 5.1.6 * T Pederson	03/11/2005	Java 1.4 Work * 							defect 7885 Ver 5.2.3  * B Hargrove	05/05/2005	chg '/**' to '/*' to begin prolog. * 							defect 7885 Ver 5.2.3  * B Hargrove	05/11/2005	Update help based on User Guide updates. * 							See also: services.util.RTSHelp * 							(fix merged in from VAJ) *  						modify actionPerfomed()  * 							defect 8177 Ver 5.2.2 Fix 5 * B Hargrove	05/20/2005	There is no 'CORREGX' TransCd at this point. * 							Also, cannot use object  * 							VehInquiryData\RegistrationModifyData\ * 							RegModifyType to determine whether CORREG	 * 							is a real CORREG or is a CORREGX (see: * 							REG008 actionPerformed() for this process) * 							because only the DocNo string is passed to  * 							this setData(). *  						modify actionPerfomed()  * 							defect 8177 Ver 5.2.2 Fix 5 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys * 							inside of its keyPressed method * 							delete keyPressed * 							defect 8240 Ver 5.2.3 * T Pederson	08/23/2005	Code cleanup * 							defect 7885 Ver 5.2.3  * --------------------------------------------------------------------- *//** * Screen for Document No Confirm CTL006 * * @version 5.2.3 			08/23/2005 * @author	Nancy Ting * <br>Creation Date: 		07/15/2001 13:29:49 *//* &FrmDocumentNoConfirmCTL006& */public class FrmDocumentNoConfirmCTL006	extends RTSDialogBox	implements ActionListener, KeyListener{/* &FrmDocumentNoConfirmCTL006'ivjButtonPanel1& */	private ButtonPanel ivjButtonPanel1 = null;/* &FrmDocumentNoConfirmCTL006'ivjJDialogBoxContentPane& */	private JPanel ivjJDialogBoxContentPane = null;/* &FrmDocumentNoConfirmCTL006'ivjJInputField1& */	private RTSInputField ivjJInputField1 = null;/* &FrmDocumentNoConfirmCTL006'ivjstcLblEnter& */	private JLabel ivjstcLblEnter = null;/* &FrmDocumentNoConfirmCTL006'csDocNo& */	private String csDocNo = null;	// Constants/* &FrmDocumentNoConfirmCTL006'DOC_NO_LENGTH& */	private final static int DOC_NO_LENGTH = 17;		// Text Constants/* &FrmDocumentNoConfirmCTL006'FRM_NAME_CTL006& */	private final static String FRM_NAME_CTL006 = 		"FrmDocumentNoConfirmCTL006";/* &FrmDocumentNoConfirmCTL006'FRM_TITLE_CTL006& */	private final static String FRM_TITLE_CTL006 = 		"Document Number Confirm   CTL006";/* &FrmDocumentNoConfirmCTL006'TXT_ENTER_DOC_NO& */	private final static String TXT_ENTER_DOC_NO = 		"Enter document number:";		/**	 * FrmDocumentNoConfirmCTL006 constructor	 *//* &FrmDocumentNoConfirmCTL006.FrmDocumentNoConfirmCTL006& */	public FrmDocumentNoConfirmCTL006()	{		super();		initialize();	}		/**	 * FrmDocumentNoConfirmCTL006 constructor	 * 	 * @param aaParent JFrame	 *//* &FrmDocumentNoConfirmCTL006.FrmDocumentNoConfirmCTL006$1& */	public FrmDocumentNoConfirmCTL006(JFrame aaParent)	{		super(aaParent);		initialize();	}		/**	 * Invoked when an action occurs	 * 	 * @param aaAE ActionEvent	 *//* &FrmDocumentNoConfirmCTL006.actionPerformed& */	public void actionPerformed(ActionEvent aaAE)	{		if (!startWorking())		{			return;		}		try		{			clearAllColor(this);			RTSException leEx = new RTSException();			String lsDocNo = getJInputField1().getText();			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())			{				if (lsDocNo.length() != DOC_NO_LENGTH)				{					leEx.addException(						new RTSException(150),						getJInputField1());				}				else if (!csDocNo.equals(lsDocNo))				{					leEx.addException(						new RTSException(315),						getJInputField1());				}				if (leEx.isValidationError())				{					leEx.displayError(this);					leEx.getFirstComponent().requestFocus();					return;				}				if (csDocNo.equals(lsDocNo))				{					getController().processData(						AbstractViewController.ENTER,						csDocNo);				}			}			else if (				aaAE.getSource() == getButtonPanel1().getBtnCancel())			{				getController().processData(					AbstractViewController.CANCEL,					null);			}			else if (				aaAE.getSource() == getButtonPanel1().getBtnHelp())			{				// defect 8177				//RTSHelp.displayHelp(RTSHelp.CTL006);				String lsTransCd = getController().getTransCode();				if (lsTransCd.equals(TransCdConstant.RENEW))				{					RTSHelp.displayHelp(RTSHelp.CTL006A);				}				else if (lsTransCd.equals(TransCdConstant.TITLE))				{					RTSHelp.displayHelp(RTSHelp.CTL006B);				}				else if (lsTransCd.equals(TransCdConstant.REPL))				{					RTSHelp.displayHelp(RTSHelp.CTL006C);				}				else if (lsTransCd.equals(TransCdConstant.EXCH))				{					RTSHelp.displayHelp(RTSHelp.CTL006D);				}				else if (lsTransCd.equals(TransCdConstant.PAWT))				{					RTSHelp.displayHelp(RTSHelp.CTL006E);				}				//else if (lsTransCd.equals(TransCdConstant.CORREGX))				//{				//	RTSHelp.displayHelp(RTSHelp.CTL006F);				//}				else if (lsTransCd.equals(TransCdConstant.CORREG))				{					RTSHelp.displayHelp(RTSHelp.CTL006F);				}				else if (lsTransCd.equals(TransCdConstant.TAWPT))				{					RTSHelp.displayHelp(RTSHelp.CTL006H);				}				// end defect 8177			}		}		finally		{			doneWorking();		}	}		/**	 * Get Builder Data	 * @deprecated	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.getBuilderData& */	private static void getBuilderData()	{		/*V1.1		**start of data**			D0CB838494G88G88GC6D5D7B0GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E1359AEDD0D457554C4414C918464C5829E37DC1D3E36948A426A52D131816F41CC9A6125634E41234E63451C9E80AF6BBB12919127432009F918DA2CD0CD2D9100FDDC04247A230C1045DC54120D05988B6B67AE09F707431EF5DFDABE0ED53F36EFD776D83F7C1EDE466F05FBD7763FC5EF34E3BEFC9FEE1DCF606B91F100CEC227DB5E69052E6A5645E336F36F2DC6EC11319987E0AG76905F41ACE8AD			B0B651E1B2671225C55B21EF077EB237497C91BC6FA54B0A7CE5709005CEA70CE52CFD777519695CFD0C5359C67B9F1566C03B9B2089F06781628FA27DCFCA77E978B9505F2DBDA3E4402E3D05B867E15BBE83BECD1B57D2485AAA8B4EB9D9DA200DAFG7CB2G8278FCCBF84F9D143B390EA62DFF7E01B83279598A73AE24E5581F032A2DDD20359672C32A08CC7272049B6977DE9EE99DD2462A55892F22D6CBC1EFF5C08DD68F491268D32BE1E0CC71D50F04A40763F8E04C2D8C0546GFFDAF1A93EE1A9B066			72F457549C59DE043C27583653FD89D98D34981F4F6FEA517818FFB90E487FD8159D0D4F6EA2461F3FA84C67661DD9DBAD4538F7106A9D57AD0371D4GA643EBC7DCB7AB434CA290A6439325DC86AF505DC0E93FEEC55A0B60B991A039184BF6341C4BB6706FC7481641D3668F2248E6BD44E46ABD9416CD0E6D4F7A187AD4EBA17AC5AC0CAD83D889108A303D44E436829C203E3C6AD885346D032A0B8A536E77BBE5EF60146295E5F8F6B8E0C4431D910714001790662FAF5B2AA27043212DE42A2F067D2B1FF0			7FCAB83304143C151C1D9DC136B92FA4E7FB34BDC6CB4232B139FEE3C139E2G3F98E085C08A40E6007CD2AE176BE07DECF2F579D4B16093643E5020DF88284E10A4FB4500A65FE469CC72C582366E9B373C0E69A53E5E2817B5BFBACD6342F43D2C092FB75714B2FDF416067542E8A7C525BDDAFA23BD90E2CB518F3F30219FDE07678C4DC799DE24785586BCBBB39B295FE2FF9D0C394B385F8E6C66B2F4E7ADA43935BD91E5D870F337B93E0C7113D4B65D36BFAE46B81586786D65A633852089E0GC08508A9			67767D6641CFEE52EFFB8D36E5EBCF94FE882DB8218604AEDFD095FCC362D9A1A089033298A45A1C5EC264FDF04A3A4F69F99A829AA7C41F9890D4C971416E4218880E94E4F33EAC5C03F382A29D75C8E3A2909873933E6FF061DEE8E5A128FE6E778A2A981E4146FD05284B2B42D95891B60067174AD167A69A836361F9A5G53557AC308AB301A4CF6AB574B6F8B73207D8C9CDB149D8E37B404EC8801C9CEFF3D85E309F8958215DB740BBEAFBC843531B7ACBB42FA99D6F41ECD940FE744850C87F57C5B16BD			8C8FFB98354C475F2363022A86244110AA76092AAA79C6426B3796A1AF41A947AFC834B3DE02BE5689F2DD84706B725DDF3457A0DF3722DC8E79BDE8B9CDCFAF52B8FBB74CDFE46539D530E12ECD32F2BF5DB4407D543D6BA1D275422908FE3A88E2D02A1579A731CDBFEB17691EE19AEFDA8734F5C95648E76D7F81C6A34746E9E4FE4D7BA3B03D1FAAC33DDBC1DEB7005746757EEC11051DB727909479896B890045C4EF584B585C246268BE76F4F1E49FFB2EB8320F7D3AF8E69F7AEDF1A49FE2E32F96E3EED1			46FD861FFC3DB80F1EE11FCF24CC6BCB884B7B1BBC288B49A7297D12F7C4D47B24EBA2A10BED68DB5BAA50372E4373BC9B772571C35C17CA2EFD17A4DD301AA36533858F1DB027EABE14EE70A546D36AA15439A80BD8BE381410CFE578CD74FCEBF8AD3E2CAB09BC77359274474B6224B66F0D122D8463089C92F5591689BBD14781E98CCC0587D62B9FB7159A6B47028A1399EB4D0B502A953C9E5A11319F5A994A9C4DFF6EA9C77EE2E1DD9C40728A6EBF43259F9B4E2D66B8AEC5161999F47B74156522BD45AB			A19142B29E6C4FF4197ED916B7ED6C2CA69F9B9FACC379E4C3CCD8DD01729D2C603597668C06EB2038E6D052DB19BC871F4AB5563E5EE0DBC4DBA264D7B29BDAAB27672FA72C38EFA76087AA396C2F1499E3D6FBB0288CC9B4BDE8F6591A856A1B25569C2199C726456699CF1FDB92E4E54429CC902D3E9CA8C33AA807DC496B4B9C1A3F97C3FF85C00A4ECFEC790DFE75C0797B5002BD03D097F945892E53EF1753BD859D4D6B38BA232ED2721E0BD847A124E9BAA2D553F515C1EDD0G781AAA4EDB1EF50FC1D7			D0F3C2FA9E81CBF2FE122C194C4F357ECAEBB677F34263643F4A308EF2D7C50E43839ADE565AB95032FAE78E55577C2A69F5503DCC0FD5536B20BC9B2F0392E1EC5FE11E177F4C628474E38FF3394E59F699646AD5A41F2A4B3435824F2A9210C6A49F476D24B82838E970E239CD25B6DE84FBA681B0DCB24525C0BFCD47FDC6FDB0375AE4AE296638799457847D6EEA1E336E21B9ECC06F4F237D6BFAFF9F4DB7B87EF5671AF0CE2B25B46EC4BE5B2D6BB73D826DB62177C9A576A7B53AFB49FDE5FF0356AB8E8B			A199520F9294FB7CDAF98778B555283FA5D44F38AE2E1A4744C747B15E8CC38188A9294FAF8E1144CFF06E7AB29ECFAD503783F8GD40079G0C66A0755BB145AB48BA0DC9AB0FD3697A3C25F41EAA29748C645460DD42DB35C4675DD20C3C1F54638184E51516E3BE1C4F627BD3D36E87FAEBD8ED24C29BD34369BCD906317112F0D518288E0E03BC557D124F2B0C43FED8A1C3A0F277DCB85D63696CBAFD62C24F698B2E6E1E3E8EAD96440D9BE3C1537B0E28F1B32E0679D2225666F8048C963FF614F37D2641			D8C9AD623ED51EEFD80BFC9E06895550327127270CE35C30916378BBD3461157A74D4671CA2BF1BC5B301E49352A143F07272F0AA772E3ADD16FC264DAA64FBC2D0DAB96B2929A6F5A46F4BB4F0A67CA8D04C4366FAF3FB06AABF7E1890DAB1176FDC65BAF25F67A9D4B1B5426B4E7980C66F01CC44B4BCC270F50B804733757711C70159DE3D69B7487G7CF5BCE6056AF1EE8C749757711865282B04762CF0C9FB136A70CA2A9220459FA0FB838A1C92F512E1E9A6564E34DCFEFB6F40CFED637ACEFC7CC1327C			1E740C1DD17432320E69E30356B695899958B2D9565AD1B7797510D7GBCGB2409C008575DC363B6A10C728E1117DF6D59506C6E9B664256DD9C99C8F2371F0333DF7E12A6635ECDDE9B47D408EA741ACBD0317A003F904914263DDE245F45DA4540763EBD27D0D31166520B7295DD26AD98E5A26637FCA71E986BC4BCDF3A99E7BD9F613F9544E653ED60FFB6A75B4B31BA64FDD75F89E307605E8A2DD2381308FA67AC6186C7B9BF1BC9976CA8548E90077F3GFB835FBB5BDE4CF49AC11F9DD0CC73D8426925			5A233FA77C51BE7BBB7A96BB56AC030A2AAAE3383F7E5EE9C739D545CF118C175900BE86A9F6D4E5D3B57C0E8674FBD99C26E816ABD751F3538BF2F99BF87EBCE567F9B626016747F7280E93203F92E00D0E4FEEC07CE668679F817D9C61787B6C7C7DEDB6607B5D6C79C950E9AF3EE99A591AFF5CAC0DCCDDE7450D516DE6EB1C5DEEF50D347E84FDFBA67DA243B5B6529AE8F8B8A86A7E5152182B633A023DCAD0C20FE4B175D5DA9F494224920279AB0F200D526BF81E9B003E9FE081C082G56B4B90DB847EB			5FB70B0C40DF9E461D20BA54BDA8F760A5A12F93BEC73B1E6C7279C36AF1C9143D358C1F1507F8E7887C50C743EFEDAC255554E04CAF1EC646478434EC7C158A63F80AB63E2F49E4E6F9FB7F143CDD5544467D505E8D404EF9C9BD3F3FDE843814A6DE1B54DE44FBB4BDBFD48B632A43F1A2A0F91D42C8B7559A4B91F2BD4F3BB94DBCC7AC24FB3A21FF9EC0ED666F3A93E5BCBEA7ACD9C8D2B7C63EF3DF67E9B047B6B35FDD569C4EDBF3E27B3358BE17AE73FD8A16FE070EC54B2BE95A7ADCC7F89F6E3B108307			236440EEE11067C0051EEF7473DE871763BF470D5F8ED6FC50E00E16E7E2B43A71867AB5A087233FFBF7A339457736306FE86FED79151C3FD48737DDEBE543AD6DEB70BC425F874F685FCCAC1FF23B5A693D6DG747DGB11F7218D1D1FFFBF75DCD37396E4AAD2FE339EEB70D37F11F1A74FA0961BFE2F15800E7E7A3CEBF9B4BE12C200557BA271B51EF66C0FF91C0A2C0B2C09A407623BC17BD5FD8C3E639DF76A87EEE880232616A136D9F6F407D8BE02FA60073GAAC08CC01C3E7F01A67CBE912D36BA23A8			340447EADE104052B4F58684FCEBE4BAD49BA269B071A87353E734F64D51F08EE13C6535E04CB181BE27956AEC009600732D1C2F1126D93F1BF4CC4C705D6463CA646BFAEB645CB5CF43AFB60C33F57DB3FECFDA9EE1BFE67BF86A53492D53FD62D10AFF29F53ACFB82C5CA7B640D867B1FE86726DC467656F0667A575B71BCFEF87980D1F5DD19A4C2E7D4D51F376174D116FE43D4D116FE4AFB54FFCA79BE80EFEA7BB59FC631D6C230E683CADF74CDECFBC6508DC87BE6D38318ECCF6C42E83FF6538318E3C0F			660E8470119547F89DE8EA60F560B3BAEE02560B7B5A603C3771F3343FA53ACCC5AD1175DD5E92D95F07DBE67E4E5250924D9E0C3F9A1AE33A01370BED1C6735D5DC8ED5477D145EBF2C047E9A9D673371F926B6FED69E36064F47CF8C4FED56BBFFD64E5DD19A2C1EFB30013E2F030158FD4B770F220C975B69B700A36ED9569B218D56A0609D8D469CCFBB4BC199BA2D7BAB70EEF8D489C857B0564B6D32B46243B7A76DCE6887957CDEB73E1D5F551C2844B3A4E1A9C1785D511715BC5F2740BC466B5BD5B371			1ABD0D578D86DEF1EC8BBC97E598F9FD410AFC0C89935D22EFC49D55E9EF24FEF388A04B49E94F354D2627EC838F79304E6AE434315F824F28F42C038AA9ED28EBC8780F5945FE0C90FEF7703B9CE3CD69FEB1BD639009DC53F5BA355F2DB843B5DD535AF3D9CC46248A8B19FA8F56077F4357E0E73ACEF4FA585C903DC72C0D4823699863F1E99B517517E5439F8255B6C61E9FFD0FDF34E30F656F95CE546BEA60BD8DA05785FA83E88170G0C82DC871887908F10841082108A108E1063067786G3B1B472949			E3F867952D8E51DE988C55870FB92B1D51635B0B4E59E376BAE764187D1A7346187D87E764187DA727B1E6EB5F241D68D3A3407700BE6FAFBA6E1C0EA32E9D9A2EDF72D2DF566E62DD6FEB78CE9189F2FC0E8B799A2738818D374700BB2761BEF461FBA9088CC71A2FBD6042F8B2AC49B26B9752B9128FDEEBF5FD14386819768B863EEDBA8E79E63FBDFB2F60F1E893B612EC15D87837B84B695058209830E8DF64AC968B7BA827128F697B2B454BA77A68C4C7B0C0BE602F0555DD1411F5D76B9D196B3F17E9A6			D9DDE4ABB3CC9749C26DF611EC2A7CAE12890619409FB3207171E39C8AD2923CC27ECB50357915933AF15194AC9615F6764C4E2B6DAB639D68163987225E992F68E067F4F547746FC7E2A15ABF67B81C01635C1F9FBBF8B81ABFC3695AB7A648B2C4594B6DBEAF5E4FB9A7D551D05794627EC1BEC79BE4E3D7686FB2AE99567C8FD0CB878849040A62AF8FGGE8A8GGD0CB818294G94G88G88GC6D5D7B049040A62AF8FGGE8A8GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4			E1F4E1D0CB8586GGGG81G81GBAGGGE98FGGGG		**end of data**/	}		/**	 * Return the ButtonPanel1 property value	 * 	 * @return ButtonPanel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.getButtonPanel1& */	private ButtonPanel getButtonPanel1()	{		if (ivjButtonPanel1 == null)		{			try			{				ivjButtonPanel1 = new ButtonPanel();				ivjButtonPanel1.setName("ButtonPanel1");				// user code begin {1}				ivjButtonPanel1.addActionListener(this);				ivjButtonPanel1.setAsDefault(this);				// user code end			}			catch (Throwable ivjExc)			{				// user code begin {2}				// user code end				handleException(ivjExc);			}		}		return ivjButtonPanel1;	}		/**	 * Return the JDialogBoxContentPane property value	 * 	 * @return JPanel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.getJDialogBoxContentPane& */	private JPanel getJDialogBoxContentPane()	{		if (ivjJDialogBoxContentPane == null)		{			try			{				ivjJDialogBoxContentPane = new JPanel();				ivjJDialogBoxContentPane.setName(					"JDialogBoxContentPane");				ivjJDialogBoxContentPane.setLayout(new GridBagLayout());				GridBagConstraints constraintsstcLblEnter =					new GridBagConstraints();				constraintsstcLblEnter.gridx = 1;				constraintsstcLblEnter.gridy = 1;				constraintsstcLblEnter.ipadx = 12;				constraintsstcLblEnter.insets =					new Insets(36, 18, 15, 6);				getJDialogBoxContentPane().add(					getstcLblEnter(),					constraintsstcLblEnter);				GridBagConstraints constraintsJInputField1 =					new GridBagConstraints();				constraintsJInputField1.gridx = 2;				constraintsJInputField1.gridy = 1;				constraintsJInputField1.fill =					GridBagConstraints.HORIZONTAL;				constraintsJInputField1.weightx = 1.0;				constraintsJInputField1.ipadx = 144;				constraintsJInputField1.insets =					new Insets(33, 7, 12, 19);				getJDialogBoxContentPane().add(					getJInputField1(),					constraintsJInputField1);				GridBagConstraints constraintsButtonPanel1 =					new GridBagConstraints();				constraintsButtonPanel1.gridx = 1;				constraintsButtonPanel1.gridy = 2;				constraintsButtonPanel1.gridwidth = 2;				constraintsButtonPanel1.fill = GridBagConstraints.BOTH;				constraintsButtonPanel1.weightx = 1.0;				constraintsButtonPanel1.weighty = 1.0;				constraintsButtonPanel1.ipadx = 30;				constraintsButtonPanel1.ipady = 26;				constraintsButtonPanel1.insets =					new Insets(12, 51, 12, 52);				getJDialogBoxContentPane().add(					getButtonPanel1(),					constraintsButtonPanel1);				// user code begin {1}				// user code end			}			catch (Throwable ivjExc)			{				// user code begin {2}				// user code end				handleException(ivjExc);			}		}		return ivjJDialogBoxContentPane;	}		/**	 * Return the JInputField1 property value	 * 	 * @return JInputField	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.getJInputField1& */	private RTSInputField getJInputField1()	{		if (ivjJInputField1 == null)		{			try			{				ivjJInputField1 = new RTSInputField();				ivjJInputField1.setName("JInputField1");				ivjJInputField1.setInput(1);				ivjJInputField1.setMaxLength(17);				// user code begin {1}				// user code end			}			catch (Throwable ivjExc)			{				// user code begin {2}				//ivjJInputField1.addActionListener(this);				// user code end				handleException(ivjExc);			}		}		return ivjJInputField1;	}		/**	 * Return the stcLblEnter property value	 * 	 * @return JLabel	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.getstcLblEnter& */	private JLabel getstcLblEnter()	{		if (ivjstcLblEnter == null)		{			try			{				ivjstcLblEnter = new JLabel();				ivjstcLblEnter.setName("stcLblEnter");				ivjstcLblEnter.setText(TXT_ENTER_DOC_NO);				ivjstcLblEnter.setHorizontalAlignment(					javax.swing.SwingConstants.RIGHT);				// user code begin {1}				// user code end			}			catch (Throwable ivjExc)			{				// user code begin {2}				// user code end				handleException(ivjExc);			}		}		return ivjstcLblEnter;	}		/**	 * Called whenever the part throws an exception	 * 	 * @param aeException Throwable	 *//* &FrmDocumentNoConfirmCTL006.handleException& */	private void handleException(Throwable aeException)	{		RTSException leRTSEx =			new RTSException(				RTSException.JAVA_ERROR,				(Exception) aeException);		leRTSEx.displayError(this);	}		/**	 * Initialize the class.	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmDocumentNoConfirmCTL006.initialize& */	private void initialize()	{		try		{			// user code begin {1}			// user code end			setName(FRM_NAME_CTL006);			setDefaultCloseOperation(				WindowConstants.DO_NOTHING_ON_CLOSE);			setSize(350, 150);			setModal(true);			setTitle(FRM_TITLE_CTL006);			setContentPane(getJDialogBoxContentPane());		}		catch (Throwable ivjExc)		{			handleException(ivjExc);		}		// user code begin {2}		// user code end	}		/**	 * main entrypoint - starts the part when it is run as an 	 * application	 * 	 * @param aarrArgs String[]	 *//* &FrmDocumentNoConfirmCTL006.main& */	public static void main(String[] aarrArgs)	{		try		{			FrmDocumentNoConfirmCTL006 laFrmDocumentNoConfirmCTL006;			laFrmDocumentNoConfirmCTL006 =				new FrmDocumentNoConfirmCTL006();			laFrmDocumentNoConfirmCTL006.setModal(true);			laFrmDocumentNoConfirmCTL006				.addWindowListener(new WindowAdapter()			{				public void windowClosing(WindowEvent aaWE)				{					System.exit(0);				};			});			laFrmDocumentNoConfirmCTL006.show();			Insets laInsets =				laFrmDocumentNoConfirmCTL006.getInsets();			laFrmDocumentNoConfirmCTL006.setSize(				laFrmDocumentNoConfirmCTL006.getWidth()					+ laInsets.left					+ laInsets.right,				laFrmDocumentNoConfirmCTL006.getHeight()					+ laInsets.top					+ laInsets.bottom);			laFrmDocumentNoConfirmCTL006.setVisibleRTS(true);		}		catch (Throwable aeException)		{			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);			aeException.printStackTrace(System.out);		}	}		/**	 * all subclasses must implement this method - it sets the data on 	 * the screen and is how the controller relays information	 * to the view	 * 	 * @param aaDataObject Object	 *//* &FrmDocumentNoConfirmCTL006.setData& */	public void setData(Object aaDataObject)	{		getJInputField1().requestFocus();		csDocNo = (String) aaDataObject;	}}/* #FrmDocumentNoConfirmCTL006# */