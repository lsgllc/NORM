package com.lsgllc.norm.util.exceptions;

/**
 * Created By: sameloyiv
 * Date: 1/4/13
 * Time: 11:50 AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2013
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class NormNotFoundException extends NormSystemException {
    public NormNotFoundException(Exception e) {
        super( e);
    }

    public NormNotFoundException() {
        super("AbstractValue not found");
    }
}
