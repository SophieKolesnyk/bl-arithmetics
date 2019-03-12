import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;


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

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть число: ");
        String input = "5475649896565498462132117203";//reader.readLine();
        System.out.println("Correct answer = " + BigInput.HugeDecToBL("73997634398453"));

        BLDigit X = BigInput.HugeDecToBL(input);
        BLDigit result = BLDigit.sqrt(X);
        System.out.println(result.toString());
        System.out.println(Convertor.toDecimal(result));
    }
}
