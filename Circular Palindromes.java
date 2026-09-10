import java.io.*;
import java.util.*;

public class Solution {

    static int n;
    static int m;

    // Manacher radii
    static int[] odd;
    static int[] even;

    // Values for palindromes touching the left/right boundary
    static int[] leftBest;
    static int[] rightBest;

    // Iterative segment tree:
    // range update = max
    // point query = max along root-to-leaf path
    static int segSize;
    static int[] tree;

    // ------------------------------------------------------------
    // Standard Manacher for odd-length palindromes.
    //
    // odd[i] = radius including center.
    // Palindrome length = 2 * odd[i] - 1
    // ------------------------------------------------------------
    static void manacherOdd(char[] s) {
        odd = new int[s.length];

        int l = 0;
        int r = -1;

        for (int i = 0; i < s.length; i++) {
            int k = (i > r) ? 1 : Math.min(odd[l + r - i], r - i + 1);

            while (i - k >= 0 &&
                   i + k < s.length &&
                   s[i - k] == s[i + k]) {
                k++;
            }

            odd[i] = k;

            if (i + k - 1 > r) {
                l = i - k + 1;
                r = i + k - 1;
            }
        }
    }

    // ------------------------------------------------------------
    // Standard Manacher for even-length palindromes.
    //
    // even[i] = radius.
    // Palindrome is [i-even[i], i+even[i)-1]
    // Length = 2 * even[i]
    // ------------------------------------------------------------
    static void manacherEven(char[] s) {
        even = new int[s.length];

        int l = 0;
        int r = -1;

        for (int i = 0; i < s.length; i++) {
            int k = (i > r) ? 0 : Math.min(even[l + r - i + 1], r - i + 1);

            while (i - k - 1 >= 0 &&
                   i + k < s.length &&
                   s[i - k - 1] == s[i + k]) {
                k++;
            }

            even[i] = k;

            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
    }

    // ------------------------------------------------------------
    // Segment tree initialization
    // ------------------------------------------------------------
    static void initTree(int n) {
        segSize = 1;
        while (segSize < n) {
            segSize <<= 1;
        }

        tree = new int[segSize << 1];
    }

    // Range max update on [l, r]
    static void rangeMax(int l, int r, int value) {
        if (l > r) {
            return;
        }

        l += segSize;
        r += segSize;

        while (l <= r) {
            if ((l & 1) == 1) {
                tree[l] = Math.max(tree[l], value);
                l++;
            }

            if ((r & 1) == 0) {
                tree[r] = Math.max(tree[r], value);
                r--;
            }

            l >>= 1;
            r >>= 1;
        }
    }

    // Point query
    static int pointQuery(int pos) {
        int result = 1;
        int p = pos + segSize;

        while (p > 0) {
            result = Math.max(result, tree[p]);
            p >>= 1;
        }

        return result;
    }

    // ------------------------------------------------------------
    // Process one Manacher radius.
    //
    // parity = 0 -> odd
    // parity = 1 -> even
    // ------------------------------------------------------------
    static void process(int center, int parity) {
        int radius = (parity == 0) ? odd[center] : even[center];

        /*
         * Maximum palindrome length that can fit in a rotation
         * of size n.
         */
        int length;

        if (parity == 0) {
            length = 2 * radius - 1;
        } else {
            length = 2 * radius;
        }

        if (length > n) {
            length = n;

            // Preserve parity.
            if (parity == 0 && (length & 1) == 0) {
                length--;
            }
            if (parity == 1 && (length & 1) == 1) {
                length--;
            }

            if (length <= 0) {
                return;
            }

            radius = (parity == 0)
                    ? (length + 1) / 2
                    : length / 2;
        }

        if (parity == 0) {
            // Odd palindrome:
            // [center-radius+1, center+radius-1]
            if (radius <= 1) {
                return;
            }

            int lx = center - radius + 1;
            int rx = center + radius - 1;

            // Rotations where this palindrome crosses a boundary.
            int updateL = Math.max(0, rx - n + 1);
            int updateR = Math.min(n - 1, lx);

            rangeMax(updateL, updateR, length);

            leftBest[lx] = Math.max(leftBest[lx], length);
            rightBest[rx] = Math.max(rightBest[rx], length);

        } else {
            // Even palindrome:
            // [center-radius, center+radius-1]
            if (radius <= 0) {
                return;
            }

            int lx = center - radius;
            int rx = center + radius - 1;

            int updateL = Math.max(0, rx - n + 1);
            int updateR = Math.min(n - 1, lx);

            rangeMax(updateL, updateR, length);

            leftBest[lx] = Math.max(leftBest[lx], length);
            rightBest[rx] = Math.max(rightBest[rx], length);
        }
    }

    static int[] solve(String str) {
        n = str.length();

        // s + s[0 .. n-2]
        char[] doubled = new char[2 * n - 1];

        for (int i = 0; i < n; i++) {
            doubled[i] = str.charAt(i);
        }

        for (int i = 0; i < n - 1; i++) {
            doubled[n + i] = str.charAt(i);
        }

        m = doubled.length;

        manacherOdd(doubled);
        manacherEven(doubled);

        leftBest = new int[m];
        rightBest = new int[m];

        initTree(n);

        /*
         * Every center contributes its maximum palindrome.
         */
        for (int i = 0; i < m; i++) {
            process(i, 0); // odd
            process(i, 1); // even
        }

        /*
         * If a palindrome of length L exists starting at one
         * boundary, moving its center one position away can reduce
         * its usable length by at most 2.
         *
         * Propagate these values toward the rotation positions.
         */
        for (int i = 1; i < m; i++) {
            leftBest[i] = Math.max(leftBest[i], leftBest[i - 1] - 2);
        }

        for (int i = m - 2; i >= 0; i--) {
            rightBest[i] = Math.max(rightBest[i], rightBest[i + 1] - 2);
        }

        int[] answer = new int[n];

        for (int i = 0; i < n; i++) {
            int best = pointQuery(i);

            best = Math.max(best, leftBest[i]);

            int rightIndex = i + n - 1;
            best = Math.max(best, rightBest[rightIndex]);

            answer[i] = best;
        }

        return answer;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int nInput = Integer.parseInt(br.readLine().trim());
        String s = br.readLine().trim();

        int[] ans = solve(s);

        StringBuilder out = new StringBuilder();

        for (int x : ans) {
            out.append(x).append('\n');
        }

        System.out.print(out);
    }
}
