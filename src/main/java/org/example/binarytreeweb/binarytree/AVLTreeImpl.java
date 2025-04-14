package org.example.binarytreeweb.binarytree;

import lombok.Getter;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.example.binarytreeweb.util.comparator.ComparatorFactory;
import org.example.binarytreeweb.util.comparator.GenericComparatorFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.UUID;

@Component
@Getter
public class AVLTreeImpl<T> {
    private Node<T> root = null;
    private Comparator<T> comparator = null;
    private Class<?> expectedType = null;
    private final AvlTreeRepository repository;

    public AVLTreeImpl(AvlTreeRepository repository) {
        this.repository = repository;
    }

    public static class Node<T> {
        public T data;
        public Node<T> left, right;
        public int height;
        public UUID id;

        public Node(T data) {
            this.data = data;
            left = right = null;
            height = 1;
        }
    }

//    @Override
//    public void draw() {
//        AVLTreePrinter.printNode(this.root);
//    }
    @Transactional
    public AvlTreeEntity insertNode(T val) {
        if (val == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }
        this.root = insertNodeRec(this.root, val);
        Node<T> newNode = findNodeByValue(val);
        return new AvlTreeEntity(newNode.id, (Integer) val, newNode.height, null, null);
    }

    public Node<T> insertNodeRec(Node<T> root, T val) {
        root = handleInsertBaseCase(root, val);
        if (val == null) return root;
        if (expectedType == null) {
            expectedType = val.getClass();
        }

        ensureComparator(val);

        int comparison = comparator.compare(val, root.data);
        if (comparison < 0) {
            root.left = insertNodeRec(root.left, val);
        } else if (comparison > 0) {
            root.right = insertNodeRec(root.right, val);
        }

        root = makeBalance(root, val);

        UUID leftId = root.left != null ? root.left.id : null;
        UUID rightId = root.right != null ? root.right.id : null;
        AvlTreeEntity avlTreeEntity = new AvlTreeEntity(root.id,(Integer) root.data, root.height, leftId, rightId);
        AvlTreeEntity savedEntity = repository.save(avlTreeEntity);
        root.id = savedEntity.getId();

        return root;
    }

    @Transactional
    public void deleteNode(T val) {
        if (val == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        Node<T> nodeToDelete = findNodeByValue(val);
        if (nodeToDelete != null) {
            repository.updateLeftChildToNull(nodeToDelete.id);
            repository.updateRightChildToNull(nodeToDelete.id);

            repository.deleteById(nodeToDelete.id);
        }

        this.root = deleteNodeRec(this.root, val);
    }

    public Node<T> deleteNodeRec(Node<T> root, T val) {
        if (root == null) {
            return root;
        }

        root = performDelete(root, val);
        if (root == null) {
//            DatabaseManager.deleteNode(root.id);
        } else {
            UUID leftId = root.left != null ? root.left.id : null;
            UUID rightId = root.right != null ? root.right.id : null;
            AvlTreeEntity avlTreeEntity = new AvlTreeEntity(root.id,(Integer) root.data, root.height, leftId, rightId);
            AvlTreeEntity savedEntity = repository.save(avlTreeEntity);
            root.id = savedEntity.getId();
        }

        return root != null ? makeBalance(root, val) : null;
    }

    private Node<T> minValueNode(Node<T> root) {
        Node<T> curr = root;

        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    private int getHeight(Node<T> N) {
        if (N == null) {
            return 0;
        }
        return N.height;
    }

    private Node<T> rightRotate(Node<T> y) {
        if (y == null || y.left == null) {
            return y;
        }
        Node<T> x = y.left;
        Node<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    private Node<T> leftRotate(Node<T> x) {
        Node<T> y = x.right;
        Node<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    private int getBalanced(Node<T> N) {
        if (N == null) {
            return 0;
        }
        return getHeight(N.left) - getHeight(N.right);
    }

    private Node<T> makeBalance(Node<T> root, T val) {
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalanced(root);

        if (balance > 1 && comparator.compare(val, root.left.data) < 0) return rightRotate(root);
        if (balance < -1 && comparator.compare(val, root.right.data) > 0) return leftRotate(root);
        if (balance > 1 && comparator.compare(val, root.left.data) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && comparator.compare(val, root.right.data) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    public Node<T> getRoot() {
        return this.root;
    }

    private void createComparator(T val) {
        ComparatorFactory<T> factory = GenericComparatorFactory.getFactory(val);
        comparator = factory.createComparator();
    }

    private Node<T> handleInsertBaseCase(Node<T> root, T val) {
        if (root == null) {
            Node<T> newNode = new Node<>(val);
            AvlTreeEntity avlTreeEntity = new AvlTreeEntity(newNode.id,(Integer) val, newNode.height, null, null);
            AvlTreeEntity savedEntity = repository.save(avlTreeEntity);
            newNode.id = savedEntity.getId();
            return newNode;
        }
        if (val == null) {
            System.out.println("Can't add null");
            return root;
        }
        return root;
    }

    private void ensureComparator(T val) {
        if (comparator == null) {
            createComparator(val);
        }
    }

    private Node<T> performDelete(Node<T> root, T val) {
        int comparison = comparator.compare(val, root.data);

        if (comparison < 0) {
            root.left = deleteNodeRec(root.left, val);
        } else if (comparison > 0) {
            root.right = deleteNodeRec(root.right, val);
        } else {
            root = deleteTargetNode(root);
        }
        return root;
    }

    private Node<T> deleteTargetNode(Node<T> root) {
        if (root.left == null || root.right == null) {
            return root.left != null ? root.left : root.right;
        }

        Node<T> successor = minValueNode(root.right);
        root.data = successor.data;
        root.right = deleteNodeRec(root.right, successor.data);
        return root;
    }

    public Node<T> findNodeByValue(T val) {
        if (val == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }
        return findNodeByValueRec(this.root, val);
    }

    private Node<T> findNodeByValueRec(Node<T> root, T val) {
        if (root == null) {
            return null;
        }

        int comparison = comparator.compare(val, root.data);
        if (comparison == 0) {
            return root;
        } else if (comparison < 0) {
            return findNodeByValueRec(root.left, val);
        } else {
            return findNodeByValueRec(root.right, val);
        }
    }
}