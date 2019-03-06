import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import ch.obermuhlner.math.big.BigDecimalMath;
import java.util.List;



public class Convertor {


    public static List<Integer> intPartList(BigInteger int_part) {
        String bin_str = int_part.toString(2);
        char[] bin_char = bin_str.toCharArray();

        List<Integer> result = new ArrayList();
        for(int i = 0; i<bin_str.length(); ++i) {
            if(bin_char[i] == '1')
                result.add(bin_str.length()-i-1);
        }
        return  result;
    }

    private static List<Integer> fractPartList(BigDecimal fraction_part) {
        MathContext context = context = new MathContext(BLDigit.precision);

        List<Integer> result = new ArrayList();

        BigDecimal zero = new BigDecimal("0.0");

        int bit_position = 0;
        BigDecimal next_val = fraction_part;

        while ((Math.abs(bit_position) != BLDigit.precision)) {
            if(next_val.compareTo(zero)==0)
                break;

            int bit = fraction_part.intValue();
            if (bit == 1)
                result.add(bit_position);
            next_val = fraction_part.subtract(new BigDecimal(bit), context).multiply(new BigDecimal("2"));
            fraction_part = next_val;
            --bit_position;
        }
        return result;
    }


    static List<Integer> DecimalToIndexesList(BigDecimal u_digit)
    {
        List<Integer> result = new ArrayList();

        if(u_digit.toString().contains(".")){
            String[] partition = u_digit.toString().split("\\.");
            result.addAll(intPartList(new BigInteger(partition[0])));
            result.addAll(fractPartList(new BigDecimal("0." + partition[1])));
        }
        else
            result.addAll(intPartList(u_digit.toBigInteger()));

        return result;
    }


    public static BLDigit decimalToRl(BigDecimal i_digit) {
        BLDigit result = new BLDigit();
        result.sign = (i_digit.compareTo(BigDecimal.ZERO) < 0)?1:0;
        result.N = DecimalToIndexesList(i_digit);
        result.Q = result.N.size();
        return result;
    }

    public static BLDigit integerToRl(Integer i_digit) {
        List<Integer> result = intPartList(BigInteger.valueOf(i_digit));
        Collections.sort(result, Collections.reverseOrder());
        BLDigit result_bl = new BLDigit((i_digit > 0) ? 0 : 1, result.size(), result);
        return result_bl;
    }

    public static BigDecimal toDecimal(BLDigit i_bl) {

        BigDecimal result = new BigDecimal("0.0");
        BigDecimal base = new BigDecimal("2.0");

        for (int i = 0; i < i_bl.Q; ++i)
            result = result.add(BigDecimalMath.pow(base, i_bl.N.get(i), new MathContext(BLDigit.precision)));

        return result;
    }

}

