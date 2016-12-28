package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product implements java.io.Serializable{
	private final long startId;
	private long currentId;
	private String name;
	private ArrayList<Product> parts;
	/**
	* Constructor 
	* @param startId - Product start id
	* @param name - Product name
	*/
    public Product(long startId, String name){
		this.startId=startId;
		this.currentId=startId;
		this.name=name;
		this.parts = new ArrayList<>();
	}

	/**
	* @return The product name as a string
	*/
    public String getName(){
		return name;
	}

	/**
	* @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId(){
		return startId;
	}
    
	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(){} 
	*/
    public long getFinalId(){
		return currentId;
	}

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts(){
		return parts;
	}

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p){
		parts.add(p);
	}

	public void setCurrentId(long id){
        this.currentId += id;
    }

    public String toString(){
		String ans="\nProductName: "+name+"  Product Id = "+currentId;
		ans+="\nPartsList {";
		for (Product part:parts) {
			ans+=part.toString();
		}
		ans+="\n}";
		return ans;
	}


}
