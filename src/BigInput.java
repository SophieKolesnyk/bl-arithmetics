import java.util.ArrayList;
import java.util.List;


public class BigInput {



    public static List<String> splitToPortions(String i_str, int p_size) {
        List<String> result = new ArrayList();
        System.out.println("input to splitToPortions " + i_str);

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
        System.out.println("Output from splitToPortions " + result.toString());
        return result;
    }


    public static List<BLDigit> strToBLPortions(String i_str, int portion_size) {
        List<String> portions = splitToPortions(i_str, portion_size);
        List<BLDigit> result = new ArrayList();

        for (int i = 0; i < portions.size(); ++i) {
            BLDigit rl_port = Convertor.integerToRl(Integer.parseInt(portions.get(i)));
            result.add(rl_port);
            System.out.println(portions.get(i) + " -> " + rl_port);
        }
        return result;
    }
}