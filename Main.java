
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

    // ==================================================
    // ЗАДАЧА 1. Правильные скобочные последовательности
    // ==================================================

    static void task1() {
        System.out.println("Задача 1:");

        int n = 3;

        char[] result = new char[2 * n];

        generateBrackets(result, 0, 0, 0, n);

        System.out.println();
    }

    static void generateBrackets(
            char[] result,
            int pos,
            int open,
            int close,
            int n
    ) {
        if (pos == 2 * n) {
            System.out.println(new String(result));
            return;
        }

        // '(' лексикографически меньше ')',
        // поэтому сначала пробуем добавить '('.
        if (open < n) {
            result[pos] = '(';

            generateBrackets(
                    result, pos + 1,
                    open + 1, close, n
            );
        }

        // Закрывающая скобка допустима,
        // только если есть незакрытая открывающая.
        if (close < open) {
            result[pos] = ')';

            generateBrackets(
                    result, pos + 1,
                    open, close + 1, n
            );
        }
    }


    // ==================================================
    // ЗАДАЧА 2. Trie: ADD, REMOVE, COUNT
    // ==================================================

    static class TrieNode {
        TrieNode[] next = new TrieNode[26];
        int count;
    }

    static class PrefixTrie {
        TrieNode root = new TrieNode();

        void add(String word) {
            TrieNode node = root;
            node.count++;

            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';

                if (node.next[c] == null) {
                    node.next[c] = new TrieNode();
                }

                node = node.next[c];
                node.count++;
            }
        }

        void remove(String word) {
            TrieNode node = root;
            node.count--;

            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';

                node = node.next[c];
                node.count--;
            }
        }

        int count(String prefix) {
            TrieNode node = root;

            for (int i = 0; i < prefix.length(); i++) {
                int c = prefix.charAt(i) - 'a';

                if (node.next[c] == null) {
                    return 0;
                }

                node = node.next[c];
            }

            return node.count;
        }
    }

    static void task2() {
        System.out.println("Задача 2:");

        PrefixTrie trie = new PrefixTrie();

        trie.add("apple");
        trie.add("app");
        trie.add("application");
        trie.add("banana");

        System.out.println(trie.count("app"));
        trie.remove("app");

        System.out.println(trie.count("app"));
        System.out.println(trie.count("ban"));

        System.out.println();
    }


    // ==================================================
    // ЗАДАЧА 3. Два числа, встречающиеся один раз
    // ==================================================

    static void task3() {
        System.out.println("Задача 3:");

        int[] a = {1, 2, 1, 3, 4, 2};

        int xor = 0;

        for (int x : a) {
            xor ^= x;
        }

        // xor = first ^ second.
        // Находим младший установленный бит.
        int bit = xor & -xor;

        int first = 0;
        int second = 0;

        for (int x : a) {
            if ((x & bit) == 0) {
                first ^= x;
            } else {
                second ^= x;
            }
        }

        System.out.println(
                Math.min(first, second) + " " +
                        Math.max(first, second)
        );

        System.out.println();
    }


    // ==================================================
    // ЗАДАЧА 4. Количество расстановок ферзей
    // ==================================================

    static void task4() {
        System.out.println("Задача 4:");

        int n = 4;

        // forbidden[row] содержит запрещённые столбцы.
        // Нумерация в массиве начинается с нуля.
        long[] forbidden = new long[n];

        // Запрещённая клетка (1, 2).
        forbidden[0] |= (1L << 1);

        long fullMask = (1L << n) - 1;

        long answer = countQueens(
                0,
                n,
                fullMask,
                forbidden,
                0L,
                0L,
                0L
        );

        System.out.println(answer);
        System.out.println();
    }

    static long countQueens(
            int row,
            int n,
            long fullMask,
            long[] forbidden,
            long columns,
            long diag1,
            long diag2
    ) {
        if (row == n) {
            return 1;
        }

        long available = fullMask
                & ~(columns | diag1 | diag2)
                & ~forbidden[row];

        long answer = 0;

        while (available != 0) {
            long bit = available & -available;

            available -= bit;

            answer += countQueens(
                    row + 1,
                    n,
                    fullMask,
                    forbidden,
                    columns | bit,
                    (diag1 | bit) << 1,
                    (diag2 | bit) >> 1
            );
        }

        return answer;
    }


    // ==================================================
    // ЗАДАЧА 5. Trie: ADD, REMOVE, KTH
    // ==================================================

    static class KthNode {
        KthNode[] next = new KthNode[26];

        // Количество слов в поддереве.
        int count;

        // Слово заканчивается в этой вершине.
        boolean terminal;
    }

    static class KthTrie {
        KthNode root = new KthNode();

        void add(String word) {
            KthNode node = root;
            node.count++;

            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';

                if (node.next[c] == null) {
                    node.next[c] = new KthNode();
                }

                node = node.next[c];
                node.count++;
            }

            node.terminal = true;
        }

        void remove(String word) {
            KthNode node = root;
            node.count--;

            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';

                node = node.next[c];
                node.count--;
            }

            node.terminal = false;
        }

        String kth(String prefix, int k) {
            KthNode node = root;

            // Находим вершину префикса.
            for (int i = 0; i < prefix.length(); i++) {
                int c = prefix.charAt(i) - 'a';

                if (node.next[c] == null) {
                    return "NONE";
                }

                node = node.next[c];
            }

            if (k <= 0 || k > node.count) {
                return "NONE";
            }

            StringBuilder answer = new StringBuilder(prefix);

            while (true) {

                // Сначала проверяем само слово.
                // Короткое слово идёт раньше продолжений.
                if (node.terminal) {
                    if (k == 1) {
                        return answer.toString();
                    }

                    k--;
                }

                boolean found = false;

                // Перебираем буквы в алфавитном порядке.
                for (int c = 0; c < 26; c++) {
                    KthNode child = node.next[c];

                    if (child == null || child.count == 0) {
                        continue;
                    }

                    if (k > child.count) {
                        k -= child.count;
                    } else {
                        answer.append((char) ('a' + c));
                        node = child;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return "NONE";
                }
            }
        }
    }

    static void task5() {
        System.out.println("Задача 5:");

        KthTrie trie = new KthTrie();

        trie.add("apple");
        trie.add("app");
        trie.add("application");
        trie.add("apply");
        trie.add("banana");

        System.out.println(trie.kth("app", 1));
        System.out.println(trie.kth("app", 2));
        System.out.println(trie.kth("app", 3));
        System.out.println(trie.kth("app", 4));
        System.out.println(trie.kth("app", 5));

        trie.remove("apple");

        System.out.println(trie.kth("app", 2));

        System.out.println();
    }


    // ==================================================
    // ЗАДАЧА 6. Максимальный XOR подмассива
    // ==================================================

    static class XorNode {
        XorNode[] next = new XorNode[2];
    }

    static class BinaryTrie {
        XorNode root = new XorNode();

        void add(int value) {
            XorNode node = root;

            for (int bit = 29; bit >= 0; bit--) {
                int current = (value >>> bit) & 1;

                if (node.next[current] == null) {
                    node.next[current] = new XorNode();
                }

                node = node.next[current];
            }
        }

        int maxXor(int value) {
            XorNode node = root;
            int result = 0;

            for (int bit = 29; bit >= 0; bit--) {
                int current = (value >>> bit) & 1;
                int opposite = current ^ 1;

                // Предпочитаем противоположный бит.
                if (node.next[opposite] != null) {
                    result |= (1 << bit);
                    node = node.next[opposite];
                } else {
                    node = node.next[current];
                }
            }

            return result;
        }
    }

    static void task6() {
        System.out.println("Задача 6:");

        int[] a = {1, 2, 3, 4};

        BinaryTrie trie = new BinaryTrie();

        // Начальный префиксный XOR.
        trie.add(0);

        int prefixXor = 0;
        int answer = 0;

        for (int x : a) {
            prefixXor ^= x;

            answer = Math.max(
                    answer,
                    trie.maxXor(prefixXor)
            );

            trie.add(prefixXor);
        }

        System.out.println(answer);
    }
}
