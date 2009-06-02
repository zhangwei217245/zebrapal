/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.factory;

import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.AbstractWorkTask;
import net.zebrapal.concurrent.task.SimpleNonQuantifiableTask;
import net.zebrapal.concurrent.task.SimplePredictableTask;
import net.zebrapal.concurrent.task.SimpleQuantifiableTask;

/**
 *
 * @author X-Spirit
 */
public class TaskFactory {
    private TaskType tasktype;
    private TaskState taskstate;

    public TaskFactory(){
        this.tasktype = TaskType.QUANTIFIABLE;
        this.taskstate = TaskState.CREATED;

    }

    public TaskFactory(TaskType tasktype){
        this.tasktype = tasktype;
    }

    public TaskFactory setTaskType(TaskType tasktype){
        this.tasktype = tasktype;
        return this;
    }

    public TaskState getTaskstate() {
        return taskstate;
    }

    public TaskFactory setTaskstate(TaskState taskstate) {
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
