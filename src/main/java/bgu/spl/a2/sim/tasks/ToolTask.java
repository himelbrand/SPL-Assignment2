package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.Tool;

/**
 * Created by Shahar on 27/12/2016.
 */
public class ToolTask extends Task<Tool> {
    private Tool tool;
    String toolName;
    public ToolTask(String toolName){
       this.toolName=toolName;

    }
    @Override
    protected void start() {
        Deferred<Tool> promisedTool = Simulator.myWarehouse.acquireTool(toolName);

        promisedTool.whenResolved(()->{

        });
    }
}
