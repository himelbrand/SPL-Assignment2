package bgu.spl.a2.sim;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

/**
 * A class that represents the simulator configuration file , containing the production waves,orders,tools and plans.
 * after the configuration file is loaded as a path argument to  {@link Simulator#main(String[])} , all the json details are insert
 * to here.
 * The purpose of the class is to turn the configuration file into a java object.
 */
public class MainOrder {

    /**
     * A class that represents the waves in the configuration file
     */
    public class Waves{
        String product;
        String startId;
        String qty;
        /**
         * Waves constructor
         */
        public Waves(){}
     }
    /**
     * A class that represents the tools in the configuration file
     */
    public class Tools{
        String tool;
        String qty;
    }
    //Main Order Fields
    ManufactoringPlan[] plans;
    Tools[] tools;
    String threads;
    Waves[][] waves;
}
