package com.lsgllc.mojo.genasm;

/**
 * Created By: sameloyiv
 * Date: 5/7/13
 * Time: 2:18 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2013
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class KeyValuePair<K,V> {
    public K  key;
    public V  val;
    public KeyValuePair(K key, V val) {
        this.key = key;
        this.val = val;
    }
}
