package net.zebrapal.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RunnableScheduledFuture;
import net.zebrapal.concurrent.controller.TaskController;
import net.zebrapal.concurrent.task.IWorkTask;

/**
 *
 * @author X-Spirit
 */
public class TaskContext {
    private ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap;
    private TaskController taskController;
    

}
