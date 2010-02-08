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
public class SimplePredictableTask extends AbstractWorkTask {

    private static final long serialVersionUID = -6416131919976886576L;
    

    public SimplePredictableTask(TaskState taskstate) {
        setTaskState(taskstate);
        setTasktype(TaskType.PREDICTABLE);
    }

    public SimplePredictableTask(TaskContext taskContext, TaskState taskstate, String taskname,
            String taskowner, IAtomOperation atomOperation, Date createDate) {
        setTaskContext(taskContext);
        setTaskState(taskstate);
        setTaskName(taskname);
        setTaskOwner(taskowner);
        setAtomOperation(atomOperation);
        setCreateDate(createDate);
        setTasktype(TaskType.PREDICTABLE);
    }

    @Override
    public void doExecute() throws Exception {
        //getAtomOperation().skip(this.completeCount+this.failedCount);
        while (getTaskState().equals(TaskState.CREATED) || getTaskState().equals(TaskState.RUNNING) || getTaskState().equals(TaskState.SLEEP)) {
            if (getTaskState().equals(TaskState.SLEEP)) {
                continue;
            }
            try {
                ++curindex;
                if(curindex>=skipIndex&&curindex<(skipIndex+skipCount)){
                    curindex+=skipCount;
                    getAtomOperation().skip(skipCount);
                    this.setTotalCount(totalCount-skipCount);
                }
                
                
                TaskState state = getAtomOperation().execute();
                updateTaskProgressByInterval(this);
                setTaskState(state);
            } catch (Exception e) {
                failedCount++;
                setTaskState(TaskState.CRASHED);
                e.printStackTrace();
            }
            if (!isRunningState()) {
                return;
            }
        }
    }
}
