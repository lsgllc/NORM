package com.lsgllc.mojo.genasm;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 1:05 PM
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
public class GenAsmClassReader extends ClassReader {
    public GenAsmClassReader(byte[] b) {
        super(b);
    }

    public GenAsmClassReader(byte[] b, int off, int len) {
        super(b, off, len);
    }

    @Override
    public int getAccess() {
        return super.getAccess();
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

    public GenAsmClassReader(InputStream is) throws IOException {
        super(is);
    }

    public GenAsmClassReader(String name) throws IOException {
        super(name);
    }

    @Override
    public void accept(ClassVisitor classVisitor, int flags) {
        super.accept(classVisitor, flags);
    }

    @Override
    public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags) {
        super.accept(classVisitor, attrs, flags);
    }

    @Override
    protected Label readLabel(int offset, Label[] labels) {
        return super.readLabel(offset, labels);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItem(int item) {
        return super.getItem(item);
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
}
