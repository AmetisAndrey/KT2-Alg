
import java.io.*;
import java.util.*;

public class Main {

    static final long MOD = 1_000_000_007L;
    static final long NEG_INF = Long.MIN_VALUE / 4;

    public static void main(String[] args) throws Exception {
        solve1();
    }

    // ==================================================
    // ЗАДАЧА 1

    static void solve1() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        String s = fs.next();
        int n = s.length();

        boolean[][] palindrome = new boolean[n][n];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    if (j - i <= 1) {
                        palindrome[i][j] = true;
                    } else {
                        palindrome[i][j] = palindrome[i + 1][j - 1];
                    }
                }
            }
        }

        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);

        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (palindrome[i][j]) {
                    dp[j + 1] = Math.min(dp[j + 1], dp[i] + 1);
                }
            }
        }

        System.out.println(dp[n]);
    }

    // ==================================================
    // ЗАДАЧА 2

    static void solve2() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int m = fs.nextInt();

        char[][] grid = new char[n][];

        for (int i = 0; i < n; i++) {
            grid[i] = fs.next().toCharArray();
        }

        if (grid[0][0] == '#' || grid[n - 1][m - 1] == '#') {
            System.out.println(0);
            return;
        }

        long[] dp = new long[m];

        dp[0] = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (grid[i][j] == '#') {
                    dp[j] = 0;
                } else if (j > 0) {
                    dp[j] = (dp[j] + dp[j - 1]) % MOD;
                }
            }
        }

        System.out.println(dp[m - 1]);
    }

    // ==================================================
    // ЗАДАЧА 3

    static void solve3() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();

        long[] value = new long[n];

        for (int i = 0; i < n; i++) {
            value[i] = fs.nextLong();
        }

        int[] head = new int[n];
        Arrays.fill(head, -1);

        int[] to = new int[2 * (n - 1)];
        int[] next = new int[2 * (n - 1)];

        int edgeCount = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = fs.nextInt() - 1;
            int v = fs.nextInt() - 1;

            to[edgeCount] = v;
            next[edgeCount] = head[u];
            head[u] = edgeCount++;

            to[edgeCount] = u;
            next[edgeCount] = head[v];
            head[v] = edgeCount++;
        }

        int[] parent = new int[n];
        int[] order = new int[n];
        int[] stack = new int[n];

        Arrays.fill(parent, -1);

        int top = 0;
        int orderSize = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];

            order[orderSize++] = u;

            for (int e = head[u]; e != -1; e = next[e]) {
                int v = to[e];

                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                stack[top++] = v;
            }
        }

        long[] dp0 = new long[n];
        long[] dp1 = new long[n];

        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];

            dp0[u] = 0;
            dp1[u] = value[u];

            for (int e = head[u]; e != -1; e = next[e]) {
                int v = to[e];

                if (parent[v] != u) {
                    continue;
                }

                dp0[u] += Math.max(dp0[v], dp1[v]);
                dp1[u] += dp0[v];
            }
        }

        System.out.println(Math.max(dp0[0], dp1[0]));
    }

    // ==================================================
    // ЗАДАЧА 4

    static void solve4() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();

        long[] a = new long[n];
        long[] prefix = new long[n + 1];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextLong();
            prefix[i + 1] = prefix[i] + a[i];
        }

        if (n == 1) {
            System.out.println(0);
            return;
        }

        long[][] dp = new long[n][n];
        int[][] opt = new int[n][n];

        for (int i = 0; i < n; i++) {
            opt[i][i] = i;
        }

        for (int len = 2; len <= n; len++) {
            for (int l = 0; l + len <= n; l++) {

                int r = l + len - 1;

                long sum = prefix[r + 1] - prefix[l];
                long best = Long.MAX_VALUE;

                int left = opt[l][r - 1];
                int right = opt[l + 1][r];

                left = Math.max(left, l);
                right = Math.min(right, r - 1);

                for (int k = left; k <= right; k++) {
                    long candidate =
                            dp[l][k] + dp[k + 1][r] + sum;

                    if (candidate < best) {
                        best = candidate;
                        opt[l][r] = k;
                    }
                }

                dp[l][r] = best;
            }
        }

        System.out.println(dp[0][n - 1]);
    }

    // ==================================================
    // ЗАДАЧА 5

    static void solve5() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int m = fs.nextInt();
        int k = fs.nextInt();

        long[][] prev = new long[m][k + 1];
        long[][] cur = new long[m][k + 1];

        for (int j = 0; j < m; j++) {
            Arrays.fill(prev[j], NEG_INF);
            Arrays.fill(cur[j], NEG_INF);
        }

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < m; j++) {
                Arrays.fill(cur[j], NEG_INF);
            }

            for (int j = 0; j < m; j++) {
                long x = fs.nextLong();

                if (i == 0 && j == 0) {
                    cur[j][0] = x;

                    if (x < 0 && k >= 1) {
                        cur[j][1] = 0;
                    }

                    continue;
                }

                for (int used = 0; used <= k; used++) {

                    long bestPrev = NEG_INF;

                    if (i > 0) {
                        bestPrev = Math.max(bestPrev, prev[j][used]);
                    }

                    if (j > 0) {
                        bestPrev = Math.max(bestPrev, cur[j - 1][used]);
                    }

                    if (bestPrev != NEG_INF) {
                        cur[j][used] = bestPrev + x;
                    }

                    if (x < 0 && used > 0) {

                        long shieldPrev = NEG_INF;

                        if (i > 0) {
                            shieldPrev = Math.max(
                                    shieldPrev,
                                    prev[j][used - 1]
                            );
                        }

                        if (j > 0) {
                            shieldPrev = Math.max(
                                    shieldPrev,
                                    cur[j - 1][used - 1]
                            );
                        }

                        if (shieldPrev != NEG_INF) {
                            cur[j][used] = Math.max(
                                    cur[j][used],
                                    shieldPrev
                            );
                        }
                    }
                }
            }

            long[][] temp = prev;
            prev = cur;
            cur = temp;
        }

        long answer = NEG_INF;

        for (int used = 0; used <= k; used++) {
            answer = Math.max(answer, prev[m - 1][used]);
        }

        System.out.println(answer);
    }

    // ==================================================
    // ЗАДАЧА 6

    static void solve6() throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int k = fs.nextInt();

        long[] value = new long[n];

        for (int i = 0; i < n; i++) {
            value[i] = fs.nextLong();
        }

        int[] head = new int[n];
        Arrays.fill(head, -1);

        int[] to = new int[2 * (n - 1)];
        int[] next = new int[2 * (n - 1)];

        int edgeCount = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = fs.nextInt() - 1;
            int v = fs.nextInt() - 1;

            to[edgeCount] = v;
            next[edgeCount] = head[u];
            head[u] = edgeCount++;

            to[edgeCount] = u;
            next[edgeCount] = head[v];
            head[v] = edgeCount++;
        }

        int[] parent = new int[n];
        int[] order = new int[n];
        int[] stack = new int[n];

        Arrays.fill(parent, -1);

        int top = 0;
        int orderSize = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];

            order[orderSize++] = u;

            for (int e = head[u]; e != -1; e = next[e]) {
                int v = to[e];

                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                stack[top++] = v;
            }
        }

        long[][] dp = new long[n][k + 1];
        int[] subtreeSize = new int[n];

        for (int u = 0; u < n; u++) {
            Arrays.fill(dp[u], NEG_INF);
        }

        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];

            dp[u][1] = value[u];
            subtreeSize[u] = 1;

            for (int e = head[u]; e != -1; e = next[e]) {
                int v = to[e];

                if (parent[v] != u) {
                    continue;
                }

                long[] newDp = dp[u].clone();

                int maxCurrent = Math.min(k, subtreeSize[u]);
                int maxChild = Math.min(k, subtreeSize[v]);

                for (int i = 1; i <= maxCurrent; i++) {
                    if (dp[u][i] == NEG_INF) {
                        continue;
                    }

                    for (int j = 1; j <= maxChild && i + j <= k; j++) {
                        if (dp[v][j] == NEG_INF) {
                            continue;
                        }

                        newDp[i + j] = Math.max(
                                newDp[i + j],
                                dp[u][i] + dp[v][j]
                        );
                    }
                }

                dp[u] = newDp;

                subtreeSize[u] = Math.min(
                        k,
                        subtreeSize[u] + subtreeSize[v]
                );
            }
        }

        long answer = NEG_INF;

        for (int u = 0; u < n; u++) {
            answer = Math.max(answer, dp[u][k]);
        }

        System.out.println(answer);
    }

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
                    throw new EOFException("Недостаточно входных данных");
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
