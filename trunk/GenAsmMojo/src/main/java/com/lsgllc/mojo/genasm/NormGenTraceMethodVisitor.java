package com.lsgllc.mojo.genasm;

import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceAnnotationVisitor;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 10:11 PM
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
public class NormGenTraceMethodVisitor extends MethodVisitor {

    public final Printer p;

    public NormGenTraceMethodVisitor(final Printer p) {
        this(null, p);
    }

    public NormGenTraceMethodVisitor(final MethodVisitor mv, final Printer p) {
        super(Opcodes.ASM4, mv);
        this.p = p;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc,
                                             final boolean visible) {
        Printer p = this.p.visitMethodAnnotation(desc, visible);
        AnnotationVisitor av = mv == null ? null : mv.visitAnnotation(desc,
                visible);
        System.out.println("Method:Visited visitAnnotation");
        return new NormGenAsmTraceAnnotationVisitor(av, p);
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        p.visitMethodAttribute(attr);
        super.visitAttribute(attr);
        System.out.println("Method:Visited visitAttribute");
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        Printer p = this.p.visitAnnotationDefault();
        AnnotationVisitor av = mv == null ? null : mv.visitAnnotationDefault();
        System.out.println("Method:Visited visitAnnotationDefault");
        return new NormGenAsmTraceAnnotationVisitor(av, p);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter,
                                                      final String desc, final boolean visible) {
        Printer p = this.p.visitParameterAnnotation(parameter, desc, visible);
        AnnotationVisitor av = mv == null ? null : mv.visitParameterAnnotation(
                parameter, desc, visible);
        System.out.println("Method:Visited visitParameterAnnotation");
        return new NormGenAsmTraceAnnotationVisitor(av, p);
    }

    @Override
    public void visitCode() {
        p.visitCode();
        super.visitCode();
        System.out.println("Method:Visited visitCode");
    }

    @Override
    public void visitFrame(final int type, final int nLocal,
                           final Object[] local, final int nStack, final Object[] stack) {
        p.visitFrame(type, nLocal, local, nStack, stack);
        super.visitFrame(type, nLocal, local, nStack, stack);
        System.out.println("Method:Visited visitFrame");
    }

    @Override
    public void visitInsn(final int opcode) {
        p.visitInsn(opcode);
        super.visitInsn(opcode);
        System.out.println("Method:Visited visitInsn");
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        p.visitIntInsn(opcode, operand);
        super.visitIntInsn(opcode, operand);
        System.out.println("Method:Visited visitIntInsn");
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        p.visitVarInsn(opcode, var);
        super.visitVarInsn(opcode, var);
        System.out.println("Method:Visited visitVarInsn");
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        p.visitTypeInsn(opcode, type);
        super.visitTypeInsn(opcode, type);
        System.out.println("Method:Visited visitTypeInsn");
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner,
                               final String name, final String desc) {
        p.visitFieldInsn(opcode, owner, name, desc);
        super.visitFieldInsn(opcode, owner, name, desc);
        System.out.println("Method:Visited visitFieldInsn");
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner,
                                final String name, final String desc) {
        p.visitMethodInsn(opcode, owner, name, desc);
        super.visitMethodInsn(opcode, owner, name, desc);
        System.out.println("Method:Visited visitMethodInsn");
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
                                       Object... bsmArgs) {
        p.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        System.out.println("Method:Visited visitInvokeDynamicInsn");
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        p.visitJumpInsn(opcode, label);
        super.visitJumpInsn(opcode, label);
        System.out.println("Method:Visited visitJumpInsn");
    }

    @Override
    public void visitLabel(final Label label) {
        p.visitLabel(label);
        super.visitLabel(label);
        System.out.println("Method:Visited visitLabel");
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        p.visitLdcInsn(cst);
        super.visitLdcInsn(cst);
        System.out.println("Method:Visited visitLdcInsn");
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        p.visitIincInsn(var, increment);
        super.visitIincInsn(var, increment);
        System.out.println("Method:Visited visitIincInsn");
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max,
                                     final Label dflt, final Label... labels) {
        p.visitTableSwitchInsn(min, max, dflt, labels);
        super.visitTableSwitchInsn(min, max, dflt, labels);
        System.out.println("Method:Visited visitTableSwitchInsn");
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys,
                                      final Label[] labels) {
        p.visitLookupSwitchInsn(dflt, keys, labels);
        super.visitLookupSwitchInsn(dflt, keys, labels);
        System.out.println("Method:Visited visitLookupSwitchInsn");
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        p.visitMultiANewArrayInsn(desc, dims);
        super.visitMultiANewArrayInsn(desc, dims);
        System.out.println("Method:Visited visitMultiANewArrayInsn");
    }

    @Override
    public void visitTryCatchBlock(final Label start, final Label end,
                                   final Label handler, final String type) {
        p.visitTryCatchBlock(start, end, handler, type);
        super.visitTryCatchBlock(start, end, handler, type);
        System.out.println("Method:Visited visitTryCatchBlock");
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
                                   final String signature, final Label start, final Label end,
                                   final int index) {
        p.visitLocalVariable(name, desc, signature, start, end, index);
        super.visitLocalVariable(name, desc, signature, start, end, index);
        System.out.println("Method:Visited visitLocalVariable");
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        p.visitLineNumber(line, start);
        super.visitLineNumber(line, start);
        System.out.println("Method:Visited visitLineNumber");
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        p.visitMaxs(maxStack, maxLocals);
        super.visitMaxs(maxStack, maxLocals);
        System.out.println("Method:Visited visitMaxs");
    }

    @Override
    public void visitEnd() {
        p.visitMethodEnd();
        super.visitEnd();
        System.out.println("Method:Visited visitEnd");
    }
}
