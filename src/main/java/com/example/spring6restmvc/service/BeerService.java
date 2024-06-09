package com.example.spring6restmvc.service;

import com.example.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveBeer(Beer beer);

    void updateById(UUID beerId, Beer beer);

    void deleteBeerById(UUID beerId);

    void updateBeerPatchById(UUID beerId, Beer beer);
}
