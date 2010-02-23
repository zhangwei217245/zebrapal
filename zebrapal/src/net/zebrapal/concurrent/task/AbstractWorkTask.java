/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.zebrapal.concurrent.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;
import net.zebrapal.concurrent.TaskContext;
import net.zebrapal.concurrent.enumrations.TaskState;
import net.zebrapal.concurrent.enumrations.TaskType;
import net.zebrapal.concurrent.task.atom.AtomException;
import net.zebrapal.concurrent.task.atom.IAtomOperation;
import net.zebrapal.concurrent.task.disc.ITaskDetail;

/**
 *
 * @author X-Spirit
 */
public abstract class AbstractWorkTask extends Observable implements IWorkTask, Serializable {

    private long startTimeMills=System.currentTimeMillis();
    private long lastTimeMills;
    private long duration;
    private int lastCompleteCount=0;
    private double currentSpeed;
    private double averageSpeed;

    private double maxSpeed;
    private double minSpeed=-1.0;

    private TaskContext taskContext;
    private TaskState taskState = TaskState.CREATED;
    private String taskName;
    private String taskOwner;
    private TaskType tasktype;
    private ITaskDetail taskDetail;
    protected int completeCount;
    protected int failedCount;
    protected int totalCount;
    private IAtomOperation atomOperation;
    private Date createDate;

    protected int skipIndex;
    protected int skipCount;

    protected int curindex=0;
    protected int skipedCount=0;

    protected abstract void doExecute() throws Exception;

    @Override
    public void run() {
        try {
            getAtomOperation().init();
            if (getTasktype().equals(TaskType.QUANTIFIABLE) || getTasktype().equals(TaskType.PREDICTABLE)) {
                setCompleteCount(0);
                setFailedCount(0);
                if (getTasktype().equals(TaskType.PREDICTABLE)) {
                    setTotalCount(getAtomOperation().getTotalCount());
                }
            }
            if (getTaskState().equals(TaskState.CREATED)) {
                super.setChanged();
                notifyObservers();
            }
            if (getTaskState().equals(TaskState.RESTORED)) {
                getAtomOperation().skip(completeCount+failedCount);
                setTaskState(TaskState.RUNNING);
            }
            startTimeMills = System.currentTimeMillis();
            lastTimeMills = startTimeMills;
            //calcSpeed();
            doExecute();
            duration = System.currentTimeMillis()-startTimeMills;
            System.out.println("Task Executed Elasped: "+duration+" milliseconds.");
            setTaskState(TaskState.FINISHED);
        } catch (Exception e) {
            calcSpeed();
            setTaskState(TaskState.CRASHED);
            e.printStackTrace();
        } finally {
            try {
                getAtomOperation().close();
            } catch (AtomException ex) {
                ex.printStackTrace();
            }

            super.setChanged();
            notifyObservers();


        }

    }

    public void calcSpeed(){
        if(isRunningState()){
            long passedMills = System.currentTimeMillis()-lastTimeMills;
            int passedCount = completeCount-lastCompleteCount;
            currentSpeed = (double)passedCount*1000/passedMills;

            if(currentSpeed>maxSpeed){
                maxSpeed = currentSpeed;
            }

            if(minSpeed<=0){
                minSpeed = currentSpeed;
            }
            if(currentSpeed<minSpeed){
                minSpeed = currentSpeed;
            }

            duration = System.currentTimeMillis()-startTimeMills;
            if(duration==Double.NaN||duration<=0){
                duration = 1;
            }
            averageSpeed = (double)completeCount*1000/duration;
        }
        

    }

    protected void updateTaskProgressByInterval(AbstractWorkTask task) {
        if(task.isRunningState()){
            completeCount++;
            
        }
        if (completeCount % getTaskContext().getPersistInterval() == 0) {
            calcSpeed();
            

            super.setChanged();
            notifyObservers();

            lastTimeMills = System.currentTimeMillis();
            lastCompleteCount = completeCount;
            calcSpeed();
        }
    }

