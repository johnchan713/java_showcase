package com.example.demo.showcase;

import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates additional collections including legacy, concurrent, and synchronized collections
 */
public class AdvancedCollectionsShowcase {

    public static void demonstrate() {
        System.out.println("\n========== ADVANCED COLLECTIONS SHOWCASE ==========\n");

        vectorDemo();
        hashtableDemo();
        copyOnWriteArrayListDemo();
        copyOnWriteArraySetDemo();
        concurrentLinkedQueueDemo();
        concurrentLinkedDequeDemo();
        synchronizedCollectionsDemo();
        abstractCollectionsDemo();
        collectionsHelperMethods();
        pollOperations();
    }

    // ========== Vector (Legacy) ==========

    private static void vectorDemo() {
        System.out.println("--- Vector (Legacy, synchronized) ---");
        System.out.println("Thread-safe but slower than ArrayList, legacy class");

        Vector<String> vector = new Vector<>();

        // Add elements
        vector.add("Apple");
        vector.add("Banana");
        vector.add("Cherry");
        vector.addElement("Date"); // Legacy method
        System.out.println("Vector: " + vector);

        // Access elements
        System.out.println("Element at 0: " + vector.get(0));
        System.out.println("First element: " + vector.firstElement());
        System.out.println("Last element: " + vector.lastElement());

        // Capacity and size
        System.out.println("Size: " + vector.size());
        System.out.println("Capacity: " + vector.capacity());

        // Search
        int index = vector.indexOf("Banana");
        System.out.println("Index of 'Banana': " + index);

        // Remove
        vector.remove("Banana");
        vector.removeElementAt(0);
        System.out.println("After removals: " + vector);

        // Iteration
        System.out.print("Iteration: ");
        Enumeration<String> enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            System.out.print(enumeration.nextElement() + " ");
        }
        System.out.println();

        // Thread-safe by default
        System.out.println("Note: All Vector methods are synchronized");

