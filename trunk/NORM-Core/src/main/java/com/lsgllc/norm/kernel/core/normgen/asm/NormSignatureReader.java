package com.lsgllc.norm.kernel.core.normgen.asm;

import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Created By: sameloyiv
 * Date: 12/21/12
 * Time: 11:44 AM
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
public class NormSignatureReader extends SignatureReader {
    public NormSignatureReader(String signature) {
        super(signature);
    }

    @Override
    public void accept(SignatureVisitor v) {
        super.accept(v);
    }

    @Override
    public void acceptType(SignatureVisitor v) {
        super.acceptType(v);
    }



}
