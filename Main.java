
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

    // =====================================================
    // Общий класс узла односвязного списка
    // =====================================================

    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    static Node buildList(int[] a) {
        Node dummy = new Node(0);
        Node tail = dummy;

        for (int x : a) {
            tail.next = new Node(x);
            tail = tail.next;
        }

        return dummy.next;
    }

    static void printList(Node head) {
        while (head != null) {
            System.out.print(head.value);

            if (head.next != null) {
                System.out.print(" ");
            }

            head = head.next;
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 1. Разворот односвязного списка
    // =====================================================

    static void task1() {
        System.out.println("Задача 1:");

        int[] a = {1, 2, 3, 4, 5};

        Node head = buildList(a);

        Node previous = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;

            current.next = previous;

            previous = current;
            current = next;
        }

        head = previous;

        printList(head);
        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 2. DSU: объединение и проверка связности
    // =====================================================

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }

            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);

            if (rootA == rootB) {
                return;
            }

            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }

            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }

        boolean connected(int a, int b) {
            return find(a) == find(b);
        }
    }

    static void task2() {
        System.out.println("Задача 2:");

        int n = 5;

        int[][] operations = {
                {1, 1, 2},
                {1, 2, 3},
                {2, 1, 3},
                {2, 1, 4},
                {1, 4, 5},
                {2, 4, 5},
                {2, 3, 5}
        };

        DSU dsu = new DSU(n);

        for (int[] op : operations) {
            int type = op[0];
            int u = op[1];
            int v = op[2];

            if (type == 1) {
                dsu.union(u, v);
            } else {
                System.out.println(
                        dsu.connected(u, v) ? "YES" : "NO"
                );
            }
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 3. Лексикографически минимальная
    // топологическая сортировка
    // =====================================================

    static void task3() {
        System.out.println("Задача 3:");

        int n = 4;

        int[][] edges = {
                {1, 3},
                {2, 3},
                {3, 4}
        };

        List<Integer>[] graph = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] indegree = new int[n + 1];

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            graph[u].add(v);
            indegree[v]++;
        }

        // Минимальная куча гарантирует
        // лексикографически минимальный порядок.
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                pq.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!pq.isEmpty()) {
            int v = pq.poll();

            order.add(v);

            for (int to : graph[v]) {
                indegree[to]--;

                if (indegree[to] == 0) {
                    pq.add(to);
                }
            }
        }

        if (order.size() != n) {
            System.out.println(-1);
        } else {
            for (int x : order) {
                System.out.print(x + " ");
            }

            System.out.println();
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 4. Разворот каждого полного блока из k узлов
    // =====================================================

    static Node reverseKGroup(Node head, int k) {
        if (head == null || k <= 1) {
            return head;
        }

        Node dummy = new Node(0);
        dummy.next = head;

        Node groupPrevious = dummy;

        while (true) {
            // Находим последний узел очередного блока.
            Node kth = groupPrevious;

            for (int i = 0; i < k && kth != null; i++) {
                kth = kth.next;
            }

            // Неполный блок оставляем без изменений.
            if (kth == null) {
                break;
            }

            Node groupNext = kth.next;

            // Разворачиваем текущий блок.
            Node previous = groupNext;
            Node current = groupPrevious.next;

            while (current != groupNext) {
                Node next = current.next;

                current.next = previous;

                previous = current;
                current = next;
            }

            // Новый первый узел блока — kth.
            Node oldGroupStart = groupPrevious.next;

            groupPrevious.next = kth;

            // Переходим к следующему блоку.
            groupPrevious = oldGroupStart;
        }

        return dummy.next;
    }

    static void task4() {
        System.out.println("Задача 4:");

        int[] a = {1, 2, 3, 4, 5, 6, 7, 8};
        int k = 3;

        Node head = buildList(a);

        head = reverseKGroup(head, k);

        printList(head);
        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 5. DSU с XOR-разницей между группами
    // =====================================================

    static class ParityDSU {
        int[] parent;
        int[] size;

        // parity[x] = цвет[x] XOR цвет[parent[x]].
        int[] parity;

        ParityDSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            parity = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
                parity[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] == x) {
                return x;
            }

            int oldParent = parent[x];

            parent[x] = find(oldParent);

            parity[x] ^= parity[oldParent];

            return parent[x];
        }

        // Требуется:
        // color[u] XOR color[v] = required.
        boolean unite(int u, int v, int required) {
            int rootU = find(u);
            int rootV = find(v);

            int parityU = parity[u];
            int parityV = parity[v];

            // Уже находятся в одной компоненте.
            if (rootU == rootV) {
                return (parityU ^ parityV) == required;
            }

            // Присоединяем меньшую компоненту к большей.
            if (size[rootU] < size[rootV]) {
                int temp = rootU;
                rootU = rootV;
                rootV = temp;
            }

            parent[rootV] = rootU;

            // XOR-разница между корнями.
            parity[rootV] = parityU ^ parityV ^ required;

            size[rootU] += size[rootV];

            return true;
        }
    }

    static void task5() {
        System.out.println("Задача 5:");

        int n = 4;

        int[][] statements = {
                {1, 2, 1},
                {2, 3, 0},
                {1, 3, 1},
                {1, 3, 0},
                {3, 4, 1}
        };

        ParityDSU dsu = new ParityDSU(n);

        boolean consistent = true;

        for (int[] statement : statements) {
            int u = statement[0];
            int v = statement[1];
            int r = statement[2];

            if (consistent) {
                consistent = dsu.unite(u, v, r);
            }

            System.out.println(
                    consistent ? "YES" : "NO"
            );
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 6. Восстановление алфавита
    // =====================================================

    static void task6() {
        System.out.println("Задача 6:");

        int n = 5;
        int k = 4;

        String[] words = {
                "baa",
                "abcd",
                "abca",
                "cab",
                "cad"
        };

        boolean[][] graph = new boolean[k][k];
        int[] indegree = new int[k];

        boolean impossible = false;

        // Сравниваем только соседние слова.
        for (int i = 0; i + 1 < n; i++) {
            String first = words[i];
            String second = words[i + 1];

            int j = 0;

            while (j < first.length()
                    && j < second.length()
                    && first.charAt(j) == second.charAt(j)) {
                j++;
            }

            // Второе слово является началом первого.
            // Такая пара нарушает порядок.
            if (j == second.length() && first.length() > second.length()) {
                impossible = true;
                break;
            }

            // Нашли первую различающуюся букву.
            if (j < first.length() && j < second.length()) {
                int u = first.charAt(j) - 'a';
                int v = second.charAt(j) - 'a';

                if (!graph[u][v]) {
                    graph[u][v] = true;
                    indegree[v]++;
                }
            }
        }

        if (impossible) {
            System.out.println("IMPOSSIBLE");
            return;
        }

        // Минимальная куча обеспечивает
        // лексикографически наименьший порядок букв.
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 0; i < k; i++) {
            if (indegree[i] == 0) {
                pq.add(i);
            }
        }

        StringBuilder answer = new StringBuilder();

        while (!pq.isEmpty()) {
            int v = pq.poll();

            answer.append((char) ('a' + v));

            for (int to = 0; to < k; to++) {
                if (graph[v][to]) {
                    indegree[to]--;

                    if (indegree[to] == 0) {
                        pq.add(to);
                    }
                }
            }
        }

        if (answer.length() != k) {
            System.out.println("IMPOSSIBLE");
        } else {
            System.out.println(answer);
        }
    }
}
