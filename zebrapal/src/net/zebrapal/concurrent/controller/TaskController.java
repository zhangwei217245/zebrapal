package net.zebrapal.concurrent.controller;

import net.zebrapal.concurrent.task.IWorkTask;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.ZebrapalPropertyKeys;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.AbstractWorkTask;

/**
 *
 * @author X-Spirit
 */
public class TaskController implements ITaskController {
    
    private TaskContext taskContext;

    private ScheduledExecutorService executor;

    private static TaskController taskController = new TaskController();
    

    private TaskController(){
        
    }



    public static TaskController getInstance(TaskContext context){
        return taskController.setTaskContext(context);
    }

    /**
     * initialize the ScheduledThreadPoolExecutor with 20 of corePoolSize
     */
    public void init(){
        init(20);
    }

    /**
     * initialize the ScheduledThreadPoolExecutor with the specific Properties or 20 if the properties is not available.
     * @param prop
     */
    public void init(Properties prop){
        int corePoolSize = 20;
        if(prop!=null&&prop.containsKey(ZebrapalPropertyKeys.KEY_CORE_POOL_SIZE)){
            String str_corePoolSize=prop.getProperty(ZebrapalPropertyKeys.KEY_CORE_POOL_SIZE);

            if(str_corePoolSize!=null&&str_corePoolSize.length()>0){
                corePoolSize = Integer.parseInt(str_corePoolSize);
            }
        }
        init(corePoolSize);
    }

    /**
     * initialize the ScheduledThreadPoolExecutor with the specific corePoolSize
     * @param corePoolSize
     */
    public void init(int corePoolSize){
        executor=new ScheduledThreadPoolExecutor(corePoolSize);
        ((ScheduledThreadPoolExecutor)executor).setThreadFactory(new TaskThreadFactory());
        List<IWorkTask> worklist = taskContext.getTaskPersistManager().queryTaskInfo();
        if(worklist!=null){
            for(IWorkTask task:worklist){
                this.submit(task);
            }
        }
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
        schedule(command, 0, TimeUnit.NANOSECONDS);
    }

    /**
     * Submit the task immediately and the task will run at once.
     * this method returns a FutureTask.
     * @param command
     * @return
     */
    public RunnableScheduledFuture<?> submit(IWorkTask command) {
        if (command == null)
            throw new NullPointerException();
        return schedule(command, 0, TimeUnit.NANOSECONDS);
    }
    
    public RunnableScheduledFuture<?> schedule(IWorkTask command,long delay,TimeUnit unit){
        ((AbstractWorkTask)command).setTaskContext(this.taskContext);
        RunnableScheduledFuture<?> ft = (RunnableScheduledFuture<?>) executor.schedule(command, delay, unit);
        taskContext.getWorkerMap().put(command, ft);
        taskContext.getTaskPersistManager().createTaskInfo(command);
        return ft;
    }
    
    public synchronized RunnableScheduledFuture<?> scheduleAtFixedRate(IWorkTask command,long initialDelay,long period,TimeUnit unit){
        ((AbstractWorkTask)command).setTaskContext(this.taskContext);
        RunnableScheduledFuture<?> ft = (RunnableScheduledFuture<?>)executor.scheduleAtFixedRate(command, initialDelay,period, unit);
        taskContext.getWorkerMap().put(command, ft);
        taskContext.getTaskPersistManager().createTaskInfo(command);
        return ft;
    }
    
    public RunnableScheduledFuture<?> scheduleWithFixedDelay(IWorkTask command,long initialDelay,long delay,TimeUnit unit){
        ((AbstractWorkTask)command).setTaskContext(this.taskContext);
        RunnableScheduledFuture<?> ft = (RunnableScheduledFuture<?>) executor.scheduleWithFixedDelay(command, initialDelay,-delay, unit);
        taskContext.getWorkerMap().put(command, ft);
        taskContext.getTaskPersistManager().createTaskInfo(command);
        return ft;
    }

    /**
     * Make the task falls asleep. The task won't be removed from the workerMap but will be persisted once.
     * For Nonquantifiable Task, it may not fall asleep...
     * @param task
     */
    public synchronized void fallAsleep(IWorkTask task){
        try {
            ((AbstractWorkTask)task).setTaskState(TaskState.SLEEP);
            taskContext.getTaskPersistManager().updateTaskInfo(task);
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
            taskContext.getTaskPersistManager().updateTaskInfo(task);
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
            if(!taskContext.getWorkerMap().get(task).isCancelled()){
                remove(task);
            }else{
                ((AbstractWorkTask)task).setTaskState(TaskState.CANCELLED);
                taskContext.getWorkerMap().get(task).cancel(true);
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
            taskContext.getWorkerMap().get(command).cancel(true);
        }
        taskContext.getWorkerMap().remove(command);
        return ((ScheduledThreadPoolExecutor)executor).remove(command);
    }

    /**
     * Clean up all the canceled work from both the workMap and the executor
     */
    public synchronized void cleanUpCanceledTask(){
        Set<IWorkTask> keys = taskContext.getWorkerMap().keySet();
        for(IWorkTask task:keys){
            if(taskContext.getWorkerMap().get(task).isCancelled()){
                taskContext.getWorkerMap().remove(task);
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

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public TaskController setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
        return this;
    }

    private class TaskThreadFactory implements ThreadFactory{
        public Thread newThread(Runnable r) {
            String taskname = 
                "ZebraWorker_"+r.getClass().getName()+"_"+System.currentTimeMillis();
                
            return new Thread(r,taskname);
        }
    }
}
