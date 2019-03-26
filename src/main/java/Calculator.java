import ch.obermuhlner.math.big.BigDecimalMath;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class Calculator {

    public static long precision = 60;

    /*--------------------------------Methods of class--------------------------------*/
    public static BLDigit add(BLDigit bl1, BLDigit bl2) {
//        System.out.println("\tadd\t"+bl1+"\t   \t"+bl2);
        BLDigit result = new BLDigit();
        if (bl1.sign != bl2.sign) {
            result = sub(bl1, bl2);
        } else {
            result = new BLDigit();
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
//                int comparison;
                Integer res_i = 0;

                for (; i1 < bl1.N.size() && j2 < bl2.N.size(); ) {
                  //  comparison = Integer.compare(bl1.N.get(i1), bl2.N.get(j2));

//                    if (comparison != 0) {
                    if (bl1.N.get(i1) != bl2.N.get(j2)) {
                        if (bl1.N.get(i1) > bl2.N.get(j2))
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
//        System.out.println("\t  =\t"+result);

        return result;
    }

    public static BLDigit sub(BLDigit term_1, BLDigit term_2) {
//        System.out.println("\tsub\t"+term_1+" - "+term_2);
        BLDigit result = BLDigit.ZERO;
        List<Integer> decreasing = new ArrayList<Integer>();
        int j = 0;

        List<Integer> subtrahend = new ArrayList<Integer>();
        int i = 0;
        if (signResolving(term_1.sign, term_2.sign, 1) == 0) {
            result = add(term_1, term_2);
        } else {
            if (BLDigit.compare(term_1, term_2) == 0)
                return result;

            BLPair in_order = getSubConditions(term_1, term_2);
            decreasing.addAll(in_order.first.N);
            subtrahend.addAll(in_order.second.N);

            while (!subtrahend.isEmpty()) {
                i = subtrahend.size() - 1;
                j = decreasing.size() - 1;

                for (; j >= 0; --j) {
                    if ((int)(decreasing.get(j)) == (int)(subtrahend.get(i))) {
                        decreasing.remove(j);
                        subtrahend.remove(i);
                        break;
                    } else {
                        if (decreasing.get(j) > subtrahend.get(i)) {
                            int decr_j = decreasing.get(j);
                            int subtr_i = subtrahend.get(i);
                            decreasing.remove(j);
                            subtrahend.remove(i);
                            while (subtr_i < decr_j) {
                                decreasing.add(j, subtr_i++);
                            }

//                            Collections.sort(decreasing, Collections.reverseOrder());
//                            while (hasRepeat(decreasing))
//                                decreasing = new ArrayList(removeRepeat(decreasing));

                            break;
                        }
                    }
                }

            }
            Collections.sort(decreasing, Collections.reverseOrder());
            while (hasRepeat(decreasing))
                decreasing = new ArrayList(removeRepeat(decreasing));
            result = new BLDigit(in_order.first.sign, decreasing.size(), decreasing);
        }
//        System.out.println("\t  =\t"+result);
        return result;
    }

    public static BLDigit mult(BLDigit bl1, BLDigit bl2) {
//        System.out.println("\tmult\t"+bl1+"\t    \t"+bl2);
        List<Integer> result_list = new ArrayList();
        List<Integer> term_1 = bl1.N;
        List<Integer> term_2 = bl2.N;
        BLDigit result = new BLDigit();

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
//        System.out.println("\t   =\t"+result);
        return result;
    }

    public static BLDigit div(BLDigit divider, BLDigit divisior) {
//        System.out.println("\tdiv\t"+divider+"\t   \t"+divisior);
        BLDigit result = new BLDigit();
        if ((BLDigit.compare(divisior, BLDigit.ZERO) == 0) || (BLDigit.compare(divider, BLDigit.ZERO) == 0)) {
            if (BLDigit.compare(divisior, BLDigit.ZERO) == 0)
                throw new ArithmeticException("Forbidden operation - 'Division by ZERO'");
            else result = BLDigit.ZERO;
        }
        else {
            List<Integer> N = new ArrayList();
            int after_point = 0;
            int D = divider.N.get(0) - divisior.N.get(0);
            int divisior_senior = divisior.N.get(0);

            while ((after_point < precision) && (divider.Q > 0)) {
                BLDigit mult_res = multByCoefficient(divisior, D);
                if (BLDigit.compare(divider, mult_res) < 0)
                    D -= 1;
                else {
                    after_point = (D<0)? (after_point + 1):after_point;
                    N.add(D);
                    BLDigit remainder = sub(divider, mult_res);

                    if (BLDigit.compare(remainder, BLDigit.ZERO) != 0) {
                        divider = new BLDigit(remainder);
                        D = divider.N.get(0) - divisior_senior;
                    }
                    else break;
                }
            }
            result = new BLDigit((result.sign = divider.sign*divisior.sign), N.size(), N);
        }
//        System.out.println("\t  =\t"+result);

        return result;
    }



    public static BLDigit sqrt(BLDigit bl)  throws IOException  {
        FileOutputStream out = new FileOutputStream("/home/sophie/Desktop/keys_searching.txt");
        BLDigit O = bl;
        BLDigit sqrt = new BLDigit(0, 1, Arrays.asList(O.N.get(0) / 2));
        BLDigit square = new BLDigit(0, 1, new ArrayList(Arrays.asList(sqrt.N.get(0) * 2)));
        BLDigit sqrt_degree = square;
        BLDigit prev_sqrt_degree = BLDigit.ZERO;
        Integer y = sqrt.N.get(0);
        int i = 0;
//        while (O.Q != 0) {
        while ((sqrt.N.get(sqrt.N.size()-1)>=0) && (O.Q != 0)) {
//            System.out.println("O_next = O - sqrt_degree");
//        while ((i<precision) && (O.Q != 0)) {

            BLDigit O_next = sub(O, sqrt_degree);
            out.write((i++ + "\nO: "+O+"\n"+"y: "+y+"\n sqrt_degree: "+sqrt_degree+"\n(O-sqrt_degree) = "+O_next+"\n").getBytes());
            if (O_next.sign == 1) {
                sqrt.N.remove(y);
                y = y - 1;
//                System.out.println("sqrt = sqrt + y");

                sqrt = add(sqrt, new BLDigit(0, 1, Arrays.asList(y)));

            } else {
                O = O_next;
                if (O.Q == 0) break;
                int N = O.N.get(0);
                prev_sqrt_degree = square;
                y = N - 1 - sqrt.N.get(0);
                while (sqrt.N.contains(y))
                    y = y - 1;
//                System.out.println("sqrt = sqrt + y");
                sqrt = add(sqrt, new BLDigit(0, 1, Arrays.asList(y)));
            }
            square = square(sqrt);
//            System.out.println("square = square(sqrt) = "+square);
//            System.out.println("sqrt_degree = square - prev_sqrt_degree");
            sqrt_degree = sub(square, prev_sqrt_degree);
            out.write("______________________________________________________\n".getBytes());
            out.write(("\n" + withoutFractPart(sqrt).toString()).getBytes());
            out.write("\n______________________________________________________\n\n".getBytes());
//            if ((sqrt.Q>=0)&&(sqrt.N.get(i)<0)) ++i;

        }

        out.write(("\n" + withoutFractPart(sqrt).toString()).getBytes());

        out.close();
        return withoutFractPart(sqrt);
    }

    public static BLDigit square(BLDigit bl1) {
        BLDigit result = BLDigit.ZERO;
        int singles_numb = submatrixNumber(bl1, 1);
        if (singles_numb > 0)
            result = singlesSum(bl1, singles_numb);
        if (bl1.Q >= 2) {
            int doubles_numb = submatrixNumber(bl1,2);
            result = add(result, doubleSubSum(bl1, doubles_numb));
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

    public static BLPair findSecretKeys(BLDigit N) throws IOException  {
    //    FileOutputStream out = new FileOutputStream("/home/sophie/Desktop/keys_searching.txt");
        BLDigit P1 = sqrt(N);
        BLDigit P2 = new BLDigit();
        BLDigit divisior = BLDigit.ZERO;
        BLDigit mult = BLDigit.ZERO;
        BLDigit i = new BLDigit("4861333901857487042388026012815767174661029535799632854021928025");
        BLDigit max_range = new BLDigit("4861333901857487042388026012815767174661029535799632854021928025");
        System.out.println(i);

  //      String title = "\t N = " + N.toString() + "\n\t P1 = " + P1 + "\n\t------------------------------------------------------------------------------------\n";
   //     title += String.format("\t| %-20s | %-20s | %-20s | %-30s |%n", "index", "P1-i", "P2", "mult") + "\t------------------------------------------------------------------------------------\n";
  //      out.write(title.getBytes());

        while ((BLDigit.compare(mult, N)!=0)&&(BLDigit.compare(i,max_range)!=0)) {
            System.out.println(i);

            divisior = sub(P1,i);
            P2 = withoutFractPart(div(N, divisior));
            mult = mult(P2, divisior);
//         //   String line = String.format("\t| %-20s | %-20s | %-20s | %-30s |%n", Converter.toDecimal(i).toString(), Converter.toDecimal(divisior).toString(), Converter.toDecimal(P2).toString(), Converter.toDecimal(mult).toString());
          //  String line = String.format("\t| %-20s | %-20s | %-20s | %-30s |%n", i.toString(), divisior.toString(), P2.toString(), mult.toString());
           // System.out.println(line);
//            out.write(line.getBytes());
            i = add(i,BLDigit.ONE);
        }

//        out.write(("q = " + divisior + "p = " + P2).getBytes());
//        out.write(("q = " + Converter.toDecimal(divisior).toString() + "p = " + Converter.toDecimal(P2).toString()).getBytes());
//        out.close();
        return new BLPair(withoutFractPart(divisior), withoutFractPart(P2));
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

    /*--------------------------------Utility methods--------------------------------*/

    private static BLPair getSubConditions(BLDigit term_1, BLDigit term_2) {
        int compare_res = BLDigit.compare(term_1, term_2);
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

    private static List<Pair> selectPairs(BLDigit bl, int H) {
        List<Pair> result = new ArrayList();
        int h = 0;
        for (int i = 0; i < bl.Q - 1; ++i) {
            for (int j = i + 1; j < bl.Q; ++j) {
                if (result.size() == H) break;
                if (i != j) {
                    Pair rows = new Pair(bl.N.get(i), bl.N.get(j));
                    if (!Pair.isContainEqual(result, rows)) {
                        result.add(rows);
                        ++h;
                    }
                }
            }
        }

        return result;
    }

    private static int submatrixNumber(BLDigit bl, int k) {
        BigDecimal Q_fact = BigDecimalMath.factorial(bl.Q);
        int difference = bl.Q - k;
        BigDecimal deductor = BigDecimalMath.factorial(k).multiply(BigDecimalMath.factorial(difference));
        BigDecimal H = Q_fact.divide(deductor);
        return H.intValue();
    }

    private static BLDigit doubleSubSum(BLDigit bl, int H) {
        List<Pair> pairs = selectPairs(bl, H);
        BLDigit result = BLDigit.ZERO;
        for (Pair pair : pairs) {
            BLDigit additor = calculateDoubleSub(pair);
            result = add(result, additor);
        }
        return result;
    }

    private static BLDigit singlesSum(BLDigit bl, int H) {
        List<Integer> N = new ArrayList();
        for (int i = 0; i < H; ++i) {
            N.add(bl.N.get(i) * 2);
        }
        return new BLDigit(bl.sign, N.size(), N);
    }

    private static BLDigit calculateDoubleSub(Pair matrix) {
        BLDigit result = Converter.toBLDigit("2");
        int additor = matrix.first + matrix.second;
        for (int i = 0; i < result.Q; i++) {
            result.N.set(i, (additor + result.N.get(i)));
        }
        return result;
    }

    private static int signResolving(int first_sign, int second_sign, int operation) {
        int result = 0;
        second_sign = (second_sign == 1) || (operation == 1) ? 1 : 0;

        if (first_sign != second_sign) {
            result = 1;
        }
        return result;
    }

    private static BLDigit multByCoefficient(BLDigit bl_digit, int coefficient) {
        List<Integer> N = new ArrayList();

        for (int i = 0; i < bl_digit.Q; ++i)
            N.add(bl_digit.N.get(i)+coefficient);

        int sign = bl_digit.sign*((coefficient>0)?0:1);

        return new BLDigit(sign, N.size(), N);
    }

    private static BLDigit withoutFractPart(BLDigit bl) {

        List<Integer> result = new ArrayList();
        for (int i = 0; i < bl.N.size(); ++i) {
            if (bl.N.get(i)>=0)
                result.add(bl.N.get(i));
        }
        return new BLDigit(bl.sign, result.size(), result);
    }
}