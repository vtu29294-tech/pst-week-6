import java.io.*;

public class Main {

    public static String twoStrings(String s1, String s2) {
        boolean[] chars = new boolean[26];

        // Store characters of s1
        for (char c : s1.toCharArray()) {
            chars[c - 'a'] = true;
        }

        // Check characters of s2
        for (char c : s2.toCharArray()) {
            if (chars[c - 'a']) {
                return "YES";
            }
        }

        return "NO";
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(br.readLine());

        while (t-- > 0) {
            String s1 = br.readLine();
            String s2 = br.readLine();

            System.out.println(twoStrings(s1, s2));
        }
    }
}
