import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    public static long precision = 60;

    public static BLDigit toBLDigit(String i_digit){
        if (i_digit.length() > 10)
            return HugeDecToBL(i_digit);

        BigDecimal decimal = new BigDecimal(i_digit);
        return BLfromDecimal(decimal);
    }

    public static BLDigit toBLDigit(BigDecimal i_digit) {
        return BLfromDecimal(i_digit);
    }

    public static BLDigit toBLDigit(Integer i_digit) {

        if (i_digit == 0) return BLDigit.ZERO;
        BLDigit result = new BLDigit();

        List<Integer> N = intPartList(i_digit.toString());
        result.sign = (i_digit.toString().substring(0,1)=="-")?1:0;
        result.Q = N.size();
        result.N = N;

        return result;
    }

    public static BigDecimal toDecimal(BLDigit i_bl) {
        Collections.sort(i_bl.N, Collections.reverseOrder());
        BigDecimal int_part = new BigDecimal("0.0");
        BigDecimal fract_part = new BigDecimal("0.0");
        BigDecimal base = new BigDecimal("2.0");

        int i = 0;
        while ((i<i_bl.Q)&&(i_bl.N.get(i)>=0)) {
            BigDecimal pow = BigDecimalMath.pow(base, i_bl.N.get(i), new MathContext(0,RoundingMode.HALF_UP));
            int_part = int_part.add(pow);
            ++i;
        }
        if (i<i_bl.Q) {
            int max_after_point = ((i_bl.Q-i)>500)?500:(i_bl.Q-i);
            while (i<max_after_point) {
                BigDecimal pow = BigDecimalMath.pow(base, i_bl.N.get(i), new MathContext(500));
                fract_part = fract_part.add(pow);
                ++i;
            }
            fract_part = new BigDecimal(fract_part.toString(), new MathContext((int)(precision/10), RoundingMode.HALF_UP));
        }


        return cutZeros(int_part.add(fract_part));
    }

    private static BLDigit HugeDecToBL(String i_str) {
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
                result = Calculator.add(result, after_point);
            }
            List<Integer> formated = new ArrayList();
            formated.addAll(result.N);
            while (Calculator.hasRepeat(formated)){
                formated = Calculator.removeRepeat(formated);
            }
            result = new BLDigit(result.sign, formated.size(), formated);
        } else
            System.out.println("Invalid input number.");

        return result;

    }

    private static BLDigit BLfromDecimal(BigDecimal i_digit) {
        if (i_digit.compareTo(new BigDecimal("0.0")) == 0) return BLDigit.ZERO;
        BLDigit result = new BLDigit();

        List<Integer> N = new ArrayList();
        String str_digit = i_digit.toString();
        if(str_digit.contains(".")){
            String[] partition = str_digit.split("\\.");
            N.addAll(intPartList(partition[0]));
//            precision *= partition[1].length();
            N.addAll(fractPartList( partition[1], precision));
        }
        else
            N.addAll(intPartList(str_digit));

        result.sign = (i_digit.toString().substring(0,1)=="-")?1:0;
        result.Q = N.size();
        result.N = N;

        return result;
    }

    private static List<Integer> intPartList(String int_part) {
        List<Integer> result = new ArrayList();
        if(int_part=="0") return result;

        String bin_str = new BigInteger(int_part).toString(2);
        char[] bin_char = bin_str.toCharArray();
        for(int i = 0; i<bin_str.length(); ++i) {
            if(bin_char[i] == '1')
                result.add(bin_str.length()-i-1);
        }
        return  result;
    }

    private static List<Integer> fractPartList(String fraction_part, long precision) {
        List<Integer> result = new ArrayList();

        if(fraction_part=="0.0") return result;
        MathContext context = new MathContext((int)(precision));
        BigDecimal dec_fract = new BigDecimal("0."+ fraction_part);

        if(fraction_part == "0.0") return result;

        int bit_position = 0;
        BigDecimal next_val = dec_fract;
        BigDecimal zero = new BigDecimal("0.0");
        BigDecimal base = new BigDecimal("2.0");

        while ((Math.abs(bit_position) != precision)) {
            if(next_val.compareTo(zero)==0)
                break;

            int bit = dec_fract.intValue();

            if (bit == 1)
                result.add(bit_position);
            next_val = dec_fract.subtract(new BigDecimal(bit), context).multiply(base);
            dec_fract = next_val;
            --bit_position;
        }
        return result;
    }

    private static BigDecimal cutZeros(BigDecimal i_dec) {
        String str_dec = "";
        Pattern pattern = Pattern.compile("^([0-9]+[\\.]([0-9]*[1-9]{1}|[^0-9]*))[0]+$");
        Matcher matcher = pattern.matcher(i_dec.toString());
        if (matcher.find())

            str_dec = matcher.group(1);
        else
            str_dec = i_dec.toString();

        return  new BigDecimal(str_dec);
    }

    private static List<String> splitToPortions(String i_str, int p_size) {
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

    private static List<BLDigit> portiosToBLs(String i_str, int p_size) {
        List<String> portions = splitToPortions(i_str, p_size);
        List<BLDigit> bls = new ArrayList();
        for (int i = 0; i < portions.size(); ++i) {
            BLDigit bl = (p_size > 0) ? toBLDigit(portions.get(i)): toBLDigit("0." + portions.get(i));
            bls.add(bl);
         //   System.out.println("precision = " + precision + "\n" + portions.get(i) + "\n" + bl);
        }
        return bls;
    }

    private static BLDigit BLMultiplier(int size) {
        char[] zeroes = new char[Math.abs(size)];
        Arrays.fill(zeroes, '0');
        String temp = String.copyValueOf(zeroes);

        String result = "";
        if (size > 0) {
            result = "1" + temp;
        } else {
            result = "0." + temp.substring(0, temp.length() - 1) + "1";
        }
        BLDigit multiplier = toBLDigit(result);

        return multiplier;
    }

    private static BLDigit mergeBLsBeforePoint(String i_str, int p_size) {
        List<BLDigit> bl_portions = portiosToBLs(i_str, p_size);
        BLDigit merge = BLDigit.ZERO;
        BLDigit multiplier = BLMultiplier(p_size);
        int j = 0;
        for (int i = bl_portions.size() - 1; i >= 0; --i) {
            BLDigit zi = bl_portions.get(i);
            j = i;
            while (j > 0) {
                zi = Calculator.mult(zi, multiplier);
                --j;
            }
            merge = Calculator.add(merge, zi);
        }

        return merge;
    }

    private static BLDigit mergeBLsAfterPoint(String i_str, int p_size) {
        List<BLDigit> bl_portions = portiosToBLs(i_str, p_size);
        BLDigit merge = BLDigit.ZERO;
        BLDigit multiplier = BLMultiplier(p_size);
        BLDigit sum_multiplier = BLMultiplier(p_size);

        int j = 0;
        for (int i = 0; i < bl_portions.size(); ++i) {
            BLDigit zi = bl_portions.get(i);
            j = i - 1;
            while (j > 0) {
                multiplier = Calculator.mult(sum_multiplier, multiplier);
                --j;
            }
            if (i > 0) {
                zi = Calculator.mult(zi, multiplier);
            }

            merge = Calculator.add(merge, zi);
        }

        return merge;
    }

}
