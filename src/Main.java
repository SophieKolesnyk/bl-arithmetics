import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число:");
        String input = reader.readLine();
        BLDigit X = Convertor.fromDecimal(new BigDecimal(input));
        BLDigit sq = BLDigit.square(X);

        sq.show();
        System.out.println(Convertor.toDecimal(sq));
    }
}

