package com.lsgllc.mojo.genasm;

import java.util.Stack;

/**
 * Created By: sameloyiv
 * Date: 5/4/13
 * Time: 10:54 AM
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
public class KeyMaker extends Stack<String> {
    private static KeyMaker ourInstance = new KeyMaker();

    public static KeyMaker getInstance() {
        return ourInstance;
    }

    private KeyMaker() {
    }
    @Override
    public synchronized String pop() {

        return this.pop() + ".";
    }

    public synchronized void saveArray(String[] array){
        for (String s: array){
            this.push(s);
        }
    }

    public synchronized String buildKey(){
        if (this.isEmpty()){
            return null;
        }
        StringBuffer results = new StringBuffer();
        return getKey(results);
    }

    private String getKey(StringBuffer results) {
        if (this.isEmpty()){
            return null;
        }
        String component = this.pop();
        String result = getKey(results);
        if (result != null){
            results.append(result).append(component);
        }
        this.push(component);
        return results.toString();
    }
}
