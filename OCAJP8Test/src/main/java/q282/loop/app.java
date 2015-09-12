/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q282.loop;

/**
 *
 * How many times will the line marked //1 be called in the following code?
 */
public class app {

    /**
     * A do-while loop is always executed at least once. So in the first
     * iteration, x is decremented and becomes 9. Now the while condition is
     * tested, which returns true because 9 is less than 10. So the loop is
     * executed again with x = 9. In the loop, x is decremented to 8 and the
     * condition is tested again, which again returns true because 8 is less
     * than 10.
     *
     * As you can see, x keeps on decreasing by one in each iteration and every
     * time the condition x<10 returns true. However, after x reaches
     * -2147483648, which is its MIN_VALUE, it cannot decrease any further and
     * at this time when x-- is executed, the value rolls over to 2147483647,
     * which is Integer.MAX_VALUE. At this time, the condition x<10 fails and
     * the loop terminates.
     */
    public static void main(String[] args) {
        int x = 10;
        do {
            x--;
            System.out.println(x);  // 1
        } while (x < 10);
    }

}
