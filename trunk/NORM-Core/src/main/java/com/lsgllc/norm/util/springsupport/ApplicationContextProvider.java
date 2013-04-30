package com.lsgllc.norm.util.springsupport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/*
  *  Author: sampaw
 */

public class ApplicationContextProvider implements ApplicationContextAware {
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        AppContext.setCtx(ctx);
    }
}
