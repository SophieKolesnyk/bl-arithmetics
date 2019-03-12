import ch.obermuhlner.math.big.BigDecimalMath;
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
        BLDigit result = new BLDigit();
        if (bl1.sign!=bl2.sign) {
            result =  BLDigit.sub(bl1, bl2);
        }
        else {
            int res_prec =(bl1.precision > bl2.precision)? bl1.precision : bl2.precision;
            result = new BLDigit(res_prec);
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
        }
        return result;
    }

    static BLDigit sub(BLDigit term_1, BLDigit term_2) {
     //   System.out.println(term_1.toString()+" - "+term_2.toString());
        BLDigit result = new BLDigit();
        int compare_res = BLDigit.compare(term_1, term_2);
        int sign_resolve = signResolving(term_1.sign, term_2.sign, 1, compare_res);
        if(sign_resolve==0) {
            result = add(term_1, term_2);
        }
        else {
            if(compare_res==0)
                return new BLDigit(term_1.sign, 0, Collections.emptyList());

            List<Integer> decreasing = new ArrayList();
            List<Integer> deduction = new ArrayList();
            int res_sign = 0;
            if(sign_resolve == 1) {
                res_sign =term_1.sign;
                decreasing.addAll(term_1.N);
                deduction.addAll(term_2.N);
            }
            else {
                res_sign = 1;
                decreasing.addAll(term_2.N);
                deduction.addAll(term_1.N);
            }
            Collections.sort(decreasing, Collections.reverseOrder());
            Collections.sort(deduction, Collections.reverseOrder());

            List<Integer> N = new ArrayList();


            int j = deduction.size()-1;
            while ((j>=0)&&(!decreasing.isEmpty())){
                int i = decreasing.size()-1;
                if (decreasing.get(i)!=deduction.get(j))
                {

                    if (decreasing.get(i)<deduction.get(j)) {
                        N.add(decreasing.get(i));

                        ++j;
                    }
                    else {
                        int Ni = decreasing.get(i);
                        int Nj = deduction.get(j);
                        while (Nj<Ni)
                            N.add(Nj++);
                    }
                }

                --j;
                decreasing.remove(i);
            }
            if (!decreasing.isEmpty())
                N.addAll(decreasing);

            Collections.sort(N, Collections.reverseOrder());

            while (hasRepeat(N))
                N = new ArrayList(removeRepeat(N));

            result = new BLDigit(res_sign, N.size(), N);
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


    public static BLDigit sqrt(BLDigit bl){
        BLDigit O = bl;
        int N = O.N.get(0);
        List<Integer> result = new ArrayList();
        result.add(N/2);
        BLDigit sqrt = new BLDigit(0,1,result);
        BLDigit square = new BLDigit(0, result.size(),new ArrayList(Arrays.asList(result.get(0)*2)));
        BLDigit reminder = BLDigit.ZERO;
                //square;
        BLDigit sqrt_degree = square;

        int i = 1;

        while ((i<20)&&(!O.N.isEmpty())) {

            System.out.println("reminder = "+reminder);
            System.out.println("sqrt = "+sqrt.toString());
            System.out.println("sqrt squeare = "+square.toString());
            System.out.println("sqrt_degree = "+sqrt_degree.toString());
            System.out.println("begin O = "+O.toString());

            square = square(sqrt);
            sqrt_degree = sub(square, reminder);
            if (sub(O, sqrt_degree).sign==1) {
                int position = sqrt.N.size() - 1;
                BLDigit y = new BLDigit(0, 1, Arrays.asList(sqrt.N.get(position)-1));
                System.out.println("Decresed last sqrt element to 1 "+y);
                sqrt.N.remove(position);
                sqrt = add(sqrt, y);
            }
            else {
                O = sub(O, sqrt_degree);
                System.out.println("change O = "+O.toString());

                if(O.Q==0)break;
                N = O.N.get(0);
                Integer y =Math.abs(N-1-sqrt.N.get(0));
                System.out.println("y = "+y);

                if (!sqrt.N.contains(y))
                    reminder= add(sqrt_degree, reminder);


                sqrt = add(sqrt, new BLDigit(0, 1,  Arrays.asList(y)));
            }
            System.out.println("sqrt = "+sqrt.toString());
            System.out.println("end O = "+O.toString());

            System.out.println("\n");
            ++i;
          //  Collections.sort(result, Collections.reverseOrder());

        }


        return sqrt;
    }

    public static BLDigit square(BLDigit bl1) {
        BLDigit result = BLDigit.ZERO;
 //       System.out.println("("+bl1.N.toString()+") ^2");
        int singles_numb = bl1.submatrixNumber(1);
        if (singles_numb>0)
            result = bl1.singlesSum(singles_numb);
        if (bl1.Q >= 2) {
            int doubles_numb = bl1.submatrixNumber(2);
            result = BLDigit.add(result, bl1.doubleSubSum(doubles_numb));
        }
      //  System.out.println(result.N.toString());
        return result;
    }

    public List<Pair> selectPairs(int H) {
        List<Pair> result = new ArrayList();
        int h = 0;
        for (int i = 0; i < this.Q-1; ++i)
        {
            for (int j = i+1; j < this.Q; ++j)
            {
                if (result.size()==H) break;
                if (i != j)
                {
                    Pair rows = new Pair(this.N.get(i),this.N.get(j));
                    if (!Pair.isContainEqual(result, rows)) {
                        result.add(rows);
                        ++h;
                    }
                }
            }

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

    public BLDigit doubleSubSum(int H) {
        List<Pair> pairs = selectPairs(H);
        BLDigit result = BLDigit.ZERO;
      //  System.out.println(H+" double subs");
        for (Pair pair : pairs) {
            BLDigit additor = calculateDoubleSub(pair);
            result = BLDigit.add(result, additor);
        }
        return result;
    }

    public BLDigit singlesSum(int H) {
        List<Integer> N = new ArrayList();
        for (int i = 0; i < H; ++i) {
            N.add(this.N.get(i)*2);
        }
        BLDigit result = new BLDigit(this.sign, N.size(), N);
 //       System.out.println("single subs sum\n"+result.N.toString());
        return result;
    }

    public BLDigit calculateDoubleSub(Pair matrix) {
        BLDigit result = Convertor.fromString("2");
        int additor = matrix.first + matrix.second;
        for (int i = 0; i < result.Q; i++) {
            result.N.set(i,(additor+result.N.get(i)));
        }
 //       System.out.println("["+matrix.first+","+matrix.second+"] = "+result.N.toString());
        return result;
    }

    static int signResolving(int first_sign, int second_sign, int operation, int compare) {
//        System.out.println("first_sign = "+first_sign+"\tsecond_sign = "+second_sign);
//        System.out.println("operation is "+operation+"\tcompare result = "+compare);

        int result = 0;
        second_sign = second_sign-operation;
        if (first_sign!=second_sign) {
            result = (compare==1)?1:-1;
        }
      //  System.out.println(result);
        return result;
    }

    public static int compare(BLDigit term_1, BLDigit term_2) {
        int result = 0;
 //       System.out.println(term_1.toString()+" compare "+term_2.toString());
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

            while ((j < term_2.Q)&&(i < term_1.Q))
            {
                int t1i = term_1.N.get(i);
                int t2j = term_2.N.get(j);

                result = (t1i > t2j)? 1:((t1i < t2j)?-1: 0);
                if(result!=0) break;

                ++i;
                ++j;
            }

            if((result==0))
                if((j >= term_2.Q)^(i >= term_1.Q))
                    result = (i < term_1.Q)?1:-1;

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

        if (this.N.size()!=0) {
            for (int i = 0; i<this.Q; ++i)
                result.append("."+this.N.get(i));
        }
        else result.append(".");

        return result.toString();
    }

    public void show() {
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
/*
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
}*/