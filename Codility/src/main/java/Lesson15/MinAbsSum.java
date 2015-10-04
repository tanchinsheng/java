package Lesson15;

import java.util.Arrays;

/**
 * Created by yaodh on 2014/12/12.
 * 1. MinAbsSum
 * Given array of integers, find the lowest absolute sum of elements.
 * Task description
 * For a given array A of N integers and a sequence S of N integers from the set {−1, 1},
 * we define val(A, S) as follows:
 * val(A, S) = |sum{ A[i]*S[i] for i = 0..N−1 }|
 * (Assume that the sum of zero elements equals zero.)
 * For a given array A, we are looking for such a sequence S that minimizes val(A,S).
 * Write a function:
 * class Solution { public int solution(int[] A); }
 * that, given an array A of N integers, computes the minimum value of val(A,S)
 * from all possible values of val(A,S) for all possible sequences S of N integers from the set {−1, 1}.
 * For example, given array:
 * A[0] =  1
 * A[1] =  5
 * A[2] =  2
 * A[3] = -2
 * your function should return 0, since for S = [−1, 1, −1, 1], val(A, S) = 0, which is the minimum possible value.
 * Assume that:
 * N is an integer within the range [0..20,000];
 * each element of array A is an integer within the range [−100..100].
 * Complexity:
 * expected worst-case time complexity is O(N*max(|A|)^2);
 * expected worst-case space complexity is O(N+sum(|A|)),
 * beyond input storage (not counting the storage required for input arguments).
 * Elements of input arrays can be modified.
 */
public class MinAbsSum {
    // https://codility.com/demo/results/demoRXE7WF-V59/
//    static final int MAX_V = 105;
//
//    public void zeroOnePack(int[] dp, int w) {
//        for (int i = dp.length - 1; i >= w; i--) {
//            dp[i] = Math.max(dp[i], dp[i - w] + w);
//        }
//    }
//
//    public int solution(int[] A) {
//        int n = A.length;
//        if (n == 0) {
//            return 0;
//        }
//
//        int sum = 0, max = 0;
//        int c[] = new int[MAX_V];
//        for (int i = 0; i < n; i++) {
//            A[i] = Math.abs(A[i]);
//            c[A[i]]++;
//            sum += A[i];
//            max = Math.max(max, A[i]);
//        }
//
//        int dp[] = new int[sum / 2 + 1];
//        for (int i = 1; i <= max; i++) {
//            int k = 1;
//            while (c[i] > 0) {
//                if (k > c[i]) {
//                    k = c[i];
//                }
//                c[i] -= k;
//                zeroOnePack(dp, i * k);
//                k <<= 1;
//            }
//        }
//        return sum - dp[sum / 2] * 2;
//    }


    // https://codility.com/demo/results/demoCJT2RF-T4F/
    static final int MAX_V = 105;

    public int solution(int[] A) {
        int n = A.length;
        if (n == 0) {
            return 0;
        }

        int sum = 0, max = 0;
        int count[] = new int[MAX_V];
        for (int i = 0; i < n; i++) {
            A[i] = Math.abs(A[i]);
            count[A[i]]++;
            sum += A[i];
            max = Math.max(max, A[i]);
        }

        int dp[] = new int[sum + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        for (int i = 1; i <= max; i++) {
            if (count[i] <= 0) continue;
            for (int j = 0; j <= sum; j++) {
                if (dp[j] >= 0) {
                    dp[j] = count[i];
                } else if (j >= i && dp[j - i] > 0) {
                    dp[j] = dp[j - i] - 1;
                }
            }
        }
        int ans = sum;
        for (int p = 0; p * 2 <= sum; p++) {
            if (dp[p] >= 0) {
                ans = Math.min(ans, Math.abs(sum - p * 2));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int ans = new MinAbsSum().solution(new int[]{1, 5, 2, -2, 1});
        System.out.println(ans);
    }
}
