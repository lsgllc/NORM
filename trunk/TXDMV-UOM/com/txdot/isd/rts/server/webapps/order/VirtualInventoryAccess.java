/**
 * VirtualInventoryAccess.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class VirtualInventoryAccess  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest[] aaVIRequestObj;

    public VirtualInventoryAccess() {
    }

    public com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest[] getAaVIRequestObj() {
        return aaVIRequestObj;
    }

    public void setAaVIRequestObj(com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest[] aaVIRequestObj) {
        this.aaVIRequestObj = aaVIRequestObj;
    }

    public com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest getAaVIRequestObj(int i) {
        return aaVIRequestObj[i];
    }

    public void setAaVIRequestObj(int i, com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest value) {
        this.aaVIRequestObj[i] = value;
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
        VirtualInventoryAccess history = (VirtualInventoryAccess) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        VirtualInventoryAccess other = (VirtualInventoryAccess) obj;
        boolean _equals;
        _equals = true
            && ((this.aaVIRequestObj==null && other.getAaVIRequestObj()==null) || 
             (this.aaVIRequestObj!=null &&
              java.util.Arrays.equals(this.aaVIRequestObj, other.getAaVIRequestObj())));
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
        VirtualInventoryAccess history = (VirtualInventoryAccess) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getAaVIRequestObj() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAaVIRequestObj());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAaVIRequestObj(), i);
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
