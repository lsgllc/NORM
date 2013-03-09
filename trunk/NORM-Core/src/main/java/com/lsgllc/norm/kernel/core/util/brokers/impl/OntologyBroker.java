package com.lsgllc.norm.kernel.core.util.brokers.impl;

import com.lsgllc.norm.kernel.core.util.brokers.IOntologyBroker;
import com.lsgllc.norm.kernel.graph.model.meta.identity.OntologyId;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormOntology;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormOntologySegment;
import com.lsgllc.norm.kernel.graph.things.INormObjectStore;
import com.lsgllc.norm.util.exceptions.NormSystemException;
import com.lsgllc.norm.util.impl.GRAPH_PROPERTY_KEYS;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

/*
*  OntologyBroker
*
* This computer software and information is distributed with "restricted
* rights." Use, duplication, or disclosure by the Government is subject
* to restrictions as set forth in subparagraph (c)(1)(ii) of the Rights
* in Technical Data and Computer Software clause at DFARS 252.227-7013.
*
* The Contractor is:
*     Potomac Fusion, Inc.,
*     4460 Brookfield Corporate Drive
*     Chantilly, VA 20151
*
* created: Apr 30, 2010 
*  Author: sampaw
*/
@ContextConfiguration(locations = { "classpath:../openjpa/META-INF/spring/camel-context.xml" })
public class OntologyBroker implements IOntologyBroker {

    public Map<UUID,Object> orgObjs = new HashMap<UUID, Object>();
    public Map<UUID, INormOntologySegment> orgDmis = new HashMap<UUID, INormOntologySegment>();

    protected Set<String> normPersitablePackages = new HashSet<String>();
    private String graphName;
    private INormOntology dmmg;
    private static List<String> packages = null;
    private Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphProperties = null;
    private NormMorphGraphUtils graphUtils = null;
    private ClassPathXmlApplicationContext springContext;

    public OntologyBroker() {
    }

    /**
     *
     * @param graphProperties
     * @throws com.lsgllc.norm.util.exceptions.NormSystemException
     */

    public void setGraphProperties(Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphProperties){
        this.graphProperties = graphProperties;
    }


    /**
     *
     * @param graphName
     * @param packageName
     * @throws com.lsgllc.norm.util.exceptions.NormSystemException
     */
    public OntologyBroker(String graphName,
                          String packageName,
                          String jarFile) throws NormSystemException {
        if (graphProperties == null) {
            this.graphProperties = new HashMap<String, Map<GRAPH_PROPERTY_KEYS, ?>>();
        }
        Map<GRAPH_PROPERTY_KEYS, Object> graphProps = new HashMap<GRAPH_PROPERTY_KEYS, Object>();
        List<String> packages = new ArrayList<String>();
        packages.add(packageName);
        List<String> jars = new ArrayList<String>();
        jars.add(jarFile);
        graphProps.put(GRAPH_PROPERTY_KEYS.PACKAGE, packages);
        graphProps.put(GRAPH_PROPERTY_KEYS.DMJARS, jars);
        this.graphProperties.put(graphName, graphProps);
        initGraphs();
//        this.graphUtils = new NormMorphGraphUtils(this.graphProperties, this.dmmg);
    }

    /**
     *
     * @throws com.lsgllc.norm.util.exceptions.NormSystemException
     */
    private void initGraphs() throws NormSystemException {
        //TODO: When persistence is enabled, we'll fetch the MasterGraph from the data repo
//        this.dmmg = new NormOntology();

        for (Map.Entry<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphs : this.graphProperties.entrySet()) {
//            this.dmmg.persistMember(graphs.getKey());
        }
//        this.graphUtils = new NormMorphGraphUtils(this.graphProperties, this.dmmg);
    }

    /**
     *
     * @param graphs
     * @return
     * @throws com.lsgllc.norm.util.exceptions.NormSystemException
     */

