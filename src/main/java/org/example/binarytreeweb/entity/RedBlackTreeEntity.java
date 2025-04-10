package org.example.binarytreeweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "red_black_tree_nodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedBlackTreeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer value;
    private String color;
    private UUID parent_id;
    private UUID left_id;
    private UUID right_id;

    public RedBlackTreeEntity(Integer value) {
        this.value = value;
    }
}
