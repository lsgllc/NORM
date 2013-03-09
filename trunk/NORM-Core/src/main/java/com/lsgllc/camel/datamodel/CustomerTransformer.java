package com.lsgllc.camel.datamodel;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.util.CastUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.List;

/**
 * Created By: sameloyiv
 * Date: 12/11/12
 * Time: 12:16 PM
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
@Converter
public class CustomerTransformer {

    private static final transient Logger LOG = LoggerFactory.getLogger(CustomerTransformer.class);

    /**
     * A transformation method to convert a person document into a customer
     * entity
     * @throws Exception
     */
    @Converter
    public CustomerEntity toCustomer(PersonDocument doc, Exchange exchange) throws Exception {
        JpaTemplate template = exchange.getIn().getHeader("CamelJpaTemplate", JpaTemplate.class);


        String user = doc.getUser();
        CustomerEntity customer = findCustomerByName(template, user);

        // let's convert information from the document into the entity bean
        customer.setUserName(user);
        customer.setFirstName(doc.getFirstName());
        customer.setSurname(doc.getLastName());
        customer.setCity(doc.getCity());

        LOG.debug("Created object customer: " + customer);
        return customer;
    }

    /**
     * Finds a customer for the given username
     */
    protected CustomerEntity findCustomerByName(JpaTemplate template, String user) throws Exception {
        List<CustomerEntity> list = CastUtils.cast(template.find("select x from customer"
                + " x where x.userName = ?1", user));
        if (list.isEmpty()) {
            CustomerEntity answer = new CustomerEntity();
            answer.setUserName(user);
            return answer;
        } else {
            return list.get(0);
        }
    }

}