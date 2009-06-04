package net.zebrapal.concurrent.task;

import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;

/**
 *
 * @author X-Spirit
 */
public class SimpleQuantifiableTask extends AbstractWorkTask{
    
    private static final long serialVersionUID = -8232350586327794610L;
    
    public SimpleQuantifiableTask(TaskState taskstate){
        this.taskState = taskstate;
        this.tasktype = TaskType.QUANTIFIABLE;
    }

    @Override
    public void run() {
        try {
            atomOperation.init();
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
                    
                    completeCount++;
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
            
        }
    }

}
