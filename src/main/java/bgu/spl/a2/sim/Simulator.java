package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufatoringTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.gson.Gson;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    public static Warehouse myWarehouse;
    private static WorkStealingThreadPool pool;
    private static MainOrder myConfiguration;
    private static volatile AtomicInteger ordersCount = new AtomicInteger(0);
    private static Product[] orderProductArray;

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {


        Object lock = new Object();
        ConcurrentLinkedQueue<Product> myProductsList = new ConcurrentLinkedQueue<>();
        Simulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(Integer.parseInt(myConfiguration.threads)));
        pool.start();
        for (MainOrder.Waves[] wave : myConfiguration.waves) {

            //Using a temp array for saving the product ,in each wave, in there right order( by final id)
            int sum = 0;
            int firstIndex = 0;
            for (MainOrder.Waves order : wave) {
                sum += Integer.parseInt(order.qty);
            }
            orderProductArray = new Product[sum];

            for (MainOrder.Waves order : wave) {
                ordersCount.addAndGet(Integer.parseInt(order.qty));
                for (int i = 0; i < Integer.parseInt(order.qty); i++) {
                    ManufatoringTask orderTask = new ManufatoringTask(order.product, Long.parseLong(order.startId) + i);

                    int firstIndexCopy = firstIndex;
                    long tempOrderId = Long.parseLong(order.startId);

                    orderTask.getResult().whenResolved(() -> {
                        orderProductArray[firstIndexCopy + (int) (orderTask.getResult().get().getStartId() - tempOrderId)] = orderTask.getResult().get();
                        if (ordersCount.decrementAndGet() == 0) {
                            //We use synchronized so only one wave will be produced at a time
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        }
                    });
                    pool.submit(orderTask);
                }
                //Used for determined the right position for each order in the same wave in the orderProductArray
                firstIndex += Integer.parseInt(order.qty);
            }
            //We use synchronized so only one wave will be produced at a time
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Wave was interrupted");
                }
            }

            //Filling the product queue with all the product in right order from the current wave
            for (Product product : orderProductArray) {
                myProductsList.add(product);
            }
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
     *
     * @param myWorkStealingThreadPool - the Wo  rkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        pool = myWorkStealingThreadPool;
    }

    public static void main(String[] args) {
        myWarehouse = new Warehouse();

        //Read the configuration file and turn into a java object (MainOrder Object)
        Gson gson = new Gson();
        try {
            myConfiguration = gson.fromJson(new FileReader(args[0]), MainOrder.class);
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found.");
        }

        //Add tools to warehouse
        for (MainOrder.Tools tool : myConfiguration.tools) {
            switch (tool.tool) {
                case "np-hammer":
                    myWarehouse.addTool(new NextPrimeHammer(), Integer.parseInt(tool.qty));
                    break;
                case "rs-pliers":
                    myWarehouse.addTool(new RandomSumPliers(), Integer.parseInt(tool.qty));
                    break;
                case "gs-driver":
                    myWarehouse.addTool(new GcdScrewDriver(), Integer.parseInt(tool.qty));
                    break;
            }
        }

        //Add plans to warehouse
        for (ManufactoringPlan plan : myConfiguration.plans) {
            myWarehouse.addPlan(plan);
        }


        //will eventually contain all the products that were made
        ConcurrentLinkedQueue<Product> SimulationResult = Simulator.start();

        //Writing result to an output file
        FileOutputStream fout;
        try {
            fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(SimulationResult);
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file not found");
        } catch (IOException e) {
            System.out.println("Problem writing result to the result.ser file");
        }
    }
}
