package bgu.spl.a2.sim;

import bgu.spl.a2.Processor;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.concurrent.ConcurrentLinkedQueue;

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


	int gcdScrewDriverToolCount = 0;
	int nextPrimeHammerToolCount = 0;
	int randomSumPliersHammerToolCount = 0;

	private ConcurrentLinkedQueue<Deferred<Tool>> gcdScrewDriverDeferredList;
	private ConcurrentLinkedQueue<Deferred<Tool>> nextPrimeHammerDeferredList ;
	private ConcurrentLinkedQueue<Deferred<Tool>> randomSumPliersDeferredList ;

	private ConcurrentLinkedQueue<ManufactoringPlan> manufactoringPlansList ;

	/**
	* Constructor
	*/
    public Warehouse(){
		gcdScrewDriverDeferredList = new ConcurrentLinkedQueue<Deferred<Tool>>();
		nextPrimeHammerDeferredList = new ConcurrentLinkedQueue<Deferred<Tool>>();
		randomSumPliersDeferredList = new ConcurrentLinkedQueue<Deferred<Tool>>();

		manufactoringPlansList = new ConcurrentLinkedQueue<ManufactoringPlan>();
	}

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    public Deferred<Tool> acquireTool(String type){
		Deferred<Tool> newTool = new Deferred<Tool>();
    	switch(type){
			case "np-hammer":
				if(nextPrimeHammerToolCount != 0){
					nextPrimeHammerToolCount --;
					newTool.resolve(new NextPrimeHammer());
				}else
					nextPrimeHammerDeferredList.add(newTool);
				break;
			case "rs-pliers":
				if(randomSumPliersHammerToolCount != 0){
					randomSumPliersHammerToolCount --;
					newTool.resolve(new RandomSumPliers());
				}else
					randomSumPliersDeferredList.add(newTool);
				break;
			case "gs-driver":
				if(gcdScrewDriverToolCount != 0){
					gcdScrewDriverToolCount --;
					newTool.resolve(new GcdScrewDriver());
				}else{
					gcdScrewDriverDeferredList.add(newTool);
				}
				break;
		}
		return newTool;
	}

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
    public void releaseTool(Tool tool){
    	switch(tool.getType()){
			case "np-hammer":
				nextPrimeHammerToolCount++;
				nextPrimeHammerDeferredList.poll().resolve(new NextPrimeHammer());
				break;
			case "rs-pliers":
				randomSumPliersHammerToolCount++;
				randomSumPliersDeferredList.poll().resolve(new RandomSumPliers());
				break;
			case "gs-driver":
				gcdScrewDriverToolCount++;
				gcdScrewDriverDeferredList.poll().resolve(new GcdScrewDriver());
				break;
		}
	}

	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product){
		ManufactoringPlan myManufactoringPlansToReturn = null;
    	for(ManufactoringPlan myManufactoringPlans:manufactoringPlansList){
    		if(myManufactoringPlans.getProductName().equals(product))
				myManufactoringPlansToReturn =  myManufactoringPlans;
		}
//		if(myManufactoringPlansToReturn == null)
//			throw new IllegalStateException("no plan exist with product name of /" + product);
		return myManufactoringPlansToReturn;
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
				nextPrimeHammerToolCount++;
				break;
			case "rs-pliers":
				randomSumPliersHammerToolCount++;
				break;
			case "gs-driver":
				gcdScrewDriverToolCount++;
				break;
		}

	}

}
