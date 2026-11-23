
import java.io.*;
import java.util.*;

public class Main {

    static final long MOD = 1_000_000_007L;
    static final long INF = Long.MAX_VALUE / 4;

    public static void main(String[] args) throws Exception {

        solve1();
        solve2();
        solve3();
        solve4();
        solve5();
        solve6();
    }



    // ================= ЗАДАЧА 1 =================
    static void solve1() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        long[] h = new long[n];

        for (int i = 0; i < n; i++) {
            h[i] = fs.nextLong();
        }

        long[] dp = new long[n];

        dp[0] = 0;
        dp[1] = Math.abs(h[1] - h[0]);

        for (int i = 2; i < n; i++) {
            long fromOne = dp[i - 1] + Math.abs(h[i] - h[i - 1]);
            long fromTwo = dp[i - 2] + Math.abs(h[i] - h[i - 2]);

            dp[i] = Math.min(fromOne, fromTwo);
        }

        System.out.println(dp[n - 1]);
    }

    // ================= ЗАДАЧА 2 =================

    static void solve2() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int W = fs.nextInt();

        long[] dp = new long[W + 1];

        for (int i = 0; i < n; i++) {
            int w = fs.nextInt();
            long v = fs.nextLong();

            for (int j = W; j >= w; j--) {
                dp[j] = Math.max(dp[j], dp[j - w] + v);
            }
        }

        long answer = 0;

        for (int j = 0; j <= W; j++) {
            answer = Math.max(answer, dp[j]);
        }

        System.out.println(answer);
    }

    // ================= ЗАДАЧА 3 =================

    static void solve3() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        String s = fs.next();
        String t = fs.next();

        int n = s.length();
        int m = t.length();

        int[] dp = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            int diagonal = 0;

            for (int j = 1; j <= m; j++) {
                int old = dp[j];

                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] = diagonal + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }

                diagonal = old;
            }
        }

        System.out.println(dp[m]);
    }

    // ================= ЗАДАЧА 4 =================

    static void solve4() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int C = fs.nextInt();

        long[] bestProfit = new long[C + 1];
        int[] bestCount = new int[C + 1];

        Arrays.fill(bestProfit, -1);
        Arrays.fill(bestCount, Integer.MAX_VALUE);

        bestProfit[0] = 0;
        bestCount[0] = 0;

        for (int i = 0; i < n; i++) {
            int w = fs.nextInt();
            long p = fs.nextLong();

            for (int j = w; j <= C; j++) {
                if (bestProfit[j - w] == -1) {
                    continue;
                }

                long newProfit = bestProfit[j - w] + p;
                int newCount = bestCount[j - w] + 1;

                if (newProfit > bestProfit[j] ||
                        (newProfit == bestProfit[j] &&
                                newCount < bestCount[j])) {

                    bestProfit[j] = newProfit;
                    bestCount[j] = newCount;
                }
            }
        }

        if (bestProfit[C] == -1) {
            System.out.println(-1);
        } else {
            System.out.println(bestProfit[C] + " " + bestCount[C]);
        }
    }

    // ================= ЗАДАЧА 5 =================

    static class Node {
        int length;
        long count;

        Node(int length, long count) {
            this.length = length;
            this.count = count;
        }
    }

    static class Fenwick {
        int n;
        int[] length;
        long[] count;

        Fenwick(int n) {
            this.n = n;
            length = new int[n + 1];
            count = new long[n + 1];
        }

        void update(int pos, int len, long ways) {
            while (pos <= n) {
                if (len > length[pos]) {
                    length[pos] = len;
                    count[pos] = ways;
                } else if (len == length[pos]) {
                    count[pos] = (count[pos] + ways) % MOD;
                }

                pos += pos & -pos;
            }
        }

        Node query(int pos) {
            int bestLength = 0;
            long ways = 0;

            while (pos > 0) {
                if (length[pos] > bestLength) {
                    bestLength = length[pos];
                    ways = count[pos];
                } else if (length[pos] == bestLength && bestLength > 0) {
                    ways = (ways + count[pos]) % MOD;
                }

                pos -= pos & -pos;
            }

            if (bestLength == 0) {
                return new Node(0, 1);
            }

            return new Node(bestLength, ways);
        }
    }

    static void solve5() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        long[] a = new long[n];
        long[] sorted = new long[n];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextLong();
            sorted[i] = a[i];
        }

        Arrays.sort(sorted);

        int size = 0;

        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[size++] = sorted[i];
            }
        }

        Fenwick fenwick = new Fenwick(size);

        for (int i = 0; i < n; i++) {
            int pos = Arrays.binarySearch(sorted, 0, size, a[i]) + 1;

            Node prev = fenwick.query(pos - 1);

            int newLength = prev.length + 1;
            long newCount = prev.count;

            fenwick.update(pos, newLength, newCount);
        }

        Node answer = fenwick.query(size);

        System.out.println(answer.length + " " + answer.count);
    }

    // ================= ЗАДАЧА 6 =================

    static void solve6() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        String s = fs.next();
        String t = fs.next();

        int n = s.length();
        int m = t.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = n; i >= 0; i--) {
            for (int j = m; j >= 0; j--) {

                if (i == n) {
                    dp[i][j] = m - j;
                } else if (j == m) {
                    dp[i][j] = n - i;
                } else if (s.charAt(i) == t.charAt(j)) {
                    dp[i][j] = 1 + dp[i + 1][j + 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i + 1][j],
                            dp[i][j + 1]
                    );
                }
            }
        }

        StringBuilder answer = new StringBuilder();

        int i = 0;
        int j = 0;

        while (i < n && j < m) {
            char a = s.charAt(i);
            char b = t.charAt(j);

            if (a == b) {
                answer.append(a);
                i++;
                j++;
            } else {
                int takeS = dp[i + 1][j];
                int takeT = dp[i][j + 1];

                if (takeS < takeT) {
                    answer.append(a);
                    i++;
                } else if (takeT < takeS) {
                    answer.append(b);
                    j++;
                } else {
                    if (a < b) {
                        answer.append(a);
                        i++;
                    } else {
                        answer.append(b);
                        j++;
                    }
                }
            }
        }

        while (i < n) {
            answer.append(s.charAt(i++));
        }

        while (j < m) {
            answer.append(t.charAt(j++));
        }

        System.out.println(answer);
    }

    // ================= СЧИТЫВАНИЕ ВВОДА =================

    static class FastScanner {
        private final BufferedReader br;
        private StringTokenizer st;

        FastScanner(InputStream in) {
            br = new BufferedReader(new InputStreamReader(in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                String line = br.readLine();

                if (line == null) {
                    return null;
                }

                st = new StringTokenizer(line);
            }

            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }
}
