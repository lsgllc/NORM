package com.lsgllc.norm.kernel.core.normgen.asm;

import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;

/**
 * Created By: sameloyiv
 * Date: 12/20/12
 * Time: 9:07 AM
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
public class NormClassWriter extends ClassWriter {
    protected OntologyBroker graphManager;
    public NormClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    public NormClassWriter(NormClassReader cr, int flags, OntologyBroker graphManager) {
        this(cr, flags);
        this.graphManager = graphManager;
    }

    @Override
    public byte[] toByteArray() {
        return super.toByteArray();
    }

    @Override
    public int newConst(Object cst) {
        return super.newConst(cst);
    }

    @Override
    public int newUTF8(String value) {
        return super.newUTF8(value);
    }

    @Override
    public int newClass(String value) {
//        String newValue = (this.graphManager.isPackageNormPersistable(value.substring(0,value.lastIndexOf("/"))))?value + "$LSG_Proxy":value;
        return super.newClass(value);
    }

    @Override
    public int newMethodType(String methodDesc) {
        return super.newMethodType(methodDesc);
    }

    @Override
    public int newHandle(int tag, String owner, String name, String desc) {
        return super.newHandle(tag, owner, name, desc);
    }

    @Override
    public int newInvokeDynamic(String name, String desc, Handle bsm, Object... bsmArgs) {
        return super.newInvokeDynamic(name, desc, bsm, bsmArgs);
    }

    @Override
    public int newField(String owner, String name, String desc) {
        return super.newField(owner, name, desc);
    }

    @Override
    public int newMethod(String owner, String name, String desc, boolean itf) {
        return super.newMethod(owner, name, desc, itf);
    }

    @Override
    public int newNameType(String name, String desc) {
        return super.newNameType(name, desc);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return super.getCommonSuperClass(type1, type2);
    }

    public void setGraphManager(OntologyBroker graphManager) {
        this.graphManager = graphManager;
    }

}
