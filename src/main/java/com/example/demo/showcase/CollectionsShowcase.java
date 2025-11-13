package com.example.demo.showcase;

import java.util.*;
import java.util.concurrent.*;

/**
 * Comprehensive demonstration of Java Collections Framework
 * Including List, Set, Map, Queue, and their various implementations
 */
public class CollectionsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== COLLECTIONS SHOWCASE ==========\n");

        arrayListDemo();
        linkedListDemo();
        hashSetDemo();
        treeSetDemo();
        linkedHashSetDemo();
        hashMapDemo();
        treeMapDemo();
        linkedHashMapDemo();
        concurrentHashMapDemo();
        queueDemo();
        dequeDemo();
        traversalMethods();
        mapEntrySetDemo();
        utilityMethods();
    }

    // ========== ArrayList ==========

    private static void arrayListDemo() {
        System.out.println("--- ArrayList ---");
        System.out.println("Fast random access, slow insertion/deletion in middle");

        ArrayList<String> list = new ArrayList<>();

        // Adding elements
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        list.add(1, "Apricot"); // Insert at index
        System.out.println("ArrayList: " + list);

        // Accessing elements
        System.out.println("Element at index 0: " + list.get(0));
        System.out.println("Index of 'Banana': " + list.indexOf("Banana"));

        // Modifying
        list.set(0, "Avocado");
        System.out.println("After set(0, 'Avocado'): " + list);

        // Removing
        list.remove("Banana");
        list.remove(0);
        System.out.println("After removals: " + list);

        // Size and contains
        System.out.println("Size: " + list.size());
        System.out.println("Contains 'Cherry': " + list.contains("Cherry"));

        // Bulk operations
        list.addAll(Arrays.asList("Date", "Elderberry", "Fig"));
        System.out.println("After addAll: " + list);

        System.out.println();
    }

    // ========== LinkedList ==========

    private static void linkedListDemo() {
        System.out.println("--- LinkedList ---");
        System.out.println("Fast insertion/deletion, slow random access, implements Deque");

        LinkedList<Integer> list = new LinkedList<>();

        // Adding elements
        list.add(1);
        list.add(2);
        list.add(3);
        list.addFirst(0);
        list.addLast(4);
        System.out.println("LinkedList: " + list);

        // Access first and last
        System.out.println("First: " + list.getFirst());
        System.out.println("Last: " + list.getLast());

        // Queue operations
        list.offer(5); // Add to end
        list.offerFirst(-1); // Add to beginning
        System.out.println("After offers: " + list);

        // Peek and poll
        System.out.println("Peek: " + list.peek());
        System.out.println("Poll: " + list.poll());
        System.out.println("After poll: " + list);

        // Stack operations
        list.push(100); // Add to beginning
        System.out.println("After push(100): " + list);
        System.out.println("Pop: " + list.pop());
        System.out.println("After pop: " + list);

        System.out.println();
    }

    // ========== HashSet ==========

    private static void hashSetDemo() {
        System.out.println("--- HashSet ---");
        System.out.println("Unordered, no duplicates, O(1) operations");

        HashSet<String> set = new HashSet<>();

        // Adding elements
        set.add("Apple");
        set.add("Banana");
        set.add("Cherry");
        set.add("Apple"); // Duplicate - will be ignored
        System.out.println("HashSet: " + set);

        // Contains
        System.out.println("Contains 'Banana': " + set.contains("Banana"));
        System.out.println("Contains 'Date': " + set.contains("Date"));

        // Size
        System.out.println("Size: " + set.size());

        // Remove
        set.remove("Banana");
        System.out.println("After remove: " + set);

        // Set operations
        HashSet<String> set2 = new HashSet<>(Arrays.asList("Cherry", "Date", "Elderberry"));

        // Union
        HashSet<String> union = new HashSet<>(set);
        union.addAll(set2);
        System.out.println("Union: " + union);

        // Intersection
        HashSet<String> intersection = new HashSet<>(set);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // Difference
        HashSet<String> difference = new HashSet<>(set);
        difference.removeAll(set2);
        System.out.println("Difference: " + difference);

        System.out.println();
    }

    // ========== TreeSet ==========

    private static void treeSetDemo() {
        System.out.println("--- TreeSet ---");
        System.out.println("Sorted (natural order or comparator), no duplicates, O(log n) operations");

        TreeSet<Integer> set = new TreeSet<>();

        // Adding elements (auto-sorted)
        set.add(5);
        set.add(2);
        set.add(8);
        set.add(1);
        set.add(9);
        set.add(3);
        System.out.println("TreeSet: " + set);

        // Navigation methods
        System.out.println("First: " + set.first());
        System.out.println("Last: " + set.last());
        System.out.println("Higher than 5: " + set.higher(5));
        System.out.println("Lower than 5: " + set.lower(5));
        System.out.println("Ceiling of 4: " + set.ceiling(4));
        System.out.println("Floor of 4: " + set.floor(4));

        // Subset operations
        System.out.println("HeadSet (< 5): " + set.headSet(5));
        System.out.println("TailSet (>= 5): " + set.tailSet(5));
        System.out.println("SubSet [2, 8): " + set.subSet(2, 8));

        // Polling
        System.out.println("Poll first: " + set.pollFirst());
        System.out.println("Poll last: " + set.pollLast());
        System.out.println("After polling: " + set);

        // Custom comparator
        TreeSet<String> reverseSet = new TreeSet<>(Comparator.reverseOrder());
        reverseSet.addAll(Arrays.asList("A", "C", "B", "E", "D"));
        System.out.println("Reverse order TreeSet: " + reverseSet);

        System.out.println();
    }

    // ========== LinkedHashSet ==========

    private static void linkedHashSetDemo() {
        System.out.println("--- LinkedHashSet ---");
        System.out.println("Maintains insertion order, no duplicates");

        LinkedHashSet<String> set = new LinkedHashSet<>();

        set.add("Zebra");
        set.add("Apple");
        set.add("Mango");
        set.add("Banana");
        System.out.println("LinkedHashSet (insertion order): " + set);

        System.out.println();
    }

    // ========== HashMap ==========

    private static void hashMapDemo() {
        System.out.println("--- HashMap ---");
        System.out.println("Unordered, key-value pairs, O(1) operations, allows null key");

        HashMap<String, Integer> map = new HashMap<>();

        // Put elements
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);
        map.put("Date", 40);
        System.out.println("HashMap: " + map);

        // Get
        System.out.println("Get 'Banana': " + map.get("Banana"));
        System.out.println("Get 'Grape': " + map.get("Grape"));

        // getOrDefault
        System.out.println("GetOrDefault 'Grape': " + map.getOrDefault("Grape", 0));

        // containsKey, containsValue
        System.out.println("Contains key 'Apple': " + map.containsKey("Apple"));
        System.out.println("Contains value 30: " + map.containsValue(30));

        // Replace
        map.replace("Apple", 15);
        System.out.println("After replace: " + map);

        // putIfAbsent
        map.putIfAbsent("Apple", 100); // Won't replace
        map.putIfAbsent("Elderberry", 50); // Will add
        System.out.println("After putIfAbsent: " + map);

        // compute, computeIfPresent, computeIfAbsent
        map.compute("Banana", (k, v) -> v == null ? 0 : v + 10);
        System.out.println("After compute: " + map);

        map.computeIfPresent("Cherry", (k, v) -> v * 2);
        System.out.println("After computeIfPresent: " + map);

        map.computeIfAbsent("Fig", k -> k.length() * 10);
        System.out.println("After computeIfAbsent: " + map);

        // merge
        map.merge("Apple", 5, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("After merge: " + map);

        // Remove
        map.remove("Date");
        System.out.println("After remove: " + map);

        // KeySet, values, entrySet
        System.out.println("Keys: " + map.keySet());
        System.out.println("Values: " + map.values());
        System.out.println("Entries: " + map.entrySet());

        System.out.println();
    }

    // ========== TreeMap ==========

    private static void treeMapDemo() {
        System.out.println("--- TreeMap ---");
        System.out.println("Sorted by keys (natural order or comparator), O(log n) operations");

        TreeMap<String, Integer> map = new TreeMap<>();

        // Put elements (auto-sorted by key)
        map.put("Zebra", 26);
        map.put("Apple", 1);
        map.put("Mango", 13);
        map.put("Banana", 2);
        map.put("Cherry", 3);
        System.out.println("TreeMap: " + map);

        // Navigation methods
        System.out.println("First key: " + map.firstKey());
        System.out.println("Last key: " + map.lastKey());
        System.out.println("First entry: " + map.firstEntry());
        System.out.println("Last entry: " + map.lastEntry());

        System.out.println("Higher key than 'Mango': " + map.higherKey("Mango"));
        System.out.println("Lower key than 'Mango': " + map.lowerKey("Mango"));
        System.out.println("Ceiling key of 'Date': " + map.ceilingKey("Date"));
        System.out.println("Floor key of 'Date': " + map.floorKey("Date"));

        // SubMap operations
        System.out.println("HeadMap (< 'Mango'): " + map.headMap("Mango"));
        System.out.println("TailMap (>= 'Mango'): " + map.tailMap("Mango"));
        System.out.println("SubMap ['Banana', 'Mango'): " + map.subMap("Banana", "Mango"));

        // Descending map
        System.out.println("Descending map: " + map.descendingMap());

        // Poll operations
        System.out.println("Poll first entry: " + map.pollFirstEntry());
        System.out.println("Poll last entry: " + map.pollLastEntry());
        System.out.println("After polling: " + map);

        System.out.println();
    }

    // ========== LinkedHashMap ==========

    private static void linkedHashMapDemo() {
        System.out.println("--- LinkedHashMap ---");
        System.out.println("Maintains insertion order (or access order)");

        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        map.put("First", 1);
        map.put("Second", 2);
        map.put("Third", 3);
        map.put("Fourth", 4);
        System.out.println("LinkedHashMap (insertion order): " + map);

        // Access order LinkedHashMap (for LRU cache)
        LinkedHashMap<String, Integer> lruMap = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > 3; // Keep only 3 entries
            }
        };

        lruMap.put("A", 1);
        lruMap.put("B", 2);
        lruMap.put("C", 3);
        System.out.println("LRU map: " + lruMap);

        lruMap.get("A"); // Access A
        lruMap.put("D", 4); // This will remove B (least recently used)
        System.out.println("After adding D (B removed): " + lruMap);

        System.out.println();
    }

    // ========== ConcurrentHashMap ==========

    private static void concurrentHashMapDemo() {
        System.out.println("--- ConcurrentHashMap ---");
        System.out.println("Thread-safe without locking entire map, high concurrency");

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Basic operations
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println("ConcurrentHashMap: " + map);

        // putIfAbsent (atomic operation)
        Integer previous = map.putIfAbsent("D", 4);
        System.out.println("putIfAbsent result: " + previous);
        System.out.println("After putIfAbsent: " + map);

        // replace (atomic)
        boolean replaced = map.replace("A", 1, 10);
        System.out.println("Replace successful: " + replaced);
        System.out.println("After replace: " + map);

        // compute operations (atomic)
        map.compute("B", (k, v) -> v == null ? 1 : v * 2);
        System.out.println("After compute: " + map);

        // forEach (parallel processing)
        System.out.println("forEach with parallelism:");
        map.forEach(1, (k, v) -> System.out.println("  " + k + " = " + v));

        // search
        String result = map.search(1, (k, v) -> v > 5 ? k : null);
        System.out.println("Search (value > 5): " + result);

        // reduce
        Integer sum = map.reduce(1, (k, v) -> v, (v1, v2) -> v1 + v2);
        System.out.println("Reduce (sum of values): " + sum);

        System.out.println();
    }

    // ========== Queue (PriorityQueue) ==========

    private static void queueDemo() {
        System.out.println("--- Queue (PriorityQueue) ---");
        System.out.println("Min-heap by default, orders elements by priority");

        PriorityQueue<Integer> queue = new PriorityQueue<>();

        // Add elements
        queue.offer(5);
        queue.offer(2);
        queue.offer(8);
        queue.offer(1);
        queue.offer(9);
        System.out.println("PriorityQueue: " + queue);

        // Peek (doesn't remove)
        System.out.println("Peek: " + queue.peek());

        // Poll (removes)
        System.out.print("Polling all: ");
        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
        System.out.println();

        // Max heap with custom comparator
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(Arrays.asList(5, 2, 8, 1, 9));
        System.out.print("Max heap polling: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();

        // Custom object priority queue
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(t -> t.priority)
        );
        taskQueue.offer(new Task("Low priority", 3));
        taskQueue.offer(new Task("High priority", 1));
        taskQueue.offer(new Task("Medium priority", 2));

        System.out.println("Task queue:");
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("  " + task.name + " (priority: " + task.priority + ")");
        }

        System.out.println();
    }

    static class Task {
        String name;
        int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }

    // ========== Deque (ArrayDeque) ==========

    private static void dequeDemo() {
        System.out.println("--- Deque (ArrayDeque) ---");
        System.out.println("Double-ended queue, can be used as stack or queue");

        ArrayDeque<String> deque = new ArrayDeque<>();

        // Add to both ends
        deque.addFirst("A");
        deque.addLast("B");
        deque.offerFirst("Z");
        deque.offerLast("C");
        System.out.println("Deque: " + deque);

        // Peek both ends
        System.out.println("Peek first: " + deque.peekFirst());
        System.out.println("Peek last: " + deque.peekLast());

        // Remove from both ends
        System.out.println("Remove first: " + deque.removeFirst());
        System.out.println("Remove last: " + deque.removeLast());
        System.out.println("After removals: " + deque);

        // Use as stack
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack: " + stack);
        System.out.println("Pop: " + stack.pop());
        System.out.println("After pop: " + stack);

        System.out.println();
    }

    // ========== Traversal Methods ==========

    private static void traversalMethods() {
        System.out.println("--- Traversal Methods ---");

        List<String> list = Arrays.asList("Apple", "Banana", "Cherry", "Date");

        // For loop
        System.out.print("For loop: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        // For-each loop
        System.out.print("For-each: ");
        for (String item : list) {
            System.out.print(item + " ");
        }
        System.out.println();

        // Iterator
        System.out.print("Iterator: ");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        // ListIterator (bidirectional)
        System.out.print("ListIterator (reverse): ");
        ListIterator<String> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            System.out.print(listIterator.previous() + " ");
        }
        System.out.println();

        // forEach with lambda
        System.out.print("forEach lambda: ");
        list.forEach(item -> System.out.print(item + " "));
        System.out.println();

        // Stream
        System.out.print("Stream: ");
        list.stream().forEach(item -> System.out.print(item + " "));
        System.out.println();

        System.out.println();
    }

    // ========== Map EntrySet Traversal ==========

    private static void mapEntrySetDemo() {
        System.out.println("--- Map EntrySet Traversal ---");

        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);
        map.put("Date", 40);

        // Using entrySet with for-each
        System.out.println("EntrySet for-each:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // Using keySet
        System.out.println("KeySet:");
        for (String key : map.keySet()) {
            System.out.println("  " + key + " = " + map.get(key));
        }

        // Using values
        System.out.print("Values: ");
        for (Integer value : map.values()) {
            System.out.print(value + " ");
        }
        System.out.println();

        // Using forEach with lambda
        System.out.println("forEach lambda:");
        map.forEach((key, value) -> System.out.println("  " + key + " = " + value));

        // Using entrySet with Iterator
        System.out.println("Iterator:");
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // Using stream
        System.out.println("Stream:");
        map.entrySet().stream()
            .forEach(entry -> System.out.println("  " + entry.getKey() + " = " + entry.getValue()));

        System.out.println();
    }

    // ========== Utility Methods ==========

    private static void utilityMethods() {
        System.out.println("--- Collections Utility Methods ---");

        List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

        // Sort
        Collections.sort(numbers);
        System.out.println("Sorted: " + numbers);

        // Reverse
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);

        // Shuffle
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);

        // Rotate
        Collections.rotate(numbers, 2);
        System.out.println("Rotated by 2: " + numbers);

        // Binary search (requires sorted list)
        Collections.sort(numbers);
        int index = Collections.binarySearch(numbers, 5);
        System.out.println("Binary search for 5: index " + index);

        // Min and Max
        System.out.println("Min: " + Collections.min(numbers));
        System.out.println("Max: " + Collections.max(numbers));

        // Frequency
        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "apple");
        System.out.println("Frequency of 'apple': " + Collections.frequency(words, "apple"));

        // Fill
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        Collections.fill(list, "X");
        System.out.println("After fill: " + list);

        // Copy
        List<String> dest = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
        List<String> src = Arrays.asList("A", "B");
        Collections.copy(dest, src);
        System.out.println("After copy: " + dest);

        // Unmodifiable collections
        List<String> unmodifiable = Collections.unmodifiableList(Arrays.asList("X", "Y", "Z"));
        System.out.println("Unmodifiable list: " + unmodifiable);

        // Synchronized collections
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        System.out.println("Created synchronized list");

        System.out.println();
    }
}
