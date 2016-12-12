package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shahar on 11/12/2016.
 */
public class DeferredTest {


    private  Deferred<Task<Runnable>> deferredObject;
    private Task<Runnable> objectToResolve;

    @Before
    public void setUp() throws Exception {
        this.deferredObject = createDeferred();
    }

    /**
     * This creates the object under test.  Note that we must create a specific implementation (StackImpl)
     * of the interface under test. The rest of the test class only refers to the interface under test.
     *
     * @return a {@link Deferred} instance.
     */
    protected Deferred createDeferred() {
        return new Deferred<Task<Runnable>>();
    }






    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * after new {@link bgu.spl.a2.Deferred} is create , no resolved value  should be exist
     */

    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * Before object is resolved  ,{@link bgu.spl.a2.Deferred#valueToReturn} is null
     * After object is resolved  ,{@link bgu.spl.a2.Deferred#valueToReturn} is not null
     */

    @Test
    public void testGet() throws Exception {

        assertNull(deferredObject.valueToReturn);
        deferredObject.resolve(objectToResolve);
        assertNotNull(deferredObject.valueToReturn);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * Before object is resolved  ,{@link bgu.spl.a2.Deferred#get()} through exception
     */
    @Test public void testGetException() {
        try {

            deferredObject.resolve(objectToResolve);
            fail("Exception expected!");
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
        assertFalse(deferredObject.isResolved);
        deferredObject.resolve(objectToResolve);
        assertTrue(deferredObject.isResolved);
    }




    @Test
    public void testResolve() throws Exception {
        assertNull(deferredObject.valueToReturn);
        deferredObject.resolve(objectToResolve);
        assertEquals(deferredObject.valueToReturn,deferredObject.get());
    }

    @Test
    public void testResolveExeption()  {
        deferredObject.resolve(objectToResolve);
            try{
                deferredObject.resolve(objectToResolve);
                fail("Exception expected!");
            }catch(Exception e){
                //test pass
            }
        }


    @Test
    public void whenResolved() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        assertNull(deferredObject);
    }

}