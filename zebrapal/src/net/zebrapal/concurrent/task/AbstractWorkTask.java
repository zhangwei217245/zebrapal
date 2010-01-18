/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.io.Serializable;
import java.util.Date;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public abstract class AbstractWorkTask implements IWorkTask,Serializable{
    
    private TaskContext taskContext;

    private TaskState taskState = TaskState.CREATED;

    private String taskName;

    private String taskOwner;

    private TaskType tasktype;

    protected long completeCount;
    
    protected long failedCount;
    
    protected long totalCount;

    private IAtomOperation atomOperation;

    private Date createDate;

    public TaskContext getTaskContext() {
        return this.taskContext;
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

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
    protected void setTaskName(String taskName) {
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
    protected void setCompleteCount(long completeCount) {
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
    protected void setFailedCount(long failedCount) {
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
            return false;
        }else if(taskState.equals(TaskState.CANCELLED)){
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
    protected void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    protected void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the tasktype
     */
    public TaskType getTasktype() {
        return tasktype;
    }

    /**
     * @param tasktype the tasktype to set
     */
    protected void setTasktype(TaskType tasktype) {
        this.tasktype = tasktype;
    }
    /**
     * the createDate
     * @return
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     *
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    protected void updateTaskProgress(){
        if(++completeCount%getTaskContext().getPersistInterval()==0){
            getTaskContext().getTaskPersistManager().updateTaskInfo(this);
        }
    }

    @Override
    public String toString(){
        return tasktype+"_"+getTaskName()+" @ "+taskState+" : "+completeCount+" Completed";
    }

}
