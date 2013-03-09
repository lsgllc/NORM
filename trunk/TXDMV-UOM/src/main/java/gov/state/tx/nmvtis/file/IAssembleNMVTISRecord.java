package gov.state.tx.nmvtis.file;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 10:16 AM
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
public interface IAssembleNMVTISRecord extends Serializable {
    Byte[] getAssembledRecordRaw();

    String getAssembledRecordString();

}
