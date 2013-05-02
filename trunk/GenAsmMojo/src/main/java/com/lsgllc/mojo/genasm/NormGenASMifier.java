package com.lsgllc.mojo.genasm;

import org.junit.Ignore;
import org.objectweb.asm.*;
import org.objectweb.asm.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

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
    private static PropertyFileMaker propertyFileMaker;
    private static String outfileName;
    private static Boolean isGetter = false;
    private static Boolean isSetter = false;
    private static Boolean isConstruct = false;
    private static Stack<String> methodStack = new Stack<String>();

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

    public NormGenASMifier(String className, final String outFilename, boolean commOutCode) throws IOException {
        int i = 0;
        int flags = commentOutCode?0:ClassReader.SKIP_DEBUG;
        outfileName = outFilename;

        commentOutCode = commOutCode;
        propertyFileMaker = getPropertyFileMaker();

        ClassReader cr;
        cr = new ClassReader(new FileInputStream(className));
        cr.accept(new NormGenAsmTraceClassVisitor(null, new NormGenASMifier(outfileName), new PrintWriter(
                new FileOutputStream(outFilename+".txt")),propertyFileMaker), flags);
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
                propertyFileMaker = new PropertyFileMaker(outfileName, true);
            }
            return propertyFileMaker;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private NormGenASMifier(int api, String name, int id, final String ofn) {
        super(api, name, id);
//        propertyFileMaker = getPropertyFileMaker();
        outfileName = ofn;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        String uid = getUID("visit");
//        propertyFileMaker.makeSplProperty("version." + uid, version, "ints");
//        propertyFileMaker.makeSplProperty("access." + uid, access, "ints");
//        propertyFileMaker.makeProperty("name." + uid, new String[]{name});
//        propertyFileMaker.makeProperty("signature." + uid,new String[]{signature});
//        propertyFileMaker.makeProperty("superName." + uid,new String[]{superName});
//        propertyFileMaker.makeProperty("interfaces." + uid,interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
        //commentText();
    }

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
//        String uid = getUID("visitClassAnnotation");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
//        desc = Merger.makeReplacable("classAnnotation..name");
//        desc = Merger.makeReplacable("classAnnotation..name");
        NormGenASMifier asmf = (NormGenASMifier) super.visitClassAnnotation(desc, visible);
        //commentText();
        return asmf;
    }

    @Override
    public void visitClassAttribute(Attribute attr) {
        super.visitClassAttribute(attr);
        //commentText();
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
//        String uid = getUID("visitInnerClass");
//        propertyFileMaker.makeProperty("name." + uid,new String[]{name});
//        propertyFileMaker.makeProperty("outerName." + uid,new String[]{outerName});
//        propertyFileMaker.makeProperty("innerName." + uid,new String[]{innerName});
        super.visitInnerClass(name, outerName, innerName, access);
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

    private static int getterAccess = 0;
    private static int setterAccess = 0;
    private static int constructAccess = 0;
    private static String key;
    private String getKey(String name){
        isGetter = name.startsWith("get");
        isSetter = name.startsWith("set");
        isConstruct = name.equals("<init>");
        String theKey = "visitMethod" + (isGetter?".getter.":(isSetter?".setter.":".construct.")) + this.id;
        String uid = getUID(theKey);
        return uid;

    }
    @Override
    public NormGenASMifier visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        key = getKey(name);
        methodStack.clear();
        propertyFileMaker.makeProperty(key+".stack", new String[]{name});
//        propertyFileMaker.makeSplProperty(uid + ".access." + uid, access, "ints");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("signature." + uid,new String[]{signature});
//        propertyFileMaker.makeProperty("exceptions." + uid,exceptions);
        NormGenASMifier asmf = (NormGenASMifier) super.visitMethod(access, name, desc, signature, exceptions);
//        commentText(null);
        return asmf;
    }
    private void incCounters(String parmName, String value){
        boolean propSet = propertyFileMaker.isCurrentKeySet(key + "." + parmName,value);
        if (propSet){
            if (isGetter){
                getterAccess++;
            } else if (isSetter){
                setterAccess++;
            } else {
                constructAccess++;
            }
        }

    }

    @Override
    public void visitClassEnd() {
        super.visitClassEnd();
        //commentText();
    }

    @Override
    public void visit(String name, Object value) {
        String uid = getUID("visit2");
//        propertyFileMaker.makeProperty("name." + uid,new String[]{name});
//        propertyFileMaker.makeSplProperty("value." + uid, value, "objs");
        super.visit(name, value);
//        commentText(null);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        String uid = getUID("visitEnum");
//        propertyFileMaker.makeProperty("name." + uid, new String[]{name});
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("value." + uid,new String[]{value});
        super.visitEnum(name, desc, value);
        //commentText();
    }

    private String getUID(String propName) {
        String uid = UUID.randomUUID().toString();
//        propertyFileMaker.makeProperty(propName +".UUID."+ uid,new String[]{uid});
        return propName;
    }

    @Override
    public NormGenASMifier visitAnnotation(String name, String desc) {
        String theKey = key + ".visitAnnotation";
        propertyFileMaker.makeProperty(theKey + ".name" , new String[]{name});
        propertyFileMaker.makeProperty(theKey + ".desc", new String[]{desc});
        incCounters(theKey, "name");
        incCounters(theKey,"desc");
        NormGenASMifier asmf = (NormGenASMifier) super.visitAnnotation(name, desc);
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
//        String uid = getUID("visitFieldAnnotation");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
        NormGenASMifier asmf = (NormGenASMifier) super.visitFieldAnnotation(desc, visible);
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
        NormGenASMifier asmf = (NormGenASMifier) super.visitAnnotationDefault();
        //commentText();
        return  asmf;
    }

    @Override
    public NormGenASMifier visitMethodAnnotation(String desc, boolean visible) {
//        String uid = getUID("visitMethodAnnotation");
//        propertyFileMaker.makeProperty("desc." + uid,new String[]{desc});
//        propertyFileMaker.makeProperty("visible." + uid,new String[]{((visible)?"true":"false")});
        NormGenASMifier asmf = (NormGenASMifier) super.visitMethodAnnotation(desc, visible);
        //commentText();
        return asmf;
    }

    @Override
    public NormGenASMifier visitParameterAnnotation(int parameter, String desc, boolean visible) {
        String theKey = key + ".visitParameterAnnotation";
        propertyFileMaker.makeSplProperty(theKey + ".value", parameter, "ints");
        propertyFileMaker.makeProperty(theKey + ".desc",new String[]{desc});
        propertyFileMaker.makeProperty(theKey + ".visible",new String[]{((visible)?"true":"false")});
        incCounters(theKey, "value");
        incCounters(theKey, "desc");
        incCounters(theKey, "visible");
        NormGenASMifier asmf = (NormGenASMifier) super.visitParameterAnnotation(parameter, desc, visible);
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
        String theKey = key + ".visitParameterAnnotation";
        propertyFileMaker.makeSplProperty(theKey + ".opcode", opcode, "ints");
        incCounters(theKey, "opcode");
        super.visitInsn(opcode);
        //commentText();
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
//        String uid = getUID("intInsn");
//        propertyFileMaker.makeSplProperty("opcode." + uid, opcode, "ints");
//        propertyFileMaker.makeSplProperty("operand." + uid, operand, "ints");
        super.visitIntInsn(opcode, operand);
        //commentText();
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        String theKey = key + ".visitVarInsn";
        propertyFileMaker.makeSplProperty(theKey + ".opcode" , opcode, "ints");
        propertyFileMaker.makeSplProperty(theKey + ".var", var, "ints");
        incCounters(theKey, "opcode");
        incCounters(theKey, "var");
        super.visitVarInsn(opcode, var);
        //commentText();
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        String theKey = key + ".visitTypeInsn";
        propertyFileMaker.makeSplProperty(theKey + ".opcode" , opcode, "ints");
        propertyFileMaker.makeProperty(theKey + ".type" , new String[]{type});
        incCounters(theKey, "opcode");
        incCounters(theKey, "type");
        super.visitTypeInsn(opcode, type);
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
        String theKey = key + ".visitMethodInsn";
        propertyFileMaker.makeSplProperty(theKey + ".opcode" , opcode, "ints");
        propertyFileMaker.makeProperty(theKey + ".owner" , new String[]{owner});
        propertyFileMaker.makeProperty(theKey + ".name" , new String[]{name});
        propertyFileMaker.makeProperty(theKey + ".desc" , new String[]{desc});
        incCounters(theKey, "opcode");
        incCounters(theKey, "owner");
        incCounters(theKey, "name");
        incCounters(theKey, "desc");

        super.visitMethodInsn(opcode, owner, name, desc);
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
//        propertyFileMaker.makeProperty("error.no.properties.defined.visitLdcInsn",new String[]{"***ERROR***"});
        super.visitLdcInsn(cst);
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
        String theKey = key + ".visitMaxs";
        propertyFileMaker.makeSplProperty(theKey + ".maxStack" , maxStack, "ints");
        propertyFileMaker.makeSplProperty(theKey + ".maxLocals" , maxLocals, "ints");
        incCounters(theKey, "maxStack");
        incCounters(theKey, "maxLocals");
        super.visitMaxs(maxStack, maxLocals);
        //commentText();
    }

    @Override
    public void visitMethodEnd() {
//        commentText(null);
        super.visitMethodEnd();
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
        return new NormGenASMifier(Opcodes.ASM4, name, id,ofn);
    }
}
