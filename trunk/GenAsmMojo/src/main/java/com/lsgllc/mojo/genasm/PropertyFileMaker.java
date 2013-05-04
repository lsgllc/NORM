package com.lsgllc.mojo.genasm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 10:35 PM
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
public class PropertyFileMaker {
    private final FileOutputStream propertyFile;
    private final Properties p;
    private final String propertyFileName;
    public PropertyFileMaker(String propertyFileName) throws FileNotFoundException {
        this.propertyFileName = propertyFileName+".properties";
        this.propertyFile = new FileOutputStream(this.propertyFileName);
        if (propertyFile == null )  {
            //("PROPERTY FILE NOT CREATED...");
        }
//        this.propertyPrefix =  (this.addPrefix)?propertyFileName.substring(propertyFileName.lastIndexOf('/')+1).replace("/",".")+".":"";

        this.p = new Properties();

        p.setProperty("ints","");
        p.setProperty("objs","");
    }

    public void makeProperty(String key, String[] strings) {
        if (strings == null){
            return;
        }
        //("SETTING PROPERTY");
        StringBuffer sb = makeCommaSeparated(strings);
        if (sb.length()>0){
            //("PROPERTY IS SET");
            p.setProperty(key,sb.toString());
        }
        try {
            propertyFile.flush();
        } catch (IOException e) {
            //("FLUSHING failed");
            e.printStackTrace();
        }
    }

    private StringBuffer makeCommaSeparated(String[] strings) {
        StringBuffer sb = new StringBuffer();
        boolean pComma = false;
        for (String s: strings){
            if (s != null && !s.isEmpty()){
                if (pComma){
                    sb.append(",");
                }
                sb.append(s);
                pComma = true;
            }
        }
        return sb;
    }

    public boolean isCurrentKeySet(String key, String value){

        return ((p.getProperty(key) != null) && p.getProperty(key).equals(value));
    }
    public void saveAndClose() throws IOException {
        //("FLUSHING PROPERTYFILE");
        p.store(this.propertyFile,"GenAsmMojo PropertyFile: " + this.propertyFileName);
        propertyFile.flush();
        //("PROPERTYFILE FLUSHED");
        //("CLOSING PROPERTYFILE");
        propertyFile.close();
        //("PROPERTYFILE CLOSED");
    }


    public void makeSplProperty(String key, Object rawVal, String splType) {
        String theInts =  p.getProperty(splType);
        String prefix = (theInts != null && !theInts.isEmpty())?",":"";
        String val = (splType.equals("objs")?rawVal.toString():(!(rawVal instanceof Boolean)?Integer.toString((Integer) rawVal):((Boolean)rawVal?"true":"false")));
        if (!(rawVal instanceof Boolean)){
            p.setProperty(splType,theInts+prefix+val);
        }
        makeProperty(key, new String[]{val});
    }

}
