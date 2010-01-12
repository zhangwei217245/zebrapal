package net.zebrapal.concurrent.task.atom;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * You need to implement an atom operation, and define some variable and initialize them in init() method
 * @author X-Spirit
 */
public interface IAtomOperation extends Serializable{
    /**
     * initialize the parameters
     * @throws net.zebrapal.concurrent.task.atom.AtomException
     */
    public void init() throws AtomException;
    /**
     * skip some operations after the task is restored..
     * @param skipCount
     */
    public void skip(long skipCount) throws AtomException;
    /**
     * execute the atom operation.
     * @return
     * @throws net.zebrapal.concurrent.task.atom.AtomException
     */
    public void execute() throws AtomException;
    /**
     * release the parameters and the related resources..
     * @throws net.zebrapal.concurrent.task.atom.AtomException
     */
    public void close() throws AtomException;
    /**
     * Get the total count of the operations
     * @return
     */
    public long getTotalCount();

    /**
     * 
     * @param dataMap
     */
    public void setDataMap(ConcurrentHashMap<?,?> dataMap);
}
