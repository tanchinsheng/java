/*
 * After some time, the requirements changed and the programmer now wants to make sure
 * that radiusB is always (200 - radiusA) instead of (100 - radiusA) without breaking existing
 * code that other people have written. Which of the following will accomplish his goal?
 */
package q053.javabasics.oo;

/**
 *
 * setRadius method makes sure that radiusB is set to sum - radiusA. So changing
 * sum to 200 should do it. However, note that radiusA, radiusB, and sum are
 * public which means that any other class can access these fields directly
 * without going through the setRadius method. So there is no way to make sure
 * that the value of radiusB is correctly set at all times. If you make them
 * private now, other classes that are accessing the fields directly will break.
 *
 * The class should have been coded with proper encapsulation of the fields in
 * the first place.
 */
class Elliptical {

    public int radiusA, radiusB;
    public int sum = 100;

    public void setRadius(int r) {
        if (r > 99) {
            throw new IllegalArgumentException();
        }
        radiusA = r;
        radiusB = sum - radiusA;

    }
}
