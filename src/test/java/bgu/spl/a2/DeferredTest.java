package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Shahar on 11/12/2016.
 */
public class DeferredTest {


    private  Deferred<Integer> deferredObject;
    private Integer valueToResolve;
    private Runnable callBack;
    private Integer testCallBackCounter;

    @Before
    public void setUp() throws Exception {
        this.deferredObject = createDeferred();
        this.valueToResolve = 100;
        this.testCallBackCounter = 1;

        this.callBack = () ->{
            this.testCallBackCounter++;
        };

    }

    /**
     * This creates the object under test.
     * @return a {@link Deferred} instance.
     */
    private Deferred<Integer> createDeferred() {

        return new Deferred<Integer>();
    }



    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     */

    @Test
    public void testGet() throws Exception {
        deferredObject.resolve(valueToResolve);
        assertEquals(deferredObject.get(),valueToResolve);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     */
    @Test public void testGetException() {
        try {
            deferredObject.get();
            fail("Exception expected! for testGetException");
        }catch(Exception e){
            //test pass
        }

    }


    /**
     * Test method for {@link bgu.spl.a2.Deferred#isResolved()}:
     **/
    @Test
    public void testIsResolved() throws Exception {
        assertFalse(deferredObject.isResolved());
        deferredObject.resolve(valueToResolve);
        assertTrue(deferredObject.isResolved());
    }


    /**
     * Test method for {@link bgu.spl.a2.Deferred#resolve(Object)}:
     */
    @Test
    public void testResolve() throws Exception {
        deferredObject.resolve(valueToResolve);
        assertEquals(deferredObject.get(),valueToResolve);
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter.intValue(),2);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#resolve(Object)}:
     */
    @Test
    public void testResolveException()  {
        deferredObject.resolve(valueToResolve);
            try{
                deferredObject.resolve(valueToResolve);
                fail("Exception expected! for testResolveException");
            }catch(Exception e){
                //test pass
            }
        }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#whenResolved(Runnable)}:
     */
    @Test
    public void testWhenResolved() throws Exception {
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter.intValue(),1);
        deferredObject.resolve(valueToResolve);
        assertEquals(testCallBackCounter.intValue(),2);
        assertEquals(testCallBackCounter.intValue(),2);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#whenResolved(Runnable)}:
     */
    @Test
    public void testOnResolvedObjectWhenResolved() throws Exception {
        deferredObject.resolve(valueToResolve);
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter.intValue(),2);
    }

    @After
    public void tearDown() throws Exception {
        deferredObject = null;
        callBack = null;
        valueToResolve = null;
        testCallBackCounter = null;
    }

}