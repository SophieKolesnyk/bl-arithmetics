import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BLDigit {

    public int sign;
    public int Q;
    public List<Integer> N;
    public static int precision = 20;

    BLDigit(){}                 //Default constructor

    BLDigit(BLDigit i_bl) {     //Copy constructorytr
        this.sign = i_bl.sign;
        this.Q = i_bl.Q;
        this.N = i_bl.N;
    }

    BLDigit(int i_sign, int i_Q, List<Integer> i_N) {
        this.sign = i_sign;
        this.Q = i_Q;
        this.N = i_N;
    }

    //Construct from decimal
    BLDigit(BigDecimal i_dec) {
        BLDigit temp = Convertor.decimalToRl(i_dec);
        this.sign = temp.sign;
        this.Q = temp.Q;
        this.N = temp.N;
    }

    static final BLDigit ZERO = new BLDigit(0, 0, Collections.emptyList());

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.sign);
        result.append("." + this.Q);

        for (int i = 0; i<this.Q; ++i)
            result.append("."+this.N.get(i));

        return result.toString();
    }

    public void show()
    {
        System.out.println(this.toString());
    }

    public static int compare(BLDigit term_1, BLDigit term_2) {
        int result = 0;

        if((term_1.Q == 0)&&(term_2.Q == 0))
            return 0;

        if((term_1.Q != 0)^(term_2.Q != 0)) result = (term_1.Q == 0) ? -1 : 1;
        else {
            int i = 0;
            int j = 0;

            while ((j < term_2.Q)||(i < term_1.Q))
            {
                if((j >= term_2.Q)^(i >= term_1.Q))
                {
                    result = (i < term_1.Q)?-1:1;
                    break;
                }

                int t1i = term_1.N.get(i);
                int t2j = term_2.N.get(j);

                result = (t1i > t2j)? 1:((t1i < t2j)?-1: 0);

                if(result!=0) break;

                ++i;
                ++j;
            }
        }
        return result;
    }



    private static boolean hasRepeat(List<Integer> i_list)
    {
        Collections.sort(i_list, Collections.reverseOrder());
        for (int i = 0, j = 1; j < i_list.size()-1; ++i, ++j) {
            if (i_list.get(i).equals(i_list.get(j))) return true;
        }

        return false;
    }

    private static List<Integer> removeRepeat(List<Integer> i_list) {
        Collections.sort(i_list, Collections.reverseOrder());

        List<Integer> result = new ArrayList();

        int i = i_list.size()-1;
        for (; i>0; ) {

            if (i_list.get(i).equals(i_list.get(i-1))){
                result.add(i_list.get(i) + 1);
                i-=2;
            }
            else {
                result.add(i_list.get(i--));
            }
        }

        if(i==0) {
            result.add(i_list.get(i));
        }

        return result;
    }

    public static BLDigit add(BLDigit bl1, BLDigit bl2) {

        List<Integer> result = new ArrayList();

        int i1 = 0, j2 = 0;
        int comparison;
        Integer res_i = new Integer(0);


        for(; i1<bl1.N.size()&&j2<bl2.N.size() ; )
        {
            comparison = Integer.compare(bl1.N.get(i1), bl2.N.get(j2));

            if (comparison!=0)
            {
                if (comparison<0)
                    res_i = bl1.N.get(i1++);
                else
                    res_i = bl2.N.get(j2++);
            }
            else
            {
                res_i = bl1.N.get(i1) + 1;
                ++i1;
                ++j2;
            }
            result.add(res_i);
        }

        while (i1<bl1.N.size()) result.add(bl1.N.get(i1++));

        while (j2<bl2.N.size()) result.add(bl2.N.get(j2++));

        if (hasRepeat(result)) removeRepeat(result);

        return new BLDigit(bl1.sign, result.size(), result);
    }

    public static BLDigit mult(BLDigit bl1, BLDigit bl2) {
        List<Integer> result = new ArrayList();

        List<Integer> term_1 = bl1.N;
        List<Integer> term_2 = bl2.N;


        for (int i = 0; i < term_1.size(); ++i) {
            for (int j = 0; j < term_2.size(); ++j){
                Integer sum_ij = term_1.get(i) + term_2.get(j);
                result.add(sum_ij);
            }
        }

        while (hasRepeat(result))
            result = new ArrayList(removeRepeat(result));

        int diff_sign = bl1.sign*bl2.sign;

        return new BLDigit(bl1.sign, result.size(), result);
    }

}

