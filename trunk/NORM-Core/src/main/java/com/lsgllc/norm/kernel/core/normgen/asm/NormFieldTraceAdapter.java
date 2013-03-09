package com.lsgllc.norm.kernel.core.normgen.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created By: sameloyiv
 * Date: 12/18/12
 * Time: 5:35 PM
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
public class NormFieldTraceAdapter extends MethodVisitor implements Opcodes {

    private String owner;

    public NormFieldTraceAdapter(final MethodVisitor mv, final String owner) {
        super(Opcodes.ASM4, mv);
        this.owner = owner;
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner,
                               final String name, final String desc) {
        if (owner.equals(this.owner)) {
            if (opcode == GETFIELD) {
                // replaces GETFIELD f by INVOKESPECIAL _getf
                String gDesc = "()" + desc;
                visitMethodInsn(INVOKESPECIAL, owner, "_get" + name, gDesc);
                return;
            } else if (opcode == PUTFIELD) {
                // replaces PUTFIELD f by INVOKESPECIAL _setf
                String sDesc = "(" + desc + ")V";
                visitMethodInsn(INVOKESPECIAL, owner, "_set" + name, sDesc);
                return;
            }
        }
        super.visitFieldInsn(opcode, owner, name, desc);
    }
}