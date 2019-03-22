import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;




public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "5475257897111192744187861";//reader.readLine();
        BigInteger int_X = new BigInteger(input);
        Pair int_keys = Calculator.findSecretKeys(int_X);
    }
}
