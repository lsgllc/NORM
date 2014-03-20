package gov.state.tx.dmv.uom.operations.admin;


import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created By: sameloyiv
 * Date: 9/24/12
 * Time: 1:29 PM
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
public interface IAssignedWorkstationIds extends Serializable, Comparable {
    int getAMDate();

    Map getAttributes();

    String getCashWsCd();

    int getCashWsId();

    Date getChngTimestmp();

    String getCPName();

    int getDeleteIndi();

    int getOfcIssuanceCd();

    int getOfcIssuanceNo();

    String getProdStatusCd();

    int getRedirPrtWsId();

    int getStkrVersionNo();

    int getSubstaId();

    String getTimeZone();

    int getTranstime();

    String getWsCd();

    int getWsId();
}
