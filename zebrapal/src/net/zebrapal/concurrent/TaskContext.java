package net.zebrapal.concurrent;

import java.util.Set;
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
        taskController.shutDownNow();
        persistWorkerMap();
    }
    /**
     * Persist all the workTask which is cancelled and done in your workmap with recursion;
     */
    private void persistWorkerMap(){
        Set<IWorkTask> workerkeys=workerMap.keySet();
        for(IWorkTask workerkey:workerkeys){
            RunnableScheduledFuture rsf = workerMap.get(workerkey);
            if(rsf.isCancelled()||rsf.isDone()){
                System.out.println("Persisting workTask "+workerkey.getTaskOwner()+" : "+workerkey.getTaskName());
                taskPersistManager.updateTaskInfo(workerkey);
                System.out.println("Persisted and remove workTask "+workerkey.getTaskOwner()+" : "+workerkey.getTaskName());
                workerMap.remove(workerkey);
            }
        }
        if(!workerMap.isEmpty()){
            persistWorkerMap();
        }
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
