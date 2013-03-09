package com.lsgllc.norm.util.exceptions;/*
 *  NormSystemException
 *
* The Contractor is: Loy Services Group, LLC. *
 * created: Apr 30, 2010 
 *  Author: sampaw
 */

public class NormSystemException extends Throwable {
    public NormSystemException(Throwable e) {
        super(e);
        e.printStackTrace();
    }

    public NormSystemException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NormSystemException() {
        this("Norm Runtime Exception");
    }

    public NormSystemException(String s) {
        this(s,null);
        //To change body of created methods use File | Settings | File Templates.
    }
}
