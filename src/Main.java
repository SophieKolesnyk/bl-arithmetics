import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;


public class Main {

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

    public static void main(String[] args) throws IOException {

        BLDigit N = Convertor.fromString("161");
        BLPair keys = Calculator.findSecretKeys(N);
        System.out.println(keys.first + "\n" + keys.second);
        System.out.println(Convertor.toDecimal(keys.first) + "\n" + Convertor.toDecimal(keys.second));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть число: ");
        String[] input = reader.readLine().split(" ");


        BLDigit X = BigInput.HugeDecToBL(input[0]);
        BLDigit Y = BigInput.HugeDecToBL(input[1]);

        BigDecimal divider = new BigDecimal(input[0]);
        BigDecimal divisior = new BigDecimal(input[1]);
        BigDecimal dec_res = divider.divide(divisior);

        BLDigit result = BLDigit.div(X, Y);
        System.out.println(result);
        System.out.println(Convertor.toDecimal(result));
        System.out.println("Correct answer = " + dec_res + "\n" + BigInput.HugeDecToBL(dec_res.toString()));

    }
}
