package net.zebrapal.concurrent.task;

import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;

/**
 *
 * @author X-Spirit
 */
public class SimpleQuantifiableTask extends AbstractWorkTask{
    public SimpleQuantifiableTask(TaskState taskstate){
        this.taskState = taskstate;
        this.tasktype = TaskType.QUANTIFIABLE;
    }
    public void run() {
        try {
            atomOperation.init();
            setCompleteCount(0L);
            setFailedCount(0L);
            if(taskState.equals(TaskState.RESTORED)){
                atomOperation.skip(completeCount);
                taskState = TaskState.RUNNING;
            }
            while(taskState.equals(TaskState.CREATED)||taskState.equals(TaskState.RUNNING) ){
                
                try {
                    atomOperation.execute();
                    
                    completeCount++;
                } catch (Exception e) {
                    failedCount++;
                    e.printStackTrace();
                }
                if(!isRunningState()){
                    break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            atomOperation.close();
        }
    }

    @Override
    public void writeTaskProgress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
