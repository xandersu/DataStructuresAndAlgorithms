package com.xandersu.datastructuresandalgorithms.hashtable;

import java.util.TreeMap;

/**
 * @Author: suxun
 * @Date: 2018/11/18 14:22
 * @Description:
 */
public class HashTable<K, V> {

    private static final int[] capacity = {
            53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157, 98317
            , 196613, 393241, 786433, 1572869, 3145739, 6291469, 12582917
            , 25165843, 50331653, 100663319, 201326611, 402653189, 805306457, 1610612741};
    private static final int upperTol = 10;
    private static final int lowerTol = 2;
    //public static final int initCapacity = 7;
    private static int capacityIndex = 0;
    private TreeMap<K, V>[] hashTable;
    private int M;
    private int size;

    private HashTable(int M) {
        this.M = M;
        size = 0;
        hashTable = new TreeMap[M];
        for (int i = 0; i < M; i++) {
            hashTable[i] = new TreeMap<>();
        }
    }

    public HashTable() {
        this(capacity[capacityIndex]);
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public int getSize() {
        return size;
    }

    public void add(K key, V value) {
        TreeMap<K, V> map = hashTable[hash(key)];
        if (map.containsKey(key)) {
            map.put(key, value);
        } else {
            map.put(key, value);
            size++;
            //编程技巧：整数除法转成乘法
            if (size >= upperTol * M && capacityIndex + 1 < capacity.length) {
                resize(capacity[++capacityIndex]);
            }
        }
    }

    public V remove(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        V ret = null;
        if (map.containsKey(key)) {
            ret = map.remove(key);
            size--;
            if (size <= lowerTol * M && capacityIndex - 1 >= 0) {
                resize(capacity[--capacityIndex]);
            }
        }
        return ret;
    }

    private void resize(int newM) {
        TreeMap<K, V>[] newHashTable = new TreeMap[newM];
        for (int i = 0; i < newM; i++) {
            newHashTable[i] = new TreeMap<>();
        }
        int oldM = M;
        this.M = newM;
        for (int i = 0; i < oldM; i++) {
            TreeMap<K, V> map = hashTable[i];
            for (K key : map.keySet()) {
                newHashTable[hash(key)].put(key, map.get(key));
            }
        }
        this.hashTable = newHashTable;
    }

    public void set(K key, V value) {
        TreeMap<K, V> map = hashTable[hash(key)];
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("does not has key");
        }
        map.put(key, value);
    }

    public boolean contains(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        return map.containsKey(key);
    }

    public V get(K key) {
        TreeMap<K, V> map = hashTable[hash(key)];
        return map.get(key);
    }
}
