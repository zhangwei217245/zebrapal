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
public class SimpleNonQuantifiableTask extends AbstractWorkTask{

    private static final long serialVersionUID = -6277610883156296111L;
    
    public SimpleNonQuantifiableTask(TaskState taskstate){
        this.taskState = taskstate;
        this.tasktype = this.tasktype = TaskType.NONQUANTIFIABLE;
    }
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeTaskProgress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
