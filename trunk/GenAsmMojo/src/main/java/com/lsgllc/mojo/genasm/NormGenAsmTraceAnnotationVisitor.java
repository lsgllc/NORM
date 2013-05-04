package com.lsgllc.mojo.genasm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceAnnotationVisitor;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 7:34 PM
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
public class NormGenAsmTraceAnnotationVisitor extends AnnotationVisitor {
    private final Printer p;
    private final PropertyFileMaker pfm;
    private final KeyMaker aKey;

//    public NormGenAsmTraceAnnotationVisitor(final Printer p) {
//        this(null, p);
//    }

//    public NormGenAsmTraceAnnotationVisitor(final AnnotationVisitor av, final Printer p, KeyMaker theKey) {
//        this( av, p, null, null);
//    }

    public NormGenAsmTraceAnnotationVisitor(AnnotationVisitor av, Printer p, final PropertyFileMaker propertyFileMaker,final KeyMaker theKey) {
        super(Opcodes.ASM4, av);
        this.pfm = propertyFileMaker;
        this.p = p;
        this.aKey = theKey;

    }

    @Override
    public void visit(final String name, final Object value) {
        ((NormGenASMifier)p).propertyFileMaker.makeProperty(this.aKey.buildKey() +".name" , new String[]{name});
        ((NormGenASMifier)p).propertyFileMaker.makeSplProperty(this.aKey.buildKey() + ".value" , value,"objs");
        p.visit(name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc,
                          final String value) {
        this.aKey.push("enum");
        pfm.makeProperty(this.aKey.buildKey() + ".name" , new String[]{name});
        pfm.makeProperty(this.aKey.buildKey() + ".desc" , new String[]{desc});
        pfm.makeProperty(this.aKey.buildKey() + ".value" , new String[]{value});
        p.visitEnum(name, desc, value);
        super.visitEnum(name, desc, value);
        this.aKey.pop();
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name,
                                             final String desc) {
        Printer p = this.p.visitAnnotation(name, desc);
        AnnotationVisitor av = this.av == null ? null : this.av
                .visitAnnotation(name, desc);
        this.aKey.push("annotation");
        AnnotationVisitor pav = new NormGenAsmTraceAnnotationVisitor(av, p, this.pfm, this.aKey);
        this.aKey.pop();
        return pav;
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        Printer p = this.p.visitArray(name);
        AnnotationVisitor av = this.av == null ? null : this.av
                .visitArray(name);
        this.aKey.push("array");
        AnnotationVisitor pav = new NormGenAsmTraceAnnotationVisitor(av, p, this.pfm, this.aKey);
        this.aKey.pop();
        return pav;
    }

    @Override
    public void visitEnd() {
        p.visitAnnotationEnd();
        super.visitEnd();
    }

}
