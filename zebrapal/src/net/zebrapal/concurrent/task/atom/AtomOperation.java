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
public class AtomOperation extends AbstractAtomOperation{

    @Override
    protected int calcTotalCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void initResource(ConcurrentMap dataMap) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected TaskState executeOnce() throws AtomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void closeResource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void skip(int skipCount) throws AtomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
