/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.util.Date;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.AtomException;
import net.zebrapal.concurrent.task.atom.IAtomOperation;

/**
 *
 * @author X-Spirit
 */
public class SimpleNonQuantifiableTask extends AbstractWorkTask{

    private static final long serialVersionUID = -6277610883156296111L;
    
    public SimpleNonQuantifiableTask(TaskState taskstate){
        setTaskState(taskstate);
        setTasktype(TaskType.NONQUANTIFIABLE);
    }

    public SimpleNonQuantifiableTask (TaskContext taskContext,TaskState taskstate,String taskname,
            String taskowner,IAtomOperation atomOperation,Date createDate){
        setTaskContext(taskContext);
        setTaskState(taskstate);
        setTaskName(taskname);
        setTaskOwner(taskowner);
        setAtomOperation(atomOperation);
        setCreateDate(createDate);
        setTasktype(TaskType.NONQUANTIFIABLE);
    }

    @Override
    public void run() {
        try {
            if(getTaskState().equals(TaskState.CREATED)){
                getTaskContext().getTaskPersistManager().createTaskInfo(this);
            }
            if(getTaskState().equals(TaskState.RESTORED)){
                getAtomOperation().skip(completeCount);
                setTaskState(TaskState.RUNNING);
            }
            getAtomOperation().init();
            while(getTaskState().equals(TaskState.CREATED)||getTaskState().equals(TaskState.RUNNING)||getTaskState().equals(TaskState.SLEEP)){
                TaskState state = getAtomOperation().execute();
                setTaskState(state);
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
