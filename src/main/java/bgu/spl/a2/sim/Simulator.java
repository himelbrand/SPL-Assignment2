/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileReader;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	public static Warehouse myWarehouse;
	private static WorkStealingThreadPool pool;

	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start(){

		pool.start();
	}

	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
		pool = myWorkStealingThreadPool;
	}
	
	public static int main(String [] args){

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(
					"/Users/<username>/Documents/file1.txt"));

			JSONObject jsonObject = (JSONObject) obj;

			String name = (String) jsonObject.get("Name");
			String author = (String) jsonObject.get("Author");
			JSONArray companyList = (JSONArray) jsonObject.get("Company List");

			System.out.println("Name: " + name);
			System.out.println("Author: " + author);
			System.out.println("\nCompany List:");
			Iterator<String> iterator = companyList.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		Simulator mySimulator = new Simulator();
		mySimulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(333));
		pool.start();
		myWarehouse = new Warehouse();
		return 1;
	}
}
