package org.task2;

import java.util.ArrayList;
import java.util.List;

public class OpenHashTable {

    private static class HashNode {
        int key;
        HashNode next;

        public HashNode(int key) {
            this.key = key;
            this.next = null;
        }
    }

    private int capacity;
    private HashNode[] table;

    private List<String> executionTrace;

    public OpenHashTable(int capacity) {
        this.capacity = capacity;
        this.table = new HashNode[capacity];
        this.executionTrace = new ArrayList<>();
    }

    private void record(String point) {
        executionTrace.add(point);
    }

    public List<String> getTrace() {
        return new ArrayList<>(executionTrace);
    }

    public void clearTrace() {
        executionTrace.clear();
    }

    private int hashFunction(int key) {
        return Math.abs(key) % capacity;
    }

    public void put(int key) {
        record("PUT_START");
        int index = hashFunction(key);
        record("CALC_HASH");

        HashNode current = table[index];
        while (current != null) {
            record("ITERATE_CHAIN");
            if (current.key == key) {
                record("FOUND_DUPLICATE");
                return;
            }
            current = current.next;
        }

        record("INSERT_NEW");
        HashNode newNode = new HashNode(key);
        newNode.next = table[index];
        table[index] = newNode;
    }

    public boolean contains(int key) {
        record("SEARCH_START");
        int index = hashFunction(key);
        record("CALC_HASH");

        HashNode current = table[index];
        while (current != null) {
            record("ITERATE_CHAIN");
            if (current.key == key) {
                record("FOUND_TRUE");
                return true;
            }
            current = current.next;
        }
        record("FOUND_FALSE");
        return false;
    }

    public void remove(int key) {
        record("REMOVE_START");
        int index = hashFunction(key);
        record("CALC_HASH");

        HashNode current = table[index];
        HashNode prev = null;

        while (current != null) {
            record("ITERATE_CHAIN");
            if (current.key == key) {
                record("REMOVE_FOUND");
                if (prev == null) {
                    record("REMOVE_HEAD");
                    table[index] = current.next;
                } else {
                    record("REMOVE_INNER");
                    prev.next = current.next;
                }
                return;
            }
            prev = current;
            current = current.next;
        }
        record("REMOVE_NOT_FOUND");
    }


}