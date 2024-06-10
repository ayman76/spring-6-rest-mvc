package com.example.spring6restmvc.mappers;

import com.example.spring6restmvc.entitiy.Beer;
import com.example.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDTO dto);
}
