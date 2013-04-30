package com.lsgllc.mojo.genasm;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 11:31 PM
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
public class Merger {
    public final static String REPLACE_BRACKET = "%@$";
    public static String makeReplacable(String key){
        return REPLACE_BRACKET + key + REPLACE_BRACKET;
    }
}
