public class BigInput {
    private  String result_rl;
    private  int portion_size = 3;
    BigInput()
    {

    }

    String [] splitToPortions(String i_str, int p_size)
    {
        int iter_numb = i_str.length()/p_size;

        int port_numb = i_str.length()%p_size>0?iter_numb+1:iter_numb;

        String result[] = new String[port_numb];

        for(int i = 0; i < iter_numb; ++i)
        {
            result[i] = i_str.substring((i_str.length()-3), (i_str.length()));
            i_str = i_str.substring(0, i_str.length()-p_size);
        }
        if(port_numb>iter_numb)
        {
            result[iter_numb] =i_str;
        }
        return result;
    }


}
