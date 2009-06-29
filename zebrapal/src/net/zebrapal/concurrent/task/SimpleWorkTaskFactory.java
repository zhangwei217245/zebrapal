package net.zebrapal.concurrent.task;

import java.util.Date;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public class SimpleWorkTaskFactory {
    private TaskType taskType;
    private TaskContext taskContext;
    private String taskName;
    private String taskOwner;
    private IAtomOperation atomOperation;
    private Date createDate;


    public SimpleWorkTaskFactory(){
        this.taskType = TaskType.QUANTIFIABLE;
    }

    public SimpleWorkTaskFactory(TaskType tasktype){
        this.taskType = tasktype;
    }

    public void checkFields(){
        if(taskContext == null){
            throw new NullPointerException("taskContext cannot be null");
        }
        if(taskName == null||taskName.length()==0){
            taskName = "ZebraWorker_"+System.currentTimeMillis();
        }
        if(taskOwner == null||taskOwner.length()==0){
            taskOwner = "Zebrapal";
        }
        if(atomOperation == null){
            throw new NullPointerException("atomOperation cannot be null");
        }
        if(createDate == null){
            createDate = new Date();
        }
    }

    public AbstractWorkTask restoreTask(long completeCount,long failedCount,long totalCount){
        checkFields();
        if(TaskType.QUANTIFIABLE.equals(this.taskType)){
            AbstractWorkTask awt = new SimpleQuantifiableTask(taskContext, TaskState.RESTORED, taskName, taskOwner, atomOperation, createDate);
            awt.setCompleteCount(completeCount);
            awt.setFailedCount(failedCount);
            awt.setTotalCount(totalCount);
            return awt;
        }else if(TaskType.PREDICTABLE.equals(this.taskType)){
            AbstractWorkTask awt = new SimplePredictableTask(taskContext, TaskState.RESTORED, taskName, taskOwner, atomOperation, createDate);
            awt.setCompleteCount(completeCount);
            awt.setFailedCount(failedCount);
            awt.setTotalCount(totalCount);
            return awt;
        }else if (TaskType.NONQUANTIFIABLE.equals(this.taskType)){
            AbstractWorkTask awt = new SimpleNonQuantifiableTask(taskContext, TaskState.RESTORED, taskName, taskOwner, atomOperation, createDate);
            awt.setCompleteCount(completeCount);
            awt.setFailedCount(failedCount);
            awt.setTotalCount(totalCount);
            return awt;
        }
        return null;
    }

    public AbstractWorkTask createTask(){
        checkFields();
        if(TaskType.QUANTIFIABLE.equals(this.taskType)){
            return new SimpleQuantifiableTask(taskContext, TaskState.CREATED, taskName, taskOwner, atomOperation, createDate);
        }else if(TaskType.PREDICTABLE.equals(this.taskType)){
            return new SimplePredictableTask(taskContext, TaskState.CREATED, taskName, taskOwner, atomOperation, createDate);
        }else if (TaskType.NONQUANTIFIABLE.equals(this.taskType)){
            return new SimpleNonQuantifiableTask(taskContext, TaskState.CREATED, taskName, taskOwner, atomOperation, createDate);
        }
        return null;
    }

    /**
     * @param taskContext the taskContext to set
     * @return
     */
    public SimpleWorkTaskFactory setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
        return this;
    }

    /**
     * @param taskName the taskName to set
     * @return
     */
    public SimpleWorkTaskFactory setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    /**
     * @param taskOwner the taskOwner to set
     * @return
     */
    public SimpleWorkTaskFactory setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
        return this;
    }

    /**
     * @param atomOperation the atomOperation to set
     * @return
     */
    public SimpleWorkTaskFactory setAtomOperation(IAtomOperation atomOperation) {
        this.atomOperation = atomOperation;
        return this;
    }

    /**
     * @param createDate the createDate to set
     * @return 
     */
    public SimpleWorkTaskFactory setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }
}
