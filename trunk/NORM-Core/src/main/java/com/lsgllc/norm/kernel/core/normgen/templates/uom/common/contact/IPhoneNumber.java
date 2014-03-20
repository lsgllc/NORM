package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations.IOrganization;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:04 PM
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
public interface IPhoneNumber<T extends CONTACT_TYPES> extends Serializable {
    String getPhoneNumber();

    String getAreaCode();

    String getExchange();

    Integer getPhoneNumberDigital();    // for VOIP and Telephony

    Integer getAreaCodeDigital();       // for VOIP and Telephony

    Integer getExchangeDigital();       // for VOIP and Telephony

    T getType();

    IOrganization getOrganization();
}
