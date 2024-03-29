package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;


public class VersionMonitorTest {
    private VersionMonitor versionMonitor;
    private Thread t1,t2;
    @Before
    public void setUp() throws Exception {
        System.out.println("Running: setUp");
        versionMonitor=createVersionMonitor();
        t1 = new Thread(()-> {
            try {
                versionMonitor.await(versionMonitor.getVersion());
            }catch (InterruptedException e){
                System.out.println("Thread interrupted");
            }catch (Exception e){
                fail("unexpected exception");
            }
        });
        t2 = new Thread(()->versionMonitor.inc());
    }
    @After
    public void tearDown() throws Exception {
        System.out.println("Running: tearDown");
        versionMonitor=null;
        t1.interrupt();
        t2.interrupt();
        assertNull(versionMonitor);

    }
    /**
     * This creates the object under test.
     *
     * @return a {@link VersionMonitor} instance.
     */
    private VersionMonitor createVersionMonitor(){
        return new VersionMonitor();
    }
    /**
     * Test Method for {@link VersionMonitor#getVersion()}
     */
    @Test
    public void testGetVersion(){
        System.out.println("Running: Test for getVersion()");
        assertEquals(0,versionMonitor.getVersion());
    }
    /**
     * Test Method for {@link VersionMonitor#inc()}
     */
    @Test
    public void testInc(){
        System.out.println("Running: Test for inc()");
        int start=versionMonitor.getVersion();
        versionMonitor.inc();
        versionMonitor.inc();
        versionMonitor.inc();
        assertEquals(start+3,versionMonitor.getVersion());
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)} waiting until interrupted
     *
     */
    @Test
    public void testAwaitInterrupted(){
        t1.start();
        t2.start();
        assertEquals(Thread.State.TERMINATED,t1.getState());
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)} waits because the version never increased
     *
     */
    @Test
    public void testAwaitNotInterrupted() throws Exception{
        t1.start();
        Timeout.seconds(5);
        assertEquals(Thread.State.WAITING,t1.getState());
    }

}