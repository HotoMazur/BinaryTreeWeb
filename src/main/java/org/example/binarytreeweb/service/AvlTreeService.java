package org.example.binarytreeweb.service;

import org.example.binarytreeweb.binarytree.AVLTreeImpl;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AvlTreeService {
    private final AvlTreeRepository repository;
    private final AVLTreeImpl<Integer> binaryTree;

    public AvlTreeService(AvlTreeRepository repository, AVLTreeImpl<Integer> binaryTree) {
        this.repository = repository;
        this.binaryTree = binaryTree;
    }

    @Transactional(readOnly = true)
    public List<AvlTreeEntity> getAllNodes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public AvlTreeEntity getNodeById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public AvlTreeEntity addNode(Integer value) {
        return binaryTree.insertNode(value);
    }

    public void deleteNode(Integer value) {
        binaryTree.deleteNode(value);
    }

    public AvlTreeEntity updateNode(UUID id, Integer value) {
        return binaryTree.updateNode(id, value);
    }
}
