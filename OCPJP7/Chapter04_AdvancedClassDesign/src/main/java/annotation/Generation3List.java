/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import java.lang.annotation.Documented;

@Documented
@interface ClassPreamble {

    @NonNull
    String author();

    @NonNull
    String date();

    @NonNull
    int currentRevision() default 1;

    @NonNull
    String lastModified() default "N/A";

    @NonNull
    String lastModifiedBy() default "N/A";

    // Note use of array
    @NonNull
    String[] reviewers();
}

@ClassPreamble(
        author = "John Doe",
        date = "3/17/2002",
        currentRevision = 6,
        lastModified = "4/12/2004",
        lastModifiedBy = "Jane Doe",
        // Note array notation
        reviewers = {"Alice", "Bob", "Cindy"}
)

public class Generation3List {

    public static void main(String[] args) {

    }

}
