package com.example.spring6restmvc.service.impl;

import com.example.spring6restmvc.exception.NotFoundException;
import com.example.spring6restmvc.model.BeerDTO;
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
    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get beer by id in service was called with id: " + id);
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beerDTO) {
        BeerDTO newBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .version(beerDTO.getVersion())
                .build();

        beerMap.put(newBeerDTO.getId(), newBeerDTO);
        return newBeerDTO;
    }

    @Override
    public void updateById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = getBeerById(beerId).orElseThrow(NotFoundException::new);
        existingBeerDTO.setBeerName(beerDTO.getBeerName());
        existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        existingBeerDTO.setUpc(beerDTO.getUpc());
        existingBeerDTO.setPrice(beerDTO.getPrice());
        existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existingBeerDTO.setVersion(beerDTO.getVersion());
        existingBeerDTO.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeerDTO.getId(), existingBeerDTO);
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void updateBeerPatchById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existingBeerDTO = getBeerById(beerId).orElseThrow(NotFoundException::new);

        if (StringUtils.hasText(beerDTO.getBeerName())){
            existingBeerDTO.setBeerName(beerDTO.getBeerName());
        }
        if(StringUtils.hasText(beerDTO.getUpc())){
            existingBeerDTO.setUpc(beerDTO.getUpc());
        }
        if(beerDTO.getBeerStyle() != null){
            existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        }
        if(beerDTO.getPrice() != null){
            existingBeerDTO.setPrice(beerDTO.getPrice());
        }
        if(beerDTO.getQuantityOnHand() != null){
            existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }
        if(beerDTO.getVersion() != null){
            existingBeerDTO.setVersion(beerDTO.getVersion());
        }
        existingBeerDTO.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeerDTO.getId(), existingBeerDTO);
    }
}
