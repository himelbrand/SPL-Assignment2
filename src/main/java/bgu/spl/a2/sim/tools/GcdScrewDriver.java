package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
import java.math.BigInteger;

/**
 * A class that represents a GcdScrewDriver tool
 */
public class GcdScrewDriver implements Tool {
    @Override
    public String getType() {
        return "gs-driver";
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
     * @return - The greatest common divider of the product id and the product id reverse.
     */
    private long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        return (b1.gcd(b2)).longValue();
    }
    /**
     * @param n - A number
     * @return - The reverse number of the given number (i.e 1234 and 4321)
     */
    private long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }
}
