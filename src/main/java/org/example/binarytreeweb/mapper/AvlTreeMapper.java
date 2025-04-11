package org.example.binarytreeweb.mapper;

import org.example.binarytreeweb.dto.AvlTreeDto;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.springframework.stereotype.Component;

@Component
public class AvlTreeMapper {
    public AvlTreeDto AvlTreeEntityToDto(AvlTreeEntity avlTreeEntity) {
        return new AvlTreeDto(avlTreeEntity.getId(),
                avlTreeEntity.getValue(),
                avlTreeEntity.getHeight(),
                avlTreeEntity.getLeft_id() != null ? avlTreeEntity.getLeft_id() : null,
                avlTreeEntity.getRight_id() != null ? avlTreeEntity.getRight_id() : null);
    }
}
