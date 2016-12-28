package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Shahar on 27/12/2016.
 */


public class ManufatoringTask extends Task<Product> {

    private  Product product;
    private ManufactoringPlan plan;
    public CopyOnWriteArrayList<ManufatoringTask> myManufatoringTaskList;
    private CopyOnWriteArrayList<String> myToolsList;
    private AtomicInteger toolsUsedCount;

    public ManufatoringTask(String productName,long startId){
        this.product = new Product(startId,productName);
        plan = Simulator.myWarehouse.getPlan(productName);
        myManufatoringTaskList = new CopyOnWriteArrayList<>();
        myToolsList = new CopyOnWriteArrayList<>();
        for(String partName:plan.getParts()){
            myManufatoringTaskList.add(new ManufatoringTask(partName,startId + 1));
        }
        for(String toolName:plan.getTools()){
            myToolsList.add(toolName);
        }
        toolsUsedCount = new AtomicInteger(myToolsList.size());

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
                if(myToolsList.size()>0) {
                    for (String toolName : myToolsList) {
                        Deferred<Tool> myDeferred = Simulator.myWarehouse.acquireTool(toolName);
                        myDeferred.whenResolved(() -> {
                            product.setCurrentId(myDeferred.get().useOn(product));
                            toolsUsedCount.decrementAndGet();
                            Simulator.myWarehouse.releaseTool(myDeferred.get());
                            System.out.println("Tool released : " + toolName);
                            System.out.println("task:" + this.taskName + " " + toolsUsedCount.get() + " Tools are needed ,    " + toolsUsedCount.get() + "tools left");
                            if (toolsUsedCount.get() == 0) {
                                complete(product);
                            }
                        });
                    }
                }else{
                    for (Product part:product.getParts())
                        product.setCurrentId(part.getFinalId());
                    complete(product);
                }

            });
        }else{
            complete(product);
        }
    }
}
