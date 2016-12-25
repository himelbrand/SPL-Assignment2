/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected void start() {

        if (array.length > 1) {
            int middle = (array.length - 1) / 2;
            // Below step sorts the left side of the array
            int[] array1 = Arrays.copyOfRange(array, 0, middle);
            int[] array2 = Arrays.copyOfRange(array, middle + 1, array.length - 1);


            MergeSort spwnTask1 = new MergeSort(array1);
            MergeSort spwnTask2 = new MergeSort(array2);
            spawn(spwnTask1, spwnTask2);
            ArrayList<MergeSort> myCollection = new ArrayList<MergeSort>(2);
            myCollection.add(spwnTask1);
            myCollection.add(spwnTask2);

            whenResolved(myCollection, () -> {
                int[] arr =mergeArrays(myCollection.get(0).getResult().get(), myCollection.get(0).getResult().get());
                for (int n:arr) {
                    System.out.print(n);
                }

                    complete(arr);

            });

        }


    }

    private int[] mergeArrays(int[] array1, int[] array2) {

        int[] newArray = new int[array1.length + array2.length];
        int[] tempMergArr = new int[array1.length + array2.length];
        int lowerIndex = 0;
        int higherIndex = newArray.length - 1;
        int middle = array1.length - 1;


        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = newArray[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                newArray[k] = tempMergArr[i];
                i++;
            } else {
                newArray[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            newArray[k] = tempMergArr[i];
            k++;
            i++;
        }

        return newArray;
    }


    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int n = 10; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();

        MergeSort task = new MergeSort(array);
        task.taskName = "Task_0";
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
            System.out.println(Arrays.toString(task.getResult().get()));
            l.countDown();
        });

        l.await();
        pool.shutdown();
    }

}
