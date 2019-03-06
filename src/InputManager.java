/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class PairErrorCodeString {
    public InputManager.ErrorCode return_code;
    public String return_str;
    PairErrorCodeString(){}
    PairErrorCodeString(InputManager.ErrorCode i_code, String i_str){
        return_code = i_code;
        return_str = i_str;
    }
}
public class InputManager
{
    public static final String SUCCESS = "Successful input", UNKNOWN_INPUT_FORMAT = "Unknown input format", INVALID_VALUE = "The value does not match the format ", INVALID_INPUT = "IO Exception";
    public enum ErrorCode {
        SUCCESS, UNKNOWN_INPUT_FORMAT, INVALID_VALUE, INVALID_INPUT, REPEAD
    }
    public static final int DEC = 1, BL = 2, BIG_DEC = 3;
    public enum format{
        DEC, BL, BIG_DEC
    }
    public static ErrorCode getDec(String i_str){
        ErrorCode return_code = ErrorCode.INVALID_VALUE;
        Pattern format_pattern = Pattern.compile("^(-?)([0]{1}|[1-9]{1}[0-9]*)(\\.([0-9]+))?$");
        Matcher matcher = format_pattern.matcher(i_str);
        if(matcher.find()){
            BLDigit bl_from_dec = Convertor.decimalToRl(new BigDecimal(i_str));
            bl_from_dec.show();
            System.out.println(Convertor.toDecimal(bl_from_dec));
            return_code = ErrorCode.SUCCESS;
        }
        return return_code;
    }
    public static ErrorCode getBL(String i_str){ //TO DO 0.14.17.16.15.12.10.6.2.1.0.-1.-5.-7.-8.-9
        ErrorCode return_code = ErrorCode.INVALID_VALUE;
        Pattern format_pattern = Pattern.compile("^(0|1){1}\\.([0]{1}|[1-9]{1}[0-9]*)\\.(\\.?-?[0-9]+)+$");
        Matcher matcher = format_pattern.matcher(i_str);
        BLDigit bl_i = new BLDigit();
        if (matcher.find()) {
            int Q =  Integer.parseInt(matcher.group(2));
            List<String> partition = new ArrayList(Arrays.asList(matcher.group(0).split("\\.")));
            partition = partition.subList(3, partition.size());
            if(Q==partition.size()){
                bl_i = Convertor.stringToRl(i_str);
                return_code = ErrorCode.SUCCESS;
            }
        }
        return return_code;
    }
    public static ErrorCode getBigDec(String i_str){
        ErrorCode return_code = ErrorCode.INVALID_VALUE;
        Pattern format_pattern = Pattern.compile("^(-?)([0]{1}|[1-9]{1}[0-9]*)(\\.([0-9]+))?$");
        Matcher matcher = format_pattern.matcher(i_str);
        if(matcher.find()){
            return_code = ErrorCode.SUCCESS;
        }
        return return_code;
    }
    public static ErrorCode selectFormat(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        ErrorCode result;
        try {
            System.out.println("Select the input format:");
            System.out.println("1 - decimal;\n2 - bit-logarithmic;\n3 - big becimal;");
            String format = reader.readLine();
            switch (Integer.parseInt(format)) {
                case 1: {
                    System.out.println("Input decimal: ");
                    input = reader.readLine();
                    result = getDec(input);
                }
                break;
                case 2: {
                    System.out.println("Input BL: ");
                    input = reader.readLine();
                    result = getBL(input);
                }
                break;
                case 3: {
                    System.out.println("Input big decimal: ");
                    input = reader.readLine();
                    result = getBigDec(input);
                }
                break;
                default: {
                    System.out.println("Unknown input format.");
                    result = ErrorCode.UNKNOWN_INPUT_FORMAT;
                }
            }
            if(result == ErrorCode.SUCCESS) {
                System.out.println("To continue input 'c'");
                char c[]  = reader.readLine().toCharArray();
                System.out.println(c[0]);
                if(c[0]=='c'){
                    result = ErrorCode.REPEAD;
                }
            }
        }
        catch (IOException e){
            return ErrorCode.INVALID_INPUT;
        }
        return result;
    }
    public static ErrorCode getInput(){
        ErrorCode result;
        ErrorCode cont;
        result = selectFormat();
        while (result==ErrorCode.REPEAD)
        {
            System.out.println(result);
            result = selectFormat();
        }
        return result;
    }
}
/*
    static String add(String rl_number1, String rl_number2) {
        StringBuilder builder = new StringBuilder();
        String rate1[] = rl_number1.split("\\.");
        String rate2[] = rl_number2.split("\\.");
        System.out.println(rl_number1 + " + " + rl_number2);
        int i = 0, j = 0;
        for (; i < rate1.length && j < rate2.length; ) {
            if (Integer.parseInt(rate1[i]) > Integer.parseInt(rate2[j]))
                builder.append(rate1[i++]).append('.');
            else if (Integer.parseInt(rate1[i]) < Integer.parseInt(rate2[j]))
                builder.append(rate2[j++]).append('.');
            else {
                builder.append(Integer.parseInt(rate1[i]) + (Integer.parseInt(rate1[i]) > 0 ? 1 : -1)).append('.');
                ++i;
                ++j;
            }
        }
        while (i < rate1.length)
            builder.append(rate1[i++]).append('.');
        while (j < rate2.length)
            builder.append(rate2[j++]).append('.');
        while (hasRepeat(builder.toString()))
            builder = new StringBuilder(replaceRepeat(builder.toString()));
        return builder.toString();
    }
    static List<Integer> sub(BLDigit rl_number1, BLDigit rl_number2) {
        List<Integer> rate1 = new ArrayList(rl_number1.N);
        List<Integer> rate2 = new ArrayList(rl_number2.N);
        int i, j;
        for (; !rate2.isEmpty(); ) {
            i = rate1.size() - 1;
            j = rate2.size() - 1;
            for (; i >= 0; ) {
                if ( rate1.get(i).intValue() >= rate2.get(j).intValue())
                    break;
                else
                    --i;
            }
            if(i < 0) i++;
            if(rate1.isEmpty()) break;
            int rate1_element = rate1.get(i).intValue();
            int rate2_element = rate2.get(j).intValue();
            rate1.remove(i);
            rate2.remove(j);
            if (rate1_element == rate2_element)
                continue;
            while (rate2_element < rate1_element)
                rate1.add(i, rate2_element++);
        }
        //for (String element : rate1)
          //  builder.append(element).append('.');
        while (hasRepeat(rate1))
            rate1 = new ArrayList(replaceRepeat(rate1));
        return rate1;
    }
    */