    private INormObjectStore makeNewGraph(Map.Entry<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphs) throws NormSystemException {
//        try {
            String key = graphs.getKey();
        INormObjectStore retval = null;
            packages = (List<String>) this.graphProperties.get(key).get(GRAPH_PROPERTY_KEYS.PACKAGE);
            if (this.graphProperties.get(graphs.getKey()).containsKey(GRAPH_PROPERTY_KEYS.DMJARS)){
                List<String> jarFiles = (List<String>) this.graphProperties.get(graphs.getKey()).get(GRAPH_PROPERTY_KEYS.DMJARS);
//                retval=  new DynaMorphGraph<String, DynaMorphInstanceWrapper>(findCommonPackage(), new IfaceClassGenerator( ClassFinder.buildJarUrls(jarFiles),packages,Object.class.getClassLoader()));
            }
            return retval;
//        } catch (DynaMorphInvalidGraphProperties e) {
//            throw new NormSystemException(e);
//        }
    }

    /**
     *
     * @return
     */
    private String findCommonPackage()  {
        if (packages.size() == 1) {
            return packages.get(0);
        }
        int commonDotCnt = 0;
        String shortestPackagename = null;
        for (String packageName : packages) {
            int numberOfDots = getDotCount(packageName);
            if (commonDotCnt < numberOfDots) {
                commonDotCnt = numberOfDots;
                shortestPackagename = packageName;
            }
        }
        if (shortestPackagename == null) {
            return null;
        }
        int index = 0;
        StringBuffer commonPackageName = new StringBuffer();
        while (commonDotCnt-- > 0) {
            int endIndex = shortestPackagename.indexOf('.', index) + 1;
            commonPackageName.append(shortestPackagename.substring(index, endIndex));
            index = commonPackageName.length();

        }
        shortestPackagename = commonPackageName.toString().substring(0, commonPackageName.length() - 1);
        for (String packageName : packages) {
            if (!packageName.startsWith(shortestPackagename)) {
//                throw new DynaMorphInvalidGraphProperties("The package " + packageName + " is not part of " + shortestPackagename);
            }
        }
        packages.add(shortestPackagename);
        return shortestPackagename;
    }

    /**
     *
     * @param packageName
     * @return
     */
    private int getDotCount(String packageName) {
        int cnt = 0;
        while (true) {
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
            cnt++;
            if (packageName.lastIndexOf('.') == -1) {
                return cnt;
            }
        }
    }

    /**
     *
     * @return
     */
    public NormMorphGraphUtils getGraphUtils() {
        return this.graphUtils;
    }

    public List<String> getJarFiles(String packageName){
        return (List<String>) NormMorphGraphUtils.getGraphPropertiesByPackageName(packageName, packages).get(GRAPH_PROPERTY_KEYS.DMJARS);
    }

    public List<String> getPackages(String packageName){
        return (List<String>) NormMorphGraphUtils.getGraphPropertiesByPackageName(packageName, packages).get(GRAPH_PROPERTY_KEYS.PACKAGE);
    }


    public Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> getGraphProperties() {
        return graphProperties;
    }

    public Object newDynaMorphInstance(String canonicalInterfaceName) throws NormSystemException {
//        try {
//            IfaceClassGenerator cl;
//            cl = NormMorphGraphUtils.getGraphByPackageName(canonicalInterfaceName.substring(0, canonicalInterfaceName.lastIndexOf('.')),packages).getGraphClassLoader();
//            Class clazz = cl.loadClass(canonicalInterfaceName,true);
//            return clazz.newInstance();
            return null;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            throw new NormSystemException(e);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            throw new NormSystemException(e);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new NormSystemException(e);
//        }
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return this.springContext.getClassLoader();
    }

    @Override
    public OntologyId getOntology(String key) {
        return null;
    }

    public boolean isPackageNormPersistable(String classPackageName) {
        return this.normPersitablePackages.contains(classPackageName);
    }

    public void cacheNormPersistablePackage(String classPackageName) {
        this.normPersitablePackages.add(classPackageName);
    }

    public String newClass(String name) {
        return null;
    }

    public void setSpringContext(ClassPathXmlApplicationContext springContext) {
        this.springContext = springContext;
    }

    public ClassPathXmlApplicationContext getSpringContext() {
        return springContext;
    }


    @Override
    public Object getId(String key) {
        return null;
    }
}
