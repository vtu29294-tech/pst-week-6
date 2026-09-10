import java.io.*;
import java.util.*;

public class Main {

    static long similarity(String s) {
        int n = s.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] = Math.min(right - i + 1, z[i - left]);
            }

            while (i + z[i] < n &&
                   s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }

        long answer = n;

        for (int i = 1; i < n; i++) {
            answer += z[i];
        }

        return answer;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(br.readLine().trim());

        StringBuilder result = new StringBuilder();

        while (t-- > 0) {
            String s = br.readLine().trim();
            result.append(similarity(s)).append('\n');
        }

        System.out.print(result);
    }
}

