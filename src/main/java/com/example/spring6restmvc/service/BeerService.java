package com.example.spring6restmvc.service;

import com.example.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateById(UUID beerId, BeerDTO beerDTO);

    void deleteBeerById(UUID beerId);

    void updateBeerPatchById(UUID beerId, BeerDTO beerDTO);
}
