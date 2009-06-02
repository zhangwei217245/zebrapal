/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import net.zebrapal.concurrent.enumrations.TaskState;

/**
 *
 * @author X-Spirit
 */
public interface IWorkTask extends Runnable{
    public String getTaskName();
    public TaskState getTaskState();
    
}
