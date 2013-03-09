package com.lsgllc.norm.kernel.core.normgen.asm;

import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Created By: sameloyiv
 * Date: 12/21/12
 * Time: 11:33 AM
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
public class NormSignatureVisitor extends SignatureVisitor {

    protected class NormSignature {
        String formalTypeParameter;
        String interfaceType;
        String parameterType;
        String returnType;
        String classType;
        String innerClassType;
        String typeVariable;
    }

    private enum BOUND_TYPE {CLASS,IFACE,PARM,RET,WCARD};
    private BOUND_TYPE lastBound = null;
    private NormSignature mySignature = new NormSignature();
    public NormSignatureVisitor(int api) {
        super(api);
    }

    @Override
    public void visitFormalTypeParameter(String name) {
        this.mySignature.formalTypeParameter = name;
        super.visitFormalTypeParameter(name);
    }

    @Override
    public SignatureVisitor visitClassBound() {
        this.lastBound = BOUND_TYPE.CLASS;
        return super.visitClassBound();
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
        this.lastBound = BOUND_TYPE.IFACE;
        return super.visitInterfaceBound();
    }

    @Override
    public SignatureVisitor visitSuperclass() {
        return super.visitSuperclass();
    }

    @Override
    public SignatureVisitor visitInterface() {
        return super.visitInterface();
    }

    @Override
    public SignatureVisitor visitParameterType() {
        this.lastBound = BOUND_TYPE.PARM;
        return super.visitParameterType();
    }

    @Override
    public SignatureVisitor visitReturnType() {
        this.lastBound = BOUND_TYPE.RET;
        return super.visitReturnType();
    }

    @Override
    public SignatureVisitor visitExceptionType() {
        return super.visitExceptionType();
    }

    @Override
    public void visitBaseType(char descriptor) {
        super.visitBaseType(descriptor);
    }

    @Override
    public void visitTypeVariable(String name) {
        this.mySignature.typeVariable = name;
        super.visitTypeVariable(name);
    }

    @Override
    public SignatureVisitor visitArrayType() {
        return super.visitArrayType();
    }

    @Override
    public void visitClassType(String name) {
        switch (this.lastBound) {

            case CLASS:
                this.mySignature.classType = name;
                break;
            case IFACE:
                this.mySignature.interfaceType = name;
                break;
            case PARM:
                this.mySignature.parameterType = name;
                break;
            case RET:
                this.mySignature.returnType = name;
                break;
        }
        super.visitClassType(name);
    }

    @Override
    public void visitInnerClassType(String name) {
        this.mySignature.innerClassType = name;
        super.visitInnerClassType(name);
    }

    @Override
    public void visitTypeArgument() {
        super.visitTypeArgument();
    }

    @Override
    public SignatureVisitor visitTypeArgument(char wildcard) {
        return super.visitTypeArgument(wildcard);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    public NormSignature getMySignature(){
        return mySignature;
    }
}
