package listing6_19;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.HashSet;
import java.util.Set;

class TestCircle {

    public static void main(String[] args) {
        Set<Circle> circleList = new HashSet<>();
        circleList.add(new Circle(10, 20, 5));
        System.out.println(circleList.contains(new Circle(10, 20, 5)));
    }
}
