package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entitiy.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
