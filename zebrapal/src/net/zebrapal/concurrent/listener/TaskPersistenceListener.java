/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.listener;

import java.util.Observable;
import java.util.Observer;
import net.zebrapal.concurrent.task.AbstractWorkTask;

/**
 *
 * @author x-spirit
 */
public class TaskPersistenceListener implements Observer{

    public void update(Observable o, Object arg) {
        AbstractWorkTask awt = (AbstractWorkTask)o;
        awt.getTaskContext().getTaskPersistManager().updateTaskInfo(awt);
    }

}
