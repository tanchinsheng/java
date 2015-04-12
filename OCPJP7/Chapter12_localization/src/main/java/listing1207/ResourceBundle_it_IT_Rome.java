package listing1207;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.ListResourceBundle;

// Italian version
public class ResourceBundle_it_IT_Rome extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return contents;
    }
    private static final Object[][] contents = {
        {"MovieName", "Che Bella Giornata"},
        {"GrossRevenue", (Long) 43000000L}, // in euros
        {"Year", (Integer) 2011}
    };
}
