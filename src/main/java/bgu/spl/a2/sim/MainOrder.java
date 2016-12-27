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

        String[] getTools ()
        {
            return tools;
        }

        public void setTools (String[] tools)
        {
            this.tools = tools;
        }

        public String[] getParts ()
        {
            return parts;
        }

        public void setParts (String[] parts)
        {
            this.parts = parts;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [product = "+product+", tools = "+tools+", parts = "+parts+"]";
        }
    }
    public class Tools
    {
        private String tool;

        private String qty;

        public String getTool ()
        {
            return tool;
        }

        public void setTool (String tool)
        {
            this.tool = tool;
        }

        public String getQty ()
        {
            return qty;
        }

        public void setQty (String qty)
        {
            this.qty = qty;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [tool = "+tool+", qty = "+qty+"]";
        }
    }
    private Plans[] plans;

    private Tools[] tools;

    private String threads;

    private Waves[][] waves;



    public Tools[] getTools ()
    {
        return tools;
    }

    public void setTools (Tools[] tools)
    {
        this.tools = tools;
    }

    public String getThreads ()
    {
        return threads;
    }

    public void setThreads (String threads)
    {
        this.threads = threads;
    }

    public Waves[][] getWaves ()
    {
        return waves;
    }

    public void setWaves (Waves[][] waves)
    {
        this.waves = waves;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [plans = "+plans+", tools = "+tools+", threads = "+threads+", waves = "+waves+"]";
    }
}
