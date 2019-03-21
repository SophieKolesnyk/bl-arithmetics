import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {


    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть число: ");
        String input = "547525789711119274418786";
        System.out.println(BigInput.HugeDecToBL("739949856214") + " <-  Correct answer");

        BLDigit X = BigInput.HugeDecToBL(input);
        BLDigit result = BLDigit.sqrt(X);
        System.out.println(result.toString() + " <-  sqrt result\n"  + Converter.toDecimal(result));
    }
}
