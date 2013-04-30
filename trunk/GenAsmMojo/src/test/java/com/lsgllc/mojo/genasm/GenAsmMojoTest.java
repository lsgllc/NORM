package com.lsgllc.mojo.genasm;

import com.lsgllc.mojo.genasm.GenAsmMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* GenAsmMojo Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 24, 2013</pre> 
* @version 1.0 
*/ 
public class GenAsmMojoTest {
/*
        <norm.asm.model.native.className>gov.state.tx.dmv.uom.common.person.impl.PersonNative</norm.asm.model.native.className>
        <norm.asm.model.ready.className>gov.state.tx.dmv.uom.common.person.impl.PersonNormReady</norm.asm.model.ready.className>
        <norm.asm.model.native.iface>gov.state.tx.dmv.uom.common.person.IPersonNative</norm.asm.model.native.iface>
        <norm.asm.model.ready.iface>gov.state.tx.dmv.uom.common.person.IPersonNormReady</norm.asm.model.ready.iface>
        <norm.asm.dest.rsrc.dir>/Users/sameloyiv/IdeaProjects/NORM/trunk/NORM-Core/src/main/resources</norm.asm.dest.rsrc.dir>
        <norm.asm.commentOutCode>false</norm.asm.commentOutCode>
 */
    protected GenAsmMojo gam = new GenAsmMojo();


@Before
public void before() throws Exception {
    gam.jmNativeImpl = "gov.state.tx.dmv.uom.common.person.impl.PersonNative";
    gam.jmNativeIface = "gov.state.tx.dmv.uom.common.person.IPersonNative";
    gam.jmReadyImpl = "gov.state.tx.dmv.uom.common.person.impl.PersonNormReady";
    gam.jmReadyIface = "gov.state.tx.dmv.uom.common.person.IPersonNormReady";
    gam.commentOutCode = false;
    gam.resourceDirectory = "/Users/sameloyiv/IdeaProjects/NORM/trunk/NORM-Core/src/main/resources";
    gam.outputDir="/Users/sameloyiv/IdeaProjects/NORM/trunk/TXDMV-UOM/target";
}
@Test
public void all(){
    try {
        gam.execute();
    } catch (MojoExecutionException e) {
        e.printStackTrace();
    }
}
@After
public void after() throws Exception {

} 

/** 
* 
* Method: execute() 
* 
*/ 
@Test
public void testExecute() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: processClassFile(String fileName) 
* 
*/ 
@Test
public void testProcessClassFile() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = GenAsmMojo.getClass().getMethod("processClassFile", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: getCanonicalPropertyFileNameMajor(String fileNameMajor) 
* 
*/ 
@Test
public void testGetCanonicalPropertyFileNameMajor() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = GenAsmMojo.getClass().getMethod("getCanonicalPropertyFileNameMajor", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: getFileNameMajor(String fileName) 
* 
*/ 
@Test
public void testGetFileNameMajor() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = GenAsmMojo.getClass().getMethod("getFileNameMajor", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
