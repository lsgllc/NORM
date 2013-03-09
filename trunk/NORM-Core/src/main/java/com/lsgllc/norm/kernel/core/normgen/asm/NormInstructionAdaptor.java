package com.lsgllc.norm.kernel.core.normgen.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 * Created By: sameloyiv
 * Date: 12/21/12
 * Time: 11:22 AM
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
@Deprecated
public class NormInstructionAdaptor extends InstructionAdapter {
    public NormInstructionAdaptor(MethodVisitor mv) {
        super(mv);
    }

    public NormInstructionAdaptor(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, owner, name, desc);
    }
}
