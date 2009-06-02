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
public class SimplePredictableTask extends AbstractWorkTask{

    public SimplePredictableTask(TaskState taskstate){
        this.taskState = taskstate;
        this.tasktype = TaskType.PREDICTABLE;
    }
    @Override
    public void run() {
        
    }

    @Override
    public void writeTaskProgress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
