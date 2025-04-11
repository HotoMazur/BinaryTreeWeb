package org.example.binarytreeweb.mapper;

import org.example.binarytreeweb.dto.RedBlackTreeDto;
import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.springframework.stereotype.Component;

@Component
public class RedBlackTreeMapper {
    public RedBlackTreeDto redBlackTreeEntityToDto(RedBlackTreeEntity redBlackTreeEntity) {
        return new RedBlackTreeDto(redBlackTreeEntity.getId(),
                redBlackTreeEntity.getValue(),
                redBlackTreeEntity.getColor(),
                redBlackTreeEntity.getParent_id() != null ? redBlackTreeEntity.getParent_id() : null,
                redBlackTreeEntity.getLeft_id() != null ? redBlackTreeEntity.getLeft_id() : null,
                redBlackTreeEntity.getRight_id() != null ? redBlackTreeEntity.getRight_id() : null);
    }
}
