package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Created by himelbrand on 12/11/16.
 */
public class VersionMonitorTest {
    private VersionMonitor versionMonitor;
    private Thread t1,t2;

    @Before
    public void setUp() throws Exception {
        System.out.println("Running: setUp");
        versionMonitor=createVersionMonitor();
        t1 = new Thread(()-> {
            int startVersion=versionMonitor.getVersion();
            try {
                versionMonitor.await(versionMonitor.getVersion());
            }catch (InterruptedException e){
                assertNotEquals(startVersion,versionMonitor.getVersion());
            }
        });
        t2 = new Thread(()->versionMonitor.inc());
    }
    @After
    public void tearDown() throws Exception {
        System.out.println("Running: tearDown");
        versionMonitor=null;
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
     * Test Method for {@link VersionMonitor#await(int)}
     *
     */
    @Test
    public void testAwaitInterrupted(){
        t1.start();
        if(t1.getState()==Thread.State.WAITING){
            t2.start();
        }else{
            fail("not waiting , but version did'nt change");
        }
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)}
     *
     */
    @Test
    public void testAwaitWaitForEver() throws Exception{
        t1.start();
        Thread.sleep(10000);//limited to 10 seconds and not forever
        assertEquals(Thread.State.WAITING,t1.getState());
        t1.interrupt();
    }

}