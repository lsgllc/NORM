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
 * (c) Texas Department of Motor Vehicles  2012
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

        super.visit(version, ACC_PUBLIC + ACC_SUPER, owner, newSig, "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", ifaces);
        generateConstructors();
    }


    private void generateConstructors() {
        MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = this.cv.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", "(TK;)V", null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", "<init>", "(Ljava/lang/Object;)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
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
        nsv.visitClassType("com/lsgllc/norm/kernel/graph/model/INormAttribute");
        nsv.visitEnd();

        addSuperClass(nsv, "com/lsgllc/norm/kernel/graph/model/INormEntity");
//        nsv.visitSuperclass();
//        nsv.visitClassType("com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("K");
//        nsv.visitTypeArgument('=');
//        nsv.visitTypeVariable("V");
//        nsv.visitEnd();
        addNormImplementedIface(nsv,"com/lsgllc/norm/kernel/graph/model/INormEntity");
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
        if (name.startsWith("get")){
            isGetter = true;
             if (Type.getReturnType(desc) != null){
                 checkCast = Type.getReturnType(desc);
             }
        } else if (name.startsWith("set")){
             if (Type.getArgumentTypes(desc) != null){
                 checkCast = (Type.getArgumentTypes(desc))[0];
             }
        }
        if (checkCast == null){
            return null;
        }
        String getterSig = null;
        String setterSig = null;
        String getterDescription = null;
        String setterDescription = null;


        if (signature != null){
            if (isGetter){
                getterDescription  = desc;
                setterDescription =  Type.getMethodDescriptor(Type.VOID_TYPE,checkCast);
                getterSig = signature;
                setterSig = getSetterSig(signature);

            } else{
                getterDescription  = Type.getMethodDescriptor(checkCast,Type.VOID_TYPE);
                setterDescription = desc;
                setterSig = signature;
                getterSig = getGetterSig(signature);
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
        }


        byteGenMethod("get"+ baseName, exceptions, propertyName, checkCast, getterSig, getterDescription);
        byteGenMethod("set"+ baseName, exceptions, propertyName, checkCast, getterSig, getterDescription);
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

    private void byteGenMethod(String baseName, String[] exceptions, String propertyName, Type checkCast, String getterSig, String getterDescription) {
        MethodVisitor mv;
        String mName = "get" + baseName;
        this.methodsGenerated.add(mName);
        mv = cv.visitMethod(ACC_PUBLIC, mName, getterDescription, getterSig, exceptions);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(INVOKEVIRTUAL, this.owner, "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
        mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
        mv.visitVarInsn(ASTORE, 1);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
        mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
        mv.visitVarInsn(ASTORE, 2);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
        mv.visitTypeInsn(CHECKCAST, checkCast.getInternalName());
        mv.visitInsn(ARETURN);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    private String getSetterSig(String signature) {
        int strt;
        int end;
        strt = signature.indexOf(")") + 1;
        end = signature.length();
        return "()" + signature.substring(strt,end);

    }
    private String getGetterSig(String signature) {
        int strt;
        int end;
        strt = signature.indexOf("(") + 1;
        end = signature.indexOf(")");
        return "(" + signature.substring(strt,end) + ")V";

    }

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