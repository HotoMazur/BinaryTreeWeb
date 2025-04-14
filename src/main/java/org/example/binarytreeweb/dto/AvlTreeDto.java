package org.example.binarytreeweb.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvlTreeDto {
    private UUID id;
    private Integer value;
    private Integer height;
    private UUID left_id;
    private UUID right_id;

    public AvlTreeDto(Integer value) {
        this.value = value;
    }
}
