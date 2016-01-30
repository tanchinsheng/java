package q171.operators.decision;

public class app {

    public static void main(String[] args) {
        boolean greenLight = true;
        boolean pedestrian = false;
        boolean rightTurn = true;
        boolean otherLane = false;
        if (((rightTurn && !pedestrian || otherLane) || ( ?  && !pedestrian && greenLight)) == true) {
            System.out.println("true");
        }
        // since the part before second || is true, the next part is not even evaluated.
    }

}
