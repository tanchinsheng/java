package Lesson4;

/**
 * Created by yaodh on 2014/12/6.
 * 1. NumberOfDiscIntersections
 * Compute intersections between sequence of discs.
 * Given an array A of N integers, we draw N discs in a 2D plane such that the I-th disc is centered on (0,I) and has a radius of A[I]. We say that the J-th disc and K-th disc intersect if J ≠ K and J-th and K-th discs have at least one common point.
 * Write a function:
 * class Solution { public int solution(int[] A); }
 * that, given an array A describing N discs as explained above, returns the number of pairs of intersecting discs. For example, given N=6 and:
 * A[0] = 1  A[1] = 5  A[2] = 2
 * A[3] = 1  A[4] = 4  A[5] = 0
 * intersecting discs appear in eleven pairs of elements:
 * 0 and 1,
 * 0 and 2,
 * 0 and 4,
 * 1 and 2,
 * 1 and 3,
 * 1 and 4,
 * 1 and 5,
 * 2 and 3,
 * 2 and 4,
 * 3 and 4,
 * 4 and 5.
 * so the function should return 11.
 * The function should return −1 if the number of intersecting pairs exceeds 10,000,000.
 * Assume that:
 * N is an integer within the range [0..100,000];
 * each element of array A is an integer within the range [0..2147483647].
 * Complexity:
 * expected worst-case time complexity is O(N*log(N));
 * expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).
 * Elements of input arrays can be modified.
 */
public class NumberOfDiscIntersections {
    public int solution(int[] A) {
        int n = A.length;
        int[] sum = new int[n];
        for (int i = 0; i < n; i++) {
            int right = n - i - 1 >= A[i] ? i + A[i] : n - 1;
            sum[right]++;
        }
        for (int i = 1; i < n; i++) sum[i] += sum[i - 1];
        long ans = (long) n * (n - 1) / 2;
        for (int i = 0; i < n; i++) {
            int left = A[i] > i ? 0 : i - A[i];
            if (left > 0) ans -= sum[left - 1];
        }
        if (ans > 10000000) return -1;
        return (int) ans;
    }

    public static void main(String[] args) {
        int[] a = new int[50000];
        int ans = new NumberOfDiscIntersections().solution(a);
        System.out.println(ans);
    }
}
