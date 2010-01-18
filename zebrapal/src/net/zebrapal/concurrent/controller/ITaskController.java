/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.controller;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.task.IWorkTask;

/**
 *
 * @author x-spirit
 */
public interface ITaskController {

    /**
     * Cancel a Task forcibly. In this situation, the task persistence manager will not persist this task.
     * If a task need to be persisted after the controller stops its work, please call the stop method.
     * @param task
     * @return
     */
    boolean cancelTask(IWorkTask task);

    /**
     * Clean up all the canceled work from both the workMap and the executor
     */
    void cleanUpCanceledTask();

    /**
     * Execute the task immediately.
     * This method is deprecated, you cannot use it because it won't return
     * a FutureTask represent for the WorkTask.
     * @param command
     * @deprecated
     */
    @Deprecated
    void execute(IWorkTask command);

    /**
     * Make the task falls asleep. The task won't be removed from the workerMap but will be persisted once.
     * For Nonquantifiable Task, it may not fall asleep...
     * @param task
     */
    void fallAsleep(IWorkTask task);

    ScheduledExecutorService getExecutor();

    TaskContext getTaskContext();

    /**
     * Hibernate the task. The task will be removed from the workerMap and will be persisted once.
     * @param task
     */
    void hibernate(IWorkTask task);

    /**
     * initialize the ScheduledThreadPoolExecutor with 20 of corePoolSize
     */
    void init();

    /**
     * initialize the ScheduledThreadPoolExecutor with the specific Properties or 20 if the properties is not available.
     * @param prop
     */
    void init(Properties prop);

    /**
     * initialize the ScheduledThreadPoolExecutor with the specific corePoolSize
     * @param corePoolSize
     */
    void init(int corePoolSize);

    RunnableScheduledFuture<?> schedule(IWorkTask command, long delay, TimeUnit unit);

    RunnableScheduledFuture<?> scheduleAtFixedRate(IWorkTask command, long initialDelay, long period, TimeUnit unit);

    RunnableScheduledFuture<?> scheduleWithFixedDelay(IWorkTask command, long initialDelay, long delay, TimeUnit unit);

    void setExecutor(ScheduledExecutorService executor);

    TaskController setTaskContext(TaskContext taskContext);

    void shutDown();

    List<Runnable> shutDownNow();

    /**
     * Submit the task immediately and the task will run at once.
     * this method returns a FutureTask.
     * @param command
     * @return
     */
    RunnableScheduledFuture<?> submit(IWorkTask command);

}
