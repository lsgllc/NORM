package com.lsgllc.mojo.genasm;

import org.objectweb.asm.*;
import org.objectweb.asm.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: sameloyiv
 * Date: 4/22/13
 * Time: 11:01 AM
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
public class NormGenASMifier extends ASMifier {

    protected static boolean commentOutCode;
    public static PropertyFileMaker propertyFileMaker;
    private static String outfileName;
    private static String methodTypeName;
    private static String setterVarName;
    private static Boolean isGetter = false;
    private static Boolean isSetter = false;
    private static Boolean isConstruct = false;
    private static KeyMaker key = null;
    private static Crusher crusher;
    protected static int getterId = 0;
    protected static int setterId = 0;
    protected static int constructId = 0;
    protected static int visitMethodInsnCntr = 0;
    protected static int visitLdcInsnCntr = 0;

    //    public NormGenASMifier(String className, String outFilename) throws IOException {
//        this(className,outFilename,false);
//
//    }
//
    private NormGenASMifier(final String ofn) {
        super();
//        propertyFileMaker = getPropertyFileMaker();
        outfileName = ofn;
    }

    private NormGenASMifier(int api, String name, int id, final String ofn) {
        super(api, name, id);
//        propertyFileMaker = getPropertyFileMaker();
        outfileName = ofn;
    }
    public NormGenASMifier(int api, String className, int id, final String outFilename, boolean commOutCode, String crusherPropFleName, String inputPropertyFilename, final KeyMaker km, boolean head) throws IOException {
        this(api, className, id, outFilename);

        if ((crusherPropFleName == null && inputPropertyFilename == null) && !head){
            return;
        }
        try {
            if (crusherPropFleName != null){
                crusher = Crusher.getInstance(crusherPropFleName, inputPropertyFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int flags = commentOutCode?0:ClassReader.SKIP_DEBUG;
        outfileName = outFilename;

        getterId = 0;
        setterId = 0;
        constructId = 0;
        commentOutCode = commOutCode;

        propertyFileMaker = getPropertyFileMaker();

        key = km;
        ClassReader cr;
        cr = new ClassReader(new FileInputStream(className));
//        key.push("Class");
        cr.accept(new NormGenAsmTraceClassVisitor(null, this, new PrintWriter(
                new FileOutputStream(outFilename+".txt")),propertyFileMaker,key), flags);
//        key.pop();
        propertyFileMaker.saveAndClose();
        propertyFileMaker = null;

    }


    private PropertyFileMaker getPropertyFileMaker() {
//        if (outfileName == null || outfileName.isEmpty())   {
//
//            return null;
//        }
        try {
            if (propertyFileMaker == null) {
                propertyFileMaker = new PropertyFileMaker(outfileName);
            }
            return propertyFileMaker;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        key.push(((access & Opcodes.ACC_INTERFACE) != 0?"Interface":"Class") + ".visit");
        super.visit(version, access, name, signature, superName, interfaces);
        String theKey = key.buildKey();

        name =  processSimple(new KeyValuePair<String,String>(theKey + ".name", name),0,Crusher.DELIM_TYPE.RUNTIME,"crusher.class.name.substitution.string");
        signature = processSignature(new KeyValuePair<String, String>(theKey + ".signature", signature),superName);
        superName = processSimple(new KeyValuePair<String,String>(theKey + ".superName", superName),0,Crusher.DELIM_TYPE.RUNTIME,"crusher.class.super.substitution.string");
//        propertyFileMaker.makeSplProperty(theKey + ".version", version, "ints");
//        kvp = propertyFileMaker.makeSplProperty(theKey + ".access", access, "ints");
//        kvp = propertyFileMaker.makeProperty(theKey + ".name", new String[]{name});
//        kvp = propertyFileMaker.makeProperty(theKey + ".signature",new String[]{signature});
//        kvp = propertyFileMaker.makeProperty(theKey + ".interfaces", interfaces);
//        KeyValuePair crusherOutput = crusher.subtract(crusherInput, superName);
//        crusher.makeRuntimeSubstitutions(crusherOutput, this.text);
//        consumeGenText(theKey, this.text);
//        key.pop();
        //commentText();
    }

    private void consumeGenText(String theKey, List<String> text) {
        if (crusher == null){
            return;
        }
            int i = 0;
        for (String lne: text){
            propertyFileMaker.makeProperty(theKey + ".line." + i++,new String[]{lne});
        }

    }

    private String  processSignature(KeyValuePair<String, String> kvp,String excluded) {
        KeyValuePair<String, String> locKvp = null;
        if (crusher != null){
            locKvp = crusher.subtract(kvp,true,Crusher.DELIM_TYPE.RUNTIME);
        }
        propertyFileMaker.makeProperty((locKvp == null?kvp.key:locKvp.key),new String[]{(locKvp == null?kvp.val:locKvp.val)});
        return kvp.val;
    }

    private String processSimple(KeyValuePair<String,String> kvp, int index, Crusher.DELIM_TYPE dt, String propKey) {
        if (crusher != null){
            kvp = crusher.makeRuntimeSubstitutions(kvp,null,Crusher.DELIM_TYPE.RUNTIME);
        }
        propertyFileMaker.makeProperty(kvp.key,new String[]{kvp.val});
        return kvp.val;
    }

//    private String settert(String canonicalName) {
//        int iOfS = canonicalName.lastIndexOf("/");
//        return (iOfS >= 0)?canonicalName.substring(iOfS + 1):canonicalName;
//    }

    private void commentText(List asmf){
        if (!commentOutCode){
            return;
        }
        List<Object> newText = new ArrayList<Object>();
        commentTextInner((asmf == null)?this.getText():asmf, newText);
        this.text.clear();
        this.text.addAll(newText);
    }
    private void commentTextInner(List<Object> text, List<Object> newText) {

        for (Object obj: text){
            if (obj instanceof String){
                StringBuffer line = new StringBuffer((String) obj);
//                //("Starts with: '" + line.toString().charAt(0) + "'")  ;
                if (!line.toString().startsWith("#")){
                    int newlneIdx = 0;
                    String prefx = "";
                    if (line.toString().startsWith("{")){
                        newlneIdx = line.toString().indexOf('\n')+1;
                        prefx = line.substring(0,newlneIdx) + "#";
                    }
                    line.replace(0,line.length(),"#" + prefx + line.toString().substring(newlneIdx));
                }
                newText.add(line.toString());
//                //(line.toString());
            } else if (obj instanceof List){
                commentTextInner((List<Object>) obj, newText);
            }
        }
    }

    @Override
    public void visitSource(String file, String debug) {
        super.visitSource(file, debug);
        //commentText();
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        super.visitOuterClass(owner, name, desc);
        //commentText();
    }

    @Override
    public NormGenASMifier visitClassAnnotation(String desc, boolean visible) {
        key.push("visitClassAnnotation");
        NormGenASMifier asmf = (NormGenASMifier) super.visitClassAnnotation(desc, visible);
        propertyFileMaker.makeProperty(key.buildKey() + ".desc", new String[]{desc});
        key.pop();
        return asmf;
    }

    @Override
    public void visitClassAttribute(Attribute attr) {
        super.visitClassAttribute(attr);
        //commentText();
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        key.push("innerClass");
        super.visitInnerClass(name, outerName, innerName, access);
        key.pop();
        //commentText();
    }

    @Override
    public NormGenASMifier visitField(int access, String name, String desc, String signature, Object value) {
//        String uid = getUID("visitField");
//        propertyFileMaker.makeSplProperty("access." + uid, access, "ints");
//        propertyFileMaker.makeProperty("name." + uid, new String[]{name});
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("signature." + uid,new String[]{signature});
//        propertyFileMaker.makeSplProperty("value." + uid, value, "objs");
        NormGenASMifier asmf = (NormGenASMifier) super.visitField(access, name, desc, signature, value);
        //commentText();
        return asmf;
    }

    public String setBeanMethod(String name){
        isGetter = name.startsWith("get");
        isSetter = name.startsWith("set");
        isConstruct = name.equals("<init>");
        if (isGetter){
            getterId++;
        } else if (isSetter) {
            setterId++;
        } else if (isConstruct){
            constructId++;
        } else {
            return name;
        }
        StringBuffer res = new StringBuffer();
        methodTypeName = isGetter?"getter":(isSetter?"setter":"construct");
        res.append(methodTypeName);
//        res.append(isGetter ? getterId : (isSetter ? setterId : constructId));
        return res.toString();

    }
    @Override
    public NormGenASMifier visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        key.push("visitMethod." + setBeanMethod(name));
        NormGenASMifier asmf = (NormGenASMifier) super.visitMethod(access, name, desc, signature, exceptions);
        propertyFileMaker.makeProperty(key.buildKey() + ".value.desc",new String[]{desc});
        propertyFileMaker.makeProperty(key.buildKey() + ".value.signature",new String[]{signature});

        desc = Crusher.staticDelim + key.buildKey() + ".code.desc" + Crusher.staticDelim;
        propertyFileMaker.makeProperty(key.buildKey() + ".code.desc",new String[]{desc});
        signature = Crusher.staticDelim + key.buildKey() + ".code.signature" + Crusher.staticDelim;
        propertyFileMaker.makeProperty(key.buildKey() + ".code.signature",new String[]{signature});
//        key.pop();
//        commentText(null);
        return asmf;
    }

    private void buildMethodProperty(List text, String name) {
        int i = 0;
        String saveHead = key.pop();
        key.push("visitMethod." + methodTypeName + ".line.");
        for (Object s: text){
            if (s instanceof String){
                propertyFileMaker.makeProperty(key.buildKey() + i++,new String[]{(String) s});
            }
        }
        key.pop();
        key.push(saveHead);

    }

    private void incCounters(String parmName, String value){
        boolean propSet = propertyFileMaker.isCurrentKeySet(key + "." + parmName,value);
//        if (propSet){
//            if (isGetter){
//                getterAccess++;
//            } else if (isSetter){
//                setterAccess++;
//            } else {
//                constructAccess++;
//            }
//        }

    }

    @Override
    public void visitClassEnd() {
        super.visitClassEnd();
        //commentText();
    }

    @Override
    public void visit(String name, Object value) {
        key.push("visit");
        super.visit(name, value);
        propertyFileMaker.makeProperty(key.buildKey() + ".name",new String[]{name});
        propertyFileMaker.makeSplProperty(key.buildKey() + ".value", value, "objs");
        key.pop();
//        commentText(null);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        key.push("visitEnum" + "." + name);
        super.visitEnum(name, desc, value);
        propertyFileMaker.makeProperty(key.buildKey(), new String[]{name});
        propertyFileMaker.makeProperty(key.buildKey() + ".desc" ,new String[]{desc});
        propertyFileMaker.makeProperty(key.buildKey() + ".value",new String[]{value});
        key.pop();
        //commentText();
    }

    @Override
    public NormGenASMifier visitAnnotation(String name, String desc) {
        key.push("visitAnnotation" + "." + name);
        NormGenASMifier asmf = (NormGenASMifier) super.visitAnnotation(name, desc);
        propertyFileMaker.makeProperty(key.buildKey(), new String[]{name});
        propertyFileMaker.makeProperty(key.buildKey() + ".desc" ,new String[]{desc});
        key.pop();
        return asmf;
    }

    @Override
    public NormGenASMifier visitArray(String name) {
//        String uid = getUID("visitArray");
//        propertyFileMaker.makeProperty("name." + uid, new String[]{name});
        NormGenASMifier asmf = (NormGenASMifier) super.visitArray(name);
        //commentText();
        return asmf;
    }

    @Override
    public void visitAnnotationEnd() {
        super.visitAnnotationEnd();
        //commentText();
    }

    @Override
    public NormGenASMifier visitFieldAnnotation(String desc, boolean visible) {
        key.push("visitFieldAnnotation");
        NormGenASMifier asmf = (NormGenASMifier) super.visitFieldAnnotation(desc, visible);
        propertyFileMaker.makeProperty(key.buildKey(), new String[]{desc});
//        String uid = getUID("visitFieldAnnotation");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
        key.pop();
        return asmf;
    }

    @Override
    public void visitFieldAttribute(Attribute attr) {
        super.visitFieldAttribute(attr);
        //commentText();
    }

    @Override
    public void visitFieldEnd() {
        super.visitFieldEnd();
        //commentText();
    }

    @Override
    public NormGenASMifier visitAnnotationDefault() {
        key.push("visitAnnotationDefault");
        NormGenASMifier asmf = (NormGenASMifier) super.visitAnnotationDefault();
        key.pop();
        //commentText();
        return  asmf;
    }

    @Override
    public NormGenASMifier visitMethodAnnotation(String desc, boolean visible) {
        key.push("visitMethodAnnotation");
        NormGenASMifier asmf = (NormGenASMifier) super.visitMethodAnnotation(desc, visible);
//        String uid = getUID("visitMethodAnnotation");
        propertyFileMaker.makeProperty(key.buildKey() + ".desc",new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
        //commentText();
        return asmf;
    }

    @Override
    public NormGenASMifier visitParameterAnnotation(int parameter, String desc, boolean visible) {
        NormGenASMifier asmf = (NormGenASMifier) super.visitParameterAnnotation(parameter, desc, visible);
        key.push("visitParameterAnnotation");
        propertyFileMaker.makeProperty(key.buildKey() + ".desc",new String[]{desc});
        propertyFileMaker.makeProperty(key.buildKey() + ".visible",new String[]{((visible)?"true":"false")});
        key.pop();
        //commentText();
        return asmf;
    }

    @Override
    public void visitMethodAttribute(Attribute attr) {
        super.visitMethodAttribute(attr);
        //commentText();
    }

    @Override
    public void visitCode() {
        super.visitCode();
        //commentText();
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);
        //commentText();
    }

    @Override
    public void visitInsn(int opcode) {
//        key.push("insn");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".opcode", opcode, "ints");
        super.visitInsn(opcode);
//        key.pop();
        //commentText();
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
//        String uid = getUID("intInsn");
//        key.push("visitIntInsn");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".opcode", opcode, "ints");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".operand" , operand, "ints");
        super.visitIntInsn(opcode, operand);
//        key.pop();
        //commentText();
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
//        key.push("visitVarInsn");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".opcode", opcode, "ints");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".var" , var, "ints");
        super.visitVarInsn(opcode, var);
//        key.pop();
        //commentText();
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
//        key.push("visitTypeInsn");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".opcode", opcode, "ints");
//        propertyFileMaker.makeProperty(key.buildKey() + ".type" , new String[]{type});
        super.visitTypeInsn(opcode, type);
//        key.pop();
        //commentText();
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
//        String uid = getUID("visitFieldInsn");
//        propertyFileMaker.makeSplProperty("opcode." + uid, opcode, "ints");
//        propertyFileMaker.makeProperty("owner." + uid,new String[]{owner});
//        propertyFileMaker.makeProperty("name." + uid,new String[]{name});
//        propertyFileMaker.makeProperty("type." + uid,new String[]{owner});
//        super.visitFieldInsn(opcode, owner, name, desc);
        //commentText();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        key.push("visitMethodInsn." + visitMethodInsnCntr++ );
        super.visitMethodInsn(opcode, owner, name, desc);
        propertyFileMaker.makeProperty(key.buildKey() + ".value.name" , new String[]{name});
        if (!owner.contains("java/")){
            owner = owner.substring(0,owner.lastIndexOf("/") + 1) + Crusher.runtimeSubString;
            propertyFileMaker.makeProperty(key.buildKey() + ".value.owner" , new String[]{owner});
        }
        propertyFileMaker.makeProperty(key.buildKey() + ".value.desc" , new String[]{desc});
        name = Crusher.staticDelim + key.buildKey() + ".code.name" + Crusher.staticDelim;
        propertyFileMaker.makeProperty(key.buildKey() + ".code.name" , new String[]{name});
        owner = Crusher.staticDelim + key.buildKey() + ".code.owner" + Crusher.staticDelim;
        propertyFileMaker.makeProperty(key.buildKey() + ".code.owner" , new String[]{owner});
        desc = Crusher.staticDelim + key.buildKey() + ".code.desc" + Crusher.staticDelim;
        propertyFileMaker.makeProperty(key.buildKey() + ".code.desc" , new String[]{desc});

        key.pop();
        //commentText();
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitInvokeDynamicInsn",new String[]{"visitInvokeDynamicInsn"});

        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        //commentText();
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitJumpInsn",new String[]{"***ERROR***"});
        super.visitJumpInsn(opcode, label);
        //commentText();
    }

    @Override
    public void visitLabel(Label label) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitLabel",new String[]{"***ERROR***"});
        super.visitLabel(label);
        //commentText();
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if (cst instanceof String){
            key.push("visitLdcInsn." + visitLdcInsnCntr++);
        }
        super.visitLdcInsn(cst);
        if (cst instanceof String){
            KeyValuePair res =  new KeyValuePair(null,null);
            res = crusher.makeRuntimeSubstitutions(res, (String) cst,null);
            propertyFileMaker.makeProperty(key.buildKey() + ".value.name" , new String[]{res.val.toString()});
            cst = Crusher.runtimeSubString;
            key.pop();
        }
        //commentText();
    }

    @Override
    public void visitIincInsn(int var, int increment) {
//        String uid = getUID("visitIincInsn");
//        propertyFileMaker.makeSplProperty("var." + uid, var, "ints");
//        propertyFileMaker.makeSplProperty("increment." + uid, var, "ints");
        super.visitIincInsn(var,increment);
        //commentText();
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitTableSwitchInsn",new String[]{"***ERROR***"});
        super.visitTableSwitchInsn(min, max, dflt, labels);
        //commentText();
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitLookupSwitchInsn",new String[]{"***ERROR***"});
        super.visitLookupSwitchInsn(dflt, keys, labels);
        //commentText();
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
//        String uid = getUID("visitMultiANewArrayInsn");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeSplProperty("dims." + uid, dims, "ints");
        super.visitMultiANewArrayInsn(desc, dims);
        //commentText();
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitTryCatchBlock",new String[]{"***ERROR***"});
        super.visitTryCatchBlock(start, end, handler, type);
        //commentText();
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitLocalVariable",new String[]{"***ERROR***"});
        super.visitLocalVariable(name, desc, signature, start, end, index);
        //commentText();
    }

    @Override
    public void visitLineNumber(int line, Label start) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitLineNumber",new String[]{"***ERROR***"});
        super.visitLineNumber(line, start);
        //commentText();
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
//        key.push("visitMaxs");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".maxStack", maxStack, "ints");
//        propertyFileMaker.makeSplProperty(key.buildKey() + ".maxLocals" , maxLocals, "ints");
        super.visitMaxs(maxStack, maxLocals);
//        key.pop();
        //commentText();
    }

    @Override
    public void visitMethodEnd() {
//        commentText(null);
        super.visitMethodEnd();
        buildMethodProperty(this.text, name);
        visitMethodInsnCntr = 0;
        visitLdcInsnCntr = 0;
        //commentText();
    }

    @Override
    public List getText() {
        List asmf = super.getText();
        return asmf;
    }

    @Override
    public void print(PrintWriter pw) {
        commentText(null);
        super.print(pw);
        //commentText();
    }

    @Override
    public NormGenASMifier visitAnnotation(String desc, boolean visible) {
//        String uid = getUID("visitAnnotation2");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
        NormGenASMifier asmf = (NormGenASMifier) super.visitAnnotation(desc, visible);
        //commentText();
        return asmf;
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
        //commentText();
    }

    @Override
    protected NormGenASMifier createASMifier(String name, int id) {
        //commentText();
        return createNormASMifier(name,id, outfileName);
    }

    @Override
    protected void appendConstant(Object cst) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.appendConstant",new String[]{"***ERROR***"});
        super.appendConstant(cst);
        //commentText();
    }

    @Override
    protected void declareLabel(Label l) {
//        propertyFileMaker.makeProperty("error.no.properties.defined.declareLabel",new String[]{"***ERROR***"});
        super.declareLabel(l);
        //commentText();
    }

    @Override
    protected void appendLabel(Label l) {
//        propertyFileMaker.makeProperty("error.no.properties.appendLabel",new String[]{"***ERROR***"});
        super.appendLabel(l);
        //commentText();
    }
    protected NormGenASMifier createNormASMifier(final String name, final int id, final String ofn) {
        try {
            return new NormGenASMifier(Opcodes.ASM4, name, id,ofn,commentOutCode,null,null,key, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
