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

    private static final long serialVersionUID = -6416131919976886576L;
    
    public SimplePredictableTask(TaskState taskstate){
        this.taskState = taskstate;
        this.tasktype = TaskType.PREDICTABLE;
    }
    @Override
    public void run() {
        try {
            atomOperation.init();
            setTotalCount(atomOperation.getTotalCount());
            setCompleteCount(0L);
            setFailedCount(0L);
            if(taskState.equals(TaskState.RESTORED)){
                atomOperation.skip(completeCount);
                taskState = TaskState.RUNNING;
            }
            while(taskState.equals(TaskState.CREATED)||taskState.equals(TaskState.RUNNING)||taskState.equals(TaskState.SLEEP)){
                if(taskState.equals(TaskState.SLEEP)){
                    continue;
                }
                try {
                    atomOperation.execute();
                    if(++completeCount%1009==0){
                        getTaskController().getTaskPersistManager().updateTaskInfo(this);
                    }
                } catch (Exception e) {
                    failedCount++;
                    e.printStackTrace();
                }
                if(!isRunningState()){
                    return;
                }
            }
            taskState = TaskState.FINISHED;
        } catch (Exception e) {
            taskState = TaskState.CRASHED;
            e.printStackTrace();
        } finally{
            atomOperation.close();
            getTaskController().getTaskPersistManager().updateTaskInfo(this);
        }
    }


}
