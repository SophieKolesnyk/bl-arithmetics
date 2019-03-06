import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число:");
        String[] input = reader.readLine().split(" ");

        BLDigit bl1 = new BLDigit(new BigDecimal(input[0]));
        BLDigit bl2 = new BLDigit(new BigDecimal(input[1]));

        BLDigit res = BLDigit.add(bl1, bl2);
        res.show();
        System.out.println(Convertor.toDecimal(res));
    }

}