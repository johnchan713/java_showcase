# Java Collections Summary

Comprehensive guide to Java collections organized by thread-safety and characteristics.

## Quick Reference Table

| Collection | Thread-Safe | Ordered | Sorted | Null Keys | Null Values | Performance | Use Case |
|------------|-------------|---------|--------|-----------|-------------|-------------|----------|
| ArrayList | ✗ No | ✓ Yes (insertion) | ✗ No | N/A | ✓ Yes | O(1) get, O(n) add/remove | General purpose list |
| LinkedList | ✗ No | ✓ Yes (insertion) | ✗ No | N/A | ✓ Yes | O(1) add/remove ends | Queue, frequent insert/delete |
| Vector | ✓ Yes | ✓ Yes (insertion) | ✗ No | N/A | ✓ Yes | O(1) get (slower) | Legacy, thread-safe list |
| Stack | ✓ Yes | ✓ Yes (LIFO) | ✗ No | N/A | ✓ Yes | O(1) push/pop | Legacy stack (use Deque) |
| CopyOnWriteArrayList | ✓ Yes | ✓ Yes (insertion) | ✗ No | N/A | ✓ Yes | Read O(1), Write O(n) | Read-heavy, concurrent iteration |
| HashSet | ✗ No | ✗ No | ✗ No | ✓ Yes (1) | ✓ Yes | O(1) operations | Unique elements, fast lookup |
| LinkedHashSet | ✗ No | ✓ Yes (insertion) | ✗ No | ✓ Yes (1) | ✓ Yes | O(1) operations | Unique + insertion order |
| TreeSet | ✗ No | ✓ Yes (sorted) | ✓ Yes | ✗ No | ✗ No | O(log n) operations | Unique + sorted |
| CopyOnWriteArraySet | ✓ Yes | ✓ Yes (insertion) | ✗ No | ✓ Yes | ✓ Yes | Read O(1), Write O(n) | Read-heavy unique elements |
| HashMap | ✗ No | ✗ No | ✗ No | ✓ Yes (1) | ✓ Yes | O(1) operations | General purpose map |
| LinkedHashMap | ✗ No | ✓ Yes (insertion/access) | ✗ No | ✓ Yes (1) | ✓ Yes | O(1) operations | Map + ordering, LRU cache |
| TreeMap | ✗ No | ✓ Yes (sorted) | ✓ Yes | ✗ No | ✓ Yes | O(log n) operations | Sorted map |
| Hashtable | ✓ Yes | ✗ No | ✗ No | ✗ No | ✗ No | O(1) (slower) | Legacy thread-safe map |
| ConcurrentHashMap | ✓ Yes | ✗ No | ✗ No | ✗ No | ✗ No | O(1) high concurrency | High-performance thread-safe |
| EnumMap | ✗ No | ✓ Yes (enum order) | ✗ No | ✗ No (enum only) | ✓ Yes | Very fast | Enum keys only |
| WeakHashMap | ✗ No | ✗ No | ✗ No | ✗ No | ✓ Yes | O(1) | Weak references, caching |
| IdentityHashMap | ✗ No | ✗ No | ✗ No | ✓ Yes | ✓ Yes | O(1) | Reference equality |
| PriorityQueue | ✗ No | ✓ Yes (priority) | ✓ Heap | ✗ No | ✗ No | O(log n) add/remove | Priority-based processing |
| ArrayDeque | ✗ No | ✓ Yes (insertion) | ✗ No | ✗ No | ✗ No | O(1) ends | Stack/Queue, better than Stack |
| ConcurrentLinkedQueue | ✓ Yes | ✓ Yes (insertion) | ✗ No | ✗ No | ✗ No | O(1) operations | Thread-safe queue |
| ConcurrentLinkedDeque | ✓ Yes | ✓ Yes (insertion) | ✗ No | ✗ No | ✗ No | O(1) operations | Thread-safe deque |

## Thread-Safe Collections

### ✓ Fully Thread-Safe (No External Synchronization Needed)

#### Legacy (Synchronized - All methods locked)
- **Vector** - Legacy List, synchronized methods
  - ✓ All operations atomic
  - ✗ Slow due to global lock
  - ✗ Manual sync needed for iteration
  - USE: Legacy code only, prefer CopyOnWriteArrayList

- **Stack** - Legacy LIFO, extends Vector
  - ✓ Thread-safe push/pop
  - ✗ Same Vector limitations
  - USE: Legacy code only, prefer ArrayDeque

- **Hashtable** - Legacy Map, synchronized methods
  - ✓ All operations atomic
  - ✗ No null keys/values
  - ✗ Slow due to global lock
  - USE: Legacy code only, prefer ConcurrentHashMap

