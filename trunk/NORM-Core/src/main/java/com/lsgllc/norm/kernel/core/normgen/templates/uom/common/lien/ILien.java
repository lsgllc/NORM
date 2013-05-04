package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.lien;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations.IFinancial;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 12:54 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface ILien extends Serializable {
    IFinancial getLienHolder();
}
