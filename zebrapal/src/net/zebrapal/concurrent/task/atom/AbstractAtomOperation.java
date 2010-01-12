/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task.atom;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author X-Spirit
 */
public abstract class AbstractAtomOperation implements IAtomOperation{
    
    protected ConcurrentHashMap<?, ?> dataMap;

    protected long totalCount;

    @Override
    public long getTotalCount(){
        return this.totalCount;
    }
    /**
     * Mutator method for setting the totalCount of the operations.
     * It will be implemented by the users.
     * @return
     */
    protected abstract long calcTotalCount();

    protected abstract void initResource(ConcurrentHashMap<?,?> dataMap);

    protected abstract void executeOnce() throws AtomException;

    protected abstract void closeResource();

    public void init() throws AtomException {
        try {
            initResource(dataMap);
            totalCount = calcTotalCount();
        } catch (Exception e) {
            throw new AtomException("AtomException:", AtomPeriod.INIT,e);
        }
        
    }

    public void execute() throws AtomException{
        try {
            executeOnce();
        } catch (Exception e) {
            throw new AtomException("AtomException When Execute:", AtomPeriod.EXECUTE, e);
        }
    };

    

    public void close() throws AtomException{
        try {
            closeResource();
            dataMap.clear();
        } catch (Exception e) {
            throw new AtomException("AtomException When Close:", AtomPeriod.RELEASE, e);
        }
        
    }

    public ConcurrentHashMap<?, ?> getDataMap() {
        return dataMap;
    }
    
    public void setDataMap(ConcurrentHashMap<?, ?> dataMap) {
        this.dataMap = dataMap;
    }

}
