import ch.obermuhlner.math.big.BigDecimalMath;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
//                System.out.println("\n bl i = "+bl_i);
                N = N.subList(new_Q, N.size());
//                System.out.println("new_Q = "+new_Q+"\nN = "+N);
            }


        }
        return resalt_list;
    }



    public static void main(String[] args) throws IOException {
        FileOutputStream out = new FileOutputStream("/home/sophie/Desktop/sqrt.txt");

        String input1 = "114381625757888867669235779976146612010218296721242362562561842935706935245733897830597123563958705058989075147599290026879543541";//867669.235779 reader.readLine();
        BLDigit X = Converter.toBLDigit(input1);
        System.out.println(Converter.toDecimal(X)+ "\n" +  X);
        List<BLDigit> bl_list = splitBL(X);

        BLDigit Z =  BLDigit.ZERO;

//        for (int i = 0; i < bl_list.size(); ++i) {
        for (int i = bl_list.size()-1; i >=0); --i) {
            System.out.println(i);
            BLDigit sqrt_port = Calculator.purt_sqrt(bl_list.get(i), Z);
            out.write(("\n"+i + "sqrt_port: "+sqrt_port+"\n").getBytes());
            List<Integer> additor = new ArrayList<Integer>();
            if(Z.Q!=0) {
                if (sqrt_port.Q!=0) {
                    boolean f = false;
                    for (int j = 0; j < additor.size(); ++j) {
                        for (int k = 0; k < Z.Q; k++) {
                            if (Z.N.get(k)<=sqrt_port.N.get(j)) {
                                f = true;
                                break;
                            }
                        }
                        if (!f) {
                            additor.add(sqrt_port.N.get(j));
                        }
                    }
                }
                Z = Calculator.add(Z, new BLDigit(0, additor.size(), additor));
            }
            else
                Z = sqrt_port;
            out.write(("rem: "+additor+"\nZ: "+Z).getBytes());

            out.write(("\t"+Converter.toDecimal(Z)).getBytes());
            out.write(("\n_______________________________________________________________________\n").getBytes());
        }
        Z = Calculator.add(Z, BLDigit.ONE);

        out.write(("Z: "+Z).getBytes());
        out.write(("\t "+Converter.toDecimal(Z)).getBytes());
        out.write(("Z^2: "+Calculator.square(Z)).getBytes());
        out.close();


    }
}
