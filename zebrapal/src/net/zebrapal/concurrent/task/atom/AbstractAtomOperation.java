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

    public long getTotalCount(){
        return this.totalCount;
    }
    /**
     * Mutator method for setting the totalCount of the operations.
     * It will be implemented by the users.
     * @return
     */
    protected abstract long calcTotalCount();

    public void init() throws AtomException {
        totalCount = calcTotalCount();
    }

    public void execute() throws AtomException {}

    public void close() {
        dataMap.clear();
    }

    public ConcurrentHashMap<?, ?> getDataMap() {
        return dataMap;
    }
    
    public void setDataMap(ConcurrentHashMap<?, ?> dataMap) {
        this.dataMap = dataMap;
    }

}
