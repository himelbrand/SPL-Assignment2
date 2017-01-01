package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product implements java.io.Serializable{

	private final long startId;
	volatile  private long currentId;
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
		//	synchronized (this) {
				return currentId;
		  //  }
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

	/**
	 * set the current id of a product ,
	 * @param id - id to be added to the current id of the product
	 * This method have to be synchronized because its relay on the currentId that can be change
	 *			inside this method. if two objects change the currentId  of a product at the same time using this method,
	 *		    there is a chance that without synchronized , only one of the changes will apply.
	 */
	public void setCurrentId(long id){
        synchronized (this) {
            this.currentId += id;
        }
    }

	/**
	 * prints the product name and every one of his parts
	 */
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
