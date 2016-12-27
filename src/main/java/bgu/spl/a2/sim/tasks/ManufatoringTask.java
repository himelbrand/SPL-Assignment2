package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahar on 27/12/2016.
 */


public class ManufatoringTask extends Task<Product> {

    private  Product product;
    private ManufactoringPlan plan;
        public List<ManufatoringTask> myManufatoringTaskList;
    private ArrayList<ToolTask> myToolsTaskList;

    public ManufatoringTask(String productName,long startId){

        this.product = new Product(startId,productName);
        plan = Simulator.myWarehouse.getPlan(productName);

         myManufatoringTaskList = new ArrayList<>();
         myToolsTaskList = new ArrayList<>();
        for(String partName:plan.getParts()){
            myManufatoringTaskList.add(new ManufatoringTask(partName,startId + 1));
        }
        for(String toolName:plan.getTools()){
            myToolsTaskList.add(new ToolTask(toolName));
        }
    }

    @Override
    protected void start() {
        if(myManufatoringTaskList.size() != 0){
            spawn();
        }else{
            complete(product);
        }
    }
}
