/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent;

/**
 *
 * @author X-Spirit
 */
public class ContextLoadException extends Exception {
    private static final long serialVersionUID = -7372386092808488113L;
    /**
     * Creates a new instance of <code>ContextLoadException</code> without detail message.
     */
    public ContextLoadException() {
        
    }


    /**
     * Constructs an instance of <code>ContextLoadException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContextLoadException(String msg) {
        super(msg);
    }

    public ContextLoadException(String msg,Throwable t){
        super(msg, t);
    }
}
