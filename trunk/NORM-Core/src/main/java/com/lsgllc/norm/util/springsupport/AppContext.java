package com.lsgllc.norm.util.springsupport;


/*
 *  AppContext
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
