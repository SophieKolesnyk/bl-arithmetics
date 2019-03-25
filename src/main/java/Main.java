import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input1 = "867669.235779";//reader.readLine();
        String input2 = "24236.2562";//reader.readLine();
        BLDigit X = new BLDigit(input1);
        BigDecimal from_bl =  Converter.toDecimal(X);
        System.out.println(X);// + "\n" +  input1 + "\n" + from_bl);

        BLDigit Y = new BLDigit(input2);
        System.out.println(Y);

        BLDigit Z = Calculator.mult(X, Y);
        System.out.println(Z);
        BigDecimal Zd = new BigDecimal(input1).multiply(new BigDecimal(input2));
        System.out.println(Converter.toDecimal(Z)+"\n"+Zd);
//        System.out.println(new BigDecimal("389956523057744818357573121014910").divide(new  BigDecimal(input2)));
        //+"\n"+input+"\n"+input.length());
//        BLDigit bl_sqrt = Calculator.sqrt(int_X);
//        System.out.println(bl_sqrt);
//        System.out.println(Converter.toDecimal(bl_sqrt)+"\n"+input);
//        //BLPair int_keys = Calculator.findSecretKeys(int_X);
    }
}
