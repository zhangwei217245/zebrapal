/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.javaee;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.ZebrapalContextLoader;

/**
 *
 * @author X-Spirit
 */
public class ZebrapalInitializeServlet extends HttpServlet{
    private static final long serialVersionUID = -4135075203934826888L;
    public static final String ZEBRAPAL_CONTEXT_KEY="ZEBRAPAL_CONTEXT_KEY";
    public static final String ZEBRAPAL_CONTEXT_LOADER="ZEBRAPAL_CONTEXT_LOADER";
    public static final String ZEBRAPAL_CONTEXT_CONFIG_FILE="ZEBRAPAL_CONTEXT_CONFIG_FILE";

    private TaskContext taskContext;
    /**
     * Initialize this Servlet and create ZebrapalContext
     * @param cfg
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig cfg) throws javax.servlet.ServletException{
        super.init(cfg);
        
        ServletContext sc = cfg.getServletContext();
        String configFileName = cfg.getInitParameter(ZEBRAPAL_CONTEXT_CONFIG_FILE);
        String contextLoaderClass = cfg.getInitParameter(ZEBRAPAL_CONTEXT_LOADER);
        try {
            log("Zebrapal Context is initializing...");
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
                    if(sc.getAttribute(ZEBRAPAL_CONTEXT_KEY)!=null){
                        throw new Exception("zebrapal task context is already loaded in the ServletContext.");
                    }
                    sc.setAttribute(ZEBRAPAL_CONTEXT_KEY, taskContext);
                }
            }else{
                throw new Exception("ContextLoader is not specified.");
            }
        } catch (Exception e) {
            log(e.getMessage(), e);
        }
        log("Zebrapal Context is successfully initialized...");
    }

    @Override
    public void destroy() {
        try {
            log("Destroying Zebrapal Context ...");
            if(taskContext==null){
                throw new Exception("TaskContext has been unloaded.");
            }else{
                taskContext.destroy();
            }
        }catch(Exception e){
            log(e.getMessage(), e);
        }
        log("Zebrapal Context is successfully destroyed...");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }


}
