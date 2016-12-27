package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by himelbrand on 12/27/16.
 */
public class NextPrimeHammer implements Tool {
    @Override
    public String getType() {
        return "np-hammer";
    }

    @Override
    public long useOn(Product p) {
        long ans=0;
        BigInteger id;
        for (Product part:p.getParts()) {
            id = BigInteger.valueOf(part.getFinalId());
            ans+=Math.abs(id.nextProbablePrime().longValueExact());
        }
        return ans;
    }
}
