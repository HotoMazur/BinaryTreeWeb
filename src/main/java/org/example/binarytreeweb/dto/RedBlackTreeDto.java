package org.example.binarytreeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedBlackTreeDto {
    private UUID id;
    private Integer value;
    private String color;
    private UUID parent_id;
    private UUID left_id;
    private UUID right_id;

    public RedBlackTreeDto(Integer value) {
        this.value = value;
    }
}