    /**
     * check the TaskState and do the operation accordingly.
     * CRASHED and FINISHED will not be set during this decision
     * @return
     */
    public boolean isRunningState() {
        //boolean b = true;
        if (taskState.equals(TaskState.SLEEP)) {
            return true;
        } else if (taskState.equals(TaskState.HIBERNATE)) {
            return false;
        } else if (taskState.equals(TaskState.CANCELLED)) {
            return false;
        } else if (taskState.equals(TaskState.CRASHED)) {
            return false;
        } else if (taskState.equals(TaskState.FINISHED)) {
            return false;
        }
        return true;
    }

    public boolean isAutoRestorable() {
        if (taskState.equals(TaskState.SLEEP)) {
            return true;
        } else if (taskState.equals(TaskState.HIBERNATE)) {
            return true;
        } else if (taskState.equals(TaskState.CANCELLED)) {
            return false;
        } else if (taskState.equals(TaskState.CRASHED)) {
            return false;
        } else if (taskState.equals(TaskState.FINISHED)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tasktype + "_" + getTaskName() + " @ " + taskState + " : " + completeCount + " Completed";
    }


    public TaskContext getTaskContext() {
        return this.taskContext;
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    /**
     * @return the taskState
     */
    public TaskState getTaskState() {
        return taskState;
    }

    /**
     * @param taskState the taskState to set
     */
    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the completeCount
     */
    public int getCompleteCount() {
        return completeCount;
    }

    /**
     * @param completeCount the completeCount to set
     */
    protected void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public IAtomOperation getAtomOperation() {
        return atomOperation;
    }

    public void setAtomOperation(IAtomOperation atomOperation) {
        this.atomOperation = atomOperation;
    }

    /**
     * @return the failedCount
     */
    public int getFailedCount() {
        return failedCount;
    }

    /**
     * @param failedCount the failedCount to set
     */
    protected void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }


    public long getStartTimeMills() {
        return startTimeMills;
    }

    public void setStartTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
    }

    public long getLastTimeMills() {
        return lastTimeMills;
    }

    public void setLastTimeMills(long lastTimeMills) {
        this.lastTimeMills = lastTimeMills;
    }

    

    /**
     * @return the taskOwner
     */
    public String getTaskOwner() {
        return taskOwner;
    }

    /**
     * @param taskOwner the taskOwner to set
     */
    protected void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    protected void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the tasktype
     */
    public TaskType getTasktype() {
        return tasktype;
    }

    /**
     * @param tasktype the tasktype to set
     */
    protected void setTasktype(TaskType tasktype) {
        this.tasktype = tasktype;
    }

    /**
     * the createDate
     * @return
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     *
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ITaskDetail getTaskDetail() {
        return this.taskDetail;
    }

    public void setTaskDetail(ITaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public void skipAndIgnoreCount(int startIdx, int skipCount) {
        this.skipIndex = startIdx;
        this.skipCount = skipCount;
    }

    

    /**
     * @return the lastCompleteCount
     */
    public int getLastCompleteCount() {
        return lastCompleteCount;
    }

    /**
     * @param lastCompleteCount the lastCompleteCount to set
     */
    public void setLastCompleteCount(int lastCompleteCount) {
        this.lastCompleteCount = lastCompleteCount;
    }

    /**
     * @return the currentSpeed
     */
    public double getCurrentSpeed() {
        
        currentSpeed = (currentSpeed==Double.NaN||currentSpeed<=0)?0:currentSpeed;
        return currentSpeed;
    }

    /**
     * @param currentSpeed the currentSpeed to set
     */
    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed>0?maxSpeed:0;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public long getDuration() {
        duration = duration<=0?1:duration;
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getAverageSpeed() {
        averageSpeed = (averageSpeed==Double.NaN||averageSpeed<=0)?0:averageSpeed;
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
    
}
