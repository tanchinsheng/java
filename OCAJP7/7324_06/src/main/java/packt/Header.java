package packt;

import java.util.Date;

// Immutable object...cannot be extended
final public class Header {

    // private and final
    private final String title;
    private final int version;
    private final Date date;

    // no setter
    public Date getDate() {
        // Do not allow mutable field objects to be changed
        // Any Date object is mutable, so by returning a copy of the date as opposed
        // to a reference to the current date, the user is unable to access and otherwise modify
        // the private field.
        return new Date(date.getTime());
    }

    public String getTitle() {
        return title;
    }

    public int getVersion() {
        return version;
    }

    public Header(String title, int version, Date date) {
        this.title = title;
        this.version = version;
        this.date = new Date(date.getTime());
    }

    public String toString() {
        return "Title: " + this.title + "\n"
                + "Version: " + this.version + "\n"
                + "Date: " + this.date + "\n";
    }
}
