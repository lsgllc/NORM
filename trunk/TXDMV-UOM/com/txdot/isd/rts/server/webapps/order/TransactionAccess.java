/**
 * TransactionAccess.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class TransactionAccess  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest[] aaTransRequestObj;

    public TransactionAccess() {
    }

    public com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest[] getAaTransRequestObj() {
        return aaTransRequestObj;
    }

    public void setAaTransRequestObj(com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest[] aaTransRequestObj) {
        this.aaTransRequestObj = aaTransRequestObj;
    }

    public com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest getAaTransRequestObj(int i) {
        return aaTransRequestObj[i];
    }

    public void setAaTransRequestObj(int i, com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest value) {
        this.aaTransRequestObj[i] = value;
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
        TransactionAccess history = (TransactionAccess) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        TransactionAccess other = (TransactionAccess) obj;
        boolean _equals;
        _equals = true
            && ((this.aaTransRequestObj==null && other.getAaTransRequestObj()==null) || 
             (this.aaTransRequestObj!=null &&
              java.util.Arrays.equals(this.aaTransRequestObj, other.getAaTransRequestObj())));
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
        TransactionAccess history = (TransactionAccess) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getAaTransRequestObj() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAaTransRequestObj());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAaTransRequestObj(), i);
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