#### Modern Concurrent Collections

- **ConcurrentHashMap** - High-performance thread-safe map
  - ✓ Lock striping, high concurrency
  - ✓ Atomic operations (putIfAbsent, compute, etc.)
  - ✓ No null keys/values (design choice)
  - ✗ Slightly more overhead than HashMap
  - USE: Multi-threaded map operations, high throughput

- **CopyOnWriteArrayList** - Thread-safe list
  - ✓ Safe iteration during modification
  - ✓ No ConcurrentModificationException
  - ✓ Perfect for read-heavy workloads
  - ✗ Expensive writes (copies entire array)
  - USE: Event listeners, configuration lists

- **CopyOnWriteArraySet** - Thread-safe set
  - ✓ Safe iteration during modification
  - ✓ Uses CopyOnWriteArrayList internally
  - ✗ O(n) contains() operation
  - ✗ Expensive writes
  - USE: Small sets, read-heavy unique elements

- **ConcurrentLinkedQueue** - Lock-free queue
  - ✓ Non-blocking algorithms
  - ✓ High throughput
  - ✓ Unbounded
  - ✗ Size() may be O(n)
  - USE: Producer-consumer, message passing

- **ConcurrentLinkedDeque** - Lock-free deque
  - ✓ Non-blocking algorithms
  - ✓ Both ends operations
  - ✓ Work-stealing algorithms
  - ✗ Size() may be O(n)
  - USE: Work-stealing, double-ended queue

#### Synchronized Wrappers
```java
Collections.synchronizedList(new ArrayList<>())
Collections.synchronizedSet(new HashSet<>())
Collections.synchronizedMap(new HashMap<>())
```
- ✓ Makes any collection thread-safe
- ✗ Global lock, lower performance
- ✗ Manual sync needed for iteration
- USE: Simple thread-safety, low contention

## Non-Thread-Safe Collections

### Lists
- **ArrayList** - Dynamic array
  - ✓ Fast random access O(1)
  - ✓ Memory efficient
  - ✗ Slow insert/delete in middle O(n)
  - USE: Default list choice, random access

- **LinkedList** - Doubly-linked list
  - ✓ Fast insert/delete O(1)
  - ✓ Implements Queue/Deque
  - ✗ Slow random access O(n)
  - ✗ More memory overhead
  - USE: Frequent insert/delete, queue operations

### Sets
- **HashSet** - Hash table based set
  - ✓ Fast operations O(1)
  - ✓ Allows null
  - ✗ No ordering
  - USE: Unique elements, fast lookup

- **LinkedHashSet** - Hash table + linked list
  - ✓ Maintains insertion order
  - ✓ Fast operations O(1)
  - ✗ Slightly more overhead
  - USE: Unique + predictable iteration order

- **TreeSet** - Red-black tree
  - ✓ Sorted order (natural/comparator)
  - ✓ NavigableSet operations
  - ✗ Slower O(log n)
  - ✗ No null (in natural ordering)
  - USE: Sorted unique elements, range queries

### Maps
- **HashMap** - Hash table based map
  - ✓ Fast operations O(1)
  - ✓ Allows 1 null key, many null values
  - ✗ No ordering
  - USE: Default map choice, key-value storage

- **LinkedHashMap** - HashMap + linked list
  - ✓ Maintains insertion/access order
  - ✓ Can create LRU cache
  - ✓ Fast operations O(1)
  - ✗ Slightly more overhead
  - USE: Ordered map, LRU cache implementation

- **TreeMap** - Red-black tree
  - ✓ Sorted by keys
  - ✓ NavigableMap operations
  - ✗ Slower O(log n)
  - ✗ No null keys (with natural ordering)
  - USE: Sorted keys, range queries

- **EnumMap** - Specialized for enum keys
  - ✓ Very fast and memory efficient
  - ✓ Maintains enum order
  - ✗ Keys must be enum
  - USE: Enum to value mapping

- **WeakHashMap** - Weak reference keys
  - ✓ Allows garbage collection of keys
  - ✓ Good for caching
  - ✗ Unpredictable entry removal
  - USE: Memory-sensitive caches

- **IdentityHashMap** - Reference equality
  - ✓ Uses == instead of equals()
  - ✓ Specific use cases
  - ✗ Unusual semantics
  - USE: Object graph traversal, serialization

### Queues/Deques
- **PriorityQueue** - Binary heap
  - ✓ Priority ordering
  - ✓ Fast peek/poll O(log n)
  - ✗ Not thread-safe
  - USE: Task scheduling, priority processing

