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

    public static synchronized KeyMaker  getInstance() {
        synchronized (ourInstance){
            ourInstance.clear();
            return ourInstance;
        }
    }

    private KeyMaker() {
    }

    @Override
    public String push(String item) {
        int iOfS = item.lastIndexOf("/");
        String theItem = (iOfS >= 0)?item.substring(iOfS + 1):item;
        synchronized (ourInstance){
            return super.push(theItem);
        }
    }

    @Override
    public synchronized String pop() {

        synchronized (ourInstance){
            return super.pop();
        }
    }

    @Override
    public synchronized String peek() {
        synchronized (ourInstance){
            return super.peek();
        }
    }

    @Override
    public boolean empty() {
        synchronized (ourInstance){
            return super.empty();
        }
    }

    public void saveArray(String[] array){
        for (String s: array){
            this.push(s);
        }
    }

    public String buildKey(){

        synchronized (ourInstance){
            if (super.isEmpty()){
                return null;
            }
        }
        StringBuffer results = new StringBuffer();
        return getKey(results);
    }

    private String getKey(StringBuffer results) {
        System.out.println("get key: " + results.toString());
        String nextComponent = null;
        System.out.println("bout to pop..." );
        nextComponent = ourInstance.pop();
        if (!super.isEmpty())  {
            getKey(results);
        }
        if (nextComponent != null){
            System.out.println("Component found: " + nextComponent);
            results.append((super.elementCount > 0)?".":"").append(nextComponent);
            ourInstance.push(nextComponent);
        }
        return results.toString();
    }
}
