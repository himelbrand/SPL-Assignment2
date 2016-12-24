package bgu.spl.a2;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

     Processor[] myProcessorArray;
     ConcurrentLinkedDeque<Task<?>>[] myDequeTasksArray;
     Thread[] myThreadsArray;
      VersionMonitor myVersionmonitor = new VersionMonitor();

    private boolean fetchTasks(int processorId){
        int queueId = (processorId +1)%myDequeTasksArray.length;
        int queueVictemSize = myDequeTasksArray[queueId].size() / 2;
        for(int i=0; i<queueVictemSize;i++){
          //  synchronized (  myDequeTasksArray[queueId].pollLast())

        }

    }
    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
        myProcessorArray = new Processor[nthreads];
        myThreadsArray = new Thread[nthreads];
        myDequeTasksArray = new ConcurrentLinkedDeque[nthreads];

        for(int i=0;i<nthreads;i++){
            myProcessorArray[i] = new Processor(i,this);
            myThreadsArray[i] = new Thread(myProcessorArray[i]);
        }
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
        myDequeTasksArray[0].add(task);

        try {
            myVersionmonitor.sema.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myVersionmonitor.inc();
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        for(int i=0;i<myProcessorArray.length;i++){
            myThreadsArray[i].interrupt();
        }
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for(int i=0;i<myProcessorArray.length;i++){
            myThreadsArray[i].start();
        }
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

}
