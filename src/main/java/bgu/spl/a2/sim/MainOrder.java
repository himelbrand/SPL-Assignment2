package bgu.spl.a2.sim;

import bgu.spl.a2.sim.conf.ManufactoringPlan;

public class MainOrder {
    public class Waves{
        String product;
        String startId;
        String qty;
        public Waves(){}
         public String toString()
        {
            return "ClassPojo [product = "+product+", startId = "+startId+", qty = "+qty+"]";
        }
    }
    public class Tools{
        String tool;
        String qty;
        public String toString()
        {
            return "ClassPojo [tool = "+tool+", qty = "+qty+"]";
        }
    }
    //Main Order Fields and toString
    ManufactoringPlan[] plans;
    Tools[] tools;
    String threads;
    Waves[][] waves;
    public String toString()
    {
        return "ClassPojo [plans = "+plans+", tools = "+tools+", threads = "+threads+", waves = "+waves+"]";
    }
}
