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
public class SimpleQuantifiableTask extends AbstractWorkTask{
    
    private static final long serialVersionUID = -8232350586327794610L;
    
    public SimpleQuantifiableTask(TaskState taskstate){
        setTaskState(taskstate);
        setTasktype(TaskType.QUANTIFIABLE);
    }
    
    public SimpleQuantifiableTask(TaskController taskController,TaskState taskstate,String taskname,
            String taskowner,IAtomOperation atomOperation,Date createDate){
        setTaskController(taskController);
        setTaskState(taskstate);
        setTaskName(taskname);
        setTaskOwner(taskowner);
        setAtomOperation(atomOperation);
        setCreateDate(createDate);
        setTasktype(TaskType.QUANTIFIABLE);
    }

    @Override
    public void run() {
        try {
            getAtomOperation().init();
            setCompleteCount(0L);
            setFailedCount(0L);
            if(getTaskState().equals(TaskState.RESTORED)){
                getAtomOperation().skip(completeCount);
                setTaskState(TaskState.RUNNING);
            }
            while(getTaskState().equals(TaskState.CREATED)||getTaskState().equals(TaskState.RUNNING)||getTaskState().equals(TaskState.SLEEP)){
                if(getTaskState().equals(TaskState.SLEEP)){
                    continue;
                }
                try {
                    getAtomOperation().execute();
                    if(++completeCount%1009==0){
                        getTaskController().getTaskPersistManager().updateTaskInfo(this);
                    }
                } catch (Exception e) {
                    failedCount++;
                    e.printStackTrace();
                }
                setTotalCount(completeCount+failedCount);
                if(!isRunningState()){
                    return;
                }
            }
            setTaskState(TaskState.FINISHED);
        } catch (Exception e) {
            setTaskState(TaskState.CRASHED);
            e.printStackTrace();
        } finally{
            getAtomOperation().close();
            getTaskController().getTaskPersistManager().updateTaskInfo(this);
        }
    }

}
