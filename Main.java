
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("Задача 1");
        task1();

        System.out.println("\nЗадача 2");
        task2();

        System.out.println("\nЗадача 3");
        task3();

        System.out.println("\nЗадача 4");
        task4();

        System.out.println("\nЗадача 5");
        task5();

        System.out.println("\nЗадача 6");
        task6();
    }

    static void task1() {

        int[] prices = {73, 74, 75, 71, 69, 72, 76, 73};

        int n = prices.length;
        int[] result = new int[n];

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {

            while (!stack.isEmpty() &&
                    prices[i] > prices[stack.peek()]) {

                int index = stack.pop();
                result[index] = i - index;
            }

            stack.push(i);
        }

        System.out.println("Prices: " + Arrays.toString(prices));
        System.out.println("Result: " + Arrays.toString(result));
    }

    static void task2() {

        char[][] grid = {
                {'S', '.', '.', '#', '.'},
                {'#', '#', '.', '#', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '#', '#', '#', '.'},
                {'.', '.', '.', 'T', '.'}
        };

        int n = grid.length;
        int m = grid[0].length;

        int[][] dist = new int[n][m];

        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        int startX = 0;
        int startY = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                }
            }
        }

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        Queue<int[]> queue = new ArrayDeque<>();

        queue.add(new int[]{startX, startY});
        dist[startX][startY] = 0;

        while (!queue.isEmpty()) {

            int[] current = queue.poll();

            int x = current[0];
            int y = current[1];

            if (grid[x][y] == 'T') {
                System.out.println("Shortest path: " + dist[x][y]);
                return;
            }

            for (int d = 0; d < 4; d++) {

                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }

                if (grid[nx][ny] == '#') {
                    continue;
                }

                if (dist[nx][ny] != -1) {
                    continue;
                }

                dist[nx][ny] = dist[x][y] + 1;

                queue.add(new int[]{nx, ny});
            }
        }

        System.out.println("Shortest path: -1");
    }

    static void task3() {

        int[] next = {
                2, 3, 4, 5, 3
        };

        int start = 1;

        int slow = start;
        int fast = start;

        while (true) {

            if (slow == -1) {
                System.out.println("NO");
                return;
            }

            slow = next[slow - 1];

            if (fast == -1) {
                System.out.println("NO");
                return;
            }

            fast = next[fast - 1];

            if (fast == -1) {
                System.out.println("NO");
                return;
            }

            fast = next[fast - 1];

            if (slow == -1 || fast == -1) {
                System.out.println("NO");
                return;
            }

            if (slow == fast) {
                System.out.println("YES");
                return;
            }
        }
    }

    static void task4() {

        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };

        int n = matrix.length;
        int m = matrix[0].length;

        int[] heights = new int[m];

        int maxArea = 0;

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < m; j++) {

                if (matrix[i][j] == '1') {
                    heights[j]++;
                } else {
                    heights[j] = 0;
                }
            }

            maxArea = Math.max(maxArea, largestRectangle(heights));
        }

        System.out.println("Maximum rectangle area: " + maxArea);
    }

    static int largestRectangle(int[] heights) {

        Stack<Integer> stack = new Stack<>();

        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {

            int currentHeight = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() &&
                    heights[stack.peek()] > currentHeight) {

                int height = heights[stack.pop()];

                int width;

                if (stack.isEmpty()) {
                    width = i;
                } else {
                    width = i - stack.peek() - 1;
                }

                maxArea = Math.max(maxArea, height * width);
            }

            stack.push(i);
        }

        return maxArea;
    }

    static void task5() {

        char[][] grid = {
                {'S', '.', 'a', '#', 'T'},
                {'#', '#', '.', '#', '.'},
                {'.', '.', '.', 'A', '.'},
                {'.', '#', '#', '#', '.'},
                {'.', '.', '.', '.', '.'}
        };

        int n = grid.length;
        int m = grid[0].length;

        int startX = 0;
        int startY = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                }
            }
        }

        int[][][] dist = new int[n][m][1 << 10];

        for (int[][] row : dist) {
            for (int[] cell : row) {
                Arrays.fill(cell, -1);
            }
        }

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        Queue<int[]> queue = new ArrayDeque<>();

        queue.add(new int[]{startX, startY, 0});
        dist[startX][startY][0] = 0;

        while (!queue.isEmpty()) {

            int[] current = queue.poll();

            int x = current[0];
            int y = current[1];
            int mask = current[2];

            if (grid[x][y] == 'T') {
                System.out.println("Shortest path with keys: " +
                        dist[x][y][mask]);
                return;
            }

            for (int d = 0; d < 4; d++) {

                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }

                char cell = grid[nx][ny];

                if (cell == '#') {
                    continue;
                }

                if (cell >= 'A' && cell <= 'J') {

                    int key = cell - 'A';

                    if ((mask & (1 << key)) == 0) {
                        continue;
                    }
                }

                int newMask = mask;

                if (cell >= 'a' && cell <= 'j') {
                    newMask |= 1 << (cell - 'a');
                }

                if (dist[nx][ny][newMask] != -1) {
                    continue;
                }

                dist[nx][ny][newMask] =
                        dist[x][y][mask] + 1;

                queue.add(new int[]{nx, ny, newMask});
            }
        }

        System.out.println("Shortest path with keys: -1");
    }

    static void task6() {

        int[] nums = {1, 3, 4, 2, 2};

        int slow = nums[0];
        int fast = nums[0];

        do {
            slow = nums[slow];
            fast = nums[nums[fast]];

        } while (slow != fast);

        slow = nums[0];

        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        System.out.println("Duplicate number: " + slow);
    }
}
