/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.enumrations;

/**
 *
 * @author X-Spirit
 */
public enum TaskState {
        /**
         * 任务刚被创建，尚未启动
         * the task is created and the run() method has not been called.
         */
        CREATED,
        /**
         * 任务已被激活，处于运行状态
         * The task is actived and running...
         */
        RUNNING,

        /**
         * 任务被中断，进入等待状态,任务的状态由内存保持
         * The task is interrupted and is waiting
         */
        SLEEP,

        /**
         * 任务被挂起，不再运行，任务的状态被持久化
         * The task throws an exception and stop running.
         */
        HIBERNATE,
        /**
         * 线程刚刚被恢复
         * The task has just been restored.
         */
        RESTORED,

        /**
         * 任务完成
         * The task is over
         */
        FINISHED,

        /**
         * 当前任务处于定时暂停中
         * The task is waiting until the indicated time expires
         */
        //TIMED_WAITING,

        /**
         * 任务发生异常，运行崩溃
         * The task throws an exception and the thread is crashed
         */
        CRASHED;
}
