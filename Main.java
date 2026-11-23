
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("========== TASK 1 ==========");
        task1();

        System.out.println("\n========== TASK 2 ==========");
        task2();

        System.out.println("\n========== TASK 3 ==========");
        task3();

        System.out.println("\n========== TASK 4 ==========");
        task4();

        System.out.println("\n========== TASK 5 ==========");
        task5();

        System.out.println("\n========== TASK 6 ==========");
        task6();
    }

    // TASK 1: Binary Search
    static void task1() {
        int[] nums = {1, 2, 3, 3, 5, 7};
        int target = 3;

        int left = 0;
        int right = nums.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("Target: " + target);
        System.out.println("Result: " + left);
    }

    // TASK 2: Prefix Sum
    static void task2() {
        int[] nums = {1, 2, 3, 4, 5};

        long[] prefix = new long[nums.length + 1];

        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int[][] queries = {
                {0, 2},
                {1, 3},
                {2, 4}
        };

        System.out.println("Array: " + Arrays.toString(nums));

        for (int[] query : queries) {
            int L = query[0];
            int R = query[1];

            long sum = prefix[R + 1] - prefix[L];

            System.out.println(
                    "L = " + L + ", R = " + R +
                            ", Sum = " + sum
            );
        }
    }

    // TASK 3: Merge Intervals
    static void task3() {
        int[][] intervals = {
                {1, 3},
                {2, 6},
                {8, 10},
                {10, 12},
                {15, 18}
        };

        Arrays.sort(intervals, (a, b) ->
                Integer.compare(a[0], b[0])
        );

        List<int[]> result = new ArrayList<>();

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            if (result.isEmpty() ||
                    start > result.get(result.size() - 1)[1]) {

                result.add(new int[]{start, end});

            } else {
                int[] last = result.get(result.size() - 1);
                last[1] = Math.max(last[1], end);
            }
        }

        System.out.println("Original intervals:");

        for (int[] interval : intervals) {
            System.out.println(Arrays.toString(interval));
        }

        System.out.println("Merged intervals:");

        for (int[] interval : result) {
            System.out.println(Arrays.toString(interval));
        }
    }

    // TASK 4: Capacity To Ship Packages
    static boolean canShip(
            int[] weights,
            int days,
            long capacity
    ) {
        int usedDays = 1;
        long currentWeight = 0;

        for (int weight : weights) {
            if (currentWeight + weight > capacity) {
                usedDays++;
                currentWeight = 0;
            }

            currentWeight += weight;

            if (usedDays > days) {
                return false;
            }
        }

        return true;
    }

    static void task4() {
        int[] weights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int days = 5;

        long left = 0;
        long right = 0;

        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }

        while (left < right) {
            long mid = left + (right - left) / 2;

            if (canShip(weights, days, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        System.out.println("Weights: " + Arrays.toString(weights));
        System.out.println("Days: " + days);
        System.out.println("Minimum capacity: " + left);
    }

    // TASK 5: Subarray Sum Equals K
    static void task5() {
        int[] nums = {1, 2, 1, 2, 1};
        long k = 3;

        Map<Long, Long> map = new HashMap<>();

        map.put(0L, 1L);

        long sum = 0;
        long count = 0;

        for (int x : nums) {
            sum += x;

            count += map.getOrDefault(sum - k, 0L);

            map.put(
                    sum,
                    map.getOrDefault(sum, 0L) + 1
            );
        }

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("K: " + k);
        System.out.println("Number of subarrays: " + count);
    }

    // TASK 6: Meeting Rooms II
    static void task6() {
        int[][] meetings = {
                {0, 30},
                {5, 10},
                {15, 20},
                {20, 25}
        };

        int n = meetings.length;

        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = meetings[i][0];
            ends[i] = meetings[i][1];
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        int rooms = 0;
        int maxRooms = 0;
        int j = 0;

        for (int i = 0; i < n; i++) {
            if (starts[i] >= ends[j]) {
                rooms--;
                j++;
            }

            rooms++;

            maxRooms = Math.max(maxRooms, rooms);
        }

        System.out.println("Meetings:");

        for (int[] meeting : meetings) {
            System.out.println(Arrays.toString(meeting));
        }

        System.out.println("Minimum rooms: " + maxRooms);
    }
}
