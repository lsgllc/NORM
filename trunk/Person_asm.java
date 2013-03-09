package asm.gov.state.tx.dmv.uom.common.person.impl;
import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.attrs.*;
public class PersonDump implements Opcodes {

public static byte[] dump () throws Exception {

ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;

cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "gov/state/tx/dmv/uom/common/person/impl/Person", "<K:Ljava/lang/String;V::Lcom/lsgllc/norm/core/model/IAttribute;>Lcom/lsgllc/norm/core/model/impl/AbstractEntity<TK;TV;>;Lgov/state/tx/dmv/uom/common/person/IPerson<Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE;>;Lcom/lsgllc/norm/core/model/IEntity<TK;TV;>;Lcom/lsgllc/norm/INormPersistable;", "com/lsgllc/norm/core/model/impl/AbstractEntity", new String[] { "gov/state/tx/dmv/uom/common/person/IPerson", "com/lsgllc/norm/core/model/IEntity", "com/lsgllc/norm/INormPersistable" });

{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/AbstractEntity", "<init>", "()V");
mv.visitInsn(RETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", "(TK;)V", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/AbstractEntity", "<init>", "(Ljava/lang/Object;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getType", "()Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("type");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("type");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/core/model/impl/Attribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/IProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/core/model/IProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "gov/state/tx/dmv/uom/common/person/PERSON_TYPE");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getMiddleName", "()Ljava/lang/String;", null, new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("middleName");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("middleName");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/core/model/impl/Attribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/IProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/core/model/IProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getKnownAddresses", "()Ljava/util/Set;", "()Ljava/util/Set<Ljava/util/HashMap<Lgov/state/tx/dmv/uom/common/contact/IContactInformation;Ljava/util/List<Ljava/lang/String;>;>;>;", new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("knownAddresses");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("knownAddresses");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/core/model/impl/Attribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/IProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/core/model/IProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/util/Set");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getAllVehicles", "()Ljava/util/List;", "()Ljava/util/List<Lgov/state/tx/dmv/uom/common/vehicle/IVehicle;>;", new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("allVehicles");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("allVehicles");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/core/model/impl/Attribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/core/model/IProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/core/model/IProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/util/List");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setType", "(Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("type");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/Attribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("type");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getFirstName", "()Ljava/lang/String;", null, new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitInsn(ACONST_NULL);
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setMiddleName", "(Ljava/lang/String;)V", null, new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("firstName");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/Attribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("firstName");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setKnownAddresses", "(Ljava/util/Set;)V", "(Ljava/util/Set<Ljava/util/HashMap<Lgov/state/tx/dmv/uom/common/contact/IContactInformation;Ljava/util/List<Ljava/lang/String;>;>;>;)V", new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("knownAddresses");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/Attribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("knownAddresses");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setAllVehicles", "(Ljava/util/List;)V", "(Ljava/util/List<Lgov/state/tx/dmv/uom/common/vehicle/IVehicle;>;)V", new String[] { "gov/state/tx/dmv/uom/exceptions/UOMSpecifiedException", "gov/state/tx/dmv/uom/exceptions/StrangeAndWonderfulException" });
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/core/model/impl/Attribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("allVehicles");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/core/model/impl/Attribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("allVehicles");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/Person", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
cw.visitEnd();

return cw.toByteArray();
}
}
