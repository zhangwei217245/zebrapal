package net.zebrapal.concurrent.task;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.AtomException;
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
    
    public SimpleQuantifiableTask(TaskContext taskContext,TaskState taskstate,String taskname,
            String taskowner,IAtomOperation atomOperation,Date createDate){
        setTaskContext(taskContext);
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
            setCompleteCount(0);
            setFailedCount(0);
            if(getTaskState().equals(TaskState.CREATED)){
                getTaskContext().getTaskPersistManager().createTaskInfo(this);
            }
            if(getTaskState().equals(TaskState.RESTORED)){
                getAtomOperation().skip(completeCount);
                setTaskState(TaskState.RUNNING);
            }
            while(getTaskState().equals(TaskState.CREATED)||getTaskState().equals(TaskState.RUNNING)||getTaskState().equals(TaskState.SLEEP)){
                if(getTaskState().equals(TaskState.SLEEP)){
                    continue;
                }
                try {
                    TaskState state = getAtomOperation().execute();
                    setTaskState(state);
                    updateTaskProgress();
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
            try {
                getAtomOperation().close();
            } catch (AtomException ex) {
                ex.printStackTrace();
            }
            getTaskContext().getTaskPersistManager().updateTaskInfo(this);
        }
    }

}
