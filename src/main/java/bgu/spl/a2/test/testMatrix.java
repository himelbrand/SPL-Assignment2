package bgu.spl.a2.test;

import bgu.spl.a2.WorkStealingThreadPool;

import java.util.Random;

/**
 * Created by himelbrand on 12/25/16.
 */
public class testMatrix {
    public static void Main() throws Exception{
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int[][] array = new int[5][10];
        createMatrix(array);
        SumMatrix myTask = new SumMatrix(array);
        pool.start();
        pool.submit(myTask);
//some stuff
        pool.shutdown(); //stopping all the threads
    }
    private static void createMatrix(int[][] array){
        Random random=new Random();
        for (int i =0 ; i<array.length;i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = random.nextInt(100);
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }

    }
}
