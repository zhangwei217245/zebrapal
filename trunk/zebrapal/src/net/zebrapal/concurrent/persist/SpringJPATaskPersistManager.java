/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.persist;

import java.util.List;
import java.util.Properties;
import net.zebrapal.concurrent.task.IWorkTask;

/**
 *
 * @author X-Spirit
 */
public class SpringJPATaskPersistManager implements ITaskPersistenceManager{

    public void init(Properties prop) {
        System.out.println("JPATaskPersistManager is initializing....");
    }

    public void createTaskInfo(IWorkTask task) {
        System.out.println("Create Task Infomation in Database");
    }

    public void updateTaskInfo(IWorkTask task) {
        System.out.println("update task infomation in database");
    }

    public List<IWorkTask> queryTaskInfo() {
        System.out.println("getting all persisted task infomation");
        return null;
    }

    public void deleteTaskInfo(IWorkTask task) {
        System.out.println("delete Task infomation in database ");
    }

    public IWorkTask readTaskInfo() {
        System.out.println("get task infomation in database");
        return null;
    }

    public void close() {
        System.out.println("close this persist manager");
    }

}
