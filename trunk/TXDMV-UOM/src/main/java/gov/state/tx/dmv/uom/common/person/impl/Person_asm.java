package gov.state.tx.dmv.uom.common.person.impl;
import org.objectweb.asm.*;
public class Person_asm implements Opcodes {

public static byte[] dump () throws Exception {

ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;
                                                                                        //<T:Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE_OLD;>Ljava/lang/Object;Lcom/lsgllc/norm/INormPersistable;<K:Ljava/lang/StringV::Lcom/lsgllc/norm/core/model/INormAttribute>Lcom/lsgllc/norm/core/model/impl/AbstractEntity<TK;TV;>;Lcom/lsgllc/norm/core/model/INormEntity<TK;TV;>;;
cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "<K:Ljava/lang/String;V::Lcom/lsgllc/norm/core/model/INormAttribute;>Lcom/lsgllc/norm/core/model/impl/AbstractEntity<TK;TV;>;Lgov/state/tx/dmv/uom/common/person/IPersonOld<Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE_OLD;>;Lcom/lsgllc/norm/core/model/INormEntity<TK;TV;>;", "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", new String[] { "gov/state/tx/dmv/uom/common/person/IPersonOld", "com/lsgllc/norm/kernel/graph/model/INormEntity"});

{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", "<init>", "()V");
mv.visitInsn(RETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", "(TK;)V", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractEntity", "<init>", "(Ljava/lang/Object;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getType", "()Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE_OLD;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("type");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("type");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "gov/state/tx/dmv/uom/common/person/PERSON_TYPE_OLD");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getFirstName", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("firstName");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("firstName");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getMiddleName", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("middleName");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("middleName");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getLastName", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("lastName");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("lastName");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getSalutation", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("salutation");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("salutation");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getNameSuffix", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("nameSuffix");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("nameSuffix");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/lang/String");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getKnownAddresses", "()Ljava/util/Set;", "()Ljava/util/Set<Lgov/state/tx/dmv/uom/common/contact/IContactInformation;>;", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("knownAddresses");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("knownAddresses");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/util/Set");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getAllVehicles", "()Ljava/util/List;", "()Ljava/util/List<Lgov/state/tx/dmv/uom/common/vehicle/IVehicle;>;", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("allVehicles");
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitLdcInsn("allVehicles");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "com/lsgllc/norm/kernel/graph/model/INormProperty");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEINTERFACE, "com/lsgllc/norm/kernel/graph/model/INormProperty", "get", "()Ljava/lang/Object;");
mv.visitTypeInsn(CHECKCAST, "java/util/List");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setType", "(Lgov/state/tx/dmv/uom/common/person/PERSON_TYPE_OLD;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("type");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("type");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setFirstName", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("firstName");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("firstName");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setMiddleName", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("middleName");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("middleName");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setLastName", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("lastName");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("lastName");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setSalutation", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("salutation");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("salutation");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setNameSuffix", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("nameSuffix");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("nameSuffix");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setKnownAddresses", "(Ljava/util/Set;)V", "(Ljava/util/Set<Lgov/state/tx/dmv/uom/common/contact/IContactInformation;>;)V", null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("knownAddresses");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("knownAddresses");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setAllVehicles", "(Ljava/util/List;)V", "(Ljava/util/List<Lgov/state/tx/dmv/uom/common/vehicle/IVehicle;>;)V", null);
mv.visitCode();
mv.visitTypeInsn(NEW, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute");
mv.visitInsn(DUP);
mv.visitLdcInsn("allVehicles");
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lsgllc/norm/kernel/graph/model/impl/AbstractAttribute", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 0);
mv.visitLdcInsn("allVehicles");
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "gov/state/tx/dmv/uom/common/person/impl/PersonOld", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
mv.visitInsn(POP);
mv.visitInsn(RETURN);
mv.visitMaxs(4, 3);
mv.visitEnd();
}
cw.visitEnd();

return cw.toByteArray();
}
}
