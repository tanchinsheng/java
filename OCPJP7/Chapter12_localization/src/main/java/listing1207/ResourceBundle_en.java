package listing1207;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.ListResourceBundle;

// default US English version
public class ResourceBundle_en extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return contents;
    }
    static final Object[][] contents = {
        {"MovieName", "Avatar"},
        {"GrossRevenue", (Long) 2782275172L}, // in US dollars
        {"Year", (Integer) 2009}
    };

}
