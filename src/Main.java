import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число:");
       // String input = reader.readLine();
        String input = "121";
        BLDigit X = Convertor.fromString(input);

          BLDigit result = BLDigit.sqrt(X);
        System.out.println(result.toString());
        System.out.println(Convertor.toDecimal(result));

    }
}
