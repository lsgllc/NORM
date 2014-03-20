package com.lsgllc.norm.kernel.core.normgen;

import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.kernel.core.normgen.asm.NormClassReader;
import com.lsgllc.norm.kernel.core.normgen.asm.NormClassWriter;
import com.lsgllc.norm.kernel.core.normgen.asm.NormPersistentClassGenerator;
import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;

import java.io.InputStream;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: sameloyiv
 * Date: 12/18/12
 * Time: 5:37 PM
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
public class NormClassLoader extends SecureClassLoader {
    protected OntologyBroker graphManager;
    private static final Map<String, BufHolder> classCache = new HashMap<String, BufHolder>();

    public NormClassLoader(ClassLoader myCl, OntologyBroker graphManager) {
        this(myCl);
        this.graphManager = graphManager;
    }

    private class BufHolder {
        public byte[] buf;
        public CodeSource cs;
        public String className;
        public Class clazz;

        private BufHolder(byte[] buf, CodeSource cs, String className) {
            this.buf = buf;
            this.cs = cs;
            this.className = className;
        }
    }
    public NormClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public synchronized Class<?> loadClass(Class clazz) throws ClassNotFoundException {
        return this.loadClass(clazz.getCanonicalName(), true);
    }

    @Override
    public synchronized Class<?> loadClass(final String resource,
                                              final boolean resolve) throws ClassNotFoundException {

        // gets an input stream to read the bytecode of the class

        Class c = findSystemClass(resource);
        if (c != null && isNormPersitable(c)) {
            c = findBufferedClass(c,resolve);
            if (c != null){
                return c;
            }
        }
        // adapts the class on the fly

        // optional: stores the adapted class on disk
//        try {
//            FileOutputStream fos = new FileOutputStream(resource + ".adapted");
//            fos.write(b);
//            fos.close();
//        } catch (IOException e) {
//        }

        // returns the adapted class
        System.out.println("loading class " + resource);
        return super.loadClass(resource, resolve);
    }

    private boolean isNormPersitable(Class<?> clazz) {
        for (Class<?> c: clazz.getInterfaces()) {
            if (c.getCanonicalName().equals(INormPersistable.class.getCanonicalName())){
                this.graphManager.cacheNormPersistablePackage(clazz.getPackage().getName().replace('.','/'));
                return true;
            }
        }
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private Class findBufferedClass(Class clazz, boolean resolve) throws ClassNotFoundException {
        if (!resolve){
            return clazz;
        }
        String[] names = this.createNormProxyClassName(clazz.getCanonicalName());
        BufHolder bh = classCache.get(names[0]);
        String nameMod = "/" +clazz.getCanonicalName().replace('.','/') + ".class";
        if (bh == null){
            InputStream is = clazz.getResourceAsStream(nameMod);
            try {
                byte[] b;
                NormClassReader cr = new NormClassReader(is, names[1], this.graphManager);
                NormClassWriter cw = new NormClassWriter(cr,0, this.graphManager);
                NormPersistentClassGenerator cv = new NormPersistentClassGenerator(cw, this.graphManager, names[1]);
                cr.accept(cv, 0);
                b = cw.toByteArray();
                return defineClass(names[0],b,0,b.length);
            } catch (Exception e) {
                throw new ClassNotFoundException(names[0], e);
            }
//            D$M$_ProxyTemplate proxy = new D$M$_ProxyTemplate(clazz, (AsmIfaceMethodsMap) this.ifaceMethods,this.allowedPackages);
//            try {
//                byte[] b = proxy.dump();
//                bh = new BufHolder(b,getCodeSource(proxy.getClassName()),proxy.getClassName());
//                classCache.put(clazz.getCanonicalName(),bh);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        if (bh != null){
            if (bh.clazz == null){
                bh.clazz = defineClass(bh.className.replace('/','.'), bh.buf, 0, bh.buf.length, bh.cs);
            }
            return bh.clazz;
        }
        return null;

    }
    private String[] createNormProxyClassName(String inName) {
        String[] names = new String[2];
        String name = inName.replace('.','/');
        int lastSlash = name.lastIndexOf("/");
        int offset = 1;
        StringBuffer nameBuffer = new StringBuffer();
        nameBuffer.append(name.substring(0,lastSlash+1));
        if (name.substring(lastSlash+offset,lastSlash+offset).equals("I")){
            offset++;
        }
        nameBuffer.append(name.substring(lastSlash+offset,lastSlash + offset).toUpperCase());
        nameBuffer.append(name.substring(lastSlash+offset+1));
        nameBuffer.append("$Norm_Proxy");
        names[0] = inName.substring(0,lastSlash+1)+inName.substring(lastSlash+offset,lastSlash + offset).toUpperCase()+ inName.substring(lastSlash+offset+1)+"$Norm_Proxy";
        names[1] = nameBuffer.toString();
        return names;
    }


//    public static void main(final String args[]) throws Exception {
//        // loads the application class (in args[0]) with an Adapt class loader
//        ClassLoader loader = new Adapt();
//        Class<?> c = loader.loadClass(args[0]);
//        // calls the 'main' static method of this class with the
//        // application arguments (in args[1] ... args[n]) as parameter
//        Method m = c.getMethod("main", new Class<?>[] { String[].class });
//        String[] applicationArgs = new String[args.length - 1];
//        System.arraycopy(args, 1, applicationArgs, 0, applicationArgs.length);
//        m.invoke(null, new Object[] { applicationArgs });
//    }

    public void setGraphManager(OntologyBroker graphManager) {
        this.graphManager = graphManager;
    }
}
