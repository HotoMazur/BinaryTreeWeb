package org.example.binarytreeweb.service;


import org.example.binarytreeweb.binarytree.RedBlackTreeImpl;
import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.example.binarytreeweb.repository.RedBlackTreeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RedBlackTreeService {
    private final RedBlackTreeRepository repository;
    private final RedBlackTreeImpl<Integer> binaryTree;

    public RedBlackTreeService(RedBlackTreeRepository repository, RedBlackTreeImpl<Integer> binaryTree) {
        this.repository = repository;
        this.binaryTree = binaryTree;
    }

    @Transactional(readOnly = true)
    public List<RedBlackTreeEntity> getAllNodes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public RedBlackTreeEntity getNodeById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public RedBlackTreeEntity addNode(Integer value) {
        return binaryTree.insertNode(value);
    }

    public void deleteNode(Integer value) {
        binaryTree.deleteNode(value);
    }
}
