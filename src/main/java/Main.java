import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static List<BLDigit> splitBL(BLDigit bl) {
        int new_Q = bl.Q;
        while (new_Q>=4) {
            if ((new_Q/2)>=2)
                new_Q /= 2;
        }
        System.out.println(new_Q);
        List<Integer> N = new ArrayList<Integer>(bl.N);
        List<BLDigit> resalt_list = new ArrayList<BLDigit>();

        while (!N.isEmpty()) {
            if (N.size()<new_Q) {
                resalt_list.add(new BLDigit(0, N.size(), new ArrayList<Integer>(N)));
                break;
            }
            else {
                BLDigit bl_i = new BLDigit(0, new_Q, new ArrayList<Integer>(N.subList(0, new_Q)));
                resalt_list.add(bl_i);
                System.out.println("\n bl i = "+bl_i);
                N = N.subList(new_Q, N.size());
                System.out.println("new_Q = "+new_Q+"\nN = "+N);
            }


        }
        return resalt_list;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input1 = "114381625757888867669235779976146612010218296721242362562561842935706935245733897830597123563958705058989075147599290026879543541";//867669.235779 reader.readLine();
        BLDigit X = Converter.toBLDigit(input1);
        System.out.println(Converter.toDecimal(X)+ "\n" +  X);// + "\n" +  input1 + "\n" + from_bl);
        List<BLDigit> bl_list = splitBL(X);
        List<BLDigit> sqrt_list = new ArrayList<BLDigit>();
         BLDigit Z = BLDigit.ZERO;

        for (int i = 0; i < bl_list.size(); ++i) {
//            FileOutputStream out = new FileOutputStream("/home/sophie/Desktop/keys_searching.txt");
//            out.write((i++ + "\nO: "+O+"\n"+"y: "+y+"\n sqrt_degree: "+sqrt_degree+"\n(O-sqrt_degree) = "+O_next+"\n").getBytes());
            System.out.println("\n"+i+"\t"+bl_list.get(i));
            BLDigit partition_sqrt = Calculator.sqrt(bl_list.get(i));
            sqrt_list.add(partition_sqrt);
            System.out.println(partition_sqrt);
            Z = Calculator.add(Z, partition_sqrt);
            System.out.println(Z+"\n");
        }


    }
}
