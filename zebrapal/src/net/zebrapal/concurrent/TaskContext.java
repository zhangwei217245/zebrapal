package net.zebrapal.concurrent;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RunnableScheduledFuture;
import net.zebrapal.concurrent.controller.TaskController;
import net.zebrapal.concurrent.persist.ITaskPersistenceManager;
import net.zebrapal.concurrent.task.IWorkTask;
import net.zebrapal.util.NumberCalculate;

/**
 *
 * @author X-Spirit
 */
public class TaskContext {
    

    private ConcurrentMap<IWorkTask,RunnableScheduledFuture> workerMap;
    private TaskController taskController;
    private ITaskPersistenceManager taskPersistManager;
    private int persistInterval;

    public TaskContext(){
        
    }

    public void initialize(){

    }
    /**
     * initialize the TaskContext including the persistManager and TaskController
     * @param props
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public void initialize(Properties props)throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        workerMap = new ConcurrentHashMap<IWorkTask, RunnableScheduledFuture>();

        //initialize the persistManager
        String perisitManagerClassName = props.getProperty(ZebrapalPropertyKeys.KEY_PERSISTENCE_MANAGER_CLASS);
        String str_persistInterval = props.getProperty(ZebrapalPropertyKeys.KEY_TASK_PERSIST_INTERVAL);
        setPersistInterval(Integer.parseInt(str_persistInterval));
        Object persistManagerObj=Class.forName(perisitManagerClassName).newInstance();
        if(persistManagerObj instanceof ITaskPersistenceManager){
            ((ITaskPersistenceManager)persistManagerObj).init(props);
            taskPersistManager = (ITaskPersistenceManager)persistManagerObj;
        }
        
        //initialize the TaskController
        taskController=TaskController.getInstance(this);
        taskController.init(props);
    }
    /**
     * Destroy the TaskContext
     */
    public void destroy(){
        taskController.shutDownNow();
        persistWorkerMap();
        taskPersistManager.close();
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

    public int getPersistInterval() {
        return persistInterval;
    }

    public void setPersistInterval(int persistInterval) {
        if(persistInterval<=0||persistInterval>1009){
            if(NumberCalculate.isPrimeInteger(persistInterval)){
                this.persistInterval = persistInterval;
            }else{
                System.out.println("An non-prime number was given, and persist interval will be set to 1009 as default");
                this.persistInterval = 1009;
            }
        }else{
            System.out.println("The given number is over range, and persist interval will be set to 1009 as default");
            this.persistInterval = 1009;
        }
    }

}
