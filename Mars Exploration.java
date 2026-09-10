import java.io.*;

public class Main {

    public static int marsExploration(String s) {
        int changes = 0;

        for (int i = 0; i < s.length(); i++) {
            char expected;

            // SOS pattern: S, O, S
            if (i % 3 == 1) {
                expected = 'O';
            } else {
                expected = 'S';
            }

            if (s.charAt(i) != expected) {
                changes++;
            }
        }

        return changes;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine().trim();

        System.out.println(marsExploration(s));
    }
}
