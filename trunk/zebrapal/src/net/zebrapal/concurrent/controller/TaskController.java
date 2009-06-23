package net.zebrapal.concurrent.controller;

import net.zebrapal.concurrent.task.IWorkTask;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.persist.ITaskPersistenceManager;
import net.zebrapal.concurrent.task.AbstractWorkTask;

/**
 *
 * @author X-Spirit
 */
public class TaskController {
    
    private ScheduledExecutorService executor;

    private static TaskController taskController = new TaskController();

    private ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap;

    private ITaskPersistenceManager taskPersistManager;

    private TaskController(){
        init();
    }

    public static TaskController getInstance(){
        return taskController;
    }

    public void init(){
        ((ScheduledThreadPoolExecutor)executor).setThreadFactory(new TaskThreadFactory());
    }

    /**
     * Execute the task immediately.
     * This method is deprecated, you cannot use it because it won't return
     * a FutureTask represent for the WorkTask.
     * @param command
     * @deprecated
     */
    @Deprecated
    public void execute(IWorkTask command) {
        if (command == null)
            throw new NullPointerException();
        scheduleAtFixedRate(command,0, 0, TimeUnit.NANOSECONDS);
    }

    /**
     * Submit the task immediately and the task will run at once.
     * this method returns a FutureTask.
     * @param command
     * @return
     */
    public Future<?> submit(IWorkTask command) {
        return scheduleAtFixedRate(command,0, 0, TimeUnit.NANOSECONDS);
    }
    
    public RunnableScheduledFuture<?> schedule(IWorkTask command,long delay,TimeUnit unit){
        return (RunnableScheduledFuture<?>) scheduleAtFixedRate(command, delay,0, unit);
    }
    
    public synchronized RunnableScheduledFuture<?> scheduleAtFixedRate(IWorkTask command,long initialDelay,long period,TimeUnit unit){
        ((AbstractWorkTask)command).setTaskController(this);
        RunnableScheduledFuture<?> ft = (RunnableScheduledFuture<?>)executor.scheduleAtFixedRate(command, initialDelay,period, unit);
        workerMap.put(command, ft);
        taskPersistManager.createTaskInfo(command);
        return ft;
    }
    
    public RunnableScheduledFuture<?> scheduleWithFixedDelay(IWorkTask command,long initialDelay,long delay,TimeUnit unit){
        return (RunnableScheduledFuture<?>) scheduleAtFixedRate(command, initialDelay,-delay, unit);
    }

    /**
     * Make the task falls asleep. The task won't be removed from the workerMap but will be persisted once.
     * For Nonquantifiable Task, it may not fall asleep...
     * @param task
     */
    public synchronized void fallAsleep(IWorkTask task){
        try {
            ((AbstractWorkTask)task).setTaskState(TaskState.SLEEP);
            taskPersistManager.updateTaskInfo(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Hibernate the task. The task will be removed from the workerMap and will be persisted once.
     * @param task
     */
    public synchronized void hibernate(IWorkTask task){
        try {
            ((AbstractWorkTask)task).setTaskState(TaskState.HIBERNATE);
            remove(task);
            taskPersistManager.updateTaskInfo(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Cancel a Task forcibly. In this situation, the task persistence manager will not persist this task.
     * If a task need to be persisted after the controller stops its work, please call the stop method.
     * @param task
     * @return
     */
    public synchronized boolean cancelTask(IWorkTask task){
        try {
            if(!workerMap.get(task).isCancelled()){
                remove(task);
            }else{
                ((AbstractWorkTask)task).setTaskState(TaskState.CANCELLED);
                workerMap.get(task).cancel(true);
                remove(task);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Remove the task from the Blocking Queue of SchedulerExecutor forcibly
     * @param command
     * @return
     */
    private synchronized boolean remove(IWorkTask command){
        if(command.getTasktype().equals(TaskType.NONQUANTIFIABLE)){
            workerMap.get(command).cancel(true);
        }
        workerMap.remove(command);
        return ((ScheduledThreadPoolExecutor)executor).remove(command);
    }

    /**
     * Clean up all the canceled work from both the workMap and the executor
     */
    public synchronized void cleanUpCanceledTask(){
        Set<IWorkTask> keys = workerMap.keySet();
        for(IWorkTask task:keys){
            if(workerMap.get(task).isCancelled()){
                workerMap.remove(task);
            }
        }
        ((ScheduledThreadPoolExecutor)executor).purge();
    }

    public synchronized void shutDown(){
        if(!(executor.isTerminated()&&executor.isShutdown())){
            executor.shutdown();
        }
    }
    public synchronized List<Runnable> shutDownNow(){
        if(!(executor.isTerminated()&&executor.isShutdown())){
            return executor.shutdownNow();
        }
        return null;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public ConcurrentMap getWorkerMap() {
        return workerMap;
    }

    /**
     * 
     * @param workerMap
     */
    public void setWorkerMap(ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap) {
        this.workerMap = workerMap;
    }

    public ITaskPersistenceManager getTaskPersistManager() {
        return taskPersistManager;
    }

    public void setTaskPersistManager(ITaskPersistenceManager taskPersistManager) {
        this.taskPersistManager = taskPersistManager;
    }
    

    private class TaskThreadFactory implements ThreadFactory{
        public Thread newThread(Runnable r) {
            String taskname = ((IWorkTask)r).getTaskName()==null?
                "ZebraWorker_"+r.getClass().getName()+"_"+System.currentTimeMillis():
                "ZebraWorker_"+((IWorkTask)r).getTaskName()+"_"+System.currentTimeMillis();
            return new Thread(r,taskname);
        }
    }
}
