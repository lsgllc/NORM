/**
 * CountyInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class CountyInfoResponse  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse[] countyInfoReturn;

    public CountyInfoResponse() {
    }

    public com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse[] getCountyInfoReturn() {
        return countyInfoReturn;
    }

    public void setCountyInfoReturn(com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse[] countyInfoReturn) {
        this.countyInfoReturn = countyInfoReturn;
    }

    public com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse getCountyInfoReturn(int i) {
        return countyInfoReturn[i];
    }

    public void setCountyInfoReturn(int i, com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse value) {
        this.countyInfoReturn[i] = value;
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
        CountyInfoResponse history = (CountyInfoResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        CountyInfoResponse other = (CountyInfoResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.countyInfoReturn==null && other.getCountyInfoReturn()==null) || 
             (this.countyInfoReturn!=null &&
              java.util.Arrays.equals(this.countyInfoReturn, other.getCountyInfoReturn())));
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
        CountyInfoResponse history = (CountyInfoResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getCountyInfoReturn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCountyInfoReturn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCountyInfoReturn(), i);
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
