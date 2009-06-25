/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.javaee;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author X-Spirit
 */
public class ZebraInitializeListener implements ServletContextListener{

    public static final String ZEBRAPAL_CONTEXT_KEY="ZEBRAPAL_CONTEXT_KEY";
    
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        if(sc.getAttribute(ZEBRAPAL_CONTEXT_KEY)!=null){
            
        }
        sc.getInitParameter("");
        
    }
        
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
