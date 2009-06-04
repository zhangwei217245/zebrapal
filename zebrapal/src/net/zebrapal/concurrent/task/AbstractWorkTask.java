/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.io.Serializable;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public abstract class AbstractWorkTask implements IWorkTask,Serializable{
    
	protected TaskState taskState = TaskState.CREATED;

    protected String taskName;

    protected String taskOwner;

    protected TaskType tasktype;

    protected long completeCount;
    
    protected long failedCount;

    protected IAtomOperation atomOperation;

    /**
     * @return the taskState
     */
    public TaskState getTaskState() {
        return taskState;
    }

    /**
     * @param taskState the taskState to set
     */
    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the completeCount
     */
    public long getCompleteCount() {
        return completeCount;
    }

    /**
     * @param completeCount the completeCount to set
     */
    public void setCompleteCount(long completeCount) {
        this.completeCount = completeCount;
    }

    public IAtomOperation getAtomOperation() {
        return atomOperation;
    }

    public void setAtomOperation(IAtomOperation atomOperation) {
        this.atomOperation = atomOperation;
    }

    /**
     * @return the failedCount
     */
    public long getFailedCount() {
        return failedCount;
    }

    /**
     * @param failedCount the failedCount to set
     */
    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }

    /**
     * check the TaskState and do the operation accordingly.
     * CRASHED and FINISHED will not be set during this decision
     * @return
     */
    protected boolean isRunningState(){
        //boolean b = true;
        if(taskState.equals(TaskState.SLEEP)){
            return true;
        }else if(taskState.equals(TaskState.HIBERNATE)){
            try {
                atomOperation.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }else if(taskState.equals(TaskState.CANCELLED)){
            try {
                atomOperation.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }else if(taskState.equals(TaskState.CRASHED)){
            return false;
        }else if(taskState.equals(TaskState.FINISHED)){
            return false;
        }
        return true;
    }

    /**
     * @return the taskOwner
     */
    public String getTaskOwner() {
        return taskOwner;
    }

    /**
     * @param taskOwner the taskOwner to set
     */
    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }
}
