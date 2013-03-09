package gov.state.tx.dmv.uom.operations.admin;

//import rtssource.old.data.util.RTSDate;

import java.io.Serializable;
import java.util.Date;

/**
 * Created By: sameloyiv
 * Date: 9/24/12
 * Time: 12:59 PM
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
public interface IAdminNameAddress extends Serializable {
    Date getChngTimestmp();

    int getDeleteIndi();

    int getId();

    int getOfcIssuanceNo();

    int getSubstaId();

    void setChngTimestmp(Date aaChngTimestmp);
}
