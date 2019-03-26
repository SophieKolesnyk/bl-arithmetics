import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class BLDigit {

    public int sign = 0;
    public int Q = 0;
    public List<Integer> N = new ArrayList();

    BLDigit() {}

    BLDigit(BLDigit i_bl) {
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

    BLDigit(String i_dec) {
        BLDigit i_bl = Converter.toBLDigit(i_dec);
        this.sign = i_bl.sign;
        this.N = i_bl.N;
        this.Q = i_bl.Q;
    }


    public static final BLDigit ZERO = new BLDigit(0, 0,  new ArrayList(Arrays.asList()));
    public static final BLDigit ONE = new BLDigit(0, 1, new ArrayList(Arrays.asList(0)));



    public static int compare(BLDigit term_1, BLDigit term_2) {
        int result = 0;
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

    public static boolean has_fraction(BLDigit i_bl){
        if (i_bl.N.get(i_bl.Q-1)<0)
            return true;

        return false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.sign);
        result.append("." + this.Q);

        if (this.N.size() != 0) {
            for (int i = 0; i < this.N.size(); ++i)
                result.append("." + this.N.get(i));
        } else result.append(".");


        return result.toString();
    }

    public void show() {
        System.out.println(this.toString());
    }
}
