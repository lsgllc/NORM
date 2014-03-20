package com.lsgllc.norm.util.exceptions;

/**
 * Created By: sameloyiv
 * Date: 12/23/12
 * Time: 11:39 AM
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
public class NormGenericTypeAllocationException extends NormSystemException {
    public NormGenericTypeAllocationException() {
        super(new NormSystemException("Check Generic name allocation assumptions..."));

    }
}
