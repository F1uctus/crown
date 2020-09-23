package com.crown.maps.pathfinding;

/**
 * A Simple heap. Behaves pretty much like priority queue.
 * <p>
 * Differences: Objects are allowed to change, but the heap must be notified
 * immediately by calling adjust(Object).
 * Each object MUST implement HeapNode interface. This allows for storing
 * and retrieving the heap index directly and hence speeding up the adjust
 * process greatly. Objects should not modify the index stored.
 * <p>
 * Downside: Each object can only be stored in ONE SimpleHeap. Hopefully this
 * is the typical case.
 *
 * @param <T> The type
 */
@SuppressWarnings("unchecked")
public class SimpleHeap<T extends HeapNode> {
    private Object[] queue;
    private int size = 0;

    public SimpleHeap(final int initialCapacity) {
        this.queue = new Object[initialCapacity];
    }

    /**
     * Add a new value. Null cannot be added.
     *
     * @param e value to add
     * @return true if could add, false if failed (can never happen)
     */
    public boolean add(final T e) {
        if (e == null)
            throw new NullPointerException();
        final int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0) {
            queue[0] = e;
            e.setHeapIndex(0);
        } else {
            siftUp(i, e);
        }
        return true;
    }

    private void siftUp(int k, final T x) {
        while (k > 0) {
            final int parent = (k - 1) >>> 1;
            final T e = (T) queue[parent];
            if (x.compareTo(e) >= 0)
                break;
            queue[k] = e;
            e.setHeapIndex(k);
            k = parent;
        }
        queue[k] = x;
        x.setHeapIndex(k);
    }

    private void siftDown(int k, final T x) {
        final int half = size >>> 1;
        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1;
            // assume left child is least
            T c = (T) queue[child];
            final int right = child + 1;
            if (right < size && c.compareTo((T) queue[right]) > 0)
                c = (T) queue[child = right];
            if (x.compareTo(c) <= 0)
                break;
            queue[k] = c;
            c.setHeapIndex(k);
            k = child;
        }
        queue[k] = x;
        x.setHeapIndex(k);
    }

    /**
     * Get the top element from the heap, removing it from the heap.
     * Returns null if none are left.
     *
     * @return element removed from heap, or null if empty
     */
    public T poll() {
        if (size == 0)
            return null;
        final int s = --size;
        final T result = (T) queue[0];
        final T x = (T) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        // Mark it as not in heap
        result.setHeapIndex(-1);
        return result;
    }

    public void adjust(final T x) {
        if (x.getHeapIndex() < 0 || x.getHeapIndex() >= size)
            return;
        siftUp(x.getHeapIndex(), x);
        siftDown(x.getHeapIndex(), x);
    }

    public boolean contains(final T x) {
        return x.getHeapIndex() >= 0 && x.getHeapIndex() < size && queue[x.getHeapIndex()] == x;
    }

    private void grow(final int minCapacity) {
        if (minCapacity < 0)
            throw new OutOfMemoryError();
        final int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64) ? ((oldCapacity + 1) * 2) : ((oldCapacity / 2) * 3));
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        final Object[] oldQueue = queue;
        queue = new Object[newCapacity];
        System.arraycopy(oldQueue, 0, queue, 0, oldQueue.length);
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            queue[i] = null;
        size = 0;
    }

    /**
     * Meant for testing rather than actual use.
     *
     * @param index index in internal queue
     * @return element at index, or null if invalid
     */
    public T getElementAt(final int index) {
        if (index >= 0 && index < queue.length)
            return (T) queue[index];
        else
            return null;
    }
}
