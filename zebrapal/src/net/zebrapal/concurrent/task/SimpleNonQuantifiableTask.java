/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class SimpleNonQuantifiableTask extends AbstractWorkTask{

    private static final long serialVersionUID = -6277610883156296111L;
    
    public SimpleNonQuantifiableTask(TaskState taskstate){
        setTaskState(taskstate);
        setTasktype(TaskType.NONQUANTIFIABLE);
    }

    public SimpleNonQuantifiableTask (TaskController taskController,TaskState taskstate,String taskname,
            String taskowner,IAtomOperation atomOperation,Date createDate){
        setTaskController(taskController);
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
            getAtomOperation().init();
            getAtomOperation().execute();
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
