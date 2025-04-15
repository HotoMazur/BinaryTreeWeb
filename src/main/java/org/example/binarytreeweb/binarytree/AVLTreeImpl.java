package org.example.binarytreeweb.binarytree;

import lombok.Getter;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.example.binarytreeweb.util.comparator.ComparatorFactory;
import org.example.binarytreeweb.util.comparator.GenericComparatorFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;
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

    @Transactional
    public AvlTreeEntity insertNode(T val) {
        if (val == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        this.root = fetchRootFromDatabase();

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

        this.root = fetchRootFromDatabase();

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

    @Transactional
    public AvlTreeEntity updateNode(UUID id, T newVal) {
        if (id == null || newVal == null) {
            throw new IllegalArgumentException("ID and value cannot be null");
        }

        // Fetch the root from the database
        this.root = fetchRootFromDatabase();

        // Find the node by ID
        Node<T> nodeToUpdate = findNodeById(this.root, id);
        if (nodeToUpdate == null) {
            throw new IllegalArgumentException("Node with ID " + id + " not found");
        }

        // Update the node's value
        nodeToUpdate.data = newVal;

        // Rebalance the tree starting from the updated node
        this.root = rebalanceTree(this.root);

        // Save the updated node to the database
        updateNodeInDatabase(nodeToUpdate);

        return new AvlTreeEntity(nodeToUpdate.id, (Integer) newVal, nodeToUpdate.height, null, null);
    }

    private Node<T> findNodeById(Node<T> root, UUID id) {
        if (id == null) {
            return null;
        }

        Optional<AvlTreeEntity> entityOpt = repository.findById(id);
        if (entityOpt.isEmpty()) {
            return null;
        }

        AvlTreeEntity entity = entityOpt.get();
        Node<T> node = new Node<>((T) entity.getValue());
        node.id = entity.getId();
        node.height = entity.getHeight();
        node.left = fetchChildNode(entity.getLeft_id());
        node.right = fetchChildNode(entity.getRight_id());

        return node;
    }

    private Node<T> rebalanceTree(Node<T> root) {
        if (root == null) {
            return null;
        }

        root.left = rebalanceTree(root.left);
        root.right = rebalanceTree(root.right);

        root = makeBalance(root, root.data);

        updateNodeInDatabase(root);

        return root;
    }

    private void updateNodeInDatabase(Node<T> node) {
        if (node == null) return;

        UUID leftId = (node.left != null) ? node.left.id : null;
        UUID rightId = (node.right != null) ? node.right.id : null;

        AvlTreeEntity avlTreeEntity = new AvlTreeEntity(
                node.id,
                (Integer) node.data,
                node.height,
                leftId,
                rightId
        );

        repository.save(avlTreeEntity);
    }

    private Node<T> fetchRootFromDatabase() {
        Optional<AvlTreeEntity> rootEntityOpt = repository.findRootNode();
        if (rootEntityOpt.isEmpty()) {
            return null;
        }

        AvlTreeEntity rootEntity = rootEntityOpt.get();
        Node<T> rootNode = new Node<>((T) rootEntity.getValue());
        rootNode.id = rootEntity.getId();
        rootNode.height = rootEntity.getHeight();
        rootNode.left = fetchChildNode(rootEntity.getLeft_id());
        rootNode.right = fetchChildNode(rootEntity.getRight_id());
        return rootNode;
    }

    private Node<T> fetchChildNode(UUID nodeId) {
        if (nodeId == null) {
            return null;
        }

        AvlTreeEntity childEntity = repository.findById(nodeId).orElse(null);
        if (childEntity == null) {
            return null;
        }

        Node<T> childNode = new Node<>((T) childEntity.getValue());
        childNode.id = childEntity.getId();
        childNode.height = childEntity.getHeight();
        childNode.left = fetchChildNode(childEntity.getLeft_id());
        childNode.right = fetchChildNode(childEntity.getRight_id());
        return childNode;
    }
}