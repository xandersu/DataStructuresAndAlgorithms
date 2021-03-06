package com.xandersu.class207_datastructuresandalgorithms.heap;

import com.xandersu.class207_datastructuresandalgorithms.array.MyArrayGeneric;

/**
 * @Author: suxun
 * @Date: 2018/10/21 13:04
 * @Description:O(logn)复杂度 几乎都跟树有关
 * 二叉堆 Binary Heap：是一颗完全二叉树(不一定是满的，不满的一定是在右下方)把元素排列成树的形状
 * 堆中某个节点的值总是不大于其父节点的值（最大堆（相应的可以定义最小堆））
 * 用数组储存二叉堆：
 * 0 - 1 - 2 - 3 - 4 - 5
 * -62 -41 -30 -28 -16
 * parent(i) = i/2; left child (i) = 2*i; right child (i) = 2 * i +1;
 * 0 - 1 - 2 - 3 - 4 - 5
 * 62 -41 -30 -28 -16
 * parent(i) = (i-1)/2; left child (i) = 2*i+1; right child (i) = 2 * i +2;
 * <p>
 * d叉堆 d-ary heap
 * </p>
 * 索引堆
 */
public class MaxHeap<E extends Comparable<E>> {

    private MyArrayGeneric<E> data;

    public MaxHeap() {
        data = new MyArrayGeneric<>();
    }

    public MaxHeap(int capacity) {
        data = new MyArrayGeneric<>(capacity);
    }

    public MaxHeap(E[] arr) {
        data = new MyArrayGeneric<>(arr);
        for (int i = parent(arr.length - 1); i >= 0; i--) {
            shiftDown(i);
        }
    }

    public int getSize() {
        return data.getSize();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    //返回完全二叉树的数组表示中，一个索引所表示的元素的父亲节点的索引
    private int parent(int index) {
        if (index == 0) {
            throw new IllegalArgumentException("index-0 doesnt hava parent.");
        }
        return (index - 1) / 2;
    }

    //返回完全二叉树的数组表示中，一个索引所表示元素的左孩子节点的索引
    private int leftChild(int index) {
        return index * 2 + 1;
    }

    //返回完全二叉树的数组表示中，一个索引所表示元素的右孩子节点的索引
    private int rightChild(int index) {
        return index * 2 + 2;
    }

    //向堆添加元素 (shift up)
    public void add(E e) {
        data.addLast(e);
        shiftUp(data.getSize() - 1);
    }

    private void shiftUp(int k) {
        while (k > 0 && data.get(parent(k)).compareTo(data.get(k)) < 0) {
            data.swap(k, parent(k));
            k = parent(k);
        }
    }

    //取最大值 shift down
    public E extractMax() {
        E ret = findMax();
        data.swap(0, data.getSize() - 1);
        data.removeLast();
        shiftDown(0);
        return ret;
    }

    private void shiftDown(int k) {
        while (leftChild(k) < data.getSize()) {
            int j = leftChild(k);
            if ((j + 1) < data.getSize() && data.get(j + 1).compareTo(data.get(j)) > 0) {
                j = rightChild(k);
                //data[j]是leftchild 和 rightchild最大值
            }
            if (data.get(k).compareTo(data.get(j)) >= 0) {
                break;
            }
            data.swap(k, j);
            k = j;
        }
    }

    public E findMax() {
        if (data.getSize() == 0) {
            throw new IllegalArgumentException("data.size = 0! ");
        }
        return data.get(0);
    }

    //取出最大，放入新元素
    public E replace(E e) {
        E ret = findMax();
        data.set(0, e);
        shiftDown(0);
        return ret;
    }
    //将任意数组整理成堆的形状

}
