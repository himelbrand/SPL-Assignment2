package bgu.spl.a2;

import com.sun.tools.classfile.Exceptions_attribute;

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
        waitingTask = new LinkedList<Task<?>>();
    }

    protected void addTask(Task<?> task){
        pool.myDequeTasksArray[id].addLast(task);
        pool.myVersionMonitor.inc();
    }

    int getId(){
        return id;
    }

    @Override
    public void run(){
        while(running){
            Task<?> currentTask = pool.myDequeTasksArray[id].pollFirst();
            System.out.println(Thread.currentThread().getName() + " try to work");
            if(currentTask != null){
                waitingTask.addFirst(currentTask);
           //     System.out.println(currentTask.taskName +" entered waiting");
                currentTask.handle(this);
            }else{
                boolean tryToSteal = pool.stealTasks(id);
                if(!tryToSteal) {
                    currentTask = pool.myDequeTasksArray[id].pollFirst();
                    if (currentTask != null) {
                        waitingTask.addFirst(currentTask);
                     //   System.out.println(currentTask.taskName + " entered waiting");
                        currentTask.handle(this);
                    } else {
                        try {
                            pool.myVersionMonitor.await(pool.myVersionMonitor.getVersion());

                        } catch (Exception e) {

                           running = false;

                        }
                    }
                }

            }
        }
    }

}
