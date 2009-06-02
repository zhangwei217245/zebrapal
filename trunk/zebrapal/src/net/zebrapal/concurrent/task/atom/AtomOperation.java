/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task.atom;

/**
 *
 * @author X-Spirit
 */
public class AtomOperation extends AbstractAtomOperation{

    @Override
    public void execute() throws AtomException {
        super.execute();
    }

    @Override
    public void init() throws AtomException {
        super.init();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public long getTotalCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void skip(long skipCount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
