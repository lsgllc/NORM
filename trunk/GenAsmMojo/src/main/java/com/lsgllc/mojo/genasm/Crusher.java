package com.lsgllc.mojo.genasm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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



    public enum DELIM_TYPE{STATIC,RUNTIME}

    public static String staticDelim;
    public static String runtimeDelim;
    public static String runtimeSubString;
    public static String excludedFromSource;
    private static boolean openForBusiness;
    private static String classSeparator;
    private static String classIfaceSearchStr;
    private static String classIfaceTargetdStr;

    private static String normPackage;

    public static String makeReplacable(String key){
        return staticDelim + key + staticDelim;
    }
    public static final Properties p = new Properties();
    public static final Properties sourceValues = new Properties();

    public static Crusher getInstance(String cfgPropertiesFilename, String inputPropertyFilename) throws IOException {
        openForBusiness = !(cfgPropertiesFilename == null || cfgPropertiesFilename.isEmpty() ||
                inputPropertyFilename == null || inputPropertyFilename.isEmpty());
        if (!openForBusiness){
            return null;
        }
        p.load(new FileInputStream(cfgPropertiesFilename));
        sourceValues.load(new FileInputStream(inputPropertyFilename+".properties"));
        staticDelim = p.getProperty("crusher.propKey.delim");
        runtimeDelim = p.getProperty("crusher.runtime.delim");
        excludedFromSource = p.getProperty("crusher.excludes");
        classSeparator = p.getProperty("crusher.classlist.separator");
        normPackage = p.getProperty("crusher.norm.package");
        classIfaceSearchStr = p.getProperty("crusher.class.iface.searchName");
        classIfaceTargetdStr = p.getProperty("crusher.class.iface.targetName");
        runtimeSubString = runtimeDelim + p.getProperty("crusher.runtime.substitution.string") + runtimeDelim;

        return ourInstance;
    }

    private Crusher() {

    }

    public KeyValuePair subtract(KeyValuePair<String,String> crusherInput, boolean exclude, DELIM_TYPE dt) {


        if (!openForBusiness){
            return null;
        }
        String delim = resolveDelim(dt);
        String sourceValue = sourceValues.getProperty(crusherInput.key);
        crusherInput.val = crusherInput.val.replace(classIfaceTargetdStr,classIfaceSearchStr);
        String workingVar = null;
        if (sourceValue == null || (crusherInput.val.length() <= sourceValue.length())){
            return null;
        }
        //<T:Lcom/lsgllc/norm/kernel/graph/model/instance/types/INSTANCE_TYPE;K:Lcom/lsgllc/norm/kernel/graph/model/instance/identity/impl/ObjectInstanceId;V::Lcom/lsgllc/norm/kernel/graph/model/instance/INormInstance<**TT;>;>Lcom/lsgllc/norm/kernel/graph/model/instance/impl/owl/AbstractEntityInstance<TK;TV;TT;>;Lcom/lsgllc/norm/kernel/core/normgen/templates/uom/common/person/IPersonNative<Lcom/lsgllc/norm/kernel/core/normgen/templates/uom/common/person/PERSON_TYPE;>;Lcom/lsgllc/norm/client/persistence/INormPersistable;
        KeyValuePair result;
//                String workingVar = sourceValue;
        StringBuffer calcValue = new StringBuffer();
        if (exclude){
            String[] leftOvers = sourceValue.split(excludedFromSource + classSeparator);
            if (leftOvers != null && leftOvers.length >0){
                for (String seg: leftOvers){
                    if (seg != null && seg.length()>0){
                        calcValue.append(seg);
                    }
                }
                workingVar = calcValue.toString();
            }
            sourceValue = workingVar;
        }

        int indx = crusherInput.val.indexOf(sourceValue);
        if (indx != -1){
            StringBuffer intres = new StringBuffer();
            String rtVal =runtimeSubString;
            if (indx > 0) {
                intres.append(crusherInput.val.substring(0,indx)).append(rtVal);
            } else {
                intres.append(rtVal).append(crusherInput.val.substring(0,indx));
            }
            if (indx + sourceValue.length() < crusherInput.val.length()){
                intres.append(crusherInput.val.substring(indx + sourceValue.length()));
            }
            result = new KeyValuePair(crusherInput.key,intres.toString());
        } else {
            result = null;
        }
        return result;
    }
    //;Lcom/lsgllc/norm/kernel/core/normgen/templates/uom/common/person/IPersonNormReady<Lcom/lsgllc/norm/kernel/core/normgen/templates/uom/common/person/PERSON_TYPE;>;Lcom/lsgllc/norm/client/persistence/INormPersistable;
    private String resolveDelim(DELIM_TYPE dt) {
        switch (dt) {
            case STATIC:
                return staticDelim;
            case RUNTIME:
                return runtimeDelim;
        }
        return null;
    }

    public KeyValuePair makeRuntimeSubstitutions(KeyValuePair<String,String> crusherOutput, String remove,DELIM_TYPE dt ) {
        int index = 0;
        if (remove != null && remove.length()>0){
            index = remove.indexOf("\"L");
            if (index < 0){
                index = 0;
            } else {
                index += 2;
            }
        }
        StringBuffer target = new StringBuffer();
        if (index > 0){
            target.append(crusherOutput.val.substring(0,index-1));
        }
        target.append(runtimeSubString);
        if (index > 0){
            int lstQuoteIndex = remove.lastIndexOf("\"");
            if ((lstQuoteIndex > index) && lstQuoteIndex <= remove.length()){
                target.append(remove.substring(lstQuoteIndex));
            }
        }
        crusherOutput.val = target.toString();
        return crusherOutput;
    }

    public boolean isOpenForBusiness() {
        return openForBusiness;
    }
    public String buildReplaceProperty(String property) {
        return staticDelim + property + staticDelim;
    }

//    public ArrayList<KeyValuePair<String, String>> crunchSignature(KeyValuePair<String, String> kvp) {
//        String[] components = kvp.val.split()
//        return null;
//    }

}
