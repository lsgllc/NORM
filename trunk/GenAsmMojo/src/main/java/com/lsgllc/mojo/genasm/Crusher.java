package com.lsgllc.mojo.genasm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created By: sameloyiv
 * Date: 5/3/13
 * Time: 3:02 PM
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
public class Crusher {
    private static Crusher ourInstance = new Crusher();

    public static String delim;
    public static String[] excludedFromSource;
    public static String makeReplacable(String key){
        return delim + key + delim;
    }
    public static final Properties p = new Properties();
    public static final Properties sourceValues = new Properties();

    public static Crusher getInstance(String cfgPropertiesFilename, String implSrc1) throws IOException {
        if (cfgPropertiesFilename == null || cfgPropertiesFilename.isEmpty() ||
                implSrc1 == null || implSrc1.isEmpty()){
            return null;
        }
        p.load(new FileInputStream(cfgPropertiesFilename));
        sourceValues.load(new FileInputStream(implSrc1+".properties"));
        delim = p.getProperty("crusher.delimeter");
        excludedFromSource = p.getProperty("crusher.excludes").split(",");
        return ourInstance;
    }

    private Crusher() {

    }

    public static String subtract(String left, String srcPropertyKey){
        String result;
        String sourceValue = sourceValues.getProperty(srcPropertyKey);

        if (sourceValue == null || (left.length() <= sourceValue.length())){
            result = null;
        }

        if (excludedFromSource != null && excludedFromSource.length > 0){
            String workingVar = sourceValue;
            for(String exclde: excludedFromSource){
                String[] leftOvers = workingVar.split(exclde);
                if (leftOvers != null && leftOvers.length >0){
                    workingVar = leftOvers.toString();
                }
            }
            sourceValue = workingVar;
        }
        int indx = left.indexOf(sourceValue);
        if (indx != -1){
            result = left.substring(indx + sourceValue.length());
        } else {
            result = null;
        }

//        left.
        return result;
    }
}
