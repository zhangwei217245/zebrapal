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
public class SimpleQuantifiableTask extends AbstractWorkTask {

    private static final long serialVersionUID = -8232350586327794610L;

    public SimpleQuantifiableTask(TaskState taskstate) {
        setTaskState(taskstate);
        setTasktype(TaskType.QUANTIFIABLE);
    }

    public SimpleQuantifiableTask(TaskContext taskContext, TaskState taskstate, String taskname,
            String taskowner, IAtomOperation atomOperation, Date createDate) {
        setTaskContext(taskContext);
        setTaskState(taskstate);
        setTaskName(taskname);
        setTaskOwner(taskowner);
        setAtomOperation(atomOperation);
        setCreateDate(createDate);
        setTasktype(TaskType.QUANTIFIABLE);
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

                getAtomOperation().skip(this.completeCount+this.failedCount);
                TaskState state = getAtomOperation().execute();
                setTaskState(state);
                updateTaskProgressByInterval(this);
            } catch (Exception e) {
                failedCount++;
                e.printStackTrace();
            }
            setTotalCount(completeCount + failedCount);
            if (!isRunningState()) {
                return;
            }
        }

    }
}
