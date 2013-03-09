package com.txdot.isd.rts.services.reports;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.CompleteTransactionData;

/*
 * Vehicle.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * @version	5.2.3			05/31/2005	
 * @author	Administrator
 * <br>Creation Date:		11/06/2001 15:44:03
 */
public class Vehicle
{
	private static String csVehString =
		"aced000573720040636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e627573696e6573732e436f6d706c657465"
			+ "5472616e73616374696f6e4461746162a67ff73724b9460200374900106344697361626c654164646c4665657349000f6344697361626c654374794665657349000d6369437572725"
			+ "472616e734e6f49000a63694465616c65724964490011636944697361626c654164646c46656573490010636944697361626c654374794665657349000763694578704d6f49000763694"
			+ "57870597249000863694673636c597249000a63694678644578704d6f49000e6369496e764974656d436f756e7449001063694d61696c4665654170706c69657349000c63694e6f4368726"
			+ "7496e646949000a63694e6f4d6f4368726749000f63694f666349737375616e63654e6f49001563694f776e72537570706c69656453746b7241676549000c6369526543616c63496e64694900126369"
			+ "526567506e6c747943687267496e646949001363695370636c506c61746550726f67496e646949001063695370656369616c506c74496e646949000a6369537562636f6e4964490010636954656d70566"
			+ "56847726f7373577449001263695472616e73477265676f7269616e447449001d636954746c496e57696e496e76507243644e6f74457154776f496e64694900136369566f69644f666349737375616e63654e6f49"
			+ "000f6369566f69645472616e7354696d6549000f6369566f69645472616e73577349644900116369566f696465645472616e73496e64694c001063416e6e75616c44696573656c4665657400284c636f6d2f7478646f"
			+ "742f6973642f7274732f73657276696365732f7574696c2f446f6c6c61723b4c000d63416e6e75616c52656746656571007e00014c000e634372647452656d61696e696e6771007e00014c0011634375737441637475616"
			+ "c52656746656571007e00014c000f63437573744261736552656746656571007e00014c000a6344696573656c46656571007e00014c000c6346756e64735570646174657400384c636f6d2f7478646f742f6973642f7274732f636c69656e"
			+ "742f6163636f756e74696e672f75692f46756e6473557064617465446174613b4c000f634f726756656869636c65496e666f7400324c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f4d465665686963"
			+ "6c65446174613b4c000c6352656746656573446174617400304c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f52656746656573446174613b4c000d635265674974656d734461746174003a4c636f6d2f7478646f742f"
			+ "6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f526567697374726174696f6e4974656d73446174613b4c00136352656754746c4164646c496e666f446174617400374c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75"
			+ "692f52656754746c4164646c496e666f446174613b4c001063526567697350656e616c747946656571007e00014c001963537562636f6e74726163746f7252656e6577616c446174617400434c636f6d2f7478646f742f6973642f7274732f636c69656e742f726567697374726174696f6e2f75692f537562636f6e74726163746f7252656e6577616c446174613b4c0008635665684d6973637400304c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f566568"
			+ "4d697363446174613b4c000f6356656853616c65735"
			+ "46178416d7471007e00014c000a63566568546178416d7471007e00014c000c63566568546178506e6c"
			+ "747971007e00014c001363566568546f74616c53616c6573546178506471007e00014c000c63566568696"
			+ "36c65496e666f71007e00034c001663646f6c6c617242736e44617465546f74616c416d7471007e00014c"
			+ "001363734f776e72537570706c696564506c744e6f7400124c6a6176612f6c616e672f537472696e673b4c"
			+ "001463734f776e72537570706c69656453746b724e6f71007e00094c000b63735472616e73436f64657100"
			+ "7e00094c000e63735478745265674d6f536f6c6471007e00094c000d6373566f69645472616e73436471007e00094c00096376496e7649746d73"
			+ "7400124c6a6176612f7574696c2f566563746f723b4c000e6c76416c6c6f63496e7649746d7371007e000a"
			+ "78700000000000000000ffffffff0000000000000000000000000000000b000007d2000007d1000000"
			+ "000000000000000001000000000000000c000000aa00000000000000000000000000000000000000000"
			+ "00000000000000000000000000000010000000000000000000000000000000073720026636f6d2e7478646"
			+ "f742e6973642e7274732e73657276696365732e7574696c2e446f6c6c61724c6b4390c9856fc50300024c"
			+ "000a626967446563696d616c7400164c6a6176612f6d6174682f426967446563696d616c3b4c000576616c"
			+ "756571007e00097870770400000000787371007e000c77044222000078707371007e000c77044222000078707371007e000c7704"
			+ "00000000787073720030636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e4d4656656869636c6544617461278776"
			+ "712ce36e410200074900076a6e6b496e64694c0009634d697363446174617400394c636f6d2f7478646f74"
			+ "2f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f526567697374726174696f6e4d697363446174613b4c000a634f776e65724461746174002e4c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f4f776e6572446174613b4c000863526567446174617400354c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f526567697374726174696f6e446174613b4c000c6353616c76616765446174617400304c636f6d2f7478646f742f6973642"
			+ "f7274732f636c69656e742f636f6d6d6f6e2f75692f53616c76616765446174613b4c000a635469746c654461746174002e4c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f5469746c65446174613b4c000c6356656869636c65446174617400304c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f56656869636c65446174613b787000000000707372002c636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e4f776e657244617461a2cab46ae65d9852020006490"
			+ "00e6369507269766163794f707443644c0009634f776e72416464727400304c636f6d2f7478646f742f6973642f7274732f636c69656e742f636f6d6d6f6e2f75692f41646472657373446174613b4c000963734f776e6572496471007e00094c000863734f776e72496471007e00094c000e63734f776e7254746c4e616d653171007e00094c000e63734f776e7254746c4e616d653271007e00097870000000037372002e636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e416464726573734461746100d8f14f198fa6b00200074c000663734369747971007e00094c00076373436e74727971007e00094c00056373537431"
			+ "71007e00094c0005637353743271007e00094c00076373537461746571007e00094c000663735a70636471007e00094c000863735a706364703471007e00097870740006495256494e4774000074000f3235313020434152545752494748547400007400025458740005373530363274000434313432707400007400104252454e44412050454e494e47544f4e74000073720033636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e526567697374726174696f6e44617461649b98239b9465f902003f49000d636943616e63506c74496e646949000e636943616e635265674578704d6f49000c636943616e6353746b72447449000f636943616e6353746b72457870597249000e636943616e635374"
			+ "6b72496e646949000c636943616e636c506c7444744900126369436c61696d436f6d7074436e74794e6f49001363694470735361667479537573706e496e646949000763694566664474490009636945666654696d6549000b636945786d7074496e646949000763694578704474490009636945787054696d6549000c636946696c655469657243644900126369487679566568557365546178496e646949000c63694e6f43687267496e646949000f63694f6666487779557365496e646949001363694f776e72537570706c696564457870597249000f6369506c745365697a6564496e646949000b6369507265764578704d6f49000b636950726576457870597249000c6369526567436c617373436449000a636952656745666644744900"
			+ "0a63695265674578704d6f49000a6369526567457870597249000e6369526567486f74436b496e646949000e6369526567496e766c64496e646949000c63695265674973737565447449000b6369526567506c7441676549000f6369526567576169766564496e6469490013636952656e776c4d61696c5274726e496e6469490015636952656e776c59724d69736d61746368496e64694900106369526573436f6d7074436e74794e6f49000f636953746b725365697a64496e646949000a6369537562636f6e496449000f6369537562636f6e4973737565447449001463695472616666696357617272616e74496e64694900136369556e7265676973746572566568496e646949000e6369566568436172796e6743617049000c63695665684"
			+ "7726f737357744c000f63437573744163746c52656746656571007e00014c000f63437573744261736552656746656571007e00014c000d63437573744368696c6446656571007e00014c000e634375737444696573656c46656571007e00014c000e634375737452644272646746656571007e00014c000a63526567526566416d7471007e00014c000e6352656e776c4d61696c4164647271007e001b4c000d6369566568526567537461746571007e00094c000c637343616e63506c7456696e71007e00094c000c637343616e6353746b72436471007e00094c000d637343616e6353746b7256696e71007e00094c000b6373446c73436572744e6f71007e00094c00126373456d697373696f6e536f75726365436471007e00094c000d63734e6f7"
			+ "466796e674369747971007e00094c001363734f776e72537570706c696564506c744e6f71007e00094c000b637350726576506c744e6f71007e00094c000c6373526563706e744e616d6571007e00094c000a6373526567506c74436471007e00094c000a6373526567506c744e6f71007e00094c00106373526567506c744f776e724e616d6571007e00094c000b637352656753746b72436471007e00094c000b637352656753746b724e6f71007e00094c000c63735469726554797065436471007e00097870000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000b000007d100000019013158c100000"
			+ "00b000007d100000000000000000131303100000004000000000000000000000000000000aa00000000000000000000000000000000000000000000000000000af071007e001071007e000f7071007e000e707371007e000c770400000000787371007e001d7400007400007400007400007400007400007400007074000074000074000070740000740000707400064b4f414c4154740000740003504c507400064b4f414c4154740000740002575374000a31323435363634365741740001507372002e636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e53616c76616765446174614de0ca3aea38097202000949000c634f776e724576646e63436449001263694c69656e4e6f74526c736564496e646949001363694"
			+ "c69656e4e6f74526c736564496e64693249001363694c69656e4e6f74526c736564496e6469334900086369536c766743644c00086364536c766744747400294c636f6d2f7478646f742f6973642f7274732f73657276696365732f7574696c2f525453446174653b4c000f63734f746872476f767454746c4e6f71007e00094c001063734f7468725374617465436e74727971007e00094c000c637353616c76596172644e6f71007e000978700000000000000000000000000000000000000000707070707372002c636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e5469746c6544617461b17da9f95b53b5d502003149001363694164646c4c69656e5265637264496e646949001163694164646c5472616465496e496e6469490010636941676e63794c6f616e64496e646949000e636943636f49737375654461746549000c6369436f6d70436e74794e6f4900"
			+ "0b6369446f6354797065436449000e6369476f76744f776e64496e64694900146369496e73706563746e576169766564496e646949001063694c6567616c52657374726e744e6f49001163694c69656e486f6c646572314461746549001163694c69656e486f6c646572324461746549001163694c69656e486f6c646572334461746549001463694d6f72654c69656e486f6c646572496e646949000d63694d756c74526567496e646949001363694d7573744368616e6765506c74496e646949001363695072696f7243434f4973737565496e646949001163695370636c506c7450726f67496e646949000d63695375727254746c44617465490013636953757276736870526967687473496e646949000d636954746c4170706c4461746549000d636954746c45786d6e496e646949000e6369547"
			+ "46c486f74436b496e646949000e636954746c497373756544617465490009636954746c4e6f4d6649000f636954746c52656a63746e4461746549000e636954746c52656a63746e4f666349000e636954746c5265766b64496e646949000d636954746c54797065496e646949000d6369566568536f6c644461746549000f63734f776e725368704576696443644c000c634c69656e486f6c646572317400324c636f6d2f7478646f742f6973642f7274732f73657276696365732f63616368652f4c69656e686f6c64657273446174613b4c000c634c69656e486f6c6465723271007e00454c000c634c69656e486f6c6465723371007e00454c000e6353616c65735461785064416d7471007e00014c000b6354746c5665684164647271007e001b4c000e6356656853616c6573507269636571007e00014c001"
			+ "3635665685472616465696e416c6c6f776e636571007e00014c00076364496d634e6f71007e00014c0"
			+ "009637342617463684e6f71007e00094c000c6373426"
			+ "e64656454746c"
			+ "436471007e00094c00086373446c7247646e71007e00094c00076373446f634e6f7100"
			+ "7e00094c000a63734f6c64446f634e6f71007e00094c000e6373507265764f776e724369747971007e00094c000e6373507265764f776e724e616d6571007e00094c000f6373507265764f776e72537461746571007e00094c0010637353616c765374617465436e74727971007e00094c000c637354746c50726f6373436471007e00094c000b6373566568556e69744e6f71007e00097870000000000000000000000000000000000000000000000001000000000000000000000000012f325f000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000012f32c70000000000000000000000000000000000000000000000000000000073720030636f6d2e7478646f742e6973642e7274732e73657276696365732e63616368652e4c69656e686f6c64657273446174610519fa5b7fd3391002000e49000c636944656c657465496e6469490"
			+ "00c63694c69656e486c6472496449000f63694f666349737375616e63654e6f49000a636953756273746149644c000d6343686e6754696d6573746d7071007e00424c000e63734c69656e486c64724369747971007e00094c000f63734c69656e486c6472436e74727971007e00094c000f63734c69656e486c64724e616d653171007e00094c000f63734c69656e486c64724e616d653271007e00094c000d63734c69656e486c647253743171007e00094c000d63734c69656e486c647253743271007e00094c000f63734c69656e486c6472537461746571007e00094c000e63734c69656e486c64725a70436471007e00094c001063734c69656e486c64725a704364503471007e0009787000000000000281fc80000000800000007074000d4752414e44205052414952494574000074001c494e444550454e44454e5420414d45524943414e20534156494e475374000074001535333020532043415252494552205041524b574159740000740002545874000537353035317400007371007e004700000000000000008000000080000000707400007400007400007400007400007400007400007400007400007371007e004700000000000000008000000080000000707400007400007400007400007400007400007400007400007400007371007e000c770400000000787371007e001d74000071007e002e7400007400007400007400007400007371007e000c770400000000787371007e000c770400000000787071007e002e7400013071007e002e74001130303030303030303030363137373036317400007400007400184241494c455920504f4e5449414320494e432048555253547400007400007400007400007372002e636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e56656869636c65446174613f69287600e4f96b02001e49000c636944696573656c496e6469490015636944697361626c65566568436c617373496e64694900106369446f7453746e64726473496e646949000d636944707353746c6e496e646949000f6369466c6f6f64446d6765496e646949000b63694678645774496e646949000f636950726d745265717264496e646949000c63695265436f6e74496e6469490015636952656e776c59724d69736d61746368496e646949001263695265706c6963615665684d6f646c5972490010636956494e41566568456d707479577449000c6369566568456d707479577449000a63695665684c6e67746849000b63695665684d6f646c597249000c636956696e457272496e646949000c636956696e61426567546f6e49000c636956696e61456e64546f6e4c00086364566568546f6e71007e00014c001363734175646974547261696c5472616e73496471007e00094c000a63735265636f6e64436471007e00094c000e63735265706c6963615665684d6b71007e00094c000a637354726c725479706571007e00094c000c63735665684264795479706571007e00094c000b637356656842647956696e71007e00094c000c6373566568436c617373436471007e00094c000763735665684d6b71007e00094c000963735665684d6f646c71007e00094c000e63735665684f646d747242726e6471007e00094c001063735665684f646d7472526561646e6771007e00094c0005637356696e71007e00097870000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000af000000000000007c30000000000000000000000007371007e000c77040000000078740011303537323534333539303030393030333074000130740000740000740002324474000074000450415353740003495355740003494d50740001417400063030303335357400114a414241523037413845303931343137357372002e636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e5265674665657344617461aa9411b1ad3d2a3d02001749000e63694578704d61784d6f6e74687349000e63694578704d696e4d6f6e746873490008636946726f6d4d6f490008636946726f6d597249000c63694d61784578704d6f597249000c63694d696e4578704d6f59724900086369506c74416765490008636953746b724e6f49000d6369546f4d6f6e746844666c7449000c6369546f4d6f6e74684d617849000c6369546f4d6f6e74684d696e49000c6369546f5965617244666c7449000b6369546f596561724d617849000b6369546f596561724d696e4c000c63466565546f74616c4d617871007e00014c000c63466565546f74616c4d696e71007e00014c000c63734578704d6f59724d617871007e00094c000c63734578704d6f59724d696e71007e00094c000963734864724465736371007e00094c00076373506c744e6f71007e00094c00086373526561736f6e71007e00094c000963735472616e73436471007e00094c000a6376566563744665657371007e000a787000005dee00005dd80000000c000007d1000000000000000000000000000000000000000b0000000a0000000c000007d2000007d3000007d17371007e000c7704429b428f787371007e000c770440a000007874000731302f3230303374000731322f3230303170707070737200106a6176612e7574696c2e566563746f72d9977d5b803baf010200034900116361706163697479496e6372656d656e7449000c656c656d656e74436f756e745b000b656c656d656e74446174617400135b4c6a6176612f6c616e672f4f626a6563743b78700000000000000001757200135b4c6a6176612e6c616e672e4f626a6563743b90ce589f1073296c02000078700000000a7372002b636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e46656573446174616f0873a589630ec8020005490011636943726474416c6c6f776564496e6469490008636949746d5174794c000a634974656d507269636571007e00014c000b63734163637449746d436471007e00094c000663734465736371007e0009787000000001000000017371007e000c77044222000078740006434f52524547740017524547495354524154494f4e20434f5252454354494f4e70707070707070707073720038636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e526567697374726174696f6e4974656d73446174614b17a4031f02546c0200014c00166376566374496e76416c6c6f636174696f6e4461746171007e000a78707073720035636f6d2e7478646f742e6973642e7274732e636c69656e742e6"
			+ "36f6d6d6f6e2e75692e52656754746c4164646c496e666f44617461493b5d6d48d11ba902001649001263694164646c4576695375726e64496e6469490013636941707072686e64466e6473436e74794e6f4900186369436872674164646c546b6e54726c72466565496e646949000d636943687267466565496e646949001363694368726754726e736672466565496e646949000d6369436f7272746e4566664d6f49000d6369436f7272746e456666597249000f636944696573656c466565496e64694900116369446f7450726f6f66526571496e646949000c63694578656d7074496e646949000e6369466c6f6f64446d67496e646949000f6369476f76744f776e6564496e64694900126369487679566568557365546178496e646949001163694e6577506c744465737264496e646949001563694e6f43687267526567456d69466565496e6469490011636950726f637342794d61696c496e646949001363695265636f6e7374727563746564496e6469490012636952656745787069726564526561736f6e49001263695370636c4578616d4e656564496e646949000c636953757276526967687473490011636954726e736672506e6c7479496e6469490013636956696e43657274576169766564496e64697870000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000007371007e000c77040000000078707372002e636f6d2e7478646f742e6973642e7274732e636c69656e742e636f6d6d6f6e2e75692e5665684d69736344617461c0ed2d6f45cf565a02001749001163694164646c5472616465496e496e646949000d6369436f6d7074436e74794e6f4900076369494d434e6f49000963694d6644776e436449001863694e6f4368726753616c546178456d69466565496e646949001363694f776e72537570706c696564457870597249000e636953616c657354617844617465490011636953616c657354617845786d70744364490012636953616c6573546178506e6c747950657249001163695370636c506c7450726f67496e646949000a6369537562636f6e494449000e63695472616465496e566568597249000d636954746c4170706c446174654c000f6354617850644f746872537461746571007e00014c000f63546f74616c526562617465416d7471007e00014c000a63566568546178416d7471007e00014c0008637341757468436471007e00094c00086373446c7247444e71007e00094c001363734f776e72537570706c696564506c744e6f71007e00094c001463734f776e72537570706c69656453746b724e6f71007e00094c000d637353616c657354617843617471007e00094c000e63735472616465496e5665684d6b71007e00094c000f63735472616465496e56656856494e71007e00097870000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000007371007e000c770400000000787371007e000c770400000000787371007e000c77040000000078707070707070707070707071007e001970707074000552454e455774002854686973207265666c65637473203132206d6f6e746873206f6620726567697374726174696f6e2e707371007e008e00000000000000007571007e00910000000a7070707070707070707070";

	/**
	 * Vehicle constructor
	 */
	public Vehicle()
	{
		super();
	}
	
	/**
	 * Method to Get Vehicle
	 * 
	 * @return CompleteTransactionData
	 */
	public static CompleteTransactionData getVeh()
	{
		CompleteTransactionData laObj = null;
		try
		{
			laObj =
				(CompleteTransactionData) Comm.StringToObj(csVehString);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		return laObj;
	}
	
	/**
	 * Main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		CompleteTransactionData laObj = getVeh();
		System.out.println(laObj);
	}
}
