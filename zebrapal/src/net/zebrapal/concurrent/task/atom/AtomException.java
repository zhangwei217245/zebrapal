/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.task.atom;

/**
 *
 * @author X-Spirit
 */
public class AtomException extends Exception {
    
    private static final long serialVersionUID = 1077098293370113422L;

    private AtomPeriod atomPeriod;

    public AtomPeriod getAtomPeriod() {
        return atomPeriod;
    }

    public void setAtomPeriod(AtomPeriod atomPeriod) {
        this.atomPeriod = atomPeriod;
    }

    
    /**
     * Creates a new instance of <code>AtomException</code> without detail message.
     */
    public AtomException() {
        setAtomPeriod(AtomPeriod.UNKNOW);
    }


    /**
     * Constructs an instance of <code>AtomException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public AtomException(String msg) {
        super(msg);
        setAtomPeriod(AtomPeriod.UNKNOW);
    }
    public AtomException(String msg,Throwable cause) {
        super(msg,cause);
        setAtomPeriod(AtomPeriod.UNKNOW);
    }
    public AtomException(String msg, AtomPeriod ap){
        super(msg);
        setAtomPeriod(ap);
    }
    public AtomException(String msg, AtomPeriod ap,Throwable cause){
        super(msg,cause);
        setAtomPeriod(ap);
    }

}
