import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    public static BigDecimal cutZeros(BigDecimal i_dec) {
        String str_dec = "";
        Pattern pattern = Pattern.compile("^([0-9]+[\\.]([0-9]*[1-9]{1}|[^0-9]*))[0]+$");
        Matcher matcher = pattern.matcher(i_dec.toString());
        if (matcher.find())

            str_dec = matcher.group(1);
        else
            str_dec = i_dec.toString();

        return  new BigDecimal(str_dec);
    }

    public static BLDigit fromDecimal(BigDecimal i_digit) {
        if (i_digit.compareTo(new BigDecimal("0.0")) == 0) return BLDigit.ZERO;
        BLDigit result = new BLDigit();

        List<Integer> N = new ArrayList();
        String str_digit = i_digit.toString();
        if(str_digit.contains(".")){
            String[] partition = str_digit.split("\\.");
            N.addAll(intPartList(partition[0]));
            result.precision *= partition[1].length();
            N.addAll(fractPartList( partition[1], result.precision));
        }
        else
            N.addAll(intPartList(str_digit));

        result.sign = (i_digit.toString().substring(0,1)=="-")?1:0;
        result.Q = N.size();
        result.N = N;

        return result;
    }

    public static BLDigit fromString(String i_digit){
        if (i_digit.length() > 10)
            return BigInput.HugeDecToBL(i_digit);

        BigDecimal decimal = new BigDecimal(i_digit);
        return fromDecimal(decimal);
    }

    public static List<Integer> intPartList(String int_part) {
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

    private static List<Integer> fractPartList(String fraction_part, int precision) {
        List<Integer> result = new ArrayList();

        if(fraction_part=="0.0") return result;
        MathContext context = new MathContext(precision);
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

    public static BigDecimal toDecimal(BLDigit i_bl) {
        Collections.sort(i_bl.N, Collections.reverseOrder());
        BigDecimal result = new BigDecimal("0.0");
        BigDecimal base = new BigDecimal("2.0");

        for (int i = 0; i < i_bl.Q; ) {
            BigDecimal pow = BigDecimalMath.pow(base, i_bl.N.get(i), new MathContext(i_bl.precision));
            result = result.add(pow);
            ++i;
        }
        result = result.setScale(i_bl.precision/20, RoundingMode.HALF_UP);
        return cutZeros(result);
    }

}
