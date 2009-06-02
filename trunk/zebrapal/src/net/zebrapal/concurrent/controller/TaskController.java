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
import net.zebrapal.concurrent.persist.ITaskPersistenceManager;

/**
 *
 * @author X-Spirit
 */
public class TaskController {
    
    private ScheduledExecutorService executor;
    private ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap;

    private ITaskPersistenceManager taskPersistManager;

    
    public void init(){
        ((ScheduledThreadPoolExecutor)executor).setThreadFactory(new TaskThreadFactory());
    }

    @Deprecated
    public void execute(IWorkTask command) {
        
        if (command == null)
            throw new NullPointerException();
        scheduleAtFixedRate(command,0, 0, TimeUnit.NANOSECONDS);
    }

    public Future<?> submit(IWorkTask command) {
        return scheduleAtFixedRate(command,0, 0, TimeUnit.NANOSECONDS);
    }
    
    public RunnableScheduledFuture<?> schedule(IWorkTask command,long delay,TimeUnit unit){
        return (RunnableScheduledFuture<?>) scheduleAtFixedRate(command, delay,0, unit);
    }
    
    public RunnableScheduledFuture<?> scheduleAtFixedRate(IWorkTask command,long initialDelay,long period,TimeUnit unit){
        RunnableScheduledFuture<?> ft = (RunnableScheduledFuture<?>)executor.scheduleAtFixedRate(command, initialDelay,period, unit);
        workerMap.put(command, ft);
        return ft;
    }
    public RunnableScheduledFuture<?> scheduleWithFixedDelay(IWorkTask command,long initialDelay,long delay,TimeUnit unit){
        return (RunnableScheduledFuture<?>) scheduleAtFixedRate(command, initialDelay,-delay, unit);
    }

    public void hibernate(IWorkTask task){
        
    }

    public void stop(IWorkTask task){
        
    }

    public void stopWithReset(IWorkTask task){
        
    }
    /**
     * Clean up all the canceled work from both the workMap and the executor
     */
    public void cleanUpCanceledTask(){
        Set<IWorkTask> keys = workerMap.keySet();
        for(IWorkTask task:keys){
            if(workerMap.get(task).isCancelled()){
                workerMap.remove(task);
            }
        }
        ((ScheduledThreadPoolExecutor)executor).purge();
    }

    /**
     * Cancel a Task forcibly.
     * @param task
     * @return
     */
    public boolean cancelTask(IWorkTask task){
        if(!workerMap.get(task).isCancelled()){

            remove(task);
        }
        return false;
    }


    /**
     * Remove the task from the Blocking Queue of SchedulerExecutor forcibly
     * @param command
     * @return
     */
    public boolean remove(IWorkTask command){

        workerMap.remove(command);
        return ((ScheduledThreadPoolExecutor)executor).remove(command);
    }
    public void shutDown(){
        executor.shutdown();
    }
    public List<Runnable> shutDownNow(){
        return executor.shutdownNow();
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

    public void setWorkerMap(ConcurrentMap workerMap) {
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
