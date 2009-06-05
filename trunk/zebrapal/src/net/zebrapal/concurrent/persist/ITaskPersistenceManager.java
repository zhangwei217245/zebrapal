/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.persist;

import java.util.List;
import net.zebrapal.concurrent.task.IWorkTask;

/**
 *
 * @author X-Spirit
 */
public interface ITaskPersistenceManager {
    public void createTaskInfo(IWorkTask task);
    public void updateTaskInfo(IWorkTask task);
    public List<IWorkTask> queryTaskInfo();
    public void deleteTaskInfo(IWorkTask task);
    public IWorkTask readTaskInfo();
}
