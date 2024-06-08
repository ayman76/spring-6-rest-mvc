package com.example.spring6restmvc.service.impl;

import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.service.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Get beer by id in service was called with id: " + id);
        return beerMap.get(id);
    }

    @Override
    public Beer saveBeer(Beer beer) {
        Beer newBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .version(beer.getVersion())
                .build();

        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public void updateById(UUID beerId, Beer beer) {
        Beer existingBeer = getBeerById(beerId);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setVersion(beer.getVersion());
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeer.getId(), existingBeer);
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void updateBeerPatchById(UUID beerId, Beer beer) {
        Beer existingBeer = getBeerById(beerId);

        if (StringUtils.hasText(beer.getBeerName())){
            existingBeer.setBeerName(beer.getBeerName());
        }
        if(StringUtils.hasText(beer.getUpc())){
            existingBeer.setUpc(beer.getUpc());
        }
        if(beer.getBeerStyle() != null){
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }
        if(beer.getPrice() != null){
            existingBeer.setPrice(beer.getPrice());
        }
        if(beer.getQuantityOnHand() != null){
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if(beer.getVersion() != null){
            existingBeer.setVersion(beer.getVersion());
        }
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeer.getId(), existingBeer);
    }
}
