import java.util.ArrayList;
import java.util.Arrays;

public class Calculator {


    Calculator() {}

    /*
        public static Pair secretKey(int N) {
        int P1 = (int)(Math.sqrt(N));

        int P2 = 0;
        int i = 1;
        int divisior = 0;
        int mult = 0;

        while (mult!=N) {
            divisior = P1-i;
            P2 = (int)(N/divisior);
            mult = P2*divisior;
            ++i;
        }

        Pair result = new Pair((int)(divisior), (int)(P2));

        return result;
    }
     */


    public static BLPair findSecretKeys(BLDigit N) {

        BLDigit P1 = BLDigit.sqrt(N);
        BLDigit P2 = new BLDigit();
        BLDigit i = new BLDigit(0, 1, new ArrayList(Arrays.asList(0)));
        BLDigit divisior = BLDigit.ZERO;
        BLDigit mult = BLDigit.ZERO;

        while (BLDigit.compare(mult, N)!=0) {
            divisior = BLDigit.sub(P1, i);
            P2 = BLDigit.wihtoutFractPart(BLDigit.div(N, divisior));
            mult = BLDigit.mult(P2, divisior);
            i = BLDigit.add(i, new BLDigit(0, 1, new ArrayList(Arrays.asList(0))));
        }

        return new BLPair(divisior, P2);

    }


}
