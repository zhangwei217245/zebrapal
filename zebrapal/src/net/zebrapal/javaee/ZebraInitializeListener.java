package net.zebrapal.javaee;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.ZebrapalContextLoader;

/**
 *
 * @author X-Spirit
 */
public class ZebraInitializeListener implements ServletContextListener{

    
    
    
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String configFileName = sc.getInitParameter(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_CONFIG_FILE);
        String contextLoaderClass = sc.getInitParameter(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_LOADER);
        TaskContext taskContext =null;
        try {
            sc.log("Zebrapal Context is initializing...");
            if(contextLoaderClass!=null){
                Object obj = Class.forName(contextLoaderClass).newInstance();
                if(obj instanceof ZebrapalContextLoader){
                    taskContext=((ZebrapalContextLoader)obj).loadContext(configFileName);
                }else{
                    throw new Exception("You spcified a wrong Context Loader.");
                }
                if(taskContext==null){
                    throw new Exception("TaskContext cannot be loaded due to previous error.");
                }else{
                    if(sc.getAttribute(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_KEY)!=null){
                        throw new Exception("Zebrapal Task Context is already loaded successfully in the ServletContext.");
                    }
                    sc.setAttribute(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_KEY, taskContext);
                }
            }else{
                throw new Exception("ContextLoader is not specified.");
            }
        } catch (Exception e) {
            sc.log(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        sc.log("Zebrapal Context is successfully initialized...");
    }
        
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String contextLoaderClass = sc.getInitParameter(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_LOADER);
        TaskContext taskContext =(TaskContext)sc.getAttribute(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_KEY);
        try {
            sc.log("Destroying Zebrapal Context ...");
            if(taskContext==null){
                throw new Exception("TaskContext has been unloaded.");
            }else{
                if(contextLoaderClass!=null){
                    Object obj = Class.forName(contextLoaderClass).newInstance();
                    if(obj instanceof ZebrapalContextLoader){
                        ((ZebrapalContextLoader)obj).unloadContext(taskContext);
                    }else{
                        throw new Exception("You spcified a wrong Context Loader.");
                    }
                }else{
                    throw new Exception("ContextLoader is not specified.");
                }
                sc.removeAttribute(ZebraServletInitKeys.ZEBRAPAL_CONTEXT_KEY);
            }
        }catch(Exception e){
            sc.log(e.getMessage(), e);
        }
        sc.log("Zebrapal Context is successfully destroyed...");
    }
    
}
