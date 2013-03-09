/**
 * SpecialPlateInfo.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class SpecialPlateInfo  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoRequest aaInfoRequestObj;

    public SpecialPlateInfo() {
    }

    public com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoRequest getAaInfoRequestObj() {
        return aaInfoRequestObj;
    }

    public void setAaInfoRequestObj(com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoRequest aaInfoRequestObj) {
        this.aaInfoRequestObj = aaInfoRequestObj;
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
        SpecialPlateInfo history = (SpecialPlateInfo) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SpecialPlateInfo other = (SpecialPlateInfo) obj;
        boolean _equals;
        _equals = true
            && ((this.aaInfoRequestObj==null && other.getAaInfoRequestObj()==null) || 
             (this.aaInfoRequestObj!=null &&
              this.aaInfoRequestObj.equals(other.getAaInfoRequestObj())));
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
        SpecialPlateInfo history = (SpecialPlateInfo) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getAaInfoRequestObj() != null) {
            _hashCode += getAaInfoRequestObj().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
