import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BigInput {

    public static BigDecimal ten = new BigDecimal("10");

    public static List<String> splitToPortions(String i_str, int p_size) {
        List<String> result = new ArrayList();
        int iter_numb = 0;

        if (p_size < 0) {
            iter_numb = Math.abs(i_str.length() / p_size);
            for (int i = 0; i < iter_numb; ++i) {
                p_size = Math.abs(p_size);

                result.add(i_str.substring(0, p_size));
                i_str = i_str.substring(p_size, i_str.length());
            }
            if (i_str.length() % p_size > 0) {
                result.add(i_str);
            }
        } else {
            iter_numb = i_str.length() / p_size;

            for (int i = 0; i < iter_numb; ++i) {
                String sub = i_str.substring(i_str.length() - p_size, i_str.length());
                result.add(0, sub);
                i_str = i_str.substring(0, i_str.length() - p_size);
            }
            if (i_str.length() % p_size > 0) {
                result.add(0, i_str);
            }
        }
        return result;
    }

    public static BLDigit BLMultiplier(int size) {
        char[] zeroes = new char[Math.abs(size)];
        Arrays.fill(zeroes, '0');
        String temp = String.copyValueOf(zeroes);

        String result = "";

        if (size > 0) {
            result = "1" + temp;
        } else {
            result = "0." + temp.substring(0, temp.length() - 1) + "1";
        }
        BLDigit bl_multipler = new BLDigit(new BigDecimal(result));
        return bl_multipler;
    }

    public static BLDigit mergeBLsBeforePoint(List<String> str_portions, int size) {
        System.out.println("mergeBLsBeforePoint(" + str_portions + "," + size + ")");


        BLDigit multiplier = BLMultiplier(size);

        BLDigit merged = BLDigit.ZERO;
        int j = 0;
        for (int i = str_portions.size() - 1; i >= 0; --i) {
            BLDigit zi = new BLDigit(new BigDecimal(str_portions.get(i)));
            j = str_portions.size() - 1 - i;
            while (j > 0) {
                zi = BLDigit.mult(zi, multiplier);

                --j;
            }
            merged = BLDigit.add(merged, zi);
            System.out.println("merged = " + Convertor.toDecimal(merged));

        }

        return merged;
    }
}