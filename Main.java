import java.io.IOException;
import java.math.BigDecimal;
//import java.math.MathContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
/*
    private static void print_result(int i) {
        System.out.println("Mahips: 0.1." + String.valueOf(i));
    }

    private static int find_mahips() {
        BigDecimal one = new BigDecimal("1");
        BigDecimal two = new BigDecimal("2");

        int i = -9999999;
        while (true) {
            try {
                if (one.equals(one.add(two.pow(i, MathContext.DECIMAL32)))) {
                    print_result(i);
                    return i;
                }
            } catch (Exception e) {
                print_result(i);
                return i;
            }
            i--;
        }
    }
*/
    public static void main(String[] args) throws IOException {
        //find_mahips(); //shows mahips
        /*
        InputManager manager = new InputManager();
        manager.init();
        manager.showUsage();
        manager.optimise();

        String res = manager.run();

        // System.out.print(res + " | ");
        // System.out.println(Convertor.toDecimalFromRl(res.substring(4)));
        System.out.println("Result:\nRL: " + res +
                        "\nDecimal: " +
               Convertor.toDecimalFromRl(res.substring(res.indexOf('.', res.indexOf('.') + 1) + 1)));
               */
        BigInput b = new BigInput();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите очень большое число: ");
        String str = reader.readLine();
        int size = b.splitToPortions(str, 3).length;
        //String res =
        for(int i = size-1; i>=0; --i)
        {
            BigDecimal t = new BigDecimal(b.splitToPortions(str, 3)[i]);
            System.out.println(t);
            System.out.println(Convertor.decimalFloatingToRl(t));
        }

    }
}
