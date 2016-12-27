package bgu.spl.a2.test;

import bgu.spl.a2.WorkStealingThreadPool;

import java.util.Random;
import java.util.StringJoiner;


public class testMatrix {
    public static void main(String args[]) throws InterruptedException{
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int[][] array = new int[5][10];
        createMatrix(array);
        SumMatrix myTask = new SumMatrix(array);
        pool.start();
        myTask.taskName = "-- Task Main --";
        myTask.getResult().whenResolved(()->{
            int sum = 0;
            for (int i =0;  i<myTask.getResult().get().length;i++){
                sum += myTask.getResult().get()[i];
            }
            System.out.println(sum);
        });
        pool.submit(myTask);
        pool.shutdown();

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
