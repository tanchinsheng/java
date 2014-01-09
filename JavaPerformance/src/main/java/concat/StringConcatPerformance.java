package concat;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

public class StringConcatPerformance {

    private static final int OUTER_ITERATION = 20;
    private static final int INNER_ITERATION = 50000;

    public static void main(String args[]) throws Exception {
        String addTestStr = "";
        String concatTestStr = "";
        StringBuffer concatTestSb = null;
        StringBuilder concatTestSbu = null;

        for (int outerIndex = 0; outerIndex <= OUTER_ITERATION; outerIndex++) {
            StopWatch stopWatch = new LoggingStopWatch("StringAddConcat");
            addTestStr = "";
            for (int innerIndex = 0; innerIndex <= INNER_ITERATION; innerIndex++) {
                addTestStr += "*";
            }
            stopWatch.stop();
        }

        for (int outerIndex = 0; outerIndex <= OUTER_ITERATION; outerIndex++) {
            StopWatch stopWatch = new LoggingStopWatch("StringConcat");
            concatTestStr = "";
            for (int innerIndex = 0; innerIndex <= INNER_ITERATION; innerIndex++) {
                concatTestStr = concatTestStr.concat("*");
            }
            stopWatch.stop();
        }

        for (int outerIndex = 0; outerIndex <= OUTER_ITERATION; outerIndex++) {
            StopWatch stopWatch = new LoggingStopWatch("StringBufferConcat");
            concatTestSb = new StringBuffer();
            for (int innerIndex = 0; innerIndex <= INNER_ITERATION; innerIndex++) {
                concatTestSb.append("*");
            }
            stopWatch.stop();
        }

        for (int outerIndex = 0; outerIndex <= OUTER_ITERATION; outerIndex++) {
            StopWatch stopWatch = new LoggingStopWatch("StringBuilderConcat");
            concatTestSbu = new StringBuilder();
            for (int innerIndex = 0; innerIndex <= INNER_ITERATION; innerIndex++) {
                concatTestSbu.append("*");
            }
            stopWatch.stop();
        }
    }
}
