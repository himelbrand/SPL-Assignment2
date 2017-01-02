package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

    private int currentVersion;
    private final  Object lock = new Object();

    /**
     *
     * @return The current version of version monitor
     */
    public int getVersion() {
        return this.currentVersion;
    }

    /**
     *This method increments the version ,
     * using synchronized on lock to avoid wrong count, so after increment notify all waiting on lock.
     */
    public void inc() {
        synchronized (lock) {
            currentVersion++;
            lock.notifyAll();
        }
    }

    /**
     * This method receives the current version and while the version does'nt change the thread using this method will wait,
     * in this method we use synchronized on lock and the wait is also on lock, so if started to wait the lock is released,
     * so other threads can wait or use {@link #inc()}.
     * when notified by {@link #inc()} continue to run, no longer in wait.
     * @param version
     * @throws InterruptedException
     */
    public void await(int version) throws InterruptedException {
        synchronized (lock){
            while(version == getVersion()){
              //  System.out.println(Thread.currentThread().getName() + " enter waiting");
                lock.wait();
              //  System.out.println(Thread.currentThread().getName() + " exit waiting");

            }
        }
    }
}
