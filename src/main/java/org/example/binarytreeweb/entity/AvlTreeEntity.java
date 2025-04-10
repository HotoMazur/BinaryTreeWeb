package org.example.binarytreeweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "avl_tree_nodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvlTreeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer value;
    private Integer height;
    private UUID left_id;
    private UUID right_id;

    public AvlTreeEntity(Integer value) {
        this.value = value;
    }
}
