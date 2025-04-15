package org.example.binarytreeweb.controller;

import org.example.binarytreeweb.dto.AvlTreeDto;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.mapper.AvlTreeMapper;
import org.example.binarytreeweb.service.AvlTreeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/avl")
public class AvlTreeController {
    private final AvlTreeService avlTreeService;
    private final AvlTreeMapper avlTreeMapper;

    public AvlTreeController(AvlTreeService avlTreeService, AvlTreeMapper avlTreeMapper) {
        this.avlTreeService = avlTreeService;
        this.avlTreeMapper = avlTreeMapper;
    }

    @GetMapping
    public List<AvlTreeDto> getAllNodes() {
        List<AvlTreeEntity> allNodes = avlTreeService.getAllNodes();
        return allNodes.stream()
                .map(avlTreeMapper::AvlTreeEntityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AvlTreeDto getNodeById(@PathVariable UUID id) {
        return avlTreeMapper.AvlTreeEntityToDto(avlTreeService.getNodeById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AvlTreeDto addNode(@RequestBody AvlTreeDto avlTreeDto) {
        return avlTreeMapper.AvlTreeEntityToDto(avlTreeService.addNode(avlTreeDto.getValue()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteNode(@RequestBody AvlTreeDto avlTreeDto) {
        avlTreeService.deleteNode(avlTreeDto.getValue());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public AvlTreeDto updateNode(@PathVariable UUID id, @RequestBody AvlTreeDto avlTreeDto) {
        return avlTreeMapper.AvlTreeEntityToDto(avlTreeService.updateNode(id, avlTreeDto.getValue()));
    }
}
