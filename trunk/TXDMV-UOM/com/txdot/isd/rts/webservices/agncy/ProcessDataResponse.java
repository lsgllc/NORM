/**
 * ProcessDataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agncy;

public class ProcessDataResponse  implements java.io.Serializable {
    private com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse[] processDataReturn;

    public ProcessDataResponse() {
    }

    public com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse[] getProcessDataReturn() {
        return processDataReturn;
    }

    public void setProcessDataReturn(com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse[] processDataReturn) {
        this.processDataReturn = processDataReturn;
    }

    public com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse getProcessDataReturn(int i) {
        return processDataReturn[i];
    }

    public void setProcessDataReturn(int i, com.txdot.isd.rts.webservices.agncy.data.RtsAgncyResponse value) {
        this.processDataReturn[i] = value;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        ProcessDataResponse history = (ProcessDataResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        ProcessDataResponse other = (ProcessDataResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.processDataReturn==null && other.getProcessDataReturn()==null) || 
             (this.processDataReturn!=null &&
              java.util.Arrays.equals(this.processDataReturn, other.getProcessDataReturn())));
        if (!_equals) {
            __history.set(null);
            return false;
        };
        __history.set(null);
        return true;
    }

    private transient java.lang.ThreadLocal __hashHistory;
    public int hashCode() {
        if (__hashHistory == null) {
            synchronized (this) {
                if (__hashHistory == null) {
                    __hashHistory = new java.lang.ThreadLocal();
                }
            }
        }
        ProcessDataResponse history = (ProcessDataResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getProcessDataReturn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProcessDataReturn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProcessDataReturn(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
