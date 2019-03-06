import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;


public class Main {

    public static void main(String[] args) throws IOException {
      //  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число:");
        System.out.println("123000 567.890");
      //  String[] input = reader.readLine().split(" ");

        BigDecimal d1 = new BigDecimal("123000");
        BigDecimal d2 = new BigDecimal("567.890");

        BLDigit bl1 = Convertor.decimalToRl(d1);
        BLDigit bl2 = Convertor.decimalToRl(d2);

        BLDigit thous = new BLDigit(new BigDecimal(1000));

        bl1.show();
        System.out.println(" * ");
        thous.show();

        BLDigit mbl1 = BLDigit.mult(bl1, thous);
        System.out.println(" = ");
        mbl1.show();
        System.out.println(Convertor.toDecimal(mbl1));
        System.out.println(" ");

        bl2.show();
        System.out.println(" * ");
        thous.show();
        BLDigit mbl2 = BLDigit.mult(bl2, thous);
        System.out.println(" = ");
        mbl2.show();
        System.out.println(Convertor.toDecimal(mbl2));
        System.out.println(" ");

        BLDigit res = BLDigit.add(mbl1, mbl2);
        mbl1.show();
        System.out.println(" + ");
        mbl2.show();
        System.out.println(" = ");
       res.show();

        System.out.println(Convertor.toDecimal(res));

    }

}
