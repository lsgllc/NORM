/**
 * TransactionAccessResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.server.webapps.order;

public class TransactionAccessResponse  implements java.io.Serializable {
    private com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse[] transactionAccessReturn;

    public TransactionAccessResponse() {
    }

    public com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse[] getTransactionAccessReturn() {
        return transactionAccessReturn;
    }

    public void setTransactionAccessReturn(com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse[] transactionAccessReturn) {
        this.transactionAccessReturn = transactionAccessReturn;
    }

    public com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse getTransactionAccessReturn(int i) {
        return transactionAccessReturn[i];
    }

    public void setTransactionAccessReturn(int i, com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse value) {
        this.transactionAccessReturn[i] = value;
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
        TransactionAccessResponse history = (TransactionAccessResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        TransactionAccessResponse other = (TransactionAccessResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.transactionAccessReturn==null && other.getTransactionAccessReturn()==null) || 
             (this.transactionAccessReturn!=null &&
              java.util.Arrays.equals(this.transactionAccessReturn, other.getTransactionAccessReturn())));
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
        TransactionAccessResponse history = (TransactionAccessResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getTransactionAccessReturn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactionAccessReturn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactionAccessReturn(), i);
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
