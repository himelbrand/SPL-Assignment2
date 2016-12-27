package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahar on 27/12/2016.
 */


public class ManufatoringTask extends Task<Product> {

    private  Product product;
    private ManufactoringPlan plan;
    public List<ManufatoringTask> myManufatoringTaskList;
    private ArrayList<String> myToolsList;

    public ManufatoringTask(String productName,long startId){

        this.product = new Product(startId,productName);
        plan = Simulator.myWarehouse.getPlan(productName);

         myManufatoringTaskList = new ArrayList<>();
         myToolsList = new ArrayList<>();
        for(String partName:plan.getParts()){
            myManufatoringTaskList.add(new ManufatoringTask(partName,startId + 1));
        }
        for(String toolName:plan.getTools()){
            myToolsList.add(toolName);
        }
    }

    @Override
    protected void start() {
        if(myManufatoringTaskList.size() != 0){
            spawn(myManufatoringTaskList.toArray(new ManufatoringTask[myManufatoringTaskList.size()]));
            ManufatoringTask taskReference = this;
            whenResolved(myManufatoringTaskList, () -> {
                for(ManufatoringTask spawnManufatoringTask:myManufatoringTaskList)
                {
                    taskReference.product.addPart(spawnManufatoringTask.getResult().get());
                }

                for(String toolName:myToolsList){
                    Deferred<Tool> myDeferred = Simulator.myWarehouse.acquireTool(toolName);
                    myDeferred.whenResolved(()->{
                        taskReference.product.setCurrentId( myDeferred.get().useOn(product));
                        Simulator.myWarehouse.releaseTool(myDeferred.get());

                        taskReference.myToolsList.remove(toolName);
                        if(taskReference.myToolsList.isEmpty()){

                            complete(taskReference.product);
                        }
                    });
                }

            });
        }else{
            complete(product);
        }
    }
}
