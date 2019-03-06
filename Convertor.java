import ch.obermuhlner.math.big.BigDecimalMath;
//.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

class Convertor {

    private static final long THRESHOLD = 1; //Лимит на знаки после запятой

    static String reverseRL(String rl_number) {
        StringBuilder builder = new StringBuilder();

        String rate[] = rl_number.split("\\.");
        for (int i = rate.length - 1; i > 0; --i)
            builder.append(rate[i]).append('.');

        return builder.toString();
    }

    static boolean isBigger(String rl_number1, String rl_number2) {
        return (Integer.parseInt(rl_number1.split("\\.")[0]) > Integer.parseInt(rl_number2.split("\\.")[0]));
    }

    static String convertToBinary(BigInteger number) {
        return number.toString(2);
    }

    static String convertToBinary(String rl_number) {
        StringBuilder builder = new StringBuilder();

        String rank[] = rl_number.split("\\.");
        int len = Integer.valueOf(rank[0]);

        for (int i = len, j = 0; i >= 0; --i) {
            if (j < rank.length && rank[j].equals(String.valueOf(i))) {
                builder.append(1);
                ++j;
            }
            else
                builder.append(0);
        }
        return builder.toString();
    }

    static BigInteger convertFromBinary(String binary) {
        String[] split = binary.split(".");
        if(split.length < 1)
            return new BigInteger(binary, 2);
        else{
            String temp = new BigDecimal(decToBin(new BigDecimal(split[0]))).toString()
                    + new BigDecimal(decToBinPoint(new BigDecimal(split[1]), THRESHOLD));
            return new BigInteger(temp);
        }
    }

    static String convertToRL(BigInteger number) {
        StringBuilder builder = new StringBuilder();

        String numberInBinary = convertToBinary(number);

        int len = numberInBinary.length();
        for (int i = 0; i < len; ++i) {
            if (numberInBinary.charAt(i) == '1')
                builder.append(len - i - 1).append('.');
        }

        return builder.toString();
    }

    static ArrayList<Integer> convertToArrayOfIntegers(String rl_number) {
        ArrayList<Integer> result = new ArrayList<>();

        String arr[] = rl_number.split("\\.");
        for (String num :
                arr) {
            result.add(Integer.parseInt(num));
        }

        return result;
    }

    public static String decimalFloatingToRl(BigDecimal x)
    {
        String res = "";

        //Convert the sign into RL form
        //(x==0)?0:(x>0)?1:(-1)
        if(x.compareTo(BigDecimal.ZERO) < 0)//(x<0)
            res += "1.";
        else
            res += "0.";


        BigDecimal x1 = x.abs();//cut the sign

        //Split the integer and the fractal parts of the number
        String[] split = x1.toString().split("\\.");

        String beforePoint = split[0];//the integer part
        if(split.length > 1) {
            int ones = countOnes(decToBin(new BigDecimal(beforePoint))) + countOnes(decToBinPoint(new BigDecimal(split[1]), THRESHOLD));
            res = res + ones + ".";
        }else
            res = res + countOnes(decToBin(new BigDecimal(beforePoint))) + ".";
        res += processBeforePoint(beforePoint);

        try {
            String afterPoint = split[1];
            res += precessAfterPoint(afterPoint);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }

        return res;
    }

    private static String decToBinPoint(BigDecimal bigDecimal, long threshhold/*точность после запятой*/) {
        BigDecimal multiplier = new BigDecimal(2);
        String res = "";
        String withZero = "0." + bigDecimal.toString();
        BigDecimal temp = new BigDecimal(withZero);
        for(long i = 0; i < threshhold; i++) {
            if(temp.compareTo(BigDecimal.ZERO) == 0)
                return res;
            temp = temp.multiply(multiplier);
            if (temp.compareTo(BigDecimal.ONE) >= 0) {
                res += 1;
            } else {
                res += 0;
            }
            try {
                temp = new BigDecimal("0." + temp.toString().split("\\.")[1]);
            } catch (Exception ex){
                return res;
            }
        }
        return res;
    }

    private static String precessAfterPoint(String afterPoint) {
        String res = "";
        char[] stringToCharArray = decToBinPoint(new BigDecimal(afterPoint), THRESHOLD).toCharArray();
        for(int i = 0; i < stringToCharArray.length; i++){
            if(stringToCharArray[i] == '1'){
                res = res + -(i + 1) + ".";
            }
        }
        return res;
    }

    private static String processBeforePoint(String beforePoint) {
        String res = "";
        char[] stringToCharArray = decToBin(new BigDecimal(beforePoint)).toCharArray();
        for(int i = 0; i < stringToCharArray.length; i++){
            if(stringToCharArray[i]== '1')
                res = res + (stringToCharArray.length - i - 1) + ".";
        }
        return res;
    }

    private static String decToBin(BigDecimal num) {
        BigDecimal divisor = new BigDecimal(2);
        String bin = "";
        StringBuilder sb = new StringBuilder();
        while(num.compareTo(divisor) >= 0)
        {
            BigDecimal rem = num.remainder(divisor);
            sb.insert(0,rem.toString());
            //			bin = rem.toString() + bin;
            num = num.divide(divisor, BigDecimal.ROUND_DOWN);
            //			if(num.compareTo(divisor) == 0)
            //				sb.insert(0,"0");
            if(num.compareTo(divisor) < 0)
                //				sb.insert(0,rem);
                sb.insert(0,"1");
            //				bin = "1"+bin;
        }
        //        System.out.println(countOnes(bin));
        return sb.toString();
    }

    private static int countOnes(String s){
        int ones = s.replaceAll("0", "").length();
        return ones;
    }

    static BigDecimal toDecimalFromRl(String rl){
        BigDecimal value2 = new BigDecimal(2);
        BigDecimal answer = new BigDecimal(0);
        //		String[] split = rl.split("\\.");
        //		String[] res = new String[split.length-2];
        //		System.arraycopy(split,2,res,0,split.length - 2);
        String[] res = rl.split("\\.");
        for(String s:res){
            answer = answer.add(BigDecimalMath.pow(value2, Long.parseLong(s),MathContext.DECIMAL64));
        }
        return answer;
    }

}