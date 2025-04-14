package org.example.binarytreeweb.dto;

import lombok.*;

import java.util.UUID;

@Data
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
