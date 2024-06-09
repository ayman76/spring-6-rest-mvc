package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.model.Customer;
import com.example.spring6restmvc.service.BeerService;
import com.example.spring6restmvc.service.impl.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor = ArgumentCaptor.forClass(Beer.class);

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }


    @Test
    void testUpdateBeerPatch() throws Exception{
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerPatchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception{
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer)));

        verify(beerService).updateById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testCreateBeer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setId(null);
        testBeer.setVersion(null);

        given(beerService.saveBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListBeers() throws Exception {
        List<Beer> beers = beerServiceImpl.listBeers();
        given(beerService.listBeers()).willReturn(beers);

        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beers.size())));
    }

    @Test
    void testGetBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}