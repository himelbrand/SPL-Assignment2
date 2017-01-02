package bgu.spl.a2;



import java.util.LinkedList;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
    LinkedList<Task<?>> waitingTask;
    private boolean running = true;


    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
        waitingTask = new LinkedList<>();
    }

    void addTask(Task<?> task){
        pool.myDequeTasksArray[id].addLast(task);
        pool.myVersionMonitor.inc();
    }

    int getId(){
        return id;
    }

    /**
     * This method is the Processor work scheme,
     * work if can't then try to {@link WorkStealingThreadPool#stealTasks(int)} if stole then work.
     * didn't steal try to work again in case a task was submitted to the queue.
     * no work? then {@link VersionMonitor#await(int)}
     */
    public void run(){
        while(running){
            //pulls a new task from queue
            Task<?> currentTask = pool.myDequeTasksArray[id].pollFirst();
            if(currentTask != null){//if the queue was'nt empty handles the task
                waitingTask.addFirst(currentTask);
                currentTask.handle(this);
            }else{//else try to steal from other queues
                boolean stole = pool.stealTasks(id);
                if(!stole) {
                    currentTask = pool.myDequeTasksArray[id].pollFirst();
                    if (currentTask != null) {//didn't steal but have work in queue , handle task
                        waitingTask.addFirst(currentTask);
                        currentTask.handle(this);
                    } else {
                        try {//didn't steal and have no work in queue , await
                            pool.myVersionMonitor.await(pool.myVersionMonitor.getVersion());
                        } catch (InterruptedException e) {
                            running = false;
                        }
                    }
                }

            }
        }
    }

}
