/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import net.zebrapal.concurrent.controller.TaskController;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;

/**
 *
 * @author X-Spirit
 */
public interface IWorkTask extends Runnable{
    
    public TaskController getTaskController();

    public long getFailedCount();

    public long getCompleteCount();

    public long getTotalCount();

    public long getCreateDate();

    public TaskType getTasktype();
    
    //public void setTasktype(TaskType tasktype);

    //public void setTaskController(TaskController taskController);
    /**
     * @return the taskName
     */
    public String getTaskName();

    /**
     * @param taskName the taskName to set
     */
    //public void setTaskName(String taskName);
    /**
     * @return the taskState
     */
    public TaskState getTaskState();

    /**
     * @param taskState the taskState to set
     */
    //public void setTaskState(TaskState taskState);
    /**
     * @return the taskOwner
     */
    public String getTaskOwner();

    /**
     * @param taskOwner the taskOwner to set
     */
    //public void setTaskOwner(String taskOwner);
}
