package com.lsgllc.norm.util.exceptions;

/*
 * $Id
 *
* The Contractor is: Loy Services Group, LLC. ***************************************************************************
 *
 * com.lsgllc.norm.util.exceptions/NormRuntimeSystemException
 * created: Apr 20, 2010 at 11:30:27 AM
 *
 ***************************************************************************
*/
public class NormRuntimeSystemException extends RuntimeException {
    public NormRuntimeSystemException(NormSystemException e) {
        super(e);
    }
}
