/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.zebrapal.util;

/**
 *
 * @author X-Spirit
 */
public class NumberCalculate {
    public static boolean isPrimeInteger(int num){
        if(num>0&&num<4){
            if(num==2||num==3){
                return true;
            }else{
                return false;
            }
        }else if (num>=4){
            for(int i=2;i<Math.sqrt(num);i++){
                if(num%i==0){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }
}
