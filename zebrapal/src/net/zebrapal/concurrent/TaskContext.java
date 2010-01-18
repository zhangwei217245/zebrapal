package net.zebrapal.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public TaskContext(String propFileName){
        try {
            initialize(propFileName);
        } catch (Exception e) {
            System.out.println("TaskContext was failed to be initialized due to the error below: ");
            e.printStackTrace();
        }
        
    }

    private void initialize(String propFileName) throws Exception{
        File propFile = new File(propFileName);

        Properties props = new Properties();

        InputStream in = null;

        try {
            if(propFile.exists()){
                System.out.println("Loading properties file: "+propFileName);
                try {
                    in = new BufferedInputStream(new FileInputStream(propFileName));
                    props.load(in);
                } catch (IOException ioe) {
                    throw new ContextLoadException("Property File "+propFileName+
                    " cannot be read, please check your property file", ioe);
                }

            }else if(propFileName!=null){
                System.out.println("Loading properties file: "+propFileName);
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
                if(in == null) {
                    throw new ContextLoadException("Properties file: '"
                        + propFileName + "' could not be found.");
                }

                in = new BufferedInputStream(in);
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    throw  new ContextLoadException("Properties file: '"
                            + propFileName + "' could not be read.", ioe);
                }
            }else {
                System.out.println("default resource file in Zebrapal package: 'zebrapal.properties'");

                in = getClass().getClassLoader().getResourceAsStream(
                        "zebrapal.properties");

                if (in == null) {
                    in = getClass().getClassLoader().getResourceAsStream(
                            "/zebrapal.properties");
                }
                if (in == null) {
                    in = getClass().getClassLoader().getResourceAsStream(
                            "net/zebrapal/zebrapal.properties");
                }
                if (in == null) {
                    throw new ContextLoadException("Default zebrapal.properties not found in class path");
                }
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    throw new ContextLoadException("Resource properties file: 'net/zebrapal/zebrapal.properties' "
                                    + "could not be read from the classpath.", ioe);
                }
            }
            initialize(props);
        } finally {
            if(in != null) {
                try { in.close(); } catch(IOException ignore) { /* ignore */ }
            }
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
