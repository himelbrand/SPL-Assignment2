package bgu.spl.a2.sim;

/**
 * Created by himelbrand on 12/27/16.
 */
public class MainOrder {
    class Waves{
        String product;
        String startId;
        String qty;
        public Waves(){}
         public String toString()
        {
            return "ClassPojo [product = "+product+", startId = "+startId+", qty = "+qty+"]";
        }
    }
    class Plans{
        String product;
        String[] tools;
        String[] parts;
        public String toString()
        {
            return "ClassPojo [product = "+product+", tools = "+tools+", parts = "+parts+"]";
        }
    }
    class Tools{
        String tool;
        String qty;
        public String toString()
        {
            return "ClassPojo [tool = "+tool+", qty = "+qty+"]";
        }
    }
    //Main Order Fields and toString
    Plans[] plans;
    Tools[] tools;
    String threads;
    Waves[][] waves;
    public String toString()
    {
        return "ClassPojo [plans = "+plans+", tools = "+tools+", threads = "+threads+", waves = "+waves+"]";
    }
}
