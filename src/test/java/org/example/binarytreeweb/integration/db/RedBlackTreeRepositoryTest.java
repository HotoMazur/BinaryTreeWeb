package org.example.binarytreeweb.integration.db;

import jakarta.persistence.EntityManager;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.example.binarytreeweb.repository.RedBlackTreeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RedBlackTreeRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RedBlackTreeRepository repository;

    @Test
    public void testInsertNodeRBTree() throws Exception {
        // Create a new node
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        // Save the node to the database
        repository.save(node);

        // Verify that the node was saved correctly
        RedBlackTreeEntity savedNode = repository.findById(node.getId()).orElse(null);
        assertNotNull(savedNode);
        assertEquals(node.getValue(), savedNode.getValue());
    }

    @Test
    public void testDeleteNodeRBTree() throws Exception {
        // Create a new node
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        // Save the node to the database
        repository.save(node);

        // Delete the node from the database
        repository.delete(node);

        // Verify that the node was deleted correctly
        RedBlackTreeEntity deletedNode = repository.findById(node.getId()).orElse(null);
        assertNull(deletedNode);
    }

    @Test
    public void testFindNodeById() throws Exception {
        // Create a new node
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        // Save the node to the database
        repository.save(node);

        // Find the node by ID
        RedBlackTreeEntity foundNode = repository.findById(node.getId()).orElse(null);

        // Verify that the node was found correctly
        assertNotNull(foundNode);
        assertEquals(node.getValue(), foundNode.getValue());
    }

    @Test
    public void findAllNodes() throws Exception {
        // Create a new node
        RedBlackTreeEntity node1 = new RedBlackTreeEntity();
        node1.setValue(10);
        node1.setColor("BLACK");
        node1.setParent_id(null);
        node1.setLeft_id(null);
        node1.setRight_id(null);



        RedBlackTreeEntity node2= new RedBlackTreeEntity();
        node2.setValue(20);
        node2.setColor("RED");
        node2.setParent_id(node1.getId());
        node2.setLeft_id(null);
        node2.setRight_id(null);

        repository.save(node1);
        repository.save(node2);

        // Find all nodes in the database
        Iterable<RedBlackTreeEntity> nodes = repository.findAll();

        // Verify that the nodes were found correctly
        assertNotNull(nodes);
        assertTrue(nodes.iterator().hasNext());
    }

}
