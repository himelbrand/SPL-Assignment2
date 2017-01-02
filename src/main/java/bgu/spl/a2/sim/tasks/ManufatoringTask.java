package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that represents a manufacturing task.
 * each task eventually produced a product.
 **/
public class ManufatoringTask extends Task<Product> {

    private  Product product;
    private CopyOnWriteArrayList<ManufatoringTask> myManufatoringTaskList;
    private CopyOnWriteArrayList<String> myToolsList;
    volatile  private AtomicInteger toolsUsedCount;

    /** ManufatoringTask constructor
     * @param productName - the product name to be created
     * @param startId - The start id of the product.
     */
    public ManufatoringTask(String productName,long startId){

        this.product = new Product(startId,productName);
        ManufactoringPlan plan = Simulator.myWarehouse.getPlan(productName);
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
    /**
     * start handling the task.
     * The method {@link Task#spawn(Task[])} new tasks respectively to the parts this product is needed (each parts is another product).
     * After the parts are ready ,they are add to the product part list.
     * Then we use each one of the tools accoring to the product plan and use them on the parts to get the final id of the product.
     */
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
                                Simulator.myWarehouse.releaseTool(myDeferred.get());
                                if (toolsUsedCount.decrementAndGet() == 0) {
                                    complete(product);
                                }
                        });
                    }
                }else{
                    complete(product);
                }

            });
        }else{
            complete(product);
        }
    }
}
