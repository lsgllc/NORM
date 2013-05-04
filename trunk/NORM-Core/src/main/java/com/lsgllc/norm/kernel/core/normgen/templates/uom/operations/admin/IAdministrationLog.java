package com.lsgllc.norm.kernel.core.normgen.templates.uom.operations.admin;


import java.io.Serializable;
import java.util.Date;

/**
 * Created By: sameloyiv
 * Date: 9/24/12
 * Time: 1:05 PM
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
public interface IAdministrationLog extends Serializable {
    String getAction();

    String getEntity();

    String getEntityValue();

    int getOfcIssuanceNo();

//    ReportSearchData getReportSearchData();

    int getSubStaId();

    int getTransAMDate();

    String getTransEmpId();

    int getTransTime();

    Date getTransTimestmp();

    int getTransWsId();
}
