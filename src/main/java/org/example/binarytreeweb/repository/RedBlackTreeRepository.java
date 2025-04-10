package org.example.binarytreeweb.repository;

import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RedBlackTreeRepository extends JpaRepository<RedBlackTreeEntity, UUID> {
}
