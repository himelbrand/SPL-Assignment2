package bgu.spl.a2;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

    private Processor[] myProcessorArray;
    ConcurrentLinkedDeque<Task<?>>[] myDequeTasksArray;
    private Thread[] myThreadsArray;
    VersionMonitor myVersionMonitor = new VersionMonitor();

    boolean stealTasks(int processorId) {
        boolean myCheckIfSteal = false;
        int queueIdVictim = (processorId + 1) % myDequeTasksArray.length;
        int queueVictimSize;
        while (queueIdVictim != processorId) {
            queueVictimSize = myDequeTasksArray[queueIdVictim].size() / 2;
            for (int i = 0; i < queueVictimSize; i++) {
                Task<?> myTask = myDequeTasksArray[queueIdVictim].pollLast();
                if (myTask != null) {
                    myDequeTasksArray[processorId].addFirst(myTask);
                    myCheckIfSteal = true;
                } else {
                    break;
                }
            }
            if (myCheckIfSteal)
                break;
            queueIdVictim = (queueIdVictim + 1) % myDequeTasksArray.length;
        }
        return myCheckIfSteal;
    }

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     *                 thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
        myProcessorArray = new Processor[nthreads];
        myThreadsArray = new Thread[nthreads];
        myDequeTasksArray = new ConcurrentLinkedDeque[nthreads];
        for (int i = 0; i < nthreads; i++) {
            myProcessorArray[i] = new Processor(i, this);
            myThreadsArray[i] = new Thread(myProcessorArray[i]);
            myDequeTasksArray[i] = new ConcurrentLinkedDeque<>();
        }
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
        Random myRandom = new Random();
        int myRandomNumber = myRandom.nextInt(myProcessorArray.length);
        myDequeTasksArray[myRandomNumber].add(task);
        myVersionMonitor.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException          if the thread that shut down the threads is
     *                                       interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     *                                       shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        for (int i = 0; i < myProcessorArray.length; i++) {
            if (Thread.currentThread() == myThreadsArray[i]) {
                throw new UnsupportedOperationException();
            }
            myThreadsArray[i].interrupt();
        }

    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (int i = 0; i < myProcessorArray.length; i++) {
            myThreadsArray[i].start();
        }
    }

}
