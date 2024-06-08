package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PatchMapping("{beerId}")
    public ResponseEntity updateBeerPatchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerPatchById(beerId, beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + beerId);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
        beerService.deleteBeerById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{beerId}")
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){

        beerService.updateById(beerId, beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + beerId);

        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity saveBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveBeer(beer);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity(responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id in controller was called");
        return beerService.getBeerById(beerId);
    }
}
