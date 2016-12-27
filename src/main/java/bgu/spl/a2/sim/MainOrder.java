package bgu.spl.a2.sim;

/**
 * Created by himelbrand on 12/27/16.
 */
public class MainOrder {
    class Waves
    {
        String product;

        String startId;

        String qty;



        @Override
        public String toString()
        {
            return "ClassPojo [product = "+product+", startId = "+startId+", qty = "+qty+"]";
        }
    }
    class Plans
    {
        String product;

        String[] tools;

        String[] parts;





        @Override
        public String toString()
        {
            return "ClassPojo [product = "+product+", tools = "+tools+", parts = "+parts+"]";
        }
    }
    class Tools
    {
        String tool;

        String qty;


        @Override
        public String toString()
        {
            return "ClassPojo [tool = "+tool+", qty = "+qty+"]";
        }
    }
    Plans[] plans;

    Tools[] tools;

    String threads;

    Waves[][] waves;
    
    @Override
    public String toString()
    {
        return "ClassPojo [plans = "+plans+", tools = "+tools+", threads = "+threads+", waves = "+waves+"]";
    }
}
