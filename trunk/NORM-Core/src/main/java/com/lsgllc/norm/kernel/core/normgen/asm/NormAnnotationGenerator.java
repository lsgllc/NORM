package com.lsgllc.norm.kernel.core.normgen.asm;

import org.objectweb.asm.AnnotationVisitor;

/**
 * Created By: sameloyiv
 * Date: 12/21/12
 * Time: 9:48 AM
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
@Deprecated
public class NormAnnotationGenerator extends AnnotationVisitor {
    public NormAnnotationGenerator(int api) {
        super(api);
    }

    public NormAnnotationGenerator(int api, AnnotationVisitor av) {
        super(api, av);
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        super.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return super.visitArray(name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
