import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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

        System.out.println("Введіть відкритий ключ: ");
        String input = reader.readLine();

        int N = Integer.parseInt(input);

        Pair keys = secretKey(N);
        System.out.println("q = "+keys.first+"\np = "+keys.second);
    }
}
