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
    private final String propertyPrefix;
    private final boolean addPrefix;
    public PropertyFileMaker(String propertyFileName, boolean addPrefix) throws FileNotFoundException {
        this.propertyFileName = propertyFileName+".properties";
        this.propertyFile = new FileOutputStream(this.propertyFileName);
        if (propertyFile == null )  {
            System.out.println("PROPERTY FILE NOT CREATED...");
        }
        this.addPrefix = addPrefix;
        this.propertyPrefix =  (this.addPrefix)?propertyFileName.substring(propertyFileName.lastIndexOf('/')+1).replace("/",".")+".":"";

        this.p = new Properties();

        p.setProperty("ints","");
        p.setProperty("objs","");
    }

    public void makeProperty(String key, String[] strings) {
        if (strings == null){
            return;
        }
        System.out.println("SETTING PROPERTY");
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
        if (sb.length()>0){
            System.out.println("PROPERTY IS SET");
            p.setProperty(this.propertyPrefix+key,sb.toString());
        }
        try {
            propertyFile.flush();
        } catch (IOException e) {
            System.out.println("FLUSHING failed");
            e.printStackTrace();
        }
    }

    public void saveAndClose() throws IOException {
        System.out.println("FLUSHING PROPERTYFILE");
        p.store(this.propertyFile,"GenAsmMojo PropertyFile: " + this.propertyFileName);
        propertyFile.flush();
        System.out.println("PROPERTYFILE FLUSHED");
        System.out.println("CLOSING PROPERTYFILE");
        propertyFile.close();
        System.out.println("PROPERTYFILE CLOSED");
    }


    public void makeSplProperty(String key, Object rawVal, String splType) {
        String theInts =  p.getProperty(splType);
        String prefix = (theInts != null && !theInts.isEmpty())?",":"";
        String val = (splType.equals("objs")?rawVal.toString():Integer.toString((Integer) rawVal));
        p.setProperty(splType,theInts+prefix+val);
        makeProperty(key, new String[]{val});
    }

}
