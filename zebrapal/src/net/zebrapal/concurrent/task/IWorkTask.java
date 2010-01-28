/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.util.Date;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;

/**
 *
 * @author X-Spirit
 */
public interface IWorkTask extends Runnable{
    
    public TaskContext getTaskContext();

    public int getFailedCount();

    public int getCompleteCount();

    public int getTotalCount();

    public Date getCreateDate();

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

    /**
     * 
     * @return
     */
    @Override
    public String toString();
}
