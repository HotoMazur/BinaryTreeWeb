package org.example.binarytreeweb.repository;

import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RedBlackTreeRepository extends JpaRepository<RedBlackTreeEntity, UUID> {
    @Query("SELECT r FROM RedBlackTreeEntity r WHERE r.parent_id IS NULL")
    Optional<RedBlackTreeEntity> findRootNode();
}
