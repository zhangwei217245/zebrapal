/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;

/**
 *
 * @author X-Spirit
 */
public class SimpleWorkTaskFactory {
    private TaskType tasktype;
    private TaskState taskstate;


    public SimpleWorkTaskFactory(){
        this.tasktype = TaskType.QUANTIFIABLE;
        this.taskstate = TaskState.CREATED;

    }

    public SimpleWorkTaskFactory(TaskType tasktype){
        this.tasktype = tasktype;
    }

    public SimpleWorkTaskFactory setTaskType(TaskType tasktype){
        this.tasktype = tasktype;
        return this;
    }

    public TaskState getTaskstate() {
        return taskstate;
    }

    public SimpleWorkTaskFactory setTaskstate(TaskState taskstate) {
        this.taskstate = taskstate;
        return this;
    }


    public AbstractWorkTask createTask(){
        if(TaskType.QUANTIFIABLE.equals(this.tasktype)){
            return new SimpleQuantifiableTask(this.taskstate);
        }else if(TaskType.PREDICTABLE.equals(this.tasktype)){
            return new SimplePredictableTask(this.taskstate);
        }else if (TaskType.NONQUANTIFIABLE.equals(this.tasktype)){
            return new SimpleNonQuantifiableTask(taskstate);
        }
        return null;
    }
}
