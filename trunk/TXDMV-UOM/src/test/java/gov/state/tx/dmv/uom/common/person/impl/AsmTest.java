package gov.state.tx.dmv.uom.common.person.impl;

//import com.lsgllc.norm.core.morpher.byclass.classloaders.IfaceClassGenerator;
//import com.lsgllc.norm.util.exceptions.DynaMorphTargetObjectNotSet;
//import com.lsgllc.norm.proto.ifaceproto.classloaders.DirectLoader;
//import com.lsgllc.norm.kernel.core.util.brokers.impl.NormMorphGraphUtils;
//import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
//import com.lsgllc.data.domain.core.objectclass.IExternalKey;
//import com.lsgllc.data.store.domain.person.IPersonOld;
//import com.lsgllc.data.store.domain.person.impl.PersonOld;
import com.lsgllc.norm.kernel.core.normgen.NormClassLoader;
import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import com.lsgllc.norm.kernel.graph.things.INormAttribute;
import com.lsgllc.norm.kernel.graph.things.INormEntity;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import gov.state.tx.dmv.uom.common.contact.IContactInformation;
import gov.state.tx.dmv.uom.common.person.IPersonNormReady;
import gov.state.tx.dmv.uom.common.person.PERSON_TYPE;
import gov.state.tx.dmv.uom.exceptions.StrangeAndWonderfulException;
import gov.state.tx.dmv.uom.exceptions.UOMSpecifiedException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/*
 * $Id
 *
 * created: Apr 20, 2010 at 11:30:27 AM
 *     354771
 ***************************************************************************
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/camel-context.xml" })
public class AsmTest  {

//    cl.
    @Autowired
    private OntologyBroker graphManager;
//    private DirectLoader graphClassLoader;
    public final Logger logger = Logger.getLogger(this.getClass());

    @Test
    public void myAsmTest() throws Exception, IllegalAccessException, InstantiationException, ClassNotFoundException, UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {

//        IfaceClassGenerator cl = NormMorphGraphUtils.getGraphByPackageName(IPersonOld.class.getPackage().getName(), new LinkedList<String>());
        ClassLoader myCl = graphManager.getContextClassLoader();
        NormClassLoader cl = new NormClassLoader(myCl,graphManager);
        Class<IPersonNormReady> testc = (Class<IPersonNormReady>) cl.loadClass(IPersonNormReady.class);
        IPersonNormReady<PERSON_TYPE> p = testc.newInstance();
        p.setFirstName("sam");
        System.out.println("firstName = " + p.getFirstName());
        // Set<HashMap<IContactInformation,List<String>>>
        ArrayList<String> aryLst = new ArrayList<String>();
        aryLst.add("One");
        aryLst.add("Two");
        aryLst.add("Three");
//        Class<IContactInformation> testd = (Class<IContactInformation>) cl.loadClass(IContactInformation.class);
//        IContactInformation ci = testd.newInstance();
//        HashMap<IContactInformation,List<String>> mhm = new HashMap<IContactInformation, List<String>>();
//        mhm.put(ci,aryLst);
//        Set<HashMap<IContactInformation,List<String>>> mhs = new HashSet<HashMap<IContactInformation, List<String>>>();
//        mhs.add(mhm);
//        p.ed(mhs);
//        mhs = p.getKnownAddresses();


//        Class<IOrganization> pc = cl.loadClass(IOrganization.class.getCanonicalName(),true);
//        IVehicle p = testc.newInstance();
//        IOrganization p = pc.newInstance();
//        p.setClassification("Classified");
//        Class<IExternalKey> ekc =  cl.loadClass(IExternalKey.class.getCanonicalName(),true);
//        IExternalKey eki = ekc.newInstance();
//        p.addExternalKeys(eki);
//        p.setAttributeValue(UUID.randomUUID());

//        Class pClass = cl.loadClass(PersonOld.class.getCanonicalName(),true);
//
//
//        System.out.println("Class name: " + pClass.getName());
//        for (Class clazz:pClass.getInterfaces()){
//            System.out.println("Iface: " + clazz.getSimpleName());
//        }
//
//        for (Method m: pClass.getDeclaredMethods()){
//            System.out.println("method: " + m.getName());
//
//        }
//
//        for (Field f: pClass.getDeclaredFields()){
//            System.out.print("field: "+f.getName());
//            if (f.isAccessible()){
//                System.out.println(" is Public ");
//            }  else {
//                System.out.println(" is Private ");
//            }
//        }
        
//        EverythingPublic ep = new EverythingPublic();
//
//        InputStream is = new FileInputStream("/Users/sampaw/IdeaProjects/lsgllc/DynaMorph/target/test-classes/com/lsgllc/norm/EverythingPublic.class");
//        ClassWriter cw = new ClassWriter(0);
//        ClassAdapter cv = new AsmAdapterProto(cw);
//        ClassReader cr = new ClassReader(is);
//        cr.accept(cv, 0);
//        Class newEPClazz = null; //cl.load(ep.getClass().getName(),cw.toByteArray());

//
//        System.out.println("Class name: " + newEPClazz.getName());
//        for (Class clazz:newEPClazz.getInterfaces()){
//            System.out.println("Iface: " + clazz.getSimpleName());
//        }
//
//        for (Method m: newEPClazz.getDeclaredMethods()){
//            System.out.println("method: " + m.getName());
//
//        }
//
//        for (Field f: newEPClazz.getDeclaredFields()){
//            System.out.print("field: "+f.getName());
//            if (f.isAccessible()){
//                System.out.println(" is Public ");
//            }  else {
//                System.out.println(" is Private ");
//            }
//        }
//        IAsmAdapterProto newEPInstance = (IAsmAdapterProto) pClass.newInstance();

//        newEPInstance = (IAsmAdapterProto)p;
//        newEPInstance._setObject( p);
//        Map<String, Object> fields = newInstance._getValues();
//        Map fieldValsep = newEPInstance._getValues();





//
//        byte[] b = this.graphManager.getRawClass(p);
////        ClassReader cr = new ClassReader(this.graphManager.getRawClass(p));
//        ClassReader cr3 = new ClassReader(b);
//        ClassWriter cw3 = new ClassWriter(0);
//        ClassAdapter cv3 = new AsmAdapterProto(cw3,p);
//        cr3.accept(cv3, 0);
//        Class pClass = p.getClass();
//        Class newClazz = cl.load(p.getClass().getName(),cw3.toByteArray());
//        System.out.println("Class name: " + newClazz.getName());
//        for (Class clazz:newClazz.getInterfaces()){
//            System.out.println("Iface: " + clazz.getSimpleName());
//        }
//
//        for (Method m: newClazz.getDeclaredMethods()){
//            System.out.println("method: " + m.getName());
//
//        }
//
//        for (Field f: newClazz.getDeclaredFields()){
//            System.out.print("field: "+f.getName());
//            if (f.isAccessible()){
//                System.out.println(" is Public ");
//            }  else {
//                System.out.println(" is Private ");
//            }
//        }
//        IAsmAdapterProto newInstance = (IAsmAdapterProto) newClazz.newInstance();
////        Map<String, Object> fields = newInstance._getValues();
//        ISourcedAttribute name = ((IPersonOld)newInstance).getFirstName();
//        Map fieldVals = newInstance._getValues();
//        if (fields != null){
//            for (Object fd: fields.values()){
//                System.out.println(fd);
//            }
//
//        }
//        for (Map.Entry<String,Object> o: newInstance.getoVals().entrySet()){
//            System.out.println("Key: " + o.getValue() + ", AbstractValue: " + o.getValue().toString());
//        }
 //       newInstance = p;
//        for (Class clazz:newInstance.getClass().getInterfaces()){
//            System.out.println("Iface: " + clazz.getSimpleName());
//        }
//        try {
////            newInstance.makeMetaObject(p,null,null);
//        } catch (NormSystemException e) {
//            e.printStackTrace();
//        }
//
//        try {
//
////            for (java.lang.reflect.Method m: modifiedClass.getMethods()){
////                if (m.getName().startsWith("_get")){
//////                    m.invoke(newInstance);
////                }
////            }
////            modifiedClass.getMethods()[0].invoke(null, new Object[] { p });
//            InstanceMetaDataGenerator m = new InstanceMetaDataGenerator();
//            ITargetAccess objectAccessor = m.getAccess();
//            logger.info(objectAccessor.makeMetaObject(p, p.getClass().getPackage().getName(),
//                    this.graphManager.getGraphUtils().getIdMakerByPackageName(p.getClass().getPackage().getName()).fetchIdAttributeName()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (DynaMorphTargetObjectNotSet dynaMorphTargetObjectNotSet) {
//            dynaMorphTargetObjectNotSet.printStackTrace();
//        } catch (NormSystemException e) {
//            e.printStackTrace();
//        }
//
////        FileInputStream fis = new FileInputStream("/Volumes/PFI_Work/IdeaProjects/lsgllc/ngg/GlobalGraph/trunk/javamodel/target/classes/com/lsgllc/globalgraph/data/domain/person/impl/PersonOld.class");
//        ClassReader cr;
//        List<Object> objs = dmcf.getClasses();
//      //  AbstractTargetAccess m = new AbstractTargetAccess();
//        for (Object obj : objs) {
//        Object obj = null;
        //           byte opcodes[] = getBytes(obj.getClass().);
//            cr = new ClassReader(fis);
//   //         cr.accept(m,0);
//        }
    }

    public static byte[] getBytes(Object obj) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        byte[] data = bos.toByteArray();
        return data;
    }

}
