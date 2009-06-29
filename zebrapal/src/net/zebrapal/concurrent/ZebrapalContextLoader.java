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
    
    public TaskContext loadContext(String configFile)throws Exception;
    public void unloadContext(TaskContext taskContext);
}
