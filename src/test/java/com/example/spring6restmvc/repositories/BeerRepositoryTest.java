package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entitiy.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSavedBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("New Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("123123123")
                .price(new BigDecimal("11.99")).build());

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSavedBeerTooLongBeerName() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("New Beer 123123123123123123123123123123123123123123123123123123123123123123123123123123123")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123123123")
                    .price(new BigDecimal("11.99")).build());

            beerRepository.flush();
        });
    }

    @Test
    void testSavedBeerWithNullValues() {

        assertThrows(ConstraintViolationException.class ,()  ->{
            Beer savedBeer = beerRepository.save(Beer.builder().build());

            beerRepository.flush();
        });
    }
}