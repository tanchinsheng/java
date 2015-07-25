
public class PairProg {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Pair<String, Integer> grade1 = new Pair<>("williams", 90);
        Pair<String, Integer> grade2 = new Pair<>("Brown", 44);
        System.out.println(grade1.getFirstItem());
        System.out.println(grade1.getSecondItem());
        System.out.println(grade2.getFirstItem());
        System.out.println(grade2.getSecondItem());
    }

}
