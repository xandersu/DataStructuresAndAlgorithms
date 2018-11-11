package com.xandersu.datastructuresandalgorithms.avl;

import lombok.ToString;

import java.util.ArrayList;

/**
 * @Author: suxun
 * @Date: 2018/11/11 13:02
 * @Description:
 */
@ToString
public class AVLTree<K extends Comparable<K>, V> {

    private Node root;
    private int size;

    public AVLTree() {

    }

    //对y右旋，返回新根结点X
    //    y                       X
    //   / \                    /  \
    //   x  T4    右旋转        z    y
    //  / \       ------>     / \  / \
    // z  t3                 t1 t2 t3 t4
    /// \
    //t1 t2
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node t3 = x.right;
        //右旋过程
        x.right = y;
        y.left = t3;
        //update height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    //左旋转
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node t2 = y.left;
        //右旋过程
        x.left = y;
        x.right = t2;
        //update height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    public void add(K k, V v) {
        root = add(root, k, v);
    }

    //向以node为根的二分搜索树中插入元素（k，v），递归算法
    private Node add(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = add(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = add(node.right, key, value);
        } else {
            node.value = value;
        }
        //更新height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(node);
//        if (Math.abs(balanceFactor) > 1) {
//            System.out.println("unbalance:" + balanceFactor);
//        }
        //平衡维护
        //LL
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }
        //RR
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }
        //LR
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {

            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        //RL
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    public boolean isBST() {
        ArrayList<K> keys = new ArrayList<>();
        inOrder(root, keys);
        for (int i = 1; i < keys.size(); i++) {
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int balanceFactor = getBalanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private void inOrder(Node node, ArrayList<K> keys) {
        if (node == null) {
            inOrder(node.left, keys);
            keys.add(node.key);
            inOrder(node.right, keys);
        }
    }

    public V remove(K key) {
        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    //二分搜索树删除任意节点
    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }
        Node retNode;
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            retNode = node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            retNode = node;
        } else { //key.compareTo(node.key) = 0
            //待删除节点左子树为空
            if (node.left == null) {
                Node right = node.right;
                node.right = null;
                size--;
                retNode = right;
            }
            //待删除节点右子树为空
            else if (node.right == null) {
                Node left = node.left;
                node.left = null;
                size--;
                retNode = left;
            } else {
                //左右节点都不为空
                Node successor = minimun(node.right);
                successor.right = remove(node.right, successor.key);
                successor.left = node.left;
                node.left = node.right = null;
                retNode = successor;
            }
        }
        if (retNode == null) {
            return null;
        }
        //更新height
        retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(retNode);
//        if (Math.abs(balanceFactor) > 1) {
//            System.out.println("unbalance:" + balanceFactor);
//        }
        //平衡维护
        //LL
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0) {
            return rightRotate(retNode);
        }
        //RR
        if (balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0) {
            return leftRotate(retNode);
        }
        //LR
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) < 0) {

            retNode.left = leftRotate(retNode.left);
            return rightRotate(retNode);
        }
        //RL
        if (balanceFactor < -1 && getBalanceFactor(retNode.right) > 0) {
            retNode.right = rightRotate(retNode.right);
            return leftRotate(retNode);
        }
        return retNode;
    }

    private Node minimun(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimun(node.left);
    }

    private Node maxmun(Node node) {
        if (node.right == null) {
            return node;
        }
        return maxmun(node.right);
    }

    //删除以node为根的二分搜索树中最小节点
    //返回删除节点后的心二分搜索树的根
//    private Node removeMin(Node node) {
//        if (node.left == null) {
//            Node rightNode = node.right;
//            node.right = null;
//            size--;
//            return rightNode;
//        }
//        node.left = removeMin(node.left);
//        return node;
//    }

    public boolean contains(K k) {
        return getNode(root, k) != null;
    }

    private Node getNode(Node node, K key) {
        if (node == null) {
            return node;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        } else if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else {
            return getNode(node.right, key);
        }
    }

    public V get(K k) {
        Node node = getNode(root, k);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException(key + "doesnt exist!");
        }
        node.value = newValue;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private class Node {
        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    "value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
