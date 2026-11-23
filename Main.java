
import java.util.*;

public class Main {

    public static void main(String[] args) {
        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
    }

    // ЗАДАЧА 1.
    // Суммы значений вершин на каждом уровне дерева.

    static void task1() {
        System.out.println("Задача 1:");

        int[] value = {5, 3, 8, 2, 4, 7, 9};
        int[] left  = {1, 3, 5, -1, -1, -1, -1};
        int[] right = {2, 4, 6, -1, -1, -1, -1};

        int n = value.length;

        int[] queue = new int[n];
        int head = 0;
        int tail = 0;

        queue[tail++] = 0;

        List<Long> sums = new ArrayList<>();

        while (head < tail) {
            int levelSize = tail - head;
            long sum = 0;

            for (int i = 0; i < levelSize; i++) {
                int v = queue[head++];
                sum += value[v];

                if (left[v] != -1) {
                    queue[tail++] = left[v];
                }

                if (right[v] != -1) {
                    queue[tail++] = right[v];
                }
            }

            sums.add(sum);
        }

        for (long sum : sums) {
            System.out.print(sum + " ");
        }

        System.out.println("\n");
    }


    // ЗАДАЧА 2.
    // k наиболее часто встречающихся чисел.

    static void task2() {
        System.out.println("Задача 2:");

        int[] a = {1, 1, 1, 2, 2, 3, 3, 3, 2, 4};
        int k = 3;

        Map<Integer, Integer> freq = new HashMap<>();

        for (int x : a) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        List<Integer> numbers = new ArrayList<>(freq.keySet());

        numbers.sort((x, y) -> {
            int byFrequency = Integer.compare(
                    freq.get(y), freq.get(x)
            );

            if (byFrequency != 0) {
                return byFrequency;
            }

            return Integer.compare(x, y);
        });

        for (int i = 0; i < k; i++) {
            System.out.print(numbers.get(i) + " ");
        }

        System.out.println("\n");
    }


    // ЗАДАЧА 3.
    // Медиана после каждого поступившего числа.

    static void task3() {
        System.out.println("Задача 3:");

        int[] a = {5, 2, 10, 4};

        // Левая половина — max-heap.
        PriorityQueue<Long> left =
                new PriorityQueue<>(Collections.reverseOrder());

        // Правая половина — min-heap.
        PriorityQueue<Long> right =
                new PriorityQueue<>();

        for (int x : a) {
            long value = x;

            if (left.isEmpty() || value <= left.peek()) {
                left.add(value);
            } else {
                right.add(value);
            }

            // Балансируем размеры куч.
            if (left.size() > right.size() + 1) {
                right.add(left.poll());
            }

            if (right.size() > left.size()) {
                left.add(right.poll());
            }

            double median;

            if (left.size() == right.size()) {
                median = (left.peek() + right.peek()) / 2.0;
            } else {
                median = left.peek();
            }

            System.out.printf(Locale.US, "%.1f ", median);
        }

        System.out.println("\n");
    }


    // ЗАДАЧА 4.
    // Максимальная сумма маршрута в бинарном дереве.

    static void task4() {
        System.out.println("Задача 4:");

        int[] value = {5, 3, 8, 2, 4, 7, 9};
        int[] left  = {1, 3, 5, -1, -1, -1, -1};
        int[] right = {2, 4, 6, -1, -1, -1, -1};

        int n = value.length;

        // Получаем порядок обхода дерева.
        // Затем обработаем его в обратном порядке.
        int[] order = new int[n];
        int[] stack = new int[n];

        int stackSize = 0;
        int orderSize = 0;

        stack[stackSize++] = 0;

        while (stackSize > 0) {
            int v = stack[--stackSize];

            order[orderSize++] = v;

            if (left[v] != -1) {
                stack[stackSize++] = left[v];
            }

            if (right[v] != -1) {
                stack[stackSize++] = right[v];
            }
        }

        // down[v] — максимальная сумма пути,
        // который начинается в v и идет вниз.
        long[] down = new long[n];

        long answer = Long.MIN_VALUE;

        for (int i = orderSize - 1; i >= 0; i--) {
            int v = order[i];

            long leftPath = 0;
            long rightPath = 0;

            if (left[v] != -1) {
                leftPath = Math.max(0, down[left[v]]);
            }

            if (right[v] != -1) {
                rightPath = Math.max(0, down[right[v]]);
            }

            // Лучший путь, проходящий через текущую вершину.
            long currentPath =
                    (long) value[v] + leftPath + rightPath;

            answer = Math.max(answer, currentPath);

            // Лучший путь из вершины вниз.
            down[v] = value[v] + Math.max(leftPath, rightPath);
        }

        System.out.println(answer);
        System.out.println();
    }


