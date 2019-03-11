import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInput {

    public static List<String> splitToPortions(String i_str, int p_size) {
        List<String> result = new ArrayList();
        int iter_numb = 0;

        if (p_size < 0) {
            p_size = Math.abs(p_size);
            iter_numb = i_str.length() / p_size;
            for (int i = 0; i < iter_numb; ++i) {
                result.add(i_str.substring(0, p_size));
                i_str = i_str.substring(p_size, i_str.length());
            }

        } else {
            iter_numb = i_str.length() / p_size;

            for (int i = iter_numb - 1; i >= 0; --i) {
                result.add(i_str.substring(i_str.length() - p_size, i_str.length()));
                i_str = i_str.substring(0, i_str.length() - p_size);
            }
        }

        if (i_str.length() % p_size > 0) {
            result.add(i_str);
        }
        return result;
    }

    public static List<BLDigit> portiosToBLs(String i_str, int p_size) {
        List<String> portions = splitToPortions(i_str, p_size);
        List<BLDigit> bls = new ArrayList();
        for (int i = 0; i < portions.size(); ++i) {
            BigDecimal dec_str = (p_size > 0) ? (new BigDecimal(portions.get(i))) : (new BigDecimal("0." + portions.get(i)));
            BLDigit bl = Convertor.fromDecimal(dec_str);
            bls.add(bl);
        }

        return bls;
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
        BLDigit multiplier = Convertor.fromDecimal(new BigDecimal(result));

        return multiplier;
    }

    public static BLDigit mergeBLsBeforePoint(String i_str, int p_size) {
        List<BLDigit> bl_portions = portiosToBLs(i_str, p_size);
        BLDigit merge = BLDigit.ZERO;
        BLDigit multiplier = BLMultiplier(p_size);
        int j = 0;
        for (int i = bl_portions.size() - 1; i >= 0; --i) {
            BLDigit zi = bl_portions.get(i);
            j = i;
            while (j > 0) {
                zi = BLDigit.mult(zi, multiplier);
                --j;
            }
            merge = BLDigit.add(merge, zi);
        }

        return merge;
    }


    public static BLDigit mergeBLsAfterPoint(String i_str, int p_size) {
        List<BLDigit> bl_portions = portiosToBLs(i_str, p_size);
        BLDigit merge = BLDigit.ZERO;
        BLDigit multiplier = BLMultiplier(p_size);
        BLDigit sum_multiplier = BLMultiplier(p_size);

        int j = 0;
        for (int i = 0; i < bl_portions.size(); ++i) {
            BLDigit zi = bl_portions.get(i);
            j = i - 1;
            while (j > 0) {
                multiplier = BLDigit.mult(sum_multiplier, multiplier);
                --j;
            }
            if (i > 0) {
                zi = BLDigit.mult(zi, multiplier);
            }

            merge = BLDigit.add(merge, zi);
        }

        return merge;
    }


    public static BLDigit HugeDecToBL(String i_str) {
        BLDigit result = BLDigit.ZERO;

        Pattern pattern = Pattern.compile("^(-?([0]{1}|[1-9]{1}[0-9]*)){1}(\\.([0-9]+))?$");
        Matcher matcher = pattern.matcher(i_str);

        if (matcher.find()) {
            int p_size = 0;
            if (matcher.group(1) != null) {
                String bp_str = matcher.group(1);
                p_size = (bp_str.length() < 3) ? bp_str.length() : 3;
                BLDigit before_point = mergeBLsBeforePoint(bp_str, p_size);
                result = before_point;
            }

            if (matcher.group(4) != null) {
                String ap_str = matcher.group(4);
                p_size = -((ap_str.length() < 3) ? ap_str.length() : 3);
                BLDigit after_point = mergeBLsAfterPoint(ap_str, p_size);
                result = BLDigit.add(result, after_point);
            }
        } else
                System.out.println("Invalid input number.");

        return result;

    }
}