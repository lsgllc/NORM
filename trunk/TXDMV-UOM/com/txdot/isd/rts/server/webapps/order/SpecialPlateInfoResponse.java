/**
 * SpecialPlateInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class SpecialPlateInfoResponse  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse[] specialPlateInfoReturn;

    public SpecialPlateInfoResponse() {
    }

    public com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse[] getSpecialPlateInfoReturn() {
        return specialPlateInfoReturn;
    }

    public void setSpecialPlateInfoReturn(com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse[] specialPlateInfoReturn) {
        this.specialPlateInfoReturn = specialPlateInfoReturn;
    }

    public com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse getSpecialPlateInfoReturn(int i) {
        return specialPlateInfoReturn[i];
    }

    public void setSpecialPlateInfoReturn(int i, com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse value) {
        this.specialPlateInfoReturn[i] = value;
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
        SpecialPlateInfoResponse history = (SpecialPlateInfoResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SpecialPlateInfoResponse other = (SpecialPlateInfoResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.specialPlateInfoReturn==null && other.getSpecialPlateInfoReturn()==null) || 
             (this.specialPlateInfoReturn!=null &&
              java.util.Arrays.equals(this.specialPlateInfoReturn, other.getSpecialPlateInfoReturn())));
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
        SpecialPlateInfoResponse history = (SpecialPlateInfoResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSpecialPlateInfoReturn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSpecialPlateInfoReturn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSpecialPlateInfoReturn(), i);
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
