import java.io.*;
import java.util.*;

public class Solution {

    public static int palindromeIndex(String s) {
        int left = 0;
        int right = s.length() - 1;

        // Find the first mismatch.
        while (left < right && s.charAt(left) == s.charAt(right)) {
            left++;
            right--;
        }

        // Already a palindrome.
        if (left >= right) {
            return -1;
        }

        // Try removing the left character.
        if (isPalindrome(s, left + 1, right)) {
            return left;
        }

        // Try removing the right character.
        if (isPalindrome(s, left, right - 1)) {
            return right;
        }

        // No solution.
        return -1;
    }

    private static boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int q = Integer.parseInt(br.readLine());

        while (q-- > 0) {
            String s = br.readLine();
            System.out.println(palindromeIndex(s));
        }
    }
}
