import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BLDigit {

    public int sign = 0;
    public int Q = 0;
    public List<Integer> N;
    public int precision = 20;

    /*------------------------Constructors------------------------*/
    BLDigit() {    }

    BLDigit(int init_precision) {
        this.precision = init_precision;
    }

    BLDigit(BLDigit i_bl) {
        this.sign = i_bl.sign;
        this.Q = i_bl.Q;
        this.N = i_bl.N;
        this.precision = i_bl.precision;
    }

    BLDigit(int i_sign, int i_Q, List<Integer> i_N) {
        this.sign = i_sign;
        this.Q = i_Q;
        this.N = i_N;
    }


    public static final BLDigit ZERO = new BLDigit(0, 0, Collections.emptyList());

    public static BLDigit add(BLDigit bl1, BLDigit bl2) {
        int res_prec =(bl1.precision > bl2.precision)? bl1.precision : bl2.precision;
        BLDigit result = new BLDigit(res_prec);
        List<Integer> result_list = new ArrayList();

        int bl1_to_zero = BLDigit.compare(bl1, BLDigit.ZERO);
        int bl2_to_zero = BLDigit.compare(bl2, BLDigit.ZERO);

        if ((bl1_to_zero==0)||(bl2_to_zero==0))
            if ((bl1_to_zero == 0) ^ (bl2_to_zero == 0)) {
                if ((bl1_to_zero == 0)) result = bl2;
                else result = bl1;
            }
            else result = BLDigit.ZERO;
        else
        {
            int i1 = 0, j2 = 0;
            int comparison;
            Integer res_i = 0;

            for(; i1<bl1.N.size()&&j2<bl2.N.size() ; )
            {
                comparison = Integer.compare(bl1.N.get(i1), bl2.N.get(j2));

                if (comparison!=0)
                {
                    if (comparison>0)
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

                result_list.add(res_i);

            }

            while (i1<bl1.N.size()) result_list.add(bl1.N.get(i1++));
            while (j2<bl2.N.size()) result_list.add(bl2.N.get(j2++));

            while (hasRepeat(result_list)) result_list = removeRepeat(result_list);
            result.sign = bl1.sign;
            result.Q = result_list.size();
            result.N = result_list;


        }

        return result;
    }

    public static BLDigit mult(BLDigit bl1, BLDigit bl2) {
        List<Integer> result_list = new ArrayList();
        List<Integer> term_1 = bl1.N;
        List<Integer> term_2 = bl2.N;
        int res_prec = bl1.precision+bl2.precision;
        BLDigit result = new BLDigit(res_prec);

        for (int i = 0; i < term_1.size(); ++i) {
            for (int j = 0; j < term_2.size(); ++j){
                Integer sum_ij = term_1.get(i) + term_2.get(j);
                result_list.add(sum_ij);
            }
        }

        while (hasRepeat(result_list))
            result_list = new ArrayList(removeRepeat(result_list));
        result.sign =(bl1.sign*bl2.sign);
        result.Q = result_list.size();
        result.N = result_list;

        return result;
    }



    public List<Pair> selectPairs(int H) {
        List<Pair> result = new ArrayList();
        int h = 0;
            for (int i = 0; i < this.Q; ++i)
                for (int j = i + 1; j < this.Q; ++j)
                {
                    if (result.size()==H) break;
                    if (i != j)
                    {
                        Pair rows = new Pair(i,j);
                        if (!Pair.isContainEqual(result, rows)) {
                            result.add(rows);
                            System.out.println(rows.first + ", "+ rows.second);
                            ++h;
                        }
                    }
                }

        return result;
    }

    public BLDigit singlesSum(int H) {
        List<Integer> N = new ArrayList();
        for (int i = 0; i < H; ++i) {
            N.add(this.N.get(i)*2);
        }
        return new BLDigit(this.sign, N.size(), N);
    }

    public BLDigit calculateDoubleSub(Pair matrix) {
        BLDigit result = Convertor.fromString("2");
        int additor = this.N.get(matrix.first) + this.N.get(matrix.second);
        System.out.println(additor);
        for (int i = 0; i < result.Q; i++) {
            result.N.set(i,(additor+result.N.get(i)));
        }

        return result;
    }

    public BLDigit doubleSubSum(int H) {
        List<Pair> pairs = selectPairs(H);
        BLDigit result = BLDigit.ZERO;
        for (Pair pair : pairs) {
            BLDigit additor = calculateDoubleSub(pair);
            result = BLDigit.add(result, additor);
        }
        return result;
    }

    public int submatrixNumber(int k) {
        BigDecimal Q_fact = BigDecimalMath.factorial(this.Q);
        int difference = this.Q - k;
        BigDecimal deductor = BigDecimalMath.factorial(k).multiply(BigDecimalMath.factorial(difference));
        BigDecimal H = Q_fact.divide(deductor);
        return H.intValue();
    }

    public static BLDigit square(BLDigit bl1) {
        BLDigit result = BLDigit.ZERO;
        int singles_numb = bl1.submatrixNumber(1);
        int doubles_numb = bl1.submatrixNumber(2);
        if (singles_numb>0)
            result = bl1.singlesSum(singles_numb);

        if (doubles_numb >0)
            result = BLDigit.add(result, bl1.doubleSubSum(doubles_numb));
        return result;
    }


/*
    public static BLDigit sqrt(BLDigit under_root) {

    }

    public static PrivateKeys getPrivateKeys(BLDigit publik_key) {
        BLDigit P1 = sqrt(publik_key);
    }
*/
    public static int compare(BLDigit term_1, BLDigit term_2) {
        int result = 0;


        if((term_1.Q == 0)||(term_2.Q == 0))
        {
            if((term_1.Q != 0)^(term_2.Q != 0))
                result = (term_1.Q == 0) ? -1 : 1;
            else
                result = 0;

        }
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

    private static boolean hasRepeat(List<Integer> i_list)  {
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


/*

    public  class PrivateKeys {
        BLDigit q;
        BLDigit p;

        CodeKeys(){}
    }
*/
}

class Pair {
    public int first;
    public int second;

    Pair(int i_first, int i_second) {
        this.first = i_first;
        this.second = i_second;
    }

    Pair(Pair i_pair) {
        this.first = i_pair.first;
        this.second = i_pair.second;
    }

    public static boolean isEqual(Pair pair1, Pair pair2) {
        if((pair1.first==pair2.second)||(pair1.first==pair2.first))
            if ((pair1.second==pair2.second)||(pair1.second==pair2.first))
                return true;
        return false;

    }

    public static boolean isContainEqual(List<Pair> i_list, Pair i_pair) {
        for (Pair pair: i_list) {
            if (isEqual(pair, i_pair))
                return true;
        }
        return false;
    }

}