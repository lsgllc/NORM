package com.lsgllc.norm.kernel.core.normgen.asm;


import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import org.objectweb.asm.*;

import java.util.HashSet;

/**
 * Created By: sameloyiv
 * Date: 12/18/12
 * Time: 4:08 PM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */

public class NormPersistentClassGenerator extends ClassVisitor implements Opcodes {

    private String owner;
    protected OntologyBroker graphManager;
    private HashSet<String> methodsGenerated = new HashSet<String>();

    public NormPersistentClassGenerator(final ClassVisitor cw) {
        super(Opcodes.ASM4, cw);
    }

    public NormPersistentClassGenerator(NormClassWriter cw, OntologyBroker graphManager) {
        this(cw);
        this.graphManager = graphManager;

    }

    public NormPersistentClassGenerator(NormClassWriter cw, OntologyBroker graphManager, String name) {
        this(cw, graphManager);
        this.owner = name;
    }

    @Override
    public void visit(final int version, final int access, final String name,
                      final String signature, final String superName,
                      final String[] interfaces) {

        NormSignatureWriter nsv = new NormSignatureWriter(name);

        String newSig =  buildSignature(signature,nsv);
        String[] ifaces = nsv.getIfaces();

        super.visit(version, ACC_PUBLIC + ACC_SUPER, owner, newSig, "com/lsgllc/norm/kernel/graph/model/instance/impl/owl/AbstractEntityInstance", ifaces);
        generateConstructors();
    }


    private void generateConstructors() {
        MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(Type.getType("L" + this.owner + ";"));
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getCanonicalName", "()Ljava/lang/String;");
        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/instance/impl/owl/AbstractEntityInstance", "<init>", "(Ljava/lang/String;Ljava/lang/Object;)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
    }

    private String buildSignature(String signature, NormSignatureWriter nsv) {
        NormSignatureReader nsr = new NormSignatureReader(signature);
        //
        nsv.visitFormalTypeParameter("K");
        nsv.visitClassBound();
        nsv.visitClassType("java/lang/String");
        nsv.visitEnd();

        nsv.visitFormalTypeParameter("V");
        nsv.visitInterfaceBound();
        nsv.visitClassType("com/lsgllc/norm/kernel/graph/things/INormAttribute");
        nsv.visitEnd();

        addSuperClass(nsv, "com/lsgllc/norm/kernel/graph/things/INormEntity");
//        nsv.visitSuperclass();      com.lsgllc.norm.kernel.graph.things.
//        nsv.visitClassType("com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("K");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("V");
//        nsv.visitEnd();
        addNormImplementedIface(nsv,"com/lsgllc/norm/kernel/graph/things/INormEntity");
//        nsv.visitInterface();
//        nsv.visitClassType("com/lsgllc/norm/kernel/graph/model/INormEntity");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("K");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("V");
//        nsv.visitEnd();
        nsv.setIgnoreSupers(true);
        nsr.accept(nsv);
        return nsv.toString();
    }

    private void addSuperClass(NormSignatureWriter nsv, String clazz) {
        nsv.visitSuperclass();
        addTypeVars(nsv, clazz);
    }

    private void addNormImplementedIface(NormSignatureWriter nsv, String clazz) {
        nsv.visitInterface();
        addTypeVars(nsv, clazz);
    }

    private void addTypeVars(NormSignatureWriter nsv, String clazz) {
        nsv.visitClassType(clazz);
        addNormTypeVariable(nsv, "K");
        addNormTypeVariable(nsv, "V");
        nsv.visitEnd();
    }

    private void addNormTypeVariable(NormSignatureWriter nsv, String variable) {
        nsv.visitTypeArgument('=');
        nsv.visitTypeVariable(variable);
    }

