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
        String[] input = reader.readLine().split(" ");
        BigDecimal d1 = new BigDecimal(input[0]);
        BigDecimal d2 = new BigDecimal(input[1]);
        BigDecimal d_res = d1.subtract(d2);
        BLDigit dbl = Convertor.fromDecimal(d_res);
        BLDigit r1 = Convertor.fromString ("48900000786147800345672345678909876543");

        BLDigit X1 = Convertor.fromString(input[0]);
        BLDigit X2 = Convertor.fromString(input[1]);


        BLDigit result = BLDigit.sub(X1, X2);
        //BLDigit result = BLDigit.add(X1, X2);

        //  BLDigit result = BLDigit.square(X);
        System.out.println(result.N.toString());
        System.out.println("\n"+r1.N.toString());
     //   System.out.println(BigDecimalMath.pow(new BigDecimal("2"), new BigDecimal(result.N.get(0).toString()), new MathContext(result.precision)));
    }
}

//48964896786147821345672345678909876543 64896000000021000000000000000000000
//   64896000000021000000000000000000000
//48900000786147800345672345678909876543

//89678614782134567 678600002134567
//  678600002134567
//89000014780000000





//[125, 122, 119, 118, 116,      114, 113, 110,           107,      105, 104, 103, 102,      99, 98,     95, 94,     92, 91,             85, 83, 80,     79, 74, 73, 72, 71, 70, 69,     67, 66, 65, 64, 63, 62,     60, 57,     54, 53, 50, 49, 48, 47, 46, 45,     43,     41,     39,                 30, 29, 28, 27, 26,     24, 23,     20, 18, 16, 13, 11, 8, 5, 4, 3, 2, 1, 0]
//[                         115, 114,      110, 109, 108, 107, 106, 105, 104, 103,      100, 99, 98, 97, 95,     93,     91, 90, 88, 86,     83,     78,         73,             69, 68,     66, 65, 64,         61, 60, 57, 56,     53,     49,     47,     45, 44,     42, 41, 40, 39, 36, 35, 33, 32, 30,                 25, 24,     21]
//                                                                                                                                                                                                                                                                                           36, 35, 33, 32, 29, 28, 27, 26, 25, 24, 22, 21, 20, 18, 16, 13, 11, 8, 5, 4, 3, 2, 1, 0]

//[125, 122, 119, 118, 115, 112, 111, 110, 107, 106, 101, 99, 98, 97, 93, 91, 89, 87, 86, 85, 80, 78, 74, 72, 71, 69, 68, 67, 63, 60, 59, 58, 57, 56, 54, 50, 48, 45, 44, 41, 39, 38, 37, 34, 32, 29, 28, 27, 25, 22, 21, 20, 18, 16, 13, 11, 8, 5, 4, 3, 2, 1, 0]
//[125, 122, 119, 118, 116, 112, 111, 110, 107, 106, 101, 99, 98, 97, 93, 91, 89, 87, 86,     80, 78, 74, 72, 71, 69, 68, 67, 63, 60, 59, 58, 57, 56, 54, 50, 48, 45, 44, 41, 39, 38, 37, 34, 32, 29, 28, 27, 25, 22, 21, 20, 18, 16, 13, 11, 8, 5, 4, 3, 2, 1, 0]
//[125, 122, 119, 118, 115, 112, 111, 110, 107, 106, 101, 99, 98, 97, 93, 91, 89, 87, 86, 85, 80, 78, 74, 72, 71, 69, 68, 67, 63, 60, 59, 58, 57, 56, 54, 50, 48, 45, 44, 41, 39, 38, 37, 34, 32, 29, 28, 27, 25, 22, 21, 20, 18, 16, 13, 11, 8, 5, 4, 3, 2, 1, 0]