import java.util.List;

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
