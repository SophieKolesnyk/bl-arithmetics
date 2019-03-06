import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    /*public static BigDecimal reminder(int N) {
        BigDecimal
    }*/

    public static void main(String[] args) throws IOException {
       // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число:\n");
        String input = "484854785147852478521482148";
        List<String> strDigits = BigInput.splitToPortions(input,3);
        BLDigit res = BigInput.mergeBLsBeforePoint(strDigits, 3);
        res.show();
        System.out.println(Convertor.toDecimal(res));

       // String[] inputs = reader.readLine().split(" ");

       // String input = reader.readLine();
      //  String input = "484854785147852478521482148.85485";
      //  BLDigit res = BigInput.stringToRl(input);
      //  res.show();

       // System.out.println(Convertor.toDecimal(res));

    }

}
