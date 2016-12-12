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
    private int testCallBackCounter;

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
     * This creates the object under test.  Note that we must create a specific implementation (StackImpl)
     * of the interface under test. The rest of the test class only refers to the interface under test.
     *
     * @return a {@link Deferred} instance.
     */
    private Deferred<Integer> createDeferred() {

        return new Deferred<Integer>();
    }






    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * after new {@link bgu.spl.a2.Deferred} is create , no resolved value  should be exist
     */

    @Test
    public void testGet() throws Exception {

        assertNull(deferredObject.get());
        deferredObject.resolve(valueToResolve);
        assertEquals(deferredObject.get(),valueToResolve);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * Before object is resolved  ,{@link bgu.spl.a2.Deferred#get()} through exception
     */
    @Test public void testGetException() {
        try {
            deferredObject.resolve(valueToResolve);
            fail("Exception expected! for testGetException");
        }catch(Exception e){
            //test pass
        }

    }



    /**
     * Test method for {@link bgu.spl.a2.Deferred#isResolved()}:
     * Before object is resolved  {@link bgu.spl.a2.Deferred#isResolved} is false
     * After object is resolved  {@link bgu.spl.a2.Deferred#isResolved} is true
     */
    @Test
    public void testIsResolved() throws Exception {
        assertFalse(deferredObject.isResolved());
        deferredObject.resolve(valueToResolve);
        assertTrue(deferredObject.isResolved());
    }




    @Test
    public void testResolve() throws Exception {
        deferredObject.resolve(valueToResolve);
        assertEquals(deferredObject.get(),valueToResolve);
    }


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
     * Before object is resolved  {@link bgu.spl.a2.Deferred#isResolved} is false
     * After object is resolved  {@link bgu.spl.a2.Deferred#isResolved} is true
     */
    @Test
    public void testWhenResolved() throws Exception {
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter,1);
        deferredObject.resolve(valueToResolve);
        assertEquals(testCallBackCounter,2);
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter,2);
    }


    @Test
    public void testOnResolvedObjectWhenResolved() throws Exception {
        deferredObject.resolve(valueToResolve);
        deferredObject.whenResolved(callBack);
        assertEquals(testCallBackCounter,2);
    }

    @After
    public void tearDown() throws Exception {
        assertNull(deferredObject);
        assertNull(callBack);
        assertNull(valueToResolve);
        assertNull(testCallBackCounter);
    }

}