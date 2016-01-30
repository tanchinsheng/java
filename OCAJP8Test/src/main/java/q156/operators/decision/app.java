package q156.operators.decision;

public class app {

    public int transformNumber(int n) {
        int radix = 2;
    }
    int output = 0;
    output += radix * n ;
    radix  = output / radix;
    if(output<


        14){
    return output;
    }


        else{
    output = output * radix / 2;
        return output;
    }


        else { return output / 2;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