    private void addNormGeneric(NormSignatureWriter nsv, String type, String className, boolean isInterface) {
        nsv.visitFormalTypeParameter(type);
        if (isInterface){
            nsv.visitInterfaceBound();
        } else {
            nsv.visitClassBound();
        }
        nsv.visitClassType(className);
        visitEnd();

    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        if (this.methodsGenerated.contains(name)){
            return null;
        }

        String baseName = name.substring(3);
        String startName = baseName.substring(0,1).toLowerCase();
        String propertyName = startName+baseName.substring(1);

        Type checkCast = null;
        boolean isGetter = false;
        String getterSig = null;
        String setterSig = null;
        String getterDescription = null;
        String setterDescription = null;
        if (name.startsWith("get")){
            isGetter = true;
             if (Type.getReturnType(desc) != null){
                 checkCast = Type.getReturnType(desc);
             }
            getterDescription = desc;
            getterSig = signature;
        } else if (name.startsWith("set")){
             if (Type.getArgumentTypes(desc) != null){
                 checkCast = (Type.getArgumentTypes(desc))[0];
             }
            setterDescription = desc;
            setterSig = signature;
        }
        if (checkCast == null){
            return null;
        }


        if (isGetter){
            setterDescription =  Type.getMethodDescriptor(Type.VOID_TYPE,checkCast);
//            setterSig = getSetterSig(checkCast);

        } else{
            getterDescription  = Type.getMethodDescriptor(checkCast,Type.VOID_TYPE);
//            getterSig = null;
        }
//            int strt;
//            int end;
//            if (isGetter){
//                getterSig = signature;
//                strt = signature.indexOf(")") + 1;
//                end = signature.length();
//
//            } else {
//                strt = signature.indexOf("(") + 1;
//                end = signature.indexOf(")");
//            }
//            String sigClassCore = signature.substring(strt,end);
//
//            if (isGetter){
//                setterSig = "(" + sigClassCore + ")V";
//            } else {
//                getterSig = "()" + sigClassCore;
//                setterSig = signature;
//
//            }


        byteGenGetMethod("get" + baseName, exceptions, propertyName, checkCast, getterSig, getterDescription);
        byteGenSetMethod("set" + baseName, exceptions, propertyName, checkCast, setterSig, setterDescription);
//        String mName;
//        MethodVisitor mv;
//
//        mName = "set" + baseName;
//        this.methodsGenerated.add(mName);
//        mv = cv.visitMethod(ACC_PUBLIC, mName, setterDescription, setterSig, exceptions);
//        mv.visitCode();
//        mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
//        mv.visitInsn(DUP);
//        mv.visitLdcInsn(propertyName);
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
//        mv.visitVarInsn(ASTORE, 2);
//        mv.visitVarInsn(ALOAD, 0);
//        mv.visitLdcInsn(propertyName);
//        mv.visitVarInsn(ALOAD, 2);
//        mv.visitMethodInsn(INVOKEVIRTUAL, this.owner, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
//        mv.visitInsn(POP);
//        mv.visitInsn(RETURN);
//        mv.visitMaxs(4, 3);
//        mv.visitEnd();
        return  null;
    }

    private void byteGenSetMethod(String baseName, String[] exceptions, String propertyName, Type checkCast, String signature, String description) {
        MethodVisitor mv;
        this.methodsGenerated.add(baseName);
        mv = cv.visitMethod(ACC_PUBLIC, baseName, description, signature, exceptions);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getCanonicalName", "()Ljava/lang/String;");
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(INVOKEVIRTUAL, this.owner, "setValue", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(4, 2);
        mv.visitEnd();
    }
    private void byteGenGetMethod(String baseName, String[] exceptions, String propertyName, Type checkCast, String signature, String description) {
        MethodVisitor mv;
        this.methodsGenerated.add(baseName);
        mv = cv.visitMethod(ACC_PUBLIC, baseName, description, null, exceptions);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getCanonicalName", "()Ljava/lang/String;");
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(INVOKEVIRTUAL, this.owner, "getValue", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
        mv.visitTypeInsn(CHECKCAST, checkCast.getInternalName());
        mv.visitInsn(ARETURN);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
    }

//    private String getSetterSig(Type type) {
//        int strt;
//        int end;
//        return "()" + type.;
//
//    }
//    private String getGetterSig(Type signature) {
//        int strt;
//        int end;
//        strt = signature.indexOf("(") + 1;
//        end = signature.indexOf(")");
//        return "(" + signature.substring(strt,end) + ")V";
//
//    }

    private Type[] getExceptionsTypeArray(String[] exceptions) {
        if (exceptions == null || exceptions.length <= 0){
            return null;
        }
        int numExceptions = exceptions.length;
        Type[] types = new Type[numExceptions];

        int x = 0;
        for (String e:exceptions){
            types[x++] = Type.getObjectType(e);
        }

        return types;
    }

    private boolean methodGenerated(String name) {
        if (!this.methodsGenerated.contains(name)) {
            return false;
        }
        return true;
    }

    @Override
    public void visitEnd() {
        this.cv.visitEnd();
        super.visitEnd();
    }
}