- **ArrayDeque** - Resizable array deque
  - ✓ Faster than LinkedList
  - ✓ No capacity restrictions
  - ✓ Better stack than Stack
  - ✗ No null elements
  - USE: Stack, queue, work-stealing

## Special Purpose Collections

### BitSet
- **BitSet** - Bit array
  - ✓ Memory efficient for bits
  - ✓ Fast bit operations
  - ✗ Not a Collection
  - ✗ Primitive int indices only
  - USE: Flags, bloom filters, bit manipulation

### Arrays
- **Arrays** class - Utilities for arrays
  - ✓ Fast, primitive support
  - ✗ Fixed size
  - ✗ No automatic resizing
  - USE: Fixed-size data, performance critical

## Decision Tree

### Choosing a Collection

```
Need unique elements?
├─ No → Need FIFO/LIFO?
│   ├─ No → Need key-value pairs?
│   │   ├─ No → Use LIST
│   │   └─ Yes → Use MAP
│   └─ Yes → Use QUEUE/DEQUE
└─ Yes → Use SET

Need thread-safety?
├─ No → Use regular collections
└─ Yes → Read-heavy?
    ├─ Yes → CopyOnWriteArrayList/Set
    └─ No → ConcurrentHashMap/Queue

Need ordering?
├─ No → HashMap/HashSet
├─ Insertion order → LinkedHashMap/LinkedHashSet
└─ Sorted → TreeMap/TreeSet

Need performance?
├─ Critical → ArrayList, HashMap, HashSet
├─ Memory → ArrayList over LinkedList
└─ Concurrent → ConcurrentHashMap
```

## Performance Characteristics

### Time Complexity Summary

#### List Operations
| Operation | ArrayList | LinkedList | Vector | CopyOnWriteArrayList |
|-----------|-----------|------------|--------|----------------------|
| get(i) | O(1) | O(n) | O(1) | O(1) |
| add(e) | O(1)* | O(1) | O(1)* | O(n) |
| add(i,e) | O(n) | O(1)** | O(n) | O(n) |
| remove(i) | O(n) | O(1)** | O(n) | O(n) |
| contains | O(n) | O(n) | O(n) | O(n) |

*Amortized  **If iterator at position

#### Set Operations
| Operation | HashSet | LinkedHashSet | TreeSet |
|-----------|---------|---------------|---------|
| add | O(1) | O(1) | O(log n) |
| remove | O(1) | O(1) | O(log n) |
| contains | O(1) | O(1) | O(log n) |

#### Map Operations
| Operation | HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
|-----------|---------|---------------|---------|-------------------|
| get | O(1) | O(1) | O(log n) | O(1) |
| put | O(1) | O(1) | O(log n) | O(1) |
| remove | O(1) | O(1) | O(log n) | O(1) |
| containsKey | O(1) | O(1) | O(log n) | O(1) |

## Best Practices

### Thread-Safety Recommendations

1. **Single-threaded**: Use non-synchronized collections (ArrayList, HashMap, etc.)
2. **Read-heavy multi-threaded**: Use CopyOnWriteArrayList/Set
3. **Balanced read/write**: Use ConcurrentHashMap, ConcurrentLinkedQueue
4. **Simple synchronization**: Use Collections.synchronizedXxx()
5. **Legacy code**: May use Vector, Hashtable (but prefer modern alternatives)

### Common Pitfalls

1. **Don't use synchronized collections for iteration without manual sync**
```java
// WRONG - can throw ConcurrentModificationException
List syncList = Collections.synchronizedList(new ArrayList());
for(Object o : syncList) { } // NOT SAFE

// CORRECT
synchronized(syncList) {
    for(Object o : syncList) { } // SAFE
}
```

2. **Don't use Vector/Hashtable in new code**
   - Use ArrayList + synchronization if needed
   - Use ConcurrentHashMap instead of Hashtable

3. **Don't use CopyOnWrite collections for write-heavy workloads**
   - Every write copies the entire array - O(n) operation

4. **Don't assume HashMap/HashSet iteration order**
   - Use LinkedHashMap/LinkedHashSet for predictable order
   - Use TreeMap/TreeSet for sorted order

## String Processing Summary

| Type | Mutable | Thread-Safe | Performance | Use Case |
|------|---------|-------------|-------------|----------|
| String | ✗ No | ✓ Yes (immutable) | Slow concatenation | Fixed strings |
| StringBuilder | ✓ Yes | ✗ No | Fast | Single-thread building |
| StringBuffer | ✓ Yes | ✓ Yes (synchronized) | Medium | Multi-thread building |

**Recommendation**: Use StringBuilder in 99% of cases, StringBuffer only when actually sharing between threads.
