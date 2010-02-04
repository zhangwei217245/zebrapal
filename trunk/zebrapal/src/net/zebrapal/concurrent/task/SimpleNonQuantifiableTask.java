/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task;

import java.util.Date;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
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
    public void doExecute() throws Exception {
        while (getTaskState().equals(TaskState.CREATED) || getTaskState().equals(TaskState.RUNNING) || getTaskState().equals(TaskState.SLEEP)) {
            TaskState state = getAtomOperation().execute();
            setTaskState(state);
        }
    }

}
