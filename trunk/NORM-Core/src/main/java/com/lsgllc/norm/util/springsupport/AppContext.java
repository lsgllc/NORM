package com.lsgllc.norm.util.springsupport;


/*
 *  AppContext
 *
 * This computer software and information is distributed with "restricted
 * rights." Use, duplication, or disclosure by the Government is subject
 * to restrictions as set forth in subparagraph (c)(1)(ii) of the Rights
 * in Technical Data and Computer Software clause at DFARS 252.227-7013.
 *
 * The Contractor is:
 *     Potomac Fusion, Inc.,
 *     4460 Brookfield Corporate Drive
 *     Chantilly, VA 20151
 *
 * created: Apr 30, 2010 
 *  Author: sampaw
 */

import org.springframework.context.ApplicationContext;

public class AppContext {
    private static ApplicationContext ctx;

    public static ApplicationContext getCtx() {
        return ctx;
    }

    public static void setCtx(ApplicationContext ctx) {
        AppContext.ctx = ctx;
    }
}