        System.out.println();
    }

    // ========== Hashtable (Legacy) ==========

    private static void hashtableDemo() {
        System.out.println("--- Hashtable (Legacy, synchronized) ---");
        System.out.println("Thread-safe but slower than HashMap, does not allow null keys/values");

        Hashtable<String, Integer> hashtable = new Hashtable<>();

        // Put elements
        hashtable.put("Apple", 10);
        hashtable.put("Banana", 20);
        hashtable.put("Cherry", 30);
        System.out.println("Hashtable: " + hashtable);

        // Get
        System.out.println("Get 'Apple': " + hashtable.get("Apple"));

        // Contains
        System.out.println("Contains key 'Banana': " + hashtable.containsKey("Banana"));
        System.out.println("Contains value 20: " + hashtable.containsValue(20));

        // Enumeration (legacy way to iterate)
        System.out.println("Keys (Enumeration):");
        Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.println("  " + key + " = " + hashtable.get(key));
        }

        // Values
        Enumeration<Integer> values = hashtable.elements();
        System.out.print("Values: ");
        while (values.hasMoreElements()) {
            System.out.print(values.nextElement() + " ");
        }
        System.out.println();

        // Modern iteration
        hashtable.forEach((k, v) -> System.out.println("  " + k + ": " + v));

        // Null handling
        try {
            hashtable.put(null, 100); // Will throw NullPointerException
        } catch (NullPointerException e) {
            System.out.println("Cannot put null key: NullPointerException");
        }

        System.out.println("Note: All Hashtable methods are synchronized");

        System.out.println();
    }

    // ========== CopyOnWriteArrayList ==========

    private static void copyOnWriteArrayListDemo() {
        System.out.println("--- CopyOnWriteArrayList ---");
        System.out.println("Thread-safe, creates copy on modification, ideal for read-heavy scenarios");

        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();

        // Add elements
        cowList.add("Apple");
        cowList.add("Banana");
        cowList.add("Cherry");
        System.out.println("CopyOnWriteArrayList: " + cowList);

        // Safe iteration while modifying
        System.out.println("Safe iteration during modification:");
        for (String item : cowList) {
            System.out.println("  Reading: " + item);
            if (item.equals("Banana")) {
                cowList.add("Date"); // Safe to modify during iteration
            }
        }
        System.out.println("After modification: " + cowList);

        // addIfAbsent
        boolean added = cowList.addIfAbsent("Apple");
        System.out.println("addIfAbsent 'Apple': " + added);

        added = cowList.addIfAbsent("Elderberry");
        System.out.println("addIfAbsent 'Elderberry': " + added);
        System.out.println("Final list: " + cowList);

        // Thread-safe operations
        System.out.println("No ConcurrentModificationException during iteration");

        System.out.println();
    }

    // ========== CopyOnWriteArraySet ==========

    private static void copyOnWriteArraySetDemo() {
        System.out.println("--- CopyOnWriteArraySet ---");
        System.out.println("Thread-safe set using CopyOnWriteArrayList internally");

        CopyOnWriteArraySet<Integer> cowSet = new CopyOnWriteArraySet<>();

        // Add elements (duplicates ignored)
        cowSet.add(1);
        cowSet.add(2);
        cowSet.add(3);
        cowSet.add(2); // Duplicate, ignored
        System.out.println("CopyOnWriteArraySet: " + cowSet);

        // Safe iteration
        for (Integer num : cowSet) {
            System.out.println("  Value: " + num);
            if (num == 2) {
                cowSet.add(4); // Safe to modify
            }
        }
        System.out.println("After modification: " + cowSet);

        // Operations
        System.out.println("Contains 3: " + cowSet.contains(3));
        cowSet.remove(1);
        System.out.println("After remove: " + cowSet);

        System.out.println();
    }

    // ========== ConcurrentLinkedQueue ==========

    private static void concurrentLinkedQueueDemo() {
        System.out.println("--- ConcurrentLinkedQueue ---");
        System.out.println("Thread-safe, unbounded, non-blocking queue");

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        // Offer (add) elements
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println("Queue: " + queue);

        // Peek (doesn't remove)
        System.out.println("Peek: " + queue.peek());

        // Poll (removes and returns)
        System.out.println("Poll: " + queue.poll());
        System.out.println("After poll: " + queue);

        // Size
        System.out.println("Size: " + queue.size());

        // isEmpty
        System.out.println("Is empty: " + queue.isEmpty());

        // Concurrent operations demo
        System.out.println("Thread-safe operations:");
        queue.offer("Fourth");
        queue.offer("Fifth");

        // Multiple threads can safely operate
        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext()) {
            System.out.println("  " + iterator.next());
        }

        // Clear
        queue.clear();
        System.out.println("After clear, size: " + queue.size());

        System.out.println();
    }

    // ========== ConcurrentLinkedDeque ==========

    private static void concurrentLinkedDequeDemo() {
        System.out.println("--- ConcurrentLinkedDeque ---");
        System.out.println("Thread-safe, unbounded, double-ended queue");

        ConcurrentLinkedDeque<Integer> deque = new ConcurrentLinkedDeque<>();

        // Add to both ends
        deque.offerFirst(1);
        deque.offerLast(2);
        deque.offerFirst(0);
        deque.offerLast(3);
        System.out.println("Deque: " + deque);

        // Peek both ends
        System.out.println("Peek first: " + deque.peekFirst());
        System.out.println("Peek last: " + deque.peekLast());

        // Poll both ends
        System.out.println("Poll first: " + deque.pollFirst());
        System.out.println("Poll last: " + deque.pollLast());
        System.out.println("After polling: " + deque);

        // Use as stack
        deque.push(10);
        deque.push(20);
        System.out.println("After push operations: " + deque);
        System.out.println("Pop: " + deque.pop());
        System.out.println("After pop: " + deque);

        // Iteration
        System.out.println("Forward iteration:");
        for (Integer num : deque) {
            System.out.println("  " + num);
        }

        System.out.println("Backward iteration:");
        Iterator<Integer> descendingIterator = deque.descendingIterator();
        while (descendingIterator.hasNext()) {
            System.out.println("  " + descendingIterator.next());
        }

        System.out.println();
    }

    // ========== Synchronized Collections ==========

    private static void synchronizedCollectionsDemo() {
        System.out.println("--- Synchronized Collections ---");
        System.out.println("Thread-safe wrappers using synchronized keyword");

        // Synchronized List
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        List<String> syncList = Collections.synchronizedList(list);
        System.out.println("Synchronized List: " + syncList);

        // Must synchronize on collection for iteration
        System.out.println("Synchronized iteration:");
        synchronized (syncList) {
            for (String item : syncList) {
                System.out.println("  " + item);
            }
        }

        // Synchronized Set
        Set<Integer> set = new HashSet<>();
        Set<Integer> syncSet = Collections.synchronizedSet(set);
        syncSet.add(1);
        syncSet.add(2);
        syncSet.add(3);
        System.out.println("Synchronized Set: " + syncSet);

        // Synchronized Map
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> syncMap = Collections.synchronizedMap(map);
        syncMap.put("One", 1);
        syncMap.put("Two", 2);
        syncMap.put("Three", 3);
        System.out.println("Synchronized Map: " + syncMap);

        // Synchronized iteration on map
        System.out.println("Synchronized map iteration:");
        synchronized (syncMap) {
            for (Map.Entry<String, Integer> entry : syncMap.entrySet()) {
                System.out.println("  " + entry.getKey() + " = " + entry.getValue());
            }
        }

        // Other synchronized collections
        Collection<String> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
        SortedSet<Integer> syncSortedSet = Collections.synchronizedSortedSet(new TreeSet<>());
        SortedMap<String, Integer> syncSortedMap = Collections.synchronizedSortedMap(new TreeMap<>());

        System.out.println("Note: synchronized keyword ensures thread-safety");
        System.out.println("Example synchronized block:");
        System.out.println("  synchronized(syncList) { /* operations */ }");

        System.out.println();
    }

    // ========== Abstract Collections ==========

    private static void abstractCollectionsDemo() {
        System.out.println("--- Abstract Collections ---");
        System.out.println("Base classes for implementing custom collections");

        // Custom AbstractSet example
        class CustomSet<E> extends AbstractSet<E> {
            private final List<E> elements = new ArrayList<>();

            @Override
            public Iterator<E> iterator() {
                return elements.iterator();
            }

            @Override
            public int size() {
                return elements.size();
            }

            @Override
            public boolean add(E e) {
                if (!elements.contains(e)) {
                    return elements.add(e);
                }
                return false;
            }
        }

        CustomSet<String> customSet = new CustomSet<>();
        customSet.add("Apple");
        customSet.add("Banana");
        customSet.add("Apple"); // Duplicate ignored
        System.out.println("Custom AbstractSet: " + customSet);
        System.out.println("Size: " + customSet.size());

        // Custom AbstractMap example
        class CustomMap<K, V> extends AbstractMap<K, V> {
            private final List<Entry<K, V>> entries = new ArrayList<>();

            @Override
            public Set<Entry<K, V>> entrySet() {
                return new AbstractSet<Entry<K, V>>() {
                    @Override
                    public Iterator<Entry<K, V>> iterator() {
                        return entries.iterator();
                    }

                    @Override
                    public int size() {
                        return entries.size();
                    }
                };
            }

            @Override
            public V put(K key, V value) {
                entries.add(new AbstractMap.SimpleEntry<>(key, value));
                return value;
            }
        }

        CustomMap<String, Integer> customMap = new CustomMap<>();
        customMap.put("One", 1);
        customMap.put("Two", 2);
        System.out.println("Custom AbstractMap: " + customMap);

        // AbstractList example
        class CustomList<E> extends AbstractList<E> {
            private final List<E> elements = new ArrayList<>();

            @Override
            public E get(int index) {
                return elements.get(index);
            }

            @Override
            public int size() {
                return elements.size();
            }

            @Override
            public boolean add(E e) {
                return elements.add(e);
            }
        }

        CustomList<Integer> customList = new CustomList<>();
        customList.add(1);
        customList.add(2);
        customList.add(3);
        System.out.println("Custom AbstractList: " + customList);

        System.out.println();
    }

    // ========== Collections Helper Methods ==========

    private static void collectionsHelperMethods() {
        System.out.println("--- Collections Helper Methods ---");

        List<String> list = new ArrayList<>(Arrays.asList("Apple", "Banana", "Cherry", "Date"));

        // Collections.copy
        List<String> dest = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
        Collections.copy(dest, list.subList(0, 2));
        System.out.println("After copy: " + dest);

        // Collections.fill
        List<String> fillList = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        Collections.fill(fillList, "X");
        System.out.println("After fill: " + fillList);

        // Collections.swap
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collections.swap(numbers, 0, 4);
        System.out.println("After swap(0, 4): " + numbers);

        // Collections.rotate
        Collections.rotate(numbers, 2);
        System.out.println("After rotate(2): " + numbers);

        // Collections.shuffle
        Collections.shuffle(numbers);
        System.out.println("After shuffle: " + numbers);

        // Collections.reverse
        Collections.reverse(numbers);
        System.out.println("After reverse: " + numbers);

        // Collections.sort
        Collections.sort(numbers);
        System.out.println("After sort: " + numbers);

        // Collections.binarySearch
        int index = Collections.binarySearch(numbers, 3);
        System.out.println("Binary search for 3: index " + index);

        // Collections.min and max
        System.out.println("Min: " + Collections.min(numbers));
        System.out.println("Max: " + Collections.max(numbers));

        // Collections.frequency
        List<String> words = Arrays.asList("a", "b", "a", "c", "a");
        System.out.println("Frequency of 'a': " + Collections.frequency(words, "a"));

        // Collections.disjoint
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        System.out.println("Disjoint: " + Collections.disjoint(list1, list2));

        // Collections.addAll
        List<String> names = new ArrayList<>();
        Collections.addAll(names, "Alice", "Bob", "Charlie");
        System.out.println("After addAll: " + names);

        // Collections.replaceAll
        Collections.replaceAll(names, "Bob", "Robert");
        System.out.println("After replaceAll: " + names);

        // Collections.indexOfSubList
        List<Integer> subList = Arrays.asList(2, 3);
        int subIndex = Collections.indexOfSubList(numbers, subList);
        System.out.println("Index of sublist [2, 3]: " + subIndex);

        // Unmodifiable, singleton, empty collections
        List<String> unmodifiable = Collections.unmodifiableList(names);
        Set<String> singleton = Collections.singleton("Single");
        List<String> emptyList = Collections.emptyList();
        Map<String, Integer> emptyMap = Collections.emptyMap();

        System.out.println("Unmodifiable: " + unmodifiable);
        System.out.println("Singleton: " + singleton);
        System.out.println("Empty list: " + emptyList);

        // Collections.nCopies
        List<String> copies = Collections.nCopies(5, "X");
        System.out.println("nCopies(5, 'X'): " + copies);

        System.out.println();
    }

    // ========== Poll Operations ==========

    private static void pollOperations() {
        System.out.println("--- Poll Operations (pollFirst, pollLast) ---");

        // LinkedList (implements Deque)
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.addAll(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("LinkedList: " + linkedList);

        System.out.println("pollFirst(): " + linkedList.pollFirst());
        System.out.println("pollLast(): " + linkedList.pollLast());
        System.out.println("After polls: " + linkedList);

        // ArrayDeque
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addAll(Arrays.asList("A", "B", "C", "D", "E"));
        System.out.println("ArrayDeque: " + deque);

        System.out.println("pollFirst(): " + deque.pollFirst());
        System.out.println("pollLast(): " + deque.pollLast());
        System.out.println("After polls: " + deque);

        // TreeSet (NavigableSet)
        TreeSet<Integer> treeSet = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));
        System.out.println("TreeSet: " + treeSet);

        System.out.println("pollFirst(): " + treeSet.pollFirst());
        System.out.println("pollLast(): " + treeSet.pollLast());
        System.out.println("After polls: " + treeSet);

        // ConcurrentLinkedDeque
        ConcurrentLinkedDeque<String> concurrentDeque = new ConcurrentLinkedDeque<>();
        concurrentDeque.addAll(Arrays.asList("X", "Y", "Z"));
        System.out.println("ConcurrentLinkedDeque: " + concurrentDeque);

        System.out.println("pollFirst(): " + concurrentDeque.pollFirst());
        System.out.println("pollLast(): " + concurrentDeque.pollLast());
        System.out.println("After polls: " + concurrentDeque);

        // poll vs pollFirst
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(5, 2, 8, 1, 9));
        System.out.println("PriorityQueue: " + pq);
        System.out.println("poll() [removes min]: " + pq.poll());
        System.out.println("After poll: " + pq);

        System.out.println();
    }
}
