package org.example.binarytreeweb.repository;

import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvlTreeRepository extends JpaRepository<AvlTreeEntity, UUID> {
    @Modifying
    @Query("UPDATE AvlTreeEntity a SET a.left_id = NULL WHERE a.left_id = :nodeId")
    void updateLeftChildToNull(@Param("nodeId") UUID nodeId);

    @Modifying
    @Query("UPDATE AvlTreeEntity a SET a.right_id = NULL WHERE a.right_id = :nodeId")
    void updateRightChildToNull(@Param("nodeId") UUID nodeId);

    @Query("SELECT a FROM AvlTreeEntity a WHERE a.height = (SELECT MIN(b.height) FROM AvlTreeEntity b)")
    Optional<AvlTreeEntity> findRootNode();
}
