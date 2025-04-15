package org.example.binarytreeweb.integration.controller;

import org.example.binarytreeweb.dto.AvlTreeDto;
import org.example.binarytreeweb.entity.AvlTreeEntity;
import org.example.binarytreeweb.mapper.AvlTreeMapper;
import org.example.binarytreeweb.mapper.RedBlackTreeMapper;
import org.example.binarytreeweb.repository.AvlTreeRepository;
import org.example.binarytreeweb.service.AuthenticationService;
import org.example.binarytreeweb.service.AvlTreeService;
import org.example.binarytreeweb.service.JwtService;
import org.example.binarytreeweb.service.RedBlackTreeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class AVlTreeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvlTreeService avlTreeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AvlTreeRepository avlTreeRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private AvlTreeMapper avlTreeMapper;

    @MockitoBean
    private RedBlackTreeService redBlackTreeService;

    @MockitoBean
    private RedBlackTreeMapper redBlackTreeMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAllAvlTree() throws Exception {
        AvlTreeEntity entity = new AvlTreeEntity(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null);
        when(avlTreeService.getAllNodes()).thenReturn(
                List.of(entity)
        );

        when(avlTreeMapper.AvlTreeEntityToDto(entity))
                .thenReturn(new AvlTreeDto(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null));
        // Mock the repository call and perform the request
        mockMvc.perform(get("/api/v1/avl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", Matchers.is("6f011b14-44b7-45f7-8e48-7d5db12e9f7a")))
                .andExpect(jsonPath("$[0].value", Matchers.is(21)))
                .andExpect(jsonPath("$[0].height", Matchers.is(1)));

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAvlTree() throws Exception {
        AvlTreeEntity entity = new AvlTreeEntity(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null);
        when(avlTreeService.getNodeById(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"))).thenReturn(
                entity
        );

        when(avlTreeMapper.AvlTreeEntityToDto(entity))
                .thenReturn(new AvlTreeDto(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null));
        mockMvc.perform(get("/api/v1/avl/6f011b14-44b7-45f7-8e48-7d5db12e9f7a"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", Matchers.is("6f011b14-44b7-45f7-8e48-7d5db12e9f7a")))
                .andExpect(jsonPath("$.value", Matchers.is(21)))
                .andExpect(jsonPath("$.height", Matchers.is(1)));

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddNodeAvlTree() throws Exception {
        AvlTreeEntity entity = new AvlTreeEntity(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null);
        when(avlTreeService.addNode(21)).thenReturn(entity);

        when(avlTreeMapper.AvlTreeEntityToDto(entity))
                .thenReturn(new AvlTreeDto(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null));

        mockMvc.perform(post("/api/v1/avl")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                    "value": 21
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", Matchers.is("6f011b14-44b7-45f7-8e48-7d5db12e9f7a")))
                .andExpect(jsonPath("$.value", Matchers.is(21)))
                .andExpect(jsonPath("$.height", Matchers.is(1)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteNodeAvlTree() throws Exception {
        AvlTreeEntity entity = new AvlTreeEntity(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null);

        mockMvc.perform(delete("/api/v1/avl")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                    "value": 21
                                }
                                """))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateNodeAvlTree() throws Exception {
        AvlTreeEntity entity = new AvlTreeEntity(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null);
        when(avlTreeService.updateNode(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21)).thenReturn(entity);

        when(avlTreeMapper.AvlTreeEntityToDto(entity))
                .thenReturn(new AvlTreeDto(UUID.fromString("6f011b14-44b7-45f7-8e48-7d5db12e9f7a"), 21, 1, null, null));

        mockMvc.perform(put("/api/v1/avl/6f011b14-44b7-45f7-8e48-7d5db12e9f7a")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                    "id": "6f011b14-44b7-45f7-8e48-7d5db12e9f7a",
                                    "value": 21,
                                    "height": 1,
                                    "left_id": null,
                                    "right_id": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", Matchers.is("6f011b14-44b7-45f7-8e48-7d5db12e9f7a")))
                .andExpect(jsonPath("$.value", Matchers.is(21)))
                .andExpect(jsonPath("$.height", Matchers.is(1)));
    }
}