package gov.state.tx.nmvtis.file.coding;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 3:24 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public enum BRAND_CODES {
    CLEAR(00),
    BOND_POSTED(33),
    MEMORANDUM_COPY(34),
    UNDISCLOSED_LEIN(37),
    ACTUAL(68),
    ODO_EXEMPT(71),
    RENEWAL_VALUE(75);

    BRAND_CODES(int i) {
    }
}
