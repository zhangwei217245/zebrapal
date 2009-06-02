/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.persist;

import java.util.List;
import net.zebrapal.concurrent.task.AbstractWorkTask;

/**
 *
 * @author X-Spirit
 */
public interface ITaskPersistenceManager {
    public void createTaskInfo(AbstractWorkTask task);
    public void updateTaskInfo(AbstractWorkTask task);
    public List<AbstractWorkTask> queryTaskInfo();
    public void deleteTaskInfo(AbstractWorkTask task);
    public AbstractWorkTask readTaskInfo();
    
}
