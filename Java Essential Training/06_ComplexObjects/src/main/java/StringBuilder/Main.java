package StringBuilder;

public class Main {

    public static void main(String[] args) {
        String s1 = "Welcome";
        s1 = s1 + " to Singapore";
        System.out.println(s1);

        StringBuilder sb = new StringBuilder(s1);
        sb.append(" to Maylaysia");
        System.out.println(sb);
    }

}
