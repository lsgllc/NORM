package gov.state.tx.nmvtis.file.impl;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:49 AM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class AssembleNMVTISRecord implements IAssembleNMVTISRecord {
    @Override
    public Byte[] getAssembledRecordRaw() {
        return new Byte[0];
    }

    @Override
    public String getAssembledRecordString() {
        return null;
    }
}
