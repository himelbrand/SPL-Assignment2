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

    public long useOn(Product p){
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }
    public long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
    }
    public long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }
//    public long useOn(Product p) {
//        long ans=0;
//        BigInteger idReverse;
//        BigInteger id;
//        BigInteger gcd;
//        for (Product part:p.getParts()) {
//            id = BigInteger.valueOf(part.getFinalId());
//            idReverse = BigInteger.valueOf(getReversedLong(part.getFinalId()));
//            gcd=id.gcd(idReverse);
//            ans+=Math.abs(gcd.longValue());
//        }
//        return ans;
//    }
//    private long getReversedLong(long num){
//        long reversed = 0;
//        while( num != 0 )
//        {
//            reversed = reversed * 10;
//            reversed = reversed + num%10;
//            num = num/10;
//        }
//        return reversed;
//    }
}
