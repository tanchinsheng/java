package listing6_12;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.ArrayList;
import java.util.List;
// This program demonstrates the usage of wild card parameters

class WildCardUse {

    static void printList(List<?> list) {
        for (Object l : list) {
            System.out.println("[" + l + "]");
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(100);
        printList(list);
        List<String> strList = new ArrayList<>();
        strList.add("10");
        strList.add("100");
        printList(strList);
//        List<?> wildCardList = new ArrayList<Integer>();
//        wildCardList.add(10);
//        wildCardList.add(100);
//        printList(wildCardList);

    }
}
