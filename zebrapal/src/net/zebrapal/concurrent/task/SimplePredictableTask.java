/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.AtomException;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public class SimplePredictableTask extends AbstractWorkTask{

    private static final long serialVersionUID = -6416131919976886576L;
    
    public SimplePredictableTask(TaskState taskstate){
        setTaskState(taskstate);
        setTasktype(TaskType.PREDICTABLE);
    }

    public SimplePredictableTask (TaskContext taskContext,TaskState taskstate,String taskname,
            String taskowner,IAtomOperation atomOperation,Date createDate){
        setTaskContext(taskContext);
        setTaskState(taskstate);
        setTaskName(taskname);
        setTaskOwner(taskowner);
        setAtomOperation(atomOperation);
        setCreateDate(createDate);
        setTasktype(TaskType.PREDICTABLE);
    }
    
    @Override
    public void run() {
        try {
            getAtomOperation().init();
            setTotalCount(getAtomOperation().getTotalCount());
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
