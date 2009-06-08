/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.util.Date;
import net.zebrapal.concurrent.controller.TaskController;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public class SimpleWorkTaskFactory {
    private TaskType taskType;
    private TaskState taskState;
    private TaskController taskController;
    private String taskName;
    private String taskOwner;
    private IAtomOperation atomOperation;
    private Date createDate;


    public SimpleWorkTaskFactory(){
        this.taskType = TaskType.QUANTIFIABLE;
        this.taskState = TaskState.CREATED;
    }

    public SimpleWorkTaskFactory(TaskType tasktype){
        this.taskType = tasktype;
        this.taskState = TaskState.CREATED;
    }

    public SimpleWorkTaskFactory setTaskState(TaskState taskState){
        this.taskState = taskState;
        return this;
    }

    public void checkFields() throws NullPointerException{
        if(taskController == null){
            throw new NullPointerException("taskController cannot be null");
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

    public AbstractWorkTask createTask() throws NullPointerException{
        checkFields();
        if(TaskType.QUANTIFIABLE.equals(this.taskType)){
            return new SimpleQuantifiableTask(taskController, taskState, taskName, taskOwner, atomOperation, createDate);
        }else if(TaskType.PREDICTABLE.equals(this.taskType)){
            return new SimplePredictableTask(taskController, taskState, taskName, taskOwner, atomOperation, createDate);
        }else if (TaskType.NONQUANTIFIABLE.equals(this.taskType)){
            return new SimpleNonQuantifiableTask(taskController, taskState, taskName, taskOwner, atomOperation, createDate);
        }
        return null;
    }

    /**
     * @param taskController the taskController to set
     * @return
     */
    public SimpleWorkTaskFactory setTaskController(TaskController taskController) {
        this.taskController = taskController;
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
