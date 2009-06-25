/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent;

/**
 *
 * @author X-Spirit
 */
public interface ZebrapalContextLoader {
    
    public TaskContext loadContext()throws ContextLoadException;
    public void unloadContext();
}
