package ru.spbstu.vadim01er.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedBlackTree<Key extends Comparable<Key>, Value> implements ru.spbstu.vadim01er.bst.Bst<Key, Value> {

    private static final boolean BLACK = false;
    private static final boolean RED = true;

    private Node root;
    private int size;

    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        boolean color;

        public Node(Key key, Value value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    // Асимптотик O(log(N))
    @Nullable
    @Override
    public Value get(@NotNull Key key) {
        Node node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node get(Node node, Key key) {
        if (node == null)
            return null;
        int compare = key.compareTo(node.key);
        if (compare < 0)
            return get(node.left, key);
        if (compare > 0)
            return get(node.right, key);
        return node;
    }
    // Асимптотик O(log(N))
    @Override
    public void put(@NotNull Key key, @NotNull Value value) {
        root = put(root, key, value);
    }

    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }
        int compare = key.compareTo(node.key);
        if (compare > 0)
            node.right = put(node.right, key, value);
        else if (compare < 0)
            node.left = put(node.left, key, value);
        else
            node.value = value;
        return fixUp(node);
    }

    // Асимптотик O(1)
    private Node fixUp(Node node) {
        if (isRed(node.right) && !isRed(node.left))
            node = rotateL(node);
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateR(node);
        if (isRed(node.left) && isRed(node.right))
            flipColor(node);
        return node;
    }

    // Асимптотик O(1)
    private void flipColor(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    // Асимптотик O(1)
    private Node rotateL(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = node.color;
        node.color = RED;
        return temp;
    }

    // Асимптотик O(1)
    private Node rotateR(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = node.color;
        node.color = RED;
        return temp;
    }

    // Асимптотик O(1)
    private boolean isRed(Node node) {
        if (node == null)
            return false;
        else
            return node.color == RED;
    }

    // Асимптотик O(log(N))
    @Nullable
    @Override
    public Value remove(@NotNull Key key) {
        Value removedValue = get(key);
        if (removedValue == null)
            return null;
        root.color = RED;
        root = remove(root, key);
        size--;
        return removedValue;
    }

    private Node remove(Node node, Key key) {
        if (node == null)
            return null;
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            if (node.left != null) {
                if (!isRed(node.left) && !isRed(node.left.left))
                    node = moveRedLeft(node);
                node.left = remove(node.left, key);
            }
        } else {
            if (isRed(node.left)) {
                node = rotateR(node);
                node.right = remove(node.right, key);
            } else if (compare == 0 && node.right == null) {
                return null;
            } else {
                if (node.right != null && !isRed(node.right) && !isRed(node.right.left))
                    node = moveRedRight(node);
                if (node.key == key) {
                    Node minNode = min(node.right);
                    node.value = minNode.value;
                    node.key = minNode.key;
                    node.right = removeMin(node.right);
                } else {
                    node.right = remove(node.right, key);
                }
            }
        }
        return fixUp(node);
    }

    // Асимптотик O(height)
    private Node removeMin(Node node) {
        if (node.left == null)
            return null;
        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);
        node.left = removeMin(node.left);
        return fixUp(node);
    }

    // Асимптотик O(1)
    private Node moveRedLeft(Node node) {
        flipColor(node);
        if (isRed(node.right.left)) {
            node.right = rotateR(node.right);
            node = rotateL(node);
            flipColor(node);
        }
        return node;
    }

    // Асимптотик O(1)
    private Node moveRedRight(Node node) {
        flipColor(node);
        if (isRed(node.left.left)) {
            node = rotateR(node);
        }
        return node;
    }

    // Асимптотик O(height)
    private Node min(Node node) {
        if (node == null)
            return null;
        if (node.left == null)
            return node;
        return min(node.left);
    }

    @Nullable
    @Override
    public Key min() {
        Node node = min(root);
        return node == null ? null : node.key;
    }

    @Nullable
    @Override
    public Value minValue() {
        Node node = min(root);
        return node == null ? null : node.value;
    }

    // Асимптотик O(height)
    private Node max(Node node) {
        if (node == null)
            return null;
        return node.right == null ? node : max(node.right);
    }

    @Nullable
    @Override
    public Key max() {
        Node node = max(root);
        return node == null? null : node.key;
    }

    @Nullable
    @Override
    public Value maxValue() {
        Node node = max(root);
        return node == null? null : node.value;
    }

    // Асимптотик O(log(N))
    @Nullable
    @Override
    public Key floor(@NotNull Key key) {
        return floor(root, key, null);
    }

    private Key floor(Node node, Key key, Key maxKey) {
        if (node == null)
            return maxKey;
        int compare = node.key.compareTo(key);
        if (compare > 0) {
            maxKey = floor(node.left, key, maxKey);
        } else if (compare < 0) {
            maxKey = node.key;
            maxKey = floor(node.right, key, maxKey);
        } else {
            maxKey = node.key;
        }
        return maxKey;
    }

    // Асимптотик O(log(N))
    @Nullable
    @Override
    public Key ceil(@NotNull Key key) {
        return ceil(root, key, null);
    }

    private Key ceil(Node node, Key key, Key minKey) {
        if (node == null)
            return minKey;
        int compare = node.key.compareTo(key);
        if (compare > 0) {
            if (minKey == null|| minKey.compareTo(node.key) > 0) {
                minKey = node.key;
            }
            minKey = ceil(node.left, key, minKey);
        } else if (compare < 0) {
            minKey = ceil(node.right, key, minKey);
        } else{
            minKey = node.key;
        }
        return minKey;
    }

    @Override
    public int size() {
        return size;
    }

}
