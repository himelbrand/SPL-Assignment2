package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse in your simulation
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {


	/**
	 * counters for each one of the tool types.
	 */
	volatile private AtomicInteger gcdScrewDriverToolCount;
	volatile private AtomicInteger nextPrimeHammerToolCount;
	volatile private AtomicInteger randomSumPliersHammerToolCount;

	/**
	 * LinkedQueue for each one of the tool types,
	 * representing past requests for tools that weren't available at that time.
	 */
	private ConcurrentLinkedQueue<Deferred<Tool>> gcdScrewDriverDeferredList;
	private ConcurrentLinkedQueue<Deferred<Tool>> nextPrimeHammerDeferredList ;
	private ConcurrentLinkedQueue<Deferred<Tool>> randomSumPliersDeferredList ;

	private ConcurrentLinkedQueue<ManufactoringPlan> manufactoringPlansList ;

    private Object lockGcd = new Object();
    private Object lockNpm = new Object();
    private Object lockRph = new Object();
	/**
	 * Constructor
	 */
	public Warehouse(){
		gcdScrewDriverDeferredList = new ConcurrentLinkedQueue<>();
		nextPrimeHammerDeferredList = new ConcurrentLinkedQueue<>();
		randomSumPliersDeferredList = new ConcurrentLinkedQueue<>();
		manufactoringPlansList = new ConcurrentLinkedQueue<>();
        gcdScrewDriverToolCount = new AtomicInteger(0);
		nextPrimeHammerToolCount= new AtomicInteger(0);
		randomSumPliersHammerToolCount= new AtomicInteger(0);

	}
	/**
	 * Tool acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * @param type - string describing the required tool
	 * @return a deferred promise for the  requested tool
	 */
	public Deferred<Tool> acquireTool(String type) {
			Deferred<Tool> newTool = new Deferred<>();
			switch (type) {
				case "np-hammer":
				    synchronized (lockNpm) {
                        if (nextPrimeHammerToolCount.get() != 0) {
                            nextPrimeHammerToolCount.decrementAndGet();
                            newTool.resolve(new NextPrimeHammer());
                        } else
                            nextPrimeHammerDeferredList.add(newTool);
                    }
					break;
				case "rs-pliers":
                    synchronized (lockRph) {
                        if (randomSumPliersHammerToolCount.get() != 0) {
                            randomSumPliersHammerToolCount.decrementAndGet();
                            newTool.resolve(new RandomSumPliers());
                        } else
                            randomSumPliersDeferredList.add(newTool);
                    }
                        break;
				case "gs-driver":
				    synchronized (lockGcd) {
                        if (gcdScrewDriverToolCount.get() != 0) {
                            gcdScrewDriverToolCount.decrementAndGet();
                            newTool.resolve(new GcdScrewDriver());
                        } else {
                            gcdScrewDriverDeferredList.add(newTool);
                        }
                    }
					break;
			}
		//	System.out.println("gcdScrewDriver : " + gcdScrewDriverToolCount.get() + " | " + "randomSumPliersHammer : " + randomSumPliersHammerToolCount.get() + " | " + "nextPrimeHammer : " + nextPrimeHammerToolCount.get());
			return newTool;
		}

	/**
	 * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	 * @param tool - The tool to be returned
	 */
	public void releaseTool(Tool tool) {
			Deferred<Tool> tempDeff;
			switch (tool.getType()) {
				case "np-hammer":
				    synchronized (lockNpm) {
                        nextPrimeHammerToolCount.incrementAndGet();
                        tempDeff = nextPrimeHammerDeferredList.poll();
                        if (tempDeff != null) {
                            nextPrimeHammerToolCount.decrementAndGet();
                            tempDeff.resolve(new NextPrimeHammer());
                        }
                    }
					break;
				case "rs-pliers":
                    synchronized (lockRph) {
                        randomSumPliersHammerToolCount.incrementAndGet();
                        tempDeff = randomSumPliersDeferredList.poll();
                        if (tempDeff != null) {
                            randomSumPliersHammerToolCount.decrementAndGet();
                            tempDeff.resolve(new RandomSumPliers());
                        }
                    }
					break;
				case "gs-driver":
                    synchronized (lockGcd) {
                        gcdScrewDriverToolCount.incrementAndGet();
                        tempDeff = gcdScrewDriverDeferredList.poll();
                        if (tempDeff != null) {
                            gcdScrewDriverToolCount.decrementAndGet();
                            tempDeff.resolve(new GcdScrewDriver());
                        }
                    }
					break;
			}
		//	System.out.println("release-gcdScrewDriver : " + gcdScrewDriverToolCount.get() + " | " + "randomSumPliersHammer : " + randomSumPliersHammerToolCount.get() + " | " + "nextPrimeHammer : " + nextPrimeHammerToolCount.get());
		}


	/**
	 * Getter for ManufactoringPlans
	 * @param product - a string with the product name for which a ManufactoringPlan is desired
	 * @return A ManufactoringPlan for product
	 */
	public ManufactoringPlan getPlan(String product){
		ManufactoringPlan myManufactoringPlanToReturn = null;
		for(ManufactoringPlan myManufactoringPlan:manufactoringPlansList){
			if(myManufactoringPlan.getProductName().equals(product))
				myManufactoringPlanToReturn =  myManufactoringPlan;
		}
		return myManufactoringPlanToReturn;
	}

	/**
	 * Store a ManufactoringPlan in the warehouse for later retrieval
	 * @param plan - a ManufactoringPlan to be stored
	 */
	public void addPlan(ManufactoringPlan plan){
		manufactoringPlansList.add(plan);
	}

	/**
	 * Store a qty Amount of tools of type tool in the warehouse for later retrieval
	 * @param tool - type of tool to be stored
	 * @param qty - amount of tools of type tool to be stored
	 */
	public void addTool(Tool tool, int qty){
		switch(tool.getType()){
			case "np-hammer":
				nextPrimeHammerToolCount.set(qty);
				break;
			case "rs-pliers":
				randomSumPliersHammerToolCount.set(qty);
				break;
			case "gs-driver":
				gcdScrewDriverToolCount.set(qty);
				break;
		}

	}


}
