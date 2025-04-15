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
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        RedBlackTreeEntity savedNode = repository.findById(node.getId()).orElse(null);
        assertNotNull(savedNode);
        assertEquals(node.getValue(), savedNode.getValue());
    }

    @Test
    public void testDeleteNodeRBTree() throws Exception {
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        repository.delete(node);

        RedBlackTreeEntity deletedNode = repository.findById(node.getId()).orElse(null);
        assertNull(deletedNode);
    }

    @Test
    public void testFindNodeById() throws Exception {
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        RedBlackTreeEntity foundNode = repository.findById(node.getId()).orElse(null);

        assertNotNull(foundNode);
        assertEquals(node.getValue(), foundNode.getValue());
    }

    @Test
    public void findAllNodes() throws Exception {
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

        Iterable<RedBlackTreeEntity> nodes = repository.findAll();

        assertNotNull(nodes);
        assertTrue(nodes.iterator().hasNext());
    }

    @Test
    public void updateNode() throws Exception {
        RedBlackTreeEntity node = new RedBlackTreeEntity();
        node.setValue(10);
        node.setColor("BLACK");
        node.setParent_id(null);
        node.setLeft_id(null);
        node.setRight_id(null);

        repository.save(node);

        node.setValue(20);
        repository.save(node);

        RedBlackTreeEntity updatedNode = repository.findById(node.getId()).orElse(null);

        assertNotNull(updatedNode);
        assertEquals(20, updatedNode.getValue());
    }

}
