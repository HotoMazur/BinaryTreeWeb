package org.example.binarytreeweb.integration.db;

import jakarta.persistence.EntityManager;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AvlTreeRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AvlTreeRepository repository;

    @Test
    public void testInsertNodeAvlTree() throws Exception {
        AvlTreeEntity node = new AvlTreeEntity();
        node.setValue(10);
        node.setHeight(1);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        AvlTreeEntity savedNode = repository.findById(node.getId()).orElse(null);
        assertNotNull(savedNode);
        assertEquals(node.getValue(), savedNode.getValue());
    }

    @Test
    public void testDeleteNodeAvlTree() throws Exception {
        // Create a new node
        AvlTreeEntity node = new AvlTreeEntity();
        node.setValue(20);
        node.setHeight(1);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        repository.delete(node);

        AvlTreeEntity deletedNode = repository.findById(node.getId()).orElse(null);
        assertNull(deletedNode);
    }

    @Test
    public void testFindNodeById() throws Exception {
        AvlTreeEntity node = new AvlTreeEntity();
        node.setValue(30);
        node.setHeight(1);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        AvlTreeEntity foundNode = repository.findById(node.getId()).orElse(null);

        assertNotNull(foundNode);
        assertEquals(node.getValue(), foundNode.getValue());
    }

    @Test
    public void findAllNodes() throws Exception {
        AvlTreeEntity node1 = new AvlTreeEntity();
        node1.setValue(40);
        node1.setHeight(1);
        node1.setLeft_id(null);
        node1.setRight_id(null);

        AvlTreeEntity node2 = new AvlTreeEntity();
        node2.setValue(50);
        node2.setHeight(1);
        node2.setLeft_id(null);
        node2.setRight_id(null);

        repository.save(node1);
        repository.save(node2);

        Iterable<AvlTreeEntity> nodes = repository.findAll();

        assertNotNull(nodes);
        assertTrue(nodes.iterator().hasNext());
    }

    @Test
    public void updateNode() throws Exception {
        AvlTreeEntity node = new AvlTreeEntity();
        node.setValue(60);
        node.setHeight(1);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        node.setValue(70);
        repository.save(node);

        AvlTreeEntity updatedNode = repository.findById(node.getId()).orElse(null);

        assertNotNull(updatedNode);
        assertEquals(70, updatedNode.getValue());
    }
}
