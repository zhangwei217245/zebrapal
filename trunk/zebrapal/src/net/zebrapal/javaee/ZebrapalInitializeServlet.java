/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.javaee;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author X-Spirit
 */
public class ZebrapalInitializeServlet extends HttpServlet{
    private static final long serialVersionUID = -4135075203934826888L;
    public static final String ZEBRAPAL_CONTEXT_KEY="ZEBRAPAL_CONTEXT_KEY";
    /**
     * Initialize this Servlet and create ZebrapalContext
     * @param cfg
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig cfg) throws javax.servlet.ServletException{
        super.init(cfg);
        log("Zebrapal Context is initializing...");
        
    }

    @Override
    public void destroy() {
        super.destroy();
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
