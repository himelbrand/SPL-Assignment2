package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by himelbrand on 12/11/16.
 */
public class VersionMonitorTest {
    private VersionMonitor versionMonitor;
    @Before
    public void setUp() throws Exception {
        System.out.println("Running: setUp");
        versionMonitor=createVersionMonitor();
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
        versionMonitor.inc();
        assertEquals(1,versionMonitor.getVersion());
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)}
     *
     */
    @Test
    public void testAwait(){
        try{
            versionMonitor.await(1);
            assertNotEquals(1,versionMonitor.getVersion());
        }catch (Exception e){
            fail("an un expected exception as been thrown");
        }
    }

}