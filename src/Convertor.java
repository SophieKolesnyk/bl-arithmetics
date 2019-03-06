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

    private static List<Integer> fractPartList(BigDecimal fract_part) {
        MathContext context = context = new MathContext(BLDigit.precision);

        List<Integer> result = new ArrayList();

        int bit_position = 0;
        BigDecimal next_val = fract_part;

        while ((Math.abs(bit_position) != BLDigit.precision)) {
            if(next_val.compareTo(BigDecimal.ZERO)==0)
                break;

            int bit = fract_part.intValue();
            if (bit == 1)
                result.add(bit_position);
            next_val = fract_part.subtract(new BigDecimal(bit), context).multiply(new BigDecimal("2"));
            fract_part = next_val;
            --bit_position;
        }
        return result;
    }

    private static List<Integer> DecimalToIndexesList(BigDecimal u_digit) {
        List<Integer> result = new ArrayList();
        String part[] = u_digit.toString().split("\\.");

        boolean isFract = (u_digit.toString().contains("."))?((part[1]!="")?true:false):false;
        Integer int_val =  new Integer(part[0]);

        List<Integer> int_list = intPartList(BigInteger.valueOf(int_val));
        result = int_list;

        if(isFract){
            List<Integer> fract_list = fractPartList(new BigDecimal("0."+part[1]));
            result.addAll(fract_list);
        }

        Collections.sort(result, Collections.reverseOrder());
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
