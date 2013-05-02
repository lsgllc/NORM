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

    public NormGenAsmTraceAnnotationVisitor(final Printer p) {
        this(null, p);
    }

    public NormGenAsmTraceAnnotationVisitor(final AnnotationVisitor av, final Printer p) {
        this( av, p, null);
    }

    public NormGenAsmTraceAnnotationVisitor(AnnotationVisitor av, Printer p, PropertyFileMaker propertyFileMaker) {
        super(Opcodes.ASM4, av);
        this.pfm = propertyFileMaker;
        this.p = p;
    }

    @Override
    public void visit(final String name, final Object value) {
        p.visit(name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc,
                          final String value) {
        p.visitEnum(name, desc, value);
        super.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name,
                                             final String desc) {
        Printer p = this.p.visitAnnotation(name, desc);
        AnnotationVisitor av = this.av == null ? null : this.av
                .visitAnnotation(name, desc);
        return new NormGenAsmTraceAnnotationVisitor(av, p);
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        Printer p = this.p.visitArray(name);
        AnnotationVisitor av = this.av == null ? null : this.av
                .visitArray(name);
        return new NormGenAsmTraceAnnotationVisitor(av, p);
    }

    @Override
    public void visitEnd() {
        p.visitAnnotationEnd();
        super.visitEnd();
    }

}
