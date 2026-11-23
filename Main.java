
import java.util.*;

public class Main {

    static final long INF = Long.MAX_VALUE / 4;
    static final int MOD = 1_000_000_007;

    static class Edge {
        int from;
        int to;
        long weight;

        Edge(int from, int to, long weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    static class State {
        int vertex;
        long distance;

        State(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    static class MultiState {
        int vertex;
        int source;
        long distance;

        MultiState(int vertex, int source, long distance) {
            this.vertex = vertex;
            this.source = source;
            this.distance = distance;
        }
    }

    static void addDirected(
            List<Edge>[] graph,
            int u,
            int v,
            long w
    ) {
        graph[u].add(new Edge(u, v, w));
    }

    static void addUndirected(
            List<Edge>[] graph,
            int u,
            int v,
            long w
    ) {
        graph[u].add(new Edge(u, v, w));
        graph[v].add(new Edge(v, u, w));
    }

    static List<Edge>[] createGraph(int n) {
        List<Edge>[] graph = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        return graph;
    }


    // =====================================================
    // ЗАДАЧА 1. Кратчайшие пути из одной вершины.
    // Алгоритм Дейкстры.
    // =====================================================

    static void task1() {
        System.out.println("Задача 1:");

        int n = 5;
        int s = 1;

        List<Edge>[] graph = createGraph(n);

        addDirected(graph, 1, 2, 2);
        addDirected(graph, 1, 3, 5);
        addDirected(graph, 2, 3, 1);
        addDirected(graph, 2, 4, 2);
        addDirected(graph, 3, 4, 1);
        addDirected(graph, 4, 5, 3);

        long[] dist = dijkstra(graph, n, s);

        for (int v = 1; v <= n; v++) {
            if (dist[v] == INF) {
                System.out.print("INF ");
            } else {
                System.out.print(dist[v] + " ");
            }
        }

        System.out.println("\n");
    }

    static long[] dijkstra(
            List<Edge>[] graph,
            int n,
            int start
    ) {
        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);

        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingLong(x -> x.distance)
        );

        dist[start] = 0;
        pq.add(new State(start, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            int v = current.vertex;
            long d = current.distance;

            if (d != dist[v]) {
                continue;
            }

            for (Edge edge : graph[v]) {
                int to = edge.to;
                long newDist = d + edge.weight;

                if (newDist < dist[to]) {
                    dist[to] = newDist;
                    pq.add(new State(to, newDist));
                }
            }
        }

        return dist;
    }


    // =====================================================
    // ЗАДАЧА 2. Кратчайшие пути с отрицательными весами.
    // Алгоритм Беллмана — Форда.
    // =====================================================

    static void task2() {
        System.out.println("Задача 2:");

        int n = 5;
        int s = 1;

        Edge[] edges = {
                new Edge(1, 2, 4),
                new Edge(1, 3, 5),
                new Edge(2, 3, -2),
                new Edge(3, 4, 3),
                new Edge(2, 4, 4)
        };

        long[] dist = bellmanFord(n, edges, s);

        for (int v = 1; v <= n; v++) {
            if (dist[v] == INF) {
                System.out.print("INF ");
            } else {
                System.out.print(dist[v] + " ");
            }
        }

        System.out.println("\n");
    }

    static long[] bellmanFord(
            int n,
            Edge[] edges,
            int start
    ) {
        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);

        dist[start] = 0;

        for (int i = 1; i < n; i++) {
            boolean changed = false;

            for (Edge edge : edges) {
                if (dist[edge.from] == INF) {
                    continue;
                }

                long candidate =
                        dist[edge.from] + edge.weight;

                if (candidate < dist[edge.to]) {
                    dist[edge.to] = candidate;
                    changed = true;
                }
            }

            if (!changed) {
                break;
            }
        }

        return dist;
    }


    // =====================================================
    // ЗАДАЧА 3. Ближайший пункт помощи.
    // Многократная Дейкстра.
    // =====================================================

    static void task3() {
        System.out.println("Задача 3:");

        int n = 5;

        int[] sources = {2, 5};

        List<Edge>[] graph = createGraph(n);

        addUndirected(graph, 1, 2, 3);
        addUndirected(graph, 2, 3, 4);
        addUndirected(graph, 1, 4, 2);
        addUndirected(graph, 4, 5, 6);
        addUndirected(graph, 3, 5, 1);

        long[] dist = new long[n + 1];
        int[] nearest = new int[n + 1];

        Arrays.fill(dist, INF);
        Arrays.fill(nearest, -1);

        PriorityQueue<MultiState> pq = new PriorityQueue<>(
                (a, b) -> {
                    int cmp = Long.compare(a.distance, b.distance);

                    if (cmp != 0) {
                        return cmp;
                    }

                    cmp = Integer.compare(a.source, b.source);

                    if (cmp != 0) {
                        return cmp;
                    }

                    return Integer.compare(a.vertex, b.vertex);
                }
        );

        for (int source : sources) {
            dist[source] = 0;
            nearest[source] = source;

            pq.add(new MultiState(source, source, 0));
        }

        while (!pq.isEmpty()) {
            MultiState current = pq.poll();

            int v = current.vertex;
            int source = current.source;
            long d = current.distance;

            if (d != dist[v] || source != nearest[v]) {
                continue;
            }

            for (Edge edge : graph[v]) {
                int to = edge.to;
                long newDist = d + edge.weight;

                if (newDist < dist[to]
                        || (newDist == dist[to]
                        && source < nearest[to])) {

                    dist[to] = newDist;
                    nearest[to] = source;

                    pq.add(new MultiState(
                            to, source, newDist
                    ));
                }
            }
        }

        for (int v = 1; v <= n; v++) {
            if (nearest[v] == -1) {
                System.out.println("-1 -1");
            } else {
                System.out.println(
                        nearest[v] + " " + dist[v]
                );
            }
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 4. Кратчайший путь с одним купоном.
    // Дейкстра по двум состояниям.
    // =====================================================

    static void task4() {
        System.out.println("Задача 4:");

        int n = 4;
        int s = 1;
        int t = 4;

        List<Edge>[] graph = createGraph(n);

        addDirected(graph, 1, 2, 8);
        addDirected(graph, 1, 3, 5);
        addDirected(graph, 2, 4, 4);
        addDirected(graph, 3, 4, 10);
        addDirected(graph, 1, 4, 20);

        long answer = dijkstraWithCoupon(graph, n, s, t);

        System.out.println(answer);
        System.out.println();
    }

    static long dijkstraWithCoupon(
            List<Edge>[] graph,
            int n,
            int start,
            int target
    ) {
        // dist[v][0] — купон ещё не использован.
        // dist[v][1] — купон уже использован.
        long[][] dist = new long[n + 1][2];

        for (int i = 1; i <= n; i++) {
            Arrays.fill(dist[i], INF);
        }

        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingLong(x -> x.distance)
        );

        // Кодируем состояние:
        // vertex * 2 + usedCoupon.
        dist[start][0] = 0;

        pq.add(new State(start * 2, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            int v = current.vertex / 2;
            int used = current.vertex % 2;
            long d = current.distance;

            if (d != dist[v][used]) {
                continue;
            }

            for (Edge edge : graph[v]) {
                int to = edge.to;
                long w = edge.weight;

                // Вариант 1: едем без купона.
                long normal = d + w;

                if (normal < dist[to][used]) {
                    dist[to][used] = normal;

                    pq.add(new State(
                            to * 2 + used,
                            normal
                    ));
                }

                // Вариант 2: используем купон,
                // если он ещё не был использован.
                if (used == 0) {
                    long discounted = d + w / 2;

                    if (discounted < dist[to][1]) {
                        dist[to][1] = discounted;

                        pq.add(new State(
                                to * 2 + 1,
                                discounted
                        ));
                    }
                }
            }
        }

        long answer = Math.min(
                dist[target][0],
                dist[target][1]
        );

        return answer == INF ? -1 : answer;
    }


    // =====================================================
    // ЗАДАЧА 5. Вершины, достижимые из отрицательных циклов.
    // Беллман — Форд + BFS.
    // =====================================================

    static void task5() {
        System.out.println("Задача 5:");

        int n = 5;
        int s = 1;

        Edge[] edges = {
                new Edge(1, 2, 1),
                new Edge(2, 3, 1),
                new Edge(3, 2, -3),
                new Edge(1, 4, 5),
                new Edge(4, 5, 1)
        };

        List<Edge>[] graph = createGraph(n);

        for (Edge edge : edges) {
            graph[edge.from].add(edge);
        }

        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);

        dist[s] = 0;

        // Обычные релаксации Беллмана — Форда.
        for (int i = 1; i < n; i++) {
            boolean changed = false;

            for (Edge edge : edges) {
                if (dist[edge.from] == INF) {
                    continue;
                }

                long candidate =
                        dist[edge.from] + edge.weight;

                if (candidate < dist[edge.to]) {
                    dist[edge.to] = candidate;
                    changed = true;
                }
            }

            if (!changed) {
                break;
            }
        }

        // Ищем вершины, которые можно улучшить
        // ещё на следующей итерации.
        boolean[] negative = new boolean[n + 1];

        Queue<Integer> queue = new ArrayDeque<>();

        for (Edge edge : edges) {
            if (dist[edge.from] == INF) {
                continue;
            }

            if (dist[edge.from] + edge.weight < dist[edge.to]) {
                if (!negative[edge.to]) {
                    negative[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        // Все вершины, достижимые из таких вершин,
        // также имеют неограниченно уменьшаемую стоимость.
        while (!queue.isEmpty()) {
            int v = queue.poll();

            for (Edge edge : graph[v]) {
                int to = edge.to;

                if (!negative[to]) {
                    negative[to] = true;
                    queue.add(to);
                }
            }
        }

        for (int v = 1; v <= n; v++) {
            if (dist[v] == INF) {
                System.out.println("*");
            } else if (negative[v]) {
                System.out.println("-");
            } else {
                System.out.println(dist[v]);
            }
        }

        System.out.println();
    }


    // =====================================================
    // ЗАДАЧА 6. Минимальная стоимость маршрута
    // не более чем из K рёбер и количество таких маршрутов.
    // =====================================================

    static void task6() {
        System.out.println("Задача 6:");

        int n = 3;
        int s = 1;
        int t = 3;
        int k = 3;

        Edge[] edges = {
                new Edge(1, 2, 1),
                new Edge(2, 3, 2),
                new Edge(1, 3, 5),
                new Edge(1, 2, 1)
        };

        solveLimitedEdges(n, edges, s, t, k);
    }

    static void solveLimitedEdges(
            int n,
            Edge[] edges,
            int start,
            int target,
            int k
    ) {
        // dp[v] — минимальная стоимость маршрута
        // ровно из текущего количества рёбер.
        long[] dp = new long[n + 1];

        // ways[v] — количество маршрутов
        // с этой минимальной стоимостью.
        int[] ways = new int[n + 1];

        Arrays.fill(dp, INF);

        dp[start] = 0;
        ways[start] = 1;

        // Лучший ответ среди маршрутов
        // с количеством рёбер от 0 до K.
        long best = INF;
        int bestWays = 0;

        for (int step = 0; step <= k; step++) {

            // Учитываем маршруты ровно из step рёбер.
            if (dp[target] < best) {
                best = dp[target];
                bestWays = ways[target];
            } else if (dp[target] == best && best != INF) {
                bestWays = (int) (
                        (bestWays + (long) ways[target]) % MOD
                );
            }

            if (step == k) {
                break;
            }

            long[] nextDp = new long[n + 1];
            int[] nextWays = new int[n + 1];

            Arrays.fill(nextDp, INF);

            // Переходы только из предыдущего слоя.
            for (Edge edge : edges) {
                int u = edge.from;
                int v = edge.to;

                if (dp[u] == INF) {
                    continue;
                }

                long candidate = dp[u] + edge.weight;

                if (candidate < nextDp[v]) {
                    nextDp[v] = candidate;
                    nextWays[v] = ways[u];
                } else if (candidate == nextDp[v]) {
                    nextWays[v] = (int) (
                            (nextWays[v] + (long) ways[u]) % MOD
                    );
                }
            }

            dp = nextDp;
            ways = nextWays;
        }

        if (best == INF) {
            System.out.println("IMPOSSIBLE");
        } else {
            System.out.println(best + " " + bestWays);
        }
    }
}
