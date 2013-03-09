package com.lsgllc.norm.kernel.core.util.brokers.impl;

import com.lsgllc.norm.kernel.graph.model.meta.owl.INormOntologySegment;
import com.lsgllc.norm.kernel.graph.things.INormObjectStore;
import com.lsgllc.norm.util.exceptions.NormUtilInitializationException;
import com.lsgllc.norm.util.impl.GRAPH_PROPERTY_KEYS;

import java.util.List;
import java.util.Map;

/*
 * $Id
 *
* The Contractor is: Loy Services Group, LLC. ***************************************************************************
 *
 * com.lsgllc.norm.util/NormMorphGraphUtils
 * created: Apr 20, 2010 at 11:30:27 AM
 *
 ***************************************************************************
*/
public class NormMorphGraphUtils {
    private static INormOntologySegment dmmg = null;
    private static Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphProperties = null;
    private static NormMorphGraphUtils instance = null;
    private static List<String> packages;

    public NormMorphGraphUtils(Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphProperties,
                               INormOntologySegment dmmg) {
        NormMorphGraphUtils.graphProperties = graphProperties;
        NormMorphGraphUtils.dmmg = dmmg;
    }

    public NormMorphGraphUtils instance(Map<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphProperties,
                                        INormOntologySegment dmmg) throws NormUtilInitializationException {
        if (NormMorphGraphUtils.instance == null || NormMorphGraphUtils.dmmg == null) {
            throw new NormUtilInitializationException();
        }
        NormMorphGraphUtils.instance = new NormMorphGraphUtils(graphProperties, dmmg);
        return instance();
    }

    public NormMorphGraphUtils instance() throws NormUtilInitializationException {
        if (NormMorphGraphUtils.graphProperties == null || NormMorphGraphUtils.dmmg == null) {
            throw new NormUtilInitializationException();
        }
        return NormMorphGraphUtils.instance;
    }

    public static INormObjectStore getGraphByPackageName(String instancePackage, List<String> packages) {
        for (Map.Entry<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphs : NormMorphGraphUtils.graphProperties.entrySet()) {
            if (isGraphOfPackage(instancePackage, packages)) {
                return getGraph(graphs.getKey());
            }
        }
        return null;
    }



    private static boolean isGraphOfPackage(String packageName, List<String> packages) {

        return isAllowed(
                packages,
                packageName);
    }




    private static INormObjectStore getGraph(String graphName) {
//        return (INormObjectStore) NormMorphGraphUtils.dmmg.get(graphName);
        return null;
    }

    public static boolean isAllowed(List<String> domains, String targetDomain) {
        if (domains==null){
            return false;
        }
        for (String domain : domains) {
            domain = domain + ".";
            if (targetDomain.length() < domain.length()) {
                if (domain.startsWith(targetDomain)) {
                    return true;
                }
            } else if (targetDomain.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    public static Map<GRAPH_PROPERTY_KEYS, ?> getGraphPropertiesByPackageName(String instancePackage, List<String> packages){
        for (Map.Entry<String, Map<GRAPH_PROPERTY_KEYS, ?>> graphs : NormMorphGraphUtils.graphProperties.entrySet()) {
            if (isGraphOfPackage(instancePackage, packages)) {
                return graphProperties.get(graphs.getKey());
            }
        }
        return null;

    }
    /**
      *
      * @return
      */
     public static String findCommonPackage(List<String> packages) {
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
//                 throw new DynaMorphInvalidGraphProperties("The package " + packageName + " is not part of " + shortestPackagename);
             }
         }
         return shortestPackagename;
     }

     /**
      *
      * @param packageName
      * @return
      */
     private static int getDotCount(String packageName) {
         int cnt = 0;
         while (true) {
             packageName = packageName.substring(0, packageName.lastIndexOf('.'));
             cnt++;
             if (packageName.lastIndexOf('.') == -1) {
                 return cnt;
             }
         }
     }
}
