package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by himelbrand on 12/27/16.
 */
public class GcdScrewDriver implements Tool {
    @Override
    public String getType() {
        return "gs-driver";
    }

    @Override
    public long useOn(Product p) {
        long ans=0;
        BigInteger idReverse;
        BigInteger id;
        BigInteger gcd;
        for (Product part:p.getParts()) {
            id = BigInteger.valueOf(part.getFinalId());
            idReverse = BigInteger.valueOf(getReversedLong(part.getFinalId()));
            gcd=id.gcd(idReverse);
            ans+=Math.abs(gcd.longValueExact());
        }
        return ans;
    }
    private long getReversedLong(long num){
        long reversed = 0;
        while( num != 0 )
        {
            reversed = reversed * 10;
            reversed = reversed + num%10;
            num = num/10;
        }
        return reversed;
    }
}
