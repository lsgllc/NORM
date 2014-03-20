package com.lsgllc.norm.kernel.core.normgen.asm;


import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created By: sameloyiv
 * Date: 12/18/12
 * Time: 3:58 PM
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

public class NormClassReader extends ClassReader {

    protected OntologyBroker graphManager;

    public NormClassReader(byte[] b) {
        super(b);
    }

    public NormClassReader(byte[] b, int off, int len) {
        super(b, off, len);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getMaxStringLength() {
        return super.getMaxStringLength();
    }

    @Override
    public int readByte(int index) {
        return super.readByte(index);
    }

    @Override
    public int readUnsignedShort(int index) {
        return super.readUnsignedShort(index);
    }

    @Override
    public short readShort(int index) {
        return super.readShort(index);
    }

    @Override
    public int readInt(int index) {
        return super.readInt(index);
    }

    @Override
    public long readLong(int index) {
        return super.readLong(index);
    }

    @Override
    public String readUTF8(int index, char[] buf) {
        return super.readUTF8(index, buf);
    }

    @Override
    public String readClass(int index, char[] buf) {
        return super.readClass(index, buf);
    }

    @Override
    public Object readConst(int item, char[] buf) {
        return super.readConst(item, buf);
    }

    protected String name;
    public NormClassReader(InputStream inputStream) throws IOException {
        super(inputStream);

    }

    public NormClassReader(InputStream is, String name) throws IOException {
        super(is);
        this.name = name;
    }

    public NormClassReader(InputStream is, String name, OntologyBroker graphManager) throws IOException {
        this(is, name);
        this.graphManager = graphManager;
    }


    @Override
    public int getAccess() {
        return super.getAccess();
    }

    public NormClassReader(String s) throws IOException {
        super(s);
    }

    @Override
    public String getClassName() {
        return super.getClassName();
    }

    @Override
    public String getSuperName() {
        return super.getSuperName();
    }

    @Override
    public String[] getInterfaces() {
        return super.getInterfaces();
    }

    @Override
    public void accept(ClassVisitor classVisitor, int i) {
        super.accept(classVisitor, i);
    }

    @Override
    public void accept(ClassVisitor classVisitor, Attribute[] attributes, int i) {
        super.accept(classVisitor, attributes, i);
    }

    @Override
    protected Label readLabel(int i, Label[] labels) {
        return super.readLabel(i, labels);
    }

    @Override
    public int getItem(int i) {
        return super.getItem(i);
    }

}
