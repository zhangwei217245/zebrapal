package net.zebrapal.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
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
    private Properties initProp;

    public TaskContext(){
        
    }

    public void selfInit(){
        try {
            if(initProp!=null){
                initialize(initProp);
            }else{
                throw new NullPointerException("The initProp is Null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }

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

        System.out.println("Initializing the Zebrapal Context...");
        //initialize the persistManager
        String persisitManagerClassName = props.getProperty(ZebrapalPropertyKeys.KEY_PERSISTENCE_MANAGER_CLASS);
        String str_persistInterval = props.getProperty(ZebrapalPropertyKeys.KEY_TASK_PERSIST_INTERVAL);
        setPersistInterval(Integer.parseInt(str_persistInterval));
        if(persisitManagerClassName!=null&&persisitManagerClassName.length()>0){
            Object persistManagerObj=Class.forName(persisitManagerClassName).newInstance();
            if(persistManagerObj instanceof ITaskPersistenceManager){
                ((ITaskPersistenceManager)persistManagerObj).init(props);
                taskPersistManager = (ITaskPersistenceManager)persistManagerObj;
            }
        }
        
        System.out.println("Initializing the Zebrapal Task Controller...");
        //initialize the TaskController
        taskController=TaskController.getInstance(this);
        taskController.init(props);

        System.out.println("Zebrapal Context was successfully initialized...");
    }
    /**
     * Destroy the TaskContext
     */
    public void destroy(){
        System.out.println("Zebrapal Context is Shuting down...");
        taskController.shutDownNow();
        System.out.println("Zebrapal Task Controller was closed...");
        persistWorkerMap();
        System.out.println("Persist Zebrapal Tasks...");
        taskPersistManager.close();
        System.out.println("Zebrapal Context was successfully closed...");
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
        if(persistInterval>0&&persistInterval<=1009){
            if(NumberCalculate.isPrimeInteger(persistInterval)){
                this.persistInterval = persistInterval;
                System.out.println("####Persist Interval is : "+this.persistInterval);
            }else{
                System.out.println("An non-prime number was given, and persist interval will be set to 1009 as default");
                this.persistInterval = 1009;
            }
        }else{
            System.out.println("The given number is over range, and persist interval will be set to 1009 as default");
            this.persistInterval = 1009;
        }
    }

    public Properties getInitProp() {
        return initProp;
    }

    public void setInitProp(Properties initProp) {
        this.initProp = initProp;
    }

    public Entry<IWorkTask,RunnableScheduledFuture> searchTaskByName(String keyWord){
        for(Entry<IWorkTask,RunnableScheduledFuture> entry:workerMap.entrySet()){
            IWorkTask task = entry.getKey();
            if(task!=null&&task.getTaskName().contains(keyWord)){
                return entry;
            }
        }
        return null;
    }

}
