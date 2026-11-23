
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        solve1();
    }

    // Быстрый ввод
    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = read();
            } while (c <= 32 && c != -1);

            while (c > 32) {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }

    // Задача 1
    static void solve1() throws Exception {
        FastScanner fs = new FastScanner();
        int n = fs.nextInt();

        long[] intervals = new long[n];

        for (int i = 0; i < n; i++) {
            long l = fs.nextLong();
            long r = fs.nextLong();
            intervals[i] = (r << 32) | l;
        }

        Arrays.sort(intervals);

        long lastEnd = -1;
        int answer = 0;

        for (long interval : intervals) {
            long r = interval >>> 32;
            long l = interval & 0xffffffffL;

            if (l >= lastEnd) {
                answer++;
                lastEnd = r;
            }
        }

        System.out.println(answer);
    }

    // Задача 2. Стек с минимумом и максимумом
    static void solve2() throws Exception {
        FastScanner fs = new FastScanner();
        int q = fs.nextInt();

        long[] values = new long[q];
        long[] mins = new long[q];
        long[] maxs = new long[q];

        int size = 0;
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < q; i++) {
            String command = fs.next();

            switch (command) {
                case "PUSH": {
                    long x = fs.nextLong();

                    values[size] = x;

                    if (size == 0) {
                        mins[size] = x;
                        maxs[size] = x;
                    } else {
                        mins[size] = Math.min(mins[size - 1], x);
                        maxs[size] = Math.max(maxs[size - 1], x);
                    }

                    size++;
                    break;
                }

                case "POP":
                    size--;
                    break;

                case "MIN":
                    out.append(mins[size - 1]).append('\n');
                    break;

                case "MAX":
                    out.append(maxs[size - 1]).append('\n');
                    break;
            }
        }

        System.out.print(out);
    }

    // Задача 3. k-я порядковая статистика
    static void solve3() throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int k = fs.nextInt() - 1;

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextInt();
        }

        Random random = new Random();

        int left = 0;
        int right = n - 1;

        while (left <= right) {
            int pivot = a[left + random.nextInt(right - left + 1)];

            int lt = left;
            int i = left;
            int gt = right;

            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            if (k < lt) {
                right = lt - 1;
            } else if (k > gt) {
                left = gt + 1;
            } else {
                System.out.println(a[k]);
                return;
            }
        }
    }

    static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Задача 4. Максимальная прибыль от выполнения заказов
    static void solve4() throws Exception {
        FastScanner fs = new FastScanner();
        int n = fs.nextInt();

        long[] jobs = new long[n];

        for (int i = 0; i < n; i++) {
            long d = fs.nextLong();
            long p = fs.nextLong();

            jobs[i] = (p << 32) | d;
        }

        Arrays.sort(jobs);

        int[] parent = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        long answer = 0;

        for (int i = n - 1; i >= 0; i--) {
            long job = jobs[i];

            long profit = job >>> 32;
            int deadline = (int) (job & 0xffffffffL);

            int slot = find(parent, Math.min(deadline, n));

            if (slot > 0) {
                answer += profit;
                parent[slot] = find(parent, slot - 1);
            }
        }

        System.out.println(answer);
    }

    static int find(int[] parent, int x) {
        int root = x;

        while (parent[root] != root) {
            root = parent[root];
        }

        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    // Задача 5. LFU-кэш с вытеснением LRU при равной частоте
    static class Node {
        int key;
        int value;
        int frequency;

        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
        }
    }

    static class Bucket {
        Node head = new Node(-1, -1);
        Node tail = new Node(-1, -1);
        int size = 0;

        Bucket() {
            head.next = tail;
            tail.prev = head;
        }

        void addFirst(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
            size++;
        }

        void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
            size--;
        }

        Node removeLast() {
            if (size == 0) return null;

            Node node = tail.prev;
            remove(node);
            return node;
        }
    }

    static class LFUCache {
        int capacity;
        int minFrequency = 0;

        HashMap<Integer, Node> nodes = new HashMap<>();
        HashMap<Integer, Bucket> buckets = new HashMap<>();

        LFUCache(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            Node node = nodes.get(key);

            if (node == null) return -1;

            increaseFrequency(node);
            return node.value;
        }

        void put(int key, int value) {
            Node node = nodes.get(key);

            if (node != null) {
                node.value = value;
                increaseFrequency(node);
                return;
            }

            if (nodes.size() == capacity) {
                Bucket bucket = buckets.get(minFrequency);
                Node removed = bucket.removeLast();
                nodes.remove(removed.key);
            }

            Node newNode = new Node(key, value);
            nodes.put(key, newNode);

            buckets.computeIfAbsent(1, f -> new Bucket())
                    .addFirst(newNode);

            minFrequency = 1;
        }

        void increaseFrequency(Node node) {
            int oldFrequency = node.frequency;

            Bucket oldBucket = buckets.get(oldFrequency);
            oldBucket.remove(node);

            if (oldBucket.size == 0) {
                buckets.remove(oldFrequency);

                if (minFrequency == oldFrequency) {
                    minFrequency = oldFrequency + 1;
                }
            }

            node.frequency++;

            buckets.computeIfAbsent(node.frequency, f -> new Bucket())
                    .addFirst(node);
        }
    }

    static void solve5() throws Exception {
        FastScanner fs = new FastScanner();

        int capacity = fs.nextInt();
        int q = fs.nextInt();

        LFUCache cache = new LFUCache(capacity);
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < q; i++) {
            String command = fs.next();
            int key = fs.nextInt();

            if (command.equals("GET")) {
                out.append(cache.get(key)).append('\n');
            } else {
                int value = fs.nextInt();
                cache.put(key, value);
            }
        }

        System.out.print(out);
    }

    // Задача 6. Первые k точек по заданному порядку
    static class Point {
        int x;
        int y;
        int id;
        long distance;

        Point(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
            this.distance = (long) x * x + (long) y * y;
        }
    }

    static int compare(Point a, Point b) {
        int c = Long.compare(a.distance, b.distance);
        if (c != 0) return c;

        c = Integer.compare(a.x, b.x);
        if (c != 0) return c;

        c = Integer.compare(a.y, b.y);
        if (c != 0) return c;

        return Integer.compare(a.id, b.id);
    }

    static void solve6() throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int k = fs.nextInt();

        int[] xs = new int[n];
        int[] ys = new int[n];
        int[] ids = new int[n];

        for (int i = 0; i < n; i++) {
            xs[i] = fs.nextInt();
            ys[i] = fs.nextInt();
            ids[i] = i + 1;
        }

        Random random = new Random();

        int left = 0;
        int right = n - 1;

        // QuickSelect: отделяем первые k точек
        while (left <= right) {
            int pivotIndex = left + random.nextInt(right - left + 1);
            int px = xs[pivotIndex];
            int py = ys[pivotIndex];
            int pid = ids[pivotIndex];

            long pd = (long) px * px + (long) py * py;

            int lt = left;
            int i = left;
            int gt = right;

            while (i <= gt) {
                long d = (long) xs[i] * xs[i] + (long) ys[i] * ys[i];

                int c = Long.compare(d, pd);
                if (c == 0) c = Integer.compare(xs[i], px);
                if (c == 0) c = Integer.compare(ys[i], py);
                if (c == 0) c = Integer.compare(ids[i], pid);

                if (c < 0) {
                    swapPoints(xs, ys, ids, lt++, i++);
                } else if (c > 0) {
                    swapPoints(xs, ys, ids, i, gt--);
                } else {
                    i++;
                }
            }

            if (k - 1 < lt) {
                right = lt - 1;
            } else if (k - 1 > gt) {
                left = gt + 1;
            } else {
                break;
            }
        }

        Point[] result = new Point[k];

        for (int i = 0; i < k; i++) {
            result[i] = new Point(xs[i], ys[i], ids[i]);
        }

        Arrays.sort(result, Main::compare);

        StringBuilder out = new StringBuilder();

        for (Point p : result) {
            out.append(p.x).append(' ')
                    .append(p.y).append(' ')
                    .append(p.id).append('\n');
        }

        System.out.print(out);
    }

    static void swapPoints(int[] xs, int[] ys, int[] ids, int i, int j) {
        int temp = xs[i];
        xs[i] = xs[j];
        xs[j] = temp;

        temp = ys[i];
        ys[i] = ys[j];
        ys[j] = temp;

        temp = ids[i];
        ids[i] = ids[j];
        ids[j] = temp;
    }
}
