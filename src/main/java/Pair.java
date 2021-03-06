import java.util.List;

class Pair {
    public int first;
    public int second;

    Pair(int i_first, int i_second) {
        this.first = i_first;
        this.second = i_second;
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

class BLPair {
    public BLDigit first;
    public BLDigit second;

    BLPair() {}

    BLPair(BLDigit i_first, BLDigit i_second) {
        this.first = i_first;
        this.second = i_second;
    }

}
