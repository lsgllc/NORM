package com.lsgllc.mojo.genasm;

import org.objectweb.asm.*;
import org.objectweb.asm.util.*;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created By: sameloyiv
 * Date: 4/22/13
 * Time: 5:14 PM
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
public class NormGenAsmTraceClassVisitor extends ClassVisitor {

    private final PropertyFileMaker pfm;


    /**
     * The print writer to be used to print the class. May be null.
     */
    private final PrintWriter pw;

    /**
     * The object that actually converts visit events into text.
     */
    public final Printer p;

    /**
     * Constructs a new {@link TraceClassVisitor}.
     *
     * @param pw
     *            the print writer to be used to print the class.
     */
//    public NormGenAsmTraceClassVisitor(final PrintWriter pw) {
//        this(null, pw);
//    }

    /**
     * Constructs a new {@link TraceClassVisitor}.
     *
     * @param cv
     *            the {@link ClassVisitor} to which this visitor delegates
     *            calls. May be <tt>null</tt>.
     * @param pw
     *            the print writer to be used to print the class.
     */

    /**
     * Constructs a new {@link TraceClassVisitor}.
     *
     * @param cv
     *            the {@link ClassVisitor} to which this visitor delegates
     *            calls. May be <tt>null</tt>.
     * @param p
     *            the object that actually converts visit events into text.
     * @param pw
     *            the print writer to be used to print the class. May be null if
     *            you simply want to use the result via
     *            {@link Printer#getText()}, instead of printing it.
     */
    public final KeyMaker cKey;
    public NormGenAsmTraceClassVisitor(final ClassVisitor cv, final Printer p,
                             final PrintWriter pw,final PropertyFileMaker pFileMaker,final KeyMaker key) {
        super(Opcodes.ASM4, cv);
        this.pw = pw;
        this.p = p;
        this.pfm = pFileMaker;
        this.cKey = key;
    }

    public NormGenAsmTraceClassVisitor(final ClassVisitor cv, final Printer asMifier,final PrintWriter printWriter, List txt, final PropertyFileMaker pFileMaker, KeyMaker key) {
        this(cv, asMifier, printWriter,pFileMaker, key);
//        text = txt;

    }

    @Override
    public void visit(final int version, final int access, final String name,
                      final String signature, final String superName,
                      final String[] interfaces) {

        p.visit(version, access, name, signature, superName, interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(final String file, final String debug) {
        p.visitSource(file, debug);
        super.visitSource(file, debug);
    }

    @Override
    public void visitOuterClass(final String owner, final String name,
                                final String desc) {
        p.visitOuterClass(owner, name, desc);
        super.visitOuterClass(owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc,
                                             final boolean visible) {
        Printer p = this.p.visitClassAnnotation(desc, visible);
        AnnotationVisitor av = cv == null ? null : cv.visitAnnotation(desc,
                visible);
        this.cKey.push("annotation");
        AnnotationVisitor pav = new NormGenAsmTraceAnnotationVisitor(av, p, this.pfm, this.cKey);
        this.cKey.pop();
        return pav;
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        p.visitClassAttribute(attr);
        super.visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName,
                                final String innerName, final int access) {
        p.visitInnerClass(name, outerName, innerName, access);
        super.visitInnerClass(name, outerName, innerName, access);
    }

//    @Override
//    public FieldVisitor visitField(final int access, final String name,
//                                   final String desc, final String signature, final Object value) {
//        Printer p = this.p.visitField(access, name, desc, signature, value);
//        FieldVisitor fv = cv == null ? null : cv.visitField(access, name, desc,
//                signature, value);
//        return new TraceFieldVisitor(fv, p);
//    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        Printer p = this.p.visitMethod(access, name, desc, signature,
                exceptions);
        MethodVisitor mv = cv == null ? null : cv.visitMethod(access, name,
                desc, signature, exceptions);
        this.cKey.push("method");
        pfm.makeProperty(this.cKey.buildKey() + ".name" , new String[]{name});
        pfm.makeProperty(this.cKey.buildKey() + ".desc" , new String[]{desc});
        pfm.makeProperty(this.cKey.buildKey() + ".signature" , new String[]{signature});
        pfm.makeProperty(this.cKey.buildKey() + ".exceptions" ,exceptions);
        MethodVisitor pav = new NormGenTraceMethodVisitor(mv, p,this.pfm, cKey);
        this.cKey.pop();
        return pav;
    }

    @Override
    public void visitEnd() {
        p.visitClassEnd();
        if (pw != null) {
//            this.text.

            p.print(pw);
            pw.flush();
        }
        super.visitEnd();
    }}
