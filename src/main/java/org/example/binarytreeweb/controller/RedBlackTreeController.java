package org.example.binarytreeweb.controller;

import org.example.binarytreeweb.dto.RedBlackTreeDto;
import org.example.binarytreeweb.entity.RedBlackTreeEntity;
import org.example.binarytreeweb.mapper.RedBlackTreeMapper;
import org.example.binarytreeweb.service.RedBlackTreeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rb")
public class RedBlackTreeController {
    private final RedBlackTreeService redBlackTreeService;
    private final RedBlackTreeMapper redBlackTreeMapper;

    public RedBlackTreeController(RedBlackTreeService redBlackTreeService, RedBlackTreeMapper redBlackTreeMapper) {
        this.redBlackTreeService = redBlackTreeService;
        this.redBlackTreeMapper = redBlackTreeMapper;
    }

    @GetMapping
    public List<RedBlackTreeDto> getAllNodes() {
        List<RedBlackTreeEntity> allNodes = redBlackTreeService.getAllNodes();
        return allNodes.stream()
                .map(redBlackTreeMapper::redBlackTreeEntityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RedBlackTreeDto getNodeById(@PathVariable UUID id) {
        return redBlackTreeMapper.redBlackTreeEntityToDto(redBlackTreeService.getNodeById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RedBlackTreeDto addNode(@RequestBody RedBlackTreeDto redBlackTreeDto) {
        return redBlackTreeMapper.redBlackTreeEntityToDto(redBlackTreeService.addNode(redBlackTreeDto.getValue()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteNode(@RequestBody RedBlackTreeDto redBlackTreeDto) {
        redBlackTreeService.deleteNode(redBlackTreeDto.getValue());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public RedBlackTreeDto updateNode(@PathVariable UUID id, @RequestBody RedBlackTreeDto redBlackTreeDto) {
        return redBlackTreeMapper.redBlackTreeEntityToDto(redBlackTreeService.updateNode(id, redBlackTreeDto.getValue()));
    }
}
