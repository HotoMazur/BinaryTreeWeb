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
