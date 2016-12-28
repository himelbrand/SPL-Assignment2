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
    public long useOn(Product p){
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }
    private long func(long id) {

        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }

        return v;
    }
//    public long useOn(Product p) {
//        long ans=0;
//        BigInteger id;
//        for (Product part:p.getParts()) {
//            id = BigInteger.valueOf(part.getFinalId());
//            ans+=Math.abs(id.nextProbablePrime().longValue());
//        }
//        return ans;
//    }

    private boolean isPrime(long value) {
        if(value < 2) return false;
        if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }
}