/*
    static int suggesedCompare(BLDigit rl_number1, BLDigit rl_number2, Integer sugN){
        int compare = 0;
        List<Integer> sugg = new ArrayList();
        sugg.add(sugN);
        if(rl_number2.Q!=0) sugg.addAll(rl_number2.N);
        Collections.sort(sugg, Collections.reverseOrder());
        return compare(rl_number1, new BLDigit(0,sugg.size(),sugg));
    }
    private static int calculateResNumber(BLDigit rl_number1, BLDigit rl_number2){
        Integer value = 0;
        if(compare(rl_number1,rl_number2) >= 0) {
            for(int i = 1; i < precision*2; i++){
                if(suggesedCompare(rl_number1, rl_number2, i)<=0){
                    value++;
                }
            }
        }
        else{
            for(int i = 0; i > -precision*2; i--) {
                if(suggesedCompare(rl_number1, rl_number2, i)==1){
                    value--;
                }
            }
        }
        return value;
    }
    static BLDigit dev(BLDigit rl_number1, BLDigit rl_number2) {
        System.out.println("rl_number1: "+ rl_number1);
        System.out.println("rl_number2: "+ rl_number2+"\n");
        int res_sign = rl_number1.sign*rl_number2.sign;
        BLDigit minuend = rl_number1;
        List<Integer> result_list = rl_number2.N;
        BLDigit result = rl_number2;
        int i = 5;
        while(i>=0){//minuend.Q!=0){
            int value = calculateResNumber(minuend, result);
            System.out.println("value = "+value);
            result_list.add(value);
            Collections.sort(result_list, Collections.reverseOrder());
            result =  new BLDigit(res_sign, result_list.size(), result_list);
            System.out.println("minuend: "+ minuend);
            System.out.println("result: "+ result);
            minuend = plus(minuend,  new BLDigit((res_sign==0)?1:0, result_list.size(), result_list));
            System.out.println("minuend = minuend - result =\n"+minuend+"\n");
            --i;
            if(minuend.Q==0)break;
        }
        return result;
    }
*/
