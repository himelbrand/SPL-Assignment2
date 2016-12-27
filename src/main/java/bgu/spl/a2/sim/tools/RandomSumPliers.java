package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

public class RandomSumPliers implements Tool {
    @Override
    public String getType() {
        return "rs-pliers";
    }
    @Override
    public long useOn(Product p) {
        long ans=0;
        Random random = new Random();
        for (Product part:p.getParts()) {
            random.setSeed(part.getFinalId());
            ans+=Math.abs(random.nextInt());
        }
        return ans;
    }
}
