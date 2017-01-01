package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * A class that represents a NextPrimeHammer tool
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

    /**
     * @param id - The product id.
     * @return - The first prime number following the product id
     */
    private long func(long id) {

        long v  = id + 1;
        while (!isPrime(v)) {
            v++;
        }
        return v;
    }


    /**
     * @param value - The value should be determined if it is a prime number or not.
     * @return boolean - True if the given value is a prime number.
     */
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
