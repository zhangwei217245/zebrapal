/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task.atom;

import java.util.concurrent.ConcurrentMap;
import net.zebrapal.concurrent.enumrations.TaskState;

/**
 *
 * @author X-Spirit
 */
public abstract class AbstractAtomOperation implements IAtomOperation{
    
    protected transient ConcurrentMap dataMap;

    protected int totalCount;

    @Override
    public int getTotalCount(){
        return this.totalCount;
    }
    /**
     * Mutator method for setting the totalCount of the operations.
     * It will be implemented by the users.
     * @return
     */
    protected abstract int calcTotalCount();

    protected abstract void initResource(ConcurrentMap dataMap) throws Exception;

    protected abstract TaskState executeOnce() throws Exception;

    protected abstract void closeResource();

    public void init() throws AtomException {
        try {
            initResource(dataMap);
            this.totalCount = calcTotalCount();
        } catch (Exception e) {
            throw new AtomException("AtomException:", AtomPeriod.INIT,e);
        }
        
    }

    public TaskState execute() throws AtomException{
        TaskState state = TaskState.RUNNING;
        try {
            state = executeOnce();
        } catch (Exception e) {
            throw new AtomException("AtomException When Execute:", AtomPeriod.EXECUTE, e);
        }
        return state;
    };

    

    public void close() throws AtomException{
        try {
            closeResource();
            if(dataMap!=null){
                dataMap.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AtomException("AtomException When Close:", AtomPeriod.RELEASE, e);
        }
        
    }

    public ConcurrentMap getDataMap() {
        return dataMap;
    }
    
    public void setDataMap(ConcurrentMap dataMap) {
        this.dataMap = dataMap;
    }

}
