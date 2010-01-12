/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.concurrent.enumrations;

/**
 *
 * @author x-spirit
 */
public class TestState {
    void checkvoid(){
        throw new NullPointerException("asdfasdf");
    }
    void check(){
        checkvoid();
    }
    public static void main(String[] args){
        TestTaskState tts = new TestTaskState();
        tts.check();
        System.out.println(TaskState.CRASHED.ordinal());
    }
}
