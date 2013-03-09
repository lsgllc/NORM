package com.txdot.isd.rts.services.util;

/**
 * An interface that every class that gets written to the Transaction cache must implement in order to be displayed
 * correctly in ShowCache
 * 
 * @date (10/3/01 9:29:12 AM)
 * @author: Michael Abernethy
 * 
 *  ---------------------------------------------------------------
 *  Change History:
 *  Name        Date        Description
 * 
 *  ---------------------------------------------------------------
 */
public interface Displayable {
/**
 * Returns a Map of the internal attributes.  Implementers of this method should use introspection to display their internal
 * variables and values
 * @return java.util.Map
 */
java.util.Map getAttributes();
}
