package ocpjp.pretest._22;

import java.util.regex.Pattern;

class Split {

    public static void main(String[] args) {
        String date = "10-01-2012"; // 10th January 2012 in dd-mm-yyyy format
        String[] dateParts = date.split("-");
        System.out.print("Using String.split method: ");
        for (String part : dateParts) {
            System.out.print(part + " ");
        }
        System.out.print("\nUsing regex pattern: ");
        Pattern datePattern = Pattern.compile("-");
        dateParts = datePattern.split(date);
        for (String part : dateParts) {
            System.out.print(part + " ");
        }
    }
}