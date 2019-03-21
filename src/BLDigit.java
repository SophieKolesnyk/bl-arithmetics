import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BLDigit {

    public int sign = 0;
    public int Q = 0;
    public List<Integer> N = new ArrayList();
    public int precision = 20;

    /*------------------------Constructors------------------------*/
    BLDigit() {}

    BLDigit(int init_precision) {
        this.precision = init_precision;
    }

    BLDigit(BLDigit i_bl) {
        this.precision = i_bl.precision;
        if (compare(i_bl, BLDigit.ZERO) != 0) {
            this.sign = i_bl.sign;
            this.Q = i_bl.Q;
            this.N.addAll(i_bl.N);
        }
        else {
            this.sign = 0;
            this.Q = 0;
            this.N = Collections.emptyList();
        }

    }

    BLDigit(int i_sign, int i_Q, List<Integer> i_N) {
        this.sign = i_sign;
        this.Q = i_Q;
        this.N = i_N;
    }


    public static final BLDigit ZERO = new BLDigit(0, 0, Collections.emptyList());

    public static BLDigit add(BLDigit bl1, BLDigit bl2) {
        BLDigit result = new BLDigit();
        if (bl1.sign != bl2.sign) {
            result = BLDigit.sub(bl1, bl2);
        } else {
            int res_prec = (bl1.precision > bl2.precision) ? bl1.precision : bl2.precision;
            result = new BLDigit(res_prec);
            List<Integer> result_list = new ArrayList();

            int bl1_to_zero = BLDigit.compare(bl1, BLDigit.ZERO);
            int bl2_to_zero = BLDigit.compare(bl2, BLDigit.ZERO);

            if ((bl1_to_zero == 0) || (bl2_to_zero == 0))
                if ((bl1_to_zero == 0) ^ (bl2_to_zero == 0)) {
                    if ((bl1_to_zero == 0)) result = bl2;
                    else result = bl1;
                } else result = BLDigit.ZERO;
            else {
                int i1 = 0, j2 = 0;
                int comparison;
                Integer res_i = 0;

                for (; i1 < bl1.N.size() && j2 < bl2.N.size(); ) {
                    comparison = Integer.compare(bl1.N.get(i1), bl2.N.get(j2));

                    if (comparison != 0) {
                        if (comparison > 0)
                            res_i = bl1.N.get(i1++);
                        else
                            res_i = bl2.N.get(j2++);
                    } else {
                        res_i = bl1.N.get(i1) + 1;
                        ++i1;
                        ++j2;
                    }

                    result_list.add(res_i);

                }

                if (i1 < bl1.N.size()) result_list.addAll(bl1.N.subList(i1, bl1.N.size()));
                if (j2 < bl2.N.size()) result_list.addAll(bl2.N.subList(j2, bl2.N.size()));

                Collections.sort(result_list, Collections.reverseOrder());

                while (hasRepeat(result_list))
                    result_list = removeRepeat(result_list);
                result.sign = bl1.sign;
                result.Q = result_list.size();
                result.N = result_list;
            }
        }
        return result;
    }

    private static BLPair getSubConditions(BLDigit term_1, BLDigit term_2) {
        int compare_res = compare(term_1, term_2);
        BLPair terms = new BLPair();
        int t2_sign = (term_2.sign == 0) ? 1 : 0;

        if (compare_res == 1) {
            terms.first = term_1;
            terms.second = term_2;
        } else {
            terms.first = new BLDigit(t2_sign, term_2.Q, term_2.N);
            terms.second = term_1;
        }
        return terms;
    }

    static BLDigit sub(BLDigit term_1, BLDigit term_2) {
        BLDigit result = BLDigit.ZERO;
        List<Integer> decreasing = new ArrayList<Integer>();
        List<Integer> subtrahend = new ArrayList<Integer>();

        if (signResolving(term_1.sign, term_2.sign, 1) == 0) {
            result = add(term_1, term_2);
        } else {
            if (compare(term_1, term_2) == 0)
                return result;

            BLPair in_order = getSubConditions(term_1, term_2);
            decreasing.addAll(in_order.first.N);
            subtrahend.addAll(in_order.second.N);

            while (!subtrahend.isEmpty()) {
                int i = subtrahend.size() - 1;
                int j = decreasing.size() - 1;

                for (; j >= 0; --j) {
                    if (decreasing.get(j) == subtrahend.get(i)) {
                        decreasing.remove(j);
                        subtrahend.remove(i);
                        break;
                    } else {
                        if (decreasing.get(j) > subtrahend.get(i)) {
                            int decr_j = decreasing.get(j);
                            int subtr_i = subtrahend.get(i);
                            decreasing.remove(j);
                            subtrahend.remove(i);
                            while (subtr_i < decr_j)
                                decreasing.add(subtr_i++);
                            break;
                        }
                    }
                }
                Collections.sort(decreasing, Collections.reverseOrder());
                while (hasRepeat(decreasing))
                    decreasing = new ArrayList(removeRepeat(decreasing));
            }
            Collections.sort(decreasing, Collections.reverseOrder());
            while (hasRepeat(decreasing))
                decreasing = new ArrayList(removeRepeat(decreasing));
            result = new BLDigit(in_order.first.sign, decreasing.size(), decreasing);
        }

        return result;
    }

    public static BLDigit mult(BLDigit bl1, BLDigit bl2) {
        List<Integer> result_list = new ArrayList();
        List<Integer> term_1 = bl1.N;
        List<Integer> term_2 = bl2.N;
        int res_prec = bl1.precision + bl2.precision;
        BLDigit result = new BLDigit(res_prec);

        for (int i = 0; i < term_1.size(); ++i) {
            for (int j = 0; j < term_2.size(); ++j) {
                Integer sum_ij = term_1.get(i) + term_2.get(j);
                result_list.add(sum_ij);
            }
        }

        while (hasRepeat(result_list))
            result_list = new ArrayList(removeRepeat(result_list));
        result.sign = (bl1.sign * bl2.sign);
        result.Q = result_list.size();
        result.N = result_list;

        return result;
    }

    public static BLDigit div(BLDigit divider, BLDigit divisior) {
        BLDigit result = new BLDigit();

        if ((compare(divisior, BLDigit.ZERO) == 0) || (compare(divider, BLDigit.ZERO) == 0)) {
            if (compare(divisior, BLDigit.ZERO) == 0)
                throw new ArithmeticException("Forbidden operation - 'Division by ZERO'");
            else result = BLDigit.ZERO;
        }
        else {
            List<Integer> N = new ArrayList();

            BLDigit remainder = new BLDigit(divider);

            int after_point = 0;
            int D = divider.N.get(0) - divisior.N.get(0);
            int divisior_senior = divisior.N.get(0);
            BLDigit mult_res = new BLDigit();


            while ((after_point < divisior.precision) && (divider.Q > 0)) {
                mult_res = multByCoefficient(divisior, D);
                if (compare(divider, mult_res) < 0)
                    D -= 1;
                else {
                    after_point = (D<0)? (after_point + 1):after_point;
                    N.add(D);
                    remainder = sub(divider, mult_res);

                    if (compare(remainder, BLDigit.ZERO) != 0) {
                        divider = new BLDigit(remainder);
                        D = divider.N.get(0) - divisior_senior;
                    }
                    else break;
                }
            }

            result = new BLDigit((result.sign = divider.sign*divisior.sign), N.size(), N);
        }

        return result;
    }
    public static BLDigit sqrt(BLDigit bl) {
        BLDigit O = bl;
        BLDigit sqrt = new BLDigit(0, 1, Arrays.asList(O.N.get(0) / 2));
        BLDigit square = new BLDigit(0, 1, new ArrayList(Arrays.asList(sqrt.N.get(0) * 2)));
        BLDigit sqrt_degree = square;
        BLDigit prev_sqrt_degree = BLDigit.ZERO;
        Integer y = sqrt.N.get(0);
        while ((sqrt.N.get(sqrt.N.size()-1)>0) && (O.Q != 0)) {
            BLDigit O_next = sub(O, sqrt_degree);

            if (O_next.sign == 1) {
                sqrt.N.remove(y);
                y = y - 1;
                sqrt = add(sqrt, new BLDigit(0, 1, Arrays.asList(y)));
            } else {
                O = sub(O, sqrt_degree);
                if (O.Q == 0) break;
                int N = O.N.get(0);
                prev_sqrt_degree = add(prev_sqrt_degree, sqrt_degree);
                y = Math.abs(N - 1 - sqrt.N.get(0));
                while (sqrt.N.contains(y))
                    y = y - 1;
                sqrt = add(sqrt, new BLDigit(0, 1, Arrays.asList(y)));
            }
            square = square(sqrt);
            sqrt_degree = sub(square, prev_sqrt_degree);
        }

        return sqrt;
    }

    public static BLDigit square(BLDigit bl1) {
        BLDigit result = BLDigit.ZERO;
        //       System.out.println("("+bl1.N.toString()+") ^2");
        int singles_numb = bl1.submatrixNumber(1);
        if (singles_numb > 0)
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
        for (int i = 0; i < this.Q - 1; ++i) {
            for (int j = i + 1; j < this.Q; ++j) {
                if (result.size() == H) break;
                if (i != j) {
                    Pair rows = new Pair(this.N.get(i), this.N.get(j));
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
            N.add(this.N.get(i) * 2);
        }
        BLDigit result = new BLDigit(this.sign, N.size(), N);
        //       System.out.println("single subs sum\n"+result.N.toString());
        return result;
    }

    public BLDigit calculateDoubleSub(Pair matrix) {
        BLDigit result = Convertor.fromString("2");
        int additor = matrix.first + matrix.second;
        for (int i = 0; i < result.Q; i++) {
            result.N.set(i, (additor + result.N.get(i)));
        }
        //       System.out.println("["+matrix.first+","+matrix.second+"] = "+result.N.toString());
        return result;
    }


    static int signResolving(int first_sign, int second_sign, int operation) {
        int result = 0;
        second_sign = (second_sign == 1) || (operation == 1) ? 1 : 0;

        if (first_sign != second_sign) {
            result = 1;
        }
        return result;
    }

    public static int compare(BLDigit term_1, BLDigit term_2) {
        int result = 0;
        //       System.out.println(term_1.toString()+" compare "+term_2.toString());
        if ((term_1.Q == 0) || (term_2.Q == 0)) {
            if ((term_1.Q != 0) ^ (term_2.Q != 0))
                result = (term_1.Q == 0) ? -1 : 1;
            else
                result = 0;

        } else {
            int i = 0;
            int j = 0;

            while ((j < term_2.Q) && (i < term_1.Q)) {
                int t1i = term_1.N.get(i);
                int t2j = term_2.N.get(j);

                result = (t1i > t2j) ? 1 : ((t1i < t2j) ? -1 : 0);
                if (result != 0) break;

                ++i;
                ++j;
            }

            if ((result == 0))
                if ((j >= term_2.Q) ^ (i >= term_1.Q))
                    result = (i < term_1.Q) ? 1 : -1;

        }
        return result;
    }

    public static boolean hasRepeat(List<Integer> i_list) {
        Collections.sort(i_list, Collections.reverseOrder());
        boolean result = false;
        for (int i = i_list.size() - 2, j = i_list.size() - 1; i >=0; --i, --j) {
            if (i_list.get(i).equals(i_list.get(j))) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static List<Integer> removeRepeat(List<Integer> i_list) {
        Collections.sort(i_list, Collections.reverseOrder());

        List<Integer> result = new ArrayList();
        while (!i_list.isEmpty()) {
            int i = i_list.size() - 1;
            if (i == 0) {
                result.add(i_list.get(i));
                i_list.remove(i);
                break;
            }
            if (i_list.get(i).equals(i_list.get(i - 1))) {
                result.add(i_list.get(i) + 1);
                i_list.remove(i);
                i_list.remove(i - 1);
            } else {
                result.add(i_list.get(i));
                i_list.remove(i);
            }
        }

        return result;
    }

    public static BLDigit multByCoefficient(BLDigit bl_digit, int coefficient) {
        List<Integer> N = new ArrayList();

        for (int i = 0; i < bl_digit.Q; ++i)
            N.add(bl_digit.N.get(i)+coefficient);

        int sign = bl_digit.sign*((coefficient>0)?0:1);

        return new BLDigit(sign, N.size(), N);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.sign);
        result.append("." + this.Q);

        if (this.N.size() != 0) {
            for (int i = 0; i < this.Q; ++i)
                result.append("." + this.N.get(i));
        } else result.append(".");

        return result.toString();
    }

    public void show() {
        System.out.println(this.toString());
    }

}