    // ЗАДАЧА 5.
    // k-я наименьшая сумма A[i] + B[j].

    static class Pair {
        long sum;
        int i;
        int j;

        Pair(long sum, int i, int j) {
            this.sum = sum;
            this.i = i;
            this.j = j;
        }
    }

    static void task5() {
        System.out.println("Задача 5:");

        int[] a = {1, 3, 5};
        int[] b = {2, 4};

        int k = 4;

        // Min-heap пар.
        PriorityQueue<Pair> pq = new PriorityQueue<>(
                (x, y) -> Long.compare(x.sum, y.sum)
        );

        // Достаточно начать с первых min(n, k) строк.
        for (int i = 0; i < Math.min(a.length, k); i++) {
            pq.add(new Pair((long) a[i] + b[0], i, 0));
        }

        long answer = 0;

        for (int count = 0; count < k; count++) {
            Pair current = pq.poll();

            answer = current.sum;

            int nextJ = current.j + 1;

            if (nextJ < b.length) {
                pq.add(new Pair(
                        (long) a[current.i] + b[nextJ],
                        current.i,
                        nextJ
                ));
            }
        }

        System.out.println(answer);
        System.out.println();
    }


    // ЗАДАЧА 6.
    // Медиана каждого скользящего окна.

    static class MedianWindow {

        TreeMap<Long, Integer> lower = new TreeMap<>();
        TreeMap<Long, Integer> upper = new TreeMap<>();

        int lowerSize = 0;
        int upperSize = 0;

        void addToMap(TreeMap<Long, Integer> map, long x) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        void removeFromMap(TreeMap<Long, Integer> map, long x) {
            int count = map.get(x);

            if (count == 1) {
                map.remove(x);
            } else {
                map.put(x, count - 1);
            }
        }

        void add(long x) {
            if (lower.isEmpty() || x <= lower.lastKey()) {
                addToMap(lower, x);
                lowerSize++;
            } else {
                addToMap(upper, x);
                upperSize++;
            }

            balance();
        }

        void remove(long x) {
            if (lower.containsKey(x)) {
                removeFromMap(lower, x);
                lowerSize--;
            } else {
                removeFromMap(upper, x);
                upperSize--;
            }

            balance();
        }

        void balance() {
            // В lower должно быть столько же элементов,
            // сколько в upper, либо на один больше.

            while (lowerSize > upperSize + 1) {
                long x = lower.lastKey();

                removeFromMap(lower, x);
                lowerSize--;

                addToMap(upper, x);
                upperSize++;
            }

            while (lowerSize < upperSize) {
                long x = upper.firstKey();

                removeFromMap(upper, x);
                upperSize--;

                addToMap(lower, x);
                lowerSize++;
            }
        }

        double getMedian() {
            if (lowerSize > upperSize) {
                return lower.lastKey();
            }

            return (lower.lastKey() + upper.firstKey()) / 2.0;
        }
    }

    static void task6() {
        System.out.println("Задача 6:");

        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;

        MedianWindow window = new MedianWindow();

        for (int i = 0; i < k; i++) {
            window.add(a[i]);
        }

        // Первое окно.
        System.out.printf(
                Locale.US, "%.1f ", window.getMedian()
        );

        // Остальные окна.
        for (int i = k; i < a.length; i++) {
            window.remove(a[i - k]);
            window.add(a[i]);

            System.out.printf(
                    Locale.US, "%.1f ", window.getMedian()
            );
        }

        System.out.println();
    }
}
