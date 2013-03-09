package com.lsgllc.norm.util.client;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 1:50 PM
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
public interface INormPersistable {
   INormPersistable persist(INormPersistable iFace);
   INormPersistable delete(INormPersistable iFace);
   INormPersistable create(INormPersistable iFace);
   INormPersistable update(INormPersistable iFace);
}
