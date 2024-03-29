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
            int middle = (array.length) / 2;
            int[] array1 = Arrays.copyOfRange(array, 0, middle);
            int[] array2 = Arrays.copyOfRange(array, middle, array.length);
            MergeSort spwnTask1 = new MergeSort(array1);
            MergeSort spwnTask2 = new MergeSort(array2);
            spawn(spwnTask1, spwnTask2);
            ArrayList<MergeSort> myCollection = new ArrayList<>(2);
            myCollection.add(spwnTask1);
            myCollection.add(spwnTask2);
            whenResolved(myCollection, () -> {
                int[] arr =mergeArrays(myCollection.get(0).getResult().get(), myCollection.get(1).getResult().get());

                    complete(arr);
            });
        }else{
            complete(array);
        }
    }
    //implements the merging of smaller sorted arrays, used in merge sort
    private int[] mergeArrays(int[] array1, int[] array2) {
        int[] newArray = new int[array1.length + array2.length];
        int i=0,k=0,j =0;
        while (i < array1.length && j < array2.length) {
            if (array1[i] <= array2[j]) {
                newArray[k] = array1[i];
                i++;
            } else {
                newArray[k] = array2[j];
                j++;
            }
            k++;
        }
        while (j < array2.length) {
            newArray[k] = array2[j];
            k++;
            j++;
        }
        while (i < array1.length) {
            newArray[k] = array1[i];
            k++;
            i++;
        }
        return newArray;
    }


    public static void main(String[] args) throws InterruptedException {
         final WorkStealingThreadPool pool = new WorkStealingThreadPool(10);
        int n = 1000000; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();
        MergeSort task = new MergeSort(array);
        System.out.println(Arrays.toString(array));
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            System.out.println(Arrays.toString(task.getResult().get()));
            l.countDown();
        });
        l.await();
        pool.shutdown();
    }

}
