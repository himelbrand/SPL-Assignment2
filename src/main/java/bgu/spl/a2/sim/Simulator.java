/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufatoringTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	public static Warehouse myWarehouse;
	private static WorkStealingThreadPool pool;
	private static MainOrder myConfiguration;
    private static volatile  int ordersCount = 0;

	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start(){


        Object lock = new Object();
        ConcurrentLinkedQueue<Product> myProductsList = new ConcurrentLinkedQueue<>();
        pool.start();
    	for(MainOrder.Waves[] wave:myConfiguration.waves) {

    	    for(MainOrder.Waves order:wave) {

    	          ordersCount += (int)Integer.parseInt(order.qty);

    	        for(int i=0;i<Integer.parseInt(order.qty);i++) {
                    ManufatoringTask orderTask = new ManufatoringTask(order.product, Long.parseLong(order.startId));
                    orderTask.getResult().whenResolved(()->{
                        myProductsList.add(orderTask.getResult().get());
                        ordersCount--;
                        System.out.println("###########created a :"+orderTask.getResult().get().getName()+"###############");
                        System.out.println("##################3 ordersCount : "+ordersCount+"!!!!! ######################");
                        System.out.println("products made : "+myProductsList.size()+"!!!!!");
                        if(ordersCount == 0){
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        }


                    });
                    pool.submit(orderTask);
                }


            }
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@2  locked @@@@@@@@@@@@@@@2@@@@@@");
                } catch (InterruptedException e) {
                    System.out.println("moving to next wave");
                }
            }
            System.out.println("moving to next wave");

        }

        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            System.out.println("pool shutdown exception");
        }
        return myProductsList;
    }

	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
		pool = myWorkStealingThreadPool;
	}
	
	public static int main(String [] args){

        myWarehouse = new Warehouse();
        Gson gson = new Gson();

        try {
            myConfiguration = gson.fromJson(new FileReader(args[0]), MainOrder.class);
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found.");
        }

        Simulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(Integer.parseInt(myConfiguration.threads)));

        //Add tools to warehouse
        for(MainOrder.Tools tool: myConfiguration.tools){
        switch(tool.tool){
            case "np-hammer":
                myWarehouse.addTool(new NextPrimeHammer(),Integer.parseInt(tool.qty));
                break;
            case "rs-pliers":
                myWarehouse.addTool(new RandomSumPliers(),Integer.parseInt(tool.qty));
                break;
            case "gs-driver":
                myWarehouse.addTool(new GcdScrewDriver(),Integer.parseInt(tool.qty));
                break;
        }
        }

        //Add plans to warehouse
        for(ManufactoringPlan plan: myConfiguration.plans){
            myWarehouse.addPlan(plan);
        }


        ConcurrentLinkedQueue<Product> SimulationResult;
        SimulationResult = Simulator.start();
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("result.ser");
            // This is the println of the content spouse to be in the output file, still not working
            System.out.println(SimulationResult);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(SimulationResult);
        } catch (Exception e) {
            e.printStackTrace();
        }



		return 0;
	}
}
