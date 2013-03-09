package com.txdot.isd.rts.client.general.ui;import java.awt.Component;import java.awt.Container;import javax.swing.DefaultFocusManager;/* * * ContainerOrderFocusManager.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	04/12/2005	deprecate this class.  It is no longer  * 							consistent with the Java (1.4) focus layer. * 							defect 7701 * --------------------------------------------------------------------- */ /**  * A Simple Swing FocusManager that walks through components in the * order they were added to containers, the same way AWT works *  * @version	5.2.3		04/12/2005 * @author	Richard Hicks * <br>Creation Date:	10/10/2001 09:53:33 * @deprecated *//* &ContainerOrderFocusManager& */public class ContainerOrderFocusManager extends DefaultFocusManager {/** Return order based on order in which *    components were added to containers *//* &ContainerOrderFocusManager.compareTabOrder& */public boolean compareTabOrder(Component a, Component b){    // find a common container for the two components    Container commonContainer;    for (Component lookA = a; a != null; a = commonContainer)        {        commonContainer = lookA.getParent();        for (Component lookB = b; b != null; b = b.getParent())            if (commonContainer.isAncestorOf(b))                // determine which is found first                return (depthFindFirst(commonContainer, a, b) == 1);    }    // if neither share a parent container,    //   do the normal focus search    return super.compareTabOrder(a, b);}    /** Helper method that walks through containers, depth-first,     *  returning     *    0: container doesn't contain either a or b     *    1: found a first     *    2: found b first     *//* &ContainerOrderFocusManager.depthFindFirst& */    protected int depthFindFirst(Container c,                                 Component a,                                 Component b) {        Component[] comps = c.getComponents();        for(int i = 0; i < comps.length; i++)            if (comps[i] == a)                return 1;            else if (comps[i] == b)                return 2;            else if (comps[i] instanceof Container) {                int result = depthFindFirst((Container)comps[i], a, b);                if (result > 0)                    return result;            }        return 0;    }}/* #ContainerOrderFocusManager# */