package bgu.spl.a2.test;

import bgu.spl.a2.WorkStealingThreadPool;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by himelbrand on 12/25/16.
 */
public class testMatrix {
    public static void main(String args[]){
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int[][] array = new int[5][10];
        createMatrix(array);
        SumMatrix myTask = new SumMatrix(array);
        pool.start();
        myTask.taskName = "-- Task Main --";
        myTask.myDeferred.whenResolved(()->{
            int sum = 0;
            for (int i =0;  i<myTask.myDeferred.get().length;i++){
                sum += myTask.myDeferred.get()[i];
            }
            System.out.println(sum);
        });
        pool.submit(myTask);
        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            return;
        }
    }
    private static void createMatrix(int[][] array){
        int x = 0;
        for (int i =0 ; i<array.length;i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = x++;
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }

    }
}