package com.lsgllc.camel.datamodel;


import com.lsgllc.camel.datamodel.PersonDocument;
import org.apache.camel.Exchange;
import org.apache.camel.spring.SpringRouteBuilder;

import static org.apache.camel.language.juel.JuelExpression.el;

/**
 * Created By: sameloyiv
 * Date: 12/11/12
 * Time: 12:15 PM
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
public class EtlRoutes extends SpringRouteBuilder {
    public void configure() throws Exception {

        from("file:src/data?noop=true")
                .convertBodyTo(PersonDocument.class)
                .to("jpa:com.lsgllc.camel.datamodel.CustomerEntity");

        // the following will dump the database to files
        from("jpa:com.lsgllc.camel.datamodel.CustomerEntity?consumeDelete=false&delay=3000&consumeLockEntity=false")
                .setHeader(Exchange.FILE_NAME, el("${in.body.userName}.xml"))
                .to("file:target/customers");

    }
}