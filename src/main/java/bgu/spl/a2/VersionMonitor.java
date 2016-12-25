package bgu.spl.a2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

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
    private final  Object lock1 = new Object();
    public int getVersion() {
        //TODO: replace method body with real implementation
        return this.currentVersion;
       // throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    public void inc() {
        synchronized (lock1) {
            currentVersion++;
            lock1.notifyAll();
        }
    }

    public void await(int version) throws InterruptedException {

        synchronized (lock1){
            //System.out.println("Processor " + Thread.currentThread().getName() +" took the key");
            while(version == getVersion())
            {
               // System.out.println("Processor " + Thread.currentThread().getName() + "version is " +version +  "   and general is "+ getVersion());
              //  System.out.println("Processor " + Thread.currentThread().getName() +" release the key");
                System.out.println( Thread.currentThread().getName() +" is enter waiting.");
                lock1.wait();
                System.out.println( Thread.currentThread().getName() +" is exit waiting.");
            }
            //System.out.println("Processor " + Thread.currentThread().getName() +" release the key2");

        }
        //TODO: replace method body with real implementation
     //   throw new UnsupportedOperationException("Not Implemented Yet.");
    }
}
