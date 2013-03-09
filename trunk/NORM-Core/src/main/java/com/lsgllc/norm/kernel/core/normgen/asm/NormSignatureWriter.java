package com.lsgllc.norm.kernel.core.normgen.asm;

import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;

/**
 * Created By: sameloyiv
 * Date: 12/21/12
 * Time: 3:28 PM
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
@ContextConfiguration(locations = { "classpath:../openjpa/META-INF/spring/camel-context.xml" })
public class NormSignatureWriter extends SignatureWriter {
    @Autowired
    protected static OntologyBroker graphManager;
    private boolean isIface;
    private static final Character[] generics = {'K','V','T','M','N','P','O'};
    private int currentGeneric = 0;
    private boolean ignoreSupers = false;
    private boolean ignoreNextClass = false;
    private HashSet<String> interfaces = new HashSet<String>();
    private int currentIface = 0;
    private boolean isSuper = false;

    public NormSignatureWriter() {
        super();
    }

    public NormSignatureWriter(String name) {
        this();
        this.interfaces.add(name);

    }

/*    visitFormalTypeParameter
* visitClassBound
* visitClassType String
* visitEnd
*
* */

    @Override
    public void visitFormalTypeParameter(String name) {
        super.visitFormalTypeParameter(name);
    }

    @Override
    public SignatureVisitor visitClassBound() {
        return super.visitClassBound();
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
        return super.visitInterfaceBound();
    }

    @Override
    public SignatureVisitor visitSuperclass() {
        if (this.ignoreSupers){
            this.ignoreNextClass=true;
        }
        return super.visitSuperclass();
    }

    @Override
    public SignatureVisitor visitInterface() {
        this.isIface = true;
        return super.visitInterface();
    }

    @Override
    public SignatureVisitor visitParameterType() {
        return super.visitParameterType();
    }

    @Override
    public SignatureVisitor visitReturnType() {
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
//        if ((this.currentGeneric) <= 6) {
//            name = generics[this.currentGeneric].toString();
//        }  else {
//            this.currentGeneric = 1;
//            name = Character.valueOf((char) (generics[6].charValue() + this.currentGeneric++)).toString();
//        }
        super.visitTypeVariable(name);
    }

    @Override
    public SignatureVisitor visitArrayType() {
        return super.visitArrayType();
    }

    @Override
    public void visitClassType(String name) {
        if (!this.ignoreNextClass){
            if (this.isIface){
                this.interfaces.add(new String(name));
                this.isIface=false;
            }
            super.visitClassType(name);
        }
    }

    @Override
    public void visitInnerClassType(String name) {
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
        if (this.ignoreNextClass){
            this.ignoreNextClass = false;
        }
        super.visitEnd();
    }

    public void setIgnoreSupers(boolean ignoreSupers) {
        this.ignoreSupers = ignoreSupers;
    }
    public <T extends String>T[]  getIfaces(){
        T[] ifaceArray = (T[]) new String[this.interfaces.size()];
        return (this.interfaces.toArray(ifaceArray));
    }
}
