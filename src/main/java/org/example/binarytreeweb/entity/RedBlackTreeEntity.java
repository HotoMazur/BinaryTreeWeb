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
    @Column(name = "node_value", unique = true)
    private Integer value;
    @Column(name = "color")
    private String color;
    @Column(name = "parent_id")
    private UUID parent_id;
    @Column(name = "left_id")
    private UUID left_id;
    @Column(name = "right_id")
    private UUID right_id;

    public RedBlackTreeEntity(Integer value) {
        this.value = value;
    }
}
