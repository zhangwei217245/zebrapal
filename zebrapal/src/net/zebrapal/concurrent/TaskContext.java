package net.zebrapal.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RunnableScheduledFuture;
import net.zebrapal.concurrent.controller.TaskController;
import net.zebrapal.concurrent.persist.ITaskPersistenceManager;
import net.zebrapal.concurrent.task.IWorkTask;

/**
 *
 * @author X-Spirit
 */
public class TaskContext {
    private ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap;
    private TaskController taskController;
    private ITaskPersistenceManager taskPersistManager;

    public void initialize(){
        workerMap = new ConcurrentHashMap<IWorkTask, RunnableScheduledFuture>();
        
        taskController=TaskController.getInstance(this);
    }

    public void destroy(){
        
    }

    public ConcurrentMap<IWorkTask, RunnableScheduledFuture> getWorkerMap() {
        return workerMap;
    }

    public void setWorkerMap(ConcurrentMap<IWorkTask, RunnableScheduledFuture> workerMap) {
        this.workerMap = workerMap;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public ITaskPersistenceManager getTaskPersistManager() {
        return taskPersistManager;
    }

    public void setTaskPersistManager(ITaskPersistenceManager taskPersistManager) {
        this.taskPersistManager = taskPersistManager;
    }

   

}
