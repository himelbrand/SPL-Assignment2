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
    private java.lang.Object objectToResolve;

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
        return new Deferred();
    }



    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}:
     * after new {@link bgu.spl.a2.Deferred} is create , no resolved value  should be exist
     */

    @Test
    public void testGet() throws Exception {

        assertNull(deferredObject.valueToReturn);
        deferredObject.resolve(objectToResolve);
        assertNotNull(deferredObject.va);
    }

    /**
     * Test method for {@link bgu.spl.a2.Deferred#get()}.
     * This is a negative test - cause an exception to be thrown.
     * We verify that the OUT fails when we invoke it without keeping the pre-condition.
     * When we invoke a method without keeping the pre-condition, we expect the method to throw an exception.
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
     * Test method for {@link Deferred#isResolved()}.
     */
    @Test
    public void testIsResolved() throws Exception {
        assertTrue(deferredObject.resolve(objectToResolve));
    }

    @Test(expected=Exception.class)
    public void resolve() throws Exception {

    }

    @Test
    public void testResolveExeption()  {
        if(deferredObject.isResolved()){
            try{
                deferredObject.resolve(Object val);
                fail("Exception expected!");
            }catch(Exception e){
                //test pass
            }
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